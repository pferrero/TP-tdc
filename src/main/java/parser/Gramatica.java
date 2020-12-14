package parser;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Gramatica {

    private Set<Simbolo>  terminales;
    private Set<Simbolo>  variables;
    private Set<Produccion> producciones;
    private Produccion prod_inicial;
    
    public Gramatica() {
        inicializarConjuntos();
    }
    
    private void inicializarConjuntos() {
        this.producciones = new HashSet<Produccion>();
        this.terminales   = new HashSet<Simbolo>();
        this.variables    = new HashSet<Simbolo>();
    }
    
    public void agregarProduccion(Produccion prod) {
        this.variables.add(prod.getLadoIzquierdo());
        for (int i = 0; i < prod.getLadoDerecho().size(); i++) {
            Simbolo caracter = prod.getLadoDerecho().get(i);
            if (Produccion.esTerminal(caracter.getSimbolo())) {
                terminales.add(caracter);
            } else {
            	if(!variables.contains(caracter))
            		variables.add(caracter);
            }
        }
        producciones.add(prod);
    }
    
    //X_{4} -> X_{34}aX_{1}bcd
//    
//    public Set<String> variablesNulleables() {
//        Set<String> nulleables = new HashSet<>();
//        // Caso base: Son nulleables las variables que tienen una producción
//        // A -> \epsilon
//        nulleables.addAll(
//                producciones
//                .stream()
//                .filter(p -> p.getLadoDerecho().equals(Produccion.EPSILON))
//                .map(Produccion::getLadoIzquierdo)
//                .collect(Collectors.toSet()) );
//        // Caso inductivo: Si existe una producción A->\alpha y todos los
//        // símbolos de \alpha son nulleables entonces A es nulleable.
//        List<Produccion> noNulleables = new ArrayList<Produccion>(producciones);
//        noNulleables.removeIf(p -> nulleables.contains(p.getLadoIzquierdo()));
//        int i = 0;
//        while(i < noNulleables.size()) {
//            Produccion p = noNulleables.get(i);
//            if (esNulleable(nulleables, p.getLadoDerecho())) {
//                nulleables.add(p.getLadoIzquierdo());
//                noNulleables.removeIf(x -> x.getLadoIzquierdo()
//                                            .equals(p.getLadoIzquierdo()));
//                i = 0;
//            } else {
//                ++i;
//            }            
//        }
//        return nulleables;
//    }    
//    
    /*
     * Se fija si toda la cadena es nulleable, siendo nulleables las variables
     * que están en el parámetro nulleables.
     */
//    private boolean esNulleable(Collection<String> nulleables, String cadena) {
//        boolean hayTerminales = cadena
//                .codePoints()
//                .mapToObj(c -> String.valueOf((char) c))
//                .anyMatch(Produccion::esTerminal);
//        if (hayTerminales)
//            return false;
//        return cadena.codePoints()
//                .mapToObj(c -> String.valueOf((char) c))
//                .allMatch(nulleables::contains);
//    }

//    public Collection<String> getSimbolosGeneradores(){
//        Collection<String> ret = new HashSet<>();
//        /*
//         * Caso base: Agrego los símbolos terminales al conjunto
//         * de símbolos generadores.
//         */
//        terminales.forEach(ret::add);
//        /*
//         * Caso inductivo: Si existe A->alfa donde alfa esté comformada por
//         * símbolos en ret, entonces agrego A a ret.
//         */
//        boolean continuar = true;
//        while (continuar) {
//            int cant = ret.size();
//            producciones.forEach( p -> {
//                boolean agregar = true;
//                for(char c : p.getLadoDerecho().toCharArray()) {
//                    agregar = agregar && ret.contains(String.valueOf(c));
//                }
//                if (agregar)
//                    ret.add(p.getLadoIzquierdo());
//            });
//            if (ret.size() > cant)
//                continuar = true;
//            else
//                continuar = false;
//        }
//        return ret;
//    }

//    public Collection<String> getSimbolosAlcanzables() {
//        Collection<String> alcanzables = new HashSet<>();
//        // Caso base: se puede alcanzar el símbolo inicial S
//        alcanzables.add("S");
//        // Caso inductivo: Si A es alcanzable y existe A->alfa, entonces
//        // podemos alcanzar todos los símbolos de alfa.
//        boolean continuar = true;
//        Collection<String> alcanzablesNuevos = new HashSet<>();
//        while (continuar) {
//            alcanzables.forEach(var -> {
//                if (Produccion.esVariable(var)) {
//                    getProduccionesDeVariable(var)
//                      .stream()
//                      .map(p -> p.getLadoDerecho())
//                      .forEach(ld -> {
//                          for (char c : ld.toCharArray())
//                              alcanzablesNuevos.add(String.valueOf(c));
//                      });
//                }
//            });
//            continuar = alcanzables.addAll(alcanzablesNuevos);
//            alcanzablesNuevos.clear();
//        }
//        return alcanzables;
//    }

    public Collection<Simbolo> getVariables(){
        return new HashSet<Simbolo>(this.variables);
    }
    
    public Collection<Produccion> getProducciones() {
        return new HashSet<Produccion>(this.producciones);
    }

    public Collection<Simbolo> getTerminales(){
        return new HashSet<Simbolo>(this.terminales);
    }

    public Collection<Produccion> getProduccionesUnitarias() {
        return this.producciones
                .stream()
                .filter(Produccion::esUnitaria)
                .collect(Collectors.toSet());
    }
    
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

	public Produccion getProd_inicial() {
		return prod_inicial;
	}

	public void setProd_inicial(Produccion prod_inicial) {
		this.prod_inicial = prod_inicial;
	}
    
}
