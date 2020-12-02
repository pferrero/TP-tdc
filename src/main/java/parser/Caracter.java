package parser;

public abstract class Caracter {
	
	private String simbolo;
	
	public Caracter(String simbolo) 
	{
		this.simbolo = simbolo;
	}
	
	protected String getSimbolo() {
		return this.simbolo;
	}

}
