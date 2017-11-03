package lexer;

import java.util.ArrayList;
import java.util.List;

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

//    private String lexema;
//    private boolean declared;
//    private String type;
//    private String uso;
//    private List<String> ambito = new ArrayList<>();
//
//    /**
//     * Construye un token sin declarar y con tipo sin definir, con el lexema pasado por parámetero.
//     * @param lexema Texto indicando el lexema a contener por el token.
//     */
//    public Token(String lexema) {
//        this.lexema = lexema.toLowerCase();
//        this.declared = false;
//        this.type = "undefined";
//        this.uso = "undefined";
//    }
//
//    /**
//     * Retorna el lexema contenido en el token.
//     * @return Texto indicando el lexema contenido en el token.
//     */
//    public String getLexema() {
//        return lexema;
//    }
//
//    /**
//     * Indica si la variable contenida en el token fue efectivamente declarada o no.
//     * @return Booleano indicando si la variable fue declarada.
//     */
//    public boolean isDeclared() {
//        return this.declared;
//    }
//
//    /**
//     * Retorna el tipo de la variable contenida en el token.
//     * @return Texto indicando el tipo de variable.
//     */
//    public String getType() {
//        return new String(type);
//    }
//
//    public String getUso() { return this.uso; }
//
//    /**
//     * Levanta el flag para indicar que la variable contenida en el token fue declarada.
//     * @param type Texto indicando el tipo de variable.
//     */
//    public void declare(String type) {
//        this.declared = true;
//        this.type = type;
//    }
//
//    public void setUso(String usillo) {
//        this.uso = usillo;
//    }
//
//    public void setAmbito(String a) { ambito.add(a); }
//
//    public void borrarAmbito() { this.ambito.clear(); }
//
//    public List<String> getAmbito() { return this.ambito; }
//
//    public List<String> getAmbitoEspecial() {
//        List<String> aux = new ArrayList<>();
//        for (int i = 0; i < ambito.size(); i++) {
//            aux.add(ambito.get(i));
//        }
//        return aux;
//    }
//
//    /**
//     * Retorna una representación en forma de texto del token.
//     * @return Texto representando el token.
//     */
//    @Override
//    public String toString() {
//        return "Token{" +
//                "lexema='" + lexema + '\'' +
//                ", uso=" + uso +
//                ", type='" + type + '\'' +
//                ", ambito='" + ambito.toString() + '\'' +
//                '}';
//    }
//
//    /**
//     * Indica si otro objeto es igual o no al token del cuál se llama al método.
//     * @param o Objeto con el cual comparar el token.
//     * @return Booleano indicando si el token es igual al objeto pasado por parámetro.
//     */
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Token token = (Token) o;
//
//        return lexema != null ? lexema.equals(token.lexema) : token.lexema == null;
//    }
//
//    /**
//     * Retorna un valor hash para el token. El método es redefinido para soportar el uso de Tokens en estructuras de
//     * clave valor.
//     * @return Valor hash para el token.
//     */
//    @Override
//    public int hashCode() {
//        return lexema != null ? lexema.hashCode() : 0;
//    }
}
