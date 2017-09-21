package accionsemantica;

import jdk.nashorn.internal.parser.Token;

public class AS6 extends AccionSemantica {
    public AS6(TablaSimbolos t) {
        super(t);
    }

    @Override
    public void aplicarAccion(char a, int indice) {

        //chequeo rango mayor (ulong)
        if (( Double.parseDouble(token.toString()) >= 0) && (Double.parseDouble(token.toString()) <= (Math.pow(2,32)-1) )){
            if (!tablita.contains("CTE", token.toString())){
                tablita.add("CTE",token.toString());
            }
            System.out.println("CTE:" + " " + token.toString());
        }
        else{
            System.out.println("Linea " + linea + ": Constante fuera de rango");
        }

        this.indice = indice-1;

    }
}
