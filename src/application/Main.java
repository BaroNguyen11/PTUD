package application;

import entity.NhanVien;
import gui.Gui_DangNhap;
import gui.Gui_TrangChu;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            Gui_DangNhap loginScreen = new Gui_DangNhap();
            loginScreen.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}