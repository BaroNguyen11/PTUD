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

import connectDB.ConnectDB;
import entity.BanAn;
import entity.HoaDon;
import entity.KhachHang;
import entity.LoaiBan;
import entity.NhanVien;
import entity.TrangThai;
import entity.ViTri;

public class Dao_ThanhToan {


	public List<BanAn> getDanhSachBanDangSuDungTheoNgay(LocalDate ngay) {
	    List<BanAn> dsBan = new ArrayList<>();

	    String sql = """
	        SELECT b.maBan, b.loai, b.trangThai, b.viTri
	        FROM BanAn b
	        JOIN PhieuDatBan p ON b.maBan = p.maBan
	        JOIN HoaDon h ON h.maHoaDon = p.maHoaDon
	        WHERE p.trangThai = N'Đang dùng'
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
	          AND p.trangThai = N'Đang dùng'  -- Chỉ lấy phiếu đang dùng (thường chỉ 1 HD đang mở)
	          AND h.trangThai = N'Chưa thanh toán'  -- HD chưa thanh toán
	        ORDER BY h.ngayTao DESC  -- Lấy cái mới nhất nếu có nhiều (nhưng thường chỉ 1)
	        OFFSET 0 ROWS FETCH NEXT 1 ROW ONLY  -- Giới hạn 1 row (SQL Server syntax)
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
	
	

}
