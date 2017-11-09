.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib

.data
mensaje_exito db "AX >= BX", 0

variableRetornoUINT DW ?
variableRetornoULONG DD ?

.code
start:

mov ax, 1
mov bx, 2

call @FUNCTION_aa

cmp ax, bx
jge @LABEL_MENSAJE

jmp @LABEL_END ;Tengo que poner un salto incondicional antes de que arranque el código de la funciones

@FUNCTION_aa:
    mov ax, 3
    ret

@LABEL_MENSAJE:
invoke MessageBox, NULL, addr mensaje_exito, addr mensaje_exito, MB_OK

@LABEL_END:
invoke ExitProcess, 0
end start