package automatas;

public class AutomataMain {
	
	private static final String EPSILON = "E";
	public static final char EPSILON_CHAR = EPSILON.charAt(0);

	public static void main(String[] args) {
		String file = "src/test/resources/afnd-epsilon.txt";
		System.out.println(file);
		
		AFND_TransEpsilon afnd = new AFND_TransEpsilon();
		afnd.buildAutomata(file);
		System.out.println(afnd.getAutomata());
		System.out.println("Convertir AFND a AFD");
		AFD afd = new AFD();
		afd.conversionAFN(afnd.getAutomata());
		
		Simulacion simulacion = new Simulacion();
		System.out.println(simulacion.simular("ba", afd.getAfd()));
	}
}
