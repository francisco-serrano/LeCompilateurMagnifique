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
	import accionsemantica.TablaSimbolos;
//#line 19 "Parser.java"




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
    0,    2,    2,    2,    2,    3,    3,    4,    4,    4,
    7,    8,    8,    9,    9,    9,   10,   11,   11,   11,
   12,   12,   12,   12,   12,   12,   12,   12,   12,   12,
   12,   12,   15,   15,   15,   15,   13,    6,    6,    6,
   17,   17,   17,   18,   18,   18,   16,   16,   16,   16,
   16,   16,   19,   19,   14,   14,    1,    1,    1,    5,
    5,   20,   20,   20,   21,   22,   22,   23,   23,   23,
};
final static short yylen[] = {                            2,
    1,    4,    3,    5,    4,    1,    1,    8,    7,    3,
    3,    1,    1,    4,    3,    3,    1,    3,    1,    2,
    9,    7,    6,    6,    6,    5,    5,    4,    4,    4,
    3,    5,    6,    5,    5,    4,    3,    3,    3,    1,
    3,    3,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    4,    3,    1,    1,    2,    1,    1,    1,
    2,    3,    2,    2,    1,    2,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    6,    7,    0,    0,    0,    0,    0,    0,
    1,   17,    0,    0,   12,   13,    0,   69,   59,   70,
   68,   55,   56,    0,    0,    0,    0,    0,   20,    0,
    0,    0,    0,   45,    0,    0,   46,    0,    0,   43,
    0,    0,    0,    0,    0,    0,    0,    0,   57,    0,
    0,   63,   66,    0,   18,   62,    0,    0,    0,    0,
   47,   48,   49,   50,   51,   52,    0,    0,    0,    0,
    0,    0,    0,   31,    0,    0,    3,    0,    0,    0,
    0,    0,    0,    0,   16,   53,   11,    0,    0,    0,
    0,    0,    0,    0,   41,   42,   29,    0,    0,   30,
    0,    0,    0,   60,    5,    0,    0,    0,   36,    2,
    0,   14,    0,    0,    0,   26,   27,   32,    0,   10,
    0,   61,    0,   35,   34,    4,    0,   24,   25,    0,
    0,   33,    0,   22,    0,    0,    0,    0,    0,   21,
    9,    0,    8,
};
final static short yydgoto[] = {                         10,
   11,   12,   13,   77,  102,   36,   37,   14,   15,   16,
   17,   18,   38,   19,   20,   67,   39,   40,   21,   22,
   23,   24,   25,
};
final static short yysindex[] = {                      -240,
 -145, -251,    0,    0, -253, -263, -244, -252, -155,    0,
    0,    0, -147, -240,    0,    0, -133,    0,    0,    0,
    0,    0,    0, -231, -251, -184, -113, -197,    0, -195,
 -175, -251, -179,    0, -113, -181,    0, -143, -116,    0,
 -250, -149, -192, -152, -113, -219, -144, -174,    0, -121,
 -148,    0,    0, -132,    0,    0, -154, -123, -113, -113,
    0,    0,    0,    0,    0,    0, -113, -139, -209, -113,
 -113, -146, -249,    0, -138, -213,    0, -192, -205, -170,
 -209, -192,  -94, -100,    0,    0,    0, -109, -209, -116,
 -116, -108, -209, -110,    0,    0,    0,  -97,  -96,    0,
 -103, -134, -196,    0,    0, -120, -209, -209,    0,    0,
 -192,    0, -209, -106, -102,    0,    0,    0, -113,    0,
  -99,    0, -209,    0,    0,    0, -119,    0,    0, -153,
 -113,    0, -209,    0,  -88, -151,  -95, -111,  -85,    0,
    0, -105,    0,
};
final static short yyrindex[] = {                         0,
 -127,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  180,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  125, -127,    0,    0,    0,    0,
  172,  178,    1,    0,    0,    0,    0,    0,   36,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  133,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  141,    0,    0,    0,    0,    0,   71,
  106, -208,    0,    0,    0,    0,    0,  162,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  175,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
  168,    0,   -1,  -60,   81,  -27,    0,    0,  -50,    0,
   62,    0,   -7,  -66,    0,    0,   99,   90,    0,    0,
    0,   53,   57,
};
final static int YYTABLESIZE=467;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         54,
   44,   46,   94,   33,   34,   30,   33,   34,   41,  104,
   59,   60,   43,   73,  109,   51,    1,  105,   35,    5,
   42,  110,  114,   99,    2,  103,  115,   58,    3,    4,
    5,    6,   44,   72,   52,   40,  104,   79,    9,   92,
  124,  125,    6,    1,    7,    8,  127,   30,   84,    9,
  126,    2,  103,   80,   31,    2,  132,    5,   32,   26,
    1,    5,   29,   27,   37,   37,  137,  106,    2,    6,
   38,   81,   26,    6,    5,  101,    9,   53,   59,   60,
    9,   32,   37,   28,   53,  107,    6,   29,   32,   55,
   56,  130,   57,    9,   76,   61,   62,   63,   64,   65,
   66,   33,   34,  136,   78,   39,   59,   60,   59,   60,
   83,   26,   82,   27,   74,   85,   45,   97,   87,  135,
  108,  139,   28,   75,   65,  100,   98,   59,   60,   68,
   69,   86,   54,   50,   93,    3,    4,   47,   48,   19,
   15,   19,   19,   33,   34,   70,   71,    3,    4,   88,
   89,   59,   60,  120,  121,  133,  134,   90,   91,   95,
   96,   28,  111,  112,  113,  116,  117,  118,  119,  128,
  123,   64,  131,  129,   23,  138,  141,   67,  142,   58,
  140,   49,  143,  122,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   44,    0,    0,
   44,   44,   44,   44,   44,    0,   44,    0,    0,    0,
    0,   44,    0,   44,   44,   44,   44,   44,   44,   44,
   44,   44,   44,   44,    0,    0,    0,    0,   44,   44,
   44,   44,   40,    0,    0,   40,   40,    0,    0,   40,
    0,   40,    0,    0,    0,    0,   40,    0,   40,   40,
   40,   40,   40,   40,   40,   40,   40,   40,   40,    0,
    0,    0,    0,   40,   40,   40,   40,   38,    0,    0,
   38,   38,    0,    0,   38,    0,   38,    0,    0,    0,
    0,   38,    0,   38,   38,   38,   38,   38,   38,   38,
   38,   38,   38,   38,    0,    0,    0,    0,   38,   38,
   38,   38,   39,    0,    0,   39,   39,    0,    0,   39,
    0,   39,    0,    0,    0,    0,   39,    0,   39,   39,
   39,   39,   39,   39,   39,   39,   39,   39,   39,   54,
   65,    0,    0,   39,   39,   39,   39,   15,   54,   65,
   65,    0,    0,   54,    0,   15,    0,   54,   54,   15,
   15,   15,   65,   65,    0,   54,    0,    0,   28,    0,
   54,   54,   54,   15,    0,   15,   15,   28,   64,    0,
   15,   23,   28,    0,    0,    0,   28,   28,    0,    0,
   23,    0,   64,   67,   28,   23,   64,   64,    0,   28,
   28,   28,   67,   67,   64,    0,    0,   23,    0,   64,
   64,   64,   23,   23,   23,   67,   67,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         27,
    0,    9,   69,  257,  258,  257,  257,  258,  272,   76,
  260,  261,  257,   41,   81,   17,  257,   78,  272,  271,
  284,   82,   89,  273,  265,   76,   93,   35,  269,  270,
  271,  283,  285,  284,  266,    0,  103,   45,  290,   67,
  107,  108,  283,  257,  285,  286,  113,  257,   50,  290,
  111,  265,  103,  273,    2,  265,  123,  271,    2,  257,
  257,  271,    1,  259,  273,  274,  133,  273,  265,  283,
    0,  291,  257,  283,  271,  289,  290,   25,  260,  261,
  290,   25,  291,  268,   32,  291,  283,   26,   32,   28,
  266,  119,  272,  290,  287,  277,  278,  279,  280,  281,
  282,  257,  258,  131,  257,    0,  260,  261,  260,  261,
  285,  257,  257,  259,  264,  264,  272,  264,  273,  273,
  291,  273,  268,  273,    0,  264,  273,  260,  261,  273,
  274,  264,    0,  267,  274,  269,  270,  285,  286,  267,
    0,  269,  270,  257,  258,  262,  263,  269,  270,  273,
  274,  260,  261,  288,  289,  275,  276,   59,   60,   70,
   71,    0,  257,  264,  274,  276,  264,  264,  272,  276,
  291,    0,  272,  276,    0,  264,  288,    0,  264,    0,
  276,   14,  288,  103,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,   -1,   -1,
  260,  261,  262,  263,  264,   -1,  266,   -1,   -1,   -1,
   -1,  271,   -1,  273,  274,  275,  276,  277,  278,  279,
  280,  281,  282,  283,   -1,   -1,   -1,   -1,  288,  289,
  290,  291,  257,   -1,   -1,  260,  261,   -1,   -1,  264,
   -1,  266,   -1,   -1,   -1,   -1,  271,   -1,  273,  274,
  275,  276,  277,  278,  279,  280,  281,  282,  283,   -1,
   -1,   -1,   -1,  288,  289,  290,  291,  257,   -1,   -1,
  260,  261,   -1,   -1,  264,   -1,  266,   -1,   -1,   -1,
   -1,  271,   -1,  273,  274,  275,  276,  277,  278,  279,
  280,  281,  282,  283,   -1,   -1,   -1,   -1,  288,  289,
  290,  291,  257,   -1,   -1,  260,  261,   -1,   -1,  264,
   -1,  266,   -1,   -1,   -1,   -1,  271,   -1,  273,  274,
  275,  276,  277,  278,  279,  280,  281,  282,  283,  257,
  266,   -1,   -1,  288,  289,  290,  291,  257,  266,  275,
  276,   -1,   -1,  271,   -1,  265,   -1,  275,  276,  269,
  270,  271,  288,  289,   -1,  283,   -1,   -1,  257,   -1,
  288,  289,  290,  283,   -1,  285,  286,  266,  257,   -1,
  290,  257,  271,   -1,   -1,   -1,  275,  276,   -1,   -1,
  266,   -1,  271,  266,  283,  271,  275,  276,   -1,  288,
  289,  290,  275,  276,  283,   -1,   -1,  283,   -1,  288,
  289,  290,  288,  289,  290,  288,  289,
};
}
final static short YYFINAL=10;
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
"programa : bloque",
"funcion : tipo FUNCTION ID cuerpo_funcion",
"funcion : FUNCTION ID cuerpo_funcion",
"funcion : tipo MOVE FUNCTION ID cuerpo_funcion",
"funcion : MOVE FUNCTION ID cuerpo_funcion",
"tipo : UINT",
"tipo : ULONG",
"cuerpo_funcion : OPEN_BRACE bloque_funcion RETURN OPEN_PAR expresion CLOSE_PAR DOT CLOSE_BRACE",
"cuerpo_funcion : OPEN_BRACE RETURN OPEN_PAR expresion CLOSE_PAR DOT CLOSE_BRACE",
"cuerpo_funcion : OPEN_BRACE bloque_funcion CLOSE_BRACE",
"invocacion_funcion : ID OPEN_PAR CLOSE_PAR",
"declaracion : declaracion_variables",
"declaracion : declaracion_funcion",
"declaracion_variables : lista_variables COLON tipo DOT",
"declaracion_variables : lista_variables COLON tipo",
"declaracion_variables : lista_variables tipo DOT",
"declaracion_funcion : funcion",
"lista_variables : ID COMMA lista_variables",
"lista_variables : ID",
"lista_variables : ID lista_variables",
"ejecutable : IF OPEN_PAR condicion CLOSE_PAR THEN bloque_sentencias ELSE bloque_sentencias END_IF",
"ejecutable : IF OPEN_PAR condicion CLOSE_PAR THEN bloque_sentencias END_IF",
"ejecutable : IF OPEN_PAR condicion CLOSE_PAR THEN bloque_sentencias",
"ejecutable : IF OPEN_PAR condicion THEN bloque_sentencias END_IF",
"ejecutable : IF condicion CLOSE_PAR THEN bloque_sentencias END_IF",
"ejecutable : IF condicion THEN bloque_sentencias END_IF",
"ejecutable : OUT OPEN_PAR CADENA CLOSE_PAR DOT",
"ejecutable : OUT OPEN_PAR CADENA CLOSE_PAR",
"ejecutable : OUT OPEN_PAR CADENA DOT",
"ejecutable : OUT CADENA CLOSE_PAR DOT",
"ejecutable : OUT CADENA DOT",
"ejecutable : OUT OPEN_PAR expresion CLOSE_PAR DOT",
"control : WHILE OPEN_PAR condicion CLOSE_PAR DO bloque_sentencias",
"control : WHILE condicion CLOSE_PAR DO bloque_sentencias",
"control : WHILE OPEN_PAR condicion DO bloque_sentencias",
"control : WHILE condicion DO bloque_sentencias",
"condicion : expresion comparador expresion",
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
"asignacion : ID ASIGN expresion DOT",
"asignacion : ID ASIGN expresion",
"bloque_sentencias : bloque_compuesto",
"bloque_sentencias : bloque_simple",
"bloque : declaracion bloque",
"bloque : declaracion",
"bloque : bloque_sentencias",
"bloque_funcion : bloque_sentencias",
"bloque_funcion : declaracion_variables bloque_funcion",
"bloque_compuesto : BEGIN sentencias END",
"bloque_compuesto : sentencias END",
"bloque_compuesto : BEGIN sentencias",
"bloque_simple : sentencia",
"sentencias : sentencia sentencias",
"sentencias : sentencia",
"sentencia : asignacion",
"sentencia : ejecutable",
"sentencia : control",
};

//#line 136 "gramatica.y"

void yyerror(String error) {
	System.err.println(error);
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


Lexer lexer;
TablaSimbolos tablaSimbolos;
//#line 435 "Parser.java"
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
case 3:
//#line 14 "gramatica.y"
{ yyerror("\tLínea " + val_peek(2).ival + ". Declaración de función incompleta. Falta tipo de retorno"); }
break;
case 5:
//#line 17 "gramatica.y"
{ yyerror("\tLínea " + val_peek(3).ival + ". Declaración de función incompleta. Falta tipo de retorno"); }
break;
case 10:
//#line 25 "gramatica.y"
{ yyerror("\tLínea " + val_peek(2).ival + ". Declaración de función incompleta. Falta sentencia RETURN"); }
break;
case 11:
//#line 28 "gramatica.y"
{ System.out.println("Invocación a función. Línea " + val_peek(2).ival); }
break;
case 14:
//#line 35 "gramatica.y"
{ System.out.println("Declaración de Variables. Línea " + val_peek(2).ival); }
break;
case 15:
//#line 36 "gramatica.y"
{ yyerror("\tLínea " + val_peek(1).ival + ". Declaración de variables incompleta. Falta DOT"); }
break;
case 16:
//#line 37 "gramatica.y"
{ yyerror("\tLínea " + val_peek(0).ival + ". Declaración de variables incompleta. Falta COLON"); }
break;
case 17:
//#line 40 "gramatica.y"
{ System.out.println("Declaración de Función. Línea " + val_peek(0).ival); }
break;
case 18:
//#line 43 "gramatica.y"
{ tablaSimbolos.defineVar(val_peek(2).sval); tablaSimbolos.defineVar(val_peek(0).sval); }
break;
case 20:
//#line 45 "gramatica.y"
{ yyerror("\tLínea " + val_peek(1).ival + ". Declaración incompleta. Falta COMMA"); }
break;
case 21:
//#line 48 "gramatica.y"
{ System.out.println("Línea " + val_peek(8).ival + ". Sentencia IF"); }
break;
case 22:
//#line 49 "gramatica.y"
{ System.out.println("Línea " + val_peek(6).ival + ". Sentencia IF"); }
break;
case 23:
//#line 50 "gramatica.y"
{ yyerror("\tLínea " + val_peek(5).ival + ". Estructura IF incompleta. Falta END_IF"); }
break;
case 24:
//#line 51 "gramatica.y"
{ yyerror("\tLínea " + val_peek(5).ival + ". Estructura IF incompleta. Falta CLOSE_PAR"); }
break;
case 25:
//#line 52 "gramatica.y"
{ yyerror("\tLínea " + val_peek(5).ival + ". Estructura IF incompleta. Falta OPEN_PAR"); }
break;
case 26:
//#line 53 "gramatica.y"
{ yyerror("\tLínea " + val_peek(4).ival + ". Estructura IF incompleta. La condición va entre paréntesis"); }
break;
case 27:
//#line 55 "gramatica.y"
{ System.out.println("Sentencia OUT. Línea " + val_peek(4).ival); }
break;
case 28:
//#line 56 "gramatica.y"
{ yyerror("\tLínea " + val_peek(3).ival + ". Estructura OUT incompleta. Falta DOT"); }
break;
case 29:
//#line 57 "gramatica.y"
{ yyerror("\tLínea " + val_peek(3).ival + ". Estructura OUT incompleta. Falta CLOSE_PAR"); }
break;
case 30:
//#line 58 "gramatica.y"
{ yyerror("\tLínea " + val_peek(3).ival + ". Estructura OUT incompleta. Falta OPEN_PAR"); }
break;
case 31:
//#line 59 "gramatica.y"
{ yyerror("\tLínea " + val_peek(2).ival + ". Estructura OUT incompleta. La cadena va entre paréntesis"); }
break;
case 32:
//#line 60 "gramatica.y"
{ yyerror("\tLínea " + val_peek(4).ival + ". Estructura OUT incorrecta. Sólo se pueden imprimir cadenas"); }
break;
case 33:
//#line 63 "gramatica.y"
{ System.out.println("Línea " + val_peek(5).ival + ". Estructura WHILE"); }
break;
case 34:
//#line 64 "gramatica.y"
{ yyerror("Línea " + val_peek(4).ival + ". Estructura WHILE incompleta. Falta OPEN_PAR"); }
break;
case 35:
//#line 65 "gramatica.y"
{ yyerror("Línea " + val_peek(4).ival + ". Estructura WHILE incompleta. Falta CLOSE_PAR"); }
break;
case 36:
//#line 66 "gramatica.y"
{ yyerror("Línea " + val_peek(3).ival + ". Estructura WHILE incompleta. La condición va entre paréntesis"); }
break;
case 37:
//#line 69 "gramatica.y"
{ System.out.println("Comparación. Línea " + val_peek(2).ival); }
break;
case 38:
//#line 72 "gramatica.y"
{ System.out.println("SUMA. Línea " + val_peek(1).ival); }
break;
case 39:
//#line 73 "gramatica.y"
{ System.out.println("RESTA. Línea " + val_peek(1).ival); }
break;
case 41:
//#line 77 "gramatica.y"
{ System.out.println("MULTIPLICACION. Línea " + val_peek(1).ival); }
break;
case 42:
//#line 78 "gramatica.y"
{ System.out.println("DIVISION. Línea " + val_peek(1).ival); }
break;
case 44:
//#line 82 "gramatica.y"
{ System.out.println("Lectura de la variable " + val_peek(0).sval + ". Línea " + val_peek(0).ival); 
			  if (! tablaSimbolos.varDefined(val_peek(0).sval))
			  	yyerror("\tError en la línea " + val_peek(0).ival + ": VARIABLE NO DEFINIDA"); 
			}
break;
case 53:
//#line 98 "gramatica.y"
{ System.out.println("Asignación. Línea " + val_peek(3).ival);
									  if (! tablaSimbolos.varDefined(val_peek(3).sval))
									  	yyerror("\tError en la línea " + val_peek(3).ival + ": VARIABLE NO DEFINIDA"); 
									}
break;
case 54:
//#line 102 "gramatica.y"
{ yyerror("\tLínea " + val_peek(2).ival + ". Asignación incompleta. Falta DOT"); }
break;
case 62:
//#line 118 "gramatica.y"
{ System.out.println("Línea " + val_peek(2).ival + ". Bloque compuesto"); }
break;
case 63:
//#line 119 "gramatica.y"
{ yyerror("\tLínea " + val_peek(1).ival + ". Bloque compuesto incompleto. Falta BEGIN"); }
break;
case 64:
//#line 120 "gramatica.y"
{ yyerror("\tLínea " + val_peek(1).ival + ". Bloque compuesto incompleto. Falta END"); }
break;
//#line 738 "Parser.java"
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
