package accionsemantica;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.*;

public class TablaSimbolos {

    private static final String[] arr_reservedWords = {"IF", "THEN", "ELSE", "END_IF", "BEGIN", "END", "OUT",
            "WHILE", "DO", "FUNCTION", "RETURN", "MOVE", "UINT", "ULONG"};

    private Multimap<String, Token> multimap = ArrayListMultimap.create();
    private List<String> reservedWords = Arrays.asList(arr_reservedWords);

    public void add(String tipoToken, String lexema) {
        if (multimap.get(tipoToken).contains(new Token(lexema)))
            return; // Quiere decir que ya fue agregado el token

        if (tipoToken.equals("ID") || tipoToken.equals("CTE") || tipoToken.equals("CADENA"))
            multimap.put(tipoToken, new Token(lexema));
        else
            throw new IllegalArgumentException("Los tipos de token disponibles son ID, CTE y CADENA");
    }

    public boolean contains(String tipoToken, String lexema) {
        return multimap.get(tipoToken).contains(lexema);
    }

    public boolean containsReservedWord(String reservedWord) {
        return reservedWords.contains(reservedWord);
    }

    public Set<String> keySet() {
        return multimap.keySet();
    }

    public Collection<Token> getLexemas(String tipoToken) {
        return multimap.get(tipoToken);
    }

    public boolean varDefined (String lexema) {
        for (Token token : multimap.get("ID")) {
            if (token.getLexema().equals(lexema))
                return token.isDefined();
        }

        throw new RuntimeException("Token no disponible en la Tabla de Símbolos");
    }

    public void defineVar (List<String> lexemas, String type) {
        for (Token token : multimap.get("ID")) {
            if (lexemas.contains(token.getLexema())) {
                token.define(type);
            }
        }
    }

    public String getVarType (String lexema) {
        for (Token token : multimap.get("ID")) {
            if (token.getLexema().equals(lexema))
                return token.getType();
        }

        return null;
    }

    public String getType(String cte) {
        long auxCte = Long.parseLong(cte);

        if (auxCte > 65536L)
            return "ULONG";

        return "UINT";
    }
}
