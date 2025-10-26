
package gui;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;

public class SideBar extends VBox {
    
    private TrangChu trangChu; 
    private HBox selectedItem; 

    // Danh sách các mục menu chính (Bao gồm Thống kê và Kết ca)
    private final String[] menuItems = {
        "Màn hình chính",
        "Quản lí đặt bàn",
        "Quản lí món ăn",
        "Quản lí khách hàng",
        "Quản lí nhân viên",
        "Quản lí khuyến mãi",
        "Quản lí hóa đơn",
        "Thống kê", // Mục chính
        "Kết ca"    // Mục riêng biệt, ngang hàng
    };
    
    // Tên file icon (Cần 9 icon tương ứng với 9 mục)
    private final String[] iconNames = {
        "home.png", 
        "booking.png", 
        "food.png", 
        "customer.png", 
        "employee.png", 
        "promotion.png", 
        "invoice.png", 
        "stats.png", // Icon cho Thống kê
        "settlement.png" // Icon cho Kết ca (Giả sử bạn có icon này)
    };

    public SideBar(TrangChu trangChu) {
        this.trangChu = trangChu; 

        // Cấu hình VBox chính (SideBar)
        this.setPrefWidth(250);
        this.getStyleClass().add("sidebar");
        this.setSpacing(5);
        this.setPadding(new Insets(20, 0, 20, 0));

        // 1. Logo
        ImageView logoView = createImageView("/img/Logo.png", 50, 50); 
        HBox logoContainer = new HBox(logoView);
        logoContainer.setAlignment(Pos.CENTER);
        logoContainer.setPadding(new Insets(0, 0, 20, 0));
        this.getChildren().add(logoContainer);

        // 2. Các mục Menu chính
        for (int i = 0; i < menuItems.length; i++) {
            HBox menuItemBox = createMenuItem(menuItems[i], iconNames[i]);
            
            // Xử lý mục "Quản lí đặt bàn" (Giả định có submenu)
            if (menuItems[i].equals("Quản lí đặt bàn")) {
                 this.getChildren().add(createBookingMenu(menuItemBox)); 
            } else {
                 this.getChildren().add(menuItemBox);
            }
            
            // Đánh dấu "Màn hình chính" là mục được chọn ban đầu
            if (menuItems[i].equals("Màn hình chính")) {
                selectedItem = menuItemBox;
                menuItemBox.getStyleClass().add("selected");
            }
        }
        
        // 3. Spacer và Đăng Xuất
        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        this.getChildren().add(spacer);

        HBox logoutButton = createLogoutButton();
        this.getChildren().add(logoutButton);
    }
    
    /**
     * Tạo HBox chứa Icon và Label cho một mục menu thông thường.
     */
    private HBox createMenuItem(String text, String iconFileName) {
        ImageView icon = createImageView("/icons/" + iconFileName, 20, 20);
        Label label = new Label(text);
        
        HBox item = new HBox(10, icon, label);
        item.getStyleClass().add("menu-item");
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10, 0, 10, 20));
        item.setPrefWidth(Double.MAX_VALUE);
        item.setId(text.toLowerCase().replaceAll("[^a-z0-9]", "-"));
        
        // THÊM SỰ KIỆN CLICK
        item.setOnMouseClicked(event -> handleMenuClick(text, item));
        
        // Thêm hiệu ứng hover
        item.setOnMouseEntered(e -> {
            if (item != selectedItem) {
                item.getStyleClass().add("menu-item-hover");
            }
        });
        item.setOnMouseExited(e -> item.getStyleClass().remove("menu-item-hover"));
        
        return item;
    }

    /**
     * Tạo cấu trúc cho mục "Quản lí đặt bàn". 
     * (Chỉ là mẫu, có thể mở rộng bằng VBox/HBox con.)
     */
    private VBox createBookingMenu(HBox parentItem) {
         VBox fullMenu = new VBox(parentItem);
         fullMenu.setSpacing(0);
         fullMenu.getStyleClass().add("booking-menu-group");
         return fullMenu;
    }

    /** Tạo nút Đăng Xuất */
    private HBox createLogoutButton() {
        ImageView icon = createImageView("/icons/logout.png", 20, 20);
        Label label = new Label("Đăng xuất");
        
        HBox item = new HBox(10, icon, label);
        item.getStyleClass().add("logout-button");
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10, 0, 10, 20));
        item.setPrefWidth(Double.MAX_VALUE);
        
        item.setOnMouseClicked(event -> handleLogout());
        
        return item;
    }

    /** Hàm tiện ích để tạo ImageView */
    private ImageView createImageView(String path, double fitWidth, double fitHeight) {
        try {
            Image image = new Image(getClass().getResourceAsStream(path));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(fitWidth);
            imageView.setFitHeight(fitHeight);
            return imageView;
        } catch (Exception e) {
            System.err.println("Không tìm thấy tài nguyên: " + path);
            return new ImageView(); 
        }
    }
    
    // --- XỬ LÝ SỰ KIỆN ---
    
    /**
     * Xử lý sự kiện khi click vào mục menu
     */
    private void handleMenuClick(String menuText, HBox clickedItem) {
        // Bỏ chọn mục cũ
        if (selectedItem != null) {
            selectedItem.getStyleClass().remove("selected");
        }
        
        // Chọn mục mới
        selectedItem = clickedItem;
        clickedItem.getStyleClass().add("selected");
        
        // Chuyển giao diện
        switch (menuText) {
            case "Màn hình chính":
                trangChu.showDashboard();
                break;
            case "Quản lí đặt bàn":
                trangChu.setMainContent(new Label("Giao diện Quản lí đặt bàn"));
                break;
            case "Quản lí món ăn":
                trangChu.setMainContent(new Label("Giao diện Quản lí món ăn"));
                break;
            case "Thống kê":
                trangChu.setMainContent(new Label("Giao diện Thống kê"));
                break;
            case "Kết ca":
                // Tích hợp giao diện KetCaMoi
                trangChu.setMainContent(new KetCa()); 
                break;
            default:
                trangChu.setMainContent(new Label("Giao diện " + menuText + " chưa triển khai"));
        }
    }
    
    /**
     * Xử lý sự kiện đăng xuất
     */
    private void handleLogout() {
        System.out.println("Đăng xuất được click!");
        // Thêm logic đăng xuất ở đây
    }
}