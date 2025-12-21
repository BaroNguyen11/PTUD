
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
        String maKhachHang = kh == null ? khachHangDAO.taoMaKhachHangMoi() : kh.getMaKhachHang();
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
            TextField txtTen = (TextField) getNodeFromGridPane(form, 1, 1);
            TextField txtSDT = (TextField) getNodeFromGridPane(form, 1, 2);

            String ten = txtTen.getText().trim();
            String sdt = txtSDT.getText().trim();
            if (ten.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Thiếu thông tin", "Vui lòng nhập tên khách hàng!");
                txtTen.requestFocus(); // Focus vào ô lỗi
                return;
            }
            if (!ten.matches("^[\\p{L}\\s]+$")) {
                showAlert(Alert.AlertType.ERROR, "Sai định dạng",
                        "Tên khách hàng chỉ được chứa chữ cái, không được chứa số hoặc ký tự đặc biệt!");
                txtTen.requestFocus();
                return;
            }
            if (ten.isEmpty() || sdt.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Thiếu thông tin", "Vui lòng nhập tên và số điện thoại!");
                return;
            }

            if (!sdt.matches("^0[35789]\\d{8}$")) {
                showAlert(Alert.AlertType.ERROR, "Sai định dạng", "Số điện thoại không hợp lệ (Phải là 10 số, bắt đầu là 03,05,07,09)!");
                return;
            }

            try {
                if (kh == null) {
                    // ================== TRƯỜNG HỢP THÊM MỚI ==================

                    // Kiểm tra trùng SĐT
                    if (khachHangDAO.isSoDienThoaiExists(sdt)) {
                        showAlert(Alert.AlertType.ERROR, "Trùng lặp", "Số điện thoại này đã tồn tại trong hệ thống!");
                        return;
                    }

                    // --- [QUAN TRỌNG] TẠO MÃ KHÁCH HÀNG MỚI TẠI ĐÂY ---
                    // Gọi hàm tạo mã theo ngày mà bạn vừa thêm vào DAO
                    String newMa = khachHangDAO.taoMaKhachHangMoi();

                    // Tạo đối tượng với mã vừa sinh ra
                    KhachHang newKh = new KhachHang(newMa, ten, sdt, 0);

                    // Gọi hàm DAO để lưu (Hàm addKhachHang trong DAO chỉ cần INSERT là đủ)
                    if (khachHangDAO.addKhachHang(newKh)) {
                        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã thêm khách hàng: " + newMa);
                        loadDataFromDatabase(); // Load lại bảng
                        modal.close();          // Đóng cửa sổ
                    }
                } else {
                    // ================== TRƯỜNG HỢP CẬP NHẬT ==================

                    // Lấy mã cũ từ đối tượng (Không lấy từ TextField để an toàn)
                    String maCu = kh.getMaKhachHang();

                    // Kiểm tra trùng SĐT với người khác
                    if (khachHangDAO.isSoDienThoaiExistsForOther(sdt, maCu)) {
                        showAlert(Alert.AlertType.ERROR, "Trùng lặp", "SĐT này đang thuộc về khách hàng khác!");
                        return;
                    }

                    // Cập nhật thông tin vào đối tượng cũ
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
                showAlert(Alert.AlertType.ERROR, "Lỗi hệ thống", "Có lỗi xảy ra: " + ex.getMessage());
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