package hu.bme.mit.inf.scheduler.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import hu.bme.mit.inf.scheduler.model.ScheduleEntry;
import javafx.application.Application;
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

import java.util.HashMap;


public class MainWindow extends Application {

    private HashMap<Integer, SectionHolder> route = new HashMap<>();

    @Override
    public void start(Stage primaryStage) throws Exception {

        //--------------------------------------------------------------------------------------------------------------
        //basic properties of the window, root node
        //--------------------------------------------------------------------------------------------------------------

        primaryStage.setTitle("Train Tracker");
        primaryStage.setMinWidth(500);

        BorderPane root = new BorderPane();

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);

        //adding color profile
        scene.getStylesheets().addAll("jfx-button-red.css");

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

        Text errorText = new Text("Ouch! Something bad has happened. Please retry.");
        errorText.setStyle("-fx-font-family: Roboto, \"Segoe UI\",  sans-serif; -fx-font-size: 12px;");
        errorText.setFill(javafx.scene.paint.Color.valueOf("#E53935"));
        errorText.setVisible(false);
        bottomBar.getChildren().add(errorText);


        //--------------------------------------------------------------------------------------------------------------
        //creating content placeholder
        //--------------------------------------------------------------------------------------------------------------
        HBox routeHolder = new HBox();
        routeHolder.setFillHeight(false);
        routeHolder.setAlignment(Pos.CENTER);
        root.setCenter(routeHolder);

        /*//testing image handling TODO: delete this
        routeHolder.getChildren().add(new SectionHolder("S12").getPanel());
        SectionHolder itt = new SectionHolder("S39"); itt.setTrainHere(true); routeHolder.getChildren().add(itt.getPanel());
        routeHolder.getChildren().add(new SectionHolder("S42").getPanel());
        routeHolder.getChildren().add(new SectionHolder("S18").getPanel());
        routeHolder.getChildren().add(new EndSectionHolder("S4").getPanel());
        */


        //--------------------------------------------------------------------------------------------------------------
        //showing window
        //--------------------------------------------------------------------------------------------------------------
        primaryStage.show();
    }

    public void init(String[] args) {
        new Thread(() -> prepareStart(args)).start();

    }

    private void prepareStart(String[] args) {launch(args);}

    public void drawRoute(ScheduleEntry e) {
        //TODO: megírni

    }
}
