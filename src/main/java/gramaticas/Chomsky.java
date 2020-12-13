package gramaticas;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Stream;

import com.google.common.base.CharMatcher;

/**
 * Representa un conversor de gramáticas a forma normal de Chomsky.
 *
 */
public class Chomsky {

    LimpiadorDeGramaticas ldg = new LimpiadorDeGramaticas();

    /**
     * Convierte la gramática g a FNC.
     * @param g Una gramática
     * @return Una gramática equivalente a g en FNC.
     */
    public Gramatica FNC(Gramatica g) {
        return Stream.of(g)
        // Paso 1: se limpia la gramática
        .map(ldg::limpiarGramatica)
        // Paso 2: por cada lado derecho que no sea terminal, hacer que todo el
        // lado derecho esté compuesto de variables.
        .map(this::reemplazarTerminales)
        // Paso 3: descomponer los lados derechos de longitud mayor a 2 en una
        // cadena de producciones con lados derechos de dos variables.
        .map(this::reemplazarLadosDerechos)
        .findFirst().get();
    }

    Gramatica reemplazarTerminales(Gramatica g) {
        Gramatica g1 = new Gramatica();
        Stack<Character> variablesDisponibles = new Stack<>();
        Stack<Character> terminales = new Stack<>();
        Map<Character, Character> terminalesVariables = new HashMap<>();
        
        Produccion.variablesAceptadas()
                .stream()
                .filter(var -> !g.getVariables().contains(var))
                .map(var -> var.charAt(0))
                .forEach(variablesDisponibles::push);

        g.getTerminales().stream().map(term -> term.charAt(0)).forEach(terminales::push);

        // Creo las nuevas producciones A_a -> a para cada terminal
        while (!terminales.isEmpty()) {
            char variable = variablesDisponibles.pop();
            char terminal = terminales.pop();
            g1.agregarProduccion(new Produccion(String.valueOf(variable), String.valueOf(terminal)));
            terminalesVariables.put(terminal, variable);
        }
        // Reemplazo las producciones existentes en lados derechos de long >= 2
        for (Produccion p : g.getProducciones()) {
            if (p.getLadoDerecho().length() == 1) {
                g1.agregarProduccion(p);
            } else {
                String ladoDerecho = p.getLadoDerecho();
                for (char terminal : terminalesVariables.keySet()) {
                    ladoDerecho = CharMatcher.is(terminal).
                            replaceFrom(ladoDerecho, terminalesVariables.get(terminal));
                }
                g1.agregarProduccion(new Produccion(p.getLadoIzquierdo(), ladoDerecho));
            }
        }
        return g1;
    }

    Gramatica reemplazarLadosDerechos(Gramatica g) {
        Gramatica g1 = new Gramatica();
        Stack<String> variablesDisponibles = new Stack<>();

        Produccion.variablesAceptadas()
                .stream()
                .filter(var -> !g.getVariables().contains(var))
                .forEach(variablesDisponibles::push);

        g.getProducciones()
          .stream()
          // Si el lado derecho es de largo 1 directamente se agrega a g1
          .map(p -> {
              if (p.getLadoDerecho().length() < 2)
                  g1.agregarProduccion(p);
              return p;
          })
          // Se filtra y se quedan solo los lados derechos mayores o iguales a 2.
          .filter(p -> p.getLadoDerecho().length() >= 2)
          // Descompone lados derechos mayores a 2 y el resto lo deja igual.
          // Luego agrega las producciones descompuestas a g1.
          .forEach(p -> {
            Queue<String> varQueue = new LinkedList<>();
            String ladoIzquierdo = p.getLadoIzquierdo();
            String ladoDerecho = p.getLadoDerecho();
            for (int i = 0; i < ladoDerecho.length(); i++) {
                varQueue.add(ladoDerecho.substring(i, i+1));
            }
            Produccion nuevaProduccion;
            while (varQueue.size() > 2) {
                String nuevaVariable = variablesDisponibles.pop();
                nuevaProduccion = new Produccion(ladoIzquierdo, varQueue.poll() + nuevaVariable);
                g1.agregarProduccion(nuevaProduccion);
                ladoIzquierdo = nuevaVariable;
            }
            nuevaProduccion = new Produccion(ladoIzquierdo, varQueue.poll() + varQueue.poll());
            g1.agregarProduccion(nuevaProduccion);
          });

        return g1;
    }
}
