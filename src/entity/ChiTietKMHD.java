package entity;
public class ChiTietKMHD {
    private HoaDon hoaDon;
    private KhuyenMai khuyenMai;
    private double soTienGiam;

    public ChiTietKMHD() {}

    public ChiTietKMHD(HoaDon hoaDon, KhuyenMai khuyenMai, double soTienGiam) {
        this.hoaDon = hoaDon;
        this.khuyenMai = khuyenMai;
        this.soTienGiam = soTienGiam;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
    }

    public double getSoTienGiam() {
        return soTienGiam;
    }

    public void setSoTienGiam(double soTienGiam) {
        this.soTienGiam = soTienGiam;
    }

    @Override
    public String toString() {
        return "ChiTietKMHD{" +
                "hoaDon=" + hoaDon +
                ", khuyenMai=" + khuyenMai +
                ", soTienGiam=" + soTienGiam +
                '}';
    }
}
