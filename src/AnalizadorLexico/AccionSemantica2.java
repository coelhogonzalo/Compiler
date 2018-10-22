package AnalizadorLexico;
//Para palabras reservadas
public class AccionSemantica2 implements AccionSemantica {

    public Token ejecutar(StringBuilder buffer, char c) {
        String lexema = buffer.toString();
        Token unToken=Analizador_Lexico.tablaSimbolos.get(lexema);
        if ( unToken != null)
            return unToken;
        else {
            Error e = new Error("ERROR", lexema + " (palabra reservada no valida)", Analizador_Lexico.cantLN);
            unToken = new Token("ERROR", Analizador_Lexico.TOKEN_ERROR,"ERROR");
            Analizador_Lexico.errores.add(e);
            if (buffer.length() < 24)
                unToken = new Token("_" + buffer.subSequence(0, buffer.length()), Analizador_Lexico.TOKEN_ID, "identificador");
            else
                unToken = new Token("_" + buffer.subSequence(0, 24), Analizador_Lexico.TOKEN_ID, "identificador");
            return unToken;
        }
    }
}