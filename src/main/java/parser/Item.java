package parser;

import gramaticas.Produccion;

public class Item {
	
	private Produccion produccion;
	private int posicion;
	
	public Item(Produccion prod, int pos) 
	{
		this.produccion = prod;
		this.posicion = pos;
		
        if (pos < 0) 
        	this.posicion= 0;

        this.posicion = pos > produccion.getLadoDerecho().length() ? produccion.getLadoDerecho().length() : pos;
	}
	
	   public Character GetCaracterLadoDerecho(Produccion prod)
       {
           if (this.posicion >= this.produccion.getLadoDerecho().length())
        	   return null;

           return this.produccion.getLadoDerecho().charAt(this.posicion);
       }
	   
	   public Character GetCaracterLadoIzquierdo(Produccion prod)
       {
           if (this.posicion >= this.produccion.getLadoDerecho().length() + 1 || this.posicion == 0)
        	   return null;

           return this.produccion.getLadoDerecho().charAt(this.posicion - 1);
       }
	   
	   public boolean IsEndPosition()
       {
           return this.posicion == this.produccion.getLadoDerecho().length();
       }
	
	   public Character GetLastSymbol()
       {
           return this.produccion.getLadoDerecho().charAt(this.produccion.getLadoDerecho().length()-1);
       }
	

}
