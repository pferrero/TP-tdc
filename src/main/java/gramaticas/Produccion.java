package gramaticas;

public class Produccion {

    public static final Character EPSILON = '\u03B5';
    
    private String ladoIzquierdo;
    private String ladoDerecho;
    
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
