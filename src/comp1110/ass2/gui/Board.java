package comp1110.ass2.gui;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Random;

public class Board extends Application {
    private static final int BOARD_WIDTH = 933;
    private static final int BOARD_HEIGHT = 700;

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

    // FIXME Task 11: Generate interesting starting placements

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}
