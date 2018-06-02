package core;

import helpers.DeltaImage;
import helpers.UpdateRuleList;

public class SCRA extends CRA<String> {

    public SCRA(String sigma, int numofstates, int[] AcceptingStates, int numofRegisters,
                UpdateRuleList<String> v, DeltaImage<String>[][] delta) {
        super(sigma, numofstates, AcceptingStates, numofRegisters, v, delta, "", false);
    }

    @Override
    public String apply(String rhsRegVal, String change) {
        return rhsRegVal+change;
    }
}

