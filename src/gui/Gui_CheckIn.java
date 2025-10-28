package gui;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import control.Crl_CheckIn;
import entity.LoaiBan;
import entity.PhieuDatBan;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;


public class Gui_CheckIn extends BorderPane {

	private Crl_CheckIn control = new Crl_CheckIn();
	private TextField txtMaKh;
	private TextField txtTenKH;
	private TextField txtSdt;
	private TextField txtDiem;
	private TextField txtGioDen;
	private TextField txtNgayDen;
	private TextField txtSoNguoi;
	private RadioButton radioDatTruoc;
	private RadioButton radioDungNgay;
	private TextField txtTienCoc;
	private GridPane luoiBan;
	private ScrollPane cuonLuoi;
	
	private List<String> dsBanDat;
	private ToggleButton tang1;
	private ToggleButton tang2;
	private TextField txtTiemKiem;
	private Button nutCheckIn;
	private String maBanDaChon = null;
	private ToggleGroup radioGroup;
	
	public Gui_CheckIn() {

        this.setStyle("-fx-background-color: white;");
  
        
        // Phần giữa: HBox với phần trái (khu vực tầng, tìm kiếm, danh sách bàn) và phần phải (thông tin khách hàng)
        HBox phanTren = taoPhanTren();
        HBox phanGiua = taoPhanGiua();
        VBox phanGiuaAll = new VBox();
        phanGiuaAll.getChildren().addAll(phanTren, phanGiua);
        phanGiuaAll.setMinWidth(750);
        this.setCenter(phanGiuaAll);
        
        
        
        // Phần dưới: Thông tin đặt bàn với DatePicker và Spinner cho giờ
        VBox phanBenPhai = taoPhanBenPhai();
        this.setRight(phanBenPhai);
        this.getStylesheets().add(getClass().getResource("/css/qlkm.css").toExternalForm());
	}

    // Tạo phần trên với nút tầng và trạng thái
    private HBox taoPhanTren() {
        VBox top = new VBox(5);
        top.setPadding(new Insets(10));
        // lbl chon tang
        Label lblChonTang = new Label("Khu vực");
        lblChonTang.getStyleClass().add("fontTieuDeNho");

        // Nút chọn tầng
        HBox nutTang = new HBox(5);
        nutTang.setAlignment(Pos.TOP_LEFT);
        nutTang.setMinWidth(400);
        tang1 = new ToggleButton("Tầng 1");
        tang1.setPrefSize(70, 40);
        tang2 = new ToggleButton("Tầng 2");
        tang2.setPrefSize(70, 40);
        ToggleGroup buttonGroup = new ToggleGroup();
        tang1.setToggleGroup(buttonGroup);
        tang2.setToggleGroup(buttonGroup);
        tang1.getStyleClass().add("nutTang");
        tang2.getStyleClass().add("nutTang");
        nutTang.getChildren().addAll(tang1, tang2);
        tang1.setSelected(true);
        
        //Chức năng loc tầng
        tang1.setOnAction(e ->{
        		cuonLuoi.setContent(taoLuoiBan(control.chiaTang(dsBanDat, "Tầng 1")));
        });
        
        tang2.setOnAction(e ->{
    		cuonLuoi.setContent(taoLuoiBan(control.chiaTang(dsBanDat, "Tầng 2")));
        });
        

        // Ô tìm kiếm
        Label lblTiemKiem = new Label("Tìm kiếm");
        lblTiemKiem.getStyleClass().add("fontTieuDeNho");
        txtTiemKiem = new TextField();
        txtTiemKiem.setPromptText("Tìm kiếm bàn số điện thoại khách hàng");
        txtTiemKiem.getStyleClass().add("timKiem");
        Button nutTimKiem = new Button("Tìm kiếm");
        nutTimKiem.getStyleClass().add("button-timKiem");
        HBox oTimKiem = new HBox(10, txtTiemKiem, nutTimKiem);
        oTimKiem.setAlignment(Pos.CENTER_LEFT);
        
        //Chuc nang tim kiem
        nutTimKiem.setOnAction(e -> {
            String chuoiSDT = txtTiemKiem.getText();
            timKiemBanDat(chuoiSDT);
        });


        // Trạng thái chức vụ
        VBox trangThai = new VBox(2);
        trangThai.setPrefWidth(100);
        trangThai.setPadding(new Insets(10));
        Label chucVu = new Label("Chức vụ");
        chucVu.getStyleClass().add("fontTieuDeNho");
        Label vip = new Label("Bàn VIP");
        vip.setStyle("-fx-text-fill: #ed8936; -fx-font-weight: bold; -fx-font-size: 11;");
        HBox chuThich1 = new HBox(2);
        ImageView iconVIP = new ImageView(new Image(getClass().getResourceAsStream("/img/vipicon.png")));
        iconVIP.setFitWidth(15);
        iconVIP.setFitHeight(15);
        chuThich1.getChildren().addAll(iconVIP, vip);
        
        
        chuThich1.getChildren().addAll();
        Label dangChon = new Label("Đang chọn");
        HBox chuThich2 = new HBox(2);
        ImageView iconDangChon = new ImageView(new Image(getClass().getResourceAsStream("/img/dotgray.png")));
        iconDangChon.setFitWidth(15);
        iconDangChon.setFitHeight(15);
        chuThich2.getChildren().addAll(iconDangChon, dangChon);
        dangChon.setStyle("-fx-text-fill: gray; -fx-font-weight: bold; -fx-font-size: 11;");
        Label daBan = new Label("Đã đặt bàn");
        daBan.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: 11;");
        HBox chuThich3 = new HBox(2);
        ImageView iconDaDat = new ImageView(new Image(getClass().getResourceAsStream("/img/dotred.png")));
        iconDaDat.setFitWidth(15);
        iconDaDat.setFitHeight(15);
        chuThich3.getChildren().addAll(iconDaDat, daBan);
        
        trangThai.getChildren().addAll(chucVu, chuThich1, chuThich2, chuThich3);

        
        top.getChildren().addAll(lblChonTang, nutTang, lblTiemKiem, oTimKiem);
        top.setStyle("-fx-border-width: 0 1 0 0; -fx-border-color: #D9D9D9");
        
        HBox topALL = new HBox(40);
        topALL.getChildren().addAll(top,trangThai);
        
        return topALL;
    }

    // Tìm kiếm bàn đặt theo SĐT
    private void timKiemBanDat(String text) {
        String soDienThoai = text.trim();

        // Gọi hàm DAO hoặc control để lấy chuỗi dữ liệu
        String chuoi = control.timBanBangSDT(soDienThoai, LocalDate.now());

        // Kiểm tra nếu không có dữ liệu
        if (chuoi == null || chuoi.isBlank() || chuoi.equals("N/A")) {
            //System.out.println("❌ Không tìm thấy bàn đặt cho SĐT: " + soDienThoai);
        	JOptionPane.showMessageDialog(null, "Không tìm thấy bàn đặt !", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        	txtTiemKiem.requestFocus();
        	txtTiemKiem.selectAll();        	
        	// Hiện lại thông tin cũ
        	List<String> dsBan = control.layThongTinDatBan(LocalDate.now());
        	cuonLuoi.setContent(taoLuoiBan(dsBan));
        tang1.setSelected(true);
        
        xoaThongTinHienThi();
        	return;
        	
        }
        
        List<String> dsBan = List.of(chuoi);
        cuonLuoi.setContent(taoLuoiBan(dsBan));    
        if(dsBan.get(0).split(",")[10].equals("Tầng 1")) {
        		tang1.setSelected(true);
        }else {
        		tang2.setSelected(true);
        }
        xoaThongTinHienThi();
    }

        
 
    
    // Tạo phần giữa với phần trái (tìm kiếm và danh sách bàn) và phần phải (thông tin khách hàng)
    private HBox taoPhanGiua() {
    		dsBanDat = control.layThongTinDatBan(LocalDate.of(2025, 10, 23));
    		//
        HBox giua = new HBox(20);
        giua.setPadding(new Insets(10));

        // Phần trái: Khu vực tìm kiếm và danh sách bàn
        VBox trai = new VBox(10);
        trai.setPrefWidth(750);

        // Tiêu đề danh sách
        Label danhSach = new Label("Danh sách bàn");
        danhSach.getStyleClass().add("fontTieuDeNho");

        // Lưới bàn
        luoiBan = taoLuoiBan(control.chiaTang(dsBanDat,"Tầng 1"));
        cuonLuoi = new ScrollPane(luoiBan);
        cuonLuoi.setFitToWidth(true);
        cuonLuoi.setPrefHeight(620);
        cuonLuoi.getStyleClass().add("scroll-pane");

        trai.getChildren().addAll( danhSach, cuonLuoi);

        // Phần phải: Thông tin khách hàng
        VBox phai = new VBox(8);
        phai.setPrefWidth(500);
        Label tieuDe = new Label("Thông tin khách hàng");
        tieuDe.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-text-fill: #2d3748;");
        GridPane luoiThongTin = new GridPane();
        luoiThongTin.setHgap(5);
        luoiThongTin.setVgap(4);

        // Các trường thông tin
        Label nhanMaKH = new Label("Mã khách hàng:");
        TextField maKH = new TextField("KH000001");
        maKH.setEditable(false);
        maKH.setPrefWidth(200);
        luoiThongTin.addRow(0, nhanMaKH, maKH);

        Label nhanTenKH = new Label("Tên khách hàng:");
        TextField tenKH = new TextField("Hồ Văn Thông");
        tenKH.setEditable(false);
        tenKH.setPrefWidth(200);
        luoiThongTin.addRow(1, nhanTenKH, tenKH);

        Label nhanSDT = new Label("SĐT:");
        TextField sdt = new TextField("089398872");
        sdt.setEditable(false);
        sdt.setPrefWidth(200);
        luoiThongTin.addRow(2, nhanSDT, sdt);

        Label nhanDiem = new Label("Điểm lịch sử:");
        TextField diem = new TextField("1236");
        diem.setEditable(false);
        diem.setPrefWidth(200);
        luoiThongTin.addRow(3, nhanDiem, diem);

        phai.getChildren().addAll(tieuDe, luoiThongTin);

        giua.getChildren().addAll(trai);

        return giua;
    }

    // Tạo lưới các bàn
    private GridPane taoLuoiBan(List<String> DSThongTinBan) {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        grid.setStyle("-fx-background-color: white");
        
        
        List<StackPane> danhSachBan = new ArrayList<StackPane>();

        
        for (int i = 0; i < DSThongTinBan.size(); i++) {
        		String chuoi = DSThongTinBan.get(i);
            // Truyền màu gốc vào taoTheBan để set style cho VBox the
            StackPane theBan = taoTheBan(DSThongTinBan.get(i).split(",")[0], DSThongTinBan.get(i).split(",")[1], DSThongTinBan.get(i).split(",")[2], DSThongTinBan.get(i).split(",")[3].equals("VIP") ? LoaiBan.VIP : LoaiBan.THUONG);
            int hang = i / 3;
            int cot = i % 3;
            danhSachBan.add(theBan);
    	
            // Sử dụng index để capture đúng bàn khi click
            final int indexBan = i;
            theBan.setOnMouseClicked(e -> {
            		loadThongTinBenPhai(chuoi);
                // Reset tất cả bàn về màu gốc (dựa trên index)
                for (int j = 0; j < danhSachBan.size(); j++) {
                    StackPane khungReset = danhSachBan.get(j);
                    VBox theReset = (VBox) khungReset.getChildren().get(1);  
                    
                    theReset.setStyle("-fx-background-color: #082744"  + ";" +
                                      "-fx-background-radius: 20;" +
                                      "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);" +
                                      "-fx-cursor: hand;");
                }
                  
                // Đổi màu thẻ được click thành xám (selected)
                StackPane khungChon = danhSachBan.get(indexBan);
                VBox theChon = (VBox) khungChon.getChildren().get(1);  // Lấy VBox the
                theChon.setStyle("-fx-background-color: #a0aec0;" + 
                                 "-fx-background-radius: 20;" +
                                 "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);" +
                                 "-fx-cursor: hand;");
                maBanDaChon = chuoi.split(",")[11];
            });
            GridPane.setRowIndex(theBan, hang);
            GridPane.setColumnIndex(theBan, cot);
            grid.getChildren().add(theBan);
        }
        
        return grid;
    }

    
    // Tạo thẻ bàn
    private StackPane taoTheBan(String tenBan, String khach, String thoiGian, LoaiBan loaiBan) {
        // ----- StackPane chứa cả thẻ -----
        StackPane khung = new StackPane();
        khung.setPrefSize(220, 130);

        // ----- Nền đỏ bên trái -----
        Region mauDo = new Region();
        mauDo.setPrefSize(15, 130);
        mauDo.setStyle("-fx-background-color: red; -fx-background-radius: 20;");

        // ----- Thẻ chính màu xanh -----
        VBox the = new VBox(10);
        the.setPrefSize(210, 130);
        the.setAlignment(Pos.CENTER);
        the.setPadding(new Insets(0, 10, 0, 10));
        the.getStyleClass().add("theBan");

        // ----- Các label -----
        Label nhanBan = new Label(tenBan);
        nhanBan.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        nhanBan.setTextFill(Color.WHITE);

        Label nhanKhach = new Label(khach);
        nhanKhach.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        nhanKhach.setTextFill(Color.BLACK);

        Label nhanThoiGian = new Label(thoiGian);
        ImageView iconClock = new ImageView(new Image("/img/clock.png"));
        iconClock.setFitHeight(20);
        iconClock.setFitWidth(20);
        nhanThoiGian.setFont(Font.font("Arial", 11));
        nhanThoiGian.setTextFill(Color.BLACK);
        HBox hboxThoiGian = new HBox(iconClock, nhanThoiGian);
        hboxThoiGian.setAlignment(Pos.CENTER);
        hboxThoiGian.setSpacing(10);
         
        VBox vboxThongTinKhach = new VBox(nhanKhach,  hboxThoiGian);
        vboxThongTinKhach.setPadding(new Insets(8));
        vboxThongTinKhach.setStyle("-fx-background-color: white; -fx-background-radius: 5");
        vboxThongTinKhach.setPrefWidth(80);
        vboxThongTinKhach.setAlignment(Pos.CENTER);
        
        ImageView iconVip = new ImageView(new Image("img/vipicon.png"));
        iconVip.setFitHeight(20);
        iconVip.setFitWidth(20);        
        HBox hboxVip = new HBox();
        if(loaiBan.equals(LoaiBan.VIP)) {
        		hboxVip.getChildren().add(iconVip);
        }
        hboxVip.setAlignment(Pos.TOP_RIGHT);
        hboxVip.setMinHeight(20);
       


        the.getChildren().addAll(hboxVip,nhanBan, vboxThongTinKhach);

        // ----- Ghép lại -----
        khung.getChildren().addAll(mauDo, the);
        StackPane.setAlignment(mauDo, Pos.CENTER_LEFT);
        StackPane.setMargin(the, new Insets(0, 0, 0, 5)); 

        return khung;
    }
    
    public void loadSauKhiCheckIn() {
    		dsBanDat = control.layThongTinDatBan(LocalDate.now());
    		if(tang1.isSelected()) {
    			cuonLuoi.setContent(taoLuoiBan(control.chiaTang(dsBanDat, "Tầng 1")));
    		}else {
    			cuonLuoi.setContent(taoLuoiBan(control.chiaTang(dsBanDat, "Tầng 2")));
    		}
    }
    
    public void xoaThongTinHienThi() {
    		txtMaKh.setText("");
    		txtTenKH.setText("");
    		txtSdt.setText("");
    		txtDiem.setText("");
    		txtGioDen.setText("");
    		txtNgayDen.setText("");
    		txtSoNguoi.setText("");
    		radioGroup.selectToggle(null);
    }


    // Tạo phần dưới với thông tin đặt bàn
    private VBox taoPhanBenPhai() {
    		// Spacer
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
    		Region spacer9 = new Region();
    		HBox.setHgrow(spacer9, Priority.ALWAYS);
    		Region spacer7 = new Region();
    		HBox.setHgrow(spacer7, Priority.ALWAYS);
    		
        // Tiêu đề - 1
        Label tieuDeKH = new Label("Thông Tin Khách Hàng");
        tieuDeKH.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");

        //Ma kh 
        Label lblMaKH = new Label("Mã khách hàng:");
        lblMaKH.getStyleClass().add("fontTieuDeNho");
        txtMaKh = new TextField();
        txtMaKh.setEditable(false);
        txtMaKh.setPrefWidth(250);
        txtMaKh.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 12; -fx-background-color: #e8f8f5; -fx-font-size: 15");
        HBox hbox1 = new HBox(lblMaKH, spacer1, txtMaKh );
        hbox1.setPadding(new Insets(5));
        

       
        //Ten kh
        Label lblTenKH = new Label("Tên khách hàng:");
        lblTenKH.getStyleClass().add("fontTieuDeNho");
        txtTenKH = new TextField();
        txtTenKH.setEditable(false);
        txtTenKH.setPrefWidth(250);
        txtTenKH.setPrefWidth(250);
        txtTenKH.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #e8f8f5; -fx-font-size: 15; -fx-border-weight: 1; -fx-border-coloer");
        HBox hbox2 = new HBox(lblTenKH, spacer2, txtTenKH );
        hbox2.setPadding(new Insets(5));
        
        //So dien Thoai
        Label lblSdt = new Label("Số điện thoại:");
        lblSdt.getStyleClass().add("fontTieuDeNho");
        txtSdt = new TextField();
        txtSdt.setPrefWidth(250);
        txtSdt.setEditable(false);
        txtSdt.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #e8f8f5; -fx-font-size: 15");
        HBox hbox3 = new HBox(lblSdt, spacer3,txtSdt );	
        hbox3.setPadding(new Insets(5));
        
        //Diem tich luy
        Label lblDiem = new Label("Điểm tích lũy:");
        lblDiem.getStyleClass().add("fontTieuDeNho");
        txtDiem = new TextField();
        txtDiem.setPrefWidth(250);
        txtDiem.setEditable(false);
        txtDiem.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #e8f8f5; -fx-font-size: 15");
        HBox hbox4 = new HBox(lblDiem, spacer4,txtDiem);
        hbox4.setPadding(new Insets(5));
        
        /// VBOX ALL - 1
        VBox vboxAll1 = new VBox(5);
        vboxAll1.getChildren().addAll(tieuDeKH, hbox1, hbox2, hbox3, hbox4);
        
        
        //Tieu de - 2
        Label lblThongTinDatBan = new Label("Thông Tin Đặt Bàn");
        lblThongTinDatBan.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");
        
        // Ngay gio den
        Label lblNgayGio = new Label("Ngày giờ đến:");
        lblNgayGio.getStyleClass().add("fontTieuDeNho");
        txtGioDen = new TextField();
        txtNgayDen = new TextField();
        txtGioDen.setEditable(false);
        txtNgayDen.setEditable(false);
        txtGioDen.setPrefWidth(80);
        txtGioDen.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #e8f8f5; -fx-font-size: 15; -fx-background-radius: 3 0 0 3");
        txtNgayDen.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-background-color: #e8f8f5; -fx-font-size: 15; -fx-background-radius: 0 3 3 0");
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
        txtSoNguoi = new TextField();
        txtSoNguoi.setEditable(false);
        txtSoNguoi.setPrefWidth(250);
        txtSoNguoi.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #e8f8f5; -fx-font-size: 15; ");
        HBox hbox6 = new HBox(lblSoNguoi, spacer6,txtSoNguoi);
        hbox6.setPadding(new Insets(5));
        
        //Kiểu đặt bàn
        Label lblKieuDatBan = new Label("Kiểu đặt bàn");
        lblKieuDatBan.getStyleClass().add("fontTieuDeNho");
        radioDatTruoc = new RadioButton("Đặt trước");
        radioDungNgay = new RadioButton("Dùng ngay");
        radioGroup = new ToggleGroup();
        radioDatTruoc.setToggleGroup(radioGroup);
        radioDungNgay.setToggleGroup(radioGroup);
        radioDatTruoc.getStyleClass().add("radio-button");
        radioDungNgay.getStyleClass().add("radio-button");
        radioDatTruoc.setDisable(true);
        radioDungNgay.setDisable(true);
        
        HBox hboxRadio = new HBox(5);
        hboxRadio.setPrefWidth(250);
        Region spaceRadio = new Region();
        HBox.setHgrow(spaceRadio, Priority.ALWAYS);
        hboxRadio.getChildren().addAll(radioDatTruoc, spaceRadio,radioDungNgay);
        HBox hbox7 = new HBox(lblKieuDatBan,spacer7, hboxRadio);
        hbox7.setPadding(new Insets(5));
        
        //Vbox all - 2
        VBox vboxAll2 = new VBox(5);
        vboxAll2.getChildren().addAll(lblThongTinDatBan, hbox5, hbox6, hbox7);
        
        
        //Tieu de - 3
        Label lblTienCocTieuDe = new Label("Tiền đặt cọc");
        lblTienCocTieuDe.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");
        
        // Tien coc
        Label lblTienCoc = new Label("Tiền cọc:");
        lblTienCoc.getStyleClass().add("fontTieuDeNho");
        txtTienCoc = new  TextField();
        txtTienCoc.setEditable(false);
        txtTienCoc.setPrefWidth(250);
        txtTienCoc.setStyle("-fx-text-fill: red; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #e8f8f5; -fx-font-size: 15; -fx-font-weight: bold;");
        HBox hbox8 = new HBox(lblTienCoc, spacer8,txtTienCoc);
        hbox8.setPadding(new Insets(5));
        
        
        // Vebox All - 3
        VBox vboxAll3 = new VBox(5);
        vboxAll3.getChildren().addAll(lblTienCocTieuDe,hbox8);
        
        
        nutCheckIn = new Button("Check-in");
        nutCheckIn.setPrefSize(120, 40);
        nutCheckIn.getStyleClass().add("button-checkin");
        HBox hbox9 = new HBox(nutCheckIn);
        nutCheckIn.setOnAction(e -> {
        	
        	//Check coi coi đã có chọn cái nào chưa
        		if(maBanDaChon == null) {
        			JOptionPane.showMessageDialog(null, "Phải chọn một bàn để check-in");
        		}else {
                	Alert dialog = new Alert(AlertType.INFORMATION);
                dialog.setTitle("Thông Báo");
                dialog.setHeaderText("");
                
                if(control.capNhatTrangThai(maBanDaChon, "Đã dùng")) {
                		dialog.setContentText("Đã check-in thành công!");
                		ImageView iconCheck = new ImageView(new Image("/img/check.png"));
                		iconCheck.setFitHeight(30);
                		iconCheck.setFitWidth(30);
                		dialog.setGraphic(iconCheck);
                		loadSauKhiCheckIn();
                }else {
	                	dialog.setContentText("Check-in không thành công!");
	            		ImageView iconCheck = new ImageView(new Image("/img/cross.png"));
	            		iconCheck.setFitHeight(30);
	            		iconCheck.setFitWidth(30);
	            		dialog.setGraphic(iconCheck);
                }
                dialog.showAndWait();
        		}
        });
        
        hbox9.setPadding(new Insets(20));
        hbox9.setPrefHeight(200);
        hbox9.setAlignment(Pos.BOTTOM_RIGHT);
        

        /// Vbox All
        VBox vboxALL = new VBox(40);
        vboxALL.getChildren().addAll(vboxAll1, vboxAll2, vboxAll3, hbox9);
        vboxALL.setAlignment(Pos.TOP_LEFT);
        vboxALL.setPadding(new Insets(20,20, 0, 0));
        vboxALL.setMinWidth(500);
        
        return vboxALL;
    }
    
	public void loadThongTinBenPhai(String chuoi) {
		// 🟢 Tách dữ liệu từ chuỗi CSV
		String[] parts = chuoi.split(",");

		String maBan = parts[0];
		String tenKH = parts[1];
		String ngayGio = parts[2];
		String ngay = ngayGio.split(" ")[1];
		String gio = ngayGio.split(" ")[0];
		String loaiBan = parts[3];
		String maKH = parts[4];
		String sdt = parts[5];
		String diemTichLuy = parts[6];
		String soNguoi = parts[8];
		String kieuDatBan = parts[9];

		// 🟢 Xác định tiền cọc dựa theo loại bàn
		double tienCoc = loaiBan.equalsIgnoreCase("VIP") ? 450000.0 : 350000.0;

		// 🟢 Gán dữ liệu lên giao diện
		txtMaKh.setText(maKH);
		txtTenKH.setText(tenKH);
		txtSdt.setText(sdt);
		txtDiem.setText(diemTichLuy);
		txtSoNguoi.setText(String.valueOf(soNguoi));
		txtNgayDen.setText(ngay);
		txtGioDen.setText(gio);

		if (!kieuDatBan.trim().equalsIgnoreCase("Dùng ngay")) {
			radioDatTruoc.setSelected(true);
			txtTienCoc.setText(String.valueOf(tienCoc) + 'đ');
		} else {
			radioDungNgay.setSelected(true);
			txtTienCoc.setText("0.0đ");
		}

	}
	

}