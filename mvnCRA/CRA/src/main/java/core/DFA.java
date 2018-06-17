package core;
import helpers.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DFA extends Automaton{

    /** members **/
    private int[][] Delta;

    /** constructors **/
    public DFA(String sigma, int numOfStates, int[][] delta, int[] acc){
        super(sigma,numOfStates,acc);
        Delta = new int[numOfStates][super.Sigma.length()];
        for(int a=0; a<delta.length; a++){
            System.arraycopy(delta[a], 0, Delta[a], 0, delta[0].length);
        }
    }

    //constructor matching Automaton
    public DFA(String sigma, int numOfStates, int[] acc){
        super(sigma,numOfStates,acc);
    }

    /** methods **/
    //getter for Delta
    private int[][] getDelta(){return this.Delta;}

    //evaluate function
    public boolean belongs(String w){
        int currentState = q0;
        int i=0;
        while(i<w.length()){
            currentState = this.Delta[currentState][super.Sigma.indexOf(w.charAt(i))];
            ++i;
        }
        return super.States[currentState];
    }

    //emptiness check of the language of this DFA
    public boolean Emptiness(){
        Integer[] ans = toObject(this.acceptingStates);
        ArrayList<Integer> lst = (ArrayList<Integer>) Arrays.asList(ans);
        return this.G.BFS(q0,lst);
    }

    //concatenates this DFA with a given DFA and returns an NFA
    public NFA concat(DFA b){
        //creates new Sigma - union of SigmaA and SigmaB and {'e'}
        String newSigma = createConcatSigma(this.getSigma(),b.getSigma());
        //creates new States - first A states and after them B states
        Boolean[] newStates = createConcatStates(this.getStates(),b.getStates());
        //creates new accepting states - B states (which are B states in new States diverted by A states length)
        int[] newAccStates = createAccStates(b.GetAcceptingStates(),this.getStates().length);
        //create newDelta
        Set<Pair<Integer,Boolean>>[][] newDelta = new HashSet[newStates.length][newSigma.length()];
        boolean isAState;
        int iIdx;
        int jIdx;
        for(int i=0; i<newDelta.length;i++){
            for(int j=0; j<newDelta[0].length;j++){
                newDelta[i][j] = new HashSet<>();
                isAState = (i<this.getStates().length);
                //stay B transitions the same
                if(!isAState){
                    //gets the original state index of the state i in StatesB by left shifting
                    iIdx = i-this.getStates().length;
                    //gets the original index of the letter j in SigmaB
                    jIdx = b.getSigma().indexOf(newSigma.charAt(j));
                    //if i in StatesB then Delta(i,sigma) = DeltaB(i,sigma)
                    newDelta[i][j].add(new Pair<>(b.getDelta()[iIdx][jIdx],true));
                }
                //stay A transitions the same
                //add epsilon transition from accepting states of a to q0b
                else{
                    //the original index of i is i
                    iIdx = i;
                    //gets the original index of the letter j in SigmaA
                    jIdx = this.getSigma().indexOf(newSigma.charAt(j));
                    //if i in StatesA then Delta(i,sigma) = DeltaA(i,sigma)
                    newDelta[i][j].add(new Pair<>(this.getDelta()[iIdx][jIdx],false));
                    //for i accepting state Delta(i,e) = {q_0b}
                    if(this.isAcceptingState(iIdx) && j+1 == newSigma.length() && newSigma.charAt(j)== 'e' ){
                        //q0b index in newStates is |Q_A|
                        int q0b = this.getStates().length;
                        newDelta[i][j].add(new Pair<>(q0b,true));
                    }
                }
            }
        }
        return new NFA(newSigma,newStates.length,newDelta,newAccStates);
    }

    //given a string return the smallest substring that belongs to this DFAs language
    public int splitString(String w){
        int idx = 1;
        while(this.belongs(w.substring(0,idx))){
            idx++;
        }
        return idx-1;
    }

}
