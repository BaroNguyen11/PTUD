package application;

import gui.TrangChu; // Thêm import lớp TrangChu
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
// import javafx.scene.layout.BorderPane; // Không cần thiết nếu TrangChu đã là BorderPane


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			// 1. Tạo instance của TrangChu (đã bao gồm SideBar)
			TrangChu root = new TrangChu(); 
			
			// Thiết lập kích thước lớn hơn để Sidebar và nội dung hiển thị tốt hơn
			Scene scene = new Scene(root, 1200, 700); 
			
			// Liên kết file CSS (Đảm bảo file application.css đã có các CSS cho Sidebar)
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setTitle("Ứng Dụng Quản Lý Nhà Hàng"); // Thêm tiêu đề
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