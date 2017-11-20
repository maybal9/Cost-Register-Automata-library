
import java.util.HashSet;

public class Automaton {
    private String Sigma;
    private int[] Q;
    private int q0;
    private int[][] Delta;
    private HashSet<Integer> A;

    public Automaton(String sigma, int numOfStates, int[][] delta, int[] acc){
        Sigma = sigma;
        Q = new int[numOfStates];
        for(int i=0; i<numOfStates; i++){
            Q[i] = i;
        }
        q0 = 0;
        A = new HashSet<Integer>();
        for(int i=0; i<acc.length ; i++){
            A.add(acc[i]);
        }
        Delta = new int[numOfStates][Sigma.length()];
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
            ans = ans & (Sigma.indexOf(w.charAt(i)) >= 0);
        }
        return ans;
    }

    private int getNextState(int thisState, char c){
        return Delta[thisState][Sigma.indexOf(c)];
    }

    public boolean Evaluate(String w){
        int currentState = q0;
        int i=0;
        while(i<w.length()){
            currentState = getNextState(currentState, w.charAt(i));
            ++i;
        }
        return A.contains(currentState);
    }

}
