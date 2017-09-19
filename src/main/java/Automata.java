import accionsemantica.AccionSemantica;
import com.google.common.base.Splitter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Automata {

    private static final int FILAS = 8;
    private static final int COLUMNAS = 22;

    private enum tipo_matriz {MATRIZ_ESTADOS, MATRIZ_ACCIONES_SEMANTICAS}

    private List<Character> archivo = new ArrayList<>();
    private Map<Character, Integer> mapeoColumna = new HashMap<>();
    private int[][] matrizEstados = new int[FILAS][COLUMNAS];
    private int[][] matrizAccionesSemanticas = new int[FILAS][COLUMNAS];

    private int cantidadLineas = 0;
    private int estadoActual = 0;
    private int idAccSemantica = 0;

    public Automata(String fileDir, String dir_matEstados, String dir_matSemantica) {

        mapeoColumna.put('L', 0);
        mapeoColumna.put('D', 1);
        mapeoColumna.put('_', 2);
        mapeoColumna.put('[', 3);
        mapeoColumna.put(']', 4);
        mapeoColumna.put('<', 5);
        mapeoColumna.put('>', 6);
        mapeoColumna.put('=', 7);
        mapeoColumna.put('(', 8);
        mapeoColumna.put(')', 9);
        mapeoColumna.put('{', 10);
        mapeoColumna.put('}', 11);
        mapeoColumna.put(':', 12);
        mapeoColumna.put('.', 13);
        mapeoColumna.put('\'', 14);
        mapeoColumna.put(',', 15);
        mapeoColumna.put('+', 16);
        mapeoColumna.put('-', 17);
        mapeoColumna.put('*', 18);
        mapeoColumna.put('/', 19);
        mapeoColumna.put(' ', 20);
        mapeoColumna.put('\n', 21);
        mapeoColumna.put('\t', 22);

        readFile(fileDir);
        generateMatrix(dir_matEstados, tipo_matriz.MATRIZ_ESTADOS);
        generateMatrix(dir_matSemantica, tipo_matriz.MATRIZ_ACCIONES_SEMANTICAS);

        for (int i = 0; i < archivo.size(); i++) {
            Integer columna = mapeoColumna.get(archivo.get(i));

            if (columna != null) {
                estadoActual = matrizEstados[estadoActual][columna];
                idAccSemantica = matrizAccionesSemanticas[estadoActual][columna];

                AccionSemantica accionSemantica = getAccion(idAccSemantica);

                continue;
            }

            System.out.println("ERROR -> tu vieja en tanga"); // ACCION SEMANTICA ERROR
        }


    }

    private void readFile(String dir) {
        FileReader fr = null;
        try {
            fr = new FileReader(dir);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int aux;
        try {
            while ((aux = fr.read()) != -1) {
                if (aux == 10)
                    cantidadLineas++;

                if (aux != 13)
                    archivo.add(getId(aux));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateMatrix(String matrixDir, tipo_matriz tipoMatriz) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(matrixDir));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        StringBuilder aux = new StringBuilder();
        String line;
        try {
            while ((line = in.readLine()) != null)
                aux.append(line);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //       System.out.println(aux);

        List<String> matriz = new ArrayList<>(Splitter.on(',').splitToList(aux.toString()));

        for (int i = 0; i < FILAS; i++)
            for (int j = 0; j < COLUMNAS; j++) {
                if (tipoMatriz == tipo_matriz.MATRIZ_ESTADOS)
                    matrizEstados[i][j] = Integer.parseInt(matriz.remove(0));

                if (tipoMatriz == tipo_matriz.MATRIZ_ACCIONES_SEMANTICAS)
                    matrizAccionesSemanticas[i][j] = Integer.parseInt(matriz.remove(0));
            }

    }

    public void imprimeLasMatrices() {

        System.out.println("matriz de estados: ");
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                System.out.print(" " + matrizEstados[i][j]);
            }
            System.out.println();
        }

        System.out.println("matriz de acciones semanticas");
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                System.out.print(" " + matrizAccionesSemanticas[i][j]);
            }
            System.out.println();
        }

    }

    public List<Character> getArchivo() {
        return this.archivo;
    }

    private char getId(int valor) {
        if ((valor >= 65 && valor <= 90) || (valor >= 97 && valor <= 122))
            return 'L';

        if (valor >= 48 && valor <= 57)
            return 'D';

        return (char) valor;
    }

    private AccionSemantica getAccion(int id) {
        switch (id) {
            default:
                throw new IllegalArgumentException("ID INVALIDO");
        }
    }
}
