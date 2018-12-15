package AnalizadorLexico;

//cadena==261
public class AccionSemantica8 implements AccionSemantica {
    // CONCATENA Y AGREGA EN LA TABLA DE SIMBOLOS DE UNA, ES PARA TOKENS QUE PASAN DIRECTO AL ESTADO FINAL

    public Token ejecutar(StringBuilder buffer, char c) {
    	
        buffer.append(c);
        String lexema=buffer.toString();
        Token unToken=AnalizadorLexico.tablaSimbolos.get(lexema);
        if (unToken!=null) {
            return unToken;
        } else {
        	unToken=new Token(lexema, AnalizadorLexico.TOKEN_CADENA,"cadena");
            AnalizadorLexico.tablaSimbolos.put(lexema, unToken);
            return unToken;
        }
    }
}