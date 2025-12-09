package gui;

import dao.DangNhap_DAO;
import entity.NhanVien;
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
public class Gui_TrangChu extends BorderPane {

    private Gui_Sidebar sideBar;
    private StackPane mainContentArea; // Vùng chứa nội dung chính
    
    public NhanVien nhanVienDangDung;

    public Gui_TrangChu(NhanVien nhanVien) {
    		this.nhanVienDangDung = nhanVien;
    	
        // 1. Khởi tạo Vùng Nội dung Chính (CENTER) trước
        this.mainContentArea = new StackPane(); 
        this.mainContentArea.getStyleClass().add("main-content-area"); 
        
        // 2. Khởi tạo SideBar và đặt vào bên TRÁI
        // TRUYỀN THAM CHIẾU 'THIS' (TrangChu) vào SideBar
        this.sideBar = new Gui_Sidebar(this);
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
        setMainContent(new Gui_Dashboard());
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

        
        // Icon Người dùng (Sử dụng Unicode thay cho FontIcon)
        Label userIcon = createSimpleIcon("\u25C9"); // Dấu tròn với chấm đen
        userIcon.getStyleClass().add("user-icon");
        
        // Tên người dùng
        DangNhap_DAO user = new DangNhap_DAO();
        Label userName = new Label("Xin chào: " + nhanVienDangDung.getTenNhanVien());
        userName.getStyleClass().add("user-name-label");

        HBox userInfo = new HBox(15, userIcon, userName);
        userInfo.setAlignment(Pos.CENTER_RIGHT);

        header.getChildren().addAll(titleLabel, userInfo);
        return header;
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
