package dao;

import ConnectDB.ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class ThongKe_DAO {

    /**
     * Lấy tổng doanh thu theo tháng (ví dụ cho biểu đồ đường)
     * @return Map<tháng, tổng doanh thu>
     */
//    public Map<String, Double> getDoanhThuTheoThang() {
//        Map<String, Double> dsDoanhThu = new LinkedHashMap<>();
//        String sql = """
//       SELECT FORMAT(H.ngayTao, 'MM') AS Thang, SUM(CH.soLuong * ISNULL(KM.giaSauKhuyenMai, MA.giaTien)) AS TongDoanhThu FROM HoaDon H JOIN ChiTietHoaDon CH ON H.maHoaDon = CH.maHoaDon JOIN MonAn MA ON CH.maMonAn = MA.maMonAn LEFT JOIN ChiTietKMMonAn KM ON CH.maMonAn = KM.maMonAn WHERE H.trangThai = N'Đã thanh toán' GROUP BY FORMAT(H.ngayTao, 'MM') ORDER BY MIN(H.ngayTao);
//    """;
//
//        try (Connection con = ConnectDB.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//
//            while (rs.next()) {
//                String thang = rs.getString("Thang");
//                double tong = rs.getDouble("TongDoanhThu");
//                dsDoanhThu.put(thang, tong);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return dsDoanhThu;
//    }
    public Map<String, Double> getDoanhThuTheoThang(int nam) { // <-- Thêm tham số int nam
        Map<String, Double> dsDoanhThu = new LinkedHashMap<>();
        String sql = """
       SELECT FORMAT(H.ngayTao, 'MM') AS Thang, 
              SUM(CH.soLuong * ISNULL(KM.giaSauKhuyenMai, MA.giaTien)) AS TongDoanhThu 
       FROM HoaDon H 
       JOIN ChiTietHoaDon CH ON H.maHoaDon = CH.maHoaDon 
       JOIN MonAn MA ON CH.maMonAn = MA.maMonAn 
       LEFT JOIN ChiTietKMMonAn KM ON CH.maMonAn = KM.maMonAn 
       WHERE H.trangThai LIKE N'Đã thanh toán' 
         AND YEAR(H.ngayTao) = ?  
       GROUP BY FORMAT(H.ngayTao, 'MM') 
       ORDER BY MIN(H.ngayTao);
    """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, nam);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String thang = rs.getString("Thang");
                    double tong = rs.getDouble("TongDoanhThu");
                    dsDoanhThu.put(thang, tong);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsDoanhThu;
    }

    /**
     * Lấy tổng doanh thu hiện tại
     */
    public double getTongDoanhThu() {
        double total = 0;
        String sql = "SELECT SUM(T.ThanhTien) " +
                "AS TongTienTatCaHoaDon " +
                "FROM ( SELECT CH.maHoaDon, (CH.soLuong * MA.giaTien) " +
                "AS ThanhTien " +
                "FROM ChiTietHoaDon CH JOIN MonAn MA " +
                "ON CH.maMonAn = MA.maMonAn ) AS T";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                total = rs.getDouble("TongTienTatCaHoaDon");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }
    public double getDoanhThuTrungBinhBan() {
        double avg = 0;
        String sql = """
           SELECT SUM(T.ThanhTien) / COUNT(DISTINCT T.maHoaDon) 
           AS DoanhThuTrungBinhBan 
           FROM ( SELECT CH.maHoaDon, (CH.soLuong * MA.giaTien) 
           AS ThanhTien 
           FROM ChiTietHoaDon CH JOIN MonAn MA 
           ON CH.maMonAn = MA.maMonAn ) 
           AS T;
        """;
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) avg = rs.getDouble("DoanhThuTrungBinhBan");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return avg;
    }

    // Tỷ lệ thanh toán tiền mặt
    public double getTiLeTienMat() {
        double tile = 0;
        String sql = """
          SELECT 100.0 * SUM(CASE WHEN phuongThuc = N'Tiền mặt' THEN 1 ELSE 0 END) / COUNT(phuongThuc) 
          AS TiLeTienMat FROM HoaDon WHERE trangThai != N'Hủy';
        """;
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) tile = rs.getDouble("TiLeTienMat");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tile;
    }

    // Doanh thu theo ca tối
    public double getDoanhThuCaToi() {
        double total = 0;
        String sql = """
           SELECT SUM(T.ThanhTien) 
           AS DoanhThuCaToi 
           FROM ( SELECT CH.maHoaDon, (CH.soLuong * MA.giaTien) 
           AS ThanhTien 
           FROM ChiTietHoaDon CH JOIN MonAn MA ON CH.maMonAn = MA.maMonAn )
            AS T JOIN HoaDon H ON T.maHoaDon = H.maHoaDon 
            WHERE DATEPART(HOUR, H.ngayTao) 
            BETWEEN 17 AND 23;
        """;
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) total = rs.getDouble("DoanhThuCaToi");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }


    /**
     * Đếm số lượng khách hàng
     */
    public int getSoLuongKhachHang() {
        int count = 0;
        String sql = "SELECT COUNT(*) AS SoLuong FROM KhachHang";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt("SoLuong");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }
    // Trong class ThongKe_DAO

    public List<String[]> getTopKhachHang() {
        List<String[]> list = new ArrayList<>();

        String sql = """
        SELECT TOP 5 
            KH.tenKhachHang,
            -- Tính tổng tiền chi tiêu
            SUM(CH.soLuong * ISNULL(KM.giaSauKhuyenMai, MA.giaTien)) AS TongTien,
            -- Đếm số lần đến (số hóa đơn khác nhau)
            COUNT(DISTINCT H.maHoaDon) AS SoLanDen
        FROM KhachHang KH
        JOIN HoaDon H ON KH.maKhachHang = H.maKhachHang
        JOIN ChiTietHoaDon CH ON H.maHoaDon = CH.maHoaDon
        JOIN MonAn MA ON CH.maMonAn = MA.maMonAn
        LEFT JOIN ChiTietKMMonAn KM ON CH.maMonAn = KM.maMonAn
        
        WHERE H.trangThai LIKE N'%thanh toán%'
        GROUP BY KH.maKhachHang, KH.tenKhachHang
        ORDER BY TongTien DESC;
    """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String ten = rs.getString("tenKhachHang");
                double tien = rs.getDouble("TongTien");
                int lan = rs.getInt("SoLanDen");

                // Format tiền thành dạng "15.5M" hoặc "500K" cho gọn
                String tienStr;
                if (tien >= 1000000) {
                    tienStr = String.format("%.1fM", tien / 1000000);
                } else {
                    tienStr = String.format("%.0fK", tien / 1000);
                }

                list.add(new String[]{ten, tienStr, lan + " lần"});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 1. Thống kê số lượng khách: [0] = Tổng khách, [1] = Khách thân thiết (ví dụ điểm > 200)
     */
    public int[] getThongKeKhachHang() {
        int[] stats = {0, 0};
        String sql = """
        SELECT 
            COUNT(*) AS TongKhach,
            SUM(CASE WHEN diemTichLuy >= 200 THEN 1 ELSE 0 END) AS KhachThanThiet
        FROM KhachHang
    """;
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                stats[0] = rs.getInt("TongKhach");
                stats[1] = rs.getInt("KhachThanThiet");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return stats;
    }

    /**
     * 2. Tính chi tiêu trung bình trên mỗi hóa đơn (Tổng thu / Số đơn)
     */
    public double getChiTieuTrungBinh() {
        double avg = 0;
        String sql = """
        SELECT AVG(TongTien) as TrungBinh
        FROM (
            SELECT SUM(CH.soLuong * ISNULL(KM.giaSauKhuyenMai, MA.giaTien)) as TongTien
            FROM HoaDon H
            JOIN ChiTietHoaDon CH ON H.maHoaDon = CH.maHoaDon
            JOIN MonAn MA ON CH.maMonAn = MA.maMonAn
            LEFT JOIN ChiTietKMMonAn KM ON CH.maMonAn = KM.maMonAn
            WHERE H.trangThai LIKE N'%thanh toán%'
            GROUP BY H.maHoaDon
        ) T
    """;
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) avg = rs.getDouble("TrungBinh");
        } catch (Exception e) { e.printStackTrace(); }
        return avg;
    }

    /**
     * 3. Tần suất quay lại trung bình (Tổng đơn / Tổng khách)
     */
    public double getTanSuatTrungBinh() {
        double freq = 0;
        String sql = """
       SELECT CAST(COUNT(DISTINCT maHoaDon) AS FLOAT) / NULLIF(COUNT(DISTINCT maKhachHang), 0) as TanSuat
       FROM HoaDon
       WHERE trangThai LIKE N'%thanh toán%'
    """;
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) freq = rs.getDouble("TanSuat");
        } catch (Exception e) { e.printStackTrace(); }
        return freq;
    }
    public Map<String, Double> getDoanhThuTheoCa() {
        Map<String, Double> data = new LinkedHashMap<>();

        // Khởi tạo trước
        data.put("Ca Sáng", 0.0);
        data.put("Ca Trưa", 0.0);
        data.put("Ca Chiều", 0.0);
        data.put("Ca Tối", 0.0);
        data.put("Ca Đêm", 0.0);

        // SQL lấy tất cả, không lọc ngày tháng để TEST dữ liệu trước
        String sql = """
        SELECT 
            CASE 
                WHEN DATEPART(HOUR, H.ngayTao) >= 6 AND DATEPART(HOUR, H.ngayTao) < 11 THEN N'Ca Sáng'
                WHEN DATEPART(HOUR, H.ngayTao) >= 11 AND DATEPART(HOUR, H.ngayTao) < 14 THEN N'Ca Trưa'
                WHEN DATEPART(HOUR, H.ngayTao) >= 14 AND DATEPART(HOUR, H.ngayTao) < 17 THEN N'Ca Chiều'
                WHEN DATEPART(HOUR, H.ngayTao) >= 17 AND DATEPART(HOUR, H.ngayTao) < 22 THEN N'Ca Tối'
                ELSE N'Ca Đêm'
            END AS TenCaChung,

            SUM(ISNULL(CH.soLuong, 0) * ISNULL(KM.giaSauKhuyenMai, MA.giaTien)) AS TongDoanhThu

        FROM HoaDon H
        LEFT JOIN ChiTietHoaDon CH ON H.maHoaDon = CH.maHoaDon
        LEFT JOIN MonAn MA ON CH.maMonAn = MA.maMonAn
        LEFT JOIN ChiTietKMMonAn KM ON CH.maMonAn = KM.maMonAn
        
        -- Quan trọng: Dùng LIKE để bắt mọi biến thể của chuỗi 'thanh toán'
        WHERE H.trangThai LIKE N'%thanh toán%'
        
        GROUP BY 
            CASE 
                WHEN DATEPART(HOUR, H.ngayTao) >= 6 AND DATEPART(HOUR, H.ngayTao) < 11 THEN N'Ca Sáng'
                WHEN DATEPART(HOUR, H.ngayTao) >= 11 AND DATEPART(HOUR, H.ngayTao) < 14 THEN N'Ca Trưa'
                WHEN DATEPART(HOUR, H.ngayTao) >= 14 AND DATEPART(HOUR, H.ngayTao) < 17 THEN N'Ca Chiều'
                WHEN DATEPART(HOUR, H.ngayTao) >= 17 AND DATEPART(HOUR, H.ngayTao) < 22 THEN N'Ca Tối'
                ELSE N'Ca Đêm'
            END
    """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("--- BẮT ĐẦU KIỂM TRA DATA THỐNG KÊ ---");
            boolean hasData = false;

            while (rs.next()) {
                hasData = true;
                String tenCa = rs.getString("TenCaChung");
                double tongTien = rs.getDouble("TongDoanhThu");

                // In ra console để kiểm tra
                System.out.println("Tìm thấy: " + tenCa + " - Doanh thu: " + tongTien);

                data.put(tenCa, tongTien);
            }

            if (!hasData) {
                System.out.println("CẢNH BÁO: Không tìm thấy dòng dữ liệu nào khớp câu lệnh SQL!");
            }
            System.out.println("--- KẾT THÚC KIỂM TRA ---");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }
    /**
     * Lấy Top 5 món ăn bán chạy nhất dựa trên tổng số lượng trong Chi Tiết Hóa Đơn
     * Chỉ tính các hóa đơn Đã thanh toán
     */
    public Map<String, Integer> getTopMonAnBanChay() {
        Map<String, Integer> data = new LinkedHashMap<>();

        String sql = """
        SELECT TOP 5 
            MA.tenMonAn, 
            SUM(CT.soLuong) AS TongSoLuong
        FROM ChiTietHoaDon CT
        JOIN MonAn MA ON CT.maMonAn = MA.maMonAn
        JOIN HoaDon H ON CT.maHoaDon = H.maHoaDon
        WHERE H.trangThai = N'Đã thanh toán' -- Chỉ tính hóa đơn đã chốt tiền
        GROUP BY MA.tenMonAn, MA.maMonAn
        ORDER BY TongSoLuong DESC; -- Sắp xếp giảm dần để lấy Top cao nhất
    """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String tenMon = rs.getString("tenMonAn");
                int soLuong = rs.getInt("TongSoLuong");
                data.put(tenMon, soLuong);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }
    public Map<String, Double> getDoanhThuTheoNhomMon() {
        Map<String, Double> data = new LinkedHashMap<>();

        String sql = """
        SELECT 
            ISNULL(MA.loaiMon, N'Khác') AS NhomMon,
            SUM(CH.soLuong * ISNULL(KM.giaSauKhuyenMai, MA.giaTien)) AS DoanhThu
        FROM ChiTietHoaDon CH
        JOIN MonAn MA ON CH.maMonAn = MA.maMonAn
        JOIN HoaDon H ON CH.maHoaDon = H.maHoaDon
        LEFT JOIN ChiTietKMMonAn KM ON CH.maMonAn = KM.maMonAn
        
        WHERE H.trangThai LIKE N'%thanh toán%'
        GROUP BY MA.loaiMon
    """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String nhom = rs.getString("NhomMon");
                double tien = rs.getDouble("DoanhThu");
                data.put(nhom, tien);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }
    /**
     * 1. Lấy món bán chạy nhất (Về số lượng)
     * Trả về mảng: [0]=Tên món, [1]=Số lượng
     */
    public String[] getMonBanChayNhat() {
        String[] result = {"Không có", "0"};
        String sql = """
        SELECT TOP 1 
            MA.tenMonAn, 
            SUM(CT.soLuong) AS TongSoLuong
        FROM ChiTietHoaDon CT
        JOIN MonAn MA ON CT.maMonAn = MA.maMonAn
        JOIN HoaDon H ON CT.maHoaDon = H.maHoaDon
        WHERE H.trangThai LIKE N'%thanh toán%'
        GROUP BY MA.tenMonAn
        ORDER BY TongSoLuong DESC
    """;
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                result[0] = rs.getString("tenMonAn");
                result[1] = String.valueOf(rs.getInt("TongSoLuong"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return result;
    }

    /**
     * 2. Lấy món có doanh thu cao nhất (Về tiền)
     * Trả về mảng: [0]=Tên món, [1]=Tổng tiền
     */
    public String[] getMonDoanhThuCaoNhat() {
        String[] result = {"Không có", "0"};
        String sql = """
        SELECT TOP 1 
            MA.tenMonAn, 
            SUM(CT.soLuong * ISNULL(KM.giaSauKhuyenMai, MA.giaTien)) AS TongTien
        FROM ChiTietHoaDon CT
        JOIN MonAn MA ON CT.maMonAn = MA.maMonAn
        JOIN HoaDon H ON CT.maHoaDon = H.maHoaDon
        LEFT JOIN ChiTietKMMonAn KM ON CT.maMonAn = KM.maMonAn
        WHERE H.trangThai LIKE N'%thanh toán%'
        GROUP BY MA.tenMonAn
        ORDER BY TongTien DESC
    """;
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                result[0] = rs.getString("tenMonAn");
                result[1] = String.valueOf(rs.getDouble("TongTien"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return result;
    }

    /**
     * 3. Lấy thống kê Đồ uống: [0]=Doanh thu Đồ uống, [1]=Tổng doanh thu toàn bộ
     */
    public double[] getThongKeDoUong() {
        double[] stats = {0, 0};
        String sql = """
        SELECT 
            SUM(CASE WHEN MA.loaiMon = N'Đồ uống' THEN CH.soLuong * ISNULL(KM.giaSauKhuyenMai, MA.giaTien) ELSE 0 END) AS DoanhThuNuoc,
            SUM(CH.soLuong * ISNULL(KM.giaSauKhuyenMai, MA.giaTien)) AS TongDoanhThu
        FROM ChiTietHoaDon CH
        JOIN MonAn MA ON CH.maMonAn = MA.maMonAn
        JOIN HoaDon H ON CH.maHoaDon = H.maHoaDon
        LEFT JOIN ChiTietKMMonAn KM ON CH.maMonAn = KM.maMonAn
        WHERE H.trangThai LIKE N'%thanh toán%'
    """;
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                stats[0] = rs.getDouble("DoanhThuNuoc");
                stats[1] = rs.getDouble("TongDoanhThu");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return stats;
    }
}
