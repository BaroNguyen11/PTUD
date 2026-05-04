package common;

import common.entity.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public interface ThanhToanRemote extends Remote {
    List<BanAn> getDanhSachBanDangSuDungTheoNgay(LocalDate ngay) throws RemoteException;
    HoaDon getHoaDonTheoMaBan(String maBan) throws RemoteException;
    NhanVien getNhanVienByMa(String maNhanVien) throws RemoteException;
    KhachHang getKhachHangByMa(String maKhachHang) throws RemoteException;
    List<String> getChiTietHoaDonTheoMa(String maHoaDon) throws RemoteException;
    PhieuDatBan timPhieuDatTheoMaBan(String maBan) throws RemoteException;
    double getSoTienGiamCaoNhatTheoHoaDon(String maHoaDon) throws RemoteException;
    boolean capNhatTrangThaiThanhToan(String maHoaDon, String phuongThuc) throws RemoteException;
    boolean capNhatTrangThaiHoanTat(String maPhieu) throws RemoteException;
    boolean capNhatTrangThaiTrong(String maBan) throws RemoteException;
    List<KhuyenMai> getKhuyenMaiApDungChoHoaDon(String maHoaDon, double tongTien) throws RemoteException;
    boolean taoChiTietKMHD(String maHoaDon, String maKhuyenMai, double soTienGiam) throws RemoteException;
    HoaDon getByMaHoaDon(String maHoaDon) throws RemoteException;
    boolean updateDiemTichLuy(String maKhachHang, double diemMoi) throws RemoteException;
}
