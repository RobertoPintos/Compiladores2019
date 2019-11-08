%{
package Sintactico; 
import Lexico.Controller;
import Lexico.Token;
import Lexico.Atributo;
import java.util.*;
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

print : PRINT '(' CADENA ')' ';'  {lexico.getLexico().agregarEstructura( "En la linea "+lexico.getLexico().getNroLinea()+" se hizo un print");}
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

condicion 	:  expresion comparador expresion
			|  comparador expresion { System.out.println("Falta la primera expresion en la condicion"); lexico.getLexico().addError("Falta la primera expresion en la condicion", lexico.getLexico().getNroLinea());}
			|  expresion comparador error { System.out.println("Falta la segunda expresion en la condicion"); lexico.getLexico().addError("Falta la segunda expresion en la condicion", lexico.getLexico().getNroLinea());}
           	;

comparador 	: '<'
	   		| '>'
	   		| '=' 
       		| C_MAYORIGUAL
	   		| C_MENORIGUAL
	   		| C_DISTINTO
	   		| C_IGUAL
            ;

seleccion : if_condicion END_IF            {lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una condicion IF");}
          | if_condicion ELSE bloque_anidado END_IF {lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una condicion IF con ELSE");}
          ;

if_condicion : IF '(' condicion ')' bloque_anidado 
             | IF condicion ')' bloque_anidado {lexico.getLexico().addError("Falta el '(' de la condicion ", lexico.getLexico().getNroLinea());}
             | IF '(' condicion  bloque_anidado {lexico.getLexico().addError("Falta el ')' de la condicion ", lexico.getLexico().getNroLinea());}
             | IF condicion bloque_anidado {lexico.getLexico().addError("Faltan los parentesis de la condicion", lexico.getLexico().getNroLinea());}
             ;

bloque_anidado  : sentencia
				| BEGIN bloque_sentencias END
				| BEGIN bloque_sentencias error {lexico.getLexico().addError("Falta el END en el bloque anidado", lexico.getLexico().getNroLinea());}
				| error bloque_sentencias END {lexico.getLexico().addError("Falta el BEGIN en el bloque anidado", lexico.getLexico().getNroLinea());}
				;

asig 	: ID ASIGNACION expresion ';' {lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una asignacion");System.out.println("Realizo la asignacion en la linea: "+lexico.getLexico().getNroLinea());}
        | ID expresion ';' { System.out.println("Error, falta el ':=' de la asignacion"); lexico.getLexico().addError("Falta el ':=' de la asignacion ", lexico.getLexico().getNroLinea());} 
        | error ASIGNACION expresion ';' { System.out.println("Error, falta el ID a la izquierda de la asignacion"); lexico.getLexico().addError("Falta el ID de la asignacion ", lexico.getLexico().getNroLinea());}
        | ID ASIGNACION expresion  {lexico.getLexico().addError("Falta el ';' que cierra la asignacion.", lexico.getLexico().getNroLinea());}
        ;

expresion 	:  expresion '+' termino {lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una suma");} 
	  		| expresion '-' termino {lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una resta");}
      		| termino {System.out.println("Paso de termino a expresion");}   
          	;

termino : termino '*' factor {lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una multiplicacion");} 
		| termino '/' factor {lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una division");}
		| factor { System.out.println("Paso factor a termino"); }
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


private Controller lexico;
public Parser(Controller lexico)
{
  this.lexico = lexico;
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

