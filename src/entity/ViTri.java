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
    
 // Map an toàn từ DB, nhiều cách viết
    public static ViTri fromDB(String value) {
        if (value == null) return null;

        value = value.trim().toUpperCase();

        switch (value) {
            case "LAU_1":
            case "LẦU_1":
            case "TẦNG 1":  
            case "TANG 1":
                return LAU_1;

            case "LAU_2":
            case "LẦU_2":
            case "TẦNG 2":   
            case "TANG 2":
                return LAU_2;

            default:
                throw new IllegalArgumentException("Không có vị trí: " + value);
        }
    }
}