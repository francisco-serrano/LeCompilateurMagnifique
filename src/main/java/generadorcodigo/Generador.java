package generadorcodigo;

import com.google.common.base.Splitter;
import com.google.common.collect.Multimap;
import lexer.*;

import java.util.*;
import java.util.stream.Collectors;

public class Generador {

    private static final int CANTIDAD_REGISTROS = 4;

    private List<Terceto> tercetos;
    private TablaRegistros tablaRegistrosUINT = new TablaRegistros(CANTIDAD_REGISTROS);
    private TablaRegistros tablaRegistrosULONG = new TablaRegistros(CANTIDAD_REGISTROS);
    private StringBuilder code = new StringBuilder();
    private TablaSimbolos tablita = new TablaSimbolos();
    private Map<String, String> mapaSaltos = new HashMap<>();

    private String lastCompOperator;
    private List<Integer> listaDireccionesSalto = new ArrayList<>();

    private boolean isFuncion = false;
    private String nombreFuncion = "";

    private StringBuilder codigoFuncion = new StringBuilder();
    private List<String> listaInstFunc = new ArrayList<>();
    private List<String> listaInstrucciones = new ArrayList<>();

    private boolean[] seGuardoUINT = {false, false, false, false};
    private boolean[] seGuardoULONG = {false, false, false, false};

    public Generador(List<Terceto> tercetos, TablaSimbolos ts) {
        this.tercetos = tercetos;
        this.tablita = ts;

        buildMap();
    }

    public String getCode() {
        code.append(codigoFuncion);
        return code.toString();
    }

    public List<String> getListaInstrucciones() {
        listaInstrucciones.addAll(listaInstFunc);
        return listaInstrucciones;
    }

    public void generateAssembler() {
        printHeader();

        declararVariables();

        code.append("\n.code\nstart:\n");

        for (Terceto terceto : tercetos)
            assembleTerceto(terceto);

        listaInstFunc = Splitter.on("\n").splitToList(codigoFuncion.toString());
        listaInstrucciones = Splitter.on("\n").splitToList(code.toString());

        listaInstrucciones = intercalarLabels(listaInstrucciones);
        listaInstFunc = intercalarLabels(listaInstFunc);

        listaInstrucciones = eliminarNumeros(listaInstrucciones);
        listaInstFunc = eliminarNumeros(listaInstFunc);

        listaInstFunc.add("@LABEL_END:");
        listaInstFunc.add("invoke ExitProcess, 0");
        listaInstFunc.add("end start");
    }

    private void printHeader() {
        this.code.append(".386 \n");
        this.code.append(".model flat, stdcall         ;Modelo de memoria 'pequeño' \n");
        this.code.append(".stack 200h                  ;Tamaño de la pila\n");
        this.code.append("option casemap :none\n");
        this.code.append("include C:\\masm32\\include\\windows.inc \n");
        this.code.append("include C:\\masm32\\include\\kernel32.inc \n");
        this.code.append("include C:\\masm32\\include\\user32.inc \n");
        this.code.append("include C:\\masm32\\include\\masm32.inc \n");
        this.code.append("includelib C:\\masm32\\lib\\kernel32.lib \n");
        this.code.append("includelib C:\\masm32\\lib\\user32.lib \n");
        this.code.append("includelib C:\\masm32\\lib\\masm32.lib \n");
    }

    private void declararVariables() {
        this.code.append("\n.data \n");
        Multimap<String, Token> mapa = tablita.getTabla();
        Collection<Token> e = mapa.values();
        Iterator<Token> it = e.iterator();
        while (it.hasNext()) {
            Token t = it.next();
            if (!t.getUso().equals("undefined")) {
                switch (t.getUso()) {
                    case "cadena":
                        String p = t.getLexema().toString();
                        this.code.append(p.replace(" ", "_").replace("\'", "").replace(",", "_").replace(".", "_") + " db \"" + p.replace("\'", "") + "\", 0 \n");
                        break;
                    case "variable":
                        String prefijo = "@" + t.getAmbito();
                        if (t.getType().equals("UINT"))
                            this.code.append(t.getLexema().toString() + prefijo + " DW" + " ?\n");
                        if (t.getType().equals("ULONG"))
                            this.code.append(t.getLexema().toString() + prefijo + " DD" + " ?\n");
                        break;
                    case "nombre_funcion":
                        if (t.getType().equals("UINT"))
                            code.append("retUINT_").append(t.getLexema()).append(" DW").append(" ?\n");

                        if (t.getType().equals("ULONG"))
                            code.append("retULONG_").append(t.getLexema()).append(" DD").append(" ?\n");

                        break;
                    case "constante":
                        if (t.getType().equals("UINT")) {
                            this.code.append("@" + t.getLexema() + " DW " + t.getLexema().toString() + "\n");
                        }
                        if (t.getType().equals("ULONG")) {
                            this.code.append("@" + t.getLexema() + " DD " + t.getLexema().toString() + "\n");
                        }
                        break;

                }
            }
        }

        // Variables auxiliares para guardar los valores en los registros de 16 bits
        code.append("tempAX DW ?\n");
        code.append("tempBX DW ?\n");
        code.append("tempCX DW ?\n");
        code.append("tempDX DW ?\n");

        // Variables auxiliares para guardar los valores en los registros de 32 bits
        code.append("tempEAX DD ?\n");
        code.append("tempEBX DD ?\n");
        code.append("tempECX DD ?\n");
        code.append("tempEDX DD ?\n");
    }

    private void assembleTerceto(Terceto terceto) {

        List<String> listaOperadores = Arrays.asList("+", "-", "*", "/");
        List<String> listaComparadores = Arrays.asList("<=", ">=", "<", ">", "==", "<>");

        // OPERACIONES ARITMETICAS
        if (listaOperadores.contains(terceto.getOperador())) {

            String situacion = getSituacionAritmetica(terceto);

            assert situacion != null;

            switch (situacion) {
                case "SITUACION_UNO":
                    applySituacion_1(terceto);
                    break;
                case "SITUACION_DOS":
                    applySituacion_2(terceto);
                    break;
                case "SITUACION_TRES":
                    applySituacion_3(terceto);
                    break;
                case "SITUACION_CUATRO_A":
                    applySituacion_4a(terceto);
                    break;
                case "SITUACION_CUATRO_B":
                    applySituacion_4b(terceto);
                    break;
                default:
                    throw new RuntimeException("BAILA COMO EL PAPUU");
            }
        }

        // OPERACIONES DE ASIGNACION
        if (terceto.getOperador().equals("=")) {
            String situacion = getSituacionAsignacion(terceto);

            switch (situacion) {
                case "SITUACION_A":
                    applySituacionAsignacion_a(terceto);
                    break;

                case "SITUACION_B":
                    applySituacionAsignacion_b(terceto);
                    break;
            }
        }

        // SALIDA POR PANTALLA (OUT)
        if (terceto.getOperador().equals("PRINT")) {
            String auxiliar = terceto.getArg1().toString().replace("'", "").replace(" ", "_");
            code.append("invoke MessageBox, NULL, addr ").append(auxiliar).append(", addr ").append(auxiliar).append(", MB_OK\n");
        }

        // COMPARACION
        if (listaComparadores.contains(terceto.getOperador())) {
            applySituacionComparacion(terceto);
            lastCompOperator = terceto.getOperador();
        }

        // BRANCH IF FALSE (BF)
        if (terceto.getOperador().equals("BF"))
            applySituacionBF(terceto);


        // BRANCH INCONDITIONAL (BI)
        if (terceto.getOperador().equals("BI"))
            applySituacionBI(terceto);


        // Inicio declaración de función
        if (terceto.getOperador().equals("FUNCTION")) {
            isFuncion = true;
            nombreFuncion = terceto.getArg1().toItemString().getArg();
            codigoFuncion.append("\n@FUNCTION_").append(nombreFuncion).append(":\n");
        }

        // Retorno de la función
        if (terceto.getOperador().equals("RETURN")) {
            isFuncion = false;

            String tipoRetorno = tablita.getTypeFuncion(nombreFuncion);

            TablaRegistros tablaUtilizar = getTablaRegistros(tipoRetorno);
            int reg = tablaUtilizar.getFreeRegister();
            tablaUtilizar.occupyRegister(reg);
            String registroUtilizar = getRegistro_x86(tipoRetorno, reg);

            if (tipoRetorno.equals("UINT")) {
                codigoFuncion.append("MOV " + registroUtilizar + ", " + terceto.getArg1().toItemString().toString() + "\n");
                codigoFuncion.append("MOV retUINT_").append(nombreFuncion).append(", ").append(registroUtilizar).append("\n");
            }

            if (tipoRetorno.equals("ULONG")) {
                codigoFuncion.append("MOV " + registroUtilizar + ", " + terceto.getArg1().toItemString().toString() + "\n");
                codigoFuncion.append("MOV retULONG_").append(nombreFuncion).append(", ").append(registroUtilizar).append("\n");
            }

            tablaUtilizar.freeRegister(reg);

            nombreFuncion = "";
            codigoFuncion.append("RET\n");
        }

        // Llamadas a función, se hace backup de los registros
        if (terceto.getOperador().equals("CALL")) {

            if (terceto.getTipo().equals("UINT")) {
                if (tablaRegistrosUINT.isOccupied(0)) {
                    code.append("MOV tempAX, AX\n");
                    seGuardoUINT[0] = true;
                }

                if (tablaRegistrosUINT.isOccupied(1)){
                    code.append("MOV tempBX, BX\n");
                    seGuardoUINT[1] = true;
                }

                if (tablaRegistrosUINT.isOccupied(2)){
                    code.append("MOV tempCX, CX\n");
                    seGuardoUINT[2] = true;
                }

                if (tablaRegistrosUINT.isOccupied(3)){
                    code.append("MOV tempDX, DX\n");
                    seGuardoUINT[3] = true;
                }
            }

            if (terceto.getTipo().equals("ULONG")) {
                if (tablaRegistrosULONG.isOccupied(0)) {
                    code.append("MOV tempEAX, EAX\n");
                    seGuardoULONG[0] = true;
                }

                if (tablaRegistrosULONG.isOccupied(1)) {
                    code.append("MOV tempEBX, EBX\n");
                    seGuardoULONG[1] = true;
                }

                if (tablaRegistrosULONG.isOccupied(2)) {
                    code.append("MOV tempECX, ECX\n");
                    seGuardoULONG[2] = true;
                }

                if (tablaRegistrosULONG.isOccupied(3)) {
                    code.append("MOV tempEDX, EDX\n");
                    seGuardoULONG[3] = true;
                }
            }
        }

        // Terceto de término de programa
        if (terceto.getOperador().equals("END"))
            code.append(terceto.getNumero()).append("}JMP @LABEL_END\n");

    }

    private String getSituacionAritmetica(Terceto terceto) {
        String op1 = terceto.getArg1().toString();
        String op2 = terceto.getArg2().toString();

        String operador = terceto.getOperador();

        if ((!op1.contains("[") || op1.contains("@")) && (!op2.contains("[") || op2.contains("@")))
            return "SITUACION_UNO";

        if (op1.contains("[") && (!op2.contains("[") || op2.contains("@")))
            return "SITUACION_DOS";

        if (op1.contains("[") && op2.contains("["))
            return "SITUACION_TRES";

        if ((!op1.contains("[") || op1.contains("@")) && op2.contains("[")) {

            if (operador.equals("+") || operador.equals("*"))
                return "SITUACION_CUATRO_A";

            // Ya se supone que el operador es - o /
            return "SITUACION_CUATRO_B";
        }

        return null;
    }

    private String getSituacionAsignacion(Terceto terceto) {
        String op2 = terceto.getArg2().toString();

        if (op2.contains("["))
            return "SITUACION_A";

        return "SITUACION_B";
    }

    //  Operación entre: VAR/CTE y VAR/CTE
    private void applySituacion_1(Terceto terceto) {
        StringBuilder codigo = getCodeSection();

        String constantType = terceto.getTipo();
        TablaRegistros tablaUtilizar = getTablaRegistros(constantType);

        int reg = tablaUtilizar.getFreeRegister();
        tablaUtilizar.occupyRegister(reg);

        String registroUtilizar = getRegistro_x86(constantType, reg);
        terceto.setAssociatedRegister(registroUtilizar);

        codigo.append(terceto.getNumero()).append("}MOV ").append(registroUtilizar).append(", ").append(terceto.getArg1().toString()).append("\n");

        String operacion = getOperacion(terceto.getOperador());
        String segundoElemento = terceto.getArg2().toString();
        if (operacion.equals("MULT") || operacion.equals("DIV")) {
            String aux = "MUL";

            if (operacion.equals("DIV"))
                aux = "DIV";

            int segundoReg = tablaUtilizar.getFreeRegister();
            tablaUtilizar.occupyRegister(segundoReg);
            String segundoRegistro = getRegistro_x86(constantType, segundoReg);

            codigo.append(terceto.getNumero() + "}MOV " + segundoRegistro + ", " + segundoElemento + "\n");
            codigo.append(terceto.getNumero() + "}" + aux + " " + segundoRegistro + "\n");

            tablaUtilizar.freeRegister(segundoReg);

            return;
        }

        codigo.append(terceto.getNumero()).append("}").append(operacion).append(" ").append(registroUtilizar).append(", ").append(segundoElemento).append("\n");
    }

    // Operación entre: REGISTRO y VAR/CTE
    private void applySituacion_2(Terceto terceto) {
        // Obtengo sección .code del archivo
        StringBuilder codigo = getCodeSection();

        Terceto terceto1 = ((ItemTerceto) terceto.getArg1()).getArg();

        String registroUtilizar = terceto1.getAssociatedRegister();
        terceto.setAssociatedRegister(registroUtilizar);

        if (terceto1.getOperador().equals("CALL")) {
            String nombreFuncion = Splitter.on("@").splitToList(terceto1.getArg1().toString()).get(0);
            String variableRetorno = "ret" + terceto1.getTipo() + "_" + nombreFuncion;

            codigo.append(terceto.getNumero()).append("}CALL @FUNCTION_").append(nombreFuncion).append("\n");
            recoverRegisters(terceto1.getTipo());

            TablaRegistros tablaUtilizar = getTablaRegistros(terceto1.getTipo());
            int reg = tablaUtilizar.getFreeRegister();
            tablaUtilizar.occupyRegister(reg);
            registroUtilizar = getRegistro_x86(terceto1.getTipo(), reg);

            String operacion = getOperacion(terceto.getOperador());
            if (operacion.equals("MULT") || operacion.equals("DIV")) {
                if (operacion.equals("MULT"))
                    operacion = "MUL";

                codigo.append(terceto.getNumero() + "}MOV " + registroUtilizar + ", " + variableRetorno + "\n");

                int reg2 = tablaUtilizar.getFreeRegister();
                tablaUtilizar.occupyRegister(reg2);
                String reg2_x86 = getRegistro_x86(terceto.getTipo(), reg2);

                codigo.append(terceto.getNumero() + "}MOV " + reg2_x86 + ", " + terceto.getArg2().toString() + "\n");
                codigo.append(terceto.getNumero() + "}" + operacion + " " + reg2_x86 + "\n");

                tablaUtilizar.freeRegister(reg2);

            } else {
                codigo.append(terceto.getNumero()).append("}").append(getOperacion(terceto.getOperador())).append(" ").append(variableRetorno).append(", ").append(terceto.getArg2().toString()).append("\n");
                codigo.append(terceto.getNumero()).append("}MOV ").append(registroUtilizar).append(", ").append(variableRetorno).append("\n");
            }

            terceto.setAssociatedRegister(registroUtilizar);

            return;
        }

        String operacion = getOperacion(terceto.getOperador());
        String segundoElemento = terceto.getArg2().toString();
        if (operacion.equals("MULT") || operacion.equals("DIV")) {
            String aux = "MUL";

            if (operacion.equals("DIV"))
                aux = "DIV";

            TablaRegistros tablaUtilizar = getTablaRegistros(terceto1.getTipo());

            int segundoReg = tablaUtilizar.getFreeRegister();
            tablaUtilizar.occupyRegister(segundoReg);
            String segundoRegistro = getRegistro_x86(terceto1.getTipo(), segundoReg);

            codigo.append(terceto.getNumero() + "}MOV " + segundoRegistro + ", " + segundoElemento + "\n");
            codigo.append(terceto.getNumero() + "}" + aux + " " + segundoRegistro + "\n");

            tablaUtilizar.freeRegister(segundoReg);

            return;
        }

        codigo.append(terceto.getNumero()).append("}").append(getOperacion(terceto.getOperador())).append(" ").append(registroUtilizar).append(", ").append(terceto.getArg2().toString()).append("\n");
    }

    // Operación entre REGISTRO y REGISTRO
    private void applySituacion_3(Terceto terceto) {
        StringBuilder codigo = getCodeSection();

        Terceto terceto1 = ((ItemTerceto) terceto.getArg1()).getArg();
        Terceto terceto2 = ((ItemTerceto) terceto.getArg2()).getArg();

        String reg1 = terceto1.getAssociatedRegister();
        String reg2 = terceto2.getAssociatedRegister();

        if (terceto1.getOperador().equals("CALL") && terceto2.getOperador().equals("CALL")) {
            String nombreFuncion1 = Splitter.on("@").splitToList(terceto1.getArg1().toString()).get(0);
            String variableRetorno1 = "ret" + terceto1.getTipo() + "_" + nombreFuncion1;

            codigo.append(terceto.getNumero() + "}CALL @FUNCTION_").append(nombreFuncion1).append("\n");
            recoverRegisters(terceto1.getTipo());

            TablaRegistros tablaUtilizar = getTablaRegistros(terceto1.getTipo());
            int reg = tablaUtilizar.getFreeRegister();
            tablaUtilizar.occupyRegister(reg);
            String registroUtilizar = getRegistro_x86(terceto2.getTipo(), reg);

            codigo.append(terceto.getNumero() + "}MOV " + registroUtilizar + ", " + variableRetorno1 + "\n");

            String nombreFuncion2 = Splitter.on("@").splitToList(terceto2.getArg1().toString()).get(0);
            String variableRetorno2 = "ret" + terceto2.getTipo() + "_" + nombreFuncion2;

            codigo.append(terceto.getNumero() + "}CALL @FUNCTION_" + nombreFuncion2 + "\n");
            recoverRegisters(terceto2.getTipo());

            codigo.append(terceto.getNumero() + "}" + getOperacion(terceto.getOperador()) + " " + registroUtilizar + ", " + variableRetorno2 + "\n");

            terceto.setAssociatedRegister(registroUtilizar);

            return;
        }

        if (terceto1.getOperador().equals("CALL") && !terceto2.getOperador().equals("CALL")) {
            String nombreFuncion = Splitter.on("@").splitToList(terceto1.getArg1().toString()).get(0);
            String variableRetorno = "ret" + terceto1.getTipo() + "_" + nombreFuncion;

            codigo.append(terceto.getNumero() + "}CALL @FUNCTION_" + nombreFuncion + "\n");
            recoverRegisters(terceto1.getTipo());

            TablaRegistros tablaUtilizar = getTablaRegistros(terceto1.getTipo());
            int reg = tablaUtilizar.getFreeRegister();
            tablaUtilizar.occupyRegister(reg);
            String registroUtilizar = getRegistro_x86(terceto1.getTipo(), reg);

            codigo.append(terceto.getNumero() + "}MOV " + registroUtilizar + ", " + variableRetorno + "\n");
            codigo.append(terceto.getNumero() + "}" + getOperacion(terceto.getOperador()) + " " + registroUtilizar + ", " + terceto2.getAssociatedRegister() + "\n");

            terceto.setAssociatedRegister(registroUtilizar);

            tablaUtilizar.freeRegister(getRegistro(terceto2.getAssociatedRegister()));

            return;
        }

        if (!terceto1.getOperador().equals("CALL") && terceto2.getOperador().equals("CALL")) {
            String nombreFuncion = Splitter.on("@").splitToList(terceto2.getArg1().toString()).get(0);
            String variableRetorno = "ret" + terceto2.getTipo() + "_" + nombreFuncion;

            codigo.append(terceto.getNumero() + "}CALL @FUNCTION_" + nombreFuncion + "\n");
            recoverRegisters(terceto2.getTipo());

            codigo.append(terceto.getNumero() + "}" + getOperacion(terceto.getOperador()) + " " + terceto1.getAssociatedRegister() + ", " + variableRetorno + "\n");

            terceto.setAssociatedRegister(terceto1.getAssociatedRegister());

            return;
        }

        codigo.append(terceto.getNumero()).append("}").append(getOperacion(terceto.getOperador())).append(" ").append(reg1).append(", ").append(reg2).append("\n");

        terceto.setAssociatedRegister(reg1);

        TablaRegistros tablaRegistros = getTablaRegistros(terceto.getTipo());
        tablaRegistros.freeRegister(getRegistro(reg2));
    }

    // Operación entre VAR/CTE y REGISTRO -> OPERADOR CONMUTATIVO
    private void applySituacion_4a(Terceto terceto) {
        StringBuilder codigo = getCodeSection();

        // El número 2 hace referencia a que es el segundo argumento del terceto que ingresa por parámetro
        Terceto terceto2 = ((ItemTerceto) terceto.getArg2()).getArg();

        String reg2 = terceto2.getAssociatedRegister();

        if (terceto2.getOperador().equals("CALL")) {
            String nombreFuncion = Splitter.on("@").splitToList(terceto2.getArg1().toString()).get(0);
            String variableRetorno = "ret" + terceto2.getTipo() + "_" + nombreFuncion;

            codigo.append(terceto.getNumero()).append("}CALL @FUNCTION_").append(nombreFuncion).append("\n");
            recoverRegisters(terceto2.getTipo());

            TablaRegistros tablaUtilizar = getTablaRegistros(terceto2.getTipo());
            int reg = tablaUtilizar.getFreeRegister();
            tablaUtilizar.occupyRegister(reg);
            String registroUtilizar = getRegistro_x86(terceto2.getTipo(), reg);

            codigo.append(terceto.getNumero() + "}MOV " + registroUtilizar + ", " + variableRetorno + "\n");

            String operacion = getOperacion(terceto.getOperador());
            if (operacion.equals("MULT") || operacion.equals("DIV")) {

                if (operacion.equals("MULT"))
                    operacion = "MUL";

                int regAux = tablaUtilizar.getFreeRegister();
                tablaUtilizar.occupyRegister(regAux);
                String regAux86 = getRegistro_x86(terceto.getTipo(), regAux);

                codigo.append(terceto.getNumero() + "}MOV " + regAux86 + ", " + terceto.getArg1().toItemString() + "\n");
                codigo.append(terceto.getNumero() + "}" + operacion + " " + regAux86 + "\n");

                tablaUtilizar.freeRegister(regAux);
            } else {
                codigo.append(terceto.getNumero() + "}" + operacion + " " + registroUtilizar + ", " + terceto.getArg1().toItemString() + "\n");
            }


            terceto.setAssociatedRegister(registroUtilizar);

            return;
        }

        codigo.append(terceto.getNumero()).append("}").append(getOperacion(terceto.getOperador())).append(" ").append(reg2).append(", ").append(terceto.getArg1().toItemString()).append("\n");

        terceto.setAssociatedRegister(reg2);
    }

    // Operación entre VAR/CTE y REGISTRO -> OPERADOR NO CONMUTATIVO
    private void applySituacion_4b(Terceto terceto) {

        StringBuilder codigo = getCodeSection();

        String type = terceto.getTipo();

        TablaRegistros tablaUtilizar = getTablaRegistros(type);
        int reg = tablaUtilizar.getFreeRegister();
        tablaUtilizar.occupyRegister(reg);

        String registroUtilizar = getRegistro_x86(type, reg);

        // El 2 hace referencia a que es el segundo argumento del terceto que entra por parámetro
        Terceto terceto2 = ((ItemTerceto) terceto.getArg2()).getArg();
        String reg2 = terceto2.getAssociatedRegister();

        if (terceto2.getOperador().equals("CALL")) {
            String nombreFuncion = Splitter.on("@").splitToList(terceto2.getArg1().toString()).get(0);
            String variableRetorno = "ret" + terceto2.getTipo() + "_" + nombreFuncion;

            codigo.append(terceto.getNumero()).append("}CALL @FUNCTION_").append(nombreFuncion).append("\n");
            recoverRegisters(terceto2.getTipo());

            registroUtilizar = getRegistro_x86(terceto2.getTipo(), reg);

            codigo.append(terceto.getNumero() + "}MOV " + registroUtilizar + ", " + terceto.getArg1().toItemString() + "\n");
            codigo.append(terceto.getNumero() + "}" + getOperacion(terceto.getOperador()) + " " + registroUtilizar + ", " + variableRetorno + "\n");

            terceto.setAssociatedRegister(registroUtilizar);

            return;
        }

        codigo.append(terceto.getNumero()).append("}MOV ").append(registroUtilizar).append(", ").append(terceto.getArg1().toItemString()).append("\n");
        codigo.append(terceto.getNumero()).append("}").append(getOperacion(terceto.getOperador())).append(" ").append(registroUtilizar).append(", ").append(reg2).append("\n");

        terceto.setAssociatedRegister(registroUtilizar);

        tablaUtilizar.freeRegister(getRegistro(reg2));
    }

    // Asignación de valor proveniente de REGISTRO
    private void applySituacionAsignacion_a(Terceto terceto) {
        StringBuilder codigo = getCodeSection();

        String constantType = terceto.getArg2().getTipo();

        Terceto terceto2 = ((ItemTerceto) terceto.getArg2()).getArg();

        TablaRegistros tablaUtilizar = getTablaRegistros(constantType);
        String registroUtilizar = terceto2.getAssociatedRegister();

        if (terceto2.getOperador().equals("CALL")) {
            String nombreFuncion = Splitter.on("@").splitToList(terceto2.getArg1().toString()).get(0);
            String variableRetorno = "ret" + constantType + "_" + nombreFuncion;

            codigo.append(terceto.getNumero()).append("}CALL @FUNCTION_").append(nombreFuncion).append("\n");
            recoverRegisters(constantType);
            codigo.append(terceto.getNumero()).append("}").append("MOV ").append(terceto.getArg1()).append(", ").append(variableRetorno).append("\n");
            return;
        }

        codigo.append(terceto.getNumero()).append("}").append("MOV ").append(terceto.getArg1()).append(", ").append(registroUtilizar).append("\n");

        tablaUtilizar.freeRegister(getRegistro(registroUtilizar));
    }

    // Asignación de valor proveniente de VAR/CTE
    private void applySituacionAsignacion_b(Terceto terceto) {
        StringBuilder codigo = getCodeSection();

        String type = terceto.getArg2().getTipo();
        TablaRegistros tablaUtilizar = getTablaRegistros(type);

        int reg = tablaUtilizar.getFreeRegister();
        tablaUtilizar.occupyRegister(reg);
        String registroUtilizar = getRegistro_x86(type, reg);

        codigo.append(terceto.getNumero()).append("}").append("MOV ").append(registroUtilizar).append(", ").append(terceto.getArg2()).append("\n");
        codigo.append(terceto.getNumero()).append("}").append("MOV ").append(terceto.getArg1()).append(", ").append(registroUtilizar).append("\n");

        tablaUtilizar.freeRegister(reg);
    }

    // COMPARACION
    private void applySituacionComparacion(Terceto terceto) {

        String arg1 = terceto.getArg1().toString();
        String arg2 = terceto.getArg2().toString();

        StringBuilder codigo = getCodeSection();

        // Comparación entre REF_TERCETO y REF_TERCETO
        if (arg1.contains("[") && arg2.contains("[")) {
            Terceto terceto1 = ((ItemTerceto) terceto.getArg1()).getArg();
            Terceto terceto2 = ((ItemTerceto) terceto.getArg2()).getArg();

            String type = terceto1.getTipo();
            TablaRegistros tablaUtilizar = getTablaRegistros(type);

            codigo.append(terceto.getNumero()).append("} CMP ").append(terceto1.getAssociatedRegister()).append(", ").append(terceto2.getAssociatedRegister()).append("\n");

            tablaUtilizar.freeRegister(getRegistro(terceto1.getAssociatedRegister()));
            tablaUtilizar.freeRegister(getRegistro(terceto2.getAssociatedRegister()));

            return;
        }

        // Comparación entre REF_TERCETO y VARIABLE
        if (arg1.contains("[") && !arg2.contains("[")) {
            Terceto terceto1 = ((ItemTerceto) terceto.getArg1()).getArg();

            String type = terceto1.getTipo();
            TablaRegistros tablaUtilizar = getTablaRegistros(type);

            int reg = tablaUtilizar.getFreeRegister();
            tablaUtilizar.occupyRegister(reg);
            String registroUtilizar = getRegistro_x86(type, reg);

            codigo.append(terceto.getNumero()).append("}MOV ").append(registroUtilizar).append(", ").append(arg2).append("\n");
            codigo.append(terceto.getNumero()).append("}CMP ").append(terceto1.getAssociatedRegister()).append(", ").append(registroUtilizar).append("\n");

            tablaUtilizar.freeRegister(getRegistro(terceto1.getAssociatedRegister()));
            tablaUtilizar.freeRegister(reg);

            return;
        }

        // Comparación entre VARIABLE y REF_TERCETO
        if (!arg1.contains("[") && arg2.contains("[")) {
            Terceto terceto2 = ((ItemTerceto) terceto.getArg2()).getArg();

            String type = terceto2.getTipo();
            TablaRegistros tablaUtilizar = getTablaRegistros(type);

            int reg = tablaUtilizar.getFreeRegister();
            tablaUtilizar.occupyRegister(reg);
            String registroUtilizar = getRegistro_x86(type, reg);

            codigo.append(terceto.getNumero()).append("}MOV ").append(registroUtilizar).append(", ").append(arg1).append("\n");
            codigo.append(terceto.getNumero()).append("}CMP ").append(registroUtilizar).append(", ").append(terceto2.getAssociatedRegister()).append("\n");

            tablaUtilizar.freeRegister(reg);
            tablaUtilizar.freeRegister(getRegistro(terceto2.getAssociatedRegister()));

            return;
        }

        if (!arg1.contains("[") && !arg2.contains("[")) {

            String type = tablita.getVarType(Splitter.on("@").splitToList(arg1).get(0));
            TablaRegistros tablaUtilizar = getTablaRegistros(type);

            int reg1 = tablaUtilizar.getFreeRegister();
            tablaUtilizar.occupyRegister(reg1);
            String registroUtilizar1 = getRegistro_x86(type, reg1);

            int reg2 = tablaUtilizar.getFreeRegister();
            tablaUtilizar.occupyRegister(reg2);
            String registroUtilizar2 = getRegistro_x86(type, reg2);

            codigo.append(terceto.getNumero()).append("}MOV ").append(registroUtilizar1).append(", ").append(arg1).append("\n");
            codigo.append(terceto.getNumero()).append("}MOV ").append(registroUtilizar2).append(", ").append(arg2).append("\n");
            codigo.append(terceto.getNumero()).append("}CMP ").append(registroUtilizar1).append(", ").append(registroUtilizar2).append("\n");

            tablaUtilizar.freeRegister(getRegistro(registroUtilizar1));
            tablaUtilizar.freeRegister(getRegistro(registroUtilizar2));

            return;
        }

        throw new RuntimeException("CORTASTE TODA LA LOZ");
    }

    private void applySituacionBF(Terceto terceto) {
        String tercetoSaltar = ((ItemString) terceto.getArg2()).getArg();

        tercetoSaltar = tercetoSaltar.replace("[", "");
        tercetoSaltar = tercetoSaltar.replace("]", "");

        listaDireccionesSalto.add(Integer.valueOf(tercetoSaltar));

        StringBuilder codigo = getCodeSection();

        codigo.append(terceto.getNumero()).append("}").append(mapaSaltos.get(lastCompOperator)).append(" Label").append(tercetoSaltar).append("\n");
    }

    private void applySituacionBI(Terceto terceto) {
        String tercetoSaltar = terceto.getArg1().toItemString().getArg();

        tercetoSaltar = tercetoSaltar.replace("[", "");
        tercetoSaltar = tercetoSaltar.replace("]", "");

        listaDireccionesSalto.add(Integer.valueOf(tercetoSaltar));

        StringBuilder codigo = getCodeSection();

        codigo.append(terceto.getNumero()).append("}").append("JMP Label").append(tercetoSaltar).append("\n");
    }

    private StringBuilder getCodeSection() {
        if (isFuncion)
            return codigoFuncion;

        return code;
    }

    private String getOperacion(String operador) {
        switch (operador) {
            case "+":
                return "ADD";
            case "-":
                return "SUB";
            case "*":
                return "MULT";
            case "/":
                return "DIV";
            default:
                throw new IllegalArgumentException("QUE ONDA BIGOTEEEEEE");
        }
    }

    private TablaRegistros getTablaRegistros(String tipo) {
        if (tipo.equals("UINT"))
            return tablaRegistrosUINT;

        if (tipo.equals("ULONG"))
            return tablaRegistrosULONG;

        throw new RuntimeException("NO SE RECONOCE EL TIPO DE TABLA SOLICITADO");
    }

    private String getRegistro_x86(String dataType, int registro) {
        String aux = "";
        if (dataType.equals("ULONG"))
            aux = "E";

        switch (registro) {
            case 0:
                return aux + "AX";
            case 1:
                return aux + "BX";
            case 2:
                return aux + "CX";
            case 3:
                return aux + "DX";
            default:
                throw new RuntimeException("HOLAAAAAA");
        }
    }

    private int getRegistro(String reg_x86) {
        if (reg_x86.contains("A"))
            return 0;

        if (reg_x86.contains("B"))
            return 1;

        if (reg_x86.contains("C"))
            return 2;

        if (reg_x86.contains("D"))
            return 3;

        throw new RuntimeException("Registro x86 con formato desconocido");
    }

    private void recoverRegisters(String type) {
        if (type.equals("UINT")) {
            if (seGuardoUINT[0]) {
                code.append("MOV AX, tempAX\n");
                seGuardoUINT[0] = false;
            }

            if (seGuardoUINT[1]) {
                code.append("MOV BX, tempBX\n");
                seGuardoUINT[1] = false;
            }

            if (seGuardoUINT[2]) {
                code.append("MOV CX, tempCX\n");
                seGuardoUINT[2] = false;
            }

            if (seGuardoUINT[3]) {
                code.append("MOV DX, tempDX\n");
                seGuardoUINT[3] = false;
            }
        }

        if (type.equals("ULONG")) {
            if (seGuardoULONG[0]) {
                code.append("MOV EAX, tempEAX\n");
                seGuardoULONG[0] = false;
            }

            if (seGuardoULONG[1]) {
                code.append("MOV EBX, tempEBX\n");
                seGuardoULONG[1] = false;
            }

            if (seGuardoULONG[2]) {
                code.append("MOV ECX, tempECX\n");
                seGuardoULONG[2] = false;
            }

            if (seGuardoULONG[3]) {
                code.append("MOV EDX, tempEDX\n");
                seGuardoULONG[3] = false;
            }
        }
    }

    private void buildMap() {
        mapaSaltos.put("<", "JNGE");
        mapaSaltos.put("<=", "JNG");
        mapaSaltos.put("==", "JNE");
        mapaSaltos.put("<>", "JE");
        mapaSaltos.put(">", "JNLE");
        mapaSaltos.put(">=", "JNL");
    }

    private List<String> intercalarLabels(List<String> listita) {
        listaDireccionesSalto = listaDireccionesSalto.stream().sorted().collect(Collectors.toList());

        List<String> auxLista = new ArrayList<>();
        auxLista.addAll(listita);

        for (int i = 0; i < auxLista.size(); i++) {
            String instruccion = auxLista.get(i);

            List<String> aux = Splitter.on("}").splitToList(instruccion);

            if (aux.size() != 2)
                continue;

            int numeroTerceto = Integer.valueOf(aux.get(0));

            if (listaDireccionesSalto.contains(numeroTerceto)) {
                auxLista.add(i, "Label" + numeroTerceto + ":");
                listaDireccionesSalto.remove((Integer) numeroTerceto);
            }
        }

        return auxLista;
    }

    private List<String> eliminarNumeros(List<String> listita) {
        listita = listita.stream().map((instruccion) -> {
            List<String> aux = Splitter.on("}").splitToList(instruccion);

            if (aux.size() == 2)
                return aux.get(1);

            return instruccion;
        }).collect(Collectors.toList());

        return listita;
    }
}
