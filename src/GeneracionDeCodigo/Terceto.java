package GeneracionDeCodigo;

import java.util.*;
import Lexico.*;

public class Terceto {

	private String operador;
	private String op1;
	private String op2;
	private int numTerceto;
	private String auxAsoc;
	private boolean marcaCMP;
	private boolean marca;
	private boolean marcaFunc;
	private String tipoOp;
	
	public Terceto(String izq, String medio, String der, int nT) {
		operador = izq;
		op1 = medio;
		op2 = der;
		this.numTerceto = nT;	
	}
	
	public void printTerceto() {
		String bf = "";
		if (auxAsoc == null)
			bf = numTerceto + ". ("+operador+", "+op1+", "+op2+")";
		else
			bf = numTerceto + ". ("+operador+", "+op1+", "+op2+")"+" @"+auxAsoc;		
		System.out.println(bf);
	}
	
	public int getNumTerceto() {
		return numTerceto;
	}
	
	public void completarTerceto (String br) {
		this.op2 = br;
	}
	
	public String getAuxAsoc () {
		return auxAsoc;
	}
	
	public void setAuxAsoc (String name) {
		auxAsoc = name;
	}
	
	public String getOperador () {
		return operador;
	}
	
	public String getOp1 () {
		return op1;
	}
	
	public String getOp2 () {
		return op2;
	}
	
	public boolean getMarcaCMP () {
		return marcaCMP;
	}
	
	public void setMarcaCMP () {
		marcaCMP = true;
	}
	
	public boolean getMarca () {
		return marca;
	}
	
	public void setMarca () {
		marca = true;
	}
	
	public boolean getMarcaFunc () {
		return marcaFunc;
	}
	
	public void setMarcaFunc () {
		marcaFunc = true;
	}
	
	public void setMarcaFunc (boolean b) {
		marcaFunc = b;
	}
	
	public String getTipoOp () {
		return tipoOp;
	}
	
	public void setTipoOp (String t) {
		tipoOp = t;
	}
	
	public void setOp1 (String o) {
		op1 = o;
	}
	
	public void setOp2 (String o) {
		op2 = o;
	}
}
