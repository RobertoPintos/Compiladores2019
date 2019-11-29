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



programa :   conjunto_sentencias { addTercetoFinal(); }
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


lista_de_variables : lista_de_variables ',' variable {if(existeVar((Token)$3.obj)){
														System.out.println("Error variable ya declarada"); 
														assembler.getConversorAssembler().addErrorCI("variable ya declarada con ese nombre: "+((Token)$3.obj).getLexema(), lexico.getLexico().getNroLinea());
													  }else{	
														listaVar.add(((Token)$3.obj));
														}}
		    	   | variable  {if(existeVar((Token)$1.obj)){
		    	   					System.out.println("Error variable ya declarada"); 
									assembler.getConversorAssembler().addErrorCI("variable ya declarada con ese nombre: "+((Token)$1.obj).getLexema(), lexico.getLexico().getNroLinea());
		    	   				}else{
		    	   					listaVar.add((Token)$1.obj);
		    	   					}}
	               ;

variable : ID {if(lexico.getTS().containsKey(((Token)$1.obj).getLexema())){
			  		System.out.println("Error variable ya declarada"); 
					assembler.getConversorAssembler().addErrorCI("variable ya declarada con ese nombre: "+((Token)$1.obj).getLexema(), lexico.getLexico().getNroLinea());
			  }else{
				lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego el identificador: "+ ((Token)$1.obj).getLexema());
				}}
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
   	 | ID { System.out.println("Viene una variable de la clase: "+ ((Token)$1.obj).getLexema());
   	 							set = true;
   	 							clase = ((Token)$1.obj).getLexema();
   	 							if (estaDeclarada(clase))
   	 								existeClase = true;
   	 							else
   	 								existeClase = false;}
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
	  
invocacion_metodo_clase : ID '.' ID '(' ')' ';' { if (esClase(((Token)$1.obj).getLexema()))
														if (esMetodo(((Token)$3.obj).getLexema())){
															
														} else
															assembler.getConversorAssembler().addErrorCI("Se trato de invocar a un metodo inexistente", lexico.getLexico().getNroLinea());
												  else
												  		assembler.getConversorAssembler().addErrorCI("No existe la clase asociada al metodo", lexico.getLexico().getNroLinea());			
												}
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


iteracion   : WHILE '(' condicion ')' {apilarTercetoIncompletoWHILE();} DO bloque_anidado {lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se creo una iteracion");
																																										   apilarTercetoIncompletoWHILE();
																																								cmp1 = false; cmp2 = false; condStart = 0;}
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
																														   completarTercetoFinalIF();
																										   cmp1 = false; cmp2 = false; condStart = 0;}
          | if_condicion {apilarTercetoIncompletoIF();} ELSE bloque_anidado END_IF {lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una condicion IF con ELSE");
          																																											   completarTercetoFinalIF();
          																																											  cmp1 = false; cmp2 = false;}
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

asig 	: ID ASIGNACION expresion ';' { if (esMetodo(((Token)$1.obj).getLexema())) {
											lexico.getLexico().addError("No se puede hacer una asignacion a un metodo de clase.", lexico.getLexico().getNroLinea());
										} else {
											lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una asignacion");
											System.out.println("Realizo la asignacion en la linea: "+lexico.getLexico().getNroLinea());
											addTercetoAsignacion(((Token)$1.obj).getLexema(), ((Token)$3.obj).getLexema());
										} genCodigo.getTercetosController().setTercetoExpNull();
										  genCodigo.getTercetosController().setTercetoTermNull(); }
        | ID expresion ';' { System.out.println("Error, falta el ':=' de la asignacion"); lexico.getLexico().addError("Falta el ':=' de la asignacion ", lexico.getLexico().getNroLinea());} 
        | error ASIGNACION expresion ';' { System.out.println("Error, falta el ID a la izquierda de la asignacion"); lexico.getLexico().addError("Falta el ID de la asignacion ", lexico.getLexico().getNroLinea());}
        | ID ASIGNACION expresion  {lexico.getLexico().addError("Falta el ';' que cierra la asignacion.", lexico.getLexico().getNroLinea());}
        ;

expresion 	:  expresion '+' termino {
											lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una suma");
									   		addTercetoExpresion(((Token)$2.obj).getLexema(), ((Token)$1.obj).getLexema(), ((Token)$3.obj).getLexema());
									}
	  		| expresion '-' termino {
	  										lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una resta");
	  								  		addTercetoExpresion(((Token)$2.obj).getLexema(), ((Token)$1.obj).getLexema(), ((Token)$3.obj).getLexema());
	  								}
      		| termino {System.out.println("Paso de termino a expresion");
      												   cambiarTercetos();}   
          	;

termino : termino '*' factor {
										 lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una multiplicacion");
										 addTercetoTermino(((Token)$2.obj).getLexema(), ((Token)$1.obj).getLexema(), ((Token)$3.obj).getLexema());		 
							 }
		| termino '/' factor {
								   lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una division");
								   addTercetoTermino(((Token)$2.obj).getLexema(), ((Token)$1.obj).getLexema(), ((Token)$3.obj).getLexema());
							 } 
		| factor { System.out.println("Paso factor a termino");}
        ;

factor  : ID {System.out.println("Cargo un identificador");}
		| ID '.' ID {if (estaDeclarada(((Token)$3.obj).getLexema()))
						if (estaDeclarada(((Token)$1.obj).getLexema()))
							System.out.println("Cargue la variable "+((Token)$3.obj).getLexema()+" de la clase "+((Token)$1.obj).getLexema());}
		| cte {System.out.println("Paso de cte a factor");}
		;


cte : CTE {System.out.println("Leo una constante INT");}
	| '-' CTE {System.out.println("Leo una constante negada");
		  actualizarTablaNegativo(((Token)$2.obj).getLexema());}
    | CTEFLOAT {System.out.println("Leo una constante FLOAT");}
    | '-' CTEFLOAT {System.out.println("Leo una float negada");
    		actualizarTablaNegativo(((Token)$2.obj).getLexema());}
	;




%%


private ArrayList <Token> listaVar = new ArrayList <Token>();
private boolean existeClase;
private String clase = "";
private boolean set;
private boolean classFlag;
private String className;
private boolean cmp1 = false;
private boolean cmp2 = false;
private int condStart = 0;


private Controller lexico;
private TercetosController genCodigo;
private ConversorAssembler assembler;
public Parser(Controller lexico, TercetosController tc, ConversorAssembler assembler)
{
  this.lexico = lexico;
  this.genCodigo = tc;
  this.assembler = assembler;
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
		if (classFlag == true) {
			if (type.equals("int"))
				lexico.getLexico().addVarTS(t.getLexema(), type, "Atributo de clase", 0, className, "-");
			else if (type.equals("float"))
				lexico.getLexico().addVarTS(t.getLexema(), type, "Atributo de clase", 0.0, className, "-");	
		} else {
			if (set)
				if (existeClase)
					lexico.getLexico().addVarTS(t.getLexema(), type, "Objeto", 0, type, "-");
				else
					lexico.getLexico().addError("La clase del objeto no esta declarada", lexico.getLexico().getNroLinea());
			else {
			if (type.equals("int"))
				lexico.getLexico().addVarTS(t.getLexema(), type, "Variable", 0, "-", "-");
			else if (type.equals("float"))
				lexico.getLexico().addVarTS(t.getLexema(), type, "Variable", 0.0, "-", "-");	
			}
		}
	}	
	removeVars();
	set = false;
	existeClase = false;
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
	if (!esClase(lex))
		lexico.getLexico().addClassTS(lex);
	else
		assembler.getConversorAssembler().addErrorCI("Clase ya declarada anteriormente", lexico.getLexico().getNroLinea());
}

public void addClaseHeredadaTS(String lex, String lex2){
	if (!esClase(lex))
		lexico.getLexico().addClassHeredadaTS(lex,lex2);
	else
		assembler.getConversorAssembler().addErrorCI("Clase ya declarada anteriormente", lexico.getLexico().getNroLinea());
}

public void addMetodoTS(String vis, String lex) {
	if (!esMetodo(lex))
		lexico.getLexico().addMethodTS(vis, lex, className);
	else
		assembler.getConversorAssembler().addErrorCI("Metodo ya declarado anteriormente", lexico.getLexico().getNroLinea());
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
	
	if (estaDeclarada(lexema)) {
		Atributo a = lexico.getLexico().getTS().get(lexema);
		a.decrementoCantRef();
		lexico.getLexico().getTS().replace(lexema, a);
		String nuevoLex = '-'+lexema;
		if (estaDeclarada(nuevoLex)) {
			Atributo b = lexico.getLexico().getTS().get(nuevoLex);
			b.incrementoCantRef();
			lexico.getLexico().getTS().replace(nuevoLex, b);
		} else {
			if (isInteger(nuevoLex)) {
				Atributo c = new Atributo ("CONST INT", "CONST INT", 0, "-", "-", 1);
				lexico.getLexico().getTS().put(nuevoLex, c);
			} else if (isFloat(nuevoLex)) {
				Atributo c = new Atributo ("CONST FLOAT", "CONST FLOAT", 0.0, "-", "-", 1);
				lexico.getLexico().getTS().put(nuevoLex, c);
			}
		}
	} else {
		String nuevoLex = '-'+lexema;
		if (isInteger(nuevoLex)) {
			Atributo c = new Atributo ("CONST INT", "CONST INT", 0, "-", "-", 1);
			lexico.getLexico().getTS().put(nuevoLex, c);
		} else if (isFloat(nuevoLex)) {
			Atributo c = new Atributo ("CONST FLOAT", "CONST FLOAT", 0.0, "-", "-", 1);
			lexico.getLexico().getTS().put(nuevoLex, c);
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
		if (estaDeclarada(op1)) {
			String tipo1 = lexico.getLexico().getTS().get(op1).getTipo();
			String tipo2 = genCodigo.getTercetosController().getTercetoExp().getTipoOp();
			if (chequeoTipo(tipo1, tipo2)) {
				Terceto t = new Terceto (":=", op1, "["+Integer.toString(genCodigo.getTercetosController().getTercetoExp().getNumTerceto())+"]", genCodigo.getTercetosController().getCantTercetos()+1);
				t.setTipoOp(tipo1);
				genCodigo.getTercetosController().addTercetoLista(t);
			} else
				lexico.getLexico().addError("Tipos incompatibles en la operacion", lexico.getLexico().getNroLinea());
		} else
			lexico.getLexico().addError("La variable "+op1+" no esta declarada.", lexico.getLexico().getNroLinea());
	} else {
		if (estaDeclarada(op1))
			if (estaDeclarada(op2)) {
				String tipo1 = lexico.getLexico().getTS().get(op1).getTipo();
				String tipo2 = lexico.getLexico().getTS().get(op2).getTipo();
				if (chequeoTipo(tipo1, tipo2)) {
					Terceto t = new Terceto (":=", op1, op2, genCodigo.getTercetosController().getCantTercetos()+1);
					t.setTipoOp(tipo1);
					genCodigo.getTercetosController().addTercetoLista(t);
				} else {
					lexico.getLexico().addError("Tipos incompatibles en la operacion", lexico.getLexico().getNroLinea());
				}
			} else {
			lexico.getLexico().addError("La variable "+op2+" no esta declarada.", lexico.getLexico().getNroLinea());
			}
		else {
		lexico.getLexico().addError("La variable "+op1+" no esta declarada.", lexico.getLexico().getNroLinea());
		}
	}
}

public void addTercetoTermino (String operando, String op1, String op2) {

		if (genCodigo.getTercetosController().getTercetoTerm() == null) {
			if (estaDeclarada(op1))
				if(estaDeclarada(op2)) {
					String tipo1 = lexico.getLexico().getTS().get(op1).getTipo();
					String tipo2 = lexico.getLexico().getTS().get(op2).getTipo();
					if (chequeoTipo(tipo1, tipo2)) {
						Terceto t = new Terceto (operando, op1, op2, genCodigo.getTercetosController().getCantTercetos()+1);
						asociarVarAuxTerceto(t, tipo1);
						t.setTipoOp(tipo1);
						genCodigo.getTercetosController().addTercetoLista(t);
						genCodigo.getTercetosController().setTercetoTerm(t);
					} else
						lexico.getLexico().addError("Tipos incompatibles en la operacion", lexico.getLexico().getNroLinea());
				} else
					lexico.getLexico().addError("La variable: "+op2+" no esta declarada.", lexico.getLexico().getNroLinea());
			else 
				lexico.getLexico().addError("La variable: "+op1+" no esta declarada.", lexico.getLexico().getNroLinea());
		} else {
			if(estaDeclarada(op2)) {
				String tipo1 = genCodigo.getTercetosController().getTercetoTerm().getTipoOp();
				String tipo2 = lexico.getLexico().getTS().get(op2).getTipo();
				if (chequeoTipo(tipo1, tipo2)) {
					Terceto t = new Terceto (operando, "["+genCodigo.getTercetosController().getTercetoTerm().getNumTerceto()+"]", op2, genCodigo.getTercetosController().getCantTercetos()+1);
					asociarVarAuxTerceto(t, tipo2);
					t.setTipoOp(tipo2);
					genCodigo.getTercetosController().addTercetoLista(t);
					genCodigo.getTercetosController().setTercetoTerm(t);
				} else
					lexico.getLexico().addError("Tipos incompatibles en la operacion", lexico.getLexico().getNroLinea());
			} else
				lexico.getLexico().addError("La variable: "+op2+" no esta declarada.", lexico.getLexico().getNroLinea());
		}
}

public void addTercetoExpresion (String operando, String op1, String op2) {
	if (genCodigo.getTercetosController().getTercetoExp() == null) {
		if (genCodigo.getTercetosController().getTercetoTerm() == null) {
			if (estaDeclarada(op1))
				if(estaDeclarada(op2)) {
					String tipo1 = lexico.getLexico().getTS().get(op1).getTipo();
					String tipo2 = lexico.getLexico().getTS().get(op2).getTipo();
					if (chequeoTipo(tipo1, tipo2)) {
						Terceto t = new Terceto (operando, op1, op2, genCodigo.getTercetosController().getCantTercetos()+1);
						asociarVarAuxTerceto(t, tipo1);
						t.setTipoOp(tipo1);
						genCodigo.getTercetosController().addTercetoLista(t);
						genCodigo.getTercetosController().setTercetoExp(t);
					} else
						lexico.getLexico().addError("Tipos incompatibles en la operacion", lexico.getLexico().getNroLinea());
				} else
					lexico.getLexico().addError("La variable: "+op2+" no esta declarada.", lexico.getLexico().getNroLinea());
			else 
				lexico.getLexico().addError("La variable: "+op1+" no esta declarada.", lexico.getLexico().getNroLinea());
		} else {
			if (estaDeclarada(op1)) {
				String tipo1 = lexico.getLexico().getTS().get(op1).getTipo();
				String tipo2 = genCodigo.getTercetosController().getTercetoTerm().getTipoOp();
				if (chequeoTipo(tipo1, tipo2)) {
					Terceto t = new Terceto (operando, op1, "["+genCodigo.getTercetosController().getTercetoTerm().getNumTerceto()+"]", genCodigo.getTercetosController().getCantTercetos()+1);
					asociarVarAuxTerceto(t, tipo1);
					t.setTipoOp(tipo1);
					genCodigo.getTercetosController().addTercetoLista(t);
					genCodigo.getTercetosController().setTercetoExp(t);
				} else
					lexico.getLexico().addError("Tipos incompatibles en la operacion", lexico.getLexico().getNroLinea());
			} else
				lexico.getLexico().addError("La variable: "+op1+" no esta declarada.", lexico.getLexico().getNroLinea());
		}
	} else {
		if (genCodigo.getTercetosController().getTercetoTerm() == null) {
			if (estaDeclarada(op2)) {
				String tipo1 = genCodigo.getTercetosController().getTercetoExp().getTipoOp();
				String tipo2 = lexico.getLexico().getTS().get(op2).getTipo();
				if (chequeoTipo(tipo1, tipo2)) {
					Terceto t = new Terceto (operando, "["+genCodigo.getTercetosController().getTercetoExp().getNumTerceto()+"]", op2, genCodigo.getTercetosController().getCantTercetos()+1);
					asociarVarAuxTerceto(t, tipo2);
					t.setTipoOp(tipo2);
					genCodigo.getTercetosController().addTercetoLista(t);
					genCodigo.getTercetosController().setTercetoExp(t);
				} else
					lexico.getLexico().addError("Tipos incompatibles en la operacion", lexico.getLexico().getNroLinea());
			} else 
				lexico.getLexico().addError("La variable: "+op2+" no esta declarada.", lexico.getLexico().getNroLinea());
		} else {
			String tipo1 = genCodigo.getTercetosController().getTercetoExp().getTipoOp();
			String tipo2 = genCodigo.getTercetosController().getTercetoTerm().getTipoOp();
			if (chequeoTipo(tipo1, tipo2)) {	
				Terceto t = new Terceto (operando, "["+genCodigo.getTercetosController().getTercetoExp().getNumTerceto()+"]", "["+genCodigo.getTercetosController().getTercetoTerm().getNumTerceto()+"]", genCodigo.getTercetosController().getCantTercetos()+1);
				asociarVarAuxTerceto(t, tipo1);
				t.setTipoOp(tipo1);
				genCodigo.getTercetosController().addTercetoLista(t);
				genCodigo.getTercetosController().setTercetoExp(t);
			} else
				lexico.getLexico().addError("Tipos incompatibles en la operacion", lexico.getLexico().getNroLinea());
		}
	}
}

private void asociarVarAuxTerceto (Terceto t, String tipo) {

	String nAux = genCodigo.getTercetosController().getNombreNextAux();
	t.setAuxAsoc(nAux);
	lexico.getLexico().addVarTS(nAux, tipo, "Variable auxiliar", 0, "-", "-");
}			

public void cambiarTercetos () {
	genCodigo.getTercetosController().setTercetoExp(genCodigo.getTercetosController().getTercetoTerm());
	genCodigo.getTercetosController().setTercetoTermNull();
} 

public void addTercetoCondicion (String op1, String lex, String op2) {
	
	if (cmp1 == true)
		if (cmp2 == true) {
			String tipo1 = genCodigo.getTercetosController().getTercetoLista(genCodigo.getTercetosController().getCantTercetos()-2).getTipoOp();
			String tipo2 = genCodigo.getTercetosController().getTercetoLista(genCodigo.getTercetosController().getCantTercetos()-1).getTipoOp();
			if (chequeoTipo (tipo1, tipo2)) {
				Terceto t = new Terceto (lex, "["+Integer.toString(genCodigo.getTercetosController().getCantTercetos()-1)+"]", "["+Integer.toString(genCodigo.getTercetosController().getCantTercetos())+"]", genCodigo.getTercetosController().getCantTercetos()+1);
				t.setTipoOp(tipo1);
				genCodigo.getTercetosController().addTercetoLista(t);
				condStart = genCodigo.getTercetosController().getCantTercetos()-2;
			} else 
				assembler.getConversorAssembler().addErrorCI("Tipos incompatibles en la comparacion", lexico.getLexico().getNroLinea());
		} else {
			if (estaDeclarada(op2)) {
				String tipo1 = genCodigo.getTercetosController().getTercetoLista(genCodigo.getTercetosController().getCantTercetos()-1).getTipoOp();
				String tipo2 = lexico.getLexico().getTS().get(op2).getTipo();
				if (chequeoTipo(tipo1,tipo2)) {
					Terceto t = new Terceto (lex, "["+Integer.toString(genCodigo.getTercetosController().getCantTercetos())+"]", op2, genCodigo.getTercetosController().getCantTercetos()+1);
					t.setTipoOp(tipo1);
					genCodigo.getTercetosController().addTercetoLista(t);
					condStart = genCodigo.getTercetosController().getCantTercetos()-1;
				} else
					assembler.getConversorAssembler().addErrorCI("Tipos incompatibles en la comparacion", lexico.getLexico().getNroLinea());
			} else 
				assembler.getConversorAssembler().addErrorCI("Variable no declarada en la comparacion", lexico.getLexico().getNroLinea());
		}
	else { if (cmp2 == true) {
				if (estaDeclarada(op1)) {
					String tipo1 = lexico.getLexico().getTS().get(op1).getTipo();
					String tipo2 = genCodigo.getTercetosController().getTercetoLista(genCodigo.getTercetosController().getCantTercetos()-1).getTipoOp();
					if (chequeoTipo(tipo1, tipo2)) {
						Terceto t = new Terceto (lex, op1, "["+Integer.toString(genCodigo.getTercetosController().getCantTercetos())+"]", genCodigo.getTercetosController().getCantTercetos()+1);
						t.setTipoOp(tipo1);
						genCodigo.getTercetosController().addTercetoLista(t);
						condStart = genCodigo.getTercetosController().getCantTercetos()-1;
					} else
						assembler.getConversorAssembler().addErrorCI("Tipos incompatibles en la comparacion", lexico.getLexico().getNroLinea());
				} else
					assembler.getConversorAssembler().addErrorCI("Variable no declarada en la comparacion", lexico.getLexico().getNroLinea());					
			} else {
				if (estaDeclarada(op1))
					if (estaDeclarada(op2)) {
						String tipo1 = lexico.getLexico().getTS().get(op1).getTipo();
						String tipo2 = lexico.getLexico().getTS().get(op2).getTipo();
						if (chequeoTipo(tipo1, tipo2)) {
							Terceto t = new Terceto (lex, op1, op2, genCodigo.getTercetosController().getCantTercetos()+1);
							t.setTipoOp(tipo1);
							genCodigo.getTercetosController().addTercetoLista(t);
							condStart = genCodigo.getTercetosController().getCantTercetos()-1;
						} else
							assembler.getConversorAssembler().addErrorCI("Tipos incompatibles en la comparacion", lexico.getLexico().getNroLinea());
					} else
						assembler.getConversorAssembler().addErrorCI("Variable no declarada en la comparacion", lexico.getLexico().getNroLinea());
				else		
					assembler.getConversorAssembler().addErrorCI("Variable no declarada en la comparacion", lexico.getLexico().getNroLinea());
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

public void apilarTercetoIncompletoWHILE () {

	if (genCodigo.getTercetosController().isPilaEmpty()) {
		Terceto t = new Terceto ("BF", "["+Integer.toString(genCodigo.getTercetosController().getCantTercetos())+"]", "-", genCodigo.getTercetosController().getCantTercetos()+1);
		genCodigo.getTercetosController().addTercetoLista(t);
		genCodigo.getTercetosController().apilarTerceto(t);
	} else {
		Terceto aux = genCodigo.getTercetosController().getTercetoPila();
		aux.completarTerceto ("["+Integer.toString(genCodigo.getTercetosController().getCantTercetos()+2)+"]");
		genCodigo.getTercetosController().modificarTercetoLista(aux.getNumTerceto()-1, aux);
		genCodigo.getTercetosController().removeTercetoPila();
		Terceto BI = new Terceto ("BI", "-", "["+condStart+"]", genCodigo.getTercetosController().getCantTercetos()+1);
		genCodigo.getTercetosController().addTercetoLista(BI);
	}
}

private boolean chequeoTipo(String var1, String var2){
	boolean valido = false;
	if(((var1.equals("int")) && (var2.equals("int"))) || ((var1.equals("float")) && (var2.equals("float"))) || ((var1.equals("int")) && (var2.equals("CONST INT")))
			|| ((var1.equals("float")) && (var2.equals("CONST FLOAT")) || ((var1.equals("CONST FLOAT")) && (var2.equals("float")) || ((var1.equals("CONST INT")) && (var2.equals("int"))
					|| ((var1.equals("CONST FLOAT")) && (var2.equals("CONST FLOAT")) || ((var1.equals("CONST INT")) && (var2.equals("CONST INT")))))))){
		valido = true;
	}	
	return valido;
}

private boolean estaDeclarada(String lex) {
	
	if (lexico.getLexico().getTS().containsKey(lex))
		return true;
	else return false;
}

public boolean existeVar(Token t){
	boolean existe = false;
	Iterator<Token> it = listaVar.iterator();
	while(it.hasNext() && existe == false){
		if(it.next().getLexema().equals(t.getLexema())){
			existe = true;
		}	
	}
	return existe;
}

private boolean esClase(String lex) {
	boolean existe = false;
	Atributo a = lexico.getLexico().getTS().get(lex);
	if (a != null)
		if (a.getUso().equals("Nombre de clase"))
			existe = true;
		else existe = false;
	return existe;
}

private boolean esMetodo(String lex) {
	boolean existe = false;
	Atributo a = lexico.getLexico().getTS().get(lex);
	if (a != null)
		if (a.getUso().equals("Metodo de clase"))
			existe = true;
		else existe = false;
	return existe;
}

private static boolean isInteger(String s) {
    try { 
        Integer.parseInt(s); 
    } catch(NumberFormatException e) { 
        return false; 
    } catch(NullPointerException e) {
        return false;
    }
    // only got here if we didn't return false
    return true;
}

private static boolean isFloat(String s) {
    try { 
        Float.parseFloat(s); 
    } catch(NumberFormatException e) { 
        return false; 
    } catch(NullPointerException e) {
        return false;
    }
    // only got here if we didn't return false
    return true;
}

private void addTercetoFinal () {
	
	Terceto BI = new Terceto ("END", "-", "-", genCodigo.getTercetosController().getCantTercetos()+1);
	genCodigo.getTercetosController().addTercetoLista(BI);
}
