package accionsemantica;

public class Token {

    private String lexema;
    private boolean defined;

    public Token(String lexema) {
        this.lexema = lexema;
        this.defined = false;
    }

    public String getLexema() {
        return lexema;
    }

    public boolean isDefined() {
        return this.defined;
    }

    public void define() {
        this.defined = true;
    }

    @Override
    public String toString() {
        return "Token{" +
                "lexema='" + lexema + '\'' +
                ", defined=" + defined +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        return lexema != null ? lexema.equals(token.lexema) : token.lexema == null;
    }

    @Override
    public int hashCode() {
        return lexema != null ? lexema.hashCode() : 0;
    }
}
