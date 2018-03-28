import java.util.ArrayList;

//represents the current state we're in and the current regsState
public class Configuration<T> {

    private Pair<Integer, ArrayList<T>> config;

    Configuration(Integer q, ArrayList<T> registersState){
        this.config = new Pair<>(q, registersState);
    }

    public Integer getState(){
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
