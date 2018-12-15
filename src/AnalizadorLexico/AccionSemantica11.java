package AnalizadorLexico;
//Para los saltos de linea de los cadena multilinea
public class AccionSemantica11 implements AccionSemantica {
    public Token ejecutar(StringBuilder buffer, char c) {
        AnalizadorLexico.cantLN++;
        return null;
    }
}
