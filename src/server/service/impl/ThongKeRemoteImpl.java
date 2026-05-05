package server.service.impl;

import server.core.RemoteCallLogger;
import common.ThongKeRemote;
import server.dao.ThongKe_DAO;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ThongKeRemoteImpl extends UnicastRemoteObject implements ThongKeRemote {
    private final ThongKe_DAO dao = new ThongKe_DAO();

    public ThongKeRemoteImpl() throws RemoteException {
    }

    public Map<String, Double> getDoanhThuTheoThang(int nam) throws RemoteException { return RemoteCallLogger.log("ThongKeRemoteImpl", "getDoanhThuTheoThang", new Object[]{nam}, () -> dao.getDoanhThuTheoThang(nam)); }
    public double getTongDoanhThu() throws RemoteException { return RemoteCallLogger.log("ThongKeRemoteImpl", "getTongDoanhThu", new Object[]{}, () -> dao.getTongDoanhThu()); }
    public double getDoanhThuThangTruoc() throws RemoteException { return RemoteCallLogger.log("ThongKeRemoteImpl", "getDoanhThuThangTruoc", new Object[]{}, () -> dao.getDoanhThuThangTruoc()); }
    public double getDoanhThuThangNay() throws RemoteException { return RemoteCallLogger.log("ThongKeRemoteImpl", "getDoanhThuThangNay", new Object[]{}, () -> dao.getDoanhThuThangNay()); }
    public double getDoanhThuTheoKhoangTG(LocalDate from, LocalDate to) throws RemoteException { return RemoteCallLogger.log("ThongKeRemoteImpl", "getDoanhThuTheoKhoangTG", new Object[]{from, to}, () -> dao.getDoanhThuTheoKhoangTG(from, to)); }
    public int[] getThongKeKhachHang(LocalDate from, LocalDate to) throws RemoteException { return RemoteCallLogger.log("ThongKeRemoteImpl", "getThongKeKhachHang", new Object[]{from, to}, () -> dao.getThongKeKhachHang(from, to)); }
    public List<String[]> getTopKhachHang(LocalDate from, LocalDate to) throws RemoteException { return RemoteCallLogger.log("ThongKeRemoteImpl", "getTopKhachHang", new Object[]{from, to}, () -> dao.getTopKhachHang(from, to)); }
    public Map<String, Integer> getTopMonAnBanChay(LocalDate from, LocalDate to) throws RemoteException { return RemoteCallLogger.log("ThongKeRemoteImpl", "getTopMonAnBanChay", new Object[]{from, to}, () -> dao.getTopMonAnBanChay(from, to)); }
    public Map<String, Double> getDoanhThuTheoNhomMon(LocalDate from, LocalDate to) throws RemoteException { return RemoteCallLogger.log("ThongKeRemoteImpl", "getDoanhThuTheoNhomMon", new Object[]{from, to}, () -> dao.getDoanhThuTheoNhomMon(from, to)); }
    public double getDoanhThuTrungBinhBan() throws RemoteException { return RemoteCallLogger.log("ThongKeRemoteImpl", "getDoanhThuTrungBinhBan", new Object[]{}, () -> dao.getDoanhThuTrungBinhBan()); }
    public double getTiLeTienMat() throws RemoteException { return RemoteCallLogger.log("ThongKeRemoteImpl", "getTiLeTienMat", new Object[]{}, () -> dao.getTiLeTienMat()); }
    public double getDoanhThuCaToi() throws RemoteException { return RemoteCallLogger.log("ThongKeRemoteImpl", "getDoanhThuCaToi", new Object[]{}, () -> dao.getDoanhThuCaToi()); }
    public List<String[]> getTopKhachHang() throws RemoteException { return RemoteCallLogger.log("ThongKeRemoteImpl", "getTopKhachHang", new Object[]{}, () -> dao.getTopKhachHang()); }
    public int[] getThongKeKhachHang() throws RemoteException { return RemoteCallLogger.log("ThongKeRemoteImpl", "getThongKeKhachHang", new Object[]{}, () -> dao.getThongKeKhachHang()); }
    public double getChiTieuTrungBinh() throws RemoteException { return RemoteCallLogger.log("ThongKeRemoteImpl", "getChiTieuTrungBinh", new Object[]{}, () -> dao.getChiTieuTrungBinh()); }
    public double getTanSuatTrungBinh() throws RemoteException { return RemoteCallLogger.log("ThongKeRemoteImpl", "getTanSuatTrungBinh", new Object[]{}, () -> dao.getTanSuatTrungBinh()); }
    public Map<String, Double> getDoanhThuTheoCa() throws RemoteException { return RemoteCallLogger.log("ThongKeRemoteImpl", "getDoanhThuTheoCa", new Object[]{}, () -> dao.getDoanhThuTheoCa()); }
    public Map<String, Integer> getTopMonAnBanChay() throws RemoteException { return RemoteCallLogger.log("ThongKeRemoteImpl", "getTopMonAnBanChay", new Object[]{}, () -> dao.getTopMonAnBanChay()); }
    public Map<String, Double> getDoanhThuTheoNhomMon() throws RemoteException { return RemoteCallLogger.log("ThongKeRemoteImpl", "getDoanhThuTheoNhomMon", new Object[]{}, () -> dao.getDoanhThuTheoNhomMon()); }
    public String[] getMonBanChayNhat() throws RemoteException { return RemoteCallLogger.log("ThongKeRemoteImpl", "getMonBanChayNhat", new Object[]{}, () -> dao.getMonBanChayNhat()); }
    public String[] getMonDoanhThuCaoNhat() throws RemoteException { return RemoteCallLogger.log("ThongKeRemoteImpl", "getMonDoanhThuCaoNhat", new Object[]{}, () -> dao.getMonDoanhThuCaoNhat()); }
    public double[] getThongKeDoUong() throws RemoteException { return RemoteCallLogger.log("ThongKeRemoteImpl", "getThongKeDoUong", new Object[]{}, () -> dao.getThongKeDoUong()); }
    public int getTongLuotDatBan() throws RemoteException { return RemoteCallLogger.log("ThongKeRemoteImpl", "getTongLuotDatBan", new Object[]{}, () -> dao.getTongLuotDatBan()); }
    public double getTyLeLapDay() throws RemoteException { return RemoteCallLogger.log("ThongKeRemoteImpl", "getTyLeLapDay", new Object[]{}, () -> dao.getTyLeLapDay()); }
    public double getTyLeHuyDat() throws RemoteException { return RemoteCallLogger.log("ThongKeRemoteImpl", "getTyLeHuyDat", new Object[]{}, () -> dao.getTyLeHuyDat()); }
    public double getThoiGianSuDungTB() throws RemoteException { return RemoteCallLogger.log("ThongKeRemoteImpl", "getThoiGianSuDungTB", new Object[]{}, () -> dao.getThoiGianSuDungTB()); }
    public Map<String, Double> getHieuSuatKhuVuc() throws RemoteException { return RemoteCallLogger.log("ThongKeRemoteImpl", "getHieuSuatKhuVuc", new Object[]{}, () -> dao.getHieuSuatKhuVuc()); }
}
