package lexer;


public class itemTerceto implements Item{
    private Terceto arg;

    public itemTerceto(Terceto arg) {
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
        return "["+String.valueOf(this.arg.getNumero())+"]";
    }

    public boolean equals(String s) {
        return false;
    }

    public itemString toItemString() {
        return new itemString("[T " + arg.getNumero() + "]");
    }

    public String getTipo (){
        return this.getArg().getTipo();
    }



}
