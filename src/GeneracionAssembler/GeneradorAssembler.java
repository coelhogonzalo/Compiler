package GeneracionAssembler;


import java.util.List;

import java.util.Set;
import java.io.File;
//import java.io.FileWriter;TODO SACAR A LA MIERDA ESTO SI NO SIRVE
import java.io.IOException;
//import java.io.InputStream;TODO SACAR A LA MIERDA ESTO SI NO SIRVE
//import java.io.PrintWriter;TODO SACAR A LA MIERDA ESTO SI NO SIRVE
//import java.lang.reflect.GenericArrayType;TODO SACAR A LA MIERDA ESTO SI NO SIRVE
import java.util.ArrayList;
//import java.util.HashSet;TODO SACAR A LA MIERDA ESTO SI NO SIRVE
import java.util.Stack;
import java.util.logging.Logger;

import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.FileManager;
import AnalizadorLexico.Token;
import GeneracionCodigoIntermedio.PolacaInversa;

public class GeneradorAssembler {
	private static Logger l=Logger.getLogger(GeneradorAssembler.class.toString());
    private Stack<StringBuilder> pilaVar = new Stack<StringBuilder>();
    private PolacaInversa PI = null;
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
    public GeneradorAssembler(PolacaInversa PI){
    	this.PI=PI;
    }
    
    //Analizador_Lexico.tablaSimbolos.get(PI.getPI().get(i)).tipo;TODO SACAR A LA MIERDA ESTO SI NO SIRVE
    //if(Analizador_Lexico.tablaSimbolos.get(PI.getPI().get(i)).tipo.equals("single")TODO SACAR A LA MIERDA ESTO SI NO SIRVE
    //if(Analizador_Lexico.tablaSimbolos.get(PI.getPI().get(i)).tipo.equals("uslinteger")TODO SACAR A LA MIERDA ESTO SI NO SIRVE
    
    public void guardarSaltos(){
    	ArrayList<StringBuilder> polaca=PI.getPI();
    	for(int i=0; i<polaca.size(); i++){
    		String buscado = polaca.get(i).toString();
    		if(buscado.contains("Label")){
    			int numero = Integer.parseInt(buscado.substring(5,buscado.length()));
    			if(!saltitos.contains(numero))
    				saltitos.add(numero);
    		}
    	}
    }
    
    
    //LEO DE LA POLACA
    public void leer(){
    	ArrayList<StringBuilder> polaca=PI.getPI();
    	for(int i=0; i<polaca.size(); i++){ //ESCRIBO LOS LABEL CUANDO LLEGUE A LA POSICION TAL
    		
    		if(saltitos.contains(i)){
				if(!estaEnFuncion)
					codigo.append("Label"+i+":"+"\r\n");
				else
					funciones.append("Label"+i+":"+"\r\n");
    		}
			
    		if(polaca.get(i).charAt(0) == '_'){ //para identificar variables
    			if(!variables.contains(PI.getPI().get(i).toString())){
    				variables.add(PI.getPI().get(i).toString());
    			}
    		}
    		
    		if(polaca.get(i).charAt(0) == '\''){ //para hacer variables de las cadenas
    			//if(!mensajes.contains(PI.getPI().get(i).toString())){TODO SACAR A LA MIERDA ESTO SI NO SIRVE
    				mensajes.add(PI.getPI().get(i).toString());
    			//}TODO SACAR A LA MIERDA ESTO SI NO SIRVE
    		}

    		if(polaca.get(i).toString().contains(".")){
	    		if(AnalizadorLexico.tablaSimbolos.get((polaca.get(i)).toString()).tipo.equals("single")&&(polaca.get(i).toString().charAt(0) != '_')){ //para hacer constantes de los float
	    			if(!flotantes.contains(polaca.get(i).toString())){
	    				flotantes.add(polaca.get(i).toString());
	    			}
	    		}
    		}
    		
    		pilaVar.push(polaca.get(i));   //SACO ELEMENTOS DE LA POLACA
    		
		
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
					pilaVar.pop();
					StringBuilder retorno = pilaVar.pop();
					Token t = null; 
						if(AnalizadorLexico.tablaSimbolos.get(retorno.toString()).tipo.equals("uslinteger")){
							StringBuilder stringOriginal = new StringBuilder(retorno.toString());
				    		if(AnalizadorLexico.tablaSimbolos.get(retorno.toString()).uso.equals("constante")){	//SACO _UL
				    			retorno = new StringBuilder(retorno.substring(0, retorno.length()-3));
				    		}
				    		funciones.append("MOV EAX, " +retorno + "\r\n" + "MOV @aux_fun, EAX" +"\r\n" +"RET" +"\r\n \r\n");
							t = new Token("@aux_fun",AnalizadorLexico.TOKEN_UL,"uslinteger"); //VER
							t.setUso(AnalizadorLexico.tablaSimbolos.get(stringOriginal.toString()).uso); 
						}
						else{
							if(AnalizadorLexico.tablaSimbolos.get(retorno.toString()).tipo.equals("single")){
								if(flotantes.contains(retorno.toString()))
									retorno= new StringBuilder("_"+retorno.toString().replace(".", "_"));
								funciones.append("FLD "+retorno+ "\r\n" + "FSTP @aux_fun"+"\r\n" +"RET" +"\r\n \r\n");
								t = new Token("@aux_fun",AnalizadorLexico.TOKEN_FLOAT,"single"); //VER
								if(flotantes.contains(retorno.toString().substring(1, retorno.length()).replace("_", ".")))
									t.setUso(AnalizadorLexico.tablaSimbolos.get(retorno.toString().substring(1, retorno.length()).replace("_", ".")).uso);
								else
									t.setUso(AnalizadorLexico.tablaSimbolos.get(retorno.toString()).uso);
								
							}
						}
					estaEnFuncion = false; //SETEA ENFUNCION A FALSE PARA QUE VUELVA A ESCRIBIR EN CODIGO
					//StringBuilder aux_fun= new StringBuilder("@aux_fun"); TODO SACAR A LA MIERDA ESTO SI NO SIRVE//crea la var?
					AnalizadorLexico.tablaSimbolos.put(t.lexema,t);  //agrega a la tabla de simbolos
					//pilaVar.push(aux_fun);	TODO SACAR A LA MIERDA ESTO SI NO SIRVE	
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
	ArrayList<StringBuilder> polaca=PI.getPI();
	//ASIGNACIONES	
	if(pilaVar.peek().toString().equals(":=")){ 
		pilaVar.pop(); //SACO EL :=
		StringBuilder aAsignar = pilaVar.pop();
		StringBuilder asignacion = pilaVar.pop();
		if((AnalizadorLexico.tablaSimbolos.get(aAsignar.toString()).tipo.equals("uslinteger"))&&(AnalizadorLexico.tablaSimbolos.get(asignacion.toString()).tipo.equals("uslinteger"))){
    		if(AnalizadorLexico.tablaSimbolos.get(asignacion.toString()).uso!=null){
				if(AnalizadorLexico.tablaSimbolos.get(asignacion.toString()).uso.equals("constante")){
	    			asignacion = new StringBuilder(asignacion.substring(0, asignacion.length()-3));
	    		}
				
		
    		}
    		escritura.append("MOV EAX, "+asignacion+"\r\n"+"MOV "+aAsignar+", EAX"+"\r\n" ); //ver si va lo de mov @aux0, EAX despues de esto
		}	
		else{
			if((AnalizadorLexico.tablaSimbolos.get(aAsignar.toString()).tipo.equals("single"))&&(AnalizadorLexico.tablaSimbolos.get(asignacion.toString()).tipo.equals("single"))){
				if(flotantes.contains(asignacion.toString()))
					if(AnalizadorLexico.tablaSimbolos.get(asignacion.toString()).uso.equals("constante")){
						if(asignacion.toString().contains("-"))
							asignacion = new StringBuilder("_neg"+asignacion.toString().replace(".", "_").substring(1,asignacion.toString().length()));	
						else
							asignacion = new StringBuilder("_"+asignacion.toString().replace(".", "_"));
					}
				escritura.append("FLD "+asignacion+"\r\n"+"FSTP "+ aAsignar+"\r\n");
			}
			else 
				escritura.append("JMP @LABEL_TIPOS_DISTINTOS"+"\r\n");
		}

	}//if(AnalizadorLexico.tablaSimbolos.get(segundoOperando.toString()).uso.equals("constante"))TODO SACAR A LA MIERDA ESTO SI NO SIRVE

	else{
	
		//LLAMADO DE FUNCIONES
		if(pilaVar.peek().toString().equals("CALL")){
			pilaVar.pop(); //SACO EL CALL
			StringBuilder fun_llamada = pilaVar.pop();
			aux_parametro = pilaVar.pop(); //asigno el valor del parametro
			if((AnalizadorLexico.tablaSimbolos.get(aux_parametro.toString()).tipo.equals("uslinteger")))
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
				//AMBOS INTEGER
				if((AnalizadorLexico.tablaSimbolos.get(primerComparado.toString()).tipo.equals("uslinteger")) && (AnalizadorLexico.tablaSimbolos.get(segundoComparado.toString()).tipo.equals("uslinteger"))){
		    		if(AnalizadorLexico.tablaSimbolos.get(primerComparado.toString()).uso.equals("constante")){
		    			primerComparado = new StringBuilder(primerComparado.substring(0, primerComparado.length()-3));
		    		}
		    		if(AnalizadorLexico.tablaSimbolos.get(segundoComparado.toString()).uso.equals("constante")){
		    			segundoComparado = new StringBuilder(segundoComparado.substring(0, segundoComparado.length()-3));
		    		}
					escritura.append("MOV EAX, "+primerComparado+"\r\n"+"MOV EBX, "+segundoComparado+"\r\n"+"CMP EBX,EAX"+"\r\n");
				}
				else{
					//AMBOS FLOAT 
					if((AnalizadorLexico.tablaSimbolos.get(primerComparado.toString()).tipo.equals("single")) && ((AnalizadorLexico.tablaSimbolos.get(segundoComparado.toString()).tipo.equals("single")))){
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
						if((AnalizadorLexico.tablaSimbolos.get(primerComparado.toString()).tipo.equals("uslinteger")) && ((AnalizadorLexico.tablaSimbolos.get(segundoComparado.toString()).tipo.equals("single"))))
							escritura.append("JMP @LABEL_TIPOS_DISTINTOS"+"\r\n");
						else{
							if((AnalizadorLexico.tablaSimbolos.get(primerComparado.toString()).tipo.equals("single")) && ((AnalizadorLexico.tablaSimbolos.get(segundoComparado.toString()).tipo.equals("uslinteger"))))
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
				if(Integer.parseInt(label.substring(5,label.length()))>=polaca.size()) //VERIFICO, SI NO CUMPLE LLEVA AL FINAL //ACA CREO ES 5 NO 6
					label =new StringBuilder("@LABEL_END"+"\r\n");
				
				if(signo.toString().equals("="))
						escritura.append("JNE "+label+"\r\n");
				else{
					if(signo.toString().equals("!="))
							escritura.append("JE "+label+"\r\n");
					else{
			    		if(!(primerComparado.toString().contains("_ul"))&&(!flotantes.contains(primerComparado.toString().substring(1, primerComparado.length()).replace("_", ".")))){
			    			if(!((AnalizadorLexico.tablaSimbolos.get(primerComparado.toString())!=null)&&(AnalizadorLexico.tablaSimbolos.get(primerComparado.toString()).uso.equals("variable"))))
			    				primerComparado = new StringBuilder(primerComparado.toString()+"_ul");
							
			    			if(AnalizadorLexico.tablaSimbolos.get(primerComparado.toString()).tipo.equals("uslinteger")){
								
								if(signo.toString().equals("<"))
									escritura.append("JGE "+label+"\r\n");
								
								if(signo.toString().equals(">"))
									escritura.append("JLE "+label+"\r\n");
							}
			    		}
			    		else{ //FLOAT
							if(flotantes.contains(primerComparado.toString().substring(1, primerComparado.length()).replace("_", "."))){
								primerComparado = new StringBuilder(primerComparado.toString().substring(1, primerComparado.length()).replace("_", "."));
								
							}
							if(AnalizadorLexico.tablaSimbolos.get(primerComparado.toString()).tipo.equals("single")){
							
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
					if(Integer.parseInt(label.substring(5,label.length()))>=polaca.size()) //VERIFICO SI ESTO LLEVA AL FINAL
						label =new StringBuilder("@LABEL_END"+"\r\n");
					escritura.append("JMP "+label+"\r\n");
				}	
				else{
					//PARA PRINTS
					if(pilaVar.peek().toString().equals("print")){
					pilaVar.pop(); //SACO LA PALABRA PRINT
						if(pilaVar.peek().toString().contains("'")){
							//StringBuilder mensajito = pilaVar.pop();TODO SACAR A LA MIERDA ESTO SI NO SIRVE
							pilaVar.pop();
							escritura.append("invoke MessageBox, NULL, addr "+"msj"+contmsj+", addr "+"msj"+contmsj+", MB_OK"+"\r\n");
							contmsj++;
						}
						else{
							StringBuilder variable = pilaVar.pop();
							if(AnalizadorLexico.tablaSimbolos.get(variable.toString()).tipo.equals("single"))
								escritura.append("invoke MessageBox,NULL,addr print_single,addr "+"print_single,MB_OK"+"\r\n");
							if(AnalizadorLexico.tablaSimbolos.get(variable.toString()).tipo.equals("uslinteger"))
								escritura.append("invoke printf, cfm$(\"%d\\n\"), "+variable+"\r\n");
						}
					}
					else{
						//PARA OPERACIONES ARITMETICAS
					    if(pilaVar.peek().toString().equals("+") || pilaVar.peek().toString().equals("-")|| pilaVar.peek().toString().equals("*") || pilaVar.peek().toString().equals("/")){
							StringBuilder operador = pilaVar.pop(); //PARA SACAR EL OPERANDO
							StringBuilder primerOperando = pilaVar.pop();
							StringBuilder segundoOperando = pilaVar.pop();
							
							if((AnalizadorLexico.tablaSimbolos.get(primerOperando.toString()).tipo.equals("uslinteger")) && ((AnalizadorLexico.tablaSimbolos.get(segundoOperando.toString()).tipo.equals("uslinteger")))){
					    		if(AnalizadorLexico.tablaSimbolos.get(primerOperando.toString()).uso.equals("constante")){
					    			primerOperando = new StringBuilder(primerOperando.substring(0, primerOperando.length()-3));
					    		}
					    		if(AnalizadorLexico.tablaSimbolos.get(segundoOperando.toString()).uso.equals("constante")){
					    			segundoOperando = new StringBuilder(segundoOperando.substring(0, segundoOperando.length()-3));
					    		}
								generarCodigoInteger(operador ,primerOperando,segundoOperando,escritura);
							}
							else{
								if((AnalizadorLexico.tablaSimbolos.get(primerOperando.toString()).tipo.equals("single")) && ((AnalizadorLexico.tablaSimbolos.get(segundoOperando.toString()).tipo.equals("single")))){
									if(flotantes.contains(primerOperando.toString())){
										if(AnalizadorLexico.tablaSimbolos.get(primerOperando.toString()).uso.equals("constante")){
											if(primerOperando.toString().contains("-"))
												primerOperando = new StringBuilder("_neg"+primerOperando.toString().replace(".", "_").substring(1,primerOperando.toString().length()));	
											else
												primerOperando=new StringBuilder("_"+primerOperando.toString().replace(".", "_"));
										}
										else
											primerOperando=new StringBuilder(primerOperando.toString());
									}
									if(flotantes.contains(segundoOperando.toString())){
										if(AnalizadorLexico.tablaSimbolos.get(segundoOperando.toString()).uso.equals("constante")){
											if(segundoOperando.toString().contains("-"))
												segundoOperando = new StringBuilder("_neg"+segundoOperando.toString().replace(".", "_").substring(1,segundoOperando.toString().length()));	
											else
												segundoOperando=new StringBuilder("_"+segundoOperando.toString().replace(".", "_"));
										}
										else
											segundoOperando=new StringBuilder(segundoOperando.toString());
									}
									//l.info(" primer operando: '"+primerOperando+"' segundo operando: '"+segundoOperando+"' ");
									generarCodigoSingle(operador,primerOperando,segundoOperando,escritura);
							}
								else{
									if((AnalizadorLexico.tablaSimbolos.get(primerOperando.toString()).tipo.equals("uslinteger")) && ((AnalizadorLexico.tablaSimbolos.get(segundoOperando.toString()).tipo.equals("single")))){
										escritura.append("JMP @LABEL_TIPOS_DISTINTOS"+"\r\n");
										StringBuilder aux= new StringBuilder("@auxDistintosTipos"+contador);
										pilaVar.push(aux);  
										Token t = new Token("@auxDistintosTipos"+contador,AnalizadorLexico.TOKEN_UL,"uslinteger");
										t.setUso("variable");
										AnalizadorLexico.tablaSimbolos.put(t.lexema,t);
								}
									else{
										if((AnalizadorLexico.tablaSimbolos.get(primerOperando.toString()).tipo.equals("single")) && ((AnalizadorLexico.tablaSimbolos.get(segundoOperando.toString()).tipo.equals("uslinteger"))))
											escritura.append("JMP @LABEL_TIPOS_DISTINTOS"+"\r\n");
											StringBuilder aux= new StringBuilder("@auxDistintosTipos"+contador);
											pilaVar.push(aux);  
											Token t = new Token("@auxDistintosTipos"+contador,AnalizadorLexico.TOKEN_UL,"single");
											t.setUso("variable");
											AnalizadorLexico.tablaSimbolos.put(t.lexema,t);
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
    	case"+" : escritura.append("MOV EAX, "+primerOperando+"\r\n"+"ADD EAX, "+segundoOperando+"\r\n"+"JC @LABEL_OVF"+"\r\n"+"MOV "+"@aux"+contador+", EAX"+"\r\n");
    				aux= new StringBuilder("@aux"+contador);
    				pilaVar.push(aux);  				
    				
    	break;
    	case"-" : escritura.append("MOV EAX, "+segundoOperando+"\r\n"+"SUB EAX, "+primerOperando+"\r\n"+"JS @LABEL_RESUL_NEG" +"\r\n"+"MOV "+"@aux"+contador+", EAX"+"\r\n");
  				  aux= new StringBuilder("@aux"+contador);
  				  pilaVar.push(aux);
		 		 
    	break;
    	case"*": escritura.append("MOV EAX, "+primerOperando+"\r\n"+"IMUL EAX, "+segundoOperando+"\r\n"+"JC @LABEL_OVF"+"\r\n"+ "MOV "+"@aux"+contador+", EAX"+"\r\n");
 				 aux= new StringBuilder("@aux"+contador);
 				 pilaVar.push(aux);
		break;		

    	case"/" : escritura.append("MOV EDX, 0"+"\r\n"+"MOV EAX, "+segundoOperando+"\r\n"+"MOV EBX,"+primerOperando+"\r\n"+"CMP EBX, 0"+"\r\n"+"JZ @LABEL_ZERO"+"\r\n"+"DIV EBX"+"\r\n"+ "MOV "+"@aux"+contador+", EAX"+"\r\n");
 				  aux= new StringBuilder("@aux"+contador);
 				  pilaVar.push(aux);
	
		 break;
    	}
		Token t = new Token("@aux"+contador,AnalizadorLexico.TOKEN_UL,"uslinteger");
		t.setUso("variable");
		AnalizadorLexico.tablaSimbolos.put(t.lexema,t);
		contador++;
    }
    	
public void generarCodigoSingle(StringBuilder operador,StringBuilder primerOperando, StringBuilder segundoOperando, StringBuilder escritura){ //para los dos single
			//l.info(" primer operando: '"+primerOperando+"' segundo operando: '"+segundoOperando+"' ");
			String op = operador.toString();
			StringBuilder aux = null;
			switch(op){
			case "+":escritura.append("FLD "+primerOperando+"\r\n"+"FLD "+segundoOperando+"\r\n"+ "FADD"+"\r\n"+"FSTP "+"@aux"+contador+"\r\n");
					 escritura.append("FLD cte_max_rango"+"\r\n"+"FLD @aux"+contador+"\r\n"+"FCOM"+"\r\n"+"FSTSW AX"+"\r\n"+"SAHF"+"\r\n"+"JA @LABEL_OVF"+"\r\n");
					 escritura.append("FLD cte_min_rango"+"\r\n"+"FLD @aux"+contador+"\r\n"+"FCOM"+"\r\n"+"FSTSW AX"+"\r\n"+"SAHF"+"\r\n"+"JA @LABEL_OVF"+"\r\n");
					 aux= new StringBuilder("@aux"+contador);
					 pilaVar.push(aux);
					
			break;
			case "-":escritura.append("FLD "+segundoOperando+"\r\n"+"FLD "+primerOperando+"\r\n"+ "FSUB"+"\r\n"+"FSTP "+"@aux"+contador+"\r\n");
					 aux= new StringBuilder("@aux"+contador);
					 pilaVar.push(aux);
					  
			break;
			case "*":escritura.append("FLD "+primerOperando+"\r\n"+"FLD "+segundoOperando+"\r\n"+ "FMUL"+"\r\n"+"FSTP "+"@aux"+contador+"\r\n");
					 escritura.append("FLD cte_max_rango"+"\r\n"+"FLD @aux"+contador+"\r\n"+"FCOM"+"\r\n"+"FSTSW AX"+"\r\n"+"SAHF"+"\r\n"+"JA @LABEL_OVF"+"\r\n");
					 escritura.append("FLD cte_min_rango"+"\r\n"+"FLD @aux"+contador+"\r\n"+"FCOM"+"\r\n"+"FSTSW AX"+"\r\n"+"SAHF"+"\r\n"+"JA @LABEL_OVF"+"\r\n");
					 aux= new StringBuilder("@aux"+contador);
					 pilaVar.push(aux);		
					 
			break;		 

			case "/":escritura.append("FLD "+segundoOperando+"\r\n"+"FLD "+primerOperando+"\r\n"+"FCOM zero"+"\r\n"+"FSTSW AX"+"\r\n"+"SAHF"+"\r\n"+"JE @LABEL_ZERO"+"\r\n"+ "FDIV"+"\r\n"+"FSTP "+"@aux"+contador+"\r\n");
					 aux= new StringBuilder("@aux"+contador);
					 pilaVar.push(aux);
					
			break;
			}
			
			Token t = new Token("@aux"+contador,AnalizadorLexico.TOKEN_FLOAT,"single");
			t.setUso("variable");
			AnalizadorLexico.tablaSimbolos.put(t.lexema,t);
			//flotantes.add(t.lexema); // TODO ESTO ESTABA AGREGANDO VARIABLES A LA DECLARACION DE .CONST TODO SACAR ESTE COMENTARIO
			contador++;
}
   

 					
					

	    						


    public void generarDeclaracion(){

    	declaracion.append("mensaje_overflow db \"La operacion aritmetica genero overflow\", 0 "+"\r\n");
    	declaracion.append("mensaje_zero db \"Division por cero\", 0 "+"\r\n");
    	declaracion.append("mensaje_resultadoNeg db \"El resultado de la resta es negativo\", 0 "+"\r\n");
    	declaracion.append("mensaje_tipos db \"Se esta operando con tipos diferentes\", 0 "+"\r\n");
    	declaracion.append("print_single  db \"Solo se pueden printear variables de tipo uslinteger\", 0 "+"\r\n");
    	declaracion.append("zero  dd 0.0"+"\r\n");
        declaracion.append("cte_max_rango  dd 3.40282347E+38"+"\r\n");
        declaracion.append("cte_min_rango  dd 1.17549435E-38"+"\r\n");
        
    	
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
        	String declarado = flotantes.get(uwu).toString().replace(".", "_");
        	l.info(flotantes.toString());// TODO SACAR ESTO... AUX0 ESTA EN FLOTANTES... KE
        	if(flotantes.get(uwu).toString().contains("-"))
        		declaracion.append("_neg"+declarado.substring(1,declarado.length())+" dd "+ flotantes.get(uwu)+"\n");
        	else
        		declaracion.append("_"+declarado+" dd "+ flotantes.get(uwu)+"\n");
        }
    	declaracion.append(".code"+"\r\n"+"start: ");
    }
   
 
    public void generarEncabezado(){
    	inicio.append(".386"+"\r\n"+".model flat, stdcall"+"\r\n"+"option casemap :none "+"\r\n"+"include \\masm32\\include\\windows.inc"+"\r\n"+"include \\masm32\\include\\kernel32.inc"
    					+"\r\n"+"include \\masm32\\include\\masm32.inc"+"\r\n"+"includelib \\masm32\\lib\\kernel32.lib"+"\r\n"+"includelib \\masm32\\lib\\masm32.lib"+"\r\n"
    			+"\r\n"+"include \\masm32\\include\\masm32rt.inc"+"\r\n"+"dll_dllcrt0 PROTO C"+"\r\n"+"printf PROTO C: VARARG"+"\r\n"+".data"+"\r\n");   
    }
    
    public void generarMensajesDeControl(){
    	fin.append("@LABEL_OVF:"+ "\r\n"+ "invoke MessageBox,NULL,addr mensaje_overflow,addr "+"mensaje_overflow,MB_OK"+"\r\n"+"JMP @LABEL_END"+"\r\n");
    	fin.append("@LABEL_ZERO:"+ "\r\n"+ "invoke MessageBox,NULL,addr mensaje_zero,addr "+"mensaje_zero,MB_OK"+"\r\n"+"JMP @LABEL_END"+"\r\n");
    	fin.append("@LABEL_RESUL_NEG:"+ "\r\n"+ "invoke MessageBox,NULL,addr mensaje_resultadoNeg,addr "+"mensaje_resultadoNeg,MB_OK"+"\r\n"+"JMP @LABEL_END"+"\r\n");
    	fin.append("@LABEL_TIPOS_DISTINTOS:"+"\r\n"+ "invoke MessageBox,NULL,addr mensaje_tipos,addr "+"mensaje_tipos,MB_OK"+"\r\n"+"JMP @LABEL_END"+"\r\n");
    }
    
    public void generarFin(){
    	fin.append("@LABEL_END:"+"\r\n"+"invoke ExitProcess, 0"+"\r\n"+"end start");
    }
    
//Cosas que me dijo anto: recorrer toda la polaca para crear la declaracion de variable, osea hacerlo al final TODO SACAR A LA MIERDA ESTO SI NO SIRVE
    //No hay anidamiento de funciones TODO SACAR A LA MIERDA ESTO SI NO SIRVE
    //Las funciones escribirlas al final del programa TODO SACAR A LA MIERDA ESTO SI NO SIRVE
    //poner en la declaracion de variables una constante con el maximo para la comparacion de overflow de single TODO SACAR A LA MIERDA ESTO SI NO SIRVE
    //despues de la declaracion de variables concatenar el .code y el start: TODO SACAR A LA MIERDA ESTO SI NO SIRVE
     
    /* TODO SACAR A LA MIERDA ESTO SI NO SIRVE
    private StringBuilder apilar(StringBuilder s){
    	return pilaVar.push(s);
    }
    */
    public void generameAssemblydotexe(String fileName) throws IOException{
    	generarEncabezado();
    	guardarSaltos();
    	leer();
    	generarDeclaracion();
    	inicio.append(declaracion);
    	inicio.append(codigo);
    	inicio.append("JMP @LABEL_END");
    	inicio.append(funciones);    	
    	generarMensajesDeControl();
    	generarFin();
    	inicio.append(fin);
    	System.out.println("Assembler generado:");
    	System.out.println(inicio);
    	FileManager.write(inicio.toString(), new File(fileName+".asm"));
    	

    }
    

}