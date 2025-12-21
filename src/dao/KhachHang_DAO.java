package dao;

import ConnectDB.ConnectDB;
import entity.KhachHang;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class KhachHang_DAO {

    // Lấy toàn bộ danh sách khách hàng
    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang ORDER BY maKhachHang";

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                KhachHang kh = new KhachHang(
                        rs.getString("maKhachHang"),
                        rs.getString("tenKhachHang"),
                        rs.getString("soDienThoai"),
                        rs.getDouble("diemTichLuy")
                );
                list.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy khách hàng theo mã
    public static KhachHang getKhachHangById(String maKH) {
        KhachHang kh = null;
        String sql = "SELECT * FROM KhachHang WHERE maKhachHang = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    kh = new KhachHang(
                            rs.getString("maKhachHang"),
                            rs.getString("tenKhachHang"),
                            rs.getString("soDienThoai"),
                            rs.getDouble("diemTichLuy")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kh;
    }

    // Lấy khách hàng theo số điện thoại
    public KhachHang getKhachHangBySdt(String sdt) {
        KhachHang kh = null;
        String sql = "SELECT * FROM KhachHang WHERE soDienThoai = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, sdt);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    kh = new KhachHang(
                            rs.getString("maKhachHang"),
                            rs.getString("tenKhachHang"),
                            rs.getString("soDienThoai"),
                            rs.getDouble("diemTichLuy")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn SQL khi tìm khách hàng theo SĐT: " + sdt);
            e.printStackTrace();
        }
        return kh;
    }



    // Thêm khách hàng mới
    public boolean addKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KhachHang (maKhachHang, tenKhachHang, soDienThoai, diemTichLuy) VALUES (?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, kh.getMaKhachHang());
            ps.setString(2, kh.getTenKhachHang());
            ps.setString(3, kh.getSoDienThoai());
            ps.setDouble(4, kh.getDiemTichLuy());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



    public String taoMaKhachHangMoi() {
        // 1. Lấy ngày hiện tại và định dạng thành chuỗi (Ví dụ: 20122025)
        LocalDate now = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy");
        String dateStr = now.format(dtf);

        // 2. Tạo tiền tố mã: KH + Ngày (Ví dụ: KH20122025)
        String prefix = "KH" + dateStr;

        // 3. Tìm mã khách hàng lớn nhất trong DB mà có chứa tiền tố này
        String sql = "SELECT TOP 1 maKhachHang FROM KhachHang " +
                "WHERE maKhachHang LIKE ? " +
                "ORDER BY maKhachHang DESC";

        String maMoi = prefix + "001"; // Mặc định nếu chưa có ai trong ngày hôm nay

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Tìm kiếm các mã bắt đầu bằng "KH20122025%"
            ps.setString(1, prefix + "%");

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String maCuoi = rs.getString("maKhachHang");
                // maCuoi dạng: KH20122025001
                // Lấy 3 số cuối (độ dài chuỗi - 3)
                try {
                    String phanSo = maCuoi.substring(maCuoi.length() - 3);
                    int soTiepTheo = Integer.parseInt(phanSo) + 1;

                    // Format lại thành 3 chữ số (ví dụ: 1 -> "001", 10 -> "010")
                    maMoi = prefix + String.format("%03d", soTiepTheo);
                } catch (NumberFormatException e) {
                    // Phòng trường hợp mã cũ trong DB không đúng định dạng số
                    e.printStackTrace();
                    System.err.println("Lỗi format mã cũ: " + maCuoi + ", reset về 001");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return maMoi;
    }

    // Thêm khách hàng mới với mã tự động
    public boolean themKhachHangMoi(KhachHang kh) {

        String maKHMoi = taoMaKhachHangMoi();

        String sql = "INSERT INTO KhachHang (maKhachHang, tenKhachHang, soDienThoai, diemTichLuy) VALUES (?, ?, ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maKHMoi);
            stmt.setString(2, kh.getTenKhachHang());
            stmt.setString(3, kh.getSoDienThoai());

            if (kh.getDiemTichLuy() == 0.0) {
                stmt.setDouble(4, 0.0);
            } else {
                stmt.setDouble(4, kh.getDiemTichLuy());
            }

            if (stmt.executeUpdate() > 0) {
                kh.setMaKhachHang(maKHMoi);
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi thêm khách hàng mới: " + e.getMessage());
            return false;
        }
    }

    // Cập nhật thông tin khách hàng
    public boolean updateKhachHang(KhachHang kh) {
        String sql = "UPDATE KhachHang SET tenKhachHang=?, soDienThoai=?, diemTichLuy=? WHERE maKhachHang=?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, kh.getTenKhachHang());
            ps.setString(2, kh.getSoDienThoai());
            ps.setDouble(3, kh.getDiemTichLuy());
            ps.setString(4, kh.getMaKhachHang());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // Kiểm tra số điện thoại đã tồn tại chưa (dùng cho thêm mới)
    public boolean isSoDienThoaiExists(String soDienThoai) {
        String sql = "SELECT COUNT(*) FROM KhachHang WHERE soDienThoai = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, soDienThoai);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra số điện thoại đã tồn tại cho khách hàng khác (dùng cho cập nhật)
    public boolean isSoDienThoaiExistsForOther(String soDienThoai, String maKhachHang) {
        String sql = "SELECT COUNT(*) FROM KhachHang WHERE soDienThoai = ? AND maKhachHang != ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, soDienThoai);
            ps.setString(2, maKhachHang);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Tự động sinh mã khách hàng mới
    public String generateMaKhachHang() {
        String sql = "SELECT MAX(maKhachHang) FROM KhachHang WHERE maKhachHang LIKE 'KH%'";

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String lastMa = rs.getString(1);
                if (lastMa != null && lastMa.matches("KH\\d+")) {
                    int number = Integer.parseInt(lastMa.substring(2)) + 1;
                    return String.format("KH%03d", number);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "KH001"; // Mã mặc định nếu không có khách hàng nào
    }


}