//package gui;
//
//import javafx.collections.FXCollections;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.chart.BarChart;
//import javafx.scene.chart.CategoryAxis;
//import javafx.scene.chart.LineChart;
//import javafx.scene.chart.NumberAxis;
//import javafx.scene.chart.PieChart;
//import javafx.scene.chart.XYChart;
//import javafx.scene.control.Button;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.DatePicker;
//import javafx.scene.control.Label;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.Priority;
//import javafx.scene.layout.VBox;
//import javafx.scene.text.Font;
//import javafx.scene.text.FontWeight;
//
//public class ThongKe extends VBox {
//
//    public ThongKe() {
//        this.setPadding(new Insets(20));
//        this.setSpacing(20);
//        this.getStyleClass().add("thong-ke-page");
//
//        // 1. Tiêu đề
//        Label title = new Label("BÁO CÁO & THỐNG KÊ");
//        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
//        title.getStyleClass().add("thong-ke-title");
//        
//        // 2. Bộ Lọc (Filters)
//        HBox filterBar = createFilterBar();
//        
//        // 3. Thẻ Số liệu Nhanh (KPI Cards) - Tái sử dụng từ TrangChu
//        GridPane statGrid = createStatGrid();
//
//        // 4. Khu vực Biểu đồ (Sử dụng GridPane để đặt 2-3 biểu đồ)
//        GridPane chartGrid = new GridPane();
//        chartGrid.setHgap(20);
//        chartGrid.setVgap(20);
//        chartGrid.setPadding(new Insets(10));
//        
//        // Biểu đồ 1: Doanh thu theo tháng (Line Chart)
//        VBox monthlyRevenueChart = createMonthlyRevenueChart();
//        GridPane.setHgrow(monthlyRevenueChart, Priority.ALWAYS);
//        chartGrid.add(monthlyRevenueChart, 0, 0);
//
//        // Biểu đồ 2: Top 5 Món bán chạy nhất (Bar Chart)
//        VBox topSellingChart = createTopSellingChart();
//        GridPane.setHgrow(topSellingChart, Priority.ALWAYS);
//        chartGrid.add(topSellingChart, 1, 0);
//
//        // Biểu đồ 3: Tỷ lệ Doanh thu theo Danh mục (Pie Chart)
//        VBox categoryPieChart = createCategoryPieChart();
//        GridPane.setHgrow(categoryPieChart, Priority.ALWAYS);
//        chartGrid.add(categoryPieChart, 0, 1);
//        
//        this.getChildren().addAll(title, filterBar, statGrid, chartGrid);
//    }
//    
//    // --- KHU VỰC CHỨC NĂNG TẠO COMPONENT ---
//
//    private HBox createFilterBar() {
//        HBox filterBar = new HBox(10);
//        filterBar.setAlignment(Pos.CENTER_LEFT);
//        filterBar.setPadding(new Insets(10, 0, 10, 0));
//        
//        Label lblDate = new Label("Từ ngày:");
//        DatePicker dateFrom = new DatePicker();
//        Label lblTo = new Label("Đến ngày:");
//        DatePicker dateTo = new DatePicker();
//        
//        ComboBox<String> cbType = new ComboBox<>(FXCollections.observableArrayList("Theo Ngày", "Theo Tháng", "Theo Năm"));
//        cbType.getSelectionModel().selectFirst();
//        
//        Button btnView = new Button("Xem Báo Cáo");
//        btnView.getStyleClass().add("primary-button");
//        
//        filterBar.getChildren().addAll(lblDate, dateFrom, lblTo, dateTo, cbType, btnView);
//        return filterBar;
//    }
//    
//    private GridPane createStatGrid() {
//        GridPane statsGrid = new GridPane();
//        statsGrid.setHgap(20);
//        statsGrid.setVgap(20);
//
//        // Thay thế Unicode bằng String đơn giản
//        statsGrid.add(createStatCard("Tổng Doanh thu", "98,700,000 VND", "T"), 0, 0); 
//        statsGrid.add(createStatCard("Tổng Chi phí", "45,200,000 VND", "C"), 1, 0);
//        statsGrid.add(createStatCard("Lợi nhuận gộp", "53,500,000 VND", "L"), 2, 0);
//        statsGrid.add(createStatCard("Khách hàng quay lại", "75%", "K"), 3, 0);
//
//        return statsGrid;
//    }
//
//    /** Tái sử dụng thẻ thống kê từ TrangChu nhưng sửa để dùng String */
//    private VBox createStatCard(String title, String value, String iconCode) {
//        VBox card = new VBox(10);
//        card.setPadding(new Insets(20));
//        card.setPrefSize(250, 120);
//        card.getStyleClass().add("stat-card"); 
//        
//        Label icon = new Label(iconCode);
//        icon.getStyleClass().add("card-icon");
//        icon.setFont(Font.font("Arial", FontWeight.BOLD, 24)); // Font size lớn
//
//        Label titleLabel = new Label(title);
//        titleLabel.setFont(Font.font("Arial", 12));
//        titleLabel.getStyleClass().add("card-title");
//
//        Label valueLabel = new Label(value);
//        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
//        valueLabel.getStyleClass().add("card-value");
//
//        card.getChildren().addAll(icon, titleLabel, valueLabel);
//        return card;
//    }
//
//    // --- BIỂU ĐỒ MẪU ---
//
//    private VBox createMonthlyRevenueChart() {
//        CategoryAxis xAxis = new CategoryAxis();
//        NumberAxis yAxis = new NumberAxis();
//        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
//        
//        lineChart.setTitle("Doanh thu 6 tháng gần nhất");
//        xAxis.setLabel("Tháng");
//        yAxis.setLabel("Doanh thu (VND)");
//        
//        XYChart.Series<String, Number> series = new XYChart.Series<>();
//        series.setName("Doanh thu");
//        
//        // Dữ liệu mẫu
//        series.getData().add(new XYChart.Data<>("Thg 5", 5000000));
//        series.getData().add(new XYChart.Data<>("Thg 6", 8000000));
//        series.getData().add(new XYChart.Data<>("Thg 7", 12000000));
//        series.getData().add(new XYChart.Data<>("Thg 8", 15000000));
//        series.getData().add(new XYChart.Data<>("Thg 9", 9500000));
//        series.getData().add(new XYChart.Data<>("Thg 10", 11000000));
//        
//        lineChart.getData().add(series);
//        lineChart.setPrefHeight(350);
//        
//        return new VBox(lineChart);
//    }
//    
//    private VBox createTopSellingChart() {
//        CategoryAxis xAxis = new CategoryAxis();
//        NumberAxis yAxis = new NumberAxis();
//        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
//        
//        barChart.setTitle("Top 5 Món ăn bán chạy nhất");
//        xAxis.setLabel("Món ăn");
//        yAxis.setLabel("Số lượng bán");
//        barChart.setLegendVisible(false); // Ẩn chú thích
//        
//        XYChart.Series<String, Number> series = new XYChart.Series<>();
//        
//        // Dữ liệu mẫu
//        series.getData().add(new XYChart.Data<>("Phở", 120));
//        series.getData().add(new XYChart.Data<>("Bún bò", 95));
//        series.getData().add(new XYChart.Data<>("Cơm sườn", 88));
//        series.getData().add(new XYChart.Data<>("Trà sữa", 150));
//        series.getData().add(new XYChart.Data<>("Nước cam", 70));
//        
//        barChart.getData().add(series);
//        barChart.setPrefHeight(350);
//        
//        return new VBox(barChart);
//    }
//    
//    private VBox createCategoryPieChart() {
//        // Dữ liệu mẫu
//        PieChart pieChart = new PieChart(FXCollections.observableArrayList(
//            new PieChart.Data("Đồ ăn chính (55%)", 55),
//            new PieChart.Data("Đồ uống (30%)", 30),
//            new PieChart.Data("Món khai vị (15%)", 15)
//        ));
//        
//        pieChart.setTitle("Cơ cấu Doanh thu");
//        pieChart.setLabelsVisible(true);
//        pieChart.setLegendVisible(true);
//        
//        return new VBox(pieChart);
//    }
//}
package gui;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip; // Import Tooltip
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ThongKe extends VBox {

    public ThongKe() {
        this.setPadding(new Insets(20));
        this.setSpacing(20);
        this.getStyleClass().add("thong-ke-page");

        // 1. Tiêu đề
        Label title = new Label("BÁO CÁO & THỐNG KÊ");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.getStyleClass().add("thong-ke-title");
        
        // 2. Bộ Lọc (Filters)
        HBox filterBar = createFilterBar();
        
        // 3. Thẻ Số liệu Nhanh (KPI Cards) - Tái sử dụng từ TrangChu
        GridPane statGrid = createStatGrid();

        // 4. Khu vực Biểu đồ (Sử dụng GridPane để đặt 2-3 biểu đồ)
        GridPane chartGrid = new GridPane();
        chartGrid.setHgap(20);
        chartGrid.setVgap(20);
        chartGrid.setPadding(new Insets(10));
        
        // Biểu đồ 1: Doanh thu theo tháng (Line Chart)
        VBox monthlyRevenueChart = createMonthlyRevenueChart();
        GridPane.setHgrow(monthlyRevenueChart, Priority.ALWAYS);
        chartGrid.add(monthlyRevenueChart, 0, 0);

        // Biểu đồ 2: Top 5 Món bán chạy nhất (Bar Chart)
        VBox topSellingChart = createTopSellingChart();
        GridPane.setHgrow(topSellingChart, Priority.ALWAYS);
        chartGrid.add(topSellingChart, 1, 0);

        // Biểu đồ 3: Tỷ lệ Doanh thu theo Danh mục (Pie Chart)
        VBox categoryPieChart = createCategoryPieChart();
        GridPane.setHgrow(categoryPieChart, Priority.ALWAYS);
        chartGrid.add(categoryPieChart, 0, 1);
        
        this.getChildren().addAll(title, filterBar, statGrid, chartGrid);
    }
    
    // --- KHU VỰC CHỨC NĂNG TẠO COMPONENT ---

    private HBox createFilterBar() {
        HBox filterBar = new HBox(10);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(10, 0, 10, 0));
        
        Label lblDate = new Label("Từ ngày:");
        DatePicker dateFrom = new DatePicker();
        Label lblTo = new Label("Đến ngày:");
        DatePicker dateTo = new DatePicker();
        
        ComboBox<String> cbType = new ComboBox<>(FXCollections.observableArrayList("Theo Ngày", "Theo Tháng", "Theo Năm"));
        cbType.getSelectionModel().selectFirst();
        
        Button btnView = new Button("Xem Báo Cáo");
        btnView.getStyleClass().add("primary-button");
        
        filterBar.getChildren().addAll(lblDate, dateFrom, lblTo, dateTo, cbType, btnView);
        return filterBar;
    }
    
    private GridPane createStatGrid() {
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(20);
        statsGrid.setVgap(20);

        // Thay thế Unicode bằng String đơn giản
        statsGrid.add(createStatCard("Tổng Doanh thu", "98,700,000 VND", "T"), 0, 0); 
        statsGrid.add(createStatCard("Tổng Chi phí", "45,200,000 VND", "C"), 1, 0);
        statsGrid.add(createStatCard("Lợi nhuận gộp", "53,500,000 VND", "L"), 2, 0);
        statsGrid.add(createStatCard("Khách hàng quay lại", "75%", "K"), 3, 0);

        return statsGrid;
    }

    /** Tái sử dụng thẻ thống kê từ TrangChu nhưng sửa để dùng String */
    private VBox createStatCard(String title, String value, String iconCode) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setPrefSize(250, 120);
        card.getStyleClass().add("stat-card"); 
        
        Label icon = new Label(iconCode);
        icon.getStyleClass().add("card-icon");
        icon.setFont(Font.font("Arial", FontWeight.BOLD, 24)); // Font size lớn

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", 12));
        titleLabel.getStyleClass().add("card-title");

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        valueLabel.getStyleClass().add("card-value");

        card.getChildren().addAll(icon, titleLabel, valueLabel);
        return card;
    }

    // --- BIỂU ĐỒ MẪU ---

    /**
     * Phương thức tiện ích để thêm Tooltip vào các điểm dữ liệu.
     */
    private <X, Y> void addChartTooltip(XYChart<X, Y> chart) {
        // Lặp qua tất cả các Series trong biểu đồ
        for (XYChart.Series<X, Y> series : chart.getData()) {
            // Lặp qua tất cả các điểm dữ liệu trong Series đó
            for (XYChart.Data<X, Y> data : series.getData()) {
                // Lấy Node (hình ảnh trực quan) của điểm dữ liệu
                javafx.scene.Node node = data.getNode();
                if (node != null) {
                    // Định dạng giá trị hiển thị trong Tooltip
                    String tooltipText = String.format("%s: %s VND", 
                                                       data.getXValue().toString(), 
                                                       data.getYValue().toString());
                    
                    // Thêm Tooltip vào Node
                    Tooltip tooltip = new Tooltip(tooltipText);
                    Tooltip.install(node, tooltip);
                    
                    // Thêm hiệu ứng khi hover (optional: làm điểm sáng lên)
                    node.setOnMouseEntered(event -> node.getStyleClass().add("chart-data-node-hover"));
                    node.setOnMouseExited(event -> node.getStyleClass().remove("chart-data-node-hover"));
                }
            }
        }
    }
    
    private VBox createMonthlyRevenueChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        
        lineChart.setTitle("Doanh thu 6 tháng gần nhất");
        xAxis.setLabel("Tháng");
        yAxis.setLabel("Doanh thu (VND)");
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu");
        
        // Dữ liệu mẫu
        series.getData().add(new XYChart.Data<>("Thg 5", 5000000));
        series.getData().add(new XYChart.Data<>("Thg 6", 8000000));
        series.getData().add(new XYChart.Data<>("Thg 7", 12000000));
        series.getData().add(new XYChart.Data<>("Thg 8", 15000000));
        series.getData().add(new XYChart.Data<>("Thg 9", 9500000));
        series.getData().add(new XYChart.Data<>("Thg 10", 11000000));
        
        lineChart.getData().add(series);
        lineChart.setPrefHeight(350);
        
        // GỌI HÀM THÊM TOOLTIP
        lineChart.applyCss(); // Cần gọi applyCss để đảm bảo các Node được tạo
        lineChart.layout();
        addChartTooltip(lineChart);
        
        return new VBox(lineChart);
    }
    
    private VBox createTopSellingChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        
        barChart.setTitle("Top 5 Món ăn bán chạy nhất");
        xAxis.setLabel("Món ăn");
        yAxis.setLabel("Số lượng bán");
        barChart.setLegendVisible(false); // Ẩn chú thích
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        
        // Dữ liệu mẫu
        series.getData().add(new XYChart.Data<>("Phở", 120));
        series.getData().add(new XYChart.Data<>("Bún bò", 95));
        series.getData().add(new XYChart.Data<>("Cơm sườn", 88));
        series.getData().add(new XYChart.Data<>("Trà sữa", 150));
        series.getData().add(new XYChart.Data<>("Nước cam", 70));
        
        barChart.getData().add(series);
        barChart.setPrefHeight(350);
        
        // GỌI HÀM THÊM TOOLTIP
        barChart.applyCss();
        barChart.layout();
        addChartTooltip(barChart);
        
        return new VBox(barChart);
    }
    
    private VBox createCategoryPieChart() {
        // Dữ liệu mẫu
        PieChart pieChart = new PieChart(FXCollections.observableArrayList(
            new PieChart.Data("Đồ ăn chính (55%)", 55),
            new PieChart.Data("Đồ uống (30%)", 30),
            new PieChart.Data("Món khai vị (15%)", 15)
        ));
        
        pieChart.setTitle("Cơ cấu Doanh thu");
        pieChart.setLabelsVisible(true);
        pieChart.setLegendVisible(true);
        
        // Thêm Tooltip cho PieChart (cần cách làm hơi khác Bar/Line Chart)
        pieChart.getData().forEach(data -> {
            String label = String.format("%s: %.1f%%", data.getName(), data.getPieValue());
            Tooltip tooltip = new Tooltip(label);
            Tooltip.install(data.getNode(), tooltip);
            
            // Hiệu ứng hover cho PieChart (optional)
            data.getNode().setOnMouseEntered(e -> data.getNode().getStyleClass().add("chart-data-node-hover"));
            data.getNode().setOnMouseExited(e -> data.getNode().getStyleClass().remove("chart-data-node-hover"));
        });
        
        return new VBox(pieChart);
    }
}
