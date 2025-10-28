package gui;

import dao.DangNhap_DAO;
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

public class GiaoDienDangNhap extends Application {

    private static final String SPICES_IMAGE_PATH = "/img/spices_background.jpg";
    private static final String USER_ICON_PATH = "/img/user_icon.png";
    private static final String LOCK_ICON_PATH = "/img/lock_icon.png";

    // Lưu thông tin đăng nhập
    private static String currentUsername;
    private static String currentMaNhanVien;
    private static boolean currentIsAdmin;

    private HBox createInputControl(String promptText, boolean isPassword, String iconPath) {
        StackPane inputStack = new StackPane();
        Control inputControl;

        if (isPassword) {
            PasswordField passwordField = new PasswordField();
            passwordField.setPromptText(promptText);
            inputControl = passwordField;
        } else {
            TextField textField = new TextField();
            textField.setPromptText(promptText);
            inputControl = textField;
        }

        inputControl.setPrefWidth(300);
        inputControl.setPrefHeight(45);
        inputControl.setMaxWidth(300);

        inputControl.setStyle(
                "-fx-background-radius: 20;" +
                        "-fx-background-color: white;" +
                        "-fx-border-color: #A9A9A9;" +
                        "-fx-border-width: 0.5;" +
                        "-fx-border-radius: 20;" +
                        "-fx-padding: 10 40 10 20;" +
                        "-fx-font-family: 'Arial';" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: normal;"
        );

        ImageView iconView = new ImageView();
        try {
            iconView.setImage(new Image(getClass().getResource(iconPath).toExternalForm()));
            iconView.setFitWidth(18);
            iconView.setFitHeight(18);

            StackPane.setAlignment(iconView, Pos.CENTER_RIGHT);
            StackPane.setMargin(iconView, new Insets(0, 15, 0, 0));

            inputStack.getChildren().addAll(inputControl, iconView);
        } catch (Exception e) {
            inputStack.getChildren().add(inputControl);
        }

        HBox container = new HBox(inputStack);
        container.setAlignment(Pos.CENTER);
        return container;
    }

    private void openMainScreen(Stage currentStage) {
        try {
            currentStage.close();

            Stage mainStage = new Stage();
            TrangChu root = new TrangChu();
            Scene scene = new Scene(root);

            try {
                scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
            } catch (Exception e) {
                System.out.println("Không tìm thấy file CSS");
            }

            mainStage.setTitle("Quản Lý Nhà Hàng - Xin chào: " + currentUsername);
            mainStage.setMaximized(true);
            mainStage.setScene(scene);
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

        HBox usernameBox = createInputControl("Tên đăng nhập", false, USER_ICON_PATH);
        HBox passwordBox = createInputControl("Mật khẩu", true, LOCK_ICON_PATH);

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

        // XỬ LÝ ĐĂNG NHẬP
//        btnLogin.setOnAction(e -> {
//            String username = usernameField.getText().trim();
//            String password = passwordField.getText().trim();
//
//            if (username.isEmpty() || password.isEmpty()) {
//                Alert alert = new Alert(Alert.AlertType.WARNING);
//                alert.setTitle("Cảnh báo");
//                alert.setHeaderText(null);
//                alert.setContentText("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!");
//                alert.showAndWait();
//                return;
//            }
//
//            try {
//                DangNhap_DAO dangNhapDAO = new DangNhap_DAO();
//
//                if (dangNhapDAO.authenticate(username, password)) {
//                    currentUsername = username;
//                    currentIsAdmin = dangNhapDAO.isAdmin(username);
//                    currentMaNhanVien = dangNhapDAO.getMaNhanVien(username);
//
//                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                    alert.setTitle("Đăng nhập thành công");
//                    alert.setHeaderText(null);
//                    alert.setContentText("Chào mừng " + (currentIsAdmin ? "Quản trị viên" : "Nhân viên") +
//                            "\nMã nhân viên: " + currentMaNhanVien);
//                    alert.showAndWait();
//
//                    openMainScreen(stage);
//
//                } else {
//                    Alert alert = new Alert(Alert.AlertType.ERROR);
//                    alert.setTitle("Đăng nhập thất bại");
//                    alert.setHeaderText(null);
//                    alert.setContentText("Tên đăng nhập hoặc mật khẩu không đúng!");
//                    alert.showAndWait();
//
//                    passwordField.clear();
//                    usernameField.requestFocus();
//                }
//
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("Lỗi kết nối");
//                alert.setHeaderText(null);
//                alert.setContentText("Không thể kết nối đến cơ sở dữ liệu!\n" + ex.getMessage());
//                alert.showAndWait();
//            }
//        });
// XỬ LÝ ĐĂNG NHẬP
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
                    currentUsername = username;
                    currentIsAdmin = dangNhapDAO.isAdmin(username);
                    currentMaNhanVien = dangNhapDAO.getMaNhanVien(username);
                    stage.hide();
                    // ✅ HIỂN THỊ MODAL VÀO CA
                    VaoCaModal vaoCaModal = new VaoCaModal(currentUsername, currentMaNhanVien);
                    vaoCaModal.showAndWait();

                    // Kiểm tra xem user đã xác nhận vào ca chưa
                    if (vaoCaModal.isConfirmed()) {
                        long tienDauCa = vaoCaModal.getTongTienDauCa();

                        System.out.println("✅ Vào ca thành công!");
                        System.out.println("💰 Tiền đầu ca: " + String.format("%,d VND", tienDauCa));

                        // TODO: Lưu thông tin vào ca vào database
                        // VaoCa_DAO.luuThongTinVaoCa(currentMaNhanVien, tienDauCa);

                        // Mở màn hình chính
                        openMainScreen(stage);
                    } else {
                        // Nếu chưa xác nhận, không cho vào hệ thống
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Chưa vào ca");
                        alert.setHeaderText(null);
                        alert.setContentText("Bạn cần xác nhận vào ca để sử dụng hệ thống!");
                        alert.showAndWait();
                    }

                } else {
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
                alert.setTitle("Lỗi kết nối");
                alert.setHeaderText(null);
                alert.setContentText("Không thể kết nối đến cơ sở dữ liệu!\n" + ex.getMessage());
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