package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entity.ChiTietHoaDon;
import ConnectDB.ConnectDB;
import entity.HoaDon;
import entity.MonAn;
public class ChiTietHoaDon_DAO {

    public boolean themChiTietHoaDon(ChiTietHoaDon cthd) {
        String sql = "INSERT INTO ChiTietHoaDon (maHoaDon, maMonAn, soLuong) "
                + "VALUES (?, ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            // 1. Ma Hoa Don
            stmt.setString(1, cthd.getHoaDon().getMaHoaDon());

            // 2. Ma Mon An
            stmt.setString(2, cthd.getMonAn().getMaMonAn());

            // 3. So Luong
            stmt.setInt(3, cthd.getSoLuong());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi thêm Chi Tiết Hóa Đơn: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public static List<ChiTietHoaDon> getChiTietHoaDonByMaHD(String maHD) {
        List<ChiTietHoaDon> ds = new ArrayList<>();

        String sql = """
            SELECT maHoaDon, maMonAn, soLuong
            FROM ChiTietHoaDon
            WHERE maHoaDon = ?
        """;

        try (PreparedStatement ps = ConnectDB.getConnection().prepareStatement(sql)) {

            ps.setString(1, maHD);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    ChiTietHoaDon cthd = new ChiTietHoaDon();

                    HoaDon hd = new HoaDon();
                    hd.setMaHoaDon(maHD);

                    MonAn mon = MonAn_DAO.getMonAnByMa(rs.getString("maMonAn"));

                    cthd.setHoaDon(hd);
                    cthd.setMonAn(mon);
                    cthd.setSoLuong(rs.getInt("soLuong"));

                    ds.add(cthd);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ds;
    }

    public boolean themHoacUpdate(String maHD, String maMon, int soLuongThem, double giaBan) {
        String sqlCheck = """
            SELECT soLuong
            FROM ChiTietHoaDon
            WHERE maHoaDon = ? AND maMonAn = ?
        """;

        String sqlInsert = """
            INSERT INTO ChiTietHoaDon (maHoaDon, maMonAn, soLuong)
            VALUES (?, ?, ?)
        """;

        String sqlUpdate = """
            UPDATE ChiTietHoaDon
            SET soLuong = soLuong + ?
            WHERE maHoaDon = ? AND maMonAn = ?
        """;

        try (Connection con = ConnectDB.getConnection()) {

            // 1️⃣ KIỂM TRA XEM MÓN ĐÃ TỒN TẠI CHƯA
            try (PreparedStatement psCheck = con.prepareStatement(sqlCheck)) {
                psCheck.setString(1, maHD);
                psCheck.setString(2, maMon);

                ResultSet rs = psCheck.executeQuery();

                if (rs.next()) {
                    // 🔄 ĐÃ TỒN TẠI → UPDATE SỐ LƯỢNG
                    try (PreparedStatement psUpdate = con.prepareStatement(sqlUpdate)) {
                        psUpdate.setInt(1, soLuongThem);
                        psUpdate.setString(2, maHD);
                        psUpdate.setString(3, maMon);
                        return psUpdate.executeUpdate() > 0;
                    }
                }
            }

            // 2️⃣ CHƯA TỒN TẠI → INSERT MỚI
            try (PreparedStatement psInsert = con.prepareStatement(sqlInsert)) {
                psInsert.setString(1, maHD);
                psInsert.setString(2, maMon);
                psInsert.setInt(3, soLuongThem);
                return psInsert.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}