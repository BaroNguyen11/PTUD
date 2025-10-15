package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

//import org.kordamp.ikonli.javafx.FontIcon;

public class SideBar extends VBox {

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
    
    // Tên file icon (ví dụ: home.png). Bạn sẽ thêm icon vào thư mục 'resources/icons'
    // Lưu ý: Bạn cần thay thế bằng tên file icon thực tế.
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


    public SideBar() {
        // Cấu hình VBox chính (SideBar)
        this.setPrefWidth(250); // Chiều rộng cố định
        this.getStyleClass().add("sidebar"); // Class CSS cho VBox chính
        this.setSpacing(5); // Khoảng cách giữa các mục menu
        this.setPadding(new Insets(20, 0, 20, 0)); // Padding trên và dưới
//        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        // 1. Thêm Logo
        // Tạo một ImageView cho Logo (Bạn cần đảm bảo file Logo.png nằm trong classpath)
        ImageView logoView = createImageView("/img/Logo.png", 50, 50); 
        HBox logoContainer = new HBox(logoView);
        logoContainer.setAlignment(Pos.CENTER);
        logoContainer.setPadding(new Insets(0, 0, 20, 0)); // Khoảng cách sau logo
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
            }
        }
        
        // Thêm khoảng trống giữa Menu và nút Đăng Xuất (Sử dụng Region/Spacer nếu cần thiết, 
        // nhưng tạm thời dùng VBox để dễ quản lý)
        VBox spacer = new VBox();
        VBox.setVgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        this.getChildren().add(spacer);

        // 3. Thêm nút Đăng Xuất
        HBox logoutButton = createLogoutButton();
        this.getChildren().add(logoutButton);
    }

    /** Tạo HBox chứa Icon và Label cho một mục menu thông thường. */
    private HBox createMenuItem(String text, String iconFileName) {
        ImageView icon = createImageView("/icons/" + iconFileName, 20, 20); // Giả định icon nằm trong /icons/
        Label label = new Label(text);
        
        HBox item = new HBox(10, icon, label); // Khoảng cách 10 giữa icon và text
        item.getStyleClass().add("menu-item");
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10, 0, 10, 20)); // Padding bên trái để icon/text không sát mép
        item.setPrefWidth(Double.MAX_VALUE); // Choán hết chiều rộng VBox
        
        // Thêm một ID nếu bạn muốn tô màu mục đang chọn
        item.setId(text.toLowerCase().replaceAll(" ", "-")); 
        
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
        
//        for (String subItemText : subMenuItems) {
//            Label subLabel = new Label(subItemText);
//            
//            // HBox chỉ chứa Label, dùng cho hiệu ứng lề
//            HBox subItemBox = new HBox(subLabel);
//            subItemBox.getStyleClass().add("sub-menu-item");
//            
//            // Mục "Hủy bàn" được tô màu làm ví dụ
//            if (subItemText.equals("Hủy bàn")) {
//                subItemBox.getStyleClass().add("selected");
//            }
//            
//            // Thiết lập lề cho mục con
//            subItemBox.setPadding(new Insets(5, 0, 5, 50)); // Lề sâu hơn mục cha
//            
//            subMenuContainer.getChildren().add(subItemBox);
//        }

        VBox fullMenu = new VBox(parentItem, subMenuContainer);
        fullMenu.getStyleClass().add("booking-menu-group");
        return fullMenu;
    }

    /** Tạo nút Đăng Xuất */
    private HBox createLogoutButton() {
        ImageView icon = createImageView("/icons/logout.png", 20, 20); // Giả định icon logout
        Label label = new Label("Đăng xuất");
        
        HBox item = new HBox(10, icon, label);
        item.getStyleClass().add("logout-button"); // Class CSS riêng cho nút Đăng Xuất
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10, 0, 10, 20));
        item.setPrefWidth(Double.MAX_VALUE);
        
        return item;
    }

    /** Hàm tiện ích để tạo ImageView */
    private ImageView createImageView(String path, double fitWidth, double fitHeight) {
        try {
            // Sử dụng getClass().getResourceAsStream() để tải tài nguyên từ classpath
            Image image = new Image(getClass().getResourceAsStream(path));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(fitWidth);
            imageView.setFitHeight(fitHeight);
            return imageView;
        } catch (Exception e) {
            System.err.println("Không tìm thấy tài nguyên: " + path);
            // Trả về một ImageView rỗng nếu không tìm thấy
            return new ImageView(); 
        }
    }
}