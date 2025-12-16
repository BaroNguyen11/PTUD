
package gui;
import dao.Ca_DAO;
import entity.Ca;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;

/**
 * Thanh SideBar (menu bên trái) - FIXED & OPTIMIZED VERSION
 * - Khắc phục lỗi NullPointerException (scrollbar chưa render)
 * - Dùng Platform.runLater để style scrollbar
 * - Cố định kích thước Sidebar
 */
public class Gui_Sidebar extends VBox {
    private Gui_TrangChu trangChu;
    private HBox selectedItem;
    private Label selectedSub;
    private VBox bookingSubmenu;
    private VBox employeeSubmenu;
    private boolean isBookingMenuExpanded = false;
    private boolean isEmployeeMenuExpanded = false;
    private Gui_KetCa ketCaScreen;
    private final String[] menuItems = {
            "Dashboard",
            "Quản lí đặt bàn",
            "Quản lí món ăn",
            "Quản lí khách hàng",
            "Quản lí nhân viên",
            "Quản lí khuyến mãi",
            "Quản lí hóa đơn",
            "Thống kê",
            "Kết ca"
    };

    private final String[] iconNames = {
            "home.png",
            "booking.png",
            "food.png",
            "customer.png",
            "employee.png",
            "promotion.png",
            "invoice.png",
            "stats.png",
            "settlement.png",
            "settings.png"
    };
    private final List<String> adminOnlyItems = Arrays.asList(
            "Quản lí món ăn",
            "Quản lí nhân viên",
            "Quản lí khuyến mãi",
            "Thống kê"
    );
    public Gui_Sidebar(Gui_TrangChu trangChu) {
        this.trangChu = trangChu;
        boolean isAdmin = Gui_DangNhap.isCurrentUserAdmin();
        // ✅ Cố định kích thước Sidebar
        this.setPrefWidth(220);
        this.setMinWidth(220);
        this.setMaxWidth(220);
        this.setStyle("-fx-background-color: #082744;");

        // ===== Container cho menu =====
        VBox menuContainer = new VBox(2);
        menuContainer.setPadding(new Insets(10, 0, 5, 0));
        menuContainer.setStyle("-fx-background-color: #082744;");

        // ===== Logo =====
        ImageView logoView = createImageView("/img/Logo.png", 40, 40);
        HBox logoContainer = new HBox(logoView);
        logoContainer.setAlignment(Pos.CENTER);
        logoContainer.setPadding(new Insets(0, 0, 10, 0));
        menuContainer.getChildren().add(logoContainer);

        // ===== Các mục menu =====
        for (int i = 0; i < menuItems.length; i++) {
            String text = menuItems[i];
            if (!isAdmin && adminOnlyItems.contains(text)) {
                continue;
            }
            HBox menuItem = createMenuItem(text, iconNames[i]);
            menuContainer.getChildren().add(menuItem);

            if (text.equals("Quản lí đặt bàn")) {
                bookingSubmenu = createBookingSubmenu();
                bookingSubmenu.setVisible(false);
                bookingSubmenu.setManaged(false);
                menuContainer.getChildren().add(bookingSubmenu);
            }
            
            if (text.equals("Quản lí nhân viên")) {
                employeeSubmenu = createEmployeeSubmenu();
                employeeSubmenu.setVisible(false);
                employeeSubmenu.setManaged(false);
                menuContainer.getChildren().add(employeeSubmenu);
            }
        }

        // Spacer đẩy logout xuống cuối
        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        menuContainer.getChildren().add(spacer);

        // Nút logout
        HBox logoutButton = createLogoutButton();
        menuContainer.getChildren().add(logoutButton);

        // ===== ScrollPane bọc menu =====
        ScrollPane scrollPane = new ScrollPane(menuContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle(
                "-fx-background-color: #082744;" +
                        "-fx-background: #082744;" +
                        "-fx-border-width: 0;"
        );

        // ✅ Fix lỗi NullPointerException: dùng Platform.runLater
        Platform.runLater(() -> {
            var verticalBar = scrollPane.lookup(".scroll-bar:vertical");
            if (verticalBar != null) {
                verticalBar.setStyle(
                        "-fx-background-color: #082744;" +
                                "-fx-pref-width: 8px;"
                );
            }
        });

        // Thêm ScrollPane vào Sidebar
        this.getChildren().add(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Load CSS (nếu có)
        try {
            this.getStylesheets().add(
                    getClass().getResource("/application/application.css").toExternalForm()
            );
        } catch (Exception e) {
            System.err.println("⚠️ Không load được CSS: " + e.getMessage());
        }
    }

    // --- Tạo menu item chính ---
    private HBox createMenuItem(String text, String iconFileName) {
        ImageView icon = createImageView("/img/icons/" + iconFileName, 18, 18);

        Label label = new Label(text);
        label.setStyle("-fx-font-size: 15px; -fx-text-fill: white; -fx-font-weight: bold");

        HBox item = new HBox(5, icon, label);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(4, 0, 4, 15));
        item.setPrefWidth(Double.MAX_VALUE);
        item.getStyleClass().add("menu-item");

        // Style mặc định
        item.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

        // Click event
        item.setOnMouseClicked(e -> handleMenuClick(text, item));

        // Hover effect
        item.setOnMouseEntered(e -> {
            if (item != selectedItem) {
                item.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-cursor: hand;");
            }
        });
        item.setOnMouseExited(e -> {
            if (item != selectedItem) {
                item.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
            }
        });

        return item;
    }

    // --- Submenu cho "Quản lí đặt bàn" ---
    private VBox createBookingSubmenu() {
        VBox submenu = new VBox(0);
        submenu.setPadding(new Insets(0, 0, 0, 10));
//        submenu.setStyle("-fx-background-color: rgba(0,0,0,0.2);");

        int index = 0;
        String[] subItems = {"Danh Sách Bàn", "Quản Lí Bàn"};
        for (String sub : subItems) {
            submenu.getChildren().add(createSubMenuItem(sub, index ));
            index++;
        }

        return submenu;
    }
    
 // --- Submenu cho "Quản lí đặt bàn" ---
    private VBox createEmployeeSubmenu() {
        VBox submenu = new VBox(0);
        submenu.setPadding(new Insets(0, 0, 0, 10));
//        submenu.setStyle("-fx-background-color: rgba(0,0,0,0.2);");

        int index = 0;
        String[] subItems = {"Danh Sách Nhân Viên", "Danh Sách Tài Khoản"};
        for (String sub : subItems) {
            submenu.getChildren().add(createSubMenuItem(sub, index ));
            index++;
        }

        return submenu;
    }
    
    private HBox createSubMenuItem(String text, int index) {
      Label label = new Label(text);
      label.setStyle("-fx-text-fill: #CCCCCC; -fx-font-size: 15px; -fx-font-weight: bold; -fx-padding: 8 5 8 5");
      label.setPrefWidth(160);
      
      ImageView icon = null;
      
      
      if(index == 0){
    	  		icon = createImageView("/img/icons/submenuicon2.png", 15, 33);
    	  		icon.setStyle("-fx-translate-y: -7px;");
      }else {
    	  		icon = createImageView("/img/icons/submenuicon.png", 24, 50);
	  		icon.setStyle("-fx-translate-y: -23px;");
      }
      
      HBox item = new HBox(5, icon, label);
      item.setAlignment(Pos.CENTER_LEFT);
      item.setPadding(new Insets(6, 0, 6, 10));
      item.getStyleClass().add("submenu-item");
      item.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
     
      item.setPrefHeight(40);
      item.setMinHeight(40);
      item.setMaxHeight(40);
      item.setPickOnBounds(false);

      item.setOnMouseClicked(e -> handleSubmenuClick(text, item ,label));
      item.setOnMouseEntered(e -> item.setStyle("-fx-background-color: rgba(255,255,255,0.05);"));
      item.setOnMouseExited(e -> item.setStyle("-fx-background-color: transparent;"));

      return item;
  }
    
    
//    private HBox createSubMenuItem(String text) {
//        Label label = new Label(text);
//        label.setStyle("-fx-text-fill: #CCCCCC; -fx-font-size: 12px;");
//
//        ImageView icon = createImageView("/img/icons/submenuicon.png", 24, 40);
//        
//        icon.setStyle("-fx-translate-y: -15px;");
//        
//        HBox item = new HBox(5, icon, label);
//        item.setAlignment(Pos.CENTER_LEFT);
//        item.setPadding(new Insets(6, 0, 6, 15));
//        item.getStyleClass().add("submenu-item");
//        item.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
//
//        item.setOnMouseClicked(e -> handleSubmenuClick(text, item));
//        item.setOnMouseEntered(e -> item.setStyle("-fx-background-color: rgba(255,255,255,0.05);"));
//        item.setOnMouseExited(e -> item.setStyle("-fx-background-color: transparent;"));
//
//        return item;
//    }

    // --- Nút Logout ---
    private HBox createLogoutButton() {
        ImageView icon = createImageView("/img/icons/logout.png", 18, 18);
        Label label = new Label("Đăng xuất");
        label.setStyle("-fx-font-size: 13px; -fx-text-fill: white;");

        HBox item = new HBox(8, icon, label);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10, 0, 10, 15));
        item.setPrefWidth(Double.MAX_VALUE);
        item.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

        item.setOnMouseEntered(e -> item.setStyle("-fx-background-color: rgba(220,53,69,0.8);"));
        item.setOnMouseExited(e -> item.setStyle("-fx-background-color: transparent;"));
        item.setOnMouseClicked(e -> handleLogout());

        return item;
    }

    // --- Xử lý menu click ---
    private void handleMenuClick(String menuText, HBox clickedItem) {
        if (selectedItem != null)
            selectedItem.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        
        if (selectedSub != null)
    		selectedSub.setStyle("-fx-text-fill: #CCCCCC; -fx-font-size: 15px; -fx-font-weight: bold; -fx-padding: 8 0 8 5; -fx-background-color: transparent; -fx-cursor: hand");

        if (menuText.equals("Quản lí đặt bàn")) {
            toggleBookingSubmenuWithAnimation();
            isEmployeeMenuExpanded = true;
            toggleEmployeeSubmenuWithAnimation();
            if (isBookingMenuExpanded) {
                clickedItem.setStyle("-fx-background-color: rgba(255,255,255,0.15);");
            }
            return;
        }
        
        if (menuText.equals("Quản lí nhân viên")) {
            toggleEmployeeSubmenuWithAnimation();
            isBookingMenuExpanded = true; 
            toggleBookingSubmenuWithAnimation();
            if (isEmployeeMenuExpanded) {
                clickedItem.setStyle("-fx-background-color: rgba(255,255,255,0.15);");
            }
            return;
        }

        selectedItem = clickedItem;
        clickedItem.setStyle("-fx-background-color: rgba(255,255,255,0.15);");

        if (isBookingMenuExpanded) toggleBookingSubmenuWithAnimation();
        if (isEmployeeMenuExpanded) toggleEmployeeSubmenuWithAnimation();

        switch (menuText) {
            case "Dashboard" -> trangChu.setMainContent(new Gui_Dashboard());
            case "Quản lí món ăn" -> trangChu.setMainContent(new Gui_QLMon());
            case "Thống kê" -> trangChu.setMainContent(new Gui_ThongKe());
            case "Kết ca" -> {
                if (ketCaScreen == null) {
                    // Lần đầu tiên bấm: Tạo mới (Dữ liệu sẽ tự load trong Constructor)
                    ketCaScreen = new Gui_KetCa();
                } else {
                    // Những lần sau: Gọi hàm refresh để cập nhật số liệu mới nhất
                    ketCaScreen.loadDuLieuCa();
                }
                trangChu.setMainContent(ketCaScreen);
            }
            case "Quản lí khách hàng" -> trangChu.setMainContent(new Gui_QuanLiKhachHang());
            case "Quản lí khuyến mãi" -> trangChu.setMainContent(new Gui_QuanLiKhuyenMai());
            case "Quản lí hóa đơn" -> trangChu.setMainContent(new Gui_QuanLiHoaDon());
            default -> trangChu.setMainContent(new Label("Giao diện " + menuText + " chưa triển khai"));
        }
    }

    private void handleSubmenuClick(String subMenuText, HBox clickedItem, Label label) {
        if (selectedItem != null)
            selectedItem.setStyle("-fx-background-color: transparent; -fx-cursor: hand");
        
        if (selectedSub != null)
        		selectedSub.setStyle("-fx-text-fill: #CCCCCC; -fx-font-size: 15px; -fx-font-weight: bold; -fx-padding: 8 0 8 5; -fx-background-color: transparent; -fx-cursor: hand");

        selectedItem = clickedItem;
        clickedItem.setStyle("-fx-background-color: rgba(255,255,255,0.15);");
        
        selectedSub = label;
        label.setStyle("-fx-text-fill: #CCCCCC; -fx-font-size: 15px; -fx-font-weight: bold; -fx-padding: 8 5 8 5; -fx-background-color: #424873; -fx-background-radius: 6px");

        
        switch (subMenuText) {
            case "Danh Sách Bàn" -> trangChu.setMainContent(new Gui_DanhSachBan(trangChu));
            case "Quản Lí Bàn" -> trangChu.setMainContent(new Gui_QuanLyBan());
            case "Danh Sách Nhân Viên" -> trangChu.setMainContent(new Gui_QuanLiNhanVien());
            case "Danh Sách Tài Khoản" -> trangChu.setMainContent(new Gui_QuanLiTaiKhoan());
            default -> trangChu.setMainContent(new Label("Submenu " + subMenuText));
        }
    }
    
    // --- Animation submenu ---
    private void toggleEmployeeSubmenuWithAnimation() {
        isEmployeeMenuExpanded = !isEmployeeMenuExpanded;

        if (isEmployeeMenuExpanded) {
            employeeSubmenu.setVisible(true);
            employeeSubmenu.setManaged(true);
            employeeSubmenu.setOpacity(0);

            FadeTransition fade = new FadeTransition(Duration.millis(250), employeeSubmenu);
            fade.setFromValue(0.0);
            fade.setToValue(1.0);
            fade.play();
        } else {
            FadeTransition fade = new FadeTransition(Duration.millis(200), employeeSubmenu);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(e -> {
	            	employeeSubmenu.setVisible(false);
	            	employeeSubmenu.setManaged(false);
            });
            fade.play();
        }
    }

    // --- Animation submenu ---
    private void toggleBookingSubmenuWithAnimation() {
        isBookingMenuExpanded = !isBookingMenuExpanded;

        if (isBookingMenuExpanded) {
            bookingSubmenu.setVisible(true);
            bookingSubmenu.setManaged(true);
            bookingSubmenu.setOpacity(0);

            FadeTransition fade = new FadeTransition(Duration.millis(250), bookingSubmenu);
            fade.setFromValue(0.0);
            fade.setToValue(1.0);
            fade.play();
        } else {
            FadeTransition fade = new FadeTransition(Duration.millis(200), bookingSubmenu);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(e -> {
                bookingSubmenu.setVisible(false);
                bookingSubmenu.setManaged(false);
            });
            fade.play();
        }
    }


    // --- Tạo icon ---
    private ImageView createImageView(String path, double fitWidth, double fitHeight) {
        try {
            Image img = new Image(getClass().getResourceAsStream(path));
            ImageView iv = new ImageView(img);
            iv.setFitWidth(fitWidth);
            iv.setFitHeight(fitHeight);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);
            return iv;
        } catch (Exception e) {
            System.err.println("⚠️ Không tìm thấy icon: " + path);
            ImageView iv = new ImageView();
            iv.setFitWidth(fitWidth);
            iv.setFitHeight(fitHeight);
            return iv;
        }
    }
    
 // --- Xử lý đăng xuất ---
    private void handleLogout() {
        // Hiển thị confirm
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Đăng xuất");
        alert.setHeaderText("Bạn có chắc muốn đăng xuất?");
        alert.setContentText("Tất cả dữ liệu sẽ được xóa.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                // 1. Lấy mã nhân viên hiện tại
                String maNhanVienHienTai = trangChu.nhanVienDangDung.getMaNhanVien();
                if (maNhanVienHienTai == null || maNhanVienHienTai.isEmpty()) {
                    // Nếu không có thông tin đăng nhập (lạ), cứ cho đóng
                    performLogout();
                    return;
                }

                // 2. Kiểm tra xem nhân viên này còn ca đang làm không
                Ca_DAO caDAO = new Ca_DAO();
                Ca caDangLam = caDAO.getCaDangLam(maNhanVienHienTai);

                if (caDangLam != null) {
                    // 3. NẾU CÒN CA -> KHÔNG CHO ĐÓNG, cảnh báo
                    Alert alert2 = new Alert(Alert.AlertType.WARNING);
                    alert2.setTitle("Chưa kết ca");
                    alert2.setHeaderText("Bạn chưa kết ca làm việc!");
                    alert2.setContentText("Vui lòng vào mục 'Kết Ca' để hoàn tất ca của bạn trước khi đăng xuất.");
                    alert2.showAndWait();
                    return;  // Không đăng xuất
                } else {
                    // 4. NẾU ĐÃ KẾT CA -> CHO ĐĂNG XUẤT BÌNH THƯỜNG
                    performLogout();
                }

            } catch (Exception e) {
                System.err.println("Lỗi kiểm tra ca: " + e.getMessage());
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Lỗi");
                errorAlert.setHeaderText("Lỗi khi kiểm tra ca làm việc");
                errorAlert.setContentText("Không thể xác minh trạng thái ca: " + e.getMessage());
                errorAlert.showAndWait();
                return;  // Không đăng xuất nếu lỗi
            }
        }
    }

    // --- Hàm phụ: Thực hiện đăng xuất (đóng stage & mở login) ---
    private void performLogout() {
        try {
            // Đóng stage hiện tại
            Stage currentStage = (Stage) trangChu.getScene().getWindow();
            currentStage.close();

            // Tạo stage MỚI cho login
            Stage loginStage = new Stage();
            loginStage.setTitle("Đăng nhập");

            // Fix phóng to: Set kích thước cố định
            loginStage.setResizable(false);
            loginStage.setMinWidth(900);
            loginStage.setMaxWidth(900);
            loginStage.setMinHeight(550);
            loginStage.setMaxHeight(550);

            Gui_DangNhap loginScreen = new Gui_DangNhap();
            loginScreen.start(loginStage);

        } catch (Exception e) {
            System.err.println("Lỗi mở login: " + e.getMessage());
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Lỗi hệ thống!");
            errorAlert.showAndWait();
        }
    }
}
