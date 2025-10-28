package entity;

public enum LoaiBan {

	VIP (450000),
	THUONG (300000);
	
	private double tienCoc;

	private LoaiBan(double tienCoc) {
		this.tienCoc = tienCoc;
	}
}
