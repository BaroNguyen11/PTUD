package dao;

import ConnectDB.ConnectDB;
import entity.KhachHang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KhachHang_DAO {

    public KhachHang getKhachHangById(String maKH) {
        KhachHang kh = null;
        String sql = "SELECT * FROM KhachHang WHERE maKhachHang = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                     // Đảm bảo entity KhachHang có constructor này
                    kh = new KhachHang(
                        rs.getString("maKhachHang"),
                        rs.getString("tenKhachHang"),
                        rs.getString("soDienThoai"),
                        rs.getDouble("diemTichLuy")
                        // Thêm các trường khác nếu có
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kh;
    }
     // Thêm các hàm khác nếu cần (getAllKhachHang, addKhachHang, ...)
}