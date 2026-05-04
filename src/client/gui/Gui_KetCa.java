//package client.gui;
//
//import javafx.animation.FadeTransition;
//import javafx.geometry.HPos;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.ScrollPane;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.ColumnConstraints;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.Priority;
//import javafx.scene.layout.Region;
//import javafx.scene.layout.VBox;
//import javafx.scene.text.Font;
//import javafx.scene.text.FontWeight;
//import javafx.util.Duration;
//
//public class KetCa extends VBox {
//
//    // 1. Khai báo TextField
//    private TextField txtTienDauCa;
//    private TextField txtTongSoHoaDon;
//    private TextField txtGiamGia;
//    private TextField txtTienMat;
//    private TextField txtNganHang;
//    private TextField txtDangPhucVu;
//    private TextField txtTienNhanCoc;
//    private TextField txtTienCuoiCa;
//
//    private VBox summaryCards;
//
//    public KetCa() {
//        VBox rootContent = new VBox(35); // Tăng spacing cho các phần chính
//        rootContent.setPadding(new Insets(35, 50, 35, 50)); // Tăng padding tổng thể
//        rootContent.setStyle("-fx-background-color: #f5f7fa;");
//
//        // 1. Tiêu đề
//        Label title = new Label("📊 Báo cáo kết ca");
//        title.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 30)); // Đậm hơn
//        title.setStyle("-fx-text-fill: #2c3e50;");
//
//        // 2. Thông tin Giờ
//        HBox timeInfo = createTimeInfoSection();
//
//        // 3. Thẻ tổng quan (Summary Cards)
//        summaryCards = createSummaryCards();
//
//        // 4. Form nhập liệu chính (chia 2 cột)
//        GridPane mainFormGrid = createMainFormGrid();
//
//        VBox formContainer = new VBox(mainFormGrid);
//        formContainer.setPadding(new Insets(30));
//        formContainer.setStyle(
//                "-fx-background-color: white; " +
//                        "-fx-background-radius: 15; " + // Radius lớn hơn
//                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 20, 0, 0, 5);" // Đổ bóng mềm mại hơn
//        );
//
//        // 5. Footer với nút gradient
//        Region spacer = new Region();
//        HBox.setHgrow(spacer, Priority.ALWAYS);
//
//        Button btnConfirm = new Button("✅ XÁC NHẬN KẾT CA"); // Viết hoa để nhấn mạnh
//        btnConfirm.setStyle(
//                "-fx-background-color: linear-gradient(to right, #4a4d6e, #3b3c58); " + // Gradient màu tím/xám đậm
//                        "-fx-text-fill: white; " +
//                        "-fx-padding: 14 40; " +
//                        "-fx-font-weight: BOLD; " +
//                        "-fx-cursor: hand; " +
//                        "-fx-font-size: 16px; " +
//                        "-fx-background-radius: 10; " +
//                        "-fx-effect: dropshadow(gaussian, rgba(74, 77, 110, 0.5), 15, 0, 0, 5);"
//        );
//
//        // Thêm hiệu ứng hover đơn giản
//        btnConfirm.setOnMouseEntered(e -> btnConfirm.setStyle(btnConfirm.getStyle().replace("#4a4d6e", "#5a5d7e").replace("#3b3c58", "#4b4c68")));
//        btnConfirm.setOnMouseExited(e -> btnConfirm.setStyle(btnConfirm.getStyle().replace("#5a5d7e", "#4a4d6e").replace("#4b4c68", "#3b3c58")));
//
//
//        HBox footer = new HBox(spacer, btnConfirm);
//        footer.setPadding(new Insets(25, 0, 0, 0));
//        footer.setAlignment(Pos.BOTTOM_RIGHT);
//
//        rootContent.getChildren().addAll(title, timeInfo, summaryCards, formContainer, footer);
//
//        // Animation
//        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), rootContent);
//        fadeIn.setFromValue(0.0);
//        fadeIn.setToValue(1.0);
//        fadeIn.play();
//
//        // ScrollPane
//        ScrollPane scrollPane = new ScrollPane(rootContent);
//        scrollPane.setFitToWidth(true);
//        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");
//
//        this.getChildren().add(scrollPane);
//        VBox.setVgrow(scrollPane, Priority.ALWAYS);
//    }
//
//    private HBox createTimeInfoSection() {
//        HBox timeBox = new HBox(50); // Tăng spacing
//        timeBox.setPadding(new Insets(20));
//        timeBox.setAlignment(Pos.CENTER_LEFT);
//        timeBox.setStyle(
//                "-fx-background-color: white; " +
//                        "-fx-background-radius: 12; " +
//                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 3);"
//        );
//
//        Label startLabel = new Label("🕐 Giờ đầu ca: 23/10/2025 8h45");
//        startLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 15));
//        startLabel.setStyle("-fx-text-fill: #34495e;");
//
//        Label currentLabel = new Label("⏰ Giờ kết ca: 23/10/2025 16h00");
//        currentLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15)); // Đậm hơn
//        currentLabel.setStyle("-fx-text-fill: #e74c3c;"); // Màu đỏ nhấn mạnh
//
//        timeBox.getChildren().addAll(startLabel, currentLabel);
//        return timeBox;
//    }
//
//    private VBox createSummaryCards() {
//        HBox cardContainer = new HBox(25); // Tăng spacing giữa các thẻ
//        cardContainer.setPadding(new Insets(0));
//
//        cardContainer.getChildren().addAll(
//                createSummaryCard("💰 Doanh thu", "36.000.000 VND", "#2ecc71", "#e8f8f5", "bar-chart-2"), // Màu xanh tươi
//                createSummaryCard("💸 Giảm giá/Chi", "1.500.000 VND", "#e67e22", "#fef5e7", "minus-circle"), // Màu cam
//                createSummaryCard("⏱️ Giờ làm", "8h 15m", "#3498db", "#ebf5fb", "clock"), // Màu xanh dương
//                createSummaryCard("∆ Chênh lệch", "+ 5.000 VND", "#2ecc71", "#e8f8f5", "trending-up") // Màu xanh tươi
//        );
//
//        HBox.setHgrow(cardContainer.getChildren().get(0), Priority.ALWAYS);
//        HBox.setHgrow(cardContainer.getChildren().get(1), Priority.ALWAYS);
//        HBox.setHgrow(cardContainer.getChildren().get(2), Priority.ALWAYS);
//        HBox.setHgrow(cardContainer.getChildren().get(3), Priority.ALWAYS);
//
//        return new VBox(cardContainer);
//    }
//
//    // Thêm tham số iconName (tạm bỏ qua do không dùng FontAwesome) và thay đổi color, bgColor
//    private VBox createSummaryCard(String title, String value, String color, String bgColor, String iconName) {
//        VBox card = new VBox(5); // Giảm spacing nội bộ
//        card.setPadding(new Insets(20));
//        card.setStyle(
//                "-fx-background-color: " + bgColor + "; " +
//                        "-fx-background-radius: 12; " +
//                        "-fx-border-color: " + color + "; " +
//                        "-fx-border-width: 0 0 0 4; " + // Border chỉ bên trái
//                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 3);"
//        );
//        card.setAlignment(Pos.CENTER_LEFT);
//
//        Label titleLabel = new Label(title);
//        titleLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
//        titleLabel.setStyle("-fx-text-fill: #5e6d82;"); // Màu tối hơn, dễ đọc hơn
//
//        Label valueLabel = new Label(value);
//        valueLabel.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 20)); // Cỡ lớn hơn, đậm hơn
//        valueLabel.setStyle("-fx-text-fill: " + color + ";");
//
//        card.getChildren().addAll(titleLabel, valueLabel);
//        return card;
//    }
//
//    private GridPane createMainFormGrid() {
//        GridPane grid = new GridPane();
//        grid.setHgap(50); // Khoảng cách giữa 2 cột
//        grid.setVgap(0);
//        grid.setAlignment(Pos.TOP_LEFT);
//
//        // Thiết lập ràng buộc cột: mỗi cột chiếm 50%
//        ColumnConstraints col1Const = new ColumnConstraints();
//        col1Const.setPercentWidth(50);
//        ColumnConstraints col2Const = new ColumnConstraints();
//        col2Const.setPercentWidth(50);
//        grid.getColumnConstraints().addAll(col1Const, col2Const);
//
//        // CỘT 1: Thông tin tổng kết ca
//        VBox col1 = new VBox(25); // Tăng spacing
//
//        Label col1Title = new Label("📝 Thông tin tổng kết ca");
//        col1Title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
//        col1Title.setStyle("-fx-text-fill: #2c3e50;");
//
//        VBox infoGroup = new VBox(15);
//        infoGroup.getChildren().addAll(
//                createInputField("Tiền đầu ca (Tiền mặt)", txtTienDauCa = new TextField(), true),
//                createInputField("Tổng số hóa đơn", txtTongSoHoaDon = new TextField(), true),
//                createInputField("Tổng tiền giảm giá", txtGiamGia = new TextField(), true)
//        );
//
//        col1.getChildren().addAll(col1Title, infoGroup);
//
//        // CỘT 2: Chi tiết thanh toán & Kết quả
//        VBox col2 = new VBox(25); // Tăng spacing
//
//        Label col2Title = new Label("💵 Chi tiết thanh toán");
//        col2Title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
//        col2Title.setStyle("-fx-text-fill: #2c3e50;");
//
//        VBox paymentGroup = new VBox(15);
//        paymentGroup.getChildren().addAll(
//                createInputField("Tiền mặt thu được", txtTienMat = new TextField(), true),
//                createInputField("Chuyển khoản (Ngân hàng)", txtNganHang = new TextField(), true),
//                createInputField("Tiền nhận cọc", txtTienNhanCoc = new TextField(), true)
//        );
//
//        // Thêm phần Tổng kết
//        VBox summaryGroup = new VBox(15);
//
//        Label otherTitle = new Label("📊 Tổng kết");
//        otherTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
//        otherTitle.setStyle("-fx-text-fill: #2c3e50; -fx-padding: 10 0 0 0;");
//
//        summaryGroup.getChildren().addAll(
//                otherTitle,
//                createInputField("Đang phục vụ", txtDangPhucVu = new TextField(), true), // Tên field này hơi khó hiểu, tôi giữ nguyên
//                createInputField("💰 TIỀN CUỐI CA", txtTienCuoiCa = new TextField(), true) // Nhấn mạnh field cuối
//        );
//
//
//        // Bôi đậm cho field Tiền cuối ca
//        txtTienCuoiCa.setStyle(
//                "-fx-background-color: #e8f8f5; " + // Màu nền xanh nhạt
//                        "-fx-border-color: #2ecc71; " + // Màu border xanh đậm
//                        "-fx-border-radius: 6; " +
//                        "-fx-background-radius: 6; " +
//                        "-fx-padding: 10; " +
//                        "-fx-font-size: 14px; " +
//                        "-fx-font-weight: bold; " + // Đậm
//                        "-fx-text-fill: #27ae60;" // Màu chữ xanh đậm
//        );
//
//        col2.getChildren().addAll(col2Title, paymentGroup, summaryGroup);
//
//        grid.add(col1, 0, 0);
//        grid.add(col2, 1, 0);
//
//        return grid;
//    }
//
//    private HBox createInputField(String labelText, TextField textField, boolean isReadOnly) {
//        HBox row = new HBox(15);
//        row.setAlignment(Pos.CENTER_LEFT);
//
//        Label label = new Label(labelText);
//        label.setMinWidth(180); // Tăng minWidth cho label
//        label.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
//        label.setStyle("-fx-text-fill: #34495e;");
//
//        // Sử dụng Priority.ALWAYS để TextField lấp đầy không gian còn lại
//        HBox.setHgrow(textField, Priority.ALWAYS);
//        textField.setPrefHeight(40); // Tăng chiều cao
//        textField.setEditable(!isReadOnly);
//
//        if (isReadOnly) {
//            textField.setStyle(
//                    "-fx-background-color: #ecf0f1; " +
//                            "-fx-border-color: #bdc3c7; " +
//                            "-fx-border-radius: 6; " +
//                            "-fx-background-radius: 6; " +
//                            "-fx-padding: 8 10; " + // Điều chỉnh padding
//                            "-fx-font-size: 14px; " +
//                            "-fx-text-fill: #7f8c8d;"
//            );
//        } else {
//            // Phong cách cho trường có thể chỉnh sửa
//            textField.setStyle(
//                    "-fx-background-color: white; " +
//                            "-fx-border-color: #3498db; " +
//                            "-fx-border-width: 1; " + // Giảm border width
//                            "-fx-border-radius: 6; " +
//                            "-fx-background-radius: 6; " +
//                            "-fx-padding: 8 10; " +
//                            "-fx-font-size: 14px;"
//            );
//
//            // Hiệu ứng focus nhẹ nhàng
//            textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
//                if (newVal) {
//                    textField.setStyle(
//                            "-fx-background-color: white; " +
//                                    "-fx-border-color: #667eea; " + // Màu focus
//                                    "-fx-border-width: 2; " +
//                                    "-fx-border-radius: 6; " +
//                                    "-fx-background-radius: 6; " +
//                                    "-fx-padding: 8 10; " +
//                                    "-fx-font-size: 14px; " +
//                                    "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.3), 8, 0, 0, 2);"
//                    );
//                } else {
//                    textField.setStyle(
//                            "-fx-background-color: white; " +
//                                    "-fx-border-color: #3498db; " +
//                                    "-fx-border-width: 1; " +
//                                    "-fx-border-radius: 6; " +
//                                    "-fx-background-radius: 6; " +
//                                    "-fx-padding: 8 10; " +
//                                    "-fx-font-size: 14px;"
//                    );
//                }
//            });
//        }
//
//        row.getChildren().addAll(label, textField);
//        return row;
//    }
//}

package client.gui;

import client.service.CaClient;
import client.service.QLHDClient;
import common.entity.Ca;
import javafx.animation.FadeTransition;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class Gui_KetCa extends VBox {

    private TextField txtTienDauCa;
    private TextField txtTongSoHoaDon;
    private TextField txtGiamGia;
    private TextField txtTienMat;
    private TextField txtNganHang;
    private TextField txtDangPhucVu;
    private TextField txtTienCuoiCa;

    private VBox summaryCards;
    private HBox timeInfo;

    // Thêm các biến để lưu thông tin ca
    private Ca caHienTai;
    private String maNhanVien;
    private Label lblDoanhThu;
    private Label lblGiamGia;
    private Label lblGioLam;
    private Label lblChenhLech;

    public Gui_KetCa() {
        // Lấy mã nhân viên từ session đăng nhập
        this.maNhanVien = Gui_DangNhap.getCurrentMaNhanVien();

        VBox rootContent = new VBox(35);
        rootContent.setPadding(new Insets(35, 50, 35, 50));
        rootContent.setStyle("-fx-background-color: #f5f7fa;");

        // 1. Tiêu đề
        Label title = new Label("📊 Báo cáo kết ca");
        title.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 30));
        title.setStyle("-fx-text-fill: #2c3e50;");

        // 2. Thông tin Giờ
        timeInfo = createTimeInfoSection();

        // 3. Thẻ tổng quan (Summary Cards)
        summaryCards = createSummaryCards();

        // 4. Form nhập liệu chính
        GridPane mainFormGrid = createMainFormGrid();

        VBox formContainer = new VBox(mainFormGrid);
        formContainer.setPadding(new Insets(30));
        formContainer.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 20, 0, 0, 5);"
        );

        // 5. Footer với nút
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnConfirm = new Button("✅ XÁC NHẬN KẾT CA");
        btnConfirm.setStyle(
                "-fx-background-color: linear-gradient(to right, #4a4d6e, #3b3c58); " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 14 40; " +
                        "-fx-font-weight: BOLD; " +
                        "-fx-cursor: hand; " +
                        "-fx-font-size: 16px; " +
                        "-fx-background-radius: 10; " +
                        "-fx-effect: dropshadow(gaussian, rgba(74, 77, 110, 0.5), 15, 0, 0, 5);"
        );

        btnConfirm.setOnMouseEntered(e ->
                btnConfirm.setStyle(btnConfirm.getStyle()
                        .replace("#4a4d6e", "#5a5d7e")
                        .replace("#3b3c58", "#4b4c68"))
        );
        btnConfirm.setOnMouseExited(e ->
                btnConfirm.setStyle(btnConfirm.getStyle()
                        .replace("#5a5d7e", "#4a4d6e")
                        .replace("#4b4c68", "#3b3c58"))
        );

        // ✅ Xử lý kết ca
        btnConfirm.setOnAction(e -> xuLyKetCa(btnConfirm));

        HBox footer = new HBox(spacer, btnConfirm);
        footer.setPadding(new Insets(25, 0, 0, 0));
        footer.setAlignment(Pos.BOTTOM_RIGHT);

        rootContent.getChildren().addAll(title, timeInfo, summaryCards, formContainer, footer);

        // Animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), rootContent);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        // ScrollPane
        ScrollPane scrollPane = new ScrollPane(rootContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");

        this.getChildren().add(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // ✅ Load dữ liệu ca hiện tại
        loadDuLieuCa();
    }

    // ✅ Load dữ liệu ca hiện tại
    public void loadDuLieuCa() {
        try {
            CaClient caClient = new CaClient();
            caHienTai = caClient.getCaDangLam(maNhanVien);

            if (caHienTai == null) {
                showAlert(Alert.AlertType.WARNING, "Chưa vào ca",
                        "Bạn chưa vào ca làm việc!\nVui lòng vào ca trước khi kết ca.");
                return;
            }

            // Cập nhật thời gian
            updateTimeInfo();

            // Load dữ liệu vào form
            txtTienDauCa.setText(formatMoney(caHienTai.getTongTienDauCa()));
            int soLuongHD = caClient.demSoHoaDonTrongCa(maNhanVien, caHienTai.getThoiGianVaoCa());
            txtTongSoHoaDon.setText(String.valueOf(soLuongHD));
            txtGiamGia.setText(formatMoney(caClient.tinhTongTienGiamGia(maNhanVien, caHienTai.getThoiGianVaoCa())));
            txtTienMat.setText(formatMoney(caClient.tinhTongTienMat(maNhanVien, caHienTai.getThoiGianVaoCa())));
            txtNganHang.setText(formatMoney(0));
            txtDangPhucVu.setText(String.valueOf(caClient.demDonDangPhucVu(maNhanVien, caHienTai.getThoiGianVaoCa())));

            // Tính tiền cuối ca tự động
            tinhTienCuoiCa();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải dữ liệu ca làm việc!");
        }
    }

    // ✅ Cập nhật thông tin thời gian
    private void updateTimeInfo() {
        if (caHienTai == null) return;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime thoiGianVao = caHienTai.getThoiGianVaoCa();
        LocalDateTime thoiGianKet = LocalDateTime.now();

        timeInfo.getChildren().clear();

        Label startLabel = new Label("🕐 Giờ đầu ca: " + thoiGianVao.format(formatter));
        startLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 15));
        startLabel.setStyle("-fx-text-fill: #34495e;");

        Label currentLabel = new Label("⏰ Giờ kết ca: " + thoiGianKet.format(formatter));
        currentLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        currentLabel.setStyle("-fx-text-fill: #e74c3c;");

        timeInfo.getChildren().addAll(startLabel, currentLabel);

        // Cập nhật giờ làm trong summary card
        updateGioLam(thoiGianVao, thoiGianKet);
    }

    // ✅ Tính giờ làm
    private void updateGioLam(LocalDateTime start, LocalDateTime end) {
        long minutes = ChronoUnit.MINUTES.between(start, end);
        long hours = minutes / 60;
        long mins = minutes % 60;

        if (lblGioLam != null) {
            lblGioLam.setText(hours + "h " + mins + "m");
        }
    }

    // ✅ Tính tiền cuối ca tự động
    private void tinhTienCuoiCa() {
        try {
            double tienDauCa = parseMoneyField(txtTienDauCa);
            double tienMat = parseMoneyField(txtTienMat);
            double tienNganHang = parseMoneyField(txtNganHang);
            double giamGia = parseMoneyField(txtGiamGia);
//            double tienCoc = parseMoneyField(txtTienNhanCoc);

            // Tiền cuối ca = Tiền đầu ca + Tiền mặt + Chuyển khoản - Giảm giá + Tiền cọc
            double tienCuoiCa = tienDauCa + tienMat + tienNganHang - giamGia ;
//    + tienCoc
            txtTienCuoiCa.setText(formatMoney(tienCuoiCa));

            // Cập nhật summary cards
            updateSummaryCards(tienMat + tienNganHang, giamGia, tienCuoiCa - tienDauCa);

        } catch (Exception e) {
            txtTienCuoiCa.setText(formatMoney(0));
        }
    }

    // ✅ Cập nhật summary cards
    private void updateSummaryCards(double doanhThu, double giamGia, double chenhLech) {
        if (lblDoanhThu != null) lblDoanhThu.setText(formatMoney(doanhThu) + " VND");
        if (lblGiamGia != null) lblGiamGia.setText(formatMoney(giamGia) + " VND");
        if (lblChenhLech != null) {
            lblChenhLech.setText((chenhLech >= 0 ? "+ " : "- ") + formatMoney(Math.abs(chenhLech)) + " VND");
        }
    }

    // ✅ Xử lý kết ca
    private void xuLyKetCa(Button btn) {
        if (caHienTai == null) {
            showAlert(Alert.AlertType.WARNING, "Lỗi", "Không tìm thấy ca đang làm!");
            return;
        }

        // Xác nhận
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận kết ca");
        confirm.setHeaderText("Bạn có chắc muốn kết ca?");
        confirm.setContentText("Tiền cuối ca: " + txtTienCuoiCa.getText() + " VND");

        if (confirm.showAndWait().get() != ButtonType.OK) {
            return;
        }

        // Disable button để tránh click nhiều lần
        btn.setDisable(true);

        try {
            double tienCuoiCa = parseMoneyField(txtTienCuoiCa);

            CaClient caClient = new CaClient();
            boolean success = caClient.ketCa(caHienTai.getMaCa(), tienCuoiCa);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công",
                        "Kết ca thành công!\n" +
                                "Mã ca: " + caHienTai.getMaCa() + "\n" +
                                "Tiền cuối ca: " + formatMoney(tienCuoiCa) + " VND");

                // Reset form
                caHienTai = null;
                resetForm();
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể kết ca! Vui lòng thử lại.");
                btn.setDisable(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Đã xảy ra lỗi: " + e.getMessage());
            btn.setDisable(false);
        }
    }

    // ✅ Reset form sau khi kết ca
    private void resetForm() {
        txtTienDauCa.clear();
        txtTongSoHoaDon.clear();
        txtGiamGia.clear();
        txtTienMat.clear();
        txtNganHang.clear();
//        txtTienNhanCoc.clear();
        txtDangPhucVu.clear();
        txtTienCuoiCa.clear();
    }

    // ✅ Format tiền
    private String formatMoney(double amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("###,###,###", symbols);
        return formatter.format(amount);
    }

    // ✅ Parse tiền từ TextField
    private double parseMoneyField(TextField field) {
        if (field == null || field.getText().isEmpty()) return 0;
        try {
            return Double.parseDouble(field.getText().replaceAll("[^\\d]", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private HBox createTimeInfoSection() {
        HBox timeBox = new HBox(50);
        timeBox.setPadding(new Insets(20));
        timeBox.setAlignment(Pos.CENTER_LEFT);
        timeBox.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 12; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 3);"
        );

        Label startLabel = new Label("🕐 Giờ đầu ca: --");
        startLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 15));
        startLabel.setStyle("-fx-text-fill: #34495e;");

        Label currentLabel = new Label("⏰ Giờ kết ca: --");
        currentLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        currentLabel.setStyle("-fx-text-fill: #e74c3c;");

        timeBox.getChildren().addAll(startLabel, currentLabel);
        return timeBox;
    }

    private VBox createSummaryCards() {
        HBox cardContainer = new HBox(25);
        cardContainer.setPadding(new Insets(0));

        VBox card1 = createSummaryCard("💰 Doanh thu", "0 VND", "#2ecc71", "#e8f8f5");
        VBox card2 = createSummaryCard("💸 Giảm giá/Chi", "0 VND", "#e67e22", "#fef5e7");
        VBox card3 = createSummaryCard("⏱️ Giờ làm", "0h 0m", "#3498db", "#ebf5fb");
        VBox card4 = createSummaryCard("∆ Chênh lệch", "0 VND", "#2ecc71", "#e8f8f5");

        // Lưu reference để update sau
        lblDoanhThu = (Label) card1.getChildren().get(1);
        lblGiamGia = (Label) card2.getChildren().get(1);
        lblGioLam = (Label) card3.getChildren().get(1);
        lblChenhLech = (Label) card4.getChildren().get(1);

        cardContainer.getChildren().addAll(card1, card2, card3, card4);

        HBox.setHgrow(card1, Priority.ALWAYS);
        HBox.setHgrow(card2, Priority.ALWAYS);
        HBox.setHgrow(card3, Priority.ALWAYS);
        HBox.setHgrow(card4, Priority.ALWAYS);

        return new VBox(cardContainer);
    }

    private VBox createSummaryCard(String title, String value, String color, String bgColor) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(20));
        card.setStyle(
                "-fx-background-color: " + bgColor + "; " +
                        "-fx-background-radius: 12; " +
                        "-fx-border-color: " + color + "; " +
                        "-fx-border-width: 0 0 0 4; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 3);"
        );
        card.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        titleLabel.setStyle("-fx-text-fill: #5e6d82;");

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 20));
        valueLabel.setStyle("-fx-text-fill: " + color + ";");

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    private GridPane createMainFormGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(50);
        grid.setVgap(0);
        grid.setAlignment(Pos.TOP_LEFT);

        ColumnConstraints col1Const = new ColumnConstraints();
        col1Const.setPercentWidth(50);
        ColumnConstraints col2Const = new ColumnConstraints();
        col2Const.setPercentWidth(50);
        grid.getColumnConstraints().addAll(col1Const, col2Const);

        // CỘT 1
        VBox col1 = new VBox(25);

        Label col1Title = new Label("📝 Thông tin tổng kết ca");
        col1Title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        col1Title.setStyle("-fx-text-fill: #2c3e50;");

        VBox infoGroup = new VBox(15);
        infoGroup.getChildren().addAll(
                createInputField("Tiền đầu ca (Tiền mặt)", txtTienDauCa = new TextField(), true),
                createInputField("Tổng số hóa đơn", txtTongSoHoaDon = new TextField(), true),
                createInputField("Tổng tiền giảm giá", txtGiamGia = new TextField(), true)
        );

        col1.getChildren().addAll(col1Title, infoGroup);

        // CỘT 2
        VBox col2 = new VBox(25);

        Label col2Title = new Label("💵 Chi tiết thanh toán");
        col2Title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        col2Title.setStyle("-fx-text-fill: #2c3e50;");

        VBox paymentGroup = new VBox(15);

        txtTienMat = new TextField();
        txtNganHang = new TextField();
//        txtTienNhanCoc = new TextField();

        // ✅ Thêm listener để tự động tính tiền cuối ca
        txtTienMat.textProperty().addListener((obs, old, val) -> tinhTienCuoiCa());
        txtNganHang.textProperty().addListener((obs, old, val) -> tinhTienCuoiCa());
//        txtTienNhanCoc.textProperty().addListener((obs, old, val) -> tinhTienCuoiCa());

        paymentGroup.getChildren().addAll(
                createInputField("Tiền mặt thu được", txtTienMat, true),
                createInputField("Chuyển khoản (Ngân hàng)", txtNganHang, true)
//                createInputField("Tiền nhận cọc", txtTienNhanCoc, false)
        );

        VBox summaryGroup = new VBox(15);

        Label otherTitle = new Label("📊 Tổng kết");
        otherTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        otherTitle.setStyle("-fx-text-fill: #2c3e50; -fx-padding: 10 0 0 0;");

        summaryGroup.getChildren().addAll(
                otherTitle,
                createInputField("Đang phục vụ", txtDangPhucVu = new TextField(), true),
                createInputField("💰 TIỀN CUỐI CA", txtTienCuoiCa = new TextField(), true)
        );

        txtTienCuoiCa.setStyle(
                "-fx-background-color: #e8f8f5; " +
                        "-fx-border-color: #2ecc71; " +
                        "-fx-border-radius: 6; " +
                        "-fx-background-radius: 6; " +
                        "-fx-padding: 10; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #27ae60;"
        );

        col2.getChildren().addAll(col2Title, paymentGroup, summaryGroup);

        grid.add(col1, 0, 0);
        grid.add(col2, 1, 0);

        return grid;
    }

    private HBox createInputField(String labelText, TextField textField, boolean isReadOnly) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);

        Label label = new Label(labelText);
        label.setMinWidth(180);
        label.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        label.setStyle("-fx-text-fill: #34495e;");

        HBox.setHgrow(textField, Priority.ALWAYS);
        textField.setPrefHeight(40);
        textField.setEditable(!isReadOnly);

        if (isReadOnly) {
            textField.setStyle(
                    "-fx-background-color: #ecf0f1; " +
                            "-fx-border-color: #bdc3c7; " +
                            "-fx-border-radius: 6; " +
                            "-fx-background-radius: 6; " +
                            "-fx-padding: 8 10; " +
                            "-fx-font-size: 14px; " +
                            "-fx-text-fill: #7f8c8d;"
            );
        } else {
            textField.setStyle(
                    "-fx-background-color: white; " +
                            "-fx-border-color: #3498db; " +
                            "-fx-border-width: 1; " +
                            "-fx-border-radius: 6; " +
                            "-fx-background-radius: 6; " +
                            "-fx-padding: 8 10; " +
                            "-fx-font-size: 14px;"
            );

            textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    textField.setStyle(
                            "-fx-background-color: white; " +
                                    "-fx-border-color: #667eea; " +
                                    "-fx-border-width: 2; " +
                                    "-fx-border-radius: 6; " +
                                    "-fx-background-radius: 6; " +
                                    "-fx-padding: 8 10; " +
                                    "-fx-font-size: 14px; " +
                                    "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.3), 8, 0, 0, 2);"
                    );
                } else {
                    textField.setStyle(
                            "-fx-background-color: white; " +
                                    "-fx-border-color: #3498db; " +
                                    "-fx-border-width: 1; " +
                                    "-fx-border-radius: 6; " +
                                    "-fx-background-radius: 6; " +
                                    "-fx-padding: 8 10; " +
                                    "-fx-font-size: 14px;"
                    );
                }
            });
        }

        row.getChildren().addAll(label, textField);
        return row;
    }
}
