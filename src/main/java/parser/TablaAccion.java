package parser;

enum TipoTablaAccion{ Desplazar, IrA, Reducir, Aceptar}

public class TablaAccion 
{
	private Produccion reduccion;
	private TipoTablaAccion tipo;
	
	private TablaSimbolo destino;
	
	public TablaAccion(TipoTablaAccion tipo, TablaSimbolo destino, Produccion reduccion)
	{
		this.destino = destino;
		this.tipo = tipo;
		this.reduccion = reduccion;
	}

	public static TablaAccion Shift(TablaSimbolo destino)
	{
		return new TablaAccion(TipoTablaAccion.Desplazar, destino, null);
	}

	public static TablaAccion IrA(TablaSimbolo destino)
	{
		return new TablaAccion(TipoTablaAccion.IrA, destino, null);
	}
	
	public Produccion getReduccion() {
		return reduccion;
	}

	public void setReduccion(Produccion reduccion) {
		this.reduccion = reduccion;
	}

	public TipoTablaAccion getTipo() {
		return tipo;
	}

	public void setTipo(TipoTablaAccion tipo) {
		this.tipo = tipo;
	}

	public TablaSimbolo getDestino() {
		return destino;
	}

	public void setDestino(TablaSimbolo destino) {
		this.destino = destino;
	}
}
