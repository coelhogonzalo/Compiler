package GeneracionAssembler;


import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

import AnalizadorLexico.Analizador_Lexico;
import AnalizadorLexico.Token;
import GeneracionCodigoIntermedio.Polaca_Inversa;

public class GeneradorAssembler {
    private Stack<StringBuilder> pilaVar = new Stack<StringBuilder>();
    Polaca_Inversa PI = null;
    StringBuilder codigo = new StringBuilder("\n");
    StringBuilder inicio = new StringBuilder();
    public int contador = 0;
    StringBuilder funciones = new StringBuilder("\n");
    StringBuilder declaracion = new StringBuilder("\n");
    boolean estaEnFuncion = false;
    private List<Integer> saltitos = new ArrayList<Integer>();
	StringBuilder signo = new StringBuilder();
	StringBuilder aux_parametro = new StringBuilder();
	StringBuilder fin = new StringBuilder("\n");
    
    //CONSTRUCTOR
    public GeneradorAssembler(Polaca_Inversa PI){
    	this.PI=PI;
    }
    
    //Analizador_Lexico.tablaSimbolos.get(PI.getPI().get(i)).tipo;
    //if(Analizador_Lexico.tablaSimbolos.get(PI.getPI().get(i)).tipo.equals("single")
    //if(Analizador_Lexico.tablaSimbolos.get(PI.getPI().get(i)).tipo.equals("uslinteger")
    
    public void guardarSaltos(){
    	for(int i=0; i<PI.getPI().size(); i++){
    		String buscado = PI.getPI().get(i).toString();
    		if(buscado.contains("Label")){
    			int numero = Integer.parseInt(buscado.substring(5,buscado.length()));
    			if(!saltitos.contains(numero))
    				saltitos.add(numero);
    		}
    	}
    }
    
    
    //LEO DE LA POLACA
    public void leer(){
    		for(int i=0; i<PI.getPI().size(); i++){ //ESCRIBO LOS LABEL CUANDO LLEGUE A LA POSICION TAL
    		
    		if(saltitos.contains(i)){
				if(!estaEnFuncion)
					codigo.append("LABEL"+i+":"+"\n");
				else
					funciones.append("LABEL"+i+":"+"\n");
    		}
			
    	
    		pilaVar.push(PI.getPI().get(i));   //SACO ELEMENTOS DE LA POLACA
		
		
			if(pilaVar.peek().toString().equals("inicio_funcion")){ //ME FIJO SI EMPIEZA UNA FUNCION PORQUE ESCRIBE EN OTRO LADO
				pilaVar.pop();
				StringBuilder nom_fun = pilaVar.pop();
				StringBuilder aux_param_declarada = pilaVar.pop();
				funciones.append("@FUNCTION_"+nom_fun+"\n");
				funciones.append("MOV EAX, "+aux_parametro+"/n"+"MOV "+aux_param_declarada+", EAX"); //asigno el valor del parametro real al de la funcion
				estaEnFuncion = true; //HAGO QUE EMPIEZE A ESCRIBIR EN LA PARTE DE FUNCIONES
			}
			
		
			if(pilaVar.peek().toString().equals("return")){ //LEE EL RETURN DE UNA FUNCION
				pilaVar.pop();
				StringBuilder retorno = pilaVar.pop();
				Token t = null; 
				if(Analizador_Lexico.tablaSimbolos.get(retorno.toString()).tipo.equals("uslinteger")){
						funciones.append("MOV EAX, " +retorno + "\n" + "MOV @aux_fun, EAX" +"\n" +"RET" +"\n \n");
						t = new Token("@aux_fun",Analizador_Lexico.TOKEN_UL,"uslinteger");
				}
				
				if(Analizador_Lexico.tablaSimbolos.get(retorno.toString()).tipo.equals("single")){
						funciones.append("FLD "+retorno+ "\n" + "FSTP @aux_fun"+"\n" +"RET" +"\n \n");
						t = new Token(retorno.toString(),Analizador_Lexico.TOKEN_FLOAT,"single");
				}
				
				estaEnFuncion = false;
				StringBuilder aux_fun= new StringBuilder(retorno);
				Analizador_Lexico.tablaSimbolos.put(t.lexema,t);
				pilaVar.push(aux_fun);		//SETEA ENFUNCION A FALSE PARA QUE VUELVA A ESCRIBIR EN CODIGO
	    	}
		
			
		if(!estaEnFuncion)
			generarCodigoAssembler(codigo);
		else
			generarCodigoAssembler(funciones);
    		
		}
	}		
			
			
public void generarCodigoAssembler(StringBuilder escritura){
	
	//ASIGNACIONES	
	if(pilaVar.peek().toString().equals(":=")){ 
		pilaVar.pop(); //SACO EL :=
		StringBuilder aAsignar = desapilar();
		StringBuilder asignacion = desapilar();
		Token t = null;
		if(Analizador_Lexico.tablaSimbolos.get(aAsignar.toString()).tipo.equals("uslinteger")){
			escritura.append("MOV EAX, "+asignacion+"\n"+"MOV +"+aAsignar+", EAX"+"\n" ); //ver si va lo de mov @aux0, EAX despues de esto
			t = new Token("@aux"+contador,Analizador_Lexico.TOKEN_UL,"uslinteger");
			Analizador_Lexico.tablaSimbolos.put(t.lexema,t);
			StringBuilder aux= new StringBuilder("@aux"+contador);
			pilaVar.push(aux);
			contador++;
		}
		else{
			if(Analizador_Lexico.tablaSimbolos.get(aAsignar.toString()).tipo.equals("single")){
					escritura.append("FLD"+aAsignar+"\n"+"FSTP @aux"+contador); //creo que seria FLD asignacion + FSTP aAsignar
					t = new Token("@aux"+contador,Analizador_Lexico.TOKEN_FLOAT,"single");
					Analizador_Lexico.tablaSimbolos.put(t.lexema,t);
					StringBuilder aux= new StringBuilder("@aux"+contador);
					pilaVar.push(aux);
					contador++;
			}
			else 
				escritura.append("JMP @LABEL_END"+"\n");
		}

	}

		
		

	
	//LLAMADO DE FUNCIONES
	if(pilaVar.peek().toString().equals("CALL")){
		pilaVar.pop(); //SACO EL CALL
		StringBuilder fun_llamada = pilaVar.pop();
		aux_parametro = pilaVar.pop(); //asigno el valor del parametro
		codigo.append("CALL @FUNCTION_"+fun_llamada+"\n");
		StringBuilder aux= new StringBuilder("@aux"+contador);
		Token t = new Token("@aux"+contador,Analizador_Lexico.TOKEN_FLOAT,"single");			//VER QUE ONDA
		Analizador_Lexico.tablaSimbolos.put(t.lexema,t);
		pilaVar.push(aux);
		contador++;
	}
    		
	//PARA COMPARACIONES	
	if(pilaVar.peek().toString().equals("<")||pilaVar.peek().toString().equals(">")||pilaVar.peek().toString().equals("=")||pilaVar.peek().toString().equals("!=")){
		signo = pilaVar.pop();
		StringBuilder primerComparado = desapilar();
		StringBuilder segundoComparado = desapilar();
		//AMBOS INTEGER
		if((Analizador_Lexico.tablaSimbolos.get(primerComparado.toString()).tipo.equals("uslinteger")) && (Analizador_Lexico.tablaSimbolos.get(segundoComparado.toString()).tipo.equals("uslinteger")))
			escritura.append("MOV EAX, "+primerComparado+"\n"+"MOV EBX, "+segundoComparado+"\n"+"CMP EAX,EBX");
		//AMBOS FLOAT 
		if((Analizador_Lexico.tablaSimbolos.get(primerComparado.toString()).tipo.equals("single")) && ((Analizador_Lexico.tablaSimbolos.get(segundoComparado.toString()).tipo.equals("single")))){
			escritura.append("FLD "+primerComparado+"\n"+"FLD "+segundoComparado+"\n"+"FCOM"+"\n"+"FSTSW @aux"+contador+"\n"+"MOV EAX, @aux"+contador+"\n"+"SAHF");
			Token t = new Token("@aux"+contador,Analizador_Lexico.TOKEN_FLOAT,"single");
			Analizador_Lexico.tablaSimbolos.put(t.lexema,t);
		}
		//PRIMERO INT SEGUNDO SINGLE
		if((Analizador_Lexico.tablaSimbolos.get(primerComparado.toString()).tipo.equals("single")) && ((Analizador_Lexico.tablaSimbolos.get(segundoComparado.toString()).tipo.equals("single"))))
			escritura.append("JMP @LABEL_END"+"\n");
		if((Analizador_Lexico.tablaSimbolos.get(primerComparado.toString()).tipo.equals("single")) && ((Analizador_Lexico.tablaSimbolos.get(segundoComparado.toString()).tipo.equals("single"))))
			escritura.append("JMP @LABEL_END"+"\n");
		StringBuilder aux= new StringBuilder("@aux"+contador);
		pilaVar.push(aux);
		contador++;
	}
		
	//PARA SALTOS
	if(pilaVar.peek().toString().equals("B")){
		pilaVar.pop(); //SACO EL B
		StringBuilder label = pilaVar.pop();
		if(Integer.parseInt(label.substring(5,label.length()))>=PI.getPI().size()) //VERIFICO, SI NO CUMPLE LLEVA AL FINAL //ACA CREO ES 5 NO 6
			label =new StringBuilder("@LABEL_END"+"\n");
		
		if(signo.toString().equals("="))
				escritura.append("JNE "+label+"\n");
			
		if(signo.toString().equals("!="))
				escritura.append("JE "+label+"\n");
			
		//IF ES INTEGER
			if(signo.toString().equals("<"))
				escritura.append("JGL "+label+"\n");
			
			if(signo.toString().equals(">"))
				escritura.append("JLE "+label+"\n");
			
		//IF ES SINGLE
			if(signo.toString().equals("<"))
				escritura.append("JAE "+label+"\n");
			
			if(signo.toString().equals(">"))
				escritura.append("JBE "+label+"\n");

	}
	
	if(pilaVar.peek().toString().equals("BT")){
		pilaVar.pop(); //SACO EL BT
		StringBuilder label = pilaVar.pop();
		if(Integer.parseInt(label.substring(5,label.length()))>=PI.getPI().size()) //VERIFICO SI ESTO LLEVA AL FINAL
			label =new StringBuilder("@LABEL_END"+"\n");
		escritura.append("JMP "+label+"\n");
	}	
	
	//PARA PRINTS
	if(pilaVar.peek().toString().equals("print")){
	pilaVar.pop(); //SACO LA PALABRA PRINT
		if(pilaVar.peek().toString().contains("'")){
			StringBuilder mensajito = pilaVar.pop();
			escritura.append("invoke MessageBox, NULL, addr "+mensajito+", addr "+mensajito+", MB_OK"+"\n");
		}
		else{
			StringBuilder variable = pilaVar.pop();
			if(Analizador_Lexico.tablaSimbolos.get(variable.toString()).tipo.equals("single"))
				escritura.append("invoke printf, cfm$(%.20Lf\n), "+variable+"\n");
			if(Analizador_Lexico.tablaSimbolos.get(variable.toString()).tipo.equals("uslinteger"))
				escritura.append("invoke printf, cfm$(%.%llu\n), "+variable+"\n");
		}
	}
    			
	//PARA OPERACIONES ARITMETICAS
    if(pilaVar.peek().toString().equals("+") || pilaVar.peek().toString().equals("-")|| pilaVar.peek().toString().equals("*") || pilaVar.peek().toString().equals("/")){
		StringBuilder operador = desapilar(); //PARA SACAR EL OPERANDO
		StringBuilder primerOperando = desapilar();
		StringBuilder segundoOperando = desapilar();

		if((Analizador_Lexico.tablaSimbolos.get(primerOperando.toString()).tipo.equals("uslinteger")) && ((Analizador_Lexico.tablaSimbolos.get(segundoOperando.toString()).tipo.equals("uslinteger"))))
			generarCodigoInteger(operador ,primerOperando,segundoOperando,escritura);
		if((Analizador_Lexico.tablaSimbolos.get(primerOperando.toString()).tipo.equals("single")) && ((Analizador_Lexico.tablaSimbolos.get(segundoOperando.toString()).tipo.equals("single"))))
			generarCodigoSingle(operador,primerOperando,segundoOperando,escritura);
		if((Analizador_Lexico.tablaSimbolos.get(primerOperando.toString()).tipo.equals("uslinteger")) && ((Analizador_Lexico.tablaSimbolos.get(segundoOperando.toString()).tipo.equals("single"))))
			escritura.append("JMP @LABEL_END"+"\n");
		if((Analizador_Lexico.tablaSimbolos.get(primerOperando.toString()).tipo.equals("single")) && ((Analizador_Lexico.tablaSimbolos.get(segundoOperando.toString()).tipo.equals("uslinteger"))))
			escritura.append("JMP @LABEL_END"+"\n");
	}
}
		
public void generarCodigoInteger(StringBuilder operador,StringBuilder primerOperando, StringBuilder segundoOperando, StringBuilder escritura){ //para los dos integer
    	String op = operador.toString();
    	StringBuilder aux = null;
    	switch(op){
    	case "+" :  escritura.append("MOV EAX,"+primerOperando+"\n"+"ADD EAX, "+segundoOperando+"\n"+"MOV "+"@aux"+contador+", EAX"+"\n");
    				aux= new StringBuilder("@aux"+contador);
    				pilaVar.push(aux);  				
    				contador++;
    	
    	case"-" : escritura.append("MOV EAX,"+segundoOperando+"\n"+"CMP EAX"+primerOperando+"\n"+"JL @LABEL_RESUL_NEG"+"\n"+"SUB EAX"+primerOperando+"\n"+ "MOV "+"@aux"+contador+", EAX"+"\n");
  				  aux= new StringBuilder("@aux"+contador);
  				  pilaVar.push(aux);
		 		  contador++;
    	
    	case"*": escritura.append("MOV EAX,"+primerOperando+"\n"+"MUL EAX"+segundoOperando+"\n"+ "JO @LABEL_OVF"+"\n"+ "MOV "+"@aux"+contador+", EAX"+"\n");
 				 aux= new StringBuilder("@aux"+contador);
 				 pilaVar.push(aux);
				 contador++;
 //EL JUMP ZERO FUNCIONA BIEN ACA?
    	case"/" : escritura.append("MOV EAX,"+segundoOperando+"\n"+"MOV EBX,"+primerOperando+"\n"+"CMP EBX, 0.0"+"\n"+"JZ @LABEL_ZERO"+"DIV EAX"+primerOperando+"\n"+ "MOV "+"@aux"+contador+", EAX"+"\n");
 				  aux= new StringBuilder("@aux"+contador);
 				  pilaVar.push(aux);
		 		  contador++;
    	}
		Token t = new Token("@aux"+contador,Analizador_Lexico.TOKEN_UL,"uslinteger");
		Analizador_Lexico.tablaSimbolos.put(t.lexema,t);
    }
    	
public void generarCodigoSingle(StringBuilder operador,StringBuilder primerOperando, StringBuilder segundoOperando, StringBuilder escritura){ //para los dos single
			String op = operador.toString();
			StringBuilder aux = null;
			switch(op){
			case "+" :  escritura.append("FLD "+primerOperando+"\n"+"FLD "+segundoOperando+"\n"+ "FADD"+"\n"+"FSTP "+"@aux"+contador+"\n");
						aux= new StringBuilder("@aux"+contador);
						pilaVar.push(aux);
						contador++;
			
			case"-" : escritura.append("FLD "+primerOperando+"\n"+"FLD "+segundoOperando+"\n"+"FCOM"+"\n"+"JB @LABEL_RESUL_NEG"+"\n"+ "FSUB"+"\n"+"FSTP "+"@aux"+contador+"\n");
					  aux= new StringBuilder("@aux"+contador);
					  pilaVar.push(aux);
					  contador++;
			
			case"*": escritura.append("FLD "+primerOperando+"\n"+"FLD "+segundoOperando+"\n"+ "FMUL"+"\n"+"FSTP "+"@aux"+contador+"\n"+"FLD cte_max_rango"+"\n"+"FLD @aux"+contador+"FCOM"+"\n"+"FSTSW AX"+"\n"+"SAHF"+"\n"+"JA @LABEL_OVF"+"\n");
					 aux= new StringBuilder("@aux"+contador);
					 pilaVar.push(aux);		
					 contador++;
//DICE QUE PARA EL FCOM TIENE QUE SER NUMERO REAL, VER!!!
			case"/" :escritura.append("FLD "+primerOperando+"\n"+"FLD "+segundoOperando+"\n"+"FCOM 0"+"\n"+"JE @LABEL_ZERO"+ "FDIV"+"\n"+"FSTP "+"@aux"+contador+"\n");
					 aux= new StringBuilder("@aux"+contador);
					 pilaVar.push(aux);
					 contador++;
			}
			Token t = new Token("@aux"+contador,Analizador_Lexico.TOKEN_FLOAT,"single");
			Analizador_Lexico.tablaSimbolos.put(t.lexema,t);
}
   

 					
					

	    						


    public void generarDeclaracion(){

    	declaracion.append("mensaje_overflow db \"Hubo overflow uwu\", 0 "+"\n");
    	declaracion.append("mensaje_zero db \"El divisor es 0 huehuehue\", 0 "+"\n");
    	declaracion.append("mensaje_resultadoNeg db \"Me dio negativo xD\", 0 "+"\n");
    	declaracion.append("@aux_fun dd?"+"\n");
    	for(int i=0; i<contador; i++){
    		declaracion.append("@aux"+i+" dd?"+"\n");
    	}
        //recorrer todo al final para ver cuales se usaron y cuales no, alto rip
        declaracion.append(".const "+"\n"+" cte_max_rango  equ 3.40282347E+38"+"\n");
    	declaracion.append(".code"+"\n"+"start: ");
    }
   
 
    public void generarEncabezado(){
    	inicio.append(".386"+"\n"+".model flat, stdcall"+"\n"+"option casemap :none "+"\n"+"include \\masm32\\include\\windows.inc"+"\n"+"include \\masm32\\include\\kernel32.inc"
    					+"\n"+"include \\masm32\\include\\masm32.inc"+"\n"+"includelib \\masm32\\lib\\kernel32.lib"+"\n"+"includelib \\masm32\\lib\\masm32.lib"+"\n"
    			+"\n"+"include \\masm32\\include\\masm32rt.inc"+"\n"+"dll_dllcrt0 PROTO C"+"\n"+"printf PROTO C: VARARG"+".data"+"\n");   
    }
    
    public void generarMensajitosDeControl(){
    	fin.append("@LABEL_OVF:"+ "\n"+ "invoke MesseageBox,NULL,addr mensaje_overflow,addr"+"\n"+"mensaje_overflow,MB_OK"+"\n"+"JMP @LABEL_END"+"\n");
    	fin.append("@LABEL_ZERO:"+ "\n"+ "invoke MesseageBox,NULL,addr mensaje_zero,addr"+"\n"+"mensaje_zero,MB_OK"+"\n"+"JMP @LABEL_END"+"\n");
    	fin.append("@LABEL_RESUL_NEG:"+ "\n"+ "invoke MesseageBox,NULL,addr mensaje_resultadoNeg,addr"+"\n"+"mensaje_resultadoNeg,MB_OK"+"\n"+"JMP @LABEL_END"+"\n");
    }
    
    public void generarFin(){
    	fin.append("@LABEL_END:"+"\n"+"invoke ExitProcess, 0"+"\n"+"end start");
    }
    
//Cosas que me dijo anto: recorrer toda la polaca para crear la declaracion de variable, osea hacerlo al final
    //No hay anidamiento de funciones
    //Las funciones escribirlas al final del programa
    //poner en la declaracion de variables una constante con el maximo para la comparacion de overflow de single
    //despues de la declaracion de variables concatenar el .code y el start:
    
    private StringBuilder desapilar(){
    	return pilaVar.pop();
    }
    
    private StringBuilder apilar(StringBuilder s){
    	return pilaVar.push(s);
    }
    
    public void generameAssemblydotexe(){
    	generarEncabezado();
    	guardarSaltos();
    	leer();
    	generarDeclaracion();
    	inicio.append(declaracion);
    	inicio.append(codigo);
    	inicio.append(funciones);    	
    	generarMensajitosDeControl();
    	generarFin();
    	inicio.append(fin);
    	System.out.println(inicio);
    }
    

}
