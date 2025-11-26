/*
 * @ (#) Ca .java     1.0     10/31/2025
 * Copyright (c) 2025 IUH.All Rights Reserved.
 */
package entity;

import java.time.LocalDateTime;

/*
 *@description: this is a class to test the Ca class
 *@author: Bao Nguyen
 *@Date: 10/31/2025
 *@version:      1.0
 */
public class Ca {
    private String maCa;
    private LocalDateTime thoiGianVaoCa;
    private LocalDateTime thoiGianKetCa;
    private double tongTienDauCa;
    private double tongTienCuoiCa;
    private NhanVien maNhanVien;

    public Ca(String maCa, LocalDateTime thoiGianVaoCa, LocalDateTime thoiGianKetCa, double tongTienDauCa, double tongTienCuoiCa, NhanVien maNhanVien) {
        this.maCa = maCa;
        this.thoiGianVaoCa = thoiGianVaoCa;
        this.thoiGianKetCa = thoiGianKetCa;
        this.tongTienDauCa = tongTienDauCa;
        this.tongTienCuoiCa = tongTienCuoiCa;
        this.maNhanVien = maNhanVien;
    }
    public Ca(String maCa, LocalDateTime thoiGianVaoCa, double tongTienDauCa, NhanVien maNhanVien) {
        this.maCa = maCa;
        this.thoiGianVaoCa = thoiGianVaoCa;
        this.tongTienDauCa = tongTienDauCa;
        this.maNhanVien = maNhanVien;
        this.thoiGianKetCa = null;
        this.tongTienCuoiCa = 0.0;
    }
    public String getMaCa() {
        return maCa;
    }

    public void setMaCa(String maCa) {
        this.maCa = maCa;
    }

    public LocalDateTime getThoiGianVaoCa() {
        return thoiGianVaoCa;
    }

    public void setThoiGianVaoCa(LocalDateTime thoiGianVaoCa) {
        this.thoiGianVaoCa = thoiGianVaoCa;
    }

    public LocalDateTime getThoiGianKetCa() {
        return thoiGianKetCa;
    }

    public void setThoiGianKetCa(LocalDateTime thoiGianKetCa) {
        this.thoiGianKetCa = thoiGianKetCa;
    }

    public double getTongTienDauCa() {
        return tongTienDauCa;
    }

    public void setTongTienDauCa(double tongTienDauCa) {
        this.tongTienDauCa = tongTienDauCa;
    }

    public double getTongTienCuoiCa() {
        return tongTienCuoiCa;
    }

    public void setTongTienCuoiCa(double tongTienCuoiCa) {
        this.tongTienCuoiCa = tongTienCuoiCa;
    }

    public NhanVien getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(NhanVien maNhanVien) {
        this.maNhanVien = maNhanVien;
    }
}
