package generadorcodigo;

import lexer.Terceto;

import java.util.List;

public class Generador {

    private static final int CANTIDAD_REGISTROS = 4;

    private List<Terceto> tercetos;
    private TablaRegistros tablaRegistros = new TablaRegistros(CANTIDAD_REGISTROS);
    private StringBuilder codigoAssembler = new StringBuilder();

    public Generador(List<Terceto> tercetos) {
        this.tercetos = tercetos;
    }

    public void generateAssembler() {
        for (Terceto terceto : tercetos)
            generateAssembler(terceto);
    }

    private void generateAssembler(Terceto terceto) {
        /*
            Cada operación tiene un tipo de assembler asociado.
            Hay que ir asignándole el registro asociado a cada terceto.
         */
        System.out.println("Generando assembler para el terceto " + terceto);
    }
}
