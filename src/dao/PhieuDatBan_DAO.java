package dao;

import java.sql.*;
import entity.PhieuDatBan;
import ConnectDB.ConnectDB;
import java.time.format.DateTimeFormatter;

public class PhieuDatBan_DAO {
    
    private static final DateTimeFormatter SQL_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public boolean themPhieuDatBan(PhieuDatBan pdb, String trangThaiPhieu) {
        String maPDBMoi = taoMaPhieuMoi();
        
        String sql = "INSERT INTO PhieuDatBan (maPhieu, thoiGianBatDau, trangThai, soNguoi, ghiChu, maKhachHang, maBan, maNhanVien, maHoaDon) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"; 
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            // 1. MaPhieu mới
            stmt.setString(1, maPDBMoi);
            
            // 2. ThoiGianBatDau (Chuyển LocalDateTime sang String cho SQL)
            String thoiGianSQL = pdb.getThoiGianBatDau().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            stmt.setString(2, thoiGianSQL);
            
            stmt.setString(3, trangThaiPhieu); 
            
            // 4. SoNguoi
            stmt.setInt(4, pdb.getSoNguoi());
            
            // 5. GhiChu
            stmt.setString(5, pdb.getGhiChu());
            
            // 6. Ma Khach Hang (Xử lý vãng lai)
            String maKH = (pdb.getKhachHang() != null && !pdb.getKhachHang().getMaKhachHang().equals("000")) 
                          ? pdb.getKhachHang().getMaKhachHang() : null;
            if (maKH != null) {
                stmt.setString(6, maKH);
            } else {
                stmt.setNull(6, Types.NVARCHAR);
            }
            
            // 7. Ma Ban
            stmt.setString(7, pdb.getBan().getMaBan()); 
            
            // 8. Ma Nhan Vien
            stmt.setString(8, pdb.getNhanVien().getMaNhanVien()); 
            
            // 9. Ma Hoa Don
            stmt.setString(9, pdb.getHoaDon().getMaHoaDon()); 

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi thêm Phiếu Đặt Bàn: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public String getMaPhieuCuoiCung() {
        String maCuoi = null;
        String sql = "SELECT TOP 1 maPhieu FROM PhieuDatBan ORDER BY maPhieu DESC";
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                maCuoi = rs.getString("maPhieu");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy mã phiếu cuối: " + e.getMessage());
        }
        return maCuoi;
    }

    public String taoMaPhieuMoi() {
        String maCuoi = getMaPhieuCuoiCung();
        if (maCuoi == null) {
            return "PDB001";
        }
        // Lấy phần số (ví dụ: 060 từ PDB060)
        String phanSo = maCuoi.substring(3); 
        int soMoi = Integer.parseInt(phanSo) + 1;
        // Format lại thành PDBxxx
        return String.format("PDB%03d", soMoi); 
    }
}
