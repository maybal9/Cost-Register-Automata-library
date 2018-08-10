package helpers;

import java.util.ArrayList;
import java.util.Arrays;

public class UpdateRuleList<T> {

    private Rule<T>[] updateRegsRules;
    private int top;
    private int size;

    public UpdateRuleList(int initialCapacity){
        this.updateRegsRules = new Rule[initialCapacity];
        this.top = 0;
        this.size = initialCapacity;
    }

    public UpdateRuleList(){
        this.updateRegsRules = new Rule[0];
        this.top = 0;
        this.size = 0;
    }

    public UpdateRuleList(Rule<T>[] a){
        this.updateRegsRules = new Rule[a.length];
        for(int i=0; i<a.length; i++){
            updateRegsRules[i] = a[i];
        }
        this.top = a.length-1;
        this.size = a.length;
    }

    public Rule<T> getRule(int i){return this.updateRegsRules[i];}

    public int getSize(){return this.updateRegsRules.length;}

    public Rule<T>[] getUpdateRegsRules(){return this.updateRegsRules;}

    private void incTop(){this.top = this.top +1;}

    private UpdateRuleList<T> padAllRegsFromRightWithZeros(int count){
        Rule<T>[] newURL = new Rule[this.getUpdateRegsRules().length];
        for(int i=0; i<newURL.length; i++){
            newURL[i] = this.getUpdateRegsRules()[i].padRightWithZeros(count);
        }
        return new UpdateRuleList<>(newURL);
    }

    private UpdateRuleList<T> shiftLeftAllRegsBy(int count){
        Rule<T>[] newURL = new Rule[this.getUpdateRegsRules().length];
        for(int i=0; i<newURL.length; i++){
            newURL[i] = this.getUpdateRegsRules()[i].shiftLeftAllRegsBy(count);
        }
        return new UpdateRuleList<>(newURL);
    }

    public UpdateRuleList<T> concatURLs(UpdateRuleList<T> other, int numOfmyRegs, int numOfOtherRegs ){
        UpdateRuleList<T> newThis = this.padAllRegsFromRightWithZeros(numOfOtherRegs);
        UpdateRuleList<T> newOther = other.shiftLeftAllRegsBy(numOfmyRegs);
        return newThis.addAll(newOther);
    }

    private UpdateRuleList<T> addAll(UpdateRuleList<T> l){
        Rule<T>[] ans = new Rule[this.updateRegsRules.length+l.updateRegsRules.length];
        System.arraycopy(this.updateRegsRules, 0, ans, 0, this.updateRegsRules.length);
        System.arraycopy(l.updateRegsRules, 0, ans, this.updateRegsRules.length, l.updateRegsRules.length);
        return new UpdateRuleList<>(ans);
    }

    public void add(Rule<T> r){
        if(top < this.size){
            this.updateRegsRules[top] = r;
            incTop();
        } else{
            System.out.println("cannot add more rules to list!");
        }
    }

    public UpdateRuleList<T> resetList(T eta){
        EmptyRule<T>[] a = new EmptyRule[updateRegsRules.length];
        for(int i=0; i<updateRegsRules.length; i++){
            a[i] = new EmptyRule<T>(updateRegsRules[i].getRegDest(),eta);
        }
        return new UpdateRuleList<>(a);
    }

    public void resetFromTo(int start, int end, T eta){
        for(int i=start; i<end; i++){
            this.updateRegsRules[i] = new EmptyRule<T>(updateRegsRules[i].getRegDest(),eta);
        }
    }

    public String printURL(){
        String ans = "";
        for(int i=0; i<this.getUpdateRegsRules().length; i++){
            ans = ans + this.getUpdateRegsRules()[i].printRule()+ "\n";
        }
        return ans;
    }

}
