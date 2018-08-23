package comp1110.ass2;
public class Pieces {
    private String id;
    public char color; //Cen Dai: color could be used to check whether the piece matches the peg or board, so I changed it into public.
    //i->Red,j->Blue,k->Green,l->Yellow
    String position; //The same as pegposition, if the piece position should be a string for the editing convenience.
    int orientation;
    Rotation rotation; // Cen Dai
    Flipping flipping; // Cen Dai
    String[] shape;
    Pieces(){
        //It's a constructor to set up an Object Pieces. It contains some public members that can be called by other methods or classes
        // such as color, position and orientation. --By Cen Dai

    }

    public String getPiecePosition()
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
    public int getOrientation(Rotation rotation, Flipping flipping){
        int orientation=0;
        switch (rotation){
            case NORTH:
                orientation=0;
                break;
            case EAST:
                orientation=1;
                break;
            case SOUTH:
                orientation=2;
                break;
            case WEST:
                orientation=3;
                break;
        }
        switch (flipping){
            case Up:
                break;
            case Down:
                orientation=orientation+4;
        }
        return orientation;
    }

    //String[] getShape(String position,int orientation)
    //this method is to set up the different shapes of pieces. Shapes are determined by the position of the element(up-left of the piece)
    //as well as the orientation
    //String getPiecePlacementString(String position,char id,int orientation)
        //return id+position+orientation (the result should be a string)

}
