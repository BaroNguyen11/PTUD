package dao;

import java.sql.*;

import entity.BanAn;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
import entity.PhieuDatBan;
import entity.TrangThai;
import ConnectDB.ConnectDB;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class PhieuDatBan_DAO {

    private static final DateTimeFormatter SQL_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private BanAn_DAO banAn_DAO;

    public PhieuDatBan_DAO() {
        // Constructor mặc định (Nếu bạn khởi tạo BanAn_DAO ở đây)
        this.banAn_DAO = new BanAn_DAO();
    }

    // Hoặc nếu bạn muốn truyền từ bên ngoài (cách linh hoạt hơn)
    public PhieuDatBan_DAO(BanAn_DAO banAn_DAO) {
        this.banAn_DAO = banAn_DAO;

    }

    public boolean themPhieuDatBan(PhieuDatBan pdb, String trangThaiPhieu) {

        LocalDate ngayDat = pdb.getThoiGianBatDau().toLocalDate();
        // 1. Tự sinh mã mới

        String maPDBMoi = taoMaPhieuMoi(ngayDat);

        // 2. CÂU LỆNH SQL ĐÃ SỬA: Thêm cột maPhieu
        String sql = "INSERT INTO PhieuDatBan (maPhieu, thoiGianBatDau, trangThai, soNguoi, ghiChu, maKhachHang, maBan, maNhanVien, maHoaDon) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            // --- Gán giá trị ---

            // 1. MaPhieu mới
            stmt.setString(1, maPDBMoi);

            // 2. ThoiGianBatDau (Chuyển LocalDateTime sang String cho SQL)
            String thoiGianSQL = pdb.getThoiGianBatDau().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            stmt.setString(2, thoiGianSQL);

            stmt.setString(3, trangThaiPhieu);

            // 4. SoNguoi
            stmt.setInt(4, pdb.getSoNguoi());

            // 5. GhiChu
            stmt.setString(5, pdb.getGhiChu());

            // 6. Ma Khach Hang (Xử lý vãng lai)
            String maKH = (pdb.getKhachHang() != null && !pdb.getKhachHang().getMaKhachHang().equals("000"))
                    ? pdb.getKhachHang().getMaKhachHang() : null;
            if (maKH != null) {
                stmt.setString(6, maKH);
            } else {
                stmt.setNull(6, Types.NVARCHAR);
            }

            // 7. Ma Ban
            stmt.setString(7, pdb.getBan().getMaBan());

            // 8. Ma Nhan Vien
            stmt.setString(8, pdb.getNhanVien().getMaNhanVien());

            // 9. Ma Hoa Don
            stmt.setString(9, pdb.getHoaDon().getMaHoaDon());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi thêm Phiếu Đặt Bàn: " + e.getMessage());
            e.printStackTrace(); // In chi tiết lỗi để kiểm tra ràng buộc khác
            return false;
        }
    }

    public String getMaPhieuCuoiCung(LocalDate ngayDat) {
        String maCuoi = null;

        // Định dạng ngày để sử dụng trong câu lệnh SQL LIKE
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy");
        String ngayFormat = ngayDat.format(dtf);

        // SQL: Lọc các mã bắt đầu bằng "PDB-NGAYDAT-" và sắp xếp
        String sql = "SELECT TOP 1 maPhieu FROM PhieuDatBan WHERE maPhieu LIKE 'PDB-" + ngayFormat + "-%' ORDER BY maPhieu DESC";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                maCuoi = rs.getString("maPhieu");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy mã phiếu cuối theo ngày: " + e.getMessage());
        }
        return maCuoi;
    }


    public String taoMaPhieuMoi(LocalDate ngayDat) {
        // 1. Định dạng ngày
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy");
        String ngayFormat = ngayDat.format(dtf); // Ví dụ: "23112025"

        // 2. Định dạng tiền tố mã
        String tienToMoi = "PDB-" + ngayFormat + "-"; // Ví dụ: "PDB-23112025-"

        // 3. Lấy mã cuối cùng cho ngày ĐẶT BÀN CỤ THỂ
        String maCuoi = getMaPhieuCuoiCung(ngayDat);

        if (maCuoi == null) {
            // Trường hợp 1: Chưa có phiếu nào trong ngày này
            return tienToMoi + "001";
        }

        // Trường hợp 2: Có phiếu trong ngày này -> Tăng số thứ tự
        try {
            // Lấy phần số thứ tự (Ví dụ: từ PDB-23112025-005 lấy ra 005)
            // Bắt đầu từ index 13 (sau "PDB-ddMMyyyy-")
            String phanSo = maCuoi.substring(13);
            int soMoi = Integer.parseInt(phanSo) + 1;


            return tienToMoi + String.format("%03d", soMoi);
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            // Xử lý lỗi nếu mã cuối cùng bị sai định dạng số (nên trả về mã đầu tiên)
            System.err.println("Lỗi định dạng mã phiếu cuối cùng: " + maCuoi);
            return tienToMoi + "001";
        }
    }
    public boolean kiemTraBanDaDatTrongNgay(String maBan, LocalDateTime thoiGianBatDau) {
        // Giả định: Bàn được coi là bị trùng nếu có PhieuDatBan trùng ngày và status là 'Đã đặt' hoặc 'Đang dùng'

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String thoiGianSQL = thoiGianBatDau.format(dtf);

        // Sửa SQL để kiểm tra: chỉ cần tìm một bản ghi có trùng mã bàn VÀ trùng ngày/giờ.
        // Việc so sánh giờ cần phải linh hoạt (ví dụ: đặt lúc 18h thì không bị trùng với đặt lúc 20h)
        // Tạm thời, ta chỉ so sánh theo mã bàn và ngày (để đơn giản)

        // Nếu bạn muốn kiểm tra theo chính xác ngày và giờ:
        // Tuy nhiên, việc so sánh giờ phức tạp, ta chỉ so sánh theo ngày

        String sql = "SELECT maPhieu FROM PhieuDatBan " +
                "WHERE maBan = ? AND CAST(thoiGianBatDau AS DATE) = CAST(? AS DATE) " +
                "AND (trangThai = N'Đã đặt' OR trangThai = N'Đang dùng')";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maBan);
            stmt.setString(2, thoiGianSQL);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Trả về true nếu tìm thấy ít nhất một phiếu
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi kiểm tra trùng bàn: " + e.getMessage());
            return true; // Giả định có lỗi CSDL là trùng để đảm bảo an toàn
        }
    }
    public PhieuDatBan getPhieuDatBanByMaBanVaNgay(String maBan, LocalDate ngayDat) {
        PhieuDatBan pdb = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy");
        String ngayFormat = ngayDat.format(dtf);

        String sql = "SELECT p.*, kh.tenKhachHang, kh.soDienThoai FROM PhieuDatBan p " +
                "JOIN KhachHang kh ON p.maKhachHang = kh.maKhachHang " +
                "WHERE p.maBan = ? AND p.maPhieu LIKE 'PDB-" + ngayFormat + "-%' " +
                "AND (p.trangThai = N'Đã đặt' OR p.trangThai = N'Đang dùng')";


        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maBan);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    pdb = new PhieuDatBan();
                    pdb.setGhiChu(rs.getString("ghiChu"));

                    KhachHang kh = new KhachHang();
                    kh.setTenKhachHang(rs.getString("tenKhachHang"));
                    kh.setSoDienThoai(rs.getString("soDienThoai"));
                    pdb.setKhachHang(kh);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi tra cứu phiếu đặt bàn: " + e.getMessage());
        }
        return pdb;
    }
    public boolean huyPhieuDatBanByMaBanVaNgay(String maBan, LocalDate ngay) {
        // Lưu ý: Cần đảm bảo chuỗi trạng thái trong CSDL là N'Đã hủy'
        String trangThaiHuy = "Đã hủy";

        // SQL tìm PDB cho bàn, ngày, và trạng thái hiện tại là 'Đã đặt'/'Đang dùng'
        String sql = "UPDATE PhieuDatBan SET trangThai = ? " +
                "WHERE maBan = ? AND CAST(thoiGianBatDau AS DATE) = CAST(? AS DATE) " +
                "AND trangThai IN (N'Đã đặt', N'Đang dùng')";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, trangThaiHuy);
            ps.setString(2, maBan);
            ps.setDate(3, java.sql.Date.valueOf(ngay));

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi hủy PDB đơn: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean huyTatCaPhieuByMaHoaDon(String maHoaDon) {
        // Lưu ý: Cần đảm bảo chuỗi trạng thái trong CSDL là N'Đã hủy'
        String trangThaiHuy = "Đã hủy";

        // SQL tìm TẤT CẢ PDB có cùng maHoaDon (và trạng thái hiện tại là 'Đã đặt'/'Đang dùng')
        String sql = "UPDATE PhieuDatBan SET trangThai = ? " +
                "WHERE maHoaDon = ? AND trangThai IN (N'Đã đặt', N'Đang dùng')";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, trangThaiHuy);
            ps.setString(2, maHoaDon);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi hủy PDB ghép: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public boolean capNhatMaBanSuDung(String maBanCu, String maBanMoi, LocalDate ngayDat) {
        // Lưu ý: Cần đảm bảo chuỗi trạng thái trong CSDL là N'Đang dùng'
        String trangThaiDangDung = "Đang dùng";

        // SQL tìm PDB của bàn cũ, trùng ngày, và trạng thái hiện tại là 'Đang dùng'
        // Sau đó UPDATE maBan sang bàn mới
        String sql = "UPDATE PhieuDatBan SET maBan = ? " +
                "WHERE maBan = ? AND CAST(thoiGianBatDau AS DATE) = CAST(? AS DATE) " +
                "AND trangThai = N'" + trangThaiDangDung + "'";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maBanMoi);
            ps.setString(2, maBanCu);
            ps.setDate(3, java.sql.Date.valueOf(ngayDat));

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi đổi bàn (Đang dùng): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public String getMaPhieuDatBanByMaBanVaNgay(String maBan, LocalDate ngayDat) {
        String maPhieuKetQua = null;

        // SQL: Tìm maPhieu có maBan trùng, trùng ngày, và trạng thái đang hoạt động
        String sql = "SELECT TOP 1 maPhieu FROM PhieuDatBan " +
                "WHERE maBan = ? AND CAST(thoiGianBatDau AS DATE) = CAST(? AS DATE) " +
                "AND trangThai IN (N'Đã đặt', N'Đang dùng')";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maBan);
            stmt.setDate(2, java.sql.Date.valueOf(ngayDat));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    maPhieuKetQua = rs.getString("maPhieu");
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi tra cứu MaPhieu theo bàn và ngày: " + e.getMessage());
        }
        return maPhieuKetQua;
    }
    public boolean doiBanDat(String maPhieu, String maBanMoi) {
        // Lưu ý: Cần đảm bảo chuỗi trạng thái trong CSDL là N'Đã đặt'
        String trangThaiDaDat = "Đã đặt";

        String sql = "UPDATE PhieuDatBan SET maBan = ? WHERE maPhieu = ? AND trangThai = N'" + trangThaiDaDat + "'";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maBanMoi);
            ps.setString(2, maPhieu);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi đổi bàn (Đã đặt): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public boolean capNhatTrangThaiBan(String maBan, TrangThai trangThaiMoi) {
        String sql = "UPDATE BanAn SET trangThai = ? WHERE maBan = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, trangThaiMoi.getThongTin());
            ps.setString(2, maBan);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi cập nhật trạng thái bàn (từ PDB_DAO): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public boolean doiBanTongHop(BanAn banCu, BanAn banMoi, LocalDate ngayDat, TrangThai trangThaiCu) {
        boolean doiPhieuThanhCong = false;

        // --- 1. Cập nhật mã bàn trong PhieuDatBan ---
        if (trangThaiCu == TrangThai.DA_DAT) {
            String maPhieu = getMaPhieuDatBanByMaBanVaNgay(banCu.getMaBan(), ngayDat);
            if (maPhieu != null) {
                doiPhieuThanhCong = doiBanDat(maPhieu, banMoi.getMaBan());
            } else {
                System.err.println("Lỗi nghiệp vụ: Không tìm thấy Phiếu Đặt Bàn đang 'Đã đặt' cho bàn " + banCu.getMaBan());
                return false;
            }
        } else if (trangThaiCu == TrangThai.DANG_SU_DUNG) {
            doiPhieuThanhCong = capNhatMaBanSuDung(banCu.getMaBan(), banMoi.getMaBan(), ngayDat);
            if (!doiPhieuThanhCong) {
                System.err.println("Lỗi nghiệp vụ: Cập nhật maBan trong PDB 'Đang dùng' cho bàn " + banCu.getMaBan() + " thất bại.");
                return false;
            }
        } else {
            return false;
        }

        // --- 2. Cập nhật trạng thái bàn (Sử dụng phương thức vừa thêm vào PDB_DAO) ---
        if (doiPhieuThanhCong) {
            try {
                // Bàn mới nhận trạng thái của bàn cũ
                // Gọi phương thức mới capNhatTrangThaiBan trong PhieuDatBan_DAO
                boolean newBanSuccess = capNhatTrangThaiBan(banMoi.getMaBan(), trangThaiCu);

                // Bàn cũ về TRỐNG
                boolean oldBanSuccess = capNhatTrangThaiBan(banCu.getMaBan(), TrangThai.TRONG);

                // Trả về kết quả tổng hợp
                return newBanSuccess && oldBanSuccess;
            } catch (Exception e) {
                System.err.println("Lỗi ngoại lệ khi cập nhật trạng thái bàn trong PDB_DAO: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        }

        return false;
    }

    public static List<PhieuDatBan> getByMaHoaDon(String maHoaDon) {
        List<PhieuDatBan> list = new ArrayList<>();
        String sql = "SELECT * FROM PhieuDatBan WHERE maHoaDon = ?";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maHoaDon);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                PhieuDatBan phieu = new PhieuDatBan();
                phieu.setMaPhieu(rs.getString("maPhieu"));
                phieu.setThoiGianBatDau(rs.getTimestamp("thoiGianBatDau").toLocalDateTime());
                phieu.setTrangThai(rs.getString("trangThai"));
                phieu.setSoNguoi(rs.getInt("soNguoi"));
                phieu.setGhiChu(rs.getNString("ghiChu"));

                HoaDon hd = new HoaDon();
                hd.setMaHoaDon(maHoaDon);

                KhachHang kh = new KhachHang();
                kh.setMaKhachHang(rs.getString("maKhachHang"));

                NhanVien nv = new NhanVien();
                nv.setMaNhanVien(rs.getString("maNhanVien"));

                BanAn ban = new BanAn();
                ban.setMaBan(rs.getString("maBan"));

                phieu.setKhachHang(kh);
                phieu.setBan(ban);
                phieu.setNhanVien(nv);
                phieu.setHoaDon(hd);


                list.add(phieu);
            }

        } catch (SQLException e) {
            System.err.println("❌ Lỗi lấy phiếu đặt bàn theo mã hóa đơn: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    public static PhieuDatBan timMotPhieuBangMaHD(String maHoaDon) {
        if (maHoaDon == null || maHoaDon.trim().isEmpty()) {
            return null;
        }

        String sql = "SELECT * FROM PhieuDatBan WHERE maHoaDon = ?";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maHoaDon.trim());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                PhieuDatBan phieu = new PhieuDatBan();
                phieu.setMaPhieu(rs.getString("maPhieu"));
                phieu.setThoiGianBatDau(rs.getTimestamp("thoiGianBatDau").toLocalDateTime());
                phieu.setTrangThai(rs.getString("trangThai"));
                phieu.setSoNguoi(rs.getInt("soNguoi"));
                phieu.setGhiChu(rs.getNString("ghiChu"));

                HoaDon hd = new HoaDon();
                hd.setMaHoaDon(maHoaDon);

                KhachHang kh = new KhachHang();
                kh.setMaKhachHang(rs.getString("maKhachHang"));

                NhanVien nv = new NhanVien();
                nv.setMaNhanVien(rs.getString("maNhanVien"));

                BanAn ban = new BanAn();
                ban.setMaBan(rs.getString("maBan"));

                phieu.setKhachHang(kh);
                phieu.setBan(ban);
                phieu.setNhanVien(nv);
                phieu.setHoaDon(hd);


                return phieu;
            }

        } catch (SQLException e) {
            System.err.println("❌ Lỗi lấy phiếu đặt bàn theo mã hóa đơn: " + e.getMessage());
            e.printStackTrace();
        }

        return null;  // Không tìm thấy
    }
// Thay thế toàn bộ hàm chuyenBanNhieuSangNhieu cũ bằng hàm này:

    public boolean chuyenBanNhieuSangNhieu(List<String> dsMaBanCu, List<String> dsMaBanMoi, String maHoaDon,String trangThaiMoi) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = ConnectDB.getConnection();
            con.setAutoCommit(false); // --- BẮT ĐẦU TRANSACTION ---

            // 1. LẤY THÔNG TIN CƠ BẢN TỪ 1 PHIẾU CŨ
            String sqlGetInfo = "SELECT TOP 1 maKhachHang, maNhanVien, ghiChu, soNguoi FROM PhieuDatBan WHERE maHoaDon = ?";
            pstmt = con.prepareStatement(sqlGetInfo);
            pstmt.setString(1, maHoaDon);
            rs = pstmt.executeQuery();

            String maKhachHang = null;
            String maNhanVien = null;
            String ghiChu = "";
            int soNguoi = 0;

            if (rs.next()) {
                maKhachHang = rs.getString("maKhachHang");
                maNhanVien = rs.getString("maNhanVien");
                ghiChu = rs.getString("ghiChu");
                soNguoi = rs.getInt("soNguoi");
            }
            rs.close();
            pstmt.close();

            // 2. CẬP NHẬT TRẠNG THÁI BÀN CŨ -> 'TRỐNG'
            String sqlUpdateOldBan = "UPDATE BanAn SET trangThai = N'Trống' WHERE maBan = ?";
            pstmt = con.prepareStatement(sqlUpdateOldBan);
            for (String maCu : dsMaBanCu) {
                pstmt.setString(1, maCu);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            pstmt.close();

            // 3. CẬP NHẬT TRẠNG THÁI BÀN MỚI -> 'ĐANG SỬ DỤNG'
            String trangThaiBanAn = trangThaiMoi.equals("Đang dùng") ? "Đang sử dụng" : trangThaiMoi;
            String sqlUpdateNewBan = "UPDATE BanAn SET trangThai = ? WHERE maBan = ?";
            pstmt = con.prepareStatement(sqlUpdateNewBan);
            for (String maMoi : dsMaBanMoi) {
                pstmt.setString(1, trangThaiBanAn); // <--- Dùng biến, không dùng cứng N'Đang ...'
                pstmt.setString(2, maMoi);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            pstmt.close();

            // 4. XÓA CÁC PHIẾU ĐẶT BÀN CŨ
            String sqlDeleteOldPDB = "DELETE FROM PhieuDatBan WHERE maHoaDon = ? AND maBan = ?";
            pstmt = con.prepareStatement(sqlDeleteOldPDB);
            for (String maCu : dsMaBanCu) {
                pstmt.setString(1, maHoaDon);
                pstmt.setString(2, maCu);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            pstmt.close();

            // 5. TẠO PHIẾU ĐẶT BÀN MỚI

            // --- KHẮC PHỤC LỖI TREO: Lấy mã phiếu cuối cùng TRONG CÙNG KẾT NỐI (con) ---
            LocalDate today = LocalDate.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy");
            String ngayFormat = today.format(dtf);
            String prefix = "PDB-" + ngayFormat + "-";

            // Query trực tiếp bằng 'con' để tránh Deadlock
            String sqlGetMaxID = "SELECT TOP 1 maPhieu FROM PhieuDatBan WHERE maPhieu LIKE '" + prefix + "%' ORDER BY maPhieu DESC";
            Statement stMax = con.createStatement();
            ResultSet rsMax = stMax.executeQuery(sqlGetMaxID);

            int currentSuffix = 0;
            if (rsMax.next()) {
                String maCuoi = rsMax.getString("maPhieu");
                try {
                    currentSuffix = Integer.parseInt(maCuoi.substring(13));
                } catch (Exception e) { currentSuffix = 0; }
            }
            rsMax.close();
            stMax.close();
            // --------------------------------------------------------------------------

            String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String sqlInsertNewPDB = "INSERT INTO PhieuDatBan (maPhieu, thoiGianBatDau, trangThai, soNguoi, ghiChu, maKhachHang, maBan, maNhanVien, maHoaDon) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = con.prepareStatement(sqlInsertNewPDB);

            for (String maMoi : dsMaBanMoi) {
                currentSuffix++;
                String maPhieuMoi = prefix + String.format("%03d", currentSuffix);

                pstmt.setString(1, maPhieuMoi);
                pstmt.setString(2, nowStr);
                pstmt.setString(3, trangThaiMoi);
                pstmt.setInt(4, soNguoi);
                pstmt.setString(5, ghiChu);
                pstmt.setString(6, maKhachHang);
                pstmt.setString(7, maMoi);
                pstmt.setString(8, maNhanVien);
                pstmt.setString(9, maHoaDon);

                pstmt.addBatch();
            }
            pstmt.executeBatch();

            con.commit(); // XÁC NHẬN GIAO DỊCH
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }
}