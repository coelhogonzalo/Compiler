package AnalizadorLexico;

public class TokenValue {
    public String lexema;
    public int cantLN;

    public TokenValue(String lexema, int cantLN) {
        super();
        this.lexema = lexema;
        this.cantLN = cantLN;
    }
}