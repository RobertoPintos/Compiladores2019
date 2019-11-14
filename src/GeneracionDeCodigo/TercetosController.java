package GeneracionDeCodigo;

import Lexico.*;
import java.util.*;

public class TercetosController {

	private ArrayList<Terceto> tercetos;
	private Terceto tercetoExp;
	private Terceto tercetoTerm;
	private int cantTercetos;
	private ArrayList<Terceto> pila;
	
	public TercetosController () {
		tercetos = new ArrayList<Terceto>();
		tercetoTerm = null;
		tercetoExp = null;
		pila = new ArrayList<Terceto>();
		cantTercetos = 0;
	}
	
	public void printTercetos () {
		System.out.println("Lista de tercetos:");
		for (Terceto t: tercetos) {
			t.printTerceto();
		}
	}
	
	public int getCantTercetos () {
		return cantTercetos;
	}
	
	public void addTercetoLista (Terceto t) {
		tercetos.add(t);
		cantTercetos++;
	}
	
	public Terceto getTercetoLista (int index) {
		return tercetos.get(index);
	}
	
	public void modificarTercetoLista (int index, Terceto t) {
		tercetos.set(index, t);
	}
	
	public Terceto getTercetoPila () {
		return pila.get(0);
	}
	
	public TercetosController getTercetosController () {
		return this;
	}
	
	public Terceto getTercetoExp () {
		return tercetoExp;
	}
	
	public void setTercetoExp (Terceto t) {
		tercetoExp = t;
	}
	
	public void setTercetoExpNull () {
		tercetoExp = null;
	}
	
	public Terceto getTercetoTerm () {
		return tercetoTerm;
	}
	
	public void setTercetoTerm (Terceto t) {
		tercetoTerm = t;
	}
	
	public void setTercetoTermNull () {
		tercetoTerm = null;
	}
	
	public void apilarTerceto (Terceto t) {
		pila.add(t);
	}
	
	public boolean isPilaEmpty () {
		if (pila.isEmpty()) 
			return true;
		else return false;
	}
	
	public void removeTercetoPila () {
		pila.remove(0);
	}
}
