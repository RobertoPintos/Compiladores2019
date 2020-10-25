package Lexico;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import AccionesSemanticas.*;
import Lexico.Controller;

public class Controller {

	//DEFINO OBJETOS DE LA CLASE
	
	//ACCIONES SEMANTICAS
	private AccSemantica as1 = new AS1();
	private AccSemantica as2 = new AS2();
	private AccSemantica as3 = new AS3();
	private AccSemantica as4 = new AS4();
	private AccSemantica as5 = new AS5();
	private AccSemantica as6 = new AS6();
	private AccSemantica as7 = new AS7();
	private AccSemantica as8 = new AS8();
	private AccSemantica as9 = new AS9();
	private AccSemantica as10 = new AS10();
	private AccSemantica err1 = new Error1();
	private AccSemantica err2 = new Error2();
	private AccSemantica err3 = new Error3();
	private AccSemantica err4 = new Error4();
	private AccSemantica as11 = new AS11();
	private AccSemantica blanco = new ASBlanco();
	
	//MATRIZ DE TRANSICION DE ESTADOS
	int [][] matEstados = {
			//      l   L   d    _   .   /   *   +   -    /n    =   >    <    :    (    )    %    e    E     ;    ,  BL,Tab 
			//      0   1   2    3   4   5   6   7   8    9   10   11   12   13   14   15   16   17   18    19   20   21
			/*0*/ { 1,  1,  2,   F, 17,  9,  F,  F,  F,   0,  14,  12,  13,  15,   F,   F,  16,   1,   1,   F,   F,   0}, 
			/*1*/ { 1,  1,  1,   1,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
			/*2*/ { F,  F,  2,   F,  3,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   0,   0,   F,   F,   F},
			/*3*/ { F,  F,  3,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   4,   4,   F,   F,   F},	
			/*4*/ { 0,  0,  5,   0,  0,  0,  0,  6,  6,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0},
			/*5*/ { F,  F,  5,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
			/*6*/ { F,  F,  6,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
			/*7*/ { F,  F,  8,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
			/*8*/ { F,  F,  8,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   4,   4,   F,   F,   F},
			/*9*/ { F,  F,  F,   F,  F,  F,  F, 10,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
			/*10*/{10, 10, 10,  10, 10, 10, 10, 11, 10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10},
			/*11*/{10, 10, 10,  10, 10,  0, 10, 11, 10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10},
			/*12*/{ F,  F,  F,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
			/*13*/{ F,  F,  F,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
			/*14*/{ F,  F,  F,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
			/*15*/{ 0,  0,  0,   0,  0,  0,  0,  0,  0,   0,   F,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0},
			/*16*/{16, 16, 16,  16, 16, 16, 16, 16, 16,   0,  16,  16,  16,  16,  16,  16,   F,  16,  16,  16,  16,  16},
			/*17*/{ F,  F,  7,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
	};
	
	//MATRIZ DE ACCIONES SEMANTICAS
	private AccSemantica [][] matAS = {
				//	    l     L     d    _    .    /    *    +    -     /n      =   >    <    :    (    )    %    e    E     ;    ,    BL,Tab 
				//      0     1     2    3    4    5    6    7    8     9      10   11   12   13   14   15   16   17   18    19   20     21
				/*0*/ { as1, as1,  as1, as2, as1, as1, as2, as2, as2, blanco, as1, as1, as1, as1, as2, as2, as1, as1, as1,  as2, as2, blanco}, 
				/*1*/ { as3, as3,  as3, as3, as6, as6, as6, as6, as6,  as6,   as6, as6, as6, as6, as6, as6, as6, as3, as3,  as6, as6,   as6},
				/*2*/ { as7, as7,  as3, as7, as3, as7, as7, as7, as7,  as7,   as7, as7, as7, as7, as7, as7, as7, as7, as7,  as7, as7,   as7},
				/*3*/ { as8, as8,  as3, as8, as8, as8, as8, as8, as8,  as8,   as8, as8, as8, as8, as8, as8, as8, as3, as3,  as8, as8,   as8},
				/*4*/ { err3,err3, as3, err3,err3,err3,err3,as3, as3,  err3,  err3,err3,err3,err3,err3,err3,err3,err3,err3, err3,err3,  err3},
				/*5*/ { as8, as8,  as3, as8, as8, as8, as8, as8, as8,  as8,   as8, as8, as8, as8, as8, as8, as8, as8, as8,  as8, as8,   as8},
				/*6*/ { as8, as8,  as3, as8, as8, as8, as8, as8, as8,  as8,   as8, as8, as8, as8, as8, as8, as8, as8, as8,  as8, as8,   as8},
				/*7*/ { as2, as2,  as3, as2, as2, as2, as2, as2, as2,  as2,   as2, as2, as2, as2, as2, as2, as2, as2, as2,  as2, as2,   as2},
				/*8*/ { as8, as8,  as3, as8, as8, as8, as8, as8, as8,  as8,   as8, as8, as8, as8, as8, as8, as8, as3, as3,  as8, as8,   as8},
				/*9*/ { as11,as11, as11,as11,as11,as11,as11,as3, as11, as11,  as11,as11,as11,as11,as11,as11,as11,as11,as11, as11,as11,  as11},
				/*10*/{ as3, as3,  as3, as3, as3, as3, as3, as3, as3,  as3,   as3, as3, as3, as3, as3, as3, as3, as3, as3,  as3, as3,   as3},
				/*11*/{ as3, as3,  as3, as3, as3, as3, as3, as3, as3,  as3,   as3, as3, as3, as3, as3, as3, as3, as3, as3,  as3, as3,   as3},
				/*12*/{ as9, as9,  as9, as9, as9, as9, as9, as9, as9,  as9,   as5, as9, as9, as9, as9, as9, as9, as9, as9,  as9, as9,   as9},
				/*13*/{ as9, as9,  as9, as9, as9, as9, as9, as9, as9,  as9,   as5,as10, as9, as9, as9, as9, as9, as9, as9,  as9, as9,   as9},
				/*14*/{ as9, as9,  as9, as9, as9, as9, as9, as9, as9,  as9,   as5, as9, as9, as9, as9, as9, as9, as9, as9,  as9, as9,   as9},
				/*15*/{ err2,err2,err2,err2,err2,err2,err2,err2,err2,  err2,  as5,err2,err2,err2,err2,err2,err2,err2,err2, err2,err2,  err2},
				/*16*/{ as3, as3,  as3, as3, as3, as3, as3, as3, as3,  err1,  as3, as3, as3, as3, as3, as3, as4, as3, as3,  as3, as3,   as3},	
				/*17*/{ as9, as9,  as3, as9, as9, as9, as9, as9, as9,  as9,   as5, as9, as9, as9, as9, as9, as9, as9, as9,  as9, as9,   as9},
	};
	
	//PALABRAS RESERVADAS DEL CODIGO
	
	public static final int ID = 257;
	public final static short CTE=258;
	public final static short CADENA=259;
	public final static short COMENTARIO=260;
	public final static short ASIGNACION=261;
	public final static short C_MAYORIGUAL=262;
	public final static short C_MENORIGUAL=263;
	public final static short C_DISTINTO=264;
	public static final int IF = 265;
    public static final int ELSE = 266;
    public static final int END_IF = 267;
    public static final int INT = 268;
    public static final int BEGIN = 269;
    public static final int END = 270;
    public static final int PRINT = 271;
    public final static short WHILE =272;
	public final static short DO = 273;
	public final static short CLASS = 274;
	public final static short PUBLIC = 275;
	public final static short PRIVATE = 276;
	public final static short C_IGUAL=277;
	public final static short CTEFLOAT=278;	
	public final static short FLOAT = 279;
	public final static short VOID = 280;
	public final static short EXTENDS = 281;


	
	//SUPUESTO ESTADO FINAL
	private static int F = 500;

	//ARRAYS WARNING AND ERRORES
	public static List<Error> errores = new ArrayList<>();
	public static List<Error> warning = new ArrayList<>();
	
	// VARIABLES DE CODIGO
	private Fuente codigoFuente;
	public String buffer = new String();	
	public static Token token;
	private int estado;
	private boolean coment;
	
	//VARIABLES DE CONTROL
	public static final int maxId = 25;
	public static final float minNegF = (float) -(3.40282347e38);
	public static final float maxNegF = (float) -(1.17549435e-38); 
	public static final float minPosF = (float) 1.17549435e-38;
	public static final float maxPosF = (float) 3.40282347e38;
	public static final float zeroF = (float) 0.0;
	
	public static final int minE = -32768;
	public static final int maxE = 32767;

	public HashMap<String,Atributo> tablaDeSimbolo = new HashMap<>();
	public static HashMap<String,Integer> palabrasReservadas = new HashMap<>();
	public static List<Token> listToken = new ArrayList<Token>(); 
    private ArrayList<String> estructuras = new ArrayList<String>();

	
	
	//CARGO ARREGLO DE PALABRAS RESERVADAS
	
	private void cargarPalabrasReservadas() {
		
		palabrasReservadas.put("if", 265);
		palabrasReservadas.put("else", 266);
		palabrasReservadas.put("end_if", 267);
		palabrasReservadas.put("int", 268);
		palabrasReservadas.put("begin", 269);
		palabrasReservadas.put("end", 270);
		palabrasReservadas.put("print", 271);
		palabrasReservadas.put("while", 272);
		palabrasReservadas.put("do", 273);
		palabrasReservadas.put("class", 274);
		palabrasReservadas.put("public", 275);
		palabrasReservadas.put("private", 276);
		palabrasReservadas.put("float", 279);
		palabrasReservadas.put("void", 280);
		palabrasReservadas.put("extends", 281);
	}
	
	//CONSTRUCTOR
	
	public Controller(Fuente cf) {
		this.codigoFuente = cf;
		cargarPalabrasReservadas();
	}
	
	//METODO CENTRAL, DEVUELVE DE A UN TOKEN DEL CODIGO FUENTE, EN EL FUTURO ES yylex()
	
	public Token getToken() {
		estado = 0;
		while (!codigoFuente.hasFinished() && !(estado == 500)) {
			char leido = codigoFuente.getChar();
			int col = getColumna(leido);
			if(col == -1) {
				addWarning("token no valido ", codigoFuente.getLinea());
				codigoFuente.siguiente();
			}else {
				AccSemantica as = matAS[estado][col];
				as.ejecutar(leido, this);
				if (estado != 500) {
					estado = matEstados[estado][col];
					codigoFuente.siguiente();
				} else {
					codigoFuente.siguiente();
				}
			}
		}
		if (!codigoFuente.hasFinished()) {
			Token t = new Token(getIdentificador(buffer), buffer, getNroLinea());
			System.out.println("Token leido: '"+t.getLexema()+"' en la linea: "+t.getNroLinea());
			return t;
		} else return null;
	}
	
// ---------------------------METODOS ADICIONALES-----------------------------------------
	
	public int get(int fila, int columna) {
		return matEstados[fila][columna];
	}
	
	//DEVUELVE LA COLUMNA DE LA MATRIZ CORRESPONDIENTE AL SIMBOLO LEIDO
	private int getColumna(char c) {
		int value = (int)c;
		if ((value == 101) && (esNum(buffer))) // DETECTA LETRA e EN CASO DE QUE EL BUFFER SEA UNA CTE
			return 17;
		if ((value == 69) && (esNum(buffer))) // DETECTA LETRA E EN CASO DE QUE EL BUFFER SEA UNA CTE
			return 18;
		if (value >= 97 && value <= 122)  //DETECTA LETRA MINUSCULA
			return 0;
		if (value >= 65 && value <= 90)  //DETECTA LETRA MAYUSCULA
			return 1;
		if ( (value >= 48 && value <= 57) )//DETECTA UN DIGITO
			return 2;
		if (value == 95) //DETECTA "_"
			return 3;
		if (value == 46) //DETECTA "."
			return 4;
		if (value == 47) //DETECTA "/"
			return 5;
		if (value == 42) //DETECTA "*"
			return 6;
		if (value == 43) //DETECTA "+"
			return 7;
		if (value == 45) //DETECTA "-"
			return 8;
		if (value==10) // /n
			return 9; 
		if (value==61) // =
			return 10;
		if (value == 62) // > 
			return 11;
		if (value == 60) // <
			return 12;
		if ((value == 58)) // :
			return 13;
		if ((value == 40)) // (
			return 14;
		if ((value == 41)) // )
			return 15;
		if (value== 37) // %
			return 16;
		if ((value == 59)) // ;
			return 19;
		if ((value == 44)) // :
			return 20;
		if ((value==9) || (value == 32))// BLANCO Y TAB
			return 21; 
		return -1;
	}
	
	private int getIdentificador(String lex) {
		if (esReservada(lex)){
            switch (lex){
                case "if": return IF;
                case "else": return ELSE;
                case "end_if": return END_IF;
                case "int": return INT;
                case "do": return DO;
                case "begin": return BEGIN;
                case "end": return END;
                case "while": return WHILE;
                case "print": return PRINT;
                case "class": return CLASS;
                case "public": return PUBLIC;
                case "private": return PRIVATE;
                case "float": return FLOAT;
                case "void": return VOID;
                case "extends": return EXTENDS;
                default: return ID;
            }
        } else 
             if(lex.length()>1){
            	if ((lex.charAt(0) == '/') && (lex.charAt(1) == '+'))
            		return COMENTARIO;
            	else if (lex.charAt(0) == '%')
            			return CADENA;
            		else {
            			if (lex.contains(".")) 
            				return CTEFLOAT;
            			if (esNum(lex))
            				return CTE;
                		if (lex.equals(":="))
                			return ASIGNACION;
                		if (lex.equals("=="))
                			return C_IGUAL;
                		if (lex.equals("!="))
                			return C_DISTINTO;
                		if (lex.equals(">="))
                			return C_MAYORIGUAL;
                		if (lex.equals("<="))
                			return C_MENORIGUAL;
                		return ID;
            		}
             }
		if (getColumna(lex.charAt(0)) == 0 || getColumna(lex.charAt(0)) == 1)
			return ID; //CASO DE LOS IDS DE UN SOLO CARACTER
		else if (esNum(lex))
				return CTE;
			 else return (int) lex.charAt(0);  //CASO DE LOS TOKEN SIMPLES (  ) , ; ETC
		
	}
	
	public boolean esReservada(String palabra){
        if (palabrasReservadas.containsKey(palabra))
            return true;
        else
            return false;
    }
	
	public void inicializarBuffer (char c) {
		buffer = ""+c;
	}
	
	public String getBuffer() {
		return buffer;
	}
	
	public void addTokenListSimple (char c, int nLinea) {
		String s = String.valueOf(c);
		Token t = new Token(getIdentificador(s), s, nLinea);
		listToken.add(t);
	}
	
	public void addTokenListCompuesto (String lex, int nLinea) {
		Token t = new Token(getIdentificador(lex), lex, nLinea);
		listToken.add(t);
	}
	
	public void addTokenTS(String lex, String t) {
		if (t.equals("CONST INT") || t.equals("CONST FLOAT")) {
			if (tablaDeSimbolo.containsKey(lex)) {
				System.out.println("La constante "+lex+" ya existe. Aumentando referencias");
				tablaDeSimbolo.get(lex).incrementoCantRef();
			} else {
				Atributo a = new Atributo(t, t, lex, "-", "-", 1);
		 		tablaDeSimbolo.put(lex, a);
			}
		}
		else if (tablaDeSimbolo.containsKey(lex)) 
				System.out.println("El lexema " + lex + " ya existe.");
			 else if (lex.charAt(0) == '%') {
				 	String nuevo = lex.substring(1);
				 	Atributo a = new Atributo(t, t, 0, "-", "-", 1);
				 	tablaDeSimbolo.put(nuevo, a);
			 	} else {
			 		Atributo a = new Atributo(t, t, 0, "-", "-", 1);
			 		tablaDeSimbolo.put(lex, a);
			 	}
	}
	
	public void addVarTS (String lex, String t, String u, Object v, String dc, String cp, String visibilidad) {
		if (tablaDeSimbolo.containsKey(lex)) 
			System.out.println("La variable "+lex+" ya existe.");
		else {
			Atributo a = new Atributo(t, u, v, dc, cp, 1);
			a.setVisibilidad(visibilidad);
			tablaDeSimbolo.put(lex, a);
		}
	}
	
	public void addClassTS (String lex) {
		if (tablaDeSimbolo.containsKey(lex)) 
			System.out.println("La clase "+lex+" ya existe.");
		else {
			Atributo a = new Atributo("-", "Nombre de clase", "-", lex, "-", 1);
			tablaDeSimbolo.put(lex, a);
		}
	}
	
	public void addClassHeredadaTS (String lex, String lex2) {
		if (tablaDeSimbolo.containsKey(lex)) 
			System.out.println("La clase "+lex+" ya existe.");
		else {
			Atributo a = new Atributo("-", "Nombre de clase", "-", lex, lex2, 1);
			tablaDeSimbolo.put(lex, a);
		}
	}
	
	public void addMethodTS (String visibilidad, String lex, String cn) {
		if (tablaDeSimbolo.containsKey(lex)) 
			System.out.println("El metodo "+lex+" ya existe.");
		else {
			Atributo a = new Atributo("void", "Metodo de clase", "-", cn, "-", 1);
			a.setVisibilidad(visibilidad);
			tablaDeSimbolo.put(lex, a);
		}
	}
	
	public void addError(String desc, int nLinea) {
		Error e = new Error(desc, nLinea);
		errores.add(e);
	}
	
	public void addWarning(String desc, int nLinea) {
		Error e = new Error(desc, nLinea);
		warning.add(e);
	}
	
	public void setEstadoFinal() {
		estado = F;
	}
	
	public int getNroLinea() {
		return codigoFuente.getLinea();
	}
	
	public void setBuffer(String s) {
		buffer = s;
	}
	
	public void recorrerCodFuente() {
		while (!codigoFuente.hasFinished()) {
			getToken();
		}
        System.out.println("Termino de leer el archivo");
	}
	
	private static boolean esNum(String s) { 
		  try {  
		    Double.parseDouble(s);  
		    return true;
		  } catch(NumberFormatException e){  
		    return false;  
		  }  
		}
	
	public void mostrarListaTokens(File f) {
		try {
			FileWriter fwriter = new FileWriter(f, true);
			PrintWriter writer = new PrintWriter(fwriter);
			writer.println("-----------------------------------");
			writer.println("-----------------------------------");
			writer.println("Lista de tokens reconocidos:");
			for (Token t: listToken) {
				if (esReservada(t.getLexema()))
					writer.println("Token obtenido: "+t.getId()+", lexema: "+t.getLexema()+", en la linea: "+t.getNroLinea()+ " (Palabra reservada)");
				else
					writer.println("Token obtenido: "+t.getId()+", lexema: "+t.getLexema()+", en la linea: "+t.getNroLinea());
			}
			writer.close();
		} catch (Exception e) {
    		e.printStackTrace();
		}
	}
	
	public void mostrarTablaSimbolos(File f) {
		try {
			FileWriter fwriter = new FileWriter(f, true);
			PrintWriter writer = new PrintWriter(fwriter);
			writer.println("-----------------------------------");
			writer.println("-----------------------------------");
			writer.println("Contenido de la tabla de simbolos:");
			for (String s: tablaDeSimbolo.keySet()) {
				String tipo = tablaDeSimbolo.get(s).getTipo();
				String uso = tablaDeSimbolo.get(s).getUso();
				Object valor = tablaDeSimbolo.get(s).getValor();
				String declase = tablaDeSimbolo.get(s).getDeClase();
				String clasePadre = tablaDeSimbolo.get(s).getClasePadre();
				String visibilidad = tablaDeSimbolo.get(s).getVisibilidad();
				int cantRef = tablaDeSimbolo.get(s).getCantRef();
				writer.println("Lexema: "+s+", tipo: "+tipo+", uso: "+uso+", valor: "+valor+", de Clase: "+declase+", Clase Padre: "+clasePadre+ ", visibilidad: " + visibilidad + ", cantref: "+cantRef);
			}
			writer.close();
		} catch (Exception e) {
    		e.printStackTrace();
		}
	}
	
	public void mostrarWarnings(File f) {
		try {
			FileWriter fwriter = new FileWriter(f, true);
			PrintWriter writer = new PrintWriter(fwriter);
			writer.println("-----------------------------------");
			writer.println("-----------------------------------");
			writer.println("Lista de warnings:");
			for (Error e: warning) {
				writer.println("-"+e.getDescripcion()+" En la linea: "+e.getNroLinea());
			}
			writer.close();
		} catch (Exception e) {
    		e.printStackTrace();
		}
	}
	
	public void mostrarErrores(File f) {
		try {
			FileWriter fwriter = new FileWriter(f, true);
			PrintWriter writer = new PrintWriter(fwriter);
			writer.println("-----------------------------------");
			writer.println("-----------------------------------");
			writer.println("Lista de errores:");
			for (Error e: errores) {
				writer.println("-"+e.getDescripcion()+" en la linea: "+e.getNroLinea());
			}
			writer.close();
		} catch (Exception e) {
    		e.printStackTrace();
		}
	}
	
	public void setSimboloAnt () {
		codigoFuente.anterior();
	}
	
    public Controller getLexico(){
        return this;
    }
    
    public void getEstructuras(File f) {
    	try {
			FileWriter fwriter = new FileWriter(f, true);
			PrintWriter writer = new PrintWriter(fwriter);
			writer.println("-----------------------------------");
			writer.println("-----------------------------------");
			writer.println("Se encontraron "+getListaEstructuras().size()+" estructuras gramaticales");
			for (String s:estructuras){
				writer.println(s);
			}
			writer.close();
		} catch (Exception e) {
    		e.printStackTrace();
		}
    }

    public ArrayList<String> getListaEstructuras() {
        return estructuras;
    }
    
    public void agregarEstructura(String error){
        estructuras.add(error);
    }
    
    public HashMap <String,Atributo> getTS(){
    	return this.tablaDeSimbolo;
    }
    
    public String generarAssemblerTS () {
    	
    	String asm = "";
    	for (String s: tablaDeSimbolo.keySet()) {
			String tipo = tablaDeSimbolo.get(s).getTipo();
			Object valor = tablaDeSimbolo.get(s).getValor();
			String uso = tablaDeSimbolo.get(s).getUso();
			String deClase = tablaDeSimbolo.get(s).getDeClase();
			if (uso.equals("Objeto")) {
				for (String s2: tablaDeSimbolo.keySet()) {
					String uso2 = tablaDeSimbolo.get(s2).getUso();
					String deClase2 = tablaDeSimbolo.get(s2).getDeClase();
					String tipo2 = tablaDeSimbolo.get(s2).getTipo();	
					if (uso2.equals("Atributo de clase") && deClase2.equals(tipo)) {
						if (tipo2.equals("int"))
							asm = asm + "_" + s +  "_" + s2 + " DW " + valor +'\n';
						else 
							if (tipo2.equals("float")) 
								asm = asm + "_" + s + "_" + s2 + " DD " + valor +'\n';
					}
				}	
			} else
				if (!uso.equals("Atributo de clase"))
					if (uso.equals("Variable auxiliar")) {
						if (tipo.equals("int") || tipo.equals("CONST INT"))
							asm = asm + "@" + s + " DW " + valor +'\n';
						else {
							
							asm = asm + "@" + s + " DD " + valor +'\n';
						}
					} else 
						if (tipo.equals("int") || tipo.equals("CONST INT") && tablaDeSimbolo.get(s).getCantRef() >= 1) {
							String s3 = s.replace('-', '_');
							asm = asm + "_" + s3 + " DW " + valor +'\n';
    					} else {
							if (tipo.equals("float") || tipo.equals("CONST FLOAT") && tablaDeSimbolo.get(s).getCantRef() >= 1) {
								String s3 = s.replace('.', '_');
								s3 = s3.replace('-', '_');
								asm = asm + "_" + s3 + " DD " + valor +'\n';
							} else {
								if (tipo.equals("CHARSEQ")) {
									String s1 = s.replaceAll("\\s", "_");
									asm = asm + s1 + " DB " + "\"" + s + "\", 0" + '\n';
								}
							}
						}
				else
					if (tipo.equals("int"))
						asm = asm + "_" + s + " DW " + valor +'\n';
					else 
						if (tipo.equals("float")) 
							asm = asm + "_" + s + " DD " + valor +'\n'; 
			}
    	return asm;
    }

	public void setComent(boolean b) {
		coment = b;
	}
	
	public boolean getComent () {
		return coment;
	}
	
	public int getFuenteSize () {
		return codigoFuente.getFuenteSize();
	}
	
	public int getPosFuente () {
		return codigoFuente.getPos();
	}
	
	public boolean hayErrores () {
		if (errores.isEmpty()) 
			return false;
		else return true;
	}
	
	public void eliminarVarTS(String lex) {
		tablaDeSimbolo.remove(lex);
	}
}
