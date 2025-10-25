package control;

import java.util.List;

import dao.Dao_CheckIn;
import entity.PhieuDatBan;

public class Crl_CheckIn {

    private Dao_CheckIn dao;

    public Crl_CheckIn() {
        dao = new Dao_CheckIn();
    }

  
    public List<PhieuDatBan> layTatCaPhieu() {
        return dao.getAllPhieuDatBan();
    }

    public List<PhieuDatBan> layPhieuTheoTrangThai(String trangThai) {
        return dao.getPhieuTheoTrangThai(trangThai);
    }

  
    public PhieuDatBan layPhieuTheoMa(String maPhieu) {
        return dao.getPhieuTheoMa(maPhieu);
    }

 
    public boolean thucHienCheckIn(String maPhieu) {
        PhieuDatBan phieu = dao.getPhieuTheoMa(maPhieu);

        if (phieu == null) {
            System.out.println("Không tìm thấy phiếu " + maPhieu);
            return false;
        }

        if ("Đã dùng".equalsIgnoreCase(phieu.getTrangThai())) {
            System.out.println("Phiếu này đã check-in rồi.");
            return false;
        }

        // Logic chính
        boolean ok = dao.capNhatTrangThaiPhieu(maPhieu, "Đã dùng");
        if (ok) {
            System.out.println("Check-in thành công cho phiếu: " + maPhieu);
        } else {
            System.out.println("Cập nhật trạng thái thất bại.");
        }
        return ok;
    }
    public boolean capNhatTrangThai(String maPhieu, String trangThaiMoi) {
        return dao.capNhatTrangThaiPhieu(maPhieu, trangThaiMoi);
    }
    
    public List<String> layThongTinDatBan(){
    		return dao.getThongTinPhieuDatBan();
    }
}
