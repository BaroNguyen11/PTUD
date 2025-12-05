package gui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import dao.BanAn_DAO;
import dao.PhieuDatBan_DAO;
import entity.BanAn;
import entity.LoaiBan;
import entity.TrangThai;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import entity.ViTri;
import entity.PhieuDatBan;

public class DialogDoiBan extends Dialog<ButtonType> {

    private BanAn banCu;
    private LocalDate ngayDat;
    private BanAn_DAO banAn_DAO;
    private PhieuDatBan_DAO phieuDatBan_DAO;
    private BanAn banMoiDaChon = null;
    private List<String> dsBanGhep;

    private TextField txtMaBanMoi;
    private TextField txtSoChoMoi;
    private TextField txtViTriMoi;
    private Label lblThongBaoNgayGio;

    private GridPane luoiBanTrong;
    private ToggleButton tang1;
    private ToggleButton tang2;
    private ViTri viTriHienTai = ViTri.LAU_1;

    public DialogDoiBan(Window owner, BanAn banCu, LocalDate ngayDat, BanAn_DAO banAn_DAO, PhieuDatBan_DAO phieuDatBan_DAO, List<String> dsBanGhep) {
        this.banCu = banCu;
        this.ngayDat = ngayDat;
        this.banAn_DAO = banAn_DAO;
        this.phieuDatBan_DAO = phieuDatBan_DAO;
        this.dsBanGhep = dsBanGhep;

        initOwner(owner);
        initStyle(StageStyle.TRANSPARENT);
        getDialogPane().getScene().setFill(Color.TRANSPARENT);
        setTitle("Đổi Bàn");

        getDialogPane().getStylesheets().add(getClass().getResource("/css/danhsachban.css").toExternalForm());

        VBox mainLayout = new VBox();
        mainLayout.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #D9D9D9; -fx-border-width: 1; -fx-border-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 15, 0, 0, 5);");

        HBox header = taoHeader();

        HBox body = taoBody();

        mainLayout.getChildren().addAll(header, body);
        getDialogPane().setContent(mainLayout);

        if (banCu.getViTri() == ViTri.LAU_2) {
            tang2.setSelected(true);
            viTriHienTai = ViTri.LAU_2;
        } else {
            tang1.setSelected(true);
            viTriHienTai = ViTri.LAU_1;
        }

        loadDanhSachBanTrong();

        getDialogPane().getButtonTypes().clear();
        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        Node closeNode = getDialogPane().lookupButton(ButtonType.CLOSE);
        closeNode.setVisible(false);
        closeNode.setManaged(false);

        getDialogPane().setPrefSize(800, 600);
        getDialogPane().setMaxSize(800, 600);
    }

    private HBox taoHeader() {
        Label title = new Label("Đổi bàn: " + banCu.getMaBan());
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");
        Button closeButton = new Button("X");
        closeButton.setStyle("-fx-background-color: transparent; -fx-font-size: 16px; -fx-font-weight: bold; -fx-cursor: hand;");
        closeButton.setOnAction(e -> close());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox headerPane = new HBox(title, spacer, closeButton);
        headerPane.setAlignment(Pos.CENTER_LEFT);
        headerPane.setPadding(new Insets(10, 15, 10, 15));
        headerPane.setStyle("-fx-background-color: #F7FAFC; -fx-border-width: 0 0 1 0; -fx-border-color: #D9D9D9;");
        return headerPane;
    }

    private HBox taoBody() {
        HBox body = new HBox(15);
        body.setPadding(new Insets(15));
        body.setAlignment(Pos.TOP_LEFT);

        // --- 1. Phần Trái: Chọn Bàn Trống ---
        VBox phanTrai = new VBox(10);
        phanTrai.setPrefWidth(450);
        phanTrai.setStyle("-fx-border-width: 0 1 0 0; -fx-border-color: #D9D9D9;");
        phanTrai.setPadding(new Insets(0, 15, 0, 0));

        Label lblKhuVuc = new Label("Khu vực");
        lblKhuVuc.getStyleClass().add("fontTieuDeNho");

        HBox nutTang = taoNutChonTang();

        VBox boxCanhBaoGhep = taoVungCanhBaoGhep();

        Label lblDSBanTrong = new Label("Danh sách bàn trống");
        lblDSBanTrong.getStyleClass().add("fontTieuDeNho");

        luoiBanTrong = taoLuoiBan();
        ScrollPane cuonLuoi = new ScrollPane(luoiBanTrong);
        cuonLuoi.setFitToWidth(true);
        cuonLuoi.setPrefHeight(450);
        cuonLuoi.getStyleClass().add("scroll-pane");

        phanTrai.getChildren().addAll(lblKhuVuc, nutTang, boxCanhBaoGhep, lblDSBanTrong, cuonLuoi);
        VBox.setVgrow(cuonLuoi, Priority.ALWAYS);

        // --- 2. Phần Phải: Thông tin bàn mới và Xác nhận ---
        VBox phanPhai = new VBox(20);
        phanPhai.setPrefWidth(300);

        Label lblThongTinMoi = new Label("Thông tin bàn mới");
        lblThongTinMoi.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // GridPane Thông tin chi tiết
        GridPane thongTinChiTiet = taoThongTinBanMoi();

        // Nút Xác nhận đổi
        Button btnXacNhan = new Button("Xác nhận đổi");
        btnXacNhan.getStyleClass().add("button-checkin");
        btnXacNhan.setPrefSize(200, 40);
        btnXacNhan.setOnAction(e -> xuLyXacNhanDoiBan());

        HBox boxXacNhan = new HBox(btnXacNhan);
        boxXacNhan.setAlignment(Pos.BOTTOM_RIGHT);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        phanPhai.getChildren().addAll(lblThongTinMoi, thongTinChiTiet, spacer, boxXacNhan);

        body.getChildren().addAll(phanTrai, phanPhai);
        return body;
    }

    private HBox taoNutChonTang() {
        HBox nutTang = new HBox(5);
        ToggleGroup buttonGroup = new ToggleGroup();

        tang1 = new ToggleButton("Tầng 1");
        tang1.setToggleGroup(buttonGroup);
        tang1.getStyleClass().add("nutTang");
        tang1.setOnAction(e -> {
            viTriHienTai = ViTri.LAU_1;
            loadDanhSachBanTrong();
        });

        tang2 = new ToggleButton("Tầng 2");
        tang2.setToggleGroup(buttonGroup);
        tang2.getStyleClass().add("nutTang");
        tang2.setOnAction(e -> {
            viTriHienTai = ViTri.LAU_2;
            loadDanhSachBanTrong();
        });

        nutTang.getChildren().addAll(tang1, tang2);
        return nutTang;
    }

    private GridPane taoLuoiBan() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setStyle("-fx-background-color: white");
        return grid;
    }


    private GridPane taoThongTinBanMoi() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(10));


        String labelStyle = "-fx-text-fill: #555; -fx-font-size: 14px; -fx-font-weight: normal;";

        String inputStyle = "-fx-background-color: white; -fx-opacity: 1; -fx-font-weight: bold;";

        // 1. Mã bàn
        Label lblMaBanTitle = new Label("Mã bàn:");
        lblMaBanTitle.setStyle(labelStyle);
        txtMaBanMoi = createInfoTextField("Chưa chọn", inputStyle);
        grid.addRow(0, lblMaBanTitle, txtMaBanMoi);

        // 2. Số chỗ
        Label lblSoChoTitle = new Label("Số chỗ:");
        lblSoChoTitle.setStyle(labelStyle);
        txtSoChoMoi = createInfoTextField("Chưa chọn", inputStyle);
        grid.addRow(1, lblSoChoTitle, txtSoChoMoi);

        // 3. Vị trí
        Label lblViTriTitle = new Label("Vị trí:");
        lblViTriTitle.setStyle(labelStyle);
        txtViTriMoi = createInfoTextField("Chưa chọn", inputStyle);
        grid.addRow(2, lblViTriTitle, txtViTriMoi);

        // --- 4. Ngày/Giờ ---

        HBox boxNgayGio = new HBox(10);
        boxNgayGio.setAlignment(Pos.CENTER_LEFT);
        boxNgayGio.getStyleClass().add("timKiem");
        boxNgayGio.setPadding(new Insets(5, 15, 5, 15));

        Label lblIconDongHo = new Label("🕒");
        lblIconDongHo.setStyle("-fx-font-size: 14px; -fx-text-fill: #4A5568;");

        lblThongBaoNgayGio = new Label(ngayDat.toString() + " | " +
                (banCu.getTrangThai() == TrangThai.DA_DAT ? "Theo PDB" : "Hiện tại"));
        lblThongBaoNgayGio.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #2D3748;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblIconLich = new Label("📅");
        lblIconLich.setStyle("-fx-font-size: 14px; -fx-text-fill: #4A5568;");

        boxNgayGio.getChildren().addAll(lblIconDongHo, lblThongBaoNgayGio, spacer, lblIconLich);

        Label lblNgayGioTitle = new Label("Ngày/giờ:");
        lblNgayGioTitle.setStyle(labelStyle);

        grid.addRow(3, lblNgayGioTitle, boxNgayGio);

        GridPane.setHgrow(txtMaBanMoi, Priority.ALWAYS);
        GridPane.setHgrow(txtSoChoMoi, Priority.ALWAYS);
        GridPane.setHgrow(txtViTriMoi, Priority.ALWAYS);
        GridPane.setHgrow(boxNgayGio, Priority.ALWAYS);

        return grid;
    }

    private TextField createInfoTextField(String text, String style) {
        TextField tf = new TextField(text);
        tf.setEditable(false);
        tf.getStyleClass().add("timKiem");
        tf.setStyle(style);
        return tf;
    }


    private void loadDanhSachBanTrong() {
        luoiBanTrong.getChildren().clear();

        List<BanAn> dsBanTrong = banAn_DAO.getTrangThaiBanTheoNgayVaViTri(viTriHienTai, ngayDat)
                .stream()
                .filter(ban -> ban.getTrangThai() == TrangThai.TRONG)
                .collect(java.util.stream.Collectors.toList());

        if (banCu.getTrangThai() == TrangThai.DANG_SU_DUNG && banCu.getViTri() == viTriHienTai) {
        } else if (banCu.getTrangThai() == TrangThai.DA_DAT && banCu.getViTri() == viTriHienTai) {
        }

        for (int i = 0; i < dsBanTrong.size(); i++) {
            BanAn ban = dsBanTrong.get(i);
            StackPane theBan = taoTheBan(ban);

            theBan.setOnMouseClicked(e -> {
                xuLyChonBanMoi(ban, theBan);
            });

            int hang = i / 3; // 3 cột
            int cot = i % 3;
            GridPane.setRowIndex(theBan, hang);
            GridPane.setColumnIndex(theBan, cot);
            luoiBanTrong.getChildren().add(theBan);
        }
    }
    private VBox taoVungCanhBaoGhep() {
        if (dsBanGhep == null || dsBanGhep.size() <= 1) {
            return new VBox(); // Trả về VBox rỗng nếu không phải bàn ghép
        }

        VBox box = new VBox(5);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: #FFF5F5; -fx-border-color: #DC3545; -fx-border-width: 1; -fx-background-radius: 5;");

        Label lblTieuDe = new Label("!LƯU Ý: BÀN GHÉP ĐANG CHỌN");
        lblTieuDe.setStyle("-fx-font-weight: bold; -fx-text-fill: #DC3545;");

        String dsBanStr = String.join(", ", dsBanGhep);
        Label lblThongTin = new Label(
                "Bàn " + banCu.getMaBan() + " thuộc nhóm " + dsBanGhep.size() + " bàn: " + dsBanStr +
                        ".\nViệc đổi bàn này sẽ chỉ chuyển Phiếu Đặt Bàn của riêng bàn này."
        );
        lblThongTin.setWrapText(true);
        lblThongTin.setStyle("-fx-font-size: 12px; -fx-text-fill: #9B2C2C;");

        box.getChildren().addAll(lblTieuDe, lblThongTin);
        return box;
    }

    private void xuLyChonBanMoi(BanAn ban, StackPane theBan) {
        VBox theVBoxMoi = (VBox) theBan.getChildren().get(1);

        if (banMoiDaChon == ban) {
            theVBoxMoi.getStyleClass().remove("theBan-selected-style");
            banMoiDaChon = null;

            capNhatThongTinChiTiet();
            return;
        }

        if (banMoiDaChon != null) {
            StackPane theCu = (StackPane) luoiBanTrong.getChildren().stream()
                    .filter(node -> node instanceof StackPane && ((StackPane)node).getUserData() == banMoiDaChon)
                    .findFirst().orElse(null);
            if (theCu != null) {
                VBox theVBoxCu = (VBox) theCu.getChildren().get(1);
                theVBoxCu.getStyleClass().remove("theBan-selected-style");
            }
        }

        banMoiDaChon = ban;
        theVBoxMoi.getStyleClass().add("theBan-selected-style");
        theBan.setUserData(ban);

        capNhatThongTinChiTiet();
    }

    private void capNhatThongTinChiTiet() {
        if (banMoiDaChon == null) {
            txtMaBanMoi.setText("000");
            txtSoChoMoi.setText("");
            txtViTriMoi.setText("");
        } else {
            int sucChua = (banMoiDaChon.getLoai().name().equals("VIP")) ? 6 : 4;

            txtMaBanMoi.setText(banMoiDaChon.getMaBan());
            txtSoChoMoi.setText(String.valueOf(sucChua) + " chỗ");
            txtViTriMoi.setText(banMoiDaChon.getViTri().name().replace("_", " "));
        }
    }

    private StackPane taoTheBan(BanAn ban) {
        StackPane khung = new StackPane();
        // Kích thước cố định
        khung.setPrefSize(140, 100);

        // 1. Dải màu trạng thái (Indicator)
        Region mauVien = new Region();
        mauVien.setPrefSize(10, 100);

        String indicatorColor;
        switch (ban.getTrangThai()) {
            case DANG_SU_DUNG:
                indicatorColor = "#32CD32"; // Xanh lá
                break;
            case DA_DAT:
                indicatorColor = "red";
                break;
            default: // TRONG
                indicatorColor = "#BDBDBD"; // Xám
                break;
        }
        mauVien.setStyle("-fx-background-color: " + indicatorColor + "; -fx-background-radius: 20;");

        // 2. Thẻ chính (VBox)
        VBox the = new VBox(5);
        the.setPrefSize(130, 100);
        the.setAlignment(Pos.CENTER);
        the.setPadding(new Insets(5));

        // Màu nền mặc định (Xanh đậm)
        the.setStyle("-fx-background-color: #0F375F; -fx-background-radius: 20; -fx-cursor: hand;");

        // Thêm hiệu ứng hover CSS (sẽ được định nghĩa trong block CSS bên dưới)
        the.getStyleClass().add("theBan-doiban");
        the.setStyle("-fx-background-color: #082744; -fx-background-radius: 20; -fx-cursor: hand;");

        // 3. Nội dung (Mã bàn và Số chỗ)
        Label nhanBan = new Label(ban.getMaBan());
        nhanBan.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        nhanBan.setTextFill(Color.WHITE);

        // Tính sức chứa (Inline Logic)
        int sucChua = (ban.getLoai().name().equals("VIP")) ? 6 : 4;

        Label soCho = new Label(sucChua + " chỗ");
        soCho.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        soCho.setTextFill(Color.WHITE);

        // 4. Icon VIP (Góc trên bên phải)
        HBox hboxIcons = new HBox();
        hboxIcons.setMinHeight(15);
        hboxIcons.setAlignment(Pos.TOP_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        hboxIcons.getChildren().add(spacer);

        ImageView iconVip = new ImageView(new Image(getClass().getResource("/img/vipicon.png").toExternalForm()));
        iconVip.setFitHeight(15);
        iconVip.setFitWidth(15);
        iconVip.setVisible(ban.getLoai() == LoaiBan.VIP);
        hboxIcons.getChildren().add(iconVip);

        // 5. Kết hợp các phần tử
        the.getChildren().addAll(hboxIcons, nhanBan, soCho);
        VBox.setMargin(hboxIcons, new Insets(0, 5, 0, 0));

        // Thêm các thành phần cố định vào StackPane khung
        khung.getChildren().addAll(mauVien, the);
        StackPane.setAlignment(mauVien, Pos.CENTER_LEFT);
        StackPane.setMargin(the, new Insets(0, 0, 0, 5));

        khung.getStylesheets().add("data:text/css,"
                        // Hiệu ứng HOVER: Chuyển sang màu xanh nhạt hơn/sáng hơn
                        + ".theBan-doiban:hover { -fx-background-color: #1A4673; }"

                        // Trạng thái SELECTED: Áp dụng màu NHẠT HƠN tương tự như hover để báo hiệu đã chọn
                        // Màu này phải khác biệt so với màu mặc định #0F375F
                        + ".theBan-doiban.theBan-selected-style { "
                        + "-fx-background-color: #2D3748; " // Màu xám xanh (hơi sáng hơn)
                        + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 5, 0, 0, 2); }"

                // Cần đảm bảo rằng hiệu ứng selected ghi đè hiệu ứng hover nếu cả hai xảy ra
        );

        return khung;
    }

    private void xuLyXacNhanDoiBan() {
        if (banMoiDaChon == null) {
            showAlert(AlertType.ERROR, "Lỗi", "Vui lòng chọn bàn mới để đổi.");
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION,
                "Xác nhận đổi bàn từ " + banCu.getMaBan() + " sang " + banMoiDaChon.getMaBan() + " không?",
                ButtonType.YES, ButtonType.NO);
        alert.setTitle("Xác nhận Đổi Bàn");
        alert.setHeaderText(null);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {

                TrangThai trangThaiCu = banCu.getTrangThai();

                if (trangThaiCu != TrangThai.DA_DAT && trangThaiCu != TrangThai.DANG_SU_DUNG) {
                    showAlert(AlertType.ERROR, "Lỗi", "Trạng thái bàn cũ không hợp lệ để đổi.");
                    return;
                }

                boolean doiTongHopThanhCong = phieuDatBan_DAO.doiBanTongHop(
                        banCu, banMoiDaChon, ngayDat, trangThaiCu
                );

                if (doiTongHopThanhCong) {
                    showAlert(AlertType.INFORMATION, "Thành công",
                            "Đã đổi bàn thành công từ " + banCu.getMaBan() + " sang " + banMoiDaChon.getMaBan() + ".");
                    setResult(ButtonType.OK);
                    close();
                } else {
                    showAlert(AlertType.ERROR, "Lỗi", "Đổi bàn thất bại. Vui lòng kiểm tra lại DAO hoặc trạng thái bàn.");
                }
            }
        });

    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}