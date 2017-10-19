#ifndef lint
static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";
#endif
#define YYBYACC 1
#line 2 "gramatica.y"
	package parser;

	import lexer.TablaSimbolos;
	import lexer.Terceto;
	import lexer.Item;
	import lexer.ItemString;
	import lexer.ItemTerceto;
	import lexer.Lexer;
	import lexer.TablaSimbolos;
	
	import java.util.Stack;
	import java.util.ArrayList;
	import java.util.List;
#line 20 "y.tab.c"
#define ID 257
#define CTE 258
#define ASIGN 259
#define ADD 260
#define SUB 261
#define MULT 262
#define DIV 263
#define DOT 264
#define BEGIN 265
#define END 266
#define COLON 267
#define COMMA 268
#define UINT 269
#define ULONG 270
#define IF 271
#define OPEN_PAR 272
#define CLOSE_PAR 273
#define THEN 274
#define ELSE 275
#define END_IF 276
#define LEQ 277
#define GEQ 278
#define LT 279
#define GT 280
#define EQ 281
#define NEQ 282
#define OUT 283
#define CADENA 284
#define FUNCTION 285
#define MOVE 286
#define OPEN_BRACE 287
#define CLOSE_BRACE 288
#define RETURN 289
#define WHILE 290
#define DO 291
#define YYERRCODE 256
short yylhs[] = {                                        -1,
    0,    2,    2,    4,    4,    4,    5,    5,    5,    6,
    6,    3,    3,    3,    3,    7,    7,    7,    1,    1,
   10,   10,   10,   10,   10,   12,   12,   12,   12,   12,
   11,   11,    8,    8,   15,   15,   15,   15,   15,   13,
   13,   17,   16,   19,   20,   20,   20,   18,   18,   18,
   22,   22,   22,   22,   23,   23,   14,   24,   25,    9,
    9,    9,   26,   26,   26,   27,   27,   27,   21,   21,
   21,   21,   21,   21,   28,
};
short yylen[] = {                                         2,
    1,    1,    1,    4,    3,    3,    3,    1,    2,    1,
    1,    4,    3,    5,    4,    8,    7,    3,    2,    1,
    1,    1,    1,    1,    1,    5,    4,    4,    4,    5,
    4,    3,    2,    1,    1,    1,    1,    1,    1,    4,
    2,    1,    4,    1,    5,    4,    4,    1,    3,    3,
    1,    1,    1,    1,    2,    1,    4,    1,    1,    3,
    3,    1,    3,    3,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    3,
};
short yydefred[] = {                                      0,
    0,   10,   11,    0,    0,    0,    0,   58,    0,    0,
   25,    2,    3,    0,    0,   20,   21,   22,   23,   24,
    0,    0,    0,    0,   67,    0,    0,    0,   44,    0,
   65,   68,    0,    0,    0,    0,   19,    9,    0,    0,
    0,    0,    0,   42,   41,    0,   59,    0,    0,    0,
    0,    0,    0,   69,   70,   71,   72,   73,   74,    0,
    0,    0,    0,    0,    0,    0,    0,   13,    0,    0,
    7,    6,    0,    0,    0,    0,   51,   54,   52,   53,
    0,   48,    0,   31,   75,    0,    0,    0,    0,   43,
   63,   64,   28,    0,    0,   29,    0,   35,    0,   36,
   37,   38,   39,   34,   15,    4,   12,    0,   56,    0,
   40,   57,    0,   46,   26,   30,    0,   18,    0,   33,
   14,   50,   49,   55,   45,    0,    0,    0,    0,    0,
    0,   17,    0,   16,
};
short yydgoto[] = {                                       9,
   10,   11,   12,   13,   14,   15,   68,   99,   27,   16,
   77,   78,   79,   80,  104,   21,   46,   81,   28,   29,
   60,   82,  110,   22,   48,   30,   31,   32,
};
short yysindex[] = {                                   -203,
 -242,    0,    0, -194, -148, -234, -257,    0,    0, -203,
    0,    0,    0, -178, -158,    0,    0,    0,    0,    0,
 -137, -194, -117, -239,    0, -117, -167, -243,    0, -120,
    0,    0, -237, -222, -251, -196,    0,    0, -123, -188,
 -192, -162, -155,    0,    0, -230,    0, -175, -135, -141,
 -167, -117, -117,    0,    0,    0,    0,    0,    0, -117,
 -230, -117, -117, -187, -176, -130, -231,    0, -251, -127,
    0,    0, -251, -113, -242, -209,    0,    0,    0,    0,
 -124,    0, -230,    0,    0, -117, -120, -120, -156,    0,
    0,    0,    0, -111, -110,    0, -116,    0, -233,    0,
    0,    0,    0,    0,    0,    0,    0, -251,    0, -241,
    0,    0, -142,    0,    0,    0, -117,    0, -115,    0,
    0,    0,    0,    0,    0, -140, -117, -109, -138, -129,
 -106,    0, -128,    0,
};
short yyrindex[] = {                                      0,
 -169,    0,    0,    0,    0,    0,    0,    0,    0,  161,
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
    0,    0, -245,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
short yygindex[] = {                                      0,
    0,    0,    0,  -56,    0,   -7,  -63,    0,  -21,  152,
    3,    4,    8,    9,   64,    0,    0,  -39,    0,  142,
  114,  -76,    0,    0,    0,   96,   88,    0,
};
#define YYTABLESIZE 470
short yytable[] = {                                     109,
   66,   49,   17,   18,   51,  105,   41,   19,   20,  107,
   98,   65,   17,   18,  122,   75,   23,   19,   20,   24,
   25,   90,   35,    1,  123,    1,   75,   36,   47,    4,
   61,   70,   50,  124,   76,   67,   62,    4,   89,    4,
    4,    5,   98,  112,  121,   47,   64,   75,    8,    5,
   66,    5,    5,    1,  118,  119,    8,   97,    8,    8,
   69,    4,   24,   25,  113,    2,    3,    4,   71,  100,
  101,   72,   60,    5,  102,  103,   93,   26,   38,    5,
    8,    6,    7,   52,   53,   94,    8,    8,   39,   40,
    2,    3,   52,   53,   73,  126,   95,    8,    8,    8,
    8,  100,  101,   52,   53,  129,  102,  103,   61,   54,
   55,   56,   57,   58,   59,   83,  114,   52,   53,   52,
   53,   52,   53,   33,   52,   53,   42,   43,   84,   74,
  125,   85,  128,   96,  131,   34,  106,   44,   45,   24,
   25,   62,   63,  108,   32,    2,    3,   87,   88,   91,
   92,  111,  115,  116,  130,  117,  127,  133,  132,  134,
    1,   37,  120,   47,   86,    0,    0,    0,    0,   27,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    5,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   66,   66,    0,    0,
   66,   66,   66,   66,   66,    0,   66,    0,    0,   66,
   66,   66,    0,   66,   66,   66,   66,   66,   66,   66,
   66,   66,   66,   66,    0,   66,   66,    0,   66,   66,
   66,   66,   62,   62,    0,    0,   62,   62,    0,    0,
   62,    0,   62,    0,    0,   62,   62,   62,    0,   62,
   62,   62,   62,   62,   62,   62,   62,   62,   62,   62,
    0,   62,   62,    0,   62,   62,   62,   62,   60,   60,
    0,    0,   60,   60,    0,    0,   60,    0,   60,    0,
    0,   60,   60,   60,    0,   60,   60,   60,   60,   60,
   60,   60,   60,   60,   60,   60,    0,   60,   60,    0,
   60,   60,   60,   60,   61,   61,    0,    0,   61,   61,
    0,    0,   61,    0,   61,    0,    0,   61,   61,   61,
    0,   61,   61,   61,   61,   61,   61,   61,   61,   61,
   61,   61,    0,   61,   61,    0,   61,   61,   61,   61,
   32,   32,    0,    0,    0,    0,    0,    0,    0,    0,
   32,    0,    0,   32,   32,   32,    0,    0,    0,   32,
   32,    0,    0,    0,    0,   27,   27,   32,    0,   32,
   32,    0,   32,   32,   32,   27,    5,    0,   27,   27,
   27,    0,    0,    0,   27,   27,    0,    0,    5,    5,
    5,    0,   27,    0,   27,   27,    0,   27,   27,   27,
    0,    0,    5,    0,    5,    5,    0,    5,    5,    5,
};
short yycheck[] = {                                      76,
    0,   23,    0,    0,   26,   69,   14,    0,    0,   73,
   67,   33,   10,   10,  256,  257,  259,   10,   10,  257,
  258,   61,  257,  257,  266,  257,  257,  285,  274,  271,
  274,   39,  272,  110,  265,  287,    0,  271,   60,  271,
  271,  283,   99,   83,  108,  291,  284,  257,  290,  283,
  273,  283,  283,  257,  288,  289,  290,  289,  290,  290,
  257,  271,  257,  258,   86,  269,  270,  271,  257,   67,
   67,  264,    0,  283,   67,   67,  264,  272,  257,  283,
  290,  285,  286,  260,  261,  273,  290,  257,  267,  268,
  269,  270,  260,  261,  257,  117,  273,  267,  268,  269,
  270,   99,   99,  260,  261,  127,   99,   99,    0,  277,
  278,  279,  280,  281,  282,  291,  273,  260,  261,  260,
  261,  260,  261,  272,  260,  261,  285,  286,  264,  285,
  273,  273,  273,  264,  273,  284,  264,  275,  276,  257,
  258,  262,  263,  257,    0,  269,  270,   52,   53,   62,
   63,  276,  264,  264,  264,  272,  272,  264,  288,  288,
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
#define YYFINAL 9
#ifndef YYDEBUG
#define YYDEBUG 0
#endif
#define YYMAXTOKEN 291
#if YYDEBUG
char *yyname[] = {
"end-of-file",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,"ID","CTE","ASIGN","ADD","SUB",
"MULT","DIV","DOT","BEGIN","END","COLON","COMMA","UINT","ULONG","IF","OPEN_PAR",
"CLOSE_PAR","THEN","ELSE","END_IF","LEQ","GEQ","LT","GT","EQ","NEQ","OUT",
"CADENA","FUNCTION","MOVE","OPEN_BRACE","CLOSE_BRACE","RETURN","WHILE","DO",
};
char *yyrule[] = {
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
"declaracion_funcion : tipo FUNCTION ID cuerpo_funcion",
"declaracion_funcion : FUNCTION ID cuerpo_funcion",
"declaracion_funcion : tipo MOVE FUNCTION ID cuerpo_funcion",
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
#endif
#ifndef YYSTYPE
typedef int YYSTYPE;
#endif
#define yyclearin (yychar=(-1))
#define yyerrok (yyerrflag=0)
#ifdef YYSTACKSIZE
#ifndef YYMAXDEPTH
#define YYMAXDEPTH YYSTACKSIZE
#endif
#else
#ifdef YYMAXDEPTH
#define YYSTACKSIZE YYMAXDEPTH
#else
#define YYSTACKSIZE 500
#define YYMAXDEPTH 500
#endif
#endif
int yydebug;
int yynerrs;
int yyerrflag;
int yychar;
short *yyssp;
YYSTYPE *yyvsp;
YYSTYPE yyval;
YYSTYPE yylval;
short yyss[YYSTACKSIZE];
YYSTYPE yyvs[YYSTACKSIZE];
#define yystacksize YYSTACKSIZE
#line 309 "gramatica.y"

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

public List<Terceto> getTercetos(){
	return this.tercetos;
}

Lexer lexer;
List<Terceto> tercetos = new ArrayList<>();
Stack<Integer> pila = new Stack<>();
TablaSimbolos tablaSimbolos;
String uso;
List<String> auxVariables = new ArrayList<>();

List<String> lastDeclared = new ArrayList<>();
String auxType;
String auxUso;

CustomStack<String> ambito = new CustomStack<>("main");
#line 399 "y.tab.c"
#define YYABORT goto yyabort
#define YYACCEPT goto yyaccept
#define YYERROR goto yyerrlab
int
yyparse()
{
    register int yym, yyn, yystate;
#if YYDEBUG
    register char *yys;
    extern char *getenv();

    if (yys = getenv("YYDEBUG"))
    {
        yyn = *yys;
        if (yyn >= '0' && yyn <= '9')
            yydebug = yyn - '0';
    }
#endif

    yynerrs = 0;
    yyerrflag = 0;
    yychar = (-1);

    yyssp = yyss;
    yyvsp = yyvs;
    *yyssp = yystate = 0;

yyloop:
    if (yyn = yydefred[yystate]) goto yyreduce;
    if (yychar < 0)
    {
        if ((yychar = yylex()) < 0) yychar = 0;
#if YYDEBUG
        if (yydebug)
        {
            yys = 0;
            if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
            if (!yys) yys = "illegal-symbol";
            printf("yydebug: state %d, reading %d (%s)\n", yystate,
                    yychar, yys);
        }
#endif
    }
    if ((yyn = yysindex[yystate]) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
    {
#if YYDEBUG
        if (yydebug)
            printf("yydebug: state %d, shifting to state %d (%s)\n",
                    yystate, yytable[yyn],yyrule[yyn]);
#endif
        if (yyssp >= yyss + yystacksize - 1)
        {
            goto yyoverflow;
        }
        *++yyssp = yystate = yytable[yyn];
        *++yyvsp = yylval;
        yychar = (-1);
        if (yyerrflag > 0)  --yyerrflag;
        goto yyloop;
    }
    if ((yyn = yyrindex[yystate]) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
    {
        yyn = yytable[yyn];
        goto yyreduce;
    }
    if (yyerrflag) goto yyinrecovery;
#ifdef lint
    goto yynewerror;
#endif
yynewerror:
    yyerror("syntax error");
#ifdef lint
    goto yyerrlab;
#endif
yyerrlab:
    ++yynerrs;
yyinrecovery:
    if (yyerrflag < 3)
    {
        yyerrflag = 3;
        for (;;)
        {
            if ((yyn = yysindex[*yyssp]) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
#if YYDEBUG
                if (yydebug)
                    printf("yydebug: state %d, error recovery shifting\
 to state %d\n", *yyssp, yytable[yyn]);
#endif
                if (yyssp >= yyss + yystacksize - 1)
                {
                    goto yyoverflow;
                }
                *++yyssp = yystate = yytable[yyn];
                *++yyvsp = yylval;
                goto yyloop;
            }
            else
            {
#if YYDEBUG
                if (yydebug)
                    printf("yydebug: error recovery discarding state %d\n",
                            *yyssp);
#endif
                if (yyssp <= yyss) goto yyabort;
                --yyssp;
                --yyvsp;
            }
        }
    }
    else
    {
        if (yychar == 0) goto yyabort;
#if YYDEBUG
        if (yydebug)
        {
            yys = 0;
            if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
            if (!yys) yys = "illegal-symbol";
            printf("yydebug: state %d, error recovery discards token %d (%s)\n",
                    yystate, yychar, yys);
        }
#endif
        yychar = (-1);
        goto yyloop;
    }
yyreduce:
#if YYDEBUG
    if (yydebug)
        printf("yydebug: state %d, reducing by rule %d (%s)\n",
                yystate, yyn, yyrule[yyn]);
#endif
    yym = yylen[yyn];
    yyval = yyvsp[1-yym];
    switch (yyn)
    {
case 1:
#line 22 "gramatica.y"
{ if (lastDeclared.size() != 0)
							tablaSimbolos.defineVar(lastDeclared, auxType, auxUso, ambito.toString()); 
					  }
break;
case 4:
#line 31 "gramatica.y"
{ 
													System.out.println("Declaración de Variables. Línea " + yyvsp[-2].ival + ". auxVariables: " + auxVariables); 
													uso = "variable";
													/*tablaSimbolos.defineVar(auxVariables, $3.sval, uso, ambito.toString());*/

													lastDeclared.addAll(auxVariables); /* las dejo en standby...*/
													auxType = yyvsp[-1].sval;
													auxUso = uso;

													/*auxVariables.clear();*/
												 }
break;
case 5:
#line 42 "gramatica.y"
{ yyerror("\tLínea " + yyvsp[-1].ival + ". Declaración de variables incompleta. Falta DOT"); }
break;
case 6:
#line 43 "gramatica.y"
{ yyerror("\tLínea " + yyvsp[0].ival + ". Declaración de variables incompleta. Falta COLON"); }
break;
case 7:
#line 46 "gramatica.y"
{ auxVariables.add(yyvsp[0].sval); }
break;
case 8:
#line 47 "gramatica.y"
{ auxVariables.add(yyvsp[0].sval); }
break;
case 9:
#line 48 "gramatica.y"
{ yyerror("\tLínea " + yyvsp[0].ival + ". Declaración incompleta. Falta COMMA"); }
break;
case 12:
#line 54 "gramatica.y"
{ 
															ambito.push(yyvsp[-1].sval);
															
															if (!tablaSimbolos.varDefined(yyvsp[-1].sval)) {
																auxVariables.removeAll(lastDeclared);
																System.out.println("Declaracion de funcion. Línea " + yyvsp[-2].ival + ". auxVariables: " + auxVariables);
																
																auxVariables.add(yyvsp[-1].sval);
																uso = "nombre_funcion";
																tablaSimbolos.defineVar(auxVariables, yyvsp[-3].sval, uso, ambito.toString());

																lastDeclared.clear();
																
																auxVariables.clear();
															} else {
																yyerror("\tLínea " + yyvsp[-2].ival + ". Redeclaración de funcion.");
															}

															ambito.pop();
													  }
break;
case 13:
#line 74 "gramatica.y"
{ yyerror("\tLínea " + yyvsp[-2].ival + ". Declaración de función incompleta. Falta tipo de retorno"); }
break;
case 14:
#line 75 "gramatica.y"
{ 
																if (!tablaSimbolos.varDefined(yyvsp[-1].sval)) {
																	System.out.println("Declaracion de funcion. Línea " + yyvsp[-2].ival);
																	auxVariables.clear();
																	auxVariables.add(yyvsp[-1].sval);
																	uso = "nombre_funcion";
																	tablaSimbolos.defineVar(auxVariables, yyvsp[-4].sval, uso, ambito.toString());
																} else {
																	yyerror("\tLínea " + yyvsp[-2].ival + ". Redeclaración de funcion.");
																}
														   }
break;
case 15:
#line 86 "gramatica.y"
{ yyerror("\tLínea " + yyvsp[-3].ival + ". Declaración de función incompleta. Falta tipo de retorno"); }
break;
case 18:
#line 91 "gramatica.y"
{ yyerror("\tLínea " + yyvsp[-2].ival + ". Declaración de función incompleta. Falta sentencia RETURN"); }
break;
case 26:
#line 100 "gramatica.y"
{ 
											System.out.println("Sentencia OUT. Línea " + yyvsp[-4].ival); 
											Terceto t = new Terceto("OUT", new ItemString(yyvsp[-2].sval), new ItemString("-"), null);
											tercetos.add(t);
										  }
break;
case 27:
#line 105 "gramatica.y"
{ yyerror("\tLínea " + yyvsp[-3].ival + ". Estructura OUT incompleta. Falta DOT"); }
break;
case 28:
#line 106 "gramatica.y"
{ yyerror("\tLínea " + yyvsp[-3].ival + ". Estructura OUT incompleta. Falta CLOSE_PAR"); }
break;
case 29:
#line 107 "gramatica.y"
{ yyerror("\tLínea " + yyvsp[-3].ival + ". Estructura OUT incompleta. Falta OPEN_PAR"); }
break;
case 30:
#line 108 "gramatica.y"
{ yyerror("\tLínea " + yyvsp[-4].ival + ". Estructura OUT incorrecta. Sólo se pueden imprimir cadenas"); }
break;
case 31:
#line 111 "gramatica.y"
{ System.out.println("ASIGNACIÓN. Línea " + yyvsp[-3].ival);
									  Item item2 = (Item) yyvsp[-1].obj;
									  Terceto t = null;	
									  if (! tablaSimbolos.varDefined(yyvsp[-3].sval))
									  	yyerror("\tError en la línea " + yyvsp[-3].ival + ": VARIABLE NO DEFINIDA"); 
									  else {
										  String tipoAsignacion = tablaSimbolos.devolverToken(yyvsp[-3].sval).getType();
										  String tipoExpresion = (String) ( ((Item) yyvsp[-1].obj).getTipo());
										  if (!tipoAsignacion.equals(tipoExpresion))
											  yyerror ("Línea " + yyvsp[-2].ival + ". Tipos incompatibles en la asignacion");
									  }
									  
									  t = new Terceto("=", new ItemString((String) yyvsp[-3].sval), item2, null);
								      tercetos.add(t);
									  yyval.obj = new ItemTerceto(t);
									  
									}
break;
case 32:
#line 128 "gramatica.y"
{ yyerror("\tLínea " + yyvsp[-2].ival + ". Asignación incompleta. Falta DOT"); }
break;
case 40:
#line 137 "gramatica.y"
{ 
																System.out.println("Línea " + yyvsp[-3].ival + ". Sentencia IF-ELSE"); 
																((Terceto)tercetos.get((pila.pop()).intValue()-1)).setOp_1(new ItemString("[" + (tercetos.size() + 1) + "]"));
															}
break;
case 41:
#line 141 "gramatica.y"
{ 
										System.out.println("Línea " + yyvsp[-1].ival + ". Sentencia IF"); 
										((Terceto) tercetos.get((pila.pop()).intValue() - 1)).setOp_2(new ItemString("[" + (tercetos.size() + 1) + "]"));
									}
break;
case 42:
#line 147 "gramatica.y"
{
				((Terceto) tercetos.get((pila.pop()).intValue() - 1)).setOp_2(new ItemString("[" + (tercetos.size() + 2) + "]"));
				Terceto t = new Terceto("BI", new ItemString("_"), new ItemString("_"), null);
				tercetos.add(t);
				pila.push(new Integer(t.getNumero()));
		   }
break;
case 44:
#line 158 "gramatica.y"
{
							Terceto t = new Terceto("BF", new ItemTerceto((Terceto) (tercetos.get(tercetos.size() - 1))), new ItemString("_"), null);
							tercetos.add(t);
							pila.push(new Integer(t.getNumero()));	
						 }
break;
case 45:
#line 165 "gramatica.y"
{ 
																System.out.println("Comparación. Línea " + yyvsp[-2].ival); 
																String tipo1=(String)( ((Item) yyvsp[-3].obj).getTipo());
																String tipo2=(String)( ((Item) yyvsp[-1].obj).getTipo());
				
																Item item1 = (Item) yyvsp[-3].obj;
																Item item2 = (Item) yyvsp[-1].obj;
																
																Terceto t = new Terceto(yyvsp[-2].sval, item1, item2, null);
																tercetos.add(t);												  
																yyval.obj = new ItemTerceto(t);
															  }
break;
case 46:
#line 177 "gramatica.y"
{ yyerror("Línea " + yyvsp[-2].ival + ". Condicion incompleta. Falta OPEN_PAR"); }
break;
case 47:
#line 178 "gramatica.y"
{ yyerror("Línea " + yyvsp[-1].ival + ". Condicion. Falta CLOSE_PAR"); }
break;
case 49:
#line 182 "gramatica.y"
{ System.out.println("Línea " + yyvsp[-2].ival + ". Bloque compuesto"); }
break;
case 50:
#line 183 "gramatica.y"
{ yyerror("Línea " + yyvsp[-2].ival + ". Bloque compuesto. Falta END"); }
break;
case 57:
#line 192 "gramatica.y"
{ 
															System.out.println("Línea " + yyvsp[-3].ival + ". Estructura WHILE"); 
															System.out.println(" FINALIZA iteracion. CONTENIDO DE LA STACK: " + pila.toString());
															((Terceto) tercetos.get((pila.pop()).intValue() - 1)).setOp_2(new ItemString("[" + (tercetos.size() + 2) + "]"));
															Terceto t = new Terceto("BI", new ItemTerceto((Terceto) tercetos.get((pila.pop()).intValue() - 1)), new ItemString("_"), null);
															tercetos.add(t);
													   }
break;
case 58:
#line 201 "gramatica.y"
{
				System.out.println(" INICIA while. CONTENIDO DE LA STACK: " + pila.toString());
				pila.push(new Integer(tercetos.size() + 1));
			 }
break;
case 59:
#line 207 "gramatica.y"
{
								System.out.println(" INICIA condicion_while. CONTENIDO DE LA STACK: " + pila.toString());
								Terceto t = new Terceto("BF", new ItemTerceto((Terceto) (tercetos.get(tercetos.size() - 1))), new ItemString("_"), null);
								/*t.setIsWhileFlag(true); // NO TIENE SENTIDO...*/
								tercetos.add(t);
								System.out.println("NÚMERO TERCETO: " + t.getNumero());
								pila.push(new Integer(t.getNumero()));
                            }
break;
case 60:
#line 217 "gramatica.y"
{ 
									System.out.println("SUMA. Línea " + yyvsp[-1].ival); 
									String tipo1 = (String)( ((Item) yyvsp[-2].obj).getTipo() );
									String tipo2 = (String)( ((Item) yyvsp[0].obj).getTipo() );
									Item item1 = (Item) yyvsp[-2].obj;
									Item item2 = (Item) yyvsp[0].obj;
									if (!tipo1.equals(tipo2))
										yyerror ("Línea " + yyvsp[-1].ival + ". Tipos incompatibles en la suma");
									Terceto t = new Terceto("+", item1, item2, tipo1);
									tercetos.add(t);
									yyval.obj = new ItemTerceto(t); 
								  }
break;
case 61:
#line 229 "gramatica.y"
{ 
									System.out.println("RESTA. Línea " + yyvsp[-1].ival); 
									String tipo1 = (String)( ((Item) yyvsp[-2].obj).getTipo() );
									String tipo2 = (String)( ((Item) yyvsp[0].obj).getTipo() );
									Item item1 = (Item) yyvsp[-2].obj;
									Item item2 = (Item) yyvsp[0].obj;
									if (!tipo1.equals(tipo2))
										yyerror ("Línea " + yyvsp[-1].ival + ". Tipos incompatibles en la resta");
									Terceto t = new Terceto("-", item1, item2, tipo1);
									tercetos.add(t);
									yyval.obj = new ItemTerceto(t);
								  }
break;
case 62:
#line 241 "gramatica.y"
{ yyval.obj = yyvsp[0].obj; }
break;
case 63:
#line 244 "gramatica.y"
{ 
								System.out.println("MULTIPLICACION. Línea " + yyvsp[-1].ival); 
								String tipo1 = (String)( ((Item) yyvsp[-2].obj).getTipo() );
								String tipo2 = (String)( ((Item) yyvsp[0].obj).getTipo() );
								Item item1 = (Item) yyvsp[-2].obj;
								Item item2 = (Item) yyvsp[0].obj;
								if (!tipo1.equals(tipo2))
									yyerror ("Línea " + yyvsp[-1].ival + ". Tipos incompatibles en la multiplicacion");
								Terceto t = new Terceto("*", item1, item2, tipo1);
								tercetos.add(t);
								yyval.obj = new ItemTerceto(t);
							  }
break;
case 64:
#line 256 "gramatica.y"
{ 
								System.out.println("DIVISION. Línea " + yyvsp[-1].ival); 
								String tipo1 = (String)( ((Item) yyvsp[-2].obj).getTipo() );
								String tipo2 = (String)( ((Item) yyvsp[0].obj).getTipo() );
								Item item1 = (Item) yyvsp[-2].obj;
								Item item2 = (Item) yyvsp[0].obj;
								if (!tipo1.equals(tipo2))
									yyerror ("Línea " + yyvsp[-1].ival + ". Tipos incompatibles en la division");
								Terceto t = new Terceto("/", item1, item2, tipo1);
								tercetos.add(t);
								yyval.obj = new ItemTerceto(t);
							 }
break;
case 65:
#line 268 "gramatica.y"
{ yyval.obj = yyvsp[0].obj; }
break;
case 66:
#line 271 "gramatica.y"
{ System.out.println("Lectura de la variable " + yyvsp[0].sval + ". Línea " + yyvsp[0].ival); 
			  if (! tablaSimbolos.varDefined(yyvsp[0].sval))
			  	yyerror("\tError en la línea " + yyvsp[0].ival + ": VARIABLE NO DEFINIDA"); 
			  String lex = yyvsp[0].sval;
			  ItemString item = new ItemString(lex);
			  item.setTabla(tablaSimbolos);
			  yyval.obj = item;
			}
break;
case 67:
#line 279 "gramatica.y"
{ String lex = yyvsp[0].sval;
			   ItemString is = new ItemString(lex);
			   is.setTabla(tablaSimbolos);
			   yyval.obj = is;
			 }
break;
case 68:
#line 284 "gramatica.y"
{ yyval.obj = yyvsp[0].obj; }
break;
case 75:
#line 295 "gramatica.y"
{ 
												if (! tablaSimbolos.varDefined(yyvsp[-2].sval))
													yyerror("\tError en la línea " + yyvsp[-2].ival + ": FUNCION NO DEFINIDA"); 
												else {
														System.out.println("Invocación a función. Línea " + yyvsp[-2].ival); 
														String lex = yyvsp[-2].sval;
														ItemString is = new ItemString(lex);
														is.setTabla(tablaSimbolos);
														yyval.obj = is;
													 }
											}
break;
#line 871 "y.tab.c"
    }
    yyssp -= yym;
    yystate = *yyssp;
    yyvsp -= yym;
    yym = yylhs[yyn];
    if (yystate == 0 && yym == 0)
    {
#if YYDEBUG
        if (yydebug)
            printf("yydebug: after reduction, shifting from state 0 to\
 state %d\n", YYFINAL);
#endif
        yystate = YYFINAL;
        *++yyssp = YYFINAL;
        *++yyvsp = yyval;
        if (yychar < 0)
        {
            if ((yychar = yylex()) < 0) yychar = 0;
#if YYDEBUG
            if (yydebug)
            {
                yys = 0;
                if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
                if (!yys) yys = "illegal-symbol";
                printf("yydebug: state %d, reading %d (%s)\n",
                        YYFINAL, yychar, yys);
            }
#endif
        }
        if (yychar == 0) goto yyaccept;
        goto yyloop;
    }
    if ((yyn = yygindex[yym]) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn];
    else
        yystate = yydgoto[yym];
#if YYDEBUG
    if (yydebug)
        printf("yydebug: after reduction, shifting from state %d \
to state %d\n", *yyssp, yystate);
#endif
    if (yyssp >= yyss + yystacksize - 1)
    {
        goto yyoverflow;
    }
    *++yyssp = yystate;
    *++yyvsp = yyval;
    goto yyloop;
yyoverflow:
    yyerror("yacc stack overflow");
yyabort:
    return (1);
yyaccept:
    return (0);
}
