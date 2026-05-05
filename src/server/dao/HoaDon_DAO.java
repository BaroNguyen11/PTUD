package server.dao;

import com.mongodb.client.model.Sorts;
import common.entity.HoaDon;
import org.bson.Document;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HoaDon_DAO extends MongoDaoSupport {

    public String themHoaDon(HoaDon hd) {
        if (hd.getMaHoaDon() == null || hd.getMaHoaDon().isEmpty()) hd.setMaHoaDon(taoMaHoaDonMoi(LocalDate.now()));
        // Set giá trị mặc định cho các trường null (MongoDB schema yêu cầu trangThai là string)
        if (hd.getTrangThai() == null) hd.setTrangThai("Đang phục vụ");
        if (hd.getPhuongThuc() == null) hd.setPhuongThuc("");
        if (hd.getGhiChu() == null) hd.setGhiChu("");
        col("HoaDon").insertOne(hoaDonDoc(hd));
        return hd.getMaHoaDon();
    }

    public String getMaHoaDonCuoiCung() {
        Document d = col("HoaDon").find().sort(Sorts.descending("maHoaDon")).first();
        return d == null ? null : s(d, "maHoaDon");
    }

    public String taoMaHoaDonMoi(LocalDate ngayLap) {
        String prefix = "HD" + ngayLap.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        String last = maxId("HoaDon", "maHoaDon", prefix);
        if (last == null) return prefix + "001";
        try {
            return prefix + String.format("%03d", Integer.parseInt(last.substring(last.length() - 3)) + 1);
        } catch (RuntimeException ex) {
            return prefix + "001";
        }
    }

    public String getMaHoaDonCuoiCungTheoNgay(LocalDate ngay) {
        String prefix = "HD" + ngay.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        return maxId("HoaDon", "maHoaDon", prefix);
    }
}
