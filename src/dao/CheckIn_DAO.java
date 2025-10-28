package dao;

import ConnectDB.ConnectDB;
import entity.PhieuDatBan;
import entity.KhachHang;
import entity.BanAn;
import entity.NhanVien;
import entity.HoaDon;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;



public class CheckIn_DAO {

    // Lấy phiếu theo mã
    public String layThongTinDatBanTheoSDT(String sdt, LocalDate ngayDat) {
        String info = "N/A";

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
                p.ghiChu,
                b.viTri, 
                p.maPhieu
            FROM PhieuDatBan p
            JOIN BanAn b ON p.maBan = b.maBan
            JOIN KhachHang k ON p.maKhachHang = k.maKhachHang
            WHERE p.trangThai = N'Đã đặt' AND k.soDienThoai = ? AND CAST(p.thoiGianBatDau AS DATE) = ?
            ORDER BY p.thoiGianBatDau DESC
        """;

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sdt);
            ps.setDate(2, Date.valueOf(ngayDat));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String maBan = safeString(rs.getString("maBan"));
                    String tenKhachHang = safeString(rs.getString("tenKhachHang"));
                    String loaiBan = safeString(rs.getString("loai"));
                    String maKhachHang = safeString(rs.getString("maKhachHang"));
                    String soDienThoai = safeString(rs.getString("soDienThoai"));
                    String ghiChu = safeString(rs.getString("ghiChu"));

                    BigDecimal diemTichLuyBD = rs.getBigDecimal("diemTichLuy");
                    String diemTichLuyStr = (diemTichLuyBD != null)
                            ? String.format("%.2f", diemTichLuyBD.doubleValue())
                            : "0.00";

                    int soNguoi = rs.getInt("soNguoi");
                    if (rs.wasNull()) soNguoi = 0;

                    // Định dạng thời gian
                    Timestamp ts = rs.getTimestamp("thoiGianBatDau");
                    String thoiGian = "N/A";
                    if (ts != null) {
                        LocalDateTime ldt = ts.toLocalDateTime();
                        thoiGian = String.format("%02dh%02d %02d/%02d/%04d",
                                ldt.getHour(),
                                ldt.getMinute(),
                                ldt.getDayOfMonth(),
                                ldt.getMonthValue(),
                                ldt.getYear());
                    }

                    String viTri = rs.getString("viTri");
                    String maPhieu = rs.getString("maPhieu");
                    info = String.format("%s,%s,%s,%s,%s,%s,%s,%d,%s,%s,%s",
                            maBan, tenKhachHang, thoiGian, loaiBan, maKhachHang,
                            soDienThoai, diemTichLuyStr, soNguoi, ghiChu, viTri, maPhieu);
                }
            }

        } catch (SQLException ex) {
            System.err.println("[Lỗi] Không thể lấy thông tin phiếu đặt bàn: " + ex.getMessage());
            ex.printStackTrace();
        }

        return info;
    }

    private String safeString(String s) {
        return (s != null && !s.isBlank()) ? s : "N/A";
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


    public List<String> getThongTinPhieuDatBan(LocalDate ngay) {
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
                p.ghiChu,
                b.viTri,
                p.maPhieu
            FROM PhieuDatBan p
            JOIN BanAn b ON p.maBan = b.maBan
            JOIN KhachHang k ON p.maKhachHang = k.maKhachHang
            WHERE p.trangThai = N'Đã đặt'
              AND CAST(p.thoiGianBatDau AS DATE) = ?
            ORDER BY p.thoiGianBatDau DESC
        """;

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(ngay));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Xử lý từng cột, tránh null
                    String maBan = safeString(rs.getString("maBan"));
                    String tenKhachHang = safeString(rs.getString("tenKhachHang"));
                    String loaiBan = safeString(rs.getString("loai"));
                    String maKhachHang = safeString(rs.getString("maKhachHang"));
                    String soDienThoai = safeString(rs.getString("soDienThoai"));
                    String ghiChu = safeString(rs.getString("ghiChu"));
                    String viTri = safeString(rs.getString("viTri"));
                    String maPhieu = safeString(rs.getString("maPhieu"));

                    BigDecimal diemTichLuyBD = rs.getBigDecimal("diemTichLuy");
                    String diemTichLuyStr = (diemTichLuyBD != null)
                            ? String.format("%.2f", diemTichLuyBD.doubleValue())
                            : "0.00";

                    int soNguoi = rs.getInt("soNguoi");
                    if (rs.wasNull()) soNguoi = 0;

                    // Xử lý thời gian
                    String thoiGian = "N/A";
                    Timestamp ts = rs.getTimestamp("thoiGianBatDau");
                    if (ts != null) {
                        LocalDateTime ldt = ts.toLocalDateTime();
                        thoiGian = String.format("%02dh%02d %02d/%02d/%04d",
                                ldt.getHour(),
                                ldt.getMinute(),
                                ldt.getDayOfMonth(),
                                ldt.getMonthValue(),
                                ldt.getYear());
                    }

                    // Format thành 1 chuỗi (CSV-like)
                    String info = String.format("%s,%s,%s,%s,%s,%s,%s,%d,%s,%s,%s",
                            maBan, tenKhachHang, thoiGian, loaiBan,
                            maKhachHang, soDienThoai, diemTichLuyStr,
                            soNguoi, ghiChu, viTri, maPhieu);

                    list.add(info);
                }
            }

        } catch (SQLException ex) {
            System.err.println("[Lỗi] Không thể lấy thông tin phiếu đặt bàn: " + ex.getMessage());
            ex.printStackTrace();
        }

        return list;
    }



}