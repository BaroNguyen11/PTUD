package gui;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
// import application/application.css; // Giả sử bạn có một file CSS để định dạng
// Lớp TrangChu nên kế thừa từ một Layout Container như BorderPane
public class TrangChu extends BorderPane {

    private SideBar sideBar;
    private StackPane mainContentArea; // Vùng chứa nội dung chính

    public TrangChu() {
        // 1. Khởi tạo SideBar
        this.sideBar = new SideBar();

        // 2. Khởi tạo Vùng Nội dung Chính (Ví dụ: dùng StackPane)
        this.mainContentArea = new StackPane();
        this.mainContentArea.setStyle("-fx-background-color: #ecf0f1;"); // Màu nền sáng cho khu vực chính
        this.mainContentArea.setPadding(new Insets(20));
        
        // ***************************************************************
        // Thêm một Label đơn giản vào khu vực nội dung để kiểm tra
        // ***************************************************************
        this.mainContentArea.getChildren().add(new javafx.scene.control.Label("Nội dung của Màn hình Chính"));

        // 3. Thiết lập bố cục TrangChu (BorderPane)
        
        // Đặt SideBar vào bên trái của BorderPane
        this.setLeft(sideBar); 
        
        // Đặt Vùng Nội dung Chính vào giữa của BorderPane
        this.setCenter(mainContentArea); 
        
        // *Tùy chọn:* Đặt thêm thanh tiêu đề (Header/Toolbar) ở trên
        // this.setTop(new Label("Thanh Tiêu Đề Ứng Dụng"));
    }
    
    // (Bạn có thể thêm các phương thức để chuyển đổi nội dung 
    // trong mainContentArea khi người dùng click vào Sidebar)
}