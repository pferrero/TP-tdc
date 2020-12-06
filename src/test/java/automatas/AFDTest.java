package automatas;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

public class AFDTest {

    @Test
    public void testConversioneAFNDVacioAAFD() {
        Automata eafndVacio = new AFND_TransEpsilon().getAutomata();
        AFD eafndConvertido = new AFD();
        eafndConvertido.conversionAFN(eafndVacio);
        assertEquals("AFD", eafndConvertido.getAfd().getTipo());
        assertEquals(0, eafndConvertido.getAfd().getEstados().size());        
    }

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

    @Test
    public void testCoversioneAFNDConDosEstados() {
        Automata eafnd = new AFND_TransEpsilon().getAutomata();
        Estado<Integer> estado1 = new Estado<Integer>(1);
        Estado<Integer> estado2 = new Estado<Integer>(2);
        Estado<Integer> estado3 = new Estado<Integer>(3);
        Transicion<Character> transicion1 = new Transicion<Character>(estado1, estado2, 'a');
        Transicion<Character> transicion2 = new Transicion<Character>(estado1, estado3, 'a');
        Transicion<Character> transicion3 = new Transicion<Character>(estado2, estado3, 'a');
        Transicion<Character> transicion4 = new Transicion<Character>(estado3, estado2, 'a');
        estado1.setTransiciones(transicion1);
        estado1.setTransiciones(transicion2);
        estado2.setTransiciones(transicion3);
        estado3.setTransiciones(transicion4);
        eafnd.addEstados(estado1);
        eafnd.addEstados(estado2);
        eafnd.addEstados(estado3);
        eafnd.setEstadoInicial(estado1);
        eafnd.addEstadosAceptacion(estado2);
        eafnd.setAlfabeto(new HashSet<>(Arrays.asList('a')));
        
        AFD eafndConvertido = new AFD();
        eafndConvertido.conversionAFN(eafnd);
        assertEquals("AFD", eafndConvertido.getAfd().getTipo());
        assertEquals(2, eafndConvertido.getAfd().getEstados().size());
    }
}
