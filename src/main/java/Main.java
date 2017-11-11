import generadorcodigo.Generador;
import lexer.*;
import parser.Parser;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

/**
 * Clase utilizada para iniciar la ejecución del compilador.
 * <p>
 * El funcionamiento se basa en una CLI en la que se solicita ingresar por consola el archivo a compilar, en caso de
 * cancelar la ejecución, ingresar "EXIT". En caso de producirse errores durante la etapa de compilación, se informarán
 * los mismos por consola. En caso de querer volver a compilar, se debe ejecutar nuevamente el archivo por lotes.
 *
 * Para iniciar la ejecución, abrir el archivo por lotes "run-compiler.bat" para mayor facilidad de uso.
 *
 * Otra alternativa de ejecución es, dentro de una consola, correr el siguiente comando:
 * "java -jar LeCompilateurMagnifique.jar"
 *
 * @author Bianco Martín, Di Pietro Esteban, Serrano Francisco
 */
public class Main {

    private static final String DIR_MATRIZ_ESTADOS = "matriz-estados.txt";
    private static final String DIR_MATRIZ_ACC_SEMANTICAS = "matriz-acc-semanticas.txt";

    /*
        // DUDAS
        TODO: Se podían hacer llamadas a función en una comparación??? (En lo posible hacernos los boludos...)

        // COSAS MENORES
        TODO: Chequear lo del LABEL_END que aparece dos veces
        TODO: Corregir los textos de las excepciones
        TODO: Una vez generado el assembler, agregar comentarios a cada línea generada para explicar un poco la movida
        TODO: Generar JavaDoc
     */

    /**
     * Inicio de la ejecución.
     *
     * @param args Argumentos de la aplicación enviados por la línea de comandos.
     */
    public static void main(String[] args) {
//        runInProductionMode();
        runInDevelopmentMode("archivo-prueba7.txt");
    }

    /**
     * Método privado auxiliar para utilizar el compilador en desarrollo. Esto es efectuar la compilación para
     * un archivo en particular, pasado por parámetro.
     *
     * @param fileDir Ruta del archivo a compilar.
     */
    private static void runInDevelopmentMode(String fileDir) {
        TablaSimbolos tablaSimbolos = new TablaSimbolos();

        Lexer lexer;
        try {
            lexer = new Lexer(fileDir, DIR_MATRIZ_ESTADOS, DIR_MATRIZ_ACC_SEMANTICAS, tablaSimbolos);
        } catch (FileNotFoundException e) {
            System.err.println("\nArchivo no encontrado");
            return;
        }

        Parser parser = new Parser();
        parser.setLexico(lexer);
        parser.setTablaSimbolos(tablaSimbolos);

        int resultadoParsing = parser.yyparse();

        System.out.println("\nRESULTADO DEL PARSING: " + resultadoParsing);

        System.out.println("\nERRORES");
        for (String errorlexico : tablaSimbolos.getErroresLexicos())
            System.out.println(errorlexico);
        for (String error : parser.getErrores())
            System.out.println(error);

        if (resultadoParsing == 1 || parser.getErrores().size() != 0)
            return;

        System.out.println("\n" + tablaSimbolos);

        System.out.println("\nTERCETOS");
        for (Terceto terceto : parser.getTercetos())
            System.out.println(terceto);

        System.out.println("\nASSEMBLER");
        Generador generador = new Generador(parser.getTercetos(), tablaSimbolos);
        generador.generateAssembler();
        for (String inst : generador.getListaInstrucciones())
            System.out.println(inst);

        generador.buildFile("salida.asm");
    }

    /**
     * Método privado auxiliar para testear el compilador en producción. Esto es con un bucle, solicitando al
     * usuario ingresar la ruta del archivo por línea de comandos.
     */
    private static void runInProductionMode() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese ruta del archivo (EXIT para salir): ");
        String fileDir = scanner.nextLine();

        if (fileDir.equalsIgnoreCase("EXIT"))
            return;

        TablaSimbolos tablaSimbolos = new TablaSimbolos();

        Lexer lexer;
        try {
            lexer = new Lexer(fileDir, DIR_MATRIZ_ESTADOS, DIR_MATRIZ_ACC_SEMANTICAS, tablaSimbolos);
        } catch (FileNotFoundException e) {
            System.err.println("\nArchivo no encontrado");
            return;
        }

        Parser parser = new Parser();
        parser.setLexico(lexer);
        parser.setTablaSimbolos(tablaSimbolos);

        int resultadoParsing = parser.yyparse();

        System.out.println("\nRESULTADO DEL PARSING: " + resultadoParsing);

        System.out.println("\nERRORES");
        List<String> erroresLexicos = tablaSimbolos.getErroresLexicos();
        if (erroresLexicos.size() != 0) {
            for (String errorlexico : erroresLexicos)
                System.out.println(errorlexico);
            for (String error : parser.getErrores())
                System.out.println(error);
        }

        if (resultadoParsing == 1 || parser.getErrores().size() != 0)
            return;

        System.out.println("NO HAY ERRORES");

        Generador generador = new Generador(parser.getTercetos(), tablaSimbolos);
        generador.generateAssembler();

        generador.buildFile(fileDir + ".asm");
        System.out.println("Se generó el código assembler, identificado como --> " + fileDir + ".asm");
    }
}

