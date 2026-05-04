//
//package client.gui;
//
//import client.service.QuanLyBanClient;
//import common.entity.BanAn;
//import common.entity.LoaiBan;
//import common.entity.TrangThai;
//import common.entity.ViTri;
//import javafx.application.Platform;
//import javafx.beans.property.ReadOnlyObjectWrapper;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Node;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.*;
//import javafx.scene.text.Font;
//import javafx.scene.text.FontWeight;
//import javafx.animation.FadeTransition;
//import javafx.util.Duration;
//
//import java.util.List;
//
//public class Gui_QuanLyBan extends BorderPane {
//
//    private final QuanLyBanClient dao = new QuanLyBanClient();
//    private final ObservableList<BanAn> data = FXCollections.observableArrayList();
//
//    private TableView<BanAn> tableView;
//
//    // Form controls
//    private TextField txtMa;
//    private ComboBox<LoaiBan> cboLoai;
//    private ComboBox<TrangThai> cboTrangThai;
//    private ComboBox<ViTri> cboViTri;
//
//    private Button btnThem, btnSua, btnClear;
//
//    // Search controls
//    private TextField txtSearch;
//    private ComboBox<LoaiBan> cboLocLoai;
//    private ComboBox<TrangThai> cboLocTrangThai;
//
//    public Gui_QuanLyBan() {
//        initializeUI();
//    }
//
//    private void initializeUI() {
//        // --- CẤU HÌNH BACKGROUND CHUNG ---
//        this.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa 0%, #e9ecef 100%);");
//
//        // 1. Header (Đặt ở TOP để cố định phía trên)
//        VBox header = createModernHeader();
//        VBox headerContainer = new VBox(header);
//        headerContainer.setPadding(new Insets(10, 30, 0, 30));
//        this.setTop(headerContainer);
//
//        // 2. Main Body (Đặt ở CENTER để tự động co giãn theo khoảng trống còn lại)
//        HBox body = new HBox(20);
//        body.setPadding(new Insets(10, 30, 20, 30)); // Padding: Top Right Bottom Left
//
//        // Left Side (Filter Bar + Table)
//        VBox leftPane = createLeftPaneModern();
//        HBox.setHgrow(leftPane, Priority.ALWAYS); // Cho phép bảng mở rộng tối đa
//
//        // Right Side (Form Control)
//        VBox rightPane = createRightPaneModern();
//        // Cố định chiều rộng form, không cho co giãn để giữ form đẹp
//        rightPane.setPrefWidth(360);
//        rightPane.setMinWidth(360);
//        rightPane.setMaxWidth(360);
//
//        body.getChildren().addAll(leftPane, rightPane);
//
//        this.setCenter(body);
//
//        // Animation Fade In
//        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), this);
//        fadeIn.setFromValue(0.0);
//        fadeIn.setToValue(1.0);
//        fadeIn.play();
//
//        // Load CSS fallback
//        try {
//            this.getStylesheets().add(getClass().getResource("/application/client.application.css").toExternalForm());
//        } catch (Exception e) {
//            // System.err.println("Không tìm thấy CSS: " + e.getMessage());
//        }
//
//        loadData();
//    }
//
//    // ===== HEADER =====
//    private VBox createModernHeader() {
//        VBox header = new VBox(5);
//
//        // Padding = 20 giống mẫu (Top, Right, Bottom, Left đều là 20)
//        header.setPadding(new Insets(20));
//
//        header.setAlignment(Pos.CENTER_LEFT);
//
//        // Style giữ nguyên radius = 15 để đồng bộ style card, nhưng màu sắc vẫn giữ #082744
//        header.setStyle(
//                "-fx-background-color: #082744;" +
//                        // Nếu bạn muốn header này dính sát lề trên cùng (không bo góc trên) thì bỏ radius đi,
//                        // nhưng nếu muốn giống hệt mẫu kia thì giữ radius:
//                        "-fx-background-radius: 15;" +
//                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 5);"
//        );
//
//        Label title = new Label("QUẢN LÝ BÀN ĂN");
//        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28)); // Size 28 giống mẫu
//        title.setStyle("-fx-text-fill: white;");
//
//        Label subtitle = new Label("Quản lý sơ đồ, trạng thái và vị trí bàn trong nhà hàng");
//        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14)); // Size 14 giống mẫu
//        subtitle.setStyle("-fx-text-fill: rgba(255,255,255,0.8);"); // Màu chữ mờ 0.8 giống mẫu
//
//        header.getChildren().addAll(title, subtitle);
//        return header;
//    }
//
//    // ===== LEFT PANE (Filter + Table) =====
//    private VBox createLeftPaneModern() {
//        VBox container = new VBox(15);
//
//        // --- 1. Modern Filter Bar ---
//        HBox filterBar = new HBox(10); // Giảm gap giữa các nút lọc
//        filterBar.setAlignment(Pos.CENTER_LEFT);
//        filterBar.setPadding(new Insets(15));
//        filterBar.setStyle(
//                "-fx-background-color: white;" +
//                        "-fx-background-radius: 10;" +
//                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 1);"
//        );
//
//        // Setup Controls
//        txtSearch = new TextField();
//        txtSearch.setPromptText("🔍 Tìm mã/vị trí...");
//        txtSearch.setPrefHeight(32);
//        txtSearch.setStyle("-fx-background-radius: 6; -fx-border-color: #e9ecef; -fx-border-radius: 6; -fx-padding: 4 8;");
//        HBox.setHgrow(txtSearch, Priority.ALWAYS);
//
//        cboLocLoai = new ComboBox<>();
//        cboLocLoai.getItems().add(null);
//        cboLocLoai.getItems().addAll(LoaiBan.values());
//        cboLocLoai.setPromptText("Loại bàn");
//        styleComboBox(cboLocLoai);
//
//        cboLocTrangThai = new ComboBox<>();
//        cboLocTrangThai.getItems().add(null);
//        cboLocTrangThai.getItems().addAll(TrangThai.values());
//        cboLocTrangThai.setPromptText("Trạng thái");
//        styleComboBox(cboLocTrangThai);
//
//        // Button
//        Button btnSearch = new Button("Tìm");
//        styleButton(btnSearch, "#f1f3f5", "#495057");
//
//        Button btnThemMoi = new Button("✚ Tạo Mới");
//        btnThemMoi.setStyle("-fx-background-color: #082744; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 6 12; -fx-cursor: hand;");
//        addHoverEffect(btnThemMoi);
//
//        // Listeners
//        txtSearch.textProperty().addListener((o, oldV, newV) -> filterData());
//        cboLocLoai.valueProperty().addListener((o, oldV, newV) -> filterData());
//        cboLocTrangThai.valueProperty().addListener((o, oldV, newV) -> filterData());
//        btnSearch.setOnAction(e -> filterData());
//
//        btnThemMoi.setOnAction(e -> {
//            clearForm();
//            String maMoi = dao.generateMaBan();
//            txtMa.setText(maMoi);
//            cboLoai.setValue(null);
//            cboTrangThai.setValue(null);
//            cboViTri.setValue(null);
//            btnThem.setDisable(false);
//            btnSua.setDisable(true);
//            tableView.getSelectionModel().clearSelection();
//            Platform.runLater(() -> {
//                cboLoai.requestFocus();
//                if (!cboLoai.isShowing()) cboLoai.show();
//            });
//        });
//
//        filterBar.getChildren().addAll(txtSearch, cboLocLoai, cboLocTrangThai, btnSearch, btnThemMoi);
//
//        // --- 2. Table Section ---
//        VBox tableContainer = new VBox();
//        tableContainer.setPadding(new Insets(2));
//        tableContainer.setStyle(
//                "-fx-background-color: white;" +
//                        "-fx-background-radius: 10;" +
//                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 1);"
//        );
//        VBox.setVgrow(tableContainer, Priority.ALWAYS); // Quan trọng: Để container bảng giãn hết cỡ
//
//        tableView = new TableView<>();
//        tableView.setItems(data);
//        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//        tableView.setStyle("-fx-background-color: white; -fx-base: white; -fx-border-color: transparent;");
//
//        // Table columns...
//        TableColumn<BanAn, Number> colStt = new TableColumn<>("STT");
//        colStt.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(tableView.getItems().indexOf(c.getValue()) + 1));
//        colStt.setMaxWidth(50);
//        colStt.setStyle("-fx-alignment: CENTER;");
//
//        TableColumn<BanAn, String> colMa = new TableColumn<>("Mã Bàn");
//        colMa.setCellValueFactory(new PropertyValueFactory<>("maBan"));
//        colMa.setStyle("-fx-font-weight: bold;");
//
//        TableColumn<BanAn, String> colLoai = new TableColumn<>("Loại Bàn");
//        colLoai.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getLoai() != null ? cell.getValue().getLoai().name() : ""));
//
//        TableColumn<BanAn, String> colTrangThai = new TableColumn<>("Trạng thái");
//        colTrangThai.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getTrangThai() != null ? cell.getValue().getTrangThai().name() : ""));
//        colTrangThai.setCellFactory(column -> new TableCell<BanAn, String>() {
//            @Override protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                if (item == null || empty) { setText(null); setStyle(""); }
//                else {
//                    setText(item);
//                    if (item.equalsIgnoreCase("TRONG")) setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold;");
//                    else if (item.equalsIgnoreCase("CO_NGUOI")) setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
//                    else setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
//                }
//            }
//        });
//
//        TableColumn<BanAn, String> colViTri = new TableColumn<>("Vị trí");
//        colViTri.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getViTri() != null ? cell.getValue().getViTri().name() : ""));
//
//        tableView.getColumns().addAll(colStt, colMa, colLoai, colTrangThai, colViTri);
//        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> onTableSelectionChanged(newV));
//
//        VBox.setVgrow(tableView, Priority.ALWAYS); // Quan trọng: Để Table giãn hết chiều cao container
//        tableContainer.getChildren().add(tableView);
//
//        container.getChildren().addAll(filterBar, tableContainer);
//        return container;
//    }
//
//    // ===== RIGHT PANE (Form Input) - Đã tinh chỉnh khoảng cách =====
//    private VBox createRightPaneModern() {
//        VBox card = new VBox(12); // Giảm gap tổng thể từ 15 xuống 12
//        card.setPadding(new Insets(20)); // Padding vừa phải
//        card.setStyle(
//                "-fx-background-color: white;" +
//                        "-fx-background-radius: 12;" +
//                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);"
//        );
//
//        Label title = new Label("Thông tin chi tiết");
//        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
//        title.setStyle("-fx-text-fill: #082744; -fx-border-width: 0 0 0 4; -fx-border-color: #082744; -fx-padding: 0 0 0 8;");
//
//        // Form Layout - Giảm Vgap để các dòng gần nhau hơn
//        GridPane form = new GridPane();
//        form.setHgap(10);
//        form.setVgap(12); // Giảm từ 20 xuống 12 để tiết kiệm chiều cao
//        form.setPadding(new Insets(5, 0, 0, 0));
//
//        ColumnConstraints c1 = new ColumnConstraints(); c1.setPercentWidth(30);
//        ColumnConstraints c2 = new ColumnConstraints(); c2.setPercentWidth(70);
//        form.getColumnConstraints().addAll(c1, c2);
//
//        // Setup Controls
//        txtMa = new TextField(); txtMa.setEditable(false);
//        txtMa.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 6;");
//
//        cboLoai = new ComboBox<>(); cboLoai.getItems().addAll(LoaiBan.values()); cboLoai.setMaxWidth(Double.MAX_VALUE);
//        styleComboBox(cboLoai);
//
//        cboTrangThai = new ComboBox<>(); cboTrangThai.getItems().addAll(TrangThai.values()); cboTrangThai.setMaxWidth(Double.MAX_VALUE);
//        styleComboBox(cboTrangThai);
//
//        cboViTri = new ComboBox<>(); cboViTri.getItems().addAll(ViTri.values()); cboViTri.setMaxWidth(Double.MAX_VALUE);
//        styleComboBox(cboViTri);
//
//        addFormRow(form, "Mã bàn:", txtMa, 0);
//        addFormRow(form, "Loại bàn:", cboLoai, 1);
//        addFormRow(form, "Trạng thái:", cboTrangThai, 2);
//        addFormRow(form, "Vị trí:", cboViTri, 3);
//
//        // Buttons
//        btnThem = new Button("Thêm Bàn"); styleButton(btnThem, "#10ac84", "white"); btnThem.setMaxWidth(Double.MAX_VALUE);
//        btnSua = new Button("Cập Nhật"); styleButton(btnSua, "#f39c12", "white"); btnSua.setMaxWidth(Double.MAX_VALUE);
//        btnClear = new Button("Làm Mới"); styleButton(btnClear, "#95a5a6", "white"); btnClear.setMaxWidth(Double.MAX_VALUE);
//
//        // Đặt buttons sát nhau hơn
//        VBox actionBox = new VBox(8, btnThem, btnSua, btnClear);
//        actionBox.setPadding(new Insets(15, 0, 0, 0));
//
//        Separator separator = new Separator();
//        separator.setStyle("-fx-background-color: #e9ecef;");
//        VBox.setMargin(separator, new Insets(10, 0, 10, 0));
//
//        // Footer Image
//        VBox imageContainer = new VBox(5);
//        imageContainer.setAlignment(Pos.CENTER);
//
//        // Spacer để đẩy logo xuống dưới cùng nhưng không quá mức
//        Region spacer = new Region();
//        VBox.setVgrow(spacer, Priority.ALWAYS);
//
//        try {
//            Image img = new Image(getClass().getResourceAsStream("/img/Logo.png"));
//            ImageView imageView = new ImageView(img);
//            imageView.setFitWidth(100); // Giảm kích thước logo để không chiếm chỗ
//            imageView.setPreserveRatio(true);
//            imageContainer.getChildren().add(imageView);
//        } catch (Exception e) {
//            // Fallback
//        }
//
//        Label lblFooter = new Label("2BT RESTAURANT");
//        lblFooter.setStyle("-fx-text-fill: #bdc3c7; -fx-font-weight: bold; -fx-font-size: 12px;");
//        imageContainer.getChildren().add(lblFooter);
//
//        // Listeners
//        btnThem.setOnAction(e -> handleAdd());
//        btnSua.setOnAction(e -> handleUpdate());
//        btnClear.setOnAction(e -> clearForm());
//
//        card.getChildren().addAll(title, form, actionBox, spacer, separator, imageContainer);
//        return card;
//    }
//
//    // ===== UI HELPERS (Giữ nguyên logic) =====
//    private void styleButton(Button btn, String bgColor, String textColor) {
//        btn.setStyle("-fx-background-color: " + bgColor + "; -fx-text-fill: " + textColor + "; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 8 15; -fx-cursor: hand;");
//        addHoverEffect(btn);
//    }
//
//    private void addHoverEffect(Node node) {
//        node.setOnMouseEntered(e -> node.setStyle(node.getStyle() + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);"));
//        node.setOnMouseExited(e -> {
//            // Reset style logic... (để đơn giản trong ví dụ này ta không parse lại string)
//            node.setEffect(null);
//        });
//    }
//
//    private void styleComboBox(ComboBox<?> cbo) {
//        cbo.setStyle("-fx-background-color: white; -fx-border-color: #ced4da; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 1;");
//        cbo.setPrefHeight(32); // Giảm chiều cao combobox
//    }
//
//    private void addFormRow(GridPane grid, String labelText, Node field, int row) {
//        Label lbl = new Label(labelText);
//        lbl.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
//        lbl.setStyle("-fx-text-fill: #6c757d;");
//        grid.add(lbl, 0, row);
//        grid.add(field, 1, row);
//    }
//
//    // ===== LOGIC HANDLERS (Giữ nguyên) =====
//    private void onTableSelectionChanged(BanAn b) {
//        if (b == null) { clearForm(); return; }
//        txtMa.setText(b.getMaBan());
//        cboLoai.setValue(b.getLoai());
//        cboTrangThai.setValue(b.getTrangThai());
//        cboViTri.setValue(b.getViTri());
//        btnThem.setDisable(true);
//        btnSua.setDisable(false);
//    }
//
//    private void handleAdd() {
//        String ma = dao.generateMaBan();
//        LoaiBan loai = cboLoai.getValue();
//        TrangThai tt = cboTrangThai.getValue();
//        ViTri vt = cboViTri.getValue();
//        if (loai == null || tt == null || vt == null) { showAlert(Alert.AlertType.ERROR, "Vui lòng chọn đầy đủ thông tin."); return; }
//        BanAn b = new BanAn(ma, loai, tt, vt);
//        if (dao.addBanAn(b)) {
//            showAlert(Alert.AlertType.INFORMATION, "Đã thêm bàn: " + ma);
//            loadData(); clearForm();
//            Platform.runLater(() -> { cboLoai.requestFocus(); if(!cboLoai.isShowing()) cboLoai.show(); });
//        } else { showAlert(Alert.AlertType.ERROR, "Thêm thất bại."); }
//    }
//
//    private void handleUpdate() {
//        String ma = txtMa.getText();
//        if (ma == null || ma.isEmpty()) { showAlert(Alert.AlertType.ERROR, "Chọn bàn cần sửa."); return; }
//        LoaiBan loai = cboLoai.getValue(); TrangThai tt = cboTrangThai.getValue(); ViTri vt = cboViTri.getValue();
//        if (loai == null || tt == null || vt == null) { showAlert(Alert.AlertType.ERROR, "Vui lòng chọn đầy đủ thông tin."); return; }
//        BanAn b = new BanAn(ma, loai, tt, vt);
//        if (dao.updateBanAn(b)) { showAlert(Alert.AlertType.INFORMATION, "Cập nhật thành công."); loadData(); }
//        else showAlert(Alert.AlertType.ERROR, "Cập nhật thất bại.");
//    }
//
//    private void filterData() {
//        String kw = txtSearch.getText().trim();
//        LoaiBan locLoai = cboLocLoai.getValue();
//        TrangThai locTT = cboLocTrangThai.getValue();
//        List<BanAn> filtered = dao.getAllBanAn();
//        if (!kw.isEmpty()) {
//            filtered.removeIf(b -> !b.getMaBan().toUpperCase().contains(kw.toUpperCase()) && !(b.getViTri() != null && b.getViTri().name().toUpperCase().contains(kw.toUpperCase())));
//        }
//        if (locLoai != null) filtered.removeIf(b -> b.getLoai() != locLoai);
//        if (locTT != null) filtered.removeIf(b -> b.getTrangThai() != locTT);
//        data.clear(); data.addAll(filtered); tableView.refresh();
//    }
//
//    private void loadData() {
//        Platform.runLater(() -> {
//            data.clear(); data.addAll(dao.getAllBanAn()); tableView.refresh();
//            txtSearch.clear(); cboLocLoai.setValue(null); cboLocTrangThai.setValue(null);
//        });
//    }
//
//    private void clearForm() {
//        txtMa.clear(); cboLoai.setValue(null); cboTrangThai.setValue(null); cboViTri.setValue(null);
//        btnThem.setDisable(false); btnSua.setDisable(true);
//        tableView.getSelectionModel().clearSelection();
//    }
//
//    private void showAlert(Alert.AlertType type, String msg) {
//        Alert a = new Alert(type); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
//    }
//}
package client.gui;

import client.service.QuanLyBanClient;
import common.entity.BanAn;
import common.entity.LoaiBan;
import common.entity.TrangThai;
import common.entity.ViTri;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

import java.util.List;

public class Gui_QuanLyBan extends BorderPane {

    private final QuanLyBanClient dao = new QuanLyBanClient();
    private final ObservableList<BanAn> data = FXCollections.observableArrayList();

    private TableView<BanAn> tableView;

    // Form controls
    private TextField txtMa;
    private ComboBox<LoaiBan> cboLoai;
    private ComboBox<TrangThai> cboTrangThai; // Sẽ bị Disable
    private ComboBox<ViTri> cboViTri;

    private Button btnThem, btnSua, btnClear;

    // Search controls
    private TextField txtSearch;
    private ComboBox<LoaiBan> cboLocLoai;
    private ComboBox<TrangThai> cboLocTrangThai;

    public Gui_QuanLyBan() {
        initializeUI();
    }

    private void initializeUI() {
        this.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa 0%, #e9ecef 100%);");

        VBox header = createModernHeader();
        VBox headerContainer = new VBox(header);
        headerContainer.setPadding(new Insets(10, 30, 0, 30));
        this.setTop(headerContainer);

        HBox body = new HBox(20);
        body.setPadding(new Insets(10, 30, 20, 30));

        VBox leftPane = createLeftPaneModern();
        HBox.setHgrow(leftPane, Priority.ALWAYS);

        VBox rightPane = createRightPaneModern();
        rightPane.setPrefWidth(360);
        rightPane.setMinWidth(360);
        rightPane.setMaxWidth(360);

        body.getChildren().addAll(leftPane, rightPane);
        this.setCenter(body);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), this);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        loadData();
    }

    // ===== HEADER =====
    private VBox createModernHeader() {
        VBox header = new VBox(5);
        header.setPadding(new Insets(20));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #082744; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 5);");

        Label title = new Label("QUẢN LÝ BÀN ĂN");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: white;");

        Label subtitle = new Label("Quản lý sơ đồ, trạng thái và vị trí bàn trong nhà hàng");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        subtitle.setStyle("-fx-text-fill: rgba(255,255,255,0.8);");

        header.getChildren().addAll(title, subtitle);
        return header;
    }

    // ===== LEFT PANE (Filter + Table) =====
    private VBox createLeftPaneModern() {
        VBox container = new VBox(15);

        // --- 1. Modern Filter Bar ---
        HBox filterBar = new HBox(10);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(15));
        filterBar.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 1);");

        txtSearch = new TextField();
        txtSearch.setPromptText("🔍 Tìm mã/vị trí...");
        txtSearch.setPrefHeight(32);
        txtSearch.setStyle("-fx-background-radius: 6; -fx-border-color: #e9ecef; -fx-border-radius: 6; -fx-padding: 4 8;");
        HBox.setHgrow(txtSearch, Priority.ALWAYS);

        cboLocLoai = new ComboBox<>();
        cboLocLoai.getItems().add(null);
        cboLocLoai.getItems().addAll(LoaiBan.values());
        cboLocLoai.setPromptText("Loại bàn");
        styleComboBox(cboLocLoai);

        cboLocTrangThai = new ComboBox<>();
        cboLocTrangThai.getItems().add(null);
        cboLocTrangThai.getItems().addAll(TrangThai.values());
        cboLocTrangThai.setPromptText("Trạng thái");
        styleComboBox(cboLocTrangThai);

        // [ĐÃ SỬA] Xóa nút Tìm, chỉ giữ nút Tạo Mới
        Button btnThemMoi = new Button("✚ Tạo Mới");
        btnThemMoi.setStyle("-fx-background-color: #082744; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 6 12; -fx-cursor: hand;");
        addHoverEffect(btnThemMoi);

        // Listeners
        txtSearch.textProperty().addListener((o, oldV, newV) -> filterData());
        cboLocLoai.valueProperty().addListener((o, oldV, newV) -> filterData());
        cboLocTrangThai.valueProperty().addListener((o, oldV, newV) -> filterData());

        btnThemMoi.setOnAction(e -> {
            clearForm();
            String maMoi = dao.generateMaBan();
            txtMa.setText(maMoi);
            // Khi tạo mới, trạng thái mặc định là TRỐNG và hiển thị lên UI để user biết
            cboTrangThai.setValue(TrangThai.TRONG);

            cboLoai.setValue(null);
            cboViTri.setValue(null);
            btnThem.setDisable(false);
            btnSua.setDisable(true);
            tableView.getSelectionModel().clearSelection();
            Platform.runLater(() -> {
                cboLoai.requestFocus();
                if (!cboLoai.isShowing()) cboLoai.show();
            });
        });

        filterBar.getChildren().addAll(txtSearch, cboLocLoai, cboLocTrangThai, btnThemMoi);

        // --- 2. Table Section ---
        VBox tableContainer = new VBox();
        tableContainer.setPadding(new Insets(2));
        tableContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 1);");
        VBox.setVgrow(tableContainer, Priority.ALWAYS);

        tableView = new TableView<>();
        tableView.setItems(data);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setStyle("-fx-background-color: white; -fx-base: white; -fx-border-color: transparent;");

        TableColumn<BanAn, Number> colStt = new TableColumn<>("STT");
        colStt.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(tableView.getItems().indexOf(c.getValue()) + 1));
        colStt.setMaxWidth(50);
        colStt.setStyle("-fx-alignment: CENTER;");

        TableColumn<BanAn, String> colMa = new TableColumn<>("Mã Bàn");
        colMa.setCellValueFactory(new PropertyValueFactory<>("maBan"));
        colMa.setStyle("-fx-font-weight: bold;");

        TableColumn<BanAn, String> colLoai = new TableColumn<>("Loại Bàn");
        colLoai.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getLoai() != null ? cell.getValue().getLoai().name() : ""));

        TableColumn<BanAn, String> colTrangThai = new TableColumn<>("Trạng thái");
        colTrangThai.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getTrangThai() != null ? cell.getValue().getTrangThai().name() : ""));
        colTrangThai.setCellFactory(column -> new TableCell<BanAn, String>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) { setText(null); setStyle(""); }
                else {
                    setText(item);
                    if (item.equalsIgnoreCase("TRONG")) setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold;");
                    else if (item.equalsIgnoreCase("CO_NGUOI")) setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    else setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
                }
            }
        });

        TableColumn<BanAn, String> colViTri = new TableColumn<>("Vị trí");
        colViTri.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getViTri() != null ? cell.getValue().getViTri().name() : ""));

        tableView.getColumns().addAll(colStt, colMa, colLoai, colTrangThai, colViTri);
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> onTableSelectionChanged(newV));

        VBox.setVgrow(tableView, Priority.ALWAYS);
        tableContainer.getChildren().add(tableView);

        container.getChildren().addAll(filterBar, tableContainer);
        return container;
    }

    // ===== RIGHT PANE (Form Input) =====
    private VBox createRightPaneModern() {
        VBox card = new VBox(12);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);");

        Label title = new Label("Thông tin chi tiết");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        title.setStyle("-fx-text-fill: #082744; -fx-border-width: 0 0 0 4; -fx-border-color: #082744; -fx-padding: 0 0 0 8;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(12);
        form.setPadding(new Insets(5, 0, 0, 0));
        form.getColumnConstraints().addAll(new ColumnConstraints() {{setPercentWidth(30);}}, new ColumnConstraints() {{setPercentWidth(70);}});

        txtMa = new TextField(); txtMa.setEditable(false);
        txtMa.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 6;");

        cboLoai = new ComboBox<>(); cboLoai.getItems().addAll(LoaiBan.values()); cboLoai.setMaxWidth(Double.MAX_VALUE);
        styleComboBox(cboLoai);

        cboTrangThai = new ComboBox<>();
        cboTrangThai.getItems().addAll(TrangThai.values());
        cboTrangThai.setMaxWidth(Double.MAX_VALUE);
        styleComboBox(cboTrangThai);

        // [ĐÃ SỬA] Vô hiệu hóa ô chọn trạng thái (Chỉ để xem)
        cboTrangThai.setDisable(true);
        cboTrangThai.setStyle("-fx-background-color: #f1f2f6; -fx-opacity: 1; -fx-border-color: #ced4da; -fx-border-radius: 5;");

        cboViTri = new ComboBox<>(); cboViTri.getItems().addAll(ViTri.values()); cboViTri.setMaxWidth(Double.MAX_VALUE);
        styleComboBox(cboViTri);

        addFormRow(form, "Mã bàn:", txtMa, 0);
        addFormRow(form, "Loại bàn:", cboLoai, 1);
        addFormRow(form, "Trạng thái:", cboTrangThai, 2);
        addFormRow(form, "Vị trí:", cboViTri, 3);

        btnThem = new Button("Thêm Bàn"); styleButton(btnThem, "#10ac84", "white"); btnThem.setMaxWidth(Double.MAX_VALUE);
        btnSua = new Button("Cập Nhật"); styleButton(btnSua, "#f39c12", "white"); btnSua.setMaxWidth(Double.MAX_VALUE);
        btnClear = new Button("Làm Mới"); styleButton(btnClear, "#95a5a6", "white"); btnClear.setMaxWidth(Double.MAX_VALUE);

        VBox actionBox = new VBox(8, btnThem, btnSua, btnClear);
        actionBox.setPadding(new Insets(15, 0, 0, 0));

        Separator separator = new Separator(); separator.setStyle("-fx-background-color: #e9ecef;");
        VBox.setMargin(separator, new Insets(10, 0, 10, 0));

        VBox imageContainer = new VBox(5);
        imageContainer.setAlignment(Pos.CENTER);
        Region spacer = new Region(); VBox.setVgrow(spacer, Priority.ALWAYS);

        try {
            Image img = new Image(getClass().getResourceAsStream("/img/Logo.png"));
            ImageView imageView = new ImageView(img);
            imageView.setFitWidth(100); imageView.setPreserveRatio(true);
            imageContainer.getChildren().add(imageView);
        } catch (Exception e) {}

        Label lblFooter = new Label("2BT RESTAURANT");
        lblFooter.setStyle("-fx-text-fill: #bdc3c7; -fx-font-weight: bold; -fx-font-size: 12px;");
        imageContainer.getChildren().add(lblFooter);

        btnThem.setOnAction(e -> handleAdd());
        btnSua.setOnAction(e -> handleUpdate());
        btnClear.setOnAction(e -> clearForm());

        card.getChildren().addAll(title, form, actionBox, spacer, separator, imageContainer);
        return card;
    }

    // ===== UI HELPERS =====
    private void styleButton(Button btn, String bgColor, String textColor) {
        btn.setStyle("-fx-background-color: " + bgColor + "; -fx-text-fill: " + textColor + "; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 8 15; -fx-cursor: hand;");
        addHoverEffect(btn);
    }

    private void addHoverEffect(Node node) {
        node.setOnMouseEntered(e -> node.setStyle(node.getStyle() + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);"));
        node.setOnMouseExited(e -> node.setEffect(null));
    }

    private void styleComboBox(ComboBox<?> cbo) {
        cbo.setStyle("-fx-background-color: white; -fx-border-color: #ced4da; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 1;");
        cbo.setPrefHeight(32);
    }

    private void addFormRow(GridPane grid, String labelText, Node field, int row) {
        Label lbl = new Label(labelText);
        lbl.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        lbl.setStyle("-fx-text-fill: #6c757d;");
        grid.add(lbl, 0, row);
        grid.add(field, 1, row);
    }

    // ===== LOGIC HANDLERS =====
    private void onTableSelectionChanged(BanAn b) {
        if (b == null) { clearForm(); return; }
        txtMa.setText(b.getMaBan());
        cboLoai.setValue(b.getLoai());
        cboTrangThai.setValue(b.getTrangThai()); // Chỉ hiển thị để xem, đã bị disable
        cboViTri.setValue(b.getViTri());
        btnThem.setDisable(true);
        btnSua.setDisable(false);
    }

    private void handleAdd() {
        String ma = dao.generateMaBan();
        LoaiBan loai = cboLoai.getValue();
        ViTri vt = cboViTri.getValue();

        // [ĐÃ SỬA] Không lấy trạng thái từ ComboBox (vì có thể null hoặc user chọn sai)
        // Mặc định bàn mới là TRỐNG
        TrangThai tt = TrangThai.TRONG;

        if (loai == null || vt == null) {
            showAlert(Alert.AlertType.ERROR, "Vui lòng chọn Loại bàn và Vị trí.");
            return;
        }

        BanAn b = new BanAn(ma, loai, tt, vt);
        if (dao.addBanAn(b)) {
            showAlert(Alert.AlertType.INFORMATION, "Đã thêm bàn: " + ma);
            loadData();
            clearForm();
            Platform.runLater(() -> { cboLoai.requestFocus(); if(!cboLoai.isShowing()) cboLoai.show(); });
        } else {
            showAlert(Alert.AlertType.ERROR, "Thêm thất bại.");
        }
    }

    private void handleUpdate() {
        String ma = txtMa.getText();
        if (ma == null || ma.isEmpty()) { showAlert(Alert.AlertType.ERROR, "Chọn bàn cần sửa."); return; }

        LoaiBan loai = cboLoai.getValue();
        ViTri vt = cboViTri.getValue();

        // [ĐÃ SỬA] Lấy trạng thái hiện tại từ ComboBox (vì ComboBox này đang giữ giá trị cũ khi click vào bảng)
        // Người dùng không sửa được nên trạng thái này được bảo toàn.
        TrangThai tt = cboTrangThai.getValue();

        if (loai == null || tt == null || vt == null) {
            showAlert(Alert.AlertType.ERROR, "Dữ liệu không hợp lệ.");
            return;
        }

        BanAn b = new BanAn(ma, loai, tt, vt);
        if (dao.updateBanAn(b)) {
            showAlert(Alert.AlertType.INFORMATION, "Cập nhật thông tin thành công.");
            loadData();
        } else {
            showAlert(Alert.AlertType.ERROR, "Cập nhật thất bại.");
        }
    }

    private void filterData() {
        String kw = txtSearch.getText().trim();
        LoaiBan locLoai = cboLocLoai.getValue();
        TrangThai locTT = cboLocTrangThai.getValue();
        List<BanAn> filtered = dao.getAllBanAn();

        if (!kw.isEmpty()) {
            filtered.removeIf(b -> !b.getMaBan().toUpperCase().contains(kw.toUpperCase()) &&
                    !(b.getViTri() != null && b.getViTri().name().toUpperCase().contains(kw.toUpperCase())));
        }
        if (locLoai != null) filtered.removeIf(b -> b.getLoai() != locLoai);
        if (locTT != null) filtered.removeIf(b -> b.getTrangThai() != locTT);

        data.clear(); data.addAll(filtered); tableView.refresh();
    }

    private void loadData() {
        Platform.runLater(() -> {
            data.clear(); data.addAll(dao.getAllBanAn()); tableView.refresh();
            txtSearch.clear(); cboLocLoai.setValue(null); cboLocTrangThai.setValue(null);
        });
    }

    private void clearForm() {
        txtMa.clear();
        cboLoai.setValue(null);
        // [ĐÃ SỬA] Reset trạng thái về null hoặc TRỐNG khi clear form
        cboTrangThai.setValue(TrangThai.TRONG);
        cboViTri.setValue(null);

        btnThem.setDisable(false);
        btnSua.setDisable(true);
        tableView.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert a = new Alert(type); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }
}