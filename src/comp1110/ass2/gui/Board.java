package comp1110.ass2.gui;

import comp1110.ass2.Pegs;
import comp1110.ass2.Pieces;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.util.Random;

public class Board extends Application {
    private static final int SQUARE_SIZE = 100;
    private static final int BOARD_WIDTH = 933;
    private static final int BOARD_HEIGHT = 700;
    private static final int COLUMNS = 8;
    private static final int ROWS = 4;

    private Boolean[][] board = new Boolean[ROWS][COLUMNS];

    private String piecePlacement;

    private double mouseX;
    private double mouseY;

    private static final String URI_BASE = "assets/";

    private final Group root = new Group();
    private final Group img=new Group();

    int[] Rotate(String placement, ImageView image) {
        //the input is a 4 char string. It represents a piece's location and shape
        char[] a = placement.toCharArray();
        int rotate = (a[3] - '0') % 4;
        int[] shift = {0, 0}; //shift[0] is x; shift[1] is y
        if (a[0] == 'a' || a[0] == 'b' || a[0] == 'd' || a[0] == 'f') {
            if (rotate == 1 || rotate == 3) {
                shift[0] = -SQUARE_SIZE / 2;
                shift[1] = SQUARE_SIZE / 2;
            }
        } else if (a[0] == 'c') {
            if (rotate == 1 || rotate == 3) {
                shift[0] = (int) (-1.5 * SQUARE_SIZE);
                shift[1] = (int) (1.5 * SQUARE_SIZE);

            }
        } else if (a[0] == 'h') {
            if (rotate == 1 || rotate == 3) {
                shift[0] = -SQUARE_SIZE;
                shift[1] = SQUARE_SIZE;
            }
        }
        return shift;
    }
    // FIXME Task 7: Implement a basic playable Twist Game in JavaFX that only allows pieces to be placed in valid places
    public boolean isValid(String piecePlacement){
        char[] pieceArray = piecePlacement.toCharArray();
        char shape = pieceArray[0];
        int orientation = pieceArray[3];
        int X = pieceArray[1] - '1';
        int Y = pieceArray[2] - 'A';

        //Pieces piece = new Pieces(shape,orientation);

        if(0 <= X && X<= 8 && 0 <= Y && Y<= 3 ) {
            return true;
        }
        return false;
    }
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

    // FIXME Task 11: Generate interesting starting placements
    //mouse press handler
    //check and update Boolean[][] boards then generate false true and null
    /**
     * Draw a placement in the window, removing any previously drawn one
     * @param placement  A valid placement string
     */
    void makePlacement(String placement) {
        //root.getChildren().clear();
        img.getChildren().clear();
        // split the placement string into a string array;
        String[] placementArray = new String[placement.length()/4];
        for(int i =0;i<placementArray.length;i++){
            placementArray[i]= placement.substring(4*i,4*i+4);
            //System.out.println(placementArray[i]);
        }
        for(int i=0; i<placementArray.length;i++){
            char[]a = placementArray[i].toCharArray();
            char id = a[0];
            int x= (int)(a[1]-'1');
            int y =(int)(a[2]-'A');
            ImageView item =new ImageView();
            Image pic = new Image(Viewer.class.getResource(URI_BASE + id + ".png").toString());
            item.setImage(pic);
            item.setFitWidth(SQUARE_SIZE*pic.getWidth()/100);
            item.setFitHeight(SQUARE_SIZE*pic.getHeight()/100);
            //check and set flip
            if((a[3]-'0')/4!=0){
                item.setScaleY(-1);
            }
            int itemRotate = (a[3]-'0')%4;
            item.setRotate(itemRotate*90);
            int[] shift=Rotate(placementArray[i],item);
            item.setLayoutX(x*SQUARE_SIZE+shift[0]);
            item.setLayoutY(y*SQUARE_SIZE+shift[1]);
            img.getChildren().add(item);

        }
        root.getChildren().add(img);
        // FIXME Task 4: implement the simple placement viewer
    }
    /**
     *draw the layout
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


    @Override
    public void start(Stage primaryStage) throws Exception {

        if (isValid(piecePlacement)) {
            makePlacement(piecePlacement);
        }
        root.getChildren().add(makeGrid());
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(root);

        Scene scene = new Scene(borderPane, BOARD_WIDTH, BOARD_HEIGHT);

        primaryStage.setTitle("IQ-Twist");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

