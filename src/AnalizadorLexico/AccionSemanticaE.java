package AnalizadorLexico;

public class AccionSemanticaE implements AccionSemantica {
private AccionSemantica5 arreglarNumero;
    public Token ejecutar(StringBuilder buffer, char c) {
        Error e = new Error("ERROR", "Definicion no valida.", Analizador_Lexico.cantLN);
        Analizador_Lexico.errores.add(e);
        if (buffer.length() > 0){
        	if (isNumero(buffer)){//Si es un numero valido le agrega el _u para que en la accionsemantica 5 (arreglar numero) se guarde como un _ul
        		arreglarNumero=new AccionSemantica5();
        		e = new Error("WARNING", buffer.toString() + ": Las constantes de tipo uslinteger deben terminar en _ul", Analizador_Lexico.cantLN);
        		buffer.append("_u");
                Analizador_Lexico.errores.add(e);
        		return arreglarNumero.ejecutar(buffer, c);
        	}
        	else{
        		//System.out.println("Corrigiendo un identificador!");
        		int indice=buffer.indexOf("_");
        		if (indice != 0)
        			buffer.deleteCharAt(indice);
        		if (buffer.charAt(0) != '_') {
        			if (buffer.length() < 25){
        				e = new Error("WARNING", buffer.toString() + ": Se agrego el caracter _ al principio y se interpreto como un identificador ", Analizador_Lexico.cantLN);
                        Analizador_Lexico.errores.add(e);
                        String lexema="_" +buffer.subSequence(0, 24);
                        Token unToken = new Token(lexema, Analizador_Lexico.TOKEN_ID, "identificador");
                        Analizador_Lexico.tablaSimbolos.put(lexema, unToken);
        				return unToken;
        			}
        			else{
        				e = new Error("WARNING", buffer.toString() + ": Se agrego el caracter _ al principio y se interpreto como un identificador ", Analizador_Lexico.cantLN);
        				Analizador_Lexico.errores.add(e);
                        String lexema="_" +buffer.subSequence(0, 24);
                        Token unToken = new Token(lexema, Analizador_Lexico.TOKEN_ID, "identificador");
                        Analizador_Lexico.tablaSimbolos.put(lexema, unToken);
        				return unToken;
        			}
        		} else {
        			if (buffer.length() < 25){
        				e = new Error("WARNING", buffer.toString() + ": Se interpreto como un identificador ", Analizador_Lexico.cantLN);
                        Analizador_Lexico.errores.add(e);
                        String lexema=buffer.subSequence(0, buffer.length()).toString();
                        Token unToken = new Token(lexema, Analizador_Lexico.TOKEN_ID, "identificador");
                        Analizador_Lexico.tablaSimbolos.put(lexema, unToken);
        				return unToken;
        			}
        			else{
        				e = new Error("WARNING", buffer.toString() + ": Se interpreto como un identificador ", Analizador_Lexico.cantLN);
                        Analizador_Lexico.errores.add(e);
                        String lexema=buffer.subSequence(0, 24).toString();
                        Token unToken = new Token(lexema, Analizador_Lexico.TOKEN_ID, "identificador");
                        Analizador_Lexico.tablaSimbolos.put(lexema, unToken);
        				return unToken;
        			}
        		}
        	}
        }
        return null;
    }
public boolean isNumero(StringBuilder buffer){
	//System.out.println("Corrigiendo un numero!");
	if(buffer.charAt(buffer.length()-1)=='l')
		buffer.deleteCharAt(buffer.length()-1);	
	if(buffer.charAt(buffer.length()-1)=='u')
		buffer.deleteCharAt(buffer.length()-1);
	if(buffer.charAt(buffer.length()-1)=='_')
		buffer.deleteCharAt(buffer.length()-1);
	String lexema=buffer.toString();
	char[] arreglo= lexema.toCharArray();
	for(char i:arreglo)
		if(i<48 || i>57)//Si no es un digito
			return false;
	return true;
}
}