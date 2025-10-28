package gui;



import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Thanh SideBar (menu bên trái)
 * Có menu chính + submenu cho mục "Quản lí đặt bàn"
 */
public class SideBar extends VBox {

    private TrangChu trangChu;
    private HBox selectedItem;
    private VBox bookingSubmenu;
    private boolean isBookingMenuExpanded = false;

    private final String[] menuItems = {
            "Dashboard",
            "Quản lí đặt bàn",
            "Quản lí món ăn",
            "Quản lí khách hàng",
            "Quản lí nhân viên",
            "Quản lí khuyến mãi",
            "Quản lí hóa đơn",
            "Thanh toán",
            "Thống kê",
            "Kết ca"
    };

    private final String[] iconNames = {
            "home.png",
            "BanAnIcon.png",
            "food.png",
            "KhachHangIcon.png",
            "employee.png",
            "GiamGiaIcon.png",
            "HoaDonIcon.png",
            "money.png",
            "stats.png",
            "KetCaIcon.png"
    };

    public SideBar(TrangChu trangChu) {
        this.trangChu = trangChu;

        setPrefWidth(250);
        setPadding(new Insets(20, 0, 20, 0));
        getStyleClass().add("sidebar");

        // Logo
        ImageView logoView = createImageView("/img/Logo.png", 50, 50);
        HBox logoContainer = new HBox(logoView);
        logoContainer.setAlignment(Pos.CENTER);
        logoContainer.setPadding(new Insets(0, 0, 20, 0));
        getChildren().add(logoContainer);

        // Thêm các mục menu
        for (int i = 0; i < menuItems.length; i++) {
            String text = menuItems[i];
            HBox menuItem = createMenuItem(text, iconNames[i]);
            getChildren().add(menuItem);

            if (text.equals("Quản lí đặt bàn")) {
                bookingSubmenu = createBookingSubmenu();
                bookingSubmenu.setVisible(false);
                bookingSubmenu.setManaged(false);
                getChildren().add(bookingSubmenu);
            }
        }

        // Spacer
        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        getChildren().add(spacer);

        // Logout
        HBox logoutButton = createLogoutButton();
        getChildren().add(logoutButton);
    }

    // --- TẠO MENU ITEM CHÍNH ---
    private HBox createMenuItem(String text, String iconFileName) {
        ImageView icon = createImageView("/img/" + iconFileName, 20, 20);
        Label label = new Label(text);
        //label.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-color: black");
        label.setPadding(Insets.EMPTY);
        	
        HBox item = new HBox(2, icon, label);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(0, 0, 0, 10));
        item.setPrefWidth(Double.MAX_VALUE);
        item.getStyleClass().add("menu-item");

        item.setOnMouseClicked(e -> handleMenuClick(text, item));
        item.setOnMouseEntered(e -> {
            if (item != selectedItem) item.getStyleClass().add("menu-item-hover");
        });
        item.setOnMouseExited(e -> item.getStyleClass().remove("menu-item-hover"));
        
        return item;
    }

    // --- TẠO SUBMENU CHO QUẢN LÍ ĐẶT BÀN ---
    private VBox createBookingSubmenu() {
        VBox submenu = new VBox();
        submenu.setPadding(new Insets(0, 0, 0, 40));
        submenu.setSpacing(0);

        String[] subItems = {"Đặt bàn", "Đổi bàn", "Hủy bàn", "Check-in"};
        for (String sub : subItems) {
            submenu.getChildren().add(createSubMenuItem(sub));
        }

        return submenu;
    }

    private HBox createSubMenuItem(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #B0B0B0; -fx-font-size: 13px;");

        HBox item = new HBox(label);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(8, 0, 8, 20));
        item.getStyleClass().add("submenu-item");

        item.setOnMouseClicked(e -> handleSubmenuClick(text, item));
        item.setOnMouseEntered(e -> {
            if (item != selectedItem) item.getStyleClass().add("submenu-item-hover");
        });
        item.setOnMouseExited(e -> item.getStyleClass().remove("submenu-item-hover"));

        return item;
    }

    // --- TẠO NÚT ĐĂNG XUẤT ---
    private HBox createLogoutButton() {
        ImageView icon = createImageView("/img/Logout.png", 20, 20);
        Label label = new Label("Đăng xuất");
        label.setStyle("-fx-text-fill: white");

        HBox item = new HBox(10, icon, label);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10, 0, 10, 20));
        item.setPrefWidth(Double.MAX_VALUE);
        item.getStyleClass().add("logout-button");

        item.setOnMouseClicked(e -> handleLogout());
        item.getStyleClass().add("btn-dangXuat");
        
        return item;
    }

    // --- XỬ LÝ SỰ KIỆN ---
    private void handleMenuClick(String menuText, HBox clickedItem) {
        if (selectedItem != null) selectedItem.getStyleClass().remove("selected");
        selectedItem = clickedItem;
        clickedItem.getStyleClass().add("selected");

        // Toggle submenu nếu là "Quản lí đặt bàn"
        if (menuText.equals("Quản lí đặt bàn")) {
            isBookingMenuExpanded = !isBookingMenuExpanded;
            bookingSubmenu.setVisible(isBookingMenuExpanded);
            bookingSubmenu.setManaged(isBookingMenuExpanded);
            return;
        }

        switch (menuText) {
            case "Dashboard":
                trangChu.setMainContent(new Dashboard());
                break;
            case "Quản lí món ăn":
                trangChu.setMainContent(new Label("Giao diện Quản lí món ăn"));
                break;
            case "Thống kê":
                trangChu.setMainContent(new ThongKe());
                break;
            case "Kết ca":
                trangChu.setMainContent(new KetCa());
                break;
            case "Quản lí khách hàng":
                trangChu.setMainContent(new QuanLyKhachHang());
                break;
            case "Quản lí nhân viên":
                trangChu.setMainContent(new QuanLyNhanVien());
                break;
            case "Thanh toán":
                trangChu.setMainContent(new Gui_ThanhToan());
                break;
            case "Quản lí khuyến mãi":
                trangChu.setMainContent(new Gui_QuanLiKhuyenMai());
                break;
            case "Quản lí hóa đơn":
                trangChu.setMainContent(new Gui_QuanLiHoaDon());
                break;
            default:
                trangChu.setMainContent(new Label("Giao diện " + menuText + " chưa triển khai"));
        }
    }

    private void handleSubmenuClick(String subMenuText, HBox clickedItem) {
        if (selectedItem != null) selectedItem.getStyleClass().remove("selected");
        selectedItem = clickedItem;
        clickedItem.getStyleClass().add("selected");

        switch (subMenuText) {
            case "Đặt bàn":
                trangChu.setMainContent(new datban());
                break;
            case "Đổi bàn":
                trangChu.setMainContent(new Gui_DoiBan());
                break;
            case "Hủy bàn":
                trangChu.setMainContent(new Gui_HuyBan());
                break;
            case "Check-in":
                trangChu.setMainContent(new Gui_CheckIn());
                break;
            default:
                trangChu.setMainContent(new Label("Submenu " + subMenuText));
        }
    }

    private void handleLogout() {
        System.out.println("Đăng xuất được click!");
        // TODO: thêm logic đăng xuất (quay lại màn hình đăng nhập)
    }

    // --- TẠO ICON ---
    private ImageView createImageView(String path, double fitWidth, double fitHeight) {
        try {
            Image img = new Image(getClass().getResourceAsStream(path));
            ImageView iv = new ImageView(img);
            iv.setFitWidth(fitWidth);
            iv.setFitHeight(fitHeight);
            return iv;
        } catch (Exception e) {
            System.err.println("Không tìm thấy tài nguyên: " + path);
            return new ImageView();
        }
    }
}