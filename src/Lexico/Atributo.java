package Lexico;

public class Atributo {

	private String tipo;
	private String uso;
	private int valor;
	private String deClase;
	private String clasePadre;
	
	public Atributo (String t, String u, int v, String dc, String cp){
		this.tipo = t;
		this.uso = u;
		this.valor = v;
		this.deClase = dc;
		this.clasePadre = cp;
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

	public int getValor() {
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
}
