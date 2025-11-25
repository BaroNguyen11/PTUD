package gui;

import java.text.DecimalFormat;
import java.util.List;

import control.Crl_QLMon;
import entity.MonAn;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
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




public class Gui_QuanLiMon extends BorderPane{

	
	
	private ComboBox<String> cboLoai;
	private Crl_QLMon control;
	private VBox selectedMon = null;
	private List<MonAn> dsMon;
	private TextField timKiem;
	private VBox vboxDSMon;

	public Gui_QuanLiMon() {
		BorderPane mainLayout = new BorderPane();
		control = new Crl_QLMon();
		
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
        
        //DAnh sách món ăn
        VBox vboxDSMon = taoDanhSachMonAn(dsMon);
        
        //VBox all
        VBox vboxAll = new VBox(lblTieuDeDS, vboxDSMon);
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
		/// img mon ăn
		ImageView imgView = new ImageView(new javafx.scene.image.Image(mon.getHinhAnh()));
		imgView.setFitWidth(100);
		imgView.setFitHeight(100);
		
		HBox hboxImg = new HBox(imgView);
		hboxImg.setPrefHeight(110);
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
                            "-fx-padding: 10; " +
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
                            "-fx-padding: 10; " +
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
                                    "-fx-padding: 10; " +
                                    "-fx-font-size: 15px; " 
                                 
                    );
                } else {
                    textField.setStyle(
                            "-fx-background-color: white; " +
                                    "-fx-border-color: #3498db; " +
                                    "-fx-border-width: 1.5; " +
                                    "-fx-border-radius: 6; " +
                                    "-fx-background-radius: 6; " +
                                    "-fx-padding: 10; " +
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
		    
		    vboxTrai.setPadding(new Insets(30, 25, 0, 25));
		    
		    StackPane stHinhAnh = new StackPane();
		    
		    HBox boxHinhAnh = new  HBox();
		    boxHinhAnh.setPrefHeight(250);
		    boxHinhAnh.setPrefWidth(250);
		    
		    boxHinhAnh.setStyle("""
		    		-fx-background-color: white;       
		    		-fx-background-radius: 10px;       
		    		-fx-border-radius: 10px;          
		    		-fx-border-color: #cccccc;        
		    		-fx-border-width: 1px;
		    		-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 0);
		    		""");
		    
		    
		    
		    ImageView imgView = new ImageView(new javafx.scene.image.Image(mon.getHinhAnh()));
		    imgView.setFitWidth(240);
		    imgView.setFitHeight(240);
		    
		    imgView.setStyle("""     
		    		-fx-background-radius: 10px;       
		    		""");
		    
		    stHinhAnh.getChildren().addAll(boxHinhAnh, imgView);
		    stHinhAnh.setAlignment(Pos.CENTER);
		    
		    Button btnThemAnh = new Button();
		    
		    
		    btnThemAnh.setPrefHeight(40);
		    btnThemAnh.setPrefWidth(40);
		    
		    SVGPath svgIcon = createSvgIcon(24, 24, "black", "M11.47 2.47a.75.75 0 0 1 1.06 0l4.5 4.5a.75.75 0 0 1-1.06 1.06l-3.22-3.22V16.5a.75.75 0 0 1-1.5 0V4.81L8.03 8.03a.75.75 0 0 1-1.06-1.06l4.5-4.5ZM3 15.75a.75.75 0 0 1 .75.75v2.25a1.5 1.5 0 0 0 1.5 1.5h13.5a1.5 1.5 0 0 0 1.5-1.5V16.5a.75.75 0 0 1 1.5 0v2.25a3 3 0 0 1-3 3H5.25a3 3 0 0 1-3-3V16.5a.75.75 0 0 1 .75-.75Z");
		    svgIcon.setStyle("-fx-background-color: black");
		    
		    btnThemAnh.setGraphic(svgIcon);
		    
		    
		    btnThemAnh.setStyle("""
		    		-fx-background-color: white;       
		    		-fx-background-radius: 10px;       
		    		-fx-border-radius: 10px;          
		    		-fx-border-color: #cccccc;        
		    		-fx-border-width: 1px;
		    		-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 0);
		    		-fx-cursor: hand;
		    		""");
		    
		    HBox hboxButton = new HBox();
		    
		    vboxTrai.getChildren().addAll(stHinhAnh, btnThemAnh);
		    vboxTrai.setAlignment(Pos.CENTER);
		    
		    //VBox phải modal
		    
		    VBox vboxPhai = new VBox(5);
		    
		    vboxPhai.setPrefWidth(450);
		    vboxPhai.setPrefHeight(500);
		    
		    Label lblTieuDe = createModernSectionTitle("Thông tin món ăn", "#667EEA");
		    
		    TextField txtMa = new TextField();
		    TextField txtTen = new TextField();
		    TextField txtLoai = new TextField();
		    TextField txtGia = new TextField();
		    
		    HBox hbox1 = createInputField("Mã", txtMa, true);
		    HBox hbox2 = createInputField("Tên", txtTen, true);
		    HBox hbox3 = createInputField("Loại", txtLoai, true);
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
				    -fx-border-width: 1.5; 
				    -fx-font-size: 15px; 
				    -fx-text-fill: #7f8c8d;
		    		""");
		    
		    textArea.setEditable(false);
		    
		    HBox hbox5 = new HBox(88);
		    
		    hbox5.getChildren().addAll(lblMoTa, textArea);
		    
		    vboxPhai.getChildren().addAll(lblTieuDe, hbox1, hbox2, hbox3, hbox4, hbox5);
		    
		    //Set Gias tri
		    txtMa.setText(mon.getMaMonAn());
		    txtTen.setText(mon.getTenMonAn());
		    txtLoai.setText(mon.getLoaiMon());
		    txtGia.setText(mon.getGiaTien() + "");
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
		    });
		    
		    btnSua.setOnAction(e ->{
		    		System.out.println("ĐANG ĐƯỢC X_T HOUSAND PHÁT TRIỂN");
		    });
		    
		    btnThemAnh.setDisable(true);
		    
		    //HBOX button
		    Region spacer2 = new Region();
		    HBox.setHgrow(spacer2, Priority.ALWAYS);
		    
		    HBox hboxBTNDuoi = new HBox(btnQuayVe, spacer2, btnSua);
		    hboxBTNDuoi.setPadding(new Insets(0, 0, 0, 100));
		    
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
}
