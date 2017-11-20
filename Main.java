public class Main {

    public static void main(String[] args) {
        String sigma = "01";
        int Q_size = 3;
        int[][] delta = { {0,1} , {2,1} , {2,2} };
        int[] acc  = {0,1};
        Automaton M = new Automaton(sigma, Q_size, delta, acc);
        testWord(M, "01");
        testWord(M, "00");
        testWord(M, "0");
        testWord(M, "1");
        testWord(M, "101");
        testWord(M, "aa");
        testWord(M, "");
    }

    public static void testWord(Automaton M, String w){
        if(M.isValidWord(w) && M.Evaluate(w)){
            System.out.println("the word "+ w +" is accepted by M");
        }
        else{
            System.out.println("the word "+ w +" is not accepted by M");
        }
    }
}
