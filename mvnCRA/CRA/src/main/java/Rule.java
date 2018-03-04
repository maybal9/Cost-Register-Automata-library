
public class Rule<T> {
    //fields
    private Integer[] regsList;
    private T change;
    private boolean isCommutative;
    private int numOfRegs;

    public Rule(int numOfRegs, T n, boolean isComm){
        this.numOfRegs = numOfRegs;
        this.isCommutative = isComm;
        this.regsList = new Integer[numOfRegs];
        this.change = n;
    }

    public Rule(Integer[] array, T n, boolean isComm){
        this.isCommutative = isComm;
        this.numOfRegs = array.length;
        this.regsList = array;
        this.change = n;
    }

    public Integer[] getRegisters(){
        return this.regsList;
    }

    public T getChange(){
        return this.change;
    }


}
