package dao;

import com.mongodb.client.model.Filters;
import common.entity.Ca;
import common.entity.NhanVien;
import org.bson.Document;

import java.time.LocalDateTime;

public class Ca_DAO extends MongoDaoSupport {

    public boolean batDauCa(double tongTienDauCa, String maNhanVien) {
        String maCa = nextId("Ca", "maCa", "CA", 3);
        col("Ca").insertOne(new Document("maCa", maCa).append("thoiGianVaoCa", toDate(LocalDateTime.now()))
                .append("thoiGianKetCa", null).append("tongTienDauCa", tongTienDauCa)
                .append("tongTienCuoiCa", 0.0).append("maNhanVien", maNhanVien));
        return true;
    }

    public boolean ketCa(String maCa, double tongTienCuoiCa) {
        return update("Ca", Filters.eq("maCa", maCa), new Document("thoiGianKetCa", toDate(LocalDateTime.now())).append("tongTienCuoiCa", tongTienCuoiCa));
    }

    public Ca getCaDangMo() {
        return ca(col("Ca").find(Filters.eq("thoiGianKetCa", null)).sort(new Document("thoiGianVaoCa", -1)).first());
    }

    public Ca getCaDangLam(String maNhanVien) {
        return ca(col("Ca").find(Filters.and(Filters.eq("maNhanVien", maNhanVien), Filters.eq("thoiGianKetCa", null))).sort(new Document("thoiGianVaoCa", -1)).first());
    }

    public double tinhTongTienMat(String maNhanVien, LocalDateTime thoiGianVaoCa) {
        double total = 0;
        for (Document hd : docs("HoaDon", Filters.and(Filters.eq("maNhanVien", maNhanVien), Filters.eq("phuongThuc", "Tiền mặt")))) {
            LocalDateTime ngay = toLocalDateTime(hd.get("ngayTao"));
            if (ngay != null && !ngay.isBefore(thoiGianVaoCa)) total += invoiceTotal(s(hd, "maHoaDon"));
        }
        return total;
    }

    public double tinhTongTienGiamGia(String maNhanVien, LocalDateTime thoiGianVaoCa) {
        double total = 0;
        for (Document hd : docs("HoaDon", Filters.eq("maNhanVien", maNhanVien))) {
            LocalDateTime ngay = toLocalDateTime(hd.get("ngayTao"));
            if (ngay != null && !ngay.isBefore(thoiGianVaoCa)) {
                for (Document km : docs("ChiTietKMHD", Filters.eq("maHoaDon", s(hd, "maHoaDon")))) total += dbl(km, "soTienGiam");
            }
        }
        return total;
    }

    public int demDonDangPhucVu(String maNhanVien, LocalDateTime thoiGianVaoCa) {
        return (int) col("HoaDon").countDocuments(Filters.and(Filters.eq("maNhanVien", maNhanVien), Filters.ne("trangThai", "Đã thanh toán")));
    }

    public int demSoHoaDonTrongCa(String maNhanVien, LocalDateTime thoiGianVaoCa) {
        int count = 0;
        for (Document hd : docs("HoaDon", Filters.eq("maNhanVien", maNhanVien))) {
            LocalDateTime ngay = toLocalDateTime(hd.get("ngayTao"));
            if (ngay != null && !ngay.isBefore(thoiGianVaoCa)) count++;
        }
        return count;
    }

    private Ca ca(Document d) {
        if (d == null) return null;
        NhanVien nv = NhanVien_DAO.getNhanVienByMa(s(d, "maNhanVien"));
        return new Ca(s(d, "maCa"), toLocalDateTime(d.get("thoiGianVaoCa")), toLocalDateTime(d.get("thoiGianKetCa")),
                dbl(d, "tongTienDauCa"), dbl(d, "tongTienCuoiCa"), nv);
    }
}
