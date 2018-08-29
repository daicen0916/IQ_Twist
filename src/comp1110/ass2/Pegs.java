package comp1110.ass2;

import gittest.C;

public class Pegs {
    // To store information about a peg

    char id;
    Color color;
   // String position;
   // String PegPlacementString;

  //  public String getPegPosition()
  //  {
  //      return position;
  //  }
    Pegs(char id){
        this.id=id;
        this.color=getPegColor();
    }
    public Color getPegColor()
    {   Color color=null;
        switch(id){
            case 'i':
                color= Color.Red;
                break;
            case 'j':
                color=Color.Blue;
                break;
            case 'k':
                color=Color.Green;
                break;
            case 'l':
                color=Color.Yellow;
                break;

        }
        return color;
    }
    //public void SetPegPlacementString(){
    //    this.PegPlacementString=this.color+this.position+'0';

    //}


}
