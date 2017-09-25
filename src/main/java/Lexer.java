import accionsemantica.*;
import com.google.common.base.Splitter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Lexer {

    // _	[	]	<	>	=	(	)	{	}	:	.	 '	,	+	-	*	/	BL SL TAB
    private static final List<Integer> CARACTERES_RECONOCIDOS = Arrays.asList(95, 91, 93, 60, 62, 61, 40, 41, 123, 125, 58, 46, 39, 44, 43, 45, 42, 47, 32, 9);
    private static final int FILAS = 8;
    private static final int COLUMNAS = 23;

    private Map<String, Integer> tiposToken = new HashMap<>();

    private enum tipo_matriz {MATRIZ_ESTADOS, MATRIZ_ACCIONES_SEMANTICAS}

    private TablaSimbolos ts = new TablaSimbolos();
    private List<Character> archivo = new ArrayList<>();
    private List<Character> noConvertida = new ArrayList<>();
    private Map<Character, Integer> mapeoColumna = new HashMap<>();
    private int[][] matrizEstados = new int[FILAS][COLUMNAS];
    private int[][] matrizAccionesSemanticas = new int[FILAS][COLUMNAS];

    private int cantidadLineas = 1;
    private int lineaactual = 1;
    private int estadoActual = 0;
    private int idAccSemantica = 0;
    private int estadoAnterior = 0;

    private List<Integer> listaTokens = new ArrayList<>();

    public Lexer(String fileDir, String fileDir_matEstados, String fileDir_matSemantica) {

        // Leo el archivo y genero la lista de chars
        readFile(fileDir);

        // Genero las matrices a partir de los archivos de texto leídos
        buildMatrix(fileDir_matEstados, tipo_matriz.MATRIZ_ESTADOS);
        buildMatrix(fileDir_matSemantica, tipo_matriz.MATRIZ_ACCIONES_SEMANTICAS);

        // Mapeo a cada char con una columna de la matriz
        buildMapeoColumna();

        // Mapeo cada tipo de token con un identifidor numérico (así queda como en las filminas)
        assignTokenIds();

        List<String> listaTokensAux = new ArrayList<>();

        for (int i = 0; i < archivo.size(); i++) {
            // Obtengo caracter
            char caracter_actual = archivo.get(i);

            if (caracter_actual != '¶') {
                // Obtengo columna correspondiente al caracter
                Integer columna = mapeoColumna.get(caracter_actual);

                // Acceso a matrices
                estadoAnterior = estadoActual;
                estadoActual = matrizEstados[estadoActual][columna];
                idAccSemantica = matrizAccionesSemanticas[estadoAnterior][columna];

                if (noConvertida.get(i) == 10 && archivo.get(i) == 'E')
                    lineaactual++;

                if (estadoActual == -2 && idAccSemantica == 0) {
                    //devuelvo el caracter derecho
//                    System.out.println(noConvertida.get(i));
                    listaTokensAux.add("->" + noConvertida.get(i).toString());
                    estadoActual = 0;
                    estadoAnterior = 0;
                }

                if (idAccSemantica != 0) {
                    AccionSemantica accionSemantica = getAccion(idAccSemantica);

                    // Usar éstos dos métodos para contemplar todos los casos, en AccionSemantica está explicado

                    String resultado = accionSemantica.aplicarAccion(noConvertida.get(i), i);

                    if (resultado != null)
                        listaTokensAux.add(resultado);

                    i = accionSemantica.getIndice();
                    if (idAccSemantica == 3 || idAccSemantica == 6 || idAccSemantica == 9 || idAccSemantica == 11 || idAccSemantica == 16) {
                        estadoActual = 0;
                        estadoAnterior = 0;

                        if (caracter_actual == 'E' && idAccSemantica != 9)
                            lineaactual--;

                    }
                }

            } else {
                if (estadoActual == 6 || estadoActual == 5)
                    System.out.println("WARNING: fin de archivo con comentario/cadena abierto/a");
            }
        }

        formatTokens(listaTokensAux);

    }

    public int yylex() {
        try {
            return listaTokens.remove(0);
        } catch (IndexOutOfBoundsException e) {
            return 0; // Indico EOF
        }
    }

    public void yyerror(String error) {
        System.err.println(error);
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

    public String getTipoToken(int id) {
        for (String key : tiposToken.keySet())
            if (tiposToken.get(key) == id)
                return key;

        return null;
    }

    private void formatTokens(List<String> listaTokensAux) {
        List<String> listaTokensPosta = new ArrayList<>();

        for (String token : listaTokensAux) {
            List<String> aux = Splitter.on("->").splitToList(token);

            String izq = aux.get(0), der = aux.get(1);

            if (izq.equals("PALABRA RESERVADA"))
                listaTokensPosta.add(der);

            if (izq.equals("ID") || izq.equals("CTE") || izq.equals("CADENA"))
                listaTokensPosta.add(izq);

            if (izq.length() == 0)
                listaTokensPosta.add(der);
        }

        for (String tokenPosta : listaTokensPosta)
            listaTokens.add(tiposToken.get(tokenPosta));
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

        // Para que coincida con yacc
        tiposToken.put("ID", 257);
        tiposToken.put("CTE", 258);
        tiposToken.put("=", 259);
        tiposToken.put("+", 260);
        tiposToken.put("-", 261);
        tiposToken.put("*", 262);
        tiposToken.put("/", 263);
        tiposToken.put(".", 264);
        tiposToken.put("BEGIN", 265);
        tiposToken.put("END", 266);
        tiposToken.put(":", 267);
        tiposToken.put(",", 268);
        tiposToken.put("UINT", 269);
        tiposToken.put("ULONG", 270);
        tiposToken.put("IF", 271);
        tiposToken.put("(", 272);
        tiposToken.put(")", 273);
        tiposToken.put("THEN", 274);
        tiposToken.put("ELSE", 275);
        tiposToken.put("END_IF", 276);
        tiposToken.put("<=", 277);
        tiposToken.put(">=", 278);
        tiposToken.put("<", 279);
        tiposToken.put(">", 280);
        tiposToken.put("==", 281);
        tiposToken.put("<>", 282);
        tiposToken.put("OUT", 283);
        tiposToken.put("CADENA", 284);
        tiposToken.put("FUNCTION", 285);
        tiposToken.put("MOVE", 286);
        tiposToken.put("{", 287);
        tiposToken.put("}", 288);
        tiposToken.put("RETURN", 289);




//        // Identificadores y constantes
//        tiposToken.put("ID", 10);
//        tiposToken.put("CTE", 11);
//
//        // Operadores aritméticos
//        tiposToken.put("+", 20);
//        tiposToken.put("-", 21);
//        tiposToken.put("*", 22);
//        tiposToken.put("/", 23);
//
//        // Operadores de asignación
//        tiposToken.put("=", 30);
//
//        // Operadores de comparación
//        tiposToken.put(">=", 40);
//        tiposToken.put("<=", 41);
//        tiposToken.put(">", 42);
//        tiposToken.put("<", 43);
//        tiposToken.put("==", 44);
//        tiposToken.put("<>", 45);
//
//        // Otros
//        tiposToken.put("(", 50);
//        tiposToken.put(")", 51);
//        tiposToken.put(",", 52);
//        tiposToken.put(":", 53);
//        tiposToken.put(".", 54);
//        tiposToken.put("BL", 55);
//        tiposToken.put("SL", 56);
//        tiposToken.put("TAB", 57);
//        tiposToken.put("{", 58);
//        tiposToken.put("}", 59);
//
//        // Palabras reservadas
//        tiposToken.put("IF", 60);
//        tiposToken.put("THEN", 61);
//        tiposToken.put("ELSE", 62);
//        tiposToken.put("END_IF", 63);
//        tiposToken.put("BEGIN", 64);
//        tiposToken.put("END", 65);
//        tiposToken.put("OUT", 66);
//        tiposToken.put("CADENA", 67);
//
//        // Palabras reservadas (específicas del grupo)
//        tiposToken.put("WHILE", 70);
//        tiposToken.put("DO", 71);
//        tiposToken.put("FUNCTION", 72);
//        tiposToken.put("RETURN", 73);
//        tiposToken.put("MOVE", 74);
//        tiposToken.put("UINT", 75);
//        tiposToken.put("ULONG", 76);
//
//        // Comentarios multilínea
//        tiposToken.put("[", 80);
//        tiposToken.put("]", 81);
//
//        // Cadenas monolínea
//        tiposToken.put("'", 90);
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
                if (aux != 13) {
                    archivo.add(getId(aux));
                    noConvertida.add((char) aux);
                }
            }
            archivo.add(getId(32));
            noConvertida.add((char) 32);
            archivo.add('¶');
            noConvertida.add('¶');
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

        if (valor == 10)
            return 'E';


        // Reconoce los caracteres especificados en el excel
        if (CARACTERES_RECONOCIDOS.contains(valor))
            return (char) valor;

        // Caracter no reconocido -> Retorna "cualquier cosa" --> 'C'
        return 'C';
    }

    public TablaSimbolos getTablaSimbolos() {
        return ts;
    }

    private AccionSemantica getAccion(int id) {
        AccionSemantica AS1 = new AS1(ts);
        AccionSemantica AS2 = new AS2(ts);
        AccionSemantica AS3 = new AS3(ts);
        AccionSemantica AS6 = new AS6(ts);
        AccionSemantica AS9 = new AS9(ts);
        AccionSemantica AS11 = new AS11(ts);
        AccionSemantica AS16 = new AS16(ts);
        AccionSemantica ASError = new ASError(ts);
        AccionSemantica a;

        switch (id) {
            case 1:
                a = AS1;
                break;
            case 2:
                a = AS2;
                break;
            case 3:
                AS3.setearlinea(lineaactual);
                a = AS3;
                break;
            case 6:
                AS6.setearlinea(lineaactual);
                a = AS6;
                break;
            case 9:
                a = AS9;
                break;
            case 11:
                a = AS11;
                break;
            case 16:
                a = AS16;
                break;
            case -1:
                estadoActual = 0;
                estadoAnterior = 0;
                ASError.setearlinea(lineaactual);
                a = ASError;
                break;
            default:
                throw new IllegalArgumentException("ID INVALIDO");
        }
        return a;
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
        mapeoColumna.put('\t', 20);
        mapeoColumna.put('E', 21); //enter
        mapeoColumna.put('C', 22);
    }
}
