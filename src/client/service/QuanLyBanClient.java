package client.service;

import client.RemoteServices;
import common.entity.BanAn;

import java.util.List;

public class QuanLyBanClient {
    public List<BanAn> getAllBanAn() { try { return RemoteServices.quanLyBan().getAllBanAn(); } catch (Exception e) { throw new RuntimeException(e); } }
    public BanAn getBanAnByMa(String maBan) { try { return RemoteServices.quanLyBan().getBanAnByMa(maBan); } catch (Exception e) { throw new RuntimeException(e); } }
    public List<BanAn> searchBanAn(String keyword) { try { return RemoteServices.quanLyBan().searchBanAn(keyword); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean addBanAn(BanAn b) { try { return RemoteServices.quanLyBan().addBanAn(b); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean updateBanAn(BanAn b) { try { return RemoteServices.quanLyBan().updateBanAn(b); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean deleteBanAn(String maBan) { try { return RemoteServices.quanLyBan().deleteBanAn(maBan); } catch (Exception e) { throw new RuntimeException(e); } }
    public String generateMaBan() { try { return RemoteServices.quanLyBan().generateMaBan(); } catch (Exception e) { throw new RuntimeException(e); } }
}
