package control;

import java.time.LocalDate;
import java.util.List;

import dao.Dao_QuanLiKM;
import entity.KhuyenMai;

public class Crl_QuanLiKM {

	private Dao_QuanLiKM dao;
	
	public Crl_QuanLiKM() {
		dao = new  Dao_QuanLiKM();
	}
	
	public List<String> layDanhSachCTKM(){
		return dao.layTatCaKhuyenMaiStrings();
	}
	
	public List<String> layDanhSachMonAnCTKM(LocalDate ngayBD, LocalDate ngayKT){
		return dao.layDanhSachMonAnStrings(ngayBD, ngayKT);
	}
	
	public KhuyenMai timKMMonAn(String maMonAn) {
		return dao.layKhuyenMaiTheoMonAn(maMonAn);
	}
	
	public double tinhGiaSauKM(double giaTien, double giaGiam) {
		return giaTien - giaGiam;		
	}
	
	
	public List<String> layDSMonTheoMaKM(String maKM){
		return dao.getDanhSachMonAnTheoKhuyenMai(maKM);
	}
	
	public String taoMaKhuyenMaiMoi() {
        return dao.taoMaKhuyenMaiTuDong();
    }
	
	// Hàm thêm khuyến mãi
    public boolean themKhuyenMai(KhuyenMai km) {

        // Ví dụ validate đơn giản
        if (km.getTenKhuyenMai() == null || km.getTenKhuyenMai().isEmpty())
            return false;

        if (km.getNgayBatDau().isAfter(km.getNgayKetThuc()))
            return false;

        return dao.insert(km);
    }
    
    public boolean themDSCTKMMonAn(List<String> dsMon, KhuyenMai km) {
    		for(String chuoi : dsMon) {
    			String maMon = chuoi.split("_")[0];
    			String maKm = km.getMaKhuyenMai();
    			double giaSauKM = Double.parseDouble(chuoi.split("_")[3]) - km.getGiaTriGiam();
    			if(!dao.insertCTKMMonAn(maKm, maMon, giaSauKM)) {
    				return false;
    			};
    		}
    		return true;
    	
    }
    
    public boolean ngungKhuyenMai(String maKM) {
    		if(maKM.isEmpty()) {
    			return false;
    		}
    		
    		return dao.ngungKhuyenMai(maKM);
    }
    
    public boolean xoaKhuyenMai(String maKM, int loaiKM) {
    		if(maKM.isEmpty()) {
    			return false;
    		}
    		
    		if(loaiKM == 1) {
    			if(!dao.xoaCTKMMonAn(maKM)) {
    				System.out.println("Lỗi khi xóa chi tiết món ăn của khuyến mãi");
    				return false;
    			}
    		}
        
    		return dao.xoaKhuyenMai(maKM);
    }
	
    public boolean suaKhuyenMai(KhuyenMai km, List<String> dsMon, int loaiKM) {
    	
    		if(km == null) {
    			return false;
    		}
    	
    		if(loaiKM == 1) {
    			if(!dao.xoaCTKMMonAn(km.getMaKhuyenMai())) {
    				System.out.println("Lỗi khi xóa chi tiết món ăn của khuyến mãi");
    				return false;
    			}
    			
    			if(!themDSCTKMMonAn(dsMon, km)) {
    				System.out.println("Lỗi khi update danh sách món ăn mới trong khuyến mãi");
    				return false;
    			}

    		}
    		
    		if(!dao.updateKhuyenMai(km.getMaKhuyenMai(), km.getTenKhuyenMai(), km.getNgayBatDau(), km.getNgayKetThuc(), km.getDieuKienApDung(), km.getGiaTriToiDa(), km.getGiamGiaPhanTram(), km.getGiaTriGiam())) {
				System.out.println("Lỗi khi update khuyến mãi");
				return false;
    		}
    		
    		return true;
    }
    
}
