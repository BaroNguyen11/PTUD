package gui;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import ctrl.ThanhToan_Ctrl;
import dao.KhachHang_DAO;
import dao.NhanVien_DAO;
import dao.QLHD_DAO;
import entity.HoaDon;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.NhanVien;
import entity.PhieuDatBan;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Gui_ThanhToan extends BorderPane {
    private ThanhToan_Ctrl control = new ThanhToan_Ctrl();
    private TextField txtTienNhan = new TextField();
    private TextField txtTienThua = new TextField();
    private TextField txtTongTien;
    private ToggleButton btnTienMat = new ToggleButton();
    private ToggleButton btnMa = new ToggleButton();

    // Numpad Buttons
    private Button btnPhim1, btnPhim2, btnPhim3, btnPhim4, btnPhim5, btnPhim6, btnPhim7, btnPhim8, btnPhim9, btnPhim0;
    private Button btnPhimC, btnPhim00, btnPhim000, btnPhimXoaMot, btnPhimEnter;

    // Quick Buttons
    private Button btnNhapNhanh1 = new Button();
    private Button btnNhapNhanh2 = new Button();
    private Button btnNhapNhanh3 = new Button();
    private Button btnNhapNhanh4 = new Button();
    private Button btnNhapNhanh5 = new Button();
    private Button btnNhapNhanh6 = new Button();

    private Button btnThanhToan = new Button();
    public Button btnQuayLai = new Button();
    private Button btnIn = new Button();

    private Label lblTongTien;
    private GridPane gridNhapNhanh;
    private double tienThanhToan;
    private NhanVien nv;
    private TextField txtGiamGia;
    private TextField txtThue;
    private TextField txtTamTinh;
    private Label lblTongTienQR;
    private ComboBox<KhuyenMai> cboKM;
    private TextField txtTienCoc;
    private String dsBan;

    // --- STYLE CONSTANTS ---
    private final String BACKGROUND_COLOR = "-fx-background-color: linear-gradient(to bottom, #f8f9fa 0%, #e9ecef 100%);";
    private final String CARD_STYLE = "-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);";
    private final String SUCCESS_BTN_STYLE = "-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-font-size: 16px;";
    private double tongTien = 0;

    public Gui_ThanhToan(NhanVien nv, String maHD) {
        this.nv = nv;

        // --- FIX 1: Maximize Size để fit chiều cao ---
        this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        this.setStyle(BACKGROUND_COLOR);
        this.setPadding(new Insets(20));

        // Layout chính
        HBox manHienThiBan = taoManHinhThanhToan(maHD);

        // Đặt vào center
        this.setCenter(manHienThiBan);
        setupTienNhanFormatter();
        // Load CSS (giữ nguyên nếu có)
        try {
            this.getStylesheets().add(getClass().getResource("/css/qlkm.css").toExternalForm());
            this.getStylesheets().add(getClass().getResource("/css/thanhtoan.css").toExternalForm());
        } catch (Exception e) {
        }
    }

    private void setupTienNhanFormatter() {
        txtTienNhan.textProperty().addListener((observable, oldValue, newValue) -> {
            // 1. Nếu ô trống thì thôi
            if (newValue == null || newValue.isEmpty()) {
                return;
            }

            // 2. Lọc sạch các ký tự không phải số (xóa dấu phẩy cũ, xóa chữ cái nếu lỡ nhập)
            String cleanString = newValue.replaceAll("[^\\d]", "");

            try {
                // 3. Nếu chuỗi rỗng sau khi lọc (vd người dùng xóa hết)
                if (cleanString.isEmpty()) {
                    txtTienNhan.setText("");
                    return;
                }

                // 4. Parse sang số long
                long value = Long.parseLong(cleanString);

                // 5. Format lại có dấu phân cách (Locale mặc định thường là dấu phẩy cho tiếng Anh, hoặc chấm cho tiếng Việt)
                // Ở đây mình dùng định dạng chuẩn quốc tế (dấu phẩy) để khớp với logic parse cũ của bạn
                DecimalFormat formatter = new DecimalFormat("#,###");
                String formattedString = formatter.format(value);

                // 6. Chỉ set lại Text nếu có sự thay đổi (để tránh vòng lặp vô tận)
                if (!newValue.equals(formattedString)) {
                    txtTienNhan.setText(formattedString);

                    // QUAN TRỌNG: Đưa con trỏ về cuối dòng để nhập tiếp không bị lỗi
                    txtTienNhan.positionCaret(formattedString.length());
                }

            } catch (NumberFormatException e) {
                // Nếu lỗi parse thì giữ nguyên giá trị cũ hợp lệ
                txtTienNhan.setText(oldValue);
            }
        });
    }

    private HBox taoManHinhThanhToan(String maHD) {
        // Sử dụng HBox để chia đôi màn hình thay vì BorderPane
        HBox rootAll = new HBox(20); // Khoảng cách giữa 2 panel là 20
        rootAll.setAlignment(Pos.TOP_CENTER);

        // --- FIX 2: Đảm bảo HBox chiếm hết không gian ---
        rootAll.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        List<PhieuDatBan> dsPhieu = null;
        try {
            dsPhieu = control.layDanhSachPhieuBangMaHD(maHD);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Tạo phần trái (Hóa đơn)
        VBox vboxPhanTrai = taoPhanTrai(dsPhieu);

        // Tạo phần phải (Thanh toán)
        VBox vboxPhanPhai = taoPhanPhai(dsPhieu);

        // --- FIX 3: Set HGrow để các phần tử co giãn ---
        HBox.setHgrow(vboxPhanTrai, Priority.ALWAYS); // Phần trái giãn hết mức có thể
        HBox.setHgrow(vboxPhanPhai, Priority.NEVER);  // Phần phải giữ kích thước cố định (hoặc ALWAYS nếu muốn chia đều)

        // Cố định kích thước phần phải cho giống máy POS (khoảng 450px)
        vboxPhanPhai.setPrefWidth(450);
        vboxPhanPhai.setMinWidth(450);
        vboxPhanPhai.setMaxWidth(450);

        rootAll.getChildren().addAll(vboxPhanTrai, vboxPhanPhai);

        return rootAll;
    }

    private VBox taoPhanTrai(List<PhieuDatBan> dsPhieu) {
        VBox vboxAll = new VBox(15);
        vboxAll.setPadding(new Insets(25));
        vboxAll.setStyle(CARD_STYLE);

        // --- FIX 4: Max Height cho Card ---
        vboxAll.setMaxHeight(Double.MAX_VALUE);

        // --- Header Hóa Đơn ---
        String maHD = "";
        if (dsPhieu == null || dsPhieu.isEmpty()) {
            showAlert(AlertType.ERROR, "Lỗi thanh toán", "Không tìm thấy danh sách phiếu");
        } else {
            maHD = dsPhieu.get(0).getHoaDon().getMaHoaDon();
        }

        dsBan = "";
        for (PhieuDatBan phieu : dsPhieu) {
            dsBan += phieu.getBan().getMaBan();
            if (!(phieu == dsPhieu.get(dsPhieu.size() - 1))) {
                dsBan += ", ";
            }
        }

        Label lblMaBan = new Label("Bàn: " + dsBan);
        lblMaBan.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        lblMaBan.setStyle("-fx-text-fill: #082744;");

        Label lblMaHD = new Label("Mã HĐ: " + maHD);
        lblMaHD.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        lblMaHD.setStyle("-fx-text-fill: #6c757d;");

        VBox headerBox = new VBox(0, lblMaBan, lblMaHD);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(0, 0, 10, 0));
        headerBox.setStyle("-fx-border-color: #dee2e6; -fx-border-width: 0 0 1 0;");

        // --- Danh sách món ---
        List<String> dsChiTietRaw = control.layDanhSachCTHD(maHD);
        ObservableList<String> dsChiTiet = FXCollections.observableArrayList(dsChiTietRaw);
        TableView<String> tableMon = new TableView<>(dsChiTiet);

        // --- FIX 5: TableView co giãn hết chiều cao ---
        tableMon.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableMon.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6;");
        VBox.setVgrow(tableMon, Priority.ALWAYS); // Table giãn chiếm phần trống

        setupTableColumns(tableMon);

        // --- Footer Hóa Đơn (Tính tiền) ---
        GridPane gridCalc = new GridPane();
        gridCalc.setHgap(10);
        gridCalc.setVgap(8);
        gridCalc.setPadding(new Insets(10, 0, 0, 0));

        Label lblTamTinh = createStyledLabel("Tạm tính:");
        txtTamTinh = createStyledReadOnlyTextField();
        Label lblThue = createStyledLabel("Thuế VAT:");
        txtThue = createStyledReadOnlyTextField();
        Label lblGiamGia = createStyledLabel("Giảm giá:");
        txtGiamGia = createStyledReadOnlyTextField();
        txtGiamGia.setStyle("-fx-text-fill: #28a745; -fx-font-weight: bold; -fx-background-color: transparent; -fx-alignment: center-right;");
        Label lblTienCoc = createStyledLabel("Tiền đã cọc:");
        txtTienCoc = createStyledReadOnlyTextField();

        lblTongTien = new Label("TỔNG TIỀN:");
        lblTongTien.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        lblTongTien.setStyle("-fx-text-fill: #d63031;");

        txtTongTien = new TextField();
        txtTongTien.setEditable(false);
        txtTongTien.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        txtTongTien.setStyle("-fx-background-color: transparent; -fx-text-fill: #d63031; -fx-alignment: center-right; -fx-padding: 0;");

        gridCalc.add(lblTamTinh, 0, 0);
        gridCalc.add(txtTamTinh, 1, 0);
        gridCalc.add(lblThue, 0, 1);
        gridCalc.add(txtThue, 1, 1);
        gridCalc.add(lblGiamGia, 0, 2);
        gridCalc.add(txtGiamGia, 1, 2);
        gridCalc.add(lblTienCoc, 0, 3);
        gridCalc.add(txtTienCoc, 1, 3);

        Region line = new Region();
        line.setStyle("-fx-background-color: #dee2e6; -fx-min-height: 1; -fx-max-height: 1;");
        GridPane.setHgrow(line, Priority.ALWAYS);
        GridPane.setColumnSpan(line, 2);
        gridCalc.add(line, 0, 4);

        gridCalc.add(lblTongTien, 0, 5);
        gridCalc.add(txtTongTien, 1, 5);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(40);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(60);
        col2.setHalignment(javafx.geometry.HPos.RIGHT);
        gridCalc.getColumnConstraints().addAll(col1, col2);

        // --- Khách hàng & KM ---
        VBox infoBox = new VBox(10);
        infoBox.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 10; -fx-background-radius: 8;");

        TextField txtTenKhacHang = createStyledReadOnlyTextField();
        TextField txtDiemTichLuy = createStyledReadOnlyTextField();
        KhachHang khachHang = KhachHang_DAO.getKhachHangById(dsPhieu.get(0).getKhachHang().getMaKhachHang());
        txtTenKhacHang.setText(khachHang.getTenKhachHang());
        txtDiemTichLuy.setText(khachHang.getDiemTichLuy() + " điểm");

        // Logic tính toán (giữ nguyên logic cũ)
        DecimalFormat format = new DecimalFormat("#,###.0 VND");
        tongTien = control.tinhTongTien(dsChiTiet);
        txtTamTinh.setText(format.format(tongTien));
        double tamTinh = parseVNDToDouble(txtTamTinh.getText());
        double thue = control.tinhThue(tongTien);
        txtThue.setText(format.format(thue));
        double tienCoc = control.tinhCocBangDanhSachPhieu(dsPhieu);
        txtTienCoc.setText(format.format(tienCoc));

        List<KhuyenMai> dsKMApDung = control.layDanhSachKhuyenMai(dsPhieu.get(0).getHoaDon().getMaHoaDon(), tamTinh);
        cboKM = new ComboBox<>(FXCollections.observableArrayList(dsKMApDung));
        cboKM.setPromptText("Chọn khuyến mãi...");
        cboKM.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(cboKM, Priority.ALWAYS);
        cboKM.setStyle("-fx-background-color: white; -fx-border-color: #ced4da; -fx-border-radius: 4;");

        if (dsKMApDung.isEmpty()) {
            tienThanhToan = control.tinhTienThanhToan(tongTien, tienCoc, false, 0.0, thue, 0.0);
            txtGiamGia.setText("0 VND");
        } else {
            KhuyenMai kmDau = dsKMApDung.get(0);
            cboKM.setValue(kmDau);
            tienThanhToan = control.tinhTienThanhToan(tongTien, tienCoc,
                    kmDau.getGiamGiaPhanTram(), kmDau.getGiaTriGiam(), thue, kmDau.getGiaTriToiDa());
            double giamGia = control.tinhTienGiamGia(kmDau.getGiaTriGiam(), kmDau.getGiamGiaPhanTram(),
                    parseVNDToDouble(txtTamTinh.getText()), kmDau.getGiaTriToiDa());
            txtGiamGia.setText(format.format(giamGia));
        }

        loadTienThanhToan(tienThanhToan, txtTongTien, lblTongTien, txtTienNhan, txtTienThua, tienCoc);

        cboKM.setOnAction(event -> {
            KhuyenMai selected = cboKM.getValue();
            if (selected != null) {
                double tienTT = control.tinhTienThanhToan(parseVNDToDouble(txtTamTinh.getText()), tienCoc,
                        selected.getGiamGiaPhanTram(), selected.getGiaTriGiam(), thue, selected.getGiaTriToiDa());
                double giaGiam = control.tinhTienGiamGia(selected.getGiaTriGiam(), selected.getGiamGiaPhanTram(),
                        tamTinh, selected.getGiaTriToiDa());
                txtGiamGia.setText(format.format(giaGiam));
                loadTienThanhToan(tienTT, txtTongTien, lblTongTien, txtTienNhan, txtTienThua, tienCoc);
                tienThanhToan = tienTT;
            } else {
                txtGiamGia.setText("0 VND");
            }
            if (txtTienNhan != null) txtTienNhan.setText("");
        });

        HBox rowKH = new HBox(10, new Label("Khách:"), txtTenKhacHang, new Label("Điểm:"), txtDiemTichLuy);
        rowKH.setAlignment(Pos.CENTER_LEFT);
        HBox rowKM = new HBox(10, new Label("Mã KM:"), cboKM);
        rowKM.setAlignment(Pos.CENTER_LEFT);

        infoBox.getChildren().addAll(rowKH, rowKM);

        vboxAll.getChildren().addAll(headerBox, tableMon, infoBox, gridCalc);
        return vboxAll;
    }

    private void setupTableColumns(TableView<String> tableMon) {
        TableColumn<String, Void> colSTT = new TableColumn<>("STT");
        colSTT.setPrefWidth(40);
        colSTT.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
                setAlignment(Pos.CENTER);
            }
        });

        TableColumn<String, String> colTenMon = new TableColumn<>("Món ăn");
        colTenMon.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().split(",")[0]));

        TableColumn<String, Integer> colSoLuong = new TableColumn<>("SL");
        colSoLuong.setCellValueFactory(cellData -> new SimpleIntegerProperty(Integer.parseInt(cellData.getValue().split(",")[1])).asObject());
        colSoLuong.setStyle("-fx-alignment: center;");
        colSoLuong.setPrefWidth(50);

        TableColumn<String, String> colTong = new TableColumn<>("Thành tiền");
        colTong.setCellValueFactory(cellData -> {
            double tongTien = Double.parseDouble(cellData.getValue().split(",")[3]);
            return new SimpleStringProperty(String.format("%,.0f", tongTien));
        });
        colTong.setStyle("-fx-alignment: center-right; -fx-font-weight: bold;");

        tableMon.getColumns().addAll(colSTT, colTenMon, colSoLuong, colTong);
    }

    private VBox taoPhanPhai(List<PhieuDatBan> dsPhieu) {
        VBox vboxAll = new VBox(15);
        vboxAll.setPadding(new Insets(25));
        vboxAll.setStyle(CARD_STYLE);

        // --- FIX 6: Max Height cho Card Phải ---
        vboxAll.setMaxHeight(Double.MAX_VALUE);

        // Header
        Label lblPhuongThuc = new Label("Thanh toán");
        lblPhuongThuc.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        lblPhuongThuc.setStyle("-fx-text-fill: #082744;");

        // Tabs
        HBox toggleGroup = new HBox(0);
        toggleGroup.setStyle("-fx-background-color: #e9ecef; -fx-background-radius: 8;");
        ToggleGroup group = new ToggleGroup();

        btnTienMat.setText("💵 Tiền mặt");
        btnTienMat.setToggleGroup(group);
        btnTienMat.setSelected(true);
        btnMa.setText("📱 Chuyển khoản");
        btnMa.setToggleGroup(group);

        btnTienMat.setPrefHeight(40);
        btnMa.setPrefHeight(40);
        btnTienMat.setMaxWidth(Double.MAX_VALUE);
        btnMa.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnTienMat, Priority.ALWAYS);
        HBox.setHgrow(btnMa, Priority.ALWAYS);

        String baseToggleStyle = "-fx-background-color: transparent; -fx-text-fill: #6c757d; -fx-font-weight: bold; -fx-font-size: 14px; -fx-cursor: hand;";
        String selectedToggleStyle = "-fx-background-color: #082744; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-weight: bold; -fx-font-size: 14px;";

        btnTienMat.setStyle(selectedToggleStyle);
        btnMa.setStyle(baseToggleStyle);
        group.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == btnTienMat) {
                btnTienMat.setStyle(selectedToggleStyle);
                btnMa.setStyle(baseToggleStyle);
            } else if (newVal == btnMa) {
                btnMa.setStyle(selectedToggleStyle);
                btnTienMat.setStyle(baseToggleStyle);
            } else if (oldVal != null) ((ToggleButton) oldVal).setSelected(true);
        });
        toggleGroup.getChildren().addAll(btnTienMat, btnMa);

        // --- Panel Tiền Mặt ---
        VBox paneTienMat = new VBox(10);
        VBox.setVgrow(paneTienMat, Priority.ALWAYS); // Giãn để đẩy nút thanh toán xuống

        Label lblTienNhan = new Label("Khách đưa:");
        lblTienNhan.setStyle("-fx-font-weight: bold; -fx-text-fill: #495057;");

        txtTienNhan.setPromptText("Nhập số tiền...");
        txtTienNhan.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        txtTienNhan.setPrefHeight(45);
        txtTienNhan.setStyle("-fx-background-color: white; -fx-border-color: #082744; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8;");

        // Gợi ý tiền
        gridNhapNhanh = new GridPane();
        gridNhapNhanh.setHgap(8);
        gridNhapNhanh.setVgap(8);
        setupQuickButtons();
        if (tienThanhToan > 0) {
            gridNhapNhanh.add(btnNhapNhanh1, 0, 0);
            gridNhapNhanh.add(btnNhapNhanh2, 1, 0);
            gridNhapNhanh.add(btnNhapNhanh3, 2, 0);
            gridNhapNhanh.add(btnNhapNhanh4, 0, 1);
            gridNhapNhanh.add(btnNhapNhanh5, 1, 1);
            gridNhapNhanh.add(btnNhapNhanh6, 2, 1);
        }

        Label lblTienThua = new Label("Tiền thừa:");
        lblTienThua.setStyle("-fx-font-weight: bold; -fx-text-fill: #495057;");
        txtTienThua.setEditable(false);
        txtTienThua.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        txtTienThua.setStyle("-fx-background-color: #e9ecef; -fx-border-radius: 8; -fx-background-radius: 8; -fx-text-fill: #28a745;");
        txtTienThua.setPrefHeight(40);

        // Numpad
        HBox numpadBox = createNumpad();
        VBox.setVgrow(numpadBox, Priority.ALWAYS); // Numpad giãn chiều cao

        paneTienMat.getChildren().addAll(lblTienNhan, txtTienNhan, gridNhapNhanh, lblTienThua, txtTienThua, numpadBox);

        // Logic tính tiền thừa
        txtTienNhan.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                txtTienThua.setText("");
                return;
            }
            try {
                String cleanInput = newValue.replaceAll("[,.]", "");
                double tienNhan = Double.parseDouble(cleanInput);
                double tongTien = this.tienThanhToan;
                double tienThua = tienNhan - tongTien;
                DecimalFormat fmt = new DecimalFormat("#,##0 VND");
                if (tienThua < 0) {
                    txtTienThua.setText("Thiếu: " + fmt.format(Math.abs(tienThua)));
                    txtTienThua.setStyle("-fx-background-color: #fff5f5; -fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                } else {
                    txtTienThua.setText("Dư: " + fmt.format(tienThua));
                    txtTienThua.setStyle("-fx-background-color: #e6fffa; -fx-text-fill: #28a745; -fx-font-weight: bold;");
                }
            } catch (Exception e) {
            }
        });

        // --- Panel QR ---
        VBox paneMa = new VBox(20);
        paneMa.setAlignment(Pos.CENTER);
        VBox.setVgrow(paneMa, Priority.ALWAYS);
        ImageView maQR = new ImageView(new Image(getClass().getResourceAsStream("/img/qr.png")));
        maQR.setFitWidth(220);
        maQR.setFitHeight(220);
        lblTongTienQR = new Label("Quét mã để thanh toán");
        lblTongTienQR.setStyle("-fx-font-size: 16px; -fx-text-fill: #6c757d;");
        paneMa.getChildren().addAll(maQR, lblTongTienQR);
        paneMa.setVisible(false);
        paneMa.setManaged(false);

        group.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == btnTienMat) {
                paneTienMat.setVisible(true);
                paneTienMat.setManaged(true);
                paneMa.setVisible(false);
                paneMa.setManaged(false);
            } else {
                paneTienMat.setVisible(false);
                paneTienMat.setManaged(false);
                paneMa.setVisible(true);
                paneMa.setManaged(true);
            }
        });

        // Footer Actions
        HBox actionBox = new HBox(10);
        actionBox.setAlignment(Pos.CENTER);

        btnIn.setText("In HĐ");
        btnIn.setPrefHeight(45);
        btnIn.setPrefWidth(90);
        btnIn.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");

        btnQuayLai.setText("Quay lại");
        btnQuayLai.setPrefHeight(45);
        btnQuayLai.setPrefWidth(90);
        btnQuayLai.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");

        btnThanhToan.setText("THANH TOÁN (F12)");
        btnThanhToan.setPrefHeight(45);
        btnThanhToan.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnThanhToan, Priority.ALWAYS);
        btnThanhToan.setStyle(SUCCESS_BTN_STYLE);

        btnIn.setOnAction(e -> thucHienIn(dsPhieu.get(0).getHoaDon().getMaHoaDon(), dsBan));
//        btnQuayLai.setOnAction(e -> this.setCenter(new Gui_DanhSachBan(new Gui_TrangChu(nv))));
        btnQuayLai.setText("Quay lại");
        btnThanhToan.setOnAction(e -> {
            if (checkTienNhan()) {
                String tong = txtTongTien.getText();
                String pt = btnTienMat.isSelected() ? "Tiền mặt" : "Chuyển khoản";
                KhuyenMai km = cboKM.getValue();
                String maKH = dsPhieu.get(0).getKhachHang().getMaKhachHang();
                String maHD = dsPhieu.get(0).getHoaDon().getMaHoaDon();
                if (thanhToan(tong, pt, maHD, dsPhieu, km, maKH)) btnQuayLai.fire();
            }
        });

        actionBox.getChildren().addAll(btnQuayLai, btnIn, btnThanhToan);

        vboxAll.getChildren().addAll(lblPhuongThuc, toggleGroup, paneTienMat, paneMa, actionBox);
        return vboxAll;
    }

    private HBox createNumpad() {
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setAlignment(Pos.CENTER);

        // Init buttons
        btnPhim1 = createNumBtn("1");
        btnPhim2 = createNumBtn("2");
        btnPhim3 = createNumBtn("3");
        btnPhim4 = createNumBtn("4");
        btnPhim5 = createNumBtn("5");
        btnPhim6 = createNumBtn("6");
        btnPhim7 = createNumBtn("7");
        btnPhim8 = createNumBtn("8");
        btnPhim9 = createNumBtn("9");
        btnPhim0 = createNumBtn("0");
        btnPhim00 = createNumBtn("00");
        btnPhim000 = createNumBtn("000");
        btnPhimC = createActionBtn("C", "#e74c3c");
        btnPhimXoaMot = createActionBtn("⌫", "#f39c12");
        btnPhimEnter = createActionBtn("↵", "#2ecc71");

        grid.add(btnPhim7, 0, 0);
        grid.add(btnPhim8, 1, 0);
        grid.add(btnPhim9, 2, 0);
        grid.add(btnPhimXoaMot, 3, 0);
        grid.add(btnPhim4, 0, 1);
        grid.add(btnPhim5, 1, 1);
        grid.add(btnPhim6, 2, 1);
        grid.add(btnPhimC, 3, 1);
        grid.add(btnPhim1, 0, 2);
        grid.add(btnPhim2, 1, 2);
        grid.add(btnPhim3, 2, 2);
        grid.add(btnPhimEnter, 3, 2, 1, 2);
        grid.add(btnPhim0, 0, 3);
        grid.add(btnPhim00, 1, 3);
        grid.add(btnPhim000, 2, 3);

        btnPhimEnter.setMaxHeight(Double.MAX_VALUE); // Fill chiều cao

        thucHienNumpad(btnPhim1, btnPhim2, btnPhim3, btnPhim4, btnPhim5, btnPhim6,
                btnPhim7, btnPhim8, btnPhim9, btnPhim0, btnPhimC, btnPhimXoaMot, btnPhimEnter,
                null, null, null, null, btnPhim00, btnPhim000, txtTienNhan);

        HBox wrapper = new HBox(grid);
        wrapper.setAlignment(Pos.CENTER);
        return wrapper;
    }

    private Button createNumBtn(String text) {
        Button btn = new Button(text);
        btn.setPrefSize(55, 45); // Kích thước nút
        String defaultStyle = "-fx-background-color: #e9ecef; " +
                "-fx-text-fill: #2c3e50; " +
                "-fx-border-color: #adb5bd; " +
                "-fx-border-radius: 5; " +
                "-fx-background-radius: 5; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 16px; " +
                "-fx-cursor: hand;";

        String hoverStyle = "-fx-background-color: #ced4da; " + // Khi di chuột vào thì đậm hơn
                "-fx-text-fill: #000000; " +
                "-fx-border-color: #6c757d; " +
                "-fx-border-radius: 5; " +
                "-fx-background-radius: 5; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 16px; " +
                "-fx-cursor: hand;";
        btn.setStyle(defaultStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(defaultStyle));
        btn.setOnMousePressed(e -> btn.setStyle("-fx-background-color: #aeb5bc; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 5;"));
        btn.setOnMouseReleased(e -> btn.setStyle(hoverStyle));
        return btn;
    }

    private Button createActionBtn(String text, String colorHex) {
        Button btn = new Button(text);
        btn.setPrefSize(55, 45);
        btn.setStyle("-fx-background-color: " + colorHex + "; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-font-size: 14px; -fx-cursor: hand;");
        return btn;
    }

    private void setupQuickButtons() {
        String quickBtnStyle = "-fx-background-color: #e2e6ea; -fx-text-fill: #2c3e50; -fx-background-radius: 5; -fx-cursor: hand; -fx-font-size: 12px;";
        for (Button btn : new Button[]{btnNhapNhanh1, btnNhapNhanh2, btnNhapNhanh3, btnNhapNhanh4, btnNhapNhanh5, btnNhapNhanh6}) {
            btn.setStyle(quickBtnStyle);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setPrefHeight(35);
            GridPane.setHgrow(btn, Priority.ALWAYS);
            btn.setOnAction(e -> {
                try {
                    String val = btn.getText().replaceAll("\\D", "");
                    txtTienNhan.setText(val);
                } catch (Exception ex) {
                }
            });
        }
    }

    private Label createStyledLabel(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        lbl.setStyle("-fx-text-fill: #495057;");
        return lbl;
    }

    private TextField createStyledReadOnlyTextField() {
        TextField txt = new TextField();
        txt.setEditable(false);
        txt.setStyle("-fx-background-color: transparent; -fx-text-fill: #082744; -fx-font-weight: bold; -fx-alignment: center-right; -fx-padding: 0;");
        return txt;
    }

    // --- LOGIC METHODS (Giữ nguyên) ---
    public void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static double parseVNDToDouble(String vndString) {
        if (vndString == null || vndString.trim().isEmpty()) return 0.0;
        try {
            String cleanString = vndString.replace("VND", "").trim();
            cleanString = cleanString.replace(",", "");
            return Double.parseDouble(cleanString);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public void loadTienThanhToan(double tienThanhToan, TextField txtTongTien, Label lblTongTien, TextField txtTienNhan, TextField txtTienThua, double tienCoc) {
        DecimalFormat format2 = new DecimalFormat("#,##0 VND");
        if (tienThanhToan > 0) {
            lblTongTien.setText("TỔNG TIỀN:");
            txtTongTien.setText(format2.format(tienThanhToan));
        } else {
            double tienHoan = Math.abs(tienThanhToan);
            lblTongTien.setText("TIỀN HOÀN LẠI:");
            txtTongTien.setText(format2.format(tienHoan));
            txtTienNhan.setText("0");
            txtTienThua.setText(format2.format(tienHoan));
        }
        loadDuLieuNhapNhanh(btnNhapNhanh1, btnNhapNhanh2, btnNhapNhanh3, btnNhapNhanh4, btnNhapNhanh5, btnNhapNhanh6);
    }

    public void loadDuLieuNhapNhanh(Button btn1, Button btn2, Button btn3, Button btn4, Button btn5, Button btn6) {
        int tongTien = (int) Math.round(parseVNDToDouble(txtTongTien.getText()));
        List<Integer> dsNhapNhanh = control.suggestCash(tongTien);
        DecimalFormat formatNhapNhanh = new DecimalFormat("#,##0");
        btn1.setText(formatNhapNhanh.format(dsNhapNhanh.get(0)));
        btn2.setText(formatNhapNhanh.format(dsNhapNhanh.get(1)));
        btn3.setText(formatNhapNhanh.format(dsNhapNhanh.get(2)));
        btn4.setText(formatNhapNhanh.format(dsNhapNhanh.get(3)));
        btn5.setText(formatNhapNhanh.format(dsNhapNhanh.get(4)));
        btn6.setText(formatNhapNhanh.format(dsNhapNhanh.get(5)));
    }

    public void thucHienNumpad(Button btn1, Button btn2, Button btn3, Button btn4, Button btn5, Button btn6, Button btn7, Button btn8, Button btn9, Button btn0, Button btnC, Button btnBackspace, Button btnEnter, Button btn50, Button btn100, Button btn200, Button btn500, Button btn00, Button btn000, TextField txtTienNhan) {
        if (btn1 != null) btn1.setOnAction(e -> txtTienNhan.appendText("1"));
        if (btn2 != null) btn2.setOnAction(e -> txtTienNhan.appendText("2"));
        if (btn3 != null) btn3.setOnAction(e -> txtTienNhan.appendText("3"));
        if (btn4 != null) btn4.setOnAction(e -> txtTienNhan.appendText("4"));
        if (btn5 != null) btn5.setOnAction(e -> txtTienNhan.appendText("5"));
        if (btn6 != null) btn6.setOnAction(e -> txtTienNhan.appendText("6"));
        if (btn7 != null) btn7.setOnAction(e -> txtTienNhan.appendText("7"));
        if (btn8 != null) btn8.setOnAction(e -> txtTienNhan.appendText("8"));
        if (btn9 != null) btn9.setOnAction(e -> txtTienNhan.appendText("9"));
        if (btn0 != null) btn0.setOnAction(e -> txtTienNhan.appendText("0"));
        if (btn00 != null) btn00.setOnAction(e -> txtTienNhan.appendText("00"));
        if (btn000 != null) btn000.setOnAction(e -> txtTienNhan.appendText("000"));
        if (btnEnter != null) btnEnter.setOnAction(e -> btnThanhToan.fire());
        if (btnC != null) btnC.setOnAction(e -> txtTienNhan.setText(""));
        if (btnBackspace != null) btnBackspace.setOnAction(e -> {
            String currentText = txtTienNhan.getText();
            if (currentText.length() > 0) txtTienNhan.setText(currentText.substring(0, currentText.length() - 1));
        });
    }

    private boolean thanhToan(String tongTien, String phuongThuc, String maHoaDon, List<PhieuDatBan> dsPhieu, KhuyenMai khuyenMai, String maKH) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận thanh toán");
        alert.setHeaderText("Bạn có chắc thanh toán?");
        alert.setContentText("Tổng tiền: " + tongTien + "\nPhương thức: " + phuongThuc);
        double giamGia = parseVNDToDouble(txtGiamGia.getText());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                double diemMoi = parseVNDToDouble(tongTien) / 20000.0;
                // Thay thế đoạn if cũ bằng đoạn này:
                boolean thanhToanXong = control.xuLiThanhToanTatCa(dsPhieu, maHoaDon, phuongThuc, giamGia, khuyenMai);

                if (thanhToanXong) {
                    // Thanh toán xong thì mới cộng điểm
                    boolean congDiemXong = control.capNhatTichLuy(maKH, diemMoi);

                    // Dù cộng điểm có lỗi hay không, thì tiền đã trừ -> Báo thành công
                    showAlert(AlertType.INFORMATION, "Thanh Toán Thành Công", "Tổng tiền thanh toán: " + tongTien);
                    return true;
                } else {
                    // Chỉ khi lưu hóa đơn thất bại mới báo lỗi
                    showAlert(AlertType.ERROR, "Thất bại", "Lỗi lưu dữ liệu hóa đơn!");
                    return false;
                }
            } catch (Exception e) {
                showAlert(AlertType.ERROR, "Thất bại", "Thanh Toán Không Thành Công 2!");
                return false;
            }
        }
        return false;
    }

    //    public boolean checkTienNhan() {
//        double giamGia = parseVNDToDouble(txtGiamGia.getText());
//        double thue = parseVNDToDouble(txtThue.getText());
//        double coc = parseVNDToDouble(txtTienCoc.getText());
//        double tongTien = parseVNDToDouble(txtTamTinh.getText());
//        double thanhToan = (tongTien - giamGia + thue - coc);
//        if (thanhToan <= 0) return true;
//        if (btnMa.isSelected()) return true;
//        String chuoiTienNhan = txtTienNhan.getText().trim();
//        if (chuoiTienNhan.isBlank()) {
//            showAlert(AlertType.ERROR, "Lỗi", "Vui lòng nhập tiền nhận");
//            txtTienNhan.requestFocus();
//            return false;
//        }
//        try {
//            double tienNhan = Double.parseDouble(chuoiTienNhan.replace(".", ""));
//            double tongTienDB = parseVNDToDouble(txtTongTien.getText());
//            if (tienNhan - tongTienDB < 0) {
//                showAlert(AlertType.ERROR, "Lỗi", "Tiền nhận phải lớn hơn hoặc bằng tổng tiền");
//                return false;
//            }
//        } catch (Exception er) {
//            showAlert(AlertType.ERROR, "Lỗi", "Tiền nhận không hợp lệ");
//            return false;
//        }
//        return true;
//    }
    public boolean checkTienNhan() {
        double giamGia = parseVNDToDouble(txtGiamGia.getText());
        double thue = parseVNDToDouble(txtThue.getText());
        double coc = parseVNDToDouble(txtTienCoc.getText());
        double tongTien = parseVNDToDouble(txtTamTinh.getText());

        // Tính số tiền thực tế cần thanh toán
        double thanhToan = (tongTien - giamGia + thue - coc);

        // Nếu số tiền <= 0 (đã cọc đủ hoặc free) hoặc chọn chuyển khoản -> Cho qua
        if (thanhToan <= 0) return true;
        if (btnMa.isSelected()) return true;

        String chuoiTienNhan = txtTienNhan.getText().trim();
        if (chuoiTienNhan.isBlank()) {
            showAlert(AlertType.ERROR, "Lỗi", "Vui lòng nhập tiền nhận");
            txtTienNhan.requestFocus();
            return false;
        }

        try {
            // --- SỬA LỖI TẠI ĐÂY ---
            // Dùng Regex "[^\\d]" để thay thế TẤT CẢ ký tự không phải số bằng rỗng.
            // Ví dụ: "1,166,000" -> "1166000"
            // Ví dụ: "1.166.000" -> "1166000"
            String cleanInput = chuoiTienNhan.replaceAll("[^\\d]", "");

            double tienNhan = Double.parseDouble(cleanInput);
            double tongTienDB = parseVNDToDouble(txtTongTien.getText());

            // Kiểm tra logic tiền
            // Lưu ý: Dùng sai số nhỏ (epsilon) khi so sánh double để tránh lỗi làm tròn máy tính
            if (tienNhan < tongTienDB - 1.0) { // Cho phép sai số 1 đồng
                showAlert(AlertType.ERROR, "Lỗi", "Tiền nhận phải lớn hơn hoặc bằng tổng tiền (" +
                        new DecimalFormat("#,###").format(tongTienDB) + ")");
                return false;
            }
        } catch (Exception er) {
            // In lỗi ra console để bạn dễ kiểm tra nếu có vấn đề khác
            er.printStackTrace();
            showAlert(AlertType.ERROR, "Lỗi", "Tiền nhận không hợp lệ (Lỗi định dạng)");
            return false;
        }
        return true;
    }

    public void thucHienIn(String maHoaDon, String dsBan) {
        QLHD_DAO daoQLHD = new QLHD_DAO();
        if (maHoaDon == null || maHoaDon.isBlank()) {
            showAlert(AlertType.ERROR, "Lỗi", "Vui lòng chọn một hóa đơn trước khi in");
            return;
        }
        List<String> dsMon = daoQLHD.getChiTietHoaDonTheoMa(maHoaDon);
        HoaDon hoaDon = daoQLHD.getHoaDonById(maHoaDon);
        xemTruocHoaDonIn((Stage) this.getScene().getWindow(), dsMon, hoaDon, dsBan);
    }

    public void xemTruocHoaDonIn(Stage owner, List<String> danhSach, HoaDon hoaDon, String dsBan) {

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
        previewContent.getChildren().add(taoHeader(1, 1, hoaDon, dsBan));

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
            inHoaDon(owner, danhSach, hoaDon, dsBan);
        });

        previewStage.show();
    }

    private VBox taoHeader(int page, int totalPages, HoaDon hoaDon, String dsBan) {
        VBox box = new VBox(10);
        box.setMaxWidth(480);

        Label lblTenNhaHang = new Label("2BT RESTAURANT");
        lblTenNhaHang.setStyle("""
                -fx-font-size: 20px;
                -fx-font-weight: bold;
                """);
        Label lblDiaChi = new Label("100 Lê Đức Thọ, P.16, Gò Vấp, TP Hồ Chí Minh");
        Label lblSdt = new Label("0987 654 321");

        Label lblHoaDon = new Label("HÓA ĐƠN THANH TOÁN");
        lblHoaDon.setStyle("""
                -fx-font-size: 20px;
                -fx-font-weight: bold;
                """);

        HBox hbox1 = new HBox();
        Region spacer1 = new Region();
        hbox1.setMaxWidth(460);
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Label lblNgay = new Label("Ngày: " + dtf.format(hoaDon.getNgayTao()));
        Label lblMaHoaDon = new Label("Mã hóa đơn: " + hoaDon.getMaHoaDon());

        hbox1.getChildren().addAll(lblNgay, spacer1, lblMaHoaDon);

        Label lblMaBan = new Label("Bàn: " + dsBan);
        HBox hbox2 = new HBox(lblMaBan);
        hbox2.setAlignment(Pos.CENTER_LEFT);

        NhanVien_DAO nvDao = new NhanVien_DAO();
        NhanVien nv = nvDao.getNhanVienByMa(hoaDon.getNhanVien().getMaNhanVien());

        Label lblThuNgan = new Label("Nhân viên: " + nv.getTenNhanVien());
        HBox hbox3 = new HBox(lblThuNgan);
        hbox3.setAlignment(Pos.CENTER_LEFT);

        box.setAlignment(Pos.CENTER);

        box.setMargin(hbox1, new Insets(0, 15, 0, 0));
        box.getChildren().addAll(lblTenNhaHang, lblDiaChi, lblSdt, lblHoaDon, hbox1, hbox2, hbox3, new Separator());
        return box;
    }

    public void inHoaDon(Stage owner, List<String> danhSach, HoaDon hoaDon, String dsBan) {
        Printer printer = Printer.getDefaultPrinter();
        PrinterJob job = PrinterJob.createPrinterJob(printer);
        if (job == null || !job.showPrintDialog(owner)) {
            System.out.println("Hủy in.");
            return;
        }

        PageLayout layout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
        double maxHeight = layout.getPrintableHeight();

        int currentIndex = 0;
        int sttGlobal = 1;
        int pageNum = 1;
        boolean hasMore = true;
        while (hasMore) {
            VBox pageBox = new VBox(10);
            pageBox.setStyle("-fx-padding: 20; -fx-font-family: Arial; -fx-background-color: white;"); // Nền trắng cho
            // page in
            pageBox.setAlignment(Pos.TOP_CENTER); // Căn giữa theo chiều dọc trên cùng

            // Header chỉ trang đầu
            if (pageNum == 1) {
                pageBox.getChildren().add(taoHeader(1, 1, hoaDon, dsBan)); // totalPages không biết trước, có thể để 1 hoặc tính trước
            }

            // Tính items cho trang này
            int itemsPerPage = (pageNum == 1) ? 19 : 25;
            int remaining = danhSach.size() - currentIndex;
            int itemsThisPage = Math.min(itemsPerPage, remaining);


            if (itemsThisPage > 0 && remaining >= 0) {
                int from = currentIndex;
                int to = currentIndex + itemsThisPage;
                List<String> subList = danhSach.subList(from, to);

                TableView<String> table = taoTable(subList, sttGlobal);
                pageBox.getChildren().add(table);

                // Cập nhật cho trang sau
                currentIndex += itemsThisPage;
                sttGlobal += itemsThisPage;
            }

            hasMore = remaining > itemsThisPage;

            if ((pageNum == 1 && (itemsThisPage > 10 && itemsThisPage <= 19)) || (pageNum > 1 && (itemsThisPage > 16 && itemsThisPage <= 25)))
                hasMore = true;

            pageBox.getChildren().add(taoFooter(!hasMore, hoaDon));

            pageBox.setAlignment(Pos.CENTER);
            pageBox.applyCss();
            pageBox.layout();

//			// Scale nếu page cao quá (hiếm vì itemsPerPage fit)
//			double pageHeight = pageBox.getBoundsInLocal().getHeight();
//			if (pageHeight > maxHeight) {
//				double scale = maxHeight / pageHeight;
//				pageBox.setScaleX(scale);
//				pageBox.setScaleY(scale);
//			}


            job.printPage(layout, pageBox);
            pageNum++;
        }

        if (job.endJob()) {
            showAlert(AlertType.INFORMATION, "In Hóa Đơn", "In Hóa Đơn Thành Công !");
        }
    }

    private TableView<String> taoTable(List<String> ds, int page) {
        TableView<String> table = new TableView<>();
        table.setItems(FXCollections.observableArrayList(ds));

        // Cột STT
        TableColumn<String, Void> colSTT = new TableColumn<>("STT");
        colSTT.setPrefWidth(30);
        colSTT.setSortable(false);
        colSTT.setCellFactory(col -> new TableCell<String, Void>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + page));
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

        colTenMon.setPrefWidth(145);

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

        table.getStylesheets().add(getClass().getResource("/css/tableprint.css").toExternalForm());

        table.getColumns().addAll(colSTT, colTenMon, colSoLuong, colGia, colTong);
        table.setPrefHeight(ds.size() * 25 + 20);
        return table;
    }

    private VBox taoFooter(boolean lastPage, HoaDon hoaDon) {
        VBox box = new VBox(5);
        box.setMaxWidth(480);
        DecimalFormat dcm = new DecimalFormat("#,##0.0 VND");

        String[] hoaDonSplit = QLHD_DAO.layHoaDonString(hoaDon.getMaHoaDon()).split(",");

        String tongTienString = hoaDonSplit[3];
        //String giamGiaString = hoaDonSplit[4];
        String loaiBan = hoaDonSplit[9];
        String ghiChu = hoaDonSplit[8];

        KhuyenMai km = cboKM.getValue();


        double tongTien = Double.parseDouble(tongTienString);
        double giamGia = control.tinhTienGiamGia(km.getGiaTriGiam(), km.getGiamGiaPhanTram(), tongTien, km.getGiaTriToiDa());
        double thue = control.tinhThue(tongTien);

        if (lastPage) {
            HBox hbox1 = new HBox();
            Region spacer1 = new Region();
            hbox1.setMaxWidth(460);
            HBox.setHgrow(spacer1, Priority.ALWAYS);

            Label lblTamTinh = new Label("Tạm tính: ");
            Label lblTamTinhText = new Label(dcm.format(tongTien));

            hbox1.getChildren().addAll(lblTamTinh, spacer1, lblTamTinhText);

            HBox hbox2 = new HBox();
            Region spacer2 = new Region();
            hbox2.setMaxWidth(460);
            HBox.setHgrow(spacer2, Priority.ALWAYS);

            Label lblThue = new Label("Thuế VAT: ");
            Label lblThueText = new Label(dcm.format(thue));

            hbox2.getChildren().addAll(lblThue, spacer2, lblThueText);

            HBox hbox3 = new HBox();
            Region spacer3 = new Region();
            hbox3.setMaxWidth(460);
            HBox.setHgrow(spacer3, Priority.ALWAYS);

            Label lblGiamGia = new Label("Giảm giá: ");
            Label lblGiamGiaText = new Label(dcm.format(giamGia));

            hbox3.getChildren().addAll(lblGiamGia, spacer3, lblGiamGiaText);

            Separator line = new Separator();
            line.setPrefWidth(480);
            line.setStyle("-fx-background-color: black");

            HBox hbox4 = new HBox();
            Region spacer4 = new Region();
            hbox4.setMaxWidth(460);
            HBox.setHgrow(spacer4, Priority.ALWAYS);

            Label lblTongTien = new Label("Tổng tiền: ");
            Label lblTongTienText = new Label(dcm.format(tongTien + thue - giamGia));

            lblTongTien.setStyle("""
                    -fx-font-size: 20px;
                    -fx-font-weight: bold;
                    """);
            lblTongTienText.setStyle("""
                    -fx-font-size: 20px;
                    -fx-font-weight: bold;
                    """);

            hbox4.getChildren().addAll(lblTongTien, spacer4, lblTongTienText);

            HBox hbox5 = new HBox();
            hbox5.setMaxWidth(460);

            Separator line2 = new Separator();
            line2.setPrefWidth(480);
            line2.setStyle("-fx-background-color: black");

            Label lblCamOn = new Label("Cảm Ơn Quý Khách - Hẹn Gặp Lại ");
            lblCamOn.setStyle("""
                    -fx-font-style: italic;
                    -fx-font-size: 20px;
                    """);

            hbox5.getChildren().addAll(lblCamOn);
            hbox5.setAlignment(Pos.CENTER);

            box.getChildren().addAll(hbox1, hbox2, hbox3, line, hbox4, line2, hbox5);

        }
        box.setStyle("-fx-padding: 10 0 0 0;");
        return box;
    }
}