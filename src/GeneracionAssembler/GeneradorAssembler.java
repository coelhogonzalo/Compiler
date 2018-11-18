package GeneracionAssembler;


import java.util.List;
import java.util.Set;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

import AnalizadorLexico.Analizador_Lexico;
import AnalizadorLexico.FileManager;
import AnalizadorLexico.Token;
import GeneracionCodigoIntermedio.Polaca_Inversa;

public class GeneradorAssembler {
    private Stack<StringBuilder> pilaVar = new Stack<StringBuilder>();
    Polaca_Inversa PI = null;
    StringBuilder codigo = new StringBuilder("\r\n");
    StringBuilder inicio = new StringBuilder();
    public int contador = 0;
    StringBuilder funciones = new StringBuilder("\r\n");
    StringBuilder declaracion = new StringBuilder("\r\n");
    boolean estaEnFuncion = false;
    private List<Integer> saltitos = new ArrayList<Integer>();
	StringBuilder signo = new StringBuilder();
	StringBuilder aux_parametro = new StringBuilder();
	StringBuilder aux_param_declarada = new StringBuilder();
	StringBuilder fin = new StringBuilder("\r\n");
	StringBuilder primerComparado = new StringBuilder();
	StringBuilder segundoComparado = new StringBuilder();
	//Set<StringBuilder> variables = new HashSet<StringBuilder>();
	List<String>variables = new ArrayList<String>();
    
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
					codigo.append("Label"+i+":"+"\r\n");
				else
					funciones.append("Label"+i+":"+"\r\n");
    		}
			
    		if(PI.getPI().get(i).charAt(0) == '_'){
    			if(!variables.contains(PI.getPI().get(i).toString())){
    				variables.add(PI.getPI().get(i).toString());
    			}
    		}
    		
    		pilaVar.push(PI.getPI().get(i));   //SACO ELEMENTOS DE LA POLACA
    		
		
			if(pilaVar.peek().toString().equals("inicio_funcion")){ //ME FIJO SI EMPIEZA UNA FUNCION PORQUE ESCRIBE EN OTRO LADO
				pilaVar.pop();
				StringBuilder nom_fun = pilaVar.pop();
				aux_param_declarada = pilaVar.pop();
				funciones.append("@FUNCTION_"+nom_fun+"\r\n");
				funciones.append("MOV EAX, @aux_param"+"\r\n"+"MOV "+aux_param_declarada+", EAX"+"\r\n"); //asigno el valor del parametro real al de la funcion
				estaEnFuncion = true; //HAGO QUE EMPIEZE A ESCRIBIR EN LA PARTE DE FUNCIONES
			}
			else{
			
				if((pilaVar.peek().toString()).equals("return")){ //LEE EL RETURN DE UNA FUNCION
					pilaVar.pop();
					StringBuilder retorno = pilaVar.pop();
					System.out.println("Retorno "+retorno);
					Token t = null; 
						if(Analizador_Lexico.tablaSimbolos.get(retorno.toString()).tipo.equals("uslinteger")){
								funciones.append("MOV EAX, " +retorno + "\r\n" + "MOV @aux_fun, EAX" +"\r\n" +"RET" +"\r\n \r\n");
								t = new Token("@aux_fun",Analizador_Lexico.TOKEN_UL,"uslinteger"); //VER
						}
						
						if(Analizador_Lexico.tablaSimbolos.get(retorno.toString()).tipo.equals("single")){
								funciones.append("FLD "+retorno+ "\r\n" + "FSTP @aux_fun"+"\r\n" +"RET" +"\r\n \r\n");
								t = new Token("@aux_fun",Analizador_Lexico.TOKEN_FLOAT,"single"); //VER
						}//ACA ARRIBA SAQUE retorno.toString() y lo reemplaze por "@aux_fun"
					
					estaEnFuncion = false;
					StringBuilder aux_fun= new StringBuilder("@aux_fun"); //crea la var?
					Analizador_Lexico.tablaSimbolos.put(t.lexema,t);  //agrega a la tabla de simbolos
					pilaVar.push(aux_fun);		//SETEA ENFUNCION A FALSE PARA QUE VUELVA A ESCRIBIR EN CODIGO
		    	}
			   else{
					if(!estaEnFuncion)
						generarCodigoAssembler(codigo);
					else
						generarCodigoAssembler(funciones);
			    		
					}
    		}
    	}
	}		
			
			
public void generarCodigoAssembler(StringBuilder escritura){
	
	//ASIGNACIONES	
	if(pilaVar.peek().toString().equals(":=")){ 
		pilaVar.pop(); //SACO EL :=
		StringBuilder aAsignar = desapilar();
		StringBuilder asignacion = desapilar();
		//Token t = null;
		if((Analizador_Lexico.tablaSimbolos.get(aAsignar.toString()).tipo.equals("uslinteger"))&&(Analizador_Lexico.tablaSimbolos.get(asignacion.toString()).tipo.equals("uslinteger"))){
			escritura.append("MOV EAX, "+asignacion+"\r\n"+"MOV "+aAsignar+", EAX"+"\r\n" ); //ver si va lo de mov @aux0, EAX despues de esto
		}
		else{
			System.out.println("A asignar: "+aAsignar+"  asignacion: "+asignacion.toString());
			if((Analizador_Lexico.tablaSimbolos.get(aAsignar.toString()).tipo.equals("single"))&&(Analizador_Lexico.tablaSimbolos.get(asignacion.toString()).tipo.equals("single"))){
					escritura.append("FLD "+asignacion+"\r\n"+"FSTP "+ aAsignar+"\r\n"); //creo que seria FLD asignacion + FSTP aAsignar
			}
			else 
				escritura.append("JMP @LABEL_TIPOS_DISTINTOS"+"\r\n");
		}

	}

	else{
	
		//LLAMADO DE FUNCIONES
		if(pilaVar.peek().toString().equals("CALL")){
			pilaVar.pop(); //SACO EL CALL
			StringBuilder fun_llamada = pilaVar.pop();
			aux_parametro = pilaVar.pop(); //asigno el valor del parametro
			escritura.append("MOV EAX, "+aux_parametro+"\r\n"+"MOV @aux_param"+", EAX"+"\r\n"); //asigno el valor del parametro real al de la funcion
			escritura.append("CALL @FUNCTION_"+fun_llamada+"\r\n");
		}
		else{	
			//PARA COMPARACIONES	
			if(pilaVar.peek().toString().equals("<")||pilaVar.peek().toString().equals(">")||pilaVar.peek().toString().equals("=")||pilaVar.peek().toString().equals("!=")){
				signo = pilaVar.pop();
				 primerComparado = desapilar();
				 segundoComparado = desapilar();
				 System.out.println("Voy a comparar '"+primerComparado+"' con '"+segundoComparado+"'");
				//AMBOS INTEGER
				if((Analizador_Lexico.tablaSimbolos.get(primerComparado.toString()).tipo.equals("uslinteger")) && (Analizador_Lexico.tablaSimbolos.get(segundoComparado.toString()).tipo.equals("uslinteger"))){
					escritura.append("MOV EAX, "+primerComparado+"\r\n"+"MOV EBX, "+segundoComparado+"\r\n"+"CMP EAX,EBX"+"\r\n");
					Token t = new Token("@aux"+contador,Analizador_Lexico.TOKEN_UL,"uslinteger");
					Analizador_Lexico.tablaSimbolos.put(t.lexema,t);
				}
				//AMBOS FLOAT 
				if((Analizador_Lexico.tablaSimbolos.get(primerComparado.toString()).tipo.equals("single")) && ((Analizador_Lexico.tablaSimbolos.get(segundoComparado.toString()).tipo.equals("single")))){
					escritura.append("FLD "+primerComparado+"\r\n"+"FLD "+segundoComparado+"\r\n"+"FCOM"+"\r\n");//+"FSTSW @aux"+contador+"\r\n"+"MOV EAX, @aux"+contador+"\r\n"+"SAHF"+"\r\n");
					Token t = new Token("@aux"+contador,Analizador_Lexico.TOKEN_FLOAT,"single");
					Analizador_Lexico.tablaSimbolos.put(t.lexema,t);
				}
				//DISTINTOS TIPOS
				Token t=null;
				if((Analizador_Lexico.tablaSimbolos.get(primerComparado.toString()).tipo.equals("uslinteger")) && ((Analizador_Lexico.tablaSimbolos.get(segundoComparado.toString()).tipo.equals("single"))))
					escritura.append("JMP @LABEL_TIPOS_DISTINTOS"+"\r\n");
				if((Analizador_Lexico.tablaSimbolos.get(primerComparado.toString()).tipo.equals("single")) && ((Analizador_Lexico.tablaSimbolos.get(segundoComparado.toString()).tipo.equals("uslinteger"))))
					escritura.append("JMP @LABEL_TIPOS_DISTINTOS"+"\r\n");
				
				StringBuilder aux= new StringBuilder("@aux"+contador);
				//No habria que agregar a la ts la aux aca? o mas arriba cuando sabes si es un single o un uslinteger
				pilaVar.push(aux);
				contador++;
			}
		else{
			//PARA SALTOS
			if(pilaVar.peek().toString().equals("B")){
				pilaVar.pop(); //SACO EL B
				StringBuilder label = pilaVar.pop();
				System.out.println("el label es: "+label);
				if(Integer.parseInt(label.substring(5,label.length()))>=PI.getPI().size()) //VERIFICO, SI NO CUMPLE LLEVA AL FINAL //ACA CREO ES 5 NO 6
					label =new StringBuilder("@LABEL_END"+"\r\n");
				
				if(signo.toString().equals("="))
						escritura.append("JNE "+label+"\r\n");
					
				if(signo.toString().equals("!="))
						escritura.append("JE "+label+"\r\n");
					
				if(Analizador_Lexico.tablaSimbolos.get(primerComparado.toString()).tipo.equals("uslinteger")){
					if(signo.toString().equals("<"))
						escritura.append("JGL "+label+"\r\n");
					
					if(signo.toString().equals(">"))
						escritura.append("JLE "+label+"\r\n");
				}
				if(Analizador_Lexico.tablaSimbolos.get(primerComparado.toString()).tipo.equals("single")){
					if(signo.toString().equals("<"))
						escritura.append("JAE "+label+"\r\n");
					
					if(signo.toString().equals(">"))
						escritura.append("JBE "+label+"\r\n");
				}
			}
			else{
				if(pilaVar.peek().toString().equals("BT")){
					pilaVar.pop(); //SACO EL BT
					StringBuilder label = pilaVar.pop();
					if(Integer.parseInt(label.substring(5,label.length()))>=PI.getPI().size()) //VERIFICO SI ESTO LLEVA AL FINAL
						label =new StringBuilder("@LABEL_END"+"\r\n");
					escritura.append("JMP "+label+"\r\n");
				}	
				else{
					//PARA PRINTS
					if(pilaVar.peek().toString().equals("print")){
					pilaVar.pop(); //SACO LA PALABRA PRINT
						if(pilaVar.peek().toString().contains("'")){
							StringBuilder mensajito = pilaVar.pop();
							escritura.append("invoke MessageBox, NULL, addr "+mensajito+", addr "+mensajito+", MB_OK"+"\r\n");
						}
						else{
							StringBuilder variable = pilaVar.pop();
							if(Analizador_Lexico.tablaSimbolos.get(variable.toString()).tipo.equals("single"))
								escritura.append("invoke printf, cfm$(%.20Lf\\r\n), "+variable+"\r\n");
							if(Analizador_Lexico.tablaSimbolos.get(variable.toString()).tipo.equals("uslinteger"))
								escritura.append("invoke printf, cfm$(%.%llu\\r\n), "+variable+"\r\n");
						}
					}
					else{
						//PARA OPERACIONES ARITMETICAS
					    if(pilaVar.peek().toString().equals("+") || pilaVar.peek().toString().equals("-")|| pilaVar.peek().toString().equals("*") || pilaVar.peek().toString().equals("/")){
							StringBuilder operador = desapilar(); //PARA SACAR EL OPERANDO
							StringBuilder primerOperando = desapilar();
							StringBuilder segundoOperando = desapilar();
							System.out.println("Voy a operar '"+primerOperando+"' con '"+segundoOperando+"'");
							if((Analizador_Lexico.tablaSimbolos.get(primerOperando.toString()).tipo.equals("uslinteger")) && ((Analizador_Lexico.tablaSimbolos.get(segundoOperando.toString()).tipo.equals("uslinteger"))))
								generarCodigoInteger(operador ,primerOperando,segundoOperando,escritura);
							if((Analizador_Lexico.tablaSimbolos.get(primerOperando.toString()).tipo.equals("single")) && ((Analizador_Lexico.tablaSimbolos.get(segundoOperando.toString()).tipo.equals("single"))))
								generarCodigoSingle(operador,primerOperando,segundoOperando,escritura);
							if((Analizador_Lexico.tablaSimbolos.get(primerOperando.toString()).tipo.equals("uslinteger")) && ((Analizador_Lexico.tablaSimbolos.get(segundoOperando.toString()).tipo.equals("single"))))
								escritura.append("JMP @LABEL_END"+"\r\n");
							if((Analizador_Lexico.tablaSimbolos.get(primerOperando.toString()).tipo.equals("single")) && ((Analizador_Lexico.tablaSimbolos.get(segundoOperando.toString()).tipo.equals("uslinteger"))))
								escritura.append("JMP @LABEL_END"+"\r\n");
						}
					}
				}
			}
		}
	}
	}
}
		
public void generarCodigoInteger(StringBuilder operador,StringBuilder primerOperando, StringBuilder segundoOperando, StringBuilder escritura){ //para los dos integer
    	String op = operador.toString();
    	StringBuilder aux = null;
    	switch(op){
    	case "+" :  escritura.append("MOV EAX,"+primerOperando+"\r\n"+"ADD EAX, "+segundoOperando+"\r\n"+"MOV "+"@aux"+contador+", EAX"+"\r\n");
    				aux= new StringBuilder("@aux"+contador);
    				pilaVar.push(aux);  				
    				
    	break;
    	case"-" : escritura.append("MOV EAX,"+segundoOperando+"\r\n"+"CMP EAX, "+primerOperando+"\r\n"+"JL @LABEL_RESUL_NEG"+"\r\n"+"SUB EAX, "+primerOperando+"\r\n"+ "MOV "+"@aux"+contador+", EAX"+"\r\n");
  				  aux= new StringBuilder("@aux"+contador);
  				  pilaVar.push(aux);
		 		 
    	break;
    	case"*": escritura.append("MOV EAX,"+primerOperando+"\r\n"+"MUL EAX, "+segundoOperando+"\r\n"+"JO @LABEL_OVF"+"\r\n"+ "MOV "+"@aux"+contador+", EAX"+"\r\n");
 				 aux= new StringBuilder("@aux"+contador);
 				 pilaVar.push(aux);
		break;		

    	case"/" : escritura.append("MOV EAX,"+segundoOperando+"\r\n"+"MOV EBX,"+primerOperando+"\r\n"+"CMP EBX, 0.0"+"\r\n"+"JZ @LABEL_ZERO"+"\r\n"+"DIV EAX, "+primerOperando+"\r\n"+ "MOV "+"@aux"+contador+", EAX"+"\r\n");
 				  aux= new StringBuilder("@aux"+contador);
 				  pilaVar.push(aux);
	
		 break;
    	}
		Token t = new Token("@aux"+contador,Analizador_Lexico.TOKEN_UL,"uslinteger");
		Analizador_Lexico.tablaSimbolos.put(t.lexema,t);
		contador++;
    }
    	
public void generarCodigoSingle(StringBuilder operador,StringBuilder primerOperando, StringBuilder segundoOperando, StringBuilder escritura){ //para los dos single
			String op = operador.toString();
			StringBuilder aux = null;
			switch(op){
			case "+" :  escritura.append("FLD "+primerOperando+"\r\n"+"FLD "+segundoOperando+"\r\n"+ "FADD"+"\r\n"+"FSTP "+"@aux"+contador+"\r\n");
						aux= new StringBuilder("@aux"+contador);
						pilaVar.push(aux);
					
			break;
			case"-" : escritura.append("FLD "+primerOperando+"\r\n"+"FLD "+segundoOperando+"\r\n"+"FCOM"+"\r\n"+"JB @LABEL_RESUL_NEG"+"\r\n"+ "FSUB"+"\r\n"+"FSTP "+"@aux"+contador+"\r\n");
					  aux= new StringBuilder("@aux"+contador);
					  pilaVar.push(aux);
					  
			break;
			case"*": escritura.append("FLD "+primerOperando+"\r\n"+"FLD "+segundoOperando+"\r\n"+ "FMUL"+"\r\n"+"FSTP "+"@aux"+contador+"\r\n"+"FLD cte_max_rango"+"\r\n"+"FLD @aux"+contador+"\r\n"+"FCOM"+"\r\n"+"FSTSW AX"+"\r\n"+"SAHF"+"\r\n"+"JA @LABEL_OVF"+"\r\n");
					 aux= new StringBuilder("@aux"+contador);
					 pilaVar.push(aux);		
					 
			break;		 

			case"/" :escritura.append("FLD "+segundoOperando+"\r\n"+"FLD "+primerOperando+"\r\n"+"FCOM 0"+"\r\n"+"JE @LABEL_ZERO"+"\r\n"+ "FDIV"+"\r\n"+"FSTP "+"@aux"+contador+"\r\n");
					 aux= new StringBuilder("@aux"+contador);
					 pilaVar.push(aux);
					
			break;
			}
			Token t = new Token("@aux"+contador,Analizador_Lexico.TOKEN_FLOAT,"single");
			Analizador_Lexico.tablaSimbolos.put(t.lexema,t);
			contador++;
}
   

 					
					

	    						


    public void generarDeclaracion(){

    	declaracion.append("mensaje_overflow db \"Hubo overflow uwu\", 0 "+"\r\n");
    	declaracion.append("mensaje_zero db \"El divisor es 0 huehuehue\", 0 "+"\r\n");
    	declaracion.append("mensaje_resultadoNeg db \"Me dio negativo xD\", 0 "+"\r\n");
    	declaracion.append("@aux_fun dd?"+"\r\n");
    	declaracion.append("@aux_param dd?"+"\r\n");
    	for(int i=0; i<contador; i++){
    		declaracion.append("@aux"+i+" dd?"+"\r\n");
    	}
    	
    	for(int j=0; j<variables.size(); j++)
    		declaracion.append(variables.get(j)+" dd?"+"\r\n");
        //recorrer todo al final para ver cuales se usaron y cuales no, alto rip
        declaracion.append(".const "+"\r\n"+" cte_max_rango  equ 3.40282347E+38"+"\r\n");
    	declaracion.append(".code"+"\r\n"+"start: ");
    }
   
 
    public void generarEncabezado(){
    	inicio.append(".386"+"\r\n"+".model flat, stdcall"+"\r\n"+"option casemap :none "+"\r\n"+"include \\masm32\\include\\windows.inc"+"\r\n"+"include \\masm32\\include\\kernel32.inc"
    					+"\r\n"+"include \\masm32\\include\\masm32.inc"+"\r\n"+"includelib \\masm32\\lib\\kernel32.lib"+"\r\n"+"includelib \\masm32\\lib\\masm32.lib"+"\r\n"
    			+"\r\n"+"include \\masm32\\include\\masm32rt.inc"+"\r\n"+"dll_dllcrt0 PROTO C"+"\r\n"+"printf PROTO C: VARARG"+"\r\n"+".data"+"\r\n");   
    }
    
    public void generarMensajitosDeControl(){
    	fin.append("@LABEL_OVF:"+ "\r\n"+ "invoke MesseageBox,NULL,addr mensaje_overflow,addr"+"\r\n"+"mensaje_overflow,MB_OK"+"\r\n"+"JMP @LABEL_END"+"\r\n");
    	fin.append("@LABEL_ZERO:"+ "\r\n"+ "invoke MesseageBox,NULL,addr mensaje_zero,addr"+"\r\n"+"mensaje_zero,MB_OK"+"\r\n"+"JMP @LABEL_END"+"\r\n");
    	fin.append("@LABEL_RESUL_NEG:"+ "\r\n"+ "invoke MesseageBox,NULL,addr mensaje_resultadoNeg,addr"+"\r\n"+"mensaje_resultadoNeg,MB_OK"+"\r\n"+"JMP @LABEL_END"+"\r\n");
    }
    
    public void generarFin(){
    	fin.append("@LABEL_END:"+"\r\n"+"invoke ExitProcess, 0"+"\r\n"+"end start");
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
    
    public void generameAssemblydotexe() throws IOException{
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
    	FileManager.write(inicio.toString(), new File("prueba.asm"));
    	System.out.println("Genere assembler");

    }
    

}
