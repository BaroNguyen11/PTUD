package ctrl;

import java.util.List;

import dao.QLMon_DAO;
import entity.MonAn;

public class QLMon_Ctrl {

	private QLMon_DAO dao;
	
	public QLMon_Ctrl() {
		dao = new QLMon_DAO();
	}

	public List<MonAn> layDSMon(){
		return dao.getDanhSachMonAn();
		
	}
	
	
	public List<MonAn> locDSMon(List<MonAn> dsMon, String maMon, String loai){
		List<MonAn> ketQua = dsMon.stream().filter(mon -> {

            boolean matchTimKiem = maMon.isEmpty() ||
                    mon.getMaMonAn().equals(maMon);
            
            // ==== Lọc theo loại ====
            boolean matchLoai = true;

            switch (loai) {
                case "Món chính":
                    matchLoai = mon.getLoaiMon().equals("Món chính");
                    break;
                    
                case "Khai vị":
                    matchLoai = mon.getLoaiMon().equals("Khai vị");
                    break;
                    
                case "Ăn kèm":
                    matchLoai = mon.getLoaiMon().equals("Ăn kèm");
                    break;
                    
                case "Nước uống":
                    matchLoai = mon.getLoaiMon().equals("Đồ uống");
                    break;
                    
                case "Nước sốt":
                    matchLoai = mon.getLoaiMon().equals("Nước sốt");
                    break;
                
                case "Tráng miệng":
                    matchLoai = mon.getLoaiMon().equals("Tráng miệng");
                    break;    
                    
                default:
                    matchLoai = true; 
            }

            return matchTimKiem && matchLoai;

        }).toList();
		
		return ketQua;
	}
	
	
	public boolean themMonMoi(MonAn mon) {
		if(mon == null ) {
			return false;
		}
		
		return dao.insertMon(mon);
	}
	
	public String taoMaMonMoi () {
		return dao.taoMaMonAn();
	}
	
	public boolean capNhatMonAn(MonAn monMoi) {
		if(monMoi == null || monMoi.getMaMonAn() == null) {
			return false;
		}
		
		return dao.updateMon(monMoi);
	}
}