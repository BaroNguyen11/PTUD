package client.service;

import client.RemoteServices;
import common.entity.PhieuDatBan;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PhieuDatBanClient {
    public boolean themPhieuDatBan(PhieuDatBan pdb, String trangThaiPhieu) { try { return RemoteServices.phieuDatBan().themPhieuDatBan(pdb, trangThaiPhieu); } catch (Exception e) { throw new RuntimeException(e); } }
    public String getMaPhieuCuoiCung(LocalDate ngayCanTim) { try { return RemoteServices.phieuDatBan().getMaPhieuCuoiCung(ngayCanTim); } catch (Exception e) { throw new RuntimeException(e); } }
    public String taoMaPhieuMoi(LocalDate ngayDat) { try { return RemoteServices.phieuDatBan().taoMaPhieuMoi(ngayDat); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean kiemTraBanDaDatTrongNgay(String maBan, LocalDateTime thoiGianBatDau) { try { return RemoteServices.phieuDatBan().kiemTraBanDaDatTrongNgay(maBan, thoiGianBatDau); } catch (Exception e) { throw new RuntimeException(e); } }
    public PhieuDatBan getPhieuDatBanByMaBanVaNgay(String maBan, LocalDate ngayDat) { try { return RemoteServices.phieuDatBan().getPhieuDatBanByMaBanVaNgay(maBan, ngayDat); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean huyPhieuDatBanByMaBanVaNgay(String maBan, LocalDate ngay) { try { return RemoteServices.phieuDatBan().huyPhieuDatBanByMaBanVaNgay(maBan, ngay); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean huyTatCaPhieuByMaHoaDon(String maHoaDon) { try { return RemoteServices.phieuDatBan().huyTatCaPhieuByMaHoaDon(maHoaDon); } catch (Exception e) { throw new RuntimeException(e); } }
    public List<PhieuDatBan> getByMaHoaDon(String maHoaDon) { try { return RemoteServices.phieuDatBan().getByMaHoaDon(maHoaDon); } catch (Exception e) { throw new RuntimeException(e); } }
    public PhieuDatBan timMotPhieuBangMaHD(String maHoaDon) { try { return RemoteServices.phieuDatBan().timMotPhieuBangMaHD(maHoaDon); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean chuyenBanNhieuSangNhieu(List<String> dsMaBanCu, List<String> dsMaBanMoi, String maHoaDon, String trangThaiMoi, LocalDate ngayChuyen) { try { return RemoteServices.phieuDatBan().chuyenBanNhieuSangNhieu(dsMaBanCu, dsMaBanMoi, maHoaDon, trangThaiMoi, ngayChuyen); } catch (Exception e) { throw new RuntimeException(e); } }
    public PhieuDatBan getPhieuDatBanMoiNhat(String maBan) { try { return RemoteServices.phieuDatBan().getPhieuDatBanMoiNhat(maBan); } catch (Exception e) { throw new RuntimeException(e); } }
    public int demSoBanDangSuDungCuaHoaDon(String maHoaDon) { try { return RemoteServices.phieuDatBan().demSoBanDangSuDungCuaHoaDon(maHoaDon); } catch (Exception e) { throw new RuntimeException(e); } }
}
