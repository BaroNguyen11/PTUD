package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Gui_CheckIn extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Main layout: BorderPane
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f7fafc;");
 
        // Left sidebar: VBox for menu
        SideBar sideBar = new SideBar();
        root.setLeft(sideBar);

       
        // Scene
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("Restaurant Check-in App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

 
    public static void main(String[] args) {
        launch(args);
    }
}