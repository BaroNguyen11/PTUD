package common;

import common.entity.PhieuDatBan;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PhieuDatBanRemote extends Remote {
    boolean themPhieuDatBan(PhieuDatBan pdb, String trangThaiPhieu) throws RemoteException;
    String getMaPhieuCuoiCung(LocalDate ngayCanTim) throws RemoteException;
    String taoMaPhieuMoi(LocalDate ngayDat) throws RemoteException;
    boolean kiemTraBanDaDatTrongNgay(String maBan, LocalDateTime thoiGianBatDau) throws RemoteException;
    boolean kiemTraBanDaDatTrongNgay(String maBan, LocalDateTime thoiGianBatDau, LocalDateTime thoiGianKetThuc) throws RemoteException;
    PhieuDatBan getPhieuDatBanByMaBanVaNgay(String maBan, LocalDate ngayDat) throws RemoteException;
    List<PhieuDatBan> getDanhSachPhieuDatBanByMaBanVaNgay(String maBan, LocalDate ngayDat) throws RemoteException;
    boolean huyPhieuDatBanByMaBanVaNgay(String maBan, LocalDate ngay) throws RemoteException;
    boolean huyTatCaPhieuByMaHoaDon(String maHoaDon) throws RemoteException;
    List<PhieuDatBan> getByMaHoaDon(String maHoaDon) throws RemoteException;
    PhieuDatBan timMotPhieuBangMaHD(String maHoaDon) throws RemoteException;
    boolean chuyenBanNhieuSangNhieu(List<String> dsMaBanCu, List<String> dsMaBanMoi, String maHoaDon, String trangThaiMoi, LocalDate ngayChuyen) throws RemoteException;
    PhieuDatBan getPhieuDatBanMoiNhat(String maBan) throws RemoteException;
    int demSoBanDangSuDungCuaHoaDon(String maHoaDon) throws RemoteException;
}
