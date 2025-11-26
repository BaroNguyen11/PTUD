
package gui;

import entity.NhanVien;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Thanh SideBar (menu bên trái) - FIXED & OPTIMIZED VERSION
 * - Khắc phục lỗi NullPointerException (scrollbar chưa render)
 * - Dùng Platform.runLater để style scrollbar
 * - Cố định kích thước Sidebar
 */
public class SideBar extends VBox {
    private TrangChu trangChu;
    private HBox selectedItem;
    private VBox bookingSubmenu;
    private boolean isBookingMenuExpanded = false;
    private KetCa ketCaScreen;
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
            "booking.png",
            "food.png",
            "customer.png",
            "employee.png",
            "promotion.png",
            "invoice.png",
            "payment.png",
            "stats.png",
            "settlement.png"
    };

    public SideBar(TrangChu trangChu) {
        this.trangChu = trangChu;

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
            HBox menuItem = createMenuItem(text, iconNames[i]);
            menuContainer.getChildren().add(menuItem);

            if (text.equals("Quản lí đặt bàn")) {
                bookingSubmenu = createBookingSubmenu();
                bookingSubmenu.setVisible(false);
                bookingSubmenu.setManaged(false);
                menuContainer.getChildren().add(bookingSubmenu);
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
        ImageView icon = createImageView("/icons/" + iconFileName, 18, 18);

        Label label = new Label(text);
        label.setStyle("-fx-font-size: 13px; -fx-text-fill: white;");

        HBox item = new HBox(8, icon, label);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(8, 0, 8, 15));
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
        submenu.setPadding(new Insets(0, 0, 0, 30));
        submenu.setStyle("-fx-background-color: rgba(0,0,0,0.2);");

        String[] subItems = {"Đặt bàn", "Đổi bàn", "Hủy bàn", "Check-in"};
        for (String sub : subItems) {
            submenu.getChildren().add(createSubMenuItem(sub));
        }

        return submenu;
    }

    private HBox createSubMenuItem(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #CCCCCC; -fx-font-size: 12px;");

        HBox item = new HBox(label);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(6, 0, 6, 15));
        item.getStyleClass().add("submenu-item");
        item.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

        item.setOnMouseClicked(e -> handleSubmenuClick(text, item));
        item.setOnMouseEntered(e -> item.setStyle("-fx-background-color: rgba(255,255,255,0.05);"));
        item.setOnMouseExited(e -> item.setStyle("-fx-background-color: transparent;"));

        return item;
    }

    // --- Nút Logout ---
    private HBox createLogoutButton() {
        ImageView icon = createImageView("/icons/logout.png", 18, 18);
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

        if (menuText.equals("Quản lí đặt bàn")) {
            toggleBookingSubmenuWithAnimation();
            if (isBookingMenuExpanded) {
                clickedItem.setStyle("-fx-background-color: rgba(255,255,255,0.15);");
            }
            return;
        }

        selectedItem = clickedItem;
        clickedItem.setStyle("-fx-background-color: rgba(255,255,255,0.15);");

        if (isBookingMenuExpanded) toggleBookingSubmenuWithAnimation();

        switch (menuText) {
            case "Dashboard" -> trangChu.setMainContent(new Dashboard());
            case "Quản lí món ăn" -> trangChu.setMainContent(new Label("Giao diện Quản lí món ăn"));
            case "Thống kê" -> trangChu.setMainContent(new ThongKe());
            case "Kết ca" -> {
                if (ketCaScreen == null) {
                    // Lần đầu tiên bấm: Tạo mới (Dữ liệu sẽ tự load trong Constructor)
                    ketCaScreen = new KetCa();
                } else {
                    // Những lần sau: Gọi hàm refresh để cập nhật số liệu mới nhất
                    ketCaScreen.loadDuLieuCa();
                }
                trangChu.setMainContent(ketCaScreen);
            }
            case "Quản lí khách hàng" -> trangChu.setMainContent(new QuanLyKhachHang());
            case "Quản lí nhân viên" -> trangChu.setMainContent(new QuanLyNhanVien());
            case "Thanh toán" -> trangChu.setMainContent(new Gui_ThanhToan(new NhanVien()));
            case "Quản lí khuyến mãi" -> trangChu.setMainContent(new Gui_QuanLiKhuyenMai());
            case "Quản lí hóa đơn" -> trangChu.setMainContent(new Gui_QuanLiHoaDon());
            default -> trangChu.setMainContent(new Label("Giao diện " + menuText + " chưa triển khai"));
        }
    }

    private void handleSubmenuClick(String subMenuText, HBox clickedItem) {
        if (selectedItem != null)
            selectedItem.setStyle("-fx-background-color: transparent;");

        selectedItem = clickedItem;
        clickedItem.setStyle("-fx-background-color: rgba(255,255,255,0.1);");

        switch (subMenuText) {
            case "Đặt bàn" -> trangChu.setMainContent(new Gui_DanhSachBan(trangChu));
            case "Đổi bàn" -> trangChu.setMainContent(new Gui_DoiBan());
            case "Hủy bàn" -> trangChu.setMainContent(new Gui_HuyBan());
            case "Check-in" -> trangChu.setMainContent(new Gui_CheckIn());
            default -> trangChu.setMainContent(new Label("Submenu " + subMenuText));
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

    private void handleLogout() {
        System.out.println("Đăng xuất được click!");
        // TODO: Thêm logic đăng xuất (hiển thị dialog hoặc quay lại màn hình đăng nhập)
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
}
