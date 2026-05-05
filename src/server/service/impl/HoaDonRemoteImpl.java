package server.service.impl;

import server.core.RemoteCallLogger;
import common.HoaDonRemote;
import server.dao.HoaDon_DAO;
import common.entity.HoaDon;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;

public class HoaDonRemoteImpl extends UnicastRemoteObject implements HoaDonRemote {
    private final HoaDon_DAO dao = new HoaDon_DAO();

    public HoaDonRemoteImpl() throws RemoteException {
    }

    public String themHoaDon(HoaDon hd) throws RemoteException { return RemoteCallLogger.log("HoaDonRemoteImpl", "themHoaDon", new Object[]{hd}, () -> dao.themHoaDon(hd)); }
    public String getMaHoaDonCuoiCung() throws RemoteException { return RemoteCallLogger.log("HoaDonRemoteImpl", "getMaHoaDonCuoiCung", new Object[]{}, () -> dao.getMaHoaDonCuoiCung()); }
    public String taoMaHoaDonMoi(LocalDate ngayLap) throws RemoteException { return RemoteCallLogger.log("HoaDonRemoteImpl", "taoMaHoaDonMoi", new Object[]{ngayLap}, () -> dao.taoMaHoaDonMoi(ngayLap)); }
    public String getMaHoaDonCuoiCungTheoNgay(LocalDate ngay) throws RemoteException { return RemoteCallLogger.log("HoaDonRemoteImpl", "getMaHoaDonCuoiCungTheoNgay", new Object[]{ngay}, () -> dao.getMaHoaDonCuoiCungTheoNgay(ngay)); }
}
