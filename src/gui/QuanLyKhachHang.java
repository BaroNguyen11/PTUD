package gui;

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

public class QuanLyKhachHang extends BorderPane {
    private final ObservableList<KhachHang> data = FXCollections.observableArrayList();

    // ====================== ✅ CONSTRUCTOR ======================
    public QuanLyKhachHang() {
        // Tạo layout
        VBox searchSection = createSearchSection(); // bỏ Stage
        TableView<KhachHang> table = createCustomerTable(); // bỏ Stage

        // Gán vào BorderPane
        setTop(searchSection);
        setCenter(table);

        setPadding(new Insets(20));
        setStyle("-fx-background-color: #f7f9fc;");
    }

    // =============================================================
    // 🔍 TẠO THANH TÌM KIẾM + LỌC + THÊM
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
        TableColumn<KhachHang, Void> colAction = new TableColumn<>("Hành động");

        colSTT.setCellValueFactory(col -> new ReadOnlyObjectWrapper<>(table.getItems().indexOf(col.getValue()) + 1));
        colID.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getMaKhachHang()));
        colName.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getTenKhachHang()));
        colPhone.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getSoDienThoai()));
        colPoints.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getDiemTichLuy()));

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

        table.getColumns().addAll(colSTT, colID, colName, colPhone, colPoints, colAction);

        // Dữ liệu mẫu
        for (int i = 1; i <= 15; i++) {
            data.add(new KhachHang("KH0000" + i, "Khách hàng " + i, "090000000" + i, Math.random() * 10));
        }

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

        Scene scene = new Scene(layout, 550, 400);
        modal.setScene(scene);
        modal.showAndWait();
    }

    private GridPane createCustomerForm(KhachHang kh) {
        Label lblMa = new Label("Mã khách hàng:");
        Label lblTen = new Label("Họ tên khách hàng:");
        Label lblSDT = new Label("Số điện thoại:");
        Label lblDiem = new Label("Điểm tích lũy:");

        for (Label lbl : new Label[]{lblMa, lblTen, lblSDT, lblDiem})
            lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");

        TextField txtMa = new TextField(kh == null ? "KH" + (data.size() + 1) : kh.getMaKhachHang());
        txtMa.setEditable(false);
        styleReadonlyField(txtMa);

        TextField txtTen = new TextField(kh == null ? "" : kh.getTenKhachHang());
        TextField txtSDT = new TextField(kh == null ? "" : kh.getSoDienThoai());
        TextField txtDiem = new TextField(kh == null ? "0" : String.valueOf(kh.getDiemTichLuy()));
        txtDiem.setEditable(false);
        styleReadonlyField(txtDiem);

        GridPane form = new GridPane();
        form.setVgap(18);
        form.setHgap(20);
        form.setPadding(new Insets(10, 40, 10, 40));
        form.addRow(0, lblMa, txtMa);
        form.addRow(1, lblTen, txtTen);
        form.addRow(2, lblSDT, txtSDT);
        form.addRow(3, lblDiem, txtDiem);
        return form;
    }

    private HBox createModalButtons(Stage modal, KhachHang kh, GridPane form) {
        Button btnClose = new Button("Đóng");
        Button btnSave = new Button("Lưu");

        styleSecondaryButton(btnClose);
        stylePrimaryDarkButton(btnSave);

        btnClose.setOnAction(e -> modal.close());
        btnSave.setOnAction(e -> modal.close());

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
}
