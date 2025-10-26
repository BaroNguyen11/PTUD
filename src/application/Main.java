package application;

import gui.TrangChu;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // 1. Tạo instance của TrangChu (đã bao gồm SideBar)
            TrangChu root = new TrangChu();

            // Tạo Scene. Không cần kích thước cố định vì sẽ chạy full-screen.
            Scene scene = new Scene(root);

            // Liên kết file CSS
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

            primaryStage.setTitle("Ứng Dụng Quản Lý Nhà Hàng");

            // --- THIẾT LẬP TOÀN MÀN HÌNH ---
            primaryStage.setFullScreen(true);
            // -------------------------------

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
