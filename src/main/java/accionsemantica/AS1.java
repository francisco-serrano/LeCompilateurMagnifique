package accionsemantica;

public class AS1 extends AccionSemantica {
    @Override
    public void aplicarAccion(char a) {
        token = new StringBuilder(a);
    }
}
