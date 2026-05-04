package client.service;

import client.RemoteServices;
import common.entity.MonAn;

import java.util.List;

public class QLMonClient {
    public List<MonAn> getDanhSachMonAn() { try { return RemoteServices.qlMon().getDanhSachMonAn(); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean insertMon(MonAn mon) { try { return RemoteServices.qlMon().insertMon(mon); } catch (Exception e) { throw new RuntimeException(e); } }
    public String taoMaMonAn() { try { return RemoteServices.qlMon().taoMaMonAn(); } catch (Exception e) { throw new RuntimeException(e); } }
    public boolean updateMon(MonAn mon) { try { return RemoteServices.qlMon().updateMon(mon); } catch (Exception e) { throw new RuntimeException(e); } }
}
