package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ThongKeRemote extends Remote {
    Map<String, Double> getDoanhThuTheoThang(int nam) throws RemoteException;
    double getTongDoanhThu() throws RemoteException;
    double getDoanhThuThangTruoc() throws RemoteException;
    double getDoanhThuThangNay() throws RemoteException;
    double getDoanhThuTheoKhoangTG(LocalDate from, LocalDate to) throws RemoteException;
    int[] getThongKeKhachHang(LocalDate from, LocalDate to) throws RemoteException;
    List<String[]> getTopKhachHang(LocalDate from, LocalDate to) throws RemoteException;
    Map<String, Integer> getTopMonAnBanChay(LocalDate from, LocalDate to) throws RemoteException;
    Map<String, Double> getDoanhThuTheoNhomMon(LocalDate from, LocalDate to) throws RemoteException;
    double getDoanhThuTrungBinhBan() throws RemoteException;
    double getTiLeTienMat() throws RemoteException;
    double getDoanhThuCaToi() throws RemoteException;
    List<String[]> getTopKhachHang() throws RemoteException;
    int[] getThongKeKhachHang() throws RemoteException;
    double getChiTieuTrungBinh() throws RemoteException;
    double getTanSuatTrungBinh() throws RemoteException;
    Map<String, Double> getDoanhThuTheoCa() throws RemoteException;
    Map<String, Integer> getTopMonAnBanChay() throws RemoteException;
    Map<String, Double> getDoanhThuTheoNhomMon() throws RemoteException;
    String[] getMonBanChayNhat() throws RemoteException;
    String[] getMonDoanhThuCaoNhat() throws RemoteException;
    double[] getThongKeDoUong() throws RemoteException;
    int getTongLuotDatBan() throws RemoteException;
    double getTyLeLapDay() throws RemoteException;
    double getTyLeHuyDat() throws RemoteException;
    double getThoiGianSuDungTB() throws RemoteException;
    Map<String, Double> getHieuSuatKhuVuc() throws RemoteException;
}
