package server.util;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public final class DevDataSeeder {
    private static final String DEFAULT_PASSWORD_HASH =
            "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92";
    private static final DateTimeFormatter DAY_ID = DateTimeFormatter.ofPattern("ddMMyyyy");

    private DevDataSeeder() {
    }

    public static void ensureMinimumLoginData(MongoDatabase database) {
        MongoCollection<Document> nhanVien = database.getCollection("NhanVien");
        MongoCollection<Document> taiKhoan = database.getCollection("TaiKhoan");
        MongoCollection<Document> banAn = database.getCollection("BanAn");
        MongoCollection<Document> monAn = database.getCollection("MonAn");
        MongoCollection<Document> khachHang = database.getCollection("KhachHang");
        MongoCollection<Document> khuyenMai = database.getCollection("KhuyenMai");
        MongoCollection<Document> hoaDon = database.getCollection("HoaDon");
        MongoCollection<Document> chiTietHoaDon = database.getCollection("ChiTietHoaDon");
        MongoCollection<Document> phieuDatBan = database.getCollection("PhieuDatBan");

        if (nhanVien.countDocuments() == 0) {
            nhanVien.insertOne(new Document("maNhanVien", "NV001")
                    .append("tenNhanVien", "Admin")
                    .append("chucVu", "Quan ly")
                    .append("CCCD", "001001001001")
                    .append("soDienThoai", "0900000001")
                    .append("ngaySinh", toDate(LocalDate.of(1995, 1, 1)))
                    .append("ngayVaoLam", toDate(LocalDate.of(2024, 1, 1)))
                    .append("ngayThoiViec", null));

            nhanVien.insertOne(new Document("maNhanVien", "NV002")
                    .append("tenNhanVien", "Nhan vien mau")
                    .append("chucVu", "Nhan vien")
                    .append("CCCD", "002002002002")
                    .append("soDienThoai", "0900000002")
                    .append("ngaySinh", toDate(LocalDate.of(1998, 2, 2)))
                    .append("ngayVaoLam", toDate(LocalDate.of(2024, 1, 1)))
                    .append("ngayThoiViec", null));

            System.out.println("Seeded minimum NhanVien data");
        }

        if (taiKhoan.countDocuments() == 0) {
            taiKhoan.insertOne(new Document("maTaiKhoan", "TK001")
                    .append("taiKhoan", "admin")
                    .append("matKhau", DEFAULT_PASSWORD_HASH)
                    .append("taiKhoanQuanLi", true)
                    .append("trangThaiHoatDong", true)
                    .append("maNhanVien", "NV001"));

            taiKhoan.insertOne(new Document("maTaiKhoan", "TK002")
                    .append("taiKhoan", "nhanvien")
                    .append("matKhau", DEFAULT_PASSWORD_HASH)
                    .append("taiKhoanQuanLi", false)
                    .append("trangThaiHoatDong", true)
                    .append("maNhanVien", "NV002"));

            System.out.println("Seeded minimum TaiKhoan data");
            System.out.println("Default accounts: admin / 123456, nhanvien / 123456");
        }

        if (banAn.countDocuments() == 0) {
            banAn.insertOne(ban("B001", "VIP", "TRONG", "LAU_1"));
            banAn.insertOne(ban("B002", "THUONG", "TRONG", "LAU_1"));
            banAn.insertOne(ban("B003", "THUONG", "TRONG", "LAU_1"));
            banAn.insertOne(ban("B004", "VIP", "TRONG", "LAU_1"));
            banAn.insertOne(ban("B005", "THUONG", "TRONG", "LAU_2"));
            banAn.insertOne(ban("B006", "THUONG", "TRONG", "LAU_2"));
            banAn.insertOne(ban("B007", "VIP", "TRONG", "LAU_2"));
            banAn.insertOne(ban("B008", "THUONG", "TRONG", "LAU_2"));
            System.out.println("Seeded minimum BanAn data");
        }

        if (monAn.countDocuments() == 0) {
            monAn.insertOne(mon("MA001", "Com chien hai san", "Com", 89000, "Com chien tom muc", ""));
            monAn.insertOne(mon("MA002", "Pho bo", "Mon nuoc", 69000, "Pho bo tai", ""));
            monAn.insertOne(mon("MA003", "Ga nuong mat ong", "Mon chinh", 129000, "Ga nuong vi mat ong", ""));
            monAn.insertOne(mon("MA004", "Ca phe sua", "Do uong", 29000, "Ca phe sua da", ""));
            monAn.insertOne(mon("MA005", "Tra dao", "Do uong", 35000, "Tra dao cam sa", ""));
            monAn.insertOne(mon("MA006", "Banh flan", "Trang mieng", 25000, "Banh flan mem", ""));
            System.out.println("Seeded minimum MonAn data");
        }

        if (khachHang.countDocuments() == 0) {
            khachHang.insertOne(khach("KH001", "Khach le", "0901000001", 0.0));
            khachHang.insertOne(khach("KH002", "Khach quen", "0901000002", 120.0));
            System.out.println("Seeded minimum KhachHang data");
        }

        if (khuyenMai.countDocuments() == 0) {
            khuyenMai.insertOne(new Document("maKhuyenMai", "KM001")
                    .append("tenKhuyenMai", "Giam 10 phan tram")
                    .append("ngayBatDau", toDate(LocalDate.now().minusDays(7)))
                    .append("ngayKetThuc", toDate(LocalDate.now().plusDays(30)))
                    .append("dieuKienApDung", 100000.0)
                    .append("giaTriToiDa", 50000.0)
                    .append("giamGiaPhanTram", true)
                    .append("giaTriGiam", 10.0));
            System.out.println("Seeded minimum KhuyenMai data");
        }

        if (hoaDon.countDocuments() == 0) {
            LocalDate today = LocalDate.now();
            LocalDate yesterday = today.minusDays(1);
            LocalDateTime activeAt = LocalDateTime.now().minusHours(1);
            LocalDateTime paidAt = LocalDateTime.now().minusDays(1);

            String hdToday = "HD" + today.format(DAY_ID) + "001";
            String hdYesterday = "HD" + yesterday.format(DAY_ID) + "001";

            hoaDon.insertOne(new Document("maHoaDon", hdToday)
                    .append("ngayTao", toDate(activeAt))
                    .append("trangThai", "\u0110ang ph\u1ee5c v\u1ee5")
                    .append("phuongThuc", "")
                    .append("ghiChu", "Dung tai cho")
                    .append("maNhanVien", "NV001")
                    .append("maKhachHang", "KH001"));

            hoaDon.insertOne(new Document("maHoaDon", hdYesterday)
                    .append("ngayTao", toDate(paidAt))
                    .append("trangThai", "\u0110\u00e3 thanh to\u00e1n")
                    .append("phuongThuc", "Ti\u1ec1n m\u1eb7t")
                    .append("ghiChu", "Hoa don mau")
                    .append("maNhanVien", "NV001")
                    .append("maKhachHang", "KH002"));
            System.out.println("Seeded minimum HoaDon data");
        }

        if (chiTietHoaDon.countDocuments() == 0) {
            LocalDate today = LocalDate.now();
            LocalDate yesterday = today.minusDays(1);
            String hdToday = "HD" + today.format(DAY_ID) + "001";
            String hdYesterday = "HD" + yesterday.format(DAY_ID) + "001";

            chiTietHoaDon.insertOne(ct(hdToday, "MA001", 2, 89000));
            chiTietHoaDon.insertOne(ct(hdToday, "MA004", 2, 29000));
            chiTietHoaDon.insertOne(ct(hdYesterday, "MA003", 1, 129000));
            chiTietHoaDon.insertOne(ct(hdYesterday, "MA005", 2, 35000));
            System.out.println("Seeded minimum ChiTietHoaDon data");
        }

        if (phieuDatBan.countDocuments() == 0) {
            LocalDate today = LocalDate.now();
            LocalDate yesterday = today.minusDays(1);
            LocalDateTime activeAt = LocalDateTime.now().minusHours(1);
            LocalDateTime paidAt = LocalDateTime.now().minusDays(1);
            String hdToday = "HD" + today.format(DAY_ID) + "001";
            String hdYesterday = "HD" + yesterday.format(DAY_ID) + "001";

            phieuDatBan.insertOne(new Document("maPhieu", "PDB" + today.format(DAY_ID) + "001")
                    .append("thoiGianBatDau", toDate(activeAt))
                    .append("trangThai", "\u0110ang d\u00f9ng")
                    .append("soNguoi", 4)
                    .append("ghiChu", "D\u00f9ng ngay")
                    .append("maKhachHang", "KH001")
                    .append("maBan", "B001")
                    .append("maNhanVien", "NV001")
                    .append("maHoaDon", hdToday));

            phieuDatBan.insertOne(new Document("maPhieu", "PDB" + yesterday.format(DAY_ID) + "001")
                    .append("thoiGianBatDau", toDate(paidAt))
                    .append("trangThai", "\u0110\u00e3 d\u00f9ng")
                    .append("soNguoi", 2)
                    .append("ghiChu", "D\u00f9ng ngay")
                    .append("maKhachHang", "KH002")
                    .append("maBan", "B002")
                    .append("maNhanVien", "NV001")
                    .append("maHoaDon", hdYesterday));
            System.out.println("Seeded minimum PhieuDatBan data");
        }
    }

    private static Date toDate(LocalDate date) {
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private static Date toDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private static Document ban(String maBan, String loai, String trangThai, String viTri) {
        return new Document("maBan", maBan)
                .append("loai", loai)
                .append("trangThai", trangThai)
                .append("viTri", viTri);
    }

    private static Document mon(String maMonAn, String tenMonAn, String loaiMon, double giaTien, String moTa, String img) {
        return new Document("maMonAn", maMonAn)
                .append("tenMonAn", tenMonAn)
                .append("loaiMon", loaiMon)
                .append("giaTien", giaTien)
                .append("moTa", moTa)
                .append("img", img);
    }

    private static Document khach(String maKhachHang, String tenKhachHang, String soDienThoai, double diemTichLuy) {
        return new Document("maKhachHang", maKhachHang)
                .append("tenKhachHang", tenKhachHang)
                .append("soDienThoai", soDienThoai)
                .append("diemTichLuy", diemTichLuy);
    }

    private static Document ct(String maHoaDon, String maMonAn, int soLuong, double giaBan) {
        return new Document("maHoaDon", maHoaDon)
                .append("maMonAn", maMonAn)
                .append("soLuong", soLuong)
                .append("giaBan", giaBan);
    }
}
