package lexer;

public interface Item {
    boolean equals(String s);

    String toString();

    ItemString toItemString();

    String getTipo();

}
