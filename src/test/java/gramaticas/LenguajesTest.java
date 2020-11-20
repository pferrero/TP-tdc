package gramaticas;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LenguajesTest {

    Gramatica g;
    Lenguajes leng;

    @Before
    public void setUp() {
        leng = new Lenguajes();
        // Gramática en FNC
        g = new Gramatica();
        g.agregarProduccion(new Produccion("S->AB"));
        g.agregarProduccion(new Produccion("A->BC"));
        g.agregarProduccion(new Produccion("A->a"));
        g.agregarProduccion(new Produccion("B->AC"));
        g.agregarProduccion(new Produccion("B->b"));
        g.agregarProduccion(new Produccion("C->a"));
        g.agregarProduccion(new Produccion("C->b"));
    }

    @Test
    public void casoBase_wPerteneceALaGramatica() {
        Gramatica g1 = new Gramatica();
        g1.agregarProduccion(new Produccion("S->a"));
        assertTrue(leng.CYK(g1, "a"));
    }

    @Test
    public void casoBaso_wNoPerteneceALaGramatica() {
        Gramatica g1 = new Gramatica();
        g1.agregarProduccion(new Produccion("S->a"));
        assertFalse(leng.CYK(g1, "b"));
    }

    @Test
    public void casoIndictivo_wPerteneceAlLenguaje() {
        assertTrue(leng.CYK(g, "ab"));
    }

    @Test
    public void casoInductivo_wNoPerteneceAlLenguaje() {
        assertFalse(leng.CYK(g, "ababa"));
    }
}
