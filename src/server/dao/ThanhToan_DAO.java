package server.dao;

import com.mongodb.client.model.Filters;
import common.entity.*;
import org.bson.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ThanhToan_DAO extends MongoDaoSupport {

    public List<BanAn> getDanhSachBanDangSuDungTheoNgay(LocalDate ngay) {
        List<BanAn> list = new ArrayList<>();
        for (Document p : docs("PhieuDatBan")) {
            if (sameDay(p.get("thoiGianBatDau"), ngay) && "Đang dùng".equalsIgnoreCase(s(p, "trangThai"))) {
                BanAn b = banAn(one("BanAn", "maBan", s(p, "maBan")));
                if (b != null)
                    list.add(b);
            }
        }
        return list;
    }

    public HoaDon getHoaDonTheoMaBan(String maBan) {
        Document p = col("PhieuDatBan").find(Filters.eq("maBan", maBan)).sort(new Document("thoiGianBatDau", -1))
                .first();
        return p == null ? null : hoaDon(one("HoaDon", "maHoaDon", s(p, "maHoaDon")));
    }

    public NhanVien getNhanVienByMa(String maNhanVien) {
        return NhanVien_DAO.getNhanVienByMa(maNhanVien);
    }

    public KhachHang getKhachHangByMa(String maKhachHang) {
        return KhachHang_DAO.getKhachHangById(maKhachHang);
    }

    public List<String> getChiTietHoaDonTheoMa(String maHoaDon) {
        return new QLHD_DAO().getChiTietHoaDonTheoMa(maHoaDon);
    }

    public PhieuDatBan timPhieuDatTheoMaBan(String maBan) {
        return phieuDatBan(
                col("PhieuDatBan").find(Filters.eq("maBan", maBan)).sort(new Document("thoiGianBatDau", -1)).first());
    }

    public double getSoTienGiamCaoNhatTheoHoaDon(String maHoaDon) {
        double max = 0;
        for (Document d : docs("ChiTietKMHD", Filters.eq("maHoaDon", maHoaDon)))
            max = Math.max(max, dbl(d, "soTienGiam"));
        return max;
    }

    public boolean capNhatTrangThaiThanhToan(String maHoaDon, String phuongThuc) {
        return update("HoaDon", Filters.eq("maHoaDon", maHoaDon),
                new Document("trangThai", "Đã thanh toán").append("phuongThuc", phuongThuc));
    }

    public boolean capNhatTrangThaiHoanTat(String maPhieu) {
        return update("PhieuDatBan", Filters.eq("maPhieu", maPhieu), new Document("trangThai", "Đã dùng"));
    }

    public boolean capNhatTrangThaiTrong(String maBan) {
        return update("BanAn", Filters.eq("maBan", maBan), new Document("trangThai", TrangThai.TRONG.name()));
    }

    public List<KhuyenMai> getKhuyenMaiApDungChoHoaDon(String maHoaDon, double tongTien) {
        List<KhuyenMai> list = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (Document d : docs("KhuyenMai")) {
            KhuyenMai km = khuyenMai(d);
            if (km != null && km.getDieuKienApDung() <= tongTien && !today.isBefore(km.getNgayBatDau())
                    && !today.isAfter(km.getNgayKetThuc()))
                list.add(km);
        }
        return list;
    }

    public boolean taoChiTietKMHD(String maHoaDon, String maKhuyenMai, double soTienGiam) {
        col("ChiTietKMHD").insertOne(
                new Document("maHoaDon", maHoaDon).append("maKhuyenMai", maKhuyenMai).append("soTienGiam", soTienGiam));
        return true;
    }

    public HoaDon getByMaHoaDon(String maHoaDon) {
        return hoaDon(one("HoaDon", "maHoaDon", maHoaDon));
    }

    public boolean updateDiemTichLuy(String maKhachHang, double diemMoi) {
        return update("KhachHang", Filters.eq("maKhachHang", maKhachHang), new Document("diemTichLuy", diemMoi));
    }
}
