public class UpdateRuleList<T> {
    private Rule<T>[] updateRegsRules;

    public UpdateRuleList(Rule<T>[] a){
        this.updateRegsRules = new Rule[a.length];
        for(int i=0; i<a.length; i++){
            updateRegsRules[i] = a[i];
        }
    }

    public Rule<T> getRule(int i){
        return this.updateRegsRules[i];
    }


}
