package gramaticas;

import static org.junit.Assert.*;

import org.junit.Test;

public class GramaticaTest {

    @Test
    public void whenAgregarProduccionNuevaThenSeAgregaLaVariableAVariables() {
        Produccion prod = new Produccion("S -> a");
        Gramatica  gram = new Gramatica();
        gram.agregarProduccion(prod);
        assertTrue(gram.getVariables().contains('S'));
        assertEquals(1, gram.getVariables().size());
    }

}
