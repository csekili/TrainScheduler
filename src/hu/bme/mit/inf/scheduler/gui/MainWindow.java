package hu.bme.mit.inf.scheduler.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import hu.bme.mit.inf.scheduler.main.Main;
import hu.bme.mit.inf.scheduler.model.ScheduleEntry;
import hu.bme.mit.inf.scheduler.model.Segment;
import hu.bme.mit.inf.scheduler.model.Train;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.HashMap;


public class MainWindow extends Application {

    HBox routeHolder;
    Text errorText;

    private HashMap<Integer, SectionHolder> route = new HashMap<>();
    private static MainWindow mw; //:(

    @Override
    public void start(Stage primaryStage) throws Exception {
        //hacking
        mw=this;

        //--------------------------------------------------------------------------------------------------------------
        //basic properties of the window, root node
        //--------------------------------------------------------------------------------------------------------------

        primaryStage.setTitle("Train Tracker");
        primaryStage.setMinWidth(500);
        primaryStage.setOnCloseRequest(new eh_FormClosing());

        BorderPane root = new BorderPane();

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);

        //adding color profile
        scene.getStylesheets().addAll("hu/bme/mit/inf/scheduler/gui/jfx-button-red.css");

        //--------------------------------------------------------------------------------------------------------------
        //creating control bar
        //--------------------------------------------------------------------------------------------------------------
        GridPane controlBar = new GridPane();
        controlBar.setHgap(10);
        controlBar.setPadding(new Insets(10, 25, 15, 25));
        controlBar.setStyle("-fx-background-color: #F0F0F0");
        root.setTop(controlBar);

        Text t1 = new Text("New route from");
        t1.setStyle("-fx-font-family: Roboto, \"Segoe UI\",  sans-serif; -fx-font-size: 15px;");
        controlBar.add(t1, 0, 0);

        JFXTextField from = new JFXTextField();
        controlBar.add(from, 1, 0);

        Text t2 = new Text("to");
        t2.setStyle("-fx-font-family: Roboto, \"Segoe UI\",  sans-serif; -fx-font-size: 15px;");
        controlBar.add(t2, 2, 0);

        JFXTextField to = new JFXTextField();
        controlBar.add(to, 3, 0);

        JFXButton goButton = new JFXButton("Go!");
        goButton.setStyle("-fx-pref-width: 80px;");
        controlBar.add(goButton, 4, 0);


        //--------------------------------------------------------------------------------------------------------------
        //creating hidden bottom bar
        //--------------------------------------------------------------------------------------------------------------
        VBox bottomBar = new VBox();
        bottomBar.setPadding(new Insets(10, 10, 25, 10));
        bottomBar.setAlignment(Pos.BASELINE_CENTER);
        root.setBottom(bottomBar);

        JFXSpinner spinner = new JFXSpinner();
        spinner.setVisible(false);
        bottomBar.getChildren().add(spinner);

        errorText = new Text("Ouch! Something bad has happened. Please retry.");
        errorText.setStyle("-fx-font-family: Roboto, \"Segoe UI\",  sans-serif; -fx-font-size: 12px;");
        errorText.setFill(javafx.scene.paint.Color.valueOf("#E53935"));
        errorText.setVisible(false);
        bottomBar.getChildren().add(errorText);


        //--------------------------------------------------------------------------------------------------------------
        //creating content
        //--------------------------------------------------------------------------------------------------------------
        VBox drawPane = new VBox();
        drawPane.setAlignment(Pos.CENTER);
        root.setCenter(drawPane);

        //creating current route placeholder
        routeHolder = new HBox();
        routeHolder.setFillHeight(false);
        routeHolder.setAlignment(Pos.CENTER);
        routeHolder.setPadding(new Insets(20, 10, 20, 10));
        drawPane.getChildren().add(routeHolder);

        //drawing map of world
        /*ImageView railroadMap = new ImageView();
        railroadMap.setImage(new Image(""));
        drawPane.getChildren().add(railroadMap);*/


        //--------------------------------------------------------------------------------------------------------------
        //showing window
        //--------------------------------------------------------------------------------------------------------------
        primaryStage.show();
    }

    public static void init(String[] args) {
        new Thread(() -> prepareStart(args)).start();

    }

    private static void prepareStart(String[] args) {launch(args);}

    public void drawRoute(ScheduleEntry e) {
        //filling up hashmap of route
        for(int i=0; i<e.getRailRoadElements().size()-1; i++) {
            route.put(e.getRailRoadElements().get(i).getId(), new SectionHolder("S" + e.getRailRoadElements().get(i).getId()));
        }
        route.put(e.getRailRoadElements().get(e.getRailRoadElements().size()-1).getId(), new EndSectionHolder("S" + e.getRailRoadElements().get(e.getRailRoadElements().size()-1).getId()));

        //drawing route on screen
        Platform.runLater(() -> routeHolder.getChildren().clear());
        for(SectionHolder h : route.values()) {
            Platform.runLater(() -> routeHolder.getChildren().add(h.getPanel()));
        }
    }

    public static MainWindow getWindow() {
        while(mw  == null){
            try {
                Thread.sleep(100);
            }catch (Exception e ){}

        };
        return mw;
    }


    //------------------------------------------------------------------------------------------------------------------
    //event handling classes
    //------------------------------------------------------------------------------------------------------------------

    private class eh_FormClosing implements EventHandler<WindowEvent> {

        @Override
        public void handle(WindowEvent event) {
            //option A
            //System.exit(0);

            //option B
            //tries to terminate main process using its windowClosed() function; if fails then gives error message
            if (Main.windowClosed()) return;

            errorText.setText("Couldn't terminate application! Please try again.");
            errorText.setVisible(true);
            event.consume();
        }
    }
}
