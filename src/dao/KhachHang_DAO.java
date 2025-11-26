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
    public KhachHang getKhachHangBySdt(String sdt) {
        KhachHang kh = null;
        // Sửa câu lệnh SQL: tìm theo cột soDienThoai
        String sql = "SELECT * FROM KhachHang WHERE soDienThoai = ?";
        
        // Đảm bảo ConnectDB và KhachHang đã được import
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            // Gán tham số số điện thoại vào câu lệnh SQL
            ps.setString(1, sdt);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Khởi tạo đối tượng KhachHang từ dữ liệu ResultSet
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
            System.err.println("Lỗi truy vấn SQL khi tìm khách hàng theo SĐT: " + sdt);
            e.printStackTrace();
        }
        return kh;
    }

    public String getMaKhachHangCuoiCung() {
        String maCuoi = null;
        String sql = "SELECT TOP 1 maKhachHang FROM KhachHang WHERE maKhachHang LIKE 'KH%' ORDER BY maKhachHang DESC";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                maCuoi = rs.getString("maKhachHang");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy mã KH cuối: " + e.getMessage());
        }
        return maCuoi;
    }

    public String taoMaKhachHangMoi() {
        String maCuoi = getMaKhachHangCuoiCung();
        if (maCuoi == null) {
            return "KH001"; 
        }
        
        try {
            String phanSo = maCuoi.substring(2); 
            int soMoi = Integer.parseInt(phanSo) + 1;
            
            return String.format("KH%03d", soMoi); 
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            System.err.println("Lỗi định dạng mã KH cuối: " + maCuoi);
            return "KH001";
        }
    }
    public boolean themKhachHangMoi(KhachHang kh) {
    	
    	String maKHMoi = taoMaKhachHangMoi();
        
        String sql = "INSERT INTO KhachHang (maKhachHang, tenKhachHang, soDienThoai, diemTichLuy) VALUES (?, ?, ?, ?)";
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
        	stmt.setString(1, maKHMoi);
            stmt.setString(2, kh.getTenKhachHang());
            stmt.setString(3, kh.getSoDienThoai());
            
            if (kh.getDiemTichLuy() == 0.0) {
            	stmt.setDouble(4, 0.0);
            } else {
                stmt.setDouble(4, kh.getDiemTichLuy());
            }

            if (stmt.executeUpdate() > 0) {
                kh.setMaKhachHang(maKHMoi); 
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi thêm khách hàng mới: " + e.getMessage());
            return false;
        }
    }
    
}