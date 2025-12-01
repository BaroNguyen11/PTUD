package entity;

public enum ViTri {
    LAU_1("Lầu 1"),
    LAU_2("Lầu 2");

    private String text;

    private ViTri(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
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
