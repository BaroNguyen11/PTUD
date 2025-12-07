
package gui;



import java.sql.Date;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import ctrl.ThanhToan_Ctrl;
import dao.HoaDon_DAO;
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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.print.*;

import java.util.Map;
import java.util.Optional;

public class Gui_ThanhToan extends BorderPane {
    private ThanhToan_Ctrl control = new ThanhToan_Ctrl();
    private TextField txtTienNhan = new TextField();
    private TextField txtTienThua = new TextField();
    private TextField txtTongTien;
    private ToggleButton btnTienMat = new ToggleButton();
    private ToggleButton btnMa = new ToggleButton();
    private Button btnPhim1;
    private Button btnPhim3;
    private Button btnPhim2;
    private Button btnPhim4;
    private Button btnPhim6;
    private Button btnPhim5;
    private Button btnPhim8;
    private Button btnPhim7;
    private Button btnPhim9;
    private Button btnPhim0;
    private Button btnPhimC;
    private Button btnPhim00;
    private Button btnPhim000;
    private Button btnPhimXoaMot;
    private Button btn50;
    private Button btn200;
    private Button btn100;
    private Button btn500;
    private Button btnThanhToan = new Button();
    private Button btnQuayLai = new Button();
    private Button btnIn = new Button();
    private final Map<KeyCombination, Runnable> shortcuts = new HashMap<>();
    private Button btnNhapNhanh1 = new Button();
    private Button btnNhapNhanh2 = new Button();
    private Button btnNhapNhanh3 = new Button();
    private Button btnNhapNhanh4 = new Button();
    private Button btnNhapNhanh5 = new Button();
    private Button btnNhapNhanh6 = new Button();
    private Label lblTongTien;
    private GridPane gridNhapNhanh;
    private double tienThanhToan;
    private Button btnPhimEnter;
    private NhanVien nv;
    private TextField txtGiamGia;
    private TextField txtThue;
    private TextField txtTamTinh;
    private Label lblTongTienQR;
    private ComboBox<KhuyenMai> cboKM;
	private TextField txtTienCoc;
	private String dsBan;

    public Gui_ThanhToan(NhanVien nv, String maHD) {
        this.nv = nv;
        // Root chính
        this.setStyle("-fx-background-color: white");

        
        BorderPane manHienThiBan = taoManHinhThanhToan(maHD);
        
        this.setCenter(manHienThiBan);
        this.getStylesheets().add(getClass().getResource("/css/qlkm.css").toExternalForm());
        this.getStylesheets().add(getClass().getResource("/css/thanhtoan.css").toExternalForm());

    }
    
    private BorderPane taoManHinhThanhToan(String maHD) {
        // Root all
        BorderPane rootAll = new BorderPane();

        List<PhieuDatBan> dsPhieu = null;
        
        try {
        		dsPhieu = control.layDanhSachPhieuBangMaHD(maHD);
        }catch (Exception e) {
			e.printStackTrace();
		}
        
        // Tạo phần trái
        VBox vboxPhanTrai = taoPhanTrai(dsPhieu);
        rootAll.setLeft(vboxPhanTrai);

        // Tạo phần phải
        VBox vboxPhanPhai = taoPhanPhai(dsPhieu);
        rootAll.setRight(vboxPhanPhai);

//        //Hàng f
//        KeyCombination f9 = new KeyCodeCombination(KeyCode.F9);
//        KeyCombination f10 = new KeyCodeCombination(KeyCode.F10);
//        KeyCombination f11 = new KeyCodeCombination(KeyCode.F11);
//        KeyCombination f12 = new KeyCodeCombination(KeyCode.F12);
//        KeyCombination f8 = new KeyCodeCombination(KeyCode.F8);
//        KeyCombination f3 = new KeyCodeCombination(KeyCode.F3);
//
//
//        //Put do
//        shortcuts.put(f12, () -> btnThanhToan.fire());
//        shortcuts.put(f11, () -> btnQuayLai.fire());
//        shortcuts.put(f10, () -> btnMa.fire());
//        shortcuts.put(f9, () -> btnTienMat.fire());
//        shortcuts.put(f8, () -> btnIn.fire());
//        shortcuts.put(f3, () -> txtTienNhan.requestFocus());
//
//        this.getScene().getAccelerators().putAll(shortcuts);

        return rootAll;
    }

    private VBox taoPhanTrai(List<PhieuDatBan> dsPhieu) {
        // VBox
        VBox vboxAll = new VBox(5);

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

        
        String maHD = "";
        
        if(dsPhieu == null || dsPhieu.isEmpty()) {
        		showAlert(AlertType.ERROR, "Lỗi thanh toán", "Không tìm thấy danh sách phiếu");
        }else {
        		maHD = dsPhieu.get(0).getHoaDon().getMaHoaDon();
        }
        
        
        dsBan = "";
        for(PhieuDatBan phieu : dsPhieu) {
        		dsBan += phieu.getBan().getMaBan();
        		if(!(phieu == dsPhieu.get(dsPhieu.size() - 1))) {
        			dsBan += ", ";
        		}
        }
        
        Label lblMaBan = new Label(dsBan);
        HBox hbox1 = new HBox(5);

        Label lblDanhSach = new Label("Danh sách món sử dụng");
        lblSetStyle(lblDanhSach, "#667eea");
        lblDanhSach.setGraphic(createSvgIcon(20, 14, "#667eea",
                "M7 3h0a6.5 6.5 0 0 1 6.5 6.5v0a1 1 0 0 1-1 1h-11a1 1 0 0 1-1-1v0A6.5 6.5 0 0 1 7 3Zm0 0V1.5m-6.5 11h13"));
        lblDanhSach.setContentDisplay(ContentDisplay.LEFT);
        lblDanhSach.setGraphicTextGap(10);

        List<String> dsChiTietRaw = control.layDanhSachCTHD(maHD);
        ObservableList<String> dsChiTiet = FXCollections.observableArrayList(dsChiTietRaw);
        TableView<String> tableMon = new TableView<>(dsChiTiet);
        tableMon.setPrefHeight(195);

        Label lblTamTinh = new Label("Tạm tính:");
        txtTamTinh = new TextField();
        HBox hbox2 = new HBox();
        Label lblThue = new Label("Thuế VAT:");
        txtThue = new TextField();
        HBox hbox3 = new HBox();
        Label lblGiamGia = new Label("Giảm giá:");
        txtGiamGia = new TextField();
        HBox hbox4 = new HBox();
        Label lblTienCoc = new Label("Tiền đã cọc:");
        txtTienCoc = new TextField();
        HBox hbox5 = new HBox();
        lblTongTien = new Label("Tổng tiền:");
        txtTongTien = new TextField();
        HBox hbox6 = new HBox();

        ///
//        iconVip.setFitHeight(30);
//        iconVip.setFitWidth(30);
        
        
        lblMaBan.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
        lblMaBan.setGraphic(createSvgIcon(24, 24, "black",
                "m9.2 17l-.975 2.425q-.125.275-.35.425t-.5.15q-.5 0-.787-.413t-.088-.862l1-2.475q.225-.575.725-.913T9.35 15H11v-4.025Q7.175 10.85 4.587 9.85T2 7.5q0-1.45 2.925-2.475T12 4q4.175 0 7.088 1.025T22 7.5q0 1.35-2.588 2.35T13 10.975V15h1.65q.6 0 1.113.338t.737.912l1 2.475q.1.225.063.45t-.163.413q-.125.187-.325.3t-.45.112q-.275 0-.5-.15t-.35-.425L14.8 17H9.2Z"));
        lblMaBan.setGraphicTextGap(10);
        lblMaBan.setContentDisplay(ContentDisplay.LEFT);

        hbox1.getChildren().addAll(lblMaBan);
        hbox1.setPadding(new Insets(0, 0, 0, 10));
        hbox1.setStyle(
                "-fx-background-color: white; -fx-effect: dropshadow(gaussian, #ddd9dd, 8, 0, 0, 2); -fx-background-radius: 5px");
        hbox1.setAlignment(Pos.CENTER_LEFT);

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

        colTenMon.setPrefWidth(250);

        // Cột số lượng
        TableColumn<String, Integer> colSoLuong = new TableColumn<>("Số lượng");

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

        colSoLuong.setPrefWidth(100);

        // Cột giá
        TableColumn<String, Double> colGia = new TableColumn<>("Giá");
        colGia.setCellValueFactory(cellData -> {
            double giaTien = Double.parseDouble(cellData.getValue().split(",")[2]);
            return new SimpleDoubleProperty(giaTien).asObject();
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
        colTong.setCellValueFactory(cellData -> {
            double tongTien = Double.parseDouble(cellData.getValue().split(",")[3]);
            return new SimpleDoubleProperty(tongTien).asObject();
        });
        colTong.setCellFactory(tc -> new TableCell<String, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                {
                    setPadding(new Insets(2, 5, 2, 5));
                }
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    Label lbl = new Label(String.format("%,.0fđ", item));
                    lbl.setStyle("""
							    -fx-background-color: white;
							    -fx-font-weight: bold;
							    -fx-text-fill: black;
							    -fx-font-size: 15px;
							""");
                    HBox hboxTongTien = new HBox();
                    hboxTongTien.setStyle(
                            "-fx-background-color: white; -fx-border-width: 0.5px; -fx-border-radius: 40; -fx-border-color: #3498db; -fx-background-radius: 40");
                    hboxTongTien.getChildren().add(lbl);
                    hboxTongTien.setAlignment(Pos.CENTER);
                    setGraphic(hboxTongTien);
                    setText(null);

                }
            }
        });

        tableMon.getColumns().addAll(colSTT, colTenMon, colSoLuong, colGia, colTong);

        ///
        lblTamTinh.getStyleClass().add("font-lblThanhToan");
        lblThue.getStyleClass().add("font-lblThanhToan");
        lblGiamGia.getStyleClass().add("font-lblThanhToan");
        lblTienCoc.getStyleClass().add("font-lblThanhToan");

        txtTamTinh.getStyleClass().add("font-txtThanhToan");
        txtThue.getStyleClass().add("font-txtThanhToan");
        txtGiamGia.getStyleClass().add("font-txtThanhToan");
        txtTienCoc.getStyleClass().add("font-txtThanhToan");
        txtTamTinh.setAlignment(Pos.CENTER_RIGHT);
        txtThue.setAlignment(Pos.CENTER_RIGHT);
        txtGiamGia.setAlignment(Pos.CENTER_RIGHT);
        txtTienCoc.setAlignment(Pos.CENTER_RIGHT);

        hbox2.getChildren().addAll(lblTamTinh, spacer1, txtTamTinh);
        hbox3.getChildren().addAll(lblThue, spacer2, txtThue);
        hbox4.getChildren().addAll(lblGiamGia, spacer3, txtGiamGia);
        hbox5.getChildren().addAll(lblTienCoc, spacer4, txtTienCoc);
        hbox6.getChildren().addAll(lblTongTien, spacer5, txtTongTien);
        hbox6.setAlignment(Pos.CENTER);
        hbox6.setStyle("-fx-background-color: #53687C;");

        hbox2.setPadding(new Insets(0, 5, 0, 5));
        hbox3.setPadding(new Insets(0, 5, 0, 5));
        hbox4.setPadding(new Insets(0, 5, 0, 5));
        hbox5.setPadding(new Insets(0, 5, 0, 5));
        hbox6.setPadding(new Insets(0, 5, 0, 5));

        hbox2.setAlignment(Pos.CENTER);
        hbox3.setAlignment(Pos.CENTER);
        hbox4.setAlignment(Pos.CENTER);
        hbox5.setAlignment(Pos.CENTER);
        hbox6.setAlignment(Pos.CENTER);

        //
        Label lblKhachHang = new Label("Khách hàng");
        lblSetStyle(lblKhachHang, "#667eea");
        lblKhachHang.setGraphic(createSvgIcon(24, 26, "#667eea",
                "M22.136 10h-5.282c-.472 0-.854-.443-.854-.989V8.99c0-.546.382-.989.854-.989h5.282c.476 0 .864.45.864 1s-.388 1-.864 1zm0 4h-5.282c-.472 0-.854-.443-.854-.989v-.022c0-.546.382-.989.854-.989h5.282c.476 0 .864.45.864 1s-.388 1-.864 1m.022 4h-3.327c-.46 0-.831-.443-.831-.989v-.022c0-.546.372-.989.831-.989h3.327c.463 0 .842.45.842 1s-.379 1-.842 1M10 13a4 4 0 1 1 0-8a4 4 0 0 1 0 8m-6.03 8.073c-.583 0-1.048-.518-.96-1.093A7.07 7.07 0 0 1 10 14a7.07 7.07 0 0 1 6.989 5.98c.09.575-.376 1.093-.958 1.093z"));
        lblKhachHang.setGraphicTextGap(10);
        lblKhachHang.setContentDisplay(ContentDisplay.LEFT);

        Label lblTenKhachHang = new Label("Tên khách hàng:");
        lblTenKhachHang.setStyle("-fx-font-size: 15px; -fx-font-weight: bold");

        TextField txtTenKhacHang = new TextField();
        txtTenKhacHang.setStyle("-fx-background-color: #ecf0f1; " + "-fx-border-color: #3498db; "
                + "-fx-border-width: 1.5; " + "-fx-border-radius: 6; " + "-fx-background-radius: 6; "
                + "-fx-padding: 10; " + "-fx-font-size: 13px;");
        txtTenKhacHang.setPrefWidth(400);
        txtTenKhacHang.setEditable(false);

        Label lblDiemTichLuy = new Label("Điểm tích lũy:");
        lblDiemTichLuy.setStyle("-fx-font-size: 15px; -fx-font-weight: bold");

        TextField txtDiemTichLuy = new TextField();
        txtDiemTichLuy.setStyle("-fx-background-color: #ecf0f1; " + "-fx-border-color: #3498db; "
                + "-fx-border-width: 1.5; " + "-fx-border-radius: 6; " + "-fx-background-radius: 6; "
                + "-fx-padding: 10; " + "-fx-font-size: 13px;");
        txtDiemTichLuy.setEditable(false);
        txtDiemTichLuy.setPrefWidth(400);

        Region spacerKh = new Region();
        Region spacerDiem = new Region();
        HBox.setHgrow(spacerDiem, Priority.ALWAYS);
        HBox.setHgrow(spacerKh, Priority.ALWAYS);

        HBox hboxTenKH = new HBox(lblTenKhachHang, spacerKh, txtTenKhacHang);
        HBox hboxDiem = new HBox(lblDiemTichLuy, spacerDiem, txtDiemTichLuy);
        hboxTenKH.setAlignment(Pos.CENTER);
        hboxDiem.setAlignment(Pos.CENTER);
        hboxTenKH.setPadding(new Insets(0, 10, 0, 10));
        hboxDiem.setPadding(new Insets(0, 10, 0, 10));

        // Khuyến mãi
        Label lblKMTieuDe = new Label("Áp dụng khuyến mãi");
        lblSetStyle(lblKMTieuDe, "#667eea");
        lblKMTieuDe.setGraphic(createSvgIcon(24, 26, "#667eea",
                "m9 14.25 6-6m4.5-3.493V21.75l-3.75-1.5-3.75 1.5-3.75-1.5-3.75 1.5V4.757c0-1.108.806-2.057 1.907-2.185a48.507 48.507 0 0 1 11.186 0c1.1.128 1.907 1.077 1.907 2.185ZM9.75 9h.008v.008H9.75V9Zm.375 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm4.125 4.5h.008v.008h-.008V13.5Zm.375 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Z"));
        lblKMTieuDe.setGraphicTextGap(10);
        lblKMTieuDe.setContentDisplay(ContentDisplay.LEFT);

        DecimalFormat format = new DecimalFormat("#,###.0 VND");
        
        double tongTien = control.tinhTongTien(dsChiTiet);
        	txtTamTinh.setText(format.format(tongTien));
        double tamTinh = parseVNDToDouble(txtTamTinh.getText());

        double thue = control.tinhThue(tongTien);
        txtThue.setText(format.format(thue));

        double tienCoc = control.tinhCocBangDanhSachPhieu(dsPhieu);
        txtTienCoc.setText(format.format(tienCoc));

        txtTamTinh.setEditable(false);
        txtThue.setEditable(false);
        txtGiamGia.setEditable(false);
        txtTienCoc.setEditable(false);
        txtTongTien.setEditable(false);

        txtTongTien.textProperty().addListener((observable, oldValue, newValue) -> {
            if(lblTongTienQR != null) {
                lblTongTienQR.setText("Tổng tiền: " + newValue);
            }
        });

        
        Label lblTenKM = new Label("Khuyến mãi:");
        lblTenKM.setStyle("-fx-font-size: 15px; -fx-font-weight: bold");
        List<KhuyenMai> dsKMApDung = control.layDanhSachKhuyenMai(dsPhieu.get(0).getHoaDon().getMaHoaDon(), tamTinh);
       
        

        cboKM = new ComboBox<KhuyenMai>(FXCollections.observableArrayList(dsKMApDung));
        cboKM.setPromptText("Không có khuyến mãi được áp dụng");
        cboKM.setStyle("-fx-background-color: white; " + "-fx-border-color: #3498db; " + "-fx-border-width: 1.5; "
                + "-fx-border-radius: 6; " + "-fx-background-radius: 6; " + "-fx-padding: 10; " + "-fx-font-size: 13px;"
                + "-fx-cursor: hand");

        tienThanhToan = 0.0;
        double giamGia = 0.0;

        if (dsKMApDung.isEmpty()) {
            tienThanhToan = control.tinhTienThanhToan(parseVNDToDouble(txtTamTinh.getText()), tienCoc, false, 0.0, thue,
                    0.0);
            txtGiamGia.setText("0 VND");
            Tooltip.install(cboKM, taoToolTipKhuyenMai(null));
        } else {
            KhuyenMai kmDau = dsKMApDung.get(0);
            cboKM.setValue(kmDau);
            tienThanhToan = control.tinhTienThanhToan(parseVNDToDouble(txtTamTinh.getText()), tienCoc,
                    kmDau.getGiamGiaPhanTram(), kmDau.getGiaTriGiam(), thue, kmDau.getGiaTriToiDa());
            giamGia = control.tinhTienGiamGia(kmDau.getGiaTriGiam(), kmDau.getGiamGiaPhanTram(),
                    parseVNDToDouble(txtTamTinh.getText()), kmDau.getGiaTriToiDa());
            Tooltip.install(cboKM, taoToolTipKhuyenMai(kmDau));
        }
        txtGiamGia.setText(format.format(giamGia));

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
                Tooltip.install(cboKM, taoToolTipKhuyenMai(selected));


            } else {
                txtGiamGia.setText("0 VND");
            }
            if (txtTienNhan != null) {
                txtTienNhan.setText("");
            }
        });

        txtTongTien.setAlignment(Pos.CENTER_RIGHT);

        Region spacerKM = new Region();
        HBox.setHgrow(spacerKM, Priority.ALWAYS);

        HBox hboxKM = new HBox(lblTenKM, spacerKM, cboKM);
        hboxKM.setAlignment(Pos.CENTER);
        hboxKM.setPadding(new Insets(0, 10, 0, 10));
        cboKM.setPrefWidth(400);
        cboKM.setMaxHeight(10);

        // Load dữ liệu từ database
        KhachHang khachHang = KhachHang_DAO.getKhachHangById(dsPhieu.get(0).getKhachHang().getMaKhachHang());

        txtTenKhacHang.setText(khachHang.getTenKhachHang());
        txtDiemTichLuy.setText(khachHang.getDiemTichLuy() + "");

        vboxAll.setPrefHeight(500);
        vboxAll.getChildren().addAll(hbox1, lblKhachHang, hboxTenKH, hboxDiem, lblKMTieuDe, hboxKM, lblDanhSach,
                tableMon, hbox2, hbox3, hbox4, hbox5, hbox6);
        vboxAll.setPadding(new Insets(20, 10, 20, 10));
        vboxAll.setStyle("-fx-border-width: 0 1 0 0; -fx-border-color: #908C8C");

        return vboxAll;
    }

    private VBox taoPhanPhai(List<PhieuDatBan> dsPhieu) {
        // VBox all
        VBox vboxAll = new VBox(5);

        // Khai báo biến

        Label lblPhuongThuc = new Label("Phương thức thanh toán");
        btnTienMat = new ToggleButton("Tiền mặt");
        btnTienMat.setTooltip(new Tooltip("Nhấn F9 để dùng nhanh"));
        btnMa = new ToggleButton("Mã QR");
        btnMa.setTooltip(new Tooltip("Nhấn F10 để dùng nhanh"));
        ToggleGroup btnGroup = new ToggleGroup();
        HBox hboxGroup = new HBox(10);
        btnTienMat.setToggleGroup(btnGroup);
        btnMa.setToggleGroup(btnGroup);
        Label lblTienNhan = new Label("Tiền nhận (F3):");
        Label lblTienThua = new Label("Tiền thừa:");
        Label lblNhapNhanh = new Label("Nhập nhanh");
        btnIn = new Button("In hóa đơn(F8)");

        btnIn.setOnAction(e -> {
            HoaDon hoaDon = control.layHoaDonBangMa(dsPhieu.get(0).getHoaDon().getMaHoaDon());
            String tamTinh = txtTamTinh.getText();
            String thueVAT = txtThue.getText();
            String giamGia = txtGiamGia.getText();
            String tongTien = txtTongTien.getText();
            
            String dsBan = "";
            for(PhieuDatBan phieu : dsPhieu) {
            		dsBan += phieu.getBan().getMaBan();
            		if(!(phieu == dsPhieu.get(dsPhieu.size() - 1))) {
            			dsBan += ", ";
            		}
            }

//            inHoaDon((Stage) this.getScene().getWindow(),control.layDanhSachCTHD(hoaDon.getMaHoaDon()), dsBan, hoaDon, tamTinh, thueVAT, giamGia, tongTien);
            thucHienIn(dsPhieu.get(0).getHoaDon().getMaHoaDon(), dsBan);
        });
        btnIn.setGraphic(createSvgIcon(15, 24, "white",
                "M6.72 13.829c-.24.03-.48.062-.72.096m.72-.096a42.415 42.415 0 0 1 10.56 0m-10.56 0L6.34 18m10.94-4.171c.24.03.48.062.72.096m-.72-.096L17.66 18m0 0 .229 2.523a1.125 1.125 0 0 1-1.12 1.227H7.231c-.662 0-1.18-.568-1.12-1.227L6.34 18m11.318 0h1.091A2.25 2.25 0 0 0 21 15.75V9.456c0-1.081-.768-2.015-1.837-2.175a48.055 48.055 0 0 0-1.913-.247M6.34 18H5.25A2.25 2.25 0 0 1 3 15.75V9.456c0-1.081.768-2.015 1.837-2.175a48.041 48.041 0 0 1 1.913-.247m10.5 0a48.536 48.536 0 0 0-10.5 0m10.5 0V3.375c0-.621-.504-1.125-1.125-1.125h-8.25c-.621 0-1.125.504-1.125 1.125v3.659M18 10.5h.008v.008H18V10.5Zm-3 0h.008v.008H15V10.5Z"));
        btnIn.setContentDisplay(ContentDisplay.LEFT);
        btnIn.setGraphicTextGap(10);
        btnIn.getStyleClass().add("btn-In");

        HBox hboxPTVaIn = new HBox();
        Region spacerIn = new Region();
        HBox.setHgrow(spacerIn, Priority.ALWAYS);
        hboxPTVaIn.getChildren().addAll(lblPhuongThuc, spacerIn, btnIn);

        gridNhapNhanh = new GridPane();

        HBox hboxPhimNhap = new HBox(5);

        GridPane gridPhim = new GridPane();

        btnPhim1 = new Button("1");
        btnPhim2 = new Button("2");
        btnPhim3 = new Button("3");
        btnPhim4 = new Button("4");
        btnPhim5 = new Button("5");
        btnPhim6 = new Button("6");
        btnPhim7 = new Button("7");
        btnPhim8 = new Button("8");
        btnPhim9 = new Button("9");
        btnPhim0 = new Button("0");
        btnPhimC = new Button("C");
        btnPhimEnter = new Button("Enter");
        btnPhim00 = new Button("00");
        btnPhim000 = new Button("000");
        btnPhimXoaMot = new Button();

        VBox vboxTien = new VBox(10);
        btn50 = new Button("50.000 VND");
        btn100 = new Button("100.000 VND");
        btn200 = new Button("200.000 VND");
        btn500 = new Button("500.000 VND");

        btnThanhToan = new Button("Thanh toán(F12)");
        btnQuayLai = new Button("Quay lại(F11)");
        HBox hboxButton = new HBox();

        ImageView maQR = new ImageView(new Image("/img/qr.png"));
        lblTongTienQR = new Label("Tổng tiền....");

        HBox hboxTienNhan = new HBox();
        HBox hboxTienThua = new HBox();

        // Cài đặt giao diện
        lblSetStyle(lblPhuongThuc, "#667eea");
        lblPhuongThuc.setGraphic(createSvgIcon(20, 16, "#667eea",
                "M2 5h20v15H2V5m18 13V7H4v11h16M17 8a2 2 0 0 0 2 2v5a2 2 0 0 0-2 2H7a2 2 0 0 0-2-2v-5a2 2 0 0 0 2-2h10m0 5v-1c0-1.1-.67-2-1.5-2s-1.5.9-1.5 2v1c0 1.1.67 2 1.5 2s1.5-.9 1.5-2m-1.5-2a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-.5.5a.5.5 0 0 1-.5-.5v-2a.5.5 0 0 1 .5-.5M13 13v-1c0-1.1-.67-2-1.5-2s-1.5.9-1.5 2v1c0 1.1.67 2 1.5 2s1.5-.9 1.5-2m-1.5-2a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-.5.5a.5.5 0 0 1-.5-.5v-2a.5.5 0 0 1 .5-.5M8 15h1v-5H8l-1 .5v1l1-.5v4Z"));
        lblPhuongThuc.setContentDisplay(ContentDisplay.LEFT);
        lblPhuongThuc.setGraphicTextGap(10);

        btnTienMat.getStyleClass().add("toggle-thanhToan");
        btnMa.getStyleClass().add("toggle-thanhToan");
        hboxGroup.getChildren().addAll(btnTienMat, btnMa);
        btnTienMat.setSelected(true);
        // chặn bỏ chọn hết
        btnGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                // nếu người dùng click lại để bỏ chọn, ta khôi phục cái cũ
                oldToggle.setSelected(true);
            }
        });

        lblTienNhan.setStyle("-fx-font-size: 15px; -fx-font-weight: bold");
        lblTienThua.setStyle("-fx-font-size: 15px; -fx-font-weight: bold");

        txtTienNhan.setStyle("-fx-background-color: white; " + "-fx-border-color: #3498db; " + "-fx-border-width: 1.5; "
                + "-fx-border-radius: 6; " + "-fx-background-radius: 6; " + "-fx-padding: 10; "
                + "-fx-font-size: 13px;");
        txtTienNhan.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                txtTienNhan.setStyle("-fx-background-color: white; " + "-fx-border-color: #667eea; "
                        + "-fx-border-width: 1.5; " + "-fx-border-radius: 6; " + "-fx-background-radius: 6; "
                        + "-fx-padding: 10; " + "-fx-font-size: 13px; "
                        + "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.3), 8, 0, 0, 2);");
            } else {
                txtTienNhan.setStyle("-fx-background-color: white; " + "-fx-border-color: #3498db; "
                        + "-fx-border-width: 1.5; " + "-fx-border-radius: 6; " + "-fx-background-radius: 6; "
                        + "-fx-padding: 10; " + "-fx-font-size: 13px;");
            }
        });

        txtTienNhan.setPrefWidth(300);
        txtTienNhan.setPromptText("Nhập tiền nhận từ khách ");
        txtTienThua.setPrefWidth(300);
        txtTienThua.setEditable(false);
        Region spacer1 = new Region();
        Region spacer2 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        hboxTienNhan.getChildren().addAll(lblTienNhan, spacer1, txtTienNhan);
        hboxTienThua.getChildren().addAll(lblTienThua, spacer2, txtTienThua);
        hboxTienNhan.setAlignment(Pos.CENTER);
        hboxTienThua.setAlignment(Pos.CENTER);

        lblNhapNhanh.setStyle("-fx-font-size: 15px; -fx-text-fill: #908C8C");

        gridNhapNhanh.setHgap(10);
        gridNhapNhanh.setVgap(5);
        gridNhapNhanh.setPadding(new Insets(10));

        btnNhapNhanh1.getStyleClass().add("textFieldNhapDuoc");
        btnNhapNhanh2.getStyleClass().add("textFieldNhapDuoc");
        btnNhapNhanh3.getStyleClass().add("textFieldNhapDuoc");
        btnNhapNhanh4.getStyleClass().add("textFieldNhapDuoc");
        btnNhapNhanh5.getStyleClass().add("textFieldNhapDuoc");
        btnNhapNhanh6.getStyleClass().add("textFieldNhapDuoc");

        btnNhapNhanh1.setOnAction(e -> {
            int giaTri = (int) parseDToDouble(btnNhapNhanh1.getText());
            txtTienNhan.setText(giaTri + "");
        });
        btnNhapNhanh2.setOnAction(e -> {
            int giaTri = (int) parseDToDouble(btnNhapNhanh2.getText());
            txtTienNhan.setText(giaTri + "");
        });
        btnNhapNhanh3.setOnAction(e -> {
            int giaTri = (int) parseDToDouble(btnNhapNhanh3.getText());
            txtTienNhan.setText(giaTri + "");
        });
        btnNhapNhanh4.setOnAction(e -> {
            int giaTri = (int) parseDToDouble(btnNhapNhanh4.getText());
            txtTienNhan.setText(giaTri + "");
        });
        btnNhapNhanh5.setOnAction(e -> {
            int giaTri = (int) parseDToDouble(btnNhapNhanh5.getText());
            txtTienNhan.setText(giaTri + "");
        });
        btnNhapNhanh6.setOnAction(e -> {
            int giaTri = (int) parseDToDouble(btnNhapNhanh6.getText());
            txtTienNhan.setText(giaTri + "");
        });

        btnNhapNhanh1.setPrefHeight(40);
        btnNhapNhanh1.setPrefWidth(150);

        btnNhapNhanh2.setPrefHeight(40);
        btnNhapNhanh2.setPrefWidth(150);

        btnNhapNhanh3.setPrefHeight(40);
        btnNhapNhanh3.setPrefWidth(150);

        btnNhapNhanh4.setPrefHeight(40);
        btnNhapNhanh4.setPrefWidth(150);

        btnNhapNhanh5.setPrefHeight(40);
        btnNhapNhanh5.setPrefWidth(150);

        btnNhapNhanh6.setPrefHeight(40);
        btnNhapNhanh6.setPrefWidth(150);

        if (tienThanhToan > 0) {
            gridNhapNhanh.add(btnNhapNhanh1, 0, 0);
            gridNhapNhanh.add(btnNhapNhanh2, 1, 0);
            gridNhapNhanh.add(btnNhapNhanh3, 2, 0);
            gridNhapNhanh.add(btnNhapNhanh4, 0, 1);
            gridNhapNhanh.add(btnNhapNhanh5, 1, 1);
            gridNhapNhanh.add(btnNhapNhanh6, 2, 1);
        } else {
            Label lblKhongCan = new Label("Không cần nhập");
            lblKhongCan.setStyle("""
					-fx-pref-width: 500px;
					-fx-pref-height: 200px;
					-fx-border-width: 1px;
					-fx-border-color: #3498db;
					-fx-border-radius: 10px;
					-fx-font-size: 20px;
					""");
            lblKhongCan.setAlignment(Pos.CENTER);
            gridNhapNhanh.add(lblKhongCan, 0, 0);
        }

        btnPhim0.getStyleClass().add("btn-banPhim");
        btnPhim1.getStyleClass().add("btn-banPhim");
        btnPhim2.getStyleClass().add("btn-banPhim");
        btnPhim3.getStyleClass().add("btn-banPhim");
        btnPhim4.getStyleClass().add("btn-banPhim");
        btnPhim5.getStyleClass().add("btn-banPhim");
        btnPhim6.getStyleClass().add("btn-banPhim");
        btnPhim7.getStyleClass().add("btn-banPhim");
        btnPhim8.getStyleClass().add("btn-banPhim");
        btnPhim9.getStyleClass().add("btn-banPhim");
        btnPhimC.getStyleClass().add("btn-banPhim");
        btnPhimEnter
                .setGraphic(createSvgIcon(24, 24, "white", "m7.49 12-3.75 3.75m0 0 3.75 3.75m-3.75-3.75h16.5V4.499"));
        btnPhimEnter.setGraphicTextGap(10);
        btnPhimEnter.setContentDisplay(ContentDisplay.LEFT);
        btnPhimXoaMot.getStyleClass().add("btn-banPhim");
        btnPhimXoaMot.setGraphic(createSvgIcon(30, 24, "#082744",
                "M12 9.75 14.25 12m0 0 2.25 2.25M14.25 12l2.25-2.25M14.25 12 12 14.25m-2.58 4.92-6.374-6.375a1.125 1.125 0 0 1 0-1.59L9.42 4.83c.21-.211.497-.33.795-.33H19.5a2.25 2.25 0 0 1 2.25 2.25v10.5a2.25 2.25 0 0 1-2.25 2.25h-9.284c-.298 0-.585-.119-.795-.33Z"));
        btnPhimXoaMot.setContentDisplay(ContentDisplay.CENTER);
        btnPhim00.getStyleClass().add("btnPhimDacBiet");
        btnPhim000.getStyleClass().add("btnPhimDacBiet");

        gridPhim.add(btnPhim7, 0, 0);
        gridPhim.add(btnPhim8, 1, 0);
        gridPhim.add(btnPhim9, 2, 0);
        gridPhim.add(btnPhimXoaMot, 3, 0);

        gridPhim.add(btnPhim4, 0, 1);
        gridPhim.add(btnPhim5, 1, 1);
        gridPhim.add(btnPhim6, 2, 1);
        gridPhim.add(btnPhim00, 3, 1);

        gridPhim.add(btnPhim1, 0, 2);
        gridPhim.add(btnPhim2, 1, 2);
        gridPhim.add(btnPhim3, 2, 2);
        gridPhim.add(btnPhim000, 3, 2);

        gridPhim.add(btnPhimC, 0, 3);
        gridPhim.add(btnPhim0, 1, 3);
        gridPhim.add(btnPhimEnter, 2, 3, 2, 1);

        gridPhim.setHgap(10);
        gridPhim.setVgap(10);
        gridPhim.setPadding(new Insets(10, 0, 0, 0));

        btn50.getStyleClass().add("btn-banPhimTien");
        btn100.getStyleClass().add("btn-banPhimTien");
        btn200.getStyleClass().add("btn-banPhimTien");
        btn500.getStyleClass().add("btn-banPhimTien");
        btnPhimEnter.getStyleClass().add("btn-banPhimTien");

        vboxTien.getChildren().addAll(btn50, btn100, btn200, btn500);
        vboxTien.setPadding(new Insets(10, 0, 0, 5));

        hboxPhimNhap.getChildren().addAll(gridPhim, vboxTien);
        hboxPhimNhap.setPadding(new Insets(10));
        hboxPhimNhap.setStyle("-fx-border-radius: 10; -fx-border-color: #908C8C; -fx-background-color: white");

        btnThanhToan.getStyleClass().add("btn-ThanhToan");
        btnQuayLai.getStyleClass().add("btn-QuayLai");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        hboxButton.getChildren().addAll(btnQuayLai, spacer, btnThanhToan);

        btnQuayLai.setOnAction(e -> {
            this.setCenter(new Gui_DanhSachBan(new Gui_TrangChu(nv)));
        });

        btnThanhToan.setOnAction(e -> {

            if (checkTienNhan()) {
                for(PhieuDatBan phieu : dsPhieu) {
                		String maHoaDon = dsPhieu.get(0).getHoaDon().getMaHoaDon();
                    String maPhieu = phieu.getMaPhieu();
                    String tongTien = txtTongTien.getText();
                    String phuongThuc = btnTienMat.isSelected() ? "Tiền mặt" : "Chuyển khoản";
                    KhuyenMai khuyenMai = cboKM.getSelectionModel().getSelectedItem();
                    if (thanhToan(tongTien, phuongThuc, maHoaDon, dsPhieu, khuyenMai, dsPhieu.get(0).getKhachHang().getMaKhachHang())) {
                        	btnQuayLai.fire();
                    }
                }

            }

        });

        txtTienNhan.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                txtTienThua.setText("");
                return;
            }

            String inputText = newValue.trim();
            DecimalFormat format = new DecimalFormat("#,##0");
            DecimalFormat formatTienThua = new DecimalFormat("#,##0 VND");

            try {
                // Parse: Loại . phân cách, giữ , nếu thập phân (nếu cần)
                String cleanInput = inputText.replaceAll("\\.", "");
                double tienNhan = Double.parseDouble(cleanInput);

                if (tienNhan <= 0) {
                    txtTienThua.setText("Tiền nhận phải lớn hơn 0");
                    txtTienThua.setStyle("-fx-background-color: #ecf0f1; " + "-fx-border-color: #3498db; "
                            + "-fx-border-width: 1.5; " + "-fx-border-radius: 6; " + "-fx-background-radius: 6; "
                            + "-fx-padding: 10; " + "-fx-font-size: 13px;" + "-fx-text-fill: red");
                    return;
                }

                double tongTien = parseVNDToDouble(txtTongTien.getText());

                double tienThua = tienNhan - tongTien;

                if (tienThua < 0) {
                    // Thiếu
                    double thieu = -tienThua;
                    txtTienThua.setText("Còn thiếu: " + formatTienThua.format(thieu));
                    txtTienThua.setStyle("-fx-background-color: #ecf0f1; " + "-fx-border-color: #3498db; "
                            + "-fx-border-width: 1.5; " + "-fx-border-radius: 6; " + "-fx-background-radius: 6; "
                            + "-fx-padding: 10; " + "-fx-font-size: 13px;" + "-fx-text-fill: red");
                } else {
                    // Dư
                    if (tienThua == 0) {
                        txtTienThua.setText("Đúng số tiền!");
                    } else {
                        txtTienThua.setText("Tiền dư: " + formatTienThua.format(tienThua));
                    }
                    txtTienThua.setStyle("-fx-background-color: #ecf0f1; " + "-fx-border-color: #3498db; "
                            + "-fx-border-width: 1.5; " + "-fx-border-radius: 6; " + "-fx-background-radius: 6; "
                            + "-fx-padding: 10; " + "-fx-font-size: 13px;" + "-fx-text-fill: green");
                }

                // Format txtTienNhan nếu cần (không set nếu đã format)
                String formattedNhan = format.format(tienNhan);

                // Điều kiện dừng
                if (!formattedNhan.equals(inputText)) {
                    txtTienNhan.setText(formattedNhan);
                }

            } catch (NumberFormatException e) {
                // Không phải số
                txtTienThua.setText("Tiền nhận phải là số");
                txtTienThua.setStyle("-fx-background-color: #ecf0f1; " + "-fx-border-color: #3498db; "
                        + "-fx-border-width: 1.5; " + "-fx-border-radius: 6; " + "-fx-background-radius: 6; "
                        + "-fx-padding: 10; " + "-fx-font-size: 13px;" + "-fx-text-fill: red");
            }
        });

        maQR.setFitHeight(300);
        maQR.setFitWidth(300);

        lblTongTienQR.setStyle("""
				-fx-font-size: 20px;
				-fx-font-weight: bold;
				""");
        lblTongTienQR.setText("Tổng tiền: " + txtTongTien.getText());

        // Pane cho từng phương thức
        VBox paneTienMat = new VBox(5);
        VBox paneMa = new VBox(20);
        paneTienMat.getChildren().addAll(hboxTienNhan, hboxTienThua, lblNhapNhanh, gridNhapNhanh, hboxPhimNhap);
        paneMa.getChildren().addAll(maQR, lblTongTienQR);
        paneMa.setAlignment(Pos.CENTER);

        // Ban đầu hiển thị Tiền mặt
        paneTienMat.setVisible(true);
        paneTienMat.setManaged(true);
        paneMa.setVisible(false);
        paneMa.setManaged(false);
        btnTienMat.setSelected(true);

        btnGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> obs, Toggle oldToggle, Toggle newToggle) {
                if (newToggle == btnTienMat) {
                    paneTienMat.setVisible(true);
                    paneTienMat.setManaged(true);
                    paneMa.setVisible(false);
                    paneMa.setManaged(false);
                } else if (newToggle == btnMa) {
                    paneTienMat.setVisible(false);
                    paneTienMat.setManaged(false);
                    paneMa.setVisible(true);
                    paneMa.setManaged(true);
                }
            }
        });

        thucHienNumpad(btnPhim1, btnPhim2, btnPhim3, btnPhim4, btnPhim5, btnPhim6, btnPhim7, btnPhim8, btnPhim9,
                btnPhim0, btnPhimC, btnPhimXoaMot, btnPhimEnter, btn50, btn100, btn200, btn500, btnPhim00, btnPhim000,
                txtTienNhan);

        vboxAll.getChildren().addAll(hboxPTVaIn, hboxGroup, paneTienMat, paneMa, hboxButton);
        vboxAll.setPrefWidth(550);
        vboxAll.setPadding(new Insets(5, 30, 10, 10));
        vboxAll.setMargin(gridNhapNhanh, new Insets(0, 0, 0, 10));

        return vboxAll;
    }

    private void lblSetStyle(Label label, String maMau) {
        label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-font-family: 'Segoe UI'; -fx-text-fill: "
                + maMau + "; -fx-border-width: 0 0 0 4; -fx-border-color:" + maMau + "; -fx-padding: 2 2 2 10");
    }

    private ImageView taoIcon(String link, int chieuCao, int chieuRong) {
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(link)));
        icon.setFitWidth(chieuRong);
        icon.setFitHeight(chieuCao);
        return icon;
    }

    private SVGPath createSvgIcon(double size, double viewBox, String mau, String pathData) {
        SVGPath svg = new SVGPath();
        svg.setContent(pathData);
        svg.setScaleX(size / viewBox);
        svg.setScaleY(size / viewBox);
        svg.setStyle("-fx-stroke: " + mau + "; -fx-fill: transparent;");
        return svg;
    }

    private void handleThanhToan() {
        btnThanhToan.fire();
    }

    private void safeClearAccelerators() {
        if (getScene() != null) {
            Platform.runLater(() -> getScene().getAccelerators().clear());
        }
    }

    public void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
                if (control.xuLiThanhToanTatCa( dsPhieu, maHoaDon, phuongThuc, giamGia, khuyenMai) && control.capNhatTichLuy(maKH, diemMoi)) {
            			showAlert(AlertType.INFORMATION, "Thanh Toán Thành Công", "Tổng tiền thanh toán: " + tongTien);
            			return true;
                }else {
                    showAlert(AlertType.ERROR, "Thất bại", "Thanh Toán Không Thành Công 1!");
                    return false;
                }
            } catch (Exception e) {
                showAlert(AlertType.ERROR, "Thất bại", "Thanh Toán Không Thành Công 2!");
                return false;
            }
        }
        return false;
    }

    public static double parseVNDToDouble(String vndString) {
        if (vndString == null || vndString.trim().isEmpty()) {
            return 0.0;
        }

        try {
            // Loại bỏ "VND" và trim
            String cleanString = vndString.replaceAll("VND", "").replaceAll("\\s+", "").trim();

            // Sử dụng NumberFormat Việt Nam để parse (handle . và , tự động)
            NumberFormat formatVN = NumberFormat.getInstance(new Locale("vi", "VN"));
            Number number = formatVN.parse(cleanString);
            return number.doubleValue();
        } catch (Exception e) {
            System.err.println("Lỗi parse VND string: " + vndString + " - " + e.getMessage());
            return 0.0;
        }
    }

    public void loadTienThanhToan(double tienThanhToan, TextField txtTienThanhToan, Label lblTienThanhToan,
                                  TextField txtTienNhan, TextField txtTienThua, double tienCoc) {
        DecimalFormat format1 = new DecimalFormat("#,##0.0 đ");
        DecimalFormat format2 = new DecimalFormat("#,##0.0 VND");

        if (tienThanhToan > 0) {
            lblTienThanhToan.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: white");
            txtTongTien.setStyle(
                    "-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: transparent");
            txtTienThua.setStyle("-fx-background-color: #ecf0f1; " + "-fx-border-color: #3498db; "
                    + "-fx-border-width: 1.5; " + "-fx-border-radius: 6; " + "-fx-background-radius: 6; "
                    + "-fx-padding: 10; " + "-fx-font-size: 13px;");

        } else {
            tienThanhToan *= -1;
            txtTienNhan.setText(format1.format(tienCoc));
            txtTienNhan.setEditable(false);
            txtTienThua.setText(format1.format(tienThanhToan));
            txtTienThua.setStyle("-fx-background-color: #ecf0f1; " + "-fx-border-color: #3498db; "
                    + "-fx-border-width: 1.5; " + "-fx-border-radius: 6; " + "-fx-background-radius: 6; "
                    + "-fx-padding: 10; " + "-fx-font-size: 13px;" + "-fx-text-fill: green");
            lblTienThanhToan.setText("Tiền hoàn lại cho khách:");
            lblTienThanhToan.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: white");
            txtTongTien.setStyle(
                    "-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: transparent");
        }
        txtTongTien.setText(format2.format(tienThanhToan));

        loadDuLieuNhapNhanh(btnNhapNhanh1, btnNhapNhanh2, btnNhapNhanh3, btnNhapNhanh4, btnNhapNhanh5, btnNhapNhanh6);
    }

    public boolean checkTienNhan() {
    		
    		double giamGia = parseVNDToDouble(txtGiamGia.getText());
    		double thue = parseVNDToDouble(txtThue.getText());
    		double coc = parseVNDToDouble(txtTienCoc.getText());
    		double tongTien = parseVNDToDouble(txtTamTinh.getText());
    		
    		double thanhToan = (tongTien - giamGia + thue - coc);
    		if(thanhToan <= 0) {
    			return true;
    		}
    		
        String chuoiTienNhan = txtTienNhan.getText().trim();
        double tienNhan = 0.0;
        if(chuoiTienNhan.isBlank()) {
            showAlert(AlertType.ERROR, "Lỗi", "Vui lòng nhập tiền nhận");
            txtTienNhan.requestFocus();
            return false;
        }
        if (!chuoiTienNhan.matches("\\d{1,3}(\\.\\d{3})*")) {
            showAlert(AlertType.ERROR, "Lỗi", "Tiền nhận phải là số theo dạng: 123.456");
            txtTienNhan.requestFocus();
            txtTienNhan.selectAll();
            return false;
        }

        String chuoiKhongCham = chuoiTienNhan.replace(".", "");
        try {
            tienNhan = Double.parseDouble(chuoiKhongCham);
        } catch (Exception er) {
            showAlert(AlertType.ERROR, "Lỗi", "Tiền nhận không convert được");
            txtTienNhan.requestFocus();
            txtTienNhan.selectAll();
            return false;
        }

        double tongTienDB = parseVNDToDouble(txtTongTien.getText());


        if (tienNhan - tongTienDB < 0) {
            showAlert(AlertType.ERROR, "Lỗi", "Tiền nhận phải lớn hơn hoặc bằng tổng tiền");
            txtTienNhan.requestFocus();
            txtTienNhan.selectAll();
            txtTienThua.setText("");
            return false;
        }

        return true;
    }

    public Tooltip taoToolTipKhuyenMai(KhuyenMai km) {
        return control.taoToolTip(km);
    }

    public void loadDuLieuNhapNhanh(Button btn1, Button btn2, Button btn3, Button btn4, Button btn5, Button btn6) {
        int tongTien = (int) Math.round(parseVNDToDouble(txtTongTien.getText()));

        List<Integer> dsNhapNhanh = control.suggestCash(tongTien);

        DecimalFormat formatNhapNhanh = new DecimalFormat("#,##0 đ");
        btn1.setText(formatNhapNhanh.format(dsNhapNhanh.get(0)));
        btn2.setText(formatNhapNhanh.format(dsNhapNhanh.get(1)));
        btn3.setText(formatNhapNhanh.format(dsNhapNhanh.get(2)));
        btn4.setText(formatNhapNhanh.format(dsNhapNhanh.get(3)));
        btn5.setText(formatNhapNhanh.format(dsNhapNhanh.get(4)));
        btn6.setText(formatNhapNhanh.format(dsNhapNhanh.get(5)));
    }

    public double parseDToDouble(String vndFormatted) {
        if (vndFormatted == null || vndFormatted.trim().isEmpty()) {
            return 0.0;
        }

        try {
            // Loại bỏ "đ" và trim
            String cleanString = vndFormatted.replaceAll("đ", "").trim();

            // Sử dụng NumberFormat Việt Nam để parse (handle "," nghìn tự động)
            NumberFormat formatVN = NumberFormat.getInstance(new Locale("vi", "VN"));
            Number number = formatVN.parse(cleanString);
            return number.doubleValue();
        } catch (Exception e) {
            System.err.println("Lỗi parse VND formatted: " + vndFormatted + " - " + e.getMessage());
            return 0.0; // Fallback
        }
    }

    public void thucHienNumpad(Button btn1, Button btn2, Button btn3, Button btn4, Button btn5, Button btn6,
                               Button btn7, Button btn8, Button btn9, Button btn0, Button btnC, Button btnBackspace, Button btnEnter,
                               Button btn50, Button btn100, Button btn200, Button btn500, Button btn00, Button btn000,
                               TextField txtTienNhan) {

        // Gán event cho nút số 1-9, 0
        btn1.setOnAction(e -> txtTienNhan.appendText("1"));
        btn2.setOnAction(e -> txtTienNhan.appendText("2"));
        btn3.setOnAction(e -> txtTienNhan.appendText("3"));
        btn4.setOnAction(e -> txtTienNhan.appendText("4"));
        btn5.setOnAction(e -> txtTienNhan.appendText("5"));
        btn6.setOnAction(e -> txtTienNhan.appendText("6"));
        btn7.setOnAction(e -> txtTienNhan.appendText("7"));
        btn8.setOnAction(e -> txtTienNhan.appendText("8"));
        btn9.setOnAction(e -> txtTienNhan.appendText("9"));
        btn0.setOnAction(e -> txtTienNhan.appendText("0"));
        btn00.setOnAction(e -> txtTienNhan.appendText("00"));
        btn000.setOnAction(e -> txtTienNhan.appendText("000"));
        btn50.setOnAction(e -> txtTienNhan.setText("50000"));
        btn100.setOnAction(e -> txtTienNhan.setText("100000"));
        btn200.setOnAction(e -> txtTienNhan.setText("200000"));
        btn500.setOnAction(e -> txtTienNhan.setText("500000"));
        btnEnter.setOnAction(e -> btnThanhToan.fire());

        // Nút "C" (Clear)
        btnC.setOnAction(e -> {
            txtTienNhan.setText("");
        });

        // Nút Backspace (xóa cuối)
        btnBackspace.setOnAction(e -> {
            String currentText = txtTienNhan.getText();
            if (currentText.length() > 0) {
                String newText = currentText.substring(0, currentText.length() - 1);
                txtTienNhan.setText(newText);
            }
        });

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
			
			if((pageNum == 1 && (itemsThisPage > 10 && itemsThisPage <= 19)) || (pageNum > 1 && (itemsThisPage > 16 && itemsThisPage <= 25)))
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
		table.setPrefHeight(ds.size() * 25 + 20);
		return table;
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

			box.getChildren().addAll(hbox1, hbox2, hbox3, line, hbox4,  line2, hbox5);

		}
		box.setStyle("-fx-padding: 10 0 0 0;");
		return box;
	}
	
	public void thucHienIn(String maHoaDon, String dsBan) {

		QLHD_DAO daoQLHD = new  QLHD_DAO();
		
	    if (maHoaDon == null || maHoaDon.isBlank()) {
	        showAlert(AlertType.ERROR, "Lỗi", "Vui lòng chọn một hóa đơn trước khi in");
	        return;
	    }

	    List<String> dsMon = daoQLHD.getChiTietHoaDonTheoMa(maHoaDon);
	    HoaDon hoaDon = daoQLHD.getHoaDonById(maHoaDon);

	    // Gọi xem trước
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
}