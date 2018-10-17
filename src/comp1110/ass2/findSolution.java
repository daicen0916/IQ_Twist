package comp1110.ass2;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class findSolution {
    static ArrayList<String> firstPieceList= getFirstPiece();
    static int RowNumber = firstPieceList.size();
    public static ArrayList<String> getFirstPiece(){
        HashSet<String> firstPieceSet = new HashSet<>();
        for(int i =0;i<=7737;i++){
            int[] num = new int[4];
            num[3]= i%10;
            num[2]= (i/10)%10;
            num[1]= (i/100)%10;
            num[0]= i/1000;
            char[] status = new char[4];
            status[0]= (char)('a'+num[0]);
            status[1]= (char)('1'+num[1]);
            status[2]= (char)('A'+num[2]);
            status[3]= (char)('0'+num[3]);
            String piecePlacement = String.valueOf(status);
            if(status[0]<='h'&&status[1]<='8'&&status[2]<='D'&&status[3]<='7'){
                if (TwistGame.isPlacementStringValid(piecePlacement)){
                firstPieceSet.add(piecePlacement);}
            }

        }
        HashSet<String> tempresult= (HashSet<String>) TwistGame.removeRedundant(firstPieceSet);
        ArrayList<String> result = new ArrayList<>();
        for(String s: tempresult){
            result.add(s);
        }
        return result;
    }
    public static int[] setLocationArray(String placement){
        char[] status = placement.toCharArray();
        int rotate = status[3]-'0';
        char id = status[0];
        int startColumn = status[1]-'1';
        int startRow = status[2]-'A';
        Pieces piece = new Pieces(id,rotate);
        Point[][] pieceArray = piece.points;
        ArrayList<Integer> LocationList= new ArrayList<>();
        for (int row =0;row<piece.height;row++){
            for (int column=0; column<piece.width;column++){
                if(pieceArray[row][column]!=null){
                int location = (startRow+row)*8+(startColumn+column);
                LocationList.add(location);}
            }
        }
        int idLocation = 32+(id-'a');
        LocationList.add(idLocation);
        int[] locationArray = new int[LocationList.size()] ;
        for (Integer element : LocationList){
            int location = element;
            locationArray[LocationList.indexOf(element)] = location;
        }
        return locationArray;
    }

static class DLNode{
        DLNode Up, Down, Left,Right;
        int Row,Column;
        String status;
        boolean filled;
        DLNode(int Row, int Column){
            this.Row=Row;
            this.Column = Column;
            this.Up=this;
            this.Down=this;
            this.Right=this;
            this.Left=this;
            this.filled= false;
        }
        void SetUp(DLNode Up){this.Up=Up;}
        void SetDown(DLNode Down){this.Down=Down;}
        void SetLeft(DLNode Left) {this.Left=Left;}
        void SetRight(DLNode Right){this.Right=Right;}
        void SetStatus(String staus){this.status = staus;}
        void SetFilled (boolean filled) {this.filled=filled;}
        void Remove(){
            this.Up.Down=this.Down;
            this.Down.Up= this.Up;
            this.Left.Right=this.Right;
            this.Right.Left=this.Left;
        }
        void Recover(){
            this.Left.Right=this;
            this.Right.Left=this;
            this.Up.Down= this;
            this.Down.Up=this;
        }
}
static public HashMap<Integer,DLNode> MakeAMatrix(){
        HashMap<Integer,DLNode> NodeMatrix =new HashMap<>();
        // make a node net
        for(int row=0;row<firstPieceList.size();row++){
            for (int column=0;column<40;column++){
                int key = row*100+column;
                DLNode value = new DLNode(row,column);
                NodeMatrix.put(key,value);
            }
        }

    for(int row=0;row<firstPieceList.size();row++){
        for (int column =0;column<40;column++){
            int key = row*100+column;
            if (column==0){
                int leftkey = row*100+39;
                boolean f = NodeMatrix.containsKey(leftkey);
                DLNode x = NodeMatrix.get(key);
                int rightkey= row*100+1;
                NodeMatrix.get(key).SetLeft(NodeMatrix.get(leftkey));
                NodeMatrix.get(key).Right=NodeMatrix.get(rightkey);
            }
            if(column==39){
                int rightkey=row*100;
                int leftkey=row*100+38;
                NodeMatrix.get(key).Left=NodeMatrix.get(leftkey);
                NodeMatrix.get(key).Right=NodeMatrix.get(rightkey);
            }
            if (row==0){
                int downkey=100+column;
                NodeMatrix.get(key).Down=NodeMatrix.get(downkey);
                int upkey=(RowNumber-1)*100+column;
                NodeMatrix.get(key).Up=NodeMatrix.get(upkey);
            }
            if(row==RowNumber-1){
                int downkey=column;
                int upkey=(RowNumber-2)*100+column;
                NodeMatrix.get(key).Up=NodeMatrix.get(upkey);
                NodeMatrix.get(key).Down=NodeMatrix.get(downkey);
            }
            if (row<firstPieceList.size()-1&&row>0&&column>0&&column<39){
                int leftkey = row*100+column-1;
                int rightkey = row*100+column+1;
                int upkey=(row-1)*100+column;
                int downkey=(row+1)*100+column;
                NodeMatrix.get(key).Up=NodeMatrix.get(upkey);
                NodeMatrix.get(key).Down=NodeMatrix.get(downkey);
                NodeMatrix.get(key).Right=NodeMatrix.get(rightkey);
                NodeMatrix.get(key).Left=NodeMatrix.get(leftkey);
            }
        }
    }


    //set the filled node
    for(String s: firstPieceList){
        int[] filledArray = setLocationArray(s);
        int row = firstPieceList.indexOf(s);
        for(int location: filledArray){
            int key =row*100+location;
            NodeMatrix.get(key).filled=true;
            NodeMatrix.get(key).status=s;
        }
    }
    return NodeMatrix;
}

    public static void main(String[] args) {
        HashMap<Integer,DLNode> matrix= MakeAMatrix();
        //int x= RowNumber;
        System.out.println(1);
    }
}
