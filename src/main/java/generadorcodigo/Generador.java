package generadorcodigo;

import com.google.common.base.Splitter;
import com.google.common.collect.Multimap;
import lexer.*;

import java.util.*;
import java.util.stream.Collectors;

public class Generador {

    private static final int CANTIDAD_REGISTROS = 4;

    private List<Terceto> tercetos;
    private TablaRegistros tablaRegistros = new TablaRegistros(CANTIDAD_REGISTROS);
    private StringBuilder code = new StringBuilder();
    private TablaSimbolos tablita = new TablaSimbolos();
    private Map<String, String> mapaSaltos = new HashMap<>();

    private String lastCompOperator;
    private List<Integer> listaDireccionesSalto = new ArrayList<>();

    private List<String> listaInstrucciones = new ArrayList<>();

    public Generador(List<Terceto> tercetos, TablaSimbolos ts) {
        this.tercetos = tercetos;
        this.tablita = ts;

        buildMap();
    }

    public String getCode() {
        return code.toString();
    }

    public List<String> getListaInstrucciones() {
        return listaInstrucciones;
    }

    private void declararVariables() {
        this.code.append(".data \n");
        Multimap<String, Token> mapa = tablita.getTabla();
        Collection<Token> e = mapa.values();
        Iterator<Token> it = e.iterator();
        while (it.hasNext()) {
            Token t = it.next();
            if (!t.getUso().equals("undefined")) {
                switch (t.getUso()) {
                    case "cadena":
                        String p = t.getLexema().toString();
                        this.code.append("msg_" + p.replace(" ", "_").replace(",", "_").replace(".","_") + " db '" + p + "',0 \n");
                        break;
                    case "variable":
                        String prefijo="@" + t.getAmbito();
                        if (t.getType().equals("UINT"))
                            this.code.append(t.getLexema().toString() + prefijo + " DW" + " ?\n");
                        if (t.getType().equals("ULONG"))
                            this.code.append(t.getLexema().toString()+ prefijo + " DD"+ " ?\n");
                        break;
                    case "nombre funcion":
                        if (t.getType().equals("UINT")){
                            this.code.append(t.getLexema().toString() + " DW" + " ?\n");
                        }
                        if (t.getType().equals("ULONG")){
                            this.code.append(t.getLexema().toString() + " DD" + " ?\n");
                        }
                        break;
                    case "constante":
                        if (t.getType().equals("UINT")) {
                            this.code.append(t.getLexema() + " DW " + t.getLexema().toString() + "\n");
                        }
                       if (t.getType().equals("ULONG")){
                           this.code.append(t.getLexema() + " DD " + t.getLexema().toString() + "\n");
                       }
                        break;

                }
            }
        }
    }

    public void generateAssembler() {
        printHeader();

        declararVariables();

        code.append(".code\nstart:\n");

        for (Terceto terceto : tercetos)
            assembleTerceto(terceto);

        code.append("end start");

        listaInstrucciones = Splitter.on("\n").splitToList(code.toString());

        intercalarLabels();

        eliminarNumeros();
    }

    private void printHeader() {
        this.code.append(".386 \n");
        this.code.append(".model flat, stdcall         ;Modelo de memoria 'pequeño' \n");
        this.code.append(".stack 200h                  ;Tamaño de la pila\n");
        this.code.append("include C:\\masm32\\include\\windows.inc \n");
        this.code.append("include C:\\masm32\\include\\kernel32.inc \n");
        this.code.append("include C:\\masm32\\include\\user32.inc \n");
        this.code.append("include C:\\masm32\\include\\masm32.inc \n");
        this.code.append("includelib C:\\masm32\\lib\\kernel32.lib \n");
        this.code.append("includelib C:\\masm32\\lib\\user32.lib \n");
        this.code.append("includelib C:\\masm32\\lib\\masm32.lib \n");
    }

    private void assembleTerceto(Terceto terceto) {
        /*
            Cada operación tiene un tipo de assembler asociado.
            Hay que ir asignándole el registro asociado a cada terceto.
         */

        List<String> listaOperadores = Arrays.asList("+", "-", "*", "/");
        List<String> listaComparadores = Arrays.asList("<=", ">=", "<", ">", "==", "<>");

        // OPERACIONES ARITMETICAS
        if (listaOperadores.contains(terceto.getOperador())) {
            System.out.println(terceto + " --> " + getSituacionAritmetica(terceto));

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
            }
        }

        // OPERACIONES DE ASIGNACION
        if (terceto.getOperador().equals("=")) {
            System.out.println(terceto + " --> " + getSituacionAsignacion(terceto));

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

        // COMPARACION
        if (listaComparadores.contains(terceto.getOperador())) {
            System.out.println(terceto + " --> COMPARADOR");

            applySituacionComparacion(terceto);

            lastCompOperator = terceto.getOperador();
        }

        // BRANCH IF FALSE (BF)
        if (terceto.getOperador().equals("BF")) {
            System.out.println(terceto + " --> BF");

            applySituacionBF(terceto);
        }

        // BRANCH INCONDITIONAL (BI)
        if (terceto.getOperador().equals("BI")) {
            System.out.println(terceto + " --> BI");

            applySituacionBI(terceto);
        }
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

        // Por descarte
        return "SITUACION_B";
    }

    private void applySituacion_1(Terceto terceto) {
        int reg = tablaRegistros.getFreeRegister();
        tablaRegistros.occupyRegister(reg);

        terceto.setAssociatedRegister(reg);

        code.append(terceto.getNumero()).append("}MOV R").append(reg).append(", ").append(terceto.getArg1().toString()).append("\n");
        code.append(terceto.getNumero() + "}").append(getOperacion(terceto.getOperador())).append(" R").append(reg).append(", ").append(terceto.getArg2().toString()).append("\n");
    }

    private void applySituacion_2(Terceto terceto) {
        code.append(terceto.getNumero() + "}").append(getOperacion(terceto.getOperador())).append(" R").append(terceto.getAssociatedRegister()).append(", ").append(terceto.getArg2().toString()).append("\n");
    }

    private void applySituacion_3(Terceto terceto) {
        int reg_arg1 = ((ItemTerceto) terceto.getArg1()).getArg().getAssociatedRegister();
        int reg_arg2 = ((ItemTerceto) terceto.getArg2()).getArg().getAssociatedRegister();

        code.append(terceto.getNumero() + "}").append(getOperacion(terceto.getOperador())).append(" R").append(reg_arg1).append(", R").append(reg_arg2).append("\n");
        tablaRegistros.freeRegister(reg_arg2);
    }

    private void applySituacion_4a(Terceto terceto) {
        code.append(terceto.getNumero() + "}").append(getOperacion(terceto.getOperador())).append(" R").append(terceto.getAssociatedRegister()).append(", ").append(terceto.getArg1()).append("\n");
    }

    private void applySituacion_4b(Terceto terceto) {
        int reg = tablaRegistros.getFreeRegister();
        tablaRegistros.occupyRegister(reg);

        code.append(terceto.getNumero() + "}").append("MOV R").append(reg).append(", ").append(terceto.getArg1()).append("\n");
        code.append(terceto.getNumero() + "}").append(getOperacion(terceto.getOperador() + " R")).append(reg).append(", R").append(terceto.getAssociatedRegister()).append("\n");
    }

    // ASIGNACION A
    private void applySituacionAsignacion_a(Terceto terceto) {
        code.append(terceto.getNumero() + "}").append("MOV ").append(terceto.getArg1()).append(", R").append(terceto.getAssociatedRegister()).append("\n");
        tablaRegistros.freeRegister(terceto.getAssociatedRegister());
    }

    // ASIGNACION B
    private void applySituacionAsignacion_b(Terceto terceto) {
        int reg = tablaRegistros.getFreeRegister();
        tablaRegistros.occupyRegister(reg);

        code.append(terceto.getNumero() + "}").append("MOV R").append(reg).append(", ").append(terceto.getArg2()).append("\n");
        code.append(terceto.getNumero() + "}").append("MOV ").append(terceto.getArg1()).append(", R").append(reg).append("\n");

        tablaRegistros.freeRegister(reg);
    }

    // COMPARACION
    private void applySituacionComparacion(Terceto terceto) {
        String op1 = terceto.getArg1().toString();
        String op2 = terceto.getArg2().toString();

        if (op1.contains("[") && op2.contains("[")) {
            code.append(terceto.getNumero() + "}").append("CMP R").append(((ItemTerceto) terceto.getArg1()).getArg().getAssociatedRegister()).append(", R").append(((ItemTerceto) terceto.getArg1()).getArg().getAssociatedRegister()).append("\n");
            return;
        }

        if (!op1.contains("[") && !op2.contains("[")) {
            code.append(terceto.getNumero() + "}").append("CMP ").append(op1).append(", ").append(op2).append("\n");
            return;
        }

        if (op1.contains("[") && !op2.contains("[")) {
            code.append(terceto.getNumero() + "}").append("CMP R").append(((ItemTerceto) terceto.getArg1()).getArg().getAssociatedRegister()).append(", ").append(op2).append("\n");
            return;
        }

        if (!op1.contains("[") && op2.contains("[")) {
            code.append(terceto.getNumero() + "}").append("CMP ").append(op1).append(", R").append(((ItemTerceto) terceto.getArg2()).getArg().getAssociatedRegister()).append("\n");
            return;
        }

        throw new IllegalArgumentException("CORTASTE TODA LA LOZ");
    }

    private void applySituacionBF(Terceto terceto) {
        String tercetoSaltar = ((ItemString) terceto.getArg2()).getArg();

        tercetoSaltar = tercetoSaltar.replace("[", "");
        tercetoSaltar = tercetoSaltar.replace("]", "");

        listaDireccionesSalto.add(Integer.valueOf(tercetoSaltar));

        code.append(terceto.getNumero() + "}").append(mapaSaltos.get(lastCompOperator)).append(" Label").append(tercetoSaltar).append("\n");
    }

    private void applySituacionBI(Terceto terceto) {
        String tercetoSaltar;
        if (terceto.getArg1().getClass() == ItemString.class)
            tercetoSaltar = ((ItemString) terceto.getArg1()).getArg();
        else
            tercetoSaltar = Integer.toString(((ItemTerceto) terceto.getArg1()).getArg().getNumero());

        tercetoSaltar = tercetoSaltar.replace("[", "");
        tercetoSaltar = tercetoSaltar.replace("]", "");

        listaDireccionesSalto.add(Integer.valueOf(tercetoSaltar));

        code.append(terceto.getNumero() + "}").append("JMP Label").append(tercetoSaltar).append("\n");
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

    private void buildMap() {
        mapaSaltos.put("<", "JNGE");
        mapaSaltos.put("<=", "JNG");
        mapaSaltos.put("==", "JNE");
        mapaSaltos.put("<>", "JE");
        mapaSaltos.put(">", "JNLE");
        mapaSaltos.put(">=", "JNL");
    }

    private void intercalarLabels() {
        listaDireccionesSalto = listaDireccionesSalto.stream().sorted().collect(Collectors.toList());

        List<String> auxLista = new ArrayList<>();
        auxLista.addAll(listaInstrucciones);

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

        listaInstrucciones = auxLista;
    }

    private void eliminarNumeros() {
        listaInstrucciones = listaInstrucciones.stream().map((instruccion) -> {
            List<String> aux = Splitter.on("}").splitToList(instruccion);

            if (aux.size() == 2)
                return aux.get(1);

            return instruccion;
        }).collect(Collectors.toList());
    }
}
