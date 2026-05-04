package server;

import common.CaRemote;
import dao.Ca_DAO;
import common.entity.Ca;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;

public class CaRemoteImpl extends UnicastRemoteObject implements CaRemote {
    private final Ca_DAO dao = new Ca_DAO();

    public CaRemoteImpl() throws RemoteException {
    }

    public boolean batDauCa(double tongTienDauCa, String maNhanVien) throws RemoteException { return RemoteCallLogger.log("CaRemoteImpl", "batDauCa", new Object[]{tongTienDauCa, maNhanVien}, () -> dao.batDauCa(tongTienDauCa, maNhanVien)); }
    public boolean ketCa(String maCa, double tongTienCuoiCa) throws RemoteException { return RemoteCallLogger.log("CaRemoteImpl", "ketCa", new Object[]{maCa, tongTienCuoiCa}, () -> dao.ketCa(maCa, tongTienCuoiCa)); }
    public Ca getCaDangMo() throws RemoteException { return RemoteCallLogger.log("CaRemoteImpl", "getCaDangMo", new Object[]{}, () -> dao.getCaDangMo()); }
    public Ca getCaDangLam(String maNhanVien) throws RemoteException { return RemoteCallLogger.log("CaRemoteImpl", "getCaDangLam", new Object[]{maNhanVien}, () -> dao.getCaDangLam(maNhanVien)); }
    public double tinhTongTienMat(String maNhanVien, LocalDateTime thoiGianVaoCa) throws RemoteException { return RemoteCallLogger.log("CaRemoteImpl", "tinhTongTienMat", new Object[]{maNhanVien, thoiGianVaoCa}, () -> dao.tinhTongTienMat(maNhanVien, thoiGianVaoCa)); }
    public double tinhTongTienGiamGia(String maNhanVien, LocalDateTime thoiGianVaoCa) throws RemoteException { return RemoteCallLogger.log("CaRemoteImpl", "tinhTongTienGiamGia", new Object[]{maNhanVien, thoiGianVaoCa}, () -> dao.tinhTongTienGiamGia(maNhanVien, thoiGianVaoCa)); }
    public int demDonDangPhucVu(String maNhanVien, LocalDateTime thoiGianVaoCa) throws RemoteException { return RemoteCallLogger.log("CaRemoteImpl", "demDonDangPhucVu", new Object[]{maNhanVien, thoiGianVaoCa}, () -> dao.demDonDangPhucVu(maNhanVien, thoiGianVaoCa)); }
    public int demSoHoaDonTrongCa(String maNhanVien, LocalDateTime thoiGianVaoCa) throws RemoteException { return RemoteCallLogger.log("CaRemoteImpl", "demSoHoaDonTrongCa", new Object[]{maNhanVien, thoiGianVaoCa}, () -> dao.demSoHoaDonTrongCa(maNhanVien, thoiGianVaoCa)); }
}
