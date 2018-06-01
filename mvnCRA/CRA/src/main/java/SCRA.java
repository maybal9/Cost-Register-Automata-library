
public class SCRA extends CRA<String> {

    public SCRA(String sigma, int numofstates, int[] AcceptingStates, int numofRegisters,
                UpdateRuleList<String> v, DeltaImage<String>[][] delta, String eta, boolean isCommutative) {
        super(sigma, numofstates, AcceptingStates, numofRegisters, v, delta, "", false);
    }

    @Override
    public String apply(String rhsRegVal, String change) {
        return rhsRegVal+change;
    }
}

