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

  
sentencia_de_clase  : CLASS ID {setClass(((Token)$2.obj).getLexema());} definicion_clase {lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se cre� un componente de clase con el nombre: "+((Token)$2.obj).getLexema());
																																											 										  addClaseTS(((Token)$2.obj).getLexema());
																																											 										  						classFlag = false;
																																											 										  						   className = "";}
					| CLASS ID {setClass(((Token)$2.obj).getLexema());} EXTENDS ID definicion_clase {lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se cre� un componente de clase extendida con el nombre: "+((Token)$2.obj).getLexema()+", que extiende de: "+((Token)$5.obj).getLexema());
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
					  
lista_atributos : visibilidad sentencia_declarativa {lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se cre� un atributo de clase");}
				;
									 
metodo_clase : visibilidad VOID ID '(' ')' {setFunctionLabel(((Token)$3.obj).getLexema());} bloque_anidado_metclase {lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se cre� un metodo de clase"); 
																																				  System.out.println("Viene un metodo");
																												  addMetodoTS(((Token)$1.obj).getLexema(), ((Token)$3.obj).getLexema());
																												  													   setReturnLabel();}
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

visibilidad : PUBLIC { visibilidad = "public"; }
		 	| PRIVATE { visibilidad = "private"; }
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
	  
invocacion_metodo_clase : ID '.' ID '(' ')' ';' { if (esObjeto(((Token)$1.obj).getLexema()))
														if (esMetodo(((Token)$3.obj).getLexema())){
															callFunction(((Token)$1.obj).getLexema(), ((Token)$3.obj).getLexema());
														} else
															assembler.getConversorAssembler().addErrorCI("Se trato de invocar a un metodo inexistente", lexico.getLexico().getNroLinea());
												  else
												  		assembler.getConversorAssembler().addErrorCI("Objeto no declarado", lexico.getLexico().getNroLinea());			
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


iteracion   : WHILE { guardarNumCondicion(); } '(' condicion ')' {apilarTercetoIncompletoWHILE();} DO bloque_anidado_while { lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se creo una iteracion");
																															cmp1 = false; cmp2 = false; condStart = 0;}
			| error_iteracion
			;																																							   
																																								
error_iteracion : WHILE error condicion ')' DO bloque_anidado_while {lexico.getLexico().addError("Falta algun parentesis en la iteracion", lexico.getLexico().getNroLinea());}
				;

condicion 	:  expresion {if (genCodigo.getTercetosController().getTercetoExp() != null)
							 cmp1 = true;}												comparador expresion { if(((Token)$1.obj).getLexema().equals("-")){
																													if (genCodigo.getTercetosController().getTercetoExp() != null){
								 																						cmp2 = true;
																														addTercetoCondicion(cteNegativaExpresion, ((Token)$3.obj).getLexema(), ((Token)$4.obj).getLexema());
																														resetAtClase();
																													} else {
																														addTercetoCondicion(cteNegativaExpresion, ((Token)$3.obj).getLexema(), ((Token)$4.obj).getLexema());
																														resetAtClase();
																													}
																												}else if(((Token)$4.obj).getLexema().equals("-")){
																															if (genCodigo.getTercetosController().getTercetoExp() != null){
								 																								cmp2 = true;
																																addTercetoCondicion(((Token)$1.obj).getLexema(), ((Token)$3.obj).getLexema(), cteNegativaExpresion);
																																resetAtClase();
																															} else {
																																addTercetoCondicion(((Token)$1.obj).getLexema(), ((Token)$3.obj).getLexema(), cteNegativaExpresion);
																																resetAtClase();
																															}
																													}else{
								 																							if (genCodigo.getTercetosController().getTercetoExp() != null){
								 																								cmp2 = true;
																																addTercetoCondicion(((Token)$1.obj).getLexema(), ((Token)$3.obj).getLexema(), ((Token)$4.obj).getLexema());
																																resetAtClase();
																															} else {
																																addTercetoCondicion(((Token)$1.obj).getLexema(), ((Token)$3.obj).getLexema(), ((Token)$4.obj).getLexema());
																																resetAtClase();
																															}
								 																						 }
								 																				}
           	| error_condicion
           	;

error_condicion : error {lexico.getLexico().addError("Error en la comparacion.", lexico.getLexico().getNroLinea());
						 errorCmp = true;} expresion
				;

bloque_anidado_while  : sentencia { completarTercetoFinalWHILE(); }
				      | BEGIN bloque_sentencias END { completarTercetoFinalWHILE(); }
				      | BEGIN bloque_sentencias error {lexico.getLexico().addError("Falta el END en el bloque anidado", lexico.getLexico().getNroLinea());}
				      | error bloque_sentencias END {lexico.getLexico().addError("Falta el BEGIN en el bloque anidado", lexico.getLexico().getNroLinea());}
				      ;


comparador 	: '<'
	   		| '>'
       		| C_MAYORIGUAL
	   		| C_MENORIGUAL
	   		| C_DISTINTO
	   		| C_IGUAL
            ;

seleccion : if_condicion END_IF {lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una condicion IF");
																														   desapilarYCompletar();
																										   cmp1 = false; cmp2 = false; condStart = 0;}
          | if_condicion {completarTercetoFinalIF();} ELSE bloque_anidado_if END_IF {lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una condicion IF con ELSE");
          																																											   desapilarYCompletar();
          																																							condStart = 0; cmp1 = false; cmp2 = false;}
          ;

if_condicion : IF '(' condicion ')' {apilarTercetoIncompletoIF();} bloque_anidado_if 
             | IF condicion ')' bloque_anidado_if {lexico.getLexico().addError("Falta el '(' de la condicion ", lexico.getLexico().getNroLinea());}
             | IF '(' condicion  bloque_anidado_if {lexico.getLexico().addError("Falta el ')' de la condicion ", lexico.getLexico().getNroLinea());}
             | IF condicion bloque_anidado_if {lexico.getLexico().addError("Faltan los parentesis de la condicion", lexico.getLexico().getNroLinea());}
             ;

bloque_anidado_if  : sentencia
				   | BEGIN bloque_sentencias END
				   | BEGIN bloque_sentencias error {lexico.getLexico().addError("Falta el END en el bloque anidado", lexico.getLexico().getNroLinea());}
				   | error bloque_sentencias END {lexico.getLexico().addError("Falta el BEGIN en el bloque anidado", lexico.getLexico().getNroLinea());}
				   ;

asig 	: ID ASIGNACION expresion ';' { if (esMetodo(((Token)$1.obj).getLexema())) {
											lexico.getLexico().addError("No se puede hacer una asignacion a un metodo de clase.", lexico.getLexico().getNroLinea());
									} else {
											if(((Token)$3.obj).getLexema().equals("-")){
												lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una asignacion");
												System.out.println("Realizo la asignacion en la linea: "+lexico.getLexico().getNroLinea());
												addTercetoAsignacion(((Token)$1.obj).getLexema(), cteNegativaExpresion);
											} else {
												lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una asignacion");
												System.out.println("Realizo la asignacion en la linea: "+lexico.getLexico().getNroLinea());
												addTercetoAsignacion(((Token)$1.obj).getLexema(), ((Token)$3.obj).getLexema());
											}
									}
										genCodigo.getTercetosController().setTercetoExpNull();
										genCodigo.getTercetosController().setTercetoTermNull(); 
										resetAtClase();}
		| ID '.' ID ASIGNACION expresion ';' { if (esMetodo(((Token)$1.obj).getLexema())) {
											lexico.getLexico().addError("No se puede hacer una asignacion a un metodo de clase.", lexico.getLexico().getNroLinea());
										} else {
												if(((Token)$5.obj).getLexema().equals("-")){
													lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una asignacion");
													System.out.println("Realizo la asignacion en la linea: "+lexico.getLexico().getNroLinea());
													addTercetoAsignacionVarClase(((Token)$3.obj).getLexema(), cteNegativaExpresion, ((Token)$1.obj).getLexema());
												} else {
													lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una asignacion");
													System.out.println("Realizo la asignacion en la linea: "+lexico.getLexico().getNroLinea());
													addTercetoAsignacionVarClase(((Token)$3.obj).getLexema(), ((Token)$5.obj).getLexema(), ((Token)$1.obj).getLexema());
												}	
										} 
										genCodigo.getTercetosController().setTercetoExpNull();
										genCodigo.getTercetosController().setTercetoTermNull(); 
										resetAtClase();}
        | ID expresion ';' { System.out.println("Error, falta el ':=' de la asignacion"); lexico.getLexico().addError("Falta el ':=' de la asignacion ", lexico.getLexico().getNroLinea());} 
        | error ASIGNACION expresion ';' { System.out.println("Error, falta el ID a la izquierda de la asignacion"); lexico.getLexico().addError("Falta el ID de la asignacion ", lexico.getLexico().getNroLinea());}
        | ID ASIGNACION expresion  {lexico.getLexico().addError("Falta el ';' que cierra la asignacion.", lexico.getLexico().getNroLinea());}
        ;

expresion 	:  expresion '+' termino {if(((Token)$1.obj).getLexema().equals("-") && ((Token)$3.obj).getLexema().equals("-")){
											lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una suma");
								 			addTercetoExpresion(((Token)$2.obj).getLexema(), cteNegativaExpresion, cteNegativaTermino);
											resetAtClase();
										}else if(((Token)$1.obj).getLexema().equals("-")){
													lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una suma");
	  								  				addTercetoExpresion(((Token)$2.obj).getLexema(), cteNegativaExpresion, ((Token)$3.obj).getLexema());
													resetAtClase();
											}else if(((Token)$3.obj).getLexema().equals("-")){
														lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una suma");
	  								  					addTercetoExpresion(((Token)$2.obj).getLexema(), ((Token)$1.obj).getLexema(), cteNegativaTermino);
														resetAtClase();	
												}else{	
								 						lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una suma");
	  								  					addTercetoExpresion(((Token)$2.obj).getLexema(), ((Token)$1.obj).getLexema(), ((Token)$3.obj).getLexema());
														resetAtClase();		
							 						}  	 
							 }
	  		| expresion '-' termino {if(((Token)$1.obj).getLexema().equals("-") && ((Token)$3.obj).getLexema().equals("-")){
											lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una resta");
								 			addTercetoExpresion(((Token)$2.obj).getLexema(), cteNegativaExpresion, cteNegativaTermino);
											resetAtClase();
										}else if(((Token)$1.obj).getLexema().equals("-")){
													lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una resta");
	  								  				addTercetoExpresion(((Token)$2.obj).getLexema(), cteNegativaExpresion, ((Token)$3.obj).getLexema());
													resetAtClase();
											}else if(((Token)$3.obj).getLexema().equals("-")){
														lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una resta");
	  								  					addTercetoExpresion(((Token)$2.obj).getLexema(), ((Token)$1.obj).getLexema(), cteNegativaTermino);
														resetAtClase();	
												}else{	
								 						lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una resta");
	  								  					addTercetoExpresion(((Token)$2.obj).getLexema(), ((Token)$1.obj).getLexema(), ((Token)$3.obj).getLexema());
														resetAtClase();		
							 						}		
	  								}
      		| termino {System.out.println("Paso de termino a expresion");
      				   cambiarTercetos();
      				   if(((Token)$1.obj).getLexema().equals("-")){
			   				cteNegativaExpresion = cteNegativaTermino;
			   				System.out.println("CTE NEGATIVA EXPRESION: "+cteNegativaExpresion);
			   			}
      				}
          	;

termino : termino '*' factor {if(((Token)$1.obj).getLexema().equals("-") && ((Token)$3.obj).getLexema().equals("-")){
									lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una multiplicacion");
								 	addTercetoTermino(((Token)$2.obj).getLexema(), cteNegativaTermino, cteNegativaFactor);
									resetAtClase(); 
								}else if(((Token)$1.obj).getLexema().equals("-")){
											lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una multiplicacion");
								 			addTercetoTermino(((Token)$2.obj).getLexema(), cteNegativaTermino, ((Token)$3.obj).getLexema());
											resetAtClase(); 
										}else if(((Token)$3.obj).getLexema().equals("-")){
													lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una multiplicacion");
								 					addTercetoTermino(((Token)$2.obj).getLexema(), ((Token)$1.obj).getLexema(), cteNegativaFactor);
													resetAtClase(); 
											}else{	
								 					lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una multiplicacion");
								 					addTercetoTermino(((Token)$2.obj).getLexema(), ((Token)$1.obj).getLexema(), ((Token)$3.obj).getLexema());
													resetAtClase(); 	
							 					}  	 
							 }
							 
		| termino '/' factor {if(((Token)$1.obj).getLexema().equals("-") && ((Token)$3.obj).getLexema().equals("-")){
									lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una division");
								 	addTercetoTermino(((Token)$2.obj).getLexema(), cteNegativaTermino, cteNegativaFactor);
									resetAtClase(); 
								}else if(((Token)$1.obj).getLexema().equals("-")){
											lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una division");
								 			addTercetoTermino(((Token)$2.obj).getLexema(), cteNegativaTermino, ((Token)$3.obj).getLexema());
											resetAtClase(); 
										}else if(((Token)$3.obj).getLexema().equals("-")){
													lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una division");
								 					addTercetoTermino(((Token)$2.obj).getLexema(), ((Token)$1.obj).getLexema(), cteNegativaFactor);
													resetAtClase(); 
											}else{	
								 					lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una division");
								 					addTercetoTermino(((Token)$2.obj).getLexema(), ((Token)$1.obj).getLexema(), ((Token)$3.obj).getLexema());
													resetAtClase(); 	
							 					}
							 } 
		| factor { System.out.println("Paso factor a termino");
					if(((Token)$1.obj).getLexema().equals("-")){
			   			cteNegativaTermino = cteNegativaFactor;
			   			System.out.println("CTE NEGATIVA TERMINO: "+cteNegativaTermino);
			   		}
				}
        ;

factor  : ID {System.out.println("Cargo un identificador");}
		| ID '.' ID {if (estaDeclarada(((Token)$1.obj).getLexema()))
						if (esObjeto(((Token)$1.obj).getLexema()))
							if (estaDeclarada(((Token)$3.obj).getLexema())) {
								if (esAtributoClase(((Token)$3.obj).getLexema())) {
									if (checkVisibilidad(((Token)$3.obj).getLexema())) {
										guardarAtClase(((Token)$1.obj).getLexema(), ((Token)$3.obj).getLexema());
										System.out.println("Cargue la variable "+((Token)$3.obj).getLexema()+" de la clase "+((Token)$1.obj).getLexema());
									} else
										assembler.getConversorAssembler().addErrorCI("Atributo inaccesible por su visibilidad", lexico.getLexico().getNroLinea());
								} else
									assembler.getConversorAssembler().addErrorCI("El elemento no es un atributo de clase", lexico.getLexico().getNroLinea());
							} else
								assembler.getConversorAssembler().addErrorCI("Atributo de clase no declarado", lexico.getLexico().getNroLinea());
						else
							assembler.getConversorAssembler().addErrorCI("El elemento utilizado no es un objeto", lexico.getLexico().getNroLinea());
					 else
						assembler.getConversorAssembler().addErrorCI("Objeto no declarado", lexico.getLexico().getNroLinea());					 	
					}	
		| cte {System.out.println("Paso de cte a factor");
			   if(((Token)$1.obj).getLexema().equals("-")){
			   		cteNegativaFactor = cteNegativa;
			   		System.out.println("CTE NEGATIVA FACTOR: "+cteNegativaFactor);
			   }
			  }
		;


cte : CTE {System.out.println("Leo una constante INT");}
	| '-' CTE {System.out.println("Leo una constante negada");
		  cteNegativa = "-"+((Token)$2.obj).getLexema();
		  System.out.println("CTE NEGATIVA INT: "+cteNegativa);
		  actualizarTablaNegativo(((Token)$2.obj).getLexema());
		  }
    | CTEFLOAT {System.out.println("Leo una constante FLOAT");}
    | '-' CTEFLOAT {System.out.println("Leo una float negada");
    		cteNegativa = "-"+((Token)$2.obj).getLexema();
		  	System.out.println("CTE NEGATIVA FLOAT: "+cteNegativa);
		  	actualizarTablaNegativo(((Token)$2.obj).getLexema());
		  	}
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
private boolean atclase1 = false;
private String c1 = "";
private String a1 = "";
private boolean atclase2 = false;
private String c2 = "";
private String a2 = "";
private String visibilidad = "-";
private boolean errorCmp = false;
private String cteNegativa = "";
private String cteNegativaFactor = "";
private String cteNegativaTermino = "";
private String cteNegativaExpresion = "";


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
				lexico.getLexico().addVarTS(t.getLexema(), type, "Atributo de clase", 0, className, "-", visibilidad);
			else if (type.equals("float"))
				lexico.getLexico().addVarTS(t.getLexema(), type, "Atributo de clase", 0.0, className, "-", visibilidad);	
		} else {
			if (set)
				if (existeClase)
					lexico.getLexico().addVarTS(t.getLexema(), type, "Objeto", 0, type, "-", visibilidad);
				else
					lexico.getLexico().addError("La clase del objeto no esta declarada", lexico.getLexico().getNroLinea());
			else {
			if (type.equals("int"))
				lexico.getLexico().addVarTS(t.getLexema(), type, "Variable", 0, "-", "-", visibilidad);
			else if (type.equals("float"))
				lexico.getLexico().addVarTS(t.getLexema(), type, "Variable", 0.0, "-", "-", visibilidad);	
			}
		}
	}
	visibilidad = "-";	
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
		if (esClase (lex2))
			lexico.getLexico().addClassHeredadaTS(lex,lex2);
		else
			assembler.getConversorAssembler().addErrorCI("Clase padre no declarada", lexico.getLexico().getNroLinea());
	else
		assembler.getConversorAssembler().addErrorCI("Clase ya declarada anteriormente", lexico.getLexico().getNroLinea());
}

public void addMetodoTS(String vis, String lex) {
	if (!esMetodo(lex))
		lexico.getLexico().addMethodTS(vis, lex, className);
	else
		assembler.getConversorAssembler().addErrorCI("Metodo ya declarado anteriormente", lexico.getLexico().getNroLinea());
	visibilidad = "-";
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
				Atributo c = new Atributo ("CONST INT", "CONST INT", Integer.parseInt(nuevoLex), "-", "-", 1);
				lexico.getLexico().getTS().put(nuevoLex, c);
			} else if (isFloat(nuevoLex)) {
				Atributo c = new Atributo ("CONST FLOAT", "CONST FLOAT", Float.parseFloat(nuevoLex), "-", "-", 1);
				lexico.getLexico().getTS().put(nuevoLex, c);
			}
		}
	} else {
		String nuevoLex = '-'+lexema;
		if (isInteger(nuevoLex)) {
			Atributo c = new Atributo ("CONST INT", "CONST INT", Integer.parseInt(nuevoLex), "-", "-", 1);
			lexico.getLexico().getTS().put(nuevoLex, c);
		} else if (isFloat(nuevoLex)) {
			Atributo c = new Atributo ("CONST FLOAT", "CONST FLOAT", Float.parseFloat(nuevoLex), "-", "-", 1);
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
				if (!atclase1) {
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
					String tipo1 = lexico.getLexico().getTS().get(op1).getTipo();
					String tipo2 = lexico.getLexico().getTS().get(a1).getTipo();
					if (chequeoTipo(tipo1, tipo2)) {
						Terceto t = new Terceto (":=", op1, c1 + "." + a1, genCodigo.getTercetosController().getCantTercetos()+1);
						t.setTipoOp(tipo1);
						genCodigo.getTercetosController().addTercetoLista(t);
					} else {
						lexico.getLexico().addError("Tipos incompatibles en la operacion", lexico.getLexico().getNroLinea());
					}
				}
			} else {
					lexico.getLexico().addError("La variable "+op2+" no esta declarada.", lexico.getLexico().getNroLinea());
				}
		else {
			lexico.getLexico().addError("La variable "+op1+" no esta declarada.", lexico.getLexico().getNroLinea());
		}
	}
}

public void addTercetoAsignacionVarClase (String op1, String op2, String clase) {
	if ( genCodigo.getTercetosController().getTercetoExp() != null ) {
		if (estaDeclarada(op1)) {
			String tipo1 = lexico.getLexico().getTS().get(op1).getTipo();
			String tipo2 = genCodigo.getTercetosController().getTercetoExp().getTipoOp();
			if (chequeoTipo(tipo1, tipo2)) {
				Terceto t = new Terceto (":=", clase + "." + op1, "["+Integer.toString(genCodigo.getTercetosController().getTercetoExp().getNumTerceto())+"]", genCodigo.getTercetosController().getCantTercetos()+1);
				t.setTipoOp(tipo1);
				genCodigo.getTercetosController().addTercetoLista(t);
			} else
				lexico.getLexico().addError("Tipos incompatibles en la operacion", lexico.getLexico().getNroLinea());
		} else
			lexico.getLexico().addError("La variable "+op1+" no esta declarada.", lexico.getLexico().getNroLinea());
	} else {
		if (estaDeclarada(op1))
			if (estaDeclarada(op2)) {
				if (!atclase1) {
					String tipo1 = lexico.getLexico().getTS().get(op1).getTipo();
					String tipo2 = lexico.getLexico().getTS().get(op2).getTipo();
					if (chequeoTipo(tipo1, tipo2)) {
						Terceto t = new Terceto (":=", clase + "." + op1, op2, genCodigo.getTercetosController().getCantTercetos()+1);
						t.setTipoOp(tipo1);
						genCodigo.getTercetosController().addTercetoLista(t);
					} else {
						lexico.getLexico().addError("Tipos incompatibles en la operacion", lexico.getLexico().getNroLinea());
					}
				} else {
					String tipo1 = lexico.getLexico().getTS().get(op1).getTipo();
					String tipo2 = lexico.getLexico().getTS().get(a1).getTipo();
					if (chequeoTipo(tipo1, tipo2)) {
						Terceto t = new Terceto (":=", clase + "." + op1, c1 + "." + a1, genCodigo.getTercetosController().getCantTercetos()+1);
						t.setTipoOp(tipo1);
						genCodigo.getTercetosController().addTercetoLista(t);
					} else {
						lexico.getLexico().addError("Tipos incompatibles en la operacion", lexico.getLexico().getNroLinea());
					}
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
		//SI NO HAY OPERACION PREVIA
		if (genCodigo.getTercetosController().getTercetoTerm() == null) {
			//SI ES OPERACION ENTRE DOS ATRIBUTOS DE CLASE
			if (atclase1 && atclase2) {
				String tipo1 = lexico.getLexico().getTS().get(a1).getTipo();
				String tipo2 = lexico.getLexico().getTS().get(a2).getTipo();
				if (chequeoTipo(tipo1, tipo2)) {
						Terceto t = new Terceto (operando, c1 + "." + a1, c2 + "." + a2, genCodigo.getTercetosController().getCantTercetos()+1);
						asociarVarAuxTerceto(t, tipo1);
						t.setTipoOp(tipo1);
						genCodigo.getTercetosController().addTercetoLista(t);
						genCodigo.getTercetosController().setTercetoTerm(t);
					} else
						lexico.getLexico().addError("Tipos incompatibles en la operacion", lexico.getLexico().getNroLinea());
			} else {
				if (atclase1) {
					//SI ES OBJETO OP1 SIGNIFICA QUE EL AT DE CLASE ESTA A LA IZQUIERDA, SI NO A DERECHA
					if (esObjeto(op1)) {
						if(estaDeclarada(op2)) {
							String tipo1 = lexico.getLexico().getTS().get(a1).getTipo();
							String tipo2 = lexico.getLexico().getTS().get(op2).getTipo();
							if (chequeoTipo(tipo1, tipo2)) {
								Terceto t = new Terceto (operando, c1 + "." + a1, op2, genCodigo.getTercetosController().getCantTercetos()+1);
								asociarVarAuxTerceto(t, tipo1);
								t.setTipoOp(tipo1);
								genCodigo.getTercetosController().addTercetoLista(t);
								genCodigo.getTercetosController().setTercetoTerm(t);
							} else
								lexico.getLexico().addError("Tipos incompatibles en la operacion", lexico.getLexico().getNroLinea());
						} else
							lexico.getLexico().addError("La variable: "+op2+" no esta declarada.", lexico.getLexico().getNroLinea());
					} else {
						if(estaDeclarada(op1)) {
							String tipo1 = lexico.getLexico().getTS().get(a1).getTipo();
							String tipo2 = lexico.getLexico().getTS().get(op1).getTipo();
							if (chequeoTipo(tipo1, tipo2)) {
								Terceto t = new Terceto (operando, op1, c1 + "." + a1, genCodigo.getTercetosController().getCantTercetos()+1);
								asociarVarAuxTerceto(t, tipo1);
								t.setTipoOp(tipo1);
								genCodigo.getTercetosController().addTercetoLista(t);
								genCodigo.getTercetosController().setTercetoTerm(t);
							} else
								lexico.getLexico().addError("Tipos incompatibles en la operacion", lexico.getLexico().getNroLinea());
						} else
							lexico.getLexico().addError("La variable: "+op1+" no esta declarada.", lexico.getLexico().getNroLinea());
					}
				//SI NO ES UNA OPERACION NORMAL ENTRE DOS VARIABLES/CONSTANTES
				} else {
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
				}
			}
		//SI NO, ES UNA OPERACION ENTRE UN TERCETO ANTERIOR Y UNA VARIABLE/CONST/AT DE CLASE
		} else {
			if (atclase1) {
				String tipo1 = genCodigo.getTercetosController().getTercetoTerm().getTipoOp();
				String tipo2 = lexico.getLexico().getTS().get(a1).getTipo();
				if (chequeoTipo(tipo1, tipo2)) {
					Terceto t = new Terceto (operando, "["+genCodigo.getTercetosController().getTercetoTerm().getNumTerceto()+"]", c1 + "." + a1, genCodigo.getTercetosController().getCantTercetos()+1);
					asociarVarAuxTerceto(t, tipo2);
					t.setTipoOp(tipo2);
					genCodigo.getTercetosController().addTercetoLista(t);
					genCodigo.getTercetosController().setTercetoTerm(t);
				} else
					lexico.getLexico().addError("Tipos incompatibles en la operacion", lexico.getLexico().getNroLinea());
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
}

public void addTercetoExpresion (String operando, String op1, String op2) {
	//SI NO HAY OPERACION PREVIA
	if (genCodigo.getTercetosController().getTercetoExp() == null) {
		if (genCodigo.getTercetosController().getTercetoTerm() == null) {
			//OPERACION ENTRE DOS AT DE CLASE
			if (atclase1 && atclase2) {
				String tipo1 = lexico.getLexico().getTS().get(a1).getTipo();
				String tipo2 = lexico.getLexico().getTS().get(a2).getTipo();
				if (chequeoTipo(tipo1, tipo2)) {
						Terceto t = new Terceto (operando, c1 + "." + a1, c2 + "." + a2, genCodigo.getTercetosController().getCantTercetos()+1);
						asociarVarAuxTerceto(t, tipo1);
						t.setTipoOp(tipo1);
						genCodigo.getTercetosController().addTercetoLista(t);
						genCodigo.getTercetosController().setTercetoExp(t);
					} else
						lexico.getLexico().addError("Tipos incompatibles en la operacion", lexico.getLexico().getNroLinea());
			} else {
				//OPERACION ENTRE AT DE CLASE Y VARIABLE
				if (atclase1) {
					//SI ES OBJETO OP1 SIGNIFICA QUE EL AT DE CLASE ESTA A LA IZQUIERDA, SI NO A DERECHA
					if (esObjeto(op1)) {
						if(estaDeclarada(op2)) {
							String tipo1 = lexico.getLexico().getTS().get(a1).getTipo();
							String tipo2 = lexico.getLexico().getTS().get(op2).getTipo();
							if (chequeoTipo(tipo1, tipo2)) {
								Terceto t = new Terceto (operando, c1 + "." + a1, op2, genCodigo.getTercetosController().getCantTercetos()+1);
								asociarVarAuxTerceto(t, tipo1);
								t.setTipoOp(tipo1);
								genCodigo.getTercetosController().addTercetoLista(t);
								genCodigo.getTercetosController().setTercetoExp(t);
							} else
								lexico.getLexico().addError("Tipos incompatibles en la operacion", lexico.getLexico().getNroLinea());
						} else
							lexico.getLexico().addError("La variable: "+op2+" no esta declarada.", lexico.getLexico().getNroLinea());
					} else {
						if(estaDeclarada(op1)) {
							String tipo1 = lexico.getLexico().getTS().get(a1).getTipo();
							String tipo2 = lexico.getLexico().getTS().get(op1).getTipo();
							if (chequeoTipo(tipo1, tipo2)) {
								Terceto t = new Terceto (operando, op1, c1 + "." + a1, genCodigo.getTercetosController().getCantTercetos()+1);
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
					//OPERACION ENTRE DOS VARIABLES
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
				}
			}
		} else {
			//OPERACION ENTRE VARIABLE/AT CLASE Y TERCETO TERMINO ANTERIOR
			if (atclase1) {
				String tipo1 = lexico.getLexico().getTS().get(a1).getTipo();
				String tipo2 = genCodigo.getTercetosController().getTercetoTerm().getTipoOp();
				if (chequeoTipo(tipo1, tipo2)) {
					Terceto t = new Terceto (operando, c1 + "." + a1, "["+genCodigo.getTercetosController().getTercetoTerm().getNumTerceto()+"]", genCodigo.getTercetosController().getCantTercetos()+1);
					asociarVarAuxTerceto(t, tipo1);
					t.setTipoOp(tipo1);
					genCodigo.getTercetosController().addTercetoLista(t);
					genCodigo.getTercetosController().setTercetoExp(t);
				} else
					lexico.getLexico().addError("Tipos incompatibles en la operacion", lexico.getLexico().getNroLinea());
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
		}
	} else {
		if (genCodigo.getTercetosController().getTercetoTerm() == null) {
			//OPERACION ENTRE DOS AT DE CLASE
			if (atclase1 && atclase2) {
				String tipo1 = lexico.getLexico().getTS().get(a1).getTipo();
				String tipo2 = lexico.getLexico().getTS().get(a2).getTipo();
				if (chequeoTipo(tipo1, tipo2)) {
						Terceto t = new Terceto (operando, c1 + "." + a1, c2 + "." + a2, genCodigo.getTercetosController().getCantTercetos()+1);
						asociarVarAuxTerceto(t, tipo1);
						t.setTipoOp(tipo1);
						genCodigo.getTercetosController().addTercetoLista(t);
						genCodigo.getTercetosController().setTercetoExp(t);
					} else
						lexico.getLexico().addError("Tipos incompatibles en la operacion", lexico.getLexico().getNroLinea());
			} else {
			//OPERACION ENTRE VARIABLE/AT CLASE Y TERCETO EXPRESION ANTERIOR
				if (atclase1) {
					String tipo1 = genCodigo.getTercetosController().getTercetoExp().getTipoOp();
					String tipo2 = lexico.getLexico().getTS().get(a1).getTipo();
					if (chequeoTipo(tipo1, tipo2)) {
						Terceto t = new Terceto (operando, "["+genCodigo.getTercetosController().getTercetoExp().getNumTerceto()+"]", c1 + "." + a1, genCodigo.getTercetosController().getCantTercetos()+1);
						asociarVarAuxTerceto(t, tipo2);
						t.setTipoOp(tipo2);
						genCodigo.getTercetosController().addTercetoLista(t);
						genCodigo.getTercetosController().setTercetoExp(t);
					} else
						lexico.getLexico().addError("Tipos incompatibles en la operacion", lexico.getLexico().getNroLinea());
				} else {
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
				}
			}
		} else {
			//OPERACION ENTRE TERCETO TERMINO Y TERCETO EXPRESION
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
	lexico.getLexico().addVarTS(nAux, tipo, "Variable auxiliar", 0, "-", "-", visibilidad);
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
			if (atclase1) {
				String tipo1 = genCodigo.getTercetosController().getTercetoLista(genCodigo.getTercetosController().getCantTercetos()-1).getTipoOp();
				String tipo2 = lexico.getLexico().getTS().get(a1).getTipo();
				if (chequeoTipo(tipo1,tipo2)) {
					Terceto t = new Terceto (lex, "["+Integer.toString(genCodigo.getTercetosController().getCantTercetos())+"]", c1 + "." + a1, genCodigo.getTercetosController().getCantTercetos()+1);
					t.setTipoOp(tipo1);
					genCodigo.getTercetosController().addTercetoLista(t);
					condStart = genCodigo.getTercetosController().getCantTercetos()-1;
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
		}
	else { if (cmp2 == true) {
				if (atclase1) {
					String tipo1 = genCodigo.getTercetosController().getTercetoLista(genCodigo.getTercetosController().getCantTercetos()-1).getTipoOp();
					String tipo2 = lexico.getLexico().getTS().get(a1).getTipo();
					if (chequeoTipo(tipo1,tipo2)) {
						Terceto t = new Terceto (lex, c1 + "." + a1, "["+Integer.toString(genCodigo.getTercetosController().getCantTercetos())+"]", genCodigo.getTercetosController().getCantTercetos()+1);
						t.setTipoOp(tipo1);
						genCodigo.getTercetosController().addTercetoLista(t);
						condStart = genCodigo.getTercetosController().getCantTercetos()-1;
					} else
						assembler.getConversorAssembler().addErrorCI("Tipos incompatibles en la comparacion", lexico.getLexico().getNroLinea());
				} else {
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
				}				
			} else {
				if (atclase1 && atclase2) {
					String tipo1 = lexico.getLexico().getTS().get(a1).getTipo();
					String tipo2 = lexico.getLexico().getTS().get(a2).getTipo();
					if (chequeoTipo(tipo1, tipo2)) {
						Terceto t = new Terceto (lex, c1 + "." + a1, c2 + "." + a2, genCodigo.getTercetosController().getCantTercetos()+1);
						t.setTipoOp(tipo1);
						genCodigo.getTercetosController().addTercetoLista(t);
						condStart = genCodigo.getTercetosController().getCantTercetos()-1;
					} else
						assembler.getConversorAssembler().addErrorCI("Tipos incompatibles en la comparacion", lexico.getLexico().getNroLinea());
				} else {
					if (atclase1) {
						if (esObjeto(op1)) {
							String tipo1 = lexico.getLexico().getTS().get(a1).getTipo();
							String tipo2 = lexico.getLexico().getTS().get(op2).getTipo();
							if (chequeoTipo(tipo1, tipo2)) {
								Terceto t = new Terceto (lex, c1 + "." + a1, op2, genCodigo.getTercetosController().getCantTercetos()+1);
								t.setTipoOp(tipo1);
								genCodigo.getTercetosController().addTercetoLista(t);
								condStart = genCodigo.getTercetosController().getCantTercetos()-1;
							} else
								assembler.getConversorAssembler().addErrorCI("Tipos incompatibles en la comparacion", lexico.getLexico().getNroLinea());
						} else {
							String tipo1 = lexico.getLexico().getTS().get(op1).getTipo();
							String tipo2 = lexico.getLexico().getTS().get(a1).getTipo();
							if (chequeoTipo(tipo1, tipo2)) {
								Terceto t = new Terceto (lex, op1, c1 + "." + a1, genCodigo.getTercetosController().getCantTercetos()+1);
								t.setTipoOp(tipo1);
								genCodigo.getTercetosController().addTercetoLista(t);
								condStart = genCodigo.getTercetosController().getCantTercetos()-1;
							} else
								assembler.getConversorAssembler().addErrorCI("Tipos incompatibles en la comparacion", lexico.getLexico().getNroLinea());
						}
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
		}
}

public void apilarTercetoIncompletoIF () {
	
		Terceto t = new Terceto ("BF", "["+Integer.toString(genCodigo.getTercetosController().getCantTercetos())+"]", "-", genCodigo.getTercetosController().getCantTercetos()+1);
		genCodigo.getTercetosController().addTercetoLista(t);
		genCodigo.getTercetosController().apilarTerceto(t);
		
}

public void completarTercetoFinalIF () {

		Terceto aux = genCodigo.getTercetosController().getTercetoPila();
		aux.completarTerceto ("["+Integer.toString(genCodigo.getTercetosController().getCantTercetos()+2)+"]");
		genCodigo.getTercetosController().modificarTercetoLista(aux.getNumTerceto()-1, aux);
		genCodigo.getTercetosController().removeTercetoPila();
		Terceto BI = new Terceto ("BI", "-", "-", genCodigo.getTercetosController().getCantTercetos()+1);
		genCodigo.getTercetosController().addTercetoLista(BI);
		genCodigo.getTercetosController().apilarTerceto(BI);

}

public void desapilarYCompletar () {

		Terceto aux = genCodigo.getTercetosController().getTercetoPila();
		aux.completarTerceto ("["+Integer.toString(genCodigo.getTercetosController().getCantTercetos()+1)+"]");
		genCodigo.getTercetosController().modificarTercetoLista(aux.getNumTerceto()-1, aux);
		genCodigo.getTercetosController().removeTercetoPila();
		
}

public void guardarNumCondicion () {

		Terceto aux = new Terceto ("-", "-", "-", genCodigo.getTercetosController().getCantTercetos()+1);
		genCodigo.getTercetosController().apilarTerceto(aux);
}

public void apilarTercetoIncompletoWHILE () {

		Terceto t = new Terceto ("BF", "["+Integer.toString(genCodigo.getTercetosController().getCantTercetos())+"]", "-", genCodigo.getTercetosController().getCantTercetos()+1);
		genCodigo.getTercetosController().addTercetoLista(t);
		genCodigo.getTercetosController().apilarTerceto(t);

}

public void completarTercetoFinalWHILE () {
		
		Terceto aux = genCodigo.getTercetosController().getTercetoPila();
		aux.completarTerceto ("["+Integer.toString(genCodigo.getTercetosController().getCantTercetos()+2)+"]");
		genCodigo.getTercetosController().modificarTercetoLista(aux.getNumTerceto()-1, aux);
		genCodigo.getTercetosController().removeTercetoPila();
		Terceto aux2 = genCodigo.getTercetosController().getTercetoPila();
		Terceto BI = new Terceto ("BI", "-", "["+aux2.getNumTerceto()+"]", genCodigo.getTercetosController().getCantTercetos()+1);
		genCodigo.getTercetosController().addTercetoLista(BI);
		genCodigo.getTercetosController().removeTercetoPila();
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
	else 
		return false;
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

private boolean esObjeto(String lex) {
	boolean existe = false;
	Atributo a = lexico.getLexico().getTS().get(lex);
	if (a != null)
		if (a.getUso().equals("Objeto"))
			existe = true;
		else existe = false;
	return existe;
}

private boolean esAtributoClase(String lex) {
	boolean existe = false;
	Atributo a = lexico.getLexico().getTS().get(lex);
	if (a != null)
		if (a.getUso().equals("Atributo de clase"))
			existe = true;
	return existe;
}

private boolean checkVisibilidad(String lex) {
	boolean existe = false;
	Atributo a = lexico.getLexico().getTS().get(lex);
	if (a != null)
		if (a.getVisibilidad().equals("public") || a.getVisibilidad().equals("-"))
			existe = true;
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

private void guardarAtClase (String clase, String at) {
		if (!this.atclase1) {
			this.c1 = clase;
			this.a1 = at;
			this.atclase1 = true;
		} else if (!this.atclase2) {
			this.c2 = clase;
			this.a2 = at;
			this.atclase2 = true;	
		}
}

private void resetAtClase () {

	if (this.atclase1)
		this.atclase1 = false;
	if (this.atclase2)
		this.atclase2 = false;
}

private void setFunctionLabel (String fn) {

	Terceto t = new Terceto ("FUNCTION", fn, "-", genCodigo.getTercetosController().getCantTercetos()+1);
	genCodigo.getTercetosController().addTercetoLista(t);
}

private void setReturnLabel () {

	Terceto t = new Terceto ("RETURN", "-", "-", genCodigo.getTercetosController().getCantTercetos()+1);
	genCodigo.getTercetosController().addTercetoLista(t);
}

private void callFunction (String o, String fn) {

	Terceto t = new Terceto ("CALL", fn + "@" + o, "-", genCodigo.getTercetosController().getCantTercetos()+1);
	genCodigo.getTercetosController().addTercetoLista(t);
}
