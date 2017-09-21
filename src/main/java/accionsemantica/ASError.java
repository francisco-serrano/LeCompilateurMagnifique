package accionsemantica;


public class ASError extends AccionSemantica {

    public ASError(TablaSimbolos t) {
        super(t);
    }

    @Override
    public void aplicarAccion(char a, int indice) {

        System.out.println("Linea " + this.linea + ": El siguiente caracter es invalido-> " + a );

        this.indice = indice;
    }

}
