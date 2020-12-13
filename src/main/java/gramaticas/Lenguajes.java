package gramaticas;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;

/**
 * Representa un manipulador de lenguajes.
 * Tiene métodos para saber si un string pertenece a un lenguaje.
 *
 */
public class Lenguajes {

    /**
     * Indica si el string w pertenece a la gramática g usando el algorítmo
     * CYK.
     * @param g Un string
     * @param w Una gramática en FNC
     * @return true si w pertenece a L(g), false si no
     */
    public boolean CYK(Gramatica g, String w) {
        Map<Indices, Set<Character>> triangulo = new HashMap<Indices, Set<Character>>(w.length() * w.length());
        Collection<Produccion> producciones = ImmutableSet.copyOf(g.getProducciones());

        // Lleno el triángulo con null
        for (int i = 0; i < w.length(); i++) {
            triangulo.put(Indices.get(i, i), null);
        }

        // Caso base: X_{ii} = {A|A->a_i es una producción}
        for(int i = 0; i < w.length(); i++) {
            Set<Character> X_ii = new HashSet<>();
            Character a_i = w.charAt(i);
            producciones
              .stream()
              .filter(p -> p.getLadoDerecho().equals(String.valueOf(a_i)))
              .map(p -> p.getLadoIzquierdo())
              .map(var -> var.charAt(0))
              .forEach(X_ii::add);
            triangulo.put(Indices.get(i, i), X_ii);
        }

        // Caso inductivo: X_{ij} = {A|existe A->BC y un entero k con i <= k < j
        // tal que B está en X_{ik} y C está en X_{(k+1)k}}
        int n = 1;
        while (n < w.length()) {
            int i = 0;
            int j = i + n;
            while (j < w.length()) {
                // Calculo una fila
                for (int k = i; k < j; k++) {
                    // Calculo un X_{ij}
                    Set<Character> X_ij = new HashSet<>();
                    for(Character B : triangulo.get(Indices.get(i, k))) {
                        for(Character C : triangulo.get(Indices.get(k+1, j))) {
                            String ladoDerecho = Joiner.on("").join(B, C);
                            producciones.stream()
                                .filter(p -> p.getLadoDerecho().equals(ladoDerecho))
                                .map(p -> p.getLadoIzquierdo())
                                .map(var -> var.charAt(0))
                                .forEach(X_ij::add);
                        }
                    }
                    triangulo.put(Indices.get(i, j), X_ij);
                }
                i++;
                j++;
            }
            n++;
        }

        // verificamos que S esté en X_{1n}
        return triangulo.get(Indices.get(0, w.length()-1)) != null
                && triangulo.get(Indices.get(0, w.length()-1)).contains('S');
    }

    private static class Indices {
        int i;
        int j;

        public static Indices get(int i, int j) {
            return new Indices(i, j);
        }

        public Indices(int i, int j) {
            this.i = i;
            this.j = j;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + j;
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
            Indices other = (Indices) obj;
            if (j != other.j)
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "(" + i + "," + j + ")";
        }
    }
}
