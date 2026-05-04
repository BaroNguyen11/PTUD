package client.service;

import client.RemoteServices;
import common.entity.Ca;

import java.time.LocalDateTime;

public class CaClient {
    public boolean batDauCa(double tongTienDauCa, String maNhanVien) { try { return RemoteServices.ca().batDauCa(tongTienDauCa, maNhanVien); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean ketCa(String maCa, double tongTienCuoiCa) { try { return RemoteServices.ca().ketCa(maCa, tongTienCuoiCa); } catch (Exception e) { throw new RuntimeException(e); } }
    public Ca getCaDangMo() { try { return RemoteServices.ca().getCaDangMo(); } catch (Exception e) { throw new RuntimeException(e); } }
    public Ca getCaDangLam(String maNhanVien) { try { return RemoteServices.ca().getCaDangLam(maNhanVien); } catch (Exception e) { throw new RuntimeException(e); } }
    public double tinhTongTienMat(String maNhanVien, LocalDateTime thoiGianVaoCa) { try { return RemoteServices.ca().tinhTongTienMat(maNhanVien, thoiGianVaoCa); } catch (Exception e) { throw new RuntimeException(e); } }
    public double tinhTongTienGiamGia(String maNhanVien, LocalDateTime thoiGianVaoCa) { try { return RemoteServices.ca().tinhTongTienGiamGia(maNhanVien, thoiGianVaoCa); } catch (Exception e) { throw new RuntimeException(e); } }
    public int demDonDangPhucVu(String maNhanVien, LocalDateTime thoiGianVaoCa) { try { return RemoteServices.ca().demDonDangPhucVu(maNhanVien, thoiGianVaoCa); } catch (Exception e) { throw new RuntimeException(e); } }
    public int demSoHoaDonTrongCa(String maNhanVien, LocalDateTime thoiGianVaoCa) { try { return RemoteServices.ca().demSoHoaDonTrongCa(maNhanVien, thoiGianVaoCa); } catch (Exception e) { throw new RuntimeException(e); } }
}
