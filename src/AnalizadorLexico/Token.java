package AnalizadorLexico;

public class Token {

    public String lexema;
    public Integer nro;
    public String tipo;
    public boolean declarada;

    public Token(String lexema, Integer nro, String tipo) {
        super();
        this.lexema = lexema;
        this.nro = nro;
        this.tipo = tipo;
        this.declarada=false;
    }
    
    public String getLexema(){
    	return lexema;
    }
    public Integer getNro(){
    	return nro;
    }
    public String getTipo(){
    	return tipo;
    }

    public String toString(){
        if ( nro != Analizador_Lexico.TOKEN_ERROR )
            return "Lexema: " + lexema + "   Numero: " + nro + "   Tipo: " + tipo +"\n";
        else
            return "TOKEN ERROR\n";
    }
}