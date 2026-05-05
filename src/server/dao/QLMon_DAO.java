package server.dao;

import com.mongodb.client.model.Filters;
import common.entity.MonAn;

import java.util.List;

public class QLMon_DAO extends MongoDaoSupport {

    public List<MonAn> getDanhSachMonAn() {
        return new MonAn_DAO().getAllMonAn();
    }

    public boolean insertMon(MonAn mon) {
        col("MonAn").insertOne(monAnDoc(mon));
        try {
            server.trigger.DataChangeNotifierImpl.getInstance().notifyClients("MonAn", "INSERT", mon.getMaMonAn());
        } catch (Exception e) {}
        return true;
    }

    public String taoMaMonAn() {
        return nextId("MonAn", "maMonAn", "MA", 3);
    }

    public boolean updateMon(MonAn mon) {
        boolean res = update("MonAn", Filters.eq("maMonAn", mon.getMaMonAn()), monAnDoc(mon));
        if (res) {
            try {
                server.trigger.DataChangeNotifierImpl.getInstance().notifyClients("MonAn", "UPDATE", mon.getMaMonAn());
            } catch (Exception e) {}
        }
        return res;
    }
}
