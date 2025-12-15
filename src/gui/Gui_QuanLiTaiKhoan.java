//package gui;
//
//import java.time.LocalDate;
//import dao.TaiKhoan_DAO;
//import entity.NhanVien;
//import entity.TaiKhoan;
//import javafx.application.Application;
//import javafx.beans.property.ReadOnlyObjectWrapper;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.geometry.*;
//import javafx.scene.Cursor;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.image.*;
//import javafx.scene.layout.*;
//import javafx.stage.Modality;
//import javafx.stage.Stage;
//
//public class Gui_QuanLiTaiKhoan extends BorderPane {
//
//    private TableView<TaiKhoan> tableView;
//    private final ObservableList<TaiKhoan> data = FXCollections.observableArrayList();
//    private final TaiKhoan_DAO taiKhoanDAO = new TaiKhoan_DAO();
//    private ComboBox<String> cbSort;
//    private TextField txtSearch;
//
//    public Gui_QuanLiTaiKhoan(){
//
//        VBox searchSection = createSearchSection();
//
//        // --- Tiêu đề ---
//        Label lblTitle = new Label("Danh sách tài khoản");
//        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
//
//        // --- Bảng dữ liệu ---
//        tableView = createTableView();
//
//        VBox content = new VBox(20, searchSection, lblTitle, tableView);
//        content.setPadding(new Insets(25));
//        content.setStyle("-fx-background-color: #fdfdfd;");
//        VBox.setVgrow(tableView, Priority.ALWAYS);
//
//        // --- Layout chính ---
////        BorderPane root = new BorderPane();
////        root.setLeft(sidebar);
////        root.setCenter(content);
//        this.setCenter(content);
//        this.setMargin(content, new Insets(10));
//        VBox.setVgrow(tableView, Priority.ALWAYS);
//
//        this.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
//
//        // Load dữ liệu từ database
//        loadDataFromDatabase();
//    }
//
//    private void loadDataFromDatabase() {
//        data.clear();
//        data.addAll(taiKhoanDAO.getAllTaiKhoan());
//    }
//
//    private void applyFilters() {
//        String searchKeyword = txtSearch.getText().trim();
//        String filterType = cbSort.getValue();
//
//        java.util.List<TaiKhoan> filteredTaiKhoan;
//
//        // Tìm kiếm theo từ khóa
//        if (!searchKeyword.isEmpty()) {
//            filteredTaiKhoan = taiKhoanDAO.searchTaiKhoan(searchKeyword);
//        } else {
//            filteredTaiKhoan = taiKhoanDAO.getAllTaiKhoan();
//        }
//
//        // Lọc theo quyền
//        if (filterType != null && !filterType.equals("Tất cả quyền")) {
//            boolean isQuanLy = "Quản lý".equals(filterType);
//            filteredTaiKhoan = filteredTaiKhoan.stream()
//                .filter(tk -> tk.isTaiKhoanQuanLi() == isQuanLy)
//                .toList();
//        }
//
//        data.clear();
//        data.addAll(filteredTaiKhoan);
//    }
//
//    private VBox createSearchSection() {
//        Label lblSearchTitle = new Label("Tìm kiếm tài khoản");
//        lblSearchTitle.setStyle("""
//                -fx-font-size: 15px;
//                -fx-font-weight: bold;
//                -fx-text-fill: #14274e;
//            """);
//
//        txtSearch = new TextField();
//        txtSearch.setPromptText("Nhập mã NV, tên NV, tên đăng nhập hoặc SĐT...");
//        txtSearch.setStyle("""
//                -fx-background-color: white;
//                -fx-border-color: #ccc;
//                -fx-border-radius: 8;
//                -fx-background-radius: 8;
//                -fx-padding: 8 10 8 30;
//            """);
//
//        ImageView iconSearch = new ImageView(new Image(getClass().getResource("/img/search-normal.png").toExternalForm()));
//        iconSearch.setFitWidth(16);
//        iconSearch.setFitHeight(16);
//
//        StackPane searchBox = new StackPane(txtSearch, iconSearch);
//        StackPane.setAlignment(iconSearch, Pos.CENTER_LEFT);
//        StackPane.setMargin(iconSearch, new Insets(0, 0, 0, 8));
//        HBox.setHgrow(searchBox, Priority.ALWAYS);
//        txtSearch.setMaxWidth(Double.MAX_VALUE);
//
//        cbSort = new ComboBox<>();
//        cbSort.getItems().addAll("Tất cả quyền", "Quản lý", "Nhân viên");
//        cbSort.getSelectionModel().selectFirst();
//        cbSort.setPrefHeight(38);
//        cbSort.setStyle("-fx-padding: 6; -fx-background-radius: 8;");
//
//        Label lblnhan = new Label("Chú thích");
//        lblnhan.setStyle("""
//                -fx-font-size: 15px;
//                -fx-font-weight: bold;
//                -fx-text-fill: #14274e;
//            """);
//        Label lblKhoa = new Label("Khoá tài khoản", createIcon("/img/lock.png"));
//        Label lblMoKhoa = new Label("Mở khoá tài khoản", createIcon("/img/unlock.png"));
//        Label lblReset = new Label("Tạo mật khẩu mặc định", createIcon("/img/reset.png"));
//        VBox chuThich = new VBox(10, lblnhan ,lblKhoa, lblMoKhoa, lblReset);
//
//        Separator sep = new Separator();
//        sep.setOrientation(javafx.geometry.Orientation.VERTICAL);
//        sep.setPrefHeight(28);
//
//        HBox searchBar = new HBox(20, searchBox, cbSort, sep, chuThich);
//        searchBar.setAlignment(Pos.CENTER_LEFT);
//        searchBar.setPadding(new Insets(0, 0, 5, 0));
//
//        // Thêm sự kiện tìm kiếm và lọc
//        txtSearch.textProperty().addListener((obs, oldValue, newValue) -> {
//            applyFilters();
//        });
//
//        cbSort.valueProperty().addListener((obs, oldValue, newValue) -> {
//            applyFilters();
//        });
//
//        return new VBox(5, lblSearchTitle, searchBar);
//    }
//
//    // ======= HÀM TẠO TABLEVIEW =======
//    private TableView<TaiKhoan> createTableView() {
//        TableView<TaiKhoan> table = new TableView<>();
//        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
//        table.setStyle("""
//            -fx-font-size: 14px;
//            -fx-background-color: white;
//            -fx-border-color: #ccc;
//            -fx-border-radius: 8;
//        """);
//
//        // Cột STT
//        TableColumn<TaiKhoan, Number> colSTT = new TableColumn<>("STT");
//        colSTT.setCellValueFactory(col ->
//            new ReadOnlyObjectWrapper<>(table.getItems().indexOf(col.getValue()) + 1)
//        );
//        colSTT.setMaxWidth(60);
//        colSTT.setStyle("-fx-alignment: CENTER;");
//
//        TableColumn<TaiKhoan, String> colMaTK = new TableColumn<>("Mã tài khoản");
//        colMaTK.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getMaTaiKhoan()));
//
//        TableColumn<TaiKhoan, String> colTenDN = new TableColumn<>("Tên đăng nhập");
//        colTenDN.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getTaiKhoan()));
//
//        TableColumn<TaiKhoan, String> colMatKhau = new TableColumn<>("Mật khẩu");
//        colMatKhau.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getMatKhau()));
//
//        TableColumn<TaiKhoan, String> colQuyen = new TableColumn<>("Phân quyền");
//        colQuyen.setCellValueFactory(c ->
//            new ReadOnlyObjectWrapper<>(c.getValue().isTaiKhoanQuanLi() ? "Quản lý" : "Lễ tân")
//        );
//
//        TableColumn<TaiKhoan, String> colTrangThai = new TableColumn<>("Trạng thái");
//        colTrangThai.setCellValueFactory(c ->
//            new ReadOnlyObjectWrapper<>(c.getValue().isTrangThaiHoatDong() ? "Hoạt động" : "Khóa")
//        );
//        colTrangThai.setCellFactory(column -> new TableCell<TaiKhoan, String>() {
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || item == null) {
//                    setText("");
//                    setStyle("");
//                } else {
//                    setText(item);
//                    if ("Hoạt động".equals(item)) {
//                        setStyle("-fx-text-fill: green; -fx-font-weight: bold; -fx-alignment: CENTER;");
//                    } else {
//                        setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-alignment: CENTER;");
//                    }
//                }
//            }
//        });
//
//        TableColumn<TaiKhoan, String> colMaNV = new TableColumn<>("Mã NV");
//        colMaNV.setCellValueFactory(c ->
//            new ReadOnlyObjectWrapper<>(c.getValue().getNhanVien().getMaNhanVien())
//        );
//
//        TableColumn<TaiKhoan, String> colTenNV = new TableColumn<>("Tên nhân viên");
//        colTenNV.setCellValueFactory(c ->
//            new ReadOnlyObjectWrapper<>(c.getValue().getNhanVien().getTenNhanVien())
//        );
//
//        TableColumn<TaiKhoan, String> colSDT = new TableColumn<>("SĐT");
//        colSDT.setCellValueFactory(c ->
//            new ReadOnlyObjectWrapper<>(c.getValue().getNhanVien().getSoDienThoai())
//        );
//
//        // Cột hành động
//        TableColumn<TaiKhoan, Void> colAction = new TableColumn<>("Hành động");
//        colAction.setCellFactory(param -> new TableCell<>() {
//            private final ImageView iconLock = createIcon("/img/lock.png", 22);
//            private final ImageView iconUnlock = createIcon("/img/unlock.png", 22);
//            private final ImageView iconReset = createIcon("/img/reset.png", 22);
//            private final HBox box = new HBox(10);
//
//            {
//                box.setAlignment(Pos.CENTER);
//                box.setPadding(new Insets(5));
//                iconLock.setCursor(Cursor.HAND);
//                iconUnlock.setCursor(Cursor.HAND);
//                iconReset.setCursor(Cursor.HAND);
//
//                iconLock.setOnMouseClicked(e -> {
//                    TaiKhoan tk = getTableView().getItems().get(getIndex());
//                    showModalXacNhan(tk, true);
//                });
//                iconUnlock.setOnMouseClicked(e -> {
//                    TaiKhoan tk = getTableView().getItems().get(getIndex());
//                    showModalXacNhan(tk, false);
//                });
//                iconReset.setOnMouseClicked(e -> {
//                    TaiKhoan tk = getTableView().getItems().get(getIndex());
//                    showModalTaoMatKhau(tk);
//                });
//            }
//
//            @Override
//            protected void updateItem(Void item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
//                    setGraphic(null);
//                } else {
//                    TaiKhoan tk = getTableView().getItems().get(getIndex());
//                    box.getChildren().clear();
//                    if (tk.isTrangThaiHoatDong()) {
//                        box.getChildren().addAll(iconLock, iconReset);
//                    } else {
//                        box.getChildren().addAll(iconUnlock, iconReset);
//                    }
//                    setGraphic(box);
//                }
//            }
//        });
//
//        // Thêm tất cả các cột vào bảng
//        table.getColumns().addAll(
//            colSTT, colMaTK, colTenDN, colMatKhau,
//            colQuyen, colTrangThai, colMaNV, colTenNV, colSDT, colAction
//        );
//
//        table.setItems(data);
//
//        // Màu nền tiêu đề cột
//        table.widthProperty().addListener((obs, oldW, newW) ->
//            table.lookupAll(".column-header-background")
//                 .forEach(node -> node.setStyle("-fx-background-color: #f2f2f2;"))
//        );
//
//        return table;
//    }
//
//    // ======= ICON HELPER =======
//    private ImageView createIcon(String path) {
//        return createIcon(path, 20);
//    }
//
//    private ImageView createIcon(String path, int size) {
//        ImageView img = new ImageView(new Image(getClass().getResourceAsStream(path)));
//        img.setFitWidth(size);
//        img.setFitHeight(size);
//        img.setCursor(javafx.scene.Cursor.HAND);
//        return img;
//    }
//
//    // ======= MODAL XÁC NHẬN =======
//    private void showModalXacNhan(TaiKhoan tk, boolean khoa) {
//        Stage dialog = new Stage();
//        dialog.initModality(Modality.APPLICATION_MODAL);
//        dialog.setTitle(khoa ? "Xác nhận khóa tài khoản" : "Xác nhận mở khóa tài khoản");
//
//        Label lbl = new Label((khoa ? "Bạn có chắc chắn muốn KHÓA " : "Bạn có chắc chắn muốn MỞ KHÓA ") +
//                "tài khoản " + tk.getMaTaiKhoan() + "?");
//        lbl.setWrapText(true);
//        lbl.setStyle("-fx-text-fill: #d9534f; -fx-font-size: 16px; -fx-font-weight: bold;");
//        lbl.setAlignment(Pos.CENTER);
//
//        Button btnYes = new Button("Đồng ý");
//        Button btnNo = new Button("Hủy");
//
//        btnYes.setStyle("""
//            -fx-background-color: #d9534f;
//            -fx-text-fill: white;
//            -fx-font-size: 14px;
//            -fx-font-weight: bold;
//            -fx-padding: 8 20 8 20;
//            -fx-background-radius: 8;
//        """);
//
//        btnNo.setStyle("""
//            -fx-background-color: #e0e0e0;
//            -fx-text-fill: #333;
//            -fx-font-size: 14px;
//            -fx-padding: 8 20 8 20;
//            -fx-background-radius: 8;
//        """);
//
//        // Hiệu ứng hover
//        btnYes.setOnMouseEntered(e -> btnYes.setStyle(btnYes.getStyle().replace("#d9534f", "#c9302c")));
//        btnYes.setOnMouseExited(e -> btnYes.setStyle(btnYes.getStyle().replace("#c9302c", "#d9534f")));
//
//        btnNo.setOnMouseEntered(e -> btnNo.setStyle(btnNo.getStyle().replace("#e0e0e0", "#d5d5d5")));
//        btnNo.setOnMouseExited(e -> btnNo.setStyle(btnNo.getStyle().replace("#d5d5d5", "#e0e0e0")));
//
//        // Hành động
//        btnYes.setOnAction(e -> {
//            if (taiKhoanDAO.updateTrangThaiTaiKhoan(tk.getMaTaiKhoan(), !khoa)) {
//                tk.setTrangThaiHoatDong(!khoa);
//                tableView.refresh();
//                showAlert(Alert.AlertType.INFORMATION, "Thành công",
//                    (khoa ? "Khóa" : "Mở khóa") + " tài khoản thành công!");
//            } else {
//                showAlert(Alert.AlertType.ERROR, "Lỗi", "Có lỗi xảy ra khi cập nhật trạng thái!");
//            }
//            dialog.close();
//        });
//
//        btnNo.setOnAction(e -> dialog.close());
//
//        HBox buttons = new HBox(15, btnYes, btnNo);
//        buttons.setAlignment(Pos.CENTER);
//
//        VBox layout = new VBox(20, lbl, buttons);
//        layout.setPadding(new Insets(25));
//        layout.setAlignment(Pos.CENTER);
//        layout.setStyle("""
//            -fx-background-color: #fff;
//            -fx-border-color: #ccc;
//            -fx-border-radius: 12;
//            -fx-background-radius: 12;
//            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 10, 0, 0, 4);
//        """);
//
//        Scene scene = new Scene(layout, 420, 200);
//        dialog.setScene(scene);
//        dialog.showAndWait();
//    }
//
//    // ======= MODAL RESET MẬT KHẨU =======
//    private void showModalTaoMatKhau(TaiKhoan tk) {
//        Stage dialog = new Stage();
//        dialog.initModality(Modality.APPLICATION_MODAL);
//        dialog.setTitle("Tạo lại mật khẩu");
//
//        Label lblIcon = new Label("🔑");
//        lblIcon.setStyle("-fx-font-size: 38px;");
//
//        Label lbl = new Label("Bạn có muốn tạo lại mật khẩu mặc định (123456)\ncho tài khoản " + tk.getMaTaiKhoan() + "?");
//        lbl.setWrapText(true);
//        lbl.setAlignment(Pos.CENTER);
//        lbl.setStyle("-fx-font-size: 15px; -fx-text-fill: #333;");
//
//        Button btnYes = new Button("Đồng ý");
//        Button btnNo = new Button("Hủy");
//
//        btnYes.setStyle("""
//            -fx-background-color: #3498db;
//            -fx-text-fill: white;
//            -fx-font-size: 14px;
//            -fx-font-weight: bold;
//            -fx-padding: 8 20 8 20;
//            -fx-background-radius: 8;
//        """);
//
//        btnNo.setStyle("""
//            -fx-background-color: #e0e0e0;
//            -fx-text-fill: #333;
//            -fx-font-size: 14px;
//            -fx-padding: 8 20 8 20;
//            -fx-background-radius: 8;
//        """);
//
//        btnYes.setOnMouseEntered(e -> btnYes.setStyle(btnYes.getStyle().replace("#3498db", "#2980b9")));
//        btnYes.setOnMouseExited(e -> btnYes.setStyle(btnYes.getStyle().replace("#2980b9", "#3498db")));
//
//        btnNo.setOnMouseEntered(e -> btnNo.setStyle(btnNo.getStyle().replace("#e0e0e0", "#d5d5d5")));
//        btnNo.setOnMouseExited(e -> btnNo.setStyle(btnNo.getStyle().replace("#d5d5d5", "#e0e0e0")));
//
//        btnYes.setOnAction(e -> {
//            if (taiKhoanDAO.resetMatKhau(tk.getMaTaiKhoan(), "123456")) {
//                tk.setMatKhau("123456");
//                tableView.refresh();
//                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Reset mật khẩu thành công!");
//            } else {
//                showAlert(Alert.AlertType.ERROR, "Lỗi", "Có lỗi xảy ra khi reset mật khẩu!");
//            }
//            dialog.close();
//        });
//
//        btnNo.setOnAction(e -> dialog.close());
//
//        HBox buttons = new HBox(15, btnYes, btnNo);
//        buttons.setAlignment(Pos.CENTER);
//
//        VBox layout = new VBox(20, lblIcon, lbl, buttons);
//        layout.setPadding(new Insets(25));
//        layout.setAlignment(Pos.CENTER);
//        layout.setStyle("""
//            -fx-background-color: #fff;
//            -fx-border-color: #ccc;
//            -fx-border-radius: 12;
//            -fx-background-radius: 12;
//            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 10, 0, 0, 4);
//        """);
//
//        Scene scene = new Scene(layout, 420, 240);
//        dialog.setScene(scene);
//        dialog.showAndWait();
//    }
//
//    // ======= HÀM HIỂN THỊ THÔNG BÁO =======
//    private void showAlert(Alert.AlertType type, String title, String message) {
//        Alert alert = new Alert(type);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//}
package gui;

import dao.TaiKhoan_DAO;
import entity.TaiKhoan;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
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

import java.util.List;

public class Gui_QuanLiTaiKhoan extends BorderPane {

    private TableView<TaiKhoan> tableView;
    private final ObservableList<TaiKhoan> data = FXCollections.observableArrayList();
    private final TaiKhoan_DAO taiKhoanDAO = new TaiKhoan_DAO();
    private ComboBox<String> cbSort;
    private TextField txtSearch;

    public Gui_QuanLiTaiKhoan() {
        initializeUI();
        loadDataFromDatabase();
    }

    private void initializeUI() {
        // --- 1. SETUP ROOT STYLE (Gradient Background) ---
        this.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa 0%, #e9ecef 100%);");
        this.setPadding(new Insets(30, 40, 30, 40));

        // --- 2. LAYOUT MAIN CONTENT ---
        VBox rootContent = new VBox(25); // Spacing between Header and Body

        // Header
        VBox header = createModernHeader();

        // Search Section (Filter Bar)
        HBox searchSection = createModernSearchBar();

        // Table Section
        VBox tableSection = createModernTableSection();

        // Add components to root content
        rootContent.getChildren().addAll(header, searchSection, tableSection);
        VBox.setVgrow(tableSection, Priority.ALWAYS); // Table takes remaining space

        this.setCenter(rootContent);

        // --- 3. ANIMATION FADE IN ---
        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), rootContent);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    // =============================================================
    // 🎨 HEADER MODERN
    // =============================================================
    private VBox createModernHeader() {
        VBox header = new VBox(10);
        header.setPadding(new Insets(25));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle(
                "-fx-background-color: #082744;" + // Dark blue background
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 4);"
        );

        Label title = new Label("QUẢN LÝ TÀI KHOẢN");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: white;");

        Label subtitle = new Label("Quản lý tài khoản đăng nhập, phân quyền và trạng thái hoạt động");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        subtitle.setStyle("-fx-text-fill: rgba(255,255,255,0.9);");

        header.getChildren().addAll(title, subtitle);
        return header;
    }

    // =============================================================
    // 🔍 SEARCH BAR (CARD STYLE)
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

        // 1. Search Field
        txtSearch = new TextField();
        txtSearch.setPromptText("🔍 Nhập mã NV, tên NV, tên đăng nhập hoặc SĐT...");
        txtSearch.setPrefHeight(40);
        styleInputField(txtSearch);
        HBox.setHgrow(txtSearch, Priority.ALWAYS);

        // 2. Filter ComboBox
        cbSort = new ComboBox<>();
        cbSort.getItems().addAll("Tất cả quyền", "Quản lý", "Nhân viên");
        cbSort.getSelectionModel().selectFirst();
        styleComboBox(cbSort);
        cbSort.setPrefWidth(180);

        // 3. Legend / Legend Icons (Help Text)
        HBox legendBox = new HBox(15);
        legendBox.setAlignment(Pos.CENTER_LEFT);

        Label lblLegendTitle = new Label("Chú thích:");
        lblLegendTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #535c68;");

        Label lblLock = new Label("Khóa", createIcon("/img/lock.png", 16));
        Label lblUnlock = new Label("Mở khóa", createIcon("/img/unlock.png", 16));
        Label lblReset = new Label("Reset MK", createIcon("/img/reset.png", 16));

        // Style legend labels
        String legendStyle = "-fx-font-size: 12px; -fx-text-fill: #7f8c8d;";
        lblLock.setStyle(legendStyle);
        lblUnlock.setStyle(legendStyle);
        lblReset.setStyle(legendStyle);

        legendBox.getChildren().addAll(lblLegendTitle, lblLock, lblUnlock, lblReset);

        // Separator
        Separator sep = new Separator();
        sep.setOrientation(javafx.geometry.Orientation.VERTICAL);

        // Event Listeners
        txtSearch.textProperty().addListener((obs, oldValue, newValue) -> applyFilters());
        cbSort.valueProperty().addListener((obs, oldValue, newValue) -> applyFilters());

        searchBar.getChildren().addAll(txtSearch, cbSort, sep, legendBox);
        return searchBar;
    }

    // =============================================================
    // 📋 TABLE SECTION (CARD STYLE)
    // =============================================================
    private VBox createModernTableSection() {
        VBox tableContainer = new VBox();
        tableContainer.setPadding(new Insets(5));
        tableContainer.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);"
        );

        tableView = new TableView<>();
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

        // --- Table Columns ---

        // STT
        TableColumn<TaiKhoan, Number> colSTT = new TableColumn<>("STT");
        colSTT.setCellValueFactory(col -> new ReadOnlyObjectWrapper<>(tableView.getItems().indexOf(col.getValue()) + 1));
        colSTT.setMaxWidth(50);
        colSTT.setStyle("-fx-alignment: CENTER; -fx-font-family: 'Segoe UI';");

        // Mã TK
        TableColumn<TaiKhoan, String> colMaTK = new TableColumn<>("Mã TK");
        colMaTK.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getMaTaiKhoan()));
        colMaTK.setStyle("-fx-alignment: CENTER_LEFT; -fx-font-family: 'Segoe UI';");

        // Tên Đăng Nhập
        TableColumn<TaiKhoan, String> colTenDN = new TableColumn<>("Tên Đăng Nhập");
        colTenDN.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getTaiKhoan()));
        colTenDN.setStyle("-fx-font-weight: bold; -fx-alignment: CENTER_LEFT; -fx-font-family: 'Segoe UI'; -fx-text-fill: #2980b9;");

        // Mật Khẩu (Hidden or shown)
        TableColumn<TaiKhoan, String> colMatKhau = new TableColumn<>("Mật Khẩu");
        colMatKhau.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getMatKhau())); // Consider masking this
        colMatKhau.setStyle("-fx-alignment: CENTER_LEFT; -fx-font-family: 'Segoe UI';");

        // Phân Quyền (Badge Style)
        TableColumn<TaiKhoan, String> colQuyen = new TableColumn<>("Phân Quyền");
        colQuyen.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().isTaiKhoanQuanLi() ? "Quản lý" : "Nhân viên"));
        colQuyen.setCellFactory(column -> new TableCell<>() {
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

                    if ("Quản lý".equals(item)) {
                        lbl.setStyle(lbl.getStyle() + "-fx-background-color: #d6eaf8; -fx-text-fill: #2980b9;"); // Blue badge
                    } else {
                        lbl.setStyle(lbl.getStyle() + "-fx-background-color: #f2f4f6; -fx-text-fill: #7f8c8d;"); // Gray badge
                    }
                    setGraphic(lbl);
                    setAlignment(Pos.CENTER);
                    setText(null);
                }
            }
        });

        // Trạng Thái (Badge Style)
        TableColumn<TaiKhoan, String> colTrangThai = new TableColumn<>("Trạng Thái");
        colTrangThai.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().isTrangThaiHoatDong() ? "Hoạt động" : "Khóa"));
        colTrangThai.setCellFactory(column -> new TableCell<>() {
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

                    if ("Hoạt động".equals(item)) {
                        lbl.setStyle(lbl.getStyle() + "-fx-background-color: #d4edda; -fx-text-fill: #155724;"); // Green
                    } else {
                        lbl.setStyle(lbl.getStyle() + "-fx-background-color: #f8d7da; -fx-text-fill: #721c24;"); // Red
                    }
                    setGraphic(lbl);
                    setAlignment(Pos.CENTER);
                    setText(null);
                }
            }
        });

        // Thông tin nhân viên
        TableColumn<TaiKhoan, String> colMaNV = new TableColumn<>("Mã NV");
        colMaNV.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getNhanVien().getMaNhanVien()));
        colMaNV.setStyle("-fx-alignment: CENTER_LEFT; -fx-font-family: 'Segoe UI';");

        TableColumn<TaiKhoan, String> colTenNV = new TableColumn<>("Tên Nhân Viên");
        colTenNV.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getNhanVien().getTenNhanVien()));
        colTenNV.setStyle("-fx-alignment: CENTER_LEFT; -fx-font-family: 'Segoe UI';");

        TableColumn<TaiKhoan, String> colSDT = new TableColumn<>("SĐT");
        colSDT.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getNhanVien().getSoDienThoai()));
        colSDT.setStyle("-fx-alignment: CENTER; -fx-font-family: 'Segoe UI';");

        // Hành động (Icons)
        TableColumn<TaiKhoan, Void> colAction = new TableColumn<>("Hành động");
        colAction.setCellFactory(param -> new TableCell<>() {
            private final ImageView iconLock = createIcon("/img/lock.png", 20);
            private final ImageView iconUnlock = createIcon("/img/unlock.png", 20);
            private final ImageView iconReset = createIcon("/img/reset.png", 20);
            private final HBox box = new HBox(12);

            {
                box.setAlignment(Pos.CENTER);
                box.setPadding(new Insets(5));

                // Add tooltips
                Tooltip.install(iconLock, new Tooltip("Khóa tài khoản"));
                Tooltip.install(iconUnlock, new Tooltip("Mở khóa tài khoản"));
                Tooltip.install(iconReset, new Tooltip("Đặt lại mật khẩu"));

                iconLock.setOnMouseClicked(e -> {
                    TaiKhoan tk = getTableView().getItems().get(getIndex());
                    showModalXacNhan(tk, true);
                });
                iconUnlock.setOnMouseClicked(e -> {
                    TaiKhoan tk = getTableView().getItems().get(getIndex());

                    // KIỂM TRA TRẠNG THÁI NHÂN VIÊN
                    if (tk.getNhanVien().getNgayThoiViec() != null) {
                        showAlert(Alert.AlertType.WARNING, "Cảnh báo",
                                "Nhân viên " + tk.getNhanVien().getTenNhanVien() + " đã nghỉ việc.\n" +
                                        "Vui lòng tái tuyển nhân viên trước khi mở khóa tài khoản.");
                        return;
                    }

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

        // Add columns to table
        tableView.getColumns().addAll(
                colSTT, colMaTK, colTenDN, colMatKhau,
                colQuyen, colTrangThai, colMaNV, colTenNV, colSDT, colAction
        );

        tableView.setItems(data);

        tableContainer.getChildren().add(tableView);
        return tableContainer;
    }

    // =============================================================
    // 🛠️ UTILITIES & HELPERS
    // =============================================================

    private void loadDataFromDatabase() {
        data.clear();
        data.addAll(taiKhoanDAO.getAllTaiKhoan());
    }

    private void applyFilters() {
        String searchKeyword = txtSearch.getText().trim();
        String filterType = cbSort.getValue();

        List<TaiKhoan> filteredTaiKhoan;

        // Search logic
        if (!searchKeyword.isEmpty()) {
            filteredTaiKhoan = taiKhoanDAO.searchTaiKhoan(searchKeyword);
        } else {
            filteredTaiKhoan = taiKhoanDAO.getAllTaiKhoan();
        }

        // Filter logic
        if (filterType != null && !filterType.equals("Tất cả quyền")) {
            boolean isQuanLy = "Quản lý".equals(filterType);
            filteredTaiKhoan = filteredTaiKhoan.stream()
                    .filter(tk -> tk.isTaiKhoanQuanLi() == isQuanLy)
                    .toList();
        }

        data.clear();
        data.addAll(filteredTaiKhoan);
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
    }

    private void styleComboBox(ComboBox<?> cbo) {
        cbo.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #ced6e0;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 2;" +
                        "-fx-font-family: 'Segoe UI';"
        );
        cbo.setPrefHeight(40);
    }

    private ImageView createIcon(String path, int size) {
        try {
            ImageView img = new ImageView(new Image(getClass().getResourceAsStream(path)));
            img.setFitWidth(size);
            img.setFitHeight(size);
            img.setCursor(Cursor.HAND);

            // Add hover effect
            img.setOnMouseEntered(e -> img.setOpacity(0.7));
            img.setOnMouseExited(e -> img.setOpacity(1.0));

            return img;
        } catch (Exception e) {
            System.err.println("Could not load icon: " + path);
            return new ImageView(); // Return empty to avoid crash
        }
    }

    // =============================================================
    // 🪟 MODALS (STYLED)
    // =============================================================

    private void showModalXacNhan(TaiKhoan tk, boolean khoa) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(khoa ? "Xác nhận Khóa" : "Xác nhận Mở Khóa");

        Label lblTitle = new Label(khoa ? "KHÓA TÀI KHOẢN" : "MỞ KHÓA TÀI KHOẢN");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        lblTitle.setStyle(khoa ? "-fx-text-fill: #e74c3c;" : "-fx-text-fill: #2ecc71;");

        Label lblMsg = new Label("Bạn có chắc chắn muốn " + (khoa ? "khóa" : "mở khóa") +
                " tài khoản " + tk.getMaTaiKhoan() + " (" + tk.getTaiKhoan() + ")?");
        lblMsg.setWrapText(true);
        lblMsg.setAlignment(Pos.CENTER);
        lblMsg.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px;");

        Button btnYes = new Button("Đồng ý");
        Button btnNo = new Button("Hủy bỏ");

        styleButton(btnYes, khoa ? "#e74c3c" : "#2ecc71", "white");
        styleButton(btnNo, "#ecf0f1", "#2c3e50");

        btnYes.setOnAction(e -> {
            if (taiKhoanDAO.updateTrangThaiTaiKhoan(tk.getMaTaiKhoan(), !khoa)) {
                tk.setTrangThaiHoatDong(!khoa);
                tableView.refresh();
                showAlert(Alert.AlertType.INFORMATION, "Thành công",
                        (khoa ? "Đã khóa" : "Đã mở khóa") + " tài khoản thành công!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Có lỗi xảy ra khi cập nhật trạng thái!");
            }
            dialog.close();
        });

        btnNo.setOnAction(e -> dialog.close());

        HBox buttons = new HBox(15, btnNo, btnYes);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, lblTitle, lblMsg, buttons);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-radius: 10;");

        Scene scene = new Scene(layout, 400, 220);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void showModalTaoMatKhau(TaiKhoan tk) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Đặt lại mật khẩu");

        Label lblTitle = new Label("ĐẶT LẠI MẬT KHẨU");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        lblTitle.setStyle("-fx-text-fill: #3498db;");

        Label lblMsg = new Label("Bạn có muốn đặt lại mật khẩu mặc định (123456)\ncho tài khoản " + tk.getTaiKhoan() + "?");
        lblMsg.setWrapText(true);
        lblMsg.setAlignment(Pos.CENTER);
        lblMsg.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        lblMsg.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px;");

        Button btnYes = new Button("Xác nhận");
        Button btnNo = new Button("Hủy bỏ");

        styleButton(btnYes, "#3498db", "white");
        styleButton(btnNo, "#ecf0f1", "#2c3e50");

        btnYes.setOnAction(e -> {
            if (taiKhoanDAO.resetMatKhau(tk.getMaTaiKhoan(), "123456")) {
                tk.setMatKhau("123456");
                tableView.refresh();
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã đặt lại mật khẩu thành công!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Có lỗi xảy ra khi đặt lại mật khẩu!");
            }
            dialog.close();
        });

        btnNo.setOnAction(e -> dialog.close());

        HBox buttons = new HBox(15, btnNo, btnYes);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, lblTitle, lblMsg, buttons);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-radius: 10;");

        Scene scene = new Scene(layout, 400, 240);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void styleButton(Button btn, String bgColor, String textColor) {
        btn.setStyle(
                "-fx-background-color: " + bgColor + ";" +
                        "-fx-text-fill: " + textColor + ";" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 8 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 13px;"
        );

        // Hover effect
        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: " + bgColor + ";" +
                        "-fx-text-fill: " + textColor + ";" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 8 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 13px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);"
        ));

        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: " + bgColor + ";" +
                        "-fx-text-fill: " + textColor + ";" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 8 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 13px;"
        ));
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}