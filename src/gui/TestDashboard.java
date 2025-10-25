package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TestDashboard extends Application {

    @Override
    public void start(Stage primaryStage) {

    	Scene scene = new Scene(new Dashboard());
    	primaryStage.setScene(scene);
    	primaryStage.setMaximized(true);
    	primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
