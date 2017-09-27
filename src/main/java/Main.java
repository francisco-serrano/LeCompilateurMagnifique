import accionsemantica.TablaSimbolos;

public class Main {

    public static void main(String[] args) {
        String fileDir = "archivo-prueba4.txt";
        String dirMatrizEstados = "matriz-estados.txt";
        String dirMatrizSemantica = "matriz-acc-semanticas.txt";

        TablaSimbolos tablaSimbolos = new TablaSimbolos();

        Lexer lexer = new Lexer(fileDir, dirMatrizEstados, dirMatrizSemantica, tablaSimbolos);

//        int token;
//        while ((token = lexer.yylex()) != 0)
//            System.out.println("<" + token + ", " + lexer.getTipoToken(token) + ">");

        // PARSER: 0 -> ACEPTADA; 1 -> RECHAZADA
        Parser parser = new Parser();
        parser.setLexico(lexer);
        parser.setTablaSimbolos(tablaSimbolos);
        System.out.println(parser.yyparse());

//        for (String key : tablaSimbolos.keySet())
//            System.out.println(key + " -> " + tablaSimbolos.getLexemas(key));


    }
}
