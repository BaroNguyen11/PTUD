package gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;

import ctrl.QLMon_Ctrl;
import entity.MonAn;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
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
import lib.ImageCacheManager;
import lib.SupabaseImageUploader;




public class Gui_QLMon extends BorderPane{

	
	
	private ComboBox<String> cboLoai;
	private QLMon_Ctrl control;
	private VBox selectedMon = null;
	private List<MonAn> dsMon;
	private TextField timKiem;
	private VBox vboxDSMon;
	private File selectedImageFile;

	public Gui_QLMon() {
		BorderPane mainLayout = new BorderPane();
		control = new QLMon_Ctrl();
		
		//Cai dat
		dsMon = control.layDSMon();
		mainLayout.setTop(taoPhanTimKiem());
		mainLayout.setCenter(taoPhanDanhSach(dsMon));
		
		
		mainLayout.setStyle("-fx-background-color: white;");
		
		this.getStylesheets().add(getClass().getResource("/css/qlmon.css").toExternalForm());
		// Set vào BorderPane chính (this)
		this.setCenter(mainLayout);
	}
	
	
	public VBox taoPhanTimKiem() {
		//Tieu đè tìm kiếm	
		Label lblTieuDeTim = createModernSectionTitle("Tìm kiếm", "#667EEA");
		
		// Ô tìm kiếm
        Label lblTiemKiem = new Label("Tìm kiếm");
        lblTiemKiem.getStyleClass().add("fontTieuDeNho");
        timKiem = new TextField();
        timKiem.setPromptText("Nhập mã món ăn...");
        timKiem.getStyleClass().add("timKiem");
        Button nutTimKiem = new Button("Tìm kiếm");
        nutTimKiem.getStyleClass().add("button-timKiem");
        HBox oTimKiem = new HBox(10, timKiem, nutTimKiem);
        oTimKiem.setAlignment(Pos.CENTER_LEFT);
        VBox vboxTimKiem = new VBox(5);
        vboxTimKiem.getChildren().addAll(lblTiemKiem, oTimKiem);
        
        
        //Combox loại món ăn
        cboLoai = new ComboBox<String>();
        String[] dsLoai = {"Tất cả", "Khai vị", "Món chính", "Ăn kèm", "Nước sốt", "Nước uống", "Tráng miệng"};        
        cboLoai.getItems().addAll(dsLoai);
        cboLoai.setValue("Tất cả");
        
        Label lblLoaiMon = new Label("Loại");
        lblLoaiMon.getStyleClass().add("fontTieuDeNho");
        
        VBox vboxLoai = new VBox(3);
        vboxLoai.getChildren().addAll(lblLoaiMon, cboLoai);
        
        //
        cboLoai.setOnAction(e ->{
        		timKiem();
        });
        
        
        //HBOX all
        HBox hboxAll = new HBox(10);
        hboxAll.getChildren().addAll(vboxTimKiem, vboxLoai);
        
        
        
        
        VBox vboxAll = new VBox(lblTieuDeTim, hboxAll);
        vboxAll.setStyle("-fx-background-color: white; -fx-padding: 10;");
        
        ///
        timKiem.setOnAction(e -> {;
        		timKiem();
        });
        
        nutTimKiem.setOnAction(e -> {;
			timKiem();
        });
        
        return vboxAll;
	}
	
	public VBox taoPhanDanhSach(List<MonAn> dsMon) {
		
		
		// Tiêu đề danh sách món ăn
        Label lblTieuDeDS = createModernSectionTitle("Danh sách món ăn", "#667EEA");
        
        //Button thêm món
        Button btnThemMon = new Button("Thêm món");
        
        btnThemMon.setOnAction(e -> {xuLiThemMon();});
        
        HBox hboxTieuDeVaBtn = new HBox();
        hboxTieuDeVaBtn.setPadding(new Insets(0, 20, 0, 0));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        hboxTieuDeVaBtn.getChildren().addAll(lblTieuDeDS, spacer, btnThemMon);
        
        //DAnh sách món ăn
        VBox vboxDSMon = taoDanhSachMonAn(dsMon);
        
        //VBox all
        VBox vboxAll = new VBox(hboxTieuDeVaBtn, vboxDSMon);
        
        vboxAll.setStyle("-fx-padding: 10");
        
        return vboxAll;
	}
	
	public VBox taoDanhSachMonAn(List<MonAn> dsMon) {
		VBox vboxAll	 = new VBox(15);
	    
	    //
	    vboxDSMon = new VBox(15);
	    vboxDSMon.setStyle("-fx-padding: 10 0 0 0");
	    
	    HBox hboxRow = null;           
	    int count = 0;

	    for (MonAn mon : dsMon) {

	        // Nếu bắt đầu 1 hàng mới
	        if (count % 6 == 0) {
	            hboxRow = new HBox(10);   
	             vboxDSMon.getChildren().add(hboxRow);
	        }

	        // Tạo UI đại diện cho món ăn
	        VBox monUI = taoMonAn(mon);

	        // Add món vào dòng hiện tại
	        hboxRow.getChildren().add(monUI);

	        count++;
	    }
	    
	    ScrollPane scrollPane = new ScrollPane(vboxDSMon);
	    scrollPane.setFitToWidth(true);     // auto fit theo chiều ngang
	    scrollPane.setPannable(true);       // cho phép kéo
	    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // ẩn thanh ngang (nếu muốn)
	    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // hiện thanh dọc khi cần

	    //VBox all cài đặt
	    vboxAll.setStyle("-fx-padding: 10 10 20 10");
	    vboxAll.getChildren().addAll(scrollPane);
	    
	    return vboxAll;
	}
	
	
	public VBox taoMonAn(MonAn mon) {
		// ✅ THÊM IMAGEVIEW HIỂN THỊ ẢNH
        ImageView imgMonAn = new ImageView();
        imgMonAn.setFitWidth(125);
        imgMonAn.setFitHeight(125);
        imgMonAn.setPreserveRatio(true);
		
        // Thêm clip bo góc trực tiếp (quan trọng!)
        Rectangle clip = new Rectangle(125, 125);  // Kích thước khớp với fitWidth/Height
        clip.setArcWidth(25);   // Độ bo ngang (25px = bo nhẹ, tăng lên 125 để gần tròn)
        clip.setArcHeight(25);  // Độ bo dọc (giữ bằng nhau cho bo đều 4 góc)
        imgMonAn.setClip(clip);  // Áp dụng clip - ảnh sẽ bị cắt bo góc
        
		// ✅ Load ảnh từ Supabase
        if (mon.getHinhAnh() != null && !mon.getHinhAnh().isEmpty()) {
            String SUPABASE_BASE_URL = "https://yxemxycygkhxygaydgcl.supabase.co/storage/v1/object/public/image/";

            // Lấy ảnh từ cache (hoặc download nếu chưa có)
            String imagePath = ImageCacheManager.getImagePath(SUPABASE_BASE_URL, mon.getHinhAnh());

            if (imagePath != null) {
                try {
                    Image image = new Image(imagePath);
                    imgMonAn.setImage(image);
                } catch (Exception e) {
                    loadDefaultImage(imgMonAn);
                }
            } else {
                // Không download được → Dùng ảnh mặc định
                loadDefaultImage(imgMonAn);
            }
        } else {
            loadDefaultImage(imgMonAn);
        }
		
		HBox hboxImg = new HBox(imgMonAn);
		hboxImg.getStyleClass().add("hinhAnhMonAn");
		hboxImg.setPadding(new Insets(10, 0 ,0 ,0));
		
		//HBOX all
		Label lblTenMon = new Label(mon.getTenMonAn());
		HBox hboxTen = new HBox(lblTenMon);
		lblTenMon.setWrapText(true);
		lblTenMon.setMaxWidth(180);
		
		Label lblMoTa = new Label(mon.getMoTa());
		HBox hboxMoTa = new HBox(lblMoTa);
		lblMoTa.setWrapText(true);
		lblMoTa.setMaxWidth(180);
		
		DecimalFormat dcm = new DecimalFormat("#,##0đ");
		Label lblGia = new Label(dcm.format(mon.getGiaTien()));
		HBox hboxGia = new HBox(lblGia);
		
		lblTenMon.getStyleClass().add("tenMon");
		lblMoTa.getStyleClass().add("moTa");
		lblGia.getStyleClass().add("giaTien");
		
		hboxTen.setAlignment(Pos.CENTER);
		hboxMoTa.setAlignment(Pos.CENTER);
		hboxGia.setAlignment(Pos.CENTER);
		hboxImg.setAlignment(Pos.CENTER);
		
		/// Spacer
		Region spacer = new Region();
		VBox.setVgrow(spacer, Priority.ALWAYS);
		
		//Vbox all
		VBox vboxAll = new VBox();
		vboxAll.getChildren().addAll(hboxImg, hboxTen, hboxMoTa, spacer, hboxGia);
		
		vboxAll.getStyleClass().add("theMonAn");
		vboxAll.setPrefHeight(240);
		vboxAll.setPrefWidth(200);
		
		vboxAll.setOnMouseEntered(e -> {
			if(selectedMon != vboxAll)
		    vboxAll.setStyle("""
		    		-fx-background-color: white;
		    		-fx-background-radius: 10px;
		    		-fx-padding: 0 0 15 0;
		    		-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0.1, 0, 0);
		    		-fx-border-width: 1;
		    		-fx-border-color: #072847;
		    		-fx-border-radius: 10px;
		    		-fx-cursor: hand;
		    		""");
		});

		vboxAll.setOnMouseExited(e -> {
			if(selectedMon != vboxAll)
				vboxAll.setStyle("""
			    		-fx-background-color: white;
			    		-fx-background-radius: 10px;
			    		-fx-padding: 0 0 15 0;
			    		-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0.1, 0, 0);
			    		-fx-border-width: 1;
			    		-fx-border-color: white;
			    		-fx-border-radius: 10px;
			    		-fx-cursor: hand;
			    		""");
		});
		
		
		
		vboxAll.setOnMouseClicked(e -> {
			
		    if (selectedMon != null) {
		    			selectedMon.setStyle("""
			    		-fx-background-color: white;
			    		-fx-background-radius: 10px;
			    		-fx-padding: 0 0 15 0;
			    		-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0.1, 0, 0);
			    		-fx-border-radius: 10px;
			    		-fx-border-width: 1;
			    		-fx-border-color: white;
			    		-fx-cursor: hand;
			    		""");
		    }
		    selectedMon = vboxAll;
		    vboxAll.setStyle("""
		    		-fx-background-color: white;
		    		-fx-background-radius: 10px;
		    		-fx-padding: 0 0 15 0;
		    		-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0.1, 0, 0);
		    		-fx-border-width: 1;
		    		-fx-border-color: #FEEC49;
		    		-fx-border-radius: 10px;
		    		-fx-cursor: hand;
		    		""");
		    
		    showMonAnModal(mon);
		});
		
		return vboxAll;
		
	}
	
	
	
	private HBox createInputField(String labelText, TextField textField, boolean isReadOnly) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);

        Label label = new Label(labelText);
        label.setMinWidth(110);
        label.getStyleClass().add("fontTieuDeNho");

        textField.setPrefWidth(250);
        textField.setEditable(!isReadOnly);

        if (isReadOnly) {
            textField.setStyle(
                    "-fx-background-color: #ECF0F1; " +
                            "-fx-border-color: #bdc3c7; " +
                            "-fx-border-radius: 6; " +
                            "-fx-background-radius: 6; " +
                            "-fx-border-width: 1.5; " +
                            "-fx-padding: 5; " +
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
                            "-fx-padding: 5; " +
                            "-fx-font-size: 15px;"
            );

            textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    textField.setStyle(
                            "-fx-background-color: white; " +
                                    "-fx-border-color: #667eea; " +
                                    "-fx-border-width: 1.5; " +
                                    "-fx-border-radius: 6; " +
                                    "-fx-background-radius: 6; " +
                                    "-fx-padding: 5; " +
                                    "-fx-font-size: 15px; " 
                                 
                    );
                } else {
                    textField.setStyle(
                            "-fx-background-color: white; " +
                                    "-fx-border-color: #3498db; " +
                                    "-fx-border-width: 1.5; " +
                                    "-fx-border-radius: 6; " +
                                    "-fx-background-radius: 6; " +
                                    "-fx-padding: 5; " +
                                    "-fx-font-size: 15px;"
                    );
                }
            });
        }

        row.getChildren().addAll(label, textField);
        return row;
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
			svg.setStyle("-fx-stroke: " + mau + "; -fx-fill: transparent;");
			return svg;
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

	 public void showMonAnModal(MonAn mon) {

		    Stage primaryStage = (Stage) this.getScene().getWindow();

		    // Áp dụng hiệu ứng mờ cho màn hình chính
		    BoxBlur blur = new BoxBlur(5, 5, 3);
		    Parent rootPane = this.getScene().getRoot();
		    rootPane.setEffect(blur);

		    // Tạo modal stage
		    Stage modal = new Stage();
		    modal.initModality(Modality.APPLICATION_MODAL);
		    modal.initOwner(primaryStage);
		    modal.setTitle(mon.getTenMonAn());

		    modal.setWidth(750);
		    modal.setHeight(500);

		    //VBOX trai
		    VBox vboxTrai = new VBox(30);

		    vboxTrai.setPrefWidth(300);
		    vboxTrai.setPrefHeight(500);

		    vboxTrai.setPadding(new Insets(30, 10, 0, 10));

		    StackPane stThemAnh = new StackPane();

		    HBox boxHinhAnh = new HBox(10);
		    boxHinhAnh.setPrefHeight(240);
		    boxHinhAnh.setPrefWidth(240);

		    boxHinhAnh.setStyle("""
		     -fx-background-color: white;
		     -fx-background-radius: 10px;
		     -fx-border-radius: 10px;
		     -fx-border-color: #cccccc;
		     -fx-border-width: 1px;
		     -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 0);
		     """);


		    ImageView imgMonAn = new ImageView();
		        imgMonAn.setFitWidth(230);
		        imgMonAn.setFitHeight(230);
		        imgMonAn.setPreserveRatio(true);

		        // Thêm clip bo góc trực tiếp (quan trọng!)
		        Rectangle clip = new Rectangle(230, 230); // Kích thước khớp với fitWidth/Height
		        clip.setArcWidth(25); // Độ bo ngang (25px = bo nhẹ, tăng lên 125 để gần tròn)
		        clip.setArcHeight(25); // Độ bo dọc (giữ bằng nhau cho bo đều 4 góc)
		        imgMonAn.setClip(clip); // Áp dụng clip - ảnh sẽ bị cắt bo góc

		        // ✅ Load ảnh từ Supabase
		        if (mon.getHinhAnh() != null && !mon.getHinhAnh().isEmpty()) {
		            String SUPABASE_BASE_URL = "https://yxemxycygkhxygaydgcl.supabase.co/storage/v1/object/public/image/";

		            // Lấy ảnh từ cache (hoặc download nếu chưa có)
		            String imagePath = ImageCacheManager.getImagePath(SUPABASE_BASE_URL, mon.getHinhAnh());

		            if (imagePath != null) {
		                try {
		                    Image image = new Image(imagePath);
		                    imgMonAn.setImage(image);
		                } catch (Exception e) {
		                    loadDefaultImage(imgMonAn);
		                }
		            } else {
		                // Không download được → Dùng ảnh mặc định
		                loadDefaultImage(imgMonAn);
		            }
		        } else {
		            loadDefaultImage(imgMonAn);
		        }

		    imgMonAn.setStyle("""
		     -fx-background-radius: 10px;
		     """);

		    // Thêm vào boxHinhAnh
		    boxHinhAnh.getChildren().add(imgMonAn);
		    boxHinhAnh.setAlignment(Pos.CENTER);

		    // Tạo overlay mờ cho hover
		    StackPane overlay = new StackPane();
		    Rectangle overlayRect = new Rectangle(240, 240);
		    overlayRect.setFill(Color.rgb(255, 255, 255, 0.5)); // Mờ đen 50%
		    overlayRect.setArcWidth(10);
		    overlayRect.setArcHeight(10);

		    SVGPath overlayPlus = createSvgIcon(60, 24, "gray", "M12 3.75a.75.75 0 0 1 .75.75v6.75h6.75a.75.75 0 0 1 0 1.5h-6.75v6.75a.75.75 0 0 1-1.5 0v-6.75H4.5a.75.75 0 0 1 0-1.5h6.75V4.5a.75.75 0 0 1 .75-.75Z");
	        overlayPlus.setStyle("-fx-background-color: gray; -fx-border-color: gray");

		    overlay.getChildren().addAll(overlayRect, overlayPlus);
		    overlay.setAlignment(Pos.CENTER);
		    overlay.setVisible(false); // Ban đầu ẩn

		    // Thêm tất cả vào StackPane
		    stThemAnh.getChildren().addAll(boxHinhAnh, overlay);
		    stThemAnh.setAlignment(Pos.CENTER);

		    vboxTrai.getChildren().addAll(stThemAnh);
		    vboxTrai.setAlignment(Pos.CENTER);

		    //VBox phải modal

		    VBox vboxPhai = new VBox(5);

		    vboxPhai.setPrefWidth(450);
		    vboxPhai.setPrefHeight(500);

		    Label lblTieuDe = createModernSectionTitle("Thông tin món ăn", "#667EEA");

		    TextField txtMa = new TextField();
		    TextField txtTen = new TextField();
		    TextField txtGia = new TextField();

		    ComboBox<String> cboLoai = new ComboBox<String>();

		    String[] loai = {"Khai vị", "Món chính", "Ăn kèm", "Nước uống", "Nước sốt", "Tráng miệng"};

		    cboLoai.setItems(FXCollections.observableArrayList(loai));

		    cboLoai.setPromptText("Chọn loại món");
		    cboLoai.setEditable(false);
		    cboLoai.setPrefWidth(250);
		    cboLoai.setDisable(true); // Disable chức năng

		    // Style CSS đúng format (text block sạch, không +)
		    cboLoai.setStyle("""
		        -fx-opacity: 1.0;

		        -fx-background-color: #ECF0F1;
		        -fx-border-color: #bdc3c7;
		        -fx-font-size: 15px;
		        -fx-background-radius: 6px;
		        -fx-border-radius: 6px;
		        -fx-border-width: 1.5px;
		        -fx-pading: 5px;
		        """);

		    Label lblLoai = new Label("Loại");

		    lblLoai.getStyleClass().add("fontTieuDeNho");

		    HBox hbox1 = createInputField("Mã", txtMa, true);
		    HBox hbox2 = createInputField("Tên", txtTen, true);
		    HBox hbox3 = new HBox(98, lblLoai, cboLoai);
		    HBox hbox4 = createInputField("Giá", txtGia, true);



		    Label lblMoTa = new Label("Mô tả");
		    lblMoTa.getStyleClass().add("fontTieuDeNho");

		    TextArea textArea = new TextArea();
		    textArea.setPromptText("Nhập mô tả");
		    textArea.setWrapText(true);
		    textArea.setPrefWidth(250);
		    textArea.setPrefHeight(150);

		    textArea.setStyle("""
		     -fx-control-inner-background: #ECF0F1;
		    -fx-border-color: #bdc3c7;
		    -fx-border-radius: 6;
		    -fx-background-radius: 6;
		    -fx-border-width: 1.5px;
		    -fx-font-size: 15px;
		    -fx-text-fill: #7f8c8d;
		     """);

		    textArea.setEditable(false);

		    HBox hbox5 = new HBox(88);

		    hbox5.getChildren().addAll(lblMoTa, textArea);

		    vboxPhai.getChildren().addAll(lblTieuDe, hbox1, hbox2, hbox3, hbox4, hbox5);

		    DecimalFormat dcm = new DecimalFormat("#,##0.0 VND");

		    //Set Gias tri
		    txtMa.setText(mon.getMaMonAn());
		    txtTen.setText(mon.getTenMonAn());
		    cboLoai.setValue(mon.getLoaiMon());
		    txtGia.setText(dcm.format(mon.getGiaTien()));
		    textArea.setText(mon.getMoTa());

		    // 2 nut
		    Button btnQuayVe = new Button("Quay về");
		    Button btnSua = new Button("Sửa");

		    btnQuayVe.setStyle("""
		     -fx-background-color: gray;
		     -fx-background-radius: 6px;
		     -fx-text-fill: white;
		     -fx-font-size: 15px;
		     -fx-cursor: hand;
		     -fx-font-weight: bold;
		     """);

		    btnSua.setStyle("""
		     -fx-background-radius: 5;
		-fx-background-color: #F4C430;
		-fx-font-size: 15px;
		-fx-text-fill: white;
		-fx-pref-width: 80px;
		-fx-cursor: hand;
		-fx-font-weight: bold;
		-fx-padding: 5;
		     """);

		    ///
		    btnQuayVe.setOnAction(e ->{
		     modal.close();
		     selectedImageFile = null;
		    });

		    btnSua.setOnAction(e ->{
		     if(btnSua.getText().equals("Sửa")) {
		     xuLiSuaMon(txtTen, cboLoai, textArea); // Không còn btnThemAnh

		     // Kích hoạt hiệu ứng hover và click cho stThemAnh
		     stThemAnh.setCursor(Cursor.HAND);

		     stThemAnh.setOnMouseEntered(ev -> {
		         overlay.setVisible(true);
		     });

		     stThemAnh.setOnMouseExited(ev -> {
		         overlay.setVisible(false);
		     });

		     stThemAnh.setOnMouseClicked(ev -> {
		         FileChooser fileChooser = new FileChooser();
		         fileChooser.setTitle("Chọn ảnh món ăn");
		         fileChooser.getExtensionFilters().addAll(
		             new FileChooser.ExtensionFilter("Hình ảnh", "*.jpg", "*.png", "*.jpeg")
		         );
		         File selectedFile = fileChooser.showOpenDialog(modal); // Mở dialog từ modal stage

		         if (selectedFile != null) {
		             try {
		                 // Load preview
		                 Image image = new Image(selectedFile.toURI().toString());
		                 imgMonAn.setImage(image);

		                 // Lưu file tạm để upload sau (e.g., biến class hoặc field)
		                 selectedImageFile = selectedFile; // Giả sử bạn có field File selectedImageFile;

		                 System.out.println("✅ Chọn ảnh: " + selectedFile.getName());
		             } catch (Exception ex) {
		                 System.err.println("❌ Lỗi load preview: " + ex.getMessage());
		             }
		         }else {
		        	 
		         }
		     });

		     btnSua.setText("Lưu");

		     btnSua.setStyle("""
		             -fx-background-radius: 5;
		     -fx-background-color: #082744;
		     -fx-font-size: 15px;
		     -fx-text-fill: white;
		     -fx-pref-width: 80px;
		     -fx-cursor: hand;
		     -fx-font-weight: bold;
		     -fx-padding: 5;
		         """);
		     }else {
		     if(xuLiLuuSauKhiSua(txtTen, cboLoai, textArea, txtMa.getText())) {
		     modal.close();
		     }
		     }

		    });


		    //HBOX button
		    Region spacer2 = new Region();
		    HBox.setHgrow(spacer2, Priority.ALWAYS);

		    HBox hboxBTNDuoi = new HBox(btnQuayVe, spacer2, btnSua);
		    hboxBTNDuoi.setPadding(new Insets(0, 40, 0, 128));

		    vboxPhai.getChildren().add(hboxBTNDuoi);

		    //Layout modal
		    HBox layout = new HBox(15,vboxTrai, vboxPhai);
		    layout.setAlignment(Pos.CENTER);
		    layout.setPadding(new Insets(20));
		    layout.setStyle("-fx-background-color: white;");

		    // Scene
		    Scene scene = new Scene(layout);
		    scene.getStylesheets().add(getClass().getResource("/css/qlmon.css").toExternalForm());
		    modal.setScene(scene);

		    // Fade-in modal
		    layout.setOpacity(0);
		    modal.setOnShown(e -> {
		        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), layout);
		        fadeIn.setFromValue(0);
		        fadeIn.setToValue(1);
		        fadeIn.play();
		    });

		    // Khi modal đóng: bỏ blur + fade-out
		    modal.setOnHiding(e -> {
		    	selectedImageFile = null;
		        rootPane.setEffect(null);
		    });

		    modal.showAndWait();
		}
	 
	 public void timKiem() {
		String maMon = timKiem.getText().trim();
		String loai = cboLoai.getValue();
		
		List<MonAn> dsLoc = control.locDSMon(dsMon, maMon, loai);
		
		capNhatDanhSachMon(dsLoc);
	 	
	 }
	 
	 public void capNhatDanhSachMon(List<MonAn> dsMon) {
		    vboxDSMon.getChildren().clear();  

		    HBox hboxRow = null;
		    int count = 0;

		    for (MonAn mon : dsMon) {
		        if (count % 6 == 0) {
		            hboxRow = new HBox(10);
		            vboxDSMon.getChildren().add(hboxRow);
		        }

		        VBox monUI = taoMonAn(mon);
		        hboxRow.getChildren().add(monUI);

		        count++;
		    }
		}
	 
	 private void loadDefaultImage(ImageView imgView) {
	        try {
	            Image defaultImg = new Image(getClass().getResourceAsStream("/img/default-food.png"));
	            imgView.setImage(defaultImg);
	        } catch (Exception e) {
	            imgView.setStyle("-fx-background-color: #E2E8F0;");
	        }
	    }
	 
	 public void showMonAnModalThemMon() {

		    Stage primaryStage = (Stage) this.getScene().getWindow();

		    // Áp dụng hiệu ứng mờ cho màn hình chính
		    BoxBlur blur = new BoxBlur(5, 5, 3);
		    Parent rootPane = this.getScene().getRoot();
		    rootPane.setEffect(blur);

		    // Tạo modal stage
		    Stage modal = new Stage();
		    modal.initModality(Modality.APPLICATION_MODAL);
		    modal.initOwner(primaryStage);
		    modal.setTitle("Thêm món ăn");

		    modal.setWidth(750);
		    modal.setHeight(500);

		    //VBOX trai
		    VBox vboxTrai = new VBox(30);

		    vboxTrai.setPrefWidth(300);
		    vboxTrai.setPrefHeight(500);

		    vboxTrai.setPadding(new Insets(30, 10, 0, 10));

		    StackPane stThemAnh = new StackPane();

		    HBox boxHinhAnh = new HBox(10);
		    boxHinhAnh.setPrefHeight(240);
		    boxHinhAnh.setPrefWidth(240);

		    SVGPath iconPlus = createSvgIcon(60, 24, "gray", "M12 3.75a.75.75 0 0 1 .75.75v6.75h6.75a.75.75 0 0 1 0 1.5h-6.75v6.75a.75.75 0 0 1-1.5 0v-6.75H4.5a.75.75 0 0 1 0-1.5h6.75V4.5a.75.75 0 0 1 .75-.75Z");
		    iconPlus.setStyle("-fx-background-color: gray; -fx-border-color: gray");

		    Label lblThemHinh = new Label("Thêm hình ảnh");

		    Label lblLuuY = new Label("Nên chọn hình ảnh có kích thước 1x1");

		    lblThemHinh.setStyle("""
		         -fx-text-fill: gray;
		         -fx-font-size: 15px;
		         """);

		    lblLuuY.setStyle("""
		         -fx-text-fill: gray;
		         -fx-font-size: 10px;
		         -fx-font-style: italic;
		         """);

		    boxHinhAnh.setStyle("""
		         -fx-background-color: white;
		         -fx-background-radius: 10px;
		         -fx-border-radius: 10px;
		         -fx-border-color: #cccccc;
		         -fx-border-width: 1px;
		         -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 0);
		         """);

		     	// Tạo ImageView cho preview (ban đầu ẩn)
		        ImageView imgPreview = new ImageView();
		        imgPreview.setFitWidth(230);
		        imgPreview.setFitHeight(230);
		        imgPreview.setPreserveRatio(true);
		        imgPreview.setVisible(false);

		        // Bo góc cho preview (như hướng dẫn trước)
		        Rectangle clip = new Rectangle(230, 230);
		        clip.setArcWidth(25);
		        clip.setArcHeight(25);
		        imgPreview.setClip(clip);
		        imgPreview.getStyleClass().add("hinhAnhMonAn"); // Áp CSS shadow/bo nền

		        // Thêm vào boxHinhAnh
		        boxHinhAnh.getChildren().add(imgPreview);
		        boxHinhAnh.setAlignment(Pos.CENTER);

		        // Ẩn icon/label khi có ảnh
		        VBox iconAndLabel = new VBox(30, iconPlus, lblThemHinh, lblLuuY); // Giữ nguyên
		        iconAndLabel.setVisible(true);
		        iconAndLabel.setAlignment(Pos.CENTER);

		        // Tạo overlay mờ cho hover
		        StackPane overlay = new StackPane();
		        Rectangle overlayRect = new Rectangle(240, 240);
		        overlayRect.setFill(Color.rgb(255, 255, 255, 0.5));
		        overlayRect.setArcWidth(10);
		        overlayRect.setArcHeight(10);

		        SVGPath overlayPlus = createSvgIcon(60, 24, "gray", "M12 3.75a.75.75 0 0 1 .75.75v6.75h6.75a.75.75 0 0 1 0 1.5h-6.75v6.75a.75.75 0 0 1-1.5 0v-6.75H4.5a.75.75 0 0 1 0-1.5h6.75V4.5a.75.75 0 0 1 .75-.75Z");
		        overlayPlus.setStyle("-fx-background-color: gray; -fx-border-color: gray");

		        overlay.getChildren().addAll(overlayRect, overlayPlus);
		        overlay.setAlignment(Pos.CENTER);
		        overlay.setVisible(false); // Ban đầu ẩn

		        // Thêm tất cả vào StackPane: boxHinhAnh (nền), iconAndLabel (nếu chưa có ảnh), overlay (trên cùng)
		        stThemAnh.getChildren().addAll(boxHinhAnh, iconAndLabel, overlay);
		        stThemAnh.setAlignment(Pos.CENTER);

		        // Làm cho stThemAnh clickable và hoverable
		        stThemAnh.setCursor(Cursor.HAND);

		        // Event hover: Hiện overlay và ẩn lớp hiện tại nếu cần
		        stThemAnh.setOnMouseEntered(e -> {
		            if (!imgPreview.isVisible()) {
		                iconAndLabel.setVisible(false);
		            }
		            overlay.setVisible(true);
		        });

		        stThemAnh.setOnMouseExited(e -> {
		            overlay.setVisible(false);
		            if (!imgPreview.isVisible()) {
		                iconAndLabel.setVisible(true);
		            }
		        });

		        // Event click: Chọn file ảnh
		        stThemAnh.setOnMouseClicked(e -> {
		            FileChooser fileChooser = new FileChooser();
		            fileChooser.setTitle("Chọn ảnh món ăn");
		            fileChooser.getExtensionFilters().addAll(
		                new FileChooser.ExtensionFilter("Hình ảnh", "*.jpg", "*.png", "*.jpeg")
		            );
		            File selectedFile = fileChooser.showOpenDialog(modal); // Mở dialog từ modal stage

		            if (selectedFile != null) {
		                try {
		                    // Load preview
		                    Image image = new Image(selectedFile.toURI().toString());
		                    imgPreview.setImage(image);
		                    imgPreview.setVisible(true); // Hiện ảnh
		                    iconAndLabel.setVisible(false); // Ẩn icon "+"

		                    // Lưu file tạm để upload sau (e.g., biến class hoặc field)
		                    selectedImageFile = selectedFile; // Giả sử bạn có field File selectedImageFile;

		                    System.out.println("✅ Chọn ảnh: " + selectedFile.getName());
		                } catch (Exception ex) {
		                    System.err.println("❌ Lỗi load preview: " + ex.getMessage());
		                }
		            }
		        });

		    vboxTrai.getChildren().addAll(stThemAnh);
		    vboxTrai.setAlignment(Pos.CENTER);

		    //VBox phải modal

		    VBox vboxPhai = new VBox(5);

		    vboxPhai.setPrefWidth(450);
		    vboxPhai.setPrefHeight(500);

		    Label lblTieuDe = createModernSectionTitle("Thông tin món ăn", "#667EEA");

		    TextField txtMa = new TextField();
		    TextField txtTen = new TextField();
		    ComboBox<String> cboLoai = new ComboBox<String>();

		    String[] loai = {"Khai vị", "Món chính", "Ăn kèm", "Nước uống", "Nước sốt", "Tráng miệng"};

		    cboLoai.setItems(FXCollections.observableArrayList(loai)); // Hoặc: cboLoai.getItems().addAll(loai);

		    cboLoai.setPromptText("Chọn loại món"); // Text gợi ý khi chưa chọn
		    cboLoai.setEditable(false); // Không cho edit (chỉ chọn từ list)
		    cboLoai.setPrefWidth(250); // Kích thước phù hợp

		    // Tùy chọn: Style CSS (thêm vào CSS file hoặc inline)
		    cboLoai.setStyle("""
		        -fx-font-size: 15px;
		        -fx-background-radius: 6px;
		        -fx-border-radius: 6px;
		        -fx-border-color: #3498db;
		        -fx-border-width: 1.5px;
		        -fx-pading: 5px;
		    """);

		    TextField txtGia = new TextField();

		    Label lblLoai = new Label("Loại");

		    lblLoai.getStyleClass().add("fontTieuDeNho");

		    HBox hbox1 = createInputField("Mã", txtMa, true);
		    HBox hbox2 = createInputField("Tên", txtTen, false);
		    HBox hbox3 = new HBox(98, lblLoai, cboLoai);
		    HBox hbox4 = createInputField("Giá", txtGia, false);

		    txtMa.setText(control.taoMaMonMoi());

		    Label lblMoTa = new Label("Mô tả");
		    lblMoTa.getStyleClass().add("fontTieuDeNho");

		    TextArea textArea = new TextArea();
		    textArea.setPromptText("Nhập mô tả");
		    textArea.setWrapText(true);
		    textArea.setPrefWidth(250);
		    textArea.setPrefHeight(150);

		    textArea.setStyle("""
		    -fx-border-color: #3498db;
		    -fx-border-radius: 6;
		    -fx-background-radius: 6;
		    -fx-border-width: 1.5;
		    -fx-font-size: 15px;
		    -fx-text-fill: black;
		     """);

		    textArea.setEditable(true);

		    HBox hbox5 = new HBox(88);

		    hbox5.getChildren().addAll(lblMoTa, textArea);

		    vboxPhai.getChildren().addAll(lblTieuDe, hbox1, hbox2, hbox3, hbox4, hbox5);

		    // 2 nut
		    Button btnQuayVe = new Button("Quay về");
		    Button btnLuu = new Button("Lưu");

		    btnQuayVe.setStyle("""
		         -fx-background-color: gray;
		         -fx-background-radius: 6px;
		         -fx-text-fill: white;
		         -fx-font-size: 15px;
		         -fx-cursor: hand;
		         -fx-font-weight: bold;
		         """);

		    btnLuu.setStyle("""
		         -fx-background-radius: 5;
		     -fx-background-color: #082744;
		     -fx-font-size: 15px;
		     -fx-text-fill: white;
		     -fx-pref-width: 80px;
		     -fx-cursor: hand;
		     -fx-font-weight: bold;
		     -fx-padding: 5;
		         """);

		    //
		    btnQuayVe.setOnAction(e ->{
		         modal.close();
		         selectedImageFile = null;
		    });

		    btnLuu.setOnAction(e -> {

		     String tenMon = txtTen.getText().trim();

		     if(tenMon.isEmpty() || tenMon == null) {
		     showAlert(AlertType.WARNING, "Cảnh báo", "Vui lòng nhập tên món!");
		     txtTen.requestFocus();
		     return;
		     }

		     String loaiMonString = cboLoai.getValue();

		     if(loaiMonString == null) {
		     showAlert(AlertType.WARNING, "Cảnh báo", "Vui lòng chọn loại món!");
		     cboLoai.requestFocus();
		     return;
		     }

		     String giaString = txtGia.getText().trim();

		     if(giaString == null || giaString.isBlank()) {
		     showAlert(AlertType.WARNING, "Cảnh báo", "Vui lòng nhập giá!");
		     txtGia.requestFocus();
		     return;
		     }

		     Double giaDouble = 0.0;

		     try {
		giaDouble = Double.parseDouble(giaString);
		} catch (Exception e2) {
		showAlert(AlertType.WARNING, "Cảnh báo", "Vui lòng nhập giá là chữ số!");
		     txtGia.requestFocus();
		     txtGia.selectAll();
		     return;
		}

		     if(giaDouble <= 0) {
		     showAlert(AlertType.WARNING, "Cảnh báo", "Vui lòng nhập giá > 0");
		     txtGia.requestFocus();
		     txtGia.selectAll();
		     return;
		     }

		     String moTa = textArea.getText().trim();

		     if(moTa.isBlank() || moTa == null) {
		     showAlert(AlertType.WARNING, "Cảnh báo", "Vui lòng nhập mô tả!");
		     textArea.requestFocus();
		     return;
		     }

		     if(moTa.length() > 150) {
		     showAlert(AlertType.WARNING, "Cảnh báo", "Vui lòng nhập mô tả ngắn gọn <= 150 kí tự!");
		     textArea.requestFocus();
		     textArea.selectAll();
		     return;
		     }

		     if(selectedImageFile == null) {
		     showAlert(AlertType.WARNING, "Cảnh báo", "Hãy chọn ảnh cho món ăn !!");
		     return;
		     }

		        // Upload ảnh lên Supabase
		        SupabaseImageUploader uploader = new SupabaseImageUploader();
		        String loaiMon = mapLoaiToSlug(cboLoai.getValue());

		        try {
		            String relativePath = uploader.uploadImage(selectedImageFile, loaiMon); // Upload & lấy path
		            System.out.println("✅ Upload thành công: " + relativePath);

		            //Insert vào sql

		            MonAn monMoi = new MonAn();
		            monMoi.setMaMonAn(txtMa.getText().trim());
		            monMoi.setTenMonAn(tenMon);
		            monMoi.setGiaTien(giaDouble); // Parse int
		            monMoi.setLoaiMon(cboLoai.getValue()); // Tên đầy đủ từ ComboBox
		            monMoi.setMoTa(moTa);
		            monMoi.setHinhAnh(relativePath); // Lưu relativePath

		            if(control.themMonMoi(monMoi)) {
		                 showAlert(AlertType.INFORMATION, "Thành Công", "Thêm món mới thành công!");
		            }else {
		                 System.out.println("Thêm món thất bại! " + relativePath);
		                 return;
		            }

		            // Đóng modal & reload UI
		            modal.close();
		            taiLaiDanhSachMonAnMoiNhat(); // Gọi method reload list món (từ code bạn)

		        } catch (IOException ex) {
		            System.err.println("❌ Upload thất bại: " + ex.getMessage());
		            Alert alert = new Alert(Alert.AlertType.ERROR);
		            alert.setContentText("Lỗi upload ảnh: " + ex.getMessage());
		            alert.showAndWait();
		        }
		    });

		    //HBOX button
		    Region spacer2 = new Region();
		    HBox.setHgrow(spacer2, Priority.ALWAYS);

		    HBox hboxBTNDuoi = new HBox(btnQuayVe, spacer2, btnLuu);
		    hboxBTNDuoi.setPadding(new Insets(0, 40, 0, 128));

		    vboxPhai.getChildren().add(hboxBTNDuoi);

		    //Layout modal
		    HBox layout = new HBox(15,vboxTrai, vboxPhai);
		    layout.setAlignment(Pos.CENTER);
		    layout.setPadding(new Insets(20));
		    layout.setStyle("-fx-background-color: white;");

		    // Scene
		    Scene scene = new Scene(layout);
		    scene.getStylesheets().add(getClass().getResource("/css/qlmon.css").toExternalForm());
		    modal.setScene(scene);

		    // Fade-in modal
		    layout.setOpacity(0);
		    modal.setOnShown(e -> {
		        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), layout);
		        fadeIn.setFromValue(0);
		        fadeIn.setToValue(1);
		        fadeIn.play();
		    });

		    // Khi modal đóng: bỏ blur + fade-out
		    modal.setOnHiding(e -> {
		    	selectedImageFile = null;
		        rootPane.setEffect(null);
		    });

		    modal.showAndWait();
		}
	 
	 public void taiLaiDanhSachMonAnMoiNhat() {;
	 	//Cai dat
		dsMon = control.layDSMon();
		
		capNhatDanhSachMon(dsMon);
	 }
	 
	 private String mapLoaiToSlug(String loaiTen) {
		    return switch (loaiTen) {
		        case "Khai vị" -> "khaivi";
		        case "Món chính" -> "monchinh";
		        case "Ăn kèm" -> "ankem";
		        case "Nước uống" -> "nuocuong";
		        case "Nước sốt" -> "nuocsot";
		        case "Tráng miệng" -> "trangmieng";
		        default -> loaiTen.toLowerCase().replace(" ", "");  // Fallback
		    };
		}
	 
	 public void xuLiThemMon() {
		 showMonAnModalThemMon();
	 }
	 
	 public void xuLiSuaMon(TextField txtTen, ComboBox cboLoai, TextArea textArea) {
		 txtTen.setEditable(true);
		 cboLoai.setDisable(false);
		 textArea.setEditable(true);
		 
		// Tùy chọn: Style CSS (thêm vào CSS file hoặc inline)
	    cboLoai.setStyle("""
	        -fx-font-size: 15px;
	        -fx-background-radius: 6px;
	        -fx-border-radius: 6px;
	        -fx-border-color: #3498db;
	        -fx-border-width: 1.5px;
	        -fx-pading: 5px;
	    """);
	    
	    txtTen.setStyle("-fx-background-color: white; " +
                "-fx-border-color: #3498db; " +
                "-fx-border-width: 1.5; " +
                "-fx-border-radius: 6; " +
                "-fx-background-radius: 6; " +
                "-fx-padding: 5; " +
                "-fx-font-size: 15px;");
	    
	    textArea.setStyle("""
			    -fx-border-color: #3498db;
			    -fx-border-radius: 6;
			    -fx-background-radius: 6;
			    -fx-border-width: 1.5;
			    -fx-font-size: 15px;
			    -fx-text-fill: black;
			     """);
	    
	    
	 }
	 
	 public boolean xuLiLuuSauKhiSua(TextField txtTen, ComboBox<String> cboLoai, TextArea textArea, String ma) {
		String tenMon = txtTen.getText().trim();
 		
		if(selectedImageFile == null) {
			showAlert(AlertType.WARNING, "Cảnh báo", "Vui lòng chọn một ảnh khác để sửa!");
			return false;
		}
		
 		if(tenMon.isEmpty() || tenMon == null) {
 			showAlert(AlertType.WARNING, "Cảnh báo", "Vui lòng nhập tên món!");
 			txtTen.requestFocus();
 			return false;
 		}
 		
 		String loaiMonString = cboLoai.getValue();
 		
 		if(loaiMonString == null) {
 			showAlert(AlertType.WARNING, "Cảnh báo", "Vui lòng chọn loại món!");
 			cboLoai.requestFocus();
 			return false;
 		}
 		
 		String moTa = textArea.getText().trim();
 		
 		if(moTa.isBlank() || moTa == null) {
 			showAlert(AlertType.WARNING, "Cảnh báo", "Vui lòng nhập mô tả!");
 			textArea.requestFocus();
 			return false;
 		}
 		
 		if(moTa.length() > 150) {
 			showAlert(AlertType.WARNING, "Cảnh báo", "Vui lòng nhập mô tả ngắn gọn <= 150 kí tự!");
 			textArea.requestFocus();
 			textArea.selectAll();
 			return false;
 		}
 		
 		// Upload ảnh lên Supabase
	    SupabaseImageUploader uploader = new SupabaseImageUploader();
	    String loaiMon = mapLoaiToSlug(cboLoai.getValue());
		
	 
		try{
			String relativePath = uploader.uploadImage(selectedImageFile, loaiMon); 
			System.out.println("✅ Upload thành công: " + relativePath);

			// Insert vào sql

			MonAn monMoi = new MonAn();
			monMoi.setMaMonAn(ma);
			monMoi.setTenMonAn(tenMon);
			monMoi.setLoaiMon(cboLoai.getValue()); 
			monMoi.setMoTa(moTa);
			monMoi.setHinhAnh(relativePath); 

			if (control.capNhatMonAn(monMoi)) {
				showAlert(AlertType.INFORMATION, "Thành Công", "Cập nhật món mới thành công!");
				taiLaiDanhSachMonAnMoiNhat(); 
				return true;
			} else {
				System.out.println("Câpj nhật món mới thất bại! " + relativePath);
				return false;
			}

		} catch (IOException ex) {
	        showAlert(AlertType.ERROR, "Thất bại", "Không thể upload hình ảnh!");
	        ex.printStackTrace();
	        return false;
		}
	}
}