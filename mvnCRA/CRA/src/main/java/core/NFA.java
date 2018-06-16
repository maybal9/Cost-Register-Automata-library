package core;


import helpers.Pair;
import helpers.TaggedPair;

import java.util.*;

public class NFA extends Automaton{

    protected Set[][] Delta;
    protected String epsilon = "e";

    //sigma can contain epsilon, and delta too
    public NFA(String sigma, int numOfStates, Set[][] delta, int[] acc){
        super(sigma,numOfStates,acc);
        int powerSet = (int) Math.pow(2,numOfStates);
        Delta = new Set[numOfStates][super.Sigma.length()];
        for(int a=0; a<delta.length; a++){
            for(int b=0; b< delta[0].length; b++){
                Delta[a][b] = delta[a][b];
            }
        }
    }

    //getters
    public Set[][] getDelta(){
        return this.Delta;
    }

    public boolean belongs(String w){
        Boolean ans = false;
        Set<Integer> currentStateSet = new HashSet<>();
        currentStateSet.add(q0);
        Set<Integer> nextStateSet = new HashSet<Integer>();
        int i=0;
        while(i<w.length()){
            nextStateSet.clear();
            for(Integer q: currentStateSet) {
                nextStateSet.addAll(getNextStates(q, w.charAt(i)));
            } ++i;
            currentStateSet.clear();
            currentStateSet.addAll(nextStateSet);

        }
        for(Integer q: currentStateSet){
            if(super.States[q]){
                ans = true;
                break;
            }
        }
        return ans;
    }

    @Override
    public Boolean evaluate(String w){ return belongs(w); }

    //helper function for evaluate
    protected Set getNextStates(int thisState, char c){
        return Delta[thisState][super.Sigma.indexOf(c)];
    }

    //NFA operations
    //removes epsilon transitions
    public NFA removeEpsilonTrans(){
        String newSigma = this.getSigma().substring(0,this.getSigma().length()-1);
        int newNumOfStates = this.getStates().length;
        int[] newacc = this.getAcceptingStates();
        Set[][] newDelta = new HashSet[newNumOfStates][newSigma.length()];
        for(int i=0; i<newNumOfStates; i++){
            for(int j=0; j<newSigma.length(); j++){
                Set epsilonClosure = calcEpsilonClosure(i);
                Set sigmaTransition = calcDelta(epsilonClosure,j);
                Set finalEpsilon = calcEpsilonClosure(sigmaTransition);
                newDelta[i][j] = new HashSet(finalEpsilon);
            }
        }
        return new NFA(newSigma,newNumOfStates,newDelta,newacc);

    }

    //concat this NFA to a given NFA
    public NFA concat(NFA b){

        //creates new Sigma - union of SigmaA and SigmaB and {'e'}
        String newSigma = createConcatSigma(this.getSigma(),b.getSigma());

        //creates new States - first A states and after them B states
        Boolean[] newStates = createConcatStates(this.getStates(),b.getStates());

        //creates new accepting states - B states (which are B states in new States diverted by A states length)
        int[] newAccStates = createAccStates(b.GetAcceptingStates(),this.getStates().length);

        //create newDelta
        Set<Pair<Integer,Boolean>>[][] newDelta = new HashSet[newStates.length][newSigma.length()];
        boolean isAState = false;
        int iIdx = 0;
        int jIdx = 0;
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
                    newDelta[i][j].addAll(tagDelta(b.getDelta()[iIdx][jIdx],true));
                }

                //stay A transitions the same
                //add epsilon transition from accepting states of a to q0b
                else{

                    //the original index of i is i
                    iIdx = i;

                    //gets the original index of the letter j in SigmaA
                    jIdx = this.getSigma().indexOf(newSigma.charAt(j));

                    //if i in StatesA then Delta(i,sigma) = DeltaA(i,sigma)
                    newDelta[i][j].addAll(tagDelta(this.getDelta()[iIdx][jIdx],false));

                    //for i accepting state Delta(i,e) = {q_0b}
                    if(this.isAcceptingState(iIdx) && j+1 == newSigma.length() && newSigma.charAt(j)== 'e' ){
                        Set<Pair<Integer,Boolean>> img = new HashSet<Pair<Integer,Boolean>>();

                        //q0b index in newStates is |Q_A|
                        int q0b = this.getStates().length;
                        img.add(new Pair<Integer,Boolean>(q0b,true));
                        newDelta[i][j].addAll(img);
                    }
                }
            }
        }
        return new NFA(newSigma,newStates.length,newDelta,newAccStates);
    }

    //create a cross NFA with a given NFA
    public NFA crossProduct(NFA b){
        //creates new Sigma - intersection of SigmaA and SigmaB union {'e'}
        String newSigma = createCrossSigma(this.getSigma(),b.getSigma());

        //creates new States - pairs of <q_A,q_b>
        Boolean[] newStates = createCrossStates(this.getStates(),b.getStates());

        //creates new accepting states - pairs of accepting states
        int[] newAccStates = createCrossAccStates(this.getAcceptingStates(), b.getAcceptingStates(),this.getStates().length);

        //create newDelta
        Set<Pair<Integer,Integer>>[][] newDelta = new HashSet[newStates.length][newSigma.length()];
        for(int i=0; i<newStates.length; i++){
            for(int sigma=0; sigma<newSigma.length(); sigma++){
                newDelta[i][sigma] = new HashSet<>();
                int iIdx = i/b.getStates().length;
                int jIdx = i%b.getStates().length;
                int aSigmaIdx = this.getSigma().indexOf(newSigma.charAt(sigma));
                int bSigmaIdx = this.getSigma().indexOf(newSigma.charAt(sigma));
                newDelta[i][sigma].addAll(
                        createCrossDelta(this.getDelta()[iIdx][aSigmaIdx],b.getDelta()[jIdx][bSigmaIdx]));
            }
        }

        return new NFA(newSigma,newStates.length, newDelta, newAccStates);
    }

    public NFA crossTaggedProduct(NFA b){
        //creates new Sigma - intersection of SigmaA and SigmaB union {'e'}
        String newSigma = createCrossSigma(this.getSigma(),b.getSigma());

        //creates new States - pairs of <q_A,q_b>
        Boolean[] newStates = createCrossStates(this.getStates(),b.getStates());

        //creates new accepting states - pairs of accepting states
        int[] newAccStates = createCrossAccStates(this.getAcceptingStates(), b.getAcceptingStates(),this.getStates().length);

        //create newDelta
        Set<TaggedPair<Integer,Integer,Boolean>>[][] newDelta = new HashSet[newStates.length][newSigma.length()];
        for(int i=0; i<newStates.length; i++){
            for(int sigma=0; sigma<newSigma.length(); sigma++){
                newDelta[i][sigma] = new HashSet<>();
                int iIdx = i/b.getStates().length;
                int jIdx = i%b.getStates().length;
                int aSigmaIdx = this.getSigma().indexOf(newSigma.charAt(sigma));
                int bSigmaIdx = this.getSigma().indexOf(newSigma.charAt(sigma));
                newDelta[i][sigma].addAll(
                        createTaggedCrossDelta(this.getDelta()[iIdx][aSigmaIdx],b.getDelta()[jIdx][bSigmaIdx]));
            }
        }

        return new NFA(newSigma,newStates.length, newDelta, newAccStates);
    }

    protected Set<Pair<Integer,Integer>> createCrossDelta(Set<Integer> first, Set<Integer> second){
        Set<Pair<Integer,Integer>> ans = new HashSet<>();
        for(Integer i: first){
            for(Integer j: second){
                ans.add(new Pair<Integer,Integer>(i,j));
            }
        }
        return ans;
    }

    protected Set<TaggedPair<Integer,Integer,Boolean>> createTaggedCrossDelta
            (Set<Pair<Integer,Boolean>> first, Set<Pair<Integer,Boolean>> second){
        Set<TaggedPair<Integer,Integer,Boolean>> ans = new HashSet<>();
        for(Pair<Integer,Boolean> i: first){
            for(Pair<Integer,Boolean> j: second){
                Integer a = i.getKey();
                Integer b = j.getKey();
                Boolean flag = (i.getValue() ^ j.getValue());
                ans.add(new TaggedPair<Integer,Integer,Boolean>(a,b,flag));
            }
        }
        return ans;
    }

    protected String createCrossSigma(String aSigma, String bSigma){
        StringBuilder ans = new StringBuilder();

        // foreah letter in bSigma - if it is also in aSigma then add it to newSigma
        for(char c: bSigma.toCharArray()){
            if(aSigma.indexOf(c) > -1){
                ans.append(c);
            }
        }
        if(aSigma.indexOf('e')== -1 && bSigma.indexOf('e')== -1){
            ans.append('e');
        }
        return ans.toString();
    }

    //given 2 boolean arrays create a new flat array for the cross product
    //the location of <i,j> in the new array is: i*|Q_B|+j
    protected Boolean[] createCrossStates(Boolean[] aStates, Boolean[]bStates){
        Boolean[] ans = new Boolean[aStates.length*bStates.length];
        int base = bStates.length;
        for(int i=0; i<aStates.length; i++){
            for(int j=0; j<bStates.length; j++){
                ans[i*base+j] = aStates[i] & bStates[j];
            }
        }
        return ans;
    }

    //foreach accepting state <i,j>
    //the new accepting state in the new states is: i*|Q_B|+j
    protected int[] createCrossAccStates(int[] aAcc, int[] bAcc, int bStateSize){
        int[] ans = new int[aAcc.length*bAcc.length];
        for(int idx=0; idx<ans.length; idx++){
            for(int i: aAcc){
                for(int j:bAcc){
                    ans[idx] = i*bStateSize+j;
                }
            }
        }
        return ans;
    }

    //helper functions for NFA operations
    protected Set calcEpsilonClosure(Set s){
        int eIdx = this.getSigma().length();
        Set ans = new HashSet<>();
        ans = calcDelta( s, eIdx);
        int iterationCount = this.getStates().length;
        while(iterationCount>0){
            ans = calcDelta( ans, eIdx);
            iterationCount++;
        }
        return ans;
    }

    protected Set calcEpsilonClosure(int n){
        int eIdx = this.getSigma().length();
        Set ans = new HashSet<>(this.getDelta()[n][eIdx]);
        int iterationCount = this.getStates().length;
        while(iterationCount>0){
            ans = calcDelta( ans, eIdx);
            iterationCount++;
        }
        return ans;
    }

    protected Set calcDelta(Set<Integer> s, int sigma){
        Set ans = new HashSet<>();
        for(Integer q: s){
            ans.addAll(this.getDelta()[q][sigma]);
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

    protected Set<Pair<Integer,Boolean>> tagDelta(Set<Integer> s, boolean b){
        Set<Pair<Integer,Boolean>> ans = new HashSet<Pair<Integer,Boolean>>();
        for(Integer i: s){
            ans.add(new Pair<Integer, Boolean>(i,b));
        }
        return ans;
    }

    public boolean has1Flag(){
        //check if there is a state with flag '1' (that is not an empty set)
        boolean ans = false;
        for(int i=0; i<this.getStates().length; i++){
            for(int j=0; j<this.getSigma().length(); j++){
                Set<TaggedPair<Integer,Integer,Boolean>> toCheck = this.getDelta()[i][j];
                if(!toCheck.isEmpty()){
                    for(TaggedPair<Integer,Integer,Boolean> q: toCheck){
                        ans = ans & q.getTag();
                    }
                }
            }
        } return ans;
    }

}

