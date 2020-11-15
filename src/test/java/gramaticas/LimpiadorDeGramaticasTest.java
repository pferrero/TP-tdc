package gramaticas;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LimpiadorDeGramaticasTest {

    private LimpiadorDeGramaticas ldg;
    
    @Before
    public void setUp() {
        ldg = new LimpiadorDeGramaticas();
    }
    
    @Test
    public void whenGramaticaNoTieneProduccionesExpectMismaGramatica() {
        Gramatica gram = new Gramatica();        
        assertEquals(gram, ldg.eliminarProduccionesEpsilon(gram));
    }
    
    @Test
    public void whenGramaticaNoTieneProduccionesEpsilonExpectMismaGramatica() {
        Gramatica gram = new Gramatica();        
        Produccion prod1 = new Produccion("S -> A");
        Produccion prod2 = new Produccion("A -> a");
        gram.agregarProduccion(prod1);
        gram.agregarProduccion(prod2);        
        assertEquals(gram, ldg.eliminarProduccionesEpsilon(gram));
    }
    
    @Test
    public void whenGramaticaTieneSoloUnaProduccionEpsilonExpectGramaticaVacia() {
        Gramatica gram = new Gramatica();
        Produccion prod = new Produccion("S ->" + Produccion.EPSILON);
        gram.agregarProduccion(prod);
        assertEquals(new Gramatica(), ldg.eliminarProduccionesEpsilon(gram));
    }
    
    @Test
    public void whenHayUnaProduccionConVariablesNulleablesThenIncluir2AlamVersionesDeEsaProduccion() {        
        /*
         * La producción S tiene una variable nulleable (A). El algorítmo crea
         * 2^m (m=1) versiones de esa producción en donde la variable nulleable
         * está y no está. No se incluye en la gramática final la producción con
         * lado derecho igual a epsilon.
         */
        Gramatica gram = new Gramatica();
        Produccion prod1 = new Produccion("S -> aA");
        Produccion prod2 = new Produccion("A -> a");
        Produccion prod3 = new Produccion("A -> " + Produccion.EPSILON);
        gram.agregarProduccion(prod1);
        gram.agregarProduccion(prod2);
        gram.agregarProduccion(prod3);
        Gramatica g1 = new Gramatica();
        g1.agregarProduccion(prod1);
        g1.agregarProduccion(new Produccion("S->a"));
        g1.agregarProduccion(prod2);
        assertEquals(g1, ldg.eliminarProduccionesEpsilon(gram));
    }
    
    @Test
    public void whenHayMasDeUnaProdNulleableExpect2mVersiones() {
        Gramatica gram = new Gramatica();
        Produccion prod1 = new Produccion("S -> aAbBc");
        Produccion prod2 = new Produccion("A -> a");
        Produccion prod3 = new Produccion("A -> " + Produccion.EPSILON);
        Produccion prod4 = new Produccion("B -> b");
        Produccion prod5 = new Produccion("B -> " + Produccion.EPSILON);
        gram.agregarProduccion(prod1);
        gram.agregarProduccion(prod2);
        gram.agregarProduccion(prod3);
        gram.agregarProduccion(prod4);
        gram.agregarProduccion(prod5);
        Gramatica g1 = new Gramatica();
        g1.agregarProduccion(prod1);
        g1.agregarProduccion(new Produccion("S -> aAbc"));
        g1.agregarProduccion(new Produccion("S -> abBc"));
        g1.agregarProduccion(new Produccion("S -> abc"));
        g1.agregarProduccion(prod2);
        g1.agregarProduccion(prod4);        
        assertEquals(g1, ldg.eliminarProduccionesEpsilon(gram));
    }
    
    @Test
    public void noSeIncluyeElCasoDondeTodasLasVarDelLadoDerechoSonNulleables() {
        Gramatica gram = new Gramatica();
        Produccion prod1 = new Produccion("S -> ABC");
        Produccion prod2 = new Produccion("S -> d");
        Produccion prod3 = new Produccion("A -> " + Produccion.EPSILON);
        Produccion prod4 = new Produccion("B -> " + Produccion.EPSILON);
        Produccion prod5 = new Produccion("C -> " + Produccion.EPSILON);
        Produccion prod6 = new Produccion("A -> a");
        Produccion prod7 = new Produccion("B -> b");
        Produccion prod8 = new Produccion("C -> c");
        gram.agregarProduccion(prod1);
        gram.agregarProduccion(prod2);
        gram.agregarProduccion(prod3);
        gram.agregarProduccion(prod4);
        gram.agregarProduccion(prod5);
        gram.agregarProduccion(prod6);
        gram.agregarProduccion(prod7);
        gram.agregarProduccion(prod8);
        Gramatica g1 = new Gramatica();
        g1.agregarProduccion(prod1);
        g1.agregarProduccion(new Produccion("S->A"));
        g1.agregarProduccion(new Produccion("S->B"));
        g1.agregarProduccion(new Produccion("S->C"));
        g1.agregarProduccion(new Produccion("S->AB"));
        g1.agregarProduccion(new Produccion("S->AC"));
        g1.agregarProduccion(new Produccion("S->BC"));
        // No se incluye S-> (el caso en que A,B y C no estén).
        g1.agregarProduccion(prod2);
        g1.agregarProduccion(prod6);
        g1.agregarProduccion(prod7);
        g1.agregarProduccion(prod8);
        assertEquals(g1, ldg.eliminarProduccionesEpsilon(gram));
    }

    @Test
    public void whenLaGramaticaEsVaciaExpectMismaGramatica() {
        Gramatica g = new Gramatica();
        assertEquals(new Gramatica(), ldg.eliminarProduccionesUnitarias(g));
    }
    
    @Test
    public void whenLaGramaticaNoTieneProduccionesUnitariasExpectMismaGramatica() {
        Gramatica g = new Gramatica();
        g.agregarProduccion(new Produccion("S->AB"));
        g.agregarProduccion(new Produccion("A -> a"));
        assertEquals(g, ldg.eliminarProduccionesUnitarias(g));
    }
    
    @Test
    public void whenLaGramaticaTieneUnaProduccionUnitariaExpectNuevaGramatica() {
        Gramatica g = new Gramatica();
        Produccion prod1 = new Produccion("S->A");
        Produccion prod2 = new Produccion("A->a");
        g.agregarProduccion(prod1);
        g.agregarProduccion(prod2);
        Gramatica g1 = new Gramatica();
        g1.agregarProduccion(prod2);
        g1.agregarProduccion(new Produccion("S->a"));
        assertEquals(g1, ldg.eliminarProduccionesUnitarias(g));
    }
    
    @Test
    public void whenLaGramaticaTieneUnaCadenaDeProduccionesUnitariasExpectNuevaGramatica() {
        Gramatica g = new Gramatica();
        Produccion prod1 = new Produccion("S->AB");
        Produccion prod2 = new Produccion("S->CD");
        Produccion prod3 = new Produccion("S->C");
        Produccion prod4 = new Produccion("A->B");
        Produccion prod5 = new Produccion("B->C");
        Produccion prod6 = new Produccion("C->D");
        Produccion prod7 = new Produccion("C->E");
        Produccion prod8 = new Produccion("E->BD");
        g.agregarProduccion(prod1);
        g.agregarProduccion(prod2);
        g.agregarProduccion(prod3);
        g.agregarProduccion(prod4);
        g.agregarProduccion(prod5);
        g.agregarProduccion(prod6);
        g.agregarProduccion(prod7);
        g.agregarProduccion(prod8);
        Gramatica g1 = new Gramatica();
        g1.agregarProduccion(prod1);
        g1.agregarProduccion(prod2);
        g1.agregarProduccion(new Produccion("S->BD"));
        g1.agregarProduccion(prod8);
        g1.agregarProduccion(new Produccion("C->BD"));
        g1.agregarProduccion(new Produccion("B->BD"));
        g1.agregarProduccion(new Produccion("A->BD"));
        assertEquals(g1, ldg.eliminarProduccionesUnitarias(g));
    }
}
