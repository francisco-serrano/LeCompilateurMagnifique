package accionsemantica;


public class AS2 extends AccionSemantica {

    public AS2(TablaSimbolos t) {
        super(t);
    }

    @Override
    public void aplicarAccion(char a, int indice) {

        token.append(a);

        this.indice = indice;
    }
}
