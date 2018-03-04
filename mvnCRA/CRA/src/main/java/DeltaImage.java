public class DeltaImage<T> {
    //fields:
    private int toState;
    private UpdateRuleList updateRegsRules;

    //methods:
    public DeltaImage(int qj, UpdateRuleList a){
        this.toState = qj;
        this.updateRegsRules = a;
    }

    public int getToState(){return this.toState;}

    public UpdateRuleList getUpdateRegsRules(){return this.updateRegsRules;}

}
