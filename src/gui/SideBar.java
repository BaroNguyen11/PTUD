package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class SideBar extends VBox {
    
    private TrangChu trangChu; // Tham chiếu đến TrangChu để gọi phương thức chuyển giao diện
    private HBox selectedItem; // Lưu mục đang được chọn

    // Danh sách các mục menu chính
    private final String[] menuItems = {
        "Màn hình chính",
        "Quản lí đặt bàn",
        "Quản lí món ăn",
        "Quản lí khách hàng",
        "Quản lí nhân viên",
        "Quản lí khuyến mãi",
        "Quản lí hóa đơn",
        "Thống kê"
    };

    // Danh sách các mục con của "Quản lí đặt bàn"
    private final String[] subMenuItems = {
        "Đặt bàn",
        "Đổi bàn",
        "Hủy bàn",
        "Check-in"
    };
    
    // Tên file icon
    private final String[] iconNames = {
        "home.png", 
        "booking.png", 
        "food.png", 
        "customer.png", 
        "employee.png", 
        "promotion.png", 
        "invoice.png", 
        "stats.png"
    };

    public SideBar(TrangChu trangChu) {
        this.trangChu = trangChu; // Lưu tham chiếu

        // Cấu hình VBox chính (SideBar)
        this.setPrefWidth(250);
        this.setMinWidth(250);
        this.setMaxWidth(250);

        this.getStyleClass().add("sidebar");
        this.setSpacing(5);
        this.setPadding(new Insets(20, 0, 20, 0));

        // 1. Thêm Logo
        ImageView logoView = createImageView("/img/Logo.png", 50, 50); 
        HBox logoContainer = new HBox(logoView);
        logoContainer.setAlignment(Pos.CENTER);
        logoContainer.setPadding(new Insets(0, 0, 20, 0));
        this.getChildren().add(logoContainer);

        // 2. Thêm các mục Menu chính
        for (int i = 0; i < menuItems.length; i++) {
            if (menuItems[i].equals("Quản lí đặt bàn")) {
                // Xử lý mục "Quản lí đặt bàn" đặc biệt (có submenu)
                VBox bookingMenu = createBookingMenu(iconNames[i]);
                this.getChildren().add(bookingMenu);
            } else {
                // Tạo mục menu thông thường
                HBox menuItemBox = createMenuItem(menuItems[i], iconNames[i]);
                this.getChildren().add(menuItemBox);
                
                // Đánh dấu "Màn hình chính" là mục được chọn ban đầu
                if (menuItems[i].equals("Màn hình chính")) {
                    selectedItem = menuItemBox;
                    menuItemBox.getStyleClass().add("selected");
                }
            }
        }
        
        // Thêm khoảng trống
        VBox spacer = new VBox();
        VBox.setVgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        this.getChildren().add(spacer);

        // 3. Thêm nút Đăng Xuất
        HBox logoutButton = createLogoutButton();
        this.getChildren().add(logoutButton);
    }

    /** Tạo HBox chứa Icon và Label cho một mục menu thông thường. */
    private HBox createMenuItem(String text, String iconFileName) {
        ImageView icon = createImageView("/icons/" + iconFileName, 20, 20);
        Label label = new Label(text);
        
        HBox item = new HBox(10, icon, label);
        item.getStyleClass().add("menu-item");
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10, 0, 10, 20));
        item.setPrefWidth(Double.MAX_VALUE);
        item.setId(text.toLowerCase().replaceAll(" ", "-")); 
        
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

    /** Tạo cấu trúc cho mục "Quản lí đặt bàn" và các mục con. */
    private VBox createBookingMenu(String iconFileName) {
        // Mục chính
        HBox parentItem = createMenuItem("Quản lí đặt bàn", iconFileName);
        parentItem.getStyleClass().add("menu-item-parent");

        // Các mục con
        VBox subMenuContainer = new VBox();
        subMenuContainer.getStyleClass().add("sub-menu-container");

        VBox fullMenu = new VBox(parentItem, subMenuContainer);
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
        
        // Thêm sự kiện logout
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
        
        // Chuyển giao diện dựa trên tên menu
        switch (menuText) {
            case "Màn hình chính":
                trangChu.showDashboard();
                break;
            case "Quản lí đặt bàn":
                trangChu.setMainContent(new Gui_DanhSachBan());
                break;
            case "Quản lí món ăn":
                trangChu.setMainContent(new Label("Giao diện Quản lí món ăn - Coming soon"));
                break;
            case "Quản lí khách hàng":
                trangChu.setMainContent(new Label("Giao diện Quản lí khách hàng - Coming soon"));
                break;
            case "Quản lí nhân viên":
                trangChu.setMainContent(new Label("Giao diện Quản lí nhân viên - Coming soon"));
                break;
            case "Quản lí khuyến mãi":
                trangChu.setMainContent(new Label("Giao diện Quản lí khuyến mãi - Coming soon"));
                break;
            case "Quản lí hóa đơn":
                trangChu.setMainContent(new Label("Giao diện Quản lí hóa đơn - Coming soon"));
                break;
            case "Thống kê":
                // Giả sử bạn đã có lớp ThongKe như trong document
                trangChu.setMainContent(new Label("Giao diện Thống kê - Coming soon"));
                break;
            default:
                trangChu.setMainContent(new Label("Giao diện chưa được triển khai"));
        }
    }
    
    /**
     * Xử lý sự kiện đăng xuất
     */
    private void handleLogout() {
        System.out.println("Đăng xuất được click!");
        // TODO: Thêm logic đăng xuất ở đây
        // Ví dụ: Hiển thị dialog xác nhận, sau đó chuyển về màn hình đăng nhập
    }
}