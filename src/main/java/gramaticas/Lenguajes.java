package gramaticas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableSet;

public class Lenguajes {

    public boolean CYK(Gramatica g, String w) {
        Map<Indices, List<Character>> triangulo = new HashMap<Indices, List<Character>>(w.length() * w.length());
        Collection<Produccion> producciones = ImmutableSet.copyOf(g.getProducciones());

        // Lleno el triángulo con null
        for (int i = 0; i < w.length(); i++) {
            triangulo.put(Indices.get(i, i), null);
        }

        // Caso base: X_{ii} = {A|A->a_i es una producción}
        for(int i = 0; i < w.length(); i++) {
            List<Character> X_ii = new ArrayList<>();
            Character a_i = w.charAt(i);
            producciones
              .stream()
              .filter(p -> p.getLadoDerecho().equals(String.valueOf(a_i)))
              .map(p -> p.getLadoIzquierdo())
              .map(var -> var.charAt(0))
              .forEach(X_ii::add);
            triangulo.put(Indices.get(i, i), X_ii);
        }

        // Caso inductivo:
        int n = 1;
        while (n < w.length()) {
            int i = 0;
            int j = i + n;
            while (j < w.length()) {
                // Calculo una fila
                System.out.println("Fila: " + Indices.get(i, j));
                i++;
                j++;
            }
            n++;
            System.out.println("----");
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
