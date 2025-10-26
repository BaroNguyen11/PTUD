package gui;

import entity.KhachHang;
import javafx.application.Application;
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

public class QuanLyKhachHang extends Application {

    // =============================================================
    // 🧩 THUỘC TÍNH
    // =============================================================
    private final ObservableList<KhachHang> data = FXCollections.observableArrayList();

<<<<<<< HEAD
    // =============================================================
    // 🚀 HÀM MAIN
    // =============================================================
=======
>>>>>>> 92de9b1ef76d406fbb61125017be30cca230840f
    public static void main(String[] args) {
        launch(args);
    }

<<<<<<< HEAD
    // =============================================================
    // 🎬 KHỞI TẠO GIAO DIỆN CHÍNH
    // =============================================================
    @Override
    public void start(Stage primaryStage) {
        SideBar sidebar = new SideBar(null);

        // --- Thanh tìm kiếm ---
        VBox searchSection = createSearchSection(primaryStage);
=======
    @Override
    public void start(Stage primaryStage) {

        SideBar sidebar = new SideBar(null);

        // --- Thanh tìm kiếm ---
        VBox searchSection = createSearchSection(primaryStage);

        // --- Tiêu đề ---
        Label lblTitle = new Label("Danh sách khách hàng");
        lblTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // --- Bảng dữ liệu ---
        TableView<KhachHang> table = createCustomerTable(primaryStage);
        table.setItems(data);

        // --- Kết hợp layout chính ---
        VBox content = new VBox(12, searchSection, lblTitle, table);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #fdfdfd;");

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(content);
        BorderPane.setMargin(content, new Insets(10));
        sidebar.setPrefWidth(230);

        Scene scene = new Scene(root, 1200, 700);
        scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Quản lý khách hàng");
        primaryStage.show();
    }

    // =============================================================
    // 🔍 TẠO THANH TÌM KIẾM + LỌC + THÊM
    // =============================================================
    private VBox createSearchSection(Stage primaryStage) {
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

        TextField txtSearch = new TextField();
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
        ComboBox<String> cbSort = new ComboBox<>();
        cbSort.getItems().addAll("Tất cả xếp loại", "Khách thường", "Khách VIP");
        cbSort.getSelectionModel().selectFirst();
        cbSort.setStyle("-fx-padding: 6; -fx-background-radius: 8;");

        Separator sep = new Separator();
        sep.setOrientation(javafx.geometry.Orientation.VERTICAL);
        sep.setPrefHeight(30);

        // Nút thêm
        Button btnAdd = new Button("➕ Thêm khách hàng mới");
        stylePrimaryButton(btnAdd);
        btnAdd.setOnAction(e -> openAddModal(primaryStage));

        HBox searchBar = new HBox(20, searchBox, cbSort, sep, btnAdd);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(txtSearch, Priority.ALWAYS);
        searchBar.setPadding(new Insets(0, 0, 5, 0));
>>>>>>> 92de9b1ef76d406fbb61125017be30cca230840f

        return new VBox(5, lblSearchTitle, searchBar);
    }

<<<<<<< HEAD
        // --- Bảng dữ liệu ---
        TableView<KhachHang> table = createCustomerTable(primaryStage);
        table.setItems(data);

        // --- Kết hợp layout chính ---
        VBox content = new VBox(12, searchSection, lblTitle, table);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #fdfdfd;");

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(content);
        BorderPane.setMargin(content, new Insets(10));

        sidebar.setPrefWidth(230);
        VBox.setVgrow(table, Priority.ALWAYS);

        Scene scene = new Scene(root, 1200, 700);
        scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Quản lý khách hàng");
        primaryStage.setMaximized(true); // 🌟 Full màn hình khi chạy
        primaryStage.show();
    }

    // =============================================================
    // 🔍 TẠO THANH TÌM KIẾM + LỌC + THÊM
    // =============================================================
    private VBox createSearchSection(Stage primaryStage) {
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

        TextField txtSearch = new TextField();
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
        ComboBox<String> cbSort = new ComboBox<>();
        cbSort.getItems().addAll("Tất cả xếp loại", "Khách thường", "Khách VIP");
        cbSort.getSelectionModel().selectFirst();
        cbSort.setStyle("-fx-padding: 6; -fx-background-radius: 8;");
        cbSort.setPrefWidth(180); // Giãn chiều dài ComboBox

        // Separator
        Separator sep = new Separator();
        sep.setOrientation(javafx.geometry.Orientation.VERTICAL);
        sep.setPrefHeight(30);

        // Nút thêm
        Button btnAdd = new Button("➕ Thêm khách hàng mới");
        stylePrimaryButton(btnAdd);
        btnAdd.setOnAction(e -> openAddModal(primaryStage));

        // Gộp toàn bộ phần trên
        HBox searchBar = new HBox(20, searchBox, cbSort, sep, btnAdd);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        searchBar.setPrefHeight(50);
        HBox.setHgrow(searchBox, Priority.ALWAYS);
        txtSearch.setMaxWidth(Double.MAX_VALUE);
        searchBar.setPadding(new Insets(0, 0, 5, 0));

        return new VBox(5, lblSearchTitle, searchBar);
    }

=======
>>>>>>> 92de9b1ef76d406fbb61125017be30cca230840f
    // =============================================================
    // 📋 TẠO BẢNG KHÁCH HÀNG
    // =============================================================
    private TableView<KhachHang> createCustomerTable(Stage primaryStage) {
        TableView<KhachHang> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<KhachHang, Number> colSTT = new TableColumn<>("STT");
        TableColumn<KhachHang, String> colID = new TableColumn<>("Mã khách hàng");
        TableColumn<KhachHang, String> colName = new TableColumn<>("Tên khách hàng");
        TableColumn<KhachHang, String> colPhone = new TableColumn<>("Số điện thoại");
        TableColumn<KhachHang, Double> colPoints = new TableColumn<>("Điểm tích lũy");
        TableColumn<KhachHang, Void> colAction = new TableColumn<>("Hành động");

        colSTT.setCellValueFactory(col -> new ReadOnlyObjectWrapper<>(table.getItems().indexOf(col.getValue()) + 1));
        colID.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getMaKhachHang()));
        colName.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getTenKhachHang()));
        colPhone.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getSoDienThoai()));
        colPoints.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getDiemTichLuy()));

<<<<<<< HEAD
        // Cột hành động
=======
        // Cột hành động (chỉnh sửa)
>>>>>>> 92de9b1ef76d406fbb61125017be30cca230840f
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
<<<<<<< HEAD
                btnEdit.setGraphic(imgView);
                btnEdit.setStyle("-fx-background-color: transparent;");
                btnEdit.setCursor(Cursor.HAND);
=======

                btnEdit.setGraphic(imgView);
                btnEdit.setStyle("-fx-background-color: transparent;");
                btnEdit.setCursor(Cursor.HAND);

>>>>>>> 92de9b1ef76d406fbb61125017be30cca230840f
                btnEdit.setOnAction(e -> {
                    KhachHang c = getTableView().getItems().get(getIndex());
                    openEditModal(primaryStage, c);
                });
<<<<<<< HEAD
=======

>>>>>>> 92de9b1ef76d406fbb61125017be30cca230840f
                box.getChildren().add(btnEdit);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });

        table.getColumns().addAll(colSTT, colID, colName, colPhone, colPoints, colAction);

        // Dữ liệu mẫu
        for (int i = 1; i <= 15; i++) {
            data.add(new KhachHang("KH0000" + i, "HAPPY", "0928737722", 1.36));
        }

<<<<<<< HEAD
=======
        // Style bảng
>>>>>>> 92de9b1ef76d406fbb61125017be30cca230840f
        table.setStyle("""
                -fx-background-color: white;
                -fx-border-color: #ccc;
                -fx-border-radius: 8;
                -fx-font-size: 14px;
                """);

        table.widthProperty().addListener((obs, oldW, newW) ->
<<<<<<< HEAD
                table.lookupAll(".column-header-background")
                        .forEach(node -> node.setStyle("-fx-background-color: #f2f2f2;"))
=======
            table.lookupAll(".column-header-background")
                 .forEach(node -> node.setStyle("-fx-background-color: #f2f2f2;"))
>>>>>>> 92de9b1ef76d406fbb61125017be30cca230840f
        );

        return table;
    }

    // =============================================================
    // 🧰 MODAL THÊM / SỬA KHÁCH HÀNG
    // =============================================================
    private void openAddModal(Stage owner) {
        openModal(owner, "Thêm khách hàng mới", null);
    }

    private void openEditModal(Stage owner, KhachHang kh) {
        openModal(owner, "Chỉnh sửa thông tin khách hàng", kh);
    }

    private void openModal(Stage owner, String title, KhachHang kh) {
        Stage modal = new Stage();
        modal.initOwner(owner);
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
        line.setStyle("-fx-background-color: #14274e;");

<<<<<<< HEAD
        GridPane form = createCustomerForm(kh);
=======
        // Form
        GridPane form = createCustomerForm(kh);

        // Nút
>>>>>>> 92de9b1ef76d406fbb61125017be30cca230840f
        HBox buttons = createModalButtons(modal, kh, form);

        VBox layout = new VBox(20, lblTitle, line, form, buttons);
        layout.setPadding(new Insets(25));
        layout.setStyle("""
<<<<<<< HEAD
                -fx-background-color: white;
                -fx-border-color: #ddd;
                -fx-border-radius: 10;
                -fx-background-radius: 10;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 5);
                """);
=======
            -fx-background-color: white;
            -fx-border-color: #ddd;
            -fx-border-radius: 10;
            -fx-background-radius: 10;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 5);
        """);
>>>>>>> 92de9b1ef76d406fbb61125017be30cca230840f

        Scene scene = new Scene(layout, 550, 400);
        modal.setScene(scene);
        modal.showAndWait();
    }

    // =============================================================
    // 📑 FORM KHÁCH HÀNG TRONG MODAL
    // =============================================================
    private GridPane createCustomerForm(KhachHang kh) {
        Label lblMa = new Label("Mã khách hàng:");
        Label lblTen = new Label("Họ tên khách hàng:");
        Label lblSDT = new Label("Số điện thoại:");
        Label lblDiem = new Label("Điểm tích lũy:");

        for (Label lbl : new Label[]{lblMa, lblTen, lblSDT, lblDiem})
            lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");

        TextField txtMa = new TextField(kh == null ? "KH0000000199" : kh.getMaKhachHang());
        txtMa.setEditable(false);
<<<<<<< HEAD
=======
        txtMa.setFocusTraversable(false);
>>>>>>> 92de9b1ef76d406fbb61125017be30cca230840f
        styleReadonlyField(txtMa);

        TextField txtTen = new TextField(kh == null ? "" : kh.getTenKhachHang());
        TextField txtSDT = new TextField(kh == null ? "" : kh.getSoDienThoai());
        TextField txtDiem = new TextField(kh == null ? "0" : String.valueOf(kh.getDiemTichLuy()));
        txtDiem.setEditable(false);
        styleReadonlyField(txtDiem);

        for (TextField tf : new TextField[]{txtTen, txtSDT}) {
            tf.setStyle("""
                    -fx-background-color: #ffffff;
                    -fx-border-color: #bbb;
                    -fx-border-radius: 5;
                    -fx-background-radius: 5;
                    -fx-padding: 6 10;
                    """);
        }

        GridPane form = new GridPane();
        form.setVgap(18);
        form.setHgap(20);
        form.setPadding(new Insets(10, 40, 10, 40));

        form.addRow(0, lblMa, txtMa);
        form.addRow(1, lblTen, txtTen);
        form.addRow(2, lblSDT, txtSDT);
        form.addRow(3, lblDiem, txtDiem);

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
    private HBox createModalButtons(Stage modal, KhachHang kh, GridPane form) {
        Button btnClose = new Button("Đóng");
        Button btnSave = new Button("Lưu");

        styleSecondaryButton(btnClose);
        stylePrimaryDarkButton(btnSave);

        btnClose.setOnAction(e -> modal.close());

        btnSave.setOnAction(e -> {
            TextField txtMa = (TextField) form.getChildren().get(1);
            TextField txtTen = (TextField) form.getChildren().get(3);
            TextField txtSDT = (TextField) form.getChildren().get(5);
            TextField txtDiem = (TextField) form.getChildren().get(7);

            if (kh == null) {
                data.add(new KhachHang(
                        txtMa.getText(),
                        txtTen.getText(),
                        txtSDT.getText(),
                        Double.parseDouble(txtDiem.getText())
                ));
            } else {
                kh.setTenKhachHang(txtTen.getText());
                kh.setSoDienThoai(txtSDT.getText());
            }
            modal.close();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox buttons = new HBox(15, btnClose, spacer, btnSave);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(15, 40, 25, 40));
<<<<<<< HEAD
=======

>>>>>>> 92de9b1ef76d406fbb61125017be30cca230840f
        return buttons;
    }

    // =============================================================
    // 🎨 STYLE HỖ TRỢ
    // =============================================================
    private void stylePrimaryButton(Button btn) {
        btn.setStyle("""
<<<<<<< HEAD
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
=======
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
>>>>>>> 92de9b1ef76d406fbb61125017be30cca230840f
        btn.setOnMouseExited(e -> stylePrimaryButton(btn));
    }

    private void styleReadonlyField(TextField tf) {
        tf.setStyle("""
<<<<<<< HEAD
                -fx-opacity: 0.8;
                -fx-background-color: #f3f3f3;
                -fx-border-color: #ccc;
                -fx-border-radius: 5;
                -fx-background-radius: 5;
                -fx-padding: 6 10;
                """);
=======
            -fx-opacity: 0.8;
            -fx-background-color: #f3f3f3;
            -fx-border-color: #ccc;
            -fx-border-radius: 5;
            -fx-background-radius: 5;
            -fx-padding: 6 10;
        """);
>>>>>>> 92de9b1ef76d406fbb61125017be30cca230840f
    }

    private void styleSecondaryButton(Button btn) {
        btn.setStyle("""
<<<<<<< HEAD
                -fx-background-color: #999;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 8 25;
                -fx-background-radius: 6;
                """);
=======
            -fx-background-color: #999;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-padding: 8 25;
            -fx-background-radius: 6;
        """);
>>>>>>> 92de9b1ef76d406fbb61125017be30cca230840f
    }

    private void stylePrimaryDarkButton(Button btn) {
        btn.setStyle("""
<<<<<<< HEAD
                -fx-background-color: #14274e;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 8 25;
                -fx-background-radius: 6;
                """);
=======
            -fx-background-color: #14274e;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-padding: 8 25;
            -fx-background-radius: 6;
        """);
>>>>>>> 92de9b1ef76d406fbb61125017be30cca230840f
    }
}
