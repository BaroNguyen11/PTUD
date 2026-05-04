package client.service;

import client.RemoteServices;
import common.entity.KhachHang;

import java.util.List;

public class KhachHangClient {
    public List<KhachHang> getAllKhachHang() { try { return RemoteServices.khachHang().getAllKhachHang(); } catch (Exception e) { throw new RuntimeException(e); } }
    public KhachHang getKhachHangById(String maKH) { try { return RemoteServices.khachHang().getKhachHangById(maKH); } catch (Exception e) { throw new RuntimeException(e); } }
    public KhachHang getKhachHangBySdt(String sdt) { try { return RemoteServices.khachHang().getKhachHangBySdt(sdt); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean addKhachHang(KhachHang kh) { try { return RemoteServices.khachHang().addKhachHang(kh); } catch (Exception e) { throw new RuntimeException(e); } }
    public String taoMaKhachHangMoi() { try { return RemoteServices.khachHang().taoMaKhachHangMoi(); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean themKhachHangMoi(KhachHang kh) { try { return RemoteServices.khachHang().themKhachHangMoi(kh); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean updateKhachHang(KhachHang kh) { try { return RemoteServices.khachHang().updateKhachHang(kh); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean isSoDienThoaiExists(String soDienThoai) { try { return RemoteServices.khachHang().isSoDienThoaiExists(soDienThoai); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean isSoDienThoaiExistsForOther(String soDienThoai, String maKhachHang) { try { return RemoteServices.khachHang().isSoDienThoaiExistsForOther(soDienThoai, maKhachHang); } catch (Exception e) { throw new RuntimeException(e); } }
    public String generateMaKhachHang() { try { return RemoteServices.khachHang().generateMaKhachHang(); } catch (Exception e) { throw new RuntimeException(e); } }
}
