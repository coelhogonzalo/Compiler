package AnalizadorLexico;

public class AccionSemantica10 implements AccionSemantica{

    public Token ejecutar(StringBuilder buffer, char c){
        char buf = buffer.charAt(0);
        int asciiCode = buf;
        Token unToken = new Token(buffer.toString(), asciiCode, "Caracter");
        return unToken;
    }
}
