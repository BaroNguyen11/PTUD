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
            String host = System.getProperty("rmi.host");
            if (host == null || host.isEmpty() || host.equalsIgnoreCase("localhost")) {
                // 1. Thử tự động tìm IP Server
                System.out.println("Client: Searching for RMI Server...");
                String discoveredIp = common.NetworkDiscovery.discoverServer();
                
                if (discoveredIp != null) {
                    host = discoveredIp;
                    System.out.println("Client: Discovered Server at " + host);
                } else {
                    // 2. Discovery thất bại (thường do Firewall chặn UDP) -> Hiện hộp thoại nhập tay
                    System.err.println("Client: Discovery failed. Asking for manual IP...");
                    final String[] manualHost = {null};
                    
                    // Sử dụng CountDownLatch để đợi UI thread
                    java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
                    javafx.application.Platform.runLater(() -> {
                        try {
                            javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog("");
                            dialog.setTitle("Kết nối máy chủ");
                            dialog.setHeaderText("Không tìm thấy máy chủ tự động");
                            dialog.setContentText("Nhập địa chỉ IP của máy chủ (Radmin IP):");
                            manualHost[0] = dialog.showAndWait().orElse("localhost");
                        } finally {
                            latch.countDown();
                        }
                    });
                    
                    // Đợi tối đa 1 phút cho người dùng nhập
                    if (!latch.await(60, java.util.concurrent.TimeUnit.SECONDS)) {
                        host = "localhost";
                    } else {
                        host = (manualHost[0] != null && !manualHost[0].isBlank()) ? manualHost[0] : "localhost";
                    }
                }
                System.setProperty("rmi.host", host);
            }
            
            String port = System.getProperty("rmi.port", DEFAULT_PORT);
            String url = "rmi://" + host + ":" + port + "/" + serviceName;
            System.out.println("Client: Connecting to " + url);
            
            return (Remote) Naming.lookup(url);
        } catch (Exception e) {
            System.err.println("Client: Connection error: " + e.getMessage());
            throw new IllegalStateException("Cannot connect to RMI service: " + serviceName, e);
        }
    }
}
