
package gui;

import ctrl.ThongKe_Ctrl;
import dao.ThongKe_DAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Side;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ThongKe extends VBox {
    private ComboBox<String> cbGroup;
    private ComboBox<String> cbArea;
    private DatePicker datePickerFrom;
    private DatePicker datePickerTo;
    ThongKe_Ctrl thongKeCtrl = new ThongKe_Ctrl();

    // ===== PHƯƠNG THỨC HỖ TRỢ ÁP DỤNG STYLE CHO BIỂU ĐỒ =====
    private void applyBlackTextStyle(Chart chart, Axis xAxis, Axis yAxis) {
        String chartStyle =
                "-fx-text-fill: black;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 12px;";

        String axisStyle =
                "-fx-tick-label-fill: black;" +
                        "-fx-axis-label-fill: black;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 11px;";

        // Áp dụng style cho biểu đồ
        chart.setStyle(chartStyle);

        // Áp dụng style cho các trục
        if (xAxis != null) {
            xAxis.setStyle(axisStyle);
        }
        if (yAxis != null) {
            yAxis.setStyle(axisStyle);
        }

        // Style cho tiêu đề biểu đồ (sau khi biểu đồ được render)
        // Sử dụng Platform.runLater để đảm bảo biểu đồ đã được render trước khi lookup
        javafx.application.Platform.runLater(() -> {
            if (chart.lookup(".chart-title") != null) {
                chart.lookup(".chart-title").setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 14px;");
            }

            // Style cho legend nếu có
            if (chart.lookup(".chart-legend") != null) {
                chart.lookup(".chart-legend").setStyle("-fx-text-fill: black;");
            }

            // Style cho các nhãn trên biểu đồ
            if (chart.lookup(".chart-content") != null) {
                chart.lookup(".chart-content").setStyle("-fx-text-fill: black;");
            }
        });
    }

    public ThongKe() {
        // Container chính với animation
        VBox rootContent = new VBox(40);
        rootContent.setPadding(new Insets(30, 40, 30, 40));
        rootContent.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa 0%, #e9ecef 100%);");

        // Header với gradient
        VBox header = createModernHeader();

        // Filter bar với modern styling
        HBox filterBar = createModernFilterBar();

        // Dashboard sections
        VBox dashboardContent = new VBox(45);
        dashboardContent.getChildren().addAll(
                createRevenueSection(),
                createBookingSection(),
                createMenuSection(),
                createCustomerSection()
        );

        rootContent.getChildren().addAll(header, filterBar, dashboardContent);

        // Smooth fade-in animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), rootContent);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        // ScrollPane với smooth scrolling
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(rootContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        this.getChildren().add(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
    }

    // ===== MODERN HEADER =====
    private VBox createModernHeader() {
        VBox header = new VBox(10);
        header.setPadding(new Insets(20, 0, 20, 0));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle(
                "-fx-background-color: #082744;" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 30;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 4);"
        );

        Label title = new Label("📊 BÁO CÁO TỔNG HỢP & PHÂN TÍCH HIỆU SUẤT");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: white;");

        Label subtitle = new Label("Dashboard Quản lý Nhà hàng - Real-time Analytics");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        subtitle.setStyle("-fx-text-fill: rgba(255,255,255,0.9);");

        header.getChildren().addAll(title, subtitle);
        return header;
    }

    // ===== MODERN FILTER BAR =====
    private HBox createModernFilterBar() {
        HBox filterBar = new HBox(15);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(20));
        filterBar.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);"
        );

        // --- 1. COMBOBOX NHÓM THEO ---
        Label lblGroup = new Label("📅 Nhóm theo:");
        lblGroup.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 12));

        cbGroup = new ComboBox<>(FXCollections.observableArrayList(
                "Theo Ngày", "Theo Tuần", "Theo Tháng", "Theo Năm"
        ));
        cbGroup.getSelectionModel().select("Theo Tháng"); // Mặc định
        cbGroup.setStyle("-fx-background-radius: 3;");

        // SỰ KIỆN: Khi chọn thay đổi -> Load lại dữ liệu
        cbGroup.setOnAction(e -> loadDashboardData());

        // --- 2. COMBOBOX KHU VỰC ---
        Label lblArea = new Label("📍 Khu vực:");
        lblArea.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 12));

        cbArea = new ComboBox<>(FXCollections.observableArrayList(
                "Tất cả", "Khu A", "Khu B", "Phòng VIP", "Sân vườn"
        ));
        cbArea.getSelectionModel().selectFirst();
        cbArea.setStyle("-fx-background-radius: 3;");

        // SỰ KIỆN: Khi chọn khu vực -> Load lại dữ liệu
        cbArea.setOnAction(e -> loadDashboardData());

        // --- 3. DATE PICKERS (TỪ NGÀY - ĐẾN NGÀY) ---
        datePickerFrom = new DatePicker();
        datePickerFrom.setPromptText("Từ ngày...");
        datePickerFrom.setStyle("-fx-background-radius: 3;");

        // Mặc định là ngày đầu tháng
        datePickerFrom.setValue(java.time.LocalDate.now().withDayOfMonth(1));

        datePickerTo = new DatePicker();
        datePickerTo.setPromptText("Đến ngày...");
        datePickerTo.setStyle("-fx-background-radius: 3;");

        // Mặc định là ngày hiện tại
        datePickerTo.setValue(java.time.LocalDate.now());

        // SỰ KIỆN: Khi chọn ngày xong -> Load lại dữ liệu
        datePickerFrom.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && datePickerTo.getValue() != null) {
                if (newVal.isAfter(datePickerTo.getValue())) {
                    showAlert("Lỗi ngày tháng", "Ngày bắt đầu không được lớn hơn ngày kết thúc!");
                    datePickerFrom.setValue(oldVal); // Reset lại
                } else {
                    loadDashboardData();
                }
            }
        });

        datePickerTo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && datePickerFrom.getValue() != null) {
                if (newVal.isBefore(datePickerFrom.getValue())) {
                    showAlert("Lỗi ngày tháng", "Ngày kết thúc không được nhỏ hơn ngày bắt đầu!");
                    datePickerTo.setValue(oldVal);
                } else {
                    loadDashboardData();
                }
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // --- 4. BUTTON XUẤT BÁO CÁO ---
        Button btnExport = new Button("⬇️ Xuất Báo Cáo");
        btnExport.setStyle(
                "-fx-background-color: #082744; -fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-background-radius: 8; -fx-padding: 10 20; -fx-cursor: hand;"
        );
        // Hover effects
        btnExport.setOnMouseEntered(e -> btnExport.setStyle("-fx-background-color: #082744; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20; -fx-cursor: hand; -fx-scale-x: 1.05; -fx-scale-y: 1.05;"));
        btnExport.setOnMouseExited(e -> btnExport.setStyle("-fx-background-color: #082744; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20; -fx-cursor: hand;"));

        // SỰ KIỆN: Xuất Excel
        btnExport.setOnAction(e -> handleExportReport());

        // --- 5. BUTTON SO SÁNH ---
        Button btnCompare = new Button("📊 So sánh Kỳ trước");
        btnCompare.setStyle("-fx-background-color: #f1f3f5; -fx-text-fill: #495057; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20; -fx-cursor: hand;");

        // SỰ KIỆN: Toggle chế độ so sánh
        btnCompare.setOnAction(e -> {
            boolean isSelected = btnCompare.getStyle().contains("#ffeaa7"); // Check flag đơn giản qua màu
            if (!isSelected) {
                btnCompare.setStyle("-fx-background-color: #ffeaa7; -fx-text-fill: #d35400; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20; -fx-cursor: hand;");
                // Gọi hàm logic so sánh ở đây
            } else {
                btnCompare.setStyle("-fx-background-color: #f1f3f5; -fx-text-fill: #495057; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 20; -fx-cursor: hand;");
            }
        });

        filterBar.getChildren().addAll(
                lblGroup, cbGroup, lblArea, cbArea,
                datePickerFrom, datePickerTo, spacer, btnCompare, btnExport
        );
        return filterBar;
    }

    // ===== 1. REVENUE SECTION =====
    private VBox createRevenueSection() {
        Label sectionTitle = createModernSectionTitle("💰 THỐNG KÊ DOANH THU", "#667eea");

        GridPane statGrid = new GridPane();
        statGrid.setHgap(20);
        statGrid.setVgap(20);
        statGrid.setPadding(new Insets(15, 0, 25, 0));
        statGrid.add(createModernStatCard("Tổng Doanh thu", String.format("%.2f", thongKeCtrl.getTongDoanhThu()), "VND", "↑ 12%", "So với tháng trước", "#667eea", "💰"), 0, 0);
        statGrid.add(createModernStatCard("DT TB/Bàn", String.format("%.2f", thongKeCtrl.getDoanhThuTBBan()), "VND", "↑ 8%", "Cao điểm: 650K", "#764ba2", "💸"), 1, 0);
        statGrid.add(createModernStatCard("TT Tiền mặt", String.format("%.2f", thongKeCtrl.getTiLeTienMat()), "%", "↓ 5%", "Thẻ/Ví: " + (100 - thongKeCtrl.getTiLeTienMat()) + "%", "#f093fb", "💵"), 2, 0);
        statGrid.add(createModernStatCard("DT Ca Tối", String.format("%.2f", thongKeCtrl.getDoanhThuCaToi()), "VND", "↑ 15%", "Chiếm 50% tổng", "#4facfe", "🌙"), 3, 0);

        // --- 1. SETUP COMBOBOX NĂM ---
        ComboBox<String> cbbYear = new ComboBox<>();
        cbbYear.getItems().addAll("2023", "2024", "2025");
        int currentYear = java.time.Year.now().getValue();
        cbbYear.setValue(String.valueOf(currentYear));

        cbbYear.setStyle("-fx-font-size: 11px; -fx-background-color: white; -fx-background-radius: 3; -fx-cursor: hand;" +
                "-fx-padding: 0 5 0 5; -fx-pref-height: 22px; -fx-min-height: 22px; -fx-max-height: 22px;");

        // --- 2. TẠO BIỂU ĐỒ TRỐNG (CẤU HÌNH CƠ BẢN) ---
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Triệu VND");
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setLegendVisible(false);
        barChart.setBarGap(3);
        applyBlackTextStyle(barChart, xAxis, yAxis);
        barChart.setPrefHeight(300);

        // --- 3. NẠP DỮ LIỆU LẦN ĐẦU & SỰ KIỆN COMBOBOX ---
        // Nạp dữ liệu năm hiện tại ngay khi mở màn hình
        updateChartData(barChart, currentYear);

        // Khi chọn năm mới -> Nạp lại dữ liệu cho chính barChart này
        cbbYear.setOnAction(e -> {
            String selectedYearStr = cbbYear.getValue();
            if (selectedYearStr != null) {
                int selectedYear = Integer.parseInt(selectedYearStr);
                updateChartData(barChart, selectedYear);
            }
        });

        // --- 4. ĐÓNG GÓI VÀO UI ---
        GridPane chartGrid = new GridPane();
        chartGrid.setHgap(25);
        chartGrid.setVgap(25);

        // Bọc barChart vào VBox để khớp với tham số của createStyledChart
        VBox chartContainer = new VBox(barChart);
        chartContainer.setAlignment(Pos.CENTER);
        chartContainer.setPadding(new Insets(10));

        // TRUYỀN chartContainer (chứa barChart đã setup) VÀO ĐÂY
        // Không gọi createMonthlyRevenueChart() nữa
        VBox monthlyRevenueChart = createStyledChart(
                chartContainer,
                "Doanh thu Theo Tháng",
                cbbYear
        );

        // BIỂU ĐỒ SO SÁNH DOANH THU THEO BUỔI
        VBox revenueBySessionChart = createStyledChart(createRevenueBySessionChart(), "Doanh thu Theo Buổi trong Ngày", null);

        GridPane.setHgrow(monthlyRevenueChart, Priority.ALWAYS);
        GridPane.setHgrow(revenueBySessionChart, Priority.ALWAYS);

        chartGrid.add(monthlyRevenueChart, 0, 0);
        chartGrid.add(revenueBySessionChart, 1, 0);

        return new VBox(25, sectionTitle, statGrid, chartGrid);
    }

    // ===== 2. BOOKING SECTION =====
    private VBox createBookingSection() {
        Label sectionTitle = createModernSectionTitle("📋 THỐNG KÊ ĐẶT BÀN & SỬ DỤNG BÀN", "#082744");

        GridPane statGrid = new GridPane();
        statGrid.setHgap(20);
        statGrid.setVgap(20);
        statGrid.setPadding(new Insets(15, 0, 25, 0));

        statGrid.add(createModernStatCard("Lượt đặt bàn", "1,250", "lượt", "↑ 95%", "Thành công", "#f093fb", "📋"), 0, 0);
        statGrid.add(createModernStatCard("Lấp đầy Bàn", "78", "%", "↑ 3%", "Cao điểm: 95%", "#4facfe", "📈"), 1, 0);
        statGrid.add(createModernStatCard("Hủy/No-show", "4.5", "%", "↓ 1%", "Cải thiện tốt", "#43e97b", "❌"), 2, 0);
        statGrid.add(createModernStatCard("Xoay vòng TB", "55", "phút", "↓ 5min", "Mục tiêu: <60p", "#fa709a", "⏱️"), 3, 0);

        GridPane chartGrid = new GridPane();
        chartGrid.setHgap(25);
        chartGrid.setVgap(25);

        VBox successChart = createStyledChart(createBookingSuccessRateChart(), "Tỷ lệ thành công", null);
        VBox turnoverChart = createStyledChart(createTableTurnoverChart(), "Hiệu suất theo khu vực", null);

        GridPane.setHgrow(successChart, Priority.ALWAYS);
        GridPane.setHgrow(turnoverChart, Priority.ALWAYS);

        chartGrid.add(successChart, 0, 0);
        chartGrid.add(turnoverChart, 1, 0);

        return new VBox(25, sectionTitle, statGrid, chartGrid);
    }

    // ===== 3. MENU SECTION =====
    private VBox createMenuSection() {
        Label sectionTitle = createModernSectionTitle("🍽️ THỐNG KÊ MÓN ĂN & ĐỒ UỐNG", "#059a1e");

        GridPane statGrid = new GridPane();
        statGrid.setHgap(20);
        statGrid.setVgap(20);
        statGrid.setPadding(new Insets(15, 0, 25, 0));

        // --- LẤY DỮ LIỆU THỰC ---

        // 1. Món bán chạy nhất (Số lượng)
        String[] bestSeller = thongKeCtrl.getMonBanChayNhat();
        String tenMonBanChay = bestSeller[0];
        String slBanChay = bestSeller[1];

        // 2. Món doanh thu cao nhất (Tiền)
        String[] topRevenueItem = thongKeCtrl.getMonDoanhThuCaoNhat();
        String tenMonDT = topRevenueItem[0];
        double tienMonDT = Double.parseDouble(topRevenueItem[1]);

        // 3. Đồ uống
        double[] drinkStats = thongKeCtrl.getThongKeDoUong();
        double dtDoUong = drinkStats[0];
        double tongDT = drinkStats[1];
        double phanTramDoUong = (tongDT > 0) ? (dtDoUong / tongDT * 100) : 0;

        // --- TẠO CARD ---

        // Card 1: Bán chạy nhất
        statGrid.add(createModernStatCard(
                "Bán chạy nhất",
                slBanChay,      // Số lượng thật
                "đã bán",
                "🥖",             // Không có trend
                tenMonBanChay,  // Tên món (VD: Cơm gà)
                "#43e97b",
                "🥖"
        ), 0, 0);

        // Card 2: Doanh thu cao (Thay cho Lợi nhuận)
        statGrid.add(createModernStatCard(
                "Doanh thu cao",
                formatToK(tienMonDT), // Số tiền thật format K
                "VND",
                "Top 1",
                tenMonDT,             // Tên món (VD: Bia)
                "#fa709a",
                "🏆"
        ), 1, 0);

        // Card 3: Doanh thu Đồ uống
        statGrid.add(createModernStatCard(
                "DT Đồ uống",
                formatToK(dtDoUong), // Số tiền thật
                "VND",
                String.format("%.0f%%", phanTramDoUong), // Phần trăm thật
                "tổng doanh thu",
                "#fee140",
                "🥤"
        ), 2, 0);

        GridPane chartGrid = new GridPane();
        chartGrid.setHgap(25);
        chartGrid.setVgap(25);

        VBox topSellingChart = createStyledChart(createTopSellingMenuChart(), "Top 5 món bán chạy", null);
        VBox menuGroupChart = createStyledChart(createRevenueByMenuGroupChart(), "Doanh thu theo nhóm", null);

        GridPane.setHgrow(topSellingChart, Priority.ALWAYS);
        GridPane.setHgrow(menuGroupChart, Priority.ALWAYS);

        chartGrid.add(topSellingChart, 0, 0);
        chartGrid.add(menuGroupChart, 1, 0);

        return new VBox(25, sectionTitle, statGrid, chartGrid);
    }

    // ===== 5. CUSTOMER SECTION =====
    // Hàm hỗ trợ rút gọn số (Thêm hàm này vào class ThongKe)
    private String formatToK(double value) {
        if (value >= 1000000) {
            return String.format("%.1fM", value / 1000000);
        } else if (value >= 1000) {
            return String.format("%.0fK", value / 1000);
        }
        return String.format("%.0f", value);
    }

    private VBox createCustomerSection() {
        Label sectionTitle = createModernSectionTitle("👤 THỐNG KÊ KHÁCH HÀNG", "#30cfd0");

        GridPane statGrid = new GridPane();
        statGrid.setHgap(20);
        statGrid.setVgap(20);
        statGrid.setPadding(new Insets(15, 0, 25, 0));

        // --- LẤY DỮ LIỆU THỰC ---
        // 1. Khách hàng
        int[] khachStats = thongKeCtrl.getThongKeKhachHang();
        int tongKhach = khachStats[0];
        int khachThanThiet = khachStats[1];

        // 2. Chi tiêu TB
        double chiTieuTB = thongKeCtrl.getChiTieuTrungBinh();

        // 3. Tần suất
        double tanSuat = thongKeCtrl.getTanSuatTrungBinh();

        // --- TẠO CARD VỚI SỐ LIỆU THẬT ---

        // Card 1: Tổng khách (Màu xanh Teal đậm hơn xíu)
        statGrid.add(createModernStatCard(
                "Tổng Khách",
                String.valueOf(tongKhach),
                "khách",
                String.valueOf(khachThanThiet),
                "Thân thiết (>200đ)",
                "#17a2b8", // Đổi từ #30cfd0 sang màu đậm hơn
                "👥"
        ), 0, 0);

// Card 2: Chi tiêu TB (Màu xanh dương)
        statGrid.add(createModernStatCard(
                "Chi tiêu TB",
                formatToK(chiTieuTB),
                "VND",
                "↑ 5%",
                "So kỳ trước",
                "#20bf6b", // Màu xanh lá (hoặc giữ màu cũ nếu thích)
                "💳"
        ), 1, 0);

// Card 3: Tần suất (Màu cam/hồng đậm)
        statGrid.add(createModernStatCard(
                "Tần suất TB",
                String.format("%.1f", tanSuat),
                "lần/khách",
                "2.5",
                "Khách VIP",
                "#fd79a8", // Đổi từ #fed6e3 sang hồng đậm hơn
                "🔄"
        ), 2, 0);
        GridPane chartGrid = new GridPane();
        chartGrid.setHgap(25);
        chartGrid.setVgap(25);

        VBox topCustomers = createStyledChart(createTopCustomersList(), "Top 5 khách hàng", null);

        GridPane.setHgrow(topCustomers, Priority.ALWAYS);
        chartGrid.add(topCustomers, 0, 0);

        return new VBox(25, sectionTitle, statGrid, chartGrid);
    }


    // ===== MODERN STAT CARD =====
//    private VBox createModernStatCard(String title, String mainValue, String unit,
//                                      String trend, String subText, String color, String icon) {
//        VBox card = new VBox(8);
//        card.setPadding(new Insets(20));
//        card.setPrefSize(290, 140);
//        card.setStyle(
//                "-fx-background-color: white;" +
//                        "-fx-background-radius: 15;" +
//                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 3);"
//        );
//
//        // Hover effect
//        card.setOnMouseEntered(e -> card.setStyle(
//                "-fx-background-color: white;" +
//                        "-fx-background-radius: 15;" +
//                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 20, 0, 0, 5);" +
//                        "-fx-scale-x: 1.02;" +
//                        "-fx-scale-y: 1.02;"
//        ));
//
//        card.setOnMouseExited(e -> card.setStyle(
//                "-fx-background-color: white;" +
//                        "-fx-background-radius: 15;" +
//                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 3);"
//        ));
//
//        // Top row: Icon + Title
//        HBox topRow = new HBox(10);
//        topRow.setAlignment(Pos.CENTER_LEFT);
//
//        Label iconLabel = new Label(icon);
//        iconLabel.setFont(Font.font("Segoe UI Emoji", 28));
//        iconLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2); -fx-background-color: #30cfd0; -fx-border-radius: 50%;" +
//                "-fx-padding: 2px 5px");
//
//        Label titleLabel = new Label(title);
//        titleLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
//        titleLabel.setStyle("-fx-text-fill: #495057;");
//        titleLabel.setWrapText(true);
//
//        topRow.getChildren().addAll(iconLabel, titleLabel);
//
//        // Main value with unit
//        HBox valueRow = new HBox(5);
//        valueRow.setAlignment(Pos.BASELINE_LEFT);
//
//        Label value = new Label(mainValue);
//        value.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
//
//        // SỬA Ở ĐÂY: Thay vì dùng màu pastel (color), hãy dùng màu xám đậm
//        value.setStyle("-fx-text-fill: #2c3e50;");
//
//        Label unitLabel = new Label(unit);
//        unitLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
//        unitLabel.setStyle("-fx-text-fill: #95a5a6;"); // Màu xám nhạt hơn cho đơn vị
//
//        valueRow.getChildren().addAll(value, unitLabel);

    /// / Trend indicator
//        Label trendLabel = new Label(trend);
//        trendLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
//
//        // Logic màu sắc thông minh hơn
//        String bgHex;
//        String textHex;
//
//        if (trend.contains("↑")) {
//            bgHex = "#d4edda"; // Nền xanh lá nhạt
//            textHex = "#155724"; // Chữ xanh lá đậm
//        } else if (trend.contains("↓")) {
//            bgHex = "#f8d7da"; // Nền đỏ nhạt
//            textHex = "#721c24"; // Chữ đỏ đậm
//        } else {
//            // Trường hợp hiển thị số lượng (như số 6, 2.5) -> Dùng màu chủ đạo của Card
//            bgHex = color + "22"; // Thêm độ trong suốt
//            textHex = color;      // Chữ cùng màu với icon
//            // Lưu ý: color đầu vào phải là mã Hex đậm một chút thì chữ mới rõ
//        }
//
//        trendLabel.setStyle(
//                "-fx-text-fill: " + textHex + ";" +
//                        "-fx-background-color: " + bgHex + ";" +
//                        "-fx-background-radius: 5;" +
//                        "-fx-padding: 3 8;"
//        );
//
//        // Subtitle
//        Label subLabel = new Label(subText);
//        subLabel.setFont(Font.font("Segoe UI", 11));
//        subLabel.setStyle("-fx-text-fill: #6c757d; -fx-font-size: 13; -fx-font-weight: bold;");
//
//        card.getChildren().addAll(topRow, valueRow, trendLabel, subLabel);
//        return card;
//    }
    private VBox createModernStatCard(String title, String mainValue, String unit,
                                      String trend, String subText, String color, String icon) {
        // 1. Tăng khoảng cách giữa các dòng lên 12 cho thoáng
        VBox card = new VBox(12);
        card.setPadding(new Insets(20));
        card.setPrefSize(290, 140);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 3);"
        );

        // Hover effect (Hiệu ứng khi di chuột)
        card.setOnMouseEntered(e -> card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 5);" +
                        "-fx-scale-x: 1.02;" +
                        "-fx-scale-y: 1.02;"
        ));

        card.setOnMouseExited(e -> card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 3);"
        ));

        // --- ROW 1: Icon + Title ---
        HBox topRow = new HBox(12);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font("Segoe UI Emoji", 26));
        // SỬA: Dùng màu động (color) làm nền nhạt, thay vì fix cứng màu xanh
        iconLabel.setStyle(
                "-fx-text-fill: " + color + ";" +
                        "-fx-background-color: " + color + "22;" + // Thêm 22 vào sau mã hex để tạo độ trong suốt (~13%)
                        "-fx-background-radius: 50%;" + // Bo tròn thành hình tròn
                        "-fx-min-width: 45; -fx-min-height: 45;" +
                        "-fx-alignment: center;"
        );

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        titleLabel.setStyle("-fx-text-fill: #636e72;");

        topRow.getChildren().addAll(iconLabel, titleLabel);

        // --- ROW 2: Main Value ---
        HBox valueRow = new HBox(6);
        valueRow.setAlignment(Pos.BASELINE_LEFT);

        Label value = new Label(mainValue);
        value.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
        value.setStyle("-fx-text-fill: #2d3436;"); // Màu đen xám đậm dễ đọc

        Label unitLabel = new Label(unit);
        unitLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        unitLabel.setStyle("-fx-text-fill: #b2bec3;");

        valueRow.getChildren().addAll(value, unitLabel);

        // --- ROW 3: Footer (Trend + Subtext nằm cùng 1 dòng HBox) ---
        HBox footerRow = new HBox(10); // Khoảng cách giữa badge và text
        footerRow.setAlignment(Pos.CENTER_LEFT);

        // A. Trend Badge
        Label trendLabel = new Label(trend);
        trendLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));

        // Logic màu sắc
        String bgHex;
        String textHex;
        if (trend.contains("↑")) {
            bgHex = "#d4edda";
            textHex = "#155724"; // Xanh lá
        } else if (trend.contains("↓")) {
            bgHex = "#f8d7da";
            textHex = "#721c24"; // Đỏ
        } else {
            // Màu theo chủ đề của card nếu không có mũi tên
            bgHex = color + "22";
            textHex = color;
        }

        trendLabel.setStyle(
                "-fx-text-fill: " + textHex + ";" +
                        "-fx-background-color: " + bgHex + ";" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 4 8;"
        );

        // B. Subtext
        Label subLabel = new Label(subText);
        // Sửa font bold theo ý bạn (-fx-font-weight: bold)
        subLabel.setStyle("-fx-text-fill: #a4b0be; -fx-font-size: 12px; -fx-font-weight: bold;");

        // Thêm cả 2 vào footerRow thay vì thêm lẻ tẻ vào card
        footerRow.getChildren().addAll(trendLabel, subLabel);

        // --- ADD ALL TO CARD ---
        card.getChildren().addAll(topRow, valueRow, footerRow);

        return card;
    }

    // ===== MODERN SECTION TITLE =====
    private Label createModernSectionTitle(String title, String color) {
        Label label = new Label(title);
        label.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        label.setStyle(
                "-fx-text-fill: " + color + ";" +
                        "-fx-padding: 10 0 10 15;" +
                        "-fx-border-width: 0 0 0 5;" +
                        "-fx-border-color: " + color + ";"
        );
        return label;
    }

    // ===== STYLED CHART WRAPPER =====
    private VBox createStyledChart(VBox chartVBox, String subtitle, Node rightControl) {
        VBox wrapper = new VBox(10);
        wrapper.setPadding(new Insets(20));
        wrapper.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 12, 0, 0, 2);"
        );

        // 1. Tạo Label Title
        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 12));
        subtitleLabel.setStyle("-fx-text-fill: #6c757d;");

        // 2. Tạo HBox chứa Title + Control (MỚI)
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT); // Căn giữa theo chiều dọc để đẹp
        header.setMinHeight(30); // Đảm bảo chiều cao tối thiểu cho header

        if (rightControl != null) {
            // Nếu có control (ComboBox), dùng Spacer đẩy sang phải
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            header.getChildren().addAll(subtitleLabel, spacer, rightControl);
        } else {
            // Nếu không có, chỉ hiện label
            header.getChildren().add(subtitleLabel);
        }

        // 3. Add Header và Chart vào Wrapper
        wrapper.getChildren().addAll(header, chartVBox);
        return wrapper;
    }


    private void updateChartData(BarChart<String, Number> chart, int year) {
        chart.getData().clear();

        // ✅ 1. Đảm bảo CategoryAxis có đủ 12 tháng theo thứ tự
        CategoryAxis xAxis = (CategoryAxis) chart.getXAxis();
        ObservableList<String> categories = FXCollections.observableArrayList(
                "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12"
        );
        xAxis.setCategories(categories);

        // ✅ 2. Lấy dữ liệu và chuẩn hóa
        Map<String, Double> rawData = thongKeCtrl.thongKeDoanhThuTheoThang(year);
        java.util.Map<Integer, Double> normalizedData = new java.util.HashMap<>();

        for (Map.Entry<String, Double> entry : rawData.entrySet()) {
            try {
                int month = Integer.parseInt(entry.getKey().trim());
                normalizedData.put(month, entry.getValue());
            } catch (NumberFormatException e) {
                System.err.println("Lỗi định dạng tháng từ SQL: " + entry.getKey());
            }
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu");

        // ✅ 3. LUÔN thêm đủ 12 tháng (kể cả tháng = 0)
        for (int i = 1; i <= 12; i++) {
            double rawRevenue = normalizedData.getOrDefault(i, 0.0);
            double revenue = rawRevenue / 1000000.0;

            String monthLabel = "T" + i;
            XYChart.Data<String, Number> data = new XYChart.Data<>(monthLabel, revenue);

            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    newNode.setStyle("-fx-bar-fill: #fa709a;");

                    // Tooltip
                    double value = data.getYValue().doubleValue();
                    Tooltip tooltip = new Tooltip(String.format("%s: %.2f triệu VND", data.getXValue(), value));
                    tooltip.setStyle("-fx-text-fill: black; -fx-font-size: 12px; -fx-background-color: #e2e7ed;");
                    Tooltip.install(newNode, tooltip);

                    setupBarAnimation(newNode);
                }
            });

            series.getData().add(data);
        }

        chart.getData().add(series);
        chart.setTitle("Doanh thu Theo Tháng - Năm " + year);

        // ✅ 4. Điều chỉnh khoảng cách để cột thẳng hàng
        chart.setBarGap(2);
        chart.setCategoryGap(10);
    }

    // Tách animation ra cho gọn code
    private void setupBarAnimation(Node node) {
        javafx.animation.ScaleTransition scaleUp = new javafx.animation.ScaleTransition(javafx.util.Duration.millis(200), node);
        scaleUp.setToX(1.1);
        scaleUp.setToY(1.1);

        javafx.animation.ScaleTransition scaleDown = new javafx.animation.ScaleTransition(javafx.util.Duration.millis(200), node);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        node.setOnMouseEntered(e -> {
            scaleUp.playFromStart();
            node.setCursor(javafx.scene.Cursor.HAND);
        });
        node.setOnMouseExited(e -> {
            scaleDown.playFromStart();
            node.setCursor(javafx.scene.Cursor.DEFAULT);
        });
    }

    private VBox createRevenueBySessionChart() {
        // 1. Lấy dữ liệu
        Map<String, Double> dataMap = thongKeCtrl.getDoanhThuTheoCa();

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        double totalRevenue = 0;
        for (Double val : dataMap.values()) totalRevenue += val;

        // --- THAY ĐỔI 1: Tính phần trăm và nối vào tên ---
        for (Map.Entry<String, Double> entry : dataMap.entrySet()) {
            if (entry.getValue() > 0) {
                double val = entry.getValue();
                double percent = (val / totalRevenue) * 100;

                // Tạo tên mới: Ví dụ "Ca Sáng" -> "Ca Sáng 40%"
                String nameWithPercent = String.format("%s %.0f%%", entry.getKey(), percent);

                pieData.add(new PieChart.Data(nameWithPercent, val));
            }
        }

        if (totalRevenue == 0) {
            pieData.add(new PieChart.Data("Chưa có doanh thu", 1));
        }

        // 2. Tạo biểu đồ
        PieChart chart = new PieChart(pieData);
        chart.setTitle("Tỷ lệ Doanh thu Theo Ca");
        chart.setLabelsVisible(true);
        chart.setLegendSide(Side.BOTTOM);
        chart.setPrefHeight(300);

        final double finalTotal = totalRevenue;

        // ✅ ĐỢI SCENE ĐƯỢC RENDER XONG RỒI MỚI GẮN TOOLTIP VÀ HIỆU ỨNG
        javafx.application.Platform.runLater(() -> {
            for (PieChart.Data data : chart.getData()) {
                Node node = data.getNode();

                if (node != null) {
                    // (Tuỳ chọn) Nếu bạn muốn set màu riêng cho từng ca, hãy dùng logic startsWith ở đây
                    // Ví dụ: if (data.getName().startsWith("Ca Sáng")) ...

                    // --- Xử lý Tooltip ---
                    if (finalTotal > 0) {
                        double amount = data.getPieValue();

                        // --- THAY ĐỔI 2: Nội dung Tooltip ---
                        // Vì tên (data.getName()) đã chứa %, ta không cần tính lại để hiển thị nữa
                        // Nội dung sẽ là: "Ca Sáng 40% \n 1.50 triệu VND"
                        String msg = String.format("%s\n%.2f triệu VND",
                                data.getName(), amount / 1000000);

                        Tooltip tooltip = new Tooltip(msg);
                        tooltip.setStyle("-fx-text-fill: black; " +
                                "-fx-font-size: 12px; " +
                                "-fx-background-color: #e2e7ed; " +
                                "-fx-padding: 8px; " +
                                "-fx-background-radius: 5; " +
                                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 0);");
                        Tooltip.install(node, tooltip);
                    } else {
                        Tooltip.install(node, new Tooltip("Chưa có dữ liệu"));
                    }

                    // --- Xử lý Hiệu ứng Hover (Giữ nguyên) ---

                    // Animation phóng to
                    javafx.animation.ScaleTransition scaleUp = new javafx.animation.ScaleTransition(
                            javafx.util.Duration.millis(200), node);
                    scaleUp.setToX(1.1);
                    scaleUp.setToY(1.1);

                    // Animation thu nhỏ
                    javafx.animation.ScaleTransition scaleDown = new javafx.animation.ScaleTransition(
                            javafx.util.Duration.millis(200), node);
                    scaleDown.setToX(1.0);
                    scaleDown.setToY(1.0);

                    node.setOnMouseEntered(e -> {
                        scaleUp.playFromStart();
                        node.setCursor(javafx.scene.Cursor.HAND);
                    });

                    node.setOnMouseExited(e -> {
                        scaleDown.playFromStart();
                        node.setCursor(javafx.scene.Cursor.DEFAULT);
                    });

                } else {
                    System.err.println("❌ Node null cho ca: " + data.getName());
                }
            }

            // 4. Style cho Title và Legend
            Node title = chart.lookup(".chart-title");
            if (title != null) {
                title.setStyle("-fx-text-fill: #2c3e50; -fx-font-weight: bold; -fx-font-size: 16px;");
            }

            for (Node item : chart.lookupAll(".chart-legend-item")) {
                if (item instanceof Label) {
                    Label label = (Label) item;
                    label.setStyle("-fx-text-fill: #000000; -fx-font-size: 12px; -fx-font-weight: bold;");
                    label.setWrapText(true);
                }
            }

            // Ẩn chú thích nếu không có dữ liệu thật
            chart.setLegendVisible(finalTotal > 0);
        });

        VBox container = new VBox(chart);
        container.setStyle("-fx-background-color: white; " +
                "-fx-padding: 15; " +
                "-fx-background-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0);");

        return container;
    }

    private VBox createRevenueByWeekdayChart() {
        PieChart chart = new PieChart(FXCollections.observableArrayList(
                new PieChart.Data("Thứ 2", 12),
                new PieChart.Data("Thứ 3", 15),
                new PieChart.Data("Thứ 4", 14),
                new PieChart.Data("Thứ 5", 18),
                new PieChart.Data("Thứ 6", 25),
                new PieChart.Data("Thứ 7", 30),
                new PieChart.Data("Chủ nhật", 26)
        ));
        chart.setTitle("Doanh thu Theo Ngày trong Tuần");
        chart.setLabelsVisible(true);
        chart.setLegendSide(Side.BOTTOM);
        chart.setPrefHeight(300);

        // CSS cho PieChart - tất cả chữ màu đen
        chart.setStyle("-fx-text-fill: black; -fx-font-family: 'Segoe UI';");
        chart.lookup(".chart-title").setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
        chart.lookup(".chart-legend").setStyle("-fx-text-fill: black;");

        return new VBox(chart);
    }


    // ===== CÁC BIỂU ĐỒ KHÁC GIỮ NGUYÊN =====

    private VBox createBookingSuccessRateChart() {
        PieChart chart = new PieChart(FXCollections.observableArrayList(
                new PieChart.Data("Thành công", 95.5),
                new PieChart.Data("Hủy/No-show", 4.5)
        ));
        chart.setTitle("Tỷ lệ Thành công");
        chart.setLabelsVisible(true);
        chart.setLegendSide(Side.BOTTOM);
        chart.setPrefHeight(320);
        chart.setStyle("-fx-background-color: transparent;");
        return new VBox(chart);
    }

    private VBox createTableTurnoverChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis("Sử dụng (%)", 0, 100, 10);
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Hiệu suất Bàn");
        chart.setLegendVisible(false);
        chart.setStyle("-fx-background-color: transparent;");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Khu A", 85));
        series.getData().add(new XYChart.Data<>("Khu B", 65));
        series.getData().add(new XYChart.Data<>("VIP", 75));
        series.getData().add(new XYChart.Data<>("Sân vườn", 50));

        chart.getData().add(series);
        chart.setPrefHeight(320);
        return new VBox(chart);
    }

    private VBox createTopSellingMenuChart() {
        // 1. Cấu hình trục
        CategoryAxis xAxis = new CategoryAxis(); // Trục tên món ăn
        NumberAxis yAxis = new NumberAxis();     // Trục số lượng
        yAxis.setLabel("Số lượng đã bán");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Top 5 Món Ăn Bán Chạy Nhất");
        chart.setLegendVisible(false);

        // Áp dụng style chữ đen (hàm bạn đã viết sẵn)
        applyBlackTextStyle(chart, xAxis, yAxis);

        // 2. Lấy dữ liệu thực từ Database
        // Giả sử thongKeCtrl đã có hàm gọi xuống DAO: return thongKeDao.getTopMonAnBanChay();
        Map<String, Integer> topMenuData = thongKeCtrl.getTopMonAnBanChay();

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // 3. Đổ dữ liệu vào Series
        for (Map.Entry<String, Integer> entry : topMenuData.entrySet()) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey(), entry.getValue());
            series.getData().add(data);
        }

        // Nếu chưa có dữ liệu thì thêm dòng fake để biểu đồ không bị lỗi hiển thị
        if (topMenuData.isEmpty()) {
            series.getData().add(new XYChart.Data<>("(Chưa có dữ liệu)", 0));
        }

        // 4. Xử lý màu sắc và Tooltip (Giống biểu đồ doanh thu tháng)
        int colorIndex = 0;
        for (XYChart.Data<String, Number> data : series.getData()) {
            data.nodeProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    // Set màu xanh lá cho món ăn (#43e97b)
                    newVal.setStyle("-fx-bar-fill: #43e97b;");

                    // Tooltip chuẩn style
                    Tooltip tooltip = new Tooltip(String.format("%s\nĐã bán: %s phần",
                            data.getXValue(), data.getYValue()));
                    tooltip.setStyle("-fx-text-fill: black; -fx-font-size: 11px; -fx-background-color: #e2e7ed;");
                    Tooltip.install(newVal, tooltip);
                    javafx.animation.ScaleTransition scaleUp = new javafx.animation.ScaleTransition(

                            javafx.util.Duration.millis(200), data.getNode());

                    scaleUp.setToX(1.1);

                    scaleUp.setToY(1.1);


// Animation thu nhỏ

                    javafx.animation.ScaleTransition scaleDown = new javafx.animation.ScaleTransition(

                            javafx.util.Duration.millis(200), data.getNode());

                    scaleDown.setToX(1.0);

                    scaleDown.setToY(1.0);


                    data.getNode().setOnMouseEntered(e -> {

                        scaleUp.playFromStart();

                        data.getNode().setCursor(javafx.scene.Cursor.HAND);

                    });


                    data.getNode().setOnMouseExited(e -> {

                        scaleDown.playFromStart();

                        data.getNode().setCursor(javafx.scene.Cursor.DEFAULT);

                    });

//                    // Hiệu ứng hover đậm màu
//                    newVal.setOnMouseEntered(e -> newVal.setStyle("-fx-bar-fill: #2ecc71;"));
//                    newVal.setOnMouseExited(e -> newVal.setStyle("-fx-bar-fill: #43e97b;"));
                }
            });
            colorIndex++;
        }

        chart.getData().add(series);
        chart.setPrefHeight(320);

        // Tinh chỉnh để biểu đồ cột trông đẹp hơn khi ít dữ liệu
        chart.setBarGap(5);
        chart.setCategoryGap(20);

        VBox container = new VBox(chart);
        container.setStyle("-fx-background-color: transparent;");
        return container;
    }

    private VBox createRevenueByMenuGroupChart() {
        // 1. Lấy dữ liệu
        Map<String, Double> dataMap = thongKeCtrl.getDoanhThuTheoNhomMon();

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        double totalRevenue = 0;
        for (Double val : dataMap.values()) totalRevenue += val;

        // --- THAY ĐỔI 1: Tính phần trăm và nối vào tên ---
        for (Map.Entry<String, Double> entry : dataMap.entrySet()) {
            if (entry.getValue() > 0) {
                double val = entry.getValue();
                // Tính phần trăm
                double percent = (val / totalRevenue) * 100;

                // Tạo tên mới: Ví dụ "Món chính" -> "Món chính 25%"
                // %.0f là làm tròn số nguyên (25%), %.1f là lấy 1 số thập phân (25.5%)
                String nameWithPercent = String.format("%s %.0f%%", entry.getKey(), percent);

                pieData.add(new PieChart.Data(nameWithPercent, val));
            }
        }

        if (totalRevenue == 0) {
            pieData.add(new PieChart.Data("Chưa có dữ liệu", 1));
        }

        // 2. Tạo biểu đồ
        PieChart chart = new PieChart(pieData);
        chart.setTitle("Doanh thu Theo Nhóm Món");
        chart.setLabelsVisible(true);
        chart.setLegendSide(Side.RIGHT);
        chart.setPrefHeight(320);

        final double finalTotal = totalRevenue;

        // ✅ ĐỢI SCENE ĐƯỢC RENDER XONG
        javafx.application.Platform.runLater(() -> {
            for (PieChart.Data data : chart.getData()) {
                Node node = data.getNode();

                if (node != null) {
                    String name = data.getName(); // Tên bây giờ là "Món chính 25%"
                    String color = "#bdc3c7";

                    // --- THAY ĐỔI 2: Sửa logic chọn màu ---
                    // Vì tên bây giờ chứa cả số %, nên dùng switch case cũ sẽ không khớp.
                    // Chuyển sang dùng if-else với startsWith hoặc contains

                    if (name.startsWith("Món chính")) color = "#ff6b6b";
                    else if (name.startsWith("Đồ uống")) color = "#4ecdc4";
                    else if (name.startsWith("Món khai vị")) color = "#ffe66d";
                    else if (name.startsWith("Tráng miệng")) color = "#ff9ff3";
                    else if (name.startsWith("Món ăn kèm")) color = "#1a535c";
                    else if (name.startsWith("Nước sốt")) color = "#6a0572";

                    node.setStyle("-fx-pie-color: " + color + ";");

                    // --- B. TẠO TOOLTIP ---
                    Tooltip tooltip = new Tooltip();
                    tooltip.setStyle("-fx-text-fill: black; " +
                            "-fx-font-size: 12px; " +
                            "-fx-background-color: #e2e7ed; " +
                            "-fx-padding: 8px; " +
                            "-fx-background-radius: 5; " +
                            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 0);");

                    if (finalTotal > 0) {
                        double amount = data.getPieValue();
                        // Tooltip hiển thị số tiền cụ thể
                        String msg = String.format("%s\n%.2f triệu VND",
                                name, amount / 1000000);
                        tooltip.setText(msg);
                    } else {
                        tooltip.setText("Chưa có dữ liệu");
                    }

                    Tooltip.install(node, tooltip);

                    // --- C. HIỆU ỨNG ---
                    javafx.animation.ScaleTransition scaleUp = new javafx.animation.ScaleTransition(
                            javafx.util.Duration.millis(200), node);
                    scaleUp.setToX(1.1);
                    scaleUp.setToY(1.1);

                    javafx.animation.ScaleTransition scaleDown = new javafx.animation.ScaleTransition(
                            javafx.util.Duration.millis(200), node);
                    scaleDown.setToX(1.0);
                    scaleDown.setToY(1.0);

                    node.setOnMouseEntered(e -> {
                        scaleUp.playFromStart();
                        node.setCursor(javafx.scene.Cursor.HAND);
                    });

                    node.setOnMouseExited(e -> {
                        scaleDown.playFromStart();
                        node.setCursor(javafx.scene.Cursor.DEFAULT);
                    });
                }
            }
        });

        // 4. Style Legend
        javafx.application.Platform.runLater(() -> {
            Node title = chart.lookup(".chart-title");
            if (title != null) {
                title.setStyle("-fx-text-fill: #2c3e50; -fx-font-weight: bold; -fx-font-size: 14px;");
            }
            // Chỉnh màu chữ Legend (Chú giải)
            for (Node item : chart.lookupAll(".chart-legend-item")) {
                if (item instanceof Label) {
                    Label label = (Label) item;
                    // Tăng chiều rộng min để chữ không bị cắt nếu dài quá
                    label.setMinWidth(150);
                    label.setStyle("-fx-text-fill: black; -fx-font-size: 11px; -fx-font-weight: bold;");
                }
            }
            chart.setLegendVisible(finalTotal > 0);
        });

        VBox container = new VBox(chart);
        container.setStyle("-fx-background-color: transparent;");
        return container;
    }

    private VBox createTopCustomersList() {
        VBox container = new VBox(15); // Tăng khoảng cách giữa các dòng
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 0);");

        // 1. Lấy dữ liệu từ DAO
        java.util.List<String[]> customers = thongKeCtrl.getTopKhachHang();

        if (customers.isEmpty()) {
            Label emptyLbl = new Label("Chưa có dữ liệu khách hàng");
            emptyLbl.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 14px;");
            container.getChildren().add(emptyLbl);
            return new VBox(container);
        }

        // 2. Tìm giá trị lớn nhất (Người Top 1) để tính % cho Progress Bar
        double maxVal = 0;
        try {
            // Parse số tiền từ chuỗi (VD: "15.2M" -> 15.2)
            String maxStr = customers.get(0)[1].replace("M", "").replace("K", "").replace(",", ".");
            maxVal = Double.parseDouble(maxStr);
            // Nếu đơn vị là M thì nhân hệ số, nhưng để tính tỷ lệ % thì không cần nhân cũng được,
            // miễn là cùng đơn vị. Nhưng để chắc chắn, ta cứ giả định Top 1 luôn là mốc 1.0 (100%)
        } catch (Exception e) {
            maxVal = 1;
        }

        // 3. Tạo giao diện từng dòng
        int rank = 1;
        for (String[] customer : customers) {
            String tenKhach = customer[0];
            String tongTienStr = customer[1]; // VD: 15.2M
            String soLan = customer[2];       // VD: 8 lần

            // --- Layout dòng ---
            HBox row = new HBox(15);
            row.setAlignment(Pos.CENTER_LEFT);

            // A. Huy hiệu / Rank (Số thứ tự)
            Label lblRank = new Label(String.valueOf(rank));
            lblRank.setPrefSize(30, 30);
            lblRank.setAlignment(Pos.CENTER);
            lblRank.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

            // Màu sắc huy hiệu theo thứ hạng
            String rankColor = switch (rank) {
                case 1 -> "#FFD700"; // Vàng
                case 2 -> "#C0C0C0"; // Bạc
                case 3 -> "#CD7F32"; // Đồng
                default -> "#ecf0f1"; // Xám nhạt
            };
            String rankTextColor = (rank <= 3) ? "white" : "#7f8c8d";
            lblRank.setStyle("-fx-background-color: " + rankColor + "; -fx-text-fill: " + rankTextColor + "; -fx-background-radius: 50%;");

            // B. Avatar + Tên + Số lần (Group trái)
            VBox infoBox = new VBox(3);
            Label lblName = new Label(tenKhach);
            lblName.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
            lblName.setStyle("-fx-text-fill: #2c3e50;"); // Màu chữ đậm

            Label lblFreq = new Label(soLan + " ghé thăm");
            lblFreq.setFont(Font.font("Segoe UI", 11));
            lblFreq.setStyle("-fx-text-fill: #95a5a6;");

            infoBox.getChildren().addAll(lblName, lblFreq);
            infoBox.setPrefWidth(180); // Cố định chiều rộng tên

            // C. Progress Bar (Thanh hiển thị mức chi tiêu)
            // Tính toán phần trăm
            double currentVal = 0;
            try {
                String valStr = tongTienStr.replace("M", "").replace("K", "").replace(",", ".");
                currentVal = Double.parseDouble(valStr);
                if (customers.get(0)[1].contains("M") && tongTienStr.contains("K")) {
                    currentVal = currentVal / 1000;
                }
            } catch (Exception e) {
                currentVal = 0;
            }

            double progress = (maxVal > 0) ? (currentVal / maxVal) : 0;

            javafx.scene.control.ProgressBar progressBar = new javafx.scene.control.ProgressBar(progress);
            progressBar.setPrefWidth(200);
            progressBar.setPrefHeight(10); // 1. Tăng độ dày lên 10px hoặc 12px

            // Chọn màu sắc
            String barColor = switch (rank) {
                case 1 -> "#f1c40f"; // Vàng
                case 2 -> "#bdc3c7"; // Bạc
                case 3 -> "#e67e22"; // Đồng
                default -> "#3498db"; // Xanh
            };

            // 2. CSS cơ bản để xóa viền mặc định
            progressBar.setStyle(
                    "-fx-accent: " + barColor + ";" +          // Màu thanh trượt
                            "-fx-control-inner-background: #ecf0f1;" + // Màu nền xám nhạt
                            "-fx-background-radius: 10;" +             // Bo tròn tổng thể
                            "-fx-padding: 0;"                          // Xóa khoảng cách thừa
            );

            // 3. [QUAN TRỌNG] Can thiệp sâu vào .bar và .track để làm phẳng hoàn toàn
            // Phải dùng runLater để đợi thanh bar được vẽ ra xong mới tìm được CSS class của nó
            javafx.application.Platform.runLater(() -> {
                Node bar = progressBar.lookup(".bar");
                if (bar != null) {
                    bar.setStyle(
                            "-fx-background-color: " + barColor + ";" +
                                    "-fx-background-radius: 10;" + // Bo tròn thanh màu
                                    "-fx-background-insets: 0;"    // Xóa viền trắng bao quanh
                    );
                }

                Node track = progressBar.lookup(".track");
                if (track != null) {
                    track.setStyle(
                            "-fx-background-color: #ecf0f1;" + // Màu nền track phẳng
                                    "-fx-background-radius: 10;" +
                                    "-fx-background-insets: 0;"
                    );
                }
            });

            // D. Số tiền (Căn phải)
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Label lblMoney = new Label(tongTienStr);
            lblMoney.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
            lblMoney.setStyle("-fx-text-fill: " + barColor + ";"); // Màu tiền trùng màu rank
            lblMoney.setMinWidth(60);
            lblMoney.setAlignment(Pos.CENTER_RIGHT);

            // Ghép các phần lại
            // Cấu trúc: [Rank] [Tên + Lần] [ProgressBar] [Spacer] [Tiền]
            // Tuy nhiên để đẹp hơn trên màn hình rộng, ta gom ProgressBar vào giữa
            VBox middleBox = new VBox(5);
            middleBox.getChildren().addAll(progressBar);
            middleBox.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(middleBox, Priority.ALWAYS); // Thanh bar tự giãn

            row.getChildren().addAll(lblRank, infoBox, middleBox, lblMoney);

            // Thêm hiệu ứng hover cho dòng
            row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10; -fx-cursor: hand;"));
            row.setOnMouseExited(e -> row.setStyle("-fx-background-color: transparent;"));

            container.getChildren().add(row);

            // Thêm đường kẻ mờ phân cách (trừ dòng cuối)
            if (rank < customers.size()) {
                Region line = new Region();
                line.setStyle("-fx-background-color: #f1f2f6; -fx-min-height: 1; -fx-max-height: 1;");
                container.getChildren().add(line);
            }

            rank++;
        }

        // Wrapper để set chiều cao cố định hoặc co giãn
        VBox wrapper = new VBox(container);
        wrapper.setStyle("-fx-background-color: transparent;");
        return wrapper;
    }
    // ===== HÀM XỬ LÝ LOGIC SỰ KIỆN =====

    /**
     * Hàm trung tâm: Được gọi mỗi khi thay đổi bất kỳ bộ lọc nào.
     * Nó sẽ lấy giá trị từ các ComboBox/DatePicker và gọi Controller để lấy dữ liệu mới.
     */
    private void loadDashboardData() {
        String nhomTheo = cbGroup.getValue();
        String khuVuc = cbArea.getValue();
        java.time.LocalDate tuNgay = datePickerFrom.getValue();
        java.time.LocalDate denNgay = datePickerTo.getValue();

        // TODO: GỌI CONTROLLER Ở ĐÂY
        // Ví dụ:
        // Map<String, Double> newData = thongKeCtrl.getDoanhThuTheoTieuChi(nhomTheo, khuVuc, tuNgay, denNgay);
        // updateChart(newData);

        // Ví dụ làm mới biểu đồ tròn (Giả lập)
        // createRevenueBySessionChart(); // Cần sửa hàm tạo biểu đồ thành hàm update dữ liệu
    }

    /**
     * Xử lý xuất báo cáo ra Excel
     */
    private void handleExportReport() {
        // Tạo FileChooser để chọn nơi lưu
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Lưu Báo Cáo Doanh Thu");
        fileChooser.getExtensionFilters().add(
                new javafx.stage.FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
        );
        fileChooser.setInitialFileName("BaoCao_DoanhThu_" + java.time.LocalDate.now() + ".xlsx");

        java.io.File file = fileChooser.showSaveDialog(this.getScene().getWindow());

        if (file != null) {
            // TODO: Gọi hàm xuất Excel từ Controller/DAO
            // boolean success = thongKeCtrl.exportToExcel(file, datePickerFrom.getValue(), datePickerTo.getValue());

            showAlert("Thành công", "Đã xuất báo cáo thành công!");
        }
    }

    /**
     * Hàm hiển thị thông báo đơn giản
     */
    private void showAlert(String title, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}