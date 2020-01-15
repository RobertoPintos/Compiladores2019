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
import Lexico.Atributo;
import java.util.*;
import GeneracionDeCodigo.*;
//#line 24 "Parser.java"




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
    8,   10,    7,   11,    7,    9,   12,   12,   13,   13,
   14,   18,   15,   17,    5,    5,    5,   16,   16,    3,
    3,    3,    3,   19,   19,   20,   21,   21,   21,   21,
   21,   26,   26,   27,   27,   27,   27,   27,   24,   24,
   28,   28,   28,   29,   31,   22,   22,   33,   36,   30,
   32,   32,   32,   32,   35,   35,   35,   35,   35,   35,
   35,   23,   38,   23,   40,   37,   37,   37,   37,   39,
   39,   39,   39,   25,   25,   25,   25,   25,   34,   34,
   34,   41,   41,   41,   42,   42,   42,   43,   43,   43,
   43,
};
final static short yylen[] = {                            2,
    1,    2,    1,    1,    1,    2,    3,    1,    3,    1,
    1,    0,    4,    0,    6,    3,    2,    1,    1,    1,
    2,    0,    7,    3,    1,    1,    1,    1,    1,    4,
    4,    4,    4,    2,    1,    1,    1,    1,    1,    1,
    1,    6,    1,    6,    6,    5,    6,    6,    5,    1,
    5,    5,    5,    0,    0,    8,    1,    6,    0,    4,
    1,    3,    3,    3,    1,    1,    1,    1,    1,    1,
    1,    2,    0,    5,    0,    6,    4,    4,    3,    1,
    3,    3,    3,    4,    6,    3,    4,    3,    3,    3,
    1,    3,    3,    1,    1,    3,    1,    1,    2,    1,
    2,
};
final static short yydefred[] = {                         0,
    0,   27,   26,    0,    0,   25,    0,    1,    0,    4,
    0,    0,    8,    0,    0,    0,    0,    0,    0,   35,
   36,   37,   38,   39,   40,   41,   43,   50,   57,    0,
    0,    0,    2,    6,   11,    0,   10,    0,    0,    0,
   98,    0,  100,    0,    0,    0,    0,   94,   97,    0,
    0,    0,    0,    0,    0,    0,    0,   34,   72,    0,
    0,    0,    0,    0,    7,    0,    0,    0,    0,    0,
    0,    0,   99,  101,   86,    0,    0,    0,    0,    0,
    0,    0,    0,   80,   79,    0,    0,    0,    0,    0,
   31,    0,   32,   33,   30,    0,   13,    0,    9,   87,
    0,   96,   84,    0,    0,    0,    0,    0,    0,   92,
   93,   75,   78,    0,    0,   77,   68,   69,   70,   71,
   65,   66,   67,    0,    0,    0,    0,    0,    0,    0,
   28,   29,    0,   18,   19,   20,    0,    0,    0,    0,
   46,    0,    0,    0,    0,   83,    0,   81,    0,   51,
   52,   53,   49,    0,   55,   74,   16,   17,    0,   21,
   15,   44,   45,   85,   47,   48,   42,   76,    0,    0,
   61,   58,    0,    0,    0,    0,    0,    0,   64,    0,
   62,   56,   22,    0,    0,   23,    0,   24,
};
final static short yydgoto[] = {                          7,
    8,    9,   10,   11,   12,   36,   13,   37,   97,   63,
   64,  133,  134,  135,  136,  137,  186,  184,   19,   20,
   21,   22,   23,   24,   25,   26,   27,   28,   56,   51,
  173,  172,   29,   52,  124,   86,   30,   60,   85,  145,
   47,   48,   49,
};
final static short yysindex[] = {                      -189,
  101,    0,    0,  101, -238,    0,    0,    0, -216,    0,
 -235, -215,    0,  -18,   51,  -40,  -37, -241, -146,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -205,
 -124,    0,    0,    0,    0,  -21,    0,  -34, -191,   23,
    0,  -34,    0, -198, -242,   18,   62,    0,    0,  -34,
   50,   58, -175, -151,  -34,   78,   72,    0,    0, -115,
  -28,  -57, -113, -139,    0, -215,  112,  114,  -83,  132,
   73,  -36,    0,    0,    0,  -34,  -34,  -34,  -34,   61,
   88,  101, -107,    0,    0,  -15,  122,   -6,  135,  -34,
    0, -107,    0,    0,    0, -148,    0,  -64,    0,    0,
  155,    0,    0,  156,  148,  -34,   16,   62,   62,    0,
    0,    0,    0, -104,  -87,    0,    0,    0,    0,    0,
    0,    0,    0,  -34,  149,  152,  -50,  -61,  172,  -47,
    0,    0, -187,    0,    0,    0, -193, -113,  167,  180,
    0,  220,  181,  -46, -107,    0,  -18,    0,   58,    0,
    0,    0,    0,  -77,    0,    0,    0,    0,  -16,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   88,  101,
    0,    0,   -9,  229,  -67,  -56,  -77,  230,    0,  -18,
    0,    0,    0,   15,  101,    0,   91,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  285,    0,
    1,    0,    0,    0,    0,    0,    0,  246,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   21,
    0, -226,    0,    0,    0,    0,    0,    0,    0,  -35,
    0,    0,    0,    0,    0,    0,  -11,    0,    0,    0,
    0,   75,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -150,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   11,   33,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -94,    0,   71,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -127,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  280,  283,  157,    0,    0,    0,  227,  163,    0,
    0,    0,  178,    0,    0,    0,    0,    0,   17,  304,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   10,
    0,  136,    0,   -1,    0,    0,    0,    0,  -63,    0,
   83,  103,    0,
};
final static int YYTABLESIZE=491;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         50,
    5,   95,   54,  107,   45,   95,   95,   95,  153,   95,
   45,   95,  167,   46,   55,   73,  113,   39,   32,  116,
   31,    2,   66,   95,   95,   95,   95,   39,  130,   91,
   93,   91,    3,   91,  127,   74,   67,   65,    5,    1,
   70,   35,   12,    6,  121,  123,  122,   91,   91,   91,
   91,   89,    4,   89,   14,   89,  144,   71,   72,   80,
   76,   59,   77,    2,   89,   68,    1,    2,   69,   89,
   89,   89,   89,   90,    3,   90,   75,   90,    3,    4,
    5,  168,  157,   87,    5,    6,  159,  131,  132,    6,
   83,   90,   90,   90,   90,   45,   44,  114,  115,  129,
   76,  112,   77,   78,  142,   88,   88,   88,   79,   14,
   15,   60,  104,  105,   88,   88,   88,   90,   16,   88,
   88,   88,  149,   57,   17,   18,  131,  132,   63,   63,
   91,   61,   15,   39,   59,   59,   59,   63,   63,   63,
   16,   98,   63,   63,   63,   62,   17,   18,   81,   15,
   92,   14,   15,  101,   76,   96,   77,   16,  108,  109,
   16,   82,  125,   17,   18,  146,   17,   18,  147,   15,
  100,   82,   82,  102,   76,  128,   77,   16,  169,   15,
  110,  111,  148,   17,   18,  175,  176,   16,   14,   15,
  103,  170,  138,   17,   18,  139,  140,   16,   94,  180,
   15,  187,  179,   17,   18,  152,  141,  150,   16,  166,
  151,  154,  155,  181,   17,   18,   40,   41,   53,  156,
   95,   95,   40,   41,  106,  162,   95,   95,   95,   95,
   95,   95,   38,   95,   95,   95,   95,   43,  163,  165,
  174,   95,   38,   43,   91,   91,  117,  118,  119,  126,
   91,   91,   91,   91,   91,   91,    5,   91,   91,   91,
   91,  120,   76,  177,   77,   91,   89,   89,  178,    5,
  183,  143,   89,   89,   89,   89,   89,   89,  164,   89,
   89,   89,   89,  185,    3,   54,   73,   89,   90,   90,
   34,   33,   99,  160,   90,   90,   90,   90,   90,   90,
  161,   90,   90,   90,   90,   81,   15,   40,   41,   90,
  158,   42,  182,    0,   16,    0,   81,   15,   82,    0,
   17,   18,   58,    0,    0,   16,   60,   60,   43,   82,
    0,   17,   18,    0,   58,   60,   59,   59,   59,   60,
    0,   60,   60,   14,   15,    0,   14,   15,   38,    0,
    0,   59,   16,    0,   84,   16,   14,   15,   17,   18,
  188,   17,   18,    0,    0,   16,    0,    0,    0,    0,
    0,   17,   18,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   84,    0,    0,   84,    0,    0,    0,
    0,    0,    0,    0,    0,   84,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   58,   58,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   84,    0,
    0,    0,    0,    0,    0,    0,    0,  171,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   58,   58,
  171,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   58,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   59,   40,   40,   45,   41,   42,   43,   59,   45,
   45,   47,   59,   15,  256,  258,   80,   46,  257,   83,
    4,  257,   44,   59,   60,   61,   62,   46,   92,   41,
   59,   43,  268,   45,   41,  278,   38,   59,  274,  256,
   42,  257,  269,  279,   60,   61,   62,   59,   60,   61,
   62,   41,  269,   43,  281,   45,   41,  256,  257,   50,
   43,  267,   45,  257,   55,  257,  256,  257,   46,   59,
   60,   61,   62,   41,  268,   43,   59,   45,  268,  269,
  274,  145,  270,  259,  274,  279,  280,  275,  276,  279,
   41,   59,   60,   61,   62,   45,   46,   81,   82,   90,
   43,   41,   45,   42,  106,  256,  257,  259,   47,  256,
  257,   41,   40,   41,  265,  266,  267,   40,  265,  270,
  271,  272,  124,  270,  271,  272,  275,  276,  256,  257,
   59,  256,  257,   46,   60,   61,   62,  265,  266,  267,
  265,  281,  270,  271,  272,  270,  271,  272,  256,  257,
  266,  256,  257,   40,   43,  269,   45,  265,   76,   77,
  265,  269,   41,  271,  272,  270,  271,  272,  256,  257,
   59,  266,  267,  257,   43,   41,   45,  265,  256,  257,
   78,   79,  270,  271,  272,  169,  170,  265,  256,  257,
   59,  269,  257,  271,  272,   41,   41,  265,  256,  256,
  257,  185,  270,  271,  272,  256,   59,   59,  265,  256,
   59,  273,   41,  270,  271,  272,  257,  258,  256,  267,
  256,  257,  257,  258,  261,   59,  262,  263,  264,  265,
  266,  267,  261,  269,  270,  271,  272,  278,   59,   59,
  257,  277,  261,  278,  256,  257,  262,  263,  264,  256,
  262,  263,  264,  265,  266,  267,  256,  269,  270,  271,
  272,  277,   43,  273,   45,  277,  256,  257,   40,  269,
   41,  256,  262,  263,  264,  265,  266,  267,   59,  269,
  270,  271,  272,  269,    0,   40,  266,  277,  256,  257,
   11,    9,   66,  137,  262,  263,  264,  265,  266,  267,
  138,  269,  270,  271,  272,  256,  257,  257,  258,  277,
  133,  261,  177,   -1,  265,   -1,  256,  257,  269,   -1,
  271,  272,   19,   -1,   -1,  265,  256,  257,  278,  269,
   -1,  271,  272,   -1,   31,  265,  262,  263,  264,  269,
   -1,  271,  272,  256,  257,   -1,  256,  257,  261,   -1,
   -1,  277,  265,   -1,   51,  265,  256,  257,  271,  272,
  270,  271,  272,   -1,   -1,  265,   -1,   -1,   -1,   -1,
   -1,  271,  272,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   80,   -1,   -1,   83,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   92,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  114,  115,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  145,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  154,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  175,  176,
  177,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  187,
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
"$$1 :",
"sentencia_de_clase : CLASS ID $$1 definicion_clase",
"$$2 :",
"sentencia_de_clase : CLASS ID $$2 EXTENDS ID definicion_clase",
"definicion_clase : BEGIN sentencias_clase END",
"sentencias_clase : sentencias_clase cuerpo_clase",
"sentencias_clase : cuerpo_clase",
"cuerpo_clase : lista_atributos",
"cuerpo_clase : metodo_clase",
"lista_atributos : visibilidad sentencia_declarativa",
"$$3 :",
"metodo_clase : visibilidad VOID ID '(' ')' $$3 bloque_anidado_metclase",
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
"$$4 :",
"$$5 :",
"iteracion : WHILE $$4 '(' condicion ')' $$5 DO bloque_anidado_while",
"iteracion : error_iteracion",
"error_iteracion : WHILE error condicion ')' DO bloque_anidado_while",
"$$6 :",
"condicion : expresion $$6 comparador expresion",
"bloque_anidado_while : sentencia",
"bloque_anidado_while : BEGIN bloque_sentencias END",
"bloque_anidado_while : BEGIN bloque_sentencias error",
"bloque_anidado_while : error bloque_sentencias END",
"comparador : '<'",
"comparador : '>'",
"comparador : '='",
"comparador : C_MAYORIGUAL",
"comparador : C_MENORIGUAL",
"comparador : C_DISTINTO",
"comparador : C_IGUAL",
"seleccion : if_condicion END_IF",
"$$7 :",
"seleccion : if_condicion $$7 ELSE bloque_anidado_if END_IF",
"$$8 :",
"if_condicion : IF '(' condicion ')' $$8 bloque_anidado_if",
"if_condicion : IF condicion ')' bloque_anidado_if",
"if_condicion : IF '(' condicion bloque_anidado_if",
"if_condicion : IF condicion bloque_anidado_if",
"bloque_anidado_if : sentencia",
"bloque_anidado_if : BEGIN bloque_sentencias END",
"bloque_anidado_if : BEGIN bloque_sentencias error",
"bloque_anidado_if : error bloque_sentencias END",
"asig : ID ASIGNACION expresion ';'",
"asig : ID '.' ID ASIGNACION expresion ';'",
"asig : ID expresion ';'",
"asig : error ASIGNACION expresion ';'",
"asig : ID ASIGNACION expresion",
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

//#line 315 "gramatica.y"


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
		System.out.println(op2);
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
	
		Terceto t = new Terceto ("BF", "["+Integer.toString(genCodigo.getTercetosController().getCantTercetos())+"]", "-", genCodigo.getTercetosController().getCantTercetos()+1);
		genCodigo.getTercetosController().addTercetoLista(t);
		genCodigo.getTercetosController().apilarTerceto(t);
		
}

public void completarTercetoFinalIF () {

		Terceto aux = genCodigo.getTercetosController().getTercetoPila();
		System.out.println("DESAPILO Y COMPLETO EL TERCETO: " + aux.getNumTerceto());
		aux.completarTerceto ("["+Integer.toString(genCodigo.getTercetosController().getCantTercetos()+2)+"]");
		genCodigo.getTercetosController().modificarTercetoLista(aux.getNumTerceto()-1, aux);
		genCodigo.getTercetosController().removeTercetoPila();
		Terceto BI = new Terceto ("BI", "-", "-", genCodigo.getTercetosController().getCantTercetos()+1);
		genCodigo.getTercetosController().addTercetoLista(BI);
		genCodigo.getTercetosController().apilarTerceto(BI);

}

public void desapilarYCompletar () {

		Terceto aux = genCodigo.getTercetosController().getTercetoPila();
		System.out.println("DESAPILO Y COMPLETO EL TERCETO: " + aux.getNumTerceto());
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
		System.out.println("DESAPILO Y COMPLETO EL TERCETO: " + aux.getNumTerceto());
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
		} else {
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
//#line 1095 "Parser.java"
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
case 1:
//#line 42 "gramatica.y"
{ addTercetoFinal(); }
break;
case 2:
//#line 45 "gramatica.y"
{System.out.println("Finaliza el programa");}
break;
case 7:
//#line 54 "gramatica.y"
{System.out.println("Llegue a una declaracion valida");
													   			  addVariableTS(((Token)val_peek(2).obj).getLexema());}
break;
case 8:
//#line 56 "gramatica.y"
{System.out.println("Llegue a una declaracion de clase");}
break;
case 9:
//#line 60 "gramatica.y"
{if(existeVar((Token)val_peek(0).obj)){
														System.out.println("Error variable ya declarada"); 
														assembler.getConversorAssembler().addErrorCI("variable ya declarada con ese nombre: "+((Token)val_peek(0).obj).getLexema(), lexico.getLexico().getNroLinea());
													  }else{	
														listaVar.add(((Token)val_peek(0).obj));
														}}
break;
case 10:
//#line 66 "gramatica.y"
{if(existeVar((Token)val_peek(0).obj)){
		    	   					System.out.println("Error variable ya declarada"); 
									assembler.getConversorAssembler().addErrorCI("variable ya declarada con ese nombre: "+((Token)val_peek(0).obj).getLexema(), lexico.getLexico().getNroLinea());
		    	   				}else{
		    	   					listaVar.add((Token)val_peek(0).obj);
		    	   					}}
break;
case 11:
//#line 74 "gramatica.y"
{if(lexico.getTS().containsKey(((Token)val_peek(0).obj).getLexema())){
			  		System.out.println("Error variable ya declarada"); 
					assembler.getConversorAssembler().addErrorCI("variable ya declarada con ese nombre: "+((Token)val_peek(0).obj).getLexema(), lexico.getLexico().getNroLinea());
			  }else{
				lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego el identificador: "+ ((Token)val_peek(0).obj).getLexema());
				}}
break;
case 12:
//#line 83 "gramatica.y"
{setClass(((Token)val_peek(0).obj).getLexema());}
break;
case 13:
//#line 83 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se creó un componente de clase con el nombre: "+((Token)val_peek(2).obj).getLexema());
																																											 										  addClaseTS(((Token)val_peek(2).obj).getLexema());
																																											 										  						classFlag = false;
																																											 										  						   className = "";}
break;
case 14:
//#line 87 "gramatica.y"
{setClass(((Token)val_peek(0).obj).getLexema());}
break;
case 15:
//#line 87 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se creó un componente de clase extendida con el nombre: "+((Token)val_peek(4).obj).getLexema()+", que extiende de: "+((Token)val_peek(1).obj).getLexema());
																																																														 addClaseHeredadaTS(((Token)val_peek(4).obj).getLexema(),((Token)val_peek(1).obj).getLexema());
																																																														 														   classFlag = false;
																																											 										  						   																		  className = "";}
break;
case 21:
//#line 104 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se creó un atributo de clase");}
break;
case 22:
//#line 107 "gramatica.y"
{setFunctionLabel(((Token)val_peek(2).obj).getLexema());}
break;
case 23:
//#line 107 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se creó un metodo de clase"); 
																																				  System.out.println("Viene un metodo");
																												  addMetodoTS(((Token)val_peek(6).obj).getLexema(), ((Token)val_peek(4).obj).getLexema());
																												  													   setReturnLabel();}
break;
case 25:
//#line 117 "gramatica.y"
{ System.out.println("Viene una variable tipo FLOAT");}
break;
case 26:
//#line 118 "gramatica.y"
{ System.out.println("Viene una variable tipo INT" );}
break;
case 27:
//#line 119 "gramatica.y"
{ System.out.println("Viene una variable de la clase: "+ ((Token)val_peek(0).obj).getLexema());
   	 							set = true;
   	 							clase = ((Token)val_peek(0).obj).getLexema();
   	 							if (estaDeclarada(clase))
   	 								existeClase = true;
   	 							else
   	 								existeClase = false;}
break;
case 28:
//#line 128 "gramatica.y"
{ visibilidad = "public"; }
break;
case 29:
//#line 129 "gramatica.y"
{ visibilidad = "private"; }
break;
case 31:
//#line 133 "gramatica.y"
{lexico.getLexico().addError("Falta el BEGIN para el bloque de sentencias ejecutables.", lexico.getLexico().getNroLinea());}
break;
case 32:
//#line 134 "gramatica.y"
{lexico.getLexico().addError("Falta el END para el bloque de sentencias ejecutables.", lexico.getLexico().getNroLinea());}
break;
case 33:
//#line 135 "gramatica.y"
{lexico.getLexico().addError("Falta el ';' que cierra el bloque de sentencias ejecutables.", lexico.getLexico().getNroLinea());}
break;
case 35:
//#line 139 "gramatica.y"
{System.out.println("Se cargo una sentencia");}
break;
case 42:
//#line 152 "gramatica.y"
{ if (esObjeto(((Token)val_peek(5).obj).getLexema()))
														if (esMetodo(((Token)val_peek(3).obj).getLexema())){
															callFunction(((Token)val_peek(5).obj).getLexema(), ((Token)val_peek(3).obj).getLexema());
														} else
															assembler.getConversorAssembler().addErrorCI("Se trato de invocar a un metodo inexistente", lexico.getLexico().getNroLinea());
												  else
												  		assembler.getConversorAssembler().addErrorCI("Objeto no declarado", lexico.getLexico().getNroLinea());			
												}
break;
case 44:
//#line 163 "gramatica.y"
{lexico.getLexico().addError("Falta la clase que contiene el metodo", lexico.getLexico().getNroLinea());}
break;
case 45:
//#line 164 "gramatica.y"
{lexico.getLexico().addError("Falta definir el metodo de clase ", lexico.getLexico().getNroLinea());}
break;
case 46:
//#line 165 "gramatica.y"
{lexico.getLexico().addError("Falta un parentesis ", lexico.getLexico().getNroLinea());}
break;
case 47:
//#line 166 "gramatica.y"
{lexico.getLexico().addError("Falta un parentesis ", lexico.getLexico().getNroLinea());}
break;
case 48:
//#line 167 "gramatica.y"
{lexico.getLexico().addError("Falta el ';' ", lexico.getLexico().getNroLinea());}
break;
case 49:
//#line 170 "gramatica.y"
{lexico.getLexico().agregarEstructura( "En la linea "+lexico.getLexico().getNroLinea()+" se hizo un print");
																	addTercetoPrint(((Token)val_peek(4).obj).getLexema(), ((Token)val_peek(2).obj).getLexema());}
break;
case 51:
//#line 176 "gramatica.y"
{lexico.getLexico().addError("Falta el '('.", lexico.getLexico().getNroLinea());}
break;
case 52:
//#line 177 "gramatica.y"
{lexico.getLexico().addError("Falta el ')'.", lexico.getLexico().getNroLinea());}
break;
case 53:
//#line 178 "gramatica.y"
{lexico.getLexico().addError("Falta el ';' que cierra el print.", lexico.getLexico().getNroLinea());}
break;
case 54:
//#line 182 "gramatica.y"
{ guardarNumCondicion(); }
break;
case 55:
//#line 182 "gramatica.y"
{apilarTercetoIncompletoWHILE();}
break;
case 56:
//#line 182 "gramatica.y"
{ lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se creo una iteracion");
																															cmp1 = false; cmp2 = false; condStart = 0;}
break;
case 58:
//#line 187 "gramatica.y"
{lexico.getLexico().addError("Falta algun parentesis en la iteracion", lexico.getLexico().getNroLinea());}
break;
case 59:
//#line 190 "gramatica.y"
{if (genCodigo.getTercetosController().getTercetoExp() != null)
							 cmp1 = true;}
break;
case 60:
//#line 191 "gramatica.y"
{ if (genCodigo.getTercetosController().getTercetoExp() != null)
							 																						cmp2 = true;
																												addTercetoCondicion(((Token)val_peek(3).obj).getLexema(), ((Token)val_peek(1).obj).getLexema(), ((Token)val_peek(0).obj).getLexema());}
break;
case 61:
//#line 196 "gramatica.y"
{ completarTercetoFinalWHILE(); }
break;
case 62:
//#line 197 "gramatica.y"
{ completarTercetoFinalWHILE(); }
break;
case 63:
//#line 198 "gramatica.y"
{lexico.getLexico().addError("Falta el END en el bloque anidado", lexico.getLexico().getNroLinea());}
break;
case 64:
//#line 199 "gramatica.y"
{lexico.getLexico().addError("Falta el BEGIN en el bloque anidado", lexico.getLexico().getNroLinea());}
break;
case 72:
//#line 212 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una condicion IF");
																														   desapilarYCompletar();
																										   cmp1 = false; cmp2 = false; condStart = 0;}
break;
case 73:
//#line 215 "gramatica.y"
{completarTercetoFinalIF();}
break;
case 74:
//#line 215 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una condicion IF con ELSE");
          																																											   desapilarYCompletar();
          																																							condStart = 0; cmp1 = false; cmp2 = false;}
break;
case 75:
//#line 220 "gramatica.y"
{apilarTercetoIncompletoIF();}
break;
case 77:
//#line 221 "gramatica.y"
{lexico.getLexico().addError("Falta el '(' de la condicion ", lexico.getLexico().getNroLinea());}
break;
case 78:
//#line 222 "gramatica.y"
{lexico.getLexico().addError("Falta el ')' de la condicion ", lexico.getLexico().getNroLinea());}
break;
case 79:
//#line 223 "gramatica.y"
{lexico.getLexico().addError("Faltan los parentesis de la condicion", lexico.getLexico().getNroLinea());}
break;
case 82:
//#line 228 "gramatica.y"
{lexico.getLexico().addError("Falta el END en el bloque anidado", lexico.getLexico().getNroLinea());}
break;
case 83:
//#line 229 "gramatica.y"
{lexico.getLexico().addError("Falta el BEGIN en el bloque anidado", lexico.getLexico().getNroLinea());}
break;
case 84:
//#line 232 "gramatica.y"
{ if (esMetodo(((Token)val_peek(3).obj).getLexema())) {
											lexico.getLexico().addError("No se puede hacer una asignacion a un metodo de clase.", lexico.getLexico().getNroLinea());
										} else {
											lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una asignacion");
											System.out.println("Realizo la asignacion en la linea: "+lexico.getLexico().getNroLinea());
											addTercetoAsignacion(((Token)val_peek(3).obj).getLexema(), ((Token)val_peek(1).obj).getLexema());
										} 
										genCodigo.getTercetosController().setTercetoExpNull();
										genCodigo.getTercetosController().setTercetoTermNull(); 
										resetAtClase();}
break;
case 85:
//#line 242 "gramatica.y"
{ if (esMetodo(((Token)val_peek(5).obj).getLexema())) {
											lexico.getLexico().addError("No se puede hacer una asignacion a un metodo de clase.", lexico.getLexico().getNroLinea());
										} else {
											lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una asignacion");
											System.out.println("Realizo la asignacion en la linea: "+lexico.getLexico().getNroLinea());
											addTercetoAsignacionVarClase(((Token)val_peek(3).obj).getLexema(), ((Token)val_peek(1).obj).getLexema(), ((Token)val_peek(5).obj).getLexema());
										} 
										genCodigo.getTercetosController().setTercetoExpNull();
										genCodigo.getTercetosController().setTercetoTermNull(); 
										resetAtClase();}
break;
case 86:
//#line 252 "gramatica.y"
{ System.out.println("Error, falta el ':=' de la asignacion"); lexico.getLexico().addError("Falta el ':=' de la asignacion ", lexico.getLexico().getNroLinea());}
break;
case 87:
//#line 253 "gramatica.y"
{ System.out.println("Error, falta el ID a la izquierda de la asignacion"); lexico.getLexico().addError("Falta el ID de la asignacion ", lexico.getLexico().getNroLinea());}
break;
case 88:
//#line 254 "gramatica.y"
{lexico.getLexico().addError("Falta el ';' que cierra la asignacion.", lexico.getLexico().getNroLinea());}
break;
case 89:
//#line 257 "gramatica.y"
{
											lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una suma");
									   		addTercetoExpresion(((Token)val_peek(1).obj).getLexema(), ((Token)val_peek(2).obj).getLexema(), ((Token)val_peek(0).obj).getLexema());
									}
break;
case 90:
//#line 261 "gramatica.y"
{
	  										lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una resta");
	  								  		addTercetoExpresion(((Token)val_peek(1).obj).getLexema(), ((Token)val_peek(2).obj).getLexema(), ((Token)val_peek(0).obj).getLexema());
	  								}
break;
case 91:
//#line 265 "gramatica.y"
{System.out.println("Paso de termino a expresion");
      												   cambiarTercetos();}
break;
case 92:
//#line 269 "gramatica.y"
{
										 lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una multiplicacion");
										 addTercetoTermino(((Token)val_peek(1).obj).getLexema(), ((Token)val_peek(2).obj).getLexema(), ((Token)val_peek(0).obj).getLexema());		 
							 }
break;
case 93:
//#line 273 "gramatica.y"
{
								   lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una division");
								   addTercetoTermino(((Token)val_peek(1).obj).getLexema(), ((Token)val_peek(2).obj).getLexema(), ((Token)val_peek(0).obj).getLexema());
							 }
break;
case 94:
//#line 277 "gramatica.y"
{ System.out.println("Paso factor a termino");}
break;
case 95:
//#line 280 "gramatica.y"
{System.out.println("Cargo un identificador");}
break;
case 96:
//#line 281 "gramatica.y"
{if (estaDeclarada(((Token)val_peek(2).obj).getLexema()))
						if (esObjeto(((Token)val_peek(2).obj).getLexema()))
							if (estaDeclarada(((Token)val_peek(0).obj).getLexema())) {
								if (esAtributoClase(((Token)val_peek(0).obj).getLexema())) {
									if (checkVisibilidad(((Token)val_peek(0).obj).getLexema())) {
										guardarAtClase(((Token)val_peek(2).obj).getLexema(), ((Token)val_peek(0).obj).getLexema());
										System.out.println("Cargue la variable "+((Token)val_peek(0).obj).getLexema()+" de la clase "+((Token)val_peek(2).obj).getLexema());
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
break;
case 97:
//#line 299 "gramatica.y"
{System.out.println("Paso de cte a factor");}
break;
case 98:
//#line 303 "gramatica.y"
{System.out.println("Leo una constante INT");}
break;
case 99:
//#line 304 "gramatica.y"
{System.out.println("Leo una constante negada");
		  actualizarTablaNegativo(((Token)val_peek(0).obj).getLexema());}
break;
case 100:
//#line 306 "gramatica.y"
{System.out.println("Leo una constante FLOAT");}
break;
case 101:
//#line 307 "gramatica.y"
{System.out.println("Leo una float negada");
    		actualizarTablaNegativo(((Token)val_peek(0).obj).getLexema());}
break;
//#line 1621 "Parser.java"
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
