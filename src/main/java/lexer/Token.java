package lexer;

/**
 * Clase que representa al elemento que contiene los lexemas dentro de una tabla de símbolos. En caso de que el lexema
 * pertenezca a un token de tipo identificador, tendrán relevancia los campos "declared" y "type", para posteriormente
 * realizar chequeos propios del análisis sintáctico.
 *
 * @author Bianco Martín, Di Pietro Esteban, Serrano Francisco
 */
public class Token {

    private String lexema;
    private boolean declared;
    private String type;
    private String uso;
    private String ambito;

    public Token(String lexema) {
        this.lexema = lexema;
        this.declared = false;
        this.type = "undefined";
        this.uso = "undefined";
        this.ambito = "undefined";
    }

    public void declare(String type) {
        this.declared = true;
        this.type = type;
    }

    public void setDeclared(){ this.declared = true; }

    public boolean isDeclared(){
        return declared;
    }

    public String getLexema() {
        return lexema;
    }

    public String getType() {
        return type;
    }

    public String getUso() {
        return uso;
    }

    public void setUso(String uso) {
        this.uso = uso;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    @Override
    public String toString() {
        return "Token{" +
                "lexema='" + lexema + '\'' +
                ", declared=" + declared +
                ", type='" + type + '\'' +
                ", uso='" + uso + '\'' +
                ", ambito='" + ambito + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        if (declared != token.declared) return false;
        if (!lexema.equals(token.lexema)) return false;
        if (!type.equals(token.type)) return false;
        if (!uso.equals(token.uso)) return false;
        return ambito.equals(token.ambito);
    }

    @Override
    public int hashCode() {
        int result = lexema.hashCode();
        result = 31 * result + (declared ? 1 : 0);
        result = 31 * result + type.hashCode();
        result = 31 * result + uso.hashCode();
        result = 31 * result + ambito.hashCode();
        return result;
    }
}
