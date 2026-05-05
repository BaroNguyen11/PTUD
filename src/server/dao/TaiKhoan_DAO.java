package server.dao;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import common.entity.NhanVien;
import common.entity.TaiKhoan;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TaiKhoan_DAO extends MongoDaoSupport {

    public List<TaiKhoan> getAllTaiKhoan() {
        List<TaiKhoan> list = new ArrayList<>();
        for (Document d : col("TaiKhoan").find().sort(Sorts.ascending("maTaiKhoan")))
            list.add(taiKhoan(d));
        return list;
    }

    public List<TaiKhoan> searchTaiKhoan(String keyword) {
        List<TaiKhoan> list = new ArrayList<>();
        for (TaiKhoan tk : getAllTaiKhoan()) {
            NhanVien nv = tk.getNhanVien();
            String haystack = (tk.getMaTaiKhoan() + " " + tk.getTaiKhoan() + " " +
                    (nv == null ? "" : nv.getMaNhanVien() + " " + nv.getTenNhanVien() + " " + nv.getSoDienThoai()))
                    .toLowerCase();
            if (haystack.contains((keyword == null ? "" : keyword).toLowerCase()))
                list.add(tk);
        }
        return list;
    }

    public List<TaiKhoan> filterTaiKhoanTheoQuyen(boolean isQuanLy) {
        List<TaiKhoan> list = new ArrayList<>();
        for (Document d : docs("TaiKhoan", Filters.eq("taiKhoanQuanLi", isQuanLy)))
            list.add(taiKhoan(d));
        return list;
    }

    public boolean updateTrangThaiTaiKhoan(String maTaiKhoan, boolean trangThai) {
        return update("TaiKhoan", Filters.eq("maTaiKhoan", maTaiKhoan), new Document("trangThaiHoatDong", trangThai));
    }

    public boolean resetMatKhau(String maTaiKhoan, String matKhauMoi) {
        return update("TaiKhoan", Filters.eq("maTaiKhoan", maTaiKhoan), new Document("matKhau", matKhauMoi));
    }

    public boolean isTenDangNhapExists(String tenDangNhap) {
        return col("TaiKhoan").countDocuments(Filters.eq("taiKhoan", tenDangNhap)) > 0;
    }

    public boolean isNhanVienDaCoTaiKhoan(String maNhanVien) {
        return col("TaiKhoan").countDocuments(Filters.eq("maNhanVien", maNhanVien)) > 0;
    }

    public String generateMaTaiKhoan() {
        return nextId("TaiKhoan", "maTaiKhoan", "TK", 3);
    }

    public boolean addTaiKhoan(TaiKhoan tk) {
        col("TaiKhoan").insertOne(taiKhoanDoc(tk));
        return true;
    }

    public boolean updateTaiKhoan(TaiKhoan tk) {
        return update("TaiKhoan", Filters.eq("maTaiKhoan", tk.getMaTaiKhoan()),
                new Document("taiKhoan", tk.getTaiKhoan())
                        .append("taiKhoanQuanLi", tk.isTaiKhoanQuanLi())
                        .append("trangThaiHoatDong", tk.isTrangThaiHoatDong()));
    }

    public TaiKhoan getTaiKhoanByMa(String maTaiKhoan) {
        return taiKhoan(one("TaiKhoan", "maTaiKhoan", maTaiKhoan));
    }

    public List<NhanVien> getNhanVienChuaCoTaiKhoan() {
        Set<String> used = new HashSet<>();
        for (Document d : docs("TaiKhoan"))
            used.add(s(d, "maNhanVien"));
        List<NhanVien> list = new ArrayList<>();
        for (Document d : docs("NhanVien")) {
            if (!used.contains(s(d, "maNhanVien")))
                list.add(nhanVien(d));
        }
        return list;
    }

    public boolean checkTaiKhoanTonTai(String username) {
        return isTenDangNhapExists(username);
    }

    public boolean createTaiKhoan(TaiKhoan tk) {
        return addTaiKhoan(tk);
    }

    public String tuDongLayMaMoi() {
        return generateMaTaiKhoan();
    }

    public String getMatKhauByMaNV(String maNV) {
        Document d = one("TaiKhoan", "maNhanVien", maNV);
        return d == null ? null : s(d, "matKhau");
    }

    public boolean updateMatKhau(String maNV, String newPassHash) {
        return update("TaiKhoan", Filters.eq("maNhanVien", maNV), new Document("matKhau", newPassHash));
    }
}
