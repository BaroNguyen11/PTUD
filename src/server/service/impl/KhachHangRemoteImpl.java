package server.service.impl;

import server.core.RemoteCallLogger;
import common.KhachHangRemote;
import server.dao.KhachHang_DAO;
import common.entity.KhachHang;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class KhachHangRemoteImpl extends UnicastRemoteObject implements KhachHangRemote {
    private final KhachHang_DAO dao = new KhachHang_DAO();

    public KhachHangRemoteImpl() throws RemoteException {
    }

    public List<KhachHang> getAllKhachHang() throws RemoteException { return RemoteCallLogger.log("KhachHangRemoteImpl", "getAllKhachHang", new Object[]{}, () -> dao.getAllKhachHang()); }
    public KhachHang getKhachHangById(String maKH) throws RemoteException { return RemoteCallLogger.log("KhachHangRemoteImpl", "getKhachHangById", new Object[]{maKH}, () -> KhachHang_DAO.getKhachHangById(maKH)); }
    public KhachHang getKhachHangBySdt(String sdt) throws RemoteException { return RemoteCallLogger.log("KhachHangRemoteImpl", "getKhachHangBySdt", new Object[]{sdt}, () -> dao.getKhachHangBySdt(sdt)); }
    public boolean addKhachHang(KhachHang kh) throws RemoteException { return RemoteCallLogger.log("KhachHangRemoteImpl", "addKhachHang", new Object[]{kh}, () -> dao.addKhachHang(kh)); }
    public String taoMaKhachHangMoi() throws RemoteException { return RemoteCallLogger.log("KhachHangRemoteImpl", "taoMaKhachHangMoi", new Object[]{}, () -> dao.taoMaKhachHangMoi()); }
    public boolean themKhachHangMoi(KhachHang kh) throws RemoteException { return RemoteCallLogger.log("KhachHangRemoteImpl", "themKhachHangMoi", new Object[]{kh}, () -> dao.themKhachHangMoi(kh)); }
    public boolean updateKhachHang(KhachHang kh) throws RemoteException { return RemoteCallLogger.log("KhachHangRemoteImpl", "updateKhachHang", new Object[]{kh}, () -> dao.updateKhachHang(kh)); }
    public boolean isSoDienThoaiExists(String soDienThoai) throws RemoteException { return RemoteCallLogger.log("KhachHangRemoteImpl", "isSoDienThoaiExists", new Object[]{soDienThoai}, () -> dao.isSoDienThoaiExists(soDienThoai)); }
    public boolean isSoDienThoaiExistsForOther(String soDienThoai, String maKhachHang) throws RemoteException { return RemoteCallLogger.log("KhachHangRemoteImpl", "isSoDienThoaiExistsForOther", new Object[]{soDienThoai, maKhachHang}, () -> dao.isSoDienThoaiExistsForOther(soDienThoai, maKhachHang)); }
    public String generateMaKhachHang() throws RemoteException { return RemoteCallLogger.log("KhachHangRemoteImpl", "generateMaKhachHang", new Object[]{}, () -> dao.generateMaKhachHang()); }
    public List<KhachHang> searchKhachHang(String keyword) throws RemoteException { return RemoteCallLogger.log("KhachHangRemoteImpl", "searchKhachHang", new Object[]{keyword}, () -> dao.searchKhachHang(keyword)); }
}
