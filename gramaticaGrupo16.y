%{
package Parser;
import java.io.IOException;
import AnalizadorLexico.Analizador_Lexico;
import AnalizadorLexico.Token;
import AnalizadorLexico.TokenValue;
import java.util.ArrayList;
import GeneracionCodigoIntermedio.Polaca_Inversa;
import java.util.Arrays;
import java.util.HashMap;
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
;


sentenciaDEC :  tipo lista_variables ',' { registrarTipo( $2.sval, $1.sval); //System.out.println($2.sval);
Parser.estructuras.add("Se detecto la declaracion de variables en la linea "+Analizador_Lexico.cantLN+"\r\n");}
	| tipoFunID parametrosDef cuerpofuncion { parametrosFunciones.put($1.sval,$2.sval);
	Parser.estructuras.add("Se detecto la declaracion de una funcion en la linea "+Analizador_Lexico.cantLN+"\r\n");}

	| lista_variables ',' {this.errores.add(new ErrorG("Error 001: Falta definir el tipo de las variables", Analizador_Lexico.cantLN));}
	| tipoFunID cuerpofuncion {this.errores.add(new ErrorG("Error 004: Falta definir los parametros de la funcion", Analizador_Lexico.cantLN));}

;

tipoFunID : tipo ID { estoyEnFuncion = true; $$=$2;//Le paso el nombre de la funcion para arriba
                idFun = $2.sval;
 Token t=Analizador_Lexico.tablaSimbolos.get($2.sval); 
	if(t.declarada){
		this.errores.add(new ErrorG("Error SIN NUMERO: El identificador '"+t.lexema+"' ,de uso '"+t.uso+"' ya esta declarado", Analizador_Lexico.cantLN));
		PI.inicioFuncion($2.sval);
	}
	else{
		t.declarada=true;
		PI.inicioFuncion($2.sval);
		t.uso="funcion";
		t.tipo=$1.sval;
		t.ambito=ambitoActual;
	    }
	this.ambitoActual=this.ambitoActual+"@"+$2.sval;	
	}
	| tipo {PI.inicioFuncion("NOMBRE_FALTANTE"); this.ambitoActual=this.ambitoActual+"@"+"NOMBRE_FALTANTE";
	this.errores.add(new ErrorG("Error 002: Falta definir el nombre de la funcion", Analizador_Lexico.cantLN));}
	| ID {PI.inicioFuncion($1.sval); this.ambitoActual=this.ambitoActual+"@"+$1.sval;
	this.errores.add(new ErrorG("Error 003: Falta definir el tipo de la funcion", Analizador_Lexico.cantLN));}
;

parametrosDef: '(' tipo ID ')' { $$=$2;//Le paso el tipo del parametro para arriba
                idParam = $3.sval;
PI.paramFun($3.sval); 
Token t=Analizador_Lexico.tablaSimbolos.get($3.sval);
	if(t!=null){
		if(!t.declarada){
			t.uso="parametro";
			t.declarada=true;
			t.ambito=this.ambitoActual;
			t.tipo=$2.sval;
		}
		else
			this.errores.add(new ErrorG("Error 009 : El identificador '"+t.lexema+"' de tipo '"+t.uso+"' no puede ser redeclarado", Analizador_Lexico.cantLN));
	}
	else
		System.out.println("El token que quisiste recuperar es null (ndmpp)"); }
	| '(' tipo ID {idParam = $3.sval;PI.paramFun($3.sval);
	this.errores.add(new ErrorG("Error 005: Falta un ) despues del identificador", Analizador_Lexico.cantLN));}
	| tipo ID ')' {idParam = $2.sval;PI.paramFun($2.sval);
	this.errores.add(new ErrorG("Error 006 : Falta un ( antes del tipo del parametro", Analizador_Lexico.cantLN));}
;

cuerpofuncion: '{' BSFuncion retorno '}' {

    this.ambitoActual=Analizador_Lexico.cortarAmbito(this.ambitoActual);}

;
BSFuncion : BSFuncion sentenciaFuncion
	|
;
sentenciaFuncion : sentenciaCE
	| sentenciaDECFuncion
;
sentenciaDECFuncion :  tipo lista_variables ',' { registrarTipo( $2.sval, $1.sval); 
Parser.estructuras.add("Se detecto la declaracion de variables en la linea "+Analizador_Lexico.cantLN+"\r\n");}

	| lista_variables ',' {this.errores.add(new ErrorG("Error 001: Falta definir el tipo de las variables", Analizador_Lexico.cantLN));}
	| tipoFunID parametrosDef cuerpofuncion { this.errores.add(new ErrorG("Error 007: Se declaro una funcion dentro de otra funcion", Analizador_Lexico.cantLN));}
;

retorno : RETURN expresioncparentesis { estoyEnFuncion = false; PI.put("return");
                idFun = "None";
 }
	|	RETURN {this.errores.add(new ErrorG("Error 008: La funcion debe retornar un valor", Analizador_Lexico.cantLN));}
	//ME ARRUINA LA DE LA COMA|	expresioncparentesis {this.errores.add(new ErrorG("Error 009: Se esperaba un return", Analizador_Lexico.cantLN));}
;

lista_variables : lista_variables ';' ID {Token t=Analizador_Lexico.tablaSimbolos.get($3.sval);
	if(t!=null){
		if(t.declarada==false&&t.uso!="parametro"){
			t.declarada=true;
			t.uso="variable";
			t.ambito=this.ambitoActual;
		}
		else
			this.errores.add(new ErrorG("Error SIN NUMERO: Se redeclaro el identificador de uso "+t.uso+" :'"+t.lexema+"' ", Analizador_Lexico.cantLN));
		$$.sval = $$.sval+" "+ $3.sval;
	}
	else
		System.out.println("El token que quisiste recuperar es null");}
	| ID{	Token t=Analizador_Lexico.tablaSimbolos.get($1.sval);
	if(t!=null){
		if(t.declarada==false&&t.uso!="parametro"){
			t.declarada=true;
			t.uso="variable";
			t.ambito=this.ambitoActual;
		}
		else
			this.errores.add(new ErrorG("Error SIN NUMERO: Se redeclaro el identificador de tipo "+t.uso+" :'"+t.lexema+"' ", Analizador_Lexico.cantLN));
		$$.sval = $$.sval+" "+ $1.sval;
	}
	else
		System.out.println("El token que quisiste recuperar es null");
	}
;

BCE : '{' BCE2 '}'
	| sentenciaCE

;

BCE2 :  BCE2 sentenciaCE
	| sentenciaCE
;

expresioncparentesis:'(' expresion ')'
	|'(' expresion  {this.errores.add(new ErrorG("Error 010: Se esperaba un )", Analizador_Lexico.cantLN));}
	|'('  ')' {this.errores.add(new ErrorG("Error 011: Se esperaba una expresion", Analizador_Lexico.cantLN));}
;

printeable : '(' CADENA ')' 		{ PI.put($2.sval); }
		| '(' ID ')'	{
		PI.put($2.sval); }
	| '(' CADENA  {this.errores.add(new ErrorG("Error 012 : Falta un )", Analizador_Lexico.cantLN));}
	|  CADENA ')' {this.errores.add(new ErrorG("Error 013 : Falta un (", Analizador_Lexico.cantLN));}
;

sentenciaCE : PRINT printeable ',' { Parser.estructuras.add("Se detecto un print en la linea "+Analizador_Lexico.cantLN+"\r\n"); PI.put("print");}
	| asignacion ',' { Parser.estructuras.add("Se detecto una asignacion en  la linea "+Analizador_Lexico.cantLN+"\r\n");}
	| ifcond BCE ENDIF { Parser.estructuras.add("Se detecto un if en la linea "+Analizador_Lexico.cantLN+"\r\n"); PI.desapilar(); }
	| ifcond BCE elsecond BCE ENDIF { Parser.estructuras.add("Se detecto un if en la linea "+Analizador_Lexico.cantLN+"\r\n"); PI.desapilar();}
	| whilecond BCE  { Parser.estructuras.add("Se detecto un while en la linea "+Analizador_Lexico.cantLN+"\r\n"); PI.saltoIncond(); PI.desapilar(); }
	
	

	|ifcond  ENDIF{this.errores.add(new ErrorG("Error 015: Se esperaba un bloque de sentencias en la rama del if", Analizador_Lexico.cantLN));}
	| ifcond  elsecond BCE ENDIF  {this.errores.add(new ErrorG("Error 015: Se esperaba un bloque de sentencias en la rama del if", Analizador_Lexico.cantLN));}
	| ifcond BCE elsecond  ENDIF {this.errores.add(new ErrorG("Error 016: Se esperaba un bloque de sentencias en la rama del else", Analizador_Lexico.cantLN));}
	| ifcond  elsecond  ENDIF{this.errores.add(new ErrorG("Error 017: Se esperaba un bloque de sentencias en la rama del if y del else", Analizador_Lexico.cantLN));}

	| PRINT printeable {this.errores.add(new ErrorG("Error 014: Se esperaba un ,", Analizador_Lexico.cantLN));}
	| asignacion  {this.errores.add(new ErrorG("Error 014: Se esperaba un ,", Analizador_Lexico.cantLN));}
;

ifcond : IF condicioncparentesis  { PI.bifurcacion(); }

;

elsecond : ELSE  { PI.desapilarElse(); PI.bifurcacionElse(); }

;

whilecond : whileparaapilar condicioncparentesis { PI.bifurcacion(); }

;

whileparaapilar: WHILE { PI.setSaltoIncond(); }

;



condicioncparentesis : '(' condicion ')'
	| '('  ')' {this.errores.add(new ErrorG("Error 018: No se definio una condicion", Analizador_Lexico.cantLN));}
	| '(' condicion  {this.errores.add(new ErrorG("Error 019: Se esperaba un ) despues de la condicion", Analizador_Lexico.cantLN));}
	| condicion ')' {this.errores.add(new ErrorG("Error 020: Se esperaba un ( antes de la condicion", Analizador_Lexico.cantLN));}
;




condicion : expresion operador_logico expresion { PI.put($2.sval); }
| operador_logico expresion  {this.errores.add(new ErrorG("Error 021: Se esperaba un operador logico valido", Analizador_Lexico.cantLN));}

;

operador_logico : '<' 		{ $$.sval = "<"; }
	| '>'				    { $$.sval = ">"; }
	| MENORIGUAL			{ $$.sval = "<="; }
	| MAYORIGUAL			{ $$.sval = ">="; }
	| '='					{ $$.sval = "="; }
	| DISTINTO			    { $$.sval = "!="; }

;

asignacion : ID ASIGN expresion {
	System.out.println("El tipo de ID :'"+Analizador_Lexico.tablaSimbolos.get($1.sval).tipo+"' y el tipo de la expresion:'"+$3.sval+"'");
	if(!$3.sval.equals(Analizador_Lexico.tablaSimbolos.get($1.sval).tipo))
		this.errores.add(new ErrorG("Error 035 : El tipo de la variable "+$1.sval+" no coincide con el de la expresion", Analizador_Lexico.cantLN));
    if ( estoyEnFuncion ){
    if ( $1.sval.equals(idParam) )
        if ( Analizador_Lexico.tablaSimbolos.get(idFun).permisoFun == Ps )
            Analizador_Lexico.tablaSimbolos.get(idFun).permisoFun = Wrps;
        else
	   if ( isPermited(Analizador_Lexico.tablaSimbolos.get(idFun).permisoFun, Wr) )
            Analizador_Lexico.tablaSimbolos.get(idFun).permisoFun = Wr;
    }

	Token t=Analizador_Lexico.tablaSimbolos.get($1.sval); PI.put($1.sval); PI.put(":=");
	if(t!=null){//Primero me fijo si esta declarada
		if(t.declarada==false)
			this.errores.add(new ErrorG("Error 033 : La variable "+$1.sval+" no esta declarada ", Analizador_Lexico.cantLN));
		else{
			t=Analizador_Lexico.getEntradaTS($1.sval,this.ambitoActual);
			if(t==null)//Despues me fijo si esta en el ambito
				this.errores.add(new ErrorG("Error 34 : El identificador "+$1.sval+" no esta en el ambito "+this.ambitoActual, Analizador_Lexico.cantLN));
		}
	}
	else
		System.out.println($1.sval+" No esta en la tabla de simbolos ndmpp ");
}
	| ID expresion {this.errores.add(new ErrorG("Error 022: Falta el operador de asignacion", Analizador_Lexico.cantLN));}//Este creo que anda si no hay otro error de los que compilancon el que hace macaana
;

expresion : expresion '+' termino	{ PI.put("+"); }//$$.sval=$3.sval;
	| expresion '-' termino		{ PI.put("-"); }//$$.sval=$3.sval;
	| termino{$$.sval=$1.sval;}
;

termino : termino '*' factor		{ PI.put("*"); }//$$.sval=$3.sval;
	| termino '/' factor		{ PI.put("/"); }//$$.sval=$3.sval;
	| factor {$$.sval=$1.sval;}
;

factor : ID 				{ PI.put($1.sval);  
	Token t=Analizador_Lexico.tablaSimbolos.get($1.sval);
	if(t!=null){
		$$.sval=t.tipo;
		if(t.declarada==false)//Primero me fijo si esta declarada
			this.errores.add(new ErrorG("Error 024 : La variable "+$1.sval+" no esta declarada ", Analizador_Lexico.cantLN));
		else{
			t=Analizador_Lexico.getEntradaTS($1.sval,this.ambitoActual);
			if(t==null)//Despues me fijo si esta en el ambito
				this.errores.add(new ErrorG("Error 025 : El identificador "+$1.sval+" no esta en el ambito "+this.ambitoActual, Analizador_Lexico.cantLN));
		}
	}
	else
		System.out.println(" No esta en la tabla de simbolos ndmpp ");
	}

	| USLINTEGER 			{ PI.put($1.sval); $$.sval=Analizador_Lexico.tablaSimbolos.get($1.sval).tipo; }
	| SINGLE 			    { PI.put($1.sval); $$.sval=Analizador_Lexico.tablaSimbolos.get($1.sval).tipo; }
	| '-' SINGLE {	Token t=Analizador_Lexico.tablaSimbolos.get($2.sval); 
	//Analizador_Lexico.tablaSimbolos.remove($2.sval); Lo saque porque puede borrar otra instancia positiva de un single
	t.lexema="-"+t.lexema; PI.put("-" + $2.sval);
	Analizador_Lexico.tablaSimbolos.put(t.lexema,t);	$$.sval=t.tipo;}
	|ID parametros ','{ $$.sval=Analizador_Lexico.tablaSimbolos.get($1.sval).tipo;
                    if ( estoyEnFuncion ){
                        //System.out.println(idParam + " contra " + $2.sval);
                        if ( idParam.equals($2.sval) )
                            if ( Analizador_Lexico.tablaSimbolos.get(idFun).permisoFun == Rd )
                                Analizador_Lexico.tablaSimbolos.get(idFun).permisoFun = Ps;
                            else if ( Analizador_Lexico.tablaSimbolos.get(idFun).permisoFun == Wr )
                                    Analizador_Lexico.tablaSimbolos.get(idFun).permisoFun = Wrps;

                    }
                    //System.out.println("\n idFun " + idFun + "\n" + "CantLN: " + Analizador_Lexico.cantLN);
                    //System.out.println(Analizador_Lexico.tablaSimbolos.get($1.sval).permisoFun + " y pase " + $2.ival);
	                if ( !isPermited(Analizador_Lexico.tablaSimbolos.get($1.sval).permisoFun, $2.ival) )
                        this.errores.add(new ErrorG("Error 026 : La funcion "+$1.sval+" no puede ser invocada con " + $2.ival, Analizador_Lexico.cantLN));
					String tipoParametro=Analizador_Lexico.tablaSimbolos.get($2.sval).tipo;
					if(!tipoParametro.equals(parametrosFunciones.get($1.sval)))
						this.errores.add(new ErrorG("Error 034 : La funcion "+$1.sval+" no puede ser invocada con un parametro de tipo '"+tipoParametro+"' ", Analizador_Lexico.cantLN));
					
	PI.jumpToFun($1.sval); Token t=Analizador_Lexico.tablaSimbolos.get($1.sval);
	if(t!=null){
		if(t.declarada==false)
			this.errores.add(new ErrorG("Error 027 : La funcion "+$1.sval+" no esta declarada ", Analizador_Lexico.cantLN));
		else
			if(t.uso!="funcion")
				this.errores.add(new ErrorG("Error 028 : El identificador "+t.lexema+" no es una funcion. ", Analizador_Lexico.cantLN));
	}
	else
		System.out.println("El identificador "+$1.sval+" no se agrego a la tabla de simbolos (El identificador es una funcion) (ndmpp)"); }
;

parametros: '(' ID ';' lista_permisos ')' {
                        $$.ival = $4.ival; $$.sval = $2.sval;

 PI.put($2.sval);
	Token t=Analizador_Lexico.tablaSimbolos.get($2.sval);
	if(t!=null){//Primero me fijo si esta declarada
		if(t.declarada==false)
			this.errores.add(new ErrorG("Error 029: La variable "+$2.sval+" no esta declarada ", Analizador_Lexico.cantLN));
		else{
			t=Analizador_Lexico.getEntradaTS($2.sval,this.ambitoActual);
			if(t==null)//Despues me fijo si esta en el ambito
				this.errores.add(new ErrorG("Error 030 : El identificador "+$2.sval+" no esta en el ambito "+this.ambitoActual, Analizador_Lexico.cantLN));
		}
	}
	else
		System.out.println(" No esta en la tabla de simbolos ndmpp ");
	}

//| ID ';' lista_permisos ')'  {this.errores.add(new ErrorG("Error NOANDA: Se esperaba un (", Analizador_Lexico.cantLN));}
|'(' ID ';' lista_permisos   {this.errores.add(new ErrorG("Error 031: Se esperaba un )", Analizador_Lexico.cantLN));}
|'(' ID  lista_permisos ')'  {this.errores.add(new ErrorG("Error 032: Se esperaba un ;", Analizador_Lexico.cantLN));}
;

tipo : USLINTEGERPR
	| SINGLEPR
;

lista_permisos : READONLY { $$.ival = Rd; }
	| WRITE { $$.ival = Wr; }
	| PASS { $$.ival = Ps; }
	| WRITE ';' PASS { $$.ival = Wrps; }
	|

	| WRITE PASS {this.errores.add(new ErrorG("Error 033: Se esperaba un ; entre los permisos", Analizador_Lexico.cantLN));}
;


%%

private static final int Rd = 0;
private static final int Wr = 1;
private static final int Ps = 2;
private static final int Wrps = 3;


public Polaca_Inversa PI = new Polaca_Inversa();
public Analizador_Lexico al;
public ArrayList<AnalizadorLexico.Error> errores;
public static TokenValue ultimoTokenleido;
public static ArrayList<String> estructuras;
public String idFun;
public boolean estoyEnFuncion = false;
public String idParam;
public String ambitoActual="@main";
public String ultimaFuncion;
private HashMap<String,String> parametrosFunciones=new HashMap<>();

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

public static boolean isPermited(int permisoFuncion, int permisoInvocacion){
    	if(permisoFuncion==permisoInvocacion)
    		return true;
    	if(permisoFuncion==Parser.Rd)
    		return true;
    	if(permisoFuncion==Parser.Wr&&permisoInvocacion==Parser.Wrps)
    		return true;
    	if(permisoFuncion==Parser.Ps&&permisoInvocacion==Parser.Wrps)
    		return true;
    	return false;
    }

    public static void main(String [] args) {
    	System.out.println("");
    	System.out.println("Testing con readonly en la funcion:");
    	System.out.println("");
    	if(isPermited(Parser.Rd,Parser.Rd))
    		System.out.println("Recibi un readonly y la funcion tenia un readonly, lo acepte");
    	if(isPermited(Parser.Rd,Parser.Ps))
    		System.out.println("Recibi un pass y la funcion tenia readonly, lo acepte");
    	if(isPermited(Parser.Rd,Parser.Wr))
    		System.out.println("Recibi un write y la funcion tenia readonly, lo acepte");
    	if(isPermited(Parser.Rd,Parser.Wrps))
    		System.out.println("Recibi un write;pass y la funcion tenia readonly, lo acepte");
    	System.out.println("");
    	System.out.println("Testing con pass en la funcion:");
    	System.out.println("");
    	if(isPermited(Parser.Ps,Parser.Ps))
    		System.out.println("Recibi un pass y la funcion tenia un pass, lo acepte");
    	if(!isPermited(Parser.Ps,Parser.Rd))
    		System.out.println("Recibi un readonly y la funcion tenia pass, RECHAZADO");
    	if(!isPermited(Parser.Ps,Parser.Wr))
    		System.out.println("Recibi un write y la funcion tenia pass, RECHAZADO");
    	if(isPermited(Parser.Ps,Parser.Wrps))
    		System.out.println("Recibi un write;pass y la funcion tenia pass, lo acepte");
    	System.out.println("");
    	System.out.println("Testing con write en la funcion:");
    	System.out.println("");
    	if(isPermited(Parser.Wr,Parser.Wr))
    		System.out.println("Recibi un write y la funcion tenia write, lo acepte");
    	if(!isPermited(Parser.Wr,Parser.Ps))
    		System.out.println("Recibi un pass y la funcion tenia un write, RECHAZADO");
    	if(!isPermited(Parser.Wr,Parser.Rd))
    		System.out.println("Recibi un readonly y la funcion tenia write, RECHAZADO");
    	if(isPermited(Parser.Wr,Parser.Wrps))
    		System.out.println("Recibi un write;pass y la funcion tenia write, lo acepte");
    	System.out.println("");
    	System.out.println("Testing con write;pass en la funcion:");
    	System.out.println("");
    	if(!isPermited(Parser.Wrps,Parser.Wr))
    		System.out.println("Recibi un write y la funcion tenia write;pass, RECHAZADO");
    	if(!isPermited(Parser.Wrps,Parser.Ps))
    		System.out.println("Recibi un pass y la funcion tenia un write;pass, RECHAZADO");
    	if(!isPermited(Parser.Wrps,Parser.Rd))
    		System.out.println("Recibi un readonly y la funcion tenia write;pass, RECHAZADO");
    	if(isPermited(Parser.Wrps,Parser.Wrps))
    		System.out.println("Recibi un write;pass y la funcion tenia write;pass, lo acepte");
    	System.out.println("");
    	System.out.println("Testing con noseusaelparametro en la funcion:");
    	System.out.println("");
    }