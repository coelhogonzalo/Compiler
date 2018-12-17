package AnalizadorLexico;
//Para palabras reservadas
public class AccionSemantica2 implements AccionSemantica {

    public Token ejecutar(StringBuilder buffer, char c) {
        String lexema = buffer.toString();
        Token unToken=AnalizadorLexico.tablaSimbolos.get(lexema);
        if ( unToken != null)
            return unToken;
        else {
            Error e = new Error("ERROR", lexema + " (palabra reservada no valida)", AnalizadorLexico.cantLN);
            unToken = new Token("ERROR", AnalizadorLexico.TOKEN_ERROR,"ERROR");
            AnalizadorLexico.errores.add(e);
            if (buffer.length() < 25){
            	lexema="_" + buffer.subSequence(0, buffer.length());
                unToken = new Token(lexema, AnalizadorLexico.TOKEN_ID, "identificador");
            }
            else{
            	lexema="_" +buffer.subSequence(0, 25);
                unToken = new Token(lexema, AnalizadorLexico.TOKEN_ID, "identificador");
            }
            AnalizadorLexico.tablaSimbolos.put(lexema, unToken);
            return unToken;
        }
    }
}