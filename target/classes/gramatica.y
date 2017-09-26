%token ID CTE ASIGN ADD SUB MULT DIV DOT BEGIN END COLON COMMA UINT ULONG IF OPEN_PAR CLOSE_PAR THEN ELSE END_IF LEQ GEQ LT GT EQ NEQ OUT CADENA FUNCTION MOVE OPEN_BRACE CLOSE_BRACE RETURN WHILE DO
%start programa

%%

programa : bloque
;

funcion : tipo FUNCTION ID cuerpo_funcion | tipo MOVE FUNCTION ID cuerpo_funcion
;

tipo : UINT | ULONG
;

cuerpo_funcion : OPEN_BRACE bloque_funcion RETURN OPEN_PAR expresion CLOSE_PAR DOT CLOSE_BRACE | OPEN_BRACE RETURN OPEN_PAR expresion CLOSE_PAR DOT CLOSE_BRACE
;

invocacion_funcion : ID OPEN_PAR CLOSE_PAR
;

declaracion : declaracion_variables | declaracion_funcion
;

declaracion_variables : lista_variables COLON tipo DOT
;

declaracion_funcion : funcion
;

lista_variables : ID COMMA lista_variables | ID
;

ejecutable : IF OPEN_PAR condicion CLOSE_PAR THEN bloque_sentencias ELSE bloque_sentencias END_IF | IF OPEN_PAR condicion CLOSE_PAR THEN bloque_sentencias END_IF | OUT OPEN_PAR CADENA CLOSE_PAR DOT
;

control : WHILE OPEN_PAR condicion CLOSE_PAR DO bloque_sentencias DOT
;

condicion : expresion comparador expresion
;

expresion : invocacion_funcion | expresion ADD termino | expresion SUB termino | termino
;

termino : termino MULT factor | termino DIV factor | factor
;

factor : ID | CTE
;

comparador : LEQ | GEQ | LT | GT | EQ | NEQ
;

asignacion : ID ASIGN expresion DOT
;

bloque_sentencias : bloque_compuesto | bloque_simple
;

bloque : declaracion bloque | declaracion | bloque_sentencias
;

bloque_funcion : declaracion_variables bloque_sentencias | declaracion_variables bloque_funcion
;

bloque_compuesto : BEGIN sentencias END
bloque_simple : sentencia
;

sentencias : sentencia sentencias | sentencia
;

sentencia : asignacion | ejecutable | control
;

%%