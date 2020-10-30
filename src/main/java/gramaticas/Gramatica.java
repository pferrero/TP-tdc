package gramaticas;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Gramatica {

    private Set<Character>  terminales;
    private Set<Character>  variables;
    private Set<Produccion> producciones;
    private Character       varInicial;
    
    public Gramatica() {
        this.varInicial = 'S';
        inicializarConjuntos();
    }
    
    private void inicializarConjuntos() {
        this.producciones = new HashSet<Produccion>();
        this.terminales   = new HashSet<>();
        this.variables    = new HashSet<>();        
    }
    
    public void agregarProduccion(Produccion prod) {
        this.variables.add(prod.getLadoIzquierdo().charAt(0));
        for (int i = 0; i < prod.getLadoDerecho().length(); i++) {
            char caracter = prod.getLadoDerecho().charAt(i);
            // Agregar método Produccion.esTerminal()
            // Character.isLowerCase(char) va a dar false con algunos terminales
            // como { o ()
            if (Character.isLowerCase(caracter)) {
                terminales.add(caracter);
            } else {
                variables.add(caracter);
            }
        }
        producciones.add(prod);
    }
    
    public Set<String> variablesNulleables() {
        Set<String> nulleables = new HashSet<>();
        // Caso base: Son nulleables las variables que tienen una producción
        // A -> \epsilon
        nulleables.addAll(producciones
                .stream()
                .filter(p -> p.getLadoDerecho().equals(Produccion.EPSILON))
                .map(Produccion::getLadoIzquierdo)
                .collect(Collectors.toSet()) );
        return nulleables;
    }
    /*
    public boolean esNulleable(char varA) {
        if (!variables.contains(varA)) {
            throw new IllegalArgumentException("La variable " + varA + 
                    " no pertenece a la gramática.");
        }
        return verificarNulleable(varA);
    }
    
    private boolean verificarNulleable(char varA) {
        List<Produccion> produccionesDeVarA = producciones.stream()
                .filter(p -> p.getLadoIzquierdo().equals(varA))
                .collect(Collectors.toList());
        
        for (Produccion p : produccionesDeVarA) {
            
        }
    }*/
}
