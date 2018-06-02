import AutomataBuilders.AdditiveParser;
import AutomataBuilders.SCRABuilder;
import core.ACRA;
import core.CRA;
import core.SCRA;
import helpers.BadArgumentException;
import helpers.DeltaImage;
import helpers.Rule;
import helpers.UpdateRuleList;
import tests.Tests;

public class Main {

    public static void main(String[] args) {
        System.out.println("");
        testA0();
        System.out.println("");
        testA1();
        System.out.println("");
        testS0();

    }

    public static void testWord(CRA M, String w){
        Object ans = M.evaluate(w);
        if(ans!=null) System.out.println("the word "+ w +" is accepted by M and it's value is: "+ ans);
    }

    public static void testA0(){
        ACRA m0 = buildACRA0();
        System.out.println("if (wa) then |w| else |w|+1");
        System.out.println("**********************");
        testWord(m0,"bba");
        testWord(m0,"ba");
        testWord(m0,"a");
        testWord(m0,"babababa");
        testWord(m0,"bbb");
    }

    public static void testA1(){
        ACRA m1 = buildACRA1();
        System.out.println("if (wa) then 3#a else 5#b");
        System.out.println("**********************");
        testWord(m1,"bba");
        testWord(m1,"ab");
        testWord(m1,"bbab");
        testWord(m1,"bb");
    }

    public static void testS0(){
        SCRA m2 = buildSCRA0();
        System.out.println("NAME");
        System.out.println("**********************");
        testWord(m2,"bba");
        testWord(m2,"ba");
        testWord(m2,"a");
        testWord(m2,"babababa");
        testWord(m2,"bbb");
    }


    public static ACRA buildACRA0(){
        Rule<Integer>[] neuArr = new Rule[2];
        Integer[] regsPart = {1};
        Rule r1 = new Rule(0,regsPart,0,true);
        Rule r2= new Rule(0,regsPart,1,true);
        neuArr[0] = r1;
        neuArr[1] = r2;
        UpdateRuleList<Integer> neu = new UpdateRuleList<>(neuArr);
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
        String out1 = "r0";
        String out2 = "r1";

        //**
        Rule<Integer> r1 = p.parseRule(rule1);
        Rule<Integer> r2 = p.parseRule(rule2);
        Rule<Integer> o1 = p.parseRule(out1);
        Rule<Integer> o2 = p.parseRule(out2);

        //create output function
        UpdateRuleList<Integer> neu = new UpdateRuleList<>(2);
        neu.add(o1);
        neu.add(o2);

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
        Tests<Integer> t = new Tests<>();
        try {
            t.testACRA(ans);
        } catch (BadArgumentException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public static SCRA buildSCRA0(){
        SCRABuilder s = new SCRABuilder();
        return s.buildSCRA();
    }

}