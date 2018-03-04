import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.util.Pair;
import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Gson gson = new GsonBuilder().create();
        Integer[] a = {1,1,0,0,1};
        Rule<Integer> r = new Rule<Integer>(a,3, true);
        gson.toJson(r,System.out);

    }

    public static void testWord(CRA M, String w){
        Object ans = M.evaluate(w);
        if(ans!=null) System.out.println("the word "+ w +" is accepted by M and it's value is: "+ ans);
    }

    //build ACRA from user
    /*public static ACRA buildACRA(){

        //init:
        Scanner reader = new Scanner(System.in);
        boolean[] okFlags = new boolean[6];
        for(int i=0; i<okFlags.length; i++) {okFlags[i] = false;}
        String sigma = "";
        int numOfStates = 0;
        int[] AcceptingStates = null;
        int numOfRegs = 0;
        ArrayList<Rule<Integer>> v = null;
        Pair<Integer, Rule<Integer>[]>[][] delta = null;
        System.out.println("please define ACRA, as follows:");

        //start getting input from user
        //getting first argument - Sigma
        while(!okFlags[0]) {
            System.out.println("please enter Sigma: ");
            sigma = reader.next();
            if(sigma.length() <= 0) System.out.println("invalid Alephbet");
            else{okFlags[0] = true;}
        }

        //getting second argument - numOfStates
        while (!okFlags[1]){
            System.out.println("please enter number of states: ");
            numOfStates = reader.nextInt();
            if(numOfStates <=0) System.out.println("number of states should be positive");
            else{okFlags[1] = true;}
        }

        //getting third argument - AcceptingStates
        while (!okFlags[2]){
            System.out.println("please enter the accepting states: ");
            String acc = reader.next();
            AcceptingStates = new int[acc.length()];
            for(int i=1; i<= AcceptingStates.length; i++){
                AcceptingStates[i] = Integer.parseInt(acc.substring(i-1,i));
            }
            if(AcceptingStates.length == 0) System.out.println("must have at least 1 accepting state");
            else {
                Arrays.sort(AcceptingStates);
                okFlags[2] = true;
                int maximalState = numOfStates -1;
                for(int j=0; j<AcceptingStates.length; j++){
                    okFlags[2] = okFlags[2] & (AcceptingStates[j]<=maximalState);
                }
                if(!okFlags[2]) System.out.println("there exists an invalid accepting state");
            }
        }

        //getting forth argument - number of registers
        while(!okFlags[3]){
            System.out.println("please enter number of registers: ");
            numOfRegs = reader.nextInt();
            if(numOfRegs <= 0) System.out.println("number of registers should be positive");
            else {okFlags[3] = true;}
        }

        //getting fifth argument - v
        while(!okFlags[4]){
            System.out.println("please enter the output function in the following format: qi:xi+n, qj:xj+m, ...");
            String parse = reader.next();
            v = parseOutputFunction(parse);
            if(v.size()<numOfStates) {
                System.out.println("the output function is not full");
            }
            else{
                okFlags[4] = true;
                for(int i=0; i<numOfStates; i++){
                    Integer[] a =v.get(i).getRegisters();
                    ArrayList regsList = new ArrayList<Integer>(Arrays.asList(a));
                    int max = (int) Collections.max(regsList);
                    okFlags[4] &= (a.length == numOfRegs) && (max <= numOfRegs);
                }
                if(okFlags[4] = false) {
                    System.out.println("there are some illegal rules");
                }
            }
        }

        //getting sixth argument - delta
        while(!okFlags[5]){
            System.out.println("please enter the extended transition function file: ");
            String dparse = reader.next();
            delta = parseDelta(dparse);
            //validity tests:
            okFlags[5] = true;
            //check if the size of delta is Q*|sigma|
            boolean allRowsInTheSameLength = true;
            int l = delta[0].length;
            for(int i=0; i<delta.length; i++){
                allRowsInTheSameLength &= (delta[i].length == l);
            }
            okFlags[5] &= (delta.length ==numOfStates && allRowsInTheSameLength && l == sigma.length());
            //check if every element in the range is valid:
            //check if the state component of Delta(i,j) is a valid state
            for(int i=0; i<delta.length; i++){
                for(int j=0; j<l; j++){
                    Pair<Integer, Rule<Integer>[]> curr = delta[i][j];
                    Integer imageState = curr.getKey();
                    Rule<Integer>[] imageRuleSet = curr.getValue();
                    okFlags[5] &= (imageState < numOfStates);
                    //check if all rules of delta(i,j) are valid rules:
                    //meaning each first component is a valid registers list:
                    //proper size of list and maximal element less than numOfRegs
                    for(int k=0; k<imageRuleSet.length; k++ ) {
                        Integer[] a =imageRuleSet[k].getRegisters();
                        ArrayList regsList = new ArrayList<Integer>(Arrays.asList(a));
                        int max = (int) Collections.max(regsList);
                        okFlags[5] &= (a.length == numOfRegs) && (max <= numOfRegs);
                    }
                }
            }
            if(!okFlags[5]){System.out.println("delta function is not valid!!!");
            }
        }
        //finish receiving input from user
        reader.close();
        return new ACRA(sigma,numOfStates,AcceptingStates,numOfRegs, v, delta);
    }

    //expecting a string in the format: "(qi, n), (qj, m)... (qk,j)"
    //example input: "(3,4), (2,1), (1,0), (0,0)"
    static ArrayList<Rule<Integer>> parseOutputFunction(String p){
        String[] tmp = p.split(", ");
        ArrayList<Rule<Integer>> ans = new ArrayList<Rule<Integer>>(tmp.length);
        for(int t=0; t< tmp.length; t++){
            String[] tokens = tmp[t].split(",");
            Integer q = Integer.valueOf(tokens[0]);
            Integer n = Integer.valueOf(tokens[1]);
            ans.add(t,new Rule<Integer>(q,n));
        }
        return ans;
    }

    //expecting a file in the following format:
    /*{ '1' : { 'a' : ['1', "t=t+3; f=f+5"],
                'b' : ['2', "t=t+3; f=f+5"]},
        '2' : { 'a' : ['1', "t=t+3; f=f+5"],
                'b' : ['2', "t=t+3; f=f+5"]}}

    static Pair<Integer, Rule<Integer>[]>[][] parseDelta(String pathToFile){
        
    }

    static Pair<Integer, Rule<Integer>[]> parseImage(){

    }

    //gets a string in the following form: "x=x+y+3"
    static Rule<Integer> ParseRule(String s){

    }

    */

    public boolean validateSigma(String sigma){  return sigma.length() > 0;  }

    public boolean validateNumOfStates(int n){   return n > 0;  }

    public boolean validateAcceptingStates(int[] a, int numOfStates){
        int maximalState = numOfStates -1;
        boolean ans = true;
        for (int j: a){
            ans&= (j<=maximalState);
        }
        return ans;
    }

    public boolean validateNumOfRegs(int x){  return (x >= 0);  }

    public boolean validateOutputFunc(ArrayList<Rule<Integer>> v,int numOfRegs, int numOfStates, int[] AcceptingStates){
        if(v.size()!= numOfStates) {
            System.out.println("the output function is not at the right size!");
            return false;
        }
        else{
            boolean ans = true;
            for(int i=0; i<v.size(); i++){
                Rule<Integer> currRule = v.get(i);
                Integer[] regs = currRule.getRegisters();
                ArrayList regsList = new ArrayList<Integer>(Arrays.asList(regs));
                int max = (int) Collections.max(regsList);
                ans &= (regs.length == numOfRegs) && (max <= numOfRegs);
            }
            if(okFlags[4] = false) {
                System.out.println("there are some illegal rules");
            }
        }
    }

    //check if the size of the regsList is exactly numOfRegs
    //and that the maximal register number is less than or equal to numOfRegs
    //PAY ATTENTION: regsList is a list of the order of the regs value appearance in the rule!!
    private boolean validateRule(Rule<Integer> r, int numOfRegs, int maxRegNum){
        boolean ans = true;
        Integer[] regs = r.getRegisters();
        ArrayList regsList = new ArrayList<Integer>(Arrays.asList(regs));
        int max = (int) Collections.max(regsList);
        ans &= (regs.length == numOfRegs) && (max <= numOfRegs);
        return ans;
    }


}