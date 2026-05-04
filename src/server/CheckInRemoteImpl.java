package server;

import common.CheckInRemote;
import dao.CheckIn_DAO;
import common.entity.PhieuDatBan;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.List;

public class CheckInRemoteImpl extends UnicastRemoteObject implements CheckInRemote {
    private final CheckIn_DAO dao = new CheckIn_DAO();

    public CheckInRemoteImpl() throws RemoteException {
    }

    public boolean capNhatTrangThaiBan(String maBan, String trangThai) throws RemoteException { return RemoteCallLogger.log("CheckInRemoteImpl", "capNhatTrangThaiBan", new Object[]{maBan, trangThai}, () -> dao.capNhatTrangThaiBan(maBan, trangThai)); }
    public PhieuDatBan timPhieuDatBanTheoBanTrongNgay(String maBan) throws RemoteException { return RemoteCallLogger.log("CheckInRemoteImpl", "timPhieuDatBanTheoBanTrongNgay", new Object[]{maBan}, () -> dao.timPhieuDatBanTheoBanTrongNgay(maBan)); }
    public List<PhieuDatBan> getPhieuDatBanTheoHoaDonVaNgay(String maHoaDon, LocalDate ngay) throws RemoteException { return RemoteCallLogger.log("CheckInRemoteImpl", "getPhieuDatBanTheoHoaDonVaNgay", new Object[]{maHoaDon, ngay}, () -> CheckIn_DAO.getPhieuDatBanTheoHoaDonVaNgay(maHoaDon, ngay)); }
    public boolean capNhatTrangThaiPhieuDatBan(String maPhieu, String trangThaiMoi) throws RemoteException { return RemoteCallLogger.log("CheckInRemoteImpl", "capNhatTrangThaiPhieuDatBan", new Object[]{maPhieu, trangThaiMoi}, () -> CheckIn_DAO.capNhatTrangThaiPhieuDatBan(maPhieu, trangThaiMoi)); }
}
