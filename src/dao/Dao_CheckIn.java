package dao;

import entity.PhieuDatBan;
import entity.KhachHang;
import entity.BanAn;
import entity.NhanVien;
import entity.HoaDon;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;

public class Dao_CheckIn {

    // Lấy tất cả phiếu đặt bàn
    public List<PhieuDatBan> getAllPhieuDatBan() {
        List<PhieuDatBan> list = new ArrayList<>();
        String sql = "SELECT maPhieu, thoiGianBatDau, trangThai, soNguoi, ghiChu, maKhachHang, maBan, maNhanVien, maHoaDon FROM PhieuDatBan ORDER BY thoiGianBatDau DESC";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PhieuDatBan p = mapResultSetToPhieu(rs);
                list.add(p);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    // Lấy phiếu theo trạng thái
    public List<PhieuDatBan> getPhieuTheoTrangThai(String trangThai) {
        List<PhieuDatBan> list = new ArrayList<>();
        String sql = "SELECT maPhieu, thoiGianBatDau, trangThai, soNguoi, ghiChu, maKhachHang, maBan, maNhanVien, maHoaDon FROM PhieuDatBan WHERE trangThai = ? ORDER BY thoiGianBatDau DESC";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, trangThai);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PhieuDatBan p = mapResultSetToPhieu(rs);
                    list.add(p);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    // Lấy phiếu theo mã
    public PhieuDatBan getPhieuTheoMa(String maPhieu) {
        String sql = "SELECT maPhieu, thoiGianBatDau, trangThai, soNguoi, ghiChu, maKhachHang, maBan, maNhanVien, maHoaDon FROM PhieuDatBan WHERE maPhieu = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maPhieu);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPhieu(rs);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Cập nhật trạng thái phiếu (ví dụ: Check-in -> "Đã dùng")
    public boolean capNhatTrangThaiPhieu(String maPhieu, String trangThaiMoi) {
        String sql = "UPDATE PhieuDatBan SET trangThai = ? WHERE maPhieu = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, trangThaiMoi);
            ps.setString(2, maPhieu);
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // Chèn phiếu mới (nếu bạn cần)
    public boolean insertPhieuDatBan(PhieuDatBan p) {
        String sql = "INSERT INTO PhieuDatBan (maPhieu, thoiGianBatDau, trangThai, soNguoi, ghiChu, maKhachHang, maBan, maNhanVien, maHoaDon) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getMaPhieu());

            // chuyển LocalDateTime -> Timestamp
            LocalDateTime dt = p.getThoiGianBatDau();
            if (dt != null) {
                ps.setTimestamp(2, Timestamp.valueOf(dt));
            } else {
                ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            }

            ps.setString(3, p.getTrangThai());
            ps.setInt(4, p.getSoNguoi());
            ps.setString(5, p.getGhiChu());

            // Nếu lớp PhieuDatBan chứa đối tượng KhachHang/BanAn... thì lấy mã
            ps.setString(6, p.getKhachHang() != null ? p.getKhachHang().getMaKhachHang() : null);
            ps.setString(7, p.getBan() != null ? p.getBan().getMaBan() : null);
            ps.setString(8, p.getNhanVien() != null ? p.getNhanVien().getMaNhanVien() : null);
            ps.setString(9, p.getHoaDon() != null ? p.getHoaDon().getMaHoaDon() : null);

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }


    // ---------------- helper ----------------
    // Map ResultSet -> PhieuDatBan
    private PhieuDatBan mapResultSetToPhieu(ResultSet rs) throws SQLException {
        PhieuDatBan p = new PhieuDatBan();

        p.setMaPhieu(rs.getString("maPhieu"));

        Timestamp ts = rs.getTimestamp("thoiGianBatDau");
        if (ts != null) {
            p.setThoiGianBatDau(ts.toLocalDateTime());
        }

        p.setTrangThai(rs.getString("trangThai"));
        p.setSoNguoi(rs.getInt("soNguoi"));
        p.setGhiChu(rs.getString("ghiChu"));

        // Lấy các mã liên quan và gán vào object liên kết (nếu class KhachHang/BanAn... tồn tại với setter tương ứng)
        String maKH = rs.getString("maKhachHang");
        if (maKH != null) {
            KhachHang kh = new KhachHang();
            // *** Giả định KhachHang có setMaKhachHang(String) ***
            kh.setMaKhachHang(maKH);
            p.setKhachHang(kh);
        }

        String maBan = rs.getString("maBan");
        if (maBan != null) {
            BanAn b = new BanAn();
            b.setMaBan(maBan);
            p.setBan(b);
        }

        String maNV = rs.getString("maNhanVien");
        if (maNV != null) {
            NhanVien nv = new NhanVien();
            nv.setMaNhanVien(maNV);
            p.setNhanVien(nv);
        }

        String maHD = rs.getString("maHoaDon");
        if (maHD != null) {
            HoaDon hd = new HoaDon();
            hd.setMaHoaDon(maHD);
            p.setHoaDon(hd);
        }

        return p;
    }
    
    public List<String> getThongTinPhieuDatBan() {
        List<String> list = new ArrayList<>();

        String sql = """
            SELECT 
                b.maBan,
                k.tenKhachHang,
                p.thoiGianBatDau,
                b.loai,
                k.maKhachHang,
                k.soDienThoai,
                k.diemTichLuy,
                p.soNguoi,
                p.ghiChu
            FROM PhieuDatBan p
            JOIN BanAn b ON p.maBan = b.maBan
            JOIN KhachHang k ON p.maKhachHang = k.maKhachHang
            WHERE p.trangThai = N'Đã đặt'
            ORDER BY p.thoiGianBatDau DESC
        """;

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Lấy tất cả các trường, xử lý null
                String maBan = rs.getString("maBan") != null ? rs.getString("maBan") : "N/A";
                String tenKhachHang = rs.getString("tenKhachHang") != null ? rs.getString("tenKhachHang") : "N/A";
                String loaiBan = rs.getString("loai") != null ? rs.getString("loai") : "N/A";
                String maKhachHang = rs.getString("maKhachHang") != null ? rs.getString("maKhachHang") : "N/A";
                String soDienThoai = rs.getString("soDienThoai") != null ? rs.getString("soDienThoai") : "N/A";
                BigDecimal diemTichLuyBD = rs.getBigDecimal("diemTichLuy");
                String diemTichLuyStr = "0.00";  // Default nếu null
                if (diemTichLuyBD != null) {
                   
                    double diemTichLuyDouble = diemTichLuyBD.doubleValue();
                    diemTichLuyStr = String.format("%.2f", diemTichLuyDouble);
                }
                int soNguoi = rs.getInt("soNguoi");  // Giả sử int
                String ghiChu = rs.getString("ghiChu") != null ? rs.getString("ghiChu") : "N/A";

                // Xử lý thời gian
                Timestamp ts = rs.getTimestamp("thoiGianBatDau");
                String thoiGian = "N/A";
                if (ts != null) {
                    LocalDateTime ldt = ts.toLocalDateTime();
                    thoiGian = String.format("(%02dh %02d/%02d/%04d)", 
                        ldt.getHour(),
                        ldt.getDayOfMonth(),
                        ldt.getMonthValue(),
                        ldt.getYear());
                    // Nếu cần thêm phút: String.format("(%02dh:%02dm %02d/%02d/%04d)", ldt.getHour(), ldt.getMinute(), ...);
                }

                // Format đầy đủ tất cả trường (CSV-like, 9 trường)
                String info = String.format("%s,%s,%s,%s,%s,%s,%s,%d,%s", 
                    maBan, tenKhachHang, thoiGian, loaiBan, maKhachHang, soDienThoai, diemTichLuyStr, soNguoi, ghiChu);
                list.add(info);
            }

        } catch (SQLException ex) {
            // Cải thiện: Log chi tiết hơn (sử dụng logger nếu có, ví dụ SLF4J)
            System.err.println("Lỗi lấy thông tin phiếu đặt bàn: " + ex.getMessage());
            ex.printStackTrace();
            // Hoặc throw new RuntimeException("Lỗi lấy thông tin phiếu: " + ex.getMessage(), ex);
        }

        return list;
    }

}
