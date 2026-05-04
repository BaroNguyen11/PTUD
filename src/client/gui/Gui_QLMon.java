
package client.gui;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import client.ctrl.QLMon_Ctrl;
import common.entity.MonAn;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import client.utils.ImageCacheManager;
import client.utils.SupabaseImageUploader;

public class Gui_QLMon extends BorderPane {

    private ComboBox<String> cboLoai;
    private QLMon_Ctrl control;
    private VBox selectedMon = null;
    private List<MonAn> dsMon;
    private TextField timKiem;

    // Sử dụng FlowPane thay vì VBox để các card tự động dàn hàng
    private FlowPane flowPaneDSMon;

    private File selectedImageFile;

    public Gui_QLMon() {
        control = new QLMon_Ctrl();
        initializeUI();
    }

    private void initializeUI() {
        // --- CẤU HÌNH BACKGROUND CHUNG ---
        this.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa 0%, #e9ecef 100%);");

        // 1. Header (Đặt ở TOP)
        VBox header = createModernHeader();
        this.setTop(header);

        // 2. Main Body (Đặt ở CENTER)
        VBox body = new VBox(20);
        body.setPadding(new Insets(10, 30, 20, 30));

        // Thanh tìm kiếm và bộ lọc
        VBox filterBar = taoPhanTimKiem();

        // Lấy dữ liệu
        dsMon = control.layDSMon();

        // Tạo vùng hiển thị danh sách (ScrollPane chứa FlowPane)
        VBox listArea = taoPhanDanhSach(dsMon);

        // QUAN TRỌNG: Cho phép listArea giãn hết chiều cao còn lại -> Kích hoạt Scroll
        VBox.setVgrow(listArea, Priority.ALWAYS);

        body.getChildren().addAll(filterBar, listArea);

        this.setCenter(body);

        // Animation Fade In
        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), this);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        // Load CSS fallback
        try {
            this.getStylesheets().add(getClass().getResource("/css/qlmon.css").toExternalForm());
        } catch (Exception e) {
            // System.err.println("CSS not found");
        }
        
        // Đăng ký nhận thông báo real-time
        dangKyNhanThongBao();
    }

    private common.DataChangeListener dataChangeListener;

    private void dangKyNhanThongBao() {
        try {
            common.DataChangeNotifierRemote notifier = client.RmiClientProvider.get("DataChangeNotifierRemote", common.DataChangeNotifierRemote.class);
            if (notifier != null) {
                dataChangeListener = new java.rmi.server.UnicastRemoteObject() {
                    @Override
                    public void onDataChanged(String collection, String action, String documentId) throws java.rmi.RemoteException {
                        if ("MonAn".equals(collection)) {
                            // Cập nhật UI trên JavaFX thread
                            javafx.application.Platform.runLater(() -> {
                                System.out.println("[Real-time] Cập nhật danh sách món ăn từ Server");
                                taiLaiDanhSachMonAnMoiNhat();
                            });
                        }
                    }
                };
                notifier.registerListener((common.DataChangeListener) dataChangeListener);
            }
        } catch (Exception e) {
            System.err.println("Không thể đăng ký nhận thông báo real-time: " + e.getMessage());
        }
    }

    // ===== HEADER =====
    private VBox createModernHeader() {
        VBox header = new VBox(5);
        header.setPadding(new Insets(20));
        header.setAlignment(Pos.CENTER_LEFT);

        header.setStyle(
                "-fx-background-color: #082744;" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 5);"
        );

        Label title = new Label("QUẢN LÝ MÓN ĂN");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: white;");

        Label subtitle = new Label("Quản lý danh sách, thông tin và hình ảnh món ăn");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        subtitle.setStyle("-fx-text-fill: rgba(255,255,255,0.8);");

        header.getChildren().addAll(title, subtitle);

        // Container bọc ngoài để tạo margin so với mép trên
        VBox headerContainer = new VBox(header);
        headerContainer.setPadding(new Insets(20, 30, 0, 30));

        return headerContainer;
    }

    // ===== SEARCH & FILTER BAR =====
    public VBox taoPhanTimKiem() {
        HBox container = new HBox(15);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(15));
        container.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 1);"
        );

        // Ô tìm kiếm
        timKiem = new TextField();
        timKiem.setPromptText("🔍 Nhập mã món ăn...");
        timKiem.setPrefHeight(35);
        timKiem.setPrefWidth(250);
        timKiem.setStyle("-fx-background-radius: 6; -fx-border-color: #e9ecef; -fx-border-radius: 6; -fx-padding: 4 8;");

        // ComboBox Loại
        cboLoai = new ComboBox<>();
        String[] dsLoai = {"Tất cả", "Khai vị", "Món chính", "Ăn kèm", "Nước sốt", "Nước uống", "Tráng miệng"};
        cboLoai.getItems().addAll(dsLoai);
        cboLoai.setValue("Tất cả");
        styleComboBox(cboLoai);
        cboLoai.setPrefWidth(150);

        // Button Tìm
        Button nutTimKiem = new Button("Tìm kiếm");
        styleButton(nutTimKiem, "#f1f3f5", "#495057");

        // Button Thêm Mới
        Button btnThemMon = new Button("✚ Thêm món");
        btnThemMon.setStyle("-fx-background-color: #082744; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 8 15; -fx-cursor: hand;");
        addHoverEffect(btnThemMon);

        // Spacer đẩy nút thêm sang phải
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        container.getChildren().addAll(timKiem, cboLoai, nutTimKiem, spacer, btnThemMon);

        // Events
        cboLoai.setOnAction(e -> timKiem());
        timKiem.setOnAction(e -> timKiem());
        nutTimKiem.setOnAction(e -> timKiem());
        btnThemMon.setOnAction(e -> xuLiThemMon());

        return new VBox(container);
    }

    // ===== LIST SECTION (FIX SCROLL & GAP) =====
    public VBox taoPhanDanhSach(List<MonAn> dsMon) {
        // Tạo FlowPane chứa các món
        flowPaneDSMon = new FlowPane();
        flowPaneDSMon.setPadding(new Insets(15)); // Padding trong FlowPane
        flowPaneDSMon.setHgap(25); // Khoảng cách ngang đều
        flowPaneDSMon.setVgap(25); // Khoảng cách dọc đều
        flowPaneDSMon.setAlignment(Pos.TOP_CENTER);
        flowPaneDSMon.setStyle("-fx-background-color: transparent;");
        flowPaneDSMon.setPrefWrapLength(1200); // Giúp FlowPane wrap đúng hơn

        // Load dữ liệu ban đầu
        capNhatFlowPane(dsMon);

        // Tạo ScrollPane chứa FlowPane
        ScrollPane scrollPane = new ScrollPane(flowPaneDSMon);
        scrollPane.setFitToWidth(true); // Quan trọng: card sẽ tự xuống dòng khi thu nhỏ
        scrollPane.setPannable(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.getStyleClass().add("edge-to-edge"); // Bỏ viền nếu có CSS hỗ trợ

        // Wrapper VBox
        VBox wrapper = new VBox(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS); // Cho phép ScrollPane giãn hết

        return wrapper;
    }

    // Helper: Cập nhật FlowPane thay vì VBox lồng nhau
    private void capNhatFlowPane(List<MonAn> list) {
        flowPaneDSMon.getChildren().clear();
        for (MonAn mon : list) {
            VBox card = taoMonAn(mon);
            flowPaneDSMon.getChildren().add(card);
        }
    }

    // ===== ITEM CARD (UI MỚI) =====
    public VBox taoMonAn(MonAn mon) {
        // Card Container Style
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setPrefWidth(230); // Width cố định
        card.setPrefHeight(290);
        card.setAlignment(Pos.TOP_CENTER);

        String defaultStyle =
                "-fx-background-color: white;" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 3);" +
                        "-fx-cursor: hand;";

        card.setStyle(defaultStyle);

        // --- IMAGE ---
        ImageView imgMonAn = new ImageView();
        imgMonAn.setFitWidth(180);
        imgMonAn.setFitHeight(140);
        imgMonAn.setPreserveRatio(false); // Fill khung

        Rectangle clip = new Rectangle(180, 140);
        clip.setArcWidth(15);
        clip.setArcHeight(15);
        imgMonAn.setClip(clip);

        // Load Ảnh
        if (mon.getHinhAnh() != null && !mon.getHinhAnh().isEmpty()) {
            String SUPABASE_BASE_URL = "https://yxemxycygkhxygaydgcl.supabase.co/storage/v1/object/public/image/";
            String imagePath = ImageCacheManager.getImagePath(SUPABASE_BASE_URL, mon.getHinhAnh());
            if (imagePath != null) {
                try {
                    imgMonAn.setImage(new Image(imagePath));
                } catch (Exception e) {
                    loadDefaultImage(imgMonAn);
                }
            } else {
                loadDefaultImage(imgMonAn);
            }
        } else {
            loadDefaultImage(imgMonAn);
        }

        HBox imgContainer = new HBox(imgMonAn);
        imgContainer.setAlignment(Pos.CENTER);

        // --- INFO ---
        Label lblTenMon = new Label(mon.getTenMonAn());
        lblTenMon.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        lblTenMon.setStyle("-fx-text-fill: #2c3e50;");
        lblTenMon.setWrapText(true);
        lblTenMon.setMaxWidth(200);
        lblTenMon.setAlignment(Pos.CENTER);
        lblTenMon.setMaxHeight(45); // Giới hạn height tên

        Label lblMoTa = new Label(mon.getMoTa());
        lblMoTa.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        lblMoTa.setStyle("-fx-text-fill: #95a5a6;");
        lblMoTa.setWrapText(true);
        lblMoTa.setMaxWidth(200);
        lblMoTa.setAlignment(Pos.CENTER);
        if (lblMoTa.getText() != null && lblMoTa.getText().length() > 50) {
            lblMoTa.setText(lblMoTa.getText().substring(0, 47) + "...");
        }

        DecimalFormat dcm = new DecimalFormat("#,##0đ");
        Label lblGia = new Label(dcm.format(mon.getGiaTien()));
        lblGia.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        lblGia.setStyle("-fx-text-fill: #e67e22;"); // Cam

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        card.getChildren().addAll(imgContainer, lblTenMon, lblMoTa, spacer, lblGia);

        // --- EVENTS ---
        card.setOnMouseEntered(e -> {
            if (selectedMon != card) {
                card.setStyle(
                        "-fx-background-color: white;" +
                                "-fx-background-radius: 15;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 5);" +
                                "-fx-scale-x: 1.03; -fx-scale-y: 1.03;" +
                                "-fx-cursor: hand;"
                );
            }
        });

        card.setOnMouseExited(e -> {
            if (selectedMon != card) {
                card.setStyle(defaultStyle);
                card.setScaleX(1.0);
                card.setScaleY(1.0);
            }
        });

        card.setOnMouseClicked(e -> {
            if (selectedMon != null) {
                selectedMon.setStyle(defaultStyle);
                selectedMon.setScaleX(1.0);
                selectedMon.setScaleY(1.0);
            }
            selectedMon = card;
            // Highlight
            card.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-background-radius: 15;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);" +
                            "-fx-border-color: #082744; -fx-border-width: 2; -fx-border-radius: 15;"
            );

            showMonAnModal(mon);
        });

        return card;
    }

    // ===== LOGIC CHỨC NĂNG (ĐẦY ĐỦ) =====

    public void timKiem() {
        String maMon = timKiem.getText().trim();
        String loai = cboLoai.getValue();

        List<MonAn> dsLoc = control.locDSMon(dsMon, maMon, loai);

        // Cập nhật FlowPane
        capNhatFlowPane(dsLoc);
    }

    public void taiLaiDanhSachMonAnMoiNhat() {
        dsMon = control.layDSMon();
        capNhatFlowPane(dsMon);
    }

    public void xuLiThemMon() {
        showMonAnModalThemMon();
    }

    // ===== MODAL: SỬA MÓN ĂN (ĐẦY ĐỦ LOGIC) =====
    public void showMonAnModal(MonAn mon) {
        Stage primaryStage = (Stage) this.getScene().getWindow();
        BoxBlur blur = new BoxBlur(5, 5, 3);
        Parent rootPane = this.getScene().getRoot();
        rootPane.setEffect(blur);

        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.initOwner(primaryStage);
        modal.setTitle(mon.getTenMonAn());
        modal.setWidth(750);
        modal.setHeight(550);

        // --- UI Modal Layout ---
        VBox vboxTrai = new VBox(30);
        vboxTrai.setPrefWidth(300);
        vboxTrai.setPadding(new Insets(30, 10, 0, 10));
        vboxTrai.setAlignment(Pos.TOP_CENTER);

        StackPane stThemAnh = new StackPane();
        HBox boxHinhAnh = new HBox();
        boxHinhAnh.setPrefSize(240, 240);
        boxHinhAnh.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-radius: 10; -fx-background-radius: 10;");
        boxHinhAnh.setAlignment(Pos.CENTER);

        ImageView imgMonAn = new ImageView();
        imgMonAn.setFitWidth(230);
        imgMonAn.setFitHeight(230);
        imgMonAn.setPreserveRatio(true);
        Rectangle clip = new Rectangle(230, 230);
        clip.setArcWidth(25);
        clip.setArcHeight(25);
        imgMonAn.setClip(clip);

        // Load ảnh chi tiết
        if (mon.getHinhAnh() != null && !mon.getHinhAnh().isEmpty()) {
            String SUPABASE_BASE_URL = "https://yxemxycygkhxygaydgcl.supabase.co/storage/v1/object/public/image/";
            String imagePath = ImageCacheManager.getImagePath(SUPABASE_BASE_URL, mon.getHinhAnh());
            if (imagePath != null) {
                try {
                    imgMonAn.setImage(new Image(imagePath));
                } catch (Exception e) {
                    loadDefaultImage(imgMonAn);
                }
            } else {
                loadDefaultImage(imgMonAn);
            }
        } else {
            loadDefaultImage(imgMonAn);
        }

        boxHinhAnh.getChildren().add(imgMonAn);

        // Overlay hover
        StackPane overlay = new StackPane();
        Rectangle overlayRect = new Rectangle(240, 240);
        overlayRect.setFill(Color.rgb(255, 255, 255, 0.5));
        overlayRect.setArcWidth(10);
        overlayRect.setArcHeight(10);
        SVGPath overlayPlus = createSvgIcon(60, 24, "gray", "M12 3.75a.75.75 0 0 1 .75.75v6.75h6.75a.75.75 0 0 1 0 1.5h-6.75v6.75a.75.75 0 0 1-1.5 0v-6.75H4.5a.75.75 0 0 1 0-1.5h6.75V4.5a.75.75 0 0 1 .75-.75Z");
        overlay.getChildren().addAll(overlayRect, overlayPlus);
        overlay.setVisible(false);

        stThemAnh.getChildren().addAll(boxHinhAnh, overlay);
        vboxTrai.getChildren().add(stThemAnh);

        // --- Form Phải ---
        VBox vboxPhai = new VBox(10);
        vboxPhai.setPrefWidth(400);

        Label lblTieuDe = createModernSectionTitle("Thông tin món ăn", "#667EEA");

        TextField txtMa = new TextField();
        TextField txtTen = new TextField();
        TextField txtGia = new TextField();
        ComboBox<String> cboLoaiModal = new ComboBox<>();
        cboLoaiModal.getItems().addAll("Khai vị", "Món chính", "Ăn kèm", "Nước uống", "Nước sốt", "Tráng miệng");
        cboLoaiModal.setDisable(true);
        styleComboBox(cboLoaiModal);
        cboLoaiModal.setPrefWidth(250);

        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setPrefHeight(100);
        textArea.setEditable(false);
        textArea.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 6; -fx-background-radius: 6;");

        // Gán dữ liệu
        txtMa.setText(mon.getMaMonAn());
        txtTen.setText(mon.getTenMonAn());
        cboLoaiModal.setValue(mon.getLoaiMon());
        DecimalFormat dcm = new DecimalFormat("#");
        txtGia.setText(dcm.format(mon.getGiaTien()));
        textArea.setText(mon.getMoTa());

        // Set rows
        vboxPhai.getChildren().addAll(
                lblTieuDe,
                createInputField("Mã", txtMa, true),
                createInputField("Tên", txtTen, true),
                new HBox(15, createLabel("Loại"), cboLoaiModal),
                createInputField("Giá", txtGia, true),
                new HBox(15, createLabel("Mô tả"), textArea)
        );
        ((HBox) vboxPhai.getChildren().get(3)).setAlignment(Pos.CENTER_LEFT); // Align HBox Loai
        ((Label) ((HBox) vboxPhai.getChildren().get(3)).getChildren().get(0)).setMinWidth(110);
        ((Label) ((HBox) vboxPhai.getChildren().get(5)).getChildren().get(0)).setMinWidth(110);

        // Buttons
        Button btnQuayVe = new Button("Quay về");
        styleButton(btnQuayVe, "gray", "white");
        Button btnSua = new Button("Sửa");
        styleButton(btnSua, "#F4C430", "white");

        HBox hboxBTNDuoi = new HBox(20, btnQuayVe, btnSua);
        hboxBTNDuoi.setAlignment(Pos.CENTER_RIGHT);
        hboxBTNDuoi.setPadding(new Insets(20, 0, 0, 0));
        vboxPhai.getChildren().add(hboxBTNDuoi);

        // Logic Buttons
        btnQuayVe.setOnAction(e -> {
            modal.close();
            selectedImageFile = null;
        });

        btnSua.setOnAction(e -> {
            if (btnSua.getText().equals("Sửa")) {
                // Chuyển sang chế độ Edit
                xuLiSuaMon(txtTen, cboLoaiModal, textArea);
                txtGia.setEditable(true);
                txtGia.setStyle(txtTen.getStyle()); // Copy style edit

                // Event chọn ảnh
                stThemAnh.setCursor(Cursor.HAND);
                stThemAnh.setOnMouseEntered(ev -> overlay.setVisible(true));
                stThemAnh.setOnMouseExited(ev -> overlay.setVisible(false));
                stThemAnh.setOnMouseClicked(ev -> {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image", "*.jpg", "*.png", "*.jpeg"));
                    File selectedFile = fileChooser.showOpenDialog(modal);
                    if (selectedFile != null) {
                        selectedImageFile = selectedFile;
                        imgMonAn.setImage(new Image(selectedFile.toURI().toString()));
                    }
                });

                btnSua.setText("Lưu");
                styleButton(btnSua, "#082744", "white");
            } else {
                // Lưu
                if (xuLiLuuSauKhiSua(txtTen, cboLoaiModal, textArea, txtGia, txtMa.getText())) {
                    modal.close();
                }
            }
        });

        HBox layout = new HBox(20, vboxTrai, vboxPhai);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: white;");

        Scene scene = new Scene(layout);
        modal.setScene(scene);

        modal.setOnHiding(ev -> {
            selectedImageFile = null;
            rootPane.setEffect(null);
        });
        modal.showAndWait();
    }

    // ===== MODAL: THÊM MÓN (ĐẦY ĐỦ LOGIC) =====
    public void showMonAnModalThemMon() {
        Stage primaryStage = (Stage) this.getScene().getWindow();
        BoxBlur blur = new BoxBlur(5, 5, 3);
        Parent rootPane = this.getScene().getRoot();
        rootPane.setEffect(blur);

        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.initOwner(primaryStage);
        modal.setTitle("Thêm món ăn mới");
        modal.setWidth(750);
        modal.setHeight(550);

        // --- UI Trái: Ảnh ---
        VBox vboxTrai = new VBox(30);
        vboxTrai.setPrefWidth(300);
        vboxTrai.setAlignment(Pos.TOP_CENTER);
        vboxTrai.setPadding(new Insets(30, 0, 0, 0));

        StackPane stThemAnh = new StackPane();
        HBox boxHinhAnh = new HBox();
        boxHinhAnh.setPrefSize(240, 240);
        boxHinhAnh.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-radius: 10; -fx-background-radius: 10;");
        boxHinhAnh.setAlignment(Pos.CENTER);

        ImageView imgPreview = new ImageView();
        imgPreview.setFitWidth(230);
        imgPreview.setFitHeight(230);
        imgPreview.setVisible(false);
        Rectangle clip = new Rectangle(230, 230);
        clip.setArcWidth(25);
        clip.setArcHeight(25);
        imgPreview.setClip(clip);
        boxHinhAnh.getChildren().add(imgPreview);

        VBox iconAndLabel = new VBox(10);
        iconAndLabel.setAlignment(Pos.CENTER);
        SVGPath iconPlus = createSvgIcon(50, 24, "gray", "M12 3.75a.75.75 0 0 1 .75.75v6.75h6.75a.75.75 0 0 1 0 1.5h-6.75v6.75a.75.75 0 0 1-1.5 0v-6.75H4.5a.75.75 0 0 1 0-1.5h6.75V4.5a.75.75 0 0 1 .75-.75Z");
        Label lblHint = new Label("Chọn hình ảnh");
        iconAndLabel.getChildren().addAll(iconPlus, lblHint);

        stThemAnh.getChildren().addAll(boxHinhAnh, iconAndLabel);
        stThemAnh.setCursor(Cursor.HAND);

        stThemAnh.setOnMouseClicked(e -> {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image", "*.jpg", "*.png"));
            File f = fc.showOpenDialog(modal);
            if (f != null) {
                selectedImageFile = f;
                imgPreview.setImage(new Image(f.toURI().toString()));
                imgPreview.setVisible(true);
                iconAndLabel.setVisible(false);
            }
        });

        vboxTrai.getChildren().add(stThemAnh);

        // --- UI Phải: Form ---
        VBox vboxPhai = new VBox(10);
        vboxPhai.setPrefWidth(400);

        Label lblTieuDe = createModernSectionTitle("Thông tin món mới", "#667EEA");
        TextField txtMa = new TextField(control.taoMaMonMoi());
        txtMa.setEditable(false);
        TextField txtTen = new TextField();
        TextField txtGia = new TextField();
        DecimalFormat decimalFormat = new  DecimalFormat("#,###");
        txtGia.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) return;

            // 1. Chỉ giữ lại số (0-9), xóa chữ và ký tự đặc biệt
            String cleanString = newValue.replaceAll("[^\\d]", "");

            // 2. Nếu xóa xong mà rỗng thì set lại rỗng và thoát
            if (cleanString.isEmpty()) {
                txtGia.setText("");
                return;
            }

            try {
                // 3. Format lại có dấu phẩy
                double value = Double.parseDouble(cleanString);
                String formattedString = decimalFormat.format(value);

                // 4. Set lại text nếu có thay đổi
                if (!newValue.equals(formattedString)) {
                    txtGia.setText(formattedString);
                    txtGia.positionCaret(formattedString.length()); // Đưa con trỏ về cuối
                }
            } catch (NumberFormatException ex) {
                // Bỏ qua lỗi
            }
        });

        ComboBox<String> cboLoaiAdd = new ComboBox<>();
        cboLoaiAdd.getItems().addAll("Khai vị", "Món chính", "Ăn kèm", "Nước uống", "Nước sốt", "Tráng miệng");
        styleComboBox(cboLoaiAdd);
        cboLoaiAdd.setPrefWidth(250);

        TextArea txtMoTa = new TextArea();
        txtMoTa.setWrapText(true);
        txtMoTa.setPrefHeight(100);

        vboxPhai.getChildren().addAll(
                lblTieuDe,
                createInputField("Mã", txtMa, true),
                createInputField("Tên", txtTen, false),
                new HBox(15, createLabel("Loại"), cboLoaiAdd),
                createInputField("Giá", txtGia, false),
                new HBox(15, createLabel("Mô tả"), txtMoTa)
        );
        ((HBox) vboxPhai.getChildren().get(3)).setAlignment(Pos.CENTER_LEFT);
        ((Label) ((HBox) vboxPhai.getChildren().get(3)).getChildren().get(0)).setMinWidth(110);
        ((Label) ((HBox) vboxPhai.getChildren().get(5)).getChildren().get(0)).setMinWidth(110);

        Button btnHuy = new Button("Hủy");
        styleButton(btnHuy, "gray", "white");
        Button btnLuu = new Button("Lưu món");
        styleButton(btnLuu, "#082744", "white");

        HBox btnBox = new HBox(20, btnHuy, btnLuu);
        btnBox.setAlignment(Pos.CENTER_RIGHT);
        btnBox.setPadding(new Insets(20, 0, 0, 0));
        vboxPhai.getChildren().add(btnBox);

        // Logic Lưu
        btnHuy.setOnAction(e -> modal.close());
        btnLuu.setOnAction(e -> {
            String ten = txtTen.getText().trim();
            if (ten.isEmpty()) {
                showAlert(AlertType.WARNING, "Lỗi", "Nhập tên món!");
                return;
            }
            if (cboLoaiAdd.getValue() == null) {
                showAlert(AlertType.WARNING, "Lỗi", "Chọn loại món!");
                return;
            }
            double gia = 0;

            try {

                String rawGia = txtGia.getText().trim().replace(",", "");
                if (rawGia.isEmpty()) {
                    showAlert(AlertType.WARNING, "Lỗi", "Vui lòng nhập giá!");
                    return;
                }
                gia = Double.parseDouble(rawGia);
            } catch (Exception ex) {
                showAlert(AlertType.WARNING, "Lỗi", "Giá không hợp lệ!");
                return;
            }
            if (selectedImageFile == null) {
                showAlert(AlertType.WARNING, "Lỗi", "Chọn ảnh món ăn!");
                return;
            }

            SupabaseImageUploader uploader = new SupabaseImageUploader();
            String slug = mapLoaiToSlug(cboLoaiAdd.getValue());
            try {
                String path = uploader.uploadImage(selectedImageFile, slug);
                MonAn m = new MonAn(txtMa.getText(), ten, cboLoaiAdd.getValue(), gia, txtMoTa.getText().trim(), path);
                if (control.themMonMoi(m)) {
                    showAlert(AlertType.INFORMATION, "Thành công", "Đã thêm món!");
                    modal.close();
                    taiLaiDanhSachMonAnMoiNhat();
                } else {
                    showAlert(AlertType.ERROR, "Lỗi", "Thêm thất bại!");
                }
            } catch (IOException ex) {
                showAlert(AlertType.ERROR, "Lỗi", "Upload ảnh thất bại!");
            }
        });

        HBox layout = new HBox(20, vboxTrai, vboxPhai);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: white;");

        Scene scene = new Scene(layout);
        modal.setScene(scene);
        modal.setOnHiding(ev -> {
            selectedImageFile = null;
            rootPane.setEffect(null);
        });
        modal.showAndWait();
    }

    // ===== HELPERS & XỬ LÝ PHỤ =====

    public void xuLiSuaMon(TextField txtTen, ComboBox cboLoai, TextArea textArea) {
        txtTen.setEditable(true);
        cboLoai.setDisable(false);
        textArea.setEditable(true);
        txtTen.setStyle("-fx-background-color: white; -fx-border-color: #3498db; -fx-border-radius: 6; -fx-padding: 5;");
        textArea.setStyle("-fx-border-color: #3498db; -fx-border-radius: 6;");
    }

    public boolean xuLiLuuSauKhiSua(TextField txtTen, ComboBox<String> cboLoai, TextArea textArea, TextField txtGia, String ma) {
        String ten = txtTen.getText().trim();
        if (ten.isEmpty()) {
            showAlert(AlertType.WARNING, "Lỗi", "Tên không được trống");
            return false;
        }

        double gia = 0;
        try {
            gia = Double.parseDouble(txtGia.getText().trim());
        } catch (Exception e) {
            showAlert(AlertType.WARNING, "Lỗi", "Giá sai định dạng");
            return false;
        }

        if (selectedImageFile == null) {
            showAlert(AlertType.WARNING, "Cảnh báo", "Vui lòng chọn ảnh (có thể chọn lại ảnh cũ nếu muốn giữ nguyên, hoặc code thêm logic giữ ảnh cũ)");
            // Lưu ý: Logic gốc của bạn bắt buộc chọn ảnh mới khi sửa.
            // Nếu muốn giữ ảnh cũ, cần check null selectedImageFile và không upload lại.
            return false;
        }

        SupabaseImageUploader uploader = new SupabaseImageUploader();
        try {
            String path = uploader.uploadImage(selectedImageFile, mapLoaiToSlug(cboLoai.getValue()));
            MonAn m = new MonAn(ma, ten, cboLoai.getValue(), gia, textArea.getText().trim(), path);
            if (control.capNhatMonAn(m)) {
                showAlert(AlertType.INFORMATION, "Thành công", "Cập nhật thành công!");
                taiLaiDanhSachMonAnMoiNhat();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private HBox createInputField(String labelText, TextField textField, boolean isReadOnly) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        Label label = createLabel(labelText);
        textField.setPrefWidth(250);
        textField.setEditable(!isReadOnly);

        if (isReadOnly) {
            textField.setStyle("-fx-background-color: #ECF0F1; -fx-border-color: #bdc3c7; -fx-border-radius: 6; -fx-padding: 5;");
        } else {
            textField.setStyle("-fx-background-color: white; -fx-border-color: #3498db; -fx-border-radius: 6; -fx-padding: 5;");
        }
        row.getChildren().addAll(label, textField);
        return row;
    }

    private Label createLabel(String text) {
        Label l = new Label(text);
        l.setMinWidth(110);
        l.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        l.setStyle("-fx-text-fill: #495057;");
        return l;
    }

    private void styleButton(Button btn, String bgColor, String textColor) {
        btn.setStyle("-fx-background-color: " + bgColor + "; -fx-text-fill: " + textColor + "; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 8 15; -fx-cursor: hand;");
        addHoverEffect(btn);
    }

    private void addHoverEffect(Node node) {
        node.setOnMouseEntered(e -> node.setStyle(node.getStyle() + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);"));
        node.setOnMouseExited(e -> node.setEffect(null));
    }

    private void styleComboBox(ComboBox<?> cbo) {
        cbo.setStyle("-fx-background-color: white; -fx-border-color: #ced4da; -fx-border-radius: 5; -fx-padding: 2;");
        cbo.setPrefHeight(35);
    }

    private void loadDefaultImage(ImageView imgView) {
        try {
            imgView.setImage(new Image(getClass().getResourceAsStream("/img/default-food.png")));
        } catch (Exception e) {
        }
    }

    private String mapLoaiToSlug(String loaiTen) {
        if (loaiTen == null) return "other";
        return switch (loaiTen) {
            case "Khai vị" -> "khaivi";
            case "Món chính" -> "monchinh";
            case "Ăn kèm" -> "ankem";
            case "Nước uống" -> "nuocuong";
            case "Nước sốt" -> "nuocsot";
            case "Tráng miệng" -> "trangmieng";
            default -> loaiTen.toLowerCase().replace(" ", "");
        };
    }

    private Label createModernSectionTitle(String title, String color) {
        Label label = new Label(title);
        label.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        label.setStyle("-fx-text-fill: " + color + "; -fx-padding: 2 0 2 10; -fx-border-width: 0 0 0 4; -fx-border-color: " + color + ";");
        return label;
    }

    public void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private SVGPath createSvgIcon(double size, double viewBox, String mau, String pathData) {
        SVGPath svg = new SVGPath();
        svg.setContent(pathData);
        svg.setScaleX(size / viewBox);
        svg.setScaleY(size / viewBox);
        svg.setStyle("-fx-fill: " + mau + ";");
        return svg;
    }
}
