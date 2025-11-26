package ctrl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dao.CheckIn_DAO;
import entity.PhieuDatBan;
import entity.TrangThai;

public class CheckIn_Ctrl {

    private CheckIn_DAO dao;

    public CheckIn_Ctrl() {
        dao = new CheckIn_DAO();
    }

    public boolean capNhatTrangThai(String maPhieu, String trangThaiMoi) {
        return dao.capNhatTrangThaiPhieu(maPhieu, trangThaiMoi);
    }
    
    public boolean capNhatTrangThaiBan(String maBan, TrangThai trangThai) {
    		if(maBan == null || trangThai == null) {
    			return false;
    		}
    		
    		String trangThaiString = trangThai.getThongTin();
    		
    		return dao.capNhatTrangThaiBan(maBan, trangThaiString);
    	
    }
    
    

}
	