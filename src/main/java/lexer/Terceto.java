package lexer;

public class Terceto {

    public static int total = 0; //cantidad de tercetos totales
    private String operador;
    private Item arg1, arg2;
    private int num; //numero del terceto en particular
    private String tipo;
    private boolean isWhile = false;
    private boolean isDireccionSalto = false;

    private String associatedRegister;


    public Terceto(String operador, Item arg1, Item arg2, String tipo) {
        this.operador = operador;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.total++;
        this.num = this.total;
        this.tipo = tipo;
    }

    public int getNumero() {
        return num;
    }

    public void setNumero(int num) {
        this.num = num;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public Item getArg1() {
        return arg1;
    }

    public void setArg1(Item arg1) {
        this.arg1 = arg1;
    }

    public Item getArg2() {
        return arg2;
    }

    public void setArg2(Item arg2) {
        this.arg2 = arg2;
    }

    public void setDireccionSalto() { this.isDireccionSalto = true; }

    public boolean getDireccionSalto() { return isDireccionSalto; }

    @Override
    public String toString() {
        return this.getNumero() + ".  " + "( " + this.operador + " , " + this.arg1.toString() + " , " + this.arg2.toString() + " ) ";
    }

    public String getTipo(){
        return this.tipo;
    }

    public void setTipo (String tipo){
        this.tipo=tipo;
    }

    public void setIsWhileFlag(boolean flag){
        this.isWhile = flag;
    }

    public boolean getIsWhileFlag(){
        return this.isWhile;
    }

    public void addRegister(String reg) {
        this.associatedRegister = reg;
    }

}

