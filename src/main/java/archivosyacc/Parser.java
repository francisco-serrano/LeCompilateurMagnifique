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
package archivosyacc;

import lexer.Lexer;
import lexer.TablaSimbolos;

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
            0, 2, 2, 4, 4, 4, 5, 5, 5, 6,
            6, 3, 3, 3, 3, 7, 7, 7, 1, 1,
            10, 10, 10, 10, 10, 12, 12, 12, 12, 12,
            11, 11, 8, 8, 15, 15, 15, 15, 15, 13,
            13, 16, 18, 19, 19, 19, 17, 17, 21, 21,
            21, 21, 22, 22, 14, 23, 9, 9, 9, 24,
            24, 24, 25, 25, 25, 20, 20, 20, 20, 20,
            20, 26,
    };
    final static short yylen[] = {2,
            1, 1, 1, 4, 3, 3, 3, 1, 2, 1,
            1, 4, 3, 5, 4, 8, 7, 3, 2, 1,
            1, 1, 1, 1, 1, 5, 4, 4, 4, 5,
            4, 3, 2, 1, 1, 1, 1, 1, 1, 4,
            2, 4, 1, 5, 4, 4, 1, 3, 1, 1,
            1, 1, 2, 1, 4, 1, 3, 3, 1, 3,
            3, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 3,
    };
    final static short yydefred[] = {0,
            0, 10, 11, 0, 0, 0, 0, 0, 0, 0,
            25, 2, 3, 0, 0, 20, 21, 22, 23, 24,
            0, 0, 0, 64, 0, 0, 0, 43, 0, 62,
            65, 0, 0, 0, 0, 56, 0, 19, 9, 0,
            0, 0, 0, 0, 0, 41, 0, 0, 0, 0,
            0, 66, 67, 68, 69, 70, 71, 0, 0, 0,
            0, 0, 0, 0, 0, 13, 0, 0, 0, 7,
            6, 0, 0, 0, 0, 49, 52, 50, 51, 0,
            47, 31, 72, 0, 0, 0, 0, 42, 60, 61,
            28, 0, 0, 29, 0, 35, 0, 36, 37, 38,
            39, 34, 15, 55, 4, 12, 0, 54, 0, 40,
            0, 45, 26, 30, 0, 18, 0, 33, 14, 48,
            53, 44, 0, 0, 0, 0, 0, 0, 17, 0,
            16,
    };
    final static short yydgoto[] = {9,
            10, 11, 12, 13, 14, 15, 66, 97, 26, 16,
            76, 77, 78, 79, 102, 21, 80, 27, 28, 58,
            81, 109, 37, 29, 30, 31,
    };
    final static short yysindex[] = {-242,
            -243, 0, 0, -232, -196, -235, -261, -232, 0, -242,
            0, 0, 0, -208, -205, 0, 0, 0, 0, 0,
            -178, -126, -240, 0, -126, -168, -219, 0, -124, 0,
            0, -227, -210, -221, -163, 0, -192, 0, 0, -129,
            -123, -128, -111, -157, -248, 0, -134, -125, -168, -126,
            -126, 0, 0, 0, 0, 0, 0, -126, -248, -126,
            -126, -190, -183, -117, -204, 0, -221, -248, -115, 0,
            0, -221, -107, -243, -199, 0, 0, 0, 0, -122,
            0, 0, 0, -126, -124, -124, -158, 0, 0, 0,
            0, -113, -112, 0, -119, 0, -238, 0, 0, 0,
            0, 0, 0, 0, 0, 0, -221, 0, -201, 0,
            -144, 0, 0, 0, -126, 0, -116, 0, 0, 0,
            0, 0, -138, -126, -109, -136, -131, -105, 0, -127,
            0,
    };
    final static short yyrindex[] = {0,
            -149, 0, 0, 0, 0, 0, 0, 0, 0, 160,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 1, 0, 0, 0, 0, 0, 36, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 133, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 168, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 71, 106, 0, 0, 0, 0,
            0, 158, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            -237, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0,
    };
    final static short yygindex[] = {0,
            0, 0, 0, -58, 0, 7, -61, 0, -20, 152,
            3, 4, 8, 10, 66, 0, 28, 0, 156, 116,
            -75, 0, 0, 92, 84, 0,
    };
    final static int YYTABLESIZE = 458;
    static short yytable[];

    static {
        yytable();
    }

    static void yytable() {
        yytable = new short[]{108,
                63, 47, 17, 18, 49, 103, 96, 19, 74, 20,
                106, 63, 17, 18, 1, 22, 75, 19, 1, 20,
                42, 34, 4, 35, 23, 24, 2, 3, 4, 23,
                24, 48, 4, 121, 5, 59, 46, 87, 96, 25,
                5, 8, 6, 7, 5, 119, 69, 8, 39, 116,
                117, 8, 1, 46, 59, 74, 62, 74, 40, 41,
                2, 3, 64, 111, 120, 65, 4, 98, 99, 4,
                57, 4, 100, 91, 101, 32, 50, 51, 5, 43,
                44, 5, 92, 5, 95, 8, 88, 33, 8, 93,
                8, 50, 51, 67, 123, 104, 45, 46, 68, 98,
                99, 50, 51, 126, 100, 58, 101, 8, 52, 53,
                54, 55, 56, 57, 112, 50, 51, 8, 8, 8,
                8, 50, 51, 50, 51, 50, 51, 73, 122, 82,
                23, 24, 32, 70, 125, 71, 128, 60, 61, 2,
                3, 85, 86, 89, 90, 72, 94, 83, 105, 107,
                113, 114, 115, 110, 127, 124, 129, 27, 130, 1,
                131, 38, 118, 36, 84, 0, 0, 5, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 63, 0, 0,
                63, 63, 63, 63, 63, 0, 63, 0, 0, 63,
                63, 63, 0, 63, 63, 63, 63, 63, 63, 63,
                63, 63, 63, 63, 0, 63, 63, 0, 63, 63,
                63, 63, 59, 0, 0, 59, 59, 0, 0, 59,
                0, 59, 0, 0, 59, 59, 59, 0, 59, 59,
                59, 59, 59, 59, 59, 59, 59, 59, 59, 0,
                59, 59, 0, 59, 59, 59, 59, 57, 0, 0,
                57, 57, 0, 0, 57, 0, 57, 0, 0, 57,
                57, 57, 0, 57, 57, 57, 57, 57, 57, 57,
                57, 57, 57, 57, 0, 57, 57, 0, 57, 57,
                57, 57, 58, 0, 0, 58, 58, 0, 0, 58,
                0, 58, 0, 0, 58, 58, 58, 0, 58, 58,
                58, 58, 58, 58, 58, 58, 58, 58, 58, 32,
                58, 58, 0, 58, 58, 58, 58, 0, 32, 0,
                0, 32, 32, 32, 0, 0, 0, 32, 32, 0,
                0, 0, 0, 0, 27, 32, 0, 32, 32, 0,
                32, 32, 32, 27, 5, 0, 27, 27, 27, 0,
                0, 0, 27, 27, 0, 0, 5, 5, 5, 0,
                27, 0, 27, 27, 0, 27, 27, 27, 0, 0,
                5, 0, 5, 5, 0, 5, 5, 5,
        };
    }

    static short yycheck[];

    static {
        yycheck();
    }

    static void yycheck() {
        yycheck = new short[]{75,
                0, 22, 0, 0, 25, 67, 65, 0, 257, 0,
                72, 32, 10, 10, 257, 259, 265, 10, 257, 10,
                14, 257, 271, 285, 257, 258, 269, 270, 271, 257,
                258, 272, 271, 109, 283, 0, 274, 58, 97, 272,
                283, 290, 285, 286, 283, 107, 40, 290, 257, 288,
                289, 290, 257, 291, 274, 257, 284, 257, 267, 268,
                269, 270, 273, 84, 266, 287, 271, 65, 65, 271,
                0, 271, 65, 264, 65, 272, 260, 261, 283, 285,
                286, 283, 273, 283, 289, 290, 59, 284, 290, 273,
                290, 260, 261, 257, 115, 68, 275, 276, 291, 97,
                97, 260, 261, 124, 97, 0, 97, 257, 277, 278,
                279, 280, 281, 282, 273, 260, 261, 267, 268, 269,
                270, 260, 261, 260, 261, 260, 261, 285, 273, 264,
                257, 258, 0, 257, 273, 264, 273, 262, 263, 269,
                270, 50, 51, 60, 61, 257, 264, 273, 264, 257,
                264, 264, 272, 276, 264, 272, 288, 0, 264, 0,
                288, 10, 97, 8, 49, -1, -1, 0, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, 257, -1, -1,
                260, 261, 262, 263, 264, -1, 266, -1, -1, 269,
                270, 271, -1, 273, 274, 275, 276, 277, 278, 279,
                280, 281, 282, 283, -1, 285, 286, -1, 288, 289,
                290, 291, 257, -1, -1, 260, 261, -1, -1, 264,
                -1, 266, -1, -1, 269, 270, 271, -1, 273, 274,
                275, 276, 277, 278, 279, 280, 281, 282, 283, -1,
                285, 286, -1, 288, 289, 290, 291, 257, -1, -1,
                260, 261, -1, -1, 264, -1, 266, -1, -1, 269,
                270, 271, -1, 273, 274, 275, 276, 277, 278, 279,
                280, 281, 282, 283, -1, 285, 286, -1, 288, 289,
                290, 291, 257, -1, -1, 260, 261, -1, -1, 264,
                -1, 266, -1, -1, 269, 270, 271, -1, 273, 274,
                275, 276, 277, 278, 279, 280, 281, 282, 283, 257,
                285, 286, -1, 288, 289, 290, 291, -1, 266, -1,
                -1, 269, 270, 271, -1, -1, -1, 275, 276, -1,
                -1, -1, -1, -1, 257, 283, -1, 285, 286, -1,
                288, 289, 290, 266, 257, -1, 269, 270, 271, -1,
                -1, -1, 275, 276, -1, -1, 269, 270, 271, -1,
                283, -1, 285, 286, -1, 288, 289, 290, -1, -1,
                283, -1, 285, 286, -1, 288, 289, 290,
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
            "seleccion : seleccion_simple ELSE bloque_sentencias END_IF",
            "seleccion : seleccion_simple END_IF",
            "seleccion_simple : IF condicion_if THEN bloque_sentencias",
            "condicion_if : condicion",
            "condicion : OPEN_PAR expresion comparador expresion CLOSE_PAR",
            "condicion : expresion comparador expresion CLOSE_PAR",
            "condicion : OPEN_PAR expresion comparador expresion",
            "bloque_sentencias : bloque_simple",
            "bloque_sentencias : BEGIN bloque_compuesto END",
            "bloque_simple : asignacion",
            "bloque_simple : seleccion",
            "bloque_simple : iteracion",
            "bloque_simple : print",
            "bloque_compuesto : bloque_compuesto bloque_simple",
            "bloque_compuesto : bloque_simple",
            "iteracion : WHILE condicion_while DO bloque_sentencias",
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

//#line 153 "gramatica.y"

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
    List<String> auxVariables = new ArrayList<>();

    //#line 437 "Parser.java"
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
                case 4:
//#line 18 "gramatica.y"
                {
                    System.out.println("Declaración de Variables. Línea " + val_peek(2).ival);
                    tablaSimbolos.defineVar(auxVariables, val_peek(1).sval);
                    auxVariables.clear();
                }
                break;
                case 5:
//#line 23 "gramatica.y"
                {
                    yyerror("\tLínea " + val_peek(1).ival + ". Declaración de variables incompleta. Falta DOT");
                }
                break;
                case 6:
//#line 24 "gramatica.y"
                {
                    yyerror("\tLínea " + val_peek(0).ival + ". Declaración de variables incompleta. Falta COLON");
                }
                break;
                case 7:
//#line 27 "gramatica.y"
                { /*tablaSimbolos.defineVar($3.sval); tablaSimbolos.defineVar($1.sval);*/
                    auxVariables.add(val_peek(0).sval);
                }
                break;
                case 8:
//#line 28 "gramatica.y"
                {
                    auxVariables.add(val_peek(0).sval);
                }
                break;
                case 9:
//#line 29 "gramatica.y"
                {
                    yyerror("\tLínea " + val_peek(0).ival + ". Declaración incompleta. Falta COMMA");
                }
                break;
                case 13:
//#line 36 "gramatica.y"
                {
                    yyerror("\tLínea " + val_peek(2).ival + ". Declaración de función incompleta. Falta tipo de retorno");
                }
                break;
                case 15:
//#line 38 "gramatica.y"
                {
                    yyerror("\tLínea " + val_peek(3).ival + ". Declaración de función incompleta. Falta tipo de retorno");
                }
                break;
                case 18:
//#line 43 "gramatica.y"
                {
                    yyerror("\tLínea " + val_peek(2).ival + ". Declaración de función incompleta. Falta sentencia RETURN");
                }
                break;
                case 26:
//#line 52 "gramatica.y"
                {
                    System.out.println("Sentencia OUT. Línea " + val_peek(4).ival);
                }
                break;
                case 27:
//#line 53 "gramatica.y"
                {
                    yyerror("\tLínea " + val_peek(3).ival + ". Estructura OUT incompleta. Falta DOT");
                }
                break;
                case 28:
//#line 54 "gramatica.y"
                {
                    yyerror("\tLínea " + val_peek(3).ival + ". Estructura OUT incompleta. Falta CLOSE_PAR");
                }
                break;
                case 29:
//#line 55 "gramatica.y"
                {
                    yyerror("\tLínea " + val_peek(3).ival + ". Estructura OUT incompleta. Falta OPEN_PAR");
                }
                break;
                case 30:
//#line 56 "gramatica.y"
                {
                    yyerror("\tLínea " + val_peek(4).ival + ". Estructura OUT incorrecta. Sólo se pueden imprimir cadenas");
                }
                break;
                case 31:
//#line 59 "gramatica.y"
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
                case 32:
//#line 68 "gramatica.y"
                {
                    yyerror("\tLínea " + val_peek(2).ival + ". Asignación incompleta. Falta DOT");
                }
                break;
                case 40:
//#line 77 "gramatica.y"
                {
                    System.out.println("Línea " + val_peek(3).ival + ". Sentencia IF-ELSE");
                }
                break;
                case 41:
//#line 78 "gramatica.y"
                {
                    System.out.println("Línea " + val_peek(1).ival + ". Sentencia IF");
                }
                break;
                case 44:
//#line 87 "gramatica.y"
                {
                    System.out.println("Comparación. Línea " + val_peek(2).ival);
                }
                break;
                case 45:
//#line 88 "gramatica.y"
                {
                    yyerror("Línea " + val_peek(2).ival + ". Condicion incompleta. Falta OPEN_PAR");
                }
                break;
                case 46:
//#line 89 "gramatica.y"
                {
                    yyerror("Línea " + val_peek(1).ival + ". Condicion. Falta CLOSE_PAR");
                }
                break;
                case 48:
//#line 93 "gramatica.y"
                {
                    System.out.println("Línea " + val_peek(2).ival + ". Bloque compuesto");
                }
                break;
                case 55:
//#line 102 "gramatica.y"
                {
                    System.out.println("Línea " + val_peek(3).ival + ". Estructura WHILE");
                }
                break;
                case 57:
//#line 108 "gramatica.y"
                {
                    System.out.println("SUMA. Línea " + val_peek(1).ival);
                    yyval = new ParserVal(Long.toString(Long.parseLong(val_peek(2).sval) + Long.parseLong(val_peek(0).sval)));
                }
                break;
                case 58:
//#line 109 "gramatica.y"
                {
                    System.out.println("RESTA. Línea " + val_peek(1).ival);
                    yyval = new ParserVal(Long.toString(Long.parseLong(val_peek(2).sval) - Long.parseLong(val_peek(0).sval)));
                }
                break;
                case 60:
//#line 113 "gramatica.y"
                {
                    System.out.println("MULTIPLICACION. Línea " + val_peek(1).ival);

                    if (val_peek(0).ival != -1)
                        yyval = new ParserVal(Long.toString(Long.parseLong(val_peek(2).sval) * Long.parseLong(val_peek(0).sval)));
                    else
                        yyval = new ParserVal(Long.toString(Long.parseLong(val_peek(2).sval) * 1));
                }
                break;
                case 61:
//#line 121 "gramatica.y"
                {
                    System.out.println("DIVISION. Línea " + val_peek(1).ival);

                    if (val_peek(0).ival != -1)
                        yyval = new ParserVal(Long.toString(Long.parseLong(val_peek(2).sval) / Long.parseLong(val_peek(0).sval)));
                    else
                        yyval = new ParserVal(Long.toString(Long.parseLong(val_peek(2).sval) / 1));
                }
                break;
                case 63:
//#line 132 "gramatica.y"
                {
                    System.out.println("Lectura de la variable " + val_peek(0).sval + ". Línea " + val_peek(0).ival);
                    if (!tablaSimbolos.varDefined(val_peek(0).sval))
                        yyerror("\tError en la línea " + val_peek(0).ival + ": VARIABLE NO DEFINIDA");
                    yyval = new ParserVal(-1);
                }
                break;
                case 65:
//#line 138 "gramatica.y"
                {
                    yyval = new ParserVal(-1);
                    yyval.sval = "0";
                }
                break;
                case 72:
//#line 149 "gramatica.y"
                {
                    System.out.println("Invocación a función. Línea " + val_peek(2).ival);
                }
                break;
//#line 736 "Parser.java"
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
