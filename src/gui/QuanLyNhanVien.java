package gui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

import dao.NhanVien_DAO;
import entity.NhanVien;
import javafx.application.Platform;
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

public class QuanLyNhanVien extends BorderPane {

    private final ObservableList<NhanVien> data = FXCollections.observableArrayList();
    private final NhanVien_DAO nhanVienDAO = new NhanVien_DAO();
    private TableView<NhanVien> table;
    private ComboBox<String> cbSort;
    private TextField txtSearch;

    public QuanLyNhanVien() {
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #f9f9f9;");

        // Thanh tìm kiếm
        VBox searchSection = createSearchSection(new Stage());

        // Bảng nhân viên
        table = createEmployeeTable(new Stage());
        VBox.setVgrow(table, Priority.ALWAYS);

        mainLayout.getChildren().addAll(searchSection, table);

        // Đặt layout này vào trung tâm BorderPane
        this.setCenter(mainLayout);

        // Load dữ liệu từ database
        loadDataFromDatabase();
    }

    // =============================================================
    // 🔍 THANH TÌM KIẾM
    // =============================================================
    private VBox createSearchSection(Stage primaryStage) {
        Label lblSearchTitle = new Label("Tìm kiếm nhân viên");
        lblSearchTitle.setStyle("""
            -fx-font-size: 13px;
            -fx-font-weight: bold;
            -fx-text-fill: #14274e;
        """);

        txtSearch = new TextField();
        txtSearch.setPromptText("Nhập mã hoặc tên nhân viên...");
        txtSearch.setStyle("""
            -fx-background-color: white;
            -fx-border-color: #ccc;
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-padding: 8 10 8 30;
        """);

        ImageView iconSearch = new ImageView(new Image(getClass().getResource("/img/search-normal.png").toExternalForm()));
        iconSearch.setFitWidth(16);
        iconSearch.setFitHeight(16);

        StackPane searchBox = new StackPane(txtSearch, iconSearch);
        StackPane.setAlignment(iconSearch, Pos.CENTER_LEFT);
        StackPane.setMargin(iconSearch, new Insets(0, 0, 0, 8));
        HBox.setHgrow(searchBox, Priority.ALWAYS);
        txtSearch.setMaxWidth(Double.MAX_VALUE);

        cbSort = new ComboBox<>();
        cbSort.getItems().addAll("Tất cả chức vụ", "Quản lý", "Nhân viên");
        cbSort.getSelectionModel().selectFirst();
        cbSort.setPrefHeight(38);
        cbSort.setStyle("-fx-padding: 6; -fx-background-radius: 8;");

        Button btnAdd = new Button("➕ Thêm nhân viên mới");
        stylePrimaryButton(btnAdd);
        btnAdd.setPrefHeight(38);
        btnAdd.setOnAction(e -> openAddModal(primaryStage));

        Separator sep = new Separator();
        sep.setOrientation(javafx.geometry.Orientation.VERTICAL);
        sep.setPrefHeight(28);

        HBox searchBar = new HBox(20, searchBox, cbSort, sep, btnAdd);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        searchBar.setPadding(new Insets(0, 0, 5, 0));

        // Thêm sự kiện tìm kiếm và lọc
        txtSearch.textProperty().addListener((obs, oldValue, newValue) -> {
            applyFilters();
        });

        cbSort.valueProperty().addListener((obs, oldValue, newValue) -> {
            applyFilters();
        });

        return new VBox(5, lblSearchTitle, searchBar);
    }

    // =============================================================
    // 🔄 HÀM LOAD DỮ LIỆU VÀ LỌC
    // =============================================================
    private void loadDataFromDatabase() {
        data.clear();
        data.addAll(nhanVienDAO.getAllNhanVien());
    }

    private void applyFilters() {
        String searchKeyword = txtSearch.getText().trim();
        String filterType = cbSort.getValue();
        
        // Lấy tất cả dữ liệu từ database
        java.util.List<NhanVien> allEmployees = nhanVienDAO.getAllNhanVien();
        java.util.List<NhanVien> filteredEmployees = new ArrayList<>();
        
        // Áp dụng bộ lọc
        for (NhanVien nv : allEmployees) {
            boolean matchesSearch = searchKeyword.isEmpty() ||
                    nv.getMaNhanVien().toLowerCase().contains(searchKeyword.toLowerCase()) ||
                    nv.getTenNhanVien().toLowerCase().contains(searchKeyword.toLowerCase()) ||
                    nv.getSoDienThoai().contains(searchKeyword);
            
            boolean matchesRole = filterType == null || 
                    filterType.equals("Tất cả chức vụ") ||
                    filterType.equals(nv.getChucVu());
            
            if (matchesSearch && matchesRole) {
                filteredEmployees.add(nv);
            }
        }
        
        // Cập nhật dữ liệu theo cách manual
        data.clear();
        if (!filteredEmployees.isEmpty()) {
            data.addAll(filteredEmployees);
        }
        
        // Force refresh table
        table.getColumns().get(0).setVisible(false);
        table.getColumns().get(0).setVisible(true);
    }
    // =============================================================
    // 📋 BẢNG NHÂN VIÊN
    // =============================================================
    private TableView<NhanVien> createEmployeeTable(Stage primaryStage) {
        TableView<NhanVien> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<NhanVien, Number> colSTT = new TableColumn<>("STT");
        TableColumn<NhanVien, String> colID = new TableColumn<>("Mã nhân viên");
        TableColumn<NhanVien, String> colName = new TableColumn<>("Tên nhân viên");
        TableColumn<NhanVien, String> colPhone = new TableColumn<>("Số điện thoại");
        TableColumn<NhanVien, String> colCCCD = new TableColumn<>("CCCD");
        TableColumn<NhanVien, String> colRole = new TableColumn<>("Chức vụ");
        TableColumn<NhanVien, LocalDate> colBirth = new TableColumn<>("Ngày sinh");
        TableColumn<NhanVien, LocalDate> colStart = new TableColumn<>("Ngày vào làm");
        TableColumn<NhanVien, LocalDate> colEnd = new TableColumn<>("Ngày thôi việc");
        TableColumn<NhanVien, Void> colAction = new TableColumn<>("Hành động");

        colSTT.setCellValueFactory(col -> new ReadOnlyObjectWrapper<>(table.getItems().indexOf(col.getValue()) + 1));
        colSTT.setStyle("-fx-alignment: CENTER;");
        
        colID.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getMaNhanVien()));
        colName.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getTenNhanVien()));
        colPhone.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getSoDienThoai()));
        colCCCD.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getCCCD()));
        colRole.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getChucVu()));
        colBirth.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getNgaySinh()));
        colStart.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getNgayVaoLam()));
        colEnd.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getNgayThoiViec()));

        // Format ngày tháng
        colBirth.setCellFactory(column -> new TableCell<NhanVien, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.toString());
            }
        });

        colStart.setCellFactory(column -> new TableCell<NhanVien, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.toString());
            }
        });

        colEnd.setCellFactory(column -> new TableCell<NhanVien, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                
                // QUAN TRỌNG: Luôn clear trước
                setText(null);
                setStyle("");
                
                if (!empty && getTableRow() != null && getTableRow().getItem() != null) {
                    NhanVien nv = getTableRow().getItem();
                    if (nv.getNgayThoiViec() == null) {
                        setText("Đang làm việc");
                        setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    } else {
                        setText(nv.getNgayThoiViec().toString());
                        setStyle("-fx-text-fill: red;");
                    }
                } else {
                    setText("");
                    setStyle("");
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
                ImageView imgView = new ImageView(new Image(getClass().getResource("/img/ChinhSua.png").toExternalForm()));
                imgView.setFitWidth(18);
                imgView.setFitHeight(18);
                btnEdit.setGraphic(imgView);
                btnEdit.setStyle("-fx-background-color: transparent;");
                btnEdit.setCursor(Cursor.HAND);
                btnEdit.setOnAction(e -> {
                    NhanVien nv = getTableView().getItems().get(getIndex());
                    if (nv.getNgayThoiViec() == null) {
                        openEditModal(primaryStage, nv);
                    } else {
                        showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Nhân viên đã thôi việc, không thể chỉnh sửa!");
                    }
                });
                box.getChildren().add(btnEdit);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });

        table.getColumns().addAll(colSTT, colID, colName, colPhone, colCCCD, colRole, colBirth, colStart, colEnd, colAction);
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
    // ⚙️ MODAL THÊM / SỬA NHÂN VIÊN
    // =============================================================
    private void openAddModal(Stage owner) {
        openEmployeeModal(owner, "Thêm nhân viên mới", null);
    }

    private void openEditModal(Stage owner, NhanVien nv) {
        openEmployeeModal(owner, "Chỉnh sửa thông tin nhân viên", nv);
    }

    private void openEmployeeModal(Stage owner, String title, NhanVien nv) {
        Stage modal = new Stage();
        modal.initOwner(owner);
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setTitle(title);

        Label lblTitle = new Label(title);
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #14274e;");
        Separator line = new Separator();

        GridPane form = createEmployeeForm(nv);
        Separator line1 = new Separator();

        HBox buttons = createModalButtons(modal, nv, form);

        VBox layout = new VBox(20, lblTitle, line, form, line1, buttons);
        layout.setPadding(new Insets(25));
        layout.setStyle("""
            -fx-background-color: white;
            -fx-border-color: #ddd;
            -fx-border-radius: 10;
            -fx-background-radius: 10;
        """);

        Scene scene = new Scene(layout, 520, 500);
        modal.setScene(scene);
        modal.showAndWait();
    }

    // =============================================================
    // 🧩 GRIDPANE FORM NHÂN VIÊN
    // =============================================================
    private GridPane createEmployeeForm(NhanVien nv) {
        Label lblMa = new Label("Mã nhân viên:");
        Label lblTen = new Label("Họ tên nhân viên:");
        Label lblSDT = new Label("Số điện thoại:");
        Label lblCCCD = new Label("CCCD:");
        Label lblChucVu = new Label("Chức vụ:");
        Label lblNgaySinh = new Label("Ngày sinh:");

        for (Label lbl : new Label[]{lblMa, lblTen, lblSDT, lblCCCD, lblChucVu, lblNgaySinh})
            lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");

        // Tạo mã tự động nếu thêm mới
        String maNhanVien = nv == null ? nhanVienDAO.generateMaNhanVien() : nv.getMaNhanVien();
        
        TextField txtMa = new TextField(maNhanVien);
        txtMa.setEditable(false);
        txtMa.setFocusTraversable(false);
        styleReadonlyField(txtMa);

        TextField txtTen = new TextField(nv == null ? "" : nv.getTenNhanVien());
        TextField txtSDT = new TextField(nv == null ? "" : nv.getSoDienThoai());
        TextField txtCCCD = new TextField(nv == null ? "" : nv.getCCCD());

        ComboBox<String> cbChucVu = new ComboBox<>();
        cbChucVu.getItems().addAll("Nhân viên", "Quản lý");
        cbChucVu.setValue(nv == null ? "Nhân viên" : nv.getChucVu());
        cbChucVu.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(cbChucVu, Priority.ALWAYS);
        cbChucVu.setStyle("""
            -fx-background-color: #ffffff;
            -fx-border-color: #bbb;
            -fx-border-radius: 5;
            -fx-background-radius: 5;
            -fx-padding: 2 2;
        """);

        DatePicker dpNgaySinh = new DatePicker(nv == null ? null : nv.getNgaySinh());
        dpNgaySinh.setStyle("""
            -fx-background-color: #ffffff;
            -fx-border-color: #bbb;
            -fx-border-radius: 5;
            -fx-background-radius: 5;
            -fx-padding: 2 2;
        """);
        dpNgaySinh.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(dpNgaySinh, Priority.ALWAYS);

        for (TextField tf : new TextField[]{txtTen, txtSDT, txtCCCD})
            tf.setStyle("""
                -fx-background-color: #ffffff;
                -fx-border-color: #bbb;
                -fx-border-radius: 5;
                -fx-background-radius: 5;
                -fx-padding: 6 10;
            """);

        GridPane form = new GridPane();
        form.setVgap(18);
        form.setHgap(20);
        form.setPadding(new Insets(10, 40, 10, 40));

        form.addRow(0, lblMa, txtMa);
        form.addRow(1, lblTen, txtTen);
        form.addRow(2, lblSDT, txtSDT);
        form.addRow(3, lblCCCD, txtCCCD);
        form.addRow(4, lblChucVu, cbChucVu);
        form.addRow(5, lblNgaySinh, dpNgaySinh);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(35);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(65);
        form.getColumnConstraints().addAll(c1, c2);

        return form;
    }

    // =============================================================
    // 🎛️ NÚT TRONG MODAL
    // =============================================================
    private HBox createModalButtons(Stage modal, NhanVien nv, GridPane form) {
        Button btnClose = new Button("Đóng");
        Button btnSave = new Button("Lưu");

        styleSecondaryButton(btnClose);
        stylePrimaryDarkButton(btnSave);

        btnClose.setOnAction(e -> modal.close());
        btnSave.setOnAction(e -> {
            TextField txtMa = (TextField) form.getChildren().get(1);
            TextField txtTen = (TextField) form.getChildren().get(3);
            TextField txtSDT = (TextField) form.getChildren().get(5);
            TextField txtCCCD = (TextField) form.getChildren().get(7);
            @SuppressWarnings("unchecked")
            ComboBox<String> cbChucVu = (ComboBox<String>) form.getChildren().get(9);
            DatePicker dpNgaySinh = (DatePicker) form.getChildren().get(11);

            String ma = txtMa.getText();
            String ten = txtTen.getText().trim();
            String sdt = txtSDT.getText().trim();
            String cccd = txtCCCD.getText().trim();
            String chucVu = cbChucVu.getValue();
            LocalDate ngaySinh = dpNgaySinh.getValue();

            // Validation
            if (ten.isEmpty() || sdt.isEmpty() || cccd.isEmpty() || ngaySinh == null) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            if (!sdt.matches("\\d{10,11}")) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Số điện thoại phải có 10-11 chữ số!");
                return;
            }

            if (!cccd.matches("\\d{12}")) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "CCCD phải có đúng 12 chữ số!");
                return;
            }

            try {
                if (nv == null) {
                    // Thêm mới - SỬA LẠI THỨ TỰ THAM SỐ THEO ENTITY
                    if (nhanVienDAO.isSoDienThoaiExists(sdt)) {
                        showAlert(Alert.AlertType.ERROR, "Lỗi", "Số điện thoại đã tồn tại!");
                        return;
                    }

                    if (nhanVienDAO.isCCCDExists(cccd)) {
                        showAlert(Alert.AlertType.ERROR, "Lỗi", "CCCD đã tồn tại!");
                        return;
                    }

                    // SỬA LẠI: Đúng thứ tự tham số theo entity
                    NhanVien newNv = new NhanVien(ma, ten, chucVu, cccd, sdt, ngaySinh, LocalDate.now(), null);
                    if (nhanVienDAO.addNhanVien(newNv)) {
                        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm nhân viên thành công!");
                        loadDataFromDatabase();
                        modal.close();
                    }
                } else {
                    // Cập nhật
                    if (nhanVienDAO.isSoDienThoaiExistsForOther(sdt, ma)) {
                        showAlert(Alert.AlertType.ERROR, "Lỗi", "Số điện thoại đã tồn tại cho nhân viên khác!");
                        return;
                    }

                    if (nhanVienDAO.isCCCDExistsForOther(cccd, ma)) {
                        showAlert(Alert.AlertType.ERROR, "Lỗi", "CCCD đã tồn tại cho nhân viên khác!");
                        return;
                    }

                    nv.setTenNhanVien(ten);
                    nv.setChucVu(chucVu);
                    nv.setCCCD(cccd);
                    nv.setSoDienThoai(sdt);
                    nv.setNgaySinh(ngaySinh);

                    if (nhanVienDAO.updateNhanVien(nv)) {
                        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật nhân viên thành công!");
                        loadDataFromDatabase();
                        modal.close();
                    }
                }
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Có lỗi xảy ra: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox buttons = new HBox(15, btnClose, spacer, btnSave);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(15, 40, 25, 40));

        return buttons;
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

    // =============================================================
    // 🎨 STYLE (giữ nguyên)
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
        btn.setOnMouseEntered(e -> btn.setStyle("""
            -fx-background-color: #1e90ff;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-padding: 8 18;
        """));
        btn.setOnMouseExited(e -> stylePrimaryButton(btn));
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
}