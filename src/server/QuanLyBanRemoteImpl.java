package server;

import common.QuanLyBanRemote;
import server.dao.QuanLyBan_DAO;
import common.entity.BanAn;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class QuanLyBanRemoteImpl extends UnicastRemoteObject implements QuanLyBanRemote {
    private final QuanLyBan_DAO dao = new QuanLyBan_DAO();

    public QuanLyBanRemoteImpl() throws RemoteException {
    }

    public List<BanAn> getAllBanAn() throws RemoteException { return RemoteCallLogger.log("QuanLyBanRemoteImpl", "getAllBanAn", new Object[]{}, () -> dao.getAllBanAn()); }
    public BanAn getBanAnByMa(String maBan) throws RemoteException { return RemoteCallLogger.log("QuanLyBanRemoteImpl", "getBanAnByMa", new Object[]{maBan}, () -> dao.getBanAnByMa(maBan)); }
    public List<BanAn> searchBanAn(String keyword) throws RemoteException { return RemoteCallLogger.log("QuanLyBanRemoteImpl", "searchBanAn", new Object[]{keyword}, () -> dao.searchBanAn(keyword)); }
    public boolean addBanAn(BanAn b) throws RemoteException { return RemoteCallLogger.log("QuanLyBanRemoteImpl", "addBanAn", new Object[]{b}, () -> dao.addBanAn(b)); }
    public boolean updateBanAn(BanAn b) throws RemoteException { return RemoteCallLogger.log("QuanLyBanRemoteImpl", "updateBanAn", new Object[]{b}, () -> dao.updateBanAn(b)); }
    public boolean deleteBanAn(String maBan) throws RemoteException { return RemoteCallLogger.log("QuanLyBanRemoteImpl", "deleteBanAn", new Object[]{maBan}, () -> dao.deleteBanAn(maBan)); }
    public String generateMaBan() throws RemoteException { return RemoteCallLogger.log("QuanLyBanRemoteImpl", "generateMaBan", new Object[]{}, () -> dao.generateMaBan()); }
}
