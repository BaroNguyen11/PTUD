package client.service;

import client.RemoteServices;
import common.entity.NhanVien;
import common.entity.TaiKhoan;

import java.util.List;

public class TaiKhoanClient {
    public List<TaiKhoan> getAllTaiKhoan() { try { return RemoteServices.taiKhoan().getAllTaiKhoan(); } catch (Exception e) { throw new RuntimeException(e); } }
    public List<TaiKhoan> searchTaiKhoan(String keyword) { try { return RemoteServices.taiKhoan().searchTaiKhoan(keyword); } catch (Exception e) { throw new RuntimeException(e); } }
    public List<TaiKhoan> filterTaiKhoanTheoQuyen(boolean isQuanLy) { try { return RemoteServices.taiKhoan().filterTaiKhoanTheoQuyen(isQuanLy); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean updateTrangThaiTaiKhoan(String maTaiKhoan, boolean trangThai) { try { return RemoteServices.taiKhoan().updateTrangThaiTaiKhoan(maTaiKhoan, trangThai); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean resetMatKhau(String maTaiKhoan, String matKhauMoi) { try { return RemoteServices.taiKhoan().resetMatKhau(maTaiKhoan, matKhauMoi); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean isTenDangNhapExists(String tenDangNhap) { try { return RemoteServices.taiKhoan().isTenDangNhapExists(tenDangNhap); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean isNhanVienDaCoTaiKhoan(String maNhanVien) { try { return RemoteServices.taiKhoan().isNhanVienDaCoTaiKhoan(maNhanVien); } catch (Exception e) { throw new RuntimeException(e); } }
    public String generateMaTaiKhoan() { try { return RemoteServices.taiKhoan().generateMaTaiKhoan(); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean addTaiKhoan(TaiKhoan tk) { try { return RemoteServices.taiKhoan().addTaiKhoan(tk); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean updateTaiKhoan(TaiKhoan tk) { try { return RemoteServices.taiKhoan().updateTaiKhoan(tk); } catch (Exception e) { throw new RuntimeException(e); } }
    public TaiKhoan getTaiKhoanByMa(String maTaiKhoan) { try { return RemoteServices.taiKhoan().getTaiKhoanByMa(maTaiKhoan); } catch (Exception e) { throw new RuntimeException(e); } }
    public List<NhanVien> getNhanVienChuaCoTaiKhoan() { try { return RemoteServices.taiKhoan().getNhanVienChuaCoTaiKhoan(); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean checkTaiKhoanTonTai(String username) { try { return RemoteServices.taiKhoan().checkTaiKhoanTonTai(username); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean createTaiKhoan(TaiKhoan tk) { try { return RemoteServices.taiKhoan().createTaiKhoan(tk); } catch (Exception e) { throw new RuntimeException(e); } }
    public String tuDongLayMaMoi() { try { return RemoteServices.taiKhoan().tuDongLayMaMoi(); } catch (Exception e) { throw new RuntimeException(e); } }
    public String getMatKhauByMaNV(String maNV) { try { return RemoteServices.taiKhoan().getMatKhauByMaNV(maNV); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean updateMatKhau(String maNV, String newPassHash) { try { return RemoteServices.taiKhoan().updateMatKhau(maNV, newPassHash); } catch (Exception e) { throw new RuntimeException(e); } }
}
