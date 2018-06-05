package core;


import java.util.HashSet;
import java.util.Set;

public class NFA extends Automaton{

    private Set<Integer>[][] Delta;
    private String epsilon = "e";

    //sigma can contain epsilon, and delta too
    public NFA(String sigma, int numOfStates, Set<Integer>[][] delta, int[] acc){
        super(sigma,numOfStates,acc);
        int powerSet = (int) Math.pow(2,numOfStates);
        Delta = new Set[numOfStates][super.Sigma.length()];
        for(int a=0; a<delta.length; a++){
            for(int b=0; b< delta[0].length; b++){
                Delta[a][b] = delta[a][b];
            }
        }
    }

    /*checks if the given word is in sigma star, meaning if it is composed of symbols of sigma
     *next step: upgrade using reduce with boolean flag*/
    public boolean isValidWord(String w){
        boolean ans = true;
        for(int i=0; i<w.length() && ans; i++){
            ans = ans & (super.Sigma.indexOf(w.charAt(i)) >= 0);
        }
        return ans;
    }

    private Set<Integer> getNextStates(int thisState, char c){
        return Delta[thisState][super.Sigma.indexOf(c)];
    }

    public Boolean evaluate(String w){
        Boolean ans = false;
        Set<Integer> currentStateSet = new HashSet<>();
        currentStateSet.add(q0);
        Set<Integer> nextStateSet = new HashSet<Integer>();
        int i=0;
        while(i<w.length()){
            nextStateSet.clear();
            for(Integer q: currentStateSet) {
                nextStateSet.addAll(getNextStates(q, w.charAt(i)));
            } ++i;
            currentStateSet.clear();
            currentStateSet.addAll(nextStateSet);

        }
        for(Integer q: currentStateSet){
            if(super.States[q]){
                ans = true;
                break;
            }
        }
        return ans;
    }

    public Set<Integer>[][] getDelta(){
        return this.Delta;
    }

}

