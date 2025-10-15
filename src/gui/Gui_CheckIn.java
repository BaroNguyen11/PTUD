package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Gui_CheckIn extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Main layout: BorderPane
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f7fafc;");
 
        // Left sidebar: VBox for menu
        VBox leftSidebar = createLeftSidebar();
        root.setLeft(leftSidebar);

        // Top panel: HBox for status and customer info
        HBox topPanel = createTopPanel();
        root.setTop(topPanel);

        // Center: ScrollPane with GridPane for tables
        GridPane tableGrid = createTableGrid();
        ScrollPane centerScroll = new ScrollPane(tableGrid);
        centerScroll.setFitToWidth(true);
        centerScroll.setPadding(new Insets(10));
        root.setCenter(centerScroll);

        // Bottom panel: HBox for reservation details
        HBox bottomPanel = createBottomPanel();
        root.setBottom(bottomPanel);

        // Scene
        Scene scene = new Scene(root, 1400, 900);
        primaryStage.setTitle("Restaurant Check-in App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createLeftSidebar() {
        VBox sidebar = new VBox(8);
        sidebar.setPrefWidth(220);
        sidebar.setPadding(new Insets(20, 10, 20, 10));
        sidebar.setStyle("-fx-background-color: #2c5282;");

        // Logo
        Label logo = new Label("2BT");
        logo.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        logo.setTextFill(Color.WHITE);
        logo.setAlignment(Pos.CENTER);
        logo.setPrefWidth(200);
        logo.setStyle("-fx-background-color: #2a4365; -fx-padding: 15 0;");
        sidebar.getChildren().add(logo);

        // Menu items
        String[] menuTexts = {
            "Quản lý chi nhánh", "Quản lý món ăn", "Quản lý kho hàng",
            "Quản lý nhân viên", "Quản lý khuyến mãi", "Quản lý hóa đơn",
            "Thống kê", "Đăng xuất"
        };
        String[] iconClasses = {
            "\uf015", "\uf2e7", "\uf291", "\uf007", "\uf291", "\uf571",
            "\uf080", "\uf2f5"
        }; // FontAwesome-like, but use Labels for simplicity

        for (int i = 0; i < menuTexts.length; i++) {
            HBox menuItem = new HBox(12);
            menuItem.setAlignment(Pos.CENTER_LEFT);
            menuItem.setPrefWidth(200);
            menuItem.setStyle("-fx-background-color: transparent; -fx-padding: 12 15;");
            menuItem.setOnMouseEntered(e -> menuItem.setStyle("-fx-background-color: #4a90e2;"));
            menuItem.setOnMouseExited(e -> menuItem.setStyle("-fx-background-color: transparent;"));

            Label icon = new Label("● "); // Placeholder for icon
            icon.setTextFill(Color.WHITE);
            Label text = new Label(menuTexts[i]);
            text.setTextFill(Color.WHITE);
            text.setFont(Font.font(12));
            menuItem.getChildren().addAll(icon, text);
            sidebar.getChildren().add(menuItem);
        }

        // Check-in button in sidebar
        Button sidebarCheckIn = new Button("Check-in");
        sidebarCheckIn.setPrefWidth(200);
        sidebarCheckIn.setStyle("-fx-background-color: #9f7aea; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12;");
        sidebarCheckIn.setFont(Font.font(12));
        sidebar.getChildren().add(sidebarCheckIn);

        return sidebar;
    }

    private HBox createTopPanel() {
        HBox top = new HBox(20);
        top.setPadding(new Insets(10));
        top.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e2e8f0; -fx-border-width: 0 0 1 0;");

        // Left: Floor buttons
        VBox floorBox = new VBox(5);
        floorBox.setAlignment(Pos.CENTER);
        Button tang1 = new Button("Tăng 1");
        tang1.setStyle("-fx-background-color: #4299e1; -fx-text-fill: white; -fx-font-weight: bold;");
        Button tang2 = new Button("Tầng 2");
        tang2.setStyle("-fx-background-color: #a0aec0; -fx-text-fill: white;");
        floorBox.getChildren().addAll(tang1, tang2);

        // Center: Status labels
        VBox statusBox = new VBox(2);
        statusBox.setPrefWidth(100);
        Label chucVu = new Label("Chức vụ");
        chucVu.setStyle("-fx-font-weight: bold; -fx-text-fill: #2d3748; -fx-font-size: 11;");
        Label vip = new Label("▲VIP");
        vip.setStyle("-fx-text-fill: #ed8936; -fx-font-weight: bold; -fx-font-size: 11;");
        Label dangChon = new Label("Đang chờ");
        dangChon.setStyle("-fx-text-fill: #e53e3e; -fx-font-weight: bold; -fx-font-size: 11;");
        Label daBan = new Label("Đã bàn");
        daBan.setStyle("-fx-text-fill: #38a169; -fx-font-weight: bold; -fx-font-size: 11;");
        statusBox.getChildren().addAll(chucVu, vip, dangChon, daBan);

        // Right: Customer info
        VBox customerBox = new VBox(8);
        customerBox.setPrefWidth(350);
        Label title = new Label("Thông tin khách hàng");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-text-fill: #2d3748;");
        GridPane fieldsGrid = new GridPane();
        fieldsGrid.setHgap(5);
        fieldsGrid.setVgap(4);

        Label maKHLabel = new Label("Mã khách hàng:");
        TextField maKH = new TextField("KH000001");
        maKH.setEditable(false);
        maKH.setPrefWidth(200);
        fieldsGrid.addRow(0, maKHLabel, maKH);

        Label tenKHLabel = new Label("Tên khách hàng:");
        TextField tenKH = new TextField("Hồ Văn Thông");
        tenKH.setEditable(false);
        tenKH.setPrefWidth(200);
        fieldsGrid.addRow(1, tenKHLabel, tenKH);

        Label sdtLabel = new Label("SĐT:");
        TextField sdt = new TextField("089398872");
        sdt.setEditable(false);
        sdt.setPrefWidth(200);
        fieldsGrid.addRow(2, sdtLabel, sdt);

        Label diemLabel = new Label("Điểm lịch sử:");
        TextField diem = new TextField("1236");
        diem.setEditable(false);
        diem.setPrefWidth(200);
        fieldsGrid.addRow(3, diemLabel, diem);

        customerBox.getChildren().addAll(title, fieldsGrid);

        top.getChildren().addAll(floorBox, statusBox, customerBox);

        return top;
    }

    private GridPane createTableGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));

        // Table data: names, guests, times, colors (blue=available, gray=reserved, red=occupied)
        String[] banNames = {"Bàn 1", "Bàn 2", "Bàn 3", "Bàn 6", "Bàn 7", "Bàn 8", "Bàn 11", "Bàn 12", "Bàn 13",
                             "Bàn 16", "Bàn 17", "Bàn 18", "Bàn 19", "Bàn 20", "Bàn 21"};
        String guestName = "Hồ Văn Thông";
        String timeStr = "(19h 22/10/25)";
        Color[] colors = {Color.web("#4299e1"), Color.web("#a0aec0"), Color.web("#fed7d7"),
                          Color.web("#4299e1"), Color.web("#a0aec0"), Color.web("#fed7d7"),
                          Color.web("#4299e1"), Color.web("#a0aec0"), Color.web("#fed7d7"),
                          Color.web("#4299e1"), Color.web("#a0aec0"), Color.web("#fed7d7"),
                          Color.web("#4299e1"), Color.web("#a0aec0"), Color.web("#fed7d7")};

        for (int i = 0; i < banNames.length; i++) {
            StackPane tableCard = createTableCard(banNames[i], guestName, timeStr, colors[i]);
            int row = i / 3;
            int col = i % 3;
            GridPane.setRowIndex(tableCard, row);
            GridPane.setColumnIndex(tableCard, col);
            grid.getChildren().add(tableCard);
        }

        return grid;
    }

    private StackPane createTableCard(String banName, String guest, String time, Color bgColor) {
        StackPane card = new StackPane();
        card.setPrefSize(140, 90);
        String hexColor = String.format("#%02x%02x%02x",
                (int) (bgColor.getRed() * 255),
                (int) (bgColor.getGreen() * 255),
                (int) (bgColor.getBlue() * 255));
        card.setStyle("-fx-background-color: " + hexColor + "; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);");

        VBox content = new VBox(4);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(8, 12, 8, 12));

        Label banLabel = new Label(banName);
        banLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        banLabel.setTextFill(Color.WHITE);

        Label guestLabel = new Label(guest);
        guestLabel.setFont(Font.font(11));
        guestLabel.setTextFill(Color.WHITE);
        guestLabel.setAlignment(Pos.CENTER);

        Label timeLabel = new Label(time);
        timeLabel.setFont(Font.font(10));
        timeLabel.setTextFill(Color.WHITE);

        content.getChildren().addAll(banLabel, guestLabel, timeLabel);
        card.getChildren().add(content);

        // Yellow indicator circle
        Circle indicator = new Circle(6, Color.YELLOW);
        indicator.setLayoutX(110);
        indicator.setLayoutY(15);
        card.getChildren().add(indicator);

        return card;
    }

    private HBox createBottomPanel() {
        HBox bottom = new HBox(30);
        bottom.setPadding(new Insets(15));
        bottom.setStyle("-fx-background-color: #edf2f7; -fx-border-color: #e2e8f0; -fx-border-width: 1 0 0 0;");

        // Time section
        VBox timeSection = new VBox(5);
        Label gioDenLabel = new Label("Giờ đến:");
        gioDenLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2d3748;");
        HBox timeInput = new HBox(5);
        timeInput.setAlignment(Pos.CENTER);
        Label hour = new Label("19");
        hour.setStyle("-fx-background-color: white; -fx-padding: 5 10; -fx-border-color: #cbd5e0; -fx-border-width: 1;");
        Label clockIcon = new Label("⏰"); // Clock placeholder
        Label date = new Label("20/08/25");
        date.setStyle("-fx-background-color: white; -fx-padding: 5 10; -fx-border-color: #cbd5e0; -fx-border-width: 1;");
        Label calendarIcon = new Label("📅");
        timeInput.getChildren().addAll(hour, clockIcon, date, calendarIcon);
        timeSection.getChildren().addAll(gioDenLabel, timeInput);

        // Quantity section
        VBox qtySection = new VBox(5);
        Label luongNguoiLabel = new Label("Lượng người:");
        luongNguoiLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2d3748;");
        Label qty = new Label("8");
        qty.setStyle("-fx-background-color: white; -fx-padding: 5 10; -fx-border-color: #cbd5e0; -fx-border-width: 1; -fx-font-weight: bold;");
        qtySection.getChildren().addAll(luongNguoiLabel, qty);

        // Table type section
        VBox typeSection = new VBox(5);
        Label kieuBanLabel = new Label("Kiểu bàn:");
        kieuBanLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2d3748;");
        ToggleGroup group = new ToggleGroup();
        RadioButton datTruoc = new RadioButton("Đặt trước");
        datTruoc.setToggleGroup(group);
        datTruoc.setSelected(true);
        RadioButton dungNgay = new RadioButton("Dùng ngay");
        dungNgay.setToggleGroup(group);
        typeSection.getChildren().addAll(kieuBanLabel, datTruoc, dungNgay);

        // Deposit and Check-in
        VBox rightSection = new VBox(10);
        VBox depositSection = new VBox(5);
        Label tienCocLabel = new Label("Tiền cọc:");
        tienCocLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2d3748;");
        Label tienCoc = new Label("450.000đ");
        tienCoc.setStyle("-fx-background-color: white; -fx-padding: 5 10; -fx-border-color: #cbd5e0; -fx-border-width: 1;");
        depositSection.getChildren().addAll(tienCocLabel, tienCoc);

        Button checkInBtn = new Button("Check-in");
        checkInBtn.setPrefSize(120, 40);
        checkInBtn.setStyle("-fx-background-color: #805ad5; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5;");
        rightSection.getChildren().addAll(depositSection, checkInBtn);

        bottom.getChildren().addAll(timeSection, qtySection, typeSection, rightSection);

        return bottom;
    }

    public static void main(String[] args) {
        launch(args);
    }
}