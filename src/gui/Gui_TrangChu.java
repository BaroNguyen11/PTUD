package gui;

import dao.DangNhap_DAO;
import entity.NhanVien;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
// Đã loại bỏ import Ikonli vì chưa thêm thư viện

// Lớp TrangChu kế thừa từ BorderPane, bố cục chuẩn cho Sidebar và Header
public class Gui_TrangChu extends BorderPane {

    private Gui_Sidebar sideBar;
    private StackPane mainContentArea; // Vùng chứa nội dung chính
    // Icon Mắt Mở (Show)
    private static final String SVG_EYE_OPEN = "M2.036 12.322a1.012 1.012 0 0 1 0-.639C3.423 7.51 7.36 4.5 12 4.5c4.638 0 8.573 3.007 9.963 7.178.07.207.07.431 0 .639C20.577 16.49 16.64 19.5 12 19.5c-4.638 0-8.573-3.007-9.963-7.178Z M15 12a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z";
    // Icon Mắt Đóng (Hide / Gạch chéo)
    private static final String SVG_EYE_CLOSED = "M3.98 8.223A10.477 10.477 0 0 0 1.934 12C3.226 16.338 7.244 19.5 12 19.5c.993 0 1.953-.138 2.863-.395M6.228 6.228A10.451 10.451 0 0 1 12 4.5c4.756 0 8.773 3.162 10.065 7.498a10.522 10.522 0 0 1-4.293 5.774M6.228 6.228 3 " +
            "3m3.228 3.228 3.65 3.65m7.894 7.894L21 21m-3.228-3.228-3.65-3.65m0 0a3 3 0 1 0-4.243-4.243m4.242 4.242L9.88 9.88";
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
     *
     * @return HBox chứa Header
     */
//    private HBox createHeader() {
//        HBox header = new HBox();
//        header.setPadding(new Insets(15, 20, 15, 20));
//        header.setSpacing(20);
//        header.setAlignment(Pos.CENTER_LEFT);
//        header.getStyleClass().add("app-header"); // Class CSS: app-header
//
//        // 1. Tiêu đề ứng dụng
//        Label titleLabel = new Label("QUẢN LÝ NHÀ HÀNG");
//        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
//        titleLabel.getStyleClass().add("header-title");
//
//        HBox.setHgrow(titleLabel, javafx.scene.layout.Priority.ALWAYS); // Đẩy các thành phần khác sang phải
//
//        // 2. Khu vực thông báo và người dùng
//
//        // Icon Thông báo (Sử dụng Unicode thay cho FontIcon)
//
//
//        // Icon Người dùng (Sử dụng Unicode thay cho FontIcon)
//        Label userIcon = createSimpleIcon("\u25C9"); // Dấu tròn với chấm đen
//        userIcon.getStyleClass().add("user-icon");
//
//        // Tên người dùng
//        DangNhap_DAO user = new DangNhap_DAO();
//        Label userName = new Label("Xin chào: " + nhanVienDangDung.getTenNhanVien());
//        userName.getStyleClass().add("user-name-label");
//
//        HBox userInfo = new HBox(15, userIcon, userName);
//        userInfo.setAlignment(Pos.CENTER_RIGHT);
//
//        header.getChildren().addAll(titleLabel, userInfo);
//        return header;
//    }
    private HBox createHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(10, 20, 10, 20));
        header.setAlignment(Pos.CENTER_LEFT);

        // Thêm bóng đổ nhẹ và nền trắng
        header.setStyle("-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 1);");
        header.setPrefHeight(60);

        // --- 1. Tiêu đề (Bên Trái) ---
        Label titleLabel = new Label("QUẢN LÝ NHÀ HÀNG");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        titleLabel.setStyle("-fx-text-fill: #082744;");

        // --- 2. Spacer (Khoảng trống ở giữa - Quan trọng) ---
        // Đây chính là "justify-content: space-between" trong JavaFX
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        // --- 3. User Profile (Bên Phải) ---
        // ... (Phần code tạo avatar và user info giữ nguyên như cũ) ...
        SVGPath iconUser = new SVGPath();
        iconUser.setContent("M15.75 6a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0ZM4.501 20.118a7.5 7.5 0 0 1 14.998 0A17.933 17.933 0 0 1 12 21.75c-2.676 0-5.216-.584-7.499-1.632Z");

        iconUser.setFill(Color.TRANSPARENT);   // Không tô màu nền bên trong nét vẽ
        iconUser.setStroke(Color.web("#5e6c84")); // Màu viền (xám xanh chuyên nghiệp)
        iconUser.setStrokeWidth(1.5);          // Độ dày nét vẽ

        StackPane avatarCircle = new StackPane(iconUser);
        avatarCircle.setPrefSize(40, 40);      // Kích thước vòng tròn avatar
        avatarCircle.setMinSize(40, 40);
        avatarCircle.setMaxSize(40, 40);
        avatarCircle.setStyle(
                "-fx-background-color: #b4b8b8;" +    // Nền xám nhạt
                        "-fx-background-radius: 50%;" +       // Bo tròn thành hình tròn
                        "-fx-border-color: #dfe1e6;" +        // Viền xám
                        "-fx-border-radius: 50%;"             // Viền bo tròn
        );

        VBox userInfo = new VBox(2);
        userInfo.setAlignment(Pos.CENTER_RIGHT); // Căn lề phải cho text

        Label lblName = new Label(nhanVienDangDung.getTenNhanVien());
        lblName.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #2d3436;");

        boolean isAdmin = Gui_DangNhap.isCurrentUserAdmin();
        Label lblRole = new Label(isAdmin ? "Quản lý" : "Nhân viên");
        lblRole.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 11px;");

        userInfo.getChildren().addAll(lblName, lblRole);

        HBox userBox = new HBox(10, userInfo, iconUser);
        userBox.setAlignment(Pos.CENTER_RIGHT);


        userBox.getStyleClass().add("user-box");


        // --- MENU THẢ XUỐNG (DROPDOWN) ---
        ContextMenu userMenu = new ContextMenu();

        // THAY ĐỔI 2: Áp dụng class CSS cho Menu
        userMenu.getStyleClass().add("custom-context-menu");

        // Item 1: Thông tin cá nhân
        MenuItem itemInfo = new MenuItem("Thông tin cá nhân");
        itemInfo.setOnAction(e -> showUserInfoModal());

        // Item 2: Đổi mật khẩu
        MenuItem itemChangePass = new MenuItem("Đổi mật khẩu");
        itemChangePass.setOnAction(e -> showChangePasswordModal());

        // Item 3: Đăng xuất
        MenuItem itemLogout = new MenuItem("Đăng xuất");
        // THAY ĐỔI 3: Thêm class riêng cho nút logout để chỉnh màu đỏ
        itemLogout.getStyleClass().add("logout-item");
        itemLogout.setOnAction(e -> handleLogout());

        userMenu.getItems().addAll(itemInfo, itemChangePass, new SeparatorMenuItem(), itemLogout);

        // Sự kiện Click vào UserBox
        userBox.setOnMouseClicked(e -> {
            if (!userMenu.isShowing()) {
                userBox.getStyleClass().add("active"); // Giữ màu nền đậm khi menu mở
                userMenu.show(userBox, Side.BOTTOM, 0, 5); // 5 là khoảng cách tách ra khỏi header 1 xíu
            } else {
                userMenu.hide();
            }
        });

        // Khi menu đóng lại thì bỏ trạng thái active của userBox
        userMenu.setOnHidden(e -> userBox.getStyleClass().remove("active"));

        // --- 4. THÊM VÀO HEADER: Title -> Spacer -> UserBox ---
        header.getChildren().addAll(titleLabel, spacer, userBox);

        return header;
    }


    /**
     * Hiển thị popup Đổi mật khẩu
     */
    private void showChangePasswordModal() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Đổi Mật Khẩu");
        // Xóa border mặc định của window để giao diện trông hiện đại hơn (tùy chọn)
        // dialog.initStyle(StageStyle.UTILITY);

        VBox root = new VBox(20);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: white;");
        root.setPrefWidth(400);

        // --- HEADER ---
        Label lblTitle = new Label("ĐỔI MẬT KHẨU");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        lblTitle.setTextFill(Color.web("#082744"));

        Label lblSub = new Label("Vui lòng nhập mật khẩu hiện tại và mật khẩu mới.");
        lblSub.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        lblSub.setTextFill(Color.web("#718096"));

        VBox header = new VBox(5, lblTitle, lblSub);
        header.setAlignment(Pos.CENTER_LEFT);

        // --- FORM INPUT ---
        // Khởi tạo các biến logic (giữ nguyên logic của bạn)
        PasswordField txtOldPass = new PasswordField();
        PasswordField txtNewPass = new PasswordField();
        PasswordField txtConfirmPass = new PasswordField();

        // Dùng hàm helper để tạo giao diện đẹp + chức năng toggle
        VBox form = new VBox(15);
        form.getChildren().addAll(
                new Label("Mật khẩu hiện tại"),
                createPasswordWithToggle(txtOldPass, "Nhập mật khẩu cũ..."),

                new Label("Mật khẩu mới"),
                createPasswordWithToggle(txtNewPass, "Nhập mật khẩu mới..."),

                new Label("Nhập lại mật khẩu mới"),
                createPasswordWithToggle(txtConfirmPass, "Xác nhận lại...")
        );

        // Style cho các Label tiêu đề input
        form.getChildren().filtered(node -> node instanceof Label).forEach(node -> {
            ((Label) node).setStyle("-fx-font-weight: bold; -fx-text-fill: #4A5568; -fx-font-size: 13px;");
        });

        // --- ACTION BUTTONS ---
        Button btnSave = new Button("Lưu thay đổi");
        Button btnCancel = new Button("Hủy bỏ");

        // Style nút bấm hiện đại
        String btnBaseStyle = "-fx-background-radius: 8; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20; -fx-font-size: 14px;";
        btnSave.setStyle(btnBaseStyle + "-fx-background-color: #082744; -fx-text-fill: white;");
        btnCancel.setStyle(btnBaseStyle + "-fx-background-color: #EDF2F7; -fx-text-fill: #2D3748;");

        // Hover effect
        btnSave.setOnMouseEntered(e -> btnSave.setStyle(btnBaseStyle + "-fx-background-color: #0a3d6a; -fx-text-fill: white;"));
        btnSave.setOnMouseExited(e -> btnSave.setStyle(btnBaseStyle + "-fx-background-color: #082744; -fx-text-fill: white;"));

        btnCancel.setOnMouseEntered(e -> btnCancel.setStyle(btnBaseStyle + "-fx-background-color: #E2E8F0; -fx-text-fill: #2D3748;"));
        btnCancel.setOnMouseExited(e -> btnCancel.setStyle(btnBaseStyle + "-fx-background-color: #EDF2F7; -fx-text-fill: #2D3748;"));

        // --- LOGIC XỬ LÝ (Giữ nguyên logic chuẩn của bạn) ---
        btnSave.setOnAction(e -> {
            String oldPass = txtOldPass.getText();
            String newPass = txtNewPass.getText();
            String confirmPass = txtConfirmPass.getText();

            if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng nhập đầy đủ các trường bắt buộc!");
                return;
            }

            if (!newPass.equals(confirmPass)) {
                showAlert(Alert.AlertType.WARNING, "Không khớp", "Mật khẩu xác nhận không trùng khớp!");
                return;
            }

            dao.TaiKhoan_DAO tkDAO = new dao.TaiKhoan_DAO();
            // Lấy pass từ DB (Giả sử bạn có biến nhanVienDangDung ở class ngoài)
            String currentHashInDB = tkDAO.getMatKhauByMaNV(nhanVienDangDung.getMaNhanVien());

            // Hash pass cũ nhập vào
            String oldPassHashInput = lib.SecurityUtils.encrypt(oldPass);

            if (!currentHashInDB.equals(oldPassHashInput)) {
                showAlert(Alert.AlertType.ERROR, "Sai mật khẩu", "Mật khẩu hiện tại không đúng!");
                return;
            }

            // Đổi pass
            String newPassHash = lib.SecurityUtils.encrypt(newPass);
            if (tkDAO.updateMatKhau(nhanVienDangDung.getMaNhanVien(), newPassHash)) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đổi mật khẩu thành công! Vui lòng đăng nhập lại.");
                dialog.close();
                handleLogout();
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi hệ thống", "Không thể cập nhật mật khẩu lúc này.");
            }
        });

        btnCancel.setOnAction(e -> dialog.close());

        HBox actions = new HBox(10, btnCancel, btnSave);
        actions.setAlignment(Pos.CENTER_RIGHT);
        actions.setPadding(new Insets(10, 0, 0, 0));

        root.getChildren().addAll(header, new Separator(), form, actions);

        Scene scene = new Scene(root);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private StackPane createPasswordWithToggle(PasswordField passField, String prompt) {
        // 1. Setup PasswordField gốc
        passField.setPromptText(prompt);
        passField.setStyle("-fx-background-color: transparent; -fx-padding: 10; -fx-border-width: 0;");

        // 2. Tạo TextField song song (để hiện chữ khi mở mắt)
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setManaged(false);
        textField.setVisible(false);
        textField.setStyle("-fx-background-color: transparent; -fx-padding: 10; -fx-border-width: 0;");

        // Đồng bộ nội dung giữa 2 ô
        textField.textProperty().bindBidirectional(passField.textProperty());

        // 3. Tạo Icon Mắt (SVG)
        SVGPath icon = new SVGPath();
        icon.setContent(SVG_EYE_OPEN); // Mặc định là icon mở (để bấm vào thì đóng/mở) nhưng trạng thái là đang ẩn pass
        icon.setStroke(Color.web("#718096"));
        icon.setFill(Color.TRANSPARENT);
        icon.setStrokeWidth(1.5);

        // Container cho icon để dễ bấm
        StackPane iconContainer = new StackPane(icon);
        iconContainer.setCursor(Cursor.HAND);
        iconContainer.setPadding(new Insets(0, 10, 0, 10)); // Padding phải
        iconContainer.setMaxSize(40, 40);
        StackPane.setAlignment(iconContainer, Pos.CENTER_RIGHT);

        // 4. Container CHUNG (Có viền bo tròn)
        StackPane inputContainer = new StackPane();
        inputContainer.setStyle("-fx-background-color: white; -fx-border-color: #CBD5E0; -fx-border-radius: 8; -fx-background-radius: 8;");
        inputContainer.setPrefHeight(40);

        // Xử lý sự kiện click vào mắt
        iconContainer.setOnMouseClicked(e -> {
            if (textField.isVisible()) {
                // Đang hiện chữ -> Chuyển về ẩn (Password)
                textField.setVisible(false);
                textField.setManaged(false);
                passField.setVisible(true);
                passField.setManaged(true);
                icon.setContent(SVG_EYE_OPEN); // Icon mắt bình thường
            } else {
                // Đang ẩn -> Chuyển về hiện chữ (Text)
                textField.setVisible(true);
                textField.setManaged(true);
                passField.setVisible(false);
                passField.setManaged(false);
                icon.setContent(SVG_EYE_CLOSED); // Icon mắt gạch chéo
            }
        });

        inputContainer.getChildren().addAll(passField, textField, iconContainer);
        return inputContainer;
    }

    /**
     * Hiển thị popup Thông tin cá nhân
     */
    private void showUserInfoModal() {
        // Logic hiển thị thông tin
        javafx.scene.control.Alert info = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        info.setTitle("Thông tin cá nhân");
        info.setHeaderText("Hồ sơ nhân viên");
        info.setContentText(
                "Mã NV: " + nhanVienDangDung.getMaNhanVien() + "\n" +
                        "Họ tên: " + nhanVienDangDung.getTenNhanVien() + "\n" +
                        "SĐT: " + nhanVienDangDung.getSoDienThoai() + "\n" +
                        "Chức vụ: " + (Gui_DangNhap.isCurrentUserAdmin() ? "Quản lý" : "Nhân viên")
        );
        info.showAndWait();
    }

    /**
     * Xử lý Đăng xuất (Gọi lại logic bên Sidebar hoặc tự xử lý)
     */
    private void handleLogout() {
        // Tận dụng lại logic logout chuẩn (bao gồm kiểm tra kết ca)
        // Vì logic này nằm bên Sidebar, ta có thể gọi thông qua đối tượng sideBar nếu nó có hàm public
        // Hoặc copy logic logout vào đây

        // Cách nhanh nhất: Gọi hàm logout của sidebar (cần sửa Gui_Sidebar để public hàm handleLogout)
        // Hoặc code lại đơn giản:
        javafx.stage.Stage currentStage = (javafx.stage.Stage) this.getScene().getWindow();
        currentStage.close();

        try {
            new Gui_DangNhap().start(new javafx.stage.Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tạo một thẻ hiển thị số liệu thống kê.
     *
     * @param title    Tiêu đề thống kê
     * @param value    Giá trị
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

    public void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
