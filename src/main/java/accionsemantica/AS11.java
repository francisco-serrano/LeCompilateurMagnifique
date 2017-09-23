package accionsemantica;

public class AS11 extends AccionSemantica {
    public AS11(TablaSimbolos t) {
        super(t);
    }

    @Override
    public String aplicarAccion(char a, int indice) {
        token.append(a);


//        System.out.println(token.toString());

        this.indice = indice;

        return "->" + token.toString();
    }
}
