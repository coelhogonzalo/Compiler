%{
package Parser;
import java.io.IOException;


import AnalizadorLexico.Analizador_Lexico;
import AnalizadorLexico.Token;
import AnalizadorLexico.TokenValue;
import java.util.ArrayList;
import GeneracionCodigoIntermedio.Polaca_Inversa;
import java.util.Arrays;
%}
%token ID CADENA USLINTEGER SINGLE IF ELSE ENDIF WHILE READONLY WRITE PASS MAYORIGUAL MENORIGUAL IGUALIGUAL DISTINTO ASIGN RETURN PRINT SINGLEPR USLINTEGERPR INVALIDO

%left '+' '-' 
%left '*' '/'

%start program

%%

program : BS
;

BS : BS sentencia {
    if ( isPermited($1.sval, $2.sval) ){
        $$.sval = $2.sval;
    }else
        $$.sval = $1.sval;
 }
	| sentencia { $$.sval = $1.sval; }
	
;

sentencia : sentenciaCE { $$.sval = $1.sval; }
	| sentenciaDEC { $$.sval = $1.sval; }
	//| error ','// Esta la sacamos y si hay mas de 2 errores que ternmine la compilacion
	
;

sentenciaDEC :  tipo lista_variables ',' { $$.sval = "noseusaelparametro"; registrarTipo( $2.sval, $1.sval); //System.out.println($2.sval);
Parser.estructuras.add("Se detecto la declaracion de variables en la linea "+Analizador_Lexico.cantLN+"\r\n");}
	| tipoFunID parametrosDef cuerpofuncion { Analizador_Lexico.tablaSimbolos.get($1.sval).permisoFun = $3.sval; Parser.estructuras.add("Se detecto la declaracion de una funcion en la linea "+Analizador_Lexico.cantLN+"\r\n");}
	
	//ESTECOMPILA| lista_variables ',' {this.errores.add(new ErrorG("Error 33: Falta definir el tipo de las variables", Analizador_Lexico.cantLN));}
	//| tipo lista_variables {this.errores.add(new ErrorG("Error 32: Se esperaba una ,", Analizador_Lexico.cantLN));} shift reduce
	//ESTECOMPILA| tipo  parametrosDef cuerpofuncion {this.errores.add(new ErrorG("Error 30: Falta definir el nombre de la funcion", Analizador_Lexico.cantLN));}
	//ESTECOMPILA| ID parametrosDef cuerpofuncion {this.errores.add(new ErrorG("Error 31: Falta definir el tipo de la funcion", Analizador_Lexico.cantLN));}
	//ESTECOMPILA| tipo ID  cuerpofuncion {this.errores.add(new ErrorG("Error 29: Falta definir los parametros de la funcion", Analizador_Lexico.cantLN));}
	//| tipo ID parametrosDef {this.errores.add(new ErrorG("Error 28: Falta definir el cuerpo de la funcion", Analizador_Lexico.cantLN));} shift reduce
	
;

tipoFunID : tipo ID { this.idFun = $2.sval; Token t=Analizador_Lexico.tablaSimbolos.get($2.sval);
	if(t.declarada){
		this.errores.add(new ErrorG("Error SIN NUMERO: El identificador '"+t.lexema+"' ,de uso '"+t.uso+"' ya esta declarado", Analizador_Lexico.cantLN));
		PI.inicioFuncion($2.sval);
	}
	else{
		t.declarada=true;
		PI.inicioFuncion($2.sval);
		t.uso="funcion";
		t.tipo=$1.sval;
	    }
	}

;

parametrosDef: '(' tipo ID ')' { PI.paramFun($3.sval);
Token t=Analizador_Lexico.tablaSimbolos.get($3.sval);
	if(t!=null){
		t.uso="parametro";
		t.declarada=true;
	}
	else
		System.out.println("El token que quisiste recuperar es null (ndmpp)");
this.idParam = $3.sval; }
	//ESTECOMPILA| '(' tipo ID {this.errores.add(new ErrorG("Error SIN NUMERO : Falta un ) despues del identificador", Analizador_Lexico.cantLN));}
	//ESTECOMPILA| tipo ID ')' {this.errores.add(new ErrorG("Error SIN NUMERO : Falta un ( antes del tipo del parametro", Analizador_Lexico.cantLN));}
;

cuerpofuncion: '{' BSFuncion retorno '}' { $$.sval = $2.sval; }

	//ESTECOMPILA| '{'  retorno '}' {this.errores.add(new ErrorG("Error 23: Se esperaba un bloque de sentencias en el cuerpo de la funcion", Analizador_Lexico.cantLN));}
	//| '{' BS retorno
	//ESTECOMPILA|  BS retorno '}' {this.errores.add(new ErrorG("Error SIN NUMERO: Se esperaba un { al principio de la funcion", Analizador_Lexico.cantLN));}
;
BSFuncion : BSFuncion sentenciaFuncion {
    if ( isPermited($1.sval, $2.sval) )
        $$.sval = $2.sval;
    else
        $$.sval = $1.sval;
 }
	| sentenciaFuncion { $$.sval = $1.sval; }
;
sentenciaFuncion : sentenciaCE { $$.sval = $1.sval; }
	| sentenciaDECFuncion { $$.sval = $1.sval; }
;
sentenciaDECFuncion :  tipo lista_variables ',' { $$.sval = "noseusaelparametro"; registrarTipo( $2.sval, $1.sval); //System.out.println($2.sval);
Parser.estructuras.add("Se detecto la declaracion de variables en la linea "+Analizador_Lexico.cantLN+"\r\n");}
	//ESTECOMPILA| lista_variables ',' {this.errores.add(new ErrorG("Error 33: Falta definir el tipo de las variables", Analizador_Lexico.cantLN));}
	//| tipo lista_variables {this.errores.add(new ErrorG("Error 32: Se esperaba una ,", Analizador_Lexico.cantLN));} shift reduce
	| tipoFunID parametrosDef cuerpofuncion { this.errores.add(new ErrorG("Error SIN NUMERO: Se declar� una funcion dentro de otra funcion", Analizador_Lexico.cantLN));}
	//ESTECOMPILA| tipo  parametrosDef cuerpofuncion {this.errores.add(new ErrorG("Error 30: Falta definir el nombre de la funcion", Analizador_Lexico.cantLN));}
	//ESTECOMPILA| ID parametrosDef cuerpofuncion {this.errores.add(new ErrorG("Error 31: Falta definir el tipo de la funcion", Analizador_Lexico.cantLN));}
	//ESTECOMPILA| tipo ID  cuerpofuncion {this.errores.add(new ErrorG("Error 29: Falta definir los parametros de la funcion", Analizador_Lexico.cantLN));}
	//| tipo ID parametrosDef {this.errores.add(new ErrorG("Error 28: Falta definir el cuerpo de la funcion", Analizador_Lexico.cantLN));} shift reduce
;

retorno : RETURN expresioncparentesis {PI.finFuncion(); }//el del return tengo qeu hacer lo que dijo anto
	//ESTECOMPILA|	RETURN {this.errores.add(new ErrorG("Error 24: La funcion debe retornar un valor", Analizador_Lexico.cantLN));}
	//ESTECOMPILA|	 expresioncparentesis {this.errores.add(new ErrorG("Error 24.5: Se esperaba un return", Analizador_Lexico.cantLN));}
;

lista_variables : lista_variables ';' ID {Token t=Analizador_Lexico.tablaSimbolos.get($3.sval);
	if(t!=null){
		if(t.declarada==false&&t.uso!="parametro"){
			t.declarada=true;
			t.uso="variable";
		}
		else	
			this.errores.add(new ErrorG("Error SIN NUMERO: Se redeclaro el identificador de uso "+t.uso+" :'"+t.lexema+"' ", Analizador_Lexico.cantLN));
		$$=new ParserVal($$.sval+" "+$3.sval);
	}
	else
		System.out.println("El token que quisiste recuperar es null");}
	| ID{	Token t=Analizador_Lexico.tablaSimbolos.get($1.sval);
	if(t!=null){
		if(t.declarada==false&&t.uso!="parametro"){
			t.declarada=true;
			t.uso="variable";
		}
		else 
			this.errores.add(new ErrorG("Error SIN NUMERO: Se redeclaro el identificador de tipo "+t.uso+" :'"+t.lexema+"' ", Analizador_Lexico.cantLN));
		$$=new ParserVal($$.sval+" "+$1.sval);
	}
	else
		System.out.println("El token que quisiste recuperar es null");
	}
;

BCE : '{' BCE2 '}'
	| sentenciaCE 
	
	//ESTECOMPILA| BCE2 '}' {this.errores.add(new ErrorG("Error SIN NUMERO: Se esperaba un { al principio del bloque de sentencias", Analizador_Lexico.cantLN));}
	//ESTECOMPILA| '{' BCE2  {this.errores.add(new ErrorG("Error SIN NUMERO: Se esperaba un } al final del bloque de sentencias", Analizador_Lexico.cantLN));}
;

BCE2 :  BCE2 sentenciaCE
	| sentenciaCE 
;

expresioncparentesis:'(' expresion ')'
	//ESTECOMPILA|'(' expresion  {this.errores.add(new ErrorG("Error 25: Se esperaba un )", Analizador_Lexico.cantLN));}
	//ESTECOMPILA| expresion ')' {this.errores.add(new ErrorG("Error 26: Se esperaba un (", Analizador_Lexico.cantLN));}
	//ESTECOMPILA|'('  ')' {this.errores.add(new ErrorG("Error 27: Se esperaba una expresion", Analizador_Lexico.cantLN));}
;

printeable : '(' CADENA ')' 		{ PI.put($2.sval); }
		| '(' ID ')'	{ if ( $2.sval == this.idParam )
		                    $$.sval = "readonly";
		                  else
		                    $$.sval = "noseusaelparametro";
		PI.put($2.sval); } {System.out.println("--------------------------------------------------------------------------------------------");
		System.out.println("DEBUGUEANDO: esto es un separador");
		System.out.println("--------------------------------------------------------------------------------------------");}
	//ESTECOMPILA| '(' CADENA  {this.errores.add(new ErrorG("Error 21 : Falta un )", Analizador_Lexico.cantLN));}
	//ESTECOMPILA|  CADENA ')' {this.errores.add(new ErrorG("Error 22 : Falta un (", Analizador_Lexico.cantLN));}
;

sentenciaCE : PRINT printeable ',' { $$.sval = $2.sval; Parser.estructuras.add("Se detecto un print en la linea "+Analizador_Lexico.cantLN+"\r\n"); PI.put("print");}
	| asignacion ',' {  $$.sval = $1.sval; Parser.estructuras.add("Se detecto una asignacion en  la linea "+Analizador_Lexico.cantLN+"\r\n");}
	| ifcond BCE ENDIF { $$.sval = $2.sval; Parser.estructuras.add("Se detecto un if en la linea "+Analizador_Lexico.cantLN+"\r\n"); PI.desapilar(); }
	| ifcond BCE elsecond BCE ENDIF { if ( isPermited($2.sval, $4.sval) ) $$.sval = $4.sval; Parser.estructuras.add("Se detecto un if en la linea "+Analizador_Lexico.cantLN+"\r\n"); PI.desapilar();}
	| whilecond BCE { $$.sval = $2.sval; Parser.estructuras.add("Se detecto un while en la linea "+Analizador_Lexico.cantLN+"\r\n"); PI.saltoIncond(); PI.desapilar(); }
	
	//ESTECOMPILA| IF condicioncparentesis ELSE BCE ENDIF {this.errores.add(new ErrorG("Error 9: Se esperaba un bloque de sentencias en la rama del if", Analizador_Lexico.cantLN));}
	//ESTECOMPILA| IF condicioncparentesis BCE ELSE ENDIF {this.errores.add(new ErrorG("Error 10: Se esperaba un bloque de sentencias en la rama del else", Analizador_Lexico.cantLN));}
	//ESTECOMPILA| IF condicioncparentesis ELSE ENDIF{this.errores.add(new ErrorG("Error 11: Se esperaba un bloque de sentencias en la rama del if y del else", Analizador_Lexico.cantLN));}
	
	//| asignacion  {this.errores.add(new ErrorG("Error 12: Se esperaba un ,", Analizador_Lexico.cantLN));} //SHIFT REDUCE
	//| PRINT printeable {this.errores.add(new ErrorG("Error 13: Se esperaba un ,", Analizador_Lexico.cantLN));}//SHIFT REDUCE

;

ifcond : IF condicioncparentesis  { PI.bifurcacion(); }

;

elsecond : ELSE  { PI.desapilarElse(); PI.bifurcacionElse(); } //ver este si lo hace bien

;

whilecond : whileparaapilar condicioncparentesis { PI.bifurcacion(); }

;

whileparaapilar: WHILE { PI.setSaltoIncond(); }

;



condicioncparentesis : '(' condicion ')'
	//ESTECOMPILA| '('  ')' {this.errores.add(new ErrorG("Error14: No se definio una condicion", Analizador_Lexico.cantLN));}
	//ESTECOMPILA| '(' condicion  {this.errores.add(new ErrorG("Error4: Se esperaba un ) despues de la condicion", Analizador_Lexico.cantLN));}
	//ESTECOMPILA| condicion ')' {this.errores.add(new ErrorG("Error5: Se esperaba un ( antes de la condicion", Analizador_Lexico.cantLN));}
;




condicion : expresion operador_logico expresion { PI.put($2.sval); }

//| expresion operador_logico  {this.errores.add(new ErrorG("Error1: Falta la expresion del lado derecho", Analizador_Lexico.cantLN));}//SHIFT REDUCE con y sin el token error
//ESTECOMPILA| operador_logico expresion  {this.errores.add(new ErrorG("Error2: Se esperaba un operador logico valido", Analizador_Lexico.cantLN));}

;

operador_logico : '<' 		{ $$.sval = "<"; }
	| '>'				    { $$.sval = ">"; }
	| MENORIGUAL			{ $$.sval = "<="; }
	| MAYORIGUAL			{ $$.sval = ">="; }
	| IGUALIGUAL			{ $$.sval = "=="; }
	| DISTINTO			    { $$.sval = "!="; }
	
;

asignacion : ID ASIGN expresion {
if ( $1.sval == this.idParam ) {
    if ( isPermited("write", $3.sval) )
        $$.sval = $3.sval;
}
else {
    if ( isPermited("noseusaelparametro", $3.sval) )
       $$.sval = $3.sval;
}
 Token t=Analizador_Lexico.tablaSimbolos.get($1.sval); PI.put($1.sval); PI.put(":=");
	if(t!=null){
		if(t.declarada==false)
			this.errores.add(new ErrorG("Error 34 : La variable "+$1.sval+" no esta declarada ", Analizador_Lexico.cantLN));
	}
	else
		System.out.println("El identificador "+$1.sval+" no se agrego a la tabla de simbolos (ndmpp)");
	}
	
	//ESTECOMPILA| ID expresion {this.errores.add(new ErrorG("Error6: Falta el operador de asignacion", Analizador_Lexico.cantLN));}//Este creo que anda si no hay otro error de los que compilancon el que hace macaana
	//ESTECOMPILA| ID ASIGN {this.errores.add(new ErrorG("Error SIN NUMERO: Se esperaba una expresion del lado derecho de la asignacion", Analizador_Lexico.cantLN));}
;

expresion : expresion '+' termino	{ if ( isPermited($1.sval, $3.sval) ) $$.sval = $3.sval; else         $$.sval = $1.sval ; PI.put("+"); }
	| expresion '-' termino		{ if ( isPermited($1.sval, $3.sval) ) $$.sval = $3.sval; else         $$.sval = $1.sval ; PI.put("-"); }
	| termino { $$.sval = $1.sval; }
;

termino : termino '*' factor		{ if ( isPermited($1.sval, $3.sval) ) $$.sval = $3.sval; else         $$.sval = $1.sval ; PI.put("*"); }
	| termino '/' factor		{ if ( isPermited($1.sval, $3.sval) ) $$.sval = $3.sval; else         $$.sval = $1.sval ; PI.put("/"); }
	| factor { $$.sval = $1.sval; }
;

factor : ID 				{ if ( idParam == $1.sval) $$.sval = "readonly"; else $$.sval = "noseusaelparametro"; PI.put($1.sval); }
	| USLINTEGER 			{ $$.sval = "noseusaelparametro"; PI.put($1.sval); }
	| SINGLE 			    { $$.sval = "noseusaelparametro"; PI.put($1.sval); }
	| '-' SINGLE {	Token t=Analizador_Lexico.tablaSimbolos.get($2.sval);
	t.lexema="-"+t.lexema; PI.put("-" + $1.sval);}
	|ID parametros { System.out.println(Analizador_Lexico.tablaSimbolos.get($1.sval).permisoFun + " versus " + $2.sval);
	                if ( !isPermited(Analizador_Lexico.tablaSimbolos.get($1.sval).permisoFun, $2.sval) )
	                    new ErrorG("Error asignacion de permisos", Analizador_Lexico.cantLN);
                    else{
                        //System.out.println("Permiso aceptado");
					}
	PI.jumpToFun($1.sval); Token t=Analizador_Lexico.tablaSimbolos.get($1.sval);
	if(t!=null){
		if(t.declarada==false)
			this.errores.add(new ErrorG("Error 34.6 : La funcion "+$1.sval+" no esta declarada ", Analizador_Lexico.cantLN));
		else
			if(t.uso!="funcion")
				this.errores.add(new ErrorG("Error 34.7 : El identificador "+t.lexema+" no es una funcion. ", Analizador_Lexico.cantLN));
	}
	else
		System.out.println("El identificador "+$1.sval+" no se agrego a la tabla de simbolos (El identificador es una funcion) (ndmpp)"); }
;

parametros: '(' ID ';' lista_permisos ')' { $$.sval = $4.sval; PI.put($2.sval);
Token t=Analizador_Lexico.tablaSimbolos.get($2.sval);
	if(t!=null){
		if(t.declarada==false)
			this.errores.add(new ErrorG("Error 35: La variable "+$2.sval+" no esta declarada ", Analizador_Lexico.cantLN));
	}
	else
		System.out.println("El identificador "+$2.sval+" no se agrego a la tabla de simbolos (ndmpp)");
	}

//ESTECOMPILA| ID ';' lista_permisos ')'  {this.errores.add(new ErrorG("Error 15: Se esperaba un (", Analizador_Lexico.cantLN));}
//ESTECOMPILA|'(' ID ';' lista_permisos   {this.errores.add(new ErrorG("Error 16: Se esperaba un )", Analizador_Lexico.cantLN));}
//ESTECOMPILA|'(' ID  lista_permisos ')'  {this.errores.add(new ErrorG("Error 17: Se esperaba un ;", Analizador_Lexico.cantLN));}
;

tipo : USLINTEGERPR
	| SINGLEPR
;

lista_permisos : READONLY
	| WRITE
	| PASS
	| WRITE ';' PASS
	|
	                                                                                                                                                                                
	//ESTECOMPILA| WRITE PASS {this.errores.add(new ErrorG("Error3: Se esperaba un ; entre los permisos", Analizador_Lexico.cantLN));}
;


%%

public Polaca_Inversa PI = new Polaca_Inversa();
public Analizador_Lexico al;
public ArrayList<AnalizadorLexico.Error> errores;
public static TokenValue ultimoTokenleido;
public static ArrayList<String> estructuras;
public String idFun;
public String idParam;


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
		TokenValue tv = new TokenValue (t.lexema, Analizador_Lexico.cantLN);
		ultimoTokenleido=tv;
		//System.out.println("leyo : "+t.lexema+" 	Numero de token: "+t.nro);
		return t.nro;
	}
	return 0;
}

public void yyerror ( String e){
	System.out.println(e);
}
public int parsepublico(){
	return this.yyparse();
}
public void registrarTipo(String listaVariables,String tipo){
	ArrayList<String> items= new ArrayList<String>(Arrays.asList(listaVariables.split(" ")));
	for (String item:items){
		//System.out.println(items.size()+" El identificador es :'"+item+"'");
		Token t=Analizador_Lexico.tablaSimbolos.get(item);
		t.tipo=tipo;
	}
	/*while(pos<listaVariables.length()){
		char nuevoChar=listaVariables.charAt(pos);
		if(nuevoChar!=' '){
			idVariable=idVariable+nuevoChar;
			System.out.println(idVariable);
		}
		else{
			Token t=Analizador_Lexico.tablaSimbolos.get(idVariable);
			t.tipo=tipo;
			idVariable="";
		}
	}*/
}
public static boolean  isPermited(String permisoFuncion,String permisoInvocacion){
    	if (permisoFucion.equals("noseusaelparametro"))
    	    return false;
    	if (permisoFuncion.equals

    	if(permisoFuncion.equals(permisoInvocacion))
    		return true;
    	if(permisoInvocacion.length()>5)
    	if(permisoFuncion.equals(permisoInvocacion.substring(0, 5)))//Se fija si permisoInvocacion tiene un write y permisoFuncion tambien lo tiene
    		return true;
    	if(permisoInvocacion.length()>6)
    		if(permisoFuncion.equals(permisoInvocacion.substring(6)))//Se fija si permisoInvocacion tiene un pass y permisoFuncion tambien lo tiene
    			return true;
    	if(permisoFuncion=="noseusaelparametro")
    		return true;
    	if(permisoFuncion!="readonly"&&permisoInvocacion=="readonly")
    		return true;
    	return false;
    }  /* Lo comento asi no genera codigo
    public static void testing_isPermited() {
    	System.out.println("");
    	System.out.println("Testing con readonly en la funcion:");
    	System.out.println("");
    	if(isPermited("readonly","readonly"))
    		System.out.println("Recibi un readonly y la funcion tenia un readonly, lo acepte");
    	if(!isPermited("readonly","pass"))
    		System.out.println("Recibi un pass y la funcion tenia readonly, RECHAZADO");
    	if(!isPermited("readonly","write"))
    		System.out.println("Recibi un write y la funcion tenia readonly, RECHAZADO");
    	if(!isPermited("readonly","write;pass"))
    		System.out.println("Recibi un write;pass y la funcion tenia readonly, RECHAZADO");
    	System.out.println("");
    	System.out.println("Testing con pass en la funcion:");
    	System.out.println("");
    	if(isPermited("pass","pass"))
    		System.out.println("Recibi un pass y la funcion tenia un pass, lo acepte");
    	if(isPermited("pass","readonly"))
    		System.out.println("Recibi un readonly y la funcion tenia pass, lo acepte");
    	if(!isPermited("pass","write"))
    		System.out.println("Recibi un write y la funcion tenia pass, RECHAZADO");
    	if(isPermited("pass","write;pass"))
    		System.out.println("Recibi un write;pass y la funcion tenia pass, lo acepte");
    	System.out.println("");
    	System.out.println("Testing con write en la funcion:");
    	System.out.println("");
    	if(isPermited("write","write"))
    		System.out.println("Recibi un write y la funcion tenia write, lo acepte");
    	if(!isPermited("write","pass"))
    		System.out.println("Recibi un pass y la funcion tenia un write, RECHAZADO");
    	if(isPermited("write","readonly"))
    		System.out.println("Recibi un readonly y la funcion tenia write, lo acepte");
    	if(isPermited("write","write;pass"))
    		System.out.println("Recibi un write;pass y la funcion tenia write, lo acepte");
    	System.out.println("");
    	System.out.println("Testing con write;pass en la funcion:");
    	System.out.println("");
    	if(!isPermited("write;pass","write"))
    		System.out.println("Recibi un write y la funcion tenia write;pass, RECHAZADO");
    	if(!isPermited("write;pass","pass"))
    		System.out.println("Recibi un pass y la funcion tenia un write;pass, RECHAZADO");
    	if(isPermited("write;pass","readonly"))
    		System.out.println("Recibi un readonly y la funcion tenia write;pass, lo acepte");
    	if(isPermited("write;pass","write;pass"))
    		System.out.println("Recibi un write;pass y la funcion tenia write;pass, lo acepte");
    	System.out.println("");
    	System.out.println("Testing con noseusaelparametro en la funcion:");
    	System.out.println("");
    	if(isPermited("noseusaelparametro","readonly"))
    		System.out.println("Recibi un readonly y la funcion tenia un noseusaelparametro, lo acepte");
    	if(isPermited("noseusaelparametro","pass"))
    		System.out.println("Recibi un pass y la funcion tenia noseusaelparametro, lo acepte");
    	if(isPermited("noseusaelparametro","write"))
    		System.out.println("Recibi un write y la funcion tenia noseusaelparametro, lo acepte");
    	if(isPermited("noseusaelparametro","write;pass"))
    		System.out.println("Recibi un write;pass y la funcion tenia noseusaelparametro, lo acepte");
    }*/
	



