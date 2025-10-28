
package gui;

import ctrl.ThongKe_Ctrl;
import dao.ThongKe_DAO;
import javafx.collections.FXCollections;
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
    ThongKe_Ctrl thongKeCtrl =  new ThongKe_Ctrl();
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

        Label lblGroup = new Label("📅 Nhóm theo:");
        lblGroup.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 12));

        ComboBox<String> cbGroup = new ComboBox<>(FXCollections.observableArrayList(
                "Theo Ngày", "Theo Tuần", "Theo Tháng", "Theo Năm"
        ));
        cbGroup.getSelectionModel().selectFirst();
        cbGroup.setStyle("-fx-background-radius: 3;");

        Label lblArea = new Label("📍 Khu vực:");
        lblArea.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 12));

        ComboBox<String> cbArea = new ComboBox<>(FXCollections.observableArrayList(
                "Tất cả", "Khu A", "Khu B", "Phòng VIP", "Sân vườn"
        ));
        cbArea.getSelectionModel().selectFirst();
        cbArea.setStyle("-fx-background-radius: 3;");

        DatePicker datePickerFrom = new DatePicker();
        datePickerFrom.setPromptText("Từ ngày...");
        datePickerFrom.setStyle("-fx-background-radius: 3;");

        DatePicker datePickerTo = new DatePicker();
        datePickerTo.setPromptText("Đến ngày...");
        datePickerTo.setStyle("-fx-background-radius: 3;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnExport = new Button("⬇️ Xuất Báo Cáo");
        btnExport.setStyle(
                "-fx-background-color: #082744;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10 20;" +
                        "-fx-cursor: hand;"
        );

        btnExport.setOnMouseEntered(e -> btnExport.setStyle(
                "-fx-background-color: #082744;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-scale-x: 1.05;" +
                        "-fx-scale-y: 1.05;"
        ));

        btnExport.setOnMouseExited(e -> btnExport.setStyle(
                "-fx-background-color: #082744;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10 20;" +
                        "-fx-cursor: hand;"
        ));

        Button btnCompare = new Button("📊 So sánh Kỳ trước");
        btnCompare.setStyle(
                "-fx-background-color: #f1f3f5;" +
                        "-fx-text-fill: #495057;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10 20;" +
                        "-fx-cursor: hand;"
        );

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
        statGrid.add(createModernStatCard("Tổng Doanh thu",  String.format("%.2f", thongKeCtrl.getTongDoanhThu()), "VND", "↑ 12%", "So với tháng trước", "#667eea", "💰"), 0, 0);
        statGrid.add(createModernStatCard("DT TB/Bàn", String.format("%.2f",thongKeCtrl.getDoanhThuTBBan()), "VND", "↑ 8%", "Cao điểm: 650K", "#764ba2", "💸"), 1, 0);
        statGrid.add(createModernStatCard("TT Tiền mặt", String.format("%.2f",thongKeCtrl.getTiLeTienMat()), "%", "↓ 5%", "Thẻ/Ví: " + (100 - thongKeCtrl.getTiLeTienMat()) + "%", "#f093fb", "💵"), 2, 0);
        statGrid.add(createModernStatCard("DT Ca Tối", String.format("%.2f",thongKeCtrl.getDoanhThuCaToi()), "VND", "↑ 15%", "Chiếm 50% tổng", "#4facfe", "🌙"), 3, 0);

        GridPane chartGrid = new GridPane();
        chartGrid.setHgap(25);
        chartGrid.setVgap(25);

        // BIỂU ĐỒ DOANH THU THEO THÁNG (NĂM HIỆN TẠI)
        VBox monthlyRevenueChart = createStyledChart(createMonthlyRevenueChart(), "Doanh thu Theo Tháng (" + java.time.Year.now().getValue() + ")");

        // BIỂU ĐỒ SO SÁNH DOANH THU THEO BUỔI
        VBox revenueBySessionChart = createStyledChart(createRevenueBySessionChart(), "Doanh thu Theo Buổi trong Ngày");

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

        VBox successChart = createStyledChart(createBookingSuccessRateChart(), "Tỷ lệ thành công");
        VBox turnoverChart = createStyledChart(createTableTurnoverChart(), "Hiệu suất theo khu vực");

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

        statGrid.add(createModernStatCard("Bán chạy nhất", "Bánh mỳ", "Pate", "180", "Đã bán", "#43e97b", "🥖"), 0, 0);
        statGrid.add(createModernStatCard("Lợi nhuận cao", "Rượu Vang", "Đỏ", "75", "% margin", "#fa709a", "🍷"), 1, 0);
        statGrid.add(createModernStatCard("DT Đồ uống", "35M", "VND", "23%", "tổng DT", "#fee140", "🥤"), 2, 0);

        GridPane chartGrid = new GridPane();
        chartGrid.setHgap(25);
        chartGrid.setVgap(25);

        VBox topSellingChart = createStyledChart(createTopSellingMenuChart(), "Top 5 món");
        VBox menuGroupChart = createStyledChart(createRevenueByMenuGroupChart(), "Theo nhóm món");

        GridPane.setHgrow(topSellingChart, Priority.ALWAYS);
        GridPane.setHgrow(menuGroupChart, Priority.ALWAYS);

        chartGrid.add(topSellingChart, 0, 0);
        chartGrid.add(menuGroupChart, 1, 0);

        return new VBox(25, sectionTitle, statGrid, chartGrid);
    }

    // ===== 5. CUSTOMER SECTION =====
    private VBox createCustomerSection() {
        Label sectionTitle = createModernSectionTitle("👤 THỐNG KÊ KHÁCH HÀNG", "#30cfd0");

        GridPane statGrid = new GridPane();
        statGrid.setHgap(20);
        statGrid.setVgap(20);
        statGrid.setPadding(new Insets(15, 0, 25, 0));

        statGrid.add(createModernStatCard("Tổng Khách", "4,800", "khách", "450", "Thân thiết", "#30cfd0", "👥"), 0, 0);
        statGrid.add(createModernStatCard("Chi tiêu TB", "480K", "VND", "↑ 5%", "So kỳ trước", "#a8edea", "💳"), 1, 0);
        statGrid.add(createModernStatCard("Tần suất TB", "1.8", "lần/tháng", "2.5", "Khách VIP", "#fed6e3", "🔄"), 2, 0);

        GridPane chartGrid = new GridPane();
        chartGrid.setHgap(25);
        chartGrid.setVgap(25);

        VBox topCustomers = createStyledChart(createTopCustomersList(), "Top 5 khách hàng");

        GridPane.setHgrow(topCustomers, Priority.ALWAYS);
        chartGrid.add(topCustomers, 0, 0);

        return new VBox(25, sectionTitle, statGrid, chartGrid);
    }

    // ===== MODERN STAT CARD =====
    private VBox createModernStatCard(String title, String mainValue, String unit,
                                      String trend, String subText, String color, String icon) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(20));
        card.setPrefSize(290, 140);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 3);"
        );

        // Hover effect
        card.setOnMouseEntered(e -> card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 20, 0, 0, 5);" +
                        "-fx-scale-x: 1.02;" +
                        "-fx-scale-y: 1.02;"
        ));

        card.setOnMouseExited(e -> card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 3);"
        ));

        // Top row: Icon + Title
        HBox topRow = new HBox(10);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font("Segoe UI Emoji", 28));
        iconLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);");

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        titleLabel.setStyle("-fx-text-fill: #495057;");
        titleLabel.setWrapText(true);

        topRow.getChildren().addAll(iconLabel, titleLabel);

        // Main value with unit
        HBox valueRow = new HBox(5);
        valueRow.setAlignment(Pos.BASELINE_LEFT);

        Label value = new Label(mainValue);
        value.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        value.setStyle("-fx-text-fill: " + color + ";");

        Label unitLabel = new Label(unit);
        unitLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        unitLabel.setStyle("-fx-text-fill: #6c757d;");

        valueRow.getChildren().addAll(value, unitLabel);

        // Trend indicator
        Label trendLabel = new Label(trend);
        trendLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        String trendColor = trend.contains("↑") ? "#28a745" : trend.contains("↓") ? "#dc3545" : "#6c757d";
        trendLabel.setStyle(
                "-fx-text-fill: " + trendColor + ";" +
                        "-fx-background-color: " + trendColor + "22;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 3 8;"
        );

        // Subtitle
        Label subLabel = new Label(subText);
        subLabel.setFont(Font.font("Segoe UI", 11));
        subLabel.setStyle("-fx-text-fill: #6c757d;");

        card.getChildren().addAll(topRow, valueRow, trendLabel, subLabel);
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
    private VBox createStyledChart(VBox chartVBox, String subtitle) {
        VBox wrapper = new VBox(10);
        wrapper.setPadding(new Insets(20));
        wrapper.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 12, 0, 0, 2);"
        );

        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 12));
        subtitleLabel.setStyle("-fx-text-fill: #6c757d;");

        wrapper.getChildren().addAll(subtitleLabel, chartVBox);
        return wrapper;
    }

    // ===== CÁC BIỂU ĐỒ DOANH THU MỚI =====

//    private VBox createMonthlyRevenueChart() {
//        CategoryAxis xAxis = new CategoryAxis();
//        NumberAxis yAxis = new NumberAxis("Triệu VND", 0, 300, 50);
//
//        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
//        chart.setTitle("Doanh thu Theo Tháng - Năm " + java.time.Year.now().getValue());
//        chart.setLegendVisible(false);
//        chart.setAnimated(true);
//
//        // CSS để tất cả chữ thành màu đen
//        applyBlackTextStyle(chart, xAxis, yAxis);
//
//        XYChart.Series<String, Number> series = new XYChart.Series<>();
//
//        // Dữ liệu doanh thu theo tháng trong năm hiện tại - có thể thay bằng dữ liệu thực từ controller
//        String[] months = {"T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12"};
//        double[] monthlyRevenue = {125.5, 142.3, 118.7, 156.2, 189.5, 210.8, 195.3, 178.6, 165.4, 148.9, 132.1, 158.7};
//
//        // Chỉ hiển thị các tháng đã qua (giả sử hiện tại là tháng 6)
//        int currentMonth = java.time.LocalDate.now().getMonthValue();
//        for (int i = 0; i < currentMonth; i++) {
//            series.getData().add(new XYChart.Data<>(months[i], monthlyRevenue[i]));
//        }
//
//        // Thêm tooltip cho từng điểm dữ liệu
//        for (XYChart.Data<String, Number> data : series.getData()) {
//            Tooltip tooltip = new Tooltip(String.format("Tháng %s: %s triệu VND",
//                    data.getXValue(), data.getYValue()));
//            Tooltip.install(data.getNode(), tooltip);
//            tooltip.setStyle("-fx-text-fill: black; -fx-font-size: 11px;");
//        }
//
//        chart.getData().add(series);
//        chart.setPrefHeight(300);
//
//        VBox container = new VBox(chart);
//        container.setPadding(new Insets(10));
//        container.setAlignment(Pos.CENTER);
//        container.setStyle("-fx-background-color: white;");
//        return container;
//    }
private VBox createMonthlyRevenueChart() {
    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis("Triệu VND", 0, 250, 50);

    BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
    chart.setTitle("Doanh thu Theo Tháng - Năm " + java.time.Year.now().getValue());
    chart.setLegendVisible(false);
    chart.setBarGap(3); // Khoảng cách giữa các cột

    applyBlackTextStyle(chart, xAxis, yAxis);

    XYChart.Series<String, Number> series = new XYChart.Series<>();
    Map<String,Double> monthlyData = thongKeCtrl.thongKeDoanhThuTheoThang();
    String[] months = {"T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12"};
    String[] a = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
//    for (Map.Entry<String, Double> entry : monthlyData.entrySet()) {
//        System.out.println("Tháng: " + entry.getKey() + " - Doanh thu: " + entry.getValue());
//    }

    int currentMonth = java.time.LocalDate.now().getMonthValue();

    // 3. ĐIỀN DỮ LIỆU VÀO SERIES THEO THỨ TỰ THÁNG
    for (int i = 0; i < currentMonth && i < months.length; i++) {
        String monthKey = a[i];
        // Lấy giá trị doanh thu từ Map, nếu không có (null), mặc định là 0.0
        // Chia cho 1_000_000 để chuyển từ VND sang Triệu VND (như trục Y)
        double revenueInMillions = monthlyData.getOrDefault(monthKey, 0.0) /10000;
        XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(monthKey, revenueInMillions);
        series.getData().add(dataPoint);
    }

    // Thêm màu cho các cột

    int colorIndex = 0;
    for (XYChart.Data<String, Number> data : series.getData()) {
        final int index = colorIndex;
        data.nodeProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                newValue.setStyle("-fx-bar-fill: " + "#fa709a" + ";");

                Tooltip tooltip = new Tooltip(String.format("Tháng %s: %s triệu VND",
                        data.getXValue(), data.getYValue()));
                Tooltip.install(newValue, tooltip);
                tooltip.setStyle("-fx-text-fill: black; -fx-font-size: 11px; -fx-background-color: #e2e7ed");
            }
        });
        colorIndex++;
    }

    chart.getData().add(series);
    chart.setPrefHeight(300);

    VBox container = new VBox(chart);
    container.setPadding(new Insets(10));
    container.setAlignment(Pos.CENTER);
    container.setStyle("-fx-background-color: white;");
    return container;
}

    private VBox createRevenueBySessionChart() {
        // Tạo biểu đồ tròn trực tiếp
        PieChart chart = new PieChart(FXCollections.observableArrayList(
                new PieChart.Data("Ca Sáng\n(6h-11h)", 18),
                new PieChart.Data("Ca Trưa\n(11h-14h)", 35),
                new PieChart.Data("Ca Chiều\n(14h-17h)", 15),
                new PieChart.Data("Ca Tối\n(17h-22h)", 28),
                new PieChart.Data("Ca Đêm\n(22h-2h)", 4)
        ));

        chart.setTitle("Tỷ lệ Doanh thu Theo Ca");
        chart.setLabelsVisible(true);
        chart.setLegendSide(Side.BOTTOM);
        chart.setPrefHeight(300);
        chart.setStyle("-fx-text-fill: black; -fx-font-family: 'Segoe UI';");

        // Thêm tooltip đơn giản
        for (PieChart.Data data : chart.getData()) {
            final String name = data.getName();
            final double percentage = data.getPieValue();
            final double amount = (percentage / 100) * thongKeCtrl.getTongDoanhThu();

            data.nodeProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    Tooltip tooltip = new Tooltip(
                            String.format("%s: %.1f%%\n%.1f triệu VND", name, percentage, amount / 1000000)
                    );
                    Tooltip.install(newVal, tooltip);
                    tooltip.setStyle("-fx-text-fill: black; -fx-font-size: 11px;");
                }
            });
        }

        // Style sau render
        javafx.application.Platform.runLater(() -> {
            if (chart.lookup(".chart-title") != null) {
                chart.lookup(".chart-title").setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 14px;");
            }
            if (chart.lookup(".chart-legend") != null) {
                chart.lookup(".chart-legend").setStyle("-fx-text-fill: black;");
            }
        });

        VBox container = new VBox(chart);
        container.setStyle("-fx-background-color: white;");
        return container;
    }


    private VBox createRevenueByWeekdayChart() {
//        PieChart chart = new PieChart(FXCollections.observableArrayList(
//                new PieChart.Data("Thứ 2", 12),
//                new PieChart.Data("Thứ 3", 15),
//                new PieChart.Data("Thứ 4", 14),
//                new PieChart.Data("Thứ 5", 18),
//                new PieChart.Data("Thứ 6", 25),
//                new PieChart.Data("Thứ 7", 30),
//                new PieChart.Data("Chủ nhật", 26)
//        ));
//        chart.setTitle("Doanh thu Theo Ngày trong Tuần");
//        chart.setLabelsVisible(true);
//        chart.setLegendSide(Side.BOTTOM);
//        chart.setPrefHeight(300);
//        chart.setStyle("-fx-background-color: transparent;");
//
//        return new VBox(chart);
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
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis("Số lượng", 0, 200, 20);
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Top Món bán chạy");
        chart.setLegendVisible(false);
        chart.setStyle("-fx-background-color: transparent;");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Bánh mỳ", 180));
        series.getData().add(new XYChart.Data<>("Trà tắc", 150));
        series.getData().add(new XYChart.Data<>("Súp bí đỏ", 120));
        series.getData().add(new XYChart.Data<>("Cà phê", 110));
        series.getData().add(new XYChart.Data<>("Nước cam", 90));

        chart.getData().add(series);
        chart.setPrefHeight(320);
        return new VBox(chart);
    }

    private VBox createRevenueByMenuGroupChart() {
        PieChart chart = new PieChart(FXCollections.observableArrayList(
                new PieChart.Data("Món chính", 45),
                new PieChart.Data("Đồ uống", 30),
                new PieChart.Data("Khai vị", 15),
                new PieChart.Data("Tráng miệng", 10)
        ));
        chart.setTitle("Theo Nhóm món");
        chart.setLabelsVisible(true);
        chart.setLegendSide(Side.RIGHT);
        chart.setPrefHeight(320);
        chart.setStyle("-fx-background-color: transparent;");
        return new VBox(chart);
    }

    private VBox createTopCustomersList() {
        VBox container = new VBox(12);
        container.setPadding(new Insets(15));
        container.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10;");

        String[][] customers = {
                {"🥇", "Nguyễn Văn A (VIP)", "15.2M", "8 lần"},
                {"🥈", "Trần Thị B (VIP)", "12.8M", "6 lần"},
                {"🥉", "Lê Văn C", "9.5M", "12 lần"},
                {"4️⃣", "Phạm Thị D", "7.1M", "3 lần"},
                {"5️⃣", "Hoàng Văn E", "6.8M", "5 lần"}
        };

        for (String[] customer : customers) {
            HBox row = new HBox(15);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(10));
            row.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-background-radius: 8;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 1);"
            );

            Label rank = new Label(customer[0]);
            rank.setFont(Font.font("Segoe UI Emoji", 18));

            Label name = new Label(customer[1]);
            name.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 12));
            name.setPrefWidth(150);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Label amount = new Label(customer[2]);
            amount.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
            amount.setStyle("-fx-text-fill: #667eea;");

            Label freq = new Label(customer[3]);
            freq.setFont(Font.font("Segoe UI", 11));
            freq.setStyle("-fx-text-fill: #6c757d;");

            row.getChildren().addAll(rank, name, spacer, amount, freq);
            container.getChildren().add(row);
        }

        VBox wrapper = new VBox(container);
        wrapper.setPrefHeight(320);
        return wrapper;
    }
}