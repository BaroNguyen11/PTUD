package dao;

import java.sql.*;
import entity.HoaDon; 
import ConnectDB.ConnectDB;
import entity.KhachHang; 
import entity.NhanVien;

public class HoaDon_DAO {
    
	public String themHoaDon(HoaDon hd) { 
	    // 1. Tự sinh mã mới
	    String maHDMoi = taoMaHoaDonMoi(); 
	    
	    String sql = "INSERT INTO HoaDon (maHoaDon, ngayTao, trangThai, maKhachHang, maNhanVien) " 
	               + "VALUES (?, GETDATE(), N'Chưa thanh toán', ?, ?)"; 
	    
	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement stmt = con.prepareStatement(sql)) {
	        
	        // Gán Mã Hóa đơn mới
	        stmt.setString(1, maHDMoi);
	        
	        // 2. Ma Khach Hang
	        String maKH = (hd.getKhachHang() != null && !hd.getKhachHang().getMaKhachHang().equals("000")) 
	                        ? hd.getKhachHang().getMaKhachHang() : null;
	        if (maKH != null) {
	            stmt.setString(2, maKH);
	        } else {
	            stmt.setNull(2, Types.NVARCHAR);
	        }

	        // 3. Ma Nhan Vien
	        String maNV = (hd.getNhanVien() != null) ? hd.getNhanVien().getMaNhanVien() : "NV001";
	        stmt.setString(3, maNV);
	        
	        // Thực thi lệnh INSERT 
	        if (stmt.executeUpdate() > 0) {
	            return maHDMoi; // Trả về mã vừa tạo
	        }
	    } catch (SQLException e) {
	        System.err.println("LỖI SQL KHI TẠO HÓA ĐƠN: " + e.getMessage()); 
	        e.printStackTrace(); 
	    }
	    return null; 
	}
	
    public String getMaHoaDonCuoiCung() {
        String maCuoi = null;
        String sql = "SELECT TOP 1 maHoaDon FROM HoaDon ORDER BY maHoaDon DESC";
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                maCuoi = rs.getString("maHoaDon");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy mã hóa đơn cuối: " + e.getMessage());
        }
        return maCuoi;
    }
    public String taoMaHoaDonMoi() {
        String maCuoi = getMaHoaDonCuoiCung();
        if (maCuoi == null) {
            return "HD001"; // Mã khởi đầu nếu chưa có hóa đơn nào
        }
        String phanSo = maCuoi.substring(2); 
        int soMoi = Integer.parseInt(phanSo) + 1;
        // Format lại thành HDxxx
        return String.format("HD%03d", soMoi); 
    }
    
}
