package lexer;

import accionsemantica.*;
import com.google.common.base.Splitter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Clase que representa al analizador léxico del compilador. Está compuesto básicamente por una matriz de estados, una
 * matriz de acciones semánticas, y una tabla de símbolos.
 *
 * El analizador léxico (Lexer) se encarga de reconocer los tokens en un determinado archivo de texto. Dichos tokens
 * son reconocidos y puestos a disposición de un analizador sintáctico (Parser) a medida que les son solicitados al
 * Lexer a través del método yylex().
 *
 * El Lexer, a medida que va reconociendo tokens, va guardando información acerca de los mismos en la tabla de
 * símbolos, que también es utilizada posteriormente por el Parser.
 *
 * @author Bianco Martín, Di Pietro Esteban, Francisco Serrano
 */
public class Lexer {

    // _	[	]	<	>	=	(	)	{	}	:	.	 '	,	+	-	*	/	BL SL TAB
    private static final List<Integer> CARACTERES_RECONOCIDOS = Arrays.asList(95, 91, 93, 60, 62, 61, 40, 41, 123, 125, 58, 46, 39, 44, 43, 45, 42, 47, 32, 9);
    private static final int FILAS = 8;
    private static final int COLUMNAS = 23;

    private Map<String, Integer> tiposToken = new HashMap<>();

    private enum tipo_matriz {MATRIZ_ESTADOS, MATRIZ_ACCIONES_SEMANTICAS}

    private TablaSimbolos tablaSimbolos;
    private List<Character> archivo = new ArrayList<>();
    private List<Character> noConvertida = new ArrayList<>();
    private Map<Character, Integer> mapeoColumna = new HashMap<>();
    private int[][] matrizEstados = new int[FILAS][COLUMNAS];
    private int[][] matrizAccionesSemanticas = new int[FILAS][COLUMNAS];

    private int currentLine = 1;
    private String currentLexema;

    private int estadoActual = 0;
    private int idAccSemantica = 0;
    private int estadoAnterior = 0;
    private int indice = 0;

    private List<Integer> listaTokens = new ArrayList<>();

    /**
     * Construye el analizador léxico a partir de tres directorios: uno del archivo a compilar, y los otros dos
     * correspondientes al contenido de la matriz de estados y de la matriz de acciones semánticas. Adicionalmente se
     * provee una tabla de símbolos en donde almacenar los tokens que se van leyendo
     * @param fileDir Directorio del archivo a compilar
     * @param fileDir_matEstados Directorio del archivo que representa a la matriz de estados
     * @param fileDir_matSemantica Directorio del archivo que representa a la matriz de acciones semánticas
     * @param tablaSimbolos Tabla de símbolos en donde guardar los tokens leídos
     * @throws FileNotFoundException En caso de que no se encuentre/n alguno/s de los archivos de texto necesarios
     */
    public Lexer(String fileDir, String fileDir_matEstados, String fileDir_matSemantica, TablaSimbolos tablaSimbolos) throws FileNotFoundException {
        this.tablaSimbolos = tablaSimbolos;

        // Leo el archivo y genero la lista de chars
        readFile(fileDir);

        // Genero las matrices a partir de los archivos de texto leídos
        buildMatrix(fileDir_matEstados, tipo_matriz.MATRIZ_ESTADOS);
        buildMatrix(fileDir_matSemantica, tipo_matriz.MATRIZ_ACCIONES_SEMANTICAS);

        // Mapeo a cada char con una columna de la matriz
        buildMapeoColumna();

        // Mapeo cada tipo de token con un identifidor numérico (así queda como en las filminas)
        assignTokenIds();
    }

    /**
     * TODO: completarlo
     */
    private void readTokens() {

        List<String> listaTokensAux = new ArrayList<>();


        while ((indice < archivo.size()) && (listaTokensAux.size() == 0)) {
            // Obtengo caracter
            char caracter_actual = archivo.get(indice);

            if (caracter_actual != '¶') {
                // Obtengo columna correspondiente al caracter
                Integer columna = mapeoColumna.get(caracter_actual);

                // Acceso a matrices
                estadoAnterior = estadoActual;
                estadoActual = matrizEstados[estadoActual][columna];
                idAccSemantica = matrizAccionesSemanticas[estadoAnterior][columna];

                if (noConvertida.get(indice) == 10 && archivo.get(indice) == 'E')
                    currentLine++;

                if (estadoActual == -2 && idAccSemantica == 0) {
                    listaTokensAux.add("->" + noConvertida.get(indice).toString());
                    estadoActual = 0;
                    estadoAnterior = 0;
                }

                if (idAccSemantica != 0) {
                    AccionSemantica accionSemantica = getAccion(idAccSemantica);

                    // Usar éstos dos métodos para contemplar todos los casos, en AccionSemantica está explicado

                    String resultado = accionSemantica.aplicarAccion(noConvertida.get(indice), indice);

                    if (resultado != null) {
                        listaTokensAux.add(resultado);
                        currentLexema = accionSemantica.getToken();
                    }

                    indice = accionSemantica.getIndice();
                    if (idAccSemantica == 3 || idAccSemantica == 6 || idAccSemantica == 9 || idAccSemantica == 11 || idAccSemantica == 16) {
                        estadoActual = 0;
                        estadoAnterior = 0;

                        if (caracter_actual == 'E' && idAccSemantica != 9)
                            currentLine--;

                    }
                }

            } else {

                if (estadoActual == 6 || estadoActual == 5)
                    System.out.println("WARNING: fin de archivo con comentario/cadena abierto/a");
            }
            indice++;
        }

        formatTokens(listaTokensAux);
    }


    /**
     * Consume un token de la lectura del archivo a compilar
     * @return Número entero identificando al tipo de token que se leyó
     */
    public int yylex() {

        readTokens();

        try {
            return listaTokens.remove(0);
        } catch (IndexOutOfBoundsException e) {
            return 0; // Indica EOF
        }
    }

    /**
     * Obtiene la línea del archivo en la que se está parado
     * @return Número indicando la línea del archivo
     */
    public int getCurrentLine() {
        return this.currentLine;
    }

    /**
     * Obtiene el lexema actual, es decir el último que se leyó
     * @return Texto identificando el lexema leído
     */
    public String getCurrentLexema() {
        return currentLexema;
    }

    /**
     * TODO: completarlo
     * @param listaTokensAux
     */
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

    /**
     * Mapea cada tipo de token con un número que sea compatible con el Parser
     */
    private void assignTokenIds() {
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
        tiposToken.put("WHILE", 290);
        tiposToken.put("DO", 291);
    }

    /**
     * Lee el archivo que se quiere compilar para luego guardarlo en una lista de caracteres
     * @param dir Directorio del archivo con el código a compilar
     * @throws FileNotFoundException En caso de no encontrar el directorio especificado
     */
    private void readFile(String dir) throws FileNotFoundException {
        FileReader fr = new FileReader(dir);

        int aux;
        try {
            while ((aux = fr.read()) != -1) {
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

    /**
     * Genera las matrices de estados y de acciones semánticas a partir de archivos de texto
     * @param matrixDir Archivo de texto representando la matriz que se quiere generar
     * @param tipoMatriz Tipo de matriz que se quiere generar (estados/acciones semánticas)
     */
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
            assert in != null;
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

    /**
     * Obtiene el caracter asociado a un determinado número (tanto letras como dígitos son agrupados)
     * @param valor Número entero que identifica a un determinado caracter
     * @return Caracter asociado al entero pasado por parámetro
     */
    private char getId(int valor) {
        // Reconoce letra
        if ((valor >= 65 && valor <= 90) || (valor >= 97 && valor <= 122))
            return 'L';

        // Reconoce dígito
        if (valor >= 48 && valor <= 57)
            return 'D';

        // Reconoce enter
        if (valor == 10)
            return 'E';

        // Reconoce los caracteres especificados en el Excel
        if (CARACTERES_RECONOCIDOS.contains(valor))
            return (char) valor;

        // Caracter no reconocido -> Retorna "cualquier cosa" --> 'C'
        return 'C';
    }

    /**
     * Retorna la acción semántica asociada a su respectivo id
     * @param id Identificador de la acción semántica solicitada
     * @return Acción semántica asociada
     */
    private AccionSemantica getAccion(int id) {
        AccionSemantica AS1 = new AS1(tablaSimbolos);
        AccionSemantica AS2 = new AS2(tablaSimbolos);
        AccionSemantica AS3 = new AS3(tablaSimbolos);
        AccionSemantica AS6 = new AS6(tablaSimbolos);
        AccionSemantica AS9 = new AS9(tablaSimbolos);
        AccionSemantica AS11 = new AS11(tablaSimbolos);
        AccionSemantica AS16 = new AS16(tablaSimbolos);
        AccionSemantica ASError = new ASError(tablaSimbolos);
        AccionSemantica a;

        switch (id) {
            case 1:
                a = AS1;
                break;
            case 2:
                a = AS2;
                break;
            case 3:
                AS3.setearlinea(currentLine);
                a = AS3;
                break;
            case 6:
                AS6.setearlinea(currentLine);
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
                ASError.setearlinea(currentLine);
                a = ASError;
                break;
            default:
                throw new IllegalArgumentException("ID INVALIDO");
        }

        return a;
    }


    /**
     * Construye el mapeo de cada tipo de caracter con su respectiva columna dentro de las matrices
     */
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
        mapeoColumna.put('E', 21); // Enter
        mapeoColumna.put('C', 22);
    }
}
