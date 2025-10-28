package control;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dao.Dao_ThanhToan;
import entity.BanAn;
import entity.HoaDon;
import entity.PhieuDatBan;
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
	
	public HoaDon layHoaDonTheoMaBan(String maBan) {
		return dao.getHoaDonTheoMaBan(maBan);
	}
	
	public PhieuDatBan timPhieuTheoMaBan(String maBan) {
		return dao.timPhieuDatTheoMaBan(maBan);
	}
	
	public double soTienGiamGia(String maHoaDon) {
		return dao.getSoTienGiamCaoNhatTheoHoaDon(maHoaDon);
	}
	
	public boolean xuLyThanhToan(String maHoaDon, String maPhieu, String maBan) throws SQLException {
        boolean hd = dao.capNhatTrangThaiThanhToan(maHoaDon);
        boolean pd =	dao.capNhatTrangThaiHoanTat(maPhieu);
        boolean ba = dao.capNhatTrangThaiTrong(maBan);

        return hd && pd && ba;
    }
}
