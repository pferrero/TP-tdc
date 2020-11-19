package gramaticas;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

public class ChosmkyTest {

    Chomsky chom = new Chomsky();

    @Test
    public void noSeBorranTerminales() {
        Gramatica g = new Gramatica();
        g.agregarProduccion(new Produccion("S->aDb"));
        Gramatica g1 = chom.reemplazarTerminales(g);
        assertEquals(2, g1.getTerminales().size());
        assertTrue(g1.getTerminales().contains("a"));
        assertTrue(g1.getTerminales().contains("b"));
    }

    @Test
    public void seReemplazaUnUnicoTerminalPorUnaVariable() {
        Gramatica g = new Gramatica();
        g.agregarProduccion(new Produccion("S->bA"));
        Gramatica g1 = chom.reemplazarTerminales(g);
        assertEquals(2, g1.getProducciones().size());
        assertTrue(g1.getTerminales().contains("b"));
        assertEquals(3, g1.getVariables().size());
    }

    @Test
    public void seReemplazanTodosLosTerminalesDelLadoDerecho() {
        Gramatica g = new Gramatica();
        g.agregarProduccion(new Produccion("S->AaBbCco"));
        Gramatica g1 = chom.reemplazarTerminales(g);
        Collection<String> variables = g1.getVariables();
        assertEquals(8, variables.size());
    }

    @Test
    public void siUnTerminalSeRepiteEnElLadoDerechoSeReemplazaPorLaMismaVariable() {
        Gramatica g = new Gramatica();
        g.agregarProduccion(new Produccion("S->aAa"));
        Gramatica g1 = chom.reemplazarTerminales(g);
        Collection<String> variables = g1.getVariables();
        assertEquals(3, variables.size());
        assertEquals(2, g1.getProducciones().size());
    }

    @Test
    public void seReemplazaElMismoTerminalEnDistintasProduccionesPorLaMismaVariable() {
        Gramatica g = new Gramatica();
        g.agregarProduccion(new Produccion("S->aA"));
        g.agregarProduccion(new Produccion("B->aS"));
        Gramatica g1 = chom.reemplazarTerminales(g);
        Collection<String> variables = g1.getVariables();
        assertEquals(4, variables.size());
        assertEquals(3, g1.getProducciones().size());
    }

    @Test
    public void noSeReemplazanLadosDerechosMenoresA2() {
        Gramatica g = new Gramatica();
        g.agregarProduccion(new Produccion("S->a"));
        assertEquals(g, chom.reemplazarLadosDerechos(g));
    }

    @Test
    public void noSeReemplazanLadosDerechosIgualesA2() {
        Gramatica g = new Gramatica();
        g.agregarProduccion(new Produccion("S->FG"));
        assertEquals(g, chom.reemplazarLadosDerechos(g));
    }

    @Test
    public void seReemplazanLadosDerechosMayoresA2() {
        Gramatica g = new Gramatica();
        g.agregarProduccion(new Produccion("S->ABC"));
        Gramatica g1 = chom.reemplazarLadosDerechos(g);
        assertEquals(2, g1.getProducciones().size());
        g1.getProducciones().forEach(p -> assertTrue(p.getLadoDerecho().length() == 2));
    }

    @Test
    public void seReemplazanTodasLasProduccionesConLadosDerechosMayoresA2() {
        Gramatica g = new Gramatica();
        g.agregarProduccion(new Produccion("S->ABC"));
        g.agregarProduccion(new Produccion("A->BDC"));
        g.agregarProduccion(new Produccion("C->c"));
        Gramatica g1 = chom.reemplazarLadosDerechos(g);
        assertEquals(5, g1.getProducciones().size());
        g1.getProducciones().forEach(p -> assertTrue(p.getLadoDerecho().length() <= 2));
    }

    @Test
    public void FNC_test() {
        // Punto 16 de la práctica 3
        Gramatica g = new Gramatica();
        g.agregarProduccion(new Produccion("S->ASB"));
        g.agregarProduccion(new Produccion("S->" + Produccion.EPSILON));
        g.agregarProduccion(new Produccion("A->aAS"));
        g.agregarProduccion(new Produccion("A->a"));
        g.agregarProduccion(new Produccion("B->SbS"));
        g.agregarProduccion(new Produccion("B->A"));
        g.agregarProduccion(new Produccion("B->bb"));
        Gramatica g1 = chom.FNC(g);
        assertEquals(19, g1.getProducciones().size());
        g1.getProducciones().forEach(p -> assertTrue(p.getLadoDerecho().length() <= 2));
    }
}
