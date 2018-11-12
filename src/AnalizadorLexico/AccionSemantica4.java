package AnalizadorLexico;//asd

import Parser.Parser;

//IDENTIFICADORES==285
public class AccionSemantica4 implements AccionSemantica {
    //PIDE DEVOLVER EL PAR <ID,PTR>

    public Token ejecutar(StringBuilder buffer, char c) {
    	Token unToken;
    	String lexema=buffer.toString();
        if (lexema.length() < 25) {
        	unToken=Analizador_Lexico.tablaSimbolos.get(lexema);
            if (unToken!=null) {
                return unToken;//return Analizador_Lexico.TOKEN_ID;
            } else {
            	//lexema+=Parser.ambitoActual;
            	//System.out.println("El identificador "+lexema);
                Analizador_Lexico.tablaSimbolos.put(lexema, new Token(lexema, Analizador_Lexico.TOKEN_ID, "identificador"));//Que integer va?
                unToken=Analizador_Lexico.tablaSimbolos.get(lexema);
            }return unToken;
        } 
        else{
            Error e = new Error("ERROR", lexema + " (identificador excede el tamaño permitido)", Analizador_Lexico.cantLN);
        	Analizador_Lexico.errores.add(e);
            unToken = new Token(buffer.substring(0, 25), Analizador_Lexico.TOKEN_ID, "identificador");
            //lexema+=Parser.ambitoActual;
            Analizador_Lexico.tablaSimbolos.put(lexema, unToken);
            return unToken;
        }
    }
}