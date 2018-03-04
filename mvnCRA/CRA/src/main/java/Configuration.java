import javafx.util.Pair;

import java.util.ArrayList;

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
}
