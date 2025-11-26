package entity;
public class MonAn {
    private String maMonAn;
    private String tenMonAn;
    private String loaiMon;
    private double giaTien;
    private String moTa;
    private String hinhAnh;

    public MonAn() {}

    public MonAn(String maMonAn, String tenMonAn, String loaiMon, double giaTien, String moTa) {
        this.maMonAn = maMonAn;
        this.tenMonAn = tenMonAn;
        this.loaiMon = loaiMon;
        this.giaTien = giaTien;
        this.moTa = moTa;
    }
    public MonAn(String maMonAn, String tenMonAn, String loaiMon, double giaTien, String moTa, String hinhAnh) {
        this.maMonAn = maMonAn;
        this.tenMonAn = tenMonAn;
        this.loaiMon = loaiMon;
        this.giaTien = giaTien;
        this.moTa = moTa;
        this.hinhAnh = hinhAnh;
    }
    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getMaMonAn() {
        return maMonAn;
    }

    public void setMaMonAn(String maMonAn) {
        this.maMonAn = maMonAn;
    }

    public String getTenMonAn() {
        return tenMonAn;
    }

    public void setTenMonAn(String tenMonAn) {
        this.tenMonAn = tenMonAn;
    }

    public String getLoaiMon() {
        return loaiMon;
    }

    public void setLoaiMon(String loaiMon) {
        this.loaiMon = loaiMon;
    }

    public double getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(double giaTien) {
        this.giaTien = giaTien;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    @Override
    public String toString() {
        return "MonAn{" +
                "maMonAn='" + maMonAn + '\'' +
                ", tenMonAn='" + tenMonAn + '\'' +
                ", loaiMon='" + loaiMon + '\'' +
                ", giaTien=" + giaTien +
                ", moTa='" + moTa + '\'' +
                '}';
    }
}
