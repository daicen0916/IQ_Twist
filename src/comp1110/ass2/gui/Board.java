package comp1110.ass2.gui;

import comp1110.ass2.Pieces;
import comp1110.ass2.TwistGame;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    private static final int PEG_PANEL_X=80;
    private static final int PEG_PANEL_Y=20;
    private static final int COLUMNS = 8;
    private static final int ROWS = 4;
    private static final String URI_BASE = "assets/";
    private final Group root = new Group();
    private final Group controls = new Group();
    private final Group img=new Group();
    private final Group pieces=new Group();
    private final Group VacantPegs = new Group();
    private final Group BackGround= new Group();
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
            int[] initX={0,210,420,690,0,150,360,570};
            int[] initY={0,130};
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
                int rotate_status= ((((int)getRotate())%360)/90)%4;
                String piecePlacement=makePiecePlacement(this.id,column,row,rotate_status);
                CurrentPlacement=TwistGame.generatePlacement(piecePlacement,CurrentPlacement);
                UpdateAndCheck();
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
            if(!onBoard()){
                int index=CurrentPlacement.indexOf(this.id);
                if(index>=0){
                    CurrentPlacement=CurrentPlacement.substring(0,index)
                            +CurrentPlacement.substring(index+4);
                }
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
                snapToHome();
            }else if(CurrentPlacement.length()>=32&&CurrentPlacement.charAt(28)=='h'){
                System.out.println("Congratulations! You complete this game!");
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
            double right_location=real_x+this.getFitWidth();
            double bottom_location=real_y+this.getFitHeight();
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
        int index= InitialPlacement.indexOf(id);
        if(index>0){
            count++;
            if(index+4==InitialPlacement.length()){
                return count;
            }else if(InitialPlacement.charAt(index+4)==id){
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


    /**
     * Create a New Game button to setup a new game.
     */
    private void makeControls() {
        Button button = new Button("New Game");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                InitialPlacement=Initialgamestage();
                CurrentPlacement = InitialPlacement;
                makeInitialPlacement(InitialPlacement);
                SetVacantPegs(InitialPlacement);
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
    private void makeBackGround(){
        BackGround.getChildren().clear();

        ImageView baseboard = new ImageView();
        baseboard.setImage(new Image(Viewer.class.getResource(URI_BASE + "board" + ".png").toString()));
        baseboard.setFitWidth(MAIN_PANEL_WIDTH);
        baseboard.setFitHeight(MAIN_PANEL_HEIGHT);
        baseboard.setLayoutX(MAIN_PANEL_X);
        baseboard.setLayoutY(MAIN_PANEL_Y);
        BackGround.getChildren().add(baseboard);
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

//    private static final int SQUARE_SIZE = 100;
//    private static final int BOARD_WIDTH = 933;
//    private static final int BOARD_HEIGHT = 700;
//    private static final int COLUMNS = 8;
//    private static final int ROWS = 4;
//
//    private Boolean[][] board = new Boolean[ROWS][COLUMNS];
//    private String piecePlacement;
//    private double mouseX;
//    private double mouseY;
//
//    private static final String URI_BASE = "C:/Users/Alienware/IdeaProjects/comp1110-ass2-thu16n/src/comp1110/ass2/gui/assets/";
//
//    private final Group root = new Group();
//    private final Group input = new Group();
//    private final Group img = new Group();
//
//    int[] Rotate(char a0, char a1, char a2, char a3) {
//        // the input is a 4 char string. It represents a piece's location and shape
//        int rotate = (a3 - '0') % 4;
//        int[] shift = { 0, 0 }; // shift[0] is x; shift[1] is y
//        if (a0 == 'a' || a0 == 'b' || a0 == 'd' || a0 == 'f') {
//            if (rotate == 1 || rotate == 3) {
//                shift[0] = -SQUARE_SIZE / 2;
//                shift[1] = SQUARE_SIZE / 2;
//            }
//        } else if (a0 == 'c') {
//            if (rotate == 1 || rotate == 3) {
//                shift[0] = (int) (-1.5 * SQUARE_SIZE);
//                shift[1] = (int) (1.5 * SQUARE_SIZE);
//            }
//        } else if (a0 == 'h') {
//            if (rotate == 1 || rotate == 3) {
//                shift[0] = -SQUARE_SIZE;
//                shift[1] = SQUARE_SIZE;
//            }
//        }
//        return shift;
//    }

    // FIXME Task 7: Implement a basic playable Twist Game in JavaFX that only
    // allows pieces to be placed in valid places
//    public boolean isValid(String piecePlacement) {
//        if (piecePlacement == null)
//            return false;
//        char[] pieceArray = piecePlacement.toCharArray();
//        if (pieceArray.length % 4 != 0)
//            return false;
//        for (int i = 0; i < pieceArray.length; i+= 4) {
//            char shape = pieceArray[i];
//            int col = pieceArray[i + 1] - '1';
//            int row = pieceArray[i + 2] - 'A';
//            int orientation = pieceArray[i + 3] - '0';
//            Pieces piece = new Pieces(shape, orientation);
//            if (shape < 'a' || shape > 'h')
//                return false;
//            if (orientation < 0 || orientation > 7)
//                return false;
//            if (col < 0 || col > 7 || col + piece.points[0].length > 8)
//                return false;
//            if (row < 0 || row > 3 || row + piece.points.length > 4)
//                return false;
//            for (int j = row; j < row + piece.points.length; j++) {
//                for (int k = col; k < col + piece.points[0].length; k++) {
//                    if (board[j][i])
//                        return false;
//                }
//            }
//        }
//        return true;
//    }

    // FIXME Task 8: Implement starting placements
    public String Initialgamestage() {
        String[] Initialplacement = { "a7A7b6A7", "i5A0", "d1A6j1C0", "c1A3d2A6", "l4B0l5C0", "j1C0k3C0",
                "h6D0i6B0j2B0", "l5C0", "b6A7i5A0", "k1b0k6B0l5A0l3C0", "g6B7h4B0k3D0", "j4B0k8B0k5D0", "c1A3D2A6",
                "d7B7j4D0" };
        Random r = new Random();
        int index = r.nextInt(14);
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
    // mouse press handler
    // check and update Boolean[][] boards then generate false true and null
//    /**
//     * Draw a placement in the window, removing any previously drawn one
//     *
//     * @param placement
//     *            A valid placement string
//     * @throws MalformedURLException
//     */
//    public void makePlacement(String placement) {
//        img.getChildren().clear();
//        clearBoard();
//        //
//        char[] pieceArray = piecePlacement.toCharArray();
//        //
//        for (int i = 0; i < pieceArray.length; i+= 4) {
//            char shape = pieceArray[i];
//            int col = pieceArray[i + 1] - '1';
//            int row = pieceArray[i + 2] - 'A';
//            int orientation = pieceArray[i + 3] - '0';
//            Pieces piece = new Pieces(shape, orientation);
//
//            ImageView item = new ImageView();
//            Image pic = new Image("file:/" + (URI_BASE + shape + ".png"));
//            item.setImage(pic);
//            makeRotate(item, orientation);
//            item.setFitWidth((SQUARE_SIZE + 5) * pic.getWidth() / 100);
//            item.setFitHeight((SQUARE_SIZE + 5) * pic.getHeight() / 100);
//            //
//            int[] shift = Rotate(pieceArray[i], pieceArray[i + 1], pieceArray[i + 2], pieceArray[i + 3]);
//            item.setLayoutX((SQUARE_SIZE + 5) * col + shift[0] + SQUARE_SIZE / 4);
//            item.setLayoutY((SQUARE_SIZE + 5) * row + shift[1] + SQUARE_SIZE / 4);
//
//            img.getChildren().add(item);
//            for (int j = row; j < row + piece.points.length; j++) {
//                for (int k = col; k < col + piece.points[0].length; k++) {
//                    if (piece.points[j - row][k - col] != null)
//                        board[j][k] = true;
//                }
//            }
//
//        }
//        if (!root.getChildren().contains(img))
//            root.getChildren().add(img);
//    }
//
//    public void makeRotate(ImageView item, int orientation) {
//        if (orientation < 4) {
//            item.setRotate(90  * orientation);
//        } else {
//            item.setScaleY(-1);
//            makeRotate(item, orientation % 4);
//        }
//    }
//
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
//
//    private void clearBoard() {
//        for (int i = 0; i < board.length; i++) {
//            for (int j = 0; j < board[i].length; j++) {
//                board[i][j] = false;
//            }
//        }
//    }
//
//    private boolean win() {
//        for (int i = 0; i < board.length; i++) {
//            for (int j = 0; j < board[i].length; j++) {
//                if(!board[i][j])
//                    return false;
//            }
//        }
//        return true;
//    }
//
//    private void setInputGruop() {
//        Label label = new Label("Input");
//        label.setLayoutX(0);
//        label.setLayoutY(0);
//        TextField text = new TextField();
//        text.setPrefWidth(195);
//        text.setLayoutX(300);
//        text.setLayoutY(50);
//        Button btnput = new Button();
//        btnput.setText("Put");
//        btnput.setPrefWidth(95);
//        btnput.setLayoutX(500);
//        btnput.setLayoutY(50);
//        Button btnremove = new Button();
//        btnremove.setText("Remove");
//        btnremove.setPrefWidth(95);
//        btnremove.setLayoutX(600);
//        btnremove.setLayoutY(50);
//
//        btnput.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent arg0) {
//                String input = text.getText();
//                if (isValid(input)) {
//                    piecePlacement += input;
//                    makePlacement(piecePlacement);
//                } else {
//                    showDialog("Input invalid!");
//                }
//                if (win())
//                    showDialog("You win!");
//            }
//        });
//
//        btnremove.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent arg0) {
//                String input = text.getText();
//                if (piecePlacement.contains(input)) {
//                    piecePlacement = piecePlacement.replace(input, "");
//                    makePlacement(piecePlacement);
//                } else {
//                    showDialog("Can't find the piece!");
//                }
//            }
//        });
//
//        input.getChildren().add(label);
//        input.getChildren().add(text);
//        input.getChildren().add(btnput);
//        input.getChildren().add(btnremove);
//    }
//
    private void showDialog(String msg) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        piecePlacement = "a1A7";
//        clearBoard();
//        if (isValid(piecePlacement)) {
//            makePlacement(piecePlacement);
//        }
//
//        root.getChildren().add(makeGrid());
//        BorderPane borderPane = new BorderPane();
//        borderPane.setCenter(root);
//
//        setInputGruop();
//        borderPane.setTop(input);
//
//        Scene scene = new Scene(borderPane, BOARD_WIDTH, BOARD_HEIGHT);
//
//        primaryStage.setTitle("IQ-Twist");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
}
