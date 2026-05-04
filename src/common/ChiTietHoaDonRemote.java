package common;

import common.entity.ChiTietHoaDon;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChiTietHoaDonRemote extends Remote {
    boolean themChiTietHoaDon(ChiTietHoaDon cthd) throws RemoteException;
    List<ChiTietHoaDon> getChiTietHoaDonByMaHD(String maHD) throws RemoteException;
    boolean themHoacUpdate(String maHD, String maMon, int soLuongThem, double giaBan) throws RemoteException;
}
