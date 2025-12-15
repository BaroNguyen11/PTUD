package dao;

import ConnectDB.ConnectDB;
import entity.NhanVien;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NhanVien_DAO {

    // Lấy toàn bộ danh sách nhân viên
    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien ORDER BY maNhanVien";

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                NhanVien nv = new NhanVien(
                        rs.getString("maNhanVien"),
                        rs.getString("tenNhanVien"),
                        rs.getString("chucVu"),
                        rs.getString("CCCD"),
                        rs.getString("soDienThoai"),
                        rs.getDate("ngaySinh") != null ? rs.getDate("ngaySinh").toLocalDate() : null,
                        rs.getDate("ngayVaoLam") != null ? rs.getDate("ngayVaoLam").toLocalDate() : null,
                        rs.getDate("ngayThoiViec") != null ? rs.getDate("ngayThoiViec").toLocalDate() : null
                );
                list.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy nhân viên theo mã
    public static NhanVien getNhanVienByMa(String maNhanVien) {
        String sql = "SELECT * FROM NhanVien WHERE maNhanVien = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maNhanVien);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new NhanVien(
                        rs.getString("maNhanVien"),
                        rs.getString("tenNhanVien"),
                        rs.getString("chucVu"),
                        rs.getString("CCCD"),
                        rs.getString("soDienThoai"),
                        rs.getDate("ngaySinh") != null ? rs.getDate("ngaySinh").toLocalDate() : null,
                        rs.getDate("ngayVaoLam") != null ? rs.getDate("ngayVaoLam").toLocalDate() : null,
                        rs.getDate("ngayThoiViec") != null ? rs.getDate("ngayThoiViec").toLocalDate() : null
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Tìm kiếm nhân viên theo mã hoặc tên
    // Tìm kiếm nhân viên theo mã, tên hoặc số điện thoại
    public List<NhanVien> searchNhanVien(String keyword) {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE maNhanVien LIKE ? OR tenNhanVien LIKE ? OR soDienThoai LIKE ? ORDER BY maNhanVien";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                NhanVien nv = new NhanVien(
                        rs.getString("maNhanVien"),
                        rs.getString("tenNhanVien"),
                        rs.getString("chucVu"),
                        rs.getString("CCCD"),
                        rs.getString("soDienThoai"),
                        rs.getDate("ngaySinh") != null ? rs.getDate("ngaySinh").toLocalDate() : null,
                        rs.getDate("ngayVaoLam") != null ? rs.getDate("ngayVaoLam").toLocalDate() : null,
                        rs.getDate("ngayThoiViec") != null ? rs.getDate("ngayThoiViec").toLocalDate() : null
                );
                list.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm nhân viên mới
    public boolean addNhanVien(NhanVien nv) {
        String sql = "INSERT INTO NhanVien (maNhanVien, tenNhanVien, chucVu, CCCD, soDienThoai, ngaySinh, ngayVaoLam, ngayThoiViec) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nv.getMaNhanVien());
            ps.setString(2, nv.getTenNhanVien());
            ps.setString(3, nv.getChucVu());
            ps.setString(4, nv.getCCCD());
            ps.setString(5, nv.getSoDienThoai());
            ps.setDate(6, nv.getNgaySinh() != null ? Date.valueOf(nv.getNgaySinh()) : null);
            ps.setDate(7, nv.getNgayVaoLam() != null ? Date.valueOf(nv.getNgayVaoLam()) : null);
            ps.setDate(8, nv.getNgayThoiViec() != null ? Date.valueOf(nv.getNgayThoiViec()) : null);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật thông tin nhân viên
    public boolean updateNhanVien(NhanVien nv) {
        String sql = "UPDATE NhanVien SET tenNhanVien=?, chucVu=?, CCCD=?, soDienThoai=?, ngaySinh=?, ngayVaoLam=?, ngayThoiViec=? WHERE maNhanVien=?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nv.getTenNhanVien());
            ps.setString(2, nv.getChucVu());
            ps.setString(3, nv.getCCCD());
            ps.setString(4, nv.getSoDienThoai());
            ps.setDate(5, nv.getNgaySinh() != null ? Date.valueOf(nv.getNgaySinh()) : null);
            ps.setDate(6, nv.getNgayVaoLam() != null ? Date.valueOf(nv.getNgayVaoLam()) : null);
            ps.setDate(7, nv.getNgayThoiViec() != null ? Date.valueOf(nv.getNgayThoiViec()) : null);
            ps.setString(8, nv.getMaNhanVien());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cho nhân viên thôi việc
    public boolean thoiViecNhanVien(String maNhanVien) {
        // 1. Cập nhật ngày thôi việc cho nhân viên
        String sqlNhanVien = "UPDATE NhanVien SET ngayThoiViec = ? WHERE maNhanVien = ?";
        // 2. Khóa tài khoản của nhân viên đó (trangThaiHoatDong = 0/false)
        String sqlTaiKhoan = "UPDATE TaiKhoan SET trangThaiHoatDong = 0 WHERE maNhanVien = ?";

        Connection con = null;
        PreparedStatement psNV = null;
        PreparedStatement psTK = null;

        try {
            con = ConnectDB.getConnection();
            // Tắt auto-commit để bắt đầu transaction
            con.setAutoCommit(false);

            // --- Cập nhật Nhân viên ---
            psNV = con.prepareStatement(sqlNhanVien);
            psNV.setDate(1, Date.valueOf(LocalDate.now()));
            psNV.setString(2, maNhanVien);
            int resultNV = psNV.executeUpdate();

            // --- Cập nhật Tài khoản ---
            psTK = con.prepareStatement(sqlTaiKhoan);
            psTK.setString(1, maNhanVien);
            int resultTK = psTK.executeUpdate();
            // Lưu ý: resultTK có thể = 0 nếu nhân viên này chưa có tài khoản, điều này vẫn chấp nhận được.

            if (resultNV > 0) {
                con.commit(); // Xác nhận giao dịch thành công
                return true;
            } else {
                con.rollback(); // Hoàn tác nếu cập nhật nhân viên thất bại
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) con.rollback(); // Hoàn tác nếu có lỗi
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            // Đóng resources thủ công vì không dùng try-with-resources để control transaction
            try {
                if (psNV != null) psNV.close();
                if (psTK != null) psTK.close();
                if (con != null) {
                    con.setAutoCommit(true); // Trả lại trạng thái mặc định
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Tái tuyển nhân viên
    public boolean taiTuyenNhanVien(String maNhanVien) {
        // 1. Cập nhật ngày thôi việc về NULL (tức là đang làm việc)
        String sqlNhanVien = "UPDATE NhanVien SET ngayThoiViec = NULL WHERE maNhanVien = ?";
        // 2. Mở khóa tài khoản (trangThaiHoatDong = 1 hoặc true)
        String sqlTaiKhoan = "UPDATE TaiKhoan SET trangThaiHoatDong = 1 WHERE maNhanVien = ?";

        Connection con = null;
        PreparedStatement psNV = null;
        PreparedStatement psTK = null;

        try {
            con = ConnectDB.getConnection();
            con.setAutoCommit(false); // Bắt đầu Transaction

            // --- Bước 1: Cập nhật Nhân viên ---
            psNV = con.prepareStatement(sqlNhanVien);
            psNV.setString(1, maNhanVien);
            int resultNV = psNV.executeUpdate();

            // --- Bước 2: Cập nhật Tài khoản ---
            psTK = con.prepareStatement(sqlTaiKhoan);
            psTK.setString(1, maNhanVien);
            psTK.executeUpdate();
            // Không cần kiểm tra kết quả update tài khoản, vì có thể nhân viên này chưa có tài khoản.
            // Chỉ cần update nhân viên thành công là được.

            if (resultNV > 0) {
                con.commit(); // Xác nhận thành công cả 2
                return true;
            } else {
                con.rollback(); // Hoàn tác nếu không tìm thấy nhân viên
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) con.rollback(); // Gặp lỗi thì hoàn tác
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            // Đóng kết nối
            try {
                if (psNV != null) psNV.close();
                if (psTK != null) psTK.close();
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    // Kiểm tra số điện thoại đã tồn tại cho nhân viên khác
    public boolean isSoDienThoaiExistsForOther(String soDienThoai, String maNhanVien) {
        String sql = "SELECT COUNT(*) FROM NhanVien WHERE soDienThoai = ? AND maNhanVien != ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, soDienThoai);
            ps.setString(2, maNhanVien);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra CCCD đã tồn tại cho nhân viên khác
    public boolean isCCCDExistsForOther(String cccd, String maNhanVien) {
        String sql = "SELECT COUNT(*) FROM NhanVien WHERE CCCD = ? AND maNhanVien != ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cccd);
            ps.setString(2, maNhanVien);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Tự động sinh mã nhân viên mới
    public String generateMaNhanVien() {
        String sql = "SELECT maNhanVien FROM NhanVien WHERE maNhanVien LIKE 'NV%'";

        int max = 0;
        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String ma = rs.getString("maNhanVien"); // NV000011
                try {
                    int num = Integer.parseInt(ma.substring(2)); // 11
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return String.format("NV%03d", max + 1);
    }


    // Lấy ngày thôi việc của nhân viên theo mã
    public LocalDate getNgayThoiViec(String maNhanVien) {
        String sql = "SELECT ngayThoiViec FROM NhanVien WHERE maNhanVien = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maNhanVien);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Date ngayThoiViec = rs.getDate("ngayThoiViec");
                return ngayThoiViec != null ? ngayThoiViec.toLocalDate() : null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}