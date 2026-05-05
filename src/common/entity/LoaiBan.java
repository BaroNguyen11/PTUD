package common.entity;



public enum LoaiBan {

    VIP (450000, "VIP"),
    THUONG (300000, "Thường");

    private double tienCoc;
    private String tenLoai;

    private LoaiBan(double tienCoc, String tenLoai) {
        this.tienCoc = tienCoc;
        this.tenLoai = tenLoai;
    }

    public double getTienCoc() {
        return tienCoc;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public static LoaiBan fromString(String text) {
        if (text != null) {
            for (LoaiBan b : LoaiBan.values()) {
                if (text.equalsIgnoreCase(b.tenLoai)) {
                    return b;
                }
            }
        }
        System.err.println("Cảnh báo: Không tìm thấy LoaiBan cho chuỗi: '" + text + "'");
        return null;
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