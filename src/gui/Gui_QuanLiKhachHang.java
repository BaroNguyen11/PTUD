//package gui;
//
//import dao.KhachHang_DAO;
//import entity.KhachHang;
//import javafx.beans.property.ReadOnlyObjectWrapper;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Cursor;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.*;
//import javafx.stage.Modality;
//import javafx.stage.Stage;
//
//public class Gui_QuanLiKhachHang extends BorderPane {
//    private final ObservableList<KhachHang> data = FXCollections.observableArrayList();
//    private final KhachHang_DAO khachHangDAO = new KhachHang_DAO();
//    private TableView<KhachHang> table;
//    private ComboBox<String> cbSort;
//    private TextField txtSearch;
//
//    // ====================== ✅ CONSTRUCTOR ======================
//    public Gui_QuanLiKhachHang() {
//        // Tạo layout
//        VBox searchSection = createSearchSection();
//        table = createCustomerTable();
//
//        // Gán vào BorderPane
//        setTop(searchSection);
//        setCenter(table);
//
//        setPadding(new Insets(20));
//        setStyle("-fx-background-color: #f7f9fc;");
//
//        // 🔹 Load dữ liệu từ database sau khi tạo giao diện
//        loadDataFromDatabase();
//    }
//
//    // =============================================================
//    // 🔍 THANH TÌM KIẾM + THÊM
//    // =============================================================
//    private VBox createSearchSection() {
//        Label lblSearchTitle = new Label("Tìm kiếm khách hàng");
//        lblSearchTitle.setStyle("""
//                -fx-font-size: 13px;
//                -fx-font-weight: bold;
//                -fx-text-fill: #14274e;
//                """);
//
//        // Ô tìm kiếm có icon
//        Image imgSearch = new Image(getClass().getResource("/img/search-normal.png").toExternalForm());
//        ImageView iconSearch = new ImageView(imgSearch);
//        iconSearch.setFitWidth(16);
//        iconSearch.setFitHeight(16);
//
//        txtSearch = new TextField();
//        txtSearch.setPromptText("Tìm theo Tên/SĐT...");
//        txtSearch.setPrefWidth(500);
//        txtSearch.setStyle("""
//                -fx-background-color: white;
//                -fx-border-color: #ccc;
//                -fx-border-radius: 8;
//                -fx-background-radius: 8;
//                -fx-padding: 6 10 6 30;
//                """);
//
//        StackPane searchBox = new StackPane(txtSearch, iconSearch);
//        StackPane.setAlignment(iconSearch, Pos.CENTER_LEFT);
//        StackPane.setMargin(iconSearch, new Insets(0, 0, 0, 8));
//
//        // ComboBox lọc
//        cbSort = new ComboBox<>();
//        cbSort.getItems().addAll("Tất cả xếp loại", "Khách thường", "Khách VIP");
//        cbSort.getSelectionModel().selectFirst();
//        cbSort.setStyle("-fx-padding: 6; -fx-background-radius: 8;");
//        cbSort.setPrefWidth(180);
//
//        // Separator
//        Separator sep = new Separator();
//        sep.setOrientation(javafx.geometry.Orientation.VERTICAL);
//        sep.setPrefHeight(30);
//
//        // Nút thêm
//        Button btnAdd = new Button("➕ Thêm khách hàng mới");
//        stylePrimaryButton(btnAdd);
//        btnAdd.setOnAction(e -> openAddModal());
//
//        // Gộp toàn bộ phần trên
//        HBox searchBar = new HBox(20, searchBox, cbSort, sep, btnAdd);
//        searchBar.setAlignment(Pos.CENTER_LEFT);
//        searchBar.setPrefHeight(50);
//        HBox.setHgrow(searchBox, Priority.ALWAYS);
//        txtSearch.setMaxWidth(Double.MAX_VALUE);
//        searchBar.setPadding(new Insets(0, 0, 5, 0));
//
//        // 🔹 Thêm sự kiện tìm kiếm
//        txtSearch.textProperty().addListener((obs, oldValue, newValue) -> {
//            applyFilters();
//        });
//
//        // 🔹 Thêm sự kiện lọc
//        cbSort.valueProperty().addListener((obs, oldValue, newValue) -> {
//            applyFilters();
//        });
//
//        return new VBox(5, lblSearchTitle, searchBar);
//    }
//
//    // =============================================================
//    // 📋 TẠO BẢNG KHÁCH HÀNG
//    // =============================================================
//    private TableView<KhachHang> createCustomerTable() {
//        TableView<KhachHang> table = new TableView<>();
//        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//
//        TableColumn<KhachHang, Number> colSTT = new TableColumn<>("STT");
//        TableColumn<KhachHang, String> colID = new TableColumn<>("Mã khách hàng");
//        TableColumn<KhachHang, String> colName = new TableColumn<>("Tên khách hàng");
//        TableColumn<KhachHang, String> colPhone = new TableColumn<>("Số điện thoại");
//        TableColumn<KhachHang, Double> colPoints = new TableColumn<>("Điểm tích lũy");
//        TableColumn<KhachHang, String> colType = new TableColumn<>("Xếp loại");
//        TableColumn<KhachHang, Void> colAction = new TableColumn<>("Hành động");
//
//        colSTT.setCellValueFactory(col -> new ReadOnlyObjectWrapper<>(table.getItems().indexOf(col.getValue()) + 1));
//        colSTT.setStyle("-fx-alignment: CENTER;");
//
//        colID.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getMaKhachHang()));
//        colName.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getTenKhachHang()));
//        colPhone.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getSoDienThoai()));
//        colPoints.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getDiemTichLuy()));
//        colPoints.setStyle("-fx-alignment: CENTER_LEFT;");
//
//        // Cột xếp loại
//        colType.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(getXepLoaiKhachHang(cell.getValue().getDiemTichLuy())));
//        colType.setCellFactory(column -> new TableCell<KhachHang, String>() {
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || item == null) {
//                    setText(null);
//                    setStyle("");
//                } else {
//                    setText(item);
//                    if ("VIP".equals(item)) {
//                        setStyle("-fx-text-fill: #d4af37; -fx-font-weight: bold; -fx-alignment: CENTER;");
//                    } else {
//                        setStyle("-fx-text-fill: #666; -fx-font-weight: bold; -fx-alignment: CENTER;");
//                    }
//                }
//            }
//        });
//
//        // Cột hành động
//        colAction.setCellFactory(param -> new TableCell<>() {
//            private final Button btnEdit = new Button();
//            private final HBox box = new HBox();
//
//            {
//                box.setAlignment(Pos.CENTER);
//                box.setPadding(new Insets(5));
//
//                Image imgEdit = new Image(getClass().getResource("/img/ChinhSua.png").toExternalForm());
//                ImageView imgView = new ImageView(imgEdit);
//                imgView.setFitWidth(18);
//                imgView.setFitHeight(18);
//                btnEdit.setGraphic(imgView);
//                btnEdit.setStyle("-fx-background-color: transparent;");
//                btnEdit.setCursor(Cursor.HAND);
//                btnEdit.setOnAction(e -> {
//                    KhachHang c = getTableView().getItems().get(getIndex());
//                    openEditModal(c);
//                });
//                box.getChildren().add(btnEdit);
//            }
//
//            @Override
//            protected void updateItem(Void item, boolean empty) {
//                super.updateItem(item, empty);
//                setGraphic(empty ? null : box);
//            }
//        });
//
//        table.getColumns().addAll(colSTT, colID, colName, colPhone, colPoints, colType, colAction);
//        table.setItems(data);
//
//        table.setStyle("""
//                -fx-background-color: white;
//                -fx-border-color: #ccc;
//                -fx-border-radius: 8;
//                -fx-font-size: 14px;
//                """);
//
//        return table;
//    }
//
//    // =============================================================
//    // 🔄 HÀM LOAD DỮ LIỆU VÀ LỌC
//    // =============================================================
//    private void loadDataFromDatabase() {
//        data.clear();
//        data.addAll(khachHangDAO.getAllKhachHang());
//    }
//
//    private void applyFilters() {
//        String searchKeyword = txtSearch.getText().trim();
//        String filterType = cbSort.getValue();
//
//        // Lấy toàn bộ dữ liệu từ database
//        java.util.List<KhachHang> allCustomers = khachHangDAO.getAllKhachHang();
//
//        // Lọc theo từ khóa tìm kiếm
//        if (!searchKeyword.isEmpty()) {
//            allCustomers = allCustomers.stream()
//                    .filter(kh ->
//                            kh.getTenKhachHang().toLowerCase().contains(searchKeyword.toLowerCase()) ||
//                                    kh.getSoDienThoai().contains(searchKeyword)
//                    )
//                    .toList();
//        }
//
//        // Lọc theo xếp loại
//        if (filterType != null && !filterType.equals("Tất cả xếp loại")) {
//            allCustomers = allCustomers.stream()
//                    .filter(kh -> {
//                        String xepLoai = getXepLoaiKhachHang(kh.getDiemTichLuy());
//                        if ("Khách VIP".equals(filterType)) {
//                            return "VIP".equals(xepLoai);
//                        } else if ("Khách thường".equals(filterType)) {
//                            return "Thường".equals(xepLoai);
//                        }
//                        return true;
//                    })
//                    .toList();
//        }
//
//        data.setAll(allCustomers);
//    }
//
//    // =============================================================
//    // 🏷️ HÀM XẾP LOẠI KHÁCH HÀNG
//    // =============================================================
//    private String getXepLoaiKhachHang(double diemTichLuy) {
//        return diemTichLuy >= 200 ? "VIP" : "Thường";
//    }
//
//    // =============================================================
//    // 🧰 MODAL THÊM / SỬA KHÁCH HÀNG
//    // =============================================================
//    private void openAddModal() {
//        openModal("Thêm khách hàng mới", null);
//    }
//
//    private void openEditModal(KhachHang kh) {
//        openModal("Chỉnh sửa thông tin khách hàng", kh);
//    }
//
//    private void openModal(String title, KhachHang kh) {
//        Stage modal = new Stage();
//        modal.initModality(Modality.APPLICATION_MODAL);
//        modal.setTitle(title);
//
//        Label lblTitle = new Label(title);
//        lblTitle.setStyle("""
//                -fx-font-size: 22px;
//                -fx-font-weight: bold;
//                -fx-text-fill: #14274e;
//                """);
//        lblTitle.setAlignment(Pos.CENTER);
//
//        Separator line = new Separator();
//        GridPane form = createCustomerForm(kh);
//        HBox buttons = createModalButtons(modal, kh, form);
//
//        VBox layout = new VBox(20, lblTitle, line, form, buttons);
//        layout.setPadding(new Insets(25));
//        layout.setStyle("""
//                -fx-background-color: white;
//                -fx-border-color: #ddd;
//                -fx-border-radius: 10;
//                -fx-background-radius: 10;
//                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 5);
//                """);
//
//        Scene scene = new Scene(layout, 550, 450);
//        modal.setScene(scene);
//        modal.showAndWait();
//    }
//
//    private GridPane createCustomerForm(KhachHang kh) {
//        Label lblMa = new Label("Mã khách hàng:");
//        Label lblTen = new Label("Họ tên khách hàng:");
//        Label lblSDT = new Label("Số điện thoại:");
//        Label lblDiem = new Label("Điểm tích lũy:");
//        Label lblXepLoai = new Label("Xếp loại:");
//
//        for (Label lbl : new Label[]{lblMa, lblTen, lblSDT, lblDiem, lblXepLoai})
//            lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");
//
//        // Tạo mã tự động nếu thêm mới
//        String maKhachHang = kh == null ? khachHangDAO.generateMaKhachHang() : kh.getMaKhachHang();
//
//        TextField txtMa = new TextField(maKhachHang);
//        txtMa.setEditable(false);
//        styleReadonlyField(txtMa);
//
//        TextField txtTen = new TextField(kh == null ? "" : kh.getTenKhachHang());
//        TextField txtSDT = new TextField(kh == null ? "" : kh.getSoDienThoai());
//
//        double diemTichLuy = kh == null ? 0 : kh.getDiemTichLuy();
//        TextField txtDiem = new TextField(String.valueOf(diemTichLuy));
//        txtDiem.setEditable(false);
//        styleReadonlyField(txtDiem);
//
//        // Hiển thị xếp loại
//        String xepLoai = getXepLoaiKhachHang(diemTichLuy);
//        TextField txtXepLoai = new TextField(xepLoai);
//        txtXepLoai.setEditable(false);
//        if ("VIP".equals(xepLoai)) {
//            txtXepLoai.setStyle("""
//                -fx-opacity: 0.8;
//                -fx-background-color: #fff8e1;
//                -fx-border-color: #d4af37;
//                -fx-border-radius: 5;
//                -fx-background-radius: 5;
//                -fx-padding: 6 10;
//                -fx-text-fill: #d4af37;
//                -fx-font-weight: bold;
//                """);
//        } else {
//            styleReadonlyField(txtXepLoai);
//        }
//
//        GridPane form = new GridPane();
//        form.setVgap(18);
//        form.setHgap(20);
//        form.setPadding(new Insets(10, 40, 10, 40));
//        form.addRow(0, lblMa, txtMa);
//        form.addRow(1, lblTen, txtTen);
//        form.addRow(2, lblSDT, txtSDT);
//        form.addRow(3, lblDiem, txtDiem);
//        form.addRow(4, lblXepLoai, txtXepLoai);
//        return form;
//    }
//
//    private HBox createModalButtons(Stage modal, KhachHang kh, GridPane form) {
//        Button btnClose = new Button("Đóng");
//        Button btnSave = new Button("Lưu");
//
//        styleSecondaryButton(btnClose);
//        stylePrimaryDarkButton(btnSave);
//
//        btnClose.setOnAction(e -> modal.close());
//
//        btnSave.setOnAction(e -> {
//            // Lấy các control từ form
//            TextField txtMa = (TextField) form.getChildren().get(1); // cột thứ 2 của dòng 0
//            TextField txtTen = (TextField) form.getChildren().get(3); // cột thứ 2 của dòng 1
//            TextField txtSDT = (TextField) form.getChildren().get(5); // cột thứ 2 của dòng 2
//
//            String ma = txtMa.getText();
//            String ten = txtTen.getText().trim();
//            String sdt = txtSDT.getText().trim();
//
//            // Validation
//            if (ten.isEmpty() || sdt.isEmpty()) {
//                showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng nhập đầy đủ thông tin!");
//                return;
//            }
//
//            if (!sdt.matches("\\d{10,11}")) {
//                showAlert(Alert.AlertType.ERROR, "Lỗi", "Số điện thoại phải có 10-11 chữ số!");
//                return;
//            }
//
//            try {
//                if (kh == null) {
//                    // Thêm mới
//                    if (khachHangDAO.isSoDienThoaiExists(sdt)) {
//                        showAlert(Alert.AlertType.ERROR, "Lỗi", "Số điện thoại đã tồn tại!");
//                        return;
//                    }
//
//                    KhachHang newKh = new KhachHang(ma, ten, sdt, 0);
//                    if (khachHangDAO.addKhachHang(newKh)) {
//                        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm khách hàng thành công!");
//                        loadDataFromDatabase();
//                        modal.close();
//                    }
//                } else {
//                    // Cập nhật
//                    if (khachHangDAO.isSoDienThoaiExistsForOther(sdt, ma)) {
//                        showAlert(Alert.AlertType.ERROR, "Lỗi", "Số điện thoại đã tồn tại cho khách hàng khác!");
//                        return;
//                    }
//
//                    kh.setTenKhachHang(ten);
//                    kh.setSoDienThoai(sdt);
//                    if (khachHangDAO.updateKhachHang(kh)) {
//                        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật khách hàng thành công!");
//                        loadDataFromDatabase();
//                        modal.close();
//                    }
//                }
//            } catch (Exception ex) {
//                showAlert(Alert.AlertType.ERROR, "Lỗi", "Có lỗi xảy ra: " + ex.getMessage());
//                ex.printStackTrace();
//            }
//        });
//
//        HBox buttons = new HBox(15, btnClose, btnSave);
//        buttons.setAlignment(Pos.CENTER);
//        buttons.setPadding(new Insets(15, 40, 25, 40));
//        return buttons;
//    }
//
//    // =============================================================
//    // 🎨 STYLE HỖ TRỢ
//    // =============================================================
//    private void stylePrimaryButton(Button btn) {
//        btn.setStyle("""
//                -fx-background-color: white;
//                -fx-border-color: #ccc;
//                -fx-border-radius: 8;
//                -fx-background-radius: 8;
//                -fx-font-weight: 600;
//                -fx-padding: 8 18;
//                -fx-cursor: hand;
//                """);
//    }
//
//    private void styleReadonlyField(TextField tf) {
//        tf.setStyle("""
//                -fx-opacity: 0.8;
//                -fx-background-color: #f3f3f3;
//                -fx-border-color: #ccc;
//                -fx-border-radius: 5;
//                -fx-background-radius: 5;
//                -fx-padding: 6 10;
//                """);
//    }
//
//    private void styleSecondaryButton(Button btn) {
//        btn.setStyle("""
//                -fx-background-color: #999;
//                -fx-text-fill: white;
//                -fx-font-weight: bold;
//                -fx-padding: 8 25;
//                -fx-background-radius: 6;
//                """);
//    }
//
//    private void stylePrimaryDarkButton(Button btn) {
//        btn.setStyle("""
//                -fx-background-color: #14274e;
//                -fx-text-fill: white;
//                -fx-font-weight: bold;
//                -fx-padding: 8 25;
//                -fx-background-radius: 6;
//                """);
//    }
//
//    // =============================================================
//    // 🔔 HÀM HIỂN THỊ THÔNG BÁO
//    // =============================================================
//    private void showAlert(Alert.AlertType type, String title, String message) {
//        Alert alert = new Alert(type);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//}
package gui;

import dao.KhachHang_DAO;
import entity.KhachHang;
import javafx.animation.FadeTransition;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Gui_QuanLiKhachHang extends BorderPane {
    private final ObservableList<KhachHang> data = FXCollections.observableArrayList();
    private final KhachHang_DAO khachHangDAO = new KhachHang_DAO();
    private TableView<KhachHang> table;
    private ComboBox<String> cbSort;
    private TextField txtSearch;

    // ====================== ✅ CONSTRUCTOR ======================
    public Gui_QuanLiKhachHang() {
        // --- 1. SETUP ROOT STYLE (Gradient Background) ---
        this.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa 0%, #e9ecef 100%);");
        this.setPadding(new Insets(30, 40, 30, 40));

        // --- 2. LAYOUT CHÍNH ---
        VBox rootContent = new VBox(25); // Khoảng cách giữa các phần tử lớn hơn

        // Header
        VBox header = createModernHeader();

        // Search Bar (Filter Section)
        HBox searchSection = createModernSearchBar();

        // Table Section
        VBox tableSection = createModernTableSection();

        rootContent.getChildren().addAll(header, searchSection, tableSection);

        // Cho bảng giãn hết chiều cao còn lại
        VBox.setVgrow(tableSection, Priority.ALWAYS);

        // Gán vào Center của BorderPane
        setCenter(rootContent);

        // --- 3. ANIMATION FADE IN ---
        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), rootContent);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        // 🔹 Load dữ liệu từ database
        loadDataFromDatabase();
    }

    // =============================================================
    // 🎨 HEADER MODERN (Đồng bộ với Gui_ThongKe)
    // =============================================================
    private VBox createModernHeader() {
        VBox header = new VBox(8);
        header.setPadding(new Insets(25));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle(
                "-fx-background-color: #082744;" + // Màu xanh đậm chủ đạo
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 4);"
        );

        Label title = new Label("QUẢN LÝ KHÁCH HÀNG");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: white;");

        Label subtitle = new Label("Tra cứu thông tin, quản lý điểm tích lũy và xếp hạng thành viên");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        subtitle.setStyle("-fx-text-fill: rgba(255,255,255,0.9);");

        header.getChildren().addAll(title, subtitle);
        return header;
    }

    // =============================================================
    // 🔍 THANH TÌM KIẾM + THÊM (STYLE CARD TRẮNG)
    // =============================================================
    private HBox createModernSearchBar() {
        HBox searchBar = new HBox(15);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        searchBar.setPadding(new Insets(20));
        searchBar.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);"
        );

        // 1. Ô tìm kiếm
        txtSearch = new TextField();
        txtSearch.setPromptText("🔍 Tìm theo Tên hoặc Số điện thoại...");
        txtSearch.setPrefHeight(40);
        txtSearch.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #e9ecef;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 0 15;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 13px;"
        );
        HBox.setHgrow(txtSearch, Priority.ALWAYS); // Giãn tối đa

        // 2. ComboBox Lọc
        cbSort = new ComboBox<>();
        cbSort.getItems().addAll("Tất cả xếp loại", "Khách thường", "Khách VIP");
        cbSort.getSelectionModel().selectFirst();
        cbSort.setPrefHeight(40);
        cbSort.setPrefWidth(180);
        cbSort.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #e9ecef;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-font-family: 'Segoe UI';"
        );

        // 3. Separator
        Separator sep = new Separator();
        sep.setOrientation(javafx.geometry.Orientation.VERTICAL);

        // 4. Nút Thêm Mới
        Button btnAdd = new Button("✚ Thêm Khách Hàng");
        btnAdd.setPrefHeight(40);
        stylePrimaryButton(btnAdd);

        // Events
        txtSearch.textProperty().addListener((obs, oldValue, newValue) -> applyFilters());
        cbSort.valueProperty().addListener((obs, oldValue, newValue) -> applyFilters());
        btnAdd.setOnAction(e -> openAddModal());

        searchBar.getChildren().addAll(txtSearch, cbSort, sep, btnAdd);
        return searchBar;
    }

    // =============================================================
    // 📋 TABLE SECTION (STYLE CARD TRẮNG)
    // =============================================================
    private VBox createModernTableSection() {
        VBox tableContainer = new VBox();
        tableContainer.setPadding(new Insets(5));
        tableContainer.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);"
        );

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // CSS Style cho Table để bỏ viền xanh mặc định và làm đẹp header
        table.setStyle(
                "-fx-background-color: white;" +
                        "-fx-base: white;" +
                        "-fx-control-inner-background: white;" +
                        "-fx-table-cell-border-color: transparent;" +
                        "-fx-table-header-border-color: transparent;" +
                        "-fx-padding: 5;"
        );

        // Tạo cột
        TableColumn<KhachHang, Number> colSTT = new TableColumn<>("STT");
        TableColumn<KhachHang, String> colID = new TableColumn<>("Mã KH");
        TableColumn<KhachHang, String> colName = new TableColumn<>("Tên khách hàng");
        TableColumn<KhachHang, String> colPhone = new TableColumn<>("Số điện thoại");
        TableColumn<KhachHang, Double> colPoints = new TableColumn<>("Điểm tích lũy");
        TableColumn<KhachHang, String> colType = new TableColumn<>("Xếp loại");
        TableColumn<KhachHang, Void> colAction = new TableColumn<>("Thao tác");

        // Cấu hình các cột
        colSTT.setCellValueFactory(col -> new ReadOnlyObjectWrapper<>(table.getItems().indexOf(col.getValue()) + 1));
        colSTT.setStyle("-fx-alignment: CENTER; -fx-font-family: 'Segoe UI';");
        colSTT.setMaxWidth(50);

        colID.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getMaKhachHang()));
        colID.setStyle("-fx-font-family: 'Segoe UI'; -fx-alignment: CENTER_LEFT;");

        colName.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getTenKhachHang()));
        colName.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-alignment: CENTER_LEFT;");

        colPhone.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getSoDienThoai()));
        colPhone.setStyle("-fx-font-family: 'Segoe UI'; -fx-alignment: CENTER;");

        colPoints.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getDiemTichLuy()));
        colPoints.setStyle("-fx-font-family: 'Segoe UI'; -fx-alignment: CENTER_RIGHT; -fx-text-fill: #2980b9; -fx-font-weight: bold;");

        // Cột xếp loại (Custom Style)
        colType.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(getXepLoaiKhachHang(cell.getValue().getDiemTichLuy())));
        colType.setCellFactory(column -> new TableCell<KhachHang, String>() {
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

                    if ("VIP".equals(item)) {
                        lbl.setStyle(lbl.getStyle() + "-fx-background-color: #fff3cd; -fx-text-fill: #856404;"); // Vàng
                    } else {
                        lbl.setStyle(lbl.getStyle() + "-fx-background-color: #d1ecf1; -fx-text-fill: #0c5460;"); // Xanh nhạt
                    }
                    setGraphic(lbl);
                    setAlignment(Pos.CENTER);
                    setText(null);
                }
            }
        });

        // Cột hành động
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit = new Button("Sửa");

            {
                btnEdit.setStyle(
                        "-fx-background-color: #e2e6ea; -fx-text-fill: #495057; -fx-font-weight: bold; " +
                                "-fx-background-radius: 6; -fx-font-size: 11px; -fx-cursor: hand;"
                );
                // Hover effect cho nút sửa
                btnEdit.setOnMouseEntered(e -> btnEdit.setStyle("-fx-background-color: #dae0e5; -fx-text-fill: #212529; -fx-font-weight: bold; -fx-background-radius: 6; -fx-font-size: 11px; -fx-cursor: hand;"));
                btnEdit.setOnMouseExited(e -> btnEdit.setStyle("-fx-background-color: #e2e6ea; -fx-text-fill: #495057; -fx-font-weight: bold; -fx-background-radius: 6; -fx-font-size: 11px; -fx-cursor: hand;"));

                btnEdit.setOnAction(e -> {
                    KhachHang c = getTableView().getItems().get(getIndex());
                    openEditModal(c);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(btnEdit);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });

        table.getColumns().addAll(colSTT, colID, colName, colPhone, colPoints, colType, colAction);
        table.setItems(data);

        // Thêm bảng vào container
        tableContainer.getChildren().add(table);
        return tableContainer;
    }

    // =============================================================
    // 🔄 LOGIC LOAD & FILTER
    // =============================================================
    private void loadDataFromDatabase() {
        data.clear();
        data.addAll(khachHangDAO.getAllKhachHang());
    }

    private void applyFilters() {
        String searchKeyword = txtSearch.getText().trim();
        String filterType = cbSort.getValue();

        java.util.List<KhachHang> allCustomers = khachHangDAO.getAllKhachHang();

        if (!searchKeyword.isEmpty()) {
            allCustomers = allCustomers.stream()
                    .filter(kh -> kh.getTenKhachHang().toLowerCase().contains(searchKeyword.toLowerCase()) ||
                            kh.getSoDienThoai().contains(searchKeyword))
                    .toList();
        }

        if (filterType != null && !filterType.equals("Tất cả xếp loại")) {
            allCustomers = allCustomers.stream()
                    .filter(kh -> {
                        String xepLoai = getXepLoaiKhachHang(kh.getDiemTichLuy());
                        if ("Khách VIP".equals(filterType)) return "VIP".equals(xepLoai);
                        else if ("Khách thường".equals(filterType)) return "Thường".equals(xepLoai);
                        return true;
                    })
                    .toList();
        }

        data.setAll(allCustomers);
    }

    private String getXepLoaiKhachHang(double diemTichLuy) {
        return diemTichLuy >= 200 ? "VIP" : "Thường";
    }

    // =============================================================
    // 🧰 MODAL STYLE
    // =============================================================
    private void openAddModal() {
        openModal("Thêm Khách Hàng Mới", null);
    }

    private void openEditModal(KhachHang kh) {
        openModal("Cập Nhật Thông Tin", kh);
    }

    private void openModal(String title, KhachHang kh) {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setTitle(title);

        Label lblTitle = new Label(title.toUpperCase());
        lblTitle.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #082744;");
        lblTitle.setAlignment(Pos.CENTER);

        GridPane form = createCustomerForm(kh);
        HBox buttons = createModalButtons(modal, kh, form);

        VBox layout = new VBox(20, lblTitle, new Separator(), form, buttons);
        layout.setPadding(new Insets(30));
        layout.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 20, 0, 0, 5);"
        );

        Scene scene = new Scene(layout, 550, 480);
        modal.setScene(scene);
        modal.showAndWait();
    }

    private GridPane createCustomerForm(KhachHang kh) {
        GridPane form = new GridPane();
        form.setVgap(20);
        form.setHgap(15);

        // Tạo các label và input field
        String maKhachHang = kh == null ? khachHangDAO.generateMaKhachHang() : kh.getMaKhachHang();
        TextField txtMa = new TextField(maKhachHang);
        txtMa.setEditable(false);
        styleReadonlyField(txtMa);

        TextField txtTen = new TextField(kh == null ? "" : kh.getTenKhachHang());
        styleInputField(txtTen);

        TextField txtSDT = new TextField(kh == null ? "" : kh.getSoDienThoai());
        styleInputField(txtSDT);

        double diemTichLuy = kh == null ? 0 : kh.getDiemTichLuy();
        TextField txtDiem = new TextField(String.valueOf(diemTichLuy));
        txtDiem.setEditable(false);
        styleReadonlyField(txtDiem);

        String xepLoai = getXepLoaiKhachHang(diemTichLuy);
        Label lblRankBadge = new Label(xepLoai);
        lblRankBadge.setPadding(new Insets(5, 15, 5, 15));
        if ("VIP".equals(xepLoai)) {
            lblRankBadge.setStyle("-fx-background-color: #FFD700; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 15;");
        } else {
            lblRankBadge.setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #7f8c8d; -fx-font-weight: bold; -fx-background-radius: 15;");
        }

        // Add rows
        addFormRow(form, "Mã khách hàng:", txtMa, 0);
        addFormRow(form, "Họ và tên:", txtTen, 1);
        addFormRow(form, "Số điện thoại:", txtSDT, 2);
        addFormRow(form, "Điểm tích lũy:", txtDiem, 3);

        Label lblRankTitle = new Label("Xếp loại hiện tại:");
        lblRankTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        form.add(lblRankTitle, 0, 4);
        form.add(lblRankBadge, 1, 4);

        return form;
    }

    private void addFormRow(GridPane grid, String labelText, Node field, int row) {
        Label lbl = new Label(labelText);
        lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        lbl.setStyle("-fx-text-fill: #535c68;");
        grid.add(lbl, 0, row);
        grid.add(field, 1, row);
    }

    private HBox createModalButtons(Stage modal, KhachHang kh, GridPane form) {
        Button btnClose = new Button("Đóng");
        styleSecondaryButton(btnClose);

        Button btnSave = new Button("Lưu Thông Tin");
        stylePrimaryButton(btnSave); // Dùng style primary giống nút Thêm

        btnClose.setOnAction(e -> modal.close());

        btnSave.setOnAction(e -> {
            // Logic lưu (Giữ nguyên logic cũ của bạn)
            TextField txtMa = (TextField) getNodeFromGridPane(form, 1, 0);
            TextField txtTen = (TextField) getNodeFromGridPane(form, 1, 1);
            TextField txtSDT = (TextField) getNodeFromGridPane(form, 1, 2);

            String ma = txtMa.getText();
            String ten = txtTen.getText().trim();
            String sdt = txtSDT.getText().trim();

            if (ten.isEmpty() || sdt.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Thiếu thông tin", "Vui lòng nhập tên và số điện thoại!");
                return;
            }
            if (!sdt.matches("\\d{10,11}")) {
                showAlert(Alert.AlertType.ERROR, "Sai định dạng", "Số điện thoại phải có 10-11 chữ số!");
                return;
            }

            try {
                if (kh == null) { // Thêm mới
                    if (khachHangDAO.isSoDienThoaiExists(sdt)) {
                        showAlert(Alert.AlertType.ERROR, "Trùng lặp", "Số điện thoại đã tồn tại!");
                        return;
                    }
                    KhachHang newKh = new KhachHang(ma, ten, sdt, 0);
                    if (khachHangDAO.addKhachHang(newKh)) {
                        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã thêm khách hàng mới!");
                        loadDataFromDatabase();
                        modal.close();
                    }
                } else { // Cập nhật
                    if (khachHangDAO.isSoDienThoaiExistsForOther(sdt, ma)) {
                        showAlert(Alert.AlertType.ERROR, "Trùng lặp", "SĐT đã thuộc về khách hàng khác!");
                        return;
                    }
                    kh.setTenKhachHang(ten);
                    kh.setSoDienThoai(sdt);
                    if (khachHangDAO.updateKhachHang(kh)) {
                        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật thông tin thành công!");
                        loadDataFromDatabase();
                        modal.close();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox buttons = new HBox(15, btnClose, btnSave);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        buttons.setPadding(new Insets(20, 0, 0, 0));
        return buttons;
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    // =============================================================
    // 🎨 STYLING HELPER METHODS
    // =============================================================

    // Nút chính (Màu xanh đậm - giống nút Xuất báo cáo bên Thống kê)
    private void stylePrimaryButton(Button btn) {
        btn.setStyle(
                "-fx-background-color: #082744; -fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-background-radius: 8; -fx-padding: 8 18; -fx-font-family: 'Segoe UI'; -fx-cursor: hand;"
        );
        addHoverEffect(btn);
    }

    // Nút phụ (Màu xám)
    private void styleSecondaryButton(Button btn) {
        btn.setStyle(
                "-fx-background-color: #ecf0f1; -fx-text-fill: #2c3e50; -fx-font-weight: bold; " +
                        "-fx-background-radius: 8; -fx-padding: 8 18; -fx-font-family: 'Segoe UI'; -fx-cursor: hand;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #bdc3c7; -fx-text-fill: #2c3e50; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 18; -fx-font-family: 'Segoe UI'; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #2c3e50; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 18; -fx-font-family: 'Segoe UI'; -fx-cursor: hand;"));
    }

    private void styleInputField(TextField tf) {
        tf.setStyle(
                "-fx-background-color: white; -fx-border-color: #ced4da; -fx-border-radius: 5; " +
                        "-fx-padding: 8; -fx-font-family: 'Segoe UI';"
        );
        tf.setPrefWidth(300);
    }

    private void styleReadonlyField(TextField tf) {
        tf.setStyle(
                "-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 5; " +
                        "-fx-padding: 8; -fx-text-fill: #6c757d; -fx-font-family: 'Segoe UI';"
        );
        tf.setPrefWidth(300);
    }

    private void addHoverEffect(Node node) {
        node.setOnMouseEntered(e -> node.setStyle(node.getStyle() + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1); -fx-scale-x: 1.02; -fx-scale-y: 1.02;"));
        node.setOnMouseExited(e -> {
            node.setScaleX(1.0);
            node.setScaleY(1.0);
        });
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}