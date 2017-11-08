.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib


.data
@3E0 dq 3
a dq ?
i dd ?
m dd ?
lalala db "lalala" , 0
print_function db "Print de la funcion" , 0
@3 dq 3
@5 dq 5
overflow db "Ha ocurrido Overflow." , 0
sf db "Se ha obtenido un valor con signo en tipos sin signo." , 0
cf db "Ha ocurrido Overflow." , 0
divideZero db "Se ha intentado dividir por cero." , 0
incomp db "Se ha producido un error de tipos." , 0

.code

start:
mov m, 5
mov eax, m
add eax, 3
mov ebx, eax
JC @LABEL_CF
mov i, ebx
FLD @3E0
FSTP a
call myfunction
invoke MessageBox, NULL, addr lalala, addr lalala, MB_OK

JMP @LABEL_END

myfunction:
invoke MessageBox, NULL, addr print_function, addr print_function, MB_OK
ret

@LABEL_CF: 
invoke MessageBox, NULL, addr cf, addr cf, MB_OK
JMP @LABEL_END
@LABEL_SF: 
invoke MessageBox, NULL, addr sf, addr sf, MB_OK
JMP @LABEL_END
@LABEL_OVERFLOW: 
invoke MessageBox, NULL, addr overflow, addr overflow, MB_OK
JMP @LABEL_END
@LABEL_DIVIDEZERO: 
invoke MessageBox, NULL, addr divideZero, addr divideZero, MB_OK
JMP @LABEL_END
@LABEL_IMCOMPTIPO: 
invoke MessageBox, NULL, addr incomp, addr incomp, MB_OK
JMP @LABEL_END
@LABEL_END: 
invoke ExitProcess, 0
end start
