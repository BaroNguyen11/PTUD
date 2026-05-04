package common;

import common.entity.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface BanAnRemote extends Remote {
    BanAn getByMaBan(String maBan) throws RemoteException;
    List<BanAn> getAllBanAn() throws RemoteException;
    List<BanAn> getBanAnTheoViTri(ViTri viTri) throws RemoteException;
    List<BanAn> getTrangThaiBanTheoNgayVaViTri(ViTri viTri, LocalDate ngay) throws RemoteException;
    Map<String, PhieuDatBan> getPhieuDatBanMapByNgay(LocalDate ngay) throws RemoteException;
    boolean updateTrangThaiBan(BanAn ban, TrangThai trangThaiMoi) throws RemoteException;
    boolean isBanDangSuDungHienTai(String maBan) throws RemoteException;
    List<String> getDanhSachBanCungHoaDon(String maHoaDon) throws RemoteException;
    String getMaHoaDonTuBan(String maBan) throws RemoteException;
}
