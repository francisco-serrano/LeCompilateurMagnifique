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
	package parser;
	import lexer.TablaSimbolos;
	import lexer.Terceto;
	
	import lexer.Item;
	import lexer.ItemString;
	import lexer.ItemTerceto;
	import lexer.Lexer;
	import lexer.TablaSimbolos;
	import java.util.Stack;
    import java.util.Vector;
	import java.util.ArrayList;
	import java.util.List;

	import com.google.common.collect.ArrayListMultimap;
	import com.google.common.collect.Multimap;
//#line 34 "Parser.java"




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
public final static short ASIGN=259;
public final static short ADD=260;
public final static short SUB=261;
public final static short MULT=262;
public final static short DIV=263;
public final static short DOT=264;
public final static short BEGIN=265;
public final static short END=266;
public final static short COLON=267;
public final static short COMMA=268;
public final static short UINT=269;
public final static short ULONG=270;
public final static short IF=271;
public final static short OPEN_PAR=272;
public final static short CLOSE_PAR=273;
public final static short THEN=274;
public final static short ELSE=275;
public final static short END_IF=276;
public final static short LEQ=277;
public final static short GEQ=278;
public final static short LT=279;
public final static short GT=280;
public final static short EQ=281;
public final static short NEQ=282;
public final static short OUT=283;
public final static short CADENA=284;
public final static short FUNCTION=285;
public final static short MOVE=286;
public final static short OPEN_BRACE=287;
public final static short CLOSE_BRACE=288;
public final static short RETURN=289;
public final static short WHILE=290;
public final static short DO=291;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    2,    2,    4,    4,    4,    5,    5,    5,    6,
    6,    8,    3,    3,    9,    3,    3,    7,    7,    7,
    1,    1,   12,   12,   12,   12,   12,   14,   14,   14,
   14,   14,   13,   13,   10,   10,   17,   17,   17,   17,
   17,   15,   15,   19,   18,   21,   22,   22,   22,   20,
   20,   20,   24,   24,   24,   24,   25,   25,   16,   26,
   27,   11,   11,   11,   28,   28,   28,   29,   29,   29,
   23,   23,   23,   23,   23,   23,   30,
};
final static short yylen[] = {                            2,
    1,    1,    1,    4,    3,    3,    3,    1,    2,    1,
    1,    0,    5,    3,    0,    6,    4,    8,    7,    3,
    2,    1,    1,    1,    1,    1,    1,    5,    4,    4,
    4,    5,    4,    3,    2,    1,    1,    1,    1,    1,
    1,    4,    2,    1,    4,    1,    5,    4,    4,    1,
    3,    3,    1,    1,    1,    1,    2,    1,    4,    1,
    1,    3,    3,    1,    3,    3,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    3,
};
final static short yydefred[] = {                         0,
    0,   10,   11,    0,    0,    0,    0,   60,    0,    0,
   27,    2,    3,    0,    0,   22,   23,   24,   25,   26,
    0,    0,    0,    0,   69,    0,    0,    0,   46,    0,
   67,   70,    0,    0,    0,    0,   21,    9,    0,    0,
    0,    0,    0,   44,   43,    0,   61,    0,    0,    0,
    0,    0,    0,   71,   72,   73,   74,   75,   76,    0,
    0,    0,    0,    0,    0,    0,    0,   14,    0,    0,
    7,    6,   12,    0,    0,    0,   53,   56,   54,   55,
    0,   50,    0,   33,   77,    0,    0,    0,    0,   45,
   65,   66,   30,    0,    0,   31,    0,   37,    0,   38,
   39,   40,   41,   36,   17,    4,    0,   15,   58,    0,
   42,   59,    0,   48,   28,   32,    0,   20,    0,   35,
   13,    0,   52,   51,   57,   47,    0,    0,   16,    0,
    0,    0,    0,   19,    0,   18,
};
final static short yydgoto[] = {                          9,
   10,   11,   12,   13,   14,   15,   68,  107,  122,   99,
   27,   16,   77,   78,   79,   80,  104,   21,   46,   81,
   28,   29,   60,   82,  110,   22,   48,   30,   31,   32,
};
final static short yysindex[] = {                      -235,
 -238,    0,    0, -172, -264, -239, -259,    0,    0, -235,
    0,    0,    0, -140, -262,    0,    0,    0,    0,    0,
 -247, -172, -176, -216,    0, -176, -166, -205,    0, -165,
    0,    0, -174, -198, -210, -158,    0,    0, -151, -156,
 -159, -137, -197,    0,    0, -224,    0, -167, -171, -127,
 -166, -176, -176,    0,    0,    0,    0,    0,    0, -176,
 -224, -176, -176, -206, -169, -117, -211,    0, -210, -116,
    0,    0,    0, -107, -238, -203,    0,    0,    0,    0,
 -125,    0, -224,    0,    0, -176, -165, -165, -138,    0,
    0,    0,    0, -111, -110,    0, -115,    0, -226,    0,
    0,    0,    0,    0,    0,    0, -210,    0,    0, -241,
    0,    0, -135,    0,    0,    0, -176,    0, -114,    0,
    0, -210,    0,    0,    0,    0, -124, -176,    0, -109,
 -121, -132, -105,    0, -128,    0,
};
final static short yyrindex[] = {                         0,
 -136,    0,    0,    0,    0,    0,    0,    0,    0,  161,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    1,    0,    0,    0,    0,    0,   37,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  145,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  180,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   73,  109,    0,    0,
    0,    0,    0,  170,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -230,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,    0,  -56,    0,   13,  -69,    0,    0,    0,
  -21,  152,    3,    4,    7,    9,   64,    0,    0,  -51,
    0,  142,  114,  -70,    0,    0,    0,   89,   81,    0,
};
final static int YYTABLESIZE=470;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        105,
   68,   49,   17,   18,   51,  109,   19,   33,   20,   90,
   98,   65,   17,   18,  123,   75,   19,   35,   20,   34,
   23,    1,   42,   43,  124,   36,   41,   44,   45,    4,
    1,  112,   75,    2,    3,    4,   64,  121,   89,  125,
   76,    5,   98,   49,    4,    1,    4,    5,    8,    6,
    7,   70,  129,   75,    8,   50,    5,   93,    5,    4,
   49,  118,  119,    8,  113,    8,   94,    4,   61,  100,
  101,    5,   62,  102,   66,  103,   67,   97,    8,    5,
   24,   25,   24,   25,   24,   25,    8,   74,   52,   53,
   52,   53,   84,   52,   53,  127,   62,   63,   69,   26,
   71,  100,  101,   95,   72,  102,  131,  103,   63,   64,
   54,   55,   56,   57,   58,   59,   38,    2,    3,   73,
    8,   52,   53,   83,   52,   53,   39,   40,    2,    3,
    8,    8,    8,    8,  114,   52,   53,  126,   52,   53,
   87,   88,   91,   92,   34,   85,   96,  106,  130,  108,
  111,  133,  115,  116,  132,  134,  117,  128,  135,  136,
    1,   37,  120,   47,   86,    0,    0,    0,    0,   29,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    5,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   68,   68,    0,    0,
   68,   68,   68,   68,   68,    0,   68,    0,    0,   68,
   68,   68,    0,   68,   68,   68,   68,   68,   68,   68,
   68,   68,   68,   68,    0,   68,   68,    0,   68,   68,
   68,   68,   64,   64,    0,    0,   64,   64,    0,    0,
   64,    0,   64,    0,    0,   64,   64,   64,    0,   64,
   64,   64,   64,   64,   64,   64,   64,   64,   64,   64,
    0,   64,   64,    0,   64,   64,   64,   64,   62,   62,
    0,    0,   62,   62,    0,    0,   62,    0,   62,    0,
    0,   62,   62,   62,    0,   62,   62,   62,   62,   62,
   62,   62,   62,   62,   62,   62,    0,   62,   62,    0,
   62,   62,   62,   62,   63,   63,    0,    0,   63,   63,
    0,    0,   63,    0,   63,    0,    0,   63,   63,   63,
    0,   63,   63,   63,   63,   63,   63,   63,   63,   63,
   63,   63,    0,   63,   63,    0,   63,   63,   63,   63,
   34,   34,    0,    0,    0,    0,    0,    0,    0,    0,
   34,    0,    0,   34,   34,   34,    0,    0,    0,   34,
   34,    0,    0,    0,    0,   29,   29,   34,    0,   34,
   34,    0,   34,   34,   34,   29,    5,    0,   29,   29,
   29,    0,    0,    0,   29,   29,    0,    0,    5,    5,
    5,    0,   29,    0,   29,   29,    0,   29,   29,   29,
    0,    0,    5,    0,    5,    5,    0,    5,    5,    5,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         69,
    0,   23,    0,    0,   26,   76,    0,  272,    0,   61,
   67,   33,   10,   10,  256,  257,   10,  257,   10,  284,
  259,  257,  285,  286,  266,  285,   14,  275,  276,  271,
  257,   83,  257,  269,  270,  271,    0,  107,   60,  110,
  265,  283,   99,  274,  271,  257,  271,  283,  290,  285,
  286,   39,  122,  257,  290,  272,  283,  264,  283,  271,
  291,  288,  289,  290,   86,  290,  273,  271,  274,   67,
   67,  283,    0,   67,  273,   67,  287,  289,  290,  283,
  257,  258,  257,  258,  257,  258,  290,  285,  260,  261,
  260,  261,  264,  260,  261,  117,  262,  263,  257,  272,
  257,   99,   99,  273,  264,   99,  128,   99,    0,  284,
  277,  278,  279,  280,  281,  282,  257,  269,  270,  257,
  257,  260,  261,  291,  260,  261,  267,  268,  269,  270,
  267,  268,  269,  270,  273,  260,  261,  273,  260,  261,
   52,   53,   62,   63,    0,  273,  264,  264,  273,  257,
  276,  273,  264,  264,  264,  288,  272,  272,  264,  288,
    0,   10,   99,   22,   51,   -1,   -1,   -1,   -1,    0,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,    0,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,   -1,   -1,
  260,  261,  262,  263,  264,   -1,  266,   -1,   -1,  269,
  270,  271,   -1,  273,  274,  275,  276,  277,  278,  279,
  280,  281,  282,  283,   -1,  285,  286,   -1,  288,  289,
  290,  291,  256,  257,   -1,   -1,  260,  261,   -1,   -1,
  264,   -1,  266,   -1,   -1,  269,  270,  271,   -1,  273,
  274,  275,  276,  277,  278,  279,  280,  281,  282,  283,
   -1,  285,  286,   -1,  288,  289,  290,  291,  256,  257,
   -1,   -1,  260,  261,   -1,   -1,  264,   -1,  266,   -1,
   -1,  269,  270,  271,   -1,  273,  274,  275,  276,  277,
  278,  279,  280,  281,  282,  283,   -1,  285,  286,   -1,
  288,  289,  290,  291,  256,  257,   -1,   -1,  260,  261,
   -1,   -1,  264,   -1,  266,   -1,   -1,  269,  270,  271,
   -1,  273,  274,  275,  276,  277,  278,  279,  280,  281,
  282,  283,   -1,  285,  286,   -1,  288,  289,  290,  291,
  256,  257,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  266,   -1,   -1,  269,  270,  271,   -1,   -1,   -1,  275,
  276,   -1,   -1,   -1,   -1,  256,  257,  283,   -1,  285,
  286,   -1,  288,  289,  290,  266,  257,   -1,  269,  270,
  271,   -1,   -1,   -1,  275,  276,   -1,   -1,  269,  270,
  271,   -1,  283,   -1,  285,  286,   -1,  288,  289,  290,
   -1,   -1,  283,   -1,  285,  286,   -1,  288,  289,  290,
};
}
final static short YYFINAL=9;
final static short YYMAXTOKEN=291;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
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
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"ID","CTE","ASIGN","ADD","SUB","MULT","DIV","DOT","BEGIN","END",
"COLON","COMMA","UINT","ULONG","IF","OPEN_PAR","CLOSE_PAR","THEN","ELSE",
"END_IF","LEQ","GEQ","LT","GT","EQ","NEQ","OUT","CADENA","FUNCTION","MOVE",
"OPEN_BRACE","CLOSE_BRACE","RETURN","WHILE","DO",
};
final static String yyrule[] = {
"$accept : programa",
"programa : sentencias",
"declaracion : declaracion_funcion",
"declaracion : declaracion_variables",
"declaracion_variables : lista_var COLON tipo DOT",
"declaracion_variables : lista_var COLON tipo",
"declaracion_variables : lista_var tipo DOT",
"lista_var : lista_var COMMA ID",
"lista_var : ID",
"lista_var : lista_var ID",
"tipo : UINT",
"tipo : ULONG",
"$$1 :",
"declaracion_funcion : tipo FUNCTION ID $$1 cuerpo_funcion",
"declaracion_funcion : FUNCTION ID cuerpo_funcion",
"$$2 :",
"declaracion_funcion : tipo MOVE FUNCTION ID $$2 cuerpo_funcion",
"declaracion_funcion : MOVE FUNCTION ID cuerpo_funcion",
"cuerpo_funcion : OPEN_BRACE bloque_funcion RETURN OPEN_PAR expresion CLOSE_PAR DOT CLOSE_BRACE",
"cuerpo_funcion : OPEN_BRACE RETURN OPEN_PAR expresion CLOSE_PAR DOT CLOSE_BRACE",
"cuerpo_funcion : OPEN_BRACE bloque_funcion CLOSE_BRACE",
"sentencias : sentencias sentencia",
"sentencias : sentencia",
"sentencia : asignacion",
"sentencia : print",
"sentencia : seleccion",
"sentencia : iteracion",
"sentencia : declaracion",
"print : OUT OPEN_PAR CADENA CLOSE_PAR DOT",
"print : OUT OPEN_PAR CADENA CLOSE_PAR",
"print : OUT OPEN_PAR CADENA DOT",
"print : OUT CADENA CLOSE_PAR DOT",
"print : OUT OPEN_PAR expresion CLOSE_PAR DOT",
"asignacion : ID ASIGN expresion DOT",
"asignacion : ID ASIGN expresion",
"bloque_funcion : bloque_funcion bloque",
"bloque_funcion : bloque",
"bloque : declaracion_variables",
"bloque : asignacion",
"bloque : print",
"bloque : seleccion",
"bloque : iteracion",
"seleccion : seleccion_simple else bloque_sentencias END_IF",
"seleccion : seleccion_simple END_IF",
"else : ELSE",
"seleccion_simple : IF condicion_if THEN bloque_sentencias",
"condicion_if : condicion",
"condicion : OPEN_PAR expresion comparador expresion CLOSE_PAR",
"condicion : expresion comparador expresion CLOSE_PAR",
"condicion : OPEN_PAR expresion comparador expresion",
"bloque_sentencias : bloque_simple",
"bloque_sentencias : BEGIN bloque_compuesto END",
"bloque_sentencias : BEGIN bloque_compuesto error",
"bloque_simple : asignacion",
"bloque_simple : seleccion",
"bloque_simple : iteracion",
"bloque_simple : print",
"bloque_compuesto : bloque_compuesto bloque_simple",
"bloque_compuesto : bloque_simple",
"iteracion : while condicion_while DO bloque_sentencias",
"while : WHILE",
"condicion_while : condicion",
"expresion : expresion ADD termino",
"expresion : expresion SUB termino",
"expresion : termino",
"termino : termino MULT factor",
"termino : termino DIV factor",
"termino : factor",
"factor : ID",
"factor : CTE",
"factor : invocacion_funcion",
"comparador : LEQ",
"comparador : GEQ",
"comparador : LT",
"comparador : GT",
"comparador : EQ",
"comparador : NEQ",
"invocacion_funcion : ID OPEN_PAR CLOSE_PAR",
};

//#line 368 "gramatica.y"

void yyerror(String error) {
	bufferErrores.add(error);
}

int yylex() {
	int val = lexer.yylex();
	yylval = new ParserVal();
	yylval.ival = lexer.getCurrentLine();
	yylval.sval = lexer.getCurrentLexema();

	return val;
}

public void setLexico(Lexer lexer) {
	this.lexer = lexer;
}

public void setTablaSimbolos(TablaSimbolos ts) {
	this.tablaSimbolos = ts;
}

public List<Terceto> getTercetos(){
    String posta = "";
	for (int i = 0; i < tercetos.size(); i++) {
        if (tercetos.get(i).getOperador() == "BF") {
            posta = parsearString(tercetos.get(i).getArg2().toItemString());
            if (tercetos.size() >= new Integer(posta).intValue()-1) {
                tercetos.get(new Integer(posta).intValue()-1).setDireccionSalto();
            }

        }
		if (tercetos.get(i).getOperador() == "BI") {
            posta = parsearString(tercetos.get(i).getArg1().toItemString());
            if (tercetos.size() >= new Integer(posta).intValue()-1) {
                tercetos.get(new Integer(posta).intValue()-1).setDireccionSalto();
            }
        } 
    }
	return tercetos;
}

private String parsearString(ItemString aux){
    String s = aux.getArg();
    String[] parts = s.split("\\[");
    String part2 = parts[1];
    String[] partes2 = part2.split("\\]");
    return partes2[0];
}

public List<String> getErrores() {
	return bufferErrores;
}

public Multimap<String, Terceto> getMapeoFuncion() {
	return mapeoFuncion;
}

Lexer lexer;

TablaSimbolos tablaSimbolos;

List<Terceto> tercetos = new ArrayList<>();
Stack<Integer> pila = new Stack<>();

List<String> bufferErrores = new ArrayList<>();

String uso;
boolean isMoveFunction = false;

List<String> auxVariables = new ArrayList<>();

CustomStack<String> ambitos = new CustomStack<>("main");

Multimap<String, Terceto> mapeoFuncion = ArrayListMultimap.create();

boolean isFunction = false;
//#line 507 "Parser.java"
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
public int yyparse()
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
case 4:
//#line 31 "gramatica.y"
{ 
													System.out.println("Declaración de Variables. Línea " + val_peek(2).ival); 
													uso = "variable";
													
													if (!tablaSimbolos.redefined(auxVariables, ambitos.toString()))
														tablaSimbolos.defineVar(auxVariables, val_peek(1).sval, uso, ambitos.toString());
													else 
														yyerror("\tLínea " + val_peek(2).ival + ". Re-Declaracion de variables.");
													
													
													auxVariables.clear();
												 }
break;
case 5:
//#line 43 "gramatica.y"
{ yyerror("\tLínea " + val_peek(1).ival + ". Declaración de variables incompleta. Falta DOT"); }
break;
case 6:
//#line 44 "gramatica.y"
{ yyerror("\tLínea " + val_peek(0).ival + ". Declaración de variables incompleta. Falta COLON"); }
break;
case 7:
//#line 47 "gramatica.y"
{ auxVariables.add(val_peek(0).sval.toLowerCase()); }
break;
case 8:
//#line 48 "gramatica.y"
{ auxVariables.add(val_peek(0).sval.toLowerCase()); }
break;
case 9:
//#line 49 "gramatica.y"
{ yyerror("\tLínea " + val_peek(0).ival + ". Declaración incompleta. Falta COMMA"); }
break;
case 12:
//#line 55 "gramatica.y"
{ ambitos.push(val_peek(0).sval); isFunction = true; }
break;
case 13:
//#line 55 "gramatica.y"
{ 
																					uso = "nombre_funcion";

																					if (!tablaSimbolos.functionDefined(val_peek(2).sval)){
																						System.out.println("Declaracion de funcion. Línea " + val_peek(3).ival);
																						auxVariables.clear();
																						auxVariables.add(val_peek(2).sval);
																						tablaSimbolos.defineVar(auxVariables, val_peek(4).sval, uso, ambitos.toString());
																					} else {
																						yyerror("\tLínea " + val_peek(3).ival + ". Redeclaracion de funcion.");
																					}

																					ambitos.pop();
																					isFunction = false;
																					auxVariables.clear();
																				 }
break;
case 14:
//#line 71 "gramatica.y"
{ yyerror("\tLínea " + val_peek(2).ival + ". Declaración de función incompleta. Falta tipo de retorno"); }
break;
case 15:
//#line 72 "gramatica.y"
{ ambitos.push(val_peek(0).sval); isMoveFunction = true; isFunction = true; }
break;
case 16:
//#line 72 "gramatica.y"
{ 	
																												uso = "nombre_funcion";

																												if (!tablaSimbolos.functionDefined(val_peek(2).sval)){
																													System.out.println("Declaracion de funcion. Línea " + val_peek(3).ival);
																													auxVariables.clear();
																													auxVariables.add(val_peek(2).sval);
																													tablaSimbolos.defineVar(auxVariables, val_peek(5).sval, uso, ambitos.toString());
																												} else {
																													yyerror("\tLínea " + val_peek(3).ival + ". Redeclaracion de funcion.");
																												}

																												ambitos.pop();
																												isMoveFunction = false;
																												isFunction = false;
																												auxVariables.clear();
																											}
break;
case 17:
//#line 89 "gramatica.y"
{ yyerror("\tLínea " + val_peek(3).ival + ". Declaración de función incompleta. Falta tipo de retorno"); }
break;
case 20:
//#line 94 "gramatica.y"
{ yyerror("\tLínea " + val_peek(2).ival + ". Declaración de función incompleta. Falta sentencia RETURN"); }
break;
case 28:
//#line 103 "gramatica.y"
{ 
											System.out.println("Sentencia OUT. Línea " + val_peek(4).ival); 
											Terceto t = new Terceto("PRINT", new ItemString(val_peek(2).sval), new ItemString("-"), null);
											
											if (isFunction)
									  			mapeoFuncion.put(ambitos.peek(), t);
									  		else
									  			tercetos.add(t);

										  }
break;
case 29:
//#line 113 "gramatica.y"
{ yyerror("\tLínea " + val_peek(3).ival + ". Estructura OUT incompleta. Falta DOT"); }
break;
case 30:
//#line 114 "gramatica.y"
{ yyerror("\tLínea " + val_peek(3).ival + ". Estructura OUT incompleta. Falta CLOSE_PAR"); }
break;
case 31:
//#line 115 "gramatica.y"
{ yyerror("\tLínea " + val_peek(3).ival + ". Estructura OUT incompleta. Falta OPEN_PAR"); }
break;
case 32:
//#line 116 "gramatica.y"
{ yyerror("\tLínea " + val_peek(4).ival + ". Estructura OUT incorrecta. Sólo se pueden imprimir cadenas"); }
break;
case 33:
//#line 119 "gramatica.y"
{ System.out.println("ASIGNACIÓN. Línea " + val_peek(3).ival);
									  Item item2 = (Item)val_peek(1).obj;
									  Terceto t = null;	
									  if (! tablaSimbolos.varDefined(val_peek(3).sval, ambitos.toString(), isMoveFunction))
									  	  yyerror("\tError en la línea " + val_peek(3).ival + ": VARIABLE NO DEFINIDA EN EL AMBITO -> " + ambitos.toString());
									  else {
										  String tipoAsignacion = tablaSimbolos.getToken(val_peek(3).sval.toLowerCase()).getType();
										  String tipoExpresion = (String)(((Item)val_peek(1).obj).getTipo());
										  if (!tipoAsignacion.equals(tipoExpresion))
                                              yyerror("Línea " + val_peek(2).ival + ". Tipos incompatibles en la asignación");
									  }

									  t = new Terceto("=", new ItemString((String)val_peek(3).sval), item2, null);

									  if (isFunction)
									  	mapeoFuncion.put(ambitos.peek(), t);
									  else
									  	tercetos.add(t);

									  yyval.obj = new ItemTerceto(t);
									}
break;
case 34:
//#line 140 "gramatica.y"
{ yyerror("\tLínea " + val_peek(2).ival + ". Asignación incompleta. Falta DOT"); }
break;
case 42:
//#line 149 "gramatica.y"
{ 
																System.out.println("Línea " + val_peek(3).ival + ". Sentencia IF-ELSE"); 
																((Terceto)tercetos.get((pila.pop()).intValue() - 1)).setArg1(new ItemString("[" + (tercetos.size() + 1) + "]"));
															}
break;
case 43:
//#line 153 "gramatica.y"
{ 
										System.out.println("Línea " + val_peek(0).ival + ". Sentencia IF");
										((Terceto)tercetos.get((pila.pop()).intValue() - 1)).setArg2(new ItemString("[" + (tercetos.size() + 1) + "]"));
										ItemString aux = new ItemString(("" + tercetos.size() + 1));
										System.out.println("El terceto que estoy buscando es el " + aux.getArg() + " " + tercetos);
										
									}
break;
case 44:
//#line 162 "gramatica.y"
{
				((Terceto)tercetos.get((pila.pop()).intValue() - 1)).setArg2(new ItemString("[" + (tercetos.size() + 2) + "]"));
				Terceto t = new Terceto("BI", new ItemString("_"), new ItemString("_"), null);
				
				if (isFunction)
					mapeoFuncion.put(ambitos.peek(), t);
				else
					tercetos.add(t);

				pila.push(new Integer(t.getNumero()));
		   }
break;
case 46:
//#line 178 "gramatica.y"
{
							Terceto t = new Terceto("BF", new ItemTerceto((Terceto)(tercetos.get(tercetos.size() - 1))), new ItemString("_"), null);
							
							if (isFunction)
								mapeoFuncion.put(ambitos.peek(), t);
							else
								tercetos.add(t);

							pila.push(new Integer(t.getNumero()));	
						 }
break;
case 47:
//#line 190 "gramatica.y"
{ 
																System.out.println("Comparación. Línea " + val_peek(2).ival); 
																String tipo1=(String)(((Item)val_peek(3).obj).getTipo());
																String tipo2=(String)(((Item)val_peek(1).obj).getTipo());
				
																Item item1 = (Item)val_peek(3).obj;
																Item item2 = (Item)val_peek(1).obj;
																
																Terceto t = new Terceto(val_peek(2).sval, item1, item2, null);
																
																if (isFunction)
									  								mapeoFuncion.put(ambitos.peek(), t);
									  							else
									  								tercetos.add(t);

																yyval.obj = new ItemTerceto(t);
															  }
break;
case 48:
//#line 207 "gramatica.y"
{ yyerror("Línea " + val_peek(2).ival + ". Condicion incompleta. Falta OPEN_PAR"); }
break;
case 49:
//#line 208 "gramatica.y"
{ yyerror("Línea " + val_peek(1).ival + ". Condicion. Falta CLOSE_PAR"); }
break;
case 51:
//#line 212 "gramatica.y"
{ System.out.println("Línea " + val_peek(2).ival + ". Bloque compuesto"); }
break;
case 52:
//#line 213 "gramatica.y"
{ yyerror("Línea " + val_peek(2).ival + ". Bloque compuesto. Falta END"); }
break;
case 59:
//#line 222 "gramatica.y"
{ 
															((Terceto)tercetos.get((pila.pop()).intValue() - 1)).setArg2(new ItemString("[" + (tercetos.size() + 2) + "]"));
															Terceto t = new Terceto("BI", new ItemTerceto((Terceto)tercetos.get((pila.pop()).intValue() - 1)), new ItemString("_"), null);
															
															if (isFunction)
									  							mapeoFuncion.put(ambitos.peek(), t);
									  						else
									  							tercetos.add(t);

													   }
break;
case 60:
//#line 234 "gramatica.y"
{
				pila.push(new Integer(tercetos.size() + 1));
			 }
break;
case 61:
//#line 239 "gramatica.y"
{
								Terceto t = new Terceto("BF", new ItemTerceto((Terceto)(tercetos.get(tercetos.size() - 1))), new ItemString("_"), null);
								
								if (isFunction)
									mapeoFuncion.put(ambitos.peek(), t);
								else
									tercetos.add(t);

								pila.push(new Integer(t.getNumero()));
                            }
break;
case 62:
//#line 251 "gramatica.y"
{ 
									System.out.println("SUMA. Línea " + val_peek(1).ival); 
									String tipo1 = (String)(((Item)val_peek(2).obj).getTipo());
									String tipo2 = (String)(((Item)val_peek(0).obj).getTipo());
									Item item1 = (Item)val_peek(2).obj;
									Item item2 = (Item)val_peek(0).obj;
									if (!tipo1.equals(tipo2))
										yyerror ("Línea " + val_peek(1).ival + ". Tipos incompatibles en la suma");
									Terceto t = new Terceto("+", item1, item2, tipo1);
									
									if (isFunction)
									  	mapeoFuncion.put(ambitos.peek(), t);
									else
									  	tercetos.add(t);

									yyval.obj = new ItemTerceto(t); 
								  }
break;
case 63:
//#line 268 "gramatica.y"
{ 
									System.out.println("RESTA. Línea " + val_peek(1).ival); 
									String tipo1 = (String)(((Item)val_peek(2).obj).getTipo());
									String tipo2 = (String)(((Item)val_peek(0).obj).getTipo());
									Item item1 = (Item)val_peek(2).obj;
									Item item2 = (Item)val_peek(0).obj;
									if (!tipo1.equals(tipo2))
										yyerror("Línea " + val_peek(1).ival + ". Tipos incompatibles en la resta");
									Terceto t = new Terceto("-", item1, item2, tipo1);
									
									if (isFunction)
									  	mapeoFuncion.put(ambitos.peek(), t);
									else
									  	tercetos.add(t);

									yyval.obj = new ItemTerceto(t);
								  }
break;
case 64:
//#line 285 "gramatica.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 65:
//#line 288 "gramatica.y"
{ 
								System.out.println("MULTIPLICACION. Línea " + val_peek(1).ival); 
								String tipo1 = (String)(((Item)val_peek(2).obj).getTipo());
								String tipo2 = (String)(((Item)val_peek(0).obj).getTipo());
								Item item1 = (Item)val_peek(2).obj;
								Item item2 = (Item)val_peek(0).obj;
								if (!tipo1.equals(tipo2))
									yyerror("Línea " + val_peek(1).ival + ". Tipos incompatibles en la multiplicación");
								Terceto t = new Terceto("*", item1, item2, tipo1);
								
								if (isFunction)
									mapeoFuncion.put(ambitos.peek(), t);
								else
									tercetos.add(t);

								yyval.obj = new ItemTerceto(t);
							  }
break;
case 66:
//#line 305 "gramatica.y"
{ 
								System.out.println("DIVISION. Línea " + val_peek(1).ival); 
								String tipo1 = (String)(((Item)val_peek(2).obj).getTipo());
								String tipo2 = (String)(((Item)val_peek(0).obj).getTipo());
								Item item1 = (Item)val_peek(2).obj;
								Item item2 = (Item)val_peek(0).obj;
								if (!tipo1.equals(tipo2))
									yyerror ("Línea " + val_peek(1).ival + ". Tipos incompatibles en la division");
								Terceto t = new Terceto("/", item1, item2, tipo1);
								
								if (isFunction)
									mapeoFuncion.put(ambitos.peek(), t);
								else
									tercetos.add(t);

								yyval.obj = new ItemTerceto(t);
							 }
break;
case 67:
//#line 322 "gramatica.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 68:
//#line 325 "gramatica.y"
{ 
				System.out.println("Lectura de la variable " + val_peek(0).sval + ". Línea " + val_peek(0).ival); 

				if (tablaSimbolos.functionDefined(val_peek(0).sval))
					yyerror("Línea " + val_peek(0).ival + ": NOMBRE DE FUNCION COMO OPERANDO --> FALTAN LOS PARENTESIS");
			    else if (!tablaSimbolos.varDefined(val_peek(0).sval, ambitos.toString(), isMoveFunction))
			  		yyerror("\tError en la línea " + val_peek(0).ival + ": VARIABLE -> " + val_peek(0).sval + " NO DEFINIDA EN EL AMBITO -> " + ambitos.toString()); 
			  		
			    String id = val_peek(0).sval.toLowerCase();
			    ItemString itemString = new ItemString(id);
			    itemString.setTabla(tablaSimbolos);
			    yyval.obj = itemString;
			}
break;
case 69:
//#line 338 "gramatica.y"
{ String cte = val_peek(0).sval;
			   ItemString itemString = new ItemString(cte);
			   itemString.setTabla(tablaSimbolos);
			   yyval.obj = itemString;
			 }
break;
case 70:
//#line 343 "gramatica.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 77:
//#line 354 "gramatica.y"
{ 
												if (! tablaSimbolos.functionDefined(val_peek(2).sval)) 
													yyerror("\tError en la línea " + val_peek(2).ival + ": FUNCION NO DEFINIDA"); 
											
												System.out.println("Invocación a función. Línea " + val_peek(2).ival); 
												String id = val_peek(2).sval;
												ItemString itemString = new ItemString(id);
												itemString.setTabla(tablaSimbolos);
												yyval.obj = itemString;
													
											}
break;
//#line 1049 "Parser.java"
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
