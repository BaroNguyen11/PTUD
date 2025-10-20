package gui;

import java.time.LocalDate;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import entity.NhanVien;
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

public class QuanLyNhanVien extends Application {

    private final ObservableList<NhanVien> data = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // --- Thanh tìm kiếm ---
        VBox searchSection = createSearchSection(primaryStage);
        SideBar sidebar = new SideBar(null);
        // --- Tiêu đề ---
        Label lblTitle = new Label("Danh sách nhân viên");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // --- Bảng dữ liệu ---
        TableView<NhanVien> table = createEmployeeTable(primaryStage);
        table.setItems(data);

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
        
        primaryStage.setTitle("Quản lý nhân viên");
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
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

    	
        TextField txtSearch = new TextField();
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

        ComboBox<String> cbSort = new ComboBox<>();
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

        return new VBox(5, lblSearchTitle,searchBar);
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
        colID.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getMaNhanVien()));
        colName.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getTenNhanVien()));
        colPhone.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getSoDienThoai()));
        colCCCD.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getCCCD()));
        colRole.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getChucVu()));
        colBirth.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getNgaySinh()));
        colStart.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getNgayVaoLam()));
        colEnd.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getNgayThoiViec()));

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
                    openEditModal(primaryStage, nv);
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

        // Dữ liệu mẫu
        for (int i = 1; i <= 10; i++) {
            data.add(new NhanVien("NV000" + i, "Nguyễn Văn A " + i, "09123456" + i, "0123456789" + i, "Nhân viên",
                    LocalDate.of(2000, 5, 15), LocalDate.of(2023, 1, 1), null));
        }

        table.setStyle("""
            -fx-background-color: white;
            -fx-border-color: #ccc;
            -fx-border-radius: 8;
            -fx-font-size: 14px;
        """);

        table.widthProperty().addListener((obs, oldW, newW) ->
                table.lookupAll(".column-header-background")
                        .forEach(node -> node.setStyle("-fx-background-color: #f2f2f2;"))
        );

        return table;
    }

 // =============================================================
 // ⚙️ MODAL THÊM / SỬA NHÂN VIÊN
 // =============================================================
 private void openAddModal(Stage owner) {
     openEmployeeModal(owner, "Thêm nhân viên mới", null);
 }

 private void openEmployeeModal(Stage owner, String title, NhanVien nv) {
     Stage modal = new Stage();
     modal.initOwner(owner);
     modal.initModality(Modality.APPLICATION_MODAL);
     modal.setTitle(title);

     // Tiêu đề
     Label lblTitle = new Label(title);
     lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #14274e;");
     Separator line = new Separator();

     // Gọi form riêng
     GridPane form = createEmployeeForm(nv);
     Separator line1 = new Separator();

     // Gọi nút riêng
     HBox buttons = createModalButtons(modal, nv, form);

     VBox layout = new VBox(20, lblTitle, line, form, line1,buttons);
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
 // 🧩 GRIDPANE FORM NHÂN VIÊN (giống createCustomerForm)
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

     TextField txtMa = new TextField(nv == null ? "NV00000002" : nv.getMaNhanVien());
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
 private void openEditModal(Stage owner, NhanVien nv) {
	    Stage modal = new Stage();
	    modal.initOwner(owner);
	    modal.initModality(Modality.APPLICATION_MODAL);
	    modal.setTitle("Thôi việc nhân viên");

	    // === Tiêu đề ===
	    Label lblTitle = new Label("Thôi việc nhân viên");
	    lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #14274e;");
	    Separator line = new Separator();

	    // === Thông tin nhân viên ===
	    GridPane info = new GridPane();
	    info.setVgap(10);
	    info.setHgap(15);
	    info.setPadding(new Insets(10, 40, 10, 40));

	    Label lblMa = new Label("Mã nhân viên:");
	    Label lblTen = new Label("Họ tên:");
	    Label lblChucVu = new Label("Chức vụ:");
	    Label lblSDT = new Label("Số điện thoại:");
	    Label lblNgaySinh = new Label("Ngày sinh:");
	    Label lblNgayVao = new Label("Ngày vào làm:");

	    for (Label lbl : new Label[]{lblMa, lblTen, lblChucVu, lblSDT, lblNgaySinh, lblNgayVao})
	        lbl.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #333;");

	    Label valMa = new Label(nv.getMaNhanVien());   
	    Label valTen = new Label(nv.getTenNhanVien());
	    Label valChucVu = new Label(nv.getChucVu());
	    Label valSDT = new Label(nv.getSoDienThoai());
	    Label valNgaySinh = new Label(nv.getNgaySinh() != null ? nv.getNgaySinh().toString() : "");
	    Label valNgayVao = new Label(nv.getNgayVaoLam() != null ? nv.getNgayVaoLam().toString() : "");
	    for (Label lbl1 : new Label[]{valMa, valTen, valChucVu, valSDT, valNgaySinh, valNgayVao})
	        lbl1.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");

	    info.addRow(0, lblMa, valMa);
	    info.addRow(1, lblTen, valTen);
	    info.addRow(2, lblChucVu, valChucVu);
	    info.addRow(3, lblSDT, valSDT);
	    info.addRow(4, lblNgaySinh, valNgaySinh);
	    info.addRow(5, lblNgayVao, valNgayVao);

	    ColumnConstraints c1 = new ColumnConstraints();
	    c1.setPercentWidth(35);
	    ColumnConstraints c2 = new ColumnConstraints();
	    c2.setPercentWidth(65);
	    info.getColumnConstraints().addAll(c1, c2);
	    
	    // === Cảnh báo
	    Label lblWarning = new Label("⚠️ Bạn có chắc chắn muốn cho nhân viên này thôi việc?");
	    lblWarning.setStyle("-fx-font-size: 14px; -fx-text-fill: #d9534f; -fx-font-weight: bold;");
	    lblWarning.setWrapText(true);

	    Separator line1 = new Separator();

	    // === Nút hành động ===
	    Button btnClose = new Button("Đóng");
	    Button btnConfirm = new Button("Cho thôi việc");
	    styleSecondaryButton(btnClose);
	    stylePrimaryDarkButton(btnConfirm);

	    btnClose.setOnAction(e -> modal.close());

	    btnConfirm.setOnAction(e -> {
	        // Lần xác nhận thứ 2
	        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
	        confirmAlert.setTitle("Xác nhận");
	        confirmAlert.setHeaderText("Bạn có chắc chắn muốn cho nhân viên này thôi việc?");
	        confirmAlert.setContentText("Hành động này sẽ không thể hoàn tác!");

	        ButtonType btnYes = new ButtonType("Đồng ý", ButtonBar.ButtonData.OK_DONE);
	        ButtonType btnNo = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);
	        confirmAlert.getButtonTypes().setAll(btnYes, btnNo);

	        // 🎨 Áp dụng CSS
	        DialogPane confirmPane = confirmAlert.getDialogPane();
	        confirmPane.getStyleClass().add("confirmation");
	        confirmPane.getStylesheets().add(
	            getClass().getResource("/application/application.css").toExternalForm()
	        );

	        confirmAlert.showAndWait().ifPresent(response -> {
	            if (response == btnYes) {
	                // Cập nhật trạng thái thôi việc
	                nv.setNgayThoiViec(LocalDate.now());
	                modal.close();

	                // Hiển thị thông báo thành công
	                Alert success = new Alert(Alert.AlertType.INFORMATION);
	                success.setTitle("Thành công");
	                success.setHeaderText(null);
	                success.setContentText("✅ Đã cho nhân viên " + nv.getTenNhanVien() + " thôi việc thành công!");

	                // 🎨 Áp dụng CSS cho alert thành công
	                DialogPane successPane = success.getDialogPane();
	                successPane.getStyleClass().add("information");
	                successPane.getStylesheets().add(
	                    getClass().getResource("/application/application.css").toExternalForm()
	                );

	                success.showAndWait();
	            }
	        });
	    });


	    BorderPane buttonPane = new BorderPane();
	    buttonPane.setLeft(btnClose);
	    buttonPane.setRight(btnConfirm);
	    BorderPane.setMargin(btnClose, new Insets(0, 0, 0, 40));
	    BorderPane.setMargin(btnConfirm, new Insets(0, 40, 0, 0));

	    // === Layout tổng ===
	    VBox layout = new VBox(20, lblTitle, line, info, lblWarning, line1,buttonPane);
	    layout.setPadding(new Insets(25));
	    layout.setStyle("""
	        -fx-background-color: white;
	        -fx-border-color: #ddd;
	        -fx-border-radius: 10;
	        -fx-background-radius: 10;
	    """);

	    Scene scene = new Scene(layout, 500, 450);
	    modal.setScene(scene);
	    modal.showAndWait();
	}



 // =============================================================
 // 🎛️ NÚT TRONG MODAL (giống createModalButtons của khách hàng)
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

         if (nv == null) {
             data.add(new NhanVien(
                 txtMa.getText(),
                 txtTen.getText(),
                 txtSDT.getText(),
                 txtCCCD.getText(),
                 cbChucVu.getValue(),
                 dpNgaySinh.getValue(),
                 LocalDate.now(),
                 null
             ));
         } else {
             nv.setTenNhanVien(txtTen.getText());
             nv.setSoDienThoai(txtSDT.getText());
             nv.setCCCD(txtCCCD.getText());
             nv.setChucVu(cbChucVu.getValue());
             nv.setNgaySinh(dpNgaySinh.getValue());
         }
         modal.close();
     });

     Region spacer = new Region();
     HBox.setHgrow(spacer, Priority.ALWAYS);

     HBox buttons = new HBox(15, btnClose, spacer, btnSave);
     buttons.setAlignment(Pos.CENTER);
     buttons.setPadding(new Insets(15, 40, 25, 40));

     return buttons;
 }

    // =============================================================
    // 🎨 STYLE
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
