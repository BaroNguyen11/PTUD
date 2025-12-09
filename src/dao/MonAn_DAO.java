package dao;

import ConnectDB.ConnectDB;
import entity.MonAn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MonAn_DAO {


	public static MonAn getMonAnByMa(String maMonAn) {
	    MonAn mon = null;
	    String sql = "SELECT * FROM MonAn WHERE maMonAn = ?";

	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, maMonAn);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                mon = new MonAn(
	                        rs.getString("maMonAn"),
	                        rs.getString("tenMonAn"),
	                        rs.getString("loaiMon"),
	                        rs.getDouble("giaTien"),
	                        rs.getString("moTa"),
	                        rs.getString("hinhAnh")
	                );
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return mon;
	}
	
	public static String getMaMonByTen(String tenMon) {
	    String sql = "SELECT maMonAn FROM MonAn WHERE tenMonAn = ?";
	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        
	        ps.setString(1, tenMon);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return rs.getString("maMonAn");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}


        // ✅ Sửa method getAllMonAn
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
                            rs.getString("moTa"),
                            rs.getString("hinhAnh") // ✅ THÊM TRƯỜNG HÌNH ẢNH
                    );
                    dsMonAn.add(mon);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return dsMonAn;
        }

        // ✅ Sửa method getMonAnByLoai (nếu có)
        public List<MonAn> getMonAnByLoai(String loaiMon) {
            List<MonAn> dsMonAn = new ArrayList<>();
            String sql = "SELECT * FROM MonAn WHERE loaiMon = ?";
            try (Connection con = ConnectDB.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, loaiMon);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    MonAn mon = new MonAn(
                            rs.getString("maMonAn"),
                            rs.getString("tenMonAn"),
                            rs.getString("loaiMon"),
                            rs.getDouble("giaTien"),
                            rs.getString("moTa"),
                            rs.getString("hinhAnh") // ✅ THÊM TRƯỜNG HÌNH ẢNH
                    );
                    dsMonAn.add(mon);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return dsMonAn;
        }
    public double layGiaSauKhuyenMai(String maMonAn, LocalDate ngayDat, double giaMacDinh) {

        String sql =
                "SELECT TOP 1 c.giaSauKhuyenMai " +
                        "FROM ChiTietKMMonAn c JOIN KhuyenMai k ON c.maKhuyenMai = k.maKhuyenMai " +
                        "WHERE c.maMonAn = ? " +
                        "AND ? BETWEEN k.ngayBatDau AND k.ngayKetThuc " +
                        "ORDER BY c.giaSauKhuyenMai ASC"; // Lấy khuyến mãi có giá tốt nhất

        double giaCuoiCung = giaMacDinh;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maMonAn);
            // Chuyển LocalDate sang java.sql.Date hoặc String phù hợp với SQL
            stmt.setDate(2, java.sql.Date.valueOf(ngayDat));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    giaCuoiCung = rs.getDouble("giaSauKhuyenMai");
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi tra cứu giá khuyến mãi: " + e.getMessage());
        }
        return giaCuoiCung;
    }
    
    
    public List<String> layDanhSachMonAnGiaKMString() {
        List<String> ds = new ArrayList<>();

        String sql = """
	        SELECT 
	            ma.maMonAn,
	            ma.tenMonAn,
	            ma.loaiMon,
	            ma.giaTien,
	            ma.moTa,
	            CASE 
	                WHEN EXISTS (
	                    SELECT 1
	                    FROM ChiTietKMMonAn ctkm
	                    JOIN KhuyenMai km ON km.maKhuyenMai = ctkm.maKhuyenMai
	                    WHERE ctkm.maMonAn = ma.maMonAn
	                      AND km.ngayKetThuc >= ?
	                      AND km.ngayBatDau <= ?
	                ) THEN 1
	                ELSE 0
	            END AS CoGiamGia,
	            ISNULL((
	                SELECT MIN(ctkm.giaSauKhuyenMai)
	                FROM ChiTietKMMonAn ctkm
	                JOIN KhuyenMai km ON km.maKhuyenMai = ctkm.maKhuyenMai
	                WHERE ctkm.maMonAn = ma.maMonAn
	                  AND km.ngayKetThuc >= ?
	                  AND km.ngayBatDau <= ?
	            ), ma.giaTien) AS giaSauKhuyenMai,
	            ISNULL((
	                SELECT STRING_AGG(ctkm.maKhuyenMai, ',')
	                FROM ChiTietKMMonAn ctkm
	                JOIN KhuyenMai km ON km.maKhuyenMai = ctkm.maKhuyenMai
	                WHERE ctkm.maMonAn = ma.maMonAn
	                  AND km.ngayKetThuc >= ?
	                  AND km.ngayBatDau <= ?
	            ), 'NA') AS maKhuyenMai, 
	            ma.hinhAnh
	        FROM MonAn ma
	        ORDER BY ma.tenMonAn
	        """;

        try (PreparedStatement ps = ConnectDB.getConnection().prepareStatement(sql)) {
            // set tham số cho ngày bắt đầu và kết thúc khuyến mãi mới
            ps.setObject(1, LocalDate.now());
            ps.setObject(2, LocalDate.now());
            ps.setObject(3, LocalDate.now());
            ps.setObject(4, LocalDate.now());
            ps.setObject(5, LocalDate.now());
            ps.setObject(6, LocalDate.now());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String line = rs.getString("maMonAn") + "-" +
                    		rs.getString("tenMonAn") + "-" +
                            rs.getString("loaiMon") + "-" +
                            rs.getBigDecimal("giaTien") + "-" +
                            rs.getString("moTa") + "-" +
                            rs.getInt("CoGiamGia") + "-" +
                            rs.getBigDecimal("giaSauKhuyenMai") + "-" +
                            rs.getString("maKhuyenMai") + "-" +
                            rs.getString("hinhAnh");
                    ds.add(line);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ds;
    }
    
}