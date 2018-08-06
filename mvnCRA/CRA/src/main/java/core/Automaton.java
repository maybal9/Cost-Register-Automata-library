package core;
import helpers.Graph;
import helpers.State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Automaton<K> {

    /*members*/
    protected String Sigma;
    protected int numOfStates;
    protected State<K>[] States;
    protected State<K> q0;
    protected int initStateIndex;
    protected boolean[] AcceptingStates;
    protected int[][] Delta;
    protected Graph G;

    /*constructor*/
    public Automaton(String sigma, State<K>[] states, State<K> q0,
                     boolean[] acceptingStates, int[][] delta) {
        this.Sigma = sigma;
        this.numOfStates = states.length;
        this.States = new State[this.numOfStates];
        for(int i=0; i< states.length; i++) {
            this.States[i] = new State<K>(states[i]);
        }
        this.q0 = new State<K>(q0);
        this.initStateIndex = 0;
        this.AcceptingStates = new boolean[acceptingStates.length];
        for(int i=0; i<this.AcceptingStates.length; i++) this.AcceptingStates[i] = acceptingStates[i];

        this.Delta = new int[this.States.length][this.Sigma.length()];
        for(int a=0; a<this.States.length; a++){
            for(int b=0; b<this.Sigma.length(); b++){
                this.Delta[a][b] = delta[a][b];
            }
        }

        this.G = null;
    }

    /*methods*/
    public boolean isAcceptingState(int index){
        return this.AcceptingStates[index];
    }

    public boolean belongs(String w){
        int currentStateIndex = initStateIndex;
        int i=0;
        while(i<w.length()){
            currentStateIndex = this.Delta[currentStateIndex][this.Sigma.indexOf(w.charAt(i))];
            ++i;
        }
        return isAcceptingState(currentStateIndex);
    }

    //checks if the given word is in sigma star, meaning if it is composed of symbols of sigma
    public boolean isValidWord(String w){
        boolean ans = true;
        for(int i=0; i<w.length() && ans; i++){
            ans = ans & (this.Sigma.indexOf(w.charAt(i)) >= 0);
        }
        return ans;
    }

    //'e' is the special char used for epsilon
    protected String createConcatSigma(String aSigma, String bSigma){
        StringBuilder ans = new StringBuilder();
        ans.append(aSigma);
        for(char c: bSigma.toCharArray()){
            if(aSigma.indexOf(c)== -1){
                ans.append(c);
            }
        }
        if(aSigma.indexOf('e')== -1 && bSigma.indexOf('e')== -1){
            ans.append('e');
        }
        return ans.toString();
    }

    protected Boolean[] createConcatStates(Boolean[] aStates, Boolean[] bStates){
        int numOfNewStates = aStates.length + bStates.length;
        Boolean[] result = Arrays.copyOf(aStates, numOfNewStates);
        System.arraycopy(bStates, 0, result, aStates.length, bStates.length);
        return result;
    }

    protected int[] createAccStates(Integer[] bAcc ,int promote){
        List<Integer> l = Arrays.asList(bAcc);
        return l.stream().mapToInt(i->i+promote).toArray() ;
    }

    //unused
    protected Integer[] toObject(int[] intArray) {

        Integer[] result = new Integer[intArray.length];
        for (int i = 0; i < intArray.length; i++) {
            result[i] = Integer.valueOf(intArray[i]);
        }
        return result;
    }

    public boolean Emptiness(){
        ArrayList<Integer> list = new ArrayList<>();
        for(int i=0; i< this.AcceptingStates.length; i++){
            if(this.AcceptingStates[i]){
                list.add(i);
            }
        }
        return this.G.BFS(initStateIndex,list);
    }

}
