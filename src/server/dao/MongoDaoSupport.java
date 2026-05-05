package server.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import common.entity.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import server.db.ConnectDB;

import java.time.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * MongoDaoSupport — Lớp ODM (Object-Document Mapping) hỗ trợ cho MongoDB.
 *
 * Lớp này cung cấp các phương thức ánh xạ giữa Java Entity và MongoDB Document,
 * tương tự vai trò của ORM (Hibernate) nhưng dành cho NoSQL (MongoDB).
 *
 * Bao gồm:
 * - Generic CRUD operations (insert, find, update, delete)
 * - Entity ↔ Document mapping methods (banAn(), monAn(), nhanVien()...)
 * - Query helpers (contains(), inRange(), sameDay()...)
 * - ID generation (nextId(), maxId())
 */
abstract class MongoDaoSupport {
    static {
        ensureServerOnly();
    }

    MongoDaoSupport() {
        ensureServerOnly();
    }

    private static void ensureServerOnly() {
        if (!"server".equals(System.getProperty("app.role"))) {
            throw new IllegalStateException("DAO access is server-only. Client code must use RMI services.");
        }
    }

    static MongoCollection<Document> col(String name) {
        ensureServerOnly();
        return ConnectDB.getDatabase().getCollection(name);
    }

    static Date toDate(LocalDate date) {
        return date == null ? null : Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    static Date toDate(LocalDateTime dateTime) {
        return dateTime == null ? null : Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    static LocalDate toLocalDate(Object value) {
        if (value == null)
            return null;
        if (value instanceof LocalDate)
            return (LocalDate) value;
        if (value instanceof Date)
            return ((Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return LocalDate.parse(value.toString());
    }

    static LocalDateTime toLocalDateTime(Object value) {
        if (value == null)
            return null;
        if (value instanceof LocalDateTime)
            return (LocalDateTime) value;
        if (value instanceof Date)
            return ((Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return LocalDateTime.parse(value.toString().replace(' ', 'T'));
    }

    static String s(Document d, String key) {
        return d == null ? null : d.getString(key);
    }

    static double dbl(Document d, String key) {
        Object value = d == null ? null : d.get(key);
        return value instanceof Number ? ((Number) value).doubleValue() : 0.0;
    }

    static int integer(Document d, String key) {
        Object value = d == null ? null : d.get(key);
        return value instanceof Number ? ((Number) value).intValue() : 0;
    }

    static boolean bool(Document d, String key) {
        Object value = d == null ? null : d.get(key);
        if (value instanceof Boolean)
            return (Boolean) value;
        if (value instanceof Number)
            return ((Number) value).intValue() != 0;
        return value != null && Boolean.parseBoolean(value.toString());
    }

    static Pattern contains(String keyword) {
        return Pattern.compile(Pattern.quote(keyword == null ? "" : keyword),
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    }

    static boolean sameDay(Object value, LocalDate day) {
        LocalDate actual = toLocalDate(value);
        return actual != null && actual.equals(day);
    }

    /**
     * Tạo Bson filter để lọc document theo ngày (MongoDB-native, thay thế sameDay
     * Java-side).
     * Dùng khoảng [startOfDay, startOfNextDay) để match chính xác 1 ngày.
     */
    static Bson sameDayFilter(String field, LocalDate date) {
        Date start = toDate(date);
        Date end = toDate(date.plusDays(1));
        return Filters.and(Filters.gte(field, start), Filters.lt(field, end));
    }

    static boolean inRange(Object value, LocalDate from, LocalDate to) {
        LocalDate actual = toLocalDate(value);
        if (actual == null)
            return false;
        return (from == null || !actual.isBefore(from)) && (to == null || !actual.isAfter(to));
    }

    static String maxId(String collection, String idField, String prefix) {
        Document last = col(collection)
                .find(Filters.regex(idField, "^" + Pattern.quote(prefix)))
                .sort(Sorts.descending(idField))
                .first();
        return last == null ? null : last.getString(idField);
    }

    static String nextId(String collection, String idField, String prefix, int width) {
        String last = maxId(collection, idField, prefix);
        int max = 0;
        if (last != null && last.startsWith(prefix)) {
            String suffix = last.substring(prefix.length()).replaceAll("[^0-9].*$", "");
            if (!suffix.isEmpty()) {
                try {
                    max = Integer.parseInt(suffix);
                } catch (NumberFormatException ignored) {
                    max = 0;
                }
            }
        }
        return prefix + String.format("%0" + width + "d", max + 1);
    }

    static List<Document> docs(String collection) {
        List<Document> result = new ArrayList<>();
        col(collection).find().into(result);
        return result;
    }

    static List<Document> docs(String collection, Bson filter) {
        List<Document> result = new ArrayList<>();
        col(collection).find(filter).into(result);
        return result;
    }

    static Document one(String collection, String field, String value) {
        return col(collection).find(Filters.eq(field, value)).first();
    }

    static boolean update(String collection, Bson filter, Document set) {
        return col(collection).updateOne(filter, new Document("$set", set)).getModifiedCount() > 0;
    }

    static boolean delete(String collection, Bson filter) {
        return col(collection).deleteOne(filter).getDeletedCount() > 0;
    }

    // ==================== GENERIC CRUD (ODM Pattern) ====================

    /**
     * Insert một document vào collection (Create).
     */
    static void insertDoc(String collection, Document doc) {
        col(collection).insertOne(doc);
    }

    /**
     * Tìm document theo ID field (Read).
     */
    static Document findById(String collection, String idField, String idValue) {
        return one(collection, idField, idValue);
    }

    /**
     * Update document theo filter, chỉ set các field trong updateDoc (Update).
     */
    static boolean updateById(String collection, String idField, String idValue, Document updateDoc) {
        return update(collection, Filters.eq(idField, idValue), updateDoc);
    }

    /**
     * Xóa document theo ID field (Delete).
     */
    static boolean deleteById(String collection, String idField, String idValue) {
        return delete(collection, Filters.eq(idField, idValue));
    }

    /**
     * Đếm số document thỏa filter.
     */
    static long countByFilter(String collection, Bson filter) {
        return col(collection).countDocuments(filter);
    }

    /**
     * Kiểm tra tồn tại theo field.
     */
    static boolean existsByField(String collection, String field, String value) {
        return col(collection).countDocuments(Filters.eq(field, value)) > 0;
    }

    /**
     * Tìm danh sách document theo filter với sắp xếp.
     */
    static List<Document> findWithFilter(String collection, Bson filter, Bson sort) {
        List<Document> result = new ArrayList<>();
        col(collection).find(filter).sort(sort).into(result);
        return result;
    }

    // ==================== ENTITY MAPPING (ODM) ====================

    static BanAn banAn(Document d) {
        if (d == null)
            return null;
        return new BanAn(
                s(d, "maBan"),
                LoaiBan.fromDB(s(d, "loai")),
                TrangThai.fromDB(s(d, "trangThai")),
                ViTri.fromDB(s(d, "viTri")));
    }

    static Document banAnDoc(BanAn b) {
        return new Document("maBan", b.getMaBan())
                .append("loai", b.getLoai() == null ? null : b.getLoai().name())
                .append("trangThai", b.getTrangThai() == null ? null : b.getTrangThai().name())
                .append("viTri", b.getViTri() == null ? null : b.getViTri().name());
    }

    static KhachHang khachHang(Document d) {
        if (d == null)
            return null;
        return new KhachHang(s(d, "maKhachHang"), s(d, "tenKhachHang"), s(d, "soDienThoai"), dbl(d, "diemTichLuy"));
    }

    static Document khachHangDoc(KhachHang k) {
        return new Document("maKhachHang", k.getMaKhachHang())
                .append("tenKhachHang", k.getTenKhachHang())
                .append("soDienThoai", k.getSoDienThoai())
                .append("diemTichLuy", k.getDiemTichLuy());
    }

    static NhanVien nhanVien(Document d) {
        if (d == null)
            return null;
        return new NhanVien(
                s(d, "maNhanVien"), s(d, "tenNhanVien"), s(d, "chucVu"), s(d, "CCCD"), s(d, "soDienThoai"),
                toLocalDate(d.get("ngaySinh")), toLocalDate(d.get("ngayVaoLam")), toLocalDate(d.get("ngayThoiViec")));
    }

    static Document nhanVienDoc(NhanVien n) {
        return new Document("maNhanVien", n.getMaNhanVien())
                .append("tenNhanVien", n.getTenNhanVien())
                .append("chucVu", n.getChucVu())
                .append("CCCD", n.getCCCD())
                .append("soDienThoai", n.getSoDienThoai())
                .append("ngaySinh", toDate(n.getNgaySinh()))
                .append("ngayVaoLam", toDate(n.getNgayVaoLam()))
                .append("ngayThoiViec", toDate(n.getNgayThoiViec()));
    }

    static MonAn monAn(Document d) {
        if (d == null)
            return null;
        return new MonAn(s(d, "maMonAn"), s(d, "tenMonAn"), s(d, "loaiMon"), dbl(d, "giaTien"), s(d, "moTa"),
                s(d, "img"));
    }

    static Document monAnDoc(MonAn m) {
        return new Document("maMonAn", m.getMaMonAn())
                .append("tenMonAn", m.getTenMonAn())
                .append("loaiMon", m.getLoaiMon())
                .append("giaTien", m.getGiaTien())
                .append("moTa", m.getMoTa())
                .append("img", m.getHinhAnh());
    }

    static KhuyenMai khuyenMai(Document d) {
        if (d == null)
            return null;
        return new KhuyenMai(s(d, "maKhuyenMai"), s(d, "tenKhuyenMai"), toLocalDate(d.get("ngayBatDau")),
                toLocalDate(d.get("ngayKetThuc")), dbl(d, "dieuKienApDung"), dbl(d, "giaTriToiDa"),
                bool(d, "giamGiaPhanTram"), dbl(d, "giaTriGiam"));
    }

    static Document khuyenMaiDoc(KhuyenMai k) {
        return new Document("maKhuyenMai", k.getMaKhuyenMai())
                .append("tenKhuyenMai", k.getTenKhuyenMai())
                .append("ngayBatDau", toDate(k.getNgayBatDau()))
                .append("ngayKetThuc", toDate(k.getNgayKetThuc()))
                .append("dieuKienApDung", k.getDieuKienApDung())
                .append("giaTriToiDa", k.getGiaTriToiDa())
                .append("giamGiaPhanTram", k.getGiamGiaPhanTram())
                .append("giaTriGiam", k.getGiaTriGiam());
    }

    static HoaDon hoaDon(Document d) {
        if (d == null)
            return null;
        return new HoaDon(
                s(d, "maHoaDon"), toLocalDateTime(d.get("ngayTao")), s(d, "trangThai"), s(d, "phuongThuc"),
                s(d, "ghiChu"),
                nhanVien(one("NhanVien", "maNhanVien", s(d, "maNhanVien"))),
                khachHang(one("KhachHang", "maKhachHang", s(d, "maKhachHang"))));
    }

    static Document hoaDonDoc(HoaDon h) {
        return new Document("maHoaDon", h.getMaHoaDon())
                .append("ngayTao", toDate(h.getNgayTao()))
                .append("trangThai", h.getTrangThai())
                .append("phuongThuc", h.getPhuongThuc())
                .append("ghiChu", h.getGhiChu())
                .append("maNhanVien", h.getNhanVien() == null ? null : h.getNhanVien().getMaNhanVien())
                .append("maKhachHang", h.getKhachHang() == null ? null : h.getKhachHang().getMaKhachHang());
    }

    static PhieuDatBan phieuDatBan(Document d) {
        if (d == null)
            return null;
        PhieuDatBan p = new PhieuDatBan(
                s(d, "maPhieu"), toLocalDateTime(d.get("thoiGianBatDau")),
                toLocalDateTime(d.get("thoiGianKetThuc")),
                s(d, "trangThai"), integer(d, "soNguoi"),
                s(d, "ghiChu"),
                khachHang(one("KhachHang", "maKhachHang", s(d, "maKhachHang"))),
                banAn(one("BanAn", "maBan", s(d, "maBan"))),
                nhanVien(one("NhanVien", "maNhanVien", s(d, "maNhanVien"))),
                hoaDon(one("HoaDon", "maHoaDon", s(d, "maHoaDon"))),
                dbl(d, "tienCoc"));
        return p;
    }

    static Document phieuDatBanDoc(PhieuDatBan p, String trangThaiPhieu) {
        return new Document("maPhieu", p.getMaPhieu())
                .append("thoiGianBatDau", toDate(p.getThoiGianBatDau()))
                .append("thoiGianKetThuc", toDate(p.getThoiGianKetThuc()))
                .append("trangThai", trangThaiPhieu != null ? trangThaiPhieu : p.getTrangThai())
                .append("soNguoi", p.getSoNguoi())
                .append("ghiChu", p.getGhiChu())
                .append("maKhachHang", p.getKhachHang() == null ? null : p.getKhachHang().getMaKhachHang())
                .append("maBan", p.getBan() == null ? null : p.getBan().getMaBan())
                .append("maNhanVien", p.getNhanVien() == null ? null : p.getNhanVien().getMaNhanVien())
                .append("maHoaDon", p.getHoaDon() == null ? null : p.getHoaDon().getMaHoaDon())
                .append("tienCoc", p.getTienCoc());
    }

    static ChiTietHoaDon chiTietHoaDon(Document d) {
        if (d == null)
            return null;
        return new ChiTietHoaDon(
                hoaDon(one("HoaDon", "maHoaDon", s(d, "maHoaDon"))),
                monAn(one("MonAn", "maMonAn", s(d, "maMonAn"))),
                integer(d, "soLuong"));
    }

    static TaiKhoan taiKhoan(Document d) {
        if (d == null)
            return null;
        return new TaiKhoan(s(d, "maTaiKhoan"), s(d, "taiKhoan"), s(d, "matKhau"), bool(d, "taiKhoanQuanLi"),
                bool(d, "trangThaiHoatDong"), nhanVien(one("NhanVien", "maNhanVien", s(d, "maNhanVien"))));
    }

    static Document taiKhoanDoc(TaiKhoan t) {
        return new Document("maTaiKhoan", t.getMaTaiKhoan())
                .append("taiKhoan", t.getTaiKhoan())
                .append("matKhau", t.getMatKhau())
                .append("taiKhoanQuanLi", t.isTaiKhoanQuanLi())
                .append("trangThaiHoatDong", t.isTrangThaiHoatDong())
                .append("maNhanVien", t.getNhanVien() == null ? null : t.getNhanVien().getMaNhanVien());
    }

    static double invoiceTotal(String maHoaDon) {
        double total = 0;
        for (Document ct : docs("ChiTietHoaDon", Filters.eq("maHoaDon", maHoaDon))) {
            Document mon = one("MonAn", "maMonAn", s(ct, "maMonAn"));
            total += integer(ct, "soLuong") * dbl(mon, "giaTien");
        }
        return total;
    }
}
