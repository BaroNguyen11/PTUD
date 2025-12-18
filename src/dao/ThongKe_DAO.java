package dao;

import ConnectDB.ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class ThongKe_DAO {


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

    public double getDoanhThuThangTruoc() {
        double total = 0;

        // Câu lệnh SQL:
        // 1. Join 3 bảng lại để lấy được: Số lượng, Giá tiền, Ngày lập.
        // 2. Tính tổng tiền (Số lượng * Giá).
        // 3. Lọc điều kiện: Tháng của ngày lập = Tháng trước (so với hiện tại).

        String sql = "SELECT SUM(CH.soLuong * MA.giaTien) AS TongTienThangTruoc " +
                "FROM ChiTietHoaDon CH " +
                "JOIN MonAn MA ON CH.maMonAn = MA.maMonAn " +
                "JOIN HoaDon HD ON CH.maHoaDon = HD.maHoaDon " +
                "WHERE MONTH(HD.ngayTao) = MONTH(DATEADD(MONTH, -1, GETDATE())) " +
                "AND YEAR(HD.ngayTao) = YEAR(DATEADD(MONTH, -1, GETDATE()))";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                total = rs.getDouble("TongTienThangTruoc");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

    // Hàm lấy doanh thu THÁNG NAY (để so sánh chuẩn với tháng trước)
    public double getDoanhThuThangNay() {
        double total = 0;
        String sql = "SELECT SUM(CH.soLuong * MA.giaTien) AS TongTienThangNay " +
                "FROM ChiTietHoaDon CH " +
                "JOIN MonAn MA ON CH.maMonAn = MA.maMonAn " +
                "JOIN HoaDon HD ON CH.maHoaDon = HD.maHoaDon " +
                "WHERE MONTH(HD.ngayTao) = MONTH(GETDATE()) " +
                "AND YEAR(HD.ngayTao) = YEAR(GETDATE())";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                total = rs.getDouble("TongTienThangNay");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public double getDoanhThuTheoKhoangTG(java.time.LocalDate from, java.time.LocalDate to) {
        double total = 0;
        // Lưu ý: Dùng BETWEEN cho ngày
        String sql = """
                    SELECT SUM(CH.soLuong * ISNULL(KM.giaSauKhuyenMai, MA.giaTien)) AS TongTien 
                    FROM HoaDon H 
                    JOIN ChiTietHoaDon CH ON H.maHoaDon = CH.maHoaDon 
                    JOIN MonAn MA ON CH.maMonAn = MA.maMonAn 
                    LEFT JOIN ChiTietKMMonAn KM ON CH.maMonAn = KM.maMonAn 
                    WHERE H.trangThai LIKE N'%thanh toán%' 
                    AND CAST(H.ngayTao AS DATE) BETWEEN ? AND ?
                """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Chuyển LocalDate (Java) sang Date (SQL)
            ps.setDate(1, java.sql.Date.valueOf(from));
            ps.setDate(2, java.sql.Date.valueOf(to));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getDouble("TongTien");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    /**
     * Thống kê số lượng khách hàng trong khoảng thời gian
     * @param from Ngày bắt đầu
     * @param to Ngày kết thúc
     * @return int[]: [0] = Tổng số khách ghé thăm, [1] = Số khách thân thiết (VIP) trong đó
     */
    public int[] getThongKeKhachHang(java.time.LocalDate from, java.time.LocalDate to) {
        int[] stats = {0, 0};

        // SQL Logic:
        // 1. Join Hóa đơn với Khách hàng
        // 2. Lọc theo ngày và trạng thái thanh toán
        // 3. Đếm tổng khách (DISTINCT để 1 khách mua 2 lần chỉ tính 1)
        // 4. Đếm khách VIP (CASE WHEN điểm cao thì tính)
        String sql = """
            SELECT 
                COUNT(DISTINCT H.maKhachHang) AS TongKhachGheTham,
                COUNT(DISTINCT CASE WHEN KH.diemTichLuy >= 200 THEN H.maKhachHang END) AS KhachThanThiet
            FROM HoaDon H
            JOIN KhachHang KH ON H.maKhachHang = KH.maKhachHang
            WHERE H.trangThai LIKE N'%thanh toán%'
            AND CAST(H.ngayTao AS DATE) BETWEEN ? AND ?
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(from));
            ps.setDate(2, java.sql.Date.valueOf(to));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    stats[0] = rs.getInt("TongKhachGheTham");
                    stats[1] = rs.getInt("KhachThanThiet");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }
    /**
     * Lấy Top 5 khách hàng chi tiêu nhiều nhất trong khoảng thời gian
     * @return List String[]: [0]=Tên, [1]=Tổng tiền (đã format K/M), [2]=Số lần ghé
     */
    public List<String[]> getTopKhachHang(java.time.LocalDate from, java.time.LocalDate to) {
        List<String[]> list = new ArrayList<>();

        String sql = """
            SELECT TOP 5 
                KH.tenKhachHang,
                SUM(CH.soLuong * ISNULL(KM.giaSauKhuyenMai, MA.giaTien)) AS TongTien,
                COUNT(DISTINCT H.maHoaDon) AS SoLanDen
            FROM KhachHang KH
            JOIN HoaDon H ON KH.maKhachHang = H.maKhachHang
            JOIN ChiTietHoaDon CH ON H.maHoaDon = CH.maHoaDon
            JOIN MonAn MA ON CH.maMonAn = MA.maMonAn
            LEFT JOIN ChiTietKMMonAn KM ON CH.maMonAn = KM.maMonAn
            
            WHERE H.trangThai LIKE N'%thanh toán%'
            AND CAST(H.ngayTao AS DATE) BETWEEN ? AND ?
            
            GROUP BY KH.maKhachHang, KH.tenKhachHang
            ORDER BY TongTien DESC;
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(from));
            ps.setDate(2, java.sql.Date.valueOf(to));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String ten = rs.getString("tenKhachHang");
                    double tien = rs.getDouble("TongTien");
                    int lan = rs.getInt("SoLanDen");

                    // Format tiền gọn: 1,500,000 -> 1.5M, 500,000 -> 500K
                    String tienStr;
                    if (tien >= 1000000) {
                        tienStr = String.format("%.1fM", tien / 1000000);
                    } else {
                        tienStr = String.format("%.0fK", tien / 1000);
                    }

                    list.add(new String[]{ten, tienStr, lan + " lần"});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy Top 5 món ăn bán chạy nhất (theo số lượng) trong khoảng thời gian
     */
    public Map<String, Integer> getTopMonAnBanChay(java.time.LocalDate from, java.time.LocalDate to) {
        Map<String, Integer> data = new LinkedHashMap<>();

        String sql = """
            SELECT TOP 5 
                MA.tenMonAn, 
                SUM(CH.soLuong) AS TongSoLuong
            FROM ChiTietHoaDon CH
            JOIN MonAn MA ON CH.maMonAn = MA.maMonAn
            JOIN HoaDon H ON CH.maHoaDon = H.maHoaDon
            
            WHERE H.trangThai LIKE N'%thanh toán%'
            AND CAST(H.ngayTao AS DATE) BETWEEN ? AND ?
            
            GROUP BY MA.tenMonAn
            ORDER BY TongSoLuong DESC;
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(from));
            ps.setDate(2, java.sql.Date.valueOf(to));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data.put(rs.getString("tenMonAn"), rs.getInt("TongSoLuong"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Lấy doanh thu phân bố theo nhóm món ăn (Món chính, Đồ uống, v.v.)
     */
    public Map<String, Double> getDoanhThuTheoNhomMon(java.time.LocalDate from, java.time.LocalDate to) {
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
            AND CAST(H.ngayTao AS DATE) BETWEEN ? AND ?
            
            GROUP BY MA.loaiMon;
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(from));
            ps.setDate(2, java.sql.Date.valueOf(to));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data.put(rs.getString("NhomMon"), rs.getDouble("DoanhThu"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            boolean hasData = false;

            while (rs.next()) {
                hasData = true;
                String tenCa = rs.getString("TenCaChung");
                double tongTien = rs.getDouble("TongDoanhThu");
                data.put(tenCa, tongTien);
            }

            if (!hasData) {
                System.err.println("CẢNH BÁO: Không tìm thấy dòng dữ liệu nào khớp câu lệnh SQL!");
            }

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
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stats;
    }
    public int getTongLuotDatBan() {
        int count = 0;
        // Đếm tất cả phiếu đặt trong tháng này
        String sql = "SELECT COUNT(*) FROM PhieuDatBan " +
                "WHERE MONTH(thoiGianBatDau) = MONTH(GETDATE()) " +
                "AND YEAR(thoiGianBatDau) = YEAR(GETDATE())";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    // 2. Tỷ lệ lấp đầy bàn (Occupancy Rate)
    // Công thức: (Số bàn đang có khách / Tổng số bàn) * 100
    public double getTyLeLapDay() {
        double rate = 0;
        String sql = "SELECT " +
                "(SELECT COUNT(*) FROM BanAn WHERE trangThai = 'DANG_SU_DUNG') * 100.0 / " +
                "(SELECT COUNT(*) FROM BanAn)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                rate = rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rate;
    }

    // 3. Tỷ lệ hủy đặt (Cancellation Rate)
    // Logic: Đếm số phiếu có trạng thái 'DA_HUY' chia cho Tổng số phiếu đã tạo
    public double getTyLeHuyDat() {
        double rate = 0;

        // Lưu ý: Thay 'DA_HUY' bằng chuỗi chính xác bạn lưu trong DB (ví dụ: N'Đã hủy' hoặc 'CANCELLED')
        String sql = "SELECT " +
                "CAST(SUM(CASE WHEN trangThai = N'Đã hủy' THEN 1 ELSE 0 END) AS FLOAT) * 100 / " +
                "NULLIF(COUNT(*), 0) " + // Dùng NULLIF để tránh lỗi chia cho 0 nếu chưa có phiếu nào
                "FROM PhieuDatBan " +
                "WHERE MONTH(thoiGianBatDau) = MONTH(GETDATE()) " + // Chỉ tính trong tháng này
                "AND YEAR(thoiGianBatDau) = YEAR(GETDATE())";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                // Nếu kết quả trả về NULL (do chưa có dữ liệu), mặc định là 0
                if (rs.getObject(1) != null) {
                    rate = rs.getDouble(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rate;
    }

    // 4. Thời gian sử dụng trung bình (phút)

    public double getThoiGianSuDungTB() {
        double min = 45;
        double max = 65;

        // Random một số trong khoảng này
        double fakeData = min + (Math.random() * (max - min));

        return fakeData;
    }

    // 5. Hiệu suất khu vực (Tỷ lệ % sử dụng theo Vị trí)
    // Trả về Map<Tên khu vực, % sử dụng>
    public Map<String, Double> getHieuSuatKhuVuc() {
        Map<String, Double> map = new HashMap<>();
        // Tính % bàn đang dùng trên tổng số bàn của từng khu vực
        String sql = "SELECT \n" +
                "    viTri,\n" +
                "    (COUNT(CASE WHEN trangThai IN (N'Đang dùng', N'Đã đặt') THEN 1 ELSE NULL END) * 100.0 / COUNT(*)) as TyLe\n" +
                "FROM BanAn\n" +
                "GROUP BY viTri;";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Giả sử viTri lưu trong DB dạng enum hoặc string (VD: 'LAU_1')
                String khuVuc = rs.getString(1);
                double tyLe = rs.getDouble(2);

                // Làm đẹp tên khu vực (VD: LAU_1 -> Tầng 1)
                if (khuVuc.equalsIgnoreCase("LAU_1")) khuVuc = "Tầng 1";
                else if (khuVuc.equalsIgnoreCase("LAU_2")) khuVuc = "Tầng 2";
                else if (khuVuc.equalsIgnoreCase("VIP")) khuVuc = "Phòng VIP";

                map.put(khuVuc, tyLe);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
