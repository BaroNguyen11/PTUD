package entity;

public enum TrangThai {
    TRONG ("Trống"),
    DANG_SU_DUNG ("Đang dùng"), // Sửa CSDL hoặc Enum cho khớp ("Đang sử dụng")
    DA_DAT ("Đang đặt"); // Phải khớp CSDL ("Đã đặt bàn"?)

    private String thongTin;

    private TrangThai(String thongTin) {
        this.thongTin = thongTin;
    }

    public String getThongTin() {
        return thongTin;
    }

    public static TrangThai fromString(String text) {
        if (text != null) {
            for (TrangThai tt : TrangThai.values()) {
                // So sánh bỏ qua khoảng trắng thừa
                if (text.trim().equalsIgnoreCase(tt.thongTin)) {
                    return tt;
                }
            }
        }
        System.err.println("Cảnh báo: Không tìm thấy TrangThai cho chuỗi: '" + text + "'");
        return null; // Hoặc trả về TRONG làm mặc định
    }
}