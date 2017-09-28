package accionsemantica;

public class Token {

    private String lexema;
    private boolean defined;
    private String type;

    public Token(String lexema) {
        this.lexema = lexema;
        this.defined = false;
        this.type = "undefined";
    }

    public String getLexema() {
        return lexema;
    }

    public boolean isDefined() {
        return this.defined;
    }

    public String getType() {
        return this.type;
    }

    public void define(String type) {
        this.defined = true;
        this.type = type;
    }


    @Override
    public String toString() {
        return "Token{" +
                "lexema='" + lexema + '\'' +
                ", defined=" + defined +
                ", type='" + type + '\'' +
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
