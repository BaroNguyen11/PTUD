package common;

import common.entity.ChiTietHoaDon;
import common.entity.HoaDon;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface QLHDRemote extends Remote {
    List<HoaDon> getAllHoaDon() throws RemoteException;
    boolean insertHoaDon(HoaDon hd, String maNhanVien, String maKhachHang) throws RemoteException;
    HoaDon getHoaDonById(String maHoaDon) throws RemoteException;
    List<String> layDanhSachMaBanTheoHoaDon(String maHoaDon) throws RemoteException;
    double tinhTienCoc(String maHoaDon) throws RemoteException;
    double tinhTongTien(String maHoaDon) throws RemoteException;
    List<ChiTietHoaDon> layDSChiTietTheoMaHoaDon(HoaDon hoaDon) throws RemoteException;
    HoaDon timHoaDonTheoMa(String maHoaDon) throws RemoteException;
    List<String> getChiTietHoaDonTheoMa(String maHoaDon) throws RemoteException;
    List<String> loadDanhSachHoaDon() throws RemoteException;
    String layHoaDonString(String maHoaDon) throws RemoteException;
}
