package client.service;

import client.RemoteServices;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ThongKeClient {
    public Map<String, Double> getDoanhThuTheoThang(int nam) { try { return RemoteServices.thongKe().getDoanhThuTheoThang(nam); } catch (Exception e) { throw new RuntimeException(e); } }
    public double getTongDoanhThu() { try { return RemoteServices.thongKe().getTongDoanhThu(); } catch (Exception e) { throw new RuntimeException(e); } }
    public double getDoanhThuThangTruoc() { try { return RemoteServices.thongKe().getDoanhThuThangTruoc(); } catch (Exception e) { throw new RuntimeException(e); } }
    public double getDoanhThuThangNay() { try { return RemoteServices.thongKe().getDoanhThuThangNay(); } catch (Exception e) { throw new RuntimeException(e); } }
    public double getDoanhThuTheoKhoangTG(LocalDate from, LocalDate to) { try { return RemoteServices.thongKe().getDoanhThuTheoKhoangTG(from, to); } catch (Exception e) { throw new RuntimeException(e); } }
    public int[] getThongKeKhachHang(LocalDate from, LocalDate to) { try { return RemoteServices.thongKe().getThongKeKhachHang(from, to); } catch (Exception e) { throw new RuntimeException(e); } }
    public List<String[]> getTopKhachHang(LocalDate from, LocalDate to) { try { return RemoteServices.thongKe().getTopKhachHang(from, to); } catch (Exception e) { throw new RuntimeException(e); } }
    public Map<String, Integer> getTopMonAnBanChay(LocalDate from, LocalDate to) { try { return RemoteServices.thongKe().getTopMonAnBanChay(from, to); } catch (Exception e) { throw new RuntimeException(e); } }
    public Map<String, Double> getDoanhThuTheoNhomMon(LocalDate from, LocalDate to) { try { return RemoteServices.thongKe().getDoanhThuTheoNhomMon(from, to); } catch (Exception e) { throw new RuntimeException(e); } }
    public double getDoanhThuTrungBinhBan() { try { return RemoteServices.thongKe().getDoanhThuTrungBinhBan(); } catch (Exception e) { throw new RuntimeException(e); } }
    public double getTiLeTienMat() { try { return RemoteServices.thongKe().getTiLeTienMat(); } catch (Exception e) { throw new RuntimeException(e); } }
    public double getDoanhThuCaToi() { try { return RemoteServices.thongKe().getDoanhThuCaToi(); } catch (Exception e) { throw new RuntimeException(e); } }
    public List<String[]> getTopKhachHang() { try { return RemoteServices.thongKe().getTopKhachHang(); } catch (Exception e) { throw new RuntimeException(e); } }
    public int[] getThongKeKhachHang() { try { return RemoteServices.thongKe().getThongKeKhachHang(); } catch (Exception e) { throw new RuntimeException(e); } }
    public double getChiTieuTrungBinh() { try { return RemoteServices.thongKe().getChiTieuTrungBinh(); } catch (Exception e) { throw new RuntimeException(e); } }
    public double getTanSuatTrungBinh() { try { return RemoteServices.thongKe().getTanSuatTrungBinh(); } catch (Exception e) { throw new RuntimeException(e); } }
    public Map<String, Double> getDoanhThuTheoCa() { try { return RemoteServices.thongKe().getDoanhThuTheoCa(); } catch (Exception e) { throw new RuntimeException(e); } }
    public Map<String, Integer> getTopMonAnBanChay() { try { return RemoteServices.thongKe().getTopMonAnBanChay(); } catch (Exception e) { throw new RuntimeException(e); } }
    public Map<String, Double> getDoanhThuTheoNhomMon() { try { return RemoteServices.thongKe().getDoanhThuTheoNhomMon(); } catch (Exception e) { throw new RuntimeException(e); } }
    public String[] getMonBanChayNhat() { try { return RemoteServices.thongKe().getMonBanChayNhat(); } catch (Exception e) { throw new RuntimeException(e); } }
    public String[] getMonDoanhThuCaoNhat() { try { return RemoteServices.thongKe().getMonDoanhThuCaoNhat(); } catch (Exception e) { throw new RuntimeException(e); } }
    public double[] getThongKeDoUong() { try { return RemoteServices.thongKe().getThongKeDoUong(); } catch (Exception e) { throw new RuntimeException(e); } }
    public int getTongLuotDatBan() { try { return RemoteServices.thongKe().getTongLuotDatBan(); } catch (Exception e) { throw new RuntimeException(e); } }
    public double getTyLeLapDay() { try { return RemoteServices.thongKe().getTyLeLapDay(); } catch (Exception e) { throw new RuntimeException(e); } }
    public double getTyLeHuyDat() { try { return RemoteServices.thongKe().getTyLeHuyDat(); } catch (Exception e) { throw new RuntimeException(e); } }
    public double getThoiGianSuDungTB() { try { return RemoteServices.thongKe().getThoiGianSuDungTB(); } catch (Exception e) { throw new RuntimeException(e); } }
    public Map<String, Double> getHieuSuatKhuVuc() { try { return RemoteServices.thongKe().getHieuSuatKhuVuc(); } catch (Exception e) { throw new RuntimeException(e); } }
}
