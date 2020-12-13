package parser;

import static org.junit.Assert.*;

import java.util.InputMismatchException;

import org.junit.Test;

public class ParserLR0Test {

	@Test(expected = InputMismatchException.class)
	public void errorGramaticaTest() {
		String filePath  = "C:\\Users\\Administrator\\Desktop\\ArchivosTest\\ArchivoGramaticaTest.txt";
		Gramatica gramatica = new Gramatica();
		ParserLR0 parserlr0 = new ParserLR0(gramatica);
		parserlr0.generarGramatica(filePath);
	}
	
	@Test 
	public void generarGramaticaTest() {
		
	}
	
	@Test
	public void conjuntoItemsTest() {
		
	}
	
	@Test
	public void irATest() {
		
	}
	
	@Test
	public void generarLRTest() {
		
	}
	
	@Test
	public void parserLRTest() {
		
	}
	
	@Test
	public void parserLRFailTest() {
		
	}
	
	

}
