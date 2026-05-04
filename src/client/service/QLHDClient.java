package client.service;

import client.RemoteServices;
import common.entity.ChiTietHoaDon;
import common.entity.HoaDon;

import java.util.List;

public class QLHDClient {
    public List<HoaDon> getAllHoaDon() { try { return RemoteServices.qlhd().getAllHoaDon(); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean insertHoaDon(HoaDon hd, String maNhanVien, String maKhachHang) { try { return RemoteServices.qlhd().insertHoaDon(hd, maNhanVien, maKhachHang); } catch (Exception e) { throw new RuntimeException(e); } }
    public HoaDon getHoaDonById(String maHoaDon) { try { return RemoteServices.qlhd().getHoaDonById(maHoaDon); } catch (Exception e) { throw new RuntimeException(e); } }
    public List<String> layDanhSachMaBanTheoHoaDon(String maHoaDon) { try { return RemoteServices.qlhd().layDanhSachMaBanTheoHoaDon(maHoaDon); } catch (Exception e) { throw new RuntimeException(e); } }
    public double tinhTienCoc(String maHoaDon) { try { return RemoteServices.qlhd().tinhTienCoc(maHoaDon); } catch (Exception e) { throw new RuntimeException(e); } }
    public double tinhTongTien(String maHoaDon) { try { return RemoteServices.qlhd().tinhTongTien(maHoaDon); } catch (Exception e) { throw new RuntimeException(e); } }
    public List<ChiTietHoaDon> layDSChiTietTheoMaHoaDon(HoaDon hoaDon) { try { return RemoteServices.qlhd().layDSChiTietTheoMaHoaDon(hoaDon); } catch (Exception e) { throw new RuntimeException(e); } }
    public HoaDon timHoaDonTheoMa(String maHoaDon) { try { return RemoteServices.qlhd().timHoaDonTheoMa(maHoaDon); } catch (Exception e) { throw new RuntimeException(e); } }
    public List<String> getChiTietHoaDonTheoMa(String maHoaDon) { try { return RemoteServices.qlhd().getChiTietHoaDonTheoMa(maHoaDon); } catch (Exception e) { throw new RuntimeException(e); } }
    public List<String> loadDanhSachHoaDon() { try { return RemoteServices.qlhd().loadDanhSachHoaDon(); } catch (Exception e) { throw new RuntimeException(e); } }
    public String layHoaDonString(String maHoaDon) { try { return RemoteServices.qlhd().layHoaDonString(maHoaDon); } catch (Exception e) { throw new RuntimeException(e); } }
    public static String layHoaDonStringStatic(String maHoaDon) { return new QLHDClient().layHoaDonString(maHoaDon); }
}
