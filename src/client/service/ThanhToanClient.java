package client.service;

import client.RemoteServices;
import common.entity.*;

import java.time.LocalDate;
import java.util.List;

public class ThanhToanClient {
    public List<BanAn> getDanhSachBanDangSuDungTheoNgay(LocalDate ngay) { try { return RemoteServices.thanhToan().getDanhSachBanDangSuDungTheoNgay(ngay); } catch (Exception e) { throw new RuntimeException(e); } }
    public HoaDon getHoaDonTheoMaBan(String maBan) { try { return RemoteServices.thanhToan().getHoaDonTheoMaBan(maBan); } catch (Exception e) { throw new RuntimeException(e); } }
    public NhanVien getNhanVienByMa(String maNhanVien) { try { return RemoteServices.thanhToan().getNhanVienByMa(maNhanVien); } catch (Exception e) { throw new RuntimeException(e); } }
    public KhachHang getKhachHangByMa(String maKhachHang) { try { return RemoteServices.thanhToan().getKhachHangByMa(maKhachHang); } catch (Exception e) { throw new RuntimeException(e); } }
    public List<String> getChiTietHoaDonTheoMa(String maHoaDon) { try { return RemoteServices.thanhToan().getChiTietHoaDonTheoMa(maHoaDon); } catch (Exception e) { throw new RuntimeException(e); } }
    public PhieuDatBan timPhieuDatTheoMaBan(String maBan) { try { return RemoteServices.thanhToan().timPhieuDatTheoMaBan(maBan); } catch (Exception e) { throw new RuntimeException(e); } }
    public double getSoTienGiamCaoNhatTheoHoaDon(String maHoaDon) { try { return RemoteServices.thanhToan().getSoTienGiamCaoNhatTheoHoaDon(maHoaDon); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean capNhatTrangThaiThanhToan(String maHoaDon, String phuongThuc) { try { return RemoteServices.thanhToan().capNhatTrangThaiThanhToan(maHoaDon, phuongThuc); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean capNhatTrangThaiHoanTat(String maPhieu) { try { return RemoteServices.thanhToan().capNhatTrangThaiHoanTat(maPhieu); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean capNhatTrangThaiTrong(String maBan) { try { return RemoteServices.thanhToan().capNhatTrangThaiTrong(maBan); } catch (Exception e) { throw new RuntimeException(e); } }
    public List<KhuyenMai> getKhuyenMaiApDungChoHoaDon(String maHoaDon, double tongTien) { try { return RemoteServices.thanhToan().getKhuyenMaiApDungChoHoaDon(maHoaDon, tongTien); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean taoChiTietKMHD(String maHoaDon, String maKhuyenMai, double soTienGiam) { try { return RemoteServices.thanhToan().taoChiTietKMHD(maHoaDon, maKhuyenMai, soTienGiam); } catch (Exception e) { throw new RuntimeException(e); } }
    public HoaDon getByMaHoaDon(String maHoaDon) { try { return RemoteServices.thanhToan().getByMaHoaDon(maHoaDon); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean updateDiemTichLuy(String maKhachHang, double diemMoi) { try { return RemoteServices.thanhToan().updateDiemTichLuy(maKhachHang, diemMoi); } catch (Exception e) { throw new RuntimeException(e); } }
}
