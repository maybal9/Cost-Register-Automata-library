package core;

import helpers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

abstract public class CRA<T,K> extends DFA<K>{

    /** members inherited from DFA:
     protected String Sigma;
     protected int numOfStates;
     protected State<K>[] States;
     protected State<K> q0;
     protected int initStateIndex;
     protected boolean[] AcceptingStates;
     protected int[][] Delta; */

//    //States - the accepting and non-accepting states of M represented as a boolean array;
//    protected Boolean[] States;

    //Registers - all registers represented as an Array<T>;
    protected  ArrayList<T> Registers;

    //eta - the initial value of all registers;
    protected T eta;

    //neu - the output function: Q-->(X*domain)^|X|
    protected UpdateRuleList<T>[] nu;

    //Delta - the extended transition function: (Q*Sigma)-->(Q*(helpers.Rule)^|X|), represented as a 2-dim array of
    //the pairs <Q, <helpers.Rule>Array = <(X,domain)>Array>
    protected MuImage<T>[][] mu;

    //commutativity flag
    protected boolean isCommutative;


    /**constructor*/

    public CRA(String sigma, State<K>[] states, State<K> q0,
               boolean[] acceptingStates, int[][] delta,
               int numofRegisters, UpdateRuleList<T>[] nu,
               MuImage<T>[][] mu, T eta, boolean isCommutative){
        super(sigma,states,q0,acceptingStates,delta);
        this.isCommutative = isCommutative;
        this.Registers = new ArrayList<>(numofRegisters);
        int i=0;
        while(i<numofRegisters){
            this.Registers.add(eta);
            i++;
        }
        this.nu = nu;
        this.mu = mu;
        this.eta = eta;
    } // end of constructor

    public CRA(String sigma, State<K>[] states, State<K> q0,
               boolean[] acceptingStates, int[][] delta,
               ArrayList<T> Regs, UpdateRuleList<T>[] nu,
               MuImage<T>[][] mu, T eta, boolean isCommutative){
        super(sigma,states,q0,acceptingStates,delta);
        this.isCommutative = isCommutative;
        this.Registers = new ArrayList<>(Regs);
        this.nu = nu;
        this.mu = mu;
        this.eta = eta;
    }

    /**factory create method*/
    public abstract CRA createCRA(String sigma, State[] states, State q0,
                                       boolean[] acceptingStates, int[][] delta,
                                       ArrayList<T> Regs, UpdateRuleList<T>[] nu,
                                       MuImage<T>[][] mu, T eta, boolean isCommutative);

    /**methods*/
    /**essential functions*/

    public CRA<T,Pair> createChooseCRA(CRA<T,K> other){
        DFA<Pair> a = super.createAllAcceptingCrossAutomaton(other);
        String newSigma = a.getSigma();
        State<Pair>[] newStates = a.getStates();
        State<Pair> newQ0 = a.getQ0();
        boolean[] acc = a.getAcceptingStates();
        int[][] newDelta = a.getDelta();
        T eta = this.eta;

        ArrayList<T> newRegs = new ArrayList<>(this.getRegisters());
        newRegs.addAll(other.getRegisters());
        UpdateRuleList<T>[] newNu =
                concatNu(newStates.length,this.States.length,eta,other.getRegisters().size(),other.getNu());
        MuImage<T>[][] newMu = new MuImage[newStates.length][newSigma.length()];
        for(int p=0; p<newStates.length;p++){
            for(int b=0; b<newSigma.length(); b++){
                int qiIdxInA1 = p/this.numOfStates;
                int qjIdxInA2 = p%this.numOfStates;
                int toState = newDelta[p][b];
                UpdateRuleList<T> u1 = this.getMu()[qiIdxInA1][b].getUpdateRegsRules();
                UpdateRuleList<T> u2 = other.getMu()[qjIdxInA2][b].getUpdateRegsRules();
                UpdateRuleList u = u1.concatURLs(u2,this.getRegisters().size(),other.getRegisters().size());
                newMu[p][b] = new MuImage<>(toState,u);
            }
        }
        boolean isComm = this.isCommutative && other.isCommutative;
        return createCRA(newSigma,newStates,newQ0,acc,newDelta,newRegs,newNu,newMu,eta,isComm);
    }

    private UpdateRuleList<T>[] concatNu(int numofNewStates, int numOfRows, T eta,
                                         int numOfOtherRegs ,UpdateRuleList<T>[] otherNu) {
        UpdateRuleList<T>[] newNu = new UpdateRuleList[numofNewStates];
        int A1NumOfRegs = this.getRegisters().size();
        for (int tau = 0; tau < newNu.length; tau++) {
            int qiIdxInA1 = tau / numOfRows;
            int qjIdxInA2 = tau % numOfRows;
            UpdateRuleList<T> currA1Nu = this.getNu()[qiIdxInA1];
            UpdateRuleList<T> currA2Nu = otherNu[qjIdxInA2];
            newNu[tau] = currA1Nu.concatURLs(currA2Nu,this.getRegisters().size(),numOfOtherRegs);
            if (super.isAcceptingState(qiIdxInA1)) {
                newNu[tau].resetFromTo(A1NumOfRegs,newNu[tau].getSize(),eta);
            } else {
                newNu[tau].resetFromTo(0,A1NumOfRegs,eta);
            }
        }
        return newNu;
    }


    //superApply. meant to encapsulate commutative apply and non-commutative apply
    private T superApply(Integer[] rhsRegsOrder, ArrayList<T> regsStateOriginal, T change){

        //if operation is commutative then recursively apply on positive integers
        //gets: [1,1,0,0,1] return: apply(x,apply(y,apply(u,apply(eta,change)))))
        if(isCommutative){
            return commSuperApply(rhsRegsOrder, regsStateOriginal, change);
        }
        else{
            return nonCommSuperApply(rhsRegsOrder, regsStateOriginal,change);
        }
    }

    //eta is the neutral element of the regs domain
    //iterates over the ones in the array and adds the value in those cells to the
    //accumulated value and finally adds the change in the end
    private T commSuperApply(Integer[] order, ArrayList<T> regsStateOriginal, T change){
        int i=0;
        T ans = this.eta;
        while(i< order.length){

            //iterate over the ones in the order of regs in the rule, with no importance to order
            if(order[i]>0){
                T regVal = regsStateOriginal.get(i);
                ans = apply(regVal,ans);
            }
            i++;
        }
        return apply(ans, change);
    }

    //eta is the neutral element of regs domain
    //finds the maximal number of the ordering, meaning the last value to add and starts by
    //applying the last value to the neutral and goes backwards until reach 1 - to avoid zeros
    //this in order to apply the values in their right order!!!
    private T nonCommSuperApply(Integer[] order, ArrayList<T> regsStateOriginal ,T change){
        T ans = this.eta;
        ArrayList orderList = new ArrayList<>(Arrays.asList(order));
        int max = (int) Collections.max(orderList);
        int counter = 1;
        int idxOfCurrReg = orderList.indexOf(counter);
        while(counter<=max){
            T regVal = regsStateOriginal.get(idxOfCurrReg);
            ans = apply(ans,regVal);
            counter++;
            idxOfCurrReg = orderList.indexOf(counter);
        }
        return apply(ans, change);
    }

    //abstract method: apply - meant for subclasses of core.CRA to implement according to type
    protected abstract T apply(T rhsRegVal, T change);

    //evaluate functions of Automaton!!!
    public ArrayList<T> evaluate(String w){
        //creates a copy of the regsState
        ArrayList<T> copyOfRegsState = new ArrayList<>(this.Registers);

        //creates initial configuration with qo and regsState
        //Q: why send the copy of regState instead the original?
        Configuration<T> currentConfig = new Configuration<>(0,copyOfRegsState);

        //loop: compute the next configuration with eval(sigma, currConfig)
        for(int indexOfSigma = 0; indexOfSigma<w.length(); indexOfSigma++){
            currentConfig = evaluate(currentConfig, w.charAt(indexOfSigma));

            //update original regsState after the change
            ArrayList<T> regsStatePostChange = currentConfig.getRegsState();
            setRegisters(regsStatePostChange);
        }

        //finished the run on w, now compute the final output
        Configuration<T> finalConfig = currentConfig;
        Integer finalState = finalConfig.getState();
        ArrayList<T> finalRegsState = finalConfig.getRegsState();

        //branching: if finished in an acc. state or not and follow accordingly
        if(isAcceptingState(finalState)){
            UpdateRuleList<T> outputRuleList = this.nu[finalState];
            Rule<T> outputRule;
            int regDest;
            for(int i=0; i< outputRuleList.getSize();i++){
                outputRule = outputRuleList.getRule(i);
                regDest = outputRule.getRegDest();
                T ans = this.eta;
                if(!outputRule.isEmptyRule()) {
                    T finalChange = outputRule.getChange();
                    Integer[] finalRegsOrder = outputRule.getRegisters();
                    ans = superApply(finalRegsOrder, finalRegsState, finalChange);

                } else{
                    ans = ((EmptyRule<T>)outputRule).getEta();
                }
                setRegisters(regDest, ans);
            }
            ArrayList<T> finalRegsVal = new ArrayList<>();
            finalRegsVal.addAll(getRegisters());

           //don't forget to reset the automaton!!!!
            resetRegs();
            return finalRegsVal;
        }
        else return null;
    }

    //computes the next configuration according to current configuration and the current letter being read
    protected Configuration<T> evaluate(Configuration<T> currConfig, char sigma){

        // extracting mu(qi, regsState) = (qj, {<xi,n> | xi is the referenced reg, n is the addition})
        // gets the information for the next step from delta table
        MuImage<T> image = this.mu[currConfig.getState()][calc(sigma)];
        int nextState = image.getToState();
        UpdateRuleList<T> rules = image.getUpdateRegsRules();

        //get the current regsState
        ArrayList<T> regsStateOriginal = currConfig.getRegsState();

        // creates a copy of the regs states to change them without collisions
        ArrayList<T> copyOfRegsState = new ArrayList<>(regsStateOriginal);

        // for each rule, preform it. read from original regsState, write into copy of regsState
        for(int i=0; i<rules.getSize(); i++){
            Rule<T> currRule = rules.getRule(i);
            int regDest = currRule.getRegDest();
            T newVal = this.eta;
            if(!currRule.isEmptyRule()) {
                T change = currRule.getChange();
                Integer[] rhsRegsOrder = currRule.getRegisters();
                newVal = superApply(rhsRegsOrder, regsStateOriginal, change);
            }
            else{
                newVal = ((EmptyRule<T>)currRule).getEta();
            }
            copyOfRegsState.set(regDest, newVal);
        }
        return new Configuration<>(nextState,copyOfRegsState);
    }

    /**help functions*/
    //getters
    public ArrayList<T> getRegisters() { return Registers; }

    public UpdateRuleList<T>[] getNu() { return nu; }

    public MuImage<T>[][] getMu() { return this.mu; }

    /**miscellaneous*/
    private int calc(char c){ return this.Sigma.indexOf(c);}

    private void printNumOfRegisters(){System.out.println("num of regs is: "+ this.Registers.size());}

    public void setRegisters(int i, T val){this.Registers.set(i,val); }

    public void setRegisters(ArrayList<T> other){
        for(int i=0; i<other.size(); i++) {
            T val = other.get(i);
            this.Registers.set(i,val);
        }
    }

    protected void resetRegs(){
        for(int i=0; i<this.Registers.size(); i++){
            this.Registers.set(i,this.eta);
        }
    }

    private void printRegs(){
        String ans = "";
        for(int i=0; i<this.Registers.size()-1; i++){
            ans = ans +" reg["+i+"] value is: " + this.Registers.get(i) + ", ";
        }
        ans = ans + " reg["+(this.Registers.size()-1)+"] value is: " + this.Registers.get(this.Registers.size()-1) + ", ";
        System.out.println(ans);
    }

}
