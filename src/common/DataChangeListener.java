package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface Callback để Server gọi ngược lại Client khi có dữ liệu thay đổi.
 * Phục vụ cho tính năng Real-time Update.
 */
public interface DataChangeListener extends Remote {
    /**
     * Được gọi bởi Server khi có dữ liệu thay đổi dưới Database (hoặc qua logic Server).
     * @param collection Tên bảng/collection bị thay đổi (ví dụ: "MonAn", "HoaDon")
     * @param action Loại hành động ("INSERT", "UPDATE", "DELETE")
     * @param documentId Mã của document bị thay đổi
     */
    void onDataChanged(String collection, String action, String documentId) throws RemoteException;
}
