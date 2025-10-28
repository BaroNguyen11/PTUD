
package gui;

import dao.Dashboard_DAO;
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
import javafx.scene.text.Text;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class Dashboard extends BorderPane {

    private Dashboard_DAO dashboardDAO;
    private NumberFormat currencyFormat;

    public Dashboard() {
        dashboardDAO = new Dashboard_DAO();
        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        setStyle("-fx-background-color: #f5f5f5;");

        // === MAIN CONTENT ===
        VBox content = new VBox(25);
        content.setPadding(new Insets(20, 25, 20, 25));
        content.setMinWidth(1300);
        // === HEADER ===
        Label lblTitle = new Label("Dashboard");
        lblTitle.setFont(Font.font("Arial", 28));
        lblTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #3e2723;");
        // === THỐNG KÊ NHANH ===
        HBox quickStats = createQuickStats();

        // === HÀNG BIỂU ĐỒ 1: Doanh thu & Món ăn ===
        HBox chartsRow1 = new HBox(30);
        chartsRow1.setAlignment(Pos.CENTER);

        VBox revenueBox = createRevenueChartBox();
        VBox bestSellerBox = createBestSellerChartBox();

        chartsRow1.getChildren().addAll(revenueBox, bestSellerBox);

        // === BIỂU ĐỒ KHUNG GIỜ KHÁCH ===
        VBox customerChartBox = new VBox(10);
        customerChartBox.setAlignment(Pos.TOP_LEFT);
        Label lblCustomer = new Label("👥 Lượng khách theo khung giờ hôm nay");
        lblCustomer.setFont(Font.font("Arial", 18));
        lblCustomer.setStyle("-fx-text-fill: #3e2723; -fx-font-weight: bold;-fx-font-size: 18");
        LineChart<String, Number> customerChart = createCustomerTimeChart();
        customerChartBox.getChildren().addAll(lblCustomer, customerChart);

        content.getChildren().addAll(lblTitle, quickStats, chartsRow1, customerChartBox);
//    setCenter(content);
// === Đưa nội dung vào ScrollPane ===
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true); // giúp nội dung bám full chiều ngang
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPannable(true); // cho phép kéo bằng chuột
        scrollPane.setStyle("-fx-background-color: transparent;");

// Đặt ScrollPane làm trung tâm
        setCenter(scrollPane);


    }

    // === TẠO THỐNG KÊ NHANH ===
    private HBox createQuickStats() {
        HBox quickStats = new HBox(20);
        quickStats.setAlignment(Pos.CENTER);

        // Lấy dữ liệu từ DAO
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

        // Tạo các card
        quickStats.getChildren().addAll(
                createStatCard("💰 Doanh thu hôm nay",
                        currencyFormat.format(doanhThuHomNay),
                        doanhThuChange,
                        "so với hôm qua",
                        doanhThuChange.startsWith("+") ? "#4CAF50" : "#F44336"),

                createStatCard("📅 Lượt đặt bàn",
                        luotDatBanHomNay + " lượt",
                        datBanChange,
                        "so với hôm qua",
                        datBanChange.startsWith("+") ? "#4CAF50" : "#F44336"),

                createStatCard("🍽 Món bán ra",
                        monBanRaHomNay + " phần",
                        monBanChange,
                        "so với hôm qua",
                        monBanChange.startsWith("+") ? "#4CAF50" : "#F44336"),

                createStatCard("👥 Khách phục vụ",
                        khachHomNay + " khách",
                        khachChange,
                        "so với hôm qua",
                        khachChange.startsWith("+") ? "#4CAF50" : "#F44336")
        );

        return quickStats;
    }

    // --- CARD THỐNG KÊ ---
    private VBox createStatCard(String title, String value, String change, String note, String changeColor) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(15));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPrefWidth(250);
        card.setStyle("""
            -fx-background-color: white;
            -fx-border-radius: 10;
            -fx-background-radius: 10;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 10, 0, 0, 4);
        """);

        Label lblTitle = new Label(title);
        lblTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #3e2723;");

        Label lblValue = new Label(value);
        lblValue.setFont(Font.font("Arial", 22));
        lblValue.setStyle("-fx-text-fill: #795548;");

        HBox changeBox = new HBox(5);
        Label lblChange = new Label(change);
        lblChange.setStyle("-fx-text-fill: " + changeColor + "; -fx-font-weight: bold;");
        Label lblNote = new Label(note);
        lblNote.setStyle("-fx-text-fill: #777; -fx-font-size: 12px;");
        changeBox.getChildren().addAll(lblChange, lblNote);

        card.getChildren().addAll(lblTitle, lblValue, changeBox);
        return card;
    }

    // --- BIỂU ĐỒ DOANH THU (ĐÃ FIX CĂN LỆCH) ---
    private BarChart<String, Number> createRevenueChart(Map<String, Double> doanhThuData, String title) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Thời gian");
        yAxis.setLabel("Doanh thu (VND)");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle(title);
        chart.setLegendVisible(false);
        chart.setAnimated(true);

        // Tạo dữ liệu
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Double> entry : doanhThuData.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        chart.getData().add(series);

        // === FIX CĂN LỆCH HIỆU QUẢ ===
        chart.applyCss();
        chart.layout();

        // Sử dụng ChangeListener thay vì layoutBoundsProperty
        chart.widthProperty().addListener((obs, oldVal, newVal) -> {
            adjustBarPositions(chart, doanhThuData.size());
        });

        // Gọi ngay sau khi render
        Platform.runLater(() -> {
            adjustBarPositions(chart, doanhThuData.size());
        });

        // Cấu hình trục X
        xAxis.setTickLabelRotation(0);
        xAxis.setTickLabelGap(5);
        xAxis.setTickLabelFont(Font.font("Arial", 11));

        // Tooltip
        for (XYChart.Data<String, Number> data : series.getData()) {
            Tooltip tooltip = new Tooltip(data.getXValue() + ": " +
                    String.format("%,.0f VND", data.getYValue().doubleValue()));
            Tooltip.install(data.getNode(), tooltip);

            // Thêm style cho bar
            data.getNode().setStyle("-fx-bar-fill: #4CAF50; -fx-background-radius: 3 3 0 0;");
        }

        chart.setPrefHeight(300);
        chart.setPrefWidth(600);
        return chart;
    }

    // === PHƯƠNG THỨC ĐIỀU CHỈNH VỊ TRÍ BAR ===
    private void adjustBarPositions(BarChart<String, Number> chart, int dataCount) {
        if (dataCount == 0) return;

        Node plotArea = chart.lookup(".chart-plot-background");
        if (plotArea == null) return;

        double plotWidth = plotArea.getBoundsInLocal().getWidth();
        if (plotWidth <= 0) return;

        double categorySpacing = plotWidth / dataCount;
        double barWidth = Math.min(categorySpacing * 0.7, 50); // 70% mỗi category, max 50px

        chart.setCategoryGap(categorySpacing - barWidth);
        chart.setBarGap(0);

    }

    // --- CẬP NHẬT PHƯƠNG THỨC createRevenueChartBox ---
    private VBox createRevenueChartBox() {
        Label lbl = new Label("📈 Doanh thu ");
        lbl.setFont(Font.font("Arial", 18));
        lbl.setStyle("-fx-text-fill: #3e2723; -fx-font-weight: bold;-fx-font-size: 18px;");

        ComboBox<String> cbo = new ComboBox<>(FXCollections.observableArrayList(
                "Hôm nay", "Tuần này", "Tháng này"));
        cbo.setValue("Tuần này");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Doanh thu (VNĐ)");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);
        chart.setPrefHeight(300);
        chart.setAnimated(false);

        // Hàm load và fix layout
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

            // Fix layout sau khi có data
            Platform.runLater(() -> {
                adjustBarPositions(chart, data.size());
            });
        };

        updateChart.run();
        cbo.setOnAction(e -> updateChart.run());

        HBox header = new HBox(10, lbl, cbo);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox box = new VBox(10, header, chart);
        box.setPadding(new Insets(15));
        box.setStyle("""
                -fx-background-color: white;
                -fx-border-radius: 10;
                -fx-background-radius: 10;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8,0,0,4);
                """);
        box.setPrefWidth(550);
        return box;
    }



    // --- KHỐI BIỂU ĐỒ MÓN ĂN ---
    private VBox createBestSellerChartBox() {
        // Sửa tiêu đề để cố định Top 5
        Label lbl = new Label("🛒 Top 5 món bán chạy nhất");
        lbl.setFont(Font.font("Arial", 20)); // Tăng font size
        lbl.setStyle("-fx-text-fill: #3e2723; -fx-font-weight: bold;-fx-font-size: 18px;");

        ComboBox<String> cbo = new ComboBox<>(FXCollections.observableArrayList("Hôm nay", "Tuần này", "Tháng này"));
        cbo.setValue("Tuần này");
        cbo.setPrefWidth(120);

        PieChart chart = new PieChart();
        chart.setPrefSize(550, 400); // Tăng kích thước biểu đồ
        chart.setLabelLineLength(20); // Tăng độ dài đường label
        chart.setLabelsVisible(true);
        chart.setLegendVisible(false); // Ẩn legend mặc định

        GridPane legendBox = new GridPane();
        legendBox.setHgap(30); // Tăng khoảng cách ngang
        legendBox.setVgap(10); // Tăng khoảng cách dọc
        legendBox.setPadding(new Insets(15));
        legendBox.setAlignment(Pos.CENTER);

        // Hàm load dữ liệu theo lựa chọn
        Runnable updateChart = () -> {
            chart.getData().clear();
            legendBox.getChildren().clear();

            Map<String, Integer> data;

            // ********** SỬA LOGIC LẤY DỮ LIỆU **********
            // Sử dụng limit = 5 cho các hàm DAO (hoặc dùng 8 nếu bạn muốn Top 8 như trong logic cũ)
            // Tôi sẽ dùng 5 cho đúng yêu cầu "Top 5" trong tiêu đề.
            int fixedLimit = 5;

            switch (cbo.getValue()) {
                case "Hôm nay" -> data = dashboardDAO.getTopMonBanChayHomNay(fixedLimit);
                case "Tháng này" -> data = dashboardDAO.getTopMonBanChayTheoThang(fixedLimit);
                default -> data = dashboardDAO.getTopMonBanChay(fixedLimit);
            }
            // ******************************************

            double total = data.values().stream().mapToDouble(v -> v).sum();

            int col = 0, row = 0;
            int colorIndex = 0;
            String[] colors = {"#4CAF50", "#2196F3", "#FF9800", "#E91E63", "#9C27B0", "#00BCD4", "#8BC34A", "#FF5722"};

            for (Map.Entry<String, Integer> e : data.entrySet()) {
                double percent = total == 0 ? 0 : (e.getValue() / total) * 100;
                String displayName = e.getKey() + " (" + String.format("%.1f%%", percent) + ")";
                PieChart.Data pieData = new PieChart.Data(displayName, e.getValue());
                chart.getData().add(pieData);

                // Thêm màu sắc cho từng phần
                final String color = colors[colorIndex % colors.length];
                pieData.getNode().setStyle("-fx-pie-color: " + color + ";");
                colorIndex++;

                // Tạo legend với màu sắc
                HBox legendItem = new HBox(8);
                legendItem.setAlignment(Pos.CENTER_LEFT);

                Rectangle colorRect = new Rectangle(12, 12);
                colorRect.setStyle("-fx-fill: " + color + "; -fx-stroke: #666; -fx-stroke-width: 1;");

                Label lblInfo = new Label(e.getKey() + ": " + e.getValue() + " phần");
                lblInfo.setFont(Font.font(14)); // Tăng font size legend
                lblInfo.setStyle("-fx-text-fill: #333;");

                legendItem.getChildren().addAll(colorRect, lblInfo);
                legendBox.add(legendItem, col, row);

                col++;
                if (col == 2) {
                    col = 0;
                    row++;
                }
            }

            // Tùy chỉnh font cho label trên biểu đồ
            for (PieChart.Data d : chart.getData()) {
                Text text = (Text) d.getNode().lookup(".chart-pie-label");
                if (text != null) {
                    text.setFont(Font.font("Arial", FontWeight.BOLD, 12));
                    text.setFill(Color.WHITE);
                    text.setStroke(Color.BLACK);
                    text.setStrokeWidth(0.3);
                }
            }
        };

        // Khởi chạy và thiết lập sự kiện
        updateChart.run();
        cbo.setOnAction(e -> updateChart.run());

        HBox header = new HBox(15, lbl, cbo);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox chartContainer = new VBox(chart);
        chartContainer.setAlignment(Pos.CENTER);
        chartContainer.setPadding(new Insets(10));

        VBox box = new VBox(15, header, chartContainer, legendBox); // Tăng khoảng cách
        box.setPadding(new Insets(20)); // Tăng padding
        box.setStyle("""
            -fx-background-color: white;
            -fx-border-radius: 15;
            -fx-background-radius: 15;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12,0,0,6);
            """);
        box.setPrefWidth(650); // Tăng chiều rộng
        box.setPrefHeight(650);
        return box;
    }


    // --- BIỂU ĐỒ KHÁCH THEO KHUNG GIỜ ---
    private LineChart<String, Number> createCustomerTimeChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Khung giờ");
        xAxis.setStyle("-fx-font-weight: bold; -fx-text-fill: #3e2723;");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Số khách");

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setLegendVisible(false);
        lineChart.setPrefHeight(350);
        lineChart.setPrefWidth(1100);

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Lấy dữ liệu từ DAO
        Map<String, Integer> khachData = dashboardDAO.getLuongKhachTheoKhungGio();

        // Thêm dữ liệu theo thứ tự khung giờ
        String[] khungGio = {"7-9h", "9-11h", "11-13h", "13-17h", "17-21h"};
        for (String kg : khungGio) {
            int soKhach = khachData.getOrDefault(kg, 0);
            series.getData().add(new XYChart.Data<>(kg, soKhach));
        }

        lineChart.getData().add(series);
        return lineChart;
    }
}