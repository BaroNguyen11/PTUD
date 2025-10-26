package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;

public class Dashboard extends BorderPane {

    public Dashboard() {
        setStyle("-fx-background-color: #f5f5f5;");

        // === SIDEBAR ===

        // === MAIN CONTENT ===
        VBox content = new VBox(25);
        content.setPadding(new Insets(20, 25, 20, 25));

        // === HEADER ===
        Label lblTitle = new Label("Dashboard");
        lblTitle.setFont(Font.font("Arial", 28));
        lblTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #3e2723;");

        // === THỐNG KÊ NHANH ===
        HBox quickStats = new HBox(20);
        quickStats.setAlignment(Pos.CENTER);
        quickStats.getChildren().addAll(
                createStatCard("💰 Doanh thu hôm nay", "12.540.000 VNĐ", "+5%", "so với hôm qua" , "#4CAF50"),
                createStatCard("📅 Lượt đặt bàn", "36 lượt", "-2%","so với hôm qua", "#F44336"),
                createStatCard("🍽 Món bán ra", "125 phần", "+8%", "so với hôm qua", "#4CAF50"),
                createStatCard("👥 Khách phục vụ", "98 khách", "+3%", "so với hôm qua", "#4CAF50")
        );

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
        lblCustomer.setStyle("-fx-text-fill: #3e2723; -fx-font-weight: bold;");
        LineChart<String, Number> customerChart = createCustomerTimeChart();
        customerChartBox.getChildren().addAll(lblCustomer, customerChart);

        content.getChildren().addAll(lblTitle, quickStats, chartsRow1, customerChartBox);
        setCenter(content);
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

    // --- KHỐI BIỂU ĐỒ DOANH THU ---
    private VBox createRevenueChartBox() {
        Label lbl = new Label("📈 Doanh thu theo ngày");
        lbl.setFont(Font.font("Arial", 18));
        lbl.setStyle("-fx-text-fill: #3e2723; -fx-font-weight: bold;");

        ComboBox<String> cbo = new ComboBox<>(FXCollections.observableArrayList("Hôm nay", "Tuần này", "Tháng này"));
        cbo.setValue("Hôm nay");

        HBox header = new HBox(10, lbl, cbo);
        header.setAlignment(Pos.CENTER_LEFT);

        BarChart<String, Number> chart = createRevenueChart();

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

    // --- BIỂU ĐỒ DOANH THU ---
    private BarChart<String, Number> createRevenueChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Doanh thu (VNĐ)");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);
        chart.setPrefHeight(300);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().addAll(
                new XYChart.Data<>("Thứ 2", 4200000),
                new XYChart.Data<>("Thứ 3", 5500000),
                new XYChart.Data<>("Thứ 4", 3800000),
                new XYChart.Data<>("Thứ 5", 6200000),
                new XYChart.Data<>("Thứ 6", 8000000)
        );

        chart.getData().add(series);

        // Làm cột doanh thu nổi bật
        chart.setStyle("""
            .chart-bar {
                -fx-bar-fill: #8D6E63;
            }
        """);

        // Hiển thị giá trị trên cột
        chart.getData().forEach(s -> s.getData().forEach(d -> {
            Label label = new Label(String.format("%,.0f", d.getYValue().doubleValue()));
            label.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: #3e2723;");
            d.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    StackPane node = (StackPane) newNode;
                    node.parentProperty().addListener((o, oldParent, newParent) -> {
                        if (newParent != null) {
                            ((Pane) chart.lookup(".chart-plot-background")).getChildren().add(label);
                            label.layoutXProperty().bind(node.layoutXProperty().add(5));
                            label.layoutYProperty().bind(node.layoutYProperty().subtract(15));
                        }
                    });
                }
            });
        }));

        return chart;
    }

    // --- KHỐI BIỂU ĐỒ MÓN ĂN ---
    private VBox createBestSellerChartBox() {
        Label lbl = new Label("🍛 Top món bán chạy");
        lbl.setFont(Font.font("Arial", 18));
        lbl.setStyle("-fx-text-fill: #3e2723; -fx-font-weight: bold;");

        ComboBox<String> cbo = new ComboBox<>(FXCollections.observableArrayList("Hôm nay", "Tuần này", "Tháng này"));
        cbo.setValue("Tuần này");

        HBox header = new HBox(10, lbl, cbo);
        header.setAlignment(Pos.CENTER_LEFT);

        PieChart chart = new PieChart();
        ObservableList<PieChart.Data> dataList = chart.getData();
        dataList.addAll(
                new PieChart.Data("Lẩu hải sản", 30),
                new PieChart.Data("Bò nướng tiêu đen", 25),
                new PieChart.Data("Gỏi cuốn tôm", 20),
                new PieChart.Data("Cơm chiên dương châu", 15),
                new PieChart.Data("Canh chua cá lóc", 10)
        );

        double total = dataList.stream().mapToDouble(PieChart.Data::getPieValue).sum();
        for (PieChart.Data data : dataList) {
            data.nameProperty().bind(
                    javafx.beans.binding.Bindings.concat(
                            data.getName(), " (", String.format("%.0f%%", (data.getPieValue() / total) * 100), ")"
                    )
            );
        }

        chart.setPrefSize(450, 300);

        // === CHÚ THÍCH DƯỚI BIỂU ĐỒ ===
        GridPane legendBox = new GridPane();
        legendBox.setHgap(25);
        legendBox.setVgap(8);
        legendBox.setPadding(new Insets(10));
        legendBox.setAlignment(Pos.CENTER);

        int col = 0, row = 0;
        for (PieChart.Data d : dataList) {
            Label lblInfo = new Label(d.getName().replaceAll("\\(.*\\)", "").trim() + ": " + (int) d.getPieValue() + " phần");
            lblInfo.setFont(Font.font(13));
            legendBox.add(lblInfo, col, row);
            col++;
            if (col == 2) { col = 0; row++; }
        }

        VBox box = new VBox(10, header, chart, legendBox);
        box.setPadding(new Insets(15));
        box.setStyle("""
                -fx-background-color: white;
                -fx-border-radius: 10;
                -fx-background-radius: 10;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8,0,0,4);
                """);
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
        series.getData().addAll(
                new XYChart.Data<>("7-9h", 5),
                new XYChart.Data<>("9-11h", 20),
                new XYChart.Data<>("11-13h", 45),
                new XYChart.Data<>("13-17h", 15),
                new XYChart.Data<>("17-21h", 60)
        );
        lineChart.getData().add(series);
        return lineChart;
    }
}