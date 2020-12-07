package parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.stream.Collectors;

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
		this.gramatica.agregarProduccion(new Produccion(izq, ladoDerecho));
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
	
	public static void main(String[] args) {
		String test = "abc";
		System.out.println(test.charAt(0));
	}
}
