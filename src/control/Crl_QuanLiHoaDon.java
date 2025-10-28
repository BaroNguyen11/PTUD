package control;

import java.util.List;

import dao.Dao_QuanLiHoaDon;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Crl_QuanLiHoaDon {
	
	private Dao_QuanLiHoaDon dao;
	
	public Crl_QuanLiHoaDon() {
		dao = new Dao_QuanLiHoaDon();
	}
	
	public ObservableList<HoaDon> layDanhSachHoaDon(){
		List<HoaDon> dsHoaDon = dao.getAllHoaDon();
	
		return FXCollections.observableArrayList(dsHoaDon);
	}
	
	
	public List<String> layDSMaBanBangMaHoaDon(String maHoaDon) {
		return dao.layDanhSachMaBanTheoHoaDon(maHoaDon);
	}
	
	public double tinhTongTienHoaDon(String maHoaDon) {
		return dao.tinhTongTien(maHoaDon);
	}
	
	public double tinhTienCoc(String maHoaDon) {
		return dao.tinhTienCoc(maHoaDon);
	}
	
	public List<ChiTietHoaDon> layDanhSachChiTietHD(HoaDon hoaDon){
		return dao.layDSChiTietTheoMaHoaDon(hoaDon);
	}
	
	public HoaDon timHoaDonTheoMa(String maHoaDon) {
        return dao.timHoaDonTheoMa(maHoaDon);
    }
	
	public List<String> dsThongTinMonAnTheoMaHD(String maHoaDon){
		return dao.getChiTietHoaDonTheoMa(maHoaDon);
	}
}
