package accionsemantica;


import lexer.TablaSimbolos;

public class ASError extends AccionSemantica {

    public ASError(TablaSimbolos t) {
        super(t);
    }

    @Override
    public String aplicarAccion(char a, int indice) {

        System.err.println("Linea " + this.linea + ": El siguiente caracter es invalido-> " + a );

        this.indice = indice;

        return null;
    }

}
