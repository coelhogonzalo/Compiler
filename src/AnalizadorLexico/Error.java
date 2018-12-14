package AnalizadorLexico;//asd

public class Error {
	public String tipo;
	public String lexema;
	public int linea;
	public static boolean huboErrores=false;
	public Error(String tipo, String lexema, int linea) {
		this.tipo = tipo;
		this.lexema = lexema;
		this.linea = linea;
		if(tipo=="ERROR")
			Error.huboErrores=true;
	}
	public String toString(){
		return "Tipo: "+tipo+"   Lexema:  "+lexema+"  Numero de linea: "+linea +"\r\n";
	}
}
