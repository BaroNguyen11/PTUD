package dao;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import common.entity.*;
import org.bson.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PhieuDatBan_DAO extends MongoDaoSupport {
    private BanAn_DAO banAn_DAO;

    public PhieuDatBan_DAO() {
        banAn_DAO = new BanAn_DAO();
    }

    public boolean themPhieuDatBan(PhieuDatBan pdb, String trangThaiPhieu) {
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

    public boolean kiemTraBanDaDatTrongNgay(String maBan, LocalDateTime thoiGianBatDau) {
        LocalDate ngay = thoiGianBatDau.toLocalDate();
        for (Document d : docs("PhieuDatBan", Filters.eq("maBan", maBan))) {
            String tt = s(d, "trangThai");
            if (sameDay(d.get("thoiGianBatDau"), ngay) && !"Đã hủy".equalsIgnoreCase(tt) && !"Đã dùng".equalsIgnoreCase(tt)) return true;
        }
        return false;
    }

    public PhieuDatBan getPhieuDatBanByMaBanVaNgay(String maBan, LocalDate ngayDat) {
        for (Document d : docs("PhieuDatBan", Filters.eq("maBan", maBan))) {
            if (sameDay(d.get("thoiGianBatDau"), ngayDat)) return phieuDatBan(d);
        }
        return null;
    }

    public boolean huyPhieuDatBanByMaBanVaNgay(String maBan, LocalDate ngay) {
        boolean ok = false;
        for (Document d : docs("PhieuDatBan", Filters.eq("maBan", maBan))) {
            if (sameDay(d.get("thoiGianBatDau"), ngay)) {
                ok |= update("PhieuDatBan", Filters.eq("maPhieu", s(d, "maPhieu")), new Document("trangThai", "Đã hủy"));
            }
        }
        return ok;
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
            col("BanAn").updateOne(Filters.eq("maBan", maBanMoi), Updates.set("trangThai", trangThaiMoi));
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
        return (int) col("PhieuDatBan").countDocuments(Filters.and(Filters.eq("maHoaDon", maHoaDon), Filters.eq("trangThai", "Đang dùng")));
    }
}
