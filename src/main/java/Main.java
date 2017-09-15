
public class Main {

    public static void main(String[] args)
    {
        String fileDir = "archivo-prueba.txt";
        String dirMatrizEstados = "matriz-estados.txt";
        String dirMatrizSemantica = "matriz-acc-semanticas.txt";

        Automata automata = new Automata(fileDir, dirMatrizEstados, dirMatrizSemantica);
        automata.imprimimeLasMatrices();
    }
}
