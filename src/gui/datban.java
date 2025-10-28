package gui;

import java.util.List;
import java.util.ArrayList;
import java.text.DecimalFormat;

// Imports cho DAO và Entity
import dao.KhachHang_DAO;
import dao.MonAn_DAO;
import dao.BanAn_DAO; // Import BanAn_DAO
import entity.KhachHang;
import entity.MonAn;
import entity.BanAn;
import entity.TrangThai; // Import TrangThai

// Imports cho JavaFX
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text; // Import Text

public class datban extends BorderPane {

	// DAO
	private MonAn_DAO monAnDAO;
	private KhachHang_DAO khachHangDAO;
	private BanAn_DAO banAn_DAO;

	// ObservableLists cho TableView
	private ObservableList<BanChon> dsBanDaChon;
	private ObservableList<MonChon> dsMonDaChon;

	// Biến toàn cục
	private final DecimalFormat df = new DecimalFormat("###,###đ");
	private BorderPane mainLayout;
	private List<BanAn> cacBanDuocChon; // Danh sách các bàn được chọn

	// Các UI components cần cập nhật
	private Label lblTongCoc;
	private Label lblTongTienMon;
	private TilePane menuTilePane;

	/**
	 * Constructor nhận mainLayout và Danh sách các bàn đã chọn
	 */
	public datban(BorderPane mainLayout, List<BanAn> cacBanDaChon) {
		// Kiểm tra null phòng trường hợp Gui_DanhSachBan gửi null
		if (cacBanDaChon == null) {
			this.cacBanDuocChon = new ArrayList<>(); // Khởi tạo list rỗng
			System.err.println("Cảnh báo: Danh sách bàn chọn bị null!");
		} else {
			this.cacBanDuocChon = cacBanDaChon;
		}
		this.mainLayout = mainLayout;

		// Khởi tạo DAO và List
		monAnDAO = new MonAn_DAO();
		khachHangDAO = new KhachHang_DAO();
		banAn_DAO = new BanAn_DAO(); // Khởi tạo BanAn_DAO

		dsBanDaChon = FXCollections.observableArrayList();
		dsMonDaChon = FXCollections.observableArrayList();

		// Dựng giao diện
		initialize();

		// Tải dữ liệu các bàn đã chọn lên bảng
		taiDuLieuBanDau();
	}

	/**
	 * Hàm dựng UI chính
	 */
	private void initialize() {
		this.setStyle("-fx-background-color: #F7FAFC;");
		HBox mainContent = new HBox();
		VBox phanBenTrai = taoPhanBenTrai();
		Separator separator = new Separator(Orientation.VERTICAL);
		VBox phanBenPhai = taoPhanBenPhai();
		mainContent.getChildren().addAll(phanBenTrai, separator, phanBenPhai);
		this.setCenter(mainContent);
		try {
			this.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
		} catch (Exception e) {
			System.err.println("Không tìm thấy CSS: /application/application.css");
		}
	}

	// =================================================================================
	// PHẦN BÊN TRÁI (THÔNG TIN KHÁCH + BÀN CHỌN)
	// =================================================================================

	private VBox taoPhanBenTrai() {
		VBox vbox = new VBox(0);
		vbox.setPadding(Insets.EMPTY);
		vbox.setPrefWidth(500);
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
		Region spacer1 = new Region(); HBox.setHgrow(spacer1, Priority.ALWAYS);
		Region spacer2 = new Region(); HBox.setHgrow(spacer2, Priority.ALWAYS);
		Region spacer3 = new Region(); HBox.setHgrow(spacer3, Priority.ALWAYS);
		Region spacer4 = new Region(); HBox.setHgrow(spacer4, Priority.ALWAYS);
		Region spacer5 = new Region(); HBox.setHgrow(spacer5, Priority.ALWAYS);
		Region spacer6 = new Region(); HBox.setHgrow(spacer6, Priority.ALWAYS);
		Region spacer7 = new Region(); HBox.setHgrow(spacer7, Priority.ALWAYS);

		// Phần Thông Tin Khách Hàng
		Label tieuDeKH = new Label("Thông Tin Khách Hàng");
		tieuDeKH.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");
		Label lblMaKH = new Label("Mã khách hàng:");
		lblMaKH.getStyleClass().add("fontTieuDeNho");
		TextField txtMaKh = new TextField();
		txtMaKh.setEditable(false); txtMaKh.setPrefWidth(250);
		txtMaKh.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 12; -fx-background-color: #D9D9D9; -fx-font-size: 15");
		HBox hbox1 = new HBox(lblMaKH, spacer1, txtMaKh); hbox1.setPadding(new Insets(5));
		Label lblTenKH = new Label("Tên khách hàng:");
		lblTenKH.getStyleClass().add("fontTieuDeNho");
		TextField txtTenKH = new TextField();
		txtTenKH.setEditable(false); txtTenKH.setPrefWidth(250);
		txtTenKH.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #D9D9D9; -fx-font-size: 15");
		HBox hbox2 = new HBox(lblTenKH, spacer2, txtTenKH); hbox2.setPadding(new Insets(5));
		Label lblSdt = new Label("Số điện thoại:");
		lblSdt.getStyleClass().add("fontTieuDeNho");
		TextField txtSdt = new TextField();
		txtSdt.setEditable(false); txtSdt.setPrefWidth(250);
		txtSdt.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #D9D9D9; -fx-font-size: 15");
		HBox hbox3 = new HBox(lblSdt, spacer3, txtSdt); hbox3.setPadding(new Insets(5));
		Label lblDiem = new Label("Điểm tích lũy:");
		lblDiem.getStyleClass().add("fontTieuDeNho");
		TextField txtDiem = new TextField();
		txtDiem.setEditable(false); txtDiem.setPrefWidth(250);
		txtDiem.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #D9D9D9; -fx-font-size: 15");
		HBox hbox4 = new HBox(lblDiem, spacer4, txtDiem); hbox4.setPadding(new Insets(5));

		String maKhachHangHienTai = "KH001"; // Logic lấy KH cần thay đổi
		KhachHang kh = khachHangDAO.getKhachHangById(maKhachHangHienTai);
		if (kh != null) {
			txtMaKh.setText(kh.getMaKhachHang());
			txtTenKH.setText(kh.getTenKhachHang());
			txtSdt.setText(kh.getSoDienThoai());
			txtDiem.setText(String.format("%.0f", kh.getDiemTichLuy()));
		} else {
			txtMaKh.setText("N/A"); txtTenKH.setText("Khách vãng lai");
			txtSdt.setText(""); txtDiem.setText("0");
		}
		VBox vboxAll1 = new VBox(5, tieuDeKH, hbox1, hbox2, hbox3, hbox4);

		// Phần Thông Tin Đặt Bàn
		Label lblThongTinDatBan = new Label("Thông Tin Đặt Bàn");
		lblThongTinDatBan.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");
		Label lblNgayGio = new Label("Ngày giờ đến:");
		lblNgayGio.getStyleClass().add("fontTieuDeNho");
		TextField txtGioDen = new TextField(); txtGioDen.setEditable(false); txtGioDen.setPrefWidth(80);
		TextField txtNgayDen = new TextField(); txtNgayDen.setEditable(false); txtNgayDen.setPrefWidth(170);
		txtGioDen.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #D9D9D9; -fx-font-size: 15; -fx-background-radius: 3 0 0 3");
		txtNgayDen.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-background-color: #D9D9D9; -fx-font-size: 15; -fx-background-radius: 0 3 3 0");

		// -- Bỏ qua phần hình ảnh theo yêu cầu --
		// ImageView iconGio = createImageViewSafe("/img/clock.png", 25, 25);
		// ImageView iconNgay = createImageViewSafe("/img/calendar.png", 25, 25);
		// StackPane stackGio = new StackPane(txtGioDen, iconGio);
		// StackPane stackNgay = new StackPane(txtNgayDen, iconNgay);
		// StackPane.setAlignment(iconGio, Pos.CENTER_RIGHT);
		// StackPane.setAlignment(iconNgay, Pos.CENTER_RIGHT);
		// StackPane.setMargin(iconNgay, new Insets(0, 4, 0, 0));
		HBox hboxNgayGio = new HBox(txtGioDen, txtNgayDen); // Chỉ hiển thị TextField
		// -- Hết phần bỏ qua hình ảnh --

		HBox hbox5 = new HBox(lblNgayGio, spacer5, hboxNgayGio); hbox5.setPadding(new Insets(5));
		Label lblSoNguoi = new Label("Số người:");
		lblSoNguoi.getStyleClass().add("fontTieuDeNho");
		TextField txtSoNguoi = new TextField("4"); txtSoNguoi.setEditable(false); txtSoNguoi.setPrefWidth(250); // Logic nhập số người cần thêm
		txtSoNguoi.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro';  -fx-background-color: #D9D9D9; -fx-font-size: 15; ");
		HBox hbox6 = new HBox(lblSoNguoi, spacer6, txtSoNguoi); hbox6.setPadding(new Insets(5));
		Label lblKieuDatBan = new Label("Kiểu đặt bàn");
		lblKieuDatBan.getStyleClass().add("fontTieuDeNho");
		RadioButton radioDatTruoc = new RadioButton("Đặt trước");
		RadioButton radioDungNgay = new RadioButton("Dùng ngay");
		ToggleGroup radioGroup = new ToggleGroup();
		radioDatTruoc.setToggleGroup(radioGroup); radioDungNgay.setToggleGroup(radioGroup);
		radioDatTruoc.getStyleClass().add("radio-button"); radioDungNgay.getStyleClass().add("radio-button");
		radioDatTruoc.setSelected(true);
		HBox hboxRadio = new HBox(5); hboxRadio.setPrefWidth(250);
		Region spaceRadio = new Region(); HBox.setHgrow(spaceRadio, Priority.ALWAYS);
		hboxRadio.getChildren().addAll(radioDatTruoc, spaceRadio, radioDungNgay);
		HBox hbox7 = new HBox(lblKieuDatBan, spacer7, hboxRadio); hbox7.setPadding(new Insets(5));
		VBox vboxAll2 = new VBox(5, lblThongTinDatBan, hbox5, hbox6, hbox7);

		// Kết hợp 2 phần
		VBox vboxALL = new VBox(25, vboxAll1, vboxAll2);
		vboxALL.setAlignment(Pos.TOP_LEFT);
		vboxALL.setPadding(new Insets(10, 20, 0, 20));
		vboxALL.setMinWidth(500);
		return vboxALL;
	}

	// Hàm createImageViewSafe đã được comment out theo yêu cầu
	/*
	private ImageView createImageViewSafe(String path, double width, double height) {
		try {
			Image img = new Image(getClass().getResourceAsStream(path));
			if (img.isError()) {
			    throw new Exception("Lỗi load ảnh: " + img.getException());
			}
			ImageView imgView = new ImageView(img);
			imgView.setFitWidth(width);
			imgView.setFitHeight(height);
			return imgView;
		} catch (Exception e) {
			System.err.println("Không tìm thấy hoặc lỗi load ảnh: " + path + " - " + e.getMessage());
			ImageView placeholder = new ImageView();
			placeholder.setFitWidth(width);
			placeholder.setFitHeight(height);
			Pane pane = new Pane();
			pane.setStyle("-fx-background-color: #CBD5E0; -fx-border-color: #A0AEC0;");
			Text text = new Text("Ảnh lỗi");
			text.setFill(Color.web("#718096"));
			StackPane stack = new StackPane(pane, text);
			stack.setPrefSize(width, height);
			placeholder.setImage(stack.snapshot(null, null));
			return placeholder;
		}
	}
	*/
	// ---- Thay thế bằng hàm trả về null để tránh lỗi ----
	private ImageView createImageViewSafe(String path, double width, double height) {
		// Tạm thời trả về null hoặc một Node rỗng
		// System.err.println("Tạm thời bỏ qua load ảnh: " + path);
		return null; // Hoặc return new ImageView();
	}
	// ---------------------------------------------------


	private VBox taoBanDaChon() {
		VBox vbox = new VBox(5);
		Label tieuDe = new Label("Bàn đã chọn");
		tieuDe.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");
		TableView<BanChon> table = new TableView<>();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setPrefHeight(200);
		table.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
		TableColumn<BanChon, String> colMaBan = new TableColumn<>("Mã bàn");
		colMaBan.setCellValueFactory(new PropertyValueFactory<>("maBan")); colMaBan.setPrefWidth(90);
		TableColumn<BanChon, String> colLoai = new TableColumn<>("Loại");
		colLoai.setCellValueFactory(new PropertyValueFactory<>("loai")); colLoai.setPrefWidth(110);
		TableColumn<BanChon, Integer> colSoNguoi = new TableColumn<>("Số người");
		colSoNguoi.setCellValueFactory(new PropertyValueFactory<>("soNguoi")); colSoNguoi.setPrefWidth(80);
		TableColumn<BanChon, String> colCoc = new TableColumn<>("Cọc");
		colCoc.setCellValueFactory(new PropertyValueFactory<>("coc")); colCoc.setPrefWidth(150);
		table.getColumns().addAll(colMaBan, colLoai, colSoNguoi, colCoc);
		table.setItems(dsBanDaChon);
		vbox.getChildren().addAll(tieuDe, table);
		return vbox;
	}

	private HBox taoTongCoc() {
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER_LEFT);
		Label lblTieuDe = new Label("Tổng cọc cần thanh toán:");
		lblTieuDe.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");
		Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
		lblTongCoc = new Label("0đ");
		lblTongCoc.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #E53E3E;");
		hbox.getChildren().addAll(lblTieuDe, spacer, lblTongCoc);
		return hbox;
	}

	private void taiDuLieuBanDau() {
		int soNguoi = 4; // Logic nhập số người cần thêm
		if (cacBanDuocChon != null) {
			for (BanAn ban : cacBanDuocChon) {
				dsBanDaChon.add(new BanChon(
						ban.getMaBan(), ban.getLoai().getTenLoai(),
						soNguoi, ban.getLoai().getTienCoc()
				));
			}
		}
		capNhatTongCoc();
	}

	private void capNhatTongCoc() {
		double tong = 0;
		for (BanChon ban : dsBanDaChon) {
			tong += ban.getCocValue();
		}
		if (lblTongCoc != null) { // Kiểm tra null trước khi set text
			lblTongCoc.setText(df.format(tong));
		}
	}

	// =================================================================================
	// PHẦN BÊN PHẢI (MENU MÓN ĂN + MÓN CHỌN)
	// =================================================================================

	private VBox taoPhanBenPhai() {
		VBox vbox = new VBox(15);
		vbox.setPadding(new Insets(15));
		vbox.setStyle("-fx-background-color: white;");
		HBox.setHgrow(vbox, Priority.ALWAYS);
		VBox boxMenu = taoMenuMonAn();
		VBox boxMonDaChon = taoMonDaChon(); VBox.setVgrow(boxMonDaChon, Priority.ALWAYS);
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
		comboLoai.getItems().addAll("Tất cả", "Món ăn kèm", "Món khai vị", "Món chính", "Nước sốt", "Đồ uống", "Tráng miệng");
		comboLoai.setValue("Tất cả"); comboLoai.setPrefWidth(150);
		comboLoai.getStyleClass().add("combo-box-menu");
		comboLoai.setOnAction(e -> taiLaiDanhSachMonAn(comboLoai.getValue()));
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setFitToWidth(true); scrollPane.setPrefHeight(600);
		scrollPane.getStyleClass().add("scroll-pane-menu");
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		menuTilePane = new TilePane();
		menuTilePane.setPadding(new Insets(10)); menuTilePane.setHgap(15);
		menuTilePane.setVgap(15); menuTilePane.setPrefColumns(4);
		scrollPane.setContent(menuTilePane);
		HBox comboContainer = new HBox(comboLoai);
		comboContainer.setPadding(new Insets(0, 0, 0, 5));
		vbox.getChildren().addAll(tieuDe, comboContainer, scrollPane);
		taiLaiDanhSachMonAn("Tất cả"); // Tải lần đầu
		return vbox;
	}

	private void taiLaiDanhSachMonAn(String loaiMon) {
		menuTilePane.getChildren().clear();
		List<MonAn> dsMonAn;
		if (loaiMon == null || loaiMon.equals("Tất cả")) {
			dsMonAn = monAnDAO.getAllMonAn();
		} else {
			dsMonAn = monAnDAO.getMonAnByLoai(loaiMon);
		}
		for (MonAn mon : dsMonAn) {
			int soLuongHienTai = 0;
			MonChon monDaChon = timMonChon(mon);
			if (monDaChon != null) {
				soLuongHienTai = monDaChon.getSoLuong();
			}
			menuTilePane.getChildren().add(taoTheMonAn(mon, soLuongHienTai));
		}
	}

	private VBox taoTheMonAn(MonAn mon, int soLuong) {
		VBox vbox = new VBox(5);
		vbox.setPrefWidth(155); vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(10));
		vbox.setStyle("-fx-background-color: white;-fx-background-radius: 20;-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");

		// -- Bỏ qua phần hình ảnh theo yêu cầu --
		// ImageView imgView = createImageViewSafe(mon.getImgPath(), 130, 110); // Giả sử có getImgPath()
		// if (imgView != null) {
		//     imgView.setPreserveRatio(true);
		//     vbox.getChildren().add(imgView);
		// }
		// -- Hết phần bỏ qua hình ảnh --

		Label lblTen = new Label(mon.getTenMonAn());
		lblTen.setWrapText(true); lblTen.setStyle("-fx-font-weight: 900; -fx-font-size: 15px; -fx-font-family: 'Times New Roman'; -fx-alignment: CENTER;");
		lblTen.setPrefWidth(140); lblTen.setMinHeight(35);
		Label lblMoTa = new Label(mon.getMoTa());
		lblMoTa.setWrapText(true); lblMoTa.setStyle("-fx-text-fill: #718096; -fx-font-size: 12px; -fx-alignment: CENTER;");
		lblMoTa.setPrefWidth(140); lblMoTa.setMinHeight(30);
		if (mon.getMoTa() == null || mon.getMoTa().isEmpty()) {
			lblMoTa.setVisible(false); lblMoTa.setManaged(false);
		}
		Label lblGia = new Label(df.format(mon.getGiaTien()));
		lblGia.setStyle("-fx-text-fill: #E53E3E; -fx-font-weight: bold; -fx-font-size: 18px; -fx-alignment: CENTER;");
		lblGia.setPrefWidth(140);
		Button btnTru = new Button("−"); btnTru.getStyleClass().add("button-dieu-chinh-menu");
		Label lblSoLuong = new Label(String.valueOf(soLuong));
		lblSoLuong.setPadding(new Insets(0, 10, 0, 10)); lblSoLuong.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
		Button btnCong = new Button("+"); btnCong.getStyleClass().add("button-dieu-chinh-menu");

		btnCong.setOnAction(e -> {
			themMonVaoGio(mon);
			MonChon monSauKhiThem = timMonChon(mon);
			if (monSauKhiThem != null) lblSoLuong.setText(String.valueOf(monSauKhiThem.getSoLuong()));
		});
		btnTru.setOnAction(e -> {
			botMonKhoiGio(mon);
			MonChon monConLai = timMonChon(mon);
			lblSoLuong.setText(String.valueOf(monConLai != null ? monConLai.getSoLuong() : 0));
		});

		HBox soLuongBox = new HBox(10, btnCong, lblSoLuong, btnTru);
		soLuongBox.setAlignment(Pos.CENTER); soLuongBox.setPadding(new Insets(5, 0, 0, 0));
		vbox.getChildren().addAll(lblTen, lblMoTa, lblGia, soLuongBox);
		return vbox;
	}

	// =================================================================================
	// LOGIC GIỎ HÀNG (MÓN CHỌN)
	// =================================================================================

	private MonChon timMonChon(MonAn mon) {
		if (mon == null) return null;
		for (MonChon mc : dsMonDaChon) {
			if (mc.getMonAn() != null && mc.getMonAn().getMaMonAn().equals(mon.getMaMonAn())) {
				return mc;
			}
		}
		return null;
	}

	private void themMonVaoGio(MonAn mon) {
		if (mon == null) return;
		MonChon monCoSan = timMonChon(mon);
		if (monCoSan != null) {
			monCoSan.tangSoLuong();
			dsMonDaChon.set(dsMonDaChon.indexOf(monCoSan), monCoSan); // Cập nhật để TableView refresh
		} else {
			dsMonDaChon.add(new MonChon(mon, 1));
		}
		capNhatTongTienMon();
	}

	private void botMonKhoiGio(MonAn mon) {
		if (mon == null) return;
		MonChon monCoSan = timMonChon(mon);
		if (monCoSan != null) {
			monCoSan.giamSoLuong();
			if (monCoSan.getSoLuong() == 0) {
				dsMonDaChon.remove(monCoSan);
			} else {
				dsMonDaChon.set(dsMonDaChon.indexOf(monCoSan), monCoSan); // Cập nhật để TableView refresh
			}
			capNhatTongTienMon();
		}
	}

	private void capNhatTongTienMon() {
		double tong = 0;
		for (MonChon mc : dsMonDaChon) {
			tong += mc.getTongValue();
		}
		if (lblTongTienMon != null) {
			lblTongTienMon.setText(df.format(tong));
		}
	}

	private VBox taoMonDaChon() {
		VBox vbox = new VBox(10);
		Label tieuDe = new Label("Món đã chọn");
		tieuDe.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");
		TableView<MonChon> table = new TableView<>();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		VBox.setVgrow(table, Priority.ALWAYS);
		TableColumn<MonChon, String> colTen = new TableColumn<>("Tên món");
		colTen.setCellValueFactory(new PropertyValueFactory<>("tenMon")); colTen.setPrefWidth(200);
		TableColumn<MonChon, Integer> colSoLuong = new TableColumn<>("Số lượng");
		colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong")); colSoLuong.setPrefWidth(80);
		TableColumn<MonChon, String> colDonGia = new TableColumn<>("Đơn giá");
		colDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia")); colDonGia.setPrefWidth(120);
		TableColumn<MonChon, String> colTong = new TableColumn<>("Tổng");
		colTong.setCellValueFactory(new PropertyValueFactory<>("tong")); colTong.setPrefWidth(120);
		table.getColumns().addAll(colTen, colSoLuong, colDonGia, colTong);
		table.setItems(dsMonDaChon);
		vbox.getChildren().addAll(tieuDe, table);
		return vbox;
	}

	private HBox taoTongTienMon() {
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER_LEFT);
		Label lblTieuDe = new Label("Tổng tiền đặt món:");
		lblTieuDe.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: gray");
		Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
		lblTongTienMon = new Label("0đ");
		lblTongTienMon.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2D3748;");
		hbox.getChildren().addAll(lblTieuDe, spacer, lblTongTienMon);
		return hbox;
	}

	// =================================================================================
	// NÚT BẤM VÀ CÁC HÀM HỖ TRỢ
	// =================================================================================

	private HBox taoNutBam() {
		HBox hbox = new HBox(10);
		hbox.setAlignment(Pos.CENTER_RIGHT);
		Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
		Button btnQuayLai = new Button("Quay lại");
		btnQuayLai.setPrefSize(150, 40); btnQuayLai.getStyleClass().add("button-checkin");
		btnQuayLai.setStyle("-fx-background-color: #A0AEC0;-fx-font-weight: bold;-fx-font-size: 20;-fx-text-fill: gray;");
		Button btnXacNhan = new Button("Xác nhận đặt bàn");
		btnXacNhan.setPrefSize(200, 40); btnXacNhan.getStyleClass().add("button-checkin");
		btnXacNhan.setStyle("-fx-background-color: #2D3748; -fx-text-fill: white; -fx-font-weight: bold;");

		btnQuayLai.setOnAction(e -> {
			mainLayout.setCenter(new Gui_DanhSachBan(mainLayout));
		});

		btnXacNhan.setOnAction(e -> {
			try {
				boolean updateSuccess = true;
				if (cacBanDuocChon == null || cacBanDuocChon.isEmpty()) {
					showAlert(AlertType.WARNING, "Lỗi", "Không có bàn nào được chọn để cập nhật.");
					return;
				}
				for (BanAn ban : cacBanDuocChon) {
					boolean result = banAn_DAO.updateTrangThaiBan(ban, TrangThai.DA_DAT);
					if (!result) {
						updateSuccess = false;
						System.err.println("Lỗi cập nhật trạng thái cho bàn: " + ban.getMaBan());
					}
				}
				if (updateSuccess) {
					// (Tương lai: Lưu phiếu đặt bàn, chi tiết món...)
					showAlert(AlertType.INFORMATION, "Thành công", "Đã đặt bàn thành công!");
					mainLayout.setCenter(new Gui_DanhSachBan(mainLayout));
				} else {
					showAlert(AlertType.ERROR, "Lỗi CSDL", "Không thể cập nhật trạng thái cho một (hoặc nhiều) bàn.");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				showAlert(AlertType.ERROR, "Lỗi nghiêm trọng", "Đã xảy ra lỗi: " + ex.getMessage());
			}
		});

		hbox.getChildren().addAll(spacer, btnQuayLai, btnXacNhan);
		return hbox;
	}

	private void showAlert(AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title); alert.setHeaderText(null);
		alert.setContentText(content); alert.showAndWait();
	}


	public static class BanChon {
		private String maBan; private String loai;
		private int soNguoi; private double cocValue;
		public BanChon(String maBan, String loai, int soNguoi, double cocValue) {
			this.maBan = maBan; this.loai = loai;
			this.soNguoi = soNguoi; this.cocValue = cocValue;
		}
		public String getMaBan() { return maBan; }
		public String getLoai() { return loai; }
		public int getSoNguoi() { return soNguoi; }
		public double getCocValue() { return cocValue; }
		public String getCoc() { return new DecimalFormat("###,###đ").format(cocValue); }
	}

	public static class MonChon {
		private MonAn monAn; private int soLuong;
		public MonChon(MonAn monAn, int soLuong) { this.monAn = monAn; this.soLuong = soLuong; }
		public MonAn getMonAn() { return monAn; }
		public int getSoLuong() { return soLuong; }
		public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
		public void tangSoLuong() { this.soLuong++; }
		public void giamSoLuong() { if (this.soLuong > 0) this.soLuong--; }
		public String getTenMon() { return (monAn != null) ? monAn.getTenMonAn() : "Lỗi món"; }
		public String getDonGia() { return (monAn != null) ? new DecimalFormat("###,###đ").format(monAn.getGiaTien()) : "0đ"; }
		public double getTongValue() { return (monAn != null) ? monAn.getGiaTien() * soLuong : 0; }
		public String getTong() { return new DecimalFormat("###,###đ").format(getTongValue()); }
	}
}