package gramaticas;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProduccionTest {

    @Test(expected = IllegalArgumentException.class)
    public void whenStringVacioExpectExepcion() {
        Produccion prod = new Produccion("");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void whenFormatoIncorrectoExpectExepcion() {
        Produccion prod = new Produccion("S => y");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void whenMasDeUnaFlechaExpectExepcion() {
        Produccion prod = new Produccion("A -> a -> A");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void whenLadoIzquierdoTieneMasDeUnaVariableExpectExepcion() {
        Produccion prod = new Produccion("AB -> a");
    }
   
    @Test(expected = IllegalArgumentException.class)
    public void whenLadoIzquierdoTieneTerminalesExpectExepcion() {
        Produccion prod = new Produccion("{ -> Aa");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void whenLadoIzquierdoVacioExpectExepcion() {
        Produccion prod = new Produccion("->b");
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenLadoIzquierdoEspacioExpectExepcion() {
        Produccion prod = new Produccion(" ->b");
    } 
   
    @Test(expected = IllegalArgumentException.class)
    public void whenLadoIzquierdoEpsilonExpectExepcion() {
        Produccion prod = new Produccion(Produccion.EPSILON + "->a");
    }
    
    @Test
    public void whenLadoIzquierdoTieneSoloUnaVariableExpectProduccionValida() {
        Produccion prod = new Produccion("S ->aA");
        assertEquals("S", prod.getLadoIzquierdo());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void whenLadoDerechoVacioExpectExepcion() {
        Produccion prod = new Produccion("G->");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void whenLadoDerechoEspacioExpectExepcion() {
        Produccion prod = new Produccion("G-> ");
    }
    
    @Test
    public void whenLadoDerechoTieneCaracteresValidosExpectProduccionValida() {
        Produccion prod1  = new Produccion("A -> I(B)a");
        Produccion prod2 = new Produccion("B->"+Produccion.EPSILON);
        assertEquals("I(B)a", prod1.getLadoDerecho());
        assertEquals(Produccion.EPSILON, prod2.getLadoDerecho());
    }
    
    @Test
    public void whenEqualsIgualObjetoExpectTrue() {
        Produccion prod = new Produccion("A -> a");
        assertTrue(prod.equals(prod));
    }
    
    @Test
    public void whenEqualsNullExpectFalse() {
        Produccion prod = new Produccion("A -> a");
        assertFalse(prod.equals(null));
    }
    
    @Test
    public void whenEqualsDistintaClaseExpectFalse() {
        String produccion = "A -> a";
        Produccion prod = new Produccion(produccion);
        assertFalse(prod.equals(produccion));
    }
    
    @Test
    public void whenEqualsLadoIzquierdoDistintoExpectFalse() {
        Produccion prod1 = new Produccion("A -> a");
        Produccion prod2 = new Produccion("B -> a");
        assertFalse(prod1.equals(prod2));
    }
    
    @Test
    public void whenEqualsLadoDerechoDistintoExpectFalse() {
        Produccion prod1 = new Produccion("A -> a");
        Produccion prod2 = new Produccion("A -> b");
        assertFalse(prod1.equals(prod2));
    }
    
    @Test
    public void whenEqualsLadoIzqYDerIgualesExpectTrue() {
        Produccion prod1 = new Produccion("A -> a");
        Produccion prod2 = new Produccion("A -> a");
        assertTrue(prod1.equals(prod2));
    }
  
    @Test
    public void whenIgualProduccionExpectIgualHashCode() {
        Produccion prod1 = new Produccion("A -> a");
        Produccion prod2 = new Produccion("A -> a");
        assertEquals(prod1.hashCode(), prod2.hashCode());
    }
    
    @Test
    public void whenDistintaProduccionExpectDistintoHashCode() {
        Produccion prod1 = new Produccion("A -> a");
        Produccion prod2 = new Produccion("A -> b");
        assertNotEquals(prod1.hashCode(), prod2.hashCode());
    }
    @Test
    public void toStringTest() {
        String produccion = "S -> a";
        Produccion prod   = new Produccion(produccion);
        assertEquals(produccion, prod.toString());
    }

}
