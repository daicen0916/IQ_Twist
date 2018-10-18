package comp1110.ass2;

public class Point {
    public Color color;
    public boolean hole;
    //int position;
    Point(Color color,boolean hole){
        this.color=color;
        this.hole=hole;
        //this.position=position;
    }
    public Color getColor(){
        return this.color;
    }
}
