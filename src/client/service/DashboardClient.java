package client.service;

import client.RemoteServices;

import java.time.LocalDate;
import java.util.Map;

public class DashboardClient {
    public double getTongDoanhThuTheoNgay(LocalDate ngay) { try { return RemoteServices.dashboard().getTongDoanhThuTheoNgay(ngay); } catch (Exception e) { throw new RuntimeException(e); } }
    public double getTongDoanhThuHomNay() { try { return RemoteServices.dashboard().getTongDoanhThuHomNay(); } catch (Exception e) { throw new RuntimeException(e); } }
    public double getTongDoanhThuHomQua() { try { return RemoteServices.dashboard().getTongDoanhThuHomQua(); } catch (Exception e) { throw new RuntimeException(e); } }
    public Map<String, Double> getDoanhThuHomNayTheoGio() { try { return RemoteServices.dashboard().getDoanhThuHomNayTheoGio(); } catch (Exception e) { throw new RuntimeException(e); } }
    public Map<String, Double> getDoanhThuTheoThang() { try { return RemoteServices.dashboard().getDoanhThuTheoThang(); } catch (Exception e) { throw new RuntimeException(e); } }
    public int getLuotDatBanHomNay() { try { return RemoteServices.dashboard().getLuotDatBanHomNay(); } catch (Exception e) { throw new RuntimeException(e); } }
    public int getLuotDatBanHomQua() { try { return RemoteServices.dashboard().getLuotDatBanHomQua(); } catch (Exception e) { throw new RuntimeException(e); } }
    public int getSoMonBanRaHomNay() { try { return RemoteServices.dashboard().getSoMonBanRaHomNay(); } catch (Exception e) { throw new RuntimeException(e); } }
    public int getSoMonBanRaHomQua() { try { return RemoteServices.dashboard().getSoMonBanRaHomQua(); } catch (Exception e) { throw new RuntimeException(e); } }
    public Map<String, Integer> getTopMonBanChayHomNay(int limit) { try { return RemoteServices.dashboard().getTopMonBanChayHomNay(limit); } catch (Exception e) { throw new RuntimeException(e); } }
    public Map<String, Integer> getTopMonBanChayTheoThang(int limit) { try { return RemoteServices.dashboard().getTopMonBanChayTheoThang(limit); } catch (Exception e) { throw new RuntimeException(e); } }
    public int getSoKhachPhucVuHomNay() { try { return RemoteServices.dashboard().getSoKhachPhucVuHomNay(); } catch (Exception e) { throw new RuntimeException(e); } }
    public int getSoKhachPhucVuHomQua() { try { return RemoteServices.dashboard().getSoKhachPhucVuHomQua(); } catch (Exception e) { throw new RuntimeException(e); } }
    public Map<String, Double> getDoanhThuTheoTuan() { try { return RemoteServices.dashboard().getDoanhThuTheoTuan(); } catch (Exception e) { throw new RuntimeException(e); } }
    public Map<String, Integer> getTopMonBanChay(int limit) { try { return RemoteServices.dashboard().getTopMonBanChay(limit); } catch (Exception e) { throw new RuntimeException(e); } }
    public String getMonBanChayNhatHomNay() { try { return RemoteServices.dashboard().getMonBanChayNhatHomNay(); } catch (Exception e) { throw new RuntimeException(e); } }
    public Map<String, Integer> getLuongKhachTheoKhungGio() { try { return RemoteServices.dashboard().getLuongKhachTheoKhungGio(); } catch (Exception e) { throw new RuntimeException(e); } }
    public Map<String, Integer> getLuongKhachTheoKhungGio(LocalDate ngay) { try { return RemoteServices.dashboard().getLuongKhachTheoKhungGio(ngay); } catch (Exception e) { throw new RuntimeException(e); } }
    public int getSoKhachMoi() { try { return RemoteServices.dashboard().getSoKhachMoi(); } catch (Exception e) { throw new RuntimeException(e); } }
    public int getSoKhachQuayLai() { try { return RemoteServices.dashboard().getSoKhachQuayLai(); } catch (Exception e) { throw new RuntimeException(e); } }
    public String tinhPhanTramThayDoi(double homNay, double homQua) { try { return RemoteServices.dashboard().tinhPhanTramThayDoi(homNay, homQua); } catch (Exception e) { throw new RuntimeException(e); } }
}
