package AnalizadorLexico;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import GeneracionAssembler.GeneradorAssembler;
import Parser.Parser;


public class Main {

    public static void main(String[] args) throws IOException {
    	String fileName=null;
    	boolean genereAssembler=false;
    	if(args.length>0){
	    	fileName=args[0];
	        File f = new File(fileName);
	        AnalizadorLexico al = new AnalizadorLexico(f);
	        Parser p = new Parser(false);
	        p.errores = al.getErrores();
	        Parser.estructuras = new ArrayList<>();
	        p.al = al;
	        int unint = p.parsepublico();
	        
	        System.out.println();
	        System.out.println("Polaca inversa resultante:");
	        p.PI.printContent();
	        File polacaFile = new File("Polaca.txt");
	        FileManager.write(p.PI.getPI().toString(), polacaFile);
	        System.out.println();
	        if (unint == 0)
	            System.out.print("ACCEPT, se reconocio la gramatica");
	        else
	            System.out.print("No se reconocio la gramatica");
	        if(unint==0 && !Error.huboErrores){
	        	
	        	System.out.print(" y no se encontraron errores (PROCEED COMPILATION)");
	        	GeneradorAssembler gen=new GeneradorAssembler(p.PI);
	        	String fileNameOutput=removeExtension(fileName);
	        	System.out.println();
	        	System.out.println("Voy a generar un .asm en: "+fileNameOutput);
	        	gen.generameAssemblydotexe(fileNameOutput);
	        	
	        	String comc ="\\masm32\\bin\\ml /c /Zd /coff "+fileNameOutput+".asm";
	        	Runtime.getRuntime().exec(comc);
	        	
	        	String coml ="\\masm32\\bin\\Link /SUBSYSTEM:CONSOLE "+fileNameOutput+".obj";
	        	Runtime.getRuntime().exec(coml);
	        	System.out.print("Si no se genero un nuevo '.exe', correr de nuevo el programa");
	        	
	     
	        }
	        else
	        	System.out.print(" y se encontraron errores (ABORT COMPILATION)");
	        if(true){
		        File fErroresOut = new File("Errores.txt");
		        FileManager.write(AnalizadorLexico.errores.toString(), fErroresOut);
		        File fTokensOut = new File("Tokens.txt");
		        FileManager.write(AnalizadorLexico.tokens.toString(), fTokensOut);
		        File estructurasOut = new File("Estructuras.txt");
		        FileManager.write(Parser.estructuras.toString(), estructurasOut);
		        Error.huboErrores=false;
	        }
    	}
    	else
    		System.out.println("Ingrese el nombre del archivo a compilar");
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