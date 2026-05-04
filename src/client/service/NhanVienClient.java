package client.service;

import client.RemoteServices;
import common.entity.NhanVien;

import java.time.LocalDate;
import java.util.List;

public class NhanVienClient {
    public List<NhanVien> getAllNhanVien() { try { return RemoteServices.nhanVien().getAllNhanVien(); } catch (Exception e) { throw new RuntimeException(e); } }
    public NhanVien getNhanVienByMa(String maNhanVien) { try { return RemoteServices.nhanVien().getNhanVienByMa(maNhanVien); } catch (Exception e) { throw new RuntimeException(e); } }
    public static NhanVien getNhanVienByMaStatic(String maNhanVien) { return new NhanVienClient().getNhanVienByMa(maNhanVien); }
    public List<NhanVien> searchNhanVien(String keyword) { try { return RemoteServices.nhanVien().searchNhanVien(keyword); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean addNhanVien(NhanVien nv) { try { return RemoteServices.nhanVien().addNhanVien(nv); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean updateNhanVien(NhanVien nv) { try { return RemoteServices.nhanVien().updateNhanVien(nv); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean thoiViecNhanVien(String maNhanVien) { try { return RemoteServices.nhanVien().thoiViecNhanVien(maNhanVien); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean taiTuyenNhanVien(String maNhanVien) { try { return RemoteServices.nhanVien().taiTuyenNhanVien(maNhanVien); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean isSoDienThoaiExistsForOther(String soDienThoai, String maNhanVien) { try { return RemoteServices.nhanVien().isSoDienThoaiExistsForOther(soDienThoai, maNhanVien); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean isCCCDExistsForOther(String cccd, String maNhanVien) { try { return RemoteServices.nhanVien().isCCCDExistsForOther(cccd, maNhanVien); } catch (Exception e) { throw new RuntimeException(e); } }
    public String generateMaNhanVien() { try { return RemoteServices.nhanVien().generateMaNhanVien(); } catch (Exception e) { throw new RuntimeException(e); } }
    public LocalDate getNgayThoiViec(String maNhanVien) { try { return RemoteServices.nhanVien().getNgayThoiViec(maNhanVien); } catch (Exception e) { throw new RuntimeException(e); } }
}
