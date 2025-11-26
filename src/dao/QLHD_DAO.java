package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ConnectDB.ConnectDB;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.MonAn;
import entity.NhanVien;

public class QLHD_DAO {
    // Lấy toàn bộ danh sách hóa đơn
    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                HoaDon hd = mapResultSetToHoaDon(rs);
                list.add(hd);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Thêm hóa đơn mới
    public boolean insertHoaDon(HoaDon hd, String maNhanVien, String maKhachHang) {
        String sql = "INSERT INTO HoaDon(maHoaDon, ngayTao, trangThai, phuongThuc, ghiChu, maNhanVien, maKhachHang) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hd.getMaHoaDon());
            ps.setTimestamp(2, Timestamp.valueOf(hd.getNgayTao()));
            ps.setString(3, hd.getTrangThai());
            ps.setString(4, hd.getPhuongThuc());
            ps.setString(5, hd.getGhiChu());
            ps.setString(6, maNhanVien);
            ps.setString(7, maKhachHang);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    // Tìm hóa đơn theo mã
    public HoaDon getHoaDonById(String maHoaDon) {
        String sql = "SELECT * FROM HoaDon WHERE maHoaDon = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maHoaDon);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToHoaDon(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private HoaDon mapResultSetToHoaDon(ResultSet rs) throws SQLException {
        String maHoaDon = rs.getString("maHoaDon");
        Timestamp ts = rs.getTimestamp("ngayTao");
        LocalDateTime ngayTao = ts != null ? ts.toLocalDateTime() : null;
        String trangThai = rs.getString("trangThai");
        String phuongThuc = rs.getString("phuongThuc");
        String ghiChu = rs.getString("ghiChu");
        String maNhanVien = rs.getString("maNhanVien");
        String maKhachHang = rs.getString("maKhachHang");
        KhachHang kh = timKHBangMa(maKhachHang);
        NhanVien nv = timNVBangMa(maNhanVien);



        return new HoaDon(maHoaDon, ngayTao, trangThai, phuongThuc, ghiChu, nv, kh);
    }

    private KhachHang timKHBangMa(String maKH) {
        String sql = "SELECT * FROM KhachHang WHERE maKhachHang = ?";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String ma = rs.getString("maKhachHang");
                    String ten = rs.getString("tenKhachHang");
                    String sdt = rs.getString("soDienThoai");
                    double diem = rs.getBigDecimal("diemTichLuy").doubleValue();

                    return new KhachHang(ma, ten, sdt, diem);
                }
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm khách hàng theo mã: " + e.getMessage());
        }

        return null;
    }

    private NhanVien timNVBangMa(String maNV) {
        String sql = "SELECT * FROM NhanVien WHERE maNhanVien = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new NhanVien(
                            rs.getString("maNhanVien"),
                            rs.getString("tenNhanVien"),
                            rs.getString("chucVu"),
                            rs.getString("CCCD"),
                            rs.getString("soDienThoai"),
                            rs.getDate("ngaySinh") != null ? rs.getDate("ngaySinh").toLocalDate() : null,
                            rs.getDate("ngayVaoLam") != null ? rs.getDate("ngayVaoLam").toLocalDate() : null,
                            rs.getDate("ngayThoiViec") != null ? rs.getDate("ngayThoiViec").toLocalDate() : null
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> layDanhSachMaBanTheoHoaDon(String maHoaDon) {
        List<String> dsMaBan = new ArrayList<>();

        String sql = """
            SELECT p.maBan
            FROM PhieuDatBan p
            JOIN HoaDon h ON h.maHoaDon = p.maHoaDon
            WHERE p.maHoaDon = ? 
        """;

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maHoaDon);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String maBan = rs.getString("maBan");
                    if (maBan != null && !maBan.isBlank()) {
                        dsMaBan.add(maBan);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("[Lỗi] Không thể lấy danh sách mã bàn theo mã hóa đơn: " + e.getMessage());
            e.printStackTrace();
        }

        return dsMaBan;
    }


    public double tinhTienCoc(String maHoaDon) {
        double tongTienCoc = 0;

        String sql = """
            SELECT b.loai, p.ghiChu
            FROM PhieuDatBan p
            JOIN BanAn b ON p.maBan = b.maBan
            WHERE p.maHoaDon = ?
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maHoaDon);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String loaiBan = rs.getString("loai");
                String ghiChu = rs.getString("ghiChu");

                // Nếu ghi chú có "đặt trước" thì mới tính cọc cho bàn đó
                if (ghiChu != null && ghiChu.toLowerCase().contains("đặt trước")) {
                    if ("VIP".equalsIgnoreCase(loaiBan)) {
                        tongTienCoc += 450000;
                    } else {
                        tongTienCoc += 350000;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("[Lỗi] Không thể tính tiền cọc: " + e.getMessage());
            e.printStackTrace();
        }

        return tongTienCoc;
    }


    public double tinhTongTien(String maHoaDon) {
        double tongTien = 0;

        String sql = """
            SELECT SUM(ct.soLuong * m.giaTien) AS tongTien
            FROM ChiTietHoaDon ct
            JOIN MonAn m ON ct.maMonAn = m.maMonAn
            WHERE ct.maHoaDon = ?
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maHoaDon);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                tongTien = rs.getDouble("tongTien");
            }


        } catch (SQLException e) {
            System.err.println("[Lỗi] Không thể tính tổng tiền: " + e.getMessage());
            e.printStackTrace();
        }

        return tongTien;
    }

    public List<ChiTietHoaDon> layDSChiTietTheoMaHoaDon(HoaDon hoaDon) {
        List<ChiTietHoaDon> ds = new ArrayList<>();

        String sql = """
            SELECT ct.maMonAn, ct.soLuong,
                   m.tenMonAn, m.giaTien, m.loaiMon, m.moTa
            FROM ChiTietHoaDon ct
            JOIN MonAn m ON ct.maMonAn = m.maMonAn
            WHERE ct.maHoaDon = ?
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, hoaDon.getMaHoaDon());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                MonAn monAn = new MonAn(
                        rs.getString("maMonAn"),
                        rs.getString("tenMonAn"),
                        rs.getString("loaiMon"),
                        rs.getDouble("giaTien"),
                        rs.getString("moTa")
                );

                ChiTietHoaDon cthd = new ChiTietHoaDon(
                        hoaDon,
                        monAn,
                        rs.getInt("soLuong")
                );

                ds.add(cthd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ds;
    }

    public HoaDon timHoaDonTheoMa(String maHoaDon) {
        HoaDon hoaDon = null;

        String sql = """
            SELECT maHoaDon, ngayTao, trangThai, phuongThuc, ghiChu, 
                   maNhanVien, maKhachHang
            FROM HoaDon
            WHERE maHoaDon = ?
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maHoaDon);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                hoaDon = new HoaDon();
                hoaDon.setMaHoaDon(rs.getString("maHoaDon"));
                hoaDon.setNgayTao(rs.getTimestamp("ngayTao").toLocalDateTime());
                hoaDon.setTrangThai(rs.getString("trangThai"));
                hoaDon.setPhuongThuc(rs.getString("phuongThuc"));
                hoaDon.setGhiChu(rs.getString("ghiChu"));

                // Gán mã (nếu có class NhanVien / KhachHang riêng thì chỉ set mã)
                String maNV = rs.getString("maNhanVien");
                String maKH = rs.getString("maKhachHang");

                hoaDon.setNhanVien(timNVBangMa(maNV));
                hoaDon.setKhachHang(timKHBangMa(maKH));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hoaDon;
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

    public List<String> loadDanhSachHoaDon() {
        List<String> ds = new ArrayList<>();

        String sql = """
                SELECT
                    hd.maHoaDon,
                    kh.tenKhachHang,
                    nv.tenNhanVien,

                    SUM(CASE
                            WHEN kmma.giaSauKhuyenMai IS NOT NULL
                                THEN kmma.giaSauKhuyenMai * cthd.soLuong
                            ELSE ma.giaTien * cthd.soLuong
                        END) AS tongTien,

                    ISNULL(ctkmhd.soTienGiam, 0) AS giamGiaHD,

                    hd.phuongThuc,
                    hd.ngayTao,
                    hd.trangThai,
                    pdb.ghiChu,

                    ba.loai AS loaiBan,

                    STRING_AGG(pdb.maBan, ',') AS danhSachBan

                FROM HoaDon hd
                LEFT JOIN KhachHang kh ON hd.maKhachHang = kh.maKhachHang
                LEFT JOIN NhanVien nv ON hd.maNhanVien = nv.maNhanVien
                LEFT JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon
                LEFT JOIN MonAn ma ON cthd.maMonAn = ma.maMonAn

                LEFT JOIN ChiTietKMMonAn kmma
                    ON kmma.maMonAn = ma.maMonAn
                    AND hd.ngayTao BETWEEN
                        (SELECT ngayBatDau FROM KhuyenMai WHERE maKhuyenMai = kmma.maKhuyenMai)
                        AND
                        (SELECT ngayKetThuc FROM KhuyenMai WHERE maKhuyenMai = kmma.maKhuyenMai)

                LEFT JOIN ChiTietKMHD ctkmhd ON hd.maHoaDon = ctkmhd.maHoaDon

                LEFT JOIN PhieuDatBan pdb ON hd.maHoaDon = pdb.maHoaDon
                LEFT JOIN BanAn ba ON ba.maBan = pdb.maBan

                WHERE ba.loai IS NOT NULL
                GROUP BY
                    hd.maHoaDon,
                    kh.tenKhachHang,
                    nv.tenNhanVien,
                    ctkmhd.soTienGiam,
                    hd.phuongThuc,
                    hd.ngayTao,
                    hd.trangThai,
                    ba.loai,
                    pdb.ghiChu
                """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                String maHD       = rs.getString("maHoaDon");
                String tenKH      = rs.getString("tenKhachHang");
                String tenNV      = rs.getString("tenNhanVien");

                String danhSachBan = rs.getString("danhSachBan");
                String loaiBan     = rs.getString("loaiBan");
                String ghiChu      = rs.getString("ghiChu");

                double tongTien    = rs.getDouble("tongTien");
                double giamGia     = rs.getDouble("giamGiaHD");

                String phuongThuc  = rs.getString("phuongThuc");
                String trangThai   = rs.getString("trangThai");
                Timestamp ngayTao  = rs.getTimestamp("ngayTao");

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                String dong = String.join(",",
                        maHD,
                        tenKH,
                        tenNV,
                        String.valueOf(tongTien),
                        String.valueOf(giamGia),
                        phuongThuc,
                        dtf.format(ngayTao.toLocalDateTime()),
                        trangThai,
                        ghiChu == null ? "" : ghiChu,
                        loaiBan == null ? "" : loaiBan,
                        danhSachBan == null ? "" : danhSachBan
                );

                ds.add(dong);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }




}