package server.dao;

import com.mongodb.client.model.Filters;
import common.entity.PhieuDatBan;
import org.bson.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CheckIn_DAO extends MongoDaoSupport {

    public boolean capNhatTrangThaiBan(String maBan, String trangThai) {
        return update("BanAn", Filters.eq("maBan", maBan), new Document("trangThai", trangThai));
    }

    public PhieuDatBan timPhieuDatBanTheoBanTrongNgay(String maBan) {
        LocalDate today = LocalDate.now();
        for (Document d : docs("PhieuDatBan", Filters.eq("maBan", maBan))) {
            if (sameDay(d.get("thoiGianBatDau"), today)) return phieuDatBan(d);
        }
        return null;
    }

    public static List<PhieuDatBan> getPhieuDatBanTheoHoaDonVaNgay(String maHoaDon, LocalDate ngay) {
        List<PhieuDatBan> list = new ArrayList<>();
        for (Document d : docs("PhieuDatBan", Filters.eq("maHoaDon", maHoaDon))) {
            if (sameDay(d.get("thoiGianBatDau"), ngay)) list.add(phieuDatBan(d));
        }
        return list;
    }

    public static boolean capNhatTrangThaiPhieuDatBan(String maPhieu, String trangThaiMoi) {
        return update("PhieuDatBan", Filters.eq("maPhieu", maPhieu), new Document("trangThai", trangThaiMoi));
    }
}
