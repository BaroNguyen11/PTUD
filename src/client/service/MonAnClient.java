package client.service;

import client.RemoteServices;
import common.entity.MonAn;

import java.time.LocalDate;
import java.util.List;

public class MonAnClient {
    public List<MonAn> getAllMonAn() { try { return RemoteServices.monAn().getAllMonAn(); } catch (Exception e) { throw new RuntimeException(e); } }
    public List<MonAn> getMonAnByLoai(String loaiMon) { try { return RemoteServices.monAn().getMonAnByLoai(loaiMon); } catch (Exception e) { throw new RuntimeException(e); } }
    public double layGiaSauKhuyenMai(String maMonAn, LocalDate ngayDat, double giaMacDinh) { try { return RemoteServices.monAn().layGiaSauKhuyenMai(maMonAn, ngayDat, giaMacDinh); } catch (Exception e) { throw new RuntimeException(e); } }
    public MonAn getMonAnByMa(String maMonAn) { try { return RemoteServices.monAn().getMonAnByMa(maMonAn); } catch (Exception e) { throw new RuntimeException(e); } }
    public String getMaMonByTen(String tenMon) { try { return RemoteServices.monAn().getMaMonByTen(tenMon); } catch (Exception e) { throw new RuntimeException(e); } }
    public List<String> layDanhSachMonAnGiaKMString() { try { return RemoteServices.monAn().layDanhSachMonAnGiaKMString(); } catch (Exception e) { throw new RuntimeException(e); } }
    public List<MonAn> timKiemMonAn(String tuKhoa) { try { return RemoteServices.monAn().timKiemMonAn(tuKhoa); } catch (Exception e) { throw new RuntimeException(e); } }
}
