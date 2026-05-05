package server.dao;

import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import common.entity.MonAn;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

    /**
     * Lấy giá sau khuyến mãi sử dụng Aggregation Pipeline.
     * Pipeline: ChiTietKMMonAn → $match maMonAn → $lookup KhuyenMai → $match ngày hợp lệ
     */
    public double layGiaSauKhuyenMai(String maMonAn, LocalDate ngayDat, double giaMacDinh) {
        Date ngay = toDate(ngayDat);
        List<Bson> pipeline = Arrays.asList(
                Aggregates.match(Filters.eq("maMonAn", maMonAn)),
                Aggregates.lookup("KhuyenMai", "maKhuyenMai", "maKhuyenMai", "kmInfo"),
                Aggregates.unwind("$kmInfo"),
                Aggregates.match(Filters.and(
                        Filters.lte("kmInfo.ngayBatDau", ngay),
                        Filters.gte("kmInfo.ngayKetThuc", ngay)
                ))
        );
        Document result = col("ChiTietKMMonAn").aggregate(pipeline).first();
        return result != null ? dbl(result, "giaSauKhuyenMai") : giaMacDinh;
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
