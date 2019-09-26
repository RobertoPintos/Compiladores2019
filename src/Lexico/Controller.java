package Lexico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import AccionesSemanticas.*;

public class Controller {

	//DEFINO OBJETOS DE LA CLASE
	
	//MATRIZ DE TRANSICION DE ESTADOS
	int [][] matEstados = {
			//      l   L   d    _   .   /   *   +   -    /n    =   >    <    :    (    )    %    e    E     ;    ,   BL,Tab 
			//      0   1   2    3   4   5   6   7   8    9   10   11   12   13   14   15   16   17   18    19   20   21
			/*0*/ { 1,  1,  2,  -1,  7,  9,  F,  F,  F,  -1,  16,  14,  15,  17,   F,   F,  18,  -1,  -1,   F,   F,   0}, 
			/*1*/ { 1,  1,  1,   1,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
			/*2*/ {-1, -1,  2,  -1,  3, -1, -1, -1, -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1},
			/*3*/ { F,  F,  3,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   4,   4,   F,   F,   F},
			/*4*/ {-1, -1,  5,  -1, -1, -1, -1,  6,  6,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1},
			/*5*/ { F,  F,  5,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
			/*6*/ { F,  F,  6,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
			/*7*/ {-1, -1,  8,  -1, -1, -1, -1, -1, -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1},
			/*8*/ { F,  F,  F,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   4,   4,   F,   F,   F},
			/*9*/ { F,  F,  F,   F,  F,  F,  F, 10,  F,   0,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
			/*10*/{10, 10, 10,  10, 10, 11, 10, 12, 10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10},
			/*11*/{-1, -1, -1,  -1, -1, -1, -1, -1, -1,  10,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1},
			/*12*/{10, 10, 10,  10, 10, 13, 10, 12, 10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10,  10},
			/*13*/{ 0,  0,  0,   0,  0,  0,  0,  0,  0,  12,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0},
			/*14*/{ F,  F,  F,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
			/*15*/{ F,  F,  F,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
			/*16*/{ F,  F,  F,   F,  F,  F,  F,  F,  F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F,   F},
			/*17*/{-1, -1, -1,  -1, -1, -1, -1, -1, -1,  -1,  -1,  -1,  -1,   F,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1},
			/*18*/{18, 18, 18,  18, 18, 19, 18, 18, 18,  18,  18,  18,  18,  18,  18,  18,   F,  18,  18,  18,  18,  18},
			/*19*/{18, 18, 18,  18, 18, 18, 18, 18, 18,   0,  18,  18,  18,  18,  18,  18,  18,  18,  18,  18,  18,  18}
	};
	
	//CONSTANTES IDENTIFICADORAS
	
	public static final int ID = 257;
	public final static short CTE=258;
	public final static short CTEFLOAT=258;
	public final static short CADENA=259;
	public final static short COMENTARIO=260;
	public final static short ASIGNACION=261;
	public final static short C_MAYORIGUAL=262;
	public final static short C_MENORIGUAL=263;
	public final static short C_DISTINTO=264;
	public final static short C_IGUAL=277;
	
	
	//PALABRAS RESERVADAS DEL CODIGO
	public static final int IF = 265;
    public static final int ELSE = 266;
    public static final int END_IF = 267;
    public static final int INT = 268;
    public static final int BEGIN = 269;
    public static final int END = 270;
    public static final int PRINT = 271;
    public final static short WHILE =272;
	public final static short DO=273;
	public final static short CLASS =274;
	public final static short PUBLIC=275;
	public final static short PRIVATE=276;
	
	//SUPUESTO ESTADO FINAL
	private static int F = 500;

	//ARRAYS WARNING AND ERRORES
	public static List<Error> errores = new ArrayList<>();
	public static List<Error> warning = new ArrayList<>();
	
	// VARIABLES DE CODIGO
	String fuente;
	public static int posFuente;
	public static int nroLinea;
		
	public static Token token;
	private String buffer = new String();

	public static HashMap<String,Atributo> tablaDeSimbolo;
	public static HashMap<String,Integer> palabrasReservadas = new HashMap<>();
	
	public int get(int fila, int columna) {
		return matEstados[fila][columna];
	}
	
	//DEVUELVE LA COLUMNA DE LA MATRIZ CORRESPONDIENTE AL SIMBOLO LEIDO
	private int getColumna(char c) {
		int value = (int)c;
		if (value >= 97 && value <= 122)  //Detecta Letra minuscula
			return 0;
		if (value >= 65 && value <= 90)  //Detecta Letra mayuscula
				return 1;
		if ( (value >= 48 && value <= 57) )//Detecta digito
			return 2;
		if (value == 95) //Detecta "_"
			return 3;
		if (value == 46) //Detecta "."
			return 4;
		if (value == 47) //Detecta "/"
			return 5;
		if (value == 42) //Detecta "*"
			return 6;
		if (value == 43) //Detecta +
			return 7;
		if (value == 45) //Detecta -
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
}
