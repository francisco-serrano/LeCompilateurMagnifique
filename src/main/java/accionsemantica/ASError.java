package accionsemantica;


import lexer.TablaSimbolos;

public class ASError extends AccionSemantica {


    public ASError(TablaSimbolos t) {
        super(t);
    }

    @Override
    public String aplicarAccion(char a, int indice) {

        tablita.setErroresLexicos("Linea " + this.linea + ": El siguiente caracter es invalido-> " + a);
        //System.err.println(s);

        this.indice = indice;

        return null;
    }

}
