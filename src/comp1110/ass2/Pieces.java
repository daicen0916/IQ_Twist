package comp1110.ass2;
public class Pieces {
    private int id;
    private char color;
    int position;

    public int getPiecePosition()
    {
        return position;
    }

    public char getPieceColor()
    {
        return color;
    }

    //getShape/getOrientation : might need some enum to describe shapes as one piece has 8 shapes according to its rotation.
    // Each enum should be a 2D array represents x-axis and y-axis.
    //Position
}
