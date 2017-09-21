package accionsemantica;

public class AS16 extends AccionSemantica  {
    public AS16(TablaSimbolos t) {
        super(t);
    }

    @Override
    public void aplicarAccion(char a, int indice) {

        System.out.println(token.toString());

       this.indice = indice-1;
    }
}
