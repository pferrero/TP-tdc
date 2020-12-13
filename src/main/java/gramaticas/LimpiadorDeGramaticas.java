package gramaticas;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Stream;

import com.google.common.base.CharMatcher;

public class LimpiadorDeGramaticas {

    /**
     * Elimina las producciones epsilon de la gramática g.
     * @param g Una gramática.
     * @return Una nueva gramática equivalente a g pero sin producciones epsilon
     */
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
        // Reemplazo las variables nulleables por si hay repetidas
        Map<String, String> repetidos = new HashMap<>();
        Stack<String> variablesDisponibles = new Stack<>();
        Produccion.variablesAceptadas()
                .stream()
                .filter(var -> !nulleables.contains(var))
                .forEach(variablesDisponibles::push);
        String ladoDerecho = prod.getLadoDerecho();
        for (char nulleable : simbolosNulleables.toCharArray()) {
             CharMatcher cm = CharMatcher.is(nulleable);
            while (cm.matchesAnyOf(ladoDerecho)) {
                String var = variablesDisponibles.pop();
                ladoDerecho = ladoDerecho.replaceFirst(String.valueOf(nulleable), var);
                repetidos.put(var, String.valueOf(nulleable));
            }
        }

        // Se crean las 2^m versiones
        String subconjuntos[] = getSubconjuntosDeString(ladoDerecho);
        for (String subconjunto : subconjuntos) {
            StringBuilder nuevoLadoDerecho = new StringBuilder();
            for (char simbolo : ladoDerecho.toCharArray()) {
                if (Produccion.esTerminal(String.valueOf(simbolo)))
                    nuevoLadoDerecho.append(simbolo);
                else if (!repetidos.containsKey(String.valueOf(simbolo))) // es var pero no nulleable
                    nuevoLadoDerecho.append(simbolo);
                else if (subconjunto.indexOf(simbolo) != -1)
                    nuevoLadoDerecho.append(simbolo);
            }
            if (!nuevoLadoDerecho.toString().isEmpty()) {
                // Se vuelven a reemplazar las variables nulleables por las originales
                // antes de agregar la produccion a ret.
                String aux = nuevoLadoDerecho.toString();
                for (char c : ladoDerecho.toCharArray()) {
                    if (repetidos.containsKey(String.valueOf(c)))
                        aux = CharMatcher.is(c).replaceFrom(aux, repetidos.get(String.valueOf(c)));
                }
                ret.add(new Produccion(prod.getLadoIzquierdo(), aux));
            }
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

    private class ParUnitario {
        private String varIzquierda;
        private String varDerecha;
        public ParUnitario(String izq, String der) {
            this.varIzquierda = izq;
            this.varDerecha   = der;
        }
        public String getVarIzquierda() {
            return varIzquierda;
        }
        public String getVarDerecha() {
            return varDerecha;
        }
    }

    /**
     * Elimina las producciones unitarias de la gramática g.
     * @param g Una gramática.
     * @return Una gramática equivalente a g sin producciones unitarias.
     */
    public Gramatica eliminarProduccionesUnitarias(Gramatica g) {
        Gramatica g1 = new Gramatica();
        Collection<ParUnitario> pares = getParesUnitarios(g);
        /* Para cada par unitario (A,B), agregar a P1 todas las
         * producciones A->alfa, donde B->alfa es una produccion no
         * unitaria en P.
         */
        pares.forEach( pu -> {
            g.getProducciones()
              .stream()
              .filter(p -> p.getLadoIzquierdo().equals(pu.getVarDerecha()))
              .filter(p -> !p.esUnitaria())
              .map(p -> new Produccion(pu.getVarIzquierda(), p.getLadoDerecho()))
              .forEach(g1::agregarProduccion);
        });
        return g1;
    }

    private Collection<ParUnitario> getParesUnitarios(Gramatica g) {
        Collection<ParUnitario> ret = new HashSet<>();
        Collection<Produccion> produccionesUnitarias = g.getProduccionesUnitarias();
        Queue<ParUnitario> puQueue = new LinkedList<ParUnitario>();
        //Caso base: Agrego los pares (A,A) para cada variable A.
        g.getVariables().stream()
               .forEach(A -> puQueue.add(new ParUnitario(A,A)));
        //Caso inductivo: Si encontramos (A,B) y B->C es unitaria, agregar (A,C)
        while (puQueue.size() != 0) {
            ParUnitario pu = puQueue.poll();
            produccionesUnitarias
                .stream()
                .filter(p -> p.getLadoIzquierdo().equals(pu.getVarDerecha()))
                .map(p -> new ParUnitario(pu.getVarIzquierda(), p.getLadoDerecho()))
                .forEach(puQueue::add);
            ret.add(pu);
        }
        return ret;
    }

    /**
     * Elimina los símbolos no generadores de la gramática g.
     * @param g Una gramática
     * @return Una gramática equivalente a g sin símbolos no generadores.
     */
    public Gramatica eliminarSimbolosNoGeneradores(Gramatica g) {
        Gramatica g1 = new Gramatica();
        Collection<String> simbolosGeneradores = g.getSimbolosGeneradores();
        g.getProducciones()
          .stream()
          .filter(p -> simbolosGeneradores.contains(p.getLadoIzquierdo()))
          .filter(p -> {
              boolean esGenerador = true;
              String ladoDerecho = p.getLadoDerecho();
              for (char c : ladoDerecho.toCharArray()) {
                  esGenerador = esGenerador && simbolosGeneradores.contains(String.valueOf(c));
              }
              return esGenerador;
          })
          .forEach(g1::agregarProduccion);
        return g1;
    }

    /**
     * Elimina los símbolos inalcanzables de g.
     * @param g Una gramática
     * @return Una gramática equivalente a g sin símbolos inalcanzables
     */
    public Gramatica eliminarSimbolosInalcanzables(Gramatica g) {
        Gramatica g1 = new Gramatica();
        Collection<String> simbolosAlcanzables = g.getSimbolosAlcanzables();
        g.getProducciones().stream()
        .filter(p -> simbolosAlcanzables.contains(p.getLadoIzquierdo()))
        .forEach(g1::agregarProduccion);
        return g1;
    }

    /**
     * Limpia la gramática g.
     * Limpiar la gramática es eliminar producciones epsilon, producciones 
     * unitarias, símbolos no generadores y símbolos inalcanzables.
     * @param g Una gramática
     * @return Una gramática equivalente a g limpia
     */
    public Gramatica limpiarGramatica(Gramatica g) {
        return Stream.of(g)
            .map(this::eliminarProduccionesEpsilon)
            .map(this::eliminarProduccionesUnitarias)
            .map(this::eliminarSimbolosNoGeneradores)
            .map(this::eliminarSimbolosInalcanzables)
            .findFirst().get();
    }
}
