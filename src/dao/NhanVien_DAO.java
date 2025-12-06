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
        String sql = "UPDATE NhanVien SET ngayThoiViec = ? WHERE maNhanVien = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(LocalDate.now()));
            ps.setString(2, maNhanVien);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Tái tuyển nhân viên
    public boolean taiTuyenNhanVien(String maNhanVien) {
        String sql = "UPDATE NhanVien SET ngayThoiViec = NULL WHERE maNhanVien = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maNhanVien);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra số điện thoại đã tồn tại chưa
    public boolean isSoDienThoaiExists(String soDienThoai) {
        String sql = "SELECT COUNT(*) FROM NhanVien WHERE soDienThoai = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, soDienThoai);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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

    // Kiểm tra CCCD đã tồn tại chưa
    public boolean isCCCDExists(String cccd) {
        String sql = "SELECT COUNT(*) FROM NhanVien WHERE CCCD = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cccd);
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

        return String.format("NV%06d", max + 1);
    }


    // Lấy danh sách nhân viên đang làm việc (chưa thôi việc)
    public List<NhanVien> getNhanVienDangLamViec() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE ngayThoiViec IS NULL ORDER BY maNhanVien";

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
                        null
                );
                list.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy danh sách nhân viên đã thôi việc
    public List<NhanVien> getNhanVienDaThoiViec() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE ngayThoiViec IS NOT NULL ORDER BY maNhanVien";

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

    // Đếm tổng số nhân viên
    public int getTongSoNhanVien() {
        String sql = "SELECT COUNT(*) FROM NhanVien";

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean updateNgayThoiViec(String maNhanVien, LocalDate ngayThoiViec) {
        String sql = "UPDATE NhanVien SET ngayThoiViec = ? WHERE maNhanVien = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(ngayThoiViec));
            ps.setString(2, maNhanVien);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Đếm số nhân viên đang làm việc
    public int getSoNhanVienDangLamViec() {
        String sql = "SELECT COUNT(*) FROM NhanVien WHERE ngayThoiViec IS NULL";

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
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

    // Kiểm tra nhân viên có đang làm việc không
    public boolean isNhanVienDangLamViec(String maNhanVien) {
        String sql = "SELECT ngayThoiViec FROM NhanVien WHERE maNhanVien = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maNhanVien);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDate("ngayThoiViec") == null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy thông tin nhân viên kèm trạng thái làm việc
    public NhanVien getNhanVienWithStatus(String maNhanVien) {
        String sql = "SELECT *, " +
                "CASE WHEN ngayThoiViec IS NULL THEN 'Đang làm việc' ELSE 'Đã nghỉ việc' END as trangThai " +
                "FROM NhanVien WHERE maNhanVien = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maNhanVien);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
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
                return nv;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}