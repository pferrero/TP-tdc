package gramaticas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Representa una gramática. Se puede crear una gramática vacía e ir 
 * extendiendola. Para extender la gramática se agregan las producciones una por
 * una. Los terminales y las variables son las que están presentes en las
 * producciones que se van agregando a la gramática.
 * @author Pablo
 *
 */
public class Gramatica {

    private Set<String>  terminales;
    private Set<String>  variables;
    private Set<Produccion> producciones;

    /**
     * Crea una gramática vacía.
     */
    public Gramatica() {
        this.producciones = new HashSet<Produccion>();
        this.terminales   = new HashSet<>();
        this.variables    = new HashSet<>();
    }

    /**
     * Agrega una producción a esta gramática.
     * @param prod La producción para agregar a la gramática.
     */
    public void agregarProduccion(Produccion prod) {
        this.variables.add(prod.getLadoIzquierdo());
        for (int i = 0; i < prod.getLadoDerecho().length(); i++) {
            char caracter = prod.getLadoDerecho().charAt(i);
            if (Produccion.esTerminal(String.valueOf(caracter))) {
                terminales.add(String.valueOf(caracter));
            } else {
                variables.add(String.valueOf(caracter));
            }
        }
        producciones.add(prod);
    }

    /**
     * Devuelve el conjunto de variables nulleables en esta gramática.
     * Una variable nulleable es una variable que deriva en cero o más pasos a
     * epsilon.
     * @return Un conjunto de variables nulleables presentes en esta gramática.
     */
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

    /**
     * Devuelve una colección de símbolos generadores.
     * Los símbolos generadores son símbolos (terminales o variables) que
     * derivan en cero o más pasos a un string compuesto únicamente de 
     * terminales.
     * @return Una colección con los símbolos generadores de esta gramática.
     */
    public Collection<String> getSimbolosGeneradores(){
        Collection<String> ret = new HashSet<>();
        /*
         * Caso base: Agrego los símbolos terminales al conjunto
         * de símbolos generadores.
         */
        terminales.forEach(ret::add);
        /*
         * Caso inductivo: Si existe A->alfa donde alfa esté comformada por
         * símbolos en ret, entonces agrego A a ret.
         */
        boolean continuar = true;
        while (continuar) {
            int cant = ret.size();
            producciones.forEach( p -> {
                boolean agregar = true;
                for(char c : p.getLadoDerecho().toCharArray()) {
                    agregar = agregar && ret.contains(String.valueOf(c));
                }
                if (agregar)
                    ret.add(p.getLadoIzquierdo());
            });
            if (ret.size() > cant)
                continuar = true;
            else
                continuar = false;
        }
        return ret;
    }

    /**
     * Devuelve una colección de símbolos alcanzables.
     * Los símbolos alcanzables son los símbolos que pueden ser alcanzados
     * derivando cero o más pasos a S (el símbolo inicial).
     * @return Una colección con los símbolos alcanzables de esta gramática.
     */
    public Collection<String> getSimbolosAlcanzables() {
        Collection<String> alcanzables = new HashSet<>();
        // Caso base: se puede alcanzar el símbolo inicial S
        alcanzables.add("S");
        // Caso inductivo: Si A es alcanzable y existe A->alfa, entonces
        // podemos alcanzar todos los símbolos de alfa.
        boolean continuar = true;
        Collection<String> alcanzablesNuevos = new HashSet<>();
        while (continuar) {
            alcanzables.forEach(var -> {
                if (Produccion.esVariable(var)) {
                    getProduccionesDeVariable(var)
                      .stream()
                      .map(p -> p.getLadoDerecho())
                      .forEach(ld -> {
                          for (char c : ld.toCharArray())
                              alcanzablesNuevos.add(String.valueOf(c));
                      });
                }
            });
            continuar = alcanzables.addAll(alcanzablesNuevos);
            alcanzablesNuevos.clear();
        }
        return alcanzables;
    }

    /**
     * Devuelve una colección con la lista de variables de la gramática.
     * @return La colección de variables de esta gramática.
     */
    public Collection<String> getVariables(){
        return new HashSet<String>(this.variables);
    }

    /**
     * Devuelve una colección con la lista de producciones de la gramática.
     * @return La colección de producciones de esta gramática.
     */
    public Collection<Produccion> getProducciones() {
        return new HashSet<Produccion>(this.producciones);
    }

    /**
     * Devuelve una colección con la lista de terminales de la gramática.
     * @return La colección de terminales de esta gramática.
     */
    public Collection<String> getTerminales(){
        return new HashSet<>(this.terminales);
    }

    /**
     * Devuelve una colección con las producciones unitarias de la gramática.
     * Una producción unitaria es una producción cuyo lado derecho consiste
     * únicamente de una variable.
     * @return La colección de producciones unitarias de esta gramática.
     */
    public Collection<Produccion> getProduccionesUnitarias() {
        return this.producciones
                .stream()
                .filter(Produccion::esUnitaria)
                .collect(Collectors.toSet());
    }

    /**
     * Devuelve una colección de las producciones de la variable var.
     * Si la variable no pertenece a la gramática devuelve una colección vacía.
     * @param var Una variable.
     * @return Una colección de producciones de var.
     */
    public Collection<Produccion> getProduccionesDeVariable(String var) {
        return this.producciones
                .stream().filter(p -> p.getLadoIzquierdo().equals(var))
                .collect(Collectors.toSet());
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
