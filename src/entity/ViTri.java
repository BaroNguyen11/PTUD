package entity;

public enum ViTri {
    LAU_1 ("Tầng 1"),
    LAU_2 ("Tầng 2");

    private String tenViTri;

    private ViTri(String tenViTri) {
        this.tenViTri = tenViTri;
    }

    public String getTenViTri() {
        return tenViTri;
    }

    public static ViTri fromString(String text) {
        if (text != null) {
            for (ViTri vt : ViTri.values()) {
                // So sánh bỏ qua khoảng trắng thừa
                if (text.trim().equalsIgnoreCase(vt.tenViTri)) {
                    return vt;
                }
            }
        }
        System.err.println("Cảnh báo: Không tìm thấy ViTri cho chuỗi: '" + text + "'");
        return null; // Hoặc trả về LAU_1 làm mặc định
    }
}