
package client.gui;

import client.ctrl.QLHD_Ctrl;
import client.service.PhieuDatBanClient;
import javafx.animation.FadeTransition;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

public class Gui_QuanLiHoaDon extends BorderPane {
    private QLHD_Ctrl control;

    // Data Lists
    private ObservableList<String> dsThongTinMonAn = FXCollections.observableArrayList();
    private ObservableList<String> dsHoaDon = FXCollections.observableArrayList();

    // Detail Fields
    private TextField txtMaHoaDon, txtKhachHang, txtNhanVien, txtTongTien, txtPhuongThuc;
    private TextField txtTienCoc, txtNgayTao, txtBanTra, txtThue, txtTrangThai, txtTienTT, txtGiamGia;

    // Tables
    private TableView<String> tableMonAn;
    private TableView<String> tableHoaDon;

    // Search & Filter
    private TextField txtTimKiem;
    private ComboBox<String> cboTrangThai;
    private DatePicker ngayLoc;
    private Button btnReset;
    private Button btnIn;

    private boolean flag = false;
    private final Map<KeyCombination, Runnable> shortcuts = new HashMap<>();

    public Gui_QuanLiHoaDon() {
        control = new QLHD_Ctrl();
        initializeUI();
        setupShortcuts();
    }

    private void initializeUI() {
        // --- 1. SETUP ROOT STYLE ---
        this.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa 0%, #e9ecef 100%);");
        this.setPadding(new Insets(20));

        // --- 2. LAYOUT STRUCTURE ---
        VBox rootContent = new VBox(20);

        // Header
        VBox header = createModernHeader();

        // Main Body (Split into Left Filter Pane and Right Content Pane)
        HBox body = new HBox(20);
        VBox.setVgrow(body, Priority.ALWAYS);

        // Left Pane: Search & Filter Card
        VBox leftPane = createLeftPane();

        // Right Pane: Tables & Details
        VBox rightPane = createRightPane();
        HBox.setHgrow(rightPane, Priority.ALWAYS);

        body.getChildren().addAll(leftPane, rightPane);
        rootContent.getChildren().addAll(header, body);

        this.setCenter(rootContent);

        // --- 3. ANIMATION ---
        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), rootContent);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    // =============================================================
    // 🎨 HEADER MODERN
    // =============================================================
    private VBox createModernHeader() {
        VBox header = new VBox(10);
        header.setPadding(new Insets(20, 30, 20, 30));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle(
                "-fx-background-color: #082744;" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 4);"
        );

        Label title = new Label("QUẢN LÝ HÓA ĐƠN");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: white;");

        Label subtitle = new Label("Tra cứu, xem chi tiết và in hóa đơn thanh toán");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        subtitle.setStyle("-fx-text-fill: rgba(255,255,255,0.9);");

        header.getChildren().addAll(title, subtitle);
        return header;
    }

    // =============================================================
    // ⬅️ LEFT PANE (FILTER & SEARCH)
    // =============================================================
    private VBox createLeftPane() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));
        container.setPrefWidth(320);
        container.setMinWidth(320);
        container.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);"
        );

        Label lblTitle = new Label("TÌM KIẾM & LỌC");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        lblTitle.setStyle("-fx-text-fill: #082744;");

        // Search Field
        txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Nhập mã hóa đơn...");
        styleInputField(txtTimKiem);
        txtTimKiem.setTooltip(new Tooltip("Nhấn F3 để tìm nhanh"));

        // Date Picker
        ngayLoc = new DatePicker();
        ngayLoc.setPromptText("Chọn ngày...");
        styleDatePicker(ngayLoc);

        // Status Filter
        String[] trangThai = {"Tất cả", "Đã thanh toán", "Chưa thanh toán", "Đã hủy"};
        cboTrangThai = new ComboBox<>();
        cboTrangThai.getItems().addAll(trangThai);
        cboTrangThai.setValue("Tất cả");
        styleComboBox(cboTrangThai);

        // Buttons
        Button btnTim = new Button("Tìm kiếm");
        styleButton(btnTim, "#082744", "white");
        btnTim.setMaxWidth(Double.MAX_VALUE);

        btnReset = new Button("Làm mới bộ lọc");
        styleButton(btnReset, "#f1f3f5", "#495057");
        btnReset.setMaxWidth(Double.MAX_VALUE);

        // --- Logic Handlers ---
        Runnable doSearch = () -> locDanhSach(txtTimKiem.getText(), ngayLoc.getValue(), cboTrangThai.getValue());

        txtTimKiem.setOnAction(e -> doSearch.run());
        btnTim.setOnAction(e -> doSearch.run());
        ngayLoc.setOnAction(e -> doSearch.run());
        cboTrangThai.setOnAction(e -> doSearch.run());

        btnReset.setOnAction(e -> taoMoiTimKiem());

        // Layout Add
        container.getChildren().addAll(
                lblTitle,
                new Label("Từ khóa:"), txtTimKiem,
                new Label("Ngày tạo:"), ngayLoc,
                new Label("Trạng thái:"), cboTrangThai,
                new Separator(),
                btnTim, btnReset
        );

        return container;
    }

    // =============================================================
    // ➡️ RIGHT PANE (TABLES & DETAILS)
    // =============================================================
    private VBox createRightPane() {
        VBox container = new VBox(15);

        // --- SECTION 1: INVOICE LIST (TOP) ---
        VBox invoiceSection = createInvoiceTableSection();
        VBox.setVgrow(invoiceSection, Priority.ALWAYS); // Chiếm phần lớn không gian

        // --- SECTION 2: DETAILS & ITEMS (BOTTOM) ---
        HBox detailsSection = new HBox(15);
        detailsSection.setPrefHeight(350); // Chiều cao cố định cho phần chi tiết

        VBox invoiceInfo = createInvoiceInfoCard();
        VBox itemTable = createItemTableSection();

        HBox.setHgrow(invoiceInfo, Priority.ALWAYS); // Chiếm 60%
        HBox.setHgrow(itemTable, Priority.ALWAYS);   // Chiếm 40%
        invoiceInfo.setPrefWidth(600);
        itemTable.setPrefWidth(400);

        detailsSection.getChildren().addAll(invoiceInfo, itemTable);

        container.getChildren().addAll(invoiceSection, detailsSection);
        return container;
    }

    // --- SUB-SECTION: INVOICE TABLE ---
    private VBox createInvoiceTableSection() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        Label lblTitle = new Label("DANH SÁCH HÓA ĐƠN");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        lblTitle.setStyle("-fx-text-fill: #082744;");

        // Setup Table
        dsHoaDon = FXCollections.observableList(control.loadDSHoaDon());
        tableHoaDon = new TableView<>(dsHoaDon);
        styleTable(tableHoaDon);

        // Define Columns
        TableColumn<String, String> colMa = new TableColumn<>("Mã HĐ");
        colMa.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().split(",")[0]));

        TableColumn<String, String> colKH = new TableColumn<>("Khách Hàng");
        colKH.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().split(",")[1]));

        TableColumn<String, String> colBan = new TableColumn<>("Bàn");
        colBan.setCellValueFactory(d -> {
//            String[] arr = d.getValue().split(",")[9].split("_");
//            return new SimpleStringProperty(String.join(", ", arr));
            String[] rowData = d.getValue().split(",", -1); // Use -1 to preserve empty slots

            // Check if index 9 exists
            if (rowData.length > 9) {
                String rawBan = rowData[9]; // This is the 'danhSachBan'
                return new SimpleStringProperty(rawBan.replace("_", ", "));
            } else {
                return new SimpleStringProperty("");
            }
        });

        TableColumn<String, String> colTong = new TableColumn<>("Tổng Tiền");
        DecimalFormat dtf = new DecimalFormat("#,##0.0 đ");
        colTong.setCellValueFactory(d -> new SimpleStringProperty(dtf.format(Double.parseDouble(d.getValue().split(",")[3]))));
        colTong.setStyle("-fx-alignment: CENTER-RIGHT; -fx-font-weight: bold; -fx-text-fill: #27ae60;");

        TableColumn<String, String> colPT = new TableColumn<>("Phương Thức");
        colPT.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().split(",")[5]));
        colPT.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    return;
                }
                Label lbl = new Label(item);
                lbl.setStyle("-fx-font-weight: bold; -fx-padding: 3 8; -fx-background-radius: 4; -fx-font-size: 11px;");
                if (item.equalsIgnoreCase("Tiền mặt")) {
                    lbl.setStyle(lbl.getStyle() + "-fx-background-color: #e8f0fe; -fx-text-fill: #1967d2;");
                } else {
                    lbl.setStyle(lbl.getStyle() + "-fx-background-color: #e6fffa; -fx-text-fill: #047481;");
                }
                setGraphic(lbl);
                setAlignment(Pos.CENTER);
            }
        });

        TableColumn<String, String> colNgay = new TableColumn<>("Ngày Tạo");
        colNgay.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().split(",")[6]));

        TableColumn<String, String> colTT = new TableColumn<>("Trạng Thái");
        colTT.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().split(",")[7]));
        colTT.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    return;
                }
                Label lbl = new Label(item);
                lbl.setStyle("-fx-font-weight: bold; -fx-padding: 3 8; -fx-background-radius: 10; -fx-font-size: 11px;");
                if (item.equalsIgnoreCase("Đã thanh toán")) {
                    lbl.setStyle(lbl.getStyle() + "-fx-background-color: #d4edda; -fx-text-fill: #155724;");
                } else if(item.equalsIgnoreCase("Chưa thanh toán")){
                    lbl.setStyle(lbl.getStyle() + "-fx-background-color: #f8d7da; -fx-text-fill: #721c24;");
                } else{
                    lbl.setStyle(lbl.getStyle() + "-fx-background-color: #fdcece; -fx-text-fill: #ff2020;");
                }
                setGraphic(lbl);
                setAlignment(Pos.CENTER);
            }
        });

        tableHoaDon.getColumns().addAll(colMa, colKH, colBan, colTong, colPT, colNgay, colTT);

        // Click Event
        tableHoaDon.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) selectInvoice(newV);
        });

        box.getChildren().addAll(lblTitle, tableHoaDon);
        return box;
    }

    // --- SUB-SECTION: INVOICE INFO (DETAILS) ---
    private VBox createInvoiceInfoCard() {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        HBox header = new HBox();
        Label lblTitle = new Label("CHI TIẾT HÓA ĐƠN");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        lblTitle.setStyle("-fx-text-fill: #082744;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        btnIn = new Button("🖨 In Hóa Đơn (F8)");
        styleButton(btnIn, "#10ac84", "white");
        btnIn.setOnAction(e -> thucHienIn());

        header.getChildren().addAll(lblTitle, spacer, btnIn);

        // Form Grid
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);

        // Init Fields
        txtMaHoaDon = createReadOnlyField();
        txtKhachHang = createReadOnlyField();
        txtNhanVien = createReadOnlyField();
        txtBanTra = createReadOnlyField();
        txtPhuongThuc = createReadOnlyField();
        txtNgayTao = createReadOnlyField();
        txtTongTien = createReadOnlyField();
        txtGiamGia = createReadOnlyField();
        txtThue = createReadOnlyField();
        txtTienCoc = createReadOnlyField();
        txtTienTT = createReadOnlyField();
        txtTienTT.setStyle("-fx-text-fill: #c0392b; -fx-font-weight: bold; -fx-background-color: #f1f2f6;");
        txtTrangThai = createReadOnlyField();

        // Add to Grid
        addInfoRow(grid, "Mã HĐ:", txtMaHoaDon, 0, 0);
        addInfoRow(grid, "Khách hàng:", txtKhachHang, 0, 1);
        addInfoRow(grid, "Nhân viên:", txtNhanVien, 0, 2);
        addInfoRow(grid, "Bàn:", txtBanTra, 0, 3);
        addInfoRow(grid, "Ngày tạo:", txtNgayTao, 0, 4);
        addInfoRow(grid, "Trạng thái:", txtTrangThai, 0, 5);

        addInfoRow(grid, "Tạm tính:", txtTongTien, 1, 0);
        addInfoRow(grid, "Giảm giá:", txtGiamGia, 1, 1);
        addInfoRow(grid, "Thuế VAT:", txtThue, 1, 2);
        addInfoRow(grid, "Tiền cọc:", txtTienCoc, 1, 3);
        addInfoRow(grid, "P.Thức:", txtPhuongThuc, 1, 4);

        Label lblTotal = new Label("TỔNG CỘNG:");
        lblTotal.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        grid.add(lblTotal, 2, 5);
        grid.add(txtTienTT, 3, 5);

        card.getChildren().addAll(header, new Separator(), grid);
        return card;
    }

    // --- SUB-SECTION: ITEM LIST (TABLE MON AN) ---
    private VBox createItemTableSection() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        Label lblTitle = new Label("DANH SÁCH MÓN");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        lblTitle.setStyle("-fx-text-fill: #082744;");

        tableMonAn = new TableView<>();
        styleTable(tableMonAn);

        TableColumn<String, String> colTen = new TableColumn<>("Tên món");
        colTen.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().split(",")[0]));

        TableColumn<String, Integer> colSL = new TableColumn<>("SL");
        colSL.setCellValueFactory(d -> new SimpleIntegerProperty(Integer.parseInt(d.getValue().split(",")[1])).asObject());
        colSL.setPrefWidth(40);

        TableColumn<String, String> colGia = new TableColumn<>("Đơn giá");
        colGia.setCellValueFactory(d -> new SimpleStringProperty(String.format("%,.0f", Double.parseDouble(d.getValue().split(",")[2]))));

        TableColumn<String, String> colThanhTien = new TableColumn<>("Thành tiền");
        colThanhTien.setCellValueFactory(d -> new SimpleStringProperty(String.format("%,.0f", Double.parseDouble(d.getValue().split(",")[3]))));
        colThanhTien.setStyle("-fx-font-weight: bold; -fx-alignment: CENTER-RIGHT;");

        tableMonAn.getColumns().addAll(colTen, colSL, colGia, colThanhTien);

        box.getChildren().addAll(lblTitle, tableMonAn);
        VBox.setVgrow(tableMonAn, Priority.ALWAYS);
        return box;
    }

    // =============================================================
    // 🛠️ UTILITIES & HELPERS
    // =============================================================

    private void styleInputField(TextField tf) {
        tf.setStyle("-fx-background-color: white; -fx-border-color: #ced6e0; -fx-border-radius: 5; -fx-padding: 8;");
    }

    private void styleDatePicker(DatePicker dp) {
        dp.setStyle("-fx-background-color: white; -fx-border-color: #ced6e0; -fx-border-radius: 5;");
    }

    private void styleComboBox(ComboBox<?> cbo) {
        cbo.setStyle("-fx-background-color: white; -fx-border-color: #ced6e0; -fx-border-radius: 5;");
        cbo.setMaxWidth(Double.MAX_VALUE);
    }

    private void styleButton(Button btn, String bg, String text) {
        btn.setStyle("-fx-background-color: " + bg + "; -fx-text-fill: " + text + "; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 8 15; -fx-cursor: hand;");
    }

    private void styleTable(TableView<?> table) {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-background-color: transparent; -fx-base: white; -fx-control-inner-background: white; -fx-table-cell-border-color: transparent; -fx-padding: 5;");
    }

    private TextField createReadOnlyField() {
        TextField tf = new TextField();
        tf.setEditable(false);
        tf.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #e9ecef; -fx-border-radius: 5; -fx-padding: 5;");
        return tf;
    }

    private void addInfoRow(GridPane grid, String label, TextField field, int colGroup, int row) {
        // colGroup 0 means columns 0,1. colGroup 1 means columns 2,3
        int startCol = colGroup * 2;
        Label lbl = new Label(label);
        lbl.setStyle("-fx-text-fill: #535c68; -fx-font-weight: bold;");
        grid.add(lbl, startCol, row);
        grid.add(field, startCol + 1, row);
    }

    private SVGPath createSvgIcon(double size, double viewBox, String color, String path) {
        SVGPath svg = new SVGPath();
        svg.setContent(path);
        svg.setScaleX(size / viewBox);
        svg.setScaleY(size / viewBox);
        svg.setStyle("-fx-fill: " + color + ";");
        return svg;
    }

    // =============================================================
    // ⚙️ LOGIC & ACTIONS
    // =============================================================

    private void selectInvoice(String data) {
        String[] parts = data.split(",", -1);
        DecimalFormat dcm = new DecimalFormat("#,##0.0 đ");

        double tong = Double.parseDouble(parts[3]);
        double giam = Double.parseDouble(parts[4]);
        double thue = control.tinhThue(tong);
        // Assuming index 9 contains table IDs separated by '_'
        String dsBanRaw = parts[9];
        double coc = control.tinhTienCocTheoDSBan(dsBanRaw);

        String[] dsBanArr = dsBanRaw.split("_");

        txtMaHoaDon.setText(parts[0]);
        txtKhachHang.setText(parts[1]);
        txtNhanVien.setText(parts[2]);
        txtBanTra.setText(String.join(", ", dsBanArr));
        txtPhuongThuc.setText(parts[5]);
        txtNgayTao.setText(parts[6]);
        txtTrangThai.setText(parts[7]);

        txtTongTien.setText(dcm.format(tong));
        txtGiamGia.setText(dcm.format(giam));
        txtThue.setText(dcm.format(thue));
        txtTienCoc.setText(dcm.format(coc));

        double finalTotal = control.tinhTienThanhToan(tong, giam, thue, coc);
        txtTienTT.setText(finalTotal >= 0 ? dcm.format(finalTotal) : "Thối lại: " + dcm.format(-finalTotal));

        // Load Items
        loadDanhSachMonAn(parts[0]);
    }

    private void loadDanhSachMonAn(String maHoaDon) {
        if (maHoaDon != null) {
            List<String> ds = control.dsThongTinMonAnTheoMaHD(maHoaDon);
            dsThongTinMonAn = FXCollections.observableArrayList(ds);
            tableMonAn.setItems(dsThongTinMonAn);
        }
    }

    private void locDanhSach(String maTim, LocalDate ngayChon, String trangThai) {
        if (flag) return;
        ObservableList<String> dsLoc = control.locHoaDon(dsHoaDon, maTim, ngayChon, trangThai);
        if (dsLoc == null || dsLoc.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Không tìm thấy hóa đơn phù hợp!");
        } else {
            tableHoaDon.setItems(dsLoc);
        }
    }

    private void taoMoiTimKiem() {
        flag = true;
        txtTimKiem.clear();
        ngayLoc.setValue(null);
        cboTrangThai.setValue("Tất cả");
        tableHoaDon.setItems(dsHoaDon);
        tableMonAn.getItems().clear();
        txtPhuongThuc.clear();
        txtTrangThai.clear();
        txtNgayTao.clear();
        txtBanTra.clear();
        // Clear details
        List.of(txtMaHoaDon, txtKhachHang, txtNhanVien, txtTongTien, txtGiamGia, txtThue, txtTienCoc, txtTienTT)
                .forEach(TextField::clear);
        flag = false;
    }

    public void thucHienIn() {
        String ma = txtMaHoaDon.getText();
        if (ma == null || ma.isBlank()) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng chọn hóa đơn để in!");
            return;
        }
        String invoiceData = tableHoaDon.getSelectionModel().getSelectedItem();
        List<String> items = new ArrayList<>(dsThongTinMonAn);
        xemTruocHoaDonIn((Stage) this.getScene().getWindow(), items, invoiceData);
    }

    public void xemTruocHoaDonIn(Stage owner, List<String> danhSach, String hoaDon) {
        // Tạo Stage xem trước
        Stage previewStage = new Stage();
        previewStage.initOwner(owner);
        previewStage.initModality(Modality.APPLICATION_MODAL);
        previewStage.setTitle("Xem trước hóa đơn");

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        // Tạo nội dung giống hệt trang in
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);

        VBox previewContent = new VBox(20);
        previewContent.setAlignment(Pos.TOP_CENTER);
        previewContent.setStyle("-fx-background-color: white; -fx-padding: 20;");

        // Header
        previewContent.getChildren().add(taoHeader(1, 1, hoaDon));

        // Bảng món ăn
        TableView<String> table = new TableView<String>();

        table.setItems(FXCollections.observableArrayList(danhSach));

        // Cột STT
        TableColumn<String, Void> colSTT = new TableColumn<>("STT");
        colSTT.setPrefWidth(30);
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

        colTenMon.setPrefWidth(200);

        // Cột số lượng
        TableColumn<String, Integer> colSoLuong = new TableColumn<>("SL");

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

        colSoLuong.setPrefWidth(40);

        // Cột giá
        TableColumn<String, Double> colGia = new TableColumn<>("Giá");
        colGia.setCellValueFactory(cellData -> {
            double giaTien = Double.parseDouble(cellData.getValue().split(",")[2]);
            return new SimpleDoubleProperty(giaTien).asObject();
        });
        colGia.setPrefWidth(120);
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
        colTong.setPrefWidth(120);
        colTong.setCellValueFactory(cellData -> {
            double tongTien = Double.parseDouble(cellData.getValue().split(",")[3]);
            return new SimpleDoubleProperty(tongTien).asObject();
        });
        colTong.setCellFactory(tc -> new TableCell<String, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("%,.0fđ", item));
                setAlignment(Pos.CENTER);
            }

        });

        table.getColumns().addAll(colSTT, colTenMon, colSoLuong, colGia, colTong);

        int rowCount = table.getItems().size();
        double rowHeight = 26;
        double headerHeight = 28;
        table.setPrefHeight(rowCount * rowHeight + headerHeight - 5);

        previewContent.getChildren().add(table);

        // Footer
        previewContent.getChildren().add(taoFooter(true, hoaDon));

        scroll.setContent(previewContent);

        // Nút Quay lại + In
        HBox buttons = new HBox(20);
        buttons.setAlignment(Pos.CENTER);

        Button btnBack = new Button("Quay lại");
        btnBack.setPrefWidth(120);
        btnBack.getStyleClass().add("btn-QuayLai");

        Button btnPrint = new Button("In hóa đơn");
        btnPrint.setPrefWidth(120);
        btnPrint.getStyleClass().add("btn-In");

        buttons.getChildren().addAll(btnBack, btnPrint);

        root.getChildren().addAll(scroll, buttons);

        Scene scene = new Scene(root, 600, 600);
        scene.getStylesheets().add(getClass().getResource("/css/qlhd.css").toExternalForm());
        previewStage.setScene(scene);

        // ----- SỰ KIỆN NÚT -----

        btnBack.setOnAction(e -> previewStage.close());

        btnPrint.setOnAction(e -> {
            previewStage.close();
            inHoaDon(owner, danhSach, hoaDon);
        });

        previewStage.show();
    }
    // --- Helper Print Methods (Simplified placeholder) ---
    // Copy your original taoHeader, taoTable, taoFooter, inHoaDon methods here.
    // Ensure taoTable returns a TableView styled for printing (usually minimalist).

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void setupShortcuts() {
        shortcuts.put(new KeyCodeCombination(KeyCode.F3), () -> txtTimKiem.requestFocus());
        shortcuts.put(new KeyCodeCombination(KeyCode.F8), () -> btnIn.fire());

        this.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) newScene.getAccelerators().putAll(shortcuts);
        });
    }

    // --- LEGACY PRINT METHODS (Copied & Adapted for Context) ---
    // Paste your original inHoaDon, taoHeader, taoFooter, taoTable here
    // to maintain printing functionality.

    private void inHoaDon(Stage owner, List<String> danhSach, String hoaDon) {
        Printer printer = Printer.getDefaultPrinter();
        PrinterJob job = PrinterJob.createPrinterJob(printer);
        if (job != null && job.showPrintDialog(owner)) {
            PageLayout layout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);

            // Logic in trang (giữ nguyên logic phân trang của bạn)
            // ... (Copy logic loop in page từ code cũ)

            // Demo simple print single page
            VBox pageBox = new VBox(10);
            pageBox.getChildren().addAll(taoHeader(1, 1, hoaDon), taoTable(danhSach, 1), taoFooter(true, hoaDon));
            job.printPage(layout, pageBox);
            job.endJob();
        }
    }

    private VBox taoHeader(int page, int total, String hoaDon) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);
        box.getChildren().add(new Label("2BT RESTAURANT - HÓA ĐƠN"));
        // Add details...
        return box;
    }

    private VBox taoFooter(boolean last, String hoaDon) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);
        box.getChildren().add(new Label("Cảm ơn quý khách!"));
        return box;
    }

    private TableView<String> taoTable(List<String> ds, int page) {
        // Return a TableView configured for printing (minimal styling)
        TableView<String> tv = new TableView<>(FXCollections.observableArrayList(ds));
        // Add columns...
        return tv;
    }
}
