package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VaoCaModal {

    private Stage modalStage;
    private TextField txtSoTo500d, txtSoTo1k, txtSoTo2k, txtSoTo5k, txtSoTo10k;
    private TextField txtSoTo20k, txtSoTo50k, txtSoTo100k, txtSoTo200k, txtSoTo500k;
    private TextField txtTongTien;
    private boolean confirmed = false;
    private long tongTienDauCa = 0;

    public VaoCaModal(String tenNhanVien, String maNhanVien) {
        modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initStyle(StageStyle.UNDECORATED);
        modalStage.setTitle("Vào ca làm việc");

        // Main container
        VBox mainContainer = new VBox(0);
        mainContainer.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 5);"
        );
        mainContainer.setPrefWidth(800);
        mainContainer.setMaxWidth(800);

        // Header
        HBox header = createHeader(tenNhanVien, maNhanVien);

        // Content
        ScrollPane scrollContent = createContent();

        // Footer
        HBox footer = createFooter();

        mainContainer.getChildren().addAll(header, scrollContent, footer);

        Scene scene = new Scene(mainContainer);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        modalStage.setScene(scene);
    }

    private HBox createHeader(String tenNhanVien, String maNhanVien) {
        HBox header = new HBox(15);
        header.setPadding(new Insets(25, 30, 20, 30));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle(
                "-fx-background-color: linear-gradient(to right, #667eea 0%, #764ba2 100%);" +
                        "-fx-background-radius: 15 15 0 0;"
        );

        Label icon = new Label("🌅");
        icon.setFont(Font.font(32));

        VBox titleBox = new VBox(5);
        Label title = new Label("Vào ca làm việc");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        title.setStyle("-fx-text-fill: white;");

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        Label subtitle = new Label("👤 " + tenNhanVien + " (" + maNhanVien + ") • ⏰ " + now.format(formatter));
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        subtitle.setStyle("-fx-text-fill: rgba(255,255,255,0.9);");

        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Nút đóng
        Button btnClose = new Button("✕");
        btnClose.setStyle(
                "-fx-background-color: rgba(255,255,255,0.2);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-radius: 20;" +
                        "-fx-min-width: 35px;" +
                        "-fx-min-height: 35px;" +
                        "-fx-max-width: 35px;" +
                        "-fx-max-height: 35px;"
        );
        btnClose.setOnMouseEntered(e -> btnClose.setStyle(
                "-fx-background-color: rgba(255,255,255,0.3);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-radius: 20;" +
                        "-fx-min-width: 35px;" +
                        "-fx-min-height: 35px;" +
                        "-fx-max-width: 35px;" +
                        "-fx-max-height: 35px;"
        ));
        btnClose.setOnMouseExited(e -> btnClose.setStyle(
                "-fx-background-color: rgba(255,255,255,0.2);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-radius: 20;" +
                        "-fx-min-width: 35px;" +
                        "-fx-min-height: 35px;" +
                        "-fx-max-width: 35px;" +
                        "-fx-max-height: 35px;"
        ));
        btnClose.setOnAction(e -> {
            if (!confirmed) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText(null);
                alert.setContentText("Bạn phải xác nhận vào ca trước khi tiếp tục!");
                alert.showAndWait();
            }
        });

        header.getChildren().addAll(icon, titleBox, spacer, btnClose);
        return header;
    }

    private ScrollPane createContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(25, 30, 25, 30));

        // Hướng dẫn
        Label instruction = new Label("💵 Vui lòng kiểm đếm và nhập số lượng tờ tiền cho từng mệnh giá");
        instruction.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        instruction.setStyle("-fx-text-fill: #34495e;");

        // Grid form
        GridPane grid = new GridPane();
        grid.setHgap(25);
        grid.setVgap(12);

        // Cột 1: Mệnh giá nhỏ
        VBox col1 = new VBox(12);
        Label col1Title = new Label("📄 Mệnh giá nhỏ");
        col1Title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        col1Title.setStyle("-fx-text-fill: #2c3e50;");

        col1.getChildren().addAll(
                col1Title,
                createMoneyInput("500đ", txtSoTo500d = new TextField()),
                createMoneyInput("1.000đ", txtSoTo1k = new TextField()),
                createMoneyInput("2.000đ", txtSoTo2k = new TextField()),
                createMoneyInput("5.000đ", txtSoTo5k = new TextField()),
                createMoneyInput("10.000đ", txtSoTo10k = new TextField())
        );

        // Cột 2: Mệnh giá lớn
        VBox col2 = new VBox(12);
        Label col2Title = new Label("💵 Mệnh giá lớn");
        col2Title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        col2Title.setStyle("-fx-text-fill: #2c3e50;");

        col2.getChildren().addAll(
                col2Title,
                createMoneyInput("20.000đ", txtSoTo20k = new TextField()),
                createMoneyInput("50.000đ", txtSoTo50k = new TextField()),
                createMoneyInput("100.000đ", txtSoTo100k = new TextField()),
                createMoneyInput("200.000đ", txtSoTo200k = new TextField()),
                createMoneyInput("500.000đ", txtSoTo500k = new TextField())
        );

        grid.add(col1, 0, 0);
        grid.add(col2, 1, 0);

        // Tổng tiền
        HBox totalBox = new HBox(15);
        totalBox.setPadding(new Insets(20, 0, 0, 0));
        totalBox.setAlignment(Pos.CENTER);
        totalBox.setStyle("-fx-border-color: #667eea; -fx-border-width: 2 0 0 0;");

        Label totalLabel = new Label("💎 Tổng tiền đầu ca:");
        totalLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        totalLabel.setStyle("-fx-text-fill: #2c3e50;");

        txtTongTien = new TextField("0 VND");
        txtTongTien.setPrefWidth(250);
        txtTongTien.setEditable(false);
        txtTongTien.setAlignment(Pos.CENTER);
        txtTongTien.setStyle(
                "-fx-background-color: #e8f8f5;" +
                        "-fx-border-color: #27ae60;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #27ae60;"
        );

        totalBox.getChildren().addAll(totalLabel, txtTongTien);

        content.getChildren().addAll(instruction, grid, totalBox);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scroll.setPrefHeight(400);
        scroll.setMaxHeight(400);

        return scroll;
    }

    private HBox createMoneyInput(String denomination, TextField textField) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        Label label = new Label(denomination + ":");
        label.setMinWidth(80);
        label.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        label.setStyle("-fx-text-fill: #34495e;");

        textField.setPrefWidth(120);
        textField.setPromptText("Số tờ");
        textField.setText("0");
        textField.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #3498db;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 8;" +
                        "-fx-font-size: 13px;"
        );

        // Focus effect
        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                textField.setStyle(
                        "-fx-background-color: white;" +
                                "-fx-border-color: #667eea;" +
                                "-fx-border-width: 2;" +
                                "-fx-border-radius: 6;" +
                                "-fx-background-radius: 6;" +
                                "-fx-padding: 8;" +
                                "-fx-font-size: 13px;" +
                                "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.3), 8, 0, 0, 2);"
                );
            } else {
                textField.setStyle(
                        "-fx-background-color: white;" +
                                "-fx-border-color: #3498db;" +
                                "-fx-border-width: 1.5;" +
                                "-fx-border-radius: 6;" +
                                "-fx-background-radius: 6;" +
                                "-fx-padding: 8;" +
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

    private HBox createFooter() {
        HBox footer = new HBox(15);
        footer.setPadding(new Insets(20, 30, 25, 30));
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 0 0 15 15;");

        Button btnConfirm = new Button("✓ Xác nhận vào ca");
        btnConfirm.setPrefWidth(180);
        btnConfirm.setPrefHeight(40);
        btnConfirm.setStyle(
                "-fx-background-color: linear-gradient(to right, #667eea 0%, #764ba2 100%);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-radius: 8;" +
                        "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.4), 10, 0, 0, 4);"
        );

        btnConfirm.setOnMouseEntered(e -> btnConfirm.setStyle(
                "-fx-background-color: linear-gradient(to right, #5568d3 0%, #653a8b 100%);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-radius: 8;" +
                        "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.6), 15, 0, 0, 5);"
        ));

        btnConfirm.setOnMouseExited(e -> btnConfirm.setStyle(
                "-fx-background-color: linear-gradient(to right, #667eea 0%, #764ba2 100%);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-radius: 8;" +
                        "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.4), 10, 0, 0, 4);"
        ));

        btnConfirm.setOnAction(e -> {
            confirmed = true;
            tongTienDauCa = calculateTotalValue();

            // TODO: Lưu thông tin vào ca vào database

            modalStage.close();
        });

        footer.getChildren().add(btnConfirm);
        return footer;
    }

    private void calculateTotal() {
        long total = calculateTotalValue();
        txtTongTien.setText(String.format("%,d VND", total));
    }

    private long calculateTotalValue() {
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
        return total;
    }

    private int getTextFieldValue(TextField textField) {
        try {
            String text = textField.getText().trim();
            return text.isEmpty() ? 0 : Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void showAndWait() {
        modalStage.showAndWait();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public long getTongTienDauCa() {
        return tongTienDauCa;
    }
}