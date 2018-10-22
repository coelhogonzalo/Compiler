package AnalizadorLexico;

public interface  AccionSemantica {
    public  Token ejecutar(StringBuilder buffer, char c);
}