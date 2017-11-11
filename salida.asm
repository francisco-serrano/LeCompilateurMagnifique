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
@0 DW 0
@100 DW 100
@1 DW 1
ENTRA_AL_IF db "ENTRA AL IF", 0 
ENTRA_AL_ELSE db "ENTRA AL ELSE", 0 
var1@main DW ?
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
MOV tempDX, DX
MOV DX, 0
CMP DX, @1
JZ @LABEL_DIV_CERO
DIV @1
MOV DX, tempDX
MOV var1@main, AX
MOV AX, var1@main
MOV BX, 100
CMP AX, BX
JNE Label9
invoke MessageBox, NULL, addr ENTRA_AL_IF, addr ENTRA_AL_IF, MB_OK
JMP Label10
Label9:
invoke MessageBox, NULL, addr ENTRA_AL_ELSE, addr ENTRA_AL_ELSE, MB_OK
Label10:
JMP @LABEL_END
JMP @LABEL_END


@FUNCTION_aa:
MOV AX, 0
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
MOV AX, 0
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
