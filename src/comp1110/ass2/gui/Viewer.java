package comp1110.ass2.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * A very simple viewer for piece placements in the twist game.
 *
 * NOTE: This class is separate from your main game class.  This
 * class does not play a game, it just illustrates various piece
 * placements.
 */
public class Viewer extends Application {

    /* board layout */
    private static final int SQUARE_SIZE = 60;
    private static final int VIEWER_WIDTH = 750;
    private static final int VIEWER_HEIGHT = 500;

    private static final String URI_BASE = "assets/";

    private final Group root = new Group();
    private final Group controls = new Group();
    TextField textField;

    private final Group img=new Group();


    public static int[] Rotate(String placement){
        //the input is a 4 char string. It represents a piece's location and shape
        char[] a = placement.toCharArray();
        int rotate=(a[3]-'0')%4;
        int[] shift = {0,0}; //shift[0] is x; shift[1] is y
        if(a[0]=='a'||a[0]=='b'||a[0]=='d'||a[0]=='f'){
            if(rotate==1||rotate==3){
                shift[0]=-SQUARE_SIZE/2;
                shift[1]=SQUARE_SIZE/2;
            }
        }else if(a[0]=='c'){
            if(rotate==1||rotate==3){
                shift[0]=(int)(-1.5*SQUARE_SIZE);
                shift[1]=(int)(1.5*SQUARE_SIZE);

            }
        }else if(a[0]=='h'){
            if(rotate==1||rotate==3){
                shift[0]=-SQUARE_SIZE;
                shift[1]=SQUARE_SIZE;
            }
        }
        return shift;
    }
    /**
     * Draw a placement in the window, removing any previously drawn one
     *
     * @param placement  A valid placement string
     */
    public void makePlacement(String placement) {
        root.getChildren().remove(img);
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
            int[] shift=Rotate(placementArray[i]);
            item.setLayoutX(x*SQUARE_SIZE+shift[0]);
            item.setLayoutY(y*SQUARE_SIZE+shift[1]);
            img.getChildren().add(item);

        }
        root.getChildren().add(img);
        // FIXME Task 4: implement the simple placement viewer
    }


    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
        Label label1 = new Label("Placement:");
        textField = new TextField ();
        textField.setPrefWidth(300);
        Button button = new Button("Refresh");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                //root.getChildren().remove(img);
                makePlacement(textField.getText());

                textField.clear();
            }
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField, button);
        hb.setSpacing(10);
        hb.setLayoutX(130);
        hb.setLayoutY(VIEWER_HEIGHT - 50);
        controls.getChildren().add(hb);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("TwistGame Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);

        root.getChildren().add(controls);
       // root.getChildren().add(img);

        makeControls();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
