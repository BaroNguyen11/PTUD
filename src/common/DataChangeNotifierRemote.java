package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface RMI để Client đăng ký nhận thông báo real-time từ Server.
 */
public interface DataChangeNotifierRemote extends Remote {
    /**
     * Đăng ký một listener từ client.
     */
    void registerListener(DataChangeListener listener) throws RemoteException;

    /**
     * Hủy đăng ký listener từ client.
     */
    void unregisterListener(DataChangeListener listener) throws RemoteException;
}
