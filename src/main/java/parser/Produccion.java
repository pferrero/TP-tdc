package parser;

import java.util.List;


public class Produccion {

	private Variable       ladoIzquierdo;
    private List<Caracter> ladoDerecho;
    public static final String EXP_PRODUCCION = "(([X][_][{])\\d+[}]\\s?->\\s?((([X][_][{])\\d+[}])|[a-z])+)";
    public static final String EXP_SIMBOLO = "(([X][_][{])\\\\d+[}])|[a-z])";
    
    public Produccion(String produccion) {
    	
        // X_{1} -> X_{4}abX_{3}d
    }
    
    public Produccion(String izq, List<Caracter> der) 
    {
    	this.ladoIzquierdo = new Variable(izq);
    	this.ladoDerecho = der;
    }
    
    public Variable getLadoIzquierdo() 
    {
    	return this.ladoIzquierdo;
    }
    
    public List<Caracter> getLadoDerecho() 
    {
    	return this.ladoDerecho;
    }
    
    public static boolean esTerminal(String a) {
        return a.matches("[a-z]");
    }
    
    public static boolean esVariable(String a) {
        return a.matches("(([X][_][{])\\\\d+[}])");
    }

    public boolean esUnitaria() {
    	return this.ladoDerecho.size() == 1 && esVariable(this.ladoDerecho.get(0).getSimbolo());
    }
    
    @Override
    public String toString() {
    	StringBuilder ladoDerechoString = new StringBuilder();
    	this.ladoDerecho.forEach(caracter -> ladoDerechoString.append(caracter.getSimbolo()));
    	return this.ladoIzquierdo.getSimbolo() + " -> " + ladoDerechoString.toString();
    }
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ladoDerecho == null) ? 0 : ladoDerecho.hashCode());
		result = prime * result + ((ladoIzquierdo == null) ? 0 : ladoIzquierdo.hashCode());
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
		Produccion other = (Produccion) obj;
		if (ladoDerecho == null) {
			if (other.ladoDerecho != null)
				return false;
		} else if (!ladoDerecho.equals(other.ladoDerecho))
			return false;
		if (ladoIzquierdo == null) {
			if (other.ladoIzquierdo != null)
				return false;
		} else if (!ladoIzquierdo.equals(other.ladoIzquierdo))
			return false;
		return true;
	}
}
