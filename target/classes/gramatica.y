%{
	import accionsemantica.TablaSimbolos;
%}

%token ID CTE ASIGN ADD SUB MULT DIV DOT BEGIN END COLON COMMA UINT ULONG IF OPEN_PAR CLOSE_PAR THEN ELSE END_IF LEQ GEQ LT GT EQ NEQ OUT CADENA FUNCTION MOVE OPEN_BRACE CLOSE_BRACE RETURN WHILE DO
%start programa

%%

programa : bloque
;

funcion : tipo FUNCTION ID cuerpo_funcion 
		| FUNCTION ID cuerpo_funcion { yyerror("\tLínea " + $1.ival + ". Declaración de función incompleta. Falta tipo de retorno"); }

		| tipo MOVE FUNCTION ID cuerpo_funcion
		| MOVE FUNCTION ID cuerpo_funcion { yyerror("\tLínea " + $1.ival + ". Declaración de función incompleta. Falta tipo de retorno"); }
;

tipo : UINT | ULONG
;

cuerpo_funcion : OPEN_BRACE bloque_funcion RETURN OPEN_PAR expresion CLOSE_PAR DOT CLOSE_BRACE 
			   | OPEN_BRACE RETURN OPEN_PAR expresion CLOSE_PAR DOT CLOSE_BRACE
			   | OPEN_BRACE bloque_funcion CLOSE_BRACE { yyerror("\tLínea " + $1.ival + ". Declaración de función incompleta. Falta sentencia RETURN"); }
;

invocacion_funcion : ID OPEN_PAR CLOSE_PAR { System.out.println("Invocación a función. Línea " + $1.ival); }
;

declaracion : declaracion_variables 
			| declaracion_funcion
;

declaracion_variables : lista_variables COLON tipo DOT { System.out.println("Declaración de Variables. Línea " + $2.ival); }
					  | lista_variables COLON tipo { yyerror("\tLínea " + $2.ival + ". Declaración de variables incompleta. Falta DOT"); }
					  | lista_variables tipo DOT { yyerror("\tLínea " + $3.ival + ". Declaración de variables incompleta. Falta COLON"); }
;

declaracion_funcion : funcion { System.out.println("Declaración de Función. Línea " + $1.ival); }
;

lista_variables : ID COMMA lista_variables { tablaSimbolos.defineVar($1.sval); tablaSimbolos.defineVar($3.sval); }
				| ID 
				| ID lista_variables { yyerror("\tLínea " + $1.ival + ". Declaración incompleta. Falta COMMA"); }
;

ejecutable : IF OPEN_PAR condicion CLOSE_PAR THEN bloque_sentencias ELSE bloque_sentencias END_IF { System.out.println("Línea " + $1.ival + ". Sentencia IF"); } 
		   | IF OPEN_PAR condicion CLOSE_PAR THEN bloque_sentencias END_IF { System.out.println("Línea " + $1.ival + ". Sentencia IF"); } 
		   | IF OPEN_PAR condicion CLOSE_PAR THEN bloque_sentencias { yyerror("\tLínea " + $1.ival + ". Estructura IF incompleta. Falta END_IF"); }
		   | IF OPEN_PAR condicion THEN bloque_sentencias END_IF { yyerror("\tLínea " + $1.ival + ". Estructura IF incompleta. Falta CLOSE_PAR"); }
		   | IF condicion CLOSE_PAR THEN bloque_sentencias END_IF { yyerror("\tLínea " + $1.ival + ". Estructura IF incompleta. Falta OPEN_PAR"); }
		   | IF condicion THEN bloque_sentencias END_IF { yyerror("\tLínea " + $1.ival + ". Estructura IF incompleta. La condición va entre paréntesis"); }
		   
		   | OUT OPEN_PAR CADENA CLOSE_PAR DOT { System.out.println("Sentencia OUT. Línea " + $1.ival); }
		   | OUT OPEN_PAR CADENA CLOSE_PAR { yyerror("\tLínea " + $1.ival + ". Estructura OUT incompleta. Falta DOT"); }
		   | OUT OPEN_PAR CADENA DOT { yyerror("\tLínea " + $1.ival + ". Estructura OUT incompleta. Falta CLOSE_PAR"); }
		   | OUT CADENA CLOSE_PAR DOT { yyerror("\tLínea " + $1.ival + ". Estructura OUT incompleta. Falta OPEN_PAR"); }
		   | OUT CADENA DOT { yyerror("\tLínea " + $1.ival + ". Estructura OUT incompleta. La cadena va entre paréntesis"); }
		   | OUT OPEN_PAR expresion CLOSE_PAR DOT { yyerror("\tLínea " + $1.ival + ". Estructura OUT incorrecta. Sólo se pueden imprimir cadenas"); }
;

control : WHILE OPEN_PAR condicion CLOSE_PAR DO bloque_sentencias { System.out.println("Línea " + $1.ival + ". Estructura WHILE"); }
		| WHILE condicion CLOSE_PAR DO bloque_sentencias { yyerror("Línea " + $1.ival + ". Estructura WHILE incompleta. Falta OPEN_PAR"); }
		| WHILE OPEN_PAR condicion DO bloque_sentencias { yyerror("Línea " + $1.ival + ". Estructura WHILE incompleta. Falta CLOSE_PAR"); }
		| WHILE condicion DO bloque_sentencias { yyerror("Línea " + $1.ival + ". Estructura WHILE incompleta. La condición va entre paréntesis"); }
;

condicion : expresion comparador expresion { System.out.println("Comparación. Línea " + $1.ival); }
;

expresion : expresion ADD termino { System.out.println("SUMA. Línea " + $2.ival); } 
		  | expresion SUB termino { System.out.println("RESTA. Línea " + $2.ival); } 
		  | termino
;

termino : termino MULT factor { System.out.println("MULTIPLICACION. Línea " + $2.ival); } 
		| termino DIV factor { System.out.println("DIVISION. Línea " + $2.ival); } 
		| factor
;

factor : ID { System.out.println("Lectura de la variable " + $1.sval + ". Línea " + $1.ival); 
			  if (! tablaSimbolos.varDefined($1.sval))
			  	yyerror("\tError en la línea " + $1.ival + ": VARIABLE NO DEFINIDA"); 
			}
	   | CTE 
	   | invocacion_funcion
;

comparador : LEQ 
		   | GEQ 
		   | LT 
		   | GT 
		   | EQ 
		   | NEQ
;

asignacion : ID ASIGN expresion DOT { System.out.println("Asignación. Línea " + $1.ival);
									  if (! tablaSimbolos.varDefined($1.sval))
									  	yyerror("\tError en la línea " + $1.ival + ": VARIABLE NO DEFINIDA"); 
									}
		   | ID ASIGN expresion { yyerror("\tLínea " + $1.ival + ". Asignación incompleta. Falta DOT"); }
;

bloque_sentencias : bloque_compuesto 
				  | bloque_simple
;

bloque : declaracion bloque 
	   | declaracion 
	   | bloque_sentencias
;

bloque_funcion : bloque_sentencias 
			   | declaracion_variables bloque_funcion
;

bloque_compuesto : BEGIN sentencias END { System.out.println("Línea " + $1.ival + ". Bloque compuesto"); }
				 | sentencias END { yyerror("\tLínea " + $1.ival + ". Bloque compuesto incompleto. Falta BEGIN"); }
				 | BEGIN sentencias { yyerror("\tLínea " + $1.ival + ". Bloque compuesto incompleto. Falta END"); }
;

bloque_simple : sentencia
;

sentencias : sentencia sentencias 
		   | sentencia
;

sentencia : asignacion 
		  | ejecutable 
		  | control
;

%%

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