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

    public int demSoHoaDonTrongCa(String maNhanVien, LocalDateTime thoiGianVaoCa ) {
        int soLuong = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectDB.getConnection();

            // LOGIC SQL:
            // 1. JOIN HoaDon và Ca thông qua maNhanVien
            // 2. Chỉ lấy những dòng mà Ngày tạo hóa đơn (ngayTao) TRÙNG VỚI Ngày vào ca (thoiGianVaoCa)
            // 3. Lọc đúng ngày được truyền vào tham số
            String sql = """
            SELECT COUNT(h.maHoaDon) 
            FROM HoaDon h
            JOIN Ca c ON h.maNhanVien = c.maNhanVien
            WHERE h.maNhanVien = ? 
              AND h.trangThai = N'Đã thanh toán'
              AND CAST(h.ngayTao AS DATE) = CAST(c.thoiGianVaoCa AS DATE)
              AND CAST(c.thoiGianVaoCa AS DATE) = CAST(? AS DATE)
        """;

            ps = con.prepareStatement(sql);

            // Tham số 1: Mã nhân viên
            ps.setString(1, maNhanVien);

            // Tham số 2: Thời gian vào ca (Code sẽ tự CAST về DATE để so sánh)
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(thoiGianVaoCa));

            rs = ps.executeQuery();
            if (rs.next()) {
                soLuong = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); if (ps != null) ps.close(); if (con != null) con.close(); } catch (Exception e) {}
        }

        return soLuong;
    }
}
