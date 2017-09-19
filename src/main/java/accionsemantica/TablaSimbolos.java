package accionsemantica;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class TablaSimbolos {

    private Multimap<String, String> multimap = ArrayListMultimap.create();

    public TablaSimbolos() {
        addReservedWords();
    }

    public void add(String tipoToken, String lexema) {
        multimap.put(tipoToken, lexema);
    }

    public boolean contains(String tipoToken) {
        return multimap.keySet().contains(tipoToken);
    }

    private void addReservedWords() {
        // Variables y constantes
        multimap.put("ID", null);
        multimap.put("CTE", null);

        // Palabras reservadas
        multimap.put("IF", null);
        multimap.put("THEN", null);
        multimap.put("ELSE", null);
        multimap.put("END_IF", null);
        multimap.put("BEGIN", null);
        multimap.put("END", null);
        multimap.put("OUT", null);

        // Palabras reservadas adicionales
        multimap.put("WHILE", null);
        multimap.put("FUNCTION", null);
        multimap.put("RETURN", null);
        multimap.put("MOVE", null);

        // HAY QUE AGREGAR +, -, /, etc????
    }
}
