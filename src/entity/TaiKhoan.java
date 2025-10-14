package entity;

public class TaiKhoan {
    private String maTaiKhoan;
    private String taiKhoan;
    private String matKhau;
    private boolean taiKhoanQuanLi;
    private boolean trangThaiHoatDong;
    private NhanVien nhanVien; // FK

    public TaiKhoan() {}

    public TaiKhoan(String maTaiKhoan, String taiKhoan, String matKhau, boolean taiKhoanQuanLi,
                    boolean trangThaiHoatDong, NhanVien nhanVien) {
        this.maTaiKhoan = maTaiKhoan;
        this.taiKhoan = taiKhoan;
        this.matKhau = matKhau;
        this.taiKhoanQuanLi = taiKhoanQuanLi;
        this.trangThaiHoatDong = trangThaiHoatDong;
        this.nhanVien = nhanVien;
    }

    public String getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public void setMaTaiKhoan(String maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
    }

    public String getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(String taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public boolean isTaiKhoanQuanLi() {
        return taiKhoanQuanLi;
    }

    public void setTaiKhoanQuanLi(boolean taiKhoanQuanLi) {
        this.taiKhoanQuanLi = taiKhoanQuanLi;
    }

    public boolean isTrangThaiHoatDong() {
        return trangThaiHoatDong;
    }

    public void setTrangThaiHoatDong(boolean trangThaiHoatDong) {
        this.trangThaiHoatDong = trangThaiHoatDong;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    @Override
    public String toString() {
        return "TaiKhoan{" +
                "maTaiKhoan='" + maTaiKhoan + '\'' +
                ", taiKhoan='" + taiKhoan + '\'' +
                ", taiKhoanQuanLi=" + taiKhoanQuanLi +
                ", trangThaiHoatDong=" + trangThaiHoatDong +
                '}';
    }
}
