package entity;

public enum TrangThai {
	TRONG ("Trống"),
	DANG_SU_DUNG ("Đang sử dụng"),
	DA_DAT ("Đã đặt trước");
	
	private String thongTin;
	
	private TrangThai(String thongTin) {
		this.thongTin = thongTin;
	}
}
