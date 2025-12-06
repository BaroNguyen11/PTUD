package dao;

import ConnectDB.ConnectDB;
import entity.PhieuDatBan;
import entity.KhachHang;
import entity.BanAn;
import entity.NhanVien;
import entity.HoaDon;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;



public class CheckIn_DAO {
    
    public boolean capNhatTrangThaiBan(String maBan, String trangThai) {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = ConnectDB.getConnection();

            String sql = "UPDATE BanAn SET trangThai = ? WHERE maBan = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, trangThai);
            ps.setString(2, maBan);

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;   // true nếu update thành công

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        } finally {
            try {
                if (ps != null) ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public PhieuDatBan timPhieuDatBanTheoBanTrongNgay(String maBan) {
        PhieuDatBan phieu = null;

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = ConnectDB.getConnection();

            String sql = """
                SELECT maPhieu, maBan, ghiChu, maKhachHang, thoiGianBatDau, soNguoi, trangThai, maNhanVien, maHoaDon, maKhachHang
                FROM PhieuDatBan
                WHERE maBan = ? 
                  AND CAST(ngayDat AS DATE) = CAST(GETDATE() AS DATE)
            """;

            stmt = con.prepareStatement(sql);
            stmt.setString(1, maBan);

            rs = stmt.executeQuery();

            KhachHang kh = new KhachHang();
            kh.setMaKhachHang(rs.getString("maKhachHang"));
            
            HoaDon hd = new HoaDon();
            hd.setMaHoaDon(rs.getString("maHoaDon"));
            
            BanAn ban = new BanAn();
            ban.setMaBan(rs.getString("maBan"));
            
            NhanVien nv = new  NhanVien();
            nv.setMaNhanVien(rs.getString("maNhanVien"));
            
            if (rs.next()) {
                phieu = new PhieuDatBan(
                    rs.getString("maPhieu"),
                    rs.getTimestamp("thoiGianBatDau").toLocalDateTime(),
                    rs.getString("trangThai"),
                    rs.getInt("soLuongNguoi"),
                    rs.getString("ghiChu"),
                    kh,
                    ban,
                    nv,
                    hd
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return phieu;
    }
    
    
    public static List<PhieuDatBan> getPhieuDatBanTheoHoaDonVaNgay(String maHoaDon, LocalDate ngay) {
        List<PhieuDatBan> ds = new ArrayList<>();

        String sql = """
            SELECT * 
            FROM PhieuDatBan 
            WHERE maHoaDon = ? 
              AND CAST(thoiGianBatDau AS DATE) = ?
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maHoaDon);
            ps.setDate(2, java.sql.Date.valueOf(ngay));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

            		KhachHang kh = new KhachHang();
                kh.setMaKhachHang(rs.getString("maKhachHang"));
                
                HoaDon hd = new HoaDon();
                hd.setMaHoaDon(rs.getString("maHoaDon"));
                
                BanAn ban = new BanAn();
                ban.setMaBan(rs.getString("maBan"));
                
                NhanVien nv = new  NhanVien();
                nv.setMaNhanVien(rs.getString("maNhanVien"));
                
                PhieuDatBan p = new PhieuDatBan(
                    rs.getString("maPhieu"),
                    rs.getTimestamp("thoiGianBatDau").toLocalDateTime(),
                    rs.getString("trangThai"),
                    rs.getInt("soNguoi"),
                    rs.getString("ghiChu"),
                    kh, ban, nv, hd
                );

                ds.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }
    
    public static boolean capNhatTrangThaiPhieuDatBan(String maPhieu, String trangThaiMoi) {
        String sql = "UPDATE PhieuDatBan SET trangThai = ? WHERE maPhieu = ?";
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, trangThaiMoi);  // ví dụ: "Đang dùng"
            stmt.setString(2, maPhieu);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    

}