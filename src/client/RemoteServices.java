package client;

import common.*;

import java.rmi.RemoteException;

public final class RemoteServices {
    private RemoteServices() {
    }

    public static BanAnRemote banAn() { return RmiClientProvider.get("BanAnRemote", BanAnRemote.class); }
    public static CaRemote ca() { return RmiClientProvider.get("CaRemote", CaRemote.class); }
    public static CheckInRemote checkIn() { return RmiClientProvider.get("CheckInRemote", CheckInRemote.class); }
    public static ChiTietHoaDonRemote chiTietHoaDon() { return RmiClientProvider.get("ChiTietHoaDonRemote", ChiTietHoaDonRemote.class); }
    public static DangNhapRemote dangNhap() { return RmiClientProvider.get("DangNhapRemote", DangNhapRemote.class); }
    public static DashboardRemote dashboard() { return RmiClientProvider.get("DashboardRemote", DashboardRemote.class); }
    public static HoaDonRemote hoaDon() { return RmiClientProvider.get("HoaDonRemote", HoaDonRemote.class); }
    public static KhachHangRemote khachHang() { return RmiClientProvider.get("KhachHangRemote", KhachHangRemote.class); }
    public static MonAnRemote monAn() { return RmiClientProvider.get("MonAnRemote", MonAnRemote.class); }
    public static NhanVienRemote nhanVien() { return RmiClientProvider.get("NhanVienRemote", NhanVienRemote.class); }
    public static PhieuDatBanRemote phieuDatBan() { return RmiClientProvider.get("PhieuDatBanRemote", PhieuDatBanRemote.class); }
    public static QLHDRemote qlhd() { return RmiClientProvider.get("QLHDRemote", QLHDRemote.class); }
    public static QLKMRemote qlkm() { return RmiClientProvider.get("QLKMRemote", QLKMRemote.class); }
    public static QLMonRemote qlMon() { return RmiClientProvider.get("QLMonRemote", QLMonRemote.class); }
    public static QuanLyBanRemote quanLyBan() { return RmiClientProvider.get("QuanLyBanRemote", QuanLyBanRemote.class); }
    public static TaiKhoanRemote taiKhoan() { return RmiClientProvider.get("TaiKhoanRemote", TaiKhoanRemote.class); }
    public static ThanhToanRemote thanhToan() { return RmiClientProvider.get("ThanhToanRemote", ThanhToanRemote.class); }
    public static ThongKeRemote thongKe() { return RmiClientProvider.get("ThongKeRemote", ThongKeRemote.class); }

    public static RuntimeException failure(RemoteException e) {
        return new IllegalStateException("RMI service call failed", e);
    }
}
