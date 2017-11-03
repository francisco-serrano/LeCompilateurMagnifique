package lexer;

import java.util.ArrayList;
import java.util.List;

public class ItemString implements Item {
    private String arg;
    private TablaSimbolos tabla = null;

    public void setTabla(TablaSimbolos tabla) {
        this.tabla = tabla;
    }

    public ItemString(String arg) {
        this.arg = arg;
    }

    public String getArg() {
        return arg;
    }

    public void setArg(String arg) {
        this.arg = arg;
    }

    @Override
    public String toString() {
        return arg.toString();
    }

    public boolean equals(String obj) {
        return arg.equals(obj);
    }

    public ItemString toItemString() {
        return this;
    }

    public String getTipo() {
        //hay que acceder a la tabla de simbolos, ver si esta el arg (puede ser constante o variable o funcion) y despues sacarle el tipo del token
        String s = this.arg;
        String[] parts = s.split("@");
        String part1 = parts[0];

        List<Token> aux = new ArrayList<>();
        aux = tabla.getTokenList(part1);
        for (int i = 0; i < aux.size(); i++) {
            if (aux.get(i).isDeclared())
                return aux.get(i).getType();
        }

        return "undefined";
    }
}
