package gramaticas;

import java.util.Arrays;
import java.util.List;

public class Produccion {

    /*
     * Representación del caracter epsilon
     */
    public static final String EPSILON = "\u03B5";
    /*
     * Lado izquierdo de la producción
     */
    private String ladoIzquierdo;
    /*
     * Lado derecho de la producción
     */
    private String ladoDerecho;

    /**
     * Construye una producción a partir de su lado izquierdo y su lado derecho.
     * @param izq El lado izquierdo de la producción.
     * @param der El lado derecho de la producción.
     */
    public Produccion(String izq, String der) {
        this(izq + "->" + der);
    }

    /**
     * Construye una producción a partir de un string en el formato predefinido.
     * El formato de una producción es: A -> w con A una variable y w un string
     * de variables y terminales.
     * @param produccion String en formato de producción.
     */
    public Produccion(String produccion) {
        if (!produccion.contains("->")) {
            errorDeFormato(produccion);
        }
        String partes[] = produccion.split("->");
        if (partes.length != 2) {
            errorDeFormato(produccion);
        }
        verificarLadoIzquierdo(partes[0].trim());
        verificarLadoDerecho(partes[1].trim());
        inicializar(partes[0].trim(), partes[1].trim());
    }
    
    private void errorDeFormato(String produccion) {
        throw new IllegalArgumentException("La producción no está en"
                + " un formato correcto.\nFormato = "
                + "<símbolo> -> <símbolos>\nString leido = " + produccion);
    }
  
    private void verificarLadoIzquierdo(String izq) {
        if (izq.isEmpty()) {
            throw new IllegalArgumentException("El lado izquierdo no puede "
                    + "ser vacío.");
        }
        if (izq.length() > 1) {
            throw new IllegalArgumentException("El lado izquierdo debe ser "
                    + "solo una variable.\nLado izquierdo " + izq);
        }
        if (!izq.matches("[A-Z]"))
            throw new IllegalArgumentException("El lado izquierdo no puede "
                    + "ser un terminal.");
    }
    
    private void verificarLadoDerecho(String der) {
        if (der.isEmpty()) {
            throw new IllegalArgumentException("El lado derecho no puede ser"
                    + "vacío.");
        }
    }
    
    private void inicializar(String izq, String der) {
        this.ladoIzquierdo = izq;
        this.ladoDerecho   = der;
    }
    
    public String getLadoIzquierdo() {
        return this.ladoIzquierdo;
    }
    
    public String getLadoDerecho() {
        return this.ladoDerecho;
    }

    /**
     * Indica si el string del parámetro representa un terminal.
     * Los terminales son letras de la a a la z en minúscula.
     * @param a Un String para verificar que representa un terminal.
     * @return true si a es representa un terminal, false si no.
     */
    public static boolean esTerminal(String a) {
        return !esVariable(a);
    }

    /**
     * Indica si el string del parámetro representa una variable.
     * Las variables son letras de la a a la z en mayúscula.
     * @param a Un String para verificar que represente una variable.
     * @return true si a representa una variable, false si no.
     */
    public static boolean esVariable(String a) {
        return a.matches("[A-Z]");
    }

    /**
     * Indica si this es una producción unitaria. Una producción unitaria es
     * una producción cuyo lado derecho consiste de una sola variable.
     * @return true si el lado derecho de esta producción consiste de una sola
     * variable, false si no.
     */
    public boolean esUnitaria() {
        return Produccion.esVariable(getLadoDerecho());
    }

    /**
     * Devuelve una lista con los strings que pueden ser aceptados como 
     * variables por una producción según el enunciado (letras del alfabeto en
     * mayúscula). 
     * @return Una lista con los strings que pueden ser usados como variables.
     */
    public static List<String> variablesAceptadas() {
        return Arrays.asList("A","B","C","D","E","F","G","H","I","J","K","L",
                "M","N","Ñ","O","P","Q","R","S","T","U","V","W","X","Y","Z");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ladoDerecho.hashCode();
        result = prime * result + ladoIzquierdo.hashCode();
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
        if (!ladoIzquierdo.equals(other.ladoIzquierdo))
            return false;
        if (!ladoDerecho.equals(other.ladoDerecho))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return ladoIzquierdo + " -> " + ladoDerecho;
    }
}
