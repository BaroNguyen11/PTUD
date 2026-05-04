package client.gui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import client.service.BanAnClient;
import client.service.PhieuDatBanClient;
import common.entity.BanAn;
import common.entity.LoaiBan;
import common.entity.TrangThai;
import common.entity.ViTri;
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
    private BanAnClient banAnClient;
    private PhieuDatBanClient phieuDatBanClient;
    private List<String> dsBanGhep;
    private Window ownerWindow;

    // OUTPUT
    private List<BanAn> dsBanMoiDaChon = new ArrayList<>();

    // UI
    private TextField txtMaBanMoi;
    private TextField txtSoChoMoi;
    private DatePicker pickerNgayChuyen;
    private GridPane luoiBanTrong;
    private ToggleButton tang1, tang2;
    private ViTri viTriHienTai = ViTri.LAU_1;

    public Gui_DoiBan(Window owner, BanAn banCu, LocalDate ngayDat, BanAnClient banAnClient, PhieuDatBanClient phieuDatBanClient, List<String> dsBanGhep) {
        this.banCu = banCu;
        this.ngayDat = ngayDat;
        this.banAnClient = banAnClient;
        this.phieuDatBanClient = phieuDatBanClient;
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

        pickerNgayChuyen = new DatePicker(ngayDat);
        pickerNgayChuyen.setPrefWidth(200);
        pickerNgayChuyen.setStyle("-fx-font-size: 14px;");
        pickerNgayChuyen.setEditable(false);
        if (banCu.getTrangThai() == TrangThai.DANG_SU_DUNG) {
            pickerNgayChuyen.setDisable(true);
            pickerNgayChuyen.setStyle("-fx-opacity: 1; -fx-background-color: #EDF2F7;"); // Giữ màu cho dễ nhìn dù disable
        } else {
            pickerNgayChuyen.setDisable(false);
        }
        addInfoRow(infoGrid, 0, "BÀN ĐÃ CHỌN:", txtMaBanMoi);
        addInfoRow(infoGrid, 2, "TỔNG SỐ CHỖ:", txtSoChoMoi);

        Label lblTime = new Label("THỜI GIAN:");
        lblTime.setStyle("-fx-text-fill: #718096; -fx-font-size: 11px; -fx-font-weight: bold;");
        infoGrid.add(lblTime, 0, 4);

        pickerNgayChuyen.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                this.ngayDat = newVal; // Cập nhật biến toàn cục
                loadDanhSachBanTrong(); // Load lại lưới bàn bên trái

                // Reset lại các bàn đã chọn vì sang ngày mới bàn đó có thể không trống
                dsBanMoiDaChon.clear();
                capNhatThongTinPhai();
            }
        });

        HBox timeBox = new HBox(10, new Label("📅"), pickerNgayChuyen);
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
        List<BanAn> dsBanTrong = banAnClient.getTrangThaiBanTheoNgayVaViTri(viTriHienTai, ngayDat)
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
        String maHD = banAnClient.getMaHoaDonTuBan(dsMaBanCu.get(0));

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

                        // FIX 2: GỌI HÀM service VỚI ĐỦ 4 THAM SỐ
                        ok = phieuDatBanClient.chuyenBanNhieuSangNhieu(dsMaBanCu, dsMaBanMoi, maHD, trangThaiMoi,this.ngayDat);

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