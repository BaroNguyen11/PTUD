package gui;

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
import javafx.stage.StageStyle; // --- IMPORT ĐÃ THÊM ---
import javafx.scene.control.Dialog; // --- IMPORT ĐÃ THÊM ---
import javafx.scene.control.ButtonType; // --- IMPORT ĐÃ THÊM ---
import javafx.scene.control.Separator; // --- IMPORT ĐÃ THÊM ---
import javafx.util.StringConverter;

public class Gui_DanhSachBan extends BorderPane {

	public enum TrangThaiBan {
		TRONG, DANG_SU_DUNG, DA_DAT_BAN
	}

	// Để cho pop-up biết cửa sổ cha của nó là gì
	private GridPane luoiBan;

	public Gui_DanhSachBan() {
		
		this.setStyle("-fx-background-color: white;");

		// Phần giữa
		HBox phanTren = taoPhanTren();
		HBox phanGiua = taoPhanGiua();
		VBox phanGiuaAll = new VBox();
		phanGiuaAll.getChildren().addAll(phanTren, phanGiua);

		this.setCenter(phanGiuaAll);



	}

	// Tạo phần trên với nút tầng và trạng thái (Giữ nguyên code của bạn)
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
		ToggleButton tang1 = new ToggleButton("Tầng 1");
		tang1.setPrefSize(70, 40);
		ToggleButton tang2 = new ToggleButton("Tầng 2");
		tang2.setPrefSize(70, 40);
		ToggleGroup buttonGroup = new ToggleGroup();
		tang1.setToggleGroup(buttonGroup);
		tang2.setToggleGroup(buttonGroup);
		tang1.getStyleClass().add("nutTang");
		tang2.getStyleClass().add("nutTang");
		nutTang.getChildren().addAll(tang1, tang2);
		tang1.setSelected(true);

		// Ô tìm kiếm
		Label lblTiemKiem = new Label("Tìm kiếm bàn");
		lblTiemKiem.getStyleClass().add("fontTieuDeNho");
		TextField timKiem = new TextField();
		timKiem.setPromptText("Tìm kiếm bằng mã bàn");
		timKiem.getStyleClass().add("timKiem");
		Button nutTimKiem = new Button("Tìm kiếm");
		nutTimKiem.getStyleClass().add("button-timKiem");

		// ComboBox
		ComboBox<String> cmbTatCa = new ComboBox<>();
		cmbTatCa.getItems().addAll("Tất cả", "Bàn trống", "Đang sử dụng", "Đã đặt bàn", "Bàn VIP");
		cmbTatCa.setValue("Tất cả");
		cmbTatCa.setPrefWidth(100);

		HBox oTimKiem = new HBox(10, timKiem, nutTimKiem, cmbTatCa);
		oTimKiem.setAlignment(Pos.CENTER_LEFT);

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
		Label dangChon = new Label("Đang chọn");
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

		String[] tenBan = new String[30];
		for (int k = 0; k < 30; k++) {
			tenBan[k] = "Bàn " + (k + 1);
		}

		List<StackPane> danhSachBan = new ArrayList<>();

		for (int i = 0; i < tenBan.length; i++) {

			// Thêm logic để biết trạng thái (đây là dữ liệu mẫu)
			TrangThaiBan trangThai;
			if (i % 3 == 0) {
				trangThai = TrangThaiBan.TRONG;
			} else if (i % 3 == 1) {
				trangThai = TrangThaiBan.DA_DAT_BAN;
			} else {
				trangThai = TrangThaiBan.DANG_SU_DUNG;
			}

			StackPane theBan = taoTheBan(tenBan[i], trangThai);

			int hang = i / 5;
			int cot = i % 5;

			danhSachBan.add(theBan);

			final int indexBan = i;
			theBan.setOnMouseClicked(e -> {
				for (int j = 0; j < danhSachBan.size(); j++) {
					StackPane khungReset = danhSachBan.get(j);
					VBox theReset = (VBox) khungReset.getChildren().get(1);
					theReset.setStyle("-fx-background-color: #082744" + ";" + "-fx-background-radius: 20;"
							+ "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);"
							+ "-fx-cursor: hand;");
				}
				StackPane khungChon = danhSachBan.get(indexBan);
				VBox theChon = (VBox) khungChon.getChildren().get(1);
				theChon.setStyle("-fx-background-color: #BDBDBD;" + "-fx-background-radius: 20;"
						+ "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);" + "-fx-cursor: hand;");
			});

			GridPane.setRowIndex(theBan, hang);
			GridPane.setColumnIndex(theBan, cot);
			grid.getChildren().add(theBan);
		}

		return grid;
	}

	private StackPane taoTheBan(String tenBan, TrangThaiBan trangThai) {
		// ----- StackPane chứa cả thẻ -----
		StackPane khung = new StackPane();
		khung.setPrefSize(220, 130);

		// ----- Viền (thay đổi màu theo trạng thái) -----
		Region mauVien = new Region();
		mauVien.setPrefSize(15, 130);

		String indicatorColor;
		switch (trangThai) {
		case DANG_SU_DUNG:
			indicatorColor = "#38A169";
			break;
		case DA_DAT_BAN:
			indicatorColor = "red";
			break;
		default:
			indicatorColor = "#32CD32";
			break;
		}
		mauVien.setStyle("-fx-background-color: " + indicatorColor + "; -fx-background-radius: 20;");

		// ----- Thẻ chính màu xanh -----
		VBox the = new VBox(10);
		the.setPrefSize(210, 130);
		the.setAlignment(Pos.CENTER);
		the.setPadding(new Insets(0, 10, 0, 10));
		the.getStyleClass().add("theBan");
		the.setStyle("-fx-background-color: #082744;" + "-fx-background-radius: 20;"
				+ "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);" + "-fx-cursor: hand;");

		// ----- Các label -----
		Label nhanBan = new Label(tenBan);
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
		HBox hboxVip = new HBox(iconVip);
		hboxVip.setAlignment(Pos.TOP_RIGHT);
		hboxVip.setMinHeight(20);

		// --- SỬA: Thay vboxThongTinKhach bằng btnXemThongTin ---
		the.getChildren().addAll(hboxVip, nhanBan, btnXemThongTin);

		// Thêm sự kiện click cho nút
		btnXemThongTin.setOnAction(e -> {
			showTableInfoDialog(tenBan, trangThai);
			e.consume();
		});

		khung.getChildren().addAll(mauVien, the);
		StackPane.setAlignment(mauVien, Pos.CENTER_LEFT);
		StackPane.setMargin(the, new Insets(0, 0, 0, 5));

		return khung;
	}

	private void showTableInfoDialog(String tenBan, TrangThaiBan trangThai) {
		Dialog<Void> dialog = new Dialog<>();

		dialog.initOwner(luoiBan.getScene().getWindow());
		dialog.initStyle(StageStyle.TRANSPARENT);
		dialog.getDialogPane().getScene().setFill(Color.TRANSPARENT);

		// Header
		dialog.setHeaderText(null);
		dialog.setGraphic(null);
		Label title = new Label(tenBan);
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
		switch (trangThai) {
		case DANG_SU_DUNG:
			statusText = "Đang phục vụ";
			subText = "Bàn đang có khách sử dụng";
			bgColor = "#F0FFF4";
			textColor = "#22543D";
			break;
		case DA_DAT_BAN:
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

		// Main Layout
		VBox mainLayout = new VBox(headerPane, separator, statusBox);
		mainLayout.setSpacing(0);
		mainLayout.setPadding(new Insets(0, 15, 15, 15));
		mainLayout.setStyle("-fx-background-color: white;");
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