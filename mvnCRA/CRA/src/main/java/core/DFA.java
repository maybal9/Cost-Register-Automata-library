package core;


public class DFA extends Automaton{

    private int[][] Delta;

    public DFA(String sigma, int numOfStates, int[][] delta, int[] acc){
        super(sigma,numOfStates,acc);
        Delta = new int[numOfStates][super.Sigma.length()];
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

    private int getNextState(int thisState, char c){
        return Delta[thisState][super.Sigma.indexOf(c)];
    }

    public Boolean evaluate(String w){
        int currentState = q0;
        int i=0;
        while(i<w.length()){
            currentState = getNextState(currentState, w.charAt(i));
            ++i;
        }
        return super.States[currentState];
    }

    public int[][] getDelta(){
        return this.Delta;
    }

}

