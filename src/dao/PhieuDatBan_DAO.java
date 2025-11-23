package dao;

import java.sql.*;
import entity.PhieuDatBan;
import ConnectDB.ConnectDB;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;


public class PhieuDatBan_DAO {
    
    private static final DateTimeFormatter SQL_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
}