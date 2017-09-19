package accionsemantica;


public class AS2 extends AccionSemantica {
    @Override
    public void aplicarAccion(char a) {
        token.append(a);
    }
}
