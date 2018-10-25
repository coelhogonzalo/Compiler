package GeneracionCodigoIntermedio;

import java.util.ArrayList;
import java.util.Stack;

import AnalizadorLexico.Token;

public class Polaca_Inversa {
    private ArrayList<StringBuilder> PI = null;
    private Stack<Integer> pila = null;

    public Polaca_Inversa() {
        PI = new ArrayList<>();
        pila = new Stack<>();
    }

    public void put(String l) {
        PI.add(new StringBuilder(l));
    }

    public void putAt(String l, int index) {
        PI.add(index, new StringBuilder(l));
    }

    public void salto(){
        pila.add(PI.size());
        PI.add(new StringBuilder("WHERE"));
        PI.add(new StringBuilder("BT"));
    }

    public void bifurcacion() {
        pila.add(PI.size());
        PI.add(new StringBuilder("WHERE"));
        PI.add(new StringBuilder("B"));
    }

    public void apilar() {
        pila.add(PI.size());
        PI.add(new StringBuilder("WHERE"));
    }

    public void desapilar() {
        int pop = pila.pop();
        StringBuilder v = PI.get(pop);
        v.setLength(0);
        v.append(PI.size());
        PI.add(new StringBuilder("Label" + pop));
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
}


