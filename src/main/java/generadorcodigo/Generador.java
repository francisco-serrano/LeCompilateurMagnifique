package generadorcodigo;

public class Generador {

    private static final int CANTIDAD_REGISTROS = 4;

    // Inicializo la tabla con todos en falso, o sea arrancan todos libres
    private boolean[] registro_ocupado = new boolean[CANTIDAD_REGISTROS];

    public Generador() {
        for (boolean reg : registro_ocupado)
            System.out.print(reg + " ");
    }
}
