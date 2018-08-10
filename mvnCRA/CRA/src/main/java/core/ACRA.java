package core;

import helpers.MuImage;
import helpers.State;
import helpers.UpdateRuleList;

import java.util.ArrayList;

public class ACRA<K> extends CRA<Integer,K>{
    //members:

    //constructor:
    public ACRA(String sigma, State<K>[] states, State<K> q0,
                boolean[] acceptingStates, int[][] delta, int numofRegisters,
                UpdateRuleList<Integer>[] nu, MuImage<Integer>[][] mu){
        super(sigma,states,q0,acceptingStates,delta,numofRegisters,nu,mu,0, true);
    }

    public ACRA(String sigma, State<K>[] states, State<K> q0,
                boolean[] acceptingStates, int[][] delta, ArrayList<Integer> Regs,
                UpdateRuleList<Integer>[] nu, MuImage<Integer>[][] mu){
        super(sigma,states,q0,acceptingStates,delta,Regs,nu,mu,0, true);
    }

    @Override
    public CRA createCRA(String sigma, State[] states, State q0,
                         boolean[] acceptingStates, int[][] delta,
                         ArrayList<Integer> Regs, UpdateRuleList<Integer>[] nu,
                         MuImage<Integer>[][] mu, Integer eta, boolean isCommutative) {
        return new ACRA<Integer>(sigma,states,q0,acceptingStates,delta,Regs,nu,mu);
    }

    //consider recursive apply func
    protected Integer apply(Integer rhsRegVal, Integer change) {
        return rhsRegVal + change;
    }



}
