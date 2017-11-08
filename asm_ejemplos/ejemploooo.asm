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

.code
start:

mov ax, 1
mov bx, 2

call funcion_aa

cmp ax, bx
jge @LABEL_MENSAJE

jmp @LABEL_END ;Tengo que poner un salto incondicional antes de que arranque el código de la funciones

funcion_aa:
    mov ax, 3
    ret

@LABEL_MENSAJE:
invoke MessageBox, NULL, addr mensaje_exito, addr mensaje_exito, MB_OK

@LABEL_END:
invoke ExitProcess, 0
end start