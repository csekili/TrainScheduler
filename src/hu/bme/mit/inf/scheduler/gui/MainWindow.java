package hu.bme.mit.inf.scheduler.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainWindow extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Train Tracker");

        GridPane root = new GridPane();

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void init(String[] args) {
        new Thread(() -> prepareStart(args)).start();

    }

    private void prepareStart(String[] args) {launch(args);}
}
