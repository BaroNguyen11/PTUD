package common;

import common.entity.HoaDon;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;

public interface HoaDonRemote extends Remote {
    String themHoaDon(HoaDon hd) throws RemoteException;
    String getMaHoaDonCuoiCung() throws RemoteException;
    String taoMaHoaDonMoi(LocalDate ngayLap) throws RemoteException;
    String getMaHoaDonCuoiCungTheoNgay(LocalDate ngay) throws RemoteException;
}
