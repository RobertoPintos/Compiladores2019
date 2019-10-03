package Lexico;

public class Error {
	String descripcion;
	int nroLinea;
	
	public Error(String d, int n) {
		this.descripcion = d;
		this.nroLinea = n;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getNroLinea() {
		return nroLinea;
	}
	public void setNroLinea(int nroLinea) {
		this.nroLinea = nroLinea;
	}
}
