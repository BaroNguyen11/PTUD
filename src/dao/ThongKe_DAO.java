package dao;

import ConnectDB.ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;


public class ThongKe_DAO {

    /**
     * Lấy tổng doanh thu theo tháng (ví dụ cho biểu đồ đường)
     * @return Map<tháng, tổng doanh thu>
     */
    public Map<String, Double> getDoanhThuTheoThang() {
        Map<String, Double> dsDoanhThu = new LinkedHashMap<>();

        String sql = """
            SELECT FORMAT(NgayLap, 'MM/yyyy') AS Thang, SUM(TongTien) AS TongDoanhThu
            FROM HoaDon
            GROUP BY FORMAT(NgayLap, 'MM/yyyy')
            ORDER BY MIN(NgayLap)
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String thang = rs.getString("Thang");
                double tong = rs.getDouble("TongDoanhThu");
                dsDoanhThu.put(thang, tong);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsDoanhThu;
    }

    /**
     * Lấy tổng doanh thu hiện tại
     */
    public double getTongDoanhThu() {
        double total = 0;
        String sql = "SELECT SUM(T.ThanhTien) " +
                "AS TongTienTatCaHoaDon " +
                "FROM ( SELECT CH.maHoaDon, (CH.soLuong * MA.giaTien) " +
                "AS ThanhTien " +
                "FROM ChiTietHoaDon CH JOIN MonAn MA " +
                "ON CH.maMonAn = MA.maMonAn ) AS T";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                total = rs.getDouble("Tong");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }
    public double getDoanhThuTrungBinhBan() {
        double avg = 0;
        String sql = """
            SELECT SUM(TongTien) / COUNT(DISTINCT MaBan) AS TB
            FROM HoaDon
            WHERE TongTien IS NOT NULL
        """;
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) avg = rs.getDouble("TB");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return avg;
    }

    // Tỷ lệ thanh toán tiền mặt
    public double getTiLeTienMat() {
        double tile = 0;
        String sql = """
            SELECT 
                100.0 * SUM(CASE WHEN PhuongThucThanhToan = N'Tiền mặt' THEN 1 ELSE 0 END) / COUNT(*) AS TiLe
            FROM HoaDon
        """;
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) tile = rs.getDouble("TiLe");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tile;
    }

    // Doanh thu theo ca tối
    public double getDoanhThuCaToi() {
        double total = 0;
        String sql = """
            SELECT SUM(TongTien) AS Tong
            FROM HoaDon
            WHERE DATEPART(HOUR, GioLap) BETWEEN 17 AND 23
        """;
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) total = rs.getDouble("Tong");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    /**
     * Đếm số lượng nhân viên hiện tại
     */
    public int getSoLuongNhanVien() {
        int count = 0;
        String sql = "SELECT COUNT(*) AS SoLuong FROM NhanVien";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt("SoLuong");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    /**
     * Đếm số lượng khách hàng
     */
    public int getSoLuongKhachHang() {
        int count = 0;
        String sql = "SELECT COUNT(*) AS SoLuong FROM KhachHang";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt("SoLuong");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }
}
