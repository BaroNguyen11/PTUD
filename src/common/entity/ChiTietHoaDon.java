package common.entity;

import java.io.Serializable;

public class ChiTietHoaDon implements Serializable {
    private static final long serialVersionUID = 1L;
    private HoaDon hoaDon;
    private MonAn monAn;
    private int soLuong;

    public ChiTietHoaDon() {}

    public ChiTietHoaDon(HoaDon hoaDon, MonAn monAn, int soLuong) {
        this.hoaDon = hoaDon;
        this.monAn = monAn;
        this.soLuong = soLuong;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public MonAn getMonAn() {
        return monAn;
    }

    public void setMonAn(MonAn monAn) {
        this.monAn = monAn;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    @Override
    public String toString() {
        return "ChiTietHoaDon{" +
                "hoaDon=" + hoaDon +
                ", monAn=" + monAn +
                ", soLuong=" + soLuong +
                '}';
    }
}
