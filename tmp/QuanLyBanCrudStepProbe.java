import common.QuanLyBanRemote;
import common.entity.BanAn;
import common.entity.LoaiBan;
import common.entity.TrangThai;
import common.entity.ViTri;

import java.rmi.Naming;

public class QuanLyBanCrudStepProbe {
    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 1099;
        String action = args.length > 2 ? args[2] : "read";
        String maBan = args.length > 3 ? args[3] : null;

        QuanLyBanRemote remote = (QuanLyBanRemote) Naming.lookup(
                "rmi://" + host + ":" + port + "/QuanLyBanRemote");

        switch (action) {
            case "create": {
                String id = remote.generateMaBan();
                BanAn ban = new BanAn(id, LoaiBan.THUONG, TrangThai.TRONG, ViTri.LAU_1);
                boolean ok = remote.addBanAn(ban);
                System.out.println("CREATE_ID " + id);
                System.out.println("CREATE_OK " + ok);
                break;
            }
            case "read": {
                BanAn ban = remote.getBanAnByMa(maBan);
                System.out.println("READ_RESULT " + ban);
                break;
            }
            case "update": {
                BanAn ban = new BanAn(maBan, LoaiBan.VIP, TrangThai.DA_DAT, ViTri.LAU_2);
                boolean ok = remote.updateBanAn(ban);
                System.out.println("UPDATE_OK " + ok);
                break;
            }
            case "delete": {
                boolean ok = remote.deleteBanAn(maBan);
                System.out.println("DELETE_OK " + ok);
                break;
            }
            default:
                throw new IllegalArgumentException("Unsupported action: " + action);
        }
    }
}
