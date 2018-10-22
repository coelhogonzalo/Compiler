package AnalizadorLexico;
//Para los token de un solo caracter que son reconocidos por su codigo ascii
public class AccionSemantica7 implements AccionSemantica {
    public Token ejecutar(StringBuilder buffer, char c) {
        buffer.append(c);
        int asciiCode = c;
        Token unToken = new Token(buffer.toString(), asciiCode, "Caracter");
        return unToken;
    }
}