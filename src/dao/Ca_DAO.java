package dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import ConnectDB.ConnectDB;
import entity.Ca;
import entity.NhanVien;

public class Ca_DAO {



    // Bắt đầu ca: INSERT
    public boolean batDauCa(double tongTienDauCa, String maNhanVien) {
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = ConnectDB.getConnection();

            String maCa = taoMaCa(con);

            String sql = "INSERT INTO Ca (maCa, thoiGianVaoCa, tongTienDauCa, maNhanVien) VALUES (?, ?, ?, ?)";

            stmt = con.prepareStatement(sql);
            stmt.setString(1, maCa);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setDouble(3, tongTienDauCa);
            stmt.setString(4, maNhanVien);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            try { if (con != null) con.close(); } catch (Exception ignored) {}
        }
    }

    private String taoMaCa(Connection con) throws SQLException {
        String sql = "SELECT TOP 1 maCa FROM Ca ORDER BY maCa DESC";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);

        if (rs.next()) {
            String last = rs.getString("maCa").substring(2);
            int num = Integer.parseInt(last) + 1;
            return String.format("CA%03d", num);
        }
        return "CA001";
    }


    // Kết ca: UPDATE
    public boolean ketCa(String maCa, double tongTienCuoiCa) {
        try {
            Connection con = ConnectDB.getConnection();
            String sql = "UPDATE Ca SET thoiGianKetCa = ?, tongTienCuoiCa = ? WHERE maCa = ?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            ps.setDouble(2, tongTienCuoiCa);
            ps.setString(3, maCa);

            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

// Thêm vào file Ca_DAO.java

    /**
     * Lấy ca BẤT KỲ đang mở (chưa kết ca), không phân biệt nhân viên.
     * Dùng để kiểm tra xem có ca nào bị "treo" không.
     * Trả về ca mới nhất đang mở.
     */
    public Ca getCaDangMo() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = ConnectDB.getConnection();
            // Lấy ca MỚI NHẤT mà chưa kết
            // Dùng "SELECT TOP 1" cho SQL Server, nếu là MySQL/PostgreSQL thì dùng "LIMIT 1"
            String sql = "SELECT TOP 1 * FROM Ca WHERE thoiGianKetCa IS NULL ORDER BY thoiGianVaoCa DESC";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            Ca ca = null;

            if (rs.next()) {
                // Bạn nên dùng NhanVien_DAO để lấy đầy đủ thông tin NV ở đây nếu cần
                NhanVien nv = new NhanVien();
                nv.setMaNhanVien(rs.getString("maNhanVien"));

                ca = new Ca(
                        rs.getString("maCa"),
                        rs.getTimestamp("thoiGianVaoCa").toLocalDateTime(),
                        rs.getDouble("tongTienDauCa"),
                        nv
                );
            }
            return ca;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            // Đóng tất cả kết nối
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (ps != null) ps.close(); } catch (Exception ignored) {}
            try { if (con != null) con.close(); } catch (Exception ignored) {}
        }
    }
    // Lấy ca đang mở của nhân viên (chưa kết ca)
    public Ca getCaDangLam(String maNhanVien) {
        try {
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM Ca WHERE maNhanVien = ? AND thoiGianKetCa IS NULL";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, maNhanVien);

            ResultSet rs = ps.executeQuery();
            Ca ca = null;

            if (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNhanVien(rs.getString("maNhanVien"));

                ca = new Ca(
                        rs.getString("maCa"),
                        rs.getTimestamp("thoiGianVaoCa").toLocalDateTime(),
                        rs.getDouble("tongTienDauCa"),
                        nv
                );
            }

            rs.close();
            ps.close();
            return ca;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Tính tổng tiền mặt thu được trong ca hiện tại dựa trên chi tiết hóa đơn.
     * Logic: JOIN HoaDon - ChiTietHoaDon - MonAn
     * Công thức: SUM(soLuong * giaTien)
     */

    public double tinhTongTienMat(String maNhanVien, LocalDateTime thoiGianVaoCa) {
        double tongTien = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectDB.getConnection();


            String sql = """
            SELECT SUM(cthd.soLuong * ma.giaTien) AS TongTienCa
            FROM ChiTietHoaDon cthd
            JOIN MonAn ma ON cthd.maMonAn = ma.maMonAn
            JOIN HoaDon hd ON cthd.maHoaDon = hd.maHoaDon
            WHERE hd.maNhanVien = ? 
              AND hd.trangThai = ? 
              AND hd.ngayTao >= ?
        """;

            ps = con.prepareStatement(sql);
            ps.setString(1, maNhanVien);
            ps.setString(2, "Đã thanh toán");
            ps.setTimestamp(3, Timestamp.valueOf(thoiGianVaoCa));

            rs = ps.executeQuery();
            if (rs.next()) {
                tongTien = rs.getDouble("TongTienCa");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (ps != null) ps.close(); } catch (Exception ignored) {}
            try { if (con != null) con.close(); } catch (Exception ignored) {}
        }

        return tongTien;
    }
    /**
     * Tính tổng tiền giảm giá trong ca
     */
    public double tinhTongTienGiamGia(String maNhanVien, LocalDateTime thoiGianVaoCa) {
        double tongGiam = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectDB.getConnection();

            // JOIN giữa ChiTietKMHD và HoaDon
            // Lấy tổng cột soTienGiam từ bảng ChiTietKMHD
            String sql = """
                SELECT SUM(ct.soTienGiam) 
                FROM ChiTietKMHD ct
                JOIN HoaDon h ON ct.maHoaDon = h.maHoaDon
                WHERE h.maNhanVien = ? 
                  AND h.trangThai LIKE N'Đã thanh toán' 
                  AND h.ngayTao >= ?
            """;

            ps = con.prepareStatement(sql);
            ps.setString(1, maNhanVien);
            ps.setTimestamp(2, Timestamp.valueOf(thoiGianVaoCa));

            rs = ps.executeQuery();
            if (rs.next()) {
                tongGiam = rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (ps != null) ps.close(); } catch (Exception ignored) {}
            try { if (con != null) con.close(); } catch (Exception ignored) {}
        }
        return tongGiam;
    }

    /**
     * Đếm số lượng đơn đang phục vụ (Chưa thanh toán)
     */
    public int demDonDangPhucVu(String maNhanVien, LocalDateTime thoiGianVaoCa) {
        int soLuong = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = ConnectDB.getConnection();
            // SỬA LỖI: Thay chuỗi cứng bằng ? cho cả 2 trạng thái
            String sql = """
                SELECT COUNT(*) 
                FROM HoaDon 
                WHERE maNhanVien = ? 
                  AND (trangThai = ? OR trangThai = ?) 
                  AND ngayTao >= ?
            """;

            ps = con.prepareStatement(sql);
            ps.setString(1, maNhanVien);
            ps.setString(2, "Chờ thanh toán"); // Tham số 2
            ps.setString(3, "Đang phục vụ");   // Tham số 3
            ps.setTimestamp(4, Timestamp.valueOf(thoiGianVaoCa)); // Tham số 4

            rs = ps.executeQuery();
            if (rs.next()) {
                soLuong = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(con, ps, rs);
        }
        return soLuong;
    }

    // Cập nhật lại phương thức đếm số hóa đơn cho chính xác theo thời gian thực
    public int demSoHoaDonTrongCa(String maNhanVien, LocalDateTime thoiGianVaoCa) {
        int soLuong = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectDB.getConnection();
            String sql = "SELECT COUNT(*) FROM HoaDon WHERE maNhanVien = ? AND trangThai = N'Đã thanh toán' AND ngayTao >= ?";

            ps = con.prepareStatement(sql);
            ps.setString(1, maNhanVien);
            ps.setTimestamp(2, Timestamp.valueOf(thoiGianVaoCa));

            rs = ps.executeQuery();
            if (rs.next()) {
                soLuong = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(con, ps, rs);
        }
        return soLuong;
    }
    private void closeResources(Connection con, Statement stmt, ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (Exception ignored) {}
        try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
        try { if (con != null) con.close(); } catch (Exception ignored) {}
    }

}
