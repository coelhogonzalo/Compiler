package AnalizadorLexico;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import GeneracionAssembler.GeneradorAssembler;
import Parser.Parser;


public class Main {

    public static void main(String[] args) throws IOException {
    	String fileName=args[0];
        File f = new File(fileName);
        Analizador_Lexico al = new Analizador_Lexico(f);
        Parser p = new Parser(false);
        p.errores = al.getErrores();
        Parser.estructuras = new ArrayList<>();
        p.al = al;
        int unint = p.parsepublico();
        
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
        	//System.out.println("Tabla de simbolos:");
            //System.out.println(Analizador_Lexico.tablaSimbolos);
        	GeneradorAssembler gen=new GeneradorAssembler(p.PI);
        	String fileNameOutput=removeExtension(fileName);
        	System.out.println("");
        	System.out.println("Voy a generar un .asm en: "+fileNameOutput);
        	gen.generameAssemblydotexe(fileNameOutput);
        	
        	String comc ="\\masm32\\bin\\ml /c /Zd /coff "+fileNameOutput+".asm";
        	Process ptasm32= Runtime.getRuntime().exec(comc);
        	InputStream is=ptasm32.getInputStream();
        	
        	String coml ="\\masm32\\bin\\Link /SUBSYSTEM:CONSOLE "+fileNameOutput+".obj";
        	Process ptlink32= Runtime.getRuntime().exec(coml);
        	InputStream is2=ptlink32.getInputStream();
        	Runtime.getRuntime().exec(coml);
        	System.out.print("Si no se genero un nuevo '.exe', correr de nuevo el programa");
        	
     
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
    private static String removeExtension(String fileName){
    	String cortado=fileName;
    	if(!cortado.isEmpty()){
    		while(cortado.charAt(cortado.length()-1)!='.')
    			cortado=cortado.substring(0, cortado.length()-1);
    		cortado=cortado.substring(0, cortado.length()-1);//Saco el arroba
    		return cortado;
    	}
    	return cortado;
    }
    	
}