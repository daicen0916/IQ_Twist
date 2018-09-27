package comp1110.ass2;
import static org.junit.Assert.*;

import org.junit.*;

public class PieceRotateTest {

    public String piecesToString(Pieces piece) {
        return piecesToString(piece.points);
    }

    public String piecesToString(Point[][] points) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                if (points[i][j] == null) {
                    sb.append(" ");
                } else if (points[i][j].color == Color.Red) {
                    sb.append((points[i][j].hole ? "r" : "R"));
                } else if (points[i][j].color == Color.Green) {
                    sb.append((points[i][j].hole ? "g" : "G"));
                } else if (points[i][j].color == Color.Blue) {
                    sb.append((points[i][j].hole ? "b" : "B"));
                } else if (points[i][j].color == Color.Yellow) {
                    sb.append((points[i][j].hole ? "y" : "Y"));
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Test
    public void testShapeA() {
        assertEquals("rRr\n  R\n", piecesToString(new Pieces('a', 0)));
        assertEquals(" r\n R\nRr\n", piecesToString(new Pieces('a', 1)));
        assertEquals("R  \nrRr\n", piecesToString(new Pieces('a', 2)));
        assertEquals("rR\nR \nr \n", piecesToString(new Pieces('a', 3)));

        assertEquals("  R\nrRr\n", piecesToString(new Pieces('a', 4)));
        assertEquals("r \nR \nrR\n", piecesToString(new Pieces('a', 5)));
        assertEquals("rRr\nR  \n", piecesToString(new Pieces('a', 6)));
        assertEquals("Rr\n R\n r\n", piecesToString(new Pieces('a', 7)));
    }

    @Test
    public void testShapeB() {
        assertEquals("RR \n rR\n", piecesToString(new Pieces('b', 0)));
        assertEquals(" R\nrR\nR \n", piecesToString(new Pieces('b', 1)));
        assertEquals("Rr \n RR\n", piecesToString(new Pieces('b', 2)));
        assertEquals(" R\nRr\nR \n", piecesToString(new Pieces('b', 3)));

        assertEquals(" rR\nRR \n", piecesToString(new Pieces('b', 4)));
        assertEquals("R \nRr\n R\n", piecesToString(new Pieces('b', 5)));
        assertEquals(" RR\nRr \n", piecesToString(new Pieces('b', 6)));
        assertEquals("R \nrR\n R\n", piecesToString(new Pieces('b', 7)));
    }

    @Test
    public void testShapeC() {
        assertEquals("BbBB\n", piecesToString(new Pieces('c', 0)));
        assertEquals("B\nb\nB\nB\n", piecesToString(new Pieces('c', 1)));
        assertEquals("BBbB\n", piecesToString(new Pieces('c', 2)));
        assertEquals("B\nB\nb\nB\n", piecesToString(new Pieces('c', 3)));

        assertEquals("BbBB\n", piecesToString(new Pieces('c', 4)));
        assertEquals("B\nb\nB\nB\n", piecesToString(new Pieces('c', 5)));
        assertEquals("BBbB\n", piecesToString(new Pieces('c', 6)));
        assertEquals("B\nB\nb\nB\n", piecesToString(new Pieces('c', 7)));

    }

    @Test
    public void testShapeD() {
        assertEquals("BBB\n bb\n", piecesToString(new Pieces('d', 0)));
        assertEquals(" B\nbB\nbB\n", piecesToString(new Pieces('d', 1)));
        assertEquals("bb \nBBB\n", piecesToString(new Pieces('d', 2)));
        assertEquals("Bb\nBb\nB \n", piecesToString(new Pieces('d', 3)));

        assertEquals(" bb\nBBB\n", piecesToString(new Pieces('d', 4)));
        assertEquals("B \nBb\nBb\n", piecesToString(new Pieces('d', 5)));
        assertEquals("BBB\nbb \n", piecesToString(new Pieces('d', 6)));
        assertEquals("bB\nbB\n B\n", piecesToString(new Pieces('d', 7)));
    }


    @Test
    public void testShapeE() {
        assertEquals("Gg\n g\n", piecesToString(new Pieces('e', 0)));
        assertEquals(" G\ngg\n", piecesToString(new Pieces('e', 1)));
        assertEquals("g \ngG\n", piecesToString(new Pieces('e', 2)));
        assertEquals("gg\nG \n", piecesToString(new Pieces('e', 3)));

        assertEquals(" g\nGg\n", piecesToString(new Pieces('e', 4)));
        assertEquals("G \ngg\n", piecesToString(new Pieces('e', 5)));
        assertEquals("gG\ng \n", piecesToString(new Pieces('e', 6)));
        assertEquals("gg\n G\n", piecesToString(new Pieces('e', 7)));

    }

    @Test
    public void testShapeF() {
        assertEquals("GGg\n g \n", piecesToString(new Pieces('f', 0)));
        assertEquals(" G\ngG\n g\n", piecesToString(new Pieces('f', 1)));
        assertEquals(" g \ngGG\n", piecesToString(new Pieces('f', 2)));
        assertEquals("g \nGg\nG \n", piecesToString(new Pieces('f', 3)));

        assertEquals(" g \nGGg\n", piecesToString(new Pieces('f', 4)));
        assertEquals("G \nGg\ng \n", piecesToString(new Pieces('f', 5)));
        assertEquals("gGG\n g \n", piecesToString(new Pieces('f', 6)));
        assertEquals(" g\ngG\n G\n", piecesToString(new Pieces('f', 7)));

    }


    @Test
    public void testShapeG() {
        assertEquals("y  \nyYY\n y \n", piecesToString(new Pieces('g', 0)));
        assertEquals(" yy\nyY \n Y \n", piecesToString(new Pieces('g', 1)));
        assertEquals(" y \nYYy\n  y\n", piecesToString(new Pieces('g', 2)));
        assertEquals(" Y \n Yy\nyy \n", piecesToString(new Pieces('g', 3)));

        assertEquals(" y \nyYY\ny  \n", piecesToString(new Pieces('g', 4)));
        assertEquals("yy \n Yy\n Y \n", piecesToString(new Pieces('g', 5)));
        assertEquals("  y\nYYy\n y \n", piecesToString(new Pieces('g', 6)));
        assertEquals(" Y \nyY \n yy\n", piecesToString(new Pieces('g', 7)));

    }


    @Test
    public void testShapeH() {
        assertEquals("yYY\n", piecesToString(new Pieces('h', 0)));
        assertEquals("y\nY\nY\n", piecesToString(new Pieces('h', 1)));
        assertEquals("YYy\n", piecesToString(new Pieces('h', 2)));
        assertEquals("Y\nY\ny\n", piecesToString(new Pieces('h', 3)));

        assertEquals("yYY\n", piecesToString(new Pieces('h', 4)));
        assertEquals("y\nY\nY\n", piecesToString(new Pieces('h', 5)));
        assertEquals("YYy\n", piecesToString(new Pieces('h', 6)));
        assertEquals("Y\nY\ny\n", piecesToString(new Pieces('h', 7)));

    }


















}
