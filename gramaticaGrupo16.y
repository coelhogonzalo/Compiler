%{
package Parser;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import AnalizadorLexico.Analizador_Lexico;
import AnalizadorLexico.Token;
import AnalizadorLexico.FileManager;
import AnalizadorLexico.Main;
import AnalizadorLexico.TokenValue;
import java.util.ArrayList;
%}
%token ID CADENA USLINTEGER SINGLE IF ELSE ENDIF WHILE READONLY WRITE PASS MAYORIGUAL MENORIGUAL IGUALIGUAL DISTINTO ASIGN RETURN PRINT SINGLEPR USLINTEGERPR INVALIDO

%left '+' '-' 
%left '*' '/'

%start program

%%

program : BS
;

BS : BS sentencia
	| sentencia
	
;

sentencia : sentenciaCE
	| sentenciaDEC
	//| error ','// Esta la sacamos y si hay mas de 2 errores que ternmine la compilacion
	
;
sentenciaDEC :  tipo lista_variables ',' {Parser.estructuras.add("Se detecto la declaracion de variables en la linea "+Analizador_Lexico.cantLN+"\n");}
	| tipo ID parametrosDef cuerpofuncion {Parser.estructuras.add("Se detecto la declaracion de una funcion en la linea "+Analizador_Lexico.cantLN+"\n");
	Token t=al.tablaSimbolos.get($2.sval);
	t.declarada=true;}
	
	//ESTECOMPILA| lista_variables ',' {this.erroresGram.add(new ErrorG("Error 33: Falta definir el tipo de las variables", al.cantLN));}
	//| tipo lista_variables {this.erroresGram.add(new ErrorG("Error 32: Se esperaba una ,", al.cantLN));} shift reduce
	//ESTECOMPILA| tipo  parametrosDef cuerpofuncion {this.erroresGram.add(new ErrorG("Error 30: Falta definir el nombre de la funcion", al.cantLN));}
	//ESTECOMPILA| ID parametrosDef cuerpofuncion {this.erroresGram.add(new ErrorG("Error 31: Falta definir el tipo de la funcion", al.cantLN));}
	//ESTECOMPILA| tipo ID  cuerpofuncion {this.erroresGram.add(new ErrorG("Error 29: Falta definir los parametros de la funcion", al.cantLN));}
	//| tipo ID parametrosDef {this.erroresGram.add(new ErrorG("Error 28: Falta definir el cuerpo de la funcion", al.cantLN));} shift reduce
	
;

parametrosDef: '(' tipo ID ')'
{Token t=al.tablaSimbolos.get($3.sval);if(t.declarada==false)this.erroresGram.add(new ErrorG("Error 18: La variable "+$3.sval+" no esta declarada ", al.cantLN));}

	//ESTECOMPILA| '(' tipo ID {this.erroresGram.add(new ErrorG("Error SIN NUMERO : Falta un ) despues del identificador", al.cantLN));}
	//ESTECOMPILA| tipo ID ')' {this.erroresGram.add(new ErrorG("Error SIN NUMERO : Falta un ( antes del tipo del parametro", al.cantLN));}
;

cuerpofuncion: '{' BS retorno '}'

	//ESTECOMPILA| '{'  retorno '}' {this.erroresGram.add(new ErrorG("Error 23: Se esperaba un bloque de sentencias en el cuerpo de la funcion", al.cantLN));}
	//| '{' BS retorno 
	//ESTECOMPILA|  BS retorno '}' {this.erroresGram.add(new ErrorG("Error SIN NUMERO: Se esperaba un { al principio de la funcion", al.cantLN));}
;

retorno : RETURN expresioncparentesis
	//ESTECOMPILA|	RETURN {this.erroresGram.add(new ErrorG("Error 24: La funcion debe retornar un valor", al.cantLN));}
	//ESTECOMPILA|	 expresioncparentesis {this.erroresGram.add(new ErrorG("Error 24.5: Se esperaba un return", al.cantLN));}
;

lista_variables : lista_variables ';' ID {Token t=al.tablaSimbolos.get($3.sval);
	if(t!=null)t.declarada=true;} 
	| ID{	Token t=al.tablaSimbolos.get($1.sval);
	if(t!=null)t.declarada=true;}
;

BCE : '{' BCE2 '}'
	| sentenciaCE 
	
	//ESTECOMPILA| BCE2 '}' {this.erroresGram.add(new ErrorG("Error SIN NUMERO: Se esperaba un { al principio del bloque de sentencias", al.cantLN));}
	//ESTECOMPILA| '{' BCE2  {this.erroresGram.add(new ErrorG("Error SIN NUMERO: Se esperaba un } al final del bloque de sentencias", al.cantLN));}
;

BCE2 :  BCE2 sentenciaCE
	| sentenciaCE 
;

expresioncparentesis:'(' expresion ')'
	//ESTECOMPILA|'(' expresion  {this.erroresGram.add(new ErrorG("Error 25: Se esperaba un )", al.cantLN));}
	//ESTECOMPILA| expresion ')' {this.erroresGram.add(new ErrorG("Error 26: Se esperaba un (", al.cantLN));}
	//ESTECOMPILA|'('  ')' {this.erroresGram.add(new ErrorG("Error 27: Se esperaba una expresion", al.cantLN));}
;

printeable : '(' CADENA ')' 
		| '(' ID ')'{System.out.println("--------------------------------------------------------------------------------------------");
		System.out.println("DEBUGUEANDO: esto es un separador");
		System.out.println("--------------------------------------------------------------------------------------------");}
	//ESTECOMPILA| '(' CADENA  {this.erroresGram.add(new ErrorG("Error 21 : Falta un )", al.cantLN));}
	//ESTECOMPILA|  CADENA ')' {this.erroresGram.add(new ErrorG("Error 22 : Falta un (", al.cantLN));}
;

sentenciaCE : PRINT printeable ',' {Parser.estructuras.add("Se detecto un print en la linea "+Analizador_Lexico.cantLN+"\n");}
	| asignacion ',' {Parser.estructuras.add("Se detecto una asignacion en  la linea "+Analizador_Lexico.cantLN+"\n");}
	| IF condicioncparentesis BCE ENDIF {Parser.estructuras.add("Se detecto un if en la linea "+Analizador_Lexico.cantLN+"\n");}
	| IF condicioncparentesis BCE ELSE BCE ENDIF {Parser.estructuras.add("Se detecto un if en la linea "+Analizador_Lexico.cantLN+"\n");}
	| WHILE condicioncparentesis BCE ','{Parser.estructuras.add("Se detecto un while en la linea "+Analizador_Lexico.cantLN+"\n");}
	
	
	//| IF condicioncparentesis BCE {this.erroresGram.add(new ErrorG("Error 7: Falta un endif", al.cantLN));} LA SAQUE POR LA CORRECCION
	//| IF condicioncparentesis BCE ELSE BCE  {this.erroresGram.add(new ErrorG("Error 8: Falta un endif", al.cantLN));} LA SAQUE POR LA CORRECCION
	//ESTECOMPILA| IF condicioncparentesis ELSE BCE ENDIF {this.erroresGram.add(new ErrorG("Error 9: Se esperaba un bloque de sentencias en la rama del if", al.cantLN));}
	//ESTECOMPILA| IF condicioncparentesis BCE ELSE ENDIF {this.erroresGram.add(new ErrorG("Error 10: Se esperaba un bloque de sentencias en la rama del else", al.cantLN));}
	//ESTECOMPILA| IF condicioncparentesis ELSE ENDIF{this.erroresGram.add(new ErrorG("Error 11: Se esperaba un bloque de sentencias en la rama del if y del else", al.cantLN));}
	
	//| asignacion  {this.erroresGram.add(new ErrorG("Error 12: Se esperaba un ,", al.cantLN));} //SHIFT REDUCE
	//| PRINT printeable {this.erroresGram.add(new ErrorG("Error 13: Se esperaba un ,", al.cantLN));}//SHIFT REDUCE

; 

condicioncparentesis : '(' condicion ')'
	//ESTECOMPILA| '('  ')' {this.erroresGram.add(new ErrorG("Error14: No se definio una condicion", al.cantLN));}
	//ESTECOMPILA| '(' condicion  {this.erroresGram.add(new ErrorG("Error4: Se esperaba un ) despues de la condicion", al.cantLN));}
	//ESTECOMPILA| condicion ')' {this.erroresGram.add(new ErrorG("Error5: Se esperaba un ( antes de la condicion", al.cantLN));}
;

condicion : expresion operador_logico expresion 

//| expresion operador_logico  {this.erroresGram.add(new ErrorG("Error1: Falta la expresion del lado derecho", al.cantLN));}//SHIFT REDUCE con y sin el token error
//ESTECOMPILA| operador_logico expresion  {this.erroresGram.add(new ErrorG("Error2: Se esperaba un operador logico valido", al.cantLN));}

;

operador_logico : '<' 
	| '>'
	| MENORIGUAL
	| MAYORIGUAL
	| IGUALIGUAL
	| DISTINTO
;

asignacion : ID ASIGN expresion {	Token t=al.tablaSimbolos.get($1.sval);
	if(t.declarada==false)this.erroresGram.add(new ErrorG("Error 34 : La variable "+$1.sval+" no esta declarada ", al.cantLN));}
	
	//ESTECOMPILA| ID expresion {this.erroresGram.add(new ErrorG("Error6: Falta el operador de asignacion", al.cantLN));}//Este creo que anda si no hay otro error de los que compilancon el que hace macaana
	//ESTECOMPILA| ID ASIGN {this.erroresGram.add(new ErrorG("Error SIN NUMERO: Se esperaba una expresion del lado derecho de la asignacion", al.cantLN));}
;

expresion : expresion '+' termino
	| expresion '-' termino
	| termino
;

termino : termino '*' factor
	| termino '/' factor
	| factor
;

factor : ID 
	| USLINTEGER 
	| SINGLE 
	| '-' SINGLE {	Token t=al.tablaSimbolos.get($2.sval);
	t.lexema="-"+t.lexema;}
	|ID parametros ','  {Parser.estructuras.add("Se detecto la invocacion de una funcion en la linea "+Analizador_Lexico.cantLN+"\n");}
;
parametros: '(' ID ';' lista_permisos ')' {Token t=al.tablaSimbolos.get($1.sval);
	if(t!=null&&t.declarada==false)this.erroresGram.add(new ErrorG("Error 35: La variable "+$2.sval+" no esta declarada ", al.cantLN));}

//ESTECOMPILA| ID ';' lista_permisos ')'  {this.erroresGram.add(new ErrorG("Error 15: Se esperaba un (", al.cantLN));}
//ESTECOMPILA|'(' ID ';' lista_permisos   {this.erroresGram.add(new ErrorG("Error 16: Se esperaba un )", al.cantLN));}
//ESTECOMPILA|'(' ID  lista_permisos ')'  {this.erroresGram.add(new ErrorG("Error 17: Se esperaba un ;", al.cantLN));}
;

tipo : USLINTEGERPR
	| SINGLEPR
;

lista_permisos : READONLY
	| WRITE
	| PASS
	| WRITE ';' PASS
	                                                                                                                                                                                
	//ESTECOMPILA| WRITE PASS {this.erroresGram.add(new ErrorG("Error3: Se esperaba un ; entre los permisos", al.cantLN));}
;


%%

public Analizador_Lexico al;
public ArrayList<AnalizadorLexico.Error> erroresGram;
public static TokenValue ultimoTokenleido;
public static ArrayList<String> estructuras;
public int yylex(){
	Token t=null;
	try {
		t = this.al.getToken();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println("IOException  en el metodo getToken");
	}
	if(t!=null){
		yylval = new ParserVal(t.lexema);
		TokenValue tv = new TokenValue (t.getLexema(), Analizador_Lexico.cantLN);
		ultimoTokenleido=tv;
		System.out.println("leyo : "+t.lexema+" 	Numero de token: "+t.getNro());
		return t.getNro();
	}
	return 0;
}

public void yyerror ( String e){
	System.out.println(e);
}
public int parsepublico(){
	return this.yyparse();
}
public static void main(String [] args) throws IOException{
	//BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    //File f = new File(reader.readLine());
	File f = new File("prueba.txt");
    Analizador_Lexico lexico = null;
    ArrayList<AnalizadorLexico.Error> erroresGram = null;
    ArrayList<Token> tokens = null;
    try {
        lexico = new Analizador_Lexico(f);
        tokens = Main.getAllTokens(lexico);
        erroresGram = new ArrayList<>();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
	Analizador_Lexico al=new Analizador_Lexico(f);
	Parser p=new Parser(false);
	p.erroresGram=erroresGram;
	Parser.estructuras=new ArrayList<>();
	p.al=al;
	int unint=p.parsepublico();
	if(unint==0)
		System.out.println("ACCEPT, se reconocio la gramatica");
	else
		System.out.println("n");
	
	File fErroresOut = new File("S_Errores.txt");
	FileManager.write(erroresGram.toString(), fErroresOut);
    File fTokensOut = new File("S_Tokens.txt");
    FileManager.write(tokens.toString(), fTokensOut);
	File estructurasOut = new File("Estructuras.txt");
	FileManager.write(Parser.estructuras.toString(), estructurasOut);
}


