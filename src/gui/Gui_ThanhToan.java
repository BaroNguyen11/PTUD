
package gui;



import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.swing.JOptionPane;
import ctrl.ThanhToan_Ctrl;
import entity.BanAn;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.LoaiBan;
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

import java.awt.Graphics2D;
import java.awt.print.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.NumberStringConverter;
import javafx.print.*;
import javafx.print.Paper;
import javafx.print.PrinterJob;

import java.util.Map;
import java.util.Optional;

public class Gui_ThanhToan extends BorderPane {
    private ThanhToan_Ctrl control = new ThanhToan_Ctrl();
    private List<BanAn> dsBan = new ArrayList<>();
    private Scene scene;
    private ScrollPane scroll;
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
    private Button btnPhimCham;
    private Button btnPhim00;
    private Button btnPhim000;
    private Button btnPhimCham000;
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
    private ToggleButton tang1;
    private ToggleButton tang2;

    public Gui_ThanhToan(NhanVien nv, BanAn ban) {
        this.nv = nv;
//        dsBan = control.layDanhSachBanThanhToan(LocalDate.now());
//        banChon = new BanAn();
        // Root chính
        this.setStyle("-fx-background-color: white");

        // Màn hiển thị danh sách bàn
//        BorderPane manHienThiBan = taoManHinhDanhSachBan(control.chiaTang(dsBan, "Tầng 1"));
        
        BorderPane manHienThiBan = taoManHinhThanhToan(ban);
        
        this.setCenter(manHienThiBan);
        this.getStylesheets().add(getClass().getResource("/css/qlkm.css").toExternalForm());
        this.getStylesheets().add(getClass().getResource("/css/thanhtoan.css").toExternalForm());

    }

    // Tạo màn danh sách bàn
    public BorderPane taoManHinhDanhSachBan(List<BanAn> dsBan) {
        BorderPane root = new BorderPane();

        // Phần trên (chọn tầng + tìm kiếm + chú thích)
        HBox hboxPhanTren = taoPhanTren();
        root.setTop(hboxPhanTren);

        // Phần danh sách bàn
        HBox hboxDanhSach = taoPhanDanhSachBan(dsBan);
        root.setCenter(hboxDanhSach);

        safeClearAccelerators();
        return root;
    }

    // Tạo phần trên với nút tầng, tìm kiếm, chú thích
    private HBox taoPhanTren() {
        VBox left = new VBox(5);
        left.setPadding(new Insets(10));

        // --- Nút chọn tầng ---
        Label lblKhuVuc = new Label("Khu vực");
        lblSetStyle(lblKhuVuc, "#667eea");
        lblKhuVuc.setGraphic(createSvgIcon(20, 16, "#667eea",
                "M5.5 2a.5.5 0 0 0-.5.5v8a.5.5 0 0 0 .5.5h5a.5.5 0 0 0 .5-.5v-8a.5.5 0 0 0-.5-.5zm1 4h2a.5.5 0 0 1 .5.5a.5.5 0 0 1-.5.5H7v.5a.5.5 0 0 1-.5.5a.5.5 0 0 1-.5-.5v-1a.5.5 0 0 1 .5-.5m-2 6a.5.5 0 0 0 0 1h7a.5.5 0 0 0 0-1zm-1 2a.5.5 0 0 0 0 1h9a.5.5 0 0 0 0-1z"));
        lblKhuVuc.setContentDisplay(ContentDisplay.LEFT);
        lblKhuVuc.setGraphicTextGap(10);

        tang1 = new ToggleButton("Tầng 1");
        tang2 = new ToggleButton("Tầng 2");
        ToggleGroup group = new ToggleGroup();
        tang1.setToggleGroup(group);
        tang2.setToggleGroup(group);
        tang1.getStyleClass().add("nutTang");
        tang2.getStyleClass().add("nutTang");
        HBox nutTang = new HBox(5, tang1, tang2);
        nutTang.setAlignment(Pos.CENTER_LEFT);

        // Chức năng loc tầng
        tang1.setOnAction(e -> {
            scroll.setContent(taoLuoiBan(control.chiaTang(dsBan, "Tầng 1")));
        });

        tang2.setOnAction(e -> {
            scroll.setContent(taoLuoiBan(control.chiaTang(dsBan, "Tầng 2")));
        });

        tang1.setSelected(true);

        // --- Ô tìm kiếm ---
        Label lblTimKiem = new Label("Tìm kiếm");
        lblSetStyle(lblTimKiem, "#667eea");
        lblTimKiem.setGraphic(createSvgIcon(20, 16, "#667eea",
                "m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z"));
        lblTimKiem.setContentDisplay(ContentDisplay.LEFT);
        lblTimKiem.setGraphicTextGap(10);

        TextField txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Tìm bằng mã bàn");
        txtTimKiem.getStyleClass().add("timKiem");
        Button btnTim = new Button("Tìm");
        btnTim.getStyleClass().add("button-timKiem");
        ComboBox<String> cboLoc = new ComboBox<String>();
        cboLoc.getItems().addAll("Tất cả loại", "Bàn vip", "Bàn thường");
        cboLoc.getStyleClass().add("combo-box");
        cboLoc.getSelectionModel().selectFirst();
        HBox timKiem = new HBox(10, txtTimKiem, btnTim, cboLoc);
        timKiem.setAlignment(Pos.CENTER_LEFT);

        left.getChildren().addAll(lblKhuVuc, nutTang, lblTimKiem, timKiem);
        left.setStyle("-fx-border-width: 0 1 0 0; -fx-border-color: #D9D9D9");

        // --- Chú thích trạng thái ---
        VBox trangThai = new VBox(5);
        trangThai.setPadding(new Insets(10));

        Label lblTrangThai = new Label("Chú thích");
        lblSetStyle(lblTrangThai, "#667eea");
        lblTrangThai.setGraphic(createSvgIcon(20, 16, "#667eea",
                "M7.5 2c-.277 0-.5.223-.5.5v2c0 .277.223.5.5.5h2c.277 0 .5-.223.5-.5v-2c0-.277-.223-.5-.5-.5zm-1 5a.499.499 0 1 0 0 1H7v5h-.5a.499.499 0 1 0 0 1h4a.499.499 0 1 0 0-1H10V7.5c0-.277-.223-.5-.5-.5z"));
        lblTrangThai.setContentDisplay(ContentDisplay.LEFT);
        lblTrangThai.setGraphicTextGap(10);
        HBox vip = new HBox();
        ImageView iconVip = new ImageView(new Image(getClass().getResourceAsStream("/img/vipicon.png")));
        iconVip.setFitWidth(15);
        iconVip.setFitHeight(15);
        Label lblVip = new Label("Bàn VIP");
        lblVip.setStyle("-fx-text-fill: yellow; -fx-font-weight: bold; -fx-font-size: 11;");
        vip.getChildren().addAll(iconVip, lblVip);
        vip.setPadding(new Insets(0, 0, 0, 4));
        vip.setAlignment(Pos.CENTER_LEFT);

        HBox ban = new HBox();
        Label lblBan = new Label("Đang sử dụng");
        lblBan.setStyle("-fx-text-fill: yellow; -fx-font-weight: bold; -fx-font-size: 11;");
        trangThai.getChildren().addAll(lblTrangThai, vip, ban);

        HBox topAll = new HBox(40, left, trangThai);
        return topAll;
    }

    // Tạo phần giữa (danh sách bàn)
    private HBox taoPhanDanhSachBan(List<BanAn> dsBanAn) {
        HBox giua = new HBox(20);
        giua.setPadding(new Insets(10));

        VBox trai = new VBox(10);

        Label lblDanhSach = new Label("Danh sách bàn");
        lblSetStyle(lblDanhSach, "#667eea");
        lblDanhSach.setGraphic(createSvgIcon(30, 16, "#667eea",
                "M1.418 8.006a.5.5 0 0 0-.412.576l1 6a.5.5 0 1 0 .988-.164L2.924 14h1.652l-.07.418a.5.5 0 1 0 .988.164l.5-3A.5.5 0 0 0 5.5 11H2.424l-.43-2.582a.503.503 0 0 0-.576-.412M4.5 8a.499.499 0 1 0 0 1H7v5.5a.499.499 0 1 0 1 0V9h2.5a.499.499 0 1 0 0-1zm8.506.418L12.576 11H9.498a.5.5 0 0 0-.492.582l.5 3a.5.5 0 1 0 .986-.164l-.07-.418h1.654l-.07.418a.5.5 0 1 0 .986.164l1-6a.5.5 0 1 0-.986-.164"));
        lblDanhSach.setContentDisplay(ContentDisplay.LEFT);
        lblDanhSach.setGraphicTextGap(10);

        GridPane luoiBan = taoLuoiBan(dsBanAn);
        scroll = new ScrollPane(luoiBan);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(620);
        scroll.getStyleClass().add("scroll-pane");

        trai.getChildren().addAll(lblDanhSach, scroll);
//        giua.setAlignment(Pos.CENTER);
        giua.getChildren().add(trai);

        return giua;
    }

    // Lưới bàn
    private GridPane taoLuoiBan(List<BanAn> dsBanAn) {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        grid.setStyle("-fx-background-color: white;");

        for (int i = 0; i < dsBanAn.size(); i++) {
            StackPane theBan = taoTheBan(dsBanAn.get(i));

            int hang = i / 5;
            int cot = i % 5;
            GridPane.setRowIndex(theBan, hang);
            GridPane.setColumnIndex(theBan, cot);
            grid.getChildren().add(theBan);

        }
        return grid;
    }

    // Tạo thẻ bàn
    private StackPane taoTheBan(BanAn banAn) {
        StackPane khung = new StackPane();
        khung.setMinHeight(100);
        khung.setMinWidth(210);

        Region mauTrai = new Region();
        mauTrai.setPrefWidth(10);
        mauTrai.setStyle("-fx-background-color: yellow; -fx-background-radius: 20;");

        VBox the = new VBox(10);
        the.setPrefSize(210, 130);
        the.setAlignment(Pos.CENTER);
        the.setPadding(new Insets(0, 10, 0, 10));
        the.setStyle("-fx-background-color: #082744; -fx-background-radius: 20;");

        ImageView iconVip = new ImageView(new Image("img/vipicon.png"));
        iconVip.setFitHeight(20);
        iconVip.setFitWidth(20);
        HBox hboxVip = new HBox();
        if (banAn.getLoai().equals(LoaiBan.VIP)) {
            hboxVip.getChildren().add(iconVip);
        }
        hboxVip.setAlignment(Pos.TOP_RIGHT);
        hboxVip.setMinHeight(20);

        Label lblTen = new Label(banAn.getMaBan());
        lblTen.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        lblTen.setTextFill(Color.WHITE);

        Button btnThanhToan = new Button("Thanh toán");
        btnThanhToan.getStyleClass().add("btn-thanhToan-guiThanhToan");
        btnThanhToan.setOnAction(e -> {
            this.setCenter(taoManHinhThanhToan(banAn));
//        		banChon = banAn;
        });

        the.getChildren().addAll(hboxVip, lblTen, btnThanhToan);

        khung.getChildren().addAll(mauTrai, the);
        StackPane.setAlignment(mauTrai, Pos.CENTER_LEFT);
        StackPane.setMargin(the, new Insets(0, 0, 0, 5));

        return khung;
    }

    private BorderPane taoManHinhThanhToan(BanAn banAn) {
        // Root all
        BorderPane rootAll = new BorderPane();

        // Lấy các dữ liệu cần thiết
        PhieuDatBan phieuDatBan = control.timPhieuTheoMaBan(banAn.getMaBan());
        KhachHang khachHang = phieuDatBan.getKhachHang();

        // Tạo phần trái
        VBox vboxPhanTrai = taoPhanTrai(banAn, khachHang, phieuDatBan);
        rootAll.setLeft(vboxPhanTrai);

        // Tạo phần phải
        VBox vboxPhanPhai = taoPhanPhai(banAn);
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

    private VBox taoPhanTrai(BanAn banAn, KhachHang khachHang, PhieuDatBan phieuDatBan) {
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

        // Khai báo biến
        ImageView iconVip = new ImageView(new Image("/img/vipicon.png"));
        Label lblMaBan = new Label(banAn.getMaBan());
        HBox hbox1 = new HBox(5);

        Label lblDanhSach = new Label("Danh sách món sử dụng");
        lblSetStyle(lblDanhSach, "#667eea");
        lblDanhSach.setGraphic(createSvgIcon(20, 14, "#667eea",
                "M7 3h0a6.5 6.5 0 0 1 6.5 6.5v0a1 1 0 0 1-1 1h-11a1 1 0 0 1-1-1v0A6.5 6.5 0 0 1 7 3Zm0 0V1.5m-6.5 11h13"));
        lblDanhSach.setContentDisplay(ContentDisplay.LEFT);
        lblDanhSach.setGraphicTextGap(10);

        List<String> dsChiTietRaw = control.layDanhSachCTHD(control.layHoaDonTheoMaBan(banAn.getMaBan()).getMaHoaDon());
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
        TextField txtTienCoc = new TextField();
        HBox hbox5 = new HBox();
        lblTongTien = new Label("Tổng tiền:");
        txtTongTien = new TextField();
        HBox hbox6 = new HBox();

        ///
        iconVip.setFitHeight(30);
        iconVip.setFitWidth(30);
        lblMaBan.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
        lblMaBan.setGraphic(createSvgIcon(24, 24, "black",
                "m9.2 17l-.975 2.425q-.125.275-.35.425t-.5.15q-.5 0-.787-.413t-.088-.862l1-2.475q.225-.575.725-.913T9.35 15H11v-4.025Q7.175 10.85 4.587 9.85T2 7.5q0-1.45 2.925-2.475T12 4q4.175 0 7.088 1.025T22 7.5q0 1.35-2.588 2.35T13 10.975V15h1.65q.6 0 1.113.338t.737.912l1 2.475q.1.225.063.45t-.163.413q-.125.187-.325.3t-.45.112q-.275 0-.5-.15t-.35-.425L14.8 17H9.2Z"));
        lblMaBan.setGraphicTextGap(10);
        lblMaBan.setContentDisplay(ContentDisplay.LEFT);

        if (banAn.getLoai().equals(LoaiBan.THUONG)) {
            hbox1.getChildren().addAll(lblMaBan);
        } else
            hbox1.getChildren().addAll(lblMaBan, iconVip);
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
        
        	txtTamTinh.setText(format.format(Double.parseDouble(dsChiTiet.get(0).split(",")[3])));
        double tamTinh = parseVNDToDouble(txtTamTinh.getText());

        double thue = control.tinhThue(Double.parseDouble(dsChiTiet.get(0).split(",")[3]));
        txtThue.setText(format.format(thue));

        double tienCoc = control.tinhCoc(banAn);
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
        List<KhuyenMai> dsKMApDung = control.layDanhSachKhuyenMai(phieuDatBan.getMaPhieu(), tamTinh);


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
                double giaGiam = control.tinhTienGiamGia(tamTinh, selected.getGiamGiaPhanTram(),
                        selected.getGiaTriGiam(), selected.getGiaTriToiDa());
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

        txtTenKhacHang.setText(khachHang.getTenKhachHang());
        txtDiemTichLuy.setText(khachHang.getDiemTichLuy() + "");

        vboxAll.setPrefHeight(500);
        vboxAll.getChildren().addAll(hbox1, lblKhachHang, hboxTenKH, hboxDiem, lblKMTieuDe, hboxKM, lblDanhSach,
                tableMon, hbox2, hbox3, hbox4, hbox5, hbox6);
        vboxAll.setPadding(new Insets(20, 10, 20, 10));
        vboxAll.setStyle("-fx-border-width: 0 1 0 0; -fx-border-color: #908C8C");

        return vboxAll;
    }

    private VBox taoPhanPhai(BanAn banAn) {
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
            HoaDon hoaDon = control.layHoaDonTheoMaBan(banAn.getMaBan());
            String tamTinh = txtTamTinh.getText();
            String thueVAT = txtThue.getText();
            String giamGia = txtGiamGia.getText();
            String tongTien = txtTongTien.getText();

            inHoaDon((Stage) this.getScene().getWindow(),control.layDanhSachCTHD(hoaDon.getMaHoaDon()), banAn, hoaDon, tamTinh, thueVAT, giamGia, tongTien);
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
            this.setCenter(new Gui_DanhSachBan(new Gui_TrangChu(new NhanVien())));
        });

        btnThanhToan.setOnAction(e -> {

            if (checkTienNhan()) {
                String maHoaDon = control.layHoaDonTheoMaBan(banAn.getMaBan()).getMaHoaDon();
                String maPhieu = control.timPhieuTheoMaBan(banAn.getMaBan()).getMaPhieu();
                String tongTien = txtTongTien.getText();
                String phuongThuc = btnTienMat.isSelected() ? "Tiền mặt" : "Chuyển khoản";
                KhuyenMai khuyenMai = cboKM.getSelectionModel().getSelectedItem();
                if (thanhToan(tongTien, phuongThuc, maHoaDon, maPhieu, banAn.getMaBan(), khuyenMai)) {
                    btnQuayLai.fire();
                    loadLaiDanhSach();
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

    private boolean thanhToan(String tongTien, String phuongThuc, String maHoaDon, String maPhieu, String maBan, KhuyenMai khuyenMai) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận thanh toán");
        alert.setHeaderText("Bạn có chắc thanh toán?");
        alert.setContentText("Tổng tiền: " + tongTien + "\nPhương thức: " + phuongThuc);
        double giamGia = parseVNDToDouble(txtGiamGia.getText());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (control.xuLyThanhToan(maHoaDon, maPhieu, maBan, phuongThuc, giamGia, khuyenMai)) {
                    showAlert(AlertType.INFORMATION, "Thanh Toán Thành Công", "Tổng tiền thanh toán: " + tongTien);
                    return true;
                }else {
                    showAlert(AlertType.ERROR, "Thất bại", "Thanh Toán Không Thành Công !");
                }
            } catch (Exception e) {
                showAlert(AlertType.ERROR, "Thất bại", "Thanh Toán Không Thành Công !");
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

    public void inHoaDon(Stage owner, List<String> danhSach, BanAn banAn, HoaDon hoaDon, String tamTinh, String thueVAT, String giamGia, String tongTien) {
        Printer printer = Printer.getDefaultPrinter();
        PrinterJob job = PrinterJob.createPrinterJob(printer);
        if (job == null || !job.showPrintDialog(owner)) {
            return;
        }

        PageLayout layout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
        double maxHeight = layout.getPrintableHeight();

        int currentIndex = 0;
        int sttGlobal = 1; // STT liên tục toàn bộ hóa đơn
        int pageNum = 1;
        boolean hasMore = true;
        while (hasMore) {
            VBox pageBox = new VBox(10);
            pageBox.setStyle("-fx-padding: 20; -fx-font-family: Arial; -fx-background-color: white;"); // Nền trắng cho
            // page in
            pageBox.setAlignment(Pos.TOP_CENTER); // Căn giữa theo chiều dọc trên cùng

            // Header chỉ trang đầu
            if (pageNum == 1) {
                pageBox.getChildren().add(taoHeader(1, 1, banAn, hoaDon)); // totalPages không biết trước, có thể để 1 hoặc tính trước
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

            pageBox.getChildren().add(taoFooter(!hasMore, tamTinh, thueVAT, giamGia, tongTien)); // Footer chỉ trang cuối

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

        job.endJob();
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

    private VBox taoHeader(int page, int totalPages, BanAn banAn, HoaDon hoaDon) {
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

        Label lblMaBan = new Label("Bàn: " + banAn.getMaBan());
        HBox hbox2 = new HBox(lblMaBan);
        hbox2.setAlignment(Pos.CENTER_LEFT);
        Label lblThuNgan = new Label("Thu ngân: " + nv.getTenNhanVien());
        HBox hbox3 = new HBox(lblThuNgan);
        hbox3.setAlignment(Pos.CENTER_LEFT);

        box.setAlignment(Pos.CENTER);

        box.setMargin(hbox1, new Insets(0, 15, 0, 0));
        box.getChildren().addAll(lblTenNhaHang, lblDiaChi, lblSdt, lblHoaDon, hbox1, hbox2, hbox3, new Separator());
        return box;
    }

    private VBox taoFooter(boolean lastPage, String tamTinh, String thueVAT, String giamGia, String tongTien) {
        VBox box = new VBox(5);
        box.setMaxWidth(480);
        if (lastPage) {
            HBox hbox1 = new HBox();
            Region spacer1 = new Region();
            hbox1.setMaxWidth(460);
            HBox.setHgrow(spacer1, Priority.ALWAYS);

            Label lblTamTinh = new Label("Tạm tính: ");
            Label lblTamTinhText = new Label(tamTinh);

            hbox1.getChildren().addAll(lblTamTinh, spacer1, lblTamTinhText);

            HBox hbox2 = new HBox();
            Region spacer2 = new Region();
            hbox2.setMaxWidth(460);
            HBox.setHgrow(spacer2, Priority.ALWAYS);

            Label lblThue = new Label("Thuế VAT: ");
            Label lblThueText = new Label(thueVAT);

            hbox2.getChildren().addAll(lblThue, spacer2, lblThueText);

            HBox hbox3 = new HBox();
            Region spacer3 = new Region();
            hbox3.setMaxWidth(460);
            HBox.setHgrow(spacer3, Priority.ALWAYS);

            Label lblGiamGia = new Label("Giảm giá: ");
            Label lblGiamGiaText = new Label(giamGia);

            hbox3.getChildren().addAll(lblGiamGia, spacer3, lblGiamGiaText);

            Separator line = new Separator();
            line.setPrefWidth(480);
            line.setStyle("-fx-background-color: black");

            HBox hbox4 = new HBox();
            Region spacer4 = new Region();
            hbox4.setMaxWidth(460);
            HBox.setHgrow(spacer4, Priority.ALWAYS);

            Label lblTongTien = new Label("Tổng tiền: ");
            Label lblTongTienText = new Label(tongTien);

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

    public void loadLaiDanhSach() {
        dsBan = control.layDanhSachBanThanhToan(LocalDate.of(2025, 10, 29));
        tang1.fire();
    }



}