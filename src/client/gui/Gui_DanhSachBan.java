
package client.gui;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import client.ctrl.CheckIn_Ctrl;
import client.service.BanAnClient;
import client.service.CheckInClient;
import client.service.ChiTietHoaDonClient;
import client.service.KhachHangClient;
import client.service.MonAnClient;
import client.service.PhieuDatBanClient;
import common.entity.*;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.BoxBlur;
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
import client.utils.ImageCacheManager;

public class Gui_DanhSachBan extends BorderPane {
    private GridPane luoiBan;
    private BanAnClient banAnClient;
    private Gui_TrangChu trangChu;
    private List<BanAn> danhSachBanDaChon = new ArrayList<>();
    private TextField timKiem;
    private ComboBox<String> cmbTatCa;
    private ToggleButton tang1;
    private ToggleButton tang2;
    private ViTri viTriHienTai = ViTri.LAU_1;
    private DatePicker datePicker;
    private LocalDate ngayChon = LocalDate.now();
    private PhieuDatBanClient phieuDatBanClient;
    private CheckIn_Ctrl controlCheckIn = new CheckIn_Ctrl();
    private MonAnClient monAnClient = new MonAnClient();
    private ChiTietHoaDonClient chiTietHoaDonClient = new ChiTietHoaDonClient();
    private Label lblTongTien;
    private VBox topContainer;
    private HBox footer;

    public Gui_DanhSachBan(Gui_TrangChu trangChu) {
        this.trangChu = trangChu;
        banAnClient = new BanAnClient();
        phieuDatBanClient = new PhieuDatBanClient();

        // --- SETUP ROOT STYLE ---
        this.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa 0%, #e9ecef 100%);");
        this.setPadding(new Insets(20));

        // --- HEADER & FILTERS ---
        topContainer = new VBox(20);
        topContainer.getChildren().addAll(createModernHeader(), createModernFilterBar());
        topContainer.setPadding(new Insets(0, 0, 20, 0));

        // --- CONTENT AREA (Grid Bàn) ---
        ScrollPane scrollPane = createScrollableGrid();

        // --- FOOTER ---
        footer = createFooter();

        // --- LAYOUT ---
        this.setTop(topContainer);
        this.setCenter(scrollPane);
        this.setBottom(footer);

        loadDataToGrid();
    }


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
        tang1 = new ToggleButton("Tầng 1");
        tang2 = new ToggleButton("Tầng 2");
        String toggleStyle = "-fx-background-color: #f1f2f6; -fx-text-fill: #2d3436; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand; -fx-padding: 8 15;";
        String toggleSelectedStyle = "-fx-background-color: #082744; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand; -fx-padding: 8 15;";
        tang1.setStyle(toggleSelectedStyle);
        tang2.setStyle(toggleStyle);
        ToggleGroup group = new ToggleGroup();
        tang1.setToggleGroup(group);
        tang2.setToggleGroup(group);
        tang1.setOnAction(e -> {
            viTriHienTai = ViTri.LAU_1;
            tang1.setStyle(toggleSelectedStyle);
            tang2.setStyle(toggleStyle);
            loadDataToGrid();
        });
        tang2.setOnAction(e -> {
            viTriHienTai = ViTri.LAU_2;
            tang2.setStyle(toggleSelectedStyle);
            tang1.setStyle(toggleStyle);
            loadDataToGrid();
        });
        HBox boxKhuVuc = new HBox(10, lblKhuVuc, tang1, tang2);
        boxKhuVuc.setAlignment(Pos.CENTER_LEFT);
        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        Label lblNgay = new Label("📅 Ngày xem:");
        lblNgay.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        lblNgay.setTextFill(Color.web("#2D3748")); // Dark gray text

        datePicker = new DatePicker(ngayChon);
        datePicker.setPrefWidth(160);
        datePicker.setEditable(false);

        datePicker.setConverter(new javafx.util.StringConverter<LocalDate>() {
            // Format ngày hiển thị thành dd/MM/yyyy cho quen thuộc với người Việt
            java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate date) {
                return (date != null) ? dateFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty()) ? LocalDate.parse(string, dateFormatter) : null;
            }
        });

// 1. STYLE CHO Ô NHẬP LIỆU (Input Box)
// Bo góc, viền xám nhạt, bỏ màu nền của nút lịch để nó hòa vào ô nhập
        datePicker.setStyle(
                "-fx-background-color: white; " +
                        "-fx-border-color: #E2E8F0; " +
                        "-fx-border-radius: 8; " +
                        "-fx-background-radius: 8; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-family: 'Segoe UI';"
        );

// 2. STYLE CAO CẤP CHO POPUP LỊCH (Inject CSS trực tiếp)
// Đoạn này sẽ đổi màu xanh mặc định thành màu #082744 của app bạn
        String customDatePickerCss = "data:text/css," +
                // 1. Chỉnh nút icon lịch bên phải ô input
                ".date-picker .arrow-button { -fx-background-color: transparent; -fx-cursor: hand; }" +
                ".date-picker .arrow-button .arrow { -fx-background-color: #082744; }" +

                // 2. Chỉnh bảng popup
                ".date-picker-popup { -fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 5); }" +
                ".date-picker-popup .month-year-pane { -fx-background-color: #082744; -fx-padding: 10; }" +
                ".date-picker-popup .month-year-pane .label { -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; }" +

                // 3. CHỈNH NÚT NEXT / PREV (QUAN TRỌNG)
                ".date-picker-popup .spinner .button { -fx-background-color: transparent; -fx-cursor: hand; }" +
                ".date-picker-popup .spinner .button:hover { -fx-background-color: rgba(255, 255, 255, 0.2); -fx-background-radius: 50%25; }" + // Hiệu ứng hover tròn
                ".date-picker-popup .spinner .button .left-arrow { -fx-background-color: white; -fx-scale-x: 0.8; -fx-scale-y: 0.8; }" + // Mũi tên trái màu trắng, nhỏ lại chút cho tinh tế
                ".date-picker-popup .spinner .button .right-arrow { -fx-background-color: white; -fx-scale-x: 0.8; -fx-scale-y: 0.8; }" + // Mũi tên phải màu trắng

                // 4. Chỉnh các ô ngày
                ".date-picker-popup .day-cell { -fx-background-color: white; -fx-text-fill: #2D3748; -fx-font-size: 13px; -fx-border-color: transparent; }" +
                ".date-picker-popup .day-cell:hover { -fx-background-color: #EBF8FF; -fx-text-fill: #082744; -fx-background-radius: 5; }" +
                ".date-picker-popup .day-cell:selected { -fx-background-color: #082744; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; }" +
                ".date-picker-popup .today { -fx-border-color: #E53E3E; -fx-border-radius: 5; -fx-border-width: 1; }";

// Thêm CSS này vào Scene (hoặc Parent hiện tại)
        this.getStylesheets().add(customDatePickerCss);


// 3. LOGIC CHẶN NGÀY & TÔ MÀU NGÀY QUÁ KHỨ (DayCellFactory)
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (date.isBefore(LocalDate.now())) {
                    // Ngày quá khứ: Vô hiệu hóa + Màu xám + Gạch ngang (tùy chọn)
                    setDisable(true);
                    setStyle("-fx-background-color: #f7fafc; -fx-text-fill: #cbd5e0;");
                }
            }
        });

// 4. XỬ LÝ SỰ KIỆN CHỌN
        datePicker.setOnAction(e -> {
            LocalDate selectedDate = datePicker.getValue();
            if (selectedDate != null) {
                if (selectedDate.isBefore(LocalDate.now())) {
                    // Tự động nhảy về hôm nay nếu cố tình chọn sai (qua phím tắt)
                    datePicker.setValue(LocalDate.now());
                    ngayChon = LocalDate.now();
                } else {
                    ngayChon = selectedDate;
                }
                loadDataToGrid();
            }
        });
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
        HBox boxNgay = new HBox(10, lblNgay, datePicker);
        boxNgay.setAlignment(Pos.CENTER_LEFT);
        row1.getChildren().addAll(boxKhuVuc, spacer1, boxNgay);
        HBox row2 = new HBox(15);
        row2.setAlignment(Pos.CENTER_LEFT);
        Label lblTimKiem = new Label("🔍 Tìm bàn:");
        lblTimKiem.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        timKiem = new TextField();
        timKiem.setPromptText("Nhập số bàn...");
        timKiem.setPrefWidth(200);
        timKiem.setStyle("-fx-background-radius: 5; -fx-padding: 8;");
        timKiem.textProperty().addListener((obs, oldVal, newVal) -> loadDataToGrid());
        Label lblTrangThai = new Label("Trạng thái:");
        lblTrangThai.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        lblTrangThai.setPadding(new Insets(0, 0, 0, 20));
        cmbTatCa = new ComboBox<>();
        cmbTatCa.getItems().addAll("Tất cả", "Bàn trống", "Đang sử dụng", "Đã đặt bàn", "Bàn VIP");
        cmbTatCa.setValue("Tất cả");
        cmbTatCa.setPrefWidth(150);
        cmbTatCa.setStyle("-fx-background-radius: 5; -fx-padding: 5;");
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

        List<BanAn> dsBanAnFull = banAnClient.getTrangThaiBanTheoNgayVaViTri(viTriHienTai, ngayChon);
        String tuKhoa = timKiem.getText().trim().toLowerCase();
        String loaiLoc = cmbTatCa.getValue();

        List<BanAn> dsDaLoc = new ArrayList<>();
        for (BanAn ban : dsBanAnFull) {
            boolean khopTuKhoa = tuKhoa.isEmpty() || ban.getMaBan().toLowerCase().contains(tuKhoa);
            boolean khopLoaiLoc = true;
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
                    if (ban.getTrangThai() == TrangThai.TRONG) {
                        theBan.setStyle(defaultStyle);
                    } else {
                        // Giữ style màu của bàn có khách nhưng bỏ viền xanh chọn
                        // Bạn có thể tùy chỉnh lại style này cho đẹp
                        theBan.setStyle(theBan.getStyle().replace("-fx-border-color: #3182ce;", "-fx-border-color: transparent;"));
                    }
                } else {
//                    if (ban.getTrangThai() == TrangThai.TRONG) {
//                        danhSachBanDaChon.add(ban);
//                        theBan.setStyle(selectedStyle);
//                    } else {
//                        showAlert(AlertType.WARNING, "Không thể chọn", "Chỉ có thể chọn bàn đang 'Trống'.");
//                    }
                    danhSachBanDaChon.add(ban);

                    // Style khi được chọn (Viền xanh đậm)
                    String currentStyle = theBan.getStyle();
                    // Đè viền xanh lên style hiện tại
                    theBan.setStyle(currentStyle + "-fx-border-color: #3182ce; -fx-border-width: 3;");
                }
            });

            if (ban.getTrangThai() == TrangThai.TRONG) {
                theBan.setOnMouseEntered(e -> {
                    if (!danhSachBanDaChon.contains(ban)) {
                        // Hover thì vẫn giữ viền transparent, chỉ thêm bóng đổ
                        theBan.setStyle(defaultStyle + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 4);");
                        theBan.setScaleX(1.03);
                        theBan.setScaleY(1.03);
                    }
                    theBan.setCursor(Cursor.HAND);
                });
                theBan.setOnMouseExited(e -> {
                    if (!danhSachBanDaChon.contains(ban)) theBan.setStyle(defaultStyle);
                    else theBan.setStyle(selectedStyle);

                    theBan.setScaleX(1.0);
                    theBan.setScaleY(1.0);
                    theBan.setCursor(Cursor.DEFAULT);
                });
            } else {
                // Các bàn đang bận/đặt chỉ có hiệu ứng nổi nhẹ
                theBan.setOnMouseEntered(e -> {
                    theBan.setScaleX(1.02);
                    theBan.setScaleY(1.02);
                });
                theBan.setOnMouseExited(e -> {
                    theBan.setScaleX(1.0);
                    theBan.setScaleY(1.0);
                });
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
            case DANG_SU_DUNG:
                headerColor = "#38A169";
                break; // Xanh lá
            case DA_DAT:
                headerColor = "#E53E3E";
                break;       // Đỏ
            default:
                headerColor = "#718096";
                break;           // Xám
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
                iconVip.setFitWidth(20);
                iconVip.setFitHeight(20);
                header.getChildren().add(iconVip);
            } catch (Exception e) {
            }
        }

        VBox body = new VBox(10);
        body.setAlignment(Pos.CENTER);
        body.setPadding(new Insets(10));
        body.setPrefHeight(90);

        String statusText = switch (ban.getTrangThai()) {
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
// Removed redundant logic
            String maHDGop = banAnClient.getMaHoaDonTuBan(ban.getMaBan());
            boolean isMerged = (maHDGop != null && banAnClient.getDanhSachBanCungHoaDon(maHDGop).size() > 1);
            if (isMerged) {
                try {
                    ImageView iconLink = new ImageView(new Image(getClass().getResource("/img/link.png").toExternalForm()));
                    iconLink.setFitWidth(18);
                    iconLink.setFitHeight(18);
                    Label lblMerged = new Label(" Bàn ghép");
                    lblMerged.setTextFill(Color.web("#3182ce"));
                    lblMerged.setFont(Font.font("Segoe UI", 11));
                    iconBox.getChildren().addAll(iconLink, lblMerged);
                } catch (Exception e) {
                }
            }
        }

        Button btnDetail = new Button("Xem chi tiết");
        btnDetail.setStyle("-fx-background-color: transparent; -fx-text-fill: #718096; -fx-underline: true; -fx-cursor: hand; -fx-font-size: 11px;");
        btnDetail.setOnAction(e -> {
            showTableInfoDialog(ban);
            e.consume();
        });

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
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button btnDatBan = new Button("➕ Đặt Bàn Ngay");
        btnDatBan.setStyle("-fx-background-color: #082744; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 8; -fx-cursor: hand;");
        btnDatBan.setOnMouseEntered(e -> btnDatBan.setStyle("-fx-background-color: #0a3d6a; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 8; -fx-cursor: hand;"));
        btnDatBan.setOnMouseExited(e -> btnDatBan.setStyle("-fx-background-color: #082744; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 8; -fx-cursor: hand;"));
        btnDatBan.setOnAction(e -> xuLyDatBan());

        Button btnGopBan = new Button("🔗 Gộp Bàn");
        btnGopBan.setStyle("-fx-background-color: #D97706; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 8; -fx-cursor: hand;");
        btnGopBan.setOnMouseEntered(e -> btnGopBan.setStyle("-fx-background-color: #B45309; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 8; -fx-cursor: hand;"));
        btnGopBan.setOnMouseExited(e -> btnGopBan.setStyle("-fx-background-color: #D97706; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 8; -fx-cursor: hand;"));

        btnGopBan.setOnAction(e -> xuLyGopBanNhanh());
        footer.getChildren().addAll(legend, spacer, btnGopBan, btnDatBan);
        return footer;
    }

    private void xuLyGopBanNhanh() {
        if (danhSachBanDaChon.size() < 2) {
            showAlert(AlertType.WARNING, "Chưa đủ thông tin", "Vui lòng chọn 1 bàn ĐANG DÙNG (Gốc) và các bàn TRỐNG muốn gộp thêm.");
            return;
        }

        List<BanAn> banGocList = new ArrayList<>();
        List<BanAn> banTrongList = new ArrayList<>();

        // Phân loại bàn đã chọn
        for (BanAn b : danhSachBanDaChon) {
            if (b.getTrangThai() == TrangThai.DANG_SU_DUNG || b.getTrangThai() == TrangThai.DA_DAT) {
                banGocList.add(b);
            } else if (b.getTrangThai() == TrangThai.TRONG) {
                banTrongList.add(b);
            }
        }

        // Validate logic
        if (banGocList.isEmpty()) {
            showAlert(AlertType.WARNING, "Sai quy trình", "Bạn chưa chọn bàn nào đang có khách để gộp vào.");
            return;
        }

        if (banGocList.size() > 1) {
            // Kiểm tra xem các bàn gốc này có cùng mã hóa đơn không (trường hợp chọn 2 bàn đã gộp từ trước + 1 bàn mới)
            String maHDChuan = banAnClient.getMaHoaDonTuBan(banGocList.get(0).getMaBan());
            for (BanAn b : banGocList) {
                String maCurrent = banAnClient.getMaHoaDonTuBan(b.getMaBan());
                if (!maCurrent.equals(maHDChuan)) {
                    showAlert(AlertType.ERROR, "Xung đột", "Bạn đang chọn 2 bàn thuộc 2 hóa đơn khác nhau. Không thể gộp tự động.");
                    return;
                }
            }
        }

        if (banTrongList.isEmpty()) {
            showAlert(AlertType.WARNING, "Sai quy trình", "Bạn chưa chọn bàn trống nào để thêm vào.");
            return;
        }

        // Lấy thông tin từ bàn gốc (Bàn đầu tiên trong list gốc)
        BanAn banGoc = banGocList.get(0);
        String maHD = banAnClient.getMaHoaDonTuBan(banGoc.getMaBan());

        if (maHD == null) {
            showAlert(AlertType.ERROR, "Lỗi dữ liệu", "Không tìm thấy hóa đơn của bàn gốc.");
            return;
        }

        // Xác nhận người dùng
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận gộp bàn");
        confirm.setHeaderText("Gộp các bàn trống vào hóa đơn: " + maHD);
        String msg = "Bàn gốc: " + banGoc.getMaBan() + "\n" +
                "Thêm các bàn: ";
        for (BanAn b : banTrongList) msg += b.getMaBan() + " ";
        confirm.setContentText(msg);

        confirm.showAndWait().ifPresent(type -> {
            if (type == ButtonType.OK) {
                boolean allSuccess = true;

                // Lấy thông tin phiếu đặt bàn cũ để copy thông tin khách hàng/nhân viên
                PhieuDatBan phieuGoc = phieuDatBanClient.getPhieuDatBanMoiNhat(banGoc.getMaBan());
                TrangThai trangThaiDich;
                String trangThaiPhieu;

                if (banGoc.getTrangThai() == TrangThai.DA_DAT) {
                    trangThaiDich = TrangThai.DA_DAT;
                    trangThaiPhieu = "Đã đặt";
                } else {
                    trangThaiDich = TrangThai.DANG_SU_DUNG;
                    trangThaiPhieu = "Đang dùng";
                }

                for (BanAn banMoi : banTrongList) {
                    // 1. Cập nhật trạng thái bàn thành ĐANG DÙNG
                    boolean upBan = banAnClient.updateTrangThaiBan(banMoi, trangThaiDich);

                    // 2. Tạo phiếu đặt bàn mới trỏ về MaHD cũ
                    PhieuDatBan pMoi = new PhieuDatBan();
                    pMoi.setBan(banMoi);

                    HoaDon hd = phieuGoc.getHoaDon();
                    if (hd.getNgayTao() == null) hd.setNgayTao(LocalDateTime.now());
                    pMoi.setHoaDon(hd);

                    pMoi.setKhachHang(phieuGoc.getKhachHang());
                    pMoi.setNhanVien(phieuGoc.getNhanVien()); // Hoặc nhân viên đang login
                    pMoi.setThoiGianBatDau(phieuGoc.getThoiGianBatDau());

                    pMoi.setTrangThai(trangThaiPhieu);
                    pMoi.setSoNguoi(0); // Số người có thể để 0 hoặc nhập thêm logic hỏi user
                    pMoi.setGhiChu("Gộp theo bàn " + banGoc.getMaBan());

                    boolean upPhieu = phieuDatBanClient.themPhieuDatBan(pMoi, trangThaiPhieu);

                    if (!upBan || !upPhieu) allSuccess = false;

                    try {
                        Thread.sleep(50); // Nghỉ 50ms (0.05 giây)
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

                if (allSuccess) {
                    showAlert(AlertType.INFORMATION, "Thành công", "Đã gộp bàn thành công!");
                    loadDataToGrid(); // Load lại giao diện
                } else {
                    showAlert(AlertType.ERROR, "Có lỗi", "Một số bàn không thể gộp. Vui lòng kiểm tra lại.");
                    loadDataToGrid();
                }
            }
        });
    }

    private HBox createLegendItem(String colorHex, String text) {
        HBox item = new HBox(5);
        item.setAlignment(Pos.CENTER_LEFT);
        Circle dot = new Circle(6, Color.web(colorHex));
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        lbl.setTextFill(Color.web("#636e72"));
        item.getChildren().addAll(dot, lbl);
        return item;
    }

    private HBox createLegendItemImg(String imgPath, String text) {
        HBox item = new HBox(5);
        item.setAlignment(Pos.CENTER_LEFT);
        try {
            ImageView img = new ImageView(new Image(getClass().getResource(imgPath).toExternalForm()));
            img.setFitWidth(16);
            img.setFitHeight(16);
            Label lbl = new Label(text);
            lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
            lbl.setTextFill(Color.web("#636e72"));
            item.getChildren().addAll(img, lbl);
        } catch (Exception e) {
        }
        return item;
    }

    private void showTableInfoDialog(BanAn ban) {
        Dialog<Void> dialog = new Dialog<>();

        // Lấy window cha để làm hiệu ứng mờ
        Window owner = this.getScene().getWindow();
        dialog.initOwner(owner);
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.getDialogPane().getScene().setFill(Color.TRANSPARENT);

        // --- 1. HIỆU ỨNG OVERLAY ---
        dialog.setOnShowing(e -> {
            if (owner.getScene() != null) {
                BoxBlur blur = new BoxBlur(10, 10, 3);
                owner.getScene().getRoot().setEffect(blur);
            }
        });
        dialog.setOnHidden(e -> {
            if (owner.getScene() != null) {
                owner.getScene().getRoot().setEffect(null);
            }
        });

        // --- 2. HEADER ---
        Label title = new Label("Thông tin bàn " + ban.getMaBan());
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#1A202C"));

        Button closeButton = new Button("✕");
        closeButton.setStyle("-fx-background-color: transparent; -fx-font-size: 18px; -fx-cursor: hand; -fx-text-fill: #A0AEC0;");
        closeButton.setOnMouseEntered(e -> closeButton.setStyle("-fx-background-color: transparent; -fx-font-size: 18px; -fx-cursor: hand; -fx-text-fill: #E53E3E;"));
        closeButton.setOnMouseExited(e -> closeButton.setStyle("-fx-background-color: transparent; -fx-font-size: 18px; -fx-cursor: hand; -fx-text-fill: #A0AEC0;"));
        closeButton.setOnAction(e -> dialog.close());

        HBox headerPane = new HBox(title, new Region(), closeButton);
        HBox.setHgrow(headerPane.getChildren().get(1), Priority.ALWAYS);
        headerPane.setAlignment(Pos.CENTER_LEFT);
        headerPane.setPadding(new Insets(20, 25, 20, 25));
        headerPane.setStyle("-fx-background-color: white; -fx-background-radius: 15 15 0 0; -fx-border-color: #F1F5F9; -fx-border-width: 0 0 1 0;");

        // --- 3. CONTENT ---
        VBox contentBox = new VBox(15);
        contentBox.setPadding(new Insets(25));
        contentBox.setStyle("-fx-background-color: white;");

        String statusText, subText, bgColor, textColor, borderColor;
        switch (ban.getTrangThai()) {
            case DANG_SU_DUNG:
                statusText = "Đang phục vụ";
                subText = "Có khách đang sử dụng";
                bgColor = "#F0FFF4";
                textColor = "#22543D";
                borderColor = "#C6F6D5";
                break;
            case DA_DAT:
                statusText = "Đã đặt trước";
                subText = "Khách sắp đến";
                bgColor = "#FFF5F5";
                textColor = "#9B2C2C";
                borderColor = "#FED7D7";
                break;
            default:
                statusText = "Bàn trống";
                subText = "Sẵn sàng đón khách";
                bgColor = "#EBF8FF";
                textColor = "#2C5282";
                borderColor = "#BEE3F8";
                break;
        }

        VBox statusBox = new VBox(5,
                createLabel(statusText, textColor, 16, true),
                createLabel(subText, textColor, 13, false)
        );
        statusBox.setPadding(new Insets(15));
        statusBox.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 10; -fx-border-color: " + borderColor + "; -fx-border-radius: 10;");

        contentBox.getChildren().addAll(
                statusBox,
                createDetailRow("Loại bàn:", ban.getLoai().name().equals("VIP") ? "⭐ VIP" : "Thường"),
                createDetailRow("Vị trí:", ban.getViTri().name().replace("_", " "))
        );

        boolean isBookedOrInUse = (ban.getTrangThai() == TrangThai.DA_DAT || ban.getTrangThai() == TrangThai.DANG_SU_DUNG);
        if (isBookedOrInUse) {
            List<PhieuDatBan> dsPdb = phieuDatBanClient.getDanhSachPhieuDatBanByMaBanVaNgay(ban.getMaBan(), datePicker.getValue());
            if (dsPdb != null && !dsPdb.isEmpty()) {
                contentBox.getChildren().add(new Separator());
                contentBox.getChildren().add(createLabel("Danh sách đặt bàn hôm nay:", "#4A5568", 14, true));
                
                VBox listPdbBox = new VBox(10);
                java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm");

                for (PhieuDatBan pdbInfo : dsPdb) {
                    VBox item = new VBox(5);
                    item.setPadding(new Insets(12));
                    // Đổi nền sang màu xám cực nhạt nhưng đậm hơn chút để nổi bật
                    item.setStyle("-fx-background-color: #EDF2F7; -fx-background-radius: 10; -fx-border-color: #CBD5E0; -fx-border-radius: 10;");
                    
                    String tenKhach = (pdbInfo.getKhachHang() != null && pdbInfo.getKhachHang().getTenKhachHang() != null) 
                                      ? pdbInfo.getKhachHang().getTenKhachHang() : "Khách vãng lai";
                    String thoiGian = pdbInfo.getThoiGianBatDau().format(timeFormatter) + " - " + 
                                     (pdbInfo.getThoiGianKetThuc() != null ? pdbInfo.getThoiGianKetThuc().format(timeFormatter) : "---");
                    
                    HBox rowInfo = new HBox(12);
                    rowInfo.setAlignment(Pos.CENTER_LEFT);
                    
                    Label lblInfo = new Label(thoiGian + " | " + tenKhach + " (" + pdbInfo.getTrangThai() + ")");
                    // ÉP MÀU ĐEN ĐẬM BẰNG STYLE
                    lblInfo.setStyle("-fx-text-fill: #1A202C; -fx-font-weight: bold; -fx-font-size: 14px;");
                    
                    Region spacerRow = new Region();
                    HBox.setHgrow(spacerRow, Priority.ALWAYS);
                    
                    rowInfo.getChildren().addAll(lblInfo, spacerRow);

                    // Chỉ hiện nút Check-in nếu phiếu đang ở trạng thái "Đã đặt"
                    if (pdbInfo.getTrangThai().equalsIgnoreCase("Đã đặt")) {
                        Button btnCi = createStyledButton("Check-in", "#2563EB", "#DBEAFE", ev -> {
                            xuLyCheckIn(pdbInfo, dialog);
                        });
                        btnCi.setScaleX(0.8); btnCi.setScaleY(0.8); // Nhỏ lại cho vừa
                        rowInfo.getChildren().add(btnCi);
                    }
                    
                    // MỚI: Hiện nút Thanh toán trực tiếp nếu phiếu đang "Đang dùng"
                    if (pdbInfo.getTrangThai().equalsIgnoreCase("Đang dùng")) {
                        Button btnPay = createStyledButton("Thanh toán", "#059669", "#D1FAE5", ev -> {
                            if (pdbInfo.getHoaDon() != null) {
                                xuLyThanhToan(pdbInfo.getHoaDon().getMaHoaDon());
                                dialog.close();
                            } else {
                                showAlert(AlertType.ERROR, "Lỗi", "Phiếu này không có mã hóa đơn!");
                            }
                        });
                        btnPay.setScaleX(0.8); btnPay.setScaleY(0.8);
                        rowInfo.getChildren().add(btnPay);
                    }
                    
                    item.getChildren().add(rowInfo);
                    if (pdbInfo.getGhiChu() != null && !pdbInfo.getGhiChu().isEmpty()) {
                        Label lblNote = new Label("📝 " + pdbInfo.getGhiChu());
                        lblNote.setFont(Font.font("Segoe UI", 11));
                        lblNote.setTextFill(Color.GRAY);
                        item.getChildren().add(lblNote);
                    }
                    
                    listPdbBox.getChildren().add(item);
                }
                
                ScrollPane scrollPdb = new ScrollPane(listPdbBox);
                scrollPdb.setFitToWidth(true);
                scrollPdb.setPrefHeight(150);
                scrollPdb.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
                contentBox.getChildren().add(scrollPdb);
            }

            String maHDGop = banAnClient.getMaHoaDonTuBan(ban.getMaBan());
            if (maHDGop != null) {
                List<String> dsBanGhep = banAnClient.getDanhSachBanCungHoaDon(maHDGop);
                if (dsBanGhep.size() > 1) {
                    HBox boxGhep = new HBox(10);
                    boxGhep.setAlignment(Pos.CENTER_LEFT);
                    boxGhep.setStyle("-fx-background-color: #EDF2F7; -fx-padding: 10; -fx-background-radius: 8;");
                    try {
                        ImageView linkIcon = new ImageView(new Image(getClass().getResource("/img/link.png").toExternalForm()));
                        linkIcon.setFitWidth(16);
                        linkIcon.setFitHeight(16);
                        boxGhep.getChildren().add(linkIcon);
                    } catch (Exception e) {
                    }
                    VBox infoGhep = new VBox(2,
                            createLabel("Đang ghép cùng:", "#2D3748", 13, true),
                            createLabel(String.join(", ", dsBanGhep), "#3182CE", 13, true)
                    );
                    boxGhep.getChildren().add(infoGhep);
                    contentBox.getChildren().addAll(new Separator(), boxGhep);
                }
            }
        }

        // --- 4. ACTION BUTTONS ---
        HBox actionBox = new HBox(10);
        actionBox.setAlignment(Pos.CENTER_RIGHT);
        actionBox.setPadding(new Insets(20, 25, 20, 25));
        actionBox.setStyle("-fx-background-color: #F7FAFC; -fx-background-radius: 0 0 15 15; -fx-border-color: #F1F5F9; -fx-border-width: 1 0 0 0;");

        switch (ban.getTrangThai()) {
            case DANG_SU_DUNG:
                actionBox.getChildren().addAll(
                        createStyledButton("Đổi bàn", "#D97706", "#FEF3C7", e -> {
                            xuLyDoiBan(ban);
                            dialog.close();
                        }),
                        createStyledButton("Thanh toán", "#059669", "#D1FAE5", e -> {
                            xuLyThanhToan(banAnClient.getMaHoaDonTuBan(ban.getMaBan()));
                            dialog.close();
                        }),
                        createStyledButton("Gọi món", "#7C3AED", "#EDE9FE", e -> {
                            xuLyGoiMon(ban);
                            dialog.close();
                        }),
                        createStyledButton("➕ Đặt bàn", "#2563EB", "#DBEAFE", e -> {
                            dialog.close(); 
                            List<BanAn> listBanChon = new ArrayList<>();
                            listBanChon.add(ban);
                            try {
                                Gui_DatBan guiDatBan = new Gui_DatBan(trangChu, listBanChon, datePicker.getValue());
                                trangChu.setMainContent(guiDatBan);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        })
                );
                break;
            case DA_DAT:
                actionBox.getChildren().addAll(
                        createStyledButton("Đổi bàn", "#D97706", "#FEF3C7", e -> {
                            xuLyDoiBan(ban);
                            dialog.close();
                        }),
                        createStyledButton("Hủy bàn", "#DC2626", "#FEE2E2", e -> {
                            xuLyHuyBan(ban);
                            dialog.close();
                        }),
                        createStyledButton("➕ Đặt bàn", "#2563EB", "#DBEAFE", e -> {
                            dialog.close(); 
                            List<BanAn> listBanChon = new ArrayList<>();
                            listBanChon.add(ban);
                            try {
                                Gui_DatBan guiDatBan = new Gui_DatBan(trangChu, listBanChon, datePicker.getValue());
                                trangChu.setMainContent(guiDatBan);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        })
                );
                break;
            case TRONG:
                // --- ĐẶT BÀN CHO BÀN TRỐNG ---
                // --- LUÔN CHO PHÉP ĐẶT BÀN CHO KHUNG GIỜ KHÁC ---
                actionBox.getChildren().add(
                        createStyledButton("➕ Đặt bàn", "#2563EB", "#DBEAFE", e -> {
                            dialog.close(); 
                            List<BanAn> listBanChon = new ArrayList<>();
                            listBanChon.add(ban);
                            try {
                                Gui_DatBan guiDatBan = new Gui_DatBan(trangChu, listBanChon, datePicker.getValue());
                                trangChu.setMainContent(guiDatBan);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        })
                );
                break;
        }

        VBox mainLayout = new VBox(headerPane, contentBox);
        if (!actionBox.getChildren().isEmpty()) mainLayout.getChildren().add(actionBox);

        mainLayout.setStyle("-fx-background-color: #F8FAFC; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 30, 0, 0, 10);");

        dialog.getDialogPane().setContent(mainLayout);
        dialog.getDialogPane().setStyle("-fx-background-color: transparent;");

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.getDialogPane().lookupButton(ButtonType.CLOSE).setVisible(false);

        dialog.showAndWait();
    }

    // Helper: Tạo hàng thông tin đẹp
    private HBox createDetailRow(String label, String value) {
        Label l = new Label(label);
        l.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        l.setTextFill(Color.web("#718096")); // Xám trung tính
        l.setMinWidth(100); // Căn lề thẳng hàng

        Label v = new Label(value);
        v.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        v.setTextFill(Color.web("#2D3748")); // Đen xám đậm

        return new HBox(10, l, v);
    }

    // Helper: Tạo nút bấm đẹp (Flat style)
    private Button createStyledButton(String text, String textColor, String bgColor, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Button btn = new Button(text);
        // Style mặc định
        String defaultStyle = "-fx-background-color: " + bgColor + "; -fx-text-fill: " + textColor + "; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 16; -fx-border-color: transparent;";
        // Style hover (đậm hơn chút)
        String hoverStyle = "-fx-background-color: " + textColor + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 16; -fx-border-color: transparent;";

        btn.setStyle(defaultStyle);

        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(defaultStyle));

        btn.setOnAction(handler);
        return btn;
    }

    private Label createLabel(String text, String color, int size, boolean bold) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", bold ? FontWeight.BOLD : FontWeight.NORMAL, size));
        l.setTextFill(Color.web(color));
        return l;
    }


    private void xuLyDatBan() {
        if (danhSachBanDaChon.isEmpty()) {
            showAlert(AlertType.ERROR, "Chưa chọn bàn", "Vui lòng click chọn ít nhất một bàn để đặt.");
            return;
        }
        // Cho phép đặt bàn kể cả khi trạng thái không trống (để đặt cho khung giờ khác)
        // Logic kiểm tra trùng giờ đã được xử lý ở PhieuDatBan_DAO
        try {
            LocalDate ngayDat = datePicker.getValue();
            Gui_DatBan guiDatBan = new Gui_DatBan(trangChu, danhSachBanDaChon, ngayDat);
            trangChu.setMainContent(guiDatBan);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void xuLyDoiBan(BanAn banCu) {
        String maHDGop = banAnClient.getMaHoaDonTuBan(banCu.getMaBan());
        List<String> dsBanGhep = (maHDGop != null) ? banAnClient.getDanhSachBanCungHoaDon(maHDGop) : new ArrayList<>();
        Window owner = this.getScene().getWindow();
        Gui_DoiBan dialog = new Gui_DoiBan(owner, banCu, ngayChon, banAnClient, phieuDatBanClient, dsBanGhep);
        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) loadDataToGrid();
        });
    }

    private void xuLyHuyBan(BanAn ban) {
        String maHDGop = banAnClient.getMaHoaDonTuBan(ban.getMaBan());
        List<String> dsBanGhep = (maHDGop != null) ? banAnClient.getDanhSachBanCungHoaDon(maHDGop) : new ArrayList<>();
        if (dsBanGhep.size() > 1) showDialogHuyBanGhep(ban, maHDGop, dsBanGhep);
        else thucHienHuyBanDon(ban);
    }

    private void thucHienHuyBanDon(BanAn ban) {
        Alert alert = new Alert(AlertType.CONFIRMATION, "Hủy đặt bàn " + ban.getMaBan() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                if (phieuDatBanClient.huyPhieuDatBanByMaBanVaNgay(ban.getMaBan(), ngayChon)) {
                    showAlert(AlertType.INFORMATION, "Thành công", "Đã hủy.");
                    loadDataToGrid();
                } else showAlert(AlertType.ERROR, "Lỗi", "Hủy thất bại.");
            }
        });
    }

    private void showDialogHuyBanGhep(BanAn ban, String maHDGop, List<String> dsBanGhep) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Hủy Bàn Ghép");
        dialog.setHeaderText("Hủy bàn " + ban.getMaBan() + " (Nhóm " + maHDGop + ")");
        VBox content = new VBox(15);
        content.setPadding(new Insets(15));
        content.getChildren().addAll(new Label("Bàn ghép: " + String.join(", ", dsBanGhep)), new Label("Hủy riêng hay hủy cả nhóm?"));
        ButtonType huyDonType = new ButtonType("Hủy riêng");
        ButtonType huyGhepType = new ButtonType("Hủy cả nhóm");
        dialog.getDialogPane().getButtonTypes().addAll(huyDonType, huyGhepType, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait().ifPresent(result -> {
            if (result == huyDonType) {
                thucHienHuyBanDon(ban);
                loadDataToGrid();
            } else if (result == huyGhepType) {
                thucHienHuyBanGhep(maHDGop, dsBanGhep);
                loadDataToGrid();
            }
        });
    }

    private void thucHienHuyBanGhep(String maHDGop, List<String> dsBanGhep) {
        Alert alert = new Alert(AlertType.CONFIRMATION, "Hủy nhóm " + dsBanGhep.size() + " bàn?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                if (phieuDatBanClient.huyTatCaPhieuByMaHoaDon(maHDGop)) {
                    showAlert(AlertType.INFORMATION, "Thành công", "Đã hủy nhóm.");
                    loadDataToGrid();
                } else showAlert(AlertType.ERROR, "Lỗi", "Hủy nhóm thất bại.");
            }
        });
    }

    private void xuLyCheckIn(PhieuDatBan pdb, Dialog<Void> dialog) {
        // 1. KIỂM TRA: Nếu bàn đang có khách (ĐANG_SU_DUNG), không cho check-in khách tiếp theo
        BanAn banHienTai = banAnClient.getByMaBan(pdb.getBan().getMaBan());
        if (banHienTai != null && banHienTai.getTrangThai() == TrangThai.DANG_SU_DUNG) {
            showAlert(AlertType.WARNING, "Bàn đang có khách", 
                "Bàn " + banHienTai.getMaBan() + " hiện đang có khách ngồi.\n" +
                "Vui lòng đợi khách cũ thanh toán hoặc thực hiện 'Đổi bàn' cho khách đặt này sang bàn trống khác.");
            return;
        }

        // 2. KIỂM TRA: Thứ tự check-in (Không cho khách sau check-in trước khách trước)
        List<PhieuDatBan> dsPhieuTrongNgay = phieuDatBanClient.getDanhSachPhieuDatBanByMaBanVaNgay(pdb.getBan().getMaBan(), pdb.getThoiGianBatDau().toLocalDate());
        for (PhieuDatBan p : dsPhieuTrongNgay) {
            // Nếu có phiếu nào sớm hơn khung giờ hiện tại mà vẫn đang "Đã đặt"
            if (p.getThoiGianBatDau().isBefore(pdb.getThoiGianBatDau()) && p.getTrangThai().equalsIgnoreCase("Đã đặt")) {
                showAlert(AlertType.WARNING, "Sai thứ tự check-in", 
                    "Vẫn còn khách đặt ở khung giờ sớm hơn (" + p.getThoiGianBatDau().getHour() + ":" + p.getThoiGianBatDau().getMinute() + ") chưa đến.\n" +
                    "Vui lòng xử lý (Check-in hoặc Hủy) cho khách đó trước khi cho khách sau vào.");
                return;
            }
        }

        KhachHang kh = pdb.getKhachHang();
        String tenKhach = (kh != null && kh.getTenKhachHang() != null) ? kh.getTenKhachHang() : "Khách hàng";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Check-in cho " + tenKhach + " (Khung giờ: " + pdb.getThoiGianBatDau().getHour() + ":" + pdb.getThoiGianBatDau().getMinute() + ")?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(res -> {
            if (res == ButtonType.YES) {
                if (!controlCheckIn.capNhatTrangThai(pdb.getMaPhieu(), "Đang dùng") || !controlCheckIn.capNhatTrangThaiBan(pdb.getBan().getMaBan(), TrangThai.DANG_SU_DUNG)) {
                    showAlert(AlertType.ERROR, "Lỗi", "Check-in thất bại.");
                    return;
                }
                showAlert(AlertType.INFORMATION, "Thành công", "Đã Check-in.");
                dialog.close();
                loadDataToGrid();
            }
        });
    }

    private void xuLyThanhToan(String maHD) {
        Gui_ThanhToan guiThanhToan = new Gui_ThanhToan(new NhanVien(), maHD);
        this.setTop(null);
        this.setBottom(null);
        this.setCenter(guiThanhToan);
        guiThanhToan.btnQuayLai.setOnAction(e -> {
            this.setTop(topContainer);
            this.setBottom(footer);
            ScrollPane scrollPane = createScrollableGrid();
            this.setCenter(scrollPane);
            loadDataToGrid();
        });

    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void xuLyGoiMon(BanAn ban) {
        // ============================================================
        // 1. LẤY MÃ HÓA ĐƠN & LOAD GIỎ HÀNG (GIỮ NGUYÊN)
        // ============================================================
        String maHD = banAnClient.getMaHoaDonTuBan(ban.getMaBan());
        if (maHD == null) {
            showAlert(AlertType.ERROR, "Lỗi", "Không tìm thấy hóa đơn của bàn: " + ban.getMaBan());
            return;
        }

        List<ChiTietHoaDon> dsCTHD = new ChiTietHoaDonClient().getChiTietHoaDonByMaHD(maHD);
        DecimalFormat df = new DecimalFormat("#,##0 VND");

        ObservableList<Object[]> gioHang = FXCollections.observableArrayList();
        for (ChiTietHoaDon ct : dsCTHD) {
            MonAn mon = ct.getMonAn();
            int slCu = ct.getSoLuong();
            double gia = mon.getGiaTien();
            gioHang.add(new Object[]{mon.getTenMonAn(), slCu, 0, slCu, df.format(slCu * gia), gia});
        }

        // ============================================================
        // 3. TẠO POPUP & OVERLAY (GIỮ NGUYÊN)
        // ============================================================
        Pane root = (Pane) this.getScene().getRoot();
        Rectangle overlay = new Rectangle();
        overlay.setFill(Color.rgb(0, 0, 0, 0.4));
        overlay.widthProperty().bind(root.widthProperty());
        overlay.heightProperty().bind(root.heightProperty());
        root.getChildren().add(overlay);

        Stage popup = new Stage();
        popup.initOwner(this.getScene().getWindow());
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Gọi món thêm - Bàn " + ban.getMaBan());
        popup.setResizable(false);

        // ============================================================
        // PANEL GIỎ HÀNG - PHẢI (GIỮ NGUYÊN)
        // ============================================================
        VBox panelGioHang = new VBox(15);
        panelGioHang.setPadding(new Insets(15));
        panelGioHang.setPrefWidth(520);
        panelGioHang.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 3);");

        Label lblGioHang = new Label("Gọi món ăn");
        lblGioHang.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));

        TilePane tileMenu = new TilePane(10, 12); // TilePane hiển thị menu
        TableView<Object[]> tbl = taoBangGioHang(gioHang, tileMenu);

        lblTongTien = new Label();
        lblTongTien.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        lblTongTien.setTextFill(Color.web("#e53e3e"));

        gioHang.addListener((ListChangeListener<Object[]>) c -> tinhTongTien(gioHang, lblTongTien));
        tinhTongTien(gioHang, lblTongTien);

        panelGioHang.getChildren().addAll(lblGioHang, tbl, lblTongTien);

        // ============================================================
        // PANEL MENU MÓN - TRÁI (CÓ SỬA ĐỔI HEADER & NAVBAR)
        // ============================================================
        VBox panelMenu = new VBox(10);
        panelMenu.setPrefWidth(560);
        panelMenu.setPadding(new Insets(15));
        panelMenu.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 8, 0, 0, 3);");

        // --- SỬA 1: Header ngang (Tiêu đề + Tìm kiếm) ---
        Label lblMenu = new Label("Chọn món ăn");
        lblMenu.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));

        TextField txtTim = new TextField();
        txtTim.setPromptText("🔍 Tìm món ăn...");
        txtTim.setPrefHeight(38);
        txtTim.setPrefWidth(250);
        txtTim.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #ced6e0; -fx-padding: 0 12; -fx-font-size: 13px;");

        // Layout Header
        HBox headerRow = new HBox(10, lblMenu, txtTim);
        headerRow.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(txtTim, Priority.ALWAYS); // Đẩy tìm kiếm giãn ra nếu cần hoặc dùng Spacer

        // Sự kiện lọc text cũ của bạn
        txtTim.textProperty().addListener((obs, oldVal, newVal) -> {
            String tuKhoa = newVal.trim();
            List<MonAn> ketQua;

            if (tuKhoa.isEmpty()) {
                // Nếu rỗng thì load lại tất cả (hoặc theo loại đang chọn nếu muốn logic phức tạp hơn)
                ketQua = monAnClient.getAllMonAn();
            } else {
                // GỌI HÀM SEARCH TỪ service (Bạn cần đảm bảo service có hàm này)
                ketQua = monAnClient.timKiemMonAn(tuKhoa);
            }

            hienThiDanhSachMon(ketQua, tileMenu, gioHang, df, tbl);
        });

        // --- SỬA 2: Navbar với Active & Hover ---
        FlowPane navLoai = new FlowPane();
        navLoai.setHgap(8);
        navLoai.setVgap(8);

        String[] dsLoai = {"Tất cả", "Món ăn kèm", "Món khai vị", "Món chính", "Nước sốt", "Đồ uống", "Tráng miệng"};
        List<Button> listBtnNavbar = new ArrayList<>(); // List lưu nút để xử lý active

        // Style mặc định
        String styleNormal = "-fx-background-color: white; -fx-border-color: #dcdde1; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 6 16; -fx-font-size: 13px; -fx-text-fill: #2f3640; -fx-cursor: hand;";
        // Style khi chọn (Active) hoặc Hover
        String styleActive = "-fx-background-color: #082744; -fx-border-color: #082744; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 6 16; -fx-font-size: 13px; -fx-text-fill: white; -fx-cursor: hand; -fx-font-weight: bold;";

        for (String loai : dsLoai) {
            Button btnLoai = new Button(loai);
            btnLoai.setWrapText(false);
            btnLoai.setStyle(styleNormal);
            listBtnNavbar.add(btnLoai);

            // Hover Logic
            btnLoai.setOnMouseEntered(e -> {
                // Chỉ đổi màu nếu nút này KHÔNG phải là nút đang chọn
                if (!btnLoai.getStyle().contains("-fx-font-weight: bold")) {
                    btnLoai.setStyle(styleActive.replace("-fx-font-weight: bold;", "")); // Hover thì ko cần bold cũng được
                }
            });
            btnLoai.setOnMouseExited(e -> {
                // Chuột rời đi: nếu không phải Active thì về Normal
                if (!btnLoai.getStyle().contains("-fx-font-weight: bold")) {
                    btnLoai.setStyle(styleNormal);
                }
            });

            // Click Logic (Active)
            btnLoai.setOnAction(e -> {
                // Reset style nút (giữ nguyên logic style cũ của bạn)
                for (Button b : listBtnNavbar) b.setStyle(styleNormal);
                btnLoai.setStyle(styleActive);

                // GỌI DATA TỪ service
                List<MonAn> dsTheoLoai;
                if (loai.equals("Tất cả")) {
                    dsTheoLoai = monAnClient.getAllMonAn();
                } else {
                    dsTheoLoai = monAnClient.getMonAnByLoai(loai);
                }

                hienThiDanhSachMon(dsTheoLoai, tileMenu, gioHang, df, tbl);
            });

            navLoai.getChildren().add(btnLoai);
        }
        // Set mặc định nút đầu tiên là Active
        if (!listBtnNavbar.isEmpty()) listBtnNavbar.get(0).setStyle(styleActive);

        // --- Load dữ liệu món ---
        hienThiDanhSachMon(monAnClient.getAllMonAn(), tileMenu, gioHang, df, tbl);

        // --- SỬA 3: Tích hợp Gợi ý tìm kiếm ---
        // Lấy dữ liệu từ TilePane vừa load để làm nguồn gợi ý
        ObservableList<String> dataGoiY = FXCollections.observableArrayList();
        for (Node n : tileMenu.getChildren()) {
            Object userData = n.getUserData();

            // Kiểm tra xem userData có phải là MonAn không để tránh lỗi
            if (userData instanceof MonAn) {
                MonAn mon = (MonAn) userData;
                // Chỉ lấy TÊN MÓN ĂN đưa vào danh sách gợi ý
                dataGoiY.add(mon.getTenMonAn());
            }
        }
        caiDatGoiYTimKiem(txtTim, dataGoiY);
        // ------------------------------------

        ScrollPane scMenu = new ScrollPane(tileMenu);
        scMenu.setFitToWidth(true);
        scMenu.setStyle("-fx-background-color: transparent;");

        // Gom Header và Navbar vào layout
        VBox topSection = new VBox(15, headerRow, navLoai);
        panelMenu.getChildren().addAll(topSection, scMenu);

        // ============================================================
        // BUTTONS & SCENE (GIỮ NGUYÊN)
        // ============================================================
        Button btnXN = new Button("Xác nhận");
        btnXN.setStyle("-fx-background-color:#082744; -fx-text-fill:white; -fx-background-radius:8; -fx-padding:10 25;");

        Button btnHuy = new Button("Hủy");
        btnHuy.setStyle("-fx-background-color:#dfe4ea; -fx-background-radius:8; -fx-padding:10 25;");
        btnHuy.setOnAction(e -> popup.close());

        btnXN.setOnAction(e -> {
            boolean coLoi = false;
            for (Object[] row : gioHang) {
                int slThem = (Integer) row[2];
                if (slThem <= 0) continue;
                String ten = (String) row[0];
                double gia = (Double) row[5];
                String maMon = monAnClient.getMaMonByTen(ten);
                if (!chiTietHoaDonClient.themHoacUpdate(maHD, maMon, slThem, gia)) {
                    coLoi = true;
                    showAlert(AlertType.ERROR, "Lỗi", "Không thể thêm món: " + ten);
                    break;
                }
            }
            if (!coLoi) {
                showAlert(AlertType.INFORMATION, "Thành công", "Đã gọi món cho bàn: " + ban.getMaBan());
                loadDataToGrid();
                popup.close();
            }
        });

        panelGioHang.getChildren().add(new HBox(10, btnHuy, btnXN));

        HBox main = new HBox(15, panelMenu, panelGioHang);
        main.setPadding(new Insets(10));
        Scene sc = new Scene(main, 1100, 520);
        // sc.getStylesheets().add(...) // Add css nếu cần
        popup.setScene(sc);
        popup.setOnHidden(ev -> root.getChildren().remove(overlay));
        popup.show();
    }

    private void caiDatGoiYTimKiem(TextField txtInput, ObservableList<String> dataNguon) {
        ContextMenu suggestionsPopup = new ContextMenu();
        suggestionsPopup.getStyleClass().add("goi-y-menu");
        suggestionsPopup.setPrefWidth(txtInput.getPrefWidth());

        Runnable hienThiGoiY = () -> {
            String tuKhoa = txtInput.getText().toLowerCase();

            // Nếu ô tìm kiếm trống, ẩn gợi ý
            if (tuKhoa.isEmpty()) {
                suggestionsPopup.hide();
                return;
            }

            List<MenuItem> suggestions = new ArrayList<>();

            for (String tenMon : dataNguon) {
                // Logic mới: So sánh trực tiếp tên món với từ khóa
                if (tenMon.toLowerCase().contains(tuKhoa)) {
                    MenuItem item = new MenuItem(tenMon);
                    item.getStyleClass().add("goi-y-item");

                    item.setOnAction(e -> {
                        txtInput.setText(tenMon);
                        txtInput.positionCaret(tenMon.length());
                        suggestionsPopup.hide();
                        // Gọi luôn logic tìm kiếm sau khi chọn gợi ý (nếu cần)
                        // hienThiDanhSachMon(monAnClient.timKiemMonAn(tenMon), tileMenu, gioHang, df, tbl);
                    });
                    suggestions.add(item);
                }
                if (suggestions.size() >= 10) break; // Giới hạn 10 gợi ý
            }

            if (!suggestions.isEmpty()) {
                suggestionsPopup.getItems().setAll(suggestions);
                if (!suggestionsPopup.isShowing()) {
                    suggestionsPopup.show(txtInput, Side.BOTTOM, 0, 0);
                }
            } else {
                suggestionsPopup.hide();
            }
        };

        txtInput.textProperty().addListener((observable, oldValue, newValue) -> hienThiGoiY.run());
        txtInput.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) suggestionsPopup.hide();
        });
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
    private void hienThiDanhSachMon(List<MonAn> dsMon, TilePane tileMenu, ObservableList<Object[]> gioHang, DecimalFormat df, TableView<Object[]> tbl) {
        tileMenu.getChildren().clear();

        if (dsMon == null || dsMon.isEmpty()) {
            Label lblTrong = new Label("Không tìm thấy món nào!");
            lblTrong.setStyle("-fx-font-style: italic; -fx-text-fill: grey; -fx-padding: 20;");
            tileMenu.getChildren().add(lblTrong);
            return;
        }

        for (MonAn mon : dsMon) {
            tileMenu.getChildren().add(taoTheMonAn(mon, gioHang, df, tbl));
        }
    }

private VBox taoTheMonAn(MonAn mon, ObservableList<Object[]> gioHang, DecimalFormat df, TableView<Object[]> table) {
    // Lấy thông tin từ Object MonAn
    String ten = mon.getTenMonAn();
    double gia = mon.getGiaTien();
    String anh = mon.getHinhAnh();

    // Lấy SL hiện tại từ giỏ hàng
    int slHienTai = getSLThemHienTai(ten, gioHang);

    VBox card = new VBox(8);
    card.setAlignment(Pos.TOP_CENTER);
    card.setPadding(new Insets(10));
    card.setPrefWidth(160);
    card.setStyle("-fx-background-color:white; -fx-background-radius:12; -fx-effect:dropshadow(gaussian, rgba(0,0,0,0.12),4,0,0,1);");

    // Xử lý ảnh
    ImageView img = new ImageView();
    img.setFitWidth(130);
    img.setFitHeight(90);
    img.setPreserveRatio(false);
    Rectangle clip = new Rectangle(130, 90);
    clip.setArcWidth(12); clip.setArcHeight(12);
    img.setClip(clip);
    loadImgTo(img, anh); // Hàm load ảnh giữ nguyên

    // Tên và Giá
    Label lblTen = new Label(ten);
    lblTen.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
    lblTen.setWrapText(true);
    lblTen.setAlignment(Pos.CENTER);

    Label lblGia = new Label(df.format(gia));
    lblGia.setTextFill(Color.web("#e53e3e"));
    lblGia.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));

    // Nút Tăng/Giảm
    Label lblSL = new Label(String.valueOf(slHienTai));
    lblSL.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
    lblSL.setPrefWidth(26);
    lblSL.setAlignment(Pos.CENTER);

    Button btnTru = createRoundButton("-");
    Button btnCong = createRoundButton("+");

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

    // Gắn UserData để sau này cần dùng lại object đỡ phải query
    card.setUserData(mon);

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
        try {
            imgView.setImage(new Image(getClass().getResourceAsStream("/img/default-food.png")));
        } catch (Exception e) {
            imgView.setStyle("-fx-background-color: #E2E8F0;");
        }
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
                } catch (Exception e) {
                    loadDefaultImage(imgView);
                }
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
                tf.setOnAction(e -> {
                    updateSLThem();
                });
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



}
