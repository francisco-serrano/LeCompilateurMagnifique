package lexer;

public class itemString implements Item{
    private String arg;
    private TablaSimbolos tabla=null;

    public void setTabla(TablaSimbolos tabla){
        this.tabla=tabla;
    }

    public itemString(String arg) {
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

    public itemString toItemString() {
        return this;
    }

    public String getTipo(){
      //hay que acceder a la tabla de simbolos, ver si esta el arg (puede ser constante o variable) y despues sacarle el tipo del token
        Token aux = tabla.devolverToken(this.arg);

        return aux.getType();
    }
}
