package common.entity;
import java.io.Serializable;
import java.time.LocalDateTime;

public class HoaDon implements Serializable {
    private static final long serialVersionUID = 1L;
    private String maHoaDon;
    private LocalDateTime ngayTao;
    private String trangThai;
    private String phuongThuc;
    private String ghiChu;

    private NhanVien nhanVien;
    private KhachHang khachHang;

    public HoaDon() {}

    public HoaDon(String maHoaDon, LocalDateTime ngayTao, String trangThai, String phuongThuc,
                  String ghiChu, NhanVien nhanVien, KhachHang khachHang) {
        this.maHoaDon = maHoaDon;
        this.ngayTao = ngayTao;
        this.trangThai = trangThai;
        this.phuongThuc = phuongThuc;
        this.ghiChu = ghiChu;
        this.nhanVien = nhanVien;
        this.khachHang = khachHang;
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getPhuongThuc() {
        return phuongThuc;
    }

    public void setPhuongThuc(String phuongThuc) {
        this.phuongThuc = phuongThuc;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    @Override
    public String toString() {
        return "HoaDon{" +
                "maHoaDon='" + maHoaDon + '\'' +
                ", ngayTao=" + ngayTao +
                ", trangThai='" + trangThai + '\'' +
                ", phuongThuc='" + phuongThuc + '\'' +
                ", ghiChu='" + ghiChu + '\'' +
                '}';
    }
}
