
public class Main {

    public static void main(String[] args)
    {
        String fileDir = "archivo-prueba.txt";
        String dirMatrizEstados = "matriz-estados.txt";
        String dirMatrizSemantica = "matriz-acc-semanticas.txt";

        AnalizadorLexico analizadorLexico = new AnalizadorLexico(fileDir, dirMatrizEstados, dirMatrizSemantica);

//        for (Integer i : analizadorLexico.getArchivo())
//            System.out.print(i + " ");
    }
}
