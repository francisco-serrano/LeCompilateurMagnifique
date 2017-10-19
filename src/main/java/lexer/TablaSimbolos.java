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
        String uso = "variable";

        List<Token> tokens = (List<Token>) multimap.get("ID");
        for (Token token : tokens) {
            if (lexemas.contains(token.getLexema())) {
                if (token.getUso().equals(uso)) {
                    List<String> listaAmbitos = token.getAmbito();
                    for (String elem : listaAmbitos) {
                        if (elem.equals(ambito))
                            return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean varDefined(String lexema, String ambito, boolean move) {
        String aux_lexema = lexema.toLowerCase();
        String aux_ambito = ambito.toLowerCase();
        boolean resultado = false;
        if (!move) {
            String[] parts = aux_ambito.split("@");
            String part1 = "";
            if (parts.length == 3) {
                part1 = "@" + parts[1];
            }
            for (Token token : multimap.get("ID")) {
                if (token.getLexema().equals(aux_lexema)) {
                    List<String> ambitos = token.getAmbito();
                    for (int i = 0; i < ambitos.size(); i++) {
                        if ((ambitos.get(i).equals(part1)) || (ambitos.get(i).equals(aux_ambito)))
                            return true;
                    }
                }
            }
        } else {
            for (Token token : multimap.get("ID")) {
                if (token.getLexema().equals(aux_lexema)) {
                    List<String> ambitos = token.getAmbito();
                    for (int i = 0; i < ambitos.size(); i++) {
                        if (ambitos.get(i).equals(aux_ambito))
                            return true;
                    }
                }
            }
        }
        return resultado;
    }

    /**
     * Asigna un determinado tipo a un conjunto de variables (lexemas)
     *
     * @param lexemas Lista de variables a declarar
     * @param type    Tipo asociado a la lista de variables
     */
    public void defineVar(List<String> lexemas, String type, String uso, String ambito) {
        if (!uso.equals("Nombre_Funcion")) {
            for (Token token : multimap.get("ID")) {
                if (lexemas.contains(token.getLexema())) {
                    String aux_tipo = token.getType();
                    List<String> aux_ambito = token.getAmbitoEspecial();
                    token.declare(type);
                    token.setUso(uso);
                    token.setAmbito(ambito);
                   /* if (token.getAmbito().size()>1){
                        token.borrarAmbito();
                        for (int j = 0; j < aux_ambito.size(); j++) {
                            token.setAmbito(aux_ambito.get(j));
                        }
                        token.declare(aux_tipo);
                        Token nuevoToken = new Token(token.getLexema());
                        multimap.put("ID", nuevoToken);
                        nuevoToken.setUso(uso);
                        nuevoToken.declare(type);
                        nuevoToken.setAmbito(ambito);
                    }*/
                }
            }
        } else {
            List<Token> token = (List<Token>) multimap.get("ID");
            for (int i = 0; i < token.size(); i++) {
                if ((lexemas.contains(token.get(i).getLexema())) && (!token.get(i).getUso().equals("Nombre_Funcion"))) {
                    Token nuevoToken = new Token(token.get(i).getLexema());
                    nuevoToken.setUso(uso);
                    nuevoToken.declare(type);
                    multimap.put("ID", nuevoToken);
                }
            }
        }
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
//        StringBuilder aux = new StringBuilder();
//
//        for (String key : multimap.keySet())
//            aux.append(key).append(" --> ").append(this.getLexemas(key)).append("\n");
//
//        return aux.toString();

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
