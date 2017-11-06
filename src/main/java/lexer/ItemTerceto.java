package lexer;


public class ItemTerceto implements Item {
    private Terceto arg;

    public ItemTerceto(Terceto arg) {
        this.arg = arg;
    }

    public Terceto getArg() {
        return arg;
    }

    public void setArg(Terceto arg) {
        this.arg = arg;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "[" + String.valueOf(this.arg.getNumero()) + "]";
    }

    public boolean equals(String s) {
        return false;
    }

    public ItemString toItemString() {
        return new ItemString("[" + arg.getNumero() + "]");
    }

    public String getTipo() {
        return this.getArg().getTipo();
    }
}
