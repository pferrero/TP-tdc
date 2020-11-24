package automatas;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeSet;

public class AFD 
{
    private Automata afd;
    private final Simulacion simulador;

    public AFD(){
        simulador = new Simulacion();
        afd = new Automata();
    }
    
    /**
     * Conversion de un automata AFN a uno AFD por el
     * metodo de subconjuntos
     * @param afn AFN
     */
    public void conversionAFN(Automata afn){
        //se crea una estructura vacia
        Automata automata = new Automata();
        //se utiliza una cola como la estructura para guardar los subconjuntos a analizar
        Queue<HashSet<Estado>> cola = new LinkedList();
        //se crea un nuevo estado inicial
        Estado inicial = new Estado(0);
        automata.setEstadoInicial(inicial);
        automata.addEstados(inicial);

        //el algoritmo empieza con el e-Closure del estado inicial del AFN
        HashSet<Estado> array_inicial = simulador.eClosure(afn.getEstadoInicial());
        //si el primer e-closure contiene estados de aceptacion hay que agregarlo
        addEstadosFinales(afn, automata, inicial, array_inicial);
        
        //lo agregamos a la pila
        cola.add(array_inicial);
        //variable temporal para guardar el resultado todos los subconjuntos creados
        ArrayList<HashSet<Estado>> temporal = new ArrayList();
       //se utilizan estos indices para saber el estado actuales y anterior
       int indexEstadoInicio = 0;
       while (!cola.isEmpty()){
           //actual subconjunto
            HashSet<Estado> actual = cola.poll();
            //se recorre el subconjunto con cada simbolo del alfabeto del AFN
            for (Object simbolo: afn.getAlfabeto())
            {
                //se realiza el move con el subconjunto
                HashSet<Estado> move_result = simulador.move(actual, (String) simbolo);

                HashSet<Estado> resultado = new HashSet();
                //e-Closure con cada estado del resultado del move y 
                //se guarda en un solo array (merge)
                for (Estado e : move_result) 
                {
                    resultado.addAll(simulador.eClosure(e));
                }

                Estado anterior = (Estado) automata.getEstados().get(indexEstadoInicio);
                /*Si el subconjunto ya fue creado una vez, solo se agregan
                transiciones al automata*/
                if (temporal.contains(resultado))
                {
                    ArrayList<Estado> array_viejo = automata.getEstados();
                    Estado estado_viejo = anterior;
                    //se busca el estado correspondiente y se le suma el offset
                    Estado estado_siguiente = array_viejo.get(temporal.indexOf(resultado)+1);
                    estado_viejo.setTransiciones(new Transicion(estado_viejo,estado_siguiente,simbolo));

                }
                //si el subconjunto no existe, se crea un nuevo estado
                else
                {
                    temporal.add(resultado);
                    cola.add(resultado);

                    Estado nuevo = new Estado(temporal.indexOf(resultado)+1);
                    anterior.setTransiciones(new Transicion(anterior,nuevo,simbolo));
                    automata.addEstados(nuevo);
                    addEstadosFinales(afn, automata, nuevo, resultado);
                }
            }
            System.out.println();
            indexEstadoInicio++;
           }
        
        this.afd = automata;
        //metodo para definir el alfabeto, se copia el del afn
        definirAlfabeto(afn);
        this.afd.setTipo("AFD");
        System.out.println(afd);
    }

	private void addEstadosFinales(Automata afn, Automata automata, Estado inicial, HashSet<Estado> array_inicial) {
		for (Estado aceptacion:afn.getEstadosAceptacion()){
            if (array_inicial.contains(aceptacion))
                automata.addEstadosAceptacion(inicial);
        }
	}
   
    /**
     * Método para quitar los estados de trampa de un autómata
     * @param afd
     * @return AFD con menos estados 
     */
    public Automata quitarEstadosTrampa(Automata afd){
        ArrayList<Estado> estadoAQuitar = new ArrayList();
        /* 1. primero se calcula los estados que son de trampa
        * Se considera de trampa los estados que tienen transiciones
        * con todas las letras del alfabeto hacia si mismos
        */
        for (int i = 0;i<afd.getEstados().size();i++){
            int verificarCantidadTransiciones = afd.getEstados().get(i).getTransiciones().size();
            int contadorTransiciones=0;
            for (Transicion t : (ArrayList<Transicion>)afd.getEstados().get(i).getTransiciones()){
                if (afd.getEstados().get(i)==t.getFin()){
                    contadorTransiciones++;
                }
                
            }
            if (verificarCantidadTransiciones==contadorTransiciones&&contadorTransiciones!=0){
                
              estadoAQuitar.add(afd.getEstados().get(i));
            }
            
        }
        /*2. una vez ya sabido que estados son los de trampa
        * se quitan las transiciones que van hacia ese estado
        * y al final se elimina el estado del autómata
        */
        for (int i = 0;i<estadoAQuitar.size();i++){
              for (int j = 0;j<afd.getEstados().size();j++){
                    ArrayList<Transicion> arrayT = afd.getEstados().get(j).getTransiciones();
                    int cont =0;
                   // System.out.println(arrayT);
                    while(arrayT.size()>cont){
                        Transicion t = arrayT.get(cont);
                        //se verifican todas las transiciones que de todos los estados
                        //que van hacia el estado a eliminar
                        if (t.getFin()==estadoAQuitar.get(i)){
                            afd.getEstados().get(j).getTransiciones().remove(t);
                            cont--;
                        }
                        cont++;

                    }
                }
                //eliminar el estao al final
                afd.getEstados().remove(estadoAQuitar.get(i));
        }
        //3. arreglar la numeración cuando se quita un estado
        for (int i = 0;i<afd.getEstados().size();i++){
            afd.getEstados().get(i).setId(i);
        }   
        return afd;
    }
   
    /**
     * Automata de prueba para comprobar la minimización
     * @return Automata
     */
    public Automata automataPrueba(){
        Automata prueba = new Automata();
        
        Estado a = new Estado("a");
        prueba.setEstadoInicial(a);
        prueba.addEstados(a);
        
        Estado b = new Estado("b");
        Estado c = new Estado("c");
        Estado d = new Estado("d");
        Estado e = new Estado("e");
        Estado f = new Estado("f");
        Estado g = new Estado("g");
        Estado h = new Estado("h");
        a.setTransiciones(new Transicion(a,b,"0"));
        a.setTransiciones(new Transicion(a,f,"1"));
        b.setTransiciones(new Transicion(b,c,"1"));
        b.setTransiciones(new Transicion(b,g,"0"));
        c.setTransiciones(new Transicion(c,c,"1"));
        c.setTransiciones(new Transicion(c,a,"0"));
        d.setTransiciones(new Transicion(d,c,"0"));
        d.setTransiciones(new Transicion(d,g,"1"));
        e.setTransiciones(new Transicion(e, f, "1"));
        e.setTransiciones(new Transicion(e, h, "0"));
        f.setTransiciones(new Transicion(f, c, "0"));
        f.setTransiciones(new Transicion(f, g, "1"));
        g.setTransiciones(new Transicion(g,g,"0"));
        g.setTransiciones(new Transicion(g,e,"1"));
        h.setTransiciones(new Transicion(h,c,"1"));
        h.setTransiciones(new Transicion(h,g,"0"));
        prueba.addEstados(b);
        prueba.addEstados(c);
        prueba.addEstados(d);
        prueba.addEstados(e);
        prueba.addEstados(f);
        prueba.addEstados(g);
        prueba.addEstados(h);
        prueba.addEstadosAceptacion(c);
        HashSet alfabeto = new HashSet();
        alfabeto.add("0");
        alfabeto.add("1");
        prueba.setAlfabeto(alfabeto);
        
        return prueba;
    }
    
    public boolean procesar(String regex) 
    {
    	return simulador.simular(regex, afd);
    }
    
    	
    /**
     * Copiar el alfabeto del AFN al AFD
     * @param afn 
     */
    private void definirAlfabeto(Automata afn){
        this.afd.setAlfabeto(afn.getAlfabeto());
    }
    
    

     /**
     * Retornar el AFD creado
     * @return Autoamta generado
     */
    public Automata getAfd() {
        return afd;
    }
    
}
