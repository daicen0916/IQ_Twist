package comp1110.ass2;

import java.lang.reflect.Array;
import java.util.*;

/**
 * This class provides the text interface for the Twist Game
 * <p>
 * The game is based directly on Smart Games' IQ-Twist game
 * (http://www.smartgames.eu/en/smartgames/iq-twist)
 */
public class TwistGame {

  /**
   * Determine whether a piece or peg placement is well-formed according to the following:
   * - it consists of exactly four characters
   * - the first character is in the range a .. l (pieces and pegs)
   * - the second character is in the range 1 .. 8 (columns)
   * - the third character is in the range A .. D (rows)
   * - the fourth character is in the range 0 .. 7 (if a piece) or is 0 (if a peg)
   *
   * @param piecePlacement A string describing a single piece or peg placement
   * @return True if the placement is well-formed
   */
  public static boolean isPlacementWellFormed(String piecePlacement) {
      char[] a = piecePlacement.toCharArray();
      int checks = 0;
    if (a.length==4)
    {
      checks++;
    }
    else
    {
      return false;
    }
    if(a[0]>='a' && a[0]<='l')
    {
      checks++;
    }
    if(a[1]>='1' && a[1]<='8')
    {
      checks++;
    }
    if(a[2]>='A' && a[2]<='D')
    {
      checks++;
    }
    if((a[0]>='i' && a[0]<='l' && a[3]=='0') ||(a[0]>='a' && a[0]<='h' && a[3]>='0' && a[3]<='7'))
    {
      checks++;
    }
    if(checks==5)
      {
        return true;
      }
    else
    return false;

  }
    //TASK 2 DONE BY PRANAV RAWAT (U6637058)

  /**
   * Determine whether a placement string is well-formed:
   * - it consists of exactly N four-character piece placements (where N = 1 .. 15);
   * - each piece or peg placement is well-formed
   * - each piece or peg placement occurs in the correct alphabetical order (duplicate pegs can be in either order)
   * - no piece or red peg appears more than once in the placement
   * - no green, blue or yellow peg appears more than twice in the placement
   *
   * @param placement A string describing a placement of one or more pieces and pegs
   * @return True if the placement is well-formed
   */
  public static boolean isPlacementStringWellFormed(String placement) {
    // FIXME Task 3: determine whether a placement is well-formed
    // check if the string consists of exactly N 4-char placement
    if(placement.length()%4!=0){
      return false;
    }
    if(placement.length()/4<1||placement.length()/4>15){
      return false;
    }
    // split the long placement string into a string array, each element contains 4 char.
    String[] placementList =new String[placement.length()/4];
    for(int index=0;index<placement.length()/4;index++){
        placementList[index] = placement.substring(4*index,4*index+4);
    }
    //check whether each piece or peg placement is well-formed
    for(String x:placementList){
      if(isPlacementWellFormed(x)==false){
        return false;
      }
    }
    //build a char array, elements are the head char of the placement.
    char[] HeadChar = new char[placement.length()/4];
    for (int i =0;i<HeadChar.length;i++){
      HeadChar[i]=placementList[i].charAt(0);
    }
    //check if the placements are in correct order
    for(int i=0;i<HeadChar.length-1;i++){
      if(HeadChar[i]>HeadChar[i+1]){
        return false;
      }
    }
    //check if the piece and red peg only exist 0 or 1 time.
    for(char x='a';x<='i';x++){
      int count =0;
      for(char y:HeadChar){
        if(y==x){
          count++;
        }
      }
      if(count>1){
        return false;
      }
    }
    //check no green/blue/yellow pegs appear more than twice
    for(char x='i';x<='l';x++){
      int count =0;
      for(char y:HeadChar){
        if(y==x){
          count++;
        }
      }
      if(count>2){
        return false;
      }
    }
    return true;
  }

  /**
   * Determine whether a placement string is valid.  To be valid, the placement
   * string must be well-formed and each piece placement must be a valid placement
   * according to the rules of the game.
   * - pieces must be entirely on the board
   * - pieces must not overlap each other
   * - pieces may only overlap pegs when the a) peg is of the same color and b) the
   *   point of overlap in the piece is a hole.
   *
   * @param placement A placement sequence string
   * @return True if the placement sequence is valid
   */
  public static boolean isPlacementStringValid(String placement) {
    if(isPlacementStringWellFormed(placement)!=true){
      return false;
    }
    // Build a Nodes[][] represents the board (contains 32 locations)
    Nodes[][] Board = new Nodes[4][8];
    //Split the placement string into a String array. Each element is a 4 char placement string
    String[] placementArray= new String[placement.length()/4];
    for(int i =0;i<placement.length()/4;i++){
      placementArray[i]=placement.substring(4*i,4*i+4);
    }
    for(int i =0;i<placementArray.length;i++){
      char[] temp= placementArray[i].toCharArray();
      int row= temp[2]-'A';
      int column=temp[1]-'1';
      // if the string represents a piece, do the following things
      if(temp[0]>='a'&&temp[0]<='h'){
        Pieces pieces = new Pieces(temp[0],temp[3]-'0');
        for(int m =0;m<pieces.height;m++){
          for(int n=0;n<pieces.width;n++){
            if(pieces.points[m][n]==null){
              continue;
            }
            if((row+m)>3||(column+n)>7){
              return false;
            }
            if(Board[row+m][column+n]==null){
              Board[row+m][column+n]= new Nodes(null,pieces.points[m][n]);
            }else if(Board[row+m][column+n].point!=null){
              if(pieces.points[m][n]!=null){
                return false;
              }
            }
          }
        }
      }

      if(temp[0]>='i'){
        Pegs peg = new Pegs(temp[0]);
        if(Board[row][column]==null){
          Board[row][column]=new Nodes(peg,null);
        }else if (Board[row][column].peg!=null){
          return false;
        }else if (Board[row][column].point!=null){
          if(Board[row][column].point.color!=peg.color||Board[row][column].point.hole==false){
            return false;
          }else{
            Board[row][column]=new Nodes(peg,Board[row][column].point);
          }
        }
      }

    }
    // FIXME Task 5: determine whether a placement string is valid
    return true;
  }

  /**
   * Given a string describing a placement of pieces and pegs, return a set
   * of all possible next viable piece placements.   To be viable, a piece
   * placement must be a valid placement of a single piece.  The piece must
   * not have already been placed (ie not already in the placement string),
   * and its placement must be valid.   If there are no valid piece placements
   * for the given placement string, return null.
   *
   * When symmetric placements of the same piece are viable, only the placement
   * with the lowest rotation should be included in the set.
   *
   * @param placement A valid placement string (comprised of peg and piece placements)
   * @return An set of viable piece placements, or null if there are none.
   */
  public static Set<String> getViablePiecePlacements(String placement) {
      if(isPlacementStringValid(placement)==false){
          return null;
      }
      ArrayList<String> GivenPlacement = new ArrayList<>();
      ArrayList<Character> head=new ArrayList<>();
      Set<String> viablePiecePlacement=new HashSet<>();
      // a set of all viable piece placement, it may contains redundant elements.
      //split given placement string into an array list,and assemble the head char to an array list
      for (int i=0;i<placement.length()/4;i++){
          String subPlacement= placement.substring(4*i,4*i+4);
          GivenPlacement.add(subPlacement);
          head.add(subPlacement.charAt(0));
      }
      //find all the viable piece placement string and put them in a hash set;
      for(char s0='a';s0<='h';s0++){
          if(head.contains(s0)){
              continue;
          }
          for(char s1='0';s1<='7';s1++){
              for(char s2='A';s2<='D';s2++){
                  for (char s3='0';s3<='7';s3++){
                      ArrayList<String> tempPlacement= new ArrayList<>(GivenPlacement);
                      String piecePlacement= ""+s0+s1+s2+s3;
                      tempPlacement.add(piecePlacement);
                      Collections.sort(tempPlacement);
                      String finalPlacement="";
                      for(String s:tempPlacement){
                          finalPlacement=finalPlacement+s;
                      }
                      if(isPlacementStringValid(finalPlacement)){
                          viablePiecePlacement.add(piecePlacement);
                      }
                  }
              }
          }
      }
      //remove the redundant element from the result hash set
      Set<String> result= new HashSet<>(viablePiecePlacement);
      for(String x:viablePiecePlacement){
          char[] x1=x.toCharArray();
          for(String y:viablePiecePlacement){
              char[] y1= y.toCharArray();
              // the redundant only appears on some piece, and the first 3 char
              //of the string must be same. and we need to remove the one with larger rotation.
              if(x1[3]<y1[3]){
                  if(x1[0]==y1[0]&&x1[1]==y1[1]&&x1[2]==y1[2]){
                      switch(x1[0]){
                          case 'b':
                              if(x1[3]=='0'&&y1[3]=='2'){
                                  result.remove(y);}
                              else if (x1[3]=='1'&&y1[3]=='3'){
                                  result.remove(y);}
                              else if (x1[3]=='4'&&y1[3]=='6'){
                                  result.remove(y);}
                              else if (x1[3]=='5'&&y1[3]=='7'){
                                  result.remove(y);}
                              continue;
                          case 'c':
                              if((y1[3]>'3')||(x1[3]=='0'&&y1[3]=='2')||(x1[3]=='1'&&y1[3]=='3'))
                              {result.remove(y);}
                              continue;
                          case 'e':
                              if(x1[3]=='0'&&y1[3]=='7'){
                                  result.remove(y);}
                              else if (x1[3]=='1'&&y1[3]=='4'){
                                  result.remove(y);}
                              else if (x1[3]=='2'&&y1[3]=='5'){
                                  result.remove(y);}
                              else if (x1[3]=='3'&&y1[3]=='6'){
                                  result.remove(y);}
                              continue;
                          case 'f':
                              if(x1[3]=='0'&&y1[3]=='6'){
                                  result.remove(y);}
                              else if (x1[3]=='1'&&y1[3]=='7'){
                                  result.remove(y);}
                              else if (x1[3]=='2'&&y1[3]=='4'){
                                  result.remove(y);}
                              else if (x1[3]=='3'&&y1[3]=='5'){
                                  result.remove(y);}
                              continue;
                          case 'h' :
                              if((y1[3]>'3')||(x1[3]=='0'&&y1[3]=='2')||(x1[3]=='1'&&y1[3]=='3'))
                              {result.remove(y);}
                              continue;
                      }
                  }
              }
          }
      }
      //if there is no valid next piece, the is empty, but it doesn't equal to null.
      if(result.isEmpty()){
          return null;
      }
    // FIXME Task 6: determine the set of valid next piece placements
    return result;
  }



  /**
   * Return an array of all unique solutions for a given starting placement.
   *
   * Each solution should be a 32-character string giving the placement sequence
   * of all eight pieces, given the starting placement.
   *
   * The set of solutions should not include any symmetric piece placements.
   *
   * In the IQ-Twist game, valid challenges can have only one solution, but
   * other starting placements that are not valid challenges may have more
   * than one solution.  The most obvious example is the unconstrained board,
   * which has very many solutions.
   *
   * @param placement A valid piece placement string.
   * @return An array of strings, each 32-characters long, describing a unique
   * unordered solution to the game given the starting point provided by placement.
   */
  public static String[] getSolutions(String placement) {
    // FIXME Task 9: determine all solutions to the game, given a particular starting placement
    return null;
  }

  //public static void InitializeBoard() //get a objective then initialize the board


  // public static void ErrorReport{}
  //if player place a piece overlap another existing piece or a peg is already in the location and the peg's color and the piece's color are not matched.
  //This method will throw a warning to the player that his/her behaviour is illegal.

}
