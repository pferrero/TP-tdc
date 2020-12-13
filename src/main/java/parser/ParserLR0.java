package parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.cli.ParseException;

import Utilities.ReadFile;
import Utilities.Validator;

public class ParserLR0 {
	
	private Gramatica gramatica;
	private HashSet<Item> items;
	private ReadFile reader = new ReadFile();
	private Validator validator = new Validator();
	
	public ParserLR0(Gramatica gramatica)
	{
		this.gramatica = gramatica;
	}
	
	public HashSet<Item> getItems() 
	{   
		if (this.items == null)
			generarItems();
		return this.items;
	}

	private void generarItems() {
		this.items = new HashSet<Item>();
		Produccion firstProd = this.gramatica.getProd_inicial();
		this.items.add(new Item(firstProd, 0));//S' -> .S
		this.items.add(new Item(firstProd, 1)); //S' -> S.
		Iterator<Produccion> producciones = this.gramatica.getProducciones().iterator();
		while(producciones.hasNext())
		{
			Produccion prod = producciones.next();
			for (int i = 0; i < prod.getLadoDerecho().size(); i++)
				this.items.add(new Item(prod, i));
		}
	}
	
    private HashSet<Item> clausuraItem(HashSet<Item> items)
    {
        HashSet<Item> clausura = new HashSet<Item>(); 
        //preparo el conjunto de items output
        //por cada item 
        for (Item item : items)
        {
            clausura.add(item);
            for(Item itemClausura : clausura)
            {
                Caracter ctr = itemClausura.GetCaracterLadoDerecho();//E -> T. E -> .T
                if (ctr != null)
                {
                    HashSet<Item> temp = (HashSet<Item>) this.getItems()
                    		.stream()
                    		.filter(i -> i.getProduccion().getLadoIzquierdo().getSimbolo().equals(ctr.getSimbolo()) && i.getPosicion() == 0)
                    		.collect(Collectors.toSet());
                    clausura.addAll(temp);
                }
            }
        }
        return clausura;
    }
    
    public HashSet<Item> IrA(HashSet<Item> items, Caracter caracter)
    {
    	HashSet<Item> conjunto = new HashSet<Item>();

    	for(Item item : items)
    	{
    		if (item.GetCaracterLadoDerecho().equals(caracter))
    		{
    			conjunto.add(new Item(item.getProduccion(), item.getPosicion() + 1));
    		}
    	}
    	return clausuraItem(conjunto);
    }
    
	public void generarGramatica(String filePath) 
	{
		this.reader.assingFile(filePath);
		for(int i = 0; i < this.reader.linesNumber(); i++) 
		{
			String linea = this.reader.getLine(i);
			if(!this.validator.isValid(linea, Produccion.EXP_PRODUCCION))
				throw new InputMismatchException("Error al leer el archivo. Produccion invalida");
			generarProduccion(linea);
		}
	}

	private void generarProduccion(String linea) {
		String[] lados = linea.split("->");
		String izq = lados[0].trim().replaceAll("(\\s)", "");  
		String der = lados[1].trim().replaceAll("(\\s)", "");
		ArrayList<Caracter> ladoDerecho = new ArrayList<Caracter>();
		for(String simbolo : validator.getAllMatchs(der, Produccion.EXP_SIMBOLO)) {
			ladoDerecho.add(new Terminal(simbolo));
		}
		Produccion prod = new Produccion(izq, ladoDerecho);
		this.gramatica.agregarProduccion(prod);
		if(izq.equals("X_{1}"))
			this.gramatica.setProd_inicial(prod);
	}

	/// Crea la coleccion elementos o estados LR(0), junto con sus acciones: (shift, goto, acept, reduce)
    public HashSet<ConjuntoItem> LR()
    {
        HashSet<ConjuntoItem> T = new HashSet<ConjuntoItem>(); //Conjunto de estados del AFD
        Item inicial = this.getItems().stream().findFirst().get(); //Tomo el primer item de los que se generaron
        HashSet<Item> clausura = new HashSet<Item>();
        clausura.add(inicial);
        clausura = clausuraItem(clausura); //hago la clausura de todos los items
        int stateIndex = 0;
        T.add(new ConjuntoItem(clausura, stateIndex)); //agrego el estado inicial
        for (ConjuntoItem I : T)
        {
            for(Item item : I.getItems())
            {
                Caracter X = item.GetCaracterLadoDerecho();
                //si es vacio
				if (X != null && !X.getSimbolo().equals(""))
                {//si es el ultimo caracter
                    if (X != item.getProduccion().getLadoDerecho().get(item.getProduccion().getLadoDerecho().size()-1))
                    {
                        ConjuntoItem estado = new ConjuntoItem(IrA(I.getItems(), X), stateIndex);
                        //agrego el estado
                        if (!T.add(estado))
                        	estado = getEstado(T,estado);
                        else stateIndex++;
                        //agrego las transiciones del estado
                        if(Produccion.esVariable(X.getSimbolo()) && !I.getAcciones().containsKey(X))
                        	I.agregarAccion(X, TablaAccion.IrA(estado));
                        else if(Produccion.esTerminal(X.getSimbolo()) && !I.getAcciones().containsKey(X))
                        	I.agregarAccion(X, TablaAccion.Shift(estado));
                    }
                    else 
                    { 
                    	I.agregarAccion(X, new TablaAccion(TipoTablaAccion.Aceptar, null, null)); 
                    }
                }
            }
        }
        return LRReduce(T);
    }
    
    private ConjuntoItem getEstado(HashSet<ConjuntoItem> estados, ConjuntoItem estado) {
    	ConjuntoItem estadoOutput = null;
    	for(ConjuntoItem e : estados) {
    		if(e.equals(estado))
    			estadoOutput = estado;
    	}
    	return estadoOutput;
    }
    
    /// Calcula el conjunto de acciones de reduccion LR(0)
    private HashSet<ConjuntoItem> LRReduce(HashSet<ConjuntoItem> T)
    {
        for(ConjuntoItem I : T)
        {
            for(Item A : I.getItems())
            {
                if (A.IsEndPosition())
                    I.agregarAccion(new Terminal(""), new TablaAccion(TipoTablaAccion.Reducir, null, A.getProduccion()));
            }
        }
        return T;
    }
    
    public void parserLR(String w) throws ParseException {
    	String parserString = w;
    	HashSet<ConjuntoItem> estados = this.LR();
    	Stack<ConjuntoItem> pilaEstados = new Stack<ConjuntoItem>();
    	pilaEstados.add(estados.stream().findFirst().get());
    	
    	while(!pilaEstados.isEmpty()) {
    		ConjuntoItem estado = pilaEstados.pop();
        	Caracter caracterActual = new Terminal(String.valueOf(parserString.charAt(0)));
        	
    		if(estado.getAcciones().get(caracterActual).getTipo() == TipoTablaAccion.Desplazar)
        	{
        		shift(pilaEstados, estado, caracterActual);
            	parserString.substring(1);
        	}
        	else if(estado.getAcciones().get(caracterActual).getTipo() == TipoTablaAccion.Reducir)
        	//vuelvo a donde indica
        	{
        		reduce(pilaEstados, estado, caracterActual); 
        	}
        	else if(estado.getAcciones().get(caracterActual).getTipo() == TipoTablaAccion.Aceptar) 
        	{
        		//termino;
        		System.out.println("El string '" + w +"' fue reconocido exitosamente por la gramatica");
        		return;
        	}
       		else
       			error(estado.getId(),caracterActual.getSimbolo());
    	}
    }

	private void shift(Stack<ConjuntoItem> pilaEstados, ConjuntoItem estado, Caracter caracterActual) {
		pilaEstados.add(estado.getAcciones().get(caracterActual).getDestino());
	}

	private void reduce(Stack<ConjuntoItem> pilaEstados, ConjuntoItem estado, Caracter caracterActual) {
		Produccion prodAReducir = estado.getAcciones().get(caracterActual).getReduccion();
		//elimino estados por la longitud del lado derecho de la produccion
		int cantCaracteres = prodAReducir.getLadoDerecho().size();
		for (int i = 0; i< cantCaracteres;i++) {
			pilaEstados.pop();
		}
		//Ir a sobre la variable del lado izquierdo
		Caracter variable = prodAReducir.getLadoIzquierdo();
		shift(pilaEstados, estado, variable);
	}
    
    private void error(int estado, String simbolo) throws ParseException 
    {
    	throw new ParseException("Error de parsing. No se encontraron acciones para el caracter '" 
    							+ simbolo + "' en el estado [" + estado + "].");
	}

	public static void main(String[] args) {
    	
//    	String filePath  = "C:\\Users\\Administrator\\Desktop\\ArchivosTest\\ArchivoGramaticaTest.txt";
//		Gramatica gramatica = new Gramatica();
//		ParserLR0 parserlr0 = new ParserLR0(gramatica);
//		parserlr0.generarGramatica(filePath);
		
//		String EXP_PRDUCCION = "((([X][_][{])\\d+[}])|[a-z])";
//		String exp = "X_{2}";
//		Pattern pat = Pattern.compile(EXP_PRDUCCION);
//		Matcher mat = pat.matcher(exp);
//		System.out.println(mat.matches());
    }
}
