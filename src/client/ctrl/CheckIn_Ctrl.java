package client.ctrl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import client.service.CheckInClient;
import common.entity.PhieuDatBan;
import common.entity.TrangThai;

public class CheckIn_Ctrl {

    private CheckInClient dao;

    public CheckIn_Ctrl() {
        dao = new CheckInClient();
    }

    public boolean capNhatTrangThai(String maPhieu, String trangThaiMoi) {
        return dao.capNhatTrangThaiPhieuDatBan(maPhieu, trangThaiMoi);
    }
    
    public boolean capNhatTrangThaiBan(String maBan, TrangThai trangThai) {
    		if(maBan == null || trangThai == null) {
    			return false;
    		}
    		
    		String trangThaiString = trangThai.getThongTin();
    		
    		return dao.capNhatTrangThaiBan(maBan, trangThaiString);
    	
    }
    
    

}
	