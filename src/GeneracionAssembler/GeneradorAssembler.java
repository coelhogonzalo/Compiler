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
    private Polaca_Inversa PI = null;
    private StringBuilder codigo = new StringBuilder("\r\n");
    private StringBuilder inicio = new StringBuilder();
    private int contador = 0;
    private int contmsj = 0;
    private StringBuilder funciones = new StringBuilder("\r\n");
    private StringBuilder declaracion = new StringBuilder("\r\n");
    private boolean estaEnFuncion = false;
    private List<Integer> saltitos = new ArrayList<Integer>();
    private StringBuilder signo = new StringBuilder();
    private StringBuilder aux_parametro = new StringBuilder();
    private StringBuilder aux_param_declarada = new StringBuilder();
    private StringBuilder fin = new StringBuilder("\r\n");
    private StringBuilder primerComparado = new StringBuilder();
    private StringBuilder segundoComparado = new StringBuilder();
	//Set<StringBuilder> variables = new HashSet<StringBuilder>();
    private List<String>variables = new ArrayList<String>();
    private List<String>mensajes = new ArrayList<String>();
    private List<String>flotantes = new ArrayList<String>();
    
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
			
    		if(PI.getPI().get(i).charAt(0) == '_'){ //para identificar variables
    			if(!variables.contains(PI.getPI().get(i).toString())){
    				variables.add(PI.getPI().get(i).toString());
    			}
    		}
    		
    		if(PI.getPI().get(i).charAt(0) == '\''){ //para hacer variables de las cadenas
    			//if(!mensajes.contains(PI.getPI().get(i).toString())){
    				mensajes.add(PI.getPI().get(i).toString());
    			//}
    		}

    		if(PI.getPI().get(i).toString().contains(".")){
	    		if(Analizador_Lexico.tablaSimbolos.get((PI.getPI().get(i)).toString()).tipo.equals("single")&&(PI.getPI().get(i).toString().charAt(0) != '_')){ //para hacer constantes de los float
	    			if(!flotantes.contains(PI.getPI().get(i).toString())){
	    				flotantes.add(PI.getPI().get(i).toString());
	    			}
	    		}
    		}
    		
    		pilaVar.push(PI.getPI().get(i));   //SACO ELEMENTOS DE LA POLACA
    		
		
			if(pilaVar.peek().toString().equals("inicio_funcion")){ //ME FIJO SI EMPIEZA UNA FUNCION PORQUE ESCRIBE EN OTRO LADO
				pilaVar.pop();
				StringBuilder nom_fun = pilaVar.pop();
				aux_param_declarada = pilaVar.pop();
				funciones.append("@FUNCTION_"+nom_fun+":"+"\r\n");
				funciones.append("MOV EAX, @aux_param"+"\r\n"+"MOV "+aux_param_declarada+", EAX"+"\r\n"); //asigno el valor del parametro real al de la funcion
				estaEnFuncion = true; //HAGO QUE EMPIEZE A ESCRIBIR EN LA PARTE DE FUNCIONES
			}
			else{
			
				if((pilaVar.peek().toString()).equals("return")){ //LEE EL RETURN DE UNA FUNCION
					//System.out.println(" la pila :");
					//System.out.println(pilaVar.toString());
					pilaVar.pop();
					StringBuilder retorno = pilaVar.pop();
					//System.out.println("Retorno "+retorno);
					Token t = null; 
						if(Analizador_Lexico.tablaSimbolos.get(retorno.toString()).tipo.equals("uslinteger")){
							StringBuilder elOriginalPapa = new StringBuilder(retorno.toString());
				    		if(Analizador_Lexico.tablaSimbolos.get(retorno.toString()).uso.equals("constante")){	//SACO _UL
				    			retorno = new StringBuilder(retorno.substring(0, retorno.length()-3));
				    		}
				    		funciones.append("MOV EAX, " +retorno + "\r\n" + "MOV @aux_fun, EAX" +"\r\n" +"RET" +"\r\n \r\n");
							t = new Token("@aux_fun",Analizador_Lexico.TOKEN_UL,"uslinteger"); //VER
							t.setUso(Analizador_Lexico.tablaSimbolos.get(elOriginalPapa.toString()).uso); //no se me ocurria como volver el token a _ul asique cree una copia, que mal Emanuel
							//System.out.println(elOriginalPapa+" y el uso que le quedo a '"+t.lexema+"' es: "+t.uso);
						}
						
						if(Analizador_Lexico.tablaSimbolos.get(retorno.toString()).tipo.equals("single")){
							if(flotantes.contains(retorno.toString()))
								retorno= new StringBuilder("_"+retorno.toString().replace(".", "_"));
							funciones.append("FLD "+retorno+ "\r\n" + "FSTP @aux_fun"+"\r\n" +"RET" +"\r\n \r\n");
							t = new Token("@aux_fun",Analizador_Lexico.TOKEN_FLOAT,"single"); //VER
							if(flotantes.contains(retorno.toString().substring(1, retorno.length()).replace("_", ".")))
								t.setUso(Analizador_Lexico.tablaSimbolos.get(retorno.toString().substring(1, retorno.length()).replace("_", ".")).uso);
							else
								t.setUso(Analizador_Lexico.tablaSimbolos.get(retorno.toString()).uso);
							
						}//ACA ARRIBA SAQUE retorno.toString() y lo reemplaze por "@aux_fun"
					
					estaEnFuncion = false; //SETEA ENFUNCION A FALSE PARA QUE VUELVA A ESCRIBIR EN CODIGO
					//StringBuilder aux_fun= new StringBuilder("@aux_fun"); //crea la var?
					Analizador_Lexico.tablaSimbolos.put(t.lexema,t);  //agrega a la tabla de simbolos
					//pilaVar.push(aux_fun);		
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
		StringBuilder aAsignar = pilaVar.pop();
		StringBuilder asignacion = pilaVar.pop();
		//System.out.println("Tengo que asignar a "+aAsignar + " el valor "+asignacion);
		//System.out.println(Analizador_Lexico.tablaSimbolos);
		if((Analizador_Lexico.tablaSimbolos.get(aAsignar.toString()).tipo.equals("uslinteger"))&&(Analizador_Lexico.tablaSimbolos.get(asignacion.toString()).tipo.equals("uslinteger"))){
    		if(Analizador_Lexico.tablaSimbolos.get(asignacion.toString()).uso!=null){
				if(Analizador_Lexico.tablaSimbolos.get(asignacion.toString()).uso.equals("constante")){
	    			asignacion = new StringBuilder(asignacion.substring(0, asignacion.length()-3));
	    		}
				
		
    		}
    		escritura.append("MOV EAX, "+asignacion+"\r\n"+"MOV "+aAsignar+", EAX"+"\r\n" ); //ver si va lo de mov @aux0, EAX despues de esto
		}	
		else{
			//System.out.println("A asignar: "+aAsignar+"  asignacion: "+asignacion.toString());
			if((Analizador_Lexico.tablaSimbolos.get(aAsignar.toString()).tipo.equals("single"))&&(Analizador_Lexico.tablaSimbolos.get(asignacion.toString()).tipo.equals("single"))){
				if(flotantes.contains(asignacion.toString()))
					if(asignacion.toString().contains("-"))
						asignacion = new StringBuilder("_neg"+asignacion.toString().replace(".", "_").substring(1,asignacion.toString().length()));	
					else
						asignacion = new StringBuilder("_"+asignacion.toString().replace(".", "_"));
				escritura.append("FLD "+asignacion+"\r\n"+"FSTP "+ aAsignar+"\r\n");
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
			if((Analizador_Lexico.tablaSimbolos.get(aux_parametro.toString()).tipo.equals("uslinteger")))
				escritura.append("MOV EAX, "+aux_parametro+"\r\n"+"MOV @aux_param"+", EAX"+"\r\n"); //asigno el valor del parametro real al de la funcion
			else
				escritura.append("FLD "+aux_parametro+"\r\n"+"FSTP @aux_param"+"\r\n");
			escritura.append("CALL @FUNCTION_"+fun_llamada+"\r\n");
			StringBuilder aux_fun= new StringBuilder("@aux_fun"); //crea la var?
			pilaVar.push(aux_fun);		
		}
		else{	
			//PARA COMPARACIONES	
			if(pilaVar.peek().toString().equals("<")||pilaVar.peek().toString().equals(">")||pilaVar.peek().toString().equals("=")||pilaVar.peek().toString().equals("!=")){
				signo = pilaVar.pop();
				primerComparado = pilaVar.pop();
				segundoComparado = pilaVar.pop();
				//System.out.println("Voy a comparar :'"+primerComparado+"' con: '"+segundoComparado+"'");
				//AMBOS INTEGER
				if((Analizador_Lexico.tablaSimbolos.get(primerComparado.toString()).tipo.equals("uslinteger")) && (Analizador_Lexico.tablaSimbolos.get(segundoComparado.toString()).tipo.equals("uslinteger"))){
		    		if(Analizador_Lexico.tablaSimbolos.get(primerComparado.toString()).uso.equals("constante")){
		    			primerComparado = new StringBuilder(primerComparado.substring(0, primerComparado.length()-3));
		    		}
		    		if(Analizador_Lexico.tablaSimbolos.get(segundoComparado.toString()).uso.equals("constante")){
		    			segundoComparado = new StringBuilder(segundoComparado.substring(0, segundoComparado.length()-3));
		    		}
					escritura.append("MOV EAX, "+primerComparado+"\r\n"+"MOV EBX, "+segundoComparado+"\r\n"+"CMP EBX,EAX"+"\r\n");
				}
				else{
					//AMBOS FLOAT 
					if((Analizador_Lexico.tablaSimbolos.get(primerComparado.toString()).tipo.equals("single")) && ((Analizador_Lexico.tablaSimbolos.get(segundoComparado.toString()).tipo.equals("single")))){
						if(flotantes.contains(primerComparado.toString())){
							if(primerComparado.toString().contains("-"))
								primerComparado = new StringBuilder("_neg"+primerComparado.toString().replace(".", "_").substring(1,primerComparado.toString().length()));	
							else
								primerComparado=new StringBuilder("_"+primerComparado.toString().replace(".", "_"));
						}
						if(flotantes.contains(segundoComparado.toString())){
							if(segundoComparado.toString().contains("-"))
								segundoComparado = new StringBuilder("_neg"+segundoComparado.toString().replace(".", "_").substring(1,segundoComparado.toString().length()));	
							else
								segundoComparado=new StringBuilder("_"+segundoComparado.toString().replace(".", "_"));
						}
						escritura.append("FLD "+primerComparado+"\r\n"+"FLD "+segundoComparado+"\r\n"+"FCOM"+"\r\n"+"FSTSW AX"+"\r\n"+"SAHF"+"\r\n");
					}
					else{
						//DISTINTOS TIPOS
						if((Analizador_Lexico.tablaSimbolos.get(primerComparado.toString()).tipo.equals("uslinteger")) && ((Analizador_Lexico.tablaSimbolos.get(segundoComparado.toString()).tipo.equals("single"))))
							escritura.append("JMP @LABEL_TIPOS_DISTINTOS"+"\r\n");
						else{
							if((Analizador_Lexico.tablaSimbolos.get(primerComparado.toString()).tipo.equals("single")) && ((Analizador_Lexico.tablaSimbolos.get(segundoComparado.toString()).tipo.equals("uslinteger"))))
								escritura.append("JMP @LABEL_TIPOS_DISTINTOS"+"\r\n");
						}
					}
				}
			}
		else{
			//PARA SALTOS
			if(pilaVar.peek().toString().equals("B")){
				pilaVar.pop(); //SACO EL B
				StringBuilder label = pilaVar.pop();
				System.out.println("Entre al primer if");
				if(Integer.parseInt(label.substring(5,label.length()))>=PI.getPI().size()) //VERIFICO, SI NO CUMPLE LLEVA AL FINAL //ACA CREO ES 5 NO 6
					label =new StringBuilder("@LABEL_END"+"\r\n");
				
				if(signo.toString().equals("="))
						escritura.append("JNE "+label+"\r\n");
				else{System.out.println("Entre al segundo if");	
					if(signo.toString().equals("!="))
							escritura.append("JE "+label+"\r\n");
					else{
						System.out.println("Entre al tercer if");
						System.out.println("El valor del comparado es: "+primerComparado);
			    		if(!(primerComparado.toString().contains("_ul"))&&(!flotantes.contains(primerComparado.toString().substring(1, primerComparado.length()).replace("_", ".")))){
			    			System.out.println("Entre al cuarto MALLLL if");
			    			if(!((Analizador_Lexico.tablaSimbolos.get(primerComparado.toString())!=null)&&(Analizador_Lexico.tablaSimbolos.get(primerComparado.toString()).uso.equals("variable"))))
			    				primerComparado = new StringBuilder(primerComparado.toString()+"_ul");
							
			    			if(Analizador_Lexico.tablaSimbolos.get(primerComparado.toString()).tipo.equals("uslinteger")){
								//System.out.println("entro segundo if"+primerComparado);
								
								if(signo.toString().equals("<"))
									escritura.append("JGE "+label+"\r\n");
								
								if(signo.toString().equals(">"))
									escritura.append("JLE "+label+"\r\n");
							}
			    		}
			    		else{ //FLOAT
			    			System.out.println("Entre al cuarto if bien");
							if(flotantes.contains(primerComparado.toString().substring(1, primerComparado.length()).replace("_", "."))){
								primerComparado = new StringBuilder(primerComparado.toString().substring(1, primerComparado.length()).replace("_", "."));
								
							}
							System.out.println("El primer comparando: "+primerComparado.toString());
							if(Analizador_Lexico.tablaSimbolos.get(primerComparado.toString()).tipo.equals("single")){
							
								if(signo.toString().equals("<"))
									escritura.append("JAE "+label+"\r\n"); //PORQUE NO SE
									
								if(signo.toString().equals(">"))
									escritura.append("JBE "+label+"\r\n"); //AAAA WHY
							}
			    		}
			    	}
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
							escritura.append("invoke MessageBox, NULL, addr "+"msj"+contmsj+", addr "+"msj"+contmsj+", MB_OK"+"\r\n");
							contmsj++;
						}
						else{
							StringBuilder variable = pilaVar.pop();
							//System.out.println("Quiero printear esta: "+variable);
							if(Analizador_Lexico.tablaSimbolos.get(variable.toString()).tipo.equals("single"))
								escritura.append("invoke MessageBox,NULL,addr print_single,addr "+"print_single,MB_OK"+"\r\n");
							if(Analizador_Lexico.tablaSimbolos.get(variable.toString()).tipo.equals("uslinteger"))
								escritura.append("invoke printf, cfm$(\"%d\\n\"), "+variable+"\r\n");
						}
					}
					else{
						//PARA OPERACIONES ARITMETICAS
					    if(pilaVar.peek().toString().equals("+") || pilaVar.peek().toString().equals("-")|| pilaVar.peek().toString().equals("*") || pilaVar.peek().toString().equals("/")){
							StringBuilder operador = pilaVar.pop(); //PARA SACAR EL OPERANDO
							StringBuilder primerOperando = pilaVar.pop();
							StringBuilder segundoOperando = pilaVar.pop();
							//System.out.println("Voy a operar '"+primerOperando+"' con '"+segundoOperando+"'");
							
							if((Analizador_Lexico.tablaSimbolos.get(primerOperando.toString()).tipo.equals("uslinteger")) && ((Analizador_Lexico.tablaSimbolos.get(segundoOperando.toString()).tipo.equals("uslinteger")))){
					    		if(Analizador_Lexico.tablaSimbolos.get(primerOperando.toString()).uso.equals("constante")){
					    			primerOperando = new StringBuilder(primerOperando.substring(0, primerOperando.length()-3));
					    		}
					    		if(Analizador_Lexico.tablaSimbolos.get(segundoOperando.toString()).uso.equals("constante")){
					    			segundoOperando = new StringBuilder(segundoOperando.substring(0, segundoOperando.length()-3));
					    		}
								generarCodigoInteger(operador ,primerOperando,segundoOperando,escritura);
							}
							else{
								if((Analizador_Lexico.tablaSimbolos.get(primerOperando.toString()).tipo.equals("single")) && ((Analizador_Lexico.tablaSimbolos.get(segundoOperando.toString()).tipo.equals("single")))){
									if(flotantes.contains(primerOperando.toString())){
										if(primerOperando.toString().contains("-"))
											primerOperando = new StringBuilder("_neg"+primerOperando.toString().replace(".", "_").substring(1,primerOperando.toString().length()));	
										else
											primerOperando=new StringBuilder("_"+primerOperando.toString().replace(".", "_"));
									}
									if(flotantes.contains(segundoOperando.toString())){
										if(segundoOperando.toString().contains("-"))
											segundoOperando = new StringBuilder("_neg"+segundoOperando.toString().replace(".", "_").substring(1,segundoOperando.toString().length()));	
										else
											segundoOperando=new StringBuilder("_"+segundoOperando.toString().replace(".", "_"));
									}
							
									generarCodigoSingle(operador,primerOperando,segundoOperando,escritura);
							}
								else{
									if((Analizador_Lexico.tablaSimbolos.get(primerOperando.toString()).tipo.equals("uslinteger")) && ((Analizador_Lexico.tablaSimbolos.get(segundoOperando.toString()).tipo.equals("single")))){
										escritura.append("JMP @LABEL_TIPOS_DISTINTOS"+"\r\n");
										StringBuilder aux= new StringBuilder("@auxDistintosTipos"+contador);
										pilaVar.push(aux);  
										Token t = new Token("@auxDistintosTipos"+contador,Analizador_Lexico.TOKEN_UL,"uslinteger");
										t.setUso("variable");
										Analizador_Lexico.tablaSimbolos.put(t.lexema,t);
								}
									else{
										if((Analizador_Lexico.tablaSimbolos.get(primerOperando.toString()).tipo.equals("single")) && ((Analizador_Lexico.tablaSimbolos.get(segundoOperando.toString()).tipo.equals("uslinteger"))))
											escritura.append("JMP @LABEL_TIPOS_DISTINTOS"+"\r\n");
											StringBuilder aux= new StringBuilder("@auxDistintosTipos"+contador);
											pilaVar.push(aux);  
											Token t = new Token("@auxDistintosTipos"+contador,Analizador_Lexico.TOKEN_UL,"single");
											t.setUso("variable");
											Analizador_Lexico.tablaSimbolos.put(t.lexema,t);
									 }
									}
								}
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
    	case"*": escritura.append("MOV EAX,"+primerOperando+"\r\n"+"MUL "+segundoOperando+"\r\n"+"JO @LABEL_OVF"+"\r\n"+ "MOV "+"@aux"+contador+", EAX"+"\r\n");
 				 aux= new StringBuilder("@aux"+contador);
 				 pilaVar.push(aux);
		break;		

    	case"/" : escritura.append("MOV EDX, 0"+"\r\n"+"MOV EAX,"+segundoOperando+"\r\n"+"MOV EBX,"+primerOperando+"\r\n"+"CMP EBX, 0"+"\r\n"+"JZ @LABEL_ZERO"+"\r\n"+"DIV EBX"+"\r\n"+ "MOV "+"@aux"+contador+", EAX"+"\r\n");
 				  aux= new StringBuilder("@aux"+contador);
 				  pilaVar.push(aux);
	
		 break;
    	}
		Token t = new Token("@aux"+contador,Analizador_Lexico.TOKEN_UL,"uslinteger");
		t.setUso("variable");
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
			case"-" : escritura.append("FLD "+segundoOperando+"\r\n"+"FLD "+primerOperando+"\r\n"+ "FSUB"+"\r\n"+"FSTP "+"@aux"+contador+"\r\n");
					  aux= new StringBuilder("@aux"+contador);
					  pilaVar.push(aux);
					  
			break;
			case"*": escritura.append("FLD "+primerOperando+"\r\n"+"FLD "+segundoOperando+"\r\n"+ "FMUL"+"\r\n"+"FSTP "+"@aux"+contador+"\r\n"+"FLD cte_max_rango"+"\r\n"+"FLD @aux"+contador+"\r\n"+"FCOM"+"\r\n"+"FSTSW AX"+"\r\n"+"SAHF"+"\r\n"+"JA @LABEL_OVF"+"\r\n");
					 aux= new StringBuilder("@aux"+contador);
					 pilaVar.push(aux);		
					 
			break;		 

			case"/" :escritura.append("FLD "+segundoOperando+"\r\n"+"FLD "+primerOperando+"\r\n"+"FCOM zero"+"\r\n"+"FSTSW AX"+"\r\n"+"SAHF"+"\r\n"+"JE @LABEL_ZERO"+"\r\n"+ "FDIV"+"\r\n"+"FSTP "+"@aux"+contador+"\r\n");
					 aux= new StringBuilder("@aux"+contador);
					 pilaVar.push(aux);
					
			break;
			}
			
			Token t = new Token("@aux"+contador,Analizador_Lexico.TOKEN_FLOAT,"single");
			t.setUso("variable");
			Analizador_Lexico.tablaSimbolos.put(t.lexema,t);
			flotantes.add(t.lexema);
			contador++;
}
   

 					
					

	    						


    public void generarDeclaracion(){

    	declaracion.append("mensaje_overflow db \"La operacion aritmetica genero overflow\", 0 "+"\r\n");
    	declaracion.append("mensaje_zero db \"Division por cero\", 0 "+"\r\n");
    	declaracion.append("mensaje_resultadoNeg db \"El resultado es negativo\", 0 "+"\r\n");
    	declaracion.append("mensaje_tipos db \"Se esta operando con tipos diferentes\", 0 "+"\r\n");
    	declaracion.append("print_single  db \"Solo se pueden printear variables de tipo uslinteger\", 0 "+"\r\n");
    	declaracion.append("zero  dd 0.0"+"\r\n");
        declaracion.append("cte_max_rango  dd 3.40282347E+38"+"\r\n");
        
    	
    	for(int k=0; k<mensajes.size(); k++)
    		declaracion.append("msj"+k+" db \""+mensajes.get(k).substring(1, mensajes.get(k).length()-1)+"\", 0"+"\n");
    	
    	declaracion.append(".data?"+"\n");
    	declaracion.append("@aux_fun dd ?"+"\r\n");
    	declaracion.append("@aux_param dd ?"+"\r\n");
    	
    	for(int i=0; i<contador; i++){
    		declaracion.append("@aux"+i+" dd ?"+"\r\n");
    	}
    	
    	for(int j=0; j<variables.size(); j++)
    		declaracion.append(variables.get(j)+" dd ?"+"\r\n");
    	

        declaracion.append(".const "+"\r\n");
        for(int uwu=0; uwu<flotantes.size(); uwu++){
        	String aVerSiAhoraTenesGanasDeAndar = flotantes.get(uwu).toString().replace(".", "_");
        	if(flotantes.get(uwu).toString().contains("-"))
        		declaracion.append("_neg"+aVerSiAhoraTenesGanasDeAndar.substring(1,aVerSiAhoraTenesGanasDeAndar.length())+" dd "+ flotantes.get(uwu)+"\n");
        	else
        		declaracion.append("_"+aVerSiAhoraTenesGanasDeAndar+" dd "+ flotantes.get(uwu)+"\n");
        }
    	declaracion.append(".code"+"\r\n"+"start: ");
    }
   
 
    public void generarEncabezado(){
    	inicio.append(".386"+"\r\n"+".model flat, stdcall"+"\r\n"+"option casemap :none "+"\r\n"+"include \\masm32\\include\\windows.inc"+"\r\n"+"include \\masm32\\include\\kernel32.inc"
    					+"\r\n"+"include \\masm32\\include\\masm32.inc"+"\r\n"+"includelib \\masm32\\lib\\kernel32.lib"+"\r\n"+"includelib \\masm32\\lib\\masm32.lib"+"\r\n"
    			+"\r\n"+"include \\masm32\\include\\masm32rt.inc"+"\r\n"+"dll_dllcrt0 PROTO C"+"\r\n"+"printf PROTO C: VARARG"+"\r\n"+".data"+"\r\n");   
    }
    
    public void generarMensajitosDeControl(){
    	fin.append("@LABEL_OVF:"+ "\r\n"+ "invoke MessageBox,NULL,addr mensaje_overflow,addr "+"mensaje_overflow,MB_OK"+"\r\n"+"JMP @LABEL_END"+"\r\n");
    	fin.append("@LABEL_ZERO:"+ "\r\n"+ "invoke MessageBox,NULL,addr mensaje_zero,addr "+"mensaje_zero,MB_OK"+"\r\n"+"JMP @LABEL_END"+"\r\n");
    	fin.append("@LABEL_RESUL_NEG:"+ "\r\n"+ "invoke MessageBox,NULL,addr mensaje_resultadoNeg,addr "+"mensaje_resultadoNeg,MB_OK"+"\r\n"+"JMP @LABEL_END"+"\r\n");
    	fin.append("@LABEL_TIPOS_DISTINTOS:"+"\r\n"+ "invoke MessageBox,NULL,addr mensaje_tipos,addr "+"mensaje_tipos,MB_OK"+"\r\n"+"JMP @LABEL_END"+"\r\n");
    }
    
    public void generarFin(){
    	fin.append("@LABEL_END:"+"\r\n"+"invoke ExitProcess, 0"+"\r\n"+"end start");
    }
    
//Cosas que me dijo anto: recorrer toda la polaca para crear la declaracion de variable, osea hacerlo al final
    //No hay anidamiento de funciones
    //Las funciones escribirlas al final del programa
    //poner en la declaracion de variables una constante con el maximo para la comparacion de overflow de single
    //despues de la declaracion de variables concatenar el .code y el start:
    
    
    private StringBuilder apilar(StringBuilder s){
    	return pilaVar.push(s);
    }
    
    public void generameAssemblydotexe(String fileName) throws IOException{
    	generarEncabezado();
    	guardarSaltos();
    	leer();
    	generarDeclaracion();
    	inicio.append(declaracion);
    	inicio.append(codigo);
    	inicio.append("JMP @LABEL_END");
    	inicio.append(funciones);    	
    	generarMensajitosDeControl();
    	generarFin();
    	inicio.append(fin);
    	System.out.println("Assembler generado:");
    	System.out.println(inicio);
    	FileManager.write(inicio.toString(), new File(fileName+".asm"));
    	

    }
    

}

