package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
// Đã loại bỏ import Ikonli vì chưa thêm thư viện

// Lớp TrangChu kế thừa từ BorderPane, bố cục chuẩn cho Sidebar và Header
public class TrangChu extends BorderPane {

    private SideBar sideBar;
    private StackPane mainContentArea; // Vùng chứa nội dung chính

    public TrangChu() {
        // 1. Khởi tạo Vùng Nội dung Chính (CENTER) trước
        this.mainContentArea = new StackPane(); 
        this.mainContentArea.getStyleClass().add("main-content-area"); 
        
        // 2. Khởi tạo SideBar và đặt vào bên TRÁI
        // TRUYỀN THAM CHIẾU 'THIS' (TrangChu) vào SideBar
        this.sideBar = new SideBar(this);
        this.setLeft(sideBar); 

        // 3. Tạo Header và đặt vào TOP
        this.setTop(createHeader());
        
        // 4. Đặt Vùng Nội dung Chính vào giữa và hiển thị Dashboard mặc định
        this.setCenter(mainContentArea); 
        showDashboard(); 
        
        // Áp dụng CSS class cho toàn bộ BorderPane
        this.getStyleClass().add("trang-chu-container");
    }
    
    // --- PHƯƠNG THỨC CHUYỂN ĐỔI NỘI DUNG (Set Main Content) ---
    
    /**
     * Phương thức chuyển đổi nội dung chính
     */
    public void setMainContent(javafx.scene.Node node) {
        mainContentArea.getChildren().clear();
        mainContentArea.getChildren().add(node);
    }

    /**
     * Hiển thị Dashboard (Trang Chủ) mặc định
     */
    public void showDashboard() {
        VBox dashboard = createDashboard();
        setMainContent(dashboard);
    }
    
    // --- CÁC PHƯƠNG THỨC TẠO GIAO DIỆN ---

    /**
     * Tạo thanh Tiêu đề (Header) của ứng dụng.
     * @return HBox chứa Header
     */
    private HBox createHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setSpacing(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add("app-header"); // Class CSS: app-header
        
        // 1. Tiêu đề ứng dụng
        Label titleLabel = new Label("QUẢN LÝ NHÀ HÀNG");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.getStyleClass().add("header-title"); 
        
        HBox.setHgrow(titleLabel, javafx.scene.layout.Priority.ALWAYS); // Đẩy các thành phần khác sang phải

        // 2. Khu vực thông báo và người dùng
        
        // Icon Thông báo (Sử dụng Unicode thay cho FontIcon)
        Label notificationIcon = createSimpleIcon("\u25CF"); // Dấu chấm tròn (mô phỏng chuông)
        notificationIcon.getStyleClass().add("header-icon");
        
        // Icon Người dùng (Sử dụng Unicode thay cho FontIcon)
        Label userIcon = createSimpleIcon("\u25C9"); // Dấu tròn với chấm đen
        userIcon.getStyleClass().add("user-icon");
        
        // Tên người dùng
        Label userName = new Label("Nguyễn Văn A (Quản lý)");
        userName.getStyleClass().add("user-name-label");

        HBox userInfo = new HBox(15, notificationIcon, userIcon, userName);
        userInfo.setAlignment(Pos.CENTER_RIGHT);

        header.getChildren().addAll(titleLabel, userInfo);
        return header;
    }

    /**
     * Tạo Dashboard (Bảng điều khiển) mẫu cho màn hình chính.
     * @return VBox chứa Dashboard
     */
    private VBox createDashboard() {
        VBox dashboard = new VBox(20);
        dashboard.setPadding(new Insets(20));
        
        Label welcomeLabel = new Label("Chào mừng đến với Bảng điều khiển!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        welcomeLabel.getStyleClass().add("welcome-label");

        // Tạo khu vực Thống kê nhanh (GridPane)
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(20);
        statsGrid.setVgap(20);

        // Thêm các thẻ thống kê mẫu (Thay thế Enum bằng String cho Unicode)
        statsGrid.add(createStatCard("Doanh thu hôm nay", "2,500,000 VND", "\u20AB"), 0, 0); // Ký tự tiền tệ
        statsGrid.add(createStatCard("Tổng hóa đơn", "45", "\u270F"), 1, 0); // Ký tự bút
        statsGrid.add(createStatCard("Khách hàng mới", "5", "\u002B"), 2, 0); // Ký tự cộng
        statsGrid.add(createStatCard("Bàn đang phục vụ", "8", "\u25A1"), 3, 0); // Ký tự hình vuông

        dashboard.getChildren().addAll(welcomeLabel, statsGrid);
        return dashboard;
    }

    /**
     * Tạo một thẻ hiển thị số liệu thống kê.
     * @param title Tiêu đề thống kê
     * @param value Giá trị
     * @param iconCode Ký tự Unicode thay cho Enum FontAwesome
     * @return VBox đại diện cho thẻ
     */
    private VBox createStatCard(String title, String value, String iconCode) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setPrefSize(250, 120);
        card.getStyleClass().add("stat-card"); // Class CSS: stat-card
        
        // Icon (Label thay cho FontIcon)
        Label icon = createSimpleIcon(iconCode);
        icon.getStyleClass().add("card-icon");
        
        // Cần chỉnh Font size để mô phỏng kích thước icon
        icon.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Tiêu đề
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", 12));
        titleLabel.getStyleClass().add("card-title");

        // Giá trị
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        valueLabel.getStyleClass().add("card-value");

        card.getChildren().addAll(icon, titleLabel, valueLabel);
        return card;
    }
    
    /**
     * Hàm tiện ích để tạo Label mô phỏng icon bằng ký tự Unicode.
     */
    private Label createSimpleIcon(String unicode) {
        Label iconLabel = new Label(unicode);
        iconLabel.setFont(Font.font("Arial", 18)); // Kích thước cơ bản cho icon Header
        return iconLabel;
    }
}