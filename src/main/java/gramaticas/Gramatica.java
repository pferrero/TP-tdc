package gramaticas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Gramatica {

    private Set<String>  terminales;
    private Set<String>  variables;
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
        this.variables.add(prod.getLadoIzquierdo());
        for (int i = 0; i < prod.getLadoDerecho().length(); i++) {
            char caracter = prod.getLadoDerecho().charAt(i);
            if (Produccion.esTerminal(String.valueOf(caracter))) {
                terminales.add(prod.getLadoDerecho());
            } else {
                variables.add(prod.getLadoDerecho());
            }
        }
        producciones.add(prod);
    }
    
    public Set<String> variablesNulleables() {
        Set<String> nulleables = new HashSet<>();
        // Caso base: Son nulleables las variables que tienen una producción
        // A -> \epsilon
        nulleables.addAll(
                producciones
                .stream()
                .filter(p -> p.getLadoDerecho().equals(Produccion.EPSILON))
                .map(Produccion::getLadoIzquierdo)
                .collect(Collectors.toSet()) );
        // Caso inductivo: Si existe una producción A->\alpha y todos los
        // símbolos de \alpha son nulleables entonces A es nulleable.
        List<Produccion> noNulleables = new ArrayList<Produccion>(producciones);
        noNulleables.removeIf(p -> nulleables.contains(p.getLadoIzquierdo()));
        int i = 0;
        while(i < noNulleables.size()) {
            Produccion p = noNulleables.get(i);
            if (esNulleable(nulleables, p.getLadoDerecho())) {
                nulleables.add(p.getLadoIzquierdo());
                noNulleables.removeIf(x -> x.getLadoIzquierdo()
                                            .equals(p.getLadoIzquierdo()));
                i = 0;
            } else {
                ++i;
            }            
        }
        return nulleables;
    }    
    
    /*
     * Se fija si toda la cadena es nulleable, siendo nulleables las variables
     * que están en el parámetro nulleables.
     */
    private boolean esNulleable(Collection<String> nulleables, String cadena) {
        boolean hayTerminales = cadena
                .codePoints()
                .mapToObj(c -> String.valueOf((char) c))
                .anyMatch(Produccion::esTerminal);
        if (hayTerminales)
            return false;
        return cadena.codePoints()
                .mapToObj(c -> String.valueOf((char) c))
                .allMatch(nulleables::contains);
    }

    public Collection<String> getVariables(){
        return new HashSet<String>(this.variables);
    }
    
    public Collection<Produccion> getProducciones() {
        return new HashSet<Produccion>(this.producciones);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + producciones.hashCode();
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
        Gramatica other = (Gramatica) obj;
        return producciones.equals(other.producciones);
    }
    
}
