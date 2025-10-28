package gui;

import java.text.DecimalFormat;
import java.time.LocalDate;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import control.Crl_QuanLiHoaDon;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.ChiTietHoaDon;
import entity.NhanVien;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.control.TableView.TableViewSelectionModel;
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
	private Crl_QuanLiHoaDon control;
    // Dữ liệu test cho món ăn
    private ObservableList<String> dsThongTinMonAn = FXCollections.observableArrayList();
    // Dữ liệu test cho hóa đơn
    private ObservableList<HoaDon> dsHoaDon = FXCollections.observableArrayList();
	private TextField txtMaHoaDon;
	private TextField txtKhachHang;
	private TextField txtNhanVien;
	private TextField txtTongTien;
	private TextField txtPhuongThuc;
	private TextField txtTienCoc;
	private TextField txtNgayTao;
	private TextField txtBanTra;
	private TableView tableMonAn;
	private TableView<HoaDon> tableHoaDon;
	private TextField timKiem;


    public Gui_QuanLiHoaDon() {
    		control = new Crl_QuanLiHoaDon();
    		dsHoaDon = control.layDanhSachHoaDon();
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
        this.getStylesheets().add(getClass().getResource("/css/qlkm.css").toExternalForm());
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
        timKiem = new TextField();
        timKiem.setPromptText("Tìm kiếm bằng mã hóa đơn");
        timKiem.getStyleClass().add("timKiem");
        Button nutTimKiem = new Button("Tìm kiếm");
        nutTimKiem.getStyleClass().add("button-timKiem");
        HBox oTimKiem = new HBox(10, timKiem, nutTimKiem);
        oTimKiem.setAlignment(Pos.CENTER_LEFT);
        VBox vboxTimKiem = new VBox(5);
        vboxTimKiem.getChildren().addAll(lblTiemKiem, oTimKiem);

        //CLick nút tìm
        nutTimKiem.setOnAction(e -> {
        		String maHoaDon = timKiem.getText();
        		timHoaDon(maHoaDon);
        });
        
        //Thông tin hóa đơn

        //Ma hóa đơn
        Label lblMaHoaDon = new Label("Mã hóa đơn:");
        lblMaHoaDon.getStyleClass().add("fontTieuDeNho");
        txtMaHoaDon = new TextField();
        txtMaHoaDon.setEditable(false);
        txtMaHoaDon.setPrefWidth(250);
        txtMaHoaDon.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #ecf0f1; -fx-font-size: 15px; -fx-border-color: #bdc3c7;");
        HBox hbox1 = new HBox(lblMaHoaDon, spacer1, txtMaHoaDon );
        hbox1.setPadding(new Insets(5));
        hbox1.setPadding(new Insets(0, 30, 0, 0));
        hbox1.setPrefWidth(250);

        //Khách hang
        Label lblKhachHang = new Label("Khách hàng:");
        lblKhachHang.getStyleClass().add("fontTieuDeNho");
        txtKhachHang = new TextField();
        txtKhachHang.setEditable(false);
        txtKhachHang.setPrefWidth(250);
        txtKhachHang.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #ecf0f1; -fx-font-size: 15px; -fx-border-color: #bdc3c7;");
        HBox hbox2 = new HBox(lblKhachHang, spacer2, txtKhachHang );
        hbox2.setPadding(new Insets(5));
        hbox2.setPadding(new Insets(0, 30, 0, 0));

        //Nhan viên
        Label lblNhanVien = new Label("Nhân viên:");
        lblNhanVien.getStyleClass().add("fontTieuDeNho");
        txtNhanVien = new TextField();
        txtNhanVien.setEditable(false);
        txtNhanVien.setPrefWidth(250);
        txtNhanVien.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #ecf0f1; -fx-font-size: 15px; -fx-border-color: #bdc3c7;");
        HBox hbox3 = new HBox(lblNhanVien, spacer3, txtNhanVien );
        hbox3.setPadding(new Insets(5));
        hbox3.setPadding(new Insets(0, 30, 0, 0));

        //Bàn đặt
        Label lblBanTra = new Label("Bàn trả:");
        lblBanTra.getStyleClass().add("fontTieuDeNho");
        txtBanTra = new TextField();
        txtBanTra.setEditable(false);
        txtBanTra.setPrefWidth(250);
        txtBanTra.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #ecf0f1; -fx-font-size: 15px; -fx-border-color: #bdc3c7;");
        HBox hbox4 = new HBox(lblBanTra, spacer4, txtBanTra );
        hbox4.setPadding(new Insets(5));
        hbox4.setPadding(new Insets(0, 30, 0, 0));

        //Tổng tiền
        Label lblTongTien = new Label("Tổng tiền:");
        lblTongTien.getStyleClass().add("fontTieuDeNho");
        txtTongTien = new TextField();
        txtTongTien.setEditable(false);
        txtTongTien.setPrefWidth(250);
        txtTongTien.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #ecf0f1; -fx-font-size: 15px; -fx-border-color: #bdc3c7;");
        HBox hbox5 = new HBox(lblTongTien, spacer5, txtTongTien );
        hbox5.setPadding(new Insets(5));
        hbox5.setPadding(new Insets(0, 30, 0, 0));

        //Phương thức
        Label lblPhuongThuc = new Label("Phương thức:");
        lblPhuongThuc.getStyleClass().add("fontTieuDeNho");
        txtPhuongThuc = new TextField();
        txtPhuongThuc.setEditable(false);
        txtPhuongThuc.setPrefWidth(250);
        txtPhuongThuc.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #ecf0f1; -fx-font-size: 15px; -fx-border-color: #bdc3c7;");
        HBox hbox6 = new HBox(lblPhuongThuc, spacer6, txtPhuongThuc );
        hbox6.setPadding(new Insets(5));
        hbox6.setPadding(new Insets(0, 30, 0, 0));

        //Tien coc
        Label lblTienCoc = new Label("Tiền cọc:");
        lblTienCoc.getStyleClass().add("fontTieuDeNho");
        txtTienCoc = new TextField();
        txtTienCoc.setEditable(false);
        txtTienCoc.setPrefWidth(250);
        txtTienCoc.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #ecf0f1; -fx-font-size: 15px; -fx-border-color: #bdc3c7;");
        HBox hbox7 = new HBox(lblTienCoc, spacer7, txtTienCoc );
        hbox7.setPadding(new Insets(5));
        hbox7.setPadding(new Insets(0, 30, 0, 0));

        //Ngày tạo
        Label lblNgayTao = new Label("Ngày tạo:");
        lblNgayTao.getStyleClass().add("fontTieuDeNho");
        txtNgayTao = new TextField();
        txtNgayTao.setEditable(false);
        txtNgayTao.setPrefWidth(250);
        txtNgayTao.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #ecf0f1; -fx-font-size: 15px; -fx-border-color: #bdc3c7;");
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

        tableMonAn = new TableView<>();

        // Cột STT
        TableColumn<String, Void> colSTT = new TableColumn<>("STT");
        colSTT.setPrefWidth(50);
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



        colTenMon.setPrefWidth(250);

        // Cột số lượng
        TableColumn<String, Integer> colSoLuong = new TableColumn<>("Số lượng");

        // Ở đây mình đang test giá trị cố định = 111 (có thể thay bằng dữ liệu thật)
        colSoLuong.setCellValueFactory( cellData -> {
        		int soLuong = Integer.parseInt(cellData.getValue().split(",")[1]);
        		return new SimpleIntegerProperty(soLuong).asObject();
        });


        colSoLuong.setPrefWidth(100);


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

        //addMonAnTestData(tableMonAn.getItems());

        vboxAll.getChildren().addAll(lblTieuDe, tableMonAn);
        return vboxAll;
    }

    // Phần bảng hóa đơn 
    private VBox taoBangHoaDon() {
        VBox vbox = new VBox(5);
        vbox.setPrefWidth(600);
        vbox.setPadding(new Insets(10));

        Label lblTitle = new Label("Danh sách hóa đơn");
        lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");
        tableHoaDon = new TableView<>(dsHoaDon);

        TableColumn<HoaDon, String> colMaHoaDon = new TableColumn<>("Mã hóa đơn");
        colMaHoaDon.setCellValueFactory(new PropertyValueFactory<>("maHoaDon"));
        colMaHoaDon.setPrefWidth(120);
        colMaHoaDon.setStyle("-fx-alignment: CENTER; -fx-font-size: 15px;");

        TableColumn<HoaDon, String> colKhachHang = new TableColumn<>("Khách hàng");
        colKhachHang.setCellValueFactory(cellData -> {
            KhachHang kh = cellData.getValue().getKhachHang();
            String tenKH = (kh != null) ? kh.getTenKhachHang() : "Không xác định";
            return new SimpleStringProperty(tenKH);
        });
        
        colKhachHang.setPrefWidth(200);
        colKhachHang.setStyle("-fx-alignment: CENTER; -fx-font-size: 15px;");

        TableColumn<HoaDon, String> colBan = new TableColumn<>("Bàn");
        colBan.setCellValueFactory(cellData -> {
        		String maHoaDon = cellData.getValue().getMaHoaDon();
        		List<String> dsMaBan = control.layDSMaBanBangMaHoaDon(maHoaDon);
        		return new SimpleStringProperty(String.join(", ", dsMaBan));
        });
        colBan.setPrefWidth(100);
        colBan.setStyle("-fx-alignment: CENTER; -fx-font-size: 15px;");

        TableColumn<HoaDon, String> colTongTien = new TableColumn<>("Tổng tiền");
        colTongTien.setCellValueFactory(cellData -> {
        		String maHoaDon = cellData.getValue().getMaHoaDon();
        		double tongTien = control.tinhTongTienHoaDon(maHoaDon);
        		DecimalFormat dtf = new DecimalFormat("#,##0.0 đ");
        		return new SimpleStringProperty(dtf.format(tongTien));
        });
        colTongTien.setPrefWidth(250);
        colTongTien.setStyle("-fx-alignment: CENTER; -fx-font-size: 15px;");

        TableColumn<HoaDon, String> colPhuongThuc = new TableColumn<>("Phương thức");
        colPhuongThuc.setCellValueFactory(new PropertyValueFactory<>("phuongThuc"));
        colPhuongThuc.setPrefWidth(200);

        colPhuongThuc.setCellFactory(tc -> new TableCell<HoaDon, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                setStyle("-fx-padding: 1 30 1 30");

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    // Lấy ra đối tượng HoaDon của dòng hiện tại
                    HoaDon hoaDon = getTableRow().getItem();
                    // Kiểm tra giá trị trong cột hiện tại
                    String phuongThuc = item; // chính là giá trị của cột này

                    Label lblPhuongThuc = new Label(phuongThuc);
                    HBox hbox = new HBox(6);
                    hbox.setAlignment(Pos.CENTER);
                    ImageView icon;

                    if (phuongThuc.equalsIgnoreCase("Tiền mặt")) {
                        lblPhuongThuc.setStyle("-fx-text-fill: #1003FF; -fx-font-size: 15px; -fx-font-weight: bold;");
                        hbox.setStyle("-fx-background-color: #D3CFFF; -fx-background-radius: 3; -fx-padding: 3 5 3 5;");
                        icon = new ImageView(new Image(getClass().getResource("/img/dollar.png").toExternalForm()));
                    } else {
                        lblPhuongThuc.setStyle("-fx-text-fill: #29D617; -fx-font-size: 15px; -fx-font-weight: bold;");
                        hbox.setStyle("-fx-background-color: #D7F7D3; -fx-background-radius: 3; -fx-padding: 3 5 3 5;");
                        icon = new ImageView(new Image(getClass().getResource("/img/banking.png").toExternalForm()));
                    }
                    icon.setFitHeight(20);
                    icon.setFitWidth(20);
                    hbox.getChildren().addAll(icon, lblPhuongThuc);
                                        
                    setGraphic(hbox);

                }
            }
        });


        TableColumn<HoaDon, String> colNgayTao = new TableColumn<>("Ngày tạo");
        colNgayTao.setCellValueFactory(cellData -> {
        		LocalDate ngayTao = cellData.getValue().getNgayTao().toLocalDate();
        		DateTimeFormatter ngayFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        		
        		return new SimpleStringProperty(ngayFormat.format(ngayTao));	
        });
        colNgayTao.setPrefWidth(200);
        colNgayTao.setStyle("-fx-alignment: CENTER; -fx-font-size: 15px;");

        TableColumn<HoaDon, String> colCoc = new TableColumn<>("Cọc");
        colCoc.setCellValueFactory(cellData -> {
        		String maHoaDon = cellData.getValue().getMaHoaDon();
        		double tienCoc = control.tinhTienCoc(maHoaDon);
        		DecimalFormat dtf = new DecimalFormat("#,##0.0 đ");
        		return new SimpleStringProperty(dtf.format(tienCoc));	
        });
        
        colCoc.setPrefWidth(100);
        colCoc.setStyle("-fx-alignment: CENTER;");
       
        tableHoaDon.getColumns().addAll(colMaHoaDon, colKhachHang, colBan, colTongTien, colCoc, colPhuongThuc, colNgayTao);
        tableHoaDon.setPrefHeight(200);
        tableHoaDon.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        //CLICK
        tableHoaDon.setOnMouseClicked(event -> {
            HoaDon hoaDon = tableHoaDon.getSelectionModel().getSelectedItem();
            int selectedIndex = tableHoaDon.getSelectionModel().getSelectedIndex();
            if (hoaDon != null) {
            		txtMaHoaDon.setText(hoaDon.getMaHoaDon());
            		txtNhanVien.setText(hoaDon.getNhanVien().getTenNhanVien());
            		txtTienCoc.setText(colCoc.getCellData(selectedIndex));
            		txtTongTien.setText(colTongTien.getCellData(selectedIndex));
                txtPhuongThuc.setText(hoaDon.getPhuongThuc());
                txtKhachHang.setText(hoaDon.getKhachHang().getTenKhachHang());
                txtTongTien.setText(String.format("%,.0f", control.tinhTongTienHoaDon(hoaDon.getMaHoaDon())));
                txtNgayTao.setText(colNgayTao.getCellData(selectedIndex));
                txtBanTra.setText(colBan.getCellData(selectedIndex));
            }
            
            loadDanhSachMonAn(hoaDon);
            
        });

        vbox.getChildren().addAll(lblTitle, tableHoaDon);

        return vbox;
    }
    
    private void loadDanhSachMonAn(HoaDon hoaDon) {
    	if (hoaDon != null) {
            List<String> ds = control.dsThongTinMonAnTheoMaHD(hoaDon.getMaHoaDon());
            dsThongTinMonAn = FXCollections.observableArrayList(ds);
            tableMonAn.getItems().clear();
            tableMonAn.setItems(dsThongTinMonAn);
        }
    }
    
    private void timHoaDon(String chuoiTim) {
        HoaDon hoaDonTimThay = control.timHoaDonTheoMa(chuoiTim.trim());

        if (hoaDonTimThay == null) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy hóa đơn !");
            timKiem.requestFocus();
            timKiem.selectAll();
            return;
        }

        // Lấy danh sách hiện tại trong bảng
        ObservableList<HoaDon> dsHoaDon = tableHoaDon.getItems();

        // Duyệt để tìm vị trí
        for (int i = 0; i < dsHoaDon.size(); i++) {
            if (dsHoaDon.get(i).getMaHoaDon().equalsIgnoreCase(hoaDonTimThay.getMaHoaDon())) {
                tableHoaDon.getSelectionModel().select(i);
                tableHoaDon.scrollTo(i);
                tableHoaDon.fireEvent(
                        new MouseEvent(MouseEvent.MOUSE_CLICKED,
                            0, 0, 0, 0,
                            MouseButton.PRIMARY, 1,
                            false, false, false, false,
                            true, false, false, true, false, false, null)
                    );
                return;
            }
        }

    }

}

	