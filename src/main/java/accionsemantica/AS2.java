package accionsemantica;

public class AS2 extends AccionSemantica {

    public AS2(TablaSimbolos t) {
        super(t);
    }

    @Override
    public String aplicarAccion(char a, int indice) {

        token.append(a);

        this.indice = indice;

        return null;
    }
}
