package comp1110.ass2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.Set;

import static org.junit.Assert.assertTrue;

public class GenerateAndRemoveTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(20000);
    private void GenerateTest(String nextPiece,String placement, String expected) {
        String output=TwistGame.generatePlacement(nextPiece,placement);
        assertTrue("No solutions returned for problem " + "Next Piece: "+nextPiece+"Placement: "+placement + ", expected " + expected, output != null);
        assertTrue("For problem " +"Next Piece: "+nextPiece+"Placement: "+placement  +", was expecting " + expected + ", but got " + output, expected.equals(output));

    }
    private void RemoveTest(String piece,String placement, String expected) {
        String output=TwistGame.remove(piece,placement);
        assertTrue("No solutions returned for problem " + "Next Piece: "+piece+"Placement: "+placement + ", expected " + expected, output != null);
        assertTrue("For problem " +"Piece: "+piece+"Placement: "+placement  +", was expecting " + expected + ", but got " + output, expected.equals(output));
    }

    @Test
    public void HeadGenerateTest(){
        String nextPiece="a7A7";
        String placement="b6A7c1A3d2A6e2C3f3C2g4A7h6D0";
        String expected="a7A7b6A7c1A3d2A6e2C3f3C2g4A7h6D0";
        GenerateTest(nextPiece,placement,expected);
    }
    @Test
    public void MiddleGenerateTest(){
        String nextPiece="e2C3";
        String placement="a7A7b6A7c1A3d2A6f3C2g4A7h6D0";
        String expected="a7A7b6A7c1A3d2A6e2C3f3C2g4A7h6D0";
        GenerateTest(nextPiece,placement,expected);
    }
    @Test
    public void EndGenerateTest(){
        String nextPiece="h6D0";
        String placement="a7A7b6A7c1A3d2A6e2C3f3C2g4A7";
        String expected="a7A7b6A7c1A3d2A6e2C3f3C2g4A7h6D0";
        GenerateTest(nextPiece,placement,expected);
    }
    @Test
    public void HeadRemoveTest(){
        String nextPiece="a7A7";
        String placement="a7A7b6A7c1A3d2A6e2C3f3C2g4A7h6D0";
        String expected="b6A7c1A3d2A6e2C3f3C2g4A7h6D0";
        RemoveTest(nextPiece,placement,expected);
    }
    @Test
    public void MiddleRemoveTest(){
        String nextPiece="d2A6";
        String placement="a7A7b6A7c1A3d2A6e2C3f3C2g4A7h6D0";
        String expected="a7A7b6A7c1A3e2C3f3C2g4A7h6D0";
        RemoveTest(nextPiece,placement,expected);
    }
    @Test
    public void EndRemoveTest(){
        String nextPiece="h6D0";
        String placement="a7A7b6A7c1A3d2A6e2C3f3C2g4A7h6D0";
        String expected="a7A7b6A7c1A3d2A6e2C3f3C2g4A7";
        RemoveTest(nextPiece,placement,expected);
    }
}
