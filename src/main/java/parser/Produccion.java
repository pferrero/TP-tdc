package parser;

import java.util.List;

public class Produccion {

    private Variable       ladoIzquierdo;
    private List<Caracter> ladoDerecho;
    public static final String EXP_PRODUCCION = "(\\X_{\\d+}\\s->\\s((\\X_{\\d+})|[a-z])+)";
    
    public Produccion(String produccion) {
        // Expresión regular que parsee 
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
        return a.matches("(\\X_{\\d+})");
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
}
