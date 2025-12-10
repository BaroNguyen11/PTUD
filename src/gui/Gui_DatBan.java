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
import javafx.scene.control.Alert.AlertType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
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

        initializeUI();
        taiDuLieuBanDau();
    }

    private void initializeUI() {
        // --- 1. ROOT STYLE ---
        this.setPadding(new Insets(15));
        this.getStylesheets().add("data:text/css," +
                ".root { -fx-text-fill: #2d3436; }" + // Thiết lập màu chữ mặc định cho root
                ".label { -fx-text-fill: #2d3436; }" + // Bắt buộc mọi Label phải màu đen xám
                ".text-field { -fx-text-fill: #2d3436; -fx-prompt-text-fill: #a0aec0; }" + // Màu chữ input và placeholder
                ".radio-button { -fx-text-fill: #2d3436; }" +
                ".button { -fx-text-fill: #2d3436; }" +

                // CSS cũ của bạn giữ nguyên bên dưới
                ".table-view { -fx-background-color: transparent; -fx-border-color: #e2e8f0; -fx-border-radius: 5; }" +
                ".table-view .column-header-background { -fx-background-color: #f7fafc; -fx-border-width: 0 0 1 0; -fx-border-color: #e2e8f0; }" +
                ".table-view .column-header .label { -fx-text-fill: #4a5568; -fx-font-weight: bold; }" +
                ".table-row-cell { -fx-text-fill: #2d3436; }" +
                ".table-row-cell:filled:selected { -fx-background-color: #ebf8ff; }" +
                ".scroll-pane { -fx-background-color: transparent; }" +
                ".scroll-pane > .viewport { -fx-background-color: transparent; }" +
                ".text-area { -fx-background-color: white; -fx-border-color: #CBD5E0; -fx-border-radius: 5; -fx-text-fill: #2d3436; }"
        );
        // --- 2. LAYOUT ---
        HBox mainContent = new HBox(20);
        mainContent.setAlignment(Pos.TOP_CENTER);

        // Tạo 2 phần: Trái (Form) & Phải (Menu)
        Node phanBenTrai = taoPhanBenTrai();
        VBox phanBenPhai = taoPhanBenPhai();

        // Chia tỷ lệ: Trái 35%, Phải 65%
        HBox.setHgrow(phanBenTrai, Priority.ALWAYS);
        HBox.setHgrow(phanBenPhai, Priority.ALWAYS);

        // Thiết lập kích thước tối thiểu/tối đa cho cột trái để UI không bị vỡ
        if (phanBenTrai instanceof Region) {
            ((Region) phanBenTrai).setMinWidth(360);
            ((Region) phanBenTrai).setMaxWidth(450);
        }

        mainContent.getChildren().addAll(phanBenTrai, phanBenPhai);
        this.setCenter(mainContent);

        themLogicGioiHanSoNguoi();

        // CSS Inline cho TableView đẹp hơn
        this.getStylesheets().add("data:text/css," + ".table-view { -fx-background-color: transparent; -fx-border-color: #e2e8f0; -fx-border-radius: 5; }" + ".table-view .column-header-background { -fx-background-color: #f7fafc; -fx-border-width: 0 0 1 0; -fx-border-color: #e2e8f0; }" + ".table-view .column-header .label { -fx-text-fill: #4a5568; -fx-font-weight: bold; }" + ".table-row-cell { -fx-text-fill: #2d3436; }" + ".table-row-cell:filled:selected { -fx-background-color: #ebf8ff; }" + ".scroll-pane { -fx-background-color: transparent; }" + ".scroll-pane > .viewport { -fx-background-color: transparent; }" + ".text-area { -fx-background-color: white; -fx-border-color: #CBD5E0; -fx-border-radius: 5; -fx-text-fill: #2d3436; }");
    }

    // ======================================================================================
    // PHẦN BÊN TRÁI: FORM NHẬP LIỆU (CÓ SCROLL)
    // ======================================================================================

    private Node taoPhanBenTrai() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(10)); // Padding trong ScrollPane

        // 1. Header Chung
        Label lblHeader = new Label("📝 THÔNG TIN ĐẶT BÀN");
        lblHeader.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        lblHeader.setTextFill(Color.web("#082744"));

        // 2. Các Card con
        VBox cardKhachHang = taoCard("Thông tin khách hàng", taoNoiDungKhachHang());
        VBox cardDatBan = taoCard("Chi tiết đặt bàn", taoNoiDungDatBan());
        VBox cardBanChon = taoCard("Bàn đã chọn", taoNoiDungBanChon());

        container.getChildren().addAll(lblHeader, cardKhachHang, cardDatBan, cardBanChon);

        // Bọc trong ScrollPane để tránh mất nội dung trên màn hình thấp
        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        return scrollPane;
    }

    // Hàm tiện ích tạo Card (Khung trắng có bóng đổ)
    private VBox taoCard(String title, Node content) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2); -fx-text-fill: #2d3436;");
        Label lblTitle = new Label(title);
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        lblTitle.setTextFill(Color.web("#2d3436"));

        card.getChildren().addAll(lblTitle, new Separator(), content);
        return card;
    }

    private Node taoNoiDungKhachHang() {
        VBox box = new VBox(10);

        // Tìm kiếm
        Label lblTim = new Label("Tìm khách hàng (SĐT):");
        lblTim.setFont(Font.font("Segoe UI", 12));
        lblTim.setTextFill(Color.web("#082744"));

        txtTimKiemSdt = new TextField();
        txtTimKiemSdt.setPromptText("Nhập số điện thoại...");
        styleTextField(txtTimKiemSdt);

        btnTimKiem = new Button("🔍");
        txtTimKiemSdt.setStyle("-fx-background-color: white; -fx-border-color: #CBD5E0; -fx-border-radius: 5; -fx-padding: 6; -fx-text-fill: #2d3436;");
        btnTimKiem.setOnAction(e -> timKiemKhachHang());

        HBox boxTim = new HBox(5, txtTimKiemSdt, btnTimKiem);
        HBox.setHgrow(txtTimKiemSdt, Priority.ALWAYS);

        lblTrangThaiTimKiem = new Label("");
        lblTrangThaiTimKiem.setFont(Font.font("Segoe UI", 12));

        // Form điền thông tin
        txtMaKh = new TextField("000");
        txtMaKh.setEditable(false);
        styleTextFieldReadonly(txtMaKh);

        txtTenKH = new TextField();
        styleTextField(txtTenKH);
        txtTenKH.setPromptText("Họ và tên");

        txtSdt = new TextField();
        styleTextFieldReadonly(txtSdt);
        txtSdt.setPromptText("Số điện thoại");

        txtDiem = new TextField("0");
        txtDiem.setEditable(false);
        styleTextFieldReadonly(txtDiem);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Tạo labels với màu chữ rõ ràng
        Label lblMaKH = new Label("Mã KH:");
        lblMaKH.setTextFill(Color.web("#2d3436")); // Màu đậm
        Label lblHoTen = new Label("Họ tên:");
        lblHoTen.setTextFill(Color.web("#2d3436"));
        Label lblSDT = new Label("SĐT:");
        lblSDT.setTextFill(Color.web("#2d3436"));
        Label lblDiem = new Label("Điểm:");
        lblDiem.setTextFill(Color.web("#2d3436"));

        grid.add(lblMaKH, 0, 0);
        grid.add(txtMaKh, 1, 0);
        grid.add(lblHoTen, 0, 1);
        grid.add(txtTenKH, 1, 1);
        grid.add(lblSDT, 0, 2);
        grid.add(txtSdt, 1, 2);
        grid.add(lblDiem, 0, 3);
        grid.add(txtDiem, 1, 3);

        box.getChildren().addAll(lblTim, boxTim, lblTrangThaiTimKiem, grid);
        return box;
    }

    private Node taoNoiDungDatBan() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);

        // Thời gian
        cmbGioDen = new ComboBox<>();
        for (int h = 0; h < 24; h++) {
            cmbGioDen.getItems().add(String.format("%02d:00", h));
            cmbGioDen.getItems().add(String.format("%02d:30", h));
        }
        cmbGioDen.setValue("18:00");
        cmbGioDen.setPrefWidth(90);

        dpNgayDen = new DatePicker(ngayDatBan);
        dpNgayDen.setPrefWidth(140);
        dpNgayDen.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
        HBox boxTime = new HBox(10, cmbGioDen, dpNgayDen);

        // Số người
        txtSoNguoi = new TextField("0");
        styleTextField(txtSoNguoi);
        txtSoNguoi.setPromptText("Số khách");

        // Loại đặt
        radioDatTruoc = new RadioButton("Đặt trước");
        radioDatTruoc.setTextFill(Color.web("#2d3436"));
        radioDungNgay = new RadioButton("Dùng ngay");
        radioDungNgay.setTextFill(Color.web("#2d3436"));
        ToggleGroup group = new ToggleGroup();
        radioDatTruoc.setToggleGroup(group);
        radioDungNgay.setToggleGroup(group);
        radioDatTruoc.setSelected(true);
        HBox boxRadio = new HBox(15, radioDatTruoc, radioDungNgay);

        // Ghi chú
        txtGhiChu = new TextField();
        txtGhiChu.setPromptText("Ghi chú (ghế trẻ em, sinh nhật...)");
        styleTextField(txtGhiChu);

        // Add to Grid với labels có màu
        Label lblThoiGian = new Label("Thời gian:");
        lblThoiGian.setTextFill(Color.web("#2d3436")); // Màu đậm

        Label lblSoNguoi = new Label("Số người:");
        lblSoNguoi.setTextFill(Color.web("#2d3436"));

        Label lblLoaiDat = new Label("Loại đặt:");
        lblLoaiDat.setTextFill(Color.web("#2d3436"));

        Label lblGhiChu = new Label("Ghi chú:");
        lblGhiChu.setTextFill(Color.web("#2d3436"));
        // Radio Button cũng cần set màu
        radioDatTruoc.setTextFill(Color.web("#2d3436"));
        radioDungNgay.setTextFill(Color.web("#2d3436"));
        txtGhiChu.setStyle("-fx-background-color: white; -fx-border-color: #CBD5E0; -fx-border-radius: 5; -fx-padding: 6; -fx-text-fill: #2d3436;");
        grid.add(lblThoiGian, 0, 0);
        grid.add(boxTime, 1, 0);
        grid.add(lblSoNguoi, 0, 1);
        grid.add(txtSoNguoi, 1, 1);
        grid.add(lblLoaiDat, 0, 2);
        grid.add(boxRadio, 1, 2);
        grid.add(lblGhiChu, 0, 3);
        grid.add(txtGhiChu, 1, 3);

        return grid;
    }

    private Node taoNoiDungBanChon() {
        VBox box = new VBox(10);

        TableView<BanAn> table = new TableView<>();
        table.setPrefHeight(120);
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

        HBox boxCoc = new HBox(5);
        boxCoc.setAlignment(Pos.CENTER_RIGHT);
        Label lblTotal = new Label("Tổng cọc:");
        lblTotal.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        lblTotal.setTextFill(Color.web("#2d3436"));
        lblTongCoc = new Label("0đ");
        lblTongCoc.setTextFill(Color.web("#E53E3E"));
        lblTongCoc.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        boxCoc.getChildren().addAll(lblTotal, lblTongCoc);

        box.getChildren().addAll(table, boxCoc);
        return box;
    }

    // ======================================================================================
    // PHẦN BÊN PHẢI: MENU MÓN ĂN & GIỎ HÀNG
    // ======================================================================================

    private VBox taoPhanBenPhai() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(15));
        container.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2); -fx-text-fill: #2d3436;");

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

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(lblMenu, spacer, comboLoai);

        // 2. Grid Món ăn (Menu) - SCROLLABLE
        menuTilePane = new TilePane();
        menuTilePane.setPadding(new Insets(5));
        menuTilePane.setHgap(15);
        menuTilePane.setVgap(15);
        menuTilePane.setPrefColumns(3);
        menuTilePane.setPrefTileWidth(160);
        menuTilePane.setPrefTileHeight(Region.USE_COMPUTED_SIZE);
        menuTilePane.setStyle("-fx-background-color: transparent;");

        ScrollPane scrollMenu = new ScrollPane(menuTilePane);
        scrollMenu.setFitToWidth(true);
        scrollMenu.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        VBox.setVgrow(scrollMenu, Priority.ALWAYS);

        // 3. Giỏ hàng
        Label lblGioHang = new Label("🛒 Món đã chọn");
        lblGioHang.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        lblGioHang.setTextFill(Color.web("#2d3436"));

        TableView<ChiTietHoaDon> tblGioHang = taoBangGioHang();
        tblGioHang.setPrefHeight(150);
        tblGioHang.setMinHeight(100);

        // 4. Footer (Tổng tiền & Nút lệnh)
        HBox footer = new HBox(15);
        footer.setAlignment(Pos.CENTER_RIGHT);

        Label lblTongTienText = new Label("Tổng:");
        lblTongTienText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        lblTongTienText.setTextFill(Color.web("#2d3436"));
        lblTongTienMon = new Label("0đ");
        lblTongTienMon.setTextFill(Color.web("#E53E3E"));
        lblTongTienMon.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));

        Button btnQuayLai = new Button("Quay lại");
        btnQuayLai.setStyle("-fx-background-color: #CBD5E0; -fx-text-fill: #2d3436; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 20;");
        btnQuayLai.setOnAction(e -> trangChu.setMainContent(new Gui_DanhSachBan(trangChu)));

        Button btnXacNhan = new Button("✅ ĐẶT BÀN");
        btnXacNhan.setStyle("-fx-background-color: #082744; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 20;");
        btnXacNhan.setOnAction(e -> xuLyXacNhanDatBan());

        footer.getChildren().addAll(lblTongTienText, lblTongTienMon, new Region(), btnQuayLai, btnXacNhan);
        HBox.setHgrow(footer.getChildren().get(2), Priority.ALWAYS);

        container.getChildren().addAll(header, scrollMenu, new Separator(), lblGioHang, tblGioHang, footer);

        taiLaiDanhSachMonAn("Tất cả");
        return container;
    }

    private TableView<ChiTietHoaDon> taoBangGioHang() {
        TableView<ChiTietHoaDon> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ChiTietHoaDon, String> colTen = new TableColumn<>("Món");
        colTen.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getMonAn().getTenMonAn()));

        TableColumn<ChiTietHoaDon, Integer> colSL = new TableColumn<>("SL");
        colSL.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        colSL.setPrefWidth(40);

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

    // --- CARD MÓN ĂN ĐƯỢC CẢI TIẾN (FIX LỖI CHỮ BỊ ẨN) ---
    private VBox taoTheMonAn(MonAn mon, int soLuong) {
        VBox card = new VBox(8);
        card.setMinWidth(150);
        card.setMaxWidth(160);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(10));

        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; " + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        // 1. Ảnh
        ImageView imgView = new ImageView();
        imgView.setFitWidth(130);
        imgView.setFitHeight(90);
        imgView.setPreserveRatio(true);

        if (mon.getHinhAnh() != null && !mon.getHinhAnh().isEmpty()) {
            String SUPABASE_BASE_URL = "https://yxemxycygkhxygaydgcl.supabase.co/storage/v1/object/public/image/";
            String imagePath = ImageCacheManager.getImagePath(SUPABASE_BASE_URL, mon.getHinhAnh());
            if (imagePath != null) {
                try {
                    Image img = new Image(imagePath);
                    imgView.setImage(img);
                    imgView.setPreserveRatio(false);
                    imgView.setFitHeight(90);
                } catch (Exception e) {
                    loadDefaultImage(imgView);
                }
            } else loadDefaultImage(imgView);
        } else loadDefaultImage(imgView);

        Rectangle clip = new Rectangle(130, 90);
        clip.setArcWidth(10);
        clip.setArcHeight(10);
        imgView.setClip(clip);

        // 2. Tên & Giá (FIX LỖI CHỮ ẨN)
        Label lblTen = new Label(mon.getTenMonAn());
        lblTen.setWrapText(true);
        lblTen.setAlignment(Pos.CENTER);
        lblTen.setTextAlignment(TextAlignment.CENTER);
        lblTen.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        lblTen.setTextFill(Color.web("#2d3436")); // ✅ THÊM MÀU CHỮ
        lblTen.setMinHeight(40);
        lblTen.setMaxWidth(Double.MAX_VALUE);

        Label lblGia = new Label(df.format(mon.getGiaTien()));
        lblGia.setTextFill(Color.web("#E53E3E"));
        lblGia.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));

        // 3. Controls Số lượng
        HBox controls = new HBox(8);
        controls.setAlignment(Pos.CENTER);

        Button btnTru = createRoundButton("-");
        Label lblSL = new Label(String.valueOf(soLuong));
        lblSL.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        lblSL.setTextFill(Color.web("#2d3436")); // ✅ THÊM MÀU CHỮ
        lblSL.setMinWidth(20);
        lblSL.setAlignment(Pos.CENTER);
        Button btnCong = createRoundButton("+");

        btnCong.setOnAction(e -> {
            themMonVaoGio(mon);
            ChiTietHoaDon c = timMonChon(mon);
            if (c != null) lblSL.setText(String.valueOf(c.getSoLuong()));
        });
        btnTru.setOnAction(e -> {
            botMonKhoiGio(mon);
            ChiTietHoaDon c = timMonChon(mon);
            lblSL.setText(String.valueOf(c != null ? c.getSoLuong() : 0));
        });

        controls.getChildren().addAll(btnTru, lblSL, btnCong);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        card.getChildren().addAll(imgView, lblTen, lblGia, spacer, controls);

        // Hover Effect
        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 4);");
            card.setCursor(Cursor.HAND);
        });
        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
            card.setCursor(Cursor.DEFAULT);
        });

        return card;
    }

    private Button createRoundButton(String text) {
        Button btn = new Button(text);
        btn.setPrefSize(28, 28);
        btn.setStyle("-fx-background-color: #EDF2F7; -fx-text-fill: #2d3436; -fx-font-weight: bold; -fx-background-radius: 14; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #E2E8F0; -fx-text-fill: #2d3436; -fx-font-weight: bold; -fx-background-radius: 14;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #EDF2F7; -fx-text-fill: #2d3436; -fx-font-weight: bold; -fx-background-radius: 14;"));
        return btn;
    }

    private void styleTextField(TextField tf) {
        tf.setStyle("-fx-background-color: white; -fx-border-color: #CBD5E0; -fx-border-radius: 5; -fx-padding: 6; -fx-text-fill: #2d3436;");
    }

    private void styleTextFieldReadonly(TextField tf) {
        tf.setStyle("-fx-background-color: #F7FAFC; -fx-border-color: #E2E8F0; -fx-border-radius: 5; -fx-padding: 6; -fx-text-fill: #718096;");
    }

    private void loadDefaultImage(ImageView imgView) {
        try {
            imgView.setImage(new Image(getClass().getResourceAsStream("/img/default-food.png")));
        } catch (Exception e) {
            imgView.setStyle("-fx-background-color: #E2E8F0;");
        }
    }

    // ======================================================================================
    // LOGIC NGHIỆP VỤ
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
        for (ChiTietHoaDon c : dsMonDaChon)
            if (c.getMonAn().getMaMonAn().equals(mon.getMaMonAn())) return c;
        return null;
    }

    private void themMonVaoGio(MonAn mon) {
        ChiTietHoaDon c = timMonChon(mon);
        if (c != null) {
            c.setSoLuong(c.getSoLuong() + 1);
            dsMonDaChon.set(dsMonDaChon.indexOf(c), c);
        } else {
            ChiTietHoaDon n = new ChiTietHoaDon();
            n.setMonAn(mon);
            n.setSoLuong(1);
            dsMonDaChon.add(n);
        }
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
        double tong = 0;
        LocalDate ngay = dpNgayDen.getValue();
        for (ChiTietHoaDon c : dsMonDaChon) {
            double gia = monAnDAO.layGiaSauKhuyenMai(c.getMonAn().getMaMonAn(), ngay, c.getMonAn().getGiaTien());
            tong += gia * c.getSoLuong();
        }
        lblTongTienMon.setText(df.format(tong));
    }

    private void taiDuLieuBanDau() {
        dsBanDaChon.addAll(cacBanDuocChon);
        double tongCoc = 0;
        int tongNguoi = 0;
        for (BanAn b : dsBanDaChon) {
            boolean isVip = b.getLoai().getTenLoai().equalsIgnoreCase("VIP");
            tongCoc += isVip ? 450000 : 300000;
            tongNguoi += isVip ? 6 : 4;
        }
        lblTongCoc.setText(df.format(tongCoc));
        if (txtSoNguoi != null) txtSoNguoi.setText(String.valueOf(tongNguoi));
    }

    private void timKiemKhachHang() {
        String sdt = txtTimKiemSdt.getText().trim();
        if (sdt.isEmpty()) {
            showAlert(AlertType.WARNING, "Cảnh báo", "Vui lòng nhập SĐT.");
            return;
        }
        if (!sdt.matches("^0[0-9]{9}$")) {
            showAlert(AlertType.ERROR, "Lỗi", "SĐT không hợp lệ.");
            return;
        }

        KhachHang kh = khachHangDAO.getKhachHangBySdt(sdt);
        if (kh != null) {
            capNhatFormKH(kh.getMaKhachHang(), kh.getTenKhachHang(), kh.getSoDienThoai(), String.format("%.0f", kh.getDiemTichLuy()), false);
            lblTrangThaiTimKiem.setText("Tìm thấy khách hàng");
            lblTrangThaiTimKiem.setTextFill(Color.web("#38A169"));
        } else {
            capNhatFormKH("000", "", sdt, "0", true);
            lblTrangThaiTimKiem.setText("Khách hàng mới");
            lblTrangThaiTimKiem.setTextFill(Color.web("#E53E3E"));
        }
    }

    private void capNhatFormKH(String ma, String ten, String sdt, String diem, boolean isNew) {
        txtMaKh.setText(ma);
        txtTenKH.setText(ten);
        txtSdt.setText(sdt);
        txtDiem.setText(diem);
        txtTenKH.setEditable(isNew);
        if (isNew) styleTextField(txtTenKH);
        else styleTextFieldReadonly(txtTenKH);
    }

    private void themLogicGioiHanSoNguoi() {
        txtSoNguoi.textProperty().addListener((obs, oldV, newV) -> {
            if (!newV.matches("\\d*")) txtSoNguoi.setText(oldV);
        });
    }

    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void xuLyXacNhanDatBan() {
        try {
            String ten = txtTenKH.getText().trim();
            String sdt = txtSdt.getText().trim();
            if (ten.isEmpty() || sdt.isEmpty()) {
                showAlert(AlertType.ERROR, "Lỗi", "Thiếu tên hoặc SĐT.");
                return;
            }

            KhachHang kh = new KhachHang();
            if (txtMaKh.getText().equals("000")) {
                if (khachHangDAO.getKhachHangBySdt(sdt) != null) {
                    showAlert(AlertType.ERROR, "Lỗi", "SĐT đã tồn tại.");
                    return;
                }
                kh.setTenKhachHang(ten);
                kh.setSoDienThoai(sdt);
                kh.setDiemTichLuy(0.0);
                if (!khachHangDAO.themKhachHangMoi(kh)) {
                    showAlert(AlertType.ERROR, "Lỗi", "Không thêm được KH.");
                    return;
                }
            } else kh.setMaKhachHang(txtMaKh.getText());

            NhanVien nv = new NhanVien();
            nv.setMaNhanVien("NV001");
            LocalDateTime time = dpNgayDen.getValue().atStartOfDay().withHour(Integer.parseInt(cmbGioDen.getValue().substring(0, 2))).withMinute(Integer.parseInt(cmbGioDen.getValue().substring(3, 5)));

            if (time.isBefore(LocalDateTime.now())) {
                showAlert(AlertType.ERROR, "Lỗi", "Thời gian không hợp lệ.");
                return;
            }

            List<String> trung = new ArrayList<>();
            for (BanAn b : cacBanDuocChon)
                if (phieuDatBanDAO.kiemTraBanDaDatTrongNgay(b.getMaBan(), time)) trung.add(b.getMaBan());
            if (!trung.isEmpty()) {
                showAlert(AlertType.ERROR, "Trùng lịch", "Bàn đã đặt: " + String.join(", ", trung));
                return;
            }

            HoaDon hd = new HoaDon();
            hd.setKhachHang(kh);
            hd.setNhanVien(nv);
            String maHD = hoaDonDAO.themHoaDon(hd);
            if (maHD == null) return;
            hd.setMaHoaDon(maHD);

            boolean ok = true;
            for (BanAn b : cacBanDuocChon) {
                if (!banAn_DAO.updateTrangThaiBan(b, TrangThai.DA_DAT)) {
                    ok = false;
                    break;
                }
                PhieuDatBan p = new PhieuDatBan();
                p.setThoiGianBatDau(time);
                p.setTrangThai(radioDatTruoc.isSelected() ? "Đã đặt" : "Đang dùng");
                p.setSoNguoi(Integer.parseInt(txtSoNguoi.getText()));
                p.setGhiChu(txtGhiChu.getText());
                p.setKhachHang(kh);
                p.setBan(b);
                p.setNhanVien(nv);
                p.setHoaDon(hd);
                if (!phieuDatBanDAO.themPhieuDatBan(p, p.getTrangThai())) {
                    ok = false;
                    break;
                }
            }

            if (ok && !dsMonDaChon.isEmpty()) {
                for (ChiTietHoaDon c : dsMonDaChon) {
                    c.setHoaDon(hd);
                    if (!chiTietHoaDonDAO.themChiTietHoaDon(c)) {
                        ok = false;
                        break;
                    }
                }
            }

            if (ok) {
                showAlert(AlertType.INFORMATION, "Thành công", "Đặt bàn thành công! Mã HĐ: " + maHD);
                trangChu.setMainContent(new Gui_DanhSachBan(trangChu));
            } else showAlert(AlertType.ERROR, "Lỗi", "Có lỗi xảy ra.");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Lỗi", e.getMessage());
        }
    }
}