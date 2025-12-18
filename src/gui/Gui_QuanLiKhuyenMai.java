package gui;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ctrl.QLKM_Ctrl;
import entity.KhuyenMai;
import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.ImageCacheManager;

public class Gui_QuanLiKhuyenMai extends BorderPane {

    // --- CONTROLLER & DATA ---
    private QLKM_Ctrl control;
    ObservableList<String> dsTTMonAn = FXCollections.observableArrayList();
    ObservableList<String> dsMonChon = FXCollections.observableArrayList();
    ObservableList<String> dsKhuyenMai = FXCollections.observableArrayList();
    ObservableList<String> dsMonTemp = FXCollections.observableArrayList();
    ObservableList<String> dsLocMon = FXCollections.observableArrayList();

    // --- GUI COMPONENTS ---
    private TextField txtMaKhuyenMai;
    private TextField txtTenKhuyenMai;
    private TextField txtDieuKienApDung;
    private TextField txtGiaTriToiDa;
    private TextField txtGiaTriGiam;
    private TextField txtTimKiem;

    private ToggleGroup radioGroup1;
    private ToggleGroup radioGroup2;
    private RadioButton radioCo;
    private RadioButton radioKhong;
    private RadioButton radioHoaDon;
    private RadioButton radioMonAn;

    private TableView<String> tableDSKM;
    private TableView<String> tableDSMonKMTheoMa = new TableView<>(dsMonChon);
    private TableColumn<String, Void> colXoa = new TableColumn<>("Xóa");

    private DatePicker dateBatDau;
    private DatePicker dateKetThuc;

    private Button btnChonMonAn;
    private Button btnSuaLuu;
    private Button btnNgung;
    private Button btnReset;
    private Button btnThemKM;

    private ComboBox<String> cboTT;
    private ComboBox<String> cboLoai;
    private ComboBox<String> cboLocMon;

    private ListView<String> listMonAn;

    // --- FLAGS & HANDLERS ---
    private boolean flagSuaLuu = true; // True: Sửa, False: Lưu
    private boolean flagNgungXoa = true; // True: Ngừng, False: Xóa
    private boolean flagXemThem = false; // True: Thêm mới, False: Xem chi tiết

    private final EventHandler<MouseEvent> blockMouse = Event::consume;
    private final EventHandler<KeyEvent> blockKey = Event::consume;
    private final Map<KeyCombination, Runnable> shortcuts = new HashMap<>();

    // --- RESOURCES (FIXED CRASH HERE) ---
    // Hàm load ảnh an toàn: Nếu không tìm thấy ảnh thì trả về null chứ không crash
    private static Image safeLoadImage(String path) {
        try {
            java.io.InputStream stream = Gui_QuanLiKhuyenMai.class.getResourceAsStream(path);
            if (stream == null) {
                System.err.println("⚠️ CẢNH BÁO: Không tìm thấy file ảnh tại đường dẫn: " + path);
                return null;
            }
            return new Image(stream);
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tải ảnh: " + e.getMessage());
            return null;
        }
    }

    private static final Image IMG_MON_AN = safeLoadImage("/img/default-food.png");
    private static final Image IMG_CHU_NHAT_XANH = safeLoadImage("/img/chuNhatXanh.png");
    private static final Image IMG_CHU_NHAT_VANG = safeLoadImage("/img/chuNhatVang.png");

    public Gui_QuanLiKhuyenMai() {
        control = new QLKM_Ctrl();
        initializeUI();
    }

    private void initializeUI() {
        // Cấu hình chung
        this.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa 0%, #e9ecef 100%);");

        // Load CSS
        try {
            this.getStylesheets().add(getClass().getResource("/css/qlkm.css").toExternalForm());
        } catch (Exception e) {
            // CSS not found is fine, just log it
        }

        // Layout chính
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: transparent;");

        // Header (Top)
//        VBox header = createModernHeader();
//        this.setTop(header);

        // Content Body (Center - chia 2 cột)
        HBox body = new HBox(20);
        body.setPadding(new Insets(10, 30, 20, 30));

        // Phần Trái (Danh sách + Bộ lọc)
        VBox phanTrai = taoPhanBenTrai();
        HBox.setHgrow(phanTrai, Priority.ALWAYS); // Giãn ra

        // Phần Phải (Form thông tin)
        VBox phanPhai = taoPhanBenPhai();
        phanPhai.setPrefWidth(420); // Cố định chiều rộng form
        phanPhai.setMinWidth(420);
        phanPhai.setMaxWidth(420);

        body.getChildren().addAll(phanTrai, phanPhai);
        this.setCenter(body);

        // Animation Fade In
        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), this);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        setupShortcuts();
    }

    // ===== HEADER =====
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
//        Label title = new Label("QUẢN LÝ KHUYẾN MÃI");
//        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
//        title.setStyle("-fx-text-fill: white;");
//
//        Label subtitle = new Label("Quản lý các chương trình giảm giá, voucher và ưu đãi");
//        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
//        subtitle.setStyle("-fx-text-fill: rgba(255,255,255,0.8);");
//
//        header.getChildren().addAll(title, subtitle);
//
//        VBox headerContainer = new VBox(header);
//        headerContainer.setPadding(new Insets(20, 30, 0, 30));
//        return headerContainer;
//    }

    private void setupShortcuts() {
        KeyCombination f2 = new KeyCodeCombination(KeyCode.F2); // Thêm
        KeyCombination f3 = new KeyCodeCombination(KeyCode.F3); // Tìm
        KeyCombination f5 = new KeyCodeCombination(KeyCode.F5); // Reset
        KeyCombination f6 = new KeyCodeCombination(KeyCode.F6); // Lưu/Sửa
        KeyCombination f9 = new KeyCodeCombination(KeyCode.F9); // Xóa/Ngừng

        shortcuts.put(f3, () -> txtTimKiem.requestFocus());
        shortcuts.put(f2, () -> btnThemKM.fire());
        shortcuts.put(f5, () -> btnReset.fire());
        shortcuts.put(f6, () -> btnSuaLuu.fire());
        shortcuts.put(f9, () -> btnNgung.fire());

        this.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) newScene.getAccelerators().putAll(shortcuts);
            if (oldScene != null) oldScene.getAccelerators().clear();
        });
    }

    private void caiDatGoiYTimKiem(TextField txtInput, ObservableList<String> dataNguon) {
        ContextMenu suggestionsPopup = new ContextMenu();
        suggestionsPopup.getStyleClass().add("goi-y-menu");
        suggestionsPopup.setPrefWidth(txtInput.getPrefWidth());

        Runnable hienThiGoiY = () -> {
            String tuKhoa = txtInput.getText().toLowerCase();
            List<MenuItem> suggestions = new ArrayList<>();
            for (String row : dataNguon) {
                String[] parts = row.split(",");
                if (parts.length < 2) continue;
                String maKM = parts[0];
                String tenKM = parts[1];

                if (tuKhoa.isEmpty() || maKM.toLowerCase().contains(tuKhoa) || tenKM.toLowerCase().contains(tuKhoa)) {
                    MenuItem item = new MenuItem(maKM + " - " + tenKM);
                    item.getStyleClass().add("goi-y-item");
                    item.setOnAction(e -> {
                        txtInput.setText(maKM);
                        txtInput.positionCaret(maKM.length());
                        suggestionsPopup.hide();
                        locKhuyenMai();
                    });
                    suggestions.add(item);
                }
                if (suggestions.size() >= 10) break;
            }

            if (!suggestions.isEmpty()) {
                suggestionsPopup.getItems().setAll(suggestions);
                if (!suggestionsPopup.isShowing()) suggestionsPopup.show(txtInput, Side.BOTTOM, 0, 0);
            } else {
                suggestionsPopup.hide();
            }
        };

        txtInput.textProperty().addListener((observable, oldValue, newValue) -> hienThiGoiY.run());
        txtInput.setOnMouseClicked(event -> hienThiGoiY.run());
        txtInput.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) suggestionsPopup.hide();
        });
    }

    // ===== PHẦN TRÁI: BỘ LỌC VÀ DANH SÁCH =====
    public VBox taoPhanBenTrai() {
        VBox container = new VBox(15);

        // 1. Filter Bar
        HBox filterBar = new HBox(15);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(15));
        filterBar.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 1);"
        );

        // Search
        txtTimKiem = new TextField();
        txtTimKiem.setPromptText("🔍 Mã/Tên khuyến mãi...");
        txtTimKiem.setPrefHeight(35);
        txtTimKiem.setPrefWidth(200);
        txtTimKiem.setStyle("-fx-background-radius: 6; -fx-border-color: #e9ecef; -fx-border-radius: 6; -fx-padding: 4 8;");
        txtTimKiem.setTooltip(new Tooltip("F3: Tìm nhanh"));
        caiDatGoiYTimKiem(txtTimKiem, dsKhuyenMai);

        // Filter Type
        cboLoai = new ComboBox<>();
        cboLoai.getItems().addAll("Tất cả", "Hóa đơn", "Món ăn");
        cboLoai.setValue("Tất cả");
        styleComboBox(cboLoai);

        // Filter Status
        cboTT = new ComboBox<>();
        cboTT.getItems().addAll("Tất cả", "Sắp diễn ra", "Đang diễn ra", "Đã kết thúc");
        cboTT.setValue("Tất cả");
        styleComboBox(cboTT);

        // Buttons
        Button nutTimKiem = new Button("Tìm");
        styleButton(nutTimKiem, "#f1f3f5", "#495057");

        btnReset = new Button("↻");
        styleButton(btnReset, "#f1f3f5", "#495057");
        btnReset.setTooltip(new Tooltip("F5: Làm mới"));

        btnThemKM = new Button("✚ Thêm KM");
        styleButton(btnThemKM, "#082744", "white");
        btnThemKM.setTooltip(new Tooltip("F2: Thêm mới"));

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        filterBar.getChildren().addAll(txtTimKiem, cboLoai, cboTT, nutTimKiem, btnReset, spacer, btnThemKM);

        // Events
        nutTimKiem.setOnAction(e -> locKhuyenMai());
        txtTimKiem.setOnAction(e -> locKhuyenMai());
        cboLoai.setOnAction(e -> locKhuyenMai());
        cboTT.setOnAction(e -> locKhuyenMai());
        btnReset.setOnAction(e -> taoMoiTimKiem());
        btnThemKM.setOnAction(e -> {
            hanhDongThem();
            flagXemThem = true;
            btnSuaLuu.setDisable(false);
        });

        // 2. Table Section
        VBox tableContainer = taoDanhSachKhuyenMai();
        VBox.setVgrow(tableContainer, Priority.ALWAYS);

        container.getChildren().addAll(filterBar, tableContainer);
        return container;
    }

    public VBox taoDanhSachKhuyenMai() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(0));
        vbox.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 1);"
        );

        List<String> dsKhuyenMaiList = control.layDanhSachCTKM();
        dsKhuyenMai.clear();
        if (dsKhuyenMaiList != null) dsKhuyenMai.addAll(dsKhuyenMaiList);

        tableDSKM = new TableView<>(dsKhuyenMai);
        tableDSKM.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableDSKM.setStyle("-fx-background-color: white; -fx-base: white; -fx-border-color: transparent;");

        // Columns
        TableColumn<String, String> colMa = new TableColumn<>("Mã");
        colMa.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().split(",")[0]));

        TableColumn<String, String> colTen = new TableColumn<>("Tên Chương Trình");
        colTen.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().split(",")[1]));

        TableColumn<String, String> colLoai = new TableColumn<>("Loại");
        colLoai.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().split(",")[8].equals("1") ? "Món ăn" : "Hóa Đơn"));

        TableColumn<String, String> colGiam = new TableColumn<>("Mức Giảm");
        colGiam.setCellValueFactory(cell -> {
            boolean pt = cell.getValue().split(",")[6].equals("1");
            DecimalFormat dcm = new DecimalFormat("#,##0.##");
            String val = cell.getValue().split(",")[7];
            return new SimpleStringProperty(pt ? val + "%" : dcm.format(Double.parseDouble(val)) + " đ");
        });

        TableColumn<String, String> colTT = new TableColumn<>("Trạng Thái");
        colTT.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().split(",")[9]));
        colTT.setCellFactory(tc -> new TableCell<String, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Label lbl = new Label();
                    switch (item) {
                        case "1":
                            lbl.setText("Đang diễn ra");
                            lbl.setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold; -fx-background-color: #eafaf1; -fx-padding: 5 10; -fx-background-radius: 15;");
                            break;
                        case "0":
                            lbl.setText("Kết thúc");
                            lbl.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-background-color: #fdedec; -fx-padding: 5 10; -fx-background-radius: 15;");
                            break;
                        case "2":
                            lbl.setText("Sắp diễn ra");
                            lbl.setStyle("-fx-text-fill: #95a5a6; -fx-font-weight: bold; -fx-background-color: #f4f6f7; -fx-padding: 5 10; -fx-background-radius: 15;");
                            break;
                    }
                    setGraphic(lbl);
                    setAlignment(Pos.CENTER);
                    setText(null);
                }
            }
        });

        tableDSKM.getColumns().addAll(colMa, colTen, colLoai, colGiam, colTT);

        // Event Selection
        tableDSKM.setOnMouseClicked(event -> handleTableSelection());

        vbox.getChildren().add(tableDSKM);
        VBox.setVgrow(tableDSKM, Priority.ALWAYS);
        return vbox;
    }

    private void handleTableSelection() {
        flagXemThem = false;
        String chuoi = tableDSKM.getSelectionModel().getSelectedItem();
        if (chuoi == null) return;

        colXoa.setVisible(false);
        String[] parts = chuoi.split(",");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Parse Data
        String maKM = parts[0];
        String tenKM = parts[1];
        boolean isPercent = parts[6].equals("1");
        double valGiam = Double.parseDouble(parts[7]);
        double valMax = Double.parseDouble(parts[5]);
        boolean isMonAn = parts[8].equals("1");
        double dkApDung = Double.parseDouble(parts[4]);
        String trangThai = parts[9];
        LocalDate bd = LocalDate.parse(parts[2], dtf);
        LocalDate kt = LocalDate.parse(parts[3], dtf);

        // Fill Form
        txtMaKhuyenMai.setText(maKM);
        txtTenKhuyenMai.setText(tenKM);
        txtGiaTriGiam.setText(String.valueOf(valGiam));

        if (isPercent) {
            radioCo.setSelected(true);
            txtGiaTriToiDa.setText(String.valueOf(valMax));
        } else {
            radioKhong.setSelected(true);
        }

        dateBatDau.setValue(bd);
        dateKetThuc.setValue(kt);

        if (isMonAn) {
            radioMonAn.setSelected(true);
            dsMonChon = FXCollections.observableArrayList(control.layDSMonTheoMaKM(maKM));
            tableDSMonKMTheoMa.setItems(dsMonChon);
        } else {
            txtDieuKienApDung.setText(String.valueOf(dkApDung));
            radioHoaDon.setSelected(true);
        }

        btnChonMonAn.setDisable(true);
        if (txtTenKhuyenMai != null) moKhoaChinhSua(false);

        // Update Button State
        switch (trangThai) {
            case "1": // Đang diễn ra
                btnNgung.setDisable(false);
                btnNgung.setText("Ngừng KM");
                flagNgungXoa = true;

                btnSuaLuu.setText("Sửa");
                styleButton(btnSuaLuu, "#f39c12", "white"); // Cam
                btnSuaLuu.setDisable(true); // Không cho sửa khi đang chạy (logic cũ của bạn)
                break;
            case "2": // Sắp diễn ra
                btnSuaLuu.setText("Sửa");
                styleButton(btnSuaLuu, "#f39c12", "white");
                btnSuaLuu.setDisable(false);
                flagSuaLuu = true;

                btnNgung.setDisable(false);
                btnNgung.setText("Xóa");
                flagNgungXoa = false;
                break;
            default: // Kết thúc
                btnSuaLuu.setDisable(true);
                btnNgung.setDisable(true);
                btnNgung.setText("Ngừng KM");
                btnSuaLuu.setText("Sửa");
                break;
        }
    }

    // ===== PHẦN PHẢI: FORM THÔNG TIN =====
    public VBox taoPhanBenPhai() {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);"
        );

        Label title = new Label("THÔNG TIN CHI TIẾT");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setStyle("-fx-text-fill: #082744; -fx-border-width: 0 0 0 4; -fx-border-color: #082744; -fx-padding: 0 0 0 10;");

        // Form Fields
        txtMaKhuyenMai = createInputField(new TextField(), true);
        txtTenKhuyenMai = createInputField(new TextField(), false);

        // Radio Groups
        radioCo = new RadioButton("Có");
        radioKhong = new RadioButton("Không");
        radioGroup1 = new ToggleGroup();
        radioCo.setToggleGroup(radioGroup1);
        radioKhong.setToggleGroup(radioGroup1);
        radioCo.setSelected(true);

        txtGiaTriGiam = createInputField(new TextField(), false);
        Label lblDonVi = new Label("%");

        txtGiaTriToiDa = createInputField(new TextField(), false);
        HBox rowMaxVal = createFormRow("Tối đa:", txtGiaTriToiDa, new Label("VND"));

        radioGroup1.selectedToggleProperty().addListener((o, oldV, newV) -> {
            if (newV == radioCo) {
                lblDonVi.setText("%");
                fadeIn(rowMaxVal);
            } else {
                lblDonVi.setText("VND");
                fadeOut(rowMaxVal);
            }
        });

        // Date Pickers
        dateBatDau = new DatePicker();
        dateKetThuc = new DatePicker();
        styleDatePicker(dateBatDau);
        styleDatePicker(dateKetThuc);

        dateBatDau.valueProperty().addListener((o, oldV, newV) -> {
            if (newV != null && dateKetThuc.getValue() != null) loadLaiDSMonAn(newV, dateKetThuc.getValue());
        });
        dateKetThuc.valueProperty().addListener((o, oldV, newV) -> {
            if (newV != null && dateBatDau.getValue() != null) loadLaiDSMonAn(dateBatDau.getValue(), newV);
        });

        // Type Logic
        radioHoaDon = new RadioButton("Hóa đơn");
        radioMonAn = new RadioButton("Món ăn");
        radioGroup2 = new ToggleGroup();
        radioHoaDon.setToggleGroup(radioGroup2);
        radioMonAn.setToggleGroup(radioGroup2);
        radioHoaDon.setSelected(true);

        txtDieuKienApDung = createInputField(new TextField(), false);
        HBox rowDieuKien = createFormRow("Điều kiện:", txtDieuKienApDung, new Label(">= VND"));

        // Button Select Item
        btnChonMonAn = new Button("Chọn món áp dụng");
        styleButton(btnChonMonAn, "#10ac84", "white");
        btnChonMonAn.setMaxWidth(Double.MAX_VALUE);

        // Table Selected Items (Mini)
        tableDSMonKMTheoMa = new TableView<>(dsMonChon);
        setupMiniTable();
        VBox boxMonAn = new VBox(10, btnChonMonAn, tableDSMonKMTheoMa);

        radioGroup2.selectedToggleProperty().addListener((o, oldV, newV) -> {
            if (newV == radioHoaDon) {
                fadeIn(rowDieuKien);
                fadeOut(boxMonAn);
                radioCo.setDisable(false);
            } else {
                fadeOut(rowDieuKien);
                fadeIn(boxMonAn);
                radioKhong.setSelected(true);
                radioCo.setDisable(true);
            }
        });

        // Initial State
        boxMonAn.setVisible(false);
        boxMonAn.setManaged(false);

        // Layout Form
        card.getChildren().addAll(
                title,
                createFormRow("Mã KM:", txtMaKhuyenMai, null),
                createFormRow("Tên KM:", txtTenKhuyenMai, null),
                new Label("Giảm theo % ?"),
                new HBox(20, radioCo, radioKhong),
                createFormRow("Giá trị:", txtGiaTriGiam, lblDonVi),
                rowMaxVal,
                createFormRow("Từ ngày:", dateBatDau, null),
                createFormRow("Đến ngày:", dateKetThuc, null),
                new Label("Áp dụng cho:"),
                new HBox(20, radioHoaDon, radioMonAn),
                rowDieuKien,
                boxMonAn
        );

        // Action Buttons
        btnSuaLuu = new Button("Sửa");
        styleButton(btnSuaLuu, "#f39c12", "white"); // Orange
        btnSuaLuu.setDisable(true);

        btnNgung = new Button("Ngừng KM");
        styleButton(btnNgung, "#e74c3c", "white"); // Red
        btnNgung.setDisable(true);

        HBox actions = new HBox(10, btnSuaLuu, btnNgung);
        actions.setAlignment(Pos.CENTER_RIGHT);

        // Spacer to push buttons to bottom
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        card.getChildren().addAll(spacer, new Separator(), actions);

        // Logic Buttons
        btnNgung.setOnAction(e -> {
            if (flagNgungXoa) hanhDongNgungKM();
            else hanhDongXoaKM();
        });

        btnSuaLuu.setOnAction(e -> {
            if (flagSuaLuu) { // Chuyển sang chế độ Sửa -> Lưu
                if (hanhDongSua()) {
                    btnSuaLuu.setText("Lưu");
                    styleButton(btnSuaLuu, "#082744", "white"); // Blue
                    colXoa.setVisible(true);
                    flagSuaLuu = false;
                    btnChonMonAn.setDisable(false);
                    moKhoaChinhSua(true);
                }
            } else { // Chuyển sang chế độ Lưu -> Sửa
                if (hanhDongLuu()) {
                    btnSuaLuu.setText("Sửa");
                    styleButton(btnSuaLuu, "#f39c12", "white"); // Orange
                    flagSuaLuu = true;
                    colXoa.setVisible(false);
                    clear();
                    moKhoaChinhSua(false);
                }
            }
        });

        // Modal Logic for btnChonMonAn
        btnChonMonAn.setOnAction(e -> moModalChonMon());

        return card;
    }

    private void setupMiniTable() {
        TableColumn<String, String> c1 = new TableColumn<>("Mã");
        c1.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().split("-")[0]));
        TableColumn<String, String> c2 = new TableColumn<>("Tên");
        c2.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().split("-")[1]));
        TableColumn<String, String> c3 = new TableColumn<>("Giá KM");
        c3.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().split("-")[6]));

        // Column Xoa
        colXoa.setCellFactory(tc -> new TableCell<String, Void>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    Button btnDel = new Button("🗑");
                    btnDel.setStyle("-fx-background-color: transparent; -fx-text-fill: red; -fx-cursor: hand;");
                    btnDel.setOnAction(e -> {
                        String monAn = getTableView().getItems().get(getIndex());
                        // Confirmation Logic
                        Alert alert = new Alert(AlertType.CONFIRMATION, "Xóa món này khỏi KM?", ButtonType.YES, ButtonType.NO);
                        if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                            String[] parts = monAn.split("-");
                            // Revert object string logic
                            String revert = parts[0] + "-" + parts[1] + "-" + parts[2] + "-" + parts[3] + "-" + parts[4] + "-" + parts[5] + "-" + parts[3] + "-" + parts[7] + "-" + parts[8];
                            dsTTMonAn.add(revert);
                            getTableView().getItems().remove(getIndex());
                            dsMonChon.remove(monAn);
                        }
                    });
                    setGraphic(btnDel);
                    setAlignment(Pos.CENTER);
                }
            }
        });

        tableDSMonKMTheoMa.getColumns().addAll(c1, c2, c3, colXoa);
        tableDSMonKMTheoMa.setPrefHeight(150);
        tableDSMonKMTheoMa.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colXoa.setVisible(false);
    }

    // ===== MODAL CHỌN MÓN =====
    private void moModalChonMon() {
        // Validation Logic
        String valGiam = txtGiaTriGiam.getText();
        if (valGiam.isEmpty()) {
            showAlert(AlertType.WARNING, "Lỗi", "Nhập giá trị giảm trước!");
            return;
        }
        double dGiam = 0;
        try {
            dGiam = Double.parseDouble(valGiam);
        } catch (Exception e) {
            showAlert(AlertType.WARNING, "Lỗi", "Giá trị giảm sai!");
            return;
        }
        if (dGiam < 0) {
            showAlert(AlertType.WARNING, "Lỗi", "Giá trị giảm < 0");
            return;
        }
        if (dateBatDau.getValue() == null || dateKetThuc.getValue() == null) {
            showAlert(AlertType.WARNING, "Lỗi", "Chọn ngày áp dụng!");
            return;
        }

        if (dsTTMonAn.isEmpty()) loadLaiDSMonAn(dateBatDau.getValue(), dateKetThuc.getValue());

        // Setup Stage
        Stage stage = (Stage) btnChonMonAn.getScene().getWindow();
        BoxBlur blur = new BoxBlur(5, 5, 3);
        stage.getScene().getRoot().setEffect(blur);

        Stage modal = new Stage();
        modal.initModality(Modality.WINDOW_MODAL);
        modal.initOwner(stage);
        modal.setTitle("Chọn Món Ăn");

        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: white;");

        layout.setCenter(taoPhanBenTraiModal());
        layout.setRight(taoPhanBenPhaiModal(modal));

        Scene scene = new Scene(layout, 900, 600);
        try {
            scene.getStylesheets().add(getClass().getResource("/css/qlkm.css").toExternalForm());
        } catch (Exception e) {
        }
        modal.setScene(scene);
        modal.showAndWait();

        stage.getScene().getRoot().setEffect(null);
    }

    public VBox taoPhanBenTraiModal() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));

        Label title = new Label("Danh Sách Món Ăn");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));

        cboLocMon = new ComboBox<>();
        cboLocMon.getItems().addAll("Tất cả", "Đã giảm giá", "Chưa giảm giá");
        cboLocMon.setValue("Tất cả");
        cboLocMon.setOnAction(e -> locMonAn());

        listMonAn = new ListView<>(dsTTMonAn);
        listMonAn.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setGraphic(null);
                else setGraphic(taoMonBenTrai(item));
            }
        });

        // Prevent row selection interfering with button click
        listMonAn.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            Node target = (Node) e.getTarget();
            if (!(target instanceof Button || (target.getParent() instanceof Button))) {
                e.consume();
                listMonAn.getSelectionModel().clearSelection();
            }
        });

        box.getChildren().addAll(title, cboLocMon, listMonAn);
        return box;
    }

    public VBox taoPhanBenPhaiModal(Stage modal) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        box.setPrefWidth(350);
        box.setStyle("-fx-border-color: #ddd; -fx-border-width: 0 0 0 1;");

        Label title = new Label("Món Đã Chọn");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));

        ListView<String> listChon = new ListView<>(dsMonTemp);
        listChon.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setGraphic(null);
                else setGraphic(taoMonBenPhai(item));
            }
        });

        // Prevent selection
        listChon.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            Node target = (Node) e.getTarget();
            if (!(target instanceof Button || (target.getParent() instanceof Button))) e.consume();
        });

        Button btnBack = new Button("Quay về");
        styleButton(btnBack, "gray", "white");

        Button btnDone = new Button("Xong");
        styleButton(btnDone, "#2ecc71", "white");

        btnBack.setOnAction(e -> {
            modal.close();
            quayVe();
        });
        btnDone.setOnAction(e -> {
            modal.close();
            themMonChon();
        });

        HBox btns = new HBox(10, btnBack, btnDone);
        btns.setAlignment(Pos.CENTER_RIGHT);

        box.getChildren().addAll(title, listChon, new Region(), btns);
        VBox.setVgrow(listChon, Priority.ALWAYS);
        return box;
    }

    // Helper tạo UI từng dòng trong list modal
    public HBox taoMonBenTrai(String monAn) {
        HBox row = new HBox(10);
        row.setPadding(new Insets(5));
        row.setAlignment(Pos.CENTER_LEFT);

        ImageView img = new ImageView(IMG_MON_AN);

        // Load Image Logic (Simplified)
        if (monAn.split("-")[8] != null && !monAn.split("-")[8].isEmpty()) {
            String SUPABASE_BASE_URL = "https://yxemxycygkhxygaydgcl.supabase.co/storage/v1/object/public/image/";
            String imagePath = ImageCacheManager.getImagePath(SUPABASE_BASE_URL, monAn.split("-")[8]);
            if (imagePath != null) {
                try {
                    img.setImage(new Image(imagePath));
                } catch (Exception e) {
                }
            }
        }

        img.setFitWidth(60);
        img.setFitHeight(60);
        Rectangle clip = new Rectangle(60, 60);
        clip.setArcWidth(10);
        clip.setArcHeight(10);
        img.setClip(clip);

        VBox info = new VBox(2);
        Label name = new Label(monAn.split("-")[1]);
        name.setStyle("-fx-font-weight: bold;");
        Label price = new Label(new DecimalFormat("#,###").format(Double.parseDouble(monAn.split("-")[3])) + " đ");
        info.getChildren().addAll(name, price);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnAdd = new Button("Thêm");
        styleButton(btnAdd, "#082744", "white");

        // Check if item is already discounted elsewhere
        boolean isDiscounted = monAn.split("-")[5].equals("1");
        if (isDiscounted) {
            btnAdd.setDisable(true);
            btnAdd.setText("Đã KM");
            btnAdd.setStyle("-fx-background-color: #f1c40f; -fx-text-fill: black;");
        }

        btnAdd.setOnAction(e -> {
            dsTTMonAn.remove(monAn);
            if (!dsLocMon.isEmpty()) dsLocMon.remove(monAn);

            String[] parts = monAn.split("-");
            double giaSau = control.tinhGiaSauKM(Double.parseDouble(parts[3]), Double.parseDouble(txtGiaTriGiam.getText()));
            // Reconstruct string with new price
            String newItem = parts[0] + "-" + parts[1] + "-" + parts[2] + "-" + parts[3] + "-" + parts[4] + "-" + parts[5] + "-" + giaSau + "-" + parts[7] + "-" + parts[8];
            dsMonTemp.add(newItem);
        });

        row.getChildren().addAll(img, info, spacer, btnAdd);
        return row;
    }

    public HBox taoMonBenPhai(String monAn) {
        HBox row = new HBox(10);
        row.setPadding(new Insets(5));
        row.setAlignment(Pos.CENTER_LEFT);

        ImageView img = new ImageView(IMG_MON_AN);
        if (monAn.split("-")[8] != null && !monAn.split("-")[8].isEmpty()) {
            // ... same load logic ...
            String SUPABASE_BASE_URL = "https://yxemxycygkhxygaydgcl.supabase.co/storage/v1/object/public/image/";
            String imagePath = ImageCacheManager.getImagePath(SUPABASE_BASE_URL, monAn.split("-")[8]);
            if (imagePath != null) {
                try {
                    img.setImage(new Image(imagePath));
                } catch (Exception e) {
                }
            }
        }
        img.setFitWidth(40);
        img.setFitHeight(40);
        Rectangle clip = new Rectangle(40, 40);
        clip.setArcWidth(8);
        clip.setArcHeight(8);
        img.setClip(clip);

        VBox info = new VBox(2);
        Label name = new Label(monAn.split("-")[1]);
        name.setStyle("-fx-font-weight: bold; -fx-font-size: 11px;");
        Label price = new Label(new DecimalFormat("#,###").format(Double.parseDouble(monAn.split("-")[6])) + " đ");
        price.setStyle("-fx-text-fill: red; -fx-font-size: 11px;");
        info.getChildren().addAll(name, price);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnDel = new Button("X");
        btnDel.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 50;");

        btnDel.setOnAction(e -> {
            dsMonTemp.remove(monAn);
            String[] parts = monAn.split("-");
            // Revert string
            String oldItem = parts[0] + "-" + parts[1] + "-" + parts[2] + "-" + parts[3] + "-" + parts[4] + "-" + parts[5] + "-" + parts[3] + "-" + parts[7] + "-" + parts[8];
            dsTTMonAn.add(oldItem);
            if (!dsLocMon.isEmpty()) dsLocMon.add(oldItem);
        });

        row.getChildren().addAll(img, info, spacer, btnDel);
        return row;
    }


    // ===== LOGIC HELPERS =====

    private HBox createFormRow(String labelText, Node field, Node suffix) {
        Label lbl = new Label(labelText);
        lbl.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 12));
        lbl.setStyle("-fx-text-fill: #7f8c8d;");
        lbl.setPrefWidth(100);

        HBox row = new HBox(10, lbl, field);
        if (suffix != null) row.getChildren().add(suffix);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private void styleButton(Button btn, String bg, String text) {
        btn.setStyle(
                "-fx-background-color: " + bg + ";" +
                        "-fx-text-fill: " + text + ";" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 8 15;" +
                        "-fx-cursor: hand;"
        );
        addHoverEffect(btn);
    }

    private void addHoverEffect(Node node) {
        node.setOnMouseEntered(e -> node.setStyle(node.getStyle() + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);"));
        node.setOnMouseExited(e -> node.setEffect(null));
    }

    private void styleComboBox(ComboBox<?> cbo) {
        cbo.setStyle("-fx-background-color: white; -fx-border-color: #ced4da; -fx-border-radius: 5; -fx-padding: 2;");
        cbo.setPrefHeight(35);
    }

    private void styleDatePicker(DatePicker dp) {
        dp.setPrefHeight(35);
        dp.setStyle("-fx-font-size: 14px;");
    }

    private TextField createInputField(TextField textField, boolean isReadOnly) {
        textField.setEditable(!isReadOnly);
        textField.setPrefHeight(35);
        if (isReadOnly) {
            textField.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #ced4da; -fx-border-radius: 5; -fx-padding: 5; -fx-text-fill: #7f8c8d;");
        } else {
            textField.setStyle("-fx-background-color: white; -fx-border-color: #3498db; -fx-border-radius: 5; -fx-padding: 5;");
        }
        return textField;
    }

    private void moKhoaChinhSua(boolean bool) {
        txtTenKhuyenMai.setEditable(bool);
        radioCo.setDisable(!bool);
        radioKhong.setDisable(!bool);
        txtGiaTriGiam.setEditable(bool);
        txtGiaTriToiDa.setEditable(bool);
        radioMonAn.setDisable(!bool);
        radioHoaDon.setDisable(!bool);
        txtDieuKienApDung.setEditable(bool);

        if (bool) {
            dateBatDau.removeEventFilter(MouseEvent.ANY, blockMouse);
            dateBatDau.getEditor().removeEventFilter(KeyEvent.ANY, blockKey);
            dateKetThuc.removeEventFilter(MouseEvent.ANY, blockMouse);
            dateKetThuc.getEditor().removeEventFilter(KeyEvent.ANY, blockKey);
        } else {
            dateBatDau.addEventFilter(MouseEvent.ANY, blockMouse);
            dateBatDau.getEditor().addEventFilter(KeyEvent.ANY, blockKey);
            dateKetThuc.addEventFilter(MouseEvent.ANY, blockMouse);
            dateKetThuc.getEditor().addEventFilter(KeyEvent.ANY, blockKey);
        }

        styleFieldState(txtTenKhuyenMai, bool);
        styleFieldState(txtGiaTriGiam, bool);
        styleFieldState(txtGiaTriToiDa, bool);
        styleFieldState(txtDieuKienApDung, bool);
    }

    private void styleFieldState(TextField txt, boolean editable) {
        if (editable)
            txt.setStyle("-fx-background-color: white; -fx-border-color: #3498db; -fx-border-radius: 5; -fx-padding: 5;");
        else
            txt.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #ced4da; -fx-border-radius: 5; -fx-padding: 5; -fx-text-fill: #7f8c8d;");
    }

    // Animation
    private void fadeIn(Node node) {
        node.setVisible(true);
        node.setManaged(true);
        node.setOpacity(0.0);
        FadeTransition ft = new FadeTransition(Duration.millis(300), node);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    private void fadeOut(Node node) {
        FadeTransition ft = new FadeTransition(Duration.millis(300), node);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setOnFinished(e -> {
            node.setVisible(false);
            node.setManaged(false);
            node.setOpacity(1.0);
        });
        ft.play();
    }

    // === LOGIC FUNCTIONS (COPIED EXACTLY AS REQUESTED) ===

    public void clear() {
        txtMaKhuyenMai.setText("");
        txtTenKhuyenMai.setText("");
        txtGiaTriGiam.setText("");
        txtGiaTriToiDa.setText("");
        txtDieuKienApDung.setText("");
        dateBatDau.setValue(null);
        dateKetThuc.setValue(null);
        radioCo.setSelected(true);
        radioHoaDon.setSelected(true);
        btnChonMonAn.setDisable(false);
        dsTTMonAn.clear();
    }

    public boolean hanhDongThem() {
        clear();
        moKhoaChinhSua(true);
        String maKMMoi = control.taoMaKhuyenMaiMoi();
        txtMaKhuyenMai.setText(maKMMoi);
        txtTenKhuyenMai.requestFocus();
        btnNgung.setDisable(true);
        btnSuaLuu.setDisable(false);
        btnSuaLuu.setText("Lưu");
        styleButton(btnSuaLuu, "#082744", "white");
        flagSuaLuu = false;
        dsMonChon.clear();
        tableDSMonKMTheoMa.setItems(dsMonChon);
        dsTTMonAn.clear();
        return true;
    }

    public boolean hanhDongLuu() {
        // [YOUR ORIGINAL VALIDATION & SAVE LOGIC]
        String maKM = txtMaKhuyenMai.getText();
        String tenKhuyenMai = txtTenKhuyenMai.getText();
        if (tenKhuyenMai.isEmpty()) {
            showAlert(AlertType.WARNING, "Tên rỗng", "Vui lòng nhập tên khuyến mãi đầy đủ!");
            return false;
        }

        String giaTriGiamString = txtGiaTriGiam.getText();
        if (giaTriGiamString.isEmpty()) {
            showAlert(AlertType.WARNING, "Giá trị giảm rỗng", "Vui lòng nhập giá trị giảm hợp lệ!");
            return false;
        }
        double giaTriGiam = 0.0;
        try {
            giaTriGiam = Double.parseDouble(giaTriGiamString);
        } catch (Exception e) {
            showAlert(AlertType.WARNING, "Lỗi", "Giá trị giảm là số");
            return false;
        }

        double giaTriToiDa = 0.0;
        if (radioCo.isSelected()) {
            if (giaTriGiam < 0 || giaTriGiam > 100.0) {
                showAlert(AlertType.WARNING, "Lỗi", "Giá trị giảm >= 0 và <= 100 !");
                return false;
            }
            try {
                giaTriToiDa = Double.parseDouble(txtGiaTriToiDa.getText());
            } catch (Exception e) {
                showAlert(AlertType.WARNING, "Lỗi", "Tối đa là số");
                return false;
            }
        } else {
            if (giaTriGiam < 0) {
                showAlert(AlertType.WARNING, "Lỗi", "Giá trị giảm >= 0 !");
                return false;
            }
        }

        double dieuKienApDung = 0.0;
        if (radioHoaDon.isSelected()) {
            try {
                dieuKienApDung = Double.parseDouble(txtDieuKienApDung.getText());
            } catch (Exception e) {
                showAlert(AlertType.WARNING, "Lỗi", "ĐK là số");
                return false;
            }
        }

        LocalDate ngayBD = dateBatDau.getValue();
        LocalDate ngayKT = dateKetThuc.getValue();
        if (ngayBD == null || ngayKT == null) {
            showAlert(AlertType.WARNING, "Lỗi", "Chọn ngày!");
            return false;
        }
        if (ngayBD.isBefore(LocalDate.now())) {
            showAlert(AlertType.WARNING, "Lỗi", "Ngày BĐ không trước hôm nay");
            return false;
        }
        if (ngayKT.isBefore(ngayBD)) {
            showAlert(AlertType.WARNING, "Lỗi", "Ngày KT >= Ngày BĐ");
            return false;
        }

        if (radioMonAn.isSelected() && dsMonChon.isEmpty()) {
            showAlert(AlertType.WARNING, "Lỗi", "Chọn món!");
            return false;
        }

        KhuyenMai km = new KhuyenMai();
        km.setMaKhuyenMai(maKM);
        km.setTenKhuyenMai(tenKhuyenMai);
        km.setGiamGiaPhanTram(radioCo.isSelected());
        km.setGiaTriToiDa(radioCo.isSelected() ? giaTriToiDa : 0.0);
        km.setGiaTriGiam(giaTriGiam);
        km.setNgayBatDau(ngayBD);
        km.setNgayKetThuc(ngayKT);

        if (radioHoaDon.isSelected()) {
            km.setDieuKienApDung(dieuKienApDung);
            if (flagXemThem) {
                if (control.themKhuyenMai(km)) {
                    showAlert(AlertType.INFORMATION, "OK", "Thêm thành công");
                    loadLaiDanhSachKM();
                    return true;
                }
            } else {
                if (control.suaKhuyenMai(km, dsMonChon, 0)) {
                    showAlert(AlertType.INFORMATION, "OK", "Sửa thành công");
                    loadLaiDanhSachKM();
                    return true;
                }
            }
        } else {
            km.setDieuKienApDung(0.0);
            if (flagXemThem) {
                if (control.themKhuyenMai(km) && control.themDSCTKMMonAn(dsMonChon, km)) {
                    showAlert(AlertType.INFORMATION, "OK", "Thêm thành công");
                    loadLaiDanhSachKM();
                    return true;
                }
            } else {
                if (control.suaKhuyenMai(km, dsMonChon, 1)) {
                    showAlert(AlertType.INFORMATION, "OK", "Sửa thành công");
                    loadLaiDanhSachKM();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hanhDongSua() {
        return true;
    }

    public void hanhDongNgungKM() {
        String maKM = txtMaKhuyenMai.getText();
        if (maKM.isEmpty()) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Ngừng KM " + maKM + "?", ButtonType.YES, ButtonType.NO);
        if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            if (control.ngungKhuyenMai(maKM)) {
                showAlert(Alert.AlertType.INFORMATION, "OK", "Đã ngừng KM");
                clear();
                loadLaiDanhSachKM();
            }
        }
    }

    public void hanhDongXoaKM() {
        String maKM = txtMaKhuyenMai.getText();
        if (maKM.isEmpty()) return;
        int loai = radioMonAn.isSelected() ? 1 : 0;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Xóa KM " + maKM + "?", ButtonType.YES, ButtonType.NO);
        if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            if (control.xoaKhuyenMai(maKM, loai)) {
                showAlert(Alert.AlertType.INFORMATION, "OK", "Đã xóa KM");
                clear();
                loadLaiDanhSachKM();
            }
        }
    }

    public void locKhuyenMai() {
        String tuKhoa = txtTimKiem.getText().trim();
        String trangThai = cboTT.getValue();
        String loai = cboLoai.getValue();
        LocalDate today = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        List<String> ketQua = dsKhuyenMai.stream().filter(km -> {
            boolean matchTimKiem = tuKhoa.isEmpty() || km.split(",")[0].equals(tuKhoa);

            boolean matchTrangThai = true;
            LocalDate bd = LocalDate.parse(km.split(",")[2], dtf);
            LocalDate kt = LocalDate.parse(km.split(",")[3], dtf);
            switch (trangThai) {
                case "Sắp diễn ra":
                    matchTrangThai = bd.isAfter(today);
                    break;
                case "Đang diễn ra":
                    matchTrangThai = (!bd.isAfter(today)) && (!kt.isBefore(today));
                    break;
                case "Đã kết thúc":
                    matchTrangThai = kt.isBefore(today);
                    break;
            }

            boolean matchLoai = true;
            switch (loai) {
                case "Hóa đơn":
                    matchLoai = km.split(",")[8].equals("0");
                    break;
                case "Món ăn":
                    matchLoai = km.split(",")[8].equals("1");
                    break;
            }
            return matchTimKiem && matchTrangThai && matchLoai;
        }).toList();

        clear();
        tableDSKM.setItems(FXCollections.observableArrayList(ketQua));
    }

    // Other Helpers
    public void loadLaiDanhSachKM() {
        List<String> newData = control.layDanhSachCTKM();
        dsKhuyenMai.clear();
        if (newData != null) dsKhuyenMai.addAll(newData);
        tableDSKM.refresh();
    }

    public void loadLaiDSMonAn(LocalDate bd, LocalDate kt) {
        dsTTMonAn = FXCollections.observableArrayList(control.layDanhSachMonAnCTKM(bd, kt));
        dsMonChon.clear();
        tableDSMonKMTheoMa.setItems(dsMonChon);
    }

    public void quayVe() {
        for (String m : dsMonTemp) {
            String[] t = m.split("-");
            String r = t[0] + "-" + t[1] + "-" + t[2] + "-" + t[3] + "-" + t[4] + "-" + t[5] + "-" + t[3] + "-" + t[7];
            dsTTMonAn.add(r);
        }
        dsMonTemp.clear();
    }

    public void themMonChon() {
        dsMonChon.addAll(dsMonTemp);
        dsMonTemp.clear();
    }

    public void taoMoiTimKiem() {
        clear();
        txtTimKiem.clear();
        cboLoai.setValue("Tất cả");
        cboTT.setValue("Tất cả");
        locKhuyenMai();
        loadLaiDanhSachKM();
    }

    public void locMonAn() {
        dsLocMon.clear();
        if (cboLocMon.getValue().equals("Tất cả")) {
            listMonAn.setItems(dsTTMonAn);
            return;
        }
        String c = cboLocMon.getValue().equals("Đã giảm giá") ? "1" : "0";
        for (String i : dsTTMonAn) if (i.split("-")[5].equals(c)) dsLocMon.add(i);
        listMonAn.setItems(dsLocMon);
    }

    public void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}