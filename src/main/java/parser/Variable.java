package parser;

public class Variable extends Caracter{

    private Integer numero;
    public static final String EXP_VARIABLE = "(\\X_{\\d+})";
    
    public Variable(String variable) {
    	super(variable);
        // expresion regular que parsee: X_{num}
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + numero.hashCode();
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Variable other = (Variable) obj;
        if (!numero.equals(other.numero))
            return false;
        return true;
    }


    @Override
    public String toString() {
        return "X_{" + numero + "}";
    }
}
