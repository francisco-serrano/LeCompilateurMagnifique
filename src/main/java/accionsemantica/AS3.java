package accionsemantica;

public class AS3 extends AccionSemantica {
    public AS3(TablaSimbolos t) {
        super(t);
    }

    @Override
    public String aplicarAccion(char a, int indice) {

        String token_retornar;

        if (token.length() > 15) {
            System.out.println("Warning linea " + linea + ": Identificador con longitud mayor a 15 fue truncado");
            token.delete(15, token.length());
        }

        //  Si es menor a 15 el size del identificador
        if (!tablita.containsReservedWord(token.toString())) {

            // Veo si no es palabra reservada y dado que no es lo paso a minisculas
            if (!tablita.contains("ID", token.toString().toLowerCase()))
                // Si no esta en la tabla lo agrego
                tablita.add("ID", token.toString().toLowerCase());

            token_retornar = "ID->" + token.toString().toLowerCase();
        } else {
            token_retornar = "PALABRA RESERVADA->" + token.toString();
        }

        this.indice = indice - 1;

        return token_retornar;
    }
}
