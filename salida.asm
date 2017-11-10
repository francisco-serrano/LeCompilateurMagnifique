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
@3 DW 3
@2 DW 2
@1 DW 1
@30 DW 30
@25 DW 25
Rama_if db "Rama if", 0 
Rama_else db "Rama else", 0 
var1@main DW ?
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
MOV AX, 3
MOV var1@main, AX
MOV AX, var1@main
MOV BX, 2
CMP AX, BX
JLE Label10
Label4:
MOV AX, var1@main
MOV BX, 2
CMP AX, BX
JLE Label9
invoke MessageBox, NULL, addr Rama_if, addr Rama_if, MB_OK
MOV AX, 1
MOV var1@main, AX
JMP Label4
Label9:
JMP Label15
Label10:
MOV AX, var1@main
MOV BX, 30
CMP AX, BX
JGE Label15
invoke MessageBox, NULL, addr Rama_else, addr Rama_else, MB_OK
MOV AX, 25
MOV var1@main, AX
JMP Label10
Label15:
JMP @LABEL_END
JMP @LABEL_END


@LABEL_END:
invoke ExitProcess, 0
end start

@LABEL_END:
invoke ExitProcess, 0
end start
