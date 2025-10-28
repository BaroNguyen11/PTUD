package lib;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Upload_Demo extends Application {

    private final SupabaseImageUploader uploader = new SupabaseImageUploader();

    @Override
    public void start(Stage primaryStage) {
        Label lblStatus = new Label("Chưa có ảnh được upload");
        ImageView imageView = new ImageView();
        imageView.setFitWidth(400);
        imageView.setPreserveRatio(true);

        Button btnChoose = new Button("Chọn Ảnh");
        btnChoose.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Hình ảnh", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                lblStatus.setText("⏳ Đang upload...");
                new Thread(() -> {
                    try {
                        // Upload ảnh và nhận URL công khai
                        String publicUrl = uploader.uploadImage(file);

                        // Cập nhật giao diện (chạy trên JavaFX Thread)
                        Platform.runLater(() -> {
                            lblStatus.setText("✅ Upload thành công!");
                            imageView.setImage(new Image(publicUrl, true)); // hiển thị ảnh trực tiếp
                        });

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Platform.runLater(() ->
                                lblStatus.setText("❌ Lỗi upload: " + ex.getMessage()));
                    }
                }).start();
            }
        });

        VBox root = new VBox(20, lblStatus, imageView, btnChoose);
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, 500, 600);
        primaryStage.setTitle("Demo Upload Ảnh Supabase");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
