package Lexico;

public class Atributo {

	private String tipo;
	private String uso;
	private Object valor;
	private String deClase;
	private String clasePadre;
	private String visibilidad;
	private int cantRef;
	
	public Atributo (String t, String u, Object v, String dc, String cp, int cr){
		this.tipo = t;
		this.uso = u;
		this.valor = v;
		this.deClase = dc;
		this.clasePadre = cp;
		this.cantRef = cr;
		this.visibilidad = "-";
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getUso() {
		return uso;
	}

	public void setUso(String uso) {
		this.uso = uso;
	}

	public Object getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}

	public String getDeClase() {
		return deClase;
	}

	public void setDeClase(String deClase) {
		this.deClase = deClase;
	}

	public String getClasePadre() {
		return clasePadre;
	}

	public void setClasePadre(String clasePadre) {
		this.clasePadre = clasePadre;
	}

	public int getCantRef() {
		return cantRef;
	}

	public void setCantRef(int cantRef) {
		this.cantRef = cantRef;
	}
	
	public void incrementoCantRef () {
		this.cantRef++;
	}
	
	public void decrementoCantRef () {
		this.cantRef--;
	}
	
	public void setVisibilidad (String v) {
		this.visibilidad = v;
	}
	
	public String getVisibilidad () {
		return visibilidad;
	}
}
