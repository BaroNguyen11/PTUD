package common;

import common.entity.BanAn;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface QuanLyBanRemote extends Remote {
    List<BanAn> getAllBanAn() throws RemoteException;
    BanAn getBanAnByMa(String maBan) throws RemoteException;
    List<BanAn> searchBanAn(String keyword) throws RemoteException;
    boolean addBanAn(BanAn b) throws RemoteException;
    boolean updateBanAn(BanAn b) throws RemoteException;
    boolean deleteBanAn(String maBan) throws RemoteException;
    String generateMaBan() throws RemoteException;
}
