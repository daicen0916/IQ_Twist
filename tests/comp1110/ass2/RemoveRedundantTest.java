package comp1110.ass2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


import static org.junit.Assert.assertTrue;
//Cen Dai u6266023
public class RemoveRedundantTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(20000);
    private void test(Set<String> input,Set<String> expected) {
        Set<String> output=TwistGame.removeRedundant(input);
        assertTrue("No solutions returned for problem " + input.toString() + ", expected " + expected, output != null);
        String instr=input.toString();
        String expstr = expected.toString();
        String outstr = output.toString();
        assertTrue("For problem " + instr + ", was expecting " + expstr + ", but got " + outstr, expstr.equals(outstr));

    }
    private Set<String> replace(Set<String> input,char c){
        Set<String> result=new HashSet<>();
        for(String s:input){
            String news=s.replace(s.charAt(0),c);
            result.add(news);
        }
        return result;
    }
    private String[]inputArray={"a1A0","a1A1","a1A2","a1A3","a1A4","a1A5","a1A6","a1A7"};
    private Set<String> input= new HashSet<String>(Arrays.asList(inputArray));

    @Test
    public void NoRedundantTest(){
        String[]inputArray={"a7A7","a6A4","a7A3","b7A5","b6A5","a6A6","a7A5","a6A7","a6A0"};
        Set<String> input= new HashSet<String>(Arrays.asList(inputArray));
        Set<String>expected=new HashSet<>(input);
        test(input,expected);
        String[]inputArray1={"a7A7","b6A7","c1A3","d2A6","e2C3","f3C2","g4A7","h6D0"};
        Set<String> input1= new HashSet<String>(Arrays.asList(inputArray1));
        Set<String>expected1=new HashSet<>(input1);
        test(input1,expected1);
    }
    @Test
    public void StrongSymmetricRedundantTest(){
        String[]outArray={"c1A0","c1A1"};
        Set<String>expected=new HashSet<>(Arrays.asList(outArray));
        String[]outArray1={"h1A0","h1A1"};
        Set<String>expected1=new HashSet<>(Arrays.asList(outArray1));
        test(replace(input,'c'),expected);
        test(replace(input,'h'),expected1);
    }
    @Test
    public void WeakSymmetricRedundantTest(){
        String[]outArray0={"b1A0","b1A1","b1A4","b1A5"};
        Set<String>expected0=new HashSet<>(Arrays.asList(outArray0));
        String[]outArray1={"e1A0","e1A1","e1A2","e1A3"};
        Set<String>expected1=new HashSet<>(Arrays.asList(outArray1));
        String[]outArray2={"f1A0","f1A1","f1A2","f1A3"};
        Set<String>expected2=new HashSet<>(Arrays.asList(outArray2));
        test(replace(input,'b'),expected0);
        test(replace(input,'e'),expected1);
        test(replace(input,'f'),expected2);
    }
    @Test
    public void MixRedundantTest(){
        String[]inputArray={"a7A7","a6A4","b1A0","b1A2","c3B4","c3B0","c3B6"};
        Set<String> input= new HashSet<String>(Arrays.asList(inputArray));
        String[]outArray={"a7A7","a6A4","b1A0","c3B0"};
        Set<String>expected=new HashSet<>(Arrays.asList(outArray));
        test(input,expected);
    }
}
