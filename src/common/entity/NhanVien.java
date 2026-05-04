package common.entity;

import java.io.Serializable;
import java.time.LocalDate;

public class NhanVien implements Serializable {
    private static final long serialVersionUID = 1L;
    private String maNhanVien;
    private String tenNhanVien;
    private String chucVu;
    private String CCCD;
    private String soDienThoai;
    private LocalDate ngaySinh;
    private LocalDate ngayVaoLam;
    private LocalDate ngayThoiViec;

    public NhanVien() {}

    public NhanVien(String maNhanVien, String tenNhanVien, String chucVu, String CCCD,
                    String soDienThoai, LocalDate ngaySinh, LocalDate ngayVaoLam, LocalDate ngayThoiViec) {
        this.maNhanVien = maNhanVien;
        this.tenNhanVien = tenNhanVien;
        this.chucVu = chucVu;
        this.CCCD = CCCD;
        this.soDienThoai = soDienThoai;
        this.ngaySinh = ngaySinh;
        this.ngayVaoLam = ngayVaoLam;
        this.ngayThoiViec = ngayThoiViec;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public String getCCCD() {
        return CCCD;
    }

    public void setCCCD(String CCCD) {
        this.CCCD = CCCD;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public LocalDate getNgayVaoLam() {
        return ngayVaoLam;
    }

    public void setNgayVaoLam(LocalDate ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }

    public LocalDate getNgayThoiViec() {
        return ngayThoiViec;
    }

    public void setNgayThoiViec(LocalDate ngayThoiViec) {
        this.ngayThoiViec = ngayThoiViec;
    }

    @Override
    public String toString() {
        return "NhanVien{" +
                "maNhanVien='" + maNhanVien + '\'' +
                ", tenNhanVien='" + tenNhanVien + '\'' +
                ", chucVu='" + chucVu + '\'' +
                ", CCCD='" + CCCD + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", ngaySinh=" + ngaySinh +
                ", ngayVaoLam=" + ngayVaoLam +
                ", ngayThoiViec=" + ngayThoiViec +
                '}';
    }
    
}
