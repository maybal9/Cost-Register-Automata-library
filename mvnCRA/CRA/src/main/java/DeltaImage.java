public class DeltaImage<T> {
    //fields:
    private int toState;
    private Rule<T>[] updateRegsRules;

    //methods:
    public DeltaImage(int qj, Rule<T>[] a){
        this.toState = qj;
        this.updateRegsRules = a;
    }

    public int getToState(){return this.toState;}

    public Rule<T>[] getUpdateRegsRules(){return this.updateRegsRules;}

}
