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
    private static final int COLUMNAS = 24;

    private List<Integer> archivo = new ArrayList<>();
    private Map<Character, Integer> mapeoColumna = new HashMap<>();
    private int[][] matrizEstados = new int[FILAS][COLUMNAS];

    public Automata(String fileDir, String matrixDir)
    {
        // [a-zA-Z]
        // [0-9]

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
        generateMatrix(matrixDir);
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
            while ((aux = fr.read()) != -1)
                archivo.add(aux);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateMatrix(String matrixDir)
    {
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

        List<String> matriz = Splitter.on(',').splitToList(aux.toString());

        for (int i = 0; i < FILAS; i++)
            for (int j = 0; j < COLUMNAS; j++)
                matrizEstados[i][j] = Integer.parseInt(matriz.remove(0));

    }
}
