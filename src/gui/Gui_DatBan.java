//package gui;
//
//import java.util.List;
//import java.text.DecimalFormat;
//
//import dao.BanAn_DAO;
//import dao.KhachHang_DAO;
//import dao.MonAn_DAO;
//import dao.PhieuDatBan_DAO;
//import dao.HoaDon_DAO;
//import dao.ChiTietHoaDon_DAO;
//import entity.KhachHang;
//import entity.MonAn;
//import entity.NhanVien;
//import entity.PhieuDatBan;
//import ConnectDB.ConnectDB;
//import entity.BanAn;
//import entity.TrangThai;
//import gui.Gui_DanhSachBan;
//import entity.ChiTietHoaDon;
//import entity.HoaDon;
//import javafx.scene.control.Alert.AlertType;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.geometry.Insets;
//import javafx.geometry.Orientation;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.*;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Circle;
//import javafx.scene.text.Font;
//import javafx.scene.text.FontWeight;
//import javafx.scene.text.Text;
//import lib.ImageCacheManager;
//
//public class Gui_DatBan extends BorderPane {
//    private MonAn_DAO monAnDAO;
//    private KhachHang_DAO khachHangDAO;
//    private BanAn_DAO banAn_DAO;
//    private HoaDon_DAO hoaDonDAO;
//    private PhieuDatBan_DAO phieuDatBanDAO;
//    private ChiTietHoaDon_DAO chiTietHoaDonDAO;
//
//    private ObservableList<BanAn> dsBanDaChon;
//    private ObservableList<ChiTietHoaDon> dsMonDaChon;
//    private final DecimalFormat df = new DecimalFormat("###,###đ");
//    private Gui_TrangChu trangChu;
//    //	private BorderPane mainLayout;
//    private List<BanAn> cacBanDuocChon;
//    private Label lblTongCoc;
//    private Label lblTongTienMon;
//    private TilePane menuTilePane;
//    private TextField txtTimKiemSdt;
//    private Button btnTimKiem;
//    private Label lblTrangThaiTimKiem;
//    private TextField txtMaKh;
//    private TextField txtTenKH;
//    private TextField txtSdt;
//    private TextField txtDiem;
//    private DatePicker dpNgayDen;
//    private ComboBox<String> cmbGioDen;
//    private LocalDate ngayDatBan;
//    private TextField txtSoNguoi;
//    private RadioButton radioDungNgay;
//    private RadioButton radioDatTruoc;
//    private TextField txtGhiChu;
//
//    public Gui_DatBan(Gui_TrangChu trangChu, List<BanAn> cacBanDaChon, LocalDate ngayDatBan) {
//        this.trangChu = trangChu;
////		this.mainLayout = mainLayout;
//        this.cacBanDuocChon = cacBanDaChon;
//        this.ngayDatBan = ngayDatBan;
//
//        monAnDAO = new MonAn_DAO();
//        khachHangDAO = new KhachHang_DAO();
//        banAn_DAO = new BanAn_DAO();
//        hoaDonDAO = new HoaDon_DAO();
//        phieuDatBanDAO = new PhieuDatBan_DAO();
//        chiTietHoaDonDAO = new ChiTietHoaDon_DAO();
//
//        dsBanDaChon = FXCollections.observableArrayList();
//        dsMonDaChon = FXCollections.observableArrayList();
//        initialize();
//        taiDuLieuBanDau();
//    }
//
//    private void initialize() {
//        this.setStyle("-fx-background-color: #F7FAFC;");
//        HBox mainContent = new HBox();
//        VBox phanBenTrai = taoPhanBenTrai();
//        Separator separator = new Separator(Orientation.VERTICAL);
//        VBox phanBenPhai = taoPhanBenPhai();
//        mainContent.getChildren().addAll(phanBenTrai, separator, phanBenPhai);
//        this.setCenter(mainContent);
//        themLogicGioiHanSoNguoi();
//        try {
//            this.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
//        } catch (Exception e) {
//            System.err.println("Không tìm thấy CSS: /application/application.css");
//        }
//    }
//
//    private VBox taoPhanBenTrai() {
//        VBox vbox = new VBox(0);
//        vbox.setPadding(Insets.EMPTY);
//        vbox.setPrefWidth(500); // Cố định chiều rộng
//        vbox.setStyle("-fx-background-color: white;");
//        VBox boxThongTin = taoThongTinKhachVaDat();
//        VBox boxBanDaChon = taoBanDaChon();
//        HBox boxTongCoc = taoTongCoc();
//        VBox bottomBox = new VBox(15);
//        bottomBox.setPadding(new Insets(15, 20, 15, 20));
//        bottomBox.getChildren().addAll(boxBanDaChon, boxTongCoc);
//        vbox.getChildren().addAll(boxThongTin, bottomBox);
//        return vbox;
//    }
//
//    private VBox taoThongTinKhachVaDat() {
//        Region spacer1 = new Region();
//        HBox.setHgrow(spacer1, Priority.ALWAYS);
//        Region spacer2 = new Region();
//        HBox.setHgrow(spacer2, Priority.ALWAYS);
//        Region spacer3 = new Region();
//        HBox.setHgrow(spacer3, Priority.ALWAYS);
//        Region spacer4 = new Region();
//        HBox.setHgrow(spacer4, Priority.ALWAYS);
//        Region spacer5 = new Region();
//        HBox.setHgrow(spacer5, Priority.ALWAYS);
//        Region spacer6 = new Region();
//        HBox.setHgrow(spacer6, Priority.ALWAYS);
//        Region spacer8 = new Region();
//        HBox.setHgrow(spacer8, Priority.ALWAYS);
//        Region spacer7 = new Region();
//        HBox.setHgrow(spacer7, Priority.ALWAYS);
//        Region spacer9 = new Region();
//        HBox.setHgrow(spacer9, Priority.ALWAYS);
//        Region spacer10 = new Region();
//        HBox.setHgrow(spacer10, Priority.ALWAYS);
//
//        Label tieuDeKH = new Label("Thông Tin Khách Hàng");
//        tieuDeKH.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");
//
//        txtTimKiemSdt = new TextField();
//        txtTimKiemSdt.setPromptText("Nhập số điện thoại khách hàng");
//        txtTimKiemSdt.getStyleClass().add("timKiem");
//        txtTimKiemSdt.setPrefWidth(300);
//        txtTimKiemSdt.setStyle("-fx-font-size: 15; -fx-padding: 8;");
//
//        btnTimKiem = new Button("Tìm kiếm");
//        btnTimKiem.getStyleClass().add("button-timKiem");
//        btnTimKiem.setPrefWidth(120);
//
//        lblTrangThaiTimKiem = new Label("");
//        lblTrangThaiTimKiem.setStyle("-fx-text-fill: #38A169; -fx-font-weight: bold;");
//
//        HBox boxTimKiem = new HBox(10, txtTimKiemSdt, btnTimKiem);
//        boxTimKiem.setAlignment(Pos.CENTER_LEFT);
//        boxTimKiem.setPadding(new Insets(5, 0, 5, 0));
//
//        // Khởi tạo thuộc tính lớp
//        txtMaKh = new TextField();
//        txtTenKH = new TextField();
//        txtSdt = new TextField();
//        txtDiem = new TextField();
//
//        // Áp dụng Style và Editable
//        String styleFixed = "-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 12; -fx-background-color: #D9D9D9; -fx-font-size: 15";
//        String styleEditable = "-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-background-color: white; -fx-border-color: #082744; -fx-border-width: 1px; -fx-border-radius: 3; -fx-font-size: 15";
//
//        // Thiết lập thuộc tính ban đầu
//        txtMaKh.setEditable(false);
//        txtMaKh.setPrefWidth(250);
//        txtMaKh.setStyle(styleFixed);
//
//        txtTenKH.setEditable(true); // Cho phép nhập tên vãng lai ngay từ đầu
//        txtTenKH.setPrefWidth(250);
//        txtTenKH.setStyle(styleEditable);
//
//        txtSdt.setEditable(false); // Cho phép nhập SĐT vãng lai ngay từ đầu
//        txtSdt.setPrefWidth(250);
//        txtSdt.setStyle(styleFixed);
//
//        txtDiem.setEditable(false);
//        txtDiem.setPrefWidth(250);
//        txtDiem.setStyle(styleFixed);
//
//        // --- TẠO CÁC HBOX THÔNG TIN (Sử dụng thuộc tính lớp) ---
//
//        Label lblMaKH = new Label("Mã khách hàng:");
//        lblMaKH.getStyleClass().add("fontTieuDeNho");
//        HBox hbox1 = new HBox(lblMaKH, spacer1, txtMaKh); // Dùng this.txtMaKh
//        hbox1.setPadding(new Insets(5));
//
//        Label lblTenKH = new Label("Tên khách hàng:");
//        lblTenKH.getStyleClass().add("fontTieuDeNho");
//        HBox hbox2 = new HBox(lblTenKH, spacer2, txtTenKH);
//        hbox2.setPadding(new Insets(5));
//
//        Label lblSdt = new Label("Số điện thoại:");
//        lblSdt.getStyleClass().add("fontTieuDeNho");
//        HBox hbox3 = new HBox(lblSdt, spacer3, txtSdt);
//        hbox3.setPadding(new Insets(5));
//
//        Label lblDiem = new Label("Điểm tích lũy:");
//        lblDiem.getStyleClass().add("fontTieuDeNho");
//        HBox hbox4 = new HBox(lblDiem, spacer4, txtDiem);
//        hbox4.setPadding(new Insets(5));
//
//        txtMaKh.setText("000");
//        txtTenKH.setText("");
//        txtSdt.setText("");
//        txtDiem.setText("0");
//
//        /// VBOX ALL - 1
//        VBox vboxAll1 = new VBox(5);
//
//        vboxAll1.getChildren().addAll(tieuDeKH, boxTimKiem, lblTrangThaiTimKiem, hbox1, hbox2, hbox3, hbox4);
//        btnTimKiem.setOnAction(e -> timKiemKhachHang());
//
//        Label lblThongTinDatBan = new Label("Thông Tin Đặt Bàn");
//        lblThongTinDatBan.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");
//
//        Label lblNgayGio = new Label("Ngày giờ đến:");
//        lblNgayGio.getStyleClass().add("fontTieuDeNho");
//
//        // moi
//
//        cmbGioDen = new ComboBox<>();
//        // từ 00:00 đến 23:30
//        for (int h = 0; h < 24; h++) {
//            cmbGioDen.getItems().add(String.format("%02d:00", h));
//            cmbGioDen.getItems().add(String.format("%02d:30", h));
//        }
//
//        cmbGioDen.setValue("18:00"); // Giờ mặc định
//        cmbGioDen.setPrefWidth(110);
//        cmbGioDen.setStyle("-fx-font-size: 15; -fx-background-radius: 3 0 0 3");
//
//        dpNgayDen = new DatePicker(ngayDatBan);
//        dpNgayDen.setPrefWidth(140);
//        dpNgayDen.setStyle("-fx-font-size: 15; -fx-background-radius: 0 3 3 0");
//
//        dpNgayDen.setDayCellFactory(picker -> new DateCell() {
//            @Override
//            public void updateItem(LocalDate date, boolean empty) {
//                super.updateItem(date, empty);
//                // Vô hiệu hóa các ngày trước ngày hiện tại
//                setDisable(empty || date.isBefore(LocalDate.now()));
//            }
//        });
//
//        HBox hboxNgayGio = new HBox(0);
//        hboxNgayGio.getChildren().addAll(cmbGioDen, dpNgayDen);
//        hboxNgayGio.setAlignment(Pos.CENTER_LEFT);
//
//        cmbGioDen.setStyle(
//                "-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-background-color: #D9D9D9; -fx-font-size: 15; -fx-background-radius: 3 0 0 3");
//        dpNgayDen.setStyle(
//                "-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-background-color: #D9D9D9; -fx-font-size: 15; -fx-background-radius: 3 0 0 3");
//
//        HBox hbox5 = new HBox(lblNgayGio, spacer5, hboxNgayGio);
//        hbox5.setPadding(new Insets(5));
//// So nguoi
//        Label lblSoNguoi = new Label("Số người: ");
//        lblSoNguoi.getStyleClass().add("fontTieuDeNho");
//        txtSoNguoi = new TextField("0");
//
//        txtSoNguoi.setEditable(true);
//        txtSoNguoi.setPrefWidth(250);
//
//        txtSoNguoi.setStyle(
//                "-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-border-color: #082744; -fx-font-size: 15; ");
//        HBox hbox6 = new HBox(lblSoNguoi, spacer6, txtSoNguoi);
//        hbox6.setPadding(new Insets(5));
//// Kiểu đặt bàn
//        Label lblKieuDatBan = new Label("Kiểu đặt bàn");
//        lblKieuDatBan.getStyleClass().add("fontTieuDeNho");
//        radioDatTruoc = new RadioButton("Đặt trước");
//        radioDungNgay = new RadioButton("Dùng ngay");
//
//        ToggleGroup radioGroup = new ToggleGroup();
//        radioDatTruoc.setToggleGroup(radioGroup);
//        radioDungNgay.setToggleGroup(radioGroup);
//        radioDatTruoc.getStyleClass().add("radio-button");
//        radioDungNgay.getStyleClass().add("radio-button");
//        radioDatTruoc.setSelected(true);
//        HBox hboxRadio = new HBox(5);
//        hboxRadio.setPrefWidth(250);
//        Region spaceRadio = new Region();
//        HBox.setHgrow(spaceRadio, Priority.ALWAYS);
//        hboxRadio.getChildren().addAll(radioDatTruoc, spaceRadio, radioDungNgay);
//        HBox hbox7 = new HBox(lblKieuDatBan, spacer7, hboxRadio);
//        hbox7.setPadding(new Insets(5));
//
////		String styleEditable = "-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-background-color: white; -fx-border-color: #082744; -fx-border-width: 1px; -fx-border-radius: 3; -fx-font-size: 15";
//
//        Label lblGhiChu = new Label("Ghi chú:");
//        lblGhiChu.getStyleClass().add("fontTieuDeNho");
//
//        txtGhiChu = new TextField(); // KHỞI TẠO
//        txtGhiChu.setPromptText("ví dụ: ghế em bé, tiệc sinh nhật,...");
//        txtGhiChu.setPrefWidth(250);
//        txtGhiChu.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-background-color: white; -fx-border-color: #082744; -fx-border-width: 1px; -fx-border-radius: 3; -fx-font-size: 15");
//
//        Region spacerGhiChu = new Region();
//        HBox.setHgrow(spacerGhiChu, Priority.ALWAYS);
//
//        HBox hboxGhiChu = new HBox(lblGhiChu, spacerGhiChu, txtGhiChu);
//        hboxGhiChu.setPadding(new Insets(5));
//
//// Vbox all - 2
//        VBox vboxAll2 = new VBox(5);
//        vboxAll2.getChildren().addAll(lblThongTinDatBan, hbox5, hbox6, hbox7, hboxGhiChu);
//// Vbox All
//        VBox vboxALL = new VBox(25);
//        vboxALL.getChildren().addAll(vboxAll1, vboxAll2);
//        vboxALL.setAlignment(Pos.TOP_LEFT);
//        vboxALL.setPadding(new Insets(10, 20, 0, 20));
//        vboxALL.setMinWidth(500);
//        return vboxALL;
//    }
//
//    private VBox taoBanDaChon() {
//        VBox vbox = new VBox(5);
//        Label tieuDe = new Label("Bàn đã chọn");
//        tieuDe.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");
//
//        TableView<BanAn> table = new TableView<>();
//        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//        table.setPrefHeight(200);
//
//        table.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
//        TableColumn<BanAn, String> colMaBan = new TableColumn<>("Mã bàn");
//        colMaBan.setCellValueFactory(new PropertyValueFactory<>("maBan"));
//        colMaBan.setPrefWidth(90);
//
//        TableColumn<BanAn, String> colLoai = new TableColumn<>("Loại");
//        colLoai.setCellValueFactory(new PropertyValueFactory<>("loai"));
//        colLoai.setPrefWidth(110);
//
//        TableColumn<BanAn, Integer> colSoNguoi = new TableColumn<>("Số người");
//        colSoNguoi.setCellValueFactory(cellData -> {
//            String tenLoai = cellData.getValue().getLoai().getTenLoai();
//            int soNguoiToiDa = tenLoai.equalsIgnoreCase("VIP") ? 6 : 4;
//            // Trả về thuộc tính kiểu IntegerProperty
//            return new javafx.beans.property.SimpleIntegerProperty(soNguoiToiDa).asObject();
//        });
//        colSoNguoi.setPrefWidth(80);
//
//        TableColumn<BanAn, String> colCoc = new TableColumn<>("Cọc");
//        colCoc.setCellValueFactory(cellData -> {
//            String tenLoai = cellData.getValue().getLoai().getTenLoai();
//            double tienCoc = tenLoai.equalsIgnoreCase("VIP") ? 450000 : 300000;
//            return new javafx.beans.property.SimpleStringProperty(df.format(tienCoc));
//        });
//        colCoc.setPrefWidth(150);
//
//        colMaBan.setSortable(false);
//        colLoai.setSortable(false);
//        colSoNguoi.setSortable(false);
//        colCoc.setSortable(false);
//        table.getColumns().addAll(colMaBan, colLoai, colSoNguoi, colCoc);
//        table.setItems(dsBanDaChon);
//        vbox.getChildren().addAll(tieuDe, table);
//        return vbox;
//    }
//
//    private HBox taoTongCoc() {
//        HBox hbox = new HBox();
//        hbox.setAlignment(Pos.CENTER_LEFT);
//        Label lblTieuDe = new Label("Tổng cọc cần thanh toán:");
//        lblTieuDe.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");
//        Region spacer = new Region();
//        HBox.setHgrow(spacer, Priority.ALWAYS);
//        lblTongCoc = new Label("0đ"); // Giá trị mặc định
//        lblTongCoc.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #E53E3E;");
//        hbox.getChildren().addAll(lblTieuDe, spacer, lblTongCoc); // Dùng lblTongCoc
//        return hbox;
//    }
//
//    private VBox taoPhanBenPhai() {
//        VBox vbox = new VBox(15);
//        vbox.setPadding(new Insets(15));
//        vbox.setStyle("-fx-background-color: white;");
//        HBox.setHgrow(vbox, Priority.ALWAYS);
//        VBox boxMenu = taoMenuMonAn();
//        VBox boxMonDaChon = taoMonDaChon();
//        VBox.setVgrow(boxMonDaChon, Priority.ALWAYS);
//        HBox boxTongTienMon = taoTongTienMon();
//        HBox boxNutBam = taoNutBam();
//        vbox.getChildren().addAll(boxMenu, boxMonDaChon, boxTongTienMon, boxNutBam);
//        return vbox;
//    }
//
//    private VBox taoMenuMonAn() {
//        VBox vbox = new VBox(10);
//        Label tieuDe = new Label("Menu món ăn");
//        tieuDe.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");
//        ComboBox<String> comboLoai = new ComboBox<>();
//        comboLoai.getItems().addAll("Tất cả", "Món ăn kèm", "Món khai vị", "Món chính", "Nước sốt", "Đồ uống",
//                "Tráng miệng");
//        comboLoai.setValue("Tất cả");
//        comboLoai.setPrefWidth(150);
//        comboLoai.getStyleClass().add("combo-box-menu");
//        ScrollPane scrollPane = new ScrollPane();
//        scrollPane.setFitToWidth(true);
//        scrollPane.setPrefHeight(600);
//        scrollPane.getStyleClass().add("scroll-pane-menu");
//        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//        menuTilePane = new TilePane();
//        menuTilePane.setPadding(new Insets(10));
//        menuTilePane.setHgap(15);
//        menuTilePane.setVgap(15);
//        menuTilePane.setPrefColumns(4);
//        taiLaiDanhSachMonAn("Tất cả");
//        scrollPane.setContent(menuTilePane);
//        comboLoai.setOnAction(e -> {
//            String loaiDaChon = comboLoai.getValue();
//            taiLaiDanhSachMonAn(loaiDaChon);
//        });
//        HBox comboContainer = new HBox(comboLoai);
//        comboContainer.setPadding(new Insets(0, 0, 0, 5));
//        vbox.getChildren().addAll(tieuDe, comboContainer, scrollPane);
//        return vbox;
//    }
//
//    private void taiLaiDanhSachMonAn(String loaiMon) {
//        menuTilePane.getChildren().clear();
//        List<MonAn> dsMonAn;
//
//        if (loaiMon == null || loaiMon.equals("Tất cả")) {
//            dsMonAn = monAnDAO.getAllMonAn();
//        } else {
//            dsMonAn = monAnDAO.getMonAnByLoai(loaiMon);
//        }
//
//        for (MonAn mon : dsMonAn) {
//            int soLuongHienTai = 0;
//
//            ChiTietHoaDon cthdDaChon = timMonChon(mon);
//
//            if (cthdDaChon != null) {
//                soLuongHienTai = cthdDaChon.getSoLuong();
//            }
//
//            menuTilePane.getChildren().add(taoTheMonAn(mon, soLuongHienTai));
//        }
//    }
//
//    private VBox taoTheMonAn(MonAn mon, int soLuong) {
//        VBox vbox = new VBox(5);
//        vbox.setPrefWidth(155);
//        vbox.setAlignment(Pos.CENTER);
//        vbox.setPadding(new Insets(10));
//        vbox.setStyle("-fx-background-color: white;" +
//                "-fx-background-radius: 20;" +
//                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
//
//        // ✅ THÊM IMAGEVIEW HIỂN THỊ ẢNH
//        ImageView imgMonAn = new ImageView();
//        imgMonAn.setFitWidth(135);
//        imgMonAn.setFitHeight(100);
//        imgMonAn.setPreserveRatio(true);
//
//        // ✅ Load ảnh từ Supabase
//        if (mon.getHinhAnh() != null && !mon.getHinhAnh().isEmpty()) {
//            String SUPABASE_BASE_URL = "https://yxemxycygkhxygaydgcl.supabase.co/storage/v1/object/public/image/";
//
//            // Lấy ảnh từ cache (hoặc download nếu chưa có)
//            String imagePath = ImageCacheManager.getImagePath(SUPABASE_BASE_URL, mon.getHinhAnh());
//
//            if (imagePath != null) {
//                try {
//                    Image image = new Image(imagePath);
//                    imgMonAn.setImage(image);
//                } catch (Exception e) {
//                    loadDefaultImage(imgMonAn);
//                }
//            } else {
//                // Không download được → Dùng ảnh mặc định
//                loadDefaultImage(imgMonAn);
//            }
//        } else {
//            loadDefaultImage(imgMonAn);
//        }
//
//        // Tên món
//        Label lblTen = new Label(mon.getTenMonAn());
//        lblTen.setWrapText(true);
//        lblTen.setStyle("-fx-font-weight: 900; -fx-font-size: 15px; -fx-font-family: 'Times New Roman'; -fx-alignment: CENTER;");
//        lblTen.setPrefWidth(140);
//        lblTen.setMinHeight(35);
//
//        // Mô tả
//        Label lblMoTa = new Label(mon.getMoTa());
//        lblMoTa.setWrapText(true);
//        lblMoTa.setStyle("-fx-text-fill: #718096; -fx-font-size: 12px; -fx-alignment: CENTER;");
//        lblMoTa.setPrefWidth(140);
//        lblMoTa.setMinHeight(30);
//        if (mon.getMoTa() == null || mon.getMoTa().isEmpty()) {
//            lblMoTa.setVisible(false);
//        }
//
//        // Giá
//        Label lblGia = new Label(df.format(mon.getGiaTien()));
//        lblGia.setStyle("-fx-text-fill: #E53E3E; -fx-font-weight: bold; -fx-font-size: 18px; -fx-alignment: CENTER;");
//        lblGia.setPrefWidth(140);
//
//        // Nút điều chỉnh số lượng
//        Button btnTru = new Button("−");
//        btnTru.getStyleClass().add("button-dieu-chinh-menu");
//
//        Label lblSoLuong = new Label(String.valueOf(soLuong));
//        lblSoLuong.setPadding(new Insets(0, 10, 0, 10));
//        lblSoLuong.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
//
//        Button btnCong = new Button("+");
//        btnCong.getStyleClass().add("button-dieu-chinh-menu");
//
//        btnCong.setOnAction(e -> {
//            System.out.println("click");
//            themMonVaoGio(mon);
//            ChiTietHoaDon cthdDaChon = timMonChon(mon);
//            if (cthdDaChon != null) {
//                lblSoLuong.setText(String.valueOf(cthdDaChon.getSoLuong()));
//            }
//        });
//
//        btnTru.setOnAction(e -> {
//            botMonKhoiGio(mon);
//            ChiTietHoaDon cthdConLai = timMonChon(mon);
//            int sl = (cthdConLai != null) ? cthdConLai.getSoLuong() : 0;
//            lblSoLuong.setText(String.valueOf(sl));
//        });
//
//        HBox soLuongBox = new HBox(10, btnCong, lblSoLuong, btnTru);
//        soLuongBox.setAlignment(Pos.CENTER);
//        soLuongBox.setPadding(new Insets(5, 0, 0, 0));
//
//        // ✅ Thêm tất cả vào VBox (ảnh ở đầu tiên)
//        vbox.getChildren().addAll(imgMonAn, lblTen, lblMoTa, lblGia, soLuongBox);
//
//        return vbox;
//    }
//
//    private void loadDefaultImage(ImageView imgView) {
//        try {
//            Image defaultImg = new Image(getClass().getResourceAsStream("/img/default-food.png"));
//            imgView.setImage(defaultImg);
//        } catch (Exception e) {
//            imgView.setStyle("-fx-background-color: #E2E8F0;");
//        }
//    }
//
//    private ChiTietHoaDon timMonChon(MonAn mon) {
//        for (ChiTietHoaDon cthd : dsMonDaChon) {
//            if (cthd.getMonAn().getMaMonAn().equals(mon.getMaMonAn())) {
//                return cthd;
//            }
//        }
//        return null;
//    }
//
//    private void themMonVaoGio(MonAn mon) {
//        ChiTietHoaDon monCoSan = timMonChon(mon);
//        if (monCoSan != null) {
//            // Giả định ChiTietHoaDon có setter/getter cho soLuong
//            monCoSan.setSoLuong(monCoSan.getSoLuong() + 1);
//            dsMonDaChon.set(dsMonDaChon.indexOf(monCoSan), monCoSan);
//        } else {
//            // Tạo ChiTietHoaDon mới
//            ChiTietHoaDon cthd = new ChiTietHoaDon();
//            cthd.setMonAn(mon); // Giả định có setter
//            cthd.setSoLuong(1);
//            dsMonDaChon.add(cthd);
//        }
//        capNhatTongTienMon();
//    }
//
//    private void botMonKhoiGio(MonAn mon) {
//        ChiTietHoaDon monCoSan = timMonChon(mon);
//        if (monCoSan != null) {
//            monCoSan.setSoLuong(monCoSan.getSoLuong() - 1);
//            if (monCoSan.getSoLuong() == 0) {
//                dsMonDaChon.remove(monCoSan);
//            } else {
//                dsMonDaChon.set(dsMonDaChon.indexOf(monCoSan), monCoSan);
//            }
//            capNhatTongTienMon();
//        }
//    }
//
//    private void capNhatGioHangTheoSoLuongNhap(MonAn mon, int soLuongMoi) {
//        ChiTietHoaDon monCoSan = timMonChon(mon);
//
//        if (soLuongMoi > 0) {
//            if (monCoSan != null) {
//                monCoSan.setSoLuong(soLuongMoi);
//                dsMonDaChon.set(dsMonDaChon.indexOf(monCoSan), monCoSan);
//            } else {
//                // Thêm mới nếu số lượng > 0
//                ChiTietHoaDon cthd = new ChiTietHoaDon();
//                cthd.setMonAn(mon);
//                cthd.setSoLuong(soLuongMoi);
//                dsMonDaChon.add(cthd);
//            }
//        } else {
//            // Xóa khỏi giỏ nếu số lượng = 0
//            if (monCoSan != null) {
//                dsMonDaChon.remove(monCoSan);
//            }
//        }
//        capNhatTongTienMon();
//        // Không cần gọi taiLaiDanhSachMonAn(null) vì chỉ cập nhật thẻ hiện tại
//        // và dsMonDaChon sẽ tự cập nhật TableView (nếu ObservableList được binding đúng)
//    }
//
//    private void capNhatTongTienMon() {
//        double tong = 0;
//        LocalDate ngayDat = dpNgayDen.getValue();
//
//        for (ChiTietHoaDon cthd : dsMonDaChon) {
//            MonAn mon = cthd.getMonAn();
//
//            double giaApDung = monAnDAO.layGiaSauKhuyenMai(mon.getMaMonAn(), ngayDat, mon.getGiaTien());
//
//            tong += giaApDung * cthd.getSoLuong();
//        }
//        lblTongTienMon.setText(df.format(tong));
//    }
//
//    private VBox taoMonDaChon() {
//
//        VBox vbox = new VBox(10);
//        Label tieuDe = new Label("Món đã chọn");
//        tieuDe.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");
//        vbox.getChildren().add(tieuDe);
//
//        TableView<ChiTietHoaDon> table = new TableView<>();
//        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//        VBox.setVgrow(table, Priority.ALWAYS);
//
//        // 0. Cột Số thứ tự
//        TableColumn<ChiTietHoaDon, Integer> colSTT = new TableColumn<>("STT");
//
//        colSTT.setCellValueFactory(cellData -> {
//            int index = table.getItems().indexOf(cellData.getValue());
//            return new javafx.beans.property.SimpleObjectProperty<>(index + 1);
//        });
//        colSTT.setPrefWidth(20);
//        colSTT.setSortable(false);
//
//        // 1. Cột Tên món
//        TableColumn<ChiTietHoaDon, String> colTen = new TableColumn<>("Tên món");
//        colTen.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
//                cellData.getValue().getMonAn().getTenMonAn()));
//
//        // 2. Cột Số lượng
//        TableColumn<ChiTietHoaDon, Integer> colSoLuong = new TableColumn<>("Số lượng");
//        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
//
//        // 3. Cột Đơn giá
//        final LocalDate ngayDat = dpNgayDen.getValue();
//        TableColumn<ChiTietHoaDon, String> colDonGia = new TableColumn<>("Đơn giá");
//        colDonGia.setCellValueFactory(cellData -> {
//            MonAn mon = cellData.getValue().getMonAn();
//
//            double giaApDung = monAnDAO.layGiaSauKhuyenMai(mon.getMaMonAn(), ngayDat, mon.getGiaTien());
//
//            return new javafx.beans.property.SimpleStringProperty(df.format(giaApDung));
//        });
//
//        // 4. Cột Tổng
//        TableColumn<ChiTietHoaDon, String> colTong = new TableColumn<>("Tổng");
//        colTong.setCellValueFactory(cellData -> {
//            MonAn mon = cellData.getValue().getMonAn();
//
//            double giaApDung = monAnDAO.layGiaSauKhuyenMai(mon.getMaMonAn(), ngayDat, mon.getGiaTien());
//
//            double tong = giaApDung * cellData.getValue().getSoLuong();
//            return new javafx.beans.property.SimpleStringProperty(df.format(tong));
//        });
//
//        table.getColumns().addAll(colSTT, colTen, colSoLuong, colDonGia, colTong);
//        table.setItems(dsMonDaChon);
//
//        vbox.getChildren().addAll(table);
//        return vbox;
//    }
//
//    private HBox taoTongTienMon() {
//        HBox hbox = new HBox();
//        hbox.setAlignment(Pos.CENTER_LEFT);
//        Label lblTieuDe = new Label("Tổng tiền đặt món:");
//        lblTieuDe.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");
//        Region spacer = new Region();
//        HBox.setHgrow(spacer, Priority.ALWAYS);
//        lblTongTienMon = new Label("0đ"); // Giá trị mặc định
//        lblTongTienMon.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #E53E3E;");
//        hbox.getChildren().addAll(lblTieuDe, spacer, lblTongTienMon); // Dùng lblTongTienMon
//        return hbox;
//    }
//
//    private void taiDuLieuBanDau() {
//        for (BanAn ban : cacBanDuocChon) {
//            dsBanDaChon.add(ban);
//        }
//        capNhatTongCoc();
//        capNhatTongSoNguoi();
//    }
//
//    private void capNhatTongCoc() {
//        double tong = 0;
//        for (BanAn ban : dsBanDaChon) { // Lặp qua BanAn
//            String tenLoai = ban.getLoai().getTenLoai();
//            double tienCoc = tenLoai.equalsIgnoreCase("VIP") ? 450000 : 300000;
//            tong += tienCoc;
//        }
//        lblTongCoc.setText(df.format(tong));
//    }
//
//    private void capNhatTongSoNguoi() {
//        int tongSoNguoi = 0;
//
//        for (BanAn ban : cacBanDuocChon) {
//            int soNguoiToiDa = ban.getLoai().getTenLoai().equalsIgnoreCase("VIP") ? 6 : 4;
//            tongSoNguoi += soNguoiToiDa;
//        }
//
//        if (txtSoNguoi != null) {
//            txtSoNguoi.setText(String.valueOf(tongSoNguoi));
//        }
//    }
//
//    private HBox taoNutBam() {
//        HBox hbox = new HBox(10);
//        hbox.setAlignment(Pos.CENTER_RIGHT);
//        Region spacer = new Region();
//        HBox.setHgrow(spacer, Priority.ALWAYS);
//        Button btnQuayLai = new Button("Quay lại");
//        btnQuayLai.setPrefSize(150, 40);
//        btnQuayLai.getStyleClass().add("button-checkin");
//        btnQuayLai.setStyle("-fx-background-color: #A0AEC0;" + "-fx-font-weight: bold;" + "-fx-font-size: 20;"
//                + "-fx-text-fill: gray;");
//        Button btnXacNhan = new Button("Xác nhận đặt bàn");
//        btnXacNhan.setPrefSize(200, 40);
//        btnXacNhan.getStyleClass().add("button-checkin");
//        btnXacNhan.setStyle("-fx-background-color: #2D3748; -fx-text-fill: white; -fx-font-weight: bold;");
//        btnQuayLai.setOnAction(e -> {
////			mainLayout.setCenter(new Gui_DanhSachBan(mainLayout));
//            trangChu.setMainContent(new Gui_DanhSachBan(trangChu));
//        });
//
//        btnXacNhan.setOnAction(e -> {
//            String maHDChung = null;
//            boolean success = true;
//
//            try {
//                // ---------------------------------------------
//                // BƯẨY DỮ LIỆU ĐẦU VÀO VÀ TẠO ENTITY
//                // ---------------------------------------------
//
//                // 1. Lấy dữ liệu từ UI
//                String sdtMoi = txtSdt.getText().trim();
//                String tenKHMoi = txtTenKH.getText().trim();
//                String maKH = txtMaKh.getText();
//                KhachHang khachHangDat;
//
//                // KIỂM TRA BẮT BUỘC NHẬP
//                if (tenKHMoi.isEmpty() || sdtMoi.isEmpty()) {
//                    showAlert(AlertType.ERROR, "Lỗi dữ liệu", "Vui lòng nhập đầy đủ Tên và Số điện thoại khách hàng.");
//                    return;
//                }
//
//                // 2. Xử lý logic Khách hàng
//                if (maKH.equals("000")) {
//                    // Trường hợp 2A: Khách vãng lai -> Kiểm tra SĐT có trùng trong CSDL không
//                    KhachHang khDaTonTai = khachHangDAO.getKhachHangBySdt(sdtMoi);
//
//                    if (khDaTonTai != null) {
//                        // Lỗi: SĐT đã tồn tại nhưng không được tìm thấy ở bước tìm kiếm
//                        showAlert(AlertType.ERROR, "Lỗi trùng SĐT", "Số điện thoại này đã tồn tại trong hệ thống.");
//                        return;
//                    }
//
//                    // Khách hàng mới (hoặc khách vãng lai đã tìm kiếm)
//                    khachHangDat = new KhachHang();
//                    khachHangDat.setTenKhachHang(tenKHMoi);
//                    khachHangDat.setSoDienThoai(sdtMoi);
//                    khachHangDat.setDiemTichLuy(0.0); // Điểm mặc định
//
//                    // Lưu khách hàng mới vào CSDL
//                    boolean themKHSuccess = khachHangDAO.themKhachHangMoi(khachHangDat);
//
//                    if (themKHSuccess) {
//                    } else {
//                        showAlert(AlertType.ERROR, "Lỗi hệ thống", "Không thể thêm khách hàng mới vào CSDL.");
//                        return;
//                    }
//
//                } else {
//                    // Trường hợp 2B: Khách hàng đã có trong hệ thống (đã tìm thấy trước đó)
//                    khachHangDat = new KhachHang();
//                    khachHangDat.setMaKhachHang(maKH);
//                    // Các thông tin khác không cần thiết vì ta chỉ cần mã KH cho các bảng liên
//                    // quan.
//                }
//
//                //2. Nhân viên: Giả định có Entity NhanVien đang đăng nhập
//                // **THAY "NV001" bằng mã NV đang đăng nhập của bạn**
//                NhanVien nvDangNhap = new NhanVien();
//                nvDangNhap.setMaNhanVien("NV001");
//
//                // 3. Thời gian đến
//                LocalDateTime thoiGianDen = dpNgayDen.getValue().atStartOfDay()
//                        .withHour(Integer.parseInt(cmbGioDen.getValue().substring(0, 2)))
//                        .withMinute(Integer.parseInt(cmbGioDen.getValue().substring(3, 5)));
//                if (thoiGianDen.isBefore(LocalDateTime.now())) {
//                    showAlert(AlertType.ERROR, "Lỗi Thời gian", "Ngày giờ đến không được là ngày trong quá khứ.");
//                    return;
//                }
//
//                String ghiChu = txtGhiChu.getText().trim();
//
//
////				int soNguoiTong = Integer.parseInt(txtSoNguoi.getText());
//                int soNguoiDaNhap = 0;
//                try {
//                    soNguoiDaNhap = Integer.parseInt(txtSoNguoi.getText().trim());
//                } catch (NumberFormatException ex) {
//                    showAlert(AlertType.ERROR, "Lỗi dữ liệu", "Số người không hợp lệ.");
//                    return;
//                }
//                String maBanDauTien = cacBanDuocChon.get(0).getMaBan();
//
//                List<String> banBiTrung = new ArrayList<>();
//                for (BanAn ban : cacBanDuocChon) {
//                    // Gọi DAO để kiểm tra cho từng bàn
//                    if (phieuDatBanDAO.kiemTraBanDaDatTrongNgay(ban.getMaBan(), thoiGianDen)) {
//                        banBiTrung.add(ban.getMaBan());
//                    }
//                }
//
//                if (!banBiTrung.isEmpty()) {
//                    showAlert(AlertType.ERROR, "Trùng lịch đặt",
//                            "Các bàn sau đã có lịch đặt trong ngày " + thoiGianDen.toLocalDate() + ": " + String.join(", ", banBiTrung));
//                    return; // Dừng quá trình đặt bàn
//                }
//
//                // ---------------------------------------------
//                // BƯỚC 1: TẠO MỘT HÓA ĐƠN DUY NHẤT
//                // ---------------------------------------------
//
//                HoaDon hdChung = new HoaDon();
//                hdChung.setKhachHang(khachHangDat);
//                hdChung.setNhanVien(nvDangNhap);
//
//                // Sử dụng DAO để tạo Hóa đơn và nhận lại mã HD
//                maHDChung = hoaDonDAO.themHoaDon(hdChung);
//
//                if (maHDChung == null) {
//                    success = false;
//                    showAlert(AlertType.ERROR, "Lỗi DAO", "Không thể tạo Hóa đơn mới.");
//                    return;
//                }
//
//                // Gán mã HD vừa tạo ngược lại cho Entity HoaDon chung
//                hdChung.setMaHoaDon(maHDChung);
//
//                // ---------------------------------------------
//                // BƯỚC 2: TẠO PHIẾU ĐẶT BÀN VÀ CẬP NHẬT TRẠNG THÁI CHO TẤT CẢ CÁC BÀN
//                // ---------------------------------------------
//
//                for (BanAn ban : cacBanDuocChon) {
//                    // A. Cập nhật trạng thái bàn trong CSDL (DA_DAT)
//                    boolean updateBan = banAn_DAO.updateTrangThaiBan(ban, TrangThai.DA_DAT);
//                    if (!updateBan) {
//                        success = false;
//                        break;
//                    }
//
//                    // B. Tạo Entity Phiếu Đặt Bàn, liên kết với các Entity khác
//                    PhieuDatBan pdb = new PhieuDatBan();
//                    pdb.setThoiGianBatDau(thoiGianDen);
//                    pdb.setTrangThai("Đã đặt");
//
//                    // Số người của bàn đó (4 hoặc 6)
//                    int soNguoiCuaBan = ban.getLoai().getTenLoai().equalsIgnoreCase("VIP") ? 6 : 4;
//                    pdb.setSoNguoi(soNguoiDaNhap);
//
//                    pdb.setGhiChu(ghiChu);
//
//                    // Gán Entity
//                    pdb.setKhachHang(khachHangDat);
//                    pdb.setBan(ban);
//                    pdb.setNhanVien(nvDangNhap);
//                    pdb.setHoaDon(hdChung); // <<<< LIÊN KẾT VỚI HÓA ĐƠN CHUNG
//
//                    String trangThaiPhieu;
//                    if (radioDatTruoc.isSelected()) { // Kiểm tra radio button "Đặt trước"
//                        trangThaiPhieu = "Đã đặt";
//                    } else if (radioDungNgay.isSelected()) { // Kiểm tra radio button "Dùng ngay"
//                        trangThaiPhieu = "Đang dùng";
//                    } else {
//                        // Trường hợp mặc định (nên chọn một)
//                        trangThaiPhieu = "Đã đặt";
//                    }
//
//                    boolean themPDB = phieuDatBanDAO.themPhieuDatBan(pdb, trangThaiPhieu);
//                    if (!themPDB) {
//                        success = false;
//                        break;
//                    }
//                }
//
//                // -----------------------------------------------------
//                // BƯỚC 3: GHI CHI TIẾT HÓA ĐƠN
//                // -----------------------------------------------------
//                if (success && !dsMonDaChon.isEmpty()) {
//                    for (ChiTietHoaDon cthd : dsMonDaChon) {
//                        // Entity cthd đã có MonAn, chỉ cần gán HoaDon chung
//                        cthd.setHoaDon(hdChung);
//
//                        boolean themCTHD = chiTietHoaDonDAO.themChiTietHoaDon(cthd);
//                        if (!themCTHD) {
//                            success = false;
//                            break;
//                        }
//                    }
//                }
//
//                // ---------------------------------------------
//                // BƯỚC 4: XỬ LÝ KẾT QUẢ VÀ QUAY LẠI
//                // ---------------------------------------------
//                if (success) {
//                    showAlert(AlertType.INFORMATION, "Thành công", "Đã đặt bàn thành công! Mã hóa đơn: " + maHDChung);
////					mainLayout.setCenter(new Gui_DanhSachBan(mainLayout));
//                    trangChu.setMainContent(new Gui_DanhSachBan(trangChu));
//                } else {
//                    showAlert(AlertType.ERROR, "Lỗi Nghiệp vụ",
//                            "Đã xảy ra lỗi khi ghi dữ liệu. Vui lòng kiểm tra lại hệ thống.");
//                    // **Thực tế cần thêm logic ROLLBACK tại đây**
//                }
//
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                showAlert(AlertType.ERROR, "Lỗi nghiêm trọng", "Đã xảy ra lỗi: " + ex.getMessage());
//            }
//        });
//        hbox.getChildren().addAll(spacer, btnQuayLai, btnXacNhan);
//        return hbox;
//    }
//
//    private void showAlert(AlertType alertType, String title, String content) {
//        Alert alert = new Alert(alertType);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(content);
//        alert.showAndWait();
//    }
//
//    private ImageView createImageViewSafe(String path, double width, double height) {
//        try {
//            Image img = new Image(getClass().getResourceAsStream(path));
//            ImageView imgView = new ImageView(img);
//            imgView.setFitWidth(width);
//            imgView.setFitHeight(height);
//            return imgView;
//        } catch (Exception e) {
//            System.err.println("Không tìm thấy ảnh: " + path);
//            ImageView placeholder = new ImageView();
//            placeholder.setFitWidth(width);
//            placeholder.setFitHeight(height);
//            placeholder.setStyle("-fx-background-color: #CBD5E0;");
//            return placeholder;
//        }
//    }
//
//    private void timKiemKhachHang() {
//        String sdt = txtTimKiemSdt.getText().trim();
//
//        final String SDT_REGEX = "^0[0-9]{9}$";
//
//        if (sdt.isEmpty()) {
//            showAlert(AlertType.WARNING, "Cảnh báo", "Vui lòng nhập số điện thoại để tìm kiếm.");
//
//            // Xóa trạng thái và thông tin khách vãng lai
//            lblTrangThaiTimKiem.setText("");
//            capNhatThongTinKhachHang("000", "", "", "0");
//            return;
//        }
//
//        if (!sdt.matches(SDT_REGEX)) {
//            showAlert(AlertType.ERROR, "Lỗi định dạng", "Số điện thoại phải bắt đầu bằng '0' và có đủ 10 chữ số.");
//
//            // Xóa trạng thái và thông tin khách vãng lai
//            lblTrangThaiTimKiem.setText("");
//            capNhatThongTinKhachHang("000", "", sdt, "0"); // Giữ lại SDT vừa nhập
//            return;
//        }
//
//        KhachHang kh = khachHangDAO.getKhachHangBySdt(sdt);
//
//        if (kh != null) {
//            capNhatThongTinKhachHang(kh.getMaKhachHang(), kh.getTenKhachHang(), kh.getSoDienThoai(),
//                    String.format("%.0f", kh.getDiemTichLuy()));
//            lblTrangThaiTimKiem.setText("Tìm thấy khách hàng");
//            lblTrangThaiTimKiem.setStyle("-fx-text-fill: #38A169; -fx-font-weight: bold;");
//        } else {
//            capNhatThongTinKhachHang("000", "Khách vãng lai", sdt,
//                    "0");
//            lblTrangThaiTimKiem.setText("Không tìm thấy khách hàng.");
//            lblTrangThaiTimKiem.setStyle("-fx-text-fill: #E53E3E; -fx-font-weight: bold;"); // Màu đỏ
//        }
//    }
//
//    private void capNhatThongTinKhachHang(String ma, String ten, String sdt, String diem) {
//
//        // Style
//        String styleFixed = "-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 12; -fx-background-color: #D9D9D9; -fx-font-size: 15";
//        String styleEditable = "-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-background-color: white; -fx-border-color: #CBD5E0; -fx-font-size: 15";
//
//        // Đặt dữ liệu
//        txtMaKh.setText(ma);
//        txtDiem.setText(diem);
//        txtSdt.setText(sdt);
//
//        // TÊN
//        txtTenKH.setText(ten);
//
//        if (ma.equals("000")) {
//            txtTenKH.setEditable(true);
//            txtSdt.setEditable(false);
//            txtTenKH.setStyle(styleEditable);
//            txtSdt.setStyle(styleFixed);
//            txtTenKH.setText("");
//        } else {
//            txtTenKH.setEditable(false);
//            txtSdt.setEditable(false);
//            txtTenKH.setStyle(styleFixed);
//            txtSdt.setStyle(styleFixed);
//        }
//        txtMaKh.setStyle(styleFixed);
//        txtDiem.setStyle(styleFixed);
//    }
//
//    private void themLogicGioiHanSoNguoi() {
//        txtSoNguoi.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue.matches("\\d*")) {
//                // Chỉ cho phép nhập số
//                txtSoNguoi.setText(oldValue);
//                return;
//            }
//
//            // Lấy tổng số người tối đa của các bàn đã chọn (mặc định)
//            int tongMax = 0;
//            for (BanAn ban : cacBanDuocChon) {
//                int soNguoiToiDa = ban.getLoai().getTenLoai().equalsIgnoreCase("VIP") ? 6 : 4;
//                tongMax += soNguoiToiDa;
//            }
//
//            try {
//                if (!newValue.isEmpty()) {
//                    int soNhap = Integer.parseInt(newValue);
//                    if (soNhap > tongMax) {
//                        // Cảnh báo và đặt lại giá trị tối đa
//                        showAlert(AlertType.WARNING, "Lỗi số lượng", "Số người không được vượt quá tối đa (" + tongMax + " người).");
//                        txtSoNguoi.setText(String.valueOf(tongMax));
//                    }
//                }
//            } catch (NumberFormatException e) {
//                // Không xảy ra nếu đã lọc regex ở trên
//            }
//        });
//    }
//
//}
package gui;

import java.util.List;
import java.text.DecimalFormat;

import dao.BanAn_DAO;
import dao.KhachHang_DAO;
import dao.MonAn_DAO;
import dao.PhieuDatBan_DAO;
import dao.HoaDon_DAO;
import dao.ChiTietHoaDon_DAO;
import entity.KhachHang;
import entity.MonAn;
import entity.NhanVien;
import entity.PhieuDatBan;
import entity.BanAn;
import entity.TrangThai;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lib.ImageCacheManager;

public class Gui_DatBan extends BorderPane {
    private MonAn_DAO monAnDAO;
    private KhachHang_DAO khachHangDAO;
    private BanAn_DAO banAn_DAO;
    private HoaDon_DAO hoaDonDAO;
    private PhieuDatBan_DAO phieuDatBanDAO;
    private ChiTietHoaDon_DAO chiTietHoaDonDAO;

    private ObservableList<BanAn> dsBanDaChon;
    private ObservableList<ChiTietHoaDon> dsMonDaChon;
    private final DecimalFormat df = new DecimalFormat("###,###đ");
    private Gui_TrangChu trangChu;
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
    private TextField txtGhiChu;

    public Gui_DatBan(Gui_TrangChu trangChu, List<BanAn> cacBanDaChon, LocalDate ngayDatBan) {
        this.trangChu = trangChu;
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

        initializeUI(); // Gọi hàm khởi tạo UI mới
        taiDuLieuBanDau();
    }

    private void initializeUI() {
        // --- 1. ROOT STYLE ---
        this.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa 0%, #e9ecef 100%);");
        this.setPadding(new Insets(10)); // Giảm padding tổng để tiết kiệm diện tích

        // --- 2. LAYOUT ---
        HBox mainContent = new HBox(15);
        mainContent.setAlignment(Pos.TOP_CENTER);

        // Tạo 2 phần
        Node phanBenTrai = taoPhanBenTrai(); // Bây giờ trả về ScrollPane hoặc VBox bọc trong ScrollPane
        VBox phanBenPhai = taoPhanBenPhai();

        // Chia tỷ lệ: Trái 30%, Phải 70% (Linh hoạt hơn fix cứng pixel)
        HBox.setHgrow(phanBenTrai, Priority.ALWAYS);
        HBox.setHgrow(phanBenPhai, Priority.ALWAYS);

        // Thiết lập độ rộng tối thiểu/tối đa để giao diện không bị nát
        if (phanBenTrai instanceof Region) {
            ((Region) phanBenTrai).setMinWidth(380);
            ((Region) phanBenTrai).setMaxWidth(450);
        }

        mainContent.getChildren().addAll(phanBenTrai, phanBenPhai);
        this.setCenter(mainContent);

        themLogicGioiHanSoNguoi();

        // CSS Inline
        this.getStylesheets().add("data:text/css," +
                ".table-view { -fx-background-color: transparent; -fx-border-color: #e2e8f0; -fx-border-radius: 5; }" +
                ".table-view .column-header-background { -fx-background-color: #f7fafc; -fx-border-width: 0 0 1 0; -fx-border-color: #e2e8f0; }" +
                ".table-view .column-header .label { -fx-text-fill: #4a5568; -fx-font-weight: bold; }" +
                ".table-row-cell:filled:selected { -fx-background-color: #ebf8ff; }" +
                ".scroll-pane { -fx-background-color: transparent; }" +
                ".scroll-pane > .viewport { -fx-background-color: transparent; }"
        );
    }

    // ======================================================================================
    // PHẦN BÊN TRÁI: THÔNG TIN ĐẶT BÀN
    // ======================================================================================

    // Sửa kiểu trả về thành Node (để trả về ScrollPane)
    private Node taoPhanBenTrai() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        // 1. Header
        Label lblHeader = new Label("📝 THÔNG TIN ĐẶT BÀN");
        lblHeader.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        lblHeader.setTextFill(Color.web("#082744"));

        // 2. Các Box con (Giữ nguyên logic cũ)
        VBox boxKhachHang = taoBoxKhachHang();
        VBox boxChiTiet = taoBoxChiTietDat();
        VBox boxBanChon = taoBoxBanDaChon();

        container.getChildren().addAll(lblHeader, boxKhachHang, new Separator(), boxChiTiet, new Separator(), boxBanChon);

        // ✅ QUAN TRỌNG: Bọc trong ScrollPane để cuộn được nếu màn hình thấp
        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true); // Nội dung co giãn theo chiều ngang
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    private VBox taoBoxKhachHang() {
        VBox box = new VBox(10);

        // Tìm kiếm
        Label lblTim = new Label("Tìm khách hàng (SĐT):");
        lblTim.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

        txtTimKiemSdt = new TextField();
        txtTimKiemSdt.setPromptText("Nhập số điện thoại...");
        styleTextField(txtTimKiemSdt);

        btnTimKiem = new Button("🔍");
        btnTimKiem.setStyle("-fx-background-color: #082744; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 5;");
        btnTimKiem.setOnAction(e -> timKiemKhachHang());

        HBox boxTim = new HBox(10, txtTimKiemSdt, btnTimKiem);
        HBox.setHgrow(txtTimKiemSdt, Priority.ALWAYS);

        lblTrangThaiTimKiem = new Label("");
        lblTrangThaiTimKiem.setFont(Font.font("Segoe UI", 12));

        // Fields
        txtMaKh = new TextField("000"); txtMaKh.setEditable(false); styleTextFieldReadonly(txtMaKh);
        txtTenKH = new TextField(); styleTextField(txtTenKH);
        txtSdt = new TextField(); styleTextFieldReadonly(txtSdt);
        txtDiem = new TextField("0"); txtDiem.setEditable(false); styleTextFieldReadonly(txtDiem);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.add(new Label("Mã KH:"), 0, 0); grid.add(txtMaKh, 1, 0);
        grid.add(new Label("Họ tên:"), 0, 1); grid.add(txtTenKH, 1, 1);
        grid.add(new Label("SĐT:"), 0, 2); grid.add(txtSdt, 1, 2);
        grid.add(new Label("Điểm:"), 0, 3); grid.add(txtDiem, 1, 3);

        // Style Labels in Grid
        for(Node n : grid.getChildren()) {
            if(n instanceof Label) ((Label)n).setFont(Font.font("Segoe UI", 13));
        }

        box.getChildren().addAll(lblTim, boxTim, lblTrangThaiTimKiem, grid);
        return box;
    }

    private VBox taoBoxChiTietDat() {
        VBox box = new VBox(10);

        // Thời gian
        Label lblThoiGian = new Label("Thời gian đến:");
        lblThoiGian.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

        cmbGioDen = new ComboBox<>();
        for (int h = 0; h < 24; h++) {
            cmbGioDen.getItems().add(String.format("%02d:00", h));
            cmbGioDen.getItems().add(String.format("%02d:30", h));
        }
        cmbGioDen.setValue("18:00");
        cmbGioDen.setPrefWidth(100);

        dpNgayDen = new DatePicker(ngayDatBan);
        dpNgayDen.setPrefWidth(150);
        dpNgayDen.setDayCellFactory(picker -> new DateCell() {
            @Override public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty); setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        HBox boxTime = new HBox(10, cmbGioDen, dpNgayDen);

        // Số người & Loại đặt
        txtSoNguoi = new TextField("0");
        styleTextField(txtSoNguoi);
        txtSoNguoi.setPromptText("Số khách");

        radioDatTruoc = new RadioButton("Đặt trước");
        radioDungNgay = new RadioButton("Dùng ngay");
        ToggleGroup group = new ToggleGroup();
        radioDatTruoc.setToggleGroup(group); radioDungNgay.setToggleGroup(group);
        radioDatTruoc.setSelected(true);
        HBox boxRadio = new HBox(15, radioDatTruoc, radioDungNgay);

        // Ghi chú
        txtGhiChu = new TextField();
        txtGhiChu.setPromptText("Ghi chú (ghế trẻ em, sinh nhật...)");
        styleTextField(txtGhiChu);

        box.getChildren().addAll(
                lblThoiGian, boxTime,
                new Label("Số người:"), txtSoNguoi,
                new Label("Loại đặt:"), boxRadio,
                new Label("Ghi chú:"), txtGhiChu
        );
        return box;
    }

    private VBox taoBoxBanDaChon() {
        VBox box = new VBox(10);
        Label lbl = new Label("Danh sách bàn đã chọn");
        lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

        TableView<BanAn> table = new TableView<>();
        table.setPrefHeight(150);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<BanAn, String> colMa = new TableColumn<>("Mã");
        colMa.setCellValueFactory(new PropertyValueFactory<>("maBan"));

        TableColumn<BanAn, String> colLoai = new TableColumn<>("Loại");
        colLoai.setCellValueFactory(new PropertyValueFactory<>("loai"));

        TableColumn<BanAn, String> colCoc = new TableColumn<>("Cọc");
        colCoc.setCellValueFactory(cellData -> {
            String tenLoai = cellData.getValue().getLoai().getTenLoai();
            double tienCoc = tenLoai.equalsIgnoreCase("VIP") ? 450000 : 300000;
            return new javafx.beans.property.SimpleStringProperty(df.format(tienCoc));
        });

        table.getColumns().addAll(colMa, colLoai, colCoc);
        table.setItems(dsBanDaChon);

        // Tổng cọc
        HBox boxCoc = new HBox(10);
        boxCoc.setAlignment(Pos.CENTER_RIGHT);
        Label lblTotal = new Label("Tổng cọc:");
        lblTotal.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        lblTongCoc = new Label("0đ");
        lblTongCoc.setTextFill(Color.web("#E53E3E"));
        lblTongCoc.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        boxCoc.getChildren().addAll(lblTotal, lblTongCoc);

        box.getChildren().addAll(lbl, table, boxCoc);
        return box;
    }

    // ======================================================================================
    // PHẦN BÊN PHẢI: CHỌN MÓN ĂN
    // ======================================================================================

    private VBox taoPhanBenPhai() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(15));
        container.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        // 1. Header Menu
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        Label lblMenu = new Label("🍽️ CHỌN MÓN ĂN");
        lblMenu.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        lblMenu.setTextFill(Color.web("#082744"));

        ComboBox<String> comboLoai = new ComboBox<>();
        comboLoai.getItems().addAll("Tất cả", "Món ăn kèm", "Món khai vị", "Món chính", "Nước sốt", "Đồ uống", "Tráng miệng");
        comboLoai.setValue("Tất cả");
        comboLoai.setOnAction(e -> taiLaiDanhSachMonAn(comboLoai.getValue()));

        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(lblMenu, spacer, comboLoai);

        // 2. Grid Món ăn (Menu)
        menuTilePane = new TilePane();
        menuTilePane.setPadding(new Insets(5));
        menuTilePane.setHgap(15); menuTilePane.setVgap(15);
        menuTilePane.setPrefColumns(3);
        // ✅ QUAN TRỌNG: Để TilePane tự động tính toán cột dựa trên chiều rộng
        menuTilePane.setPrefTileWidth(160);
        menuTilePane.setPrefTileHeight(Region.USE_COMPUTED_SIZE); // Chiều cao tự động
        menuTilePane.setStyle("-fx-background-color: transparent;");

        ScrollPane scrollMenu = new ScrollPane(menuTilePane);
        scrollMenu.setFitToWidth(true);
        scrollMenu.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        // ✅ SỬA LỖI: Bỏ setPrefHeight, dùng Vgrow để full chiều cao
        VBox.setVgrow(scrollMenu, Priority.ALWAYS);

        // 3. Giỏ hàng
        Label lblGioHang = new Label("🛒 Món đã chọn");
        lblGioHang.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));

        TableView<ChiTietHoaDon> tblGioHang = taoBangGioHang();
        tblGioHang.setPrefHeight(150); // Chiều cao cố định cho giỏ hàng nhỏ
        tblGioHang.setMinHeight(100);

        // 4. Footer
        HBox footer = new HBox(15);
        footer.setAlignment(Pos.CENTER_RIGHT);

        Label lblTongTienText = new Label("Tổng:");
        lblTongTienText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        lblTongTienMon = new Label("0đ");
        lblTongTienMon.setTextFill(Color.web("#E53E3E"));
        lblTongTienMon.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));

        Button btnQuayLai = new Button("Quay lại");
        btnQuayLai.setStyle("-fx-background-color: #CBD5E0; -fx-text-fill: #2d3436; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 15;");
        btnQuayLai.setOnAction(e -> trangChu.setMainContent(new Gui_DanhSachBan(trangChu)));

        Button btnXacNhan = new Button("✅ ĐẶT BÀN");
        btnXacNhan.setStyle("-fx-background-color: #082744; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 15;");
        btnXacNhan.setOnAction(e -> xuLyXacNhanDatBan());

        footer.getChildren().addAll(lblTongTienText, lblTongTienMon, new Region(), btnQuayLai, btnXacNhan);
        HBox.setHgrow(footer.getChildren().get(2), Priority.ALWAYS);

        container.getChildren().addAll(header, scrollMenu, new Separator(), lblGioHang, tblGioHang, footer);

        taiLaiDanhSachMonAn("Tất cả");
        return container;
    }

    private TableView<ChiTietHoaDon> taoBangGioHang() {
        TableView<ChiTietHoaDon> table = new TableView<>();
        table.setPrefHeight(200);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ChiTietHoaDon, String> colTen = new TableColumn<>("Món");
        colTen.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getMonAn().getTenMonAn()));

        TableColumn<ChiTietHoaDon, Integer> colSL = new TableColumn<>("SL");
        colSL.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        colSL.setPrefWidth(50);

        TableColumn<ChiTietHoaDon, String> colGia = new TableColumn<>("Đơn giá");
        colGia.setCellValueFactory(cell -> {
            MonAn m = cell.getValue().getMonAn();
            double gia = monAnDAO.layGiaSauKhuyenMai(m.getMaMonAn(), dpNgayDen.getValue(), m.getGiaTien());
            return new javafx.beans.property.SimpleStringProperty(df.format(gia));
        });

        TableColumn<ChiTietHoaDon, String> colTong = new TableColumn<>("Thành tiền");
        colTong.setCellValueFactory(cell -> {
            MonAn m = cell.getValue().getMonAn();
            double gia = monAnDAO.layGiaSauKhuyenMai(m.getMaMonAn(), dpNgayDen.getValue(), m.getGiaTien());
            return new javafx.beans.property.SimpleStringProperty(df.format(gia * cell.getValue().getSoLuong()));
        });

        table.getColumns().addAll(colTen, colSL, colGia, colTong);
        table.setItems(dsMonDaChon);
        return table;
    }

    // --- CARD MÓN ĂN ĐẸP HƠN ---
    private VBox taoTheMonAn(MonAn mon, int soLuong) {
        VBox card = new VBox(8);
        // ✅ SỬA LỖI: Không fix chiều cao (220), chỉ fix chiều rộng tối thiểu
        card.setMinWidth(150);
        card.setMaxWidth(160);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(10));
        card.setStyle(
                "-fx-background-color: white; -fx-background-radius: 10; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );

        // 1. Ảnh
        ImageView imgView = new ImageView();
        imgView.setFitWidth(130);
        imgView.setFitHeight(90);
        imgView.setPreserveRatio(true); // Giữ tỷ lệ ảnh

        if (mon.getHinhAnh() != null && !mon.getHinhAnh().isEmpty()) {
            String SUPABASE_BASE_URL = "https://yxemxycygkhxygaydgcl.supabase.co/storage/v1/object/public/image/";
            String imagePath = ImageCacheManager.getImagePath(SUPABASE_BASE_URL, mon.getHinhAnh());
            if (imagePath != null) {
                try {
                    // Cắt ảnh cho vuông vức (Center crop)
                    Image img = new Image(imagePath);
                    imgView.setImage(img);
                    // Reset fit height để nó tự tính theo ratio hoặc crop
                    imgView.setPreserveRatio(false);
                    imgView.setFitHeight(90); // Ép chiều cao ảnh cố định để thẻ đều nhau
                } catch (Exception e) { loadDefaultImage(imgView); }
            } else loadDefaultImage(imgView);
        } else loadDefaultImage(imgView);

        // Bo tròn ảnh (Tùy chọn)
        Rectangle clip = new Rectangle(130, 90);
        clip.setArcWidth(10); clip.setArcHeight(10);
        imgView.setClip(clip);

        // 2. Tên & Giá
        Label lblTen = new Label(mon.getTenMonAn());
        lblTen.setWrapText(true); // ✅ QUAN TRỌNG: Tự động xuống dòng
        lblTen.setAlignment(Pos.CENTER);
        lblTen.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        lblTen.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        lblTen.setMinHeight(35); // Chiều cao tối thiểu cho tên (khoảng 2 dòng)
        lblTen.setMaxWidth(Double.MAX_VALUE); // Cho phép giãn ngang hết cỡ

        Label lblGia = new Label(df.format(mon.getGiaTien()));
        lblGia.setTextFill(Color.web("#E53E3E"));
        lblGia.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));

        // 3. Controls Số lượng
        HBox controls = new HBox(8);
        controls.setAlignment(Pos.CENTER);

        Button btnTru = createRoundButton("-");
        Label lblSL = new Label(String.valueOf(soLuong));
        lblSL.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        lblSL.setMinWidth(20);
        lblSL.setAlignment(Pos.CENTER);
        Button btnCong = createRoundButton("+");

        btnCong.setOnAction(e -> {
            themMonVaoGio(mon);
            ChiTietHoaDon c = timMonChon(mon);
            if(c!=null) lblSL.setText(String.valueOf(c.getSoLuong()));
        });
        btnTru.setOnAction(e -> {
            botMonKhoiGio(mon);
            ChiTietHoaDon c = timMonChon(mon);
            lblSL.setText(String.valueOf(c!=null ? c.getSoLuong() : 0));
        });

        controls.getChildren().addAll(btnTru, lblSL, btnCong);

        // Spacer ở giữa giá và nút để đẩy nút xuống dưới cùng nếu thẻ bị kéo dài
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        card.getChildren().addAll(imgView, lblTen, lblGia, spacer, controls);

        return card;
    }

    private Button createRoundButton(String text) {
        Button btn = new Button(text);
        btn.setPrefSize(30, 30);
        btn.setStyle(
                "-fx-background-color: #EDF2F7; -fx-text-fill: #2d3436; -fx-font-weight: bold; " +
                        "-fx-background-radius: 15; -fx-cursor: hand;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #E2E8F0; -fx-text-fill: #2d3436; -fx-font-weight: bold; -fx-background-radius: 15;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #EDF2F7; -fx-text-fill: #2d3436; -fx-font-weight: bold; -fx-background-radius: 15;"));
        return btn;
    }

    private void styleTextField(TextField tf) {
        tf.setStyle("-fx-background-color: white; -fx-border-color: #CBD5E0; -fx-border-radius: 5; -fx-padding: 8;");
    }

    private void styleTextFieldReadonly(TextField tf) {
        tf.setStyle("-fx-background-color: #F7FAFC; -fx-border-color: #E2E8F0; -fx-border-radius: 5; -fx-padding: 8; -fx-text-fill: #718096;");
    }

    private void loadDefaultImage(ImageView imgView) {
        try { imgView.setImage(new Image(getClass().getResourceAsStream("/img/default-food.png"))); }
        catch (Exception e) { imgView.setStyle("-fx-background-color: #E2E8F0;"); }
    }

    // ======================================================================================
    // LOGIC NGHIỆP VỤ (GIỮ NGUYÊN)
    // ======================================================================================

    private void taiLaiDanhSachMonAn(String loaiMon) {
        menuTilePane.getChildren().clear();
        List<MonAn> dsMonAn = (loaiMon == null || loaiMon.equals("Tất cả")) ? monAnDAO.getAllMonAn() : monAnDAO.getMonAnByLoai(loaiMon);
        for (MonAn mon : dsMonAn) {
            ChiTietHoaDon c = timMonChon(mon);
            menuTilePane.getChildren().add(taoTheMonAn(mon, c != null ? c.getSoLuong() : 0));
        }
    }

    private ChiTietHoaDon timMonChon(MonAn mon) {
        for (ChiTietHoaDon c : dsMonDaChon) if (c.getMonAn().getMaMonAn().equals(mon.getMaMonAn())) return c;
        return null;
    }

    private void themMonVaoGio(MonAn mon) {
        ChiTietHoaDon c = timMonChon(mon);
        if (c != null) { c.setSoLuong(c.getSoLuong() + 1); dsMonDaChon.set(dsMonDaChon.indexOf(c), c); }
        else { ChiTietHoaDon n = new ChiTietHoaDon(); n.setMonAn(mon); n.setSoLuong(1); dsMonDaChon.add(n); }
        capNhatTongTienMon();
    }

    private void botMonKhoiGio(MonAn mon) {
        ChiTietHoaDon c = timMonChon(mon);
        if (c != null) {
            c.setSoLuong(c.getSoLuong() - 1);
            if (c.getSoLuong() == 0) dsMonDaChon.remove(c);
            else dsMonDaChon.set(dsMonDaChon.indexOf(c), c);
            capNhatTongTienMon();
        }
    }

    private void capNhatTongTienMon() {
        double tong = 0; LocalDate ngay = dpNgayDen.getValue();
        for (ChiTietHoaDon c : dsMonDaChon) {
            double gia = monAnDAO.layGiaSauKhuyenMai(c.getMonAn().getMaMonAn(), ngay, c.getMonAn().getGiaTien());
            tong += gia * c.getSoLuong();
        }
        lblTongTienMon.setText(df.format(tong));
    }

    private void taiDuLieuBanDau() {
        dsBanDaChon.addAll(cacBanDuocChon);
        double tongCoc = 0; int tongNguoi = 0;
        for (BanAn b : dsBanDaChon) {
            boolean isVip = b.getLoai().getTenLoai().equalsIgnoreCase("VIP");
            tongCoc += isVip ? 450000 : 300000;
            tongNguoi += isVip ? 6 : 4;
        }
        lblTongCoc.setText(df.format(tongCoc));
        if(txtSoNguoi != null) txtSoNguoi.setText(String.valueOf(tongNguoi));
    }

    private void timKiemKhachHang() {
        String sdt = txtTimKiemSdt.getText().trim();
        if (sdt.isEmpty()) { showAlert(AlertType.WARNING, "Cảnh báo", "Vui lòng nhập SĐT."); return; }
        if (!sdt.matches("^0[0-9]{9}$")) { showAlert(AlertType.ERROR, "Lỗi", "SĐT không hợp lệ."); return; }

        KhachHang kh = khachHangDAO.getKhachHangBySdt(sdt);
        if (kh != null) {
            capNhatFormKH(kh.getMaKhachHang(), kh.getTenKhachHang(), kh.getSoDienThoai(), String.format("%.0f", kh.getDiemTichLuy()), false);
            lblTrangThaiTimKiem.setText("Tìm thấy khách hàng"); lblTrangThaiTimKiem.setTextFill(Color.web("#38A169"));
        } else {
            capNhatFormKH("000", "", sdt, "0", true);
            lblTrangThaiTimKiem.setText("Khách hàng mới"); lblTrangThaiTimKiem.setTextFill(Color.web("#E53E3E"));
        }
    }

    private void capNhatFormKH(String ma, String ten, String sdt, String diem, boolean isNew) {
        txtMaKh.setText(ma); txtTenKH.setText(ten); txtSdt.setText(sdt); txtDiem.setText(diem);
        txtTenKH.setEditable(isNew);
        if(isNew) styleTextField(txtTenKH); else styleTextFieldReadonly(txtTenKH);
    }

    private void themLogicGioiHanSoNguoi() {
        txtSoNguoi.textProperty().addListener((obs, oldV, newV) -> {
            if (!newV.matches("\\d*")) txtSoNguoi.setText(oldV);
        });
    }

    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type); alert.setTitle(title); alert.setHeaderText(null); alert.setContentText(content); alert.showAndWait();
    }

    // --- LOGIC XÁC NHẬN ĐẶT BÀN (TÁCH RIÊNG CHO GỌN) ---
    private void xuLyXacNhanDatBan() {
        try {
            String ten = txtTenKH.getText().trim(); String sdt = txtSdt.getText().trim();
            if (ten.isEmpty() || sdt.isEmpty()) { showAlert(AlertType.ERROR, "Lỗi", "Thiếu tên hoặc SĐT."); return; }

            KhachHang kh = new KhachHang();
            if (txtMaKh.getText().equals("000")) {
                if (khachHangDAO.getKhachHangBySdt(sdt) != null) { showAlert(AlertType.ERROR, "Lỗi", "SĐT đã tồn tại."); return; }
                kh.setTenKhachHang(ten); kh.setSoDienThoai(sdt); kh.setDiemTichLuy(0.0);
                if (!khachHangDAO.themKhachHangMoi(kh)) { showAlert(AlertType.ERROR, "Lỗi", "Không thêm được KH."); return; }
            } else kh.setMaKhachHang(txtMaKh.getText());

            NhanVien nv = new NhanVien(); nv.setMaNhanVien("NV001");
            LocalDateTime time = dpNgayDen.getValue().atStartOfDay().withHour(Integer.parseInt(cmbGioDen.getValue().substring(0,2))).withMinute(Integer.parseInt(cmbGioDen.getValue().substring(3,5)));

            if (time.isBefore(LocalDateTime.now())) { showAlert(AlertType.ERROR, "Lỗi", "Thời gian không hợp lệ."); return; }

            // Check trùng
            List<String> trung = new ArrayList<>();
            for(BanAn b : cacBanDuocChon) if(phieuDatBanDAO.kiemTraBanDaDatTrongNgay(b.getMaBan(), time)) trung.add(b.getMaBan());
            if(!trung.isEmpty()) { showAlert(AlertType.ERROR, "Trùng lịch", "Bàn đã đặt: " + String.join(", ", trung)); return; }

            HoaDon hd = new HoaDon(); hd.setKhachHang(kh); hd.setNhanVien(nv);
            String maHD = hoaDonDAO.themHoaDon(hd);
            if(maHD == null) return; hd.setMaHoaDon(maHD);

            boolean ok = true;
            for(BanAn b : cacBanDuocChon) {
                if(!banAn_DAO.updateTrangThaiBan(b, TrangThai.DA_DAT)) { ok=false; break; }
                PhieuDatBan p = new PhieuDatBan();
                p.setThoiGianBatDau(time); p.setTrangThai(radioDatTruoc.isSelected() ? "Đã đặt" : "Đang dùng");
                p.setSoNguoi(Integer.parseInt(txtSoNguoi.getText())); p.setGhiChu(txtGhiChu.getText());
                p.setKhachHang(kh); p.setBan(b); p.setNhanVien(nv); p.setHoaDon(hd);
                if(!phieuDatBanDAO.themPhieuDatBan(p, p.getTrangThai())) { ok=false; break; }
            }

            if(ok && !dsMonDaChon.isEmpty()) {
                for(ChiTietHoaDon c : dsMonDaChon) { c.setHoaDon(hd); if(!chiTietHoaDonDAO.themChiTietHoaDon(c)) { ok=false; break; } }
            }

            if(ok) {
                showAlert(AlertType.INFORMATION, "Thành công", "Đặt bàn thành công! Mã HĐ: " + maHD);
                trangChu.setMainContent(new Gui_DanhSachBan(trangChu));
            } else showAlert(AlertType.ERROR, "Lỗi", "Có lỗi xảy ra.");

        } catch(Exception e) { e.printStackTrace(); showAlert(AlertType.ERROR, "Lỗi", e.getMessage()); }
    }
}