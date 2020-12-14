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
        assertTrue(afd.procesar("aac"));
        assertTrue(afd.procesar("abb"));
        assertTrue(afd.procesar("acb"));
        assertTrue(afd.procesar("bab"));
        assertTrue(afd.procesar("bbb"));
        assertTrue(afd.procesar("bba"));
        assertTrue(afd.procesar("ccc"));
        assertTrue(afd.procesar("caa"));
        assertTrue(afd.procesar("cac"));
        assertTrue(afd.procesar("cca"));
        assertTrue(afd.procesar("cba"));
        assertTrue(afd.procesar("cab"));
        assertTrue(afd.procesar("cbb"));
        assertTrue(afd.procesar("acc"));
        assertTrue(afd.procesar("ac"));
        assertTrue(afd.procesar("bb"));
        assertTrue(afd.procesar("cc"));
        assertTrue(afd.procesar("cb"));
        assertTrue(afd.procesar("ca"));
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
        assertFalse(afd.procesar("ba"));
        assertFalse(afd.procesar("aaa"));
        assertFalse(afd.procesar("aab"));
    }

    /*
     * Procesar strings en un AFD vacío.
     */
    @Test
    public void testProcesarStringEnAFDVacio() {
        AFD afd = new AFD();
        assertFalse(afd.procesar("a"));
    }

    /*
     * Procesar strings en un AFD con un solo estado y que acepta el lenguaje
     * compuesto por cualquier cantidad de a's.
     */
    @Test
    public void testProcesarStringEnAFDConUnSoloEstado() {
        Automata automata = new Automata();
        Estado<Integer> estado = new Estado<Integer>(1);
        Transicion<String> transicion = new Transicion<String>(estado, estado, "a");
        estado.setTransiciones(transicion);
        automata.addEstados(estado);
        automata.addEstadosAceptacion(estado);
        automata.setEstadoInicial(estado);
        automata.setAlfabeto(new HashSet<>(Arrays.asList("a")));
        AFD afd = new AFD();
        afd.conversionAFN(automata);
        
        assertTrue(afd.procesar(""));
        assertTrue(afd.procesar("a"));
        assertTrue(afd.procesar("aa"));
        assertTrue(afd.procesar("aaa"));
    }

    /*
     * Procesar un string que no pertenece al lenguaje en un AFD
     */
    @Test
    public void testProcesarStringQueNoPerteneceAlLenguaje() {
        Automata automata = new Automata();
        Estado<Integer> estado = new Estado<Integer>(1);
        Transicion<String> transicion = new Transicion<String>(estado, estado, "a");
        estado.setTransiciones(transicion);
        automata.addEstados(estado);
        automata.addEstadosAceptacion(estado);
        automata.setEstadoInicial(estado);
        automata.setAlfabeto(new HashSet<>(Arrays.asList("a")));
        AFD afd = new AFD();
        afd.conversionAFN(automata);
        
        assertFalse(afd.procesar("b"));
    }
    
    /*
     * Test para procesar string con un autamata afnd - e convertido a AFD. Generado a partir de un archivo de texto 
     */
    
    @Test
    public void testProcesarStringDesdeAFNDEnArchivo() {
    	String file = "src/test/resources/afnd-epsilon.txt";
		System.out.println(file);
		
		AFND_TransEpsilon afnd = new AFND_TransEpsilon();
		afnd.buildAutomata(file);
		System.out.println(afnd.getAutomata());
		System.out.println("Convertir AFND a AFD");
		AFD afd = new AFD();
		afd.conversionAFN(afnd.getAutomata());
		
		Simulacion simulacion = new Simulacion();
		assertTrue(simulacion.simular("aa", afd.getAfd()));
		assertFalse(simulacion.simular("ba", afd.getAfd()));
    }
    
}
