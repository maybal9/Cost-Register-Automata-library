import AutomataBuilders.ACRAMaker;
import AutomataBuilders.AdditiveParser;
import AutomataBuilders.ConcatParser;
import core.ACRA;
import core.DFA;
import core.SCRA;
import helpers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        System.out.println("");
        ACRA a0 = testA0();
        System.out.println("");
        ACRA a1 = testA1();
        System.out.println("");
        SCRA s0 = testS0();
        System.out.println("");
        ACRA a3 = testA3();
        System.out.println("");
        ACRA a4 = testA4();
        System.out.println("");
        ACRA a5 = testA5();
        System.out.println("");
        ACRA a6 = testA6();
        System.out.println("");
        ACRA a7 = testA7();
        System.out.println("");
        ACRA a8 = testA8();
        System.out.println("");
        ACRA c = testChoose();
        System.out.println("");
        ACRA c1 = testChoose78();
        System.out.println("");
        System.out.println("");
        newTestChoose78("ba#bb#");
        System.out.println("");
        newTestChoose78("a#bab#a");
        System.out.println("");
        newTestChoose78("baababa#aa");
        System.out.println("");
        newTestChoose78("aba#bab#aa#aa");
        System.out.println("");

    }

    public static void printOutput(ArrayList<Integer> ans, String w) {
        if (ans != null) {
            System.out.println("the word " + w + " is accepted by M and it's value " +
                    "induces the registers values: ");
            printRegsValueint(ans);
        }
    }

    private static void printRegsValuestr(ArrayList<String> arr){
        String ans = "";
        for(int i=0; i<arr.size()-1; i++){
            ans = ans +"r"+i+"=" + arr.get(i) + ", ";
        }
        ans = ans + "r"+(arr.size()-1)+"=" + arr.get(arr.size()-1);
        System.out.println(ans);
    }

    private static void printRegsValueint(ArrayList<Integer> arr){
        String ans = "";
        if(arr==null || arr.size()==0){
            System.out.println("Hi");
        }
        else {
            for (int i = 0; i < arr.size() - 1; i++) {
                ans = ans + "r" + i + "=" + arr.get(i) + ", ";
            }
            ans = ans + "r" + (arr.size() - 1) + "=" + arr.get(arr.size() - 1);
            System.out.println(ans);
        }
    }

    private static void printRegsValueWithRegsNames(ArrayList<String> arr, Map<Integer,String> regsNames){
        String ans = "";
        String name = "";
        for(int i=0; i<arr.size()-1; i++){
            name = regsNames.get(i);
            ans = ans + name + "=" + arr.get(i) + ", ";
        }
        ans = ans + regsNames.get(arr.size()-1)+"=" + arr.get(arr.size()-1);
        System.out.println(ans);
    }

    public static void testWord(ACRA M, String w){
        ArrayList ans = M.evaluate(w);
        System.out.println("the word "+ w +" is accepted by M and it's value " +
                    "induces the registers values: ");
        printRegsValueint(ans);
        M.resetRegs();
    }

    public static void testWordAgainst(ACRA m1,String name1 ,ACRA m2, String name2 ,ACRA c, String w){
        ArrayList ans1 = m1.evaluate(w);
        m1.resetRegs();
        ArrayList ans2 = m2.evaluate(w);
        m2.resetRegs();
        ArrayList ans3 = c.evaluate(w);
        if(m1.belongs(w)){
            System.out.println("the word "+ w +" is accepted by "+ name1 +" and it's value " +
                    "induces the registers values: ");
            printRegsValueint(ans1);
        } else{
            System.out.println("the word "+ w +" is NOT accepted by "+ name1 +" and therefore it's value " +
                    "is calculated by "+ name2 +" which induces the registers values: ");
            printRegsValueint(ans2);
        }
        System.out.println("the choose ACRA on "+w+" induces the registers value: ");
        printRegsValueint(ans3);
    }

    public static void testWord(SCRA M, String w){
        ArrayList ans = M.evaluate(w);
        if(ans!=null) {
            System.out.println("the word "+ w +" is accepted by M and it's value " +
                    "induces the registers values: ");
            printRegsValuestr(ans);
        }
    }

    public static void testWordWithRegsName(SCRA M, String w, Map<Integer,String> regsNames){
        ArrayList ans = M.evaluate(w);
        if(ans!=null) {
            System.out.println("the word "+ w +" is accepted by M and it's value " +
                    "produces the registers values: ");
            printRegsValueWithRegsNames(ans,regsNames);
        }
    }

    public static ACRA testA0(){
        ACRA m0 = buildACRA0();
        System.out.println("if (wa) then |w| else |w|+1");
        System.out.println("**********************");
        testWord(m0,"bba");
        testWord(m0,"ba");
        testWord(m0,"a");
        testWord(m0,"babababa");
        testWord(m0,"bbb");
        return m0;
    }

    public static ACRA testA1(){
        ACRA m1 = buildACRA1();
        System.out.println("if (wa) then 3#a else 5#b");
        System.out.println("**********************");
        testWord(m1,"bba");
        testWord(m1,"ab");
        testWord(m1,"bbab");
        testWord(m1,"bb");
        testWord(m1,"abb");
        return m1;
    }

    public static ACRA testA3(){
        ACRA m3 = buildACRA3();
        System.out.println("if (wa) then #b");
        System.out.println("**********************");
        testWord(m3,"bba");
        testWord(m3,"abbababababa");
        testWord(m3,"bbaba");
        testWord(m3,"bbbbba");
        testWord(m3,"abbaaaaaa");
        return m3;
    }

    public static ACRA testA4(){
        ACRA m4 = buildACRA4();
        System.out.println("if (bwb) then #(ab)+1 else if (awb) then #(ab)");
        System.out.println("**********************");
        testWord(m4,"bbab");
        testWord(m4,"abbababababab");
        testWord(m4,"bbabab");
        testWord(m4,"bbbbbab");
        testWord(m4,"abbaaaaaabab");
        return m4;
    }

    public static ACRA testA5(){
        ACRA m5 = buildACRA5();
        System.out.println("if (wa) then 7#a else undefined");
        System.out.println("**********************");
        testWord(m5,"bbaba");
        testWord(m5,"abbabababababa");
        testWord(m5,"bbabab");
        testWord(m5,"bbbbbabaa");
        testWord(m5,"abbaaaaaababa");
        return m5;
    }

    public static ACRA testA6(){
        ACRA m6 = buildACRA6();
        System.out.println("if (wa) then 30|w| else 50|w|");
        System.out.println("**********************");
        testWord(m6,"bbaba");
        testWord(m6,"abbabababababa");
        testWord(m6,"bbabab");
        testWord(m6,"bbbbbabaa");
        testWord(m6,"abbaaaaaababa");
        return m6;
    }

    public static ACRA testA7(){
        ACRA m7 = buildACRA7();
        System.out.println("Pay 10 for the first a in a block" +
                " and 1 for each consecutive a" +
                " if the letter ends with # output the number of blocks");
        System.out.println("**********************");
        testWord(m7,"b#bab#");
//        testWord(m7,"##a#a");
//        testWord(m7,"a##a#a#a#a#a#a");
//        testWord(m7,"##a#a#");
//        testWord(m7,"#a#aa");
//        testWord(m7,"a##aaaaaa#a#a");
        return m7;
    }

    public static ACRA testA8(){
        ACRA m8 = buildACRA8();
        System.out.println("Pay 10 for the first letter (a or b) in a block " +
                " and 1 for each consecutive same letter " +
                "until the first # then pay double");
        System.out.println("**********************");
        testWord(m8,"b#bab#");
//        testWord(m8,"bb#ab#a");
//        testWord(m8,"abb#aba#babababa");
//        testWord(m8,"bb#abab#");
//        testWord(m8,"bbb#bb#aba#a");
//        testWord(m8,"ab#ba#aa#aaaba#ba");
        return m8;
    }

    public static ACRA testChoose(){
        ACRA m3 = buildACRA3();
        ACRA m4 = buildACRA4();
        ACRA c = buildChooseACRA(m3,m4);
        System.out.println("if (wa) then #b else (if (bw) then #(ab)+1 else then #(ab))");
        System.out.println("**********************");
        System.out.println("words that end with a b: if (bw) then #(ab)+1 else then #(ab)");
        testWord(c,"bbab");
        testWord(c,"abbababababab");
        testWord(c,"bbabab");
        testWord(c,"bbbbbab");
        testWord(c,"abbaaaaaabab");
        testWord(c,"ababab");
        System.out.println("words that end with an a: if (wa) then #b");
        testWord(c,"ababa");
        testWord(c,"aaaaaaa");
        testWord(c,"ababaaaa");
        testWord(c,"bababababaaba");
        testWord(c,"abbba");
        return c;

    }

    public static ACRA testChoose78(){
        ACRA m7 = buildACRA7();
        ACRA m8 = buildACRA8();
        ACRA c = buildChooseACRA(m7,m8);
        System.out.println("words that do not end with a # and have at least 1 a: m7, else: m8");
        System.out.println("**********************");
        testWord(c,"b#bab#");
//        testWord(c,"aaaa#ab#a");
//        testWord(c,"aaaa#aaa");
//        testWord(c,"aa#a#");
//        testWord(c,"##aaa");
//        testWord(c,"a#a#a");
//        testWord(c, "aaaa#bbb");
//        testWord(c,"a#a#a#");
//        testWord(c, "aaaa#bbb#");
        return c;

    }

    public static void newTestChoose78(String w){
        ACRA m7 = buildACRA7();
        ACRA m8 = buildACRA8();
        ACRA c = buildChooseACRA(m7,m8);
        testWordAgainst(m7,"m7",m8,"m8",c,w);
    }


    public static SCRA testS0(){
        SCRA m2 = buildSCRA0();
        System.out.println("Correcting transmissions SCRA");
        System.out.println("after 'ab' there should not come an 'a'");
        System.out.println("after 'ac' there should not come an 'b'");
        System.out.println("**********************");
        Map<Integer, String> regsNames = new HashMap<>(2);
        regsNames.put(0,"OK");
        regsNames.put(1,"ERROR");
        String s = "aaaaabacc";
        System.out.println("the original transmission: " + s);
        testWordWithRegsName(m2,s,regsNames);
        System.out.println("");
        String s1 = "abbbacbb";
        System.out.println("the original transmission: " + s1);
        testWordWithRegsName(m2,s1,regsNames);
        System.out.println("");
        String s2 = "abacba";
        System.out.println("the original transmission: " + s2);
        testWordWithRegsName(m2,s2,regsNames);
        return m2;

    }

    public static ACRA buildACRA0(){
        String sigma = "ab";

        State<Integer> zero = new State<>(0);
        State<Integer> one = new State<>(1);
        State[] states = new State[2];
        states[0] = zero;
        states[1] = one;

        boolean[] F = {true,true};

        Integer[] regsPart = {1};

        Rule<Integer>[] neuArr0 = new Rule[1];
        Rule r1 = new Rule(0,regsPart,0,true);
        neuArr0[0] = r1;
        UpdateRuleList<Integer> neu0 = new UpdateRuleList(neuArr0);

        Rule<Integer>[] neuArr1 = new Rule[1];
        Rule r2 = new Rule(0,regsPart,1,true);
        neuArr1[0] = r2;
        UpdateRuleList<Integer> neu1 = new UpdateRuleList(neuArr1);

        UpdateRuleList<Integer>[] neu = new UpdateRuleList[2];
        neu[0] = neu0;
        neu[1] = neu1;


        int[][] delta = new int[states.length][sigma.length()];
        delta[0][0] = 0;
        delta[0][1] = 1;
        delta[1][0] = 0;
        delta[1][1] = 1;

        MuImage<Integer>[][] meu = new MuImage[2][2];
        Rule<Integer> r = new Rule(0,regsPart,1,true);
        Rule<Integer>[] m = new Rule[1];
        m[0] = r;
        UpdateRuleList<Integer> u = new UpdateRuleList<>(m);
        meu[0][0] = new MuImage<>(0,u);
        meu[0][1] = new MuImage<>(1,u);
        meu[1][0] = new MuImage<>(0,u);
        meu[1][1] = new MuImage<>(1,u);
        ACRA ans = new ACRA(sigma,states,zero,F,delta,1,neu,meu);
        return ans;
    }

    public static ACRA buildACRA1(){
        String sigma = "ab";

        State<Integer> zero = new State<>(0);
        State<Integer> one = new State<>(1);
        State[] states = new State[2];
        states[0] = zero;
        states[1] = one;

        boolean[] F = {true,true};

        int numofregs = 2;
        AdditiveParser p = new AdditiveParser(numofregs);
        //**
        String rule1 = "r0=r0+3";
        String rule2 = "r1=r1+5";
        String out1 = "r0=r0";
        String out2 = "r1=r1";

        //**
        Rule<Integer> r1 = p.parseRule(rule1);
        Rule<Integer> r2 = p.parseRule(rule2);
        Rule<Integer> o1 = p.parseRule(out1);
        Rule<Integer> o2 = p.parseRule(out2);

        //create output function
        UpdateRuleList<Integer> neu0 = new UpdateRuleList<>(1);
        neu0.add(o1);
        UpdateRuleList<Integer> neu1 = new UpdateRuleList<>(1);
        neu1.add(o2);

        UpdateRuleList<Integer>[] neu = new UpdateRuleList[2];
        neu[0] = neu0;
        neu[1] = neu1;

        int[][] delta = new int[states.length][sigma.length()];
        delta[0][0] = 0;
        delta[0][1] = 1;
        delta[1][0] = 0;
        delta[1][1] = 1;

        //create the mu
        MuImage<Integer>[][] meu = new MuImage[2][2];

        //**
        Rule<Integer>[] m1 = new Rule[1];
        m1[0] = r1;
        UpdateRuleList<Integer> u1 = new UpdateRuleList<>(m1);

        //**
        Rule<Integer>[] m2 = new Rule[1];
        m2[0] = r2;
        UpdateRuleList<Integer> u2 = new UpdateRuleList<>(m2);

        //**
        meu[0][0] = new MuImage<>(0,u1);
        meu[0][1] = new MuImage<>(1,u2);
        meu[1][0] = new MuImage<>(0,u1);
        meu[1][1] = new MuImage<>(1,u2);

        //**
        ACRA ans = new ACRA(sigma,states,zero,F,delta,numofregs,neu,meu);
//        Tests<Integer> t = new Tests<>(false);
//        try {
//            t.testACRA(ans);
//        } catch (BadArgumentException e) {
//            e.printStackTrace();
//        }
        return ans;
    }

    public static SCRA buildSCRA0(){
        String sigma = "abc";

        State<Integer> zero = new State<>(0);
        State<Integer> one = new State<>(1);
        State<Integer> two = new State<>(2);
        State<Integer> three = new State<>(3);
        State<Integer> four = new State<>(4);
        State<Integer> five = new State<>(5);

        State[] states = new State[6];
        states[0] = zero;
        states[1] = one;
        states[2] = two;
        states[3] = three;
        states[4] = four;
        states[5] = five;

        boolean[] F = {true,true,true,true,true,true};

        int numofregs = 2;
        ConcatParser p = new ConcatParser(numofregs);
        //**
        //ok updates
        Rule<String> okaRule = p.parseRule("r0=r0*a");
        Rule<String> okbRule = p.parseRule("r0=r0*b");
        Rule<String> okcRule = p.parseRule("r0=r0*c");
        Rule<String> okNotChanged = p.parseRule("r0=r0");
        //error updates
        Rule<String> errora = p.parseRule("r1=r1*a");
        Rule<String> errorb = p.parseRule("r1=r1*b");
        Rule<String> errorNotChanged = p.parseRule("r1=r1");

        //create Delta
        int[][] delta = new int[states.length][sigma.length()];
        delta[0][0] = 1;
        delta[0][1] = 0;
        delta[0][2] = 0;
        delta[1][0] = 1;
        delta[1][1] = 2;
        delta[1][2] = 3;
        delta[2][0] = 4;
        delta[2][1] = 0;
        delta[2][2] = 0;
        delta[3][0] = 1;
        delta[3][1] = 5;
        delta[3][2] = 0;
        delta[4][0] = 4;
        delta[4][1] = 2;
        delta[4][2] = 3;
        delta[5][0] = 1;
        delta[5][1] = 5;
        delta[5][2] = 0;

        //create output function
        UpdateRuleList<String>[] neu = new UpdateRuleList[6];
        for(int i=0; i<6; i++){
            neu[i] = new UpdateRuleList<>(2);
            neu[i].add(okNotChanged);
            neu[i].add(errorNotChanged);
        }

        //create the mu
        MuImage<String>[][] meu = new MuImage[6][3];

        //**
        Rule<String>[] aba = new Rule[2];
        aba[0] = okNotChanged;
        aba[1] = errora;
        UpdateRuleList<String> illegalAafterAB = new UpdateRuleList<>(aba);

        Rule<String>[] acb = new Rule[2];
        acb[0] = okNotChanged;
        acb[1] = errorb;
        UpdateRuleList<String> illegalBafterAC = new UpdateRuleList<>(acb);

        //**
        Rule<String>[] oka = new Rule[2];
        oka[0] = okaRule;
        oka[1] = errorNotChanged;
        UpdateRuleList<String> legalA = new UpdateRuleList<>(oka);

        Rule<String>[] okb = new Rule[2];
        okb[0] = okbRule;
        okb[1] = errorNotChanged;
        UpdateRuleList<String> legalB = new UpdateRuleList<>(okb);

        Rule<String>[] okc = new Rule[2];
        okc[0] = okcRule;
        okc[1] = errorNotChanged;
        UpdateRuleList<String> legalC = new UpdateRuleList<>(okc);

        //**
        meu[0][0] = new MuImage<>(1,legalA);
        meu[0][1] = new MuImage<>(0,legalB);
        meu[0][2] = new MuImage<>(0,legalC);
        meu[1][0] = new MuImage<>(1,legalA);
        meu[1][1] = new MuImage<>(2,legalB);
        meu[1][2] = new MuImage<>(3,legalC);
        meu[2][0] = new MuImage<>(4,illegalAafterAB);
        meu[2][1] = new MuImage<>(0,legalB);
        meu[2][2] = new MuImage<>(0,legalC);
        meu[3][0] = new MuImage<>(1,legalA);
        meu[3][1] = new MuImage<>(5,illegalBafterAC);
        meu[3][2] = new MuImage<>(0,legalC);
        meu[4][0] = new MuImage<>(4,illegalAafterAB);
        meu[4][1] = new MuImage<>(2,legalB);
        meu[4][2] = new MuImage<>(3,legalC);
        meu[5][0] = new MuImage<>(1,legalA);
        meu[5][1] = new MuImage<>(5,illegalBafterAC);
        meu[5][2] = new MuImage<>(0,legalC);


        //**
        SCRA ans = new SCRA(sigma,states,zero,F,delta,numofregs,neu,meu);
//        Tests<Integer> t = new Tests<>();
//        try {
//            t.testACRA(ans);
//        } catch (BadArgumentException e) {
//            e.printStackTrace();
//        }
        return ans;
    }

    public static ACRA buildACRA3(){
        ACRAMaker m = new ACRAMaker();
        m.setSigma("ab");
        m.setNumOfStates(2);
        ArrayList<Integer> acc = new ArrayList<>(1);
        acc.add(0);
        m.setAcceptingStates(acc);
        m.setRegisters(1);
        String outRule = "r0=r0";
        String[] outList = {outRule};
        ArrayList<String[]> outputList = new ArrayList<>(2);
        outputList.add(0,outList);
        outputList.add(1,outList);
        m.setNu(outputList);
        String incRule = "r0=r0+1";
        String dontChangeRule = "r0=r0";
        String[] incList = {incRule};
        String[] dontChangeList = {dontChangeRule};
        Pair<Integer, String[]>[][] mu = new Pair[2][2];
        mu[0][0] = new Pair(0,dontChangeList);
        mu[0][1] = new Pair(1,incList);
        mu[1][0] = new Pair(0,dontChangeList);
        mu[1][1] = new Pair(1,incList);
        m.setMuAndDelta(mu);
        return m.makeACRA();
    }

    public static ACRA buildACRA4(){
        ACRAMaker m = new ACRAMaker();
        m.setSigma("ab");
        m.setNumOfStates(2);
        ArrayList<Integer> acc = new ArrayList<>(1);
        acc.add(1);
        m.setAcceptingStates(acc);
        m.setRegisters(1);
        String outRule = "r0=r0";
        String[] outList = {outRule};
        ArrayList<String[]> outputList = new ArrayList<>(2);
        outputList.add(0,outList);
        outputList.add(1,outList);
        m.setNu(outputList);
        String incRule = "r0=r0+1";
        String dontChangeRule = "r0=r0";
        String[] incList = {incRule};
        String[] dontChangeList = {dontChangeRule};
        Pair<Integer, String[]>[][] mu = new Pair[2][2];
        mu[0][0] = new Pair(0,dontChangeList);
        mu[0][1] = new Pair(1,incList);
        mu[1][0] = new Pair(0,dontChangeList);
        mu[1][1] = new Pair(1,dontChangeList);
        m.setMuAndDelta(mu);
        return m.makeACRA();
    }

    public static ACRA buildACRA5() {
        ACRAMaker m = new ACRAMaker();
        m.setSigma("ab");
        m.setNumOfStates(2);
        ArrayList<Integer> acc = new ArrayList<>(1);
        acc.add(0);
        m.setAcceptingStates(acc);
        m.setRegisters(2);
        String outRule0 = "r0=r0";
        String outRule1 = "r1=r1";
        String[] out0List = {outRule0};
        String[] out1List = {outRule1};
        ArrayList<String[]> outputList = new ArrayList<>(2);
        outputList.add(0, out0List);
        outputList.add(1, out1List);
        m.setNu(outputList);
        String inc0By7Rule = "r0=r0+7";
        String dontChange0Rule = "r0=r0";
        String inc1By11Rule = "r1=r1+11";
        String dontChange1Rule = "r1=r1";
        String[] inc0By7List = {inc0By7Rule,dontChange1Rule};
        String[] inc1By11List = {dontChange0Rule,inc1By11Rule};
        Pair<Integer, String[]>[][] mu = new Pair[2][2];
        mu[0][0] = new Pair(0, inc0By7List);
        mu[0][1] = new Pair(1, inc1By11List);
        mu[1][0] = new Pair(0, inc0By7List);
        mu[1][1] = new Pair(1, inc1By11List);
        m.setMuAndDelta(mu);
        return m.makeACRA();
    }

    public static ACRA buildACRA6() {
        ACRAMaker m = new ACRAMaker();
        m.setSigma("ab");
        m.setNumOfStates(2);
        ArrayList<Integer> acc = new ArrayList<>(2);
        acc.add(0);
        acc.add(1);
        m.setAcceptingStates(acc);
        m.setRegisters(2);
        String out0Rule = "r0=r0";
        String out1Rule = "r1=r1";
        String[] out0List = {out0Rule};
        String[] out1List = {out1Rule};
        ArrayList<String[]> outputList = new ArrayList<>(2);
        outputList.add(0, out0List);
        outputList.add(1, out1List);
        m.setNu(outputList);
        String inc0by30Rule = "r0=r0+30";
        String inc1by50Rule = "r1=r1+50";
        String[] incList = {inc0by30Rule,inc1by50Rule};
        Pair<Integer, String[]>[][] mu = new Pair[2][2];
        mu[0][0] = new Pair(0, incList);
        mu[0][1] = new Pair(1, incList);
        mu[1][0] = new Pair(0, incList);
        mu[1][1] = new Pair(1, incList);
        m.setMuAndDelta(mu);
        return m.makeACRA();
    }

    public static ACRA buildACRA7() {
        ACRAMaker m = new ACRAMaker();
        m.setSigma("ab#");
        m.setNumOfStates(2);
        ArrayList<Integer> acc = new ArrayList<>(1);
        acc.add(1);
        m.setAcceptingStates(acc);
        m.setRegisters(2);
        String out0Rule = "r0=r0";
        String out1Rule = "r1=r1";
        String[] out0List = {out0Rule};
        String[] out1List = {out1Rule};
        ArrayList<String[]> outputList = new ArrayList<>(2);
        outputList.add(0, out0List);
        outputList.add(1, out1List);
        m.setNu(outputList);
        String inc1by10Rule = "r1=r1+10";
        String inc1by1Rule = "r1=r1+1";
        String inc0by1Rule = "r0=r0+1";
        String nochange0Rule = "r0=r0";
        String nochange1Rule = "r1=r1";
        String[] inc1by10List = {nochange0Rule,inc1by10Rule};
        String[] inc0by1List = {inc0by1Rule,nochange1Rule};
        String[] inc1by1List = {nochange0Rule,inc1by1Rule};
        String[] dontChangeList = {nochange0Rule,nochange1Rule};
        Pair<Integer, String[]>[][] mu = new Pair[2][3];
        mu[0][0] = new Pair(1, inc1by10List);
        mu[0][1] = new Pair(0, dontChangeList);
        mu[0][2] = new Pair(0, inc0by1List);
        mu[1][0] = new Pair(1, inc1by1List);
        mu[1][1] = new Pair(1, dontChangeList);
        mu[1][2] = new Pair(0, inc0by1List);
        m.setMuAndDelta(mu);
        return m.makeACRA();
    }

    public static ACRA buildACRA8() {
        ACRAMaker m = new ACRAMaker();
        m.setSigma("ab#");
        m.setNumOfStates(4);
        ArrayList<Integer> acc = new ArrayList<>(4);
        acc.add(0);
        acc.add(1);
        acc.add(2);
        acc.add(3);
        m.setAcceptingStates(acc);
        m.setRegisters(2);
        String out0Rule = "r0=r0";
        String out1Rule = "r1=r1";
        String[] out0List = {out0Rule};
        String[] out1List = {out1Rule};
        ArrayList<String[]> outputList = new ArrayList<>(4);
        outputList.add(0, out0List);
        outputList.add(1, out0List);
        outputList.add(2, out1List);
        outputList.add(3, out1List);
        m.setNu(outputList);

        String inc0by10Rule = "r0=r0+10";
        String inc0by1Rule = "r0=r0+1";
        String inc1by2Rule = "r1=r1+2";
        String inc1by20Rule = "r1=r1+20";
        String dontChange0Rule = "r0=r0";
        String dontChange1Rule = "r1=r1";
        String[] incfirstList = {inc0by10Rule,inc1by20Rule};
        String[] incsecondList = {inc0by1Rule,inc1by2Rule};
        String[] inc1onlyList = {dontChange0Rule,inc1by2Rule};
        String[] inc1only20List = {dontChange0Rule,inc1by20Rule};
        String[] dontChangeList = {dontChange0Rule,dontChange1Rule};
        Pair<Integer, String[]>[][] mu = new Pair[4][3];
        mu[0][0] = new Pair(1, incfirstList);
        mu[0][1] = new Pair(0, incsecondList);
        mu[0][2] = new Pair(2, dontChangeList);
        mu[1][0] = new Pair(1, incsecondList);
        mu[1][1] = new Pair(0, incfirstList);
        mu[1][2] = new Pair(2, dontChangeList);
        mu[2][0] = new Pair(3, inc1only20List);
        mu[2][1] = new Pair(2, inc1onlyList);
        mu[2][2] = new Pair(2, dontChangeList);
        mu[3][0] = new Pair(3, inc1onlyList);
        mu[3][1] = new Pair(2, inc1only20List);
        mu[3][2] = new Pair(3, dontChangeList);
        m.setMuAndDelta(mu);
        return m.makeACRA();
    }

    public static ACRA buildChooseACRA(ACRA a1, ACRA a2){
        return (ACRA) a1.createChooseCRA(a2);
    }

    public static DFA buildDFAEndsWithAnA(){
        String Sigma = "ab";

        State[] states = new State[2];
        State<Integer> zero = new State<>(0);
        State<Integer> one = new State<>(1);
        states[0] = zero;
        states[1] = one;

        boolean[] F = {true,false};

        int[][] delta = new int[states.length][Sigma.length()];
        delta[0][0] = 0;
        delta[0][1] = 1;
        delta[1][0] = 0;
        delta[1][1] = 1;
        return new DFA<Integer>(Sigma, states, zero, F, delta);
    }

    public static DFA buildDFAStartsWithAnA(){
        String Sigma = "ab";

        State[] states = new State[3];
        State<Integer> zero = new State<>(0);
        State<Integer> one = new State<>(1);
        State<Integer> two = new State<>(2);
        states[0] = zero;
        states[1] = one;
        states[2] = two;

        boolean[] F = {false,true,false};

        int[][] delta = new int[states.length][Sigma.length()];
        delta[0][0] = 1;
        delta[0][1] = 2;
        delta[1][0] = 1;
        delta[1][1] = 1;
        delta[2][0] = 2;
        delta[2][1] = 2;
        return new DFA<Integer>(Sigma, states, zero, F, delta);
    }

    public static DFA buildDFAContainsAA(){
        String Sigma = "ab";

        State[] states = new State[3];
        State<Integer> zero = new State<>(0);
        State<Integer> one = new State<>(1);
        State<Integer> two = new State<>(2);
        states[0] = zero;
        states[1] = one;
        states[2] = two;

        boolean[] F = {false,false,true};

        int[][] delta = new int[states.length][Sigma.length()];
        delta[0][0] = 1;
        delta[0][1] = 0;
        delta[1][0] = 2;
        delta[1][1] = 1;
        delta[2][0] = 2;
        delta[2][1] = 2;
        return new DFA<Integer>(Sigma, states, zero, F, delta);
    }

}