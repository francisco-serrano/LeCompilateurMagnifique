package accionsemantica;

import lexer.TablaSimbolos;

public abstract class AccionSemantica {

    protected static StringBuilder token;
    protected int indice;
    protected TablaSimbolos tablita;
    protected int linea;

    public AccionSemantica(TablaSimbolos t) {
        this.tablita = t;
    }

    public abstract String aplicarAccion(char a, int indice);

    public int getIndice() {
        return indice;
    }

    public void setearlinea(int l) {
        this.linea = l;
    }

    public String getToken() {
        return token.toString();
    }
}
    