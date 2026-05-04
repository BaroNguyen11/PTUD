package common.entity;

import java.io.Serializable;

public class ChiTietKMMonAn implements Serializable {
    private static final long serialVersionUID = 1L;
    private MonAn monAn;
    private KhuyenMai khuyenMai;
    private double giaSauKhuyenMai;

    public ChiTietKMMonAn() {}

    public ChiTietKMMonAn(MonAn monAn, KhuyenMai khuyenMai, double giaSauKhuyenMai) {
        this.monAn = monAn;
        this.khuyenMai = khuyenMai;
        this.giaSauKhuyenMai = giaSauKhuyenMai;
    }

    public MonAn getMonAn() {
        return monAn;
    }

    public void setMonAn(MonAn monAn) {
        this.monAn = monAn;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
    }

    public double getGiaSauKhuyenMai() {
        return giaSauKhuyenMai;
    }

    public void setGiaSauKhuyenMai(double giaSauKhuyenMai) {
        this.giaSauKhuyenMai = giaSauKhuyenMai;
    }

    @Override
    public String toString() {
        return "ChiTietKMMonAn{" +
                "monAn=" + monAn +
                ", khuyenMai=" + khuyenMai +
                ", giaSauKhuyenMai=" + giaSauKhuyenMai +
                '}';
    }
}
