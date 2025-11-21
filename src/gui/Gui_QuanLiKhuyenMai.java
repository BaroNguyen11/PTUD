package gui;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import control.Crl_QuanLiKM;
import entity.KhuyenMai;
import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Gui_QuanLiKhuyenMai extends BorderPane {
    // DANH SÁCH TOÀN CỤC
	private Crl_QuanLiKM control;
    ObservableList<String> dsTTMonAn = FXCollections.observableArrayList();
    ObservableList<String> dsMonChon = FXCollections.observableArrayList();
    ObservableList<String> dsKhuyenMai = FXCollections.observableArrayList();
    ObservableList<String> dsMonTemp = FXCollections.observableArrayList();
    ObservableList<String> dsLocMon = FXCollections.observableArrayList();
     
	private TextField txtMaKhuyenMai;
	private TextField txtTenKhuyenMai;
	private TextField txtDieuKienApDung;
	private TextField txtGiaTriToiDa;
	private TextField txtGiaTriGiam;
	private ToggleGroup radioGroup1;
	private ToggleGroup radioGroup2;
	private TableView<String> tableDSKM;
	private TableView<String> tableDSMonKMTheoMa = new TableView<String>(dsMonChon);
	private RadioButton radioCo;
	private RadioButton radioKhong;
	private RadioButton radioHoaDon;
	private RadioButton radioMonAn;
	private DatePicker dateBatDau;
	private DatePicker dateKetThuc;
	private Button btnChonMonAn;
	private Button btnSuaLuu;
	private final EventHandler<MouseEvent> blockMouse = Event::consume;
	private final EventHandler<KeyEvent> blockKey = Event::consume;
	private Button btnNgung;
	private ListView listMonAn;
	TableColumn<String, Void> colXoa = new TableColumn<>("Xóa");
	
	// Cờ sửa và lưu || True là sửa || False là lưu
	private boolean flagSuaLuu = true;
	
	// Cờ xóa và ngừng KM || True là ngừng || False là xóa
	private boolean flagNgungXoa = true;
	  
	// Cờ đg xem và đang thêm || True là đang thêm || False là đang xem
	private boolean flagXemThem = false;
	
	private ComboBox<String> cboTT;
	private ComboBox<String> cboLoai;
	private TextField txtTimKiem;
	private Button btnReset;
	private ComboBox<String> cboLocMon;
	private Button btnThemKM;
	private final Map<KeyCombination, Runnable> shortcuts = new HashMap<>();

    // Preload image để tránh load lại mỗi lần tạo cell (giảm lag scroll)
    private static final Image IMG_MON_AN = new Image("/img/monAn.png");
    private static final Image IMG_CHU_NHAT_XANH = new Image("/img/chuNhatXanh.png");
    private static final Image IMG_CHU_NHAT_VANG = new Image("/img/chuNhatVang.png");


   public Gui_QuanLiKhuyenMai() {
	   control  = new Crl_QuanLiKM();
       BorderPane mainLayout = new BorderPane();

       // Thêm phần bên trái và bên phải
       VBox phanTrai = taoPhanBenTrai();
       VBox phanPhai = taoPhanBenPhai();

       mainLayout.setLeft(phanTrai);
       mainLayout.setRight(phanPhai);
       mainLayout.setStyle("-fx-background-color: white;");
       this.getStylesheets().add(getClass().getResource("/css/qlkm.css").toExternalForm());
       
       // Set vào BorderPane chính (this)
       this.setCenter(mainLayout);
       
       // === Phím tắt ===
       
       /////Hàng f
       /// === Thêm khuyến mãi : F2
       KeyCombination f2 = new KeyCodeCombination(KeyCode.F2);
       /// === Truy cập tìm kiếm nhanh : F3
       KeyCombination f3 = new KeyCodeCombination(KeyCode.F3);
       /// === Tạo mới : F5
       KeyCombination f5 = new KeyCodeCombination(KeyCode.F5);
       /// === Lưu, sửa : F6
       KeyCombination f6 = new KeyCodeCombination(KeyCode.F6);
       /// === Xóa, ngừng KM : F9
       KeyCombination f9 = new KeyCodeCombination(KeyCode.F9);
       
       
       shortcuts.put(f3, () -> txtTimKiem.requestFocus());
       shortcuts.put(f2, () -> btnThemKM.fire());
       shortcuts.put(f5, () -> btnReset.fire());
       shortcuts.put(f6, () -> btnSuaLuu.fire());
       shortcuts.put(f9, () -> btnNgung.fire());
       
       
       this.sceneProperty().addListener((obs, oldScene, newScene) -> {
           if (newScene != null) {
               newScene.getAccelerators().putAll(shortcuts);
           }
           
           if(oldScene != null) {
           		oldScene.getAccelerators().clear();
           }
       });
       
   }
    public VBox taoPhanBenTrai() {
        // Ô tìm kiếm
        Label lblTimKiem = createModernSectionTitle("Tìm kiếm", "#667eea");
        //lblTimKiem.setGraphic(createSvgIcon(24, 24, "#667eea", "m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z"));
        
        txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Nhập mã khuyến mãi");
        txtTimKiem.getStyleClass().add("timKiem");
        Button nutTimKiem = new Button("Tìm kiếm");
        nutTimKiem.getStyleClass().add("button-timKiem");
        HBox oTimKiem = new HBox(10, txtTimKiem, nutTimKiem);
        oTimKiem.setAlignment(Pos.CENTER_LEFT);
        VBox vboxTimKiem = new VBox(5);
        vboxTimKiem.getChildren().addAll(lblTimKiem, oTimKiem);
        
        txtTimKiem.setTooltip(new Tooltip("Nhấn F3 để truy cập nhanh"));
        
        nutTimKiem.setOnAction(e -> {locKhuyenMai();});
        txtTimKiem.setOnAction(e -> {locKhuyenMai();});
        
        // ===== Lọc theo trạng thái =====
        Label lblLocTT = new Label("Trạng thái");
        lblLocTT.getStyleClass().add("fontTieuDeNho");
        
        cboTT = new ComboBox<>();
        cboTT.getItems().addAll("Tất cả", "Sắp diễn ra", "Đang diễn ra", "Đã kết thúc");
        cboTT.setValue("Tất cả"); // mặc định

        VBox vboxTT = new VBox(3, lblLocTT, cboTT);
        
        cboTT.setOnAction(e -> {locKhuyenMai();});

        // ===== Lọc theo loại =====
        Label lblLocLoai = new Label("Loại");
        lblLocLoai.getStyleClass().add("fontTieuDeNho");

        cboLoai = new ComboBox<>();
        cboLoai.getItems().addAll("Tất cả", "Hóa đơn", "Món ăn");
        cboLoai.setValue("Tất cả");
        
        VBox vboxLoai = new VBox(3);
        vboxLoai.getChildren().addAll(lblLocLoai, cboLoai);
        
        cboLoai.setOnAction(e -> {locKhuyenMai();});
        
        // ===== nút reset tới chơi =====
        
        VBox vboxReset = new VBox();
        
        btnReset = new Button();
        btnReset.setGraphic(createSvgIcon(25, 24,"gray","M16.023 9.348h4.992v-.001M2.985 19.644v-4.992m0 0h4.992m-4.993 0 3.181 3.183a8.25 8.25 0 0 0 13.803-3.7M4.031 9.865a8.25 8.25 0 0 1 13.803-3.7l3.181 3.182m0-4.991v4.99"));
        btnReset.setStyle("""
        		-fx-background-color: null;
        		-fx-border-radius: 5px;
        		-fx-border-color: #D9D9D9;
        		-fx-cursor: hand;
        		-fx-translate-y: -5;
        		""");
        
        btnReset.setOnAction(e -> taoMoiTimKiem());
        
        btnReset.setTooltip(new Tooltip("Nhấn F5 để dùng nhanh"));
        
        vboxReset.getChildren().addAll(btnReset);
        
        //Hbox tim kiem
        HBox hboxTimKiem = new HBox(10);
        hboxTimKiem.getChildren().addAll(vboxTimKiem, vboxLoai, vboxTT, vboxReset);
        hboxTimKiem.setAlignment(Pos.BOTTOM_LEFT);
        vboxLoai.setPadding(new Insets(5, 0, 0, 0));
        vboxTT.setPadding(new Insets(5, 0, 0, 0));
        vboxReset.setPadding(new Insets(36, 0, 0, 0));
        
        ///Tieu de danh sach va chu thich danh sach
        Label lblDanhSach = new Label("Danh sách khuyến mãi");
        lblDanhSach.getStyleClass().add("fontTieuDeNho");
        lblDanhSach.setMinWidth(200);

        //HBOX chú thích
        HBox hboxChuThich = new HBox(3);
        
        HBox hboxDangDienRa = new HBox(2);
        HBox hboxSapDienRa = new HBox(2);
        HBox hboxKetThuc = new HBox(2);
        
        Label lblDangDienRa = new Label("Đang diễn ra");
        Label lblSapDienRa = new Label("Sắp diễn ra");
        Label lblKetThuc = new Label("Đã kết thúc");
        
        lblDangDienRa.getStyleClass().add("fontTieuDeNho");
        lblSapDienRa.getStyleClass().add("fontTieuDeNho");
        lblKetThuc.getStyleClass().add("fontTieuDeNho");
        
        SVGPath iconDangDienRa = createSvgIcon(18, 24, "green", "m4.5 12.75 6 6 9-13.5");
        SVGPath iconSapDienRa = createSvgIcon(18, 24, "gray", "M12 6v6h4.5m4.5 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z");
        SVGPath iconKetThuc = createSvgIcon(18, 24, "red", "M6 18 18 6M6 6l12 12");
        
        hboxDangDienRa.getChildren().addAll(iconDangDienRa, lblDangDienRa);
        hboxDangDienRa.setAlignment(Pos.CENTER_LEFT);
        hboxSapDienRa.getChildren().addAll(iconSapDienRa, lblSapDienRa);
        hboxSapDienRa.setAlignment(Pos.CENTER_LEFT);
        hboxKetThuc.getChildren().addAll(iconKetThuc, lblKetThuc);
        hboxKetThuc.setAlignment(Pos.CENTER_LEFT);
        hboxKetThuc.setPadding(new Insets(0, 160, 0, 0));
        
        // === Button thêm khuyến mãi tới chơi ===
        
        btnThemKM = new Button("Thêm KM");
        btnThemKM.getStyleClass().add("btn-them");
        
        btnThemKM.setOnAction(e -> {
        		hanhDongThem();
        		flagXemThem = true;
        		
        		btnSuaLuu.setDisable(false);
        	});
        btnThemKM.setTooltip(new Tooltip("Nhấn F2 để dùng nhanh"));
        
        hboxChuThich.getChildren().addAll(hboxDangDienRa, hboxSapDienRa, hboxKetThuc, btnThemKM);
        
        HBox hboxChuThichAll = new HBox(10);
        hboxChuThichAll.getChildren().addAll(lblDanhSach, hboxChuThich);
        hboxChuThichAll.setAlignment(Pos.BOTTOM_RIGHT);

        //Danh sach khuyen mai
        VBox dsKhuyenMai = taoDanhSachKhuyenMai();
        
        

        //ALLLLL
        VBox vboxAll = new VBox(20);
        vboxAll.setPadding(new Insets(10, 20, 10, 20));
        vboxAll.setMinHeight(600);
        //vboxAll.setStyle("-fx-background-color: black");
        vboxAll.setMinWidth(800);
        vboxAll.getChildren().addAll(hboxTimKiem, hboxChuThichAll, dsKhuyenMai);

        return vboxAll;
    }

    public VBox taoDanhSachKhuyenMai() {
        VBox vboxAll = new VBox();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        List<String> dsKhuyenMaiList = control.layDanhSachCTKM();
        
        dsKhuyenMai = FXCollections.observableArrayList(dsKhuyenMaiList);
        
        tableDSKM = new TableView<>(dsKhuyenMai);
        tableDSKM.setPrefHeight(600);
        
        

        // Cột Mã KM
        TableColumn<String, String> colMa = new TableColumn<>("Mã");
        colMa.setCellValueFactory(cellData -> {
        		return new SimpleStringProperty(cellData.getValue().split(",")[0]);
        });
        colMa.setPrefWidth(100);

        // Cột Tên KM
        TableColumn<String, String> colTen = new TableColumn<>("Tên khuyến mãi");
        colTen.setCellValueFactory(cellData -> {
    			return new SimpleStringProperty(cellData.getValue().split(",")[1]);
        });
        colTen.setPrefWidth(300);

        // Cột Loại (Hóa đơn / Món ăn)
        TableColumn<String, String> colLoai = new TableColumn<>("Loại");
        colLoai.setCellValueFactory(cellData -> {
    			return new SimpleStringProperty(cellData.getValue().split(",")[8].equals("1") ? "Món ăn" : "Hóa Đơn");
        });
        colLoai.setPrefWidth(100);

        // Cột Giá trị giảm
        TableColumn<String, String> colGiam = new TableColumn<>("Giảm");
        colGiam.setCellValueFactory(cellData -> {
        		boolean giamGiaPT = cellData.getValue().split(",")[6].equals("1") ? true : false;
        		DecimalFormat dcm = new DecimalFormat("#,##0.0 VND");
    			return new SimpleStringProperty(giamGiaPT ?  cellData.getValue().split(",")[7] + "%" : dcm.format(Double.parseDouble(cellData.getValue().split(",")[7])) );
        });
        colGiam.setPrefWidth(200);
        
        // Cột trạng thái 
        TableColumn<String, String> colTT = new TableColumn<>("TT");
        colTT.setCellValueFactory(cellData -> {
            String trangThaiStr = cellData.getValue().split(",")[9];
            return new SimpleStringProperty(trangThaiStr);
        });
        colTT.setPrefWidth(57);
        
        colTT.setCellFactory(tc -> new TableCell<String, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(null);
                setGraphic(null);

                if (!empty && item != null) {
                    switch (item) {
                        case "1": // Đang diễn ra
                            setGraphic(createSvgIcon(24, 24, "green", "m4.5 12.75 6 6 9-13.5"));
                            break;
                        case "0": // Đã kết thúc
                            setGraphic(createSvgIcon(24, 24, "red", "M6 18 18 6M6 6l12 12"));
                            break;
                        case "2": // Sắp diễn ra
                            setGraphic(createSvgIcon(24, 24, "gray", "M12 6v6h4.5m4.5 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"));
                            break;
                        default:
                            setGraphic(null);
                    }
                    setAlignment(Pos.CENTER);
                }
            }
        });
        

        // Thêm tất cả cột vào bảng
        tableDSKM.getColumns().addAll(colMa, colTen, colLoai, colGiam, colTT);
        
        //CLICK
        tableDSKM.setOnMouseClicked(event -> {
        		//
        		flagXemThem = false;
        		
        		
        		String chuoi = tableDSKM.getSelectionModel().getSelectedItem();
        		
        		if(chuoi == null) {
        			return;
        		}
        	
        		///Ẩn col xóa đi
        		colXoa.setVisible(false);
        		///
            
            String[] chuoiTach = chuoi.split(",");
            
            //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DecimalFormat dcm = new  DecimalFormat("#,##0.0 đ");
            
            //Lấy thông tin 
            String maKM = chuoiTach[0];
            String tenKM = chuoiTach[1];
            boolean giamGiaPT = chuoiTach[6].equals("1") ? true : false;
            double giaTriGiam = Double.parseDouble(chuoiTach[7]);
            double giaTriToiDa = Double.parseDouble(chuoiTach[5]);
            boolean laMonAn = chuoiTach[8].equals("1") ? true : false;
            double dkApDung = Double.parseDouble(chuoiTach[4]);
            String trangThai = chuoiTach[9];
            
            
            LocalDate ngayBD = LocalDate.parse(chuoiTach[2], dtf);
            LocalDate ngayKT = LocalDate.parse(chuoiTach[3], dtf);
            
            
            
            //Set gia tri
            txtMaKhuyenMai.setText(maKM);
            txtTenKhuyenMai.setText(tenKM);
            txtGiaTriGiam.setText(giaTriGiam + "");
            if(giamGiaPT) {
            		radioCo.setSelected(true);
            		txtGiaTriToiDa.setText(giaTriToiDa + "");
            }else {
            		radioKhong.setSelected(true);
            }
            
            dateBatDau.setValue(ngayBD);
            dateKetThuc.setValue(ngayKT);
            
            if(laMonAn) {
            		radioMonAn.setSelected(true);
                dsMonChon = FXCollections.observableArrayList(control.layDSMonTheoMaKM(chuoi.split(",")[0]));
                tableDSMonKMTheoMa.setItems(dsMonChon);
            }else {
            		txtDieuKienApDung.setText(dkApDung + "");
            		radioHoaDon.setSelected(true);
            }
            
            btnChonMonAn.setDisable(true);
            		 
            if(txtTenKhuyenMai != null ) {
            		moKhoaChinhSua(false);
            }
            
            if (trangThai.equals("1")) {
                btnNgung.setDisable(false);
                btnNgung.setText("Ngừng KM");
                flagNgungXoa = true;
                
                	btnSuaLuu.setText("Sửa");
        			btnSuaLuu.getStyleClass().remove("btn-luu");
        			btnSuaLuu.getStyleClass().add("btn-sua");
                
                btnSuaLuu.setDisable(true);
            } else {
                if(trangThai.equals("2")) {
	                	//Đổi lại button
	        			btnSuaLuu.setText("Sửa");
	        			btnSuaLuu.getStyleClass().remove("btn-luu");
	        			btnSuaLuu.getStyleClass().add("btn-sua");
	        			btnSuaLuu.setDisable(false);
	        			flagSuaLuu = true;
	        			
	        			btnNgung.setDisable(false);
	        			btnNgung.setText("Xóa");
	        			flagNgungXoa = false;
	        			
                }else {
                		btnSuaLuu.setDisable(true);
                		btnNgung.setDisable(true);
                		
                		btnNgung.setText("Ngừng KM");
                		btnSuaLuu.setText("Sửa");
                		btnSuaLuu.getStyleClass().remove("btn-luu");
	        			btnSuaLuu.getStyleClass().add("btn-sua");
                		
                }
            }
			
        });
        
        vboxAll.getChildren().add(tableDSKM);
        
        return vboxAll;
    }

    public VBox taoPhanBenPhai() {
    	 	
        //Spacer
        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        Region spacer3 = new Region();
        HBox.setHgrow(spacer3, Priority.ALWAYS);
        Region spacer4 = new Region();
        HBox.setHgrow(spacer4, Priority.ALWAYS);
        Region spacer5 = new Region();
        HBox.setHgrow(spacer5, Priority.ALWAYS);
        Region spacer6 = new Region();
        HBox.setHgrow(spacer6, Priority.ALWAYS);
        Region spacer8 = new Region();
        HBox.setHgrow(spacer8, Priority.ALWAYS);
        Region spacer9 = new Region();
        HBox.setHgrow(spacer9, Priority.ALWAYS);
        Region spacer7 = new Region();
        HBox.setHgrow(spacer7, Priority.ALWAYS);

        // Tiêu đề - 1
        Label lblTieuDeKhuyenMai = createModernSectionTitle("Thông Tin Khuyến Mãi", "#667eea");
        //lblTieuDeKhuyenMai.setGraphic(createSvgIcon(24,24, "#667eea", "M3.75 6.75h16.5M3.75 12H12m-8.25 5.25h16.5"));
        

        //Ma khuyen mai
        Label lblMaKhuyenMai = new Label("Mã khuyến mãi:");
        lblMaKhuyenMai.getStyleClass().add("fontTieuDeNho");
        txtMaKhuyenMai = createInputField(new TextField(), true);
        txtMaKhuyenMai.setPrefWidth(250);
        HBox hbox1 = new HBox(lblMaKhuyenMai, spacer1, txtMaKhuyenMai );
        hbox1.setPadding(new Insets(5));
        hbox1.setPadding(new Insets(0, 30, 0, 0));


        //Ten khuyen mai
        Label lblTenKhuyenMai = new Label("Tên khuyến mãi:");
        lblTenKhuyenMai.getStyleClass().add("fontTieuDeNho");
        txtTenKhuyenMai = createInputField(new TextField(), false);
        txtTenKhuyenMai.setPrefWidth(250);
        HBox hbox2 = new HBox(lblTenKhuyenMai, spacer2, txtTenKhuyenMai );
        hbox2.setPadding(new Insets(5));
        hbox2.setPadding(new Insets(0, 30, 0, 0));

        //Giảm giá phần trăm
        Label lblGiamGiaPhanTram = new Label("Giảm giá phần trăm:");
        lblGiamGiaPhanTram.getStyleClass().add("fontTieuDeNho");
        radioCo = new RadioButton("Có");
        radioKhong = new RadioButton("Không");
        radioGroup1 = new ToggleGroup();
        radioCo.setToggleGroup(radioGroup1);
        radioKhong.setToggleGroup(radioGroup1);
        radioCo.getStyleClass().add("radio-button");
        radioKhong.getStyleClass().add("radio-button");
        radioCo.setSelected(true);

        HBox hboxRadio1 = new HBox(5);
        hboxRadio1.setPrefWidth(250);
        Region spaceRadio1 = new Region();
        spaceRadio1.setPrefWidth(30);
        hboxRadio1.getChildren().addAll(radioCo, spaceRadio1,radioKhong);

        HBox hbox3 = new HBox(5);
        hbox3.getChildren().addAll(lblGiamGiaPhanTram, spacer3, hboxRadio1);

        //Giá trị giảm
        Label lblGiaTriGiam = new Label("Giá trị giảm:");
        lblGiaTriGiam.getStyleClass().add("fontTieuDeNho");
        txtGiaTriGiam = createInputField(new TextField(), false);
        txtGiaTriGiam.setPrefWidth(250);
        Label lblDonVi = new Label("VND");
        lblDonVi.setPrefWidth(30);
        lblDonVi.setPrefHeight(35);
        lblDonVi.setAlignment(Pos.BOTTOM_RIGHT);
        if(radioCo.isSelected()) {
            lblDonVi.setText("%");
        }
        lblDonVi.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 10px; -fx-font-weight: bold ;");

        HBox hbox4 = new HBox(lblGiaTriGiam, spacer4, txtGiaTriGiam , lblDonVi);


        //Giá trị tối đa
        Label lblGiaTriToiDa = new Label("Giá trị tối đa:");
        lblGiaTriToiDa.getStyleClass().add("fontTieuDeNho");
        txtGiaTriToiDa = createInputField(new TextField(), false);;
        txtGiaTriToiDa.setPrefWidth(250);
        Label lblDonViVND1 = new Label("VND");
        lblDonViVND1.setPrefWidth(30);
        lblDonViVND1.setPrefHeight(35);
        lblDonViVND1.setAlignment(Pos.BOTTOM_RIGHT);
        lblDonViVND1.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 10px; -fx-font-weight: bold;");

        HBox hbox5 = new HBox(lblGiaTriToiDa, spacer5, txtGiaTriToiDa, lblDonViVND1);

        if(!radioCo.isSelected()) {
            hbox5.setVisible(false);
            hbox5.setManaged(false);
        }

        // cai dat group 1
        radioGroup1.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal == radioCo) {
            		lblDonVi.setText("%");
                fadeIn(hbox5);
            } else {
                lblDonVi.setText("VND");
                fadeOut(hbox5);
            }
        });


        //Loại áp dụng
        Label lblLoaiApDung = new Label("Loại áp dụng:");
        lblLoaiApDung.getStyleClass().add("fontTieuDeNho");
        radioHoaDon = new RadioButton("Hóa đơn");
        radioMonAn = new RadioButton("Món ăn");
        radioGroup2 = new ToggleGroup();
        radioHoaDon.setToggleGroup(radioGroup2);
        radioMonAn.setToggleGroup(radioGroup2);
        radioHoaDon.getStyleClass().add("radio-button");
        radioMonAn.getStyleClass().add("radio-button");

        HBox hboxRadio2 = new HBox(5);
        hboxRadio2.setPrefWidth(250);
        Region spaceRadio2 = new Region();
        spaceRadio2.setPrefWidth(30);
        hboxRadio2.getChildren().addAll(radioHoaDon, spaceRadio2,radioMonAn);

        HBox hbox6 = new HBox(5);
        hbox6.getChildren().addAll(lblLoaiApDung, spacer6, hboxRadio2);

        //Dieu kiện áp dụng
        Label lblDieuKienApDung = new Label("Điều kiện áp dụng:");
        lblDieuKienApDung.getStyleClass().add("fontTieuDeNho");
        txtDieuKienApDung = createInputField(new TextField(), false);;
        txtDieuKienApDung.setPrefWidth(250);
        Label lblDonViVND2 = new Label("VND");
        lblDonViVND2.setPrefWidth(30);
        lblDonViVND2.setPrefHeight(35);
        lblDonViVND2.setAlignment(Pos.BOTTOM_RIGHT);
        lblDonViVND2.setStyle("-fx-text-fill: black; -fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 10px; -fx-font-weight: bold;");

        SVGPath iconLonHonBang = createSvgIcon(2, 24, "gray", "m4.435 1.704l17.3 6.796l-17.3 6.796l-.731-1.861L16.265 8.5L3.704 3.565l.73-1.861ZM3 19h18v2H3v-2Z");
        iconLonHonBang.setStyle("-fx-stroke: gray; -fx-fill: gray;");
        StackPane stDieuKien = new StackPane(iconLonHonBang, txtDieuKienApDung);
        stDieuKien.setAlignment(Pos.CENTER_LEFT);
        StackPane.setMargin(txtDieuKienApDung, new Insets(0, 0, 0, 20));
        stDieuKien.setStyle("""
        			-fx-background-color: white;
        			-fx-border-color: white;
        		""");

        HBox hbox7 = new HBox(lblDieuKienApDung, spacer7, stDieuKien, lblDonViVND2);

        radioHoaDon.setSelected(true);
        if(radioMonAn.isSelected()) {
            hbox7.setVisible(false);
            hbox7.setManaged(false);
        }

        // Vbox phan 1
        VBox vbox1 = new VBox(10);
        vbox1.getChildren().addAll(lblTieuDeKhuyenMai,hbox1, hbox2, hbox3, hbox4, hbox5, hbox6, hbox7);
        vbox1.setPadding(new Insets(0, 20, 0, 20));

        // Tiêu đề - 2
        Label lblTieuDeNgayApDung = createModernSectionTitle("Ngày Áp Dụng", "#667eea");
        //lblTieuDeNgayApDung.setGraphic(createSvgIcon(24, 24, "#667eea", "M6.75 3v2.25M17.25 3v2.25M3 18.75V7.5a2.25 2.25 0 0 1 2.25-2.25h13.5A2.25 2.25 0 0 1 21 7.5v11.25m-18 0A2.25 2.25 0 0 0 5.25 21h13.5A2.25 2.25 0 0 0 21 18.75m-18 0v-7.5A2.25 2.25 0 0 1 5.25 9h13.5A2.25 2.25 0 0 1 21 11.25v7.5"));
        

        //Ngay bat dau
        Label lblNgayBatDau = new Label("Ngày bắt đầu:");
        lblNgayBatDau.getStyleClass().add("fontTieuDeNho");
        dateBatDau = new DatePicker();
        dateKetThuc = new DatePicker();
        
        dateKetThuc = new DatePicker();
        dateBatDau.setPrefWidth(250);
        dateBatDau.getStyleClass().add("date-picker");
        
        dateBatDau.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && dateKetThuc.getValue() != null) {
                loadLaiDSMonAn(newVal, dateKetThuc.getValue());
            }
        });

        HBox hbox8 = new HBox(lblNgayBatDau, spacer8, dateBatDau);
        hbox8.setPadding(new Insets(0, 30, 0, 0));

        //Ngay ket thuc
        Label lblNgayKetThuc = new Label("Ngày kết thúc:");
        lblNgayKetThuc.getStyleClass().add("fontTieuDeNho");
        dateKetThuc.setPrefWidth(250);
        dateKetThuc.getStyleClass().add("date-picker");
        
        dateKetThuc.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && dateBatDau.getValue() != null) {
                loadLaiDSMonAn(dateBatDau.getValue(), newVal);
            }
        });

        HBox hbox9 = new HBox(lblNgayKetThuc, spacer9, dateKetThuc);
        hbox9.setPadding(new Insets(0, 30, 0, 0));

        // Khu vuc khuyen mai mon an

        VBox vboxKhuyenMaiMonAn = new VBox(5);

        //Button chọn món khuyến mãi

        btnChonMonAn = new Button("Chọn món");
        btnChonMonAn.setGraphic(createSvgIcon(24, 24, "white", "M12 2.25c-5.385 0-9.75 4.365-9.75 9.75s4.365 9.75 9.75 9.75 9.75-4.365 9.75-9.75S17.385 2.25 12 2.25ZM12.75 9a.75.75 0 0 0-1.5 0v2.25H9a.75.75 0 0 0 0 1.5h2.25V15a.75.75 0 0 0 1.5 0v-2.25H15a.75.75 0 0 0 0-1.5h-2.25V9Z"));
        btnChonMonAn.setMinHeight(30);
        btnChonMonAn.setMinWidth(100);
        btnChonMonAn.getStyleClass().add("button-chonMon");

        // BUTTON CHON MON
        // TẠO MODAL HIỆN RA
        btnChonMonAn.setOnAction(e -> {
        		
			String giaTriGiamString = txtGiaTriGiam.getText();
			    		
	    		if(giaTriGiamString.isEmpty()) {
		    			showAlert(AlertType.WARNING, "Giá trị giảm rỗng", "Vui lòng nhập giá trị giảm trước khi thêm khuyến mãi !");
		    			focus(txtGiaTriGiam);
		    			return;
	    		}
	    		
	    		double giaTriGiam = 0.0;
	    		
	    		try {
	    			giaTriGiam = Double.parseDouble(giaTriGiamString);
	    		}catch (Exception er) {
	    			showAlert(AlertType.WARNING, "Giá trị giảm sai", "Vui lòng nhập giá trị giảm là số");
	    			focus(txtGiaTriGiam);
	    			return;
	    		}	
	    	
    			if(giaTriGiam < 0) {
    				showAlert(AlertType.WARNING, "Giá trị giảm không đúng", "Vui lòng nhập giá trị giảm >= 0 !");
        			focus(txtGiaTriGiam);
        			return;
    			}
        	
        		LocalDate ngayBD = dateBatDau.getValue();
    		
	    		if(ngayBD == null) {
	    			showAlert(AlertType.WARNING, "Ngày bắt đầu rỗng", "Vui lòng chọn ngày bắt đầu!");
	    			dateBatDau.requestFocus();
	    			return;
	    		}
	    		
	    		LocalDate ngayKT = dateKetThuc.getValue();
	    		
	    		if(ngayKT == null) {
	    			showAlert(AlertType.WARNING, "Ngày kết thúc rỗng", "Vui lòng chọn ngày kết thúc!");
	    			dateKetThuc.requestFocus();
	    			return;
	    		}
	    		
	    		if(ngayBD.isBefore(LocalDate.now())) {
	    			showAlert(AlertType.WARNING, "Ngày bắt không hợp lệ", "Ngày bắt đầu không được trước ngày hiện tại!");
	    			dateBatDau.requestFocus();
	    			return;
	    		}
	    		
	    		if (ngayKT.isBefore(ngayBD)) {
	    		    showAlert(AlertType.WARNING, "Ngày kết thúc không hợp lệ", "Ngày kết thúc phải sau hoặc bằng ngày bắt đầu!");
	    		    dateKetThuc.requestFocus();
	    		    return;
	    		}
	        		
        		if(dsTTMonAn.isEmpty()) {
        			loadLaiDSMonAn(ngayBD, ngayKT);
        		}
        	
            Stage stageChinh = (Stage) btnChonMonAn.getScene().getWindow(); 
            Parent rootChinh = stageChinh.getScene().getRoot();

            // Hiệu ứng mờ
            BoxBlur blur = new BoxBlur(5, 5, 3);
            rootChinh.setEffect(blur);

            //Khoi tao modal
            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Chọn món ăn khuyến mãi");
            modal.initModality(Modality.WINDOW_MODAL);
            //Layout

            BorderPane layout = new BorderPane();
            layout.setStyle("-fx-background-color: white");

            //Tạo phần bên trái modal thêm món ăn
            VBox vboxPhanTrai = taoPhanBenTraiModal();
            layout.setCenter(vboxPhanTrai);

            // Tạo phần bên phải modal thêm món ăn
            VBox vboxPhanPhai = taoPhanBenPhaiModal(modal);
            layout.setRight(vboxPhanPhai);


            //Scene
            Scene scene = new Scene(layout,  900, 600);
            scene.getStylesheets().add(getClass().getResource("/css/qlkm.css").toExternalForm());
            modal.setScene(scene);
            modal.showAndWait();

            // Khi modal đóng, xóa blur
            rootChinh.setEffect(null);
        });


        // Bảng món ăn
        tableDSMonKMTheoMa = new TableView<>(dsMonChon);

        TableColumn<String, String> colMaMon = new TableColumn<>("Mã");
        colMaMon.setCellValueFactory(cellData ->{
        		return new SimpleStringProperty(cellData.getValue().split("_")[0]);
        });
        colMaMon.setPrefWidth(80);

        TableColumn<String, String> colTenMon = new TableColumn<>("Tên món");
        colTenMon.setCellValueFactory(cellData ->{
    			return new SimpleStringProperty(cellData.getValue().split("_")[1]);
        });
        colTenMon.setPrefWidth(170);

        TableColumn<String, String> colGiaTien = new TableColumn<>("Giá tiền");
        colGiaTien.setCellValueFactory(cellData ->{
    			return new SimpleStringProperty(cellData.getValue().split("_")[3]);
        });
        colGiaTien.setPrefWidth(120);

        TableColumn<String, String> colGiaSauKM = new TableColumn<>("Giá KM");
        colGiaSauKM.setCellValueFactory(cellData ->{
    			return new SimpleStringProperty(cellData.getValue().split("_")[6]);
        });
        colGiaSauKM.setMinWidth(120);
        
        ///Col xoa
        
        colXoa.setCellFactory(tc -> new TableCell<String, Void>() {

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    return;
                }

                Button btnTBXoa = new Button();
                btnTBXoa.setGraphic(createSvgIcon(15, 24, "red", "m14.74 9-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 0 1-2.244 2.077H8.084a2.25 2.25 0 0 1-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 0 0-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 0 1 3.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 0 0-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 0 0-7.5 0"));
                btnTBXoa.setStyle("-fx-background-color: transparent;");
                btnTBXoa.setStyle("-fx-cursor: hand; -fx-background-color: transparent; -fx-padding: 0");
                btnTBXoa.setMaxHeight(15);
                btnTBXoa.setMaxWidth(15);
                
                btnTBXoa.setOnAction(e -> {
                    // lấy đúng item của dòng hiện tại
                    String monAn = getTableView().getItems().get(getIndex());
                    String[] monAnSplit = monAn.split("_");

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Xác nhận");
                    alert.setHeaderText("Bạn có chắc muốn xóa món này không?");
                    alert.setContentText(
                        "Mã: " + monAnSplit[0] + "\n" +
                        "Tên: " + monAnSplit[1] + "\n" +
                        "Giá gốc: " + monAnSplit[3] + "\n" +
                        "Giá KM: " + monAnSplit[6]
                    );

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        // thực hiện xóa
                        dsTTMonAn.add(
                            monAnSplit[0] + "_" +
                            monAnSplit[1] + "_" +
                            monAnSplit[2] + "_" +
                            monAnSplit[3] + "_" +
                            monAnSplit[4] + "_" +
                            monAnSplit[5] + "_" +
                            monAnSplit[3] + "_" +
                            monAnSplit[7]
                        );
                        getTableView().getItems().remove(getIndex());
                        dsMonChon.remove(monAn);
                    }
                });


                setGraphic(btnTBXoa);
                setAlignment(Pos.CENTER);
            }
        });

        colXoa.setVisible(false);
        colXoa.setPrefWidth(50);
        
        // Thêm cột vào bảng
        tableDSMonKMTheoMa.getColumns().addAll(colMaMon, colTenMon, colGiaTien, colGiaSauKM, colXoa);


        tableDSMonKMTheoMa.setPrefHeight(100);
        tableDSMonKMTheoMa.getStyleClass().add("table-view");
        tableDSMonKMTheoMa.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        vboxKhuyenMaiMonAn.getChildren().addAll(btnChonMonAn, tableDSMonKMTheoMa);
        //
        if(radioHoaDon.isSelected()) {
            vboxKhuyenMaiMonAn.setVisible(false);
            vboxKhuyenMaiMonAn.setManaged(false);
            
        }

        // cai dat group 2
        radioGroup2.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal == radioHoaDon) {
                fadeIn(hbox7);
                fadeOut(vboxKhuyenMaiMonAn);
                radioCo.setDisable(false);
            } else {
                fadeOut(hbox7);
                fadeIn(vboxKhuyenMaiMonAn);
                radioKhong.setSelected(true);
                radioCo.setDisable(true);
            }
        });


        //Vbox phan 2
        VBox vbox2 = new VBox(10);
        vbox2.getChildren().addAll(lblTieuDeNgayApDung, hbox8, hbox9, vboxKhuyenMaiMonAn);
        vbox2.setPadding(new Insets(0, 20, 0, 20));

        //Phần nút
        btnSuaLuu = new Button("Sửa");
        btnNgung = new Button("Ngưng KM");
        
        btnNgung.setDisable(true);
        btnSuaLuu.setDisable(true);

        btnSuaLuu.getStyleClass().add("btn-sua");
        btnNgung.getStyleClass().add("btn-ngung");
        
        btnSuaLuu.setTooltip(new Tooltip("Nhấn F6 để dùng nhanh"));
        btnNgung.setTooltip(new Tooltip("Nhấn F9 để dùng nhanh"));
        
        btnNgung.setOnAction(e -> {
        		if(flagNgungXoa) {
        			hanhDongNgungKM();
        		}else {
        			hanhDongXoaKM();
        		}
        		
        });
        
        btnSuaLuu.setOnAction(e -> {
        		if(flagSuaLuu) {
        			if(hanhDongSua()) {
        				btnSuaLuu.setText("Lưu");
        				btnSuaLuu.getStyleClass().remove("btn-sua");
            			btnSuaLuu.getStyleClass().add("btn-luu");
            			colXoa.setVisible(true);
            			flagSuaLuu = false;
            			btnChonMonAn.setDisable(false);
            			
            			moKhoaChinhSua(true);
        			}
        		}else {
        			if(hanhDongLuu()) {
        				btnSuaLuu.setText("Sửa");
        				btnSuaLuu.getStyleClass().remove("btn-luu");
            			btnSuaLuu.getStyleClass().add("btn-sua");
            			flagSuaLuu = true;
            			colXoa.setVisible(false);
            			clear();
            			
            			moKhoaChinhSua(false);
        			}
        		}
        });

        HBox hboxButton = new HBox(10);
        Region spaceButton = new Region();

        hboxButton.getChildren().addAll(btnSuaLuu, btnNgung);
        hboxButton.setAlignment(Pos.CENTER_LEFT);
        hboxButton.setPadding(new Insets(0, 25, 0, 22));

        /////
        VBox vboxAll = new VBox(30);
        vboxAll.getChildren().addAll(vbox1, vbox2, hboxButton);
        vboxAll.setPadding(new Insets(30, 0, 30, 0));

        //vboxAll.setStyle("-fx-background-color: black");
        moKhoaChinhSua(false);
        vboxAll.setPrefWidth(500);
        return vboxAll;
    }

    public VBox taoPhanBenTraiModal() {
        //VBox benTrai
        VBox vboxBenTrai = new VBox(8);
        vboxBenTrai.setPadding(new Insets(20,10,20,10));
        vboxBenTrai.setPrefWidth(550);

        // Biến bên trái
        Label lblTieuDeChonMon = new Label("Chọn món giảm giá");
        HBox hboxChuThich = new HBox(10);
        Label lblDanhSach = new Label("Danh sách món");
        HBox hboxChuaGiamGia = new HBox(2);
        ImageView iconChuaGiamGia = new ImageView(IMG_CHU_NHAT_XANH);
        Label lblChuaGiamGia = new Label("Chưa giảm giá");
        HBox hboxDaGiamGia = new HBox(2);
        ImageView iconDaGiamGia = new ImageView(IMG_CHU_NHAT_VANG);
        Label lblDaGiamGia = new Label("Đã giảm giá");
        cboLocMon = new ComboBox<String>();
        cboLocMon.getItems().addAll("Tất cả","Đã giảm giá","Chưa giảm giá");
        
        cboLocMon.setOnAction(e -> {locMonAn();});

        //Tieu de
        lblTieuDeChonMon.setStyle("-fx-font-size: 30px; -fx-font-family: 'Tai Heritage Pro'; -fx-font-weight: bold");
        vboxBenTrai.getChildren().add(lblTieuDeChonMon);

        // Danh sách chú thích
        lblDanhSach.getStyleClass().add("fontTieuDeNho");
        lblChuaGiamGia.getStyleClass().add("fontTieuDeNho");
        lblDaGiamGia.getStyleClass().add("fontTieuDeNho");
        iconChuaGiamGia.setFitHeight(10);
        iconChuaGiamGia.setFitWidth(15);
        iconDaGiamGia.setFitHeight(10);
        iconDaGiamGia.setFitWidth(15);
        hboxChuaGiamGia.getChildren().addAll(iconChuaGiamGia, lblChuaGiamGia);
        hboxChuaGiamGia.setAlignment(Pos.CENTER_LEFT);
        hboxDaGiamGia.getChildren().addAll(iconDaGiamGia, lblDaGiamGia);
        hboxDaGiamGia.setAlignment(Pos.CENTER_LEFT);
        hboxChuThich.getChildren().addAll(lblDanhSach, hboxChuaGiamGia, hboxDaGiamGia, cboLocMon);
        hboxChuThich.setAlignment(Pos.CENTER_LEFT);
        vboxBenTrai.getChildren().add(hboxChuThich);
        cboLocMon.getStyleClass().add("combo-box");
        cboLocMon.getSelectionModel().selectFirst();

        //Danh sách món ăn giảm giá
        
        
        ListView<String> listMonAn = taoDanhSachMonBenTrai();
        listMonAn.setFixedCellSize(-1);

        //
        vboxBenTrai.getChildren().add(listMonAn);

        return vboxBenTrai;
    }

    public VBox taoPhanBenPhaiModal(Stage modal) {
        //Vbox bên phải
        VBox vboxBenPhai = new VBox(20);
        vboxBenPhai.setPrefWidth(350);
        vboxBenPhai.setPadding(new Insets(50, 10, 50, 10));

        //Biến bên phải
        HBox hboxTieuDe = new HBox();
        Label lblMonDaChon = new Label("Món đã chọn");
        ListView<String> listMonChon = taoDanhSachMonBenPhai();
        HBox hboxButton = new HBox();
        Region spacerButton  = new Region();
        Button btnQuayVe = new Button("Quay về");
        Button btnXong = new Button("Xong");

        //Tạo giao diện
        lblMonDaChon.setStyle("-fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: black");
        hboxTieuDe.getChildren().add(lblMonDaChon);
        hboxTieuDe.setAlignment(Pos.CENTER);
        btnQuayVe.setPrefHeight(50);
        btnQuayVe.setPrefWidth(100);
        btnXong.setPrefHeight(50);
        btnXong.setPrefWidth(100);
        btnQuayVe.setStyle("-fx-background-color: #082744; -fx-font-size: 18; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand");
        btnXong.setStyle("-fx-background-color: green; -fx-font-size: 18; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand");
 
        hboxButton.setHgrow(spacerButton, Priority.ALWAYS);
        hboxButton.getChildren().addAll(btnQuayVe, spacerButton, btnXong);
        hboxButton.setPadding(new Insets(0, 20, 0, 20));

        //SỰ kiện button
        btnQuayVe.setOnAction(e -> {
            modal.close();
            quayVe();
        });

        btnXong.setOnAction(e -> {
            modal.close();
            themMonChon();
        });


        //
        vboxBenPhai.getChildren().addAll(hboxTieuDe, listMonChon, hboxButton);
        vboxBenPhai.setStyle("-fx-border-width: 0 0 0 1; -fx-border-color: #D9D9D9");

        return vboxBenPhai;
    }

    public ListView<String> taoDanhSachMonBenPhai() {
        //Vbox danh sách món
        ListView<String> listMonAn = new ListView<>(dsMonTemp);
        listMonAn.setStyle("-fx-background-color: transparent;");
        listMonAn.setFixedCellSize(80);

        listMonAn.setCellFactory(list -> new ListCell<String>() {
            @Override
            protected void updateItem(String mon, boolean empty) {
                super.updateItem(mon, empty);
                if (empty || mon == null) {
                    setGraphic(null);
                } else {
                    setGraphic(taoMonBenPhai(mon));
                }
            }
        });

        // Trong hàm taoDanhSachMonBenTrai(), thay phần chặn event:
        listMonAn.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            // Kiểm tra target hoặc parent có phải Button không (reuse cho text bên trong Button)
            Node target = (Node) e.getTarget();
            if (!(target instanceof Button || (target.getParent() != null && target.getParent() instanceof Button))) {
                e.consume();  // Chỉ chặn nếu KHÔNG phải Button (hoặc text của Button)
                listMonAn.getSelectionModel().clearSelection();
            }
            // Nếu click Button/text của Button, event tự do → setOnAction hoạt động
        });

        return listMonAn;
    }

    public HBox taoMonBenPhai(String monAn) {
        //HBOx mon la
        HBox hboxMonAn = new HBox(4);

        //Khai báo biến
        HBox hboxBenTrai = new HBox(3);
        ImageView imgMonAn = new ImageView(IMG_MON_AN); 
        VBox vboxThongTinMon = new VBox(5);
        Label lblTenMon = new Label("Món nào đó");
        HBox hboxGia = new HBox(3);
        Label lblSau = new Label();
        VBox vboxBenPhai = new VBox(3);
        Button btnXoa = new Button("Xóa");

        // Cai dat event
        btnXoa.setOnAction(e ->{
            dsMonTemp.remove(monAn);
            
            
            String[] monAnTach = monAn.split("_");
            
            String monAn2 = monAnTach[0] + "_" +
            		monAnTach[1] + "_" +
            		monAnTach[2] + "_" +
            		monAnTach[3] + "_" +
            		monAnTach[4] + "_" +
            		monAnTach[5] + "_" +
            		monAnTach[3] + "_" +
            		monAnTach[7] ;
            
            dsTTMonAn.add(monAn2);
            
            if(!dsLocMon.isEmpty()) {
    				dsLocMon.add(monAn2);
            }
        });
        

        //Set giá trị
        DecimalFormat fomat = new DecimalFormat("#,### VND");
        
        lblTenMon.setText(monAn.split("_")[1]);
        
        String maKM = txtMaKhuyenMai.getText();
        
        lblSau.setText(fomat.format(Double.parseDouble(monAn.split("_")[6])));

        //Tạo món ăn
        //Trái
        hboxBenTrai.setPrefWidth(220);
        hboxBenTrai.setPrefHeight(50);
        imgMonAn.setFitHeight(40);
        imgMonAn.setFitWidth(40);
        imgMonAn.getStyleClass().add("img-MonAn");
        lblTenMon.setStyle("-fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 12px; -fx-font-weight: bold");
        lblTenMon.setAlignment(Pos.CENTER_LEFT);
        lblSau.setStyle("-fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: red");
        vboxThongTinMon.getChildren().addAll(lblTenMon, lblSau);
        vboxThongTinMon.setAlignment(Pos.CENTER_LEFT);
        vboxThongTinMon.setPadding(new Insets(0,0,0,5));
        hboxBenTrai.getStyleClass().add("monAnTrai");
        hboxBenTrai.getChildren().addAll(imgMonAn, vboxThongTinMon);
        hboxBenTrai.setAlignment(Pos.CENTER_LEFT);
        hboxBenTrai.setPadding(new Insets(0,0,0,5));

        //Phải
        vboxBenPhai.setPrefWidth(80);
        vboxBenPhai.setPrefHeight(50);
        btnXoa.setStyle("-fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 13px; -fx-text-fill: black; -fx-background-color: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand");
        vboxBenPhai.getChildren().addAll(btnXoa);
        vboxBenPhai.setStyle("-fx-background-color: #082744; -fx-background-radius: 10");
        vboxBenPhai.setAlignment(Pos.CENTER);

        hboxMonAn.getChildren().addAll(hboxBenTrai, vboxBenPhai);
        hboxMonAn.getStyleClass().add("monAn");
        //
        return hboxMonAn;
    }

    public HBox taoMonBenTrai(String monAn) {
        //HBox mon an
        HBox hboxMonAn = new HBox(10);

        //Khai báo biến
        HBox hboxBenTrai = new HBox(3);
        ImageView imgMonAn = new ImageView(IMG_MON_AN); 
        VBox vboxThongTinMon = new VBox(5);
        Label lblTenMon = new Label();
        
        Label lblBanDau = new Label();
        Label lblSau = new Label();
        
        VBox vboxBenPhai = new VBox(3);
        Label lblGiamHayChua = new Label();
        Button btnThemMon = new Button("Thêm");
        Label lblMaKhuyenMai = new Label();

        //Set gia tri
        DecimalFormat fomat = new DecimalFormat("#,### VND");
        lblTenMon.setText(monAn.split("_")[1]);
        
        lblSau.setText(fomat.format(Double.parseDouble(monAn.split("_")[6])));
        


        //Trái
        hboxBenTrai.setPrefWidth(350);
        hboxBenTrai.setPrefHeight(100);
        imgMonAn.setFitHeight(90);
        imgMonAn.setFitWidth(90);
        imgMonAn.getStyleClass().add("img-MonAn");
        lblTenMon.setStyle("-fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 20px; -fx-font-weight: bold");
        lblTenMon.setAlignment(Pos.CENTER_LEFT);
        lblBanDau.setStyle("-fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 10; -fx-strikethrough: true;");
        lblSau.setStyle("-fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: red");
        vboxThongTinMon.getChildren().addAll(lblTenMon, lblSau);
        vboxThongTinMon.setAlignment(Pos.CENTER_LEFT);
        vboxThongTinMon.setPadding(new Insets(0,0,0,5));
        
        hboxBenTrai.getStyleClass().add("monAnTrai");
        hboxBenTrai.getChildren().addAll(imgMonAn, vboxThongTinMon);
        hboxBenTrai.setAlignment(Pos.CENTER_LEFT);
        hboxBenTrai.setPadding(new Insets(0,0,0,5));

        //Cài đặt event
        btnThemMon.setOnAction(e ->{
            dsTTMonAn.remove(monAn);
            if(!dsLocMon.isEmpty()) {
        			dsLocMon.remove(monAn);
            }
            String[] monAnTach = monAn.split("_");
            
            double giaSauKM = control.tinhGiaSauKM(Double.parseDouble(monAnTach[3]), Double.parseDouble(txtGiaTriGiam.getText()));
            String monAn2 = monAnTach[0] + "_" +
            		monAnTach[1] + "_" +
            		monAnTach[2] + "_" +
            		monAnTach[3] + "_" +
            		monAnTach[4] + "_" +
            		monAnTach[5] + "_" +
            		giaSauKM + "_" +
            		monAnTach[7];
            dsMonTemp.add(monAn2);
        });

        //Phải
        boolean giamGia = monAn.split("_")[5].equals("1") ? true : false;
        
        vboxBenPhai.setPrefWidth(150);
        vboxBenPhai.setPrefHeight(100);
        lblGiamHayChua.setStyle("-fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold");
        btnThemMon.setStyle("-fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 20px; -fx-text-fill: black; -fx-background-color: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand");
        lblMaKhuyenMai.setStyle("-fx-font-family: 'Tai Heritage Pro'; -fx-font-size: 12px; -fx-text-fill: black");
        
        
        if(giamGia) {
	        	vboxBenPhai.setStyle("-fx-background-color: #D5B009; -fx-background-radius: 10");
	    		vboxBenPhai.getChildren().addAll(lblGiamHayChua);
	    		lblGiamHayChua.setText("Mã " + monAn.split("_")[7]);
        }
        else {
        		vboxBenPhai.setStyle("-fx-background-color: #082744; -fx-background-radius: 10");
        		lblGiamHayChua.setText("Chưa giảm giá");
        		vboxBenPhai.getChildren().addAll(lblGiamHayChua, btnThemMon);
        }
        	
        vboxBenPhai.setAlignment(Pos.CENTER);

        //
        hboxMonAn.getChildren().addAll(hboxBenTrai, vboxBenPhai);
        hboxMonAn.getStyleClass().add("monAn");

        return hboxMonAn;
    }

    public ListView<String> taoDanhSachMonBenTrai() {
        listMonAn = new ListView<>(dsTTMonAn);
        
        listMonAn.setStyle("-fx-background-color: transparent;");
        listMonAn.setFixedCellSize(110);

        listMonAn.setCellFactory(list -> new ListCell<String>() {
            @Override
            protected void updateItem(String mon, boolean empty) {
                super.updateItem(mon, empty);
                
                if (empty || mon == null) {
                    setGraphic(null);
                } else {
                    setGraphic(taoMonBenTrai(mon));
                }
            }
        });

        // Trong hàm taoDanhSachMonBenTrai(), thay phần chặn event:
        listMonAn.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            // Kiểm tra target hoặc parent có phải Button không (reuse cho text bên trong Button)
            Node target = (Node) e.getTarget();
            if (!(target instanceof Button || (target.getParent() != null && target.getParent() instanceof Button))) {
                e.consume();  // Chỉ chặn nếu KHÔNG phải Button (or text của Button)
                listMonAn.getSelectionModel().clearSelection();
            }
            // Nếu click Button/text của Button, event tự do → setOnAction hoạt động
        });


        return listMonAn;
    }


    // ANIMATION
    // Fade in: Hiện dần từ mờ đến rõ
    private void fadeIn(Node node) {
        if (node == null) return;

        node.setVisible(true);
        node.setManaged(true);
        node.setOpacity(0.0);  

        FadeTransition fade = new FadeTransition(Duration.millis(300), node);
        fade.setFromValue(0.0);  
        fade.setToValue(1.0);    
        fade.play();
    }

    // Fade out: Ẩn dần từ rõ đến mờ
    private void fadeOut(Node node) {
        if (node == null) return;

        FadeTransition fade = new FadeTransition(Duration.millis(300), node);
        fade.setFromValue(1.0);  // Từ rõ
        fade.setToValue(0.0);    // Đến mờ
        fade.setOnFinished(e -> {
            node.setVisible(false);
            node.setManaged(false);
            node.setOpacity(1.0);  // Reset để lần sau OK
        });
        fade.play();
    }
    
    private Label createModernSectionTitle(String title, String color) {
        Label label = new Label(title);
        label.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        label.setStyle(
                "-fx-text-fill: " + color + ";" +
                        "-fx-padding: 2 0 2 10;" +
                        "-fx-border-width: 0 0 0 4;" +
                        "-fx-border-color: " + color + ";" +
                        "-fx-font-weight: bold"
        );
        return label;
    }

    
    private SVGPath createSvgIcon(double size, double viewBox, String mau, String pathData) {
		SVGPath svg = new SVGPath();
		svg.setContent(pathData);
		svg.setScaleX(size / viewBox);
		svg.setScaleY(size / viewBox);
		svg.setStyle("-fx-stroke: " + mau + "; -fx-fill: transparent;");
		return svg;
	}
    
    private TextField createInputField(TextField textField, boolean isReadOnly) {

        textField.setEditable(!isReadOnly);

        if (isReadOnly) {
            textField.setStyle(
                    "-fx-background-color: #ECF0F1; " +
                            "-fx-border-color: #bdc3c7; " +
                            "-fx-border-radius: 6; " +
                            "-fx-background-radius: 6; " +
                            "-fx-padding: 5 10 5 10; " +
                            "-fx-font-size: 15px; " +
                            "-fx-text-fill: #7f8c8d;"
            );
        } else {
            textField.setStyle(
                    "-fx-background-color: white; " +
                            "-fx-border-color: #3498db; " +
                            "-fx-border-width: 1.5; " +
                            "-fx-border-radius: 6; " +
                            "-fx-background-radius: 6; " +
                            "-fx-padding: 5 10 5 10; " +
                            "-fx-font-size: 15px;"
            );

        }

       
        return textField;
    }
    
    private void moKhoaChinhSua(boolean bool) {
    		if(bool) {
    			txtTenKhuyenMai.setEditable(true);
    			radioCo.setDisable(false);
    			radioKhong.setDisable(false);
    			txtGiaTriGiam.setEditable(true);
    			txtGiaTriToiDa.setEditable(true);
    			radioMonAn.setDisable(false);
    			radioHoaDon.setDisable(false);
    			txtDieuKienApDung.setEditable(true);
    			dateBatDau.removeEventFilter(MouseEvent.ANY, blockMouse);
    			dateBatDau.getEditor().removeEventFilter(KeyEvent.ANY, blockKey);

    			dateKetThuc.removeEventFilter(MouseEvent.ANY, blockMouse);
    			dateKetThuc.getEditor().removeEventFilter(KeyEvent.ANY, blockKey);
    			
    			moKhoaChinhSuaTextField(txtTenKhuyenMai, bool);
    			moKhoaChinhSuaTextField(txtGiaTriGiam, bool);
    			moKhoaChinhSuaTextField(txtGiaTriToiDa, bool);
    			moKhoaChinhSuaTextField(txtDieuKienApDung, bool);
    			
    		}else {
    			txtTenKhuyenMai.setEditable(false);
    			radioCo.setDisable(true);
    			radioKhong.setDisable(true);
    			txtGiaTriGiam.setEditable(false);
    			txtGiaTriToiDa.setEditable(false);
    			txtDieuKienApDung.setEditable(false);
    			radioMonAn.setDisable(true);
    			radioHoaDon.setDisable(true);
    			dateBatDau.addEventFilter(MouseEvent.ANY, blockMouse);
    			dateBatDau.getEditor().addEventFilter(KeyEvent.ANY, blockKey);

    			dateKetThuc.addEventFilter(MouseEvent.ANY, blockMouse);
    			dateKetThuc.getEditor().addEventFilter(KeyEvent.ANY, blockKey);
    			
    			moKhoaChinhSuaTextField(txtTenKhuyenMai, bool);
    			moKhoaChinhSuaTextField(txtGiaTriGiam, bool);
    			moKhoaChinhSuaTextField(txtGiaTriToiDa, bool);
    			moKhoaChinhSuaTextField(txtDieuKienApDung, bool);
    		}
    }
    
    private void moKhoaChinhSuaTextField(TextField textField, boolean bool) {
    	if (!bool) {
            textField.setStyle(
                    "-fx-background-color: #ECF0F1; " +
                            "-fx-border-color: #bdc3c7; " +
                            "-fx-border-radius: 6; " +
                            "-fx-background-radius: 6; " +
                            "-fx-padding: 5 10 5 10; " +
                            "-fx-font-size: 15px; " +
                            "-fx-text-fill: #7f8c8d;"
            );
        } else {
            textField.setStyle(
                    "-fx-background-color: white; " +
                            "-fx-border-color: #3498db; " +
                            "-fx-border-width: 1.5; " +
                            "-fx-border-radius: 6; " +
                            "-fx-background-radius: 6; " +
                            "-fx-padding: 5 10 5 10; " +
                            "-fx-font-size: 15px;" +
                            "-fx-cursor: hand"
            );
        }
    }
    
    public void clear() {
    		txtMaKhuyenMai.setText("");
    		txtTenKhuyenMai.setText("");
    		txtGiaTriGiam.setText("");
    		txtGiaTriToiDa.setText("");
    		txtDieuKienApDung.setText("");
    		dateBatDau.setValue(null);
    		dateKetThuc.setValue(null);
    		radioCo.setSelected(true);
    		radioHoaDon.setSelected(true);
    		btnChonMonAn.setDisable(false);
    		dsTTMonAn.clear();
    }
    
    public boolean hanhDongThem() {
    		clear();
    		moKhoaChinhSua(true);
    		String maKMMoi = control.taoMaKhuyenMaiMoi();
    		
    		txtMaKhuyenMai.setText(maKMMoi);
    		
    		txtTenKhuyenMai.requestFocus();
    		btnNgung.setDisable(true);
    		btnSuaLuu.setDisable(false);
    		
    		btnSuaLuu.setText("Lưu");
    		btnSuaLuu.getStyleClass().remove("btn-sua");
    		btnSuaLuu.getStyleClass().add("btn-luu");
    		flagSuaLuu = false;
    		
    		dsMonChon.clear();
    		tableDSMonKMTheoMa.setItems(dsMonChon);
    		dsTTMonAn.clear();
    		
    		btnNgung.setDisable(true);
    		btnSuaLuu.setDisable(true);
    		
    		return true;
    }
    
    public void focus(TextField txt) {
    		txt.requestFocus();
    		txt.selectAll();
    }
    
    public void showAlert(Alert.AlertType type, String title, String message) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

    public boolean hanhDongLuu() {
    		String maKM = txtMaKhuyenMai.getText();
    		String tenKhuyenMai = txtTenKhuyenMai.getText();
    		
    		if(tenKhuyenMai.isEmpty()) {
    			showAlert(AlertType.WARNING, "Tên rỗng", "Vui lòng nhập tên khuyến mãi đầy đủ!");
    			focus(txtTenKhuyenMai);
    			return false;
    		}
    		
    		String giaTriGiamString = txtGiaTriGiam.getText();
    		
    		if(giaTriGiamString.isEmpty()) {
    			showAlert(AlertType.WARNING, "Giá trị giảm rỗng", "Vui lòng nhập giá trị giảm hợp lệ!");
    			focus(txtGiaTriGiam);
    			return false;
    		}
    		
    		double giaTriGiam = 0.0;
    		
    		try {
    			giaTriGiam = Double.parseDouble(giaTriGiamString);
    		}catch (Exception e) {
    			showAlert(AlertType.WARNING, "Giá trị giảm sai", "Vui lòng nhập giá trị giảm là số");
    			focus(txtGiaTriGiam);
    			return false;
		}
    		
    		String giaTriToiDaString = "";
    		double giaTriToiDa = 0.0;
    		
    		if(radioCo.isSelected()) {
    			if(giaTriGiam < 0 || giaTriGiam > 100.0) {
    				showAlert(AlertType.WARNING, "Giá trị giảm không đúng", "Vui lòng nhập giá trị giảm >= 0 và <= 100 !");
        			focus(txtGiaTriGiam);
        			return false;
    			}
    			
    			giaTriToiDaString = txtGiaTriToiDa.getText();
    			if(giaTriToiDaString.isEmpty()) {
    				showAlert(AlertType.WARNING, "Giá trị tối đa rỗng", "Vui lòng nhập giá trị tối đa !");
        			focus(txtGiaTriToiDa);
        			return false;
    			}
    			
    			try {
					giaTriToiDa = Double.parseDouble(giaTriToiDaString);
				} catch (Exception e) {
					showAlert(AlertType.WARNING, "Giá trị tối đa sai", "Vui lòng nhập giá trị tối đa là số !");
	        			focus(txtGiaTriToiDa);
	        			return false;
			}
    			
    			if(giaTriToiDa < 0) {
    				showAlert(AlertType.WARNING, "Giá trị tối đa sai", "Vui lòng nhập giá trị tối đa >= 0 !");
        			focus(txtGiaTriToiDa);
        			return false;
    			}
    			
    		}else { 
    			if(giaTriGiam < 0) {
    				showAlert(AlertType.WARNING, "Giá trị giảm không đúng", "Vui lòng nhập giá trị giảm >= 0 !");
        			focus(txtGiaTriGiam);
        			return false; 
    			}
    		}
    		
    		String dieuKienApDungString = "";
    		double dieuKienApDung = 0.0;
    		
    		if(radioHoaDon.isSelected()) {
    			dieuKienApDungString = txtDieuKienApDung.getText();
    			
    			if(dieuKienApDungString.isEmpty()) {
    				showAlert(AlertType.WARNING, "Điều kiện áp dụng rỗng", "Vui lòng nhập điều kiện áp dụng cho hóa đơn!");
        			focus(txtDieuKienApDung);
        			return false;
    			}
    			
    			try {
    				dieuKienApDung = Double.parseDouble(dieuKienApDungString);	
			} catch (Exception e) {
				showAlert(AlertType.WARNING, "Điều kiên áp dụng sai", "Vui lòng nhập điều kiện áp dụng là số !");
	    			focus(txtDieuKienApDung);
	    			return false;
			}
    			
    			if(dieuKienApDung < 0) {
    				showAlert(AlertType.WARNING, "Điều kiên áp dụng sai", "Vui lòng nhập điều kiện áp dụng >= 0!");
	    			focus(txtDieuKienApDung);
	    			return false;
    			}
    			
    		}
    		
    		LocalDate ngayBD = dateBatDau.getValue();
    		
    		if(ngayBD == null) {
    			showAlert(AlertType.WARNING, "Ngày bắt đầu rỗng", "Vui lòng chọn ngày bắt đầu!");
    			dateBatDau.requestFocus();
    			return false;
    		}
    		
    		LocalDate ngayKT = dateKetThuc.getValue();
    		
    		if(ngayKT == null) {
    			showAlert(AlertType.WARNING, "Ngày kết thúc rỗng", "Vui lòng chọn ngày kết thúc!");
    			dateKetThuc.requestFocus();
    			return false;
    		}
    		
    		if(ngayBD.isBefore(LocalDate.now())) {
    			showAlert(AlertType.WARNING, "Ngày bắt không hợp lệ", "Ngày bắt đầu không được trước ngày hiện tại!");
    			dateBatDau.requestFocus();
    			return false; 
    		}
    		
    		if (ngayKT.isBefore(ngayBD)) {
    		    showAlert(AlertType.WARNING, "Ngày kết thúc không hợp lệ", "Ngày kết thúc phải sau hoặc bằng ngày bắt đầu!");
    		    dateKetThuc.requestFocus();
    		    return false;
    		}
    		
    		if(radioMonAn.isSelected()) {
    			if(dsMonChon.isEmpty()) {
    				showAlert(AlertType.WARNING, "Chưa chọn món", "Vui lòng chọn ít nhất một món muốn khuyến mãi!");
        			btnChonMonAn.fire();
        			return false;
    			}
    		}
    		
    		KhuyenMai km = new KhuyenMai();
    		km.setMaKhuyenMai(maKM);
    		km.setTenKhuyenMai(tenKhuyenMai);
    		if(radioCo.isSelected()) {
    			km.setGiamGiaPhanTram(true);
    			km.setGiaTriToiDa(giaTriToiDa);
    		}else {
    			km.setGiamGiaPhanTram(false);
    			km.setGiaTriToiDa(0.0);
    		}
    		
    		km.setGiaTriGiam(giaTriGiam);
    		
    		km.setNgayBatDau(ngayBD);
    		km.setNgayKetThuc(ngayKT);
    		
    		if(radioHoaDon.isSelected()) {
    			if(flagXemThem) {
    				//Tạo khuyến mãi cho hóa đơn
        			km.setDieuKienApDung(dieuKienApDung);
        			if(control.themKhuyenMai(km)) {
            			showAlert(AlertType.INFORMATION, "THÀNH CÔNG", "Thêm khuyến mãi mới thành công");
            			loadLaiDanhSachKM();
            			return true;
            		}else {
            			showAlert(AlertType.ERROR, "THẤT BẠI", "Không thể thêm khuyến mãi mới");
            			return false;
            		}
    			}else {
    				//Sửa cho khuyến mãi hóa đơn
    				if(control.suaKhuyenMai(km, dsMonChon, 0)) {
    					showAlert(AlertType.INFORMATION, "THÀNH CÔNG", "Sửa khuyến mãi thành công");
            			loadLaiDanhSachKM();
            			return true;
    				}else {
            			showAlert(AlertType.ERROR, "THẤT BẠI", "Không thể sửa khuyến mãi");
            			return false;
            		}
    			}
    		}else {
    			if (flagXemThem) {
    				//Tạo khuyến mãi cho các món ăn
        			km.setDieuKienApDung(0.0);
        			if(control.themKhuyenMai(km) && control.themDSCTKMMonAn(dsMonChon, km)) {
            			showAlert(AlertType.INFORMATION, "THÀNH CÔNG", "Thêm khuyến mãi mới thành công");
            			loadLaiDanhSachKM();
            			return true;
            		}else {
            			showAlert(AlertType.ERROR, "THẤT BẠI", "Không thể thêm khuyến mãi mới");
            			return false;
            		}
			}else {
				//Sửa khuyến mãi cho món ăn
				if(control.suaKhuyenMai(km, dsMonChon, 1)) {
					showAlert(AlertType.INFORMATION, "THÀNH CÔNG", "Sửa khuyến mãi thành công");
	        			loadLaiDanhSachKM();
	        			return true;
				}else {
	        			showAlert(AlertType.ERROR, "THẤT BẠI", "Không thể sửa khuyến mãi");
	        			return false;
        			}
				
			}
    		}
    		
    }
    
    public boolean hanhDongSua() {
    		return true;
    }
    
    public void loadLaiDanhSachKM() {
    		dsKhuyenMai = FXCollections.observableArrayList(control.layDanhSachCTKM());
    		tableDSKM.setItems(dsKhuyenMai);
    }
    
    public void loadLaiDSMonAn(LocalDate ngayBatDau, LocalDate ngayKetThuc) {
    		dsTTMonAn = FXCollections.observableArrayList(control.layDanhSachMonAnCTKM(ngayBatDau, ngayKetThuc));
    		dsMonChon.clear();
    		tableDSMonKMTheoMa.setItems(dsMonChon);
    }
    
    public void quayVe() {
    		for(String monAn : dsMonTemp) {
		        String[] monAnTach = monAn.split("_");
		        
		        String monAn2 = monAnTach[0] + "_" +
		        		monAnTach[1] + "_" +
		        		monAnTach[2] + "_" +
		        		monAnTach[3] + "_" +
		        		monAnTach[4] + "_" +
		        		monAnTach[5] + "_" +
		        		monAnTach[3] + "_" +
		        		monAnTach[7] ;
		        
		        dsTTMonAn.add(monAn2);
			}
			dsMonTemp.clear();
    }
    
    public void themMonChon() {
	    	for(String monAn : dsMonTemp) {
		        dsMonChon.add(monAn);
			}
			dsMonTemp.clear();
    }
    
    public void hanhDongNgungKM() {
    		String maKM = txtMaKhuyenMai.getText();
    		
    		if (maKM.isEmpty()) {
    	        showAlert(Alert.AlertType.WARNING, "Chưa chọn KM", "Vui lòng chọn mã khuyến mãi trước khi ngừng.");
    	        return;
    	    }
    		
    		// Hiển thị hộp thoại xác nhận
    	    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    	    alert.setTitle("Xác nhận");
    	    alert.setHeaderText("Bạn có chắc muốn ngừng khuyến mãi này không?");
    	    alert.setContentText("Mã khuyến mãi: " + maKM);
    	    
    	    Optional<ButtonType> result = alert.showAndWait();
    	    if (result.isPresent() && result.get() == ButtonType.OK) {

    	        boolean success = control.ngungKhuyenMai(maKM);
    	        if (success) {
    	            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Khuyến mãi đã được ngừng.");

	    	         // == reset lại ==
	    	    	    clear();
	    	    		loadLaiDanhSachKM();
    	        } else {
    	            showAlert(Alert.AlertType.ERROR, "Thất bại", "Không thể ngừng khuyến mãi. Kiểm tra lại dữ liệu.");
    	        }
    	    }
    	    
    	    
    }
    
    public void hanhDongXoaKM() {
    		String maKM = txtMaKhuyenMai.getText();
		
		if (maKM.isEmpty()) {
	        showAlert(Alert.AlertType.WARNING, "Chưa chọn KM", "Vui lòng chọn mã khuyến mãi trước khi xóa.");
	        return;
	    }
		
		// Hiển thị hộp thoại xác nhận
	    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	    alert.setTitle("Xác nhận");
	    alert.setHeaderText("Bạn có chắc muốn xóa khuyến mãi này không?");
	    alert.setContentText("Mã khuyến mãi: " + maKM);
		
	    int loaiKM = radioMonAn.isSelected() ? 1 : 0;
	    
	    Optional<ButtonType> result = alert.showAndWait();
	    if (result.isPresent() && result.get() == ButtonType.OK) {

	        boolean success = control.xoaKhuyenMai(maKM, loaiKM);
	        
	        if (success) {
	            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa khuyến mãi thành công");

		         // == reset lại ==
		    	    clear();
		    		loadLaiDanhSachKM();
	        } else {
	            showAlert(Alert.AlertType.ERROR, "Thất bại", "Không thể xóa khuyến mãi này, vui lòng kiểm tra lại dữ liệu");
	        }
	    }
	    
    }
    
    private void locKhuyenMai() {
        String tuKhoa = txtTimKiem.getText().trim();
        String trangThai = cboTT.getValue();
        String loai = cboLoai.getValue();
        LocalDate today = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        List<String> ketQua = dsKhuyenMai.stream().filter(km -> {

            boolean matchTimKiem = tuKhoa.isEmpty() ||
                    km.split(",")[0].equals(tuKhoa);

            // ==== Lọc theo trạng thái ====
            boolean matchTrangThai = true;
            LocalDate bd = LocalDate.parse(km.split(",")[2], dtf);
            LocalDate kt = LocalDate.parse(km.split(",")[3], dtf);

            switch (trangThai) {
                case "Sắp diễn ra":
                    matchTrangThai = bd.isAfter(today);
                    break;
                case "Đang diễn ra":
                    matchTrangThai = ( !bd.isAfter(today) ) && ( !kt.isBefore(today) );
                    break;

                case "Đã kết thúc":
                    matchTrangThai = kt.isBefore(today);
                    break;

                default: 
                    matchTrangThai = true; 
            }

            // ==== Lọc theo loại ====
            boolean matchLoai = true;

            switch (loai) {
                case "Hóa đơn":
                    matchLoai = km.split(",")[8].equals("0");
                    break;

                case "Món ăn":
                    matchLoai = km.split(",")[8].equals("1");
                    break;

                default:
                    matchLoai = true; 
            }

            return matchTimKiem && matchTrangThai && matchLoai;

        }).toList();
        clear();

        tableDSKM.setItems(FXCollections.observableArrayList(ketQua));
    }
    
    public void taoMoiTimKiem() {
    		clear();
    		txtTimKiem.setText("");
    		cboLoai.setValue("Tất cả");
    		cboTT.setValue("Tất cả");
    		locKhuyenMai();
    		loadLaiDanhSachKM();
    }
    
    public void locMonAn() {
    		dsLocMon.clear();
    		if(cboLocMon.getValue().equals("Tất cả")) {
    			listMonAn.setItems(dsTTMonAn);
    			return;
    		}
    	
		String chuoi = cboLocMon.getValue().equals("Đã giảm giá") ? "1" : "0";
		
		for(String i : dsTTMonAn) {
			if(i.split("_")[5].equals(chuoi)) {
				dsLocMon.add(i);
			}
		}
		
		listMonAn.setItems(dsLocMon);
    }
}