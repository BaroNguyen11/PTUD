package common;

import common.entity.KhachHang;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface KhachHangRemote extends Remote {
    List<KhachHang> getAllKhachHang() throws RemoteException;
    KhachHang getKhachHangById(String maKH) throws RemoteException;
    KhachHang getKhachHangBySdt(String sdt) throws RemoteException;
    boolean addKhachHang(KhachHang kh) throws RemoteException;
    String taoMaKhachHangMoi() throws RemoteException;
    boolean themKhachHangMoi(KhachHang kh) throws RemoteException;
    boolean updateKhachHang(KhachHang kh) throws RemoteException;
    boolean isSoDienThoaiExists(String soDienThoai) throws RemoteException;
    boolean isSoDienThoaiExistsForOther(String soDienThoai, String maKhachHang) throws RemoteException;
    String generateMaKhachHang() throws RemoteException;
    List<KhachHang> searchKhachHang(String keyword) throws RemoteException;
}
