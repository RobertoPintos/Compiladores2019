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
   14,   15,   17,    5,    5,    5,   16,   16,    3,    3,
    3,    3,   18,   18,   19,   20,   20,   20,   20,   20,
   25,   25,   26,   26,   26,   26,   26,   23,   23,   27,
   27,   27,   29,   21,   21,   21,   21,   33,   28,   32,
   32,   32,   32,   32,   32,   32,   22,   35,   22,   36,
   34,   34,   34,   34,   30,   30,   30,   30,   24,   24,
   24,   24,   31,   31,   31,   37,   37,   37,   38,   38,
   38,   39,   39,   39,   39,
};
final static short yylen[] = {                            2,
    1,    2,    1,    1,    1,    2,    3,    1,    3,    1,
    1,    0,    4,    0,    6,    3,    2,    1,    1,    1,
    2,    6,    3,    1,    1,    1,    1,    1,    4,    4,
    4,    4,    2,    1,    1,    1,    1,    1,    1,    1,
    6,    1,    6,    6,    5,    6,    6,    5,    1,    5,
    5,    5,    0,    7,    5,    5,    5,    0,    4,    1,
    1,    1,    1,    1,    1,    1,    2,    0,    5,    0,
    6,    4,    4,    3,    1,    3,    3,    3,    4,    3,
    4,    3,    3,    3,    1,    3,    3,    1,    1,    3,
    1,    1,    2,    1,    2,
};
final static short yydefred[] = {                         0,
    0,   26,   25,    0,    0,   24,    0,    1,    0,    4,
    0,    0,    8,    0,    0,    0,    0,    0,    0,   34,
   35,   36,   37,   38,   39,   40,   42,   49,    0,    0,
    0,    2,    6,   11,    0,   10,    0,    0,    0,   92,
    0,   94,    0,    0,    0,    0,   88,   91,    0,    0,
    0,    0,    0,    0,    0,    0,   33,   67,    0,    0,
    0,    0,    0,    7,    0,    0,    0,    0,    0,    0,
    0,   93,   95,   80,    0,    0,    0,    0,    0,    0,
    0,    0,   75,   74,    0,    0,    0,    0,    0,   30,
    0,   31,   32,   29,    0,   13,    0,    9,   81,    0,
   90,   79,    0,    0,    0,    0,    0,   86,   87,   70,
   73,    0,    0,   72,   63,   64,   65,   66,   60,   61,
   62,    0,    0,    0,    0,    0,    0,    0,    0,   27,
   28,    0,   18,   19,   20,    0,    0,    0,    0,   45,
    0,    0,    0,   78,    0,   76,    0,   50,   51,   52,
   48,   56,    0,   57,   55,   69,   16,   17,    0,   21,
   15,   43,   44,   46,   47,   41,   71,    0,    0,   54,
    0,    0,    0,   22,    0,   23,
};
final static short yydgoto[] = {                          7,
    8,    9,   10,   11,   12,   35,   13,   36,   96,   62,
   63,  132,  133,  134,  135,  136,  174,   19,   83,   21,
   22,   23,   24,   25,   26,   27,   28,   50,  153,   84,
   51,  122,   85,   29,   59,  143,   46,   47,   48,
};
final static short yysindex[] = {                      -197,
  -85,    0,    0,  -85, -227,    0,    0,    0, -223,    0,
 -226, -202,    0,  -29,  -31,  -40,  -34,  -38, -110,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -233, -107,
    0,    0,    0,    0,    7,    0,    4, -182,   32,    0,
    4,    0, -144, -222,   31,   10,    0,    0,    4,   73,
   80, -171, -167,    4,   57,   44,    0,    0, -142,  -30,
  -56, -135, -133,    0, -202,   52,   97, -115,   77,  111,
  119,    0,    0,    0,    4,    4,    4,    4,   78,   95,
  -85,  -90,    0,    0,  -52,  127,  -23,  -18,  -97,    0,
  -90,    0,    0,    0, -122,    0,  -80,    0,    0,  149,
    0,    0,  150,  133,  -22,   10,   10,    0,    0,    0,
    0,  -87,  -68,    0,    0,    0,    0,    0,    0,    0,
    0,    4,  134,  136,  -55,  -90,  -90,  -90,  -71,    0,
    0, -176,    0,    0,    0, -164, -135,  139,  140,    0,
  146,  -32,  -90,    0,  -29,    0,   80,    0,    0,    0,
    0,    0,  -67,    0,    0,    0,    0,    0,  -50,    0,
    0,    0,    0,    0,    0,    0,    0,  -90,  168,    0,
  175,  -60,  -85,    0,   98,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  221,    0,
    1,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -43,    0,
 -211,    0,    0,    0,    0,    0,    0,    0,  -21,    0,
    0,    0,    0,    0,    0,    2,    0,    0,    0,    0,
  -49,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -139,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   24,   46,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -44,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -127,    0,   68,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  226,  230,  117,    0,    0,    0,  189,  123,    0,
    0,    0,  131,    0,    0,    0,    0,   21,  327,    0,
    0,    0,    0,    0,    0,    0,    0,   19,    0,  -47,
   13,    0,    0,    0,    0,    0,   81,   96,    0,
};
final static int YYTABLESIZE=502;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         49,
    5,   54,   94,  151,   44,   53,   44,  119,  121,  120,
   58,   58,   58,   44,   43,   38,   38,  125,  142,   89,
   89,   89,  127,   89,   30,   89,  166,   45,   92,   31,
    2,  111,    1,   58,  114,   72,   55,   89,   89,   89,
   89,    3,   85,  129,   85,    4,   85,    5,   44,   66,
   65,   77,    6,   69,   34,   73,   78,   12,    1,    2,
   85,   85,   85,   85,   83,   64,   83,   79,   83,   14,
    3,    4,   88,   75,   67,   76,    5,   68,  152,  154,
  155,    6,   83,   83,   83,   83,   84,   86,   84,   74,
   84,   87,    2,  157,   75,  167,   76,   89,  130,  131,
  112,  113,   90,    3,   84,   84,   84,   84,   59,    5,
   99,   70,   71,   82,    6,  159,   82,   82,  110,   75,
  170,   76,   75,   91,   76,   82,   82,   82,   77,   77,
   82,   82,   82,   95,  147,  102,  100,   77,   77,   77,
   38,  101,   77,   77,   77,   14,   15,   97,   60,   15,
  103,  104,  130,  131,   16,  106,  107,   16,  105,   56,
   17,   18,   61,   17,   18,   80,   15,  123,   14,   15,
   14,   15,  108,  109,   16,  128,  137,   16,   81,   16,
   17,   18,  144,   17,   18,   17,   18,  145,   15,  138,
  139,  140,  148,  175,  149,  156,   16,  162,  163,   93,
  150,  146,   17,   18,  164,  168,  169,  171,  173,  115,
  116,  117,   58,   58,   58,  172,   39,   40,   39,   40,
    3,   52,   68,  165,  118,   39,   40,   58,   53,   41,
   37,   37,  124,  141,   89,   89,   33,   42,   32,   42,
   89,   89,   89,   89,   89,   89,   42,   89,   89,   89,
   89,   89,  160,   98,  126,   89,    5,   85,   85,  161,
   39,   40,  158,   85,   85,   85,   85,   85,   85,    5,
   85,   85,   85,   85,   85,    0,    0,    0,   85,   83,
   83,   42,    0,    0,    0,   83,   83,   83,   83,   83,
   83,    0,   83,   83,   83,   83,   83,    0,    0,    0,
   83,   84,   84,    0,    0,    0,    0,   84,   84,   84,
   84,   84,   84,    0,   84,   84,   84,   84,   84,    0,
    0,    0,   84,   59,   59,    0,    0,   20,   80,   15,
   20,    0,   59,   80,   15,    0,   59,   16,   59,   59,
   59,   81,   16,   17,   18,   57,   81,    0,   17,   18,
   14,   15,    0,   14,   15,   37,   57,    0,    0,   16,
    0,    0,   16,    0,    0,   17,   18,  176,   17,   18,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   20,   20,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   57,   57,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   20,
    0,   57,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,   59,   59,   45,   40,   45,   60,   61,   62,
   60,   61,   62,   45,   46,   46,   46,   41,   41,   41,
   42,   43,   41,   45,    4,   47,   59,   15,   59,  257,
  257,   79,  256,  267,   82,  258,   18,   59,   60,   61,
   62,  268,   41,   91,   43,  269,   45,  274,   45,   37,
   44,   42,  279,   41,  257,  278,   47,  269,  256,  257,
   59,   60,   61,   62,   41,   59,   43,   49,   45,  281,
  268,  269,   54,   43,  257,   45,  274,   46,  126,  127,
  128,  279,   59,   60,   61,   62,   41,  259,   43,   59,
   45,  259,  257,  270,   43,  143,   45,   41,  275,  276,
   80,   81,   59,  268,   59,   60,   61,   62,   41,  274,
   59,  256,  257,   41,  279,  280,  256,  257,   41,   43,
  168,   45,   43,  266,   45,  265,  266,  267,  256,  257,
  270,  271,  272,  269,  122,   59,   40,  265,  266,  267,
   46,  257,  270,  271,  272,  256,  257,  281,  256,  257,
   40,   41,  275,  276,  265,   75,   76,  265,   40,  270,
  271,  272,  270,  271,  272,  256,  257,   41,  256,  257,
  256,  257,   77,   78,  265,  273,  257,  265,  269,  265,
  271,  272,  270,  271,  272,  271,  272,  256,  257,   41,
   41,   59,   59,  173,   59,  267,  265,   59,   59,  256,
  256,  270,  271,  272,   59,  273,  257,   40,  269,  262,
  263,  264,  262,  263,  264,   41,  257,  258,  257,  258,
    0,  256,  266,  256,  277,  257,  258,  277,  273,  261,
  261,  261,  256,  256,  256,  257,   11,  278,    9,  278,
  262,  263,  264,  265,  266,  267,  278,  269,  270,  271,
  272,  273,  136,   65,  273,  277,  256,  256,  257,  137,
  257,  258,  132,  262,  263,  264,  265,  266,  267,  269,
  269,  270,  271,  272,  273,   -1,   -1,   -1,  277,  256,
  257,  278,   -1,   -1,   -1,  262,  263,  264,  265,  266,
  267,   -1,  269,  270,  271,  272,  273,   -1,   -1,   -1,
  277,  256,  257,   -1,   -1,   -1,   -1,  262,  263,  264,
  265,  266,  267,   -1,  269,  270,  271,  272,  273,   -1,
   -1,   -1,  277,  256,  257,   -1,   -1,    1,  256,  257,
    4,   -1,  265,  256,  257,   -1,  269,  265,  271,  272,
  273,  269,  265,  271,  272,   19,  269,   -1,  271,  272,
  256,  257,   -1,  256,  257,  261,   30,   -1,   -1,  265,
   -1,   -1,  265,   -1,   -1,  271,  272,  270,  271,  272,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   80,   81,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  112,  113,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  173,
   -1,  175,
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
"metodo_clase : visibilidad VOID ID '(' ')' bloque_anidado_metclase",
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
"$$3 :",
"iteracion : WHILE '(' condicion ')' $$3 DO bloque_anidado",
"iteracion : WHILE condicion ')' DO bloque_anidado",
"iteracion : WHILE '(' condicion DO bloque_anidado",
"iteracion : WHILE '(' condicion ')' bloque_anidado",
"$$4 :",
"condicion : expresion $$4 comparador expresion",
"comparador : '<'",
"comparador : '>'",
"comparador : '='",
"comparador : C_MAYORIGUAL",
"comparador : C_MENORIGUAL",
"comparador : C_DISTINTO",
"comparador : C_IGUAL",
"seleccion : if_condicion END_IF",
"$$5 :",
"seleccion : if_condicion $$5 ELSE bloque_anidado END_IF",
"$$6 :",
"if_condicion : IF '(' condicion ')' $$6 bloque_anidado",
"if_condicion : IF condicion ')' bloque_anidado",
"if_condicion : IF '(' condicion bloque_anidado",
"if_condicion : IF condicion bloque_anidado",
"bloque_anidado : sentencia",
"bloque_anidado : BEGIN bloque_sentencias END",
"bloque_anidado : BEGIN bloque_sentencias error",
"bloque_anidado : error bloque_sentencias END",
"asig : ID ASIGNACION expresion ';'",
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

//#line 282 "gramatica.y"


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
//#line 926 "Parser.java"
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
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se creó un metodo de clase"); 
																																				  System.out.println("Viene un metodo");
																												  addMetodoTS(((Token)val_peek(5).obj).getLexema(), ((Token)val_peek(3).obj).getLexema());}
break;
case 24:
//#line 116 "gramatica.y"
{ System.out.println("Viene una variable tipo FLOAT");}
break;
case 25:
//#line 117 "gramatica.y"
{ System.out.println("Viene una variable tipo INT" );}
break;
case 26:
//#line 118 "gramatica.y"
{ System.out.println("Viene una variable de la clase: "+ ((Token)val_peek(0).obj).getLexema());
   	 							set = true;
   	 							clase = ((Token)val_peek(0).obj).getLexema();
   	 							if (estaDeclarada(clase))
   	 								existeClase = true;
   	 							else
   	 								existeClase = false;}
break;
case 30:
//#line 132 "gramatica.y"
{lexico.getLexico().addError("Falta el BEGIN para el bloque de sentencias ejecutables.", lexico.getLexico().getNroLinea());}
break;
case 31:
//#line 133 "gramatica.y"
{lexico.getLexico().addError("Falta el END para el bloque de sentencias ejecutables.", lexico.getLexico().getNroLinea());}
break;
case 32:
//#line 134 "gramatica.y"
{lexico.getLexico().addError("Falta el ';' que cierra el bloque de sentencias ejecutables.", lexico.getLexico().getNroLinea());}
break;
case 34:
//#line 138 "gramatica.y"
{System.out.println("Se cargo una sentencia");}
break;
case 41:
//#line 151 "gramatica.y"
{ if (esClase(((Token)val_peek(5).obj).getLexema()))
														if (esMetodo(((Token)val_peek(3).obj).getLexema())){
															
														} else
															assembler.getConversorAssembler().addErrorCI("Se trato de invocar a un metodo inexistente", lexico.getLexico().getNroLinea());
												  else
												  		assembler.getConversorAssembler().addErrorCI("No existe la clase asociada al metodo", lexico.getLexico().getNroLinea());			
												}
break;
case 43:
//#line 162 "gramatica.y"
{lexico.getLexico().addError("Falta la clase que contiene el metodo", lexico.getLexico().getNroLinea());}
break;
case 44:
//#line 163 "gramatica.y"
{lexico.getLexico().addError("Falta definir el metodo de clase ", lexico.getLexico().getNroLinea());}
break;
case 45:
//#line 164 "gramatica.y"
{lexico.getLexico().addError("Falta un parentesis ", lexico.getLexico().getNroLinea());}
break;
case 46:
//#line 165 "gramatica.y"
{lexico.getLexico().addError("Falta un parentesis ", lexico.getLexico().getNroLinea());}
break;
case 47:
//#line 166 "gramatica.y"
{lexico.getLexico().addError("Falta el ';' ", lexico.getLexico().getNroLinea());}
break;
case 48:
//#line 169 "gramatica.y"
{lexico.getLexico().agregarEstructura( "En la linea "+lexico.getLexico().getNroLinea()+" se hizo un print");
																	addTercetoPrint(((Token)val_peek(4).obj).getLexema(), ((Token)val_peek(2).obj).getLexema());}
break;
case 50:
//#line 175 "gramatica.y"
{lexico.getLexico().addError("Falta el '('.", lexico.getLexico().getNroLinea());}
break;
case 51:
//#line 176 "gramatica.y"
{lexico.getLexico().addError("Falta el ')'.", lexico.getLexico().getNroLinea());}
break;
case 52:
//#line 177 "gramatica.y"
{lexico.getLexico().addError("Falta el ';' que cierra el print.", lexico.getLexico().getNroLinea());}
break;
case 53:
//#line 181 "gramatica.y"
{apilarTercetoIncompletoWHILE();}
break;
case 54:
//#line 181 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se creo una iteracion");
																																										   apilarTercetoIncompletoWHILE();
																																								cmp1 = false; cmp2 = false; condStart = 0;}
break;
case 55:
//#line 184 "gramatica.y"
{lexico.getLexico().addError("Falta algun parentesis en la iteracion", lexico.getLexico().getNroLinea());}
break;
case 56:
//#line 185 "gramatica.y"
{lexico.getLexico().addError("Falta algun parentesis en la iteracion", lexico.getLexico().getNroLinea());}
break;
case 57:
//#line 186 "gramatica.y"
{lexico.getLexico().addError("Falta la palabra DO en la iteracion", lexico.getLexico().getNroLinea());}
break;
case 58:
//#line 189 "gramatica.y"
{if (genCodigo.getTercetosController().getTercetoExp() != null)
							 cmp1 = true;}
break;
case 59:
//#line 190 "gramatica.y"
{ if (genCodigo.getTercetosController().getTercetoExp() != null)
							 																						cmp2 = true;
																												addTercetoCondicion(((Token)val_peek(3).obj).getLexema(), ((Token)val_peek(1).obj).getLexema(), ((Token)val_peek(0).obj).getLexema());}
break;
case 67:
//#line 206 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una condicion IF");
																														   completarTercetoFinalIF();
																										   cmp1 = false; cmp2 = false; condStart = 0;}
break;
case 68:
//#line 209 "gramatica.y"
{apilarTercetoIncompletoIF();}
break;
case 69:
//#line 209 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una condicion IF con ELSE");
          																																											   completarTercetoFinalIF();
          																																											  cmp1 = false; cmp2 = false;}
break;
case 70:
//#line 214 "gramatica.y"
{apilarTercetoIncompletoIF();}
break;
case 72:
//#line 215 "gramatica.y"
{lexico.getLexico().addError("Falta el '(' de la condicion ", lexico.getLexico().getNroLinea());}
break;
case 73:
//#line 216 "gramatica.y"
{lexico.getLexico().addError("Falta el ')' de la condicion ", lexico.getLexico().getNroLinea());}
break;
case 74:
//#line 217 "gramatica.y"
{lexico.getLexico().addError("Faltan los parentesis de la condicion", lexico.getLexico().getNroLinea());}
break;
case 77:
//#line 222 "gramatica.y"
{lexico.getLexico().addError("Falta el END en el bloque anidado", lexico.getLexico().getNroLinea());}
break;
case 78:
//#line 223 "gramatica.y"
{lexico.getLexico().addError("Falta el BEGIN en el bloque anidado", lexico.getLexico().getNroLinea());}
break;
case 79:
//#line 226 "gramatica.y"
{ if (esMetodo(((Token)val_peek(3).obj).getLexema())) {
											lexico.getLexico().addError("No se puede hacer una asignacion a un metodo de clase.", lexico.getLexico().getNroLinea());
										} else {
											lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una asignacion");
											System.out.println("Realizo la asignacion en la linea: "+lexico.getLexico().getNroLinea());
											addTercetoAsignacion(((Token)val_peek(3).obj).getLexema(), ((Token)val_peek(1).obj).getLexema());
										} genCodigo.getTercetosController().setTercetoExpNull();
										  genCodigo.getTercetosController().setTercetoTermNull(); }
break;
case 80:
//#line 234 "gramatica.y"
{ System.out.println("Error, falta el ':=' de la asignacion"); lexico.getLexico().addError("Falta el ':=' de la asignacion ", lexico.getLexico().getNroLinea());}
break;
case 81:
//#line 235 "gramatica.y"
{ System.out.println("Error, falta el ID a la izquierda de la asignacion"); lexico.getLexico().addError("Falta el ID de la asignacion ", lexico.getLexico().getNroLinea());}
break;
case 82:
//#line 236 "gramatica.y"
{lexico.getLexico().addError("Falta el ';' que cierra la asignacion.", lexico.getLexico().getNroLinea());}
break;
case 83:
//#line 239 "gramatica.y"
{
											lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una suma");
									   		addTercetoExpresion(((Token)val_peek(1).obj).getLexema(), ((Token)val_peek(2).obj).getLexema(), ((Token)val_peek(0).obj).getLexema());
									}
break;
case 84:
//#line 243 "gramatica.y"
{
	  										lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una resta");
	  								  		addTercetoExpresion(((Token)val_peek(1).obj).getLexema(), ((Token)val_peek(2).obj).getLexema(), ((Token)val_peek(0).obj).getLexema());
	  								}
break;
case 85:
//#line 247 "gramatica.y"
{System.out.println("Paso de termino a expresion");
      												   cambiarTercetos();}
break;
case 86:
//#line 251 "gramatica.y"
{
										 lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una multiplicacion");
										 addTercetoTermino(((Token)val_peek(1).obj).getLexema(), ((Token)val_peek(2).obj).getLexema(), ((Token)val_peek(0).obj).getLexema());		 
							 }
break;
case 87:
//#line 255 "gramatica.y"
{
								   lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una division");
								   addTercetoTermino(((Token)val_peek(1).obj).getLexema(), ((Token)val_peek(2).obj).getLexema(), ((Token)val_peek(0).obj).getLexema());
							 }
break;
case 88:
//#line 259 "gramatica.y"
{ System.out.println("Paso factor a termino");}
break;
case 89:
//#line 262 "gramatica.y"
{System.out.println("Cargo un identificador");}
break;
case 90:
//#line 263 "gramatica.y"
{if (estaDeclarada(((Token)val_peek(0).obj).getLexema()))
						if (estaDeclarada(((Token)val_peek(2).obj).getLexema()))
							System.out.println("Cargue la variable "+((Token)val_peek(0).obj).getLexema()+" de la clase "+((Token)val_peek(2).obj).getLexema());}
break;
case 91:
//#line 266 "gramatica.y"
{System.out.println("Paso de cte a factor");}
break;
case 92:
//#line 270 "gramatica.y"
{System.out.println("Leo una constante INT");}
break;
case 93:
//#line 271 "gramatica.y"
{System.out.println("Leo una constante negada");
		  actualizarTablaNegativo(((Token)val_peek(0).obj).getLexema());}
break;
case 94:
//#line 273 "gramatica.y"
{System.out.println("Leo una constante FLOAT");}
break;
case 95:
//#line 274 "gramatica.y"
{System.out.println("Leo una float negada");
    		actualizarTablaNegativo(((Token)val_peek(0).obj).getLexema());}
break;
//#line 1394 "Parser.java"
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
