package GeneracionDeCodigo;

import Lexico.*;
import java.util.*;

public class TercetosController {

	private ArrayList<Terceto> tercetos;
	private Terceto tercetoExp;
	private Terceto tercetoTerm;
	private int cantTercetos;
	private int tercetoActual;
	private ArrayList<Terceto> pila;
	private ArrayList<String> auxiliares;
	private HashMap<Integer, String> bifurcaciones;

	public TercetosController() {
		tercetos = new ArrayList<Terceto>();
		tercetoTerm = null;
		tercetoExp = null;
		pila = new ArrayList<Terceto>();
		cantTercetos = 0;
		auxiliares = new ArrayList<String>();
		bifurcaciones = new HashMap<>();
	}

	public void printTercetos() {
		System.out.println("Lista de tercetos:");
		for (Terceto t : tercetos) {
			t.printTerceto();
		}
	}

	public int getCantTercetos() {
		return cantTercetos;
	}

	public void addTercetoLista(Terceto t) {
		tercetos.add(t);
		cantTercetos++;
	}

	public Terceto getTercetoLista(int index) {
		return tercetos.get(index);
	}

	public void modificarTercetoLista(int index, Terceto t) {
		tercetos.set(index, t);
	}

	public Terceto getTercetoPila() {
		return pila.get(0);
	}

	public TercetosController getTercetosController() {
		return this;
	}

	public Terceto getTercetoExp() {
		return tercetoExp;
	}

	public void setTercetoExp(Terceto t) {
		tercetoExp = t;
	}

	public void setTercetoExpNull() {
		tercetoExp = null;
	}

	public Terceto getTercetoTerm() {
		return tercetoTerm;
	}

	public void setTercetoTerm(Terceto t) {
		tercetoTerm = t;
	}

	public void setTercetoTermNull() {
		tercetoTerm = null;
	}

	public void apilarTerceto(Terceto t) {
		pila.add(t);
	}

	public boolean isPilaEmpty() {
		if (pila.isEmpty())
			return true;
		else
			return false;
	}

	public void removeTercetoPila() {
		pila.remove(0);
	}

	public String generarAssembler() {

		// PRIMER PASADA, MARCO BIFURCACIONES Y COMPARACIONES CON DOBLES REGISTROS
		analizarTercetos();

		// SEGUNDA PASADA, GENERO EL ASSEMBLER FINAL
		String asm = "";
		tercetoActual = 0;
		for (Terceto t : tercetos) {
			if (!t.getOperador().equals("PRINT")) {
				asm = asm + writeAssembler(t) + '\n';
				tercetoActual++;
			} else {
				String s = t.getOp1().replaceAll("\\s", "_");
				asm = asm + "invoke MessageBox, NULL, addr " + s + ", addr " + s + ", MB_OK" + '\n';
				tercetoActual++;
			}
		}
		return asm;
	}

	private void analizarTercetos() {

		String op2 = "";
		int l;
		for (Terceto t : tercetos) {
			if (t.getOperador().equals("<") || t.getOperador().equals(">") || t.getOperador().equals("<=")
					|| t.getOperador().equals(">=") || t.getOperador().equals("==") || t.getOperador().equals("!="))
				if (t.getOp1().startsWith("[") && t.getOp2().startsWith("[")) {
					Terceto tercAnt = tercetos.get(t.getNumTerceto() - 2);
					tercAnt.setMarcaCMP();
					tercetos.set(t.getNumTerceto() - 2, tercAnt);
				}
			if (t.getOperador().equals("BF")) {
				l = t.getOp2().length() - 1;
				op2 = t.getOp2().substring(1, l);
				Terceto tercAnt = tercetos.get(t.getNumTerceto() - 2);
				if (tercAnt.getOperador().equals("<")) {
					bifurcaciones.put(t.getNumTerceto(), "JL Label" + op2);
					if (Integer.parseInt(op2) <= tercetos.size()) {
						Terceto aMarcar = tercetos.get(Integer.parseInt(op2) - 1);
						aMarcar.setMarca();
						tercetos.set(aMarcar.getNumTerceto() - 1, aMarcar);
					}
				}
				if (tercAnt.getOperador().equals(">")) {
					bifurcaciones.put(t.getNumTerceto(), "JG Label" + op2);
					if (Integer.parseInt(op2) <= tercetos.size()) {
						Terceto aMarcar = tercetos.get(Integer.parseInt(op2) - 1);
						aMarcar.setMarca();
						tercetos.set(aMarcar.getNumTerceto() - 1, aMarcar);
					}
				}
				if (tercAnt.getOperador().equals("<=")) {
					bifurcaciones.put(t.getNumTerceto(), "JLE Label" + op2);
					if (Integer.parseInt(op2) <= tercetos.size()) {
						Terceto aMarcar = tercetos.get(Integer.parseInt(op2) - 1);
						aMarcar.setMarca();
						tercetos.set(aMarcar.getNumTerceto() - 1, aMarcar);
					}
				}
				if (tercAnt.getOperador().equals(">=")) {
					bifurcaciones.put(t.getNumTerceto(), "JGE Label" + op2);
					if (Integer.parseInt(op2) <= tercetos.size()) {
						Terceto aMarcar = tercetos.get(Integer.parseInt(op2) - 1);
						aMarcar.setMarca();
						tercetos.set(aMarcar.getNumTerceto() - 1, aMarcar);
					}
				}
				if (tercAnt.getOperador().equals("==")) {
					bifurcaciones.put(t.getNumTerceto(), "JE Label" + op2);
					if (Integer.parseInt(op2) <= tercetos.size()) {
						Terceto aMarcar = tercetos.get(Integer.parseInt(op2) - 1);
						aMarcar.setMarca();
						tercetos.set(aMarcar.getNumTerceto() - 1, aMarcar);
					}
				}
				if (tercAnt.getOperador().equals("!=")) {
					bifurcaciones.put(t.getNumTerceto(), "JNE Label" + op2);
					if (Integer.parseInt(op2) <= tercetos.size()) {
						Terceto aMarcar = tercetos.get(Integer.parseInt(op2) - 1);
						aMarcar.setMarca();
						tercetos.set(aMarcar.getNumTerceto() - 1, aMarcar);
					}
				}
			}
			if (t.getOperador().equals("BI")) {
				l = t.getOp2().length() - 1;
				op2 = t.getOp2().substring(1, l);
				bifurcaciones.put(t.getNumTerceto(), "JMP Label" + op2);
				if (Integer.parseInt(op2) <= tercetos.size()) {
					Terceto aMarcar = tercetos.get(Integer.parseInt(op2) - 1);
					aMarcar.setMarca();
					tercetos.set(aMarcar.getNumTerceto() - 1, aMarcar);
				}
			}
		}
	}

	public String getNombreNextAux() {
		String name = "";
		int n = auxiliares.size() + 1;
		name = "aux" + Integer.toString(n);
		auxiliares.add(name);
		return name;
	}

	private String writeAssembler(Terceto t) {

		String asm = "";
		String estatica = "";
		String reg = "";
		// CASO OP = +
		if (t.getOperador().equals("+"))
			estatica = "ADD";
		// CASO OP = -
		if (t.getOperador().equals("-"))
			estatica = "SUB";
		// CASO OP = *
		if (t.getOperador().equals("*"))
			estatica = "IMUL";
		// CASO OP = /
		if (t.getOperador().equals("/"))
			estatica = "IDIV";
		if (t.getTipoOp() != null) {
			// CASO MARCA COMPARACION ENTRE DOS EXP
			if (t.getMarcaCMP()) {
				reg = "BX";
			} else
				reg = "AX";
			if (!estatica.equals("")) { // SI ES ALGUNA OPERACION ARITMETICA, ENTRO ACA
				// CASO MARCA LABEL
				if (t.getMarca())
					asm = "Label" + t.getNumTerceto() + ":" + '\n';

				// CASO 1: (OP, VAR, VAR)
				if ((!t.getOp1().startsWith("[")) && (!t.getOp2().startsWith("["))) {
					if (estatica.equals("IDIV")) { // SI ES UNA DIVISION TENGO QUE CHEQUEAR QUE EL DIVISOR NO SEA 0
						// DOS VARIABLES ENTERAS
						if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT")) {
							asm += "MOV " + reg + ", _" + t.getOp2() + '\n';
							asm += "CMP " + reg + ", 0" + '\n';
							asm += "JE LabelDivCero \n";

							asm += "MOV " + reg + ", _" + t.getOp1() + '\n';
							asm += estatica + " _" + t.getOp2() + '\n';
							asm += "MOV @" + t.getAuxAsoc() + ", " + reg;
						} else { // CASO DOS VARIABLES FLOTANTES
							String s1 = t.getOp1().replace(".", "_");
							String s2 = t.getOp2().replace(".", "_");
							asm += "FLD _" + s2 + '\n';
							asm += "FTST \n";
							asm += "FSTSW AX \n";
							asm += "SAHF \n";
							asm += "JE LabelDivCero \n";

							asm += "FDIVR" + " _" + s1 + '\n';
							asm += "FSTP @" + t.getAuxAsoc();
						}
					} else // CASO DOS VARIABLES ENTERAS
					if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT")) {
						asm += "MOV " + reg + ", _" + t.getOp1() + '\n';
						asm += estatica + " " + reg + ", _" + t.getOp2() + '\n';
						asm += "MOV @" + t.getAuxAsoc() + ", " + reg;
					} else { // CASO DOS VARIABLES FLOTANTES
						String s1 = t.getOp1().replace(".", "_");
						String s2 = t.getOp2().replace(".", "_");
						asm += "FLD _" + s1 + '\n';
						asm += "F" + estatica + " _" + s2 + '\n';
						asm += "FSTP @" + t.getAuxAsoc();
					}
				}
				// CASO 2: (OP, TERCETO, VAR)
				if ((t.getOp1().startsWith("[")) && (!t.getOp2().startsWith("["))) {
					int idTerceto = Integer.parseInt(t.getOp1().substring(1, 2)) - 1;
					String nombreVarAsoc = tercetos.get(idTerceto).getAuxAsoc();
					if (estatica.equals("IDIV")) { // SI ES UNA DIVISION TENGO QUE CHEQUEAR QUE EL DIVISOR NO SEA 0
						// DOS VARIABLES ENTERAS
						if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT")) {
							asm += "MOV " + reg + ", _" + t.getOp2() + '\n';
							asm += "CMP " + reg + ", 0" + '\n';
							asm += "JE LabelDivCero \n";

							asm += "MOV " + reg + ", @" + nombreVarAsoc + '\n';
							asm += estatica + " _" + t.getOp2() + '\n';
							asm += "MOV @" + t.getAuxAsoc() + ", " + reg;
						} else { // CASO DOS VARIABLES FLOTANTES
							String s2 = t.getOp2().replace(".", "_");
							asm += "FLD _" + s2 + '\n';
							asm += "FTST \n";
							asm += "FSTSW AX \n";
							asm += "SAHF \n";
							asm += "JE LabelDivCero \n";

							asm += "FDIVR" + " @" + nombreVarAsoc + '\n';
							asm += "FSTP @" + t.getAuxAsoc();
						}
					} else
					// CASO VAR ENTERA
					if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT")) {
						asm += "MOV " + reg + ", @" + nombreVarAsoc + '\n';
						asm += estatica + " " + reg + ", _" + t.getOp2() + '\n';
						asm += "MOV @" + t.getAuxAsoc() + ", " + reg;
					} else { // CASO VAR FLOAT
						String s2 = t.getOp2().replace(".", "_");
						asm += "FLD @" + nombreVarAsoc + '\n';
						asm += "F" + estatica + " _" + s2 + '\n';
						asm += "FSTP @" + t.getAuxAsoc();
					}
				}
				// CASO 3: (OP, VAR, TERCETO)
				if ((!t.getOp1().startsWith("[")) && (t.getOp2().startsWith("["))) {
					int idTerceto = Integer.parseInt(t.getOp2().substring(1, 2)) - 1;
					String nombreVarAsoc = tercetos.get(idTerceto).getAuxAsoc();
					if (estatica.equals("IDIV")) { // SI ES UNA DIVISION TENGO QUE CHEQUEAR QUE EL DIVISOR NO SEA 0
						// VAR ENTERA
						if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT")) {
							asm += "MOV " + reg + ", @" + nombreVarAsoc + '\n';
							asm += "CMP " + reg + ", 0" + '\n';
							asm += "JE LabelDivCero \n";

							asm += "MOV " + reg + ", _" + t.getOp1() + '\n';
							asm += estatica + " @" + nombreVarAsoc + '\n';
							asm += "MOV @" + t.getAuxAsoc() + ", " + reg;
						} else { // VAR FLOAT
							String s1 = t.getOp1().replace(".", "_");
							asm += "FLD @" + nombreVarAsoc + '\n';
							asm += "FTST \n";
							asm += "FSTSW AX \n";
							asm += "SAHF \n";
							asm += "JE LabelDivCero \n";

							asm += "FDIVR" + " @" + s1 + '\n';
							asm += "FSTP @" + t.getAuxAsoc();
						}
					} else
					// CASO VAR ENTERA
					if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT")) {
						asm += "MOV " + reg + ", _" + t.getOp1() + '\n';
						asm += estatica + " " + reg + ", @" + nombreVarAsoc + '\n';
						asm += "MOV @" + t.getAuxAsoc() + ", " + reg;
					} else {// CASO VAR FLOAT
						String s1 = t.getOp2().replace(".", "_");
						asm += "FLD @" + nombreVarAsoc + '\n';
						asm += "F" + estatica + " _" + s1 + '\n';
						asm += "FSTP @" + t.getAuxAsoc();
					}
				}
				// CASO 4: (OP, TERCETO, TERCETO)
				if ((t.getOp1().startsWith("[")) && (t.getOp2().startsWith("["))) {
					int idTerceto1 = Integer.parseInt(t.getOp1().substring(1, 2)) - 1;
					int idTerceto2 = Integer.parseInt(t.getOp2().substring(1, 2)) - 1;
					String nombreVarAsoc1 = tercetos.get(idTerceto1).getAuxAsoc();
					String nombreVarAsoc2 = tercetos.get(idTerceto2).getAuxAsoc();
					if (estatica.equals("IDIV")) { // SI ES UNA DIVISION TENGO QUE CHEQUEAR QUE EL DIVISOR NO SEA 0
						// VAR ENTERA
						if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT")) {
							asm += "MOV " + reg + ", @" + nombreVarAsoc2 + '\n';
							asm += "CMP " + reg + ", 0" + '\n';
							asm += "JE LabelDivCero \n";

							asm += "MOV " + reg + ", @" + nombreVarAsoc1 + '\n';
							asm += estatica + " @" + nombreVarAsoc2 + '\n';
							asm += "MOV @" + t.getAuxAsoc() + ", " + reg;
						} else { // VAR FLOAT
							asm += "FLD @" + nombreVarAsoc2 + '\n';
							asm += "FTST \n";
							asm += "FSTSW AX \n";
							asm += "SAHF \n";
							asm += "JE LabelDivCero \n";

							asm += "FDIVR" + " @" + nombreVarAsoc1 + '\n';
							asm += "FSTP @" + t.getAuxAsoc();
						}
					} else
					// CASO TERCETOS ENTEROS
					if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT")) {
						asm += "MOV " + reg + ", @" + nombreVarAsoc1 + '\n';
						asm += estatica + " " + reg + ", @" + nombreVarAsoc2 + '\n';
						asm += "MOV @" + t.getAuxAsoc() + ", " + reg;
					} else { // CASO TERCETOS FLOTANTES
						asm += "FLD @" + nombreVarAsoc2 + '\n';
						asm += "F" + estatica + " @" + nombreVarAsoc1 + '\n';
						asm += "FSTP @" + t.getAuxAsoc();
					}
				}

			} else {
				// CASO OP = ":="
				if (t.getOperador().equals(":=")) {
					// CASO 1: (OP, VAR, VAR)
					if ((!t.getOp2().startsWith("["))) {
						// CASO VARIABLES ENTERAS
						if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT")) {
							asm = "MOV " + reg + ", _" + t.getOp2() + '\n';
							asm = asm + "MOV _" + t.getOp1() + ", " + reg;
						} else { // CASO VARIABLES FLOTANTES
							String f = t.getOp2().replace(".", "_");
							asm = "FLD _" + f + '\n';
							asm = asm + "FSTP _" + t.getOp1();
						}
					}
					// CASO 2: (OP, VAR, TERCETO)
					if ((t.getOp2().startsWith("["))) {
						int idTerceto = Integer.parseInt(t.getOp2().substring(1, 2)) - 1;
						String nombreVarAsoc = tercetos.get(idTerceto).getAuxAsoc();
						// CASO VAR ENTERA
						if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT")) {
							asm = "MOV " + reg + ", @" + nombreVarAsoc + '\n';
							asm = asm + "MOV _" + t.getOp1() + ", " + reg;
						} else { // CASO VAR FLOAT
							asm = "FLD @" + nombreVarAsoc + '\n';
							asm = asm + "FSTP _" + t.getOp1();
						}
					}
				} else
				// CASO OP = "> , <, >=, <=, ==, !="
				if (t.getOperador().equals("<") || t.getOperador().equals(">") || t.getOperador().equals("<=")
						|| t.getOperador().equals(">=") || t.getOperador().equals("==")
						|| t.getOperador().equals("!=")) {
					// CASO 1: DOS TERCETOS ANTERIORES
					if (t.getOp1().startsWith("[") && t.getOp2().startsWith("["))
						if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT"))
							asm = "CMP AX, BX";
						else
							asm = "CMP EAX, EBX";
					// CASO 2: UN TERCETO Y UNA CONSTANTE
					else if (t.getOp1().startsWith("["))
						if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT"))
							asm = "CMP AX, _" + t.getOp2();
						else
							asm = "CMP EAX, _" + t.getOp2();
					// CASO 3: UNA CONSTANTE Y UN TERCETO
					else if (t.getOp2().startsWith("["))
						if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT"))
							asm = "CMP _" + t.getOp1() + ", AX";
						else
							asm = "CMP _" + t.getOp1() + ", EAX";
					// CASO 4: DOS CONSTANTES
					else {
						if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT")) {
							asm = "MOV AX, _" + t.getOp1() + '\n';
							asm = asm + "CMP AX, _" + t.getOp2();
						} else {
							asm = "MOV EAX, _" + t.getOp1() + '\n';
							asm = asm + "CMP EAX, _" + t.getOp2();
						}
					}
				}
			}
		} else if ((t.getOperador().equals("BF")) || (t.getOperador().equals("BI"))) {
			String valor = bifurcaciones.get(t.getNumTerceto());
			asm = valor;
		} else if (t.getOperador().equals("END"))
			asm = "JMP LabelEnd";

		return asm;
	}
}