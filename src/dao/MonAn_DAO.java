package dao;

import ConnectDB.ConnectDB;
import entity.MonAn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MonAn_DAO {

    public List<MonAn> getAllMonAn() {
        List<MonAn> dsMonAn = new ArrayList<>();
        String sql = "SELECT * FROM MonAn";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                MonAn mon = new MonAn(
                    rs.getString("maMonAn"),
                    rs.getString("tenMonAn"),
                    rs.getString("loaiMon"),
                    rs.getDouble("giaTien"),
                    rs.getString("moTa")
                    // Thêm các trường khác nếu entity MonAn có (ví dụ: hình ảnh)
                );
                dsMonAn.add(mon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsMonAn;
    }

    public List<MonAn> getMonAnByLoai(String loaiMon) {
        List<MonAn> dsMonAn = new ArrayList<>();
        String sql = "SELECT * FROM MonAn WHERE loaiMon = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, loaiMon);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MonAn mon = new MonAn(
                        rs.getString("maMonAn"),
                        rs.getString("tenMonAn"),
                        rs.getString("loaiMon"),
                        rs.getDouble("giaTien"),
                        rs.getString("moTa")
                        // Thêm các trường khác nếu có
                    );
                    dsMonAn.add(mon);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsMonAn;
    }
}