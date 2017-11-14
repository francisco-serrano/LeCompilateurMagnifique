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
@111076 DD 111076
@100 DW 100
var1@main DD ?
var2@main DW ?
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
MOV EAX, 111076
MOV var1@main, EAX
MOV AX, 100
MOV var2@main, AX
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
