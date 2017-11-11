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
@50 DW 50
@100 DW 100
QUINCE_PESOS db "QUINCE PESOS", 0 
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
mensaje_division_cero db "HOLA SOY UN ERROR EN TIEMPO DE EJECUCION -> DIVIDIR POR CERO VIOLA EL CODIGO ESTETICO Y MORAL", 0
mensaje_overflow_producto db "HOLA SOY UN ERROR EN TIEMPO DE EJECUCION -> OVERFLOW EN PRODUCTO", 0

.code
start:
MOV AX, 100
MOV var1@main, AX
CALL @FUNCTION_aa
MOV AX, var1@main
SUB AX, retUINT_aa
MOV var2@main, AX
MOV AX, var1@main
MOV BX, var2@main
CMP AX, BX
JLE Label11
invoke MessageBox, NULL, addr QUINCE_PESOS, addr QUINCE_PESOS, MB_OK
Label11:
JMP @LABEL_END
JMP @LABEL_END


@FUNCTION_aa:
MOV AX, 50
MOV var3@main@aa, AX
MOV AX, var3@main@aa
MOV retUINT_aa, AX
RET

@LABEL_OVF_PRODUCTO:
invoke MessageBox, NULL, addr mensaje_overflow_producto, addr mensaje_overflow_producto, MB_OK
JMP @LABEL_END
@LABEL_DIV_CERO:
invoke MessageBox, NULL, addr mensaje_division_cero, addr mensaje_division_cero, MB_OK
@LABEL_END:
invoke ExitProcess, 0
end start

@FUNCTION_aa:
MOV AX, 50
MOV var3@main@aa, AX
MOV AX, var3@main@aa
MOV retUINT_aa, AX
RET

@LABEL_OVF_PRODUCTO:
invoke MessageBox, NULL, addr mensaje_overflow_producto, addr mensaje_overflow_producto, MB_OK
JMP @LABEL_END
@LABEL_DIV_CERO:
invoke MessageBox, NULL, addr mensaje_division_cero, addr mensaje_division_cero, MB_OK
@LABEL_END:
invoke ExitProcess, 0
end start
