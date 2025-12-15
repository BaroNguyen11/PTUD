package gui;

import java.util.HashMap;
import java.util.List;
import java.text.DecimalFormat;
import java.util.ArrayList;

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
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
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
    // DAO
    private MonAn_DAO monAnDAO;
    private KhachHang_DAO khachHangDAO;
    private BanAn_DAO banAn_DAO;
    private HoaDon_DAO hoaDonDAO;
    private PhieuDatBan_DAO phieuDatBanDAO;
    private ChiTietHoaDon_DAO chiTietHoaDonDAO;

    // Data
    private ObservableList<BanAn> dsBanDaChon;
    private ObservableList<ChiTietHoaDon> dsMonDaChon;
    private ObservableList<String> nguonGoiYMonAn; // Danh sách nguồn cho gợi ý (Format: "Mã,Tên")

    private final DecimalFormat df = new DecimalFormat("###,###đ");
    private Gui_TrangChu trangChu;
    private List<BanAn> cacBanDuocChon;
    private String currentCategory = "Tất cả"; // Lưu danh mục đang chọn

    // UI Elements
    private Label lblTongCoc;
    private Label lblTongTienMon;
    private TilePane menuTilePane;
    private Label lblTrangThaiKhachHang;
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
    private TextField txtTimMonAn; // Ô tìm kiếm món
    private TableView<ChiTietHoaDon> tblGioHang;
    private Map<String, Double> cacheGiaKhuyenMai = new HashMap<>();
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
        nguonGoiYMonAn = FXCollections.observableArrayList();

        // Chuẩn bị dữ liệu gợi ý ngay khi khởi tạo
        chuanBiDuLieuGoiY();

        initializeUI();
        taiDuLieuBanDau();
    }

    // Hàm lấy tất cả món ăn chuyển thành chuỗi "Mã,Tên" để gợi ý
    private void chuanBiDuLieuGoiY() {
        List<MonAn> allMon = monAnDAO.getAllMonAn();
        for (MonAn m : allMon) {
            // Lưu format: Mã món,Tên món
            nguonGoiYMonAn.add(m.getMaMonAn() + "," + m.getTenMonAn());
        }
    }

    private void initializeUI() {
        this.setPadding(new Insets(15));

        // CSS Style
        this.getStylesheets().add("data:text/css," + ".root { -fx-text-fill: #2d3436; }" + ".label { -fx-text-fill: #2d3436; }" + ".text-field { -fx-text-fill: #2d3436; -fx-prompt-text-fill: #a0aec0; }" + ".radio-button { -fx-text-fill: #2d3436; }" + ".button { -fx-text-fill: #2d3436; }" + ".table-view { -fx-background-color: transparent; -fx-border-color: #e2e8f0; -fx-border-radius: 5; }" + ".table-view .column-header-background { -fx-background-color: #f7fafc; -fx-border-width: 0 0 1 0; -fx-border-color: #e2e8f0; }" + ".table-view .column-header .label { -fx-text-fill: #4a5568; -fx-font-weight: bold; }" + ".table-row-cell { -fx-text-fill: #2d3436; }" + ".table-row-cell:filled:selected { -fx-background-color: #ebf8ff; }" + ".scroll-pane { -fx-background-color: transparent; }" + ".scroll-pane > .viewport { -fx-background-color: transparent; }" + ".text-area { -fx-background-color: white; -fx-border-color: #CBD5E0; -fx-border-radius: 5; -fx-text-fill: #2d3436; }" +
                // CSS cho Menu Gợi ý
                ".goi-y-menu { -fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2); }" + ".goi-y-item .label { -fx-text-fill: #2d3436; -fx-padding: 5 10; }" + ".goi-y-item:hover { -fx-background-color: #ebf8ff; }");

        HBox mainContent = new HBox(20);
        mainContent.setAlignment(Pos.TOP_CENTER);

        Node phanBenTrai = taoPhanBenTrai();
        VBox phanBenPhai = taoPhanBenPhai();

        HBox.setHgrow(phanBenTrai, Priority.ALWAYS);
        HBox.setHgrow(phanBenPhai, Priority.ALWAYS);

        if (phanBenTrai instanceof Region) {
            ((Region) phanBenTrai).setMinWidth(360);
            ((Region) phanBenTrai).setMaxWidth(450);
        }

        mainContent.getChildren().addAll(phanBenTrai, phanBenPhai);
        this.setCenter(mainContent);

        themLogicGioiHanSoNguoi();
        themLogicTuDongTimKiemSdt();
    }

    // --- FUNCTION GỢI Ý TÌM KIẾM BẠN YÊU CẦU ---
    private void caiDatGoiYTimKiem(TextField txtInput, ObservableList<String> dataNguon) {
        ContextMenu suggestionsPopup = new ContextMenu();
        suggestionsPopup.getStyleClass().add("goi-y-menu");

        // Popup rộng bằng ô input
        suggestionsPopup.setPrefWidth(txtInput.getPrefWidth());
        // Hoặc set min width nếu pref width chưa được tính
        suggestionsPopup.setMinWidth(200);

        Runnable hienThiGoiY = () -> {
            String tuKhoa = txtInput.getText().toLowerCase();
            List<MenuItem> suggestions = new ArrayList<>();

            for (String row : dataNguon) {
                String[] parts = row.split(",");
                if (parts.length < 2) continue;
                String maMon = parts[0];
                String tenMon = parts[1];

                // Tìm theo mã hoặc tên
                if (tuKhoa.isEmpty() || maMon.toLowerCase().contains(tuKhoa) || tenMon.toLowerCase().contains(tuKhoa)) {
                    MenuItem item = new MenuItem(tenMon); // Hiển thị tên món
                    item.getStyleClass().add("goi-y-item");

                    item.setOnAction(e -> {
                        txtInput.setText(tenMon); // Điền tên món vào ô
                        txtInput.positionCaret(tenMon.length());
                        suggestionsPopup.hide();
                        taiLaiDanhSachMonAn(); // Gọi hàm lọc lại danh sách món
                    });
                    suggestions.add(item);
                }
                if (suggestions.size() >= 10) break; // Giới hạn 10 gợi ý
            }

            if (!suggestions.isEmpty()) {
                suggestionsPopup.getItems().setAll(suggestions);
                if (!suggestionsPopup.isShowing()) {
                    suggestionsPopup.show(txtInput, Side.BOTTOM, 0, 0);
                }
            } else {
                suggestionsPopup.hide();
            }
        };

        txtInput.textProperty().addListener((observable, oldValue, newValue) -> hienThiGoiY.run());
        txtInput.setOnMouseClicked(event -> hienThiGoiY.run());
        txtInput.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) suggestionsPopup.hide();
        });
    }

    // ... (Giữ nguyên phần bên trái: taoPhanBenTrai, logic SĐT...) ...
    private Node taoPhanBenTrai() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(10));
        Label lblHeader = new Label("📝 THÔNG TIN ĐẶT BÀN");
        lblHeader.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        lblHeader.setTextFill(Color.web("#082744"));
        VBox cardKhachHang = taoCard("Thông tin khách hàng", taoNoiDungKhachHang());
        VBox cardDatBan = taoCard("Chi tiết đặt bàn", taoNoiDungDatBan());
        VBox cardBanChon = taoCard("Bàn đã chọn", taoNoiDungBanChon());
        container.getChildren().addAll(lblHeader, cardKhachHang, cardDatBan, cardBanChon);
        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return scrollPane;
    }

    private VBox taoCard(String title, Node content) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        Label lblTitle = new Label(title);
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        lblTitle.setTextFill(Color.web("#2d3436"));
        card.getChildren().addAll(lblTitle, new Separator(), content);
        return card;
    }

    private Node taoNoiDungKhachHang() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);
        Label lblSDT = new Label("SĐT (*):");
        lblSDT.setTextFill(Color.web("#2d3436"));
        txtSdt = new TextField();
        txtSdt.setPromptText("Nhập SĐT khách hàng...");
        styleTextField(txtSdt);
        lblTrangThaiKhachHang = new Label("Nhập SĐT để tự động tìm");
        lblTrangThaiKhachHang.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 11));
        lblTrangThaiKhachHang.setTextFill(Color.web("#718096"));
        Label lblHoTen = new Label("Họ tên:");
        lblHoTen.setTextFill(Color.web("#2d3436"));
        txtTenKH = new TextField();
        txtTenKH.setPromptText("Tên khách hàng");
        styleTextField(txtTenKH);
        Label lblMaKH = new Label("Mã KH:");
        lblMaKH.setTextFill(Color.web("#2d3436"));
        txtMaKh = new TextField("000");
        txtMaKh.setEditable(false);
        styleTextFieldReadonly(txtMaKh);
        Label lblDiem = new Label("Điểm:");
        lblDiem.setTextFill(Color.web("#2d3436"));
        txtDiem = new TextField("0");
        txtDiem.setEditable(false);
        styleTextFieldReadonly(txtDiem);
        grid.add(lblSDT, 0, 0);
        grid.add(txtSdt, 1, 0);
        grid.add(lblTrangThaiKhachHang, 1, 1);
        grid.add(lblHoTen, 0, 2);
        grid.add(txtTenKH, 1, 2);
        grid.add(lblMaKH, 0, 3);
        grid.add(txtMaKh, 1, 3);
        grid.add(lblDiem, 0, 4);
        grid.add(txtDiem, 1, 4);
        return grid;
    }

    private void themLogicTuDongTimKiemSdt() {
        txtSdt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtSdt.setText(oldValue);
                return;
            }
            if (newValue.length() >= 10) {
                KhachHang kh = khachHangDAO.getKhachHangBySdt(newValue);
                if (kh != null) {
                    txtMaKh.setText(kh.getMaKhachHang());
                    txtTenKH.setText(kh.getTenKhachHang());
                    txtDiem.setText(String.format("%.0f", kh.getDiemTichLuy()));
                    lblTrangThaiKhachHang.setText("✔ Khách hàng thân thiết");
                    lblTrangThaiKhachHang.setTextFill(Color.web("#38A169"));
                } else {
                    resetFormToNewCustomer(false);
                }
            } else {
                if (!txtMaKh.getText().equals("000")) {
                    resetFormToNewCustomer(false);
                }
                lblTrangThaiKhachHang.setText("Đang nhập...");
                lblTrangThaiKhachHang.setTextFill(Color.web("#718096"));
            }
        });
    }

    private void resetFormToNewCustomer(boolean clearSdt) {
        txtMaKh.setText("000");
        txtDiem.setText("0");
        txtTenKH.clear();
        if (clearSdt) txtSdt.clear();
        lblTrangThaiKhachHang.setText("✨ Khách hàng mới");
        lblTrangThaiKhachHang.setTextFill(Color.web("#3182CE"));
        txtTenKH.setEditable(true);
        styleTextField(txtTenKH);
    }

    private Node taoNoiDungDatBan() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
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
        txtSoNguoi = new TextField("0");
        styleTextField(txtSoNguoi);
        txtSoNguoi.setPromptText("Số khách");
        radioDatTruoc = new RadioButton("Đặt trước");
        radioDatTruoc.setTextFill(Color.web("#2d3436"));
        radioDungNgay = new RadioButton("Dùng ngay");
        radioDungNgay.setTextFill(Color.web("#2d3436"));
        ToggleGroup group = new ToggleGroup();
        radioDatTruoc.setToggleGroup(group);
        radioDungNgay.setToggleGroup(group);
        radioDatTruoc.setSelected(true);
        HBox boxRadio = new HBox(15, radioDatTruoc, radioDungNgay);
        txtGhiChu = new TextField();
        txtGhiChu.setPromptText("Ghi chú...");
        styleTextField(txtGhiChu);
        Label lblThoiGian = new Label("Thời gian:");
        lblThoiGian.setTextFill(Color.web("#2d3436"));
        Label lblSoNguoi = new Label("Số người:");
        lblSoNguoi.setTextFill(Color.web("#2d3436"));
        Label lblLoaiDat = new Label("Loại đặt:");
        lblLoaiDat.setTextFill(Color.web("#2d3436"));
        Label lblGhiChu = new Label("Ghi chú:");
        lblGhiChu.setTextFill(Color.web("#2d3436"));
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
    // PHẦN BÊN PHẢI: ÁP DỤNG GỢI Ý TÌM KIẾM
    // ======================================================================================

    private VBox taoPhanBenPhai() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(15));
        container.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        Label lblMenu = new Label("🍽️ THỰC ĐƠN");
        lblMenu.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        lblMenu.setTextFill(Color.web("#082744"));

        txtTimMonAn = new TextField();
        txtTimMonAn.setPromptText("Tìm tên món ăn...");
        txtTimMonAn.setPrefWidth(200);
        styleTextField(txtTimMonAn);

        // --- GỌI HÀM CÀI ĐẶT GỢI Ý ---
        caiDatGoiYTimKiem(txtTimMonAn, nguonGoiYMonAn);

        // Logic lọc khi gõ thường (dành cho trường hợp không click gợi ý mà gõ tay)
        txtTimMonAn.textProperty().addListener((obs, oldV, newV) -> taiLaiDanhSachMonAn());

        Button btnSearchIcon = new Button("🔍");
        btnSearchIcon.setStyle("-fx-background-color: #082744; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox searchBox = new HBox(5, txtTimMonAn, btnSearchIcon);
        searchBox.setAlignment(Pos.CENTER_RIGHT);
        header.getChildren().addAll(lblMenu, spacer, searchBox);



        menuTilePane = new TilePane();
        menuTilePane.setPadding(new Insets(5));
        menuTilePane.setHgap(15);
        menuTilePane.setVgap(15);
        menuTilePane.setPrefColumns(3);
        menuTilePane.setPrefTileWidth(160);
        menuTilePane.setStyle("-fx-background-color: transparent;");
        ScrollPane navBar = taoCategoryNavbar();
        ScrollPane scrollMenu = new ScrollPane(menuTilePane);
        scrollMenu.setFitToWidth(true);
        scrollMenu.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        VBox.setVgrow(scrollMenu, Priority.ALWAYS);

        Label lblGioHang = new Label("🛒 Món đã chọn");
        lblGioHang.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        lblGioHang.setTextFill(Color.web("#2d3436"));
         tblGioHang = taoBangGioHang();
        tblGioHang.setPrefHeight(150);
        tblGioHang.setMinHeight(100);

        HBox footer = new HBox(15);
        footer.setAlignment(Pos.CENTER_RIGHT);
        Label lblTongTienText = new Label("Tổng:");
        lblTongTienText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
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

        container.getChildren().addAll(header, navBar, scrollMenu, new Separator(), lblGioHang, tblGioHang, footer);
//        taiLaiDanhSachMonAn();
        return container;
    }

    private ScrollPane taoCategoryNavbar() {
        HBox navBox = new HBox(10);
        navBox.setPadding(new Insets(5, 0, 5, 0));
        navBox.setAlignment(Pos.CENTER_LEFT);
        String[] categories = {"Tất cả", "Món ăn kèm", "Món khai vị", "Món chính", "Nước sốt", "Đồ uống", "Tráng miệng"};
        ToggleGroup group = new ToggleGroup();
        for (String cat : categories) {
            ToggleButton btn = new ToggleButton(cat);
            btn.setToggleGroup(group);
            btn.setCursor(Cursor.HAND);
            btn.setStyle(getTabStyle(false));
            btn.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                if (isSelected) {
                    btn.setStyle(getTabStyle(true));
                    currentCategory = cat;
                    taiLaiDanhSachMonAn();
                } else {
                    btn.setStyle(getTabStyle(false));
                }
            });
            if (cat.equals("Tất cả")) {
                btn.setSelected(true);
                btn.setStyle(getTabStyle(true));
            }
            navBox.getChildren().add(btn);
        }

        ScrollPane scrollNav = new ScrollPane(navBox);
        scrollNav.setFitToHeight(true);
        scrollNav.setFitToWidth(true);
        scrollNav.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollNav.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollNav.setMinHeight(70);
        scrollNav.setPrefHeight(70);
        scrollNav.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return scrollNav;
    }

    private String getTabStyle(boolean isActive) {
        return isActive ? "-fx-background-color: #082744; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 8 15;" : "-fx-background-color: #EDF2F7; -fx-text-fill: #2d3436; -fx-font-weight: normal; -fx-background-radius: 20; -fx-padding: 8 15;";
    }

    private void taiLaiDanhSachMonAn() {
        menuTilePane.getChildren().clear();
        List<MonAn> dsTheoLoai = currentCategory.equals("Tất cả") ? monAnDAO.getAllMonAn() : monAnDAO.getMonAnByLoai(currentCategory);
        String keyword = txtTimMonAn.getText().trim().toLowerCase();
        List<MonAn> dsHienThi = new ArrayList<>();
        if (keyword.isEmpty()) {
            dsHienThi = dsTheoLoai;
        } else {
            for (MonAn m : dsTheoLoai) {
                if (m.getTenMonAn().toLowerCase().contains(keyword)) {
                    dsHienThi.add(m);
                }
            }
        }
        for (MonAn mon : dsHienThi) {
            ChiTietHoaDon c = timMonChon(mon);
            menuTilePane.getChildren().add(taoTheMonAn(mon, c != null ? c.getSoLuong() : 0));
        }
    }

    // ... (Giữ nguyên các hàm bổ trợ khác như TableView, taoTheMonAn, logic xử lý...) ...
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

    private VBox taoTheMonAn(MonAn mon, int soLuong) {
        VBox card = new VBox(8);
        card.setMinWidth(150);
        card.setMaxWidth(160);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        ImageView imgView = new ImageView();
        imgView.setFitWidth(130);
        imgView.setFitHeight(90);
        imgView.setPreserveRatio(true);
        if (mon.getHinhAnh() != null && !mon.getHinhAnh().isEmpty()) {
            String SUPABASE_BASE_URL = "https://yxemxycygkhxygaydgcl.supabase.co/storage/v1/object/public/image/";
            String imagePath = ImageCacheManager.getImagePath(SUPABASE_BASE_URL, mon.getHinhAnh());
            if (imagePath != null) {
                try {
                    imgView.setImage(new Image(imagePath));
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
        Label lblTen = new Label(mon.getTenMonAn());
        lblTen.setWrapText(true);
        lblTen.setAlignment(Pos.CENTER);
        lblTen.setTextAlignment(TextAlignment.CENTER);
        lblTen.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        lblTen.setTextFill(Color.web("#2d3436"));
        lblTen.setMinHeight(40);
        lblTen.setMaxWidth(Double.MAX_VALUE);
        Label lblGia = new Label(df.format(mon.getGiaTien()));
        lblGia.setTextFill(Color.web("#E53E3E"));
        lblGia.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        HBox controls = new HBox(8);
        controls.setAlignment(Pos.CENTER);
        Button btnTru = createRoundButton("-");
        Label lblSL = new Label(String.valueOf(soLuong));
        lblSL.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        lblSL.setTextFill(Color.web("#2d3436"));
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

    private ChiTietHoaDon timMonChon(MonAn mon) {
        for (ChiTietHoaDon c : dsMonDaChon) if (c.getMonAn().getMaMonAn().equals(mon.getMaMonAn())) return c;
        return null;
    }

    private void themMonVaoGio(MonAn mon) {
        ChiTietHoaDon c = timMonChon(mon);
        if (!cacheGiaKhuyenMai.containsKey(mon.getMaMonAn())) {
            double giaKM = monAnDAO.layGiaSauKhuyenMai(mon.getMaMonAn(), dpNgayDen.getValue(), mon.getGiaTien());
            cacheGiaKhuyenMai.put(mon.getMaMonAn(), giaKM);
        }

        if (c != null) {
            c.setSoLuong(c.getSoLuong() + 1);
            // TableView tự nhận biết thay đổi nếu dùng ObservableList đúng cách
            // Nhưng để chắc chắn TableView refresh cột thành tiền:
            tblGioHang.refresh();
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
            if (c.getSoLuong() == 0) {
                dsMonDaChon.remove(c);
            } else {
                tblGioHang.refresh(); // Chỉ refresh giao diện bảng thay vì set lại object
            }
            capNhatTongTienMon();
        }
    }

    private void capNhatTongTienMon() {
        double tong = 0;
        for (ChiTietHoaDon c : dsMonDaChon) {
            String maMon = c.getMonAn().getMaMonAn();
            double gia;

            // Kiểm tra xem đã có giá trong cache chưa
            if (cacheGiaKhuyenMai.containsKey(maMon)) {
                gia = cacheGiaKhuyenMai.get(maMon);
            } else {
                // Nếu chưa có thì mới gọi DAO và lưu vào cache
                gia = monAnDAO.layGiaSauKhuyenMai(maMon, dpNgayDen.getValue(), c.getMonAn().getGiaTien());
                cacheGiaKhuyenMai.put(maMon, gia);
            }

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
            String maNVHT = Gui_DangNhap.getCurrentMaNhanVien();
            if (maNVHT == null || maNVHT.isEmpty()) {
                showAlert(AlertType.ERROR, "Lỗi xác thực", "Không tìm thấy thông tin đăng nhập!\nVui lòng đăng xuất và đăng nhập lại.");
                return;
            }

            NhanVien nv = new NhanVien();
            nv.setMaNhanVien(maNVHT);
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