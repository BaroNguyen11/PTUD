package gui;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import entity.MonAn;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Gui_QuanLiKhuyenMai extends BorderPane {
    // DANH SÁCH TOÀN CỤC
    ObservableList<MonAn> dsMonAn = FXCollections.observableArrayList();
    ObservableList<MonAn> dsMonChon = FXCollections.observableArrayList();

    // Preload image để tránh load lại mỗi lần tạo cell (giảm lag scroll)
    private static final Image IMG_MON_AN = new Image("/img/monAn.png");
    private static final Image IMG_DAU_CONG_DEN = new Image("/img/dauCongDen.png");
    private static final Image IMG_LICH_TRANG = new Image("/img/lichTrang.png");
    private static final Image IMG_CHAM_XANH = new Image("/img/chamXanh.png");
    private static final Image IMG_CHAM_DO = new Image("/img/chamDo.png");
    private static final Image IMG_CHU_NHAT_XANH = new Image("/img/chuNhatXanh.png");
    private static final Image IMG_CHU_NHAT_VANG = new Image("/img/chuNhatVang.png");
    private static final Image IMG_LON_HON_HOAC_BANG = new Image("/img/lonHonHoacBang.png");


   public Gui_QuanLiKhuyenMai() {
       BorderPane mainLayout = new BorderPane();

       // Thêm phần bên trái và bên phải
       VBox phanTrai = taoPhanBenTrai();
       VBox phanPhai = taoPhanBenPhai();

       mainLayout.setLeft(phanTrai);
       mainLayout.setRight(phanPhai);
       mainLayout.setStyle("-fx-background-color: #f5f5f5;");
       this.getStylesheets().add(getClass().getResource("/css/qlkm.css").toExternalForm());
       // Set vào BorderPane chính (this)
       this.setCenter(mainLayout);
   }
    public VBox taoPhanBenTrai() {
        // Ô tìm kiếm
        Label lblTiemKiem = new Label("Tìm kiếm");
        lblTiemKiem.getStyleClass().add("fontTieuDeNho");
        TextField timKiem = new TextField();
        timKiem.setPromptText("Tìm kiếm bàn số điện thoại khách hàng");
        timKiem.getStyleClass().add("timKiem");
        Button nutTimKiem = new Button("Tìm kiếm");
        nutTimKiem.getStyleClass().add("button-timKiem");
        HBox oTimKiem = new HBox(10, timKiem, nutTimKiem);
        oTimKiem.setAlignment(Pos.CENTER_LEFT);
        VBox vboxTimKiem = new VBox(5);
        vboxTimKiem.getChildren().addAll(lblTiemKiem, oTimKiem);

        ///Tieu de danh sach va chu thich danh sach
        Label lblDanhSach = new Label("Danh sách khuyến mãi");
        lblDanhSach.getStyleClass().add("fontTieuDeNho");
        lblDanhSach.setMinWidth(200);

        //Hb1
        HBox hbox1 = new HBox(2);
        Label lblConHan = new Label("Còn hạn sử dụng");
        lblConHan.setStyle("-fx-text-fill: gray; -fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 10");
        ImageView iconChamXanh = new ImageView(IMG_CHAM_XANH);
        iconChamXanh.setFitHeight(10);
        iconChamXanh.setFitWidth(10);
        hbox1.getChildren().addAll(iconChamXanh, lblConHan);
        hbox1.setAlignment(Pos.CENTER_LEFT);

        //hb2
        HBox hbox2 = new HBox(2);
        Label lblHetHan = new Label("Hết hạn sử dụng");
        lblHetHan.setStyle("-fx-text-fill: gray; -fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 10px");
        ImageView iconChamDo = new ImageView(IMG_CHAM_DO);
        iconChamDo.setFitHeight(10);
        iconChamDo.setFitWidth(10);
        hbox2.getChildren().addAll(iconChamDo, lblHetHan);
        hbox2.setAlignment(Pos.CENTER_LEFT);

        //Vbox 1
        VBox vbox1 = new VBox(3);
        vbox1.getChildren().addAll(hbox1, hbox2);

        //hb3
        HBox hbox3 = new HBox(2);
        Label lblHoaDon = new Label("Hóa đơn");
        lblHoaDon.setStyle("-fx-text-fill: gray; -fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 10px");
        ImageView iconChuNhatXanh = new ImageView(IMG_CHU_NHAT_XANH);
        iconChuNhatXanh.setFitHeight(10);
        iconChuNhatXanh.setFitWidth(15);
        hbox3.getChildren().addAll(iconChuNhatXanh, lblHoaDon);
        hbox3.setAlignment(Pos.CENTER_LEFT);

        //hb4
        HBox hbox4 = new HBox(2);
        Label lblMonAn = new Label("Món ăn");
        lblMonAn.setStyle("-fx-text-fill: gray; -fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 10px");
        ImageView iconChuNhatVang = new ImageView(IMG_CHU_NHAT_VANG);
        iconChuNhatVang.setFitHeight(10);
        iconChuNhatVang.setFitWidth(15);
        hbox4.getChildren().addAll(iconChuNhatVang, lblMonAn);
        hbox4.setAlignment(Pos.CENTER_LEFT);

        //vb2
        VBox vbox2 = new VBox(3);
        vbox2.getChildren().addAll(hbox3, hbox4);

        //HBOX chú thích
        HBox hboxChuThich = new HBox(10);
        hboxChuThich.getChildren().addAll(lblDanhSach, vbox1, vbox2);
        hboxChuThich.setAlignment(Pos.BOTTOM_LEFT);

        //Danh sach khuyen mai
        GridPane dsKhuyenMai = taoDanhSachKhuyenMai();
        dsKhuyenMai.setMinWidth(500);
        ScrollPane cuonLuoi = new ScrollPane(dsKhuyenMai);
        cuonLuoi.setPrefWidth(500);
        cuonLuoi.setPrefHeight(620);
        cuonLuoi.getStyleClass().add("scroll-pane");

        //ALLLLL
        VBox vboxAll = new VBox(20);
        vboxAll.setPadding(new Insets(10, 20, 10, 20));
        vboxAll.setMinHeight(600);

        vboxAll.getChildren().addAll(vboxTimKiem, hboxChuThich, cuonLuoi);

        return vboxAll;
    }

    public GridPane taoDanhSachKhuyenMai() {
        GridPane grid = new GridPane();
        //Danh sách tên khuyen mãi
        String[] tenKM = {"1","2","3","4","5","6","7","8","9","10"};

        List<HBox> dsKhuyenMai = new ArrayList<HBox>();

        for(int i = 0; i  < tenKM.length; i++) {
            HBox khuyenMai = taoKhuyenMai(1, false, 10000000, true, tenKM[i], LocalDate.now(), LocalDate.now());
            dsKhuyenMai.add(khuyenMai);
            //ADD
            grid.add(khuyenMai,0, i);
        }

        //GRID
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.setStyle("-fx-background-color: white");

        return grid;
    }

    public HBox taoKhuyenMai(int loai, boolean giamGiaPhanTran, double giaTriGiam, boolean hsd, String tenGiamGia, LocalDate ngayBatDau, LocalDate ngayKetThuc) {
        //The ben trai
        StackPane stBenTrai = new StackPane();

        //Nen dang sau
        HBox hboxHSD = new HBox();
        hboxHSD.setMinHeight(110);
        hboxHSD.setPrefWidth(50);
        String stringHSD = "red";
        if(hsd) {
            stringHSD = "green";
        }
        hboxHSD.setStyle("-fx-background-radius: 8; "
                + "-fx-background-color: " + stringHSD );

        //ThongTin
        HBox hboxThongTin = new HBox(10);
        hboxThongTin.setMinWidth(380);
        hboxThongTin.setStyle("-fx-border-width: 0 0 1 0; -fx-border-color: #A09E9E ");

        HBox hboxGiaTriGiam = new HBox();
        DecimalFormat fomatTien = new DecimalFormat("#,###");
        Label lblGiaTriGiam = new Label(fomatTien.format(giaTriGiam));
        if(giamGiaPhanTran) {
            lblGiaTriGiam.setText("Giảm giá " + giaTriGiam);
        }
        lblGiaTriGiam.setStyle("-fx-text-fill: red; -fx-font-size: 35; -fx-font-weight: bold; ");
        lblGiaTriGiam.setMinWidth(300);
        lblGiaTriGiam.setAlignment(Pos.BOTTOM_RIGHT);
        hboxGiaTriGiam.getChildren().add(lblGiaTriGiam);

        HBox hboxDonVi = new HBox();
        Label lblDonVi = new Label("VND");
        if(giamGiaPhanTran) {
            lblDonVi.setText("%");
        }
        lblDonVi.setStyle("-fx-text-fill: red; -fx-font-size: 20; -fx-font-weight: bold;");
        hboxDonVi.getChildren().add(lblDonVi);
        hboxDonVi.setAlignment(Pos.BOTTOM_RIGHT);
        hboxDonVi.setPrefWidth(100);
        HBox.setMargin(lblDonVi, new Insets(0, 10, 0, 0));

        hboxThongTin.getChildren().addAll(hboxGiaTriGiam, hboxDonVi);

        // Ten giam Gia
        HBox hboxTenGiamGia = new HBox();
        Label lblTenGiamGia = new Label(tenGiamGia);
        lblTenGiamGia.setStyle("-fx-font-size: 30; -fx-font-weight: bold");
        hboxTenGiamGia.getChildren().add(lblTenGiamGia);
        hboxTenGiamGia.setAlignment(Pos.CENTER);

        VBox vboxThongTin = new VBox(5);
        vboxThongTin.setPrefWidth(300);
        vboxThongTin.setMinHeight(100);
        vboxThongTin.setStyle("-fx-background-color: white; -fx-background-radius: 8");
        vboxThongTin.setPadding(new Insets(20,20,0,20));
        vboxThongTin.getChildren().addAll(hboxThongTin, hboxTenGiamGia);

        stBenTrai.setMinHeight(110);
        stBenTrai.setMinWidth(300);
        stBenTrai.getChildren().addAll(hboxHSD, vboxThongTin);

        StackPane.setMargin(vboxThongTin, new Insets(0, 0, 0, 10));

        // The ben phải
        StackPane stBenPhai = new StackPane();

        stBenPhai.setMinHeight(110);
        stBenPhai.setMinWidth(150);

        HBox hboxNgayApDung = new HBox(2);

        ImageView iconLich = new ImageView(IMG_LICH_TRANG);
        iconLich.setFitHeight(40);
        iconLich.setFitWidth(40);

        VBox vboxNgay = new VBox(2);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Label lblNgayBatDau = new Label(dtf.format(ngayBatDau));
        Label lblNgayKetThuc = new Label(dtf.format(ngayKetThuc));
        lblNgayBatDau.setStyle("-fx-font-size:15; -fx-text-fill: white; -fx-font-weight: bold");
        lblNgayKetThuc.setStyle("-fx-font-size:15; -fx-text-fill: white; -fx-font-weight: bold");

        vboxNgay.getChildren().addAll(lblNgayBatDau, lblNgayKetThuc);
        vboxNgay.setMinHeight(40);

        hboxNgayApDung.getChildren().addAll(iconLich, vboxNgay);

        hboxNgayApDung.setAlignment(Pos.CENTER);
        vboxNgay.setAlignment(Pos.CENTER_LEFT);

        stBenPhai.setStyle("-fx-background-radius: 8; -fx-background-color: #082744");
        stBenPhai.getChildren().addAll(hboxNgayApDung);

        ///HBOX all
        HBox hboxAll = new HBox(5);
        hboxAll.getChildren().addAll(stBenTrai, stBenPhai);
        hboxAll.getStyleClass().add("hbox-shadow");

        return hboxAll;
    }

    public VBox taoPhanBenPhai() {
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
        Region spacer6 = new Region();
        HBox.setHgrow(spacer6, Priority.ALWAYS);
        Region spacer8 = new Region();
        HBox.setHgrow(spacer8, Priority.ALWAYS);
        Region spacer9 = new Region();
        HBox.setHgrow(spacer9, Priority.ALWAYS);
        Region spacer7 = new Region();
        HBox.setHgrow(spacer7, Priority.ALWAYS);

        // Tiêu đề - 1
        Label lblTieuDeKhuyenMai = new Label("Thông Tin Khyến Mãi");
        lblTieuDeKhuyenMai.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");

        //Ma khuyen mai
        Label lblMaKhuyenMai = new Label("Mã khuyến mãi:");
        lblMaKhuyenMai.getStyleClass().add("fontTieuDeNho");
        TextField txtMaKhuyenMai = new TextField();
        txtMaKhuyenMai.setEditable(false);
        txtMaKhuyenMai.setPrefWidth(250);
        txtMaKhuyenMai.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #D9D9D9; -fx-font-size: 15");
        HBox hbox1 = new HBox(lblMaKhuyenMai, spacer1, txtMaKhuyenMai );
        hbox1.setPadding(new Insets(5));
        hbox1.setPadding(new Insets(0, 30, 0, 0));


        //Ten khuyen mai
        Label lblTenKhuyenMai = new Label("Tên khuyến mãi:");
        lblTenKhuyenMai.getStyleClass().add("fontTieuDeNho");
        TextField txtTenKhuyenMai = new TextField();
        txtTenKhuyenMai.setText("Hồ Vạn Thương");
        txtTenKhuyenMai.setPrefWidth(250);
        txtTenKhuyenMai.getStyleClass().add("textField-guiKhuyenMai");
        HBox hbox2 = new HBox(lblTenKhuyenMai, spacer2, txtTenKhuyenMai );
        hbox2.setPadding(new Insets(5));
        hbox2.setPadding(new Insets(0, 30, 0, 0));

        //Giảm giá phần trăm
        Label lblGiamGiaPhanTram = new Label("Giảm giá phần trăm:");
        lblGiamGiaPhanTram.getStyleClass().add("fontTieuDeNho");
        RadioButton radioCo = new RadioButton("Có");
        RadioButton radioKhong = new RadioButton("Không");
        ToggleGroup radioGroup1 = new ToggleGroup();
        radioCo.setToggleGroup(radioGroup1);
        radioKhong.setToggleGroup(radioGroup1);
        radioCo.getStyleClass().add("radio-button");
        radioKhong.getStyleClass().add("radio-button");
        radioCo.setSelected(true);

        HBox hboxRadio1 = new HBox(5);
        hboxRadio1.setPrefWidth(250);
        Region spaceRadio1 = new Region();
        spaceRadio1.setPrefWidth(30);
        hboxRadio1.getChildren().addAll(radioCo, spaceRadio1,radioKhong);

        HBox hbox3 = new HBox(5);
        hbox3.getChildren().addAll(lblGiamGiaPhanTram, spacer3, hboxRadio1);

        //Giá trị giảm
        Label lblGiaTriGiam = new Label("Giá trị giảm:");
        lblGiaTriGiam.getStyleClass().add("fontTieuDeNho");
        TextField txtGiaTriGiam = new TextField();
        txtGiaTriGiam.setPrefWidth(250);
        txtGiaTriGiam.getStyleClass().add("textField-guiKhuyenMai");
        Label lblDonVi = new Label("VND");
        lblDonVi.setPrefWidth(30);
        lblDonVi.setPrefHeight(35);
        lblDonVi.setAlignment(Pos.BOTTOM_RIGHT);
        if(radioCo.isSelected()) {
            lblDonVi.setText("%");
        }
        lblDonVi.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 10px; -fx-font-weight: bold ;");

        HBox hbox4 = new HBox(lblGiaTriGiam, spacer4, txtGiaTriGiam , lblDonVi);


        //Giá trị tối đa
        Label lblGiaTriToiDa = new Label("Giá trị tối đa:");
        lblGiaTriToiDa.getStyleClass().add("fontTieuDeNho");
        TextField txtGiaTriToiDa = new TextField();
        txtGiaTriToiDa.setText("1.000.000");
        txtGiaTriToiDa.setPrefWidth(250);
        txtGiaTriToiDa.getStyleClass().add("textField-guiKhuyenMai");
        Label lblDonViVND1 = new Label("VND");
        lblDonViVND1.setPrefWidth(30);
        lblDonViVND1.setPrefHeight(35);
        lblDonViVND1.setAlignment(Pos.BOTTOM_RIGHT);
        lblDonViVND1.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 10px; -fx-font-weight: bold;");

        HBox hbox5 = new HBox(lblGiaTriToiDa, spacer5, txtGiaTriToiDa, lblDonViVND1);

        if(!radioCo.isSelected()) {
            hbox5.setVisible(false);
            hbox5.setManaged(false);
        }

        // cai dat group 1
        radioGroup1.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal == radioCo) {
                lblDonVi.setText("%");
                fadeIn(hbox5);
            } else {
                lblDonVi.setText("VND");
                fadeOut(hbox5);
            }
        });


        //Loại áp dụng
        Label lblLoaiApDung = new Label("Loại áp dụng:");
        lblLoaiApDung.getStyleClass().add("fontTieuDeNho");
        RadioButton radioHoaDon = new RadioButton("Hóa đơn");
        RadioButton radioMonAn = new RadioButton("Món ăn");
        ToggleGroup radioGroup2 = new ToggleGroup();
        radioHoaDon.setToggleGroup(radioGroup2);
        radioMonAn.setToggleGroup(radioGroup2);
        radioHoaDon.getStyleClass().add("radio-button");
        radioMonAn.getStyleClass().add("radio-button");

        HBox hboxRadio2 = new HBox(5);
        hboxRadio2.setPrefWidth(250);
        Region spaceRadio2 = new Region();
        spaceRadio2.setPrefWidth(30);
        hboxRadio2.getChildren().addAll(radioHoaDon, spaceRadio2,radioMonAn);

        HBox hbox6 = new HBox(5);
        hbox6.getChildren().addAll(lblLoaiApDung, spacer6, hboxRadio2);

        //Dieu kiện áp dụng
        Label lblDieuKienApDung = new Label("Điều kiện áp dụng:");
        lblDieuKienApDung.getStyleClass().add("fontTieuDeNho");
        TextField txtDieuKienApDung = new TextField();
        txtDieuKienApDung.setText("Hồ Vạn Thương");
        txtDieuKienApDung.setPrefWidth(250);
        txtDieuKienApDung.setStyle("-fx-background-color: white");
        Label lblDonViVND2 = new Label("VND");
        lblDonViVND2.setPrefWidth(30);
        lblDonViVND2.setPrefHeight(35);
        lblDonViVND2.setAlignment(Pos.BOTTOM_RIGHT);
        lblDonViVND2.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 10px; -fx-font-weight: bold;");

        ImageView iconLonHonBang = new ImageView(IMG_LON_HON_HOAC_BANG);
        iconLonHonBang.setFitHeight(20);
        iconLonHonBang.setFitWidth(20);

        StackPane stDieuKien = new StackPane(iconLonHonBang, txtDieuKienApDung);
        stDieuKien.setAlignment(Pos.CENTER_LEFT);
        StackPane.setMargin(txtDieuKienApDung, new Insets(0, 0, 0, 20));
        stDieuKien.getStyleClass().add("textField-guiKhuyenMai");

        HBox hbox7 = new HBox(lblDieuKienApDung, spacer7, stDieuKien, lblDonViVND2);

        radioHoaDon.setSelected(true);
        if(radioMonAn.isSelected()) {
            hbox7.setVisible(false);
            hbox7.setManaged(false);
        }

        // Vbox phan 1
        VBox vbox1 = new VBox(10);
        vbox1.getChildren().addAll(lblTieuDeKhuyenMai,hbox1, hbox2, hbox3, hbox4, hbox5, hbox6, hbox7);
        vbox1.setPadding(new Insets(0, 20, 0, 20));

        // Tiêu đề - 2
        Label lblTieuDeNgayApDung = new Label("Ngày Áp Dụng");
        lblTieuDeNgayApDung.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");

        //Ngay bat dau
        Label lblNgayBatDau = new Label("Ngày bắt đầu:");
        lblNgayBatDau.getStyleClass().add("fontTieuDeNho");
        DatePicker dateBatDau = new DatePicker();
        dateBatDau.setPrefWidth(250);
        dateBatDau.getStyleClass().add("date-picker");

        HBox hbox8 = new HBox(lblNgayBatDau, spacer8, dateBatDau);
        hbox8.setPadding(new Insets(0, 30, 0, 0));

        //Ngay ket thuc
        Label lblNgayKetThuc = new Label("Ngày kết thúc:");
        lblNgayKetThuc.getStyleClass().add("fontTieuDeNho");
        DatePicker dateKetThuc = new DatePicker();
        dateKetThuc.setPrefWidth(250);
        dateKetThuc.getStyleClass().add("date-picker");

        HBox hbox9 = new HBox(lblNgayKetThuc, spacer9, dateKetThuc);
        hbox9.setPadding(new Insets(0, 30, 0, 0));

        // Khu vuc khuyen mai mon an

        VBox vboxKhuyenMaiMonAn = new VBox(5);

        //Button chọn món khuyến mãi
        ImageView iconDauCong = new ImageView(IMG_DAU_CONG_DEN);
        iconDauCong.setFitHeight(30);
        iconDauCong.setFitWidth(30);

        Button btnChonMonAn = new Button("Chọn món khuyến mãi", iconDauCong);
        btnChonMonAn.setMinHeight(30);
        btnChonMonAn.setMinWidth(100);
        btnChonMonAn.getStyleClass().add("button-chonMon");

        // BUTTON CHON MON
        // TẠO MODAL HIỆN RA
        btnChonMonAn.setOnAction(e -> {
            Stage stageChinh = (Stage) btnChonMonAn.getScene().getWindow(); // Lấy stage chính
            Parent rootChinh = stageChinh.getScene().getRoot();

            // Hiệu ứng mờ
            BoxBlur blur = new BoxBlur(5, 5, 3);
            rootChinh.setEffect(blur);

            //Khoi tao modal
            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Chọn món ăn khuyến mãi");
            modal.initModality(Modality.WINDOW_MODAL);
            //Layout

            BorderPane layout = new BorderPane();
            layout.setStyle("-fx-background-color: white");

            //Tạo phần bên trái modal thêm món ăn
            VBox vboxPhanTrai = taoPhanBenTraiModal();
            layout.setCenter(vboxPhanTrai);

            // Tạo phần bên phải modal thêm món ăn
            VBox vboxPhanPhai = taoPhanBenPhaiModal(modal);
            layout.setRight(vboxPhanPhai);


            //Scene
            Scene scene = new Scene(layout,  900, 600);
            scene.getStylesheets().add(getClass().getResource("/css/qlkm.css").toExternalForm());
            modal.setScene(scene);
            modal.showAndWait();

            // Khi modal đóng, xóa blur
            rootChinh.setEffect(null);
        });


        // Bảng món ăn
        TableView<MonAn> table = new TableView<>();

        TableColumn<MonAn, String> colMaMon = new TableColumn<>("Mã món");
        colMaMon.setCellValueFactory(new PropertyValueFactory<>("maMonAn"));
        colMaMon.setPrefWidth(80);

        TableColumn<MonAn, String> colTenMon = new TableColumn<>("Tên món");
        colTenMon.setCellValueFactory(new PropertyValueFactory<>("tenMonAn"));
        colTenMon.setPrefWidth(200);

        TableColumn<MonAn, Double> colGiaTien = new TableColumn<>("Giá tiền");
        colGiaTien.setCellValueFactory(new PropertyValueFactory<>("giaTien"));
        colGiaTien.setPrefWidth(120);

        TableColumn<MonAn, Double> colGiaSauKM = new TableColumn<>("Giá sau khuyến mãi");
        colGiaSauKM.setCellValueFactory(cellData -> new SimpleObjectProperty<>(100000.0));
        colGiaSauKM.setMinWidth(150);


        // Format giá tiền
        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        colGiaTien.setCellFactory(tc -> new TableCell<MonAn, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price) + "đ");
                    setStyle("-fx-font-weight: bold; -fx-text-fill: #555;");
                }
            }
        });
        colGiaSauKM.setCellFactory(tc -> new TableCell<MonAn, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price) + "đ");
                    setStyle("-fx-font-weight: bold; -fx-text-fill: #555;");
                }
            }
        });

        // Thêm cột vào bảng
        table.getColumns().addAll(colMaMon, colTenMon, colGiaTien, colGiaSauKM);

        // Thêm dữ liệu mẫu
        table.getItems().addAll(
                new MonAn("M01", "Cơm gà ớt tỏi", "mon an",1000000, "test" ,"tsts")
        );

        table.setPrefHeight(100);
        table.getStyleClass().add("table-view");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        vboxKhuyenMaiMonAn.getChildren().addAll(btnChonMonAn, table);
        //
        if(radioHoaDon.isSelected()) {
            vboxKhuyenMaiMonAn.setVisible(false);
            vboxKhuyenMaiMonAn.setManaged(false);
        }

        // cai dat group 2
        radioGroup2.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal == radioHoaDon) {
                fadeIn(hbox7);
                fadeOut(vboxKhuyenMaiMonAn);
            } else {
                fadeOut(hbox7);
                fadeIn(vboxKhuyenMaiMonAn);
            }
        });


        //Vbox phan 2
        VBox vbox2 = new VBox(10);
        vbox2.getChildren().addAll(lblTieuDeNgayApDung, hbox8, hbox9, vboxKhuyenMaiMonAn);
        vbox2.setPadding(new Insets(0, 20, 0, 20));

        //Phần nút
        Button btnThem = new Button("Thêm");
        Button btnLuu = new Button("Lưu");

        btnThem.getStyleClass().add("btn-them");
        btnLuu.getStyleClass().add("btn-luu");

        HBox hboxButton = new HBox();
        Region spaceButton = new Region();
        HBox.setHgrow(spaceButton, Priority.ALWAYS);

        hboxButton.getChildren().addAll(btnThem, spaceButton, btnLuu);
        hboxButton.setPadding(new Insets(0, 80, 0, 80));

        /////
        VBox vboxAll = new VBox(30);
        vboxAll.getChildren().addAll(vbox1, vbox2, hboxButton);
        vboxAll.setPadding(new Insets(30, 0, 30, 0));

        return vboxAll;
    }

    public VBox taoPhanBenTraiModal() {
        //VBox benTrai
        VBox vboxBenTrai = new VBox(8);
        vboxBenTrai.setPadding(new Insets(20,10,20,10));
        vboxBenTrai.setPrefWidth(550);

        // Biến bên trái
        Label lblTieuDeChonMon = new Label("Chọn món giảm giá");
        HBox hboxChuThich = new HBox(10);
        Label lblDanhSach = new Label("Danh sách món");
        HBox hboxChuaGiamGia = new HBox(2);
        ImageView iconChuaGiamGia = new ImageView(IMG_CHU_NHAT_XANH);
        Label lblChuaGiamGia = new Label("Chưa giảm giá");
        HBox hboxDaGiamGia = new HBox(2);
        ImageView iconDaGiamGia = new ImageView(IMG_CHU_NHAT_VANG);
        Label lblDaGiamGia = new Label("Đã giảm giá");
        ComboBox<String> cboLoc = new ComboBox<String>();
        cboLoc.getItems().addAll("Tất cả","Đã giảm giá","Chưa giảm giá");

        //Tieu de
        lblTieuDeChonMon.setStyle("-fx-font-size: 30px; -fx-font-family: 'Tai Heritage Pro'; -fx-font-weight: bold");
        vboxBenTrai.getChildren().add(lblTieuDeChonMon);

        // Danh sách chú thích
        lblDanhSach.getStyleClass().add("fontTieuDeNho");
        lblChuaGiamGia.getStyleClass().add("fontTieuDeNho");
        lblDaGiamGia.getStyleClass().add("fontTieuDeNho");
        iconChuaGiamGia.setFitHeight(10);
        iconChuaGiamGia.setFitWidth(15);
        iconDaGiamGia.setFitHeight(10);
        iconDaGiamGia.setFitWidth(15);
        hboxChuaGiamGia.getChildren().addAll(iconChuaGiamGia, lblChuaGiamGia);
        hboxChuaGiamGia.setAlignment(Pos.CENTER_LEFT);
        hboxDaGiamGia.getChildren().addAll(iconDaGiamGia, lblDaGiamGia);
        hboxDaGiamGia.setAlignment(Pos.CENTER_LEFT);
        hboxChuThich.getChildren().addAll(lblDanhSach, hboxChuaGiamGia, hboxDaGiamGia, cboLoc);
        hboxChuThich.setAlignment(Pos.CENTER_LEFT);
        vboxBenTrai.getChildren().add(hboxChuThich);
        cboLoc.getStyleClass().add("combo-box");
        cboLoc.getSelectionModel().selectFirst();

        //Danh sách món ăn giảm giá

        for(int i = 0; i < 20; i++) {
            dsMonAn.add(new MonAn("M" + (i+1), "Cơm gà ớt tỏi", "mon an",1000000, "test" ,"JSJJSj"));
        }
        ListView<MonAn> listMonAn = taoDanhSachMonBenTrai();
        listMonAn.setFixedCellSize(-1);

        //
        vboxBenTrai.getChildren().add(listMonAn);

        return vboxBenTrai;
    }

    public VBox taoPhanBenPhaiModal(Stage modal) {
        //Vbox bên phải
        VBox vboxBenPhai = new VBox(20);
        vboxBenPhai.setPrefWidth(350);
        vboxBenPhai.setPadding(new Insets(50, 10, 50, 10));

        //Biến bên phải
        HBox hboxTieuDe = new HBox();
        Label lblMonDaChon = new Label("Món đã chọn");
        ListView<MonAn> listMonChon = taoDanhSachMonBenPhai();
        HBox hboxButton = new HBox();
        Region spacerButton  = new Region();
        Button btnQuayVe = new Button("Quay về");
        Button btnXong = new Button("Xong");

        //Tạo giao diện
        lblMonDaChon.setStyle("-fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: black");
        hboxTieuDe.getChildren().add(lblMonDaChon);
        hboxTieuDe.setAlignment(Pos.CENTER);
        btnQuayVe.setPrefHeight(50);
        btnQuayVe.setPrefWidth(100);
        btnXong.setPrefHeight(50);
        btnXong.setPrefWidth(100);
        btnQuayVe.setStyle("-fx-background-color: #082744; -fx-font-size: 18; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand");
        btnXong.setStyle("-fx-background-color: green; -fx-font-size: 18; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand");

        hboxButton.setHgrow(spacerButton, Priority.ALWAYS);
        hboxButton.getChildren().addAll(btnQuayVe, spacerButton, btnXong);
        hboxButton.setPadding(new Insets(0, 20, 0, 20));

        //SỰ kiện button
        btnQuayVe.setOnAction(e -> {
            modal.close();
        });

        btnXong.setOnAction(e -> {
            modal.close();
        });


        //
        vboxBenPhai.getChildren().addAll(hboxTieuDe, listMonChon, hboxButton);
        vboxBenPhai.setStyle("-fx-border-width: 0 0 0 1; -fx-border-color: #D9D9D9");

        return vboxBenPhai;
    }

    public ListView<MonAn> taoDanhSachMonBenPhai() {
        //Vbox danh sách món
        ListView<MonAn> listMonAn = new ListView<>(dsMonChon);
        listMonAn.setStyle("-fx-background-color: transparent;");
        listMonAn.setFixedCellSize(80);

        listMonAn.setCellFactory(list -> new ListCell<MonAn>() {
            @Override
            protected void updateItem(MonAn mon, boolean empty) {
                super.updateItem(mon, empty);
                if (empty || mon == null) {
                    setGraphic(null);
                } else {
                    setGraphic(taoMonBenPhai(mon));
                }
            }
        });

        // Trong hàm taoDanhSachMonBenTrai(), thay phần chặn event:
        listMonAn.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            // Kiểm tra target hoặc parent có phải Button không (reuse cho text bên trong Button)
            Node target = (Node) e.getTarget();
            if (!(target instanceof Button || (target.getParent() != null && target.getParent() instanceof Button))) {
                e.consume();  // Chỉ chặn nếu KHÔNG phải Button (hoặc text của Button)
                listMonAn.getSelectionModel().clearSelection();
            }
            // Nếu click Button/text của Button, event tự do → setOnAction hoạt động
        });

        return listMonAn;
    }

    public HBox taoMonBenPhai(MonAn monAn) {
        //HBOx mon la
        HBox hboxMonAn = new HBox(4);

        //Khai báo biến
        HBox hboxBenTrai = new HBox(3);
        ImageView imgMonAn = new ImageView(IMG_MON_AN); // Reuse preloaded image
        VBox vboxThongTinMon = new VBox(5);
        Label lblTenMon = new Label("Món nào đó");
        HBox hboxGia = new HBox(3);
        Label lblSau = new Label();
        VBox vboxBenPhai = new VBox(3);
        Button btnXoa = new Button("Xóa");

        // Cai dat event
        btnXoa.setOnAction(e ->{
            dsMonChon.remove(monAn);
            dsMonAn.add(monAn);
        });

        //Set giá trị
        DecimalFormat fomat = new DecimalFormat("#,### VND");
        lblTenMon.setText(monAn.getTenMonAn());
        lblSau.setText(fomat.format(monAn.getGiaTien()));

        //Tạo món ăn
        //Trái
        hboxBenTrai.setPrefWidth(220);
        hboxBenTrai.setPrefHeight(50);
        imgMonAn.setFitHeight(40);
        imgMonAn.setFitWidth(40);
        imgMonAn.getStyleClass().add("img-MonAn");
        lblTenMon.setStyle("-fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 12px; -fx-font-weight: bold");
        lblTenMon.setAlignment(Pos.CENTER_LEFT);
        lblSau.setStyle("-fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: red");
        hboxGia.getChildren().addAll(lblSau);
        hboxGia.setAlignment(Pos.CENTER_RIGHT);
        vboxThongTinMon.getChildren().addAll(lblTenMon, hboxGia);
        vboxThongTinMon.setAlignment(Pos.CENTER_LEFT);
        vboxThongTinMon.setPadding(new Insets(0,0,0,5));
        hboxBenTrai.getStyleClass().add("monAnTrai");
        hboxBenTrai.getChildren().addAll(imgMonAn, vboxThongTinMon);
        hboxBenTrai.setAlignment(Pos.CENTER_LEFT);
        hboxBenTrai.setPadding(new Insets(0,0,0,5));

        //Phải
        vboxBenPhai.setPrefWidth(80);
        vboxBenPhai.setPrefHeight(50);
        btnXoa.setStyle("-fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 13px; -fx-text-fill: black; -fx-background-color: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand");
        vboxBenPhai.getChildren().addAll(btnXoa);
        vboxBenPhai.setStyle("-fx-background-color: #082744; -fx-background-radius: 10");
        vboxBenPhai.setAlignment(Pos.CENTER);

        hboxMonAn.getChildren().addAll(hboxBenTrai, vboxBenPhai);
        hboxMonAn.getStyleClass().add("monAn");
        //
        return hboxMonAn;
    }

    public HBox taoMonBenTrai(MonAn monAn) {
        //HBox mon an
        HBox hboxMonAn = new HBox(10);

        //Khai báo biến
        HBox hboxBenTrai = new HBox(3);
        ImageView imgMonAn = new ImageView(IMG_MON_AN); // Reuse preloaded image
        VBox vboxThongTinMon = new VBox(5);
        Label lblTenMon = new Label("Món nào đó");
        HBox hboxGia = new HBox(3);
        Label lblBanDau = new Label("Giá tiền ...VND");
        Label lblSau = new Label();
        VBox vboxBenPhai = new VBox(3);
        Label lblGiamHayChua = new Label("Chưa giảm giá");
        Button btnThemMon = new Button("Thêm");
        Label lblTenKhuyenMai = new Label("...");

        //Set gia tri
        DecimalFormat fomat = new DecimalFormat("#,### VND");
        lblTenMon.setText(monAn.getTenMonAn());
        lblSau.setText(fomat.format(monAn.getGiaTien()));
        //---


        //Trái
        hboxBenTrai.setPrefWidth(350);
        hboxBenTrai.setPrefHeight(100);
        imgMonAn.setFitHeight(90);
        imgMonAn.setFitWidth(90);
        imgMonAn.getStyleClass().add("img-MonAn");
        lblTenMon.setStyle("-fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 20px; -fx-font-weight: bold");
        lblTenMon.setAlignment(Pos.CENTER_LEFT);
        lblBanDau.setStyle("-fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 10; -fx-strikethrough: true;");
        lblSau.setStyle("-fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: red");
        hboxGia.getChildren().addAll(lblBanDau, lblSau);
        hboxGia.setAlignment(Pos.CENTER_RIGHT);
        vboxThongTinMon.getChildren().addAll(lblTenMon, hboxGia);
        vboxThongTinMon.setAlignment(Pos.CENTER_LEFT);
        vboxThongTinMon.setPadding(new Insets(0,0,0,5));
        hboxBenTrai.getStyleClass().add("monAnTrai");
        hboxBenTrai.getChildren().addAll(imgMonAn, vboxThongTinMon);
        hboxBenTrai.setAlignment(Pos.CENTER_LEFT);
        hboxBenTrai.setPadding(new Insets(0,0,0,5));

        //Cài đặt event
        btnThemMon.setOnAction(e ->{
            dsMonAn.remove(monAn);
            dsMonChon.add(monAn);
        });

        //Phải
        vboxBenPhai.setPrefWidth(150);
        vboxBenPhai.setPrefHeight(100);
        lblGiamHayChua.setStyle("-fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold");
        btnThemMon.setStyle("-fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 20px; -fx-text-fill: black; -fx-background-color: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand");
        lblTenKhuyenMai.setStyle("-fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 12px; -fx-text-fill: black");
        vboxBenPhai.getChildren().addAll(lblGiamHayChua, btnThemMon);
        vboxBenPhai.setStyle("-fx-background-color: #082744; -fx-background-radius: 10");
        vboxBenPhai.setAlignment(Pos.CENTER);

        //
        hboxMonAn.getChildren().addAll(hboxBenTrai, vboxBenPhai);
        hboxMonAn.getStyleClass().add("monAn");

        return hboxMonAn;
    }

    public ListView<MonAn> taoDanhSachMonBenTrai() {
        ListView<MonAn> listMonAn = new ListView<>(dsMonAn);
        listMonAn.setStyle("-fx-background-color: transparent;");
        listMonAn.setFixedCellSize(110);

        listMonAn.setCellFactory(list -> new ListCell<MonAn>() {
            @Override
            protected void updateItem(MonAn mon, boolean empty) {
                super.updateItem(mon, empty);
                if (empty || mon == null) {
                    setGraphic(null);
                } else {
                    setGraphic(taoMonBenTrai(mon));
                }
            }
        });

        // Trong hàm taoDanhSachMonBenTrai(), thay phần chặn event:
        listMonAn.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            // Kiểm tra target hoặc parent có phải Button không (reuse cho text bên trong Button)
            Node target = (Node) e.getTarget();
            if (!(target instanceof Button || (target.getParent() != null && target.getParent() instanceof Button))) {
                e.consume();  // Chỉ chặn nếu KHÔNG phải Button (or text của Button)
                listMonAn.getSelectionModel().clearSelection();
            }
            // Nếu click Button/text của Button, event tự do → setOnAction hoạt động
        });


        return listMonAn;
    }


    // ANIMATION
    // Fade in: Hiện dần từ mờ đến rõ
    private void fadeIn(Node node) {
        if (node == null) return;

        node.setVisible(true);
        node.setManaged(true);
        node.setOpacity(0.0);  // Bắt đầu từ mờ

        FadeTransition fade = new FadeTransition(Duration.millis(300), node);
        fade.setFromValue(0.0);  // Từ mờ
        fade.setToValue(1.0);    // Đến rõ
        fade.play();
    }

    // Fade out: Ẩn dần từ rõ đến mờ
    private void fadeOut(Node node) {
        if (node == null) return;

        FadeTransition fade = new FadeTransition(Duration.millis(300), node);
        fade.setFromValue(1.0);  // Từ rõ
        fade.setToValue(0.0);    // Đến mờ
        fade.setOnFinished(e -> {
            node.setVisible(false);
            node.setManaged(false);
            node.setOpacity(1.0);  // Reset để lần sau OK
        });
        fade.play();
    }


}