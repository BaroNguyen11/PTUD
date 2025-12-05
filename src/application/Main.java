package application;

import gui.GiaoDienDangNhap;
import gui.TrangChu;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
//        try {
//            GiaoDienDangNhap loginScreen = new GiaoDienDangNhap();
//            loginScreen.start(primaryStage);
//
//
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
    	
    	
    	try {
            // 1. Tạo instance của TrangChu (đã bao gồm SideBar)
            TrangChu root = new TrangChu();

            // Tạo Scene. Không cần kích thước cố định vì sẽ chạy full-screen.
            Scene scene = new Scene(root);

            // Liên kết file CSS
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

            primaryStage.setTitle("Ứng Dụng Quản Lý Nhà Hàng");

            // --- THIẾT LẬP TOÀN MÀN HÌNH ---
            primaryStage.setMaximized(true);
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