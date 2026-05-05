package server.dao;

import common.entity.KhachHang;
import org.bson.Document;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

public class Dashboard_DAO extends MongoDaoSupport {

    public double getTongDoanhThuTheoNgay(LocalDate ngay) {
        double total = 0;
        for (Document hd : docs("HoaDon"))
            if (sameDay(hd.get("ngayTao"), ngay))
                total += invoiceTotal(s(hd, "maHoaDon"));
        return total;
    }

    public double getTongDoanhThuHomNay() {
        return getTongDoanhThuTheoNgay(LocalDate.now());
    }

    public double getTongDoanhThuHomQua() {
        return getTongDoanhThuTheoNgay(LocalDate.now().minusDays(1));
    }

    public Map<String, Double> getDoanhThuHomNayTheoGio() {
        Map<String, Double> map = new LinkedHashMap<>();
        for (Document hd : docs("HoaDon")) {
            if (!sameDay(hd.get("ngayTao"), LocalDate.now()))
                continue;
            int h = toLocalDateTime(hd.get("ngayTao")).getHour();
            String key = String.format("%02d:00", h);
            map.put(key, map.getOrDefault(key, 0.0) + invoiceTotal(s(hd, "maHoaDon")));
        }
        return map;
    }

    public Map<String, Double> getDoanhThuTheoThang() {
        Map<String, Double> map = new LinkedHashMap<>();
        for (Document hd : docs("HoaDon")) {
            LocalDate d = toLocalDate(hd.get("ngayTao"));
            if (d != null) {
                String key = String.format("%02d", d.getMonthValue());
                map.put(key, map.getOrDefault(key, 0.0) + invoiceTotal(s(hd, "maHoaDon")));
            }
        }
        return map;
    }

    public int getLuotDatBanHomNay() {
        return getLuotDatBanTheoNgay(LocalDate.now());
    }

    public int getLuotDatBanHomQua() {
        return getLuotDatBanTheoNgay(LocalDate.now().minusDays(1));
    }

    private int getLuotDatBanTheoNgay(LocalDate ngay) {
        int c = 0;
        for (Document p : docs("PhieuDatBan"))
            if (sameDay(p.get("thoiGianBatDau"), ngay))
                c++;
        return c;
    }

    public int getSoMonBanRaHomNay() {
        return getSoMonBanRaTheoNgay(LocalDate.now());
    }

    public int getSoMonBanRaHomQua() {
        return getSoMonBanRaTheoNgay(LocalDate.now().minusDays(1));
    }

    private int getSoMonBanRaTheoNgay(LocalDate ngay) {
        int c = 0;
        for (Document hd : docs("HoaDon"))
            if (sameDay(hd.get("ngayTao"), ngay))
                for (Document ct : docs("ChiTietHoaDon",
                        com.mongodb.client.model.Filters.eq("maHoaDon", s(hd, "maHoaDon"))))
                    c += integer(ct, "soLuong");
        return c;
    }

    public Map<String, Integer> getTopMonBanChayHomNay(int limit) {
        return topMon(LocalDate.now(), null, limit);
    }

    public Map<String, Integer> getTopMonBanChayTheoThang(int limit) {
        return topMon(LocalDate.now().withDayOfMonth(1), LocalDate.now(), limit);
    }

    public int getSoKhachPhucVuHomNay() {
        return getSoKhachPhucVuTheoNgay(LocalDate.now());
    }

    public int getSoKhachPhucVuHomQua() {
        return getSoKhachPhucVuTheoNgay(LocalDate.now().minusDays(1));
    }

    private int getSoKhachPhucVuTheoNgay(LocalDate ngay) {
        Set<String> set = new HashSet<>();
        for (Document hd : docs("HoaDon"))
            if (sameDay(hd.get("ngayTao"), ngay) && s(hd, "maKhachHang") != null)
                set.add(s(hd, "maKhachHang"));
        return set.size();
    }

    public Map<String, Double> getDoanhThuTheoTuan() {
        Map<String, Double> map = new LinkedHashMap<>();
        for (Document hd : docs("HoaDon")) {
            LocalDate d = toLocalDate(hd.get("ngayTao"));
            if (d != null && !d.isBefore(LocalDate.now().minusDays(6))) {
                String key = d.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault());
                map.put(key, map.getOrDefault(key, 0.0) + invoiceTotal(s(hd, "maHoaDon")));
            }
        }
        return map;
    }

    public Map<String, Integer> getTopMonBanChay(int limit) {
        return topMon(null, null, limit);
    }

    public String getMonBanChayNhatHomNay() {
        Map<String, Integer> m = getTopMonBanChayHomNay(1);
        return m.isEmpty() ? "" : m.keySet().iterator().next();
    }

    public Map<String, Integer> getLuongKhachTheoKhungGio() {
        return getLuongKhachTheoKhungGio(LocalDate.now());
    }

    public Map<String, Integer> getLuongKhachTheoKhungGio(LocalDate ngay) {
        Map<String, Integer> map = new LinkedHashMap<>();
        for (Document p : docs("PhieuDatBan"))
            if (sameDay(p.get("thoiGianBatDau"), ngay)) {
                int h = toLocalDateTime(p.get("thoiGianBatDau")).getHour();
                String key = String.format("%02d:00", h);
                map.put(key, map.getOrDefault(key, 0) + integer(p, "soNguoi"));
            }
        return map;
    }

    public int getSoKhachMoi() {
        return (int) col("KhachHang").countDocuments();
    }

    public int getSoKhachQuayLai() {
        int c = 0;
        for (KhachHang k : new KhachHang_DAO().getAllKhachHang())
            if (k.getDiemTichLuy() > 0)
                c++;
        return c;
    }

    public String tinhPhanTramThayDoi(double homNay, double homQua) {
        if (homQua == 0)
            return homNay == 0 ? "0%" : "100%";
        return String.format("%.1f%%", ((homNay - homQua) / homQua) * 100);
    }

    private Map<String, Integer> topMon(LocalDate from, LocalDate to, int limit) {
        Map<String, Integer> counts = new HashMap<>();
        for (Document hd : docs("HoaDon")) {
            LocalDate d = toLocalDate(hd.get("ngayTao"));
            if ((from != null && d.isBefore(from)) || (to != null && d.isAfter(to)))
                continue;
            for (Document ct : docs("ChiTietHoaDon",
                    com.mongodb.client.model.Filters.eq("maHoaDon", s(hd, "maHoaDon")))) {
                Document mon = one("MonAn", "maMonAn", s(ct, "maMonAn"));
                String ten = mon == null ? s(ct, "maMonAn") : s(mon, "tenMonAn");
                counts.put(ten, counts.getOrDefault(ten, 0) + integer(ct, "soLuong"));
            }
        }
        return counts.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).limit(limit)
                .collect(LinkedHashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), LinkedHashMap::putAll);
    }
}
