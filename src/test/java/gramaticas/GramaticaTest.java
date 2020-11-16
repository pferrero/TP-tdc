package gramaticas;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Set;

import org.junit.Test;

public class GramaticaTest {

    @Test
    public void whenAgregarProduccionNuevaThenSeAgregaLaVariableAVariables() {
        Produccion prod = new Produccion("S -> a");
        Gramatica  gram = new Gramatica();
        gram.agregarProduccion(prod);
        assertTrue(gram.getVariables().contains("S"));
        assertEquals(1, gram.getVariables().size());
    }
    
    @Test
    public void whenHayUnaProdNoNulleablesExpectSetVacio() {
        Produccion prod = new Produccion("S ->a");
        Gramatica  gram = new Gramatica();
        gram.agregarProduccion(prod);
        assertEquals(0, gram.variablesNulleables().size());
    }

    @Test
    public void whenHayUnaProdNulleableExpectSetConEsaProduccion() {
        Produccion prod = new Produccion("S ->" + Produccion.EPSILON);
        Gramatica  gram = new Gramatica(); 
        gram.agregarProduccion(prod);
        assertEquals(1, gram.variablesNulleables().size());
        assertTrue(gram.variablesNulleables().contains(prod.getLadoIzquierdo()));
    }
    
    @Test
    public void whenHayVariasProduccionesNoNulleablesExpectSetVacio() {
        Produccion prod1 = new Produccion("S ->A");
        Produccion prod2 = new Produccion("A ->aA");
        Produccion prod3 = new Produccion("B ->b");
        Gramatica  gram = new Gramatica();
        gram.agregarProduccion(prod1);
        gram.agregarProduccion(prod2);
        gram.agregarProduccion(prod3);
        assertEquals(0, gram.variablesNulleables().size());
    }
    
    @Test
    public void whenHayVariasProduccionesNulleablesExpectSetConNulleables() {
        Produccion prod1 = new Produccion("S ->A");
        Produccion prod2 = new Produccion("A ->" + Produccion.EPSILON);
        Produccion prod3 = new Produccion("B ->" + Produccion.EPSILON);
        Gramatica  gram = new Gramatica();
        gram.agregarProduccion(prod1);
        gram.agregarProduccion(prod2);
        gram.agregarProduccion(prod3);
        Set<String> nulleables = gram.variablesNulleables();
        assertEquals(3, nulleables.size());
        assertTrue(nulleables.contains(prod1.getLadoIzquierdo()));
        assertTrue(nulleables.contains(prod2.getLadoIzquierdo()));
        assertTrue(nulleables.contains(prod3.getLadoIzquierdo()));
    }
    
    @Test
    public void whenHayUnaCadenaDeProduccionesNulleablesExpectSetConProdNulleables() {
        Produccion prod1 = new Produccion("S ->A");
        Produccion prod2 = new Produccion("A ->B");
        Produccion prod3 = new Produccion("B ->" + Produccion.EPSILON);
        Gramatica  gram = new Gramatica();
        gram.agregarProduccion(prod1);
        gram.agregarProduccion(prod2);
        gram.agregarProduccion(prod3);
        Set<String> nulleables = gram.variablesNulleables();
        assertEquals(3, nulleables.size());
        assertTrue(nulleables.contains(prod1.getLadoIzquierdo()));
        assertTrue(nulleables.contains(prod2.getLadoIzquierdo()));
        assertTrue(nulleables.contains(prod3.getLadoIzquierdo()));
    }
    
    @Test
    public void whenHayUnCicloDeProduccionesNulleablesExpectSetConProdNulleables() {
        Produccion prod1 = new Produccion("S ->A");
        Produccion prod2 = new Produccion("S ->C");
        Produccion prod3 = new Produccion("A ->C");
        Produccion prod4 = new Produccion("A ->S");
        Produccion prod5 = new Produccion("C ->" + Produccion.EPSILON);
        Gramatica  gram = new Gramatica();
        gram.agregarProduccion(prod1);
        gram.agregarProduccion(prod2);
        gram.agregarProduccion(prod3);
        gram.agregarProduccion(prod4);
        gram.agregarProduccion(prod5);
        Set<String> nulleables = gram.variablesNulleables();
        assertEquals(3, nulleables.size());
        assertTrue(nulleables.contains(prod1.getLadoIzquierdo()));
        assertTrue(nulleables.contains(prod3.getLadoIzquierdo()));
        assertTrue(nulleables.contains(prod5.getLadoIzquierdo()));
    }

    @Test
    public void todosLosSimbolosTerminalesSonGeneradores() {
        Gramatica g = new Gramatica();
        g.agregarProduccion(new Produccion("S->a"));
        g.agregarProduccion(new Produccion("A -> e"));
        Collection<String> generadores = g.getSimbolosGeneradores();
        assertEquals(4, generadores.size());
        assertTrue(generadores.contains("a"));
        assertTrue(generadores.contains("e"));
    }
    
    @Test
    public void unaVariableEsGeneradoraSiDerivaSoloSimbolosGeneradores() {
        Gramatica g = new Gramatica();
        g.agregarProduccion(new Produccion("A -> Ba"));
        Collection<String> generadores = g.getSimbolosGeneradores();
        assertEquals(1, generadores.size());
        assertTrue(generadores.contains("a"));
        assertFalse(generadores.contains("A"));
        assertFalse(generadores.contains("B"));
    }

    @Test
    public void unaVariableEsGeneradoraSiDerivaAOtraQueFueIdentificadaComoGeneradora() {
        Gramatica g = new Gramatica();
        g.agregarProduccion(new Produccion("S->AB"));
        g.agregarProduccion(new Produccion("S->C"));
        g.agregarProduccion(new Produccion("A->aA"));
        g.agregarProduccion(new Produccion("A->a"));
        g.agregarProduccion(new Produccion("B->bB"));
        g.agregarProduccion(new Produccion("C->c"));
        Collection<String> generadores = g.getSimbolosGeneradores();
        assertEquals(6, generadores.size());
        // S se identifica como generador en el caso inductivo.
        assertTrue(generadores.contains("S"));
    }
}
