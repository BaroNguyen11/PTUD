package common.entity;

import java.io.Serializable;

public class BanAn implements Serializable {
    private static final long serialVersionUID = 1L;
    private String maBan;
    private LoaiBan loai;
    private TrangThai trangThai;
    private ViTri viTri;

    public BanAn() {}
    public BanAn(String maBan, LoaiBan loai, TrangThai trangThai, ViTri viTri) {
        this.maBan = maBan;
        this.loai = loai;
        this.trangThai = trangThai;
        this.viTri = viTri;
    }
    public String getMaBan() {
        return maBan;
    }

    public void setMaBan(String maBan) {
        this.maBan = maBan;
    }

    public LoaiBan getLoai() {
        return loai;
    }

    public void setLoai(LoaiBan loai) {
        this.loai = loai;
    }

    public TrangThai getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThai trangThai) {
        this.trangThai = trangThai;
    }

    public ViTri getViTri() {
        return viTri;
    }

    public void setViTri(ViTri viTri) {
        this.viTri = viTri;
    }

    @Override
    public String toString() {
        return "BanAn{" +
                "maBan='" + maBan + '\'' +
                ", loai='" + loai + '\'' +
                ", trangThai='" + trangThai + '\'' +
                ", viTri='" + viTri + '\'' +
                '}';
    }
}
