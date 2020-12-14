package parser;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.InputMismatchException;

import org.apache.commons.cli.ParseException;
import org.junit.Test;

public class ParserLR0Test {

	@Test(expected = InputMismatchException.class)
	public void errorGramaticaTest() {
		String filePath  = "src/test/resources/ErrorGramaticaTest.txt";
		Gramatica gramatica = new Gramatica();
		ParserLR0 parserlr0 = new ParserLR0(gramatica);
		parserlr0.generarGramatica(filePath);
	}
	
	@Test 
	public void generarGramaticaTest() {
		String filePath  = "src/test/resources/ArchivoGramaticaTest.txt";
		Gramatica gramatica = new Gramatica();
		ParserLR0 parserlr0 = new ParserLR0(gramatica);
		parserlr0.generarGramatica(filePath);
		assertEquals(6, parserlr0.getGramatica().getProducciones().size());
		assertEquals(3, parserlr0.getGramatica().getVariables().size());
		assertEquals(5, parserlr0.getGramatica().getTerminales().size());
	}
	/*
	 * X_{1} -> X_{1}aX_{2}
	 * X_{1} -> X_{2}
	 * X_{2} -> X_{2}bX_{3}
	 * X_{2} -> X_{3}
	 * X_{3} -> cX_{1}d
	 * X_{3} -> z
	 * X_{1} - X
	 */
	
	@Test
	public void conjuntoItemsTest() {
		ArrayList<Simbolo> simbolos = new ArrayList<>();
		simbolos.add(new Simbolo("a",TipoSimbolo.Terminal));
		simbolos.add(new Simbolo("X_{1}", TipoSimbolo.Variable));
		simbolos.add(new Simbolo("b",TipoSimbolo.Terminal));
		Item itemTest = new Item(new Produccion("X_{1}", simbolos), 1);
		String filePath  = "src/test/resources/ItemsTest.txt";
		Gramatica gramatica = new Gramatica();
		ParserLR0 parserlr0 = new ParserLR0(gramatica);
		parserlr0.generarGramatica(filePath);
		HashSet<Item> items = parserlr0.getItems();
		System.out.println(items);
		assertEquals(4, items.size());
		assertEquals(2, items.stream().findFirst().get().getPosicion());
		assertTrue(items.contains(itemTest));
	} 
	
	@Test
	public void irATerminalTest() {
		String filePath  = "src/test/resources/ItemsTest.txt";
		Gramatica gramatica = new Gramatica();
		ParserLR0 parserlr0 = new ParserLR0(gramatica);
		parserlr0.generarGramatica(filePath);
		HashSet<Item> items = parserlr0.getItems();
		HashSet<Item> itemsIrA = parserlr0.IrA(items, new Simbolo("a",TipoSimbolo.Terminal));
		System.out.println(itemsIrA);
		assertEquals(3, itemsIrA.size());
	}
	
	@Test
	public void irAVariableTest() {
		String filePath  = "src/test/resources/ItemsTest.txt";
		Gramatica gramatica = new Gramatica();
		ParserLR0 parserlr0 = new ParserLR0(gramatica);
		parserlr0.generarGramatica(filePath);
		HashSet<Item> items = parserlr0.getItems();
		HashSet<Item> itemsIrA = parserlr0.IrA(items, new Simbolo("X_{1}",TipoSimbolo.Variable));
		System.out.println(itemsIrA);
		assertEquals(1, itemsIrA.size());
	}
	
	@Test
	public void generarLRTest() {
		String filePath  = "src/test/resources/ItemsTest.txt";
		Gramatica gramatica = new Gramatica();
		ParserLR0 parserlr0 = new ParserLR0(gramatica);
		parserlr0.generarGramatica(filePath);
		HashSet<ConjuntoItem> estados = parserlr0.LR();
		mostrarEstados(estados);
		assertEquals(6, estados.size());
	}
	
	private void mostrarEstados(HashSet<ConjuntoItem> estados) {
		for(ConjuntoItem CI : estados) {
			System.out.println("Estado: " + CI.getId() + "\n" +
								"Items: " + CI.getItems()+ "\n" + "Acciones: \n" +
								CI.getAcciones().keySet());
			for(Simbolo X : CI.getAcciones().keySet()) {
				System.out.println(X + "-> " + CI.getAcciones().get(X).getTipo());
				if(X.getSimbolo().equals("")) {
					System.out.println(CI.getAcciones().get(X).getReduccion());
				};
				if(CI.getAcciones().get(X).getTipo() == TipoTablaAccion.Desplazar || CI.getAcciones().get(X).getTipo() == TipoTablaAccion.IrA) {
					System.out.println(X + "-> " + CI.getAcciones().get(X).getDestino().getId());
				}
			}
			System.out.println("\n");
		}
	}

	@Test
	public void parserLRTest() {
		String filePath  = "src/test/resources/ItemsTest.txt";
		Gramatica gramatica = new Gramatica();
		ParserLR0 parserlr0 = new ParserLR0(gramatica);
		parserlr0.generarGramatica(filePath);
		try {
			parserlr0.parserLR("aacbb$");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Test(expected = Exception.class)
	public void parserLRFailTest() throws ParseException {
		String filePath  = "src/test/resources/ItemsTest.txt";
		Gramatica gramatica = new Gramatica();
		ParserLR0 parserlr0 = new ParserLR0(gramatica);
		parserlr0.generarGramatica(filePath);
		parserlr0.parserLR("aacbbb$");
	}
	
	
	

}
