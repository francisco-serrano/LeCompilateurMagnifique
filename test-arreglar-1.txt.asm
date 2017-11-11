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
@1 DW 1
@2 DW 2
@25 DW 25
@100 DW 100
@7 DW 7
@4 DW 4
@50 DW 50
Rama_if db "Rama if", 0 
Rama_else db "Rama else", 0 
vavava db "vavava", 0 
var1@main DW ?
tempAX DW ?
tempBX DW ?
tempCX DW ?
tempDX DW ?
tempEAX DD ?
tempEBX DD ?
tempECX DD ?
tempEDX DD ?
mensaje_division_cero db "HOLA SOY UN ERROR EN TIEMPO DE EJECUCION -> DIVIDIR POR CERO VIOLA EL CODIGO ESTETICO Y MORAL", 0
mensaje_overflow_producto db "HOLA SOY UN ERROR EN TIEMPO DE EJECUCION -> OVERFLOW EN PRODUCTO", 0

.code
start:
MOV AX, 1
MOV var1@main, AX
MOV AX, var1@main
MOV BX, 2
CMP AX, BX
JLE Label7
MOV AX, 25
MOV var1@main, AX
invoke MessageBox, NULL, addr Rama_if, addr Rama_if, MB_OK
JMP Label12
Label7:
MOV AX, 100
MUL @1
JO @LABEL_OVF_PRODUCTO

MOV tempDX, DX
MOV DX, 0
CMP DX, @7
JZ @LABEL_DIV_CERO
DIV @7
MOV DX, tempDX
ADD AX, 100
MOV var1@main, AX
invoke MessageBox, NULL, addr Rama_else, addr Rama_else, MB_OK
Label12:
MOV AX, var1@main
ADD AX, var1@main
SUB AX, var1@main
MOV BX, 100
MOV tempAX, AX
MOV AX, BX
MUL @1
JO @LABEL_OVF_PRODUCTO

MOV BX, AX
MOV AX, tempAX
MOV tempAX, AX
MOV AX, BX
MOV tempDX, DX
MOV DX, 0
CMP DX, @4
JZ @LABEL_DIV_CERO
DIV @4
MOV DX, tempDX
MOV BX, AX
MOV AX, tempAX
ADD BX, 100
SUB BX, 50
ADD BX, 50
CMP AX, BX
JNE Label22
invoke MessageBox, NULL, addr vavava, addr vavava, MB_OK


@LABEL_OVF_PRODUCTO:
invoke MessageBox, NULL, addr mensaje_overflow_producto, addr mensaje_overflow_producto, MB_OK
JMP @LABEL_END
@LABEL_DIV_CERO:
invoke MessageBox, NULL, addr mensaje_division_cero, addr mensaje_division_cero, MB_OK
@LABEL_END:
invoke ExitProcess, 0
end start
