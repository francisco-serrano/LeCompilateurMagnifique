.386 
.model flat, stdcall         ;Modelo de memoria 'pequeño' 
.stack 200h                  ;Tamaño de la pila
option casemap :none
include C:\masm32\include\windows.inc 
include C:\masm32\include\kernel32.inc 
include C:\masm32\include\user32.inc 
include C:\masm32\include\masm32.inc 
includelib C:\masm32\lib\kernel32.lib 
includelib C:\masm32\lib\user32.lib 
includelib C:\masm32\lib\masm32.lib 

.data 
@1234 DW 1234
@100 DW 100
var1@main DW ?
var2@main DW ?
var3@main@aa DW ?
retUINT_aa DW ?
tempAX DW ?
tempBX DW ?
tempCX DW ?
tempDX DW ?
tempEAX DD ?
tempEBX DD ?
tempECX DD ?
tempEDX DD ?

.code
start:
MOV AX, 100
MOV var1@main, AX
CALL @FUNCTION_aa
MOV AX, var1@main
SUB AX, retUINT_aa
MOV var2@main, AX
JMP @LABEL_END
JMP @LABEL_END


@FUNCTION_aa:
MOV AX, 1234
MOV var3@main@aa, AX
MOV AX, 100
MOV retUINT_aa, AX
RET

@LABEL_END:
invoke ExitProcess, 0
end start

@FUNCTION_aa:
MOV AX, 1234
MOV var3@main@aa, AX
MOV AX, 100
MOV retUINT_aa, AX
RET

@LABEL_END:
invoke ExitProcess, 0
end start
