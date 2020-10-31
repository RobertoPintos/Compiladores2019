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
   30,   38,   37,   32,   32,   32,   32,   35,   35,   35,
   35,   35,   35,   23,   40,   23,   42,   39,   39,   39,
   39,   41,   41,   41,   41,   25,   25,   25,   25,   25,
   34,   34,   34,   43,   43,   43,   44,   44,   44,   45,
   45,   45,   45,
};
final static short yylen[] = {                            2,
    1,    2,    1,    1,    1,    2,    3,    1,    3,    1,
    1,    0,    4,    0,    6,    3,    2,    1,    1,    1,
    2,    0,    7,    3,    1,    1,    1,    1,    1,    4,
    4,    4,    4,    2,    1,    1,    1,    1,    1,    1,
    1,    6,    1,    6,    6,    5,    6,    6,    5,    1,
    5,    5,    5,    0,    0,    8,    1,    6,    0,    4,
    1,    0,    3,    1,    3,    3,    3,    1,    1,    1,
    1,    1,    1,    2,    0,    5,    0,    6,    4,    4,
    3,    1,    3,    3,    3,    4,    6,    3,    4,    3,
    3,    3,    1,    3,    3,    1,    1,    3,    1,    1,
    2,    1,    2,
};
final static short yydefred[] = {                         0,
    0,   27,   26,    0,    0,   25,    0,    1,    0,    4,
    0,    0,    8,    0,    0,    0,    0,    0,    0,   35,
   36,   37,   38,   39,   40,   41,   43,   50,   57,    0,
    0,    0,    2,    6,   11,    0,   10,    0,    0,    0,
  100,    0,  102,    0,    0,    0,    0,   96,   99,   62,
    0,    0,    0,   61,    0,    0,    0,    0,    0,   34,
   74,    0,    0,    0,    0,    0,    7,    0,    0,    0,
    0,    0,    0,    0,  101,  103,   88,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   82,   81,    0,    0,
    0,    0,    0,   31,    0,   32,   33,   30,    0,   13,
    0,    9,   89,    0,   98,   86,    0,    0,    0,    0,
    0,    0,   94,   95,    0,   77,   80,    0,    0,   79,
   70,   71,   72,   73,   68,   69,    0,    0,    0,    0,
    0,    0,    0,   28,   29,    0,   18,   19,   20,    0,
    0,    0,    0,   46,    0,    0,    0,    0,   85,    0,
   83,    0,   51,   52,   53,   49,    0,   55,   76,   16,
   17,    0,   21,   15,   44,   45,   87,   47,   48,   42,
   78,    0,    0,   64,   58,    0,    0,    0,    0,    0,
    0,   67,    0,   65,   56,   22,    0,    0,   23,    0,
   24,
};
final static short yydgoto[] = {                          7,
    8,    9,   10,   11,   12,   36,   13,   37,  100,   65,
   66,  136,  137,  138,  139,  140,  189,  187,   19,   20,
   21,   22,   23,   24,   25,   26,   27,   28,   58,   52,
  176,  175,   29,   53,  127,   89,   54,   82,   30,   62,
   88,  148,   47,   48,   49,
};
final static short yysindex[] = {                      -211,
  134,    0,    0,  134, -242,    0,    0,    0, -179,    0,
 -213, -217,    0,  -20,   51,  -40,  -37, -196, -129,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -195,
 -108,    0,    0,    0,    0,    3,    0,  -34, -177,   48,
    0,  -34,    0, -150, -235,   24,   37,    0,    0,    0,
   -9,   50,   43,    0, -170, -161,   -9,   63,   62,    0,
    0, -156,  -28,  -57, -138, -141,    0, -217,   42,  106,
 -122,   66,   76,  -36,    0,    0,    0,  -34,  -34,  -34,
  -34,  -34,   61,   98,  134, -104,    0,    0,   85,  114,
  -13,  118,   -9,    0, -104,    0,    0,    0, -146,    0,
  -91,    0,    0,  136,    0,    0,  138,  123,  -34,   -6,
   37,   37,    0,    0,   43,    0,    0,  -87,  -76,    0,
    0,    0,    0,    0,    0,    0,  -34,  127,  131,  -50,
 -100,  150,  -70,    0,    0, -254,    0,    0,    0, -215,
 -138,  139,  141,    0,  115,  143,  -46, -104,    0,  -20,
    0,   43,    0,    0,    0,    0,  -64,    0,    0,    0,
    0,  -53,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   98,  134,    0,    0,  -62,  169,  110,  122,  -64,
  171,    0,  -20,    0,    0,    0,  -56,  134,    0,  132,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  214,    0,
    1,    0,    0,    0,    0,    0,    0,  175,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -27,
    0, -231,    0,    0,    0,    0,    0,    0,    0,  -35,
    0,    0,    0,    0,    0,    0,  -11,    0,    0,    0,
    0,    0,   94,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -152,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   11,   33,    0,    0,   71,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -116,
    0,   81,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -133,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
    0,  209,  217,  100,    0,    0,    0,  194,  124,    0,
    0,    0,  128,    0,    0,    0,    0,    0,   15,  282,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -18,
    0,   83,    0,   -1,    0,    0,    0,    0,    0,    0,
  -66,    0,   93,   95,    0,
};
final static int YYTABLESIZE=472;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         51,
    5,   98,   56,  110,   45,   97,   97,   97,  156,   97,
   45,   97,  170,   46,   32,  160,  117,   39,   31,  120,
  134,  135,   75,   97,   97,   39,   97,  130,  133,   93,
   96,   93,   83,   93,  147,   45,   69,   12,   92,   35,
   72,    2,   76,    2,    1,    2,   68,   93,   93,   14,
   93,   91,    3,   91,    3,   91,    3,    4,    5,   57,
    5,   67,    5,    6,  162,    6,   78,    6,   79,   91,
   91,   61,   91,   92,  132,   92,    1,   92,   80,   70,
  115,  171,   77,   81,   78,   78,   79,   79,   90,    4,
   86,   92,   92,   71,   92,   45,   44,   91,  118,  119,
  103,  116,   93,   90,   90,   73,   74,  145,   78,   95,
   79,   63,   90,   90,   90,  107,  108,   90,   90,   90,
   94,   60,   66,   66,  106,  152,   14,   15,  134,  135,
   99,   66,   66,   66,  105,   16,   66,   66,   66,  101,
   59,   17,   18,   39,  125,  104,  126,   63,   15,   84,
   84,   84,   15,   59,  128,   59,   16,   78,  131,   79,
   16,   64,   17,   18,   85,  141,   17,   18,   14,   15,
  111,  112,  157,  167,  113,  114,  142,   16,  143,  150,
   15,  144,  149,   17,   18,  153,  178,  179,   16,  154,
  158,  172,   15,  151,   17,   18,  159,  165,   97,  166,
   16,  168,  190,  177,  173,  155,   17,   18,  181,  169,
  180,  186,  188,    3,   54,   50,   40,   41,   55,   34,
   97,   97,   40,   41,  109,   33,   97,   97,   97,   97,
   97,   97,   38,   97,   97,   97,   97,   43,   75,  163,
   38,   97,  129,   43,   93,   93,   50,   40,   41,  146,
   93,   93,   93,   93,   93,   93,    5,   93,   93,   93,
   93,  102,  185,  161,  164,   93,   91,   91,   43,    5,
    0,    0,   91,   91,   91,   91,   91,   91,    0,   91,
   91,   91,   91,    0,    0,    0,    0,   91,   92,   92,
    0,    0,    0,    0,   92,   92,   92,   92,   92,   92,
   60,   92,   92,   92,   92,   84,   15,   40,   41,   92,
    0,   42,   60,    0,   16,    0,   84,   15,   85,    0,
   17,   18,    0,    0,    0,   16,   63,   63,   43,   85,
    0,   17,   18,   87,    0,   63,   60,   60,    0,   63,
    0,   63,   63,    0,    0,   60,  121,  122,  123,   60,
    0,   60,   60,   14,   15,   59,   59,   59,   38,    0,
    0,  124,   16,    0,   87,   14,   15,   87,   17,   18,
   59,    0,    0,    0,   16,    0,   87,  183,   15,  182,
   17,   18,    0,    0,    0,    0,   16,   14,   15,   14,
   15,  184,   17,   18,    0,    0,   16,    0,   16,   60,
   60,  191,   17,   18,   17,   18,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   87,
    0,    0,    0,    0,    0,    0,    0,    0,  174,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   60,
   60,  174,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   60,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   59,   40,   40,   45,   41,   42,   43,   59,   45,
   45,   47,   59,   15,  257,  270,   83,   46,    4,   86,
  275,  276,  258,   59,   60,   46,   62,   41,   95,   41,
   59,   43,   51,   45,   41,   45,   38,  269,   57,  257,
   42,  257,  278,  257,  256,  257,   44,   59,   60,  281,
   62,   41,  268,   43,  268,   45,  268,  269,  274,  256,
  274,   59,  274,  279,  280,  279,   43,  279,   45,   59,
   60,  267,   62,   41,   93,   43,  256,   45,   42,  257,
   82,  148,   59,   47,   43,   43,   45,   45,  259,  269,
   41,   59,   60,   46,   62,   45,   46,  259,   84,   85,
   59,   41,   40,  256,  257,  256,  257,  109,   43,  266,
   45,   41,  265,  266,  267,   40,   41,  270,  271,  272,
   59,   41,  256,  257,   59,  127,  256,  257,  275,  276,
  269,  265,  266,  267,  257,  265,  270,  271,  272,  281,
  270,  271,  272,   46,   60,   40,   62,  256,  257,  266,
  267,  256,  257,   60,   41,   62,  265,   43,   41,   45,
  265,  270,  271,  272,  269,  257,  271,  272,  256,  257,
   78,   79,  273,   59,   80,   81,   41,  265,   41,  256,
  257,   59,  270,  271,  272,   59,  172,  173,  265,   59,
   41,  256,  257,  270,  271,  272,  267,   59,  256,   59,
  265,   59,  188,  257,  269,  256,  271,  272,   40,  256,
  273,   41,  269,    0,   40,  256,  257,  258,  256,   11,
  256,  257,  257,  258,  261,    9,  262,  263,  264,  265,
  266,  267,  261,  269,  270,  271,  272,  278,  266,  140,
  261,  277,  256,  278,  256,  257,  256,  257,  258,  256,
  262,  263,  264,  265,  266,  267,  256,  269,  270,  271,
  272,   68,  180,  136,  141,  277,  256,  257,  278,  269,
   -1,   -1,  262,  263,  264,  265,  266,  267,   -1,  269,
  270,  271,  272,   -1,   -1,   -1,   -1,  277,  256,  257,
   -1,   -1,   -1,   -1,  262,  263,  264,  265,  266,  267,
   19,  269,  270,  271,  272,  256,  257,  257,  258,  277,
   -1,  261,   31,   -1,  265,   -1,  256,  257,  269,   -1,
  271,  272,   -1,   -1,   -1,  265,  256,  257,  278,  269,
   -1,  271,  272,   52,   -1,  265,  256,  257,   -1,  269,
   -1,  271,  272,   -1,   -1,  265,  262,  263,  264,  269,
   -1,  271,  272,  256,  257,  262,  263,  264,  261,   -1,
   -1,  277,  265,   -1,   83,  256,  257,   86,  271,  272,
  277,   -1,   -1,   -1,  265,   -1,   95,  256,  257,  270,
  271,  272,   -1,   -1,   -1,   -1,  265,  256,  257,  256,
  257,  270,  271,  272,   -1,   -1,  265,   -1,  265,  118,
  119,  270,  271,  272,  271,  272,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  148,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  157,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  178,
  179,  180,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  190,
};
}
final static short YYFINAL=7;
final static short YYMAXTOKEN=281;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'",null,"'>'",null,null,null,null,null,null,null,null,null,null,null,null,
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
"condicion : error_condicion",
"$$7 :",
"error_condicion : error $$7 expresion",
"bloque_anidado_while : sentencia",
"bloque_anidado_while : BEGIN bloque_sentencias END",
"bloque_anidado_while : BEGIN bloque_sentencias error",
"bloque_anidado_while : error bloque_sentencias END",
"comparador : '<'",
"comparador : '>'",
"comparador : C_MAYORIGUAL",
"comparador : C_MENORIGUAL",
"comparador : C_DISTINTO",
"comparador : C_IGUAL",
"seleccion : if_condicion END_IF",
"$$8 :",
"seleccion : if_condicion $$8 ELSE bloque_anidado_if END_IF",
"$$9 :",
"if_condicion : IF '(' condicion ')' $$9 bloque_anidado_if",
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

//#line 322 "gramatica.y"


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
//#line 1247 "Parser.java"
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
case 62:
//#line 197 "gramatica.y"
{lexico.getLexico().addError("Error en la comparacion.", lexico.getLexico().getNroLinea());}
break;
case 64:
//#line 200 "gramatica.y"
{ completarTercetoFinalWHILE(); }
break;
case 65:
//#line 201 "gramatica.y"
{ completarTercetoFinalWHILE(); }
break;
case 66:
//#line 202 "gramatica.y"
{lexico.getLexico().addError("Falta el END en el bloque anidado", lexico.getLexico().getNroLinea());}
break;
case 67:
//#line 203 "gramatica.y"
{lexico.getLexico().addError("Falta el BEGIN en el bloque anidado", lexico.getLexico().getNroLinea());}
break;
case 74:
//#line 215 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una condicion IF");
																														   desapilarYCompletar();
																										   cmp1 = false; cmp2 = false; condStart = 0;}
break;
case 75:
//#line 218 "gramatica.y"
{completarTercetoFinalIF();}
break;
case 76:
//#line 218 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una condicion IF con ELSE");
          																																											   desapilarYCompletar();
          																																							condStart = 0; cmp1 = false; cmp2 = false;}
break;
case 77:
//#line 223 "gramatica.y"
{apilarTercetoIncompletoIF();}
break;
case 79:
//#line 224 "gramatica.y"
{lexico.getLexico().addError("Falta el '(' de la condicion ", lexico.getLexico().getNroLinea());}
break;
case 80:
//#line 225 "gramatica.y"
{lexico.getLexico().addError("Falta el ')' de la condicion ", lexico.getLexico().getNroLinea());}
break;
case 81:
//#line 226 "gramatica.y"
{lexico.getLexico().addError("Faltan los parentesis de la condicion", lexico.getLexico().getNroLinea());}
break;
case 84:
//#line 231 "gramatica.y"
{lexico.getLexico().addError("Falta el END en el bloque anidado", lexico.getLexico().getNroLinea());}
break;
case 85:
//#line 232 "gramatica.y"
{lexico.getLexico().addError("Falta el BEGIN en el bloque anidado", lexico.getLexico().getNroLinea());}
break;
case 86:
//#line 235 "gramatica.y"
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
case 87:
//#line 245 "gramatica.y"
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
case 88:
//#line 255 "gramatica.y"
{ System.out.println("Error, falta el ':=' de la asignacion"); lexico.getLexico().addError("Falta el ':=' de la asignacion ", lexico.getLexico().getNroLinea());}
break;
case 89:
//#line 256 "gramatica.y"
{ System.out.println("Error, falta el ID a la izquierda de la asignacion"); lexico.getLexico().addError("Falta el ID de la asignacion ", lexico.getLexico().getNroLinea());}
break;
case 90:
//#line 257 "gramatica.y"
{lexico.getLexico().addError("Falta el ';' que cierra la asignacion.", lexico.getLexico().getNroLinea());}
break;
case 91:
//#line 260 "gramatica.y"
{
											lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una suma");
									   		addTercetoExpresion(((Token)val_peek(1).obj).getLexema(), ((Token)val_peek(2).obj).getLexema(), ((Token)val_peek(0).obj).getLexema());
									   		resetAtClase();
									}
break;
case 92:
//#line 265 "gramatica.y"
{
	  										lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una resta");
	  								  		addTercetoExpresion(((Token)val_peek(1).obj).getLexema(), ((Token)val_peek(2).obj).getLexema(), ((Token)val_peek(0).obj).getLexema());
	  								  		resetAtClase();
	  								}
break;
case 93:
//#line 270 "gramatica.y"
{System.out.println("Paso de termino a expresion");
      												   cambiarTercetos();}
break;
case 94:
//#line 274 "gramatica.y"
{
										 lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una multiplicacion");
										 addTercetoTermino(((Token)val_peek(1).obj).getLexema(), ((Token)val_peek(2).obj).getLexema(), ((Token)val_peek(0).obj).getLexema());		
										 resetAtClase(); 
							 }
break;
case 95:
//#line 279 "gramatica.y"
{
								  		 lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una division");
									     addTercetoTermino(((Token)val_peek(1).obj).getLexema(), ((Token)val_peek(2).obj).getLexema(), ((Token)val_peek(0).obj).getLexema());
									     resetAtClase();
							 }
break;
case 96:
//#line 284 "gramatica.y"
{ System.out.println("Paso factor a termino");}
break;
case 97:
//#line 287 "gramatica.y"
{System.out.println("Cargo un identificador");}
break;
case 98:
//#line 288 "gramatica.y"
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
case 99:
//#line 306 "gramatica.y"
{System.out.println("Paso de cte a factor");}
break;
case 100:
//#line 310 "gramatica.y"
{System.out.println("Leo una constante INT");}
break;
case 101:
//#line 311 "gramatica.y"
{System.out.println("Leo una constante negada");
		  actualizarTablaNegativo(((Token)val_peek(0).obj).getLexema());}
break;
case 102:
//#line 313 "gramatica.y"
{System.out.println("Leo una constante FLOAT");}
break;
case 103:
//#line 314 "gramatica.y"
{System.out.println("Leo una float negada");
    		actualizarTablaNegativo(((Token)val_peek(0).obj).getLexema());}
break;
//#line 1781 "Parser.java"
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
