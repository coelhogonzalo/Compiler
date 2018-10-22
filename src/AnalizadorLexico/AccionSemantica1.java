package AnalizadorLexico;

public class AccionSemantica1 implements AccionSemantica {
//CONCATENA

    public Token ejecutar(StringBuilder buffer, char c) {
        buffer.append(c);
        return null;
    }

}