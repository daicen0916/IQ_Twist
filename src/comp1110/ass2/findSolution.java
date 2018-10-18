package comp1110.ass2;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class findSolution {
    static ArrayList<String> firstPieceList= getFirstPiece();
    static int RowNumber = firstPieceList.size();
    static HashMap<Integer,DLNode> NodeNet = MakeAMatrix();
    static Integer HeadKey= -99;
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
        Collections.sort(result);
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
        for(int column=0;column<40;column++){
            int key = -100-column;
            DLNode columnHead= new DLNode(-1,column);
            NodeMatrix.put(key,columnHead);
        }
        int HeadKey = -99;
        DLNode Head = new DLNode(-1,-1);
        NodeMatrix.put(HeadKey,Head);


    for (int column=-1;column<40;column++){
        Integer key = -100-column;
        switch (column){
            case -1:
                NodeMatrix.get(key).Left=NodeMatrix.get(-100-39);
                NodeMatrix.get(key).Right=NodeMatrix.get(-100-1);
                continue;
            case 39:
                NodeMatrix.get(key).Right = NodeMatrix.get(-99);
                NodeMatrix.get(key).Left=NodeMatrix.get(-100-(column-1));
                NodeMatrix.get(key).Up=NodeMatrix.get(100*(RowNumber-1)+column);
                NodeMatrix.get(key).Down=NodeMatrix.get(column);
                continue;
            default:
                NodeMatrix.get(key).Left=NodeMatrix.get(-100-(column-1));
                NodeMatrix.get(key).Right=NodeMatrix.get(-100-(column+1));
                NodeMatrix.get(key).Up=NodeMatrix.get(100*(RowNumber-1)+column);
                NodeMatrix.get(key).Down=NodeMatrix.get(column);
                continue;
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
                NodeMatrix.get(key).Left=(NodeMatrix.get(leftkey));
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
                int upkey=-100-column;
                NodeMatrix.get(key).Up=NodeMatrix.get(upkey);
            }
            if(row==RowNumber-1){
                int downkey=-100-column;
                int upkey=(RowNumber-2)*100+column;
                NodeMatrix.get(key).Up=NodeMatrix.get(upkey);
                NodeMatrix.get(key).Down=NodeMatrix.get(downkey);
            }
            if (row<RowNumber-1&&row>0&&column>0&&column<39){
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

    //remove the nodes which are not filled
    for(Integer x=0;x<100*RowNumber;x++){
        if( NodeMatrix.containsKey(x)){
            if (!NodeMatrix.get(x).filled){
                NodeMatrix.get(x).Remove();
                NodeMatrix.remove(x);
            }
        }
    }
    return NodeMatrix;
}

static public void removeColumn(int column){
        for (Integer key =-100; key<100*(RowNumber-1)+40;key++){
            if (key!=-99 && NodeNet.containsKey(key)&&Math.abs(key)%100==column){
                NodeNet.get(key).Remove();
            }
        }
}

static public void RecoverColumn(int column){
        for (Integer key =-100; key<100*(RowNumber-1)+40;key++){
            if (key!=-99 && NodeNet.containsKey(key)&&Math.abs(key)%100==column){
                NodeNet.get(key).Recover();
            }
        }
    }
static public void RemoveRow(int row){
        for (Integer key =0; key<100*(RowNumber-1)+40;key++){
            if (NodeNet.containsKey(key)&&(int)(key/100)==row){
                NodeNet.get(key).Remove();
            }
        }
    }

static public void RecoverRow(int row){
   for (Integer key =0; key<100*(RowNumber-1)+40;key++){
        if (NodeNet.containsKey(key)&&(int)(key/100)==row){
            NodeNet.get(key).Recover();
        }
   }
}


    public static void main(String[] args) {
        RemoveRow(12);
        RecoverRow(12);
    }
}
