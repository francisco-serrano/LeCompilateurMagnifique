%token ID CTE_UINT ASIGN
%start programa

%%

programa : asignacion
;

asignacion : ID ASIGN expresion
;

expresion : expresion '+' termino | expresion '-' termino | termino
;

termino : termino '*' factor | termino '/' factor | factor
;

factor : ID | CTE_UINT
;

%%