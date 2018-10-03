package comp1110.ass2.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
/**
 *
 * @author Viki Wang
 *
 * This class is UI class
 *
 */
public class TwistGameUI extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 600, 450);
        primaryStage.setTitle("TwistGame");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void gameStart(Stage primaryStage) {
        Board board = new Board();
        //while (!board.isPlacementWellFormed()) {
            // wait user input
            // check and then try to placement
        //}
    }
    public static void main(String[] args) {
        launch(args);
    }

}
