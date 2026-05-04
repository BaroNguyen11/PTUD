package common;

import common.entity.MonAn;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface QLMonRemote extends Remote {
    List<MonAn> getDanhSachMonAn() throws RemoteException;
    boolean insertMon(MonAn mon) throws RemoteException;
    String taoMaMonAn() throws RemoteException;
    boolean updateMon(MonAn mon) throws RemoteException;
}
