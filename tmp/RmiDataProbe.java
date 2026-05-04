import common.BanAnRemote;
import common.DashboardRemote;
import common.MonAnRemote;

import java.rmi.Naming;

public class RmiDataProbe {
    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 1099;

        BanAnRemote banAn = (BanAnRemote) Naming.lookup("rmi://" + host + ":" + port + "/BanAnRemote");
        MonAnRemote monAn = (MonAnRemote) Naming.lookup("rmi://" + host + ":" + port + "/MonAnRemote");
        DashboardRemote dashboard = (DashboardRemote) Naming.lookup("rmi://" + host + ":" + port + "/DashboardRemote");

        System.out.println("BANAN_COUNT " + banAn.getAllBanAn().size());
        System.out.println("MONAN_COUNT " + monAn.getAllMonAn().size());
        System.out.println("DASHBOARD_REVENUE_TODAY " + dashboard.getTongDoanhThuHomNay());
        System.out.println("DASHBOARD_TOP_TODAY " + dashboard.getMonBanChayNhatHomNay());
    }
}
