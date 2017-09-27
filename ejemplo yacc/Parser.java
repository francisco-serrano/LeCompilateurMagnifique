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
    0,    2,    2,    3,    3,    4,    4,    7,    8,    8,
    9,   10,   11,   11,   12,   12,   12,   15,   13,    6,
    6,    6,   17,   17,   17,   18,   18,   18,   16,   16,
   16,   16,   16,   16,   19,   14,   14,    1,    1,    1,
    5,    5,   20,   21,   22,   22,   23,   23,   23,
};
final static short yylen[] = {                            2,
    1,    4,    5,    1,    1,    8,    7,    3,    1,    1,
    4,    1,    3,    1,    9,    7,    5,    6,    3,    3,
    3,    1,    3,    3,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    4,    1,    1,    2,    1,    1,
    1,    2,    3,    1,    2,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    4,    5,    0,    0,    0,    0,    1,   12,
    0,    0,    9,   10,    0,   48,   40,   49,   47,   36,
   37,   44,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   38,    0,    0,   27,    0,   28,    0,   25,
    0,   13,   43,   45,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   35,    0,    0,   29,   30,   31,
   32,   33,   34,    0,    0,    0,    0,    0,    2,    0,
   11,    8,    0,    0,   23,   24,    0,    0,   17,    0,
    0,    0,    0,   41,    3,    0,   18,    0,    0,   42,
    0,   16,    0,    0,    0,    0,    0,   15,    0,    0,
    7,    0,    6,
};
final static short yydgoto[] = {                          8,
    9,   10,   11,   69,   82,   45,   38,   12,   13,   14,
   15,   16,   46,   17,   18,   64,   39,   40,   19,   20,
   21,   26,   22,
};
final static short yysindex[] = {                      -256,
 -249, -250,    0,    0, -270, -264, -244,    0,    0,    0,
 -263, -256,    0,    0, -251,    0,    0,    0,    0,    0,
    0,    0, -183, -209, -204, -205, -250, -183, -226, -183,
 -179, -212,    0, -206, -182,    0, -208,    0, -181,    0,
 -157,    0,    0,    0, -235, -150, -149, -148, -155, -124,
 -130, -138, -183, -183,    0, -183, -183,    0,    0,    0,
    0,    0,    0, -183, -137, -128, -153, -254,    0, -155,
    0,    0, -181, -181,    0,    0, -159, -253,    0, -253,
 -133, -147, -233,    0,    0, -170,    0, -183, -132,    0,
 -253,    0, -201, -183, -135, -121, -169,    0, -144, -119,
    0, -142,    0,
};
final static short yyrindex[] = {                         0,
 -120,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  148,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -117,    0,    0,    0,
    0,    0,    0,    0, -194,    0,    0,    0, -184,    0,
 -120,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -161, -151,    0,    0, -123,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
  139,    0,  118,   83,   71,  -23,    0,    0,  -63,    0,
  131,    0,  126,  -29,    0,    0,   54,   58,    0,    0,
    0,  130,    4,
};
final static int YYTABLESIZE=157;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         37,
    1,   28,    1,   25,   83,   27,   25,   29,    2,   23,
    2,    2,    3,    4,    5,   34,    5,    5,   24,   83,
    5,   31,   32,    1,   53,   54,    6,   30,    6,    6,
   27,    2,    6,    7,   81,    7,    7,    5,   84,    7,
   77,   58,   59,   60,   61,   62,   63,   41,   86,    6,
   87,   53,   54,   84,   23,   55,    7,   47,   53,   54,
   43,   95,    3,    4,   93,   26,   26,   26,   26,   26,
   97,   96,   50,   35,   36,   22,   22,   49,   26,   22,
   56,   57,   26,   26,   26,   26,   26,   26,   22,   52,
   53,   54,   22,   22,   22,   22,   22,   22,   20,   20,
   53,   54,   20,  100,   91,   92,   73,   74,   21,   21,
   24,   20,   21,   75,   76,   20,   20,   20,   20,   20,
   20,   21,   65,   66,   67,   21,   21,   21,   21,   21,
   21,   68,   70,   71,   72,   79,   78,   80,   88,   94,
   98,   89,   99,  101,  102,  103,   14,   39,   46,   19,
   33,   51,   85,   90,   42,   48,   44,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         23,
  257,  272,  257,  257,   68,    2,  257,  272,  265,  259,
  265,  265,  269,  270,  271,  267,  271,  271,  268,   83,
  271,  285,  286,  257,  260,  261,  283,  272,  283,  283,
   27,  265,  283,  290,  289,  290,  290,  271,   68,  290,
   64,  277,  278,  279,  280,  281,  282,  257,   78,  283,
   80,  260,  261,   83,  259,  264,  290,  284,  260,  261,
  266,   91,  269,  270,   88,  260,  261,  262,  263,  264,
   94,  273,  285,  257,  258,  260,  261,  257,  273,  264,
  262,  263,  277,  278,  279,  280,  281,  282,  273,  272,
  260,  261,  277,  278,  279,  280,  281,  282,  260,  261,
  260,  261,  264,  273,  275,  276,   53,   54,  260,  261,
  268,  273,  264,   56,   57,  277,  278,  279,  280,  281,
  282,  273,  273,  273,  273,  277,  278,  279,  280,  281,
  282,  287,  257,  264,  273,  264,  274,  291,  272,  272,
  276,  289,  264,  288,  264,  288,  267,    0,  266,  273,
   12,   34,   70,   83,   24,   30,   27,
};
}
final static short YYFINAL=8;
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
"funcion : tipo MOVE FUNCTION ID cuerpo_funcion",
"tipo : UINT",
"tipo : ULONG",
"cuerpo_funcion : OPEN_BRACE bloque_funcion RETURN OPEN_PAR expresion CLOSE_PAR DOT CLOSE_BRACE",
"cuerpo_funcion : OPEN_BRACE RETURN OPEN_PAR expresion CLOSE_PAR DOT CLOSE_BRACE",
"invocacion_funcion : ID OPEN_PAR CLOSE_PAR",
"declaracion : declaracion_variables",
"declaracion : declaracion_funcion",
"declaracion_variables : lista_variables COLON tipo DOT",
"declaracion_funcion : funcion",
"lista_variables : ID COMMA lista_variables",
"lista_variables : ID",
"ejecutable : IF OPEN_PAR condicion CLOSE_PAR THEN bloque_sentencias ELSE bloque_sentencias END_IF",
"ejecutable : IF OPEN_PAR condicion CLOSE_PAR THEN bloque_sentencias END_IF",
"ejecutable : OUT OPEN_PAR CADENA CLOSE_PAR DOT",
"control : WHILE OPEN_PAR condicion CLOSE_PAR DO bloque_sentencias",
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
"bloque_sentencias : bloque_compuesto",
"bloque_sentencias : bloque_simple",
"bloque : declaracion bloque",
"bloque : declaracion",
"bloque : bloque_sentencias",
"bloque_funcion : bloque_sentencias",
"bloque_funcion : declaracion_variables bloque_funcion",
"bloque_compuesto : BEGIN sentencias END",
"bloque_simple : sentencia",
"sentencias : sentencia sentencias",
"sentencias : sentencia",
"sentencia : asignacion",
"sentencia : ejecutable",
"sentencia : control",
};

//#line 101 "gramatica.y"

void yyerror(String error) {
	lexer.yyerror(error);
}

int yylex() {
	int val = lexer.yylex();
	
	yylval = new ParserVal();
	yylval.ival = lexer.getCurrentLine();

	return val;
}

public void setLexico(Lexer lexer) {
	this.lexer = lexer;
}



Lexer lexer;
//#line 328 "Parser.java"
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
case 8:
//#line 20 "gramatica.y"
{ System.out.println("Invocación a función. Línea " + val_peek(2).ival); }
break;
case 11:
//#line 27 "gramatica.y"
{ System.out.println("Declaración de Variables. Línea " + val_peek(2).ival); }
break;
case 12:
//#line 30 "gramatica.y"
{ System.out.println("Declaración de Función. Línea " + val_peek(0).ival); }
break;
case 15:
//#line 37 "gramatica.y"
{ System.out.println("Sentencia IF. Línea " + val_peek(8).ival); }
break;
case 16:
//#line 38 "gramatica.y"
{ System.out.println("Sentencia IF. Línea " + val_peek(6).ival); }
break;
case 17:
//#line 39 "gramatica.y"
{ System.out.println("Sentencia OUT. Línea " + val_peek(4).ival); }
break;
case 18:
//#line 42 "gramatica.y"
{ System.out.println("Sentencia WHILE. Línea " + val_peek(5).ival); }
break;
case 19:
//#line 45 "gramatica.y"
{ System.out.println("Comparación. Línea " + val_peek(2).ival); }
break;
case 20:
//#line 48 "gramatica.y"
{ System.out.println("SUMA. Línea " + val_peek(1).ival); }
break;
case 21:
//#line 49 "gramatica.y"
{ System.out.println("RESTA. Línea " + val_peek(1).ival); }
break;
case 23:
//#line 53 "gramatica.y"
{ System.out.println("MULTIPLICACION. Línea " + val_peek(1).ival); }
break;
case 24:
//#line 54 "gramatica.y"
{ System.out.println("DIVISION. Línea " + val_peek(1).ival); }
break;
case 35:
//#line 71 "gramatica.y"
{ System.out.println("Asignación. Línea " + val_peek(3).ival); }
break;
//#line 529 "Parser.java"
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
