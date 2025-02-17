package automatas;

import java.util.ArrayList;
import java.util.HashSet;

public class Automata {

	//compuesto por un estado inicial
    private Estado inicial;
    //en general deberia ser un arreglo de conjuntos finales
    //pero de acuerdo al algoritmo de thomson, siempre 
    //se mantiene un unico estado de aceptacion/final
    private final ArrayList<Estado> finales;
    //array de estados
    private final ArrayList<Estado> estados;
    // alfabeto del aut�mata, hash para no tener elementos repetidos
    private HashSet alfabeto;
    //atributo para saber si es Determinista o No determinista
    private String tipo;
    //atributo para saber el lenguaje r, la cadena w y el resultado de la simulacion
    private String[] resultadoRegex;
    private String lenguajeR;
 
     /**
     * Constructor vacio
     */
    public Automata()
    {
        this.estados = new ArrayList();
        this.finales = new ArrayList();
        this.alfabeto = new HashSet();
        this.resultadoRegex = new String[3];
    }
    
    /**
     * Accesor del estado inicial del aut�mata
     * @return Estado
     */
    public Estado getEstadoInicial() {
        return inicial;
    }
    /**
     * Mutador del estado inicial del aut�mata
     * @param inicial Estado inicial
     */
    public void setEstadoInicial(Estado inicial) {
        this.inicial = inicial;
    }
    /**
     * Accesor del estado de aceptacion o final del aut�mata
     * @return Estado
     */
    public ArrayList<Estado> getEstadosAceptacion() {
        return finales;
    }
    /**
     * Mutador del estado final o aceptacion del aut�mata
     * @param fin Estado final
     */
    public void addEstadosAceptacion(Estado fin) {
        this.finales.add(fin);
    }

    /**
     * Obtener los estados del aut�mata
     * @return Array de Estados
     */
    public ArrayList<Estado> getEstados() {
        return estados;
    }
   
    public Estado getEstados(int index){
        return estados.get(index);
    }
    
    /**
     * Agregar un estado al aut�mata
     * @param estado estructura de estado
     */
    public void addEstados(Estado estado) {
        this.estados.add(estado);
    }
    
    /**
     * Mostrar los atributos del aut�mata
     * @return String
     */
    public HashSet getAlfabeto() {
        return alfabeto;
    }
     
    /**
     * Metodo para definir el alfabeto del automata a partir 
     * de la expresion regular
     * @param regex 
     */
    public void createAlfabeto(String regex) {
        for (Character ch: regex.toCharArray()){
           
            if (ch != '|' && ch != '.' && ch != '*' && ch != 'E' /*AutomataMain.EPSILON_CHAR*/)
                this.alfabeto.add(Character.toString(ch));
        }
    }
    
    public void setAlfabeto(HashSet alfabeto){
        this.alfabeto=alfabeto;
    }

    public void setTipo(String tipo){
        this.tipo = tipo;
    }
    
    public String getTipo(){
        return this.tipo;
    }

    public Estado getInicial() {
        return inicial;
    }

    public void setInicial(Estado inicial) {
        this.inicial = inicial;
    }

    public String[] getResultadoRegex() {
        return resultadoRegex;
    }

    public void addResultadoRegex(int key, String value) {
        this.resultadoRegex[key] = value;
    }
    
    @Override
    public String toString(){
        String res = new String();
        res += "-------"+this.tipo+"---------\r\n";
        res += "Alfabeto " + this.alfabeto+"\r\n";
        res += "Estado inicial " + this.inicial.toString() +"\r\n";
        res += "Conjutos de estados de aceptacion " + this.finales +"\r\n";
        res += "Conjunto de Estados " + this.estados.toString()+"\r\n";
        res += "Conjunto de transiciones ";
        for (int i =0 ; i<this.estados.size();i++){
             Estado est = estados.get(i);
             res += est.getTransiciones()+"-";
        }
        res += "\r\n";
//        res += "Lenguaje r: " +this.lenguajeR + "\r\n";
//        res += "Cadena w ingresada: "+this.resultadoRegex[1] + "\r\n";
//        res += "Resultado: "+ this.resultadoRegex[2] + "\r\n";
        
        return res;
    }

    public String getLenguajeR() {
        return lenguajeR;
    }

    public void setLenguajeR(String lenguajeR) {
        this.lenguajeR = lenguajeR;
    }
    
   
}
