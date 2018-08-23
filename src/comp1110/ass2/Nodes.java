package comp1110.ass2;

public class Nodes {
    Pegs peg;
    Pieces piece;
    String state;//it should responds the statement of a certain location including whether it is occupied by a peg or/and a piece.
    //If occupied, the peg's color？ The pieces color? Is the circle filled or it's a hole?

    //As same as assignment 1, we might need a node class to define each of the 32 locations on the board.
  //Parameter: locationsate[]   or it can be a 2D array, the 2 indexes represent the (x,y) location, and the element is encoded(the encoding rule please find in the instruction on Gitlab), represents the piece’s id, shape, color and so on
    //Update(): when a piece is placed on board, update the location state;
    //Check(): when a piece is placed and the board is updated check if all the pieces are on board, if so check whether the solution is correct.

    //Methods:
    //Isoccupied(): it could judge whether the location is occupied by nothing/ a piece/ a piece and a peg/ two piece(overlap);
    // It might corporate with the update() and check()
}
