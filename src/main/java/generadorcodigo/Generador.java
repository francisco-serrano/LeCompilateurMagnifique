package generadorcodigo;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Multimap;
import lexer.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
//import java.util.stream.Collectors;

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

        listaInstrucciones = reemplazarMultDiv(listaInstrucciones);
        listaInstFunc = reemplazarMultDiv(listaInstFunc);

        listaInstrucciones = prepararDividendos(listaInstrucciones);
        listaInstFunc = prepararDividendos(listaInstFunc);

        listaInstrucciones = agregarControlOVF_Producto(listaInstrucciones);
        listaInstFunc = agregarControlOVF_Producto(listaInstFunc);

        listaInstrucciones = agregarControlDivision(listaInstrucciones);
        listaInstFunc = agregarControlDivision(listaInstFunc);

        listaInstFunc.add("@LABEL_OVF_PRODUCTO:");
        listaInstFunc.add("invoke MessageBox, NULL, addr mensaje_overflow_producto, addr mensaje_overflow_producto, MB_OK");
        listaInstFunc.add("JMP @LABEL_END");

        listaInstFunc.add("@LABEL_DIV_CERO:");
        listaInstFunc.add("invoke MessageBox, NULL, addr mensaje_division_cero, addr mensaje_division_cero, MB_OK");

        listaInstFunc.add("@LABEL_END:");
        listaInstFunc.add("invoke ExitProcess, 0");
        listaInstFunc.add("end start");
    }

    private List<String> reemplazarMultDiv(List<String> listaIterar) {

        List<String> auxListaInstrucciones = new ArrayList<>();

        for (int i = 0; i < listaIterar.size(); i++) {
            String linea = listaIterar.get(i);

            String operacion = Splitter.on(" ").splitToList(linea).get(0);

            if (operacion.equals("MULT") || operacion.equals("DIV")) {
                String registro = Splitter.on(" ").splitToList(Splitter.on(", ").splitToList(linea).get(0)).get(1); // Obtengo el reg donde se almacena el resultado
                String valor = Splitter.on(", ").splitToList(linea).get(1); // Obtengo el valor a almacenar en el registro

                if (linea.contains("MULT"))
                    operacion = "MUL";

                if (!valor.contains("@")) // Quiere decir que se trata de una constante
                    valor = new StringBuilder(valor).insert(0, "@").toString();

                if (registro.contains("A")) { // Quiere decir que estoy en AX/EAX
//                    auxListaInstrucciones.add(operacion + " @" + valor);
                    auxListaInstrucciones.add(operacion + " " + valor);
                    continue;
                }

                if (!registro.contains("A")) { // Agrego las instrucciones de atrás para adelante porque se van intercalando
                    auxListaInstrucciones.add("MOV tempAX, AX");
                    auxListaInstrucciones.add("MOV AX, " + registro);
//                    auxListaInstrucciones.add(operacion + " @" + valor);
                    auxListaInstrucciones.add(operacion + " " + valor);
                    auxListaInstrucciones.add("MOV " + registro + ", AX");
                    auxListaInstrucciones.add("MOV AX, tempAX");
                    continue;
                }
            }

            auxListaInstrucciones.add(linea);
        }

        return auxListaInstrucciones;
    }

    private List<String> prepararDividendos(List<String> lista) {
        List<String> listaRetornar = new ArrayList<>();

        for (String elementoLista : lista) {
            String operacion = Splitter.on(" ").splitToList(elementoLista).get(0);

            if (operacion.equals("DIV")) {
                String tipoValor;

                if (elementoLista.contains("UINT")) {
                    tipoValor = "UINT";
                    elementoLista = elementoLista.replace("@", "");
                } else if (elementoLista.contains("ULONG")) {
                    tipoValor = "ULONG";
                    elementoLista = elementoLista.replace("@", "");
                } else
                    tipoValor = getTipo(Integer.valueOf(Splitter.on("@").splitToList(elementoLista).get(1)));

                if (tipoValor.equals("UINT")) {
                    listaRetornar.add("MOV tempDX, DX");
                    listaRetornar.add("MOV DX, 0");
                    listaRetornar.add(elementoLista);
                    listaRetornar.add("MOV DX, tempDX");
                }

                if (tipoValor.equals("ULONG")) {
                    listaRetornar.add("MOV tempEDX, EDX");
                    listaRetornar.add("MOV EDX, 0");
                    listaRetornar.add(elementoLista);
                    listaRetornar.add("MOV EDX, tempEDX");
                }

                continue;
            }

            listaRetornar.add(elementoLista);
        }

        return listaRetornar;
    }

    private List<String> agregarControlOVF_Producto(List<String> lista) {
        List<String> listaRetornar = new ArrayList<>();

        for (String elem : lista) {
            String operacion = Splitter.on(" ").splitToList(elem).get(0);

            listaRetornar.add(elem);

            if (operacion.equals("MUL"))
                listaRetornar.add("JO @LABEL_OVF_PRODUCTO");
        }

        return listaRetornar;
    }

    private List<String> agregarControlDivision(List<String> lista) {
        List<String> listaRetornar = new ArrayList<>();

        for (String elem : lista) {
            String operacion = Splitter.on(" ").splitToList(elem).get(0);

            if (operacion.equals("DIV")) {
                String divisor = Splitter.on(" ").splitToList(elem).get(1);

                // Me fijo si el valor viene de una constante
                if (divisor.contains("@")) {
                    String tipo = getTipo(new Long(divisor.replace("@", "")));

                    if (tipo.equals("UINT")) {
                        listaRetornar.add("CMP DX, " + divisor);
                        listaRetornar.add("JZ @LABEL_DIV_CERO");
                    }

                    if (tipo.equals("ULONG")) {
                        listaRetornar.add("CMP EDX, " + divisor);
                        listaRetornar.add("JZ @LABEL_DIV_CERO");
                    }
                }

                // Me fijo si el valor viene del retorno de una función UINT
                if (divisor.contains("retUINT_")) {
                    listaRetornar.add("CMP DX, " + divisor);
                    listaRetornar.add("JZ @LABEL_DIV_CERO");
                }

                // Me fijo si el valor viene del retorno de una función ULONG
                if (divisor.contains("retULONG_")) {
                    listaRetornar.add("CMP EDX, " + divisor);
                    listaRetornar.add("JZ @LABEL_DIV_CERO");
                }
            }

            listaRetornar.add(elem);
        }

        return listaRetornar;
    }

    private String getTipo(long valor) {
        if (valor >= 0 && valor <= 65535)
            return "UINT";

        if (valor >= 65536 && valor <= 4294967295d)
            return "ULONG";

        throw new RuntimeException("No se reconoció el tipo");
    }

    public void buildFile(String nombreArchivo) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new File(nombreArchivo));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        writer.println(Joiner.on("\n").join(getListaInstrucciones()));

        writer.close();
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
                        String p = t.getLexema();
                        code.append(p.replace(" ", "_").replace("\'", "").replace(",", "_").replace(".", "_")).append(" db \"").append(p.replace("\'", "")).append("\", 0 \n");
                        break;
                    case "variable":
                        String prefijo = "@" + t.getAmbito();
                        if (t.getType().equals("UINT"))
                            code.append(t.getLexema()).append(prefijo).append(" DW").append(" ?\n");

                        if (t.getType().equals("ULONG"))
                            code.append(t.getLexema()).append(prefijo).append(" DD").append(" ?\n");
                        break;
                    case "nombre_funcion":
                        if (t.getType().equals("UINT"))
                            code.append("retUINT_").append(t.getLexema()).append(" DW").append(" ?\n");

                        if (t.getType().equals("ULONG"))
                            code.append("retULONG_").append(t.getLexema()).append(" DD").append(" ?\n");

                        break;
                    case "constante":
                        if (t.getType().equals("UINT"))
                            code.append("@").append(t.getLexema()).append(" DW ").append(t.getLexema()).append("\n");

                        if (t.getType().equals("ULONG"))
                            code.append("@").append(t.getLexema()).append(" DD ").append(t.getLexema()).append("\n");

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

        // Variables con los mensajes a mostrar en caso de errores
        code.append("mensaje_division_cero db \"ERROR EN TIEMPO DE EJECUCION -> DIVISION POR CERO\", 0\n");
        code.append("mensaje_overflow_producto db \"ERROR EN TIEMPO DE EJECUCION -> OVERFLOW EN PRODUCTO\", 0\n");
    }

    private void assembleTerceto(Terceto terceto) {

        List<String> listaOperadores = Arrays.asList("+", "-", "*", "/");
        List<String> listaComparadores = Arrays.asList("<=", ">=", "<", ">", "==", "<>");

        // OPERACIONES ARITMETICAS
        if (listaOperadores.contains(terceto.getOperador())) {

            String situacion = getSituacionAritmetica(terceto);

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
                    throw new RuntimeException("Situación (" + situacion + ") no reconocida");
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
            code.append(terceto.getNumero()).append("}invoke MessageBox, NULL, addr ").append(auxiliar).append(", addr ").append(auxiliar).append(", MB_OK\n");
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
                codigoFuncion.append("MOV ").append(registroUtilizar).append(", ").append(terceto.getArg1().toItemString().toString()).append("\n");
                codigoFuncion.append("MOV retUINT_").append(nombreFuncion).append(", ").append(registroUtilizar).append("\n");
            }

            if (tipoRetorno.equals("ULONG")) {
                codigoFuncion.append("MOV ").append(registroUtilizar).append(", ").append(terceto.getArg1().toItemString().toString()).append("\n");
                codigoFuncion.append("MOV retULONG_").append(nombreFuncion).append(", ").append(registroUtilizar).append("\n");
            }

            tablaUtilizar.freeRegister(reg);

            nombreFuncion = "";
            codigoFuncion.append("RET\n");
        }

        // Llamadas a función, se hace backup de los registros
        if (terceto.getOperador().equals("CALL"))
            backupRegisters(terceto.getTipo());

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

        String tipo = terceto.getTipo();
        TablaRegistros tablaUtilizar = getTablaRegistros(tipo);

        String operacion = getOperacion(terceto.getOperador());
        String primerElemento = terceto.getArg1().toString();
        String segundoElemento = terceto.getArg2().toString();

        int reg_1 = tablaUtilizar.getFreeRegister();
        tablaUtilizar.occupyRegister(reg_1);
        String registro_x86_1 = getRegistro_x86(tipo, reg_1);
        terceto.setAssociatedRegister(registro_x86_1);

        codigo.append(terceto.getNumero()).append("}MOV ").append(registro_x86_1).append(", ").append(primerElemento).append("\n");

        codigo.append(terceto.getNumero()).append("}").append(operacion).append(" ").append(registro_x86_1).append(", ").append(segundoElemento).append("\n");
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

            codigo.append(terceto.getNumero()).append("}").append(operacion).append(" ").append(variableRetorno).append(", ").append(terceto.getArg2().toString()).append("\n");
            codigo.append(terceto.getNumero()).append("}MOV ").append(registroUtilizar).append(", ").append(variableRetorno).append("\n");

            terceto.setAssociatedRegister(registroUtilizar);

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

            codigo.append(terceto.getNumero()).append("}CALL @FUNCTION_").append(nombreFuncion1).append("\n");
            recoverRegisters(terceto1.getTipo());

            TablaRegistros tablaUtilizar = getTablaRegistros(terceto1.getTipo());
            int reg = tablaUtilizar.getFreeRegister();
            tablaUtilizar.occupyRegister(reg);
            String registroUtilizar = getRegistro_x86(terceto2.getTipo(), reg);

            codigo.append(terceto.getNumero()).append("}MOV ").append(registroUtilizar).append(", ").append(variableRetorno1).append("\n");

            String nombreFuncion2 = Splitter.on("@").splitToList(terceto2.getArg1().toString()).get(0);
            String variableRetorno2 = "ret" + terceto2.getTipo() + "_" + nombreFuncion2;

            codigo.append(terceto.getNumero()).append("}CALL @FUNCTION_").append(nombreFuncion2).append("\n");
            recoverRegisters(terceto2.getTipo());

            codigo.append(terceto.getNumero()).append("}").append(getOperacion(terceto.getOperador())).append(" ").append(registroUtilizar).append(", ").append(variableRetorno2).append("\n");

            terceto.setAssociatedRegister(registroUtilizar);

            return;
        }

        if (terceto1.getOperador().equals("CALL") && !terceto2.getOperador().equals("CALL")) {
            String nombreFuncion = Splitter.on("@").splitToList(terceto1.getArg1().toString()).get(0);
            String variableRetorno = "ret" + terceto1.getTipo() + "_" + nombreFuncion;

            codigo.append(terceto.getNumero()).append("}CALL @FUNCTION_").append(nombreFuncion).append("\n");
            recoverRegisters(terceto1.getTipo());

            TablaRegistros tablaUtilizar = getTablaRegistros(terceto1.getTipo());
            int reg = tablaUtilizar.getFreeRegister();
            tablaUtilizar.occupyRegister(reg);
            String registroUtilizar = getRegistro_x86(terceto1.getTipo(), reg);

            codigo.append(terceto.getNumero()).append("}MOV ").append(registroUtilizar).append(", ").append(variableRetorno).append("\n");
            codigo.append(terceto.getNumero()).append("}").append(getOperacion(terceto.getOperador())).append(" ").append(registroUtilizar).append(", ").append(terceto2.getAssociatedRegister()).append("\n");

            terceto.setAssociatedRegister(registroUtilizar);

            tablaUtilizar.freeRegister(getRegistro(terceto2.getAssociatedRegister()));

            return;
        }

        if (!terceto1.getOperador().equals("CALL") && terceto2.getOperador().equals("CALL")) {
            String nombreFuncion = Splitter.on("@").splitToList(terceto2.getArg1().toString()).get(0);
            String variableRetorno = "ret" + terceto2.getTipo() + "_" + nombreFuncion;

            codigo.append(terceto.getNumero()).append("}CALL @FUNCTION_").append(nombreFuncion).append("\n");
            recoverRegisters(terceto2.getTipo());

            String operacion = getOperacion(terceto.getOperador());

            codigo.append(terceto.getNumero()).append("}").append(operacion).append(" ").append(terceto1.getAssociatedRegister()).append(", ").append(variableRetorno).append("\n");

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

            codigo.append(terceto.getNumero()).append("}MOV ").append(registroUtilizar).append(", ").append(variableRetorno).append("\n");

            String operacion = getOperacion(terceto.getOperador());

            codigo.append(terceto.getNumero()).append("}").append(operacion).append(" ").append(registroUtilizar).append(", ").append(terceto.getArg1().toItemString()).append("\n");

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

            codigo.append(terceto.getNumero()).append("}MOV ").append(registroUtilizar).append(", ").append(terceto.getArg1().toItemString()).append("\n");
            codigo.append(terceto.getNumero()).append("}").append(getOperacion(terceto.getOperador())).append(" ").append(registroUtilizar).append(", ").append(variableRetorno).append("\n");

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

            codigo.append(terceto.getNumero()).append("}CMP ").append(terceto1.getAssociatedRegister()).append(", ").append(terceto2.getAssociatedRegister()).append("\n");

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

        throw new RuntimeException("Operación de comparación no reconocida -> " + terceto);
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
                throw new IllegalArgumentException("Operador no reconocido --> " + operador);
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
                throw new RuntimeException("Registro no reconocido --> " + registro);
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

    private void backupRegisters(String type) {
        if (type.equals("UINT")) {
            if (tablaRegistrosUINT.isOccupied(0)) {
                code.append("MOV tempAX, AX\n");
                seGuardoUINT[0] = true;
            }

            if (tablaRegistrosUINT.isOccupied(1)) {
                code.append("MOV tempBX, BX\n");
                seGuardoUINT[1] = true;
            }

            if (tablaRegistrosUINT.isOccupied(2)) {
                code.append("MOV tempCX, CX\n");
                seGuardoUINT[2] = true;
            }

            if (tablaRegistrosUINT.isOccupied(3)) {
                code.append("MOV tempDX, DX\n");
                seGuardoUINT[3] = true;
            }
        }

        if (type.equals("ULONG")) {
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
        mapaSaltos.put("<", "JGE");
        mapaSaltos.put("<=", "JG");
        mapaSaltos.put("==", "JNE");
        mapaSaltos.put("<>", "JE");
        mapaSaltos.put(">", "JLE");
        mapaSaltos.put(">=", "JL");
    }

    /**
     * Intercalo en el código las labels a las cuales se va a saltar.
     * @param listita Lista con el código sin labels.
     * @return Lista con el código con labels.
     */
    private List<String> intercalarLabels(List<String> listita) {
//        listaDireccionesSalto = listaDireccionesSalto.stream().sorted().distinct().collect(Collectors.toList());

        Collections.sort(listaDireccionesSalto);

        listaDireccionesSalto = new ArrayList<>(new HashSet<>(listaDireccionesSalto));

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

//        listita = listita.stream().map((instruccion) -> {
//            List<String> aux = Splitter.on("}").splitToList(instruccion);
//
//            if (aux.size() == 2)
//                return aux.get(1);
//
//            return instruccion;
//        }).collect(Collectors.toList());
//        return listita;

        List<String> result = new ArrayList<>();

        for (String aux : listita) {
            if (aux.contains("}")) {
                int indice = aux.indexOf("}");
                aux = aux.substring(indice + 1);
                result.add(aux);
            } else
                result.add(aux);
        }

        return result;
    }
}