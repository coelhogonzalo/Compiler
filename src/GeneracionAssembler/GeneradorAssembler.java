package GeneracionAssembler;


import java.util.ArrayList;
import java.util.Stack;

import AnalizadorLexico.Analizador_Lexico;
import GeneracionCodigoIntermedio.Polaca_Inversa;

public class GeneradorAssembler {
    private Stack<StringBuilder> pilaVar = null;
    Polaca_Inversa PI = null;
    StringBuilder codigo = null;
    public int contador = 0;
    StringBuilder funciones = null;
    StringBuilder declaracion = null;
    boolean estaEnFuncion = false;
    private ArrayList<StringBuilder> saltos = null;
    
    //CONSTRUCTOR
    public GeneradorAssembler(Stack<StringBuilder> pilaVar,Polaca_Inversa PI){
    	this.pilaVar=pilaVar;
    	this.PI=PI;
    	//Analizador_Lexico.tablaSimbolos.get(PI.getPI().get(i)).tipo;
    }
    
    
    //LEO DE LA POLACA
    public void leer(){
    	for(int i=0; i<PI.getPI().size(); i++){
    		pilaVar.push(PI.getPI().get(i));   //SACO ELEMENTOS DE LA POLACA
    		
    		if(pilaVar.peek().toString() == "inicio_funcion") //ME FIJO SI EMPIEZA UNA FUNCION PORQUE ESCRIBE EN OTRO LADO
    			pilaVar.pop();
    			StringBuilder nom_fun = pilaVar.pop();
    			funciones.append("@FUNCTION_"+nom_fun+"/n");
    			estaEnFuncion = true; //HAGO QUE EMPIEZE A ESCRIBIR EN LA PARTE DE FUNCIONES
    			
    		if(pilaVar.peek().toString() == "return"){ //LEE EL RETURN DE UNA FUNCION
    			funciones.append("MOV EAX, " +pilaVar.peek() + "/n" + "MOV @aux_fun, EAX" +"/n" +"RET" +"/n /n");
    			estaEnFuncion = false;
    			StringBuilder aux_fun= new StringBuilder();
				pilaVar.push(aux_fun);		//SETEA ENFUNCION A FALSE PARA QUE VUELVA A ESCRIBIR EN CODIGO
    		}
    		
    		if(estaEnFuncion){     //FALTA HACER QUE ES HACER TODO LO DE ABAJO PERO EN VEZ DE ESCRIBIR EN CODIGO ESCRIBE EN FUNCIONES
    			//generarCodigo();
    		}
    		else{
    			//PARA OPERACIONES ARITMETICAS \\\\\\\\\\FALTA SACAR EL TIPO DE CADA STRINGBUIDER QUE NO SE COMO YO, PREGUNTAR
	    			if(pilaVar.peek().toString() =="+" || pilaVar.peek().toString() =="-"|| pilaVar.peek().toString() =="*" || pilaVar.peek().toString() =="/"){
	    				StringBuilder primerOperando = desapilar();
	    				StringBuilder segundoOperando = desapilar();
	    				//	Analizador_Lexico.tablaSimbolos.get(PI.get(i).toString).tipo;
	    				//if para cada uno de los tipos, PERO COMO SACO LOS TIPOS DE CADA UNO DE LOS ELEMENTOS DE LA POLACA? WUT UWU, buscar el lexema en la tabla de simbolos con el string
	    				generarCodigoInteger(pilaVar.peek(),primerOperando,segundoOperando);
	    				generarCodigoSingle(pilaVar.peek(),primerOperando,segundoOperando);
	    				generarCodigoPrimeroInteger(pilaVar.peek(),primerOperando,segundoOperando);
	    				generarCodigoSegundoInteger(pilaVar.peek(),primerOperando,segundoOperando);
	    				
	    		//PARA LLAMADO A FUNCIONES	
	    			if(pilaVar.peek().toString()=="CALL")
	    				pilaVar.pop(); //lo tira al espacio tiempo?
	    				StringBuilder fun_llamada = pilaVar.pop();
	    				codigo.append("CALL @FUNCTION_"+fun_llamada+"/n"+"MOV @aux"+contador+"@aux_fun"+"/n");
	    				
	    		//PARA SALTOS	
	    			if(pilaVar.peek().toString().contains("LABEL")) //ESTO ES VALIDO PARA EL WHILE
	    				if(verificarSaltoExistente(pilaVar.peek())) //ACLARACION: PARA LOS SALTOS DE WHILE USO UNA LISTA PARA VER SI EXISTE O NO ENTONCES VEO SI ESCRIBO CON O SIN JMP
	    					codigo.append("JMP @"+pilaVar.pop());
	    				else
	    					codigo.append("@"+pilaVar.pop());
	    			}
	    			
	    		//PARA PRINTS
	    			if(pilaVar.peek().toString()=="print")
	    				pilaVar.pop();
	    				if(pilaVar.peek().toString().contains("'")){
	    					StringBuilder mensajito = pilaVar.pop();
	    					codigo.append("invoke MessageBox, NULL, addr "+mensajito+", addr "+mensajito+", MB_OK");
	    				}
	    				else{
	    					StringBuilder variable = pilaVar.pop();
	    					//if(variable es un flotante)  //LO MISMO QUE ARRIBA PARA EL TIPO
	    					codigo.append("invoke printf, cfm$(%.20Lf\n), "+variable);
	    					//if(variable es entero)
	    					codigo.append("invoke printf, cfm$(%.%llu\n), "+variable);
	    				}
	    		}
    		}
    	}
    
    private boolean verificarSaltoExistente(StringBuilder buscado){
    	return(saltos.contains(buscado));
    }
    
    public void generarDeclaracion(){
    						//Puse \" en vez de "
    	declaracion.append("mensaje_overflow db \"Hubo overflow uwu\", 0 "+"/n");		//QUE HAGO ACA PARA SALVAR ESO? UWU
    	declaracion.append("mensaje_zero db \"El divisor es 0 huehuehue\", 0 "+"/n");
    	declaracion.append("mensaje_resultadoNeg db \"Me dio negativo xD\", 0 "+"/n");
    	declaracion.append("@aux_fun dd?");
    	for(int i=0; i<contador; i++){
    		declaracion.append("@aux"+contador+" dd?");
    	}
        //recorrer todo al final para ver cuales se usaron y cuales no, alto rip
        declaracion.append(".const  cte_max_rango  equ 3.40282347E+38"+"/n");
    	declaracion.append(".code"+"/n"+"start:");
    }

    //ACA EMPIEZA PARA LAS OPERACIONES ARITMETICAS
    
    public void generarCodigoInteger(StringBuilder operador,StringBuilder primerOperando, StringBuilder segundoOperando){ //para los dos integer
    	String op = operador.toString();
    	StringBuilder aux = null;
    	switch(op){
    	case "+" :  codigo.append("MOV EAX,"+primerOperando+"/n"+"ADD EAX, "+segundoOperando+"/n"+"MOV "+"@aux"+contador+", EAX"+"/n");
    				aux= new StringBuilder("@aux"+contador);
    				pilaVar.push(aux);  				
    				contador++;
    	
    	case"-" : codigo.append("MOV EAX,"+segundoOperando+"/n"+"CMP EAX"+primerOperando+"/n"+"JL @LABEL_RESUL_NEG"+"/n"+"SUB EAX"+primerOperando+"/n"+ "MOV "+"@aux"+contador+", EAX"+"/n");
  				  aux= new StringBuilder("@aux"+contador);
  				  pilaVar.push(aux);
		 		  contador++;
    	
    	case"*": codigo.append("MOV EAX,"+primerOperando+"/n"+"MUL EAX"+segundoOperando+"/n"+ "JO @LABEL_OVF"+"/n"+ "MOV "+"@aux"+contador+", EAX"+"/n");
 				 aux= new StringBuilder("@aux"+contador);
 				 pilaVar.push(aux);
				 contador++;
 //EL JUMP ZERO FUNCIONA BIEN ACA?
    	case"/" : codigo.append("MOV EAX,"+segundoOperando+"/n"+"MOV EBX,"+primerOperando+"/n"+"CMP EBX, 0"+"/n"+"JZ @LABEL_ZERO"+"DIV EAX"+primerOperando+"/n"+ "MOV "+"@aux"+contador+", EAX"+"/n");
 				  aux= new StringBuilder("@aux"+contador);
 				  pilaVar.push(aux);
		 		  contador++;
    	}
    }
    	
    	 public void generarCodigoSingle(StringBuilder operador,StringBuilder primerOperando, StringBuilder segundoOperando){ //para los dos single
    	    	String op = operador.toString();
    	    	StringBuilder aux = null;
    	    	switch(op){
    	    	case "+" :  codigo.append("FLD "+primerOperando+"/n"+"FLD "+segundoOperando+"/n"+ "FADD"+"/n"+"FSTP "+"@aux"+contador+"/n");
    	    				aux= new StringBuilder("@aux"+contador);
    	    				pilaVar.push(aux);
    	    				contador++;
    	    	
    	    	case"-" : codigo.append("FLD "+primerOperando+"/n"+"FLD "+segundoOperando+"/n"+"FCOM"+"/n"+"JB @LABEL_RESUL_NEG"+"/n"+ "FSUB"+"/n"+"FSTP "+"@aux"+contador+"/n");
						  aux= new StringBuilder("@aux"+contador);
						  pilaVar.push(aux);
    			 		  contador++;
    	    	
    	    	case"*": codigo.append("FLD "+primerOperando+"/n"+"FLD "+segundoOperando+"/n"+ "FMUL"+"/n"+"FSTP "+"@aux"+contador+"/n"+"FLD cte_max_rango"+"/n"+"FLD @aux"+contador+"FCOM"+"/n"+"FSTSW AX"+"/n"+"SAHF"+"/n"+"JA @LABEL_OVF"+"/n");
						 aux= new StringBuilder("@aux"+contador);
						 pilaVar.push(aux);		
		    	    	 contador++;
   //DICE QUE PARA EL FCOM TIENE QUE SER NUMERO REAL, VER!!!
    	    	case"/" :codigo.append("FLD "+primerOperando+"/n"+"FLD "+segundoOperando+"/n"+"FCOM 0"+"/n"+"JE @LABEL_ZERO"+ "FDIV"+"/n"+"FSTP "+"@aux"+contador+"/n");
						 aux= new StringBuilder("@aux"+contador);
						 pilaVar.push(aux);
		    	    	 contador++;
    	    	}
    }
   
    	 public void generarCodigoPrimeroInteger(StringBuilder operador,StringBuilder primerOperando, StringBuilder segundoOperando){ //para el primero integer y el segundo single
 	    	String op = operador.toString();
 	    	StringBuilder aux = null;
 	    	switch(op){
 	    	case "+" :  codigo.append("FILD "+primerOperando+"/n"+"FLD "+segundoOperando+"/n"+ "FADD"+"/n"+"FSTP "+"@aux"+contador+"/n");
						aux= new StringBuilder("@aux"+contador);
						pilaVar.push(aux);
 	    				contador++;
 	    	
 	    	case"-" : codigo.append("FILD "+primerOperando+"/n"+"FLD "+segundoOperando+"/n"+"FCOM"+"/n"+"JB @LABEL_RESUL_NEG"+"/n"+ "FSUB"+"/n"+"FSTP "+"@aux"+contador+"/n");
				      aux= new StringBuilder("@aux"+contador);
					  pilaVar.push(aux);
		 			  contador++;
 	    	
 	    	case"*": codigo.append("FILD "+primerOperando+"/n"+"FLD "+segundoOperando+"/n"+ "FMUL"+"/n"+"FSTP "+"@aux"+contador+"/n"+"FLD cte_max_rango"+"/n"+"FLD @aux"+contador+"FCOM"+"/n"+"FSTSW AX"+"/n"+"SAHF"+"/n"+"JA @LABEL_OVF"+"/n");
					 aux= new StringBuilder("@aux"+contador);
					 pilaVar.push(aux);		 
			 	     contador++;
 	    	
 	    	case"/" : codigo.append("FILD "+primerOperando+"/n"+"FLD "+segundoOperando+"/n"+"FCOM 0"+"/n"+"JE @LABEL_ZERO"+ "FDIV"+"/n"+"FSTP "+"@aux"+contador+"/n");
					  aux= new StringBuilder("@aux"+contador);
					  pilaVar.push(aux); 		  
		 	    	  contador++;
 	    	}
 }
    	 
    	 
    	 public void generarCodigoSegundoInteger(StringBuilder operador,StringBuilder primerOperando, StringBuilder segundoOperando){ //para primero single y segundo integer
 	    	String op = operador.toString();
 	    	StringBuilder aux = null;
 	    	switch(op){
 	    	case "+" :  codigo.append("FLD "+primerOperando+"/n"+"FILD "+segundoOperando+"/n"+ "FADD"+"/n"+"FSTP "+"@aux"+contador+"/n");
						aux= new StringBuilder("@aux"+contador);
						pilaVar.push(aux);
 	    				contador++;
 	    	
 	    	case"-" : codigo.append("FLD "+primerOperando+"/n"+"FILD "+segundoOperando+"/n"+"FCOM"+"/n"+"JB @LABEL_RESUL_NEG"+"/n"+ "FSUB"+"/n"+"FSTP "+"@aux"+contador+"/n");
					  aux= new StringBuilder("@aux"+contador);
					  pilaVar.push(aux);
 			 		  contador++;
 	    	
 	    	case"*": codigo.append("FLD "+primerOperando+"/n"+"FILD "+segundoOperando+"/n"+ "FMUL"+"/n"+"FSTP "+"@aux"+contador+"/n"+"FLD cte_max_rango"+"/n"+"FLD @aux"+contador+"FCOM"+"/n"+"FSTSW AX"+"/n"+"SAHF"+"/n"+"JA @LABEL_OVF"+"/n");
					 aux= new StringBuilder("@aux"+contador);
					 pilaVar.push(aux);
 					 contador++;
 	    	
 	    	case"/" : codigo.append("FLD "+primerOperando+"/n"+"FILD "+segundoOperando+"FCOM 0"+"/n"+"JE @LABEL_ZERO"+"/n"+ "FDIV"+"/n"+"FSTP "+"@aux"+contador+"/n");
					  aux= new StringBuilder("@aux"+contador);
					  pilaVar.push(aux);
 			 		  contador++;
 	    	}
 }
    	 								// Le hice el escape a las \ en el string
    public void generarEncabezado(){
    	codigo.append(".386"+"/n"+".model flat, stdcall"+"/n"+"option casemap :none "+"/n"+"include \\masm32\\include\\windows.inc"+"/n"+"include \\masm32\\include\\kernel32.inc"
    					+"/n"+"include \\masm32\\include\\masm32.inc"+"/n"+"includelib \\masm32\\lib\\kernel32.lib"+"/n"+"includelib \\masm32\\lib\\masm32.lib"+"/n"+".data"+"/n");   
    }
    
    public void generarMensajitosDeControl(){
    	codigo.append("@LABEL_OVF:"+ "/n"+ "invoke MesseageBox,NULL,addr mensaje_overflow,addr"+"/n"+"mensaje_overflow,MB_OK"+"/n"+"JMP @LABEL_END"+"/n");
    	codigo.append("@LABEL_ZERO:"+ "/n"+ "invoke MesseageBox,NULL,addr mensaje_zero,addr"+"/n"+"mensaje_zero,MB_OK"+"/n"+"JMP @LABEL_EN"+"/n");
    	codigo.append("@LABEL_RESUL_NEG:"+ "/n"+ "invoke MesseageBox,NULL,addr mensaje_resultadoNeg,addr"+"/n"+"mensaje_resultadoNeg,MB_OK"+"/n"+"JMP @LABEL_END"+"/n");
    }
    
    public void generarFin(){
    	codigo.append("invoke ExitProcess, 0"+"/n"+"end start");
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


}
