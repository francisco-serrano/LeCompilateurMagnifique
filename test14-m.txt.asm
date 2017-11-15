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
@2 DW 2
@3 DW 3
@6 DW 6
entra_al_primer_if db "entra al primer if", 0 
entra_al_segundo_if db "entra al segundo if", 0 
var1@main DW ?
var2@main DW ?
tempAX DW ?
tempBX DW ?
tempCX DW ?
tempDX DW ?
tempEAX DD ?
tempEBX DD ?
tempECX DD ?
tempEDX DD ?
mensaje_division_cero db "ERROR EN TIEMPO DE EJECUCION -> DIVISION POR CERO", 0
mensaje_overflow_producto db "ERROR EN TIEMPO DE EJECUCION -> OVERFLOW EN PRODUCTO", 0

.code
start:
MOV AX, 2
MOV var1@main, AX
MOV AX, 3
MOV var2@main, AX
MOV AX, var1@main
MUL var2@main
JO @LABEL_OVF_PRODUCTO
MOV var1@main, AX
MOV AX, var2@main
MUL @2
JO @LABEL_OVF_PRODUCTO
MOV var2@main, AX
MOV AX, var1@main
MOV BX, 6
CMP AX, BX
JNE Label10
invoke MessageBox, NULL, addr entra_al_primer_if, addr entra_al_primer_if, MB_OK
Label10:
MOV AX, var2@main
MOV BX, 6
CMP AX, BX
JNE Label13
invoke MessageBox, NULL, addr entra_al_segundo_if, addr entra_al_segundo_if, MB_OK
Label13:
JMP @LABEL_END
JMP @LABEL_END


@LABEL_OVF_PRODUCTO:
invoke MessageBox, NULL, addr mensaje_overflow_producto, addr mensaje_overflow_producto, MB_OK
JMP @LABEL_END
@LABEL_DIV_CERO:
invoke MessageBox, NULL, addr mensaje_division_cero, addr mensaje_division_cero, MB_OK
@LABEL_END:
invoke ExitProcess, 0
end start

@LABEL_OVF_PRODUCTO:
invoke MessageBox, NULL, addr mensaje_overflow_producto, addr mensaje_overflow_producto, MB_OK
JMP @LABEL_END
@LABEL_DIV_CERO:
invoke MessageBox, NULL, addr mensaje_division_cero, addr mensaje_division_cero, MB_OK
@LABEL_END:
invoke ExitProcess, 0
end start
