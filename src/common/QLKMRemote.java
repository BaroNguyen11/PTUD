package common;

import common.entity.KhuyenMai;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public interface QLKMRemote extends Remote {
    List<String> layTatCaKhuyenMaiStrings() throws RemoteException;
    List<String> layDanhSachMonAnStrings(LocalDate ngayBDMoi, LocalDate ngayKTMoi) throws RemoteException;
    KhuyenMai layKhuyenMaiTheoMonAn(String maMonAn) throws RemoteException;
    KhuyenMai layKhuyenMaiTheoMa(String maKhuyenMai) throws RemoteException;
    List<String> getDanhSachMonAnTheoKhuyenMai(String maKhuyenMai) throws RemoteException;
    String taoMaKhuyenMaiTuDong() throws RemoteException;
    boolean insert(KhuyenMai km) throws RemoteException;
    boolean insertCTKMMonAn(String maKhuyenMai, String maMonAn, double giaSauKM) throws RemoteException;
    boolean ngungKhuyenMai(String maKhuyenMai) throws RemoteException;
    boolean xoaCTKMMonAn(String maKM) throws RemoteException;
    boolean xoaKhuyenMai(String maKM) throws RemoteException;
    boolean updateKhuyenMai(String maKhuyenMai, String tenKhuyenMai, LocalDate ngayBatDau, LocalDate ngayKetThuc,
                            double dieuKienApDung, double giaTriToiDa, boolean giamGiaPhanTram, double giaTriGiam) throws RemoteException;
}
