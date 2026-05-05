package client.ctrl;

import client.service.CheckInClient;
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
    		
    		String trangThaiString = trangThai.name();
    		
    		return dao.capNhatTrangThaiBan(maBan, trangThaiString);
    	
    }
    
    

}
	
