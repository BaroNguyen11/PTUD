package dao;

import ConnectDB.ConnectDB;
import java.sql.*;
import java.time.*;
import java.util.*;

public class Dashboard_DAO {

    private java.sql.Date toSqlDate(LocalDate ld) {
        return java.sql.Date.valueOf(ld);
    }

    // ========================= DOANH THU =============================

    public double getTongDoanhThuTheoNgay(LocalDate ngay) {
        String sql = """
            SELECT ISNULL(SUM(ct.soLuong * m.giaTien), 0) AS tongDoanhThu
            FROM HoaDon h
            JOIN ChiTietHoaDon ct ON h.maHoaDon = ct.maHoaDon
            JOIN MonAn m ON ct.maMonAn = m.maMonAn
            WHERE CAST(h.ngayTao AS DATE) = ?
              AND h.trangThai = N'Đã thanh toán'
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, toSqlDate(ngay));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("tongDoanhThu");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getTongDoanhThuHomNay() { return getTongDoanhThuTheoNgay(LocalDate.now()); }
    public double getTongDoanhThuHomQua() { return getTongDoanhThuTheoNgay(LocalDate.now().minusDays(1)); }
    // === Doanh thu hôm nay (chia theo giờ) ===
    public Map<String, Double> getDoanhThuHomNayTheoGio() {
        Map<String, Double> data = new LinkedHashMap<>();

        // Khởi tạo tất cả 24 giờ với giá trị 0
        for (int i = 0; i < 24; i++) {
            data.put(String.format("%02d:00", i), 0.0);
        }

        String sql = """
            SELECT DATEPART(HOUR, h.ngayTao) AS gio, 
                   ISNULL(SUM(ct.soLuong * m.giaTien), 0) AS doanhThu
            FROM HoaDon h
            JOIN ChiTietHoaDon ct ON h.maHoaDon = ct.maHoaDon
            JOIN MonAn m ON ct.maMonAn = m.maMonAn
            WHERE CAST(h.ngayTao AS DATE) = ?
              AND h.trangThai = N'Đã thanh toán'
            GROUP BY DATEPART(HOUR, h.ngayTao)
            ORDER BY gio
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, toSqlDate(LocalDate.now()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int gio = rs.getInt("gio");
                    double doanhThu = rs.getDouble("doanhThu");
                    // Cập nhật giá trị thực tế vào map
                    data.put(String.format("%02d:00", gio), doanhThu);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
    // === Doanh thu theo tháng (chia theo ngày) ===
    public Map<String, Double> getDoanhThuTheoThang() {
        Map<String, Double> data = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();
        LocalDate firstDay = today.withDayOfMonth(1);
        LocalDate lastDay = today.withDayOfMonth(today.lengthOfMonth());

        // Khởi tạo tất cả các ngày trong tháng với giá trị 0
        int daysInMonth = today.lengthOfMonth();
        for (int i = 1; i <= daysInMonth; i++) {
            data.put("Ngày " + i, 0.0);
        }

        String sql = """
            SELECT DAY(h.ngayTao) AS ngay, 
                   ISNULL(SUM(ct.soLuong * m.giaTien), 0) AS doanhThu
            FROM HoaDon h
            JOIN ChiTietHoaDon ct ON h.maHoaDon = ct.maHoaDon
            JOIN MonAn m ON ct.maMonAn = m.maMonAn
            WHERE CAST(h.ngayTao AS DATE) BETWEEN ? AND ?
              AND h.trangThai = N'Đã thanh toán'
            GROUP BY DAY(h.ngayTao)
            ORDER BY ngay
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, toSqlDate(firstDay));
            ps.setDate(2, toSqlDate(lastDay));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int ngay = rs.getInt("ngay");
                    double doanhThu = rs.getDouble("doanhThu");
                    // Cập nhật giá trị thực tế vào map
                    data.put("Ngày " + ngay, doanhThu);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }





    // ========================= ĐẶT BÀN =============================

    private int getLuotDatBanTheoNgay(LocalDate ngay) {
        String sql = """
            SELECT COUNT(*) AS soLuot
            FROM PhieuDatBan
            WHERE CAST(thoiGianBatDau AS DATE) = ?
              AND trangThai NOT IN (N'Hủy', N'Đã hủy')
        """;
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, toSqlDate(ngay));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("soLuot");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getLuotDatBanHomNay() { return getLuotDatBanTheoNgay(LocalDate.now()); }
    public int getLuotDatBanHomQua() { return getLuotDatBanTheoNgay(LocalDate.now().minusDays(1)); }

    // ========================= MÓN ĂN =============================

    private int getSoMonBanRaTheoNgay(LocalDate ngay) {
        String sql = """
            SELECT ISNULL(SUM(ct.soLuong), 0) AS tongMon
            FROM HoaDon h
            JOIN ChiTietHoaDon ct ON h.maHoaDon = ct.maHoaDon
            WHERE CAST(h.ngayTao AS DATE) = ?
              AND h.trangThai = N'Đã thanh toán'
        """;
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, toSqlDate(ngay));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("tongMon");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getSoMonBanRaHomNay() { return getSoMonBanRaTheoNgay(LocalDate.now()); }
    public int getSoMonBanRaHomQua() { return getSoMonBanRaTheoNgay(LocalDate.now().minusDays(1)); }

    // === Hôm nay ===
    public Map<String, Integer> getTopMonBanChayHomNay(int limit) {
        Map<String, Integer> data = new LinkedHashMap<>();
        String sql = """
            SELECT TOP 5 m.tenMonAn, SUM(ct.soLuong) AS tongSoLuong
            FROM HoaDon h
            JOIN ChiTietHoaDon ct ON h.maHoaDon = ct.maHoaDon
            JOIN MonAn m ON ct.maMonAn = m.maMonAn
            WHERE CAST(h.ngayTao AS DATE) = ?
              AND h.trangThai = N'Đã thanh toán'
            GROUP BY m.tenMonAn
            ORDER BY tongSoLuong DESC
        """;
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setDate(2, toSqlDate(LocalDate.now()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data.put(rs.getString("tenMonAn"), rs.getInt("tongSoLuong"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    // === Theo tháng ===
    public Map<String, Integer> getTopMonBanChayTheoThang(int limit) {
        Map<String, Integer> data = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();
        LocalDate firstDay = today.withDayOfMonth(1);

        String sql = """
            SELECT TOP (?) m.tenMonAn, SUM(ct.soLuong) AS tongSoLuong
            FROM HoaDon h
            JOIN ChiTietHoaDon ct ON h.maHoaDon = ct.maHoaDon
            JOIN MonAn m ON ct.maMonAn = m.maMonAn
            WHERE CAST(h.ngayTao AS DATE) BETWEEN ? AND ?
              AND h.trangThai = N'Đã thanh toán'
            GROUP BY m.tenMonAn
            ORDER BY tongSoLuong DESC
        """;
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setDate(2, toSqlDate(firstDay));
            ps.setDate(3, toSqlDate(today));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data.put(rs.getString("tenMonAn"), rs.getInt("tongSoLuong"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }



    // ========================= KHÁCH PHỤC VỤ =============================

    private int getSoKhachPhucVuTheoNgay(LocalDate ngay) {
        String sql = """
            SELECT ISNULL(SUM(soNguoi),0) AS tongKhach
            FROM PhieuDatBan
            WHERE CAST(thoiGianBatDau AS DATE) = ?
              AND trangThai NOT IN (N'Hủy', N'Đã hủy')
        """;
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, toSqlDate(ngay));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("tongKhach");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getSoKhachPhucVuHomNay() { return getSoKhachPhucVuTheoNgay(LocalDate.now()); }
    public int getSoKhachPhucVuHomQua() { return getSoKhachPhucVuTheoNgay(LocalDate.now().minusDays(1)); }

    // ========================= DOANH THU THEO TUẦN =============================

    public Map<String, Double> getDoanhThuTheoTuan() {
        Map<String, Double> data = new LinkedHashMap<>();

        // Khởi tạo 7 ngày trong tuần với giá trị 0
        String[] daysOfWeek = {"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật"};
        for (String day : daysOfWeek) {
            data.put(day, 0.0);
        }

        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);
        LocalDate sunday = today.with(DayOfWeek.SUNDAY);

        String sql = """
            SELECT DATEPART(WEEKDAY, h.ngayTao) AS thu, 
                   ISNULL(SUM(ct.soLuong * m.giaTien), 0) AS doanhThu
            FROM HoaDon h
            JOIN ChiTietHoaDon ct ON h.maHoaDon = ct.maHoaDon
            JOIN MonAn m ON ct.maMonAn = m.maMonAn
            WHERE CAST(h.ngayTao AS DATE) BETWEEN ? AND ?
              AND h.trangThai = N'Đã thanh toán'
            GROUP BY DATEPART(WEEKDAY, h.ngayTao)
            ORDER BY thu
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, toSqlDate(monday));
            ps.setDate(2, toSqlDate(sunday));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int thuIndex = rs.getInt("thu");
                    double doanhThu = rs.getDouble("doanhThu");

                    // Chuyển đổi từ SQL Server weekday index sang tên ngày
                    // SQL Server: 1=Chủ nhật, 2=Thứ 2, ..., 7=Thứ 7
                    String dayName = convertWeekdayIndexToName(thuIndex);
                    if (dayName != null) {
                        data.put(dayName, doanhThu);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
    private String convertWeekdayIndexToName(int sqlServerWeekdayIndex) {
        // SQL Server: 1=Chủ nhật, 2=Thứ 2, 3=Thứ 3, 4=Thứ 4, 5=Thứ 5, 6=Thứ 6, 7=Thứ 7
        switch (sqlServerWeekdayIndex) {
            case 1: return "Chủ nhật";
            case 2: return "Thứ 2";
            case 3: return "Thứ 3";
            case 4: return "Thứ 4";
            case 5: return "Thứ 5";
            case 6: return "Thứ 6";
            case 7: return "Thứ 7";
            default: return null;
        }
    }

    // ========================= TOP MÓN BÁN CHẠY =============================

    public Map<String, Integer> getTopMonBanChay(int limit) {
        Map<String, Integer> data = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();
        LocalDate monday = today.minusDays(today.getDayOfWeek().getValue() - 1);

        String sql = """
            SELECT TOP (?) m.tenMonAn, SUM(ct.soLuong) AS tongSoLuong
            FROM HoaDon h
            JOIN ChiTietHoaDon ct ON h.maHoaDon = ct.maHoaDon
            JOIN MonAn m ON ct.maMonAn = m.maMonAn
            WHERE CAST(h.ngayTao AS DATE) BETWEEN ? AND ?
              AND h.trangThai = N'Đã thanh toán'
            GROUP BY m.tenMonAn
            ORDER BY tongSoLuong DESC
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setDate(2, toSqlDate(monday));
            ps.setDate(3, toSqlDate(today));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data.put(rs.getString("tenMonAn"), rs.getInt("tongSoLuong"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public String getMonBanChayNhatHomNay() {
        Map<String, Integer> top = getTopMonBanChay(1);
        return top.isEmpty() ? "Không có dữ liệu" : top.keySet().iterator().next();
    }

    // ========================= KHÁCH THEO KHUNG GIỜ =============================

    public Map<String, Integer> getLuongKhachTheoKhungGio() {
        return getLuongKhachTheoKhungGio(LocalDate.now());
    }

    public Map<String, Integer> getLuongKhachTheoKhungGio(LocalDate ngay) {
        Map<String, Integer> data = new LinkedHashMap<>();
        data.put("7-9h", 0);
        data.put("9-11h", 0);
        data.put("11-13h", 0);
        data.put("13-17h", 0);
        data.put("17-21h", 0);
        data.put("Khác", 0);

        String sql = """
            SELECT 
                CASE 
                    WHEN DATEPART(HOUR, thoiGianBatDau) BETWEEN 7 AND 8 THEN '7-9h'
                    WHEN DATEPART(HOUR, thoiGianBatDau) BETWEEN 9 AND 10 THEN '9-11h'
                    WHEN DATEPART(HOUR, thoiGianBatDau) BETWEEN 11 AND 12 THEN '11-13h'
                    WHEN DATEPART(HOUR, thoiGianBatDau) BETWEEN 13 AND 16 THEN '13-17h'
                    WHEN DATEPART(HOUR, thoiGianBatDau) BETWEEN 17 AND 20 THEN '17-21h'
                    ELSE 'Khác'
                END AS khungGio,
                SUM(soNguoi) AS tongKhach
            FROM PhieuDatBan
            WHERE CAST(thoiGianBatDau AS DATE) = ?
              AND trangThai NOT IN (N'Hủy', N'Đã hủy')
            GROUP BY 
                CASE 
                    WHEN DATEPART(HOUR, thoiGianBatDau) BETWEEN 7 AND 8 THEN '7-9h'
                    WHEN DATEPART(HOUR, thoiGianBatDau) BETWEEN 9 AND 10 THEN '9-11h'
                    WHEN DATEPART(HOUR, thoiGianBatDau) BETWEEN 11 AND 12 THEN '11-13h'
                    WHEN DATEPART(HOUR, thoiGianBatDau) BETWEEN 13 AND 16 THEN '13-17h'
                    WHEN DATEPART(HOUR, thoiGianBatDau) BETWEEN 17 AND 20 THEN '17-21h'
                    ELSE 'Khác'
                END
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, toSqlDate(ngay));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data.put(rs.getString("khungGio"), rs.getInt("tongKhach"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    // ========================= KHÁCH MỚI / QUAY LẠI =============================

    public int getSoKhachMoi() {
        String sql = """
            SELECT COUNT(*) AS soKhachMoi
            FROM (
                SELECT kh.maKhachHang
                FROM HoaDon hd
                JOIN KhachHang kh ON hd.maKhachHang = kh.maKhachHang
                WHERE hd.trangThai = N'Đã thanh toán'
                GROUP BY kh.maKhachHang
                HAVING COUNT(hd.maHoaDon) = 1
            ) AS t
        """;
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("soKhachMoi");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getSoKhachQuayLai() {
        String sql = """
            SELECT COUNT(*) AS soKhachQuayLai
            FROM (
                SELECT kh.maKhachHang
                FROM HoaDon hd
                JOIN KhachHang kh ON hd.maKhachHang = kh.maKhachHang
                WHERE hd.trangThai = N'Đã thanh toán'
                GROUP BY kh.maKhachHang
                HAVING COUNT(hd.maHoaDon) > 1
            ) AS t
        """;
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("soKhachQuayLai");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ========================= TÍNH TOÁN =============================

    public String tinhPhanTramThayDoi(double homNay, double homQua) {
        if (homQua == 0) return homNay > 0 ? "+100%" : "0%";
        double pct = ((homNay - homQua) / homQua) * 100;
        return String.format("%+.0f%%", pct);
    }
}