package gramaticas;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class LimpiadorDeGramaticas {

    public Gramatica eliminarProduccionesEpsilon(Gramatica g) {
        Gramatica g1 = new Gramatica();        
        Set<String> nulleables = Collections.unmodifiableSet(g.variablesNulleables());
        g.getProducciones()
            .stream()
            .filter(A -> !A.getLadoDerecho().equals(Produccion.EPSILON))
            .flatMap(A -> dosAlaMVersiones(nulleables, A).stream())
            .forEach(g1::agregarProduccion);
        return g1;
    }

    /*
     * Devuelve un conjunto con 2^m versiones de la producción prod.
     * m es la cantidad de símbolos nulleables.
     */
    private Set<Produccion> dosAlaMVersiones(Collection<String> nulleables, Produccion prod) {
        Set<Produccion> ret = new HashSet<>();
        String simbolosNulleables = getNulleablesEnElString(nulleables, prod.getLadoDerecho());
        if (simbolosNulleables.length() == 0) {
            ret.add(prod);
            return ret;
        }
        String subconjuntos[] = getSubconjuntosDeString(simbolosNulleables);
        for (String subconjunto : subconjuntos) {
            StringBuilder ladoDerecho = new StringBuilder();
            for (char simbolo : prod.getLadoDerecho().toCharArray()) {
                if (Produccion.esTerminal(String.valueOf(simbolo)))
                    ladoDerecho.append(simbolo);
                else if (subconjunto.indexOf(simbolo) != -1)
                    ladoDerecho.append(simbolo);
            }
            if (!ladoDerecho.toString().isEmpty())
                ret.add(new Produccion(prod.getLadoIzquierdo() + "->" + ladoDerecho));
        }
        return ret;
    }
    
    /*
     * retorna un string con las variables nulleables del string s, siendo
     * nulleables las de la colección nulleables.
     */
    private String getNulleablesEnElString(Collection<String> nulleables, String s) {
        StringBuilder sb = new StringBuilder();
        for(char c : s.toCharArray())
            if(nulleables.contains(String.valueOf(c)))
                sb.append(String.valueOf(c));
        return sb.toString();
    }
    
    /*
     * retorna un arreglo con todos los subconjuntos posibles que se obtienen
     * tomando o no cada símbolo del string símbolos.
     * Ej:
     * simbolos = ABC
     * return = [A,B,C,AB,AC,BC,ABC,'']
     */
    private String[] getSubconjuntosDeString(String simbolos) {
        String[] ret = new String[1<<simbolos.length()];        
        for (int i = 0; i < (1<<simbolos.length()); i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < simbolos.length(); j++)   
                if ((i & (1 << j)) > 0)
                    sb.append(simbolos.charAt(j));
            ret[i] = sb.toString();
        }        
        return ret;
    }
}
