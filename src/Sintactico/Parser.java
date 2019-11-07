//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "gramatica.y"
package Sintactico; 
import Lexico.Controller;
import Lexico.Token;
import java.util.*;
//#line 22 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short CTE=258;
public final static short CADENA=259;
public final static short COMENTARIO=260;
public final static short ASIGNACION=261;
public final static short C_MAYORIGUAL=262;
public final static short C_MENORIGUAL=263;
public final static short C_DISTINTO=264;
public final static short IF=265;
public final static short ELSE=266;
public final static short END_IF=267;
public final static short INT=268;
public final static short BEGIN=269;
public final static short END=270;
public final static short PRINT=271;
public final static short WHILE=272;
public final static short DO=273;
public final static short CLASS=274;
public final static short PUBLIC=275;
public final static short PRIVATE=276;
public final static short C_IGUAL=277;
public final static short CTEFLOAT=278;
public final static short FLOAT=279;
public final static short VOID=280;
public final static short EXTENDS=281;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    1,    2,    2,    4,    4,    6,    6,
    8,    7,    7,    9,   10,   10,   11,   11,   12,   16,
   13,   15,    5,    5,    5,   14,   14,    3,    3,    3,
    3,   17,   17,   18,   19,   19,   19,   19,   19,   24,
   24,   25,   25,   25,   25,   25,   22,   22,   26,   26,
   26,   20,   20,   20,   20,   27,   27,   27,   30,   30,
   30,   30,   30,   30,   30,   21,   21,   31,   31,   31,
   31,   28,   28,   28,   28,   23,   23,   23,   23,   29,
   29,   29,   32,   32,   32,   33,   33,   33,   34,   34,
   34,   34,
};
final static short yylen[] = {                            2,
    1,    2,    1,    1,    1,    2,    3,    1,    3,    1,
    1,    3,    5,    3,    2,    1,    1,    1,    2,    0,
    7,    3,    1,    1,    1,    1,    1,    4,    4,    4,
    4,    2,    1,    1,    1,    1,    1,    1,    1,    6,
    1,    6,    6,    5,    6,    6,    5,    1,    5,    5,
    5,    6,    5,    5,    5,    3,    2,    3,    1,    1,
    1,    1,    1,    1,    1,    2,    4,    5,    4,    4,
    3,    1,    3,    3,    3,    4,    3,    4,    3,    3,
    3,    1,    3,    3,    1,    1,    3,    1,    1,    2,
    1,    2,
};
final static short yydefred[] = {                         0,
    0,   25,   24,    0,    0,   23,    0,    1,    0,    4,
    0,    0,    8,    0,    0,    0,    0,    0,    0,    0,
   33,   34,   35,   36,   37,   38,   39,   41,   48,    0,
    0,    0,    2,    6,   11,    0,   10,    0,    0,    0,
    0,   89,   62,   63,   64,   65,   91,    0,   59,   60,
   61,    0,    0,    0,    0,    0,   85,   88,    0,    0,
    0,    0,    0,    0,    0,   32,    0,   66,    0,    0,
    0,    0,   12,    7,    0,    0,    0,    0,    0,    0,
    0,   90,   92,    0,    0,    0,   72,   71,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   77,
   29,    0,   30,   31,   28,   26,   27,    0,   16,   17,
   18,    0,    0,    9,   78,    0,    0,    0,    0,   87,
    0,   70,    0,    0,   69,    0,    0,   58,    0,   83,
   84,    0,    0,    0,    0,    0,    0,   76,   67,   14,
   15,    0,   19,   13,    0,    0,   44,    0,    0,   68,
   75,    0,   73,   49,   50,   51,   47,   54,    0,   55,
   53,    0,   42,   43,   45,   46,   40,   52,    0,    0,
    0,   20,    0,   21,   22,
};
final static short yydgoto[] = {                          7,
    8,    9,   10,   11,   12,   36,   13,   19,   73,  108,
  109,  110,  111,  112,  172,  174,   20,   87,   22,   23,
   24,   25,   26,   27,   28,   29,   53,   88,   54,   55,
   30,   56,   57,   58,
};
final static short yysindex[] = {                      -166,
  -60,    0,    0,  -60, -241,    0,    0,    0, -216,    0,
 -213, -215,    0,  -28,   23,  -40,  -25,  -37,   81, -127,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -221,
  -82, -234,    0,    0,    0,   -1,    0,   14, -194, -150,
   36,    0,    0,    0,    0,    0,    0,   73,    0,    0,
    0, -245,  111,  -34,   14,   63,    0,    0, -180, -162,
   73,   90,   14,   82,   77,    0,  158,    0,  -42,  -53,
 -164, -117,    0,    0, -215,  133,  108,  106,  109, -101,
  121,    0,    0,  138,  -60,  158,    0,    0,   14,   14,
  -43,   17,   14,   14,  123,  -22,  -31, -105,  148,    0,
    0,  -98,    0,    0,    0,    0,    0, -187,    0,    0,
    0, -193,  -99,    0,    0,  132,  153,  120,  -12,    0,
  158,    0,  161,  178,    0,   63,   63,    0,   17,    0,
    0,  136,  139,  -52,  158, -106,  158,    0,    0,    0,
    0,  -58,    0,    0,  142,  143,    0,  157,  -47,    0,
    0,  -28,    0,    0,    0,    0,    0,    0,  158,    0,
    0,  160,    0,    0,    0,    0,    0,    0,  191,  -33,
  -60,    0,  181,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  239,    0,
    1,    0,    0,    0,   83,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -11,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   11,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   91,    0,    0,    0,    0,    0,    0,  -85,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   33,   55,    0,  101,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  141,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  236,  240,  151,    0,    0,    0,    2,  135,    0,
  156,    0,    0,    0,    0,    0,   35,   37,    0,    0,
    0,    0,    0,    0,    0,    0,   19,   18,   46,  196,
    0,   34,   64,    0,
};
final static int YYTABLESIZE=453;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         48,
    5,   52,   61,   39,   52,  105,  157,   52,   89,  136,
   90,  167,   82,   37,   60,   32,  103,   39,  134,   49,
   51,   50,   49,   51,   50,   49,   51,   50,  149,   86,
   86,   86,   83,   86,   71,   86,   62,   21,   31,    1,
   21,   35,   75,    2,   67,   68,   72,   86,   86,   86,
   86,   82,    4,   82,    3,   82,   66,   74,   52,   89,
    5,   90,   77,    2,   64,    6,   81,   66,   40,   82,
   82,   82,   82,   80,    3,   80,  114,   80,   95,   97,
    5,   80,  140,   76,  102,    6,  142,  106,  107,    1,
    2,   80,   80,   80,   80,   81,   96,   81,  122,   81,
   92,    3,    4,  125,   93,   78,   79,    5,   99,   94,
  106,  107,    6,   81,   81,   81,   81,   52,  123,  124,
   21,   21,  126,  127,   89,   52,   90,   11,   14,   15,
   98,   57,   49,   51,   50,  101,  129,   16,  150,  113,
  100,   56,   65,   17,   18,  117,  118,  116,  119,   84,
   15,   86,  158,  160,  161,  120,  130,  131,   16,   66,
   66,  121,   85,  132,   17,   18,  159,  137,  139,   71,
   79,   79,  145,   69,   15,   89,  168,   90,  147,   79,
   79,   79,   16,   39,   79,   79,   79,   70,   17,   18,
   89,  115,   90,  146,  154,   14,   15,  155,  162,  169,
  163,  164,  104,  156,   16,  173,  138,   21,  166,   66,
   17,   18,  128,   41,   42,  165,   41,   42,   38,   41,
   42,   43,   44,   45,   43,   44,   45,   43,   44,   45,
   59,  170,   38,  133,   47,  171,   46,   47,    3,   46,
   47,  135,   46,  148,   86,   86,   34,  144,   33,   91,
   86,   86,   86,   86,   86,   86,    5,   86,   86,   86,
   86,   86,  143,  141,    0,   86,   82,   82,    0,    5,
   41,   42,   82,   82,   82,   82,   82,   82,    0,   82,
   82,   82,   82,   82,    0,    0,    0,   82,   80,   80,
    0,   47,    0,    0,   80,   80,   80,   80,   80,   80,
    0,   80,   80,   80,   80,   80,    0,    0,    0,   80,
   81,   81,    0,    0,    0,    0,   81,   81,   81,   81,
   81,   81,    0,   81,   81,   81,   81,   81,    0,   41,
   42,   81,    0,    0,   43,   44,   45,   41,   42,   11,
   11,   63,    0,   11,    0,    0,   57,   57,    0,   46,
   47,    0,    0,    0,    0,   57,   56,   56,   47,   57,
   11,   57,   57,   57,    0,   56,   84,   15,    0,   56,
    0,   56,   56,   56,    0,   16,   84,   15,    0,   85,
    0,   17,   18,    0,    0,   16,    0,    0,    0,   85,
    0,   17,   18,   14,   15,    0,   74,   74,   38,    0,
    0,    0,   16,    0,    0,   74,   74,   74,   17,   18,
   74,   74,   74,   84,   15,    0,   14,   15,    0,    0,
    0,    0,   16,    0,    0,   16,   85,    0,   17,   18,
  151,   17,   18,  152,   15,    0,   14,   15,    0,    0,
    0,    0,   16,    0,    0,   16,    0,  153,   17,   18,
  175,   17,   18,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   45,   40,   46,   45,   59,   59,   45,   43,   41,
   45,   59,  258,   12,   40,  257,   59,   46,   41,   60,
   61,   62,   60,   61,   62,   60,   61,   62,   41,   41,
   42,   43,  278,   45,  269,   47,   18,    1,    4,  256,
    4,  257,   44,  257,  266,  267,  281,   59,   60,   61,
   62,   41,  269,   43,  268,   45,   20,   59,   45,   43,
  274,   45,  257,  257,   19,  279,   48,   31,   46,   59,
   60,   61,   62,   41,  268,   43,   75,   45,  259,   61,
  274,   46,  270,   38,   67,  279,  280,  275,  276,  256,
  257,   59,   60,   61,   62,   41,  259,   43,   81,   45,
   55,  268,  269,   86,   42,  256,  257,  274,   63,   47,
  275,  276,  279,   59,   60,   61,   62,   45,   84,   85,
   84,   85,   89,   90,   43,   45,   45,   45,  256,  257,
   41,   41,   60,   61,   62,   59,   91,  265,  121,  257,
   59,   41,  270,  271,  272,   40,   41,   40,   40,  256,
  257,   41,  135,  136,  137,  257,   93,   94,  265,  123,
  124,   41,  269,   41,  271,  272,  273,  273,  267,  269,
  256,  257,   41,  256,  257,   43,  159,   45,   59,  265,
  266,  267,  265,   46,  270,  271,  272,  270,  271,  272,
   43,   59,   45,   41,   59,  256,  257,   59,  257,   40,
   59,   59,  256,  256,  265,  171,   59,  171,  256,  173,
  271,  272,  256,  257,  258,   59,  257,  258,  261,  257,
  258,  262,  263,  264,  262,  263,  264,  262,  263,  264,
  256,   41,  261,  256,  278,  269,  277,  278,    0,  277,
  278,  273,  277,  256,  256,  257,   11,  113,    9,   54,
  262,  263,  264,  265,  266,  267,  256,  269,  270,  271,
  272,  273,  112,  108,   -1,  277,  256,  257,   -1,  269,
  257,  258,  262,  263,  264,  265,  266,  267,   -1,  269,
  270,  271,  272,  273,   -1,   -1,   -1,  277,  256,  257,
   -1,  278,   -1,   -1,  262,  263,  264,  265,  266,  267,
   -1,  269,  270,  271,  272,  273,   -1,   -1,   -1,  277,
  256,  257,   -1,   -1,   -1,   -1,  262,  263,  264,  265,
  266,  267,   -1,  269,  270,  271,  272,  273,   -1,  257,
  258,  277,   -1,   -1,  262,  263,  264,  257,  258,  257,
  258,  261,   -1,  261,   -1,   -1,  256,  257,   -1,  277,
  278,   -1,   -1,   -1,   -1,  265,  256,  257,  278,  269,
  278,  271,  272,  273,   -1,  265,  256,  257,   -1,  269,
   -1,  271,  272,  273,   -1,  265,  256,  257,   -1,  269,
   -1,  271,  272,   -1,   -1,  265,   -1,   -1,   -1,  269,
   -1,  271,  272,  256,  257,   -1,  256,  257,  261,   -1,
   -1,   -1,  265,   -1,   -1,  265,  266,  267,  271,  272,
  270,  271,  272,  256,  257,   -1,  256,  257,   -1,   -1,
   -1,   -1,  265,   -1,   -1,  265,  269,   -1,  271,  272,
  270,  271,  272,  256,  257,   -1,  256,  257,   -1,   -1,
   -1,   -1,  265,   -1,   -1,  265,   -1,  270,  271,  272,
  270,  271,  272,
};
}
final static short YYFINAL=7;
final static short YYMAXTOKEN=281;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,"ID","CTE","CADENA","COMENTARIO","ASIGNACION",
"C_MAYORIGUAL","C_MENORIGUAL","C_DISTINTO","IF","ELSE","END_IF","INT","BEGIN",
"END","PRINT","WHILE","DO","CLASS","PUBLIC","PRIVATE","C_IGUAL","CTEFLOAT",
"FLOAT","VOID","EXTENDS",
};
final static String yyrule[] = {
"$accept : programa",
"programa : conjunto_sentencias",
"conjunto_sentencias : conj_sentencias_declarativas sentencias_ejecutables",
"conjunto_sentencias : conj_sentencias_declarativas",
"conjunto_sentencias : sentencias_ejecutables",
"conj_sentencias_declarativas : sentencia_declarativa",
"conj_sentencias_declarativas : sentencia_declarativa conj_sentencias_declarativas",
"sentencia_declarativa : tipo lista_de_variables ';'",
"sentencia_declarativa : sentencia_de_clase",
"lista_de_variables : lista_de_variables ',' variable",
"lista_de_variables : variable",
"variable : ID",
"sentencia_de_clase : CLASS ID definicion_clase",
"sentencia_de_clase : CLASS ID EXTENDS ID definicion_clase",
"definicion_clase : BEGIN sentencias_clase END",
"sentencias_clase : sentencias_clase cuerpo_clase",
"sentencias_clase : cuerpo_clase",
"cuerpo_clase : lista_atributos",
"cuerpo_clase : metodo_clase",
"lista_atributos : visibilidad sentencia_declarativa",
"$$1 :",
"metodo_clase : visibilidad VOID ID '(' ')' bloque_anidado_metclase $$1",
"bloque_anidado_metclase : BEGIN bloque_sentencias END",
"tipo : FLOAT",
"tipo : INT",
"tipo : ID",
"visibilidad : PUBLIC",
"visibilidad : PRIVATE",
"sentencias_ejecutables : BEGIN bloque_sentencias END ';'",
"sentencias_ejecutables : error bloque_sentencias END ';'",
"sentencias_ejecutables : BEGIN bloque_sentencias error ';'",
"sentencias_ejecutables : BEGIN bloque_sentencias END error",
"bloque_sentencias : bloque_sentencias sentencia",
"bloque_sentencias : sentencia",
"sentencia : ejecucion",
"ejecucion : iteracion",
"ejecucion : seleccion",
"ejecucion : print",
"ejecucion : asig",
"ejecucion : invocacion_metodo_clase",
"invocacion_metodo_clase : ID '.' ID '(' ')' ';'",
"invocacion_metodo_clase : error_inv_metodo_clase",
"error_inv_metodo_clase : error '.' ID '(' ')' ';'",
"error_inv_metodo_clase : ID '.' error '(' ')' ';'",
"error_inv_metodo_clase : ID '.' error ')' ';'",
"error_inv_metodo_clase : ID '.' ID '(' error ';'",
"error_inv_metodo_clase : ID '.' ID '(' ')' error",
"print : PRINT '(' CADENA ')' ';'",
"print : error_print",
"error_print : PRINT error CADENA ')' ';'",
"error_print : PRINT '(' CADENA error ';'",
"error_print : PRINT '(' CADENA ')' error",
"iteracion : WHILE '(' condicion ')' DO bloque_anidado",
"iteracion : WHILE condicion ')' DO bloque_anidado",
"iteracion : WHILE '(' condicion DO bloque_anidado",
"iteracion : WHILE '(' condicion ')' bloque_anidado",
"condicion : expresion comparador expresion",
"condicion : comparador expresion",
"condicion : expresion comparador error",
"comparador : '<'",
"comparador : '>'",
"comparador : '='",
"comparador : C_MAYORIGUAL",
"comparador : C_MENORIGUAL",
"comparador : C_DISTINTO",
"comparador : C_IGUAL",
"seleccion : if_condicion END_IF",
"seleccion : if_condicion ELSE bloque_anidado END_IF",
"if_condicion : IF '(' condicion ')' bloque_anidado",
"if_condicion : IF condicion ')' bloque_anidado",
"if_condicion : IF '(' condicion bloque_anidado",
"if_condicion : IF condicion bloque_anidado",
"bloque_anidado : sentencia",
"bloque_anidado : BEGIN bloque_sentencias END",
"bloque_anidado : BEGIN bloque_sentencias error",
"bloque_anidado : error bloque_sentencias END",
"asig : variable ASIGNACION expresion ';'",
"asig : variable expresion ';'",
"asig : error ASIGNACION expresion ';'",
"asig : variable ASIGNACION expresion",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : ID",
"factor : ID '.' ID",
"factor : cte",
"cte : CTE",
"cte : '-' CTE",
"cte : CTEFLOAT",
"cte : '-' CTEFLOAT",
};

//#line 212 "gramatica.y"


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
		if (classFlag == true) {
			if (type.equals("int"))
				lexico.getLexico().addVarTS(t.getLexema(), type, "Variable", 0, className, "-");
			else if (type.equals("float"))
				lexico.getLexico().addVarTS(t.getLexema(), type, "Variable", 0.0, className, "-");
		} else {
			if (type.equals("int"))
				lexico.getLexico().addVarTS(t.getLexema(), type, "Variable", 0, "-", "-");
			else if (type.equals("float"))
				lexico.getLexico().addVarTS(t.getLexema(), type, "Variable", 0.0, "-", "-");
		}
	}
	removeVars();
	classFlag = false;
	className = "";
}

public void setClass(String cn) {
	classFlag = true;
	className = cn;
				System.out.println(className);
							System.out.println(classFlag);
}

public void removeVars() {
	int size = listaVar.size();
	for (int i = 0; i < size; i++) 
		listaVar.remove(0);
}

//#line 503 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 2:
//#line 43 "gramatica.y"
{System.out.println("Finaliza el programa");}
break;
case 7:
//#line 52 "gramatica.y"
{System.out.println("Llegue a una declaracion valida");
													  System.out.println(((Token)val_peek(2).obj).getLexema()); addVariableTS(((Token)val_peek(2).obj).getLexema());}
break;
case 8:
//#line 54 "gramatica.y"
{System.out.println("Llegue a una declaracion de clase");}
break;
case 9:
//#line 58 "gramatica.y"
{listaVar.add(((Token)val_peek(0).obj));}
break;
case 10:
//#line 59 "gramatica.y"
{listaVar.add(((Token)val_peek(0).obj));}
break;
case 11:
//#line 62 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego el identificador: "+ ((Token)val_peek(0).obj).getLexema());}
break;
case 12:
//#line 66 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se creó un componente de clase con el nombre: "+((Token)val_peek(1).obj).getLexema());
																																											   setClass(((Token)val_peek(1).obj).getLexema());}
break;
case 13:
//#line 68 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se creó un componente de clase extendida con el nombre: "+((Token)val_peek(3).obj).getLexema()+", que extiende de: "+((Token)val_peek(1).obj).getLexema());
																																																													  setClass(((Token)val_peek(3).obj).getLexema());}
break;
case 19:
//#line 83 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se creó un atributo de clase");}
break;
case 20:
//#line 86 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se creó un metodo de clase");}
break;
case 21:
//#line 86 "gramatica.y"
{ System.out.println("Viene un metodo");}
break;
case 23:
//#line 93 "gramatica.y"
{ System.out.println("Viene una variable tipo FLOAT");}
break;
case 24:
//#line 94 "gramatica.y"
{ System.out.println("Viene una variable tipo INT" );}
break;
case 25:
//#line 95 "gramatica.y"
{ System.out.println("Viene una variable de la clase: "+ID );}
break;
case 29:
//#line 103 "gramatica.y"
{lexico.getLexico().addError("Falta el BEGIN para el bloque de sentencias ejecutables.", lexico.getLexico().getNroLinea());}
break;
case 30:
//#line 104 "gramatica.y"
{lexico.getLexico().addError("Falta el END para el bloque de sentencias ejecutables.", lexico.getLexico().getNroLinea());}
break;
case 31:
//#line 105 "gramatica.y"
{lexico.getLexico().addError("Falta el ';' que cierra el bloque de sentencias ejecutables.", lexico.getLexico().getNroLinea());}
break;
case 33:
//#line 109 "gramatica.y"
{System.out.println("Se cargo una sentencia");}
break;
case 42:
//#line 126 "gramatica.y"
{lexico.getLexico().addError("Falta la clase que contiene el metodo", lexico.getLexico().getNroLinea());}
break;
case 43:
//#line 127 "gramatica.y"
{lexico.getLexico().addError("Falta definir el metodo de clase ", lexico.getLexico().getNroLinea());}
break;
case 44:
//#line 128 "gramatica.y"
{lexico.getLexico().addError("Falta un parentesis ", lexico.getLexico().getNroLinea());}
break;
case 45:
//#line 129 "gramatica.y"
{lexico.getLexico().addError("Falta un parentesis ", lexico.getLexico().getNroLinea());}
break;
case 46:
//#line 130 "gramatica.y"
{lexico.getLexico().addError("Falta el ';' ", lexico.getLexico().getNroLinea());}
break;
case 47:
//#line 133 "gramatica.y"
{lexico.getLexico().agregarEstructura( "En la linea "+lexico.getLexico().getNroLinea()+" se hizo un print");}
break;
case 49:
//#line 138 "gramatica.y"
{lexico.getLexico().addError("Falta el '('.", lexico.getLexico().getNroLinea());}
break;
case 50:
//#line 139 "gramatica.y"
{lexico.getLexico().addError("Falta el ')'.", lexico.getLexico().getNroLinea());}
break;
case 51:
//#line 140 "gramatica.y"
{lexico.getLexico().addError("Falta el ';' que cierra el print.", lexico.getLexico().getNroLinea());}
break;
case 52:
//#line 144 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se creo una iteracion");}
break;
case 53:
//#line 145 "gramatica.y"
{lexico.getLexico().addError("Falta algun parentesis en la iteracion", lexico.getLexico().getNroLinea());}
break;
case 54:
//#line 146 "gramatica.y"
{lexico.getLexico().addError("Falta algun parentesis en la iteracion", lexico.getLexico().getNroLinea());}
break;
case 55:
//#line 147 "gramatica.y"
{lexico.getLexico().addError("Falta la palabra DO en la iteracion", lexico.getLexico().getNroLinea());}
break;
case 57:
//#line 151 "gramatica.y"
{ System.out.println("Falta la primera expresion en la condicion"); lexico.getLexico().addError("Falta la primera expresion en la condicion", lexico.getLexico().getNroLinea());}
break;
case 58:
//#line 152 "gramatica.y"
{ System.out.println("Falta la segunda expresion en la condicion"); lexico.getLexico().addError("Falta la segunda expresion en la condicion", lexico.getLexico().getNroLinea());}
break;
case 66:
//#line 164 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una condicion IF");}
break;
case 67:
//#line 165 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una condicion IF con ELSE");}
break;
case 69:
//#line 169 "gramatica.y"
{lexico.getLexico().addError("Falta el '(' de la condicion ", lexico.getLexico().getNroLinea());}
break;
case 70:
//#line 170 "gramatica.y"
{lexico.getLexico().addError("Falta el ')' de la condicion ", lexico.getLexico().getNroLinea());}
break;
case 71:
//#line 171 "gramatica.y"
{lexico.getLexico().addError("Faltan los parentesis de la condicion", lexico.getLexico().getNroLinea());}
break;
case 74:
//#line 176 "gramatica.y"
{lexico.getLexico().addError("Falta el END en el bloque anidado", lexico.getLexico().getNroLinea());}
break;
case 75:
//#line 177 "gramatica.y"
{lexico.getLexico().addError("Falta el BEGIN en el bloque anidado", lexico.getLexico().getNroLinea());}
break;
case 76:
//#line 180 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una asignacion");System.out.println("Realizo la asignacion en la linea: "+lexico.getLexico().getNroLinea());}
break;
case 77:
//#line 181 "gramatica.y"
{ System.out.println("Error, falta el ':=' de la asignacion"); lexico.getLexico().addError("Falta el ':=' de la asignacion ", lexico.getLexico().getNroLinea());}
break;
case 78:
//#line 182 "gramatica.y"
{ System.out.println("Error, falta el ID a la izquierda de la asignacion"); lexico.getLexico().addError("Falta el ID de la asignacion ", lexico.getLexico().getNroLinea());}
break;
case 79:
//#line 183 "gramatica.y"
{lexico.getLexico().addError("Falta el ';' que cierra la asignacion.", lexico.getLexico().getNroLinea());}
break;
case 80:
//#line 186 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una suma");}
break;
case 81:
//#line 187 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una resta");}
break;
case 82:
//#line 188 "gramatica.y"
{System.out.println("Paso de termino a expresion");}
break;
case 83:
//#line 191 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una multiplicacion");}
break;
case 84:
//#line 192 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una division");}
break;
case 85:
//#line 193 "gramatica.y"
{ System.out.println("Paso factor a termino"); }
break;
case 86:
//#line 196 "gramatica.y"
{System.out.println("Cargo un identificador");}
break;
case 87:
//#line 197 "gramatica.y"
{System.out.println("Cargue la variable "+ID+" de la clase "+ID);}
break;
case 88:
//#line 198 "gramatica.y"
{System.out.println("Paso de cte a factor");}
break;
case 89:
//#line 202 "gramatica.y"
{System.out.println("Leo una constante INT");}
break;
case 90:
//#line 203 "gramatica.y"
{System.out.println("Leo una constante negada");}
break;
case 91:
//#line 204 "gramatica.y"
{System.out.println("Leo una constante FLOAT");}
break;
case 92:
//#line 205 "gramatica.y"
{System.out.println("Leo una float negada");}
break;
//#line 883 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
