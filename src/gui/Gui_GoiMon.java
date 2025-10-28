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
import javafx.stage.Stage;

public class Gui_GoiMon extends BorderPane {

    public Gui_GoiMon() {
    	
        this.setStyle("-fx-background-color: white;"); 

        HBox mainContent = new HBox();

        VBox phanBenTrai_GoiMon = taoCotCenter();
        Separator separator = new Separator(Orientation.VERTICAL);
        VBox phanBenPhai_DatBan = taoPhanBenPhai();
        HBox.setHgrow(phanBenPhai_DatBan, Priority.ALWAYS); 

        mainContent.getChildren().addAll(phanBenTrai_GoiMon, separator, phanBenPhai_DatBan);
        

        this.setCenter(mainContent);

    }
    

    private VBox taoCotCenter() {
        VBox phanGiuaAll = new VBox();
        phanGiuaAll.setStyle("-fx-background-color: white;"); // Đặt màu nền trắng
        
        // 1. Khu vực, Tìm kiếm, Chú thích
        HBox phanTren = taoPhanTren_GoiMon();
        
        // 2. Danh sách bàn
        VBox phanGiua_Trai = taoPhanGiua();
        VBox.setVgrow(phanGiua_Trai, Priority.ALWAYS); 
        
        phanGiuaAll.getChildren().addAll(phanTren, phanGiua_Trai);
        
        phanGiuaAll.setPrefWidth(500); 
        
        return phanGiuaAll;
    }

    private HBox taoPhanTren_GoiMon() {
        VBox top = new VBox(5);
		top.setPadding(new Insets(10));
		top.setMinWidth(330); 
		
		Label lblChonTang = new Label("Khu vực");
		lblChonTang.getStyleClass().add("fontTieuDeNho");

		HBox nutTang = new HBox(5);
		nutTang.setAlignment(Pos.TOP_LEFT);
		ToggleButton tang1 = new ToggleButton("Tầng 1");
		tang1.setPrefSize(70, 40);
        tang1.setSelected(true);
		ToggleButton tang2 = new ToggleButton("Tầng 2");
		tang2.setPrefSize(70, 40);
		ToggleGroup buttonGroup = new ToggleGroup();
		tang1.setToggleGroup(buttonGroup);
		tang2.setToggleGroup(buttonGroup);
		tang1.getStyleClass().add("nutTang");
		tang2.getStyleClass().add("nutTang");
		nutTang.getChildren().addAll(tang1, tang2);

		Label lblTiemKiem = new Label("Tìm kiếm bàn");
		lblTiemKiem.getStyleClass().add("fontTieuDeNho");
		TextField timKiem = new TextField();
		timKiem.setPromptText("Tìm kiếm bàn số điện thoại khách hàng");
		timKiem.getStyleClass().add("timKiem");
        timKiem.setPrefWidth(180); 
		Button nutTimKiem = new Button("Tìm kiếm");
		nutTimKiem.getStyleClass().add("button-timKiem");
		HBox oTimKiem = new HBox(10, timKiem, nutTimKiem);
		oTimKiem.setAlignment(Pos.CENTER_LEFT);

		// Trạng thái chức vụ (CHÚ THÍCH)
		VBox trangThai = new VBox(2);
		trangThai.setPrefWidth(130); 
		trangThai.setPadding(new Insets(5));
		Label chucVu = new Label("Chú thích");
		chucVu.getStyleClass().add("fontTieuDeNho");

		Label vip = new Label("Bàn VIP");
		vip.setStyle("-fx-text-fill: #ed8936; -fx-font-weight: bold; -fx-font-size: 11;");
		HBox chuThich1 = new HBox(5);
		ImageView iconVIP = new ImageView();
		try {
			iconVIP.setImage(new Image("img/vipicon.png"));
		} catch (Exception e) {}
		iconVIP.setFitWidth(15);
		iconVIP.setFitHeight(15);
		chuThich1.getChildren().addAll(iconVIP, vip);

		Label dangChon = new Label("Đang chọn");
		dangChon.setStyle("-fx-text-fill: gray; -fx-font-weight: bold; -fx-font-size: 11;");
		HBox chuThich2 = new HBox(5);
		Circle dotGray = new Circle(5, Color.web("#BDBDBD"));
		chuThich2.getChildren().addAll(dotGray, dangChon);

		Label dangSuDung = new Label("Đang sử dụng");
		dangSuDung.setStyle("-fx-text-fill: #38A169; -fx-font-weight: bold; -fx-font-size: 11;");
		HBox chuThich4 = new HBox(5);
		Circle dotGreen = new Circle(5, Color.web("#38A169"));
		chuThich4.getChildren().addAll(dotGreen, dangSuDung);

		Label daBan = new Label("Đã đặt bàn");
		daBan.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: 11;");
		HBox chuThich3 = new HBox(5);
		Circle dotRed = new Circle(5, Color.RED);
		chuThich3.getChildren().addAll(dotRed, daBan);

		trangThai.getChildren().addAll(chucVu, chuThich1, chuThich2, chuThich4, chuThich3);

		top.getChildren().addAll(lblChonTang, nutTang, lblTiemKiem, oTimKiem);
		top.setStyle("-fx-border-width: 0 1 0 0; -fx-border-color: #D9D9D9");

		HBox topALL = new HBox(20); 
		topALL.getChildren().addAll(top, trangThai);

		return topALL;
    }
    
   
    private VBox taoPhanGiua() {
		VBox trai = new VBox(10);
		trai.setPrefWidth(500); 
        trai.setPadding(new Insets(10));
        VBox.setVgrow(trai, Priority.ALWAYS);
        trai.setStyle("-fx-background-color: white;");

		Label danhSach = new Label("Danh sách bàn đang sử dụng"); 
		danhSach.getStyleClass().add("fontTieuDeNho");

		GridPane luoiBan = taoLuoiBan(); 
		ScrollPane cuonLuoi = new ScrollPane(luoiBan);
		cuonLuoi.setFitToWidth(true);
		cuonLuoi.setPrefHeight(620);
        VBox.setVgrow(cuonLuoi, Priority.ALWAYS);
		cuonLuoi.getStyleClass().add("scroll-pane");

		trai.getChildren().addAll(danhSach, cuonLuoi);
        
        return trai; 
    }

    private GridPane taoLuoiBan() {
		GridPane grid = new GridPane();
		grid.setHgap(10); 
		grid.setVgap(10); 
		grid.setPadding(new Insets(10)); 
		grid.setStyle("-fx-background-color: white"); 

		String[] tenBan = { "Bàn 1", "Bàn 2", "Bàn 3", "Bàn 6", "Bàn 7", "Bàn 8", "Bàn 11", "Bàn 12", "Bàn 13",
				"Bàn 16", "Bàn 17", "Bàn 18", "Bàn 19", "Bàn 20", "Bàn 21" };
		String tenKhach = "Hồ Vạn Thương";

        boolean[] isVIPList = { 
            true, false, true, 
            false, true, false, 
            false, false, true, 
            true, true, false, 
            true, false, true 
        };

		List<StackPane> danhSachBan = new ArrayList<>(); 

		for (int i = 0; i < tenBan.length; i++) {
            
			StackPane theBanKhung = taoTheBan_GoiMon(tenBan[i], tenKhach, isVIPList[i]);
			
            int hang = i / 3;
			int cot = i % 3;
            
			danhSachBan.add(theBanKhung);

			final int indexBan = i;
            
			theBanKhung.setOnMouseClicked(e -> {
				for (StackPane khung : danhSachBan) {
                    VBox theReset = (VBox) khung.getChildren().get(1); 
					theReset.getStyleClass().remove("theBan-selected");
					if (!theReset.getStyleClass().contains("theBan")) {
						theReset.getStyleClass().add("theBan");
					}
				}
				
				StackPane khungChon = danhSachBan.get(indexBan);
                VBox theChon = (VBox) khungChon.getChildren().get(1);
				theChon.getStyleClass().add("theBan-selected");
			});

			GridPane.setRowIndex(theBanKhung, hang);
			GridPane.setColumnIndex(theBanKhung, cot);
			grid.getChildren().add(theBanKhung);
		}
		return grid;
	}

	private StackPane taoTheBan_GoiMon(String tenBan, String khach, boolean isVIP) { 
        StackPane khung = new StackPane();
		khung.setPrefSize(140, 110);

        // 1. Phần viền (Region)
        Region viền = new Region();
        viền.setPrefSize(10, 110);
        viền.setStyle("-fx-background-color: #38A169; -fx-background-radius: 20;"); // Màu xanh lá

        // 2. Phần nội dung thẻ (VBox)
		VBox the = new VBox(8);
		the.setPrefSize(135, 110);
		the.setPadding(new Insets(10));
        the.getStyleClass().add("theBan");

		Label nhanBan = new Label(tenBan);
		nhanBan.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

		ImageView iconVip = new ImageView();
        if(isVIP) {
            try {
                iconVip.setImage(new Image(getClass().getResourceAsStream("/img/vipicon.png")));
            } catch (Exception e) {}
        }
		iconVip.setFitHeight(16);
		iconVip.setFitWidth(16);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
		HBox topRow = new HBox(nhanBan, spacer, iconVip);
        topRow.setAlignment(Pos.TOP_LEFT);

		Label nhanKhach = new Label(khach);
		nhanKhach.setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-text-fill: black;");

		VBox khachBox = new VBox(nhanKhach); 
		khachBox.setStyle("-fx-background-color: white; -fx-background-radius: 5");
		khachBox.setPrefWidth(120);
        khachBox.setPrefHeight(30);
		khachBox.setAlignment(Pos.CENTER); 

		the.getChildren().addAll(topRow, khachBox);

        // Đặt viền và thẻ vào StackPane
		khung.getChildren().addAll(viền, the);
		StackPane.setAlignment(viền, Pos.CENTER_LEFT);
		StackPane.setMargin(the, new Insets(0, 0, 0, 3));

		return khung;
	}

    private VBox taoPhanBenPhai() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(15));
        vbox.setStyle("-fx-background-color: white;"); // Màu nền trắng
        HBox.setHgrow(vbox, Priority.ALWAYS); 
        vbox.setMinWidth(650); 

        VBox boxMenu = taoMenuMonAn();
        VBox boxMonDaChon = taoMonDaChon();
        VBox.setVgrow(boxMonDaChon, Priority.ALWAYS); 
        HBox boxTongTienMon = taoTongTienMon();
        HBox boxNutBam = taoNutBam();

        vbox.getChildren().addAll(boxMenu, boxMonDaChon, boxTongTienMon, boxNutBam);

        return vbox;
    }
    

    private VBox taoMenuMonAn() {
        VBox vbox = new VBox(10);
        
        Label tieuDe = new Label("Menu món ăn");
        tieuDe.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");
        
        ComboBox<String> comboLoai = new ComboBox<>();
        comboLoai.getItems().addAll("Tất cả", "Món chính", "Tráng miệng", "Đồ uống");
        comboLoai.setValue("Tất cả");
        comboLoai.setPrefWidth(150);
        comboLoai.getStyleClass().add("combo-box-menu"); 

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(600); 
        scrollPane.getStyleClass().add("scroll-pane-menu"); 
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); 

        TilePane tilePane = new TilePane();
        tilePane.setPadding(new Insets(10));
        tilePane.setHgap(15); 
        tilePane.setVgap(15); 
        tilePane.setPrefColumns(4); 

        // Dữ liệu mẫu
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
        
        HBox comboContainer = new HBox(comboLoai);
        comboContainer.setPadding(new Insets(0, 0, 0, 5)); 
        
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
        VBox.setVgrow(table, Priority.ALWAYS); 

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

        Button btnXacNhan = new Button("Gọi món");
        btnXacNhan.setPrefSize(200, 40);
        btnXacNhan.getStyleClass().add("button-checkin");
        btnXacNhan.setStyle("-fx-background-color: #2D3748; -fx-text-fill: white; -fx-font-weight: bold;");


        hbox.getChildren().addAll(spacer, btnQuayLai, btnXacNhan);
        return hbox;
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
}