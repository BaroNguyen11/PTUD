package common.entity;
import java.io.Serializable;
import java.time.LocalDateTime;

public class PhieuDatBan implements Serializable {
    private static final long serialVersionUID = 1L;
    private String maPhieu;
    private LocalDateTime thoiGianBatDau;
    private LocalDateTime thoiGianKetThuc; // Thời gian kết thúc dự kiến
    private String trangThai;
    private int soNguoi;
    private String ghiChu;
    private KhachHang khachHang;  
    private BanAn ban;
    private NhanVien nhanVien;
    private HoaDon hoaDon;       
    private double tienCoc;

    public PhieuDatBan() {}

    public PhieuDatBan(String maPhieu, LocalDateTime thoiGianBatDau, LocalDateTime thoiGianKetThuc,
                       String trangThai, int soNguoi, String ghiChu,
                       KhachHang khachHang, BanAn ban, NhanVien nhanVien, HoaDon hoaDon, double tienCoc) {
        this.maPhieu = maPhieu;
        this.thoiGianBatDau = thoiGianBatDau;
        this.thoiGianKetThuc = thoiGianKetThuc;
        this.trangThai = trangThai;
        this.soNguoi = soNguoi;
        this.ghiChu = ghiChu;
        this.khachHang = khachHang;
        this.ban = ban;
        this.nhanVien = nhanVien;
        this.hoaDon = hoaDon;
        this.tienCoc = tienCoc;
    }

    public String getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
    }

    public LocalDateTime getThoiGianBatDau() {
        return thoiGianBatDau;
    }

    public void setThoiGianBatDau(LocalDateTime thoiGianBatDau) {
        this.thoiGianBatDau = thoiGianBatDau;
    }

    public LocalDateTime getThoiGianKetThuc() {
        return thoiGianKetThuc;
    }

    public void setThoiGianKetThuc(LocalDateTime thoiGianKetThuc) {
        this.thoiGianKetThuc = thoiGianKetThuc;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public int getSoNguoi() {
        return soNguoi;
    }

    public void setSoNguoi(int soNguoi) {
        this.soNguoi = soNguoi;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public BanAn getBan() {
        return ban;
    }

    public void setBan(BanAn ban) {
        this.ban = ban;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public double getTienCoc() {
        return tienCoc;
    }

    public void setTienCoc(double tienCoc) {
        this.tienCoc = tienCoc;
    }

    @Override
    public String toString() {
        return "PhieuDatBan{" +
                "maPhieu='" + maPhieu + '\'' +
                ", thoiGianBatDau=" + thoiGianBatDau +
                ", thoiGianKetThuc=" + thoiGianKetThuc +
                ", trangThai='" + trangThai + '\'' +
                ", soNguoi=" + soNguoi +
                ", ghiChu='" + ghiChu + '\'' +
                '}';
    }
}
