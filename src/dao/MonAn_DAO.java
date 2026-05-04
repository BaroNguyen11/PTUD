package dao;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import common.entity.MonAn;
import org.bson.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MonAn_DAO extends MongoDaoSupport {

    public List<MonAn> getAllMonAn() {
        List<MonAn> list = new ArrayList<>();
        for (Document d : col("MonAn").find().sort(Sorts.ascending("maMonAn"))) list.add(monAn(d));
        return list;
    }

    public List<MonAn> getMonAnByLoai(String loaiMon) {
        List<MonAn> list = new ArrayList<>();
        for (Document d : col("MonAn").find(Filters.eq("loaiMon", loaiMon)).sort(Sorts.ascending("maMonAn"))) list.add(monAn(d));
        return list;
    }

    public double layGiaSauKhuyenMai(String maMonAn, LocalDate ngayDat, double giaMacDinh) {
        for (Document ct : docs("ChiTietKMMonAn", Filters.eq("maMonAn", maMonAn))) {
            Document km = one("KhuyenMai", "maKhuyenMai", s(ct, "maKhuyenMai"));
            if (km != null && inRange(toDate(ngayDat), toLocalDate(km.get("ngayBatDau")), toLocalDate(km.get("ngayKetThuc")))) {
                return dbl(ct, "giaSauKhuyenMai");
            }
        }
        return giaMacDinh;
    }

    public static MonAn getMonAnByMa(String maMonAn) {
        return monAn(one("MonAn", "maMonAn", maMonAn));
    }

    public static String getMaMonByTen(String tenMon) {
        Document d = one("MonAn", "tenMonAn", tenMon);
        return d == null ? null : s(d, "maMonAn");
    }

    public List<String> layDanhSachMonAnGiaKMString() {
        List<String> list = new ArrayList<>();
        for (Document d : docs("MonAn")) {
            MonAn m = monAn(d);
            double gia = layGiaSauKhuyenMai(m.getMaMonAn(), LocalDate.now(), m.getGiaTien());
            list.add(m.getMaMonAn() + "," + m.getTenMonAn() + "," + m.getLoaiMon() + "," + m.getGiaTien() + "," + gia);
        }
        return list;
    }

    public List<MonAn> timKiemMonAn(String tuKhoa) {
        List<MonAn> list = new ArrayList<>();
        for (Document d : col("MonAn").find(Filters.or(
                Filters.regex("maMonAn", contains(tuKhoa)),
                Filters.regex("tenMonAn", contains(tuKhoa)),
                Filters.regex("loaiMon", contains(tuKhoa))
        ))) list.add(monAn(d));
        return list;
    }
}
