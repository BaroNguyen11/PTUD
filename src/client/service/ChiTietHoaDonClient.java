package client.service;

import client.RemoteServices;
import common.entity.ChiTietHoaDon;

import java.util.List;

public class ChiTietHoaDonClient {
    public boolean themChiTietHoaDon(ChiTietHoaDon cthd) { try { return RemoteServices.chiTietHoaDon().themChiTietHoaDon(cthd); } catch (Exception e) { throw new RuntimeException(e); } }
    public List<ChiTietHoaDon> getChiTietHoaDonByMaHD(String maHD) { try { return RemoteServices.chiTietHoaDon().getChiTietHoaDonByMaHD(maHD); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean themHoacUpdate(String maHD, String maMon, int soLuongThem, double giaBan) { try { return RemoteServices.chiTietHoaDon().themHoacUpdate(maHD, maMon, soLuongThem, giaBan); } catch (Exception e) { throw new RuntimeException(e); } }
}
