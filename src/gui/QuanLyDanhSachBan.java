package gui;

import dao.QuanLyDanhSachBan_DAO;
import entity.BanAn;
import entity.LoaiBan;
import entity.TrangThai;
import entity.ViTri;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.util.List;

public class QuanLyDanhSachBan extends BorderPane {

    private final QuanLyDanhSachBan_DAO dao = new QuanLyDanhSachBan_DAO();
    private final ObservableList<BanAn> data = FXCollections.observableArrayList();

    private TableView<BanAn> tableView;
    
    // Form controls
    private TextField txtMa;
    private ComboBox<LoaiBan> cboLoai;
    private ComboBox<TrangThai> cboTrangThai;
    private ComboBox<ViTri> cboViTri;

    private Button btnThem, btnSua, btnClear;
    
    // Search controls
    private TextField txtSearch;
    private ComboBox<LoaiBan> cboLocLoai;
    private ComboBox<TrangThai> cboLocTrangThai;

    // Constructor thay vì start()
    public QuanLyDanhSachBan() {
        initializeUI();
    }
    private void initializeUI() {
        this.getStyleClass().add("quan-ly-ban-an-root");
        
        VBox root = new VBox(12);
        root.setPadding(new Insets(14));
        root.getStyleClass().add("root");

        Label title = new Label("Quản Lý Bàn Ăn");
        title.getStyleClass().add("main-title");

        HBox body = new HBox(16);
        VBox.setVgrow(body, Priority.ALWAYS);

        VBox left = createLeftPane();
        HBox.setHgrow(left, Priority.ALWAYS);

        VBox right = createRightPane();
        right.setPrefWidth(380);

        body.getChildren().addAll(left, right);
        root.getChildren().addAll(title, body);

        this.setCenter(root);

        // Tải CSS
        try {
            this.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Không tìm thấy CSS: " + e.getMessage());
        }

        loadData();
    }

    private VBox createLeftPane() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(6));

        // Search bar
        HBox searchBar = new HBox(8);
        txtSearch = new TextField();
        txtSearch.setPromptText("Tìm mã hoặc vị trí...");
        txtSearch.getStyleClass().add("input");
        HBox.setHgrow(txtSearch, Priority.ALWAYS);

        Button btnSearch = new Button("Tìm");
        btnSearch.getStyleClass().addAll("button", "btn-outline");
        
        // ComboBox lọc loại bàn
        cboLocLoai = new ComboBox<>();
        cboLocLoai.getItems().add(null); // giá trị mặc định là không lọc
        cboLocLoai.getItems().addAll(LoaiBan.values());
        cboLocLoai.setPromptText("Loại bàn");

        // ComboBox lọc trạng thái
        cboLocTrangThai = new ComboBox<>();
        cboLocTrangThai.getItems().add(null);
        cboLocTrangThai.getItems().addAll(TrangThai.values());
        cboLocTrangThai.setPromptText("Trạng thái");
        
        HBox.setHgrow(cboLocLoai, Priority.ALWAYS);
        HBox.setHgrow(cboLocTrangThai, Priority.ALWAYS);

        Button btnThemMoi = new Button("Thêm mới");
        btnThemMoi.getStyleClass().addAll("button", "btn-primary");
        
        btnSearch.getStyleClass().add("search-control");
        btnThemMoi.getStyleClass().add("search-control");
        cboLocLoai.getStyleClass().add("search-control");
        cboLocTrangThai.getStyleClass().add("search-control");


        searchBar.getChildren().addAll(txtSearch, btnSearch, cboLocLoai, cboLocTrangThai, btnThemMoi);

        // Thêm listener cho txtSearch để lọc khi nhập
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData();
        });

        // Thêm listener cho cboLocLoai để lọc khi chọn
        cboLocLoai.valueProperty().addListener((observable, oldValue, newValue) -> {
            filterData();
        });

        // Thêm listener cho cboLocTrangThai để lọc khi chọn
        cboLocTrangThai.valueProperty().addListener((observable, oldValue, newValue) -> {
            filterData();
        });

        btnSearch.setOnAction(e -> filterData());
        
        btnThemMoi.setOnAction(e -> {
        	clearForm();
            String maMoi = dao.generateMaBan(); // sinh mã mới
            txtMa.setText(maMoi);               // hiển thị vào ô Mã
            cboLoai.setValue(null);
            cboTrangThai.setValue(null);
            cboViTri.setValue(null);

            btnThem.setDisable(false);
            btnSua.setDisable(true);

            tableView.getSelectionModel().clearSelection();
            
            Platform.runLater(() -> {
                cboLoai.requestFocus();
                if (!cboLoai.isShowing()) {
                    cboLoai.show(); 
                }
            });
        });


        // Table
        tableView = new TableView<>();
        tableView.setItems(data);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        TableColumn<BanAn, Number> colStt = new TableColumn<>("STT");
        colStt.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(tableView.getItems().indexOf(c.getValue()) + 1));
        colStt.setMaxWidth(70);

        TableColumn<BanAn, String> colMa = new TableColumn<>("Mã");
        colMa.setCellValueFactory(new PropertyValueFactory<>("maBan"));

        TableColumn<BanAn, String> colLoai = new TableColumn<>("Loại");
        colLoai.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(
                cell.getValue().getLoai() != null ? cell.getValue().getLoai().name() : ""));

        TableColumn<BanAn, String> colTrangThai = new TableColumn<>("Trạng thái");
        colTrangThai.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(
                cell.getValue().getTrangThai() != null ? cell.getValue().getTrangThai().name() : ""));

        TableColumn<BanAn, String> colViTri = new TableColumn<>("Vị trí");
        colViTri.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(
                cell.getValue().getViTri() != null ? cell.getValue().getViTri().name() : ""));

        tableView.getColumns().addAll(colStt, colMa, colLoai, colTrangThai, colViTri);

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> onTableSelectionChanged(newV));

        box.getChildren().addAll(searchBar, new Label("Danh sách Bàn Ăn"), tableView);
        return box;
    }

    private VBox createRightPane() {
        VBox card = new VBox(12);
        card.getStyleClass().add("right-pane-box");
        card.setPadding(new Insets(12));

        Label title = new Label("Thông tin bàn ăn");
        title.getStyleClass().add("panel-title");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(38);
        c1.setHalignment(HPos.LEFT);

        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(62);
        c2.setHgrow(Priority.ALWAYS);

        form.getColumnConstraints().addAll(c1, c2);

        // controls
        txtMa = new TextField();
        txtMa.setEditable(false);
        txtMa.getStyleClass().add("input");

        cboLoai = new ComboBox<>();
        cboLoai.getItems().addAll(LoaiBan.values());
        cboLoai.setMaxWidth(Double.MAX_VALUE);
        cboLoai.getStyleClass().add("input");

        cboTrangThai = new ComboBox<>();
        cboTrangThai.getItems().addAll(TrangThai.values());
        cboTrangThai.setMaxWidth(Double.MAX_VALUE);
        cboTrangThai.getStyleClass().add("input");

        cboViTri = new ComboBox<>();
        cboViTri.getItems().addAll(ViTri.values());
        cboViTri.setMaxWidth(Double.MAX_VALUE);
        cboViTri.getStyleClass().add("input");
        

        // ensure all grow
        txtMa.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(txtMa, Priority.ALWAYS);
        GridPane.setHgrow(cboLoai, Priority.ALWAYS);
        GridPane.setHgrow(cboTrangThai, Priority.ALWAYS);
        GridPane.setHgrow(cboViTri, Priority.ALWAYS);

        // add to form
        form.add(new Label("Mã:"), 0, 0); form.add(txtMa, 1, 0);
        form.add(new Label("Loại:"), 0, 1); form.add(cboLoai, 1, 1);
        form.add(new Label("Trạng thái:"), 0, 2); form.add(cboTrangThai, 1, 2);
        form.add(new Label("Vị trí:"), 0, 3); form.add(cboViTri, 1, 3);

        // Buttons - now placed vertically below the form
        btnThem = new Button("Thêm"); 
        btnThem.getStyleClass().addAll("button", "btn-primary");
        btnThem.setMaxWidth(Double.MAX_VALUE);
        
        btnSua = new Button("Sửa"); 
        btnSua.getStyleClass().addAll("button", "btn-accent");
        btnSua.setMaxWidth(Double.MAX_VALUE);
        
        btnClear = new Button("Làm mới"); 
        btnClear.getStyleClass().addAll("button", "btn-muted");
        btnClear.setMaxWidth(Double.MAX_VALUE);

        // Create button container with vertical layout
        HBox buttonContainer = new HBox(8, btnThem, btnSua, btnClear);
        buttonContainer.setPadding(new Insets(10, 0, 0, 0));
        buttonContainer.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(buttonContainer, new Insets(20, 0, 0, 0));
        
        Separator separator = new Separator();
        separator.setPrefWidth(Double.MAX_VALUE);  // full chiều ngang
        separator.setStyle("-fx-background-color: #cccccc; -fx-opacity: 0.3;"); // màu nhạt
        VBox.setMargin(separator, new Insets(20, 0, 0, 0));

        Image img = new Image(getClass().getResourceAsStream("/img/Logo.png")); // đường dẫn file ảnh trong resources
        ImageView imageView = new ImageView(img);
        
        imageView.setFitWidth(180);   // vừa phải
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        
        Label lblFooter = new Label("Ứng dụng quản lý nhà hàng 2BT");
        lblFooter.getStyleClass().add("footer-text"); // nếu muốn CSS
        lblFooter.setAlignment(Pos.CENTER);
        
        VBox imageContainer = new VBox(6, imageView, lblFooter);
        imageContainer.setAlignment(Pos.CENTER);   // căn giữa theo chiều ngang
        imageContainer.setPadding(new Insets(70, 0, 0, 0)); // khoảng cách trên 20px từ các nút
        
        // Handlers
        btnThem.setOnAction(e -> handleAdd());
        btnSua.setOnAction(e -> handleUpdate());
        btnClear.setOnAction(e -> clearForm());

        card.getChildren().addAll(title, form, buttonContainer, separator,imageContainer);
        return card;
    }

    private void onTableSelectionChanged(BanAn b) {
        if (b == null) {
            clearForm();
            return;
        }
        txtMa.setText(b.getMaBan());
        cboLoai.setValue(b.getLoai());
        cboTrangThai.setValue(b.getTrangThai());
        cboViTri.setValue(b.getViTri());

        btnThem.setDisable(true);
        btnSua.setDisable(false);
    }

    // CRUD handlers
    private void handleAdd() {
        String ma = dao.generateMaBan();
        LoaiBan loai = cboLoai.getValue();
        TrangThai tt = cboTrangThai.getValue();
        ViTri vt = cboViTri.getValue();

        if (loai == null || tt == null || vt == null) {
            showAlert(Alert.AlertType.ERROR, "Vui lòng chọn đầy đủ Loại, Trạng Thái và Vị Trí.");
            return;
        }

        BanAn b = new BanAn(ma, loai, tt, vt);
        boolean ok = dao.addBanAn(b);
        if (ok) {
            showAlert(Alert.AlertType.INFORMATION, "Đã thêm bàn: " + ma);
            loadData();
            clearForm();
            // Sau khi thêm xong, focus vào Loại để tiếp tục nhập
            Platform.runLater(() -> {
                cboLoai.requestFocus();
                if (!cboLoai.isShowing()) {
                    cboLoai.show();
                }
            });
        } else {
            showAlert(Alert.AlertType.ERROR, "Thêm thất bại. Kiểm tra kết nối hoặc mã trùng.");
        }
    }

    private void handleUpdate() {
        String ma = txtMa.getText();
        if (ma == null || ma.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Chọn bàn cần sửa.");
            return;
        }
        LoaiBan loai = cboLoai.getValue();
        TrangThai tt = cboTrangThai.getValue();
        ViTri vt = cboViTri.getValue();
        if (loai == null || tt == null || vt == null) {
            showAlert(Alert.AlertType.ERROR, "Vui lòng chọn đầy đủ Loại, Trạng Thái và Vị Trí.");
            return;
        }
        BanAn b = new BanAn(ma, loai, tt, vt);
        boolean ok = dao.updateBanAn(b);
        if (ok) {
            showAlert(Alert.AlertType.INFORMATION, "Cập nhật thành công.");
            loadData();
        } else showAlert(Alert.AlertType.ERROR, "Cập nhật thất bại.");
    }

    // Hàm lọc dữ liệu
    private void filterData() {
        String kw = txtSearch.getText().trim();
        LoaiBan locLoai = cboLocLoai.getValue();
        TrangThai locTT = cboLocTrangThai.getValue();

        List<BanAn> filtered = dao.getAllBanAn(); // lấy tất cả
        
        // Lọc theo từ khóa tìm kiếm
        if (!kw.isEmpty()) {
            filtered.removeIf(b -> 
                !b.getMaBan().toUpperCase().contains(kw.toUpperCase())
                && !(b.getViTri() != null && b.getViTri().name().toUpperCase().contains(kw.toUpperCase()))
            );
        }
        
        // Lọc theo loại bàn
        if (locLoai != null) {
            filtered.removeIf(b -> b.getLoai() != locLoai);
        }
        
        // Lọc theo trạng thái
        if (locTT != null) {
            filtered.removeIf(b -> b.getTrangThai() != locTT);
        }

        data.clear();
        data.addAll(filtered);
        tableView.refresh();
    }

    // Helpers
    private void loadData() {
        Platform.runLater(() -> {
            data.clear();
            data.addAll(dao.getAllBanAn());
            tableView.refresh();
            // Reset các bộ lọc sau khi load lại dữ liệu
            txtSearch.clear();
            cboLocLoai.setValue(null);
            cboLocTrangThai.setValue(null);
        });
    }

    private void clearForm() {
        txtMa.clear();
        cboLoai.setValue(null);
        cboTrangThai.setValue(null);
        cboViTri.setValue(null);

        btnThem.setDisable(false);
        btnSua.setDisable(true);

        tableView.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert a = new Alert(type);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private boolean confirmDialog(String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.OK, ButtonType.CANCEL);
        a.setHeaderText(null);
        return a.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

}