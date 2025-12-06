package gui;

import dao.Ca_DAO;
import entity.NhanVien;
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Gui_VaoCa {

    private Stage modalStage;
    private TextField txtTongTienMatDauCa;
    private boolean confirmed = false;
    private long tongTienDauCa = 0;

    private NhanVien nhanVien; // ✅ NhanVien object

    public Gui_VaoCa(NhanVien nhanVien) {
        this.nhanVien = nhanVien;

        modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initStyle(StageStyle.UNDECORATED);
        modalStage.setTitle("Vào ca làm việc");

        VBox mainContainer = new VBox(0);
        mainContainer.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 5);"
        );
        mainContainer.setPrefWidth(550);

        HBox header = createHeader(nhanVien.getTenNhanVien(), nhanVien.getMaNhanVien());
        VBox content = createSimpleContent();
        HBox footer = createFooter();

        mainContainer.getChildren().addAll(header, content, footer);

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
        subtitle.setFont(Font.font(13));
        subtitle.setStyle("-fx-text-fill: rgba(255,255,255,0.9);");

        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnClose = new Button("✕");
        btnClose.setStyle(
                "-fx-background-color: rgba(255,255,255,0.2);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-radius: 20;" +
                        "-fx-min-width: 35px;" +
                        "-fx-min-height: 35px;"
        );
        btnClose.setOnAction(e -> modalStage.close());

        header.getChildren().addAll(icon, titleBox, spacer, btnClose);
        return header;
    }

    private VBox createSimpleContent() {
        VBox content = new VBox(25);
        content.setPadding(new Insets(35, 40, 35, 40));
        content.setAlignment(Pos.TOP_CENTER);

        Label instruction = new Label("💰 Vui lòng nhập tổng số tiền mặt có trong két đầu ca");
        instruction.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 15));
        instruction.setStyle("-fx-text-fill: #34495e;");

        HBox inputRow = new HBox(15);
        inputRow.setAlignment(Pos.CENTER);

        Label totalLabel = new Label("Tổng tiền mặt:");
        totalLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        totalLabel.setStyle("-fx-text-fill: #2c3e50;");
        totalLabel.setMinWidth(120);

        txtTongTienMatDauCa = new TextField("0");
        txtTongTienMatDauCa.setPrefHeight(45);
        txtTongTienMatDauCa.setAlignment(Pos.CENTER_RIGHT);
        txtTongTienMatDauCa.setPromptText("Nhập số tiền (VND)");
        txtTongTienMatDauCa.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #3498db;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-radius: 8;" +
                        "-fx-padding: 10;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: BOLD;"
        );

        txtTongTienMatDauCa.textProperty().addListener((obs, oldVal, newVal) -> {
            String clean = newVal.replaceAll("[^\\d]", "");
            if (clean.isEmpty()) {
                tongTienDauCa = 0;
                txtTongTienMatDauCa.setText("");
                return;
            }
            try {
                long value = Long.parseLong(clean);
                tongTienDauCa = value;

                DecimalFormatSymbols s = new DecimalFormatSymbols(new Locale("vi", "VN"));
                s.setGroupingSeparator('.');
                DecimalFormat f = new DecimalFormat("###,###,###", s);
                String formatted = f.format(value);

                if (!txtTongTienMatDauCa.getText().equals(formatted)) {
                    txtTongTienMatDauCa.setText(formatted);
                }
                txtTongTienMatDauCa.positionCaret(formatted.length());
            } catch (Exception ex) {
                txtTongTienMatDauCa.setText(oldVal);
                txtTongTienMatDauCa.positionCaret(oldVal.length());
            }
        });

        Label unitLabel = new Label("VND");
        unitLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        unitLabel.setStyle("-fx-text-fill: #27ae60;");

        inputRow.getChildren().addAll(totalLabel, txtTongTienMatDauCa, unitLabel);
        content.getChildren().addAll(instruction, inputRow);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        content.getChildren().add(spacer);

        return content;
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
                        "-fx-font-weight: bold;"
        );
        btnConfirm.setOnAction(e -> {
            if (tongTienDauCa < 0) {
                showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "Số tiền nhập vào phải ≥ 0");
                return;
            }

            Ca_DAO dao = new Ca_DAO();
            boolean ok = dao.batDauCa(tongTienDauCa, nhanVien.getMaNhanVien());

            if (!ok) {
                showAlert(Alert.AlertType.ERROR, "Lỗi hệ thống", "Không thể bắt đầu ca.\nVui lòng thử lại!");
                return;
            }

            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Bắt đầu ca làm thành công!");
            confirmed = true;
            modalStage.close();
        });

        footer.getChildren().add(btnConfirm);
        return footer;
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
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
