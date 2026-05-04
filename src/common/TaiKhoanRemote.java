package common;

import common.entity.NhanVien;
import common.entity.TaiKhoan;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface TaiKhoanRemote extends Remote {
    List<TaiKhoan> getAllTaiKhoan() throws RemoteException;
    List<TaiKhoan> searchTaiKhoan(String keyword) throws RemoteException;
    List<TaiKhoan> filterTaiKhoanTheoQuyen(boolean isQuanLy) throws RemoteException;
    boolean updateTrangThaiTaiKhoan(String maTaiKhoan, boolean trangThai) throws RemoteException;
    boolean resetMatKhau(String maTaiKhoan, String matKhauMoi) throws RemoteException;
    boolean isTenDangNhapExists(String tenDangNhap) throws RemoteException;
    boolean isNhanVienDaCoTaiKhoan(String maNhanVien) throws RemoteException;
    String generateMaTaiKhoan() throws RemoteException;
    boolean addTaiKhoan(TaiKhoan tk) throws RemoteException;
    boolean updateTaiKhoan(TaiKhoan tk) throws RemoteException;
    TaiKhoan getTaiKhoanByMa(String maTaiKhoan) throws RemoteException;
    List<NhanVien> getNhanVienChuaCoTaiKhoan() throws RemoteException;
    boolean checkTaiKhoanTonTai(String username) throws RemoteException;
    boolean createTaiKhoan(TaiKhoan tk) throws RemoteException;
    String tuDongLayMaMoi() throws RemoteException;
    String getMatKhauByMaNV(String maNV) throws RemoteException;
    boolean updateMatKhau(String maNV, String newPassHash) throws RemoteException;
}
