package AnalizadorLexico;

public class Token {

    public String lexema;
    public Integer nro;
    public String tipo;
    public boolean declarada;
    public String uso;
    public String permisoFun;
    public String ambito;

    public Token(String lexema, Integer nro, String tipo) {
        super();
        this.lexema = lexema;
        this.nro = nro;
        this.tipo = tipo;
        this.declarada=false;
        this.permisoFun = "noseusaelparametro";
        this.ambito = "Nulo";
        //this.ambito;//Todos los token arrancan con arroba para despues agregar @
    }
    
    

    public String toString(){
        if ( nro != Analizador_Lexico.TOKEN_ERROR )
            return "Lexema: '" + this.lexema + " DECLARADA?: "+this.declarada + " Tipo: " +this.tipo +" Ambito:"+this.ambito+"\r\n";
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