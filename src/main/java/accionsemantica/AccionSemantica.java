package accionsemantica;

public abstract class AccionSemantica {

    protected static StringBuilder token;
    protected int indice;
    protected TablaSimbolos tablita = new TablaSimbolos();
    protected int linea;

    public AccionSemantica(TablaSimbolos t){
        this.tablita=t;
    }

    /*
        Hacer que adentro del aplicarAcción se edite el índice atributo de la clase, luego desde el analizador
        se obtiene el nuevo índice llamando al getIndice()

        Esto se hace porque en java el pasaje de parámetros (para los primitivos) es por valor, o sea que no
        podemos modificar el parámetro indice: lo pasamos a una variable interna de la clase para después accederlo con
        un getter

        Después si alguna acción semántica no tiene que tocar el índice no importa, hacemos simplemente
        un this.indice = indice para que quede igual
     */
    public abstract String aplicarAccion(char a, int indice);

    public int getIndice() {
        return indice;
    }

    public void setearlinea(int l){this.linea=l;}

    public String getToken() {
        // TODO: acomodar, acceder a la tabla de símbolos

        // este método es el que va a poblar la lista de tokens del analizador léxico
        // la forma que deben tener los tokens a entregar es:  [ID, ‘Plazo’] o [CTE, ‘30’] o bien [IF] en caso de que
        // tengan un solo lexema
        return token.toString();
    }

}
    