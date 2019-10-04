package Lexico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import AccionesSemanticas.*;

public class Controller {

	//DEFINO OBJETOS DE LA CLASE
	
	//ACCIONES SEMANTICAS
	private AccSemantica as1 = new AS1();
	private AccSemantica as2 = new AS2();
	private AccSemantica as3 = new AS3();
	private AccSemantica as4 = new AS4();
	private AccSemantica err1 = new Error1();
	private AccSemantica err2 = new Error2();
	private AccSemantica blanco = new ASBlanco();
	
	//MATRIZ DE TRANSICION DE ESTADOS
	int [][] matEstados = {
			//      l   L   d    _   .   /   *   +   -    /n    =   >    <    :    (    )    %    e    E     ;    ,  BL,Tab 
			//      0   1   2    3   4   5   6   7   8    9   10   11   12   13   14   15   16   17   18    19   20   21
			/*0*/ { 1,  1,  2,   F,  7,  9,  F,  F,  F,   0,  14,  12,  13,  15,   F,   F,  16,   1,   1,   F,   F,   0}, 
			/*1*/ { 1,  1,  1,   1,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
			/*2*/ { F,  F,  2,   F,  3,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   0,   0,   F,   F,   F},
			/*3*/ { F,  F,  3,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   4,   4,   F,   F,   F},
			/*4*/ { 0,  0,  5,   0,  0,  0,  0,  6,  6,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0},
			/*5*/ { F,  F,  5,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
			/*6*/ { F,  F,  6,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
			/*7*/ { 0,  0,  8,   0,  0,  0,  0,  0,  0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0},
			/*8*/ { F,  F,  8,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   4,   4,   F,   F,   F},
			/*9*/ { F,  F,  F,   F,  F,  F,  F, 10,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
			/*10*/{10, 10, 10,  10, 10, 10, 10, 11, 10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10},
			/*11*/{10, 10, 10,  10, 10,  0, 10, 11, 10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10},
			/*12*/{ F,  F,  F,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
			/*13*/{ F,  F,  F,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
			/*14*/{ F,  F,  F,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
			/*15*/{ 0,  0,  0,   0,  0,  0,  0,  0,  0,   0,   F,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0},
			/*16*/{16, 16, 16,  16, 16, 16, 16, 16, 16,   0,  16,  16,  16,  16,  16,  16,   F,  16,  16,  16,  16,  16},
	};
	
	//MATRIZ DE ACCIONES SEMANTICAS
	private AccSemantica [][] matAS = {
				//	    l     L     d    _    .    /    *    +    -     /n      =   >    <    :    (    )    %    e    E     ;    ,    BL,Tab 
				//      0     1     2    3    4    5    6    7    8     9      10   11   12   13   14   15   16   17   18    19   20     21
				/*0*/ { as1, as1,  as1, as2, as1, as1, as2, as2, as2, blanco,  as1, as1, as1, as1, as2, as2, as1, as1, as1,  as2, as2, blanco}, 
				/*1*/ { 1,  1,  1,   1,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
				/*2*/ { F,  F,  2,   F,  3,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   0,   0,   F,   F,   F},
				/*3*/ { F,  F,  3,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   4,   4,   F,   F,   F},
				/*4*/ { 0,  0,  5,   0,  0,  0,  0,  6,  6,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0},
				/*5*/ { F,  F,  5,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
				/*6*/ { F,  F,  6,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
				/*7*/ { 0,  0,  8,   0,  0,  0,  0,  0,  0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0},
				/*8*/ { F,  F,  8,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   4,   4,   F,   F,   F},
				/*9*/ { F,  F,  F,   F,  F,  F,  F, 10,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
				/*10*/{10, 10, 10,  10, 10, 10, 10, 11, 10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10},
				/*11*/{10, 10, 10,  10, 10,  0, 10, 11, 10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10},
				/*12*/{ F,  F,  F,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
				/*13*/{ F,  F,  F,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
				/*14*/{ F,  F,  F,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
				/*15*/{ err2,err2,err2,err2,err2,err2,err2,err2,err2,  err2,   F,  err2,err2,err2,err2,err2,err2,err2,err2,err2,err2,  err2},
				/*16*/{ as3, as3,  as3, as3, as3, as3, as3, as3, as3,   err1,  as3, as3, as3, as3, as3,  as3, as4, as3, as3,  as3, as3, as3},	
	};
	
	//CONSTANTES IDENTIFICADORAS
	
	public static final int ID = 257;
	public final static short CTE=258;
	public final static short CADENA=259;
	public final static short COMENTARIO=260;
	public final static short ASIGNACION=261;
	public final static short C_MAYORIGUAL=262;
	public final static short C_MENORIGUAL=263;
	public final static short C_DISTINTO=264;
	public final static short C_IGUAL=277;
	public final static short CTEFLOAT=278;
	
	//PALABRAS RESERVADAS DEL CODIGO
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
	public final static short FLOAT = 279;

	
	//SUPUESTO ESTADO FINAL
	private static int F = 500;

	//ARRAYS WARNING AND ERRORES
	public static List<Error> errores = new ArrayList<>();
	public static List<Error> warning = new ArrayList<>();
	
	// VARIABLES DE CODIGO
	private Fuente codigoFuente;
	private int nroLinea;
	public String buffer = new String();	
	public static Token token;
	private int estado;
	
	//VARIABLES DE CONTROL
	public static final int maxId = 25;
	public static final float minF = 0.0; //definir bien los maximos de los float
	public static final float maxf = 1.0;
	public static final int minE = -32768;
	public static final int maxE = 32767;

	public static HashMap<String,String> tablaDeSimbolo;
	public static HashMap<String,Integer> palabrasReservadas = new HashMap<>();
	public static List<Token> listToken = new ArrayList<Token>(); 
	
	
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

	}
	
	//CONSTRUCTOR
	
	public Controller(Fuente cf) {
		this.codigoFuente = cf;
		cargarPalabrasReservadas();
	}
	
	//METODO CENTRAL, DEVUELVE DE A UN TOKEN DEL CODIGO FUENTE, EN EL FUTURO ES yylex()
	
	public Token getToken() {
		estado = 0;
		char charLeido;
		int columna;
		int estadoSig;
	}
	
// ---------------------------METODOS ADICIONALES-----------------------------------------
	
	public int get(int fila, int columna) {
		return matEstados[fila][columna];
	}
	
	//DEVUELVE LA COLUMNA DE LA MATRIZ CORRESPONDIENTE AL SIMBOLO LEIDO
	private int getColumna(char c) {
		int value = (int)c;
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
		if ((value == 101)) // e
			return 17;
		if ((value == 69)) // E
			return 18;
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
                default: return ID;
            }
        } else 
             if(lex.length()>1){
            	if ((lex.charAt(0) == "/") && (lex.charAt(1) == "+"))
            		return COMENTARIO;
            	else if (lex.charAt(0) == "%")
            			return CADENA;
            		else {
            			if (lex.contains(".")) 
            				return CTEFLOAT;
            			// if (lex = cte) FALTA DETERMINAR SI EL LEXEMA ES UNA CTE
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
            		}
         return (int) lex.charAt(0);  //CASO DE LOS TOKEN SIMPLES (  ) , ; ETC
		
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
		Token t = new Token(getIdentificador(s), s, nLinea));
		listToken.add(t);
	}
	
	public void addTokenListCompuesto (String lex, int nLinea) {
		Token t = new Token(getIdentificador(lex), lex, nLinea)
		listToken.add(t);
	}
	
	public void addTokenTS(String lex, String t) {
		if (tablaDeSimbolo.containsKey(lex))
			System.out.println("El lexema " + lex + " ya existe.");
		else if (lex.charAt(0) == "%") {
				String nuevo = lex.substring(1);
				tablaDeSimbolo.put(nuevo, t);
			} else 
				tablaDeSimbolo.put(lex, t);
	}
	
	public void addError(String desc, int nLinea) {
		Error e = new Error(desc, nLinea);
		errores.add(e);
	}
	
	public void setEstadoFinal() {
		estado = F;
	}
	
	public int getNroLinea() {
		return nroLinea;
	}
	
	public void setBuffer(String s) {
		buffer = s;
	}
	
	public void recorrerCodFuente() {
		while (!Fuente.hasFinished()) {
			getToken();
		}
	}
}
