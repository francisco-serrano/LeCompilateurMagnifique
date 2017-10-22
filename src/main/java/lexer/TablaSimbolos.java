package lexer;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.*;

/**
 * Clase que representa la tabla de símbolos del compilador. Está compuesta por una lista de palabras reservadas y por
 * un multimapa que mapea el tipo de token (String) a una colección de lexemas asociados. Cada lexema está representado
 * con la clase Token en la que, según el caso, se aprovecha total o parcialmente la funcionalidad de la misma.
 *
 * @author Martín Bianco, Esteban Di Pietro, Francisco Serrano
 */
public class TablaSimbolos {

    private static final String[] arr_reservedWords = {"IF", "THEN", "ELSE", "END_IF", "BEGIN", "END", "OUT",
            "WHILE", "DO", "FUNCTION", "RETURN", "MOVE", "UINT", "ULONG"};

    private Multimap<String, Token> multimap = ArrayListMultimap.create();
    private List<String> reservedWords = Arrays.asList(arr_reservedWords);

    /**
     * Añade un lexema a la tabla de símbolos
     *
     * @param tipoToken Tipo de token asociado al lexema que se quiere insertar
     * @param lexema    Lexema propiamente dicho
     */
    public void add(String tipoToken, String lexema) {
        if (multimap.get(tipoToken).contains(new Token(lexema)))
            return; // Quiere decir que el token ya fue agregado

        if (tipoToken.equals("ID") || tipoToken.equals("CTE") || tipoToken.equals("CADENA"))
            multimap.put(tipoToken, new Token(lexema));
        else
            throw new IllegalArgumentException("Los tipos de token disponibles son ID, CTE y CADENA");
    }

    public void addConstant(String tipoToken, String lexema, String tipo) {
        if (multimap.get(tipoToken).contains(new Token(lexema)))
            return; // Quiere decir que el token ya fue agregado

        if (tipoToken.equals("CTE")) {
            Token tok = new Token(lexema);
            tok.declare(tipo);
            multimap.put(tipoToken, tok);
        } else
            throw new IllegalArgumentException("Los tipos de token disponibles son ID, CTE y CADENA");
    }

    /**
     * Pregunta si un determinado lexema está presente en la tabla de símbolos
     *
     * @param tipoToken Tipo de token asociado al lexema
     * @param lexema    Lexema propiamente dicho
     * @return Booleano indicando si está presente o no
     */
    public boolean contains(String tipoToken, String lexema) {
        return multimap.get(tipoToken).contains(lexema);
    }

    /**
     * Pregunta si una determinada palabra está definida como palabra reservada
     *
     * @param reservedWord Texto que representa a la palabra reservada a consultar
     * @return Booleano indicando si la palabra es efectivamente reservada o no
     */
    public boolean containsReservedWord(String reservedWord) {
        return reservedWords.contains(reservedWord);
    }

    /**
     * Obtiene los lexemas asociados a un determinado tipo de token
     *
     * @param tipoToken Texto indicando el tipo de token (ID/CTE/CADENA)
     * @return Colección de lexemas asociado al tipo de token
     */
    public Collection<Token> getLexemas(String tipoToken) {
        return multimap.get(tipoToken);
    }

    /**
     * Pregunta si una variable (lexema) está declarada (equivalente a estar presente en la TS)
     *
     * @param lexema Variable a consultar
     * @return Booleano indicando si la variable está declarada o no
     */
    public boolean functionDefined(String lexema, String uso) {
        boolean result = false;
        String aux = lexema.toLowerCase();
        for (Token token : multimap.get("ID")) {
            if (token.getLexema().equals(aux)) {
                if (token.getUso().equals(uso)) {
                    result = true;
                }
            }
        }
        return result;
    }

    public boolean redefined(List<String> lexemas, String ambito) {
        for (String lexema : lexemas)
            if (redefined(lexema, ambito))
                return true;

        return false;
    }

    private boolean redefined(String lexema, String ambito) {
        Token token = getToken("ID", lexema);

        return token != null && token.getUso().equals("variable") && token.getAmbito().equals(ambito);
    }

    public boolean varDefined(String lexema, String ambito, boolean moveFunction) {
        // TODO: por ahí llevar a lowercase es innecesario
        lexema = lexema.toLowerCase();

        if (moveFunction)
            return varDefinedLocalScope(lexema, ambito);

        return varDefinedGlobalScope(lexema, ambito);
    }

    private boolean varDefinedLocalScope(String lexema, String ambito) {
        Token token = getToken("ID", lexema);

        return token != null && token.getAmbito().equalsIgnoreCase(ambito);

    }

    private boolean varDefinedGlobalScope(String lexema, String ambito) {
        if (varDefinedLocalScope(lexema, ambito))
            return true;

        Token token = getToken("ID", lexema);

        return token != null && ambito.contains(token.getAmbito());

    }

    private Token getToken(String tipoToken, String lexema) {
        for (Token token : multimap.get(tipoToken))
            if (token.getLexema().equals(lexema))
                return token;

        return null;
    }

//    /**
//     * Asigna un determinado tipo a un conjunto de variables (lexemas)
//     *
//     * @param lexemas Lista de variables a declarar
//     * @param type    Tipo asociado a la lista de variables
//     */
//    public void defineVar(List<String> lexemas, String type, String uso, String ambito) {
//        if (!uso.equals("nombre_funcion")) {
//            for (Token token : multimap.get("ID")) {
//
//                if (lexemas.contains(token.getLexema())) {
//                    String aux_tipo = token.getType();
//                    List<String> aux_ambito = token.getAmbitoEspecial();
//                    token.declare(type);
//                    token.setUso(uso);
//                    token.setAmbito(ambito);
//
//
////                    if (token.getAmbito().size() > 1){
////                        token.borrarAmbito();
////
////                        for (String anAux_ambito : aux_ambito)
////                            token.setAmbito(anAux_ambito);
////
////                        token.declare(aux_tipo);
////                        Token nuevoToken = new Token(token.getLexema());
////                        multimap.put("ID", nuevoToken);
////                        nuevoToken.setUso(uso);
////                        nuevoToken.declare(type);
////                        nuevoToken.setAmbito(ambito);
////                    }
//
//                }
//            }
//        } else {
//            List<Token> token = (List<Token>) multimap.get("ID");
//            for (int i = 0; i < token.size(); i++) {
//                if ((lexemas.contains(token.get(i).getLexema())) && (!token.get(i).getUso().equals("nombre_funcion"))) {
//                    Token nuevoToken = new Token(token.get(i).getLexema());
//                    nuevoToken.setUso(uso);
//                    nuevoToken.declare(type);
//                    multimap.put("ID", nuevoToken);
//                }
//            }
//        }
//    }

    public void defineVar(List<String> lexemas, String type, String uso, String ambito) {
        if (!uso.equals("nombre_funcion") && !uso.equals("variable"))
            throw new IllegalArgumentException("Los usos soportados son \'nombre_funcion\' y \'variable\'");

        if (uso.equals("nombre_funcion"))
            defineFunction(lexemas.get(0), type, ambito);

        if (uso.equals("variable"))
            defineVariables(lexemas, type, ambito);
    }

    private void defineFunction(String lexema, String type, String ambito) {
        Token token = getToken("ID", lexema);

        // TODO: acomodar
        if (token == null)
            throw new RuntimeException("QUINCE PESOS");

        token.declare(type);
        token.setUso("nombre_funcion");
        token.setAmbito(ambito);
    }

    private void defineVariables(List<String> lexemas, String type, String ambito) {
        for (String lexema: lexemas)
            defineVar(lexema, type, ambito);
    }

    private void defineVar(String lexema, String type, String ambito) {
        Token token = getToken("ID", lexema);

        // TODO: temporal, acomodar el texto
        if (token == null)
            throw new RuntimeException("QUINCE PESOS");

        if (!token.getAmbito().equals("undefined")) {
            token = new Token(lexema);
            multimap.put("ID", token);
        }

        token.declare(type);
        token.setUso("variable");
        token.setAmbito(ambito);
    }



    /**
     * Devuelve el tipo asociado a la variable declarada
     *
     * @param lexema Variable de la que se quiere obtener el tipo
     * @return Tipo de la variable pasada por parámetro (null si no está declarada)
     */
    public String getVarType(String lexema) {
        for (Token token : multimap.get("ID")) {
            if (token.getLexema().equals(lexema))
                return token.getType();
        }

        return null;
    }

    /**
     * Devuelve el tipo de valor (UINT/ULONG) de la constante
     *
     * @param cte Constante numérica representada en forma de texto
     * @return Tipo de valor (UINT/ULONG)
     */
    public String getType(String cte) {
        long auxCte = Long.parseLong(cte);

        if (auxCte > 65536L)
            return "ULONG";

        return "UINT";
    }

    public Token devolverToken(String lexema) {
        for (Token token : multimap.get("ID")) {
            if (token.getLexema().equals(lexema))
                return token;
        }
        for (Token token : multimap.get("CTE")) {
            if (token.getLexema().equals(lexema))
                return token;
        }
        for (Token token : multimap.get("CADENA")) {
            if (token.getLexema().equals(lexema))
                return token;
        }
        return null;
    }

    /**
     * Imprime en contenido de la tabla de símbolos
     *
     * @return String representando la tabla de símbolos
     */
    @Override
    public String toString() {
        StringBuilder aux = new StringBuilder();

        for (String key : multimap.keySet()) {
            aux.append(key).append("\n");
            for (Token token : multimap.get(key))
                aux.append(token).append("\n");

            aux.append("\n");
        }

        return aux.toString();
    }
}
