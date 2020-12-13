package automatas;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

public class Simulacion {
	
private String resultado;
    
    public Simulacion(){
        
    }
    
    public Simulacion(Automata afn_simulacion, String regex){
        simular(regex,afn_simulacion);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked"})
	public HashSet<Estado> eClosure(Estado eClosureEstado){
        Stack<Estado> pilaClosure = new Stack();
        Estado actual = eClosureEstado;
//        actual.getTransiciones();
        HashSet<Estado> resultado = new HashSet();
        
        pilaClosure.push(actual);
        while(!pilaClosure.isEmpty()){
            actual = pilaClosure.pop();
           
            for (Transicion t: (ArrayList<Transicion>)actual.getTransiciones()){
            	boolean isEpsilon = t.getSimbolo().equals("E");
            	boolean containsEstado = resultado.contains(t.getFin());
                if (isEpsilon && !containsEstado) 
                {
                    resultado.add(t.getFin());
                    pilaClosure.push(t.getFin());
                }
            }
        }
        resultado.add(eClosureEstado); //la operacion e-Closure debe tener el estado aplicado
        System.out.println("CL("+ eClosureEstado.getId() +") = " + resultado.toString());
        return resultado;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public HashSet<Estado> move(HashSet<Estado> estados, String simbolo){
       
        HashSet<Estado> alcanzados = new HashSet();
        Iterator<Estado> iterador = estados.iterator();
        while (iterador.hasNext()){
            
            for (Transicion t: (ArrayList<Transicion>)iterador.next().getTransiciones()){
                Estado siguiente = t.getFin();
                String simb = (String) t.getSimbolo();
                if (simb.equals(simbolo)){
                    alcanzados.add(siguiente);
                }
            }
        }
        System.out.println("Estados: " + estados + " , Simbolo: " + simbolo );
        System.out.println("Alcanzados: "+ alcanzados);
        return alcanzados;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public Estado move(Estado estado, String simbolo){
        ArrayList<Estado> alcanzados = new ArrayList();
           
        for (Transicion t: (ArrayList<Transicion>)estado.getTransiciones()){
            Estado siguiente = t.getFin();
            String simb = (String) t.getSimbolo();
            
            if (simb.equals(simbolo)&&!alcanzados.contains(siguiente)){
                alcanzados.add(siguiente);
            }
        }
        return alcanzados.get(0);
    }
    
    /**
     * M�todo para simular un automata sin importar si es determinista o no deterministas
     * 
     * @param regex recibe la cadena a simular 
     * @param automata recibe el automata a ser simulado
     */
    public boolean simular(String regex, Automata automata)
    {
        if (automata.getEstados().size() == 0) {
            return false;
        }
        Estado inicial = automata.getEstadoInicial();
        ArrayList<Estado> estados = automata.getEstados();
        ArrayList<Estado> aceptacion = new ArrayList(automata.getEstadosAceptacion());
        
        HashSet<Estado> conjunto = eClosure(inicial);
        for (Character ch: regex.toCharArray()){
            conjunto = move(conjunto,ch.toString());
            HashSet<Estado> temp = new HashSet();
            Iterator<Estado> iter = conjunto.iterator();
            
            while (iter.hasNext()){
               Estado siguiente = iter.next();
               /**
                * En esta parte es muy importante el metodo addAll
                * porque se tiene que agregar el eClosure de todo el conjunto
                * resultante del move y se utiliza un hashSet temporal porque
                * no se permite la mutacion mientras se itera
                */
                temp.addAll(eClosure(siguiente)); 
               
            }
            conjunto=temp;
        }
        
        boolean res = false;
        
        for (Estado estado_aceptacion : aceptacion){
            if (conjunto.contains(estado_aceptacion))
                res = true;
        }
        if (res)
            return true;
        else
            return false;
    }

    public String getResultado() 
    {
    	return resultado;
    }

}
