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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
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

    private static final String URI_BASE = "C:/Users/Alienware/IdeaProjects/comp1110-ass2-thu16n/src/comp1110/ass2/gui/assets/";

    private final Group root = new Group();
    private final Group input = new Group();
    private final Group img = new Group();

    int[] Rotate(char a0, char a1, char a2, char a3) {
        // the input is a 4 char string. It represents a piece's location and shape
        int rotate = (a3 - '0') % 4;
        int[] shift = { 0, 0 }; // shift[0] is x; shift[1] is y
        if (a0 == 'a' || a0 == 'b' || a0 == 'd' || a0 == 'f') {
            if (rotate == 1 || rotate == 3) {
                shift[0] = -SQUARE_SIZE / 2;
                shift[1] = SQUARE_SIZE / 2;
            }
        } else if (a0 == 'c') {
            if (rotate == 1 || rotate == 3) {
                shift[0] = (int) (-1.5 * SQUARE_SIZE);
                shift[1] = (int) (1.5 * SQUARE_SIZE);
            }
        } else if (a0 == 'h') {
            if (rotate == 1 || rotate == 3) {
                shift[0] = -SQUARE_SIZE;
                shift[1] = SQUARE_SIZE;
            }
        }
        return shift;
    }

    // FIXME Task 7: Implement a basic playable Twist Game in JavaFX that only
    // allows pieces to be placed in valid places
    public boolean isValid(String piecePlacement) {
        if (piecePlacement == null)
            return false;
        char[] pieceArray = piecePlacement.toCharArray();
        if (pieceArray.length % 4 != 0)
            return false;
        for (int i = 0; i < pieceArray.length; i+= 4) {
            char shape = pieceArray[i];
            int col = pieceArray[i + 1] - '1';
            int row = pieceArray[i + 2] - 'A';
            int orientation = pieceArray[i + 3] - '0';
            Pieces piece = new Pieces(shape, orientation);
            if (shape < 'a' || shape > 'h')
                return false;
            if (orientation < 0 || orientation > 7)
                return false;
            if (col < 0 || col > 7 || col + piece.points[0].length > 8)
                return false;
            if (row < 0 || row > 3 || row + piece.points.length > 4)
                return false;
            for (int j = row; j < row + piece.points.length; j++) {
                for (int k = col; k < col + piece.points[0].length; k++) {
                    if (board[j][i])
                        return false;
                }
            }
        }
        return true;
    }

    // FIXME Task 8: Implement starting placements
    public String Initialgamestage() {
        String[] Initialplacement = { "a7A7b6A7", "i5A0", "d1A6j1C0", "c1A3d2A6", "l4B0l5C0", "j1C0k3C0",
                "h6D0i6B0j2B0", "l5C0", "b6A7i5A0", "k1b0k6B0l5A0l3C0", "g6B7h4B0k3D0", "j4B0k8B0k5D0", "c1A3D2A6",
                "d7C7j4D0" };
        Random r = new Random();
        int index = r.nextInt(15);
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

    // FIXME Task 11: Generate interesting starting placements
    // mouse press handler
    // check and update Boolean[][] boards then generate false true and null
    /**
     * Draw a placement in the window, removing any previously drawn one
     *
     * @param placement
     *            A valid placement string
     * @throws MalformedURLException
     */
    public void makePlacement(String placement) {
        img.getChildren().clear();
        clearBoard();
        //
        char[] pieceArray = piecePlacement.toCharArray();
        //
        for (int i = 0; i < pieceArray.length; i+= 4) {
            char shape = pieceArray[i];
            int col = pieceArray[i + 1] - '1';
            int row = pieceArray[i + 2] - 'A';
            int orientation = pieceArray[i + 3] - '0';
            Pieces piece = new Pieces(shape, orientation);

            ImageView item = new ImageView();
            Image pic = new Image("file:/" + (URI_BASE + shape + ".png"));
            item.setImage(pic);
            makeRotate(item, orientation);
            item.setFitWidth((SQUARE_SIZE + 5) * pic.getWidth() / 100);
            item.setFitHeight((SQUARE_SIZE + 5) * pic.getHeight() / 100);
            //
            int[] shift = Rotate(pieceArray[i], pieceArray[i + 1], pieceArray[i + 2], pieceArray[i + 3]);
            item.setLayoutX((SQUARE_SIZE + 5) * col + shift[0] + SQUARE_SIZE / 4);
            item.setLayoutY((SQUARE_SIZE + 5) * row + shift[1] + SQUARE_SIZE / 4);

            img.getChildren().add(item);
            for (int j = row; j < row + piece.points.length; j++) {
                for (int k = col; k < col + piece.points[0].length; k++) {
                    if (piece.points[j - row][k - col] != null)
                        board[j][k] = true;
                }
            }

        }
        if (!root.getChildren().contains(img))
            root.getChildren().add(img);
    }

    public void makeRotate(ImageView item, int orientation) {
        if (orientation < 4) {
            item.setRotate(90  * orientation);
        } else {
            item.setScaleY(-1);
            makeRotate(item, orientation % 4);
        }
    }

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

    private void clearBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = false;
            }
        }
    }

    private boolean win() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(!board[i][j])
                    return false;
            }
        }
        return true;
    }

    private void setInputGruop() {
        Label label = new Label("Input");
        label.setLayoutX(0);
        label.setLayoutY(0);
        TextField text = new TextField();
        text.setPrefWidth(195);
        text.setLayoutX(300);
        text.setLayoutY(50);
        Button btnput = new Button();
        btnput.setText("Put");
        btnput.setPrefWidth(95);
        btnput.setLayoutX(500);
        btnput.setLayoutY(50);
        Button btnremove = new Button();
        btnremove.setText("Remove");
        btnremove.setPrefWidth(95);
        btnremove.setLayoutX(600);
        btnremove.setLayoutY(50);

        btnput.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                String input = text.getText();
                if (isValid(input)) {
                    piecePlacement += input;
                    makePlacement(piecePlacement);
                } else {
                    showDialog("Input invalid!");
                }
                if (win())
                    showDialog("You win!");
            }
        });

        btnremove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                String input = text.getText();
                if (piecePlacement.contains(input)) {
                    piecePlacement = piecePlacement.replace(input, "");
                    makePlacement(piecePlacement);
                } else {
                    showDialog("Can't find the piece!");
                }
            }
        });

        input.getChildren().add(label);
        input.getChildren().add(text);
        input.getChildren().add(btnput);
        input.getChildren().add(btnremove);
    }

    private void showDialog(String msg) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        piecePlacement = "a1A7";
        clearBoard();
        if (isValid(piecePlacement)) {
            makePlacement(piecePlacement);
        }

        root.getChildren().add(makeGrid());
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(root);

        setInputGruop();
        borderPane.setTop(input);

        Scene scene = new Scene(borderPane, BOARD_WIDTH, BOARD_HEIGHT);

        primaryStage.setTitle("IQ-Twist");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
