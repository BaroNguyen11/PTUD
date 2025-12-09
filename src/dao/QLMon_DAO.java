package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ConnectDB.ConnectDB;
import entity.MonAn;

public class QLMon_DAO {

	public List<MonAn> getDanhSachMonAn() {
	    List<MonAn> ds = new ArrayList<>();

	    String sql = "SELECT maMonAn, tenMonAn, loaiMon, giaTien, moTa, hinhAnh FROM MonAn";

	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement stmt = con.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            String ma = rs.getString("maMonAn");
	            String ten = rs.getString("tenMonAn");
	            String loai = rs.getString("loaiMon");
	            double gia = rs.getDouble("giaTien");
	            String moTa = rs.getString("moTa");
	            String hinhAnh = rs.getString("hinhAnh");

	            ds.add(new MonAn(ma, ten, loai, gia, moTa, hinhAnh));
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return ds;
	}
	
	public boolean insertMon(MonAn mon) {
        String sql = "INSERT INTO MonAn (maMonAn, tenMonAn, loaiMon, giaTien, moTa, hinhAnh) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn =ConnectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, mon.getMaMonAn());
            pstmt.setNString(2, mon.getTenMonAn());  // NString cho Unicode (tiếng Việt)
            pstmt.setNString(3, mon.getLoaiMon());
            pstmt.setDouble(4, mon.getGiaTien());
            pstmt.setNString(5, mon.getMoTa());
            pstmt.setString(6, mon.getHinhAnh());  // Relative path từ Supabase

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;  // Thành công nếu ảnh hưởng 1 row

        } catch (SQLException e) {
            System.err.println("❌ Lỗi INSERT MonAn: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
	
	public String taoMaMonAn() {
	    String sql = "SELECT MAX(maMonAn) AS maxMa FROM MonAn";
	    String prefix = "MA";
	    int nextNumber = 1;  // Default nếu bảng rỗng

	    try (Connection conn = ConnectDB.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql);
	         ResultSet rs = pstmt.executeQuery()) {

	        if (rs.next()) {
	            String maxMa = rs.getString("maxMa");
	            if (maxMa != null && maxMa.startsWith(prefix)) {
	                // Trích xuất số từ mã (e.g., "MA049" → 49)
	                Pattern pattern = Pattern.compile(prefix + "(\\d+)");
	                Matcher matcher = pattern.matcher(maxMa);
	                if (matcher.find()) {
	                    nextNumber = Integer.parseInt(matcher.group(1)) + 1;
	                }
	            }
	        }

	        // Format: MA + số pad 3 chữ số (MA001, MA010, MA100...)
	        return prefix + String.format("%03d", nextNumber);

	    } catch (SQLException e) {
	        System.err.println("❌ Lỗi tạo mã mới: " + e.getMessage());
	        e.printStackTrace();
	        return prefix + String.format("%03d", nextNumber);  // Fallback
	    }
	}
	
	/**
	 * Cập nhật chỉ tên, loại, mô tả và hình ảnh của món ăn
	 * @param mon Đối tượng MonAn (chỉ cần maMonAn làm khóa, và các trường cần update)
	 * @return true nếu UPDATE thành công
	 */
	public boolean updateMon(MonAn mon) {
	    if (mon == null || mon.getMaMonAn() == null || mon.getMaMonAn().trim().isEmpty()) {
	        return false;
	    }

	    String sql = "UPDATE MonAn SET tenMonAn = ?, loaiMon = ?, moTa = ?, hinhAnh = ? WHERE maMonAn = ?";
	    
	    try (Connection conn = ConnectDB.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setNString(1, mon.getTenMonAn());
	        pstmt.setNString(2, mon.getLoaiMon());
	        pstmt.setNString(3, mon.getMoTa());
	        pstmt.setString(4, mon.getHinhAnh());
	        pstmt.setString(5, mon.getMaMonAn());

	        int rowsAffected = pstmt.executeUpdate();
	        return rowsAffected > 0;

	    } catch (SQLException e) {
	        System.err.println("❌ Lỗi UPDATE MonAn: " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}

}