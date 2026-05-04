package client.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InvoicePrinter extends Application {

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        // Tạo form nhập liệu
        VBox form = createInvoiceForm();
        root.setCenter(form);

        // Nút in
        Button printBtn = new Button("In Hóa Đơn");
        printBtn.setOnAction(e -> printInvoice(stage));

        HBox btnBox = new HBox(printBtn);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setPadding(new Insets(20));
        root.setBottom(btnBox);

        Scene scene = new Scene(root, 600, 500);
        stage.setTitle("In Hóa Đơn JavaFX");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createInvoiceForm() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setAlignment(Pos.TOP_CENTER);

        TextField invoiceNo = new TextField("HD001");
        TextField customerName = new TextField("Nguyễn Văn A");
        TextField productName = new TextField("Sản phẩm XYZ");
        TextField quantity = new TextField("2");
        TextField price = new TextField("500000");

        form.getChildren().addAll(
                new Label("Mã hóa đơn:"), invoiceNo,
                new Label("Tên khách hàng:"), customerName,
                new Label("Sản phẩm:"), productName,
                new Label("Số lượng:"), quantity,
                new Label("Đơn giá:"), price
        );

        return form;
    }

    private void printInvoice(Stage owner) {
        // Tạo PrinterJob
        PrinterJob job = PrinterJob.createPrinterJob();

        if (job != null) {
            // Cấu hình trang in
            PageLayout pageLayout = job.getPrinter().createPageLayout(
                    Paper.A4,
                    PageOrientation.PORTRAIT,
                    Printer.MarginType.DEFAULT
            );

            // Hiển thị dialog chọn máy in và cài đặt
            boolean showDialog = job.showPrintDialog(owner);

            if (showDialog) {
                // Tạo nội dung hóa đơn
                VBox invoice = createPrintableInvoice();

                // Đặt kích thước phù hợp với trang in
                invoice.setPrefWidth(pageLayout.getPrintableWidth());

                // In trang
                boolean success = job.printPage(pageLayout, invoice);

                if (success) {
                    job.endJob();
                    showAlert("Thành công", "In hóa đơn thành công!");
                } else {
                    showAlert("Lỗi", "Không thể in hóa đơn!");
                }
            } else {
                job.cancelJob();
            }
        } else {
            showAlert("Lỗi", "Không tìm thấy máy in!");
        }
    }

    private VBox createPrintableInvoice() {
        VBox invoice = new VBox(15);
        invoice.setPadding(new Insets(30));
        invoice.setStyle("-fx-background-color: white;");

        // Header - Tiêu đề
        Text header = new Text("HÓA ĐƠN BÁN HÀNG");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Thông tin công ty
        VBox companyInfo = new VBox(5);
        companyInfo.getChildren().addAll(
                new Text("CÔNG TY ABC"),
                new Text("Địa chỉ: 123 Đường XYZ, TP.HCM"),
                new Text("Điện thoại: 0123456789")
        );

        // Separator
        Separator sep1 = new Separator();

        // Thông tin hóa đơn
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        GridPane info = new GridPane();
        info.setHgap(20);
        info.setVgap(8);

        info.add(new Text("Mã hóa đơn:"), 0, 0);
        info.add(new Text("HD001"), 1, 0);
        info.add(new Text("Ngày:"), 0, 1);
        info.add(new Text(now.format(formatter)), 1, 1);
        info.add(new Text("Khách hàng:"), 0, 2);
        info.add(new Text("Nguyễn Văn A"), 1, 2);

        // Separator
        Separator sep2 = new Separator();

        // Bảng sản phẩm
        GridPane products = new GridPane();
        products.setHgap(15);
        products.setVgap(10);

        // Header bảng
        Text[] headers = {
                createBoldText("STT"),
                createBoldText("Sản phẩm"),
                createBoldText("SL"),
                createBoldText("Đơn giá"),
                createBoldText("Thành tiền")
        };

        for (int i = 0; i < headers.length; i++) {
            products.add(headers[i], i, 0);
        }

        // Dữ liệu mẫu
        products.add(new Text("1"), 0, 1);
        products.add(new Text("Sản phẩm XYZ"), 1, 1);
        products.add(new Text("2"), 2, 1);
        products.add(new Text("500,000đ"), 3, 1);
        products.add(new Text("1,000,000đ"), 4, 1);

        products.add(new Text("2"), 0, 2);
        products.add(new Text("Sản phẩm ABC"), 1, 2);
        products.add(new Text("1"), 2, 2);
        products.add(new Text("300,000đ"), 3, 2);
        products.add(new Text("300,000đ"), 4, 2);

        // Separator
        Separator sep3 = new Separator();

        // Tổng tiền
        VBox total = new VBox(5);
        total.setAlignment(Pos.CENTER_RIGHT);
        Text totalText = createBoldText("TỔNG CỘNG: 1,300,000đ");
        totalText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        total.getChildren().add(totalText);

        // Footer
        HBox footer = new HBox(100);
        footer.setPadding(new Insets(30, 0, 0, 0));

        VBox customer = new VBox(5);
        customer.setAlignment(Pos.CENTER);
        customer.getChildren().addAll(
                createBoldText("Khách hàng"),
                new Text("\n\n"),
                new Text("(Ký, ghi rõ họ tên)")
        );

        VBox seller = new VBox(5);
        seller.setAlignment(Pos.CENTER);
        seller.getChildren().addAll(
                createBoldText("Người bán hàng"),
                new Text("\n\n"),
                new Text("(Ký, ghi rõ họ tên)")
        );

        footer.getChildren().addAll(customer, seller);

        // Thêm tất cả vào invoice
        invoice.getChildren().addAll(
                header, companyInfo, sep1, info, sep2,
                products, sep3, total, footer
        );

        // Căn giữa header
        invoice.setAlignment(Pos.TOP_CENTER);

        return invoice;
    }

    private Text createBoldText(String content) {
        Text text = new Text(content);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        return text;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
