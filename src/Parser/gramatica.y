%{
package Parser; 
import Lexico.Controller;
import Lexico.Token;
%}

%token ID
%token CTE
%token CADENA
%token COMENTARIO
%token ASIGNACION
%token C_MAYORIGUAL
%token C_MENORIGUAL
%token C_DISTINTO
%token C_IGUAL
%token CTEFLOAT
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
%token FLOAT
%token VOID
%token EXTENDS

%start programa

%%



programa :   sentencias_declarativas sentencias_ejecutables
           ;

sentencias_ejecutables : bloque ';'
			;

bloque : sentencia {System.out.println("se cargo una sentencia");}
       | BEGIN bloque_sentencias END
       | error_bloque
       ;
       
       
bloque_sentencias  :   sentencia 
                    |  bloque_sentencias sentencia
                    ;

error_bloque : error bloque_sentencias '}' {lexico.getLexico().agregarError("en la linea "+" (aca va el numero de la linea)"+" Error sintactico: falta '{' ");}
	    {/*  | error bloque_sentencias error  {lexico.getLexico().agregarError("en la linea "+ "(aca va el numero de la linea)"+" Error sintactico: falta '{'y '}' ");}*/}
	     | '{' bloque_sentencias error {lexico.getLexico().agregarError( "en la linea "+"(aca va el numero de la linea)"+" Error sintactico: falta '}' ");}
             
 ;


sentencia   :  ejecucion
           | error_sentencia_d 
            ;


error_sentencia_d : ejecucion error {	lexico.getLexico().agregarError( "(aca va el numero de la linea)"+"Error sintactico: falta la coma");}   
              	;


sentencias_declarativas  :  tipo lista_de_variables {System.out.println("llegue a esta puta declaracion");}
			| sentencia_de_clase
            ;


lista_de_variables : lista_de_variables ',' ID 
		    | ID  {lexico.getLexico().agregarEstructura( "en la linea "+"(aca va el numero de la linea)"+" se agrego el identificador"+ID);}{System.out.println("lei un ID");}
		    |error_lista_de_variables 
	        ;

error_lista_de_variables : lista_de_variables  ID {lexico.getLexico().agregarError( "(aca va el numero de la linea)"+"falta la , que separa las variables");}
		         ;

  
sentencia_de_clase : CLASS ID_CLASE BEGIN declaraciones_de_clase END
					| CLASS ID_CLASE EXTENDS ID_CLASE BEGIN declaraciones_de_clase END
					;

declaracion_de_clase : declaracion_atributos
					| declaracion_metodos
					;
					
declaracion_atributos : PUBLIC sentencias_declarativas
					| PRIVATE sentencias_declarativas
					;

declaracion_metodos : PUBLIC VOID ID_METODO '(' ')' bloque
					| PRIVATE VOID ID_METODO '(' ')' bloque
					;

tipo :  INT { System.out.println("lei un integer ");}
	| FLOAT    { System.out.println("lei un float" );}
	| ID_CLASE { System.out.println("lei un objeto de la clase: "+ID_CLASE );}
        ;



ejecucion : iteracion 
	  | seleccion 
	  | print 
	  | asignacion 
	  | invocacion_metodo_clase
	  ;		
	  
invocacion_metodo_clase : ID_CLASE '.' ID_METODO '(' ')' ';'
			| error_inv_metodo_clase
			;
			
error_inv_metodo_clase : error '.' ID_METODO '(' ')' ';' {lexico.getLexico().agregarError( "(aca va el numero de la linea)"+"falta la clase que contiene el metodo ");}
			| ID_CLASE '.' error '(' ')' ';' {lexico.getLexico().agregarError( "(aca va el numero de la linea)"+"falta definir el metodo de clase ");}
			| ID_CLASE '.' ID_METODO error ')' ';' {lexico.getLexico().agregarError( "(aca va el numero de la linea)"+"falta un parentesis ");}
			| ID_CLASE '.' ID_METODO '(' error ';' {lexico.getLexico().agregarError( "(aca va el numero de la linea)"+"falta un parentesis ");}
			| ID_CLASE '.' ID_METODO '(' ') error {lexico.getLexico().agregarError( "(aca va el numero de la linea)"+"falta el ';' ");}
			;

print : PRINT '(' CADENA ')'  {lexico.getLexico().agregarEstructura( "en la linea "+"(aca va el numero de la linea)"+" se hizo un print");}
	| error_print
	;



error_print : PRINT CADENA ')' {lexico.getLexico().agregarError( "(aca va el numero de la linea)"+"falta el '(' ");}
	     | PRINT '(' CADENA {lexico.getLexico().agregarError( "(aca va el numero de la linea)"+"falta el ')'");} 
	     | PRINT CADENA {lexico.getLexico().agregarError( "(aca va el numero de la linea)"+"falta los '(' ')' ");}
	     ;


iteracion : WHILE '(' condicion ')' DO bloque {lexico.getLexico().agregarEstructura( "en la linea "+"(aca va el numero de la linea)"+" se creo una iteracion");}
	| error_iteracion
	 ;

error_iteracion : WHILE error condicion ')' DO bloque {lexico.getLexico().agregarError("en la linea "+" (aca va el numero de la linea)"+"falta algun parentesis o llave");}
  	      |	WHILE '(' condicion error DO bloque {lexico.getLexico().agregarError("en la linea "+ "(aca va el numero de la linea)"+" falta algun parentesis o llave");}
  	      |	WHILE condicion error DO bloque {lexico.getLexico().agregarError("en la linea "+ "(aca va el numero de la linea)"+" falta algun parentesis o llave");}
		  | WHILE '(' condicion ')' error bloque {lexico.getLexico().agregarError("en la linea "+ "(aca va el numero de la linea)"+" falta la palabra DO");}
	      ;

cte : CTE_FLOAT {System.out.println("leida FLOAT");}
      | CTE {System.out.println("Leida CTE");}
	;



if_condicion : IF '(' condicion ')' 
             | IF error condicion ')' {lexico.getLexico().agregarError("en la linea "+ "(aca va el numero de la linea)"+"falta el '(' de la condicion ");}
             | IF '(' condicion error {lexico.getLexico().agregarError("en la linea "+ "(aca va el numero de la linea)"+"falta el ')' de la condicion ");}
             | IF condicion  error   {lexico.getLexico().agregarError("en la linea "+ "(aca va el numero de la linea)"+"faltan los parentesis de la condicion");}
            ;

seleccion : if_condicion bloque END_IF            {lexico.getLexico().agregarEstructura( "en la linea "+"(aca va el numero de la linea)"+" se agrego una condicion IF");}
          | if_condicion bloque ELSE bloque END_IF {lexico.getLexico().agregarEstructura( "en la linea "+"(aca va el numero de la linea)"+" se agrego una condicion IF con ELSE");}
          | if_condicion error                      {lexico.getLexico().agregarError("en la linea "+ "(aca va el numero de la linea)"+"faltan el bloque de la condicion");}
          | if_condicion bloque error               {lexico.getLexico().agregarError("en la linea "+ "(aca va el numero de la linea)"+"faltan end_if");}
          ;



asignacion : ID {System.out.println("lei id");} ASIGNACION {System.out.println("lei asig");} expresion{System.out.println("lei exp");} {lexico.getLexico().agregarEstructura( "en la linea "+"(aca va el numero de la linea)"+" se agrego una asignacion");System.out.println("realice una asignacion");}
           | error_asignacion
           ;


error_asignacion : ID expresion { System.out.println("Error"); lexico.getLexico().agregarError("en la linea "+ "(aca va el numero de la linea)"+"falta el := de la asignacion ");}
                 | ASIGNACION expresion { System.out.println("Error"); lexico.getLexico().agregarError("en la linea "+ "(aca va el numero de la linea)"+"falta el ID de la asignacion ");}
                 ;


expresion : expresion '+' termino {System.out.println("se hizo una suma ");}{lexico.getLexico().agregarEstructura( "en la linea "+"(aca va el numero de la linea)"+" se agrego una suma");} 
	  | expresion '-' termino {lexico.getLexico().agregarEstructura( "en la linea "+"(aca va el numero de la linea)"+" se agrego una resta");}
	  | termino {System.out.println("TERMINO a EXPR");}
          ;

termino : termino '*' factor {lexico.getLexico().agregarEstructura( "en la linea "+"(aca va el numero de la linea)"+" se agrego una multiplicacion");} 
	| termino '/' factor {lexico.getLexico().agregarEstructura( "en la linea "+"(aca va el numero de la linea)"+" se agrego una division");}
	| factor { System.out.println("FACTOR a TERMINO"); }
        ;

factor : cte { System.out.println("CTE a FACTOR"); }
	| factor_negado
	| ID {System.out.println("cargue un identificador");}
	| ID_CLASE '.' ID {System.out.println("cargue la variable "+ID+" de la clase "+ID_CLASE)}
	;

factor_negado : '-' CTE
			| '-' CTEFLOAT
              ;


condicion :  expresion comparador expresion
			|  error comparador expresion { System.out.println("Error"); lexico.getLexico().agregarError("en la linea "+ "(aca va el numero de la linea)"+"falta la primera expresion ");}
			| expresion comparador error { System.out.println("Error"); lexico.getLexico().agregarError("en la linea "+ "(aca va el numero de la linea)"+"falta la segunda expresion ");}
           ;

comparador : '<'
	   |  '>'
	   |   '=' 
       | C_MAYORIGUAL
	   | C_MENORIGUAL
	   | C_DISTINTO
	   | C_IGUAL
            ;

%%





private Controller lexico;
public Parser(Controller lexico)
{
  this.lexico = lexico;
} 

public int yylex(){
    Token token = this.lexico.getToken();
   	if(token != null ){ 
    	int val = token.getId();
    	yyval = new ParserVal(token);
    	return val;
	}
   	else return 0;
}

public void yyerror(String s){
    System.out.println("Parser: " + s);
}



