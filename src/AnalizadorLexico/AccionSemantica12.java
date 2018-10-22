package AnalizadorLexico;

public class AccionSemantica12 implements AccionSemantica {
    @Override
    public Token ejecutar(StringBuilder buffer, char c) {
        buffer.append("E");
        return null;
    }
}
