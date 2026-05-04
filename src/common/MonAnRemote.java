package common;

import common.entity.MonAn;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public interface MonAnRemote extends Remote {
    List<MonAn> getAllMonAn() throws RemoteException;
    List<MonAn> getMonAnByLoai(String loaiMon) throws RemoteException;
    double layGiaSauKhuyenMai(String maMonAn, LocalDate ngayDat, double giaMacDinh) throws RemoteException;
    MonAn getMonAnByMa(String maMonAn) throws RemoteException;
    String getMaMonByTen(String tenMon) throws RemoteException;
    List<String> layDanhSachMonAnGiaKMString() throws RemoteException;
    List<MonAn> timKiemMonAn(String tuKhoa) throws RemoteException;
}
