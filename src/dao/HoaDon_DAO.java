package dao;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import entity.HoaDon;
import ConnectDB.ConnectDB;
import entity.KhachHang;
import entity.NhanVien;

public class HoaDon_DAO {

    public String themHoaDon(HoaDon hd) {
        // 1. Tự sinh mã mới
        LocalDate ngaybatdau = hd.getNgayTao().toLocalDate();
        String maHDMoi = taoMaHoaDonMoi(ngaybatdau);

        String sql = "INSERT INTO HoaDon (maHoaDon, ngayTao, trangThai, maKhachHang, maNhanVien) "
                + "VALUES (?, GETDATE(), N'Chưa thanh toán', ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            // Gán Mã Hóa đơn mới
            stmt.setString(1, maHDMoi);

            // 2. Ma Khach Hang
            String maKH = (hd.getKhachHang() != null && !hd.getKhachHang().getMaKhachHang().equals("000"))
                    ? hd.getKhachHang().getMaKhachHang() : null;
            if (maKH != null) {
                stmt.setString(2, maKH);
            } else {
                stmt.setNull(2, Types.NVARCHAR);
            }

            // 3. Ma Nhan Vien
            String maNV = (hd.getNhanVien() != null) ? hd.getNhanVien().getMaNhanVien() : "NV001";
            stmt.setString(3, maNV);

            // Thực thi lệnh INSERT
            if (stmt.executeUpdate() > 0) {
                return maHDMoi; // Trả về mã vừa tạo
            }
        } catch (SQLException e) {
            System.err.println("LỖI SQL KHI TẠO HÓA ĐƠN: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public String getMaHoaDonCuoiCung() {
        String maCuoi = null;
        String sql = "SELECT TOP 1 maHoaDon FROM HoaDon ORDER BY maHoaDon DESC";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                maCuoi = rs.getString("maHoaDon");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy mã hóa đơn cuối: " + e.getMessage());
        }
        return maCuoi;
    }
    public String taoMaHoaDonMoi(LocalDate ngayLap) {
        // 1. Định dạng ngày: ddMMyyyy
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy");
        String ngayFormat = ngayLap.format(dtf); // Ví dụ: "18122025"

        // 2. Định dạng tiền tố mã: HD-{ngay}-
        // "HD-" (3 ký tự) + "18122025" (8 ký tự) + "-" (1 ký tự) = 12 ký tự
        String tienToMoi = "HD-" + ngayFormat + "-"; // Ví dụ: "HD-18122025-"

        // 3. Lấy mã cuối cùng TRONG NGÀY ĐÓ từ CSDL
        // Bạn cần viết thêm hàm này trong DAO, query theo LIKE 'HD-ddMMyyyy-%'
        String maCuoi = getMaHoaDonCuoiCungTheoNgay(ngayLap);

        if (maCuoi == null || maCuoi.isEmpty()) {
            // Trường hợp 1: Chưa có hóa đơn nào trong ngày này -> Bắt đầu 001
            return tienToMoi + "001";
        }

        // Trường hợp 2: Đã có hóa đơn -> Tăng số thứ tự
        try {
            // Lấy phần số thứ tự ở cuối
            // Cấu trúc: HD-18122025-001 (Độ dài tiền tố là 12)
            // Cắt từ index 12 trở đi để lấy "001"
            String phanSo = maCuoi.substring(12);
            int soMoi = Integer.parseInt(phanSo) + 1;

            // Format lại thành 3 chữ số (ví dụ: 1 -> 001)
            return tienToMoi + String.format("%03d", soMoi);

        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            System.err.println("Lỗi format mã hóa đơn cũ: " + maCuoi);
            return tienToMoi + "001"; // Fallback an toàn
        }
    }
    public String getMaHoaDonCuoiCungTheoNgay(LocalDate ngay) {
        String maHD = null;


        // Tạo tiền tố tìm kiếm: HD-ddMMyyyy-
        String datePart = ngay.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        String pattern = "HD-" + datePart + "-%"; // Ví dụ: HD-18122025-%

        // Lấy mã lớn nhất khớp với pattern này
        String sql = "SELECT TOP 1 maHoaDon FROM HoaDon WHERE maHoaDon LIKE ? ORDER BY maHoaDon DESC";

        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, pattern);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                maHD = rs.getString("maHoaDon");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maHD;
    }
}