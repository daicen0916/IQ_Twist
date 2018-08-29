package comp1110.ass2;
public class Pieces {
   /* private String id;
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
        //return id+position+orientation (the result should be a string)*/
   char id;
   public Color color;
   public Point[][] points;
   int shapeCode;
   int rotate;
   int flip;
   int width;
   int height;
   Pieces(char id, int shapeCode){
       this.id=id;
       this.shapeCode=shapeCode;
       this.rotate=shapeCode%4;
       this.flip=shapeCode/4;
       this.points= setRotateArray(this.id);
       getColor();
       this.height=this.points.length;
       this.width=this.points[0].length;
   }
    void getColor(){
        Color color;
        if(this.id=='a'||this.id=='b'){
            this.color=Color.Red;
        }else if(this.id=='c'||this.id=='d'){
            this.color=Color.Blue;
        }else if(this.id=='e'||this.id=='f'){
            this.color=Color.Green;
        }else if(this.id=='g'||this.id=='h'){
            this.color=Color.Yellow;
        }
    }
    Point[][] setInitialArray(char id){
       Point[][] initial= new Point[2][3];
       switch (id){
           case 'a':
               initial[0][0]=new Point(Color.Red,true);
               initial[0][1]=new Point(Color.Red,false);
               initial[0][2]=new Point(Color.Red,true);
               initial[1][0]=null;
               initial[1][1]=null;
               initial[1][2]=new Point(Color.Red,false);
               break;
           case 'b':
               initial[0][0]=new Point(Color.Red,false);
               initial[0][1]=new Point(Color.Red,false);
               initial[0][2]=null;
               initial[1][0]=null;
               initial[1][1]=new Point(Color.Red,true);
               initial[1][2]=new Point(Color.Red,false);
               break;
           case 'c':
               initial=new Point[1][4];
               initial[0][0]=new Point(Color.Blue,false);
               initial[0][1]=new Point(Color.Blue,true);
               initial[0][2]=new Point(Color.Blue,false);
               initial[0][3]=new Point(Color.Blue,false);
               break;
           case 'd':
               initial[0][0]=new Point(Color.Blue,false);
               initial[0][1]=new Point(Color.Blue,false);
               initial[0][2]=new Point(Color.Blue,false);
               initial[1][0]=null;
               initial[1][1]=new Point(Color.Blue,true);
               initial[1][2]=new Point(Color.Blue,true);
               break;
           case 'e':
               initial=new Point[2][2];
               initial[0][0]=new Point(Color.Green,false);
               initial[0][1]=new Point(Color.Green,true);
               initial[1][0]=null;
               initial[1][1]=new Point(Color.Green,true);
               break;
           case 'f':
               initial[0][0]=new Point(Color.Green,false);
               initial[0][1]=new Point(Color.Green,false);
               initial[0][2]=new Point(Color.Green,true);
               initial[1][0]=null;
               initial[1][1]=new Point(Color.Green,true);
               initial[1][2]=null;
               break;
           case 'g':
               initial=new Point[3][3];
               initial[0][0]=new Point(Color.Yellow,true);
               initial[0][1]=null;
               initial[0][2]=null;
               initial[1][0]=new Point(Color.Yellow,true);
               initial[1][1]=new Point(Color.Yellow,false);
               initial[1][2]=new Point(Color.Yellow,false);
               initial[2][0]=null;
               initial[2][1]=new Point(Color.Yellow,true);
               initial[2][2]=null;
               break;
           case 'h':
               initial=new Point[1][3];
               initial[0][0]=new Point(Color.Yellow,true);
               initial[0][1]=new Point(Color.Yellow,false);
               initial[0][2]=new Point(Color.Yellow,false);
               break;
       }
       return initial;
    }
    Point[][] setRotateArray(char id){
       Point[][] flipArray = setFlipArray(id);
       Point[][] rotateArray=null;
       int row= flipArray[0].length;
       int column=flipArray.length;
       //= new Point[initialArray[0].length][initialArray.length];
       switch (this.rotate){
           case 0:
               rotateArray=flipArray;
               break;
           case 1:
               rotateArray= new Point[row][column];
               for(int i =0;i<row;i++){
                   for(int j=0;j<column;j++){
                       rotateArray[i][j]=flipArray[column-1-j][i];
                   }
               }
               //TODO 1 rotate 90 degree clockwise;
               break;
           case 2:
               rotateArray= new Point[flipArray.length][flipArray[0].length];
               for(int i =0;i<rotateArray.length;i++){
                   for(int j=0;j<rotateArray[0].length;j++){
                       rotateArray[i][j]=flipArray[rotateArray.length-1-i][rotateArray[0].length-1-j];
                   }
               }
               // TODO 2 rotate 180 degree
               break;
           case 3:
               rotateArray= new Point[row][column];
               for(int i =0;i<row;i++){
                   for(int j=0;j<column;j++){
                       rotateArray[i][j]=flipArray[j][row-1-i];
                   }
               }
               // TODO 3 rotate 270 degree
               break;
       }
       return rotateArray;

    }
    Point[][] setFlipArray(char id){
        Point[][] initialArray= setInitialArray(id);
        Point[][] flipArray= new Point[initialArray.length][initialArray[0].length];
        if(this.flip==1){
            for(int i=0;i<flipArray.length;i++){
                flipArray[i]=initialArray[initialArray.length-1-i];
            }

        }else{
            flipArray=initialArray;
        }
        return flipArray;
    }


}
