package core;
import com.sun.org.apache.xpath.internal.operations.Bool;
import helpers.Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Automaton {
    protected Graph G;

    //Sigma - the Alephhbet represented as a String;
    protected String Sigma;

    protected int numOfStates;

    protected Boolean[] States;

    protected int q0;

    protected int[] acceptingStates;

    public Automaton(String Sigma,int numOfStates, int[] acc){
        this.Sigma = Sigma;
        this.numOfStates = numOfStates;
        this.G = new Graph(numOfStates);
        this.q0=0;

        //init States
        this.States = new Boolean[numOfStates];
        for(int i=0; i<numOfStates; i++) this.States[i] = false;
        for (int AcceptingState : acc) this.States[AcceptingState] = true;

        //init acceptingStates
        this.acceptingStates = new int[acc.length];
        for(int i=0;i<acc.length;i++){
            this.acceptingStates[i] = acc[i];
        }
    }

    private Integer[] toObject(int[] intArray) {

        Integer[] result = new Integer[intArray.length];
        for (int i = 0; i < intArray.length; i++) {
            result[i] = Integer.valueOf(intArray[i]);
        }
        return result;
    }

    public boolean Emptiness(){
        Integer[] ans = toObject(this.acceptingStates);
        ArrayList<Integer> lst = (ArrayList<Integer>) Arrays.asList(ans);
        return this.G.BFS(q0,lst);
    }

    public abstract Object evaluate(String w);

    public String getSigma() {
        return this.Sigma;
    }
    public Boolean[] getStates() {
        return this.States;
    }

    public int[] getAcceptingStates(){
        return this.acceptingStates;
    }


}
