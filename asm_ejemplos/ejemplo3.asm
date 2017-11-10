.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib

.data

retUINT_aa dw ?
mensaje db "QUE ONDA BIGOTE", 0

.code
start:

mov retUINT_aa, 200
mov bx, 300

call @FUNCTION_aa

mov ax, retUINT_aa

cmp ax, bx
je @MENSAJE

jmp @LABEL_END


@FUNCTION_aa:
    mov retUINT_aa, 300
    ret


@MENSAJE:
invoke MessageBox, NULL, addr mensaje, addr mensaje, MB_OK

@LABEL_END:
invoke ExitProcess, 0

end start