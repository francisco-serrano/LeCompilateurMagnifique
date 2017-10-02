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
     * @param tipoToken Tipo de token asociado al lexema que se quiere insertar
     * @param lexema Lexema propiamente dicho
     */
    public void add(String tipoToken, String lexema) {
        if (multimap.get(tipoToken).contains(new Token(lexema)))
            return; // Quiere decir que el token ya fue agregado

        if (tipoToken.equals("ID") || tipoToken.equals("CTE") || tipoToken.equals("CADENA"))
            multimap.put(tipoToken, new Token(lexema));
        else
            throw new IllegalArgumentException("Los tipos de token disponibles son ID, CTE y CADENA");
    }

    /**
     * Pregunta si un determinado lexema está presente en la tabla de símbolos
     * @param tipoToken Tipo de token asociado al lexema
     * @param lexema Lexema propiamente dicho
     * @return Booleano indicando si está presente o no
     */
    public boolean contains(String tipoToken, String lexema) {
        return multimap.get(tipoToken).contains(lexema);
    }

    /**
     * Pregunta si una determinada palabra está definida como palabra reservada
     * @param reservedWord Texto que representa a la palabra reservada a consultar
     * @return Booleano indicando si la palabra es efectivamente reservada o no
     */
    public boolean containsReservedWord(String reservedWord) {
        return reservedWords.contains(reservedWord);
    }

    /**
     * Obtiene los lexemas asociados a un determinado tipo de token
     * @param tipoToken Texto indicando el tipo de token (ID/CTE/CADENA)
     * @return Colección de lexemas asociado al tipo de token
     */
    public Collection<Token> getLexemas(String tipoToken) {
        return multimap.get(tipoToken);
    }

    /**
     * Pregunta si una variable (lexema) está declarada (equivalente a estar presente en la TS)
     * @param lexema Variable a consultar
     * @return Booleano indicando si la variable está declarada o no
     */
    public boolean varDefined (String lexema) {
        for (Token token : multimap.get("ID")) {
            if (token.getLexema().equals(lexema))
                return token.isDeclared();
        }

        throw new RuntimeException("Token no disponible en la Tabla de Símbolos");
    }

    /**
     * Asigna un determinado tipo a un conjunto de variables (lexemas)
     * @param lexemas Lista de variables a declarar
     * @param type Tipo asociado a la lista de variables
     */
    public void defineVar (List<String> lexemas, String type) {
        for (Token token : multimap.get("ID")) {
            if (lexemas.contains(token.getLexema()))
                token.declare(type);
        }
    }

    /**
     * Devuelve el tipo asociado a la variable declarada
     * @param lexema Variable de la que se quiere obtener el tipo
     * @return Tipo de la variable pasada por parámetro (null si no está declarada)
     */
    public String getVarType (String lexema) {
        for (Token token : multimap.get("ID")) {
            if (token.getLexema().equals(lexema))
                return token.getType();
        }

        return null;
    }

    /**
     * Devuelve el tipo de valor (UINT/ULONG) de la constante
     * @param cte Constante numérica representada en forma de texto
     * @return Tipo de valor (UINT/ULONG)
     */
    public String getType(String cte) {
        long auxCte = Long.parseLong(cte);

        if (auxCte > 65536L)
            return "ULONG";

        return "UINT";
    }

    /**
     * Imprime en contenido de la tabla de símbolos
     * @return String representando la tabla de símbolos
     */
    @Override
    public String toString() {
        StringBuilder aux = new StringBuilder();

        for (String key : multimap.keySet())
            aux.append(key).append(" --> ").append(this.getLexemas(key)).append("\n");

        return aux.toString();
    }
}
