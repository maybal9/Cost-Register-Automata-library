package helpers;

import java.util.ArrayList;

//represents the current state we're in and the current regsState
public class Configuration<T,K> {

    private Pair<K, ArrayList<T>> config;

    public Configuration(K q, ArrayList<T> registersState){
        this.config = new Pair<>(q, registersState);
    }

    public K getState(){
        return this.config.getKey();
    }

    public ArrayList<T> getRegsState(){
        return this.config.getValue();
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
