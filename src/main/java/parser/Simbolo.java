package parser;

enum TipoSimbolo {Variable,Terminal,EndOfFile,Empty} 

public class Simbolo {
	
	private String simbolo;
	private TipoSimbolo tipo;
	
	public Simbolo(String simbolo, TipoSimbolo tipo) 
	{
		this.simbolo = simbolo;
		this.setTipo(tipo);
	}
	
	public String getSimbolo() {
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
		Simbolo other = (Simbolo) obj;
		if (simbolo == null) {
			if (other.simbolo != null)
				return false;
		} else if (!simbolo.equals(other.simbolo))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return this.simbolo;
	}

	public TipoSimbolo getTipo() {
		return tipo;
	}

	private void setTipo(TipoSimbolo tipo) {
		this.tipo = tipo;
	}
	
}
