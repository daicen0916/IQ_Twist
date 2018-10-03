package comp1110.ass2.gui;

import comp1110.ass2.Pieces;
import comp1110.ass2.TwistGame;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;


public class Board extends Application {
    /* board layout */
    private static final int SQUARE_SIZE = 60;
    private static final int VIEWER_WIDTH = 933;
    private static final int VIEWER_HEIGHT = 700;
    private static final int PIECE_PANEL_X=30;
    private static final int PIECE_PANEL_Y=383;
    private static final int MAIN_PANEL_X=0;
    private static final int MAIN_PANEL_Y=0;
    private static final int MAIN_PANEL_WIDTH=480;
    private static final int MAIN_PANEL_HEIGHT=240;
    private static final String URI_BASE = "assets/";

    private final Group root = new Group();
    private final Group controls = new Group();

    private final Group img=new Group();
    private final Group pieces=new Group();

    public class Item extends ImageView {
        char id;

        /**
         * Construct a particular playing piece
         *
         * @param id The letter representing the tile to be created.
         */
        Item(char id) {
            this.id=id;
            if (id > 'h') {
                throw new IllegalArgumentException("Bad piece: \"" + id + "\"");
            }
            Image pic = new Image(Viewer.class.getResource(URI_BASE + id + ".png").toString());
            setImage(pic);
            setFitWidth(SQUARE_SIZE*pic.getWidth()/100);
            setFitHeight(SQUARE_SIZE*pic.getHeight()/100);
        }

    }
    class DraggablePiece extends Item{
        int homeX, homeY;           // the position in the window where the mask should be when not on the board
        double mouseX, mouseY;
        int rotate;
        /**
         * Construct a particular playing piece
         *
         * @param id The letter representing the tile to be created.
         */
        DraggablePiece(char id) {
            super(id);
            int[] initX={0,210,420,690,0,150,360,570};
            int[] initY={0,125};
            rotate=0;
            homeX = PIECE_PANEL_X+initX[id-'a'];
            setLayoutX(homeX);
            homeY = PIECE_PANEL_Y+initY[(id-'a')/4];
            setLayoutY(homeY);
            /* event handlers */
            setOnScroll(event -> {            // scroll to change orientation
                //hideCompletion();
                if(!onBoard()){
                rotate=rotate+90;
                setRotate(rotate % 360);
                if((rotate/360)%2==1){
                    setScaleY(-1);
                }else {setScaleY(1);}
                event.consume();}
            });
            setOnMousePressed(event -> {      // mouse press indicates begin of drag
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
            });
            setOnMouseDragged(event -> {      // mouse is being dragged
               // hideCompletion();
                toFront();
                double movementX = event.getSceneX() - mouseX;
                double movementY = event.getSceneY() - mouseY;
                setLayoutX(getLayoutX() + movementX);
                setLayoutY(getLayoutY() + movementY);
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                event.consume();
            });
            setOnMouseReleased(event -> {     // drag is complete
                snapToGrid();
            });

        }
        private int[] snapShift(int roll){
            int rotate=(((roll)%360)/90)%4;
            int[] shift = {0,0}; //shift[0] is x; shift[1] is y
            if(this.id=='a'||this.id=='b'||this.id=='d'||this.id=='f'){
                if(rotate==1||rotate==3){
                    shift[0]=SQUARE_SIZE/2;
                    shift[1]=-SQUARE_SIZE/2;
                }
            }else if(this.id=='c'){
                if(rotate==1||rotate==3){
                    shift[0]=(int)(1.5*SQUARE_SIZE);
                    shift[1]=(int)(-1.5*SQUARE_SIZE);

                }
            }else if(this.id=='h'){
                if(rotate==1||rotate==3){
                    shift[0]=SQUARE_SIZE;
                    shift[1]=-SQUARE_SIZE;
                }
            }
            return shift;
        }
        private void snapToGrid() {
            if(onBoard()){
                double viewer_x= getLayoutX();
                double viewer_y=getLayoutY();
                int[] shift= snapShift((int)getRotate());
                double board_x=viewer_x+shift[0]-MAIN_PANEL_X;
                double board_y=viewer_y+shift[1]-MAIN_PANEL_Y;
                System.out.println("board x="+board_x);
                System.out.println("board y="+board_y);
                int dif= SQUARE_SIZE/3;
                int column=0;
                int row=0;
                if(board_x%SQUARE_SIZE>2*SQUARE_SIZE/3){
                    column=(int)(board_x/SQUARE_SIZE)+1;
                }
                else {
                    column=(int)(Math.abs(board_x)/SQUARE_SIZE);
                }
                if (board_y%SQUARE_SIZE>2*SQUARE_SIZE/3){
                    row=(int)(board_y/SQUARE_SIZE)+1;
                }else {
                    row=(int)(Math.abs(board_y)/SQUARE_SIZE);
                }
                double real_x=column*SQUARE_SIZE+MAIN_PANEL_X;
                double real_y= row*SQUARE_SIZE+MAIN_PANEL_Y;
                viewer_x= real_x-shift[0];
                viewer_y=real_y-shift[1];
                setLayoutX(viewer_x);
                setLayoutY(viewer_y);
                int rotate_status= ((((int)getRotate())%360)/90)%4;
                String piecePlacement=makePiecePlacement(this.id,column,row,rotate_status);
                System.out.println(row);
                System.out.println(piecePlacement);
                //TwistGame.generatePlacement(piecePlacement,);
            }else {
                snapToHome();
                //remember to remove the string
            }
        }
        private String makePiecePlacement(char id, int column, int row, int rotate){
            char[] piece= new char[4];
            piece[0]=id;
            piece[1]=(char)('1'+column);
            piece[2]=(char)('A'+row);
            piece[3]=(char)('0'+rotate);
            String pieceplacement= new String(piece);
            return pieceplacement;
        }
        private void updatePlacement(){

        }

        /**
         * Snap the mask to its home position (if it is not on the grid)
         */
        private void snapToHome() {
            setLayoutX(homeX);
            setLayoutY(homeY);
            rotate=0;
            setRotate(rotate);
        }
        /**
         * @return true if the mask is on the board
         */
        private boolean onBoard() {
            int left_margin=MAIN_PANEL_X-SQUARE_SIZE/3;
            int right_margin=MAIN_PANEL_X+MAIN_PANEL_WIDTH+SQUARE_SIZE/3;
            int top_margin=MAIN_PANEL_Y-SQUARE_SIZE/3;
            int bottom_margin=MAIN_PANEL_Y+MAIN_PANEL_HEIGHT+SQUARE_SIZE/3;
            double viewer_x= getLayoutX();
            double viewer_y=getLayoutY();
            int[] shift= snapShift((int)getRotate());
            double real_x=viewer_x+shift[0];
            double real_y=viewer_y+shift[1];
            double right_location=real_x+this.getFitWidth();
            double bottom_location=real_y+this.getFitHeight();
            if(real_x<left_margin||real_y<top_margin||right_location>right_margin||bottom_location>bottom_margin){
                return false;
            }else {
                return true;
            }
        }

    }



    public ArrayList<String> makePlacementList(String placement){
        ArrayList<String> placementList= new ArrayList<>();
        for(int i =0;i<placement.length()/4;i++){
            placementList.add( placement.substring(4*i,4*i+4));
        }
        return placementList;
    }

    /**
     * Draw a placement in the window, removing any previously drawn one
     *
     * @param placement  A valid placement string
     */
    public void makeInitialPlacement(String placement) {
        root.getChildren().remove(img);
        img.getChildren().clear();
        // split the placement string into a string array;
        ArrayList<String> placementList= makePlacementList(placement);
        for(String s:placementList){
            char[]a = s.toCharArray();
            char id = a[0];
            int x= (int)(a[1]-'1');
            int y =(int)(a[2]-'A');
            Item item =new Item(id);
            //check and set flip
            if((a[3]-'0')/4!=0){
                item.setScaleY(-1);
            }
            int itemRotate = (a[3]-'0')%4;
            item.setRotate(itemRotate*90);
            int[] shift=Viewer.Rotate(s);
            item.setLayoutX(MAIN_PANEL_X+x*SQUARE_SIZE+shift[0]);
            item.setLayoutY(MAIN_PANEL_Y+y*SQUARE_SIZE+shift[1]);
            img.getChildren().add(item);
        }
        root.getChildren().add(img);
    }
    public void setDraggablePiece(String placement){
        root.getChildren().remove(pieces);
        pieces.getChildren().clear();
        ArrayList<String> placementList= makePlacementList(placement);
        ArrayList<Character> head =new ArrayList<>();
        for(String s: placementList){
            head.add(s.charAt(0));
        }
        for(char c='a';c<='h';c++){
            if (head.indexOf(c)<0){
                DraggablePiece piece= new DraggablePiece(c);
                pieces.getChildren().add(piece);
            }
        }
        root.getChildren().add(pieces);

    }


    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
        Button button = new Button("New Game");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String InitialPlacement = "a7A7b6A7d2A6";
                String CurrentPlacement = InitialPlacement;
                makeInitialPlacement(InitialPlacement);
                setDraggablePiece(InitialPlacement);

            }
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(button);
        hb.setSpacing(10);
        hb.setLayoutX(130);
        hb.setLayoutY(VIEWER_HEIGHT - 50);
        controls.getChildren().add(hb);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("IQ-TWIST");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);
        root.getChildren().add(controls);
        makeControls();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    // FIXME Task 7: Implement a basic playable Twist Game in JavaFX that only allows pieces to be placed in valid places
    // FIXME Task 8: Implement starting placements
    public String Initialgamestage()
    {
        String[] Initialplacement= {"a7A7b6A7","i5A0","d1A6j1C0","c1A3d2A6","l4B0l5C0","j1C0k3C0","h6D0i6B0j2B0","l5C0","b6A7i5A0","k1b0k6B0l5A0l3C0",
                "g6B7h4B0k3D0","j4B0k8B0k5D0","c1A3D2A6","d7C7j4D0"};
        Random r=new Random();
        int index=r.nextInt(15);
        return (Initialplacement[index]);
    }
    // FIXME Task 10: Implement hints
    public String Hints(String placement)
    {
        String []Solution=TwistGame.getSolutions(placement);
        Random r=new Random();
        int index=r.nextInt(15);
        String Solutionstring=Solution[index];
        String nextpieces=TwistGame.remove(placement,Solutionstring);
        Random p=new Random();
        int index1=r.nextInt(nextpieces.length()/4);
        String hint=nextpieces.substring(4*index1,4*index1+4);
        return hint;

    }


}
