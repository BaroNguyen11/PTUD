package dao;

import ConnectDB.ConnectDB;
import entity.BanAn;
import entity.LoaiBan;
import entity.PhieuDatBan;
import entity.TrangThai;
import entity.ViTri;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public List<BanAn> getBanAnTheoViTri(ViTri viTri) {
        List<BanAn> dsBanAn = new ArrayList<>();
        String sql = "SELECT * FROM BanAn WHERE viTri = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

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
                        continue;
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
    public List<BanAn> getTrangThaiBanTheoNgayVaViTri(ViTri viTri, LocalDate ngay) {
        List<BanAn> dsBanAnKetQua = new ArrayList<>();

        // 1. Lấy trạng thái vật lý của bàn (từ bảng BanAn).
        List<BanAn> dsBanGoc = getBanAnTheoViTri(viTri);

        // 2. Lấy tất cả PDB có trạng thái cần quan tâm (Đã đặt, Đang dùng) cho ngày đó
        Map<String, PhieuDatBan> mapPDBTheoBan = getPhieuDatBanMapByNgay(ngay);

        for (BanAn ban : dsBanGoc) {
            TrangThai trangThaiVatLy = ban.getTrangThai();
            TrangThai trangThaiPDB = TrangThai.TRONG;
            PhieuDatBan pdb = mapPDBTheoBan.get(ban.getMaBan());

            if (pdb != null) {
                // LẤY STRING VÀ CHUYỂN ĐỔI SANG ENUM
                String rawTrangThai = pdb.getTrangThai();
                TrangThai trangThaiTuPDB = TrangThai.fromString(rawTrangThai); // <-- CHUYỂN ĐỔI CHÍNH

                // Kiểm tra: Nếu chuyển đổi thành công VÀ là trạng thái quan trọng
                if (trangThaiTuPDB != null) {
                    // Các trạng thái DA_DAT và DANG_SU_DUNG là trạng thái quan trọng (như đã lọc trong SQL)
                    trangThaiPDB = trangThaiTuPDB;
                }
            }

            TrangThai trangThaiCuoiCung;

            if (ngay.isEqual(LocalDate.now())) {

                if (trangThaiVatLy == TrangThai.DANG_SU_DUNG) {
                    trangThaiCuoiCung = TrangThai.DANG_SU_DUNG;
                } else {
                    trangThaiCuoiCung = trangThaiPDB;
                }
            } else {
                trangThaiCuoiCung = trangThaiPDB;
            }

            ban.setTrangThai(trangThaiCuoiCung);
            dsBanAnKetQua.add(ban);
        }
        return dsBanAnKetQua;
    }
    public Map<String, PhieuDatBan> getPhieuDatBanMapByNgay(LocalDate ngay) {
        Map<String, PhieuDatBan> mapPDB = new java.util.HashMap<>();

        String sql = "SELECT * FROM PhieuDatBan WHERE CONVERT(date, thoiGianBatDau) = ? AND trangThai IN (?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(ngay));

            // Chỉ lấy trạng thái cần quan tâm: Đã đặt và Đang dùng
            ps.setString(2, "Đã đặt");
            ps.setString(3, "Đang dùng");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Lấy chuỗi trạng thái trực tiếp từ CSDL (vì Entity không được sửa)
                    String rawTrangThai = rs.getString("trangThai");
                    PhieuDatBan pdb = new PhieuDatBan(
                            rs.getString("maPhieu"),
                            rs.getTimestamp("thoiGianBatDau").toLocalDateTime(),
                            rawTrangThai,
                            rs.getInt("soNguoi"),
                            rs.getString("ghiChu"),
                            null, // KhachHang
                            null, // BanAn
                            null, // NhanVien
                            null  // HoaDon
                    );

                    String maBan = rs.getString("maBan");
                    mapPDB.put(maBan, pdb);
                }
            }
        } catch (SQLException e) {
            System.err.println("LỖI DAO: Lấy Phiếu Đặt Bàn theo ngày thất bại.");
            e.printStackTrace();
        }
        return mapPDB;
    }

    public boolean isBanDangSuDungHienTai(String maBan) {
        String sql = "SELECT trangThai FROM BanAn WHERE maBan = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maBan);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String rawTrangThai = rs.getString("trangThai");
                    return TrangThai.fromString(rawTrangThai) == TrangThai.DANG_SU_DUNG;
                }
            }
        } catch (SQLException e) {
            System.err.println("LỖI DAO: Kiểm tra trạng thái vật lý thất bại.");
            e.printStackTrace();
        }
        return false;
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

    public List<String> getDanhSachBanCungHoaDon(String maHoaDon) {
        List<String> dsBan = new ArrayList<>();
        // Lấy MaBan từ các PDB cùng chung MaHoaDon
        String sql = "SELECT DISTINCT maBan FROM PhieuDatBan WHERE maHoaDon = ? AND maBan IS NOT NULL";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maHoaDon);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    dsBan.add(rs.getString("maBan"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách bàn cùng hóa đơn: " + e.getMessage());
        }
        return dsBan;
    }


    public String getMaHoaDonTuBan(String maBan, LocalDate ngay) {
        String maHD = null;

        String sql = "SELECT TOP 1 maHoaDon FROM PhieuDatBan " +
                "WHERE maBan = ? AND CAST(thoiGianBatDau AS DATE) = CAST(? AS DATE) " +
                "AND trangThai IN (N'Đã đặt', N'Đang dùng') AND maHoaDon IS NOT NULL " +
                "ORDER BY thoiGianBatDau DESC";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maBan);
            ps.setDate(2, java.sql.Date.valueOf(ngay));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    maHD = rs.getString("maHoaDon");
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy Mã Hóa Đơn từ bàn: " + e.getMessage());
        }
        return maHD;
    }
}