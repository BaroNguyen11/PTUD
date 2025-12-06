package gui;

import java.time.LocalDate;
import dao.TaiKhoan_DAO;
import entity.NhanVien;
import entity.TaiKhoan;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Gui_QuanLiTaiKhoan extends BorderPane {

    private TableView<TaiKhoan> tableView;
    private final ObservableList<TaiKhoan> data = FXCollections.observableArrayList();
    private final TaiKhoan_DAO taiKhoanDAO = new TaiKhoan_DAO();
    private ComboBox<String> cbSort;
    private TextField txtSearch;

    public Gui_QuanLiTaiKhoan(){
        
        VBox searchSection = createSearchSection();
        
        // --- Tiêu đề ---
        Label lblTitle = new Label("Danh sách tài khoản");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // --- Bảng dữ liệu ---
        tableView = createTableView();

        VBox content = new VBox(20, searchSection, lblTitle, tableView);
        content.setPadding(new Insets(25));
        content.setStyle("-fx-background-color: #fdfdfd;");
        VBox.setVgrow(tableView, Priority.ALWAYS);

        // --- Layout chính ---
//        BorderPane root = new BorderPane();
//        root.setLeft(sidebar);
//        root.setCenter(content);
        this.setCenter(content);
        this.setMargin(content, new Insets(10));
        VBox.setVgrow(tableView, Priority.ALWAYS);

        this.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());

        // Load dữ liệu từ database
        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        data.clear();
        data.addAll(taiKhoanDAO.getAllTaiKhoan());
    }

    private void applyFilters() {
        String searchKeyword = txtSearch.getText().trim();
        String filterType = cbSort.getValue();
        
        java.util.List<TaiKhoan> filteredTaiKhoan;
        
        // Tìm kiếm theo từ khóa
        if (!searchKeyword.isEmpty()) {
            filteredTaiKhoan = taiKhoanDAO.searchTaiKhoan(searchKeyword);
        } else {
            filteredTaiKhoan = taiKhoanDAO.getAllTaiKhoan();
        }
        
        // Lọc theo quyền
        if (filterType != null && !filterType.equals("Tất cả quyền")) {
            boolean isQuanLy = "Quản lý".equals(filterType);
            filteredTaiKhoan = filteredTaiKhoan.stream()
                .filter(tk -> tk.isTaiKhoanQuanLi() == isQuanLy)
                .toList();
        }
        
        data.clear();
        data.addAll(filteredTaiKhoan);
    }

    private VBox createSearchSection() {
        Label lblSearchTitle = new Label("Tìm kiếm tài khoản");
        lblSearchTitle.setStyle("""
                -fx-font-size: 15px;
                -fx-font-weight: bold;
                -fx-text-fill: #14274e;
            """);
        
        txtSearch = new TextField();
        txtSearch.setPromptText("Nhập mã NV, tên NV, tên đăng nhập hoặc SĐT...");
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
        cbSort.getItems().addAll("Tất cả quyền", "Quản lý", "Nhân viên");
        cbSort.getSelectionModel().selectFirst();
        cbSort.setPrefHeight(38);
        cbSort.setStyle("-fx-padding: 6; -fx-background-radius: 8;");

        Label lblnhan = new Label("Chú thích");
        lblnhan.setStyle("""
                -fx-font-size: 15px;
                -fx-font-weight: bold;
                -fx-text-fill: #14274e;
            """);
        Label lblKhoa = new Label("Khoá tài khoản", createIcon("/img/lock.png"));
        Label lblMoKhoa = new Label("Mở khoá tài khoản", createIcon("/img/unlock.png"));
        Label lblReset = new Label("Tạo mật khẩu mặc định", createIcon("/img/reset.png"));
        VBox chuThich = new VBox(10, lblnhan ,lblKhoa, lblMoKhoa, lblReset);
        
        Separator sep = new Separator();
        sep.setOrientation(javafx.geometry.Orientation.VERTICAL);
        sep.setPrefHeight(28);

        HBox searchBar = new HBox(20, searchBox, cbSort, sep, chuThich);
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

    // ======= HÀM TẠO TABLEVIEW =======
    private TableView<TaiKhoan> createTableView() {
        TableView<TaiKhoan> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setStyle("""
            -fx-font-size: 14px;
            -fx-background-color: white;
            -fx-border-color: #ccc;
            -fx-border-radius: 8;
        """);

        // Cột STT
        TableColumn<TaiKhoan, Number> colSTT = new TableColumn<>("STT");
        colSTT.setCellValueFactory(col ->
            new ReadOnlyObjectWrapper<>(table.getItems().indexOf(col.getValue()) + 1)
        );
        colSTT.setMaxWidth(60);
        colSTT.setStyle("-fx-alignment: CENTER;");

        TableColumn<TaiKhoan, String> colMaTK = new TableColumn<>("Mã tài khoản");
        colMaTK.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getMaTaiKhoan()));

        TableColumn<TaiKhoan, String> colTenDN = new TableColumn<>("Tên đăng nhập");
        colTenDN.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getTaiKhoan()));

        TableColumn<TaiKhoan, String> colMatKhau = new TableColumn<>("Mật khẩu");
        colMatKhau.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getMatKhau()));

        TableColumn<TaiKhoan, String> colQuyen = new TableColumn<>("Phân quyền");
        colQuyen.setCellValueFactory(c ->
            new ReadOnlyObjectWrapper<>(c.getValue().isTaiKhoanQuanLi() ? "Quản lý" : "Lễ tân")
        );

        TableColumn<TaiKhoan, String> colTrangThai = new TableColumn<>("Trạng thái");
        colTrangThai.setCellValueFactory(c ->
            new ReadOnlyObjectWrapper<>(c.getValue().isTrangThaiHoatDong() ? "Hoạt động" : "Khóa")
        );
        colTrangThai.setCellFactory(column -> new TableCell<TaiKhoan, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                    setStyle("");
                } else {
                    setText(item);
                    if ("Hoạt động".equals(item)) {
                        setStyle("-fx-text-fill: green; -fx-font-weight: bold; -fx-alignment: CENTER;");
                    } else {
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-alignment: CENTER;");
                    }
                }
            }
        });

        TableColumn<TaiKhoan, String> colMaNV = new TableColumn<>("Mã NV");
        colMaNV.setCellValueFactory(c ->
            new ReadOnlyObjectWrapper<>(c.getValue().getNhanVien().getMaNhanVien())
        );

        TableColumn<TaiKhoan, String> colTenNV = new TableColumn<>("Tên nhân viên");
        colTenNV.setCellValueFactory(c ->
            new ReadOnlyObjectWrapper<>(c.getValue().getNhanVien().getTenNhanVien())
        );

        TableColumn<TaiKhoan, String> colSDT = new TableColumn<>("SĐT");
        colSDT.setCellValueFactory(c ->
            new ReadOnlyObjectWrapper<>(c.getValue().getNhanVien().getSoDienThoai())
        );

        // Cột hành động
        TableColumn<TaiKhoan, Void> colAction = new TableColumn<>("Hành động");
        colAction.setCellFactory(param -> new TableCell<>() {
            private final ImageView iconLock = createIcon("/img/lock.png", 22);
            private final ImageView iconUnlock = createIcon("/img/unlock.png", 22);
            private final ImageView iconReset = createIcon("/img/reset.png", 22);
            private final HBox box = new HBox(10);

            {
                box.setAlignment(Pos.CENTER);
                box.setPadding(new Insets(5));
                iconLock.setCursor(Cursor.HAND);
                iconUnlock.setCursor(Cursor.HAND);
                iconReset.setCursor(Cursor.HAND);

                iconLock.setOnMouseClicked(e -> {
                    TaiKhoan tk = getTableView().getItems().get(getIndex());
                    showModalXacNhan(tk, true);
                });
                iconUnlock.setOnMouseClicked(e -> {
                    TaiKhoan tk = getTableView().getItems().get(getIndex());
                    showModalXacNhan(tk, false);
                });
                iconReset.setOnMouseClicked(e -> {
                    TaiKhoan tk = getTableView().getItems().get(getIndex());
                    showModalTaoMatKhau(tk);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    TaiKhoan tk = getTableView().getItems().get(getIndex());
                    box.getChildren().clear();
                    if (tk.isTrangThaiHoatDong()) {
                        box.getChildren().addAll(iconLock, iconReset);
                    } else {
                        box.getChildren().addAll(iconUnlock, iconReset);
                    }
                    setGraphic(box);
                }
            }
        });

        // Thêm tất cả các cột vào bảng
        table.getColumns().addAll(
            colSTT, colMaTK, colTenDN, colMatKhau,
            colQuyen, colTrangThai, colMaNV, colTenNV, colSDT, colAction
        );

        table.setItems(data);

        // Màu nền tiêu đề cột
        table.widthProperty().addListener((obs, oldW, newW) ->
            table.lookupAll(".column-header-background")
                 .forEach(node -> node.setStyle("-fx-background-color: #f2f2f2;"))
        );

        return table;
    }

    // ======= ICON HELPER =======
    private ImageView createIcon(String path) {
        return createIcon(path, 20);
    }

    private ImageView createIcon(String path, int size) {
        ImageView img = new ImageView(new Image(getClass().getResourceAsStream(path)));
        img.setFitWidth(size);
        img.setFitHeight(size);
        img.setCursor(javafx.scene.Cursor.HAND);
        return img;
    }

    // ======= MODAL XÁC NHẬN =======
    private void showModalXacNhan(TaiKhoan tk, boolean khoa) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(khoa ? "Xác nhận khóa tài khoản" : "Xác nhận mở khóa tài khoản");

        Label lbl = new Label((khoa ? "Bạn có chắc chắn muốn KHÓA " : "Bạn có chắc chắn muốn MỞ KHÓA ") +
                "tài khoản " + tk.getMaTaiKhoan() + "?");
        lbl.setWrapText(true);
        lbl.setStyle("-fx-text-fill: #d9534f; -fx-font-size: 16px; -fx-font-weight: bold;");
        lbl.setAlignment(Pos.CENTER);

        Button btnYes = new Button("Đồng ý");
        Button btnNo = new Button("Hủy");

        btnYes.setStyle("""
            -fx-background-color: #d9534f;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-padding: 8 20 8 20;
            -fx-background-radius: 8;
        """);

        btnNo.setStyle("""
            -fx-background-color: #e0e0e0;
            -fx-text-fill: #333;
            -fx-font-size: 14px;
            -fx-padding: 8 20 8 20;
            -fx-background-radius: 8;
        """);

        // Hiệu ứng hover
        btnYes.setOnMouseEntered(e -> btnYes.setStyle(btnYes.getStyle().replace("#d9534f", "#c9302c")));
        btnYes.setOnMouseExited(e -> btnYes.setStyle(btnYes.getStyle().replace("#c9302c", "#d9534f")));

        btnNo.setOnMouseEntered(e -> btnNo.setStyle(btnNo.getStyle().replace("#e0e0e0", "#d5d5d5")));
        btnNo.setOnMouseExited(e -> btnNo.setStyle(btnNo.getStyle().replace("#d5d5d5", "#e0e0e0")));

        // Hành động
        btnYes.setOnAction(e -> {
            if (taiKhoanDAO.updateTrangThaiTaiKhoan(tk.getMaTaiKhoan(), !khoa)) {
                tk.setTrangThaiHoatDong(!khoa);
                tableView.refresh();
                showAlert(Alert.AlertType.INFORMATION, "Thành công", 
                    (khoa ? "Khóa" : "Mở khóa") + " tài khoản thành công!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Có lỗi xảy ra khi cập nhật trạng thái!");
            }
            dialog.close();
        });

        btnNo.setOnAction(e -> dialog.close());

        HBox buttons = new HBox(15, btnYes, btnNo);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, lbl, buttons);
        layout.setPadding(new Insets(25));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("""
            -fx-background-color: #fff;
            -fx-border-color: #ccc;
            -fx-border-radius: 12;
            -fx-background-radius: 12;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 10, 0, 0, 4);
        """);

        Scene scene = new Scene(layout, 420, 200);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    // ======= MODAL RESET MẬT KHẨU =======
    private void showModalTaoMatKhau(TaiKhoan tk) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Tạo lại mật khẩu");

        Label lblIcon = new Label("🔑");
        lblIcon.setStyle("-fx-font-size: 38px;");

        Label lbl = new Label("Bạn có muốn tạo lại mật khẩu mặc định (123456)\ncho tài khoản " + tk.getMaTaiKhoan() + "?");
        lbl.setWrapText(true);
        lbl.setAlignment(Pos.CENTER);
        lbl.setStyle("-fx-font-size: 15px; -fx-text-fill: #333;");

        Button btnYes = new Button("Đồng ý");
        Button btnNo = new Button("Hủy");

        btnYes.setStyle("""
            -fx-background-color: #3498db;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-padding: 8 20 8 20;
            -fx-background-radius: 8;
        """);

        btnNo.setStyle("""
            -fx-background-color: #e0e0e0;
            -fx-text-fill: #333;
            -fx-font-size: 14px;
            -fx-padding: 8 20 8 20;
            -fx-background-radius: 8;
        """);

        btnYes.setOnMouseEntered(e -> btnYes.setStyle(btnYes.getStyle().replace("#3498db", "#2980b9")));
        btnYes.setOnMouseExited(e -> btnYes.setStyle(btnYes.getStyle().replace("#2980b9", "#3498db")));

        btnNo.setOnMouseEntered(e -> btnNo.setStyle(btnNo.getStyle().replace("#e0e0e0", "#d5d5d5")));
        btnNo.setOnMouseExited(e -> btnNo.setStyle(btnNo.getStyle().replace("#d5d5d5", "#e0e0e0")));

        btnYes.setOnAction(e -> {
            if (taiKhoanDAO.resetMatKhau(tk.getMaTaiKhoan(), "123456")) {
                tk.setMatKhau("123456");
                tableView.refresh();
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Reset mật khẩu thành công!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Có lỗi xảy ra khi reset mật khẩu!");
            }
            dialog.close();
        });

        btnNo.setOnAction(e -> dialog.close());

        HBox buttons = new HBox(15, btnYes, btnNo);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, lblIcon, lbl, buttons);
        layout.setPadding(new Insets(25));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("""
            -fx-background-color: #fff;
            -fx-border-color: #ccc;
            -fx-border-radius: 12;
            -fx-background-radius: 12;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 10, 0, 0, 4);
        """);

        Scene scene = new Scene(layout, 420, 240);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    // ======= HÀM HIỂN THỊ THÔNG BÁO =======
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}