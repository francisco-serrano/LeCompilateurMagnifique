%{
	package parser;
	import lexer.TablaSimbolos;
	import lexer.Terceto;
	
	import lexer.Item;
	import lexer.ItemString;
	import lexer.ItemTerceto;
	import lexer.Lexer;
	import lexer.TablaSimbolos;
	import java.util.Stack;
    import java.util.Vector;
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
													uso = "variable";
													
													if (!tablaSimbolos.redefined(auxVariables, ambitos.toString()))
														tablaSimbolos.defineVar(auxVariables, $3.sval, uso, ambitos.toString());
													else 
														yyerror("\tLínea " + $2.ival + ". Re-Declaracion de variables.");
													
													
													auxVariables.clear();
												 }
					  | lista_var COLON tipo { yyerror("\tLínea " + $2.ival + ". Declaración de variables incompleta. Falta DOT"); }
					  | lista_var tipo DOT { yyerror("\tLínea " + $3.ival + ". Declaración de variables incompleta. Falta COLON"); }
;

lista_var : lista_var COMMA ID { auxVariables.add($3.sval.toLowerCase()); }
			| ID { auxVariables.add($1.sval.toLowerCase()); }
			| lista_var ID { yyerror("\tLínea " + $2.ival + ". Declaración incompleta. Falta COMMA"); }
;

tipo : UINT | ULONG
;

declaracion_funcion : tipo FUNCTION ID { ambitos.push($3.sval); } cuerpo_funcion { 
																					uso = "nombre_funcion";

																					if (!tablaSimbolos.functionDefined($3.sval)){
																						System.out.println("Declaracion de funcion. Línea " + $2.ival);
																						auxVariables.clear();
																						auxVariables.add($3.sval);
																						tablaSimbolos.defineVar(auxVariables, $1.sval, uso, ambitos.toString());
																					} else {
																						yyerror("\tLínea " + $2.ival + ". Redeclaracion de funcion.");
																					}

																					ambitos.pop();
																					auxVariables.clear();
																				 }
					| FUNCTION ID cuerpo_funcion { yyerror("\tLínea " + $1.ival + ". Declaración de función incompleta. Falta tipo de retorno"); }
					| tipo MOVE FUNCTION ID { ambitos.push($4.sval); isMoveFunction = true; } cuerpo_funcion { 	
																												uso = "nombre_funcion";

																												if (!tablaSimbolos.functionDefined($4.sval)){
																													System.out.println("Declaracion de funcion. Línea " + $3.ival);
																													auxVariables.clear();
																													auxVariables.add($4.sval);
																													tablaSimbolos.defineVar(auxVariables, $1.sval, uso, ambitos.toString());
																												} else {
																													yyerror("\tLínea " + $3.ival + ". Redeclaracion de funcion.");
																												}

																												ambitos.pop();
																												isMoveFunction = false;
																												auxVariables.clear();
																											}
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

print : OUT OPEN_PAR CADENA CLOSE_PAR DOT { 
											System.out.println("Sentencia OUT. Línea " + $1.ival); 
											Terceto t = new Terceto("PRINT", new ItemString($3.sval), new ItemString("-"), null);
											tercetos.add(t);
										  }
	  | OUT OPEN_PAR CADENA CLOSE_PAR { yyerror("\tLínea " + $1.ival + ". Estructura OUT incompleta. Falta DOT"); }
	  | OUT OPEN_PAR CADENA DOT { yyerror("\tLínea " + $1.ival + ". Estructura OUT incompleta. Falta CLOSE_PAR"); }
	  | OUT CADENA CLOSE_PAR DOT { yyerror("\tLínea " + $1.ival + ". Estructura OUT incompleta. Falta OPEN_PAR"); }
	  | OUT OPEN_PAR expresion CLOSE_PAR DOT { yyerror("\tLínea " + $1.ival + ". Estructura OUT incorrecta. Sólo se pueden imprimir cadenas"); }
;

asignacion : ID ASIGN expresion DOT { System.out.println("ASIGNACIÓN. Línea " + $1.ival);
									  Item item2 = (Item)$3.obj;
									  Terceto terceto = null;	
									  if (! tablaSimbolos.varDefined($1.sval, ambitos.toString(), isMoveFunction))
									  	  yyerror("\tError en la línea " + $1.ival + ": VARIABLE NO DEFINIDA EN EL AMBITO -> " + ambitos.toString());
									  else {
										  String tipoAsignacion = tablaSimbolos.getToken($1.sval.toLowerCase()).getType();
										  String tipoExpresion = (String)(((Item)$3.obj).getTipo());
										  if (!tipoAsignacion.equals(tipoExpresion))
                                              yyerror("Línea " + $2.ival + ". Tipos incompatibles en la asignación");

										  /*
										  if ((!tipoAsignacion.equals("ULONG")) && (tipoExpresion.equals("ULONG")))
											  yyerror("Línea " + $2.ival + ". Tipos incompatibles en la asignación");
										  */

									  }

									  terceto = new Terceto("=", new ItemString((String)$1.sval), item2, null);
								      tercetos.add(terceto);
									  $$.obj = new ItemTerceto(terceto);
									}
		   | ID ASIGN expresion { yyerror("\tLínea " + $1.ival + ". Asignación incompleta. Falta DOT"); }
;

bloque_funcion : bloque_funcion bloque | bloque
;

bloque : declaracion_variables | asignacion | print | seleccion | iteracion
;

seleccion : seleccion_simple else bloque_sentencias END_IF { 
																System.out.println("Línea " + $1.ival + ". Sentencia IF-ELSE"); 
																((Terceto)tercetos.get((pila.pop()).intValue() - 1)).setArg1(new ItemString("[" + (tercetos.size() + 1) + "]"));
															}
		  | seleccion_simple END_IF { 
										System.out.println("Línea " + $1.ival + ". Sentencia IF"); 
										((Terceto)tercetos.get((pila.pop()).intValue() - 1)).setArg2(new ItemString("[" + (tercetos.size() + 1) + "]"));
									}
;

else: ELSE {
				((Terceto)tercetos.get((pila.pop()).intValue() - 1)).setArg2(new ItemString("[" + (tercetos.size() + 2) + "]"));
				Terceto t = new Terceto("BI", new ItemString("_"), new ItemString("_"), null);
				tercetos.add(t);
				pila.push(new Integer(t.getNumero()));
		   }
;

seleccion_simple : IF condicion_if THEN bloque_sentencias
;

condicion_if : condicion {
							Terceto t = new Terceto("BF", new ItemTerceto((Terceto)(tercetos.get(tercetos.size() - 1))), new ItemString("_"), null);
							tercetos.add(t);
							pila.push(new Integer(t.getNumero()));	
						 }	
;

condicion : OPEN_PAR expresion comparador expresion CLOSE_PAR { 
																System.out.println("Comparación. Línea " + $3.ival); 
																String tipo1=(String)(((Item)$2.obj).getTipo());
																String tipo2=(String)(((Item)$4.obj).getTipo());
				
																Item item1 = (Item)$2.obj;
																Item item2 = (Item)$4.obj;
																
																Terceto t = new Terceto($3.sval, item1, item2, null);
																tercetos.add(t);												  
																$$.obj = new ItemTerceto(t);
															  }
		  | expresion comparador expresion CLOSE_PAR { yyerror("Línea " + $2.ival + ". Condicion incompleta. Falta OPEN_PAR"); }
		  | OPEN_PAR expresion comparador expresion { yyerror("Línea " + $3.ival + ". Condicion. Falta CLOSE_PAR"); }
;

bloque_sentencias : bloque_simple 
				  | BEGIN bloque_compuesto END { System.out.println("Línea " + $1.ival + ". Bloque compuesto"); }
				  | BEGIN bloque_compuesto error { yyerror("Línea " + $1.ival + ". Bloque compuesto. Falta END"); }
;

bloque_simple : asignacion | seleccion | iteracion | print
;

bloque_compuesto : bloque_compuesto bloque_simple | bloque_simple
;

iteracion : while condicion_while DO bloque_sentencias { 
															System.out.println("Línea " + $1.ival + ". Estructura WHILE"); 
															System.out.println(" FINALIZA ITERACION PILA" + pila.toString());
															((Terceto)tercetos.get((pila.pop()).intValue() - 1)).setArg2(new ItemString("[" + (tercetos.size() + 2) + "]"));
															Terceto t = new Terceto("BI", new ItemTerceto((Terceto)tercetos.get((pila.pop()).intValue() - 1)), new ItemString("_"), null);
															tercetos.add(t);
													   }
;

while : WHILE{
				System.out.println(" ARRANCA WHILE PILA" + pila.toString());
				pila.push(new Integer(tercetos.size() + 1));
			 }
;

condicion_while : condicion {
								System.out.println(" ARRANCA CONDICION_WHILE PILA" + pila.toString());
								Terceto t = new Terceto("BF", new ItemTerceto((Terceto)(tercetos.get(tercetos.size() - 1))), new ItemString("_"), null);
								tercetos.add(t);
								System.out.println("NUMERO! " + t.getNumero());
								pila.push(new Integer(t.getNumero()));
                            }
;

expresion : expresion ADD termino { 
									System.out.println("SUMA. Línea " + $2.ival); 
									String tipo1 = (String)(((Item)$1.obj).getTipo());
									String tipo2 = (String)(((Item)$3.obj).getTipo());
									Item item1 = (Item)$1.obj;
									Item item2 = (Item)$3.obj;
									if (!tipo1.equals(tipo2))
										yyerror ("Línea " + $2.ival + ". Tipos incompatibles en la suma");
									Terceto t = new Terceto("+", item1, item2, tipo1);
									tercetos.add(t);
									$$.obj = new ItemTerceto(t); 
								  } 
		  | expresion SUB termino { 
									System.out.println("RESTA. Línea " + $2.ival); 
									String tipo1 = (String)(((Item)$1.obj).getTipo());
									String tipo2 = (String)(((Item)$3.obj).getTipo());
									Item item1 = (Item)$1.obj;
									Item item2 = (Item)$3.obj;
									if (!tipo1.equals(tipo2))
										yyerror("Línea " + $2.ival + ". Tipos incompatibles en la resta");
									Terceto t = new Terceto("-", item1, item2, tipo1);
									tercetos.add(t);
									$$.obj = new ItemTerceto(t);
								  } 
		  | termino { $$.obj = $1.obj; }
;

termino : termino MULT factor { 
								System.out.println("MULTIPLICACION. Línea " + $2.ival); 
								String tipo1 = (String)(((Item)$1.obj).getTipo());
								String tipo2 = (String)(((Item)$3.obj).getTipo());
								Item item1 = (Item)$1.obj;
								Item item2 = (Item)$3.obj;
								if (!tipo1.equals(tipo2))
									yyerror("Línea " + $2.ival + ". Tipos incompatibles en la multiplicación");
								Terceto t = new Terceto("*", item1, item2, tipo1);
								tercetos.add(t);
								$$.obj = new ItemTerceto(t);
							  } 
		| termino DIV factor { 
								System.out.println("DIVISION. Línea " + $2.ival); 
								String tipo1 = (String)(((Item)$1.obj).getTipo());
								String tipo2 = (String)(((Item)$3.obj).getTipo());
								Item item1 = (Item)$1.obj;
								Item item2 = (Item)$3.obj;
								if (!tipo1.equals(tipo2))
									yyerror ("Línea " + $2.ival + ". Tipos incompatibles en la division");
								Terceto t = new Terceto("/", item1, item2, tipo1);
								tercetos.add(t);
								$$.obj = new ItemTerceto(t);
							 } 
		| factor { $$.obj = $1.obj; }
;

factor : ID { 
				System.out.println("Lectura de la variable " + $1.sval + ". Línea " + $1.ival); 

				if (tablaSimbolos.functionDefined($1.sval))
					yyerror("Línea " + $1.ival + ": NOMBRE DE FUNCION COMO OPERANDO --> FALTAN LOS PARENTESIS");
			    else if (!tablaSimbolos.varDefined($1.sval, ambitos.toString(), isMoveFunction))
			  		yyerror("\tError en la línea " + $1.ival + ": VARIABLE -> " + $1.sval + " NO DEFINIDA EN EL AMBITO -> " + ambitos.toString()); 
			  		
			    String id = $1.sval.toLowerCase();
			    ItemString itemString = new ItemString(id);
			    itemString.setTabla(tablaSimbolos);
			    $$.obj = itemString;
			}
	   | CTE { String cte = $1.sval;
			   ItemString itemString = new ItemString(cte);
			   itemString.setTabla(tablaSimbolos);
			   $$.obj = itemString;
			 }
	   | invocacion_funcion { $$.obj = $1.obj; }
;

comparador : LEQ 
		   | GEQ 
		   | LT 
		   | GT 
		   | EQ 
		   | NEQ
;

invocacion_funcion : ID OPEN_PAR CLOSE_PAR { 
												if (! tablaSimbolos.functionDefined($1.sval)) 
													yyerror("\tError en la línea " + $1.ival + ": FUNCION NO DEFINIDA"); 
											
												System.out.println("Invocación a función. Línea " + $1.ival); 
												String id = $1.sval;
												ItemString itemString = new ItemString(id);
												itemString.setTabla(tablaSimbolos);
												$$.obj = itemString;
													
											}
;

%%

void yyerror(String error) {
	bufferErrores.add(error);
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
	return tercetos;
} 

public List<String> getErrores() {
	return bufferErrores;
}

Lexer lexer;

TablaSimbolos tablaSimbolos;

List<Terceto> tercetos = new ArrayList<>();
Stack<Integer> pila = new Stack<>();

List<String> bufferErrores = new ArrayList<>();

String uso;
boolean isMoveFunction = false;

List<String> auxVariables = new ArrayList<>();

CustomStack<String> ambitos = new CustomStack<>("main");