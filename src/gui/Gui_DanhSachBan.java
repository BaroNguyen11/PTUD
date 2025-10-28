package gui;

import dao.BanAn_DAO;
import entity.BanAn;
import entity.LoaiBan;
import entity.TrangThai;
import entity.ViTri;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Separator;
import javafx.util.StringConverter;

public class Gui_DanhSachBan extends BorderPane {

	private GridPane luoiBan;
	private BanAn_DAO banAn_DAO;

	private BorderPane mainLayout;
	private List<BanAn> danhSachBanDaChon = new ArrayList<>();

	private TextField timKiem;
	private ComboBox<String> cmbTatCa;
	private ToggleButton tang1;
	private ToggleButton tang2;
	private ViTri viTriHienTai = ViTri.LAU_1;

	public Gui_DanhSachBan(BorderPane mainLayout) {

		this.mainLayout = mainLayout;
		banAn_DAO = new BanAn_DAO();

		this.setStyle("-fx-background-color: white;");

		// Phần giữa
		HBox phanTren = taoPhanTren();
		HBox phanGiua = taoPhanGiua();
		VBox phanGiuaAll = new VBox();
		phanGiuaAll.getChildren().addAll(phanTren, phanGiua);

		this.setCenter(phanGiuaAll);

		loadDataToGrid();

	}

	// Tạo phần trên với nút tầng và trạng thái
	private HBox taoPhanTren() {
		VBox top = new VBox(5);
		top.setPadding(new Insets(10));
		// lbl chon tang
		Label lblChonTang = new Label("Khu vực");
		lblChonTang.getStyleClass().add("fontTieuDeNho");

		// Nút chọn tầng
		HBox nutTang = new HBox(5);
		nutTang.setAlignment(Pos.TOP_LEFT);
		nutTang.setMinWidth(400);
		tang1 = new ToggleButton("Tầng 1");
		tang1.setPrefSize(70, 40);
		tang2 = new ToggleButton("Tầng 2");
		tang2.setPrefSize(70, 40);
		ToggleGroup buttonGroup = new ToggleGroup();
		tang1.setToggleGroup(buttonGroup);
		tang2.setToggleGroup(buttonGroup);
		tang1.getStyleClass().add("nutTang");
		tang2.getStyleClass().add("nutTang");

		nutTang.getChildren().addAll(tang1, tang2);
		tang1.setSelected(true);

		tang1.setOnAction(e -> {
			viTriHienTai = ViTri.LAU_1;
			loadDataToGrid();
		});
		tang2.setOnAction(e -> {
			viTriHienTai = ViTri.LAU_2;
			loadDataToGrid();
		});

		// Ô tìm kiếm
		Label lblTiemKiem = new Label("Tìm kiếm bàn");
		lblTiemKiem.getStyleClass().add("fontTieuDeNho");

		// biến toàn cục
		timKiem = new TextField();
		timKiem.setPromptText("Tìm kiếm bằng mã bàn");
		timKiem.getStyleClass().add("timKiem");
		Button nutTimKiem = new Button("Tìm kiếm");
		nutTimKiem.getStyleClass().add("button-timKiem");

		// SỬA: Dùng biến toàn cục
		cmbTatCa = new ComboBox<>();
		cmbTatCa.getItems().addAll("Tất cả", "Bàn trống", "Đang sử dụng", "Đã đặt bàn", "Bàn VIP");
		cmbTatCa.setValue("Tất cả");
		cmbTatCa.setPrefWidth(100);

		HBox oTimKiem = new HBox(10, timKiem, nutTimKiem, cmbTatCa);
		oTimKiem.setAlignment(Pos.CENTER_LEFT);

		nutTimKiem.setOnAction(e -> loadDataToGrid());
		cmbTatCa.setOnAction(e -> loadDataToGrid());

		// Trạng thái chức vụ
		VBox trangThai = new VBox(2);
		trangThai.setPrefWidth(150);
		trangThai.setPadding(new Insets(10));
		Label chucVu = new Label("Chú thích");
		chucVu.getStyleClass().add("fontTieuDeNho");

		// Chú thích VIP
		Label vip = new Label("Bàn VIP");
		vip.setStyle("-fx-text-fill: #ed8936; -fx-font-weight: bold; -fx-font-size: 11;");
		HBox chuThich1 = new HBox(5);
		ImageView iconVIP = new ImageView(new Image("img/vipicon.png"));
		iconVIP.setFitWidth(15);
		iconVIP.setFitHeight(15);
		chuThich1.getChildren().addAll(iconVIP, vip);

		// Chú thích Đang chọn
		Label dangChon = new Label("Đang trống");
		dangChon.setStyle("-fx-text-fill: gray; -fx-font-weight: bold; -fx-font-size: 11;");
		HBox chuThich2 = new HBox(5);
		Circle dotGray = new Circle(5, Color.web("#BDBDBD"));
		chuThich2.getChildren().addAll(dotGray, dangChon);

		// Chú thích Đang sử dụng
		Label dangSuDung = new Label("Đang sử dụng");
		dangSuDung.setStyle("-fx-text-fill: #38A169; -fx-font-weight: bold; -fx-font-size: 11;");
		HBox chuThich4 = new HBox(5);
		Circle dotGreen = new Circle(5, Color.web("#38A169"));
		chuThich4.getChildren().addAll(dotGreen, dangSuDung);

		// Chú thích Đã đặt bàn
		Label daBan = new Label("Đã đặt bàn");
		daBan.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: 11;");
		HBox chuThich3 = new HBox(5);
		Circle dotRed = new Circle(5, Color.RED);
		chuThich3.getChildren().addAll(dotRed, daBan);

		trangThai.getChildren().addAll(chucVu, chuThich1, chuThich2, chuThich4, chuThich3);

		top.getChildren().addAll(lblChonTang, nutTang, lblTiemKiem, oTimKiem);
		top.setStyle("-fx-border-width: 0 1 0 0; -fx-border-color: #D9D9D9");

		HBox topALL = new HBox(40);
		topALL.getChildren().addAll(top, trangThai);

		return topALL;
	}

	// Tạo phần giữa (Giữ nguyên code của bạn)
	private HBox taoPhanGiua() {
		HBox giua = new HBox(20);
		giua.setPadding(new Insets(10));

		// Phần trái
		VBox trai = new VBox(10);
		Label danhSach = new Label("Danh sách bàn");
		danhSach.getStyleClass().add("fontTieuDeNho");

		luoiBan = taoLuoiBan();
		ScrollPane cuonLuoi = new ScrollPane(luoiBan);
		cuonLuoi.setFitToWidth(true);
		cuonLuoi.setPrefHeight(620);
		cuonLuoi.getStyleClass().add("scroll-pane");

		// Nút Đặt bàn
		Button btnDatBan = new Button("Đặt bàn");
		btnDatBan.getStyleClass().add("button-checkin");
		btnDatBan.setPrefSize(120, 40);

		// thêm
		btnDatBan.setOnAction(e -> {
			xuLyDatBan();
		});

		HBox boxDatBan = new HBox(btnDatBan);
		boxDatBan.setAlignment(Pos.BOTTOM_RIGHT);
		boxDatBan.setPadding(new Insets(10, 20, 10, 10));

		trai.getChildren().addAll(danhSach, cuonLuoi, boxDatBan);
		VBox.setVgrow(cuonLuoi, Priority.ALWAYS);

		giua.getChildren().addAll(trai);
		HBox.setHgrow(trai, Priority.ALWAYS);
		return giua;
	}

	private GridPane taoLuoiBan() {
		GridPane grid = new GridPane();
		grid.setHgap(15);
		grid.setVgap(15);
		grid.setPadding(new Insets(20));
		grid.setStyle("-fx-background-color: white");
		return grid;
	}

	private void loadDataToGrid() {
		// 1. Xóa bàn cũ và reset lựa chọn
		luoiBan.getChildren().clear();
		danhSachBanDaChon.clear();

		// 2. Lấy tất cả bàn từ DAO (chỉ theo tầng)
		List<BanAn> dsBanAnFull = banAn_DAO.getBanAnTheoViTri(viTriHienTai);

		// 3. Lấy giá trị từ các bộ lọc
		String tuKhoa = timKiem.getText().trim().toLowerCase();
		String loaiLoc = cmbTatCa.getValue();

		// 4. Lọc danh sách
		List<BanAn> dsDaLoc = new ArrayList<>();

		for (BanAn ban : dsBanAnFull) {
			boolean khopTuKhoa = true;
			boolean khopLoaiLoc = true;

			if (!tuKhoa.isEmpty()) {
				khopTuKhoa = ban.getMaBan().toLowerCase().contains(tuKhoa);
			}

			switch (loaiLoc) {
			case "Bàn trống":
				khopLoaiLoc = (ban.getTrangThai() == TrangThai.TRONG);
				break;
			case "Đang sử dụng":
				khopLoaiLoc = (ban.getTrangThai() == TrangThai.DANG_SU_DUNG);
				break;
			case "Đã đặt bàn":
				khopLoaiLoc = (ban.getTrangThai() == TrangThai.DA_DAT);
				break;
			case "Bàn VIP":
				khopLoaiLoc = (ban.getLoai() == LoaiBan.VIP);
				break;
			default:
				khopLoaiLoc = true;
				break;
			}

			if (khopTuKhoa && khopLoaiLoc) {
				dsDaLoc.add(ban);
			}
		}

		// 5. Hiển thị các bàn ĐÃ LỌC lên lưới
		List<StackPane> danhSachTheBan = new ArrayList<>();
		for (int i = 0; i < dsDaLoc.size(); i++) {
			BanAn ban = dsDaLoc.get(i);

			StackPane theBan = taoTheBan(ban);
			danhSachTheBan.add(theBan);

			theBan.setOnMouseClicked(e -> {
				VBox theChon = (VBox) theBan.getChildren().get(1);

				if (danhSachBanDaChon.contains(ban)) {
					danhSachBanDaChon.remove(ban);
					theChon.getStyleClass().add("theBan");
					theChon.setStyle("");
				} else {
					if (ban.getTrangThai() == TrangThai.TRONG) {
						danhSachBanDaChon.add(ban);
						theChon.getStyleClass().removeAll("theBan");
						theChon.setStyle("-fx-background-color: #BDBDBD;" + "-fx-background-radius: 20;"
								+ "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);"
								+ "-fx-cursor: hand;");
					} else {
						showAlert(AlertType.WARNING, "Không thể chọn", "Chỉ có thể chọn bàn đang 'Trống'.");
					}
				}
			});

			// 6. Thêm vào lưới
			int hang = i / 5;
			int cot = i % 5;
			GridPane.setRowIndex(theBan, hang);
			GridPane.setColumnIndex(theBan, cot);
			luoiBan.getChildren().add(theBan);
		}
	}

	private void xuLyDatBan() {
		if (danhSachBanDaChon.isEmpty()) { // Sửa
			showAlert(AlertType.ERROR, "Chưa chọn bàn", "Vui lòng click chọn ít nhất một bàn để đặt.");
			return;
		}

		for (BanAn ban : danhSachBanDaChon) {
			if (ban.getTrangThai() != TrangThai.TRONG) {
				showAlert(AlertType.WARNING, "Bàn không hợp lệ",
						"Trong danh sách có bàn " + ban.getMaBan() + " không 'Trống'.");
				return;
			}
		}

		try {
			datban guiDatBan = new datban(mainLayout, danhSachBanDaChon);
			mainLayout.setCenter(guiDatBan);

		} catch (Exception e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Lỗi", "Không thể mở giao diện đặt bàn.");
		}
	}

	private void showAlert(AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

	private StackPane taoTheBan(BanAn ban) {
		StackPane khung = new StackPane();
		khung.setPrefSize(220, 130);

		// Viền thay đổi màu theo trạng thái
		Region mauVien = new Region();
		mauVien.setPrefSize(15, 130);

		String indicatorColor;

		switch (ban.getTrangThai()) {
		case DANG_SU_DUNG:
			indicatorColor = "#32CD32";
			break;
		case DA_DAT:
			indicatorColor = "red";
			break;
		default:
			indicatorColor = "#BDBDBD";
			break;
		}
		mauVien.setStyle("-fx-background-color: " + indicatorColor + "; -fx-background-radius: 20;");

		// ----- Thẻ chính màu xanh -----
		VBox the = new VBox(10);
		the.setPrefSize(210, 130);
		the.setAlignment(Pos.CENTER);
		the.setPadding(new Insets(0, 10, 0, 10));
		the.getStyleClass().add("theBan"); // Dùng style class

		Label nhanBan = new Label(ban.getMaBan());
		nhanBan.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		nhanBan.setTextFill(Color.WHITE);

		Button btnXemThongTin = new Button("Xem thông tin");
		btnXemThongTin.setStyle("-fx-background-color: #6B49C7; " + // Màu tím
				"-fx-text-fill: white; " + "-fx-font-weight: bold; " + "-fx-font-size: 14px;"
				+ "-fx-background-radius: 15;" + "-fx-cursor: hand;");
		btnXemThongTin.setPrefWidth(150);

		// Icon VIP
		ImageView iconVip = new ImageView(new Image("img/vipicon.png"));
		iconVip.setFitHeight(20);
		iconVip.setFitWidth(20);

		// --- SỬA: Chỉ hiển thị icon nếu là bàn VIP ---
		iconVip.setVisible(ban.getLoai() == LoaiBan.VIP);

		HBox hboxVip = new HBox(iconVip);
		hboxVip.setAlignment(Pos.TOP_RIGHT);
		hboxVip.setMinHeight(20);

		the.getChildren().addAll(hboxVip, nhanBan, btnXemThongTin);

		btnXemThongTin.setOnAction(e -> {
			showTableInfoDialog(ban);
			e.consume();
		});

		khung.getChildren().addAll(mauVien, the);
		StackPane.setAlignment(mauVien, Pos.CENTER_LEFT);
		StackPane.setMargin(the, new Insets(0, 0, 0, 5));

		return khung;
	}

	private void showTableInfoDialog(BanAn ban) {
		Dialog<Void> dialog = new Dialog<>();

		dialog.initOwner(luoiBan.getScene().getWindow());
		dialog.initStyle(StageStyle.TRANSPARENT);
		dialog.getDialogPane().getScene().setFill(Color.TRANSPARENT);

		// Header
		dialog.setHeaderText(null);
		dialog.setGraphic(null);
		Label title = new Label(ban.getMaBan());
		title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");
		Button closeButton = new Button("X");
		closeButton.setStyle(
				"-fx-background-color: transparent; -fx-font-size: 16px; -fx-font-weight: bold; -fx-cursor: hand;");
		closeButton.setOnAction(e -> dialog.close());
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		HBox headerPane = new HBox(title, spacer, closeButton);
		headerPane.setAlignment(Pos.CENTER_LEFT);
		headerPane.setPadding(new Insets(10, 10, 10, 15));
		headerPane.setStyle("-fx-background-color: #F7FAFC;");
		Separator separator = new Separator();
		// Content
		String statusText, subText, bgColor, textColor;

		switch (ban.getTrangThai()) {
		case DANG_SU_DUNG:
			statusText = "Đang phục vụ";
			subText = "Bàn đang có khách sử dụng";
			bgColor = "#F0FFF4";
			textColor = "#22543D";
			break;
		case DA_DAT:
			statusText = "Bàn đã đặt";
			subText = "Bàn đã được khách đặt trước";
			bgColor = "#FFF5F5";
			textColor = "#9B2C2C";
			break;
		default: // TRONG
			statusText = "Bàn trống";
			subText = "Sẵn sàng phục vụ khách";
			bgColor = "#FFFBEB";
			textColor = "#975A16";
			break;
		}

		Label lblStatus = new Label(statusText);
		lblStatus.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: " + textColor + ";");
		Label lblSubText = new Label(subText);
		lblSubText.setStyle("-fx-font-size: 14px; -fx-text-fill: " + textColor + ";");

		VBox statusBox = new VBox(5, lblStatus, lblSubText);
		statusBox.setPadding(new Insets(15));
		statusBox.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 8;");

		Label lblLoaiBan = new Label("Loại bàn: " + ban.getLoai().name());
		lblLoaiBan.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
		Label lblViTri = new Label("Vị trí: " + ban.getViTri().name().replace("_", " "));
		lblViTri.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

		VBox extraInfoBox = new VBox(5, lblLoaiBan, lblViTri);
		extraInfoBox.setPadding(new Insets(15, 0, 0, 0));

		VBox mainLayout = new VBox(headerPane, separator, statusBox, extraInfoBox);
		mainLayout.setSpacing(0);
		mainLayout.setPrefWidth(350); 

		dialog.getDialogPane().setContent(mainLayout);

		dialog.getDialogPane().getStylesheets().add("data:text/css,"
				+ ".dialog-pane { -fx-background-color: white; -fx-padding: 0; "
				+ "-fx-border-color: #D9D9D9; -fx-border-width: 1; -fx-background-radius: 8; "
				+ "-fx-border-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5); }"
				+ ".dialog-pane .content { -fx-padding: 0; }");

		dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
		Node closeNode = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
		closeNode.setVisible(false);
		closeNode.setManaged(false);
		dialog.showAndWait();
	}

}