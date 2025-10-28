package control;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dao.Dao_ThanhToan;
import entity.BanAn;
import entity.ViTri;

public class Crl_ThanhToan {
	private Dao_ThanhToan dao;
	
	public Crl_ThanhToan() {
		dao = new Dao_ThanhToan();
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
}
