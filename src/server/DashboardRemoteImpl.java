package server;

import common.DashboardRemote;
import server.dao.Dashboard_DAO;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.Map;

public class DashboardRemoteImpl extends UnicastRemoteObject implements DashboardRemote {
    private final Dashboard_DAO dao = new Dashboard_DAO();

    public DashboardRemoteImpl() throws RemoteException {
    }

    public double getTongDoanhThuTheoNgay(LocalDate ngay) throws RemoteException { return RemoteCallLogger.log("DashboardRemoteImpl", "getTongDoanhThuTheoNgay", new Object[]{ngay}, () -> dao.getTongDoanhThuTheoNgay(ngay)); }
    public double getTongDoanhThuHomNay() throws RemoteException { return RemoteCallLogger.log("DashboardRemoteImpl", "getTongDoanhThuHomNay", new Object[]{}, () -> dao.getTongDoanhThuHomNay()); }
    public double getTongDoanhThuHomQua() throws RemoteException { return RemoteCallLogger.log("DashboardRemoteImpl", "getTongDoanhThuHomQua", new Object[]{}, () -> dao.getTongDoanhThuHomQua()); }
    public Map<String, Double> getDoanhThuHomNayTheoGio() throws RemoteException { return RemoteCallLogger.log("DashboardRemoteImpl", "getDoanhThuHomNayTheoGio", new Object[]{}, () -> dao.getDoanhThuHomNayTheoGio()); }
    public Map<String, Double> getDoanhThuTheoThang() throws RemoteException { return RemoteCallLogger.log("DashboardRemoteImpl", "getDoanhThuTheoThang", new Object[]{}, () -> dao.getDoanhThuTheoThang()); }
    public int getLuotDatBanHomNay() throws RemoteException { return RemoteCallLogger.log("DashboardRemoteImpl", "getLuotDatBanHomNay", new Object[]{}, () -> dao.getLuotDatBanHomNay()); }
    public int getLuotDatBanHomQua() throws RemoteException { return RemoteCallLogger.log("DashboardRemoteImpl", "getLuotDatBanHomQua", new Object[]{}, () -> dao.getLuotDatBanHomQua()); }
    public int getSoMonBanRaHomNay() throws RemoteException { return RemoteCallLogger.log("DashboardRemoteImpl", "getSoMonBanRaHomNay", new Object[]{}, () -> dao.getSoMonBanRaHomNay()); }
    public int getSoMonBanRaHomQua() throws RemoteException { return RemoteCallLogger.log("DashboardRemoteImpl", "getSoMonBanRaHomQua", new Object[]{}, () -> dao.getSoMonBanRaHomQua()); }
    public Map<String, Integer> getTopMonBanChayHomNay(int limit) throws RemoteException { return RemoteCallLogger.log("DashboardRemoteImpl", "getTopMonBanChayHomNay", new Object[]{limit}, () -> dao.getTopMonBanChayHomNay(limit)); }
    public Map<String, Integer> getTopMonBanChayTheoThang(int limit) throws RemoteException { return RemoteCallLogger.log("DashboardRemoteImpl", "getTopMonBanChayTheoThang", new Object[]{limit}, () -> dao.getTopMonBanChayTheoThang(limit)); }
    public int getSoKhachPhucVuHomNay() throws RemoteException { return RemoteCallLogger.log("DashboardRemoteImpl", "getSoKhachPhucVuHomNay", new Object[]{}, () -> dao.getSoKhachPhucVuHomNay()); }
    public int getSoKhachPhucVuHomQua() throws RemoteException { return RemoteCallLogger.log("DashboardRemoteImpl", "getSoKhachPhucVuHomQua", new Object[]{}, () -> dao.getSoKhachPhucVuHomQua()); }
    public Map<String, Double> getDoanhThuTheoTuan() throws RemoteException { return RemoteCallLogger.log("DashboardRemoteImpl", "getDoanhThuTheoTuan", new Object[]{}, () -> dao.getDoanhThuTheoTuan()); }
    public Map<String, Integer> getTopMonBanChay(int limit) throws RemoteException { return RemoteCallLogger.log("DashboardRemoteImpl", "getTopMonBanChay", new Object[]{limit}, () -> dao.getTopMonBanChay(limit)); }
    public String getMonBanChayNhatHomNay() throws RemoteException { return RemoteCallLogger.log("DashboardRemoteImpl", "getMonBanChayNhatHomNay", new Object[]{}, () -> dao.getMonBanChayNhatHomNay()); }
    public Map<String, Integer> getLuongKhachTheoKhungGio() throws RemoteException { return RemoteCallLogger.log("DashboardRemoteImpl", "getLuongKhachTheoKhungGio", new Object[]{}, () -> dao.getLuongKhachTheoKhungGio()); }
    public Map<String, Integer> getLuongKhachTheoKhungGio(LocalDate ngay) throws RemoteException { return RemoteCallLogger.log("DashboardRemoteImpl", "getLuongKhachTheoKhungGio", new Object[]{ngay}, () -> dao.getLuongKhachTheoKhungGio(ngay)); }
    public int getSoKhachMoi() throws RemoteException { return RemoteCallLogger.log("DashboardRemoteImpl", "getSoKhachMoi", new Object[]{}, () -> dao.getSoKhachMoi()); }
    public int getSoKhachQuayLai() throws RemoteException { return RemoteCallLogger.log("DashboardRemoteImpl", "getSoKhachQuayLai", new Object[]{}, () -> dao.getSoKhachQuayLai()); }
    public String tinhPhanTramThayDoi(double homNay, double homQua) throws RemoteException { return RemoteCallLogger.log("DashboardRemoteImpl", "tinhPhanTramThayDoi", new Object[]{homNay, homQua}, () -> dao.tinhPhanTramThayDoi(homNay, homQua)); }
}
