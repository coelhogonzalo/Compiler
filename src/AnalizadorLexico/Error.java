package AnalizadorLexico;//asd

public class Error {
	public String tipo;
	public String lexema;
	public int linea;
	public Error(String tipo, String lexema, int linea) {
		super();
		this.tipo = tipo;
		this.lexema = lexema;
		this.linea = linea;
	}
	public String toString(){
		return "Tipo: "+tipo+"   Lexema:  "+lexema+"  Numero de linea: "+linea +"\r\n";
	}
}
