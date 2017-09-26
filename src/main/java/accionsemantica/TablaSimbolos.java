package accionsemantica;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.*;

public class TablaSimbolos {

    // La tabla de símbolos contiene información de tokens que representen MÁS de un lexema

    private static final String[] arr_reservedWords = {"IF", "THEN", "ELSE", "END_IF", "BEGIN", "END", "OUT",
            "WHILE", "DO", "FUNCTION", "RETURN", "MOVE", "UINT", "ULONG"};

    private Multimap<String, String> multimap = ArrayListMultimap.create();
    private List<String> reservedWords = Arrays.asList(arr_reservedWords);

    public void add(String tipoToken, String lexema) {
        /*
            ID -> <LEXEMA, NRO.LINEA>
         */

        if (tipoToken.equals("ID") || tipoToken.equals("CTE"))
            multimap.put(tipoToken, lexema);
        else
            throw new IllegalArgumentException("Los tipos de token disponibles son ID y CTE");

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

    public Collection<String> getLexemas(String tipoToken) {
        return multimap.get(tipoToken);
    }
}
