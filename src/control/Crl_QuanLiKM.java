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
    		return dao.ngungKhuyenMai(maKM);
    }
	
}
