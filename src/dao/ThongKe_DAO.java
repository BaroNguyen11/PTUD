package dao;

import org.bson.Document;

import java.time.LocalDate;
import java.util.*;

public class ThongKe_DAO extends MongoDaoSupport {
    private final Dashboard_DAO dashboard = new Dashboard_DAO();

    public Map<String, Double> getDoanhThuTheoThang(int nam) {
        Map<String, Double> map = new LinkedHashMap<>();
        for (Document hd : docs("HoaDon")) {
            LocalDate d = toLocalDate(hd.get("ngayTao"));
            if (d != null && d.getYear() == nam) {
                String key = String.format("%02d", d.getMonthValue());
                map.put(key, map.getOrDefault(key, 0.0) + invoiceTotal(s(hd, "maHoaDon")));
            }
        }
        return map;
    }

    public double getTongDoanhThu() { double t = 0; for (Document hd : docs("HoaDon")) t += invoiceTotal(s(hd, "maHoaDon")); return t; }
    public double getDoanhThuThangTruoc() { return doanhThuThang(LocalDate.now().minusMonths(1)); }
    public double getDoanhThuThangNay() { return doanhThuThang(LocalDate.now()); }
    public double getDoanhThuTheoKhoangTG(LocalDate from, LocalDate to) {
        double t = 0; for (Document hd : docs("HoaDon")) if (inRange(hd.get("ngayTao"), from, to)) t += invoiceTotal(s(hd, "maHoaDon")); return t;
    }
    public int[] getThongKeKhachHang(LocalDate from, LocalDate to) { return getThongKeKhachHang(); }
    public List<String[]> getTopKhachHang(LocalDate from, LocalDate to) { return getTopKhachHang(); }
    public Map<String, Integer> getTopMonAnBanChay(LocalDate from, LocalDate to) { return dashboard.getTopMonBanChay(5); }
    public Map<String, Double> getDoanhThuTheoNhomMon(LocalDate from, LocalDate to) { return getDoanhThuTheoNhomMon(); }
    public double getDoanhThuTrungBinhBan() { long n = col("BanAn").countDocuments(); return n == 0 ? 0 : getTongDoanhThu() / n; }
    public double getTiLeTienMat() {
        long total = col("HoaDon").countDocuments(); if (total == 0) return 0;
        return 100.0 * col("HoaDon").countDocuments(com.mongodb.client.model.Filters.eq("phuongThuc", "Tiền mặt")) / total;
    }
    public double getDoanhThuCaToi() { return 0; }
    public List<String[]> getTopKhachHang() {
        Map<String, Double> totals = new HashMap<>();
        for (Document hd : docs("HoaDon")) totals.put(s(hd, "maKhachHang"), totals.getOrDefault(s(hd, "maKhachHang"), 0.0) + invoiceTotal(s(hd, "maHoaDon")));
        List<String[]> rows = new ArrayList<>();
        totals.entrySet().stream().sorted(Map.Entry.<String, Double>comparingByValue().reversed()).limit(5).forEach(e -> {
            Document kh = one("KhachHang", "maKhachHang", e.getKey());
            rows.add(new String[]{e.getKey(), kh == null ? "" : s(kh, "tenKhachHang"), String.valueOf(e.getValue())});
        });
        return rows;
    }
    public int[] getThongKeKhachHang() { return new int[]{(int) col("KhachHang").countDocuments(), dashboard.getSoKhachQuayLai()}; }
    public double getChiTieuTrungBinh() { long n = col("HoaDon").countDocuments(); return n == 0 ? 0 : getTongDoanhThu() / n; }
    public double getTanSuatTrungBinh() { return 0; }
    public Map<String, Double> getDoanhThuTheoCa() { return new LinkedHashMap<>(); }
    public Map<String, Integer> getTopMonAnBanChay() { return dashboard.getTopMonBanChay(5); }
    public Map<String, Double> getDoanhThuTheoNhomMon() {
        Map<String, Double> map = new LinkedHashMap<>();
        for (Document ct : docs("ChiTietHoaDon")) {
            Document mon = one("MonAn", "maMonAn", s(ct, "maMonAn"));
            String loai = mon == null ? "" : s(mon, "loaiMon");
            map.put(loai, map.getOrDefault(loai, 0.0) + integer(ct, "soLuong") * dbl(mon, "giaTien"));
        }
        return map;
    }
    public String[] getMonBanChayNhat() { Map<String, Integer> m = getTopMonAnBanChay(); return m.isEmpty() ? new String[]{"", "0"} : new String[]{m.keySet().iterator().next(), String.valueOf(m.values().iterator().next())}; }
    public String[] getMonDoanhThuCaoNhat() { return getMonBanChayNhat(); }
    public double[] getThongKeDoUong() { return new double[]{0, 0}; }
    public int getTongLuotDatBan() { return (int) col("PhieuDatBan").countDocuments(); }
    public double getTyLeLapDay() { long total = col("BanAn").countDocuments(); return total == 0 ? 0 : 100.0 * col("BanAn").countDocuments(com.mongodb.client.model.Filters.eq("trangThai", "DANG_SU_DUNG")) / total; }
    public double getTyLeHuyDat() { long total = col("PhieuDatBan").countDocuments(); return total == 0 ? 0 : 100.0 * col("PhieuDatBan").countDocuments(com.mongodb.client.model.Filters.eq("trangThai", "Đã hủy")) / total; }
    public double getThoiGianSuDungTB() { return 0; }
    public Map<String, Double> getHieuSuatKhuVuc() { return new LinkedHashMap<>(); }

    private double doanhThuThang(LocalDate month) {
        double t = 0; for (Document hd : docs("HoaDon")) { LocalDate d = toLocalDate(hd.get("ngayTao")); if (d != null && d.getYear() == month.getYear() && d.getMonth() == month.getMonth()) t += invoiceTotal(s(hd, "maHoaDon")); } return t;
    }
}
