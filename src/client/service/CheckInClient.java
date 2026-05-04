package client.service;

import client.RemoteServices;
import common.entity.PhieuDatBan;

import java.time.LocalDate;
import java.util.List;

public class CheckInClient {
    public boolean capNhatTrangThaiBan(String maBan, String trangThai) { try { return RemoteServices.checkIn().capNhatTrangThaiBan(maBan, trangThai); } catch (Exception e) { throw new RuntimeException(e); } }
    public PhieuDatBan timPhieuDatBanTheoBanTrongNgay(String maBan) { try { return RemoteServices.checkIn().timPhieuDatBanTheoBanTrongNgay(maBan); } catch (Exception e) { throw new RuntimeException(e); } }
    public List<PhieuDatBan> getPhieuDatBanTheoHoaDonVaNgay(String maHoaDon, LocalDate ngay) { try { return RemoteServices.checkIn().getPhieuDatBanTheoHoaDonVaNgay(maHoaDon, ngay); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean capNhatTrangThaiPhieuDatBan(String maPhieu, String trangThaiMoi) { try { return RemoteServices.checkIn().capNhatTrangThaiPhieuDatBan(maPhieu, trangThaiMoi); } catch (Exception e) { throw new RuntimeException(e); } }
}
