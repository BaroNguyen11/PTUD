//package dao;
//
//import ConnectDB.ConnectDB;
//import entity.BanAn;
//import entity.LoaiBan;
//import entity.PhieuDatBan;
//import entity.TrangThai;
//import entity.ViTri;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class BanAn_DAO {
//
//	public  static BanAn getByMaBan(String maBan) {
//        if (maBan == null || maBan.trim().isEmpty()) {
//            return null;
//        }
//
//        String sql = "SELECT * FROM BanAn WHERE maBan = ?";
//
//        try (Connection conn = ConnectDB.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setString(1, maBan);
//            ResultSet rs = pstmt.executeQuery();
//
//            if (rs.next()) {
//                String rawLoai = rs.getString("loai");
//                String rawTrangThai = rs.getString("trangThai");
//                String rawViTri = rs.getString("viTri");
//
//                LoaiBan loai = LoaiBan.fromString(rawLoai);
//                TrangThai trangThai = TrangThai.fromString(rawTrangThai);
//                ViTri viTri = ViTri.fromString(rawViTri);
//
//                // Thêm kiểm tra null trước khi tạo BanAn
//                if (loai == null || trangThai == null || viTri == null) {
//                    System.err.println("LỖI DAO (getAllBanAn): Dữ liệu không hợp lệ cho bàn '" + maBan +
//                            "'. Loai='" + rawLoai + "', TrangThai='" + rawTrangThai + "', ViTri='" + rawViTri + "'");
//                }
//
//                BanAn ban = new BanAn(maBan, loai, trangThai, viTri);
//
//                return ban;
//            }
//
//        } catch (SQLException e) {
//            System.err.println("❌ Lỗi lấy bàn ăn theo mã: " + e.getMessage());
//            e.printStackTrace();
//        }
//
//        return null;  // Không tìm thấy
//    }
//
//
//    public List<BanAn> getAllBanAn() {
//        List<BanAn> dsBanAn = new ArrayList<>();
//        String sql = "SELECT * FROM BanAn";
//
//        try (Connection con = ConnectDB.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//
//            while (rs.next()) {
//                String maBan = rs.getString("maBan");
//                String rawLoai = rs.getString("loai");
//                String rawTrangThai = rs.getString("trangThai");
//                String rawViTri = rs.getString("viTri");
//
//                LoaiBan loai = LoaiBan.fromString(rawLoai);
//                TrangThai trangThai = TrangThai.fromString(rawTrangThai);
//                ViTri viTri = ViTri.fromString(rawViTri);
//
//                // Thêm kiểm tra null trước khi tạo BanAn
//                if (loai == null || trangThai == null || viTri == null) {
//                    System.err.println("LỖI DAO (getAllBanAn): Dữ liệu không hợp lệ cho bàn '" + maBan +
//                            "'. Loai='" + rawLoai + "', TrangThai='" + rawTrangThai + "', ViTri='" + rawViTri + "'");
//                    continue; // Bỏ qua bàn này nếu có lỗi
//                }
//
//                BanAn ban = new BanAn(maBan, loai, trangThai, viTri);
//                dsBanAn.add(ban);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return dsBanAn;
//    }
//
//    public List<BanAn> getBanAnTheoViTri(ViTri viTri) {
//        List<BanAn> dsBanAn = new ArrayList<>();
//        String sql = "SELECT * FROM BanAn WHERE viTri = ?";
//
//        try (Connection con = ConnectDB.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setString(1, viTri.getTenViTri());
//
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    String maBan = rs.getString("maBan");
//                    String rawLoai = rs.getString("loai");
//                    String rawTrangThai = rs.getString("trangThai");
//                    String rawViTri = rs.getString("viTri"); // Lấy lại ViTri để kiểm tra
//
//                    LoaiBan loai = LoaiBan.fromString(rawLoai);
//                    TrangThai trangThai = TrangThai.fromString(rawTrangThai);
//                    ViTri vt = ViTri.fromString(rawViTri);
//
//                    // Thêm kiểm tra null
//                    if (loai == null || trangThai == null || vt == null) {
//                        System.err.println("LỖI DAO (getBanAnTheoViTri): Dữ liệu không hợp lệ cho bàn '" + maBan +
//                                "'. Loai='" + rawLoai + "', TrangThai='" + rawTrangThai + "', ViTri='" + rawViTri + "'");
//                        continue;
//                    }
//
//                    BanAn ban = new BanAn(maBan, loai, trangThai, vt);
//                    dsBanAn.add(ban);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return dsBanAn;
//    }
////    public List<BanAn> getTrangThaiBanTheoNgayVaViTri(ViTri viTri, LocalDate ngay) {
////        List<BanAn> dsBanAnKetQua = new ArrayList<>();
////
////        // 1. Lấy trạng thái vật lý của bàn (từ bảng BanAn).
////        List<BanAn> dsBanGoc = getBanAnTheoViTri(viTri);
////
////        // 2. Lấy tất cả PDB có trạng thái cần quan tâm (Đã đặt, Đang dùng) cho ngày đó
////        Map<String, PhieuDatBan> mapPDBTheoBan = getPhieuDatBanMapByNgay(ngay);
////
////        for (BanAn ban : dsBanGoc) {
////            TrangThai trangThaiVatLy = ban.getTrangThai();
////            TrangThai trangThaiPDB = TrangThai.TRONG;
////            PhieuDatBan pdb = mapPDBTheoBan.get(ban.getMaBan());
////
////            if (pdb != null) {
////                // LẤY STRING VÀ CHUYỂN ĐỔI SANG ENUM
////                String rawTrangThai = pdb.getTrangThai();
////                TrangThai trangThaiTuPDB = TrangThai.fromString(rawTrangThai); // <-- CHUYỂN ĐỔI CHÍNH
////
////                // Kiểm tra: Nếu chuyển đổi thành công VÀ là trạng thái quan trọng
////                if (trangThaiTuPDB != null) {
////                    // Các trạng thái DA_DAT và DANG_SU_DUNG là trạng thái quan trọng (như đã lọc trong SQL)
////                    trangThaiPDB = trangThaiTuPDB;
////                }
////            }
////
////            TrangThai trangThaiCuoiCung;
////
////            if (ngay.isEqual(LocalDate.now())) {
////
////                if (trangThaiVatLy == TrangThai.DANG_SU_DUNG) {
////                    trangThaiCuoiCung = TrangThai.DANG_SU_DUNG;
////                } else {
////                    trangThaiCuoiCung = trangThaiPDB;
////                }
////            } else {
////                trangThaiCuoiCung = trangThaiPDB;
////            }
////
////            ban.setTrangThai(trangThaiCuoiCung);
////            dsBanAnKetQua.add(ban);
////        }
////        return dsBanAnKetQua;
////    }
//public List<BanAn> getTrangThaiBanTheoNgayVaViTri(ViTri viTri, LocalDate ngay) {
//    List<BanAn> dsBanAnKetQua = new ArrayList<>();
//
//    // Bước 1: Lấy danh sách bàn vật lý (Trạng thái gốc trong DB)
//    List<BanAn> dsBanGoc = getBanAnTheoViTri(viTri);
//
//    // Bước 2: Lấy thông tin đặt bàn trong ngày được chọn
//    Map<String, PhieuDatBan> mapPDBTheoBan = getPhieuDatBanMapByNgay(ngay);
//
//    for (BanAn ban : dsBanGoc) {
//        TrangThai trangThaiVatLy = ban.getTrangThai(); // Trạng thái thực tế trong DB
//        TrangThai trangThaiPDB = TrangThai.TRONG;      // Trạng thái từ phiếu đặt
//
//        // Kiểm tra xem có phiếu đặt bàn nào cho ngày này không
//        PhieuDatBan pdb = mapPDBTheoBan.get(ban.getMaBan());
//        if (pdb != null) {
//            TrangThai temp = TrangThai.fromString(pdb.getTrangThai());
//            if (temp != null) trangThaiPDB = temp;
//        }
//
//        TrangThai trangThaiHienThi;
//
//        // --- LOGIC XỬ LÝ "KHÁCH NGỒI QUA ĐÊM" ---
//        if (ngay.isEqual(LocalDate.now())) {
//            // Nếu xem ngày HÔM NAY:
//            // Ưu tiên trạng thái vật lý: Nếu DB báo "Đang dùng", thì chắc chắn là đang có khách
//            // (Bất kể khách vào từ hôm qua hay tuần trước, miễn chưa thanh toán là còn Status này)
//            if (trangThaiVatLy == TrangThai.DANG_SU_DUNG) {
//                trangThaiHienThi = TrangThai.DANG_SU_DUNG;
//            } else {
//                // Nếu vật lý trống, mới xét đến lịch đặt bàn hôm nay
//                trangThaiHienThi = trangThaiPDB;
//            }
//        } else {
//            // Nếu xem QUÁ KHỨ hoặc TƯƠNG LAI:
//            // Chỉ hiển thị theo lịch sử đặt bàn (PhieuDatBan)
//            trangThaiHienThi = trangThaiPDB;
//        }
//
//        ban.setTrangThai(trangThaiHienThi);
//        dsBanAnKetQua.add(ban);
//    }
//    return dsBanAnKetQua;
//}
//    public Map<String, PhieuDatBan> getPhieuDatBanMapByNgay(LocalDate ngay) {
//        Map<String, PhieuDatBan> mapPDB = new java.util.HashMap<>();
//
//        String sql = "SELECT * FROM PhieuDatBan WHERE CONVERT(date, thoiGianBatDau) = ? AND trangThai IN (?, ?)";
//
//        try (Connection con = ConnectDB.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setDate(1, java.sql.Date.valueOf(ngay));
//
//            // Chỉ lấy trạng thái cần quan tâm: Đã đặt và Đang dùng
//            ps.setString(2, "Đã đặt");
//            ps.setString(3, "Đang dùng");
//
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    // Lấy chuỗi trạng thái trực tiếp từ CSDL (vì Entity không được sửa)
//                    String rawTrangThai = rs.getString("trangThai");
//                    PhieuDatBan pdb = new PhieuDatBan(
//                            rs.getString("maPhieu"),
//                            rs.getTimestamp("thoiGianBatDau").toLocalDateTime(),
//                            rawTrangThai,
//                            rs.getInt("soNguoi"),
//                            rs.getString("ghiChu"),
//                            null, // KhachHang
//                            null, // BanAn
//                            null, // NhanVien
//                            null  // HoaDon
//                    );
//
//                    String maBan = rs.getString("maBan");
//                    mapPDB.put(maBan, pdb);
//                }
//            }
//        } catch (SQLException e) {
//            System.err.println("LỖI DAO: Lấy Phiếu Đặt Bàn theo ngày thất bại.");
//            e.printStackTrace();
//        }
//        return mapPDB;
//    }
//
//    public boolean isBanDangSuDungHienTai(String maBan) {
//        String sql = "SELECT trangThai FROM BanAn WHERE maBan = ?";
//        try (Connection con = ConnectDB.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setString(1, maBan);
//
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    String rawTrangThai = rs.getString("trangThai");
//                    return TrangThai.fromString(rawTrangThai) == TrangThai.DANG_SU_DUNG;
//                }
//            }
//        } catch (SQLException e) {
//            System.err.println("LỖI DAO: Kiểm tra trạng thái vật lý thất bại.");
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public boolean updateTrangThaiBan(BanAn ban, TrangThai trangThaiMoi) {
//        String sql = "UPDATE BanAn SET trangThai = ? WHERE maBan = ?";
//        try (Connection con = ConnectDB.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//
//            // Cập nhật dùng tên tiếng Việt từ Enum
//            ps.setString(1, trangThaiMoi.getThongTin());
//            ps.setString(2, ban.getMaBan());
//
//            int n = ps.executeUpdate();
//            return n > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public List<String> getDanhSachBanCungHoaDon(String maHoaDon) {
//        List<String> dsBan = new ArrayList<>();
//        // Lấy MaBan từ các PDB cùng chung MaHoaDon
//        String sql = "SELECT DISTINCT maBan FROM PhieuDatBan WHERE maHoaDon = ? AND maBan IS NOT NULL";
//
//        try (Connection con = ConnectDB.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setString(1, maHoaDon);
//
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    dsBan.add(rs.getString("maBan"));
//                }
//            }
//        } catch (SQLException e) {
//            System.err.println("Lỗi SQL khi lấy danh sách bàn cùng hóa đơn: " + e.getMessage());
//        }
//        return dsBan;
//    }
//
//
//    // Bỏ tham số LocalDate ngay vì không cần thiết nữa
//    public String getMaHoaDonTuBan(String maBan) {
//        String maHD = null;
//
//        // Bỏ đoạn so sánh ngày: CAST(thoiGianBatDau AS DATE) = ...
//        String sql = "SELECT TOP 1 maHoaDon FROM PhieuDatBan " +
//                "WHERE maBan = ? " +
//                "AND trangThai IN (N'Đã đặt', N'Đang dùng') " + // Quan trọng nhất là trạng thái này
//                "AND maHoaDon IS NOT NULL " +
//                "ORDER BY thoiGianBatDau DESC"; // Lấy cái mới nhất
//
//        try (Connection con = ConnectDB.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setString(1, maBan);
//            // ps.setDate(2, ...); // Xóa dòng set ngày này đi
//
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    maHD = rs.getString("maHoaDon");
//                }
//            }
//        } catch (SQLException e) {
//            System.err.println("Lỗi SQL khi lấy Mã Hóa Đơn từ bàn: " + e.getMessage());
//        }
//        return maHD;
//    }
//}
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BanAn_DAO {

    // 1. Lấy thông tin bàn theo Mã bàn
    public static BanAn getByMaBan(String maBan) {
        if (maBan == null || maBan.trim().isEmpty()) return null;

        String sql = "SELECT * FROM BanAn WHERE maBan = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maBan);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToBanAn(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 2. Lấy tất cả bàn
    public List<BanAn> getAllBanAn() {
        List<BanAn> dsBanAn = new ArrayList<>();
        String sql = "SELECT * FROM BanAn";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                BanAn ban = mapResultSetToBanAn(rs);
                if (ban != null) dsBanAn.add(ban);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsBanAn;
    }

    // 3. Lấy bàn theo Vị trí (Tầng 1, Tầng 2...)
    public List<BanAn> getBanAnTheoViTri(ViTri viTri) {
        List<BanAn> dsBanAn = new ArrayList<>();
        String sql = "SELECT * FROM BanAn WHERE viTri = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, viTri.getTenViTri());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BanAn ban = mapResultSetToBanAn(rs);
                    if (ban != null) dsBanAn.add(ban);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsBanAn;
    }

    /**
     * =================================================================================
     * HÀM QUAN TRỌNG NHẤT: XỬ LÝ TRẠNG THÁI HIỂN THỊ TRÊN GIAO DIỆN
     * =================================================================================
     */
    public List<BanAn> getTrangThaiBanTheoNgayVaViTri(ViTri viTri, LocalDate ngay) {
        List<BanAn> dsBanAnKetQua = new ArrayList<>();

        // Bước 1: Lấy danh sách bàn vật lý (Trạng thái gốc trong DB)
        List<BanAn> dsBanGoc = getBanAnTheoViTri(viTri);

        // Bước 2: Lấy thông tin phiếu đặt/đang dùng.
        // Logic trong hàm này đã được sửa để lấy cả khách "ngồi qua đêm"
        Map<String, PhieuDatBan> mapPDBTheoBan = getPhieuDatBanMapByNgay(ngay);

        for (BanAn ban : dsBanGoc) {
            TrangThai trangThaiVatLy = ban.getTrangThai(); // Trạng thái thực tế trong bảng BanAn
            TrangThai trangThaiPDB = TrangThai.TRONG;      // Trạng thái từ phiếu

            // Kiểm tra xem có phiếu đặt/đang dùng nào cho bàn này không
            PhieuDatBan pdb = mapPDBTheoBan.get(ban.getMaBan());
            if (pdb != null) {
                TrangThai temp = TrangThai.fromString(pdb.getTrangThai());
                if (temp != null) trangThaiPDB = temp;
            }

            TrangThai trangThaiHienThi = TrangThai.TRONG;

            // --- LOGIC XỬ LÝ HIỂN THỊ ---
            if (ngay.isEqual(LocalDate.now())) {
                // Nếu xem ngày HÔM NAY:
                // Ưu tiên 1: Nếu bảng BanAn báo đang dùng -> Chắc chắn đang dùng (bất kể ngày nào)
                if (trangThaiVatLy == TrangThai.DANG_SU_DUNG) {
                    trangThaiHienThi = TrangThai.DANG_SU_DUNG;
                }
                // Ưu tiên 2: Nếu bảng BanAn trống, kiểm tra xem có phiếu "Đang dùng" (từ hôm qua) hoặc "Đã đặt" không
                else if (trangThaiPDB == TrangThai.DANG_SU_DUNG) {
                    trangThaiHienThi = TrangThai.DANG_SU_DUNG;
                }
                else {
                    trangThaiHienThi = trangThaiPDB; // Thường là TRONG hoặc DA_DAT
                }
            } else {
                // Nếu xem QUÁ KHỨ hoặc TƯƠNG LAI:
                // Chỉ hiển thị theo lịch sử phiếu (PhieuDatBan)
                trangThaiHienThi = trangThaiPDB;
            }

            ban.setTrangThai(trangThaiHienThi);
            dsBanAnKetQua.add(ban);
        }
        return dsBanAnKetQua;
    }

    /**
     * Helper: Lấy map phiếu đặt bàn theo ngày
     * [ĐÃ SỬA]: Nếu xem ngày hôm nay, lấy luôn cả những phiếu "Đang dùng" từ quá khứ
     */
    public Map<String, PhieuDatBan> getPhieuDatBanMapByNgay(LocalDate ngay) {
        Map<String, PhieuDatBan> mapPDB = new HashMap<>();
        String sql;

        // LOGIC MỚI:
        if (ngay.isEqual(LocalDate.now())) {
            // Nếu xem ngày HÔM NAY: Lấy phiếu của ngày hôm nay HOẶC phiếu đang dùng (bất kể ngày nào)
            sql = "SELECT * FROM PhieuDatBan WHERE " +
                    "(CONVERT(date, thoiGianBatDau) = ? AND trangThai IN (?, ?)) " + // Lấy phiếu đúng ngày
                    "OR (trangThai = ?)"; // Lấy phiếu đang treo (Đang dùng) dù là từ hôm qua
        } else {
            // Nếu xem ngày KHÁC: Chỉ lấy đúng ngày đó
            sql = "SELECT * FROM PhieuDatBan WHERE CONVERT(date, thoiGianBatDau) = ? AND trangThai IN (?, ?)";
        }

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(ngay));
            ps.setString(2, "Đã đặt");
            ps.setString(3, "Đang dùng");

            // Nếu là hôm nay thì set tham số thứ 4 cho điều kiện OR
            if (ngay.isEqual(LocalDate.now())) {
                ps.setString(4, "Đang dùng");
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PhieuDatBan pdb = new PhieuDatBan(
                            rs.getString("maPhieu"),
                            rs.getTimestamp("thoiGianBatDau").toLocalDateTime(),
                            rs.getString("trangThai"),
                            rs.getInt("soNguoi"),
                            rs.getString("ghiChu"),
                            null, null, null, null
                    );
                    mapPDB.put(rs.getString("maBan"), pdb);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mapPDB;
    }

    // 4. Update trạng thái bàn (Khi Check-in / Thanh toán)
    public boolean updateTrangThaiBan(BanAn ban, TrangThai trangThaiMoi) {
        String sql = "UPDATE BanAn SET trangThai = ? WHERE maBan = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, trangThaiMoi.getThongTin()); // Lưu chuỗi tiếng Việt vào DB
            ps.setString(2, ban.getMaBan());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. Kiểm tra bàn có đang dùng thật không
    public boolean isBanDangSuDungHienTai(String maBan) {
        String sql = "SELECT trangThai FROM BanAn WHERE maBan = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maBan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return TrangThai.fromString(rs.getString("trangThai")) == TrangThai.DANG_SU_DUNG;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 6. Lấy danh sách bàn gộp
    public List<String> getDanhSachBanCungHoaDon(String maHoaDon) {
        List<String> dsBan = new ArrayList<>();
        String sql = "SELECT DISTINCT maBan FROM PhieuDatBan WHERE maHoaDon = ? AND maBan IS NOT NULL";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHoaDon);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) dsBan.add(rs.getString("maBan"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsBan;
    }

    // 7. Lấy mã hóa đơn hiện tại của bàn
    public String getMaHoaDonTuBan(String maBan) {
        // Ưu tiên lấy hóa đơn của phiếu Đang Dùng gần nhất
        String sql = "SELECT TOP 1 maHoaDon FROM PhieuDatBan " +
                "WHERE maBan = ? " +
                "AND (trangThai = N'Đang dùng' OR trangThai = N'Đã đặt')" +
                "AND maHoaDon IS NOT NULL " +
                "ORDER BY thoiGianBatDau DESC";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maBan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("maHoaDon");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // --- Helper mapping từ ResultSet sang Object BanAn ---
    private static BanAn mapResultSetToBanAn(ResultSet rs) throws SQLException {
        String maBan = rs.getString("maBan");
        String rawLoai = rs.getString("loai");
        String rawTrangThai = rs.getString("trangThai");
        String rawViTri = rs.getString("viTri");

        LoaiBan loai = LoaiBan.fromString(rawLoai);
        TrangThai trangThai = TrangThai.fromString(rawTrangThai);
        ViTri viTri = ViTri.fromString(rawViTri);

        if (loai == null || trangThai == null || viTri == null) {
            System.err.println("WARN: Dữ liệu bàn " + maBan + " không khớp Enum.");
            return null;
        }
        return new BanAn(maBan, loai, trangThai, viTri);
    }
}