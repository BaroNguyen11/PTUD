package application;

import gui.GiaoDienDangNhap;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            GiaoDienDangNhap loginScreen = new GiaoDienDangNhap();
            loginScreen.start(primaryStage);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}