package automatas;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

public class AFDTest {

    /*
     * Construye y devuelve el autómata del punto 16 de la práctica 1
     */
    private Automata getAutomataPunto16() {
        Automata eafnd = new AFND_TransEpsilon().getAutomata();
        Estado estado1 = new Estado<Integer>(1);
        Estado estado2 = new Estado<Integer>(2);
        Estado estado3 = new Estado<Integer>(3);
        Transicion<String> transicion1 = new Transicion<String>(estado1, estado1, "a");
        Transicion<String> transicion2 = new Transicion<String>(estado1, estado2, "b");
        Transicion<String> transicion3 = new Transicion<String>(estado1, estado3, "c");
        
        Transicion<String> transicion4 = new Transicion<String>(estado2, estado1, "E");
        Transicion<String> transicion5 = new Transicion<String>(estado2, estado2, "a");
        Transicion<String> transicion6 = new Transicion<String>(estado2, estado3, "b");
        
        Transicion<String> transicion7 = new Transicion<String>(estado3, estado2, "E");
        Transicion<String> transicion8 = new Transicion<String>(estado3, estado3, "a");
        Transicion<String> transicion9 = new Transicion<String>(estado3, estado1, "c");
        
        estado1.setTransiciones(transicion1);
        estado1.setTransiciones(transicion2);
        estado1.setTransiciones(transicion3);
        estado2.setTransiciones(transicion4);
        estado2.setTransiciones(transicion5);
        estado2.setTransiciones(transicion6);
        estado3.setTransiciones(transicion7);
        estado3.setTransiciones(transicion8);
        estado3.setTransiciones(transicion9);
        eafnd.addEstados(estado1);
        eafnd.addEstados(estado2);
        eafnd.addEstados(estado3);
        eafnd.setEstadoInicial(estado1);
        eafnd.addEstadosAceptacion(estado3);
        eafnd.setAlfabeto(new HashSet<>(Arrays.asList("a","b","c")));
        return eafnd;
    }

    /*
     * Convertir un eAFND vacía a un AFD. El return es un AFD vacío.
     */
    @Test
    public void testConversioneAFNDVacioAAFD() {
        Automata eafndVacio = new AFND_TransEpsilon().getAutomata();
        AFD eafndConvertido = new AFD();
        eafndConvertido.conversionAFN(eafndVacio);
        assertEquals("AFD", eafndConvertido.getAfd().getTipo());
        assertEquals(0, eafndConvertido.getAfd().getEstados().size());        
    }

    /*
     * Convertir un eAFND con un estado, sin transiciones a un AFD.
     * El return es un AFD con un solo estado, el inicial.
     */
    @Test
    public void testConversioneAFNDUnEstadoAAFD() {
        Automata eafndConUnEstado = new AFND_TransEpsilon()
                .getAutomata();
        eafndConUnEstado.addEstados(new Estado<Integer>(new Integer(1)));
        eafndConUnEstado.setEstadoInicial(new Estado<Integer>(new Integer(1)));
        
        AFD eafndConvertido = new AFD();
        eafndConvertido.conversionAFN(eafndConUnEstado);
        assertEquals("AFD", eafndConvertido.getAfd().getTipo());
        assertEquals(1, eafndConvertido.getAfd().getEstados().size());
        // Asumo que el estado 1 siempre es el estado inicial
        assertEquals(new Integer(1), eafndConvertido.getAfd().getEstados(0).getId());
    }

    /*
     * Convertir el siguiente eAFND a AFD:
     * a (alfabeto)
     * 3 (cant. estados)
     * 2 (estado final)
     * 1, a -> 2
     * 1, a -> 3
     * 2, a -> 3
     * 3, a -> 2
     * El lenguaje que acepta es aa*, cualquier cantidad de a, a partir de 1
     * El AFD resultante tiene que tener 2 estados, uno inicial y el otro final
     * con una transición a que va del estado 1 (inicial) al 2 y una transición
     * que va del 2 al mismo 2. El 2 es estado final.
     */
    @Test
    public void testCoversioneAFNDConTresEstados() {
        Automata eafnd = new AFND_TransEpsilon().getAutomata();
        Estado<Integer> estado1 = new Estado<Integer>(1);
        Estado<Integer> estado2 = new Estado<Integer>(2);
        Estado<Integer> estado3 = new Estado<Integer>(3);
        Transicion<String> transicion1 = new Transicion<String>(estado1, estado2, "a");
        Transicion<String> transicion2 = new Transicion<String>(estado1, estado3, "a");
        Transicion<String> transicion3 = new Transicion<String>(estado2, estado3, "a");
        Transicion<String> transicion4 = new Transicion<String>(estado3, estado2, "a");
        estado1.setTransiciones(transicion1);
        estado1.setTransiciones(transicion2);
        estado2.setTransiciones(transicion3);
        estado3.setTransiciones(transicion4);
        eafnd.addEstados(estado1);
        eafnd.addEstados(estado2);
        eafnd.addEstados(estado3);
        eafnd.setEstadoInicial(estado1);
        eafnd.addEstadosAceptacion(estado2);
        eafnd.setAlfabeto(new HashSet<>(Arrays.asList("a")));
        
        AFD eafndConvertido = new AFD();
        eafndConvertido.conversionAFN(eafnd);
        assertEquals("AFD", eafndConvertido.getAfd().getTipo());
        ArrayList<Estado> estados = eafndConvertido.getAfd().getEstados();
        assertEquals(2, estados.size());
        assertEquals(1, eafndConvertido.getAfd().getEstadosAceptacion().size());
        estados.forEach(e -> {
            assertEquals(1, e.getTransiciones().size());
        });
    }

    /*
     * Convertir un eAFND con 2 estados y una transición epsilon entre ellos
     * a un AFD. El estado 1 es el inicial y el 2 el final.
     */
    @Test
    public void testConversioneAFNDConTransicionEpsilon() {
        Automata eafnd = new AFND_TransEpsilon().getAutomata();
        Estado estado1 = new Estado<Integer>(1);
        Estado estado2 = new Estado<Integer>(2);
        Transicion<String> transicion = new Transicion<String>(estado1, estado2, "a");
        estado1.setTransiciones(transicion);
        eafnd.addEstados(estado1);
        eafnd.addEstados(estado2);
        eafnd.setEstadoInicial(estado1);
        eafnd.addEstadosAceptacion(estado2);
        eafnd.setAlfabeto(new HashSet<>(Arrays.asList("a")));
        
        AFD eafndConvertido = new AFD();
        eafndConvertido.conversionAFN(eafnd);
        assertEquals("AFD", eafndConvertido.getAfd().getTipo());
        assertEquals(1, eafndConvertido.getAfd().getEstados().size());
        assertEquals(1, eafndConvertido.getAfd().getEstadosAceptacion().size());
    }

    /*
     * Convertir el eAFND del punto 16 de la práctica 1 a un AFD.
     * El eAFND es:
     * a,b,c
     * 3 (cantidad de estados)
     * 3 (estado final)
     * 1, a -> 1
     * 1, b -> 2
     * 1, c -> 3
     * 2, E -> 1
     * 2, a -> 2
     * 2, b -> 3
     * 3, E -> 2
     * 3, a -> 3
     * 3, c -> 1
     * El AFD resultante tiene 4 estados, uno de ellos el estado trampa y un
     * solo estado final (el 3). 
     */
    @Test
    public void testConversioneAFNDPunto16Practica() {
        Automata eafnd = getAutomataPunto16();
        AFD eafndConvertido = new AFD();
        eafndConvertido.conversionAFN(eafnd);
        assertEquals("AFD", eafndConvertido.getAfd().getTipo());
        assertEquals(4, eafndConvertido.getAfd().getEstados().size());
        assertEquals(1, eafndConvertido.getAfd().getEstadosAceptacion().size());
    }
}
