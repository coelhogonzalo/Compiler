package AnalizadorLexico;

public class Token {

    public String lexema;
    public Integer nro;
    public String tipo;
    public boolean declarada;
    public String uso;
    public String permisoFun;

    public Token(String lexema, Integer nro, String tipo) {
        super();
        this.lexema = lexema;
        this.nro = nro;
        this.tipo = tipo;
        this.declarada=false;
        this.permisoFun = "noseusaelparametro";
    }
    
    

    public String toString(){
        if ( nro != Analizador_Lexico.TOKEN_ERROR )
            return "Lexema: '" + lexema + "'   Numero: " + nro + "   Tipo: " + tipo +"\r\n";
        else
            return "TOKEN ERROR\n";
    }
    public String toStringDetallado(){
        if ( nro != Analizador_Lexico.TOKEN_ERROR )
            return "LEXEMA: '" + lexema + "'||| NUMERO: " + nro + "||| TIPO: " + tipo +"|||DECLARADA?: "+this.declarada+"|||USO: "+this.uso+"|||PERMISO: "+this.permisoFun+"\r\n";
        else
            return "TOKEN ERROR\n";
    }
}