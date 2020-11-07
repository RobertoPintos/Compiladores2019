package GeneracionDeCodigo;

import Lexico.*;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
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
	static Controller controller;

	public TercetosController(Controller c) {
		tercetos = new ArrayList<Terceto>();
		tercetoTerm = null;
		tercetoExp = null;
		pila = new ArrayList<Terceto>();
		cantTercetos = 0;
		auxiliares = new ArrayList<String>();
		bifurcaciones = new HashMap<>();
		this.controller = c; 
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
		return pila.get(pila.size()-1);
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
		pila.remove(pila.size()-1);
	}

	public String generarAssembler() {  //METODO PARA GENERAR EL ASSEMBLER FINAL DE LOS TERCETOS

		// PRIMER PASADA, MARCO BIFURCACIONES Y COMPARACIONES CON DOBLES REGISTROS
		analizarTercetos();
		
		// SEGUNDA PASADA PARA OPTIMIZACION POR REDUCCION SIMPLE
		try {
			reduccionSimple();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		// SEGUNDA PASADA, GENERO EL ASSEMBLER FINAL
		String asmFinal = "";
		tercetoActual = 0;
		for (Terceto t : tercetos) {
			if (!t.getOperador().equals("PRINT")) {
				if (!t.getMarcaFunc() && t != null) {
					asmFinal += writeAssembler(t) + '\n';
					tercetoActual++;
				}
			} else {
				if (!t.getMarcaFunc()) {
					// CASO MARCA LABEL
					if (t.getMarca())
						asmFinal += "Label" + t.getNumTerceto() + ":" + '\n';
					String s = t.getOp1().replaceAll("\\s", "_");
					asmFinal += "invoke MessageBox, NULL, addr " + s + ", addr " + s + ", MB_OK" + '\n';
					tercetoActual++;
				}
			}
		}
		return asmFinal;
	}

	
	private String calculador(String operacion, String op1, String op2, String tipo) {
		String calculo = "";
		if(tipo.equals("CONST INT")) {
			int result = 0;
			if(operacion.equals("*")){
				result = Integer.parseInt(op1) * Integer.parseInt(op2);
			}else if(operacion.equals("/")){
					  result = Integer.parseInt(op1) / Integer.parseInt(op2);
				  }else if (operacion.equals("+")) {
					  		result = Integer.parseInt(op1) + Integer.parseInt(op2);
				  		}else {
							result = Integer.parseInt(op1) - Integer.parseInt(op2);
						}
			calculo = String.valueOf(result);	
		}else {	
				float result = 0;
				if(operacion.equals("*")){
					result = Integer.parseInt(op1) * Integer.parseInt(op2);
				}else if(operacion.equals("/")){
						result = Integer.parseInt(op1) / Integer.parseInt(op2);
					}else if (operacion.equals("+")) {
				  			result = Integer.parseInt(op1) + Integer.parseInt(op2);
			  			}else {
			  				result = Integer.parseInt(op1) - Integer.parseInt(op2);
			  				}
				calculo = String.valueOf(result);
		}
		return calculo;
	}
	
	private void reemplazarReferencia(int numTerceto, String calculo, int i) {
		boolean referencia = false;
		while(i < tercetos.size() && !referencia) {
			if (tercetos.get(i).getOp1().startsWith("[")) {
				if(numTerceto == Integer.parseInt(tercetos.get(i).getOp1().substring(1, tercetos.get(i).getOp1().length() - 1))) {
					tercetos.get(i).setOp1(calculo);
					referencia = true;
				}
			}
			if (tercetos.get(i).getOp2().startsWith("[")) {
				if(numTerceto ==  Integer.parseInt(tercetos.get(i).getOp2().substring(1, tercetos.get(i).getOp2().length() - 1))) {
					tercetos.get(i).setOp2(calculo);
					referencia = true;
				}	
			}
			i++;
		}
		
	}
	
	private void reduccionSimple() {
		String op1 = "";
		String op2 = "";
		int i = 0;
		while ( i < tercetos.size() ) {
				op1 = tercetos.get(i).getOp1();
				op2 = tercetos.get(i).getOp2();
				if ((tercetos.get(i).getOperador().equals("*") || tercetos.get(i).getOperador().equals("/") || tercetos.get(i).getOperador().equals("+") || tercetos.get(i).getOperador().equals("-")) && controller.getLexico().getTS().get(op1).getTipo().equals("CONST INT") && controller.getLexico().getTS().get(op2).getTipo().equals("CONST INT") 
					|| (tercetos.get(i).getOperador().equals("*") || tercetos.get(i).getOperador().equals("/") || tercetos.get(i).getOperador().equals("+") || tercetos.get(i).getOperador().equals("-"))  && controller.getLexico().getTS().get(op1).getTipo().equals("CONST FLOAT") && controller.getLexico().getTS().get(op2).getTipo().equals("CONST FLOAT")) {
					String calculo = calculador(tercetos.get(i).getOperador(), tercetos.get(i).getOp1(), tercetos.get(i).getOp2(), controller.getLexico().getTS().get(op1).getTipo());
					reemplazarReferencia(tercetos.get(i).getNumTerceto(), calculo, i++);// le mando i++ por que seria donde comienza la busqueda para el reemplazo de la referencia de los tercetos
					int j = i - 1;
					controller.getLexico().eliminarVarTS(tercetos.get(j).getAuxAsoc());
					tercetos.remove(j);	
					i++;
				}else {
					i++;
				}
		}
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
				Terceto tercAnt = tercetos.get(t.getNumTerceto() - 1);
				if (tercAnt.getOperador().equals("<")) {
					bifurcaciones.put(t.getNumTerceto(), "JGE Label" + op2);
					if (Integer.parseInt(op2) <= tercetos.size()) {
						Terceto aMarcar = tercetos.get(Integer.parseInt(op2) - 1);
						aMarcar.setMarca();
						tercetos.set(aMarcar.getNumTerceto() - 1, aMarcar);
					}
				}
				if (tercAnt.getOperador().equals(">")) {
					bifurcaciones.put(t.getNumTerceto(), "JLE Label" + op2);
					if (Integer.parseInt(op2) <= tercetos.size()) {
						Terceto aMarcar = tercetos.get(Integer.parseInt(op2) - 1);
						aMarcar.setMarca();
						tercetos.set(aMarcar.getNumTerceto() - 1, aMarcar);
					}
				}
				if (tercAnt.getOperador().equals("<=")) {
					bifurcaciones.put(t.getNumTerceto(), "JG Label" + op2);
					if (Integer.parseInt(op2) <= tercetos.size()) {
						Terceto aMarcar = tercetos.get(Integer.parseInt(op2) - 1);
						aMarcar.setMarca();
						tercetos.set(aMarcar.getNumTerceto() - 1, aMarcar);
					}
				}
				if (tercAnt.getOperador().equals(">=")) {
					bifurcaciones.put(t.getNumTerceto(), "JL Label" + op2);
					if (Integer.parseInt(op2) <= tercetos.size()) {
						Terceto aMarcar = tercetos.get(Integer.parseInt(op2) - 1);
						aMarcar.setMarca();
						tercetos.set(aMarcar.getNumTerceto() - 1, aMarcar);
					}
				}
				if (tercAnt.getOperador().equals("==")) {
					bifurcaciones.put(t.getNumTerceto(), "JNE Label" + op2);
					if (Integer.parseInt(op2) <= tercetos.size()) {
						Terceto aMarcar = tercetos.get(Integer.parseInt(op2) - 1);
						aMarcar.setMarca();
						tercetos.set(aMarcar.getNumTerceto() - 1, aMarcar);
					}
				}
				if (tercAnt.getOperador().equals("!=")) {
					bifurcaciones.put(t.getNumTerceto(), "JE Label" + op2);
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
			if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT"))
				estatica = "IMUL";
			else
				estatica = "MUL";
		// CASO OP = /
		if (t.getOperador().equals("/"))
			if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT"))
				estatica = "IDIV";
			else
				estatica = "DIV";
		// CASO MARCA LABEL
		if (t.getMarca()) 
			asm = "Label" + t.getNumTerceto() + ":" + '\n';
		if (t.getTipoOp() != null) {
			// CASO MARCA COMPARACION ENTRE DOS EXP
			if (t.getMarcaCMP()) {
				reg = "BX";
			} else
				reg = "AX";
			if (!estatica.equals("")) { // SI ES ALGUNA OPERACION ARITMETICA, ENTRO ACA
				// CASO 1: (OP, VAR, VAR)
				if ((!t.getOp1().startsWith("[")) && (!t.getOp2().startsWith("["))) {
					if (estatica.equals("DIV") || estatica.equals("IDIV")) { // SI ES UNA DIVISION TENGO QUE CHEQUEAR QUE EL DIVISOR NO SEA 0
						// DOS VARIABLES ENTERAS
						if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT")) {
							asm += "MOV " + reg + ", _" + t.getOp2() + '\n';
							asm += "CMP " + reg + ", 0" + '\n';
							asm += "JE LabelDivCero \n";

							asm += "MOV " + reg + ", _" + t.getOp1() + '\n';
							asm += "CWD \n";
							asm += estatica + " _" + t.getOp2() + '\n';
							asm += "MOV @" + t.getAuxAsoc() + ", " + reg;
						} else { // CASO DOS VARIABLES FLOTANTES
							String s1 = t.getOp1().replace(".", "_");
							String s2 = t.getOp2().replace(".", "_");
							s1 = s1.replace('-', '_');
							s2 = s2.replace('-', '_');
							asm += "FLD _" + s2 + '\n';
							asm += "FTST \n";
							asm += "FSTSW AX \n";
							asm += "SAHF \n";
							asm += "JE LabelDivCero \n";

							asm += "FDIVR" + " _" + s1 + '\n';
							asm += "FSTP @" + t.getAuxAsoc();
						}
					} else if (estatica.equals("ADD") || estatica.equals("IMUL") || estatica.equals("MUL")) { // SI
																												// ES
																												// SUMA
																												// O
																												// MUL,
																												// CHEQUEO
																												// OVERFLOW
																												// AL
																												// FINAL
						String label = "";
						if (estatica.contentEquals("ADD"))
							label = "LabelOverflowSuma";
						else
							label = "LabelOverflowMul";
						// DOS VARIABLES ENTERAS
						if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT")) {
							asm += "MOV " + reg + ", _" + t.getOp1() + '\n';
							asm += estatica + " " + reg + ", _" + t.getOp2() + '\n';
							asm += "JO " + label + "\n";
							asm += "MOV @" + t.getAuxAsoc() + ", " + reg;

						} else { // CASO DOS VARIABLES FLOTANTES
							String s1 = t.getOp1().replace(".", "_");
							String s2 = t.getOp2().replace(".", "_");
							s1 = s1.replace('-', '_');
							s2 = s2.replace('-', '_');
							asm += "FLD _" + s1 + '\n';
							asm += "F" + estatica + " _" + s2 + '\n';
							asm += "FSTP @" + t.getAuxAsoc() + '\n';

							asm += "FLD _max_float_pos \n";
							asm += "FLD @" + t.getAuxAsoc() + '\n';
							asm += "FCOMPP \n";
							asm += "FSTSW AX \n";
							asm += "SAHF \n";
							asm += "JA " + label + '\n';
							

							asm += "FLD _min_float_neg \n";
							asm += "FLD @" + t.getAuxAsoc() + '\n';
							asm += "FCOMPP \n";
							asm += "FSTSW AX \n";
							asm += "SAHF \n";
							asm += "JB " + label;
							
						}
					} else // PARA EL RESTO DE LAS OPERACIONES (RESTA)
					if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT")) { // DOS VARIABLES ENTERAS
						asm += "MOV " + reg + ", _" + t.getOp1() + '\n';
						asm += estatica + " " + reg + ", _" + t.getOp2() + '\n';
						asm += "MOV @" + t.getAuxAsoc() + ", " + reg;
					} else { // CASO DOS VARIABLES FLOTANTES
						String s1 = t.getOp1().replace(".", "_");
						String s2 = t.getOp2().replace(".", "_");
						s1 = s1.replace('-', '_');
						s2 = s2.replace('-', '_');
						asm += "FLD _" + s1 + '\n';
						asm += "F" + estatica + " _" + s2 + '\n';
						asm += "FSTP @" + t.getAuxAsoc();
					}
				}
				// CASO 2: (OP, TERCETO, VAR)
				if ((t.getOp1().startsWith("[")) && (!t.getOp2().startsWith("["))) {
					int idTerceto = Integer.parseInt(t.getOp1().substring(1, t.getOp1().length()-1)) - 1;
					String nombreVarAsoc = tercetos.get(idTerceto).getAuxAsoc();
					if (estatica.equals("DIV")) { // SI ES UNA DIVISION TENGO QUE CHEQUEAR QUE EL DIVISOR NO SEA 0
						// DOS VARIABLES ENTERAS
						if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT")) {
							asm += "MOV " + reg + ", _" + t.getOp2() + '\n';
							asm += "CMP " + reg + ", 0" + '\n';
							asm += "JE LabelDivCero \n";

							asm += "MOV " + reg + ", @" + nombreVarAsoc + '\n';
							asm += "CWD \n";
							asm += estatica + " _" + t.getOp2() + '\n';
							asm += "MOV @" + t.getAuxAsoc() + ", " + reg;
						} else { // CASO DOS VARIABLES FLOTANTES
							String s2 = t.getOp2().replace(".", "_");
							s2 = s2.replace('-', '_');
							asm += "FLD _" + s2 + '\n';
							asm += "FTST \n";
							asm += "FSTSW AX \n";
							asm += "SAHF \n";
							asm += "JE LabelDivCero \n";

							asm += "FDIVR" + " @" + nombreVarAsoc + '\n';
							asm += "FSTP @" + t.getAuxAsoc();
						}
					} else if (estatica.equals("ADD") || estatica.equals("IMUL") || estatica.equals("MUL")) { // SI ES SUMA O MUL, CHEQUEO OVERFLOW AL FINAL
						String label = "";
						if (estatica.contentEquals("ADD"))
							label = "LabelOverflowSuma";
						else
							label = "LabelOverflowMul";
						// VARIABLES ENTERAS
						if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT")) {
							asm += "MOV " + reg + ", @" + nombreVarAsoc + '\n';
							asm += estatica + " " + reg + ", _" + t.getOp2() + '\n';
							asm += "JO " + label + "\n";
							asm += "MOV @" + t.getAuxAsoc() + ", " + reg;

						} else { // CASO VARIABLES FLOTANTES
							String s2 = t.getOp2().replace(".", "_");
							s2 = s2.replace('-', '_');
							asm += "FLD @" + nombreVarAsoc + '\n';
							asm += "F" + estatica + " _" + s2 + '\n';
							asm += "FSTP @" + t.getAuxAsoc() + '\n';

							asm += "FLD _max_float_pos \n";
							asm += "FLD @" + t.getAuxAsoc() + '\n';
							asm += "FCOMPP \n";
							asm += "FSTSW AX \n";
							asm += "SAHF \n";
							asm += "JA " + label + '\n';

							asm += "FLD _min_float_neg \n";
							asm += "FLD @" + t.getAuxAsoc() + '\n';
							asm += "FCOMPP \n";
							asm += "FSTSW AX \n";
							asm += "SAHF \n";
							asm += "JB " + label;
						}
					} else // PARA EL RESTO DE LAS OPERACIONES (RESTA)
					// CASO VAR ENTERA
					if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT")) {
						asm += "MOV " + reg + ", @" + nombreVarAsoc + '\n';
						asm += estatica + " " + reg + ", _" + t.getOp2() + '\n';
						asm += "MOV @" + t.getAuxAsoc() + ", " + reg;
					} else { // CASO VAR FLOAT
						String s2 = t.getOp2().replace(".", "_");
						s2 = s2.replace('-', '_');
						asm += "FLD @" + nombreVarAsoc + '\n';
						asm += "F" + estatica + " _" + s2 + '\n';
						asm += "FSTP @" + t.getAuxAsoc();
					}
				}
				// CASO 3: (OP, VAR, TERCETO)
				if ((!t.getOp1().startsWith("[")) && (t.getOp2().startsWith("["))) {
					int idTerceto = Integer.parseInt(t.getOp2().substring(1, t.getOp2().length()-1)) - 1;
					String nombreVarAsoc = tercetos.get(idTerceto).getAuxAsoc();
					if (estatica.equals("IDIV")) { // SI ES UNA DIVISION TENGO QUE CHEQUEAR QUE EL DIVISOR NO SEA 0
						// VAR ENTERA
						if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT")) {
							asm += "MOV " + reg + ", @" + nombreVarAsoc + '\n';
							asm += "CMP " + reg + ", 0" + '\n';
							asm += "JE LabelDivCero \n";

							asm += "MOV " + reg + ", _" + t.getOp1() + '\n';
							asm += "CWD \n";
							asm += estatica + " @" + nombreVarAsoc + '\n';
							asm += "MOV @" + t.getAuxAsoc() + ", " + reg;
						} else { // VAR FLOAT
							String s1 = t.getOp1().replace(".", "_");
							s1 = s1.replace('-', '_');
							asm += "FLD @" + nombreVarAsoc + '\n';
							asm += "FTST \n";
							asm += "FSTSW AX \n";
							asm += "SAHF \n";
							asm += "JE LabelDivCero \n";

							asm += "FDIVR" + " @" + s1 + '\n';
							asm += "FSTP @" + t.getAuxAsoc();
						}
					} else if (estatica.equals("ADD") || estatica.equals("IMUL") || estatica.equals("MUL")) { // SI
																												// ES
																												// SUMA
																												// O
																												// MUL,
																												// CHEQUEO
																												// OVERFLOW
																												// AL
																												// FINAL
						String label = "";
						if (estatica.contentEquals("ADD"))
							label = "LabelOverflowSuma";
						else
							label = "LabelOverflowMul";
						// VARIABLES ENTERAS
						if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT")) {
							asm += "MOV " + reg + ", _" + t.getOp1() + '\n';
							asm += estatica + " " + reg + ", @" + nombreVarAsoc + '\n';
							asm += "JO " + label + "\n";
							asm += "MOV @" + t.getAuxAsoc() + ", " + reg;

						} else { // CASO VARIABLES FLOTANTES
							String s1 = t.getOp1().replace(".", "_");
							s1 = s1.replace('-', '_');
							asm += "FLD _" + s1 + '\n';
							asm += "F" + estatica + " _" + nombreVarAsoc + '\n';
							asm += "FSTP @" + t.getAuxAsoc() + '\n';

							asm += "FLD _max_float_pos \n";
							asm += "FLD @" + t.getAuxAsoc() + '\n';
							asm += "FCOMPP \n";
							asm += "FSTSW AX \n";
							asm += "SAHF \n";
							asm += "JA " + label + '\n';

							asm += "FLD _min_float_neg \n";
							asm += "FLD @" + t.getAuxAsoc() + '\n';
							asm += "FCOMPP \n";
							asm += "FSTSW AX \n";
							asm += "SAHF \n";
							asm += "JB " + label;
						}
					} else // PARA EL RESTO DE LAS OPERACIONES (RESTA)
					// CASO VAR ENTERA
					if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT")) {
						asm += "MOV " + reg + ", _" + t.getOp1() + '\n';
						asm += estatica + " " + reg + ", @" + nombreVarAsoc + '\n';
						asm += "MOV @" + t.getAuxAsoc() + ", " + reg;
					} else {// CASO VAR FLOAT
						String s1 = t.getOp1().replace(".", "_");
						s1 = s1.replace('-', '_');
						asm += "FLD _" + s1 + '\n';
						asm += "F" + estatica + " @" + nombreVarAsoc + '\n';
						asm += "FSTP @" + t.getAuxAsoc();
					}
				}
				// CASO 4: (OP, TERCETO, TERCETO)
				if ((t.getOp1().startsWith("[")) && (t.getOp2().startsWith("["))) {
					int idTerceto1 = Integer.parseInt(t.getOp1().substring(1, t.getOp2().length()-1)) - 1;
					int idTerceto2 = Integer.parseInt(t.getOp2().substring(1, t.getOp2().length()-1)) - 1;
					String nombreVarAsoc1 = tercetos.get(idTerceto1).getAuxAsoc();
					String nombreVarAsoc2 = tercetos.get(idTerceto2).getAuxAsoc();
					if (estatica.equals("IDIV")) { // SI ES UNA DIVISION TENGO QUE CHEQUEAR QUE EL DIVISOR NO SEA 0
						// VAR ENTERA
						if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT")) {
							asm += "MOV " + reg + ", @" + nombreVarAsoc2 + '\n';
							asm += "CMP " + reg + ", 0" + '\n';
							asm += "JE LabelDivCero \n";

							asm += "MOV " + reg + ", @" + nombreVarAsoc1 + '\n';
							asm += "CWD \n";
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
					} else if (estatica.equals("ADD") || estatica.equals("IMUL") || estatica.equals("MUL")) { // SI
																												// ES
																												// SUMA
																												// O
																												// MUL,
																												// CHEQUEO
																												// OVERFLOW
																												// AL
																												// FINAL
						String label = "";
						if (estatica.contentEquals("ADD"))
							label = "LabelOverflowSuma";
						else
							label = "LabelOverflowMul";
						// VARIABLES ENTERAS
						if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT")) {
							asm += "MOV " + reg + ", @" + nombreVarAsoc1 + '\n';
							asm += estatica + " " + reg + ", @" + nombreVarAsoc2 + '\n';
							asm += "JO " + label + "\n";
							asm += "MOV @" + t.getAuxAsoc() + ", " + reg;

						} else { // CASO VARIABLES FLOTANTES
							asm += "FLD @" + nombreVarAsoc1 + '\n';
							asm += "F" + estatica + " @" + nombreVarAsoc2 + '\n';
							asm += "FSTP @" + t.getAuxAsoc() + '\n';

							asm += "FLD _max_float_pos \n";
							asm += "FLD @" + t.getAuxAsoc() + '\n';
							asm += "FCOMPP \n";
							asm += "FSTSW AX \n";
							asm += "SAHF \n";
							asm += "JA " + label + '\n';

							asm += "FLD _min_float_neg \n";
							asm += "FLD @" + t.getAuxAsoc() + '\n';
							asm += "FCOMPP \n";
							asm += "FSTSW AX \n";
							asm += "SAHF \n";
							asm += "JB " + label;
						}
					} else // PARA EL RESTO DE LAS OPERACIONES (RESTA)
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

			} else { // CASO DE QUE NO SEA UNA OPERACION ARITMETICA
				// CASO OP = ":="
				if (t.getOperador().equals(":=")) {
					String aux1 = t.getOp1().replace(".", "_");
					String aux2 = t.getOp2().replace(".", "_");
					aux1 = aux1.replace('-', '_');
					aux2 = aux2.replace('-', '_');
					// CASO 1: (OP, VAR, VAR)
					if ((!t.getOp2().startsWith("["))) {
						// CASO VARIABLES ENTERAS
						if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT")) {
							asm += "MOV " + reg + ", _" + aux2 + '\n';
							asm += "MOV _" + aux1 + ", " + reg;
						} else { // CASO VARIABLES FLOTANTES
							String f = t.getOp2().replace(".", "_");
							f = f.replace('-', '_');
							asm += "FLD _" + f + '\n';
							asm += "FSTP _" + aux1;
						}
					}
					// CASO 2: (OP, VAR, TERCETO)
					if ((t.getOp2().startsWith("["))) {
						int idTerceto = Integer.parseInt(t.getOp2().substring(1, t.getOp2().length()-1)) -1;
						String nombreVarAsoc = tercetos.get(idTerceto).getAuxAsoc();
						// CASO VAR ENTERA
						if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT")) {
							asm += "MOV " + reg + ", @" + nombreVarAsoc + '\n';
							asm += "MOV _" + aux1 + ", " + reg;
						} else { // CASO VAR FLOAT
							asm += "FLD @" + nombreVarAsoc + '\n';
							asm += "FSTP _" + aux1;
						}
					}
				} else
				// CASO OP = "> , <, >=, <=, ==, !="
				if (t.getOperador().equals("<") || t.getOperador().equals(">") || t.getOperador().equals("<=")
						|| t.getOperador().equals(">=") || t.getOperador().equals("==")
						|| t.getOperador().equals("!=")) {
					// CASO 1: DOS TERCETOS ANTERIORES
					if (t.getOp1().startsWith("[") && t.getOp2().startsWith("["))
						if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT")) // COMPARACION ENTEROS
							asm += "CMP AX, BX";
						else { // COMPARACION ENTRE DOS FLOAT
							int idTerceto1 = Integer.parseInt(t.getOp1().substring(1, t.getOp2().length()-1)) - 1;
							String nombreVarAsoc1 = tercetos.get(idTerceto1).getAuxAsoc();
							int idTerceto2 = Integer.parseInt(t.getOp2().substring(1, t.getOp2().length()-1)) - 1;
							String nombreVarAsoc2 = tercetos.get(idTerceto2).getAuxAsoc();
							asm += "FLD @" + nombreVarAsoc1 + '\n';
							asm += "FLD @" + nombreVarAsoc2 + '\n';
							asm += "FCOMPP \n";
							asm += "FSTSW AX \n";
							asm += "SAHF";
						}
					// CASO 2: UN TERCETO Y UNA CONSTANTE/VARIBLE
					else if (t.getOp1().startsWith("["))
						if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT")) // COMPARACION ENTEROS
							asm += "CMP AX, _" + t.getOp2();
						else { // COMPARACION ENTRE DOS FLOAT
							int idTerceto = Integer.parseInt(t.getOp1().substring(1, t.getOp2().length()-1)) - 1;
							String nombreVarAsoc = tercetos.get(idTerceto).getAuxAsoc();
							String s2 = t.getOp1().replace('.', '_');
							s2 = s2.replace('-', '_');
							asm += "FLD _" + s2 + '\n';
							asm += "FLD @" + nombreVarAsoc + '\n';
							asm += "FCOMPP \n";
							asm += "FSTSW AX \n";
							asm += "SAHF";
						}
					// CASO 3: UNA CONSTANTE/VARIABLE Y UN TERCETO
					else if (t.getOp2().startsWith("["))
						if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT")) // COMPARACION ENTEROS
							asm += "CMP _" + t.getOp1() + ", AX";
						else { // COMPARACION ENTRE DOS FLOAT
							int idTerceto = Integer.parseInt(t.getOp2().substring(1, t.getOp2().length()-1)) - 1;
							String nombreVarAsoc = tercetos.get(idTerceto).getAuxAsoc();
							String s1 = t.getOp1().replace('.', '_');
							s1 = s1.replace('-', '_');
							asm += "FLD _" + s1 + '\n';
							asm += "FLD @" + nombreVarAsoc + '\n';
							asm += "FCOMPP \n";
							asm += "FSTSW AX \n";
							asm += "SAHF";
						}
					// CASO 4: DOS CONSTANTES/VARIABLES
					else {
						if (t.getTipoOp().equals("int") || t.getTipoOp().equals("CONST INT")) { // VAR/CONST ENTERAS
							asm += "MOV AX, _" + t.getOp1() + '\n';
							asm += "CMP AX, _" + t.getOp2();
						} else { // VAR/CONST FLOAT
							String s1 = t.getOp1().replace('.', '_');
							String s2 = t.getOp2().replace('.', '_');
							s1 = s1.replace('-', '_');
							s2 = s2.replace('-', '_');
							asm += "FLD _" + s1 + '\n';
							asm += "FLD _" + s2 + '\n';
							asm += "FCOMPP \n";
							asm += "FSTSW AX \n";
							asm += "SAHF";
						}
					}
				}
			}
			// ULTIMO CASO, OPERACIONES DE SALTO, FUNCIONES Y FINALIZACION DE PROGRAMA
		} else if ((t.getOperador().equals("BF")) || (t.getOperador().equals("BI"))) {
			String valor = bifurcaciones.get(t.getNumTerceto());
			for (Integer s: bifurcaciones.keySet())
				System.out.println();
			asm += valor;
		} else if (t.getOperador().equals("END"))
			asm += "JMP LabelEnd";
		else if (t.getOperador().equals("CALL")) {
			int aux = t.getOp1().indexOf('@');
			String instancia = t.getOp1().substring(aux+1, t.getOp1().length());
			boolean intercambio = false;
			if (hasAtributosAsociados(instancia)) {
				asm += intercambioAtributos(instancia, intercambio);
				intercambio = true;
			}
			String aux2 = t.getOp1().substring(0, aux);
			asm += "CALL @FUNCTION_" + aux2;
			if (intercambio)
				asm += '\n' + intercambioAtributos(instancia, intercambio);
		}
		return asm;
	}

	
	private boolean hasAtributosAsociados (String inst) {
		
		boolean result = false;
		HashMap <String,Atributo> tablaSimb = controller.getTS();
		String type = tablaSimb.get(inst).getTipo();
		for (String s: tablaSimb.keySet()) {
			String uso2 = tablaSimb.get(s).getUso();
			String deClase2 = tablaSimb.get(s).getDeClase();
			if (uso2.equals("Atributo de clase") && deClase2.equals(type)) {
				result = true;
			}
		}
		return result;
	}
	
	private String intercambioAtributos (String inst, boolean interc) {
		
		String asm = "";
		if (!interc) {
			HashMap <String,Atributo> tablaSimb = controller.getTS();
			String type = tablaSimb.get(inst).getTipo();
			for (String s: tablaSimb.keySet()) {
				String uso2 = tablaSimb.get(s).getUso();
				String deClase2 = tablaSimb.get(s).getDeClase();
				if (uso2.equals("Atributo de clase") && deClase2.equals(type)) {
					asm += "MOV AX, _" + inst + "_" + s + '\n';
					asm += "MOV _" + s + ", AX \n"; 
				}	
			}
		} else {
			HashMap <String,Atributo> tablaSimb = controller.getTS();
			String type = tablaSimb.get(inst).getTipo();
			for (String s: tablaSimb.keySet()) {
				String uso2 = tablaSimb.get(s).getUso();
				String deClase2 = tablaSimb.get(s).getDeClase();
				if (uso2.equals("Atributo de clase") && deClase2.equals(type)) {
					asm += "MOV AX, _" + s + '\n';
					asm += "MOV _" + inst + "_" + s + ", AX \n"; 
				}	
			}
			asm = asm.substring(0, asm.length()-2);
		}
		return asm;
	}
	
	public String generarAssemblerFunctions() {

		analizarTercetosFunctions();

		String asm = "";
		for (Terceto t : tercetos) {
			if (t.getOperador().equals("FUNCTION"))
				asm += "@FUNCTION_" + t.getOp1() + ": \n";
			else if (t.getOperador().equals("RETURN"))
				asm += "RET \n";
			else if (t.getMarcaFunc()) {
				if (t.getOperador().equals("PRINT")) {
					String s = t.getOp1().replaceAll("\\s", "_");
					asm += "invoke MessageBox, NULL, addr " + s + ", addr " + s + ", MB_OK";
				}
				t.setMarcaFunc(false);
				asm += writeAssembler(t) + '\n';
				t.setMarcaFunc();
			}
		}

		return asm;
	}

	private void analizarTercetosFunctions() {

		boolean marco = false;
		for (Terceto t : tercetos) {
			if (t.getOperador().equals("FUNCTION") && !marco) {
				t.setMarcaFunc();
				marco = true;
			}
			if (marco)
				t.setMarcaFunc();
			if (t.getOperador().equals("RETURN") && marco) {
				t.setMarcaFunc();
				marco = false;
			}
		}
	}
	
	public void mostrarTercetos(File f) {
		try {
			FileWriter fwriter = new FileWriter(f, true);
			PrintWriter writer = new PrintWriter(fwriter);
			writer.println("-----------------------------------");
			writer.println("-----------------------------------");
			writer.println("Lista de tercetos:");
			for (Terceto t: tercetos) {
				String bf = "";
				if (t.getAuxAsoc() == null)
					bf = t.getNumTerceto() + ". ("+t.getOperador()+", "+t.getOp1()+", "+t.getOp2()+")";
				else
					bf = t.getNumTerceto() + ". ("+t.getOperador()+", "+t.getOp1()+", "+t.getOp2()+")"+ "@"+t.getAuxAsoc();	
				writer.println(bf);
			}
			writer.close();
		} catch (Exception e) {
    		e.printStackTrace();
		}
	}
}