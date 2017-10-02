package accionsemantica;

import lexer.TablaSimbolos;

public class AS16 extends AccionSemantica {
    public AS16(TablaSimbolos t) {
        super(t);
    }

    @Override
    public String aplicarAccion(char a, int indice) {
        this.indice = indice - 1;

        return "->" + token.toString();
    }
}
