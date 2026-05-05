package server.dao;

import com.mongodb.client.model.Filters;
import common.entity.PhieuDatBan;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckIn_DAO extends MongoDaoSupport {

    public boolean capNhatTrangThaiBan(String maBan, String trangThai) {
        com.mongodb.client.result.UpdateResult result = col("BanAn").updateOne(
                Filters.eq("maBan", maBan),
                new Document("$set", new Document("trangThai", trangThai))
        );
        return result.getMatchedCount() > 0;
    }

    /**
     * Tìm phiếu đặt bàn theo bàn trong ngày hôm nay sử dụng MongoDB compound filter.
     * Loại trừ phiếu đã hủy và đã dùng.
     */
    public PhieuDatBan timPhieuDatBanTheoBanTrongNgay(String maBan) {
        LocalDate today = LocalDate.now();
        Bson filter = Filters.and(
                Filters.eq("maBan", maBan),
                sameDayFilter("thoiGianBatDau", today),
                Filters.nin("trangThai", Arrays.asList("Đã hủy", "Đã dùng"))
        );
        return phieuDatBan(col("PhieuDatBan").find(filter).first());
    }

    public static List<PhieuDatBan> getPhieuDatBanTheoHoaDonVaNgay(String maHoaDon, LocalDate ngay) {
        List<PhieuDatBan> list = new ArrayList<>();
        for (Document d : docs("PhieuDatBan", Filters.eq("maHoaDon", maHoaDon))) {
            if (sameDay(d.get("thoiGianBatDau"), ngay)) list.add(phieuDatBan(d));
        }
        return list;
    }

    public static boolean capNhatTrangThaiPhieuDatBan(String maPhieu, String trangThaiMoi) {
        com.mongodb.client.result.UpdateResult result = col("PhieuDatBan").updateOne(
                Filters.eq("maPhieu", maPhieu),
                new Document("$set", new Document("trangThai", trangThaiMoi))
        );
        return result.getMatchedCount() > 0;
    }
}
