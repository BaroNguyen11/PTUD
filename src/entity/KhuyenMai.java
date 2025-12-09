package entity;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class KhuyenMai {
    private String maKhuyenMai;
    private String tenKhuyenMai;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private double dieuKienApDung;
    private double giaTriToiDa;
    private boolean giamGiaPhanTram;
    private double giaTriGiam;
   

    public KhuyenMai() {}

    public KhuyenMai(String maKhuyenMai, String tenKhuyenMai, LocalDate ngayBatDau,
                     LocalDate ngayKetThuc, double dieuKienApDung, double giaTriToiDa,
                     boolean giamGiaPhanTram, double giaTriGiam) {
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

    public double getDieuKienApDung() {
        return dieuKienApDung;
    }

    public void setDieuKienApDung(double dieuKienApDung) {
        this.dieuKienApDung = dieuKienApDung;
    }

    public double getGiaTriToiDa() {
        return giaTriToiDa;
    }

    public void setGiaTriToiDa(double giaTriToiDa) {
        this.giaTriToiDa = giaTriToiDa;
    }

    public boolean getGiamGiaPhanTram() {
        return giamGiaPhanTram;
    }

    public void setGiamGiaPhanTram(boolean giamGiaPhanTram) {
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
    		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    		DecimalFormat dcm = new DecimalFormat("#,##0.0 VND");
    		
    		if(giamGiaPhanTram) {
    			return tenKhuyenMai + " | " +
    					"Ngày hết hạn: " + dtf.format(ngayKetThuc) + " | " +
    	                "Điều kiện: " + dieuKienApDung + " | " +
    	                "Tối đa: " + dcm.format(giaTriToiDa) + " | " +
    	                "Giảm giá: " + giaTriGiam + " %";
    		}else {
    			return tenKhuyenMai + " | " +
    					"Ngày hết hạn: " + dtf.format(ngayKetThuc) + " | " +
    	                "Điều kiện: " + dieuKienApDung + " | " +
    	                "Giảm giá: " + dcm.format(giaTriGiam);
    		}    
    }
}