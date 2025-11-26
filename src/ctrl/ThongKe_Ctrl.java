/*
 * @ (#) ThongKe_Ctrl .java     1.0     10/27/2025
 * Copyright (c) 2025 IUH.All Rights Reserved.
 */
package ctrl;

import dao.ThongKe_DAO;

import java.util.List;
import java.util.Map;

/*
 *@description: this is a class to test the ThongKe_Ctrl class
 *@author: Bao Nguyen
 *@Date: 10/27/2025
 *@version:      1.0
 */
public class ThongKe_Ctrl {
    ThongKe_DAO thongKeDao = new ThongKe_DAO();

    public double getTongDoanhThu(){
        return  thongKeDao.getTongDoanhThu();
    }
    public double getDoanhThuTBBan(){
        return thongKeDao.getDoanhThuTrungBinhBan();
    }
    public double getTiLeTienMat(){
        return thongKeDao.getTiLeTienMat();
    }
    public double getDoanhThuCaToi(){
        return thongKeDao.getDoanhThuCaToi();
    }
    public Map<String, Double> thongKeDoanhThuTheoThang(int nam){
        return thongKeDao.getDoanhThuTheoThang(nam);
    }
    public  Map<String,Double> getDoanhThuTheoCa() {
        return thongKeDao.getDoanhThuTheoCa();
    }
    public Map<String,Integer> getTopMonAnBanChay(){
        return thongKeDao.getTopMonAnBanChay();
    }
    public Map<String,Double> getDoanhThuTheoNhomMon(){
        return  thongKeDao.getDoanhThuTheoNhomMon();
    }
    public List<String[]> getTopKhachHang() {
        return thongKeDao.getTopKhachHang();
    }

    public int[] getThongKeKhachHang() {
        return thongKeDao.getThongKeKhachHang();
    }

    public double getChiTieuTrungBinh() {
        return thongKeDao.getChiTieuTrungBinh();
    }

    public double getTanSuatTrungBinh() {
        return thongKeDao.getTanSuatTrungBinh();
    }
    // Trong ThongKe_Ctrl.java

    public String[] getMonBanChayNhat() {
        return thongKeDao.getMonBanChayNhat();
    }

    public String[] getMonDoanhThuCaoNhat() {
        return thongKeDao.getMonDoanhThuCaoNhat();
    }

    public double[] getThongKeDoUong() {
        return thongKeDao.getThongKeDoUong();
    }
}
