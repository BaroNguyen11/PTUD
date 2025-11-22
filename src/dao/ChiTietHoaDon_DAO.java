package dao;

import java.sql.*;
import entity.ChiTietHoaDon; 
import ConnectDB.ConnectDB;

public class ChiTietHoaDon_DAO {

    public boolean themChiTietHoaDon(ChiTietHoaDon cthd) {
        String sql = "INSERT INTO ChiTietHoaDon (maHoaDon, maMonAn, soLuong) "
                   + "VALUES (?, ?, ?)"; 
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            // 1. Ma Hoa Don
            stmt.setString(1, cthd.getHoaDon().getMaHoaDon()); 
            
            // 2. Ma Mon An
            stmt.setString(2, cthd.getMonAn().getMaMonAn());
            
            // 3. So Luong
            stmt.setInt(3, cthd.getSoLuong());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi thêm Chi Tiết Hóa Đơn: " + e.getMessage());
            e.printStackTrace(); 
            return false;
        }
    }
}
