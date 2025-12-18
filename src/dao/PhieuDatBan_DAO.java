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


    public boolean themPhieuDatBan(PhieuDatBan pdb, String trangThaiPhieu) {

        // 1. Tự sinh mã mới
        LocalDate ngayDat = pdb.getHoaDon().getNgayTao().toLocalDate();
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

    // Trong PhieuDatBan_DAO.java

    public String getMaPhieuCuoiCung(LocalDate ngayCanTim) {
        String maPhieu = null;


        // 1. Tạo pattern tìm kiếm để lọc bớt dữ liệu (PDB-ddMMyyyy-%)
        String datePart = ngayCanTim.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        String prefixPattern = "PDB-" + datePart + "-%";

        // 2. Câu SQL JOIN: Lấy mã phiếu từ bảng PDB, nhưng lọc theo ngày tạo của HOADON
        String sql = "SELECT TOP 1 p.maPhieu " +
                "FROM PhieuDatBan p " +
                "JOIN HoaDon h ON p.maHoaDon = h.maHoaDon " +
                "WHERE CAST(h.ngayTao AS DATE) = ? " + // Lọc theo ngày của hóa đơn
                "AND p.maPhieu LIKE ? " +              // Đảm bảo đúng tiền tố ngày
                "ORDER BY p.maPhieu DESC";             // Lấy cái lớn nhất

        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            // Set tham số 1: Ngày (java.sql.Date)
            stmt.setDate(1, java.sql.Date.valueOf(ngayCanTim));
            // Set tham số 2: Pattern (String)
            stmt.setString(2, prefixPattern);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                maPhieu = rs.getString("maPhieu");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maPhieu;
    }


    public String taoMaPhieuMoi(LocalDate ngayDat) {
        // 1. Format ngày: ddMMyyyy
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy");
        String ngayFormat = ngayDat.format(dtf);

        // 2. Tạo tiền tố: PDB-18122025-
        String tienToMoi = "PDB-" + ngayFormat + "-";

        // 3. Gọi hàm DAO vừa viết ở trên (đã JOIN bảng)
        String maCuoi = new PhieuDatBan_DAO().getMaPhieuCuoiCung(ngayDat);

        if (maCuoi == null) {
            return tienToMoi + "001"; // Chưa có phiếu nào trong ngày -> 001
        }

        try {
            // Cắt chuỗi để lấy số đuôi (PDB-ddMMyyyy- có độ dài 13 ký tự)
            String phanSo = maCuoi.substring(13);
            int soMoi = Integer.parseInt(phanSo) + 1;
            return tienToMoi + String.format("%03d", soMoi);
        } catch (Exception e) {
            return tienToMoi + "001";
        }
    }
    public boolean kiemTraBanDaDatTrongNgay(String maBan, LocalDateTime thoiGianBatDau) {
        // Giả định: Bàn được coi là bị trùng nếu có PhieuDatBan trùng ngày và status là 'Đã đặt' hoặc 'Đang dùng'

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String thoiGianSQL = thoiGianBatDau.format(dtf);
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
        Connection con = null;
        PreparedStatement psGetHD = null;
        PreparedStatement psUpdateHD = null;
        PreparedStatement psUpdatePDB = null;

        String trangThaiHuy = "Đã hủy"; // Đảm bảo trong DB cũng là N'Đã hủy'

        try {
            con = ConnectDB.getConnection();

            // 1. TẮT TỰ ĐỘNG LƯU (Bắt đầu Transaction)
            con.setAutoCommit(false);

            // ---------------------------------------------------------
            // BƯỚC 1: Lấy mã Hóa Đơn đang dính với cái bàn này trước
            // ---------------------------------------------------------
            String sqlGetHD = "SELECT maHoaDon FROM PhieuDatBan " +
                    "WHERE maBan = ? " +
                    "AND CAST(thoiGianBatDau AS DATE) = CAST(? AS DATE) " +
                    "AND trangThai IN (N'Đã đặt', N'Đang dùng')";

            psGetHD = con.prepareStatement(sqlGetHD);
            psGetHD.setString(1, maBan);
            psGetHD.setDate(2, java.sql.Date.valueOf(ngay));

            ResultSet rs = psGetHD.executeQuery();
            String maHoaDon = null;
            if (rs.next()) {
                maHoaDon = rs.getString("maHoaDon");
            }

            // ---------------------------------------------------------
            // BƯỚC 2: Cập nhật Hóa Đơn (Nếu tìm thấy)
            // ---------------------------------------------------------
            if (maHoaDon != null) {
                String sqlUpdateHD = "UPDATE HoaDon SET trangThai = ? WHERE maHoaDon = ?";
                psUpdateHD = con.prepareStatement(sqlUpdateHD);
                psUpdateHD.setString(1, trangThaiHuy); // Set thành 'Đã hủy'
                psUpdateHD.setString(2, maHoaDon);

                // Chạy lệnh update Hóa đơn
                psUpdateHD.executeUpdate();
            }

            // ---------------------------------------------------------
            // BƯỚC 3: Cập nhật Phiếu Đặt Bàn (Code cũ của bạn)
            // ---------------------------------------------------------
            String sqlUpdatePDB = "UPDATE PhieuDatBan SET trangThai = ? " +
                    "WHERE maBan = ? " +
                    "AND CAST(thoiGianBatDau AS DATE) = CAST(? AS DATE) " +
                    "AND trangThai IN (N'Đã đặt', N'Đang dùng')";

            psUpdatePDB = con.prepareStatement(sqlUpdatePDB);
            psUpdatePDB.setString(1, trangThaiHuy);
            psUpdatePDB.setString(2, maBan);
            psUpdatePDB.setDate(3, java.sql.Date.valueOf(ngay));

            int rowsPDB = psUpdatePDB.executeUpdate();

            // ---------------------------------------------------------
            // KẾT THÚC: Kiểm tra và Chốt sổ
            // ---------------------------------------------------------
            if (rowsPDB > 0) {
                con.commit(); // Thành công hết thì mới LƯU
                return true;
            } else {
                con.rollback(); // Không tìm thấy phiếu để hủy thì hoàn tác
                return false;
            }

        } catch (SQLException e) {
            // Có lỗi xảy ra ở bất kỳ bước nào -> HOÀN TÁC TOÀN BỘ
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            // Đóng kết nối thủ công vì không dùng try-with-resources cho transaction phức tạp
            try {
                if (psGetHD != null) psGetHD.close();
                if (psUpdateHD != null) psUpdateHD.close();
                if (psUpdatePDB != null) psUpdatePDB.close();
                // Trả lại trạng thái auto commit mặc định cho connection pool (nếu dùng)
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
    public PhieuDatBan getPhieuDatBanMoiNhat(String maBan) {
        PhieuDatBan phieu = null;

        // Câu lệnh SQL: Lấy phiếu mới nhất có trạng thái chưa kết thúc
        // Kết nối bảng để lấy luôn thông tin Khách Hàng và Hóa Đơn
        String sql = "SELECT TOP 1 p.maPhieu, p.thoiGianBatDau, p.ghiChu, p.soNguoi, p.trangThai, " +
                "k.maKhachHang, k.tenKhachHang, k.soDienThoai, " +
                "nv.maNhanVien, nv.tenNhanVien, " +
                "hd.maHoaDon " +
                "FROM PhieuDatBan p " +
                "JOIN KhachHang k ON p.maKhachHang = k.maKhachHang " +
                "JOIN NhanVien nv ON p.maNhanVien = nv.maNhanVien " +
                "LEFT JOIN HoaDon hd ON p.maHoaDon = hd.maHoaDon " + // Dùng LEFT JOIN phòng trường hợp chưa có HĐ (dù hiếm)
                "WHERE p.maBan = ? " +
                "AND p.trangThai IN (N'Đang dùng', N'Đã đặt') " + // Chỉ lấy phiếu đang active
                "ORDER BY p.thoiGianBatDau DESC";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maBan);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    phieu = new PhieuDatBan();
                    phieu.setMaPhieu(rs.getString("maPhieu"));
                    phieu.setThoiGianBatDau(rs.getTimestamp("thoiGianBatDau").toLocalDateTime());
                    phieu.setGhiChu(rs.getString("ghiChu"));
                    phieu.setSoNguoi(rs.getInt("soNguoi"));
                    phieu.setTrangThai(rs.getString("trangThai"));

                    // Map thông tin Bàn
                    BanAn ban = new BanAn();
                    ban.setMaBan(maBan);
                    phieu.setBan(ban);

                    // Map thông tin Khách Hàng
                    KhachHang kh = new KhachHang();
                    kh.setMaKhachHang(rs.getString("maKhachHang"));
                    kh.setTenKhachHang(rs.getString("tenKhachHang"));
                    kh.setSoDienThoai(rs.getString("soDienThoai"));
                    phieu.setKhachHang(kh);

                    // Map thông tin Nhân Viên
                    NhanVien nv = new NhanVien();
                    nv.setMaNhanVien(rs.getString("maNhanVien"));
                    nv.setTenNhanVien(rs.getString("tenNhanVien"));
                    phieu.setNhanVien(nv);

                    // Map thông tin Hóa Đơn
                    String maHD = rs.getString("maHoaDon");
                    if (maHD != null) {
                        HoaDon hd = new HoaDon();
                        hd.setMaHoaDon(maHD);
                        phieu.setHoaDon(hd);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return phieu;
    }
}