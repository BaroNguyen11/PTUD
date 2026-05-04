/*
 * @ (#) ThongKe_Ctrl .java     1.0     10/27/2025
 * Copyright (c) 2025 IUH.All Rights Reserved.
 */
package client.ctrl;

import client.service.ThongKeClient;

import java.util.List;
import java.util.Map;

/*
 *@description: this is a class to test the ThongKe_Ctrl class
 *@author: Bao Nguyen
 *@Date: 10/27/2025
 *@version:      1.0
 */
public class ThongKe_Ctrl {
    ThongKeClient thongKeClient = new ThongKeClient();
    public double getDoanhThuThangTruoc(){
        return  thongKeClient.getDoanhThuThangTruoc();
    }
    public double getTongTienThangNay(){
        return thongKeClient.getDoanhThuThangNay();
    }
    public double getDoanhThuTBBan(){
        return thongKeClient.getDoanhThuTrungBinhBan();
    }
    public double getTiLeTienMat(){
        return thongKeClient.getTiLeTienMat();
    }
    public double getDoanhThuCaToi(){
        return thongKeClient.getDoanhThuCaToi();
    }
    public Map<String, Double> thongKeDoanhThuTheoThang(int nam){
        return thongKeClient.getDoanhThuTheoThang(nam);
    }
    public  Map<String,Double> getDoanhThuTheoCa() {
        return thongKeClient.getDoanhThuTheoCa();
    }
    public Map<String,Integer> getTopMonAnBanChay(){
        return thongKeClient.getTopMonAnBanChay();
    }
    public Map<String,Double> getDoanhThuTheoNhomMon(){
        return  thongKeClient.getDoanhThuTheoNhomMon();
    }
    public List<String[]> getTopKhachHang() {
        return thongKeClient.getTopKhachHang();
    }

    public int[] getThongKeKhachHang() {
        return thongKeClient.getThongKeKhachHang();
    }

    public double getChiTieuTrungBinh() {
        return thongKeClient.getChiTieuTrungBinh();
    }

    public double getTanSuatTrungBinh() {
        return thongKeClient.getTanSuatTrungBinh();
    }
    // Trong ThongKe_Ctrl.java

    public String[] getMonBanChayNhat() {
        return thongKeClient.getMonBanChayNhat();
    }

    public String[] getMonDoanhThuCaoNhat() {
        return thongKeClient.getMonDoanhThuCaoNhat();
    }

    public double[] getThongKeDoUong() {
        return thongKeClient.getThongKeDoUong();
    }
    public int getTongLuotDatBan(){
        return thongKeClient.getTongLuotDatBan();
    }

    public double getTyLeLapDay() {
        return thongKeClient.getTyLeLapDay();
    }

    public double getTyLeHuyDat() {
        return thongKeClient.getTyLeHuyDat();
    }

    public double getThoiGianSuDungTB() {
        return thongKeClient.getThoiGianSuDungTB();
    }

    public Map<String, Double> getHieuSuatKhuVuc() {
        return thongKeClient.getHieuSuatKhuVuc();
    }
}
