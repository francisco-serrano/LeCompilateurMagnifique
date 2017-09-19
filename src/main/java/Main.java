import accionsemantica.TablaSimbolos;

public class Main {

    public static void main(String[] args)
    {
        String fileDir = "archivo-prueba.txt";
        String dirMatrizEstados = "matriz-estados.txt";
        String dirMatrizSemantica = "matriz-acc-semanticas.txt";

        AnalizadorLexico analizadorLexico = new AnalizadorLexico(fileDir, dirMatrizEstados, dirMatrizSemantica);

        TablaSimbolos tablaSimbolos = new TablaSimbolos();
        tablaSimbolos.add("ID", "pepe");
        tablaSimbolos.add("ID", "cantidadArchivos");
        tablaSimbolos.add("ID", "altaVariable");
        tablaSimbolos.add("CTE", "23");
        tablaSimbolos.add("CTE", "34");

        for (String key : tablaSimbolos.keySet())
            System.out.println(key + " --> " + tablaSimbolos.getLexemas(key));



//        for (Integer i : analizadorLexico.getArchivo())
//            System.out.print(i + " ");
    }
}
