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
@40 DW 40
@30 DW 30
@50 DW 50
@25 DW 25
@5 DW 5
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
MOV AX, 40
MOV var1@main, AX
Label2:
MOV AX, var1@main
MOV BX, 30
CMP AX, BX
JLE Label12
MOV AX, var1@main
MOV BX, 50
CMP AX, BX
JLE Label8
MOV AX, 25
MOV var1@main, AX
JMP Label11
Label8:
invoke MessageBox, NULL, addr Rama_else, addr Rama_else, MB_OK
MOV AX, var1@main
ADD AX, 5
MOV var1@main, AX
Label11:
JMP Label2
Label12:
JMP @LABEL_END
JMP @LABEL_END


@LABEL_END:
invoke ExitProcess, 0
end start

@LABEL_END:
invoke ExitProcess, 0
end start
