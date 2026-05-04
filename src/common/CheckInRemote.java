package common;

import common.entity.PhieuDatBan;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public interface CheckInRemote extends Remote {
    boolean capNhatTrangThaiBan(String maBan, String trangThai) throws RemoteException;
    PhieuDatBan timPhieuDatBanTheoBanTrongNgay(String maBan) throws RemoteException;
    List<PhieuDatBan> getPhieuDatBanTheoHoaDonVaNgay(String maHoaDon, LocalDate ngay) throws RemoteException;
    boolean capNhatTrangThaiPhieuDatBan(String maPhieu, String trangThaiMoi) throws RemoteException;
}
