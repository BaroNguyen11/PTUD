package server;

import common.QLMonRemote;
import dao.QLMon_DAO;
import common.entity.MonAn;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class QLMonRemoteImpl extends UnicastRemoteObject implements QLMonRemote {
    private final QLMon_DAO dao = new QLMon_DAO();

    public QLMonRemoteImpl() throws RemoteException {
    }

    public List<MonAn> getDanhSachMonAn() throws RemoteException { return RemoteCallLogger.log("QLMonRemoteImpl", "getDanhSachMonAn", new Object[]{}, () -> dao.getDanhSachMonAn()); }
    public boolean insertMon(MonAn mon) throws RemoteException { return RemoteCallLogger.log("QLMonRemoteImpl", "insertMon", new Object[]{mon}, () -> dao.insertMon(mon)); }
    public String taoMaMonAn() throws RemoteException { return RemoteCallLogger.log("QLMonRemoteImpl", "taoMaMonAn", new Object[]{}, () -> dao.taoMaMonAn()); }
    public boolean updateMon(MonAn mon) throws RemoteException { return RemoteCallLogger.log("QLMonRemoteImpl", "updateMon", new Object[]{mon}, () -> dao.updateMon(mon)); }
}
