package parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Stack;
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
		this.items.add(new Item(firstProd, 1)); //S' -> S.$
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
                Simbolo ctr = itemClausura.GetCaracterLadoDerecho();//E -> T. E -> .T
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
    
    public HashSet<Item> IrA(HashSet<Item> items, Simbolo caracter)
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
		ArrayList<Simbolo> ladoDerecho = new ArrayList<Simbolo>();
		for(String simbolo : validator.getAllMatchs(der, Produccion.EXP_SIMBOLO)) {
			TipoSimbolo tipoSimbolo = null; 
			if (Produccion.esTerminal(simbolo)) 
			{
				tipoSimbolo = TipoSimbolo.Terminal;
	        } 
			else 
			{
				tipoSimbolo = TipoSimbolo.Variable;
			}
			 
			ladoDerecho.add(new Simbolo(simbolo, tipoSimbolo));
		}
		Produccion prod = new Produccion(izq, ladoDerecho);
		this.gramatica.agregarProduccion(prod);
		if(izq.equals("X_{1}") && this.gramatica.getProd_inicial() == null)
			this.gramatica.setProd_inicial(prod);
	}

	/// Crea la coleccion elementos o estados LR(0), junto con sus acciones: (shift, goto, acept, reduce)
    public HashSet<ConjuntoItem> LR()
    {
    	HashSet<ConjuntoItem> resultado = new HashSet<ConjuntoItem>();
    	//aumento la gramatica
    	ArrayList<Simbolo> variableInicial = new ArrayList<Simbolo>();
    	variableInicial.add(this.gramatica.getProd_inicial().getLadoIzquierdo());
    	variableInicial.add(new Simbolo("$", TipoSimbolo.EndOfFile));
    	Produccion nuevaProdInicial = new Produccion("X_{0}",variableInicial);
    	this.gramatica.agregarProduccion(nuevaProdInicial);
    	this.gramatica.setProd_inicial(nuevaProdInicial);
    	
        Stack<ConjuntoItem> T = new Stack<ConjuntoItem>(); //Conjunto de estados del AFD
        Item inicial = getFirstItem(); //Tomo el primer item de los que se generaron
        System.out.println(this.getItems());
        HashSet<Item> clausura = new HashSet<Item>();
        clausura.add(inicial);
        clausura = clausuraItem(clausura); //hago la clausura de todos los items
        int stateIndex = 0;
        ConjuntoItem estadoInicial = new ConjuntoItem(clausura, stateIndex);
        T.add(estadoInicial); //agrego el estado inicial
        stateIndex++;
        resultado.add(estadoInicial);
        
        
        while (!T.empty())
        {
        	ConjuntoItem I = T.pop();
            System.out.println(I.getItems());

            for(Item item : I.getItems())
            {
                Simbolo X = item.GetCaracterLadoDerecho();
                //si es vacio
				if (X != null && !X.getSimbolo().equals(""))
                {//si es el ultimo caracter
                    if (X.getTipo() != TipoSimbolo.EndOfFile )
                    {
                        ConjuntoItem estado = new ConjuntoItem(IrA(I.getItems(), X), stateIndex);
                        //me fijo si hay un estado con esos items
                        if (existeEstado(resultado,estado)) {
                        	estado = getEstado(resultado,estado);
                        	}
                        else { 
                        	T.add(estado);
                        	resultado.add(estado);
                        	stateIndex++;
                        }
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
        HashSet<ConjuntoItem> estados = agregarEstados(LRReduce(resultado));
        return estados;
    }
    
    private boolean existeEstado(HashSet<ConjuntoItem> resultado, ConjuntoItem estado) {
		for(ConjuntoItem ci : resultado) {
			if(ci.getItems().equals(estado.getItems()))
				return true;
		}
		return false;
	}


	private HashSet<ConjuntoItem> agregarEstados(HashSet<ConjuntoItem> lrReduce) {
		HashSet<ConjuntoItem> estados = new HashSet<>();
		for(ConjuntoItem item : lrReduce) {
			estados.add(item);
		}
		return estados;
	}

	private Item getFirstItem() {
		Item first = null;
		for(Item item : this.getItems()) {
			if(item.getProduccion().equals(getGramatica().getProd_inicial())) {
				first = item;
			}
		}
		System.out.println("Primer item: " + first);
		return first;
	}
    
    private ConjuntoItem getEstado(HashSet<ConjuntoItem> estados, ConjuntoItem estado) {
    	ConjuntoItem estadoOutput = null;
    	for(ConjuntoItem e : estados) {
    		if(e.getItems().equals(estado.getItems()))
    			estadoOutput = e;
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
                if (A.IsEndPosition()) {
                    
                	for(Simbolo terminal : this.gramatica.getTerminales()) {
                		I.agregarAccion(terminal, new TablaAccion(TipoTablaAccion.Reducir, null, A.getProduccion()));
                	}
            		I.agregarAccion(new Simbolo("$",TipoSimbolo.EndOfFile), new TablaAccion(TipoTablaAccion.Reducir, null, A.getProduccion()));
                }
            }
        }
        return T;
    }
    
    public void parserLR(String w) throws ParseException {
    	String parserString = w;
    	HashSet<ConjuntoItem> estados = this.LR();
    	ArrayList<ConjuntoItem> pilaEstados = new ArrayList<ConjuntoItem>();
    	pilaEstados.add(getEstadoInicial(estados));

    	while(!pilaEstados.isEmpty()) {
    		ConjuntoItem estado = pilaEstados.get(pilaEstados.size()-1);
        	Simbolo caracterActual = new Simbolo(String.valueOf(parserString.charAt(0)),TipoSimbolo.Terminal);
        	
    		if(estado.getAcciones().get(caracterActual).getTipo() != null && estado.getAcciones().get(caracterActual).getTipo() == TipoTablaAccion.Desplazar)
        	{
    			//shift
    			shift(pilaEstados, estado, caracterActual);
            	parserString = parserString.substring(1);
        	}
        	else if(estado.getAcciones().get(caracterActual).getTipo() != null && estado.getAcciones().get(caracterActual).getTipo() == TipoTablaAccion.Reducir)
        	//vuelvo a donde indica
        	{
        		reduce(pilaEstados, estado, caracterActual);
        	}
        	else if(estado.getAcciones().get(caracterActual).getTipo() != null && estado.getAcciones().get(caracterActual).getTipo() == TipoTablaAccion.Aceptar) 
        	{
        		//termino;
        		System.out.println("El string '" + w +"' fue reconocido exitosamente por la gramatica");
        		return;
        	}
       		else
       			error(estado.getId(),caracterActual.getSimbolo());
    	}
    }

	private ConjuntoItem getEstadoInicial(HashSet<ConjuntoItem> estados) {
		ConjuntoItem estadoInicial = null;
		for(ConjuntoItem CI : estados) {
			if(CI.getId() == 0) {
				estadoInicial = CI;
			}
		}
		return estadoInicial;
	}

	private void shift(ArrayList<ConjuntoItem> pilaEstados, ConjuntoItem estado, Simbolo caracterActual) {
		pilaEstados.add(estado.getAcciones().get(caracterActual).getDestino());
	}

	private void reduce(ArrayList<ConjuntoItem> pilaEstados, ConjuntoItem estado, Simbolo caracterActual) {
		Produccion prodAReducir = estado.getAcciones().get(caracterActual).getReduccion();
		//elimino estados por la longitud del lado derecho de la produccion
		int cantCaracteres = prodAReducir.getLadoDerecho().size();
		for (int i = 0; i< cantCaracteres;i++) {
			pilaEstados.remove(pilaEstados.size()-1);
		}
		//Ir a sobre la variable del lado izquierdo
		Simbolo variable = prodAReducir.getLadoIzquierdo();
		estado = pilaEstados.get(pilaEstados.size()-1);//me quedo con el ultimo estado luego de reducir
		shift(pilaEstados, estado, variable);
	}
    
    private void error(int estado, String simbolo) throws ParseException 
    {
    	throw new ParseException("Error de parsing. No se encontraron acciones para el caracter '" 
    							+ simbolo + "' en el estado [" + estado + "].");
	}
    
    public Gramatica getGramatica() {
    	return this.gramatica;
    }
  	
}
