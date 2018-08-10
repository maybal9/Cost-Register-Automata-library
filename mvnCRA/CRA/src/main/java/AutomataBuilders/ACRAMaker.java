package AutomataBuilders;//please insert a full delta function.
//regs with no change denote "ri=ri";

import core.ACRA;
import helpers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ACRAMaker {

    private String Sigma;
    private int numOfStates;
    private State<Integer>[] States;
    private State<Integer> q0;
    private boolean[] AcceptingStates;
    private int[][] Delta;
    private int numOfRegs;
    private UpdateRuleList<Integer>[] nu;
    private MuImage<Integer>[][] mu;
    private Parser p;

    public ACRAMaker(){}

    public ACRA makeACRA(){
        return new ACRA(Sigma, States, q0, AcceptingStates, Delta, numOfRegs ,nu, mu);
    }

    public void setSigma(String s){
        this.Sigma = s;
    }

    public void setNumOfStates(int numOfStates) {
        this.numOfStates = numOfStates;
        setStates();
        setQ0();
    }

    private void setStates() {
        this.States = new State[this.numOfStates];
        for(int i=0; i<this.States.length; i++){
            this.States[i] = new State<Integer>(i);
        }
    }

    private void setQ0() {
        this.q0 = this.States[0];
    }

    public void setAcceptingStates(ArrayList<Integer> acceptingStates) {
        this.AcceptingStates = new boolean[this.States.length];
        for(int i=0; i< this.AcceptingStates.length; i++){
            this.AcceptingStates[i] = acceptingStates.contains(i);
        }
    }

    public void setRegisters(int n) {
        this.numOfRegs = n;
        p = new AdditiveParser(this.numOfRegs);
    }

    public void setNu(ArrayList<String[]> listOfOutputRules) {
        this.nu = new UpdateRuleList[this.States.length];
        int i=0;
        for(String[] l: listOfOutputRules){
            nu[i] = buildUpdateRuleList(l);
            i++;
        }
    }

    public void setMuAndDelta(Pair<Integer, String[]>[][] listOfUpdateRules) {
        this.mu = new MuImage[this.numOfStates][this.Sigma.length()];
        this.Delta = new int[this.numOfStates][this.Sigma.length()];
        int qj;
        String[] updates;
        for(int q=0; q<this.numOfStates; q++){
            for(int s=0; s<this.Sigma.length(); s++){
                qj = listOfUpdateRules[q][s].getKey();
                this.Delta[q][s] = qj;
                updates = listOfUpdateRules[q][s].getValue();
                UpdateRuleList<Integer> l = buildUpdateRuleList(updates);
                this.mu[q][s] = new MuImage(qj,l);
            }
        }
    }

    private UpdateRuleList<Integer> buildUpdateRuleList(String[] rules){
        UpdateRuleList<Integer> ans = new UpdateRuleList<>(this.numOfRegs);
        Rule<Integer> r;
        for(int i=0; i<rules.length; i++){
            r = p.parseRule(rules[i]);
            ans.add(r);
        }
        return ans;
    }

}



