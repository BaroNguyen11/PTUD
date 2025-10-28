package dao;

import ConnectDB.ConnectDB;
import entity.BanAn;
import entity.LoaiBan;
import entity.TrangThai;
import entity.ViTri;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BanAn_DAO {

    public List<BanAn> getAllBanAn() {
        List<BanAn> dsBanAn = new ArrayList<>();
        String sql = "SELECT * FROM BanAn";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String maBan = rs.getString("maBan");
                String rawLoai = rs.getString("loai");
                String rawTrangThai = rs.getString("trangThai");
                String rawViTri = rs.getString("viTri");

                LoaiBan loai = LoaiBan.fromString(rawLoai);
                TrangThai trangThai = TrangThai.fromString(rawTrangThai);
                ViTri viTri = ViTri.fromString(rawViTri);

                 // Thêm kiểm tra null trước khi tạo BanAn
                if (loai == null || trangThai == null || viTri == null) {
                    System.err.println("LỖI DAO (getAllBanAn): Dữ liệu không hợp lệ cho bàn '" + maBan +
                                       "'. Loai='" + rawLoai + "', TrangThai='" + rawTrangThai + "', ViTri='" + rawViTri + "'");
                    continue; // Bỏ qua bàn này nếu có lỗi
                }

                BanAn ban = new BanAn(maBan, loai, trangThai, viTri);
                dsBanAn.add(ban);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsBanAn;
    }

    // Đổi tên hàm này lại thành getBanAnByViTri cho thống nhất
    public List<BanAn> getBanAnTheoViTri(ViTri viTri) {
        List<BanAn> dsBanAn = new ArrayList<>();
        String sql = "SELECT * FROM BanAn WHERE viTri = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Truy vấn dùng tên tiếng Việt từ Enum
            ps.setString(1, viTri.getTenViTri());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String maBan = rs.getString("maBan");
                    String rawLoai = rs.getString("loai");
                    String rawTrangThai = rs.getString("trangThai");
                    String rawViTri = rs.getString("viTri"); // Lấy lại ViTri để kiểm tra

                    LoaiBan loai = LoaiBan.fromString(rawLoai);
                    TrangThai trangThai = TrangThai.fromString(rawTrangThai);
                    ViTri vt = ViTri.fromString(rawViTri);

                    // Thêm kiểm tra null
                    if (loai == null || trangThai == null || vt == null) {
                         System.err.println("LỖI DAO (getBanAnTheoViTri): Dữ liệu không hợp lệ cho bàn '" + maBan +
                                           "'. Loai='" + rawLoai + "', TrangThai='" + rawTrangThai + "', ViTri='" + rawViTri + "'");
                        continue; // Bỏ qua bàn này
                    }

                    BanAn ban = new BanAn(maBan, loai, trangThai, vt);
                    dsBanAn.add(ban);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsBanAn;
    }

    public boolean updateTrangThaiBan(BanAn ban, TrangThai trangThaiMoi) {
        String sql = "UPDATE BanAn SET trangThai = ? WHERE maBan = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Cập nhật dùng tên tiếng Việt từ Enum
            ps.setString(1, trangThaiMoi.getThongTin());
            ps.setString(2, ban.getMaBan());

            int n = ps.executeUpdate();
            return n > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}