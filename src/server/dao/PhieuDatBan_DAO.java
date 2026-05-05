package server.dao;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import common.entity.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PhieuDatBan_DAO extends MongoDaoSupport {
    private BanAn_DAO banAn_DAO;

    public PhieuDatBan_DAO() {
        banAn_DAO = new BanAn_DAO();
    }

    public boolean themPhieuDatBan(PhieuDatBan pdb, String trangThaiPhieu) {
        // Tự sinh mã phiếu nếu client không gửi (MongoDB schema yêu cầu maPhieu là string)
        if (pdb.getMaPhieu() == null || pdb.getMaPhieu().isEmpty()) {
            LocalDate ngay = pdb.getThoiGianBatDau() != null ? pdb.getThoiGianBatDau().toLocalDate() : LocalDate.now();
            pdb.setMaPhieu(taoMaPhieuMoi(ngay));
        }
        if (trangThaiPhieu == null) trangThaiPhieu = "Đã đặt";
        if (pdb.getGhiChu() == null) pdb.setGhiChu("");
        col("PhieuDatBan").insertOne(phieuDatBanDoc(pdb, trangThaiPhieu));
        return true;
    }

    public String getMaPhieuCuoiCung(LocalDate ngayCanTim) {
        String prefix = "PDB" + ngayCanTim.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        return maxId("PhieuDatBan", "maPhieu", prefix);
    }

    public String taoMaPhieuMoi(LocalDate ngayDat) {
        String prefix = "PDB" + ngayDat.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        String last = maxId("PhieuDatBan", "maPhieu", prefix);
        if (last == null) return prefix + "001";
        try {
            return prefix + String.format("%03d", Integer.parseInt(last.substring(last.length() - 3)) + 1);
        } catch (RuntimeException ex) {
            return prefix + "001";
        }
    }

    /**
     * Kiểm tra bàn đã được đặt trong ngày sử dụng MongoDB compound filter.
     */
    public boolean kiemTraBanDaDatTrongNgay(String maBan, LocalDateTime thoiGianBatDau) {
        LocalDate ngay = thoiGianBatDau.toLocalDate();
        Bson filter = Filters.and(
                Filters.eq("maBan", maBan),
                sameDayFilter("thoiGianBatDau", ngay),
                Filters.nin("trangThai", Arrays.asList("Đã hủy", "Đã dùng"))
        );
        return col("PhieuDatBan").countDocuments(filter) > 0;
    }

    /**
     * Tìm phiếu đặt bàn theo mã bàn và ngày, loại trừ phiếu đã hủy.
     */
    public PhieuDatBan getPhieuDatBanByMaBanVaNgay(String maBan, LocalDate ngayDat) {
        Bson filter = Filters.and(
                Filters.eq("maBan", maBan),
                sameDayFilter("thoiGianBatDau", ngayDat),
                Filters.ne("trangThai", "Đã hủy")
        );
        return phieuDatBan(col("PhieuDatBan").find(filter)
                .sort(new Document("thoiGianBatDau", -1)).first());
    }

    /**
     * Hủy phiếu đặt bàn theo mã bàn và ngày sử dụng MongoDB updateMany.
     */
    public boolean huyPhieuDatBanByMaBanVaNgay(String maBan, LocalDate ngay) {
        Bson filter = Filters.and(
                Filters.eq("maBan", maBan),
                sameDayFilter("thoiGianBatDau", ngay),
                Filters.nin("trangThai", Arrays.asList("Đã hủy", "Đã dùng"))
        );
        return col("PhieuDatBan").updateMany(filter,
                new Document("$set", new Document("trangThai", "Đã hủy"))).getModifiedCount() > 0;
    }

    public boolean huyTatCaPhieuByMaHoaDon(String maHoaDon) {
        return col("PhieuDatBan").updateMany(Filters.eq("maHoaDon", maHoaDon), new Document("$set", new Document("trangThai", "Đã hủy"))).getModifiedCount() > 0;
    }

    public static List<PhieuDatBan> getByMaHoaDon(String maHoaDon) {
        List<PhieuDatBan> list = new ArrayList<>();
        for (Document d : docs("PhieuDatBan", Filters.eq("maHoaDon", maHoaDon))) list.add(phieuDatBan(d));
        return list;
    }

    public static PhieuDatBan timMotPhieuBangMaHD(String maHoaDon) {
        return phieuDatBan(col("PhieuDatBan").find(Filters.eq("maHoaDon", maHoaDon)).first());
    }

    public boolean chuyenBanNhieuSangNhieu(List<String> dsMaBanCu, List<String> dsMaBanMoi, String maHoaDon, String trangThaiMoi, LocalDate ngayChuyen) {
        for (String maBanCu : dsMaBanCu) {
            col("BanAn").updateOne(Filters.eq("maBan", maBanCu), Updates.set("trangThai", TrangThai.TRONG.name()));
        }
        for (String maBanMoi : dsMaBanMoi) {
            String banTrangThai = TrangThai.DANG_SU_DUNG.name(); // Default
            if (trangThaiMoi != null) {
                if (trangThaiMoi.equalsIgnoreCase("Đã đặt") || trangThaiMoi.equalsIgnoreCase("?a ??t")) banTrangThai = TrangThai.DA_DAT.name();
                else if (trangThaiMoi.equalsIgnoreCase("Trống")) banTrangThai = TrangThai.TRONG.name();
            }
            col("BanAn").updateOne(Filters.eq("maBan", maBanMoi), Updates.set("trangThai", banTrangThai));
        }
        List<Document> phieu = docs("PhieuDatBan", Filters.eq("maHoaDon", maHoaDon));
        int i = 0;
        for (Document d : phieu) {
            if (sameDay(d.get("thoiGianBatDau"), ngayChuyen) && i < dsMaBanMoi.size()) {
                col("PhieuDatBan").updateOne(Filters.eq("maPhieu", s(d, "maPhieu")), Updates.set("maBan", dsMaBanMoi.get(i++)));
            }
        }
        return true;
    }

    public PhieuDatBan getPhieuDatBanMoiNhat(String maBan) {
        return phieuDatBan(col("PhieuDatBan").find(Filters.eq("maBan", maBan)).sort(new Document("thoiGianBatDau", -1)).first());
    }

    public int demSoBanDangSuDungCuaHoaDon(String maHoaDon) {
        return (int) col("PhieuDatBan").countDocuments(Filters.and(Filters.eq("maHoaDon", maHoaDon), Filters.in("trangThai", java.util.Arrays.asList("Đang dùng", "Đã đặt"))));
    }
}
