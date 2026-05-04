package common.entity;

public enum TrangThai {
    TRONG ("Trống"),
    DANG_SU_DUNG ("Đang dùng"), // Chuỗi chuẩn bạn muốn
    DA_DAT ("Đã đặt");

    private String thongTin;

    TrangThai(String thongTin) {
        this.thongTin = thongTin;
    }

    public String getThongTin() {
        return thongTin;
    }

    // --- SỬA HÀM NÀY ---
    public static TrangThai fromString(String text) {
        if (text == null) return TRONG;

        String input = text.trim();

        // 1. Kiểm tra khớp với chuỗi chuẩn ("Trống", "Đang dùng", "Đã đặt")
        for (TrangThai tt : TrangThai.values()) {
            if (input.equalsIgnoreCase(tt.thongTin)) {
                return tt;
            }
        }

        // 2. BẮT THÊM CÁC TRƯỜNG HỢP NGOẠI LỆ (Do dữ liệu cũ trong DB)
        if (input.equalsIgnoreCase("Đang sử dụng")) {
            return DANG_SU_DUNG;
        }
        if (input.equalsIgnoreCase("Đã đặt trước")) {
            return DA_DAT;
        }

        // Nếu vẫn không tìm thấy, trả về TRONG thay vì null để tránh lỗi Crash ứng dụng
        System.err.println("Cảnh báo: Dữ liệu lạ '" + text + "', tự động gán về TRONG");
        return TRONG;
    }
    public static TrangThai fromDB(String value) {
        if (value == null) return null;

        String text = value.trim();

        // 1. So sánh với giá trị chuẩn ("Trống", "Đang dùng", "Đã đặt")
        for (TrangThai tt : TrangThai.values()) {
            if (text.equalsIgnoreCase(tt.thongTin)) {
                return tt;
            }
        }

        // 2. Xử lý các trường hợp ngoại lệ từ Database cũ
        if (text.equalsIgnoreCase("Đang sử dụng")) return DANG_SU_DUNG;
        if (text.equalsIgnoreCase("DANG_SU_DUNG")) return DANG_SU_DUNG;
        if (text.equalsIgnoreCase("Đã đặt trước")) return DA_DAT;
        if (text.equalsIgnoreCase("DA_DAT")) return DA_DAT;

        return null; // Hoặc return TRONG nếu muốn an toàn tuyệt đối
    }
}