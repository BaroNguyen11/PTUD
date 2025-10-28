package dao;

import ConnectDB.ConnectDB; // Đảm bảo import đúng package ConnectDB
import entity.BanAn;
import entity.LoaiBan;
import entity.TrangThai;
import entity.ViTri;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BanAn_DAO {

    public List<BanAn> getAllBanAn() {
        List<BanAn> dsBanAn = new ArrayList<>();
        String sql = "SELECT * FROM BanAn";
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {

                String maBan = rs.getString("maBan");

                LoaiBan loai = LoaiBan.fromString(rs.getString("loai"));
                TrangThai trangThai = TrangThai.fromString(rs.getString("trangThai"));
                ViTri viTri = ViTri.fromString(rs.getString("viTri"));
                
                BanAn ban = new BanAn(maBan, loai, trangThai, viTri);
                dsBanAn.add(ban);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsBanAn;
    }

    public List<BanAn> getBanAnTheoViTri(ViTri viTri) {
        List<BanAn> dsBanAn = new ArrayList<>();
        String sql = "SELECT * FROM BanAn WHERE viTri = ?"; 
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, viTri.getTenViTri()); 

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String maBan = rs.getString("maBan");
                    
                    LoaiBan loai = LoaiBan.fromString(rs.getString("loai"));
                    TrangThai trangThai = TrangThai.fromString(rs.getString("trangThai"));
                    ViTri vt = ViTri.fromString(rs.getString("viTri")); 

                    BanAn ban = new BanAn(maBan, loai, trangThai, vt);
                    dsBanAn.add(ban);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsBanAn;
    }
    
    public boolean updateTrangThaiBan(BanAn ban, TrangThai trangThaiMoi) {

        String sql = "UPDATE BanAn SET trangThai = ? WHERE maBan = ?"; 
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            

            ps.setString(1, trangThaiMoi.getThongTin()); 
            ps.setString(2, ban.getMaBan());
            
            int n = ps.executeUpdate();
            return n > 0; 
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
    
