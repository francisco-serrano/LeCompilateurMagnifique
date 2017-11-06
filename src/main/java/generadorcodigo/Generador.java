package generadorcodigo;

import com.google.common.collect.Multimap;
import lexer.TablaSimbolos;
import lexer.Terceto;
import lexer.Token;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Generador {

    private static final int CANTIDAD_REGISTROS = 4;

    private List<Terceto> tercetos;
    private TablaRegistros tablaRegistros = new TablaRegistros(CANTIDAD_REGISTROS);
    private StringBuilder code = new StringBuilder();
    private TablaSimbolos tablita = new TablaSimbolos();

    public Generador(List<Terceto> tercetos, TablaSimbolos ts) {
        this.tercetos = tercetos;
        this.tablita = ts;
    }

    private void declararVariables(){
        this.code.append(".data \n");
        Multimap<String, Token> mapa = tablita.getTabla();
        Collection<Token> e = mapa.values();
        Iterator<Token> it = e.iterator();
        while (it.hasNext()){
            Token t = it.next();
            if (!t.getUso().equals("undefined")){
                switch (t.getUso()){
                    case "cadena":
                        break;
                    case "variable":
                        break;
                    case "nombre funcion":
                        break;
                    case "constante":
                        break;

                }
            }
        }
    }

    public void generateAssembler() {
        this.code.append(".386 \n");
        this.code.append(".model flat, stdcall         ;Modelo de memoria 'pequeño' \n");
        this.code.append(".stack 200h                  ;Tamaño de la pila\n");

        //this.declararMacro();

        this.declararVariables();

        for (Terceto terceto : tercetos)
            assemblerTerceto(terceto);
    }

    private void assemblerTerceto(Terceto terceto) {
        /*
            Cada operación tiene un tipo de assembler asociado.
            Hay que ir asignándole el registro asociado a cada terceto.
         */
        System.out.println("Generando assembler para el terceto " + terceto);
    }
}
