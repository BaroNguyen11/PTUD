package client;

import java.rmi.Naming;
import java.rmi.Remote;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class RmiClientProvider {
    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_PORT = "1099";
    private static final Map<String, Remote> CACHE = new ConcurrentHashMap<>();

    private RmiClientProvider() {
    }

    public static boolean isRemoteMode() {
        return !"server".equalsIgnoreCase(System.getProperty("app.role", "client"));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Remote> T get(String serviceName, Class<T> type) {
        try {
            Remote remote = CACHE.computeIfAbsent(serviceName, RmiClientProvider::lookup);
            return (T) remote;
        } catch (RuntimeException e) {
            throw e;
        }
    }

    private static Remote lookup(String serviceName) {
        try {
            String host = System.getProperty("rmi.host", DEFAULT_HOST);
            String port = System.getProperty("rmi.port", DEFAULT_PORT);
            return (Remote) Naming.lookup("rmi://" + host + ":" + port + "/" + serviceName);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot connect to RMI service: " + serviceName, e);
        }
    }
}
