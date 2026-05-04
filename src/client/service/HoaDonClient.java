package client.service;

import client.RemoteServices;
import common.entity.HoaDon;

import java.time.LocalDate;

public class HoaDonClient {
    public String themHoaDon(HoaDon hd) { try { return RemoteServices.hoaDon().themHoaDon(hd); } catch (Exception e) { throw new RuntimeException(e); } }
    public String getMaHoaDonCuoiCung() { try { return RemoteServices.hoaDon().getMaHoaDonCuoiCung(); } catch (Exception e) { throw new RuntimeException(e); } }
    public String taoMaHoaDonMoi(LocalDate ngayLap) { try { return RemoteServices.hoaDon().taoMaHoaDonMoi(ngayLap); } catch (Exception e) { throw new RuntimeException(e); } }
    public String getMaHoaDonCuoiCungTheoNgay(LocalDate ngay) { try { return RemoteServices.hoaDon().getMaHoaDonCuoiCungTheoNgay(ngay); } catch (Exception e) { throw new RuntimeException(e); } }
}
