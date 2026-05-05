package server;

import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.CountDownLatch;
import server.trigger.DataChangeNotifierImpl;
import common.NetworkDiscovery;

public class ServerMain {
    public static final int DEFAULT_PORT = 1099;

    public static void main(String[] args) {
        System.setProperty("app.role", "server");
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;

        try {
            configureRmiHostname();

            // Step 1: Open RMI Registry and bind services first
            Registry registry = createOrGetRegistry(port);
            bind(registry, "BanAnRemote", new BanAnRemoteImpl());
            bind(registry, "CaRemote", new CaRemoteImpl());
            bind(registry, "CheckInRemote", new CheckInRemoteImpl());
            bind(registry, "ChiTietHoaDonRemote", new ChiTietHoaDonRemoteImpl());
            bind(registry, "DangNhapRemote", new DangNhapRemoteImpl());
            bind(registry, "DashboardRemote", new DashboardRemoteImpl());
            bind(registry, "HoaDonRemote", new HoaDonRemoteImpl());
            bind(registry, "KhachHangRemote", new KhachHangRemoteImpl());
            bind(registry, "MonAnRemote", new MonAnRemoteImpl());
            bind(registry, "NhanVienRemote", new NhanVienRemoteImpl());
            bind(registry, "PhieuDatBanRemote", new PhieuDatBanRemoteImpl());
            bind(registry, "QLHDRemote", new QLHDRemoteImpl());
            bind(registry, "QLKMRemote", new QLKMRemoteImpl());
            bind(registry, "QLMonRemote", new QLMonRemoteImpl());
            bind(registry, "QuanLyBanRemote", new QuanLyBanRemoteImpl());
            bind(registry, "TaiKhoanRemote", new TaiKhoanRemoteImpl());
            bind(registry, "ThanhToanRemote", new ThanhToanRemoteImpl());
            bind(registry, "ThongKeRemote", new ThongKeRemoteImpl());
            bind(registry, "DataChangeNotifierRemote", DataChangeNotifierImpl.getInstance());

            System.out.println("RMI server started on port " + port);

            // Step 2: Perform DB seeding and validation after RMI is ready
            System.out.println("Connecting to MongoDB...");
            DevDataSeeder.ensureMinimumLoginData(ConnectDB.getDatabase());
            SchemaValidator.ensureConstraints(ConnectDB.getDatabase());
            System.out.println("Database ready: " + ConnectDB.getDatabase().getName());

            NetworkDiscovery.startServerDiscoveryListener();
            new CountDownLatch(1).await();
        } catch (Exception e) {
            System.err.println("RMI server failed to start");
            e.printStackTrace();
        }
    }

    private static void configureRmiHostname() {
        if (System.getProperty("java.rmi.server.hostname") != null) {
            return;
        }

        String configuredHost = System.getProperty("rmi.host");
        if (configuredHost == null || configuredHost.isBlank()) {
            configuredHost = "localhost";
            try {
                InetAddress.getByName(configuredHost);
            } catch (Exception ignored) {
                configuredHost = "127.0.0.1";
            }
        }
        System.setProperty("java.rmi.server.hostname", configuredHost);
        System.out.println("RMI hostname: " + configuredHost);
    }

    private static Registry createOrGetRegistry(int port) throws Exception {
        try {
            Registry registry = LocateRegistry.getRegistry(port);
            registry.list();
            return registry;
        } catch (Exception ignored) {
            return LocateRegistry.createRegistry(port);
        }
    }

    private static void bind(Registry registry, String name, Remote service) throws Exception {
        registry.rebind(name, service);
        System.out.println("Bound " + name);
    }
}
