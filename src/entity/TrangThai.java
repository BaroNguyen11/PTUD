package entity;

public enum TrangThai {
    TRONG("Trống"),
    DANG_SU_DUNG("Đang sử dụng"),
    DA_DAT("Đã đặt trước");

    private String thongTin;

    TrangThai(String thongTin) {
        this.thongTin = thongTin;
    }

    public String getThongTin() {
        return thongTin;
    }

    @Override
    public String toString() {
        return thongTin;
    }

    public static TrangThai fromDB(String value) {
        if (value == null) return null;

        value = value.trim().toUpperCase();

        switch (value) {
            case "TRONG":
            case "TRỐNG":
                return TRONG;

            case "DANG_SU_DUNG":
            case "ĐANG SỬ DỤNG":
            case "ĐANG DÙNG":   
                return DANG_SU_DUNG;

            case "DA_DAT":
            case "ĐÃ ĐẶT TRƯỚC":
            case "ĐANG ĐẶT":
                return DA_DAT;

            default:
                throw new IllegalArgumentException("Không có trạng thái: " + value);
        }
    }
}
