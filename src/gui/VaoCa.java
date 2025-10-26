/*
 * @ (#) VaoCa.java     1.0     10/23/2025
 * Copyright (c) 2025 IUH.All Rights Reserved.
 */
package gui;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
import javafx.stage.Stage;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
 *@description: Giao diện vào ca - Kiểm đếm tiền mặt đầu ca
 *@author: Bao Nguyen
 *@Date: 10/23/2025
 *@version: 1.0
 */
public class VaoCa extends VBox {

    // TextField cho các mệnh giá tiền
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
    private TextField txtTongTien;

    // Label hiển thị thông tin
    private Label lblNhanVien;
    private Label lblGioVaoCa;

    public VaoCa() {
        VBox rootContent = new VBox(25);
        rootContent.setPadding(new Insets(30, 40, 30, 40));
        rootContent.setStyle("-fx-background-color: #f5f7fa;");

        // 1. Header với thông tin ca làm
        VBox header = createHeader();

        // 2. Thẻ thông tin nổi bật
        HBox infoCards = createInfoCards();

        // 3. Form kiểm đếm tiền
        VBox formContainer = createMoneyCountForm();

        // 4. Footer với các nút
        HBox footer = createFooter();

        rootContent.getChildren().addAll(header, infoCards, formContainer, footer);

        // Animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), rootContent);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        // ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(rootContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        this.getChildren().add(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
    }

    // ===== HEADER =====
    private VBox createHeader() {
        VBox header = new VBox(15);
        header.setPadding(new Insets(0, 0, 10, 0));

        // Tiêu đề chính
        Label title = new Label("🌅 Vào ca làm việc");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: #2c3e50;");

        // Thông tin nhân viên và giờ vào ca
        HBox infoBox = new HBox(40);
        infoBox.setPadding(new Insets(15, 20, 15, 20));
        infoBox.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 8, 0, 0, 2);"
        );

        lblNhanVien = new Label("👤 Nhân viên: Nguyễn Văn A");
        lblNhanVien.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        lblNhanVien.setStyle("-fx-text-fill: #34495e;");

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        lblGioVaoCa = new Label("⏰ Giờ vào ca: " + now.format(formatter));
        lblGioVaoCa.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        lblGioVaoCa.setStyle("-fx-text-fill: #27ae60;");

        infoBox.getChildren().addAll(lblNhanVien, lblGioVaoCa);

        header.getChildren().addAll(title, infoBox);
        return header;
    }

    // ===== INFO CARDS =====
    private HBox createInfoCards() {
        HBox cardContainer = new HBox(20);
        cardContainer.setPadding(new Insets(0, 0, 15, 0));

        VBox card1 = createInfoCard("💰 Tiền đầu ca", "Cần kiểm đếm", "#3498db", "#ebf5fb");
        VBox card2 = createInfoCard("📝 Trạng thái", "Đang kiểm tra", "#f39c12", "#fef5e7");
        VBox card3 = createInfoCard("✅ Bước tiếp theo", "Xác nhận vào ca", "#27ae60", "#e8f8f5");

        cardContainer.getChildren().addAll(card1, card2, card3);
        return cardContainer;
    }

    private VBox createInfoCard(String title, String value, String color, String bgColor) {
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
        valueLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        valueLabel.setStyle("-fx-text-fill: " + color + ";");

        card.getChildren().addAll(titleLabel, valueLabel);
        HBox.setHgrow(card, Priority.ALWAYS);
        return card;
    }

    // ===== FORM KIỂM ĐẾCH TIỀN =====
    private VBox createMoneyCountForm() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(25));
        container.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 12; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);"
        );

        // Tiêu đề form
        Label formTitle = new Label("💵 Kiểm đếm tiền mặt đầu ca");
        formTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        formTitle.setStyle("-fx-text-fill: #2c3e50;");

        Label formSubtitle = new Label("Vui lòng nhập số lượng tờ tiền cho từng mệnh giá");
        formSubtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        formSubtitle.setStyle("-fx-text-fill: #7f8c8d;");

        // Grid chứa các trường nhập
        GridPane grid = new GridPane();
        grid.setHgap(40);
        grid.setVgap(16);
        grid.setPadding(new Insets(20, 0, 0, 0));

        // Cột 1: Mệnh giá nhỏ
        VBox col1 = new VBox(16);
        Label col1Title = new Label("📄 Mệnh giá nhỏ");
        col1Title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        col1Title.setStyle("-fx-text-fill: #34495e;");
        col1Title.setPadding(new Insets(0, 0, 8, 0));

        col1.getChildren().addAll(
                col1Title,
                createMoneyInputField("500đ", txtSoTo500d = new TextField()),
                createMoneyInputField("1.000đ", txtSoTo1k = new TextField()),
                createMoneyInputField("2.000đ", txtSoTo2k = new TextField()),
                createMoneyInputField("5.000đ", txtSoTo5k = new TextField()),
                createMoneyInputField("10.000đ", txtSoTo10k = new TextField())
        );

        // Cột 2: Mệnh giá lớn
        VBox col2 = new VBox(16);
        Label col2Title = new Label("💵 Mệnh giá lớn");
        col2Title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        col2Title.setStyle("-fx-text-fill: #34495e;");
        col2Title.setPadding(new Insets(0, 0, 8, 0));

        col2.getChildren().addAll(
                col2Title,
                createMoneyInputField("20.000đ", txtSoTo20k = new TextField()),
                createMoneyInputField("50.000đ", txtSoTo50k = new TextField()),
                createMoneyInputField("100.000đ", txtSoTo100k = new TextField()),
                createMoneyInputField("200.000đ", txtSoTo200k = new TextField()),
                createMoneyInputField("500.000đ", txtSoTo500k = new TextField())
        );

        grid.add(col1, 0, 0);
        grid.add(col2, 1, 0);

        // Tổng tiền
        HBox totalBox = new HBox(15);
        totalBox.setPadding(new Insets(25, 0, 0, 0));
        totalBox.setStyle(
                "-fx-border-color: #667eea; " +
                        "-fx-border-width: 2 0 0 0; " +
                        "-fx-padding: 20 0 0 0;"
        );
        totalBox.setAlignment(Pos.CENTER_LEFT);

        Label totalLabel = new Label("💎 Tổng tiền đầu ca:");
        totalLabel.setMinWidth(200);
        totalLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        totalLabel.setStyle("-fx-text-fill: #2c3e50;");

        txtTongTien = new TextField("0 VND");
        txtTongTien.setPrefWidth(300);
        txtTongTien.setEditable(false);
        txtTongTien.setStyle(
                "-fx-background-color: #e8f8f5; " +
                        "-fx-border-color: #27ae60; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 8; " +
                        "-fx-background-radius: 8; " +
                        "-fx-padding: 12; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #27ae60;"
        );

        totalBox.getChildren().addAll(totalLabel, txtTongTien);

        container.getChildren().addAll(formTitle, formSubtitle, grid, totalBox);
        return container;
    }

    private HBox createMoneyInputField(String denomination, TextField textField) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);

        Label label = new Label("Mệnh giá " + denomination + ":");
        label.setMinWidth(140);
        label.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        label.setStyle("-fx-text-fill: #34495e;");

        textField.setPrefWidth(180);
        textField.setPromptText("Số tờ");
        textField.setStyle(
                "-fx-background-color: white; " +
                        "-fx-border-color: #3498db; " +
                        "-fx-border-width: 1.5; " +
                        "-fx-border-radius: 6; " +
                        "-fx-background-radius: 6; " +
                        "-fx-padding: 10; " +
                        "-fx-font-size: 13px;"
        );

        // Hiệu ứng focus
        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                textField.setStyle(
                        "-fx-background-color: white; " +
                                "-fx-border-color: #667eea; " +
                                "-fx-border-width: 2; " +
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

        // Chỉ cho phép nhập số
        textField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                textField.setText(newVal.replaceAll("[^\\d]", ""));
            }
            calculateTotal();
        });

        row.getChildren().addAll(label, textField);
        return row;
    }

    // ===== FOOTER =====
    private HBox createFooter() {
        HBox footer = new HBox(15);
        footer.setPadding(new Insets(20, 0, 0, 0));
        footer.setAlignment(Pos.CENTER_RIGHT);

        // Nút Hủy
        Button btnCancel = new Button("✕ Hủy");
        btnCancel.setStyle(
                "-fx-background-color: #95a5a6; " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 12 30; " +
                        "-fx-font-weight: bold; " +
                        "-fx-cursor: hand; " +
                        "-fx-font-size: 15px; " +
                        "-fx-background-radius: 8;"
        );

        btnCancel.setOnMouseEntered(e -> btnCancel.setStyle(
                "-fx-background-color: #7f8c8d; " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 12 30; " +
                        "-fx-font-weight: bold; " +
                        "-fx-cursor: hand; " +
                        "-fx-font-size: 15px; " +
                        "-fx-background-radius: 8;"
        ));

        btnCancel.setOnMouseExited(e -> btnCancel.setStyle(
                "-fx-background-color: #95a5a6; " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 12 30; " +
                        "-fx-font-weight: bold; " +
                        "-fx-cursor: hand; " +
                        "-fx-font-size: 15px; " +
                        "-fx-background-radius: 8;"
        ));

        // Nút Xác nhận vào ca
        Button btnConfirm = new Button("✓ Xác nhận vào ca");
        btnConfirm.setStyle(
                "-fx-background-color: linear-gradient(to right, #667eea 0%, #764ba2 100%); " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 12 35; " +
                        "-fx-font-weight: bold; " +
                        "-fx-cursor: hand; " +
                        "-fx-font-size: 15px; " +
                        "-fx-background-radius: 8; " +
                        "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.4), 10, 0, 0, 4);"
        );

        btnConfirm.setOnMouseEntered(e -> btnConfirm.setStyle(
                "-fx-background-color: linear-gradient(to right, #5568d3 0%, #653a8b 100%); " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 12 35; " +
                        "-fx-font-weight: bold; " +
                        "-fx-cursor: hand; " +
                        "-fx-font-size: 15px; " +
                        "-fx-background-radius: 8; " +
                        "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.6), 15, 0, 0, 5);"
        ));

        btnConfirm.setOnMouseExited(e -> btnConfirm.setStyle(
                "-fx-background-color: linear-gradient(to right, #667eea 0%, #764ba2 100%); " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 12 35; " +
                        "-fx-font-weight: bold; " +
                        "-fx-cursor: hand; " +
                        "-fx-font-size: 15px; " +
                        "-fx-background-radius: 8; " +
                        "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.4), 10, 0, 0, 4);"
        ));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        footer.getChildren().addAll(spacer, btnCancel, btnConfirm);
        return footer;
    }

    // ===== TÍNH TỔNG TIỀN =====
    private void calculateTotal() {
        long total = 0;

        total += getTextFieldValue(txtSoTo500d) * 500;
        total += getTextFieldValue(txtSoTo1k) * 1000;
        total += getTextFieldValue(txtSoTo2k) * 2000;
        total += getTextFieldValue(txtSoTo5k) * 5000;
        total += getTextFieldValue(txtSoTo10k) * 10000;
        total += getTextFieldValue(txtSoTo20k) * 20000;
        total += getTextFieldValue(txtSoTo50k) * 50000;
        total += getTextFieldValue(txtSoTo100k) * 100000;
        total += getTextFieldValue(txtSoTo200k) * 200000;
        total += getTextFieldValue(txtSoTo500k) * 500000;

        txtTongTien.setText(String.format("%,d VND", total));
    }

    private int getTextFieldValue(TextField textField) {
        try {
            String text = textField.getText().trim();
            return text.isEmpty() ? 0 : Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static void main(String[] args) {
        Application.launch(VaoCaApp.class, args);
    }

    /** Class phụ để chạy JavaFX vì VaoCa không kế thừa Application */
    public static class VaoCaApp extends Application {
        @Override
        public void start(Stage primaryStage) {
            VaoCa vaoCa = new VaoCa();
            Scene scene = new Scene(vaoCa, 900, 700);
            primaryStage.setTitle("🕓 Vào ca làm việc");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }
}