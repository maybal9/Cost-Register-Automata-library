package helpers;

import java.util.ArrayList;

//represents the current state we're in and the current regsState
public class Configuration<T> {

    private int currState;
    private ArrayList<T> currRegsValue;

    public Configuration(int q, ArrayList<T> registersState){
        this.currState = q;
        this.currRegsValue = new ArrayList<>(registersState.size());
        this.currRegsValue.addAll(registersState);
    }

    public int getState(){
        return this.currState;
    }

    public ArrayList<T> getRegsState(){
        return this.currRegsValue;
    }

    public String printConfig(){
        return "config is: " + getState() + " "+ printRegsList();
    }

    private String printRegsList(){
        String ans = "";
        for(int i=0; i<this.getRegsState().size(); i++){
            ans = ans +" regsList["+i+"] is: " + this.getRegsState().get(i) + ", ";
        }
        return ans;
    }

}
