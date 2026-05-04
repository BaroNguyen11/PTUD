package server.dao;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import common.entity.*;
import org.bson.Document;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class QLHD_DAO extends MongoDaoSupport {
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> list = new ArrayList<>();
        for (Document d : col("HoaDon").find().sort(Sorts.descending("ngayTao"))) list.add(hoaDon(d));
        return list;
    }

    /**
     * Tìm kiếm hóa đơn theo mã hóa đơn, trạng thái, phương thức thanh toán.
     * Sử dụng MongoDB $regex với CASE_INSENSITIVE.
     */
    public List<HoaDon> searchHoaDon(String keyword) {
        List<HoaDon> list = new ArrayList<>();
        for (Document d : col("HoaDon").find(Filters.or(
                Filters.regex("maHoaDon", contains(keyword)),
                Filters.regex("trangThai", contains(keyword)),
                Filters.regex("phuongThuc", contains(keyword)),
                Filters.regex("ghiChu", contains(keyword))
        )).sort(Sorts.descending("ngayTao"))) list.add(hoaDon(d));
        return list;
    }

    public boolean insertHoaDon(HoaDon hd, String maNhanVien, String maKhachHang) {
        Document d = hoaDonDoc(hd).append("maNhanVien", maNhanVien).append("maKhachHang", maKhachHang);
        col("HoaDon").insertOne(d);
        
        // Trigger: Tự động cộng điểm tích lũy cho khách hàng
        try {
            double tongTien = tinhTongTien(hd.getMaHoaDon());
            server.trigger.HoaDonTrigger.onHoaDonInserted(hd, maKhachHang, tongTien);
            // Thông báo real-time update
            server.trigger.DataChangeNotifierImpl.getInstance().notifyClients("HoaDon", "INSERT", hd.getMaHoaDon());
        } catch (Exception e) {
            System.err.println("Error firing trigger/notifier: " + e.getMessage());
        }
        
        return true;
    }

    public HoaDon getHoaDonById(String maHoaDon) { return timHoaDonTheoMa(maHoaDon); }

    public List<String> layDanhSachMaBanTheoHoaDon(String maHoaDon) {
        return new BanAn_DAO().getDanhSachBanCungHoaDon(maHoaDon);
    }

    public double tinhTienCoc(String maHoaDon) {
        double total = 0;
        for (String maBan : layDanhSachMaBanTheoHoaDon(maHoaDon)) {
            BanAn b = BanAn_DAO.getByMaBan(maBan);
            if (b != null && b.getLoai() != null) total += b.getLoai() == LoaiBan.VIP ? 450000 : 350000;
        }
        return total;
    }

    public double tinhTongTien(String maHoaDon) { return invoiceTotal(maHoaDon); }

    public List<ChiTietHoaDon> layDSChiTietTheoMaHoaDon(HoaDon hoaDon) {
        return ChiTietHoaDon_DAO.getChiTietHoaDonByMaHD(hoaDon.getMaHoaDon());
    }

    public HoaDon timHoaDonTheoMa(String maHoaDon) {
        return hoaDon(one("HoaDon", "maHoaDon", maHoaDon));
    }

    public List<String> getChiTietHoaDonTheoMa(String maHoaDon) {
        List<String> list = new ArrayList<>();
        for (Document ct : docs("ChiTietHoaDon", Filters.eq("maHoaDon", maHoaDon))) {
            Document mon = one("MonAn", "maMonAn", s(ct, "maMonAn"));
            int sl = integer(ct, "soLuong");
            double gia = dbl(mon, "giaTien");
            list.add(s(mon, "tenMonAn") + "," + sl + "," + gia + "," + (sl * gia));
        }
        return list;
    }

    public List<String> loadDanhSachHoaDon() {
        List<String> list = new ArrayList<>();
        for (Document d : col("HoaDon").find().sort(Sorts.descending("ngayTao"))) list.add(hoaDonString(d));
        return list;
    }

    public static String layHoaDonString(String maHoaDon) {
        return hoaDonString(one("HoaDon", "maHoaDon", maHoaDon));
    }

    private static String hoaDonString(Document d) {
        if (d == null) return "";
        String ma = s(d, "maHoaDon");
        KhachHang kh = khachHang(one("KhachHang", "maKhachHang", s(d, "maKhachHang")));
        NhanVien nv = nhanVien(one("NhanVien", "maNhanVien", s(d, "maNhanVien")));
        String dsBan = String.join("_", new BanAn_DAO().getDanhSachBanCungHoaDon(ma));
        double tong = invoiceTotal(ma);
        double coc = new QLHD_DAO().tinhTienCoc(ma);
        String ngay = toLocalDate(d.get("ngayTao")) == null ? "" : DTF.format(toLocalDate(d.get("ngayTao")));
        return ma + "," + (kh == null ? "" : kh.getTenKhachHang()) + "," + (nv == null ? "" : nv.getTenNhanVien()) + "," +
                tong + "," + coc + "," + s(d, "phuongThuc") + "," + ngay + "," + s(d, "trangThai") + "," + s(d, "ghiChu") + "," + dsBan;
    }
}
