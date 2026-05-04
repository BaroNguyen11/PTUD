package server;

import common.ChiTietHoaDonRemote;
import dao.ChiTietHoaDon_DAO;
import common.entity.ChiTietHoaDon;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ChiTietHoaDonRemoteImpl extends UnicastRemoteObject implements ChiTietHoaDonRemote {
    private final ChiTietHoaDon_DAO dao = new ChiTietHoaDon_DAO();

    public ChiTietHoaDonRemoteImpl() throws RemoteException {
    }

    public boolean themChiTietHoaDon(ChiTietHoaDon cthd) throws RemoteException { return RemoteCallLogger.log("ChiTietHoaDonRemoteImpl", "themChiTietHoaDon", new Object[]{cthd}, () -> dao.themChiTietHoaDon(cthd)); }
    public List<ChiTietHoaDon> getChiTietHoaDonByMaHD(String maHD) throws RemoteException { return RemoteCallLogger.log("ChiTietHoaDonRemoteImpl", "getChiTietHoaDonByMaHD", new Object[]{maHD}, () -> ChiTietHoaDon_DAO.getChiTietHoaDonByMaHD(maHD)); }
    public boolean themHoacUpdate(String maHD, String maMon, int soLuongThem, double giaBan) throws RemoteException { return RemoteCallLogger.log("ChiTietHoaDonRemoteImpl", "themHoacUpdate", new Object[]{maHD, maMon, soLuongThem, giaBan}, () -> dao.themHoacUpdate(maHD, maMon, soLuongThem, giaBan)); }
}
