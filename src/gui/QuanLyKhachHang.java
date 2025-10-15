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

    private final ObservableList<KhachHang> data = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
    	
    	 SideBar sidebar = new SideBar(null);

    	 
    	 
        // --- Thanh tìm kiếm + Lọc + Thêm khách hàng ---
    	Label lblSearchTitle = new Label("Tìm kiếm khách hàng");
    	lblSearchTitle.setStyle("""
    	    -fx-font-size: 13px;
    	    -fx-font-weight: bold;
    	    -fx-text-fill: #14274e;
    	""");
    	
//        TextField txtSearch = new TextField();
//        txtSearch.setPromptText("Tìm theo Tên/SĐT...");
//        txtSearch.setPrefWidth(250);
//        txtSearch.setStyle("-fx-padding: 8; -fx-background-radius: 8;");
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
    	    -fx-padding: 6 10 6 30; /* chừa khoảng trống cho icon */
    	""");
    	// đặt icon nằm đè lên trái ô tìm kiếm
    	StackPane searchBox = new StackPane(txtSearch, iconSearch);
    	StackPane.setAlignment(iconSearch, Pos.CENTER_LEFT);
    	StackPane.setMargin(iconSearch, new Insets(0, 0, 0, 8));
    	
    	
    	
    	
    	
    	
        ComboBox<String> cbSort = new ComboBox<>();
        cbSort.getItems().addAll("Tất cả xếp loại", "Khách thường", "Khách VIP");
        cbSort.getSelectionModel().selectFirst();
        cbSort.setStyle("-fx-padding: 6; -fx-background-radius: 8;");

        Separator sep = new Separator();
        sep.setOrientation(javafx.geometry.Orientation.VERTICAL);
        sep.setPrefHeight(30);

        Button btnAdd = new Button("➕ Thêm khách hàng mới");
        btnAdd.setStyle("""
            -fx-background-color: white;
            -fx-border-color: #ccc;
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-font-weight: 600;
            -fx-padding: 8 18;
            -fx-cursor: hand;
            -fx-transition: all 0.2s ease;
        """);

        // Hiệu ứng hover
        btnAdd.setOnMouseEntered(e -> btnAdd.setStyle("""
            -fx-background-color: #1e90ff;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-padding: 8 18;
        """));
        btnAdd.setOnMouseExited(e -> btnAdd.setStyle("""
            -fx-background-color: white;
            -fx-border-color: #ccc;
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-font-weight: 600;
            -fx-padding: 8 18;
            -fx-cursor: hand;
        """));

        btnAdd.setOnAction(e -> openAddModal(primaryStage));

        HBox searchBar = new HBox(20,searchBox, cbSort, sep, btnAdd);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(txtSearch, Priority.ALWAYS);
        searchBar.setPadding(new Insets(0, 0, 5, 0));

        // --- Tiêu đề ---
        Label lblTitle = new Label("Danh sách khách hàng");
        lblTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // --- Bảng dữ liệu khách hàng ---
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

        // Nút hành động (sửa)
//        colAction.setCellFactory(param -> new TableCell<>() {
//            private final Button btnEdit = new Button("✏");
//            {
//                btnEdit.setStyle("-fx-background-color: transparent; -fx-text-fill: red; -fx-font-size: 14px; -fx-cursor: hand;");
//                btnEdit.setOnAction(e -> {
//                    KhachHang c = getTableView().getItems().get(getIndex());
//                    openEditModal(primaryStage, c);
//                });
//            }
//            @Override
//            protected void updateItem(Void item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty) setGraphic(null);
//                else setGraphic(btnEdit);
//            }
//        });
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit = new Button();
            private final HBox box = new HBox();

            {
                // căn giữa trong ô
                box.setAlignment(Pos.CENTER);
                box.setPadding(new Insets(5));

                // tạo icon
                Image imgEdit = new Image(getClass().getResource("/img/ChinhSua.png").toExternalForm());
                ImageView imgView = new ImageView(imgEdit);
                imgView.setFitWidth(18);
                imgView.setFitHeight(18);

                // cấu hình nút
                btnEdit.setGraphic(imgView);
                btnEdit.setStyle("-fx-background-color: transparent;");
                btnEdit.setCursor(Cursor.HAND);

                // thêm nút vào box
                box.getChildren().add(btnEdit);

                // xử lý sự kiện click
                btnEdit.setOnAction(e -> {
                    KhachHang c = getTableView().getItems().get(getIndex());
                    openEditModal(primaryStage, c);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(box); // ✅ quan trọng: dùng box chứ không phải btnEdit
                }
            }
        });

        

        table.getColumns().addAll(colSTT, colID, colName, colPhone, colPoints, colAction);

        // Dữ liệu mẫu
        for (int i = 1; i <= 15; i++) {
            data.add(new KhachHang("KH0000" + i, "HAPYPY", "0928737722", 1.36));
        }
        table.setItems(data);

        // --- Style Table ---
        table.setStyle("""
            -fx-background-color: white;
            -fx-border-color: #ccc;
            -fx-border-radius: 8;
            -fx-font-size: 14px;
        """);

//        table.setRowFactory(tv -> {
//            TableRow<KhachHang> row = new TableRow<>();
//            row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: #e6ecff;"));
//            row.setOnMouseExited(e -> row.setStyle(""));
//            return row;
//        });

        table.widthProperty().addListener((obs, oldW, newW) -> {
            table.lookupAll(".column-header-background").forEach(node -> node.setStyle("-fx-background-color: #f2f2f2;"));
        });
//        VBox searchSection = new VBox(5, lblSearchTitle, searchBar);
//        VBox root = new VBox(12, searchSection, lblTitle, table);
//        root.setPadding(new Insets(20));
//        root.setStyle("-fx-background-color: #fdfdfd;");
//
//        Scene scene = new Scene(root, 950, 600);
//        primaryStage.setTitle("Quản lý khách hàng");
//        primaryStage.setScene(scene);
//        primaryStage.show();
        VBox searchSection = new VBox(5, lblSearchTitle, searchBar);
        VBox content = new VBox(12, searchSection, lblTitle, table);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #fdfdfd;");
        
        //  Bố cục chính
        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(content);

        // Căn chỉnh sidebar và nội dung
        BorderPane.setMargin(content, new Insets(10, 10, 10, 10));

        // Tùy chỉnh kích thước sidebar
        sidebar.setPrefWidth(230);

        // Tạo scene
        Scene scene = new Scene(root, 1200, 700);
        scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Quản lý khách hàng");
        primaryStage.show();
    }

    // --- Modal thêm khách hàng ---
    private void openAddModal(Stage owner) {
        openModal(owner, "Thêm khách hàng mới", null);
    }

    // --- Modal chỉnh sửa khách hàng ---
    private void openEditModal(Stage owner, KhachHang kh) {
        openModal(owner, "Chỉnh sửa thông tin khách hàng", kh);
    }

    // --- Hàm chung hiển thị modal ---
    private void openModal(Stage owner, String title, KhachHang kh) {
        Stage modal = new Stage();
        modal.initOwner(owner);
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setTitle(title);


        // 🏷️ Tiêu đề + đường line
        Label lblTitle = new Label(title);
        lblTitle.setStyle("""
            -fx-font-size: 22px;
            -fx-font-weight: bold;
            -fx-text-fill: #14274e;
        """);
        lblTitle.setAlignment(Pos.CENTER);

        Separator line = new Separator();
        line.setStyle("-fx-background-color: #14274e;");

        // 🧾 Nhãn + ô nhập liệu
        Label lblMa = new Label("Mã khách hàng:");
        Label lblTen = new Label("Họ tên khách hàng:");
        Label lblSDT = new Label("Số điện thoại:");
        Label lblDiem = new Label("Điểm tích lũy:");

        for (Label lbl : new Label[]{lblMa, lblTen, lblSDT, lblDiem}) {
            lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");
        }

        TextField txtMa = new TextField(kh == null ? "KH0000000199" : kh.getMaKhachHang());
        txtMa.setFocusTraversable(false);
        txtMa.setEditable(false);
        txtMa.setStyle("""
            -fx-opacity: 0.8;
            -fx-background-color: #f3f3f3;
            -fx-border-color: #ccc;
            -fx-border-radius: 5;
            -fx-background-radius: 5;
            -fx-padding: 6 10;
        """);

        TextField txtTen = new TextField(kh == null ? "" : kh.getTenKhachHang());
        TextField txtSDT = new TextField(kh == null ? "" : kh.getSoDienThoai());
        TextField txtDiem = new TextField(kh == null ? "0" : String.valueOf(kh.getDiemTichLuy()));
        txtDiem.setEditable(false);
        txtDiem.setStyle("""
            -fx-opacity: 0.8;
            -fx-background-color: #f3f3f3;
            -fx-border-color: #ccc;
            -fx-border-radius: 5;
            -fx-background-radius: 5;
            -fx-padding: 6 10;
        """);

        for (TextField tf : new TextField[]{txtTen, txtSDT}) {
            tf.setStyle("""
                -fx-background-color: #ffffff;
                -fx-border-color: #bbb;
                -fx-border-radius: 5;
                -fx-background-radius: 5;
                -fx-padding: 6 10;
            """);
        }

        // 📋 Form căn đều cột
        GridPane form = new GridPane();
        form.setVgap(18);
        form.setHgap(20);
        form.setPadding(new Insets(10, 40, 10, 40));
        form.addRow(0, lblMa, txtMa);
        form.addRow(1, lblTen, txtTen);
        form.addRow(2, lblSDT, txtSDT);
        form.addRow(3, lblDiem, txtDiem);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(35);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(65);
        form.getColumnConstraints().addAll(col1, col2);

        // 🖲️ Nút
        Button btnClose = new Button("Đóng");
        Button btnSave = new Button("Lưu");

        btnClose.setStyle("""
            -fx-background-color: #999;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-padding: 8 25;
            -fx-background-radius: 6;
        """);

        btnSave.setStyle("""
            -fx-background-color: #14274e;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-padding: 8 25;
            -fx-background-radius: 6;
        """);

        btnClose.setOnMouseEntered(e -> btnClose.setStyle("-fx-background-color: #777; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 25; -fx-background-radius: 6;"));
        btnClose.setOnMouseExited(e -> btnClose.setStyle("-fx-background-color: #999; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 25; -fx-background-radius: 6;"));

        btnSave.setOnMouseEntered(e -> btnSave.setStyle("-fx-background-color: #0e1a36; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 25; -fx-background-radius: 6;"));
        btnSave.setOnMouseExited(e -> btnSave.setStyle("-fx-background-color: #14274e; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 25; -fx-background-radius: 6;"));

        btnClose.setOnAction(e -> modal.close());
        btnSave.setOnAction(e -> {
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

        // 🧩 Layout chính
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



    public static void main(String[] args) {
        launch(args);
    }
}
