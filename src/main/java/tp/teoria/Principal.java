package tp.teoria;

import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import Utilities.ReadFile;
import automatas.AFD;
import automatas.AFND_TransEpsilon;
import gramaticas.Chomsky;
import gramaticas.Gramatica;
import gramaticas.Lenguajes;
import gramaticas.LimpiadorDeGramaticas;
import gramaticas.Produccion;

public class Principal {

    public static void main(String[] args) {
        // Opciones
        Options opciones = new Options();
        Option  archivo = Option.builder("f")
                .longOpt("file")
                .hasArg()
                .argName("ARCHIVO")
                .desc("El archivo para ser leído.")
                //.required()
                .build();
        Option punto = Option.builder("p")
                .longOpt("punto")
                .hasArg()
                .argName("PUNTO")
                .desc("El punto del trabajo práctico a ejecutar")
                //.required()
                .build();
        Option ayuda = new Option("h", "Imprimir esta ayuda");
        ayuda.setLongOpt("help");
        opciones.addOption(archivo);
        opciones.addOption(punto);
        opciones.addOption(ayuda);


        // Parser
        String pathArchivo = null;
        int   puntoElegido = 0;
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(opciones, args, true);
        } catch (ParseException ex) {
            System.err.println("Error al procesar los parámteros"
                        + ex.getMessage());
            System.exit(1);
        }

        if (cmd.hasOption("h")) {
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp("tp-tdc", opciones, true);
            System.exit(0);
        }

        if (cmd.hasOption("f")) {
            pathArchivo = cmd.getOptionValue("f");
        } else {
            System.err.println("No se especifico el archivo a leer.");
            System.exit(1);
        }

        if (cmd.hasOption("p")) {
            try {
                puntoElegido = Integer.parseInt(cmd.getOptionValue("p"));
            } catch(NumberFormatException ex) {
                System.err.println("Error al elegir el punto del TP. Debe ser"
                        + " un número. p = " + cmd.getOptionValue("p"));
                System.exit(1);
            }
            if (puntoElegido < 1 || puntoElegido > 3) {
                System.err.println("Error el elegir el punto del TP. Debe ser "
                        + "un número entre 1 y 3.");
                System.exit(1);
            }
        } else {
            System.err.println("No se especificó el punto del TP para ejecutar.");
            System.exit(1);
        }

        System.out.println("Archivo: " + pathArchivo + "\nPunto: " + puntoElegido);
        switch (puntoElegido) {
        case 1:
            ejecutarPunto1(pathArchivo);
            break;
        case 2:
            ejecutarPunto2(pathArchivo);
            break;
        case 3:
            break;
        }
    }

    private static void ejecutarPunto1(String path) {
        ReadFile rf = new ReadFile();
        rf.assingFile(path);
        Gramatica gramatica = new Gramatica();
        for (int i = 0; i < rf.linesNumber(); i++) {
            gramatica.agregarProduccion(new Produccion(rf.getLine(i)));
        }
        Gramatica g2 = new LimpiadorDeGramaticas().limpiarGramatica(gramatica);
        Chomsky c = new Chomsky();
        promptCYK(c.FNC(g2));
    }

    private static void promptCYK(Gramatica g) {
        g.getProducciones().forEach(System.out::println);
        Lenguajes leng = new Lenguajes();
        Scanner scanner = new Scanner(System.in);
        System.out.print("CYK > ");
        String  w = scanner.next();
        while (!w.equals("quit")) {
            System.out.println(leng.CYK(g, w));
            System.out.print("CYK > ");
            w = scanner.next();
        }
        scanner.close();
    }

    private static void ejecutarPunto2(String path) {
        AFND_TransEpsilon eAFND = new AFND_TransEpsilon();
        eAFND.buildAutomata(path);
        AFD afd = new AFD();
        afd.conversionAFN(eAFND.getAutomata());
        promptProcesar(afd);
    }

    private static void promptProcesar(AFD afd) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Procesar > ");
        String w = scanner.next();
        while (!w.equals("quit")) {
            System.out.println(afd.procesar(w));
            System.out.print("Procesar > ");
            w = scanner.next();
        }
        scanner.close();
    }
}
