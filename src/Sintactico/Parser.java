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
//#line 21 "Parser.java"




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
    9,    8,    7,    7,   10,   10,   10,   11,   11,   11,
   11,   13,   14,   12,   12,    5,   16,    5,    3,    3,
    3,    3,   15,   15,   17,   18,   18,   18,   18,   18,
   23,   23,   24,   24,   24,   24,   24,   21,   21,   25,
   25,   25,   19,   19,   19,   19,   26,   26,   26,   29,
   29,   29,   29,   29,   29,   29,   20,   20,   30,   30,
   30,   30,   27,   27,   27,   27,   22,   22,   22,   22,
   28,   28,   28,   31,   31,   31,   32,   32,   32,   33,
   33,   33,   33,
};
final static short yylen[] = {                            2,
    1,    2,    1,    1,    1,    2,    3,    1,    3,    1,
    0,    2,    5,    7,    2,    1,    1,    2,    2,    1,
    1,    2,    2,    6,    6,    1,    0,    2,    4,    4,
    4,    4,    2,    1,    1,    1,    1,    1,    1,    1,
    6,    1,    6,    6,    5,    6,    6,    5,    1,    5,
    5,    5,    6,    5,    5,    5,    3,    2,    3,    1,
    1,    1,    1,    1,    1,    1,    2,    4,    5,    4,
    4,    3,    1,    3,    3,    3,    4,    3,    4,    3,
    3,    3,    1,    3,    3,    1,    1,    3,    1,    1,
    2,    1,    2,
};
final static short yydefred[] = {                         0,
    0,   27,    0,    0,   26,    0,    1,    0,    4,    0,
    0,    8,    0,    0,    0,    0,    0,    0,    0,   34,
   35,   36,   37,   38,   39,   40,   42,   49,    0,   28,
    0,    0,    2,    6,   11,    0,   10,    0,    0,    0,
   12,    0,   90,   63,   64,   65,   66,   92,    0,   60,
   61,   62,    0,    0,    0,    0,    0,   86,   89,    0,
    0,    0,    0,    0,    0,    0,   33,    0,   67,    0,
    0,    0,    0,    7,    0,    0,    0,    0,    0,    0,
    0,   91,   93,    0,    0,    0,   73,   72,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   78,
   30,    0,   31,   32,   29,    0,    0,    0,    0,   17,
    0,    0,    0,    9,   79,    0,    0,    0,    0,   88,
    0,   71,    0,    0,   70,    0,    0,   59,    0,   84,
   85,    0,    0,    0,    0,    0,    0,   77,   68,    0,
   22,    0,   23,   13,    0,    0,   15,    0,    0,   18,
   19,    0,    0,    0,   45,    0,    0,   69,   76,    0,
   74,   50,   51,   52,   48,   55,    0,   56,   54,    0,
    0,    0,   43,   44,   46,   47,   41,   53,    0,    0,
   14,    0,    0,    0,    0,
};
final static short yydgoto[] = {                          6,
    7,  143,    9,   10,   11,   36,   12,   18,   41,  108,
  109,  110,  111,  112,   19,   30,   87,   21,   22,   23,
   24,   25,   26,   27,   28,   54,   88,   55,   56,   29,
   57,   58,   59,
};
final static short yysindex[] = {                      -216,
 -147,    0, -147, -241,    0,    0,    0, -223,    0, -162,
 -203,    0,  -28,   -3,  -40,  -25,  -37,   86,  -88,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -138,    0,
  -71, -196,    0,    0,    0,   45,    0,  -10, -192,  -86,
    0,   21,    0,    0,    0,    0,    0,    0,   78,    0,
    0,    0, -214,  116,  -34,  -10,   66,    0,    0, -173,
 -169,   78,   55,  -10,   -4,   52,    0,  155,    0,  -42,
  -53,  -85, -143,    0, -203,   25,   76,  147,   90, -131,
  126,    0,    0,  143, -147,  155,    0,    0,  -10,  -10,
  -43,    2,  -10,  -10,   91,  -22,  -31, -139,   48,    0,
    0, -118,    0,    0,    0, -208,   -5, -112,  -69,    0,
  -26,  -26, -107,    0,    0,  123,  131,  119,  -12,    0,
  155,    0,  -60,  160,    0,   66,   66,    0,    2,    0,
    0,  136,  139,  -52,  155, -121,  155,    0,    0,  -55,
    0,  -49,    0,    0,  -64,  -48,    0, -162, -162,    0,
    0,  -85,  177,  180,    0,  208,  -47,    0,    0,  -28,
    0,    0,    0,    0,    0,    0,  155,    0,    0,  244,
  252,   28,    0,    0,    0,    0,    0,    0,  258,  265,
    0, -147, -147, -147, -147,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,  312,    0,    1,
    0,    0,    0,   88,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -11,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   16,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   96,    0,    0,    0,    0,    0,    0, -111,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   43,    0,
   44,   49,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   38,   60,    0,  106,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -91,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   50,   51,
};
final static short yygindex[] = {                         0,
    0,  153,  310,  -92,    0,    0,    0,   27,    0,  176,
  179,  225,    0,    0,   10,    0,  391,    0,    0,    0,
    0,    0,    0,    0,    0,   20,    6,   24,  283,    0,
  175,  203,    0,
};
final static int YYTABLESIZE=576;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         49,
    5,   53,   62,   39,   53,  105,  165,   53,   89,  136,
   90,  177,   31,  141,   61,   32,  103,   39,  134,   50,
   52,   51,   50,   52,   51,   50,   52,   51,  157,   87,
   87,   87,    1,   87,   53,   87,   63,   37,   89,    1,
   90,   65,   40,   82,   89,    3,   90,   87,   87,   87,
   87,    2,    3,   35,  100,  141,   83,    4,   83,    2,
   83,   76,    5,   83,   77,    4,   80,   89,   81,   90,
    5,  140,   72,  102,   83,   83,   83,   83,   81,   92,
   81,   97,   81,  115,   73,   95,  122,   99,   75,   96,
   89,  125,   90,  123,  124,   98,   81,   81,   81,   81,
   82,  114,   82,   74,   82,    2,  138,   93,   13,   14,
  101,    4,   94,  113,  129,  116,    5,   15,   82,   82,
   82,   82,   53,   16,   17,  120,  158,   68,   69,  119,
   53,  132,   11,  137,   84,   14,   58,   50,   52,   51,
  166,  168,  169,   15,   80,   80,   57,   85,  139,   16,
   17,  167,    8,   80,   80,   80,   86,  144,   80,   80,
   80,  152,   34,  153,   75,   75,  121,   13,   14,   78,
   79,  154,  178,   75,   75,   75,   15,  155,   75,   75,
   75,   66,   16,   17,   70,   14,  117,  118,   39,  106,
  107,  184,  185,   15,  162,   13,   14,  163,   71,   16,
   17,  170,  104,  164,   15,  145,  146,  171,  176,  159,
   16,   17,  128,   42,   43,  140,   42,   43,   38,   42,
   43,   44,   45,   46,   44,   45,   46,   44,   45,   46,
   60,  142,   38,  133,   48,  173,   47,   48,  174,   47,
   48,  135,   47,  156,   87,   87,   42,   43,  148,  149,
   87,   87,   87,   87,   87,   87,    5,   87,   87,   87,
   87,   87,    2,  126,  127,   87,  175,   48,    4,    5,
    5,   83,   83,    5,  142,    5,    5,   83,   83,   83,
   83,   83,   83,  179,   83,   83,   83,   83,   83,  150,
  151,  180,   83,   81,   81,  130,  131,  181,  182,   81,
   81,   81,   81,   81,   81,  183,   81,   81,   81,   81,
   81,    3,   16,   20,   81,   82,   82,   33,   21,   24,
   25,   82,   82,   82,   82,   82,   82,  172,   82,   82,
   82,   82,   82,  147,   42,   43,   82,   91,    0,   44,
   45,   46,   42,   43,   11,   11,   64,    0,   11,    0,
    0,   58,   58,    0,   47,   48,    0,    0,    0,    0,
   58,   57,   57,   48,   58,   11,   58,   58,   58,    0,
   57,   84,   14,    0,   57,    0,   57,   57,   57,    0,
   15,   84,   14,    0,   85,    0,   16,   17,    0,    0,
   15,   20,    0,   20,   85,    0,   16,   17,   13,   14,
    0,    0,    0,   38,    0,    0,    0,   15,    0,   67,
   84,   14,    0,   16,   17,  160,   14,    0,    0,   15,
    0,   67,    0,   85,   15,   16,   17,    0,    0,  161,
   16,   17,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   20,   20,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   67,   67,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   20,   20,   67,   67,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   45,   40,   46,   45,   59,   59,   45,   43,   41,
   45,   59,    3,  106,   40,  257,   59,   46,   41,   60,
   61,   62,   60,   61,   62,   60,   61,   62,   41,   41,
   42,   43,  256,   45,   45,   47,   17,   11,   43,  256,
   45,   18,   46,  258,   43,  269,   45,   59,   60,   61,
   62,  268,  269,  257,   59,  148,   41,  274,   43,  268,
   45,   38,  279,  278,  257,  274,   46,   43,   49,   45,
  279,  280,  269,   68,   59,   60,   61,   62,   41,   56,
   43,   62,   45,   59,  281,  259,   81,   64,   44,  259,
   43,   86,   45,   84,   85,   41,   59,   60,   61,   62,
   41,   75,   43,   59,   45,  268,   59,   42,  256,  257,
   59,  274,   47,  257,   91,   40,  279,  265,   59,   60,
   61,   62,   45,  271,  272,  257,  121,  266,  267,   40,
   45,   41,   45,  273,  256,  257,   41,   60,   61,   62,
  135,  136,  137,  265,  256,  257,   41,  269,  267,  271,
  272,  273,    0,  265,  266,  267,   41,  270,  270,  271,
  272,  269,   10,   41,  256,  257,   41,  256,  257,  256,
  257,   41,  167,  265,  266,  267,  265,   59,  270,  271,
  272,  270,  271,  272,  256,  257,   40,   41,   46,  275,
  276,  182,  183,  265,   59,  256,  257,   59,  270,  271,
  272,  257,  256,  256,  265,  275,  276,  257,  256,  270,
  271,  272,  256,  257,  258,  280,  257,  258,  261,  257,
  258,  262,  263,  264,  262,  263,  264,  262,  263,  264,
  256,  280,  261,  256,  278,   59,  277,  278,   59,  277,
  278,  273,  277,  256,  256,  257,  257,  258,  275,  276,
  262,  263,  264,  265,  266,  267,  256,  269,  270,  271,
  272,  273,  268,   89,   90,  277,   59,  278,  274,  269,
  270,  256,  257,  279,  280,  275,  276,  262,  263,  264,
  265,  266,  267,   40,  269,  270,  271,  272,  273,  111,
  112,   40,  277,  256,  257,   93,   94,  270,   41,  262,
  263,  264,  265,  266,  267,   41,  269,  270,  271,  272,
  273,    0,  270,  270,  277,  256,  257,    8,  270,  270,
  270,  262,  263,  264,  265,  266,  267,  152,  269,  270,
  271,  272,  273,  109,  257,  258,  277,   55,   -1,  262,
  263,  264,  257,  258,  257,  258,  261,   -1,  261,   -1,
   -1,  256,  257,   -1,  277,  278,   -1,   -1,   -1,   -1,
  265,  256,  257,  278,  269,  278,  271,  272,  273,   -1,
  265,  256,  257,   -1,  269,   -1,  271,  272,  273,   -1,
  265,  256,  257,   -1,  269,   -1,  271,  272,   -1,   -1,
  265,    1,   -1,    3,  269,   -1,  271,  272,  256,  257,
   -1,   -1,   -1,  261,   -1,   -1,   -1,  265,   -1,   19,
  256,  257,   -1,  271,  272,  256,  257,   -1,   -1,  265,
   -1,   31,   -1,  269,  265,  271,  272,   -1,   -1,  270,
  271,  272,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   84,   85,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  123,  124,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  182,  183,  184,  185,
};
}
final static short YYFINAL=6;
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
"$$1 :",
"variable : ID $$1",
"sentencia_de_clase : CLASS ID BEGIN declaraciones_de_clase END",
"sentencia_de_clase : CLASS ID EXTENDS ID BEGIN declaraciones_de_clase END",
"declaraciones_de_clase : declaracion_atributos declaracion_metodos",
"declaraciones_de_clase : declaracion_atributos",
"declaraciones_de_clase : declaracion_metodos",
"declaracion_atributos : atributos_pub declaracion_atributos",
"declaracion_atributos : atributos_priv declaracion_atributos",
"declaracion_atributos : atributos_pub",
"declaracion_atributos : atributos_priv",
"atributos_pub : PUBLIC sentencia_declarativa",
"atributos_priv : PRIVATE conj_sentencias_declarativas",
"declaracion_metodos : PUBLIC VOID ID '(' ')' bloque_sentencias",
"declaracion_metodos : PRIVATE VOID ID '(' ')' bloque_sentencias",
"tipo : FLOAT",
"$$2 :",
"tipo : INT $$2",
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

//#line 205 "gramatica.y"





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

public int yyparser(){
	return yyparse();
}

//#line 495 "Parser.java"
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
//#line 42 "gramatica.y"
{System.out.println("Finaliza el programa");}
break;
case 7:
//#line 51 "gramatica.y"
{System.out.println("Llegue a una declaracion valida");}
break;
case 11:
//#line 60 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego el identificador: "+ID);}
break;
case 12:
//#line 60 "gramatica.y"
{System.out.println("lei un ID");}
break;
case 26:
//#line 89 "gramatica.y"
{ System.out.println("Viene una variable tipo FLOAT");}
break;
case 27:
//#line 90 "gramatica.y"
{ System.out.println("Viene una variable tipo INT" );}
break;
case 28:
//#line 91 "gramatica.y"
{/*	| ID { System.out.println("Viene una variable de la clase: "+ID );} */}
break;
case 30:
//#line 96 "gramatica.y"
{lexico.getLexico().addError("Falta el BEGIN para el bloque de sentencias ejecutables.", lexico.getLexico().getNroLinea());}
break;
case 31:
//#line 97 "gramatica.y"
{lexico.getLexico().addError("Falta el END para el bloque de sentencias ejecutables.", lexico.getLexico().getNroLinea());}
break;
case 32:
//#line 98 "gramatica.y"
{lexico.getLexico().addError("Falta el ';' que cierra el bloque de sentencias ejecutables.", lexico.getLexico().getNroLinea());}
break;
case 34:
//#line 102 "gramatica.y"
{System.out.println("Se cargo una sentencia");}
break;
case 43:
//#line 119 "gramatica.y"
{lexico.getLexico().addError("Falta la clase que contiene el metodo", lexico.getLexico().getNroLinea());}
break;
case 44:
//#line 120 "gramatica.y"
{lexico.getLexico().addError("Falta definir el metodo de clase ", lexico.getLexico().getNroLinea());}
break;
case 45:
//#line 121 "gramatica.y"
{lexico.getLexico().addError("Falta un parentesis ", lexico.getLexico().getNroLinea());}
break;
case 46:
//#line 122 "gramatica.y"
{lexico.getLexico().addError("Falta un parentesis ", lexico.getLexico().getNroLinea());}
break;
case 47:
//#line 123 "gramatica.y"
{lexico.getLexico().addError("Falta el ';' ", lexico.getLexico().getNroLinea());}
break;
case 48:
//#line 126 "gramatica.y"
{lexico.getLexico().agregarEstructura( "En la linea "+lexico.getLexico().getNroLinea()+" se hizo un print");}
break;
case 50:
//#line 131 "gramatica.y"
{lexico.getLexico().addError("Falta el '('.", lexico.getLexico().getNroLinea());}
break;
case 51:
//#line 132 "gramatica.y"
{lexico.getLexico().addError("Falta el ')'.", lexico.getLexico().getNroLinea());}
break;
case 52:
//#line 133 "gramatica.y"
{lexico.getLexico().addError("Falta el ';' que cierra el print.", lexico.getLexico().getNroLinea());}
break;
case 53:
//#line 137 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se creo una iteracion");}
break;
case 54:
//#line 138 "gramatica.y"
{lexico.getLexico().addError("Falta algun parentesis en la iteracion", lexico.getLexico().getNroLinea());}
break;
case 55:
//#line 139 "gramatica.y"
{lexico.getLexico().addError("Falta algun parentesis en la iteracion", lexico.getLexico().getNroLinea());}
break;
case 56:
//#line 140 "gramatica.y"
{lexico.getLexico().addError("Falta la palabra DO en la iteracion", lexico.getLexico().getNroLinea());}
break;
case 58:
//#line 144 "gramatica.y"
{ System.out.println("Falta la primera expresion en la condicion"); lexico.getLexico().addError("Falta la primera expresion en la condicion", lexico.getLexico().getNroLinea());}
break;
case 59:
//#line 145 "gramatica.y"
{ System.out.println("Falta la segunda expresion en la condicion"); lexico.getLexico().addError("Falta la segunda expresion en la condicion", lexico.getLexico().getNroLinea());}
break;
case 67:
//#line 157 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una condicion IF");}
break;
case 68:
//#line 158 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una condicion IF con ELSE");}
break;
case 70:
//#line 162 "gramatica.y"
{lexico.getLexico().addError("Falta el '(' de la condicion ", lexico.getLexico().getNroLinea());}
break;
case 71:
//#line 163 "gramatica.y"
{lexico.getLexico().addError("Falta el ')' de la condicion ", lexico.getLexico().getNroLinea());}
break;
case 72:
//#line 164 "gramatica.y"
{lexico.getLexico().addError("Faltan los parentesis de la condicion", lexico.getLexico().getNroLinea());}
break;
case 75:
//#line 169 "gramatica.y"
{lexico.getLexico().addError("Falta el END en el bloque anidado", lexico.getLexico().getNroLinea());}
break;
case 76:
//#line 170 "gramatica.y"
{lexico.getLexico().addError("Falta el BEGIN en el bloque anidado", lexico.getLexico().getNroLinea());}
break;
case 77:
//#line 173 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una asignacion");System.out.println("Realizo la asignacion en la linea: "+lexico.getLexico().getNroLinea());}
break;
case 78:
//#line 174 "gramatica.y"
{ System.out.println("Error, falta el ':=' de la asignacion"); lexico.getLexico().addError("Falta el ':=' de la asignacion ", lexico.getLexico().getNroLinea());}
break;
case 79:
//#line 175 "gramatica.y"
{ System.out.println("Error, falta el ID a la izquierda de la asignacion"); lexico.getLexico().addError("Falta el ID de la asignacion ", lexico.getLexico().getNroLinea());}
break;
case 80:
//#line 176 "gramatica.y"
{lexico.getLexico().addError("Falta el ';' que cierra la asignacion.", lexico.getLexico().getNroLinea());}
break;
case 81:
//#line 179 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una suma");}
break;
case 82:
//#line 180 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una resta");}
break;
case 83:
//#line 181 "gramatica.y"
{System.out.println("Paso de termino a expresion");}
break;
case 84:
//#line 184 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una multiplicacion");}
break;
case 85:
//#line 185 "gramatica.y"
{lexico.getLexico().agregarEstructura("En la linea "+lexico.getLexico().getNroLinea()+" se agrego una division");}
break;
case 86:
//#line 186 "gramatica.y"
{ System.out.println("Paso factor a termino"); }
break;
case 87:
//#line 189 "gramatica.y"
{System.out.println("Cargo un identificador");}
break;
case 88:
//#line 190 "gramatica.y"
{System.out.println("Cargue la variable "+ID+" de la clase "+ID);}
break;
case 89:
//#line 191 "gramatica.y"
{System.out.println("Paso de cte a factor");}
break;
case 90:
//#line 195 "gramatica.y"
{System.out.println("Leo una constante INT");}
break;
case 91:
//#line 196 "gramatica.y"
{System.out.println("Leo una constante negada");}
break;
case 92:
//#line 197 "gramatica.y"
{System.out.println("Leo una constante FLOAT");}
break;
case 93:
//#line 198 "gramatica.y"
{System.out.println("Leo una float negada");}
break;
//#line 844 "Parser.java"
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
