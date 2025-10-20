package gui;

import java.time.LocalDate;

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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class QuanLyTaiKhoan extends Application {

    private TableView<TaiKhoan> tableView;
    private ObservableList<TaiKhoan> dsTaiKhoan;
    private final ObservableList<TaiKhoan> data = FXCollections.observableArrayList(); 

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        
        VBox searchSection = createSearchSection(primaryStage);
        SideBar sidebar = new SideBar(null);
        // --- Tiêu đề ---
        Label lblTitle = new Label("Danh sách tài khoản");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // --- Bảng dữ liệu ---
        TableView<TaiKhoan> table = createTableView(primaryStage);
        //table.setItems(data);

        VBox content = new VBox(20, searchSection, lblTitle, table);
        content.setPadding(new Insets(25));
        content.setStyle("-fx-background-color: #fdfdfd;");
        VBox.setVgrow(table, Priority.ALWAYS);

        // --- Layout chính ---
        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(content);
        BorderPane.setMargin(content, new Insets(10));
        sidebar.setPrefWidth(230);
        VBox.setVgrow(table, Priority.ALWAYS);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
        
        primaryStage.setTitle("Quản lý tài khoản");
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    

 private VBox createSearchSection(Stage primaryStage) {
    	
        Label lblSearchTitle = new Label("Tìm kiếm tài khoản");
        lblSearchTitle.setStyle("""
                -fx-font-size: 15px;
                -fx-font-weight: bold;
                -fx-text-fill: #14274e;
            """);
        
        TextField txtSearch = new TextField();
        txtSearch.setPromptText("Nhập mã nhân viên...");
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

        ComboBox<String> cbSort = new ComboBox<>();
        cbSort.getItems().addAll("Tất cả quyền", "Quản lý", "Nhân viên");
        cbSort.getSelectionModel().selectFirst();
        cbSort.setPrefHeight(38);
        cbSort.setStyle("-fx-padding: 6; -fx-background-radius: 8;");

//        Button btnAdd = new Button("➕ Thêm nhân viên mới");
//        stylePrimaryButton(btnAdd);
//        btnAdd.setPrefHeight(38);
//        btnAdd.setOnAction(e -> openAddModal(primaryStage));
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

        return new VBox(5, lblSearchTitle,searchBar);
    }
    // ======= HÀM TẠO TABLEVIEW =======
 private TableView<TaiKhoan> createTableView(Stage primaryStage) {
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

	            iconLock.setOnMouseClicked(e ->
	                showModalXacNhan(getTableView().getItems().get(getIndex()), true)
	            );
	            iconUnlock.setOnMouseClicked(e ->
	                showModalXacNhan(getTableView().getItems().get(getIndex()), false)
	            );
	            iconReset.setOnMouseClicked(e ->
	                showModalTaoMatKhau(getTableView().getItems().get(getIndex()))
	            );
	        }

	        @Override
	        protected void updateItem(Void item, boolean empty) {
	            super.updateItem(item, empty);
	            if (empty) {
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

	    // Dữ liệu mẫu
	    NhanVien nv = new NhanVien("NV0001", "Nguyễn Văn A", "0912345678", "0123456789", "Nhân viên",
	            LocalDate.of(2000, 5, 15), LocalDate.of(2023, 1, 1), null);

	    ObservableList<TaiKhoan> dsTaiKhoan = FXCollections.observableArrayList(
	            new TaiKhoan("TK001", "user1", "123456", false, true, nv),
	            new TaiKhoan("TK002", "user2", "654321", true, true, nv),
	            new TaiKhoan("TK003", "user3", "999999", false, false, nv)
	    );

	    table.setItems(dsTaiKhoan);

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

        // Biểu tượng cảnh báo
//        Label lblIcon = new Label("⚠️");
//        lblIcon.setStyle("-fx-font-size: 36px;");

        // Cảnh báo chính
//        Label lblWarning = new Label("Hành động này không thể hoàn tác!");
//        lblWarning.setStyle("-fx-text-fill: #d9534f; -fx-font-size: 16px; -fx-font-weight: bold;");

        // Nội dung
        Label lbl = new Label((khoa ? "Bạn có chắc chắn muốn KHÓA " : "Bạn có chắc chắn muốn MỞ KHÓA ") +
                "tài khoản " + tk.getMaTaiKhoan() + "?");
        lbl.setWrapText(true);
        lbl.setStyle("-fx-text-fill: #d9534f; -fx-font-size: 16px; -fx-font-weight: bold;");
        lbl.setAlignment(Pos.CENTER);

        // Nút
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
            tk.setTrangThaiHoatDong(!khoa);
            tableView.refresh();
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

        Scene scene = new Scene(layout, 420, 260);
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
            tk.setMatKhau("123456");
            tableView.refresh();
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

}
