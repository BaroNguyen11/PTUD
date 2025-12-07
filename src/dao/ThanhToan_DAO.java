package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ConnectDB.ConnectDB;
import entity.BanAn;
import entity.HoaDon;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.LoaiBan;
import entity.NhanVien;
import entity.PhieuDatBan;
import entity.TrangThai;
import entity.ViTri;

public class ThanhToan_DAO {


    public List<BanAn> getDanhSachBanDangSuDungTheoNgay(LocalDate ngay) {
        List<BanAn> dsBan = new ArrayList<>();

        String sql = """
	        SELECT b.maBan, b.loai, b.trangThai, b.viTri
	        FROM BanAn b
	        JOIN PhieuDatBan p ON b.maBan = p.maBan
	        JOIN HoaDon h ON h.maHoaDon = p.maHoaDon
	        WHERE b.trangThai = N'Đang dùng'
	          AND h.trangThai = N'Chưa thanh toán'
	          AND CAST(p.thoiGianBatDau AS DATE) = ?
	    """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(ngay));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String maBan = rs.getString("maBan");
                String loaiStr = rs.getString("loai");
                String trangThaiStr = rs.getString("trangThai");
                String viTriStr = rs.getString("viTri");
                LoaiBan loai = mapToLoaiBan(loaiStr);
                TrangThai trangThai = mapToTrangThai(trangThaiStr);
                ViTri viTri = mapToViTri(viTriStr);

                BanAn ban = new BanAn(maBan, loai, trangThai, viTri);
                dsBan.add(ban);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsBan;
    }

    private LoaiBan mapToLoaiBan(String loaiStr) {
        if (loaiStr == null) return null;
        loaiStr = loaiStr.trim().toUpperCase();  // Normalize
        return switch (loaiStr) {
            case "THƯỜNG" -> LoaiBan.THUONG;
            case "VIP" -> LoaiBan.VIP;
            default -> throw new IllegalArgumentException("Loại bàn không hợp lệ: " + loaiStr);
        };
    }

    private TrangThai mapToTrangThai(String trangThaiStr) {
        if (trangThaiStr == null) return null;
        trangThaiStr = trangThaiStr.trim().toUpperCase();
        return switch (trangThaiStr) {
            case "ĐANG DÙNG" -> TrangThai.DANG_SU_DUNG;
            case "TRỐNG" -> TrangThai.TRONG;
            case "ĐANG ĐẶT" -> TrangThai.DA_DAT;
            // Thêm case khác nếu cần
            default -> throw new IllegalArgumentException("Trạng thái không hợp lệ: " + trangThaiStr);
        };
    }

    private ViTri mapToViTri(String viTriStr) {
        if (viTriStr == null) return null;
        viTriStr = viTriStr.trim().toUpperCase();
        return switch (viTriStr) {
            case "TẦNG 1" -> ViTri.LAU_1;
            case "TẦNG 2" -> ViTri.LAU_2;
            // Thêm case khác nếu cần
            default -> throw new IllegalArgumentException("Vị trí không hợp lệ: " + viTriStr);
        };
    }

    public HoaDon getHoaDonTheoMaBan(String maBan) {
        if (maBan == null || maBan.trim().isEmpty()) {
            return null;  // Trả về null nếu maBan không hợp lệ
        }

        String sql = """
	         SELECT h.maHoaDon, h.ngayTao, h.trangThai, h.phuongThuc, h.ghiChu,
	               h.maNhanVien, h.maKhachHang
	        FROM HoaDon h
	        JOIN PhieuDatBan p ON h.maHoaDon = p.maHoaDon
	        WHERE p.maBan = ?
	         AND h.trangThai = N'Chưa thanh toán'  
	    """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maBan.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String maHoaDon = rs.getString("maHoaDon");
                Timestamp ngayTaoSql = rs.getTimestamp("ngayTao");
                LocalDateTime ngayTao = (ngayTaoSql != null) ? ngayTaoSql.toLocalDateTime() : null;
                String trangThaiStr = rs.getString("trangThai");
                String phuongThuc = rs.getString("phuongThuc");
                String ghiChu = rs.getString("ghiChu");
                NhanVien nhanVien = getNhanVienByMa(rs.getString("maNhanVien"));
                KhachHang khachHang = getKhachHangByMa(rs.getString("maKhachHang"));




                // Tạo entity HoaDon (giả sử constructor phù hợp)
                return new HoaDon(maHoaDon, ngayTao, trangThaiStr, phuongThuc, ghiChu, nhanVien, khachHang);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;  // Không tìm thấy HD
    }
    public NhanVien getNhanVienByMa(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.trim().isEmpty()) {
            return null;  // Trả về null nếu mã không hợp lệ
        }

        String sql = """
	        SELECT maNhanVien, tenNhanVien, chucVu, CCCD, soDienThoai, 
	               ngaySinh, ngayVaoLam, ngayThoiViec
	        FROM NhanVien
	        WHERE maNhanVien = ?
	    """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maNhanVien.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String ma = rs.getString("maNhanVien");
                String ten = rs.getString("tenNhanVien");
                String chucVu = rs.getString("chucVu");
                String cccd = rs.getString("CCCD");
                String soDienThoai = rs.getString("soDienThoai");
                java.sql.Date ngaySinhSql = rs.getDate("ngaySinh");
                java.sql.Date ngayVaoLamSql = rs.getDate("ngayVaoLam");
                java.sql.Date ngayThoiViecSql = rs.getDate("ngayThoiViec");

                LocalDate ngaySinh = (ngaySinhSql != null) ? ngaySinhSql.toLocalDate() : null;
                LocalDate ngayVaoLam = (ngayVaoLamSql != null) ? ngayVaoLamSql.toLocalDate() : null;
                LocalDate ngayThoiViec = (ngayThoiViecSql != null) ? ngayThoiViecSql.toLocalDate() : null;

                // Giả sử entity NhanVien có constructor phù hợp
                return new NhanVien(ma, ten, chucVu, cccd, soDienThoai, ngaySinh, ngayVaoLam, ngayThoiViec);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;  // Không tìm thấy
    }

    public KhachHang getKhachHangByMa(String maKhachHang) {
        if (maKhachHang == null || maKhachHang.trim().isEmpty()) {
            return null;  // Trả về null nếu mã không hợp lệ
        }

        String sql = """
	        SELECT maKhachHang, tenKhachHang, soDienThoai, diemTichLuy
	        FROM KhachHang
	        WHERE maKhachHang = ?
	    """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maKhachHang.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String ma = rs.getString("maKhachHang");
                String ten = rs.getString("tenKhachHang");
                String soDienThoai = rs.getString("soDienThoai");
                double diemTichLuy = rs.getBigDecimal("diemTichLuy").doubleValue();

                // Giả sử entity KhachHang có constructor phù hợp
                return new KhachHang(ma, ten, soDienThoai, diemTichLuy );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;  // Không tìm thấy
    }

    public List<String> getChiTietHoaDonTheoMa(String maHoaDon) {
        List<String> dsChiTiet = new ArrayList<>();

        String sql = """
            SELECT 
                ma.tenMonAn,
                cthd.soLuong,
                CASE 
                    WHEN km.maKhuyenMai IS NOT NULL 
                         AND hd.ngayTao BETWEEN km.ngayBatDau AND km.ngayKetThuc 
                    THEN ctkmMonAn.giaSauKhuyenMai
                    ELSE ma.giaTien
                END AS giaApDung,
                cthd.soLuong *
                CASE 
                    WHEN km.maKhuyenMai IS NOT NULL 
                         AND hd.ngayTao BETWEEN km.ngayBatDau AND km.ngayKetThuc 
                    THEN ctkmMonAn.giaSauKhuyenMai
                    ELSE ma.giaTien
                END AS tongTien
            FROM HoaDon hd
            JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon
            JOIN MonAn ma ON cthd.maMonAn = ma.maMonAn
            LEFT JOIN ChiTietKMMOnAn ctkmMonAn ON ma.maMonAn = ctkmMonAn.maMonAn
            LEFT JOIN KhuyenMai km ON ctkmMonAn.maKhuyenMai = km.maKhuyenMai
            WHERE hd.maHoaDon = ?
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maHoaDon);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String tenMon = rs.getString("tenMonAn");
                int soLuong = rs.getInt("soLuong");
                double giaApDung = rs.getDouble("giaApDung");
                double tongTien = rs.getDouble("tongTien");

                String dong = String.format("%s,%d,%.0f,%.0f",tenMon, soLuong, giaApDung, tongTien);
                dsChiTiet.add(dong);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsChiTiet;
    }

    public PhieuDatBan timPhieuDatTheoMaBan(String maBan) {
        if (maBan == null || maBan.trim().isEmpty()) {
            return null;
        }

        String sql = "SELECT * FROM PhieuDatBan WHERE maBan = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maBan.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Lấy thông tin các khóa ngoại
                String maKhachHang = rs.getString("maKhachHang");
                String maNhanVien = rs.getString("maNhanVien");
                String maHoaDon = rs.getString("maHoaDon");

                // Dùng lại các hàm bạn đã viết để lấy entity đầy đủ
                KhachHang kh = getKhachHangByMa(maKhachHang);
                NhanVien nv = getNhanVienByMa(maNhanVien);
                HoaDon hd = getHoaDonTheoMaBan(maBan); // hoặc tạo getHoaDonByMa nếu muốn chính xác hơn

                BanAn ban = new BanAn();
                ban.setMaBan(maBan);

                // Tạo đối tượng phiếu đặt
                return new PhieuDatBan(
                        rs.getString("maPhieu"),
                        rs.getTimestamp("thoiGianBatDau").toLocalDateTime(),
                        rs.getString("trangThai"),
                        rs.getInt("soNguoi"),
                        rs.getString("ghiChu"),
                        kh, ban, nv, hd
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Không tìm thấy
    }

    public double getSoTienGiamCaoNhatTheoHoaDon(String maHoaDon) {
        String sql = "SELECT MAX(soTienGiam) AS soTienGiamCaoNhat FROM ChiTietKMHD WHERE maHoaDon = ?";
        double soTienGiam = 0.0;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maHoaDon);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                soTienGiam = rs.getDouble("soTienGiamCaoNhat");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return soTienGiam;
    }

    public boolean capNhatTrangThaiThanhToan(String maHoaDon, String phuongThuc) throws SQLException {
        Connection con = ConnectDB.getConnection();

        // Cập nhật 2 cột: trangThai và phuongThuc
        String sql = "UPDATE HoaDon SET trangThai = N'Đã thanh toán', phuongThuc = ? WHERE maHoaDon = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, phuongThuc);
            ps.setString(2, maHoaDon);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhatTrangThaiHoanTat(String maPhieu) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "UPDATE PhieuDatBan SET trangThai = N'Đã dùng' WHERE maPhieu = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maPhieu);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhatTrangThaiTrong(String maBan) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "UPDATE BanAn SET trangThai = N'Trống' WHERE maBan = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maBan);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public List<KhuyenMai> getKhuyenMaiApDungChoHoaDon(String maHoaDon, double tongTien) {
        List<KhuyenMai> dsKM = new ArrayList<>();

        String sql = """
            SELECT
                k.maKhuyenMai, k.tenKhuyenMai, k.ngayBatDau, k.ngayKetThuc, k.dieuKienApDung,
                k.giaTriToiDa, k.giamGiaPhanTram, k.giaTriGiam,
                CASE
                    WHEN k.giamGiaPhanTram = 1 THEN
                        CASE
                            WHEN ? >= k.dieuKienApDung THEN
                                LEAST( (? * k.giaTriGiam / 100), COALESCE(k.giaTriToiDa, 999999999))
                            ELSE 0
                        END
                    ELSE
                        k.giaTriGiam
                END AS soTienGiamThucTe
            FROM KhuyenMai k
            LEFT JOIN ChiTietKMMonAn ctm ON k.maKhuyenMai = ctm.maKhuyenMai
            WHERE k.ngayBatDau <= (
                SELECT TOP 1 CAST(p.thoiGianBatDau AS DATE) FROM PhieuDatBan p WHERE p.maHoaDon = ? ORDER BY p.thoiGianBatDau
            )
            AND k.ngayKetThuc >= (
                SELECT TOP 1 CAST(p.thoiGianBatDau AS DATE) FROM PhieuDatBan p WHERE p.maHoaDon = ? ORDER BY p.thoiGianBatDau
            )
            AND ? >= k.dieuKienApDung  -- Kiểm tra tổng tiền đủ điều kiện
            AND ctm.maKhuyenMai IS NULL  -- Loại trừ khuyến mãi món ăn
            ORDER BY soTienGiamThucTe DESC
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setDouble(1, tongTien);  // Cho % giảm
            stmt.setDouble(2, tongTien);  // Cho tổng tiền * %
            stmt.setString(3, maHoaDon.trim());  // Subquery 1
            stmt.setString(4, maHoaDon.trim());  // Subquery 2
            stmt.setDouble(5, tongTien);  // Kiểm tra điều kiện tổng tiền

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                KhuyenMai km = new KhuyenMai(
                        rs.getString("maKhuyenMai"),
                        rs.getString("tenKhuyenMai"),
                        rs.getDate("ngayBatDau").toLocalDate(),
                        rs.getDate("ngayKetThuc").toLocalDate(),
                        rs.getBigDecimal("dieuKienApDung").doubleValue(),
                        rs.getBigDecimal("giaTriToiDa") != null ? rs.getBigDecimal("giaTriToiDa").doubleValue() : 0,
                        rs.getBoolean("giamGiaPhanTram"),
                        rs.getBigDecimal("giaTriGiam").doubleValue()
                );
                

                dsKM.add(km);
            }

        } catch (SQLException e) {
            System.err.println("❌ Lỗi lấy khuyến mãi cho hóa đơn: " + e.getMessage());
            e.printStackTrace();
        }

        return dsKM;
    }

    public boolean taoChiTietKMHD(String maHoaDon, String maKhuyenMai, double soTienGiam) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "INSERT INTO ChiTietKMHD(maHoaDon, maKhuyenMai, soTienGiam) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHoaDon);
            ps.setString(2, maKhuyenMai);
            ps.setDouble(3, soTienGiam);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) con.close();
        }
    }
    
    public HoaDon getByMaHoaDon(String maHoaDon) {
        if (maHoaDon == null || maHoaDon.trim().isEmpty()) {
            return null;
        }

        String sql = "SELECT * FROM HoaDon WHERE maHoaDon = ?";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maHoaDon);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                HoaDon hoaDon = new HoaDon();
                hoaDon.setMaHoaDon(rs.getString("maHoaDon"));
                hoaDon.setNgayTao(rs.getTimestamp("ngayTao").toLocalDateTime());  // Timestamp sang LocalDateTime
                hoaDon.setTrangThai(rs.getString("trangThai"));
                hoaDon.setPhuongThuc(rs.getString("phuongThuc"));
                hoaDon.setGhiChu(rs.getNString("ghiChu"));

                // Set object fields (null ban đầu, lazy load sau nếu cần)
                NhanVien nv = new NhanVien();
                nv.setMaNhanVien(rs.getString("maNhanVien"));
                
                hoaDon.setNhanVien(nv);  // Hoặc load: new NhanVienDAO().getByMaNhanVien(rs.getString("maNhanVien"));
                
                KhachHang kh = new KhachHang();
                kh.setMaKhachHang(rs.getString("maKhachHang"));
                
                hoaDon.setKhachHang(kh);  // Tương tự

                return hoaDon;
            }

        } catch (SQLException e) {
            System.err.println("❌ Lỗi lấy hóa đơn theo mã: " + e.getMessage());
            e.printStackTrace();
        }

        return null;  // Không tìm thấy
    }
    
    public boolean updateDiemTichLuy(String maKhachHang, double diemMoi) {
        if (maKhachHang == null || maKhachHang.trim().isEmpty()) {
            return false; 
        }

        String sql = "UPDATE KhachHang SET diemTichLuy = ? WHERE maKhachHang = ?";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, diemMoi);
            pstmt.setString(2, maKhachHang.trim());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;  // Thành công nếu ảnh hưởng 1 row

        } catch (SQLException e) {
            System.err.println("❌ Lỗi cập nhật điểm tích lũy: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}