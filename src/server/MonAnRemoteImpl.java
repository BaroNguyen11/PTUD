package server;

import common.MonAnRemote;
import server.dao.MonAn_DAO;
import common.entity.MonAn;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.List;

public class MonAnRemoteImpl extends UnicastRemoteObject implements MonAnRemote {
    private final MonAn_DAO dao = new MonAn_DAO();

    public MonAnRemoteImpl() throws RemoteException {
    }

    public List<MonAn> getAllMonAn() throws RemoteException { return RemoteCallLogger.log("MonAnRemoteImpl", "getAllMonAn", new Object[]{}, () -> dao.getAllMonAn()); }
    public List<MonAn> getMonAnByLoai(String loaiMon) throws RemoteException { return RemoteCallLogger.log("MonAnRemoteImpl", "getMonAnByLoai", new Object[]{loaiMon}, () -> dao.getMonAnByLoai(loaiMon)); }
    public double layGiaSauKhuyenMai(String maMonAn, LocalDate ngayDat, double giaMacDinh) throws RemoteException { return RemoteCallLogger.log("MonAnRemoteImpl", "layGiaSauKhuyenMai", new Object[]{maMonAn, ngayDat, giaMacDinh}, () -> dao.layGiaSauKhuyenMai(maMonAn, ngayDat, giaMacDinh)); }
    public MonAn getMonAnByMa(String maMonAn) throws RemoteException { return RemoteCallLogger.log("MonAnRemoteImpl", "getMonAnByMa", new Object[]{maMonAn}, () -> MonAn_DAO.getMonAnByMa(maMonAn)); }
    public String getMaMonByTen(String tenMon) throws RemoteException { return RemoteCallLogger.log("MonAnRemoteImpl", "getMaMonByTen", new Object[]{tenMon}, () -> MonAn_DAO.getMaMonByTen(tenMon)); }
    public List<String> layDanhSachMonAnGiaKMString() throws RemoteException { return RemoteCallLogger.log("MonAnRemoteImpl", "layDanhSachMonAnGiaKMString", new Object[]{}, () -> dao.layDanhSachMonAnGiaKMString()); }
    public List<MonAn> timKiemMonAn(String tuKhoa) throws RemoteException { return RemoteCallLogger.log("MonAnRemoteImpl", "timKiemMonAn", new Object[]{tuKhoa}, () -> dao.timKiemMonAn(tuKhoa)); }
}
