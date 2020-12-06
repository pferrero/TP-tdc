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
	
	public ParserLR0(Gramatica gramatica)
	{
		this.gramatica = gramatica;
	}
	
	public HashSet<Item> getItems() 
	{   if (this.items == null)
    {
        this.items = new HashSet<Item>();
        
        Produccion firstProd = this.gramatica.getProd_inicial();
        
        this.items.add(new Item(firstProd, 0));//S' -> .S
        this.items.add(new Item(firstProd, 1)); //S' -> S.
        
        Iterator<Produccion> producciones = this.gramatica.getProducciones().iterator();
        while(producciones.hasNext())
        {
        	Produccion prod = producciones.next();
            for (int i = 0; i < prod.getLadoDerecho().size(); i++)
            {
                this.items.add(new Item(prod, i));
            }
        }
    }
    	return this.items;
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
		ReadFile reader = new ReadFile();
		Validator validator = new Validator();
		reader.assingFile(filePath);
		for(int i = 0; i < reader.linesNumber(); i++) 
		{
			String linea = reader.getLine(i);
			if(!validator.isValid(linea, Produccion.EXP_PRODUCCION))
				throw new InputMismatchException("Error al leer el archivo. Produccion invalida");
			this.gramatica.agregarProduccion(new Produccion(linea));
		}
	}
	
	//accion()
	//reduce()
	//shift()
	
	public static void main(String[] args) {
		String test = "abc";
		System.out.println(test.charAt(0));
		
		ArrayList<String> lista = new ArrayList<String>();
		
		lista.add("X{23");
		lista.add("X02");
		lista.add("X03");
		lista.add("X05");
		lista.add("X13");
		lista.add("X01");
		lista.add("X10");
		lista.add("X04");
		lista.add("X0");
		lista.add("X12");
		lista.add("X6");
		lista.add("X8");
		lista.add("X7");
		
		System.out.println(lista);
	}
	

	
}
