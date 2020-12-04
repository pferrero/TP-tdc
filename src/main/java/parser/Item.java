package parser;

public class Item {
	
	private Produccion produccion;
	private int posicion;
	
	public Item(Produccion prod, int pos) 
	{
		this.produccion = prod;
		this.posicion = pos;
		
        if (pos < 0) 
        	this.posicion= 0;

        this.posicion = pos > produccion.getLadoDerecho().size() ? produccion.getLadoDerecho().size() : pos;
	}
	
	public Produccion getProduccion() 
	{
		return this.produccion;
	}
	
	public int getPosicion() 
	{
		return this.posicion;
	}
	
	
	public Caracter GetCaracterLadoDerecho()
	{
		if (this.posicion >= this.produccion.getLadoDerecho().size())
			return null;

		return this.produccion.getLadoDerecho().get(posicion);
	}

	public Caracter GetCaracterLadoIzquierdo()
	{
		if (this.posicion >= this.produccion.getLadoDerecho().size() + 1 || this.posicion == 0)
			return null;

		return this.produccion.getLadoDerecho().get(this.posicion - 1);
	}

	public boolean IsEndPosition()
	{
		return this.posicion == this.produccion.getLadoDerecho().size();
	}

	public Caracter GetLastSymbol()
	{
		return this.produccion.getLadoDerecho().get(this.produccion.getLadoDerecho().size()-1);
	}

}
