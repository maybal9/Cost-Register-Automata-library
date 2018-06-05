package core;

import helpers.Pair;

import java.util.HashSet;
import java.util.Set;

public class CrossNFA extends CrossAutomaton{

    private Set<Pair<Integer,Integer>>[][] Delta;
    private String epsilon = "e";

    public CrossNFA(NFA a, NFA b, Pair<Integer,Integer>[] acc){
        super(a.getSigma(),a.getStates().length,b.getStates().length,acc);
        this.Delta = new HashSet[super.numOfStatesA*numOfStatesB][super.Sigma.length()];
        for(int i=0; i<Delta.length;i++){
            for(int j=0; j<Delta.length;j++){

            }
        }

    }
}
