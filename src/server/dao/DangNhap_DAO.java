package server.dao;

import com.mongodb.client.model.Filters;
import org.bson.Document;

public class DangNhap_DAO extends MongoDaoSupport {

    public boolean authenticate(String username, String password) {
        return col("TaiKhoan").countDocuments(Filters.and(Filters.eq("taiKhoan", username), Filters.eq("matKhau", password))) > 0;
    }

    public boolean isAdmin(String username) {
        Document d = one("TaiKhoan", "taiKhoan", username);
        return bool(d, "taiKhoanQuanLi");
    }

    public String getMaNhanVien(String username) {
        Document d = one("TaiKhoan", "taiKhoan", username);
        return d == null ? null : s(d, "maNhanVien");
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) {
        return update("TaiKhoan", Filters.and(Filters.eq("taiKhoan", username), Filters.eq("matKhau", oldPassword)), new Document("matKhau", newPassword));
    }

    public boolean isUsernameExist(String username) {
        return col("TaiKhoan").countDocuments(Filters.eq("taiKhoan", username)) > 0;
    }

    public boolean isTaiKhoanHoatDong(String username) {
        Document d = one("TaiKhoan", "taiKhoan", username);
        return bool(d, "trangThaiHoatDong");
    }
}
