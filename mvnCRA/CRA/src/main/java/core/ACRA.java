package core;

import helpers.DeltaImage;
import helpers.UpdateRuleList;

public class ACRA extends CRA<Integer>{
    //members:

    //constructor:
    public ACRA(String sigma1, int numofstates1, int[] AcceptingStates1 , int numofRegisters1,
                UpdateRuleList<Integer> v1, DeltaImage<Integer>[][] delta1){
        super(sigma1,numofstates1,AcceptingStates1,numofRegisters1,v1,delta1,0, true);
    }

    //methods:
    //consider recursive apply func
    Integer apply(Integer rhsRegVal, Integer change) {
        return rhsRegVal + change;
    }

}
