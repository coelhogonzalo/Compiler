package AnalizadorLexico;//asd

import Parser.Parser;

//IDENTIFICADORES==285
public class AccionSemantica4 implements AccionSemantica {
    //PIDE DEVOLVER EL PAR <ID,PTR>

    public Token ejecutar(StringBuilder buffer, char c) {
    	Token unToken;
    	String lexema=buffer.toString();
        if (lexema.length() < 25) {
        	unToken=AnalizadorLexico.tablaSimbolos.get(lexema);
            if (unToken!=null) {
                return unToken;//return Analizador_Lexico.TOKEN_ID;
            } else {
            	//lexema+=Parser.ambitoActual;
            	//System.out.println("El identificador "+lexema);
                AnalizadorLexico.tablaSimbolos.put(lexema, new Token(lexema, AnalizadorLexico.TOKEN_ID, "identificador"));//Que integer va?
                unToken=AnalizadorLexico.tablaSimbolos.get(lexema);
            }return unToken;
        } 
        else{
            Error e = new Error("ERROR", lexema + " (identificador excede el tamaño permitido)", AnalizadorLexico.cantLN);
        	AnalizadorLexico.errores.add(e);
            unToken = new Token(buffer.substring(0, 25), AnalizadorLexico.TOKEN_ID, "identificador");
            //lexema+=Parser.ambitoActual;
            AnalizadorLexico.tablaSimbolos.put(lexema, unToken);
            return unToken;
        }
    }
}