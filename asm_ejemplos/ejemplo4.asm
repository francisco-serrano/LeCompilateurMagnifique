.386 
.model flat, stdcall         ;Modelo de memoria 'pequeño' 
.stack 200h                  ;Tamaño de la pila
option casemap :none
include \masm32\include\windows.inc 
include \masm32\include\kernel32.inc 
include \masm32\include\user32.inc 
includelib \masm32\lib\kernel32.lib 
includelib \masm32\lib\user32.lib 

.data 
@555 DW 555
@99999999 DD 99999999
@100 DW 100
var1@main DW ?
var2@main@aa DW ?
retUINT_aa DW ?
retULONG_bb DD ?

.code
start:
CALL @FUNCTION_aa
MOV AX, 100
SUB AX, retUINT_aa
MOV var1@main, AX
JMP @LABEL_END


@FUNCTION_aa:
MOV AX, 555
MOV var2@main@aa, AX
MOV retUINT_aa, var2@main@aa
RET

@FUNCTION_bb:
MOV retULONG_bb, 99999999
RET

@LABEL_END:
invoke ExitProcess, 0
end start