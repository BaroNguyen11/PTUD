package server.service.impl;

import server.core.RemoteCallLogger;
import common.BanAnRemote;
import server.dao.BanAn_DAO;
import common.entity.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class BanAnRemoteImpl extends UnicastRemoteObject implements BanAnRemote {
    private final BanAn_DAO dao = new BanAn_DAO();

    public BanAnRemoteImpl() throws RemoteException {
    }

    public BanAn getByMaBan(String maBan) throws RemoteException { return RemoteCallLogger.log("BanAnRemoteImpl", "getByMaBan", new Object[]{maBan}, () -> BanAn_DAO.getByMaBan(maBan)); }
    public List<BanAn> getAllBanAn() throws RemoteException { return RemoteCallLogger.log("BanAnRemoteImpl", "getAllBanAn", new Object[]{}, () -> dao.getAllBanAn()); }
    public List<BanAn> getBanAnTheoViTri(ViTri viTri) throws RemoteException { return RemoteCallLogger.log("BanAnRemoteImpl", "getBanAnTheoViTri", new Object[]{viTri}, () -> dao.getBanAnTheoViTri(viTri)); }
    public List<BanAn> getTrangThaiBanTheoNgayVaViTri(ViTri viTri, LocalDate ngay) throws RemoteException { return RemoteCallLogger.log("BanAnRemoteImpl", "getTrangThaiBanTheoNgayVaViTri", new Object[]{viTri, ngay}, () -> dao.getTrangThaiBanTheoNgayVaViTri(viTri, ngay)); }
    public Map<String, PhieuDatBan> getPhieuDatBanMapByNgay(LocalDate ngay) throws RemoteException { return RemoteCallLogger.log("BanAnRemoteImpl", "getPhieuDatBanMapByNgay", new Object[]{ngay}, () -> dao.getPhieuDatBanMapByNgay(ngay)); }
    public boolean updateTrangThaiBan(BanAn ban, TrangThai trangThaiMoi) throws RemoteException { return RemoteCallLogger.log("BanAnRemoteImpl", "updateTrangThaiBan", new Object[]{ban, trangThaiMoi}, () -> dao.updateTrangThaiBan(ban, trangThaiMoi)); }
    public boolean isBanDangSuDungHienTai(String maBan) throws RemoteException { return RemoteCallLogger.log("BanAnRemoteImpl", "isBanDangSuDungHienTai", new Object[]{maBan}, () -> dao.isBanDangSuDungHienTai(maBan)); }
    public List<String> getDanhSachBanCungHoaDon(String maHoaDon) throws RemoteException { return RemoteCallLogger.log("BanAnRemoteImpl", "getDanhSachBanCungHoaDon", new Object[]{maHoaDon}, () -> dao.getDanhSachBanCungHoaDon(maHoaDon)); }
    public String getMaHoaDonTuBan(String maBan) throws RemoteException { return RemoteCallLogger.log("BanAnRemoteImpl", "getMaHoaDonTuBan", new Object[]{maBan}, () -> dao.getMaHoaDonTuBan(maBan)); }
}
