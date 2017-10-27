package accionsemantica;

import lexer.TablaSimbolos;

public class AS6 extends AccionSemantica {
    public AS6(TablaSimbolos t) {
        super(t);
    }

    @Override
    public String aplicarAccion(char a, int indice) {

        String token_retornar = null;

        // Chequeo rango menor (ulong)
        if (Double.parseDouble(token.toString()) < 0) {
            System.out.println("Linea " + linea + ": Constante fuera de rango");
        }
        // Si esta dentro del rango
        if (Double.parseDouble(token.toString()) <= (Math.pow(2, 32) - 1)) {
            if (!tablita.contains("CTE", token.toString())) {
                if (Double.parseDouble(token.toString()) <= (Math.pow(2, 16) - 1)) {
                    //Es UINT
                    tablita.addConstant(/*"CTE",*/token.toString(), "UINT");
                } else {
                    //Es ULONG
                    tablita.addConstant(/*"CTE",*/token.toString(), "ULONG");
                }
                token_retornar = "CTE->" + token.toString();
            }
        } else {  //en caso de q sea mayor al rango, calculo el overflow
            long aux = (long) (Double.parseDouble(token.toString())); //long para q no quede con coma
            while (aux > (Math.pow(2, 32) - 1))
                aux = aux - (long) (Math.pow(2, 32) - 1);

            String extra = String.valueOf(aux);
            token = new StringBuilder(String.valueOf(aux));

            if (!tablita.contains("CTE", extra)) {
                tablita.addConstant(/*"CTE",*/extra, "ULONG");
                token_retornar = "CTE->" + extra;
            }
        }
        this.indice = indice - 1;
        return token_retornar;
    }

}
