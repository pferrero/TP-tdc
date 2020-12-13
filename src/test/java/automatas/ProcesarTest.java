package automatas;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

public class ProcesarTest {

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
     * El item b del punto 16 de la práctica 1 pide dar todos los strings de 
     * longitud 3 o menos aceptados por el autómata, estos son: {bab,bb,abb,c}.
     * Los comprobamos en este test.
     */
    @Test
    public void testStringDeLongitud3OMenosPunto16() {
        AFD afd = new AFD();
        afd.conversionAFN(getAutomataPunto16());
        assertTrue(afd.procesar("bab"));
        assertTrue(afd.procesar("bb"));
        assertTrue(afd.procesar("abb"));
        assertTrue(afd.procesar("c"));
    }

    /*
     * Probamos el mismo autómata del punto 16 con strings que no pertenecen al
     * lenguaje.
     */
    @Test
    public void testStringsNoAceptadosPorAutomataPunto16() {
        AFD afd = new AFD();
        afd.conversionAFN(getAutomataPunto16());
        assertFalse(afd.procesar("b"));
        assertFalse(afd.procesar("a"));
        assertFalse(afd.procesar("cc"));
        assertFalse(afd.procesar("bbb"));
    }
}
