package server.dao;

import com.mongodb.client.model.Filters;
import common.entity.KhuyenMai;
import common.entity.MonAn;
import org.bson.Document;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class QLKM_DAO extends MongoDaoSupport {
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public List<String> layTatCaKhuyenMaiStrings() {
        List<String> list = new ArrayList<>();
        for (Document d : docs("KhuyenMai")) {
            String ma = s(d, "maKhuyenMai");
            boolean mon = col("ChiTietKMMonAn").countDocuments(Filters.eq("maKhuyenMai", ma)) > 0;
            String tt = toLocalDate(d.get("ngayKetThuc")) != null && toLocalDate(d.get("ngayKetThuc")).isBefore(LocalDate.now()) ? "Ngừng" : "Đang áp dụng";
            list.add(ma + "," + s(d, "tenKhuyenMai") + "," + DTF.format(toLocalDate(d.get("ngayBatDau"))) + "," +
                    DTF.format(toLocalDate(d.get("ngayKetThuc"))) + "," + dbl(d, "dieuKienApDung") + "," + dbl(d, "giaTriToiDa") + "," +
                    (bool(d, "giamGiaPhanTram") ? "1" : "0") + "," + dbl(d, "giaTriGiam") + "," + (mon ? "1" : "0") + "," + tt);
        }
        return list;
    }

    public List<String> layDanhSachMonAnStrings(LocalDate ngayBDMoi, LocalDate ngayKTMoi) {
        List<String> list = new ArrayList<>();
        for (MonAn m : new MonAn_DAO().getAllMonAn()) {
            Document ct = one("ChiTietKMMonAn", "maMonAn", m.getMaMonAn());
            boolean discounted = ct != null;
            double giaSau = discounted ? dbl(ct, "giaSauKhuyenMai") : m.getGiaTien();
            String maKM = discounted ? s(ct, "maKhuyenMai") : "";
            list.add(m.getMaMonAn() + "-" + m.getTenMonAn() + "-" + m.getLoaiMon() + "-" + m.getGiaTien() + "-" + maKM + "-" +
                    (discounted ? "1" : "0") + "-" + giaSau + "-" + m.getMoTa() + "-" + (m.getHinhAnh() == null ? "" : m.getHinhAnh()));
        }
        return list;
    }

    public KhuyenMai layKhuyenMaiTheoMonAn(String maMonAn) {
        Document ct = one("ChiTietKMMonAn", "maMonAn", maMonAn);
        return ct == null ? null : khuyenMai(one("KhuyenMai", "maKhuyenMai", s(ct, "maKhuyenMai")));
    }

    public KhuyenMai layKhuyenMaiTheoMa(String maKhuyenMai) { return khuyenMai(one("KhuyenMai", "maKhuyenMai", maKhuyenMai)); }

    public List<String> getDanhSachMonAnTheoKhuyenMai(String maKhuyenMai) {
        List<String> list = new ArrayList<>();
        for (Document ct : docs("ChiTietKMMonAn", Filters.eq("maKhuyenMai", maKhuyenMai))) list.add(s(ct, "maMonAn"));
        return list;
    }

    public String taoMaKhuyenMaiTuDong() { return nextId("KhuyenMai", "maKhuyenMai", "KM", 3); }

    public boolean insert(KhuyenMai km) {
        col("KhuyenMai").insertOne(khuyenMaiDoc(km));
        return true;
    }

    public boolean insertCTKMMonAn(String maKhuyenMai, String maMonAn, double giaSauKM) {
        col("ChiTietKMMonAn").insertOne(new Document("maKhuyenMai", maKhuyenMai).append("maMonAn", maMonAn).append("giaSauKhuyenMai", giaSauKM));
        return true;
    }

    public boolean ngungKhuyenMai(String maKhuyenMai) {
        return update("KhuyenMai", Filters.eq("maKhuyenMai", maKhuyenMai), new Document("ngayKetThuc", toDate(LocalDate.now().minusDays(1))));
    }

    public boolean xoaCTKMMonAn(String maKM) {
        col("ChiTietKMMonAn").deleteMany(Filters.eq("maKhuyenMai", maKM));
        return true;
    }

    public boolean xoaKhuyenMai(String maKM) { return delete("KhuyenMai", Filters.eq("maKhuyenMai", maKM)); }

    public boolean updateKhuyenMai(String maKhuyenMai, String tenKhuyenMai, LocalDate ngayBatDau, LocalDate ngayKetThuc,
                                   double dieuKienApDung, double giaTriToiDa, boolean giamGiaPhanTram, double giaTriGiam) {
        return update("KhuyenMai", Filters.eq("maKhuyenMai", maKhuyenMai),
                new Document("tenKhuyenMai", tenKhuyenMai).append("ngayBatDau", toDate(ngayBatDau)).append("ngayKetThuc", toDate(ngayKetThuc))
                        .append("dieuKienApDung", dieuKienApDung).append("giaTriToiDa", giaTriToiDa)
                        .append("giamGiaPhanTram", giamGiaPhanTram).append("giaTriGiam", giaTriGiam));
    }
}
