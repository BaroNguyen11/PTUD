package gui;

import dao.Dashboard_DAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.animation.ScaleTransition;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.scene.Cursor;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class Gui_Dashboard extends BorderPane {

    private Dashboard_DAO dashboardDAO;
    private NumberFormat currencyFormat;

    public Gui_Dashboard() {
        dashboardDAO = new Dashboard_DAO();
        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        // === 1. SETUP ROOT CONTAINER ===
        VBox rootContent = new VBox(30);
        rootContent.setPadding(new Insets(30, 40, 30, 40));
        rootContent.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa 0%, #e9ecef 100%);");

        // === 2. HEADER ===
        VBox header = createModernHeader();

        // === 3. THỐNG KÊ NHANH ===
        GridPane quickStats = createQuickStats();

        // === 4. HÀNG BIỂU ĐỒ 1 ===
        GridPane chartsRow1 = new GridPane();
        chartsRow1.setHgap(25);
        chartsRow1.setVgap(25);

        VBox revenueBox = createRevenueChartBox();
        VBox bestSellerBox = createBestSellerChartBox();

        GridPane.setHgrow(revenueBox, Priority.ALWAYS);
        GridPane.setHgrow(bestSellerBox, Priority.ALWAYS);

        chartsRow1.add(revenueBox, 0, 0);
        chartsRow1.add(bestSellerBox, 1, 0);

        // === 5. BIỂU ĐỒ KHUNG GIỜ KHÁCH ===
        VBox customerChartBox = createCustomerTimeChartBox();

        // === 6. ADD ALL TO ROOT ===
        rootContent.getChildren().addAll(header, quickStats, chartsRow1, customerChartBox);

        // Animation Fade In
        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), rootContent);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        // === 7. SCROLLPANE ===
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(rootContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        setCenter(scrollPane);
    }

    // =================================================================================================
    // 🔥 HÀM TIỆN ÍCH: THÊM HIỆU ỨNG HOVER & TOOLTIP (GIỐNG THỐNG KÊ)
    // =================================================================================================
    private void addHoverEffect(Node node, String tooltipText) {
        if (node == null) return;

        // 1. Tooltip Style (Giống Gui_ThongKe)
        Tooltip tooltip = new Tooltip(tooltipText);
        tooltip.setStyle("-fx-text-fill: black; -fx-font-size: 12px; -fx-background-color: #e2e7ed; -fx-padding: 8px; -fx-background-radius: 5;");
        tooltip.setShowDelay(Duration.millis(100)); // Hiện nhanh hơn mặc định
        Tooltip.install(node, tooltip);

        // 2. Animation Scale (Phóng to/Thu nhỏ)
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), node);
        scaleUp.setToX(1.1);
        scaleUp.setToY(1.1);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), node);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        // 3. Sự kiện chuột
        node.setOnMouseEntered(e -> {
            node.setCursor(Cursor.HAND);
            scaleUp.playFromStart();
            node.toFront(); // Đưa node lên trên cùng để không bị che khi phóng to
        });

        node.setOnMouseExited(e -> {
            node.setCursor(Cursor.DEFAULT);
            scaleDown.playFromStart();
        });
    }

    // =================================================================================================
    // SECTION 1: HEADER & STAT CARDS
    // =================================================================================================

    private VBox createModernHeader() {
        VBox header = new VBox(10);
        header.setPadding(new Insets(20, 0, 20, 0));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #082744; -fx-background-radius: 15; -fx-padding: 30; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 4);");

        Label title = new Label("🚀 DASHBOARD TỔNG QUAN");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: white;");

        Label subtitle = new Label("Chào mừng trở lại! Dưới đây là tình hình kinh doanh hôm nay.");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        subtitle.setStyle("-fx-text-fill: rgba(255,255,255,0.9);");

        header.getChildren().addAll(title, subtitle);
        return header;
    }

    private GridPane createQuickStats() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(0, 0, 10, 0));

        double doanhThuHomNay = dashboardDAO.getTongDoanhThuHomNay();
        double doanhThuHomQua = dashboardDAO.getTongDoanhThuHomQua();
        String doanhThuChange = dashboardDAO.tinhPhanTramThayDoi(doanhThuHomNay, doanhThuHomQua);

        int luotDatBanHomNay = dashboardDAO.getLuotDatBanHomNay();
        int luotDatBanHomQua = dashboardDAO.getLuotDatBanHomQua();
        String datBanChange = dashboardDAO.tinhPhanTramThayDoi(luotDatBanHomNay, luotDatBanHomQua);

        int monBanRaHomNay = dashboardDAO.getSoMonBanRaHomNay();
        int monBanRaHomQua = dashboardDAO.getSoMonBanRaHomQua();
        String monBanChange = dashboardDAO.tinhPhanTramThayDoi(monBanRaHomNay, monBanRaHomQua);

        int khachHomNay = dashboardDAO.getSoKhachPhucVuHomNay();
        int khachHomQua = dashboardDAO.getSoKhachPhucVuHomQua();
        String khachChange = dashboardDAO.tinhPhanTramThayDoi(khachHomNay, khachHomQua);

        grid.add(createModernStatCard("Doanh thu hôm nay", currencyFormat.format(doanhThuHomNay), "", doanhThuChange, "so với hôm qua", "#4CAF50", "💰"), 0, 0);
        grid.add(createModernStatCard("Lượt đặt bàn", String.valueOf(luotDatBanHomNay), "lượt", datBanChange, "so với hôm qua", "#2196F3", "📅"), 1, 0);
        grid.add(createModernStatCard("Món bán ra", String.valueOf(monBanRaHomNay), "phần", monBanChange, "so với hôm qua", "#FF9800", "🍽"), 2, 0);
        grid.add(createModernStatCard("Khách phục vụ", String.valueOf(khachHomNay), "khách", khachChange, "so với hôm qua", "#E91E63", "👥"), 3, 0);

        return grid;
    }

    private VBox createModernStatCard(String title, String mainValue, String unit, String trend, String subText, String color, String icon) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(20));
        card.setPrefSize(290, 140);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 3);");

        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 5); -fx-scale-x: 1.02; -fx-scale-y: 1.02;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 3);"));

        HBox topRow = new HBox(12);
        topRow.setAlignment(Pos.CENTER_LEFT);
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font("Segoe UI Emoji", 26));
        iconLabel.setStyle("-fx-text-fill: " + color + "; -fx-background-color: " + color + "22; -fx-background-radius: 50%; -fx-min-width: 45; -fx-min-height: 45; -fx-alignment: center;");
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        titleLabel.setStyle("-fx-text-fill: #636e72;");
        topRow.getChildren().addAll(iconLabel, titleLabel);

        HBox valueRow = new HBox(6);
        valueRow.setAlignment(Pos.BASELINE_LEFT);
        Label value = new Label(mainValue);
        value.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        value.setStyle("-fx-text-fill: #2d3436;");
        Label unitLabel = new Label(unit);
        unitLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        unitLabel.setStyle("-fx-text-fill: #b2bec3;");
        valueRow.getChildren().addAll(value, unitLabel);

        HBox footerRow = new HBox(10);
        footerRow.setAlignment(Pos.CENTER_LEFT);
        Label trendLabel = new Label(trend);
        trendLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        String bgHex = trend.contains("+") || trend.contains("↑") ? "#d4edda" : "#f8d7da";
        String textHex = trend.contains("+") || trend.contains("↑") ? "#155724" : "#721c24";
        trendLabel.setStyle("-fx-text-fill: " + textHex + "; -fx-background-color: " + bgHex + "; -fx-background-radius: 6; -fx-padding: 4 8;");
        Label subLabel = new Label(subText);
        subLabel.setStyle("-fx-text-fill: #a4b0be; -fx-font-size: 12px; -fx-font-weight: bold;");
        footerRow.getChildren().addAll(trendLabel, subLabel);

        card.getChildren().addAll(topRow, valueRow, footerRow);
        return card;
    }

    // =================================================================================================
    // SECTION 2: REVENUE CHART (UPDATED WITH HOVER EFFECT)
    // =================================================================================================

    private VBox createRevenueChartBox() {
        ComboBox<String> cbo = new ComboBox<>(FXCollections.observableArrayList("Hôm nay", "Tuần này", "Tháng này"));
        cbo.setValue("Tuần này");
        cbo.setStyle("-fx-background-radius: 5; -fx-background-color: #f1f2f6;");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Doanh thu (VNĐ)");
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);
        chart.setPrefHeight(350);
        chart.setAnimated(false);
        applyBlackTextStyle(chart, xAxis, yAxis);

        Runnable updateChart = () -> {
            chart.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            Map<String, Double> data;

            switch (cbo.getValue()) {
                case "Hôm nay" -> data = dashboardDAO.getDoanhThuHomNayTheoGio();
                case "Tháng này" -> data = dashboardDAO.getDoanhThuTheoThang();
                default -> data = dashboardDAO.getDoanhThuTheoTuan();
            }

            for (Map.Entry<String, Double> e : data.entrySet()) {
                series.getData().add(new XYChart.Data<>(e.getKey(), e.getValue()));
            }
            chart.getData().add(series);

            // ✅ ÁP DỤNG HIỆU ỨNG SAU KHI DỮ LIỆU ĐƯỢC THÊM
            for (XYChart.Data<String, Number> dataPoint : series.getData()) {
                Node node = dataPoint.getNode();
                if (node != null) {
                    node.setStyle("-fx-bar-fill: #667eea; -fx-background-radius: 3 3 0 0;");
                    String tooltipText = dataPoint.getXValue() + ": " + String.format("%,.0f VND", dataPoint.getYValue().doubleValue());
                    addHoverEffect(node, tooltipText); // GỌI HÀM HOVER
                }
            }

            Platform.runLater(() -> adjustBarPositions(chart, data.size()));
        };

        updateChart.run();
        cbo.setOnAction(e -> updateChart.run());

        return createStyledChart(chart, "Biểu đồ Doanh thu", cbo);
    }

    private void adjustBarPositions(BarChart<String, Number> chart, int dataCount) {
        if (dataCount == 0) return;
        Node plotArea = chart.lookup(".chart-plot-background");
        if (plotArea == null) return;
        double plotWidth = plotArea.getBoundsInLocal().getWidth();
        if (plotWidth <= 0) return;
        double categorySpacing = plotWidth / dataCount;
        double barWidth = Math.min(categorySpacing * 0.7, 50);
        chart.setCategoryGap(categorySpacing - barWidth);
        chart.setBarGap(0);
    }

    // =================================================================================================
    // SECTION 3: BEST SELLER & CUSTOMER CHARTS (UPDATED WITH HOVER EFFECT)
    // =================================================================================================

    private VBox createBestSellerChartBox() {
        ComboBox<String> cbo = new ComboBox<>(FXCollections.observableArrayList("Hôm nay", "Tuần này", "Tháng này"));
        cbo.setValue("Tuần này");
        cbo.setStyle("-fx-background-radius: 5; -fx-background-color: #f1f2f6;");

        PieChart chart = new PieChart();
        chart.setPrefSize(500, 350);
        chart.setLabelsVisible(true);
        chart.setLegendVisible(false);
        chart.setStyle("-fx-font-family: 'Segoe UI';");

        GridPane legendBox = new GridPane();
        legendBox.setHgap(20); legendBox.setVgap(10);
        legendBox.setPadding(new Insets(10, 0, 0, 0));
        legendBox.setAlignment(Pos.CENTER);

        Runnable updateChart = () -> {
            chart.getData().clear();
            legendBox.getChildren().clear();
            Map<String, Integer> data;
            int fixedLimit = 5;

            switch (cbo.getValue()) {
                case "Hôm nay" -> data = dashboardDAO.getTopMonBanChayHomNay(fixedLimit);
                case "Tháng này" -> data = dashboardDAO.getTopMonBanChayTheoThang(fixedLimit);
                default -> data = dashboardDAO.getTopMonBanChay(fixedLimit);
            }

            double total = data.values().stream().mapToDouble(v -> v).sum();
            int col = 0, row = 0;
            String[] colors = {"#4CAF50", "#2196F3", "#FF9800", "#E91E63", "#9C27B0"};
            int colorIndex = 0;

            for (Map.Entry<String, Integer> e : data.entrySet()) {
                double percent = total == 0 ? 0 : (e.getValue() / total) * 100;
                String displayName = e.getKey() + " (" + String.format("%.1f%%", percent) + ")";
                PieChart.Data pieData = new PieChart.Data(displayName, e.getValue());
                chart.getData().add(pieData);

                final String color = colors[colorIndex % colors.length];
                Node node = pieData.getNode();
                if (node != null) {
                    node.setStyle("-fx-pie-color: " + color + ";");
                    String tooltipText = e.getKey() + ": " + e.getValue() + " phần (" + String.format("%.1f%%", percent) + ")";
                    addHoverEffect(node, tooltipText); // ✅ GỌI HÀM HOVER
                }
                colorIndex++;

                // Custom Legend
                HBox legendItem = new HBox(8);
                legendItem.setAlignment(Pos.CENTER_LEFT);
                Rectangle colorRect = new Rectangle(12, 12, Color.web(color));
                colorRect.setArcWidth(4); colorRect.setArcHeight(4);
                Label lblInfo = new Label(e.getKey() + ": " + String.format("%.1f%%", percent) + "%");
                lblInfo.setStyle("-fx-text-fill: #2d3436;");
                lblInfo.setFont(Font.font("Segoe UI", 12));
                legendItem.getChildren().addAll(colorRect, lblInfo);
                legendBox.add(legendItem, col, row);

                col++;
                if (col == 2) { col = 0; row++; }
            }
        };

        updateChart.run();
        cbo.setOnAction(e -> updateChart.run());

        VBox content = new VBox(10, chart, legendBox);
        content.setAlignment(Pos.CENTER);
        return createStyledChart(content, "Top 5 Món bán chạy", cbo);
    }

    private VBox createCustomerTimeChartBox() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Khung giờ");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Số lượng khách");

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setLegendVisible(false);
        lineChart.setPrefHeight(350);
        applyBlackTextStyle(lineChart, xAxis, yAxis);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        Map<String, Integer> khachData = dashboardDAO.getLuongKhachTheoKhungGio();
        String[] khungGio = {"7-9h", "9-11h", "11-13h", "13-17h", "17-21h"};

        for (String kg : khungGio) {
            int soKhach = khachData.getOrDefault(kg, 0);
            series.getData().add(new XYChart.Data<>(kg, soKhach));
        }
        lineChart.getData().add(series);

        Platform.runLater(() -> {
            Node line = lineChart.lookup(".chart-series-line");
            if(line != null) line.setStyle("-fx-stroke: #FF5722; -fx-stroke-width: 2px;");

            for(XYChart.Data<String, Number> d : series.getData()) {
                Node symbol = d.getNode();
                if(symbol != null) {
                    symbol.setStyle("-fx-background-color: #FF5722, white;");
                    String tooltipText = d.getXValue() + ": " + d.getYValue() + " khách";
                    addHoverEffect(symbol, tooltipText); // ✅ GỌI HÀM HOVER CHO ĐIỂM TRÊN BIỂU ĐỒ
                }
            }
        });

        return createStyledChart(lineChart, "Lượng khách theo khung giờ hôm nay", null);
    }

    private VBox createStyledChart(Node content, String subtitle, Node rightControl) {
        VBox wrapper = new VBox(10);
        wrapper.setPadding(new Insets(20));
        wrapper.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 12, 0, 0, 2);");

        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 16));
        subtitleLabel.setStyle("-fx-text-fill: #2c3e50;");

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        if (rightControl != null) {
            header.getChildren().addAll(subtitleLabel, spacer, rightControl);
        } else {
            header.getChildren().add(subtitleLabel);
        }

        wrapper.getChildren().addAll(header, content);
        return wrapper;
    }

    private void applyBlackTextStyle(Chart chart, Axis xAxis, Axis yAxis) {
        String chartStyle = "-fx-text-fill: black; -fx-font-family: 'Segoe UI'; -fx-font-size: 12px;";
        String axisStyle = "-fx-tick-label-fill: #2d3436; -fx-axis-label-fill: #2d3436; -fx-font-family: 'Segoe UI'; -fx-font-size: 11px;";
        chart.setStyle(chartStyle);
        if (xAxis != null) xAxis.setStyle(axisStyle);
        if (yAxis != null) yAxis.setStyle(axisStyle);
    }
}