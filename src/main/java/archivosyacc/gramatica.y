%{
	import accionsemantica.TablaSimbolos;
	import java.util.ArrayList;
    import java.util.List;
%}

%token ID CTE ASIGN ADD SUB MULT DIV DOT BEGIN END COLON COMMA UINT ULONG IF OPEN_PAR CLOSE_PAR THEN ELSE END_IF LEQ GEQ LT GT EQ NEQ OUT CADENA FUNCTION MOVE OPEN_BRACE CLOSE_BRACE RETURN WHILE DO
%start programa

%%

programa : sentencias
;

declaracion : declaracion_funcion | declaracion_variables
;

declaracion_variables : lista_var COLON tipo DOT { 
													System.out.println("Declaración de Variables. Línea " + $2.ival); 
													tablaSimbolos.defineVar(auxVariables, $3.sval);
													auxVariables.clear();
												 }
					  | lista_var COLON tipo { yyerror("\tLínea " + $2.ival + ". Declaración de variables incompleta. Falta DOT"); }
					  | lista_var tipo DOT { yyerror("\tLínea " + $3.ival + ". Declaración de variables incompleta. Falta COLON"); }
;

lista_var : lista_var COMMA ID { /*tablaSimbolos.defineVar($3.sval); tablaSimbolos.defineVar($1.sval);*/ auxVariables.add($3.sval); }
			| ID { auxVariables.add($1.sval); }
			| lista_var ID { yyerror("\tLínea " + $2.ival + ". Declaración incompleta. Falta COMMA"); }
;

tipo : UINT | ULONG
;

declaracion_funcion : tipo FUNCTION ID cuerpo_funcion 
					| FUNCTION ID cuerpo_funcion { yyerror("\tLínea " + $1.ival + ". Declaración de función incompleta. Falta tipo de retorno"); }
					| tipo MOVE FUNCTION ID cuerpo_funcion
					| MOVE FUNCTION ID cuerpo_funcion { yyerror("\tLínea " + $1.ival + ". Declaración de función incompleta. Falta tipo de retorno"); }
;

cuerpo_funcion : OPEN_BRACE bloque_funcion RETURN OPEN_PAR expresion CLOSE_PAR DOT CLOSE_BRACE 
			   | OPEN_BRACE RETURN OPEN_PAR expresion CLOSE_PAR DOT CLOSE_BRACE
			   | OPEN_BRACE bloque_funcion CLOSE_BRACE { yyerror("\tLínea " + $1.ival + ". Declaración de función incompleta. Falta sentencia RETURN"); }
;

sentencias : sentencias sentencia | sentencia
;

sentencia : asignacion | print | seleccion | iteracion | declaracion
;

print : OUT OPEN_PAR CADENA CLOSE_PAR DOT { System.out.println("Sentencia OUT. Línea " + $1.ival); }
	  | OUT OPEN_PAR CADENA CLOSE_PAR { yyerror("\tLínea " + $1.ival + ". Estructura OUT incompleta. Falta DOT"); }
	  | OUT OPEN_PAR CADENA DOT { yyerror("\tLínea " + $1.ival + ". Estructura OUT incompleta. Falta CLOSE_PAR"); }
	  | OUT CADENA CLOSE_PAR DOT { yyerror("\tLínea " + $1.ival + ". Estructura OUT incompleta. Falta OPEN_PAR"); }
	  | OUT OPEN_PAR expresion CLOSE_PAR DOT { yyerror("\tLínea " + $1.ival + ". Estructura OUT incorrecta. Sólo se pueden imprimir cadenas"); }
;

asignacion : ID ASIGN expresion DOT { System.out.println("Asignación. Línea " + $1.ival);
									  if (! tablaSimbolos.varDefined($1.sval))
									  	yyerror("\tError en la línea " + $1.ival + ": VARIABLE NO DEFINIDA"); 

									  if (tablaSimbolos.getType($3.sval).equals("ULONG") && tablaSimbolos.getVarType($1.sval).equals("UINT"))
									  	yyerror("\tError en la línea " + $1.ival + ": Se quiere asignar valor ULONG a variable declarada como UINT");
									  else
									  	System.out.println("Valor de la expresión: " + $3.sval);
									}
		   | ID ASIGN expresion { yyerror("\tLínea " + $1.ival + ". Asignación incompleta. Falta DOT"); }
;

bloque_funcion : bloque_funcion bloque | bloque
;

bloque : declaracion_variables | asignacion | print | seleccion | iteracion
;

seleccion : seleccion_simple ELSE bloque_sentencias END_IF { System.out.println("Línea " + $1.ival + ". Sentencia IF-ELSE"); }
		  | seleccion_simple END_IF { System.out.println("Línea " + $1.ival + ". Sentencia IF"); }
;

seleccion_simple : IF condicion_if THEN bloque_sentencias
;

condicion_if : condicion
;

condicion : OPEN_PAR expresion comparador expresion CLOSE_PAR { System.out.println("Comparación. Línea " + $3.ival); }
		  | expresion comparador expresion CLOSE_PAR { yyerror("Línea " + $2.ival + ". Condicion incompleta. Falta OPEN_PAR"); }
		  | OPEN_PAR expresion comparador expresion { yyerror("Línea " + $3.ival + ". Condicion. Falta CLOSE_PAR"); }
;

bloque_sentencias : bloque_simple 
				  | BEGIN bloque_compuesto END { System.out.println("Línea " + $1.ival + ". Bloque compuesto"); }
;

bloque_simple : asignacion | seleccion | iteracion | print
;

bloque_compuesto : bloque_compuesto bloque_simple | bloque_simple
;

iteracion : WHILE condicion_while DO bloque_sentencias { System.out.println("Línea " + $1.ival + ". Estructura WHILE"); }
;

condicion_while : condicion
;

expresion : expresion ADD termino { System.out.println("SUMA. Línea " + $2.ival); $$ = new ParserVal(Long.toString(Long.parseLong($1.sval) + Long.parseLong($3.sval))); } 
		  | expresion SUB termino { System.out.println("RESTA. Línea " + $2.ival); $$ = new ParserVal(Long.toString(Long.parseLong($1.sval) - Long.parseLong($3.sval)));} 
		  | termino
;

termino : termino MULT factor { 
								System.out.println("MULTIPLICACION. Línea " + $2.ival); 

								if ($3.ival != -1)
									$$ = new ParserVal(Long.toString(Long.parseLong($1.sval) * Long.parseLong($3.sval)));
								else
									$$ = new ParserVal(Long.toString(Long.parseLong($1.sval) * 1));
							  } 
		| termino DIV factor { 
								System.out.println("DIVISION. Línea " + $2.ival); 

								if ($3.ival != -1)
									$$ = new ParserVal(Long.toString(Long.parseLong($1.sval) / Long.parseLong($3.sval)));
								else
									$$ = new ParserVal(Long.toString(Long.parseLong($1.sval) / 1));
							 } 
		| factor 
;

factor : ID { System.out.println("Lectura de la variable " + $1.sval + ". Línea " + $1.ival); 
			  if (! tablaSimbolos.varDefined($1.sval))
			  	yyerror("\tError en la línea " + $1.ival + ": VARIABLE NO DEFINIDA"); 
			  $$ = new ParserVal(-1);
			}
	   | CTE 
	   | invocacion_funcion { $$ = new ParserVal(-1); $$.sval = "0"; }
;

comparador : LEQ 
		   | GEQ 
		   | LT 
		   | GT 
		   | EQ 
		   | NEQ
;

invocacion_funcion : ID OPEN_PAR CLOSE_PAR { System.out.println("Invocación a función. Línea " + $1.ival); }
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
List<String> auxVariables = new ArrayList<>();