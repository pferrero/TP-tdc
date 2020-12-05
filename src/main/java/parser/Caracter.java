package parser;

public abstract class Caracter {
	
	private String simbolo;
	
	public Caracter(String simbolo) 
	{
		this.simbolo = simbolo;
	}
	
	protected String getSimbolo() {
		return this.simbolo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((simbolo == null) ? 0 : simbolo.hashCode());
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
		Caracter other = (Caracter) obj;
		if (simbolo == null) {
			if (other.simbolo != null)
				return false;
		} else if (!simbolo.equals(other.simbolo))
			return false;
		return true;
	}
	
	

}
