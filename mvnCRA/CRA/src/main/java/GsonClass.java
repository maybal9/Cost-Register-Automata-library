import com.google.gson.*;

public class GsonClass {

    public static void main(String[] args){
        Gson gson = new GsonBuilder().create();
        Integer[] a = {1,1,0,0,1};
        Rule<Integer> r = new Rule<Integer>(a,3, true);
        gson.toJson(r,System.out);
    }

}
