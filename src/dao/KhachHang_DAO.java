package dao;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import common.entity.KhachHang;
import org.bson.Document;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class KhachHang_DAO extends MongoDaoSupport {

    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> list = new ArrayList<>();
        for (Document d : col("KhachHang").find().sort(Sorts.ascending("maKhachHang"))) list.add(khachHang(d));
        return list;
    }

    public static KhachHang getKhachHangById(String maKH) {
        return khachHang(one("KhachHang", "maKhachHang", maKH));
    }

    public KhachHang getKhachHangBySdt(String sdt) {
        return khachHang(one("KhachHang", "soDienThoai", sdt));
    }

    public boolean addKhachHang(KhachHang kh) {
        col("KhachHang").insertOne(khachHangDoc(kh));
        return true;
    }

    public String taoMaKhachHangMoi() {
        String prefix = "KH" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        String last = maxId("KhachHang", "maKhachHang", prefix);
        if (last == null) return prefix + "001";
        try {
            return prefix + String.format("%03d", Integer.parseInt(last.substring(last.length() - 3)) + 1);
        } catch (RuntimeException ex) {
            return prefix + "001";
        }
    }

    public boolean themKhachHangMoi(KhachHang kh) {
        kh.setMaKhachHang(taoMaKhachHangMoi());
        if (kh.getDiemTichLuy() == 0.0) kh.setDiemTichLuy(0.0);
        return addKhachHang(kh);
    }

    public boolean updateKhachHang(KhachHang kh) {
        return update("KhachHang", Filters.eq("maKhachHang", kh.getMaKhachHang()), khachHangDoc(kh));
    }

    public boolean isSoDienThoaiExists(String soDienThoai) {
        return col("KhachHang").countDocuments(Filters.eq("soDienThoai", soDienThoai)) > 0;
    }

    public boolean isSoDienThoaiExistsForOther(String soDienThoai, String maKhachHang) {
        return col("KhachHang").countDocuments(Filters.and(Filters.eq("soDienThoai", soDienThoai), Filters.ne("maKhachHang", maKhachHang))) > 0;
    }

    public String generateMaKhachHang() {
        return nextId("KhachHang", "maKhachHang", "KH", 3);
    }
}
