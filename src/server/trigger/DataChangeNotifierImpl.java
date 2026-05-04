package server.trigger;

import common.DataChangeListener;
import common.DataChangeNotifierRemote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Quản lý các client listeners và broadcast thông báo khi có thay đổi dữ liệu.
 */
public class DataChangeNotifierImpl extends UnicastRemoteObject implements DataChangeNotifierRemote {
    private static DataChangeNotifierImpl instance;
    private final Set<DataChangeListener> listeners = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private DataChangeNotifierImpl() throws RemoteException {
        super();
    }

    public static synchronized DataChangeNotifierImpl getInstance() throws RemoteException {
        if (instance == null) {
            instance = new DataChangeNotifierImpl();
        }
        return instance;
    }

    @Override
    public void registerListener(DataChangeListener listener) throws RemoteException {
        if (listener != null) {
            listeners.add(listener);
            System.out.println("[DataChangeNotifier] Client registered. Total: " + listeners.size());
        }
    }

    @Override
    public void unregisterListener(DataChangeListener listener) throws RemoteException {
        if (listener != null) {
            listeners.remove(listener);
            System.out.println("[DataChangeNotifier] Client unregistered. Total: " + listeners.size());
        }
    }

    /**
     * Broadcast sự kiện đến tất cả các client đã đăng ký.
     */
    public void notifyClients(String collection, String action, String documentId) {
        System.out.println(String.format("[Trigger] Collection: %s | Action: %s | ID: %s", collection, action, documentId));
        
        Set<DataChangeListener> deadListeners = Collections.newSetFromMap(new ConcurrentHashMap<>());
        
        for (DataChangeListener listener : listeners) {
            try {
                listener.onDataChanged(collection, action, documentId);
            } catch (RemoteException e) {
                // Client đã ngắt kết nối
                deadListeners.add(listener);
            }
        }
        
        // Dọn dẹp các listener đã chết
        if (!deadListeners.isEmpty()) {
            listeners.removeAll(deadListeners);
            System.out.println("[DataChangeNotifier] Removed " + deadListeners.size() + " dead listeners.");
        }
    }
}
