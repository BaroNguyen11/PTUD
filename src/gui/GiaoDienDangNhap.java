package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class GiaoDienDangNhap extends Application {

    // Đường dẫn giả định cho hình ảnh gia vị (thay thế bằng đường dẫn thực tế nếu có)
    private static final String SPICES_IMAGE_PATH = "/img/spices_background.jpg";
    // Đường dẫn giả định cho icons (thay thế bằng đường dẫn thực tế nếu có)
    private static final String USER_ICON_PATH = "/img/user_icon.png";
    private static final String LOCK_ICON_PATH = "/img/lock_icon.png";

    /**
     * Phương thức tạo HBox chứa TextField/PasswordField và icon.
     */
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

        // Cấu hình Style và Font qua CSS
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

    @Override
    public void start(Stage stage) {

        // ==========================================================
        // KÍCH THƯỚC VÀ CĂN CHỈNH
        // ==========================================================
        double IMAGE_WIDTH = 380;
        double FORM_WIDTH = 450;
        double TOTAL_HEIGHT = 550;
        double ARC_RADIUS = 50; // Bán kính bo góc

        // ==========================================================
        // 1. KHỐI ĐĂNG NHẬP (NỀN TRẮNG) - DÙNG CHO CĂN CHỈNH
        // ==========================================================

        // Tiêu đề ĐĂNG NHẬP
        Label lblLogin = new Label("ĐĂNG NHẬP");
        lblLogin.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        lblLogin.setTextFill(Color.BLACK);

        // Form nhập liệu
        HBox usernameBox = createInputControl("Tên đăng nhập", false, USER_ICON_PATH);
        HBox passwordBox = createInputControl("Mật khẩu", true, LOCK_ICON_PATH);

        // Link quên mật khẩu
        Hyperlink forgotPassword = new Hyperlink("Quên mật khẩu?");
        forgotPassword.setTextFill(Color.BLACK);
        forgotPassword.setFont(Font.font("Arial", FontWeight.NORMAL, 12));

        HBox forgotPassContainer = new HBox(forgotPassword);
        forgotPassContainer.setPrefWidth(300);
        forgotPassContainer.setAlignment(Pos.CENTER_RIGHT);

        // Nút đăng nhập
        Button btnLogin = new Button("Đăng nhập");
        btnLogin.setPrefWidth(300);
        btnLogin.setPrefHeight(45);
        btnLogin.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        btnLogin.setStyle(
                "-fx-background-color: #0A2940;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 20;"
        );
        btnLogin.setOnMouseEntered(e -> btnLogin.setStyle("-fx-background-color: #123E63; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-weight: bold; -fx-font-size: 16px;"));
        btnLogin.setOnMouseExited(e -> btnLogin.setStyle("-fx-background-color: #0A2940; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-weight: bold; -fx-font-size: 16px;"));


        // Layout Form Đăng nhập
        VBox formContent = new VBox(25); // Khoảng cách giữa các phần tử
        formContent.getChildren().addAll(
                lblLogin,
                usernameBox,
                passwordBox,
                forgotPassContainer,
                btnLogin
        );
        formContent.setAlignment(Pos.CENTER);
        formContent.setPadding(new Insets(50, 40, 50, 40));
        formContent.setPrefSize(FORM_WIDTH, TOTAL_HEIGHT);
        formContent.setMaxSize(FORM_WIDTH, TOTAL_HEIGHT);

        // Khối Form chính (Nền Trắng - Không bo góc)
        StackPane formPane = new StackPane(formContent);
        formPane.setStyle(
                "-fx-background-color: white;"
                // KHÔNG BO GÓC cho khối này theo yêu cầu
        );

        // ==========================================================
        // 2. KHỐI HÌNH ẢNH (ĐÈ LÊN)
        // ==========================================================
        StackPane paneHinhAnh = new StackPane();
        paneHinhAnh.setPrefSize(IMAGE_WIDTH, TOTAL_HEIGHT);
        paneHinhAnh.setMaxSize(IMAGE_WIDTH, TOTAL_HEIGHT);

        try {
            Image spicesImage = new Image(getClass().getResource(SPICES_IMAGE_PATH).toExternalForm());
            ImageView imageView = new ImageView(spicesImage);

            imageView.setFitWidth(IMAGE_WIDTH);
            imageView.setFitHeight(TOTAL_HEIGHT);
            imageView.setPreserveRatio(false);

            // TÁC DỤNG CỦA CSS: Bo góc trên và dưới bên trái (ARC_RADIUS 0 0 ARC_RADIUS)
            paneHinhAnh.setStyle(
                    "-fx-background-image: url('" + getClass().getResource(SPICES_IMAGE_PATH).toExternalForm() + "');" +
//                            "-fx-background-size: cover;" +
                            "-fx-background-repeat: no-repeat;" +
                            "-fx-background-position: right center;" +
                            "-fx-background-size: 200% 100%;"+
                            "-fx-background-radius: 0 " + ARC_RADIUS + " " + ARC_RADIUS + " 0;"+
                            "-fx-background-insets: 0;"
            );

            // Xóa ImageView và dùng background CSS để đạt bo góc tốt hơn
            // paneHinhAnh.getChildren().add(imageView);

        } catch (Exception e) {
            System.err.println("Không tìm thấy ảnh: " + SPICES_IMAGE_PATH + " - Sử dụng màu nền thay thế.");
            paneHinhAnh.setStyle("-fx-background-color: #333333; -fx-background-radius: " + ARC_RADIUS + " 0 0 " + ARC_RADIUS + ";");
        }

        // ==========================================================
        // 3. CONTAINER TỔNG (SỬ DỤNG HBox VÀ StackPane ĐỂ CHỒNG LÊN)
        // ==========================================================

        // HBox để xếp Khối Ảnh và Khối Form cạnh nhau
        HBox tempContainer = new HBox(paneHinhAnh, formPane);
        tempContainer.setAlignment(Pos.CENTER_LEFT);
        tempContainer.setPrefSize(IMAGE_WIDTH + FORM_WIDTH, TOTAL_HEIGHT);

        // Khối chính (mainContainer) là StackPane để thêm Shadow
        StackPane mainContainer = new StackPane(tempContainer);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPrefSize(IMAGE_WIDTH + FORM_WIDTH, TOTAL_HEIGHT);



        // Clip cho mainContainer để bo góc toàn bộ khối nếu cần (đã bỏ vì form không bo góc)
        // Nếu muốn bo góc phải của form, bạn cần thêm CSS vào formPane

        // ===== Nền chính (Root) =====
        StackPane root = new StackPane(mainContainer);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: #F0F0F0;");


        // ===== Scene =====
        Scene scene = new Scene(root, 900, 500);
        stage.setTitle("Đăng nhập");
        stage.setScene(scene);
        stage.show();
    }

    // public static void main(String[] args) {
    //     launch();
    // }
}