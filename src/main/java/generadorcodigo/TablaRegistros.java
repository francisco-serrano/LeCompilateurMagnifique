package generadorcodigo;

public class TablaRegistros {

    private boolean[] registroOcupado;

    public TablaRegistros(int cantidadRegistros) {

        // Se inicializan todos en falso
        registroOcupado = new boolean[cantidadRegistros];
    }

    /**
     * Retorna el primer registro vacío que se encuentre. En caso de estar todos ocupados, devuelve una constante con
     * un valor inválido.
     *
     * @return Entero con el índice del registro libre.
     */
    public int getFreeRegister() {
        String aux = "R";
        for (int i = 0; i < registroOcupado.length; i++) {
            if (!registroOcupado[i])
                return i;
        }

        return Integer.MIN_VALUE;
    }

    public void occupyRegister(int reg) {
        registroOcupado[reg] = true;
    }

    public void freeRegister(int reg) {
        registroOcupado[reg] = false;
    }

    public boolean isOccupied(int reg) {
        return registroOcupado[reg];
    }
}