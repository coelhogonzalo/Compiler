package AnalizadorLexico;

public class AccionSemanticaE implements AccionSemantica {
private AccionSemantica5 arreglarNumero;
    public Token ejecutar(StringBuilder buffer, char c) {
        Error e = new Error("ERROR", "Definicion no valida.", Analizador_Lexico.cantLN);
        Analizador_Lexico.errores.add(e);
        if (buffer.length() > 0){
        	System.out.println(buffer);
        	if (isNumero(buffer)){
        		arreglarNumero=new AccionSemantica5();
        		e = new Error("WARNING", buffer.toString() + ": Las constantes de tipo uslinteger deben terminar en _ul", Analizador_Lexico.cantLN);

                Analizador_Lexico.errores.add(e);
        		buffer.append("_u");
        		return arreglarNumero.ejecutar(buffer, c);
        	}
        	else{
        		int indice=buffer.indexOf("_");
        		if (indice != 0)
        			buffer.deleteCharAt(indice);
        		if (buffer.charAt(0) != '_') {
        			if (buffer.length() < 15){
        				e = new Error("WARNING", buffer.toString() + ": Se agrego el caracter _ al principio y se interpreto como un identificador ", Analizador_Lexico.cantLN);
                        Analizador_Lexico.errores.add(e);
        				return new Token("_" + buffer.subSequence(0, buffer.length()) + " error", Analizador_Lexico.TOKEN_ID, "identificador");
        			}
        			else{
        				e = new Error("WARNING", buffer.toString() + ": Se agrego el caracter _ al principio y se interpreto como un identificador ", Analizador_Lexico.cantLN);
                        System.out.println(e.toString());
        				return new Token("_" + buffer.subSequence(0, 15) + " error", Analizador_Lexico.TOKEN_ID, "identificador");
        			}
        		} else {
        			if (buffer.length() < 15){
        				e = new Error("WARNING", buffer.toString() + ": Se interpreto como un identificador ", Analizador_Lexico.cantLN);
                        Analizador_Lexico.errores.add(e);
        				return new Token(buffer.subSequence(0, buffer.length()) + " error", Analizador_Lexico.TOKEN_ID, "identificador");
        			}
        			else{
        				e = new Error("WARNING", buffer.toString() + ": Se interpreto como un identificador ", Analizador_Lexico.cantLN);
                        Analizador_Lexico.errores.add(e);
        				return new Token(buffer.subSequence(0, 15) + " error", Analizador_Lexico.TOKEN_ID, "identificador");
        			}
        		}
        	}
        }
        return null;
    }
public boolean isNumero(StringBuilder buffer){
	String lexema=buffer.toString();
	char[] arreglo= lexema.toCharArray();
	for(char i:arreglo)
		if(i<48 || i>57)//Si no es un digito
			return false;
	return true;
}
}