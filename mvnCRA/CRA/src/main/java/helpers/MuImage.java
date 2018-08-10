package helpers;

public class MuImage<T> {
    //fields:
    private int toState;
    private UpdateRuleList<T> updateRegsRules;

    //methods:
    public MuImage(int qjIdx, UpdateRuleList a){
        this.toState = qjIdx;
        this.updateRegsRules = a;
    }

    public int getToState(){return this.toState;}

    public UpdateRuleList<T> getUpdateRegsRules(){return this.updateRegsRules;}
}
