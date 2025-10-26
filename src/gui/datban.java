package gui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class datban extends BorderPane {

    public datban(){
        // Khởi tạo giao diện
        initialize();
    }

    // ✅ THÊM METHOD INITIALIZE
    private void initialize() {
        this.setStyle("-fx-background-color: #F7FAFC;");

        // Phần nội dung chính
        HBox mainContent = new HBox();

        VBox phanBenTrai = taoPhanBenTrai();
        Separator separator = new Separator(Orientation.VERTICAL);
        VBox phanBenPhai = taoPhanBenPhai();

        mainContent.getChildren().addAll(phanBenTrai, separator, phanBenPhai);

        // ✅ Set vào BorderPane chính (this)
        this.setCenter(mainContent);

        // ✅ Load CSS nếu có
        try {
            this.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Không tìm thấy CSS: /application/application.css");
        }
    }

    private VBox taoPhanBenTrai() {
        VBox vbox = new VBox(0);
        vbox.setPadding(Insets.EMPTY);
        vbox.setPrefWidth(500); // Cố định chiều rộng
        vbox.setStyle("-fx-background-color: white;");

        VBox boxThongTin = taoThongTinKhachVaDat();

        VBox boxBanDaChon = taoBanDaChon();
        HBox boxTongCoc = taoTongCoc();

        VBox bottomBox = new VBox(15);
        bottomBox.setPadding(new Insets(15, 20, 15, 20));
        bottomBox.getChildren().addAll(boxBanDaChon, boxTongCoc);

        vbox.getChildren().addAll(boxThongTin, bottomBox);
        return vbox;
    }

    private VBox taoThongTinKhachVaDat() {
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
        Region spacer7 = new Region();
        HBox.setHgrow(spacer7, Priority.ALWAYS);
        Region spacer9 = new Region();
        HBox.setHgrow(spacer9, Priority.ALWAYS);
        Region spacer10 = new Region();
        HBox.setHgrow(spacer10, Priority.ALWAYS);

        // Tiêu đề - 1
        Label tieuDeKH = new Label("Thông Tin Khách Hàng");
        tieuDeKH.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");

        // Ma kh
        Label lblMaKH = new Label("Mã khách hàng:");
        lblMaKH.getStyleClass().add("fontTieuDeNho");
        TextField txtMaKh = new TextField("KH000001");
        txtMaKh.setEditable(false);
        txtMaKh.setPrefWidth(250);
        txtMaKh.setStyle(
                "-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 12; -fx-background-color: #D9D9D9; -fx-font-size: 15");
        HBox hbox1 = new HBox(lblMaKH, spacer1, txtMaKh);
        hbox1.setPadding(new Insets(5));

        // Ten kh
        Label lblTenKH = new Label("Tên khách hàng:");
        lblTenKH.getStyleClass().add("fontTieuDeNho");
        TextField txtTenKH = new TextField();
        txtTenKH.setText("Hồ Vạn Thương");
        txtTenKH.setEditable(false);
        txtTenKH.setPrefWidth(250);
        txtTenKH.setPrefWidth(250);
        txtTenKH.setStyle(
                "-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #D9D9D9; -fx-font-size: 15");
        HBox hbox2 = new HBox(lblTenKH, spacer2, txtTenKH);
        hbox2.setPadding(new Insets(5));

        // So dien Thoai
        Label lblSdt = new Label("Số điện thoại:");
        lblSdt.getStyleClass().add("fontTieuDeNho");
        TextField txtSdt = new TextField("0839298272");
        txtSdt.setPrefWidth(250);
        txtSdt.setEditable(false);
        txtSdt.setStyle(
                "-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #D9D9D9; -fx-font-size: 15");
        HBox hbox3 = new HBox(lblSdt, spacer3, txtSdt);
        hbox3.setPadding(new Insets(5));

        // Diem tich luy
        Label lblDiem = new Label("Điểm tích lũy:");
        lblDiem.getStyleClass().add("fontTieuDeNho");
        TextField txtDiem = new TextField("1236");
        txtDiem.setPrefWidth(250);
        txtDiem.setEditable(false);
        txtDiem.setStyle(
                "-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #D9D9D9; -fx-font-size: 15");
        HBox hbox4 = new HBox(lblDiem, spacer4, txtDiem);
        hbox4.setPadding(new Insets(5));

        /// VBOX ALL - 1
        VBox vboxAll1 = new VBox(5);
        vboxAll1.getChildren().addAll(tieuDeKH, hbox1, hbox2, hbox3, hbox4);

        // Tieu de - 2
        Label lblThongTinDatBan = new Label("Thông Tin Đặt Bàn");
        lblThongTinDatBan.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");

        // Ngay gio den
        Label lblNgayGio = new Label("Ngày giờ đến:");
        lblNgayGio.getStyleClass().add("fontTieuDeNho");
        TextField txtGioDen = new TextField();
        TextField txtNgayDen = new TextField();
        txtGioDen.setEditable(false);
        txtNgayDen.setEditable(false);
        txtGioDen.setText("19:00");
        txtNgayDen.setText("23/10/2025");
        txtGioDen.setPrefWidth(80);
        txtGioDen.setStyle(
                "-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #D9D9D9; -fx-font-size: 15; -fx-background-radius: 3 0 0 3");
        txtNgayDen.setStyle(
                "-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-background-color: #D9D9D9; -fx-font-size: 15; -fx-background-radius: 0 3 3 0");
        txtNgayDen.setPrefWidth(170);

        // ✅ Sử dụng createImageViewSafe
        ImageView iconGio = createImageViewSafe("/img/clock.png", 25, 25);
        ImageView iconNgay = createImageViewSafe("/img/calendar.png", 25, 25);

        StackPane stackGio = new StackPane(txtGioDen, iconGio);
        StackPane stackNgay = new StackPane(txtNgayDen, iconNgay);
        StackPane.setAlignment(iconGio, Pos.CENTER_RIGHT);
        StackPane.setAlignment(iconNgay, Pos.CENTER_RIGHT);
        StackPane.setMargin(iconNgay, new Insets(0, 4, 0, 0));

        HBox hboxNgayGio = new HBox(stackGio, stackNgay);

        HBox hbox5 = new HBox(lblNgayGio, spacer5, hboxNgayGio);
        hbox5.setPadding(new Insets(5));

        // So nguoi
        Label lblSoNguoi = new Label("Số người:");
        lblSoNguoi.getStyleClass().add("fontTieuDeNho");
        TextField txtSoNguoi = new TextField("4");
        txtSoNguoi.setEditable(false);
        txtSoNguoi.setPrefWidth(250);
        txtSoNguoi.setStyle(
                "-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #D9D9D9; -fx-font-size: 15; ");
        HBox hbox6 = new HBox(lblSoNguoi, spacer6, txtSoNguoi);
        hbox6.setPadding(new Insets(5));

        // Kiểu đặt bàn
        Label lblKieuDatBan = new Label("Kiểu đặt bàn");
        lblKieuDatBan.getStyleClass().add("fontTieuDeNho");
        RadioButton radioDatTruoc = new RadioButton("Đặt trước");
        RadioButton radioDungNgay = new RadioButton("Dùng ngay");
        ToggleGroup radioGroup = new ToggleGroup();
        radioDatTruoc.setToggleGroup(radioGroup);
        radioDungNgay.setToggleGroup(radioGroup);
        radioDatTruoc.getStyleClass().add("radio-button");
        radioDungNgay.getStyleClass().add("radio-button");

        radioDatTruoc.setSelected(true);

        HBox hboxRadio = new HBox(5);
        hboxRadio.setPrefWidth(250);
        Region spaceRadio = new Region();
        HBox.setHgrow(spaceRadio, Priority.ALWAYS);
        hboxRadio.getChildren().addAll(radioDatTruoc, spaceRadio, radioDungNgay);
        HBox hbox7 = new HBox(lblKieuDatBan, spacer7, hboxRadio);
        hbox7.setPadding(new Insets(5));

        // Vbox all - 2
        VBox vboxAll2 = new VBox(5);
        vboxAll2.getChildren().addAll(lblThongTinDatBan, hbox5, hbox6, hbox7);

        // Vbox All (Chỉ chứa 2 mục theo yêu cầu)
        VBox vboxALL = new VBox(25);
        vboxALL.getChildren().addAll(vboxAll1, vboxAll2);
        vboxALL.setAlignment(Pos.TOP_LEFT);
        vboxALL.setPadding(new Insets(10, 20, 0, 20));
        vboxALL.setMinWidth(500);

        return vboxALL;
    }

    private VBox taoBanDaChon() {
        VBox vbox = new VBox(5);
        Label tieuDe = new Label("Bàn đã chọn");
        tieuDe.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");

        TableView<BanChon> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(200);
        table.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");

        TableColumn<BanChon, String> colMaBan = new TableColumn<>("Mã bàn");
        colMaBan.setCellValueFactory(new PropertyValueFactory<>("maBan"));
        colMaBan.setPrefWidth(90);

        TableColumn<BanChon, String> colLoai = new TableColumn<>("Loại");
        colLoai.setCellValueFactory(new PropertyValueFactory<>("loai"));
        colLoai.setPrefWidth(110);

        TableColumn<BanChon, Integer> colSoNguoi = new TableColumn<>("Số người");
        colSoNguoi.setCellValueFactory(new PropertyValueFactory<>("soNguoi"));
        colSoNguoi.setPrefWidth(80);

        TableColumn<BanChon, String> colCoc = new TableColumn<>("Cọc");
        colCoc.setCellValueFactory(new PropertyValueFactory<>("coc"));
        colCoc.setPrefWidth(150);

        colMaBan.setSortable(false);
        colLoai.setSortable(false);
        colSoNguoi.setSortable(false);
        colCoc.setSortable(false);

        table.getColumns().addAll(colMaBan, colLoai, colSoNguoi, colCoc);

        ObservableList<BanChon> data = FXCollections.observableArrayList(
                new BanChon("Bàn 4", "VIP", 8, "450.000đ"),
                new BanChon("Bàn 5", "VIP", 8, "450.000đ"),
                new BanChon("Bàn 9", "THƯỜNG", 6, "300.000đ")
        );
        table.setItems(data);

        vbox.getChildren().addAll(tieuDe, table);
        return vbox;
    }

    private HBox taoTongCoc() {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        Label lblTieuDe = new Label("Tổng cọc cần thanh toán:");
        lblTieuDe.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblGia = new Label("1.200.000 VNĐ");
        lblGia.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #E53E3E;");

        hbox.getChildren().addAll(lblTieuDe, spacer, lblGia);
        return hbox;
    }

    private VBox taoPhanBenPhai() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(15));
        vbox.setStyle("-fx-background-color: white;");
        HBox.setHgrow(vbox, Priority.ALWAYS);

        VBox boxMenu = taoMenuMonAn();
        VBox boxMonDaChon = taoMonDaChon();
        VBox.setVgrow(boxMonDaChon, Priority.ALWAYS);
        HBox boxTongTienMon = taoTongTienMon();
        HBox boxNutBam = taoNutBam();

        vbox.getChildren().addAll(boxMenu, boxMonDaChon, boxTongTienMon, boxNutBam);

        return vbox;
    }

    private VBox taoMenuMonAn() {
        VBox vbox = new VBox(10);

        Label tieuDe = new Label("Menu món ăn");
        tieuDe.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");

        ComboBox<String> comboLoai = new ComboBox<>();
        comboLoai.getItems().addAll("Tất cả", "Món chính", "Tráng miệng", "Đồ uống");
        comboLoai.setValue("Tất cả");
        comboLoai.setPrefWidth(150);
        comboLoai.getStyleClass().add("combo-box-menu");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(600);
        scrollPane.getStyleClass().add("scroll-pane-menu");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        TilePane tilePane = new TilePane();
        tilePane.setPadding(new Insets(10));
        tilePane.setHgap(15);
        tilePane.setVgap(15);
        tilePane.setPrefColumns(4);

        tilePane.getChildren().add(taoTheMonAn("/img/monAn.png", "Samosa", "Giòn tan khó cưỡng", "36.000đ", 2));
        tilePane.getChildren().add(taoTheMonAn("/img/monAn.png", "Cơm gà chiên tỏi ớt", "Thơm nứt mũi, ăn mê tít", "36.000đ", 2));
        tilePane.getChildren().add(taoTheMonAn("/img/monAn.png", "Pizza", "Phô mai kéo sợi", "136.000đ", 2));
        tilePane.getChildren().add(taoTheMonAn("/img/monAn.png", "Burger", "Burger phô mai béo ngậy", "36.000đ", 2));
        tilePane.getChildren().add(taoTheMonAn("/img/monAn.png", "Samosa", "Giòn tan khó cưỡng", "36.000đ", 0));
        tilePane.getChildren().add(taoTheMonAn("/img/monAn.png", "Samosa", "Giòn tan khó cưỡng", "36.000đ", 0));
        tilePane.getChildren().add(taoTheMonAn("/img/monAn.png", "Samosa", "Giòn tan khó cưỡng", "36.000đ", 0));
        tilePane.getChildren().add(taoTheMonAn("/img/monAn.png", "Samosa", "Giòn tan khó cưỡng", "36.000đ", 0));
        tilePane.getChildren().add(taoTheMonAn("/img/monAn.png", "Samosa", "", "36.000đ", 0));
        tilePane.getChildren().add(taoTheMonAn("/img/monAn.png", "Samosa", "", "36.000đ", 0));
        tilePane.getChildren().add(taoTheMonAn("/img/monAn.png", "Samosa", "", "36.000đ", 0));
        tilePane.getChildren().add(taoTheMonAn("/img/monAn.png", "Samosa", "", "36.000đ", 0));

        scrollPane.setContent(tilePane);

        HBox comboContainer = new HBox(comboLoai);
        comboContainer.setPadding(new Insets(0, 0, 0, 5));

        vbox.getChildren().addAll(tieuDe, comboContainer, scrollPane);
        return vbox;
    }


    private VBox taoTheMonAn(String imgPath, String tenMon, String moTa, String gia, int soLuong) {
        VBox vbox = new VBox(5);
        vbox.setPrefWidth(155);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));
        vbox.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);"
        );

        // ✅ Sử dụng createImageViewSafe
        ImageView imgView = createImageViewSafe(imgPath, 130, 110);
        imgView.setPreserveRatio(true);
        vbox.getChildren().add(imgView);

        Label lblTen = new Label(tenMon);
        lblTen.setWrapText(true);
        lblTen.setStyle("-fx-font-weight: 900; -fx-font-size: 15px; -fx-font-family: 'Times New Roman'; -fx-alignment: CENTER;");
        lblTen.setPrefWidth(140);
        lblTen.setMinHeight(35);

        Label lblMoTa = new Label(moTa);
        lblMoTa.setWrapText(true);
        lblMoTa.setStyle("-fx-text-fill: #718096; -fx-font-size: 12px; -fx-alignment: CENTER;");
        lblMoTa.setPrefWidth(140);
        lblMoTa.setMinHeight(30);

        if (moTa == null || moTa.isEmpty()) {
            lblMoTa.setVisible(false);
        }

        Label lblGia = new Label(gia);
        lblGia.setStyle("-fx-text-fill: #E53E3E; -fx-font-weight: bold; -fx-font-size: 18px; -fx-alignment: CENTER;");
        lblGia.setPrefWidth(140);

        Button btnTru = new Button("−");
        btnTru.getStyleClass().add("button-dieu-chinh-menu");

        Label lblSoLuong = new Label(String.valueOf(soLuong));
        lblSoLuong.setPadding(new Insets(0, 10, 0, 10));
        lblSoLuong.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");

        Button btnCong = new Button("+");
        btnCong.getStyleClass().add("button-dieu-chinh-menu");

        HBox soLuongBox = new HBox(10, btnCong, lblSoLuong, btnTru);
        soLuongBox.setAlignment(Pos.CENTER);
        soLuongBox.setPadding(new Insets(5, 0, 0, 0));

        vbox.getChildren().addAll(lblTen, lblMoTa, lblGia, soLuongBox);
        return vbox;
    }

    private VBox taoMonDaChon() {
        VBox vbox = new VBox(10);
        Label tieuDe = new Label("Món đã chọn");
        tieuDe.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");

        TableView<MonChon> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<MonChon, String> colTen = new TableColumn<>("Tên món");
        colTen.setCellValueFactory(new PropertyValueFactory<>("tenMon"));
        colTen.setPrefWidth(200);

        TableColumn<MonChon, Integer> colSoLuong = new TableColumn<>("Số lượng");
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        colSoLuong.setPrefWidth(80);

        TableColumn<MonChon, String> colDonGia = new TableColumn<>("Đơn giá");
        colDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
        colDonGia.setPrefWidth(120);

        TableColumn<MonChon, String> colTong = new TableColumn<>("Tổng");
        colTong.setCellValueFactory(new PropertyValueFactory<>("tong"));
        colTong.setPrefWidth(120);

        table.getColumns().addAll(colTen, colSoLuong, colDonGia, colTong);

        ObservableList<MonChon> data = FXCollections.observableArrayList(
                new MonChon("Samosa", 2, "69.000đ", "138.000đ"),
                new MonChon("Cơm gà chiên tỏi ớt", 2, "39.000đ", "78.000đ"),
                new MonChon("Pizza", 2, "169.000đ", "338.000đ"),
                new MonChon("Burger", 2, "89.000đ", "178.000đ")
        );
        table.setItems(data);

        vbox.getChildren().addAll(tieuDe, table);
        return vbox;
    }

    private HBox taoTongTienMon() {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        Label lblTieuDe = new Label("Tổng tiền đặt món:");
        lblTieuDe.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblGia = new Label("732.000 VNĐ");
        lblGia.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2D3748;");

        hbox.getChildren().addAll(lblTieuDe, spacer, lblGia);
        return hbox;
    }

    private HBox taoNutBam() {
        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnQuayLai = new Button("Quay lại");
        btnQuayLai.setPrefSize(150, 40);
        btnQuayLai.getStyleClass().add("button-checkin");
        btnQuayLai.setStyle("-fx-background-color: #A0AEC0;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 20;" +
                "-fx-text-fill: gray;");

        Button btnXacNhan = new Button("Xác nhận đặt bàn");
        btnXacNhan.setPrefSize(200, 40);
        btnXacNhan.getStyleClass().add("button-checkin");
        btnXacNhan.setStyle("-fx-background-color: #2D3748; -fx-text-fill: white; -fx-font-weight: bold;");

        hbox.getChildren().addAll(spacer, btnQuayLai, btnXacNhan);
        return hbox;
    }

    // ✅ THÊM METHOD TẠO IMAGEVIEW AN TOÀN
    private ImageView createImageViewSafe(String path, double width, double height) {
        try {
            Image img = new Image(getClass().getResourceAsStream(path));
            ImageView imgView = new ImageView(img);
            imgView.setFitWidth(width);
            imgView.setFitHeight(height);
            return imgView;
        } catch (Exception e) {
            System.err.println("Không tìm thấy ảnh: " + path);
            // Tạo placeholder
            ImageView placeholder = new ImageView();
            placeholder.setFitWidth(width);
            placeholder.setFitHeight(height);
            placeholder.setStyle("-fx-background-color: #CBD5E0;");
            return placeholder;
        }
    }

    // Inner classes
    public static class BanChon {
        private String maBan;
        private String loai;
        private int soNguoi;
        private String coc;

        public BanChon(String maBan, String loai, int soNguoi, String coc) {
            this.maBan = maBan;
            this.loai = loai;
            this.soNguoi = soNguoi;
            this.coc = coc;
        }

        public String getMaBan() { return maBan; }
        public String getLoai() { return loai; }
        public int getSoNguoi() { return soNguoi; }
        public String getCoc() { return coc; }
    }

    public static class MonChon {
        private String tenMon;
        private int soLuong;
        private String donGia;
        private String tong;

        public MonChon(String tenMon, int soLuong, String donGia, String tong) {
            this.tenMon = tenMon;
            this.soLuong = soLuong;
            this.donGia = donGia;
            this.tong = tong;
        }

        public String getTenMon() { return tenMon; }
        public int getSoLuong() { return soLuong; }
        public String getDonGia() { return donGia; }
        public String getTong() { return tong; }
    }
}