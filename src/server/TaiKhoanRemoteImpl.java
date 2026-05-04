package server;

import common.TaiKhoanRemote;
import server.dao.TaiKhoan_DAO;
import common.entity.NhanVien;
import common.entity.TaiKhoan;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class TaiKhoanRemoteImpl extends UnicastRemoteObject implements TaiKhoanRemote {
    private final TaiKhoan_DAO dao = new TaiKhoan_DAO();

    public TaiKhoanRemoteImpl() throws RemoteException {
    }

    public List<TaiKhoan> getAllTaiKhoan() throws RemoteException { return RemoteCallLogger.log("TaiKhoanRemoteImpl", "getAllTaiKhoan", new Object[]{}, () -> dao.getAllTaiKhoan()); }
    public List<TaiKhoan> searchTaiKhoan(String keyword) throws RemoteException { return RemoteCallLogger.log("TaiKhoanRemoteImpl", "searchTaiKhoan", new Object[]{keyword}, () -> dao.searchTaiKhoan(keyword)); }
    public List<TaiKhoan> filterTaiKhoanTheoQuyen(boolean isQuanLy) throws RemoteException { return RemoteCallLogger.log("TaiKhoanRemoteImpl", "filterTaiKhoanTheoQuyen", new Object[]{isQuanLy}, () -> dao.filterTaiKhoanTheoQuyen(isQuanLy)); }
    public boolean updateTrangThaiTaiKhoan(String maTaiKhoan, boolean trangThai) throws RemoteException { return RemoteCallLogger.log("TaiKhoanRemoteImpl", "updateTrangThaiTaiKhoan", new Object[]{maTaiKhoan, trangThai}, () -> dao.updateTrangThaiTaiKhoan(maTaiKhoan, trangThai)); }
    public boolean resetMatKhau(String maTaiKhoan, String matKhauMoi) throws RemoteException { return RemoteCallLogger.log("TaiKhoanRemoteImpl", "resetMatKhau", new Object[]{maTaiKhoan, matKhauMoi}, () -> dao.resetMatKhau(maTaiKhoan, matKhauMoi)); }
    public boolean isTenDangNhapExists(String tenDangNhap) throws RemoteException { return RemoteCallLogger.log("TaiKhoanRemoteImpl", "isTenDangNhapExists", new Object[]{tenDangNhap}, () -> dao.isTenDangNhapExists(tenDangNhap)); }
    public boolean isNhanVienDaCoTaiKhoan(String maNhanVien) throws RemoteException { return RemoteCallLogger.log("TaiKhoanRemoteImpl", "isNhanVienDaCoTaiKhoan", new Object[]{maNhanVien}, () -> dao.isNhanVienDaCoTaiKhoan(maNhanVien)); }
    public String generateMaTaiKhoan() throws RemoteException { return RemoteCallLogger.log("TaiKhoanRemoteImpl", "generateMaTaiKhoan", new Object[]{}, () -> dao.generateMaTaiKhoan()); }
    public boolean addTaiKhoan(TaiKhoan tk) throws RemoteException { return RemoteCallLogger.log("TaiKhoanRemoteImpl", "addTaiKhoan", new Object[]{tk}, () -> dao.addTaiKhoan(tk)); }
    public boolean updateTaiKhoan(TaiKhoan tk) throws RemoteException { return RemoteCallLogger.log("TaiKhoanRemoteImpl", "updateTaiKhoan", new Object[]{tk}, () -> dao.updateTaiKhoan(tk)); }
    public TaiKhoan getTaiKhoanByMa(String maTaiKhoan) throws RemoteException { return RemoteCallLogger.log("TaiKhoanRemoteImpl", "getTaiKhoanByMa", new Object[]{maTaiKhoan}, () -> dao.getTaiKhoanByMa(maTaiKhoan)); }
    public List<NhanVien> getNhanVienChuaCoTaiKhoan() throws RemoteException { return RemoteCallLogger.log("TaiKhoanRemoteImpl", "getNhanVienChuaCoTaiKhoan", new Object[]{}, () -> dao.getNhanVienChuaCoTaiKhoan()); }
    public boolean checkTaiKhoanTonTai(String username) throws RemoteException { return RemoteCallLogger.log("TaiKhoanRemoteImpl", "checkTaiKhoanTonTai", new Object[]{username}, () -> dao.checkTaiKhoanTonTai(username)); }
    public boolean createTaiKhoan(TaiKhoan tk) throws RemoteException { return RemoteCallLogger.log("TaiKhoanRemoteImpl", "createTaiKhoan", new Object[]{tk}, () -> dao.createTaiKhoan(tk)); }
    public String tuDongLayMaMoi() throws RemoteException { return RemoteCallLogger.log("TaiKhoanRemoteImpl", "tuDongLayMaMoi", new Object[]{}, () -> dao.tuDongLayMaMoi()); }
    public String getMatKhauByMaNV(String maNV) throws RemoteException { return RemoteCallLogger.log("TaiKhoanRemoteImpl", "getMatKhauByMaNV", new Object[]{maNV}, () -> dao.getMatKhauByMaNV(maNV)); }
    public boolean updateMatKhau(String maNV, String newPassHash) throws RemoteException { return RemoteCallLogger.log("TaiKhoanRemoteImpl", "updateMatKhau", new Object[]{maNV, newPassHash}, () -> dao.updateMatKhau(maNV, newPassHash)); }
}
