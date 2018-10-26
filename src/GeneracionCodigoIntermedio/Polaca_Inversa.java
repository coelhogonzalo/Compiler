package GeneracionCodigoIntermedio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import AnalizadorLexico.Token;

public class Polaca_Inversa {
    private ArrayList<StringBuilder> PI = null;
    private Stack<Integer> pila = null;
    private int saltoIncond;
    private HashMap<StringBuilder, tamFuncion> funcs;
    private StringBuilder nombreFunActual;

    public Polaca_Inversa() {
        PI = new ArrayList<>();
        pila = new Stack<>();
        saltoIncond = 0;
        funcs = new HashMap<>();
        nombreFunActual = new StringBuilder();
    }

    public void put(String l) {
        PI.add(new StringBuilder(l));
    }

    public void putAt(String l, int index) {
        PI.add(index, new StringBuilder(l));
    }

    public void setSaltoIncond(){
        saltoIncond = PI.size() - 1;
    }
    
    public void saltoIncond(){
    	PI.add(new StringBuilder("Label" + saltoIncond));
    }
    
    public void bifurcacion() {
        pila.add(PI.size());
        PI.add(new StringBuilder("WHERE"));
        PI.add(new StringBuilder("B"));
    }
    
    public void inicioFuncion(String nombreFun) {
    	tamFuncion tf = new tamFuncion();
    	tf.setInicio(PI.size());
    	nombreFunActual = new StringBuilder(nombreFun);
    	funcs.put(nombreFunActual, tf);
    	PI.add(new StringBuilder("inicio_funcion"));
    }
    
    public void finFuncion() {
    	tamFuncion tf = funcs.get(nombreFunActual);
    	tf.setFin(PI.size());
    	PI.add(new StringBuilder("return"));

    }
    
    public void jumpToFun(String fun){
    	tamFuncion tf = funcs.get(fun);
    	PI.add(new StringBuilder("Label" + tf.getInicio())); //esto en assembler deberia de ser un CALL para usar un RET y ya tener la pos
    }

   // public void apilar() {
   //     pila.add(PI.size());
   //     PI.add(new StringBuilder("WHERE"));
   // }

    public void desapilar() {
        int pop = pila.pop();
        StringBuilder v = PI.get(pop);
        v.setLength(0);
        v.append("Label" + PI.size());
        //PI.add(new StringBuilder("Label" + pop));
    }
    public void desapilarElse() {
        int pop = pila.pop();
        StringBuilder v = PI.get(pop);
        v.setLength(0);
        v.append("Label" + (PI.size() + 2));
        //PI.add(new StringBuilder("Label" + pop));
    }
    public void printContent(){//PARA DEBUG
    	System.out.println(this.PI);
    }
    /*public void apiloBranch(){
    	pila.add(PI.size());
    	PI.add(new Token("Branch", -1, "Branch"));
    }
    
    public void actualizoBranch(int dondeSalta){
    	PI.get(pila.pop()).nro = dondeSalta;
    }*/
    
    private class tamFuncion{
    	int inicio, fin;
    	public tamFuncion(){
    		inicio = 0;
    		fin = 0;
    	}
    	public void setInicio(int i){
    		inicio = i;
    	}
    	public void setFin(int f){
    		fin = f;
    	}
    	public int getInicio(){
    		return inicio;
    	}
    	public int getFin(){
    		return fin;
    	}
    }
}


