package AnalizadorLexico;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import Parser.Parser;


public class Main {
//LA IDEA DE ESTE MAIN ES QUE HAGA ALL DE UN SAQUE CON ALL EL TEMA DEL OS ARCHIVO ASI COMO QUERIA ELLA
    //dice all en vez de tod o porque no me deja sino ni idea que onda
   /* public static void main(String[] args) throws IOException {
        File f = new File("Ejemplos parte 1.txt");
        Analizador_Lexico al = new Analizador_Lexico(f);
        Parser p = new Parser(false);
        ArrayList<AnalizadorLexico.Error> erroresGram = new ArrayList<>();
        p.erroresGram = erroresGram;
        Parser.estructuras = new ArrayList<>();
        p.al = al;
        int unint = p.parsepublico();
        if (unint == 0)
            System.out.println("ACCEPT, se reconocio la gramatica");
        else
            System.out.println("n");
    }*/


    public static void mainLexico(String[] args) throws IOException {
        System.out.println("prueba.txt");
        File f = new File("prueba.txt");
        Analizador_Lexico lexico = null;
        ArrayList<Token> tokens = null;
        ArrayList<Error> errores = null;
        try {
            lexico = new Analizador_Lexico(f);
            tokens = getAllTokens(lexico);
            errores = lexico.getErrores();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        File fTokensOut = new File("L_Tokens.txt");
        FileManager.write(tokens.toString(), fTokensOut);
        File fErroresOut = new File("L_Errores.txt");
        FileManager.write(errores.toString(), fErroresOut);
        File fTSOut = new File("L_Tabla_Simbolos.txt");
        FileManager.write(Analizador_Lexico.tablaSimbolos.toString(), fTSOut);
    }

    public static void main(String[] args) throws IOException {
        //mainLexico(args);
        File f = new File("prueba.txt");
        Analizador_Lexico lexico = null;
        ArrayList<AnalizadorLexico.Error> erroresGram = null;
        ArrayList<Token> tokens = null;
        try {
            lexico = new Analizador_Lexico(f);
            tokens = Main.getAllTokens(lexico);
            erroresGram = new ArrayList<>();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Analizador_Lexico al = new Analizador_Lexico(f);
        Parser p = new Parser(false);
        p.erroresGram = erroresGram;
        Parser.estructuras = new ArrayList<>();
        p.al = al;
        int unint = p.parsepublico();
        System.out.println(Analizador_Lexico.tablaSimbolos);
        p.PI.printContent();
        if (unint == 0)
            System.out.println("ACCEPT, se reconocio la gramatica");
        else
            System.out.println("No se reconocio la gramatica");

        File fErroresOut = new File("S_Errores.txt");
        FileManager.write(erroresGram.toString(), fErroresOut);
        File fTokensOut = new File("Tokens.txt");
        FileManager.write(tokens.toString(), fTokensOut);
        File estructurasOut = new File("Estructuras.txt");
        FileManager.write(Parser.estructuras.toString(), estructurasOut);
    }

    public static ArrayList<Token> getAllTokens(Analizador_Lexico lexico) throws IOException {
        ArrayList<Token> out = new ArrayList<>();
        Token token = lexico.getToken();
        while (token != null) {
            out.add(token);
            token = lexico.getToken();
        }
        Analizador_Lexico.cantLN = 1;
        return out;
    }
}