package server.service.impl;

import server.core.RemoteCallLogger;
import common.QLKMRemote;
import server.dao.QLKM_DAO;
import common.entity.KhuyenMai;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.List;

public class QLKMRemoteImpl extends UnicastRemoteObject implements QLKMRemote {
    private final QLKM_DAO dao = new QLKM_DAO();

    public QLKMRemoteImpl() throws RemoteException {
    }

    public List<String> layTatCaKhuyenMaiStrings() throws RemoteException { return RemoteCallLogger.log("QLKMRemoteImpl", "layTatCaKhuyenMaiStrings", new Object[]{}, () -> dao.layTatCaKhuyenMaiStrings()); }
    public List<String> layDanhSachMonAnStrings(LocalDate ngayBDMoi, LocalDate ngayKTMoi) throws RemoteException { return RemoteCallLogger.log("QLKMRemoteImpl", "layDanhSachMonAnStrings", new Object[]{ngayBDMoi, ngayKTMoi}, () -> dao.layDanhSachMonAnStrings(ngayBDMoi, ngayKTMoi)); }
    public KhuyenMai layKhuyenMaiTheoMonAn(String maMonAn) throws RemoteException { return RemoteCallLogger.log("QLKMRemoteImpl", "layKhuyenMaiTheoMonAn", new Object[]{maMonAn}, () -> dao.layKhuyenMaiTheoMonAn(maMonAn)); }
    public KhuyenMai layKhuyenMaiTheoMa(String maKhuyenMai) throws RemoteException { return RemoteCallLogger.log("QLKMRemoteImpl", "layKhuyenMaiTheoMa", new Object[]{maKhuyenMai}, () -> dao.layKhuyenMaiTheoMa(maKhuyenMai)); }
    public List<String> getDanhSachMonAnTheoKhuyenMai(String maKhuyenMai) throws RemoteException { return RemoteCallLogger.log("QLKMRemoteImpl", "getDanhSachMonAnTheoKhuyenMai", new Object[]{maKhuyenMai}, () -> dao.getDanhSachMonAnTheoKhuyenMai(maKhuyenMai)); }
    public String taoMaKhuyenMaiTuDong() throws RemoteException { return RemoteCallLogger.log("QLKMRemoteImpl", "taoMaKhuyenMaiTuDong", new Object[]{}, () -> dao.taoMaKhuyenMaiTuDong()); }
    public boolean insert(KhuyenMai km) throws RemoteException { return RemoteCallLogger.log("QLKMRemoteImpl", "insert", new Object[]{km}, () -> dao.insert(km)); }
    public boolean insertCTKMMonAn(String maKhuyenMai, String maMonAn, double giaSauKM) throws RemoteException { return RemoteCallLogger.log("QLKMRemoteImpl", "insertCTKMMonAn", new Object[]{maKhuyenMai, maMonAn, giaSauKM}, () -> dao.insertCTKMMonAn(maKhuyenMai, maMonAn, giaSauKM)); }
    public boolean ngungKhuyenMai(String maKhuyenMai) throws RemoteException { return RemoteCallLogger.log("QLKMRemoteImpl", "ngungKhuyenMai", new Object[]{maKhuyenMai}, () -> dao.ngungKhuyenMai(maKhuyenMai)); }
    public boolean xoaCTKMMonAn(String maKM) throws RemoteException { return RemoteCallLogger.log("QLKMRemoteImpl", "xoaCTKMMonAn", new Object[]{maKM}, () -> dao.xoaCTKMMonAn(maKM)); }
    public boolean xoaKhuyenMai(String maKM) throws RemoteException { return RemoteCallLogger.log("QLKMRemoteImpl", "xoaKhuyenMai", new Object[]{maKM}, () -> dao.xoaKhuyenMai(maKM)); }
    public boolean updateKhuyenMai(String maKhuyenMai, String tenKhuyenMai, LocalDate ngayBatDau, LocalDate ngayKetThuc,
                                   double dieuKienApDung, double giaTriToiDa, boolean giamGiaPhanTram, double giaTriGiam) throws RemoteException { return RemoteCallLogger.log("QLKMRemoteImpl", "updateKhuyenMai", new Object[]{maKhuyenMai, tenKhuyenMai, ngayBatDau, ngayKetThuc, dieuKienApDung, giaTriToiDa, giamGiaPhanTram, giaTriGiam}, () -> dao.updateKhuyenMai(maKhuyenMai, tenKhuyenMai, ngayBatDau, ngayKetThuc, dieuKienApDung, giaTriToiDa, giamGiaPhanTram, giaTriGiam)); }
}
