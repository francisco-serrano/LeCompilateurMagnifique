package accionsemantica;

import lexer.TablaSimbolos;

public class AS6 extends AccionSemantica {
    public AS6(TablaSimbolos t) {
        super(t);
    }

    @Override
    public String aplicarAccion(char a, int indice) {

        String token_retornar = null;

        // Chequeo rango mayor (ulong)
        if ((Double.parseDouble(token.toString()) >= 0) && (Double.parseDouble(token.toString()) <= (Math.pow(2, 32) - 1))) {
            if (!tablita.contains("CTE", token.toString()))
                tablita.add("CTE", token.toString());

            token_retornar = "CTE->" + token.toString();
        } else {
            System.out.println("Linea " + linea + ": Constante fuera de rango");
        }

        this.indice = indice - 1;

        return token_retornar;
    }
}
