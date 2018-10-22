package AnalizadorLexico;

//UNSIGNED LONG==286
public class AccionSemantica5 implements AccionSemantica {
    //ES PARA CONSTANTES ENTEROS LARGOS SIN SIGNO
    //CHEQUEA RANGO CONSTANTE Y AGREGA A TABLA DE SIMBOLOS, SI SE VA DE RANGO REEMPLAZA CON EL MAYOR DEL RANGO
    //PIDE DEVOLVER EL PAR <ID,PTR>

    public Token ejecutar(StringBuilder buffer, char c) {

        if (buffer.length() > 0) {
            EliminarChars(buffer);
            long auxLong=Long.parseLong(buffer.toString());
            if ((auxLong<= 4294967295l) && (auxLong>= 0)) {
                buffer.append("_ul");
                String lexema=buffer.toString();
                Token unToken=new Token(lexema, Analizador_Lexico.TOKEN_UL,"uslinteger");
                Analizador_Lexico.tablaSimbolos.put(lexema, unToken);//esto esta mal no?
                return unToken;
            } else {
                Error e = new Error("WARNING", buffer.toString() + ": valor fuera de rango", Analizador_Lexico.cantLN);
                Analizador_Lexico.errores.add(e);
                buffer.delete(0, buffer.length());
                buffer.append("4294967295_ul");
                String lexema=buffer.toString();
                Token unToken=new Token(lexema, Analizador_Lexico.TOKEN_UL,"uslinteger");
                Analizador_Lexico.tablaSimbolos.put(lexema, unToken);
                return unToken;
            }

            //System.out.print("Warning: Se excedio del rango permitido, se procede a realizar tecnica de reemplazo");
        }
        return null;
    }

    private void EliminarChars(StringBuilder b) {
        if (b.length() > 0)
            b.delete(b.indexOf("_u"), b.length());
    }
	
	/*public static void test1(String [] args){// El sacarle el _ul anda bien
		String buffer="23432_ul";
		StringBuilder nbuffer=new StringBuilder(buffer);
		nbuffer.deleteCharAt(nbuffer.length()-1);
		nbuffer.deleteCharAt(nbuffer.length()-1);
		nbuffer.deleteCharAt(nbuffer.length()-1);
		buffer=nbuffer.toString();
		System.out.println(buffer);
	}*/

}