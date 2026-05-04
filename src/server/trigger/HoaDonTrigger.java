package server.trigger;

import server.dao.KhachHang_DAO;
import common.entity.KhachHang;
import common.entity.HoaDon;

/**
 * HoaDonTrigger - Kích hoạt tự động khi có thao tác trên Hóa Đơn.
 * Đóng vai trò như Database Trigger nhưng ở tầng Application.
 */
public class HoaDonTrigger {
    
    private static final KhachHang_DAO khDao = new KhachHang_DAO();

    /**
     * Tự động cộng điểm tích lũy cho khách hàng khi hóa đơn hoàn tất.
     * Quy đổi: 10,000 VNĐ = 1 điểm.
     */
    public static void onHoaDonInserted(HoaDon hd, String maKhachHang, double tongTien) {
        if (maKhachHang == null || maKhachHang.isEmpty() || maKhachHang.equals("KH000")) {
            return; // Khách vãng lai, không cộng điểm
        }
        
        try {
            KhachHang kh = KhachHang_DAO.getKhachHangById(maKhachHang);
            if (kh != null) {
                // Tính điểm cộng thêm (Ví dụ: 10k = 1 điểm)
                double diemCongThem = Math.floor(tongTien / 10000.0);
                if (diemCongThem > 0) {
                    double diemMoi = kh.getDiemTichLuy() + diemCongThem;
                    kh.setDiemTichLuy(diemMoi);
                    khDao.updateKhachHang(kh);
                    System.out.println("[Trigger] Đã cộng " + diemCongThem + " điểm cho khách hàng " + maKhachHang);
                }
            }
        } catch (Exception e) {
            System.err.println("[Trigger] Lỗi khi cập nhật điểm tích lũy: " + e.getMessage());
        }
    }
}
