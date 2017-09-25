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


public class Parser {

    boolean yydebug;        //do I want debug output?
    int yynerrs;            //number of errors so far
    int yyerrflag;          //was there an error?
    int yychar;             //the current working character

    //########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
    void debug(String msg) {
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
    final void state_push(int state) {
        try {
            stateptr++;
            statestk[stateptr] = state;
        } catch (ArrayIndexOutOfBoundsException e) {
            int oldsize = statestk.length;
            int newsize = oldsize * 2;
            int[] newstack = new int[newsize];
            System.arraycopy(statestk, 0, newstack, 0, oldsize);
            statestk = newstack;
            statestk[stateptr] = state;
        }
    }

    final int state_pop() {
        return statestk[stateptr--];
    }

    final void state_drop(int cnt) {
        stateptr -= cnt;
    }

    final int state_peek(int relative) {
        return statestk[stateptr - relative];
    }

    //###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
    final boolean init_stacks() {
        stateptr = -1;
        val_init();
        return true;
    }

    //###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
    void dump_stacks(int count) {
        int i;
        System.out.println("=index==state====value=     s:" + stateptr + "  v:" + valptr);
        for (i = 0; i < count; i++)
            System.out.println(" " + i + "    " + statestk[i] + "      " + valstk[i]);
        System.out.println("======================");
    }


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


    String yytext;//user variable to return contextual strings
    ParserVal yyval; //used to return semantic vals from action routines
    ParserVal yylval;//the 'lval' (result) I got from yylex()
    ParserVal valstk[];
    int valptr;

    //###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
    void val_init() {
        valstk = new ParserVal[YYSTACKSIZE];
        yyval = new ParserVal();
        yylval = new ParserVal();
        valptr = -1;
    }

    void val_push(ParserVal val) {
        if (valptr >= YYSTACKSIZE)
            return;
        valstk[++valptr] = val;
    }

    ParserVal val_pop() {
        if (valptr < 0)
            return new ParserVal();
        return valstk[valptr--];
    }

    void val_drop(int cnt) {
        int ptr;
        ptr = valptr - cnt;
        if (ptr < 0)
            return;
        valptr = ptr;
    }

    ParserVal val_peek(int relative) {
        int ptr;
        ptr = valptr - relative;
        if (ptr < 0)
            return new ParserVal();
        return valstk[ptr];
    }

    final ParserVal dup_yyval(ParserVal val) {
        ParserVal dup = new ParserVal();
        dup.ival = val.ival;
        dup.dval = val.dval;
        dup.sval = val.sval;
        dup.obj = val.obj;
        return dup;
    }

    //#### end semantic value section ####
    public final static short ID = 257;
    public final static short CTE = 258;
    public final static short ASIGN = 259;
    public final static short ADD = 260;
    public final static short SUB = 261;
    public final static short MULT = 262;
    public final static short DIV = 263;
    public final static short DOT = 264;
    public final static short BEGIN = 265;
    public final static short END = 266;
    public final static short COLON = 267;
    public final static short COMMA = 268;
    public final static short UINT = 269;
    public final static short ULONG = 270;
    public final static short IF = 271;
    public final static short OPEN_PAR = 272;
    public final static short CLOSE_PAR = 273;
    public final static short THEN = 274;
    public final static short ELSE = 275;
    public final static short END_IF = 276;
    public final static short LEQ = 277;
    public final static short GEQ = 278;
    public final static short LT = 279;
    public final static short GT = 280;
    public final static short EQ = 281;
    public final static short NEQ = 282;
    public final static short OUT = 283;
    public final static short CADENA = 284;
    public final static short FUNCTION = 285;
    public final static short MOVE = 286;
    public final static short OPEN_BRACE = 287;
    public final static short CLOSE_BRACE = 288;
    public final static short RETURN = 289;
    public final static short YYERRCODE = 256;
    final static short yylhs[] = {-1,
            0, 2, 2, 4, 4, 6, 1, 1, 7, 8,
            10, 10, 9, 9, 9, 11, 12, 12, 13, 13,
            13, 15, 16, 16, 16, 16, 16, 16, 14, 14,
            3, 3, 5, 5, 5, 5, 17, 17, 17, 18,
            18,
    };
    final static short yylen[] = {2,
            1, 4, 5, 8, 7, 3, 1, 1, 1, 3,
            2, 1, 1, 1, 1, 4, 4, 1, 7, 9,
            5, 3, 1, 1, 1, 1, 1, 1, 3, 1,
            1, 1, 1, 3, 3, 1, 3, 3, 1, 1,
            1,
    };
    final static short yydefred[] = {0,
            0, 0, 31, 32, 0, 0, 0, 1, 18, 0,
            7, 8, 9, 13, 14, 15, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 41, 0, 33,
            0, 39, 0, 29, 11, 10, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 16, 0, 0, 23, 24,
            25, 26, 27, 28, 0, 0, 0, 0, 2, 0,
            17, 6, 40, 0, 0, 37, 38, 0, 0, 21,
            0, 0, 3, 0, 0, 0, 0, 19, 0, 0,
            0, 0, 0, 20, 0, 0, 5, 0, 4,
    };
    final static short yydgoto[] = {7,
            8, 9, 10, 59, 29, 30, 11, 12, 13, 21,
            14, 15, 16, 17, 38, 55, 31, 32,
    };
    final static short yysindex[] = {-252,
            -229, -248, 0, 0, -264, -261, 0, 0, 0, -245,
            0, 0, 0, 0, 0, 0, -240, -210, -228, -248,
            -234, -210, -232, -203, -215, -219, -181, 0, -257, 0,
            -206, 0, -191, 0, 0, 0, -156, -173, -172, -184,
            -151, -157, -165, -190, -190, 0, -190, -190, 0, 0,
            0, 0, 0, 0, -210, -164, -155, -255, 0, -184,
            0, 0, 0, -206, -206, 0, 0, -189, -252, 0,
            -160, -176, 0, -202, -210, -158, -252, 0, -235, -210,
            -149, -136, -224, 0, -159, -134, 0, -154, 0,
    };
    final static short yyrindex[] = {0,
            -135, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, -133,
            0, 0, 0, 0, 0, 0, -218, 0, 0, 0,
            -195, 0, -135, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, -185, -162, 0, 0, -142, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
    };
    final static short yygindex[] = {0,
            -57, 0, 109, 76, -22, 0, 0, 0, 4, 117,
            0, 0, 0, 119, 0, 0, 36, 42,
    };
    final static int YYTABLESIZE = 138;
    static short yytable[];

    static {
        yytable();
    }

    static void yytable() {
        yytable = new short[]{37,
                72, 1, 44, 45, 1, 20, 46, 22, 1, 2,
                23, 74, 2, 3, 4, 5, 3, 4, 5, 81,
                3, 4, 5, 20, 44, 45, 26, 6, 33, 18,
                6, 36, 68, 71, 6, 44, 45, 82, 19, 24,
                25, 40, 40, 40, 40, 40, 27, 28, 86, 3,
                4, 39, 79, 40, 40, 47, 48, 83, 40, 40,
                40, 40, 40, 40, 36, 36, 63, 28, 36, 41,
                44, 45, 77, 78, 34, 34, 19, 36, 34, 64,
                65, 36, 36, 36, 36, 36, 36, 34, 66, 67,
                43, 34, 34, 34, 34, 34, 34, 35, 35, 56,
                57, 35, 58, 44, 45, 60, 61, 62, 70, 69,
                35, 75, 76, 80, 35, 35, 35, 35, 35, 35,
                49, 50, 51, 52, 53, 54, 84, 85, 87, 88,
                22, 30, 12, 89, 42, 73, 35, 34,
        };
    }

    static short yycheck[];

    static {
        yycheck();
    }

    static void yycheck() {
        yycheck = new short[]{22,
                58, 257, 260, 261, 257, 2, 264, 272, 257, 265,
                272, 69, 265, 269, 270, 271, 269, 270, 271, 77,
                269, 270, 271, 20, 260, 261, 267, 283, 257, 259,
                283, 266, 55, 289, 283, 260, 261, 273, 268, 285,
                286, 260, 261, 262, 263, 264, 257, 258, 273, 269,
                270, 284, 75, 257, 273, 262, 263, 80, 277, 278,
                279, 280, 281, 282, 260, 261, 257, 258, 264, 285,
                260, 261, 275, 276, 260, 261, 268, 273, 264, 44,
                45, 277, 278, 279, 280, 281, 282, 273, 47, 48,
                272, 277, 278, 279, 280, 281, 282, 260, 261, 273,
                273, 264, 287, 260, 261, 257, 264, 273, 264, 274,
                273, 272, 289, 272, 277, 278, 279, 280, 281, 282,
                277, 278, 279, 280, 281, 282, 276, 264, 288, 264,
                273, 267, 266, 288, 26, 60, 20, 19,
        };
    }

    final static short YYFINAL = 7;
    final static short YYMAXTOKEN = 289;
    final static String yyname[] = {
            "end-of-file", null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, "ID", "CTE", "ASIGN", "ADD", "SUB", "MULT", "DIV", "DOT", "BEGIN", "END",
            "COLON", "COMMA", "UINT", "ULONG", "IF", "OPEN_PAR", "CLOSE_PAR", "THEN", "ELSE",
            "END_IF", "LEQ", "GEQ", "LT", "GT", "EQ", "NEQ", "OUT", "CADENA", "FUNCTION", "MOVE",
            "OPEN_BRACE", "CLOSE_BRACE", "RETURN",
    };
    final static String yyrule[] = {
            "$accept : programa",
            "programa : bloque_sentencias",
            "funcion : tipo FUNCTION ID cuerpo_funcion",
            "funcion : tipo MOVE FUNCTION ID cuerpo_funcion",
            "cuerpo_funcion : OPEN_BRACE bloque_sentencias RETURN OPEN_PAR expresion CLOSE_PAR DOT CLOSE_BRACE",
            "cuerpo_funcion : OPEN_BRACE RETURN OPEN_PAR expresion CLOSE_PAR DOT CLOSE_BRACE",
            "invocacion_funcion : ID OPEN_PAR CLOSE_PAR",
            "bloque_sentencias : bloque_simple",
            "bloque_sentencias : bloque_compuesto",
            "bloque_simple : sentencia",
            "bloque_compuesto : BEGIN sentencias END",
            "sentencias : sentencia sentencias",
            "sentencias : sentencia",
            "sentencia : asignacion",
            "sentencia : declaracion",
            "sentencia : ejecutable",
            "asignacion : ID ASIGN expresion DOT",
            "declaracion : lista_variables COLON tipo DOT",
            "declaracion : funcion",
            "ejecutable : IF OPEN_PAR condicion CLOSE_PAR THEN bloque_sentencias END_IF",
            "ejecutable : IF OPEN_PAR condicion CLOSE_PAR THEN bloque_sentencias ELSE bloque_sentencias END_IF",
            "ejecutable : OUT OPEN_PAR CADENA CLOSE_PAR DOT",
            "condicion : expresion comparador expresion",
            "comparador : LEQ",
            "comparador : GEQ",
            "comparador : LT",
            "comparador : GT",
            "comparador : EQ",
            "comparador : NEQ",
            "lista_variables : ID COMMA lista_variables",
            "lista_variables : ID",
            "tipo : UINT",
            "tipo : ULONG",
            "expresion : invocacion_funcion",
            "expresion : expresion ADD termino",
            "expresion : expresion SUB termino",
            "expresion : termino",
            "termino : termino MULT factor",
            "termino : termino DIV factor",
            "termino : factor",
            "factor : ID",
            "factor : CTE",
    };

    //###############################################################
// method: yylexdebug : check lexer state
//###############################################################
    void yylexdebug(int state, int ch) {
        String s = null;
        if (ch < 0) ch = 0;
        if (ch <= YYMAXTOKEN) //check index bounds
            s = yyname[ch];    //now get it
        if (s == null)
            s = "illegal-symbol";
        debug("state " + state + ", reading " + ch + " (" + s + ")");
    }


    //The following are now global, to aid in error reporting
    int yyn;       //next next thing to do
    int yym;       //
    int yystate;   //current parsing state from state table
    String yys;    //current token string


    //###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
    int yyparse() {
        boolean doaction;
        init_stacks();
        yynerrs = 0;
        yyerrflag = 0;
        yychar = -1;          //impossible char forces a read
        yystate = 0;            //initial state
        state_push(yystate);  //save it
        val_push(yylval);     //save empty value
        while (true) //until parsing is done, either correctly, or w/error
        {
            doaction = true;
            if (yydebug) debug("loop");
            //#### NEXT ACTION (from reduction table)
            for (yyn = yydefred[yystate]; yyn == 0; yyn = yydefred[yystate]) {
                if (yydebug) debug("yyn:" + yyn + "  state:" + yystate + "  yychar:" + yychar);
                if (yychar < 0)      //we want a char?
                {
                    yychar = lexer.yylex();  //get next token
                    if (yydebug) debug(" next yychar:" + yychar);
                    //#### ERROR CHECK ####
                    if (yychar < 0)    //it it didn't work/error
                    {
                        yychar = 0;      //change it to default string (no -1!)
                        if (yydebug)
                            yylexdebug(yystate, yychar);
                    }
                }//yychar<0
                yyn = yysindex[yystate];  //get amount to shift by (shift index)
                if ((yyn != 0) && (yyn += yychar) >= 0 &&
                        yyn <= YYTABLESIZE && yycheck[yyn] == yychar) {
                    if (yydebug)
                        debug("state " + yystate + ", shifting to state " + yytable[yyn]);
                    //#### NEXT STATE ####
                    yystate = yytable[yyn];//we are in a new state
                    state_push(yystate);   //save it
                    val_push(yylval);      //push our lval as the input for next rule
                    yychar = -1;           //since we have 'eaten' a token, say we need another
                    if (yyerrflag > 0)     //have we recovered an error?
                        --yyerrflag;        //give ourselves credit
                    doaction = false;        //but don't process yet
                    break;   //quit the yyn=0 loop
                }

                yyn = yyrindex[yystate];  //reduce
                if ((yyn != 0) && (yyn += yychar) >= 0 &&
                        yyn <= YYTABLESIZE && yycheck[yyn] == yychar) {   //we reduced!
                    if (yydebug) debug("reduce");
                    yyn = yytable[yyn];
                    doaction = true; //get ready to execute
                    break;         //drop down to actions
                } else //ERROR RECOVERY
                {
                    if (yyerrflag == 0) {
                        lexer.yyerror("syntax error");
                        yynerrs++;
                    }
                    if (yyerrflag < 3) //low error count?
                    {
                        yyerrflag = 3;
                        while (true)   //do until break
                        {
                            if (stateptr < 0)   //check for under & overflow here
                            {
                                lexer.yyerror("stack underflow. aborting...");  //note lower case 's'
                                return 1;
                            }
                            yyn = yysindex[state_peek(0)];
                            if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE) {
                                if (yydebug)
                                    debug("state " + state_peek(0) + ", error recovery shifting to state " + yytable[yyn] + " ");
                                yystate = yytable[yyn];
                                state_push(yystate);
                                val_push(yylval);
                                doaction = false;
                                break;
                            } else {
                                if (yydebug)
                                    debug("error recovery discarding state " + state_peek(0) + " ");
                                if (stateptr < 0)   //check for under & overflow here
                                {
                                    lexer.yyerror("Stack underflow. aborting...");  //capital 'S'
                                    return 1;
                                }
                                state_pop();
                                val_pop();
                            }
                        }
                    } else            //discard this token
                    {
                        if (yychar == 0)
                            return 1; //yyabort
                        if (yydebug) {
                            yys = null;
                            if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
                            if (yys == null) yys = "illegal-symbol";
                            debug("state " + yystate + ", error recovery discards token " + yychar + " (" + yys + ")");
                        }
                        yychar = -1;  //read another
                    }
                }//end error recovery
            }//yyn=0 loop
            if (!doaction)   //any reason not to proceed?
                continue;      //skip action
            yym = yylen[yyn];          //get count of terminals on rhs
            if (yydebug)
                debug("state " + yystate + ", reducing " + yym + " by rule " + yyn + " (" + yyrule[yyn] + ")");
            if (yym > 0)                 //if count of rhs not 'nil'
                yyval = val_peek(yym - 1); //get current semantic value
            yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
            switch (yyn) {
//########## USER-SUPPLIED ACTIONS ##########
                case 16:
//#line 34 "gramatica.y"
                {
                    System.out.println("Asignación");
                }
                break;
                case 17:
//#line 37 "gramatica.y"
                {
                    System.out.println("Declaración de variables");
                }
                break;
                case 18:
//#line 37 "gramatica.y"
                {
                    System.out.println("Declaración de función");
                }
                break;
                case 19:
//#line 40 "gramatica.y"
                {
                    System.out.println("Sentencia IF");
                }
                break;
                case 20:
//#line 41 "gramatica.y"
                {
                    System.out.println("Sentencia IF");
                }
                break;
                case 21:
//#line 42 "gramatica.y"
                {
                    System.out.println("Sentencia OUT");
                }
                break;
//#line 456 "Parser.java"
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
                if (yydebug) debug("After reduction, shifting from state 0 to state " + YYFINAL + "");
                yystate = YYFINAL;         //explicitly say we're done
                state_push(YYFINAL);       //and save it
                val_push(yyval);           //also save the semantic value of parsing
                if (yychar < 0)            //we want another character?
                {
                    yychar = lexer.yylex();        //get next character
                    if (yychar < 0) yychar = 0;  //clean, if necessary
                    if (yydebug)
                        yylexdebug(yystate, yychar);
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
                if (yydebug)
                    debug("after reduction, shifting from state " + state_peek(0) + " to state " + yystate + "");
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
    public void run() {
        yyparse();
    }
//## end of method run() ########################################


//## Constructors ###############################################

    /**
     * Default constructor.  Turn off with -Jnoconstruct .
     */
    public Parser(Lexer lexer) {
        //nothing to do
        this.lexer = lexer;
    }

    private Lexer lexer;


    /**
     * Create a parser, setting the debug to true or false.
     *
     * @param debugMe true for debugging, false for no debug.
     */
    public Parser(boolean debugMe) {
        yydebug = debugMe;
    }
//###############################################################


}
//################### END OF CLASS ##############################
