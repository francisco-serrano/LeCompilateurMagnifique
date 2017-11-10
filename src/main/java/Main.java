import com.google.common.base.Joiner;
import generadorcodigo.Generador;
import lexer.*;
import parser.Parser;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Clase utilizada para iniciar la ejecución del compilador.
 * <p>
 * El funcionamiento se basa en una CLI en la que se ingresa la ruta del archivo a compilar, para que posteriormente
 * al dar ENTER se proceda a realizar efectivamente la compilación. Una vez finalizado el proceso para el archivo
 * ingresado, se otorga la posibilidad de ingresar otro archivo para luego proceder de forma análoga.
 * <p>
 * En caso de ingresar "EXIT", se da por finalizada la ejecución del compilador.
 *
 * @author Bianco Martín, Di Pietro Esteban, Serrano Francisco
 */
public class Main {

    private static final String DIR_MATRIZ_ESTADOS = "matriz-estados.txt";
    private static final String DIR_MATRIZ_ACC_SEMANTICAS = "matriz-acc-semanticas.txt";

    /*

        // COSAS IMPORTANTES
        TODO: Agregar los chequeos en tiempo de ejecucion
        TODO: Ver la sintaxis de la multiplicación -> ES MUY BARDERA (VER ejemplo2.asm)
        TODO: Agregar soporte para llamadas a función.

        TODO: Chequear que la instrucción MOV no puede usarse para mover variables
        TODO: NO USAR JAVA 8

        // COSAS MENORES
        TODO: Preguntar si las constantes hay que definirlas en la tabla de símbolos
        TODO: Chequear lo del LABEL_END que aparece dos veces
        TODO: Una vez generado el assembler, agregar comentarios a cada línea generada para explicar un poco la movida
        TODO: Definir si el método redefined y varDefined se pueden juntar
        TODO: Acomodar los nombres de los métodos (poner todos en declarar en vez de definir)

        // ACOMODAR
        TODO: Estandarizar la salida por consola
        TODO: JavaDoc -> Clase Lexer
        TODO: JavaDoc -> Acciones Semánticas
        TODO: JavaDoc -> Parser (Dejarlo para el final del trabajo)
     */

    /**
     * Inicio de la ejecución.
     * @param args Argumentos de la aplicación enviados por la línea de comandos.
     */
    public static void main(String[] args) {
        runInDevelopmentMode("archivo-prueba7.txt");
    }

    /**
     * Método privado auxiliar para testear el compilador en desarrollo. Esto es efectuar la compilación para
     * un archivo en particular, pasado por parámetro.
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
        for (String errorlexico : tablaSimbolos.getErroreLexicos())
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
    }

    /**
     * Método privado auxiliar para testear el compilador en producción. Esto es con un bucle, solicitando al
     * usuario ingresar la ruta del archivo por línea de comandos.
     */
    private static void runInProductionMode() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Ingrese ruta del archivo (EXIT para salir): ");
            String fileDir = scanner.nextLine();

            if (fileDir.equalsIgnoreCase("EXIT"))
                break;

            TablaSimbolos tablaSimbolos = new TablaSimbolos();

            Lexer lexer;
            try {
                lexer = new Lexer(fileDir, DIR_MATRIZ_ESTADOS, DIR_MATRIZ_ACC_SEMANTICAS, tablaSimbolos);
            } catch (FileNotFoundException e) {
                System.err.println("\nArchivo no encontrado");
                continue;
            }

            Parser parser = new Parser();
            parser.setLexico(lexer);
            parser.setTablaSimbolos(tablaSimbolos);

            System.out.println("\nRESULTADO DEL PARSING: " + parser.yyparse());

            System.out.println("\n" + tablaSimbolos);

            System.out.println("\nERRORES");
            for (String error : parser.getErrores())
                System.out.println(error);

            System.out.println("\nTERCETOS");
            for (Terceto terceto : parser.getTercetos())
                System.out.println(terceto);
        }
    }
}
