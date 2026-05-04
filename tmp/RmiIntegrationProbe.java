import common.DangNhapRemote;
import common.QuanLyBanRemote;
import common.entity.BanAn;
import common.entity.LoaiBan;
import common.entity.TrangThai;
import common.entity.ViTri;

import java.rmi.Naming;

public class RmiIntegrationProbe {
    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 1099;

        DangNhapRemote dangNhap = (DangNhapRemote) Naming.lookup(
                "rmi://" + host + ":" + port + "/DangNhapRemote");
        QuanLyBanRemote quanLyBan = (QuanLyBanRemote) Naming.lookup(
                "rmi://" + host + ":" + port + "/QuanLyBanRemote");

        boolean usernameExists = dangNhap.isUsernameExist("__rmi_probe__");
        System.out.println("LOOKUP_OK DangNhapRemote");
        System.out.println("CALL_OK isUsernameExist(__rmi_probe__)=" + usernameExists);

        String maBan = quanLyBan.generateMaBan();
        BanAn created = new BanAn(maBan, LoaiBan.THUONG, TrangThai.TRONG, ViTri.LAU_1);
        boolean inserted = quanLyBan.addBanAn(created);
        System.out.println("CRUD_CREATE id=" + maBan + " inserted=" + inserted);

        BanAn fetched = quanLyBan.getBanAnByMa(maBan);
        System.out.println("CRUD_READ fetched=" + fetched);

        BanAn updatedBan = new BanAn(maBan, LoaiBan.VIP, TrangThai.DA_DAT, ViTri.LAU_2);
        boolean updated = quanLyBan.updateBanAn(updatedBan);
        System.out.println("CRUD_UPDATE updated=" + updated);

        BanAn fetchedAfterUpdate = quanLyBan.getBanAnByMa(maBan);
        System.out.println("CRUD_READ_AFTER_UPDATE fetched=" + fetchedAfterUpdate);

        boolean deleted = quanLyBan.deleteBanAn(maBan);
        System.out.println("CRUD_DELETE deleted=" + deleted);

        BanAn fetchedAfterDelete = quanLyBan.getBanAnByMa(maBan);
        System.out.println("CRUD_READ_AFTER_DELETE fetched=" + fetchedAfterDelete);
    }
}
