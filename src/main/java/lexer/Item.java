package lexer;


public interface Item {
    public boolean equals(String s);
    public String toString();
    public itemString toItemString();
    public String getTipo();

}
