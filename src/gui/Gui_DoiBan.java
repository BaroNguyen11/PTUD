////package gui;
////
////import java.time.LocalDate;
////import java.util.ArrayList;
////import java.util.List;
////import dao.BanAn_DAO;
////import dao.PhieuDatBan_DAO;
////import entity.BanAn;
////import entity.LoaiBan;
////import entity.TrangThai;
////import javafx.geometry.Insets;
////import javafx.geometry.Pos;
////import javafx.scene.Node;
////import javafx.scene.control.Alert;
////import javafx.scene.control.Alert.AlertType;
////import javafx.scene.control.Button;
////import javafx.scene.control.ButtonType;
////import javafx.scene.control.Dialog;
////import javafx.scene.control.Label;
////import javafx.scene.control.ScrollPane;
////import javafx.scene.control.TextField;
////import javafx.scene.control.ToggleButton;
////import javafx.scene.control.ToggleGroup;
////import javafx.scene.image.Image;
////import javafx.scene.image.ImageView;
////import javafx.scene.layout.GridPane;
////import javafx.scene.layout.HBox;
////import javafx.scene.layout.Priority;
////import javafx.scene.layout.Region;
////import javafx.scene.layout.StackPane;
////import javafx.scene.layout.VBox;
////import javafx.scene.paint.Color;
////import javafx.scene.text.Font;
////import javafx.scene.text.FontWeight;
////import javafx.stage.StageStyle;
////import javafx.stage.Window;
////import entity.ViTri;
////import entity.PhieuDatBan;
////
////public class Gui_DoiBan extends Dialog<ButtonType> {
////
////    private BanAn banCu;
////    private LocalDate ngayDat;
////    private BanAn_DAO banAn_DAO;
////    private PhieuDatBan_DAO phieuDatBan_DAO;
////    private BanAn banMoiDaChon = null;
////    private List<String> dsBanGhep;
////
////    private TextField txtMaBanMoi;
////    private TextField txtSoChoMoi;
////    private TextField txtViTriMoi;
////    private Label lblThongBaoNgayGio;
////
////    private GridPane luoiBanTrong;
////    private ToggleButton tang1;
////    private ToggleButton tang2;
////    private ViTri viTriHienTai = ViTri.LAU_1;
////
////    public Gui_DoiBan(Window owner, BanAn banCu, LocalDate ngayDat, BanAn_DAO banAn_DAO, PhieuDatBan_DAO phieuDatBan_DAO, List<String> dsBanGhep) {
////        this.banCu = banCu;
////        this.ngayDat = ngayDat;
////        this.banAn_DAO = banAn_DAO;
////        this.phieuDatBan_DAO = phieuDatBan_DAO;
////        this.dsBanGhep = dsBanGhep;
////
////        initOwner(owner);
////        initStyle(StageStyle.TRANSPARENT);
////        getDialogPane().getScene().setFill(Color.TRANSPARENT);
////        setTitle("Đổi Bàn");
////
////        getDialogPane().getStylesheets().add(getClass().getResource("/css/danhsachban.css").toExternalForm());
////
////        VBox mainLayout = new VBox();
////        mainLayout.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #D9D9D9; -fx-border-width: 1; -fx-border-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 15, 0, 0, 5);");
////
////        HBox header = taoHeader();
////
////        HBox body = taoBody();
////
////        mainLayout.getChildren().addAll(header, body);
////        getDialogPane().setContent(mainLayout);
////
////        if (banCu.getViTri() == ViTri.LAU_2) {
////            tang2.setSelected(true);
////            viTriHienTai = ViTri.LAU_2;
////        } else {
////            tang1.setSelected(true);
////            viTriHienTai = ViTri.LAU_1;
////        }
////
////        loadDanhSachBanTrong();
////
////        getDialogPane().getButtonTypes().clear();
////        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
////
////        Node closeNode = getDialogPane().lookupButton(ButtonType.CLOSE);
////        closeNode.setVisible(false);
////        closeNode.setManaged(false);
////
////        getDialogPane().setPrefSize(800, 600);
////        getDialogPane().setMaxSize(800, 600);
////    }
////
////    private HBox taoHeader() {
////        Label title = new Label("Đổi bàn: " + banCu.getMaBan());
////        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");
////        Button closeButton = new Button("X");
////        closeButton.setStyle("-fx-background-color: transparent; -fx-font-size: 16px; -fx-font-weight: bold; -fx-cursor: hand;");
////        closeButton.setOnAction(e -> close());
////
////        Region spacer = new Region();
////        HBox.setHgrow(spacer, Priority.ALWAYS);
////
////        HBox headerPane = new HBox(title, spacer, closeButton);
////        headerPane.setAlignment(Pos.CENTER_LEFT);
////        headerPane.setPadding(new Insets(10, 15, 10, 15));
////        headerPane.setStyle("-fx-background-color: #F7FAFC; -fx-border-width: 0 0 1 0; -fx-border-color: #D9D9D9;");
////        return headerPane;
////    }
////
////    private HBox taoBody() {
////        HBox body = new HBox(15);
////        body.setPadding(new Insets(15));
////        body.setAlignment(Pos.TOP_LEFT);
////
////        // --- 1. Phần Trái: Chọn Bàn Trống ---
////        VBox phanTrai = new VBox(10);
////        phanTrai.setPrefWidth(450);
////        phanTrai.setStyle("-fx-border-width: 0 1 0 0; -fx-border-color: #D9D9D9;");
////        phanTrai.setPadding(new Insets(0, 15, 0, 0));
////
////        Label lblKhuVuc = new Label("Khu vực");
////        lblKhuVuc.getStyleClass().add("fontTieuDeNho");
////
////        HBox nutTang = taoNutChonTang();
////
////        VBox boxCanhBaoGhep = taoVungCanhBaoGhep();
////
////        Label lblDSBanTrong = new Label("Danh sách bàn trống");
////        lblDSBanTrong.getStyleClass().add("fontTieuDeNho");
////
////        luoiBanTrong = taoLuoiBan();
////        ScrollPane cuonLuoi = new ScrollPane(luoiBanTrong);
////        cuonLuoi.setFitToWidth(true);
////        cuonLuoi.setPrefHeight(450);
////        cuonLuoi.getStyleClass().add("scroll-pane");
////
////        phanTrai.getChildren().addAll(lblKhuVuc, nutTang, boxCanhBaoGhep, lblDSBanTrong, cuonLuoi);
////        VBox.setVgrow(cuonLuoi, Priority.ALWAYS);
////
////        // --- 2. Phần Phải: Thông tin bàn mới và Xác nhận ---
////        VBox phanPhai = new VBox(20);
////        phanPhai.setPrefWidth(300);
////
////        Label lblThongTinMoi = new Label("Thông tin bàn mới");
////        lblThongTinMoi.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
////
////        // GridPane Thông tin chi tiết
////        GridPane thongTinChiTiet = taoThongTinBanMoi();
////
////        // Nút Xác nhận đổi
////        Button btnXacNhan = new Button("Xác nhận đổi");
////        btnXacNhan.getStyleClass().add("button-checkin");
////        btnXacNhan.setPrefSize(200, 40);
////        btnXacNhan.setOnAction(e -> xuLyXacNhanDoiBan());
////
////        HBox boxXacNhan = new HBox(btnXacNhan);
////        boxXacNhan.setAlignment(Pos.BOTTOM_RIGHT);
////
////        Region spacer = new Region();
////        VBox.setVgrow(spacer, Priority.ALWAYS);
////
////        phanPhai.getChildren().addAll(lblThongTinMoi, thongTinChiTiet, spacer, boxXacNhan);
////
////        body.getChildren().addAll(phanTrai, phanPhai);
////        return body;
////    }
////
////    private HBox taoNutChonTang() {
////        HBox nutTang = new HBox(5);
////        ToggleGroup buttonGroup = new ToggleGroup();
////
////        tang1 = new ToggleButton("Tầng 1");
////        tang1.setToggleGroup(buttonGroup);
////        tang1.getStyleClass().add("nutTang");
////        tang1.setOnAction(e -> {
////            viTriHienTai = ViTri.LAU_1;
////            loadDanhSachBanTrong();
////        });
////
////        tang2 = new ToggleButton("Tầng 2");
////        tang2.setToggleGroup(buttonGroup);
////        tang2.getStyleClass().add("nutTang");
////        tang2.setOnAction(e -> {
////            viTriHienTai = ViTri.LAU_2;
////            loadDanhSachBanTrong();
////        });
////
////        nutTang.getChildren().addAll(tang1, tang2);
////        return nutTang;
////    }
////
////    private GridPane taoLuoiBan() {
////        GridPane grid = new GridPane();
////        grid.setHgap(15);
////        grid.setVgap(15);
////        grid.setStyle("-fx-background-color: white");
////        return grid;
////    }
////
////
////    private GridPane taoThongTinBanMoi() {
////        GridPane grid = new GridPane();
////        grid.setHgap(10);
////        grid.setVgap(15);
////        grid.setPadding(new Insets(10));
////
////
////        String labelStyle = "-fx-text-fill: #555; -fx-font-size: 14px; -fx-font-weight: normal;";
////
////        String inputStyle = "-fx-background-color: white; -fx-opacity: 1; -fx-font-weight: bold;";
////
////        // 1. Mã bàn
////        Label lblMaBanTitle = new Label("Mã bàn:");
////        lblMaBanTitle.setStyle(labelStyle);
////        txtMaBanMoi = createInfoTextField("Chưa chọn", inputStyle);
////        grid.addRow(0, lblMaBanTitle, txtMaBanMoi);
////
////        // 2. Số chỗ
////        Label lblSoChoTitle = new Label("Số chỗ:");
////        lblSoChoTitle.setStyle(labelStyle);
////        txtSoChoMoi = createInfoTextField("Chưa chọn", inputStyle);
////        grid.addRow(1, lblSoChoTitle, txtSoChoMoi);
////
////        // 3. Vị trí
////        Label lblViTriTitle = new Label("Vị trí:");
////        lblViTriTitle.setStyle(labelStyle);
////        txtViTriMoi = createInfoTextField("Chưa chọn", inputStyle);
////        grid.addRow(2, lblViTriTitle, txtViTriMoi);
////
////        // --- 4. Ngày/Giờ ---
////
////        HBox boxNgayGio = new HBox(10);
////        boxNgayGio.setAlignment(Pos.CENTER_LEFT);
////        boxNgayGio.getStyleClass().add("timKiem");
////        boxNgayGio.setPadding(new Insets(5, 15, 5, 15));
////
////        Label lblIconDongHo = new Label("🕒");
////        lblIconDongHo.setStyle("-fx-font-size: 14px; -fx-text-fill: #4A5568;");
////
////        lblThongBaoNgayGio = new Label(ngayDat.toString() + " | " +
////                                       (banCu.getTrangThai() == TrangThai.DA_DAT ? "Theo PDB" : "Hiện tại"));
////        lblThongBaoNgayGio.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #2D3748;");
////
////        Region spacer = new Region();
////        HBox.setHgrow(spacer, Priority.ALWAYS);
////
////        Label lblIconLich = new Label("📅");
////        lblIconLich.setStyle("-fx-font-size: 14px; -fx-text-fill: #4A5568;");
////
////        boxNgayGio.getChildren().addAll(lblIconDongHo, lblThongBaoNgayGio, spacer, lblIconLich);
////
////        Label lblNgayGioTitle = new Label("Ngày/giờ:");
////        lblNgayGioTitle.setStyle(labelStyle);
////
////        grid.addRow(3, lblNgayGioTitle, boxNgayGio);
////
////        GridPane.setHgrow(txtMaBanMoi, Priority.ALWAYS);
////        GridPane.setHgrow(txtSoChoMoi, Priority.ALWAYS);
////        GridPane.setHgrow(txtViTriMoi, Priority.ALWAYS);
////        GridPane.setHgrow(boxNgayGio, Priority.ALWAYS);
////
////        return grid;
////    }
////
////    private TextField createInfoTextField(String text, String style) {
////        TextField tf = new TextField(text);
////        tf.setEditable(false);
////        tf.getStyleClass().add("timKiem");
////        tf.setStyle(style);
////        return tf;
////    }
////
////
////    private void loadDanhSachBanTrong() {
////        luoiBanTrong.getChildren().clear();
////
////        List<BanAn> dsBanTrong = banAn_DAO.getTrangThaiBanTheoNgayVaViTri(viTriHienTai, ngayDat)
////                                            .stream()
////                                            .filter(ban -> ban.getTrangThai() == TrangThai.TRONG)
////                                            .collect(java.util.stream.Collectors.toList());
////
////        if (banCu.getTrangThai() == TrangThai.DANG_SU_DUNG && banCu.getViTri() == viTriHienTai) {
////        } else if (banCu.getTrangThai() == TrangThai.DA_DAT && banCu.getViTri() == viTriHienTai) {
////        }
////
////        for (int i = 0; i < dsBanTrong.size(); i++) {
////            BanAn ban = dsBanTrong.get(i);
////            StackPane theBan = taoTheBan(ban);
////
////            theBan.setOnMouseClicked(e -> {
////                xuLyChonBanMoi(ban, theBan);
////            });
////
////            int hang = i / 3; // 3 cột
////            int cot = i % 3;
////            GridPane.setRowIndex(theBan, hang);
////            GridPane.setColumnIndex(theBan, cot);
////            luoiBanTrong.getChildren().add(theBan);
////        }
////    }
////    private VBox taoVungCanhBaoGhep() {
////        if (dsBanGhep == null || dsBanGhep.size() <= 1) {
////            return new VBox(); // Trả về VBox rỗng nếu không phải bàn ghép
////        }
////
////        VBox box = new VBox(5);
////        box.setPadding(new Insets(10));
////        box.setStyle("-fx-background-color: #FFF5F5; -fx-border-color: #DC3545; -fx-border-width: 1; -fx-background-radius: 5;");
////
////        Label lblTieuDe = new Label("!LƯU Ý: BÀN GHÉP ĐANG CHỌN");
////        lblTieuDe.setStyle("-fx-font-weight: bold; -fx-text-fill: #DC3545;");
////
////        String dsBanStr = String.join(", ", dsBanGhep);
////        Label lblThongTin = new Label(
////            "Bàn " + banCu.getMaBan() + " thuộc nhóm " + dsBanGhep.size() + " bàn: " + dsBanStr +
////            ".\nViệc đổi bàn này sẽ chỉ chuyển Phiếu Đặt Bàn của riêng bàn này."
////        );
////        lblThongTin.setWrapText(true);
////        lblThongTin.setStyle("-fx-font-size: 12px; -fx-text-fill: #9B2C2C;");
////
////        box.getChildren().addAll(lblTieuDe, lblThongTin);
////        return box;
////    }
////
////    private void xuLyChonBanMoi(BanAn ban, StackPane theBan) {
////        VBox theVBoxMoi = (VBox) theBan.getChildren().get(1);
////
////        if (banMoiDaChon == ban) {
////            theVBoxMoi.getStyleClass().remove("theBan-selected-style");
////            banMoiDaChon = null;
////
////            capNhatThongTinChiTiet();
////            return;
////        }
////
////        if (banMoiDaChon != null) {
////            StackPane theCu = (StackPane) luoiBanTrong.getChildren().stream()
////                .filter(node -> node instanceof StackPane && ((StackPane)node).getUserData() == banMoiDaChon)
////                .findFirst().orElse(null);
////            if (theCu != null) {
////                VBox theVBoxCu = (VBox) theCu.getChildren().get(1);
////                theVBoxCu.getStyleClass().remove("theBan-selected-style");
////            }
////        }
////
////        banMoiDaChon = ban;
////        theVBoxMoi.getStyleClass().add("theBan-selected-style");
////        theBan.setUserData(ban);
////
////        capNhatThongTinChiTiet();
////    }
////
////    private void capNhatThongTinChiTiet() {
////        if (banMoiDaChon == null) {
////            txtMaBanMoi.setText("000");
////            txtSoChoMoi.setText("");
////            txtViTriMoi.setText("");
////        } else {
////            int sucChua = (banMoiDaChon.getLoai().name().equals("VIP")) ? 6 : 4;
////
////            txtMaBanMoi.setText(banMoiDaChon.getMaBan());
////            txtSoChoMoi.setText(String.valueOf(sucChua) + " chỗ");
////            txtViTriMoi.setText(banMoiDaChon.getViTri().name().replace("_", " "));
////        }
////    }
////
////    private StackPane taoTheBan(BanAn ban) {
////        StackPane khung = new StackPane();
////        // Kích thước cố định
////        khung.setPrefSize(140, 100);
////
////        // 1. Dải màu trạng thái (Indicator)
////        Region mauVien = new Region();
////        mauVien.setPrefSize(10, 100);
////
////        String indicatorColor;
////        switch (ban.getTrangThai()) {
////            case DANG_SU_DUNG:
////                indicatorColor = "#32CD32"; // Xanh lá
////                break;
////            case DA_DAT:
////                indicatorColor = "red";
////                break;
////            default: // TRONG
////                indicatorColor = "#BDBDBD"; // Xám
////                break;
////        }
////        mauVien.setStyle("-fx-background-color: " + indicatorColor + "; -fx-background-radius: 20;");
////
////        // 2. Thẻ chính (VBox)
////        VBox the = new VBox(5);
////        the.setPrefSize(130, 100);
////        the.setAlignment(Pos.CENTER);
////        the.setPadding(new Insets(5));
////
////        // Màu nền mặc định (Xanh đậm)
////        the.setStyle("-fx-background-color: #0F375F; -fx-background-radius: 20; -fx-cursor: hand;");
////
////        // Thêm hiệu ứng hover CSS (sẽ được định nghĩa trong block CSS bên dưới)
////        the.getStyleClass().add("theBan-doiban");
////        the.setStyle("-fx-background-color: #082744; -fx-background-radius: 20; -fx-cursor: hand;");
////
////        // 3. Nội dung (Mã bàn và Số chỗ)
////        Label nhanBan = new Label(ban.getMaBan());
////        nhanBan.setFont(Font.font("Arial", FontWeight.BOLD, 18));
////        nhanBan.setTextFill(Color.WHITE);
////
////        // Tính sức chứa (Inline Logic)
////        int sucChua = (ban.getLoai().name().equals("VIP")) ? 6 : 4;
////
////        Label soCho = new Label(sucChua + " chỗ");
////        soCho.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
////        soCho.setTextFill(Color.WHITE);
////
////        // 4. Icon VIP (Góc trên bên phải)
////        HBox hboxIcons = new HBox();
////        hboxIcons.setMinHeight(15);
////        hboxIcons.setAlignment(Pos.TOP_RIGHT);
////
////        Region spacer = new Region();
////        HBox.setHgrow(spacer, Priority.ALWAYS);
////        hboxIcons.getChildren().add(spacer);
////
////        ImageView iconVip = new ImageView(new Image(getClass().getResource("/img/vipicon.png").toExternalForm()));
////        iconVip.setFitHeight(15);
////        iconVip.setFitWidth(15);
////        iconVip.setVisible(ban.getLoai() == LoaiBan.VIP);
////        hboxIcons.getChildren().add(iconVip);
////
////        // 5. Kết hợp các phần tử
////        the.getChildren().addAll(hboxIcons, nhanBan, soCho);
////        VBox.setMargin(hboxIcons, new Insets(0, 5, 0, 0));
////
////        // Thêm các thành phần cố định vào StackPane khung
////        khung.getChildren().addAll(mauVien, the);
////        StackPane.setAlignment(mauVien, Pos.CENTER_LEFT);
////        StackPane.setMargin(the, new Insets(0, 0, 0, 5));
////
////        khung.getStylesheets().add("data:text/css,"
////                // Hiệu ứng HOVER: Chuyển sang màu xanh nhạt hơn/sáng hơn
////                + ".theBan-doiban:hover { -fx-background-color: #1A4673; }"
////
////                // Trạng thái SELECTED: Áp dụng màu NHẠT HƠN tương tự như hover để báo hiệu đã chọn
////                // Màu này phải khác biệt so với màu mặc định #0F375F
////                + ".theBan-doiban.theBan-selected-style { "
////                + "-fx-background-color: #2D3748; " // Màu xám xanh (hơi sáng hơn)
////                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 5, 0, 0, 2); }"
////
////                // Cần đảm bảo rằng hiệu ứng selected ghi đè hiệu ứng hover nếu cả hai xảy ra
////            );
////
////        return khung;
////    }
////
////    private void xuLyXacNhanDoiBan() {
////        if (banMoiDaChon == null) {
////            showAlert(AlertType.ERROR, "Lỗi", "Vui lòng chọn bàn mới để đổi.");
////            return;
////        }
////
////        Alert alert = new Alert(AlertType.CONFIRMATION,
////            "Xác nhận đổi bàn từ " + banCu.getMaBan() + " sang " + banMoiDaChon.getMaBan() + " không?",
////            ButtonType.YES, ButtonType.NO);
////        alert.setTitle("Xác nhận Đổi Bàn");
////        alert.setHeaderText(null);
////
////        alert.showAndWait().ifPresent(response -> {
////            if (response == ButtonType.YES) {
////
////                TrangThai trangThaiCu = banCu.getTrangThai();
////
////                if (trangThaiCu != TrangThai.DA_DAT && trangThaiCu != TrangThai.DANG_SU_DUNG) {
////                    showAlert(AlertType.ERROR, "Lỗi", "Trạng thái bàn cũ không hợp lệ để đổi.");
////                    return;
////                }
////
////                boolean doiTongHopThanhCong = phieuDatBan_DAO.doiBanTongHop(
////                    banCu, banMoiDaChon, ngayDat, trangThaiCu
////                );
////
////                if (doiTongHopThanhCong) {
////                    showAlert(AlertType.INFORMATION, "Thành công",
////                        "Đã đổi bàn thành công từ " + banCu.getMaBan() + " sang " + banMoiDaChon.getMaBan() + ".");
////                    setResult(ButtonType.OK);
////                    close();
////                } else {
////                    showAlert(AlertType.ERROR, "Lỗi", "Đổi bàn thất bại. Vui lòng kiểm tra lại DAO hoặc trạng thái bàn.");
////                }
////            }
////        });
////
////    }
////
////    private void showAlert(AlertType alertType, String title, String content) {
////		Alert alert = new Alert(alertType);
////		alert.setTitle(title);
////		alert.setHeaderText(null);
////		alert.setContentText(content);
////		alert.showAndWait();
////	}
////}
//package gui;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import dao.BanAn_DAO;
//import dao.PhieuDatBan_DAO;
//import entity.BanAn;
//import entity.LoaiBan;
//import entity.TrangThai;
//import entity.ViTri;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Cursor;
//import javafx.scene.Node;
//import javafx.scene.control.*;
//import javafx.scene.control.Alert.AlertType;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.*;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//import javafx.scene.text.FontWeight;
//import javafx.stage.StageStyle;
//import javafx.stage.Window;
//
//// LƯU Ý: Tên class phải là Gui_DoiBan để khớp với Gui_DanhSachBan
//public class Gui_DoiBan extends Dialog<ButtonType> {
//
//    // INPUT
//    private BanAn banCu;
//    private LocalDate ngayDat;
//    private BanAn_DAO banAn_DAO;
//    private PhieuDatBan_DAO phieuDatBan_DAO;
//    private List<String> dsBanGhep;
//
//    // OUTPUT: Danh sách các bàn mới được chọn
//    private List<BanAn> dsBanMoiDaChon = new ArrayList<>();
//
//    // UI Components
//    private TextField txtMaBanMoi;
//    private TextField txtSoChoMoi;
//    private Label lblThongBaoNgayGio;
//    private GridPane luoiBanTrong;
//    private ToggleButton tang1, tang2;
//    private ViTri viTriHienTai = ViTri.LAU_1;
//
//    public Gui_DoiBan(Window owner, BanAn banCu, LocalDate ngayDat, BanAn_DAO banAn_DAO, PhieuDatBan_DAO phieuDatBan_DAO, List<String> dsBanGhep) {
//        this.banCu = banCu;
//        this.ngayDat = ngayDat;
//        this.banAn_DAO = banAn_DAO;
//        this.phieuDatBan_DAO = phieuDatBan_DAO;
//        this.dsBanGhep = dsBanGhep;
//
//        initOwner(owner);
//        initStyle(StageStyle.TRANSPARENT);
//        getDialogPane().getScene().setFill(Color.TRANSPARENT);
//        setTitle("Chuyển Đổi Bàn");
//
//        VBox mainLayout = new VBox();
//        mainLayout.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #D9D9D9; -fx-border-width: 1; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 15, 0, 0, 5);");
//
//        mainLayout.getChildren().addAll(taoHeader(), taoBody());
//        getDialogPane().setContent(mainLayout);
//
//        // Mặc định chọn tầng của bàn cũ
//        if (banCu.getViTri() == ViTri.LAU_2) {
//            tang2.setSelected(true);
//            viTriHienTai = ViTri.LAU_2;
//        } else {
//            tang1.setSelected(true);
//            viTriHienTai = ViTri.LAU_1;
//        }
//
//        loadDanhSachBanTrong();
//
//        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
//        Node closeNode = getDialogPane().lookupButton(ButtonType.CLOSE);
//        closeNode.setVisible(false);
//        closeNode.setManaged(false);
//
//        getDialogPane().setPrefSize(900, 600);
//    }
//
//    private HBox taoHeader() {
//        String titleText = "Đổi bàn";
//        if (dsBanGhep != null && dsBanGhep.size() > 1) {
//            titleText += " (Nhóm gốc: " + String.join(", ", dsBanGhep) + ")";
//        } else {
//            titleText += " từ bàn: " + banCu.getMaBan();
//        }
//
//        Label title = new Label(titleText);
//        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #082744;");
//
//        Button closeButton = new Button("✕");
//        closeButton.setStyle("-fx-background-color: transparent; -fx-font-size: 18px; -fx-cursor: hand; -fx-text-fill: #718096;");
//        closeButton.setOnAction(e -> close());
//
//        Region spacer = new Region();
//        HBox.setHgrow(spacer, Priority.ALWAYS);
//
//        HBox headerPane = new HBox(title, spacer, closeButton);
//        headerPane.setAlignment(Pos.CENTER_LEFT);
//        headerPane.setPadding(new Insets(15));
//        headerPane.setStyle("-fx-background-color: #F7FAFC; -fx-border-width: 0 0 1 0; -fx-border-color: #E2E8F0; -fx-background-radius: 10 10 0 0;");
//        return headerPane;
//    }
//
//    private HBox taoBody() {
//        HBox body = new HBox(0);
//
//        // --- TRÁI: DANH SÁCH BÀN TRỐNG ---
//        VBox phanTrai = new VBox(15);
//        phanTrai.setPrefWidth(580);
//        phanTrai.setPadding(new Insets(20));
//        phanTrai.setStyle("-fx-border-width: 0 1 0 0; -fx-border-color: #E2E8F0;");
//
//        HBox controlBox = new HBox(15);
//        controlBox.setAlignment(Pos.CENTER_LEFT);
//        Label lblKV = new Label("Khu vực:");
//        lblKV.setStyle("-fx-font-weight: bold;");
//        controlBox.getChildren().addAll(lblKV, taoNutChonTang());
//
//        Label lblGuide = new Label("💡 Có thể chọn nhiều bàn để ghép");
//        lblGuide.setStyle("-fx-text-fill: #718096; -fx-font-style: italic;");
//
//        luoiBanTrong = new GridPane();
//        luoiBanTrong.setHgap(15);
//        luoiBanTrong.setVgap(15);
//        luoiBanTrong.setPadding(new Insets(5));
//
//        ScrollPane scroll = new ScrollPane(luoiBanTrong);
//        scroll.setFitToWidth(true);
//        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
//
//        phanTrai.getChildren().addAll(controlBox, lblGuide, scroll);
//        VBox.setVgrow(scroll, Priority.ALWAYS);
//
//        // --- PHẢI: THÔNG TIN ĐÍCH & XÁC NHẬN ---
//        VBox phanPhai = new VBox(20);
//        phanPhai.setPrefWidth(320);
//        phanPhai.setPadding(new Insets(20));
//        phanPhai.setStyle("-fx-background-color: #FAFBFC; -fx-background-radius: 0 0 10 0;");
//
//        Label lblTitle = new Label("Bàn Đích Đến");
//        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #082744;");
//
//        // Form thông tin
//        GridPane infoGrid = new GridPane();
//        infoGrid.setVgap(15);
//        infoGrid.setHgap(10);
//
//        txtMaBanMoi = createInfoTextField("Chưa chọn");
//        txtSoChoMoi = createInfoTextField("0 chỗ");
//        lblThongBaoNgayGio = new Label(ngayDat.toString());
//        lblThongBaoNgayGio.setStyle("-fx-font-weight: bold; -fx-text-fill: #2D3748;");
//
//        addInfoRow(infoGrid, 0, "MÃ BÀN CHỌN:", txtMaBanMoi);
//        addInfoRow(infoGrid, 2, "TỔNG SỐ CHỖ:", txtSoChoMoi);
//
//        Label lblTime = new Label("THỜI GIAN:");
//        lblTime.setStyle("-fx-text-fill: #64748B; -fx-font-size: 11px; -fx-font-weight: bold;");
//        infoGrid.add(lblTime, 0, 4);
//        HBox timeBox = new HBox(10, new Label("📅"), lblThongBaoNgayGio);
//        timeBox.setStyle("-fx-background-color: #EDF2F7; -fx-padding: 8; -fx-background-radius: 5;");
//        infoGrid.add(timeBox, 0, 5);
//
//        // Nút xác nhận
//        Button btnConfirm = new Button("Xác nhận chuyển");
//        btnConfirm.setStyle("-fx-background-color: #082744; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12; -fx-background-radius: 8; -fx-cursor: hand;");
//        btnConfirm.setMaxWidth(Double.MAX_VALUE);
//        btnConfirm.setOnAction(e -> xuLyXacNhanDoiBan());
//
//        Region spacer = new Region();
//        VBox.setVgrow(spacer, Priority.ALWAYS);
//
//        phanPhai.getChildren().addAll(lblTitle, infoGrid, spacer, btnConfirm);
//
//        body.getChildren().addAll(phanTrai, phanPhai);
//        HBox.setHgrow(phanTrai, Priority.ALWAYS);
//        return body;
//    }
//
//    private void addInfoRow(GridPane grid, int row, String title, Node content) {
//        Label lbl = new Label(title);
//        lbl.setStyle("-fx-text-fill: #64748B; -fx-font-size: 11px; -fx-font-weight: bold;");
//        grid.add(lbl, 0, row);
//        grid.add(content, 0, row + 1);
//    }
//
//    private TextField createInfoTextField(String text) {
//        TextField tf = new TextField(text);
//        tf.setEditable(false);
//        tf.setStyle("-fx-background-color: white; -fx-border-color: #E2E8F0; -fx-border-radius: 5; -fx-padding: 8; -fx-font-weight: bold; -fx-text-fill: #082744;");
//        return tf;
//    }
//
//    private HBox taoNutChonTang() {
//        tang1 = new ToggleButton("Tầng 1");
//        tang2 = new ToggleButton("Tầng 2");
//        ToggleGroup group = new ToggleGroup();
//        tang1.setToggleGroup(group);
//        tang2.setToggleGroup(group);
//
//        String styleNormal = "-fx-background-color: white; -fx-border-color: #CBD5E0; -fx-border-radius: 4; -fx-cursor: hand;";
//        String styleSelected = "-fx-background-color: #082744; -fx-text-fill: white; -fx-border-radius: 4; -fx-cursor: hand;";
//
//        tang1.setStyle(styleSelected);
//        tang2.setStyle(styleNormal);
//
//        tang1.setOnAction(e -> {
//            viTriHienTai = ViTri.LAU_1;
//            tang1.setStyle(styleSelected); tang2.setStyle(styleNormal);
//            loadDanhSachBanTrong();
//        });
//        tang2.setOnAction(e -> {
//            viTriHienTai = ViTri.LAU_2;
//            tang2.setStyle(styleSelected); tang1.setStyle(styleNormal);
//            loadDanhSachBanTrong();
//        });
//
//        return new HBox(10, tang1, tang2);
//    }
//
//    private void loadDanhSachBanTrong() {
//        luoiBanTrong.getChildren().clear();
//        List<BanAn> dsBanTrong = banAn_DAO.getTrangThaiBanTheoNgayVaViTri(viTriHienTai, ngayDat)
//                .stream().filter(b -> b.getTrangThai() == TrangThai.TRONG).collect(Collectors.toList());
//
//        int col = 0; int row = 0;
//        for (BanAn ban : dsBanTrong) {
//            StackPane card = taoCardBan(ban);
//
//            // Highlight nếu đã chọn (khi chuyển tầng quay lại)
//            boolean isSelected = dsBanMoiDaChon.stream().anyMatch(b -> b.getMaBan().equals(ban.getMaBan()));
//            updateCardStyle(card, isSelected);
//
//            card.setOnMouseClicked(e -> {
//                xuLyClickBan(ban, card);
//            });
//
//            luoiBanTrong.add(card, col, row);
//            col++;
//            if (col > 3) { col = 0; row++; }
//        }
//    }
//
//    private StackPane taoCardBan(BanAn ban) {
//        StackPane root = new StackPane();
//        root.setPrefSize(120, 80);
//
//        VBox box = new VBox(2);
//        box.setAlignment(Pos.CENTER);
//        box.setPrefSize(120, 80);
//
//        Label lblMa = new Label(ban.getMaBan());
//        lblMa.setFont(Font.font("Arial", FontWeight.BOLD, 16));
//
//        Label lblCho = new Label((ban.getLoai() == LoaiBan.VIP ? "6" : "4") + " chỗ");
//        lblCho.setFont(Font.font("Arial", 11));
//
//        box.getChildren().addAll(lblMa, lblCho);
//        root.getChildren().add(box);
//        return root;
//    }
//
//    private void updateCardStyle(StackPane card, boolean isSelected) {
//        VBox box = (VBox) card.getChildren().get(0);
//        Label lblMa = (Label) box.getChildren().get(0);
//        Label lblCho = (Label) box.getChildren().get(1);
//
//        if (isSelected) {
//            // Style khi ĐƯỢC CHỌN
//            box.setStyle("-fx-background-color: #2B6CB0; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5,0,0,1);");
//            lblMa.setTextFill(Color.WHITE);
//            lblCho.setTextFill(Color.rgb(200, 220, 255));
//        } else {
//            // Style khi TRỐNG
//            box.setStyle("-fx-background-color: #EDF2F7; -fx-background-radius: 8; -fx-border-color: #CBD5E0; -fx-border-radius: 8;");
//            lblMa.setTextFill(Color.BLACK);
//            lblCho.setTextFill(Color.GRAY);
//        }
//    }
//
//    // --- LOGIC QUAN TRỌNG: MULTI-SELECT ---
//    private void xuLyClickBan(BanAn ban, StackPane card) {
//        // Kiểm tra xem bàn đã có trong danh sách chọn chưa
//        // Dùng equals maBan để tránh lỗi so sánh object reference
//        BanAn banDaTonTai = dsBanMoiDaChon.stream()
//                .filter(b -> b.getMaBan().equals(ban.getMaBan()))
//                .findFirst().orElse(null);
//
//        if (banDaTonTai != null) {
//            dsBanMoiDaChon.remove(banDaTonTai); // Bỏ chọn
//            updateCardStyle(card, false);
//        } else {
//            dsBanMoiDaChon.add(ban); // Thêm chọn
//            updateCardStyle(card, true);
//        }
//
//        capNhatThongTinPhai();
//    }
//
//    private void capNhatThongTinPhai() {
//        if (dsBanMoiDaChon.isEmpty()) {
//            txtMaBanMoi.setText("Chưa chọn");
//            txtSoChoMoi.setText("0 chỗ");
//        } else {
//            String dsTen = dsBanMoiDaChon.stream().map(BanAn::getMaBan).collect(Collectors.joining(", "));
//            txtMaBanMoi.setText(dsTen);
//
//            int tongCho = dsBanMoiDaChon.stream().mapToInt(b -> b.getLoai() == LoaiBan.VIP ? 6 : 4).sum();
//            txtSoChoMoi.setText(tongCho + " chỗ");
//        }
//    }
//
//    private void xuLyXacNhanDoiBan() {
//        if (dsBanMoiDaChon.isEmpty()) {
//            new Alert(AlertType.ERROR, "Vui lòng chọn ít nhất 1 bàn mới!").show();
//            return;
//        }
//
//        List<String> dsMaBanCu = (dsBanGhep != null && !dsBanGhep.isEmpty()) ? dsBanGhep : List.of(banCu.getMaBan());
//        List<String> dsMaBanMoi = dsBanMoiDaChon.stream().map(BanAn::getMaBan).collect(Collectors.toList());
//        String maHD = banAn_DAO.getMaHoaDonTuBan(dsMaBanCu.get(0), ngayDat);
//
//        if (maHD == null) {
//            new Alert(AlertType.ERROR, "Lỗi: Không tìm thấy hóa đơn bàn cũ.").show();
//            return;
//        }
//
//        Alert confirm = new Alert(AlertType.CONFIRMATION,
//                "Chuyển từ: " + String.join(", ", dsMaBanCu) + "\n" +
//                        "Sang bàn: " + String.join(", ", dsMaBanMoi) + "?",
//                ButtonType.YES, ButtonType.NO);
//
//        confirm.showAndWait().ifPresent(res -> {
//            if (res == ButtonType.YES) {
//
//                // 1. Tạo loading dialog
//                Dialog<Void> loading = new Dialog<>();
//                loading.initStyle(StageStyle.UNDECORATED);
//                ProgressIndicator pi = new ProgressIndicator();
//                Label lblLoad = new Label("Đang xử lý chuyển bàn...");
//                HBox boxLoad = new HBox(10, pi, lblLoad);
//                boxLoad.setPadding(new Insets(20));
//                boxLoad.setAlignment(Pos.CENTER);
//                loading.getDialogPane().setContent(boxLoad);
//
//                // Sửa lại owner cho chắc chắn
//                loading.initOwner(this.getDialogPane().getScene().getWindow());
//
//                loading.show(); // Hiện loading (Non-blocking)
//
//                // 2. Chạy Thread ngầm
//                new Thread(() -> {
//                    boolean ok = false;
//                    try {
//                        // Gọi DAO (Tác vụ nặng)
//                        String trangThaiCanChuyen = "Đang dùng"; // Mặc định
//
//                        // Kiểm tra trạng thái bàn cũ (biến banCu đã có sẵn trong class)
//                        if (banCu.getTrangThai() == TrangThai.DA_DAT) {
//                            trangThaiCanChuyen = "Đã đặt"; // Giữ nguyên trạng thái đặt
//                        } else {
//                            trangThaiCanChuyen = "Đang dùng"; // Giữ nguyên trạng thái ăn
//                        }
//
//                        // Gọi DAO với tham số mới
//                        ok = phieuDatBan_DAO.chuyenBanNhieuSangNhieu(dsMaBanCu, dsMaBanMoi, maHD, trangThaiCanChuyen);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        ok = false;
//                    } finally {
//                        // 3. Cập nhật UI (Luôn chạy dù có lỗi hay không)
//                        boolean finalOk = ok;
//                        javafx.application.Platform.runLater(() -> {
//                            // QUAN TRỌNG: Tắt loading trước tiên
//                            loading.setResult(null); // Đôi khi cần set result để đóng dialog
//                            loading.close();
//
//                            if (finalOk) {
//                                Alert success = new Alert(AlertType.INFORMATION, "Chuyển bàn thành công!");
//                                success.initOwner(this.getDialogPane().getScene().getWindow());
//                                success.showAndWait();
//                                setResult(ButtonType.OK);
//                                close(); // Đóng dialog đổi bàn chính
//                            } else {
//                                Alert error = new Alert(AlertType.ERROR, "Lỗi cập nhật CSDL. Vui lòng thử lại.");
//                                error.initOwner(this.getDialogPane().getScene().getWindow());
//                                error.show();
//                            }
//                        });
//                    }
//                }).start();
//            }
//        });
//    }
//}
package gui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dao.BanAn_DAO;
import dao.PhieuDatBan_DAO;
import entity.BanAn;
import entity.LoaiBan;
import entity.TrangThai;
import entity.ViTri;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class Gui_DoiBan extends Dialog<ButtonType> {

    // INPUT
    private BanAn banCu;
    private LocalDate ngayDat;
    private BanAn_DAO banAn_DAO;
    private PhieuDatBan_DAO phieuDatBan_DAO;
    private List<String> dsBanGhep;
    private Window ownerWindow;

    // OUTPUT
    private List<BanAn> dsBanMoiDaChon = new ArrayList<>();

    // UI
    private TextField txtMaBanMoi;
    private TextField txtSoChoMoi;
    private Label lblThongBaoNgayGio;
    private GridPane luoiBanTrong;
    private ToggleButton tang1, tang2;
    private ViTri viTriHienTai = ViTri.LAU_1;

    public Gui_DoiBan(Window owner, BanAn banCu, LocalDate ngayDat, BanAn_DAO banAn_DAO, PhieuDatBan_DAO phieuDatBan_DAO, List<String> dsBanGhep) {
        this.banCu = banCu;
        this.ngayDat = ngayDat;
        this.banAn_DAO = banAn_DAO;
        this.phieuDatBan_DAO = phieuDatBan_DAO;
        this.dsBanGhep = dsBanGhep;
        this.ownerWindow = owner;

        initOwner(owner);
        initStyle(StageStyle.TRANSPARENT);
        getDialogPane().getScene().setFill(Color.TRANSPARENT);
        setTitle("Chuyển Đổi Bàn");

        // Hiệu ứng làm mờ
        setOnShowing(e -> applyBlur(true));
        setOnHidden(e -> applyBlur(false));

        VBox mainLayout = new VBox();
        mainLayout.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-border-color: #E2E8F0; -fx-border-width: 1; -fx-border-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 20, 0, 0, 5);");

        mainLayout.getChildren().addAll(taoHeader(), taoBody());
        getDialogPane().setContent(mainLayout);

        if (banCu.getViTri() == ViTri.LAU_2) {
            tang2.setSelected(true);
            viTriHienTai = ViTri.LAU_2;
        } else {
            tang1.setSelected(true);
            viTriHienTai = ViTri.LAU_1;
        }

        loadDanhSachBanTrong();

        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeNode = getDialogPane().lookupButton(ButtonType.CLOSE);
        closeNode.setVisible(false);
        closeNode.setManaged(false);

        getDialogPane().setPrefSize(950, 650);
    }

    private void applyBlur(boolean enable) {
        if (ownerWindow != null && ownerWindow.getScene() != null) {
            Node root = ownerWindow.getScene().getRoot();
            if (enable) {
                BoxBlur blur = new BoxBlur(10, 10, 3);
                root.setEffect(blur);
            } else {
                root.setEffect(null);
            }
        }
    }

    private HBox taoHeader() {
        String titleText = "Chuyển bàn";
        if (dsBanGhep != null && dsBanGhep.size() > 1) {
            titleText += " (Nhóm: " + String.join(", ", dsBanGhep) + ")";
        } else {
            titleText += " từ bàn: " + banCu.getMaBan();
        }

        Label title = new Label(titleText);
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1A202C;");

        Button closeButton = new Button("✕");
        closeButton.setStyle("-fx-background-color: transparent; -fx-font-size: 18px; -fx-cursor: hand; -fx-text-fill: #718096;");
        closeButton.setOnAction(e -> close());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox headerPane = new HBox(title, spacer, closeButton);
        headerPane.setAlignment(Pos.CENTER_LEFT);
        headerPane.setPadding(new Insets(20, 25, 20, 25));
        headerPane.setStyle("-fx-background-color: white; -fx-border-width: 0 0 1 0; -fx-border-color: #EDF2F7; -fx-background-radius: 15 15 0 0;");
        return headerPane;
    }

    private HBox taoBody() {
        HBox body = new HBox(0);

        // --- TRÁI ---
        VBox phanTrai = new VBox(20);
        phanTrai.setPrefWidth(600);
        phanTrai.setPadding(new Insets(25));
        phanTrai.setStyle("-fx-border-width: 0 1 0 0; -fx-border-color: #EDF2F7;");

        HBox controlBox = new HBox(20);
        controlBox.setAlignment(Pos.CENTER_LEFT);
        Label lblKV = new Label("Khu vực:");
        lblKV.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #4A5568;");
        controlBox.getChildren().addAll(lblKV, taoNutChonTang());

        Label lblGuide = new Label("💡 Chọn một hoặc nhiều bàn trống để chuyển đến.");
        lblGuide.setStyle("-fx-text-fill: #718096; -fx-font-style: italic; -fx-font-size: 13px;");

        luoiBanTrong = new GridPane();
        luoiBanTrong.setHgap(15);
        luoiBanTrong.setVgap(15);
        luoiBanTrong.setPadding(new Insets(5));

        ScrollPane scroll = new ScrollPane(luoiBanTrong);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-padding: 0;");
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        phanTrai.getChildren().addAll(controlBox, lblGuide, scroll);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        // --- PHẢI ---
        VBox phanPhai = new VBox(25);
        phanPhai.setPrefWidth(350);
        phanPhai.setPadding(new Insets(25));
        phanPhai.setStyle("-fx-background-color: #F7FAFC; -fx-background-radius: 0 0 15 0;");

        Label lblTitle = new Label("Thông tin chuyển đến");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2D3748;");

        GridPane infoGrid = new GridPane();
        infoGrid.setVgap(15);
        infoGrid.setHgap(10);

        txtMaBanMoi = createInfoTextField("Chưa chọn");
        txtSoChoMoi = createInfoTextField("0 chỗ");

        lblThongBaoNgayGio = new Label(ngayDat.toString());
        lblThongBaoNgayGio.setStyle("-fx-font-weight: bold; -fx-text-fill: #2D3748; -fx-font-size: 14px;");

        addInfoRow(infoGrid, 0, "BÀN ĐÃ CHỌN:", txtMaBanMoi);
        addInfoRow(infoGrid, 2, "TỔNG SỐ CHỖ:", txtSoChoMoi);

        Label lblTime = new Label("THỜI GIAN:");
        lblTime.setStyle("-fx-text-fill: #718096; -fx-font-size: 11px; -fx-font-weight: bold;");
        infoGrid.add(lblTime, 0, 4);

        HBox timeBox = new HBox(10, new Label("📅"), lblThongBaoNgayGio);
        timeBox.setAlignment(Pos.CENTER_LEFT);
        timeBox.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-background-radius: 8; -fx-border-color: #E2E8F0; -fx-border-radius: 8;");
        infoGrid.add(timeBox, 0, 5);

        Button btnConfirm = new Button("Xác nhận chuyển");
        btnConfirm.setStyle("-fx-background-color: #3182CE; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 12 20; -fx-background-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(49, 130, 206, 0.4), 10, 0, 0, 3);");
        btnConfirm.setMaxWidth(Double.MAX_VALUE);

        btnConfirm.setOnMouseEntered(e -> btnConfirm.setStyle("-fx-background-color: #2B6CB0; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 12 20; -fx-background-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(43, 108, 176, 0.4), 10, 0, 0, 3);"));
        btnConfirm.setOnMouseExited(e -> btnConfirm.setStyle("-fx-background-color: #3182CE; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 12 20; -fx-background-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(49, 130, 206, 0.4), 10, 0, 0, 3);"));

        btnConfirm.setOnAction(e -> xuLyXacNhanDoiBan());

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        phanPhai.getChildren().addAll(lblTitle, infoGrid, spacer, btnConfirm);

        body.getChildren().addAll(phanTrai, phanPhai);
        HBox.setHgrow(phanTrai, Priority.ALWAYS);
        return body;
    }

    private void addInfoRow(GridPane grid, int row, String title, Node content) {
        Label lbl = new Label(title);
        lbl.setStyle("-fx-text-fill: #718096; -fx-font-size: 11px; -fx-font-weight: bold;");
        grid.add(lbl, 0, row);
        grid.add(content, 0, row + 1);
    }

    private TextField createInfoTextField(String text) {
        TextField tf = new TextField(text);
        tf.setEditable(false);
        tf.setStyle("-fx-background-color: white; -fx-border-color: #E2E8F0; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10; -fx-font-weight: bold; -fx-text-fill: #2D3748; -fx-font-size: 13px;");
        return tf;
    }

    private HBox taoNutChonTang() {
        tang1 = new ToggleButton("Tầng 1");
        tang2 = new ToggleButton("Tầng 2");
        ToggleGroup group = new ToggleGroup();
        tang1.setToggleGroup(group);
        tang2.setToggleGroup(group);

        String styleNormal = "-fx-background-color: white; -fx-text-fill: #4A5568; -fx-border-color: #CBD5E0; -fx-border-radius: 6; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 15;";
        String styleSelected = "-fx-background-color: #3182CE; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-color: #3182CE; -fx-border-radius: 6; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 6 15;";

        tang1.setStyle(styleSelected);
        tang2.setStyle(styleNormal);

        tang1.setOnAction(e -> {
            viTriHienTai = ViTri.LAU_1;
            tang1.setStyle(styleSelected); tang2.setStyle(styleNormal);
            loadDanhSachBanTrong();
        });
        tang2.setOnAction(e -> {
            viTriHienTai = ViTri.LAU_2;
            tang2.setStyle(styleSelected); tang1.setStyle(styleNormal);
            loadDanhSachBanTrong();
        });

        return new HBox(10, tang1, tang2);
    }

    private void loadDanhSachBanTrong() {
        luoiBanTrong.getChildren().clear();
        List<BanAn> dsBanTrong = banAn_DAO.getTrangThaiBanTheoNgayVaViTri(viTriHienTai, ngayDat)
                .stream().filter(b -> b.getTrangThai() == TrangThai.TRONG).collect(Collectors.toList());

        int col = 0; int row = 0;
        for (BanAn ban : dsBanTrong) {
            StackPane card = taoCardBan(ban);

            boolean isSelected = dsBanMoiDaChon.stream().anyMatch(b -> b.getMaBan().equals(ban.getMaBan()));
            updateCardStyle(card, isSelected);

            card.setOnMouseClicked(e -> {
                xuLyClickBan(ban, card);
            });

            luoiBanTrong.add(card, col, row);
            col++;
            if (col > 3) { col = 0; row++; }
        }
    }

    private StackPane taoCardBan(BanAn ban) {
        StackPane root = new StackPane();
        root.setPrefSize(130, 90);
        root.setMaxSize(130, 90);

        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));

        // Mặc định
        box.setStyle("-fx-background-color: white; -fx-border-color: #E2E8F0; -fx-border-width: 2; -fx-border-radius: 12; -fx-background-radius: 12; -fx-cursor: hand;");

        // --- QUAN TRỌNG: GẮN ID ĐỂ TÌM KIẾM ---
        Label lblMa = new Label(ban.getMaBan());
        lblMa.setId("lblMa");
        lblMa.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        lblMa.setTextFill(Color.web("#2D3748")); // Mặc định đen

        HBox boxCho = new HBox(5);
        boxCho.setAlignment(Pos.CENTER);
        Label iconNguoi = new Label("👤");
        iconNguoi.setId("icon");
        iconNguoi.setStyle("-fx-font-size: 12px; -fx-text-fill: #718096;");

        String loaiBan = ban.getLoai() == LoaiBan.VIP ? "6" : "4";
        Label lblCho = new Label(loaiBan + " chỗ");
        lblCho.setId("lblCho");
        lblCho.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        lblCho.setTextFill(Color.web("#718096")); // Mặc định xám

        boxCho.getChildren().addAll(iconNguoi, lblCho);

        if (ban.getLoai() == LoaiBan.VIP) {
            Label lblVip = new Label("VIP");
            lblVip.setStyle("-fx-background-color: #F6E05E; -fx-text-fill: #744210; -fx-font-size: 9px; -fx-font-weight: bold; -fx-padding: 2 6; -fx-background-radius: 10;");
            box.getChildren().add(0, lblVip);
        }

        box.getChildren().addAll(lblMa, boxCho);
        root.getChildren().add(box);

        root.setUserData(ban);
        return root;
    }

    private void updateCardStyle(StackPane card, boolean isSelected) {
        VBox box = (VBox) card.getChildren().get(0);
        BanAn banData = (BanAn) card.getUserData();

        // --- TÌM LABEL BẰNG ID (CHẮC CHẮN 100% TÌM THẤY) ---
        Label lblMa = (Label) box.lookup("#lblMa");
        Label lblCho = (Label) box.lookup("#lblCho");
        Label icon = (Label) box.lookup("#icon");

        if (isSelected) {
            // == ĐÃ CHỌN: Nền xanh, Chữ trắng ==
            box.setStyle("-fx-background-color: #3182CE; -fx-border-color: #3182CE; -fx-border-width: 2; -fx-background-radius: 12; -fx-border-radius: 12; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(49, 130, 206, 0.4), 8, 0, 0, 4);");

            // Ép màu chữ bằng style để ghi đè CSS global
            if(lblMa != null) lblMa.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
            if(lblCho != null) lblCho.setStyle("-fx-text-fill: #EBF8FF; -fx-font-size: 12px;");
            if(icon != null) icon.setStyle("-fx-text-fill: #EBF8FF; -fx-font-size: 12px;");

            box.setOnMouseEntered(null);
            box.setOnMouseExited(null);

        } else {
            // == CHƯA CHỌN: Nền trắng, Chữ ĐEN/XÁM ==
            box.setStyle("-fx-background-color: white; -fx-border-color: #E2E8F0; -fx-border-width: 2; -fx-background-radius: 12; -fx-border-radius: 12; -fx-cursor: hand;");

            // QUAN TRỌNG: Set màu đen/xám rõ ràng bằng setStyle
            if(lblMa != null) lblMa.setStyle("-fx-text-fill: #2D3748; -fx-font-weight: bold; -fx-font-size: 16px;");
            if(lblCho != null) lblCho.setStyle("-fx-text-fill: #718096; -fx-font-size: 12px;");
            if(icon != null) icon.setStyle("-fx-text-fill: #718096; -fx-font-size: 12px;");

            // Hover
            box.setOnMouseEntered(e -> {
                boolean isAlreadySelected = dsBanMoiDaChon.stream().anyMatch(b -> b.getMaBan().equals(banData.getMaBan()));
                if(!isAlreadySelected) {
                    box.setStyle("-fx-background-color: #F7FAFC; -fx-border-color: #3182CE; -fx-border-width: 2; -fx-background-radius: 12; -fx-border-radius: 12; -fx-cursor: hand;");
                }
            });

            box.setOnMouseExited(e -> {
                boolean isAlreadySelected = dsBanMoiDaChon.stream().anyMatch(b -> b.getMaBan().equals(banData.getMaBan()));
                if(!isAlreadySelected) {
                    box.setStyle("-fx-background-color: white; -fx-border-color: #E2E8F0; -fx-border-width: 2; -fx-background-radius: 12; -fx-border-radius: 12; -fx-cursor: hand;");
                }
            });
        }
    }

    private void xuLyClickBan(BanAn ban, StackPane card) {
        BanAn banDaTonTai = dsBanMoiDaChon.stream()
                .filter(b -> b.getMaBan().equals(ban.getMaBan()))
                .findFirst().orElse(null);

        if (banDaTonTai != null) {
            dsBanMoiDaChon.remove(banDaTonTai);
            updateCardStyle(card, false);
        } else {
            dsBanMoiDaChon.add(ban);
            updateCardStyle(card, true);
        }
        capNhatThongTinPhai();
    }

    private void capNhatThongTinPhai() {
        if (dsBanMoiDaChon.isEmpty()) {
            txtMaBanMoi.setText("Chưa chọn");
            txtSoChoMoi.setText("0 chỗ");
        } else {
            String dsTen = dsBanMoiDaChon.stream().map(BanAn::getMaBan).collect(Collectors.joining(", "));
            txtMaBanMoi.setText(dsTen);

            int tongCho = dsBanMoiDaChon.stream().mapToInt(b -> b.getLoai() == LoaiBan.VIP ? 6 : 4).sum();
            txtSoChoMoi.setText(tongCho + " chỗ");
        }
    }

    private void xuLyXacNhanDoiBan() {
        if (dsBanMoiDaChon.isEmpty()) {
            new Alert(AlertType.ERROR, "Vui lòng chọn ít nhất 1 bàn mới!").show();
            return;
        }

        List<String> dsMaBanCu = (dsBanGhep != null && !dsBanGhep.isEmpty()) ? dsBanGhep : List.of(banCu.getMaBan());
        List<String> dsMaBanMoi = dsBanMoiDaChon.stream().map(BanAn::getMaBan).collect(Collectors.toList());
        String maHD = banAn_DAO.getMaHoaDonTuBan(dsMaBanCu.get(0), ngayDat);

        if (maHD == null) {
            new Alert(AlertType.ERROR, "Lỗi: Không tìm thấy hóa đơn bàn cũ.").show();
            return;
        }

        Alert confirm = new Alert(AlertType.CONFIRMATION,
                "Chuyển từ: " + String.join(", ", dsMaBanCu) + "\n" +
                        "Sang bàn: " + String.join(", ", dsMaBanMoi) + "?",
                ButtonType.YES, ButtonType.NO);

        confirm.showAndWait().ifPresent(res -> {
            if (res == ButtonType.YES) {

                Dialog<Void> loading = new Dialog<>();
                loading.initStyle(StageStyle.UNDECORATED);
                loading.initOwner(this.getDialogPane().getScene().getWindow());

                ProgressIndicator pi = new ProgressIndicator();
                Label lblLoad = new Label("Đang xử lý chuyển bàn...");
                lblLoad.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

                HBox boxLoad = new HBox(15, pi, lblLoad);
                boxLoad.setPadding(new Insets(25));
                boxLoad.setAlignment(Pos.CENTER);
                boxLoad.setStyle("-fx-background-color: white; -fx-border-color: #E2E8F0; -fx-border-width: 1; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 4);");

                loading.getDialogPane().setContent(boxLoad);

                new Thread(() -> {
                    boolean ok = false;
                    try {
                        Thread.sleep(500);

                        // FIX 1: Xác định trạng thái bàn mới
                        String trangThaiMoi = "Đang dùng";
                        if (banCu.getTrangThai() == TrangThai.DA_DAT) {
                            trangThaiMoi = "Đã đặt";
                        }

                        // FIX 2: GỌI HÀM DAO VỚI ĐỦ 4 THAM SỐ
                        ok = phieuDatBan_DAO.chuyenBanNhieuSangNhieu(dsMaBanCu, dsMaBanMoi, maHD, trangThaiMoi);

                    } catch (Exception e) {
                        e.printStackTrace();
                        ok = false;
                    } finally {
                        boolean finalOk = ok;
                        Platform.runLater(() -> {
                            loading.setResult(null);
                            loading.close();
                            if (finalOk) {
                                Alert success = new Alert(AlertType.INFORMATION, "Chuyển bàn thành công!");
                                success.initOwner(this.getDialogPane().getScene().getWindow());
                                success.showAndWait();
                                setResult(ButtonType.OK);
                                close();
                            } else {
                                Alert error = new Alert(AlertType.ERROR, "Lỗi cập nhật CSDL. Vui lòng thử lại.");
                                error.initOwner(this.getDialogPane().getScene().getWindow());
                                error.show();
                            }
                        });
                    }
                }).start();

                loading.show();
            }
        });
    }
}