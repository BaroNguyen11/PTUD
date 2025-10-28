package ctrl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dao.CheckIn_DAO;
import entity.PhieuDatBan;

public class CheckIn_Ctrl {

    private CheckIn_DAO dao;

    public CheckIn_Ctrl() {
        dao = new CheckIn_DAO();
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

//    public List<String> chiaTang(List<String> dsThongTinBan, String tang){
//
//        List<String> dsThongTinTheoTang = new ArrayList<String>();
//        for(int i = 0; i < dsThongTinBan.size(); i++) {
//            if(dsThongTinBan.get(i).split(",")[9].equals(tang)) {
//                dsThongTinTheoTang.add(dsThongTinBan.get(i));
//            }
//        }
//
//        return dsThongTinTheoTang;
//    }
public List<String> chiaTang(List<String> dsThongTinBan, String tang) {
    List<String> dsThongTinTheoTang = new ArrayList<>();
    if (dsThongTinBan == null) return dsThongTinTheoTang;

    String tangNormalized = tang == null ? "" : tang.trim();
    for (String item : dsThongTinBan) {
        if (item == null) continue;
        String[] parts = item.split(",", -1); // giữ các ô rỗng
        String viTri = parts.length > 9 ? parts[9].trim() : "";
        if (viTri.equalsIgnoreCase(tangNormalized)) {
            dsThongTinTheoTang.add(item);
        }
    }
    return dsThongTinTheoTang;
}

}
	