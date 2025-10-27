package control;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dao.Dao_CheckIn;
import entity.PhieuDatBan;

public class Crl_CheckIn {

    private Dao_CheckIn dao;

    public Crl_CheckIn() {
        dao = new Dao_CheckIn();
    }

    public boolean capNhatTrangThai(String maPhieu, String trangThaiMoi) {
        return dao.capNhatTrangThaiPhieu(maPhieu, trangThaiMoi);
    }
    
    public List<String> layThongTinDatBan(LocalDate ngay){
    		return dao.getThongTinPhieuDatBan(ngay);
    }
    
    public String timBanBangSDT(String sdt, LocalDate ngay) {
    		return dao.layThongTinDatBanTheoSDT(sdt, ngay);
    }
    
    public List<String> chiaTang(List<String> dsThongTinBan, String tang){
    		
    		List<String> dsThongTinTheoTang = new ArrayList<String>();
    		for(int i = 0; i < dsThongTinBan.size(); i++) {
    			if(dsThongTinBan.get(i).split(",")[10].equals(tang)) {
    				dsThongTinTheoTang.add(dsThongTinBan.get(i));
    			}
    		}
    		
    		return dsThongTinTheoTang;
    }
}
	