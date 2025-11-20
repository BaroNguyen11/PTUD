package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.KhuyenMai;

public class Dao_QuanLiKM {

	public List<String> layTatCaKhuyenMaiStrings() {
        List<String> ds = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String sql = """
		            SELECT 
		    km.maKhuyenMai,
		    km.tenKhuyenMai,
		    km.ngayBatDau,
		    km.ngayKetThuc,
		    km.dieuKienApDung,
		    km.giaTriToiDa,
		    km.giamGiaPhanTram,
		    km.giaTriGiam,
		
		    CASE 
		        WHEN EXISTS (
		            SELECT 1 
		            FROM ChiTietKMMonAn ctkmMonAn 
		            WHERE ctkmMonAn.maKhuyenMai = km.maKhuyenMai
		        ) THEN 1
		        ELSE 0
		    END AS LaMonAn,
		
		    CASE
		        WHEN km.ngayKetThuc < CAST(GETDATE() AS DATE)
		            THEN '0'  
		        WHEN km.ngayBatDau > CAST(GETDATE() AS DATE)
		            THEN '2'   
		        ELSE '1'       
		    END AS TrangThai
		
		FROM KhuyenMai km
		ORDER BY km.maKhuyenMai;
        """;

        try (PreparedStatement ps = ConnectDB.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                StringBuilder sb = new StringBuilder();
                sb.append(rs.getString("maKhuyenMai")).append(",")
                  .append(rs.getString("tenKhuyenMai")).append(",")
                  .append(dtf.format(rs.getDate("ngayBatDau").toLocalDate())).append(",")
                  .append(dtf.format(rs.getDate("ngayKetThuc").toLocalDate())).append(",")
                  .append(rs.getBigDecimal("dieuKienApDung")).append(",")
                  .append(rs.getBigDecimal("giaTriToiDa")).append(",")
                  .append(rs.getBoolean("giamGiaPhanTram") ? 1 : 0).append(",")
                  .append(rs.getBigDecimal("giaTriGiam")).append(",")
                  .append(rs.getInt("LaMonAn")).append(",")
                  .append(rs.getString("TrangThai"));

                ds.add(sb.toString());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ds;
    }
	
	public List<String> layDanhSachMonAnStrings(LocalDate ngayBDMoi, LocalDate ngayKTMoi) {
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
	            ), 'NA') AS maKhuyenMai
	        FROM MonAn ma
	        ORDER BY ma.tenMonAn
	        """;

	    try (PreparedStatement ps = ConnectDB.getConnection().prepareStatement(sql)) {
	        // set tham số cho ngày bắt đầu và kết thúc khuyến mãi mới
	        ps.setObject(1, ngayBDMoi);
	        ps.setObject(2, ngayKTMoi);
	        ps.setObject(3, ngayBDMoi);
	        ps.setObject(4, ngayKTMoi);
	        ps.setObject(5, ngayBDMoi);
	        ps.setObject(6, ngayKTMoi);

	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                String line = rs.getString("maMonAn") + "_" +
	                              rs.getString("tenMonAn") + "_" +
	                              rs.getString("loaiMon") + "_" +
	                              rs.getBigDecimal("giaTien") + "_" +
	                              rs.getString("moTa") + "_" +
	                              rs.getInt("CoGiamGia") + "_" +
	                              rs.getBigDecimal("giaSauKhuyenMai") + "_" +
	                              rs.getString("maKhuyenMai");
	                ds.add(line);
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return ds;
	}

    
    public KhuyenMai layKhuyenMaiTheoMonAn(String maMonAn) {
        String sql = """
            SELECT 
                km.maKhuyenMai,
                km.tenKhuyenMai,
                km.ngayBatDau,
                km.ngayKetThuc,
                km.dieuKienApDung,
                km.giaTriToiDa,
                km.giamGiaPhanTram,
                km.giaTriGiam
            FROM KhuyenMai km
            INNER JOIN ChiTietKMMonAn ctkm ON km.maKhuyenMai = ctkm.maKhuyenMai
            WHERE ctkm.maMonAn = ?
        """;

        try (PreparedStatement ps = ConnectDB.getConnection().prepareStatement(sql)) {
            ps.setString(1, maMonAn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    KhuyenMai km = new KhuyenMai();
                    km.setMaKhuyenMai(rs.getString("maKhuyenMai"));
                    km.setTenKhuyenMai(rs.getString("tenKhuyenMai"));
                    km.setNgayBatDau(rs.getDate("ngayBatDau").toLocalDate());
                    km.setNgayKetThuc(rs.getDate("ngayKetThuc").toLocalDate());
                    km.setDieuKienApDung(rs.getDouble("dieuKienApDung"));
                    km.setGiaTriToiDa(rs.getDouble("giaTriToiDa"));
                    km.setGiamGiaPhanTram(rs.getBoolean("giamGiaPhanTram"));
                    km.setGiaTriGiam(rs.getDouble("giaTriGiam"));
                    return km;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public KhuyenMai layKhuyenMaiTheoMa(String maKhuyenMai) {
        String sql = """
            SELECT 
                maKhuyenMai,
                tenKhuyenMai,
                ngayBatDau,
                ngayKetThuc,
                dieuKienApDung,
                giaTriToiDa,
                giamGiaPhanTram,
                giaTriGiam
            FROM KhuyenMai
            WHERE maKhuyenMai = ?
        """;

        try (PreparedStatement ps = ConnectDB.getConnection().prepareStatement(sql)) {
            ps.setString(1, maKhuyenMai);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    KhuyenMai km = new KhuyenMai();
                    km.setMaKhuyenMai(rs.getString("maKhuyenMai"));
                    km.setTenKhuyenMai(rs.getString("tenKhuyenMai"));
                    km.setNgayBatDau(rs.getDate("ngayBatDau").toLocalDate());
                    km.setNgayKetThuc(rs.getDate("ngayKetThuc").toLocalDate());
                    km.setDieuKienApDung(rs.getDouble("dieuKienApDung"));
                    km.setGiaTriToiDa(rs.getDouble("giaTriToiDa"));
                    km.setGiamGiaPhanTram(rs.getBoolean("giamGiaPhanTram"));
                    km.setGiaTriGiam(rs.getDouble("giaTriGiam"));
                    return km;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; 
    }
    
    public List<String> getDanhSachMonAnTheoKhuyenMai(String maKhuyenMai) {
        List<String> ds = new ArrayList<>();

        String sql = """
            SELECT
                ma.maMonAn,
                ma.tenMonAn,
                ma.loaiMon,
                ma.giaTien,
                ma.moTa,

                -- Kiểm tra món này có ít nhất 1 khuyến mãi hay không
                CASE
                    WHEN EXISTS (
                        SELECT 1
                        FROM ChiTietKMMonAn ctkm
                        WHERE ctkm.maMonAn = ma.maMonAn
                    ) THEN 1
                    ELSE 0
                END AS CoGiamGia,

                -- Giá sau KM theo đúng mã KM đang xem
                ISNULL((
                    SELECT TOP 1 ctkm.giaSauKhuyenMai
                    FROM ChiTietKMMonAn ctkm
                    WHERE ctkm.maMonAn = ma.maMonAn
                      AND ctkm.maKhuyenMai = ?
                ), ma.giaTien) AS giaSauKhuyenMai,

                -- Mã khuyến mãi áp dụng cho món (theo đúng mã cần xem)
                ISNULL((
                    SELECT TOP 1 ctkm.maKhuyenMai
                    FROM ChiTietKMMonAn ctkm
                    WHERE ctkm.maMonAn = ma.maMonAn
                      AND ctkm.maKhuyenMai = ?
                ), 'NA') AS maKhuyenMai

            FROM MonAn ma
            WHERE ma.maMonAn IN (
                SELECT maMonAn
                FROM ChiTietKMMonAn
                WHERE maKhuyenMai = ?
            )
            ORDER BY ma.tenMonAn
            """;

        try (PreparedStatement stmt = ConnectDB.getConnection().prepareStatement(sql)) {

            // Gán đúng 3 parameter cho 3 dấu '?'
            stmt.setString(1, maKhuyenMai);
            stmt.setString(2, maKhuyenMai);
            stmt.setString(3, maKhuyenMai);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                String row =
                        rs.getString("maMonAn") + "_" +
                        rs.getString("tenMonAn") + "_" +
                        rs.getString("loaiMon") + "_" +
                        rs.getDouble("giaTien") + "_" +
                        rs.getString("moTa") + "_" +
                        rs.getInt("CoGiamGia") + "_" +
                        rs.getDouble("giaSauKhuyenMai") + "_" +
                        rs.getString("maKhuyenMai");

                ds.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }


    public String taoMaKhuyenMaiTuDong() {
        String sql = """
            SELECT TOP 1 maKhuyenMai
            FROM KhuyenMai
            ORDER BY maKhuyenMai DESC
        """;

        try (PreparedStatement stmt = ConnectDB.getConnection().prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String maCu = rs.getString("maKhuyenMai"); // Ví dụ: "KM023"

                // Tách phần số
                int so = Integer.parseInt(maCu.substring(2));
                so++;

                // Format lại với 3 chữ số
                return "KM" + String.format("%03d", so);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Trường hợp chưa có khuyến mãi nào
        return "KM001";
    }

    public boolean insert(KhuyenMai km) {
        String sql = "INSERT INTO KhuyenMai (" +
                "maKhuyenMai, tenKhuyenMai, ngayBatDau, ngayKetThuc, " +
                "dieuKienApDung, giaTriToiDa, giamGiaPhanTram, giaTriGiam) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, km.getMaKhuyenMai());
            ps.setString(2, km.getTenKhuyenMai());
            ps.setDate(3, java.sql.Date.valueOf(km.getNgayBatDau()));
            ps.setDate(4, java.sql.Date.valueOf(km.getNgayKetThuc()));
            ps.setDouble(5, km.getDieuKienApDung());
            ps.setDouble(6, km.getGiaTriToiDa());
            ps.setBoolean(7, km.getGiamGiaPhanTram());
            ps.setDouble(8, km.getGiaTriGiam());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean insertCTKMMonAn(String maKhuyenMai, String maMonAn, double giaSauKM) {
        String sql = "INSERT INTO ChiTietKMMonAn (maKhuyenMai, maMonAn, giaSauKhuyenMai) VALUES (?, ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maKhuyenMai);
            ps.setString(2, maMonAn);
            ps.setDouble(3, giaSauKM);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    
    public boolean ngungKhuyenMai(String maKhuyenMai) {
        String sql = "UPDATE KhuyenMai SET ngayKetThuc = GETDATE() WHERE maKhuyenMai = ?";

        try (PreparedStatement stmt = ConnectDB.getConnection().prepareStatement(sql)) {
            stmt.setString(1, maKhuyenMai);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0; // true nếu có bản ghi được cập nhật
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
