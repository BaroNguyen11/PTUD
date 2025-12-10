//package gui;
//import javafx.print.PageLayout;
//import javafx.print.Paper;
//import javafx.print.PageOrientation;
//import javafx.print.Printer;
//import java.text.DecimalFormat;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.awt.Graphics2D;
//import java.awt.print.*;
//import javafx.scene.Node;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.beans.property.SimpleStringProperty;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javafx.scene.input.MouseEvent;
//import javafx.scene.input.KeyCode;
//import javafx.scene.input.KeyCodeCombination;
//import javafx.scene.input.KeyCombination;
//import javafx.scene.input.MouseButton;
//import ctrl.QLHD_Ctrl;
//import dao.PhieuDatBan_DAO;
//import entity.BanAn;
//import entity.HoaDon;
//import entity.LoaiBan;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.geometry.Insets;
//import javafx.beans.property.SimpleDoubleProperty;
//import javafx.beans.property.SimpleIntegerProperty;
//import javafx.geometry.Pos;
//import javafx.print.PrinterJob;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Alert.AlertType;
//import javafx.scene.control.Button;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.ContentDisplay;
//import javafx.scene.control.DatePicker;
//import javafx.scene.control.Label;
//import javafx.scene.control.ScrollPane;
//import javafx.scene.control.Separator;
//import javafx.scene.control.TableCell;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.control.TextField;
//import javafx.scene.control.Tooltip;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.Priority;
//import javafx.scene.layout.Region;
//import javafx.scene.layout.StackPane;
//import javafx.scene.layout.VBox;
//import javafx.scene.shape.SVGPath;
//import javafx.scene.text.Font;
//import javafx.scene.text.FontWeight;
//import javafx.stage.Modality;
//import javafx.stage.Stage;
//
//public class Gui_QuanLiHoaDon extends BorderPane {
//	private QLHD_Ctrl control;
//    // Dữ liệu test cho món ăn
//    private ObservableList<String> dsThongTinMonAn = FXCollections.observableArrayList();
//    // Dữ liệu test cho hóa đơn
//    private ObservableList<String> dsHoaDon = FXCollections.observableArrayList();
//	private TextField txtMaHoaDon;
//	private TextField txtKhachHang;
//	private TextField txtNhanVien;
//	private TextField txtTongTien;
//	private TextField txtPhuongThuc;
//	private TextField txtTienCoc;
//	private TextField txtNgayTao;
//	private TextField txtBanTra;
//	private TextField txtThue;
//	private TextField txtTrangThai;
//	private TextField txtTienTT;
//	private TextField txtGiamGia;
//	private TableView tableMonAn;
//	private TableView<String> tableHoaDon;
//	private TextField txtTimKiem;
//	private ComboBox cboTrangThai;
//	private DatePicker ngayLoc;
//	private Button btnReset;
//	private boolean flag = false;
//	private final Map<KeyCombination, Runnable> shortcuts = new HashMap<>();
//	private Button btnIn;
//
//    public Gui_QuanLiHoaDon() {
//    		control = new QLHD_Ctrl();
//
//        // Phần thông tin và tìm kiếm
//        VBox bangThongTinTimKiem = taoPhanThongTimKiem();
//        this.setCenter(bangThongTinTimKiem);
//        BorderPane.setAlignment(bangThongTinTimKiem, Pos.TOP_LEFT);
//        BorderPane.setMargin(bangThongTinTimKiem, new Insets(20, 40, 10, 0));
//
//        // Bảng danh sách món thanh toán
//        VBox bangMonAn = taoBangMonAn();
//        this.setRight(bangMonAn);
//        BorderPane.setMargin(bangMonAn, new Insets(10, 20, 10, 10));
//
//        //Bảng hóa đơn
//        this.setBottom(taoBangHoaDon());
//
//        /////Hàng f
//        KeyCombination f3 = new KeyCodeCombination(KeyCode.F3);
//        KeyCombination f8 = new KeyCodeCombination(KeyCode.F8);
//
//
//        //
//        shortcuts.put(f3, () -> txtTimKiem.requestFocus());
//        shortcuts.put(f8, () -> btnIn.fire());
//
//        this.sceneProperty().addListener((obs, oldScene, newScene) -> {
//            if (newScene != null) {
//                newScene.getAccelerators().putAll(shortcuts);
//            }
//
//            if(oldScene != null) {
//            		oldScene.getAccelerators().clear();
//            }
//        });
//
//
//        this.getStylesheets().add(getClass().getResource("/css/qlhd.css").toExternalForm());
//        this.setStyle("-fx-background-color: white");
//
//    }
//
//
//    // Phần tìm kiếm hóa đơn
//    private VBox taoPhanThongTimKiem() {
//        //VBox all
//        VBox vboxAll = new VBox(8);
//        //// Ô tìm kiếm
//        Label lblTiemKiem = new Label("Tìm kiếm hóa đơn");
//        lblTiemKiem.getStyleClass().add("fontTieuDeNho");
//        txtTimKiem = new TextField();
//        txtTimKiem.setPromptText("Tìm kiếm bằng mã hóa đơn");
//        txtTimKiem.getStyleClass().add("timKiem");
//        Button nutTimKiem = new Button("Tìm");
//        nutTimKiem.getStyleClass().add("button-timKiem");
//        HBox oTimKiem = new HBox(10, txtTimKiem, nutTimKiem);
//        oTimKiem.setAlignment(Pos.CENTER_LEFT);
//        VBox vboxTimKiem = new VBox(5);
//        vboxTimKiem.getChildren().addAll(lblTiemKiem, oTimKiem);
//        txtTimKiem.setMaxWidth(220);
//
//        txtTimKiem.setTooltip(new Tooltip("Nhấn F3 để đến"));
//
//        txtTimKiem.setOnKeyPressed(event -> {
//            if (event.getCode() == KeyCode.ENTER) {
//            		locDanhSach( txtTimKiem.getText(), ngayLoc.getValue(), (String)cboTrangThai.getValue());
//                event.consume();
//            }
//        });
//
//        //Lọc hóa đơn theo ngày
//        ngayLoc = new DatePicker();
//        ngayLoc.setPrefWidth(150);
//        ngayLoc.getStyleClass().add("date-picker");
//
//        ngayLoc.setOnAction(e -> {
//        		locDanhSach( txtTimKiem.getText(), ngayLoc.getValue(), (String)cboTrangThai.getValue());
//        });
//
//        //Lọc theo trạng thái
//
//        String[] trangThai = {"Tất cả", "Đã thanh toán", "Chưa thanh toán", "Đã hủy"};
//        cboTrangThai = new ComboBox<>();
//        cboTrangThai.getItems().addAll(trangThai);
//        cboTrangThai.setValue("Tất cả");
//        cboTrangThai.setMaxWidth(120);
//        cboTrangThai.setStyle("""
//        		-fx-font-size: 15px;
//        		-fx-background-radius: 5px;
//        		-fx-border-radius: 5px;
//        		-fx-border-color: #D9D9D9;
//        		""");
//
//        cboTrangThai.setOnAction(e -> {
//        		locDanhSach( txtTimKiem.getText(), ngayLoc.getValue(), (String)cboTrangThai.getValue());
//        		});
//        //Nút reset lọc
//        btnReset = new Button();
//        btnReset.setGraphic(createSvgIcon(25, 24,"gray","M16.023 9.348h4.992v-.001M2.985 19.644v-4.992m0 0h4.992m-4.993 0 3.181 3.183a8.25 8.25 0 0 0 13.803-3.7M4.031 9.865a8.25 8.25 0 0 1 13.803-3.7l3.181 3.182m0-4.991v4.99"));
//        btnReset.setStyle("""
//        		-fx-background-color: null;
//        		-fx-border-radius: 5px;
//        		-fx-border-color: #D9D9D9;
//        		-fx-cursor: hand;
//        		-fx-translate-y: -5;
//        		""");
//
//        btnReset.setOnAction(e -> taoMoiTimKiem());
//
//        //
//        HBox hboxTimKiem = new HBox(20);
//        hboxTimKiem.getChildren().addAll(vboxTimKiem, ngayLoc, cboTrangThai, btnReset);
//        hboxTimKiem.setAlignment(Pos.BOTTOM_LEFT);
//
//        //CLick nút tìm
//        nutTimKiem.setOnAction(e -> {
//        		locDanhSach( txtTimKiem.getText(), ngayLoc.getValue(), (String)cboTrangThai.getValue());
//        });
//
//        //Thông tin hóa đơns
//        HBox hbox1 = createInputField("Mã hóa đơn", txtMaHoaDon = new TextField() , true);
//        HBox hbox2 = createInputField("Khách hàng", txtKhachHang = new TextField() , true);
//        HBox hbox3 = createInputField("Nhân viên", txtNhanVien = new TextField() , true);
//        HBox hbox4 = createInputField("Bàn", txtBanTra = new TextField() , true);
//        HBox hbox5 = createInputField("Phương thức", txtPhuongThuc = new TextField() , true);
//        HBox hbox6 = createInputField("Ngày tạo", txtNgayTao = new TextField() , true);
//        HBox hbox7 = createInputField("Tạm tính", txtTongTien = new TextField() , true);
//        HBox hbox8 = createInputField("Giảm giá", txtGiamGia = new TextField() , true);
//        HBox hbox9 = createInputField("Thuế", txtThue = new TextField() , true);
//        HBox hbox10 = createInputField("Cọc", txtTienCoc = new TextField() , true);
//        HBox hbox11 = createInputField("Tổng thanh toán", txtTienTT = new TextField() , true);
//        HBox hbox12 = createInputField("Trạng thái", txtTrangThai = new TextField() , true);
//
//
//        VBox vboxTrai = new VBox(4);
//        VBox vboxPhai = new VBox(4);
//
//        //
//        vboxTrai.getChildren().addAll(hbox1, hbox2,hbox3,hbox4,hbox5,hbox6);
//        vboxPhai.getChildren().addAll(hbox7, hbox8,hbox9,hbox10,hbox11,hbox12);
//
//        //
//        HBox hboxTTHoaDon = new HBox(10);
//        hboxTTHoaDon.getChildren().addAll(vboxTrai, vboxPhai);
//
//
//        //Button in hóa đơn
//        HBox hbox13 = new HBox();
//        btnIn = new Button("(F8) In");
//        hbox13.setAlignment(Pos.CENTER_RIGHT);
//        hbox13.getChildren().add(btnIn);
//        hbox13.setPadding(new Insets(0, 0, 0, 10));
//
//        btnIn.setOnAction(e ->{
//        		thucHienIn();
//        });
//
//        btnIn.setGraphic(createSvgIcon(15, 24, "white",
//				"M6.72 13.829c-.24.03-.48.062-.72.096m.72-.096a42.415 42.415 0 0 1 10.56 0m-10.56 0L6.34 18m10.94-4.171c.24.03.48.062.72.096m-.72-.096L17.66 18m0 0 .229 2.523a1.125 1.125 0 0 1-1.12 1.227H7.231c-.662 0-1.18-.568-1.12-1.227L6.34 18m11.318 0h1.091A2.25 2.25 0 0 0 21 15.75V9.456c0-1.081-.768-2.015-1.837-2.175a48.055 48.055 0 0 0-1.913-.247M6.34 18H5.25A2.25 2.25 0 0 1 3 15.75V9.456c0-1.081.768-2.015 1.837-2.175a48.041 48.041 0 0 1 1.913-.247m10.5 0a48.536 48.536 0 0 0-10.5 0m10.5 0V3.375c0-.621-.504-1.125-1.125-1.125h-8.25c-.621 0-1.125.504-1.125 1.125v3.659M18 10.5h.008v.008H18V10.5Zm-3 0h.008v.008H15V10.5Z"));
//		btnIn.setContentDisplay(ContentDisplay.LEFT);
//		btnIn.setGraphicTextGap(10);
//		btnIn.getStyleClass().add("btn-In");
//
//        //
//        vboxAll.setPadding(new Insets(10));
//        vboxAll.getChildren().addAll(hboxTimKiem, hboxTTHoaDon, hbox13);
//        vboxAll.setStyle("-fx-border-width: 0 1 0 0; -fx-border-color: #D9D9D9;");
//        vboxAll.setMaxWidth(730);
//        vboxAll.setMaxHeight(350);
//
//        //
//        return vboxAll;
//    }
//
//    private VBox taoBangMonAn() {
//        VBox vboxAll = new VBox(20);
//        vboxAll.setMaxWidth(520);
//        Label lblTieuDe = new Label("Danh sách món thanh toán");
//        lblTieuDe.getStyleClass().add("fontTieuDeNho");
//
//        tableMonAn = new TableView<>();
//
//        // Cột STT
//        TableColumn<String, Void> colSTT = new TableColumn<>("STT");
//        colSTT.setPrefWidth(40);
//        colSTT.setSortable(false);
//        colSTT.setCellFactory(col -> new TableCell<String, Void>() {
//            @Override
//            protected void updateItem(Void item, boolean empty) {
//                super.updateItem(item, empty);
//                setText(empty ? null : String.valueOf(getIndex() + 1));
//                setAlignment(Pos.CENTER);
//            }
//        });
//
//        // Cột tên món
//        TableColumn<String, String> colTenMon = new TableColumn<>("Tên");
//
//        // Gán dữ liệu từ thuộc tính "tenMonAn" trong class MonAn
//        colTenMon.setCellValueFactory(cellData -> {
//        		String tenMon = cellData.getValue().split(",")[0];
//        		return new SimpleStringProperty(tenMon);
//        });
//        colTenMon.setPrefWidth(170);
//
//        // Cột số lượng
//        TableColumn<String, Integer> colSoLuong = new TableColumn<>("SL");
//
//        // Ở đây mình đang test giá trị cố định = 111 (có thể thay bằng dữ liệu thật)
//        colSoLuong.setCellValueFactory( cellData -> {
//        		int soLuong = Integer.parseInt(cellData.getValue().split(",")[1]);
//        		return new SimpleIntegerProperty(soLuong).asObject();
//        });
//        colSoLuong.setPrefWidth(20);
//
//
//        // Cột giá
//        TableColumn<String, Double> colGia = new TableColumn<>("Giá");
//        colGia.setCellValueFactory( cellData -> {
//        		double gia = Double.parseDouble(cellData.getValue().split(",")[2]);
//        		return new SimpleDoubleProperty(gia).asObject();
//        });
//        colGia.setPrefWidth(150);
//        colGia.setCellFactory(tc -> new TableCell<String, Double>() {
//            @Override
//            protected void updateItem(Double item, boolean empty) {
//                super.updateItem(item, empty);
//                setText(empty || item == null ? null : String.format("%,.0fđ", item));
//                setAlignment(Pos.CENTER);
//            }
//
//        });
//
//        // Cột tổng tiền
//        TableColumn<String, Double> colTong = new TableColumn<>("Tổng tiền");
//        colTong.setPrefWidth(150);
//        colTong.setCellValueFactory( cellData -> {
//        		double tongTien = Double.parseDouble(cellData.getValue().split(",")[3]);
//        		return new SimpleDoubleProperty(tongTien).asObject();
//        });
//        colTong.setCellFactory(tc -> new TableCell<String, Double>() {
//            @Override
//            protected void updateItem(Double item, boolean empty) {
//                super.updateItem(item, empty);
//
//                if (empty || item == null) {
//                    setText(null);
//                    setGraphic(null);
//                    setStyle("");
//                } else {
//                    Label lbl = new Label(String.format("%,.0fđ", item));
//                    lbl.setStyle("""
//                        -fx-background-color: #D7F7D3;
//                        -fx-background-radius: 3;
//                        -fx-padding: 2 15 2 15 ;
//                        -fx-font-weight: bold;
//                        -fx-text-fill: black;
//                        -fx-font-size: 10;
//                    """);
//
//                    StackPane wrapper = new StackPane(lbl);
//                    wrapper.setPadding(new Insets(3));
//
//                    setGraphic(wrapper);
//                    setText(null);
//
//                }
//            }
//        });
//
//        tableMonAn.getColumns().addAll(colSTT, colTenMon, colSoLuong, colGia, colTong);
//        tableMonAn.setPrefHeight(350);
//
//        //addMonAnTestData(tableMonAn.getItems());
//
//        vboxAll.getChildren().addAll(lblTieuDe, tableMonAn);
//        return vboxAll;
//    }
//
//    // Phần bảng hóa đơn
//    private VBox taoBangHoaDon() {
//    	//Lay Danh Sach Hoa Don
//    		DecimalFormat dtf = new DecimalFormat("#,##0.0 đ");
//        dsHoaDon = FXCollections.observableList(control.loadDSHoaDon());
//        VBox vbox = new VBox(5);
//        vbox.setPrefWidth(600);
//        vbox.setPadding(new Insets(10));
//
//        Label lblTitle = new Label("Danh sách hóa đơn");
//        lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");
//        tableHoaDon = new TableView<>(dsHoaDon);
//
//        TableColumn<String, String> colMaHoaDon = new TableColumn<>("Mã hóa đơn");
//        colMaHoaDon.setCellValueFactory(cellData -> {
//            String maHD = cellData.getValue().split(",")[0];
//            return new SimpleStringProperty(maHD);
//        });
//        colMaHoaDon.setPrefWidth(120);
//        colMaHoaDon.setStyle("-fx-alignment: CENTER; -fx-font-size: 15px;");
//
//
//        TableColumn<String, String> colKhachHang = new TableColumn<>("Khách hàng");
//        colKhachHang.setCellValueFactory(cellData -> {
//            String tenKH = cellData.getValue().split(",")[1];
//            return new SimpleStringProperty(tenKH);
//        });
//
//        colKhachHang.setPrefWidth(200);
//        colKhachHang.setStyle("-fx-alignment: CENTER; -fx-font-size: 15px;");
//
//        TableColumn<String, String> colBan = new TableColumn<>("Bàn");
//        colBan.setCellValueFactory(cellData -> {
//        		String[] chuoi = cellData.getValue().split(",")[9].split("_");
//        		String dsMaBan = String.join(",", Arrays.copyOfRange(chuoi, 0, chuoi.length));
//
//        		return new SimpleStringProperty(dsMaBan);
//        });
//        colBan.setPrefWidth(100);
//        colBan.setStyle("-fx-alignment: CENTER; -fx-font-size: 15px;");
//
//        TableColumn<String, String> colTongTien = new TableColumn<>("Tổng tiền");
//        colTongTien.setCellValueFactory(cellData -> {
//        		return new SimpleStringProperty(dtf.format(Double.parseDouble(cellData.getValue().split(",")[3])));
//        });
//        colTongTien.setPrefWidth(200);
//        colTongTien.setStyle("-fx-alignment: CENTER; -fx-font-size: 15px;");
//
//        TableColumn<String, String> colPhuongThuc = new TableColumn<>("Phương thức");
//        colPhuongThuc.setCellValueFactory(cellData -> {
//    			return new SimpleStringProperty(cellData.getValue().split(",")[5]);
//        });
//        colPhuongThuc.setPrefWidth(200);
//
//        colPhuongThuc.setCellFactory(tc -> new TableCell<String, String>() {
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//
//                setStyle("-fx-padding: 1 30 1 30");
//
//                // Trường hợp 1: Dòng trống → reset
//                if (empty) {
//                    setGraphic(null);
//                    setText(null);
//                    return;
//                }
//
//                // Trường hợp 2: Chưa thanh toán (item null hoặc empty)
//                if (item == null || item.isBlank()) {
//                    setGraphic(null);
//                    setText("Chưa thanh toán");
//                    setStyle("-fx-text-fill: gray;");
//                    return;
//                }
//
//                    // Kiểm tra giá trị trong cột hiện tại
//                    String phuongThuc = item; // chính là giá trị của cột này
//
//                    Label lblPhuongThuc = new Label(phuongThuc);
//                    HBox hbox = new HBox(6);
//                    hbox.setAlignment(Pos.CENTER);
//                    //ImageView icon;
//                    SVGPath icon;
//                    if (phuongThuc.equalsIgnoreCase("Tiền mặt")) {
//                        lblPhuongThuc.setStyle("-fx-text-fill: #1003FF; -fx-font-size: 15px; -fx-font-weight: bold;");
//                        hbox.setStyle("-fx-background-color: #D3CFFF; -fx-background-radius: 3; -fx-padding: 3 5 3 5;");
//                        icon = createSvgIcon(24, 32, "#1003FF", "M2 7v17h28V7H2zm4 2h20a2 2 0 0 0 2 2v9a2 2 0 0 0-2 2H6a2 2 0 0 0-2-2v-9a2 2 0 0 0 2-2zm10 2c-2.211 0-4 2.016-4 4.5s1.789 4.5 4 4.5c2.211 0 4-2.016 4-4.5S18.211 11 16 11zm0 2c1.102 0 2 1.121 2 2.5s-.898 2.5-2 2.5c-1.102 0-2-1.121-2-2.5s.898-2.5 2-2.5zm-7.5 1a1.5 1.5 0 1 0 .001 3.001A1.5 1.5 0 0 0 8.5 14zm15 0a1.5 1.5 0 1 0 .001 3.001A1.5 1.5 0 0 0 23.5 14z");
//                    } else {
//                        lblPhuongThuc.setStyle("-fx-text-fill: #29D617; -fx-font-size: 15px; -fx-font-weight: bold;");
//                        hbox.setStyle("-fx-background-color: #D7F7D3; -fx-background-radius: 3; -fx-padding: 3 5 3 5;");
//                        icon = createSvgIcon(14, 14, "#29D617", "M12.91 5.5H1.09c-.56 0-.8-.61-.36-.9L6.64.73a.71.71 0 0 1 .72 0l5.91 3.87c.44.29.2.9-.36.9M13 11H1a.5.5 0 0 0-.5.5V13a.5.5 0 0 0 .5.5h12a.5.5 0 0 0 .5-.5v-1.5a.5.5 0 0 0-.5-.5M2 5.5V11m3.333-5.5V11m3.334-5.5V11M12 5.5V11");
//                    }
//                    hbox.getChildren().addAll(icon, lblPhuongThuc);
//                    setText(null);
//                    setGraphic(hbox);
//
//
//            }
//        });
//
//
//        TableColumn<String, String> colNgayTao = new TableColumn<>("Ngày tạo");
//        colNgayTao.setCellValueFactory(cellData -> {
//    			return new SimpleStringProperty(cellData.getValue().split(",")[6]);
//        });
//        colNgayTao.setPrefWidth(200);
//        colNgayTao.setStyle("-fx-alignment: CENTER; -fx-font-size: 15px;");
//
//
//        TableColumn<String, String> colThanhToan = new TableColumn<>("TT");
//        colThanhToan.setPrefWidth(50);
//        colThanhToan.setCellValueFactory(data -> {
//            String[] parts = data.getValue().split(",");
//            return new SimpleStringProperty(parts[7]);
//        });
//        colThanhToan.setStyle("-fx-alignment: CENTER; -fx-font-size: 15px;");
//        colThanhToan.setCellFactory(tc -> new TableCell<String, String>() {
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//
//                setStyle("-fx-padding: 1 10 1 10");
//
//                if (empty || item == null) {
//                    setGraphic(null);
//                    setText(null);
//                } else {
//                    // Kiểm tra giá trị trong cột hiện tại
//                    String thanhToan = item; // chính là giá trị của cột này
//                    HBox hbox = new HBox(6);
//                    hbox.setAlignment(Pos.CENTER);
//                    //ImageView icon;
//                    SVGPath icon;
//                    if (thanhToan.equalsIgnoreCase("Đã thanh toán")) {
//                        icon = createSvgIcon(24, 24, "Green", "m4.5 12.75 6 6 9-13.5");
//                    } else {
//                        icon = createSvgIcon(24, 24, "red", "M6 18 18 6M6 6l12 12");
//                    }
//                    hbox.getChildren().addAll(icon);
//
//                    setGraphic(hbox);
//
//                }
//            }
//        });
//
//        TableColumn<String, String> colCoc = new TableColumn<>("Cọc");
//        colCoc.setCellValueFactory(cellData -> {
//        		String[] chuoi = cellData.getValue().split(",");
//        		String ghiChu = PhieuDatBan_DAO.timMotPhieuBangMaHD(chuoi[0]).getGhiChu();
//        		String dsBan = chuoi[9];
//
//        		if(ghiChu.equals("Dùng ngay")) {
//        			return new SimpleStringProperty(dtf.format(0.0));
//        		}
//
//        		double tienCoc = control.tinhTienCocTheoDSBan(dsBan);
//
//        		return new SimpleStringProperty(dtf.format(tienCoc));
//        });
//
//        colCoc.setPrefWidth(100);
//        colCoc.setStyle("-fx-alignment: CENTER;");
//
//        tableHoaDon.getColumns().addAll(colMaHoaDon, colKhachHang, colBan, colTongTien, colCoc, colPhuongThuc, colNgayTao, colThanhToan);
//        tableHoaDon.setPrefHeight(250);
//        tableHoaDon.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//
//        //CLICK
//        tableHoaDon.setOnMouseClicked(event -> {
//
//        		if(tableHoaDon.getSelectionModel().getSelectedItem() == null) {
//        			//System.err.println("Sao null tá");
//        			return;
//        		}
//            String chuoi = tableHoaDon.getSelectionModel().getSelectedItem();
//
//            if(chuoi == null) {
//	    			//System.err.println("Sao null tá");
//	    			return;
//    			}
//
//            String[] chuoiTach = chuoi.split(",");
//
//            DecimalFormat dcm = new  DecimalFormat("#,##0.0 đ");
//
//            double tongTien = Double.parseDouble(chuoiTach[3]);
//            double giamGia = Double.parseDouble(chuoiTach[4]);
//            double tienCoc = control.tinhTienCocTheoDSBan(chuoiTach[9]);
//            double tienThue = control.tinhThue(Double.parseDouble(chuoiTach[3]));
//
//            String[] dsBan = chuoiTach[9].split("_");
//
//            txtMaHoaDon.setText(chuoiTach[0]);
//            txtKhachHang.setText(chuoiTach[1]);
//            txtNhanVien.setText(chuoiTach[2]);
//            txtBanTra.setText(String.join(",", Arrays.copyOfRange(dsBan, 0, dsBan.length)));
//            txtPhuongThuc.setText(chuoiTach[5]);
//            txtNgayTao.setText(chuoiTach[6]);
//            txtTongTien.setText(dcm.format(tongTien));
//            txtGiamGia.setText(dcm.format(giamGia));
//            txtThue.setText(dcm.format(tienThue));
//            txtTienCoc.setText(dcm.format(tienCoc));
//
//            double tienTT = 0.0;
//            tienTT = control.tinhTienThanhToan(tongTien, giamGia, tienThue, tienCoc);
//            if(tienTT >= 0)
//            		txtTienTT.setText(dcm.format(tienTT));
//            else {
//            		txtTienTT.setText("Thối lại: " + dcm.format(-tienTT));
//            }
//
//            txtTrangThai.setText(chuoiTach[7]);
//
//
//            loadDanhSachMonAn(chuoiTach[0]);
//
//        });
//
//        vbox.getChildren().addAll(lblTitle, tableHoaDon);
//
//        ///
//
//
//        return vbox;
//    }
//
//    private void loadDanhSachMonAn(String maHoaDon) {
//	    	if (maHoaDon != null) {
//	            List<String> ds = control.dsThongTinMonAnTheoMaHD(maHoaDon);
//	            dsThongTinMonAn = FXCollections.observableArrayList(ds);
//	            tableMonAn.getItems().clear();
//	            tableMonAn.setItems(dsThongTinMonAn);
//	        }
//	    }
//
//
//    private SVGPath createSvgIcon(double size, double viewBox, String mau, String pathData) {
//		SVGPath svg = new SVGPath();
//		svg.setContent(pathData);
//		svg.setScaleX(size / viewBox);
//		svg.setScaleY(size / viewBox);
//		svg.setStyle("-fx-stroke: " + mau + "; -fx-fill: transparent;");
//		return svg;
//	}
//
//    private HBox createInputField(String labelText, TextField textField, boolean isReadOnly) {
//        HBox row = new HBox(15);
//        row.setAlignment(Pos.CENTER_LEFT);
//
//        Label label = new Label(labelText);
//        label.setMinWidth(110);
//        label.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
//        label.setStyle("-fx-text-fill: #34495e;");
//
//        textField.setPrefWidth(220);
//        textField.setEditable(!isReadOnly);
//
//        if (isReadOnly) {
//            textField.setStyle(
//                    "-fx-background-color: #ECF0F1; " +
//                            "-fx-border-color: #bdc3c7; " +
//                            "-fx-border-radius: 6; " +
//                            "-fx-background-radius: 6; " +
//                            "-fx-padding: 10; " +
//                            "-fx-font-size: 15px; " +
//                            "-fx-text-fill: #7f8c8d;"
//            );
//        } else {
//            textField.setStyle(
//                    "-fx-background-color: white; " +
//                            "-fx-border-color: #3498db; " +
//                            "-fx-border-width: 1.5; " +
//                            "-fx-border-radius: 6; " +
//                            "-fx-background-radius: 6; " +
//                            "-fx-padding: 10; " +
//                            "-fx-font-size: 15px;"
//            );
//
//            textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
//                if (newVal) {
//                    textField.setStyle(
//                            "-fx-background-color: white; " +
//                                    "-fx-border-color: #667eea; " +
//                                    "-fx-border-width: 1.5; " +
//                                    "-fx-border-radius: 6; " +
//                                    "-fx-background-radius: 6; " +
//                                    "-fx-padding: 10; " +
//                                    "-fx-font-size: 13px; " +
//                                    "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.3), 8, 0, 0, 2);"
//                    );
//                } else {
//                    textField.setStyle(
//                            "-fx-background-color: white; " +
//                                    "-fx-border-color: #3498db; " +
//                                    "-fx-border-width: 1.5; " +
//                                    "-fx-border-radius: 6; " +
//                                    "-fx-background-radius: 6; " +
//                                    "-fx-padding: 10; " +
//                                    "-fx-font-size: 13px;"
//                    );
//                }
//            });
//        }
//
//        row.getChildren().addAll(label, textField);
//        return row;
//    }
//
//    public void showAlert(Alert.AlertType type, String title, String message) {
//		Alert alert = new Alert(type);
//		alert.setTitle(title);
//		alert.setHeaderText(null);
//		alert.setContentText(message);
//		alert.showAndWait();
//	}
//
//    public void taoMoiTimKiem() {
//    		flag = true;
//
//    		txtTimKiem.setText("");
//    		cboTrangThai.setValue("Tất cả");
//    		ngayLoc.setValue(null);
//    		tableHoaDon.setItems(dsHoaDon);
//    		tableMonAn.getItems().clear();
//    		txtMaHoaDon.setText("");
//    		txtKhachHang.setText("");
//    		txtNhanVien.setText("");
//    		txtNgayTao.setText("");
//    		txtPhuongThuc.setText("");
//    		txtTongTien.setText("");
//    		txtThue.setText("");
//    		txtGiamGia.setText("");
//    		txtTienTT.setText("");
//    		txtTienCoc.setText("");
//    		txtTrangThai.setText("");
//    		txtBanTra.setText("");
//
//    		flag = false;
//    }
//
//    private void locDanhSach(String maTim, LocalDate ngayChon, String trangThai) {
//    		if(flag == true) {
//    			return;
//    		}
//
//	    	ObservableList<String> dsLoc = control.locHoaDon(dsHoaDon, maTim, ngayChon, trangThai);
//
//	    	if(dsLoc == null || dsLoc.isEmpty()) {
//	    		showAlert(AlertType.INFORMATION, "Không tìm thấy", "Không tìm thấy hóa đơn phù hợp !!");
//	    	}
//
//	    	tableHoaDon.setItems(dsLoc);
//    }
//
//
//    public void inHoaDon(Stage owner, List<String> danhSach, String hoaDon) {
//		Printer printer = Printer.getDefaultPrinter();
//		PrinterJob job = PrinterJob.createPrinterJob(printer);
//		if (job == null || !job.showPrintDialog(owner)) {
//			System.out.println("Hủy in.");
//			return;
//		}
//
//		PageLayout layout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
//		double maxHeight = layout.getPrintableHeight();
//
//		int currentIndex = 0;
//		int sttGlobal = 1;
//		int pageNum = 1;
//		boolean hasMore = true;
//		while (hasMore) {
//			VBox pageBox = new VBox(10);
//			pageBox.setStyle("-fx-padding: 20; -fx-font-family: Arial; -fx-background-color: white;"); // Nền trắng cho
//																										// page in
//			pageBox.setAlignment(Pos.TOP_CENTER); // Căn giữa theo chiều dọc trên cùng
//
//			// Header chỉ trang đầu
//			if (pageNum == 1) {
//				pageBox.getChildren().add(taoHeader(1, 1, hoaDon)); // totalPages không biết trước, có thể để 1 hoặc tính trước
//			}
//
//			// Tính items cho trang này
//			int itemsPerPage = (pageNum == 1) ? 19 : 25;
//			int remaining = danhSach.size() - currentIndex;
//			int itemsThisPage = Math.min(itemsPerPage, remaining);
//
//
//			if (itemsThisPage > 0 && remaining >= 0) {
//				int from = currentIndex;
//				int to = currentIndex + itemsThisPage;
//				List<String> subList = danhSach.subList(from, to);
//
//				TableView<String> table = taoTable(subList, sttGlobal);
//				pageBox.getChildren().add(table);
//
//				// Cập nhật cho trang sau
//				currentIndex += itemsThisPage;
//				sttGlobal += itemsThisPage;
//			}
//
//			hasMore = remaining > itemsThisPage;
//
//			if((pageNum == 1 && (itemsThisPage > 10 && itemsThisPage <= 19)) || (pageNum > 1 && (itemsThisPage > 16 && itemsThisPage <= 25)))
//				hasMore = true;
//
//			pageBox.getChildren().add(taoFooter(!hasMore, hoaDon));
//
//			pageBox.setAlignment(Pos.CENTER);
//			pageBox.applyCss();
//			pageBox.layout();
//
/// /			// Scale nếu page cao quá (hiếm vì itemsPerPage fit)
/// /			double pageHeight = pageBox.getBoundsInLocal().getHeight();
/// /			if (pageHeight > maxHeight) {
/// /				double scale = maxHeight / pageHeight;
/// /				pageBox.setScaleX(scale);
/// /				pageBox.setScaleY(scale);
/// /			}
//
//
//
//			job.printPage(layout, pageBox);
//			pageNum++;
//		}
//
//		if(job.endJob()) {
//			showAlert(AlertType.INFORMATION, "In Hóa Đơn", "In Hóa Đơn Thành Công !");
//		}
//	}
//
//	private TableView<String> taoTable(List<String> ds, int page) {
//		TableView<String> table = new TableView<>();
//		table.setItems(FXCollections.observableArrayList(ds));
//
//		// Cột STT
//		TableColumn<String, Void> colSTT = new TableColumn<>("STT");
//		colSTT.setPrefWidth(30);
//		colSTT.setSortable(false);
//		colSTT.setCellFactory(col -> new TableCell<String, Void>() {
//			@Override
//			protected void updateItem(Void item, boolean empty) {
//				super.updateItem(item, empty);
//				setText(empty ? null : String.valueOf(getIndex() + page));
//				setAlignment(Pos.CENTER);
//			}
//		});
//
//		// Cột tên món
//		TableColumn<String, String> colTenMon = new TableColumn<>("Tên món");
//
//		// Gán dữ liệu từ thuộc tính "tenMonAn" trong class MonAn
//		colTenMon.setCellValueFactory(cellData -> {
//			String tenMon = cellData.getValue().split(",")[0];
//			return new SimpleStringProperty(tenMon);
//		});
//
//		colTenMon.setCellFactory(tc -> new TableCell<String, String>() {
//			@Override
//			protected void updateItem(String item, boolean empty) {
//				super.updateItem(item, empty);
//
//				if (empty || item == null) {
//					setText(null);
//					setStyle("");
//				} else {
//					setText(item);
//					setAlignment(Pos.CENTER_LEFT);
//				}
//			}
//		});
//
//		colTenMon.setPrefWidth(145);
//
//		// Cột số lượng
//		TableColumn<String, Integer> colSoLuong = new TableColumn<>("SL");
//
//		colSoLuong.setCellValueFactory(cellData -> {
//			int soLuong = Integer.parseInt(cellData.getValue().split(",")[1]);
//			return new SimpleIntegerProperty(soLuong).asObject();
//		});
//
//		colSoLuong.setCellFactory(tc -> new TableCell<String, Integer>() {
//			@Override
//			protected void updateItem(Integer item, boolean empty) {
//				super.updateItem(item, empty);
//				if (empty || item == null) {
//					setText(null);
//					setStyle("");
//				} else {
//					setText(String.valueOf(item));
//					setAlignment(Pos.CENTER);
//					setStyle("-fx-font-size: 13px;");
//				}
//			}
//		});
//
//		colSoLuong.setPrefWidth(40);
//
//		// Cột giá
//		TableColumn<String, Double> colGia = new TableColumn<>("Giá");
//		colGia.setCellValueFactory(cellData -> {
//			double giaTien = Double.parseDouble(cellData.getValue().split(",")[2]);
//			return new SimpleDoubleProperty(giaTien).asObject();
//		});
//		colGia.setPrefWidth(120);
//		colGia.setCellFactory(tc -> new TableCell<String, Double>() {
//			@Override
//			protected void updateItem(Double item, boolean empty) {
//				super.updateItem(item, empty);
//				setText(empty || item == null ? null : String.format("%,.0fđ", item));
//				setAlignment(Pos.CENTER);
//			}
//
//		});
//
//		// Cột tổng tiền
//		TableColumn<String, Double> colTong = new TableColumn<>("Tổng tiền");
//		colTong.setPrefWidth(120);
//		colTong.setCellValueFactory(cellData -> {
//			double tongTien = Double.parseDouble(cellData.getValue().split(",")[3]);
//			return new SimpleDoubleProperty(tongTien).asObject();
//		});
//		colTong.setCellFactory(tc -> new TableCell<String, Double>() {
//			@Override
//			protected void updateItem(Double item, boolean empty) {
//				super.updateItem(item, empty);
//				setText(empty || item == null ? null : String.format("%,.0fđ", item));
//				setAlignment(Pos.CENTER);
//			}
//
//		});
//
//		table.getStylesheets().add(getClass().getResource("/css/tableprint.css").toExternalForm());
//
//		table.getColumns().addAll(colSTT, colTenMon, colSoLuong, colGia, colTong);
//		table.setPrefHeight(ds.size() * 25 + 30);
//		return table;
//	}
//
//	private VBox taoHeader(int page, int totalPages, String hoaDon) {
//		VBox box = new VBox(10);
//		box.setMaxWidth(480);
//
//		Label lblTenNhaHang = new Label("2BT RESTAURANT");
//		lblTenNhaHang.setStyle("""
//				-fx-font-size: 20px;
//				-fx-font-weight: bold;
//				""");
//		Label lblDiaChi = new Label("100 Lê Đức Thọ, P.16, Gò Vấp, TP Hồ Chí Minh");
//		Label lblSdt = new Label("0987 654 321");
//
//		Label lblHoaDon = new Label("HÓA ĐƠN THANH TOÁN");
//		lblHoaDon.setStyle("""
//				-fx-font-size: 20px;
//				-fx-font-weight: bold;
//				""");
//
//		HBox hbox1 = new HBox();
//		Region spacer1 = new Region();
//		hbox1.setMaxWidth(460);
//		HBox.setHgrow(spacer1, Priority.ALWAYS);
//
//		Label lblNgay = new Label("Ngày: " + hoaDon.split(",")[6]);
//		Label lblMaHoaDon = new Label("Mã hóa đơn: " + hoaDon.split(",")[0]);
//
//		hbox1.getChildren().addAll(lblNgay, spacer1, lblMaHoaDon);
//
//		Label lblMaBan = new Label("Bàn: " + String.join(",", Arrays.copyOfRange(hoaDon.split(","), 10, hoaDon.split(",").length)));
//		HBox hbox2 = new HBox(lblMaBan);
//		hbox2.setAlignment(Pos.CENTER_LEFT);
//		Label lblThuNgan = new Label("Thu ngân: " + hoaDon.split(",")[2]);
//		HBox hbox3 = new HBox(lblThuNgan);
//		hbox3.setAlignment(Pos.CENTER_LEFT);
//
//		box.setAlignment(Pos.CENTER);
//
//		box.setMargin(hbox1, new Insets(0, 15, 0, 0));
//		box.getChildren().addAll(lblTenNhaHang, lblDiaChi, lblSdt, lblHoaDon, hbox1, hbox2, hbox3, new Separator());
//		return box;
//		}
//
//	private VBox taoFooter(boolean lastPage, String hoaDon) {
//		VBox box = new VBox(5);
//		box.setMaxWidth(480);
//		DecimalFormat dcm = new DecimalFormat("#,##0.0 VND");
//
//		String[] hoaDonSplit = hoaDon.split(",");
//
//		String tongTienString = hoaDonSplit[3];
//		String giamGiaString = hoaDonSplit[4];
//		String loaiBan = hoaDonSplit[9];
//		String ghiChu = hoaDonSplit[8];
//
//		double tongTien = Double.parseDouble(tongTienString);
//		double giamGia = Double.parseDouble(giamGiaString);
//		double thue = control.tinhThue(tongTien);
//
//		if (lastPage) {
//			HBox hbox1 = new HBox();
//			Region spacer1 = new Region();
//			hbox1.setMaxWidth(460);
//			HBox.setHgrow(spacer1, Priority.ALWAYS);
//
//			Label lblTamTinh = new Label("Tạm tính: ");
//			Label lblTamTinhText = new Label(dcm.format(tongTien));
//
//			hbox1.getChildren().addAll(lblTamTinh, spacer1, lblTamTinhText);
//
//			HBox hbox2 = new HBox();
//			Region spacer2 = new Region();
//			hbox2.setMaxWidth(460);
//			HBox.setHgrow(spacer2, Priority.ALWAYS);
//
//			Label lblThue = new Label("Thuế VAT: ");
//			Label lblThueText = new Label(dcm.format(thue));
//
//			hbox2.getChildren().addAll(lblThue, spacer2, lblThueText);
//
//			HBox hbox3 = new HBox();
//			Region spacer3 = new Region();
//			hbox3.setMaxWidth(460);
//			HBox.setHgrow(spacer3, Priority.ALWAYS);
//
//			Label lblGiamGia = new Label("Giảm giá: ");
//			Label lblGiamGiaText = new Label(dcm.format(giamGia));
//
//			hbox3.getChildren().addAll(lblGiamGia, spacer3, lblGiamGiaText);
//
//			Separator line = new Separator();
//			line.setPrefWidth(480);
//			line.setStyle("-fx-background-color: black");
//
//			HBox hbox4 = new HBox();
//			Region spacer4 = new Region();
//			hbox4.setMaxWidth(460);
//			HBox.setHgrow(spacer4, Priority.ALWAYS);
//
//			Label lblTongTien = new Label("Tổng tiền: ");
//			Label lblTongTienText = new Label(dcm.format(tongTien + thue - giamGia));
//
//			lblTongTien.setStyle("""
//					-fx-font-size: 20px;
//					-fx-font-weight: bold;
//					""");
//			lblTongTienText.setStyle("""
//					-fx-font-size: 20px;
//					-fx-font-weight: bold;
//					""");
//
//			hbox4.getChildren().addAll(lblTongTien, spacer4, lblTongTienText);
//
//			HBox hbox5 = new HBox();
//			hbox5.setMaxWidth(460);
//
//			Separator line2 = new Separator();
//			line2.setPrefWidth(480);
//			line2.setStyle("-fx-background-color: black");
//
//			Label lblCamOn = new Label("Cảm Ơn Quý Khách - Hẹn Gặp Lại ");
//			lblCamOn.setStyle("""
//					-fx-font-style: italic;
//					-fx-font-size: 20px;
//					""");
//
//			hbox5.getChildren().addAll(lblCamOn);
//			hbox5.setAlignment(Pos.CENTER);
//
//			box.getChildren().addAll(hbox1, hbox2, hbox3, line, hbox4,  line2, hbox5);
//
//		}
//		box.setStyle("-fx-padding: 10 0 0 0;");
//		return box;
//	}
//
//	public void thucHienIn() {
//	    String maHoaDon = txtMaHoaDon.getText();
//
//	    if (maHoaDon == null || maHoaDon.isBlank()) {
//	        showAlert(AlertType.ERROR, "Lỗi", "Vui lòng chọn một hóa đơn trước khi in");
//	        return;
//	    }
//
//	    List<String> dsMon = new ArrayList<>(dsThongTinMonAn);
//	    String hoaDonChuoi = tableHoaDon.getSelectionModel().getSelectedItem();
//
//	    // Gọi xem trước
//	    xemTruocHoaDonIn((Stage) this.getScene().getWindow(), dsMon, hoaDonChuoi);
//	}
//
//	public void xemTruocHoaDonIn(Stage owner, List<String> danhSach, String hoaDon) {
//
//	    // Tạo Stage xem trước
//	    Stage previewStage = new Stage();
//	    previewStage.initOwner(owner);
//	    previewStage.initModality(Modality.APPLICATION_MODAL);
//	    previewStage.setTitle("Xem trước hóa đơn");
//
//	    VBox root = new VBox(20);
//	    root.setPadding(new Insets(20));
//	    root.setAlignment(Pos.CENTER);
//
//	    // Tạo nội dung giống hệt trang in
//	    ScrollPane scroll = new ScrollPane();
//	    scroll.setFitToWidth(true);
//
//	    VBox previewContent = new VBox(20);
//	    previewContent.setAlignment(Pos.TOP_CENTER);
//	    previewContent.setStyle("-fx-background-color: white; -fx-padding: 20;");
//
//	    // Header
//	    previewContent.getChildren().add(taoHeader(1, 1, hoaDon));
//
//	    // Bảng món ăn
//	    TableView<String> table = new TableView<String>();
//
//		table.setItems(FXCollections.observableArrayList(danhSach));
//
//		// Cột STT
//		TableColumn<String, Void> colSTT = new TableColumn<>("STT");
//		colSTT.setPrefWidth(30);
//		colSTT.setSortable(false);
//		colSTT.setCellFactory(col -> new TableCell<String, Void>() {
//			@Override
//			protected void updateItem(Void item, boolean empty) {
//				super.updateItem(item, empty);
//				setText(empty ? null : String.valueOf(getIndex() + 1));
//				setAlignment(Pos.CENTER);
//			}
//		});
//
//		// Cột tên món
//		TableColumn<String, String> colTenMon = new TableColumn<>("Tên món");
//
//		// Gán dữ liệu từ thuộc tính "tenMonAn" trong class MonAn
//		colTenMon.setCellValueFactory(cellData -> {
//			String tenMon = cellData.getValue().split(",")[0];
//			return new SimpleStringProperty(tenMon);
//		});
//
//		colTenMon.setCellFactory(tc -> new TableCell<String, String>() {
//			@Override
//			protected void updateItem(String item, boolean empty) {
//				super.updateItem(item, empty);
//
//				if (empty || item == null) {
//					setText(null);
//					setStyle("");
//				} else {
//					setText(item);
//					setAlignment(Pos.CENTER_LEFT);
//				}
//			}
//		});
//
//		colTenMon.setPrefWidth(200);
//
//		// Cột số lượng
//		TableColumn<String, Integer> colSoLuong = new TableColumn<>("SL");
//
//		colSoLuong.setCellValueFactory(cellData -> {
//			int soLuong = Integer.parseInt(cellData.getValue().split(",")[1]);
//			return new SimpleIntegerProperty(soLuong).asObject();
//		});
//
//		colSoLuong.setCellFactory(tc -> new TableCell<String, Integer>() {
//			@Override
//			protected void updateItem(Integer item, boolean empty) {
//				super.updateItem(item, empty);
//				if (empty || item == null) {
//					setText(null);
//					setStyle("");
//				} else {
//					setText(String.valueOf(item));
//					setAlignment(Pos.CENTER);
//					setStyle("-fx-font-size: 13px;");
//				}
//			}
//		});
//
//		colSoLuong.setPrefWidth(40);
//
//		// Cột giá
//		TableColumn<String, Double> colGia = new TableColumn<>("Giá");
//		colGia.setCellValueFactory(cellData -> {
//			double giaTien = Double.parseDouble(cellData.getValue().split(",")[2]);
//			return new SimpleDoubleProperty(giaTien).asObject();
//		});
//		colGia.setPrefWidth(120);
//		colGia.setCellFactory(tc -> new TableCell<String, Double>() {
//			@Override
//			protected void updateItem(Double item, boolean empty) {
//				super.updateItem(item, empty);
//				setText(empty || item == null ? null : String.format("%,.0fđ", item));
//				setAlignment(Pos.CENTER);
//			}
//
//		});
//
//		// Cột tổng tiền
//		TableColumn<String, Double> colTong = new TableColumn<>("Tổng tiền");
//		colTong.setPrefWidth(120);
//		colTong.setCellValueFactory(cellData -> {
//			double tongTien = Double.parseDouble(cellData.getValue().split(",")[3]);
//			return new SimpleDoubleProperty(tongTien).asObject();
//		});
//		colTong.setCellFactory(tc -> new TableCell<String, Double>() {
//			@Override
//			protected void updateItem(Double item, boolean empty) {
//				super.updateItem(item, empty);
//				setText(empty || item == null ? null : String.format("%,.0fđ", item));
//				setAlignment(Pos.CENTER);
//			}
//
//		});
//
//		table.getColumns().addAll(colSTT, colTenMon, colSoLuong, colGia, colTong);
//
//		int rowCount = table.getItems().size();
//		double rowHeight = 26;
//		double headerHeight = 28;
//		table.setPrefHeight(rowCount * rowHeight + headerHeight - 5);
//
//	    previewContent.getChildren().add(table);
//
//	    // Footer
//	    previewContent.getChildren().add(taoFooter(true, hoaDon));
//
//	    scroll.setContent(previewContent);
//
//	    // Nút Quay lại + In
//	    HBox buttons = new HBox(20);
//	    buttons.setAlignment(Pos.CENTER);
//
//	    Button btnBack = new Button("Quay lại");
//	    btnBack.setPrefWidth(120);
//	    btnBack.getStyleClass().add("btn-QuayLai");
//
//	    Button btnPrint = new Button("In hóa đơn");
//	    btnPrint.setPrefWidth(120);
//	    btnPrint.getStyleClass().add("btn-In");
//
//	    buttons.getChildren().addAll(btnBack, btnPrint);
//
//	    root.getChildren().addAll(scroll, buttons);
//
//	    Scene scene = new Scene(root, 600, 600);
//	    scene.getStylesheets().add(getClass().getResource("/css/qlhd.css").toExternalForm());
//	    previewStage.setScene(scene);
//
//	    // ----- SỰ KIỆN NÚT -----
//
//	    btnBack.setOnAction(e -> previewStage.close());
//
//	    btnPrint.setOnAction(e -> {
//	        previewStage.close();
//	        inHoaDon(owner, danhSach, hoaDon);
//	    });
//
//	    previewStage.show();
//	}
//
//
//}
//
//
package gui;

import ctrl.QLHD_Ctrl;
import dao.PhieuDatBan_DAO;
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
            String[] arr = d.getValue().split(",")[9].split("_");
            return new SimpleStringProperty(String.join(", ", arr));
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
                } else {
                    lbl.setStyle(lbl.getStyle() + "-fx-background-color: #f8d7da; -fx-text-fill: #721c24;");
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
        String[] parts = data.split(",");
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