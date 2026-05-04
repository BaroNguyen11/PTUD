package dao;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import common.entity.NhanVien;
import org.bson.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NhanVien_DAO extends MongoDaoSupport {

    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> list = new ArrayList<>();
        for (Document d : col("NhanVien").find().sort(Sorts.ascending("maNhanVien"))) list.add(nhanVien(d));
        return list;
    }

    public static NhanVien getNhanVienByMa(String maNhanVien) {
        return nhanVien(one("NhanVien", "maNhanVien", maNhanVien));
    }

    public List<NhanVien> searchNhanVien(String keyword) {
        List<NhanVien> list = new ArrayList<>();
        for (Document d : col("NhanVien").find(Filters.or(
                Filters.regex("maNhanVien", contains(keyword)),
                Filters.regex("tenNhanVien", contains(keyword)),
                Filters.regex("soDienThoai", contains(keyword))
        )).sort(Sorts.ascending("maNhanVien"))) list.add(nhanVien(d));
        return list;
    }

    public boolean addNhanVien(NhanVien nv) {
        col("NhanVien").insertOne(nhanVienDoc(nv));
        return true;
    }

    public boolean updateNhanVien(NhanVien nv) {
        return update("NhanVien", Filters.eq("maNhanVien", nv.getMaNhanVien()), nhanVienDoc(nv));
    }

    public boolean thoiViecNhanVien(String maNhanVien) {
        boolean ok = update("NhanVien", Filters.eq("maNhanVien", maNhanVien), new Document("ngayThoiViec", toDate(LocalDate.now())));
        col("TaiKhoan").updateMany(Filters.eq("maNhanVien", maNhanVien), new Document("$set", new Document("trangThaiHoatDong", false)));
        return ok;
    }

    public boolean taiTuyenNhanVien(String maNhanVien) {
        boolean ok = update("NhanVien", Filters.eq("maNhanVien", maNhanVien), new Document("ngayThoiViec", null));
        col("TaiKhoan").updateMany(Filters.eq("maNhanVien", maNhanVien), new Document("$set", new Document("trangThaiHoatDong", true)));
        return ok;
    }

    public boolean isSoDienThoaiExistsForOther(String soDienThoai, String maNhanVien) {
        return col("NhanVien").countDocuments(Filters.and(Filters.eq("soDienThoai", soDienThoai), Filters.ne("maNhanVien", maNhanVien))) > 0;
    }

    public boolean isCCCDExistsForOther(String cccd, String maNhanVien) {
        return col("NhanVien").countDocuments(Filters.and(Filters.eq("CCCD", cccd), Filters.ne("maNhanVien", maNhanVien))) > 0;
    }

    public String generateMaNhanVien() {
        return nextId("NhanVien", "maNhanVien", "NV", 3);
    }

    public LocalDate getNgayThoiViec(String maNhanVien) {
        Document d = one("NhanVien", "maNhanVien", maNhanVien);
        return d == null ? null : toLocalDate(d.get("ngayThoiViec"));
    }
}
