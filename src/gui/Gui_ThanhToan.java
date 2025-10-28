package gui;



import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import control.Crl_ThanhToan;
import entity.BanAn;
import entity.LoaiBan;
import entity.MonAn;
import entity.TrangThai;
import entity.ViTri;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Gui_ThanhToan extends BorderPane {
	private Crl_ThanhToan control = new Crl_ThanhToan();
    private List<BanAn> dsBan = new ArrayList<>();
    private Scene scene;
	private ScrollPane scroll;
	private BanAn banChon;

    public Gui_ThanhToan() {
    		
    	 	dsBan = control.layDanhSachBanThanhToan(LocalDate.of(2024, 10, 23));
    	 	banChon = new BanAn();
        // Root chính
        this.setStyle("-fx-background-color: white");

        // Màn hiển thị danh sách bàn
        BorderPane manHienThiBan = taoManHinhDanhSachBan(control.chiaTang(dsBan, "Tầng 1"));
        this.setCenter(manHienThiBan);
        this.getStylesheets().add(getClass().getResource("/css/qlkm.css").toExternalForm());
        this.getStylesheets().add(getClass().getResource("/css/thanhtoan.css").toExternalForm());

    }

    // Tạo màn danh sách bàn
    public BorderPane taoManHinhDanhSachBan(List<BanAn> dsBan) {
        BorderPane root = new BorderPane();

        // Phần trên (chọn tầng + tìm kiếm + chú thích)
        HBox hboxPhanTren = taoPhanTren();
        root.setTop(hboxPhanTren);

        // Phần danh sách bàn
        HBox hboxDanhSach = taoPhanDanhSachBan(dsBan);
        root.setCenter(hboxDanhSach);

        return root;
    }

    // Tạo phần trên với nút tầng, tìm kiếm, chú thích
    private HBox taoPhanTren() {
        VBox left = new VBox(5);
        left.setPadding(new Insets(10));

        // --- Nút chọn tầng ---
        Label lblKhuVuc = new Label("Khu vực");
        lblKhuVuc.getStyleClass().add("fontTieuDeNho");

        ToggleButton tang1 = new ToggleButton("Tầng 1");
        ToggleButton tang2 = new ToggleButton("Tầng 2");
        ToggleGroup group = new ToggleGroup();
        tang1.setToggleGroup(group);
        tang2.setToggleGroup(group);
        tang1.getStyleClass().add("nutTang");
        tang2.getStyleClass().add("nutTang");
        HBox nutTang = new HBox(5, tang1, tang2);
        nutTang.setAlignment(Pos.CENTER_LEFT);
        
      //Chức năng loc tầng
        tang1.setOnAction(e ->{
        		scroll.setContent(taoLuoiBan(control.chiaTang(dsBan, "Tầng 1")));
        });
        
        tang2.setOnAction(e ->{
    			scroll.setContent(taoLuoiBan(control.chiaTang(dsBan, "Tầng 2")));
        });
        
        tang1.setSelected(true);

        // --- Ô tìm kiếm ---
        Label lblTimKiem = new Label("Tìm kiếm");
        lblTimKiem.getStyleClass().add("fontTieuDeNho");
        TextField txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Tìm bằng mã bàn");
        txtTimKiem.getStyleClass().add("timKiem");
        Button btnTim = new Button("Tìm");
        btnTim.getStyleClass().add("button-timKiem");
        ComboBox<String> cboLoc = new ComboBox<String>();
		cboLoc.getItems().addAll("Tất cả loại","Bàn vip","Bàn thường");
		cboLoc.getStyleClass().add("combo-box");
		cboLoc.getSelectionModel().selectFirst();
        HBox timKiem = new HBox(10, txtTimKiem, btnTim, cboLoc);
        timKiem.setAlignment(Pos.CENTER_LEFT);
       

        left.getChildren().addAll(lblKhuVuc, nutTang, lblTimKiem, timKiem);
        left.setStyle("-fx-border-width: 0 1 0 0; -fx-border-color: #D9D9D9");

        // --- Chú thích trạng thái ---
        VBox trangThai = new VBox(5);
        trangThai.setPadding(new Insets(10));

        Label lblTrangThai = new Label("Chú thích");
        lblTrangThai.getStyleClass().add("fontTieuDeNho");

        HBox vip = taoChuThich("/img/vipicon.png", "Bàn VIP", "yellow");
        HBox ban = taoChuThich("/img/dotYellow.png", "Đang sử dụng", "yellow");

        trangThai.getChildren().addAll(lblTrangThai, vip, ban);

        HBox topAll = new HBox(40, left, trangThai);
        return topAll;
    }

    private HBox taoChuThich(String iconPath, String text, String color) {
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        icon.setFitWidth(15);
        icon.setFitHeight(15);
        Label lbl = new Label(text);
        lbl.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold; -fx-font-size: 11;");
        HBox box = new HBox(5, icon, lbl);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    // Tạo phần giữa (danh sách bàn)
    private HBox taoPhanDanhSachBan(List<BanAn> dsBanAn) {
        HBox giua = new HBox(20);
        giua.setPadding(new Insets(10));

        VBox trai = new VBox(10);

        Label lblDanhSach = new Label("Danh sách bàn");
        lblDanhSach.getStyleClass().add("fontTieuDeNho");

        GridPane luoiBan = taoLuoiBan(dsBanAn);
        scroll = new ScrollPane(luoiBan);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(620);
        scroll.getStyleClass().add("scroll-pane");

        trai.getChildren().addAll(lblDanhSach, scroll);
//        giua.setAlignment(Pos.CENTER);
        giua.getChildren().add(trai);

        return giua;
    }

    // Lưới bàn
    private GridPane taoLuoiBan(List<BanAn> dsBanAn) {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        grid.setStyle("-fx-background-color: white;");
        
       

        for (int i = 0; i < dsBanAn.size(); i++) {
            StackPane theBan = taoTheBan(dsBanAn.get(i));

            int hang = i / 5;
            int cot = i % 5;
            GridPane.setRowIndex(theBan, hang);
            GridPane.setColumnIndex(theBan, cot);
            grid.getChildren().add(theBan);

        }
        return grid;
    }

    // Tạo thẻ bàn
    private StackPane taoTheBan(BanAn banAn) {
        StackPane khung = new StackPane();
        khung.setMinHeight(100);
        khung.setMinWidth(210);

        Region mauTrai = new Region();
        mauTrai.setPrefWidth(10);
        mauTrai.setStyle("-fx-background-color: yellow; -fx-background-radius: 20;");

        VBox the = new VBox(10);
        the.setPrefSize(210, 130);
        the.setAlignment(Pos.CENTER);
        the.setPadding(new Insets(0, 10, 0, 10));
        the.setStyle("-fx-background-color: #082744; -fx-background-radius: 20;");

        ImageView iconVip = new ImageView(new Image("img/vipicon.png"));
        iconVip.setFitHeight(20);
        iconVip.setFitWidth(20);
        HBox hboxVip = new HBox();
        if(banAn.getLoai().equals(LoaiBan.VIP)) {
        		hboxVip.getChildren().add(iconVip);
        }
        hboxVip.setAlignment(Pos.TOP_RIGHT);
        hboxVip.setMinHeight(20);

        Label lblTen = new Label(banAn.getMaBan());
        lblTen.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        lblTen.setTextFill(Color.WHITE);

        Button btnThanhToan = new Button("Thanh toán");
        btnThanhToan.getStyleClass().add("btn-thanhToan-guiThanhToan");
        btnThanhToan.setOnAction(e ->{
        		this.setCenter(taoManHinhThanhToan(banAn));
//        		banChon = banAn;
        });

        the.getChildren().addAll(hboxVip, lblTen, btnThanhToan);

        khung.getChildren().addAll(mauTrai, the);
        StackPane.setAlignment(mauTrai, Pos.CENTER_LEFT);
        StackPane.setMargin(the, new Insets(0, 0, 0, 5));

        return khung;
    }
    
    private BorderPane taoManHinhThanhToan(BanAn banAn) {
    		//Root all
    		BorderPane rootAll = new BorderPane();
    		
    		//Tạo phần trái
    		VBox vboxPhanTrai = taoPhanTrai(banAn);
    		rootAll.setLeft(vboxPhanTrai);
    		
    		//Tạo phần phải
    		VBox vboxPhanPhai = taoPhanPhai();
    		rootAll.setRight(vboxPhanPhai);
    		
    		return rootAll;
    }
    
    private VBox taoPhanTrai(BanAn banAn) {
    		//VBox
    		VBox vboxAll = new VBox(5);
    		
    		//Spacer
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
    		
    		//Khai báo biến
    		ImageView iconVip = new ImageView(new Image("/img/vipicon.png"));
    		Label lblMaBan = new Label(banAn.getMaBan());
    		HBox hbox1 = new HBox(5);
    		
    		Label lblDanhSach = new Label("Danh sách món sử dụng");
    		List<String> dsChiTietRaw = control.layDanhSachCTHD(banAn.getMaBan());
    		ObservableList<String> dsChiTiet = FXCollections.observableArrayList(dsChiTietRaw);
    		TableView<String> tableMon = new TableView<>(dsChiTiet);  
    		tableMon.setPrefHeight(380);
    		
    		Label lblTamTinh  = new Label("Tạm tính:");
    		TextField txtTamTinh = new TextField();
    		HBox hbox2 = new HBox();
    		Label lblThue = new Label("Thuế VAT:");
    		TextField txtThue = new TextField();
    		HBox hbox3 = new HBox();
    		Label lblGiamGia = new Label("Giảm giá:");
    		TextField txtGiamGia = new TextField();
    		HBox hbox4 = new HBox();
    		Label lblTienCoc = new Label("Tiền đã cọc:");
    		TextField txtTienCoc = new TextField();
    		HBox hbox5 = new HBox();
    		Label lblTongTien = new Label("Tổng tiền:");
    		TextField txtTongTien = new TextField();
    		HBox hbox6 = new HBox();
    		
    		///
    		iconVip.setFitHeight(30);
    		iconVip.setFitWidth(30);
    		lblMaBan.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
    		hbox1.getChildren().addAll(iconVip, lblMaBan);
    		
    		lblDanhSach.setStyle("-fx-font-size: 20px; -fx-font-weight: bold");
    		
    		// Cột STT
        TableColumn<String, Void> colSTT = new TableColumn<>("STT");
        colSTT.setPrefWidth(40);
        colSTT.setSortable(false);
        colSTT.setCellFactory(col -> new TableCell<String, Void>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
                setAlignment(Pos.CENTER);
            }
        });
        
        
        
     // Cột tên món
        TableColumn<String, String> colTenMon = new TableColumn<>("Tên món");

        // Gán dữ liệu từ thuộc tính "tenMonAn" trong class MonAn
        colTenMon.setCellValueFactory(cellData -> {
        		String tenMon = cellData.getValue().split(",")[0];
        		return new SimpleStringProperty(tenMon);
        });

        colTenMon.setCellFactory(tc -> new TableCell<String, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle(""); 
                } else {
                    setText(item);
                    setAlignment(Pos.CENTER_LEFT); 
                }
            }
        });

        colTenMon.setPrefWidth(250);

        // Cột số lượng
        TableColumn<String, Integer> colSoLuong = new TableColumn<>("Số lượng");

        colSoLuong.setCellValueFactory(cellData -> {
        		int soLuong = Integer.parseInt(cellData.getValue().split(",")[1]);
        		return new SimpleIntegerProperty(soLuong).asObject();
        });

        colSoLuong.setCellFactory(tc -> new TableCell<String, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle(""); 
                } else {
                    setText(String.valueOf(item)); 
                    setAlignment(Pos.CENTER);
                    setStyle("-fx-font-size: 13px;"); 
                }
            }
        });

        colSoLuong.setPrefWidth(100);


        // Cột giá
        TableColumn<String, Double> colGia = new TableColumn<>("Giá");
        colGia.setCellValueFactory(cellData -> {
        		double giaTien = Double.parseDouble(cellData.getValue().split(",")[2]);
        		return new SimpleDoubleProperty(giaTien).asObject();
        });
        colGia.setPrefWidth(150);
        colGia.setCellFactory(tc -> new TableCell<String, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("%,.0fđ", item));
                setAlignment(Pos.CENTER);
            }
            
        });

        // Cột tổng tiền
        TableColumn<String, Double> colTong = new TableColumn<>("Tổng tiền");
        colTong.setPrefWidth(150);
        colTong.setCellValueFactory(cellData -> {
	    		double tongTien = Double.parseDouble(cellData.getValue().split(",")[3]);
	    		return new SimpleDoubleProperty(tongTien).asObject();
        });
        colTong.setCellFactory(tc -> new TableCell<String, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                {
                		setPadding(new Insets(2, 5, 2, 5));
                }
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    Label lbl = new Label(String.format("%,.0fđ", item));
                    lbl.setStyle("""
                        -fx-background-radius: 3;
                        -fx-font-weight: bold;
                        -fx-text-fill: black;
                        -fx-font-size: 13;
                    """);
                    HBox hboxTongTien = new HBox();
                    hboxTongTien.setStyle("-fx-background-color: #D7F7D3;");
                    hboxTongTien.getChildren().add(lbl);
                    hboxTongTien.setAlignment(Pos.CENTER);
                    setGraphic(hboxTongTien);
                    setText(null);
                    
                }
            }
        });

        tableMon.getColumns().addAll(colSTT, colTenMon, colSoLuong, colGia, colTong);
    		
        
        ///
        lblTamTinh.getStyleClass().add("font-lblThanhToan");
        lblThue.getStyleClass().add("font-lblThanhToan");
        lblGiamGia.getStyleClass().add("font-lblThanhToan");
        lblTienCoc.getStyleClass().add("font-lblThanhToan");
        
        txtTamTinh.getStyleClass().add("font-txtThanhToan");
        txtThue.getStyleClass().add("font-txtThanhToan");
        txtGiamGia.getStyleClass().add("font-txtThanhToan");
        txtTienCoc.getStyleClass().add("font-txtThanhToan");
        txtTamTinh.setAlignment(Pos.CENTER_RIGHT);
        txtThue.setAlignment(Pos.CENTER_RIGHT);
        txtGiamGia.setAlignment(Pos.CENTER_RIGHT);
        txtTienCoc.setAlignment(Pos.CENTER_RIGHT);
        
        DecimalFormat format = new DecimalFormat("#,### VND");
        txtTamTinh.setText(format.format(100000000));
        txtThue.setText(format.format(100000000));
        txtGiamGia.setText(format.format(100000000));
        txtTienCoc.setText(format.format(100000000));
        txtTongTien.setText(format.format(100000000));
        txtTamTinh.setEditable(false);
        txtThue.setEditable(false);
        txtGiamGia.setEditable(false);
        txtTienCoc.setEditable(false);
        txtTongTien.setEditable(false);
    		
        lblTongTien.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: white");
        txtTongTien.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: transparent");
        txtTongTien.setAlignment(Pos.CENTER_RIGHT);
        
        hbox2.getChildren().addAll(lblTamTinh, spacer1, txtTamTinh);
        hbox3.getChildren().addAll(lblThue, spacer2, txtThue);
        hbox4.getChildren().addAll(lblGiamGia, spacer3, txtGiamGia);
        hbox5.getChildren().addAll(lblTienCoc, spacer4, txtTienCoc);
        hbox6.getChildren().addAll(lblTongTien, spacer5, txtTongTien);
        hbox6.setAlignment(Pos.CENTER);
        hbox6.setStyle("-fx-background-color: #53687C");
        
        hbox2.setPadding(new Insets(0, 5, 0, 5));
        hbox3.setPadding(new Insets(0, 5, 0, 5));
        hbox4.setPadding(new Insets(0, 5, 0, 5));
        hbox5.setPadding(new Insets(0, 5, 0, 5));
        hbox6.setPadding(new Insets(0, 5, 0, 5));
        
        //
//        addMonAnTestData(tableMon.getItems());
        
        //
        vboxAll.setPrefHeight(500);
        vboxAll.getChildren().addAll(hbox1, lblDanhSach, tableMon, hbox2, hbox3, hbox4, hbox5, hbox6);
        vboxAll.setPadding(new Insets(20, 10, 20, 10));
        vboxAll.setStyle("-fx-border-width: 0 1 0 0; -fx-border-color: #908C8C");
        
    		return vboxAll;
    }
    
    private VBox taoPhanPhai() {
    		//VBox all
    		VBox vboxAll = new VBox(5);
    		
    		//Khai báo biến
    		Label lblPhuongThuc = new Label("Phương thức thanh toán");
    		ToggleButton btnTienMat = new ToggleButton("Tiền mặt");
    		ToggleButton btnMa = new ToggleButton("Mã QR");
    		ToggleGroup btnGroup = new ToggleGroup();
    		HBox hboxGroup = new HBox(10);
    		btnTienMat.setToggleGroup(btnGroup);
    		btnMa.setToggleGroup(btnGroup);
    		Label lblTienNhan = new Label("Tiền nhận:");
    		TextField txtTienNhan = new TextField();
    		Label lblTienThua = new Label("Tiền thừa:");
    		TextField txtTienThua = new TextField();
    		Label lblNhapNhanh = new Label("Nhập nhanh");
    		
    		
    		Button btnNhapNhanh1 = new  Button("Số 1");
    		Button btnNhapNhanh2 = new  Button("Số 2");
    		Button btnNhapNhanh3 = new  Button("Số 3");
    		Button btnNhapNhanh4 = new  Button("Số 4");
    		Button btnNhapNhanh5 = new  Button("Số 5");
    		Button btnNhapNhanh6 = new  Button("Số 6");
    		
    		GridPane gridNhapNhanh = new GridPane();
    		
    		HBox hboxPhimNhap = new HBox(5);
    		
    		GridPane gridPhim = new GridPane();
    		
    		Button btnPhim1 = new Button("1");
    		Button btnPhim2 = new Button("2");
    		Button btnPhim3 = new Button("3");
    		Button btnPhim4 = new Button("4");
    		Button btnPhim5 = new Button("5");
    		Button btnPhim6 = new Button("6");
    		Button btnPhim7 = new Button("7");
    		Button btnPhim8 = new Button("8");
    		Button btnPhim9 = new Button("9");
    		Button btnPhim0 = new Button("0");
    		Button btnPhimC = new Button("C");
    		Button btnPhimCham = new Button(".");
    		Button btnPhim00 = new Button("00");
    		Button btnPhim000 = new Button("000");
    		Button btnPhimCham000 = new Button(".000");
    		Button btnPhimXoaMot = new Button();
    		ImageView iconXoa = new ImageView(new Image("/img/iconDelete.png"));
    		
    		VBox vboxTien = new VBox(10);
    		Button btn50 = new Button("50.000 VND");
    		Button btn100 = new Button("100.000 VND");
    		Button btn200 = new Button("200.000 VND");
    		Button btn500 = new Button("500.000 VND");
    		
    		Button btnThanhToan = new Button("Thanh toán");
    		Button btnQuayLai = new Button("Quay lại");
    		HBox hboxButton = new HBox();
    		
    		ImageView maQR = new ImageView(new Image("/img/qr.png"));
    		Label lblTongTienQR = new Label("Tổng tiền....");
    		
    		HBox hboxTienNhan = new HBox();
    		HBox hboxTienThua = new HBox();
    		
    		//Cài đặt giao diện
    		lblPhuongThuc.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");
    		
    		btnTienMat.getStyleClass().add("toggle-thanhToan");
    		btnMa.getStyleClass().add("toggle-thanhToan");
    		hboxGroup.getChildren().addAll(btnTienMat, btnMa);
    		btnTienMat.setSelected(true);
    		// chặn bỏ chọn hết
    		btnGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
    		    if (newToggle == null) {
    		        // nếu người dùng click lại để bỏ chọn, ta khôi phục cái cũ
    		        oldToggle.setSelected(true);
    		    }
    		});
    		
    		lblTienNhan.setStyle("-fx-font-size: 15px; -fx-font-weight: bold");
    		lblTienThua.setStyle("-fx-font-size: 15px; -fx-font-weight: bold");
    		
    		txtTienNhan.setStyle("-fx-background-color: white; -fx-border-width: 1; -fx-border-radius: 5; -fx-border-color: #908C8C; -fx-font-size: 15px");
    		txtTienThua.setStyle("-fx-background-color: white; -fx-border-width: 1; -fx-border-radius: 5; -fx-border-color: #908C8C; -fx-font-size: 15px");
    		txtTienNhan.setPrefWidth(300);
    		txtTienThua.setPrefWidth(300);
    		txtTienThua.setEditable(false);
    		Region spacer1 = new Region();
    		Region spacer2 = new Region();
    		HBox.setHgrow(spacer1, Priority.ALWAYS);
    		HBox.setHgrow(spacer2, Priority.ALWAYS);
    		    		
    		hboxTienNhan.getChildren().addAll(lblTienNhan, spacer1,  txtTienNhan);
    		hboxTienThua.getChildren().addAll(lblTienThua, spacer2, txtTienThua);
    		
    		lblNhapNhanh.setStyle("-fx-font-size: 15px; -fx-text-fill: #908C8C");
    		
    		gridNhapNhanh.setHgap(10);
    		gridNhapNhanh.setVgap(10);
    		gridNhapNhanh.setPadding(new Insets(10));
    		
    		btnNhapNhanh1.setStyle("-fx-background-color: white; -fx-border-width: 1; -fx-border-radius: 3; -fx-border-color: #908C8C; -fx-font-size: 15px; -fx-font-weight: bold; -fx-cursor: hand");
    		btnNhapNhanh2.setStyle("-fx-background-color: white; -fx-border-width: 1; -fx-border-radius: 3; -fx-border-color: #908C8C; -fx-font-size: 15px; -fx-font-weight: bold; -fx-cursor: hand");
    		btnNhapNhanh3.setStyle("-fx-background-color: white; -fx-border-width: 1; -fx-border-radius: 3; -fx-border-color: #908C8C; -fx-font-size: 15px; -fx-font-weight: bold; -fx-cursor: hand");
    		btnNhapNhanh4.setStyle("-fx-background-color: white; -fx-border-width: 1; -fx-border-radius: 3; -fx-border-color: #908C8C; -fx-font-size: 15px; -fx-font-weight: bold; -fx-cursor: hand");
    		btnNhapNhanh5.setStyle("-fx-background-color: white; -fx-border-width: 1; -fx-border-radius: 3; -fx-border-color: #908C8C; -fx-font-size: 15px; -fx-font-weight: bold; -fx-cursor: hand");
    		btnNhapNhanh6.setStyle("-fx-background-color: white; -fx-border-width: 1; -fx-border-radius: 3; -fx-border-color: #908C8C; -fx-font-size: 15px; -fx-font-weight: bold; -fx-cursor: hand");
    		
    		btnNhapNhanh1.setPrefHeight(40);
    		btnNhapNhanh1.setPrefWidth(150);
    		
    		btnNhapNhanh2.setPrefHeight(40);
    		btnNhapNhanh2.setPrefWidth(150);
    		
    		btnNhapNhanh3.setPrefHeight(40);
    		btnNhapNhanh3.setPrefWidth(150);
    		
    		btnNhapNhanh4.setPrefHeight(40);
    		btnNhapNhanh4.setPrefWidth(150);
    		
    		btnNhapNhanh5.setPrefHeight(40);
    		btnNhapNhanh5.setPrefWidth(150);
    		
    		btnNhapNhanh6.setPrefHeight(40);
    		btnNhapNhanh6.setPrefWidth(150);
    		
    		gridNhapNhanh.add(btnNhapNhanh1, 0, 0);
    		gridNhapNhanh.add(btnNhapNhanh2, 1, 0);
    		gridNhapNhanh.add(btnNhapNhanh3, 2, 0);
    		gridNhapNhanh.add(btnNhapNhanh4, 0, 1);
    		gridNhapNhanh.add(btnNhapNhanh5, 1, 1);
    		gridNhapNhanh.add(btnNhapNhanh6, 2, 1);
    		
    		btnPhim0.getStyleClass().add("btn-banPhim");
    		btnPhim1.getStyleClass().add("btn-banPhim");
    		btnPhim2.getStyleClass().add("btn-banPhim");
    		btnPhim3.getStyleClass().add("btn-banPhim");
    		btnPhim4.getStyleClass().add("btn-banPhim");
    		btnPhim5.getStyleClass().add("btn-banPhim");
    		btnPhim6.getStyleClass().add("btn-banPhim");
    		btnPhim7.getStyleClass().add("btn-banPhim");
    		btnPhim8.getStyleClass().add("btn-banPhim");
    		btnPhim9.getStyleClass().add("btn-banPhim");
    		btnPhimC.getStyleClass().add("btn-banPhim");
    		btnPhimCham.getStyleClass().add("btn-banPhim");
    		btnPhimXoaMot.getStyleClass().add("btn-banPhim");
    		iconXoa.setFitHeight(25);
    		iconXoa.setFitWidth(25);
    		btnPhimXoaMot.setGraphic(iconXoa);
    		btnPhim00.setStyle("-fx-font-size: 20px;"
    				+ "	-fx-text-fill: white;"
    				+ "	-fx-border-width: 1;"
    				+ "	-fx-border-radius: 4;"
    				+ "	-fx-border-color: #082744;"
    				+ "	-fx-background-color:  #082744;"
    				+ "	-fx-pref-width: 70;"
    				+ "-fx-font-weight: bold;"
    				+ "-fx-cursor: hand;"
    				+ "	-fx-pref-height: 70;");
    		btnPhim000.setStyle("-fx-font-size: 20px;"
    				+ "	-fx-text-fill: white;"
    				+ "	-fx-border-width: 1;"
    				+ "	-fx-border-radius: 4;"
    				+ "	-fx-border-color: #082744;"
    				+ "	-fx-background-color:  #082744;"
    				+ "	-fx-pref-width: 70;"
    				+ "-fx-font-weight: bold;"
    				+ "-fx-cursor: hand;"
    				+ "	-fx-pref-height: 70;");
    		btnPhimCham000.setStyle("-fx-font-size: 19px;"
    				+ "	-fx-text-fill: white;"
    				+ "	-fx-border-width: 1;"
    				+ "	-fx-border-radius: 4;"
    				+ "	-fx-border-color: #082744;"
    				+ "	-fx-background-color:  #082744;"
    				+ "	-fx-pref-width: 70;"
    				+ "-fx-font-weight: bold;"
    				+ "-fx-cursor: hand;"
    				+ "	-fx-pref-height: 70;");
    		
    		gridPhim.add(btnPhim7, 0, 0);
    		gridPhim.add(btnPhim8, 1, 0);
    		gridPhim.add(btnPhim9, 2, 0);
    		gridPhim.add(btnPhimXoaMot, 3, 0);
    		gridPhim.add(btnPhim4, 0, 1);
    		gridPhim.add(btnPhim5, 1, 1);
    		gridPhim.add(btnPhim6, 2, 1);
    		gridPhim.add(btnPhim00, 3, 1);
    		gridPhim.add(btnPhim1, 0, 2);
    		gridPhim.add(btnPhim2, 1, 2);
    		gridPhim.add(btnPhim3, 2, 2);
    		gridPhim.add(btnPhim000, 3, 2);
    		gridPhim.add(btnPhimC, 0, 3);
    		gridPhim.add(btnPhim0, 1, 3);
    		gridPhim.add(btnPhimCham, 2, 3);
    		gridPhim.add(btnPhimCham000, 3, 3);
    		
    		gridPhim.setHgap(10);
    		gridPhim.setVgap(10);
    		gridPhim.setPadding(new Insets(10, 0, 0,0));
    		
    		btn50.getStyleClass().add("btn-banPhimTien");
    		btn100.getStyleClass().add("btn-banPhimTien");
    		btn200.getStyleClass().add("btn-banPhimTien");
    		btn500.getStyleClass().add("btn-banPhimTien");

    		vboxTien.getChildren().addAll(btn50, btn100, btn200, btn500);
    		vboxTien.setPadding(new Insets(10, 0, 0, 5));
    		
    		hboxPhimNhap.getChildren().addAll(gridPhim, vboxTien);
    		hboxPhimNhap.setPadding(new Insets(10));
    		hboxPhimNhap.setStyle("-fx-border-radius: 10; -fx-border-color: #908C8C; -fx-background-color: white");
    		
    		btnThanhToan.getStyleClass().add("btn-ThanhToan");
    		btnQuayLai.getStyleClass().add("btn-QuayLai");
    		Region spacer = new Region();
    		HBox.setHgrow(spacer, Priority.ALWAYS);
    		hboxButton.getChildren().addAll(btnQuayLai, spacer, btnThanhToan);
    		
    		btnQuayLai.setOnAction(e ->{
    			this.setCenter(taoManHinhDanhSachBan(dsBan));
    		});
    		
    		maQR.setFitHeight(300);
    		maQR.setFitWidth(300);
    		
    		lblTongTienQR.setStyle("-fx-font-size: 20px");
    		
    		
    		// Pane cho từng phương thức
    		VBox paneTienMat = new VBox(10);
    		VBox paneMa = new VBox(20);
    		paneTienMat.getChildren().addAll(hboxTienNhan, hboxTienThua, lblNhapNhanh, gridNhapNhanh, hboxPhimNhap);
    		paneMa.getChildren().addAll(maQR, lblTongTienQR);
    		paneMa.setAlignment(Pos.CENTER);

    		// Ban đầu hiển thị Tiền mặt
    		paneTienMat.setVisible(true);
    		paneTienMat.setManaged(true);
    		paneMa.setVisible(false);
    		paneMa.setManaged(false);
    		btnTienMat.setSelected(true);

    		btnGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
    		    @Override
    		    public void changed(ObservableValue<? extends Toggle> obs, Toggle oldToggle, Toggle newToggle) {
    		        if (newToggle == btnTienMat) {
    		            paneTienMat.setVisible(true);
    		            paneTienMat.setManaged(true);
    		            paneMa.setVisible(false);
    		            paneMa.setManaged(false);
    		            btnThanhToan.setVisible(true);
    		            btnThanhToan.setManaged(true);
    		        } else if (newToggle == btnMa) {
    		            paneTienMat.setVisible(false);
    		            paneTienMat.setManaged(false);
    		            paneMa.setVisible(true);
    		            paneMa.setManaged(true);
    		            btnThanhToan.setVisible(false);
    		            btnThanhToan.setManaged(false);
    		        }
    		    }
    		});

    		
    		vboxAll.getChildren().addAll(lblPhuongThuc, hboxGroup,paneTienMat, paneMa, hboxButton);
    		vboxAll.setPrefWidth(550);
    		vboxAll.setPadding(new Insets(10, 30, 10, 10));
    		vboxAll.setMargin(gridNhapNhanh, new Insets(0,0,0,10));
//    		vboxAll.setMargin(btnThanhToan, new Insets(10, 10, 0,350));
//    		vboxAll.setStyle("-fx-background-color: black");
    		
    		
    		
    		return vboxAll;
    }

//    private void addMonAnTestData(ObservableList<MonAn> items) {
//    	items.addAll(
//    		    new MonAn("MA001", "Cơm gà xào sả ớt", "Món chính", 50000.0, "Món ngon, cay nồng"),
//    		    new MonAn("MA002", "Bò nướng lá lốt", "Món chính", 120000.0, "Thịt bò mềm, thơm"),
//    		    new MonAn("MA003", "Cá kho tộ", "Món chính", 80000.0, "Cá tươi, kho đậm đà"),
//    		    new MonAn("MA004", "Rau củ xào", "Món phụ", 30000.0, "Rau tươi, xào nhanh"),
//    		    new MonAn("MA005", "Nước cam tươi", "Đồ uống", 40000.0, "Cam tươi, ép tại chỗ"),
//    		    new MonAn("MA006", "Canh chua cá lóc", "Món chính", 70000.0, "Vị chua thanh mát"),
//    		    new MonAn("MA007", "Cơm chiên dương châu", "Món chính", 60000.0, "Cơm chiên trứng và xúc xích"),
//    		    new MonAn("MA008", "Mì xào hải sản", "Món chính", 85000.0, "Hải sản tươi, xào đậm vị"),
//    		    new MonAn("MA009", "Gỏi cuốn tôm thịt", "Món khai vị", 45000.0, "Cuốn tươi, chấm nước mắm chua ngọt"),
//    		    new MonAn("MA010", "Chè khúc bạch", "Tráng miệng", 35000.0, "Mát lạnh, ngọt dịu"),
//    		    new MonAn("MA011", "Cà phê sữa đá", "Đồ uống", 30000.0, "Đậm đà, truyền thống Việt"),
//    		    new MonAn("MA012", "Bánh flan caramel", "Tráng miệng", 25000.0, "Béo ngậy, thơm ngon"),
//    		    new MonAn("MA013", "Phở bò tái", "Món chính", 70000.0, "Nước lèo trong, thịt bò mềm"),
//    		    new MonAn("MA014", "Bún chả Hà Nội", "Món chính", 65000.0, "Chả nướng thơm lừng"),
//    		    new MonAn("MA015", "Gà hấp muối", "Món đặc biệt", 150000.0, "Gà ta hấp muối nguyên con"),
//    		    new MonAn("MA016", "Nước ép dưa hấu", "Đồ uống", 35000.0, "Giải khát, tự nhiên"),
//    		    new MonAn("MA017", "Trà đào cam sả", "Đồ uống", 40000.0, "Hương vị tươi mát"),
//    		    new MonAn("MA018", "Bánh mì thịt nướng", "Món phụ", 30000.0, "Thịt nướng thơm, pate béo"),
//    		    new MonAn("MA019", "Khoai tây chiên", "Món phụ", 25000.0, "Giòn tan, ăn kèm tương ớt"),
//    		    new MonAn("MA020", "Súp cua", "Món khai vị", 40000.0, "Sánh mịn, thơm vị cua"),
//    		    new MonAn("MA021", "Lẩu thái hải sản", "Món đặc biệt", 250000.0, "Cay nồng, hải sản tươi"),
//    		    new MonAn("MA022", "Lẩu gà lá giang", "Món đặc biệt", 180000.0, "Chua thanh, vị quê"),
//    		    new MonAn("MA023", "Bánh xèo miền Tây", "Món chính", 50000.0, "Giòn rụm, tôm thịt đầy đặn"),
//    		    new MonAn("MA024", "Gà rán giòn", "Món chính", 60000.0, "Giòn tan, thơm ngon"),
//    		    new MonAn("MA025", "Cá hồi nướng bơ tỏi", "Món đặc biệt", 220000.0, "Thơm béo, thịt cá mềm"),
//    		    new MonAn("MA026", "Sinh tố bơ", "Đồ uống", 35000.0, "Béo mịn, mát lạnh"),
//    		    new MonAn("MA027", "Soda chanh", "Đồ uống", 30000.0, "Có gas, chua nhẹ"),
//    		    new MonAn("MA028", "Bánh ngọt socola", "Tráng miệng", 40000.0, "Ngọt ngào, tan chảy"),
//    		    new MonAn("MA029", "Cơm tấm sườn bì chả", "Món chính", 65000.0, "Đặc sản Sài Gòn"),
//    		    new MonAn("MA030", "Nước suối", "Đồ uống", 15000.0, "Tinh khiết, không gas")
//    		);
//
//    }
}
