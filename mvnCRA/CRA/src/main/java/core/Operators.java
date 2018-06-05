package core;

import helpers.Pair;

import javax.print.attribute.IntegerSyntax;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Operators<T> {

    public Pair<T,ArrayList<T>> choose(CRA<T> c1, CRA<T> c2, String w){
        if(c1.belongs(w)) return c1.evaluate(w);
        else return c2.evaluate(w);
    }

    public Pair<ArrayList<T>,ArrayList<ArrayList<T>>> concat(ArrayList<CRA<T>> list, String w){
        boolean ans = true;
        for(int i=0; i<list.size();i++ ){
            ans = ans & list.get(i).belongs(w);
        }
        if(ans) {return new Pair<ArrayList<T>,ArrayList<ArrayList<T>>>();}
    }

    public boolean isUnambigious(DFA a, DFA b){
        NFA n1 = buildConcatNFA(a,b);
        NFA n2 = removeEpsilonTrans(n1);
        NFA product = buildCrossNFA(n2,n2);

    }

    //for sake of simplicity we can assume A and B have the same Sigma
    private NFA buildCrossNFA(NFA a, NFA b){

    }

    private NFA buildConcatNFA(DFA a, DFA b){
        int sizeQA = a.getStates().length;
        int sizeQB = b.getStates().length;
        int sizeSigmaA=a.getSigma().length();
        int sizeSigmaB=b.getSigma().length();
        String newSigma = a.getSigma() + b.getSigma() + "e";
        int newStates = sizeQA + sizeQB;
        int[] newacc = new int[b.getAcceptingStates().length];
        for(int i=0;i<b.getAcceptingStates().length;i++){
            newacc[i] = b.getAcceptingStates()[i] + sizeQA;
        }
        Set<Integer>[][] newDelta = new HashSet[newStates][newSigma.length()];
        for(int i=0; i<newDelta.length;i++){
            for(int j=0; j<newDelta[0].length;j++){
                //we leave A transitions the same
                if(i<sizeQA) {
                    if(j<sizeSigmaA) {
                        newDelta[i][j] = new HashSet<Integer>(a.getDelta()[i][j]);
                    }
                    //add e-transition for the accepting states of A to q0 of B
                    //last column of newDelta is "e"
                    if(j==newSigma.length()-1){
                        //if i is an accepting state of A
                        if(a.getStates()[i]){
                            //add to i's Delta an e-trans. to q_0 of B, which is at index sizeQA
                            newDelta[i][j]= new HashSet<Integer>();
                            newDelta[i][j].add(sizeQA);
                        }
                        else{
                            newDelta[i][j] = new HashSet<>();
                        }
                    }
                    else{
                        newDelta[i][j] = new HashSet<>();
                    }
                }
                //we leave B transitions the same
                if( (i>=sizeQA && i<sizeQB) && (j>= sizeSigmaA && j<sizeSigmaB)){
                    newDelta[i][j] = new HashSet<Integer>(b.getDelta()[i-sizeQA][j-sizeSigmaA]);
                }
                else{
                    newDelta[i][j] = new HashSet<>();
                }
            }
        }
        return new NFA(newSigma,newStates,newDelta,newacc);
    }

    private NFA removeEpsilonTrans(NFA n){
        String newSigma = n.getSigma().substring(0,n.getSigma().length()-1);
        int newNumOfStates = n.getStates().length;
        int[] newacc = n.getAcceptingStates();
        Set<Integer>[][] newDelta = new HashSet[newNumOfStates][newSigma.length()];
        for(int i=0; i<newNumOfStates; i++){
            for(int j=0; j<newSigma.length(); j++){
                Set<Integer> epsilonClosure = calcEpsilonClosure(n,i);
                Set<Integer> sigmaTransition = calcDelta(n,epsilonClosure,j);
                Set<Integer> finalEpsilon = calcEpsilonClosure(n,sigmaTransition);
                newDelta[i][j] = new HashSet<Integer>(finalEpsilon);
            }
        }
        return new NFA(newSigma,newNumOfStates,newDelta,newacc);

    }

    private Set<Integer> calcEpsilonClosure(NFA a, Set<Integer> s){
        int eIdx = a.getSigma().length();
        Set<Integer> ans = new HashSet<>();
        ans = calcDelta(a, s, eIdx);
        int iterationCount = a.getStates().length;
        while(iterationCount>0){
            ans = calcDelta(a, ans, eIdx);
            iterationCount++;
        }
        return ans;
    }

    private Set<Integer> calcEpsilonClosure(NFA a, int n){
        int eIdx = a.getSigma().length();
        Set<Integer> ans = new HashSet<>(a.getDelta()[n][eIdx]);
        int iterationCount = a.getStates().length;
        while(iterationCount>0){
            ans = calcDelta(a, ans, eIdx);
            iterationCount++;
        }
        return ans;
    }

    private Set<Integer> calcDelta(NFA a, Set<Integer> s, int sigma){
        Set<Integer> ans = new HashSet<>();
        for(Integer q: s){
            ans.addAll(a.getDelta()[q][sigma]);
        }
        return ans;
    }



}
