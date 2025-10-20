package gui;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.util.Duration;

// Lớp này dựa trên phong cách và các hàm tiện ích từ ThongKe.java của bạn
public class KetCa extends VBox {

    public KetCa() {
        // Container chính với animation (tái sử dụng cấu trúc từ ThongKe.java)
        VBox rootContent = new VBox(40);
        rootContent.setPadding(new Insets(30, 40, 30, 40));
        rootContent.setStyle("-fx-background-color: #f8f9fa;"); // Nền đơn giản hơn
        this.getStylesheets().add(getClass().getResource("/css/ketca.css").toExternalForm());


        // 1. Khu vực Chấm Công và Nhập thủ công (GridPane)
        GridPane attendanceGrid = createAttendanceSection();

        // 2. Khu vực Tổng Kết Ca
        VBox summarySection = createSummarySection();

        // 3. Nút xác nhận kết ca (Đặt trong StackPane/HBox footer)
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button btnConfirm = new Button("Xác nhận kết ca");
        btnConfirm.getStyleClass().add("confirm-button-dark");
        HBox footer = new HBox(spacer, btnConfirm);
        footer.setPadding(new Insets(20, 0, 0, 0));
        footer.setAlignment(Pos.BOTTOM_RIGHT);


        rootContent.getChildren().addAll(attendanceGrid, summarySection, footer);

        // Smooth fade-in animation
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
    
    // ===== 1. KHU VỰC CHẤM CÔNG VÀ NHẬP THỦ CÔNG =====
    private GridPane createAttendanceSection() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);

        VBox chamCongCard = createChamCongNhanh();
        VBox nhapThuCongCard = createNhapThuCong();
        
        GridPane.setHgrow(chamCongCard, Priority.ALWAYS);
        GridPane.setHgrow(nhapThuCongCard, Priority.ALWAYS);
        
        grid.add(chamCongCard, 0, 0);
        grid.add(nhapThuCongCard, 1, 0);

        return grid;
    }
    
    private VBox createChamCongNhanh() {
        VBox card = new VBox(20);
        card.setPadding(new Insets(30));
        card.getStyleClass().add("punch-card-light"); 
        card.setAlignment(Pos.TOP_CENTER);
        
        Label timeIcon = new Label("🕒"); 
        timeIcon.setFont(Font.font("Segoe UI Emoji", 40)); 
        timeIcon.setStyle("-fx-text-fill: #6c757d;");
        
        Label title = new Label("Chấm Công Nhanh");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setStyle("-fx-text-fill: #3b3c58;");
        
        Label subtitle = new Label("Ghi nhận giờ vào/ra ca");
        subtitle.setStyle("-fx-text-fill: #6c757d;");
        
        Button btnIn = new Button("➡️ Vào ca");
        btnIn.getStyleClass().addAll("punch-button", "btn-in-green");
        
        Button btnOut = new Button("⬅️ Ra ca");
        btnOut.getStyleClass().addAll("punch-button", "btn-out-red");

        VBox buttons = new VBox(10, btnIn, btnOut);
        buttons.setAlignment(Pos.CENTER);
        VBox.setVgrow(buttons, Priority.ALWAYS); // Đẩy buttons xuống

        card.getChildren().addAll(timeIcon, title, subtitle, buttons);
        return card;
    }

    private VBox createNhapThuCong() {
        VBox card = new VBox(20);
        card.setPadding(new Insets(30));
        card.getStyleClass().add("punch-card-light"); 
        card.setAlignment(Pos.TOP_CENTER);
        
        Label timeIcon = new Label("🕒"); 
        timeIcon.setFont(Font.font("Segoe UI Emoji", 40)); 
        timeIcon.setStyle("-fx-text-fill: #6c757d;");
        
        Label title = new Label("Nhập thủ công");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setStyle("-fx-text-fill: #3b3c58;");
        
        Button btnManual = new Button("+ Nhập ca làm việc");
        btnManual.getStyleClass().addAll("punch-button", "btn-in-green");
        
        // Dùng Region để căn giữa nút theo chiều dọc
        Region spacer1 = new Region();
        Region spacer2 = new Region();
        VBox.setVgrow(spacer1, Priority.ALWAYS);
        VBox.setVgrow(spacer2, Priority.ALWAYS);

        card.getChildren().addAll(timeIcon, title, spacer1, btnManual, spacer2);
        return card;
    }


    // ===== 2. KHU VỰC TỔNG KẾT =====
    private VBox createSummarySection() {
        VBox section = new VBox(15);
        section.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10;");
 
        Label title = new Label("Tổng kết");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        title.setStyle("-fx-text-fill: #3b3c58;");

        HBox cardContainer = new HBox(20);
        cardContainer.setPadding(new Insets(10, 0, 0, 0));
        
        cardContainer.getChildren().addAll(
            createSummaryCard("Thời gian vào làm", "19:00", "summary-card-light"),
            createSummaryCard("Thời gian nghỉ", "20:00", "summary-card-light"),
            createSummaryCard("Tiền cọc đã nhận", "3.600.000 VND", "summary-card-light"),
            createSummaryCard("Tổng tiền", "36.000.000 VND", "summary-card-light")
        );
        
        section.getChildren().addAll(title, cardContainer);
        return section;
    }
    
    private VBox createSummaryCard(String title, String value, String styleClass) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(25));
        card.getStyleClass().addAll("stat-card", "summary-card-no-shadow", styleClass); // Dùng stat-card của bạn
        card.setAlignment(Pos.TOP_CENTER);
        
        Label icon = new Label("🕒");
        icon.setFont(Font.font("Segoe UI Emoji", 30));
        icon.setStyle("-fx-text-fill: #6c757d;");
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        titleLabel.setStyle("-fx-text-fill: #6c757d;");
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        valueLabel.setStyle("-fx-text-fill: #3b3c58;");
        
        // Thêm icon giả lập trong Summary Card
        card.getChildren().addAll(titleLabel, valueLabel);
        HBox.setHgrow(card, Priority.ALWAYS);
        return card;
    }
    
    
   
}