package gui;
import control.QLHD_Ctrl;
import javafx.print.PageLayout;
import javafx.print.Paper;
import javafx.print.PageOrientation;
import javafx.print.Printer;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.awt.print.*;
import javafx.beans.property.SimpleStringProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import entity.LoaiBan;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
public class Gui_QuanLiHoaDon extends BorderPane {
    private control.QLHD_Ctrl control;
    // Dữ liệu test cho món ăn
    private ObservableList<String> dsThongTinMonAn = FXCollections.observableArrayList();
    // Dữ liệu test cho hóa đơn
    private ObservableList<String> dsHoaDon = FXCollections.observableArrayList();
    private TextField txtMaHoaDon;
    private TextField txtKhachHang;
    private TextField txtNhanVien;
    private TextField txtTongTien;
    private TextField txtPhuongThuc;
    private TextField txtTienCoc;
    private TextField txtNgayTao;
    private TextField txtBanTra;
    private TextField txtThue;
    private TextField txtTrangThai;
    private TextField txtTienTT;
    private TextField txtGiamGia;
    private TableView tableMonAn;
    private TableView<String> tableHoaDon;
    private TextField txtTimKiem;
    private ComboBox cboTrangThai;
    private DatePicker ngayLoc;
    private Button btnReset;
    private boolean flag = false;
    private final Map<KeyCombination, Runnable> shortcuts = new HashMap<>();
    private Button btnIn;

    public Gui_QuanLiHoaDon() {
        control = new QLHD_Ctrl();

        // Phần thông tin và tìm kiếm
        VBox bangThongTinTimKiem = taoPhanThongTimKiem();
        this.setCenter(bangThongTinTimKiem);
        BorderPane.setAlignment(bangThongTinTimKiem, Pos.TOP_LEFT);
        BorderPane.setMargin(bangThongTinTimKiem, new Insets(20, 40, 10, 0));

        // Bảng danh sách món thanh toán
        VBox bangMonAn = taoBangMonAn();
        this.setRight(bangMonAn);
        BorderPane.setMargin(bangMonAn, new Insets(10, 20, 10, 10));

        //Bảng hóa đơn
        this.setBottom(taoBangHoaDon());

        /////Hàng f
        KeyCombination f3 = new KeyCodeCombination(KeyCode.F3);
        KeyCombination f8 = new KeyCodeCombination(KeyCode.F8);


        //
        shortcuts.put(f3, () -> txtTimKiem.requestFocus());
        shortcuts.put(f8, () -> btnIn.fire());

        this.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getAccelerators().putAll(shortcuts);
            }

            if(oldScene != null) {
                oldScene.getAccelerators().clear();
            }
        });


        this.getStylesheets().add(getClass().getResource("/css/qlhd.css").toExternalForm());
        this.setStyle("-fx-background-color: white");

    }
    /**
     * Hàm cài đặt tính năng gợi ý tự động cho ô tìm kiếm
     */
    private void caiDatGoiYTimKiem(TextField txtInput, ObservableList<String> dataNguon) {
        // 1. Tạo Menu ngữ cảnh
        ContextMenu suggestionsPopup = new ContextMenu();
        suggestionsPopup.getStyleClass().add("goi-y-menu");

        // Đặt chiều rộng popup bằng chiều rộng TextField
        suggestionsPopup.setPrefWidth(txtInput.getPrefWidth());

        // 2. Logic chung: Lọc và hiển thị
        Runnable hienThiGoiY = () -> {
            String tuKhoa = txtInput.getText().toLowerCase();
            List<MenuItem> suggestions = new ArrayList<>();

            // Nếu ô trống -> Có thể hiển thị toàn bộ hoặc 5-10 cái đầu tiên (tùy chọn)
            // Nếu có chữ -> Lọc
            for (String row : dataNguon) {
                String[] parts = row.split(",");
                if (parts.length < 2) continue;

                String maHD = parts[0];
                String tenKH = parts[1];

                // Logic lọc: Nếu từ khóa rỗng (khi click) HOẶC chứa từ khóa
                if (tuKhoa.isEmpty() || maHD.toLowerCase().contains(tuKhoa) || tenKH.toLowerCase().contains(tuKhoa)) {
                    String hienThi = maHD + " - " + tenKH;
                    MenuItem item = new MenuItem(hienThi);
                    item.getStyleClass().add("goi-y-item");

                    item.setOnAction(e -> {
                        txtInput.setText(maHD);
                        txtInput.positionCaret(maHD.length());
                        suggestionsPopup.hide();
                        locDanhSach(maHD, ngayLoc.getValue(), (String) cboTrangThai.getValue());
                    });
                    suggestions.add(item);
                }
                if (suggestions.size() >= 10) break; // Giới hạn 10 dòng
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

        // 3. Sự kiện: Khi gõ chữ
        txtInput.textProperty().addListener((observable, oldValue, newValue) -> hienThiGoiY.run());

        // 4. Sự kiện: Khi CLICK chuột vào ô input (Fix vấn đề của bạn)
        txtInput.setOnMouseClicked(event -> hienThiGoiY.run());

        // 5. Ẩn khi mất focus
        txtInput.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) suggestionsPopup.hide();
        });
    }

    // Phần tìm kiếm hóa đơn
    private VBox taoPhanThongTimKiem() {
        //VBox all
        VBox vboxAll = new VBox(8);
        //// Ô tìm kiếm
        Label lblTiemKiem = new Label("Tìm kiếm hóa đơn");
        lblTiemKiem.getStyleClass().add("fontTieuDeNho");
        txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Tìm kiếm bằng mã hóa đơn");
        txtTimKiem.getStyleClass().add("timKiem");
        caiDatGoiYTimKiem(txtTimKiem, dsHoaDon);
        Button nutTimKiem = new Button("Tìm kiếm");
        nutTimKiem.getStyleClass().add("button-timKiem");
        HBox oTimKiem = new HBox(10, txtTimKiem, nutTimKiem);
        oTimKiem.setAlignment(Pos.CENTER_LEFT);
        VBox vboxTimKiem = new VBox(5);
        vboxTimKiem.getChildren().addAll(lblTiemKiem, oTimKiem);
        txtTimKiem.setMaxWidth(220);

        txtTimKiem.setTooltip(new Tooltip("Nhấn F3 để đến"));

        txtTimKiem.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                locDanhSach( txtTimKiem.getText(), ngayLoc.getValue(), (String)cboTrangThai.getValue());
                event.consume();
            }
        });

        //Lọc hóa đơn theo ngày
        ngayLoc = new DatePicker();
        ngayLoc.setPrefWidth(150);
        ngayLoc.getStyleClass().add("date-picker");
        ngayLoc.setOnAction(e -> {
            locDanhSach( txtTimKiem.getText(), ngayLoc.getValue(), (String)cboTrangThai.getValue());
        });

        //Lọc theo trạng thái

        String[] trangThai = {"Tất cả", "Đã thanh toán", "Chưa thanh toán", "Đã hủy"};
        cboTrangThai = new ComboBox<>();
        cboTrangThai.getItems().addAll(trangThai);
        cboTrangThai.setValue("Tất cả");
        cboTrangThai.setMaxWidth(120);
        cboTrangThai.setStyle("""
        		-fx-font-size: 15px;
        		-fx-background-radius: 5px;
        		-fx-border-radius: 5px;
        		-fx-border-color: #D9D9D9;
        		""");

        cboTrangThai.setOnAction(e -> {
            locDanhSach( txtTimKiem.getText(), ngayLoc.getValue(), (String)cboTrangThai.getValue());
        });
        //Nút reset lọc
        btnReset = new Button();
        btnReset.setGraphic(createSvgIcon(25, 24,"gray","M16.023 9.348h4.992v-.001M2.985 19.644v-4.992m0 0h4.992m-4.993 0 3.181 3.183a8.25 8.25 0 0 0 13.803-3.7M4.031 9.865a8.25 8.25 0 0 1 13.803-3.7l3.181 3.182m0-4.991v4.99"));
        btnReset.setStyle("""
        		-fx-background-color: null;
        		-fx-border-radius: 5px;
        		-fx-border-color: #D9D9D9;
        		-fx-cursor: hand;
        		-fx-translate-y: -5;
        		""");

        btnReset.setOnAction(e -> taoMoiTimKiem());

        //
        HBox hboxTimKiem = new HBox(20);
        hboxTimKiem.getChildren().addAll(vboxTimKiem, ngayLoc, cboTrangThai, btnReset);
        hboxTimKiem.setAlignment(Pos.BOTTOM_LEFT);

        //CLick nút tìm
        nutTimKiem.setOnAction(e -> {
            locDanhSach( txtTimKiem.getText(), ngayLoc.getValue(), (String)cboTrangThai.getValue());
        });

        //Thông tin hóa đơns
        HBox hbox1 = createInputField("Mã hóa đơn", txtMaHoaDon = new TextField() , true);
        HBox hbox2 = createInputField("Khách hàng", txtKhachHang = new TextField() , true);
        HBox hbox3 = createInputField("Nhân viên", txtNhanVien = new TextField() , true);
        HBox hbox4 = createInputField("Bàn", txtBanTra = new TextField() , true);
        HBox hbox5 = createInputField("Phương thức", txtPhuongThuc = new TextField() , true);
        HBox hbox6 = createInputField("Ngày tạo", txtNgayTao = new TextField() , true);
        HBox hbox7 = createInputField("Tạm tính", txtTongTien = new TextField() , true);
        HBox hbox8 = createInputField("Giảm giá", txtGiamGia = new TextField() , true);
        HBox hbox9 = createInputField("Thuế", txtThue = new TextField() , true);
        HBox hbox10 = createInputField("Cọc", txtTienCoc = new TextField() , true);
        HBox hbox11 = createInputField("Tổng thanh toán", txtTienTT = new TextField() , true);
        HBox hbox12 = createInputField("Trạng thái", txtTrangThai = new TextField() , true);


        VBox vboxTrai = new VBox(4);
        VBox vboxPhai = new VBox(4);

        //
        vboxTrai.getChildren().addAll(hbox1, hbox2,hbox3,hbox4,hbox5,hbox6);
        vboxPhai.getChildren().addAll(hbox7, hbox8,hbox9,hbox10,hbox11,hbox12);

        //
        HBox hboxTTHoaDon = new HBox(10);
        hboxTTHoaDon.getChildren().addAll(vboxTrai, vboxPhai);


        //Button in hóa đơn
        HBox hbox13 = new HBox();
        btnIn = new Button("(F8) In");
        hbox13.setAlignment(Pos.CENTER_RIGHT);
        hbox13.getChildren().add(btnIn);
        hbox13.setPadding(new Insets(0, 0, 0, 10));

        btnIn.setOnAction(e ->{
            thucHienIn();
        });

        btnIn.setGraphic(createSvgIcon(15, 24, "white",
                "M6.72 13.829c-.24.03-.48.062-.72.096m.72-.096a42.415 42.415 0 0 1 10.56 0m-10.56 0L6.34 18m10.94-4.171c.24.03.48.062.72.096m-.72-.096L17.66 18m0 0 .229 2.523a1.125 1.125 0 0 1-1.12 1.227H7.231c-.662 0-1.18-.568-1.12-1.227L6.34 18m11.318 0h1.091A2.25 2.25 0 0 0 21 15.75V9.456c0-1.081-.768-2.015-1.837-2.175a48.055 48.055 0 0 0-1.913-.247M6.34 18H5.25A2.25 2.25 0 0 1 3 15.75V9.456c0-1.081.768-2.015 1.837-2.175a48.041 48.041 0 0 1 1.913-.247m10.5 0a48.536 48.536 0 0 0-10.5 0m10.5 0V3.375c0-.621-.504-1.125-1.125-1.125h-8.25c-.621 0-1.125.504-1.125 1.125v3.659M18 10.5h.008v.008H18V10.5Zm-3 0h.008v.008H15V10.5Z"));
        btnIn.setContentDisplay(ContentDisplay.LEFT);
        btnIn.setGraphicTextGap(10);
        btnIn.getStyleClass().add("btn-In");

        //
        vboxAll.setPadding(new Insets(10));
        vboxAll.getChildren().addAll(hboxTimKiem, hboxTTHoaDon, hbox13);
        vboxAll.setStyle("-fx-border-width: 0 1 0 0; -fx-border-color: #D9D9D9;");
        vboxAll.setMaxWidth(730);
        vboxAll.setMaxHeight(350);

        //
        return vboxAll;
    }

    private VBox taoBangMonAn() {
        VBox vboxAll = new VBox(20);
        vboxAll.setMaxWidth(520);
        Label lblTieuDe = new Label("Danh sách món thanh toán");
        lblTieuDe.getStyleClass().add("fontTieuDeNho");

        tableMonAn = new TableView<>();

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
        TableColumn<String, String> colTenMon = new TableColumn<>("Tên");

        // Gán dữ liệu từ thuộc tính "tenMonAn" trong class MonAn
        colTenMon.setCellValueFactory(cellData -> {
            String tenMon = cellData.getValue().split(",")[0];
            return new SimpleStringProperty(tenMon);
        });
        colTenMon.setPrefWidth(170);

        // Cột số lượng
        TableColumn<String, Integer> colSoLuong = new TableColumn<>("SL");

        // Ở đây mình đang test giá trị cố định = 111 (có thể thay bằng dữ liệu thật)
        colSoLuong.setCellValueFactory( cellData -> {
            int soLuong = Integer.parseInt(cellData.getValue().split(",")[1]);
            return new SimpleIntegerProperty(soLuong).asObject();
        });
        colSoLuong.setPrefWidth(20);


        // Cột giá
        TableColumn<String, Double> colGia = new TableColumn<>("Giá");
        colGia.setCellValueFactory( cellData -> {
            double gia = Double.parseDouble(cellData.getValue().split(",")[2]);
            return new SimpleDoubleProperty(gia).asObject();
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
        colTong.setCellValueFactory( cellData -> {
            double tongTien = Double.parseDouble(cellData.getValue().split(",")[3]);
            return new SimpleDoubleProperty(tongTien).asObject();
        });
        colTong.setCellFactory(tc -> new TableCell<String, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    Label lbl = new Label(String.format("%,.0fđ", item));
                    lbl.setStyle("""
                        -fx-padding: 2 15 2 15 ;
                        -fx-font-weight: bold;
                        -fx-text-fill: black;
                        -fx-font-size: 10;
                    """);

                    StackPane wrapper = new StackPane(lbl);
                    wrapper.setPadding(new Insets(3));

                    setGraphic(wrapper);
                    setText(null);

                }
            }
        });

        tableMonAn.getColumns().addAll(colSTT, colTenMon, colSoLuong, colGia, colTong);
        tableMonAn.setPrefHeight(350);

        //addMonAnTestData(tableMonAn.getItems());

        vboxAll.getChildren().addAll(lblTieuDe, tableMonAn);
        return vboxAll;
    }

    // Phần bảng hóa đơn
    private VBox taoBangHoaDon() {
        //Lay Danh Sach Hoa Don
        DecimalFormat dtf = new DecimalFormat("#,##0.0 đ");
//        dsHoaDon = FXCollections.observableList(control.loadDSHoaDon());
        List<String> dataMoi = control.loadDSHoaDon();
        dsHoaDon.clear();
        if (dataMoi != null) {
            dsHoaDon.addAll(dataMoi);
        }
        VBox vbox = new VBox(5);
        vbox.setPrefWidth(600);
        vbox.setPadding(new Insets(10));

        Label lblTitle = new Label("Danh sách hóa đơn");
        lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");
        tableHoaDon = new TableView<>(dsHoaDon);

        TableColumn<String, String> colMaHoaDon = new TableColumn<>("Mã hóa đơn");
        colMaHoaDon.setCellValueFactory(cellData -> {
            String maHD = cellData.getValue().split(",")[0];
            return new SimpleStringProperty(maHD);
        });
        colMaHoaDon.setPrefWidth(120);
        colMaHoaDon.setStyle("-fx-alignment: CENTER; -fx-font-size: 15px;");


        TableColumn<String, String> colKhachHang = new TableColumn<>("Khách hàng");
        colKhachHang.setCellValueFactory(cellData -> {
            String tenKH = cellData.getValue().split(",")[1];
            return new SimpleStringProperty(tenKH);
        });

        colKhachHang.setPrefWidth(200);
        colKhachHang.setStyle("-fx-alignment: CENTER; -fx-font-size: 15px;");

        TableColumn<String, String> colBan = new TableColumn<>("Bàn");
        colBan.setCellValueFactory(cellData -> {
            String[] chuoi = cellData.getValue().split(",");
            String dsMaBan = String.join(",", Arrays.copyOfRange(chuoi, 10, chuoi.length));

            return new SimpleStringProperty(dsMaBan);
        });
        colBan.setPrefWidth(100);
        colBan.setStyle("-fx-alignment: CENTER; -fx-font-size: 15px;");

        TableColumn<String, String> colTongTien = new TableColumn<>("Tổng tiền");
        colTongTien.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(dtf.format(Double.parseDouble(cellData.getValue().split(",")[3])));
        });
        colTongTien.setPrefWidth(200);
        colTongTien.setStyle("-fx-alignment: CENTER; -fx-font-size: 15px;");

        TableColumn<String, String> colPhuongThuc = new TableColumn<>("Phương thức");
        colPhuongThuc.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().split(",")[5]);
        });
        colPhuongThuc.setPrefWidth(200);

        colPhuongThuc.setCellFactory(tc -> new TableCell<String, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                setStyle("-fx-padding: 1 30 1 30");

                // Trường hợp 1: Dòng trống → reset
                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                // Trường hợp 2: Chưa thanh toán (item null hoặc empty)
                if (item == null || item.isBlank()) {
                    setGraphic(null);
                    setText("Chưa thanh toán");
                    setStyle("-fx-text-fill: gray;");
                    return;
                }

                // Kiểm tra giá trị trong cột hiện tại
                String phuongThuc = item; // chính là giá trị của cột này

                Label lblPhuongThuc = new Label(phuongThuc);
                HBox hbox = new HBox(6);
                hbox.setAlignment(Pos.CENTER);
                //ImageView icon;
                SVGPath icon;
                if (phuongThuc.equalsIgnoreCase("Tiền mặt")) {
                    lblPhuongThuc.setStyle("-fx-text-fill: #1003FF; -fx-font-size: 15px; -fx-font-weight: bold;");
                    hbox.setStyle("-fx-background-color: #D3CFFF; -fx-background-radius: 3; -fx-padding: 3 5 3 5;");
                    icon = createSvgIcon(24, 32, "#1003FF", "M2 7v17h28V7H2zm4 2h20a2 2 0 0 0 2 2v9a2 2 0 0 0-2 2H6a2 2 0 0 0-2-2v-9a2 2 0 0 0 2-2zm10 2c-2.211 0-4 2.016-4 4.5s1.789 4.5 4 4.5c2.211 0 4-2.016 4-4.5S18.211 11 16 11zm0 2c1.102 0 2 1.121 2 2.5s-.898 2.5-2 2.5c-1.102 0-2-1.121-2-2.5s.898-2.5 2-2.5zm-7.5 1a1.5 1.5 0 1 0 .001 3.001A1.5 1.5 0 0 0 8.5 14zm15 0a1.5 1.5 0 1 0 .001 3.001A1.5 1.5 0 0 0 23.5 14z");
                } else {
                    lblPhuongThuc.setStyle("-fx-text-fill: #29D617; -fx-font-size: 15px; -fx-font-weight: bold;");
                    hbox.setStyle("-fx-background-color: #D7F7D3; -fx-background-radius: 3; -fx-padding: 3 5 3 5;");
                    icon = createSvgIcon(14, 14, "#29D617", "M12.91 5.5H1.09c-.56 0-.8-.61-.36-.9L6.64.73a.71.71 0 0 1 .72 0l5.91 3.87c.44.29.2.9-.36.9M13 11H1a.5.5 0 0 0-.5.5V13a.5.5 0 0 0 .5.5h12a.5.5 0 0 0 .5-.5v-1.5a.5.5 0 0 0-.5-.5M2 5.5V11m3.333-5.5V11m3.334-5.5V11M12 5.5V11");
                }
                hbox.getChildren().addAll(icon, lblPhuongThuc);
                setText(null);
                setGraphic(hbox);


            }
        });


        TableColumn<String, String> colNgayTao = new TableColumn<>("Ngày tạo");
        colNgayTao.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().split(",")[6]);
        });
        colNgayTao.setPrefWidth(200);
        colNgayTao.setStyle("-fx-alignment: CENTER; -fx-font-size: 15px;");


        TableColumn<String, String> colThanhToan = new TableColumn<>("TT");
        colThanhToan.setPrefWidth(50);
        colThanhToan.setCellValueFactory(data -> {
            String[] parts = data.getValue().split(",");
            return new SimpleStringProperty(parts[7]);
        });
        colThanhToan.setStyle("-fx-alignment: CENTER; -fx-font-size: 15px;");
        colThanhToan.setCellFactory(tc -> new TableCell<String, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                setStyle("-fx-padding: 1 10 1 10");

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    // Kiểm tra giá trị trong cột hiện tại
                    String thanhToan = item; // chính là giá trị của cột này
                    HBox hbox = new HBox(6);
                    hbox.setAlignment(Pos.CENTER);
                    //ImageView icon;
                    SVGPath icon;
                    if (thanhToan.equalsIgnoreCase("Đã thanh toán")) {
                        icon = createSvgIcon(24, 24, "Green", "m4.5 12.75 6 6 9-13.5");
                    } else {
                        icon = createSvgIcon(24, 24, "red", "M6 18 18 6M6 6l12 12");
                    }
                    hbox.getChildren().addAll(icon);

                    setGraphic(hbox);

                }
            }
        });

        TableColumn<String, String> colCoc = new TableColumn<>("Cọc");
        colCoc.setCellValueFactory(cellData -> {
            String[] chuoi = cellData.getValue().split(",");
            String ghiChu = chuoi[8];
            String loaiString = chuoi[9];
            LoaiBan loai;
            if(loaiString.equals("VIP")) {
                loai = LoaiBan.VIP;
            }else {
                loai = LoaiBan.THUONG;
            }

            double tienCoc = control.tinhTienCocLoaiVaGhiChu(loai, ghiChu);

            return new SimpleStringProperty(dtf.format(tienCoc));
        });

        colCoc.setPrefWidth(100);
        colCoc.setStyle("-fx-alignment: CENTER;");

        tableHoaDon.getColumns().addAll(colMaHoaDon, colKhachHang, colBan, colTongTien, colCoc, colPhuongThuc, colNgayTao, colThanhToan);
        tableHoaDon.setPrefHeight(250);
        tableHoaDon.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //CLICK
        tableHoaDon.setOnMouseClicked(event -> {
            String chuoi = tableHoaDon.getSelectionModel().getSelectedItem();

            String[] chuoiTach = chuoi.split(",");

            DecimalFormat dcm = new  DecimalFormat("#,##0.0 đ");

            double tongTien = Double.parseDouble(chuoiTach[3]);
            double giamGia = Double.parseDouble(chuoiTach[4]);
            double tienCoc = control.tinhTienCocLoaiVaGhiChu(chuoiTach[10].equals("VIP") ? LoaiBan.VIP : LoaiBan.THUONG , chuoiTach[9]);
            double tienThue = control.tinhThue(Double.parseDouble(chuoiTach[3]));

            txtMaHoaDon.setText(chuoiTach[0]);
            txtKhachHang.setText(chuoiTach[1]);
            txtNhanVien.setText(chuoiTach[2]);
            txtBanTra.setText(String.join(",", Arrays.copyOfRange(chuoiTach, 10, chuoiTach.length)));
            txtPhuongThuc.setText(chuoiTach[5]);
            txtNgayTao.setText(chuoiTach[6]);
            txtTongTien.setText(dcm.format(tongTien));
            txtGiamGia.setText(dcm.format(giamGia));
            txtThue.setText(dcm.format(tienThue));
            txtTienCoc.setText(dcm.format(tienCoc));

            double tienTT = 0.0;
            tienTT = control.tinhTienThanhToan(tongTien, giamGia, tienThue, tienCoc);
            if(tienTT >= 0)
                txtTienTT.setText(dcm.format(tienTT));
            else {
                txtTienTT.setText("Thối lại: " + dcm.format(-tienTT));
            }

            txtTrangThai.setText(chuoiTach[7]);


            loadDanhSachMonAn(chuoiTach[0]);

        });

        vbox.getChildren().addAll(lblTitle, tableHoaDon);

        ///


        return vbox;
    }

    private void loadDanhSachMonAn(String maHoaDon) {
        if (maHoaDon != null) {
            List<String> ds = control.dsThongTinMonAnTheoMaHD(maHoaDon);
            dsThongTinMonAn = FXCollections.observableArrayList(ds);
            tableMonAn.getItems().clear();
            tableMonAn.setItems(dsThongTinMonAn);
        }
    }


    private SVGPath createSvgIcon(double size, double viewBox, String mau, String pathData) {
        SVGPath svg = new SVGPath();
        svg.setContent(pathData);
        svg.setScaleX(size / viewBox);
        svg.setScaleY(size / viewBox);
        svg.setStyle("-fx-stroke: " + mau + "; -fx-fill: transparent;");
        return svg;
    }

    private HBox createInputField(String labelText, TextField textField, boolean isReadOnly) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);

        Label label = new Label(labelText);
        label.setMinWidth(110);
        label.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        label.setStyle("-fx-text-fill: #34495e;");

        textField.setPrefWidth(220);
        textField.setEditable(!isReadOnly);

        if (isReadOnly) {
            textField.setStyle(
                    "-fx-background-color: #ECF0F1; " +
                            "-fx-border-color: #bdc3c7; " +
                            "-fx-border-radius: 6; " +
                            "-fx-background-radius: 6; " +
                            "-fx-padding: 5 10; " +
                            "-fx-font-size: 15px; " +
                            "-fx-text-fill: #7f8c8d;"
            );
        } else {
            textField.setStyle(
                    "-fx-background-color: white; " +
                            "-fx-border-color: #3498db; " +
                            "-fx-border-width: 1.5; " +
                            "-fx-border-radius: 6; " +
                            "-fx-background-radius: 6; " +
                            "-fx-padding: 5 10; " +
                            "-fx-font-size: 15px;"
            );

            textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    textField.setStyle(
                            "-fx-background-color: white; " +
                                    "-fx-border-color: #667eea; " +
                                    "-fx-border-width: 1.5; " +
                                    "-fx-border-radius: 6; " +
                                    "-fx-background-radius: 6; " +
                                    "-fx-padding: 10; " +
                                    "-fx-font-size: 13px; " +
                                    "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.3), 8, 0, 0, 2);"
                    );
                } else {
                    textField.setStyle(
                            "-fx-background-color: white; " +
                                    "-fx-border-color: #3498db; " +
                                    "-fx-border-width: 1.5; " +
                                    "-fx-border-radius: 6; " +
                                    "-fx-background-radius: 6; " +
                                    "-fx-padding: 10; " +
                                    "-fx-font-size: 13px;"
                    );
                }
            });
        }

        row.getChildren().addAll(label, textField);
        return row;
    }

    public void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void taoMoiTimKiem() {
        flag = true;

        txtTimKiem.setText("");
        cboTrangThai.setValue("Tất cả");
        ngayLoc.setValue(null);
        tableHoaDon.setItems(dsHoaDon);
        tableMonAn.getItems().clear();
        txtMaHoaDon.setText("");
        txtKhachHang.setText("");
        txtNhanVien.setText("");
        txtNgayTao.setText("");
        txtPhuongThuc.setText("");
        txtTongTien.setText("");
        txtThue.setText("");
        txtGiamGia.setText("");
        txtTienTT.setText("");
        txtTienCoc.setText("");
        txtTrangThai.setText("");
        txtBanTra.setText("");

        flag = false;
    }

    private void locDanhSach(String maTim, LocalDate ngayChon, String trangThai) {
        if(flag == true) {
            return;
        }

        ObservableList<String> dsLoc = control.locHoaDon(dsHoaDon, maTim, ngayChon, trangThai);

        if(dsLoc == null || dsLoc.isEmpty()) {
            showAlert(AlertType.INFORMATION, "Không tìm thấy", "Không tìm thấy hóa đơn phù hợp !!");
        }

        tableHoaDon.setItems(dsLoc);
    }


    public void inHoaDon(Stage owner, List<String> danhSach, String hoaDon) {
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
                pageBox.getChildren().add(taoHeader(1, 1, hoaDon)); // totalPages không biết trước, có thể để 1 hoặc tính trước
            }

            // Tính items cho trang này
            int itemsPerPage = (pageNum == 1) ? 19 : 25;
            int remaining = danhSach.size() - currentIndex;
            int itemsThisPage = Math.min(itemsPerPage, remaining);
            hasMore = remaining > itemsThisPage;

            if (itemsThisPage > 0) {
                int from = currentIndex;
                int to = currentIndex + itemsThisPage;
                List<String> subList = danhSach.subList(from, to);

                TableView<String> table = taoTable(subList, sttGlobal);
                pageBox.getChildren().add(table);

                // Cập nhật cho trang sau
                currentIndex += itemsThisPage;
                sttGlobal += itemsThisPage;
            }

            pageBox.getChildren().add(taoFooter(!hasMore, hoaDon)); // Footer chỉ trang cuối

            pageBox.setAlignment(Pos.CENTER);
            pageBox.applyCss();
            pageBox.layout();

            // Scale nếu page cao quá (hiếm vì itemsPerPage fit)
            double pageHeight = pageBox.getBoundsInLocal().getHeight();
            if (pageHeight > maxHeight) {
                double scale = maxHeight / pageHeight;
                pageBox.setScaleX(scale);
                pageBox.setScaleY(scale);
            }

            job.printPage(layout, pageBox);
            pageNum++;
        }

        if(job.endJob()) {
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
        table.setPrefHeight(ds.size() * 25 + 30);
        return table;
    }

    private VBox taoHeader(int page, int totalPages, String hoaDon) {
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

        Label lblNgay = new Label("Ngày: " + hoaDon.split(",")[6]);
        Label lblMaHoaDon = new Label("Mã hóa đơn: " + hoaDon.split(",")[0]);

        hbox1.getChildren().addAll(lblNgay, spacer1, lblMaHoaDon);

        Label lblMaBan = new Label("Bàn: " + String.join(",", Arrays.copyOfRange(hoaDon.split(","), 10, hoaDon.split(",").length)));
        HBox hbox2 = new HBox(lblMaBan);
        hbox2.setAlignment(Pos.CENTER_LEFT);
        Label lblThuNgan = new Label("Thu ngân: " + hoaDon.split(",")[2]);
        HBox hbox3 = new HBox(lblThuNgan);
        hbox3.setAlignment(Pos.CENTER_LEFT);

        box.setAlignment(Pos.CENTER);

        box.setMargin(hbox1, new Insets(0, 15, 0, 0));
        box.getChildren().addAll(lblTenNhaHang, lblDiaChi, lblSdt, lblHoaDon, hbox1, hbox2, hbox3, new Separator());
        return box;
    }

    private VBox taoFooter(boolean lastPage, String hoaDon) {
        VBox box = new VBox(5);
        box.setMaxWidth(480);
        DecimalFormat dcm = new DecimalFormat("#,##0.0 VND");

        String[] hoaDonSplit = hoaDon.split(",");

        String tongTienString = hoaDonSplit[3];
        String giamGiaString = hoaDonSplit[4];
        String loaiBan = hoaDonSplit[9];
        String ghiChu = hoaDonSplit[8];

        double tongTien = Double.parseDouble(tongTienString);
        double giamGia = Double.parseDouble(giamGiaString);
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

            box.getChildren().addAll(hbox1, hbox2, hbox3, line, hbox4,  line2, hbox5);

        }
        box.setStyle("-fx-padding: 10 0 0 0;");
        return box;
    }

    public void thucHienIn() {
        String maHoaDon = txtMaHoaDon.getText();

        if(maHoaDon == null || maHoaDon.isBlank()) {
            showAlert(AlertType.ERROR, "Lỗi", "Vui lòng chọn một hóa đơn trước khi in");
            return;
        }

        List<String> dsMon = new ArrayList<String>(dsThongTinMonAn);

        String hoaDonChuoi = tableHoaDon.getSelectionModel().getSelectedItem();

        inHoaDon((Stage) this.getScene().getWindow(), dsMon, hoaDonChuoi);
    }

}

	