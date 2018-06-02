package tests;

import core.ACRA;
import helpers.BadArgumentException;
import helpers.DeltaImage;
import helpers.Rule;
import helpers.UpdateRuleList;

import java.util.Arrays;

public class Tests<T> {

    public void testACRA(ACRA m) throws BadArgumentException{
        isSigmaValid(m.getSigma());
        isQValid(m.getStates().length);
        isAcceptingStatesValid(m.getAcceptingStates(), m.getStates().length);
        isRegistersValid(m.getRegisters().size());
        isOutputValid(m.getV(),m.getStates().length ,m.getRegisters().size());
        isDeltaValid(m.getDelta(), m.getStates().length, m.getSigma(), m.getRegisters().size());
        isCopyless(m);
    }

    private void isSigmaValid(String sigma) throws BadArgumentException{
        String title = "Sigma Error: ";
        String msg = "";
        if(sigma.length()<=0){
            msg = "the Aleph-Bet size should be greater than 0";
            throw new BadArgumentException(title+msg);
        }
    }

    private void isQValid(int numOfStates) throws BadArgumentException{
        String title = "Q Error: ";
        String msg = "";
        if(numOfStates<=0){
            msg = "number of states should be greater than 0";
            throw new BadArgumentException(title+msg);
        }
    }

    private void isRegistersValid(int numOfRegs) throws BadArgumentException{
        String title = "Registers Error: ";
        String msg = "";
        if(numOfRegs<0){
            msg = "number of registers should be greater than 0";
            throw new BadArgumentException(title+msg);
        } if(numOfRegs==0){
            msg = "we don't support DFA's here";
        }
    }

    private void isAcceptingStatesValid(int[] arr, int numOfStates) throws BadArgumentException{
        String title = "Accepting States Error: ";
        String msg = "";
        if(arr.length == 0) {
            msg = "no accepting states were defined";
            throw new BadArgumentException(title+msg);}
        if(arr.length > numOfStates){
            msg = "defined more accepting states than number of states";
            throw new BadArgumentException(title+msg);
        }else {
            boolean flag = true;
            Arrays.sort(arr);
            int maximalState = arr.length -1;
            for(int j=0; j<arr.length; j++){
                flag = flag & (arr[j]<=maximalState);
            }
            if(!flag){
                msg = "there exists an invalid accepting state";
                throw new BadArgumentException(title+msg);
            }

        }
    }

    private void isOutputValid(UpdateRuleList neu, int numOfStates, int numOfRegs) throws BadArgumentException{
        String title = "Accepting States Error: ";
        String msg = "";
        if(neu.getSize()<numOfStates){
            msg = "output function is not full";
            throw new BadArgumentException(title+msg);
        }
        else{
            if(neu.getSize() > numOfStates) {
                msg = "output function is larger than number of states";
                throw new BadArgumentException(title+msg);
            }
            else{
                for(int i=0; i<neu.getSize(); i++){
                    try{
                        isRuleValid(neu.getRule(i), numOfRegs, numOfStates);
                    } catch (BadArgumentException e){
                        msg = "rule number: "+ i + "is bad: ";
                        throw new BadArgumentException(title+msg+e.getMessage());
                    }
                }
            }
        }
    }

    private void isDeltaValid(DeltaImage<Integer>[][] delta, int numOfStates, String sigma, int numOfRegs) throws BadArgumentException{
        String title="Delta Error: ";
        String msg = "";
        if(!(delta.length==numOfStates && delta[0].length==sigma.length())){
            msg = "delta domain is not Q*Sigma";
            throw new BadArgumentException(title+msg);
        }
        else{
            for(int i=0; i<delta.length; i++){
                for(int j=0; j<delta[0].length; j++){
                    try {
                        isValidState(delta[i][j].getToState(), numOfStates);
                    } catch (BadArgumentException e){
                        msg = "delta["+i+"]["+j+"] destination state is not valid";
                        throw new BadArgumentException(title+msg);
                    }
                    try{
                        isOutputValid(delta[i][j].getUpdateRegsRules(), numOfStates, numOfRegs);
                    } catch(BadArgumentException e){
                        msg = "delta["+i+"]["+j+"] helpers.UpdateRuleList is not valid: ";
                        throw new BadArgumentException(title+msg+e.getMessage());
                    }
                }
            }
        }
    }

    private void isValidState(int state, int numOfStates) throws BadArgumentException{
        String title = "State Error: ";
        String msg = "";
        if(state>= numOfStates){
            msg = "invalid state";
            throw new BadArgumentException(title+msg);
        }
    }

    private void isRuleValid(Rule r, int numOfRegs, int numOfStates) throws BadArgumentException{
        String title="helpers.Rule Error: ";
        String msg = "";
        try{
            isValidState(r.getRegDest(), numOfStates);
        } catch (BadArgumentException e){
            msg = e.getMessage();
            throw new BadArgumentException(title+msg);
        }
        if(r.getRegisters().length!=numOfRegs){
            msg = "invalid helpers.Rule" ;
            throw new BadArgumentException(title+msg);
        }
    }

    private void isCopyless(ACRA m) throws BadArgumentException{
        boolean copyFlag = true;
        int[] copyCheck = new int[m.getRegisters().size()];
        for(int a=0; a<copyCheck.length; a++){
            copyCheck[a]=0;
        }
        for(int i=0; i<m.getDelta().length;i++){
            for(int j=0; j<m.getDelta()[0].length;j++){
                for(Rule r: m.getDelta()[i][j].getUpdateRegsRules().getUpdateRegsRules()){
                    for(int reg: r.getRegisters()){
                        copyCheck[reg]++;
                    }
                    copyFlag = checkCopy(copyCheck);
                }
                copyFlag = checkCopy(copyCheck);
            }
        }
        if(!copyFlag){
            throw new BadArgumentException("Not Copyless");
        }
    }

    private boolean checkCopy(int[] a){
        boolean ans = true;
        for(int reg: a){
            ans = ans & (reg<2);
        }
        return ans;
    }
}
