package core;

import helpers.Graph;
import helpers.Pair;

import java.util.Set;

public class CrossAutomaton {
    protected Graph G;

    //Sigma - the Alephhbet represented as a String;
    protected String Sigma;
    protected int numOfStatesA;
    protected int numOfStatesB;
    protected Boolean[][] States;
    protected Pair<Integer,Integer> initialState;
    protected Pair<Integer,Integer>[] acceptingStates;

    public CrossAutomaton(String Sigma, int numOfStatesA, int numOfStatesB, Pair<Integer,Integer>[] acc){
        this.Sigma = Sigma;
        this.numOfStatesA = numOfStatesA;
        this.numOfStatesB = numOfStatesB;
        this.G = new Graph(numOfStatesA*numOfStatesB);
        this.initialState = new Pair<Integer, Integer>(0,0);

        //init States
        this.States = new Boolean[numOfStatesA][numOfStatesB];
        for(int i=0; i<numOfStatesA; i++) {
            for (int j = 0; j < numOfStatesB; j++) {
                this.States[i][j] = false;
            }
        }

        for (Pair<Integer,Integer> AcceptingState : acc)
        {this.States[AcceptingState.getKey()][AcceptingState.getValue()] = true;}

        //init acceptingStates
        this.acceptingStates = new Pair[acc.length];
        for(int i=0;i<acc.length;i++){
            this.acceptingStates[i] = acc[i];
        }
    }
}
