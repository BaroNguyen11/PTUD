package common;

import common.entity.Ca;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;

public interface CaRemote extends Remote {
    boolean batDauCa(double tongTienDauCa, String maNhanVien) throws RemoteException;
    boolean ketCa(String maCa, double tongTienCuoiCa) throws RemoteException;
    Ca getCaDangMo() throws RemoteException;
    Ca getCaDangLam(String maNhanVien) throws RemoteException;
    double tinhTongTienMat(String maNhanVien, LocalDateTime thoiGianVaoCa) throws RemoteException;
    double tinhTongTienGiamGia(String maNhanVien, LocalDateTime thoiGianVaoCa) throws RemoteException;
    int demDonDangPhucVu(String maNhanVien, LocalDateTime thoiGianVaoCa) throws RemoteException;
    int demSoHoaDonTrongCa(String maNhanVien, LocalDateTime thoiGianVaoCa) throws RemoteException;
}
