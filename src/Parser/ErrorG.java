package Parser;
import AnalizadorLexico.Error;
public class ErrorG extends Error{

	public ErrorG( String texto, int linea) {
		super(null, texto, linea);
		// TODO Auto-generated constructor stub
		System.out.println(this.toString());
	}
	public String toString(){
		return lexema+" en la linea: "+linea+"\r\n";
	}
}
