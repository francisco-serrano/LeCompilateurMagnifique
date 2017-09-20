package accionsemantica;

public class AS1 extends AccionSemantica {
    @Override
    public void aplicarAccion(char a, int indice) {
        token = new StringBuilder().append(a);

        this.indice = indice;
    }
}
