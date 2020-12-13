package automatas;

import java.util.InputMismatchException;
import Utilities.ReadFile;
import Utilities.Validator;

public class AFND_TransEpsilon 
{
	private Automata afnd;
	
	private ReadFile reader;
	private Validator validator;

	private static final String EXPTERMINALES = "(([a-z]|[0-9]),?\\s?)+";
	private static final String EXPDIGIT = "(\\d+)";
	private static final String EXPESTADOSFINALES = "(\\d+,?\\s?)+";
	private static final String EXPTRANSICIONES = "(\\d+,\\s?([a-z]|[0-9]|E)\\s?->\\s?\\d+)";
	
	public AFND_TransEpsilon() 
	{
		this.afnd = new Automata();
		this.reader = new ReadFile();
		this.validator = new Validator();
	}
	
	public Automata getAutomata() 
	{
		return this.afnd;
	}
	
	public void setAutomata(Automata afnd) 
	{
		this.afnd = afnd;
	}
	
	public void buildAutomata(String file) 
	{
		this.reader.assingFile(file);
		this.read();
	}
	
	private void read() {
		int cantidadLineas = reader.linesNumber();
		String simbolosAlf = reader.getLine(0).trim();
		String cantEstados = reader.getLine(1).trim();
		String lineaEstFinales = reader.getLine(2);
		
		if (cantidadLineas < 4) 
			throw new InputMismatchException("El input no tiene trancisiones");
		if(!this.validator.isValid(simbolosAlf,EXPTERMINALES))
			throw new InputMismatchException("Error al leer el alfabeto.");
		if(!this.validator.isValid(cantEstados, EXPDIGIT))
			throw new InputMismatchException("Error al leer la cantidad de estados.");
		if(!this.validator.isValid(lineaEstFinales,EXPESTADOSFINALES))
			throw new InputMismatchException("Error al leer los estados finales.");
		
		String[] estadosFinales = lineaEstFinales.split(",");
		addEstados(this.afnd,Integer.parseInt(cantEstados));
		addEstadosFinales(estadosFinales,this.afnd);
		addTransiciones(cantidadLineas);
		simbolosAlf = simbolosAlf.replaceAll("(\\s)|(,)", "");
		
//		System.out.println(this.afnd.getEstados(0).getId());
		this.afnd.setEstadoInicial(this.afnd.getEstados(0));
		this.afnd.createAlfabeto(simbolosAlf);
		this.afnd.setTipo("epsilon-afnd");
	}

	private void addTransiciones(int cantidadLineas) {
		for (int i = 3;i< cantidadLineas;i++) 
		{
			String linea = reader.getLine(i).trim();
			if(!validator.isValid(linea, EXPTRANSICIONES))
					throw new InputMismatchException("Trancision invalida. Linea " + (i + 1));
			else
				addTransition(linea);
		}
	}

	private void addTransition(String linea) {
		String [] partesLinea = linea.split(",");
		int estadoActual = Integer.parseInt(partesLinea[0])-1;
		String[] partesTransicion = partesLinea[1].split("->");
		String simboloTransicion = partesTransicion[0];
		int estadoSiguiente = Integer.parseInt(partesTransicion[1].replaceAll("(\\s)|(,)", "")) -1;
		//agrego la nueva transicion
//		System.out.println("Agregar transicion: " + estadoActual + ", " + simboloTransicion + "->" + estadoSiguiente);
		Estado estadoInicio = this.afnd.getEstados(estadoActual) ;
		Estado estadoFin =  this.afnd.getEstados(estadoSiguiente);
		Transicion nuevaTransicion = new Transicion<>(estadoInicio, estadoFin, simboloTransicion.replaceAll("(\\s)", ""));
//		System.out.println("Existe el estado: " + this.afnd.getEstados().contains(this.afnd.getEstados(estadoSiguiente)));
		if(!this.afnd.getEstados().contains(this.afnd.getEstados(estadoActual).getTransiciones().contains(nuevaTransicion)))
			this.afnd.getEstados(estadoActual).setTransiciones(nuevaTransicion);
	}
	
	private void addEstados(Automata automata, int cantEstados) 
	{
		for (int i = 0;i<cantEstados;i++) 
		{
			Estado<Integer> nuevoEstado = new Estado<>(i+1);
			automata.addEstados(nuevoEstado);
		}
	}
	
	private void addEstadosFinales(String [] estados, Automata automata) 
	{
		for (int i = 0; i< estados.length;i++) 
		{
			int indEstado = Integer.parseInt(estados[i].replaceAll("(\\s)", "")) - 1;
			automata.addEstadosAceptacion(automata.getEstados(indEstado));
		}
	}

}
