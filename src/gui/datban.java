package gui;

import java.util.List;
import java.text.DecimalFormat;
import dao.BanAn_DAO;
import dao.KhachHang_DAO;
import dao.MonAn_DAO;
import dao.PhieuDatBan_DAO; // Cần thêm import này
import dao.HoaDon_DAO;       // Cần thêm import này
import dao.ChiTietHoaDon_DAO;
import entity.KhachHang;
import entity.MonAn;
import entity.NhanVien;
import entity.PhieuDatBan;
import ConnectDB.ConnectDB;
import entity.BanAn;
import entity.TrangThai;
import gui.Gui_DanhSachBan;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import javafx.scene.control.Alert.AlertType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
import lib.ImageCacheManager;

public class datban extends BorderPane {
    private MonAn_DAO monAnDAO;
    private KhachHang_DAO khachHangDAO;
    private BanAn_DAO banAn_DAO;
    private HoaDon_DAO hoaDonDAO;
    private PhieuDatBan_DAO phieuDatBanDAO;
    private ChiTietHoaDon_DAO chiTietHoaDonDAO;

    private ObservableList<BanAn> dsBanDaChon;
    private ObservableList<ChiTietHoaDon> dsMonDaChon;
    private final DecimalFormat df = new DecimalFormat("###,###đ");
    private BorderPane mainLayout;
    private List<BanAn> cacBanDuocChon;
    private Label lblTongCoc;
    private Label lblTongTienMon;
    private TilePane menuTilePane;
    private TextField txtTimKiemSdt;
    private Button btnTimKiem;
    private Label lblTrangThaiTimKiem;
    private TextField txtMaKh;
    private TextField txtTenKH;
    private TextField txtSdt;
    private TextField txtDiem;
    private DatePicker dpNgayDen;
    private ComboBox<String> cmbGioDen;
    private LocalDate ngayDatBan;
    private TextField txtSoNguoi;
    private RadioButton radioDungNgay;
    private RadioButton radioDatTruoc;

    public datban(BorderPane mainLayout, List<BanAn> cacBanDaChon, LocalDate ngayDatBan) {
        this.mainLayout = mainLayout;
        this.cacBanDuocChon = cacBanDaChon;
        this.ngayDatBan = ngayDatBan;

        monAnDAO = new MonAn_DAO();
        khachHangDAO = new KhachHang_DAO();
        banAn_DAO = new BanAn_DAO();
        hoaDonDAO = new HoaDon_DAO();
        phieuDatBanDAO = new PhieuDatBan_DAO();
        chiTietHoaDonDAO = new ChiTietHoaDon_DAO();

        dsBanDaChon = FXCollections.observableArrayList();
        dsMonDaChon = FXCollections.observableArrayList();
        initialize();
        taiDuLieuBanDau();
    }

    private void initialize() {
        this.setStyle("-fx-background-color: #F7FAFC;");
        HBox mainContent = new HBox();
        VBox phanBenTrai = taoPhanBenTrai();
        Separator separator = new Separator(Orientation.VERTICAL);
        VBox phanBenPhai = taoPhanBenPhai();
        mainContent.getChildren().addAll(phanBenTrai, separator, phanBenPhai);
        this.setCenter(mainContent);
        try {
            this.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Không tìm thấy CSS: /application/application.css");
        }
    }

    private VBox taoPhanBenTrai() {
        VBox vbox = new VBox(0);
        vbox.setPadding(Insets.EMPTY);
        vbox.setPrefWidth(500);
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

        Label tieuDeKH = new Label("Thông Tin Khách Hàng");
        tieuDeKH.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");

        txtTimKiemSdt = new TextField();
        txtTimKiemSdt.setPromptText("Nhập số điện thoại khách hàng");
        txtTimKiemSdt.getStyleClass().add("timKiem");
        txtTimKiemSdt.setPrefWidth(300);
        txtTimKiemSdt.setStyle("-fx-font-size: 15; -fx-padding: 8;");

        btnTimKiem = new Button("Tìm kiếm");
        btnTimKiem.getStyleClass().add("button-timKiem");
        btnTimKiem.setPrefWidth(120);

        lblTrangThaiTimKiem = new Label("Tìm thấy khách hàng");
        lblTrangThaiTimKiem.setStyle("-fx-text-fill: #38A169; -fx-font-weight: bold;");

        HBox boxTimKiem = new HBox(10, txtTimKiemSdt, btnTimKiem);
        boxTimKiem.setAlignment(Pos.CENTER_LEFT);
        boxTimKiem.setPadding(new Insets(5, 0, 5, 0));

        // Khởi tạo thuộc tính lớp
        txtMaKh = new TextField();
        txtTenKH = new TextField();
        txtSdt = new TextField();
        txtDiem = new TextField();

        // Áp dụng Style và Editable
        String styleFixed = "-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 12; -fx-background-color: #D9D9D9; -fx-font-size: 15";
        String styleEditable = "-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-background-color: white; -fx-border-color: #082744; -fx-border-width: 1px; -fx-border-radius: 3; -fx-font-size: 15";

        // Thiết lập thuộc tính ban đầu
        txtMaKh.setEditable(false);
        txtMaKh.setPrefWidth(250);
        txtMaKh.setStyle(styleFixed);

        txtTenKH.setEditable(true);
        txtTenKH.setPrefWidth(250);
        txtTenKH.setStyle(styleEditable);

        txtSdt.setEditable(true);
        txtSdt.setPrefWidth(250);
        txtSdt.setStyle(styleEditable);

        txtDiem.setEditable(false);
        txtDiem.setPrefWidth(250);
        txtDiem.setStyle(styleFixed);

        Label lblMaKH = new Label("Mã khách hàng:");
        lblMaKH.getStyleClass().add("fontTieuDeNho");
        HBox hbox1 = new HBox(lblMaKH, spacer1, txtMaKh);
        hbox1.setPadding(new Insets(5));

        Label lblTenKH = new Label("Tên khách hàng:");
        lblTenKH.getStyleClass().add("fontTieuDeNho");
        HBox hbox2 = new HBox(lblTenKH, spacer2, txtTenKH);
        hbox2.setPadding(new Insets(5));

        Label lblSdt = new Label("Số điện thoại:");
        lblSdt.getStyleClass().add("fontTieuDeNho");
        HBox hbox3 = new HBox(lblSdt, spacer3, txtSdt);
        hbox3.setPadding(new Insets(5));

        Label lblDiem = new Label("Điểm tích lũy:");
        lblDiem.getStyleClass().add("fontTieuDeNho");
        HBox hbox4 = new HBox(lblDiem, spacer4, txtDiem);
        hbox4.setPadding(new Insets(5));

        txtMaKh.setText("000");
        txtTenKH.setText("");
        txtSdt.setText("");
        txtDiem.setText("0");

        /// VBOX ALL - 1
        VBox vboxAll1 = new VBox(5);

        vboxAll1.getChildren().addAll(tieuDeKH, boxTimKiem, lblTrangThaiTimKiem, hbox1, hbox2, hbox3, hbox4);
        btnTimKiem.setOnAction(e -> timKiemKhachHang());

        Label lblThongTinDatBan = new Label("Thông Tin Đặt Bàn");
        lblThongTinDatBan.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");

        Label lblNgayGio = new Label("Ngày giờ đến:");
        lblNgayGio.getStyleClass().add("fontTieuDeNho");

        cmbGioDen = new ComboBox<>();
        //  từ 00:00 đến 23:30
        for (int h = 0; h < 24; h++) {
            cmbGioDen.getItems().add(String.format("%02d:00", h));
            cmbGioDen.getItems().add(String.format("%02d:30", h));
        }

        cmbGioDen.setValue("18:00"); // Giờ mặc định
        cmbGioDen.setPrefWidth(110);
        cmbGioDen.setStyle("-fx-font-size: 15; -fx-background-radius: 3 0 0 3");

        dpNgayDen = new DatePicker(ngayDatBan); // GÁN NGÀY MẶC ĐỊNH TỪ CONSTRUCTOR
        dpNgayDen.setPrefWidth(140);
        dpNgayDen.setStyle("-fx-font-size: 15; -fx-background-radius: 0 3 3 0");


        HBox hboxNgayGio = new HBox(0); // Khoảng cách giữa 0 để chúng dính liền
        hboxNgayGio.getChildren().addAll(cmbGioDen, dpNgayDen);
        hboxNgayGio.setAlignment(Pos.CENTER_LEFT);

        cmbGioDen.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-background-color: #D9D9D9; -fx-font-size: 15; -fx-background-radius: 3 0 0 3");
        dpNgayDen.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-background-color: #D9D9D9; -fx-font-size: 15; -fx-background-radius: 3 0 0 3");

        HBox hbox5 = new HBox(lblNgayGio, spacer5, hboxNgayGio);
        hbox5.setPadding(new Insets(5));
// So nguoi
        Label lblSoNguoi = new Label("Số người:");
        lblSoNguoi.getStyleClass().add("fontTieuDeNho");
        txtSoNguoi = new TextField("0");
        txtSoNguoi.setEditable(false);
        txtSoNguoi.setPrefWidth(250);
        txtSoNguoi.setStyle(
                "-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-background-color: #D9D9D9; -fx-font-size: 15; ");
        HBox hbox6 = new HBox(lblSoNguoi, spacer6, txtSoNguoi);
        hbox6.setPadding(new Insets(5));
// Kiểu đặt bàn
        Label lblKieuDatBan = new Label("Kiểu đặt bàn");
        lblKieuDatBan.getStyleClass().add("fontTieuDeNho");
        radioDatTruoc = new RadioButton("Đặt trước");
        radioDungNgay = new RadioButton("Dùng ngay");
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
// Vbox All
        VBox vboxALL = new VBox(25);
        vboxALL.getChildren().addAll(vboxAll1, vboxAll2);
        vboxALL.setAlignment(Pos.TOP_LEFT);
        vboxALL.setPadding(new Insets(10, 20, 0, 20));
        vboxALL.setMinWidth(500);
        return vboxALL;
    }

    private VBox taoBanDaChon() {
        VBox vbox = new VBox(5);
        Label tieuDe = new Label("Bàn đã chọn");
        tieuDe.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");

        TableView<BanAn> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(200);

        table.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        TableColumn<BanAn, String> colMaBan = new TableColumn<>("Mã bàn");
        colMaBan.setCellValueFactory(new PropertyValueFactory<>("maBan"));
        colMaBan.setPrefWidth(90);

        TableColumn<BanAn, String> colLoai = new TableColumn<>("Loại");
        colLoai.setCellValueFactory(new PropertyValueFactory<>("loai"));
        colLoai.setPrefWidth(110);

        TableColumn<BanAn, Integer> colSoNguoi = new TableColumn<>("Số người");
        colSoNguoi.setCellValueFactory(cellData -> {
            String tenLoai = cellData.getValue().getLoai().getTenLoai();
            int soNguoiToiDa = tenLoai.equalsIgnoreCase("VIP") ? 6 : 4;
            // Trả về thuộc tính kiểu IntegerProperty
            return new javafx.beans.property.SimpleIntegerProperty(soNguoiToiDa).asObject();
        });
        colSoNguoi.setPrefWidth(80);

        TableColumn<BanAn, String> colCoc = new TableColumn<>("Cọc");
        colCoc.setCellValueFactory(cellData -> {
            String tenLoai = cellData.getValue().getLoai().getTenLoai();
            double tienCoc = tenLoai.equalsIgnoreCase("VIP") ? 450000 : 300000;
            return new javafx.beans.property.SimpleStringProperty(df.format(tienCoc));
        });
        colCoc.setPrefWidth(150);

        colMaBan.setSortable(false);
        colLoai.setSortable(false);
        colSoNguoi.setSortable(false);
        colCoc.setSortable(false);
        table.getColumns().addAll(colMaBan, colLoai, colSoNguoi, colCoc);
        table.setItems(dsBanDaChon);
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
        lblTongCoc = new Label("0đ"); // Giá trị mặc định
        lblTongCoc.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #E53E3E;");
        hbox.getChildren().addAll(lblTieuDe, spacer, lblTongCoc); // Dùng lblTongCoc
        return hbox;
    }

    private VBox taoPhanBenPhai() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(15));
        vbox.setStyle("-fx-background-color: white;");
        HBox.setHgrow(vbox, Priority.ALWAYS);
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
        comboLoai.getItems().addAll("Tất cả", "Món ăn kèm", "Món khai vị", "Món chính", "Nước sốt", "Đồ uống",
                "Tráng miệng");
        comboLoai.setValue("Tất cả");
        comboLoai.setPrefWidth(150);
        comboLoai.getStyleClass().add("combo-box-menu");
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(600);
        scrollPane.getStyleClass().add("scroll-pane-menu");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        menuTilePane = new TilePane();
        menuTilePane.setPadding(new Insets(10));
        menuTilePane.setHgap(15);
        menuTilePane.setVgap(15);
        menuTilePane.setPrefColumns(4);
        taiLaiDanhSachMonAn("Tất cả");
        scrollPane.setContent(menuTilePane);
        comboLoai.setOnAction(e -> {
            String loaiDaChon = comboLoai.getValue();
            taiLaiDanhSachMonAn(loaiDaChon);
        });
        HBox comboContainer = new HBox(comboLoai);
        comboContainer.setPadding(new Insets(0, 0, 0, 5));
        vbox.getChildren().addAll(tieuDe, comboContainer, scrollPane);
        return vbox;
    }

    private void taiLaiDanhSachMonAn(String loaiMon) {
        menuTilePane.getChildren().clear();
        List<MonAn> dsMonAn;

        if (loaiMon == null || loaiMon.equals("Tất cả")) {
            dsMonAn = monAnDAO.getAllMonAn();
        } else {
            dsMonAn = monAnDAO.getMonAnByLoai(loaiMon);
        }

        for (MonAn mon : dsMonAn) {
            int soLuongHienTai = 0;

            ChiTietHoaDon cthdDaChon = timMonChon(mon);

            if (cthdDaChon != null) {
                // Lấy số lượng từ thuộc tính soLuong của ChiTietHoaDon
                soLuongHienTai = cthdDaChon.getSoLuong();
            }

            menuTilePane.getChildren().add(taoTheMonAn(mon, soLuongHienTai));
        }
    }

    private VBox taoTheMonAn(MonAn mon, int soLuong) {
        VBox vbox = new VBox(5);
        vbox.setPrefWidth(155);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));
        vbox.setStyle("-fx-background-color: white;" +
                "-fx-background-radius: 20;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");

        // ✅ THÊM IMAGEVIEW HIỂN THỊ ẢNH
        ImageView imgMonAn = new ImageView();
        imgMonAn.setFitWidth(135);
        imgMonAn.setFitHeight(100);
        imgMonAn.setPreserveRatio(true);

        // ✅ Load ảnh từ Supabase
        if (mon.getHinhAnh() != null && !mon.getHinhAnh().isEmpty()) {
            String SUPABASE_BASE_URL = "https://yxemxycygkhxygaydgcl.supabase.co/storage/v1/object/public/image/";

            // Lấy ảnh từ cache (hoặc download nếu chưa có)
            String imagePath = ImageCacheManager.getImagePath(SUPABASE_BASE_URL, mon.getHinhAnh());

            if (imagePath != null) {
                try {
                    Image image = new Image(imagePath);
                    imgMonAn.setImage(image);
                } catch (Exception e) {
                    loadDefaultImage(imgMonAn);
                }
            } else {
                // Không download được → Dùng ảnh mặc định
                loadDefaultImage(imgMonAn);
            }
        } else {
            loadDefaultImage(imgMonAn);
        }

        // Tên món
        Label lblTen = new Label(mon.getTenMonAn());
        lblTen.setWrapText(true);
        lblTen.setStyle("-fx-font-weight: 900; -fx-font-size: 15px; -fx-font-family: 'Times New Roman'; -fx-alignment: CENTER;");
        lblTen.setPrefWidth(140);
        lblTen.setMinHeight(35);

        // Mô tả
        Label lblMoTa = new Label(mon.getMoTa());
        lblMoTa.setWrapText(true);
        lblMoTa.setStyle("-fx-text-fill: #718096; -fx-font-size: 12px; -fx-alignment: CENTER;");
        lblMoTa.setPrefWidth(140);
        lblMoTa.setMinHeight(30);
        if (mon.getMoTa() == null || mon.getMoTa().isEmpty()) {
            lblMoTa.setVisible(false);
        }

        // Giá
        Label lblGia = new Label(df.format(mon.getGiaTien()));
        lblGia.setStyle("-fx-text-fill: #E53E3E; -fx-font-weight: bold; -fx-font-size: 18px; -fx-alignment: CENTER;");
        lblGia.setPrefWidth(140);

        // Nút điều chỉnh số lượng
        Button btnTru = new Button("−");
        btnTru.getStyleClass().add("button-dieu-chinh-menu");

        Label lblSoLuong = new Label(String.valueOf(soLuong));
        lblSoLuong.setPadding(new Insets(0, 10, 0, 10));
        lblSoLuong.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");

        Button btnCong = new Button("+");
        btnCong.getStyleClass().add("button-dieu-chinh-menu");

        btnCong.setOnAction(e -> {
            themMonVaoGio(mon);
            ChiTietHoaDon cthdDaChon = timMonChon(mon);
            if (cthdDaChon != null) {
                lblSoLuong.setText(String.valueOf(cthdDaChon.getSoLuong()));
            }
        });

        btnTru.setOnAction(e -> {
            botMonKhoiGio(mon);
            ChiTietHoaDon cthdConLai = timMonChon(mon);
            int sl = (cthdConLai != null) ? cthdConLai.getSoLuong() : 0;
            lblSoLuong.setText(String.valueOf(sl));
        });

        HBox soLuongBox = new HBox(10, btnCong, lblSoLuong, btnTru);
        soLuongBox.setAlignment(Pos.CENTER);
        soLuongBox.setPadding(new Insets(5, 0, 0, 0));

        // ✅ Thêm tất cả vào VBox (ảnh ở đầu tiên)
        vbox.getChildren().addAll(imgMonAn, lblTen, lblMoTa, lblGia, soLuongBox);

        return vbox;
    }

    private void loadDefaultImage(ImageView imgView) {
        try {
            Image defaultImg = new Image(getClass().getResourceAsStream("/img/default-food.png"));
            imgView.setImage(defaultImg);
        } catch (Exception e) {
            imgView.setStyle("-fx-background-color: #E2E8F0;");
        }
    }
    private ChiTietHoaDon timMonChon(MonAn mon) {
        for (ChiTietHoaDon cthd : dsMonDaChon) {
            if (cthd.getMonAn().getMaMonAn().equals(mon.getMaMonAn())) {
                return cthd;
            }
        }
        return null;
    }

    private void themMonVaoGio(MonAn mon) {
        ChiTietHoaDon monCoSan = timMonChon(mon);
        if (monCoSan != null) {
            monCoSan.setSoLuong(monCoSan.getSoLuong() + 1);
            dsMonDaChon.set(dsMonDaChon.indexOf(monCoSan), monCoSan);
        } else {
            // Tạo ChiTietHoaDon mới
            ChiTietHoaDon cthd = new ChiTietHoaDon();
            cthd.setMonAn(mon);
            cthd.setSoLuong(1);
            dsMonDaChon.add(cthd);
        }
        capNhatTongTienMon();
        taiLaiDanhSachMonAn(null);
    }

    private void botMonKhoiGio(MonAn mon) {
        ChiTietHoaDon monCoSan = timMonChon(mon);
        if (monCoSan != null) {
            monCoSan.setSoLuong(monCoSan.getSoLuong() - 1);
            if (monCoSan.getSoLuong() == 0) {
                dsMonDaChon.remove(monCoSan);
            } else {
                dsMonDaChon.set(dsMonDaChon.indexOf(monCoSan), monCoSan);
            }
            capNhatTongTienMon();
            // Sau khi bớt, cập nhật lại TilePane
            taiLaiDanhSachMonAn(null);
        }
    }

    private void capNhatTongTienMon() {
        double tong = 0;
        for (ChiTietHoaDon cthd : dsMonDaChon) {
            tong += cthd.getMonAn().getGiaTien() * cthd.getSoLuong();
        }
        lblTongTienMon.setText(df.format(tong));
    }

    private VBox taoMonDaChon() {
        VBox vbox = new VBox(10);
        Label tieuDe = new Label("Món đã chọn");
        tieuDe.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");
        vbox.getChildren().add(tieuDe);

        TableView<ChiTietHoaDon> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);

        // 1. Cột Tên món
        TableColumn<ChiTietHoaDon, String> colTen = new TableColumn<>("Tên món");
        colTen.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getMonAn().getTenMonAn()));

        // 2. Cột Số lượng
        TableColumn<ChiTietHoaDon, Integer> colSoLuong = new TableColumn<>("Số lượng");
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));

        // 3. Cột Đơn giá
        TableColumn<ChiTietHoaDon, String> colDonGia = new TableColumn<>("Đơn giá");
        colDonGia.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                df.format(cellData.getValue().getMonAn().getGiaTien())));

        // 4. Cột Tổng
        TableColumn<ChiTietHoaDon, String> colTong = new TableColumn<>("Tổng");
        colTong.setCellValueFactory(cellData -> {
            double tong = cellData.getValue().getMonAn().getGiaTien() * cellData.getValue().getSoLuong();
            return new javafx.beans.property.SimpleStringProperty(df.format(tong));
        });

        table.getColumns().addAll(colTen, colSoLuong, colDonGia, colTong);
        table.setItems(dsMonDaChon); // dsMonDaChon là ObservableList<ChiTietHoaDon>

        vbox.getChildren().addAll(table);
        return vbox;
    }

    private HBox taoTongTienMon() {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        Label lblTieuDe = new Label("Tổng tiền đặt món:");
        lblTieuDe.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        lblTongTienMon = new Label("0đ"); // Giá trị mặc định
        lblTongTienMon.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #E53E3E;");
        hbox.getChildren().addAll(lblTieuDe, spacer, lblTongTienMon); // Dùng lblTongTienMon
        return hbox;
    }

    private void taiDuLieuBanDau() {
        for (BanAn ban : cacBanDuocChon) {
            dsBanDaChon.add(ban);
        }
        capNhatTongCoc();
        capNhatTongSoNguoi();
    }

    private void capNhatTongCoc() {
        double tong = 0;
        for (BanAn ban : dsBanDaChon) { // Lặp qua BanAn
            String tenLoai = ban.getLoai().getTenLoai();
            double tienCoc = tenLoai.equalsIgnoreCase("VIP") ? 450000 : 300000;
            tong += tienCoc;
        }
        lblTongCoc.setText(df.format(tong));
    }

    private void capNhatTongSoNguoi() {
        int tongSoNguoi = 0;

        for (BanAn ban : cacBanDuocChon) {
            int soNguoiToiDa = ban.getLoai().getTenLoai().equalsIgnoreCase("VIP") ? 6 : 4;
            tongSoNguoi += soNguoiToiDa;
        }

        if (txtSoNguoi != null) {
            txtSoNguoi.setText(String.valueOf(tongSoNguoi));
        }
    }

    private HBox taoNutBam() {
        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button btnQuayLai = new Button("Quay lại");
        btnQuayLai.setPrefSize(150, 40);
        btnQuayLai.getStyleClass().add("button-checkin");
        btnQuayLai.setStyle("-fx-background-color: #A0AEC0;" + "-fx-font-weight: bold;" + "-fx-font-size: 20;"
                + "-fx-text-fill: gray;");
        Button btnXacNhan = new Button("Xác nhận đặt bàn");
        btnXacNhan.setPrefSize(200, 40);
        btnXacNhan.getStyleClass().add("button-checkin");
        btnXacNhan.setStyle("-fx-background-color: #2D3748; -fx-text-fill: white; -fx-font-weight: bold;");
        btnQuayLai.setOnAction(e -> {
            mainLayout.setCenter(new Gui_DanhSachBan(mainLayout));
        });

        btnXacNhan.setOnAction(e -> {
            String maHDChung = null;
            boolean success = true;

            try {

                String maKH = txtMaKh.getText();
                KhachHang khachHangDat;
                if (maKH.equals("000")) {
                    // Khách vãng lai
                    khachHangDat = new KhachHang();
                    khachHangDat.setMaKhachHang("000");
                } else {
                    khachHangDat = new KhachHang();
                    khachHangDat.setMaKhachHang(maKH);
                }

                NhanVien nvDangNhap = new NhanVien();
                nvDangNhap.setMaNhanVien("NV001");

                //  Thời gian đến
                LocalDateTime thoiGianDen = dpNgayDen.getValue().atStartOfDay().withHour(Integer.parseInt(cmbGioDen.getValue().substring(0, 2)))
                        .withMinute(Integer.parseInt(cmbGioDen.getValue().substring(3, 5)));
                String ghiChu = "Khách đặt trước - " + txtTenKH.getText();
                int soNguoiTong = Integer.parseInt(txtSoNguoi.getText());
                String maBanDauTien = cacBanDuocChon.get(0).getMaBan();

                // ---------------------------------------------
                // BƯỚC 1: TẠO MỘT HÓA ĐƠN DUY NHẤT
                // ---------------------------------------------

                HoaDon hdChung = new HoaDon();
                hdChung.setKhachHang(khachHangDat);
                hdChung.setNhanVien(nvDangNhap);

                maHDChung = hoaDonDAO.themHoaDon(hdChung);

                if (maHDChung == null) {
                    success = false;
                    showAlert(AlertType.ERROR, "Lỗi DAO", "Không thể tạo Hóa đơn mới.");
                    return;
                }

                // Gán mã HD vừa tạo ngược lại cho Entity HoaDon chung
                hdChung.setMaHoaDon(maHDChung);

                // ---------------------------------------------
                // BƯỚC 2: TẠO PHIẾU ĐẶT BÀN VÀ CẬP NHẬT TRẠNG THÁI CHO TẤT CẢ CÁC BÀN
                // ---------------------------------------------

                for (BanAn ban : cacBanDuocChon) {
                    // A. Cập nhật trạng thái bàn trong CSDL (DA_DAT)
                    boolean updateBan = banAn_DAO.updateTrangThaiBan(ban, TrangThai.DA_DAT);
                    if (!updateBan) {
                        success = false;
                        break;
                    }

                    // B. Tạo Entity Phiếu Đặt Bàn, liên kết với các Entity khác
                    PhieuDatBan pdb = new PhieuDatBan();
                    pdb.setThoiGianBatDau(thoiGianDen);
                    pdb.setTrangThai("Đã đặt");

                    int soNguoiCuaBan = ban.getLoai().getTenLoai().equalsIgnoreCase("VIP") ? 6 : 4;
                    pdb.setSoNguoi(soNguoiCuaBan);

                    pdb.setGhiChu(ghiChu);

                    // Gán Entity
                    pdb.setKhachHang(khachHangDat);
                    pdb.setBan(ban);
                    pdb.setNhanVien(nvDangNhap);
                    pdb.setHoaDon(hdChung);

                    String trangThaiPhieu;
                    if (radioDatTruoc.isSelected()) {
                        trangThaiPhieu = "Đã đặt";
                    } else if (radioDungNgay.isSelected()) {
                        trangThaiPhieu = "Đang dùng";
                    } else {
                        trangThaiPhieu = "Đã đặt";
                    }

                    boolean themPDB = phieuDatBanDAO.themPhieuDatBan(pdb, trangThaiPhieu);
                    if (!themPDB) {
                        success = false;
                        break;
                    }
                }

                // -----------------------------------------------------
                // BƯỚC 3: GHI CHI TIẾT HÓA ĐƠN
                // -----------------------------------------------------
                if (success && !dsMonDaChon.isEmpty()) {
                    for (ChiTietHoaDon cthd : dsMonDaChon) {
                        cthd.setHoaDon(hdChung);

                        boolean themCTHD = chiTietHoaDonDAO.themChiTietHoaDon(cthd);
                        if (!themCTHD) {
                            success = false;
                            break;
                        }
                    }
                }

                // ---------------------------------------------
                // BƯỚC 4: XỬ LÝ KẾT QUẢ VÀ QUAY LẠI
                // ---------------------------------------------
                if (success) {
                    showAlert(AlertType.INFORMATION, "Thành công", "Đã đặt bàn thành công! Mã hóa đơn: " + maHDChung);
                    mainLayout.setCenter(new Gui_DanhSachBan(mainLayout));
                } else {
                    showAlert(AlertType.ERROR, "Lỗi Nghiệp vụ", "Đã xảy ra lỗi khi ghi dữ liệu. Vui lòng kiểm tra lại hệ thống.");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert(AlertType.ERROR, "Lỗi nghiêm trọng", "Đã xảy ra lỗi: " + ex.getMessage());
            }
        });
        hbox.getChildren().addAll(spacer, btnQuayLai, btnXacNhan);
        return hbox;
    }
    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private ImageView createImageViewSafe(String path, double width, double height) {
        try {
            Image img = new Image(getClass().getResourceAsStream(path));
            ImageView imgView = new ImageView(img);
            imgView.setFitWidth(width);
            imgView.setFitHeight(height);
            return imgView;
        } catch (Exception e) {
            System.err.println("Không tìm thấy ảnh: " + path);
            ImageView placeholder = new ImageView();
            placeholder.setFitWidth(width);
            placeholder.setFitHeight(height);
            placeholder.setStyle("-fx-background-color: #CBD5E0;");
            return placeholder;
        }
    }

    private void timKiemKhachHang() {
        String sdt = txtTimKiemSdt.getText().trim();

        if (sdt.isEmpty()) {
            showAlert(AlertType.WARNING, "Cảnh báo", "Vui lòng nhập số điện thoại để tìm kiếm.");
            return;
        }

        KhachHang kh = khachHangDAO.getKhachHangBySdt(sdt);

        if (kh != null) {
            capNhatThongTinKhachHang(kh.getMaKhachHang(), kh.getTenKhachHang(), kh.getSoDienThoai(),
                    String.format("%.0f", kh.getDiemTichLuy()));
            lblTrangThaiTimKiem.setText("Tìm thấy khách hàng");
            lblTrangThaiTimKiem.setStyle("-fx-text-fill: #38A169; -fx-font-weight: bold;"); // Màu xanh
        } else {
            capNhatThongTinKhachHang("000", "Khách vãng lai", sdt, // Giữ lại SDT vừa tìm kiếm
                    "0");
            lblTrangThaiTimKiem.setText("Không tìm thấy khách hàng.");
            lblTrangThaiTimKiem.setStyle("-fx-text-fill: #E53E3E; -fx-font-weight: bold;"); // Màu đỏ
        }
    }

    private void capNhatThongTinKhachHang(String ma, String ten, String sdt, String diem) {

        // Style
        String styleFixed = "-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 12; -fx-background-color: #D9D9D9; -fx-font-size: 15";
        String styleEditable = "-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-background-color: white; -fx-border-color: #CBD5E0; -fx-font-size: 15";

        // Đặt dữ liệu
        txtMaKh.setText(ma);
        txtDiem.setText(diem);
        txtSdt.setText(sdt);

        // TÊN
        txtTenKH.setText(ten);

        if (ma.equals("000")) {
            txtTenKH.setEditable(true);
            txtSdt.setEditable(true);
            txtTenKH.setStyle(styleEditable);
            txtSdt.setStyle(styleEditable);
            txtTenKH.setText("");
        } else {
            txtTenKH.setEditable(false);
            txtSdt.setEditable(false);
            txtTenKH.setStyle(styleFixed);
            txtSdt.setStyle(styleFixed);
        }
        txtMaKh.setStyle(styleFixed);
        txtDiem.setStyle(styleFixed);
    }

}