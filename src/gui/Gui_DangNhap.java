package gui;

import dao.Ca_DAO;
import dao.DangNhap_DAO;
import dao.NhanVien_DAO;
import entity.Ca;
import entity.NhanVien;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class Gui_DangNhap extends Application {

    private static final String SPICES_IMAGE_PATH = "/img/spices_background.jpg";
    private static final String USER_ICON_PATH = "/img/user_icon.png";
    private static final String LOCK_ICON_PATH = "/img/lock_icon.png";
    private static final String EYE_CLOSED_PATH = "/img/eye_closed.png";
    private static final String EYE_OPEN_PATH = "/img/eye_open.png";
    // Lưu thông tin đăng nhập
    private static String currentUsername;
    private static String currentMaNhanVien;
    private static boolean currentIsAdmin;

//    private HBox createInputControl(String iconPath, boolean isPassword, String promptText) {
//        StackPane inputStack = new StackPane();
//        Control inputControl;
//
//        if (isPassword) {
//            PasswordField passwordField = new PasswordField();
//            passwordField.setPromptText(promptText);
//            inputControl = passwordField;
//        } else {
//            TextField textField = new TextField();
//            textField.setPromptText(promptText);
//            inputControl = textField;
//        }
//
//        inputControl.setPrefWidth(300);
//        inputControl.setPrefHeight(45);
//        inputControl.setMaxWidth(300);
//
//        inputControl.setStyle(
//                "-fx-background-radius: 20;" +
//                        "-fx-background-color: white;" +
//                        "-fx-border-color: #A9A9A9;" +
//                        "-fx-border-width: 0.5;" +
//                        "-fx-border-radius: 20;" +
//                        "-fx-padding: 10 20 10 40;" +
//                        "-fx-font-family: 'Arial';" +
//                        "-fx-font-size: 16px;" +
//                        "-fx-font-weight: normal;"
//        );
//
//        ImageView iconView = new ImageView();
//        try {
//            iconView.setImage(new Image(getClass().getResource(iconPath).toExternalForm()));
//            iconView.setFitWidth(18);
//            iconView.setFitHeight(18);
//
//            StackPane.setAlignment(iconView, Pos.CENTER_LEFT);
//            StackPane.setMargin(iconView, new Insets(0, 0, 0, 15));
//
//            inputStack.getChildren().addAll(inputControl, iconView);
//        } catch (Exception e) {
//            inputStack.getChildren().add(inputControl);
//        }
//
//        HBox container = new HBox(inputStack);
//        container.setAlignment(Pos.CENTER);
//        return container;
//    }

private HBox createInputControl(String iconPath, boolean isPassword, String promptText) {
    StackPane inputStack = new StackPane();
    Control inputControl;

    // ⭐️ Biến lưu padding, sẽ thay đổi nếu là password
    String paddingStyle;

    // ⭐️ Cần một TextField để hiển thị mật khẩu khi nhấn vào mắt
    TextField visiblePasswordField = null;

    if (isPassword) {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(promptText);
        inputControl = passwordField; // ⭐️ Đây là control chính (ẩn)

        // ⭐️ Tạo một TextField "song sinh" để hiển thị mật khẩu
        visiblePasswordField = new TextField();
        visiblePasswordField.setPromptText(promptText);
        visiblePasswordField.setVisible(false); // Ẩn nó đi lúc đầu

        // ⭐️ Liên kết nội dung của 2 trường: gõ ở 1 bên, bên kia tự cập nhật
        visiblePasswordField.textProperty().bindBidirectional(passwordField.textProperty());

        // ⭐️ Cần chừa 40px ở cả 2 bên (trái cho khóa, phải cho mắt)
        paddingStyle = "-fx-padding: 10 40 10 40;";

    } else {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        inputControl = textField;

        // ⭐️ Chỉ cần chừa 40px bên trái cho icon user
        paddingStyle = "-fx-padding: 10 20 10 40;";
    }

    inputControl.setPrefWidth(300);
    inputControl.setPrefHeight(45);
    inputControl.setMaxWidth(300);

    // ⭐️ Tách style ra để dùng chung
    String baseStyle = "-fx-background-radius: 20;" +
            "-fx-background-color: white;" +
            "-fx-border-color: #A9A9A9;" +
            "-fx-border-width: 0.5;" +
            "-fx-border-radius: 20;" +
            "-fx-font-family: 'Arial';" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: normal;";

    // ⭐️ Áp dụng style với padding tương ứng
    inputControl.setStyle(baseStyle + paddingStyle);

    // ⭐️ Thêm control chính vào stack (nó sẽ là child 0)
    inputStack.getChildren().add(inputControl);

    if (visiblePasswordField != null) {
        // ⭐️ Áp dụng style cho cả trường "song sinh"
        visiblePasswordField.setStyle(baseStyle + paddingStyle);
        visiblePasswordField.setPrefSize(300, 45);
        visiblePasswordField.setMaxWidth(300);

        // ⭐️ Thêm trường "song sinh" vào stack
        inputStack.getChildren().add(visiblePasswordField);
    }

    // ⭐️ Xử lý icon bên trái (User/Lock)
    try {
        ImageView leftIconView = new ImageView(new Image(getClass().getResource(iconPath).toExternalForm()));
        leftIconView.setFitWidth(18);
        leftIconView.setFitHeight(18);

        StackPane.setAlignment(leftIconView, Pos.CENTER_LEFT);
        StackPane.setMargin(leftIconView, new Insets(0, 0, 0, 15));

        inputStack.getChildren().add(leftIconView); // Thêm icon trái

    } catch (Exception e) {
        System.err.println("Không tải được icon trái: " + iconPath);
    }

    // ⭐️ Xử lý thêm icon mắt (bên phải) NẾU là password
    if (isPassword) {
        try {
            Image eyeClosedImg = new Image(getClass().getResource(EYE_CLOSED_PATH).toExternalForm());
            Image eyeOpenImg = new Image(getClass().getResource(EYE_OPEN_PATH).toExternalForm());
            ImageView eyeIconView = new ImageView(eyeClosedImg); // Ban đầu là nhắm mắt
            eyeIconView.setFitWidth(18);
            eyeIconView.setFitHeight(18);
            eyeIconView.setCursor(Cursor.HAND);

            // ⭐️ Căn lề phải
            StackPane.setAlignment(eyeIconView, Pos.CENTER_RIGHT);
            StackPane.setMargin(eyeIconView, new Insets(0, 15, 0, 0));

            // ⭐️ Logic bấm vào mắt
            // Cần khai báo final để dùng trong lambda
            final Control finalInputControl = inputControl;
            final TextField finalVisiblePasswordField = visiblePasswordField;

            eyeIconView.setOnMouseClicked(e -> {
                if (finalVisiblePasswordField.isVisible()) {
                    // Đang hiện -> Giấu đi
                    finalVisiblePasswordField.setVisible(false);
                    finalInputControl.setVisible(true); // Hiện PasswordField
                    eyeIconView.setImage(eyeClosedImg);
                } else {
                    // Đang giấu -> Hiện lên
                    finalVisiblePasswordField.setVisible(true);
                    finalInputControl.setVisible(false); // Giấu PasswordField
                    eyeIconView.setImage(eyeOpenImg);
                }
            });

            // ⭐️ Thêm icon mắt vào stack
            inputStack.getChildren().add(eyeIconView);

        } catch (Exception e) {
            System.err.println("Không thể tải icon mắt: " + e.getMessage());
        }
    }

    HBox container = new HBox(inputStack);
    container.setAlignment(Pos.CENTER);
    return container;
}

private void openMainScreen(Stage currentStage, NhanVien nhanVien) {
    try {
        currentStage.close();

        Stage mainStage = new Stage();
        Gui_TrangChu root = new Gui_TrangChu(nhanVien);
        Scene scene = new Scene(root);

        try {
            scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("Không tìm thấy file CSS");
        }

        mainStage.setTitle("Quản Lý Nhà Hàng - Xin chào: " + currentUsername);
        mainStage.setMaximized(true);
        mainStage.setScene(scene);

        // ⭐️⭐️⭐️ ĐOẠN CODE QUAN TRỌNG ĐÂY ⭐️⭐️⭐️
        // Bắt sự kiện khi người dùng bấm nút X để đóng cửa sổ
        mainStage.setOnCloseRequest(event -> {
            try {
                // 1. Lấy mã nhân viên hiện tại
                String maNhanVienHienTai = getCurrentMaNhanVien();
                if (maNhanVienHienTai == null || maNhanVienHienTai.isEmpty()) {
                    // Nếu không có thông tin đăng nhập (lạ), cứ cho đóng
                    return;
                }

                // 2. Kiểm tra xem nhân viên này còn ca đang làm không
                Ca_DAO caDAO = new Ca_DAO();
                Ca caDangLam = caDAO.getCaDangLam(maNhanVienHienTai);

                if (caDangLam != null) {
                    // 3. NẾU CÒN CA -> KHÔNG CHO ĐÓNG
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Chưa kết ca");
                    alert.setHeaderText("Bạn chưa kết ca làm việc!");
                    alert.setContentText("Vui lòng vào mục 'Kết Ca' để hoàn tất ca của bạn trước khi tắt ứng dụng.");
                    alert.showAndWait();

                    // 4. Hủy sự kiện đóng cửa sổ (quan trọng nhất)
                    event.consume();

                } else {
                    // 5. NẾU ĐÃ KẾT CA -> CHO ĐÓNG BÌNH THƯỜNG
                    // Không làm gì cả, cửa sổ sẽ tự đóng
                }

            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText("Lỗi khi kiểm tra ca làm việc");
                alert.setContentText("Không thể xác minh trạng thái ca: " + e.getMessage());
                alert.showAndWait();
                // Hủy sự kiện để an toàn, phòng lỗi
                event.consume();
            }
        });
        // ⭐️⭐️⭐️ HẾT ĐOẠN CODE MỚI ⭐️⭐️⭐️

        mainStage.show();

    } catch (Exception e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText("Không thể mở màn hình chính!\n" + e.getMessage());
        alert.showAndWait();
    }
}
    @Override
    public void start(Stage stage) {
        double IMAGE_WIDTH = 380;
        double FORM_WIDTH = 450;
        double TOTAL_HEIGHT = 550;
        double ARC_RADIUS = 50;

        Label lblLogin = new Label("ĐĂNG NHẬP");
        lblLogin.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        lblLogin.setTextFill(Color.BLACK);

        HBox usernameBox = createInputControl(USER_ICON_PATH, false, "Tên đăng nhập");
        HBox passwordBox = createInputControl(LOCK_ICON_PATH, true, "Mật khẩu");

        TextField usernameField = (TextField) ((StackPane) usernameBox.getChildren().get(0)).getChildren().get(0);
        PasswordField passwordField = (PasswordField) ((StackPane) passwordBox.getChildren().get(0)).getChildren().get(0);

        Hyperlink forgotPassword = new Hyperlink("Quên mật khẩu?");
        forgotPassword.setTextFill(Color.BLACK);
        forgotPassword.setFont(Font.font("Arial", FontWeight.NORMAL, 12));

        HBox forgotPassContainer = new HBox(forgotPassword);
        forgotPassContainer.setPrefWidth(300);
        forgotPassContainer.setAlignment(Pos.CENTER_RIGHT);

        Button btnLogin = new Button("Đăng nhập");
        btnLogin.setPrefWidth(300);
        btnLogin.setPrefHeight(45);
        btnLogin.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        btnLogin.setStyle(
                "-fx-background-color: #0A2940;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 20;"
        );
        btnLogin.setCursor(Cursor.HAND);
        btnLogin.setOnMouseEntered(e -> btnLogin.setStyle("-fx-background-color: #123E63; -fx-text-fill: white; " +
                "-fx-background-radius: 20; -fx-font-weight: bold; -fx-font-size: 16px; " ));
        btnLogin.setOnMouseExited(e -> btnLogin.setStyle("-fx-background-color: #0A2940; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-weight: bold; -fx-font-size: 16px;"));
        btnLogin.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!");
                alert.showAndWait();
                return;
            }

            try {
                DangNhap_DAO dangNhapDAO = new DangNhap_DAO();

                if (dangNhapDAO.authenticate(username, password)) {
                    // 1. Kiểm tra tài khoản bị khóa
                    if (!dangNhapDAO.isTaiKhoanHoatDong(username)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Tài khoản bị khóa");
                        alert.setHeaderText("Đăng nhập bị từ chối");
                        alert.setContentText("Tài khoản này đã bị vô hiệu hóa.\nVui lòng liên hệ quản lý.");
                        alert.showAndWait();
                        return;
                    }

                    // 2. Lấy thông tin cơ bản
                    String maNvVuaDangNhap = dangNhapDAO.getMaNhanVien(username);
                    String tenNvVuaDangNhap = username; // Nên lấy tên thật nếu có thể
                    boolean laAdmin = dangNhapDAO.isAdmin(username);

                    NhanVien nhanVien = NhanVien_DAO.getNhanVienByMa(maNvVuaDangNhap);

                    // Lưu session
                    currentUsername = tenNvVuaDangNhap;
                    currentIsAdmin = laAdmin;
                    currentMaNhanVien = maNvVuaDangNhap;

                    // ✅✅✅ LOGIC PHÂN QUYỀN VÀO CA ✅✅✅

                    if (laAdmin) {
                        stage.hide();
                        openMainScreen(stage, nhanVien);
                    } else {
                        Ca_DAO caDAO = new Ca_DAO();
                        Ca caBiTreo = caDAO.getCaDangMo();

                        if (caBiTreo != null) {
                            // A. CÓ CA ĐANG MỞ (TREO)
                            String maNvCuaCaTreo = caBiTreo.getMaNhanVien().getMaNhanVien();

                            if (maNvCuaCaTreo.equals(maNvVuaDangNhap)) {
                                // Chính nhân viên này đang làm dở -> Vào lại tiếp tục
                                stage.hide();
                                openMainScreen(stage, nhanVien);
                            } else {
                                // Nhân viên KHÁC chưa kết ca -> Chặn
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Lỗi Ca Làm Việc");
                                alert.setHeaderText("Ca trước chưa được kết thúc!");
                                alert.setContentText("Nhân viên [" + maNvCuaCaTreo + "] chưa kết ca.\n" +
                                        "Vui lòng yêu cầu nhân viên đó đăng nhập và KẾT CA trước.");
                                alert.showAndWait();
                                // Reset session vì bị chặn
                                currentUsername = null;
                                currentMaNhanVien = null;
                                currentIsAdmin = false;
                            }

                        } else {
                            // B. KHÔNG CÓ CA NÀO TREO -> VÀO CA MỚI
                            stage.hide();
                            Gui_VaoCa vaoCaModal = new Gui_VaoCa(nhanVien);
                            vaoCaModal.showAndWait();

                            if (vaoCaModal.isConfirmed()) {
                                openMainScreen(stage, nhanVien);
                            } else {
                                // Hủy vào ca -> Quay lại đăng nhập
                                stage.show();
                                currentUsername = null;
                                currentMaNhanVien = null;
                                currentIsAdmin = false;
                            }
                        }
                    }
                    // ✅✅✅ KẾT THÚC LOGIC MỚI ✅✅✅

                } else {
                    // Đăng nhập sai pass
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Đăng nhập thất bại");
                    alert.setHeaderText(null);
                    alert.setContentText("Tên đăng nhập hoặc mật khẩu không đúng!");
                    alert.showAndWait();
                    passwordField.clear();
                    usernameField.requestFocus();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi hệ thống");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        });
        passwordField.setOnAction(e -> btnLogin.fire());

        VBox formContent = new VBox(25);
        formContent.getChildren().addAll(lblLogin, usernameBox, passwordBox, forgotPassContainer, btnLogin);
        formContent.setAlignment(Pos.CENTER);
        formContent.setPadding(new Insets(50, 40, 50, 40));
        formContent.setPrefSize(FORM_WIDTH, TOTAL_HEIGHT);
        formContent.setMaxSize(FORM_WIDTH, TOTAL_HEIGHT);

        StackPane formPane = new StackPane(formContent);
        formPane.setStyle("-fx-background-color: white;");

        StackPane paneHinhAnh = new StackPane();
        paneHinhAnh.setPrefSize(IMAGE_WIDTH, TOTAL_HEIGHT);
        paneHinhAnh.setMaxSize(IMAGE_WIDTH, TOTAL_HEIGHT);

        try {
            paneHinhAnh.setStyle(
                    "-fx-background-image: url('" + getClass().getResource(SPICES_IMAGE_PATH).toExternalForm() + "');" +
                            "-fx-background-repeat: no-repeat;" +
                            "-fx-background-position: right center;" +
                            "-fx-background-size: 200% 100%;"+
                            "-fx-background-radius: 0 " + ARC_RADIUS + " " + ARC_RADIUS + " 0;"+
                            "-fx-background-insets: 0;"
            );
        } catch (Exception e) {
            paneHinhAnh.setStyle("-fx-background-color: #333333; -fx-background-radius: 0 " + ARC_RADIUS + " " + ARC_RADIUS + " 0;");
        }

        HBox tempContainer = new HBox(paneHinhAnh, formPane);
        tempContainer.setAlignment(Pos.CENTER_LEFT);
        tempContainer.setPrefSize(IMAGE_WIDTH + FORM_WIDTH, TOTAL_HEIGHT);

        StackPane mainContainer = new StackPane(tempContainer);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPrefSize(IMAGE_WIDTH + FORM_WIDTH, TOTAL_HEIGHT);

        StackPane root = new StackPane(mainContainer);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: #F0F0F0;");

        Scene scene = new Scene(root, 900, 550);
        stage.setTitle("Đăng nhập - Quản Lý Nhà Hàng");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        usernameField.requestFocus();
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static String getCurrentMaNhanVien() {
        return currentMaNhanVien;
    }

    public static boolean isCurrentUserAdmin() {
        return currentIsAdmin;
    }

    public static void main(String[] args) {
        launch();
    }
}