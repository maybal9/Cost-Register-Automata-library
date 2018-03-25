
import java.util.ArrayList;

public class DeltaRange<T> {
    private Rule<T>[] ruleList;
    private Pair<Integer, Rule<T>[]> image;
    private ArrayList<ArrayList<Pair<Integer, Rule<T>[]>>> range;

    public DeltaRange(Pair<Integer, Rule<T>[]>[][] r){
        this.range = new ArrayList<>(r.length);
        for(int i=0; i<r.length; i++){
            ArrayList a = new ArrayList<>(r[i].length);
            this.range.add(a);
            ArrayList<Pair<Integer, Rule<T>[]>> currRow = this.range.get(i);
            for(int j=0; j<currRow.size(); j++ ){
                currRow.add(j,r[i][j]);
            }
        }
    }

    public int[] size(){
        int[] ans = new int[2];
        ans[0] = this.range.size();
        int innerSize = this.range.get(0).size();
        boolean flag = true;
        for(int j=0; j<this.range.size() && flag; j++){
            flag &= (this.range.get(j).size() == innerSize);
        }
        if(flag) {ans[1] = innerSize;}
        else {ans[1] = 0;}
        return ans;
    }

    public Pair<Integer, Rule<T>[]> get(int i, int j){
        return range.get(i).get(j);
    }
}
