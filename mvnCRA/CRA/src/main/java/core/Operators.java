package core;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Operators<T> {

    public ArrayList<T> choose(CRA<T> c1, CRA<T> c2, String w){
        if(c1.belongs(w)) return c1.evaluate(w);
        else return c2.evaluate(w);
    }

    public ArrayList<ArrayList<T>> op(Function<ArrayList<T>,ArrayList<T>> mapper, ArrayList<CRA<T>> list, String w){
        boolean ans = true;
        for(CRA<T> c: list ){
            ans = ans & c.belongs(w);
        }
        if(ans) {
            return (ArrayList<ArrayList<T>>) list.stream().map(c -> c.evaluate(w)).map(mapper).collect(Collectors.toList());
        }
        return null;
    }

    public boolean isUnambigiouslyConcatenable(DFA a, DFA b){
        NFA noEpsilon = a.concat(b).removeEpsilonTrans();
        NFA TaggedCrossConcat = noEpsilon.crossTaggedProduct(noEpsilon);
        return !TaggedCrossConcat.has1Flag();
    }

    public ArrayList<T> split(CRA a, CRA b, String w){
        if(isUnambigiouslyConcatenable(a,b)){
            int sep = a.splitString(w);
            String w1 = w.substring(0, sep);
            String w2 = w.substring(sep);
            ArrayList<T> res = a.evaluate(w1);
            res.addAll(b.evaluate(w2));
            return res;
        }
        else {
            System.out.println("a and b are ambiguously concatenable");
            return null;
        }
    }

}
