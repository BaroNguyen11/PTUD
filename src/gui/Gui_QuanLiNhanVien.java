//package gui;
//
//import dao.NhanVien_DAO;
//import entity.NhanVien;
//import javafx.application.Platform;
//import javafx.beans.property.ReadOnlyObjectWrapper;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.geometry.HPos;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.layout.*;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//public class Gui_QuanLiNhanVien extends VBox {
//
//    private final NhanVien_DAO dao = new NhanVien_DAO();
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
//
//    public Gui_QuanLiNhanVien() {
//        initializeUI();
//        loadData();
//    }
//
//    private void initializeUI() {
//        this.setSpacing(16);
//        this.setPadding(new Insets(16));
//        this.getStyleClass().add("quan-ly-nhan-vien");
//
//        // --- Title ---
//        Label mainTitle = new Label("Tìm kiếm nhân viên");
//        mainTitle.getStyleClass().add("main-title");
//
//        // --- Body HBox ---
//        HBox body = new HBox(16);
//        VBox.setVgrow(body, Priority.ALWAYS);
//
//        VBox leftPane = createLeftPane();
//        HBox.setHgrow(leftPane, Priority.ALWAYS);
//
//        VBox rightPane = createRightPane();
//        HBox.setHgrow(rightPane, Priority.ALWAYS);
//
//        body.getChildren().addAll(leftPane, rightPane);
//        this.getChildren().addAll(mainTitle, body);
//    }
//
//    // ========== LEFT PANE ==========
//    private VBox createLeftPane() {
//        VBox box = new VBox(12);
//        box.getStyleClass().add("left-pane");
//
//        // --- Search Bar ---
//        HBox searchBar = new HBox(8);
//        searchBar.setAlignment(Pos.CENTER_LEFT);
//
//        TextField txtSearch = new TextField();
//        txtSearch.setPromptText("Tìm mã, tên hoặc SĐT...");
//        txtSearch.getStyleClass().add("input");
//        HBox.setHgrow(txtSearch, Priority.ALWAYS);
//
//        Button btnSearch = new Button("Tìm kiếm");
//        btnSearch.getStyleClass().add("btn-outline");
//
//        Button btnRefresh = new Button("Làm mới");
//        btnRefresh.getStyleClass().add("btn-outline");
//
//        searchBar.getChildren().addAll(txtSearch, btnSearch, btnRefresh);
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
//        btnRefresh.setOnAction(e -> {
//            txtSearch.clear();
//            loadData();
//        });
//
//        // --- Table ---
//        tableView = new TableView<>();
//        tableView.setItems(data);
//        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
//        tableView.getStyleClass().add("nhan-vien-table");
//        VBox.setVgrow(tableView, Priority.ALWAYS);
//
//        TableColumn<NhanVien, Number> colSTT = new TableColumn<>("STT");
//        colSTT.setCellValueFactory(col -> new ReadOnlyObjectWrapper<>(tableView.getItems().indexOf(col.getValue()) + 1));
//        colSTT.setMaxWidth(60);
//        colSTT.setStyle("-fx-alignment: CENTER;");
//
//        TableColumn<NhanVien, String> colMa = new TableColumn<>("Mã");
//        colMa.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));
//        colMa.setStyle("-fx-alignment: CENTER;");
//
//        TableColumn<NhanVien, String> colTen = new TableColumn<>("Tên nhân viên");
//        colTen.setCellValueFactory(new PropertyValueFactory<>("tenNhanVien"));
//
//        TableColumn<NhanVien, String> colChucVu = new TableColumn<>("Chức vụ");
//        colChucVu.setCellValueFactory(new PropertyValueFactory<>("chucVu"));
//        colChucVu.setStyle("-fx-alignment: CENTER;");
//
//        TableColumn<NhanVien, String> colSDT = new TableColumn<>("SĐT");
//        colSDT.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
//        colSDT.setStyle("-fx-alignment: CENTER;");
//
//        TableColumn<NhanVien, String> colCCCD = new TableColumn<>("CCCD");
//        colCCCD.setCellValueFactory(new PropertyValueFactory<>("CCCD"));
//        colCCCD.setStyle("-fx-alignment: CENTER;");
//
//        TableColumn<NhanVien, String> colTT = new TableColumn<>("Trạng Thái");
//        colTT.setCellValueFactory(cell -> {
//            NhanVien nv = cell.getValue();
//            String s = nv.getNgayThoiViec() == null ? "Đang làm" : "Đã nghỉ";
//            return new ReadOnlyObjectWrapper<>(s);
//        });
//        colTT.setCellFactory(col -> new TableCell<>() {
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || item == null) {
//                    setText(null);
//                    setStyle("");
//                    return;
//                }
//                setText(item);
//                if (item.equals("Đang làm")) {
//                    setStyle("-fx-text-fill: green; -fx-font-weight: bold; -fx-alignment: CENTER;");
//                } else {
//                    setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-alignment: CENTER;");
//                }
//            }
//        });
//        colTT.setMaxWidth(110);
//
//        tableView.getColumns().addAll(colSTT, colMa, colTen, colChucVu, colSDT, colCCCD, colTT);
//
//        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> onTableSelectionChanged(newV));
//
//        box.getChildren().addAll(searchBar, new Label("Danh sách nhân viên"), tableView);
//        VBox.setVgrow(tableView, Priority.ALWAYS);
//
//        return box;
//    }
//
//    // ========== RIGHT PANE ==========
//    private VBox createRightPane() {
//        VBox box = new VBox(15);
//        box.setPadding(new Insets(20));
//        box.getStyleClass().add("right-pane-box");
//        box.setMaxWidth(380);
//
//        Label title = new Label("Thông tin nhân viên");
//        title.getStyleClass().add("panel-title");
//        title.setMaxWidth(Double.MAX_VALUE);
//        title.setAlignment(Pos.CENTER);
//
//        // --- Form Controls ---
//        txtMa = new TextField();
//        txtMa.setEditable(false);
//        txtMa.getStyleClass().add("input");
//        txtMa.setPrefHeight(35);
//
//        txtTen = new TextField();
//        txtTen.getStyleClass().add("input");
//        txtTen.setPrefHeight(35);
//
//        cboChucVu = new ComboBox<>();
//        cboChucVu.getItems().addAll("Nhân viên", "Quản lý");
//        cboChucVu.getStyleClass().add("input");
//        cboChucVu.setMaxWidth(Double.MAX_VALUE);
//        HBox.setHgrow(cboChucVu, Priority.ALWAYS);
//
//        txtSoDT = new TextField();
//        txtSoDT.getStyleClass().add("input");
//        txtSoDT.setPrefHeight(35);
//        txtCCCD = new TextField();
//        txtCCCD.getStyleClass().add("input");
//        txtCCCD.setPrefHeight(35);
//        dpNgaySinh = new DatePicker();
//        dpNgaySinh.getStyleClass().add("input");
//        dpNgaySinh.setPrefHeight(35);
//        dpNgaySinh.getStyleClass().add("input");
//        //dpNgaySinh.setStyle("-fx-pref-width: 100%;");
//        dpNgayVaoLam = new DatePicker();
//        dpNgayVaoLam.getStyleClass().add("input");
//        dpNgayVaoLam.setPrefHeight(35);
//
//        txtNgayThoiViec = new TextField();
//        txtNgayThoiViec.setEditable(false);
//        txtNgayThoiViec.getStyleClass().add("input-readonly");
//        txtNgayThoiViec.setPrefHeight(35);
//        txtNgayThoiViec.setMaxWidth(Double.MAX_VALUE);
//
//
//        // Tạo form với bố cục chặt chẽ hơn
//        GridPane form = new GridPane();
//        form.setHgap(15);
//        form.setVgap(15);
//        form.setMaxWidth(Double.MAX_VALUE);
//        form.setPadding(new Insets(5));
//
//        // Add form labels + controls với label được căn phải
//        form.add(createFormLabel("Mã nhân viên:"), 0, 0);
//        form.add(txtMa, 1, 0);
//        form.add(createFormLabel("Họ tên:"), 0, 1);
//        form.add(txtTen, 1, 1);
//        form.add(createFormLabel("Chức vụ:"), 0, 2);
//        form.add(cboChucVu, 1, 2);
//        form.add(createFormLabel("Số ĐT:"), 0, 3);
//        form.add(txtSoDT, 1, 3);
//        form.add(createFormLabel("CCCD:"), 0, 4);
//        form.add(txtCCCD, 1, 4);
//        form.add(createFormLabel("Ngày sinh:"), 0, 5);
//        form.add(dpNgaySinh, 1, 5);
//        form.add(createFormLabel("Ngày vào làm:"), 0, 6);
//        form.add(dpNgayVaoLam, 1, 6);
//        form.add(createFormLabel("Ngày thôi việc:"), 0, 7);
//        form.add(txtNgayThoiViec, 1, 7);
//
//        // Column constraints - tối ưu tỷ lệ
//        ColumnConstraints c1 = new ColumnConstraints();
//        c1.setPercentWidth(38);
//        c1.setHalignment(HPos.LEFT);
//        c1.setFillWidth(true);
//
//        ColumnConstraints c2 = new ColumnConstraints();
//        c2.setPercentWidth(62);
//        c2.setHgrow(Priority.ALWAYS);
//
//        form.getColumnConstraints().addAll(c1, c2);
//
//        // --- Buttons với bố cục gọn gàng và ĐẦY ĐỦ SỰ KIỆN ---
//        btnThem = createActionButton("Thêm", "btn-primary", 100);
//        btnSua = createActionButton("Sửa", "btn-accent", 100);
//        btnThoiViec = createActionButton("Thôi việc", "btn-danger", 100);
//        btnTaiTuyen = createActionButton("Tái tuyển", "btn-warning", 100);
//        btnClear = createActionButton("Làm mới", "btn-muted", 100);
//
//        // === GẮN SỰ KIỆN CHO CÁC NÚT ===
//        btnThem.setOnAction(e -> handleAdd());
//        btnSua.setOnAction(e -> handleUpdate());
//        btnThoiViec.setOnAction(e -> handleThoiViec());
//        btnTaiTuyen.setOnAction(e -> handleTaiTuyen());
//        btnClear.setOnAction(e -> clearForm());
//
//        // Sắp xếp buttons thành 2 hàng để tiết kiệm không gian
//        VBox buttonsContainer = new VBox(12);
//        buttonsContainer.setAlignment(Pos.CENTER);
//        buttonsContainer.setPadding(new Insets(15, 0, 170, 0));
//
//        // Hàng 1: Thêm, Sửa, Làm mới
//        HBox topButtons = new HBox(10);
//        topButtons.setAlignment(Pos.CENTER);
//        topButtons.getChildren().addAll(btnThem, btnSua, btnClear);
//
//        // Hàng 2: Thôi việc, Tái tuyển
//        HBox bottomButtons = new HBox(10);
//        bottomButtons.setAlignment(Pos.CENTER);
//        bottomButtons.getChildren().addAll(btnThoiViec, btnTaiTuyen);
//
//        buttonsContainer.getChildren().addAll(topButtons, bottomButtons);
//
//        // Thêm khoảng trống linh hoạt
//        Region spacer = new Region();
//        VBox.setVgrow(spacer, Priority.SOMETIMES);
//
//        box.getChildren().addAll(title, form, spacer, buttonsContainer);
//
//        // Khởi tạo trạng thái ban đầu cho các nút
//        clearForm();
//
//        return box;
//    }
//
//    // Helper method để tạo label cho form
//    private Label createFormLabel(String text) {
//        Label label = new Label(text);
//        label.getStyleClass().add("form-label");
//        label.setAlignment(Pos.CENTER_LEFT);
//        label.setPrefHeight(35);
//        label.setMaxWidth(Double.MAX_VALUE);
//        return label;
//    }
//
//    // Helper method để tạo button
//    private Button createActionButton(String text, String styleClass, double width) {
//        Button button = new Button(text);
//        button.getStyleClass().add(styleClass);
//        button.setPrefHeight(35);
//        button.setPrefWidth(width);
//        button.setMaxWidth(Double.MAX_VALUE);
//        return button;
//    }
//
//    // ========== Handlers ==========
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
//    // --- Add, Update, Thôi việc, Tái tuyển ---
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
//    // --- Helpers ---
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
//        btnThem.setDisable(false);
//        btnSua.setDisable(true);
//        btnThoiViec.setDisable(true);
//        btnTaiTuyen.setDisable(true);
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
package gui;

import dao.NhanVien_DAO;
import entity.NhanVien;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Gui_QuanLiNhanVien extends VBox {

    private final NhanVien_DAO dao = new NhanVien_DAO();
    private final ObservableList<NhanVien> data = FXCollections.observableArrayList();

    private TableView<NhanVien> tableView;

    // Form controls
    private TextField txtMa, txtTen, txtSoDT, txtCCCD;
    private ComboBox<String> cboChucVu;
    private DatePicker dpNgaySinh, dpNgayVaoLam;
    private Button btnThem, btnSua, btnThoiViec, btnTaiTuyen, btnClear;
    private TextField txtNgayThoiViec;
    private TextField txtSearch; // Declared here for access in handlers

    public Gui_QuanLiNhanVien() {
        initializeUI();
        loadData();
    }

    private void initializeUI() {
        // --- 1. SETUP ROOT STYLE (Gradient Background) ---
        this.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa 0%, #e9ecef 100%);");
        this.setPadding(new Insets(30, 40, 30, 40));
        this.setSpacing(20);

        // --- 2. HEADER ---
        VBox header = createModernHeader();

        // --- 3. MAIN BODY (Split Pane) ---
        HBox body = new HBox(25);
        VBox.setVgrow(body, Priority.ALWAYS);

        // Left Pane (Search + Table)
        VBox leftPane = createLeftPaneModern();
        HBox.setHgrow(leftPane, Priority.ALWAYS);

        // Right Pane (Form + Actions)
        VBox rightPane = createRightPaneModern();
        rightPane.setPrefWidth(380);
        rightPane.setMinWidth(380);
        rightPane.setMaxWidth(380);

        body.getChildren().addAll(leftPane, rightPane);

        this.getChildren().addAll(header, body);

        // --- 4. ANIMATION FADE IN ---
        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), this);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    // =============================================================
    // 🎨 HEADER MODERN
    // =============================================================
    private VBox createModernHeader() {
        VBox header = new VBox(10);
        header.setPadding(new Insets(20, 30, 20, 30));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle(
                "-fx-background-color: #082744;" + // Dark blue background
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 4);"
        );

        Label title = new Label("QUẢN LÝ NHÂN VIÊN");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: white;");

        Label subtitle = new Label("Quản lý thông tin hồ sơ, chức vụ và trạng thái làm việc của nhân viên");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        subtitle.setStyle("-fx-text-fill: rgba(255,255,255,0.9);");

        header.getChildren().addAll(title, subtitle);
        return header;
    }

    // =============================================================
    // ⬅️ LEFT PANE (SEARCH + TABLE)
    // =============================================================
    private VBox createLeftPaneModern() {
        VBox container = new VBox(20);

        // --- 1. Filter Bar (Card Style) ---
        HBox filterBar = new HBox(15);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(20));
        filterBar.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);"
        );

        txtSearch = new TextField();
        txtSearch.setPromptText("🔍 Tìm mã, tên hoặc SĐT...");
        txtSearch.setPrefHeight(35);
        styleInputField(txtSearch); // Helper method for styling
        HBox.setHgrow(txtSearch, Priority.ALWAYS);

        Button btnSearch = new Button("Tìm kiếm");
        styleButton(btnSearch, "#f1f3f5", "#495057"); // Light gray style

        Button btnRefresh = new Button("🔄 Làm mới");
        styleButton(btnRefresh, "#f1f3f5", "#495057"); // Light gray style

        // Event Handlers
        btnSearch.setOnAction(e -> {
            String kw = txtSearch.getText().trim();
            if (kw.isEmpty()) {
                loadData();
            } else {
                data.clear();
                List<NhanVien> list = dao.searchNhanVien(kw);
                data.addAll(list);
                tableView.refresh();
            }
        });

        // Search on Enter key press in text field
        txtSearch.setOnAction(e -> btnSearch.fire());

        btnRefresh.setOnAction(e -> {
            txtSearch.clear();
            loadData();
        });

        filterBar.getChildren().addAll(txtSearch, btnSearch, btnRefresh);

        // --- 2. Table Section (Card Style) ---
        VBox tableContainer = new VBox();
        tableContainer.setPadding(new Insets(5));
        tableContainer.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);"
        );
        VBox.setVgrow(tableContainer, Priority.ALWAYS);

        tableView = new TableView<>();
        tableView.setItems(data);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Modern Table Styling
        tableView.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-base: white;" +
                        "-fx-control-inner-background: white;" +
                        "-fx-table-cell-border-color: transparent;" +
                        "-fx-table-header-border-color: transparent;" +
                        "-fx-padding: 5;"
        );
        VBox.setVgrow(tableView, Priority.ALWAYS);

        // --- Table Columns ---
        TableColumn<NhanVien, Number> colSTT = new TableColumn<>("STT");
        colSTT.setCellValueFactory(col -> new ReadOnlyObjectWrapper<>(tableView.getItems().indexOf(col.getValue()) + 1));
        colSTT.setMaxWidth(50);
        colSTT.setStyle("-fx-alignment: CENTER; -fx-font-family: 'Segoe UI';");

        TableColumn<NhanVien, String> colMa = new TableColumn<>("Mã NV");
        colMa.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));
        colMa.setStyle("-fx-alignment: CENTER_LEFT; -fx-font-family: 'Segoe UI';");

        TableColumn<NhanVien, String> colTen = new TableColumn<>("Họ và Tên");
        colTen.setCellValueFactory(new PropertyValueFactory<>("tenNhanVien"));
        colTen.setStyle("-fx-font-weight: bold; -fx-alignment: CENTER_LEFT; -fx-font-family: 'Segoe UI';");

        TableColumn<NhanVien, String> colChucVu = new TableColumn<>("Chức Vụ");
        colChucVu.setCellValueFactory(new PropertyValueFactory<>("chucVu"));
        colChucVu.setStyle("-fx-alignment: CENTER; -fx-font-family: 'Segoe UI';");

        TableColumn<NhanVien, String> colSDT = new TableColumn<>("Số Điện Thoại");
        colSDT.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
        colSDT.setStyle("-fx-alignment: CENTER; -fx-font-family: 'Segoe UI';");

        TableColumn<NhanVien, String> colCCCD = new TableColumn<>("CCCD");
        colCCCD.setCellValueFactory(new PropertyValueFactory<>("CCCD"));
        colCCCD.setStyle("-fx-alignment: CENTER; -fx-font-family: 'Segoe UI';");

        // Status Column with Badge Style
        TableColumn<NhanVien, String> colTT = new TableColumn<>("Trạng Thái");
        colTT.setCellValueFactory(cell -> {
            NhanVien nv = cell.getValue();
            String s = nv.getNgayThoiViec() == null ? "Đang làm" : "Đã nghỉ";
            return new ReadOnlyObjectWrapper<>(s);
        });
        colTT.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label lbl = new Label(item);
                    lbl.setPadding(new Insets(4, 10, 4, 10));
                    lbl.setStyle("-fx-font-weight: bold; -fx-background-radius: 12; -fx-font-family: 'Segoe UI'; -fx-font-size: 11px;");

                    if ("Đang làm".equals(item)) {
                        lbl.setStyle(lbl.getStyle() + "-fx-background-color: #d4edda; -fx-text-fill: #155724;"); // Green badge
                    } else {
                        lbl.setStyle(lbl.getStyle() + "-fx-background-color: #f8d7da; -fx-text-fill: #721c24;"); // Red badge
                    }
                    setGraphic(lbl);
                    setAlignment(Pos.CENTER);
                    setText(null);
                }
            }
        });
        colTT.setMaxWidth(120);

        tableView.getColumns().addAll(colSTT, colMa, colTen, colChucVu, colSDT, colCCCD, colTT);

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> onTableSelectionChanged(newV));

        tableContainer.getChildren().add(tableView);
        container.getChildren().addAll(filterBar, tableContainer);

        return container;
    }

    // =============================================================
    // ➡️ RIGHT PANE (FORM + ACTIONS)
    // =============================================================
    private VBox createRightPaneModern() {
        VBox card = new VBox(15);
        card.setPadding(new Insets(25));
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 12, 0, 0, 2);"
        );

        // Title Section
        Label title = new Label("Thông tin chi tiết");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setStyle("-fx-text-fill: #082744; -fx-border-width: 0 0 0 4; -fx-border-color: #082744; -fx-padding: 0 0 0 10;");

        // Form Layout
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15); // Slightly tighter vertical gap
        form.setPadding(new Insets(10, 0, 0, 0));

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(35);
        c1.setHalignment(HPos.LEFT);

        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(65);
        c2.setHgrow(Priority.ALWAYS);

        form.getColumnConstraints().addAll(c1, c2);

        // --- Setup Form Controls ---
        txtMa = new TextField();
        txtMa.setEditable(false);
        styleReadonlyField(txtMa); // Helper for read-only style

        txtTen = new TextField();
        styleInputField(txtTen);

        cboChucVu = new ComboBox<>();
        cboChucVu.getItems().addAll("Nhân viên", "Quản lý");
        styleComboBox(cboChucVu);

        txtSoDT = new TextField();
        styleInputField(txtSoDT);

        txtCCCD = new TextField();
        styleInputField(txtCCCD);

        dpNgaySinh = new DatePicker();
        styleDatePicker(dpNgaySinh);

        dpNgayVaoLam = new DatePicker();
        styleDatePicker(dpNgayVaoLam);

        txtNgayThoiViec = new TextField();
        txtNgayThoiViec.setEditable(false);
        styleReadonlyField(txtNgayThoiViec);

        // --- Add Rows to Form ---
        addFormRow(form, "Mã nhân viên:", txtMa, 0);
        addFormRow(form, "Họ tên:", txtTen, 1);
        addFormRow(form, "Chức vụ:", cboChucVu, 2);
        addFormRow(form, "Số điện thoại:", txtSoDT, 3);
        addFormRow(form, "CCCD:", txtCCCD, 4);
        addFormRow(form, "Ngày sinh:", dpNgaySinh, 5);
        addFormRow(form, "Ngày vào làm:", dpNgayVaoLam, 6);
        addFormRow(form, "Ngày thôi việc:", txtNgayThoiViec, 7);

        // --- Buttons Section ---
        // Top Row Buttons
        btnThem = new Button("Thêm");
        styleButton(btnThem, "#10ac84", "white"); // Green
        btnThem.setMaxWidth(Double.MAX_VALUE);

        btnSua = new Button("Cập Nhật");
        styleButton(btnSua, "#2e86de", "white"); // Blue
        btnSua.setMaxWidth(Double.MAX_VALUE);

        btnClear = new Button("Làm Mới");
        styleButton(btnClear, "#95a5a6", "white"); // Gray
        btnClear.setMaxWidth(Double.MAX_VALUE);

        // Bottom Row Buttons
        btnThoiViec = new Button("Thôi Việc");
        styleButton(btnThoiViec, "#ee5253", "white"); // Red
        btnThoiViec.setMaxWidth(Double.MAX_VALUE);

        btnTaiTuyen = new Button("Tái Tuyển");
        styleButton(btnTaiTuyen, "#feca57", "#222f3e"); // Yellow
        btnTaiTuyen.setMaxWidth(Double.MAX_VALUE);

        // Layout for Buttons
        GridPane btnGrid = new GridPane();
        btnGrid.setHgap(10);
        btnGrid.setVgap(10);
        btnGrid.setPadding(new Insets(20, 0, 0, 0));

        // Row 1
        btnGrid.add(btnThem, 0, 0);
        btnGrid.add(btnSua, 1, 0);
        btnGrid.add(btnClear, 2, 0);

        // Row 2 (Spanning 2 columns for wider buttons if needed, or distribute evenly)
        btnGrid.add(btnThoiViec, 0, 1, 2, 1); // Span 2 cols
        btnGrid.add(btnTaiTuyen, 2, 1); // 1 col

        // Set column constraints for buttons to be equal width
        ColumnConstraints btnCol = new ColumnConstraints();
        btnCol.setPercentWidth(33.33);
        btnGrid.getColumnConstraints().addAll(btnCol, btnCol, btnCol);

        // Button Actions (Logic remains the same)
        btnThem.setOnAction(e -> handleAdd());
        btnSua.setOnAction(e -> handleUpdate());
        btnThoiViec.setOnAction(e -> handleThoiViec());
        btnTaiTuyen.setOnAction(e -> handleTaiTuyen());
        btnClear.setOnAction(e -> clearForm());

        // Assemble Right Pane
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS); // Push content up

        card.getChildren().addAll(title, form, btnGrid, spacer);

        // Initialize button states
        clearForm();

        return card;
    }

    // =============================================================
    // 🛠️ UI HELPERS
    // =============================================================

    private void addFormRow(GridPane grid, String labelText, Node field, int row) {
        Label lbl = new Label(labelText);
        lbl.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        lbl.setStyle("-fx-text-fill: #535c68;"); // Grayish text for labels
        grid.add(lbl, 0, row);
        grid.add(field, 1, row);
    }

    private void styleInputField(TextField tf) {
        tf.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #ced6e0;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 8;" +
                        "-fx-font-family: 'Segoe UI';"
        );
        tf.setPrefHeight(35);
    }

    private void styleReadonlyField(TextField tf) {
        tf.setStyle(
                "-fx-background-color: #f1f2f6;" + // Light gray background
                        "-fx-border-color: #dfe4ea;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 8;" +
                        "-fx-text-fill: #747d8c;" + // Dimmed text color
                        "-fx-font-family: 'Segoe UI';"
        );
        tf.setPrefHeight(35);
    }

    private void styleComboBox(ComboBox<?> cbo) {
        cbo.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #ced6e0;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 2;" + // Padding inside control
                        "-fx-font-family: 'Segoe UI';"
        );
        cbo.setMaxWidth(Double.MAX_VALUE);
        cbo.setPrefHeight(35);
    }

    private void styleDatePicker(DatePicker dp) {
        dp.setStyle("-fx-font-family: 'Segoe UI';"); // Styles mainly inherited, just setting font
        dp.getEditor().setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #ced6e0;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 8;"
        );
        dp.setMaxWidth(Double.MAX_VALUE);
        dp.setPrefHeight(35);
    }

    private void styleButton(Button btn, String bgColor, String textColor) {
        btn.setStyle(
                "-fx-background-color: " + bgColor + ";" +
                        "-fx-text-fill: " + textColor + ";" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 8 15;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 13px;"
        );
        addHoverEffect(btn);
    }

    private void addHoverEffect(Node node) {
        node.setOnMouseEntered(e -> {
            node.setStyle(node.getStyle() + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1); -fx-scale-x: 1.03; -fx-scale-y: 1.03;");
        });
        node.setOnMouseExited(e -> {
            node.setScaleX(1.0);
            node.setScaleY(1.0);
        });
    }

    // =============================================================
    // ⚙️ LOGIC HANDLERS (UNCHANGED)
    // =============================================================

    private void onTableSelectionChanged(NhanVien nv) {
        if (nv == null) {
            clearForm();
            return;
        }

        txtMa.setText(nv.getMaNhanVien());
        txtTen.setText(nv.getTenNhanVien());
        cboChucVu.setValue(nv.getChucVu());
        txtSoDT.setText(nv.getSoDienThoai());
        txtCCCD.setText(nv.getCCCD());
        dpNgaySinh.setValue(nv.getNgaySinh());
        dpNgayVaoLam.setValue(nv.getNgayVaoLam());

        LocalDate ngayThoiViec = dao.getNgayThoiViec(nv.getMaNhanVien());
        txtNgayThoiViec.setText(
                ngayThoiViec != null
                        ? ngayThoiViec.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        : ""
        );

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
        LocalDate vao = dpNgayVaoLam.getValue() != null ? dpNgayVaoLam.getValue() : LocalDate.now();

        // Validate
        if (ten.isEmpty() || chucVu.isEmpty() || sdt.isEmpty() || cccd.isEmpty() || sinh == null) {
            showAlert(Alert.AlertType.ERROR, "Vui lòng nhập đầy đủ thông tin bắt buộc!");
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
        if (vao.isAfter(LocalDate.now())) {
            showAlert(Alert.AlertType.ERROR, "Ngày vào làm không được vượt quá hiện tại.");
            return;
        }

        if (!sdt.matches("\\d{10,11}")) {
            showAlert(Alert.AlertType.ERROR, "Số điện thoại không đúng định dạng (10-11 chữ số).");
            return;
        }

        if (!cccd.matches("\\d{12}")) {
            showAlert(Alert.AlertType.ERROR, "CCCD phải đúng 12 chữ số.");
            return;
        }

        String ma = dao.generateMaNhanVien();
        NhanVien nv = new NhanVien(ma, ten, chucVu, cccd, sdt, sinh, vao, null);

        boolean ok = dao.addNhanVien(nv);
        if (ok) {
            showAlert(Alert.AlertType.INFORMATION, "Thêm nhân viên thành công.");
            loadData();
            clearForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Thêm nhân viên thất bại.");
        }
    }

    private void handleUpdate() {
        String ma = txtMa.getText();
        if (ma.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Vui lòng chọn nhân viên cần sửa.");
            return;
        }

        String ten = txtTen.getText().trim();
        String chucVu = cboChucVu.getValue();
        String sdt = txtSoDT.getText().trim();
        String cccd = txtCCCD.getText().trim();
        LocalDate sinh = dpNgaySinh.getValue();
        LocalDate vao = dpNgayVaoLam.getValue();

        if (ten.isEmpty() || chucVu == null || sdt.isEmpty() || cccd.isEmpty() || sinh == null || vao == null) {
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
        if (vao.isAfter(LocalDate.now())) {
            showAlert(Alert.AlertType.ERROR, "Ngày vào làm không được vượt quá hiện tại.");
            return;
        }

        if (!sdt.matches("\\d{10,11}")) {
            showAlert(Alert.AlertType.ERROR, "Số điện thoại không đúng định dạng (10-11 chữ số).");
            return;
        }

        if (!cccd.matches("\\d{12}")) {
            showAlert(Alert.AlertType.ERROR, "CCCD phải đúng 12 chữ số.");
            return;
        }

        if (dao.isSoDienThoaiExistsForOther(sdt, ma)) {
            showAlert(Alert.AlertType.ERROR, "Số điện thoại đã tồn tại cho nhân viên khác!");
            return;
        }
        if (dao.isCCCDExistsForOther(cccd, ma)) {
            showAlert(Alert.AlertType.ERROR, "CCCD đã tồn tại cho nhân viên khác!");
            return;
        }

        NhanVien nv = dao.getNhanVienByMa(ma);
        if (nv == null) {
            showAlert(Alert.AlertType.ERROR, "Không tìm thấy nhân viên.");
            return;
        }

        nv.setTenNhanVien(ten);
        nv.setChucVu(chucVu);
        nv.setSoDienThoai(sdt);
        nv.setCCCD(cccd);
        nv.setNgaySinh(sinh);
        nv.setNgayVaoLam(vao);

        boolean ok = dao.updateNhanVien(nv);
        if (ok) {
            showAlert(Alert.AlertType.INFORMATION, "Cập nhật thành công.");
            loadData();
            clearForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Cập nhật thất bại.");
        }
    }

    private void handleThoiViec() {
        NhanVien sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showAlert(Alert.AlertType.ERROR, "Chọn nhân viên cần cho thôi việc.");
            return;
        }
        if (sel.getNgayThoiViec() != null) {
            showAlert(Alert.AlertType.INFORMATION, "Nhân viên đã nghỉ rồi.");
            return;
        }

        boolean conf = confirmDialog("Bạn có chắc muốn cho nhân viên " + sel.getTenNhanVien() + " thôi việc?");
        if (!conf) return;

        boolean ok = dao.thoiViecNhanVien(sel.getMaNhanVien());
        if (ok) {
            showAlert(Alert.AlertType.INFORMATION, "Đã cho nghỉ.");
            loadData();
            clearForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Thao tác thất bại.");
        }
    }

    private void handleTaiTuyen() {
        NhanVien sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showAlert(Alert.AlertType.ERROR, "Chọn nhân viên cần tái tuyển.");
            return;
        }
        if (sel.getNgayThoiViec() == null) {
            showAlert(Alert.AlertType.INFORMATION, "Nhân viên hiện đang làm việc.");
            return;
        }

        boolean conf = confirmDialog("Bạn có chắc muốn tái tuyển nhân viên " + sel.getTenNhanVien() + " không?");
        if (!conf) return;

        boolean ok = dao.taiTuyenNhanVien(sel.getMaNhanVien());
        if (ok) {
            showAlert(Alert.AlertType.INFORMATION, "Đã tái tuyển.");
            loadData();
            clearForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Thao tác thất bại.");
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
        dpNgayVaoLam.setValue(null);
        txtNgayThoiViec.clear();

        btnThem.setDisable(false);
        btnSua.setDisable(true);
        btnThoiViec.setDisable(true);
        btnTaiTuyen.setDisable(true);

        tableView.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert a = new Alert(type);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private boolean confirmDialog(String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        return a.showAndWait().filter(btn -> btn == ButtonType.OK).isPresent();
    }
}