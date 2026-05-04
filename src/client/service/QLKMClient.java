package client.service;

import client.RemoteServices;
import common.entity.KhuyenMai;

import java.time.LocalDate;
import java.util.List;

public class QLKMClient {
    public List<String> layTatCaKhuyenMaiStrings() { try { return RemoteServices.qlkm().layTatCaKhuyenMaiStrings(); } catch (Exception e) { throw new RuntimeException(e); } }
    public List<String> layDanhSachMonAnStrings(LocalDate ngayBDMoi, LocalDate ngayKTMoi) { try { return RemoteServices.qlkm().layDanhSachMonAnStrings(ngayBDMoi, ngayKTMoi); } catch (Exception e) { throw new RuntimeException(e); } }
    public KhuyenMai layKhuyenMaiTheoMonAn(String maMonAn) { try { return RemoteServices.qlkm().layKhuyenMaiTheoMonAn(maMonAn); } catch (Exception e) { throw new RuntimeException(e); } }
    public KhuyenMai layKhuyenMaiTheoMa(String maKhuyenMai) { try { return RemoteServices.qlkm().layKhuyenMaiTheoMa(maKhuyenMai); } catch (Exception e) { throw new RuntimeException(e); } }
    public List<String> getDanhSachMonAnTheoKhuyenMai(String maKhuyenMai) { try { return RemoteServices.qlkm().getDanhSachMonAnTheoKhuyenMai(maKhuyenMai); } catch (Exception e) { throw new RuntimeException(e); } }
    public String taoMaKhuyenMaiTuDong() { try { return RemoteServices.qlkm().taoMaKhuyenMaiTuDong(); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean insert(KhuyenMai km) { try { return RemoteServices.qlkm().insert(km); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean insertCTKMMonAn(String maKhuyenMai, String maMonAn, double giaSauKM) { try { return RemoteServices.qlkm().insertCTKMMonAn(maKhuyenMai, maMonAn, giaSauKM); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean ngungKhuyenMai(String maKhuyenMai) { try { return RemoteServices.qlkm().ngungKhuyenMai(maKhuyenMai); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean xoaCTKMMonAn(String maKM) { try { return RemoteServices.qlkm().xoaCTKMMonAn(maKM); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean xoaKhuyenMai(String maKM) { try { return RemoteServices.qlkm().xoaKhuyenMai(maKM); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean updateKhuyenMai(String maKhuyenMai, String tenKhuyenMai, LocalDate ngayBatDau, LocalDate ngayKetThuc, double dieuKienApDung, double giaTriToiDa, boolean giamGiaPhanTram, double giaTriGiam) { try { return RemoteServices.qlkm().updateKhuyenMai(maKhuyenMai, tenKhuyenMai, ngayBatDau, ngayKetThuc, dieuKienApDung, giaTriToiDa, giamGiaPhanTram, giaTriGiam); } catch (Exception e) { throw new RuntimeException(e); } }
}
