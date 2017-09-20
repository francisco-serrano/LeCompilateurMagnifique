import accionsemantica.AccionSemantica;
import com.google.common.base.Splitter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class AnalizadorLexico {

    // _	[	]	<	>	=	(	)	{	}	:	.	 '	,	+	-	*	/	BL SL TAB
    private static final List<Integer> CARACTERES_RECONOCIDOS = Arrays.asList(95, 91, 93, 60, 62, 61, 40, 41, 123, 125, 58, 46, 39, 44, 43, 45, 42, 47, 32, 10, 9);
    private static final int FILAS = 8;
    private static final int COLUMNAS = 22;

    private Map<String, Integer> tiposToken = new HashMap<>();

    private enum tipo_matriz {MATRIZ_ESTADOS, MATRIZ_ACCIONES_SEMANTICAS}

    private List<Character> archivo = new ArrayList<>();
    private Map<Character, Integer> mapeoColumna = new HashMap<>();
    private int[][] matrizEstados = new int[FILAS][COLUMNAS];
    private int[][] matrizAccionesSemanticas = new int[FILAS][COLUMNAS];

    private int cantidadLineas = 0;
    private int estadoActual = 0;
    private int idAccSemantica = 0;

    private List<String> listaTokens = new ArrayList<>();

    public AnalizadorLexico(String fileDir, String fileDir_matEstados, String fileDir_matSemantica) {

        // Leo el archivo y genero la lista de chars
        readFile(fileDir);

        // Genero las matrices a partir de los archivos de texto leídos
        buildMatrix(fileDir_matEstados, tipo_matriz.MATRIZ_ESTADOS);
        buildMatrix(fileDir_matSemantica, tipo_matriz.MATRIZ_ACCIONES_SEMANTICAS);

        // Mapeo a cada char con una columna de la matriz
        buildMapeoColumna();

        // Mapeo cada tipo de token con un identifidor numérico (así queda como en las filminas)
        assignTokenIds();

        for (int i = 0; i < archivo.size(); i++) {
            // Obtengo caracter
            char caracter_actual = archivo.get(i);

            // Obtengo columna correspondiente al caracter
            Integer columna = mapeoColumna.get(caracter_actual);

            // Acceso a matrices
            estadoActual = matrizEstados[estadoActual][columna];
            idAccSemantica = matrizAccionesSemanticas[estadoActual][columna];

            if (estadoActual == -1)
                System.out.println("ERROR");

            if (estadoActual == -2)
                System.out.println("ESTADO FINAL");

            AccionSemantica accionSemantica = getAccion(idAccSemantica);

            // Usar éstos dos métodos para contemplar todos los casos, en AccionSemantica está explicado
            accionSemantica.aplicarAccion(caracter_actual, i);
            i = accionSemantica.getIndice();
        }
    }

    // TODO: PROVISORIO, revisarlo al final
    public String getToken() {

        /*
            Voy removiendo los tokens de la lista así se van consumiendo

            Sí el índice es inválido, se captura la excepción que lance el remove, y devuelvo null, así le indico
            al analizador sintáctico que ya no quedan tokens por consumir
         */
        try {
            return listaTokens.remove(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public void printMatrices() {

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

    private void assignTokenIds() {
        /*
            Es una crotada, pero por ahora dejarlo así

            La lógica para poner el número que mapea a cada tipo de token es que
            el primer dígito de los dos identifique al grupo de tipos de token
            ejemplo: 21 --> es el menos, el 2 indica que es un operador aritmético

            Este mapa después lo usamos para que cuando obtengamos el token,
            convirtamos el tipoToken, que es un String, a un Integer, para que
            quede como en las filminas
         */

        // Identificadores y constantes
        tiposToken.put("ID", 10);
        tiposToken.put("CTE", 11);

        // Operadores aritméticos
        tiposToken.put("+", 20);
        tiposToken.put("-", 21);
        tiposToken.put("*", 22);
        tiposToken.put("/", 23);

        // Operadores de asignación
        tiposToken.put("=", 30);

        // Operadores de comparación
        tiposToken.put(">=", 40);
        tiposToken.put("<=", 41);
        tiposToken.put(">", 42);
        tiposToken.put("<", 43);
        tiposToken.put("==", 44);
        tiposToken.put("<>", 45);

        // Otros
        tiposToken.put("(", 50);
        tiposToken.put(")", 51);
        tiposToken.put(",", 52);
        tiposToken.put(":", 53);
        tiposToken.put(".", 54);
        tiposToken.put("BL", 55);
        tiposToken.put("SL", 56);
        tiposToken.put("TAB", 57);

        // Palabras reservadas
        tiposToken.put("IF", 60);
        tiposToken.put("THEN", 61);
        tiposToken.put("ELSE", 62);
        tiposToken.put("END_IF", 63);
        tiposToken.put("BEGIN", 64);
        tiposToken.put("END", 65);
        tiposToken.put("OUT", 66);

        // Palabras reservadas (específicas del grupo)
        tiposToken.put("WHILE", 70);
        tiposToken.put("DO", 71);
        tiposToken.put("FUNCTION", 72);
        tiposToken.put("RETURN", 73);
        tiposToken.put("MOVE", 74);

        // Comentarios multilínea
        tiposToken.put("[", 80);
        tiposToken.put("]", 81);

        // Cadenas monolínea
        tiposToken.put("'", 90);
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

    private void buildMatrix(String matrixDir, tipo_matriz tipoMatriz) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(matrixDir));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String line;
        StringBuilder aux = new StringBuilder();
        try {
            while ((line = in.readLine()) != null)
                aux.append(line);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> matriz = new ArrayList<>(Splitter.on(',').splitToList(aux.toString()));

        for (int i = 0; i < FILAS; i++)
            for (int j = 0; j < COLUMNAS; j++) {
                if (tipoMatriz == tipo_matriz.MATRIZ_ESTADOS)
                    matrizEstados[i][j] = Integer.parseInt(matriz.remove(0));

                if (tipoMatriz == tipo_matriz.MATRIZ_ACCIONES_SEMANTICAS)
                    matrizAccionesSemanticas[i][j] = Integer.parseInt(matriz.remove(0));
            }

    }

    private char getId(int valor) {
        // Reconoce letra
        if ((valor >= 65 && valor <= 90) || (valor >= 97 && valor <= 122))
            return 'L';

        // Reconoce dígito
        if (valor >= 48 && valor <= 57)
            return 'D';

        // Reconoce los caracteres especificados en el excel
        if (CARACTERES_RECONOCIDOS.contains(valor))
            return (char) valor;

        // Caracter no reconocido -> Retorna "cualquier cosa" --> 'C'
        return 'C';
    }

    private AccionSemantica getAccion(int id) {
        switch (id) {
            case 1:
                // return new AS1(....)
                System.out.println("ACCION SEMANTICA 1");
                break;
            case 2:
                System.out.println("ACCION SEMANTICA 2");
                break;
            case 3:
                System.out.println("ACCION SEMANTICA 3");
                break;
            case 4:
                System.out.println("ACCION SEMANTICA 4");
                break;
            case 5:
                System.out.println("ACCION SEMANTICA 5");
                break;
            case 6:
                System.out.println("ACCION SEMANTICA 6");
                break;
            case 7:
                System.out.println("ACCION SEMANTICA 7");
                break;
            case 8:
                System.out.println("ACCION SEMANTICA 8");
                break;
            case 9:
                System.out.println("ACCION SEMANTICA 9");
                break;
            case 10:
                System.out.println("ACCION SEMANTICA 10");
                break;
            case 11:
                System.out.println("ACCION SEMANTICA 11");
                break;
            case 12:
                System.out.println("ACCION SEMANTICA 12");
                break;
            case 13:
                System.out.println("ACCION SEMANTICA 13");
                break;
            case 14:
                System.out.println("ACCION SEMANTICA 14");
                break;
            case 15:
                System.out.println("ACCION SEMANTICA 15");
                break;
            case -1:
                System.out.println("ERROR");
                break;
            default:
                throw new IllegalArgumentException("ID INVALIDO");
        }

        return null; // provisorio para que el compilador no se queje mientras tanto
    }

    private void buildMapeoColumna() {
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
        mapeoColumna.put('\n', 20);
        mapeoColumna.put('\t', 20);
        mapeoColumna.put('C', 21);
    }
}
