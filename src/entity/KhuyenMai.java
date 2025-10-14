package entity;
import java.time.LocalDate;

public class KhuyenMai {
    private String maKhuyenMai;
    private String tenKhuyenMai;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private String dieuKienApDung;
    private double giaTriToiDa;
    private double giamGiaPhanTram;
    private double giaTriGiam;

    public KhuyenMai() {}

    public KhuyenMai(String maKhuyenMai, String tenKhuyenMai, LocalDate ngayBatDau,
                     LocalDate ngayKetThuc, String dieuKienApDung, double giaTriToiDa,
                     double giamGiaPhanTram, double giaTriGiam) {
        this.maKhuyenMai = maKhuyenMai;
        this.tenKhuyenMai = tenKhuyenMai;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.dieuKienApDung = dieuKienApDung;
        this.giaTriToiDa = giaTriToiDa;
        this.giamGiaPhanTram = giamGiaPhanTram;
        this.giaTriGiam = giaTriGiam;
    }

    public String getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public void setMaKhuyenMai(String maKhuyenMai) {
        this.maKhuyenMai = maKhuyenMai;
    }

    public String getTenKhuyenMai() {
        return tenKhuyenMai;
    }

    public void setTenKhuyenMai(String tenKhuyenMai) {
        this.tenKhuyenMai = tenKhuyenMai;
    }

    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getDieuKienApDung() {
        return dieuKienApDung;
    }

    public void setDieuKienApDung(String dieuKienApDung) {
        this.dieuKienApDung = dieuKienApDung;
    }

    public double getGiaTriToiDa() {
        return giaTriToiDa;
    }

    public void setGiaTriToiDa(double giaTriToiDa) {
        this.giaTriToiDa = giaTriToiDa;
    }

    public double getGiamGiaPhanTram() {
        return giamGiaPhanTram;
    }

    public void setGiamGiaPhanTram(double giamGiaPhanTram) {
        this.giamGiaPhanTram = giamGiaPhanTram;
    }

    public double getGiaTriGiam() {
        return giaTriGiam;
    }

    public void setGiaTriGiam(double giaTriGiam) {
        this.giaTriGiam = giaTriGiam;
    }

    @Override
    public String toString() {
        return "KhuyenMai{" +
                "maKhuyenMai='" + maKhuyenMai + '\'' +
                ", tenKhuyenMai='" + tenKhuyenMai + '\'' +
                ", ngayBatDau=" + ngayBatDau +
                ", ngayKetThuc=" + ngayKetThuc +
                ", dieuKienApDung='" + dieuKienApDung + '\'' +
                ", giaTriToiDa=" + giaTriToiDa +
                ", giamGiaPhanTram=" + giamGiaPhanTram +
                ", giaTriGiam=" + giaTriGiam +
                '}';
    }
}
