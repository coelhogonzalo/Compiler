package GeneracionCodigoIntermedio;

import java.util.ArrayList;
import java.util.Stack;

import AnalizadorLexico.Token;

public class Polaca_Inversa {
    private ArrayList<Token> PI = null;
    private Stack<Integer> pila = null;

    public Polaca_Inversa(){
        PI = new ArrayList<>();
        pila = new Stack<>();
    }

    public void put(Token t){
    	PI.add(t);
    }
    
    public void putAt(Token t, int index){
    	PI.add(index, t);
    }
    
    public void apiloBranch(){
    	pila.add(PI.size());
    	PI.add(new Token("Branch", -1, "Branch"));
    }
    
    public void actualizoBranch(int dondeSalta){
    	PI.get(pila.pop()).nro = dondeSalta;
    }
}


