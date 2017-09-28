%{
	import accionsemantica.TablaSimbolos;
	import java.util.ArrayList;
    import java.util.List;
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

declaracion_variables : lista_variables COLON tipo DOT { 
														 System.out.println("Declaración de Variables. Línea " + $2.ival); 
														 tablaSimbolos.defineVar(auxVariables, $3.sval);
														 auxVariables.clear();
													   }
					  | lista_variables COLON tipo { yyerror("\tLínea " + $2.ival + ". Declaración de variables incompleta. Falta DOT"); }
					  | lista_variables tipo DOT { yyerror("\tLínea " + $3.ival + ". Declaración de variables incompleta. Falta COLON"); }
;

declaracion_funcion : funcion { System.out.println("Declaración de Función. Línea " + $1.ival); }
;

lista_variables : ID COMMA lista_variables { /*tablaSimbolos.defineVar($1.sval); tablaSimbolos.defineVar($3.sval);*/ auxVariables.add($1.sval); }
				| ID { auxVariables.add($1.sval); }
				| ID lista_variables { yyerror("\tLínea " + $1.ival + ". Declaración incompleta. Falta COMMA"); }
;

ejecutable : IF OPEN_PAR condicion CLOSE_PAR THEN bloque_control ELSE bloque_control END_IF { System.out.println("Línea " + $1.ival + ". Sentencia IF"); }
		   | IF OPEN_PAR condicion CLOSE_PAR THEN bloque_control END_IF { System.out.println("Línea " + $1.ival + ". Sentencia IF"); }
		   | IF OPEN_PAR condicion CLOSE_PAR THEN bloque_control error { yyerror("\tLínea " + $1.ival + ". Estructura IF incompleta. Falta END_IF"); }
		   | IF OPEN_PAR condicion THEN bloque_control END_IF { yyerror("\tLínea " + $1.ival + ". Estructura IF incompleta. Falta CLOSE_PAR"); }
		   | IF condicion CLOSE_PAR THEN bloque_control END_IF { yyerror("\tLínea " + $1.ival + ". Estructura IF incompleta. Falta OPEN_PAR"); }
		   
		   | OUT OPEN_PAR CADENA CLOSE_PAR DOT { System.out.println("Sentencia OUT. Línea " + $1.ival); }
		   | OUT OPEN_PAR CADENA CLOSE_PAR { yyerror("\tLínea " + $1.ival + ". Estructura OUT incompleta. Falta DOT"); }
		   | OUT OPEN_PAR CADENA DOT { yyerror("\tLínea " + $1.ival + ". Estructura OUT incompleta. Falta CLOSE_PAR"); }
		   | OUT CADENA CLOSE_PAR DOT { yyerror("\tLínea " + $1.ival + ". Estructura OUT incompleta. Falta OPEN_PAR"); }
		   | OUT OPEN_PAR expresion CLOSE_PAR DOT { yyerror("\tLínea " + $1.ival + ". Estructura OUT incorrecta. Sólo se pueden imprimir cadenas"); }
;

control : WHILE OPEN_PAR condicion CLOSE_PAR DO bloque_control { System.out.println("Línea " + $1.ival + ". Estructura WHILE"); }
		| WHILE condicion CLOSE_PAR DO bloque_control { yyerror("Línea " + $1.ival + ". Estructura WHILE incompleta. Falta OPEN_PAR"); }
		| WHILE OPEN_PAR condicion DO bloque_control { yyerror("Línea " + $1.ival + ". Estructura WHILE incompleta. Falta CLOSE_PAR"); }
;

condicion : expresion comparador expresion { System.out.println("Comparación. Línea " + $2.ival); }
;

expresion : expresion ADD termino { System.out.println("SUMA. Línea " + $2.ival); $$ = new ParserVal(Long.toString(Long.parseLong($1.sval) + Long.parseLong($3.sval))); } 
		  | expresion SUB termino { System.out.println("RESTA. Línea " + $2.ival); $$ = new ParserVal(Long.toString(Long.parseLong($1.sval) - Long.parseLong($3.sval)));} 
		  | termino { 
		  			  /*if ($1.ival == -1) {
		  			  	System.out.println("ENTRA AL IF");
		  				 $1.ival = 0; 
		  				 $1.sval = "0"; 
		  			  }*/
		  			}
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

asignacion : ID ASIGN expresion DOT { System.out.println("Asignación. Línea " + $1.ival);
									  System.out.println("ABURUBA ASIGNACION - EXPRESION ---> " + $3.sval + ", " + $3.ival);
									  if (! tablaSimbolos.varDefined($1.sval))
									  	yyerror("\tError en la línea " + $1.ival + ": VARIABLE NO DEFINIDA"); 

									  if (tablaSimbolos.getType($3.sval).equals("ULONG") && tablaSimbolos.getVarType($1.sval).equals("UINT"))
									  	yyerror("\tError en la línea " + $1.ival + ": Se quiere asignar valor ULONG a variable declarada como UINT");
									  else
									  	System.out.println("Valor de la expresión: " + $3.sval);
									}
		   | ID ASIGN expresion { yyerror("\tLínea " + $1.ival + ". Asignación incompleta. Falta DOT"); }
;

bloque : sentencia_bloque bloque
	   | sentencia_bloque
;

bloque_funcion : sentencias
			   | declaracion_variables bloque_funcion
;

bloque_control : bloque_simple | bloque_compuesto
;

bloque_compuesto : BEGIN sentencias END { System.out.println("Línea " + $1.ival + ". Bloque compuesto"); }
				 
;

bloque_simple : sentencia
;

sentencia_bloque : asignacion
				 | ejecutable
				 | control
				 | declaracion
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
List<String> auxVariables = new ArrayList<>();