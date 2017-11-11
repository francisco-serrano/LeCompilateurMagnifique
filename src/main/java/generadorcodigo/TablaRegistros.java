package generadorcodigo;

/**
 * Clase utilizada para modelar los registros a utilizar para así llevar a cabo el algoritmo
 * del seguimiento de registros. Se trata básicamente de un arreglo en el que se pueden ocupar y
 * liberar registros, pudiendo preguntar además si alguno de los mismos está siendo ocupado o no.
 *
 * @author Bianco Martin, Di Pietro Esteban, Serrano Francisco
 */
public class TablaRegistros {

    private boolean[] registroOcupado;

    /**
     * Constructor de la tabla de registros en el que solamente se instancia el arreglo de registros con
     * la cantidad indicada por parámetro.
     * @param cantidadRegistros Número indicando la cantidad de registros a contener en la tabla.
     */
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

    /**
     * Ocupa el registro indicado por parámetro.
     * @param reg Indice en el arreglo que indica el registro a ocupar.
     */
    public void occupyRegister(int reg) {
        registroOcupado[reg] = true;
    }

    /**
     * Libera el registro indicado por parámetro.
     * @param reg Indice en el arreglo que indica el registro a liberar.
     */
    public void freeRegister(int reg) {
        registroOcupado[reg] = false;
    }

    /**
     * Pregunta si un determinado registro está siendo ocupado.
     * @param reg Indice en el arreglo que indica el registro por el cual se quiere preguntar.
     * @return Booleano indicando si el registro está siendo efectivamente ocupado o no.
     */
    public boolean isOccupied(int reg) {
        return registroOcupado[reg];
    }
}