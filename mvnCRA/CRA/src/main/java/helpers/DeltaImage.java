package helpers;

public class DeltaImage<T,K> {
    //fields:
    private K toState;
    private UpdateRuleList<T> updateRegsRules;

    //methods:
    public DeltaImage(K qj, UpdateRuleList a){
        this.toState = qj;
        this.updateRegsRules = a;
    }

    public K getToState(){return this.toState;}

    public UpdateRuleList<T> getUpdateRegsRules(){return this.updateRegsRules;}
}
