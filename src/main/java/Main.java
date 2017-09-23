
public class Main {

    public static void main(String[] args) {
        String fileDir = "archivo-prueba2.txt";
        String dirMatrizEstados = "matriz-estados.txt";
        String dirMatrizSemantica = "matriz-acc-semanticas.txt";

        Lexer lexer = new Lexer(fileDir, dirMatrizEstados, dirMatrizSemantica);

//        int token;
//        while ((token = lexer.yylex()) != 0)
//            System.out.println(token + ", " + lexer.getTipoToken(token));

        Parser parser = new Parser(lexer);
        System.out.println(parser.yyparse());

    }
}
