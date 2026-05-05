package server.core;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.ValidationOptions;
import com.mongodb.client.model.ValidationAction;
import com.mongodb.client.model.ValidationLevel;
import org.bson.Document;

import java.util.Arrays;

/**
 * SchemaValidator - Tạo ràng buộc (constraints) dưới database MongoDB.
 *
 * MongoDB hỗ trợ JSON Schema Validation để kiểm tra dữ liệu trước khi insert/update,
 * tương tự CHECK CONSTRAINT trong SQL Server.
 *
 * Ngoài ra, tạo Unique Index tương tự UNIQUE CONSTRAINT trong SQL.
 */
public final class SchemaValidator {

    private SchemaValidator() {}

    /**
     * Đảm bảo tất cả constraints được thiết lập khi server khởi động.
     */
    public static void ensureConstraints(MongoDatabase db) {
        System.out.println("=== Đang thiết lập ràng buộc database ===");

        ensureNhanVienConstraints(db);
        ensureTaiKhoanConstraints(db);
        ensureMonAnConstraints(db);
        ensureBanAnConstraints(db);
        ensureHoaDonConstraints(db);
        ensureKhachHangConstraints(db);
        ensureChiTietHoaDonConstraints(db);
        ensurePhieuDatBanConstraints(db);
        ensureKhuyenMaiConstraints(db);

        System.out.println("=== Ràng buộc database đã thiết lập xong ===");
    }

    // ==================== NHÂN VIÊN ====================
    private static void ensureNhanVienConstraints(MongoDatabase db) {
        Document schema = new Document("$jsonSchema", new Document("bsonType", "object")
                .append("required", Arrays.asList("maNhanVien", "tenNhanVien", "chucVu", "CCCD", "soDienThoai"))
                .append("properties", new Document()
                        .append("maNhanVien", new Document("bsonType", "string")
                                .append("description", "Mã nhân viên - bắt buộc, kiểu string"))
                        .append("tenNhanVien", new Document("bsonType", "string")
                                .append("minLength", 1)
                                .append("description", "Tên nhân viên - bắt buộc, không rỗng"))
                        .append("chucVu", new Document("bsonType", "string")
                                .append("description", "Chức vụ - bắt buộc"))
                        .append("CCCD", new Document("bsonType", "string")
                                .append("pattern", "^[0-9]{12}$")
                                .append("description", "CCCD - bắt buộc 12 số"))
                        .append("soDienThoai", new Document("bsonType", "string")
                                .append("pattern", "^0[0-9]{9}$")
                                .append("description", "SĐT - bắt buộc 10 số bắt đầu bằng 0"))
                ));

        applyValidator(db, "NhanVien", schema);

        // Unique indexes
        createUniqueIndex(db, "NhanVien", "maNhanVien");
        createUniqueIndex(db, "NhanVien", "CCCD");
        createUniqueIndex(db, "NhanVien", "soDienThoai");
        System.out.println("  ✓ NhanVien: schema + unique indexes");
    }

    // ==================== TÀI KHOẢN ====================
    private static void ensureTaiKhoanConstraints(MongoDatabase db) {
        Document schema = new Document("$jsonSchema", new Document("bsonType", "object")
                .append("required", Arrays.asList("maTaiKhoan", "taiKhoan", "matKhau", "maNhanVien"))
                .append("properties", new Document()
                        .append("maTaiKhoan", new Document("bsonType", "string")
                                .append("description", "Mã tài khoản - bắt buộc"))
                        .append("taiKhoan", new Document("bsonType", "string")
                                .append("minLength", 3)
                                .append("description", "Tên đăng nhập - bắt buộc, ít nhất 3 ký tự"))
                        .append("matKhau", new Document("bsonType", "string")
                                .append("minLength", 1)
                                .append("description", "Mật khẩu hash - bắt buộc"))
                        .append("taiKhoanQuanLi", new Document("bsonType", "bool")
                                .append("description", "Quyền quản lý"))
                        .append("trangThaiHoatDong", new Document("bsonType", "bool")
                                .append("description", "Trạng thái hoạt động"))
                        .append("maNhanVien", new Document("bsonType", "string")
                                .append("description", "Mã nhân viên liên kết - bắt buộc"))
                ));

        applyValidator(db, "TaiKhoan", schema);
        createUniqueIndex(db, "TaiKhoan", "maTaiKhoan");
        createUniqueIndex(db, "TaiKhoan", "taiKhoan");
        System.out.println("  ✓ TaiKhoan: schema + unique indexes");
    }

    // ==================== MÓN ĂN ====================
    private static void ensureMonAnConstraints(MongoDatabase db) {
        Document schema = new Document("$jsonSchema", new Document("bsonType", "object")
                .append("required", Arrays.asList("maMonAn", "tenMonAn", "loaiMon", "giaTien"))
                .append("properties", new Document()
                        .append("maMonAn", new Document("bsonType", "string")
                                .append("description", "Mã món ăn - bắt buộc"))
                        .append("tenMonAn", new Document("bsonType", "string")
                                .append("minLength", 1)
                                .append("description", "Tên món ăn - bắt buộc"))
                        .append("loaiMon", new Document("bsonType", "string")
                                .append("description", "Loại món - bắt buộc"))
                        .append("giaTien", new Document("bsonType", Arrays.asList("double", "int", "long"))
                                .append("minimum", 0)
                                .append("description", "Giá tiền - bắt buộc >= 0"))
                ));

        applyValidator(db, "MonAn", schema);
        createUniqueIndex(db, "MonAn", "maMonAn");
        System.out.println("  ✓ MonAn: schema + unique index");
    }

    // ==================== BÀN ĂN ====================
    private static void ensureBanAnConstraints(MongoDatabase db) {
        Document schema = new Document("$jsonSchema", new Document("bsonType", "object")
                .append("required", Arrays.asList("maBan", "loai", "trangThai", "viTri"))
                .append("properties", new Document()
                        .append("maBan", new Document("bsonType", "string")
                                .append("description", "Mã bàn - bắt buộc"))
                        .append("loai", new Document("bsonType", "string")
                                .append("enum", Arrays.asList("VIP", "THUONG"))
                                .append("description", "Loại bàn - chỉ VIP hoặc THUONG"))
                        .append("trangThai", new Document("bsonType", "string")
                                .append("enum", Arrays.asList("TRONG", "DANG_SU_DUNG", "DA_DAT"))
                                .append("description", "Trạng thái - chỉ TRONG, DANG_SU_DUNG, DA_DAT"))
                        .append("viTri", new Document("bsonType", "string")
                                .append("description", "Vị trí bàn - bắt buộc"))
                ));

        applyValidator(db, "BanAn", schema);
        createUniqueIndex(db, "BanAn", "maBan");
        System.out.println("  ✓ BanAn: schema + unique index");
    }

    // ==================== HÓA ĐƠN ====================
    private static void ensureHoaDonConstraints(MongoDatabase db) {
        Document schema = new Document("$jsonSchema", new Document("bsonType", "object")
                .append("required", Arrays.asList("maHoaDon", "ngayTao", "maNhanVien"))
                .append("properties", new Document()
                        .append("maHoaDon", new Document("bsonType", "string")
                                .append("description", "Mã hóa đơn - bắt buộc"))
                        .append("ngayTao", new Document("bsonType", "date")
                                .append("description", "Ngày tạo - bắt buộc, kiểu date"))
                        .append("trangThai", new Document("bsonType", "string")
                                .append("description", "Trạng thái hóa đơn"))
                        .append("maNhanVien", new Document("bsonType", "string")
                                .append("description", "Nhân viên tạo - bắt buộc"))
                ));

        applyValidator(db, "HoaDon", schema);
        createUniqueIndex(db, "HoaDon", "maHoaDon");
        System.out.println("  ✓ HoaDon: schema + unique index");
    }

    // ==================== KHÁCH HÀNG ====================
    private static void ensureKhachHangConstraints(MongoDatabase db) {
        Document schema = new Document("$jsonSchema", new Document("bsonType", "object")
                .append("required", Arrays.asList("maKhachHang", "tenKhachHang", "soDienThoai"))
                .append("properties", new Document()
                        .append("maKhachHang", new Document("bsonType", "string")
                                .append("description", "Mã khách hàng - bắt buộc"))
                        .append("tenKhachHang", new Document("bsonType", "string")
                                .append("minLength", 1)
                                .append("description", "Tên khách hàng - bắt buộc"))
                        .append("soDienThoai", new Document("bsonType", "string")
                                .append("description", "Số điện thoại - bắt buộc"))
                        .append("diemTichLuy", new Document("bsonType", Arrays.asList("double", "int", "long"))
                                .append("minimum", 0)
                                .append("description", "Điểm tích lũy >= 0"))
                ));

        applyValidator(db, "KhachHang", schema);
        createUniqueIndex(db, "KhachHang", "maKhachHang");
        System.out.println("  ✓ KhachHang: schema + unique index");
    }

    // ==================== CHI TIẾT HÓA ĐƠN ====================
    private static void ensureChiTietHoaDonConstraints(MongoDatabase db) {
        Document schema = new Document("$jsonSchema", new Document("bsonType", "object")
                .append("required", Arrays.asList("maHoaDon", "maMonAn", "soLuong"))
                .append("properties", new Document()
                        .append("maHoaDon", new Document("bsonType", "string")
                                .append("description", "Mã hóa đơn - bắt buộc"))
                        .append("maMonAn", new Document("bsonType", "string")
                                .append("description", "Mã món ăn - bắt buộc"))
                        .append("soLuong", new Document("bsonType", Arrays.asList("int", "long"))
                                .append("minimum", 1)
                                .append("description", "Số lượng - bắt buộc >= 1"))
                ));

        applyValidator(db, "ChiTietHoaDon", schema);
        System.out.println("  ✓ ChiTietHoaDon: schema validation");
    }

    // ==================== PHIẾU ĐẶT BÀN ====================
    private static void ensurePhieuDatBanConstraints(MongoDatabase db) {
        Document schema = new Document("$jsonSchema", new Document("bsonType", "object")
                .append("required", Arrays.asList("maPhieu", "thoiGianBatDau", "soNguoi", "maBan"))
                .append("properties", new Document()
                        .append("maPhieu", new Document("bsonType", "string")
                                .append("description", "Mã phiếu đặt bàn - bắt buộc"))
                        .append("thoiGianBatDau", new Document("bsonType", "date")
                                .append("description", "Thời gian bắt đầu - bắt buộc"))
                        .append("soNguoi", new Document("bsonType", Arrays.asList("int", "long"))
                                .append("minimum", 1)
                                .append("description", "Số người - bắt buộc >= 1"))
                        .append("maBan", new Document("bsonType", "string")
                                .append("description", "Mã bàn - bắt buộc"))
                ));

        applyValidator(db, "PhieuDatBan", schema);
        createUniqueIndex(db, "PhieuDatBan", "maPhieu");
        System.out.println("  ✓ PhieuDatBan: schema + unique index");
    }

    // ==================== KHUYẾN MÃI ====================
    private static void ensureKhuyenMaiConstraints(MongoDatabase db) {
        Document schema = new Document("$jsonSchema", new Document("bsonType", "object")
                .append("required", Arrays.asList("maKhuyenMai", "tenKhuyenMai", "ngayBatDau", "ngayKetThuc"))
                .append("properties", new Document()
                        .append("maKhuyenMai", new Document("bsonType", "string")
                                .append("description", "Mã khuyến mãi - bắt buộc"))
                        .append("tenKhuyenMai", new Document("bsonType", "string")
                                .append("minLength", 1)
                                .append("description", "Tên khuyến mãi - bắt buộc"))
                        .append("giaTriGiam", new Document("bsonType", Arrays.asList("double", "int", "long"))
                                .append("minimum", 0)
                                .append("description", "Giá trị giảm >= 0"))
                ));

        applyValidator(db, "KhuyenMai", schema);
        createUniqueIndex(db, "KhuyenMai", "maKhuyenMai");
        System.out.println("  ✓ KhuyenMai: schema + unique index");
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Áp dụng JSON Schema Validator cho collection.
     * Nếu collection chưa tồn tại → tạo mới với validator.
     * Nếu đã tồn tại → update validator bằng collMod command.
     */
    private static void applyValidator(MongoDatabase db, String collectionName, Document validator) {
        try {
            if (!collectionExists(db, collectionName)) {
                ValidationOptions validationOptions = new ValidationOptions()
                        .validator(validator)
                        .validationLevel(ValidationLevel.MODERATE)
                        .validationAction(ValidationAction.ERROR);

                db.createCollection(collectionName, new CreateCollectionOptions().validationOptions(validationOptions));
            } else {
                // Update validator cho collection đã tồn tại
                db.runCommand(new Document("collMod", collectionName)
                        .append("validator", validator)
                        .append("validationLevel", "moderate")
                        .append("validationAction", "error"));
            }
        } catch (Exception e) {
            System.err.println("  ⚠ Warning: Could not apply validator for " + collectionName + ": " + e.getMessage());
        }
    }

    /**
     * Tạo Unique Index (tương đương UNIQUE CONSTRAINT trong SQL).
     */
    private static void createUniqueIndex(MongoDatabase db, String collectionName, String field) {
        try {
            db.getCollection(collectionName).createIndex(
                    Indexes.ascending(field),
                    new IndexOptions().unique(true).name("unique_" + field)
            );
        } catch (Exception e) {
            // Index có thể đã tồn tại, bỏ qua
        }
    }

    private static boolean collectionExists(MongoDatabase db, String name) {
        for (String n : db.listCollectionNames()) {
            if (n.equals(name)) return true;
        }
        return false;
    }
}
