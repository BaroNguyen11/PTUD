package common.entity;

public enum TrangThai {
    TRONG("Trong"),
    DANG_SU_DUNG("Dang dung"),
    DA_DAT("Da dat");

    private final String thongTin;

    TrangThai(String thongTin) {
        this.thongTin = thongTin;
    }

    public String getThongTin() {
        return thongTin;
    }

    public static TrangThai fromString(String text) {
        if (text == null) {
            return TRONG;
        }

        String normalized = normalize(text);
        for (TrangThai tt : values()) {
            if (normalize(tt.thongTin).equals(normalized) || tt.name().equalsIgnoreCase(normalized)) {
                return tt;
            }
        }

        if (normalized.equals("DANG SU DUNG")) {
            return DANG_SU_DUNG;
        }
        if (normalized.equals("DA DAT TRUOC")) {
            return DA_DAT;
        }
        return TRONG;
    }

    public static TrangThai fromDB(String value) {
        if (value == null) {
            return null;
        }

        String normalized = normalize(value);
        return switch (normalized) {
            case "TRONG" -> TRONG;
            case "DANG DUNG", "DANG SU DUNG", "DANG_SU_DUNG" -> DANG_SU_DUNG;
            case "DA DAT", "DA DAT TRUOC", "DA_DAT" -> DA_DAT;
            default -> TRONG;
        };
    }

    private static String normalize(String value) {
        return value == null ? "" : value
                .trim()
                .replace('\u0110', 'D')
                .replace('\u0111', 'd')
                .replaceAll("[_\\-]+", " ")
                .replaceAll("\\s+", " ")
                .toUpperCase();
    }
}
