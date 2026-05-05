package client.gui;

import client.service.CaClient;
import client.service.DangNhapClient;
import client.service.NhanVienClient;
import common.entity.Ca;
import common.entity.NhanVien;
import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.stage.StageStyle;
import javafx.util.Pair;
import client.utils.SecurityUtils;
import client.utils.EmailService;

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
            java.net.URL iconUrl = getClass().getResource(iconPath);
            if (iconUrl != null) {
                ImageView leftIconView = new ImageView(new Image(iconUrl.toExternalForm()));
                leftIconView.setFitWidth(18);
                leftIconView.setFitHeight(18);

                StackPane.setAlignment(leftIconView, Pos.CENTER_LEFT);
                StackPane.setMargin(leftIconView, new Insets(0, 0, 0, 15));

                inputStack.getChildren().add(leftIconView); // Thêm icon trái
            } else {
                System.err.println("⚠️ Không tìm thấy icon trái: " + iconPath);
            }

        } catch (Exception e) {
            System.err.println("❌ Lỗi tải icon trái: " + iconPath);
        }

        // ⭐️ Xử lý thêm icon mắt (bên phải) NẾU là password
        if (isPassword) {
            try {
                java.net.URL closedUrl = getClass().getResource(EYE_CLOSED_PATH);
                java.net.URL openUrl = getClass().getResource(EYE_OPEN_PATH);

                if (closedUrl != null && openUrl != null) {
                    Image eyeClosedImg = new Image(closedUrl.toExternalForm());
                    Image eyeOpenImg = new Image(openUrl.toExternalForm());
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
                } else {
                    System.err.println("⚠️ Không tìm thấy icon mắt.");
                }

            } catch (Exception e) {
                System.err.println("❌ Không thể tải icon mắt: " + e.getMessage());
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
                java.net.URL cssUrl = getClass().getResource("/client/application/application.css");
                if (cssUrl != null) {
                    scene.getStylesheets().add(cssUrl.toExternalForm());
                } else {
                    System.err.println("⚠️ Không tìm thấy file CSS: /client/application/application.css");
                }
            } catch (Exception e) {
                System.err.println("⚠️ Lỗi khi load CSS: " + e.getMessage());
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
                    CaClient caClient = new CaClient();
                    Ca caDangLam = caClient.getCaDangLam(maNhanVienHienTai);

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
//        forgotPassword.setOnAction(e -> {
//            Dialog<Pair<String, String>> dialog = new Dialog<>();
//            dialog.setTitle("Quên mật khẩu");
//            dialog.setHeaderText("Nhập thông tin để cấp lại mật khẩu");
//
//            ButtonType loginButtonType = new ButtonType("Gửi mật khẩu", ButtonBar.ButtonData.OK_DONE);
//            dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
//
//            GridPane grid = new GridPane();
//            grid.setHgap(10);
//            grid.setVgap(10);
//            grid.setPadding(new Insets(20, 150, 10, 10));
//
//            TextField usernameFieldForgot = new TextField();
//            usernameFieldForgot.setPromptText("Tên đăng nhập");
//            TextField emailField = new TextField();
//            emailField.setPromptText("Email nhận mật khẩu");
//
//            grid.add(new Label("Tên đăng nhập:"), 0, 0);
//            grid.add(usernameFieldForgot, 1, 0);
//            grid.add(new Label("Email nhận:"), 0, 1);
//            grid.add(emailField, 1, 1);
//
//            dialog.getDialogPane().setContent(grid);
//
//            // Convert kết quả khi bấm nút
//            dialog.setResultConverter(dialogButton -> {
//                if (dialogButton == loginButtonType) {
//                    return new Pair<>(usernameFieldForgot.getText(), emailField.getText());
//                }
//                return null;
//            });
//
//            dialog.showAndWait().ifPresent(result -> {
//                String username = result.getKey();
//                String emailTo = result.getValue();
//
//                if (username.isEmpty() || emailTo.isEmpty()) {
//                    showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng nhập đủ thông tin!");
//                    return;
//                }
//
//                client.service.TaiKhoanClient taiKhoanClient = new client.service.TaiKhoanClient();
//                client.service.DangNhapClient dangNhapClient = new client.service.DangNhapClient();
//
//                // 1. Kiểm tra username có tồn tại không
//                // (Lưu ý: Dùng hàm có sẵn của bạn để lấy mã NV từ username)
//                String maNV = null;
//                try {
//                    maNV = dangNhapClient.getMaNhanVien(username);
//                } catch (Exception ex) { ex.printStackTrace(); }
//
//                if (maNV == null) {
//                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Tên đăng nhập không tồn tại!");
//                    return;
//                }
//
//                // 2. Sinh mật khẩu mới và mã hóa
//                String passMoi = client.utils.EmailService.generateRandomPass(); // Ví dụ 123456
//                String passMoiHash = client.utils.SecurityUtils.encrypt(passMoi);
//
//                // 3. Hiển thị Loading
//                Alert loading = new Alert(Alert.AlertType.INFORMATION);
//                loading.setTitle("Xin chờ");
//                loading.setHeaderText("Đang gửi email...");
//                loading.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
//                loading.show();
//
//                String finalMaNV = maNV;
//                new Thread(() -> {
//                    // Cập nhật mật khẩu mới vào DB
//                    // (Bạn dùng hàm updateMatKhau đã viết ở câu trước)
//                    boolean updateOK = taiKhoanClient.updateMatKhau(finalMaNV, passMoiHash);
//
//                    if (updateOK) {
//                        // Gửi mail vào địa chỉ người dùng vừa nhập (Bất chấp đúng sai)
//                        boolean sendOK = client.utils.EmailService.sendEmail(emailTo, passMoi);
//
//                        Platform.runLater(() -> {
//                            loading.close();
//                            if (sendOK) {
//                                showAlert(Alert.AlertType.INFORMATION, "Thành công",
//                                        "Mật khẩu mới cho tài khoản '" + username + "' đã gửi tới: " + emailTo);
//                            } else {
//                                showAlert(Alert.AlertType.ERROR, "Lỗi mạng", "Không thể gửi email.");
//                            }
//                        });
//                    }
//                }).start();
//            });
//        });
        forgotPassword.setOnAction(e -> {
            // 1. Tạo Dialog nhập liệu
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Quên mật khẩu");
            dialog.setHeaderText("Nhập thông tin để cấp lại mật khẩu");

            ButtonType loginButtonType = new ButtonType("Gửi mật khẩu", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField usernameFieldForgot = new TextField();
            usernameFieldForgot.setPromptText("Tên đăng nhập");
            TextField emailField = new TextField();
            emailField.setPromptText("Email nhận mật khẩu");

            grid.add(new Label("Tên đăng nhập:"), 0, 0);
            grid.add(usernameFieldForgot, 1, 0);
            grid.add(new Label("Email nhận:"), 0, 1);
            grid.add(emailField, 1, 1);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == loginButtonType) {
                    return new Pair<>(usernameFieldForgot.getText(), emailField.getText());
                }
                return null;
            });

            dialog.showAndWait().ifPresent(result -> {
                String username = result.getKey();
                String emailTo = result.getValue();

                if (username.isEmpty() || emailTo.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng nhập đủ thông tin!");
                    alert.showAndWait();
                    return;
                }

                client.service.TaiKhoanClient taiKhoanClient = new client.service.TaiKhoanClient();
                client.service.DangNhapClient dangNhapClient = new client.service.DangNhapClient();

                String maNV = null;
                try {
                    maNV = dangNhapClient.getMaNhanVien(username);
                } catch (Exception ex) { ex.printStackTrace(); }

                if (maNV == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Tên đăng nhập không tồn tại!");
                    alert.showAndWait();
                    return;
                }
                Dialog<Void> loading = new Dialog<>();
                loading.initStyle(StageStyle.UNDECORATED);

                // SỬA LỖI NULL POINTER: Lấy window từ nút forgotPassword thay vì dialog đã đóng
                loading.initOwner(forgotPassword.getScene().getWindow());

                ProgressIndicator pi = new ProgressIndicator();
                Label lblLoad = new Label("Đang gửi đến email...");
                lblLoad.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

                HBox boxLoad = new HBox(15, pi, lblLoad);
                boxLoad.setPadding(new Insets(25));
                boxLoad.setAlignment(Pos.CENTER);
                // Style đẹp y chang bạn gửi
                boxLoad.setStyle("-fx-background-color: white; -fx-border-color: #E2E8F0; -fx-border-width: 1;");

                loading.getDialogPane().setContent(boxLoad);

                // Ẩn nút mặc định của Dialog loading
                loading.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
                loading.getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);

                String finalMaNV = maNV;

                new Thread(() -> {
                    boolean ok = false;
                    try {
                        // Giả vờ ngủ 0.5s cho giống hiệu ứng xoay (tùy chọn)
                        Thread.sleep(500);

                        // --- LOGIC GỬI EMAIL ---
                        // 1. Sinh mật khẩu
                        String passMoi = EmailService.generateRandomPass();
                        String passMoiHash = client.utils.SecurityUtils.encrypt(passMoi);

                        // 2. Cập nhật SQL
                        boolean updateOK = taiKhoanClient.updateMatKhau(finalMaNV, passMoiHash);

                        // 3. Gửi Mail
                        if (updateOK) {
                            ok = EmailService.sendEmail(emailTo, passMoi);
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        ok = false;
                    } finally {
                        boolean finalOk = ok;
                        Platform.runLater(() -> {
                            // Tắt xoay xoay
                            loading.setResult(null);
                            loading.close();

                            if (finalOk) {
                                Alert success = new Alert(Alert.AlertType.INFORMATION,
                                        "Mật khẩu mới đã được gửi tới: " + emailTo);
                                // Sửa lỗi Owner cho Alert thành công luôn
                                success.initOwner(forgotPassword.getScene().getWindow());
                                success.showAndWait();
                            } else {
                                Alert error = new Alert(Alert.AlertType.ERROR, "Lỗi hệ thống. Không thể gửi email.");
                                error.initOwner(forgotPassword.getScene().getWindow());
                                error.show();
                            }
                        });
                    }
                }).start();

                loading.show();
            });
        });
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
                "-fx-background-radius: 20; -fx-font-weight: bold; -fx-font-size: 16px; "));
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
                DangNhapClient dangNhapClient = new DangNhapClient();
                String hassPassword = SecurityUtils.encrypt(password);
                if (dangNhapClient.authenticate(username, hassPassword)) {
                    // 1. Kiểm tra tài khoản bị khóa
                    if (!dangNhapClient.isTaiKhoanHoatDong(username)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Tài khoản bị khóa");
                        alert.setHeaderText("Đăng nhập bị từ chối");
                        alert.setContentText("Tài khoản này đã bị vô hiệu hóa.\nVui lòng liên hệ quản lý.");
                        alert.showAndWait();
                        return;
                    }

                    // 2. Lấy thông tin cơ bản
                    String maNvVuaDangNhap = dangNhapClient.getMaNhanVien(username);
                    String tenNvVuaDangNhap = username;
                    boolean laAdmin = dangNhapClient.isAdmin(username);

                    NhanVien nhanVien = new NhanVienClient().getNhanVienByMa(maNvVuaDangNhap);

                    // Lưu session
                    currentUsername = tenNvVuaDangNhap;
                    currentIsAdmin = laAdmin;
                    currentMaNhanVien = maNvVuaDangNhap;

                    // ✅✅✅ LOGIC PHÂN QUYỀN VÀO CA ✅✅✅

                    if (laAdmin) {
                        stage.hide();
                        openMainScreen(stage, nhanVien);
                    } else {
                        CaClient caClient = new CaClient();
                        Ca caBiTreo = caClient.getCaDangMo();

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
                            "-fx-background-size: 200% 100%;" +
                            "-fx-background-radius: 0 " + ARC_RADIUS + " " + ARC_RADIUS + " 0;" +
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
