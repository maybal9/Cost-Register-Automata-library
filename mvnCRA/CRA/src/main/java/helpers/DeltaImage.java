package helpers;

public class DeltaImage<T> {
    //fields:
    private int toState;
    private UpdateRuleList<T> updateRegsRules;

    //methods:
    public DeltaImage(int qj, UpdateRuleList a){
        this.toState = qj;
        this.updateRegsRules = a;
    }

    public int getToState(){return this.toState;}

    public UpdateRuleList<T> getUpdateRegsRules(){return this.updateRegsRules;}
}
