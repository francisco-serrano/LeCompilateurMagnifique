%token ID CTE ASIGN ADD SUB MULT DIV DOT BEGIN END COLON COMMA UINT ULONG IF OPEN_PAR CLOSE_PAR THEN ELSE END_IF LEQ GEQ LT GT EQ NEQ OUT CADENA FUNCTION MOVE OPEN_BRACE CLOSE_BRACE RETURN
%start programa

%%

programa : bloque_sentencias
;

funcion : tipo FUNCTION ID cuerpo_funcion | tipo MOVE FUNCTION ID cuerpo_funcion
;

cuerpo_funcion : OPEN_BRACE bloque_sentencias RETURN OPEN_PAR expresion CLOSE_PAR DOT CLOSE_BRACE
		| OPEN_BRACE RETURN OPEN_PAR expresion CLOSE_PAR DOT CLOSE_BRACE
;

invocacion_funcion : ID OPEN_PAR CLOSE_PAR
;

bloque_sentencias : bloque_simple | bloque_compuesto
;

bloque_simple : sentencia
;

bloque_compuesto : BEGIN sentencias END
;

sentencias : sentencia sentencias | sentencia
;

sentencia : asignacion | declaracion | ejecutable
;

asignacion : ID ASIGN expresion DOT { System.out.println("Asignación"); }
;

declaracion : lista_variables COLON tipo DOT {System.out.println("Declaración de variables");} | funcion {System.out.println("Declaración de función");}
;

ejecutable : IF OPEN_PAR condicion CLOSE_PAR THEN bloque_sentencias END_IF {System.out.println("Sentencia IF");}
		| IF OPEN_PAR condicion CLOSE_PAR THEN bloque_sentencias ELSE bloque_sentencias END_IF {System.out.println("Sentencia IF");}
		| OUT OPEN_PAR CADENA CLOSE_PAR DOT { System.out.println("Sentencia OUT"); }
;

condicion : expresion comparador expresion
;

comparador : LEQ | GEQ | LT | GT | EQ | NEQ
;

lista_variables : ID COMMA lista_variables | ID
;

tipo : UINT | ULONG
;

expresion : invocacion_funcion | expresion ADD termino | expresion SUB termino | termino
;

termino : termino MULT factor | termino DIV factor | factor
;

factor : ID | CTE
;

%%