package server;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {
    public static final int DEFAULT_PORT = 1099;

    public static void main(String[] args) {
        System.setProperty("app.role", "server");
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;

        try {
            ConnectDB.getDatabase().listCollectionNames().first();

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

            System.out.println("RMI server started on port " + port);
            System.out.println("Mongo database: " + ConnectDB.getDatabase().getName());
        } catch (Exception e) {
            System.err.println("RMI server failed to start");
            e.printStackTrace();
        }
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
