
public class Rule<T> {
    //fields
    private Integer[] regsList;
    private T change;
    private boolean isCommutative;
    private int numOfRegs;


    public Rule(Integer[] array, T n, boolean isComm){
        this.isCommutative = isComm;
        this.numOfRegs = array.length;
        this.regsList = new Integer[array.length];
        this.change = n;
        for(int i=0; i<array.length; i++){
            this.regsList[i]=array[i];
        }
    }

    public Integer[] getRegisters(){
        return this.regsList;
    }

    public T getChange(){
        return this.change;
    }

    public String printRule(){
        return "rule is: regsList: " + printRegsList() + "change is: " + change;
    }

    private String printRegsList(){
        String ans = "";
        for(int i=0; i<regsList.length; i++){
            ans = ans +" regsList[i] is: " + regsList[i] + "\n";
        }
        return ans;
    }


}
