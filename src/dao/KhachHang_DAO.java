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
                    kh = new KhachHang(
                        rs.getString("maKhachHang"),
                        rs.getString("tenKhachHang"),
                        rs.getString("soDienThoai"),
                        rs.getDouble("diemTichLuy")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kh;
    }
    public KhachHang getKhachHangBySdt(String sdt) {
        KhachHang kh = null;
        String sql = "SELECT * FROM KhachHang WHERE soDienThoai = ?";
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, sdt);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    kh = new KhachHang(
                        rs.getString("maKhachHang"),
                        rs.getString("tenKhachHang"),
                        rs.getString("soDienThoai"),
                        rs.getDouble("diemTichLuy")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn SQL khi tìm khách hàng theo SĐT: " + sdt);
            e.printStackTrace();
        }
        return kh;
    }
}
