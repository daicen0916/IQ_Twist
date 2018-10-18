package comp1110.ass2.gui;

import comp1110.ass2.Nodes;
import comp1110.ass2.Pieces;
import comp1110.ass2.TwistGame;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Random;

public class Board extends Application {
    private static final int SQUARE_SIZE = 60;
    private static final int VIEWER_WIDTH = 933;
    private static final int VIEWER_HEIGHT = 700;
    private static final int MAIN_PANEL_X=80;
    private static final int MAIN_PANEL_Y=110;
    private static final int MAIN_PANEL_WIDTH=480;
    private static final int MAIN_PANEL_HEIGHT=240;
    private static final int PIECE_PANEL_X=30;
    private static final int PIECE_PANEL_Y=370;
    private static final int PEG_PANEL_X=110;
    private static final int PEG_PANEL_Y=20;
    private static final int COLUMN_PANEL_X=80;
    private static final int COLUMN_PANEL_Y=80;
    private static final int ROW_PANEL_X=50;
    private static final int ROW_PANEL_Y=110;
    private static final int COLUMNS = 8;
    private static final int ROWS = 4;
    private static final String URI_BASE = "assets/";
    private final Group root = new Group();
    private final Group controls = new Group();
    private final Group img=new Group();
    private final Group pieces=new Group();
    private final Group VacantPegs = new Group();
    private final Group BackGround= new Group();
    private final Group Hint = new Group();
    private static String InitialPlacement="";
    private static String CurrentPlacement="";

    public class Item extends ImageView {
        char id;

        /**
         * Construct a particular playing piece
         *
         * @param id The letter representing the tile to be created.
         */
        Item(char id) {
            this.id=id;
            if (id > 'l') {
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
            int[] initX={0,210,440,690,0,150,360,560};
            int[] initY={20,20,50,0,180,180,130,180};
            this.rotate=0;
            homeX = PIECE_PANEL_X+initX[id-'a'];
            setLayoutX(homeX);
            homeY = PIECE_PANEL_Y+initY[(id-'a')];
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
                UpdateAndCheck();
            });

        }

        /**
         * @return return an int array, this array is used to
         * compensate the layout coordinate shift induced by the
         * rotate.
         */
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
        /**
         * If current piece is on board, snap it to the nearest
         * grid of the board. If not, snap it to its home place.
         */
        private void snapToGrid() {
            if(onBoard()){
                double viewer_x= getLayoutX();
                double viewer_y=getLayoutY();
                int[] shift= snapShift((int)getRotate());
                double board_x=viewer_x+shift[0]-MAIN_PANEL_X;
                double board_y=viewer_y+shift[1]-MAIN_PANEL_Y;
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
                int rotate_status=0;
                if(getScaleY()==1){
                    rotate_status=(int)getRotate()/90;
                }else if(getScaleY()==-1){
                    rotate_status=(int)getRotate()/90+4;
                }
                String piecePlacement=makePiecePlacement(this.id,column,row,rotate_status);
                removePiece();
                CurrentPlacement=TwistGame.generatePlacement(piecePlacement,CurrentPlacement);
            }else {
                snapToHome();
                removePiece();
            }
        }
        /**
         * Update the current placement string, if put a piece
         * out of the board.
         */
        private void removePiece(){
            int index=CurrentPlacement.indexOf(this.id);
            if(index>=0){
                CurrentPlacement=CurrentPlacement.substring(0,index)
                        +CurrentPlacement.substring(index+4);
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
        /**
         * Update the current placement string, after a piece is
         * put on the board. If the game is completed successfully,
         * give a congratulation message. If the placement is
         * illegal, give a illegal remind message.
         */
        private void UpdateAndCheck(){
            if(!TwistGame.isPlacementStringValid(CurrentPlacement)){
                showDialog("Sorry! It's an illegal movement");
                removePiece();
                snapToHome();
            }else if(CurrentPlacement.length()>=32&&CurrentPlacement.charAt(28)=='h'){
                showDialog("Congratulations! You complete this game!");
            }
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
         * @return true if the piece is on the board
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
            double right_location,bottom_location;
            if((int)getRotate()/90%2==1) {
                right_location = real_x + this.getFitHeight();
                bottom_location=real_y+this.getFitWidth();
            }
            else {
                right_location=real_x+this.getFitWidth();
                bottom_location=real_y+this.getFitHeight();
            }
            if(real_x<left_margin||real_y<top_margin||right_location>right_margin||bottom_location>bottom_margin){
                return false;
            }else {
                return true;
            }
        }

    }


    /**
     * make an array list for the input placement string, each element represents
     * a piece on board.
     */
    public ArrayList<String> makePlacementList(String placement){
        ArrayList<String> placementList= new ArrayList<>();
        for(int i =0;i<placement.length()/4;i++){
            placementList.add( placement.substring(4*i,4*i+4));
        }
        return placementList;
    }

    /**
     * Draw an initial placement in the window, removing any previously drawn one
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
    public int CountPegs(char id,String InitialPlacement){
        int count=0;
        for(int i=0;i<InitialPlacement.length()/4;i++){
            if(InitialPlacement.charAt(4*i)==id){
                count++;
            }
        }
        return count;

    }
    public void SetVacantPegs(String InitialPlacement){
        root.getChildren().remove(VacantPegs);
        VacantPegs.getChildren().clear();
        if(CountPegs('i',InitialPlacement)==0){
            Item PegI= new Item('i');
            PegI.setLayoutX(PEG_PANEL_X);
            PegI.setLayoutY(PEG_PANEL_Y);
            VacantPegs.getChildren().add(PegI);
        }
        for(char id='j';id<='l';id++){
            if(CountPegs(id,InitialPlacement)==0){
                Item Peg1= new Item(id);
                Item Peg2=new Item(id);
                Peg1.setLayoutX(PEG_PANEL_X+(2*(int)(id-'i')-1)*SQUARE_SIZE);
                Peg1.setLayoutY(PEG_PANEL_Y);
                Peg2.setLayoutX(PEG_PANEL_X+2*(int)(id-'i')*SQUARE_SIZE);
                Peg2.setLayoutY(PEG_PANEL_Y);
                VacantPegs.getChildren().add(Peg1);
                VacantPegs.getChildren().add(Peg2);
            }else if(CountPegs(id,InitialPlacement)==1){
                Item Peg= new Item(id);
                Peg.setLayoutX(PEG_PANEL_X+2*(int)(id-'i')*SQUARE_SIZE);
                Peg.setLayoutY(PEG_PANEL_Y);
                VacantPegs.getChildren().add(Peg);
            }
        }
        root.getChildren().add(VacantPegs);
    }

    /**
     * set up all the draggable piece for a certain beginning of the game.
     *
     * @param placement  A valid placement string
     */
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
    public void MakeHint(String HintPlacement){
        root.getChildren().remove(Hint);
        Hint.getChildren().clear();
        char[]a = HintPlacement.toCharArray();
        char id = a[0];
        int x= (int)(a[1]-'1');
        int y =(int)(a[2]-'A');
        Item Hintpiece =new Item(id);
        //check and set flip
        if((a[3]-'0')/4!=0){
            Hintpiece.setScaleY(-1);
        }
        int itemRotate = (a[3]-'0')%4;
        Hintpiece.setRotate(itemRotate*90);
        int[] shift=Viewer.Rotate(HintPlacement);
        Hintpiece.setLayoutX(MAIN_PANEL_X+x*SQUARE_SIZE+shift[0]);
        Hintpiece.setLayoutY(MAIN_PANEL_Y+y*SQUARE_SIZE+shift[1]);
        Hintpiece.setOpacity(0.75);
        Hint.getChildren().add(Hintpiece);
        root.getChildren().add(Hint);
    }


    /**
     * Create a New Game button to setup a new game.
     */
    private void makeControls() {
        ComboBox level = new ComboBox();
        level.getItems().add("Easy");
        level.getItems().add("Medium");
        level.getItems().add("Hard");
        level.setLayoutX(750);
        level.setLayoutY(80);
        Button newGame = new Button("New Game");
        //System.out.println(newGame.getLayoutX());
        newGame.setLayoutX(750);
        newGame.setLayoutY(150);
        newGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String difficulty = (String) level.getValue();
                InitialPlacement=setStart(Initialgamestage(),difficulty);
                CurrentPlacement = InitialPlacement;
                makeInitialPlacement(InitialPlacement);
                SetVacantPegs(InitialPlacement);
                setDraggablePiece(InitialPlacement);

            }
        });
        Button ResetGame =new Button("Reset");
        ResetGame.setLayoutX(750);
        ResetGame.setLayoutY(200);
        ResetGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CurrentPlacement=InitialPlacement;
//                makeInitialPlacement(InitialPlacement);
//                SetVacantPegs(InitialPlacement);
                setDraggablePiece(InitialPlacement);
                root.getChildren().remove(Hint);
                Hint.getChildren().clear();
            }
        });
        Button Hint =new Button("Hint");
        Hint.setLayoutX(750);
        Hint.setLayoutY(250);
        Hint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int pieceNumber =0;
                for(int i=0;i<CurrentPlacement.length()/4;i++){
                    if(CurrentPlacement.charAt(i*4)<='h'){
                        pieceNumber++;}
                }
                if (pieceNumber<4){
                    showDialog("Just try to put on "+(4-pieceNumber)+" pieces~");}
                else if(TwistGame.getSolutions(CurrentPlacement).length==0){
                    showDialog("Ooops! There are no solutions.");
                }
                else {
                String hints = Hints(CurrentPlacement);
                MakeHint(hints);}
            }
        });
        controls.getChildren().addAll(newGame,ResetGame,Hint,level);
    }
    private void makeBackGround(){
        BackGround.getChildren().clear();
        ImageView baseboard = new ImageView();
        ImageView pegboard = new ImageView();
        ImageView columnlable = new ImageView();
        ImageView rowlable = new ImageView();
        baseboard.setImage(new Image(Viewer.class.getResource(URI_BASE + "board" + ".png").toString()));
        pegboard.setImage(new Image(Viewer.class.getResource(URI_BASE + "pegboard" + ".png").toString()));
        columnlable.setImage(new Image(Viewer.class.getResource(URI_BASE + "columnlable" + ".png").toString()));
        rowlable.setImage(new Image(Viewer.class.getResource(URI_BASE + "rowlable" + ".png").toString()));
//        baseboard.setFitWidth(MAIN_PANEL_WIDTH);
//        baseboard.setFitHeight(MAIN_PANEL_HEIGHT);
        baseboard.setLayoutX(MAIN_PANEL_X);
        baseboard.setLayoutY(MAIN_PANEL_Y);
        pegboard.setLayoutX(PEG_PANEL_X);
        pegboard.setLayoutY(PEG_PANEL_Y);
        columnlable.setLayoutX(COLUMN_PANEL_X);
        columnlable.setLayoutY(COLUMN_PANEL_Y);
        rowlable.setLayoutX(ROW_PANEL_X);
        rowlable.setLayoutY(ROW_PANEL_Y);
        BackGround.getChildren().addAll(baseboard,pegboard,
                columnlable,rowlable);
        BackGround.toBack();
        root.getChildren().add(BackGround);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("IQ-TWIST");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);
        makeBackGround();
        root.getChildren().add(controls);
        makeControls();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // FIXME Task 8: Implement starting placements
    public String Initialgamestage() {
        String[] Initialplacement = {
                "a7A7b6A7c1A3d2A6e2C3f3C2g4A7h6D0",
                "a6B0b6C0c5A2d1B3e4A5f4C2g2B3h1A2",
                "a6A0b4A2c3A3d1A3e1C4f4B3g6B2h5D0",
                "a4C4b2C4c1B2d7B1e1C6f6A0g4A5h1A0",
                "a7B1b2C4c1B2d4C4e1C3f4A0g6A1h1A0",
                "a1B5b2C0c5A2d7B7e5B0f1A6g3A7h5D0",
                "a1C6b6A6c2D0d7B1e1A3f2A2g4B2h4A2",
                "a6C4b7A1c2D0d1A0e5B4f1B3g3A3h5A0",
                "a1A3b5A4c5C0d3A6e7A1f3C4g1B3h6D0",
                "a7A7b3B5c3A0d1A3e5C2f1C4g6B7h4B0"
        };
        Random r = new Random();
        int index = r.nextInt(10);
        return (Initialplacement[index]);
    }
    //Code by Pranav Rawat(u6637058)
    // FIXME Task 10: Implement hints
    public String Hints(String placement)
    {
        String []Solution=TwistGame.getSolutions(placement);
        Random r=new Random();
        int index=r.nextInt(Solution.length);
        String Solutionstring=Solution[index];
        for(int i=0;i<placement.length()/4;i++){
            String CurrentPiece= placement.substring(4*i,4*i+4);
            Solutionstring=TwistGame.remove(CurrentPiece,Solutionstring);
        }
        Random p=new Random();
        int index1=p.nextInt(Solutionstring.length()/4);
        String hint=Solutionstring.substring(4*index1,4*index1+4);
        return hint;
    }
    //Code by Pranav Rawat & Cen Dai

    // FIXME Task 11: Generate interesting starting placements

    /**
     * draw the layout
     */
    private Shape makeGrid() {
        Shape shape = new Rectangle((COLUMNS + 1) * SQUARE_SIZE, (ROWS + 1) * SQUARE_SIZE);

        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                Circle circle = new Circle(SQUARE_SIZE / 2);
                circle.setCenterX(SQUARE_SIZE / 2);
                circle.setCenterY(SQUARE_SIZE / 2);
                circle.setTranslateX(x * (SQUARE_SIZE + 5) + SQUARE_SIZE / 4);
                circle.setTranslateY(y * (SQUARE_SIZE + 5) + SQUARE_SIZE / 4);
                shape = Shape.subtract(shape, circle);
            }
        }

        shape.setFill(Color.BLACK);
        return shape;
    }
    private void showDialog(String msg) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    private String setStart(String Initial, String level) {
        String startstage="";
        Nodes[][] Board = new Nodes[4][8];
        //Split the placement string into a String array. Each element is a 4 char placement string
        String[] placementArray = new String[Initial.length() / 4];
        for (int i = 0; i < Initial.length() / 4; i++) {
            placementArray[i] = Initial.substring(4 * i, 4 * i + 4);
        }
        for (int i = 0; i < placementArray.length; i++) {
            char[] temp = placementArray[i].toCharArray();
            int row = temp[2] - 'A';
            int column = temp[1] - '1';
            // if the string represents a piece, do the following things
            if (temp[0] >= 'a' && temp[0] <= 'h') {
                Pieces pieces = new Pieces(temp[0], temp[3] - '0');
                for (int m = 0; m < pieces.height; m++) {
                    for (int n = 0; n < pieces.width; n++) {
                        if (pieces.points[m][n] == null) {
                            continue;
                        }
                        if (Board[row + m][column + n] == null) {
                            Board[row + m][column + n] = new Nodes(null, pieces.points[m][n]);
                        }
                    }
                }
            }
        }
        ArrayList<String> peglist = new ArrayList<>();
        for(int row=0;row<4;row++){
            for (int column=0;column<8;column++){
                if (Board[row][column].point.hole){
                    comp1110.ass2.Color pegColor = Board[row][column].point.getColor();
                    char p0='i';
                    switch(pegColor){
                        case Red:
                            p0='i';
                            break;
                        case Blue:
                            p0='j';
                            break;
                        case Green:
                            p0='k';
                            break;
                        case Yellow:
                            p0='l';
                            break;

                    }
                    char p1= (char)(column+'1');
                    char p2=(char)(row+'A');
                    char p3='0';
                    char[] pegplacement ={p0,p1,p2,p3};
                    String pegstring =new String(pegplacement);
                    peglist.add(pegstring);
                }
            }
        }
        switch (level){
            case "Easy":
                startstage=Initial.substring(0,4)+
                        Initial.substring(8,12)+Initial.substring(28,32)+peglist.get(2)+peglist.get(4);
                break;
            case "Medium":
                startstage=Initial.substring(0,4)+
                        Initial.substring(8,12)+peglist.get(2)+peglist.get(4);
                break;
            case "Hard":
                startstage=Initial.substring(0,4)+
                        Initial.substring(8,12)+peglist.get(2);
                break;
        }

        return startstage;
    }
}
