package lexer;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.Arrays;
import java.util.List;

/**
 * Clase que representa la tabla de símbolos del compilador. Está compuesta por una lista de palabras reservadas y por
 * un multimapa que mapea el tipo de token (String) a una colección de lexemas asociados a dicho tipo. Cada lexema está
 * representado con la clase Token en la que, según el caso, se aprovecha total o parcialmente la funcionalidad de
 * la misma.
 *
 * @author Martín Bianco, Esteban Di Pietro, Francisco Serrano
 */
public class TablaSimbolos {

    private static final String[] arr_reservedWords = {"IF", "THEN", "ELSE", "END_IF", "BEGIN", "END", "OUT",
            "WHILE", "DO", "FUNCTION", "RETURN", "MOVE", "UINT", "ULONG"};

    private Multimap<String, Token> multimap = ArrayListMultimap.create();
    private List<String> reservedWords = Arrays.asList(arr_reservedWords);

    /**
     * Añade un lexema a la tabla de símbolos.
     *
     * @param tipoToken Tipo de token asociado al lexema que se quiere insertar.
     * @param lexema    Lexema propiamente dicho.
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
     * Añade a la Tabla de Símbolos un token asociado a una constante.
     *
     * @param lexema Lexema que se quiere almacenar en el Token.
     * @param tipo Tipo de dato de la constante a declarar en la TS.
     */
    public void addConstant(String lexema, String tipo) {
        Token token = new Token(lexema);

        if (multimap.get("CTE").contains(token))
            return; // Quiere decir que el token ya fue agregado

        token.declare(tipo);

        multimap.put("CTE", token);
    }

    /**
     * Pregunta si un determinado lexema está presente en la tabla de símbolos.
     *
     * @param tipoToken Tipo de token asociado al lexema.
     * @param lexema    Lexema propiamente dicho.
     * @return Booleano indicando si está presente o no.
     */
    public boolean contains(String tipoToken, String lexema) {
        return multimap.get(tipoToken).contains(new Token(lexema));
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
     * Pregunta si una función está efectivamente declarada en la Tabla de Símbolos.
     *
     * @param lexema Lexema que representa el nombre de la función por el cual se quiere preguntar.
     * @return Booleano indicando si la función está declarada o no.
     */
    public boolean functionDefined(String lexema) {
        lexema = lexema.toLowerCase();

        for (Token token : multimap.get("ID")) {
            if (token.getLexema().equals(lexema) && token.getUso().equals("nombre_funcion"))
                return true;
        }

        return false;
    }

    /**
     * Pregunta si en una declaración de variables, existe alguna que fue redefinida.
     * @param lexemas Lista de variables sobre las que se quiere consultar.
     * @param ambito Ambito en donde se dió la declaración.
     * @return Booleano indicando si existe alguna variables redefinida entre las provistas.
     */
    public boolean redefined(List<String> lexemas, String ambito) {
        for (String lexema : lexemas)
            if (redefined(lexema, ambito))
                return true;

        return false;
    }

    /**
     * Método privado auxiliar que pregunta si una variable en especial ya fue definida.
     * @param lexema Variable sobre la que se quiere consultar.
     * @param ambito Ambito en donde se dió la declaración.
     * @return Booleano indicando si la variable sería redefinida en el ámbito provisto.
     */
    private boolean redefined(String lexema, String ambito) {
        Token token = getToken(lexema, ambito);

        return token != null && token.getUso().equals("variable") && token.getAmbito().equals(ambito);
    }

    public boolean varDefined(String lexema, String ambito, boolean moveFunction) {
        // TODO: por ahí llevar a lowercase es innecesario
        lexema = lexema.toLowerCase();

        if (moveFunction)
            return varDefinedLocalScope(lexema, ambito);

        return varDefinedGlobalScope(lexema, ambito);
    }

    /**
     * Método privado auxiliar que determina si una variable está definida en el ámbito local actual.
     * @param lexema Variable sobre la que se efectúa la consulta en la tabla.
     * @param ambito Ambito local actual.
     * @return Booleano indicando si la variable está definida en el ámbito local actual.
     */
    private boolean varDefinedLocalScope(String lexema, String ambito) {
        Token token = getToken(lexema, ambito);

        return token != null;
    }

    /**
     * Método privado auxiliar que determina si una variable está definida en el ámbito global al actual.
     * @param lexema Variable sobre la que se efectúa la consulta en la tabla.
     * @param ambito Ámbito global al actual.
     * @return Booleano indicando si la variable está definida en el ámbito global al actual.
     */
    private boolean varDefinedGlobalScope(String lexema, String ambito) {
        if (varDefinedLocalScope(lexema, ambito))
            return true;

        Token token = getToken(lexema);

        return token != null && ambito.contains(token.getAmbito());
    }

    /**
     * Define un conjunto de identificadores (variable/función) en la tabla de símbolos.
     * @param lexemas Lista de identificadores a definir.
     * @param type Tipo de dato asociado a los identificadores.
     * @param uso Uso asociado a los identificadores(nombre_funcion/variable).
     * @param ambito Ambito en donde se produce la declaración.
     */
    public void defineVar(List<String> lexemas, String type, String uso, String ambito) {
        if (!uso.equals("nombre_funcion") && !uso.equals("variable"))
            throw new IllegalArgumentException("Los usos soportados son \'nombre_funcion\' y \'variable\'");

        if (uso.equals("nombre_funcion"))
            defineFunction(lexemas.get(0), type, ambito);

        if (uso.equals("variable"))
            defineVariables(lexemas, type, ambito);
    }

    /**
     * Método privado auxiliar que define una función en la tabla de símbolos.
     * @param lexema Identificador de la función a definir.
     * @param type Tipo de retorno de la función a definir.
     * @param ambito Ambito en donde se da la declaración de la función.
     */
    private void defineFunction(String lexema, String type, String ambito) {
        Token token = getToken(lexema);

        if (token == null)
            throw new RuntimeException("El token con los siguientes datos {" + lexema + ", " + type + ", " + ambito + "} no existe");

        token.declare(type);
        token.setUso("nombre_funcion");
        token.setAmbito(ambito);
    }

    /**
     * Método privado auxiliar que define un listado de variables.
     * @param lexemas Lista de variables a definir.
     * @param type Tipo de dato asociado a las variables a definir.
     * @param ambito Ambito en donde se produce la definición de las variables.
     */
    private void defineVariables(List<String> lexemas, String type, String ambito) {
        for (String lexema: lexemas)
            defineVar(lexema, type, ambito);
    }

    /**
     * Método privado auxiliar que define una variable en la TS.
     * @param lexema Variable a definir.
     * @param type Tipo de dato asociado a la variable a definir.
     * @param ambito Ambito en donde se produce la definición de la variable.
     */
    private void defineVar(String lexema, String type, String ambito) {
        Token token = getToken(lexema);

        if (token == null)
            throw new RuntimeException("El token con los siguientes datos {" + lexema + ", " + type + ", " + ambito + "} no existe");

        if (!token.getAmbito().equals("undefined")) {
            token = new Token(lexema);
            multimap.put("ID", token);
        }

        token.declare(type);
        token.setUso("variable");
        token.setAmbito(ambito);
    }

    /**
     * Obtiene un token de la Tabla de Símbolos mediante la especificación del lexema.
     * @param lexema Lexema con el cual se identifica al token a retornar.
     * @return Token solicitado.
     */
    public Token getToken(String lexema) {
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
     * Método privado auxiliar que retorna un token de la TS con la restricción adicional del ámbito.
     * @param lexema Lexema con el cual se identifica al token a retornar.
     * @param ambito Ambito con el que se idenficia al token a retornar.
     * @return Token solicitado.
     */
    private Token getToken(String lexema, String ambito) {
        for (Token token : multimap.get("ID"))
            if (token.getLexema().equals(lexema) && token.getAmbito().equals(ambito))
                return token;

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
