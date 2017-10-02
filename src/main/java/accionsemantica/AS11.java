package accionsemantica;

import lexer.TablaSimbolos;

public class AS11 extends AccionSemantica {
    public AS11(TablaSimbolos t) {
        super(t);
    }

    @Override
    public String aplicarAccion(char a, int indice) {
        token.append(a);

        this.indice = indice;

        return "->" + token.toString();
    }
}
