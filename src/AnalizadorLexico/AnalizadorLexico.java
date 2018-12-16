package AnalizadorLexico;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import GeneracionAssembler.GeneradorAssembler;

public class AnalizadorLexico {
    private final int ESTADO_FINAL = -1;
    
    private static Logger l=Logger.getLogger(GeneradorAssembler.class.toString());
    private String charAnterior = "0"; //0 es que no es valido, 1 es que es valido
    public static HashMap<String, Token> tablaSimbolos = null;
    private HashMap<String, Integer> equivalentes = null;
    private MatrizLexica MLexica = null;
    public static List<Error> errores;
    public static int cantLN ;
    public static List<Token> tokens;
    private static FileManager fm = null;
    public final static int TOKEN_CADENA = 258;//Cambiar si es necesario
    public final static int TOKEN_FLOAT = 260;
    public final static int TOKEN_UL = 259;
    public final static int TOKEN_ID = 257;
    public final static int TOKEN_ERROR = 277;


    public final static short ID = 257;
    public final static short CADENA = 258;
    public final static short USLINTEGER = 259;
    public final static short SINGLE = 260;
    public final static short IF = 261;
    public final static short ELSE = 262;
    public final static short ENDIF = 263;
    public final static short WHILE = 264;
    public final static short READONLY = 265;
    public final static short WRITE = 266;
    public final static short PASS = 267;
    public final static short MAYORIGUAL = 268;
    public final static short MENORIGUAL = 269;
    public final static short IGUALIGUAL = 270;
    public final static short DISTINTO = 271;
    public final static short ASIGN = 272;
    public final static short RETURN = 273;
    public final static short PRINT = 274;
    public final static short SINGLEPR = 275;
    public final static short USLINTEGERPR = 276;
    public final static short INVALIDO = 277;


    public AnalizadorLexico(File mFile) throws IOException {
        MLexica = new MatrizLexica();
        errores = new ArrayList<>();
        tokens= new ArrayList<>();
        cargarEquivalentes();
        fm = new FileManager(mFile);
        AnalizadorLexico.tablaSimbolos = new HashMap<>();
        CargarTablaSimbolos();
        AnalizadorLexico.cantLN=1;
    }

    public ArrayList<Error> getErrores() {
        return (ArrayList<Error>) errores;
    }

    private void CargarTablaSimbolos() {

        int aux = AnalizadorLexico.ASIGN;
        Token unToken = new Token(":=", aux, "Operador");
        tablaSimbolos.put(":=", unToken);
        aux = AnalizadorLexico.MAYORIGUAL;
        unToken = new Token(">=", aux, "Operador");
        tablaSimbolos.put(">=", unToken);
        aux = AnalizadorLexico.MENORIGUAL;
        unToken = new Token("<=", aux, "Operador");
        tablaSimbolos.put("<=", unToken);
        aux = AnalizadorLexico.DISTINTO;
        unToken = new Token("!=", aux, "Operador");
        tablaSimbolos.put("!=", unToken);
        aux = AnalizadorLexico.IF;
        unToken = new Token("if", aux, "String");
        tablaSimbolos.put("if", unToken);
        aux = AnalizadorLexico.ELSE;
        unToken = new Token("else", aux, "String");
        tablaSimbolos.put("else", unToken);
        aux = AnalizadorLexico.ENDIF;
        unToken = new Token("endif", aux, "String");
        tablaSimbolos.put("endif", unToken);
        aux = AnalizadorLexico.PRINT;
        unToken = new Token("print", aux, "String");
        tablaSimbolos.put("print", unToken);
        aux = AnalizadorLexico.USLINTEGER;
        unToken = new Token("uslinteger", aux, "String");
        tablaSimbolos.put("uslinteger", unToken);
        aux = AnalizadorLexico.SINGLE;
        unToken = new Token("single", aux, "String");
        tablaSimbolos.put("single", unToken);
        aux = AnalizadorLexico.WHILE;
        unToken = new Token("while", aux, "String");
        tablaSimbolos.put("while", unToken);
        aux = AnalizadorLexico.WRITE;
        unToken = new Token("write", aux, "String");
        tablaSimbolos.put("write", unToken);
        aux = AnalizadorLexico.READONLY;
        unToken = new Token("readonly", aux, "String");
        tablaSimbolos.put("readonly", unToken);
        aux = AnalizadorLexico.PASS;
        unToken = new Token("pass", aux, "String");
        tablaSimbolos.put("pass", unToken);
        aux = AnalizadorLexico.RETURN;
        unToken = new Token("return", aux, "String");
        tablaSimbolos.put("return", unToken);
        aux = AnalizadorLexico.USLINTEGERPR;
        unToken = new Token("uslinteger", aux, "String");
        tablaSimbolos.put("uslinteger", unToken);
        aux = AnalizadorLexico.SINGLEPR;
        unToken = new Token("single", aux, "String");
        tablaSimbolos.put("single", unToken);
    }

    public Token getToken() throws IOException {
        StringBuilder buffer = new StringBuilder();
        int estadoActual = 0;
        boolean estadoFantasma = false;
        Token unToken = null;
        Token tokenAnterior = new Token("", 0, "");
        Character c;
        if ( charAnterior.charAt(0) == '0')
            c = fm.readChar();
        else
            c = charAnterior.substring(1).charAt(0);
        while (estadoActual != ESTADO_FINAL && c != null) {
            int columna = this.getEquivalente(c);
            
            int estadoFuturo = MLexica.getEstado(estadoActual, columna);
            AccionSemantica as = MLexica.getAS(estadoActual, columna);
            unToken = as.ejecutar(buffer, c);
            aumentarCantSaltosLinea(c, verificaRepetidosEnDiferentesIteraciones(buffer, tokenAnterior));
            estadoFantasma = verificaEstadoFantasma(estadoActual, estadoFuturo, c); //si paso por el estado fantasma no tiene que decrementar el cursor (son para los estados que tienen un solo caracter)
            estadoActual = estadoFuturo;
            if (estadoActual != ESTADO_FINAL || estadoFantasma) {
                c = fm.readChar();
                charAnterior = "1" + c;
                //l.info("Lei el char: '"+c+"'");
            }
        }
        if(unToken!=null)
        	AnalizadorLexico.tokens.add(unToken);
        //l.info("RETORNO EL TOKENr: '"+unToken.lexema+"'");
        return unToken;
    }

    private boolean verificaRepetidosEnDiferentesIteraciones(StringBuilder buffer, Token tokenAnterior) {
        if (tokenAnterior == null)
            return false;
        if (tokenAnterior.lexema.equals(buffer.toString())) {
            tokenAnterior.lexema = buffer.toString();// ?????
            return false;
        }
        return true;
    }

    private void aumentarCantSaltosLinea(char c, boolean repetidos) {
        if ((c == '\n' || c=='\r')&& !repetidos) {
            cantLN++;
        }
    }

    private boolean verificaEstadoFantasma(int ea, int ef, char c) {
        if (ef == -1) {
            if (ea == 18 && c == 39) //diferente de comilla simple
                return true;
            if (ea == 8 && c == 'l')
                return true;
            if (ea == 0)
                return true;
            if (ea == 12 && c == '=')
                return true;
            if (ea == 13 && c == '=')
                return true;
            if (ea == 14 && c == '=')
                return true;
            if (ea == 15 && c == '=')
                return true;
            if (ea == 17 && c == '=')
                return true;
        }
        return false;
    }
    public static Token getEntradaTS(String lexema, String ambito){
    	Token t=AnalizadorLexico.tablaSimbolos.get(lexema);
    	if(t!=null){
    		while(ambito.length()>0){
    			if(t.ambito.equals(ambito))
    				return t;
    			ambito=cortarAmbito(ambito);
    		}
    	}
    	return null;
    }
    public static String cortarAmbito(String ambito){//Recorta el ultimo ambito 
    	String cortado=ambito;
    	if(!cortado.isEmpty()){
    		while(cortado.charAt(cortado.length()-1)!='@')
    			cortado=cortado.substring(0, cortado.length()-1);
    		cortado=cortado.substring(0, cortado.length()-1);//Saco el arroba
    		return cortado;
    	}
    	return cortado;
    }
    private void cargarEquivalentes() {
        equivalentes = new HashMap<>();
        equivalentes.put("Numero", 0);
        equivalentes.put("Letra", 1);
        equivalentes.put("F", 2);
        equivalentes.put("_", 3);
        equivalentes.put("/", 4);
        equivalentes.put("*", 5);
        equivalentes.put("+", 6);
        equivalentes.put("-", 7);
        equivalentes.put("<", 8);
        equivalentes.put(">", 9);
        equivalentes.put("=", 10);
        equivalentes.put("!", 11);
        equivalentes.put("ln", 12);
        equivalentes.put("'", 13);
        equivalentes.put(".", 14);
        equivalentes.put("{", 15);
        equivalentes.put("}", 16);
        equivalentes.put("(", 17);
        equivalentes.put(")", 18);
        equivalentes.put(":", 19);
        equivalentes.put("u", 20);
        equivalentes.put("l", 21);
        equivalentes.put(",", 22);
        equivalentes.put(";", 23);
        equivalentes.put("blanco", 24);
        equivalentes.put("tab", 25);

    }
    public int getEquivalente(char c) throws IOException {//Devuelve el indice (correspondiente al caracter) de la matriz lexica
        // espacio|| tab  || salto de linea
        if (c == 32) //es un espacio?
            return equivalentes.get("blanco");
        if (c == 9) //es una tabulacion?
            return equivalentes.get("tab");
        if( c == 10 )  //es salto de linea? /n
            return equivalentes.get("ln");
        if (c == 13) {   //es salto de linea? /r
            fm.readChar();
            return equivalentes.get("ln");
        }
        if ((c >= 48) && (c <= 57)) {//Es un digito?
            return equivalentes.get("Numero");
        }                                                        // no es la  'F' , la 'u', o la 'l'
        if ((((c >= 65) && (c <= 90)) || ((c >= 97) && (c <= 122))) && ((c != 70) && (c != 117) && (c != 108))) {//Es una letra? (distinta de F,l,u)
            return equivalentes.get("Letra");
        }

        String caracter = "";
        caracter += c;//La clave es un string del caracter que recibo
        Integer indice = equivalentes.get(caracter);
        if (indice == null)
            return 26;//Si el caracter no existe en el lenguaje
        return indice;//Si es un caracter valido
    }
}