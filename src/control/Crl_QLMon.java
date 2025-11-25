package control;

import java.util.List;

import dao.Dao_QLMon;
import entity.MonAn;

public class Crl_QLMon {

	private Dao_QLMon dao;
	
	public Crl_QLMon() {
		dao = new Dao_QLMon();
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
                    matchLoai = mon.getLoaiMon().equals("Món khai vị");
                    break;
                    
                case "Ăn kèm":
                    matchLoai = mon.getLoaiMon().equals("Món ăn kèm");
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
}
