//a class that holds various parsing methods:
//main parsing methods: parseRule, parseRegister,

//creating an ACRA parser!!!
public class Parser<T> {

    int numOfRegs;

    public Parser(int n){
        this.numOfRegs = n;
    }

    private String trimSpacesInEdges(String s){
        String ans=s;
        if(!s.startsWith(" ") && !s.endsWith(" ")){
            return s;
        } else {
            if(s.startsWith(" ")) {
                ans = ans.substring(1);
            } if(s.endsWith(" ")){
                ans = ans.substring(0,ans.length());
            }
            return trimSpacesInEdges(ans);
        }
    }

    //expecting: "ri", make sure there are no spaces at the end of the string
    private int parseRegister(String s){
        if(s.startsWith("r") && !s.endsWith(" ")){
            String trimS = s.substring(1);
            if(trimS.matches("[0-9]+")){
                return Integer.parseInt(s.substring(1));
            }
            else{
                System.out.println("register name is not numeric, could not parse");
                return -1;
            }
        }
        else if(s.startsWith("r") && s.endsWith(" ")){
            String trimS = trimSpacesInEdges(s);
            return parseRegister(trimS);
        }
        else {
            System.out.println("problem parsing register, could not parse");
            return -1;
        }
    }

    //expecting a string in the form: "rj+rk+..+d", no dups
    private Pair<Integer[],Integer> parseRightHandSideInRule(String s){
        String delimiter = "+";

        //extract change as last argument of rule
        int lastIdxOfDel = s.lastIndexOf(delimiter);
        String change = s.substring(lastIdxOfDel+1);
        String trimChange = trimSpacesInEdges(change);
        int changeVal = Integer.parseInt(trimChange);


        //extract all registers in rule
        String u = s.substring(0,lastIdxOfDel);
        String[] vals = u.split("\\+");
        Integer[] regs = new Integer[vals.length];
        for(int i=0; i<vals.length; i++) {
            regs[i] = parseRegister(trimSpacesInEdges(vals[i]));
        }

        Integer[] rep = createRepresentingRegOrder(regs,this.numOfRegs);
        //create the needed information for rule
        return new Pair<>(rep,changeVal);
    }

    //expecting a string in the form: "ri = rj+rk+..+d"
    public Rule parseRule(String s) {
        String del = "=";
        int equalSignIndex = s.indexOf(del);

        //got: "ri"
        if(equalSignIndex<0){
            String trim = trimSpacesInEdges(s);
            int regDest = parseRegister(trim);
            Integer[] self = new Integer[1];
            self[0] = regDest;
            Integer[] rep = createRepresentingRegOrder(self,numOfRegs);
            return new Rule<>(regDest, rep, 0, true);
        }

        //parse LHS of rule
        String LHS = s.substring(0, equalSignIndex);
        String trimLHS = trimSpacesInEdges(LHS);
        int regToPlaceIn = parseRegister(trimLHS);

        //parse RHS of rule
        String RHS = s.substring(equalSignIndex + 1);
        String trimRHS = trimSpacesInEdges(RHS);
        Pair<Integer[], Integer> grammarRule = parseRightHandSideInRule(trimRHS);

        //create a new rule
        Rule<Integer> r = new Rule<>(regToPlaceIn, grammarRule.getKey(), grammarRule.getValue(), true);

        return r;
    }

    private Integer[] createRepresentingRegOrder(Integer[] self ,int numOfRegs){

        Integer[] ans = new Integer[numOfRegs];
        for(int i=0; i<ans.length; i++) {
            ans[i] = 0;
        }
        for(int j=0; j<self.length; j++){
            ans[self[j]] = 1;
        }

        return ans;
    }

    private void printArray(Integer[] a){
        String ans = "[";
        int i=0;
        while(i<a.length-1){
            ans = ans + a[i]+", ";
            i++;
        }
        ans = ans +a[i]+"]";
        System.out.println(ans);
    }


}
