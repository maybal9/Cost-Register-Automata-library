public class UpdateRuleList<T> {

    private Rule<T>[] updateRegsRules;
    private int top;
    private int size;

    public UpdateRuleList(int initialCapacity){
        this.updateRegsRules = new Rule[initialCapacity];
        this.top = 0;
        this.size = initialCapacity;
    }

    public UpdateRuleList(Rule<T>[] a){
        this.updateRegsRules = new Rule[a.length];
        for(int i=0; i<a.length; i++){
            updateRegsRules[i] = a[i];
        }
        this.top = a.length-1;
        this.size = a.length;
    }

    public void add(Rule<T> r){
        if(top < this.size){
            this.updateRegsRules[top] = r;
            incTop();
        } else{
            System.out.println("cannot add more rules to list!");
        }
    }

    private void incTop(){
        this.top = this.top +1;
    }

    public Rule<T> getRule(int i){
        return this.updateRegsRules[i];
    }

    public int getSize(){
        return this.size;
    }

    public Rule<T>[] getUpdateRegsRules(){return this.updateRegsRules;}

}
