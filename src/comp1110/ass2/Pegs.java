package comp1110.ass2;

public class Pegs {
    // To store information about a peg

    private int id;
    private char color;
    int position;
    // Since player don't need to put other pegs on the board during the game, I'm still considering whether we need to get
    //the pegs' position. It is set when we initialize the game board. Maybe we don't need to operate the pegs anymore. But for
    // build the peg placement string, the position maybe necessary. As a result we might keep it but change it in to a String?
    //Another question: is the id still needed?
    String PegPlacementString;

    public int getPegPosition()
    {
        return position;
    }
    public char getPegColor()
    {
        return color;
    }
    //public void SetPegPlacementString(){
    //    this.PegPlacementString=this.color+this.position+'0';

    //}


}
