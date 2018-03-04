
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

abstract public class CRA<T> {
    //fields:

    //Sigma - the Alephhbet represented as a String;
    private String Sigma;

    //States - the accepting and non-accepting states of M represented as a boolean array;
    private Boolean[] States;

    //Registers - all registers represented as an Array<T>;
    private  ArrayList<T> Registers;

    //eta - the initial value of all registers;
    private T eta;

    //neu - the output function: Q-->X*domain
    private UpdateRuleList<T> v;

    //Delta - the extended transition function: (Q*Sigma)-->(Q*(Rule)^|X|), represented as a 2-dim array of
    //the pairs <Q, <Rule>Array = <(X,domain)>Array>
    private DeltaImage<T>[][] delta;

    //commutativity flag
    private boolean isCommutative;

    //constructor
    public CRA(String sigma, int numofstates, int[] AcceptingStates , int numofRegisters,
               UpdateRuleList<T> v, DeltaImage<T>[][] delta, T eta, boolean isCommutative){
        //init Sigma
        this.Sigma = sigma;

        //init commutativity flag
        this.isCommutative = isCommutative;

        //init States
        this.States = new Boolean[numofstates];
        for(int i=0; i<numofstates; i++) this.States[i] = false;
        for (int AcceptingState : AcceptingStates) this.States[AcceptingState] = true;

        //init Registers
        this.Registers = new ArrayList<T>(numofRegisters);
        for(int i=0; i< numofRegisters; i++) this.Registers.set(i, this.eta);

        //init neu
        this.v = v;

        //init delta
        this.delta = delta;

        //init eta
        this.eta = eta;

    } // end of constructor


    //private methods:
    private int calc(char c){ return this.Sigma.indexOf(c);}
    private boolean isAcceptingState(int k){return States[k]; }

    //superApply. meant to encapsulate commutative apply and non-commutative apply
    private T superApply(Integer[] rhsRegsOrder, ArrayList<T> copyRegsState ,T change){
        //if operation is commutative then recursively apply on positive integers
        //gets: [1,1,0,0,1] return: apply(x,apply(y,apply(u,apply(eta,change)))))
        if(isCommutative){
            return commSuperApply(rhsRegsOrder, copyRegsState, change);
        }
        else{
            return nonCommSuperApply(rhsRegsOrder, copyRegsState,change);
        }
    }

    //eta is the neutral element of the regs domain
    //iterates over the ones in the array and adds the value in those cells to the
    //accumulated value and finally adds the change in the end
    private T commSuperApply(Integer[] order, ArrayList<T> copyOfRegsState, T change){
        int i=0;
        T ans = this.eta;
        while(i< order.length){
            //iterate over the ones in the order of regs in the rule, with no importance to order
            if(order[i]>0){
                T regVal = copyOfRegsState.get(i);
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
    private T nonCommSuperApply(Integer[] order, ArrayList<T> copyOfRegsState ,T change){
        T ans = this.eta;
        ArrayList orderList = new ArrayList<Integer>(Arrays.asList(order));
        int i = (int) Collections.max(orderList);
        int idxOfCurrReg = orderList.indexOf(i);
        while(i> 0 && idxOfCurrReg >= 0){
            T regVal = copyOfRegsState.get(idxOfCurrReg);
            ans = apply(regVal,ans);
            i--;
            idxOfCurrReg = orderList.indexOf(i);
        }
        return apply(ans, change);
    }

    //abstract method: apply - meant for subclasses of CRA to implement according to type
    abstract T apply(T rhsRegVal, T change);


    //evaluate functions of Automaton!!!

    T evaluate(String w){
        ArrayList<T> copyOfRegsState = new ArrayList<T>(this.Registers.size());
        copyOfRegsState.addAll(this.Registers);
        Configuration<T> currentConfig = new Configuration<T>(0,copyOfRegsState);
        for(int indexOfSigma = 0; indexOfSigma<w.length(); indexOfSigma++){
            currentConfig = evaluate(currentConfig, w.charAt(indexOfSigma));
        }
        Configuration<T> finalConfig = currentConfig;
        Integer finalState = finalConfig.getState();
        ArrayList<T> finalRegsState = finalConfig.getRegsState();
        if(isAcceptingState(finalState)){
            Rule<T> finalRule = this.v.getRule(finalState);
            T finalChange = finalRule.getChange();
            Integer[] finalRegsOrder = finalRule.getRegisters();
            return superApply(finalRegsOrder,finalRegsState,finalChange);
        }
        else return null;
    }

    Configuration<T> evaluate(Configuration<T> currConfig, char sigma){
        // extracting delta(qi, regsState) = (qj, {<xi,n> | xi is the referenced reg, n is the addition})
        DeltaImage<T> image = this.delta[currConfig.getState()][calc(sigma)];
        Integer nextState = image.getToState();
        UpdateRuleList rules = image.getUpdateRegsRules();
        ArrayList<T> copyOfRegsState = new ArrayList<T>(currConfig.getRegsState().size());
        for(int i=0; i<this.Registers.size(); i++){
            Rule<T> currRule = rules.getRule(i);
            T lhsRegOldVal = copyOfRegsState.get(i);
            T change = currRule.getChange();
            //dilemma: where to calculate the current reg state, in CRA or in apply func?
            //dilemma 2: where to consider the commutativity?
            Integer[] rhsRegsOrder = currRule.getRegisters();
            T newVal = superApply(rhsRegsOrder,copyOfRegsState,change);
            copyOfRegsState.set(i, newVal);
        }
        return new Configuration<T>(nextState,copyOfRegsState);
    }


}
