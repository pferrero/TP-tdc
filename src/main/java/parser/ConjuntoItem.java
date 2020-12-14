package parser;

import java.util.HashMap;
import java.util.HashSet;

public class ConjuntoItem {

	private HashSet<Item> items;
    private HashMap<Simbolo, TablaAccion> acciones;
    private int id;
    
	public ConjuntoItem(HashSet<Item> items, int id)
	{
		this.items = items;
		this.acciones = new HashMap<Simbolo, TablaAccion>();
		this.id = id;
		
	}
	
	public void agregarAccion(Simbolo caracter, TablaAccion accion) {
		this.acciones.put(caracter, accion);
	}

	public HashSet<Item> getItems() {
		return items;
	}

	public void setItems(HashSet<Item> items) {
		this.items = items;
	}

	public HashMap<Simbolo, TablaAccion> getAcciones() {
		return acciones;
	}

	public void setAcciones(HashMap<Simbolo, TablaAccion> acciones) {
		this.acciones = acciones;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acciones == null) ? 0 : acciones.hashCode());
		result = prime * result + id;
		result = prime * result + ((items == null) ? 0 : items.hashCode());
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
		ConjuntoItem other = (ConjuntoItem) obj;
		if (acciones == null) {
			if (other.acciones != null)
				return false;
		} else if (!acciones.equals(other.acciones))
			return false;
		if (id != other.id)
			return false;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		return true;
	}
}
