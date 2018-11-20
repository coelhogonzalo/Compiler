package GeneracionCodigoIntermedio; //prueba

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import AnalizadorLexico.Token;

public class Polaca_Inversa {
    private ArrayList<StringBuilder> PI = null;
    private Stack<Integer> pila = null;
    private Stack<Integer> saltoIncond = null;
    private Stack<Integer> saltoFuncs = null;
    private String nombreFunActual;

    public Polaca_Inversa() {
        PI = new ArrayList<>();
        pila = new Stack<>();
        saltoIncond = new Stack<>();

        nombreFunActual = null;
    }

    public void put(String l) {
        PI.add(new StringBuilder(l));
    }

    public void putAt(String l, int index) {
        PI.add(index, new StringBuilder(l));
    }

    public void setSaltoIncond(){
        saltoIncond.add(PI.size());
    }
    
    public void saltoIncond(){
    	PI.add(new StringBuilder("Label" + saltoIncond.pop()));
    	PI.add(new StringBuilder("BT"));
    }
    
    public void bifurcacion() {
        pila.add(PI.size());
        //System.out.println("La bifurcacion apilo "+PI.size());
        PI.add(new StringBuilder("WHERE"));
        PI.add(new StringBuilder("B"));
    }
    
    public void bifurcacionElse() {
        pila.add(PI.size());
        PI.add(new StringBuilder("WHERE"));
        PI.add(new StringBuilder("BT"));
    }
    
    public void inicioFuncion(String nombreFun) {
    	nombreFunActual = nombreFun;
        PI.add(new StringBuilder(nombreFun));
        PI.add(new StringBuilder("inicio_funcion"));
    }

    public void jumpToFun(String fun){
        PI.add(new StringBuilder(fun));
        PI.add(new StringBuilder("CALL"));
    }

    public ArrayList<StringBuilder> getPI(){
        ArrayList<StringBuilder> PIOut = new ArrayList<>(PI);
        return PIOut;
    }

    public void desapilar() {
        int pop = pila.pop();
        StringBuilder v = PI.get(pop);
        //System.out.println("Desapile esto "+v.toString());
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

    public void paramFun(String param){
        PI.add(PI.size()-2, new StringBuilder(param));
    }

}


