import common.BanAnRemote;
import common.entity.BanAn;
import java.rmi.Naming;
public class BanStatusProbe {
  public static void main(String[] args) throws Exception {
    BanAnRemote r=(BanAnRemote)Naming.lookup("rmi://localhost:1099/BanAnRemote");
    for (BanAn b: r.getAllBanAn()) {
      System.out.println(b.getMaBan()+"|"+b.getLoai()+"|"+b.getTrangThai()+"|"+b.getViTri());
    }
  }
}
