package dao;

import ConnectDB.ConnectDB;
import entity.TaiKhoan;
import entity.NhanVien;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoan_DAO {

    // Lấy toàn bộ danh sách tài khoản
    public List<TaiKhoan> getAllTaiKhoan() {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = """
            SELECT tk.*, nv.tenNhanVien, nv.soDienThoai, nv.chucVu, nv.CCCD, nv.ngaySinh, nv.ngayVaoLam, nv.ngayThoiViec 
            FROM TaiKhoan tk 
            INNER JOIN NhanVien nv ON tk.maNhanVien = nv.maNhanVien 
            ORDER BY tk.maTaiKhoan
        """;

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Tạo đối tượng Nhân viên
                NhanVien nv = new NhanVien(
                    rs.getString("maNhanVien"),
                    rs.getString("tenNhanVien"),
                    rs.getString("chucVu"),
                    rs.getString("CCCD"),
                    rs.getString("soDienThoai"),
                    rs.getDate("ngaySinh") != null ? rs.getDate("ngaySinh").toLocalDate() : null,
                    rs.getDate("ngayVaoLam") != null ? rs.getDate("ngayVaoLam").toLocalDate() : null,
                    rs.getDate("ngayThoiViec") != null ? rs.getDate("ngayThoiViec").toLocalDate() : null
                );

                // Tạo đối tượng Tài khoản
                TaiKhoan tk = new TaiKhoan(
                    rs.getString("maTaiKhoan"),
                    rs.getString("taiKhoan"),
                    rs.getString("matKhau"),
                    rs.getBoolean("taiKhoanQuanLi"),
                    rs.getBoolean("trangThaiHoatDong"),
                    nv
                );
                list.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm kiếm tài khoản theo mã NV, tên NV, tên đăng nhập hoặc số điện thoại
    public List<TaiKhoan> searchTaiKhoan(String keyword) {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = """
            SELECT tk.*, nv.tenNhanVien, nv.soDienThoai, nv.chucVu, nv.CCCD, nv.ngaySinh, nv.ngayVaoLam, nv.ngayThoiViec 
            FROM TaiKhoan tk 
            INNER JOIN NhanVien nv ON tk.maNhanVien = nv.maNhanVien 
            WHERE tk.maTaiKhoan LIKE ? OR tk.taiKhoan LIKE ? OR nv.tenNhanVien LIKE ? OR nv.maNhanVien LIKE ? OR nv.soDienThoai LIKE ?
            ORDER BY tk.maTaiKhoan
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ps.setString(4, searchPattern);
            ps.setString(5, searchPattern);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                NhanVien nv = new NhanVien(
                    rs.getString("maNhanVien"),
                    rs.getString("tenNhanVien"),
                    rs.getString("chucVu"),
                    rs.getString("CCCD"),
                    rs.getString("soDienThoai"),
                    rs.getDate("ngaySinh") != null ? rs.getDate("ngaySinh").toLocalDate() : null,
                    rs.getDate("ngayVaoLam") != null ? rs.getDate("ngayVaoLam").toLocalDate() : null,
                    rs.getDate("ngayThoiViec") != null ? rs.getDate("ngayThoiViec").toLocalDate() : null
                );

                TaiKhoan tk = new TaiKhoan(
                    rs.getString("maTaiKhoan"),
                    rs.getString("taiKhoan"),
                    rs.getString("matKhau"),
                    rs.getBoolean("taiKhoanQuanLi"),
                    rs.getBoolean("trangThaiHoatDong"),
                    nv
                );
                list.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lọc tài khoản theo quyền
    public List<TaiKhoan> filterTaiKhoanTheoQuyen(boolean isQuanLy) {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = """
            SELECT tk.*, nv.tenNhanVien, nv.soDienThoai, nv.chucVu, nv.CCCD, nv.ngaySinh, nv.ngayVaoLam, nv.ngayThoiViec 
            FROM TaiKhoan tk 
            INNER JOIN NhanVien nv ON tk.maNhanVien = nv.maNhanVien 
            WHERE tk.taiKhoanQuanLi = ?
            ORDER BY tk.maTaiKhoan
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, isQuanLy);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                NhanVien nv = new NhanVien(
                    rs.getString("maNhanVien"),
                    rs.getString("tenNhanVien"),
                    rs.getString("chucVu"),
                    rs.getString("CCCD"),
                    rs.getString("soDienThoai"),
                    rs.getDate("ngaySinh") != null ? rs.getDate("ngaySinh").toLocalDate() : null,
                    rs.getDate("ngayVaoLam") != null ? rs.getDate("ngayVaoLam").toLocalDate() : null,
                    rs.getDate("ngayThoiViec") != null ? rs.getDate("ngayThoiViec").toLocalDate() : null
                );

                TaiKhoan tk = new TaiKhoan(
                    rs.getString("maTaiKhoan"),
                    rs.getString("taiKhoan"),
                    rs.getString("matKhau"),
                    rs.getBoolean("taiKhoanQuanLi"),
                    rs.getBoolean("trangThaiHoatDong"),
                    nv
                );
                list.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Cập nhật trạng thái tài khoản (khóa/mở khóa)
    public boolean updateTrangThaiTaiKhoan(String maTaiKhoan, boolean trangThai) {
        String sql = "UPDATE TaiKhoan SET trangThaiHoatDong = ? WHERE maTaiKhoan = ?";
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setBoolean(1, trangThai);
            ps.setString(2, maTaiKhoan);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Reset mật khẩu
    public boolean resetMatKhau(String maTaiKhoan, String matKhauMoi) {
        String sql = "UPDATE TaiKhoan SET matKhau = ? WHERE maTaiKhoan = ?";
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, matKhauMoi);
            ps.setString(2, maTaiKhoan);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra tên đăng nhập đã tồn tại chưa
    public boolean isTenDangNhapExists(String tenDangNhap) {
        String sql = "SELECT COUNT(*) FROM TaiKhoan WHERE taiKhoan = ?";
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, tenDangNhap);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra nhân viên đã có tài khoản chưa
    public boolean isNhanVienDaCoTaiKhoan(String maNhanVien) {
        String sql = "SELECT COUNT(*) FROM TaiKhoan WHERE maNhanVien = ?";
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, maNhanVien);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Tự động sinh mã tài khoản mới
    public String generateMaTaiKhoan() {
        String sql = "SELECT MAX(maTaiKhoan) FROM TaiKhoan WHERE maTaiKhoan LIKE 'TK%'";
        
        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                String lastMa = rs.getString(1);
                if (lastMa != null && lastMa.matches("TK\\d+")) {
                    int number = Integer.parseInt(lastMa.substring(2)) + 1;
                    return String.format("TK%03d", number);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "TK001"; // Mã mặc định nếu không có tài khoản nào
    }

    // Thêm tài khoản mới
    public boolean addTaiKhoan(TaiKhoan tk) {
        String sql = "INSERT INTO TaiKhoan (maTaiKhoan, taiKhoan, matKhau, taiKhoanQuanLi, trangThaiHoatDong, maNhanVien) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tk.getMaTaiKhoan());
            ps.setString(2, tk.getTaiKhoan());
            ps.setString(3, tk.getMatKhau());
            ps.setBoolean(4, tk.isTaiKhoanQuanLi());
            ps.setBoolean(5, tk.isTrangThaiHoatDong());
            ps.setString(6, tk.getNhanVien().getMaNhanVien());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật thông tin tài khoản
    public boolean updateTaiKhoan(TaiKhoan tk) {
        String sql = "UPDATE TaiKhoan SET taiKhoan = ?, taiKhoanQuanLi = ?, trangThaiHoatDong = ? WHERE maTaiKhoan = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tk.getTaiKhoan());
            ps.setBoolean(2, tk.isTaiKhoanQuanLi());
            ps.setBoolean(3, tk.isTrangThaiHoatDong());
            ps.setString(4, tk.getMaTaiKhoan());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy tài khoản theo mã
    public TaiKhoan getTaiKhoanByMa(String maTaiKhoan) {
        String sql = """
            SELECT tk.*, nv.tenNhanVien, nv.soDienThoai, nv.chucVu, nv.CCCD, nv.ngaySinh, nv.ngayVaoLam, nv.ngayThoiViec 
            FROM TaiKhoan tk 
            INNER JOIN NhanVien nv ON tk.maNhanVien = nv.maNhanVien 
            WHERE tk.maTaiKhoan = ?
        """;
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, maTaiKhoan);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                NhanVien nv = new NhanVien(
                    rs.getString("maNhanVien"),
                    rs.getString("tenNhanVien"),
                    rs.getString("chucVu"),
                    rs.getString("CCCD"),
                    rs.getString("soDienThoai"),
                    rs.getDate("ngaySinh") != null ? rs.getDate("ngaySinh").toLocalDate() : null,
                    rs.getDate("ngayVaoLam") != null ? rs.getDate("ngayVaoLam").toLocalDate() : null,
                    rs.getDate("ngayThoiViec") != null ? rs.getDate("ngayThoiViec").toLocalDate() : null
                );

                return new TaiKhoan(
                    rs.getString("maTaiKhoan"),
                    rs.getString("taiKhoan"),
                    rs.getString("matKhau"),
                    rs.getBoolean("taiKhoanQuanLi"),
                    rs.getBoolean("trangThaiHoatDong"),
                    nv
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

        public List<NhanVien> getNhanVienChuaCoTaiKhoan() {
            List<NhanVien> list = new ArrayList<>();

            PreparedStatement stmt = null;

            // Logic: Lấy tất cả nhân viên mà Mã của họ KHÔNG NẰM TRONG bảng TaiKhoan
            // Lưu ý: Đảm bảo nhân viên đó chưa nghỉ việc (nếu bạn có cột trangThai hoặc ngayThoiViec)
            String sql = "SELECT * FROM NhanVien " +
                    "WHERE maNhanVien NOT IN (SELECT maNhanVien FROM TaiKhoan)";

            try {
                Connection con = ConnectDB.getConnection();
                stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    // Tạo đối tượng nhân viên từ kết quả truy vấn
                    // Bạn cần kiểm tra lại Constructor của class NhanVien xem có khớp thứ tự không nhé
                    // Giả sử constructor là: new NhanVien(ma, ten, sdt, ...)

                    NhanVien nv = new NhanVien();
                    nv.setMaNhanVien(rs.getString("maNhanVien"));
                    nv.setTenNhanVien(rs.getString("tenNhanVien"));
                    nv.setSoDienThoai(rs.getString("soDienThoai"));
                    // Các trường khác nếu cần thiết (chức vụ, ngày sinh...)
                    // nv.setChucVu(rs.getString("chucVu"));

                    list.add(nv);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // Không đóng connection ở đây nếu bạn dùng Singleton ConnectDB dùng chung
            return list;
        }

        /**
         * 2. Kiểm tra tên đăng nhập đã tồn tại chưa
         * Trả về true nếu đã có, false nếu chưa có
         */
        public boolean checkTaiKhoanTonTai(String username) {

            PreparedStatement stmt = null;
            boolean tonTai = false;

            String sql = "SELECT COUNT(*) FROM TaiKhoan WHERE taiKhoan = ?";

            try {
                Connection con = ConnectDB.getConnection();
                stmt = con.prepareStatement(sql);
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    // Nếu số lượng tìm thấy > 0 tức là đã tồn tại
                    tonTai = rs.getInt(1) > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return tonTai;
        }

        /**
         * 3. Thêm mới một tài khoản vào CSDL
         */
        public boolean createTaiKhoan(TaiKhoan tk) {

            PreparedStatement stmt = null;
            int n = 0;

            // Câu lệnh Insert
            // Thứ tự dấu ? phải khớp với thứ tự cột trong Database của bạn
            // Giả sử thứ tự: maTaiKhoan, taiKhoan, matKhau, taiKhoanQuanLi, trangThaiHoatDong, maNhanVien
            String sql = "INSERT INTO TaiKhoan (maTaiKhoan, taiKhoan, matKhau, taiKhoanQuanLi, trangThaiHoatDong, maNhanVien) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            try {
                Connection con = ConnectDB.getConnection();
                stmt = con.prepareStatement(sql);

                stmt.setString(1, tk.getMaTaiKhoan());
                stmt.setString(2, tk.getTaiKhoan());
                stmt.setString(3, tk.getMatKhau()); // Lưu ý: Mật khẩu này phải được mã hóa trước khi truyền vào đây
                stmt.setBoolean(4, tk.isTaiKhoanQuanLi()); // SQL Server bit (1/0) tự map với boolean
                stmt.setBoolean(5, tk.isTrangThaiHoatDong());
                stmt.setString(6, tk.getNhanVien().getMaNhanVien());

                n = stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return n > 0;
        }


    public String tuDongLayMaMoi() {
        String maMoi = "";

        String sql = "SELECT maTaiKhoan FROM TaiKhoan";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            int max = 0;
            while(rs.next()){
                String ma = rs.getString(1);
                // Kiểm tra xem mã có đúng định dạng TK + số không
                if(ma.matches("TK\\d+")){
                    try {
                        // Cắt bỏ chữ "TK" để lấy số. Ví dụ: "TK010" -> "010" -> 10
                        String soStr = ma.substring(2);

                        // QUAN TRỌNG: Nếu mã quá dài (do cái timestamp cũ gây ra), bỏ qua nó
                        // Số thứ tự thường chỉ tối đa 4-5 chữ số thôi
                        if (soStr.length() > 5) continue;

                        int so = Integer.parseInt(soStr);
                        if(so > max) max = so;
                    } catch(NumberFormatException e){
                        // Bỏ qua nếu không phải số
                    }
                }
            }
            // Cộng thêm 1 và format thành 3 chữ số (ví dụ: 1 -> 001, 11 -> 011)
            maMoi = "TK" + String.format("%03d", max + 1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maMoi; // Kết quả sẽ là TK011
    }
    /**
     * 4. Lấy mật khẩu hiện tại (đã mã hóa) của nhân viên
     * Dùng để so sánh khi người dùng đổi mật khẩu
     */
    public String getMatKhauByMaNV(String maNV) {

        PreparedStatement stmt = null;
        String matKhau = null;

        // Truy vấn lấy mật khẩu dựa trên Mã Nhân Viên
        String sql = "SELECT matKhau FROM TaiKhoan WHERE maNhanVien = ?";

        try {
            Connection con = ConnectDB.getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setString(1, maNV);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                matKhau = rs.getString("matKhau");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matKhau;
    }

    /**
     * 5. Cập nhật mật khẩu mới cho nhân viên
     * @param maNV Mã nhân viên cần đổi pass
     * @param newPassHash Mật khẩu MỚI đã được mã hóa SHA-256
     */
    public boolean updateMatKhau(String maNV, String newPassHash) {

        PreparedStatement stmt = null;
        int n = 0;

        String sql = "UPDATE TaiKhoan SET matKhau = ? WHERE maNhanVien = ?";

        try {
            Connection con = ConnectDB.getConnection();
            stmt = con.prepareStatement(sql);

            // Tham số 1: Mật khẩu mới (đã hash)
            stmt.setString(1, newPassHash);

            // Tham số 2: Điều kiện là mã nhân viên
            stmt.setString(2, maNV);

            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return n > 0; // Trả về true nếu update thành công
    }
}