package core;
import helpers.Graph;
import helpers.Pair;
import helpers.State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DFA<K> {

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
    public DFA(String sigma, State<K>[] states, State<K> q0,
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

    //getters
    public String getSigma() {
        return Sigma;
    }

    public int getNumOfStates() {
        return numOfStates;
    }

    public State<K>[] getStates() {
        return States;
    }

    public State<K> getQ0() {
        return q0;
    }

    public int getInitStateIndex() {
        return initStateIndex;
    }

    public boolean[] getAcceptingStates() {
        return AcceptingStates;
    }

    public int[][] getDelta() {
        return Delta;
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

    public DFA<Pair> createCrossAutomaton(DFA<K> other){
        State<Pair>[] newStates = createCrossStates(other);
        State<Pair> newq0 = createCrossInitState(other);
        boolean[] newAcceptingStates = createCrossAcceptingState(other);
        int[][] newDelta = createCrossDelta(other);
        return new DFA<Pair>(this.Sigma,newStates,newq0,newAcceptingStates,newDelta);
    }

    public DFA<Pair> createAllAcceptingCrossAutomaton(DFA<K> other){
        State<Pair>[] newStates = createCrossStates(other);
        State<Pair> newq0 = createCrossInitState(other);
        boolean[] newAcceptingStates = createAllAcceptingCrossAcceptingState(other);
        int[][] newDelta = createCrossDelta(other);
        return new DFA<Pair>(this.Sigma,newStates,newq0,newAcceptingStates,newDelta);
    }

    private State<Pair>[] createCrossStates(DFA<K> other){
        State<Pair>[] ans = new State[this.States.length*other.States.length];
        for(int i=0; i<this.States.length; i++){
            for(int j=0; j<other.States.length; j++){
                Pair p = new Pair(this.States[i].getState(),other.States[j].getState());
                ans[i*other.States.length + j] = new State(p);
            }
        }
        return ans;
    }

    private State<Pair> createCrossInitState(DFA<K> other){
        Pair p = new Pair(this.q0.getState(),other.q0.getState());
        State<Pair> ans = new State(p);
        return ans;
    }

    private boolean[] createCrossAcceptingState(DFA<K> other){
        boolean[] ans = new boolean[this.States.length*other.States.length];
        for(int i=0; i<this.AcceptingStates.length; i++){
            for(int j=0; j<other.AcceptingStates.length; j++){
                ans[i*this.States.length + j] = this.AcceptingStates[i] && other.AcceptingStates[j];
            }
        }
        return ans;
    }

    private boolean[] createAllAcceptingCrossAcceptingState(DFA<K> other){
        boolean[] ans = new boolean[this.States.length*other.States.length];
        for(int i=0; i<ans.length; i++){
            ans[i] = true;
        }
        return ans;
    }

    private int[][] createCrossDelta(DFA<K> other){
        int[][] ans = new int[this.States.length*other.States.length][this.Sigma.length()];
        for(int sigmaIdx=0; sigmaIdx<this.Sigma.length(); sigmaIdx++) {
            for (int i = 0; i < this.Delta.length; i++) {
                for (int j = 0; j < other.Delta.length; j++) {
                    int delta1 = this.Delta[i][sigmaIdx];
                    int delta2 = other.Delta[j][sigmaIdx];
                    ans[i * other.States.length + j][sigmaIdx] = delta1 * other.States.length + delta2;
                }
            }
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
