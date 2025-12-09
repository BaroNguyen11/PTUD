////package gui;
////
////import java.time.LocalDate;
////import java.util.ArrayList;
////import java.util.List;
////import java.util.Optional;
////
////import ctrl.CheckIn_Ctrl;
////import dao.BanAn_DAO;
////import dao.CheckIn_DAO;
////import dao.KhachHang_DAO;
////import dao.PhieuDatBan_DAO;
////import entity.BanAn;
////import entity.KhachHang;
////import entity.LoaiBan;
////import entity.NhanVien;
////import entity.PhieuDatBan;
////import entity.TrangThai;
////import entity.ViTri;
////import javafx.geometry.Insets;
////import javafx.geometry.Pos;
////import javafx.scene.Node;
////import javafx.scene.control.Alert;
////import javafx.scene.control.Alert.AlertType;
////import javafx.scene.control.Button;
////import javafx.scene.control.ButtonType;
////import javafx.scene.control.ComboBox;
////import javafx.scene.control.DatePicker;
////import javafx.scene.control.Dialog;
////import javafx.scene.control.Label;
////import javafx.scene.control.ScrollPane;
////import javafx.scene.control.Separator;
////import javafx.scene.control.TextField;
////import javafx.scene.control.ToggleButton;
////import javafx.scene.control.ToggleGroup;
////import javafx.scene.image.Image;
////import javafx.scene.image.ImageView;
////import javafx.scene.layout.BorderPane;
////import javafx.scene.layout.GridPane;
////import javafx.scene.layout.HBox;
////import javafx.scene.layout.Priority;
////import javafx.scene.layout.Region;
////import javafx.scene.layout.StackPane;
////import javafx.scene.layout.VBox;
////import javafx.scene.paint.Color;
////import javafx.scene.shape.Circle;
////import javafx.scene.text.Font;
////import javafx.scene.text.FontWeight;
////import javafx.stage.StageStyle;
////import javafx.stage.Window;
////
////public class Gui_DanhSachBan extends BorderPane {
////    private GridPane luoiBan;
////    private BanAn_DAO banAn_DAO;
////    private Gui_TrangChu trangChu;
////    private List<BanAn> danhSachBanDaChon = new ArrayList<>();
////    private TextField timKiem;
////    private ComboBox<String> cmbTatCa;
////    private ToggleButton tang1;
////    private ToggleButton tang2;
////    private ViTri viTriHienTai = ViTri.LAU_1;
////    private DatePicker datePicker;
////    private LocalDate ngayChon = LocalDate.now();
////    private PhieuDatBan_DAO phieuDatBan_DAO;
////    private CheckIn_Ctrl controlCheckIn = new CheckIn_Ctrl();
////
////    public Gui_DanhSachBan(Gui_TrangChu trangChu) {
////
////        this.trangChu = trangChu;
////        banAn_DAO = new BanAn_DAO();
////        phieuDatBan_DAO = new PhieuDatBan_DAO();
////        this.setStyle("-fx-background-color: white;");
////        HBox phanTren = taoPhanTren();
////        HBox phanGiua = taoPhanGiua();
////        VBox phanGiuaAll = new VBox();
////        phanGiuaAll.getChildren().addAll(phanTren, phanGiua);
////        this.setCenter(phanGiuaAll);
////        loadDataToGrid();
////
////        this.getStylesheets().add(getClass().getResource("/css/danhsachban.css").toExternalForm());
////    }
////
////    // Tạo phần trên với nút tầng và trạng thái
////    private HBox taoPhanTren() {
////        VBox top = new VBox(5);
////        top.setPadding(new Insets(10));
///// / lbl chon tang
////        Label lblChonTang = new Label("Khu vực");
////        lblChonTang.getStyleClass().add("fontTieuDeNho");
////// Nút chọn tầng
////        HBox nutTang = new HBox(5);
////        nutTang.setAlignment(Pos.TOP_LEFT);
////        nutTang.setMinWidth(400);
////        tang1 = new ToggleButton("Tầng 1");
////        tang1.setPrefSize(70, 40);
////        tang2 = new ToggleButton("Tầng 2");
////        tang2.setPrefSize(70, 40);
////        ToggleGroup buttonGroup = new ToggleGroup();
////        tang1.setToggleGroup(buttonGroup);
////        tang2.setToggleGroup(buttonGroup);
////        tang1.getStyleClass().add("nutTang");
////        tang2.getStyleClass().add("nutTang");
////        nutTang.getChildren().addAll(tang1, tang2);
////
////        tang1.setSelected(true);
////        tang1.setOnAction(e -> {
////            viTriHienTai = ViTri.LAU_1;
////            loadDataToGrid();
////        });
////        tang2.setOnAction(e -> {
////            viTriHienTai = ViTri.LAU_2;
////            loadDataToGrid();
////        });
////
////        datePicker = new DatePicker(ngayChon);
////        datePicker.setPrefSize(120, 40);
////        datePicker.getStyleClass().add("datepicker-custom");
////
////        datePicker.setOnAction(e -> {
////            LocalDate ngayMoi = datePicker.getValue();
////            if (ngayMoi.isBefore(LocalDate.now())) {
////                showAlert(AlertType.ERROR, "Ngày không hợp lệ", "Không thể chọn ngày trong quá khứ.");
////                datePicker.setValue(ngayChon); // Đặt lại ngày
////            } else {
////                ngayChon = ngayMoi;
////                loadDataToGrid(); // Tải lại dữ liệu theo ngày mới
////            }
////        });
////
////        VBox boxKhuVuc = new VBox(5, lblChonTang, nutTang);
////
////        // VBox Chọn Ngày
////        Label lblNgayDen = new Label("Ngày đến");
////        lblNgayDen.getStyleClass().add("fontTieuDeNho");
////
////        // DatePicker (đã có logic setOnAction và kiểm tra ngày)
////        VBox boxNgay = new VBox(5, lblNgayDen, datePicker);
////
////        // HBox gom Khu vực và Ngày
////        HBox boxKhuVucVaNgay = new HBox(40, boxKhuVuc, boxNgay);
////        boxKhuVucVaNgay.setAlignment(Pos.TOP_LEFT);
////
////
////
////        // Ô tìm kiếm
////        Label lblTiemKiem = new Label("Tìm kiếm bàn");
////        lblTiemKiem.getStyleClass().add("fontTieuDeNho");
////        // biến toàn cục
////        timKiem = new TextField();
////        timKiem.setPromptText("Tìm kiếm bằng mã bàn");
////        timKiem.getStyleClass().add("timKiem");
////        Button nutTimKiem = new Button("Tìm kiếm");
////        nutTimKiem.getStyleClass().add("button-timKiem");
////        // SỬA: Dùng biến toàn cục
////        cmbTatCa = new ComboBox<>();
////        cmbTatCa.getItems().addAll("Tất cả", "Bàn trống", "Đang sử dụng", "Đã đặt bàn", "Bàn VIP");
////        cmbTatCa.setValue("Tất cả");
////        cmbTatCa.setPrefWidth(100);
////        cmbTatCa.getStyleClass().add("timBan");
////
////        HBox oTimKiem = new HBox(10, timKiem, nutTimKiem, cmbTatCa);
////        oTimKiem.setAlignment(Pos.CENTER_LEFT);
////        nutTimKiem.setOnAction(e -> loadDataToGrid());
////        cmbTatCa.setOnAction(e -> loadDataToGrid());
////        // Trạng thái chức vụ
////        VBox trangThai = new VBox(2);
////        trangThai.setPrefWidth(150);
////        trangThai.setPadding(new Insets(10));
////        Label chucVu = new Label("Chú thích");
////        chucVu.getStyleClass().add("fontTieuDeNho");
////        // Chú thích VIP
////        Label vip = new Label("Bàn VIP");
////        vip.setStyle("-fx-text-fill: #ed8936; -fx-font-weight: bold; -fx-font-size: 11;");
////        HBox chuThich1 = new HBox(5);
////        ImageView iconVIP = new ImageView(new Image("img/vipicon.png"));
////        iconVIP.setFitWidth(15);
////        iconVIP.setFitHeight(15);
////        chuThich1.getChildren().addAll(iconVIP, vip);
////        // Chú thích Đang chọn
////        Label dangChon = new Label("Đang trống");
////        dangChon.setStyle("-fx-text-fill: gray; -fx-font-weight: bold; -fx-font-size: 11;");
////        HBox chuThich2 = new HBox(5);
////        Circle dotGray = new Circle(5, Color.web("#BDBDBD"));
////        chuThich2.getChildren().addAll(dotGray, dangChon);
////        // Chú thích Đang sử dụng
////        Label dangSuDung = new Label("Đang sử dụng");
////        dangSuDung.setStyle("-fx-text-fill: #38A169; -fx-font-weight: bold; -fx-font-size: 11;");
////        HBox chuThich4 = new HBox(5);
////        Circle dotGreen = new Circle(5, Color.web("#38A169"));
////        chuThich4.getChildren().addAll(dotGreen, dangSuDung);
////        // Chú thích Đã đặt bàn
////        Label daBan = new Label("Đã đặt bàn");
////        daBan.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: 11;");
////        HBox chuThich3 = new HBox(5);
////        Circle dotRed = new Circle(5, Color.RED);
////        chuThich3.getChildren().addAll(dotRed, daBan);
////        trangThai.getChildren().addAll(chucVu, chuThich1, chuThich2, chuThich4, chuThich3);
////
////        top.getChildren().addAll(boxKhuVucVaNgay, lblTiemKiem, oTimKiem);
////        top.setStyle("-fx-border-width: 0 1 0 0; -fx-border-color: #D9D9D9");
////
////        HBox topALL = new HBox(40);
////        topALL.getChildren().addAll(top, trangThai);
////        return topALL;
////    }
////
////    private HBox taoPhanGiua() {
////        HBox giua = new HBox(20);
////        giua.setPadding(new Insets(10));
////        // Phần trái
////        VBox trai = new VBox(10);
////        Label danhSach = new Label("Danh sách bàn");
////        danhSach.getStyleClass().add("fontTieuDeNho");
////        luoiBan = taoLuoiBan();
////        ScrollPane cuonLuoi = new ScrollPane(luoiBan);
////        cuonLuoi.setFitToWidth(true);
////        cuonLuoi.setPrefHeight(620);
////        cuonLuoi.getStyleClass().add("scroll-pane");
////        // Nút Đặt bàn
////        Button btnDatBan = new Button("Đặt bàn");
////        btnDatBan.getStyleClass().add("button-checkin");
////        btnDatBan.setPrefSize(120, 40);
////        // thêm
////        btnDatBan.setOnAction(e -> {
////            xuLyDatBan();
////        });
////        HBox boxDatBan = new HBox(btnDatBan);
////        boxDatBan.setAlignment(Pos.BOTTOM_RIGHT);
////        boxDatBan.setPadding(new Insets(10, 20, 10, 10));
////        trai.getChildren().addAll(danhSach, cuonLuoi, boxDatBan);
////        VBox.setVgrow(cuonLuoi, Priority.ALWAYS);
////        giua.getChildren().addAll(trai);
////        HBox.setHgrow(trai, Priority.ALWAYS);
////        return giua;
////    }
////
////    private GridPane taoLuoiBan() {
////        GridPane grid = new GridPane();
////        grid.setHgap(15);
////        grid.setVgap(15);
////        grid.setPadding(new Insets(20));
////        grid.setStyle("-fx-background-color: white");
////        return grid;
////    }
////
////    private void loadDataToGrid() {
////// 1. Xóa bàn cũ và reset lựa chọn
////        luoiBan.getChildren().clear();
////        danhSachBanDaChon.clear();
////// 2. Lấy tất cả bàn từ DAO (chỉ theo tầng)
////        List<BanAn> dsBanAnFull = banAn_DAO.getTrangThaiBanTheoNgayVaViTri(viTriHienTai, ngayChon);
////// 3. Lấy giá trị từ các bộ lọc
////        String tuKhoa = timKiem.getText().trim().toLowerCase();
////        String loaiLoc = cmbTatCa.getValue();
////// 4. Lọc danh sách
////        List<BanAn> dsDaLoc = new ArrayList<>();
////        for (BanAn ban : dsBanAnFull) {
////            boolean khopTuKhoa = true;
////            boolean khopLoaiLoc = true;
////            if (!tuKhoa.isEmpty()) {
////                khopTuKhoa = ban.getMaBan().toLowerCase().contains(tuKhoa);
////            }
////            switch (loaiLoc) {
////                case "Bàn trống":
////                    khopLoaiLoc = (ban.getTrangThai() == TrangThai.TRONG);
////                    break;
////                case "Đang sử dụng":
////                    khopLoaiLoc = (ban.getTrangThai() == TrangThai.DANG_SU_DUNG);
////                    break;
////                case "Đã đặt bàn":
////                    khopLoaiLoc = (ban.getTrangThai() == TrangThai.DA_DAT);
////                    break;
////                case "Bàn VIP":
////                    khopLoaiLoc = (ban.getLoai() == LoaiBan.VIP);
////                    break;
////                default:
////                    khopLoaiLoc = true;
////                    break;
////            }
////            if (khopTuKhoa && khopLoaiLoc) {
////                dsDaLoc.add(ban);
////            }
////        }
////// 5. Hiển thị các bàn ĐÃ LỌC lên lưới
////        List<StackPane> danhSachTheBan = new ArrayList<>();
////        for (int i = 0; i < dsDaLoc.size(); i++) {
////            BanAn ban = dsDaLoc.get(i);
////            StackPane theBan = taoTheBan(ban);
////            danhSachTheBan.add(theBan);
////            theBan.setOnMouseClicked(e -> {
////                VBox theChon = (VBox) theBan.getChildren().get(1);
////                if (danhSachBanDaChon.contains(ban)) {
////                    danhSachBanDaChon.remove(ban);
////                    theChon.getStyleClass().add("theBan");
////                    theChon.setStyle("");
////                } else {
////                    if (ban.getTrangThai() == TrangThai.TRONG) {
////                        danhSachBanDaChon.add(ban);
////                        theChon.getStyleClass().removeAll("theBan");
////                        theChon.setStyle("-fx-background-color: #BDBDBD;" + "-fx-background-radius: 20;"
////                                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);"
////                                + "-fx-cursor: hand;");
////                    } else {
////                        showAlert(AlertType.WARNING, "Không thể chọn", "Chỉ có thể chọn bàn đang 'Trống'.");
////                    }
////                }
////            });
////// 6. Thêm vào lưới
////            int hang = i / 5;
////            int cot = i % 5;
////            GridPane.setRowIndex(theBan, hang);
////            GridPane.setColumnIndex(theBan, cot);
////            luoiBan.getChildren().add(theBan);
////        }
////    }
////
////    private void xuLyDatBan() {
////        if (danhSachBanDaChon.isEmpty()) {
////            showAlert(AlertType.ERROR, "Chưa chọn bàn", "Vui lòng click chọn ít nhất một bàn để đặt.");
////            return;
////        }
////        for (BanAn ban : danhSachBanDaChon) {
////            if (ban.getTrangThai() != TrangThai.TRONG) {
////                showAlert(AlertType.WARNING, "Bàn không hợp lệ",
////                        "Trong danh sách có bàn " + ban.getMaBan() + " không 'Trống'.");
////                return;
////            }
////        }
////
////        try {
////            LocalDate ngayDat = datePicker.getValue();
////
////            Gui_DatBan guiDatBan = new Gui_DatBan(trangChu, danhSachBanDaChon, ngayDat);
////
////            trangChu.setMainContent(guiDatBan);
////        } catch (Exception e) {
////            e.printStackTrace();
////            new Gui_DatBan(trangChu, danhSachBanDaChon, datePicker.getValue());
////        }
////
////    }
////
////    private void showAlert(AlertType alertType, String title, String content) {
////        Alert alert = new Alert(alertType);
////        alert.setTitle(title);
////        alert.setHeaderText(null);
////        alert.setContentText(content);
////        alert.showAndWait();
////    }
////
////    private StackPane taoTheBan(BanAn ban) {
////        StackPane khung = new StackPane();
////        khung.setPrefSize(220, 130);
////// Viền thay đổi màu theo trạng thái
////        Region mauVien = new Region();
////        mauVien.setPrefSize(15, 130);
////        String indicatorColor;
////        switch (ban.getTrangThai()) {
////            case DANG_SU_DUNG:
////                indicatorColor = "#32CD32";
////                break;
////            case DA_DAT:
////                indicatorColor = "red";
////                break;
////            default:
////                indicatorColor = "#BDBDBD";
////                break;
////        }
////        mauVien.setStyle("-fx-background-color: " + indicatorColor + "; -fx-background-radius: 20;");
////// ----- Thẻ chính màu xanh -----
////        VBox the = new VBox(10);
////        the.setPrefSize(210, 130);
////        the.setAlignment(Pos.CENTER);
////        the.setPadding(new Insets(0, 10, 0, 10));
////        the.getStyleClass().add("theBan");
////        Label nhanBan = new Label(ban.getMaBan());
////        nhanBan.setFont(Font.font("Arial", FontWeight.BOLD, 20));
////        nhanBan.setTextFill(Color.WHITE);
////        Button btnXemThongTin = new Button("Xem thông tin");
////        btnXemThongTin.setStyle("-fx-background-color: #6B49C7; " + // Màu tím
////                "-fx-text-fill: white; " + "-fx-font-weight: bold; " + "-fx-font-size: 14px;"
////                + "-fx-background-radius: 15;" + "-fx-cursor: hand;");
////        btnXemThongTin.setPrefWidth(150);
////
////        HBox hboxIcons = new HBox(5);
////        hboxIcons.setMinHeight(20);
////        hboxIcons.setAlignment(Pos.CENTER_LEFT);
////
////        if (ban.getTrangThai() == TrangThai.DANG_SU_DUNG || ban.getTrangThai() == TrangThai.DA_DAT) {
////            String maHDGop = banAn_DAO.getMaHoaDonTuBan(ban.getMaBan(), ngayChon);
////            boolean isMerged = (maHDGop != null && banAn_DAO.getDanhSachBanCungHoaDon(maHDGop).size() > 1);
////
////            if (isMerged) {
////                // Sửa đường dẫn để đảm bảo tìm thấy tài nguyên
////                ImageView iconLink = new ImageView(new Image(getClass().getResource("/img/link.png").toExternalForm()));
////                iconLink.setFitWidth(20);
////                iconLink.setFitHeight(20);
////                hboxIcons.getChildren().add(iconLink);
////            }
////        }
////
////        Region spacer = new Region();
////        HBox.setHgrow(spacer, Priority.ALWAYS);
////        hboxIcons.getChildren().add(spacer);
////
////        ImageView iconVip = new ImageView(new Image(getClass().getResource("/img/vipicon.png").toExternalForm()));
////        iconVip.setFitHeight(20);
////        iconVip.setFitWidth(20);
////        iconVip.setVisible(ban.getLoai() == LoaiBan.VIP);
////        hboxIcons.getChildren().add(iconVip);
////
////        // Thêm các phần tử vào VBox the (Thẻ chính)
////        the.getChildren().addAll(hboxIcons, nhanBan, btnXemThongTin);
////
////        btnXemThongTin.setOnAction(e -> {
////            showTableInfoDialog(ban);
////            e.consume();
////        });
////
////        // Thêm các thành phần cố định vào StackPane khung
////        khung.getChildren().addAll(mauVien, the);
////        StackPane.setAlignment(mauVien, Pos.CENTER_LEFT);
////        StackPane.setMargin(the, new Insets(0, 0, 0, 5));
////
////        return khung;
////    }
////
////    private void showTableInfoDialog(BanAn ban) {
////        Dialog<Void> dialog = new Dialog<>();
////        dialog.initOwner(luoiBan.getScene().getWindow());
////        dialog.initStyle(StageStyle.TRANSPARENT);
////        dialog.getDialogPane().getScene().setFill(Color.TRANSPARENT);
////// Header
////        dialog.setHeaderText(null);
////        dialog.setGraphic(null);
////        Label title = new Label(ban.getMaBan());
////        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");
////        Button closeButton = new Button("X");
////        closeButton.setStyle(
////                "-fx-background-color: transparent; -fx-font-size: 16px; -fx-font-weight: bold; -fx-cursor: hand;");
////        closeButton.setOnAction(e -> dialog.close());
////        Region spacer = new Region();
////        HBox.setHgrow(spacer, Priority.ALWAYS);
////        HBox headerPane = new HBox(title, spacer, closeButton);
////        headerPane.setAlignment(Pos.CENTER_LEFT);
////        headerPane.setPadding(new Insets(10, 10, 10, 15));
////        headerPane.setStyle("-fx-background-color: #F7FAFC;");
////        Separator separator = new Separator();
////
////        boolean isBookedOrInUse = (ban.getTrangThai() == TrangThai.DA_DAT || ban.getTrangThai() == TrangThai.DANG_SU_DUNG);
////
////        PhieuDatBan pdbInfo = null;
////        if (isBookedOrInUse) {
////
////            LocalDate ngayDat = datePicker.getValue();
////
////            pdbInfo = phieuDatBan_DAO.getPhieuDatBanByMaBanVaNgay(ban.getMaBan(), ngayDat);
////        }
////
////        String statusText, subText, bgColor, textColor;
////        switch (ban.getTrangThai()) {
////            case DANG_SU_DUNG:
////                statusText = "Đang phục vụ";
////                subText = "Bàn đang có khách sử dụng";
////                bgColor = "#F0FFF4";
////                textColor = "#22543D";
////                break;
////            case DA_DAT:
////                statusText = "Bàn đã đặt";
////                subText = "Bàn đã được khách đặt trước";
////                bgColor = "#FFF5F5";
////                textColor = "#9B2C2C";
////                break;
////            default: // TRONG
////                statusText = "Bàn trống";
////                subText = "Sẵn sàng phục vụ khách";
////                bgColor = "#FFFBEB";
////                textColor = "#975A16";
////                break;
////        }
////
////        Label lblStatus = new Label(statusText);
////        lblStatus.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: " + textColor + ";");
////        Label lblSubText = new Label(subText);
////        lblSubText.setStyle("-fx-font-size: 14px; -fx-text-fill: " + textColor + ";");
////        VBox statusBox = new VBox(5, lblStatus, lblSubText);
////        statusBox.setPadding(new Insets(15));
////        statusBox.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 8;");
////
////        Label lblLoaiBan = new Label("Loại bàn: " + ban.getLoai().name());
////        lblLoaiBan.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
////        Label lblViTri = new Label("Vị trí: " + ban.getViTri().name().replace("_", " "));
////        lblViTri.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
////        VBox extraInfoBox = new VBox(5, lblLoaiBan, lblViTri);
////        extraInfoBox.setPadding(new Insets(15, 0, 0, 0));
////
////        VBox bookingInfoBox = new VBox(5);
////        bookingInfoBox.setPadding(new Insets(5, 0, 0, 0));
////        if (pdbInfo != null) {
////            bookingInfoBox.getChildren().add(new Label("Tên khách hàng: " + pdbInfo.getKhachHang().getTenKhachHang()));
////            bookingInfoBox.getChildren().add(new Label("SĐT: " + pdbInfo.getKhachHang().getSoDienThoai()));
////
////            String ghiChu = (pdbInfo.getGhiChu() != null && !pdbInfo.getGhiChu().trim().isEmpty())
////                    ? pdbInfo.getGhiChu() : "Không có ghi chú.";
////            bookingInfoBox.getChildren().add(new Label("Ghi chú: " + ghiChu));
////
////            for(Node node : bookingInfoBox.getChildren()) {
////                if (node instanceof Label) {
////                    ((Label) node).setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
////                }
////            }
////        }
////
////
////
////        VBox mainLayout = new VBox(headerPane, separator, statusBox, extraInfoBox);
////        mainLayout.setSpacing(0);
////        mainLayout.setPrefWidth(350);
////
////        if (pdbInfo != null) {
////            mainLayout.getChildren().add(bookingInfoBox);
////        }
////
////        if (ban.getTrangThai() == TrangThai.DANG_SU_DUNG || ban.getTrangThai() == TrangThai.DA_DAT) {
////            String maHDGop = banAn_DAO.getMaHoaDonTuBan(ban.getMaBan(), ngayChon);
////
////            if (maHDGop != null) {
////                List<String> dsBanGhep = banAn_DAO.getDanhSachBanCungHoaDon(maHDGop);
////
////                if (dsBanGhep.size() > 1) {
////                    // Đây là một bàn ghép, hiển thị danh sách các bàn chung hóa đơn
////                    Label lblGhepBanTitle = new Label("Bàn ghép cùng :");
////                    lblGhepBanTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #007BFF;");
////
////                    Label lblBanGhep = new Label(String.join(", ", dsBanGhep));
////                    lblBanGhep.setStyle("-fx-font-weight: normal; -fx-font-size: 14px;");
////
////                    VBox groupInfoBox = new VBox(5, lblGhepBanTitle, lblBanGhep);
////                    groupInfoBox.setPadding(new Insets(15, 0, 0, 0));
////
////                    mainLayout.getChildren().add(groupInfoBox);
////                }
////            }
////        }
////
////        HBox actionButtonsBox = new HBox(10);
////        actionButtonsBox.setAlignment(Pos.CENTER_RIGHT);
////        actionButtonsBox.setPadding(new Insets(15, 10, 10, 10));
////
////        Separator bottomSeparator = new Separator();
////
////
////        switch (ban.getTrangThai()) {
////            case DANG_SU_DUNG:
////                // 1. Nút Đổi bàn
////                Button btnDoiBan_SD = createActionButton("Đổi bàn", "#FFC107"); // Màu Vàng
////                btnDoiBan_SD.setOnAction(e -> xuLyDoiBan(ban));
////
////                // 2. Nút Thanh toán
////                Button btnThanhToan = createActionButton("Thanh toán", "#38A169"); // Màu Xanh lá
////                btnThanhToan.setOnAction(e -> xuLyThanhToan(banAn_DAO.getMaHoaDonTuBan(ban.getMaBan(), ngayChon)));
////
////                // 3. Nút Gọi món
////                Button btnGoiMon = createActionButton("Gọi món", "#6B49C7"); // Màu Tím
////                btnGoiMon.setOnAction(e -> xuLyGoiMon(ban));
////
////                actionButtonsBox.getChildren().addAll(btnDoiBan_SD, btnThanhToan, btnGoiMon);
////                break;
////
////            case DA_DAT:
////                // 1. Nút Đổi bàn
////                Button btnDoiBan_DB = createActionButton("Đổi bàn", "#FFC107"); // Màu Vàng
////                btnDoiBan_DB.setOnAction(e -> xuLyDoiBan(ban));
////
////                // 2. Nút Hủy bàn
////                Button btnHuyBan = createActionButton("Hủy bàn", "#DC3545"); // Màu Đỏ
////                btnHuyBan.setOnAction(e -> xuLyHuyBan(ban));
////
////                // 3. Nút Check-in
////                Button btnCheckIn = createActionButton("Check-in", "#007BFF"); // Màu Xanh dương
////                btnCheckIn.setOnAction(e -> xuLyCheckIn(ban, dialog));
////
////                actionButtonsBox.getChildren().addAll(btnDoiBan_DB, btnHuyBan, btnCheckIn);
////                break;
////
////            case TRONG:
////                break;
////        }
////
////        if (!actionButtonsBox.getChildren().isEmpty()) {
////            mainLayout.getChildren().addAll(bottomSeparator, actionButtonsBox);
////        }
////
////
////        dialog.getDialogPane().setContent(mainLayout);
////        dialog.getDialogPane().getStylesheets().add("data:text/css,"
////                + ".dialog-pane { -fx-background-color: white; -fx-padding: 0; "
////                + "-fx-border-color: #D9D9D9; -fx-border-width: 1; -fx-background-radius: 8; "
////                + "-fx-border-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5); }"
////                + ".dialog-pane .content { -fx-padding: 0; }");
////        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
////        Node closeNode = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
////        closeNode.setVisible(false);
////        closeNode.setManaged(false);
////        dialog.showAndWait();
////    }
////    private Button createActionButton(String text, String color) {
////        Button btn = new Button(text);
////        btn.setStyle("-fx-background-color: " + color + "; " +
////                "-fx-text-fill: white; " +
////                "-fx-font-weight: bold; " +
////                "-fx-font-size: 12px;" +
////                "-fx-background-radius: 5;" +
////                "-fx-cursor: hand;");
////        return btn;
////    }
////
////    // Xử lý logic cho Đổi Bàn
////    private void xuLyDoiBan(BanAn banCu) {
////
////        String maHDGop = banAn_DAO.getMaHoaDonTuBan(banCu.getMaBan(), ngayChon);
////        List<String> dsBanGhep = (maHDGop != null) ? banAn_DAO.getDanhSachBanCungHoaDon(maHDGop) : new ArrayList<>();
////
////        // Không cần Alert xác nhận/cảnh báo ở đây nếu bạn muốn hiển thị nó trong Dialog.
////
////        Window owner = this.getScene().getWindow();
////
////        // TẠO DIALOG DÙNG CONSTRUCTOR MỚI (6 tham số)
////        Gui_DoiBan dialog = new Gui_DoiBan(owner, banCu, ngayChon, banAn_DAO, phieuDatBan_DAO, dsBanGhep);
////
////        dialog.showAndWait().ifPresent(result -> {
////            if (result == ButtonType.OK) {
////                loadDataToGrid();
////            }
////        });
////    }
////
////    // Xử lý logic cho Gọi Món
////    private void xuLyGoiMon(BanAn ban) {
////        showAlert(AlertType.INFORMATION, "Chức năng Gọi món", "Mở giao diện gọi món/thêm món cho " + ban.getMaBan() + "...");
////        // TODO: Triển khai logic nghiệp vụ gọi món.
////    }
////
////    // Xử lý logic cho Hủy Bàn
////    private void xuLyHuyBan(BanAn ban) {
////        String maHDGop = banAn_DAO.getMaHoaDonTuBan(ban.getMaBan(), ngayChon);
////        List<String> dsBanGhep = (maHDGop != null) ? banAn_DAO.getDanhSachBanCungHoaDon(maHDGop) : new ArrayList<>();
////
////        boolean laBanGhep = dsBanGhep.size() > 1;
////
////        if (laBanGhep) {
////            showDialogHuyBanGhep(ban, maHDGop, dsBanGhep);
////        } else {
////            thucHienHuyBanDon(ban);
////        }
////
////    }
////    private void thucHienHuyBanDon(BanAn ban) {
////        Alert alert = new Alert(AlertType.CONFIRMATION,
////                "Bạn có chắc chắn muốn hủy đặt bàn cho " + ban.getMaBan() + " không?",
////                ButtonType.YES, ButtonType.NO);
////        alert.setTitle("Xác nhận hủy");
////        alert.setHeaderText(null);
////
////        alert.showAndWait().ifPresent(response -> {
////            if (response == ButtonType.YES) {
////                if (phieuDatBan_DAO.huyPhieuDatBanByMaBanVaNgay(ban.getMaBan(), ngayChon)) {
////                    showAlert(AlertType.INFORMATION, "Thành công",
////                            "Đã hủy đặt bàn thành công cho bàn " + ban.getMaBan() + ".");
////                    loadDataToGrid();
////                } else {
////                    showAlert(AlertType.ERROR, "Lỗi",
////                            "Không tìm thấy phiếu đặt bàn hoặc hủy thất bại cho bàn " + ban.getMaBan() + ".");
////                }
////                loadDataToGrid();
////            }
////        });
////    }
////    private void showDialogHuyBanGhep(BanAn ban, String maHDGop, List<String> dsBanGhep) {
////        Dialog<ButtonType> dialog = new Dialog<>();
////        dialog.setTitle("Lựa chọn Hủy Bàn Ghép");
////        dialog.setHeaderText("Bàn " + ban.getMaBan() + " là một phần của nhóm bàn ghép (" + maHDGop + ")");
////
////        VBox content = new VBox(15);
////        content.setPadding(new Insets(15));
////
////        // Danh sách bàn ghép
////        Label lblGroup = new Label("Danh sách bàn ghép: " + String.join(", ", dsBanGhep));
////        lblGroup.setStyle("-fx-font-weight: bold;");
////
////        Label lblQuestion = new Label("Bạn muốn hủy riêng bàn này hay hủy toàn bộ nhóm?");
////
////        content.getChildren().addAll(lblGroup, lblQuestion);
////
////        // --- Các nút lựa chọn ---
////
////        // 1. Hủy riêng bàn này
////        ButtonType huyDonType = new ButtonType("Hủy riêng Bàn " + ban.getMaBan());
////        // 2. Hủy toàn bộ nhóm
////        ButtonType huyGhepType = new ButtonType("Hủy toàn bộ (" + dsBanGhep.size() + " bàn)");
////
////        dialog.getDialogPane().getButtonTypes().addAll(huyDonType, huyGhepType, ButtonType.CANCEL);
////        dialog.getDialogPane().setContent(content);
////
////        dialog.showAndWait().ifPresent(result -> {
////            if (result == huyDonType) {
////                thucHienHuyBanDon(ban);
////                loadDataToGrid();
////            } else if (result == huyGhepType) {
////                thucHienHuyBanGhep(maHDGop, dsBanGhep);
////                loadDataToGrid();
////            }
////        });
////    }
////
////    private void thucHienHuyBanGhep(String maHDGop, List<String> dsBanGhep) {
////        Alert alert = new Alert(AlertType.CONFIRMATION,
////                "Bạn có chắc chắn muốn hủy " + dsBanGhep.size() + " bàn thuộc nhóm " + maHDGop + " không?",
////                ButtonType.YES, ButtonType.NO);
////        alert.setTitle("Xác nhận hủy nhóm");
////        alert.setHeaderText(null);
////
////        alert.showAndWait().ifPresent(response -> {
////            if (response == ButtonType.YES) {
////                if (phieuDatBan_DAO.huyTatCaPhieuByMaHoaDon(maHDGop)) {
////                    showAlert(AlertType.INFORMATION, "Thành công",
////                            "Đã hủy đặt bàn thành công cho toàn bộ nhóm bàn ghép (" + String.join(", ", dsBanGhep) + ").");
////                    loadDataToGrid();
////                } else {
////                    showAlert(AlertType.ERROR, "Lỗi",
////                            "Không tìm thấy phiếu đặt bàn hoặc hủy nhóm thất bại (Mã HD: " + maHDGop + ").");
////                }
////                loadDataToGrid();
////            }
////        });
////    }
////
////    // Xử lý logic cho Check-in
////    private void xuLyCheckIn(BanAn ban, Dialog<Void> dialog) {
////        String maHDGop = banAn_DAO.getMaHoaDonTuBan(ban.getMaBan(), ngayChon);
////
////        List<PhieuDatBan> dsPhieuDatBan = CheckIn_DAO.getPhieuDatBanTheoHoaDonVaNgay(maHDGop, ngayChon);
////
////        KhachHang kh = KhachHang_DAO.getKhachHangById(dsPhieuDatBan.get(0).getKhachHang().getMaKhachHang());
////        //System.out.println(dsPhieuDatBan);
////
////        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
////        alert.setTitle("Xác nhận");
////        alert.setHeaderText(null);
////
////        String dsBanDat = "";
////        for(PhieuDatBan phieu : dsPhieuDatBan) {
////            dsBanDat += phieu.getBan().getMaBan() + ", ";
////        }
////
////        alert.setContentText("Tiếp tục Check-In\nTên Khách Hàng: " + kh.getTenKhachHang() + "\nSố điện thoại: " + kh.getSoDienThoai() + "\nBàn đặt: " + dsBanDat);
////
////
////        Optional<ButtonType> result = alert.showAndWait();
////        if (result.isPresent() && result.get() == ButtonType.OK) {
////            for(PhieuDatBan pdb : dsPhieuDatBan) {
////                if(!controlCheckIn.capNhatTrangThai(pdb.getMaPhieu(), "Đang dùng") || !controlCheckIn.capNhatTrangThaiBan(pdb.getBan().getMaBan(), TrangThai.DANG_SU_DUNG))
////                {	showAlert(AlertType.INFORMATION, "Lỗi", "Lỗi khi checkIn bàn và phiếu đặt bàn");
////                    return;
////                }
////            }
////
////            showAlert(AlertType.INFORMATION, "Thành công", "Check-In thành công!");
////
////            dialog.close();
////
////            loadDataToGrid();
////        }
////
////
////    }
////
////
////    // Xử lý logic cho Thanh Toán
////    private void xuLyThanhToan(String maHD) {
////        this.setCenter(new Gui_ThanhToan(new NhanVien(), maHD));
////
////    }
////
////}
//package gui;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import ctrl.CheckIn_Ctrl;
//import dao.BanAn_DAO;
//import dao.CheckIn_DAO;
//import dao.KhachHang_DAO;
//import dao.PhieuDatBan_DAO;
//import entity.BanAn;
//import entity.KhachHang;
//import entity.LoaiBan;
//import entity.NhanVien;
//import entity.PhieuDatBan;
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
//import javafx.scene.shape.Circle;
//import javafx.scene.text.Font;
//import javafx.scene.text.FontWeight;
//import javafx.stage.StageStyle;
//import javafx.stage.Window;
//
//public class Gui_DanhSachBan extends BorderPane {
//    private GridPane luoiBan;
//    private BanAn_DAO banAn_DAO;
//    private Gui_TrangChu trangChu;
//    private List<BanAn> danhSachBanDaChon = new ArrayList<>();
//    private TextField timKiem;
//    private ComboBox<String> cmbTatCa;
//    private ToggleButton tang1;
//    private ToggleButton tang2;
//    private ViTri viTriHienTai = ViTri.LAU_1;
//    private DatePicker datePicker;
//    private LocalDate ngayChon = LocalDate.now();
//    private PhieuDatBan_DAO phieuDatBan_DAO;
//    private CheckIn_Ctrl controlCheckIn = new CheckIn_Ctrl();
//
//    public Gui_DanhSachBan(Gui_TrangChu trangChu) {
//        this.trangChu = trangChu;
//        banAn_DAO = new BanAn_DAO();
//        phieuDatBan_DAO = new PhieuDatBan_DAO();
//
//        // --- SETUP ROOT STYLE ---
//        this.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa 0%, #e9ecef 100%);");
//        this.setPadding(new Insets(20));
//
//        // --- HEADER & FILTERS ---
//        VBox topContainer = new VBox(20);
//        topContainer.getChildren().addAll(createModernHeader(), createModernFilterBar());
//        topContainer.setPadding(new Insets(0, 0, 20, 0));
//
//        // --- CONTENT AREA (Grid Bàn) ---
//        ScrollPane scrollPane = createScrollableGrid();
//
//        // --- FOOTER ---
//        HBox footer = createFooter();
//
//        // --- LAYOUT ---
//        this.setTop(topContainer);
//        this.setCenter(scrollPane);
//        this.setBottom(footer);
//
//        loadDataToGrid();
//    }
//
//    // =================================================================================================
//    // SECTION 1: HEADER & FILTER BAR
//    // =================================================================================================
//
//    private VBox createModernHeader() {
//        VBox header = new VBox(5);
//        header.setPadding(new Insets(20));
//        header.setAlignment(Pos.CENTER_LEFT);
//        header.setStyle(
//                "-fx-background-color: #082744;" +
//                        "-fx-background-radius: 15;" +
//                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 5);"
//        );
//
//        Label title = new Label("🍽️ SƠ ĐỒ BÀN ĂN");
//        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
//        title.setStyle("-fx-text-fill: white;");
//
//        Label subtitle = new Label("Quản lý trạng thái bàn, đặt bàn và check-in khách hàng.");
//        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
//        subtitle.setStyle("-fx-text-fill: rgba(255,255,255,0.8);");
//
//        header.getChildren().addAll(title, subtitle);
//        return header;
//    }
//
//    private VBox createModernFilterBar() {
//        VBox container = new VBox(15);
//        container.setPadding(new Insets(15));
//        container.setStyle(
//                "-fx-background-color: white;" +
//                        "-fx-background-radius: 12;" +
//                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);"
//        );
//
//        HBox row1 = new HBox(20);
//        row1.setAlignment(Pos.CENTER_LEFT);
//
//        Label lblKhuVuc = new Label("📍 Khu vực:");
//        lblKhuVuc.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
//
//        tang1 = new ToggleButton("Tầng 1");
//        tang2 = new ToggleButton("Tầng 2");
//
//        String toggleStyle = "-fx-background-color: #f1f2f6; -fx-text-fill: #2d3436; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand; -fx-padding: 8 15;";
//        String toggleSelectedStyle = "-fx-background-color: #082744; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand; -fx-padding: 8 15;";
//
//        tang1.setStyle(toggleSelectedStyle);
//        tang2.setStyle(toggleStyle);
//
//        ToggleGroup group = new ToggleGroup();
//        tang1.setToggleGroup(group);
//        tang2.setToggleGroup(group);
//
//        tang1.setOnAction(e -> {
//            viTriHienTai = ViTri.LAU_1;
//            tang1.setStyle(toggleSelectedStyle);
//            tang2.setStyle(toggleStyle);
//            loadDataToGrid();
//        });
//        tang2.setOnAction(e -> {
//            viTriHienTai = ViTri.LAU_2;
//            tang2.setStyle(toggleSelectedStyle);
//            tang1.setStyle(toggleStyle);
//            loadDataToGrid();
//        });
//
//        HBox boxKhuVuc = new HBox(10, lblKhuVuc, tang1, tang2);
//        boxKhuVuc.setAlignment(Pos.CENTER_LEFT);
//
//        Region spacer1 = new Region();
//        HBox.setHgrow(spacer1, Priority.ALWAYS);
//
//        Label lblNgay = new Label("📅 Ngày xem:");
//        lblNgay.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
//        datePicker = new DatePicker(ngayChon);
//        datePicker.setPrefWidth(150);
//        datePicker.setStyle("-fx-font-size: 14px;");
//        datePicker.setOnAction(e -> {
//            LocalDate ngayMoi = datePicker.getValue();
//            if (ngayMoi.isBefore(LocalDate.now())) {
//                showAlert(AlertType.ERROR, "Ngày không hợp lệ", "Không thể chọn ngày trong quá khứ.");
//                datePicker.setValue(ngayChon);
//            } else {
//                ngayChon = ngayMoi;
//                loadDataToGrid();
//            }
//        });
//
//        HBox boxNgay = new HBox(10, lblNgay, datePicker);
//        boxNgay.setAlignment(Pos.CENTER_LEFT);
//        row1.getChildren().addAll(boxKhuVuc, spacer1, boxNgay);
//
//        HBox row2 = new HBox(15);
//        row2.setAlignment(Pos.CENTER_LEFT);
//        Label lblTimKiem = new Label("🔍 Tìm bàn:");
//        lblTimKiem.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
//        timKiem = new TextField();
//        timKiem.setPromptText("Nhập số bàn...");
//        timKiem.setPrefWidth(200);
//        timKiem.setStyle("-fx-background-radius: 5; -fx-padding: 8;");
//        timKiem.textProperty().addListener((obs, oldVal, newVal) -> loadDataToGrid());
//
//        Label lblTrangThai = new Label("Trạng thái:");
//        lblTrangThai.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
//        lblTrangThai.setPadding(new Insets(0, 0, 0, 20));
//        cmbTatCa = new ComboBox<>();
//        cmbTatCa.getItems().addAll("Tất cả", "Bàn trống", "Đang sử dụng", "Đã đặt bàn", "Bàn VIP");
//        cmbTatCa.setValue("Tất cả");
//        cmbTatCa.setPrefWidth(150);
//        cmbTatCa.setStyle("-fx-background-radius: 5; -fx-padding: 5;");
//        cmbTatCa.setOnAction(e -> loadDataToGrid());
//
//        row2.getChildren().addAll(lblTimKiem, timKiem, lblTrangThai, cmbTatCa);
//        container.getChildren().addAll(row1, new Separator(), row2);
//        return container;
//    }
//
//    // =================================================================================================
//    // SECTION 2: CONTENT AREA (SCROLLABLE GRID)
//    // =================================================================================================
//
//    private ScrollPane createScrollableGrid() {
//        luoiBan = new GridPane();
//        luoiBan.setHgap(25);
//        luoiBan.setVgap(25);
//        luoiBan.setPadding(new Insets(10));
//        luoiBan.setStyle("-fx-background-color: transparent;");
//
//        ScrollPane scrollPane = new ScrollPane(luoiBan);
//        scrollPane.setFitToWidth(true);
//        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
//        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
//
//        return scrollPane;
//    }
//
//    // =================================================================================================
//    // SECTION 3: FOOTER
//    // =================================================================================================
//
//    private HBox createFooter() {
//        HBox footer = new HBox(20);
//        footer.setPadding(new Insets(15, 0, 0, 0));
//        footer.setAlignment(Pos.CENTER_LEFT);
//
//        HBox legend = new HBox(15);
//        legend.setAlignment(Pos.CENTER_LEFT);
//        legend.getChildren().addAll(
//                createLegendItem("#718096", "Bàn trống"),      // Xám
//                createLegendItem("#38A169", "Đang sử dụng"),   // Xanh lá
//                createLegendItem("#E53E3E", "Đã đặt trước"),   // Đỏ
//                createLegendItemImg("/img/vipicon.png", "Bàn VIP")
//        );
//
//        Region spacer = new Region();
//        HBox.setHgrow(spacer, Priority.ALWAYS);
//
//        Button btnDatBan = new Button("➕ Đặt Bàn Ngay");
//        btnDatBan.setStyle("-fx-background-color: #082744; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 8; -fx-cursor: hand;");
//        btnDatBan.setOnMouseEntered(e -> btnDatBan.setStyle("-fx-background-color: #0a3d6a; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 8; -fx-cursor: hand;"));
//        btnDatBan.setOnMouseExited(e -> btnDatBan.setStyle("-fx-background-color: #082744; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 8; -fx-cursor: hand;"));
//
//        btnDatBan.setOnAction(e -> xuLyDatBan());
//
//        footer.getChildren().addAll(legend, spacer, btnDatBan);
//        return footer;
//    }
//
//    private HBox createLegendItem(String colorHex, String text) {
//        HBox item = new HBox(5);
//        item.setAlignment(Pos.CENTER_LEFT);
//        Circle dot = new Circle(6, Color.web(colorHex));
//        Label lbl = new Label(text);
//        lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
//        lbl.setTextFill(Color.web("#636e72"));
//        item.getChildren().addAll(dot, lbl);
//        return item;
//    }
//
//    private HBox createLegendItemImg(String imgPath, String text) {
//        HBox item = new HBox(5);
//        item.setAlignment(Pos.CENTER_LEFT);
//        try {
//            ImageView img = new ImageView(new Image(getClass().getResource(imgPath).toExternalForm()));
//            img.setFitWidth(16);
//            img.setFitHeight(16);
//            Label lbl = new Label(text);
//            lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
//            lbl.setTextFill(Color.web("#636e72"));
//            item.getChildren().addAll(img, lbl);
//        } catch (Exception e) {
//        }
//        return item;
//    }
//
//    // =================================================================================================
//    // SECTION 4: LOGIC LOAD DATA & CARD CREATION
//    // =================================================================================================
//
//    private void loadDataToGrid() {
//        luoiBan.getChildren().clear();
//        danhSachBanDaChon.clear();
//
//        List<BanAn> dsBanAnFull = banAn_DAO.getTrangThaiBanTheoNgayVaViTri(viTriHienTai, ngayChon);
//        String tuKhoa = timKiem.getText().trim().toLowerCase();
//        String loaiLoc = cmbTatCa.getValue();
//
//        List<BanAn> dsDaLoc = new ArrayList<>();
//        for (BanAn ban : dsBanAnFull) {
//            boolean khopTuKhoa = tuKhoa.isEmpty() || ban.getMaBan().toLowerCase().contains(tuKhoa);
//            boolean khopLoaiLoc = true;
//            switch (loaiLoc) {
//                case "Bàn trống":
//                    khopLoaiLoc = (ban.getTrangThai() == TrangThai.TRONG);
//                    break;
//                case "Đang sử dụng":
//                    khopLoaiLoc = (ban.getTrangThai() == TrangThai.DANG_SU_DUNG);
//                    break;
//                case "Đã đặt bàn":
//                    khopLoaiLoc = (ban.getTrangThai() == TrangThai.DA_DAT);
//                    break;
//                case "Bàn VIP":
//                    khopLoaiLoc = (ban.getLoai() == LoaiBan.VIP);
//                    break;
//            }
//            if (khopTuKhoa && khopLoaiLoc) {
//                dsDaLoc.add(ban);
//            }
//        }
//
//        for (int i = 0; i < dsDaLoc.size(); i++) {
//            BanAn ban = dsDaLoc.get(i);
//            VBox theBan = taoTheBan(ban); // SỬ DỤNG VBOX CARD MỚI
//
//            String defaultStyle = "-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);";
//            String selectedStyle = "-fx-background-color: #ebf8ff; -fx-background-radius: 10; -fx-border-color: #3182ce; -fx-border-width: 2; -fx-border-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 3);";
//
//            theBan.setStyle(defaultStyle);
//
//            theBan.setOnMouseClicked(e -> {
//                if (danhSachBanDaChon.contains(ban)) {
//                    danhSachBanDaChon.remove(ban);
//                    theBan.setStyle(defaultStyle);
//                } else {
//                    if (ban.getTrangThai() == TrangThai.TRONG) {
//                        danhSachBanDaChon.add(ban);
//                        theBan.setStyle(selectedStyle);
//                    } else {
//                        showAlert(AlertType.WARNING, "Không thể chọn", "Chỉ có thể chọn bàn đang 'Trống'.");
//                    }
//                }
//            });
//
//            if (ban.getTrangThai() == TrangThai.TRONG) {
//                theBan.setOnMouseEntered(e -> {
//                    if (!danhSachBanDaChon.contains(ban)) {
//                        theBan.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 4);");
//                        theBan.setScaleX(1.03);
//                        theBan.setScaleY(1.03);
//                    }
//                    theBan.setCursor(Cursor.HAND);
//                });
//                theBan.setOnMouseExited(e -> {
//                    if (!danhSachBanDaChon.contains(ban)) theBan.setStyle(defaultStyle);
//                    else theBan.setStyle(selectedStyle);
//                    theBan.setScaleX(1.0);
//                    theBan.setScaleY(1.0);
//                    theBan.setCursor(Cursor.DEFAULT);
//                });
//            } else {
//                theBan.setOnMouseEntered(e -> {
//                    theBan.setScaleX(1.02);
//                    theBan.setScaleY(1.02);
//                });
//                theBan.setOnMouseExited(e -> {
//                    theBan.setScaleX(1.0);
//                    theBan.setScaleY(1.0);
//                });
//            }
//
//            int col = i % 5;
//            int row = i / 5;
//            luoiBan.add(theBan, col, row);
//        }
//    }
//
//    // --- PHẦN QUAN TRỌNG NHẤT: THIẾT KẾ THẺ MỚI ---
//    private VBox taoTheBan(BanAn ban) {
//        VBox card = new VBox();
//        card.setPrefSize(200, 130);
//
//        // 1. Header màu
//        HBox header = new HBox(10);
//        header.setAlignment(Pos.CENTER_LEFT);
//        header.setPadding(new Insets(8, 15, 8, 15));
//
//        String headerColor;
//        switch (ban.getTrangThai()) {
//            case DANG_SU_DUNG:
//                headerColor = "#38A169";
//                break; // Xanh lá
//            case DA_DAT:
//                headerColor = "#E53E3E";
//                break;       // Đỏ
//            default:
//                headerColor = "#718096";
//                break;           // Xám
//        }
//        header.setStyle("-fx-background-color: " + headerColor + "; -fx-background-radius: 10 10 0 0;");
//
//        Label lblMaBan = new Label(ban.getMaBan());
//        lblMaBan.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
//        lblMaBan.setTextFill(Color.WHITE);
//
//        Region spacer = new Region();
//        HBox.setHgrow(spacer, Priority.ALWAYS);
//
//        header.getChildren().addAll(lblMaBan, spacer);
//
//        if (ban.getLoai() == LoaiBan.VIP) {
//            try {
//                ImageView iconVip = new ImageView(new Image(getClass().getResource("/img/vipicon.png").toExternalForm()));
//                iconVip.setFitWidth(20);
//                iconVip.setFitHeight(20);
//                header.getChildren().add(iconVip);
//            } catch (Exception e) {
//            }
//        }
//
//        // 2. Body
//        VBox body = new VBox(10);
//        body.setAlignment(Pos.CENTER);
//        body.setPadding(new Insets(10));
//        body.setPrefHeight(90);
//
//        String statusText = switch (ban.getTrangThai()) {
//            case DANG_SU_DUNG -> "Đang phục vụ";
//            case DA_DAT -> "Đã đặt trước";
//            default -> "Bàn trống";
//        };
//        Label lblStatus = new Label(statusText);
//        lblStatus.setFont(Font.font("Segoe UI", 14));
//        lblStatus.setTextFill(Color.web("#4A5568"));
//
//        HBox iconBox = new HBox();
//        iconBox.setAlignment(Pos.CENTER);
//        if (ban.getTrangThai() == TrangThai.DANG_SU_DUNG || ban.getTrangThai() == TrangThai.DA_DAT) {
//            String maHDGop = banAn_DAO.getMaHoaDonTuBan(ban.getMaBan(), ngayChon);
//            boolean isMerged = (maHDGop != null && banAn_DAO.getDanhSachBanCungHoaDon(maHDGop).size() > 1);
//            if (isMerged) {
//                try {
//                    ImageView iconLink = new ImageView(new Image(getClass().getResource("/img/link.png").toExternalForm()));
//                    iconLink.setFitWidth(18);
//                    iconLink.setFitHeight(18);
//                    Label lblMerged = new Label(" Bàn ghép");
//                    lblMerged.setTextFill(Color.web("#3182ce"));
//                    lblMerged.setFont(Font.font("Segoe UI", 11));
//                    iconBox.getChildren().addAll(iconLink, lblMerged);
//                } catch (Exception e) {
//                }
//            }
//        }
//
//        Button btnDetail = new Button("Xem chi tiết");
//        btnDetail.setStyle("-fx-background-color: transparent; -fx-text-fill: #718096; -fx-underline: true; -fx-cursor: hand; -fx-font-size: 11px;");
//        btnDetail.setOnAction(e -> {
//            showTableInfoDialog(ban);
//            e.consume();
//        });
//
//        Region vSpacer = new Region();
//        VBox.setVgrow(vSpacer, Priority.ALWAYS);
//
//        body.getChildren().addAll(lblStatus, iconBox, vSpacer, btnDetail);
//        card.getChildren().addAll(header, body);
//        return card;
//    }
//
//    // =================================================================================================
//    // SECTION 5: DIALOGS & ACTIONS
//    // =================================================================================================
//
//    private void showTableInfoDialog(BanAn ban) {
//        Dialog<Void> dialog = new Dialog<>();
//        dialog.initOwner(this.getScene().getWindow());
//        dialog.initStyle(StageStyle.TRANSPARENT);
//        dialog.getDialogPane().getScene().setFill(Color.TRANSPARENT);
//
//        Label title = new Label("ℹ️ THÔNG TIN BÀN " + ban.getMaBan());
//        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
//        title.setTextFill(Color.web("#2d3436"));
//        Button closeButton = new Button("✕");
//        closeButton.setStyle("-fx-background-color: transparent; -fx-font-size: 16px; -fx-cursor: hand; -fx-text-fill: #636e72;");
//        closeButton.setOnAction(e -> dialog.close());
//
//        HBox headerPane = new HBox(title, new Region(), closeButton);
//        HBox.setHgrow(headerPane.getChildren().get(1), Priority.ALWAYS);
//        headerPane.setAlignment(Pos.CENTER_LEFT);
//        headerPane.setPadding(new Insets(15));
//        headerPane.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8 8 0 0;");
//
//        VBox contentBox = new VBox(15);
//        contentBox.setPadding(new Insets(20));
//        contentBox.setStyle("-fx-background-color: white;");
//
//        String statusText, subText, bgColor, textColor;
//        switch (ban.getTrangThai()) {
//            case DANG_SU_DUNG:
//                statusText = "Đang phục vụ";
//                subText = "Có khách đang sử dụng";
//                bgColor = "#F0FFF4";
//                textColor = "#22543D";
//                break;
//            case DA_DAT:
//                statusText = "Đã đặt trước";
//                subText = "Khách sắp đến";
//                bgColor = "#FFF5F5";
//                textColor = "#9B2C2C";
//                break;
//            default:
//                statusText = "Bàn trống";
//                subText = "Sẵn sàng đón khách";
//                bgColor = "#F0F4FF";
//                textColor = "#004085";
//                break;
//        }
//
//        VBox statusBox = new VBox(5, createLabel(statusText, textColor, 16, true), createLabel(subText, textColor, 14, false));
//        statusBox.setPadding(new Insets(15));
//        statusBox.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 8;");
//
//        contentBox.getChildren().addAll(statusBox, createDetailRow("Loại bàn:", ban.getLoai().name()), createDetailRow("Vị trí:", ban.getViTri().name().replace("_", " ")));
//
//        boolean isBookedOrInUse = (ban.getTrangThai() == TrangThai.DA_DAT || ban.getTrangThai() == TrangThai.DANG_SU_DUNG);
//        if (isBookedOrInUse) {
//            PhieuDatBan pdbInfo = phieuDatBan_DAO.getPhieuDatBanByMaBanVaNgay(ban.getMaBan(), datePicker.getValue());
//            if (pdbInfo != null) {
//                contentBox.getChildren().addAll(new Separator(),
//                        createLabel("Thông tin khách hàng:", "#2d3436", 14, true),
//                        createDetailRow("Tên khách:", pdbInfo.getKhachHang().getTenKhachHang()),
//                        createDetailRow("SĐT:", pdbInfo.getKhachHang().getSoDienThoai()),
//                        createDetailRow("Ghi chú:", (pdbInfo.getGhiChu() != null && !pdbInfo.getGhiChu().isEmpty()) ? pdbInfo.getGhiChu() : "---")
//                );
//            }
//            String maHDGop = banAn_DAO.getMaHoaDonTuBan(ban.getMaBan(), ngayChon);
//            if (maHDGop != null) {
//                List<String> dsBanGhep = banAn_DAO.getDanhSachBanCungHoaDon(maHDGop);
//                if (dsBanGhep.size() > 1) {
//                    contentBox.getChildren().addAll(new Separator(), createLabel("Bàn ghép cùng:", "#007BFF", 14, true), createLabel(String.join(", ", dsBanGhep), "#2d3436", 14, false));
//                }
//            }
//        }
//
//        HBox actionBox = new HBox(10);
//        actionBox.setAlignment(Pos.CENTER_RIGHT);
//        actionBox.setPadding(new Insets(15));
//        actionBox.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 0 0 8 8;");
//
//        switch (ban.getTrangThai()) {
//            case DANG_SU_DUNG:
//                actionBox.getChildren().addAll(createActionButton("Đổi bàn", "#FFC107", e -> xuLyDoiBan(ban)), createActionButton("Thanh toán", "#38A169", e -> xuLyThanhToan(banAn_DAO.getMaHoaDonTuBan(ban.getMaBan(), ngayChon))), createActionButton("Gọi món", "#6B49C7", e -> xuLyGoiMon(ban)));
//                break;
//            case DA_DAT:
//                actionBox.getChildren().addAll(createActionButton("Đổi bàn", "#FFC107", e -> xuLyDoiBan(ban)), createActionButton("Hủy bàn", "#DC3545", e -> xuLyHuyBan(ban)), createActionButton("Check-in", "#007BFF", e -> xuLyCheckIn(ban, dialog)));
//                break;
//            case TRONG:
//                break;
//        }
//
//        VBox mainLayout = new VBox(headerPane, contentBox);
//        if (!actionBox.getChildren().isEmpty()) mainLayout.getChildren().add(actionBox);
//
//        dialog.getDialogPane().setContent(mainLayout);
//        dialog.getDialogPane().setStyle("-fx-background-color: transparent; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);");
//        mainLayout.setStyle("-fx-background-color: white; -fx-background-radius: 8;");
//        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
//        dialog.getDialogPane().lookupButton(ButtonType.CLOSE).setVisible(false);
//        dialog.showAndWait();
//    }
//
//    private Label createLabel(String text, String color, int size, boolean bold) {
//        Label l = new Label(text);
//        l.setFont(Font.font("Segoe UI", bold ? FontWeight.BOLD : FontWeight.NORMAL, size));
//        l.setTextFill(Color.web(color));
//        return l;
//    }
//
//    private HBox createDetailRow(String label, String value) {
//        Label l = new Label(label);
//        l.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
//        l.setTextFill(Color.web("#636e72"));
//        l.setMinWidth(80);
//        Label v = new Label(value);
//        v.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
//        v.setTextFill(Color.web("#2d3436"));
//        return new HBox(10, l, v);
//    }
//
//    private Button createActionButton(String text, String color, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
//        Button btn = new Button(text);
//        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand; -fx-padding: 8 15;");
//        btn.setOnAction(handler);
//        return btn;
//    }
//
//    private void xuLyDatBan() {
//        if (danhSachBanDaChon.isEmpty()) {
//            showAlert(AlertType.ERROR, "Chưa chọn bàn", "Vui lòng click chọn ít nhất một bàn để đặt.");
//            return;
//        }
//        for (BanAn ban : danhSachBanDaChon) {
//            if (ban.getTrangThai() != TrangThai.TRONG) {
//                showAlert(AlertType.WARNING, "Bàn không hợp lệ", "Bàn " + ban.getMaBan() + " không trống.");
//                return;
//            }
//        }
//        try {
//            LocalDate ngayDat = datePicker.getValue();
//            Gui_DatBan guiDatBan = new Gui_DatBan(trangChu, danhSachBanDaChon, ngayDat);
//            trangChu.setMainContent(guiDatBan);
//        } catch (Exception e) {
//            e.printStackTrace();
//            new Gui_DatBan(trangChu, danhSachBanDaChon, datePicker.getValue());
//        }
//    }
//
//    private void xuLyDoiBan(BanAn banCu) {
//        String maHDGop = banAn_DAO.getMaHoaDonTuBan(banCu.getMaBan(), ngayChon);
//        List<String> dsBanGhep = (maHDGop != null) ? banAn_DAO.getDanhSachBanCungHoaDon(maHDGop) : new ArrayList<>();
//        Window owner = this.getScene().getWindow();
//        Gui_DoiBan dialog = new Gui_DoiBan(owner, banCu, ngayChon, banAn_DAO, phieuDatBan_DAO, dsBanGhep);
//        dialog.showAndWait().ifPresent(result -> {
//            if (result == ButtonType.OK) loadDataToGrid();
//        });
//    }
//
//    private void xuLyGoiMon(BanAn ban) {
//        showAlert(AlertType.INFORMATION, "Chức năng Gọi món", "Mở giao diện gọi món cho " + ban.getMaBan());
//    }
//
//    private void xuLyHuyBan(BanAn ban) {
//        String maHDGop = banAn_DAO.getMaHoaDonTuBan(ban.getMaBan(), ngayChon);
//        List<String> dsBanGhep = (maHDGop != null) ? banAn_DAO.getDanhSachBanCungHoaDon(maHDGop) : new ArrayList<>();
//        if (dsBanGhep.size() > 1) showDialogHuyBanGhep(ban, maHDGop, dsBanGhep);
//        else thucHienHuyBanDon(ban);
//    }
//
//    private void thucHienHuyBanDon(BanAn ban) {
//        Alert alert = new Alert(AlertType.CONFIRMATION, "Hủy đặt bàn " + ban.getMaBan() + "?", ButtonType.YES, ButtonType.NO);
//        alert.showAndWait().ifPresent(response -> {
//            if (response == ButtonType.YES) {
//                if (phieuDatBan_DAO.huyPhieuDatBanByMaBanVaNgay(ban.getMaBan(), ngayChon)) {
//                    showAlert(AlertType.INFORMATION, "Thành công", "Đã hủy.");
//                    loadDataToGrid();
//                } else showAlert(AlertType.ERROR, "Lỗi", "Hủy thất bại.");
//            }
//        });
//    }
//
//    private void showDialogHuyBanGhep(BanAn ban, String maHDGop, List<String> dsBanGhep) {
//        Dialog<ButtonType> dialog = new Dialog<>();
//        dialog.setTitle("Hủy Bàn Ghép");
//        dialog.setHeaderText("Hủy bàn " + ban.getMaBan() + " (Nhóm " + maHDGop + ")");
//        VBox content = new VBox(15);
//        content.setPadding(new Insets(15));
//        content.getChildren().addAll(new Label("Bàn ghép: " + String.join(", ", dsBanGhep)), new Label("Hủy riêng hay hủy cả nhóm?"));
//        ButtonType huyDonType = new ButtonType("Hủy riêng");
//        ButtonType huyGhepType = new ButtonType("Hủy cả nhóm");
//        dialog.getDialogPane().getButtonTypes().addAll(huyDonType, huyGhepType, ButtonType.CANCEL);
//        dialog.getDialogPane().setContent(content);
//        dialog.showAndWait().ifPresent(result -> {
//            if (result == huyDonType) {
//                thucHienHuyBanDon(ban);
//                loadDataToGrid();
//            } else if (result == huyGhepType) {
//                thucHienHuyBanGhep(maHDGop, dsBanGhep);
//                loadDataToGrid();
//            }
//        });
//    }
//
//    private void thucHienHuyBanGhep(String maHDGop, List<String> dsBanGhep) {
//        Alert alert = new Alert(AlertType.CONFIRMATION, "Hủy nhóm " + dsBanGhep.size() + " bàn?", ButtonType.YES, ButtonType.NO);
//        alert.showAndWait().ifPresent(response -> {
//            if (response == ButtonType.YES) {
//                if (phieuDatBan_DAO.huyTatCaPhieuByMaHoaDon(maHDGop)) {
//                    showAlert(AlertType.INFORMATION, "Thành công", "Đã hủy nhóm.");
//                    loadDataToGrid();
//                } else showAlert(AlertType.ERROR, "Lỗi", "Hủy nhóm thất bại.");
//            }
//        });
//    }
//
//    private void xuLyCheckIn(BanAn ban, Dialog<Void> dialog) {
//        String maHDGop = banAn_DAO.getMaHoaDonTuBan(ban.getMaBan(), ngayChon);
//        List<PhieuDatBan> dsPhieuDatBan = CheckIn_DAO.getPhieuDatBanTheoHoaDonVaNgay(maHDGop, ngayChon);
//        KhachHang kh = KhachHang_DAO.getKhachHangById(dsPhieuDatBan.get(0).getKhachHang().getMaKhachHang());
//        Alert alert = new Alert(AlertType.CONFIRMATION, "Check-in cho " + kh.getTenKhachHang() + "?", ButtonType.YES, ButtonType.NO);
//        alert.showAndWait().ifPresent(res -> {
//            if (res == ButtonType.YES) {
//                for (PhieuDatBan pdb : dsPhieuDatBan) {
//                    if (!controlCheckIn.capNhatTrangThai(pdb.getMaPhieu(), "Đang dùng") || !controlCheckIn.capNhatTrangThaiBan(pdb.getBan().getMaBan(), TrangThai.DANG_SU_DUNG)) {
//                        showAlert(AlertType.ERROR, "Lỗi", "Check-in thất bại.");
//                        return;
//                    }
//                }
//                showAlert(AlertType.INFORMATION, "Thành công", "Đã Check-in.");
//                dialog.close();
//                loadDataToGrid();
//            }
//        });
//    }
//
//    private void xuLyThanhToan(String maHD) {
//        this.setCenter(new Gui_ThanhToan(new NhanVien(), maHD));
//    }
//
//    private void showAlert(AlertType alertType, String title, String content) {
//        Alert alert = new Alert(alertType);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(content);
//        alert.showAndWait();
//    }
//}
package gui;

import java.io.File;
import java.security.AlgorithmConstraints;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import ctrl.CheckIn_Ctrl;
import dao.BanAn_DAO;
import dao.CheckIn_DAO;
import dao.ChiTietHoaDon_DAO;
import dao.KhachHang_DAO;
import dao.MonAn_DAO;
import dao.PhieuDatBan_DAO;
import entity.BanAn;
import entity.ChiTietHoaDon;
import entity.KhachHang;
import entity.LoaiBan;
import entity.MonAn;
import entity.NhanVien;
import entity.PhieuDatBan;
import entity.TrangThai;
import entity.ViTri;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import lib.ImageCacheManager;

public class Gui_DanhSachBan extends BorderPane {
    private GridPane luoiBan;
    private BanAn_DAO banAn_DAO;
    private Gui_TrangChu trangChu;
    private List<BanAn> danhSachBanDaChon = new ArrayList<>();
    private TextField timKiem;
    private ComboBox<String> cmbTatCa;
    private ToggleButton tang1;
    private ToggleButton tang2;
    private ViTri viTriHienTai = ViTri.LAU_1;
    private DatePicker datePicker;
    private LocalDate ngayChon = LocalDate.now();
    private PhieuDatBan_DAO phieuDatBan_DAO;
    private CheckIn_Ctrl controlCheckIn = new CheckIn_Ctrl();
	private MonAn_DAO monAn_DAO = new MonAn_DAO();
	private ChiTietHoaDon_DAO chiTietHoaDon_DAO = new ChiTietHoaDon_DAO();
	private Label lblTongTien;

    public Gui_DanhSachBan(Gui_TrangChu trangChu) {
        this.trangChu = trangChu;
        banAn_DAO = new BanAn_DAO();
        phieuDatBan_DAO = new PhieuDatBan_DAO();

        // --- SETUP ROOT STYLE ---
        this.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa 0%, #e9ecef 100%);");
        this.setPadding(new Insets(20));

        // --- HEADER & FILTERS ---
        VBox topContainer = new VBox(20);
        topContainer.getChildren().addAll(createModernHeader(), createModernFilterBar());
        topContainer.setPadding(new Insets(0, 0, 20, 0));

        // --- CONTENT AREA (Grid Bàn) ---
        ScrollPane scrollPane = createScrollableGrid();

        // --- FOOTER ---
        HBox footer = createFooter();

        // --- LAYOUT ---
        this.setTop(topContainer);
        this.setCenter(scrollPane);
        this.setBottom(footer);

        loadDataToGrid();
    }

    // ... (Giữ nguyên phần Header và FilterBar không đổi) ...
    // ... Copy lại các hàm createModernHeader, createModernFilterBar từ code trước ...
    // ... Để tiết kiệm không gian, tôi chỉ paste lại những hàm BỊ THAY ĐỔI bên dưới ...

    private VBox createModernHeader() {
        VBox header = new VBox(5);
        header.setPadding(new Insets(20));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #082744; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 5);");
        Label title = new Label("🍽️ SƠ ĐỒ BÀN ĂN");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: white;");
        Label subtitle = new Label("Quản lý trạng thái bàn, đặt bàn và check-in khách hàng.");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        subtitle.setStyle("-fx-text-fill: rgba(255,255,255,0.8);");
        header.getChildren().addAll(title, subtitle);
        return header;
    }

    private VBox createModernFilterBar() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(15));
        container.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        HBox row1 = new HBox(20);
        row1.setAlignment(Pos.CENTER_LEFT);
        Label lblKhuVuc = new Label("📍 Khu vực:");
        lblKhuVuc.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        tang1 = new ToggleButton("Tầng 1"); tang2 = new ToggleButton("Tầng 2");
        String toggleStyle = "-fx-background-color: #f1f2f6; -fx-text-fill: #2d3436; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand; -fx-padding: 8 15;";
        String toggleSelectedStyle = "-fx-background-color: #082744; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand; -fx-padding: 8 15;";
        tang1.setStyle(toggleSelectedStyle); tang2.setStyle(toggleStyle);
        ToggleGroup group = new ToggleGroup(); tang1.setToggleGroup(group); tang2.setToggleGroup(group);
        tang1.setOnAction(e -> { viTriHienTai = ViTri.LAU_1; tang1.setStyle(toggleSelectedStyle); tang2.setStyle(toggleStyle); loadDataToGrid(); });
        tang2.setOnAction(e -> { viTriHienTai = ViTri.LAU_2; tang2.setStyle(toggleSelectedStyle); tang1.setStyle(toggleStyle); loadDataToGrid(); });
        HBox boxKhuVuc = new HBox(10, lblKhuVuc, tang1, tang2);
        boxKhuVuc.setAlignment(Pos.CENTER_LEFT);
        Region spacer1 = new Region(); HBox.setHgrow(spacer1, Priority.ALWAYS);
        Label lblNgay = new Label("📅 Ngày xem:"); lblNgay.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        datePicker = new DatePicker(ngayChon); datePicker.setPrefWidth(150); datePicker.setStyle("-fx-font-size: 14px;");
        datePicker.setOnAction(e -> {
            LocalDate ngayMoi = datePicker.getValue();
            if (ngayMoi.isBefore(LocalDate.now())) { showAlert(AlertType.ERROR, "Ngày không hợp lệ", "Không thể chọn ngày trong quá khứ."); datePicker.setValue(ngayChon); }
            else { ngayChon = ngayMoi; loadDataToGrid(); }
        });
        HBox boxNgay = new HBox(10, lblNgay, datePicker); boxNgay.setAlignment(Pos.CENTER_LEFT);
        row1.getChildren().addAll(boxKhuVuc, spacer1, boxNgay);
        HBox row2 = new HBox(15); row2.setAlignment(Pos.CENTER_LEFT);
        Label lblTimKiem = new Label("🔍 Tìm bàn:"); lblTimKiem.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        timKiem = new TextField(); timKiem.setPromptText("Nhập số bàn..."); timKiem.setPrefWidth(200); timKiem.setStyle("-fx-background-radius: 5; -fx-padding: 8;");
        timKiem.textProperty().addListener((obs, oldVal, newVal) -> loadDataToGrid());
        Label lblTrangThai = new Label("Trạng thái:"); lblTrangThai.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14)); lblTrangThai.setPadding(new Insets(0, 0, 0, 20));
        cmbTatCa = new ComboBox<>(); cmbTatCa.getItems().addAll("Tất cả", "Bàn trống", "Đang sử dụng", "Đã đặt bàn", "Bàn VIP"); cmbTatCa.setValue("Tất cả"); cmbTatCa.setPrefWidth(150); cmbTatCa.setStyle("-fx-background-radius: 5; -fx-padding: 5;");
        cmbTatCa.setOnAction(e -> loadDataToGrid());
        row2.getChildren().addAll(lblTimKiem, timKiem, lblTrangThai, cmbTatCa);
        container.getChildren().addAll(row1, new Separator(), row2);
        return container;
    }

    // =================================================================================================
    // SECTION 2: CONTENT AREA (ĐÃ SỬA: CĂN GIỮA)
    // =================================================================================================

    private ScrollPane createScrollableGrid() {
        luoiBan = new GridPane();
        luoiBan.setHgap(25);
        luoiBan.setVgap(25);
        luoiBan.setPadding(new Insets(10));

        // --- SỬA 1: CĂN GIỮA DANH SÁCH BÀN ---
        luoiBan.setAlignment(Pos.TOP_CENTER);

        luoiBan.setStyle("-fx-background-color: transparent;");

        ScrollPane scrollPane = new ScrollPane(luoiBan);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        return scrollPane;
    }

    // =================================================================================================
    // SECTION 4: LOGIC LOAD DATA & CARD CREATION (ĐÃ SỬA: VIỀN TRONG SUỐT)
    // =================================================================================================

    private void loadDataToGrid() {
        luoiBan.getChildren().clear();
        danhSachBanDaChon.clear();

        List<BanAn> dsBanAnFull = banAn_DAO.getTrangThaiBanTheoNgayVaViTri(viTriHienTai, ngayChon);
        String tuKhoa = timKiem.getText().trim().toLowerCase();
        String loaiLoc = cmbTatCa.getValue();

        List<BanAn> dsDaLoc = new ArrayList<>();
        for (BanAn ban : dsBanAnFull) {
            boolean khopTuKhoa = tuKhoa.isEmpty() || ban.getMaBan().toLowerCase().contains(tuKhoa);
            boolean khopLoaiLoc = true;
            switch (loaiLoc) {
                case "Bàn trống": khopLoaiLoc = (ban.getTrangThai() == TrangThai.TRONG); break;
                case "Đang sử dụng": khopLoaiLoc = (ban.getTrangThai() == TrangThai.DANG_SU_DUNG); break;
                case "Đã đặt bàn": khopLoaiLoc = (ban.getTrangThai() == TrangThai.DA_DAT); break;
                case "Bàn VIP": khopLoaiLoc = (ban.getLoai() == LoaiBan.VIP); break;
            }
            if (khopTuKhoa && khopLoaiLoc) {
                dsDaLoc.add(ban);
            }
        }

        // Render Cards
        for (int i = 0; i < dsDaLoc.size(); i++) {
            BanAn ban = dsDaLoc.get(i);
            VBox theBan = taoTheBan(ban);

            // --- SỬA 2: DÙNG VIỀN TRONG SUỐT ĐỂ GIỮ CHỖ (TRÁNH BỊ NHẢY SIZE) ---

            // Style mặc định: Viền transparent 2px
            String defaultStyle =
                    "-fx-background-color: white; " +
                            "-fx-background-radius: 10; " +
                            "-fx-border-color: transparent; " + // Viền trong suốt
                            "-fx-border-width: 2; " +           // Độ dày 2px (giữ chỗ)
                            "-fx-border-radius: 10; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);";

            // Style khi chọn: Viền xanh 2px
            String selectedStyle =
                    "-fx-background-color: #ebf8ff; " +
                            "-fx-background-radius: 10; " +
                            "-fx-border-color: #3182ce; " +     // Viền xanh
                            "-fx-border-width: 2; " +           // Độ dày 2px (bằng với mặc định)
                            "-fx-border-radius: 10; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 3);";

            theBan.setStyle(defaultStyle);

            theBan.setOnMouseClicked(e -> {
                if (danhSachBanDaChon.contains(ban)) {
                    danhSachBanDaChon.remove(ban);
                    theBan.setStyle(defaultStyle);
                } else {
                    if (ban.getTrangThai() == TrangThai.TRONG) {
                        danhSachBanDaChon.add(ban);
                        theBan.setStyle(selectedStyle);
                    } else {
                        showAlert(AlertType.WARNING, "Không thể chọn", "Chỉ có thể chọn bàn đang 'Trống'.");
                    }
                }
            });

            if (ban.getTrangThai() == TrangThai.TRONG) {
                theBan.setOnMouseEntered(e -> {
                    if (!danhSachBanDaChon.contains(ban)) {
                        // Hover thì vẫn giữ viền transparent, chỉ thêm bóng đổ
                        theBan.setStyle(defaultStyle + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 4);");
                        theBan.setScaleX(1.03); theBan.setScaleY(1.03);
                    }
                    theBan.setCursor(Cursor.HAND);
                });
                theBan.setOnMouseExited(e -> {
                    if (!danhSachBanDaChon.contains(ban)) theBan.setStyle(defaultStyle);
                    else theBan.setStyle(selectedStyle);

                    theBan.setScaleX(1.0); theBan.setScaleY(1.0);
                    theBan.setCursor(Cursor.DEFAULT);
                });
            } else {
                // Các bàn đang bận/đặt chỉ có hiệu ứng nổi nhẹ
                theBan.setOnMouseEntered(e -> { theBan.setScaleX(1.02); theBan.setScaleY(1.02); });
                theBan.setOnMouseExited(e -> { theBan.setScaleX(1.0); theBan.setScaleY(1.0); });
            }

            int col = i % 5;
            int row = i / 5;
            luoiBan.add(theBan, col, row);
        }
    }

    private VBox taoTheBan(BanAn ban) {
        VBox card = new VBox();
        card.setPrefSize(200, 130);

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(8, 15, 8, 15));

        String headerColor;
        switch (ban.getTrangThai()) {
            case DANG_SU_DUNG: headerColor = "#38A169"; break; // Xanh lá
            case DA_DAT: headerColor = "#E53E3E"; break;       // Đỏ
            default: headerColor = "#718096"; break;           // Xám
        }
        header.setStyle("-fx-background-color: " + headerColor + "; -fx-background-radius: 8 8 0 0;"); // Radius 8 để khớp với viền border

        Label lblMaBan = new Label(ban.getMaBan());
        lblMaBan.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        lblMaBan.setTextFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(lblMaBan, spacer);

        if (ban.getLoai() == LoaiBan.VIP) {
            try {
                ImageView iconVip = new ImageView(new Image(getClass().getResource("/img/vipicon.png").toExternalForm()));
                iconVip.setFitWidth(20); iconVip.setFitHeight(20);
                header.getChildren().add(iconVip);
            } catch (Exception e) {}
        }

        VBox body = new VBox(10);
        body.setAlignment(Pos.CENTER);
        body.setPadding(new Insets(10));
        body.setPrefHeight(90);

        String statusText = switch(ban.getTrangThai()) {
            case DANG_SU_DUNG -> "Đang phục vụ";
            case DA_DAT -> "Đã đặt trước";
            default -> "Bàn trống";
        };
        Label lblStatus = new Label(statusText);
        lblStatus.setFont(Font.font("Segoe UI", 14));
        lblStatus.setTextFill(Color.web("#4A5568"));

        HBox iconBox = new HBox();
        iconBox.setAlignment(Pos.CENTER);
        if (ban.getTrangThai() == TrangThai.DANG_SU_DUNG || ban.getTrangThai() == TrangThai.DA_DAT) {
            String maHDGop = banAn_DAO.getMaHoaDonTuBan(ban.getMaBan(), ngayChon);
            boolean isMerged = (maHDGop != null && banAn_DAO.getDanhSachBanCungHoaDon(maHDGop).size() > 1);
            if (isMerged) {
                try {
                    ImageView iconLink = new ImageView(new Image(getClass().getResource("/img/link.png").toExternalForm()));
                    iconLink.setFitWidth(18); iconLink.setFitHeight(18);
                    Label lblMerged = new Label(" Bàn ghép");
                    lblMerged.setTextFill(Color.web("#3182ce"));
                    lblMerged.setFont(Font.font("Segoe UI", 11));
                    iconBox.getChildren().addAll(iconLink, lblMerged);
                } catch(Exception e){}
            }
        }

        Button btnDetail = new Button("Xem chi tiết");
        btnDetail.setStyle("-fx-background-color: transparent; -fx-text-fill: #718096; -fx-underline: true; -fx-cursor: hand; -fx-font-size: 11px;");
        btnDetail.setOnAction(e -> { showTableInfoDialog(ban); e.consume(); });

        Region vSpacer = new Region();
        VBox.setVgrow(vSpacer, Priority.ALWAYS);

        body.getChildren().addAll(lblStatus, iconBox, vSpacer, btnDetail);
        card.getChildren().addAll(header, body);
        return card;
    }

    // ... (Phần Footer, Dialog và Logic xử lý giữ nguyên như bản trước) ...
    // ... Để code ngắn gọn, tôi paste lại phần còn thiếu bên dưới ...

    private HBox createFooter() {
        HBox footer = new HBox(20);
        footer.setPadding(new Insets(15, 0, 0, 0));
        footer.setAlignment(Pos.CENTER_LEFT);
        HBox legend = new HBox(15);
        legend.setAlignment(Pos.CENTER_LEFT);
        legend.getChildren().addAll(
                createLegendItem("#718096", "Bàn trống"),
                createLegendItem("#38A169", "Đang sử dụng"),
                createLegendItem("#E53E3E", "Đã đặt trước"),
                createLegendItemImg("/img/vipicon.png", "Bàn VIP")
        );
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
        Button btnDatBan = new Button("➕ Đặt Bàn Ngay");
        btnDatBan.setStyle("-fx-background-color: #082744; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 8; -fx-cursor: hand;");
        btnDatBan.setOnMouseEntered(e -> btnDatBan.setStyle("-fx-background-color: #0a3d6a; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 8; -fx-cursor: hand;"));
        btnDatBan.setOnMouseExited(e -> btnDatBan.setStyle("-fx-background-color: #082744; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 8; -fx-cursor: hand;"));
        btnDatBan.setOnAction(e -> xuLyDatBan());
        footer.getChildren().addAll(legend, spacer, btnDatBan);
        return footer;
    }

    private HBox createLegendItem(String colorHex, String text) {
        HBox item = new HBox(5); item.setAlignment(Pos.CENTER_LEFT);
        Circle dot = new Circle(6, Color.web(colorHex));
        Label lbl = new Label(text); lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12)); lbl.setTextFill(Color.web("#636e72"));
        item.getChildren().addAll(dot, lbl); return item;
    }

    private HBox createLegendItemImg(String imgPath, String text) {
        HBox item = new HBox(5); item.setAlignment(Pos.CENTER_LEFT);
        try { ImageView img = new ImageView(new Image(getClass().getResource(imgPath).toExternalForm())); img.setFitWidth(16); img.setFitHeight(16);
            Label lbl = new Label(text); lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12)); lbl.setTextFill(Color.web("#636e72"));
            item.getChildren().addAll(img, lbl); } catch (Exception e) { } return item;
    }

    private void showTableInfoDialog(BanAn ban) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.initOwner(this.getScene().getWindow());
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.getDialogPane().getScene().setFill(Color.TRANSPARENT);
        Label title = new Label("ℹ️ THÔNG TIN BÀN " + ban.getMaBan()); title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18)); title.setTextFill(Color.web("#2d3436"));
        Button closeButton = new Button("✕"); closeButton.setStyle("-fx-background-color: transparent; -fx-font-size: 16px; -fx-cursor: hand; -fx-text-fill: #636e72;");
        closeButton.setOnAction(e -> dialog.close());
        HBox headerPane = new HBox(title, new Region(), closeButton); HBox.setHgrow(headerPane.getChildren().get(1), Priority.ALWAYS);
        headerPane.setAlignment(Pos.CENTER_LEFT); headerPane.setPadding(new Insets(15)); headerPane.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8 8 0 0;");
        VBox contentBox = new VBox(15); contentBox.setPadding(new Insets(20)); contentBox.setStyle("-fx-background-color: white;");
        String statusText, subText, bgColor, textColor;
        switch (ban.getTrangThai()) {
            case DANG_SU_DUNG: statusText = "Đang phục vụ"; subText = "Có khách đang sử dụng"; bgColor = "#F0FFF4"; textColor = "#22543D"; break;
            case DA_DAT: statusText = "Đã đặt trước"; subText = "Khách sắp đến"; bgColor = "#FFF5F5"; textColor = "#9B2C2C"; break;
            default: statusText = "Bàn trống"; subText = "Sẵn sàng đón khách"; bgColor = "#F0F4FF"; textColor = "#004085"; break;
        }
        VBox statusBox = new VBox(5, createLabel(statusText, textColor, 16, true), createLabel(subText, textColor, 14, false));
        statusBox.setPadding(new Insets(15)); statusBox.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 8;");
        contentBox.getChildren().addAll(statusBox, createDetailRow("Loại bàn:", ban.getLoai().name()), createDetailRow("Vị trí:", ban.getViTri().name().replace("_", " ")));
        boolean isBookedOrInUse = (ban.getTrangThai() == TrangThai.DA_DAT || ban.getTrangThai() == TrangThai.DANG_SU_DUNG);
        if (isBookedOrInUse) {
            PhieuDatBan pdbInfo = phieuDatBan_DAO.getPhieuDatBanByMaBanVaNgay(ban.getMaBan(), datePicker.getValue());
            if (pdbInfo != null) {
                contentBox.getChildren().addAll(new Separator(), createLabel("Thông tin khách hàng:", "#2d3436", 14, true), createDetailRow("Tên khách:", pdbInfo.getKhachHang().getTenKhachHang()), createDetailRow("SĐT:", pdbInfo.getKhachHang().getSoDienThoai()), createDetailRow("Ghi chú:", (pdbInfo.getGhiChu() != null && !pdbInfo.getGhiChu().isEmpty()) ? pdbInfo.getGhiChu() : "---"));
            }
            String maHDGop = banAn_DAO.getMaHoaDonTuBan(ban.getMaBan(), ngayChon);
            if (maHDGop != null) {
                List<String> dsBanGhep = banAn_DAO.getDanhSachBanCungHoaDon(maHDGop);
                if (dsBanGhep.size() > 1) { contentBox.getChildren().addAll(new Separator(), createLabel("Bàn ghép cùng:", "#007BFF", 14, true), createLabel(String.join(", ", dsBanGhep), "#2d3436", 14, false)); }
            }
        }
        HBox actionBox = new HBox(10); actionBox.setAlignment(Pos.CENTER_RIGHT); actionBox.setPadding(new Insets(15)); actionBox.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 0 0 8 8;");
        switch (ban.getTrangThai()) {
            case DANG_SU_DUNG: actionBox.getChildren().addAll(createActionButton("Đổi bàn", "#FFC107", e -> xuLyDoiBan(ban)), createActionButton("Thanh toán", "#38A169", e -> xuLyThanhToan(banAn_DAO.getMaHoaDonTuBan(ban.getMaBan(), ngayChon))), createActionButton("Gọi món", "#6B49C7", e -> {xuLyGoiMon(ban); dialog.close();})); break;
            case DA_DAT: actionBox.getChildren().addAll(createActionButton("Đổi bàn", "#FFC107", e -> xuLyDoiBan(ban)), createActionButton("Hủy bàn", "#DC3545", e -> xuLyHuyBan(ban)), createActionButton("Check-in", "#007BFF", e -> xuLyCheckIn(ban, dialog))); break;
            case TRONG: break;
        }
        VBox mainLayout = new VBox(headerPane, contentBox); if (!actionBox.getChildren().isEmpty()) mainLayout.getChildren().add(actionBox);
        dialog.getDialogPane().setContent(mainLayout);
        dialog.getDialogPane().setStyle("-fx-background-color: transparent; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        mainLayout.setStyle("-fx-background-color: white; -fx-background-radius: 8;");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE); dialog.getDialogPane().lookupButton(ButtonType.CLOSE).setVisible(false);
        dialog.showAndWait();
    }

    private Label createLabel(String text, String color, int size, boolean bold) { Label l = new Label(text); l.setFont(Font.font("Segoe UI", bold ? FontWeight.BOLD : FontWeight.NORMAL, size)); l.setTextFill(Color.web(color)); return l; }
    private HBox createDetailRow(String label, String value) { Label l = new Label(label); l.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14)); l.setTextFill(Color.web("#636e72")); l.setMinWidth(80); Label v = new Label(value); v.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14)); v.setTextFill(Color.web("#2d3436")); return new HBox(10, l, v); }
    private Button createActionButton(String text, String color, javafx.event.EventHandler<javafx.event.ActionEvent> handler) { Button btn = new Button(text); btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand; -fx-padding: 8 15;"); btn.setOnAction(handler); return btn; }

    private void xuLyDatBan() { if (danhSachBanDaChon.isEmpty()) { showAlert(AlertType.ERROR, "Chưa chọn bàn", "Vui lòng click chọn ít nhất một bàn để đặt."); return; } for (BanAn ban : danhSachBanDaChon) { if (ban.getTrangThai() != TrangThai.TRONG) { showAlert(AlertType.WARNING, "Bàn không hợp lệ", "Bàn " + ban.getMaBan() + " không trống."); return; } } try { LocalDate ngayDat = datePicker.getValue(); Gui_DatBan guiDatBan = new Gui_DatBan(trangChu, danhSachBanDaChon, ngayDat); trangChu.setMainContent(guiDatBan); } catch (Exception e) { e.printStackTrace(); new Gui_DatBan(trangChu, danhSachBanDaChon, datePicker.getValue()); } }
    private void xuLyDoiBan(BanAn banCu) { String maHDGop = banAn_DAO.getMaHoaDonTuBan(banCu.getMaBan(), ngayChon); List<String> dsBanGhep = (maHDGop != null) ? banAn_DAO.getDanhSachBanCungHoaDon(maHDGop) : new ArrayList<>(); Window owner = this.getScene().getWindow(); Gui_DoiBan dialog = new Gui_DoiBan(owner, banCu, ngayChon, banAn_DAO, phieuDatBan_DAO, dsBanGhep); dialog.showAndWait().ifPresent(result -> { if (result == ButtonType.OK) loadDataToGrid(); }); }
    
    private void xuLyHuyBan(BanAn ban) { String maHDGop = banAn_DAO.getMaHoaDonTuBan(ban.getMaBan(), ngayChon); List<String> dsBanGhep = (maHDGop != null) ? banAn_DAO.getDanhSachBanCungHoaDon(maHDGop) : new ArrayList<>(); if (dsBanGhep.size() > 1) showDialogHuyBanGhep(ban, maHDGop, dsBanGhep); else thucHienHuyBanDon(ban); }
    private void thucHienHuyBanDon(BanAn ban) { Alert alert = new Alert(AlertType.CONFIRMATION, "Hủy đặt bàn " + ban.getMaBan() + "?", ButtonType.YES, ButtonType.NO); alert.showAndWait().ifPresent(response -> { if (response == ButtonType.YES) { if (phieuDatBan_DAO.huyPhieuDatBanByMaBanVaNgay(ban.getMaBan(), ngayChon)) { showAlert(AlertType.INFORMATION, "Thành công", "Đã hủy."); loadDataToGrid(); } else showAlert(AlertType.ERROR, "Lỗi", "Hủy thất bại."); } }); }
    private void showDialogHuyBanGhep(BanAn ban, String maHDGop, List<String> dsBanGhep) { Dialog<ButtonType> dialog = new Dialog<>(); dialog.setTitle("Hủy Bàn Ghép"); dialog.setHeaderText("Hủy bàn " + ban.getMaBan() + " (Nhóm " + maHDGop + ")"); VBox content = new VBox(15); content.setPadding(new Insets(15)); content.getChildren().addAll(new Label("Bàn ghép: " + String.join(", ", dsBanGhep)), new Label("Hủy riêng hay hủy cả nhóm?")); ButtonType huyDonType = new ButtonType("Hủy riêng"); ButtonType huyGhepType = new ButtonType("Hủy cả nhóm"); dialog.getDialogPane().getButtonTypes().addAll(huyDonType, huyGhepType, ButtonType.CANCEL); dialog.getDialogPane().setContent(content); dialog.showAndWait().ifPresent(result -> { if (result == huyDonType) { thucHienHuyBanDon(ban); loadDataToGrid(); } else if (result == huyGhepType) { thucHienHuyBanGhep(maHDGop, dsBanGhep); loadDataToGrid(); } }); }
    private void thucHienHuyBanGhep(String maHDGop, List<String> dsBanGhep) { Alert alert = new Alert(AlertType.CONFIRMATION, "Hủy nhóm " + dsBanGhep.size() + " bàn?", ButtonType.YES, ButtonType.NO); alert.showAndWait().ifPresent(response -> { if (response == ButtonType.YES) { if (phieuDatBan_DAO.huyTatCaPhieuByMaHoaDon(maHDGop)) { showAlert(AlertType.INFORMATION, "Thành công", "Đã hủy nhóm."); loadDataToGrid(); } else showAlert(AlertType.ERROR, "Lỗi", "Hủy nhóm thất bại."); } }); }
    private void xuLyCheckIn(BanAn ban, Dialog<Void> dialog) { String maHDGop = banAn_DAO.getMaHoaDonTuBan(ban.getMaBan(), ngayChon); List<PhieuDatBan> dsPhieuDatBan = CheckIn_DAO.getPhieuDatBanTheoHoaDonVaNgay(maHDGop, ngayChon); KhachHang kh = KhachHang_DAO.getKhachHangById(dsPhieuDatBan.get(0).getKhachHang().getMaKhachHang()); Alert alert = new Alert(AlertType.CONFIRMATION, "Check-in cho " + kh.getTenKhachHang() + "?", ButtonType.YES, ButtonType.NO); alert.showAndWait().ifPresent(res -> { if (res == ButtonType.YES) { for (PhieuDatBan pdb : dsPhieuDatBan) { if (!controlCheckIn.capNhatTrangThai(pdb.getMaPhieu(), "Đang dùng") || !controlCheckIn.capNhatTrangThaiBan(pdb.getBan().getMaBan(), TrangThai.DANG_SU_DUNG)) { showAlert(AlertType.ERROR, "Lỗi", "Check-in thất bại."); return; } } showAlert(AlertType.INFORMATION, "Thành công", "Đã Check-in."); dialog.close(); loadDataToGrid(); } }); }
    private void xuLyThanhToan(String maHD) { this.setCenter(new Gui_ThanhToan(new NhanVien(), maHD)); }
    private void showAlert(AlertType alertType, String title, String content) { Alert alert = new Alert(alertType); alert.setTitle(title); alert.setHeaderText(null); alert.setContentText(content); alert.showAndWait(); }



    private void xuLyGoiMon(BanAn ban) {
        // ============================================================
        // 1. LẤY MÃ HÓA ĐƠN HIỆN TẠI
        // ============================================================
        String maHD = banAn_DAO.getMaHoaDonTuBan(ban.getMaBan(), ngayChon);

        if (maHD == null) {
            showAlert(AlertType.ERROR, "Lỗi", "Không tìm thấy hóa đơn của bàn: " + ban.getMaBan());
            return;
        }

        // ============================================================
        // 2. LOAD GIỎ HÀNG HIỆN TẠI
        // ============================================================
        List<ChiTietHoaDon> dsCTHD = ChiTietHoaDon_DAO.getChiTietHoaDonByMaHD(maHD);
        DecimalFormat df = new DecimalFormat("#,##0 VND");

        ObservableList<Object[]> gioHang = FXCollections.observableArrayList();
        for (ChiTietHoaDon ct : dsCTHD) {
            MonAn mon = ct.getMonAn();
            int slCu = ct.getSoLuong();
            double gia = mon.getGiaTien();

            gioHang.add(new Object[]{
                    mon.getTenMonAn(),  // 0
                    slCu,               // 1 - SL cũ
                    0,                  // 2 - SL thêm
                    slCu,               // 3 - Tổng SL
                    df.format(slCu * gia), // 4 - Thành tiền hiển thị
                    gia                 // 5 - Giá gốc
            });
        }

        // ============================================================
        // 3. TẠO LAYER MỜ
        // ============================================================
        Pane root = (Pane) this.getScene().getRoot();

        Rectangle overlay = new Rectangle();
        overlay.setFill(Color.rgb(0, 0, 0, 0.4));

        overlay.widthProperty().bind(root.widthProperty());
        overlay.heightProperty().bind(root.heightProperty());

        root.getChildren().add(overlay);

        // ============================================================
        // 4. TẠO POPUP
        // ============================================================
        Stage popup = new Stage();
        popup.initOwner(this.getScene().getWindow());
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Gọi món thêm - Bàn " + ban.getMaBan());
        popup.setResizable(false);

        // ============================================================
        // PANEL GIỎ HÀNG (PHẢI)
        // ============================================================
        VBox panelGioHang = new VBox(15);
        panelGioHang.setPadding(new Insets(15));
        panelGioHang.setPrefWidth(520);
        panelGioHang.setStyle("""
                -fx-background-color: white; 
                -fx-background-radius: 12;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 3);
            """);

        Label lblGioHang = new Label("Gọi món ăn");
        lblGioHang.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));

        TableView<Object[]> tbl = taoBangGioHang(gioHang, new TilePane());

        lblTongTien = new Label();
        lblTongTien.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        lblTongTien.setTextFill(Color.web("#e53e3e"));

        gioHang.addListener((ListChangeListener<Object[]>) c -> tinhTongTien(gioHang, lblTongTien));
        tinhTongTien(gioHang, lblTongTien);

        panelGioHang.getChildren().addAll(lblGioHang, tbl, lblTongTien);

        // ============================================================
        // PANEL MENU MÓN (TRÁI)
        // ============================================================
        VBox panelMenu = new VBox(10);
        panelMenu.setPrefWidth(560);
        panelMenu.setPadding(new Insets(15));
        panelMenu.setStyle("""
                -fx-background-color: #f8f9fa;
                -fx-background-radius: 12;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 8, 0, 0, 3);
            """);

        Label lblMenu = new Label("Chọn món ăn");
        lblMenu.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));

        ComboBox<String> cbLoai = new ComboBox<>();
        cbLoai.getItems().addAll("Tất cả", "Món ăn kèm", "Món khai vị",
                "Món chính", "Nước sốt", "Đồ uống", "Tráng miệng");
        cbLoai.setValue("Tất cả");
        

        TilePane tileMenu = new TilePane(10, 12);
        tileMenu.setPrefColumns(3);
        
        cbLoai.setOnAction(e -> {
            String loai = cbLoai.getValue();
            locTheoLoai(tileMenu, gioHang, df, tbl, loai);
        });
        

        taiDanhSachMonAn(tileMenu, gioHang, df, tbl);

        ScrollPane scMenu = new ScrollPane(tileMenu);
        scMenu.setFitToWidth(true);
        scMenu.setStyle("-fx-background-color: transparent;");

        HBox header = new HBox(lblMenu, new Region(), cbLoai);
        HBox.setHgrow(header.getChildren().get(1), Priority.ALWAYS);

        panelMenu.getChildren().addAll(header, scMenu);

        // ============================================================
        // BUTTONS
        // ============================================================
        Button btnXN = new Button("Xác nhận");
        btnXN.setStyle("""
                -fx-background-color:#082744; 
                -fx-text-fill:white; 
                -fx-background-radius:8;
                -fx-padding:10 25;
            """);

        Button btnHuy = new Button("Hủy");
        btnHuy.setStyle("""
                -fx-background-color:#dfe4ea; 
                -fx-background-radius:8;
                -fx-padding:10 25;
            """);

        btnHuy.setOnAction(e -> popup.close());

        btnXN.setOnAction(e -> {
            boolean coLoi = false;

            for (Object[] row : gioHang) {
                int slThem = (Integer) row[2];
                if (slThem <= 0) continue;

                String ten = (String) row[0];
                double gia = (Double) row[5];
                String maMon = MonAn_DAO.getMaMonByTen(ten);

                boolean ok = chiTietHoaDon_DAO.themHoacUpdate(maHD, maMon, slThem, gia);

                if (!ok) {
                    coLoi = true;
                    showAlert(AlertType.ERROR, "Lỗi", "Không thể thêm món: " + ten);
                    break;
                }
            }

            if (!coLoi) {
                showAlert(AlertType.INFORMATION, "Thành công",
                        "Đã gọi món cho bàn: " + ban.getMaBan());
                loadDataToGrid();
                popup.close();
            }
        });

        panelGioHang.getChildren().add(new HBox(10, btnHuy, btnXN));

        // ============================================================
        // TẠO GIAO DIỆN CHÍNH TRONG POPUP
        // ============================================================
        HBox main = new HBox(15, panelMenu, panelGioHang);
        main.setPadding(new Insets(10));

        Scene sc = new Scene(main, 1100, 520);
        sc.getStylesheets().add(getClass().getResource("/css/danhsachban.css").toExternalForm());
        
        popup.setScene(sc);

        // ============================================================
        // GỠ LỚP MỜ KHI ĐÓNG POPUP
        // ============================================================
        popup.setOnHidden(ev -> root.getChildren().remove(overlay));

        popup.show();
    }



    public void tinhTongTien(ObservableList<Object[]> gioHang, Label lblTongTien) {
    	
    	DecimalFormat df = new DecimalFormat("#,##0.0 VND");
    	double tong = 0;

        for (Object[] row : gioHang) {
            String tien = (String) row[4];

            // XÓA toàn bộ ký tự không phải số
            tien = tien.replaceAll("[^0-9]", "");

            if (!tien.isEmpty()) {
                tong += Double.parseDouble(tien);
            }
        }

        lblTongTien.setText("Tổng thành tiền: " + df.format(tong));
    }
    
    private void taiDanhSachMonAn(TilePane pane, ObservableList<Object[]> gioHang, DecimalFormat df, TableView<Object[]> table) {
        pane.getChildren().clear();
        List<String> ds = monAn_DAO.layDanhSachMonAnGiaKMString();
        for (String mon : ds) {
            pane.getChildren().add(taoTheMonAn(mon, gioHang, df, table));
        }
    }

    private VBox taoTheMonAn(String monStr, ObservableList<Object[]> gioHang, DecimalFormat df, TableView<Object[]> table) {
        String[] arr = monStr.split("-");
        String ma = arr.length > 0 ? arr[0] : "";
        String ten = arr.length > 1 ? arr[1] : "Món không xác định";
        double gia = arr.length > 6 ? Double.parseDouble(arr[6]) : 0.0;
        String anh = arr.length > 8 ? arr[8] : "";
        // Lấy SL hiện tại từ giỏ hàng để hiển thị trên lblSL
        int slHienTai = getSLThemHienTai(ten, gioHang);
        VBox card = new VBox(8);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(10));
        card.setPrefWidth(160);
        card.setStyle("""
                -fx-background-color:white;
                -fx-background-radius:12;
                -fx-effect:dropshadow(gaussian, rgba(0,0,0,0.12),4,0,0,1);
                """);
        // Ảnh
        ImageView img = new ImageView();
        img.setFitWidth(130);
        img.setFitHeight(90);
        img.setPreserveRatio(false);
        Rectangle clip = new Rectangle(130, 90);
        clip.setArcWidth(12);
        clip.setArcHeight(12);
        img.setClip(clip);
        loadImgTo(img, anh);
        Label lblTen = new Label(ten);
        lblTen.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        lblTen.setWrapText(true);
        lblTen.setAlignment(Pos.CENTER);
        Label lblGia = new Label(df.format(gia));
        lblGia.setTextFill(Color.web("#e53e3e"));
        lblGia.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        // Controls +/-
        Label lblSL = new Label(String.valueOf(slHienTai));
        lblSL.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        lblSL.setPrefWidth(26);
        lblSL.setAlignment(Pos.CENTER);
        Button btnTru = createRoundButton("-");
        Button btnCong = createRoundButton("+");
        // Sự kiện +/-btn
        btnCong.setOnAction(e -> {
            tangSL(ten, gia, lblSL, gioHang, df, table);
            table.refresh();
        });
        btnTru.setOnAction(e -> {
            giamSL(ten, gia, lblSL, gioHang, df, table);
            table.refresh();
        });
        HBox controls = new HBox(10, btnTru, lblSL, btnCong);
        controls.setAlignment(Pos.CENTER);
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        card.getChildren().addAll(img, lblTen, lblGia, spacer, controls);
        return card;
    }

    private int getSLThemHienTai(String ten, ObservableList<Object[]> gioHang) {
        for (Object[] row : gioHang) {
            if (row[0].equals(ten)) {
                return (Integer) row[2];
            }
        }
        return 0;
    }

    private void tangSL(String ten, double gia, Label lblSL, ObservableList<Object[]> gioHang, DecimalFormat df, TableView<Object[]> table) {
        boolean existed = false;
        for (int i = 0; i < gioHang.size(); i++) {
            Object[] row = gioHang.get(i);
            if (row[0].equals(ten)) {
                row[2] = (Integer) row[2] + 1; // slThem
                row[3] = (Integer) row[1] + (Integer) row[2]; // tongSL
                row[4] = df.format((Integer) row[3] * gia);
                existed = true;
                break;
            }
        }
        if (!existed) {
            gioHang.add(new Object[]{ten, 0, 1, 1, df.format(gia), gia});
        }
        int sl = Integer.parseInt(lblSL.getText()) + 1;
        lblSL.setText(String.valueOf(sl));
        table.refresh(); // Refresh table để cập nhật UI
    }

    private void giamSL(String ten, double gia, Label lblSL, ObservableList<Object[]> gioHang, DecimalFormat df, TableView<Object[]> table) {
        int sl = Integer.parseInt(lblSL.getText());
        if (sl == 0) return;
        for (int i = 0; i < gioHang.size(); i++) {
            Object[] row = gioHang.get(i);
            if (row[0].equals(ten)) {
                row[2] = (Integer) row[2] - 1;
                if ((Integer) row[2] <= 0 && (Integer) row[1] == 0) {
                    gioHang.remove(i);
                    break;
                }
                row[3] = (Integer) row[1] + (Integer) row[2];
                row[4] = df.format((Integer) row[3] * gia);
                break;
            }
        }
        sl--;
        lblSL.setText(String.valueOf(sl));
        table.refresh(); // Refresh table để cập nhật UI
    }

    private Button createRoundButton(String text) {
        Button btn = new Button(text);
        btn.setPrefSize(30, 30);
        btn.setStyle(
                "-fx-background-color: #EDF2F7; -fx-text-fill: #2d3436; -fx-font-weight: bold; " +
                        "-fx-background-radius: 15; -fx-cursor: hand;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #E2E8F0; -fx-text-fill: #2d3436; -fx-font-weight: bold; -fx-background-radius: 15;-fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #EDF2F7; -fx-text-fill: #2d3436; -fx-font-weight: bold; -fx-background-radius: 15;-fx-cursor: hand;"));
        return btn;
    }

    private void loadDefaultImage(ImageView imgView) {
        try { imgView.setImage(new Image(getClass().getResourceAsStream("/img/default-food.png"))); }
        catch (Exception e) { imgView.setStyle("-fx-background-color: #E2E8F0;"); }
    }

    private void loadImgTo(ImageView imgView, String path) {
        if (path != null && !path.isEmpty()) {
            String SUPABASE_BASE_URL = "https://yxemxycygkhxygaydgcl.supabase.co/storage/v1/object/public/image/";
            String imagePath = ImageCacheManager.getImagePath(SUPABASE_BASE_URL, path);
            if (imagePath != null) {
                try {
                    Image img = new Image(imagePath);
                    imgView.setImage(img);
                    imgView.setPreserveRatio(false);
                    imgView.setFitHeight(90);
                } catch (Exception e) { loadDefaultImage(imgView); }
            } else loadDefaultImage(imgView);
        } else loadDefaultImage(imgView);
    }

    private TableView<Object[]> taoBangGioHang(ObservableList<Object[]> gioHang, TilePane tilePane) {
        TableView<Object[]> table = new TableView<>();
        table.setItems(gioHang);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // ===================== CỘT TÊN MÓN =====================
        TableColumn<Object[], String> colTen = new TableColumn<>("Tên món");
        colTen.setCellValueFactory(row ->
            new ReadOnlyStringWrapper((String) row.getValue()[0])
        );
        colTen.setPrefWidth(150);

        // ===================== CỘT SL CŨ =====================
        TableColumn<Object[], Integer> colSLCu = new TableColumn<>("SL cũ");
        colSLCu.setCellValueFactory(row ->
            new ReadOnlyObjectWrapper<Integer>((Integer) row.getValue()[1])
        );
        colSLCu.setPrefWidth(65);

        // ===================== CỘT SL THÊM (CÓ THỂ NHẬP) =====================
        TableColumn<Object[], Integer> colSLThem = new TableColumn<>("Thêm");
        colSLThem.setPrefWidth(70);
        colSLThem.setCellValueFactory(row ->
            new ReadOnlyObjectWrapper<Integer>((Integer) row.getValue()[2])
        );
        colSLThem.setCellFactory(col -> new TableCell<>() {
            private final TextField tf = new TextField();
            {
                tf.setMaxWidth(55);
                tf.setAlignment(Pos.CENTER);
                tf.setOnAction(e -> {updateSLThem();});
                tf.textProperty().addListener((obs, oldValue, newValue) -> {
                    if (!newValue.matches("\\d*")) {
                        tf.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                    if (!tf.getText().isEmpty() && Integer.parseInt(tf.getText()) < 0) {
                        tf.setText("0");
                    }
                    tinhTongTien(gioHang, lblTongTien);
                });
            }

            private void updateSLThem() {
                try {
                    String text = tf.getText().trim();
                    if (text.isEmpty()) text = "0";
                    int slNew = Integer.parseInt(text) < 0 ? 0 : Integer.parseInt(text);

                    Object[] row = getTableView().getItems().get(getIndex());
                    row[2] = slNew;
                    row[3] = (Integer) row[1] + slNew;
                    double gia = (Double) row[5];
                    row[4] = new DecimalFormat("#,##0 VND").format(((Integer) row[3]) * gia);
                    getTableView().refresh();

                    // Cập nhật lại số lượng trên card món ăn
                    Platform.runLater(() -> {
                        if (tilePane != null) {
                            for (Node node : tilePane.getChildren()) {
                                if (node instanceof VBox card) {
                                    Label lblTen = (Label) card.getChildren().get(1);
                                    
                                    if (lblTen.getText().equals(row[0])) {
                                        HBox controls = (HBox) card.getChildren().get(4);
                                        Label lblSL = (Label) controls.getChildren().get(1);
                                        lblSL.setText(String.valueOf(slNew));
                                        break;
                                    }
                                }
                            }
                        }
                    });
                } catch (Exception ex) {
                    tf.setText("0");
                }
            }

            @Override
            protected void updateItem(Integer value, boolean empty) {
                super.updateItem(value, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    int displayValue = Math.max(0, value != null ? value : 0);
                    tf.setText(String.valueOf(displayValue));
                    setGraphic(tf);
                }
            }
        });

        // ===================== CỘT TỔNG SL =====================
        TableColumn<Object[], Integer> colTongSL = new TableColumn<>("Tổng SL");
        colTongSL.setCellValueFactory(row ->
            new ReadOnlyObjectWrapper<Integer>((Integer) row.getValue()[3])
        );
        colTongSL.setPrefWidth(70);

        // ===================== CỘT TỔNG TIỀN =====================
        TableColumn<Object[], String> colTongTien = new TableColumn<>("Thành tiền");
        colTongTien.setCellValueFactory(row ->
            new ReadOnlyStringWrapper((String) row.getValue()[4])
        );
        colTongTien.setPrefWidth(110);

        // ===================== GHÉP CỘT =====================
        table.getColumns().addAll(colTen, colSLCu, colSLThem, colTongSL, colTongTien);
        return table;
    }
    
	private void locTheoLoai(TilePane tileMenu, ObservableList<Object[]> gioHang, DecimalFormat df,
			TableView<Object[]> table, String loaiChon) {

		tileMenu.getChildren().clear();
		List<String> ds = monAn_DAO.layDanhSachMonAnGiaKMString();

		for (String mon : ds) {

			String[] arr = mon.split("-");
			String loai = arr.length > 2 ? arr[2] : ""; // arr[2] phải là cột loại món (để ý)

			// Nếu chọn "Tất cả" → load tất
			if (loaiChon.equals("Tất cả") || loai.equalsIgnoreCase(loaiChon)) {
				tileMenu.getChildren().add(taoTheMonAn(mon, gioHang, df, table));
			}
		}
	}

}



