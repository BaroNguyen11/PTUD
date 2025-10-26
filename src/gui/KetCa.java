
package gui;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class KetCa extends VBox {

    private TextField txtTienDauCa;
    private TextField txtTongSoHoaDon;
    private TextField txtGiamGia;
    private TextField txtTienMat;
    private TextField txtNganHang;
    private TextField txtBuAnPhucVu;
    private TextField txtTienNhanCoc;
    private TextField txtTienCuoiCa;

    private TextField txtSoTo500d;
    private TextField txtSoTo1k;
    private TextField txtSoTo2k;
    private TextField txtSoTo5k;
    private TextField txtSoTo10k;
    private TextField txtSoTo20k;
    private TextField txtSoTo50k;
    private TextField txtSoTo100k;
    private TextField txtSoTo200k;
    private TextField txtSoTo500k;
    private TextField txtTongMenhGia;

    private VBox summaryCards;

    public KetCa() {
        VBox rootContent = new VBox(25);
        rootContent.setPadding(new Insets(30, 40, 30, 40));
        rootContent.setStyle("-fx-background-color: #f5f7fa;");

        // 1. Tiêu đề với gradient
        Label title = new Label("📊 Báo cáo kết ca");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: #2c3e50;");

        // 2. Thông tin Giờ
        HBox timeInfo = createTimeInfoSection();

        // 3. Thẻ tổng quan
        summaryCards = createSummaryCards();

        // 4. Form nhập liệu chính
        GridPane mainFormGrid = createMainFormGrid();

        VBox formContainer = new VBox(mainFormGrid);
        formContainer.setPadding(new Insets(25));
        formContainer.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 12; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);"
        );

        // 5. Footer với nút gradient
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnConfirm = new Button("✅ Xác nhận kết ca");
        btnConfirm.setStyle(
                "-fx-background-color: #3b3c58; " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 12 30; " +
                        "-fx-font-weight: bold; " +
                        "-fx-cursor: hand; " +
                        "-fx-font-size: 15px; " +
                        "-fx-background-radius: 8; " +
                        "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.4), 10, 0, 0, 4);"
        );





        HBox footer = new HBox(spacer, btnConfirm);
        footer.setPadding(new Insets(20, 0, 0, 0));
        footer.setAlignment(Pos.BOTTOM_RIGHT);

        rootContent.getChildren().addAll(title, timeInfo, summaryCards, formContainer, footer);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), rootContent);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(rootContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        this.getChildren().add(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
    }

    private HBox createTimeInfoSection() {
        HBox timeBox = new HBox(40);
        timeBox.setPadding(new Insets(15, 20, 20, 20));
        timeBox.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 8, 0, 0, 2);"
        );

        Label startLabel = new Label("🕐 Giờ đầu ca: 23/10/2025 8h45");
        startLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        startLabel.setStyle("-fx-text-fill: #34495e;");

        Label currentLabel = new Label("⏰ Giờ hiện tại: 23/10/2025 16h00");
        currentLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        currentLabel.setStyle("-fx-text-fill: #e74c3c;");

        timeBox.getChildren().addAll(startLabel, currentLabel);
        return timeBox;
    }

    private VBox createSummaryCards() {
        HBox cardContainer = new HBox(20);
        cardContainer.setPadding(new Insets(0, 0, 25, 0));

        cardContainer.getChildren().addAll(
                createSummaryCard("💰 Doanh thu", "36.000.000 VND", "#27ae60", "#e8f8f5"),
                createSummaryCard("📉 Giảm giá/Chi", "1.500.000 VND", "#f39c12", "#fef5e7"),
                createSummaryCard("⏱️ Giờ làm", "8h 15m", "#3498db", "#ebf5fb"),
                createSummaryCard("∆ Chênh lệch", "+ 5.000 VND", "#27ae60", "#e8f8f5")
        );

        VBox container = new VBox(cardContainer);
        return container;
    }

    private VBox createSummaryCard(String title, String value, String color, String bgColor) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(20, 15, 20, 15));
        card.setStyle(
                "-fx-background-color: " + bgColor + "; " +
                        "-fx-background-radius: 12; " +
                        "-fx-border-color: " + color + "; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 12; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 3);"
        );
        card.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        titleLabel.setStyle("-fx-text-fill: #7f8c8d;");

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        valueLabel.setStyle("-fx-text-fill: " + color + ";");

        card.getChildren().addAll(titleLabel, valueLabel);
        HBox.setHgrow(card, Priority.ALWAYS);
        return card;
    }

    private GridPane createMainFormGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(80);
        grid.setVgap(18);
        grid.setAlignment(Pos.TOP_LEFT);

        VBox col1 = new VBox(18);

        Label col1Title = new Label("📝 Thông tin tổng kết ca");
        col1Title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        col1Title.setStyle("-fx-text-fill: #2c3e50;");
        col1Title.setPadding(new Insets(0, 0, 10, 0));

        col1.getChildren().addAll(
                col1Title,
                createInputField("Tiền đầu ca", txtTienDauCa = new TextField(), true),
                createInputField("Tổng số hóa đơn", txtTongSoHoaDon = new TextField(), true),
                createInputField("Giảm giá", txtGiamGia = new TextField(), true),
                createInputField("Tiền mặt", txtTienMat = new TextField(), true),
                createInputField("Ngân hàng", txtNganHang = new TextField(), true),
                createInputField("Đang phục vụ", txtBuAnPhucVu = new TextField(), true),
                createInputField("Tiền nhận cọc", txtTienNhanCoc = new TextField(), true),
                createInputField("Tiền cuối ca", txtTienCuoiCa = new TextField(), true)
        );

        VBox col2 = new VBox(18);

        Label col2Title = new Label("💵 Kiểm đếm tiền mặt");
        col2Title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        col2Title.setStyle("-fx-text-fill: #2c3e50;");
        col2Title.setPadding(new Insets(0, 0, 10, 0));

        col2.getChildren().addAll(
                col2Title,
                createInputField("Số tờ 500đ", txtSoTo500d = new TextField(), false),
                createInputField("Số tờ 1.000đ", txtSoTo1k = new TextField(), false),
                createInputField("Số tờ 2.000đ", txtSoTo2k = new TextField(), false),
                createInputField("Số tờ 5.000đ", txtSoTo5k = new TextField(), false),
                createInputField("Số tờ 10.000đ", txtSoTo10k = new TextField(), false),
                createInputField("Số tờ 20.000đ", txtSoTo20k = new TextField(), false),
                createInputField("Số tờ 50.000đ", txtSoTo50k = new TextField(), false),
                createInputField("Số tờ 100.000đ", txtSoTo100k = new TextField(), false),
                createInputField("Số tờ 200.000đ", txtSoTo200k = new TextField(), false),
                createInputField("Số tờ 500.000đ", txtSoTo500k = new TextField(), false)
        );

        VBox totalBox = new VBox(10);
        totalBox.setPadding(new Insets(15, 0, 0, 0));
        totalBox.setStyle(
                "-fx-border-color: #3498db; " +
                        "-fx-border-width: 2 0 0 0; " +
                        "-fx-padding: 15 0 0 0;"
        );
        totalBox.getChildren().add(createInputField("Tổng mệnh giá tiền", txtTongMenhGia = new TextField(), true));
        col2.getChildren().add(totalBox);

        grid.add(col1, 0, 0);
        grid.add(col2, 1, 0);

        return grid;
    }

    private HBox createInputField(String labelText, TextField textField, boolean isReadOnly) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);

        Label label = new Label(labelText);
        label.setMinWidth(160);
        label.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        label.setStyle("-fx-text-fill: #34495e;");

        textField.setPrefWidth(220);
        textField.setEditable(!isReadOnly);

        if (isReadOnly) {
            textField.setStyle(
                    "-fx-background-color: #ecf0f1; " +
                            "-fx-border-color: #bdc3c7; " +
                            "-fx-border-radius: 6; " +
                            "-fx-background-radius: 6; " +
                            "-fx-padding: 10; " +
                            "-fx-font-size: 13px; " +
                            "-fx-text-fill: #7f8c8d;"
            );
        } else {
            textField.setStyle(
                    "-fx-background-color: white; " +
                            "-fx-border-color: #3498db; " +
                            "-fx-border-width: 1.5; " +
                            "-fx-border-radius: 6; " +
                            "-fx-background-radius: 6; " +
                            "-fx-padding: 10; " +
                            "-fx-font-size: 13px;"
            );

            textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    textField.setStyle(
                            "-fx-background-color: white; " +
                                    "-fx-border-color: #667eea; " +
                                    "-fx-border-width: 1.5; " +
                                    "-fx-border-radius: 6; " +
                                    "-fx-background-radius: 6; " +
                                    "-fx-padding: 10; " +
                                    "-fx-font-size: 13px; " +
                                    "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.3), 8, 0, 0, 2);"
                    );
                } else {
                    textField.setStyle(
                            "-fx-background-color: white; " +
                                    "-fx-border-color: #3498db; " +
                                    "-fx-border-width: 1.5; " +
                                    "-fx-border-radius: 6; " +
                                    "-fx-background-radius: 6; " +
                                    "-fx-padding: 10; " +
                                    "-fx-font-size: 13px;"
                    );
                }
            });
        }

        row.getChildren().addAll(label, textField);
        return row;
    }
}