package core;

import helpers.MuImage;
import helpers.State;
import helpers.UpdateRuleList;

import java.util.ArrayList;

public class SCRA<K> extends CRA<String,K> {

    public SCRA(String sigma, State<K>[] states, State<K> q0,
                boolean[] acceptingStates, int[][] delta, int numofRegisters,
                UpdateRuleList<String>[] nu, MuImage<String>[][] mu) {
        super(sigma, states, q0,acceptingStates,delta,numofRegisters,nu,mu,"", false);
    }

    public SCRA(String sigma, State<K>[] states, State<K> q0,
                boolean[] acceptingStates, int[][] delta, ArrayList<String> Regs,
                UpdateRuleList<String>[] nu, MuImage<String>[][] mu) {
        super(sigma, states, q0,acceptingStates,delta,Regs,nu,mu,"", false);
    }

    @Override
    public CRA createCRA(String sigma, State[] states, State q0,
                         boolean[] acceptingStates, int[][] delta,
                         ArrayList<String> Regs, UpdateRuleList<String>[] nu,
                         MuImage<String>[][] mu, String eta, boolean isCommutative) {
        return new SCRA<String>(sigma,states,q0,acceptingStates,delta,Regs,nu,mu);
    }

    @Override
    protected String apply(String rhsRegVal, String change) {
        return rhsRegVal+change;
    }
}

