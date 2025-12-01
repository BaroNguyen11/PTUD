package entity;

public enum LoaiBan {

    VIP(450000, "VIP"),
    THUONG(300000, "Thường");

    private double tienCoc;
    private String text; // Hiển thị đẹp

    private LoaiBan(double tienCoc, String text) {
        this.tienCoc = tienCoc;
        this.text = text;
    }

    public double getTienCoc() {
        return tienCoc;
    }

    public String getText() {
        return text;
    }
    public void setTienCoc(double tienCoc) {
        this.tienCoc = tienCoc;
    }

    @Override
    public String toString() {
        return text; // Hiển thị tiếng Việt
    }

    public static LoaiBan fromDB(String value) {
        if (value == null) return null;

        value = value.trim().toUpperCase();

        switch (value) {
            case "VIP":
                return VIP;
            case "THUONG":
            case "THƯỜNG":
                return THUONG;
            default:
                throw new IllegalArgumentException("Không có loại bàn: " + value);
        }
    }
}

