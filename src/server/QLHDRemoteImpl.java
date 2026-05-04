package server;

import common.QLHDRemote;
import dao.QLHD_DAO;
import common.entity.ChiTietHoaDon;
import common.entity.HoaDon;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class QLHDRemoteImpl extends UnicastRemoteObject implements QLHDRemote {
    private final QLHD_DAO dao = new QLHD_DAO();

    public QLHDRemoteImpl() throws RemoteException {
    }

    public List<HoaDon> getAllHoaDon() throws RemoteException { return RemoteCallLogger.log("QLHDRemoteImpl", "getAllHoaDon", new Object[]{}, () -> dao.getAllHoaDon()); }
    public boolean insertHoaDon(HoaDon hd, String maNhanVien, String maKhachHang) throws RemoteException { return RemoteCallLogger.log("QLHDRemoteImpl", "insertHoaDon", new Object[]{hd, maNhanVien, maKhachHang}, () -> dao.insertHoaDon(hd, maNhanVien, maKhachHang)); }
    public HoaDon getHoaDonById(String maHoaDon) throws RemoteException { return RemoteCallLogger.log("QLHDRemoteImpl", "getHoaDonById", new Object[]{maHoaDon}, () -> dao.getHoaDonById(maHoaDon)); }
    public List<String> layDanhSachMaBanTheoHoaDon(String maHoaDon) throws RemoteException { return RemoteCallLogger.log("QLHDRemoteImpl", "layDanhSachMaBanTheoHoaDon", new Object[]{maHoaDon}, () -> dao.layDanhSachMaBanTheoHoaDon(maHoaDon)); }
    public double tinhTienCoc(String maHoaDon) throws RemoteException { return RemoteCallLogger.log("QLHDRemoteImpl", "tinhTienCoc", new Object[]{maHoaDon}, () -> dao.tinhTienCoc(maHoaDon)); }
    public double tinhTongTien(String maHoaDon) throws RemoteException { return RemoteCallLogger.log("QLHDRemoteImpl", "tinhTongTien", new Object[]{maHoaDon}, () -> dao.tinhTongTien(maHoaDon)); }
    public List<ChiTietHoaDon> layDSChiTietTheoMaHoaDon(HoaDon hoaDon) throws RemoteException { return RemoteCallLogger.log("QLHDRemoteImpl", "layDSChiTietTheoMaHoaDon", new Object[]{hoaDon}, () -> dao.layDSChiTietTheoMaHoaDon(hoaDon)); }
    public HoaDon timHoaDonTheoMa(String maHoaDon) throws RemoteException { return RemoteCallLogger.log("QLHDRemoteImpl", "timHoaDonTheoMa", new Object[]{maHoaDon}, () -> dao.timHoaDonTheoMa(maHoaDon)); }
    public List<String> getChiTietHoaDonTheoMa(String maHoaDon) throws RemoteException { return RemoteCallLogger.log("QLHDRemoteImpl", "getChiTietHoaDonTheoMa", new Object[]{maHoaDon}, () -> dao.getChiTietHoaDonTheoMa(maHoaDon)); }
    public List<String> loadDanhSachHoaDon() throws RemoteException { return RemoteCallLogger.log("QLHDRemoteImpl", "loadDanhSachHoaDon", new Object[]{}, () -> dao.loadDanhSachHoaDon()); }
    public String layHoaDonString(String maHoaDon) throws RemoteException { return RemoteCallLogger.log("QLHDRemoteImpl", "layHoaDonString", new Object[]{maHoaDon}, () -> QLHD_DAO.layHoaDonString(maHoaDon)); }
}
