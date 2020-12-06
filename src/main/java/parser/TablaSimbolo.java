package parser;

import java.util.HashMap;
import java.util.HashSet;

public class TablaSimbolo {

	private HashSet<Item> items;
    private HashMap<Caracter, TablaAccion> acciones;
    private int id;
    
	public TablaSimbolo(HashSet<Item> items, int id)
	{
		this.items = items;
		this.acciones = new HashMap<Caracter, TablaAccion>();
		this.id = id;
		
	}

	public HashSet<Item> getItems() {
		return items;
	}

	public void setItems(HashSet<Item> items) {
		this.items = items;
	}

	public HashMap<Caracter, TablaAccion> getAcciones() {
		return acciones;
	}

	public void setAcciones(HashMap<Caracter, TablaAccion> acciones) {
		this.acciones = acciones;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
