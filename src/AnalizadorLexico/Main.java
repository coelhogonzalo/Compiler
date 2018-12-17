package AnalizadorLexico;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
	        	genereAssembler=true;
	        	String comc ="\\masm32\\bin\\ml /c /Zd /coff "+fileNameOutput+".asm";
	        	Process ptasm32 = Runtime.getRuntime().exec(comc);
	        	InputStream is1= ptasm32.getInputStream();
	        	String coml ="\\masm32\\bin\\Link /SUBSYSTEM:CONSOLE "+fileNameOutput+".obj";
	        	Process ptlink32=Runtime.getRuntime().exec(coml);
	        	System.out.println();
	        	InputStream is2= ptlink32.getInputStream();
	        	Scanner scanner = new Scanner(ptasm32.getInputStream());
	        	while (scanner.hasNext()){
	        		String stringError=scanner.nextLine();
	        		System.out.println(stringError);
	        		if(stringError.contains("fatal error"))
	        			System.out.println("Ocurrio un error generando el .obj, se debe ejecutar de nuevo");
	        	}
	        	Scanner scanner2 = new Scanner(ptlink32.getInputStream());
	        	while (scanner2.hasNext()){
	        		String stringError=scanner2.nextLine();
	        		System.out.println(stringError);
	        		if(stringError.contains("fatal error"))
	        			System.out.println("Ocurrio un error generando el .exe, se debe ejecutar de nuevo");
	        	}
	        	
	     
	        }
	        else
	        	System.out.print(" y se encontraron errores (ABORT COMPILATION)");
	        File fErroresOut = new File("Errores.txt");
	        File fTokensOut = new File("Tokens.txt");
	        File estructurasOut = new File("Estructuras.txt");
	        FileManager.write(AnalizadorLexico.tokens.toString(), fTokensOut);
	        FileManager.write(Parser.estructuras.toString(), estructurasOut);
	        if(!genereAssembler){
		        FileManager.write(AnalizadorLexico.errores.toString(), fErroresOut);
		        Error.huboErrores=false;
	        }
	        else
	        	FileManager.write("No hubo errores", fErroresOut);
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