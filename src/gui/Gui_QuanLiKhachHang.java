package gui;

import dao.KhachHang_DAO;
import entity.KhachHang;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Gui_QuanLiKhachHang extends BorderPane {
    private final ObservableList<KhachHang> data = FXCollections.observableArrayList();
    private final KhachHang_DAO khachHangDAO = new KhachHang_DAO();
    private TableView<KhachHang> table;
    private ComboBox<String> cbSort;
    private TextField txtSearch;

    // ====================== ✅ CONSTRUCTOR ======================
    public Gui_QuanLiKhachHang() {
        // Tạo layout
        VBox searchSection = createSearchSection();
        table = createCustomerTable();

        // Gán vào BorderPane
        setTop(searchSection);
        setCenter(table);

        setPadding(new Insets(20));
        setStyle("-fx-background-color: #f7f9fc;");

        // 🔹 Load dữ liệu từ database sau khi tạo giao diện
        loadDataFromDatabase();
    }

    // =============================================================
    // 🔍 THANH TÌM KIẾM + THÊM
    // =============================================================
    private VBox createSearchSection() {
        Label lblSearchTitle = new Label("Tìm kiếm khách hàng");
        lblSearchTitle.setStyle("""
                -fx-font-size: 13px;
                -fx-font-weight: bold;
                -fx-text-fill: #14274e;
                """);

        // Ô tìm kiếm có icon
        Image imgSearch = new Image(getClass().getResource("/img/search-normal.png").toExternalForm());
        ImageView iconSearch = new ImageView(imgSearch);
        iconSearch.setFitWidth(16);
        iconSearch.setFitHeight(16);

        txtSearch = new TextField();
        txtSearch.setPromptText("Tìm theo Tên/SĐT...");
        txtSearch.setPrefWidth(500);
        txtSearch.setStyle("""
                -fx-background-color: white;
                -fx-border-color: #ccc;
                -fx-border-radius: 8;
                -fx-background-radius: 8;
                -fx-padding: 6 10 6 30;
                """);

        StackPane searchBox = new StackPane(txtSearch, iconSearch);
        StackPane.setAlignment(iconSearch, Pos.CENTER_LEFT);
        StackPane.setMargin(iconSearch, new Insets(0, 0, 0, 8));

        // ComboBox lọc
        cbSort = new ComboBox<>();
        cbSort.getItems().addAll("Tất cả xếp loại", "Khách thường", "Khách VIP");
        cbSort.getSelectionModel().selectFirst();
        cbSort.setStyle("-fx-padding: 6; -fx-background-radius: 8;");
        cbSort.setPrefWidth(180);

        // Separator
        Separator sep = new Separator();
        sep.setOrientation(javafx.geometry.Orientation.VERTICAL);
        sep.setPrefHeight(30);

        // Nút thêm
        Button btnAdd = new Button("➕ Thêm khách hàng mới");
        stylePrimaryButton(btnAdd);
        btnAdd.setOnAction(e -> openAddModal());

        // Gộp toàn bộ phần trên
        HBox searchBar = new HBox(20, searchBox, cbSort, sep, btnAdd);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        searchBar.setPrefHeight(50);
        HBox.setHgrow(searchBox, Priority.ALWAYS);
        txtSearch.setMaxWidth(Double.MAX_VALUE);
        searchBar.setPadding(new Insets(0, 0, 5, 0));

        // 🔹 Thêm sự kiện tìm kiếm
        txtSearch.textProperty().addListener((obs, oldValue, newValue) -> {
            applyFilters();
        });

        // 🔹 Thêm sự kiện lọc
        cbSort.valueProperty().addListener((obs, oldValue, newValue) -> {
            applyFilters();
        });

        return new VBox(5, lblSearchTitle, searchBar);
    }

    // =============================================================
    // 📋 TẠO BẢNG KHÁCH HÀNG
    // =============================================================
    private TableView<KhachHang> createCustomerTable() {
        TableView<KhachHang> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<KhachHang, Number> colSTT = new TableColumn<>("STT");
        TableColumn<KhachHang, String> colID = new TableColumn<>("Mã khách hàng");
        TableColumn<KhachHang, String> colName = new TableColumn<>("Tên khách hàng");
        TableColumn<KhachHang, String> colPhone = new TableColumn<>("Số điện thoại");
        TableColumn<KhachHang, Double> colPoints = new TableColumn<>("Điểm tích lũy");
        TableColumn<KhachHang, String> colType = new TableColumn<>("Xếp loại");
        TableColumn<KhachHang, Void> colAction = new TableColumn<>("Hành động");

        colSTT.setCellValueFactory(col -> new ReadOnlyObjectWrapper<>(table.getItems().indexOf(col.getValue()) + 1));
        colSTT.setStyle("-fx-alignment: CENTER;");

        colID.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getMaKhachHang()));
        colName.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getTenKhachHang()));
        colPhone.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getSoDienThoai()));
        colPoints.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getDiemTichLuy()));
        colPoints.setStyle("-fx-alignment: CENTER_LEFT;");

        // Cột xếp loại
        colType.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(getXepLoaiKhachHang(cell.getValue().getDiemTichLuy())));
        colType.setCellFactory(column -> new TableCell<KhachHang, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("VIP".equals(item)) {
                        setStyle("-fx-text-fill: #d4af37; -fx-font-weight: bold; -fx-alignment: CENTER;");
                    } else {
                        setStyle("-fx-text-fill: #666; -fx-font-weight: bold; -fx-alignment: CENTER;");
                    }
                }
            }
        });

        // Cột hành động
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit = new Button();
            private final HBox box = new HBox();

            {
                box.setAlignment(Pos.CENTER);
                box.setPadding(new Insets(5));

                Image imgEdit = new Image(getClass().getResource("/img/ChinhSua.png").toExternalForm());
                ImageView imgView = new ImageView(imgEdit);
                imgView.setFitWidth(18);
                imgView.setFitHeight(18);
                btnEdit.setGraphic(imgView);
                btnEdit.setStyle("-fx-background-color: transparent;");
                btnEdit.setCursor(Cursor.HAND);
                btnEdit.setOnAction(e -> {
                    KhachHang c = getTableView().getItems().get(getIndex());
                    openEditModal(c);
                });
                box.getChildren().add(btnEdit);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });

        table.getColumns().addAll(colSTT, colID, colName, colPhone, colPoints, colType, colAction);
        table.setItems(data);

        table.setStyle("""
                -fx-background-color: white;
                -fx-border-color: #ccc;
                -fx-border-radius: 8;
                -fx-font-size: 14px;
                """);

        return table;
    }

    // =============================================================
    // 🔄 HÀM LOAD DỮ LIỆU VÀ LỌC
    // =============================================================
    private void loadDataFromDatabase() {
        data.clear();
        data.addAll(khachHangDAO.getAllKhachHang());
    }

    private void applyFilters() {
        String searchKeyword = txtSearch.getText().trim();
        String filterType = cbSort.getValue();

        // Lấy toàn bộ dữ liệu từ database
        java.util.List<KhachHang> allCustomers = khachHangDAO.getAllKhachHang();

        // Lọc theo từ khóa tìm kiếm
        if (!searchKeyword.isEmpty()) {
            allCustomers = allCustomers.stream()
                    .filter(kh ->
                            kh.getTenKhachHang().toLowerCase().contains(searchKeyword.toLowerCase()) ||
                                    kh.getSoDienThoai().contains(searchKeyword)
                    )
                    .toList();
        }

        // Lọc theo xếp loại
        if (filterType != null && !filterType.equals("Tất cả xếp loại")) {
            allCustomers = allCustomers.stream()
                    .filter(kh -> {
                        String xepLoai = getXepLoaiKhachHang(kh.getDiemTichLuy());
                        if ("Khách VIP".equals(filterType)) {
                            return "VIP".equals(xepLoai);
                        } else if ("Khách thường".equals(filterType)) {
                            return "Thường".equals(xepLoai);
                        }
                        return true;
                    })
                    .toList();
        }

        data.setAll(allCustomers);
    }

    // =============================================================
    // 🏷️ HÀM XẾP LOẠI KHÁCH HÀNG
    // =============================================================
    private String getXepLoaiKhachHang(double diemTichLuy) {
        return diemTichLuy >= 200 ? "VIP" : "Thường";
    }

    // =============================================================
    // 🧰 MODAL THÊM / SỬA KHÁCH HÀNG
    // =============================================================
    private void openAddModal() {
        openModal("Thêm khách hàng mới", null);
    }

    private void openEditModal(KhachHang kh) {
        openModal("Chỉnh sửa thông tin khách hàng", kh);
    }

    private void openModal(String title, KhachHang kh) {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setTitle(title);

        Label lblTitle = new Label(title);
        lblTitle.setStyle("""
                -fx-font-size: 22px;
                -fx-font-weight: bold;
                -fx-text-fill: #14274e;
                """);
        lblTitle.setAlignment(Pos.CENTER);

        Separator line = new Separator();
        GridPane form = createCustomerForm(kh);
        HBox buttons = createModalButtons(modal, kh, form);

        VBox layout = new VBox(20, lblTitle, line, form, buttons);
        layout.setPadding(new Insets(25));
        layout.setStyle("""
                -fx-background-color: white;
                -fx-border-color: #ddd;
                -fx-border-radius: 10;
                -fx-background-radius: 10;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 5);
                """);

        Scene scene = new Scene(layout, 550, 450);
        modal.setScene(scene);
        modal.showAndWait();
    }

    private GridPane createCustomerForm(KhachHang kh) {
        Label lblMa = new Label("Mã khách hàng:");
        Label lblTen = new Label("Họ tên khách hàng:");
        Label lblSDT = new Label("Số điện thoại:");
        Label lblDiem = new Label("Điểm tích lũy:");
        Label lblXepLoai = new Label("Xếp loại:");

        for (Label lbl : new Label[]{lblMa, lblTen, lblSDT, lblDiem, lblXepLoai})
            lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");

        // Tạo mã tự động nếu thêm mới
        String maKhachHang = kh == null ? khachHangDAO.generateMaKhachHang() : kh.getMaKhachHang();

        TextField txtMa = new TextField(maKhachHang);
        txtMa.setEditable(false);
        styleReadonlyField(txtMa);

        TextField txtTen = new TextField(kh == null ? "" : kh.getTenKhachHang());
        TextField txtSDT = new TextField(kh == null ? "" : kh.getSoDienThoai());

        double diemTichLuy = kh == null ? 0 : kh.getDiemTichLuy();
        TextField txtDiem = new TextField(String.valueOf(diemTichLuy));
        txtDiem.setEditable(false);
        styleReadonlyField(txtDiem);

        // Hiển thị xếp loại
        String xepLoai = getXepLoaiKhachHang(diemTichLuy);
        TextField txtXepLoai = new TextField(xepLoai);
        txtXepLoai.setEditable(false);
        if ("VIP".equals(xepLoai)) {
            txtXepLoai.setStyle("""
                -fx-opacity: 0.8;
                -fx-background-color: #fff8e1;
                -fx-border-color: #d4af37;
                -fx-border-radius: 5;
                -fx-background-radius: 5;
                -fx-padding: 6 10;
                -fx-text-fill: #d4af37;
                -fx-font-weight: bold;
                """);
        } else {
            styleReadonlyField(txtXepLoai);
        }

        GridPane form = new GridPane();
        form.setVgap(18);
        form.setHgap(20);
        form.setPadding(new Insets(10, 40, 10, 40));
        form.addRow(0, lblMa, txtMa);
        form.addRow(1, lblTen, txtTen);
        form.addRow(2, lblSDT, txtSDT);
        form.addRow(3, lblDiem, txtDiem);
        form.addRow(4, lblXepLoai, txtXepLoai);
        return form;
    }

    private HBox createModalButtons(Stage modal, KhachHang kh, GridPane form) {
        Button btnClose = new Button("Đóng");
        Button btnSave = new Button("Lưu");

        styleSecondaryButton(btnClose);
        stylePrimaryDarkButton(btnSave);

        btnClose.setOnAction(e -> modal.close());

        btnSave.setOnAction(e -> {
            // Lấy các control từ form
            TextField txtMa = (TextField) form.getChildren().get(1); // cột thứ 2 của dòng 0
            TextField txtTen = (TextField) form.getChildren().get(3); // cột thứ 2 của dòng 1
            TextField txtSDT = (TextField) form.getChildren().get(5); // cột thứ 2 của dòng 2

            String ma = txtMa.getText();
            String ten = txtTen.getText().trim();
            String sdt = txtSDT.getText().trim();

            // Validation
            if (ten.isEmpty() || sdt.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            if (!sdt.matches("\\d{10,11}")) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Số điện thoại phải có 10-11 chữ số!");
                return;
            }

            try {
                if (kh == null) {
                    // Thêm mới
                    if (khachHangDAO.isSoDienThoaiExists(sdt)) {
                        showAlert(Alert.AlertType.ERROR, "Lỗi", "Số điện thoại đã tồn tại!");
                        return;
                    }

                    KhachHang newKh = new KhachHang(ma, ten, sdt, 0);
                    if (khachHangDAO.addKhachHang(newKh)) {
                        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm khách hàng thành công!");
                        loadDataFromDatabase();
                        modal.close();
                    }
                } else {
                    // Cập nhật
                    if (khachHangDAO.isSoDienThoaiExistsForOther(sdt, ma)) {
                        showAlert(Alert.AlertType.ERROR, "Lỗi", "Số điện thoại đã tồn tại cho khách hàng khác!");
                        return;
                    }

                    kh.setTenKhachHang(ten);
                    kh.setSoDienThoai(sdt);
                    if (khachHangDAO.updateKhachHang(kh)) {
                        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật khách hàng thành công!");
                        loadDataFromDatabase();
                        modal.close();
                    }
                }
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Có lỗi xảy ra: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        HBox buttons = new HBox(15, btnClose, btnSave);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(15, 40, 25, 40));
        return buttons;
    }

    // =============================================================
    // 🎨 STYLE HỖ TRỢ
    // =============================================================
    private void stylePrimaryButton(Button btn) {
        btn.setStyle("""
                -fx-background-color: white;
                -fx-border-color: #ccc;
                -fx-border-radius: 8;
                -fx-background-radius: 8;
                -fx-font-weight: 600;
                -fx-padding: 8 18;
                -fx-cursor: hand;
                """);
    }

    private void styleReadonlyField(TextField tf) {
        tf.setStyle("""
                -fx-opacity: 0.8;
                -fx-background-color: #f3f3f3;
                -fx-border-color: #ccc;
                -fx-border-radius: 5;
                -fx-background-radius: 5;
                -fx-padding: 6 10;
                """);
    }

    private void styleSecondaryButton(Button btn) {
        btn.setStyle("""
                -fx-background-color: #999;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 8 25;
                -fx-background-radius: 6;
                """);
    }

    private void stylePrimaryDarkButton(Button btn) {
        btn.setStyle("""
                -fx-background-color: #14274e;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 8 25;
                -fx-background-radius: 6;
                """);
    }

    // =============================================================
    // 🔔 HÀM HIỂN THỊ THÔNG BÁO
    // =============================================================
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}