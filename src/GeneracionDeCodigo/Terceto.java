package GeneracionDeCodigo;

import java.util.*;
import Lexico.*;

public class Terceto {

	private String operador;
	private String op1;
	private String op2;
	private int numTerceto;
	
	public Terceto(String izq, String medio, String der, int nT) {
		operador = izq;
		op1 = medio;
		op2 = der;
		this.numTerceto = nT;	
	}
	
	public void printTerceto() {
		String bf = "";
		bf = numTerceto + ". ("+operador+", "+op1+", "+op2+")";
		System.out.println(bf);
	}
	
	public int getNumTerceto() {
		return numTerceto;
	}
	
	public void completarTerceto (String br) {
		this.op2 = br;
	}
}
