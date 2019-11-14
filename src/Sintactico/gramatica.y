%{
package Sintactico; 
import Lexico.Controller;
import Lexico.Token;
import Lexico.Atributo;
import java.util.*;
import GeneracionDeCodigo.*;
%}

%token ID
%token CTE
%token CADENA
%token COMENTARIO
%token ASIGNACION
%token C_MAYORIGUAL
%token C_MENORIGUAL
%token C_DISTINTO
%token IF
%token ELSE
%token END_IF
%token INT
%token BEGIN
%token END
%token PRINT
%token WHILE
%token DO 
%token CLASS
%token PUBLIC
%token PRIVATE
%token C_IGUAL
%token CTEFLOAT
%token FLOAT
%token VOID
%token EXTENDS

%start programa

%%



programa :   conjunto_sentencias
           ;

conjunto_sentencias : conj_sentencias_declarativas sentencias_ejecutables {System.out.println("Finaliza el programa");}
					| conj_sentencias_declarativas
					| sentencias_ejecutables
					;
					
conj_sentencias_declarativas : sentencia_declarativa 
							 | sentencia_declarativa conj_sentencias_declarativas 
							 ;

sentencia_declarativa  :  tipo lista_de_variables ';' {System.out.println("Llegue a una declaracion valida");
													   			  addVariableTS(((Token)$1.obj).getLexema());}
					   |  sentencia_de_clase {System.out.println("Llegue a una declaracion de clase");}
           	 		   ;


lista_de_variables : lista_de_variables ',' variable {listaVar.add(((Token)$3.obj));}
		    	   | variable {listaVar.add(((Token)$1.obj));}
	               ;

variable : ID {lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego el identificador: "+ ((Token)$1.obj).getLexema());}
		 ; 

  
sentencia_de_clase  : CLASS ID {setClass(((Token)$2.obj).getLexema());} definicion_clase {lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se creó un componente de clase con el nombre: "+((Token)$2.obj).getLexema());
																																											 										  addClaseTS(((Token)$2.obj).getLexema());
																																											 										  						classFlag = false;
																																											 										  						   className = "";}
					| CLASS ID {setClass(((Token)$2.obj).getLexema());} EXTENDS ID definicion_clase {lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se creó un componente de clase extendida con el nombre: "+((Token)$2.obj).getLexema()+", que extiende de: "+((Token)$5.obj).getLexema());
																																																														 addClaseHeredadaTS(((Token)$2.obj).getLexema(),((Token)$5.obj).getLexema());
																																																														 														   classFlag = false;
																																											 										  						   																		  className = "";}
					;

definicion_clase : BEGIN sentencias_clase END
				 ;
			
sentencias_clase : sentencias_clase cuerpo_clase
				 | cuerpo_clase
				 ;
				
cuerpo_clase : lista_atributos
			 | metodo_clase
			 ;				
					  
lista_atributos : visibilidad sentencia_declarativa {lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se creó un atributo de clase");}
				;
									 
metodo_clase : visibilidad VOID ID '(' ')' bloque_anidado_metclase {lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se creó un metodo de clase"); 
																																				  System.out.println("Viene un metodo");
																												  addMetodoTS(((Token)$1.obj).getLexema(), ((Token)$3.obj).getLexema());}
			 ;

bloque_anidado_metclase : BEGIN bloque_sentencias END
						;


tipo : FLOAT { System.out.println("Viene una variable tipo FLOAT");}
	 | INT    { System.out.println("Viene una variable tipo INT" );}
   	 | ID { System.out.println("Viene una variable de la clase: "+ID );}
     ;

visibilidad : PUBLIC
		 	| PRIVATE
		 	;

sentencias_ejecutables : BEGIN bloque_sentencias END ';'
					   | error bloque_sentencias END ';' {lexico.getLexico().addError("Falta el BEGIN para el bloque de sentencias ejecutables.", lexico.getLexico().getNroLinea());}
					   | BEGIN bloque_sentencias error ';' {lexico.getLexico().addError("Falta el END para el bloque de sentencias ejecutables.", lexico.getLexico().getNroLinea());}
					   | BEGIN bloque_sentencias END error {lexico.getLexico().addError("Falta el ';' que cierra el bloque de sentencias ejecutables.", lexico.getLexico().getNroLinea());}
					   ;

bloque_sentencias : bloque_sentencias sentencia
				  |	sentencia {System.out.println("Se cargo una sentencia");} 
       			  ;

sentencia   :  ejecucion
            ;

ejecucion : iteracion 
	 	  | seleccion 
	  	  | print 
	 	  | asig
	  	  | invocacion_metodo_clase
	  	  ;		
	  
invocacion_metodo_clase : ID '.' ID '(' ')' ';'
						| error_inv_metodo_clase
						;
			
error_inv_metodo_clase : error '.' ID '(' ')' ';' {lexico.getLexico().addError("Falta la clase que contiene el metodo", lexico.getLexico().getNroLinea());}
	   				   | ID '.' error '(' ')' ';' {lexico.getLexico().addError("Falta definir el metodo de clase ", lexico.getLexico().getNroLinea());}
					   | ID '.'  error ')' ';' {lexico.getLexico().addError("Falta un parentesis ", lexico.getLexico().getNroLinea());}
					   | ID '.' ID '(' error ';' {lexico.getLexico().addError("Falta un parentesis ", lexico.getLexico().getNroLinea());}
					   | ID '.' ID '(' ')' error {lexico.getLexico().addError("Falta el ';' ", lexico.getLexico().getNroLinea());}
					   ;

print : PRINT '(' CADENA ')' ';'  {lexico.getLexico().agregarEstructura( "En la linea "+lexico.getLexico().getNroLinea()+" se hizo un print");
																	addTercetoPrint(((Token)$1.obj).getLexema(), ((Token)$3.obj).getLexema());}
	  | error_print
	  ;


error_print : PRINT error CADENA ')' ';' {lexico.getLexico().addError("Falta el '('.", lexico.getLexico().getNroLinea());}
	        | PRINT '(' CADENA error ';' {lexico.getLexico().addError("Falta el ')'.", lexico.getLexico().getNroLinea());} 
	        | PRINT '(' CADENA ')' error {lexico.getLexico().addError("Falta el ';' que cierra el print.", lexico.getLexico().getNroLinea());}
	        ;


iteracion   : WHILE '(' condicion ')' DO bloque_anidado {lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se creo una iteracion");}
			| WHILE condicion ')' DO bloque_anidado {lexico.getLexico().addError("Falta algun parentesis en la iteracion", lexico.getLexico().getNroLinea());}
			| WHILE '(' condicion DO bloque_anidado {lexico.getLexico().addError("Falta algun parentesis en la iteracion", lexico.getLexico().getNroLinea());}
	   		| WHILE '(' condicion ')' bloque_anidado {lexico.getLexico().addError("Falta la palabra DO en la iteracion", lexico.getLexico().getNroLinea());}
			;

condicion 	:  expresion {if (genCodigo.getTercetosController().getTercetoExp() != null)
							 cmp1 = true;}												comparador expresion { if (genCodigo.getTercetosController().getTercetoExp() != null)
							 																						cmp2 = true;
																												addTercetoCondicion(((Token)$1.obj).getLexema(), ((Token)$3.obj).getLexema(), ((Token)$4.obj).getLexema());}
           	;



comparador 	: '<'
	   		| '>'
	   		| '=' 
       		| C_MAYORIGUAL
	   		| C_MENORIGUAL
	   		| C_DISTINTO
	   		| C_IGUAL
            ;

seleccion : if_condicion END_IF {lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una condicion IF");
																														   completarTercetoFinalIF();}
          | if_condicion {apilarTercetoIncompletoIF();} ELSE bloque_anidado END_IF {lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una condicion IF con ELSE");
          																																											   completarTercetoFinalIF();}
          ;

if_condicion : IF '(' condicion ')' {apilarTercetoIncompletoIF();} bloque_anidado 
             | IF condicion ')' bloque_anidado {lexico.getLexico().addError("Falta el '(' de la condicion ", lexico.getLexico().getNroLinea());}
             | IF '(' condicion  bloque_anidado {lexico.getLexico().addError("Falta el ')' de la condicion ", lexico.getLexico().getNroLinea());}
             | IF condicion bloque_anidado {lexico.getLexico().addError("Faltan los parentesis de la condicion", lexico.getLexico().getNroLinea());}
             ;

bloque_anidado  : sentencia
				| BEGIN bloque_sentencias END
				| BEGIN bloque_sentencias error {lexico.getLexico().addError("Falta el END en el bloque anidado", lexico.getLexico().getNroLinea());}
				| error bloque_sentencias END {lexico.getLexico().addError("Falta el BEGIN en el bloque anidado", lexico.getLexico().getNroLinea());}
				;

asig 	: ID ASIGNACION expresion ';' {lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una asignacion");System.out.println("Realizo la asignacion en la linea: "+lexico.getLexico().getNroLinea());
																																									 addTercetoAsignacion(((Token)$1.obj).getLexema(), ((Token)$3.obj).getLexema());}
        | ID expresion ';' { System.out.println("Error, falta el ':=' de la asignacion"); lexico.getLexico().addError("Falta el ':=' de la asignacion ", lexico.getLexico().getNroLinea());} 
        | error ASIGNACION expresion ';' { System.out.println("Error, falta el ID a la izquierda de la asignacion"); lexico.getLexico().addError("Falta el ID de la asignacion ", lexico.getLexico().getNroLinea());}
        | ID ASIGNACION expresion  {lexico.getLexico().addError("Falta el ';' que cierra la asignacion.", lexico.getLexico().getNroLinea());}
        ;

expresion 	:  expresion '+' termino {lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una suma");
									   addTercetoExpresion(((Token)$2.obj).getLexema(), ((Token)$1.obj).getLexema(), ((Token)$3.obj).getLexema());}  
	  		| expresion '-' termino {lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una resta");
	  								   addTercetoExpresion(((Token)$2.obj).getLexema(), ((Token)$1.obj).getLexema(), ((Token)$3.obj).getLexema());} 
      		| termino {System.out.println("Paso de termino a expresion");
      												   cambiarTercetos();}   
          	;

termino : termino '*' factor {lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una multiplicacion");
										 addTercetoTermino(((Token)$2.obj).getLexema(), ((Token)$1.obj).getLexema(), ((Token)$3.obj).getLexema());} 
		| termino '/' factor {lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una division");
								   addTercetoTermino(((Token)$2.obj).getLexema(), ((Token)$1.obj).getLexema(), ((Token)$3.obj).getLexema());} 
		| factor { System.out.println("Paso factor a termino");}
        ;

factor  : ID {System.out.println("Cargo un identificador");}
		| ID '.' ID {System.out.println("Cargue la variable "+ID+" de la clase "+ID);}
		| cte {System.out.println("Paso de cte a factor");}
		;


cte : CTE {System.out.println("Leo una constante INT");}
	| '-' CTE {System.out.println("Leo una constante negada");
		  actualizarTablaNegativo(((Token)$2.obj).getLexema());
		  	   System.out.println("cambio la cte");}
    | CTEFLOAT {System.out.println("Leo una constante FLOAT");}
    | '-' CTEFLOAT {System.out.println("Leo una float negada");}
	;




%%


private ArrayList <Token> listaVar = new ArrayList <Token>();
private boolean classFlag;
private String className;
private boolean cmp1 = false;
private boolean cmp2 = false;


private Controller lexico;
private TercetosController genCodigo;
public Parser(Controller lexico, TercetosController tc)
{
  this.lexico = lexico;
  this.genCodigo = tc;
} 

public int yylex(){
    Token token = this.lexico.getToken();
   	if(token != null ){ 
    	int val = token.getId();
    	yylval = new ParserVal(token);
    	return val;
	}
   	else return 0;
}

public void yyerror(String s){
    System.out.println("Parser: " + s);
}

public int yyparser(){
	return yyparse();
}


public void addVariableTS (String type) {
	for (Token t : listaVar) {
		System.out.println(t.getLexema());
		if (classFlag == true) {
			if (type.equals("int"))
				lexico.getLexico().addVarTS(t.getLexema(), type, "Atributo de clase", 0, className, "-");
			else if (type.equals("float"))
				lexico.getLexico().addVarTS(t.getLexema(), type, "Atributo de clase", 0.0, className, "-");	
		} else {
			if (type.equals("int"))
				lexico.getLexico().addVarTS(t.getLexema(), type, "Variable", 0, "-", "-");
			else if (type.equals("float"))
				lexico.getLexico().addVarTS(t.getLexema(), type, "Variable", 0.0, "-", "-");	
		}
	}	
	removeVars();
}

public void setClass(String cn) {
	classFlag = true;
	className = cn;
}

public void removeVars() {
	int size = listaVar.size();
	for (int i = 0; i < size; i++) 
		listaVar.remove(0);
}

public void addClaseTS(String lex) {
	lexico.getLexico().addClassTS(lex);
}

public void addClaseHeredadaTS(String lex, String lex2){
	lexico.getLexico().addClassHeredadaTS(lex,lex2);
}

public void addMetodoTS(String vis, String lex) {
	lexico.getLexico().addMethodTS(vis, lex, className);
}

private void actualizarTablaDeSimbolosCte(String lexema, String nuevoLexema, Atributo atr){

	if(atr.getCantRef() < 1){
		lexico.getTS().remove(lexema);
	}else
		//lexico.getTS().get(lexema).decrementoCantRef();
	if(lexico.getTS().containsKey(nuevoLexema)){
		lexico.getTS().get(nuevoLexema).incrementoCantRef();
	}else{
		lexico.getTS().put(nuevoLexema, new Atributo(atr.getTipo(), atr.getUso(), atr.getValor(), atr.getDeClase(), atr.getClasePadre(), 1));
	}
}

public void actualizarTablaNegativo(String lexema){
	
	String nuevoLexema = "-"+lexema;
	if(lexico.getTS().containsKey(lexema)){
		lexico.getTS().get(lexema).decrementoCantRef();
		Atributo atr = lexico.getTS().get(lexema);
		System.out.println(atr.getCantRef());
		if(atr.getTipo() == "int"){
			int i = Integer.parseInt(nuevoLexema);
			if ((i <= lexico.maxE) && (i >= lexico.minE)) {
				actualizarTablaDeSimbolosCte(lexema, nuevoLexema, atr);
			} else 
				lexico.getLexico().addError("CTE fuera de rango: "+nuevoLexema, lexico.getLexico().getNroLinea());
		}else{
		 	float valor = Float.parseFloat(nuevoLexema);
			if (((valor > lexico.minPosF) && (valor < lexico.maxPosF)) || ((valor > lexico.minNegF) && (valor < lexico.maxNegF)) || (valor == lexico.zeroF)) {
				actualizarTablaDeSimbolosCte(lexema, nuevoLexema, atr);
			} else
				lexico.getLexico().addError("CTE fuera de rango: "+nuevoLexema, lexico.getLexico().getNroLinea());
		}
	}
}

public void addTercetoPrint (String lex, String cad) {

	String aux = cad.substring(1, cad.length()-1);
	Terceto t = new Terceto (lex.toUpperCase(), aux, "-", genCodigo.getTercetosController().getCantTercetos()+1);
	genCodigo.getTercetosController().addTercetoLista(t);	
}

public void addTercetoAsignacion (String op1, String op2) {
	if ( genCodigo.getTercetosController().getTercetoExp() != null ) {
		Terceto t = new Terceto (":=", op1, "["+Integer.toString(genCodigo.getTercetosController().getTercetoExp().getNumTerceto())+"]", genCodigo.getTercetosController().getCantTercetos()+1);
		genCodigo.getTercetosController().addTercetoLista(t);
	} else {
		Terceto t = new Terceto (":=", op1, op2, genCodigo.getTercetosController().getCantTercetos()+1);
		genCodigo.getTercetosController().addTercetoLista(t);
	}
}

public void addTercetoTermino (String operando, String op1, String op2) {
	
	if (genCodigo.getTercetosController().getTercetoTerm() == null) {
		Terceto t = new Terceto (operando, op1, op2, genCodigo.getTercetosController().getCantTercetos()+1);
		genCodigo.getTercetosController().addTercetoLista(t);
		genCodigo.getTercetosController().setTercetoTerm(t);
	} else {
		Terceto t = new Terceto (operando, "["+genCodigo.getTercetosController().getTercetoTerm().getNumTerceto()+"]", op2, genCodigo.getTercetosController().getCantTercetos()+1);
		genCodigo.getTercetosController().addTercetoLista(t);
		genCodigo.getTercetosController().setTercetoTerm(t);
	}
}

public void addTercetoExpresion (String operando, String op1, String op2) {
	if (genCodigo.getTercetosController().getTercetoExp() == null) {
		if (genCodigo.getTercetosController().getTercetoTerm() == null) {
			Terceto t = new Terceto (operando, op1, op2, genCodigo.getTercetosController().getCantTercetos()+1);
			genCodigo.getTercetosController().addTercetoLista(t);
			genCodigo.getTercetosController().setTercetoExp(t);
		} else {
			Terceto t = new Terceto (operando, op1, "["+genCodigo.getTercetosController().getTercetoTerm().getNumTerceto()+"]", genCodigo.getTercetosController().getCantTercetos()+1);
			genCodigo.getTercetosController().addTercetoLista(t);
			genCodigo.getTercetosController().setTercetoExp(t);
		}
	} else {
		if (genCodigo.getTercetosController().getTercetoTerm() == null) {
			Terceto t = new Terceto (operando, "["+genCodigo.getTercetosController().getTercetoExp().getNumTerceto()+"]", op2, genCodigo.getTercetosController().getCantTercetos()+1);
			genCodigo.getTercetosController().addTercetoLista(t);
			genCodigo.getTercetosController().setTercetoExp(t);
		} else {
			Terceto t = new Terceto (operando, "["+genCodigo.getTercetosController().getTercetoExp().getNumTerceto()+"]", "["+genCodigo.getTercetosController().getTercetoTerm().getNumTerceto()+"]", genCodigo.getTercetosController().getCantTercetos()+1);
			genCodigo.getTercetosController().addTercetoLista(t);
			genCodigo.getTercetosController().setTercetoExp(t);
		}
	}
}			

public void cambiarTercetos () {
	genCodigo.getTercetosController().setTercetoExp(genCodigo.getTercetosController().getTercetoTerm());
	genCodigo.getTercetosController().setTercetoTermNull();
} 

public void addTercetoCondicion (String op1, String lex, String op2) {
	
	if (cmp1 == true)
		if (cmp2 == true) {
			Terceto t = new Terceto (lex, "["+Integer.toString(genCodigo.getTercetosController().getCantTercetos()-1)+"]", "["+Integer.toString(genCodigo.getTercetosController().getCantTercetos())+"]", genCodigo.getTercetosController().getCantTercetos()+1);
			genCodigo.getTercetosController().addTercetoLista(t);
		} else {
			Terceto t = new Terceto (lex, "["+Integer.toString(genCodigo.getTercetosController().getCantTercetos())+"]", op2, genCodigo.getTercetosController().getCantTercetos()+1);
			genCodigo.getTercetosController().addTercetoLista(t);
		}
	else { if (cmp2 == true) {
				Terceto t = new Terceto (lex, op1, "["+Integer.toString(genCodigo.getTercetosController().getCantTercetos())+"]", genCodigo.getTercetosController().getCantTercetos()+1);
				genCodigo.getTercetosController().addTercetoLista(t);
			} else {
				Terceto t = new Terceto (lex, op1, op2, genCodigo.getTercetosController().getCantTercetos()+1);
				genCodigo.getTercetosController().addTercetoLista(t);
			}
		}
}

public void apilarTercetoIncompletoIF () {
	
	if (genCodigo.getTercetosController().isPilaEmpty()) {
		Terceto t = new Terceto ("BF", "["+Integer.toString(genCodigo.getTercetosController().getCantTercetos())+"]", "-", genCodigo.getTercetosController().getCantTercetos()+1);
		genCodigo.getTercetosController().addTercetoLista(t);
		genCodigo.getTercetosController().apilarTerceto(t);
	} else {
		Terceto aux = genCodigo.getTercetosController().getTercetoPila();
		aux.completarTerceto ("["+Integer.toString(genCodigo.getTercetosController().getCantTercetos()+2)+"]");
		genCodigo.getTercetosController().modificarTercetoLista(aux.getNumTerceto()-1, aux);
		genCodigo.getTercetosController().removeTercetoPila();
		Terceto BI = new Terceto ("BI", "-", "-", genCodigo.getTercetosController().getCantTercetos()+1);
		genCodigo.getTercetosController().addTercetoLista(BI);
		genCodigo.getTercetosController().apilarTerceto(BI);
	}
}

public void completarTercetoFinalIF () {

	if (!genCodigo.getTercetosController().isPilaEmpty()) {
		Terceto aux = genCodigo.getTercetosController().getTercetoPila();
		aux.completarTerceto ("["+Integer.toString(genCodigo.getTercetosController().getCantTercetos()+1)+"]");
		genCodigo.getTercetosController().modificarTercetoLista(aux.getNumTerceto()-1, aux);
		genCodigo.getTercetosController().removeTercetoPila();
	}
}
