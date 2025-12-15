package dao;

import ConnectDB.ConnectDB;
import entity.BanAn;
import entity.LoaiBan;
import entity.TrangThai;
import entity.ViTri;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuanLyBan_DAO {

    // Hàm map chung để tránh lặp code
    private BanAn mapBanAn(ResultSet rs) throws SQLException {
        String loaiStr = rs.getString("loai");
        String ttStr = rs.getString("trangThai");
        String vtStr = rs.getString("viTri");

        return new BanAn(
                rs.getString("maBan"),
                loaiStr != null ? LoaiBan.fromDB(loaiStr) : null,
                ttStr != null ? TrangThai.fromDB(ttStr) : null,
                vtStr != null ? ViTri.fromDB(vtStr) : null
        );
    }

    // Lấy tất cả bàn ăn
    public List<BanAn> getAllBanAn() {
        List<BanAn> list = new ArrayList<>();
        String sql = "SELECT * FROM BanAn ORDER BY maBan";

        try (Connection con = ConnectDB.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapBanAn(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    // Lấy 1 bàn theo mã
    public BanAn getBanAnByMa(String maBan) {
        String sql = "SELECT * FROM BanAn WHERE maBan = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maBan);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapBanAn(rs);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Tìm kiếm theo mã hoặc vị trí
    public List<BanAn> searchBanAn(String keyword) {
        List<BanAn> list = new ArrayList<>();
        String sql = "SELECT * FROM BanAn WHERE UPPER(maBan) LIKE ? OR UPPER(viTri) LIKE ? ORDER BY maBan";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String k = "%" + keyword.toUpperCase() + "%";

            ps.setString(1, k);
            ps.setString(2, k);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapBanAn(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    // Thêm bàn ăn
    public boolean addBanAn(BanAn b) {
        String sql = "INSERT INTO BanAn (maBan, loai, trangThai, viTri) VALUES (?, ?, ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, b.getMaBan());
            // Sửa .name() thành .getTenLoai() (nếu LoaiBan có hàm này) hoặc xử lý tương tự
            ps.setString(2, b.getLoai() != null ? b.getLoai().getTenLoai() : null);

            // --- SỬA Ở ĐÂY ---
            // Thay .name() bằng .getThongTin()
            ps.setString(3, b.getTrangThai() != null ? b.getTrangThai().getThongTin() : null);

            // Tương tự với ViTri, nên dùng phương thức lấy chuỗi hiển thị
            ps.setString(4, b.getViTri() != null ? b.getViTri().getTenViTri() : null); // Giả sử ViTri có hàm getTenViTri

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // Cập nhật bàn ăn
    public boolean updateBanAn(BanAn b) {
        String sql = "UPDATE BanAn SET loai = ?, trangThai = ?, viTri = ? WHERE maBan = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Sửa tương tự như trên
            ps.setString(1, b.getLoai() != null ? b.getLoai().getTenLoai() : null);

            // --- SỬA Ở ĐÂY ---
            ps.setString(2, b.getTrangThai() != null ? b.getTrangThai().getThongTin() : null);

            ps.setString(3, b.getViTri() != null ? b.getViTri().getTenViTri() : null);
            ps.setString(4, b.getMaBan());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // Xóa bàn ăn
    public boolean deleteBanAn(String maBan) {
        String sql = "DELETE FROM BanAn WHERE maBan = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maBan);
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // Sinh mã tự động (tối ưu bằng MAX)
    public String generateMaBan() {
        String sql = "SELECT MAX(CAST(SUBSTRING(maBan, 3, LEN(maBan)-2) AS INT)) AS maxNum FROM BanAn WHERE maBan LIKE 'BA%'";

        try (Connection con = ConnectDB.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                int max = rs.getInt("maxNum");
                return String.format("BA%03d", max + 1);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return "BA0001"; // Trường hợp chưa có dữ liệu
    }

}