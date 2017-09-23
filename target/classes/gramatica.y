%token ID CTE ASIGN ADD SUB MULT DIV
%start programa

%%

programa : asignacion
;

asignacion : ID ASIGN expresion
;

expresion : expresion ADD termino | expresion SUB termino | termino
;

termino : termino MULT factor | termino DIV factor | factor
;

factor : ID | CTE
;

%%