package AutomataBuilders;//please insert a full delta function.
//regs with no change denote "ri=ri";

import core.ACRA;
import helpers.*;
import tests.Tests;

import java.util.ArrayList;


public class ACRABuilder {

    private String sigma = "";
    private int numOfStates = 0;
    private int numOfRegs = 0;
    private int[] acceptingStates = null;
    private Parser p = null;


    public ACRA buildACRA(String sigma, int numOfStates, int numOfRegs, int[] acceptingStates,
                          Pair<Integer, String[]>[][] listOfUpdateRules, ArrayList<String[]> listOfOutputRules){

        buildStaticVars(sigma, numOfStates, numOfRegs, acceptingStates);
        p = new AdditiveParser(this.numOfRegs);

        //create output function
        UpdateRuleList<Integer>[] neu = new UpdateRuleList[listOfOutputRules.size()];
        int i=0;
        for(String[] l: listOfOutputRules){
            neu[i] = buildUpdateRuleList(l);
            i++;
        }

        //create delta
        DeltaImage<Integer>[][] delta = new DeltaImage[this.numOfStates][this.sigma.length()];
        int qj;
        String[] updates;
        for(int q=0; q<numOfStates; q++){
            for(int s=0; s<this.sigma.length(); s++){
                qj = listOfUpdateRules[q][s].getKey();
                updates = listOfUpdateRules[q][s].getValue();
                UpdateRuleList<Integer> l = buildUpdateRuleList(updates);
                delta[q][s] = new DeltaImage<>(qj,l);
            }
        }

        //**
        ACRA ans = new ACRA(this.sigma,this.numOfStates,this.acceptingStates,this.numOfRegs,neu,delta);
        Tests<Integer> t = new Tests<>();

        try {
            t.testACRA(ans);
        } catch (BadArgumentException e) {
            e.printStackTrace();
        }

        resetACRABuilder();
        return ans;
    }

    private void buildStaticVars(String sigma,int numOfStates, int numOfRegs, int[] acceptingStates){
        this.sigma = sigma;
        this.numOfStates = numOfStates;
        this.numOfRegs = numOfRegs;
        this.acceptingStates = new int[acceptingStates.length];
        for(int i=0; i<acceptingStates.length; i++){
            this.acceptingStates[i] = acceptingStates[i];
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

    private void resetACRABuilder(){
        this.sigma="";
        this.numOfStates = 0;
        this.numOfRegs = 0;
        this.acceptingStates=null;
        this.p = null;
    }

}



