package common;

import common.entity.NhanVien;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public interface NhanVienRemote extends Remote {
    List<NhanVien> getAllNhanVien() throws RemoteException;
    NhanVien getNhanVienByMa(String maNhanVien) throws RemoteException;
    List<NhanVien> searchNhanVien(String keyword) throws RemoteException;
    boolean addNhanVien(NhanVien nv) throws RemoteException;
    boolean updateNhanVien(NhanVien nv) throws RemoteException;
    boolean thoiViecNhanVien(String maNhanVien) throws RemoteException;
    boolean taiTuyenNhanVien(String maNhanVien) throws RemoteException;
    boolean isSoDienThoaiExistsForOther(String soDienThoai, String maNhanVien) throws RemoteException;
    boolean isCCCDExistsForOther(String cccd, String maNhanVien) throws RemoteException;
    String generateMaNhanVien() throws RemoteException;
    LocalDate getNgayThoiViec(String maNhanVien) throws RemoteException;
}
