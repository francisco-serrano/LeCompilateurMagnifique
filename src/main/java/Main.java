import lexer.TablaSimbolos;
import archivosyacc.Parser;
import lexer.Lexer;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Clase utilizada para iniciar la ejecución del compilador.
 *
 * El funcionamiento se basa en una CLI en la que se ingresa la ruta del archivo a compilar, para que posteriormente
 * al dar ENTER se proceda a realizar efectivamente la compilación. Una vez finalizado el proceso para el archivo
 * ingresado, se otorga la posibilidad de ingresar otro archivo para luego proceder de forma análoga.
 *
 * En caso de ingresar "EXIT", se da por finalizada la ejecución del compilador.
 *
 * @author Bianco Martín, Di Pietro Esteban, Serrano Francisco
 */
public class Main {

    /*
        TODO: JavaDoc -> Clase Lexer
        TODO: JavaDoc -> Acciones Semánticas
        TODO: JavaDoc -> Parser (Dejarlo para el final del trabajo)
     */

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Ingrese ruta del archivo (EXIT para salir): ");
            String fileDir = scanner.nextLine();

            // Dejarlo comentado para después ir probando con un archivo fijo
//            String fileDir = "archivo-prueba4.txt";

            if (fileDir.equalsIgnoreCase("EXIT"))
                break;

            String dirMatrizEstados = "matriz-estados.txt";
            String dirMatrizSemantica = "matriz-acc-semanticas.txt";

            TablaSimbolos tablaSimbolos = new TablaSimbolos();

            Lexer lexer;
            try {
                lexer = new Lexer(fileDir, dirMatrizEstados, dirMatrizSemantica, tablaSimbolos);
            } catch (FileNotFoundException e) {
                System.err.println("\nArchivo no encontrado");
                continue;
            }

            // En caso de querer probar los tokens que se imprimen
//            Main.consumeTokens(lexer);

            // PARSER: 0 -> ACEPTADA; 1 -> RECHAZADA
            Parser parser = new Parser();
            parser.setLexico(lexer);
            parser.setTablaSimbolos(tablaSimbolos);
            System.out.println("\nRESULTADO DEL PARSING: " + parser.yyparse() + "\n");

            System.out.println(tablaSimbolos);

            System.out.println("\n");
        }
    }

    /**
     * Método auxiliar para controlar el funcionamiento del análisis léxico. Imprime los tokens
     * reconocidos por salida estándar.
     * @param lexer Analizador Léxico a controlar
     */
    private static void consumeTokens(Lexer lexer) {
        int token;
        while ((token = lexer.yylex()) != 0)
            System.out.println("<" + token + ">");
    }
}
