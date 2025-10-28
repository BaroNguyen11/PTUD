package dao;

import ConnectDB.ConnectDB;
import entity.KhachHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhachHang_DAO {

    // Lấy toàn bộ danh sách khách hàng
    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang ORDER BY maKhachHang";

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                KhachHang kh = new KhachHang(
                        rs.getString("maKhachHang"),
                        rs.getString("tenKhachHang"),
                        rs.getString("soDienThoai"),
                        rs.getDouble("diemTichLuy")
                );
                list.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy khách hàng theo mã
    public KhachHang getKhachHangByMa(String maKhachHang) {
        String sql = "SELECT * FROM KhachHang WHERE maKhachHang = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maKhachHang);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new KhachHang(
                        rs.getString("maKhachHang"),
                        rs.getString("tenKhachHang"),
                        rs.getString("soDienThoai"),
                        rs.getDouble("diemTichLuy")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy khách hàng theo số điện thoại
    public KhachHang getKhachHangBySoDienThoai(String soDienThoai) {
        String sql = "SELECT * FROM KhachHang WHERE soDienThoai = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, soDienThoai);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new KhachHang(
                        rs.getString("maKhachHang"),
                        rs.getString("tenKhachHang"),
                        rs.getString("soDienThoai"),
                        rs.getDouble("diemTichLuy")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Tìm kiếm khách hàng theo tên hoặc số điện thoại
    public List<KhachHang> searchKhachHang(String keyword) {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE tenKhachHang LIKE ? OR soDienThoai LIKE ? ORDER BY maKhachHang";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                KhachHang kh = new KhachHang(
                        rs.getString("maKhachHang"),
                        rs.getString("tenKhachHang"),
                        rs.getString("soDienThoai"),
                        rs.getDouble("diemTichLuy")
                );
                list.add(kh);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm khách hàng mới
    public boolean addKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KhachHang (maKhachHang, tenKhachHang, soDienThoai, diemTichLuy) VALUES (?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, kh.getMaKhachHang());
            ps.setString(2, kh.getTenKhachHang());
            ps.setString(3, kh.getSoDienThoai());
            ps.setDouble(4, kh.getDiemTichLuy());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Thêm khách hàng mới với mã tự động
    public boolean addKhachHangAutoID(KhachHang kh) {
        String sql = "INSERT INTO KhachHang (maKhachHang, tenKhachHang, soDienThoai, diemTichLuy) VALUES (?, ?, ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Tự động sinh mã
            String newMa = generateMaKhachHang();
            kh.setMaKhachHang(newMa);

            ps.setString(1, kh.getMaKhachHang());
            ps.setString(2, kh.getTenKhachHang());
            ps.setString(3, kh.getSoDienThoai());
            ps.setDouble(4, kh.getDiemTichLuy());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật thông tin khách hàng
    public boolean updateKhachHang(KhachHang kh) {
        String sql = "UPDATE KhachHang SET tenKhachHang=?, soDienThoai=?, diemTichLuy=? WHERE maKhachHang=?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, kh.getTenKhachHang());
            ps.setString(2, kh.getSoDienThoai());
            ps.setDouble(3, kh.getDiemTichLuy());
            ps.setString(4, kh.getMaKhachHang());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật điểm tích lũy
    public boolean updateDiemTichLuy(String maKhachHang, double diemThem) {
        String sql = "UPDATE KhachHang SET diemTichLuy = diemTichLuy + ? WHERE maKhachHang = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, diemThem);
            ps.setString(2, maKhachHang);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Đặt lại điểm tích lũy
    public boolean resetDiemTichLuy(String maKhachHang) {
        String sql = "UPDATE KhachHang SET diemTichLuy = 0 WHERE maKhachHang = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maKhachHang);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra số điện thoại đã tồn tại chưa (dùng cho thêm mới)
    public boolean isSoDienThoaiExists(String soDienThoai) {
        String sql = "SELECT COUNT(*) FROM KhachHang WHERE soDienThoai = ?";

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

    // Kiểm tra số điện thoại đã tồn tại cho khách hàng khác (dùng cho cập nhật)
    public boolean isSoDienThoaiExistsForOther(String soDienThoai, String maKhachHang) {
        String sql = "SELECT COUNT(*) FROM KhachHang WHERE soDienThoai = ? AND maKhachHang != ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, soDienThoai);
            ps.setString(2, maKhachHang);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Tự động sinh mã khách hàng mới
    public String generateMaKhachHang() {
        String sql = "SELECT MAX(maKhachHang) FROM KhachHang WHERE maKhachHang LIKE 'KH%'";

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String lastMa = rs.getString(1);
                if (lastMa != null && lastMa.matches("KH\\d+")) {
                    int number = Integer.parseInt(lastMa.substring(2)) + 1;
                    return String.format("KH%03d", number);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "KH001"; // Mã mặc định nếu không có khách hàng nào
    }

    // Lấy top khách hàng có điểm tích lũy cao nhất
    public List<KhachHang> getTopKhachHangTheoDiem(int topN) {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT TOP (?) * FROM KhachHang ORDER BY diemTichLuy DESC, maKhachHang";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, topN);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                KhachHang kh = new KhachHang(
                        rs.getString("maKhachHang"),
                        rs.getString("tenKhachHang"),
                        rs.getString("soDienThoai"),
                        rs.getDouble("diemTichLuy")
                );
                list.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Đếm tổng số khách hàng
    public int getTongSoKhachHang() {
        String sql = "SELECT COUNT(*) FROM KhachHang";

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

    // Lấy tổng điểm tích lũy của tất cả khách hàng
    public double getTongDiemTichLuy() {
        String sql = "SELECT SUM(diemTichLuy) FROM KhachHang";

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}