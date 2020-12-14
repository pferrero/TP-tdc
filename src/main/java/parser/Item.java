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
	
	
	public Simbolo GetCaracterLadoDerecho()
	{
		if (this.posicion >= this.produccion.getLadoDerecho().size())
			return null;

		return this.produccion.getLadoDerecho().get(posicion);
	}

	public Simbolo GetCaracterLadoIzquierdo()
	{
		if (this.posicion >= this.produccion.getLadoDerecho().size() + 1 || this.posicion == 0)
			return null;

		return this.produccion.getLadoDerecho().get(this.posicion - 1);
	}

	public boolean IsEndPosition()
	{
		return this.posicion == this.produccion.getLadoDerecho().size();
	}

	public Simbolo GetLastSymbol()
	{
		return this.produccion.getLadoDerecho().get(this.produccion.getLadoDerecho().size()-1);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + posicion;
		result = prime * result + ((produccion == null) ? 0 : produccion.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (posicion != other.posicion)
			return false;
		if (produccion == null) {
			if (other.produccion != null)
				return false;
		} else if (!produccion.equals(other.produccion))
			return false;
		return true;
	}
	
	@Override
	public String toString()
	{
		return this.getProduccion().toString() + ". Posicion del item: " + this.posicion + "\n"; 
	}
	
}
