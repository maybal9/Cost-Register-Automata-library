import AutomataBuilders.AdditiveParser;
import AutomataBuilders.ConcatParser;
import AutomataBuilders.SCRABuilder;
import core.ACRA;
import core.CRA;
import core.Operators;
import core.SCRA;
import helpers.BadArgumentException;
import helpers.DeltaImage;
import helpers.Rule;
import helpers.UpdateRuleList;
import tests.Tests;

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
        System.out.println("******operators********");
        Operators o = new Operators();
        String w1 = "bba";
        String w2 = "abb";
        printOutput(o.choose(a0,a1,w1),w1);
        printOutput(o.choose(a0,a1,w2),w2);


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
        for(int i=0; i<arr.size()-1; i++){
            ans = ans +"r"+i+"=" + arr.get(i) + ", ";
        }
        ans = ans + "r"+(arr.size()-1)+"=" + arr.get(arr.size()-1);
        System.out.println(ans);
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
        if(ans!=null) {
            System.out.println("the word "+ w +" is accepted by M and it's value " +
                    "induces the registers values: ");
            printRegsValueint(ans);
        }
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
        Integer[] regsPart = {1};

        Rule<Integer>[] neuArr0 = new Rule[1];
        Rule r1 = new Rule(0,regsPart,0,true);
        neuArr0[0] = r1;
        UpdateRuleList<Integer> neu0 = new UpdateRuleList(neuArr0);

        Rule<Integer>[] neuArr1 = new Rule[1];
        Rule r2= new Rule(0,regsPart,1,true);
        neuArr1[0] = r2;
        UpdateRuleList<Integer> neu1 = new UpdateRuleList(neuArr1);

        UpdateRuleList<Integer>[] neu = new UpdateRuleList[2];
        neu[0] = neu0;
        neu[1] = neu1;

        int[] F = {0,1};
        DeltaImage<Integer>[][] delta = new DeltaImage[2][2];
        Rule<Integer> r = new Rule(0,regsPart,1,true);
        Rule<Integer>[] m = new Rule[1];
        m[0] = r;
        UpdateRuleList<Integer> u = new UpdateRuleList<>(m);
        delta[0][0] = new DeltaImage<>(0,u);
        delta[0][1] = new DeltaImage<>(1,u);
        delta[1][0] = new DeltaImage<>(0,u);
        delta[1][1] = new DeltaImage<>(1,u);
        ACRA ans = new ACRA("ab",2,F,1,neu,delta);
        return ans;
    }

    public static ACRA buildACRA1(){
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

        //create accepting states
        int[] F = {0,1};

        //create the delta
        DeltaImage<Integer>[][] delta = new DeltaImage[2][2];

        //**
        Rule<Integer>[] m1 = new Rule[1];
        m1[0] = r1;
        UpdateRuleList<Integer> u1 = new UpdateRuleList<>(m1);

        //**
        Rule<Integer>[] m2 = new Rule[1];
        m2[0] = r2;
        UpdateRuleList<Integer> u2 = new UpdateRuleList<>(m2);

        //**
        delta[0][0] = new DeltaImage<>(0,u1);
        delta[0][1] = new DeltaImage<>(1,u2);
        delta[1][0] = new DeltaImage<>(0,u1);
        delta[1][1] = new DeltaImage<>(1,u2);

        //**
        ACRA ans = new ACRA("ab",2,F,numofregs,neu,delta);
        Tests<Integer> t = new Tests<>(false);
        try {
            t.testACRA(ans);
        } catch (BadArgumentException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public static SCRA buildSCRA0(){
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

        //create output function
        UpdateRuleList<String>[] neu = new UpdateRuleList[6];
        for(int i=0; i<6; i++){
            neu[i] = new UpdateRuleList<>(2);
            neu[i].add(okNotChanged);
            neu[i].add(errorNotChanged);
        }

        //create accepting states
        int[] F = {0,1,2,3,4,5};

        //create the delta
        DeltaImage<String>[][] delta = new DeltaImage[6][3];

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
        delta[0][0] = new DeltaImage<>(1,legalA);
        delta[0][1] = new DeltaImage<>(0,legalB);
        delta[0][2] = new DeltaImage<>(0,legalC);
        delta[1][0] = new DeltaImage<>(1,legalA);
        delta[1][1] = new DeltaImage<>(2,legalB);
        delta[1][2] = new DeltaImage<>(3,legalC);
        delta[2][0] = new DeltaImage<>(4,illegalAafterAB);
        delta[2][1] = new DeltaImage<>(0,legalB);
        delta[2][2] = new DeltaImage<>(0,legalC);
        delta[3][0] = new DeltaImage<>(1,legalA);
        delta[3][1] = new DeltaImage<>(5,illegalBafterAC);
        delta[3][2] = new DeltaImage<>(0,legalC);
        delta[4][0] = new DeltaImage<>(4,illegalAafterAB);
        delta[4][1] = new DeltaImage<>(2,legalB);
        delta[4][2] = new DeltaImage<>(3,legalC);
        delta[5][0] = new DeltaImage<>(1,legalA);
        delta[5][1] = new DeltaImage<>(5,illegalBafterAC);
        delta[5][2] = new DeltaImage<>(0,legalC);


        //**
        SCRA ans = new SCRA("abc",6,F,numofregs,neu,delta);
//        Tests<Integer> t = new Tests<>();
//        try {
//            t.testACRA(ans);
//        } catch (BadArgumentException e) {
//            e.printStackTrace();
//        }
        return ans;
    }

}