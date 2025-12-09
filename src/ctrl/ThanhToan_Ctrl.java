package ctrl;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import dao.BanAn_DAO;
import dao.KhachHang_DAO;
import dao.PhieuDatBan_DAO;
import dao.ThanhToan_DAO;
import entity.BanAn;
import entity.HoaDon;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.LoaiBan;
import entity.PhieuDatBan;
import entity.ViTri;
import gui.Gui_ThanhToan;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tooltip;

public class ThanhToan_Ctrl {
    private ThanhToan_DAO dao;
    private Gui_ThanhToan gui;

    public ThanhToan_Ctrl() {
        dao = new ThanhToan_DAO();
    }

    public List<BanAn> layDanhSachBanThanhToan(LocalDate ngay){
        return dao.getDanhSachBanDangSuDungTheoNgay(ngay);
    }

    public List<BanAn> chiaTang(List<BanAn> dsThongTinBan, String tang){
        ViTri viTri;
        if(tang.equals("Tầng 1")) {
            viTri = ViTri.LAU_1;
        }else {
            viTri = ViTri.LAU_2;
        }
        List<BanAn> dsThongTinTheoTang = new ArrayList<BanAn>();
        for(int i = 0; i < dsThongTinBan.size(); i++) {
            if(dsThongTinBan.get(i).getViTri().equals(viTri)) {
                dsThongTinTheoTang.add(dsThongTinBan.get(i));
            }
        }

        return dsThongTinTheoTang;
    }

    public List<String> layDanhSachCTHD(String maHoaDon){
        return dao.getChiTietHoaDonTheoMa(maHoaDon);
    }

    public HoaDon layHoaDonTheoMaBan(String maBan) {
        return dao.getHoaDonTheoMaBan(maBan);
    }

    public PhieuDatBan timPhieuTheoMaBan(String maBan) {
        return dao.timPhieuDatTheoMaBan(maBan);
    }

    public double soTienGiamGia(String maHoaDon) {
        return dao.getSoTienGiamCaoNhatTheoHoaDon(maHoaDon);
    }

    
    public KhachHang timKHBangMa(String maKH) {
        return dao.getKhachHangByMa(maKH);
    }

    public List<KhuyenMai> layDanhSachKhuyenMai(String maHD, double tongTien){
    	
        if(maHD == null || maHD.isEmpty()) {
            gui.showAlert(AlertType.ERROR, "Lỗi mã phiếu", "Mã phiếu null hoặc rỗng");
            return null;
        }

        if(tongTien < 0) {
            gui.showAlert(AlertType.ERROR, "Lỗi tổng tiền", "Tổng tiền phải lớn hơn 0");
            return null;
        }

        return dao.getKhuyenMaiApDungChoHoaDon(maHD, tongTien);
    }

    public double tinhThue (double tongTien) {
        if(tongTien < 0) {
            gui.showAlert(AlertType.ERROR, "Lỗi tổng tiền", "Tổng tiền phải lớn hơn 0");
            return 0;
        }
        return 5/100.0 * tongTien;
    }

    public double tinhCoc(BanAn banAn) {
        if(banAn == null) {
            gui.showAlert(AlertType.ERROR, "Lỗi", "Bàn ăn không được null");
            return 0;
        }
        double tienCoc = banAn.getLoai().equals(LoaiBan.VIP) ? 450000.0 : 350000;
        return tienCoc;
    }
    
    public double tinhCocBangDanhSachPhieu(List<PhieuDatBan> dsPhieu) {
    		if(dsPhieu == null || dsPhieu.isEmpty()) {
    			return 0.0;
    		}
    		
    		double tienCoc = 0.0;
    		
    		for(PhieuDatBan phieu : dsPhieu) {
    			if(!phieu.getGhiChu().equals("Dùng ngay")) {
    				tienCoc += tinhCoc(BanAn_DAO.getByMaBan(phieu.getBan().getMaBan()));
    			}
    		}
    		
    		return tienCoc;
    		
    }

    public double tinhTienGiamGia(double giaTriGiam, boolean giamGiaPhanTram, double tongTien, double giaTriToiDa) {
        if(giaTriGiam < 0 || tongTien < 0 || giaTriToiDa < 0) {
            gui.showAlert(AlertType.ERROR, "Lỗi", "Giá trị giảm và tổng tiền và giá trị tối đa phải lớn hơn 0");
            return 0;
        }

        double tienGiamGia = 0.0;

        if(giamGiaPhanTram) {
            tienGiamGia = tongTien * giaTriGiam/100;
            return tienGiamGia < giaTriToiDa ? tienGiamGia : giaTriToiDa;
        }else {
            return giaTriGiam;
        }

    }

    public double tinhTienThanhToan(double tongTien, double tienCoc, boolean giamGiaPhanTram, double giaTriGiam, double thue, double giaTriToiDa) {
        if(tongTien < 0 || tienCoc < 0 || giaTriGiam < 0 || thue < 0) {
            gui.showAlert(AlertType.ERROR, "Lỗi", "Các giá trị tính toán phải lớn hơn 0");
            return 0;
        }
        double tienGiamGia = tinhTienGiamGia(giaTriGiam, giamGiaPhanTram, tongTien, giaTriToiDa);

        double tienThanhToan = tongTien + thue - tienCoc - tienGiamGia;

        return tienThanhToan;
    }

    public Tooltip taoToolTip(KhuyenMai km) {

        if(km == null) {
//			gui.showAlert(AlertType.ERROR, "Lỗi", "Khuyến mãi không được rỗng");
            return new Tooltip("Không có khuyến mãi áp dụng");
        }

        DecimalFormat formatTienVND = new DecimalFormat("#,##0.0 VND");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String tooltipText = "";
        // Tạo tooltip chi tiết
        if(km.getGiamGiaPhanTram()) {
            tooltipText = String.format(
                    "Tên: %s\nGiảm: %s\nĐiều kiện: %s\nGiá trị giảm tối đa: %s\nNgày hết hạn: %s",
                    km.getTenKhuyenMai(),
                    km.getGiamGiaPhanTram() ? km.getGiaTriGiam() + "%" : formatTienVND.format((km.getGiaTriGiam())),
                    formatTienVND.format(km.getDieuKienApDung()),
                    formatTienVND.format(km.getGiaTriToiDa()),
                    km.getNgayKetThuc().format(dtf)
            );
        }else {
            tooltipText = String.format(
                    "Tên: %s\nGiảm: %s\nĐiều kiện: %s\nNgày hết hạn: %s",
                    km.getTenKhuyenMai(),
                    km.getGiamGiaPhanTram() ? km.getGiaTriGiam() + "%" : formatTienVND.format((km.getGiaTriGiam())),
                    formatTienVND.format(km.getDieuKienApDung()),
                    km.getNgayKetThuc()
            );
        }

        return new Tooltip(tooltipText);

    }

    public List<Integer> suggestCash(int tongTien) {
        // Mệnh giá phổ biến, ưu tiên loại lớn
        int[] uuTien = {5000,10000,20000,50000, 100000, 200000, 500000};
        Set<Integer> goiY = new LinkedHashSet<>();

        // Luôn có gợi ý đầu tiên là đúng tổng tiền
        goiY.add(tongTien);

        // Sinh gợi ý làm tròn lên theo từng mệnh giá lớn (ưu tiên thực tế)
        for (int m : uuTien) {
            int boi = ((tongTien + m - 1) / m) * m;
            if (boi > tongTien)
                goiY.add(boi);
        }

        // Nếu chưa đủ 6, tiếp tục tăng theo mốc hợp lý (nhân 1.5–2 lần)
        List<Integer> result = new ArrayList<>(goiY);
        while (result.size() < 6) {
            int last = result.get(result.size() - 1);
            int next = last + 50000;
            goiY.add(next);
            result = new ArrayList<>(goiY);
        }

        // Trả về danh sách tăng dần, giới hạn 6 gợi ý
        return result.stream().sorted().limit(6).toList();
    }
    
    public List<PhieuDatBan> layDanhSachPhieuBangMaHD(String maHD){
    		if(maHD == null || maHD.trim().isBlank()){
    			return null;
    		}
    		
    		return PhieuDatBan_DAO.getByMaHoaDon(maHD);
    		
    }

    public HoaDon layHoaDonBangMa(String maHD) {
    		if(maHD.trim().isBlank() || maHD == null) {
    			return null;
    		}
    		
    		return dao.getByMaHoaDon(maHD);
    }
    
	public boolean xuLyThanhToan(String maPhieu, String maBan) throws SQLException {

		if (!dao.capNhatTrangThaiHoanTat(maPhieu))
			return false;

		if (!dao.capNhatTrangThaiTrong(maBan))
			return false;

		return true;
	}

    
    public boolean xuLiThanhToanTatCa(List<PhieuDatBan> dsPhieu, String maHoaDon, String phuongThuc, double giamGia,
			KhuyenMai km) {
    		if(dsPhieu == null || dsPhieu.isEmpty() || maHoaDon == null || maHoaDon.trim().isBlank() || phuongThuc == null || phuongThuc.trim().isBlank()) {
    			return false;
    		}
    		
    		if(giamGia < 0 || km == null) {
    			return false;
    		}
    	
    		for(PhieuDatBan phieu : dsPhieu) {
    			try {
    				xuLyThanhToan(phieu.getMaPhieu(), phieu.getBan().getMaBan());
			} catch (Exception e) {
					return false;
			}
    		}
    		
    		try {
    			dao.capNhatTrangThaiThanhToan(maHoaDon, phuongThuc);
    			dao.taoChiTietKMHD(maHoaDon, km.getMaKhuyenMai(), giamGia);
    			
		} catch (Exception e) {
				return false;
		}
    		
    		return true;
    }
    
    public boolean capNhatTichLuy(String maKH, double diemMoi) {
    		KhachHang kh = KhachHang_DAO.getKhachHangById(maKH);
    		diemMoi = kh.getDiemTichLuy() + diemMoi;
    	
    		return dao.updateDiemTichLuy(maKH, diemMoi);
    }
    
    public double tinhTongTien(List<String> dsChiTiet) {
    		if(dsChiTiet == null || dsChiTiet.isEmpty()) {
    			return 0.0;
    		}
    		double tongTien = 0.0;
    		
    		for(String chuoi : dsChiTiet) {
    			tongTien += Double.parseDouble(chuoi.split(",")[3]);
    		}
    		return tongTien;
    }

}