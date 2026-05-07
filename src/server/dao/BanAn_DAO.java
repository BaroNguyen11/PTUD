package server.dao;

import com.mongodb.client.model.Filters;
import common.entity.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDate;
import java.util.*;

public class BanAn_DAO extends MongoDaoSupport {

    public static BanAn getByMaBan(String maBan) {
        return banAn(one("BanAn", "maBan", maBan));
    }

    public List<BanAn> getAllBanAn() {
        return new QuanLyBan_DAO().getAllBanAn();
    }

    public List<BanAn> getBanAnTheoViTri(ViTri viTri) {
        List<BanAn> list = new ArrayList<>();
        for (Document d : docs("BanAn", Filters.eq("viTri", viTri.name())))
            list.add(banAn(d));
        return list;
    }

    public List<BanAn> getTrangThaiBanTheoNgayVaViTri(ViTri viTri, LocalDate ngay) {
        List<BanAn> list = getBanAnTheoViTri(viTri);
        // Reset tất cả bàn về TRONG trước khi áp dụng trạng thái theo phiếu đặt
        for (BanAn ban : list) {
            ban.setTrangThai(TrangThai.TRONG);
        }
        Map<String, PhieuDatBan> phieuByBan = getPhieuDatBanMapByNgay(ngay);
        for (BanAn ban : list) {
            PhieuDatBan p = phieuByBan.get(ban.getMaBan());
            if (p != null) {
                String tt = p.getTrangThai();
                if ("Đang dùng".equalsIgnoreCase(tt) || "DANG_SU_DUNG".equalsIgnoreCase(tt))
                    ban.setTrangThai(TrangThai.DANG_SU_DUNG);
                else if ("Đã đặt".equalsIgnoreCase(tt) || "DA_DAT".equalsIgnoreCase(tt))
                    ban.setTrangThai(TrangThai.DA_DAT);
            }
        }
        return list;
    }

    /**
     * Lấy map PhieuDatBan theo ngày sử dụng MongoDB Query filter.
     * Filter: thoiGianBatDau trong ngày + trạng thái NOT IN ["Đã hủy", "Đã dùng"].
     */
    public Map<String, PhieuDatBan> getPhieuDatBanMapByNgay(LocalDate ngay) {
        Map<String, PhieuDatBan> map = new HashMap<>();
        Bson filter = Filters.and(
                sameDayFilter("thoiGianBatDau", ngay),
                Filters.nin("trangThai", Arrays.asList("Đã hủy", "Đã dùng"))
        );
        
        // Lấy danh sách và sắp xếp: Đã đặt trước, Đang dùng sau để "Đang dùng" ghi đè lên trong Map
        List<Document> docs = new ArrayList<>();
        col("PhieuDatBan").find(filter).forEach(docs::add);
        
        // Sắp xếp thủ công: Đã đặt -> Đang dùng
        docs.sort((d1, d2) -> {
            String t1 = s(d1, "trangThai");
            String t2 = s(d2, "trangThai");
            if (t1.equals(t2)) return 0;
            if (t1.equalsIgnoreCase("Đang dùng")) return 1;
            return -1;
        });

        for (Document d : docs) {
            map.put(s(d, "maBan"), phieuDatBan(d));
        }
        return map;
    }

    public boolean updateTrangThaiBan(BanAn ban, TrangThai trangThaiMoi) {
        com.mongodb.client.result.UpdateResult result = col("BanAn").updateOne(
                Filters.eq("maBan", ban.getMaBan()),
                new Document("$set", new Document("trangThai", trangThaiMoi.name()))
        );
        return result.getMatchedCount() > 0;
    }

    public boolean isBanDangSuDungHienTai(String maBan) {
        Document d = one("BanAn", "maBan", maBan);
        return d != null && "DANG_SU_DUNG".equalsIgnoreCase(s(d, "trangThai"));
    }

    public List<String> getDanhSachBanCungHoaDon(String maHoaDon) {
        List<String> list = new ArrayList<>();
        for (Document d : docs("PhieuDatBan",
                Filters.and(Filters.eq("maHoaDon", maHoaDon), Filters.ne("maBan", null)))) {
            String maBan = s(d, "maBan");
            if (!list.contains(maBan))
                list.add(maBan);
        }
        return list;
    }

    /**
     * Lấy mã hóa đơn từ bàn, chỉ lấy phiếu đang hoạt động (loại trừ đã hủy/đã dùng).
     */
    public String getMaHoaDonTuBan(String maBan) {
        Bson filter = Filters.and(
                Filters.eq("maBan", maBan),
                Filters.nin("trangThai", Arrays.asList("Đã hủy", "Đã dùng"))
        );
        
        // Ưu tiên tìm phiếu "Đang dùng" trước
        Document d = col("PhieuDatBan").find(Filters.and(filter, Filters.eq("trangThai", "Đang dùng"))).first();
        
        if (d == null) {
            // Nếu không có, lấy phiếu "Đã đặt" gần nhất
            d = col("PhieuDatBan").find(filter).sort(new Document("thoiGianBatDau", -1)).first();
        }
        
        return d == null ? null : s(d, "maHoaDon");
    }
}
