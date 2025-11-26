package control;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import dao.QLHD_DAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.LoaiBan;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class QLHD_Ctrl {

    private QLHD_DAO dao;

    public QLHD_Ctrl() {
        dao = new QLHD_DAO();
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

    public List<String> loadDSHoaDon(){
        return dao.loadDanhSachHoaDon();
    }

    public double tinhTienCocLoaiVaGhiChu(LoaiBan loai, String ghiChu) {
        if(loai == null) {
            return -1;
        }

        if(ghiChu.equalsIgnoreCase("Dùng ngay")) {
            return 0;
        }

        if(loai.equals(LoaiBan.VIP)) {
            return 450000;
        }else {
            return 350000;
        }
    }

    public double tinhThue(double tongTien) {
        if(tongTien < 0) {
            return -1;
        }

        return tongTien * 0.05;
    }

    public double tinhTienThanhToan(double tongTien, double giamGia, double thue, double coc) {
        return (tongTien + thue) - (giamGia + coc);
    }


    public String timHoaDonChuoi(String maHoaDon,ObservableList<String> dsHoaDon) {
        if(dsHoaDon == null || maHoaDon == null || maHoaDon.isBlank() ) {
            System.out.println("Lỗi khi truyền mã hóa đơn và danh sách hóa đơn tại hàm timHoaDonChuoi");
            return null;
        }

        String chuoi = "";
        for(String i : dsHoaDon) {
            if(i.split(",")[0].equals(maHoaDon))
                return i;
        }
        return null;
    }

    public ObservableList<String> locHoaDonTheoNgay(ObservableList<String> dsHoaDon, LocalDate ngayChon){
        if(dsHoaDon == null || ngayChon == null) {
            System.out.println("Lỗi trong tham số truyền vào trong hàm locHoaDonTheoNgay");
            return null;
        }

        List<String> dsLoc = new ArrayList<String>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for(String chuoi : dsHoaDon) {
            System.out.println(chuoi.split(",")[6]  + "và " + dtf.format(ngayChon));
            if(chuoi.split(",")[6].equals(dtf.format(ngayChon)))
                dsLoc.add(chuoi);
        }
        return FXCollections.observableArrayList(dsLoc);

    }

    public ObservableList<String> locHoaDonTheoTrangThai(ObservableList<String> dsHoaDon, String trangThai){
        if(dsHoaDon == null || trangThai == null) {
            System.out.println("Lỗi trong tham số truyền vào trong hàm locHoaDonTheoTrangThai");
            return null;
        }

        List<String> dsLoc = new ArrayList<String>();

        for(String chuoi : dsHoaDon) {
            if(chuoi.split(",")[7].equals(trangThai))
                dsLoc.add(chuoi);
        }
        return FXCollections.observableArrayList(dsLoc);

    }

    public ObservableList<String> locHoaDon(ObservableList<String> dsHoaDon, String maTim, LocalDate ngayChon, String trangThai) {
        if(dsHoaDon == null) return FXCollections.observableArrayList();

        ObservableList<String> dsLoc = FXCollections.observableArrayList();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (String s : dsHoaDon) {
            if (s == null || s.isBlank()) continue;

            String[] parts = s.split(",");

            // 1. Lọc theo mã
            boolean matchesMa = (maTim == null || maTim.isBlank()) || parts[0].equals(maTim);

            // 2. Lọc theo ngày
            boolean matchesNgay = true;
            if (ngayChon != null) {
                try {
                    LocalDate ngayHD = LocalDate.parse(parts[6], dtf);
                    matchesNgay = ngayHD.equals(ngayChon);
                } catch (Exception ex) {
                    matchesNgay = false;
                }
            }

            // 3. Lọc theo trạng thái
            boolean matchesTrangThai = trangThai == null || trangThai.equals("Tất cả") || parts[7].equalsIgnoreCase(trangThai);

            if (matchesMa && matchesNgay && matchesTrangThai) {
                dsLoc.add(s);
            }
        }

        return dsLoc;
    }
}