package AnalizadorLexico;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import Parser.Parser;


public class Main {

    public static void main(String[] args) throws IOException {
        //mainLexico(args);
        File f = new File("prueba.txt");
        Analizador_Lexico al = new Analizador_Lexico(f);
        Parser p = new Parser(false);
        p.errores = al.getErrores();
        Parser.estructuras = new ArrayList<>();
        p.al = al;
        int unint = p.parsepublico();
        System.out.println("Tabla de simbolos:");
        System.out.println(Analizador_Lexico.tablaSimbolos);
        System.out.println("");
        System.out.println("Polaca inversa resultante:");
        p.PI.printContent();
        System.out.println("");
        if (unint == 0)
            System.out.print("ACCEPT, se reconocio la gramatica");
        else
            System.out.print("No se reconocio la gramatica");
        if(!Error.huboErrores){
        	System.out.print(" y no se encontraron errores (PROCEED COMPILATION)");
        	//ACA EMA NOS GENERA UN TERRIBLE ASSEMBLER
        }
        else
        	System.out.print(" y se encontraron errores (ABORT COMPILATION)");

        File fErroresOut = new File("Errores.txt");
        FileManager.write(Analizador_Lexico.errores.toString(), fErroresOut);
        File fTokensOut = new File("Tokens.txt");
        FileManager.write(Analizador_Lexico.tokens.toString(), fTokensOut);
        File estructurasOut = new File("Estructuras.txt");
        FileManager.write(Parser.estructuras.toString(), estructurasOut);
        Error.huboErrores=false;
    }

}