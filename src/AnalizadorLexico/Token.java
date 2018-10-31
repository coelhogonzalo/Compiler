package AnalizadorLexico;

public class Token {

    public String lexema;
    public Integer nro;
    public String tipo;
    public boolean declarada;
    public String uso;

    public Token(String lexema, Integer nro, String tipo) {
        super();
        this.lexema = lexema;
        this.nro = nro;
        this.tipo = tipo;
        this.declarada=false;
    }
    
    

    public String toString(){
        if ( nro != Analizador_Lexico.TOKEN_ERROR )
            return "Lexema: " + lexema + "   Numero: " + nro + "   Tipo: " + tipo +"\n";
        else
            return "TOKEN ERROR\n";
    }
}