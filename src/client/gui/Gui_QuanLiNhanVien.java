//
//package client.gui;
//
//import client.service.NhanVienClient;
//import common.entity.NhanVien;
//import javafx.animation.FadeTransition;
//import javafx.application.Platform;
//import javafx.beans.property.ReadOnlyObjectWrapper;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.geometry.HPos;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Node;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.layout.*;
//import javafx.scene.text.Font;
//import javafx.scene.text.FontWeight;
//import javafx.util.Duration;
//import javafx.util.StringConverter;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.time.format.DateTimeParseException;
//import java.util.List;
//
//public class Gui_QuanLiNhanVien extends VBox {
//
//    private final NhanVienClient dao = new NhanVienClient();
//    private final ObservableList<NhanVien> data = FXCollections.observableArrayList();
//
//    private TableView<NhanVien> tableView;
//
//    // Form controls
//    private TextField txtMa, txtTen, txtSoDT, txtCCCD;
//    private ComboBox<String> cboChucVu;
//    private DatePicker dpNgaySinh, dpNgayVaoLam;
//    private Button btnThem, btnSua, btnThoiViec, btnTaiTuyen, btnClear;
//    private TextField txtNgayThoiViec;
//    private TextField txtSearch; // Declared here for access in handlers
//
//    // --- CSS CHO DATEPICKER GIỐNG GUI_DANHSACHBAN ---
//    private final String customDatePickerCss = "data:text/css," +
//            ".date-picker .arrow-button { -fx-background-color: transparent; -fx-cursor: hand; }" +
//            ".date-picker .arrow-button .arrow { -fx-background-color: #082744; }" +
//            ".date-picker-popup { -fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 5); }" +
//            ".date-picker-popup .month-year-pane { -fx-background-color: #082744; -fx-padding: 10; }" +
//            ".date-picker-popup .month-year-pane .label { -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; }" +
//            ".date-picker-popup .spinner .button { -fx-background-color: transparent; -fx-cursor: hand; }" +
//            ".date-picker-popup .spinner .button:hover { -fx-background-color: rgba(255, 255, 255, 0.2); -fx-background-radius: 50%25; }" +
//            ".date-picker-popup .spinner .button .left-arrow { -fx-background-color: white; -fx-scale-x: 0.8; -fx-scale-y: 0.8; }" +
//            ".date-picker-popup .spinner .button .right-arrow { -fx-background-color: white; -fx-scale-x: 0.8; -fx-scale-y: 0.8; }" +
//            ".date-picker-popup .day-cell { -fx-background-color: white; -fx-text-fill: #2D3748; -fx-font-size: 13px; -fx-border-color: transparent; }" +
//            ".date-picker-popup .day-cell:hover { -fx-background-color: #EBF8FF; -fx-text-fill: #082744; -fx-background-radius: 5; }" +
//            ".date-picker-popup .day-cell:selected { -fx-background-color: #082744; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; }" +
//            ".date-picker-popup .today { -fx-border-color: #E53E3E; -fx-border-radius: 5; -fx-border-width: 1; }";
//
//    public Gui_QuanLiNhanVien() {
//        initializeUI();
//        loadData();
//    }
//
//    private void initializeUI() {
//        // --- 1. SETUP ROOT STYLE ---
//        this.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa 0%, #e9ecef 100%);");
//        this.setPadding(new Insets(0, 40, 30, 40));
//
//        // Inject CSS vào Scene (VBox này)
//        this.getStylesheets().add(customDatePickerCss);
//
//        // --- 3. MAIN BODY (Split Pane) ---
//        HBox body = new HBox(25);
//        body.setPadding(new Insets(10, 40, 30, 40));
//        VBox.setVgrow(body, Priority.ALWAYS);
//
//        // Left Pane (Search + Table)
//        VBox leftPane = createLeftPaneModern();
//        HBox.setHgrow(leftPane, Priority.ALWAYS);
//
//        // Right Pane (Form + Actions)
//        VBox rightPane = createRightPaneModern();
//        rightPane.setPrefWidth(380);
//        rightPane.setMinWidth(380);
//        rightPane.setMaxWidth(380);
//
//        body.getChildren().addAll(leftPane, rightPane);
//
//        this.getChildren().addAll(body);
//
//        // --- 4. ANIMATION FADE IN ---
//        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), this);
//        fadeIn.setFromValue(0.0);
//        fadeIn.setToValue(1.0);
//        fadeIn.play();
//    }
//
//    // =============================================================
//    // ⬅️ LEFT PANE (SEARCH + TABLE)
//    // =============================================================
//    private VBox createLeftPaneModern() {
//        VBox container = new VBox(20);
//
//        // --- 1. Filter Bar (Card Style) ---
//        HBox filterBar = new HBox(15);
//        filterBar.setAlignment(Pos.CENTER_LEFT);
//        filterBar.setPadding(new Insets(20));
//        filterBar.setStyle(
//                "-fx-background-color: white;" +
//                        "-fx-background-radius: 12;" +
//                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);"
//        );
//
//        txtSearch = new TextField();
//        txtSearch.setPromptText("🔍 Tìm mã, tên hoặc SĐT...");
//        txtSearch.setPrefHeight(35);
//        styleInputField(txtSearch);
//        HBox.setHgrow(txtSearch, Priority.ALWAYS);
//
//        Button btnSearch = new Button("Tìm kiếm");
//        styleButton(btnSearch, "#f1f3f5", "#495057");
//
//        Button btnRefresh = new Button("🔄 Làm mới");
//        styleButton(btnRefresh, "#f1f3f5", "#495057");
//
//        btnSearch.setOnAction(e -> {
//            String kw = txtSearch.getText().trim();
//            if (kw.isEmpty()) {
//                loadData();
//            } else {
//                data.clear();
//                List<NhanVien> list = dao.searchNhanVien(kw);
//                data.addAll(list);
//                tableView.refresh();
//            }
//        });
//
//        txtSearch.setOnAction(e -> btnSearch.fire());
//
//        btnRefresh.setOnAction(e -> {
//            txtSearch.clear();
//            loadData();
//        });
//
//        filterBar.getChildren().addAll(txtSearch, btnSearch, btnRefresh);
//
//        // --- 2. Table Section (Card Style) ---
//        VBox tableContainer = new VBox();
//        tableContainer.setPadding(new Insets(5));
//        tableContainer.setStyle(
//                "-fx-background-color: white;" +
//                        "-fx-background-radius: 12;" +
//                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);"
//        );
//        VBox.setVgrow(tableContainer, Priority.ALWAYS);
//
//        tableView = new TableView<>();
//        tableView.setItems(data);
//        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//
//        tableView.setStyle(
//                "-fx-background-color: transparent;" +
//                        "-fx-base: white;" +
//                        "-fx-control-inner-background: white;" +
//                        "-fx-table-cell-border-color: transparent;" +
//                        "-fx-table-header-border-color: transparent;" +
//                        "-fx-padding: 5;"
//        );
//        VBox.setVgrow(tableView, Priority.ALWAYS);
//
//        // --- Table Columns ---
//        TableColumn<NhanVien, Number> colSTT = new TableColumn<>("STT");
//        colSTT.setCellValueFactory(col -> new ReadOnlyObjectWrapper<>(tableView.getItems().indexOf(col.getValue()) + 1));
//        colSTT.setMaxWidth(50);
//        colSTT.setStyle("-fx-alignment: CENTER; -fx-font-family: 'Segoe UI';");
//
//        TableColumn<NhanVien, String> colMa = new TableColumn<>("Mã NV");
//        colMa.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));
//        colMa.setStyle("-fx-alignment: CENTER_LEFT; -fx-font-family: 'Segoe UI';");
//
//        TableColumn<NhanVien, String> colTen = new TableColumn<>("Họ và Tên");
//        colTen.setCellValueFactory(new PropertyValueFactory<>("tenNhanVien"));
//        colTen.setStyle("-fx-font-weight: bold; -fx-alignment: CENTER_LEFT; -fx-font-family: 'Segoe UI';");
//
//        TableColumn<NhanVien, String> colChucVu = new TableColumn<>("Chức Vụ");
//        colChucVu.setCellValueFactory(new PropertyValueFactory<>("chucVu"));
//        colChucVu.setStyle("-fx-alignment: CENTER; -fx-font-family: 'Segoe UI';");
//
//        TableColumn<NhanVien, String> colSDT = new TableColumn<>("Số Điện Thoại");
//        colSDT.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
//        colSDT.setStyle("-fx-alignment: CENTER; -fx-font-family: 'Segoe UI';");
//
//        TableColumn<NhanVien, String> colCCCD = new TableColumn<>("CCCD");
//        colCCCD.setCellValueFactory(new PropertyValueFactory<>("CCCD"));
//        colCCCD.setStyle("-fx-alignment: CENTER; -fx-font-family: 'Segoe UI';");
//
//        TableColumn<NhanVien, String> colTT = new TableColumn<>("Trạng Thái");
//        colTT.setCellValueFactory(cell -> {
//            NhanVien nv = cell.getValue();
//            String s = nv.getNgayThoiViec() == null ? "Đang làm" : "Đã nghỉ";
//            return new ReadOnlyObjectWrapper<>(s);
//        });
//        colTT.setCellFactory(column -> new TableCell<>() {
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || item == null) {
//                    setText(null);
//                    setGraphic(null);
//                } else {
//                    Label lbl = new Label(item);
//                    lbl.setPadding(new Insets(4, 10, 4, 10));
//                    lbl.setStyle("-fx-font-weight: bold; -fx-background-radius: 12; -fx-font-family: 'Segoe UI'; -fx-font-size: 11px;");
//
//                    if ("Đang làm".equals(item)) {
//                        lbl.setStyle(lbl.getStyle() + "-fx-background-color: #d4edda; -fx-text-fill: #155724;"); // Green badge
//                    } else {
//                        lbl.setStyle(lbl.getStyle() + "-fx-background-color: #f8d7da; -fx-text-fill: #721c24;"); // Red badge
//                    }
//                    setGraphic(lbl);
//                    setAlignment(Pos.CENTER);
//                    setText(null);
//                }
//            }
//        });
//        colTT.setMaxWidth(120);
//
//        tableView.getColumns().addAll(colSTT, colMa, colTen, colChucVu, colSDT, colCCCD, colTT);
//
//        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> onTableSelectionChanged(newV));
//
//        tableContainer.getChildren().add(tableView);
//        container.getChildren().addAll(filterBar, tableContainer);
//
//        return container;
//    }
//
//    // =============================================================
//    // ➡️ RIGHT PANE (FORM + ACTIONS)
//    // =============================================================
//    private VBox createRightPaneModern() {
//        VBox card = new VBox(15);
//        card.setPadding(new Insets(25));
//        card.setStyle(
//                "-fx-background-color: white;" +
//                        "-fx-background-radius: 12;" +
//                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 12, 0, 0, 2);"
//        );
//
//        Label title = new Label("Thông tin chi tiết");
//        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
//        title.setStyle("-fx-text-fill: #082744; -fx-border-width: 0 0 0 4; -fx-border-color: #082744; -fx-padding: 0 0 0 10;");
//
//        GridPane form = new GridPane();
//        form.setHgap(15);
//        form.setVgap(15);
//        form.setPadding(new Insets(10, 0, 0, 0));
//
//        ColumnConstraints c1 = new ColumnConstraints();
//        c1.setPercentWidth(35);
//        c1.setHalignment(HPos.LEFT);
//
//        ColumnConstraints c2 = new ColumnConstraints();
//        c2.setPercentWidth(65);
//        c2.setHgrow(Priority.ALWAYS);
//
//        form.getColumnConstraints().addAll(c1, c2);
//
//        // --- Setup Form Controls ---
//        txtMa = new TextField();
//        txtMa.setEditable(false);
//        styleReadonlyField(txtMa);
//
//        txtTen = new TextField();
//        styleInputField(txtTen);
//
//        cboChucVu = new ComboBox<>();
//        cboChucVu.getItems().addAll("Nhân viên", "Quản lý");
//        styleComboBox(cboChucVu);
//
//        txtSoDT = new TextField();
//        styleInputField(txtSoDT);
//
//        txtCCCD = new TextField();
//        styleInputField(txtCCCD);
//
//        // --- CẤU HÌNH DATEPICKER MỚI ---
//        dpNgaySinh = new DatePicker();
//        configDatePicker(dpNgaySinh);
//
//        dpNgayVaoLam = new DatePicker();
//        configDatePicker(dpNgayVaoLam);
//
//        txtNgayThoiViec = new TextField();
//        txtNgayThoiViec.setEditable(false);
//        styleReadonlyField(txtNgayThoiViec);
//
//        // --- Add Rows ---
//        addFormRow(form, "Mã nhân viên:", txtMa, 0);
//        addFormRow(form, "Họ tên:", txtTen, 1);
//        addFormRow(form, "Chức vụ:", cboChucVu, 2);
//        addFormRow(form, "Số điện thoại:", txtSoDT, 3);
//        addFormRow(form, "CCCD:", txtCCCD, 4);
//        addFormRow(form, "Ngày sinh:", dpNgaySinh, 5);
//        addFormRow(form, "Ngày vào làm:", dpNgayVaoLam, 6);
//        addFormRow(form, "Ngày thôi việc:", txtNgayThoiViec, 7);
//
//        //Buttons
//        btnThem = new Button("Thêm");
//        styleButton(btnThem, "#10ac84", "white"); // Green
//        btnThem.setMaxWidth(Double.MAX_VALUE);
//
//        btnSua = new Button("Cập Nhật");
//        styleButton(btnSua, "#2e86de", "white"); // Blue
//        btnSua.setMaxWidth(Double.MAX_VALUE);
//
//        btnClear = new Button("Làm Mới");
//        styleButton(btnClear, "#95a5a6", "white"); // Gray
//        btnClear.setMaxWidth(Double.MAX_VALUE);
//
//        btnThoiViec = new Button("Thôi Việc");
//        styleButton(btnThoiViec, "#ee5253", "white"); // Red
//        btnThoiViec.setMaxWidth(Double.MAX_VALUE);
//
//        btnTaiTuyen = new Button("Tái Tuyển");
//        styleButton(btnTaiTuyen, "#feca57", "#222f3e"); // Yellow
//        btnTaiTuyen.setMaxWidth(Double.MAX_VALUE);
//
//        GridPane btnGrid = new GridPane();
//        btnGrid.setHgap(10);
//        btnGrid.setVgap(10);
//        btnGrid.setPadding(new Insets(20, 0, 0, 0));
//
//        btnGrid.add(btnThem, 0, 0);
//        btnGrid.add(btnSua, 1, 0);
//        btnGrid.add(btnClear, 2, 0);
//        btnGrid.add(btnThoiViec, 0, 1, 2, 1);
//        btnGrid.add(btnTaiTuyen, 2, 1);
//
//        ColumnConstraints btnCol = new ColumnConstraints();
//        btnCol.setPercentWidth(33.33);
//        btnGrid.getColumnConstraints().addAll(btnCol, btnCol, btnCol);
//
//        btnThem.setOnAction(e -> handleAdd());
//        btnSua.setOnAction(e -> handleUpdate());
//        btnThoiViec.setOnAction(e -> handleThoiViec());
//        btnTaiTuyen.setOnAction(e -> handleTaiTuyen());
//        btnClear.setOnAction(e -> clearForm());
//
//        Region spacer = new Region();
//        VBox.setVgrow(spacer, Priority.ALWAYS);
//
//        card.getChildren().addAll(title, form, btnGrid, spacer);
//        clearForm();
//
//        return card;
//    }
//
//    // =============================================================
//    // 🛠️ UI HELPERS
//    // =============================================================
//
//    /**
//     * Phương thức này áp dụng đầy đủ Style và Converter cho DatePicker
//     */
//    /**
//     * Cấu hình DatePicker nâng cao:
//     * - Cho phép nhập tay (Editable)
//     * - Tự động format dd/MM/yyyy
//     * - Xử lý lỗi khi nhập sai định dạng
//     */
//    private void configDatePicker(DatePicker dp) {
//        // 1. Cho phép nhập liệu trực tiếp
//        dp.setEditable(true);
//        dp.setPromptText("dd/MM/yyyy"); // Gợi ý định dạng
//
//        // 2. Converter: Chuyển đổi giữa Text và LocalDate
//        dp.setConverter(new StringConverter<LocalDate>() {
//            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//            @Override
//            public String toString(LocalDate date) {
//                if (date != null) {
//                    return dateFormatter.format(date);
//                } else {
//                    return "";
//                }
//            }
//
//            @Override
//            public LocalDate fromString(String string) {
//                if (string != null && !string.isEmpty()) {
//                    try {
//                        return LocalDate.parse(string, dateFormatter);
//                    } catch (DateTimeParseException e) {
//                        // Nếu nhập sai định dạng, trả về null (hoặc xử lý tùy ý)
//                        return null;
//                    }
//                }
//                return null;
//            }
//        });
//
//        // 3. Xử lý khi người dùng nhấn Enter hoặc focus out (Tự sửa lỗi nhập liệu)
//        dp.focusedProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue) { // Khi mất focus (click ra chỗ khác)
//                try {
//                    String text = dp.getEditor().getText();
//                    if (!text.isEmpty()) {
//                        // Cố gắng parse lại text hiện tại
//                        LocalDate date = dp.getConverter().fromString(text);
//                        dp.setValue(date);
//                    }
//                } catch (Exception e) {
//                    // Nếu sai định dạng -> Xóa trắng hoặc giữ nguyên tùy ý
//                    // dp.setValue(null);
//                }
//            }
//        });
//
//        // 4. Style giao diện (Giữ nguyên style đẹp cũ)
//        dp.setStyle(
//                "-fx-background-color: white;" +
//                        "-fx-border-color: #ced6e0;" +
//                        "-fx-border-radius: 5;" +
//                        "-fx-background-radius: 5;" +
//                        "-fx-font-family: 'Segoe UI';" +
//                        "-fx-font-size: 13px;"
//        );
//        dp.getEditor().setStyle("-fx-background-color: transparent;");
//        dp.setMaxWidth(Double.MAX_VALUE);
//        dp.setPrefHeight(35);
//    }
//
//    private void addFormRow(GridPane grid, String labelText, Node field, int row) {
//        Label lbl = new Label(labelText);
//        lbl.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
//        lbl.setStyle("-fx-text-fill: #535c68;");
//        grid.add(lbl, 0, row);
//        grid.add(field, 1, row);
//    }
//
//    private void styleInputField(TextField tf) {
//        tf.setStyle(
//                "-fx-background-color: white;" +
//                        "-fx-border-color: #ced6e0;" +
//                        "-fx-border-radius: 5;" +
//                        "-fx-background-radius: 5;" +
//                        "-fx-padding: 8;" +
//                        "-fx-font-family: 'Segoe UI';"
//        );
//        tf.setPrefHeight(35);
//    }
//
//    private void styleReadonlyField(TextField tf) {
//        tf.setStyle(
//                "-fx-background-color: #f1f2f6;" +
//                        "-fx-border-color: #dfe4ea;" +
//                        "-fx-border-radius: 5;" +
//                        "-fx-background-radius: 5;" +
//                        "-fx-padding: 8;" +
//                        "-fx-text-fill: #747d8c;" +
//                        "-fx-font-family: 'Segoe UI';"
//        );
//        tf.setPrefHeight(35);
//    }
//
//    private void styleComboBox(ComboBox<?> cbo) {
//        cbo.setStyle(
//                "-fx-background-color: white;" +
//                        "-fx-border-color: #ced6e0;" +
//                        "-fx-border-radius: 5;" +
//                        "-fx-background-radius: 5;" +
//                        "-fx-padding: 2;" +
//                        "-fx-font-family: 'Segoe UI';"
//        );
//        cbo.setMaxWidth(Double.MAX_VALUE);
//        cbo.setPrefHeight(35);
//    }
//
//    private void styleButton(Button btn, String bgColor, String textColor) {
//        btn.setStyle(
//                "-fx-background-color: " + bgColor + ";" +
//                        "-fx-text-fill: " + textColor + ";" +
//                        "-fx-font-weight: bold;" +
//                        "-fx-font-family: 'Segoe UI';" +
//                        "-fx-background-radius: 8;" +
//                        "-fx-padding: 8 15;" +
//                        "-fx-cursor: hand;" +
//                        "-fx-font-size: 13px;"
//        );
//        addHoverEffect(btn);
//    }
//
//    private void addHoverEffect(Node node) {
//        node.setOnMouseEntered(e -> {
//            node.setStyle(node.getStyle() + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1); -fx-scale-x: 1.03; -fx-scale-y: 1.03;");
//        });
//        node.setOnMouseExited(e -> {
//            node.setScaleX(1.0);
//            node.setScaleY(1.0);
//        });
//    }
//
//    // =============================================================
//    // ⚙️ LOGIC HANDLERS (UNCHANGED)
//    // =============================================================
//
//    private void onTableSelectionChanged(NhanVien nv) {
//        if (nv == null) {
//            clearForm();
//            return;
//        }
//
//        txtMa.setText(nv.getMaNhanVien());
//        txtTen.setText(nv.getTenNhanVien());
//        cboChucVu.setValue(nv.getChucVu());
//        txtSoDT.setText(nv.getSoDienThoai());
//        txtCCCD.setText(nv.getCCCD());
//        dpNgaySinh.setValue(nv.getNgaySinh());
//        dpNgayVaoLam.setValue(nv.getNgayVaoLam());
//
//        LocalDate ngayThoiViec = dao.getNgayThoiViec(nv.getMaNhanVien());
//        txtNgayThoiViec.setText(
//                ngayThoiViec != null
//                        ? ngayThoiViec.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
//                        : ""
//        );
//
//        btnThem.setDisable(true);
//        btnSua.setDisable(false);
//        btnThoiViec.setDisable(nv.getNgayThoiViec() != null);
//        btnTaiTuyen.setDisable(nv.getNgayThoiViec() == null);
//    }
//
//    private void handleAdd() {
//        String ten = txtTen.getText().trim();
//        String chucVu = cboChucVu.getValue() != null ? cboChucVu.getValue() : "";
//        String sdt = txtSoDT.getText().trim();
//        String cccd = txtCCCD.getText().trim();
//        LocalDate sinh = dpNgaySinh.getValue();
//        LocalDate vao = dpNgayVaoLam.getValue() != null ? dpNgayVaoLam.getValue() : LocalDate.now();
//
//        // Validate
//        if (ten.isEmpty() || chucVu.isEmpty() || sdt.isEmpty() || cccd.isEmpty() || sinh == null) {
//            showAlert(Alert.AlertType.ERROR, "Vui lòng nhập đầy đủ thông tin bắt buộc!");
//            return;
//        }
//
//        if (!ten.matches("^[\\p{L} ]+$")) {
//            showAlert(Alert.AlertType.ERROR, "Họ tên chỉ được chứa chữ cái.");
//            return;
//        }
//        if (sinh.isAfter(LocalDate.now().minusYears(18))) {
//            showAlert(Alert.AlertType.ERROR, "Nhân viên phải đủ 18 tuổi.");
//            return;
//        }
//        if (vao.isAfter(LocalDate.now())) {
//            showAlert(Alert.AlertType.ERROR, "Ngày vào làm không được vượt quá hiện tại.");
//            return;
//        }
//
//        if (!sdt.matches("\\d{10,11}")) {
//            showAlert(Alert.AlertType.ERROR, "Số điện thoại không đúng định dạng (10-11 chữ số).");
//            return;
//        }
//
//        if (!cccd.matches("\\d{12}")) {
//            showAlert(Alert.AlertType.ERROR, "CCCD phải đúng 12 chữ số.");
//            return;
//        }
//
//        String ma = dao.generateMaNhanVien();
//        NhanVien nv = new NhanVien(ma, ten, chucVu, cccd, sdt, sinh, vao, null);
//
//        boolean ok = dao.addNhanVien(nv);
//        if (ok) {
//            showAlert(Alert.AlertType.INFORMATION, "Thêm nhân viên thành công.");
//            loadData();
//            clearForm();
//        } else {
//            showAlert(Alert.AlertType.ERROR, "Thêm nhân viên thất bại.");
//        }
//    }
//
//    private void handleUpdate() {
//        String ma = txtMa.getText();
//        if (ma.isEmpty()) {
//            showAlert(Alert.AlertType.ERROR, "Vui lòng chọn nhân viên cần sửa.");
//            return;
//        }
//
//        String ten = txtTen.getText().trim();
//        String chucVu = cboChucVu.getValue();
//        String sdt = txtSoDT.getText().trim();
//        String cccd = txtCCCD.getText().trim();
//        LocalDate sinh = dpNgaySinh.getValue();
//        LocalDate vao = dpNgayVaoLam.getValue();
//
//        if (ten.isEmpty() || chucVu == null || sdt.isEmpty() || cccd.isEmpty() || sinh == null || vao == null) {
//            showAlert(Alert.AlertType.ERROR, "Vui lòng nhập đầy đủ thông tin!");
//            return;
//        }
//        if (!ten.matches("^[\\p{L} ]+$")) {
//            showAlert(Alert.AlertType.ERROR, "Họ tên chỉ được chứa chữ cái.");
//            return;
//        }
//        if (sinh.isAfter(LocalDate.now().minusYears(18))) {
//            showAlert(Alert.AlertType.ERROR, "Nhân viên phải đủ 18 tuổi.");
//            return;
//        }
//        if (vao.isAfter(LocalDate.now())) {
//            showAlert(Alert.AlertType.ERROR, "Ngày vào làm không được vượt quá hiện tại.");
//            return;
//        }
//
//        if (!sdt.matches("\\d{10,11}")) {
//            showAlert(Alert.AlertType.ERROR, "Số điện thoại không đúng định dạng (10-11 chữ số).");
//            return;
//        }
//
//        if (!cccd.matches("\\d{12}")) {
//            showAlert(Alert.AlertType.ERROR, "CCCD phải đúng 12 chữ số.");
//            return;
//        }
//
//        if (dao.isSoDienThoaiExistsForOther(sdt, ma)) {
//            showAlert(Alert.AlertType.ERROR, "Số điện thoại đã tồn tại cho nhân viên khác!");
//            return;
//        }
//        if (dao.isCCCDExistsForOther(cccd, ma)) {
//            showAlert(Alert.AlertType.ERROR, "CCCD đã tồn tại cho nhân viên khác!");
//            return;
//        }
//
//        NhanVien nv = dao.getNhanVienByMa(ma);
//        if (nv == null) {
//            showAlert(Alert.AlertType.ERROR, "Không tìm thấy nhân viên.");
//            return;
//        }
//
//        nv.setTenNhanVien(ten);
//        nv.setChucVu(chucVu);
//        nv.setSoDienThoai(sdt);
//        nv.setCCCD(cccd);
//        nv.setNgaySinh(sinh);
//        nv.setNgayVaoLam(vao);
//
//        boolean ok = dao.updateNhanVien(nv);
//        if (ok) {
//            showAlert(Alert.AlertType.INFORMATION, "Cập nhật thành công.");
//            loadData();
//            clearForm();
//        } else {
//            showAlert(Alert.AlertType.ERROR, "Cập nhật thất bại.");
//        }
//    }
//
//    private void handleThoiViec() {
//        NhanVien sel = tableView.getSelectionModel().getSelectedItem();
//        if (sel == null) {
//            showAlert(Alert.AlertType.ERROR, "Chọn nhân viên cần cho thôi việc.");
//            return;
//        }
//        if (sel.getNgayThoiViec() != null) {
//            showAlert(Alert.AlertType.INFORMATION, "Nhân viên đã nghỉ rồi.");
//            return;
//        }
//
//        boolean conf = confirmDialog("Bạn có chắc muốn cho nhân viên " + sel.getTenNhanVien() + " thôi việc?");
//        if (!conf) return;
//
//        boolean ok = dao.thoiViecNhanVien(sel.getMaNhanVien());
//        if (ok) {
//            showAlert(Alert.AlertType.INFORMATION, "Đã cho nghỉ.");
//            loadData();
//            clearForm();
//        } else {
//            showAlert(Alert.AlertType.ERROR, "Thao tác thất bại.");
//        }
//    }
//
//    private void handleTaiTuyen() {
//        NhanVien sel = tableView.getSelectionModel().getSelectedItem();
//        if (sel == null) {
//            showAlert(Alert.AlertType.ERROR, "Chọn nhân viên cần tái tuyển.");
//            return;
//        }
//        if (sel.getNgayThoiViec() == null) {
//            showAlert(Alert.AlertType.INFORMATION, "Nhân viên hiện đang làm việc.");
//            return;
//        }
//
//        boolean conf = confirmDialog("Bạn có chắc muốn tái tuyển nhân viên " + sel.getTenNhanVien() + " không?");
//        if (!conf) return;
//
//        boolean ok = dao.taiTuyenNhanVien(sel.getMaNhanVien());
//        if (ok) {
//            showAlert(Alert.AlertType.INFORMATION, "Đã tái tuyển.");
//            loadData();
//            clearForm();
//        } else {
//            showAlert(Alert.AlertType.ERROR, "Thao tác thất bại.");
//        }
//    }
//
//    private void loadData() {
//        Platform.runLater(() -> {
//            data.clear();
//            data.addAll(dao.getAllNhanVien());
//            tableView.refresh();
//        });
//    }
//
//    private void clearForm() {
//        txtMa.clear();
//        txtTen.clear();
//        cboChucVu.setValue(null);
//        txtSoDT.clear();
//        txtCCCD.clear();
//        dpNgaySinh.setValue(null);
//        dpNgayVaoLam.setValue(null);
//        txtNgayThoiViec.clear();
//
//        btnThem.setDisable(false);
//        btnSua.setDisable(true);
//        btnThoiViec.setDisable(true);
//        btnTaiTuyen.setDisable(true);
//
//        tableView.getSelectionModel().clearSelection();
//    }
//
//    private void showAlert(Alert.AlertType type, String msg) {
//        Alert a = new Alert(type);
//        a.setHeaderText(null);
//        a.setContentText(msg);
//        a.showAndWait();
//    }
//
//    private boolean confirmDialog(String msg) {
//        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
//        a.setHeaderText(null);
//        a.setContentText(msg);
//        return a.showAndWait().filter(btn -> btn == ButtonType.OK).isPresent();
//    }
//}
package client.gui;

import client.service.NhanVienClient;
import common.entity.NhanVien;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Gui_QuanLiNhanVien extends VBox {

    private final NhanVienClient dao = new NhanVienClient();
    private final ObservableList<NhanVien> data = FXCollections.observableArrayList();

    private TableView<NhanVien> tableView;

    // Form controls
    private TextField txtMa, txtTen, txtSoDT, txtCCCD;
    private ComboBox<String> cboChucVu;
    private DatePicker dpNgaySinh; // Chỉ giữ DatePicker cho ngày sinh
    private TextField txtNgayVaoLam; // Thay DatePicker bằng TextField Read-only
    private Button btnThem, btnSua, btnThoiViec, btnTaiTuyen, btnClear;
    private TextField txtNgayThoiViec;
    private TextField txtSearch;

    // Formatter chung
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // --- CSS --- (Giữ nguyên như cũ)
    private final String customDatePickerCss = "data:text/css," +
            ".date-picker .arrow-button { -fx-background-color: transparent; -fx-cursor: hand; }" +
            ".date-picker .arrow-button .arrow { -fx-background-color: #082744; }" +
            ".date-picker-popup { -fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 5); }" +
            ".date-picker-popup .month-year-pane { -fx-background-color: #082744; -fx-padding: 10; }" +
            ".date-picker-popup .month-year-pane .label { -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; }" +
            ".date-picker-popup .spinner .button { -fx-background-color: transparent; -fx-cursor: hand; }" +
            ".date-picker-popup .spinner .button:hover { -fx-background-color: rgba(255, 255, 255, 0.2); -fx-background-radius: 50%25; }" +
            ".date-picker-popup .spinner .button .left-arrow { -fx-background-color: white; -fx-scale-x: 0.8; -fx-scale-y: 0.8; }" +
            ".date-picker-popup .spinner .button .right-arrow { -fx-background-color: white; -fx-scale-x: 0.8; -fx-scale-y: 0.8; }" +
            ".date-picker-popup .day-cell { -fx-background-color: white; -fx-text-fill: #2D3748; -fx-font-size: 13px; -fx-border-color: transparent; }" +
            ".date-picker-popup .day-cell:hover { -fx-background-color: #EBF8FF; -fx-text-fill: #082744; -fx-background-radius: 5; }" +
            ".date-picker-popup .day-cell:selected { -fx-background-color: #082744; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; }" +
            ".date-picker-popup .today { -fx-border-color: #E53E3E; -fx-border-radius: 5; -fx-border-width: 1; }";

    public Gui_QuanLiNhanVien() {
        initializeUI();
        loadData();
    }

    private void initializeUI() {
        this.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa 0%, #e9ecef 100%);");
        this.setPadding(new Insets(0, 40, 30, 40));
        this.getStylesheets().add(customDatePickerCss);

        HBox body = new HBox(25);
        body.setPadding(new Insets(10, 40, 30, 40));
        VBox.setVgrow(body, Priority.ALWAYS);

        VBox leftPane = createLeftPaneModern();
        HBox.setHgrow(leftPane, Priority.ALWAYS);

        VBox rightPane = createRightPaneModern();
        rightPane.setPrefWidth(380);
        rightPane.setMinWidth(380);
        rightPane.setMaxWidth(380);

        body.getChildren().addAll(leftPane, rightPane);
        this.getChildren().addAll(body);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), this);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    // =============================================================
    // ⬅️ LEFT PANE (Giữ nguyên)
    // =============================================================
    private VBox createLeftPaneModern() {
        VBox container = new VBox(20);

        HBox filterBar = new HBox(15);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(20));
        filterBar.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);");

        txtSearch = new TextField();
        txtSearch.setPromptText("🔍 Tìm mã, tên hoặc SĐT...");
        styleInputField(txtSearch);
        HBox.setHgrow(txtSearch, Priority.ALWAYS);

        Button btnSearch = new Button("Tìm kiếm");
        styleButton(btnSearch, "#f1f3f5", "#495057");

        Button btnRefresh = new Button("🔄 Làm mới");
        styleButton(btnRefresh, "#f1f3f5", "#495057");

        btnSearch.setOnAction(e -> {
            String kw = txtSearch.getText().trim();
            if (kw.isEmpty()) loadData();
            else {
                data.clear();
                data.addAll(dao.searchNhanVien(kw));
                tableView.refresh();
            }
        });
        txtSearch.setOnAction(e -> btnSearch.fire());
        btnRefresh.setOnAction(e -> { txtSearch.clear(); loadData(); });

        filterBar.getChildren().addAll(txtSearch, btnSearch, btnRefresh);

        VBox tableContainer = new VBox();
        tableContainer.setPadding(new Insets(5));
        tableContainer.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);");
        VBox.setVgrow(tableContainer, Priority.ALWAYS);

        tableView = new TableView<>();
        tableView.setItems(data);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setStyle("-fx-background-color: transparent; -fx-base: white; -fx-control-inner-background: white;");
        VBox.setVgrow(tableView, Priority.ALWAYS);

        // Columns definition (Giữ nguyên logic cột)
        TableColumn<NhanVien, Number> colSTT = new TableColumn<>("STT");
        colSTT.setCellValueFactory(col -> new ReadOnlyObjectWrapper<>(tableView.getItems().indexOf(col.getValue()) + 1));
        colSTT.setMaxWidth(50);
        colSTT.setStyle("-fx-alignment: CENTER;");

        TableColumn<NhanVien, String> colMa = new TableColumn<>("Mã NV");
        colMa.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));

        TableColumn<NhanVien, String> colTen = new TableColumn<>("Họ và Tên");
        colTen.setCellValueFactory(new PropertyValueFactory<>("tenNhanVien"));
        colTen.setStyle("-fx-font-weight: bold; -fx-alignment: CENTER_LEFT;");

        TableColumn<NhanVien, String> colChucVu = new TableColumn<>("Chức Vụ");
        colChucVu.setCellValueFactory(new PropertyValueFactory<>("chucVu"));

        TableColumn<NhanVien, String> colSDT = new TableColumn<>("SĐT");
        colSDT.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
        colSDT.setStyle("-fx-alignment: CENTER;");

        TableColumn<NhanVien, String> colTT = new TableColumn<>("Trạng Thái");
        colTT.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getNgayThoiViec() == null ? "Đang làm" : "Đã nghỉ"));
        colTT.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Label lbl = new Label(item);
                    lbl.setPadding(new Insets(4, 10, 4, 10));
                    lbl.setStyle("-fx-font-weight: bold; -fx-background-radius: 12; -fx-font-size: 11px;");
                    if ("Đang làm".equals(item)) lbl.setStyle(lbl.getStyle() + "-fx-background-color: #d4edda; -fx-text-fill: #155724;");
                    else lbl.setStyle(lbl.getStyle() + "-fx-background-color: #f8d7da; -fx-text-fill: #721c24;");
                    setGraphic(lbl);
                    setAlignment(Pos.CENTER);
                }
            }
        });

        tableView.getColumns().addAll(colSTT, colMa, colTen, colChucVu, colSDT, colTT);
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> onTableSelectionChanged(newV));

        tableContainer.getChildren().add(tableView);
        container.getChildren().addAll(filterBar, tableContainer);
        return container;
    }

    // =============================================================
    // ➡️ RIGHT PANE (Đã sửa logic Ngày vào làm & Ngày sinh)
    // =============================================================
    private VBox createRightPaneModern() {
        VBox card = new VBox(15);
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 12, 0, 0, 2);");

        Label title = new Label("Thông tin chi tiết");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setStyle("-fx-text-fill: #082744; -fx-border-width: 0 0 0 4; -fx-border-color: #082744; -fx-padding: 0 0 0 10;");

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);

        ColumnConstraints c1 = new ColumnConstraints(); c1.setPercentWidth(35);
        ColumnConstraints c2 = new ColumnConstraints(); c2.setPercentWidth(65);
        form.getColumnConstraints().addAll(c1, c2);

        // --- Setup Controls ---
        txtMa = new TextField();
        txtMa.setEditable(false);
        styleReadonlyField(txtMa);

        txtTen = new TextField();
        styleInputField(txtTen);

        cboChucVu = new ComboBox<>();
        cboChucVu.getItems().addAll("Nhân viên", "Quản lý");
        styleComboBox(cboChucVu);

        txtSoDT = new TextField(); styleInputField(txtSoDT);
        txtCCCD = new TextField(); styleInputField(txtCCCD);

        // [SỬA] Cấu hình DatePicker cho phép nhập nhanh (gõ số tự thêm /)
        dpNgaySinh = new DatePicker();
        configDatePicker(dpNgaySinh);
        enableFastDateEntry(dpNgaySinh); // <--- LOGIC MỚI: Tự động format khi gõ

        // [SỬA] TextField Read-only thay cho DatePicker Ngày vào làm
        txtNgayVaoLam = new TextField();
        txtNgayVaoLam.setEditable(false);
        styleReadonlyField(txtNgayVaoLam);

        txtNgayThoiViec = new TextField();
        txtNgayThoiViec.setEditable(false);
        styleReadonlyField(txtNgayThoiViec);

        // Add Rows
        addFormRow(form, "Mã nhân viên:", txtMa, 0);
        addFormRow(form, "Họ tên:", txtTen, 1);
        addFormRow(form, "Chức vụ:", cboChucVu, 2);
        addFormRow(form, "Số điện thoại:", txtSoDT, 3);
        addFormRow(form, "CCCD:", txtCCCD, 4);
        addFormRow(form, "Ngày sinh:", dpNgaySinh, 5);
        addFormRow(form, "Ngày vào làm:", txtNgayVaoLam, 6); // Hiển thị Text
        addFormRow(form, "Ngày thôi việc:", txtNgayThoiViec, 7);

        // Buttons
        btnThem = new Button("Thêm"); styleButton(btnThem, "#10ac84", "white");
        btnSua = new Button("Cập Nhật"); styleButton(btnSua, "#2e86de", "white");
        btnClear = new Button("Làm Mới"); styleButton(btnClear, "#95a5a6", "white");
        btnThoiViec = new Button("Thôi Việc"); styleButton(btnThoiViec, "#ee5253", "white");
        btnTaiTuyen = new Button("Tái Tuyển"); styleButton(btnTaiTuyen, "#feca57", "#222f3e");

        GridPane btnGrid = new GridPane();
        btnGrid.setHgap(10); btnGrid.setVgap(10);
        btnGrid.setPadding(new Insets(20, 0, 0, 0));
        btnGrid.add(btnThem, 0, 0); btnGrid.add(btnSua, 1, 0); btnGrid.add(btnClear, 2, 0);
        btnGrid.add(btnThoiViec, 0, 1, 2, 1); btnGrid.add(btnTaiTuyen, 2, 1);

        ColumnConstraints btnCol = new ColumnConstraints(); btnCol.setPercentWidth(33.33);
        btnGrid.getColumnConstraints().addAll(btnCol, btnCol, btnCol);

        // Actions
        btnThem.setOnAction(e -> handleAdd());
        btnSua.setOnAction(e -> handleUpdate());
        btnThoiViec.setOnAction(e -> handleThoiViec());
        btnTaiTuyen.setOnAction(e -> handleTaiTuyen());
        btnClear.setOnAction(e -> clearForm());

        Region spacer = new Region(); VBox.setVgrow(spacer, Priority.ALWAYS);
        card.getChildren().addAll(title, form, btnGrid, spacer);

        clearForm(); // Khởi tạo form ban đầu
        return card;
    }

    // =============================================================
    // 🛠️ UI HELPERS & FAST DATE LOGIC
    // =============================================================

    /**
     * LOGIC MỚI: Cho phép gõ ngày tháng siêu nhanh.
     * Người dùng gõ "15051995" -> Tự động thành "15/05/1995"
     */
    private void enableFastDateEntry(DatePicker datePicker) {
        datePicker.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) return;

            // Chỉ cho phép nhập số và dấu /
            if (!newVal.matches("[0-9/]*")) {
                datePicker.getEditor().setText(oldVal);
                return;
            }

            // Logic tự động thêm dấu /
            // Nếu độ dài là 2 hoặc 5 và không phải đang xóa lùi -> thêm /
            if ((newVal.length() == 2 || newVal.length() == 5) && newVal.length() > oldVal.length()) {
                Platform.runLater(() -> {
                    datePicker.getEditor().setText(newVal + "/");
                    datePicker.getEditor().positionCaret(newVal.length() + 1);
                });
            }

            // Giới hạn độ dài chuỗi là 10 ký tự (dd/MM/yyyy)
            if (newVal.length() > 10) {
                datePicker.getEditor().setText(oldVal);
            }
        });
        datePicker.setPromptText("dd/MM/yyyy (Gõ số tự nhận)");
    }

    private void configDatePicker(DatePicker dp) {
        dp.setEditable(true);
        dp.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? DATE_FORMATTER.format(date) : "";
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    try {
                        return LocalDate.parse(string, DATE_FORMATTER);
                    } catch (DateTimeParseException e) { return null; }
                }
                return null;
            }
        });
        dp.setStyle("-fx-background-color: white; -fx-border-color: #ced6e0; -fx-border-radius: 5;");
        dp.setPrefHeight(35);
        dp.setMaxWidth(Double.MAX_VALUE);
    }

    private void addFormRow(GridPane grid, String labelText, Node field, int row) {
        Label lbl = new Label(labelText);
        lbl.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        lbl.setStyle("-fx-text-fill: #535c68;");
        grid.add(lbl, 0, row);
        grid.add(field, 1, row);
    }

    // Các hàm style giữ nguyên
    private void styleInputField(TextField tf) {
        tf.setStyle("-fx-background-color: white; -fx-border-color: #ced6e0; -fx-border-radius: 5; -fx-padding: 8;");
        tf.setPrefHeight(35);
    }
    private void styleReadonlyField(TextField tf) {
        tf.setStyle("-fx-background-color: #f1f2f6; -fx-border-color: #dfe4ea; -fx-border-radius: 5; -fx-text-fill: #747d8c; -fx-padding: 8;");
        tf.setPrefHeight(35);
    }
    private void styleComboBox(ComboBox<?> cbo) {
        cbo.setStyle("-fx-background-color: white; -fx-border-color: #ced6e0; -fx-border-radius: 5;");
        cbo.setMaxWidth(Double.MAX_VALUE);
        cbo.setPrefHeight(35);
    }
    private void styleButton(Button btn, String bg, String text) {
        btn.setStyle("-fx-background-color: "+bg+"; -fx-text-fill: "+text+"; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 15; -fx-cursor: hand;");
    }

    // =============================================================
    // ⚙️ LOGIC HANDLERS
    // =============================================================

    private void onTableSelectionChanged(NhanVien nv) {
        if (nv == null) { clearForm(); return; }

        txtMa.setText(nv.getMaNhanVien());
        txtTen.setText(nv.getTenNhanVien());
        cboChucVu.setValue(nv.getChucVu());
        txtSoDT.setText(nv.getSoDienThoai());
        txtCCCD.setText(nv.getCCCD());
        dpNgaySinh.setValue(nv.getNgaySinh());

        // Hiển thị ngày vào làm từ dữ liệu (convert sang String)
        if(nv.getNgayVaoLam() != null) {
            txtNgayVaoLam.setText(DATE_FORMATTER.format(nv.getNgayVaoLam()));
        } else {
            txtNgayVaoLam.setText("");
        }

        LocalDate ngayThoiViec = dao.getNgayThoiViec(nv.getMaNhanVien());
        txtNgayThoiViec.setText(ngayThoiViec != null ? DATE_FORMATTER.format(ngayThoiViec) : "");

        btnThem.setDisable(true);
        btnSua.setDisable(false);
        btnThoiViec.setDisable(nv.getNgayThoiViec() != null);
        btnTaiTuyen.setDisable(nv.getNgayThoiViec() == null);
    }

    private void handleAdd() {
        String ten = txtTen.getText().trim();
        String chucVu = cboChucVu.getValue() != null ? cboChucVu.getValue() : "";
        String sdt = txtSoDT.getText().trim();
        String cccd = txtCCCD.getText().trim();
        LocalDate sinh = dpNgaySinh.getValue();

        // [LOGIC MỚI] Lấy ngày hiện tại làm ngày vào làm
        LocalDate vao = LocalDate.now();

        if (ten.isEmpty() || chucVu.isEmpty() || sdt.isEmpty() || cccd.isEmpty() || sinh == null) {
            showAlert(Alert.AlertType.ERROR, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (!ten.matches("^[\\p{L} ]+$")) {
            showAlert(Alert.AlertType.ERROR, "Họ tên chỉ được chứa chữ cái.");
            return;
        }
        if (sinh.isAfter(LocalDate.now().minusYears(18))) {
            showAlert(Alert.AlertType.ERROR, "Nhân viên phải đủ 18 tuổi.");
            return;
        }
        if (!sdt.matches("\\d{10,11}")) {
            showAlert(Alert.AlertType.ERROR, "SĐT không đúng (10-11 số).");
            return;
        }
        if (!cccd.matches("\\d{12}")) {
            showAlert(Alert.AlertType.ERROR, "CCCD phải đúng 12 chữ số.");
            return;
        }

        String ma = dao.generateMaNhanVien();
        NhanVien nv = new NhanVien(ma, ten, chucVu, cccd, sdt, sinh, vao, null);

        if (dao.addNhanVien(nv)) {
            showAlert(Alert.AlertType.INFORMATION, "Thêm thành công. Ngày vào làm: " + DATE_FORMATTER.format(vao));
            loadData();
            clearForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Thêm thất bại.");
        }
    }

    private void handleUpdate() {
        String ma = txtMa.getText();
        if (ma.isEmpty()) return;

        String ten = txtTen.getText().trim();
        String chucVu = cboChucVu.getValue();
        String sdt = txtSoDT.getText().trim();
        String cccd = txtCCCD.getText().trim();
        LocalDate sinh = dpNgaySinh.getValue();

        // [LƯU Ý] Khi cập nhật, ta lấy lại ngày vào làm cũ từ Textfield (parse về LocalDate)
        LocalDate vao = null;
        try {
            vao = LocalDate.parse(txtNgayVaoLam.getText(), DATE_FORMATTER);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi dữ liệu ngày vào làm.");
            return;
        }

        if (ten.isEmpty() || chucVu == null || sdt.isEmpty() || cccd.isEmpty() || sinh == null) {
            showAlert(Alert.AlertType.ERROR, "Thiếu thông tin!");
            return;
        }

        // ... (Validate giống handleAdd) ...
        if (!ten.matches("^[\\p{L} ]+$")) { showAlert(Alert.AlertType.ERROR, "Tên sai định dạng"); return; }
        if (sinh.isAfter(LocalDate.now().minusYears(18))) { showAlert(Alert.AlertType.ERROR, "Chưa đủ 18 tuổi"); return; }
        if (!sdt.matches("\\d{10,11}")) { showAlert(Alert.AlertType.ERROR, "SĐT sai"); return; }
        if (!cccd.matches("\\d{12}")) { showAlert(Alert.AlertType.ERROR, "CCCD sai"); return; }

        if (dao.isSoDienThoaiExistsForOther(sdt, ma)) { showAlert(Alert.AlertType.ERROR, "SĐT trùng!"); return; }
        if (dao.isCCCDExistsForOther(cccd, ma)) { showAlert(Alert.AlertType.ERROR, "CCCD trùng!"); return; }

        NhanVien nv = dao.getNhanVienByMa(ma);
        if (nv != null) {
            nv.setTenNhanVien(ten);
            nv.setChucVu(chucVu);
            nv.setSoDienThoai(sdt);
            nv.setCCCD(cccd);
            nv.setNgaySinh(sinh);
            nv.setNgayVaoLam(vao); // Cập nhật lại ngày vào làm (dù thường ít sửa)

            if (dao.updateNhanVien(nv)) {
                showAlert(Alert.AlertType.INFORMATION, "Cập nhật thành công.");
                loadData();
                clearForm();
            } else {
                showAlert(Alert.AlertType.ERROR, "Cập nhật thất bại.");
            }
        }
    }

    private void handleThoiViec() {
        NhanVien sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null || sel.getNgayThoiViec() != null) return;
        if (confirmDialog("Cho nhân viên " + sel.getTenNhanVien() + " nghỉ việc?")) {
            if (dao.thoiViecNhanVien(sel.getMaNhanVien())) {
                showAlert(Alert.AlertType.INFORMATION, "Đã cập nhật trạng thái nghỉ việc.");
                loadData(); clearForm();
            }
        }
    }

    private void handleTaiTuyen() {
        NhanVien sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null || sel.getNgayThoiViec() == null) return;
        if (confirmDialog("Tái tuyển nhân viên " + sel.getTenNhanVien() + "?")) {
            if (dao.taiTuyenNhanVien(sel.getMaNhanVien())) {
                showAlert(Alert.AlertType.INFORMATION, "Tái tuyển thành công.");
                loadData(); clearForm();
            }
        }
    }

    private void loadData() {
        Platform.runLater(() -> {
            data.clear();
            data.addAll(dao.getAllNhanVien());
            tableView.refresh();
        });
    }

    private void clearForm() {
        txtMa.clear();
        txtTen.clear();
        cboChucVu.setValue(null);
        txtSoDT.clear();
        txtCCCD.clear();
        dpNgaySinh.setValue(null);
        dpNgaySinh.getEditor().clear(); // Xóa text trong editor

        // Set mặc định ngày vào làm là Hôm nay để người dùng thấy
        txtNgayVaoLam.setText(DATE_FORMATTER.format(LocalDate.now()));

        txtNgayThoiViec.clear();

        btnThem.setDisable(false);
        btnSua.setDisable(true);
        btnThoiViec.setDisable(true);
        btnTaiTuyen.setDisable(true);
        tableView.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert a = new Alert(type); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }
    private boolean confirmDialog(String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION); a.setHeaderText(null); a.setContentText(msg);
        return a.showAndWait().filter(b -> b == ButtonType.OK).isPresent();
    }
}
