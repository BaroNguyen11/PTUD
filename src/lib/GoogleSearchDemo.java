package lib;

import javafx.application.Application;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GoogleSearchDemo extends Application {

    // 1. Danh sách dữ liệu mẫu (Giả lập database)
    private final List<String> data = Arrays.asList(
            "dich - Tìm kiếm trên Google",
            "traveloka",
            "trái bầu",
            "xác định các phương thức liên kết",
            "handwriting generator",
            "java tutorial",
            "javafx autocomplete",
            "google search ui"
    );

    @Override
    public void start(Stage primaryStage) {
        TextField txtSearch = new TextField();
        txtSearch.setPromptText("Nhập từ khóa tìm kiếm...");

        // Tạo ContextMenu để chứa các gợi ý
        ContextMenu suggestionsPopup = new ContextMenu();

        // 2. Sự kiện: Khi người dùng gõ chữ (Gợi ý tới đó)
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                // Nếu xóa hết chữ, có thể ẩn hoặc hiện lịch sử (tùy chọn)
                suggestionsPopup.hide();
            } else {
                // Lọc danh sách dựa trên từ khóa
                showSuggestions(txtSearch, suggestionsPopup, newValue);
            }
        });

        // 3. Sự kiện: Khi click chuột vào ô input (Hiển thị gợi ý ngay)
        txtSearch.setOnMouseClicked(event -> {
            // Nếu ô trống thì hiện tất cả (hoặc history), nếu có chữ thì lọc
            String text = txtSearch.getText();
            showSuggestions(txtSearch, suggestionsPopup, text);
        });

        // Ẩn popup khi người dùng focus ra chỗ khác
        txtSearch.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) suggestionsPopup.hide();
        });

        VBox root = new VBox(txtSearch);
        root.setStyle("-fx-padding: 50px; -fx-background-color: #202124;"); // Màu nền tối giống Google
        Scene scene = new Scene(root, 600, 400);

        // Thêm CSS (bước dưới)
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        primaryStage.setTitle("Google Search Suggestion Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Hàm logic lọc và hiển thị
    private void showSuggestions(TextField txtSearch, ContextMenu suggestionsPopup, String query) {
        List<String> matches;

        if (query.isEmpty()) {
            // Nếu chưa gõ gì, hiển thị tất cả (hoặc danh sách lịch sử tìm kiếm)
            matches = data;
        } else {
            // Lọc các từ có chứa ký tự đang gõ (không phân biệt hoa thường)
            matches = data.stream()
                    .filter(item -> item.toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (!matches.isEmpty()) {
            // Xóa mục cũ
            suggestionsPopup.getItems().clear();

            // Thêm mục mới vào menu
            for (String match : matches) {
                MenuItem item = new MenuItem(match);

                // Sự kiện khi chọn một gợi ý
                item.setOnAction(e -> {
                    txtSearch.setText(match);
                    txtSearch.positionCaret(match.length()); // Đưa con trỏ về cuối
                    suggestionsPopup.hide();
                });

                suggestionsPopup.getItems().add(item);
            }

            // Hiển thị popup ngay dưới TextField
            if (!suggestionsPopup.isShowing()) {
                suggestionsPopup.show(txtSearch, Side.BOTTOM, 0, 0);
            }
        } else {
            suggestionsPopup.hide();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}