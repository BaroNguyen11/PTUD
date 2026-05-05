package server.service.impl;

import server.core.RemoteCallLogger;
import common.PhieuDatBanRemote;
import server.dao.PhieuDatBan_DAO;
import common.entity.PhieuDatBan;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PhieuDatBanRemoteImpl extends UnicastRemoteObject implements PhieuDatBanRemote {
    private final PhieuDatBan_DAO dao = new PhieuDatBan_DAO();

    public PhieuDatBanRemoteImpl() throws RemoteException {
    }

    public boolean themPhieuDatBan(PhieuDatBan pdb, String trangThaiPhieu) throws RemoteException { return RemoteCallLogger.log("PhieuDatBanRemoteImpl", "themPhieuDatBan", new Object[]{pdb, trangThaiPhieu}, () -> dao.themPhieuDatBan(pdb, trangThaiPhieu)); }
    public String getMaPhieuCuoiCung(LocalDate ngayCanTim) throws RemoteException { return RemoteCallLogger.log("PhieuDatBanRemoteImpl", "getMaPhieuCuoiCung", new Object[]{ngayCanTim}, () -> dao.getMaPhieuCuoiCung(ngayCanTim)); }
    public String taoMaPhieuMoi(LocalDate ngayDat) throws RemoteException { return RemoteCallLogger.log("PhieuDatBanRemoteImpl", "taoMaPhieuMoi", new Object[]{ngayDat}, () -> dao.taoMaPhieuMoi(ngayDat)); }
    public boolean kiemTraBanDaDatTrongNgay(String maBan, LocalDateTime thoiGianBatDau) throws RemoteException { return RemoteCallLogger.log("PhieuDatBanRemoteImpl", "kiemTraBanDaDatTrongNgay", new Object[]{maBan, thoiGianBatDau}, () -> dao.kiemTraBanDaDatTrongNgay(maBan, thoiGianBatDau)); }
    public boolean kiemTraBanDaDatTrongNgay(String maBan, LocalDateTime thoiGianBatDau, LocalDateTime thoiGianKetThuc) throws RemoteException { return RemoteCallLogger.log("PhieuDatBanRemoteImpl", "kiemTraBanDaDatTrongNgay3", new Object[]{maBan, thoiGianBatDau, thoiGianKetThuc}, () -> dao.kiemTraBanDaDatTrongNgay(maBan, thoiGianBatDau, thoiGianKetThuc)); }
    public PhieuDatBan getPhieuDatBanByMaBanVaNgay(String maBan, LocalDate ngayDat) throws RemoteException { return RemoteCallLogger.log("PhieuDatBanRemoteImpl", "getPhieuDatBanByMaBanVaNgay", new Object[]{maBan, ngayDat}, () -> dao.getPhieuDatBanByMaBanVaNgay(maBan, ngayDat)); }
    public List<PhieuDatBan> getDanhSachPhieuDatBanByMaBanVaNgay(String maBan, LocalDate ngayDat) throws RemoteException { return RemoteCallLogger.log("PhieuDatBanRemoteImpl", "getDanhSachPhieuDatBanByMaBanVaNgay", new Object[]{maBan, ngayDat}, () -> dao.getDanhSachPhieuDatBanByMaBanVaNgay(maBan, ngayDat)); }
    public boolean huyPhieuDatBanByMaBanVaNgay(String maBan, LocalDate ngay) throws RemoteException { return RemoteCallLogger.log("PhieuDatBanRemoteImpl", "huyPhieuDatBanByMaBanVaNgay", new Object[]{maBan, ngay}, () -> dao.huyPhieuDatBanByMaBanVaNgay(maBan, ngay)); }
    public boolean huyTatCaPhieuByMaHoaDon(String maHoaDon) throws RemoteException { return RemoteCallLogger.log("PhieuDatBanRemoteImpl", "huyTatCaPhieuByMaHoaDon", new Object[]{maHoaDon}, () -> dao.huyTatCaPhieuByMaHoaDon(maHoaDon)); }
    public List<PhieuDatBan> getByMaHoaDon(String maHoaDon) throws RemoteException { return RemoteCallLogger.log("PhieuDatBanRemoteImpl", "getByMaHoaDon", new Object[]{maHoaDon}, () -> PhieuDatBan_DAO.getByMaHoaDon(maHoaDon)); }
    public PhieuDatBan timMotPhieuBangMaHD(String maHoaDon) throws RemoteException { return RemoteCallLogger.log("PhieuDatBanRemoteImpl", "timMotPhieuBangMaHD", new Object[]{maHoaDon}, () -> PhieuDatBan_DAO.timMotPhieuBangMaHD(maHoaDon)); }
    public boolean chuyenBanNhieuSangNhieu(List<String> dsMaBanCu, List<String> dsMaBanMoi, String maHoaDon, String trangThaiMoi, LocalDate ngayChuyen) throws RemoteException { return RemoteCallLogger.log("PhieuDatBanRemoteImpl", "chuyenBanNhieuSangNhieu", new Object[]{dsMaBanCu, dsMaBanMoi, maHoaDon, trangThaiMoi, ngayChuyen}, () -> dao.chuyenBanNhieuSangNhieu(dsMaBanCu, dsMaBanMoi, maHoaDon, trangThaiMoi, ngayChuyen)); }
    public PhieuDatBan getPhieuDatBanMoiNhat(String maBan) throws RemoteException { return RemoteCallLogger.log("PhieuDatBanRemoteImpl", "getPhieuDatBanMoiNhat", new Object[]{maBan}, () -> dao.getPhieuDatBanMoiNhat(maBan)); }
    public int demSoBanDangSuDungCuaHoaDon(String maHoaDon) throws RemoteException { return RemoteCallLogger.log("PhieuDatBanRemoteImpl", "demSoBanDangSuDungCuaHoaDon", new Object[]{maHoaDon}, () -> dao.demSoBanDangSuDungCuaHoaDon(maHoaDon)); }
}
