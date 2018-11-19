package AnalizadorLexico;

public class Token {

    private final int READONLY = 0;

    public String lexema;
    public Integer nro;
    public String tipo;
    public boolean declarada;
    public String uso;
    public int permisoFun;
    public String ambito;

    public Token(String lexema, Integer nro, String tipo) {
        super();
        this.lexema = lexema;
        this.nro = nro;
        this.tipo = tipo;
        this.declarada=false;
        this.permisoFun = READONLY;
        this.ambito = "Nulo";
        //this.ambito;//Todos los token arrancan con arroba para despues agregar @
    }


    public void setUso(String uso){
    	this.uso=uso;
    }
    
    public String toString(){
        if ( nro != Analizador_Lexico.TOKEN_ERROR )
            return "Lexema: '" + this.lexema + "' dec: "+this.declarada + " Tipo: " +this.tipo +" Ambito:"+this.ambito+"\r\n";
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