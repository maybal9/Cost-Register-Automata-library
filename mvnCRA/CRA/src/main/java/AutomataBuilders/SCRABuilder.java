package AutomataBuilders;
//please insert a full delta function.
//regs with no change denote "ri=ri";

import core.CRA;
import core.SCRA;
import helpers.DeltaImage;
import helpers.Pair;
import helpers.Rule;
import helpers.UpdateRuleList;
import tests.Tests;

import java.util.ArrayList;

public class SCRABuilder {

    private String sigma = "";
    private int numOfStates = 0;
    private int numOfRegs = 0;
    private int[] acceptingStates = null;
    private Parser p = null;


    public SCRA buildSCRA(String sigma, int numOfStates, int numOfRegs, int[] acceptingStates,
                          Pair<Integer, String[]>[][] listOfUpdateRules, ArrayList<String[]> listOfOutputRules){

        buildStaticVars(sigma, numOfStates, numOfRegs, acceptingStates);
        p = new ConcatParser(this.numOfRegs);

        //create output function
        UpdateRuleList<String>[] neu = new UpdateRuleList[listOfOutputRules.size()];
        int i=0;
        for(String[] l: listOfOutputRules){
            neu[i] = buildUpdateRuleList(l);
            i++;
        }

        //create delta
        DeltaImage<String>[][] delta = new DeltaImage[this.numOfStates][this.sigma.length()];
        int qj;
        String[] updates;
        for(int q=0; q<numOfStates; q++){
            for(int s=0; s<this.sigma.length(); s++){
                qj = listOfUpdateRules[q][s].getKey();
                updates = listOfUpdateRules[q][s].getValue();
                UpdateRuleList<String> l = buildUpdateRuleList(updates);
                delta[q][s] = new DeltaImage<>(qj,l);
            }
        }

        //**
        SCRA ans = new SCRA(this.sigma,this.numOfStates,this.acceptingStates,this.numOfRegs,neu,delta);
//        Tests<Integer> t = new Tests<>();
//        try {
//            t.testACRA(ans);
//        } catch (helpers.BadArgumentException e) {
//            e.printStackTrace();
//        }

        resetCRABuilder();
        return ans;
    }

    private void buildStaticVars(String sigma,int numOfStates, int numOfRegs, int[] acceptingStates){
        this.sigma = sigma;
        this.numOfStates = numOfStates;
        this.numOfRegs = numOfRegs;
        this.acceptingStates = new int[acceptingStates.length];
        System.arraycopy(acceptingStates, 0, this.acceptingStates, 0, acceptingStates.length);
    }

    private UpdateRuleList<String> buildUpdateRuleList(String[] rules){
        UpdateRuleList<String> ans = new UpdateRuleList<>(this.numOfRegs);
        Rule<String> r;
        for (String rule : rules) {
            r = p.parseRule(rule);
            ans.add(r);
        }
        return ans;
    }

    private void resetCRABuilder(){
        this.sigma="";
        this.numOfStates = 0;
        this.numOfRegs = 0;
        this.acceptingStates=null;
        this.p = null;
    }

}



