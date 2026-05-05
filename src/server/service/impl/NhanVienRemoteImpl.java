package server.service.impl;

import server.core.RemoteCallLogger;
import common.NhanVienRemote;
import server.dao.NhanVien_DAO;
import common.entity.NhanVien;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.List;

public class NhanVienRemoteImpl extends UnicastRemoteObject implements NhanVienRemote {
    private final NhanVien_DAO dao = new NhanVien_DAO();

    public NhanVienRemoteImpl() throws RemoteException {
    }

    public List<NhanVien> getAllNhanVien() throws RemoteException { return RemoteCallLogger.log("NhanVienRemoteImpl", "getAllNhanVien", new Object[]{}, () -> dao.getAllNhanVien()); }
    public NhanVien getNhanVienByMa(String maNhanVien) throws RemoteException { return RemoteCallLogger.log("NhanVienRemoteImpl", "getNhanVienByMa", new Object[]{maNhanVien}, () -> NhanVien_DAO.getNhanVienByMa(maNhanVien)); }
    public List<NhanVien> searchNhanVien(String keyword) throws RemoteException { return RemoteCallLogger.log("NhanVienRemoteImpl", "searchNhanVien", new Object[]{keyword}, () -> dao.searchNhanVien(keyword)); }
    public boolean addNhanVien(NhanVien nv) throws RemoteException { return RemoteCallLogger.log("NhanVienRemoteImpl", "addNhanVien", new Object[]{nv}, () -> dao.addNhanVien(nv)); }
    public boolean updateNhanVien(NhanVien nv) throws RemoteException { return RemoteCallLogger.log("NhanVienRemoteImpl", "updateNhanVien", new Object[]{nv}, () -> dao.updateNhanVien(nv)); }
    public boolean thoiViecNhanVien(String maNhanVien) throws RemoteException { return RemoteCallLogger.log("NhanVienRemoteImpl", "thoiViecNhanVien", new Object[]{maNhanVien}, () -> dao.thoiViecNhanVien(maNhanVien)); }
    public boolean taiTuyenNhanVien(String maNhanVien) throws RemoteException { return RemoteCallLogger.log("NhanVienRemoteImpl", "taiTuyenNhanVien", new Object[]{maNhanVien}, () -> dao.taiTuyenNhanVien(maNhanVien)); }
    public boolean isSoDienThoaiExistsForOther(String soDienThoai, String maNhanVien) throws RemoteException { return RemoteCallLogger.log("NhanVienRemoteImpl", "isSoDienThoaiExistsForOther", new Object[]{soDienThoai, maNhanVien}, () -> dao.isSoDienThoaiExistsForOther(soDienThoai, maNhanVien)); }
    public boolean isCCCDExistsForOther(String cccd, String maNhanVien) throws RemoteException { return RemoteCallLogger.log("NhanVienRemoteImpl", "isCCCDExistsForOther", new Object[]{cccd, maNhanVien}, () -> dao.isCCCDExistsForOther(cccd, maNhanVien)); }
    public String generateMaNhanVien() throws RemoteException { return RemoteCallLogger.log("NhanVienRemoteImpl", "generateMaNhanVien", new Object[]{}, () -> dao.generateMaNhanVien()); }
    public LocalDate getNgayThoiViec(String maNhanVien) throws RemoteException { return RemoteCallLogger.log("NhanVienRemoteImpl", "getNgayThoiViec", new Object[]{maNhanVien}, () -> dao.getNgayThoiViec(maNhanVien)); }
}
