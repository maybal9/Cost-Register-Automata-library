package core;
import com.sun.org.apache.xpath.internal.operations.Bool;
import helpers.Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Automaton<T> {

    /*members*/
    protected Graph G;

    protected String Sigma;

    protected int numOfStates;

    protected Boolean[] States;

    protected int q0;

    protected int[] acceptingStates;

    /*constructor*/
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

    /*methods*/
    protected Integer[] toObject(int[] intArray) {

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
    public abstract boolean belongs(String w);

    public String getSigma() { return this.Sigma; }

    public Boolean[] getStates() { return this.States;  }

    public int[] getAcceptingStates(){  return this.acceptingStates; }

    public Integer[] GetAcceptingStates(){
        return Arrays.stream( this.acceptingStates ).boxed().toArray( Integer[]::new );
    }

    public boolean isAcceptingState(int state){ return this.States[state];}

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
}
