package gui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import entity.HoaDon;
import entity.KhachHang;
import entity.MonAn;
import entity.NhanVien;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Gui_QuanLiHoaDon extends BorderPane {

    // Dữ liệu test cho món ăn
    private ObservableList<MonAn> dsMonAnTest = FXCollections.observableArrayList();
    // Dữ liệu test cho hóa đơn
    private ObservableList<HoaDon> dsHoaDonTest = FXCollections.observableArrayList();


    public Gui_QuanLiHoaDon() {

        // Phần thông tin và tìm kiếm
        VBox bangThongTinTimKiem = taoPhanThongTimKiem();
        this.setCenter(bangThongTinTimKiem);
        BorderPane.setAlignment(bangThongTinTimKiem, Pos.CENTER_LEFT);
        BorderPane.setMargin(bangThongTinTimKiem, new Insets(20, 40, 10, 0));

        // Bảng danh sách món thanh toán
        VBox bangMonAn = taoBangMonAn();
        this.setRight(bangMonAn);
        //    bangMonAn.setStyle("-fx-background-color: black");
        //    BorderPane.setAlignment(bangMonAn, Pos.CENTER_LEFT);
        BorderPane.setMargin(bangMonAn, new Insets(10, 40, 10, 10));

        //Bảng hóa đơn
        this.setBottom(taoBangHoaDon());
        this.setStyle("-fx-background-color: white");
    }


    // Phần tìm kiếm hóa đơn
    private VBox taoPhanThongTimKiem() {
        //VBox all
        VBox vboxAll = new VBox(8);

        //Spacer
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
        Region spacer9 = new Region();
        HBox.setHgrow(spacer9, Priority.ALWAYS);
        Region spacer7 = new Region();
        HBox.setHgrow(spacer7, Priority.ALWAYS);

        //// Ô tìm kiếm
        Label lblTiemKiem = new Label("Tìm kiếm hóa đơn");
        lblTiemKiem.getStyleClass().add("fontTieuDeNho");
        TextField timKiem = new TextField();
        timKiem.setPromptText("Tìm kiếm bằng mã hóa đơn");
        timKiem.getStyleClass().add("timKiem");
        Button nutTimKiem = new Button("Tìm kiếm");
        nutTimKiem.getStyleClass().add("button-timKiem");
        HBox oTimKiem = new HBox(10, timKiem, nutTimKiem);
        oTimKiem.setAlignment(Pos.CENTER_LEFT);
        VBox vboxTimKiem = new VBox(5);
        vboxTimKiem.getChildren().addAll(lblTiemKiem, oTimKiem);

        //Thông tin hóa đơn

        //Ma hóa đơn
        Label lblMaHoaDon = new Label("Mã hóa đơn:");
        lblMaHoaDon.getStyleClass().add("fontTieuDeNho");
        TextField txtMaHoaDon = new TextField();
        txtMaHoaDon.setEditable(false);
        txtMaHoaDon.setPrefWidth(250);
        txtMaHoaDon.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #D9D9D9; -fx-font-size: 15");
        HBox hbox1 = new HBox(lblMaHoaDon, spacer1, txtMaHoaDon );
        hbox1.setPadding(new Insets(5));
        hbox1.setPadding(new Insets(0, 30, 0, 0));
        hbox1.setPrefWidth(250);

        //Khách hang
        Label lblKhachHang = new Label("Khách hàng:");
        lblKhachHang.getStyleClass().add("fontTieuDeNho");
        TextField txtKhachHang = new TextField();
        txtKhachHang.setEditable(false);
        txtKhachHang.setPrefWidth(250);
        txtKhachHang.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #D9D9D9; -fx-font-size: 15");
        HBox hbox2 = new HBox(lblKhachHang, spacer2, txtKhachHang );
        hbox2.setPadding(new Insets(5));
        hbox2.setPadding(new Insets(0, 30, 0, 0));

        //Nhan viên
        Label lblNhanVien = new Label("Nhân viên:");
        lblNhanVien.getStyleClass().add("fontTieuDeNho");
        TextField txtNhanVien = new TextField();
        txtNhanVien.setEditable(false);
        txtNhanVien.setPrefWidth(250);
        txtNhanVien.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #D9D9D9; -fx-font-size: 15");
        HBox hbox3 = new HBox(lblNhanVien, spacer3, txtNhanVien );
        hbox3.setPadding(new Insets(5));
        hbox3.setPadding(new Insets(0, 30, 0, 0));

        //Giảm giá
        Label lblGiamGia = new Label("Giảm giá:");
        lblGiamGia.getStyleClass().add("fontTieuDeNho");
        TextField txtGiamGia = new TextField();
        txtGiamGia.setEditable(false);
        txtGiamGia.setPrefWidth(250);
        txtGiamGia.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #D9D9D9; -fx-font-size: 15");
        HBox hbox4 = new HBox(lblGiamGia, spacer4, txtGiamGia );
        hbox4.setPadding(new Insets(5));
        hbox4.setPadding(new Insets(0, 30, 0, 0));

        //Tổng tiền
        Label lblTongTien = new Label("Tổng tiền:");
        lblTongTien.getStyleClass().add("fontTieuDeNho");
        TextField txtTongTien = new TextField();
        txtTongTien.setEditable(false);
        txtTongTien.setPrefWidth(250);
        txtTongTien.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #D9D9D9; -fx-font-size: 15");
        HBox hbox5 = new HBox(lblTongTien, spacer5, txtTongTien );
        hbox5.setPadding(new Insets(5));
        hbox5.setPadding(new Insets(0, 30, 0, 0));

        //Phương thức
        Label lblPhuongThuc = new Label("Phương thức:");
        lblPhuongThuc.getStyleClass().add("fontTieuDeNho");
        TextField txtPhuongThuc = new TextField();
        txtPhuongThuc.setEditable(false);
        txtPhuongThuc.setPrefWidth(250);
        txtPhuongThuc.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #D9D9D9; -fx-font-size: 15");
        HBox hbox6 = new HBox(lblPhuongThuc, spacer6, txtPhuongThuc );
        hbox6.setPadding(new Insets(5));
        hbox6.setPadding(new Insets(0, 30, 0, 0));

        //Tien coc
        Label lblTienCoc = new Label("Tiền cọc:");
        lblTienCoc.getStyleClass().add("fontTieuDeNho");
        TextField txtTienCoc = new TextField();
        txtTienCoc.setEditable(false);
        txtTienCoc.setPrefWidth(250);
        txtTienCoc.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #D9D9D9; -fx-font-size: 15");
        HBox hbox7 = new HBox(lblTienCoc, spacer7, txtTienCoc );
        hbox7.setPadding(new Insets(5));
        hbox7.setPadding(new Insets(0, 30, 0, 0));

        //Ngày tạo
        Label lblNgayTao = new Label("Ngày tạo:");
        lblNgayTao.getStyleClass().add("fontTieuDeNho");
        TextField txtNgayTao = new TextField();
        txtNgayTao.setEditable(false);
        txtNgayTao.setPrefWidth(250);
        txtNgayTao.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #D9D9D9; -fx-font-size: 15");
        HBox hbox8 = new HBox(lblNgayTao, spacer8, txtNgayTao );
        hbox8.setPadding(new Insets(5));
        hbox8.setPadding(new Insets(0, 30, 0, 0));

        //Button in hóa đơn
        HBox hbox9 = new HBox();
        Button btnIn = new Button("In hóa đơn");
        btnIn.setStyle("-fx-background-color: #082744; -fx-background-radius: 5; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-family:'Tai Heritage Pro'; -fx-font-weight: bold; -fx-cursor: hand");
        hbox9.setAlignment(Pos.CENTER_RIGHT);
        hbox9.getChildren().add(btnIn);
        hbox9.setPadding(new Insets(0, 30, 0, 0));

        //
        vboxAll.setPadding(new Insets(10));
        vboxAll.getChildren().addAll(vboxTimKiem, hbox1, hbox2, hbox3, hbox4, hbox5, hbox6, hbox7, hbox8, hbox9);
        vboxAll.setStyle("-fx-border-width: 0 1 0 0; -fx-border-color: #D9D9D9");
        vboxAll.setMaxWidth(450);
        vboxAll.setMaxHeight(450);

        //
        return vboxAll;
    }

    private VBox taoBangMonAn() {
        VBox vboxAll = new VBox(20);
        vboxAll.setPadding(new Insets(10));

        Label lblTieuDe = new Label("Danh sách món thanh toán");
        lblTieuDe.getStyleClass().add("fontTieuDeNho");

        TableView<MonAn> tableMonAn = new TableView<>();

        // Cột STT
        TableColumn<MonAn, Void> colSTT = new TableColumn<>("STT");
        colSTT.setPrefWidth(50);
        colSTT.setSortable(false);
        colSTT.setCellFactory(col -> new TableCell<MonAn, Void>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
                setAlignment(Pos.CENTER);
            }
        });

        // Cột tên món
        TableColumn<MonAn, String> colTenMon = new TableColumn<>("Tên món");

        // Gán dữ liệu từ thuộc tính "tenMonAn" trong class MonAn
        colTenMon.setCellValueFactory(new PropertyValueFactory<>("tenMonAn"));

        // Tùy chỉnh hiển thị từng ô (cell)
        colTenMon.setCellFactory(tc -> new TableCell<MonAn, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle(""); // reset style khi ô rỗng
                } else {
                    setText(item);
                    setAlignment(Pos.CENTER_LEFT); // căn trái (CENTER hoặc RIGHT tùy ý)
                }
            }
        });

        colTenMon.setPrefWidth(250);

        // Cột số lượng
        TableColumn<MonAn, Integer> colSoLuong = new TableColumn<>("Số lượng");

        // Ở đây mình đang test giá trị cố định = 111 (có thể thay bằng dữ liệu thật)
        colSoLuong.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(111));

        colSoLuong.setCellFactory(tc -> new TableCell<MonAn, Integer>() {
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
        TableColumn<MonAn, Double> colGia = new TableColumn<>("Giá");
        colGia.setCellValueFactory(new PropertyValueFactory<>("giaTien"));
        colGia.setPrefWidth(150);
        colGia.setCellFactory(tc -> new TableCell<MonAn, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("%,.0fđ", item));
                setAlignment(Pos.CENTER);
            }

        });

        // Cột tổng tiền
        TableColumn<MonAn, Double> colTong = new TableColumn<>("Tổng tiền");
        colTong.setPrefWidth(150);
        colTong.setCellValueFactory(param -> new ReadOnlyObjectWrapper<Double>(1100111111111.0));
        colTong.setCellFactory(tc -> new TableCell<MonAn, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    Label lbl = new Label(String.format("%,.0fđ", item));
                    lbl.setStyle("""
                        -fx-background-color: #D7F7D3;
                        -fx-background-radius: 3;
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
        tableMonAn.setPrefHeight(400);

        addMonAnTestData(tableMonAn.getItems());

        vboxAll.getChildren().addAll(lblTieuDe, tableMonAn);
        return vboxAll;
    }


    private void addMonAnTestData(ObservableList<MonAn> items) {
        items.addAll(
                new MonAn("MA001", "Cơm gà xào sả ớt", "Món chính", 50000.0, "Món ngon, cay nồng"),
                new MonAn("MA002", "Bò nướng lá lốt", "Món chính", 120000.0, "Thịt bò mềm, thơm"),
                new MonAn("MA003", "Cá kho tộ", "Món chính", 80000.0, "Cá tươi, kho đậm đà"),
                new MonAn("MA004", "Rau củ xào", "Món phụ", 30000.0, "Rau tươi, xào nhanh"),
                new MonAn("MA005", "Nước cam tươi", "Đồ uống", 40000.0, "Cam tươi, ép tại chỗ")
        );
    }
    // Phần bảng hóa đơn 
    private VBox taoBangHoaDon() {
        VBox vbox = new VBox(5);
        vbox.setPrefWidth(600);
        vbox.setPadding(new Insets(10));

        Label lblTitle = new Label("Danh sách hóa đơn test");
        lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");
        taoDanhSachHoaDon();
        TableView<HoaDon> table = new TableView<>(dsHoaDonTest);

        TableColumn<HoaDon, String> colMaHoaDon = new TableColumn<>("Mã hóa đơn");
        colMaHoaDon.setCellValueFactory(new PropertyValueFactory<>("maHoaDon"));
        colMaHoaDon.setPrefWidth(120);
        colMaHoaDon.setStyle("-fx-alignment: CENTER; -fx-font-size: 15px;");

        TableColumn<HoaDon, String> colKhachHang = new TableColumn<>("Khách hàng");
        colKhachHang.setCellValueFactory(new PropertyValueFactory<>("tenKhachHang"));
        colKhachHang.setPrefWidth(300);
        colKhachHang.setStyle("-fx-alignment: CENTER; -fx-font-size: 15px;");

        TableColumn<HoaDon, String> colBan = new TableColumn<>("Bàn");
        colBan.setCellValueFactory(new PropertyValueFactory<>("ban"));
        colBan.setPrefWidth(100);
        colBan.setStyle("-fx-alignment: CENTER; -fx-font-size: 15px;");

        TableColumn<HoaDon, Double> colTongTien = new TableColumn<>("Tổng tiền");
        colTongTien.setCellValueFactory(new PropertyValueFactory<>("tongTien"));
        colTongTien.setPrefWidth(200);
        colTongTien.setStyle("-fx-alignment: CENTER; -fx-font-size: 15px;");
        colTongTien.setCellFactory(tc -> new TableCell<HoaDon, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%,.0fđ", item));
                }
            }
        });

        TableColumn<HoaDon, String> colPhuongThuc = new TableColumn<>("Phương thức");
        colPhuongThuc.setCellValueFactory(new PropertyValueFactory<>("phuongThuc"));
        colPhuongThuc.setPrefWidth(200);

        colPhuongThuc.setCellFactory(tc -> new TableCell<HoaDon, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                {
                    setStyle("-fx-padding: 1 30 1 30");
                }

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    // Tạo label
                    Label lblPhuongThuc = new Label(item);
                    lblPhuongThuc.setStyle(
                            "-fx-text-fill: #29D617;" +
                                    "-fx-font-size: 15px;" +
                                    "-fx-font-weight: bold;"
                    );

                    // Tạo icon
                    ImageView imgTienMat = new ImageView(new Image(getClass().getResource("/img/banking.png").toExternalForm()));
                    imgTienMat.setFitHeight(20);
                    imgTienMat.setFitWidth(20);

                    // Gom icon và text lại
                    HBox hboxPhuongThuc = new HBox(6);
                    hboxPhuongThuc.setAlignment(Pos.CENTER);
                    hboxPhuongThuc.getChildren().addAll(imgTienMat, lblPhuongThuc);

                    // Style của container
                    hboxPhuongThuc.setStyle(
                            "-fx-background-color: #D7F7D3;" +
                                    "-fx-background-radius: 3;" +
                                    "-fx-padding: 3 5 3 5;" +
                                    "-fx-border-color: transparent;"
                    );
                    setGraphic(hboxPhuongThuc);
                }
            }
        });


        TableColumn<HoaDon, String> colNgayTao = new TableColumn<>("Ngày tạo");
        colNgayTao.setCellValueFactory(new PropertyValueFactory<>("ngayTao"));
        colNgayTao.setPrefWidth(200);
        colNgayTao.setStyle("-fx-alignment: CENTER; -fx-font-size: 15px;");

        TableColumn<HoaDon, String> colCoc = new TableColumn<>("Cọc");
        colCoc.setCellValueFactory(new PropertyValueFactory<>("testCheck"));
        colCoc.setPrefWidth(100);
        colCoc.setStyle("-fx-alignment: CENTER;");
        colCoc.setCellFactory(tc -> new TableCell<HoaDon, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    ImageView iconCheck = new ImageView(new Image("/img/check.png"));
                    iconCheck.setFitHeight(20);
                    iconCheck.setFitWidth(20);
                    setGraphic(iconCheck);
                    setText(null);
                } else {
                    ImageView iconCheck = new ImageView(new Image("/img/check.png"));
                    iconCheck.setFitHeight(20);
                    iconCheck.setFitWidth(20);
                    setGraphic(iconCheck);
                    setText(null);
                }
            }
        });


        table.getColumns().addAll(colMaHoaDon, colKhachHang, colBan, colTongTien, colPhuongThuc, colNgayTao, colCoc);
        table.setPrefHeight(250);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        vbox.getChildren().addAll(lblTitle, table);

        return vbox;
    }

    public void taoDanhSachHoaDon() {
        dsHoaDonTest.addAll(new HoaDon("HD001", LocalDateTime.now(), "Đã thanh toán", "Tiền mặt", "Không có", new NhanVien(), new KhachHang())
                ,new HoaDon("HD001", LocalDateTime.now(), "Đã thanh toán", "Tiền mặt", "Không có", new NhanVien(), new KhachHang())
                ,new HoaDon("HD001", LocalDateTime.now(), "Đã thanh toán", "Tiền mặt", "Không có", new NhanVien(), new KhachHang())
                ,new HoaDon("HD001", LocalDateTime.now(), "Đã thanh toán", "Tiền mặt", "Không có", new NhanVien(), new KhachHang())
                ,new HoaDon("HD001", LocalDateTime.now(), "Đã thanh toán", "Tiền mặt", "Không có", new NhanVien(), new KhachHang())
                ,new HoaDon("HD001", LocalDateTime.now(), "Đã thanh toán", "Tiền mặt", "Không có", new NhanVien(), new KhachHang())
                ,new HoaDon("HD001", LocalDateTime.now(), "Đã thanh toán", "Tiền mặt", "Không có", new NhanVien(), new KhachHang())
                ,new HoaDon("HD001", LocalDateTime.now(), "Đã thanh toán", "Tiền mặt", "Không có", new NhanVien(), new KhachHang())
                ,new HoaDon("HD001", LocalDateTime.now(), "Đã thanh toán", "Tiền mặt", "Không có", new NhanVien(), new KhachHang())
                ,new HoaDon("HD001", LocalDateTime.now(), "Đã thanh toán", "Tiền mặt", "Không có", new NhanVien(), new KhachHang())
                ,new HoaDon("HD001", LocalDateTime.now(), "Đã thanh toán", "Tiền mặt", "Không có", new NhanVien(), new KhachHang())
                ,new HoaDon("HD001", LocalDateTime.now(), "Đã thanh toán", "Tiền mặt", "Không có", new NhanVien(), new KhachHang())
                ,new HoaDon("HD001", LocalDateTime.now(), "Đã thanh toán", "Tiền mặt", "Không có", new NhanVien(), new KhachHang())
                ,new HoaDon("HD001", LocalDateTime.now(), "Đã thanh toán", "Tiền mặt", "Không có", new NhanVien(), new KhachHang())
                ,new HoaDon("HD001", LocalDateTime.now(), "Đã thanh toán", "Tiền mặt", "Không có", new NhanVien(), new KhachHang())
                ,new HoaDon("HD001", LocalDateTime.now(), "Đã thanh toán", "Tiền mặt", "Không có", new NhanVien(), new KhachHang()));
    }


}

	