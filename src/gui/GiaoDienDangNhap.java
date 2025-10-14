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

    @Override
    public void start(Stage stage) {
    	   ImageView logoView = new ImageView(new Image(getClass().getResource("/img/Logo.png").toExternalForm()));
    	   logoView.setFitHeight(64); 
    	   logoView.setPreserveRatio(true);
        // ===== Tiêu đề =====
        Label lblTitle = new Label("2BT Restaurant");
        lblTitle.setFont(Font.font("Times New Roman", FontWeight.BOLD, 36));
        lblTitle.setTextFill(Color.WHITE);

        Label lblLogin = new Label("ĐĂNG NHẬP");
        lblLogin.setFont(Font.font("Times New Roman", FontWeight.BOLD, 28));
        lblLogin.setTextFill(Color.WHITE);

        // ===== Ô nhập tài khoản =====
        TextField txtUsername = new TextField();
        txtUsername.setPromptText("Tên đăng nhập");
        txtUsername.setMaxWidth(300);
        txtUsername.setStyle(
                "-fx-background-radius: 25;" +
                "-fx-background-color: rgba(255,255,255,0.85);" +
                "-fx-font-size: 16px;" +
                "-fx-padding: 10 20 10 20;" +
                "-fx-border-color: transparent;"
        );

        // ===== Ô nhập mật khẩu =====
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Mật khẩu");
        txtPassword.setMaxWidth(300);
        txtPassword.setStyle(
                "-fx-background-radius: 25;" +
                "-fx-background-color: rgba(255,255,255,0.85);" +
                "-fx-font-size: 16px;" +
                "-fx-padding: 10 20 10 20;" +
                "-fx-border-color: transparent;"
        );

        // ===== Link quên mật khẩu =====
        Hyperlink forgotPassword = new Hyperlink("Quên mật khẩu?");
        forgotPassword.setTextFill(Color.BLUE);
        forgotPassword.setBorder(Border.EMPTY);
        forgotPassword.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        forgotPassword.setStyle(
        		"-fx-padding-left: 20"
        	);
        
        // ===== Nút đăng nhập =====
        Button btnLogin = new Button("Đăng nhập");
        btnLogin.setPrefWidth(300);
        btnLogin.setStyle(
                "-fx-background-color: #0A2940;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 18px;" +
                "-fx-background-radius: 25;" +
                "-fx-padding: 10 0 10 0;"
        );

        btnLogin.setOnMouseEntered(e -> btnLogin.setStyle(
                "-fx-background-color: #123E63;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 18px;" +
                "-fx-background-radius: 25;" +
                "-fx-padding: 10 0 10 0;"
        ));
        btnLogin.setOnMouseExited(e -> btnLogin.setStyle(
                "-fx-background-color: #0A2940;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 18px;" +
                "-fx-background-radius: 25;" +
                "-fx-padding: 10 0 10 0;"
        ));

        // ===== Layout =====
        VBox loginBox = new VBox(15, logoView, lblTitle, lblLogin, txtUsername, txtPassword, forgotPassword, btnLogin);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPrefWidth(480);
        loginBox.setStyle(
                "-fx-background-color: rgba(255,255,255,0.2);" +
                "-fx-background-radius: 20;"
        );
        

        // ===== Nền chính =====
        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(loginBox);
        root.setPadding(new Insets(50));

        // ===== Hình nền =====
        root.setBackground(new Background(new BackgroundImage(
                new Image(getClass().getResource("/img/NenDangNhap.png").toExternalForm(),
                        1200, 700, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, BackgroundSize.DEFAULT
        )));

        // ===== Scene =====
        Scene scene = new Scene(root, 950, 570);
        stage.setTitle("Đăng nhập - 2BT Restaurant");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
