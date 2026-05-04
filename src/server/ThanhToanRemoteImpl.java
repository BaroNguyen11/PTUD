package server;

import common.ThanhToanRemote;
import dao.ThanhToan_DAO;
import common.entity.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.List;

public class ThanhToanRemoteImpl extends UnicastRemoteObject implements ThanhToanRemote {
    private final ThanhToan_DAO dao = new ThanhToan_DAO();

    public ThanhToanRemoteImpl() throws RemoteException {
    }

    public List<BanAn> getDanhSachBanDangSuDungTheoNgay(LocalDate ngay) throws RemoteException { return RemoteCallLogger.log("ThanhToanRemoteImpl", "getDanhSachBanDangSuDungTheoNgay", new Object[]{ngay}, () -> dao.getDanhSachBanDangSuDungTheoNgay(ngay)); }
    public HoaDon getHoaDonTheoMaBan(String maBan) throws RemoteException { return RemoteCallLogger.log("ThanhToanRemoteImpl", "getHoaDonTheoMaBan", new Object[]{maBan}, () -> dao.getHoaDonTheoMaBan(maBan)); }
    public NhanVien getNhanVienByMa(String maNhanVien) throws RemoteException { return RemoteCallLogger.log("ThanhToanRemoteImpl", "getNhanVienByMa", new Object[]{maNhanVien}, () -> dao.getNhanVienByMa(maNhanVien)); }
    public KhachHang getKhachHangByMa(String maKhachHang) throws RemoteException { return RemoteCallLogger.log("ThanhToanRemoteImpl", "getKhachHangByMa", new Object[]{maKhachHang}, () -> dao.getKhachHangByMa(maKhachHang)); }
    public List<String> getChiTietHoaDonTheoMa(String maHoaDon) throws RemoteException { return RemoteCallLogger.log("ThanhToanRemoteImpl", "getChiTietHoaDonTheoMa", new Object[]{maHoaDon}, () -> dao.getChiTietHoaDonTheoMa(maHoaDon)); }
    public PhieuDatBan timPhieuDatTheoMaBan(String maBan) throws RemoteException { return RemoteCallLogger.log("ThanhToanRemoteImpl", "timPhieuDatTheoMaBan", new Object[]{maBan}, () -> dao.timPhieuDatTheoMaBan(maBan)); }
    public double getSoTienGiamCaoNhatTheoHoaDon(String maHoaDon) throws RemoteException { return RemoteCallLogger.log("ThanhToanRemoteImpl", "getSoTienGiamCaoNhatTheoHoaDon", new Object[]{maHoaDon}, () -> dao.getSoTienGiamCaoNhatTheoHoaDon(maHoaDon)); }
    public boolean capNhatTrangThaiThanhToan(String maHoaDon, String phuongThuc) throws RemoteException { return RemoteCallLogger.log("ThanhToanRemoteImpl", "capNhatTrangThaiThanhToan", new Object[]{maHoaDon, phuongThuc}, () -> dao.capNhatTrangThaiThanhToan(maHoaDon, phuongThuc)); }
    public boolean capNhatTrangThaiHoanTat(String maPhieu) throws RemoteException { return RemoteCallLogger.log("ThanhToanRemoteImpl", "capNhatTrangThaiHoanTat", new Object[]{maPhieu}, () -> dao.capNhatTrangThaiHoanTat(maPhieu)); }
    public boolean capNhatTrangThaiTrong(String maBan) throws RemoteException { return RemoteCallLogger.log("ThanhToanRemoteImpl", "capNhatTrangThaiTrong", new Object[]{maBan}, () -> dao.capNhatTrangThaiTrong(maBan)); }
    public List<KhuyenMai> getKhuyenMaiApDungChoHoaDon(String maHoaDon, double tongTien) throws RemoteException { return RemoteCallLogger.log("ThanhToanRemoteImpl", "getKhuyenMaiApDungChoHoaDon", new Object[]{maHoaDon, tongTien}, () -> dao.getKhuyenMaiApDungChoHoaDon(maHoaDon, tongTien)); }
    public boolean taoChiTietKMHD(String maHoaDon, String maKhuyenMai, double soTienGiam) throws RemoteException { return RemoteCallLogger.log("ThanhToanRemoteImpl", "taoChiTietKMHD", new Object[]{maHoaDon, maKhuyenMai, soTienGiam}, () -> dao.taoChiTietKMHD(maHoaDon, maKhuyenMai, soTienGiam)); }
    public HoaDon getByMaHoaDon(String maHoaDon) throws RemoteException { return RemoteCallLogger.log("ThanhToanRemoteImpl", "getByMaHoaDon", new Object[]{maHoaDon}, () -> dao.getByMaHoaDon(maHoaDon)); }
    public boolean updateDiemTichLuy(String maKhachHang, double diemMoi) throws RemoteException { return RemoteCallLogger.log("ThanhToanRemoteImpl", "updateDiemTichLuy", new Object[]{maKhachHang, diemMoi}, () -> dao.updateDiemTichLuy(maKhachHang, diemMoi)); }
}
