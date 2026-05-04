package client.service;

import client.RemoteServices;
import common.entity.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class BanAnClient {
    public BanAn getByMaBan(String maBan) { try { return RemoteServices.banAn().getByMaBan(maBan); } catch (Exception e) { throw new RuntimeException(e); } }
    public static BanAn getByMaBanStatic(String maBan) { return new BanAnClient().getByMaBan(maBan); }
    public List<BanAn> getAllBanAn() { try { return RemoteServices.banAn().getAllBanAn(); } catch (Exception e) { throw new RuntimeException(e); } }
    public List<BanAn> getBanAnTheoViTri(ViTri viTri) { try { return RemoteServices.banAn().getBanAnTheoViTri(viTri); } catch (Exception e) { throw new RuntimeException(e); } }
    public List<BanAn> getTrangThaiBanTheoNgayVaViTri(ViTri viTri, LocalDate ngay) { try { return RemoteServices.banAn().getTrangThaiBanTheoNgayVaViTri(viTri, ngay); } catch (Exception e) { throw new RuntimeException(e); } }
    public Map<String, PhieuDatBan> getPhieuDatBanMapByNgay(LocalDate ngay) { try { return RemoteServices.banAn().getPhieuDatBanMapByNgay(ngay); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean updateTrangThaiBan(BanAn ban, TrangThai trangThaiMoi) { try { return RemoteServices.banAn().updateTrangThaiBan(ban, trangThaiMoi); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean isBanDangSuDungHienTai(String maBan) { try { return RemoteServices.banAn().isBanDangSuDungHienTai(maBan); } catch (Exception e) { throw new RuntimeException(e); } }
    public List<String> getDanhSachBanCungHoaDon(String maHoaDon) { try { return RemoteServices.banAn().getDanhSachBanCungHoaDon(maHoaDon); } catch (Exception e) { throw new RuntimeException(e); } }
    public String getMaHoaDonTuBan(String maBan) { try { return RemoteServices.banAn().getMaHoaDonTuBan(maBan); } catch (Exception e) { throw new RuntimeException(e); } }
}
