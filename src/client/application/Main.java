package client.application;

import common.entity.NhanVien;
import client.gui.Gui_DangNhap;
import client.gui.Gui_TrangChu;
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