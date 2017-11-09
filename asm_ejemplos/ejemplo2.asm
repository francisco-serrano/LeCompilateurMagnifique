.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib

.data
var1@main DW ?
var2@main@aa DW ?
tempUINT DW ?
tempULONG DD ?
retUINT DW ?
retULONG DD ?

mensaje_salto db "SALTO", 0
mensaje_no_salto db "NO SALTO", 0

.code
start:

mov ax, 100
mov bx, 1
mul bx

mov tempUINT, ax	; Guardo en TEMP el valor de AX
call @FUNCTION_aa	; Llamo a la función aa()
mov retUINT, ax 	; Muevo el retorno de la función a RET
mov ax, tempUINT	; Recupero el valor que había en AX
add retUINT, ax 	; Hago la operación ret->[aa()] + ax->[100 * 1]
mov ax, retUINT		; Guardo en AX el resultado de la expresión
mov var1@main, ax 	; Guardo en var1 en resultado de la expresión

mov bx, 300
cmp ax, bx
je @LABEL_SALTO
jmp @LABEL_NO_SALTO

@LABEL_SALTO:
invoke MessageBox, NULL, addr mensaje_salto, addr mensaje_salto, MB_OK
jmp @LABEL_END

@LABEL_NO_SALTO:
invoke MessageBox, NULL, addr mensaje_no_salto, addr mensaje_no_salto, MB_OK
jmp @LABEL_END


@FUNCTION_aa:
    mov ax, 200
    mov var2@main@aa, ax
    mov ax, var2@main@aa
    ret

@LABEL_END:
invoke ExitProcess, 0

end start