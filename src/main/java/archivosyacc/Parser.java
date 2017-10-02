package archivosyacc;//### This file created by BYACC 1.8(/Java extension  1.15)
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

import lexer.TablaSimbolos;
import lexer.Lexer;

import java.util.ArrayList;
import java.util.List;

//#line 21 "Parser.java"


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
    public final static short WHILE = 290;
    public final static short DO = 291;
    public final static short YYERRCODE = 256;
    final static short yylhs[] = {-1,
            0, 2, 2, 2, 2, 3, 3, 4, 4, 4,
            7, 8, 8, 9, 9, 9, 10, 11, 11, 11,
            12, 12, 12, 12, 12, 12, 12, 12, 12, 12,
            12, 12, 12, 15, 15, 15, 13, 6, 6, 6,
            17, 17, 17, 18, 18, 18, 16, 16, 16, 16,
            16, 16, 19, 19, 1, 1, 5, 5, 14, 14,
            23, 22, 20, 20, 20, 20, 21, 21, 24, 24,
            24,
    };
    final static short yylen[] = {2,
            1, 4, 3, 5, 4, 1, 1, 8, 7, 3,
            3, 1, 1, 4, 3, 3, 1, 3, 1, 2,
            9, 7, 7, 6, 6, 9, 8, 8, 5, 4,
            4, 4, 5, 6, 5, 5, 3, 3, 3, 1,
            3, 3, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 4, 3, 2, 1, 1, 2, 1, 1,
            3, 1, 1, 1, 1, 1, 2, 1, 1, 1,
            1,
    };
    final static short yydefred[] = {0,
            0, 6, 7, 0, 0, 0, 0, 0, 0, 1,
            17, 0, 66, 12, 13, 0, 64, 65, 63, 0,
            0, 0, 0, 20, 0, 45, 0, 0, 46, 0,
            0, 43, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 55, 0, 18, 0, 0, 0, 0, 47,
            48, 49, 50, 51, 52, 0, 0, 0, 0, 0,
            0, 0, 0, 3, 0, 0, 0, 0, 0, 0,
            16, 53, 11, 0, 0, 0, 0, 0, 0, 41,
            42, 31, 0, 0, 32, 0, 0, 0, 70, 71,
            69, 57, 0, 5, 0, 0, 0, 2, 0, 14,
            0, 0, 0, 0, 59, 60, 62, 0, 29, 33,
            0, 10, 0, 58, 67, 0, 36, 35, 4, 0,
            0, 0, 24, 0, 25, 0, 0, 34, 23, 0,
            22, 61, 0, 0, 0, 0, 0, 27, 28, 0,
            0, 26, 21, 9, 0, 8,
    };
    final static short yydgoto[] = {9,
            10, 11, 12, 64, 87, 28, 29, 13, 14, 15,
            16, 89, 30, 104, 90, 56, 31, 32, 91, 20,
            92, 105, 106, 107,
    };
    final static short yysindex[] = {-221,
            -138, 0, 0, -154, -262, -245, -264, -150, 0, 0,
            0, -144, 0, 0, 0, -136, 0, 0, 0, -221,
            -241, -114, -228, 0, -216, 0, -114, -180, 0, -214,
            -116, 0, -244, -177, -226, -146, -114, -156, -128, -165,
            -121, -139, 0, -137, 0, -141, -123, -114, -114, 0,
            0, 0, 0, 0, 0, -114, -110, -114, -114, -256,
            -203, -124, -248, 0, -226, -179, -155, -226, -92, -98,
            0, 0, 0, -107, -239, -116, -116, -108, -239, 0,
            0, 0, -96, -95, 0, -101, -134, -211, 0, 0,
            0, 0, -205, 0, -118, -239, -239, 0, -226, 0,
            -239, -87, -205, -119, 0, 0, 0, -117, 0, 0,
            -114, 0, -97, 0, 0, -239, 0, 0, 0, -193,
            -90, -239, 0, -239, 0, -147, -114, 0, 0, -239,
            0, 0, -102, -99, -86, -145, -189, 0, 0, -109,
            -84, 0, 0, 0, -106, 0,
    };
    final static short yyrindex[] = {0,
            -132, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 181,
            -132, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            37, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 145, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 139,
            0, 0, 0, 0, 0, 73, 109, -181, 0, 0,
            0, 0, 170, 0, 0, 0, 0, 0, 0, 0,
            0, 0, -212, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
    };
    final static short yygindex[] = {0,
            163, 0, -11, -61, 96, -22, 0, 0, -57, 0,
            67, 71, 47, -77, 75, 0, 112, 104, 86, 0,
            -78, 0, 0, -60,
    };
    final static int YYTABLESIZE = 460;
    static short yytable[];

    static {
        yytable();
    }

    static void yytable() {
        yytable = new short[]{44,
                44, 108, 93, 94, 42, 88, 98, 82, 1, 33,
                61, 35, 25, 26, 115, 21, 83, 102, 117, 118,
                36, 34, 4, 120, 121, 103, 23, 93, 21, 70,
                88, 4, 93, 78, 5, 1, 40, 119, 128, 60,
                86, 8, 93, 5, 133, 1, 134, 2, 3, 4,
                8, 102, 137, 68, 38, 46, 48, 49, 57, 4,
                63, 5, 129, 6, 7, 4, 142, 24, 8, 84,
                17, 5, 38, 47, 18, 68, 68, 5, 8, 48,
                49, 130, 131, 66, 8, 19, 143, 24, 126, 45,
                17, 37, 37, 95, 18, 62, 50, 51, 52, 53,
                54, 55, 25, 26, 136, 19, 25, 26, 39, 37,
                65, 96, 48, 49, 48, 49, 67, 27, 21, 69,
                22, 37, 48, 49, 71, 135, 72, 141, 68, 23,
                41, 73, 2, 3, 19, 97, 19, 19, 15, 85,
                39, 40, 25, 26, 54, 58, 59, 2, 3, 74,
                75, 48, 49, 112, 113, 122, 123, 124, 125, 76,
                77, 80, 81, 79, 99, 100, 101, 109, 110, 30,
                111, 22, 116, 138, 127, 132, 139, 140, 144, 145,
                56, 146, 43, 114, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 44, 44, 0, 0,
                44, 44, 44, 44, 44, 0, 44, 0, 0, 44,
                44, 44, 0, 44, 44, 44, 44, 44, 44, 44,
                44, 44, 44, 44, 0, 44, 44, 0, 44, 44,
                44, 44, 40, 40, 0, 0, 40, 40, 0, 0,
                40, 0, 40, 0, 0, 40, 40, 40, 0, 40,
                40, 40, 40, 40, 40, 40, 40, 40, 40, 40,
                0, 40, 40, 0, 40, 40, 40, 40, 38, 38,
                0, 0, 38, 38, 0, 0, 38, 0, 38, 0,
                0, 38, 38, 38, 0, 38, 38, 38, 38, 38,
                38, 38, 38, 38, 38, 38, 0, 38, 38, 0,
                38, 38, 38, 38, 39, 39, 0, 0, 39, 39,
                0, 0, 39, 0, 39, 0, 0, 39, 39, 39,
                0, 39, 39, 39, 39, 39, 39, 39, 39, 39,
                39, 39, 0, 39, 39, 15, 39, 39, 39, 39,
                54, 54, 0, 0, 0, 0, 0, 15, 15, 15,
                54, 0, 0, 54, 54, 54, 0, 0, 0, 54,
                54, 15, 0, 15, 15, 30, 30, 54, 15, 54,
                54, 0, 54, 54, 54, 30, 0, 0, 30, 30,
                30, 0, 0, 0, 30, 30, 0, 0, 0, 0,
                0, 0, 30, 0, 30, 30, 0, 30, 30, 30,
        };
    }

    static short yycheck[];

    static {
        yycheck();
    }

    static void yycheck() {
        yycheck = new short[]{22,
                0, 79, 63, 65, 16, 63, 68, 264, 257, 272,
                33, 257, 257, 258, 93, 257, 273, 257, 96, 97,
                285, 284, 271, 101, 103, 265, 268, 88, 257, 41,
                88, 271, 93, 56, 283, 257, 0, 99, 116, 284,
                289, 290, 103, 283, 122, 257, 124, 269, 270, 271,
                290, 257, 130, 266, 8, 272, 260, 261, 273, 271,
                287, 283, 256, 285, 286, 271, 256, 1, 290, 273,
                0, 283, 0, 27, 0, 288, 289, 283, 290, 260,
                261, 275, 276, 37, 290, 0, 276, 21, 111, 23,
                20, 273, 274, 273, 20, 273, 277, 278, 279, 280,
                281, 282, 257, 258, 127, 20, 257, 258, 0, 291,
                257, 291, 260, 261, 260, 261, 273, 272, 257, 285,
                259, 272, 260, 261, 264, 273, 264, 273, 257, 268,
                267, 273, 269, 270, 267, 291, 269, 270, 0, 264,
                285, 286, 257, 258, 0, 262, 263, 269, 270, 273,
                274, 260, 261, 288, 289, 275, 276, 275, 276, 48,
                49, 58, 59, 274, 257, 264, 274, 264, 264, 0,
                272, 259, 291, 276, 272, 266, 276, 264, 288, 264,
                0, 288, 20, 88, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, 256, 257, -1, -1,
                260, 261, 262, 263, 264, -1, 266, -1, -1, 269,
                270, 271, -1, 273, 274, 275, 276, 277, 278, 279,
                280, 281, 282, 283, -1, 285, 286, -1, 288, 289,
                290, 291, 256, 257, -1, -1, 260, 261, -1, -1,
                264, -1, 266, -1, -1, 269, 270, 271, -1, 273,
                274, 275, 276, 277, 278, 279, 280, 281, 282, 283,
                -1, 285, 286, -1, 288, 289, 290, 291, 256, 257,
                -1, -1, 260, 261, -1, -1, 264, -1, 266, -1,
                -1, 269, 270, 271, -1, 273, 274, 275, 276, 277,
                278, 279, 280, 281, 282, 283, -1, 285, 286, -1,
                288, 289, 290, 291, 256, 257, -1, -1, 260, 261,
                -1, -1, 264, -1, 266, -1, -1, 269, 270, 271,
                -1, 273, 274, 275, 276, 277, 278, 279, 280, 281,
                282, 283, -1, 285, 286, 257, 288, 289, 290, 291,
                256, 257, -1, -1, -1, -1, -1, 269, 270, 271,
                266, -1, -1, 269, 270, 271, -1, -1, -1, 275,
                276, 283, -1, 285, 286, 256, 257, 283, 290, 285,
                286, -1, 288, 289, 290, 266, -1, -1, 269, 270,
                271, -1, -1, -1, 275, 276, -1, -1, -1, -1,
                -1, -1, 283, -1, 285, 286, -1, 288, 289, 290,
        };
    }

    final static short YYFINAL = 9;
    final static short YYMAXTOKEN = 291;
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
            "OPEN_BRACE", "CLOSE_BRACE", "RETURN", "WHILE", "DO",
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
            "ejecutable : IF OPEN_PAR condicion CLOSE_PAR THEN bloque_control ELSE bloque_control END_IF",
            "ejecutable : IF OPEN_PAR condicion CLOSE_PAR THEN bloque_control END_IF",
            "ejecutable : IF OPEN_PAR condicion CLOSE_PAR THEN bloque_control error",
            "ejecutable : IF OPEN_PAR condicion THEN bloque_control END_IF",
            "ejecutable : IF condicion CLOSE_PAR THEN bloque_control END_IF",
            "ejecutable : IF OPEN_PAR condicion CLOSE_PAR THEN bloque_control ELSE bloque_control error",
            "ejecutable : IF OPEN_PAR condicion THEN bloque_control ELSE bloque_control END_IF",
            "ejecutable : IF condicion CLOSE_PAR THEN bloque_control ELSE bloque_control END_IF",
            "ejecutable : OUT OPEN_PAR CADENA CLOSE_PAR DOT",
            "ejecutable : OUT OPEN_PAR CADENA CLOSE_PAR",
            "ejecutable : OUT OPEN_PAR CADENA DOT",
            "ejecutable : OUT CADENA CLOSE_PAR DOT",
            "ejecutable : OUT OPEN_PAR expresion CLOSE_PAR DOT",
            "control : WHILE OPEN_PAR condicion CLOSE_PAR DO bloque_control",
            "control : WHILE condicion CLOSE_PAR DO bloque_control",
            "control : WHILE OPEN_PAR condicion DO bloque_control",
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
            "bloque : sentencia_bloque bloque",
            "bloque : sentencia_bloque",
            "bloque_funcion : sentencias",
            "bloque_funcion : declaracion_variables bloque_funcion",
            "bloque_control : bloque_simple",
            "bloque_control : bloque_compuesto",
            "bloque_compuesto : BEGIN sentencias END",
            "bloque_simple : sentencia",
            "sentencia_bloque : asignacion",
            "sentencia_bloque : ejecutable",
            "sentencia_bloque : control",
            "sentencia_bloque : declaracion",
            "sentencias : sentencia sentencias",
            "sentencias : sentencia",
            "sentencia : asignacion",
            "sentencia : ejecutable",
            "sentencia : control",
    };

//#line 167 "gramatica.y"

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
        if (lexer == null)
            throw new IllegalArgumentException("El Analizador Léxico no puede ser nulo");

        this.lexer = lexer;
    }

    public void setTablaSimbolos(TablaSimbolos ts) {
        this.tablaSimbolos = ts;
    }


    Lexer lexer;
    TablaSimbolos tablaSimbolos;
    List<String> auxVariables = new ArrayList<>();

    //#line 439 "Parser.java"
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
    public int yyparse() {
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
                    yychar = yylex();  //get next token
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
                        yyerror("syntax error");
                        yynerrs++;
                    }
                    if (yyerrflag < 3) //low error count?
                    {
                        yyerrflag = 3;
                        while (true)   //do until break
                        {
                            if (stateptr < 0)   //check for under & overflow here
                            {
                                yyerror("stack underflow. aborting...");  //note lower case 's'
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
                                    yyerror("Stack underflow. aborting...");  //capital 'S'
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
                case 3:
//#line 16 "gramatica.y"
                {
                    yyerror("\tLínea " + val_peek(2).ival + ". Declaración de función incompleta. Falta tipo de retorno");
                }
                break;
                case 5:
//#line 19 "gramatica.y"
                {
                    yyerror("\tLínea " + val_peek(3).ival + ". Declaración de función incompleta. Falta tipo de retorno");
                }
                break;
                case 10:
//#line 27 "gramatica.y"
                {
                    yyerror("\tLínea " + val_peek(2).ival + ". Declaración de función incompleta. Falta sentencia RETURN");
                }
                break;
                case 11:
//#line 30 "gramatica.y"
                {
                    System.out.println("Invocación a función. Línea " + val_peek(2).ival);
                }
                break;
                case 14:
//#line 37 "gramatica.y"
                {
                    System.out.println("Declaración de Variables. Línea " + val_peek(2).ival);
                    tablaSimbolos.defineVar(auxVariables, val_peek(1).sval);
                    auxVariables.clear();
                }
                break;
                case 15:
//#line 42 "gramatica.y"
                {
                    yyerror("\tLínea " + val_peek(1).ival + ". Declaración de variables incompleta. Falta DOT");
                }
                break;
                case 16:
//#line 43 "gramatica.y"
                {
                    yyerror("\tLínea " + val_peek(0).ival + ". Declaración de variables incompleta. Falta COLON");
                }
                break;
                case 17:
//#line 46 "gramatica.y"
                {
                    System.out.println("Declaración de Función. Línea " + val_peek(0).ival);
                }
                break;
                case 18:
//#line 49 "gramatica.y"
                { /*tablaSimbolos.defineVar($1.sval); tablaSimbolos.defineVar($3.sval);*/
                    auxVariables.add(val_peek(2).sval);
                }
                break;
                case 19:
//#line 50 "gramatica.y"
                {
                    auxVariables.add(val_peek(0).sval);
                }
                break;
                case 20:
//#line 51 "gramatica.y"
                {
                    yyerror("\tLínea " + val_peek(1).ival + ". Declaración incompleta. Falta COMMA");
                }
                break;
                case 21:
//#line 54 "gramatica.y"
                {
                    System.out.println("Línea " + val_peek(8).ival + ". Sentencia IF");
                }
                break;
                case 22:
//#line 55 "gramatica.y"
                {
                    System.out.println("Línea " + val_peek(6).ival + ". Sentencia IF");
                }
                break;
                case 23:
//#line 56 "gramatica.y"
                {
                    yyerror("\tLínea " + val_peek(6).ival + ". Estructura IF incompleta. Falta END_IF");
                }
                break;
                case 24:
//#line 57 "gramatica.y"
                {
                    yyerror("\tLínea " + val_peek(5).ival + ". Estructura IF incompleta. Falta CLOSE_PAR");
                }
                break;
                case 25:
//#line 58 "gramatica.y"
                {
                    yyerror("\tLínea " + val_peek(5).ival + ". Estructura IF incompleta. Falta OPEN_PAR");
                }
                break;
                case 26:
//#line 60 "gramatica.y"
                {
                    yyerror("\tLínea " + val_peek(8).ival + ". Estructura IF incompleta. Falta END_IF");
                }
                break;
                case 27:
//#line 61 "gramatica.y"
                {
                    yyerror("\tLínea " + val_peek(7).ival + ". Estructura IF incompleta. Falta CLOSE_PAR");
                }
                break;
                case 28:
//#line 62 "gramatica.y"
                {
                    yyerror("\tLínea " + val_peek(7).ival + ". Estructura IF incompleta. Falta OPEN_PAR");
                }
                break;
                case 29:
//#line 64 "gramatica.y"
                {
                    System.out.println("Sentencia OUT. Línea " + val_peek(4).ival);
                }
                break;
                case 30:
//#line 65 "gramatica.y"
                {
                    yyerror("\tLínea " + val_peek(3).ival + ". Estructura OUT incompleta. Falta DOT");
                }
                break;
                case 31:
//#line 66 "gramatica.y"
                {
                    yyerror("\tLínea " + val_peek(3).ival + ". Estructura OUT incompleta. Falta CLOSE_PAR");
                }
                break;
                case 32:
//#line 67 "gramatica.y"
                {
                    yyerror("\tLínea " + val_peek(3).ival + ". Estructura OUT incompleta. Falta OPEN_PAR");
                }
                break;
                case 33:
//#line 68 "gramatica.y"
                {
                    yyerror("\tLínea " + val_peek(4).ival + ". Estructura OUT incorrecta. Sólo se pueden imprimir cadenas");
                }
                break;
                case 34:
//#line 71 "gramatica.y"
                {
                    System.out.println("Línea " + val_peek(5).ival + ". Estructura WHILE");
                }
                break;
                case 35:
//#line 72 "gramatica.y"
                {
                    yyerror("Línea " + val_peek(4).ival + ". Estructura WHILE incompleta. Falta OPEN_PAR");
                }
                break;
                case 36:
//#line 73 "gramatica.y"
                {
                    yyerror("Línea " + val_peek(4).ival + ". Estructura WHILE incompleta. Falta CLOSE_PAR");
                }
                break;
                case 37:
//#line 76 "gramatica.y"
                {
                    System.out.println("Comparación. Línea " + val_peek(1).ival);
                }
                break;
                case 38:
//#line 79 "gramatica.y"
                {
                    System.out.println("SUMA. Línea " + val_peek(1).ival);
                    yyval = new ParserVal(Long.toString(Long.parseLong(val_peek(2).sval) + Long.parseLong(val_peek(0).sval)));
                }
                break;
                case 39:
//#line 80 "gramatica.y"
                {
                    System.out.println("RESTA. Línea " + val_peek(1).ival);
                    yyval = new ParserVal(Long.toString(Long.parseLong(val_peek(2).sval) - Long.parseLong(val_peek(0).sval)));
                }
                break;
                case 41:
//#line 84 "gramatica.y"
                {
                    System.out.println("MULTIPLICACION. Línea " + val_peek(1).ival);

                    if (val_peek(0).ival != -1)
                        yyval = new ParserVal(Long.toString(Long.parseLong(val_peek(2).sval) * Long.parseLong(val_peek(0).sval)));
                    else
                        yyval = new ParserVal(Long.toString(Long.parseLong(val_peek(2).sval) * 1));
                }
                break;
                case 42:
//#line 92 "gramatica.y"
                {
                    System.out.println("DIVISION. Línea " + val_peek(1).ival);

                    if (val_peek(0).ival != -1)
                        yyval = new ParserVal(Long.toString(Long.parseLong(val_peek(2).sval) / Long.parseLong(val_peek(0).sval)));
                    else
                        yyval = new ParserVal(Long.toString(Long.parseLong(val_peek(2).sval) / 1));
                }
                break;
                case 44:
//#line 103 "gramatica.y"
                {
                    System.out.println("Lectura de la variable " + val_peek(0).sval + ". Línea " + val_peek(0).ival);
                    if (!tablaSimbolos.varDefined(val_peek(0).sval))
                        yyerror("\tError en la línea " + val_peek(0).ival + ": VARIABLE NO DEFINIDA");
                    yyval = new ParserVal(-1);
                }
                break;
                case 46:
//#line 109 "gramatica.y"
                {
                    yyval = new ParserVal(-1);
                    yyval.sval = "0";
                }
                break;
                case 53:
//#line 120 "gramatica.y"
                {
                    System.out.println("Asignación. Línea " + val_peek(3).ival);
                    if (!tablaSimbolos.varDefined(val_peek(3).sval))
                        yyerror("\tError en la línea " + val_peek(3).ival + ": VARIABLE NO DEFINIDA");

                    if (tablaSimbolos.getType(val_peek(1).sval).equals("ULONG") && tablaSimbolos.getVarType(val_peek(3).sval).equals("UINT"))
                        yyerror("\tError en la línea " + val_peek(3).ival + ": Se quiere asignar valor ULONG a variable declarada como UINT");
                    else
                        System.out.println("Valor de la expresión: " + val_peek(1).sval);
                }
                break;
                case 54:
//#line 129 "gramatica.y"
                {
                    yyerror("\tLínea " + val_peek(2).ival + ". Asignación incompleta. Falta DOT");
                }
                break;
                case 61:
//#line 143 "gramatica.y"
                {
                    System.out.println("Línea " + val_peek(2).ival + ". Bloque compuesto");
                }
                break;
//#line 766 "Parser.java"
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
                    yychar = yylex();        //get next character
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
    public Parser() {
        //nothing to do
    }


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
