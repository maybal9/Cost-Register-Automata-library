package helpers;

public class Rule<T>{

    //fields
    protected Integer regDest;
    protected int numOfRegs;
    protected Integer[] regsList;
    protected T change;
    protected boolean isCommutative;
    protected boolean isEmptyRule = false;

    public boolean isEmptyRule() {
        return isEmptyRule;
    }

    public Rule(Integer r, T eta){
        this.isCommutative = true;
        this.numOfRegs = 0;
        this.regDest = r;
        this.regsList = new Integer[0];
        this.change = eta;
    }

    public Rule(Integer r,Integer[] array, T n, boolean isComm){
        this.isCommutative = isComm;
        this.numOfRegs = array.length;
        this.regDest = r;
        this.regsList = new Integer[array.length];
        this.change = n;
        for(int i=0; i<array.length; i++){
            this.regsList[i]=array[i];
        }
    }

    public Rule<T> shiftLeftAllRegsBy(int count){
        boolean isComm = this.isCommutative;
        T n = this.change;
        int newNumOfRegs = this.numOfRegs + count;
        Integer newRegDest = this.regDest + count;
        Integer[] newArray = new Integer[newNumOfRegs];
        for(int i=0; i<newArray.length; i++){
            if(i<count){
                newArray[i] = 0;
            }
            else{ newArray[i] = this.regsList[i-count];}
        }
        return new Rule<T>(newRegDest,newArray,n,isComm);
    }

    public Rule<T> padRightWithZeros(int count){
        boolean isComm = this.isCommutative;
        T n = this.change;
        int newNumOfRegs = this.numOfRegs + count;
        Integer newRegDest = this.regDest;
        Integer[] newArray = new Integer[newNumOfRegs];
        for(int i=0; i<newArray.length; i++){
            if(i<this.numOfRegs){
                newArray[i] = this.regsList[i];
            }
            else{ newArray[i] = 0;}
        }
        return new Rule<T>(newRegDest,newArray,n,isComm);
    }

//    public Rule<T> shiftAllRegsBy(Integer[] toPushInFront){
//        int count = toPushInFront.length;
//        boolean isComm = this.isCommutative;
//        T n = this.change;
//        int newNumOfRegs = this.numOfRegs + count;
//        Integer newRegDest = this.regDest + count;
//        Integer[] newArray = new Integer[newNumOfRegs];
//        for(int i=0; i<newArray.length; i++){
//            if(i<count){
//                newArray[i] = toPushInFront[i];
//            }
//            else{ newArray[i] = this.regsList[i-count];}
//        }
//        return new Rule<T>(newRegDest,newArray,n,isComm);
//    }

    public Integer[] getRegisters(){ return this.regsList;   }

    public Integer getRegDest(){ return this.regDest;  }

    public T getChange(){
        return this.change;
    }

    public String printRule(){
        return "rule is: " +
                "regDest: " + printRegDest() + " regsList: " + printRegsList() + "change is: " + change;
    }

    private String printRegDest(){ return ""+getRegDest();}

    private String printRegsList(){
        String ans = "";
        for(int i=0; i<regsList.length; i++){
            ans = ans +" regsList ["+i+"] is: " + regsList[i] + ", ";
        }
        return ans;
    }



}
