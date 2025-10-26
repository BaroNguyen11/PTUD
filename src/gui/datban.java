package gui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class datban extends Application {

    @Override
    public void start(Stage primaryStage) {

        // Layout chính
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F7FAFC;"); // Màu nền xám nhạt

        // --- Phần Nội dung chính (Center) ---
        // Sử dụng HBox để chia đôi: Cột thông tin (trái) và Cột menu (phải)
        HBox mainContent = new HBox();

        // Cột bên trái (Thông tin KH, Thông tin đặt bàn, Bàn đã chọn)
        VBox phanBenTrai = taoPhanBenTrai();
        
        // Dấu ngăn cách
        Separator separator = new Separator(Orientation.VERTICAL);
        
        // Cột bên phải (Menu món ăn, Món đã chọn)
        VBox phanBenPhai = taoPhanBenPhai();
        HBox.setHgrow(phanBenPhai, Priority.ALWAYS); // Cho phép cột menu co giãn

        mainContent.getChildren().addAll(phanBenTrai, separator, phanBenPhai);
        
        // Gắn nội dung vào root
        root.setCenter(mainContent);

        // Tạo scene và hiển thị
        BorderPane rootGoc = new BorderPane();
        
        // YÊU CẦU 1: Lấy sidebar từ Gui_HuyBan.java
        HBox sidebar = new HBox();
        sidebar.setPrefWidth(250);
        sidebar.setStyle("-fx-background-color: black"); 
        
        rootGoc.setLeft(sidebar);
        rootGoc.setCenter(root);

        Scene scene = new Scene(rootGoc, 1400, 800); 
        
        	scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
        

        primaryStage.setTitle("2BT-Restaurant - Đặt bàn");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMaximized(true);
    }
    
    private VBox taoPhanBenTrai() {
        VBox vbox = new VBox(0);
        vbox.setPadding(Insets.EMPTY); 
        vbox.setPrefWidth(500); // Cố định chiều rộng
        vbox.setStyle("-fx-background-color: white;");

        VBox boxThongTin = taoThongTinKhachVaDat();

        VBox boxBanDaChon = taoBanDaChon();
        HBox boxTongCoc = taoTongCoc();

        VBox bottomBox = new VBox(15);
        bottomBox.setPadding(new Insets(15, 20, 15, 20)); 
        bottomBox.getChildren().addAll(boxBanDaChon, boxTongCoc);

        vbox.getChildren().addAll(boxThongTin, bottomBox);
        return vbox;
    }
    
    private VBox taoThongTinKhachVaDat() {
    	Region spacer1 = new Region();
		HBox.setHgrow(spacer1, Priority.ALWAYS);
		Region spacer2 = new Region();
		HBox.setHgrow(spacer2, Priority.ALWAYS);
		Region spacer3 = new Region();
		HBox.setHgrow(spacer3, Priority.ALWAYS);
		Region spacer4 = new Region();
		HBox.setHgrow(spacer4, Priority.ALWAYS);
		Region spacer5 = new Region();
		HBox.setHgrow(spacer5, Priority.ALWAYS);
		Region spacer6 = new Region();
		HBox.setHgrow(spacer6, Priority.ALWAYS);
		Region spacer8 = new Region();
		HBox.setHgrow(spacer8, Priority.ALWAYS);
		Region spacer7 = new Region();
		HBox.setHgrow(spacer7, Priority.ALWAYS);
		Region spacer9 = new Region();
		HBox.setHgrow(spacer9, Priority.ALWAYS);
		Region spacer10 = new Region();
		HBox.setHgrow(spacer10, Priority.ALWAYS);

		// Tiêu đề - 1
		Label tieuDeKH = new Label("Thông Tin Khách Hàng");
		tieuDeKH.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");

		// Ma kh
		Label lblMaKH = new Label("Mã khách hàng:");
		lblMaKH.getStyleClass().add("fontTieuDeNho");
		TextField txtMaKh = new TextField("KH000001");
		txtMaKh.setEditable(false);
		txtMaKh.setPrefWidth(250);
		txtMaKh.setStyle(
				"-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 12; -fx-background-color: #D9D9D9; -fx-font-size: 15");
		HBox hbox1 = new HBox(lblMaKH, spacer1, txtMaKh);
		hbox1.setPadding(new Insets(5));

		// Ten kh
		Label lblTenKH = new Label("Tên khách hàng:");
		lblTenKH.getStyleClass().add("fontTieuDeNho");
		TextField txtTenKH = new TextField();
		txtTenKH.setText("Hồ Vạn Thương");
		txtTenKH.setEditable(false);
		txtTenKH.setPrefWidth(250);
		txtTenKH.setPrefWidth(250);
		txtTenKH.setStyle(
				"-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #D9D9D9; -fx-font-size: 15");
		HBox hbox2 = new HBox(lblTenKH, spacer2, txtTenKH);
		hbox2.setPadding(new Insets(5));

		// So dien Thoai
		Label lblSdt = new Label("Số điện thoại:");
		lblSdt.getStyleClass().add("fontTieuDeNho");
		TextField txtSdt = new TextField("0839298272");
		txtSdt.setPrefWidth(250);
		txtSdt.setEditable(false);
		txtSdt.setStyle(
				"-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #D9D9D9; -fx-font-size: 15");
		HBox hbox3 = new HBox(lblSdt, spacer3, txtSdt);
		hbox3.setPadding(new Insets(5));

		// Diem tich luy
		Label lblDiem = new Label("Điểm tích lũy:");
		lblDiem.getStyleClass().add("fontTieuDeNho");
		TextField txtDiem = new TextField("1236");
		txtDiem.setPrefWidth(250);
		txtDiem.setEditable(false);
		txtDiem.setStyle(
				"-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #D9D9D9; -fx-font-size: 15");
		HBox hbox4 = new HBox(lblDiem, spacer4, txtDiem);
		hbox4.setPadding(new Insets(5));

		/// VBOX ALL - 1
		VBox vboxAll1 = new VBox(5);
		vboxAll1.getChildren().addAll(tieuDeKH, hbox1, hbox2, hbox3, hbox4);

		// Tieu de - 2
		Label lblThongTinDatBan = new Label("Thông Tin Đặt Bàn");
		lblThongTinDatBan.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");

		// Ngay gio den
		Label lblNgayGio = new Label("Ngày giờ đến:");
		lblNgayGio.getStyleClass().add("fontTieuDeNho");
		TextField txtGioDen = new TextField();
		TextField txtNgayDen = new TextField();
		txtGioDen.setEditable(false);
		txtNgayDen.setEditable(false);
		txtGioDen.setText("19:00");
		txtNgayDen.setText("23/10/2025");
		txtGioDen.setPrefWidth(80);
		txtGioDen.setStyle(
				"-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #D9D9D9; -fx-font-size: 15; -fx-background-radius: 3 0 0 3");
		txtNgayDen.setStyle(
				"-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-background-color: #D9D9D9; -fx-font-size: 15; -fx-background-radius: 0 3 3 0");
		txtNgayDen.setPrefWidth(170);
		ImageView iconGio = new ImageView(new Image("/img/clock.png"));
		iconGio.setFitHeight(25);
		iconGio.setFitWidth(25);
		ImageView iconNgay = new ImageView(new Image("/img/calendar.png"));
		iconNgay.setFitHeight(25);
		iconNgay.setFitWidth(25);

		StackPane stackGio = new StackPane(txtGioDen, iconGio);
		StackPane stackNgay = new StackPane(txtNgayDen, iconNgay);
		StackPane.setAlignment(iconGio, Pos.CENTER_RIGHT);
		StackPane.setAlignment(iconNgay, Pos.CENTER_RIGHT);
		StackPane.setMargin(iconNgay, new Insets(0, 4, 0, 0));

		HBox hboxNgayGio = new HBox(stackGio, stackNgay);

		HBox hbox5 = new HBox(lblNgayGio, spacer5, hboxNgayGio);
		hbox5.setPadding(new Insets(5));

		// So nguoi
		Label lblSoNguoi = new Label("Số người:");
		lblSoNguoi.getStyleClass().add("fontTieuDeNho");
		TextField txtSoNguoi = new TextField("4");
		txtSoNguoi.setEditable(false);
		txtSoNguoi.setPrefWidth(250);
		txtSoNguoi.setStyle(
				"-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #D9D9D9; -fx-font-size: 15; ");
		HBox hbox6 = new HBox(lblSoNguoi, spacer6, txtSoNguoi);
		hbox6.setPadding(new Insets(5));

		// Kiểu đặt bàn
		Label lblKieuDatBan = new Label("Kiểu đặt bàn");
		lblKieuDatBan.getStyleClass().add("fontTieuDeNho");
		RadioButton radioDatTruoc = new RadioButton("Đặt trước");
		RadioButton radioDungNgay = new RadioButton("Dùng ngay");
		ToggleGroup radioGroup = new ToggleGroup();
		radioDatTruoc.setToggleGroup(radioGroup);
		radioDungNgay.setToggleGroup(radioGroup);
		radioDatTruoc.getStyleClass().add("radio-button");
		radioDungNgay.getStyleClass().add("radio-button");

		radioDatTruoc.setSelected(true);

		HBox hboxRadio = new HBox(5);
		hboxRadio.setPrefWidth(250);
		Region spaceRadio = new Region();
		HBox.setHgrow(spaceRadio, Priority.ALWAYS);
		hboxRadio.getChildren().addAll(radioDatTruoc, spaceRadio, radioDungNgay);
		HBox hbox7 = new HBox(lblKieuDatBan, spacer7, hboxRadio);
		hbox7.setPadding(new Insets(5));

		// Vbox all - 2
		VBox vboxAll2 = new VBox(5);
		vboxAll2.getChildren().addAll(lblThongTinDatBan, hbox5, hbox6, hbox7);
        
        // Vbox All (Chỉ chứa 2 mục theo yêu cầu)
		VBox vboxALL = new VBox(25);
		vboxALL.getChildren().addAll(vboxAll1, vboxAll2); // CHỈ THÊM vboxAll1 và vboxAll2
		vboxALL.setAlignment(Pos.TOP_LEFT);
		vboxALL.setPadding(new Insets(10, 20, 0, 20)); // Giữ nguyên padding gốc
		vboxALL.setMinWidth(500);

		return vboxALL;
    }

    private VBox taoBanDaChon() {
        VBox vbox = new VBox(5); // Giảm khoảng cách giữa tiêu đề và bảng
        Label tieuDe = new Label("Bàn đã chọn");
        // Làm tiêu đề đậm và nét hơn
        tieuDe.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");

        TableView<BanChon> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(200); // Tăng chiều cao để hiển thị các hàng trống
        
        // Bỏ vòng lặp focus màu xanh khi click vào bảng
        table.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");

        TableColumn<BanChon, String> colMaBan = new TableColumn<>("Mã bàn");
        colMaBan.setCellValueFactory(new PropertyValueFactory<>("maBan"));
        colMaBan.setPrefWidth(90); // Điều chỉnh chiều rộng
        
        TableColumn<BanChon, String> colLoai = new TableColumn<>("Loại");
        colLoai.setCellValueFactory(new PropertyValueFactory<>("loai"));
        colLoai.setPrefWidth(110); // Điều chỉnh chiều rộng

        TableColumn<BanChon, Integer> colSoNguoi = new TableColumn<>("Số người");
        colSoNguoi.setCellValueFactory(new PropertyValueFactory<>("soNguoi"));
        colSoNguoi.setPrefWidth(80); // Điều chỉnh chiều rộng

        TableColumn<BanChon, String> colCoc = new TableColumn<>("Cọc");
        colCoc.setCellValueFactory(new PropertyValueFactory<>("coc"));
        colCoc.setPrefWidth(150); // Điều chỉnh chiều rộng
        
        colMaBan.setSortable(false);
        colLoai.setSortable(false);
        colSoNguoi.setSortable(false);
        colCoc.setSortable(false);

        table.getColumns().addAll(colMaBan, colLoai, colSoNguoi, colCoc);
        
        // Dữ liệu mẫu
        ObservableList<BanChon> data = FXCollections.observableArrayList(
            new BanChon("Bàn 4", "VIP", 8, "450.000đ"),
            new BanChon("Bàn 5", "VIP", 8, "450.000đ"),
            new BanChon("Bàn 9", "THƯỜNG", 6, "300.000đ")
        );
        table.setItems(data);

        vbox.getChildren().addAll(tieuDe, table);
        return vbox;
    }

    private HBox taoTongCoc() {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        Label lblTieuDe = new Label("Tổng cọc cần thanh toán:");
        lblTieuDe.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblGia = new Label("1.200.000 VNĐ");
        lblGia.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #E53E3E;"); // Màu đỏ

        hbox.getChildren().addAll(lblTieuDe, spacer, lblGia);
        return hbox;
    }

    private VBox taoPhanBenPhai() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(15));
        vbox.setStyle("-fx-background-color: white;");
        HBox.setHgrow(vbox, Priority.ALWAYS); // Cho phép VBox này co giãn

        // Phần menu
        VBox boxMenu = taoMenuMonAn();
        
        // Phần món đã chọn
        VBox boxMonDaChon = taoMonDaChon();
        VBox.setVgrow(boxMonDaChon, Priority.ALWAYS); // Cho phép bảng này chiếm không gian

        // Phần tổng tiền món
        HBox boxTongTienMon = taoTongTienMon();
        
        // Phần nút bấm
        HBox boxNutBam = taoNutBam();

        vbox.getChildren().addAll(boxMenu, boxMonDaChon, boxTongTienMon, boxNutBam);

        return vbox;
    }
    
    private VBox taoMenuMonAn() {
        VBox vbox = new VBox(10);
        
        // Tiêu đề
        Label tieuDe = new Label("Menu món ăn");
        tieuDe.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");
        
        // ComboBox loại (Nằm dưới tiêu đề)
        ComboBox<String> comboLoai = new ComboBox<>();
        comboLoai.getItems().addAll("Tất cả", "Món chính", "Tráng miệng", "Đồ uống");
        comboLoai.setValue("Tất cả");
        comboLoai.setPrefWidth(150);
        comboLoai.getStyleClass().add("combo-box-menu"); 

        // ScrollPane chứa các món ăn
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(600); 
        scrollPane.getStyleClass().add("scroll-pane-menu"); 
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); 

        // Dùng TilePane để tự động xếp các món
        TilePane tilePane = new TilePane();
        tilePane.setPadding(new Insets(10));
        
        // <<< FIX: Giảm khoảng cách để 4 cột có thể vừa
        tilePane.setHgap(15); 
        tilePane.setVgap(15); 
        
        tilePane.setPrefColumns(4); // Đã có 4 cột

        // Thêm món ăn mẫu (dùng cùng 1 ảnh theo yêu cầu)
        tilePane.getChildren().add(taoTheMonAn("/img/monAn.png", "Samosa", "Giòn tan khó cưỡng", "36.000đ", 2));
        tilePane.getChildren().add(taoTheMonAn("/img/monAn.png", "Cơm gà chiên tỏi ớt", "Thơm nứt mũi, ăn mê tít", "36.000đ", 2));
        tilePane.getChildren().add(taoTheMonAn("/img/monAn.png", "Pizza", "Phô mai kéo sợi", "136.000đ", 2));
        tilePane.getChildren().add(taoTheMonAn("/img/monAn.png", "Burger", "Burger phô mai béo ngậy", "36.000đ", 2));
        tilePane.getChildren().add(taoTheMonAn("/img/monAn.png", "Samosa", "Giòn tan khó cưỡng", "36.000đ", 0));
        tilePane.getChildren().add(taoTheMonAn("/img/monAn.png", "Samosa", "Giòn tan khó cưỡng", "36.000đ", 0));
        tilePane.getChildren().add(taoTheMonAn("/img/monAn.png", "Samosa", "Giòn tan khó cưỡng", "36.000đ", 0));
        tilePane.getChildren().add(taoTheMonAn("/img/monAn.png", "Samosa", "Giòn tan khó cưỡng", "36.000đ", 0));
        tilePane.getChildren().add(taoTheMonAn("/img/monAn.png", "Samosa", "", "36.000đ", 0));
        tilePane.getChildren().add(taoTheMonAn("/img/monAn.png", "Samosa", "", "36.000đ", 0));
        tilePane.getChildren().add(taoTheMonAn("/img/monAn.png", "Samosa", "", "36.000đ", 0));
        tilePane.getChildren().add(taoTheMonAn("/img/monAn.png", "Samosa", "", "36.000đ", 0));
        
        scrollPane.setContent(tilePane);
        
        // Đặt ComboBox trong HBox để căn lề trái
        HBox comboContainer = new HBox(comboLoai);
        comboContainer.setPadding(new Insets(0, 0, 0, 5)); // Thêm lề trái nhỏ
        
        vbox.getChildren().addAll(tieuDe, comboContainer, scrollPane);
        return vbox;
    }
    

    private VBox taoTheMonAn(String imgPath, String tenMon, String moTa, String gia, int soLuong) {
        VBox vbox = new VBox(5);
        
        vbox.setPrefWidth(155); 
        
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));
        vbox.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);"
        );

        ImageView imgView = new ImageView();
        try {
            imgView.setImage(new Image(imgPath)); 
            imgView.setFitWidth(130);
            imgView.setFitHeight(110);
            imgView.setPreserveRatio(true);
            
            vbox.getChildren().add(imgView); 
            
        } catch (Exception e) {
            Region imgPlaceholder = new Region();
            imgPlaceholder.setPrefSize(130, 110);
            imgPlaceholder.setStyle("-fx-background-color: #CBD5E0; -fx-background-radius: 5;");
            vbox.getChildren().add(imgPlaceholder);
            System.out.println("Không tìm thấy ảnh: " + imgPath);
        }

        Label lblTen = new Label(tenMon);
        lblTen.setWrapText(true);
        lblTen.setStyle("-fx-font-weight: 900; -fx-font-size: 15px; -fx-font-family: 'Times New Roman'; -fx-alignment: CENTER;");
        
        lblTen.setPrefWidth(140); 
        lblTen.setMinHeight(35); 
        
        Label lblMoTa = new Label(moTa);
        lblMoTa.setWrapText(true);
        lblMoTa.setStyle("-fx-text-fill: #718096; -fx-font-size: 12px; -fx-alignment: CENTER;");
        
        lblMoTa.setPrefWidth(140); 
        lblMoTa.setMinHeight(30); 
        
        if (moTa == null || moTa.isEmpty()) {
            lblMoTa.setVisible(false);
        }
        
        Label lblGia = new Label(gia);
        lblGia.setStyle("-fx-text-fill: #E53E3E; -fx-font-weight: bold; -fx-font-size: 18px; -fx-alignment: CENTER;");
        
        lblGia.setPrefWidth(140); 

        Button btnTru = new Button("−"); 
        btnTru.getStyleClass().add("button-dieu-chinh-menu");
        
        Label lblSoLuong = new Label(String.valueOf(soLuong));
        lblSoLuong.setPadding(new Insets(0, 10, 0, 10)); 
        lblSoLuong.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
        
        Button btnCong = new Button("+");
        btnCong.getStyleClass().add("button-dieu-chinh-menu");
        
        HBox soLuongBox = new HBox(10, btnCong, lblSoLuong, btnTru); 
        soLuongBox.setAlignment(Pos.CENTER);
        soLuongBox.setPadding(new Insets(5, 0, 0, 0)); 

        vbox.getChildren().addAll(lblTen, lblMoTa, lblGia, soLuongBox);
        return vbox;
    }
    
    private VBox taoMonDaChon() {
        VBox vbox = new VBox(10);
        Label tieuDe = new Label("Món đã chọn");
        tieuDe.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");

        TableView<MonChon> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS); // Cho phép bảng co giãn

        TableColumn<MonChon, String> colTen = new TableColumn<>("Tên món");
        colTen.setCellValueFactory(new PropertyValueFactory<>("tenMon"));
        colTen.setPrefWidth(200);

        TableColumn<MonChon, Integer> colSoLuong = new TableColumn<>("Số lượng");
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        colSoLuong.setPrefWidth(80);

        TableColumn<MonChon, String> colDonGia = new TableColumn<>("Đơn giá");
        colDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
        colDonGia.setPrefWidth(120);

        TableColumn<MonChon, String> colTong = new TableColumn<>("Tổng");
        colTong.setCellValueFactory(new PropertyValueFactory<>("tong"));
        colTong.setPrefWidth(120);

        table.getColumns().addAll(colTen, colSoLuong, colDonGia, colTong);

        // Dữ liệu mẫu
        ObservableList<MonChon> data = FXCollections.observableArrayList(
            new MonChon("Samosa", 2, "69.000đ", "138.000đ"),
            new MonChon("Cơm gà chiên tỏi ớt", 2, "39.000đ", "78.000đ"),
            new MonChon("Pizza", 2, "169.000đ", "338.000đ"),
            new MonChon("Burger", 2, "89.000đ", "178.000đ")
        );
        table.setItems(data);

        vbox.getChildren().addAll(tieuDe, table);
        return vbox;
    }

    private HBox taoTongTienMon() {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        Label lblTieuDe = new Label("Tổng tiền đặt món:");
        lblTieuDe.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblGia = new Label("732.000 VNĐ");
        lblGia.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2D3748;"); 

        hbox.getChildren().addAll(lblTieuDe, spacer, lblGia);
        return hbox;
    }

    private HBox taoNutBam() {
        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnQuayLai = new Button("Quay lại");
        btnQuayLai.setPrefSize(150, 40);
        btnQuayLai.getStyleClass().add("button-checkin");
        btnQuayLai.setStyle("-fx-background-color: #A0AEC0;" + 
                "-fx-font-weight: bold;" + 
                "-fx-font-size: 20;" + 
                "-fx-text-fill: gray;");

        Button btnXacNhan = new Button("Xác nhận đặt bàn");
        btnXacNhan.setPrefSize(200, 40);
        btnXacNhan.getStyleClass().add("button-checkin");
        btnXacNhan.setStyle("-fx-background-color: #2D3748; -fx-text-fill: white; -fx-font-weight: bold;");


        hbox.getChildren().addAll(spacer, btnQuayLai, btnXacNhan);
        return hbox;
    }

    public static class BanChon {
        private String maBan;
        private String loai;
        private int soNguoi;
        private String coc;

        public BanChon(String maBan, String loai, int soNguoi, String coc) {
            this.maBan = maBan;
            this.loai = loai;
            this.soNguoi = soNguoi;
            this.coc = coc;
        }

        public String getMaBan() { return maBan; }
        public String getLoai() { return loai; }
        public int getSoNguoi() { return soNguoi; }
        public String getCoc() { return coc; }
    }

    public static class MonChon {
        private String tenMon;
        private int soLuong;
        private String donGia;
        private String tong;

        public MonChon(String tenMon, int soLuong, String donGia, String tong) {
            this.tenMon = tenMon;
            this.soLuong = soLuong;
            this.donGia = donGia;
            this.tong = tong;
        }

        public String getTenMon() { return tenMon; }
        public int getSoLuong() { return soLuong; }
        public String getDonGia() { return donGia; }
        public String getTong() { return tong; }
    }


    public static void main(String[] args) {
        launch(args);
    }
}