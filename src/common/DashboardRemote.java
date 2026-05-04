package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Map;

public interface DashboardRemote extends Remote {
    double getTongDoanhThuTheoNgay(LocalDate ngay) throws RemoteException;
    double getTongDoanhThuHomNay() throws RemoteException;
    double getTongDoanhThuHomQua() throws RemoteException;
    Map<String, Double> getDoanhThuHomNayTheoGio() throws RemoteException;
    Map<String, Double> getDoanhThuTheoThang() throws RemoteException;
    int getLuotDatBanHomNay() throws RemoteException;
    int getLuotDatBanHomQua() throws RemoteException;
    int getSoMonBanRaHomNay() throws RemoteException;
    int getSoMonBanRaHomQua() throws RemoteException;
    Map<String, Integer> getTopMonBanChayHomNay(int limit) throws RemoteException;
    Map<String, Integer> getTopMonBanChayTheoThang(int limit) throws RemoteException;
    int getSoKhachPhucVuHomNay() throws RemoteException;
    int getSoKhachPhucVuHomQua() throws RemoteException;
    Map<String, Double> getDoanhThuTheoTuan() throws RemoteException;
    Map<String, Integer> getTopMonBanChay(int limit) throws RemoteException;
    String getMonBanChayNhatHomNay() throws RemoteException;
    Map<String, Integer> getLuongKhachTheoKhungGio() throws RemoteException;
    Map<String, Integer> getLuongKhachTheoKhungGio(LocalDate ngay) throws RemoteException;
    int getSoKhachMoi() throws RemoteException;
    int getSoKhachQuayLai() throws RemoteException;
    String tinhPhanTramThayDoi(double homNay, double homQua) throws RemoteException;
}
